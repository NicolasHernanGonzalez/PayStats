package ar.com.gl.paystadistics.domain;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Data;

@Data
/**
 * Abstraction of a pay made in cash, to be register later in some storage device or in the cloud 
 * @author n.gonzalez
 *
 */
public class CashPayItem {

    @NotNull
    private PayConceptEnum conceptEnum; 
    
    @NotNull
    @Min(value = 1)
    private Double amount;
    
    @NotNull
    @Past
    private Date date;
    
    public CashPayItem(PayConceptEnum conceptEnum, Double amount, Date date) {
        this.setConceptEnum(conceptEnum);
        this.setAmount(amount);
        this.date = date;
    }
    
    public CashPayItem(){}
    
    public void setDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.date = (((Date) sdf.parseObject(date)));
    }
}