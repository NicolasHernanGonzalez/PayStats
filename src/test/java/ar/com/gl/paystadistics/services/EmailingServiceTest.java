package ar.com.gl.paystadistics.services;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.mail.NoSuchProviderException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-contextTest.xml"})
@Configurable
public class EmailingServiceTest {

	@Resource
	private IEmailingService emailingService;
	
	@Value("${mail.sinceDeltaDays}")
	private String sinceDeltaDays;
	
	@Value("${mail.toDeltaDays}")
	private String toDeltaDays;
	
	@Value("${mail.firstDayMonth}")
	private String firstDayMonth;
	
//	@Test
	public void functionalTest() throws NoSuchProviderException {
		
		Calendar calendar = Calendar.getInstance();
		
        //SinceDate
		Date currentDate = new Date();
		calendar.setTime(currentDate);  
	    calendar.add(Calendar.DATE, -6);
        Date sinceDate = calendar.getTime();
       
        //ToDate
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,4);
        Date toDate = calendar.getTime();

		CreditCardItem[] creditCardItems = emailingService.retrieveEmaiIinfo(sinceDate,toDate,CreditCardEnum.SANTANDER_RIO);
		
		for (CreditCardItem creditCardItem : creditCardItems) {
			System.out.println(creditCardItem);
			System.out.println(creditCardItem.isAmex());
		}
	}
	
	@Test
	public void testInvokeRetrieveEmailInfoWithoutParameters() {
		
		//Create Mock
		EmailingService service = mock(EmailingService.class);
		
		Date current = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(current);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(firstDayMonth));
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		
		//Build SinceDate which might be used in SUD
		calendar.add(Calendar.DATE,Integer.valueOf(sinceDeltaDays));
		Date sinceDate = calendar.getTime();

		//Build ToDate which might be used in SUD
		calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,Integer.valueOf(toDeltaDays));
        calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
        Date toDate = calendar.getTime();
        
		//Doing this cause the nature of the calendar (Singleton)
        calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
		
        Calendar otherCalendarInstance = Calendar.getInstance();
        otherCalendarInstance.set(Calendar.MINUTE,0);
		otherCalendarInstance.set(Calendar.SECOND,0);
		/*------------------------------------------------------*/
		
		//Do the mocking
        when(service.getFirstDayMonth()).thenReturn(firstDayMonth);
		when(service.getSinceDeltaDays()).thenReturn(sinceDeltaDays);
		when(service.getToDeltaDays()).thenReturn(toDeltaDays);
        when(service.retrieveEmaiIinfo((Date)anyObject(), (Date)anyObject(),(CreditCardEnum)anyObject())).thenReturn(null);
        when(service.getCalendar()).thenReturn(calendar,otherCalendarInstance);
 		when(service.retrieveEmaiIinfo(CreditCardEnum.SANTANDER_RIO)).thenCallRealMethod();
        
		//MUT
		service.retrieveEmaiIinfo(CreditCardEnum.SANTANDER_RIO);
		
		//verifying
		verify(service).retrieveEmaiIinfo(CreditCardEnum.SANTANDER_RIO);
		verify(service).retrieveEmaiIinfo(sinceDate, toDate, CreditCardEnum.SANTANDER_RIO);
	}
	
}
