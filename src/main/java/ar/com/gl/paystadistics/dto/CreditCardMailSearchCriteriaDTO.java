package ar.com.gl.paystadistics.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.stereotype.Component;

/**
 * Representation of a credit card search criteria. Each credit card have their own keys words that helps the parser to retrieve the info. This key words could change in the 
 * future, so they are set up by Spring and loaded from a properties file to make easy changing them.
 * @author n.gonzalez
 *
 */
@Component
@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreditCardMailSearchCriteriaDTO {
    private String mailSubjectKey,mailBodyKey;

    public CreditCardMailSearchCriteriaDTO() {}
    
    
}
