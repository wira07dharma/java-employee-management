/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.utility.service.leavedp;

import com.dimata.harisma.entity.attendance.AlStockManagement;
import com.dimata.harisma.entity.attendance.PstAlStockManagement;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.leave.I_Leave;
import com.dimata.harisma.entity.leave.LeaveAlClosing;
import com.dimata.harisma.entity.leave.LeaveAlClosingList;
import com.dimata.harisma.entity.leave.LeaveAlClosingNoStockList;
import com.dimata.harisma.entity.masterdata.EmpCategory;
import com.dimata.harisma.entity.masterdata.PstEmpCategory;
import com.dimata.harisma.session.leave.SessLeaveClosing;
import com.dimata.system.entity.system.PstSystemProperty;
import com.dimata.util.Formater;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Gunadi
 */
public class ServiceAlClosing {
    
    private static ServiceAlClosing man = null;
    //public static boolean running = false;
    private static AutomaticAlClosing assistant =null;
    /**
     * @return the txtProcessList
     */
    public static AutomaticAlClosing getAssistant(){
        if(assistant !=null){
            return assistant;
        }
        else{
            return assistant = new AutomaticAlClosing();
        }
        
    }

    public static ServiceAlClosing getInstance(boolean withAssistant) {
        if (man == null) {
            man = new ServiceAlClosing();            
            if(withAssistant){
                assistant = new AutomaticAlClosing();
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
            assistant= new AutomaticAlClosing();
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
