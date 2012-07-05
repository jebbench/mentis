/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.workers;

import org.springframework.beans.factory.annotation.Autowired;
import uk.co.techsols.eiocha.entities.Job;
import uk.co.techsols.eiocha.job.JobManager;

/**
 *
 * @author james.bench
 */
public abstract class Worker implements Runnable {

    @Autowired
    JobManager jobManager;
    Job job;
    
    public void process(Job job) {
        this.job = job;
        new Thread(this).start();
    }
    
    protected abstract void complete();
    protected abstract void error(String error);
}
