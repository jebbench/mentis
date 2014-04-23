/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.rest;

import java.text.MessageFormat;
import uk.co.techsols.mentis.common.NodeTemplate;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.co.techsols.mentis.entities.Node;
import uk.co.techsols.mentis.node.NodeManager;

/**
 *
 * @author James Bench
 */
@Controller
@RequestMapping("/node")
public class NodeController {
    
    private static final Logger LOG = LoggerFactory.getLogger(NodeController.class);
    
    private final NodeManager transformNodeManager;
    private final NodeManager renderNodeManager;

    public NodeController(NodeManager transformNodeManager, NodeManager renderNodeManager) {
        this.transformNodeManager = transformNodeManager;
        this.renderNodeManager = renderNodeManager;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addNode(@RequestBody NodeTemplate nodeTemplate){
        if(LOG.isDebugEnabled()){
            LOG.debug("REST request to create new node.");
        }
        
        NodeManager manager;
        switch(nodeTemplate.type){
            case TRANSFORM:
                manager = transformNodeManager;
                break;
            default:
                manager = renderNodeManager;
        }
        Node node = manager.createNode(nodeTemplate.totalCapacity);
        return new ResponseEntity<String>(node.getId().toString(), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Collection<Node> listNodes(){
        ArrayList<Node> c = new ArrayList<Node>();
        c.addAll(transformNodeManager.getNodes());
        c.addAll(renderNodeManager.getNodes());
        return c;
    }
    
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody Node viewNode(@PathVariable long id) throws ResourceNotFoundException {
        Node n;
        n = transformNodeManager.getNode(id);
        if(null == n){
            n = renderNodeManager.getNode(id);
        }
        if(null == n){
            throw new ResourceNotFoundException(MessageFormat.format("Could not find a node with id {1}", id));
        }
        return n;
    }
    
}
