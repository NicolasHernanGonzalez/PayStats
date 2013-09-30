package ar.com.gl.paystadistics.services;

import ar.com.gl.paystadistics.domain.CreditCardItemDTO;

public interface ICreditCardItemHtmlParser {

	/**
	 * Parses the html in order to retrieve particular info related to credit card resume info such 
	 * as Name, expiration date, etc... 
	 * @param html Credit card info html format
	 * @return
	 */
	public CreditCardItemDTO paseHTML(String html);  

}
