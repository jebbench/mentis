/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.node;

import uk.co.techsols.mentis.entities.Job;
import uk.co.techsols.mentis.entities.Job.State;

/**
 *
 * @author James Bench
 */
public class JobStateMap {
    
    Job.State queue;
    Job.State ing;
    Job.State error;

    public JobStateMap(State queue, State ing, State error) {
        this.queue = queue;
        this.ing = ing;
        this.error = error;
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
    
}
