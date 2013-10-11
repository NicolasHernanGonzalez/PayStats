package ar.com.gl.paystadistics;

import java.util.Map;

import lombok.extern.java.Log;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;
import ar.com.gl.paystadistics.services.IEmailingService;
import ar.com.gl.paystadistics.services.IStatsExporter;

@Component
@Log
public class PayStatsSystemFacade {

    @Autowired
    private IEmailingService emailingService;
    
    @Autowired
    private IStatsExporter statsExporter;
    
    public void exportLastStats(CreditCardEnum[] creditCardKey) {
        
        log.info("exporting stats...");
        
        for (CreditCardEnum creditCard : creditCardKey) {
            log.info("exporting " + creditCard.toString() + " stats");
            exportLastStats(creditCard);
        }
    }
    
    public void exportLastStats() {
        log.info("Retrieving info for all configured credit cards");
        throw new NotImplementedException();
    }
    
    public void exportLastStats(CreditCardEnum creditCardKey) {
        
        log.info("Retrieving info for " + creditCardKey.toString());
        
        CreditCardEnum[] creditCardKeys = new CreditCardEnum[1];
        creditCardKeys[0] = creditCardKey;
        
        Map<CreditCardEnum,CreditCardItem> stats = emailingService.retrieveEmailLastinfo(creditCardKeys);
        
        log.info("exporting stats");
        
        statsExporter.exportStats(stats);
    }
}