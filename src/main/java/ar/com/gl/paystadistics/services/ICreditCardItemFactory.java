package ar.com.gl.paystadistics.services;

import javax.mail.Message;

import ar.com.gl.paystadistics.domain.CreditCardItem;

public interface ICreditCardItemFactory {

    CreditCardItem buildCreditCardItem(Message message);

}
