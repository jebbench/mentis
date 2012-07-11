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
public class RenderNodeManager extends NodeManager {

    @Autowired
    public RenderNodeManager(Manager manager) {
        super(manager, new JobStateMap(Job.State.RQUEUE, Job.State.RING, Job.State.RERROR));
    }

    @Override
    public final Node.Type getType() {
        return Node.Type.RENDER;
    }
}
