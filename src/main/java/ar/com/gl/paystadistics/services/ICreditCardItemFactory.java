package ar.com.gl.paystadistics.services;

import javax.mail.Message;

import ar.com.gl.paystadistics.domain.CreditCardItem;

/**
 * 
 * @author n.gonzalez
 *
 */
public interface ICreditCardItemFactory {

    /**
     * Build a <code>CreditCardItem</code> entity objecto according to the message parameter
     * @param message Representation of a mail
     * @return
     */
    CreditCardItem buildCreditCardItem(Message message);

}
