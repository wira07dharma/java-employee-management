/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.attendance;
import com.dimata.harisma.entity.attendance.PstSpecialStockId;
import com.dimata.harisma.entity.attendance.SpecialStockId;
import javax.servlet.http.*;
import com.dimata.util.*;
import com.dimata.util.lang.*;
import com.dimata.qdep.system.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.form.attendance.FrmSpecialStockId;

/*
Description : Controll SpecialStockId
Date : Mon Jul 08 2019
Author : Dharma
*/

public class CtrlSpecialStockId extends Control implements I_Language {

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
  private SpecialStockId entSpecialStockId;
  private PstSpecialStockId pstSpecialStockId;
  private FrmSpecialStockId frmSpecialStockId;
  int language = LANGUAGE_DEFAULT;

  public CtrlSpecialStockId(HttpServletRequest request) {
    msgString = "";
    entSpecialStockId = new SpecialStockId();
    try {
      pstSpecialStockId = new PstSpecialStockId(0);
    } catch (Exception e) {;
    }
    frmSpecialStockId = new FrmSpecialStockId(request, entSpecialStockId);
  }

  private String getSystemMessage(int msgCode) {
    switch (msgCode) {
      case I_DBExceptionInfo.MULTIPLE_ID:
        this.frmSpecialStockId.addError(FrmSpecialStockId.FRM_FIELD_SPECIAL_STOCK_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

  public SpecialStockId getSpecialStockId() {
    return entSpecialStockId;
  }

  public FrmSpecialStockId getForm() {
    return frmSpecialStockId;
  }

  public String getMessage() {
    return msgString;
  }

  public int getStart() {
    return start;
  }

  public int action(int cmd, long oidSpecialStockId) {
    msgString = "";
    int excCode = I_DBExceptionInfo.NO_EXCEPTION;
    int rsCode = RSLT_OK;
    switch (cmd) {
      case Command.ADD:
        break;

      case Command.SAVE:
        if (oidSpecialStockId != 0) {
          try {
            entSpecialStockId = PstSpecialStockId.fetchExc(oidSpecialStockId);
          } catch (Exception exc) {
          }
        }

        frmSpecialStockId.requestEntityObject(entSpecialStockId);

        if (frmSpecialStockId.errorSize() > 0) {
          msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
          return RSLT_FORM_INCOMPLETE;
        }

        if (entSpecialStockId.getOID() == 0) {
          try {
            long oid = pstSpecialStockId.insertExc(this.entSpecialStockId);
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
            long oid = pstSpecialStockId.updateExc(this.entSpecialStockId);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }

        }
        break;

      case Command.EDIT:
        if (oidSpecialStockId != 0) {
          try {
            entSpecialStockId = PstSpecialStockId.fetchExc(oidSpecialStockId);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.ASK:
        if (oidSpecialStockId != 0) {
          try {
            entSpecialStockId = PstSpecialStockId.fetchExc(oidSpecialStockId);
          } catch (DBException dbexc) {
            excCode = dbexc.getErrorCode();
            msgString = getSystemMessage(excCode);
          } catch (Exception exc) {
            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
          }
        }
        break;

      case Command.DELETE:
        if (oidSpecialStockId != 0) {
          try {
            long oid = PstSpecialStockId.deleteExc(oidSpecialStockId);
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