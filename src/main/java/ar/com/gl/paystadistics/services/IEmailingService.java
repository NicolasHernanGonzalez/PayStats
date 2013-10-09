package ar.com.gl.paystadistics.services;

import java.util.Date;
import java.util.Map;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;

public interface IEmailingService {

	Map<CreditCardEnum,CreditCardItem> retrieveEmaiIinfo(Date sinceDate, Date toDate, CreditCardEnum creditCardKey);
	
	Map<CreditCardEnum,CreditCardItem> retrieveEmaiIinfo(Date sinceDate, Date toDate,CreditCardEnum[] creditCard);
	
	Map<CreditCardEnum,CreditCardItem> retrieveEmailLastinfo(CreditCardEnum creditCardKey);
	
	Map<CreditCardEnum,CreditCardItem> retrieveEmailLastinfo(CreditCardEnum[] creditCardKeys);
	
}
