/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.utility.service.leavedp;

/**
 *
 * @author Gunadi
 */
public class ServiceLlClosing {
    
    private static ServiceLlClosing man = null;
    //public static boolean running = false;
    private static AutomaticLlClosing assistant =null;
    /**
     * @return the txtProcessList
     */
    public static AutomaticLlClosing getAssistant(){
        if(assistant !=null){
            return assistant;
        }
        else{
            return assistant = new AutomaticLlClosing();
        }
        
    }

    public static ServiceLlClosing getInstance(boolean withAssistant) {
        if (man == null) {
            man = new ServiceLlClosing();            
            if(withAssistant){
                assistant = new AutomaticLlClosing();
                assistant.setRunning(false);                
            } else{
                assistant=null;    
            }
        }
        return man;
    }

    

    
    public void startService() {
       //  public void startTransfer(long oidDepartement, String employeeName,java.util.Date fromDate,java.util.Date toDate ,long oidSection) {
        if(assistant!=null){
            assistant.setRunning(true);
            Runnable task = (Runnable) assistant;                                      
            Thread worker = new Thread(task);
            worker.setDaemon(false);
            worker.start();                        
        }else {
            assistant= new AutomaticLlClosing();
            assistant.setRunning(true);
            Runnable task = (Runnable) assistant;                                      
            Thread worker = new Thread(task);
            worker.setDaemon(false);
            worker.start();                        
    }
       
        //running = true;
    }
    
    public void stopService() 
    {
        //setRunning(false);
        if(assistant!=null){
            assistant.setRunning(false);
           
        }
        System.out.println(".................... ServiceDPStock stoped ....................");       
    }
    
    public boolean isRunningAssistant() {
        if (assistant!=null) {                    
           return  assistant.isRunning(); 
            
        }
        else{
             return false;
        }
       
       
    }
     public boolean getStatus() {
          if (assistant!=null) {                    
           return  assistant.isRunning(); 
            
        }
        else{
             return false;
        }
       
    }
}
