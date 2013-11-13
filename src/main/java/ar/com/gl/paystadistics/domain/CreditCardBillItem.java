package ar.com.gl.paystadistics.domain;

import javax.mail.Message;

import lombok.Data;
import lombok.NonNull;
import ar.com.gl.paystadistics.exceptions.BusinessException;
import ar.com.gl.paystadistics.parser.ICreditCardItemHtmlParser;

/**
 * Template class responsible for providing the common class skeleton for all implementations delegating in the subclasses parsing mails behavior;
 * @author n.gonzalez
 *
 */
@Data
public abstract class CreditCardBillItem {

    public static final  String AMEX = "American Express";

    @NonNull
    private String creditCardName;

    @NonNull
    private String expirationDate;
    
    @NonNull
    private String amount;
  
    public CreditCardBillItem(Message message,ICreditCardItemHtmlParser parser) {
        
        if (message == null) {
            throw new BusinessException("The message cannot be null");
        }
 
        parseInfo(message,parser);
        
    }

    abstract void parseInfo(Message message, ICreditCardItemHtmlParser parser) ;

    public boolean isAmex() {
        return this.creditCardName.contains(AMEX);
    }
    
}
