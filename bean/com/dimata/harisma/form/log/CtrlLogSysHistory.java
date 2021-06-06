/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.log;

import com.dimata.harisma.entity.log.LogSysHistory;
import com.dimata.harisma.entity.log.PstLogSysHistory;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author khirayinnura
 */
public class CtrlLogSysHistory extends Control implements I_Language {

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
    private LogSysHistory entLogSysHistory;
    private PstLogSysHistory pstLogSysHistory;
    private FrmLogSysHistory frmLogSysHistory;
    int language = LANGUAGE_DEFAULT;

    public CtrlLogSysHistory(HttpServletRequest request) {
        msgString = "";
        entLogSysHistory = new LogSysHistory();
        try {
            pstLogSysHistory = new PstLogSysHistory(0);
        } catch (Exception e) {;
        }
        frmLogSysHistory = new FrmLogSysHistory(request, entLogSysHistory);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmLogSysHistory.addError(frmLogSysHistory.FRM_FIELD_LOG_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public LogSysHistory getLogSysHistory() {
        return entLogSysHistory;
    }

    public FrmLogSysHistory getForm() {
        return frmLogSysHistory;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }
    

    public int action(int cmd, long oidLogSysHistory) {
        return action(cmd, oidLogSysHistory, "", 0); 
    }

    public int action(int cmd, long oidLogSysHistory, String approveList, long appUserIdSess) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidLogSysHistory != 0) {
                    try {
                        entLogSysHistory = PstLogSysHistory.fetchExc(oidLogSysHistory);
                    } catch (Exception exc) {
                    }
                }

                frmLogSysHistory.requestEntityObject(entLogSysHistory);

                if (frmLogSysHistory.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entLogSysHistory.getOID() == 0) {
                    try {
                        long oid = pstLogSysHistory.insertExc(this.entLogSysHistory);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }

                } else {
                    try {
                        long oid = pstLogSysHistory.updateExc(this.entLogSysHistory);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidLogSysHistory != 0) {
                    try {
                        entLogSysHistory = PstLogSysHistory.fetchExc(oidLogSysHistory);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidLogSysHistory != 0) {
                    try {
                        entLogSysHistory = PstLogSysHistory.fetchExc(oidLogSysHistory);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidLogSysHistory != 0) {
                    try {
                        long oid = PstLogSysHistory.deleteExc(oidLogSysHistory);
                        if (oid != 0) {
                            msgString = FRMMessage.getMessage(FRMMessage.MSG_DELETED);
                            excCode = RSLT_OK;
                        } else {
                            msgString = FRMMessage.getMessage(FRMMessage.ERR_DELETED);
                            excCode = RSLT_FORM_INCOMPLETE;
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.LIST:
                frmLogSysHistory.requestEntityObject(entLogSysHistory);
                break;

            case Command.APPROVE:
                //splits => untuk mengurutkan beberapa data menjadi baris data
                String[] splits = approveList.split(",");
                for (String asset : splits) {
                    if (asset != "") {
                        long oidLogList = Long.parseLong(asset);
                        if (oidLogList != 0) {
                            try {
                                entLogSysHistory = PstLogSysHistory.fetchExc(oidLogList);
                            } catch (Exception exc) {
                            }

                            entLogSysHistory.setLogStatus(I_DocStatus.DOCUMENT_STATUS_CLOSED);
                            entLogSysHistory.setApproverId(appUserIdSess);
                            entLogSysHistory.setApproveDate(new Date());

                            try {
                                long oid = pstLogSysHistory.updateExc(this.entLogSysHistory);
                            } catch (DBException dbexc) {
                                excCode = dbexc.getErrorCode();
                                msgString = getSystemMessage(excCode);
                            } catch (Exception exc) {
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }
                        }
                    }
                }
                break;

            default:

        }
        return rsCode;
    }
}
