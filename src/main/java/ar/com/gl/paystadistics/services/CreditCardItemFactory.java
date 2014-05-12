package ar.com.gl.paystadistics.services;

import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message;

import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardBillItem;
import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.SantanderRioCreditCardBillItem;

@Component
@Log4j
public class CreditCardItemFactory {
    
    @Resource
    private Map<CreditCardEnum,ICreditCardItemFactory> creditCardsItemFactories;
    
    
    public CreditCardBillItem buildCreditCardItem(CreditCardEnum creditCardKey,Message message) {
        
        log.debug("Choosing apropiate factory to build a CreditCardItem...");
        
        return creditCardsItemFactories.get(creditCardKey).buildCreditCardItem(message);
    }
    
    public CreditCardBillItem buildSantanderRioCreditCardItem(Message message) {
        
        log.info("Building SantanderRioItem...");
        
        CreditCardBillItem item = new SantanderRioCreditCardBillItem(message);
        
        log.debug("Build Success");
        
        return item;
    }
}