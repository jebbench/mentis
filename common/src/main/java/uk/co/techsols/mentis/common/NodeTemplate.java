/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.techsols.mentis.common;

import java.util.ArrayList;

/**
 *
 * @author James Bench
 */
public class NodeTemplate {
    public NodeType type;
    public int totalCapacity;
    public int usedCapacity;
    public long id;
    public ArrayList<JobTemplate> jobs;
}
