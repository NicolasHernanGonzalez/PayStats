package ar.com.gl.paystadistics.services;

import java.util.Map;

import javax.annotation.Resource;
import javax.mail.Message;

import lombok.extern.log4j.Log4j;

import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;
import ar.com.gl.paystadistics.domain.SantanderRioCreditCardItem;

@Component
@Log4j
public class CreditCardItemFactory {
    
    @Resource
    private Map<CreditCardEnum,ICreditCardItemFactory> creditCardsItemFactories;
    
    
    public CreditCardItem buildCreditCardItem(CreditCardEnum creditCardKey,Message message) {
        
        log.debug("Choosing apropiate factory to build a CreditCardItem...");
        
        return creditCardsItemFactories.get(creditCardKey).buildCreditCardItem(message);
    }
    
    public CreditCardItem buildSantanderRioCreditCardItem(Message message) {
        
        log.info("Building SantanderRioItem...");
        
        CreditCardItem item = new SantanderRioCreditCardItem(message);
        
        log.debug("Build Success");
        
        return item;
    }
}