/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.techsols.mentis.rest.converters;

import java.util.ArrayList;
import uk.co.techsols.mentis.common.JobTemplate;
import uk.co.techsols.mentis.common.NodeTemplate;
import uk.co.techsols.mentis.entities.Job;
import uk.co.techsols.mentis.entities.Node;

/**
 *
 * @author james.bench
 */
public class NodeConverter {
    
    private JobConverter jobConverter;

    public void setJobConverter(JobConverter jobConverter) {
        this.jobConverter = jobConverter;
    }
    
    public NodeTemplate convert(Node node){
        NodeTemplate nt = new NodeTemplate();
        nt.id = node.getId();
        nt.totalCapacity = node.getTotalCapacity();
        nt.usedCapacity = node.getUsedCapacity();
        nt.type = node.getType();
        
        nt.jobs = new ArrayList<JobTemplate>(node.getJobs().size());
        
        for(Job j : node.getJobs().values()){
            nt.jobs.add(jobConverter.convert(j));
        }
        
        return nt;
    }
    
}
