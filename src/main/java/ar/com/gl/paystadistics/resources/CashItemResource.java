package ar.com.gl.paystadistics.resources;

import java.util.Date;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.log4j.Log4j;

import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.gl.paystadistics.PayStatsSystemFacade;
import ar.com.gl.paystadistics.domain.CashPayItem;
import ar.com.gl.paystadistics.domain.PayConceptEnum;
import ar.com.gl.paystadistics.exceptions.BusinessException;

@Service
@Path(CashItemResource.CASH_URL)
@ValidateRequest
@Log4j
public class CashItemResource {

	public static final String CASH_URL = "/cash";
	
	@Autowired
	private PayStatsSystemFacade facade;

	@Path("/export")
    @PUT
    @ValidateRequest
    @Consumes({MediaType.APPLICATION_JSON})
    public Response importPayCashItem(@Valid CashPayItem payItem) throws BusinessException {
        
        log.info("Exporting cash pay item");
        
        facade.exportPayCashItem(payItem);
        
        return Response.ok(payItem).build();
    }
	
	@Path("export3")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response importPayCashItem2() {
        
        log.info("Importing cash pay item");
        
        CashPayItem item = new CashPayItem(PayConceptEnum.DRINK,new Double(123),new Date(),"Fusion");
        
        return Response.accepted(item).build();
    }

}
