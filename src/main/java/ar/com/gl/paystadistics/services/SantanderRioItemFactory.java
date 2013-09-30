package ar.com.gl.paystadistics.services;

import javax.mail.Message;

import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardItem;
import ar.com.gl.paystadistics.domain.SantanderRioCreditCardItem;

/**
 * 'Santander Rio' implementation of <code>ICreditCardItemFactory</code> that knows how to build a <code>CreditCardItem</codes> 
 * from a mail with the 'Santander Rio' mail format and styles.
 * @author n.gonzalez
 *
 */
@Log4j
@Component
public class SantanderRioItemFactory implements ICreditCardItemFactory {

	@Override
	public CreditCardItem buildCreditCardItem(Message message) {
		
		log.info("Building SantanderRioItem...");
		
		CreditCardItem item = new SantanderRioCreditCardItem(message);
		
		log.debug("Build Success");
		
		return item;
	}

}
