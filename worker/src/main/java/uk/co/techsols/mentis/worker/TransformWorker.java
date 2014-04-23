/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.techsols.mentis.worker;

import uk.co.techsols.mentis.common.NodeType;

/**
 *
 * @author james.bench
 */
public class TransformWorker extends CommonWorker implements Worker{

    public TransformWorker() {
        type = NodeType.TRANSFORM;
    }
    
}
