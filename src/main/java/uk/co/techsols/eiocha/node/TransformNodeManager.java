/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.node;

import org.springframework.beans.factory.annotation.Autowired;
import uk.co.techsols.eiocha.Manager;
import uk.co.techsols.eiocha.entities.Job;
import uk.co.techsols.eiocha.entities.Node;

/**
 *
 * @author James Bench
 */
public class TransformNodeManager extends NodeManager {

    @Autowired
    public TransformNodeManager(Manager manager) {
        super(manager, new JobStateMap(Job.State.TQUEUE, Job.State.TING, Job.State.TERROR));
    }

    @Override
    public final Node.Type getType() {
        return Node.Type.TRANSFORM;
    }
}
