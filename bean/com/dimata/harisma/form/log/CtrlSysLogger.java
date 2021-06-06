
package com.dimata.harisma.form.log;

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
import com.dimata.harisma.entity.log.*;


public class CtrlSysLogger extends Control implements I_Language 
{
	public static int RSLT_OK               = 0;
	public static int RSLT_UNKNOWN_ERROR    = 1;
	public static int RSLT_EST_CODE_EXIST   = 2;
	public static int RSLT_FORM_INCOMPLETE  = 3;

	public static String[][] resultText = {
		{"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap"},
		{"Succes", "Can not process", "Estimation code exist", "Data incomplete"}
	};

	private int start;
	private String msgString;
	private SysLogger logger;
	private PstSysLogger pstLogger;
	private FrmSysLogger frmLogger;
	int language = LANGUAGE_DEFAULT;

        
	public CtrlSysLogger(HttpServletRequest request){
            msgString = "";
            logger = new SysLogger();
            
            try{
                pstLogger = new PstSysLogger(0);
            }
            catch(Exception e){}
            
            frmLogger = new FrmSysLogger(request, logger);
	}

	private String getSystemMessage(int msgCode){
            switch (msgCode){
                case I_DBExceptionInfo.MULTIPLE_ID :
                    this.frmLogger.addError(frmLogger.FRM_FIELD_LOG_ID, resultText[language][RSLT_EST_CODE_EXIST] );
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

	public SysLogger getSysLogger() { return logger; } 

	public FrmSysLogger getForm() { return frmLogger; }

	public String getMessage(){ return msgString; }

	public int getStart() { return start; }

	public int action(int cmd , long oidLogger){
		msgString = "";
		int excCode = I_DBExceptionInfo.NO_EXCEPTION;
		int rsCode = RSLT_OK;
		switch(cmd){
			case Command.ADD :
				break;

			case Command.SAVE :
				if(oidLogger != 0){
					try{
						logger = PstSysLogger.fetchExc(oidLogger);
					}catch(Exception exc){
					}
				}

				frmLogger.requestEntityObject(logger);

				if(frmLogger.errorSize()>0) {
					msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
					return RSLT_FORM_INCOMPLETE ;
				}

				if(logger.getOID()==0){
					try{
						long oid = pstLogger.insertExc(this.logger);
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
						long oid = pstLogger.updateExc(this.logger);
					}catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					}catch (Exception exc){
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN); 
					}

				}
				break;

			case Command.EDIT :
				if (oidLogger != 0) {
					try {
						logger = PstSysLogger.fetchExc(oidLogger);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.ASK :
				if (oidLogger != 0) {
					try {                     
                                                msgString = FRMMessage.getMessage(FRMMessage.MSG_ASKDEL);
						logger = PstSysLogger.fetchExc(oidLogger);
					} catch (DBException dbexc){
						excCode = dbexc.getErrorCode();
						msgString = getSystemMessage(excCode);
					} catch (Exception exc){ 
						msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
					}
				}
				break;

			case Command.DELETE :
				if (oidLogger != 0){
					try{
						long oid = PstSysLogger.deleteExc(oidLogger);
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
