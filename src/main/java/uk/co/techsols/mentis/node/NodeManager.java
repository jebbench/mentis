/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.node;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.techsols.mentis.UnknownTypeException;
import uk.co.techsols.mentis.entities.Job;
import uk.co.techsols.mentis.entities.Node;
import uk.co.techsols.mentis.entities.Node.Type;
import uk.co.techsols.mentis.job.JobManager;

/**
 *
 * @author James Bench
 */
public class NodeManager implements Runnable {

    private final static Log LOG = LogFactory.getLog(NodeManager.class);
    
    private final Type type;
    
    private HashMap<Long, Node> nodes = new HashMap<Long, Node>();
    private PriorityBlockingQueue<Node> availableNodes = new PriorityBlockingQueue<Node>();
    private long idIdx = 0;
    private boolean run = true;
    
    @Autowired
    private JobManager jobManager;

    public NodeManager(Type type) {
        this.type = type;
    }

    public Node createNode(int capacity) {
        Node node = new Node(getType(), ++idIdx, capacity);
        nodes.put(node.getId(), node);
        availableNodes.add(node);

        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Added {0} node with capacity of {1}. Total nodes now {2}.", getType(), capacity, idIdx));
        }

        return node;
    }

    public Type getType(){
        return type;
    }

    public Node getNode(Long id) {
        return nodes.get(id);
    }

    public void queueNode(Node node) {
        assert (node.hasFreeCapacity());
        availableNodes.add(node);
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Added {0} node with usage of {1}. Total nodes with capacity now {2}.", node.getType(), node.getUtilisation(), availableNodes.size()));
        }
    }

    @Override
    public void run() {
        while (run) {
            Node node = null;
            Job job = null;
            try {
                try {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(MessageFormat.format("Waiting for a {0} node.", getType()));
                    }
                    node = availableNodes.take();

                    if (LOG.isDebugEnabled()) {
                        LOG.debug(MessageFormat.format("Waiting for a job for {0} node {1}.", node.getType(), node.getId()));
                    }
                    job = jobManager.getNextJob(getType());

                    if (LOG.isDebugEnabled()) {
                        LOG.debug(MessageFormat.format("Processing job {0} on {1} node {2}.", job.getId(), node.getType(), node.getId()));
                    }
                    node.process(job);
                } catch (InterruptedException e) {
                    LOG.error(e);
                    if (node != null) {
                        queueNode(node);
                    }
                    if (job != null) {
                        jobManager.queueJob(getType(), job);
                    }
                }
            } catch (UnknownTypeException e) {
                LOG.error(e);
            }
        }
    }
    
}
