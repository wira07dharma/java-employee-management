/*
 * Ctrl Name  		:  CtrlPresence.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  		: karya
 * @version  		: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.harisma.form.attendance;

/* java package */
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* dimata package */
import com.dimata.util.*;
import com.dimata.util.lang.*;
/* qdep package */
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.gui.jsp.*;
/* project package */
//import com.dimata.harisma.db.*;
import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.entity.masterdata.PstPeriod;
import com.dimata.harisma.utility.service.presence.PresenceAnalyser;

// import barcode/presence logger -- add by edhy
import com.dimata.harisma.utility.service.tma.*;

public class CtrlPresence extends Control implements I_Language {
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    //update by satrya 2012-10-10
    public static int RSLT_RECORD_NOT_FOUND = 4;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap","Data tidak ada"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete","Record Not Found"}
    };
    
    private int start;
    private String msgString;
    private Presence presence;
    private PstPresence pstPresence;
    private FrmPresence frmPresence;
    int language = LANGUAGE_DEFAULT;
    //update by devin 2014-03-24
    private Hashtable msgSuccess = new Hashtable();
    
    public CtrlPresence(HttpServletRequest request){            
        msgString = "";
        presence = new Presence();
        try{
            pstPresence = new PstPresence(0);
        }catch(Exception e){;}
        frmPresence = new FrmPresence(request, presence);
    }
    
    private String getSystemMessage(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                this.frmPresence.addError(frmPresence.FRM_FIELD_PRESENCE_ID, resultText[language][RSLT_EST_CODE_EXIST] );
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }
    
    private int getControlMsgId(int msgCode){
        switch (msgCode){
            case I_DBExceptionInfo.MULTIPLE_ID :
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }
    
    public int getLanguage(){ return language; }
    
    public void setLanguage(int language){ this.language = language; }
    
    public Presence getPresence() { return presence; }
    
    public FrmPresence getForm() { return frmPresence; }
    
    public String getMessage(){ return msgString; }
    //update by devin 2014-04-24
    
    public Hashtable msgSuccess(){ return msgSuccess;}
    
    public int getStart() { return start; }
   
   /**
    * create by devin 2014-04-09
    * Ket: untuk action presence create many employee
    * @param cmd
    * @param oidPresence
    * @param request
    * @param rangeTime
    * @param time
    * @param inputDateFrom
    * @return 
    */
   public int actionn(int cmd , long oidPresence, HttpServletRequest request,int rangeTime,Date time,Date inputDateFrom)
    {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd)
        {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                // added by edhy
                // indicate previous presence before process update execute
                Presence previousPresence = new Presence();
               
                if(oidPresence != 0)
                {
                    try 
                    {
                        presence = PstPresence.fetchExc(oidPresence);
                        previousPresence = PstPresence.fetchExc(oidPresence);
                    }
                    catch(Exception exc) 
                    {
                        System.out.println("Exc when fetchPresence : "+exc.toString());
                    }
                }
                 
    
            
                frmPresence.manualInputPresenceMultiple();
                Date presenceDate = ControlDate.getDate(FrmPresence.fieldNames[FrmPresence.FRM_FIELD_PRESENCE_DATETIME], request);
                Date presenceTime = ControlDate.getTime(FrmPresence.fieldNames[FrmPresence.FRM_FIELD_PRESENCE_DATETIME], request);
                int y = presenceDate.getYear();
                int M = presenceDate.getMonth();
                int d = presenceDate.getDate();
                int h = presenceTime.getHours();
                int m = presenceTime.getMinutes();
                Date presenceDateTime = new Date(y, M, d, h, m);
                 Date datePresence = new Date(); 
                presence.setPresenceDatetime(presenceDateTime);                                
                if(frmPresence.getCekBox()!=null && frmPresence.getCekBox().size()>0 ){
                    for(int x=0;x<frmPresence.getCekBox().size();x++){
                        //if(frmPresence.errorSize()>0) 
                //{
                  //  msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    //return RSLT_FORM_INCOMPLETE ;
                //}
                Presence kehadiran =(Presence)frmPresence.getCekBox().get(x);
                long dx = kehadiran.getEmployeeId(); 
                String strNote = "";
                if(presence.getOID()==0)
                {
                   
                    int jam=0;
                    int menit=0;
                    int detik=0;
                     for(int idxdt=0; idxdt<rangeTime;idxdt++){
                          if(inputDateFrom!=null && idxdt==0  ){
                         jam=time.getHours();
                         menit=time.getMinutes();
                         detik=time.getSeconds();
                        inputDateFrom.setHours(jam);
                        inputDateFrom.setMinutes(menit);
                        inputDateFrom.setSeconds(detik);
                        kehadiran.setPresenceDatetime(inputDateFrom);
                        
                     }else{
                              
                             
                              datePresence = (Date)new Date(inputDateFrom.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                              datePresence.setHours(jam);
                              datePresence.setMinutes(menit);
                              datePresence.setSeconds(detik);
                             kehadiran.setPresenceDatetime(datePresence);
                          }
                         
                         try
                    {
                        long oid = pstPresence.insertExc(kehadiran);
                        
                        // add by edhy
                        // update in schedule
                        long periodId=0;
                        if(idxdt==0){
                             periodId = PstPeriod.getPeriodIdBySelectedDate(inputDateFrom);
                        }else{
                             periodId = PstPeriod.getPeriodIdBySelectedDate(datePresence);
                        }
                        
                        int updatedFieldIndex = -1;
                        long updatePeriodId = periodId;
                        Vector vectFieldIndex=new Vector();
                         if(idxdt==0){
                             vectFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(periodId, kehadiran.getEmployeeId(), kehadiran.getStatus(), inputDateFrom);                         
                             
                         }else{
                              vectFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(periodId, kehadiran.getEmployeeId(), kehadiran.getStatus(), datePresence);                        
                         }
                        
                        
                        if(vectFieldIndex!=null && vectFieldIndex.size()==2) 
                        {
                            updatePeriodId = Long.parseLong(String.valueOf(vectFieldIndex.get(0)));
                            updatedFieldIndex = Integer.parseInt(String.valueOf(vectFieldIndex.get(1)));
                            //System.out.println("... Update field : " + PstEmpSchedule.fieldNames[updatedFieldIndex] + " on period " + updatePeriodId);
                        }                        
                                                
                        int updateStatus = 0;
                        try 
                        {
                            updateStatus = PstEmpSchedule.updateScheduleDataByPresence(updatePeriodId, kehadiran.getEmployeeId(), updatedFieldIndex, idxdt==0?inputDateFrom:datePresence);
                            if(updateStatus>0) 
                            {
                                presence.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                            }
                        }
                        catch(Exception e) 
                        {
                            System.out.println("Update Presence exc : "+e.toString());
                        }
                        
                        // process on absence and lateness
                        // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence
                        //com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presenceDateTime, presence.getEmployeeId());
                        //com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presenceDateTime, presence.getEmployeeId());
                        //update by satrya 2012-10-15 
                        PresenceAnalyser.analyzePresencePerEmployeeByEmployeeId( idxdt==0?inputDateFrom:datePresence,kehadiran.getEmployeeId());
                        
                        // logging ==> not use logging
                        /*
                        if(oid>0) 
                        {
                            strNote = "Insert data presence dengan oid = " + oid;
                            
                            BarcodeLog barcodeLog = new BarcodeLog();
                            barcodeLog.setCmdType("INSERT");
                            barcodeLog.setDate(new Date());
                            barcodeLog.setNotes(strNote);
                            PstBarcodeLog.insertExc(barcodeLog);  
                        }
                        */      
                     
                        
                    }
                    catch(DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                     }
                    
                }
                   if(kehadiran.getStatus()==0){
                            msgSuccess.put(kehadiran.getEmployeeId(),"Success For IN");
                        }else{
                         msgSuccess.put(kehadiran.getEmployeeId(),"Success For OUT");
                    }
                    }
                }
                
                
                
                
                
                else
                {
                    try   
                    {
                        long oid = pstPresence.updateExc(this.presence);
                        
                        // add by edhy
                        // update "in" schedule with updated data
                        long periodId = PstPeriod.getPeriodIdBySelectedDate(presenceDateTime);
                        int updatedFieldIndex = -1;
                        long updatePeriodId = periodId;
                        Vector vectFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(periodId, presence.getEmployeeId(), presence.getStatus(), presenceDateTime);                        
                        if(vectFieldIndex!=null && vectFieldIndex.size()==2) 
                        {
                            updatePeriodId = Long.parseLong(String.valueOf(vectFieldIndex.get(0)));
                            updatedFieldIndex = Integer.parseInt(String.valueOf(vectFieldIndex.get(1)));
                           // System.out.println("... Update curr field : " + PstEmpSchedule.fieldNames[updatedFieldIndex] + " on period " + updatePeriodId);                            
                        }
                        
                        // update "in" schedule with previous data
                        long prevPeriodId = PstPeriod.getPeriodIdBySelectedDate(previousPresence.getPresenceDatetime());
                        int prevUpdatedFieldIndex = -1;
                        long prevUpdatePeriodId = prevPeriodId;
                        Vector vectPrevFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(prevPeriodId, previousPresence.getEmployeeId(), previousPresence.getStatus(), previousPresence.getPresenceDatetime());
                        if(vectPrevFieldIndex!=null && vectPrevFieldIndex.size()==2) 
                        {                            
                            prevUpdatePeriodId = Long.parseLong(String.valueOf(vectPrevFieldIndex.get(0)));
                            prevUpdatedFieldIndex = Integer.parseInt(String.valueOf(vectPrevFieldIndex.get(1)));
                           // System.out.println("... Update prev field : " + PstEmpSchedule.fieldNames[prevUpdatedFieldIndex] + " on period " + prevUpdatePeriodId);                            
                        }                        
                        
                        int updateStatus = 0;
                        try 
                        {
                            updateStatus = PstEmpSchedule.updateScheduleDataByPresence(updatePeriodId, presence.getEmployeeId(), updatedFieldIndex, presenceDateTime);
                            
                            if(updatedFieldIndex != prevUpdatedFieldIndex) 
                            {
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(prevUpdatePeriodId, previousPresence.getEmployeeId(), prevUpdatedFieldIndex, null);
                            }
                            
                            if(updateStatus>0) 
                            {
                                presence.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                            }
                        }
                        catch(Exception e) 
                        {
                            System.out.println("Update Presence exc : "+e.toString());
                        }
                        
                        
                        // process on absence and lateness
                        // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence
                       
                       // com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presenceDateTime, presence.getEmployeeId());
                        //com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presenceDateTime, presence.getEmployeeId());
                         PresenceAnalyser.analyzePresencePerEmployeeByEmployeeId( presence.getPresenceDatetime(),presence.getEmployeeId());
                       
                        
                        //logging  ==> not use logging  
                        /*
                        if(oid>0) 
                         {
                            strNote = "Update data presence dengan oid = " + oid;
                            
                            BarcodeLog barcodeLog = new BarcodeLog();
                            barcodeLog.setCmdType("UPDATE");
                            barcodeLog.setDate(new Date());
                            barcodeLog.setNotes(strNote);
                            PstBarcodeLog.insertExc(barcodeLog);
                        }
                        */
                    }
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.EDIT :
                if (oidPresence != 0) 
                {
                    try 
                    {
                        presence = PstPresence.fetchExc(oidPresence);
                    } 
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } 
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (oidPresence != 0) 
                {
                    try   
                    {
                        presence = PstPresence.fetchExc(oidPresence);
                    } 
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } 
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidPresence != 0)
                {
                    try
                    {
                        // added by edhy
                        // indicate previous presence before process update execute
                        Presence prevPresence = new Presence();
                        try 
                        {
                            prevPresence = PstPresence.fetchExc(oidPresence);                                                        
                        }
                        catch(Exception exc) 
                        {
                            System.out.println("Exc when fetchPresence : "+exc.toString());
                        }
                        
                        long oid = PstPresence.deleteExc(oidPresence);                        
                        if(oid!=0)
                        {                            
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;                            
                            
                            // update in schedule with previous data  
                            Date presenceDateDel = prevPresence.getPresenceDatetime();                            
                            int yd = presenceDateDel.getYear();
                            int Md = presenceDateDel.getMonth();
                            int dd = presenceDateDel.getDate();                                                        
                            int Hd = presenceDateDel.getHours();
                            int md = presenceDateDel.getMinutes();
                            int sd = presenceDateDel.getSeconds();
                            Date presenceDateTimeDel = new Date(yd, Md, dd, Hd, md, sd);
                            
                            long prevPeriodId = PstPeriod.getPeriodIdBySelectedDate(presenceDateTimeDel);
                            
                            int prevUpdatedFieldIndex = -1;
                            long prevUpdatePeriodId = prevPeriodId;
                            Vector vectPrevFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(prevPeriodId, prevPresence.getEmployeeId(), prevPresence.getStatus(), presenceDateTimeDel);                                                        
                            if(vectPrevFieldIndex!=null && vectPrevFieldIndex.size()==2) 
                            {                                
                                prevUpdatePeriodId = Long.parseLong(String.valueOf(vectPrevFieldIndex.get(0)));
                                prevUpdatedFieldIndex = Integer.parseInt(String.valueOf(vectPrevFieldIndex.get(1)));                             
                                System.out.println("... Update prev field : " + PstEmpSchedule.fieldNames[prevUpdatedFieldIndex] + " on period " + prevUpdatePeriodId);
                            }                            
                            
                            
                            int updateStatus = 0;
                            try 
                            {
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(prevUpdatePeriodId, prevPresence.getEmployeeId(), prevUpdatedFieldIndex, null);
                            }
                            catch(Exception e) 
                            {
                                System.out.println("Update Presence exc : "+e.toString());
                            }
                            
                            // process on absence and lateness
                            // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence  
                           // com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presenceDateTimeDel, prevPresence.getEmployeeId());
                            //com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presenceDateTimeDel, prevPresence.getEmployeeId());
                              PresenceAnalyser.analyzePresencePerEmployeeByEmployeeId( presence.getPresenceDatetime(),presence.getEmployeeId());
                        }
                        else  
                        {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }
                    catch(DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch(Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;  
            default :
        }
        return rsCode;
    }


    
    public int action(int cmd , long oidPresence, HttpServletRequest request)
    {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch(cmd)
        {
            case Command.ADD :
                break;
                
            case Command.SAVE :
                // added by edhy
                // indicate previous presence before process update execute
                Presence previousPresence = new Presence();
                if(oidPresence != 0)
                {
                    try 
                    {
                        presence = PstPresence.fetchExc(oidPresence);
                        previousPresence = PstPresence.fetchExc(oidPresence);
                    }
                    catch(Exception exc) 
                    {
                        System.out.println("Exc when fetchPresence : "+exc.toString());
                    }
                }
                
                frmPresence.requestEntityObject(presence);
                Date presenceDate = ControlDate.getDate(FrmPresence.fieldNames[FrmPresence.FRM_FIELD_PRESENCE_DATETIME], request);
                Date presenceTime = ControlDate.getTime(FrmPresence.fieldNames[FrmPresence.FRM_FIELD_PRESENCE_DATETIME], request);
                int y = presenceDate.getYear();
                int M = presenceDate.getMonth();
                int d = presenceDate.getDate();
                int h = presenceTime.getHours();
                int m = presenceTime.getMinutes();
                Date presenceDateTime = new Date(y, M, d, h, m);
                presence.setPresenceDatetime(presenceDateTime);                                
                
                if(frmPresence.errorSize()>0) 
                {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE ;
                }
                
                String strNote = "";
                if(presence.getOID()==0)
                {
                    try
                    {
                        long oid = pstPresence.insertExc(this.presence);
                        
                        // add by edhy
                        // update in schedule
                        long periodId = PstPeriod.getPeriodIdBySelectedDate(presenceDateTime);
                        int updatedFieldIndex = -1;
                        long updatePeriodId = periodId;
                        Vector vectFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(periodId, presence.getEmployeeId(), presence.getStatus(), presenceDateTime);                        
                        
                        if(vectFieldIndex!=null && vectFieldIndex.size()==2) 
                        {
                            updatePeriodId = Long.parseLong(String.valueOf(vectFieldIndex.get(0)));
                            updatedFieldIndex = Integer.parseInt(String.valueOf(vectFieldIndex.get(1)));
                            //System.out.println("... Update field : " + PstEmpSchedule.fieldNames[updatedFieldIndex] + " on period " + updatePeriodId);
                        }                        
                                                
                        int updateStatus = 0;
                        try 
                        {
                            updateStatus = PstEmpSchedule.updateScheduleDataByPresence(updatePeriodId, presence.getEmployeeId(), updatedFieldIndex, presenceDateTime);
                            if(updateStatus>0) 
                            {
                                presence.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                            }
                        }
                        catch(Exception e) 
                        {
                            System.out.println("Update Presence exc : "+e.toString());
                        }
                        
                        // process on absence and lateness
                        // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence
                        //com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presenceDateTime, presence.getEmployeeId());
                        //com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presenceDateTime, presence.getEmployeeId());
                        //update by satrya 2012-10-15 
                        PresenceAnalyser.analyzePresencePerEmployeeByEmployeeId( presence.getPresenceDatetime(),presence.getEmployeeId());
                        
                        // logging ==> not use logging
                        /*
                        if(oid>0) 
                        {
                            strNote = "Insert data presence dengan oid = " + oid;
                            
                            BarcodeLog barcodeLog = new BarcodeLog();
                            barcodeLog.setCmdType("INSERT");
                            barcodeLog.setDate(new Date());
                            barcodeLog.setNotes(strNote);
                            PstBarcodeLog.insertExc(barcodeLog);  
                        }
                        */                        
                    }
                    catch(DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                
                
                else
                {
                    try   
                    {
                        long oid = pstPresence.updateExc(this.presence);
                        
                        // add by edhy
                        // update "in" schedule with updated data
                        long periodId = PstPeriod.getPeriodIdBySelectedDate(presenceDateTime);
                        int updatedFieldIndex = -1;
                        long updatePeriodId = periodId;
                        Vector vectFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(periodId, presence.getEmployeeId(), presence.getStatus(), presenceDateTime);                        
                        if(vectFieldIndex!=null && vectFieldIndex.size()==2) 
                        {
                            updatePeriodId = Long.parseLong(String.valueOf(vectFieldIndex.get(0)));
                            updatedFieldIndex = Integer.parseInt(String.valueOf(vectFieldIndex.get(1)));
                           // System.out.println("... Update curr field : " + PstEmpSchedule.fieldNames[updatedFieldIndex] + " on period " + updatePeriodId);                            
                        }
                        
                        // update "in" schedule with previous data
                        long prevPeriodId = PstPeriod.getPeriodIdBySelectedDate(previousPresence.getPresenceDatetime());
                        int prevUpdatedFieldIndex = -1;
                        long prevUpdatePeriodId = prevPeriodId;
                        Vector vectPrevFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(prevPeriodId, previousPresence.getEmployeeId(), previousPresence.getStatus(), previousPresence.getPresenceDatetime());
                        if(vectPrevFieldIndex!=null && vectPrevFieldIndex.size()==2) 
                        {                            
                            prevUpdatePeriodId = Long.parseLong(String.valueOf(vectPrevFieldIndex.get(0)));
                            prevUpdatedFieldIndex = Integer.parseInt(String.valueOf(vectPrevFieldIndex.get(1)));
                           // System.out.println("... Update prev field : " + PstEmpSchedule.fieldNames[prevUpdatedFieldIndex] + " on period " + prevUpdatePeriodId);                            
                        }                        
                        
                        int updateStatus = 0;
                        try 
                        {
                            updateStatus = PstEmpSchedule.updateScheduleDataByPresence(updatePeriodId, presence.getEmployeeId(), updatedFieldIndex, presenceDateTime);
                            
                            if(updatedFieldIndex != prevUpdatedFieldIndex) 
                            {
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(prevUpdatePeriodId, previousPresence.getEmployeeId(), prevUpdatedFieldIndex, null);
                            }
                            
                            if(updateStatus>0) 
                            {
                                presence.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                            }
                        }
                        catch(Exception e) 
                        {
                            System.out.println("Update Presence exc : "+e.toString());
                        }
                        
                        
                        // process on absence and lateness
                        // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence
                       
                       // com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presenceDateTime, presence.getEmployeeId());
                        //com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presenceDateTime, presence.getEmployeeId());
                         PresenceAnalyser.analyzePresencePerEmployeeByEmployeeId( presence.getPresenceDatetime(),presence.getEmployeeId());
                       
                        
                        //logging  ==> not use logging  
                        /*
                        if(oid>0) 
                         {
                            strNote = "Update data presence dengan oid = " + oid;
                            
                            BarcodeLog barcodeLog = new BarcodeLog();
                            barcodeLog.setCmdType("UPDATE");
                            barcodeLog.setDate(new Date());
                            barcodeLog.setNotes(strNote);
                            PstBarcodeLog.insertExc(barcodeLog);
                        }
                        */
                    }
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.EDIT :
                if (oidPresence != 0) 
                {
                    try 
                    {
                        presence = PstPresence.fetchExc(oidPresence);
                    } 
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } 
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.ASK :
                if (oidPresence != 0) 
                {
                    try   
                    {
                        presence = PstPresence.fetchExc(oidPresence);
                    } 
                    catch (DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } 
                    catch (Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                
            case Command.DELETE :
                if (oidPresence != 0)
                {
                    try
                    {
                        // added by edhy
                        // indicate previous presence before process update execute
                        Presence prevPresence = new Presence();
                        try 
                        {
                            prevPresence = PstPresence.fetchExc(oidPresence);                                                        
                        }
                        catch(Exception exc) 
                        {
                            System.out.println("Exc when fetchPresence : "+exc.toString());
                        }
                        
                        long oid = PstPresence.deleteExc(oidPresence);                        
                        if(oid!=0)
                        {                            
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;                            
                            
                            // update in schedule with previous data  
                            Date presenceDateDel = prevPresence.getPresenceDatetime();                            
                            int yd = presenceDateDel.getYear();
                            int Md = presenceDateDel.getMonth();
                            int dd = presenceDateDel.getDate();                                                        
                            int Hd = presenceDateDel.getHours();
                            int md = presenceDateDel.getMinutes();
                            int sd = presenceDateDel.getSeconds();
                            Date presenceDateTimeDel = new Date(yd, Md, dd, Hd, md, sd);
                            
                            long prevPeriodId = PstPeriod.getPeriodIdBySelectedDate(presenceDateTimeDel);
                            
                            int prevUpdatedFieldIndex = -1;
                            long prevUpdatePeriodId = prevPeriodId;
                            Vector vectPrevFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(prevPeriodId, prevPresence.getEmployeeId(), prevPresence.getStatus(), presenceDateTimeDel);                                                        
                            if(vectPrevFieldIndex!=null && vectPrevFieldIndex.size()==2) 
                            {                                
                                prevUpdatePeriodId = Long.parseLong(String.valueOf(vectPrevFieldIndex.get(0)));
                                prevUpdatedFieldIndex = Integer.parseInt(String.valueOf(vectPrevFieldIndex.get(1)));                             
                                System.out.println("... Update prev field : " + PstEmpSchedule.fieldNames[prevUpdatedFieldIndex] + " on period " + prevUpdatePeriodId);
                            }                            
                            
                            
                            int updateStatus = 0;
                            try 
                            {
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(prevUpdatePeriodId, prevPresence.getEmployeeId(), prevUpdatedFieldIndex, null);
                            }
                            catch(Exception e) 
                            {
                                System.out.println("Update Presence exc : "+e.toString());
                            }
                            
                            // process on absence and lateness
                            // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence  
                           // com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presenceDateTimeDel, prevPresence.getEmployeeId());
                            //com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presenceDateTimeDel, prevPresence.getEmployeeId());
                              PresenceAnalyser.analyzePresencePerEmployeeByEmployeeId( presence.getPresenceDatetime(),presence.getEmployeeId());
                        }
                        else  
                        {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    }
                    catch(DBException dbexc)
                    {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    }
                    catch(Exception exc)
                    {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;  
            default :
        }
        return rsCode;
    }
}
