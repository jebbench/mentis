/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.entities;

import java.util.HashMap;
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
    private int usedCapacity;
    private HashMap<Long, Job> jobs = new HashMap<Long, Job>();
    private NodeManager nodeManager;

    public Node(NodeManager nodeManager, long id, int totalCapacity) {
        this.nodeManager = nodeManager;
        this.id = id;
        this.type = nodeManager.getType();
        this.totalCapacity = totalCapacity;
        this.usedCapacity = 0;
    }

    public void setNodeManager(NodeManager nodeManager) {
        this.nodeManager = nodeManager;
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
        usedCapacity++;
        if (hasFreeCapacity()) {
            nodeManager.queueNode(this);
        }

        Transformer transformer = new Transformer();
        transformer.process(job);

    }
}
