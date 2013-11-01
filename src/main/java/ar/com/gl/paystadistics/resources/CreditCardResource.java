package ar.com.gl.paystadistics.resources;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.gl.paystadistics.IPayStatsSystemFacade;
import ar.com.gl.paystadistics.domain.CreditCardEnum;
import ar.com.gl.paystadistics.exceptions.BusinessException;

import com.fasterxml.jackson.annotation.JsonValue;

@Service
@Path(CreditCardResource.CREDIT_CARD_URL)
public class CreditCardResource {
    
    @Autowired
    private IPayStatsSystemFacade facade;
    
    public static final String CREDIT_CARD_URL = "/creditCard";
    
    @Path("{key}/stats/")
    @PUT
    @Produces({MediaType.APPLICATION_JSON})
    public Response exportInfo(@PathParam("key") String creditCardKey) throws BusinessException {
	
	CreditCardEnum creditCard = validateKey(creditCardKey);
	
	facade.exportLastStats(creditCard);
	
	return Response.ok(creditCardKey + " info exported successfully").entity("Error during bla").build();
    }
    
    
    
    private CreditCardEnum validateKey(String creditCardKey) {
	
	try {
	     return CreditCardEnum.valueOf(creditCardKey);
	}
	  catch(IllegalArgumentException e){
	     throw new BusinessException("Invalid credit card key: " + creditCardKey,e);
	}
    }
    
    
}
