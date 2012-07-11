/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.workers;

import uk.co.techsols.eiocha.Manager;
import uk.co.techsols.eiocha.entities.Job;

/**
 *
 * @author james.bench
 */
public abstract class Worker implements Runnable {

    Manager manager;
    Job job;
    
    public Worker(Manager manager) {
        this.manager = manager;
    }
    
    public void process(Job job) {
        this.job = job;
        new Thread(this).start();
    }
}
