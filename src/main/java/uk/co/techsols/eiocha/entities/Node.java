/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.entities;

import java.util.HashMap;
import uk.co.techsols.eiocha.Manager;
import uk.co.techsols.eiocha.UnknownTypeException;
import uk.co.techsols.eiocha.node.NodeManager;
import uk.co.techsols.eiocha.workers.Transformer;

/**
 *
 * @author James Bench
 */
public class Node {

    public enum Type {

        RENDER, TRANSFORM
    };
    private final long id;
    private final Type type;
    private final int totalCapacity;
    private final NodeManager nodeManager;
    private int usedCapacity;
    private HashMap<Long, Job> jobs = new HashMap<Long, Job>();
    private Manager manager;

    public Node(Manager manager, long id, int totalCapacity) throws UnknownTypeException {
        this.manager = manager;
        this.id = id;
        this.type = Node.Type.TRANSFORM;
        this.totalCapacity = totalCapacity;
        this.usedCapacity = 0;
        this.nodeManager = manager.getNodeManager(type);
    }

    public void setNodeManager(Manager manager) {
        this.manager = manager;
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

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public Long getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void complete(Job job) {
        jobs.remove(job.getId());
        usedCapacity--;
        if (hasFreeCapacity()) {
            nodeManager.queueNode(this);
        }
    }

    public void process(Job job) {
        jobs.put(job.getId(), job);
        job.setNode(this);
        usedCapacity++;
        if (hasFreeCapacity()) {
            nodeManager.queueNode(this);
        }

        
        // TODO: Send it to a remote worker
        Transformer transformer = new Transformer(manager);
        transformer.process(job);

    }
}
