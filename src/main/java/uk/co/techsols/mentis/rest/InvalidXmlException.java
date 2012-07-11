/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.rest;

/**
 *
 * @author james.bench
 */
public class InvalidXmlException extends Exception {

    /**
     * Creates a new instance of
     * <code>InvlalidXmlException</code> without detail message.
     */
    public InvalidXmlException() {
    }

    /**
     * Constructs an instance of
     * <code>InvlalidXmlException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidXmlException(String msg) {
        super(msg);
    }
}
