package ar.com.gl.paystadistics.services;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.AndTerm;
import javax.mail.search.BodyTerm;
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
import ar.com.gl.paystadistics.dto.CreditCardMailSearchCriteriaDTO;
import ar.com.gl.paystadistics.exceptions.BusinessException;

/**
 * Class responsible for connecting to an email service provider, which is configured 
 * by properties, and then retrieving all the business info needed 
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
    
    @Resource
    private Map<CreditCardEnum,CreditCardMailSearchCriteriaDTO> creditCardsMailSearchCriteria;
    
    /**
     * Use as default date parameters the last days of the past month, and the first days of the current one.
     * @param creditCardKey
     * @return
     */
    public Map<CreditCardEnum,CreditCardItem> retrieveEmailLastinfo(CreditCardEnum[] creditCardKeys) {
        
        Calendar calendar = getCalendar();
        
        //SinceDate
        calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(this.getFirstDayMonth()));  
        calendar.add(Calendar.DATE,Integer.valueOf(this.getSinceDeltaDays()));
        Date sinceDate = calendar.getTime();
       
        //ToDate
        calendar = getCalendar();
        calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(this.getToDeltaDays()));
        Date toDate = calendar.getTime();
        
        return retrieveEmaiIinfo(sinceDate, toDate, creditCardKeys);
    }
    
    /**
     * Designed only for testing purpose
     * @return
     */
    protected Calendar getCalendar(){
        return Calendar.getInstance();
    }
    
    
    /**
     * Adapter to retrieveEmaiIinfo, making possible a easier invocation sign
     */
    public Map<CreditCardEnum,CreditCardItem> retrieveEmaiIinfo(Date sinceDate, Date toDate, CreditCardEnum creditCardKey) {
        return retrieveEmaiIinfo(sinceDate,toDate,buildEnum(creditCardKey));
    }
    
    public Map<CreditCardEnum,CreditCardItem> retrieveEmaiIinfo(Date sinceDate, Date toDate, CreditCardEnum[] creditCardKeys) {
         
        HashMap<CreditCardEnum,CreditCardItem> creditCardItems = new HashMap<CreditCardEnum,CreditCardItem>();
        
            try {
                    Store store = initStore();
                    
                    store.connect(mailHost,userName,password);
 
                    //get inbox folder
                    Folder myBankFolder = store.getFolder(mailInboxFolder);
 
                    //open folder only to read
                    myBankFolder.open(Folder.READ_ONLY);
                    
                    for (CreditCardEnum creditCardKey : creditCardKeys) {
                        
                        //Retrieve the key words to build the filters
                        CreditCardMailSearchCriteriaDTO searchCriteria = creditCardsMailSearchCriteria.get(creditCardKey);
                        
                        //Build all the filters according to the credit card
                        SearchTerm customSearchTerm = buildFilters(sinceDate, toDate,searchCriteria);
                        
                        Message[] messages = myBankFolder.search(customSearchTerm);
                        
                        verifyMessages(messages);
                        
                        // messages[0] <-- Shouldn't have more than one message, but it is possible depending on the search criterias
                        creditCardItems.put(creditCardKey,cardItemFactory.buildCreditCardItem(creditCardKey, messages[0]));
                    }
                    
                    myBankFolder.close(true);
 
                    store.close();
 
                    return creditCardItems;
            
            }
            
            catch( NoSuchProviderException nspe) {
                throw new BusinessException("Couldn't connect to mail service provider",nspe);
            }
            
            catch( MessagingException me) {
                throw new BusinessException("Couldn't parser credit card htlm mail",me);
            }
    }
    
    private void verifyMessages(Message[] messages) {
        if (messages == null || messages.length == 0){
            throw new BusinessException("No messages found with the given search criteria");
        }
    }

    protected CreditCardEnum[] buildEnum(CreditCardEnum creditCardKey){
        CreditCardEnum[] creditCardKeys = new CreditCardEnum[1];
        creditCardKeys[0] = creditCardKey;
        return creditCardKeys;
    }
    
    @Override
    public Map<CreditCardEnum, CreditCardItem> retrieveEmailLastinfo(CreditCardEnum creditCardKey) {
        return retrieveEmailLastinfo(buildEnum(creditCardKey));
    }

    private SearchTerm buildFilters(Date sinceDate, Date toDate,CreditCardMailSearchCriteriaDTO searchCriteria) {
        
        SearchTerm olderThan = new ReceivedDateTerm(ComparisonTerm.LT,toDate);
        SearchTerm newerThan = new ReceivedDateTerm(ComparisonTerm.GT, sinceDate);
        SearchTerm subjectPattern = new SubjectTerm(searchCriteria.getMailSubjectKey());
        SearchTerm flagTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), true);
        SearchTerm bodyTerm = new BodyTerm(searchCriteria.getMailBodyKey());
        
        SearchTerm[] searchTerms = new SearchTerm[5];
        searchTerms[0] = subjectPattern;
        searchTerms[1] = flagTerm;
        searchTerms[2] = bodyTerm;
        searchTerms[3] = olderThan;
        searchTerms[4] = newerThan;
        
        SearchTerm customSearchTerm = new AndTerm(searchTerms);
        
        return customSearchTerm;
    }

    protected Store initStore() throws NoSuchProviderException {
        
        Properties properties = new Properties();
 
        properties.setProperty("mail.store.protocol",storeProtocol);
        
        Session session = Session.getDefaultInstance(properties, null);
        
        Store store = session.getStore(storeProtocol);
        
        return store;
    }
}
