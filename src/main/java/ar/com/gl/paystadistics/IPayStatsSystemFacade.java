package ar.com.gl.paystadistics;

import org.springframework.stereotype.Component;

import ar.com.gl.paystadistics.domain.CreditCardEnum;


@Component
/**
 * All exposed system functionality
 * @author n.gonzalez
 *
 */
public interface IPayStatsSystemFacade {

    /**
     * Retrieve credit cards info, according with the enums parameter, then such info is will be exported to same place depending of the implementation (The default one is Google doc) 
     * @param creditCardKey
     */
    void exportLastCreditCardStats(CreditCardEnum[] creditCardKey);
    
    
    /**
     * Retrieve all configured credit cards(see gralConfig.properties) configured info then such info is will be exported to same place depending of the implementation (The default one is Google doc) 
     * @param creditCardKey
     */
    void exportLastCreditCardStats();
    
    /**
     * Retrieve credit cards info, according with the enum parameter, then such info is will be exported to same place depending of the implementation (The default one is Google doc) 
     * @param creditCardKey
     */
    void exportLastCreditCardStats(CreditCardEnum creditCardKey);

}
