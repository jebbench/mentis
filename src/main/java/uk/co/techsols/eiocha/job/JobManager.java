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
                throw new NotImplementedException();
            default:
                throw new UnknownTypeException(MessageFormat.format("Unknown type ''{0}''.", type));
        }
    }

    public Job getNextJob(Node.Type type) throws InterruptedException, UnknownTypeException {
        try {
            switch (type) {
                case TRANSFORM:
                    return transformQueue.take();
                case RENDER:
                    throw new NotImplementedException();
                default:
                    throw new UnknownTypeException(MessageFormat.format("Unknown type ''{0}''.", type));
            }
        } catch (InterruptedException e) {
            LOG.debug(MessageFormat.format("Interrupted while waiting for a {0} job.", type), e);
            throw e;
        }
    }
}
