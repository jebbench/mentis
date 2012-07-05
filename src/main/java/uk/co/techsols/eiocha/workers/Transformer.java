/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.workers;

import java.text.MessageFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author james.bench
 */
public class Transformer extends Worker {

    private final static Log LOG = LogFactory.getLog(Transformer.class);

    @Override
    public void run() {
        try {
            LOG.debug(MessageFormat.format("Starting work on job {0}", job.getId()));
            Thread.sleep(1000);
            LOG.debug(MessageFormat.format("Completed work on job {0}", job.getId()));
        } catch (InterruptedException e) {
            {
                // nothing
            }
        }

    }

    @Override
    protected void complete() {
        jobManager.completeTransform(job);
    }

    @Override
    protected void error(String error) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
