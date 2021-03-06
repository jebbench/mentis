///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package uk.co.techsols.mentis.workers;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.text.MessageFormat;
//import javax.xml.transform.Result;
//import javax.xml.transform.Source;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.transform.stream.StreamSource;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// *
// * @author james.bench
// */
//public class Transformer  {
//
//    private final static Logger LOG = LoggerFactory.getLogger(Transformer.class);
//    private final TransformerFactory transformerFactory = new net.sf.saxon.TransformerFactoryImpl();
//
//    
//    public void run() {
//        try {
//            LOG.debug(MessageFormat.format("Starting work on job {0}.", job.getId()));
//
//            Source xmlSource = new StreamSource(new File(job.getDataDirectory(), "xml"));
//            Source xslSource = new StreamSource(new File(job.getDataDirectory(), "xsl"));
//
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            Result xslfoResult = new StreamResult(baos);
//
//            javax.xml.transform.Transformer xslTransformer = transformerFactory.newTransformer(xslSource);
//            xslTransformer.transform(xmlSource, xslfoResult);
//
//            LOG.debug(MessageFormat.format("Completed work on job {0}.", job.getId()));
//            complete(baos.toByteArray());
//        } catch (TransformerException e) {
//            error(e.getMessageAndLocation());
//        }
//
//    }
//
//}
