package ar.com.gl.paystadistics;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ar.com.gl.paystadistics.domain.CreditCardEnum;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/application-contextTest.xml"})
public class PayStatsSysteFacadeTest {

    @Autowired
    private PayStatsSystemFacade facade;
    
//    @Test
    public void functionalTest(){
        facade.exportLastStats(CreditCardEnum.AMEX_SANTANDER_RIO);
    }

}
