/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.entities;

import java.io.File;
import java.text.MessageFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author James Bench
 */
public class Job {
    
    public enum State {NEW, TQUEUE, TING, TERROR, RQUEUE, RING, RERROR, DONE, ERROR};
    
    private final static Log LOG = LogFactory.getLog(Job.class);
    
    private File dataDirectory;
    private Long id;
    private State state = State.NEW;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    
    
    
}
