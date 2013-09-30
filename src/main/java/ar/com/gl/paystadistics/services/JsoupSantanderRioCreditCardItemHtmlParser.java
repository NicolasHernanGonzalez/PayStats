/**
 * 
 */
package ar.com.gl.paystadistics.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import ar.com.gl.paystadistics.domain.CreditCardItemDTO;

/**
 * Jsoup implementation for <code>ICreditCardItemHtmlParser</code> that have the responsibility for interpreting 
 * and parsing credit card html in SantanderRio format
 * @author n.gonzalez
 *
 */
public class JsoupSantanderRioCreditCardItemHtmlParser implements ICreditCardItemHtmlParser {

	/* (non-Javadoc)
	 * @see ar.com.gl.paystadistics.services.ICreditCardItemHtmlParser#paseHTML(java.lang.String)
	 */
	@Override
	public CreditCardItemDTO paseHTML(String htmlMail) {
		
		Document parsedDocument = Jsoup.parse(htmlMail);

		Element element = parsedDocument.getElementsByClass("info").first();

		//TODO Search the way to improve this shit, maybe searching in all tree looking for a key word such as "Fecha de Vencimiento"
		Node nameNode = element.childNode(0).childNode(0).childNode(0).childNode(0).childNode(0);

		Node expirationDateNode = element.childNode(0).childNode(2).childNode(1).childNode(0).childNode(0);

		Node amountNode = element.childNode(0).childNode(3).childNode(1).childNode(0).childNode(0);

		String amount = amountNode.toString();

		amount = amount.replace("$&nbsp;", "").replace(".", "").replace(",", ".");
		
		CreditCardItemDTO creditCardItemDTO = new CreditCardItemDTO(amount,expirationDateNode.toString() , nameNode.toString()); 
		
		return creditCardItemDTO;
	}

}
