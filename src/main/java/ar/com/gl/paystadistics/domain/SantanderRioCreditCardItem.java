package ar.com.gl.paystadistics.domain;

import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;

import ar.com.gl.paystadistics.dto.CreditCardItemDTO;
import ar.com.gl.paystadistics.exceptions.BusinessException;
import ar.com.gl.paystadistics.parser.ICreditCardItemHtmlParser;
import ar.com.gl.paystadistics.parser.JsoupSantanderRioCreditCardItemHtmlParser;

/**
 * Particular "Santander Rio" class of Abstract class <code>CreditCard</code> that knows 
 * how to parse info from "Santander Rio" email message 
 * @author n.gonzalez
 */
public class SantanderRioCreditCardItem extends CreditCardItem {

    //TODO Desacoplar dominio y java mail
    public SantanderRioCreditCardItem(Message message) {
        super(message,new JsoupSantanderRioCreditCardItemHtmlParser());
    }

    /**
     * Responsible to populate all class attributes from a Santander Rio email
     */
    @Override
    void parseInfo(Message message,ICreditCardItemHtmlParser parser) {

       try {
          
            String htmlMail = (String) message.getContent();

            CreditCardItemDTO cardItemDTO = parser.paseHTML(htmlMail); 
 
            this.setExpirationDate(cardItemDTO.getExpirationDate());
            this.setCreditCardName(cardItemDTO.getCreditCardName());
            this.setAmount(cardItemDTO.getAmount());
        }

        catch (IOException | MessagingException e) {
            throw new BusinessException("Could not get content from mail message",e);
        }

        catch (Exception  e) {
            throw new BusinessException("Fatal error parsing html mail content",e);
        }
    }
}
