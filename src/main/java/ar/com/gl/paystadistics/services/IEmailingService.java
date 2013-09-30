package ar.com.gl.paystadistics.services;

import java.util.Date;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;

public interface IEmailingService {

	public CreditCardItem[] retrieveEmaiIinfo(Date sinceDate, Date toDate,CreditCardEnum creditCard);
	
	public CreditCardItem[] retrieveEmaiIinfo(CreditCardEnum creditCardKey);
	
}
