/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.co.techsols.mentis.worker;

import java.net.URI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author james.bench
 */
public class Main {
    
    public static void main(String args[]){
        
        if(args.length != 3){
            System.out.println("Usage: type url cores\n\ttype:\n\t\tr - renderer\n\t\tt - transformer\n\turl: the url to Eiocha.\n\tcores: the number of cores to use\n\n\te.g. node.jar t http://localhost:8080/server 4\n\n");
            System.exit(1);
            return;
        }
        
        
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
        Worker worker;
        switch(args[0]){
            case "r":
                worker = applicationContext.getBean("renderWorker", Worker.class);
                break;
            case "t":
                worker = applicationContext.getBean("transformWorker", Worker.class);
                break;
            default:
                System.out.println("Error: unreconised worker type.");
                System.exit(1);
                return;
        }
        
        worker.setup(URI.create(args[1]), Integer.parseInt(args[2]));
    }
    
}
