/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.node;

import uk.co.techsols.mentis.common.JobState;

/**
 *
 * @author James Bench
 */
public class JobStateMap {
    
    JobState queue;
    JobState ing;
    JobState error;
    JobState done;

    public JobStateMap(JobState queue, JobState ing, JobState error, JobState done) {
        this.queue = queue;
        this.ing = ing;
        this.error = error;
        this.done = done;
    }

    public JobState getError() {
        return error;
    }

    public JobState getIng() {
        return ing;
    }

    public JobState getQueue() {
        return queue;
    }

    public JobState getDone() {
        return done;
    }
    
}
