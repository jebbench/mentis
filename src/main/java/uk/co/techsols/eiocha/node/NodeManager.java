/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.node;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import uk.co.techsols.eiocha.UnknownTypeException;
import uk.co.techsols.eiocha.entities.Job;
import uk.co.techsols.eiocha.entities.Node;
import uk.co.techsols.eiocha.entities.Node.Type;
import uk.co.techsols.eiocha.job.JobManager;

/**
 *
 * @author James Bench
 */
public abstract class NodeManager implements Runnable {

    private final static Log LOG = LogFactory.getLog(NodeManager.class);
    private HashMap<Long, Node> nodes = new HashMap<Long, Node>();
    private PriorityBlockingQueue<Node> availableNodes = new PriorityBlockingQueue<Node>();
    private long count = 0;
    private boolean run = true;
    private JobManager jobManager;
    private final JobStateMap jobStateMap;

    public NodeManager(JobManager jobManager, JobStateMap jobStateMap) {
        this.jobManager = jobManager;
        this.jobStateMap = jobStateMap;
        
        new Thread(this).start();
    }

    public Node createNode(int capacity) {
        Node node = new Node(this, ++count, capacity);
        nodes.put(node.getId(), node);
        availableNodes.add(node);

        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Added {0} node with capacity of {1}. Total nodes now {2}.", getType(), capacity, count));
        }

        return node;
    }

    public abstract Type getType();

    public Node getNode(Long id) {
        return nodes.get(id);
    }

    public void queueNode(Node node) {
        assert (node.hasFreeCapacity());
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Added {0} node with capacity of {1}. Total nodes with capacity now {2}.", node.getType(), node.getCurrentCapacity(), availableNodes.size()));
        }
        availableNodes.add(node);
    }

    public void completeJob(Job job) {
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
                    job.setState(jobStateMap.ing);
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
