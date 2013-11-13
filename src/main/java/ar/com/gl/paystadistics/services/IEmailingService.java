package ar.com.gl.paystadistics.services;

import java.util.Date;
import java.util.Map;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardBillItem;

public interface IEmailingService {

    Map<CreditCardEnum,CreditCardBillItem> retrieveEmaiIinfo(Date sinceDate, Date toDate, CreditCardEnum creditCardKey);
    
    Map<CreditCardEnum,CreditCardBillItem> retrieveEmaiIinfo(Date sinceDate, Date toDate,CreditCardEnum[] creditCard);
    
    Map<CreditCardEnum,CreditCardBillItem> retrieveEmailLastinfo(CreditCardEnum creditCardKey);
    
    Map<CreditCardEnum,CreditCardBillItem> retrieveEmailLastinfo(CreditCardEnum[] creditCardKeys);
    
}
