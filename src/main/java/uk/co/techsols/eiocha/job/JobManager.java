/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.job;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.techsols.eiocha.Manager;
import uk.co.techsols.eiocha.UnknownTypeException;
import uk.co.techsols.eiocha.entities.Job;
import uk.co.techsols.eiocha.entities.Node;

/**
 *
 * @author James Bench
 */
public class JobManager {

    private final static Log LOG = LogFactory.getLog(JobManager.class);
    private final Manager manager;
    private final HashMap<Long, Job> jobs = new HashMap<Long, Job>();
    private final PriorityBlockingQueue<Job> transformQueue = new PriorityBlockingQueue<Job>();
    private final PriorityBlockingQueue<Job> renderQueue = new PriorityBlockingQueue<Job>();
    private File jobDataDirectory;
    private long count = 0;

    @Autowired
    public JobManager(Manager manager) {
        this.manager = manager;
    }

    public void setJobDataDirectory(File jobDataDirectory) {
        this.jobDataDirectory = jobDataDirectory;
        if (!this.jobDataDirectory.exists()) {
            LOG.info(MessageFormat.format("Creating directory ''{0}''.", this.jobDataDirectory.getAbsolutePath()));
            if (!this.jobDataDirectory.mkdirs()) {
                RuntimeException e = new RuntimeException(MessageFormat.format("Failed to create directory ''{0}''; check path and permissions.", this.jobDataDirectory.getAbsolutePath()));
                LOG.fatal(e);
                throw e;
            }
        }
    }

    private File getJobFile(Job job, String type){
        return new File(job.getDataDirectory(), type);
    }
    
    public void saveFile(Job job, byte[] content, String type) throws IOException {
        File file = getJobFile(job, type);
        IOUtils.write(content, new FileOutputStream(file));
    }

    public Job createJob(byte[] xsl, byte[] xml) {
        Job job = new Job();
        job.setId(++count);
        try {
            job.setDataDirectory(new File(jobDataDirectory, job.getId().toString()));

            saveFile(job, xml, "xml");
            saveFile(job, xsl, "xsl");

            jobs.put(job.getId(), job);
            if (LOG.isDebugEnabled()) {
                LOG.debug(MessageFormat.format("Created job {0}.", job.getId()));
            }
            addJobToTransformQueue(job);
        } catch (IOException e) {
            errorJob(job, e.getLocalizedMessage());
        }
        return job;
    }

    public void completeTransform(Job job) {
        if(!getJobFile(job, "xslfo").exists()){
            String message = MessageFormat.format("Tried to complete the transform of job {0} without XSL-FO.", job.getId());
            errorJob(job, message);
            throw new RuntimeException(message);
        }
        job.getNode().complete(job);
        addJobToRenderQueue(job);
    }

    public void completeRender(Job job) {
        if(!getJobFile(job, "pdf").exists()){
            String message = MessageFormat.format("Tried to complete the render of job {0} without PDF.", job.getId());
            errorJob(job, message);
            throw new RuntimeException(message);
        }
        job.getNode().complete(job);
        job.setState(Job.State.DONE);
    }

    public void errorTransform(Job job, String error) {
        errorJob(Node.Type.TRANSFORM, job, error);
    }

    public void errorRender(Job job, String error) {
        errorJob(Node.Type.RENDER, job, error);
    }

    public void addJobToRenderQueue(Job job) {
        if(!getJobFile(job, "xslfo").exists()){
            String message = MessageFormat.format("Tried to add job {0} to the render queue without XSL-FO.", job.getId());
            errorJob(job, message);
            throw new RuntimeException(message);
        }
        job.setState(Job.State.RQUEUE);
        renderQueue.add(job);
        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Added job {0} to the RENDER queue.", job.getId()));
        }
    }

    public void addJobToTransformQueue(Job job) {
        if(!getJobFile(job, "xml").exists() || !getJobFile(job, "xsl").exists()){
            String message = MessageFormat.format("Tried to add job {0} to the transform queue without XML or XSL.", job.getId());
            errorJob(job, message);
            throw new RuntimeException(message);
        }
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
