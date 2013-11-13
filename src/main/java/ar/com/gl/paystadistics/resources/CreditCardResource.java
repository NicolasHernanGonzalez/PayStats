package ar.com.gl.paystadistics.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.java.Log;

import org.jboss.resteasy.plugins.validation.hibernate.ValidateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.gl.paystadistics.IPayStatsSystemFacade;
import ar.com.gl.paystadistics.domain.CashPayItem;
import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.exceptions.BusinessException;

@Service
@Path(CreditCardResource.CREDIT_CARD_URL)
@Log
public class CreditCardResource {
    
    @Autowired
    private IPayStatsSystemFacade facade;
    
    public static final String CREDIT_CARD_URL = "/creditCard";
    
    @Path("{key}/stats/")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    public Response exportInfo(@PathParam("creditCardKey") String creditCardKey) throws BusinessException {
        
        CreditCardEnum creditCard = validateKey(creditCardKey);
        
        facade.exportLastCreditCardStats(creditCard);
        
        return Response.ok("creditCardKey" + " info exported successfully").entity("Error during bla").build();
    }
    
    private CreditCardEnum validateKey(String creditCardKey) {
        
        try {
             return CreditCardEnum.valueOf(creditCardKey);
        }
          catch(IllegalArgumentException e) {
             throw new BusinessException("Invalid credit card key: " + creditCardKey,e);
        }
    }
    
    @Path("creditCard/")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response importCreditCardPayItem(CashPayItem payItem) throws BusinessException {
        log.info("Importing cash pay item");
        return null;
    }
    
    @Path("cash/")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    @ValidateRequest
    public Response importPayCashItem(@Valid CashPayItem payItem) throws BusinessException {
        
        log.info("Importing cash pay item");
        
        
        
        return Response.ok().build();
    }
    
}