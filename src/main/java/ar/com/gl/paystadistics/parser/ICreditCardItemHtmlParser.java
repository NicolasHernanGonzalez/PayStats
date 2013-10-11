package ar.com.gl.paystadistics.parser;

import ar.com.gl.paystadistics.dto.CreditCardItemDTO;

public interface ICreditCardItemHtmlParser {

    /**
     * Parses the html in order to retrieve particular info related to credit card resume info such 
     * as Name, expiration date, etc... 
     * @param html Credit card info html format
     * @return
     */
    CreditCardItemDTO paseHTML(String html);  

}
