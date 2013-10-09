package ar.com.gl.paystadistics.services;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;
import ar.com.gl.paystadistics.domain.SantanderRioCreditCardItem;
import ar.com.gl.paystadistics.dto.CreditCardItemDTO;
import ar.com.gl.paystadistics.parser.ICreditCardItemHtmlParser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-contextTest.xml"})
public class CreditCardItemFactoryTest {

	@Autowired
	//SUD
	private CreditCardItemFactory creditCardItemFactory;
	
	private String validSantanderRioMailSample1;
	private String validSantanderRioMailSample2;
	private String invalidSantanderRioMailSample;
	
	@Before
	public void setup() throws IOException {
		//Load examples from the file system
		validSantanderRioMailSample1 = FileUtils.readFileToString(FileUtils.toFile(this.getClass().getResource("/SantanderRioMailSamples/mail1.html")));
		validSantanderRioMailSample2 = FileUtils.readFileToString(FileUtils.toFile(this.getClass().getResource("/SantanderRioMailSamples/mail2.html")));
		invalidSantanderRioMailSample = FileUtils.readFileToString(FileUtils.toFile(this.getClass().getResource("/SantanderRioMailSamples/mail3.html")));
	}

	
	@Test
	public void buildSantanderRioVisaCreditCardItemWithValidHtmlInputAndEnumKeyParam() throws IOException, MessagingException {
		
		//Mock message entity
		Message message = mock(Message.class);
		when(message.getContent()).thenReturn(validSantanderRioMailSample1);
		
		//MUD
		CreditCardItem creditCarditem = creditCardItemFactory.buildCreditCardItem(CreditCardEnum.VISA_SANTANDER_RIO, message);
		
		//Asserts
		assertEquals(creditCarditem instanceof SantanderRioCreditCardItem, true);
		assertEquals(creditCarditem.getCreditCardName(),"Tarjeta VISA");
		assertEquals(creditCarditem.getAmount(),"3261.51");
		assertEquals(creditCarditem.getExpirationDate(),"01/10/2013");
		assertEquals(creditCarditem.isAmex(),false);
	}
	
	@Test
	public void buildSantanderRioVisaCreditCardItemWithValidHtmlInputAndNoEnumKeyParam() throws IOException, MessagingException {
		
		//Mock message entity
		Message message = mock(Message.class);
		when(message.getContent()).thenReturn(validSantanderRioMailSample1);
		
		//MUD
		CreditCardItem creditCarditem = creditCardItemFactory.buildSantanderRioCreditCardItem(message);
		
		//Asserts
		assertEquals(creditCarditem instanceof SantanderRioCreditCardItem, true);
		assertEquals(creditCarditem.getCreditCardName(),"Tarjeta VISA");
		assertEquals(creditCarditem.getAmount(),"3261.51");
		assertEquals(creditCarditem.getExpirationDate(),"01/10/2013");
		assertEquals(creditCarditem.isAmex(),false);
	}		
		
	@Test
	public void buildSantanderRioAmexCreditCardItemWithValidHtmlInputAndEnumKeyParam() throws IOException, MessagingException {
		
		//Mock message entity
		Message message = mock(Message.class);
		when(message.getContent()).thenReturn(validSantanderRioMailSample2);
		
		//MUD
		CreditCardItem creditCarditem = creditCardItemFactory.buildCreditCardItem(CreditCardEnum.VISA_SANTANDER_RIO, message);
		
		//Asserts
		assertEquals(creditCarditem instanceof SantanderRioCreditCardItem, true);
		assertEquals(creditCarditem.getCreditCardName(),"Tarjeta American Express");
		assertEquals(creditCarditem.getAmount(),"693.37");
		assertEquals(creditCarditem.getExpirationDate(),"02/10/2013");
		assertEquals(creditCarditem.isAmex(),true);
	}
	
	@Test
	@ExpectedException(ar.com.gl.paystadistics.exceptions.BusinessException.class)
	public void buildSantanderRioVisaCreditCardItemWithValidInvalidHtmlInputWithoutEnumKeyParam() throws MessagingException, IOException {
		
		//Mock message entity
		Message message = mock(Message.class);
		when(message.getContent()).thenReturn(invalidSantanderRioMailSample);
		
		//MUD
		creditCardItemFactory.buildSantanderRioCreditCardItem(message);
	}
	
	@Test
	@ExpectedException(ar.com.gl.paystadistics.exceptions.BusinessException.class)
	public void buildSantanderRioCreditCardItemWithNullMessage(){
		creditCardItemFactory.buildCreditCardItem(CreditCardEnum.VISA_SANTANDER_RIO,null);
	}
	
	public void buildSantanderRioCreditCardItemWithMalFormedHtmlMessage() throws IOException, MessagingException{
		
		Message message = mock(Message.class);
		
		when(message.getContent()).thenReturn(anyObject());
		
		ICreditCardItemHtmlParser parser = mock(ICreditCardItemHtmlParser.class);
		
		when(parser.paseHTML(anyObject().toString())).thenReturn(new CreditCardItemDTO(null, null, null));
		
		creditCardItemFactory.buildCreditCardItem(CreditCardEnum.VISA_SANTANDER_RIO,message);					
		
	}
	
	
}