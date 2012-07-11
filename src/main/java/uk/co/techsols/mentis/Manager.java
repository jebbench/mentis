/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis;

import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.techsols.mentis.entities.Node;
import uk.co.techsols.mentis.job.JobManager;
import uk.co.techsols.mentis.node.NodeManager;
import uk.co.techsols.mentis.node.RenderNodeManager;
import uk.co.techsols.mentis.node.TransformNodeManager;

/**
 *
 * @author James Bench
 */
public class Manager {

    @Autowired
    TransformNodeManager transformNodeManager;
    @Autowired
    RenderNodeManager renderNodeManager;
    @Autowired
    JobManager jobManager;

    public Manager() {
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public TransformNodeManager getTransformNodeManager() {
        return transformNodeManager;
    }

    public RenderNodeManager getRenderNodeManager() {
        return renderNodeManager;
    }

    public NodeManager getNodeManager(Node.Type type) throws UnknownTypeException {
        switch (type) {
            case TRANSFORM:
                return getTransformNodeManager();
            case RENDER:
                return getRenderNodeManager();
            default:
                throw new UnknownTypeException(MessageFormat.format("Unknown type ''{0}''.", type));
        }
    }
}
