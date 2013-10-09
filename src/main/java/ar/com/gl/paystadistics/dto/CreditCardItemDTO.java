/**
 * 
 */
package ar.com.gl.paystadistics.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.sun.istack.NotNull;


/**
 * Simple plain object representation of a credit card item.
 * @author n.gonzalez
 *
 */
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CreditCardItemDTO {

    @NotNull
    @Getter
    private String amount,expirationDate,creditCardName;

}
