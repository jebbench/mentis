/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.techsols.eiocha.job.JobManager;
import uk.co.techsols.eiocha.entities.Job;

/**
 *
 * @author James Bench
 */
@Controller
@RequestMapping("/job")
public class JobController {
    
    JobManager jobManager;

    @Autowired
    public JobController(JobManager jobManager) {
        this.jobManager = jobManager;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addJob(){
        Job job = jobManager.createJob();
        return new ResponseEntity<String>(job.getId().toString(), HttpStatus.CREATED);
    }
    
}
