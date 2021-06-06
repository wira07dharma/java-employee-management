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

package com.dimata.harisma.form.masterdata;

/**
 *
 * @author Priska
 */
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
/* project package */
//import com.dimata.harisma.db.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.search.SrcEmployee;
import com.dimata.harisma.session.employee.SessEmployee;
import com.dimata.system.entity.PstSystemProperty;
import java.sql.*;

public class CtrlEmpDocList extends Control implements I_Language{
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
    private EmpDocList empDocList;
    private PstEmpDocList pstEmpDocList;
    private FrmEmpDocList frmEmpDocList;
    int language = LANGUAGE_DEFAULT;

    public CtrlEmpDocList(HttpServletRequest request) {
        msgString = "";
        empDocList = new EmpDocList();
        try {
            pstEmpDocList = new PstEmpDocList(0);
        } catch (Exception e) {
            ;
        }
        frmEmpDocList = new FrmEmpDocList(request, empDocList);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmEmpDocList.addError(frmEmpDocList.FRM_FIELD_EMP_DOC_LIST_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public EmpDocList getdEmpDocList() {
        return empDocList;
    }

    public FrmEmpDocList getForm() {
        return frmEmpDocList;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }
    public int action(int cmd, long oidEmpDocList,SrcEmployee srcEmployee,int start,int recordToGet,String where,String objectName,long oidDoc) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

          case Command.SAVE :
				if(oidEmpDocList != 0){
					try{
						empDocList = PstEmpDocList.fetchExc(oidEmpDocList);
					}catch(Exception exc){
					}
				}
                                Vector listEmployee = SessEmployee.searchEmployeeWithDocList(srcEmployee, start, recordToGet,where,objectName,oidDoc);
				frmEmpDocList.requestEntityObject(empDocList);
                                
                                frmEmpDocList.requestEntityMultipleObject(listEmployee);
				Vector VListEmp = (Vector) frmEmpDocList.getVlistEmpDocList();
                                
                                if(frmEmpDocList.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}
                                
                                for (int i=0; i < VListEmp.size(); i++){
                                    EmpDocList empDocList1 = (EmpDocList) VListEmp.get(i); 
                                    long delete = pstEmpDocList.deletewhere(empDocList1.getEmployee_id(), empDocList1.getEmp_doc_id(), empDocList1.getObject_name());
                                        try{
						long oid = pstEmpDocList.insertExc(empDocList1);
					}catch(DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
						return getControlMsgId(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
					}
                                
                                }
//				if(empDocList.getOID()==0){
//					try{
//						long oid = pstEmpDocList.insertExc(this.empDocList);
//					}catch(DBException dbexc){
//						excCode = dbexc.getErrorCode();
//						msgString = getSystemMessage(excCode);
//						return getControlMsgId(excCode);
//					}catch (Exception exc){
//						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
//						return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
//					}
//
//				}else{
//					try {
//						long oid = pstEmpDocList.updateExc(this.empDocList);
//					}catch (DBException dbexc){
//						excCode = dbexc.getErrorCode();
//						msgString = getSystemMessage(excCode);
//					}catch (Exception exc){
//						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
//					}
//
//				}
				break;

			case Command.EDIT :
				if (oidEmpDocList != 0) {
					try {
						empDocList = PstEmpDocList.fetchExc(oidEmpDocList);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidEmpDocList != 0) {
					try {
						empDocList = PstEmpDocList.fetchExc(oidEmpDocList);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;


			case Command.DELETE :
				if (oidEmpDocList != 0){
					try{
						long oid = PstEmpDocList.deleteExc(oidEmpDocList);
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
