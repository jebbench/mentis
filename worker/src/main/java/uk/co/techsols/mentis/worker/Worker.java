/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.techsols.mentis.worker;

import java.net.URI;

/**
 *
 * @author james.bench
 */
public interface Worker {
    
    public void setup(URI url, int cores);
    
}
