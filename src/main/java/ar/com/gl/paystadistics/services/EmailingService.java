package ar.com.gl.paystadistics.services;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.ComparisonTerm;
import javax.mail.search.FlagTerm;
import javax.mail.search.ReceivedDateTerm;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;

import lombok.Getter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;
import ar.com.gl.paystadistics.exceptions.BusinessException;

/**
 * Class responsible for connecting to an email service provider, which is configured 
 * by properties, and retrieving business info 
 * @author n.gonzalez
 *
 */
@Component
public class EmailingService implements IEmailingService {
	
	@Value("${mail.storeProtocol}")
	private String storeProtocol;

	@Value("${mail.userName}")
	private String userName;

	@Value("${mail.password}")
	private String password;

	@Value("${mail.host}")
	private String mailHost;

	@Value("${mail.inboxFolder}")
	private String mailInboxFolder;

	@Value("${mail.subjectPattern}")
	private String mailSubjectPattern;
	
	@Value("${mail.sinceDeltaDays}")
	@Getter
	private String sinceDeltaDays;
	
	@Value("${mail.toDeltaDays}")
	@Getter
	private String toDeltaDays;
	
	@Value("${mail.firstDayMonth}")
	@Getter
	private String firstDayMonth;
	
	@Autowired
	private CreditCardItemFactory cardItemFactory;
	
	/**
	 * Use as default date parameters the last days of the past month, and the first days of the current one.
	 * @param creditCardKey
	 * @return
	 */
	public CreditCardItem[] retrieveEmaiIinfo(CreditCardEnum creditCardKey) {
		
		Calendar calendar = getCalendar();
		
        //SinceDate
		calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(this.getFirstDayMonth()));  
        calendar.add(Calendar.DATE,Integer.valueOf(this.getSinceDeltaDays()));
        Date sinceDate = calendar.getTime();
       
        //ToDate
        calendar = getCalendar();
        calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(this.getToDeltaDays()));
        Date toDate = calendar.getTime();
		
        return retrieveEmaiIinfo(sinceDate, toDate, creditCardKey);
	}
	
	/**
	 * Designed only for testing purpose
	 * @return
	 */
	protected Calendar getCalendar(){
		return Calendar.getInstance();
	}
	
	
	public CreditCardItem[] retrieveEmaiIinfo(Date sinceDate, Date toDate, CreditCardEnum creditCardKey) {
		 
        CreditCardItem[] creditCardItems = new CreditCardItem[2];
		
            try {
            		Store store = initStore();
            		
            		store.connect(mailHost,userName,password);
 
                    Folder myBankFolder = store.getFolder(mailInboxFolder);//get inbox folder
 
                    myBankFolder.open(Folder.READ_ONLY);//open folder only to read
            		
                    SearchTerm[] searchTerms = buildFilters(sinceDate, toDate);
            		
            		SearchTerm customSearchTerm = new AndTerm(searchTerms);
            		
            		Message[] messages = myBankFolder.search(customSearchTerm);
            		
            		creditCardItems[0] = cardItemFactory.buildCreditCardItem(creditCardKey, messages[0]);
            		
            		creditCardItems[1] = cardItemFactory.buildCreditCardItem(creditCardKey, messages[1]);
            		
                    myBankFolder.close(true);
 
                    store.close();
 
                    return creditCardItems;
            
            }
            catch( NoSuchProviderException nspe) {
            	throw new BusinessException("Couldn't connect to mail service provider");
            }
            catch( MessagingException me) {
            	throw new BusinessException("Couldn't parser credit card htlm mail");
            }
            
    }


	private SearchTerm[] buildFilters(Date sinceDate, Date toDate) {
		
		SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT,toDate);
		SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, sinceDate);
		SearchTerm subjectPattern = new SubjectTerm(mailSubjectPattern);
		SearchTerm flagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), true);
		
		SearchTerm[] searchTerms = new SearchTerm[4];
		searchTerms[0] = olderThan;
		searchTerms[1] = newerThan;
		searchTerms[2] = subjectPattern;
		searchTerms[3] = flagTerm;
		return searchTerms;
	}

	private Store initStore() throws NoSuchProviderException {
		
		Properties properties = new Properties();
 
        properties.setProperty("mail.store.protocol",storeProtocol);
        
        Session session = Session.getDefaultInstance(properties, null);
        
        Store store = session.getStore(storeProtocol);
		
        return store;
	}
}
