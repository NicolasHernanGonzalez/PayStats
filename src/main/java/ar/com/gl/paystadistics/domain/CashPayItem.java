package ar.com.gl.paystadistics.domain;

import java.text.ParseException;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Getter;
import lombok.Setter;

/**
 * Abstraction of a pay made in cash, to be register later in some storage device or in the cloud 
 * @author n.gonzalez
 *
 */
public class CashPayItem {

    @NotNull
    @Getter
    @Setter
    private PayConceptEnum conceptEnum; 
    
    @NotNull
    @Min(value = 1)
    @Getter
    @Setter
    private Double amount;
    
    @NotNull
    @Past
    @Getter
    private Date date;
    
    @Setter
    @Getter
    private String comment;
    
    public CashPayItem(PayConceptEnum conceptEnum, Double amount, Date date,String comment) {
        this.conceptEnum = conceptEnum ;
        this.amount = amount;
        this.date = date;
        this.comment = comment;
    }
    
    public CashPayItem(){}
    
    public void setDate(String date) throws ParseException {
        this.date = (new Date(Long.valueOf(date)));
    }
    
    
}