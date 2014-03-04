/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.rest;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    
    private final NodeManager transformNodeManager;
    private final NodeManager renderNodeManager;

    public NodeController(NodeManager transformNodeManager, NodeManager renderNodeManager) {
        this.transformNodeManager = transformNodeManager;
        this.renderNodeManager = renderNodeManager;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> addNode(){
        Node node = transformNodeManager.createNode(2);
        return new ResponseEntity<String>(node.getId().toString(), HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody Collection<Node> listNodes(){
        ArrayList<Node> c = new ArrayList<Node>();
        c.addAll(transformNodeManager.getNodes());
        c.addAll(renderNodeManager.getNodes());
        return c;
    }
    
}
