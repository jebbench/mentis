/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis;

import java.text.MessageFormat;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.co.techsols.mentis.common.NodeType;
import uk.co.techsols.mentis.entities.Node;
import uk.co.techsols.mentis.job.JobManager;
import uk.co.techsols.mentis.node.NodeManager;

/**
 *
 * @author James Bench
 */
public class Manager {

    private final NodeManager transformNodeManager;
    private final NodeManager renderNodeManager;
    private final JobManager jobManager; 
    
    private final static Log LOG = LogFactory.getLog(Manager.class);
    
    private Thread renderNodesThread;
    private Thread transformNodesThread;

    public Manager(NodeManager transformNodeManager, NodeManager renderNodeManager, JobManager jobManager) {
        this.transformNodeManager = transformNodeManager;
        this.renderNodeManager = renderNodeManager;
        this.jobManager = jobManager;
    }
    
    @PostConstruct
    public void start(){
        transformNodesThread = new Thread(transformNodeManager);
        renderNodesThread = new Thread(renderNodeManager);
        
        transformNodesThread.start();
        renderNodesThread.start();
        
        if(LOG.isDebugEnabled()){
            LOG.debug("Started threads for transform and render node managers.");
        }
    }
    
    @PreDestroy
    public void stop(){
        transformNodesThread.interrupt();
        renderNodesThread.interrupt();
        
        if(LOG.isDebugEnabled()){
            LOG.debug("Sent interrupts to transform and render node managers.");
        }
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public NodeManager getTransformNodeManager() {
        return transformNodeManager;
    }

    public NodeManager getRenderNodeManager() {
        return renderNodeManager;
    }

    public NodeManager getNodeManager(NodeType type) {
        switch (type) {
            case TRANSFORM:
                return getTransformNodeManager();
            case RENDER:
                return getRenderNodeManager();
            default:
                throw new RuntimeException(MessageFormat.format("Unknown type ''{0}''.", type));
        }
    }
}
