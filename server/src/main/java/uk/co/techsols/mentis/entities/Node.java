/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.entities;

import java.text.MessageFormat;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.techsols.mentis.Manager;
import uk.co.techsols.mentis.node.JobStateMap;

/**
 *
 * @author James Bench
 */
public class Node implements Comparable<Node> {

    public enum Type {
        RENDER, TRANSFORM
    };
    
    public static final String MESSAGE_JOBID = "JobId";
    private final static Log LOG = LogFactory.getLog(Node.class);
    private final long id;
    private final Type type;
    private final int totalCapacity;
    private final JobStateMap jobStateMap;
    private int usedCapacity;
    private HashMap<Long, Job> jobs = new HashMap<Long, Job>();
    
    @Autowired
    private Manager manager;

    public Node(Type type, long id, int totalCapacity) {
        this.id = id;
        this.type = type;
        this.totalCapacity = totalCapacity;
        this.usedCapacity = 0;
        switch (this.type) {
            case RENDER:
                this.jobStateMap = new JobStateMap(Job.State.RQUEUE, Job.State.RING, Job.State.RERROR, Job.State.RDONE);
                break;
            case TRANSFORM:
                this.jobStateMap = new JobStateMap(Job.State.TQUEUE, Job.State.TING, Job.State.TERROR, Job.State.TDONE);
                break;
            default:
                throw new RuntimeException(MessageFormat.format("Unknown type ''{0}''.", type));
        }

        LOG.info(MessageFormat.format("Created {0} node {1} with capacity {2}.", type, id, totalCapacity));
    }

    @Override
    public int compareTo(Node o) {
        return this.getFreeCapacity() > o.getFreeCapacity() ? 1 : this.getFreeCapacity() < o.getFreeCapacity() ? -1 : 0;
    }

    public boolean hasFreeCapacity() {
        return (usedCapacity < totalCapacity);
    }

    public float getUtilisation() {
        return (usedCapacity / totalCapacity) * 100;
    }

    public int getUsedCapacity() {
        return usedCapacity;
    }

    public int getFreeCapacity() {
        return totalCapacity - usedCapacity;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public Long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    private void increaseUsedCapacity() {
        usedCapacity++;
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("{0} node {1} increased used capacity to {2} of {3}.", type, id, usedCapacity, totalCapacity));
        }
        if (hasFreeCapacity()) {
            manager.getNodeManager(type).queueNode(this);
        }
    }

    private void decreaseUsedCapacity() {
        usedCapacity--;
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("{0} node {1} decreased used capacity to {2} of {3}.", type, id, usedCapacity, totalCapacity));
        }
        if (hasFreeCapacity()) {
            manager.getNodeManager(type).queueNode(this);
        }
    }

    private void removeJob(Job job) {
        jobs.remove(job.getId());
        decreaseUsedCapacity();
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("{0} node {1} removed job {2}.", type, id, job.getId()));
        }
    }

    public void complete(Job job) {
        job.setState(jobStateMap.getDone());
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("{0} node {1} completed job {2}.", type, id, job.getId()));
        }
        removeJob(job);
    }
    
    public void error(Job job) {
        job.setState(jobStateMap.getError());
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("{0} node {1} errored job {2}.", type, id, job.getId()));
        }
        removeJob(job);
    }

    public void process(Job job) {
        jobs.put(job.getId(), job);
        job.setNode(this);
        increaseUsedCapacity();
        job.setState(jobStateMap.getIng());

        // TODO - send job to node

        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Send message to {0} node {1} to process job {2}.", type, id, job.getId()));
        }
    }
}