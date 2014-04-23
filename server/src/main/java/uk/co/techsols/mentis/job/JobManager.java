/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.job;

import java.io.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.concurrent.PriorityBlockingQueue;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.techsols.mentis.UnknownTypeException;
import uk.co.techsols.mentis.common.NodeType;
import uk.co.techsols.mentis.entities.Job;
import uk.co.techsols.mentis.entities.Node;

/**
 *
 * @author James Bench
 */
public class JobManager {

    private final static Logger LOG = LoggerFactory.getLogger(JobManager.class);
    private final HashMap<Long, Job> jobs = new HashMap<Long, Job>();
    private final PriorityBlockingQueue<Job> transformQueue = new PriorityBlockingQueue<Job>();
    private final PriorityBlockingQueue<Job> renderQueue = new PriorityBlockingQueue<Job>();
    
    private long count = 0;
    
    // Set by context
    private File jobDataDirectory;

    public void setJobDataDirectory(File jobDataDirectory) {
        this.jobDataDirectory = jobDataDirectory;
        if (!this.jobDataDirectory.exists()) {
            LOG.info(MessageFormat.format("Creating directory ''{0}''.", this.jobDataDirectory.getAbsolutePath()));
            if (!this.jobDataDirectory.mkdirs()) {
                RuntimeException e = new RuntimeException(MessageFormat.format("Failed to create directory ''{0}''; check path and permissions.", this.jobDataDirectory.getAbsolutePath()));
                LOG.error(e.getLocalizedMessage(), e);
                throw e;
            }
        }
    }

    private File getJobFile(Job job, String type) {
        return new File(job.getDataDirectory(), type);
    }

    public void saveFile(Job job, byte[] content, String type) throws IOException {
        File file = getJobFile(job, type);
        try {
            IOUtils.write(content, new FileOutputStream(file));
        } catch (IOException e) {
            LOG.error(MessageFormat.format("Encountered and error while trying to save file ''{0}''.", file.getAbsolutePath()), e);
            throw e;
        }
    }

    public Job createJob(byte[] xml, byte[] xsl) {
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

    public void completeTransform(Job job, byte[] xslfo) {
        try {
            saveFile(job, xslfo, "xslfo");
            job.getNode().complete(job);
            addJobToRenderQueue(job);
        } catch (IOException e) {
            errorJob(job, e.getLocalizedMessage());
        }
    }

    public void completeRender(Job job, byte[] pdf) {
        try {
            saveFile(job, pdf, "pdf");
            job.getNode().complete(job);
            job.setState(Job.State.DONE);
        } catch (IOException e) {
            errorJob(job, e.getLocalizedMessage());
        }
    }

    public void errorTransform(Job job, String error) {
        errorJob(NodeType.TRANSFORM, job, error);
    }

    public void errorRender(Job job, String error) {
        errorJob(NodeType.RENDER, job, error);
    }

    public void addJobToRenderQueue(Job job) {
        if (!getJobFile(job, "xslfo").exists()) {
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
        if (!getJobFile(job, "xml").exists() || !getJobFile(job, "xsl").exists()) {
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

    public void queueJob(NodeType type, Job job) throws UnknownTypeException {
        switch (type) {
            case TRANSFORM:
                addJobToTransformQueue(job);
            case RENDER:
                addJobToRenderQueue(job);
            default:
                throw new UnknownTypeException(MessageFormat.format("Unknown type ''{0}''.", type));
        }
    }

    public void errorJob(NodeType type, Job job, String error) {
        switch (type) {
            case TRANSFORM:
                job.setState(Job.State.TERROR);
            case RENDER:
                job.setState(Job.State.RERROR);
            default:
                job.setState(Job.State.ERROR);
        }
        try {
            saveFile(job, error.getBytes(), "error");
        } catch (IOException e) {
            LOG.error("Failed to save error message.", e);
            throw new RuntimeException(e);
        }
    }

    public void errorJob(Job job, String error) {
        errorJob(null, job, error);
    }

    public Job getNextJob(NodeType type) throws InterruptedException, UnknownTypeException {
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
