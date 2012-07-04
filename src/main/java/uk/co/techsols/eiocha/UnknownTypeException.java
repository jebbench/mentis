/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha;

/**
 *
 * @author James Bench
 */
public class UnknownTypeException extends Exception {

    /**
     * Creates a new instance of
     * <code>UnknownTypeException</code> without detail message.
     */
    public UnknownTypeException() {
    }

    /**
     * Constructs an instance of
     * <code>UnknownTypeException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public UnknownTypeException(String msg) {
        super(msg);
    }
}
