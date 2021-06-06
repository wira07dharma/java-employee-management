/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dimata.harisma.form.masterdata;


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
/**
 *
 * @author Wiweka
 */
public class CtrlReprimand extends Control implements I_Language{
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
	private Reprimand reprimand;
	private PstReprimand pstReprimand;
	private FrmReprimand frmReprimand;
	int language = LANGUAGE_DEFAULT;

        public CtrlReprimand(HttpServletRequest request){
		msgString = "";
		reprimand = new Reprimand();
		try{
			pstReprimand = new PstReprimand(0);
		}catch(Exception e){;}
		frmReprimand = new FrmReprimand(request, reprimand);
	}

	private String getSystemMessage(int msgCode){
		switch (msgCode){
			case I_DBExceptionInfo.MULTIPLE_ID :
				this.frmReprimand.addError(frmReprimand.FRM_FIELD_REPRIMAND_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public Reprimand getReprimand() { return reprimand; }

	public FrmReprimand getForm() { return frmReprimand; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

         public int action(int cmd , long oidReprimand){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidReprimand != 0){
					try{
						reprimand = PstReprimand.fetchExc(oidReprimand);
					}catch(Exception exc){
					}
				}

				frmReprimand.requestEntityObject(reprimand);

				if(frmReprimand.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(reprimand.getOID()==0){
					try{
						long oid = pstReprimand.insertExc(this.reprimand);
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
						long oid = pstReprimand.updateExc(this.reprimand);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}

				}
				break;

			case Command.EDIT :
				if (oidReprimand != 0) {
					try {
						reprimand = PstReprimand.fetchExc(oidReprimand);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidReprimand != 0) {
					try {
                                            if(PstReprimand.checkMaster(oidReprimand))
                                                msgString = FRMMessage.getMessage(FRMMessage.MSG_IN_USED);
                                            else
                                                msgString = FRMMessage.getMessage(FRMMessage.MSG_ASKDEL);
                                            reprimand = PstReprimand.fetchExc(oidReprimand);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidReprimand != 0){
					try{
						long oid = PstReprimand.deleteExc(oidReprimand);
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
