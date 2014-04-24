/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.entities;

import java.io.File;
import java.text.MessageFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.techsols.mentis.common.JobState;

/**
 *
 * @author James Bench
 */
public class Job {
    
    private final static Logger LOG = LoggerFactory.getLogger(Job.class);
    
    private File dataDirectory;
    private Long id;
    private JobState state = JobState.NEW;
    private Node node;

    public void setDataDirectory(File dataDirectory) {
        this.dataDirectory = dataDirectory;
        if(!this.dataDirectory.isDirectory()){
            LOG.debug(MessageFormat.format("Creating job data directory ''{0}'' for job ''{1}''.", this.dataDirectory.getAbsolutePath(), id));
            if(!this.dataDirectory.mkdirs()){
                LOG.error(MessageFormat.format("Error creating job data directory ''{0}'' for job ''{1}''.", this.dataDirectory.getAbsolutePath(), id));
            }
        }
    }

    public File getDataDirectory() {
        return dataDirectory;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    
    
    
}
