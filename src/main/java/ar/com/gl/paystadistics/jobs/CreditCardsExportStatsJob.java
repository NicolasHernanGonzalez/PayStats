package ar.com.gl.paystadistics.jobs;

import java.util.ArrayList;
import java.util.Collection;

import lombok.extern.java.Log;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import ar.com.gl.paystadistics.IPayStatsSystemFacade;
import ar.com.gl.paystadistics.domain.CreditCardEnum;

@Log
public class CreditCardsExportStatsJob extends QuartzJobBean {

    private IPayStatsSystemFacade payStatsSystemFacade;
    
    private Collection<CreditCardEnum> creditCardKeys = new ArrayList<CreditCardEnum>();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        
        log.info("Job stating to run...");
        
        this.payStatsSystemFacade.exportLastStats((creditCardKeys.toArray(new CreditCardEnum[creditCardKeys.size()])));
        
        log.info("Successfull!");
    }

    
    /**
     * @param payStatsSystemFacade the payStatsSystemFacade to set
     */
    public void setPayStatsSystemFacade(IPayStatsSystemFacade payStatsSystemFacade) {
        this.payStatsSystemFacade = payStatsSystemFacade;
    }

    
    /**
     * @param creditCardKeys the creditCardKeys to set
     */
    public void setCreditCardKeys(String creditCardKeys) {
    
        String[] splitedKeys = creditCardKeys.split(",");
        for (String splitedKey : splitedKeys) {
            this.creditCardKeys.add(CreditCardEnum.valueOf(splitedKey));
        }
    }

 }