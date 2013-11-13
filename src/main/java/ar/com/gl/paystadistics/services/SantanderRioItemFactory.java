package ar.com.gl.paystadistics.services;

import javax.mail.Message;

import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardBillItem;
import ar.com.gl.paystadistics.domain.SantanderRioCreditCardBillItem;

/**
 * 'Santander Rio' implementation of <code>ICreditCardItemFactory</code> that knows how to build a <code>CreditCardItem</codes> 
 * from a mail with a 'Santander Rio' mail format and styles.
 * @author n.gonzalez
 *
 */
@Log4j
@Component
public class SantanderRioItemFactory implements ICreditCardItemFactory {

    /**
     * @inherited
     */
    @Override
    public CreditCardBillItem buildCreditCardItem(Message message) {
        
        log.info("Building SantanderRioItem...");
        
        CreditCardBillItem item = new SantanderRioCreditCardBillItem(message);
        
        log.debug("Build Success");
        
        return item;
    }
}
