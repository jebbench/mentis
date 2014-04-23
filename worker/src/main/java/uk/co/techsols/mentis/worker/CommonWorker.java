/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.techsols.mentis.worker;

import java.net.URI;
import org.springframework.web.client.RestTemplate;
import uk.co.techsols.mentis.common.NodeTemplate;
import uk.co.techsols.mentis.common.NodeType;

/**
 *
 * @author james.bench
 */
public abstract class CommonWorker {
    
    protected NodeType type;
    protected RestTemplate restTemplate;
    
    protected URI url;
    protected int cores;

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public void setup(URI url, int cores) {
        this.url = url;
        this.cores = cores;
        register(); // Move this to a loop for reconnecting?
    }
    
    protected void register(){
        NodeTemplate nodeTemplate = new NodeTemplate();
        nodeTemplate.totalCapacity = cores;
        nodeTemplate.type = type;
        
        URI uri = url.resolve("rest/node");
        
        restTemplate.postForLocation(uri, nodeTemplate);
    }
    
}
