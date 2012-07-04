/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.entities;

/**
 *
 * @author James Bench
 */
public class Job {
    
    public enum State {NEW, TQUEUE, TING, TERROR, RQUEUE, RING, RERROR, DONE, ERROR};
    
    private Long id;
    private State state = State.NEW;
    private Node node;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    
    
    
}
