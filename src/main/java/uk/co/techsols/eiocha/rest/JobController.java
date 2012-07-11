/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.eiocha.rest;

import java.text.MessageFormat;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import uk.co.techsols.eiocha.job.JobManager;
import uk.co.techsols.eiocha.entities.Job;

/**
 *
 * @author James Bench
 */
@Controller
@RequestMapping("/job")
public class JobController {

    private final static Log LOG = LogFactory.getLog(JobController.class);
    JobManager jobManager;
    DocumentBuilder documentBuilder;

    @Autowired
    public JobController(JobManager jobManager) throws ParserConfigurationException {
        this.jobManager = jobManager;
        documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    }

    private String getElementContent(Document document, String elementName) throws InvalidXmlException {
        NodeList priorityNodes = document.getElementsByTagName(elementName);
        if (priorityNodes.getLength() != 1) {
            throw new InvalidXmlException(MessageFormat.format("Unable to find a single node named ''{0}''.", elementName));
        }
        return priorityNodes.item(0).getTextContent().trim();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addJob(@RequestBody StreamSource body) {
        try {
            
            Document document = documentBuilder.parse(body.getInputStream());
            boolean priority = (getElementContent(document, "priority").toLowerCase().equals("true"));
            byte[] xml = Base64.decodeBase64(getElementContent(document, "xml"));
            byte[] xsl = Base64.decodeBase64(getElementContent(document, "xsl"));

            Job job = jobManager.createJob(xml, xsl);
            return new ResponseEntity<String>(job.getId().toString(), HttpStatus.CREATED);
        } catch (Exception e) {
            LOG.error(e);
            return new ResponseEntity<String>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
