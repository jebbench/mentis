/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.node;

import uk.co.techsols.mentis.entities.Job.State;

/**
 *
 * @author James Bench
 */
public class JobStateMap {
    
    State queue;
    State ing;
    State error;
    State done;

    public JobStateMap(State queue, State ing, State error, State done) {
        this.queue = queue;
        this.ing = ing;
        this.error = error;
        this.done = done;
    }

    public State getError() {
        return error;
    }

    public State getIng() {
        return ing;
    }

    public State getQueue() {
        return queue;
    }

    public State getDone() {
        return done;
    }
    
}
