/* 
 * Ctrl Name  		:  CtrlDevImprovementPlan.java 
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

package com.dimata.harisma.form.employee;

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
import com.dimata.harisma.entity.employee.*;

public class CtrlDevImprovementPlan extends Control implements I_Language 
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
	private DevImprovementPlan devImprovementPlan;
	private PstDevImprovementPlan pstDevImprovementPlan;
	private FrmDevImprovementPlan frmDevImprovementPlan;
	int language = LANGUAGE_DEFAULT;

	public CtrlDevImprovementPlan(HttpServletRequest request){
		msgString = "";
		devImprovementPlan = new DevImprovementPlan();
		try{
			pstDevImprovementPlan = new PstDevImprovementPlan(0);
		}catch(Exception e){;}
		frmDevImprovementPlan = new FrmDevImprovementPlan(request, devImprovementPlan);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmDevImprovementPlan.addError(frmDevImprovementPlan.FRM_FIELD_DEV_IMPROVEMENT_PLAN_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public DevImprovementPlan getDevImprovementPlan() { return devImprovementPlan; } 

	public FrmDevImprovementPlan getForm() { return frmDevImprovementPlan; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidDevImprovementPlan, long empAppraisalOID){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidDevImprovementPlan != 0){
					try{
						devImprovementPlan = PstDevImprovementPlan.fetchExc(oidDevImprovementPlan);
					}catch(Exception exc){
					}
				}

				frmDevImprovementPlan.requestEntityObject(devImprovementPlan);

                devImprovementPlan.setOID(oidDevImprovementPlan);
                devImprovementPlan.setEmployeeAppraisal(empAppraisalOID);

				if(frmDevImprovementPlan.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(devImprovementPlan.getOID()==0){
					try{
						long oid = pstDevImprovementPlan.insertExc(this.devImprovementPlan);
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
						long oid = pstDevImprovementPlan.updateExc(this.devImprovementPlan);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidDevImprovementPlan != 0) {
					try {
						devImprovementPlan = PstDevImprovementPlan.fetchExc(oidDevImprovementPlan);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidDevImprovementPlan != 0) {
					try {
						devImprovementPlan = PstDevImprovementPlan.fetchExc(oidDevImprovementPlan);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidDevImprovementPlan != 0){
					try{
						long oid = PstDevImprovementPlan.deleteExc(oidDevImprovementPlan);
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
