/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.node;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import uk.co.techsols.eiocha.entities.Job;
import uk.co.techsols.eiocha.entities.Node;
import uk.co.techsols.eiocha.job.JobManager;

/**
 *
 * @author James Bench
 */
@Component
@Scope(value = "singleton")
public class TransformNodeManager extends NodeManager {

    @Autowired
    public TransformNodeManager(JobManager jobManager) {
        super(jobManager, new JobStateMap(Job.State.TQUEUE, Job.State.TING, Job.State.TERROR));
    }

    @Override
    public final Node.Type getType() {
        return Node.Type.TRANSFORM;
    }
}
