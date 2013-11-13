package ar.com.gl.paystadistics.domain;

import lombok.Data;

/**
 * Abstraction of a purchase made in any way
 * @author n.gonzalez
 *
 */
@Data
public abstract class PayItem {
    
    
    
    abstract void export();
}