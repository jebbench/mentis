/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.techsols.mentis.rest.converters;

import uk.co.techsols.mentis.common.JobTemplate;
import uk.co.techsols.mentis.entities.Job;

/**
 *
 * @author james.bench
 */
public class JobConverter {
    
    public JobTemplate convert(Job job){
        JobTemplate jt = new JobTemplate();
        jt.id = job.getId();
        jt.state = job.getState();
        return jt;
    }
    
}
