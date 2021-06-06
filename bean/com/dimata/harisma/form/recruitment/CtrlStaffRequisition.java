/* 
 * Ctrl Name  		:  CtrlStaffRequisition.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		:  [authorName] 
 * @version  		:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.form.recruitment;

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
import com.dimata.harisma.entity.recruitment.*;

public class CtrlStaffRequisition extends Control implements I_Language 
{
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
	private StaffRequisition staffRequisition;
	private PstStaffRequisition pstStaffRequisition;
	private FrmStaffRequisition frmStaffRequisition;
	int language = LANGUAGE_DEFAULT;

	public CtrlStaffRequisition(HttpServletRequest request){
		msgString = "";
		staffRequisition = new StaffRequisition();
		try{
			pstStaffRequisition = new PstStaffRequisition(0);
		}catch(Exception e){;}
		frmStaffRequisition = new FrmStaffRequisition(request, staffRequisition);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmStaffRequisition.addError(frmStaffRequisition.FRM_FIELD_STAFF_REQUISITION_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public StaffRequisition getStaffRequisition() { return staffRequisition; } 

	public FrmStaffRequisition getForm() { return frmStaffRequisition; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidStaffRequisition){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidStaffRequisition != 0){
					try{
						staffRequisition = PstStaffRequisition.fetchExc(oidStaffRequisition);
					}catch(Exception exc){
					}
				}

				frmStaffRequisition.requestEntityObject(staffRequisition);

				if(frmStaffRequisition.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(staffRequisition.getOID()==0){
					try{
						long oid = pstStaffRequisition.insertExc(this.staffRequisition);
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
						long oid = pstStaffRequisition.updateExc(this.staffRequisition);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidStaffRequisition != 0) {
					try {
						staffRequisition = PstStaffRequisition.fetchExc(oidStaffRequisition);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidStaffRequisition != 0) {
					try {
						staffRequisition = PstStaffRequisition.fetchExc(oidStaffRequisition);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidStaffRequisition != 0){
					try{
						long oid = PstStaffRequisition.deleteExc(oidStaffRequisition);
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
