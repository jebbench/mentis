/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.co.techsols.mentis.rest;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import uk.co.techsols.mentis.common.NodeTemplate;
import uk.co.techsols.mentis.entities.Node;
import uk.co.techsols.mentis.node.NodeManager;
import uk.co.techsols.mentis.rest.converters.NodeConverter;

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
    
    private NodeConverter nodeConverter;

    public void setNodeConverter(NodeConverter nodeConverter) {
        this.nodeConverter = nodeConverter;
    }

    public NodeController(NodeManager transformNodeManager, NodeManager renderNodeManager) {
        this.transformNodeManager = transformNodeManager;
        this.renderNodeManager = renderNodeManager;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addNode(@RequestBody NodeTemplate nodeTemplate, UriComponentsBuilder b) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("REST request to create new node.");
        }

        NodeManager manager;
        switch (nodeTemplate.type) {
            case TRANSFORM:
                manager = transformNodeManager;
                break;
            default:
                manager = renderNodeManager;
        }
        Node node = manager.createNode(nodeTemplate.totalCapacity);

        UriComponents uriComponents = b.path("/node/{id}").buildAndExpand(node.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uriComponents.toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Collection<NodeTemplate> listNodes() {
        ArrayList<NodeTemplate> c = new ArrayList<NodeTemplate>();
        for(Node n : transformNodeManager.getNodes()){
            c.add(nodeConverter.convert(n));
        }
        for(Node n : renderNodeManager.getNodes()){
            c.add(nodeConverter.convert(n));
        }
        return c;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    NodeTemplate viewNode(@PathVariable long id) throws ResourceNotFoundException {
        Node n = transformNodeManager.getNode(id);
        if (null == n) {
            n = renderNodeManager.getNode(id);
        }
        if (null == n) {
            throw new ResourceNotFoundException(MessageFormat.format("Could not find a node with id {1}", id));
        }
        return nodeConverter.convert(n);
    }

}
