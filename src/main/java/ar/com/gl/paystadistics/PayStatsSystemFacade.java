package ar.com.gl.paystadistics;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

import lombok.extern.java.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ar.com.gl.paystadistics.domain.CashPayItem;
import ar.com.gl.paystadistics.domain.CreditCardBillItem;
import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.exceptions.BusinessException;
import ar.com.gl.paystadistics.services.IEmailingService;
import ar.com.gl.paystadistics.services.IStatsExporter;

import com.google.gdata.util.ServiceException;


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
    
    
    public void exportLastCreditCardStats(CreditCardEnum[] creditCardKeys) {
        
        log.info("exporting stats for  " + creditCardKeys);
        
        emailingService.retrieveEmailLastinfo(creditCardKeys);
        
        log.info("Stats exported successfully!");
    }
    
    public void exportLastCreditCardStats() {
        
        log.info("exporting stats for all configured credit cards...");
        
        emailingService.retrieveEmailLastinfo(CreditCardEnum.getAllCreditCardsEnumAsArray());
        
        log.info("Stats exported successfully!");
    }
    
    public void exportLastCreditCardStats(CreditCardEnum creditCardKey) {
        
        log.info("Retrieving info for " + creditCardKey.toString());
        
        CreditCardEnum[] creditCardKeys = new CreditCardEnum[1];
        creditCardKeys[0] = creditCardKey;
        
        Map<CreditCardEnum,CreditCardBillItem> stats = emailingService.retrieveEmailLastinfo(creditCardKeys);
        
        log.info("exporting stats");
        
        statsExporter.exportStats(stats);
        
        log.info("Stats exported successfully!");
    }
    
    public void exportPayCashItem(CashPayItem item) {
        
    	log.info("Exporting cash payments, into default SpreedSheet...");
    	
    	try {
			
    		this.statsExporter.exportCashPayment(item,"Personal Business Stuff");
		} 
    	  catch (IOException | ServiceException e) {
    		  log.log(Level.SEVERE,"Error happened during insertion of a new row");
    		  throw new BusinessException(e.getMessage());
		} 
    }

}