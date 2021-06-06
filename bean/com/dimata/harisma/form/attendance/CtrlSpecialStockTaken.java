/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.attendance;

import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.session.leave.SessLeaveApp;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class CtrlSpecialStockTaken extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_FRM_TAKEN_BEFORE_EXPIRED_DATE = 4;
    public static int RSLT_FRM_ELIGBLE_MINUS = 5;
    public static int RSLT_FRM_DATE_IN_RANGE = 6;
     public static int RSLT_FRM_INSERT_DATA = 8;
      public static int RSLT_FRM_UPDATE_DATA = 9;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap", "Taken harus sebelum expired date", "EligbleDay tidak boleh minus", "cuti yang di request sudah ada","Menambah data Dp","Ubah Data Dp"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete", "Taken date must be before expired date", "EligbleDay dosn't minus", "The are overlapping leave request, please check again","Insert Data Dp","Update Data Dp"}
    };
    private int start;
    private String msgString;
    private SpecialStockTaken entSpecialStockTaken;
    private PstSpecialStockTaken pstSpecialStockTaken;
    private FrmSpecialStockTaken frmSpecialStockTaken;
    int language = LANGUAGE_DEFAULT;

    public CtrlSpecialStockTaken(HttpServletRequest request) {
        msgString = "";
        entSpecialStockTaken = new SpecialStockTaken();
        try {
            pstSpecialStockTaken = new PstSpecialStockTaken(0);
        } catch (Exception e) {;
        }
        frmSpecialStockTaken = new FrmSpecialStockTaken(request, entSpecialStockTaken);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmSpecialStockTaken.addError(frmSpecialStockTaken.FRM_FIELD_SPECIAL_STOCK_TAKEN_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public SpecialStockTaken getSpecialStockTaken() {
        return entSpecialStockTaken;
    }

    public FrmSpecialStockTaken getForm() {
        return frmSpecialStockTaken;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidSpecialStockTaken) {
        msgString = "";
        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                if (oidSpecialStockTaken != 0) {
                    try {
                        entSpecialStockTaken = PstSpecialStockTaken.fetchExc(oidSpecialStockTaken);
                    } catch (Exception exc) {
                    }
                }

                frmSpecialStockTaken.requestEntityObject(entSpecialStockTaken);
				
				if (entSpecialStockTaken.getSpecialStockId()==0){
					String whereClause = PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_EMPLOYEE_ID]+"="
							+ entSpecialStockTaken.getEmployeeId() + " AND " + PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_SCHEDULE_ID]
							+ " = " + entSpecialStockTaken.getScheduleId()+" AND " + PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_STATUS]+"= 0";
					Vector listStock = PstSpecialStockId.list(0, 0, whereClause, "");
					if (listStock.size()>0){
						SpecialStockId specialStockId = (SpecialStockId) listStock.get(0);
						entSpecialStockTaken.setSpecialStockId(specialStockId.getOID());
					}
				}

                if (frmSpecialStockTaken.errorSize() > 0 || entSpecialStockTaken.getSpecialStockId() ==0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }

                if (entSpecialStockTaken.getOID() == 0) {
                    try {
						String adaError="";
						Vector listDPTaken = SessLeaveApp.checkOverLapsLeaveTaken(entSpecialStockTaken.getEmployeeId(), entSpecialStockTaken.getTakenDate(), entSpecialStockTaken.getTakenFinishDate());
						if(listDPTaken!=null && listDPTaken.size()>0){
							for (int dpCheckIdx = 0; dpCheckIdx < listDPTaken.size(); dpCheckIdx++) {
								LeaveCheckTakenDateFinish dpCheck = (LeaveCheckTakenDateFinish) listDPTaken.get(dpCheckIdx);
								adaError = adaError + "<b> <br>"+dpCheck.getLeaveSymbol() + " Taken Date :" + Formater.formatDate(dpCheck.getTakenDate(), "dd-MM-yyyy HH:mm") + " And Finish Date :" + Formater.formatDate(dpCheck.getFinishDate(), "dd-MM-yyyy HH:mm")  + " Date of Request : </b> <a href=\"javascript:openLeaveOverlap(\'" + dpCheck.getLeaveAppId() + "\');\">" + dpCheck.getSubmissionDate() + "</a> ; ";
							}
						}
						if(adaError.length()>0){
							msgString = resultText[language][RSLT_FRM_DATE_IN_RANGE] + " please check other Leave form on the same range: "+ adaError;
							rsCode = RSLT_FRM_DATE_IN_RANGE;
						 } else {
							long oid = pstSpecialStockTaken.insertExc(this.entSpecialStockTaken);

							SpecialStockId specialStockId = PstSpecialStockId.fetchExc(entSpecialStockTaken.getSpecialStockId());
							specialStockId.setStatus(1);
							specialStockId.setQtyUsed(1);
							PstSpecialStockId.updateExc(specialStockId);

							msgString = FRMMessage.getMessage(FRMMessage.MSG_SAVED);
						}
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
                        long oid = pstSpecialStockTaken.updateExc(this.entSpecialStockTaken);
                        msgString = FRMMessage.getMessage(FRMMessage.MSG_UPDATED);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }

                }
                break;

            case Command.EDIT:
                if (oidSpecialStockTaken != 0) {
                    try {
                        entSpecialStockTaken = PstSpecialStockTaken.fetchExc(oidSpecialStockTaken);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidSpecialStockTaken != 0) {
                    try {
                        entSpecialStockTaken = PstSpecialStockTaken.fetchExc(oidSpecialStockTaken);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidSpecialStockTaken != 0) {
                    try {
						SpecialStockTaken specialStockTaken = PstSpecialStockTaken.fetchExc(oidSpecialStockTaken);
						SpecialStockId specialStockId = PstSpecialStockId.fetchExc(specialStockTaken.getSpecialStockId());
						specialStockId.setStatus(0);
						PstSpecialStockId.updateExc(specialStockId);
                        long oid = PstSpecialStockTaken.deleteExc(oidSpecialStockTaken);
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

            default:

        }
        return rsCode;
    }
}
