package ar.com.gl.paystadistics;

import java.util.Map;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.domain.CreditCardItem;
import ar.com.gl.paystadistics.services.IEmailingService;
import ar.com.gl.paystadistics.services.IStatsExporter;


@Qualifier("defaultFacade")
@Service
@Log
/**
 * @inheritDoc
 * 
 */
public class PayStatsSystemFacade implements IPayStatsSystemFacade {

    @Autowired
    private IEmailingService emailingService;
    
    @Autowired
    private IStatsExporter statsExporter;
    
    
    public void exportLastStats(CreditCardEnum[] creditCardKeys) {
        
        log.info("exporting stats for  " + creditCardKeys);
        
        emailingService.retrieveEmailLastinfo(creditCardKeys);
        
        log.info("Stats exported successfully!");
    }
    
    public void exportLastStats() {
        
        log.info("exporting stats for all configured credit cards...");
        
        emailingService.retrieveEmailLastinfo(CreditCardEnum.getAllCreditCardsEnumAsArray());
        
        log.info("Stats exported successfully!");
    }
    
    public void exportLastStats(CreditCardEnum creditCardKey) {
        
        log.info("Retrieving info for " + creditCardKey.toString());
        
        CreditCardEnum[] creditCardKeys = new CreditCardEnum[1];
        creditCardKeys[0] = creditCardKey;
        
        Map<CreditCardEnum,CreditCardItem> stats = emailingService.retrieveEmailLastinfo(creditCardKeys);
        
        log.info("exporting stats");
        
        statsExporter.exportStats(stats);
        
        log.info("Stats exported successfully!");
    }
}