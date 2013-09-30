package ar.com.gl.paystadistics.services;

import javax.mail.Message;

import ar.com.gl.paystadistics.domain.CreditCardItem;

public interface ICreditCardItemFactory {

	public CreditCardItem buildCreditCardItem(Message message);

}
