package ar.com.gl.paystadistics.domain;

import static junit.framework.Assert.assertEquals;

import java.util.Calendar;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.BeforeClass;
import org.junit.Test;

public class CashItemTest {

    private static Validator validator;
    
    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void nonPastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + 1);
        
        CashPayItem cashPayItem = new CashPayItem(PayConceptEnum.DRINK,324.03,calendar.getTime());
        Set<ConstraintViolation<CashPayItem>> constraintViolations = validator.validate( cashPayItem );

        assertEquals( 1, constraintViolations.size() );
        assertEquals( "must be in the past", constraintViolations.iterator().next().getMessage() );
    }
    
    @Test
    public void nullAmount() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 10, 11,calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE) - 1);
        
        CashPayItem cashPayItem = new CashPayItem(PayConceptEnum.DRINK,null,calendar.getTime());
        Set<ConstraintViolation<CashPayItem>> constraintViolations = validator.validate( cashPayItem );

        assertEquals( 1, constraintViolations.size() );
        assertEquals( "may not be null", constraintViolations.iterator().next().getMessage() );
    }
    
    @Test
    public void nullConcept() {
        
        Calendar calendar = Calendar.getInstance();
        calendar.set(2013, 10, 11,calendar.get(Calendar.HOUR),calendar.get(Calendar.MINUTE) - 1);
        
        CashPayItem cashPayItem = new CashPayItem(null,321.5,calendar.getTime());
        Set<ConstraintViolation<CashPayItem>> constraintViolations = validator.validate( cashPayItem );

        assertEquals( 1, constraintViolations.size() );
        assertEquals( "may not be null", constraintViolations.iterator().next().getMessage() );
    }
}
