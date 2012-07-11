/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.co.techsols.mentis.entities.Node;
import uk.co.techsols.mentis.node.TransformNodeManager;

/**
 *
 * @author James Bench
 */
@Controller
@RequestMapping("/node")
public class NodeController {
    
    TransformNodeManager transformNodeManager;

    @Autowired
    public NodeController(TransformNodeManager transformNodeManager) {
        this.transformNodeManager = transformNodeManager;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addNode(){
        Node node = transformNodeManager.createNode(2);
        return new ResponseEntity<String>(node.getId().toString(), HttpStatus.CREATED);
    }
    
}
