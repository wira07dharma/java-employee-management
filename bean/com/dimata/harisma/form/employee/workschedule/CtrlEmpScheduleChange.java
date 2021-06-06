/* Created on 	:  30 September 2011 [time] AM/PM
 *
 * @author  	:  Priska
 * @version  	:  [version]
 */

/*******************************************************************
 * Class Description 	: CtrlCompany
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.harisma.form.employee.workschedule;

/**
 *
 * @author Priska
 */
/* java package */
import com.dimata.harisma.entity.employee.workschedule.EmpScheduleChange;
import com.dimata.harisma.entity.employee.workschedule.PstEmpScheduleChange;
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
/* project package */
//import com.dimata.harisma.db.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.system.entity.PstSystemProperty;
import java.sql.*;

public class CtrlEmpScheduleChange extends Control implements I_Language{
    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
    };
    private int start;
    private String msgString;
    private EmpScheduleChange empScheduleChange;
    private PstEmpScheduleChange pstEmpScheduleChange;
    private FrmEmpScheduleChange frmEmpScheduleChange;
    int language = LANGUAGE_DEFAULT;

    public CtrlEmpScheduleChange(HttpServletRequest request) {
        msgString = "";
        empScheduleChange = new EmpScheduleChange();
        try {
            pstEmpScheduleChange = new PstEmpScheduleChange(0);
        } catch (Exception e) {
            ;
        }
        frmEmpScheduleChange = new FrmEmpScheduleChange(request, empScheduleChange);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmEmpScheduleChange.addError(frmEmpScheduleChange.FRM_FIELD_EMP_SCHEDULE_CHANGE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
                return resultText[language][RSLT_EST_CODE_EXIST];
            default:
                return resultText[language][RSLT_UNKNOWN_ERROR];
        }
    }

    private int getControlMsgId(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                return RSLT_EST_CODE_EXIST;
            default:
                return RSLT_UNKNOWN_ERROR;
        }
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public EmpScheduleChange getdEmpScheduleChange() {
        return empScheduleChange;
    }

    public FrmEmpScheduleChange getForm() {
        return frmEmpScheduleChange;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidEmpScheduleChange) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

          case Command.SAVE :
				if(oidEmpScheduleChange != 0){
					try{
						empScheduleChange = PstEmpScheduleChange.fetchExc(oidEmpScheduleChange);
					}catch(Exception exc){
					}
				}

				frmEmpScheduleChange.requestEntityObject(empScheduleChange);

				if(frmEmpScheduleChange.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}
                                
				if(empScheduleChange.getOID()==0){
					try{
						long oid = pstEmpScheduleChange.insertExc(this.empScheduleChange);
					}catch(DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
						return getControlMsgId(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}

				}else{
					try {
                                                EmpScheduleChange empScheduleChangeOld = pstEmpScheduleChange.fetchExc(oidEmpScheduleChange);
                                                empScheduleChange.setApprovalDateApplicant(empScheduleChangeOld.getApprovalDateApplicant());
                                                empScheduleChange.setApprovalDateExchange(empScheduleChangeOld.getApprovalDateExchange());
                                                empScheduleChange.setApprovalDateLevel1(empScheduleChangeOld.getApprovalDateLevel1());
                                                empScheduleChange.setApprovalDateLevel2(empScheduleChangeOld.getApprovalDateLevel2());
                                            
						long oid = pstEmpScheduleChange.updateExc(this.empScheduleChange);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidEmpScheduleChange != 0) {
					try {
						empScheduleChange = PstEmpScheduleChange.fetchExc(oidEmpScheduleChange);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;
                        case Command.SUBMIT :
				if (oidEmpScheduleChange != 0) {
					try {
						empScheduleChange = PstEmpScheduleChange.fetchExc(oidEmpScheduleChange);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;    

			case Command.ASK :
				if (oidEmpScheduleChange != 0) {
					try {
						empScheduleChange = PstEmpScheduleChange.fetchExc(oidEmpScheduleChange);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;


			case Command.DELETE :
				if (oidEmpScheduleChange != 0){
					try{
						long oid = PstEmpScheduleChange.deleteExc(oidEmpScheduleChange);
						if(oid!=0){
							msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
							excCode = RSLT_OK;
						}else{
							msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
							excCode = RSLT_FORM_INCOMPLETE;
						}
					}catch(DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch(Exception exc){	
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			default :

		}
		return rsCode;
	}
}
