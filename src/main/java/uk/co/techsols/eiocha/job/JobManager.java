/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.job;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.co.techsols.eiocha.UnknownTypeException;
import uk.co.techsols.eiocha.entities.Job;
import uk.co.techsols.eiocha.entities.Node;

/**
 *
 * @author James Bench
 */
@Component
@Scope(value = "singleton")
public class JobManager {

    private final static Log LOG = LogFactory.getLog(JobManager.class);
    private HashMap<Long, Job> jobs = new HashMap<Long, Job>();
    private PriorityBlockingQueue<Job> transformQueue = new PriorityBlockingQueue<Job>();
    private PriorityBlockingQueue<Job> renderQueue = new PriorityBlockingQueue<Job>();
    private long count = 0;

    public JobManager() {
    }

    public Job createJob() {
        Job job = new Job();
        job.setId(++count);
        jobs.put(job.getId(), job);
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Created job {0}.", job.getId()));
        }
        addJobToTransformQueue(job);
        return job;
    }
    
    public void completeTransform(Job job){
        // TODO: check job has XSL-FO
        job.getNode().complete(job);
        addJobToRenderQueue(job);
    }
    
    public void completeRender(Job job){
        //TODO: check job has PDF
        job.getNode().complete(job);
        job.setState(Job.State.DONE);
    }
    
    public void errorTransform(Job job, String error){
        errorJob(Node.Type.TRANSFORM, job, error);
    }
    
    public void errorRender(Job job, String error){
        errorJob(Node.Type.RENDER, job, error);
    }
    
    public void addJobToRenderQueue(Job job) {
        // TODO: Check the job has XSL-FO
        job.setState(Job.State.RQUEUE);
        renderQueue.add(job);
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Added job {0} to the RENDER queue.", job.getId()));
        }
    }

    public void addJobToTransformQueue(Job job) {
        // TODO: Check the job has XSL and XML
        job.setState(Job.State.TQUEUE);
        transformQueue.add(job);
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Added job {0} to the TRANSFORM queue.", job.getId()));
        }
    }

    public void queueJob(Node.Type type, Job job) throws UnknownTypeException {
        switch (type) {
            case TRANSFORM:
                addJobToTransformQueue(job);
            case RENDER:
                addJobToRenderQueue(job);
            default:
                throw new UnknownTypeException(MessageFormat.format("Unknown type ''{0}''.", type));
        }
    }
    
    public void completeJob(Node.Type type, Job job) throws UnknownTypeException {
        switch (type) {
            case TRANSFORM:
                completeTransform(job);
            case RENDER:
                completeRender(job);
            default:
                throw new UnknownTypeException(MessageFormat.format("Unknown type ''{0}''.", type));
        }
    }
    
    public void errorJob(Node.Type type, Job job, String error) {
        switch (type) {
            case TRANSFORM:
                job.setState(Job.State.TERROR);
            case RENDER:
                job.setState(Job.State.RERROR);
            default:
                job.setState(Job.State.ERROR);
        }
        job.setError(error);
    }
    
    public void errorJob(Job job, String error) {
        errorJob(null, job, error);
    }

    public Job getNextJob(Node.Type type) throws InterruptedException, UnknownTypeException {
        try {
            switch (type) {
                case TRANSFORM:
                    return transformQueue.take();
                case RENDER:
                    return renderQueue.take();
                default:
                    throw new UnknownTypeException(MessageFormat.format("Unknown type ''{0}''.", type));
            }
        } catch (InterruptedException e) {
            LOG.debug(MessageFormat.format("Interrupted while waiting for a {0} job.", type), e);
            throw e;
        }
    }
}
