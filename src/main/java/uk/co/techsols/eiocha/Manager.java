/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha;

import org.springframework.beans.factory.annotation.Autowired;
import uk.co.techsols.eiocha.job.JobManager;
import uk.co.techsols.eiocha.node.TransformNodeManager;

/**
 *
 * @author James Bench
 */

public class Manager {
    
    @Autowired
    TransformNodeManager transformNodeManager;
    
    @Autowired
    JobManager jobManager;

    public Manager() {
    }

    public JobManager getJobManager() {
        return jobManager;
    }
    
}
