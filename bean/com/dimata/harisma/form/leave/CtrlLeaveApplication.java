/*
 * CtrlLeaveApplication.java
 *
 * Created on October 27, 2004, 11:52 AM
 */
package com.dimata.harisma.form.leave;

import com.dimata.harisma.entity.admin.AppUser;
import com.dimata.harisma.entity.admin.PstAppUser;
import javax.servlet.http.HttpServletRequest;
import java.util.Vector;
import java.util.Date;

import com.dimata.qdep.form.Control;
import com.dimata.qdep.form.FRMMessage;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.system.I_DBExceptionInfo;
import com.dimata.harisma.session.leave.*;
import com.dimata.util.lang.I_Language;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import com.dimata.harisma.session.attendance.*;
import com.dimata.harisma.session.employee.SessEmployee;

import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.leave.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.entity.log.I_LogHistory;
import com.dimata.harisma.entity.log.LogSysHistory;
import com.dimata.harisma.entity.log.PstLogSysHistory;
import com.dimata.harisma.entity.payroll.PayGeneral;
import com.dimata.harisma.entity.payroll.PstPayGeneral;
import com.dimata.harisma.session.admin.SessUserSession;
import com.dimata.harisma.session.employee.SessEmployeePicture;
import com.dimata.harisma.util.email;
import com.dimata.harisma.utility.service.presence.AbsenceAnalyser;
import com.dimata.harisma.utility.service.presence.LatenessAnalyser;
import com.dimata.qdep.entity.I_DocStatus;
import com.dimata.system.entity.system.PstSystemProperty;
import com.dimata.util.DateCalc;
import com.dimata.util.net.MailSender;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Hashtable;
import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;

/**
 *
 * @author gedhy
 */
public class CtrlLeaveApplication extends Control implements I_Language {

    public static int RSLT_OK = 0;
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_CREATE_FORM_LEAVE_SUCCESS = 4;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap", "Membuat / Update Form Cuti Berhasil"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete", "Create / Update Form Cuti Success"}};
    private int start;
    private String msgString;
    private LeaveApplication leaveApplication;
    private PstLeaveApplication pstLeaveApplication;
    private FrmLeaveApplication frmLeaveApplication;
    int language = LANGUAGE_FOREIGN;

    public CtrlLeaveApplication(HttpServletRequest request) {
        msgString = "";
        leaveApplication = new LeaveApplication();
        try {
            pstLeaveApplication = new PstLeaveApplication(0);
        } catch (Exception e) {
            ;
        }
        frmLeaveApplication = new FrmLeaveApplication(request, leaveApplication);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmLeaveApplication.addError(0, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public LeaveApplication getLeaveApplication() {
        return leaveApplication;
    }

    public FrmLeaveApplication getForm() {
        return frmLeaveApplication;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    /**
     * @param cmd
     * @param oidLeaveApplication
     * @param vectOfLeaveAppDetail
     * @return
     */
//     public int action(int cmd, long oidLeaveApplication, Vector vectOfLeaveAppDetail,String approot) {
//        return action( cmd,  oidLeaveApplication,  vectOfLeaveAppDetail, approot,0);
//     }
    public int action(int cmd, long oidLeaveApplication, Vector vectOfLeaveAppDetail, String approot) {
        msgString = "";

        I_Leave leaveConfig = null;
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;

        int sysLog = Integer.parseInt(String.valueOf(PstSystemProperty.getPropertyLongbyName("SET_USER_ACTIVITY_LOG")));
        String logField = "";
        String logPrev = "";
        String logCurr = "";
        //long sysLog = 1;
        String logDetail = "";
        Date nowDate = new Date();
        AppUser appUser = new AppUser();
        Employee emp = new Employee();
        long userId = 12424125124l;
        try {
            appUser = PstAppUser.fetch(userId);
            emp = PstEmployee.fetchExc(appUser.getEmployeeId());
        } catch (Exception e) {
            System.out.println("Get AppUser: userId: " + e.toString());
        }

        switch (cmd) {

            case Command.ADD:
                break;

            case Command.SAVE:

                int docPrev = -1;
                int docAfter = -1;

                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);
                        docPrev = leaveApplication.getDocStatus();
                    } catch (Exception exc) {
                        System.out.println("Exc when fetch LeaveApplication entity : " + exc.toString());
                    }
                }

                frmLeaveApplication.requestEntityObject(leaveApplication);
                docAfter = leaveApplication.getDocStatus();
                if (docPrev == PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT
                        && docAfter == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
                    Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                    Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                    Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                    Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());
					Vector listTknSs = SessLeaveApplication.getTakenSs(leaveApplication.getOID());
                    if ((listTknAl == null || listTknAl.size() < 1) && (listTknLl == null || listTknLl.size() < 1)
                            && (listTknDp == null || listTknDp.size() < 1) && (listTknSp == null || listTknSp.size() < 1)
							&& (listTknSs == null || listTknSs.size() < 1)) {
                        msgString = "Leave application has no detail of AL, LL or DP, cannot be set to TO BE APPROVE";
                        return RSLT_FORM_INCOMPLETE;
                    }
                }
                if (frmLeaveApplication.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                int maxApproval = leaveConfig.getMaxApproval(leaveApplication.getEmployeeId());
                boolean stsDokApprov = false;

                // if doc status Draft
                if (leaveApplication.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT) {
                    leaveApplication.setDepHeadApproval(0);
                    leaveApplication.setDepHeadApproveDate(null);
                    leaveApplication.setHrManApproveDate(null);
                    leaveApplication.setHrManApproval(0);
                    leaveApplication.setGmApproval(0);
                    leaveApplication.setGmApprovalDate(null);

                } else if (leaveApplication.getDocStatus() == 1) {

                    if (leaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_1) {
                        if (leaveApplication.getDepHeadApproval() != 0) {
                            stsDokApprov = true;
                            leaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                        }
                    } else if (leaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_2) {
                        if (leaveApplication.getDepHeadApproval() != 0 && leaveApplication.getHrManApproval() != 0) {
                            stsDokApprov = true;
                            leaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                        }
                    } else if (leaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_3) {
                        if (leaveApplication.getGmApproval() != 0) {
                            stsDokApprov = true;
                            leaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                        }
                    }
                }

                /**
                 * @DESC : PENGECEK STATUS SCHEDULE DARI APPROVED KE DRAFT Jika
                 * iLeaveMinuteEnable =1 maka menggunakan konfigurasi jam-jam'an
                 */
                //update by satrya 2012-08-03
                int iLeaveMinuteEnable = 0;
                try {
                    iLeaveMinuteEnable = Integer.parseInt(PstSystemProperty.getValueByName("LEAVE_MINUTE_ENABLE"));//menambahkan system properties
                } catch (Exception e) {
                    System.out.println("Exeception LEAVE_MINUTE_ENABLE:" + e);
                }

                if (docPrev != -1 && docAfter != -1
                        && docPrev == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED
                        && docAfter == PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT && iLeaveMinuteEnable != 1) {

                    Vector listEmpSchedule = new Vector();

                    AlStockTaken alStockTaken = new AlStockTaken();
                    LlStockTaken llStockTaken = new LlStockTaken();
                    DpStockTaken dpStockTaken = new DpStockTaken();
                    SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();

                    Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                    Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                    Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                    Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());

                    for (int idxAl = 0; idxAl < listTknAl.size(); idxAl++) {

                        alStockTaken = (AlStockTaken) listTknAl.get(idxAl);
                        Date strtDate = alStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxAlTkn = 0; idxAlTkn < alStockTaken.getTakenQty(); idxAlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }

                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxLl = 0; idxLl < listTknLl.size(); idxLl++) {

                        llStockTaken = (LlStockTaken) listTknLl.get(idxLl);
                        Date strtDate = llStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxLlTkn = 0; idxLlTkn < llStockTaken.getTakenQty(); idxLlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }

                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxDp = 0; idxDp < listTknDp.size(); idxDp++) {

                        dpStockTaken = (DpStockTaken) listTknDp.get(idxDp);
                        Date strtDate = dpStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxDpTkn = 0; idxDpTkn < dpStockTaken.getTakenQty(); idxDpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");

                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxSp = 0; idxSp < listTknSp.size(); idxSp++) {

                        specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(idxSp);
                        Date strtDate = specialUnpaidLeaveTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxSpTkn = 0; idxSpTkn < specialUnpaidLeaveTaken.getTakenQty(); idxSpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                        for (int k = 0; k < listEmpSchedule.size(); k++) {

                            EmpSchedule objAktifSchedule = new EmpSchedule();
                            objAktifSchedule = (EmpSchedule) listEmpSchedule.get(k);

                            EmpSchedule objSchedule = new EmpSchedule();

                            try {
                                objSchedule = PstEmpSchedule.fetchExc(objAktifSchedule.getOID());
                            } catch (Exception e) {
                                System.out.println("Exeption " + e.toString());
                            }

                            Period period = new Period();

                            try {
                                period = PstPeriod.fetchExc(objSchedule.getPeriodId());
                            } catch (Exception e) {
                                System.out.println("Exception " + e.toString());
                            }

                            int diffPeriod = 0;

                            diffPeriod = SessLeaveApplication.DATEDIFF(period.getEndDate(), period.getStartDate());

                            diffPeriod = diffPeriod + 1;

                            Date startDate = new Date();

                            Date newDate = new Date();

                            startDate = period.getStartDate();

                            for (int x = 0; x < diffPeriod; x++) {

                                AbsenceAnalyser.checkEmployeeAbsenceAutomatic(startDate, this.leaveApplication.getEmployeeId());
                                LatenessAnalyser.EmployeeLateness(startDate, this.leaveApplication.getEmployeeId());

                                long tmpDate = startDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                startDate = newDate;

                            }
                        }
                    }
                }

                /**
                 * @DESC : UNTUK STATUS CANCEL Jika iLeaveMinuteEnable =1 maka
                 * menggunakan konfigurasi jam-jam'an
                 */
                //update by satrya 2012-08-06
                if (leaveApplication.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_CANCELED && iLeaveMinuteEnable != 1) {

                    Vector listEmpSchedule = new Vector();

                    AlStockTaken alStockTaken = new AlStockTaken();
                    LlStockTaken llStockTaken = new LlStockTaken();
                    DpStockTaken dpStockTaken = new DpStockTaken();
                    SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();

                    Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                    Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                    Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                    Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());

                    for (int idxAl = 0; idxAl < listTknAl.size(); idxAl++) {

                        alStockTaken = (AlStockTaken) listTknAl.get(idxAl);
                        Date strtDate = alStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxAlTkn = 0; idxAlTkn < alStockTaken.getTakenQty(); idxAlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status schedule
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxLl = 0; idxLl < listTknLl.size(); idxLl++) {

                        llStockTaken = (LlStockTaken) listTknLl.get(idxLl);
                        Date strtDate = llStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxLlTkn = 0; idxLlTkn < llStockTaken.getTakenQty(); idxLlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }

                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxDp = 0; idxDp < listTknDp.size(); idxDp++) {

                        dpStockTaken = (DpStockTaken) listTknDp.get(idxDp);
                        Date strtDate = dpStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxDpTkn = 0; idxDpTkn < dpStockTaken.getTakenQty(); idxDpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");

                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxSp = 0; idxSp < listTknSp.size(); idxSp++) {

                        specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(idxSp);
                        Date strtDate = specialUnpaidLeaveTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxSpTkn = 0; idxSpTkn < specialUnpaidLeaveTaken.getTakenQty(); idxSpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                        for (int k = 0; k < listEmpSchedule.size(); k++) {

                            EmpSchedule objAktifSchedule = new EmpSchedule();
                            objAktifSchedule = (EmpSchedule) listEmpSchedule.get(k);

                            EmpSchedule objSchedule = new EmpSchedule();

                            try {
                                objSchedule = PstEmpSchedule.fetchExc(objAktifSchedule.getOID());
                            } catch (Exception e) {
                                System.out.println("Exeption " + e.toString());
                            }

                            Period period = new Period();

                            try {
                                period = PstPeriod.fetchExc(objSchedule.getPeriodId());
                            } catch (Exception e) {
                                System.out.println("Exception " + e.toString());
                            }

                            int diffPeriod = 0;

                            diffPeriod = SessLeaveApplication.DATEDIFF(period.getEndDate(), period.getStartDate());

                            diffPeriod = diffPeriod + 1;

                            Date startDate = new Date();

                            Date newDate = new Date();

                            startDate = period.getStartDate();

                            for (int x = 0; x < diffPeriod; x++) {

                                AbsenceAnalyser.checkEmployeeAbsenceAutomatic(startDate, this.leaveApplication.getEmployeeId());
                                LatenessAnalyser.EmployeeLateness(startDate, this.leaveApplication.getEmployeeId());

                                long tmpDate = startDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                startDate = newDate;

                            }
                        }
                    }
                }

                /**
                 * @DESC : PENGECEKAN CONFIGURASI UPDATE SCHEDULE jika
                 * iLeaveMinuteEnable = 0 maka mengukan konfigurasi full day
                 */
                 //update by satrya 2012-08-03
                if (SessEmpSchedule.GetConfigurasiUpdtSch() == PstEmpSchedule.UPDATE_SCHEDULE_AFTER_APPROVED && iLeaveMinuteEnable != 1) {
                    // if (SessEmpSchedule.GetConfigurasiUpdtSch() == PstEmpSchedule.UPDATE_SCHEDULE_AFTER_APPROVED) {
                    /**
                     * @DEC : UNTUK MENGUPDATE STATUS SCHEDULE JIKA STATUS
                     * DOCUMENT APPROVED
                     */
                    if (stsDokApprov == true || leaveApplication.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {

                        Vector listEmpSchedule = new Vector();

                        AlStockTaken alStockTaken = new AlStockTaken();
                        LlStockTaken llStockTaken = new LlStockTaken();
                        DpStockTaken dpStockTaken = new DpStockTaken();
                        SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();

                        Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                        Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                        Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                        Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());

                        String oidAl = String.valueOf(PstSystemProperty.getValueByName("OID_AL"));
                        String oidLl = String.valueOf(PstSystemProperty.getValueByName("OID_LL"));
                        String oidDp = String.valueOf(PstSystemProperty.getValueByName("OID_DP"));

                        for (int idxAl = 0; idxAl < listTknAl.size(); idxAl++) {

                            alStockTaken = (AlStockTaken) listTknAl.get(idxAl);
                            Date strtDate = alStockTaken.getTakenDate();
                            Date newDate = new Date();

                            for (int idxAlTkn = 0; idxAlTkn < alStockTaken.getTakenQty(); idxAlTkn++) {

                                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                if (schId != 0) {

                                    boolean same = false;

                                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                        for (int i = 0; i < listEmpSchedule.size(); i++) {

                                            EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                            objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                            if (objEmpScheduleProcess.getOID() == schId) {
                                                same = true;
                                            }
                                        }
                                    }
                                    if (same == false) {
                                        EmpSchedule empScheduleProcess = new EmpSchedule();
                                        empScheduleProcess.setOID(schId);
                                        listEmpSchedule.add(empScheduleProcess);
                                    }
                                    //update status
                                    int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidAl);
                                }
                                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                strtDate = newDate;
                            }
                        }

                        for (int idxLl = 0; idxLl < listTknLl.size(); idxLl++) {

                            llStockTaken = (LlStockTaken) listTknLl.get(idxLl);
                            Date strtDate = llStockTaken.getTakenDate();
                            Date newDate = new Date();

                            for (int idxLlTkn = 0; idxLlTkn < llStockTaken.getTakenQty(); idxLlTkn++) {

                                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                if (schId != 0) {

                                    boolean same = false;

                                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                        for (int i = 0; i < listEmpSchedule.size(); i++) {

                                            EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                            objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                            if (objEmpScheduleProcess.getOID() == schId) {
                                                same = true;
                                            }
                                        }
                                    }
                                    if (same == false) {
                                        EmpSchedule empScheduleProcess = new EmpSchedule();
                                        empScheduleProcess.setOID(schId);
                                        listEmpSchedule.add(empScheduleProcess);
                                    }

                                    int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidLl);
                                }

                                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                strtDate = newDate;
                            }
                        }

                        for (int idxDp = 0; idxDp < listTknDp.size(); idxDp++) {

                            dpStockTaken = (DpStockTaken) listTknDp.get(idxDp);
                            Date strtDate = dpStockTaken.getTakenDate();
                            Date newDate = new Date();

                            for (int idxDpTkn = 0; idxDpTkn < dpStockTaken.getTakenQty(); idxDpTkn++) {

                                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                if (schId != 0) {

                                    boolean same = false;

                                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                        for (int i = 0; i < listEmpSchedule.size(); i++) {

                                            EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                            objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                            if (objEmpScheduleProcess.getOID() == schId) {
                                                same = true;
                                            }
                                        }
                                    }
                                    if (same == false) {
                                        EmpSchedule empScheduleProcess = new EmpSchedule();
                                        empScheduleProcess.setOID(schId);
                                        listEmpSchedule.add(empScheduleProcess);
                                    }

                                    int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidDp);

                                }
                                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                strtDate = newDate;
                            }
                        }

                        for (int idxSp = 0; idxSp < listTknSp.size(); idxSp++) {

                            specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(idxSp);
                            Date strtDate = specialUnpaidLeaveTaken.getTakenDate();
                            Date newDate = new Date();
                            String oidSp = "" + specialUnpaidLeaveTaken.getScheduledId();

                            for (int idxSpTkn = 0; idxSpTkn < specialUnpaidLeaveTaken.getTakenQty(); idxSpTkn++) {

                                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                if (schId != 0) {

                                    boolean same = false;

                                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                        for (int i = 0; i < listEmpSchedule.size(); i++) {

                                            EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                            objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                            if (objEmpScheduleProcess.getOID() == schId) {
                                                same = true;
                                            }
                                        }
                                    }
                                    if (same == false) {
                                        EmpSchedule empScheduleProcess = new EmpSchedule();
                                        empScheduleProcess.setOID(schId);
                                        listEmpSchedule.add(empScheduleProcess);
                                    }

                                    int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidSp);
                                }
                                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                strtDate = newDate;
                            }
                        }

                        if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                            for (int k = 0; k < listEmpSchedule.size(); k++) {

                                EmpSchedule objAktifSchedule = new EmpSchedule();
                                objAktifSchedule = (EmpSchedule) listEmpSchedule.get(k);

                                EmpSchedule objSchedule = new EmpSchedule();

                                try {
                                    objSchedule = PstEmpSchedule.fetchExc(objAktifSchedule.getOID());
                                } catch (Exception e) {
                                    System.out.println("Exeption " + e.toString());
                                }

                                Period period = new Period();

                                try {
                                    period = PstPeriod.fetchExc(objSchedule.getPeriodId());
                                } catch (Exception e) {
                                    System.out.println("Exception " + e.toString());
                                }

                                int diffPeriod = 0;

                                diffPeriod = SessLeaveApplication.DATEDIFF(period.getEndDate(), period.getStartDate());

                                diffPeriod = diffPeriod + 1;

                                Date startDate = new Date();

                                Date newDate = new Date();

                                startDate = period.getStartDate();

                                for (int x = 0; x < diffPeriod; x++) {

                                    AbsenceAnalyser.checkEmployeeAbsenceAutomatic(startDate, this.leaveApplication.getEmployeeId());
                                    LatenessAnalyser.EmployeeLateness(startDate, this.leaveApplication.getEmployeeId());

                                    long tmpDate = startDate.getTime() + (24 * 60 * 60 * 1000);
                                    newDate = new Date(tmpDate);
                                    startDate = newDate;
                                }
                            }
                        }
                    }
                }

                if (leaveApplication.getOID() == 0) {
                    try {
                        long oid = pstLeaveApplication.insertExc(this.leaveApplication);
                        if (oid != 0) {
                            msgString = resultText[language][RSLT_CREATE_FORM_LEAVE_SUCCESS];

                            //buatkan save carrer path
                            if (sysLog != 0) { /* kondisi jika sysLog == 1, maka proses di bawah ini dijalankan*/

                                String className = leaveApplication.getClass().getName();
                                LogSysHistory logSysHistory = new LogSysHistory();

                                String reqUrl = "leave_app_edit.jsp?FRM_FLD_LEAVE_APPLICATION_ID=" + leaveApplication.getOID();
                                /* Lakukan set data ke entity logSysHistory */
                                logSysHistory.setLogDocumentId(0);
                                logSysHistory.setLogUserId(userId);
                                logSysHistory.setApproverId(userId);
                                logSysHistory.setApproveDate(nowDate);
                                logSysHistory.setLogLoginName(appUser.getLoginId());
                                logSysHistory.setLogDocumentNumber("");
                                logSysHistory.setLogDocumentType(className); //entity
                                logSysHistory.setLogUserAction("ADD"); // command
                                logSysHistory.setLogOpenUrl(reqUrl); // locate jsp
                                logSysHistory.setLogUpdateDate(nowDate);
                                logSysHistory.setLogApplication(I_LogHistory.SYSTEM_NAME[I_LogHistory.SYSTEM_HAIRISMA]); // interface
                                    /* Inisialisasi logField dengan menggambil field EmpEducation */
                                /* Tips: ambil data field dari persistent */
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_TYPE_LEAVE] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_REASON_ID] + " ;";
                                logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_DIFFERENT_PERIOD] + " ;";

                                /* data logField yg telah terisi kemudian digunakan untuk setLogDetail */
                                logSysHistory.setLogDetail(logField); // apa yang dirubah
                                    /* inisialisasi value, yaitu logCurr */
                                logCurr += "" + leaveApplication.getOID() + ";";
                                logCurr += "" + leaveApplication.getSubmissionDate() + ";";
                                logCurr += "" + leaveApplication.getEmployeeId() + ";";
                                logCurr += "" + leaveApplication.getLeaveReason() + ";";
                                logCurr += "" + leaveApplication.getDepHeadApproval() + ";";
                                logCurr += "" + leaveApplication.getHrManApproval() + ";";
                                logCurr += "" + leaveApplication.getDocStatus() + ";";
                                logCurr += "" + leaveApplication.getDepHeadApproveDate() + ";";
                                logCurr += "" + leaveApplication.getHrManApproveDate() + ";";
                                logCurr += "" + leaveApplication.getGmApproval() + ";";
                                logCurr += "" + leaveApplication.getGmApprovalDate() + ";";
                                logCurr += "" + leaveApplication.getTypeLeave() + ";";
                                logCurr += "" + leaveApplication.getTypeFormLeave() + ";";
                                logCurr += "" + leaveApplication.getReasonId() + ";";
                                logCurr += "" + leaveApplication.getLeaveAppDiffPeriod() + ";";

                                /* data logCurr yg telah diinisalisasi kemudian dipakai untuk set ke logPrev, dan logCurr */
                                /* data struktur perusahaan didapat dari pengguna yang login melalui AppUser */
                                logSysHistory.setCompanyId(emp.getCompanyId());
                                logSysHistory.setDivisionId(emp.getDivisionId());
                                logSysHistory.setDepartmentId(emp.getDepartmentId());
                                logSysHistory.setSectionId(emp.getSectionId());
                                /* mencatat item yang diedit */
                                logSysHistory.setLogEditedUserId(leaveApplication.getOID());
                                /* setelah di set maka lakukan proses insert ke table logSysHistory */
                                PstLogSysHistory.insertExc(logSysHistory);
                            }
                        }
                        // process leave application detail
                        if (vectOfLeaveAppDetail != null && vectOfLeaveAppDetail.size() > 0) {

                            int leaveAppCount = vectOfLeaveAppDetail.size();
                            for (int ij = 0; ij < leaveAppCount; ij++) {
                                LeaveApplicationDetail objLeaveApplicationDetail = (LeaveApplicationDetail) vectOfLeaveAppDetail.get(ij);
                                objLeaveApplicationDetail.setLeaveMainOid(oid);
                                long oidLeaveDetail = PstLeaveApplicationDetail.insertExc(objLeaveApplicationDetail);

                            }
                        }

                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oid);
                        leaveApplication.setListOfDetail(listOfDetail);
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
                        long oid = pstLeaveApplication.updateExc(this.leaveApplication);
                        if (oid != 0) {
                            msgString = resultText[language][RSLT_CREATE_FORM_LEAVE_SUCCESS];
                        }
                        // process leave application detail
                        if (vectOfLeaveAppDetail != null && vectOfLeaveAppDetail.size() > 0) {
                            int intDelResult = PstLeaveApplicationDetail.deleteByApplicationMain(oid);

                            int leaveAppCount = vectOfLeaveAppDetail.size();
                            for (int ij = 0; ij < leaveAppCount; ij++) {
                                LeaveApplicationDetail objLeaveApplicationDetail = (LeaveApplicationDetail) vectOfLeaveAppDetail.get(ij);
                                objLeaveApplicationDetail.setLeaveMainOid(oid);
                                long oidLeaveDetail = PstLeaveApplicationDetail.insertExc(objLeaveApplicationDetail);
                            }
                        }

                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                sendEmailNew(this.leaveApplication, approot, 0);
                break;

            case Command.EDIT:
                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);

                        // get data detail
                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.POST:
                if (oidLeaveApplication != 0) {
                    try {
                        boolean status_excecution = SessLeaveApplication.processExecute(oidLeaveApplication);
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);
                        // get data detail
                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                     //update by satrya 2012-07-27
            //untuk execute Leave Form

                  //end
            case Command.ASK:
                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);

                        // get data detail
                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidLeaveApplication != 0) {
                    try {
                        // delete leave item in detail
                        int delResult = PstLeaveApplicationDetail.deleteByApplicationMain(oidLeaveApplication);

                        long oid = PstLeaveApplication.deleteExc(oidLeaveApplication);
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

            case Command.UNLOCK:

                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);
                        docPrev = leaveApplication.getDocStatus();
                    } catch (Exception exc) {
                        System.out.println("Exc when fetch LeaveApplication entity : " + exc.toString());
                    }
                }

                LeaveApplication leaveApplicationx = new LeaveApplication();
                frmLeaveApplication.requestEntityObjectOnlyReason(leaveApplicationx);

                leaveApplication.setLeaveReason(leaveApplicationx.getLeaveReason());

                try {
                    long oid = pstLeaveApplication.updateExc(this.leaveApplication);
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                    return getControlMsgId(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                }

                break;

            default:

        }
        return rsCode;
    }

    /**
     * <pre>create by satrya : 2013-12-10</pre>
     * <pre>Keterangan action untuk yg ada login id</pre>
     *
     * @param cmd : command
     * @param oidLeaveApplication : oid Leave application
     * @param vectOfLeaveAppDetail :
     * @param approot
     * @param oidEmployeeLogin : user yg login
     * @return int
     */
    public int action(int cmd, long oidLeaveApplication, Vector vectOfLeaveAppDetail, String approot, long oidEmployeeLogin) {
        msgString = "";
        //update by devin 2014-04-09
        boolean notSamePeriodLeaveKonfig = false;
        try {
            notSamePeriodLeaveKonfig = Boolean.parseBoolean(PstSystemProperty.getValueByName("SET_CONFIGURATION_LEAVE_NOT_SAME_PERIOD"));
        } catch (Exception exc) {
            notSamePeriodLeaveKonfig = false;
            System.out.println("Exc SET_CONFIGURATION_LEAVE_NOT_SAME_PERIOD" + exc);
        }

        I_Leave leaveConfig = null;
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        int excCode = I_DBExceptionInfo.NO_EXCEPTION;
        int rsCode = RSLT_OK;
        int sysLog = Integer.parseInt(String.valueOf(PstSystemProperty.getPropertyLongbyName("SET_USER_ACTIVITY_LOG")));
        String logField = "";
        String logPrev = "";
        String logCurr = "";
        //long sysLog = 1;
        String logDetail = "";
        Date nowDate = new Date();
        AppUser appUser = new AppUser();
        Employee emp = new Employee();
        long userId = oidEmployeeLogin;
        try {
            appUser = PstAppUser.fetch(userId);
            emp = PstEmployee.fetchExc(appUser.getEmployeeId());
        } catch (Exception e) {
            System.out.println("Get AppUser: userId: " + e.toString());
        }
        switch (cmd) {

            case Command.ADD:
                break;

            case Command.SAVE:

                int docPrev = -1;
                int docAfter = -1;

                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);
                        docPrev = leaveApplication.getDocStatus();
                    } catch (Exception exc) {
                        System.out.println("Exc when fetch LeaveApplication entity : " + exc.toString());
                    }
                }

                frmLeaveApplication.requestEntityObject(leaveApplication);
                docAfter = leaveApplication.getDocStatus();
                if (docPrev == PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT
                        && docAfter == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
                    Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                    Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                    Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                    Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());
                    if ((listTknAl == null || listTknAl.size() < 1) && (listTknLl == null || listTknLl.size() < 1)
                            && (listTknDp == null || listTknDp.size() < 1) && (listTknSp == null || listTknSp.size() < 1)) {
                        msgString = "Leave application has no detail of AL, LL or DP, cannot be set to TO BE APPROVE";
                        return RSLT_FORM_INCOMPLETE;
                    }
                }
                if (frmLeaveApplication.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                int maxApproval = leaveConfig.getMaxApproval(leaveApplication.getEmployeeId());
                boolean stsDokApprov = false;

                // if doc status Draft
                if (leaveApplication.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT) {
                    leaveApplication.setDepHeadApproval(0);
                    leaveApplication.setDepHeadApproveDate(null);
                    leaveApplication.setHrManApproveDate(null);
                    leaveApplication.setHrManApproval(0);
                    leaveApplication.setGmApproval(0);
                    leaveApplication.setGmApprovalDate(null);

                } else if (leaveApplication.getDocStatus() == 1) {

                    if (leaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_1) {
                        if (leaveApplication.getDepHeadApproval() != 0) {
                            stsDokApprov = true;
                            leaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                        }
                    } else if (leaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_2) {
                        if (leaveApplication.getDepHeadApproval() != 0 && leaveApplication.getHrManApproval() != 0) {
                            stsDokApprov = true;
                            leaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                        }
                    } else if (leaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_3) {
                        if (leaveApplication.getGmApproval() != 0) {
                            stsDokApprov = true;
                            leaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                        }
                    }
                }

                /**
                 * @DESC : PENGECEK STATUS SCHEDULE DARI APPROVED KE DRAFT Jika
                 * iLeaveMinuteEnable =1 maka menggunakan konfigurasi jam-jam'an
                 */
                //update by satrya 2012-08-03
                int iLeaveMinuteEnable = 0;
                try {
                    iLeaveMinuteEnable = Integer.parseInt(PstSystemProperty.getValueByName("LEAVE_MINUTE_ENABLE"));//menambahkan system properties
                } catch (Exception e) {
                    System.out.println("Exeception LEAVE_MINUTE_ENABLE:" + e);
                }

                if (docPrev != -1 && docAfter != -1
                        && docPrev == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED
                        && docAfter == PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT && iLeaveMinuteEnable != 1) {

                    Vector listEmpSchedule = new Vector();

                    AlStockTaken alStockTaken = new AlStockTaken();
                    LlStockTaken llStockTaken = new LlStockTaken();
                    DpStockTaken dpStockTaken = new DpStockTaken();
                    SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();

                    Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                    Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                    Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                    Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());

                    for (int idxAl = 0; idxAl < listTknAl.size(); idxAl++) {

                        alStockTaken = (AlStockTaken) listTknAl.get(idxAl);
                        Date strtDate = alStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxAlTkn = 0; idxAlTkn < alStockTaken.getTakenQty(); idxAlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }

                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxLl = 0; idxLl < listTknLl.size(); idxLl++) {

                        llStockTaken = (LlStockTaken) listTknLl.get(idxLl);
                        Date strtDate = llStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxLlTkn = 0; idxLlTkn < llStockTaken.getTakenQty(); idxLlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }

                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxDp = 0; idxDp < listTknDp.size(); idxDp++) {

                        dpStockTaken = (DpStockTaken) listTknDp.get(idxDp);
                        Date strtDate = dpStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxDpTkn = 0; idxDpTkn < dpStockTaken.getTakenQty(); idxDpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");

                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxSp = 0; idxSp < listTknSp.size(); idxSp++) {

                        specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(idxSp);
                        Date strtDate = specialUnpaidLeaveTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxSpTkn = 0; idxSpTkn < specialUnpaidLeaveTaken.getTakenQty(); idxSpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                        for (int k = 0; k < listEmpSchedule.size(); k++) {

                            EmpSchedule objAktifSchedule = new EmpSchedule();
                            objAktifSchedule = (EmpSchedule) listEmpSchedule.get(k);

                            EmpSchedule objSchedule = new EmpSchedule();

                            try {
                                objSchedule = PstEmpSchedule.fetchExc(objAktifSchedule.getOID());
                            } catch (Exception e) {
                                System.out.println("Exeption " + e.toString());
                            }

                            Period period = new Period();

                            try {
                                period = PstPeriod.fetchExc(objSchedule.getPeriodId());
                            } catch (Exception e) {
                                System.out.println("Exception " + e.toString());
                            }

                            int diffPeriod = 0;

                            diffPeriod = SessLeaveApplication.DATEDIFF(period.getEndDate(), period.getStartDate());

                            diffPeriod = diffPeriod + 1;

                            Date startDate = new Date();

                            Date newDate = new Date();

                            startDate = period.getStartDate();

                            for (int x = 0; x < diffPeriod; x++) {

                                AbsenceAnalyser.checkEmployeeAbsenceAutomatic(startDate, this.leaveApplication.getEmployeeId());
                                LatenessAnalyser.EmployeeLateness(startDate, this.leaveApplication.getEmployeeId());

                                long tmpDate = startDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                startDate = newDate;

                            }
                        }
                    }
                }

                /**
                 * @DESC : UNTUK STATUS CANCEL Jika iLeaveMinuteEnable =1 maka
                 * menggunakan konfigurasi jam-jam'an
                 */
                //update by satrya 2012-08-06
                if (leaveApplication.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_CANCELED && iLeaveMinuteEnable != 1) {

                    Vector listEmpSchedule = new Vector();

                    AlStockTaken alStockTaken = new AlStockTaken();
                    LlStockTaken llStockTaken = new LlStockTaken();
                    DpStockTaken dpStockTaken = new DpStockTaken();
                    SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();

                    Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                    Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                    Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                    Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());

                    for (int idxAl = 0; idxAl < listTknAl.size(); idxAl++) {

                        alStockTaken = (AlStockTaken) listTknAl.get(idxAl);
                        Date strtDate = alStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxAlTkn = 0; idxAlTkn < alStockTaken.getTakenQty(); idxAlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status schedule
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxLl = 0; idxLl < listTknLl.size(); idxLl++) {

                        llStockTaken = (LlStockTaken) listTknLl.get(idxLl);
                        Date strtDate = llStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxLlTkn = 0; idxLlTkn < llStockTaken.getTakenQty(); idxLlTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                ///update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }

                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxDp = 0; idxDp < listTknDp.size(); idxDp++) {

                        dpStockTaken = (DpStockTaken) listTknDp.get(idxDp);
                        Date strtDate = dpStockTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxDpTkn = 0; idxDpTkn < dpStockTaken.getTakenQty(); idxDpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");

                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    for (int idxSp = 0; idxSp < listTknSp.size(); idxSp++) {

                        specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(idxSp);
                        Date strtDate = specialUnpaidLeaveTaken.getTakenDate();
                        Date newDate = new Date();

                        for (int idxSpTkn = 0; idxSpTkn < specialUnpaidLeaveTaken.getTakenQty(); idxSpTkn++) {

                            long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                            if (schId != 0) {

                                boolean same = false;

                                if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                    for (int i = 0; i < listEmpSchedule.size(); i++) {

                                        EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                        objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                        if (objEmpScheduleProcess.getOID() == schId) {
                                            same = true;
                                        }
                                    }
                                }
                                if (same == false) {
                                    EmpSchedule empScheduleProcess = new EmpSchedule();
                                    empScheduleProcess.setOID(schId);
                                    listEmpSchedule.add(empScheduleProcess);
                                }
                                //update status
                                int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                            }
                            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            strtDate = newDate;
                        }
                    }

                    if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                        for (int k = 0; k < listEmpSchedule.size(); k++) {

                            EmpSchedule objAktifSchedule = new EmpSchedule();
                            objAktifSchedule = (EmpSchedule) listEmpSchedule.get(k);

                            EmpSchedule objSchedule = new EmpSchedule();

                            try {
                                objSchedule = PstEmpSchedule.fetchExc(objAktifSchedule.getOID());
                            } catch (Exception e) {
                                System.out.println("Exeption " + e.toString());
                            }

                            Period period = new Period();

                            try {
                                period = PstPeriod.fetchExc(objSchedule.getPeriodId());
                            } catch (Exception e) {
                                System.out.println("Exception " + e.toString());
                            }

                            int diffPeriod = 0;

                            diffPeriod = SessLeaveApplication.DATEDIFF(period.getEndDate(), period.getStartDate());

                            diffPeriod = diffPeriod + 1;

                            Date startDate = new Date();

                            Date newDate = new Date();

                            startDate = period.getStartDate();

                            for (int x = 0; x < diffPeriod; x++) {

                                AbsenceAnalyser.checkEmployeeAbsenceAutomatic(startDate, this.leaveApplication.getEmployeeId());
                                LatenessAnalyser.EmployeeLateness(startDate, this.leaveApplication.getEmployeeId());

                                long tmpDate = startDate.getTime() + (24 * 60 * 60 * 1000);
                                newDate = new Date(tmpDate);
                                startDate = newDate;

                            }
                        }
                    }
                }

                /**
                 * @DESC : PENGECEKAN CONFIGURASI UPDATE SCHEDULE jika
                 * iLeaveMinuteEnable = 0 maka mengukan konfigurasi full day
                 */
                 //update by satrya 2012-08-03
                try {
                    if (SessEmpSchedule.GetConfigurasiUpdtSch() == PstEmpSchedule.UPDATE_SCHEDULE_AFTER_APPROVED && iLeaveMinuteEnable != 1) {
                        // if (SessEmpSchedule.GetConfigurasiUpdtSch() == PstEmpSchedule.UPDATE_SCHEDULE_AFTER_APPROVED) {
                        /**
                         * @DEC : UNTUK MENGUPDATE STATUS SCHEDULE JIKA STATUS
                         * DOCUMENT APPROVED
                         */
                        if (stsDokApprov == true || leaveApplication.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {

                            Vector listEmpSchedule = new Vector();

                            AlStockTaken alStockTaken = new AlStockTaken();
                            LlStockTaken llStockTaken = new LlStockTaken();
                            DpStockTaken dpStockTaken = new DpStockTaken();
                            SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();

                            Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
                            Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
                            Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
                            Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());

                            String oidAl = String.valueOf(PstSystemProperty.getValueByName("OID_AL"));
                            String oidLl = String.valueOf(PstSystemProperty.getValueByName("OID_LL"));
                            String oidDp = String.valueOf(PstSystemProperty.getValueByName("OID_DP"));

                            for (int idxAl = 0; idxAl < listTknAl.size(); idxAl++) {

                                alStockTaken = (AlStockTaken) listTknAl.get(idxAl);
                                Date strtDate = alStockTaken.getTakenDate();
                                Date newDate = new Date();

                                for (int idxAlTkn = 0; idxAlTkn < alStockTaken.getTakenQty(); idxAlTkn++) {

                                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                    if (schId != 0) {

                                        boolean same = false;

                                        if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                            for (int i = 0; i < listEmpSchedule.size(); i++) {

                                                EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                                objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                                if (objEmpScheduleProcess.getOID() == schId) {
                                                    same = true;
                                                }
                                            }
                                        }
                                        if (same == false) {
                                            EmpSchedule empScheduleProcess = new EmpSchedule();
                                            empScheduleProcess.setOID(schId);
                                            listEmpSchedule.add(empScheduleProcess);
                                        }
                                        //update status
                                        int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidAl);
                                    }
                                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                    newDate = new Date(tmpDate);
                                    strtDate = newDate;
                                }
                            }

                            for (int idxLl = 0; idxLl < listTknLl.size(); idxLl++) {

                                llStockTaken = (LlStockTaken) listTknLl.get(idxLl);
                                Date strtDate = llStockTaken.getTakenDate();
                                Date newDate = new Date();

                                for (int idxLlTkn = 0; idxLlTkn < llStockTaken.getTakenQty(); idxLlTkn++) {

                                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                    if (schId != 0) {

                                        boolean same = false;

                                        if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                            for (int i = 0; i < listEmpSchedule.size(); i++) {

                                                EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                                objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                                if (objEmpScheduleProcess.getOID() == schId) {
                                                    same = true;
                                                }
                                            }
                                        }
                                        if (same == false) {
                                            EmpSchedule empScheduleProcess = new EmpSchedule();
                                            empScheduleProcess.setOID(schId);
                                            listEmpSchedule.add(empScheduleProcess);
                                        }

                                        int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidLl);
                                    }

                                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                    newDate = new Date(tmpDate);
                                    strtDate = newDate;
                                }
                            }

                            for (int idxDp = 0; idxDp < listTknDp.size(); idxDp++) {

                                dpStockTaken = (DpStockTaken) listTknDp.get(idxDp);
                                Date strtDate = dpStockTaken.getTakenDate();
                                Date newDate = new Date();

                                for (int idxDpTkn = 0; idxDpTkn < dpStockTaken.getTakenQty(); idxDpTkn++) {

                                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                    if (schId != 0) {

                                        boolean same = false;

                                        if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                            for (int i = 0; i < listEmpSchedule.size(); i++) {

                                                EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                                objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                                if (objEmpScheduleProcess.getOID() == schId) {
                                                    same = true;
                                                }
                                            }
                                        }
                                        if (same == false) {
                                            EmpSchedule empScheduleProcess = new EmpSchedule();
                                            empScheduleProcess.setOID(schId);
                                            listEmpSchedule.add(empScheduleProcess);
                                        }

                                        int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidDp);

                                    }
                                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                    newDate = new Date(tmpDate);
                                    strtDate = newDate;
                                }
                            }

                            for (int idxSp = 0; idxSp < listTknSp.size(); idxSp++) {

                                specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(idxSp);
                                Date strtDate = specialUnpaidLeaveTaken.getTakenDate();
                                Date newDate = new Date();
                                String oidSp = "" + specialUnpaidLeaveTaken.getScheduledId();

                                for (int idxSpTkn = 0; idxSpTkn < specialUnpaidLeaveTaken.getTakenQty(); idxSpTkn++) {

                                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);

                                    if (schId != 0) {

                                        boolean same = false;

                                        if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                            for (int i = 0; i < listEmpSchedule.size(); i++) {

                                                EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                                objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);

                                                if (objEmpScheduleProcess.getOID() == schId) {
                                                    same = true;
                                                }
                                            }
                                        }
                                        if (same == false) {
                                            EmpSchedule empScheduleProcess = new EmpSchedule();
                                            empScheduleProcess.setOID(schId);
                                            listEmpSchedule.add(empScheduleProcess);
                                        }

                                        int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidSp);
                                    }
                                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                                    newDate = new Date(tmpDate);
                                    strtDate = newDate;
                                }
                            }

                            if (listEmpSchedule != null && listEmpSchedule.size() > 0) {

                                for (int k = 0; k < listEmpSchedule.size(); k++) {

                                    EmpSchedule objAktifSchedule = new EmpSchedule();
                                    objAktifSchedule = (EmpSchedule) listEmpSchedule.get(k);

                                    EmpSchedule objSchedule = new EmpSchedule();

                                    try {
                                        objSchedule = PstEmpSchedule.fetchExc(objAktifSchedule.getOID());
                                    } catch (Exception e) {
                                        System.out.println("Exeption " + e.toString());
                                    }

                                    Period period = new Period();

                                    try {
                                        period = PstPeriod.fetchExc(objSchedule.getPeriodId());
                                    } catch (Exception e) {
                                        System.out.println("Exception " + e.toString());
                                    }

                                    int diffPeriod = 0;

                                    diffPeriod = SessLeaveApplication.DATEDIFF(period.getEndDate(), period.getStartDate());

                                    diffPeriod = diffPeriod + 1;

                                    Date startDate = new Date();

                                    Date newDate = new Date();

                                    startDate = period.getStartDate();

                                    for (int x = 0; x < diffPeriod; x++) {

                                        AbsenceAnalyser.checkEmployeeAbsenceAutomatic(startDate, this.leaveApplication.getEmployeeId());
                                        LatenessAnalyser.EmployeeLateness(startDate, this.leaveApplication.getEmployeeId());

                                        long tmpDate = startDate.getTime() + (24 * 60 * 60 * 1000);
                                        newDate = new Date(tmpDate);
                                        startDate = newDate;
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception exc) {
                    System.out.println("Execption PENGECEKAN CONFIGURASI UPDATE SCHEDULE" + exc);
                }

                boolean alAllowance = leaveConfig.getALallowance(this.leaveApplication);
                boolean alLllowance = leaveConfig.getLLallowance(this.leaveApplication);

                if (leaveApplication.getOID() == 0) {
                    try {
                        long oid = pstLeaveApplication.insertExc(this.leaveApplication);

                        //buatkan save carrer path
                        if (sysLog != 0) { /* kondisi jika sysLog == 1, maka proses di bawah ini dijalankan*/

                            String className = leaveApplication.getClass().getName();
                            LogSysHistory logSysHistory = new LogSysHistory();

                            String reqUrl = "leave_app_edit.jsp?FRM_FLD_LEAVE_APPLICATION_ID=" + leaveApplication.getOID();
                            /* Lakukan set data ke entity logSysHistory */
                            logSysHistory.setLogDocumentId(0);
                            logSysHistory.setLogUserId(userId);
                            logSysHistory.setApproverId(userId);
                            logSysHistory.setApproveDate(nowDate);
                            logSysHistory.setLogLoginName(appUser.getLoginId());
                            logSysHistory.setLogDocumentNumber("");
                            logSysHistory.setLogDocumentType(className); //entity
                            logSysHistory.setLogUserAction("ADD"); // command
                            logSysHistory.setLogOpenUrl(reqUrl); // locate jsp
                            logSysHistory.setLogUpdateDate(nowDate);
                            logSysHistory.setLogApplication(I_LogHistory.SYSTEM_NAME[I_LogHistory.SYSTEM_HAIRISMA]); // interface
                                    /* Inisialisasi logField dengan menggambil field EmpEducation */
                            /* Tips: ambil data field dari persistent */
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_TYPE_LEAVE] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_REASON_ID] + " ;";
                            logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_DIFFERENT_PERIOD] + " ;";

                            /* data logField yg telah terisi kemudian digunakan untuk setLogDetail */
                            logSysHistory.setLogDetail(logField); // apa yang dirubah
                                    /* inisialisasi value, yaitu logCurr */
                            logCurr += "" + leaveApplication.getOID() + ";";
                            logCurr += "" + leaveApplication.getSubmissionDate() + ";";
                            logCurr += "" + leaveApplication.getEmployeeId() + ";";
                            logCurr += "" + leaveApplication.getLeaveReason() + ";";
                            logCurr += "" + leaveApplication.getDepHeadApproval() + ";";
                            logCurr += "" + leaveApplication.getHrManApproval() + ";";
                            logCurr += "" + leaveApplication.getDocStatus() + ";";
                            logCurr += "" + leaveApplication.getDepHeadApproveDate() + ";";
                            logCurr += "" + leaveApplication.getHrManApproveDate() + ";";
                            logCurr += "" + leaveApplication.getGmApproval() + ";";
                            logCurr += "" + leaveApplication.getGmApprovalDate() + ";";
                            logCurr += "" + leaveApplication.getTypeLeave() + ";";
                            logCurr += "" + leaveApplication.getTypeFormLeave() + ";";
                            logCurr += "" + leaveApplication.getReasonId() + ";";
                            logCurr += "" + leaveApplication.getLeaveAppDiffPeriod() + ";";

                            /* data logCurr yg telah diinisalisasi kemudian dipakai untuk set ke logPrev, dan logCurr */
                            /* data struktur perusahaan didapat dari pengguna yang login melalui AppUser */
                            logSysHistory.setCompanyId(emp.getCompanyId());
                            logSysHistory.setDivisionId(emp.getDivisionId());
                            logSysHistory.setDepartmentId(emp.getDepartmentId());
                            logSysHistory.setSectionId(emp.getSectionId());
                            /* mencatat item yang diedit */
                            logSysHistory.setLogEditedUserId(leaveApplication.getOID());
                            /* setelah di set maka lakukan proses insert ke table logSysHistory */
                            PstLogSysHistory.insertExc(logSysHistory);
                        }
                        // process leave application detail
                        if (vectOfLeaveAppDetail != null && vectOfLeaveAppDetail.size() > 0) {

                            int leaveAppCount = vectOfLeaveAppDetail.size();
                            for (int ij = 0; ij < leaveAppCount; ij++) {
                                LeaveApplicationDetail objLeaveApplicationDetail = (LeaveApplicationDetail) vectOfLeaveAppDetail.get(ij);
                                objLeaveApplicationDetail.setLeaveMainOid(oid);
                                long oidLeaveDetail = PstLeaveApplicationDetail.insertExc(objLeaveApplicationDetail);

                            }
                        }

                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oid);
                        leaveApplication.setListOfDetail(listOfDetail);
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
                        //test
                        //update by satrya 2013-12-23
                        long oid = 0;
                        if (notSamePeriodLeaveKonfig) {

                            //cari taken al , mislakan 20 s/d 23
                            //lalu di pecah berdasarkan periode akhir'nya
                            Vector vAlStockTaken = PstAlStockTaken.getAlTaken(leaveApplication.getOID(), leaveApplication.getEmployeeId());
                            Hashtable hashPeriod = new Hashtable();
                            Date takenDate = new Date();
                            Date takenDateFinish = new Date();
                            Hashtable leaveOid = new Hashtable();

                            if (leaveApplication.getDocStatus() != I_DocStatus.DOCUMENT_STATUS_DRAFT && leaveApplication.getDocStatus() != I_DocStatus.DOCUMENT_STATUS_CANCELLED) {

                                if (vAlStockTaken != null && vAlStockTaken.size() > 0) {
                                    //boolean takenAwal=true;
                                    Vector listPeriodDate = new Vector();
                                    Hashtable bulan = new Hashtable();
                                    Period periodc = new Period();
                                    long oidPeriod = 0;
                                    Vector vBulan = new Vector();

                                    for (int x = 0; x < vAlStockTaken.size(); x++) {
                                        AlStockTaken alStockTaken = (AlStockTaken) vAlStockTaken.get(x);
                                        AlStockTaken dev = (AlStockTaken) vAlStockTaken.get(x);
                                        int c = 0;
                                        if (vAlStockTaken.size() > 1) {
                                            for (int i = 0; i < vAlStockTaken.size(); i++) {
                                                AlStockTaken objAlStockTaken = (AlStockTaken) vAlStockTaken.get(i);
                                                takenDate = objAlStockTaken.getTakenDate();
                                                takenDateFinish = objAlStockTaken.getTakenFinnishDate();
                                                if (c == 1) {
                                                    vBulan = PstPeriod.getListStartEndDatePeriod(takenDate, takenDateFinish);
                                                    periodc = (Period) vBulan.get(0);
                                                    oidPeriod = periodc.getOID();
                                                    if (bulan.containsKey(oidPeriod) == false) {
                                                        vBulan = PstPeriod.getListStartEndDatePeriod(takenDate, takenDateFinish);
                                                        periodc = (Period) vBulan.get(0);
                                                        listPeriodDate.add(periodc);
                                                    }
                                                }
                                                if (takenDate != null && takenDateFinish != null && c == 0) {
                                                    listPeriodDate = PstPeriod.getListStartEndDatePeriod(takenDate, takenDateFinish);
                                                    c = 1;
                                                    periodc = (Period) listPeriodDate.get(0);
                                                    bulan.put(periodc.getOID(), true);
                                                }

                                            }

                                        } else {
                                            listPeriodDate = PstPeriod.getListStartEndDatePeriod(alStockTaken.getTakenDate(), alStockTaken.getTakenFinnishDate());
                                        }

                                        //artinya jika periode yg di temukan lebih dari 1 maka artinya dia beda period
                                        Period periodd = (Period) listPeriodDate.get(0);
                                        long periodz = periodd.getOID();

                                        Date dtTaken = alStockTaken.getTakenDate();
                                        Date dtTakenFinish = alStockTaken.getTakenFinnishDate();
                                        Date cekz = (Date) dev.getTakenDate();
                                    // boolean cekAwalPayPeriod=true;

                                        long updateLeave = 0;
                                        Hashtable hashSamePeriod = new Hashtable();
                                        long periodId = 0;
                                        if (listPeriodDate != null && listPeriodDate.size() > 1) {
                                            /*for(int idxPer=0; idxPer<listPeriodDate.size();idxPer++){
                                             Period period = (Period)listPeriodDate.get(idxPer); 
                                            
                                             }*/

                                            boolean takenAwal = true;
                                            Date dtTakenTemp = (Date) dtTaken.clone();
                                            dtTakenTemp.setHours(0);
                                            dtTakenTemp.setMinutes(0);
                                            dtTakenTemp.setSeconds(0);
                                            Date dtTakenFinishTemp = (Date) dtTakenFinish.clone();
                                            dtTakenFinishTemp.setHours(0);
                                            dtTakenFinishTemp.setMinutes(0);
                                            dtTakenFinishTemp.setSeconds(0);
                                            long diffStartToFinish = dtTakenFinishTemp.getTime() - dtTakenTemp.getTime();
                                            int itDate = Integer.parseInt(String.valueOf(diffStartToFinish / 86400000)) + 1;
                                            for (int idxdt = 0; idxdt < itDate; idxdt++) {

                                                if (itDate == 0) {
                                                    dtTaken = (Date) dtTaken.clone();
                                                    dtTakenFinish = (Date) dtTakenFinish.clone();

                                                } else if (idxdt == 0 && itDate > 0) {

                                                    dtTaken = (Date) dtTaken.clone();

                                                    dtTakenFinish = (Date) dtTaken.clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                    ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, alStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                    int dtIdxJam = 17;
                                                    int dtIdxMin = 0;
                                                    //update by devin 2014-04-09 
                                                    int dtIdxInJam = 8;
                                                    int dtIdxInMin = 0;
                                                    if (scheduleSymbol != null && scheduleSymbol.getTimeOut() != null && scheduleSymbol.getTimeIn() != null) {
                                                        dtIdxJam = scheduleSymbol.getTimeOut().getHours();
                                                        dtIdxMin = scheduleSymbol.getTimeOut().getMinutes();
                                                        dtIdxInJam = scheduleSymbol.getTimeIn().getHours();
                                                        dtIdxInMin = scheduleSymbol.getTimeIn().getMinutes();
                                                    }
                                                    dtTaken.setHours(dtIdxInJam);
                                                    dtTaken.setMinutes(dtIdxInMin);
                                                    dtTaken.setSeconds(0);
                                                    dtTakenFinish.setHours(dtIdxJam);
                                                    dtTakenFinish.setMinutes(dtIdxMin);
                                                    dtTakenFinish.setSeconds(0);
                                                    updateLeave = periodId;

                                                } else if (itDate == idxdt && itDate > 0) {
                                                    dtTaken = (Date) dtTakenFinish.clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                    ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, alStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                    int dtIdxJam = 17;
                                                    int dtIdxMin = 0;
                                                    if (scheduleSymbol != null && scheduleSymbol.getTimeIn() != null) {
                                                        dtIdxJam = scheduleSymbol.getTimeIn().getHours();
                                                        dtIdxMin = scheduleSymbol.getTimeIn().getMinutes();
                                                    }
                                                    dtTaken.setHours(dtIdxJam);
                                                    dtTaken.setMinutes(dtIdxMin);
                                                    dtTaken.setSeconds(0);

                                                    dtTakenFinish = (Date) dtTakenFinish.clone();

                                                } else {

                                                    if (leaveApplication.getDepHeadApproval() == 0) {
                                                        dtTaken = (Date) dtTaken.clone();

                                                        dtTakenFinish = (Date) new Date(dtTaken.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                    } else {
                                                        Date dtTime = (Date) cekz.clone();
                                                        dtTaken = (Date) dtTaken.clone();

                                                        dtTakenFinish = (Date) new Date(dtTime.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));

                                                        if (hashPeriod.containsKey(periodId) == true) {
                                                            Date dtTimex = (Date) cekz.clone();
                                                            dtTaken = (Date) dtTaken.clone();

                                                            dtTakenFinish = (Date) new Date(dtTimex.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                            periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                        } else {

                                                            dtTaken = (Date) new Date(cekz.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();

                                                            dtTakenFinish = (Date) dtTaken.clone();
                                                            periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTaken, "yyyy-MM-dd"));
                                                        }

                                                    }

                                                    ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, alStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                    int dtIdxJamIn = 8;
                                                    int dtIdxMinIn = 0;
                                                    int dtIdxJamOut = 17;
                                                    int dtIdxMinOut = 0;
                                                    if (scheduleSymbol != null && scheduleSymbol.getTimeIn() != null && scheduleSymbol.getTimeOut() != null) {
                                                        dtIdxJamIn = scheduleSymbol.getTimeIn().getHours();
                                                        dtIdxMinIn = scheduleSymbol.getTimeIn().getMinutes();
                                                        dtIdxJamOut = scheduleSymbol.getTimeOut().getHours();
                                                        dtIdxMinOut = scheduleSymbol.getTimeOut().getMinutes();
                                                    }
                                                    dtTaken.setHours(dtIdxJamIn);
                                                    dtTaken.setMinutes(dtIdxMinIn);
                                                    dtTaken.setSeconds(0);

                                                    dtTakenFinish.setHours(dtIdxJamOut);
                                                    dtTakenFinish.setMinutes(dtIdxMinOut);
                                                    dtTakenFinish.setSeconds(0);
                                                }
                                                alStockTaken.setTakenDate(dtTaken);
                                                alStockTaken.setTakenFinnishDate(dtTakenFinish);
                                                /*for(int idxPer=0; idxPer<listPeriodDate.size();idxPer++){
                                                 Period period = (Period)listPeriodDate.get(idxPer); 
                                                 hashSamePeriod.put(period.getOID(),periodId);
                                                 }*/
                                                periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                long cek = 0;
                                                if (oidLeaveApplication != 0) {
                                                    cek = PstLeaveApplication.cariDiferent(oidLeaveApplication);
                                                }
                                                //devin
                                                if (leaveApplication.getDepHeadApproval() == 0 && leaveApplication.getLeaveAppDiffPeriod() > 0) {
                                                    for (int i = 0; i < vAlStockTaken.size(); i++) {
                                                        Date takenDates = new Date();
                                                        Date takenDateFinishs = new Date();
                                                        AlStockTaken objAlStockTaken = (AlStockTaken) vAlStockTaken.get(i);
                                                        takenDate = objAlStockTaken.getTakenDate();
                                                        takenDateFinish = objAlStockTaken.getTakenFinnishDate();
                                                        if (listPeriodDate.size() > 1) {
                                                            leaveApplication.setLeaveAppDiffPeriod(0);
                                                            oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                        }

                                                    }
                                                }

                                                // long samePeriod=hashSamePeriod==null || hashSamePeriod.size()==0?0:(Long)hashSamePeriod.get(periodId);
                                                if (leaveApplication.getDepHeadApproval() == 0 || cek > 0 || hashPeriod.containsKey(periodId) == true) {
                                                    try {
                                                        //update by devin 2014-04-02
                                                        float qty = 0;
                                                        qty = DateCalc.workDayDifference(alStockTaken.getTakenDate(), alStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                        alStockTaken.setTakenQty(qty);
                                                        long oidsuccess = PstAlStockTaken.updateExc(alStockTaken);

                                                        leaveApplication.getEmployeeId();

                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                        takenAwal = false;
                                                        hashPeriod.put(periodId, true);
                                                        leaveOid.put("" + periodId, oid);
                                                    } catch (Exception exc) {
                                                        System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                    }
                                                } else if (listPeriodDate.size() > 1 && updateLeave == periodId && leaveApplication.getDepHeadApproval() > 0) {
                                                    try {
                                                        float qty = 0;
                                                        qty = DateCalc.workDayDifference(alStockTaken.getTakenDate(), alStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                        alStockTaken.setTakenQty(qty);
                                                        long oidsuccess = PstAlStockTaken.updateExc(alStockTaken);

                                                        leaveApplication.getEmployeeId();

                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                        takenAwal = false;
                                                        hashPeriod.put(periodId, true);
                                                        leaveOid.put("" + periodId, oid);
                                                    } catch (Exception exc) {
                                                        System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                    }

                                                } else if (leaveApplication.getDepHeadApproval() > 0 || leaveApplication.getHrManApproval() > 0) {
                                                    if (leaveApplication.getDepHeadApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                        try {
                                                            LeaveApplication obLeaveApplication = (LeaveApplication) leaveApplication;
                                                            obLeaveApplication.setLeaveAppDiffPeriod(leaveApplication.getOID());
                                                            obLeaveApplication.setOID(0);
                                                            long oidLeave = PstLeaveApplication.insertExc(obLeaveApplication);
                                                            leaveOid.put("" + periodId, oidLeave);

                                                        } catch (Exception exc) {
                                                            System.out.println("Error insert taken ddi ctrlStockTaken" + exc);
                                                        }
                                                    }
                                                    if (leaveOid.containsKey("" + periodId) && leaveOid.get("" + periodId) != null) {
                                                        long oidx = (Long) leaveOid.get("" + periodId);
                                                        if (oidx != 0) {
                                                            //update by devin 2014-04-02
                                                            float qty = 0;
                                                            alStockTaken.setLeaveApplicationId(oidx);
                                                            qty = DateCalc.workDayDifference(alStockTaken.getTakenDate(), alStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                            alStockTaken.setTakenQty(qty);

                                                            PstAlStockTaken.insertExc(alStockTaken);
                                                            hashPeriod.put(periodId, true);
                                                        }
                                                    } else {
                                                        hashPeriod.put(periodz, true);
                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    }
                                                    if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                        try {
                                                            PstAlStockTaken.updateExc(alStockTaken);

                                                            oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                            takenAwal = false;
                                                            hashPeriod.put(periodz, true);
                                                        } catch (Exception exc) {
                                                            System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                        }
                                                    } else if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == true) {
                                                        hashPeriod.put(periodz, true);
                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    }

                                                }
                                           //hashSamePeriod.put(periodId,periodId);

                                            }
                                         //end period

                                        } else {
                                            hashPeriod.put(periodId, true);
                                            oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                        }
                                    }
                                }
                            } else {
                                oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                // process leave application detail
                                if (vectOfLeaveAppDetail != null && vectOfLeaveAppDetail.size() > 0) {
                                    int intDelResult = PstLeaveApplicationDetail.deleteByApplicationMain(oid);

                                    int leaveAppCount = vectOfLeaveAppDetail.size();
                                    for (int ij = 0; ij < leaveAppCount; ij++) {
                                        LeaveApplicationDetail objLeaveApplicationDetail = (LeaveApplicationDetail) vectOfLeaveAppDetail.get(ij);
                                        objLeaveApplicationDetail.setLeaveMainOid(oid);
                                        long oidLeaveDetail = PstLeaveApplicationDetail.insertExc(objLeaveApplicationDetail);
                                    }
                                }

                                Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                                leaveApplication.setListOfDetail(listOfDetail);
                            }
                            //untuk mencari dp
                            Vector vDpStockTaken = PstDpStockTaken.getDpTaken(leaveApplication.getOID(), leaveApplication.getEmployeeId());
                            if (vDpStockTaken != null && vDpStockTaken.size() > 0) {
                                boolean takenAwal = true;
                                Hashtable hashPeriodd = new Hashtable();
                                Date takenDatee = new Date();
                                Date takenDateFinishh = new Date();
                                Vector listPeriodDate = new Vector();
                                Hashtable bulan = new Hashtable();
                                Period periodc = new Period();
                                long oidPeriod = 0;
                                Vector vBulan = new Vector();
                                for (int x = 0; x < vDpStockTaken.size(); x++) {
                                    DpStockTaken dPStockTaken = (DpStockTaken) vDpStockTaken.get(x);
                                    DpStockTaken dev = (DpStockTaken) vDpStockTaken.get(x);
                                    int c = 0;
                                    if (vDpStockTaken.size() > 1) {
                                        for (int i = 0; i < vDpStockTaken.size(); i++) {
                                            DpStockTaken objDpStockTaken = (DpStockTaken) vDpStockTaken.get(i);
                                            takenDatee = objDpStockTaken.getTakenDate();
                                            takenDateFinishh = objDpStockTaken.getTakenFinnishDate();
                                            if (c == 1) {
                                                vBulan = PstPeriod.getListStartEndDatePeriod(takenDatee, takenDateFinishh);
                                                periodc = (Period) vBulan.get(0);
                                                oidPeriod = periodc.getOID();
                                                if (bulan.containsKey(oidPeriod) == false) {
                                                    vBulan = PstPeriod.getListStartEndDatePeriod(takenDatee, takenDateFinishh);
                                                    periodc = (Period) vBulan.get(0);
                                                    listPeriodDate.add(periodc);
                                                }
                                            }
                                            if (takenDatee != null && takenDateFinishh != null && c == 0) {
                                                listPeriodDate = PstPeriod.getListStartEndDatePeriod(takenDatee, takenDateFinishh);
                                                c = 1;
                                                periodc = (Period) listPeriodDate.get(0);
                                                bulan.put(periodc.getOID(), true);
                                            }

                                        }

                                    } else {
                                        listPeriodDate = PstPeriod.getListStartEndDatePeriod(dPStockTaken.getTakenDate(), dPStockTaken.getTakenFinnishDate());
                                    }
                                    Period periodd = (Period) listPeriodDate.get(0);
                                    long periodz = periodd.getOID();

                                    // boolean cekAwalPayPeriod=true;
                                    Hashtable hashSamePeriod = new Hashtable();
                                    long periodId = 0;
                                     //Vector listPeriodDate = PstPeriod.getListStartEndDatePeriod(llStockTaken.getTakenDate(), llStockTaken.getTakenFinnishDate()); 
                                    //artinya jika periode yg di temukan lebih dari 1 maka artinya dia beda period
                                    Date dtTaken = dPStockTaken.getTakenDate();
                                    Date dtTakenFinish = dPStockTaken.getTakenFinnishDate();
                                    Date cekz = (Date) dev.getTakenDate();
                                    boolean cekAwalPayPeriod = true;
                                    long updateLeave = 0;

                                    if (listPeriodDate != null && listPeriodDate.size() > 1) {
                                        Date dtTakenTemp = (Date) dtTaken.clone();

                                        Date dtTakenFinishTemp = (Date) dtTakenFinish.clone();

                                        long diffStartToFinish = dtTakenFinishTemp.getTime() - dtTakenTemp.getTime();
                                        int itDate = Integer.parseInt(String.valueOf(diffStartToFinish / 86400000)) + 1;
                                        for (int idxdt = 0; idxdt < itDate; idxdt++) {

                                            if (itDate == 0) {
                                                dtTaken = (Date) dtTaken.clone();
                                                dtTakenFinish = (Date) dtTakenFinish.clone();

                                            } else if (idxdt == 0 && itDate > 0) {

                                                dtTaken = (Date) dtTaken.clone();

                                                dtTakenFinish = (Date) dtTaken.clone();
                                                periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, dPStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                int dtIdxJam = 17;
                                                int dtIdxMin = 0;
                                                int dtIdxInJam = 8;
                                                int dtIdxInMin = 0;
                                                if (scheduleSymbol != null && scheduleSymbol.getTimeOut() != null && scheduleSymbol.getTimeIn() != null) {
                                                    dtIdxJam = scheduleSymbol.getTimeOut().getHours();
                                                    dtIdxMin = scheduleSymbol.getTimeOut().getMinutes();
                                                    dtIdxInJam = scheduleSymbol.getTimeIn().getHours();
                                                    dtIdxInMin = scheduleSymbol.getTimeIn().getMinutes();
                                                }
                                                dtTaken.setHours(dtIdxInJam);
                                                dtTaken.setMinutes(dtIdxInMin);
                                                dtTaken.setSeconds(0);
                                                dtTakenFinish.setHours(dtIdxJam);
                                                dtTakenFinish.setMinutes(dtIdxMin);
                                                dtTakenFinish.setSeconds(0);
                                                updateLeave = periodId;
                                            } else if (itDate == idxdt && itDate > 0) {
                                                dtTaken = (Date) dtTakenFinish.clone();
                                                periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, dPStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                int dtIdxJam = 17;
                                                int dtIdxMin = 0;
                                                if (scheduleSymbol != null && scheduleSymbol.getTimeIn() != null) {
                                                    dtIdxJam = scheduleSymbol.getTimeIn().getHours();
                                                    dtIdxMin = scheduleSymbol.getTimeIn().getMinutes();
                                                }
                                                dtTaken.setHours(dtIdxJam);
                                                dtTaken.setMinutes(dtIdxMin);
                                                dtTaken.setSeconds(0);

                                                dtTakenFinish = (Date) dtTakenFinish.clone();

                                            } else {

                                                if (leaveApplication.getDepHeadApproval() == 0) {
                                                    dtTaken = (Date) dtTaken.clone();

                                                    dtTakenFinish = (Date) new Date(dtTaken.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                } else {
                                                    Date dtTime = (Date) cekz.clone();
                                                    dtTaken = (Date) dtTaken.clone();

                                                    dtTakenFinish = (Date) new Date(dtTime.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));

                                                    if (hashPeriod.containsKey(periodId) == true) {
                                                        Date dtTimex = (Date) cekz.clone();
                                                        dtTaken = (Date) dtTaken.clone();

                                                        dtTakenFinish = (Date) new Date(dtTimex.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                    } else {

                                                        dtTaken = (Date) new Date(cekz.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();

                                                        dtTakenFinish = (Date) dtTaken.clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTaken, "yyyy-MM-dd"));
                                                    }

                                                }

                                                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, dPStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                int dtIdxJamIn = 8;
                                                int dtIdxMinIn = 0;
                                                int dtIdxJamOut = 17;
                                                int dtIdxMinOut = 0;
                                                if (scheduleSymbol != null && scheduleSymbol.getTimeIn() != null && scheduleSymbol.getTimeOut() != null) {
                                                    dtIdxJamIn = scheduleSymbol.getTimeIn().getHours();
                                                    dtIdxMinIn = scheduleSymbol.getTimeIn().getMinutes();
                                                    dtIdxJamOut = scheduleSymbol.getTimeOut().getHours();
                                                    dtIdxMinOut = scheduleSymbol.getTimeOut().getMinutes();
                                                }
                                                dtTaken.setHours(dtIdxJamIn);
                                                dtTaken.setMinutes(dtIdxMinIn);
                                                dtTaken.setSeconds(0);

                                                dtTakenFinish.setHours(dtIdxJamOut);
                                                dtTakenFinish.setMinutes(dtIdxMinOut);
                                                dtTakenFinish.setSeconds(0);
                                            }
                                            dPStockTaken.setTakenDate(dtTaken);
                                            dPStockTaken.setTakenFinnishDate(dtTakenFinish);
                                            /*for(int idxPer=0; idxPer<listPeriodDate.size();idxPer++){
                                             Period period = (Period)listPeriodDate.get(idxPer); 
                                             hashSamePeriod.put(period.getOID(),periodId);
                                             }*/
                                            periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                            long cek = 0;
                                            if (oidLeaveApplication != 0) {
                                                cek = PstLeaveApplication.cariDiferent(oidLeaveApplication);
                                            }

                                            if (leaveApplication.getDepHeadApproval() == 0 && leaveApplication.getLeaveAppDiffPeriod() > 0) {
                                                for (int i = 0; i < vDpStockTaken.size(); i++) {
                                                    Date takenDates = new Date();
                                                    Date takenDateFinishs = new Date();
                                                    DpStockTaken objDlStockTaken = (DpStockTaken) vDpStockTaken.get(i);
                                                    takenDates = objDlStockTaken.getTakenDate();
                                                    takenDateFinishs = objDlStockTaken.getTakenFinnishDate();
                                                    if (listPeriodDate.size() > 1) {
                                                        leaveApplication.setLeaveAppDiffPeriod(0);
                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    }

                                                }
                                            }
                                            // long samePeriod=hashSamePeriod==null || hashSamePeriod.size()==0?0:(Long)hashSamePeriod.get(periodId);
                                            if (leaveApplication.getDepHeadApproval() == 0 || cek > 0 || hashPeriod.containsKey(periodId) == true) {
                                                try {
                                                    float qty = 0;
                                                    qty = DateCalc.workDayDifference(dPStockTaken.getTakenDate(), dPStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                    dPStockTaken.setTakenQty(qty);
                                                    PstDpStockTaken.updateExc(dPStockTaken);
                                                    leaveApplication.getEmployeeId();
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    takenAwal = false;
                                                    hashPeriod.put(periodId, true);
                                                    leaveOid.put("" + periodId, oid);
                                                } catch (Exception exc) {
                                                    System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                }
                                            } else if (listPeriodDate.size() > 1 && updateLeave == periodId && leaveApplication.getDepHeadApproval() > 0) {
                                                try {
                                                    float qty = 0;
                                                    qty = DateCalc.workDayDifference(dPStockTaken.getTakenDate(), dPStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                    dPStockTaken.setTakenQty(qty);
                                                    long oidsuccess = PstDpStockTaken.updateExc(dPStockTaken);

                                                    leaveApplication.getEmployeeId();

                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    takenAwal = false;
                                                    hashPeriod.put(periodId, true);
                                                    leaveOid.put("" + periodId, oid);
                                                } catch (Exception exc) {
                                                    System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                }

                                            } else if (leaveApplication.getDepHeadApproval() > 0 || leaveApplication.getHrManApproval() > 0) {
                                                if (leaveApplication.getDepHeadApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                    try {
                                                        LeaveApplication obLeaveApplication = (LeaveApplication) leaveApplication;
                                                        obLeaveApplication.setLeaveAppDiffPeriod(leaveApplication.getOID());
                                                        obLeaveApplication.setOID(0);

                                                        long oidLeave = PstLeaveApplication.insertExc(obLeaveApplication);
                                                        leaveOid.put("" + periodId, oidLeave);

                                                    } catch (Exception exc) {
                                                        System.out.println("Error insert taken ddi ctrlStockTaken" + exc);
                                                    }
                                                }
                                                if (leaveOid.containsKey("" + periodId) && leaveOid.get("" + periodId) != null) {
                                                    long oidx = (Long) leaveOid.get("" + periodId);
                                                    if (oidx != 0) {
                                                        float qty = 0;
                                                        qty = DateCalc.workDayDifference(dPStockTaken.getTakenDate(), dPStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                        dPStockTaken.setTakenQty(qty);
                                                        dPStockTaken.setLeaveApplicationId(oidx);
                                                        PstDpStockTaken.insertExc(dPStockTaken);
                                                        hashPeriod.put(periodId, true);
                                                    }
                                                } else {
                                                    hashPeriod.put(periodz, true);
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                }
                                                if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                    try {
                                                        PstDpStockTaken.updateExc(dPStockTaken);

                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                        takenAwal = false;
                                                        hashPeriod.put(periodz, true);
                                                    } catch (Exception exc) {
                                                        System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                    }
                                                } else if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == true) {
                                                    hashPeriod.put(periodz, true);
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                }
                                            }
                                           //hashSamePeriod.put(periodId,periodId);

                                        }
                                         //end period

                                    } else {
                                        hashPeriod.put(periodId, true);
                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                    }

                                }
                            }

                            //untuk mencari ll
                            Vector vLLStockTaken = PstLlStockTaken.getLlTaken(leaveApplication.getOID(), leaveApplication.getEmployeeId());
                            Hashtable hashPeriodd = new Hashtable();
                            Date takenDatee = new Date();
                            Date takenDateFinishh = new Date();
                            Hashtable leaveOidd = new Hashtable();
                            if (vLLStockTaken != null && vLLStockTaken.size() > 0) {
                                boolean takenAwal = true;
                                Vector listPeriodDate = new Vector();
                                Hashtable bulan = new Hashtable();
                                Period periodc = new Period();
                                long oidPeriod = 0;
                                Vector vBulan = new Vector();
                                for (int x = 0; x < vLLStockTaken.size(); x++) {
                                    LlStockTaken llStockTaken = (LlStockTaken) vLLStockTaken.get(x);
                                    LlStockTaken dev = (LlStockTaken) vLLStockTaken.get(x);
                                    int c = 0;
                                    if (vLLStockTaken.size() > 1) {
                                        for (int i = 0; i < vLLStockTaken.size(); i++) {
                                            LlStockTaken objLlStockTaken = (LlStockTaken) vLLStockTaken.get(i);
                                            takenDatee = objLlStockTaken.getTakenDate();
                                            takenDateFinishh = objLlStockTaken.getTakenFinnishDate();
                                            if (c == 1) {
                                                vBulan = PstPeriod.getListStartEndDatePeriod(takenDatee, takenDateFinishh);
                                                periodc = (Period) vBulan.get(0);
                                                oidPeriod = periodc.getOID();
                                                if (bulan.containsKey(oidPeriod) == false) {
                                                    vBulan = PstPeriod.getListStartEndDatePeriod(takenDatee, takenDateFinishh);
                                                    periodc = (Period) vBulan.get(0);
                                                    listPeriodDate.add(periodc);
                                                }
                                            }
                                            if (takenDate != null && takenDateFinish != null && c == 0) {
                                                listPeriodDate = PstPeriod.getListStartEndDatePeriod(takenDatee, takenDateFinishh);
                                                c = 1;
                                                periodc = (Period) listPeriodDate.get(0);
                                                bulan.put(periodc.getOID(), true);
                                            }

                                        }

                                    } else {
                                        listPeriodDate = PstPeriod.getListStartEndDatePeriod(llStockTaken.getTakenDate(), llStockTaken.getTakenFinnishDate());
                                    }
                                    //artinya jika periode yg di temukan lebih dari 1 maka artinya dia beda period
                                    Period periodd = (Period) listPeriodDate.get(0);
                                    long periodz = periodd.getOID();

                                    // boolean cekAwalPayPeriod=true;
                                    Hashtable hashSamePeriod = new Hashtable();
                                    long periodId = 0;
                                     //Vector listPeriodDate = PstPeriod.getListStartEndDatePeriod(llStockTaken.getTakenDate(), llStockTaken.getTakenFinnishDate()); 
                                    //artinya jika periode yg di temukan lebih dari 1 maka artinya dia beda period
                                    Date dtTaken = llStockTaken.getTakenDate();
                                    Date dtTakenFinish = llStockTaken.getTakenFinnishDate();
                                    Date cekz = (Date) dev.getTakenDate();
                                    boolean cekAwalPayPeriod = true;
                                    long updateLeave = 0;
                                    if (listPeriodDate != null && listPeriodDate.size() > 1) {
                                        Date dtTakenTemp = (Date) dtTaken.clone();

                                        Date dtTakenFinishTemp = (Date) dtTakenFinish.clone();

                                        long diffStartToFinish = dtTakenFinishTemp.getTime() - dtTakenTemp.getTime();
                                        int itDate = Integer.parseInt(String.valueOf(diffStartToFinish / 86400000)) + 1;
                                        for (int idxdt = 0; idxdt < itDate; idxdt++) {

                                            if (itDate == 0) {
                                                dtTaken = (Date) dtTaken.clone();
                                                dtTakenFinish = (Date) dtTakenFinish.clone();

                                            } else if (idxdt == 0 && itDate > 0) {

                                                dtTaken = (Date) dtTaken.clone();

                                                dtTakenFinish = (Date) dtTaken.clone();
                                                periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                /**
                                                 * ScheduleSymbol scheduleSymbol
                                                 * =
                                                 * PstEmpSchedule.getEmpScheduleDateTime(periodId,
                                                 * llStockTaken.getEmployeeId(),
                                                 * dtTakenFinish, 0); int
                                                 * dtIdxJam=17; int dtIdxMin=0;
                                                 * int dtIdxInJam=17; int
                                                 * dtIdxInMin=0;
                                                 * if(scheduleSymbol!=null &&
                                                 * scheduleSymbol.getTimeOut()!=null){
                                                 * dtIdxJam =
                                                 * scheduleSymbol.getTimeOut().getHours();
                                                 * dtIdxMin =
                                                 * scheduleSymbol.getTimeOut().getMinutes();
                                                 * dtIdxInJam =
                                                 * scheduleSymbol.getTimeIn().getHours();
                                                 * dtIdxInMin =
                                                 * scheduleSymbol.getTimeIn().getMinutes();
                                                 * }
                                                 * dtTaken.setHours(dtIdxInJam);
                                                 * dtTaken.setMinutes(dtIdxInMin);
                                                 * dtTaken.setSeconds(0);
                                                 * dtTakenFinish.setHours(dtIdxJam);
                                                 * dtTakenFinish.setMinutes(dtIdxMin);
                                               dtTakenFinish.setSeconds(0);
                                                 */
                                                updateLeave = periodId;
                                            } else if (itDate == idxdt && itDate > 0) {
                                                dtTaken = (Date) dtTakenFinish.clone();
                                                periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                /**
                                                 * ScheduleSymbol scheduleSymbol
                                                 * =
                                                 * PstEmpSchedule.getEmpScheduleDateTime(periodId,
                                                 * llStockTaken.getEmployeeId(),
                                                 * dtTakenFinish, 0); int
                                                 * dtIdxJam=17; int dtIdxMin=0;
                                                 * if(scheduleSymbol!=null &&
                                                 * scheduleSymbol.getTimeIn()!=null){
                                                 * dtIdxJam =
                                                 * scheduleSymbol.getTimeIn().getHours();
                                                 * dtIdxMin =
                                                 * scheduleSymbol.getTimeIn().getMinutes();
                                                 * } dtTaken.setHours(dtIdxJam);
                                                 * dtTaken.setMinutes(dtIdxMin);
                                               dtTaken.setSeconds(0);
                                                 */

                                                dtTakenFinish = (Date) dtTakenFinish.clone();

                                            } else {

                                                if (leaveApplication.getDepHeadApproval() == 0) {
                                                    dtTaken = (Date) dtTaken.clone();

                                                    dtTakenFinish = (Date) new Date(dtTaken.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                } else {
                                                    Date dtTime = (Date) cekz.clone();
                                                    dtTaken = (Date) dtTaken.clone();

                                                    dtTakenFinish = (Date) new Date(dtTime.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));

                                                    if (hashPeriod.containsKey(periodId) == true) {
                                                        Date dtTimex = (Date) cekz.clone();
                                                        dtTaken = (Date) dtTaken.clone();

                                                        dtTakenFinish = (Date) new Date(dtTimex.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                    } else {

                                                        dtTaken = (Date) new Date(cekz.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();

                                                        dtTakenFinish = (Date) dtTaken.clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTaken, "yyyy-MM-dd"));
                                                    }

                                                }

                                                /*ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, llStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                 int dtIdxJamIn=8;
                                                 int dtIdxMinIn=0;
                                                 int dtIdxJamOut=17;
                                                 int dtIdxMinOut=0;
                                                 if(scheduleSymbol!=null && scheduleSymbol.getTimeIn()!=null){
                                                 dtIdxJamIn =  scheduleSymbol.getTimeIn().getHours();
                                                 dtIdxMinIn = scheduleSymbol.getTimeIn().getMinutes();
                                                 dtIdxJamOut =  scheduleSymbol.getTimeOut().getHours();
                                                 dtIdxMinOut = scheduleSymbol.getTimeOut().getMinutes();
                                                 }
                                                 dtTaken.setHours(dtIdxJamIn);
                                                 dtTaken.setMinutes(dtIdxMinIn);
                                                 dtTaken.setSeconds(0);
                                               
                                                 dtTakenFinish.setHours(dtIdxJamOut);
                                                 dtTakenFinish.setMinutes(dtIdxMinOut);
                                                 dtTakenFinish.setSeconds(0);  */
                                            }
                                            llStockTaken.setTakenDate(dtTaken);
                                            llStockTaken.setTakenFinnishDate(dtTakenFinish);
                                            /*for(int idxPer=0; idxPer<listPeriodDate.size();idxPer++){
                                             Period period = (Period)listPeriodDate.get(idxPer); 
                                             hashSamePeriod.put(period.getOID(),periodId);
                                             }*/
                                            periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                            long cek = 0;
                                            if (oidLeaveApplication != 0) {
                                                cek = PstLeaveApplication.cariDiferent(oidLeaveApplication);
                                            }

                                            if (leaveApplication.getDepHeadApproval() == 0 && leaveApplication.getLeaveAppDiffPeriod() > 0) {
                                                for (int i = 0; i < vLLStockTaken.size(); i++) {
                                                    Date takenDates = new Date();
                                                    Date takenDateFinishs = new Date();
                                                    LlStockTaken objLlStockTaken = (LlStockTaken) vLLStockTaken.get(i);
                                                    takenDate = objLlStockTaken.getTakenDate();
                                                    takenDateFinish = objLlStockTaken.getTakenFinnishDate();
                                                    if (listPeriodDate.size() > 1) {
                                                        leaveApplication.setLeaveAppDiffPeriod(0);
                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    }

                                                }
                                            }
                                            // long samePeriod=hashSamePeriod==null || hashSamePeriod.size()==0?0:(Long)hashSamePeriod.get(periodId);
                                            if (leaveApplication.getDepHeadApproval() == 0 || cek > 0 || hashPeriod.containsKey(periodId) == true) {
                                                try {
                                                    float qty = 0;
                                                    qty = DateCalc.workDayDifference(llStockTaken.getTakenDate(), llStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                    llStockTaken.setTakenQty(qty);
                                                    PstLlStockTaken.updateExc(llStockTaken);
                                                    leaveApplication.getEmployeeId();
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    takenAwal = false;
                                                    hashPeriod.put(periodId, true);
                                                    leaveOid.put("" + periodId, oid);
                                                } catch (Exception exc) {
                                                    System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                }
                                            } else if (listPeriodDate.size() > 1 && updateLeave == periodId && leaveApplication.getDepHeadApproval() > 0) {
                                                try {
                                                    float qty = 0;
                                                    qty = DateCalc.workDayDifference(llStockTaken.getTakenDate(), llStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                    llStockTaken.setTakenQty(qty);
                                                    long oidsuccess = PstLlStockTaken.updateExc(llStockTaken);

                                                    leaveApplication.getEmployeeId();

                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    takenAwal = false;
                                                    hashPeriod.put(periodId, true);
                                                    leaveOid.put("" + periodId, oid);
                                                } catch (Exception exc) {
                                                    System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                }

                                            } else if (leaveApplication.getDepHeadApproval() > 0 || leaveApplication.getHrManApproval() > 0) {
                                                if (leaveApplication.getDepHeadApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                    try {
                                                        LeaveApplication obLeaveApplication = (LeaveApplication) leaveApplication;
                                                        obLeaveApplication.setLeaveAppDiffPeriod(leaveApplication.getOID());
                                                        obLeaveApplication.setOID(0);

                                                        long oidLeave = PstLeaveApplication.insertExc(obLeaveApplication);
                                                        leaveOid.put("" + periodId, oidLeave);

                                                    } catch (Exception exc) {
                                                        System.out.println("Error insert taken ddi ctrlStockTaken" + exc);
                                                    }
                                                }
                                                if (leaveOid.containsKey("" + periodId) && leaveOid.get("" + periodId) != null) {
                                                    long oidx = (Long) leaveOid.get("" + periodId);
                                                    if (oidx != 0) {
                                                        float qty = 0;
                                                        qty = DateCalc.workDayDifference(llStockTaken.getTakenDate(), llStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                        llStockTaken.setTakenQty(qty);
                                                        llStockTaken.setLeaveApplicationId(oidx);
                                                        PstLlStockTaken.insertExc(llStockTaken);
                                                        hashPeriod.put(periodId, true);
                                                    }
                                                } else {
                                                    hashPeriod.put(periodz, true);
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                }
                                                if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                    try {
                                                        PstLlStockTaken.updateExc(llStockTaken);

                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                        takenAwal = false;
                                                        hashPeriod.put(periodz, true);
                                                    } catch (Exception exc) {
                                                        System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                    }
                                                } else if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == true) {
                                                    hashPeriod.put(periodz, true);
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                }
                                            }
                                           //hashSamePeriod.put(periodId,periodId);

                                        }
                                         //end period

                                    } else {
                                        hashPeriod.put(periodId, true);
                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                    }
                                }
                            }
                            //untuk mencari Special Leave
                            Vector vSpStockTaken = PstSpecialUnpaidLeaveTaken.getSpTaken(leaveApplication.getOID(), leaveApplication.getEmployeeId());
                            Hashtable hashPeriode = new Hashtable();
                            Date takenDateee = new Date();
                            Date takenDateFinishhh = new Date();
                            Hashtable leaveOide = new Hashtable();
                            if (vSpStockTaken != null && vSpStockTaken.size() > 0) {
                                boolean takenAwal = true;
                                Vector listPeriodDate = new Vector();
                                Hashtable bulan = new Hashtable();
                                Period periodc = new Period();
                                long oidPeriod = 0;
                                Vector vBulan = new Vector();
                                for (int x = 0; x < vSpStockTaken.size(); x++) {
                                    SpecialUnpaidLeaveTaken spStockTaken = (SpecialUnpaidLeaveTaken) vSpStockTaken.get(x);
                                    SpecialUnpaidLeaveTaken dev = (SpecialUnpaidLeaveTaken) vSpStockTaken.get(x);
                                    int c = 0;
                                    if (vSpStockTaken.size() > 1) {
                                        for (int i = 0; i < vSpStockTaken.size(); i++) {
                                            SpecialUnpaidLeaveTaken objSpStockTaken = (SpecialUnpaidLeaveTaken) vSpStockTaken.get(i);
                                            takenDateee = objSpStockTaken.getTakenDate();
                                            takenDateFinishhh = objSpStockTaken.getTakenFinnishDate();
                                            if (c == 1) {
                                                vBulan = PstPeriod.getListStartEndDatePeriod(takenDateee, takenDateFinishhh);
                                                periodc = (Period) vBulan.get(0);
                                                oidPeriod = periodc.getOID();
                                                if (bulan.containsKey(oidPeriod) == false) {
                                                    vBulan = PstPeriod.getListStartEndDatePeriod(takenDateee, takenDateFinishhh);
                                                    periodc = (Period) vBulan.get(0);
                                                    listPeriodDate.add(periodc);
                                                }
                                            }
                                            if (takenDateee != null && takenDateFinishhh != null && c == 0) {
                                                listPeriodDate = PstPeriod.getListStartEndDatePeriod(takenDateee, takenDateFinishhh);
                                                c = 1;
                                                periodc = (Period) listPeriodDate.get(0);
                                                bulan.put(periodc.getOID(), true);
                                            }

                                        }

                                    } else {
                                        listPeriodDate = PstPeriod.getListStartEndDatePeriod(spStockTaken.getTakenDate(), spStockTaken.getTakenFinnishDate());
                                    }
                                    //artinya jika periode yg di temukan lebih dari 1 maka artinya dia beda period
                                    Period periodd = (Period) listPeriodDate.get(0);
                                    long periodz = periodd.getOID();

                                    // boolean cekAwalPayPeriod=true;
                                    Hashtable hashSamePeriod = new Hashtable();
                                    long periodId = 0;
                                     //Vector listPeriodDate = PstPeriod.getListStartEndDatePeriod(llStockTaken.getTakenDate(), llStockTaken.getTakenFinnishDate()); 
                                    //artinya jika periode yg di temukan lebih dari 1 maka artinya dia beda period
                                    Date dtTaken = spStockTaken.getTakenDate();
                                    Date dtTakenFinish = spStockTaken.getTakenFinnishDate();
                                    Date cekz = (Date) dev.getTakenDate();
                                    boolean cekAwalPayPeriod = true;
                                    long updateLeave = 0;
                                    if (listPeriodDate != null && listPeriodDate.size() > 1) {
                                        Date dtTakenTemp = (Date) dtTaken.clone();

                                        Date dtTakenFinishTemp = (Date) dtTakenFinish.clone();

                                        long diffStartToFinish = dtTakenFinishTemp.getTime() - dtTakenTemp.getTime();
                                        int itDate = Integer.parseInt(String.valueOf(diffStartToFinish / 86400000)) + 1;
                                        for (int idxdt = 0; idxdt < itDate; idxdt++) {

                                            if (itDate == 0) {
                                                dtTaken = (Date) dtTaken.clone();
                                                dtTakenFinish = (Date) dtTakenFinish.clone();

                                            } else if (idxdt == 0 && itDate > 0) {

                                                dtTaken = (Date) dtTaken.clone();

                                                dtTakenFinish = (Date) dtTaken.clone();
                                                periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, spStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                int dtIdxJam = 17;
                                                int dtIdxMin = 0;
                                                int dtIdxInJam = 8;
                                                int dtIdxInMin = 0;
                                                if (scheduleSymbol != null && scheduleSymbol.getTimeOut() != null && scheduleSymbol.getTimeIn() != null) {
                                                    dtIdxJam = scheduleSymbol.getTimeOut().getHours();
                                                    dtIdxMin = scheduleSymbol.getTimeOut().getMinutes();
                                                    dtIdxInJam = scheduleSymbol.getTimeIn().getHours();
                                                    dtIdxInMin = scheduleSymbol.getTimeIn().getMinutes();
                                                }
                                                dtTaken.setHours(dtIdxInJam);
                                                dtTaken.setMinutes(dtIdxInMin);
                                                dtTaken.setSeconds(0);
                                                dtTakenFinish.setHours(dtIdxJam);
                                                dtTakenFinish.setMinutes(dtIdxMin);
                                                dtTakenFinish.setSeconds(0);
                                                updateLeave = periodId;
                                            } else if (itDate == idxdt && itDate > 0) {
                                                dtTaken = (Date) dtTakenFinish.clone();
                                                periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, spStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                int dtIdxJam = 17;
                                                int dtIdxMin = 0;
                                                if (scheduleSymbol != null && scheduleSymbol.getTimeIn() != null) {
                                                    dtIdxJam = scheduleSymbol.getTimeIn().getHours();
                                                    dtIdxMin = scheduleSymbol.getTimeIn().getMinutes();
                                                }
                                                dtTaken.setHours(dtIdxJam);
                                                dtTaken.setMinutes(dtIdxMin);
                                                dtTaken.setSeconds(0);

                                                dtTakenFinish = (Date) dtTakenFinish.clone();

                                            } else {

                                                if (leaveApplication.getDepHeadApproval() == 0) {
                                                    dtTaken = (Date) dtTaken.clone();

                                                    dtTakenFinish = (Date) new Date(dtTaken.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                } else {
                                                    Date dtTime = (Date) cekz.clone();
                                                    dtTaken = (Date) dtTaken.clone();

                                                    dtTakenFinish = (Date) new Date(dtTime.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                    periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));

                                                    if (hashPeriod.containsKey(periodId) == true) {
                                                        Date dtTimex = (Date) cekz.clone();
                                                        dtTaken = (Date) dtTaken.clone();

                                                        dtTakenFinish = (Date) new Date(dtTimex.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                                    } else {

                                                        dtTaken = (Date) new Date(cekz.getTime() + idxdt * 1000L * 60 * 60 * 24).clone();

                                                        dtTakenFinish = (Date) dtTaken.clone();
                                                        periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTaken, "yyyy-MM-dd"));
                                                    }

                                                }

                                                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, spStockTaken.getEmployeeId(), dtTakenFinish, 0);
                                                int dtIdxJamIn = 8;
                                                int dtIdxMinIn = 0;
                                                int dtIdxJamOut = 17;
                                                int dtIdxMinOut = 0;
                                                if (scheduleSymbol != null && scheduleSymbol.getTimeIn() != null && scheduleSymbol.getTimeOut() != null) {
                                                    dtIdxJamIn = scheduleSymbol.getTimeIn().getHours();
                                                    dtIdxMinIn = scheduleSymbol.getTimeIn().getMinutes();
                                                    dtIdxJamOut = scheduleSymbol.getTimeOut().getHours();
                                                    dtIdxMinOut = scheduleSymbol.getTimeOut().getMinutes();
                                                }
                                                dtTaken.setHours(dtIdxJamIn);
                                                dtTaken.setMinutes(dtIdxMinIn);
                                                dtTaken.setSeconds(0);

                                                dtTakenFinish.setHours(dtIdxJamOut);
                                                dtTakenFinish.setMinutes(dtIdxMinOut);
                                                dtTakenFinish.setSeconds(0);
                                            }
                                            spStockTaken.setTakenDate(dtTaken);
                                            spStockTaken.setTakenFinnishDate(dtTakenFinish);
                                            /*for(int idxPer=0; idxPer<listPeriodDate.size();idxPer++){
                                             Period period = (Period)listPeriodDate.get(idxPer); 
                                             hashSamePeriod.put(period.getOID(),periodId);
                                             }*/
                                            periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(dtTakenFinish, "yyyy-MM-dd"));
                                            long cek = 0;
                                            if (oidLeaveApplication != 0) {
                                                cek = PstLeaveApplication.cariDiferent(oidLeaveApplication);
                                            }

                                            if (leaveApplication.getDepHeadApproval() == 0 && leaveApplication.getLeaveAppDiffPeriod() > 0) {
                                                for (int i = 0; i < vSpStockTaken.size(); i++) {
                                                    Date takenDates = new Date();
                                                    Date takenDateFinishs = new Date();
                                                    SpecialUnpaidLeaveTaken objSpStockTaken = (SpecialUnpaidLeaveTaken) vSpStockTaken.get(i);
                                                    takenDate = objSpStockTaken.getTakenDate();
                                                    takenDateFinish = objSpStockTaken.getTakenFinnishDate();
                                                    if (listPeriodDate.size() > 1) {
                                                        leaveApplication.setLeaveAppDiffPeriod(0);
                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    }

                                                }
                                            }
                                            // long samePeriod=hashSamePeriod==null || hashSamePeriod.size()==0?0:(Long)hashSamePeriod.get(periodId);
                                            if (leaveApplication.getDepHeadApproval() == 0 || cek > 0 || hashPeriod.containsKey(periodId) == true) {
                                                try {
                                                    float qty = 0;
                                                    qty = DateCalc.workDayDifference(spStockTaken.getTakenDate(), spStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                    spStockTaken.setTakenQty(qty);
                                                    long oidsuccess = PstSpecialUnpaidLeaveTaken.updateExc(spStockTaken);
                                                    PstSpecialUnpaidLeaveTaken.updateExc(spStockTaken);
                                                    leaveApplication.getEmployeeId();
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    takenAwal = false;
                                                    hashPeriod.put(periodId, true);
                                                    leaveOid.put("" + periodId, oid);
                                                } catch (Exception exc) {
                                                    System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                }
                                            } else if (listPeriodDate.size() > 1 && updateLeave == periodId && leaveApplication.getDepHeadApproval() > 0) {
                                                try {
                                                    float qty = 0;
                                                    qty = DateCalc.workDayDifference(spStockTaken.getTakenDate(), spStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                    spStockTaken.setTakenQty(qty);
                                                    long oidsuccess = PstSpecialUnpaidLeaveTaken.updateExc(spStockTaken);

                                                    leaveApplication.getEmployeeId();

                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    if (oid != 0) {
                                                        msgString = resultText[language][RSLT_OK];
                                                    }
                                                    takenAwal = false;
                                                    hashPeriod.put(periodId, true);
                                                    leaveOid.put("" + periodId, oid);
                                                } catch (Exception exc) {
                                                    System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                }

                                            } else if (leaveApplication.getDepHeadApproval() > 0 || leaveApplication.getHrManApproval() > 0) {
                                                if (leaveApplication.getDepHeadApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                    try {
                                                        LeaveApplication obLeaveApplication = (LeaveApplication) leaveApplication;
                                                        obLeaveApplication.setLeaveAppDiffPeriod(leaveApplication.getOID());
                                                        obLeaveApplication.setOID(0);

                                                        long oidLeave = PstLeaveApplication.insertExc(obLeaveApplication);
                                                        leaveOid.put("" + periodId, oidLeave);

                                                    } catch (Exception exc) {
                                                        System.out.println("Error insert taken ddi ctrlStockTaken" + exc);
                                                    }
                                                }
                                                if (leaveOid.containsKey("" + periodId) && leaveOid.get("" + periodId) != null) {
                                                    float qty = 0;
                                                    qty = DateCalc.workDayDifference(spStockTaken.getTakenDate(), spStockTaken.getTakenFinnishDate(), leaveConfig.getHourOneWorkday());
                                                    spStockTaken.setTakenQty(qty);
                                                    long oidx = (Long) leaveOid.get("" + periodId);
                                                    if (oidx != 0) {
                                                        spStockTaken.setLeaveApplicationId(oidx);
                                                        PstSpecialUnpaidLeaveTaken.insertExc(spStockTaken);
                                                        hashPeriod.put(periodId, true);
                                                    }
                                                } else {
                                                    hashPeriod.put(periodz, true);
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                }
                                                if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == false) {
                                                    try {
                                                        PstSpecialUnpaidLeaveTaken.updateExc(spStockTaken);

                                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                        if (oid != 0) {
                                                            msgString = resultText[language][RSLT_OK];
                                                        }
                                                        takenAwal = false;
                                                        hashPeriod.put(periodz, true);
                                                    } catch (Exception exc) {
                                                        System.out.println("Error update taken ddi ctrlStockTaken" + exc);
                                                    }
                                                } else if (leaveApplication.getHrManApproval() > 0 && hashPeriod.containsKey(periodId) == true) {
                                                    hashPeriod.put(periodz, true);
                                                    oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                                    if (oid != 0) {
                                                        msgString = resultText[language][RSLT_OK];
                                                    }
                                                }
                                            }
                                           //hashSamePeriod.put(periodId,periodId);

                                        }
                                         //end period

                                    } else {
                                        hashPeriod.put(periodId, true);
                                        oid = pstLeaveApplication.updateExc(this.leaveApplication);
                                        if (oid != 0) {
                                            msgString = resultText[language][RSLT_OK];
                                        }
                                    }
                                }
                            }
                        } else {
                            oid = pstLeaveApplication.updateExc(this.leaveApplication);
                            if (oid != 0) {
                                msgString = resultText[language][RSLT_OK];
                            }
                        }
                        // process leave application detail
                        if (vectOfLeaveAppDetail != null && vectOfLeaveAppDetail.size() > 0) {
                            int intDelResult = PstLeaveApplicationDetail.deleteByApplicationMain(oid);

                            int leaveAppCount = vectOfLeaveAppDetail.size();
                            for (int ij = 0; ij < leaveAppCount; ij++) {
                                LeaveApplicationDetail objLeaveApplicationDetail = (LeaveApplicationDetail) vectOfLeaveAppDetail.get(ij);
                                objLeaveApplicationDetail.setLeaveMainOid(oid);
                                long oidLeaveDetail = PstLeaveApplicationDetail.insertExc(objLeaveApplicationDetail);
                            }
                        }

                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                //buatkan save carrer path
                if (sysLog != 0) { /* kondisi jika sysLog == 1, maka proses di bawah ini dijalankan*/

                    String className = leaveApplication.getClass().getName();
                    LogSysHistory logSysHistory = new LogSysHistory();

                    String reqUrl = "leave_app_edit.jsp?FRM_FLD_LEAVE_APPLICATION_ID=" + leaveApplication.getOID();
                    /* Lakukan set data ke entity logSysHistory */
                    logSysHistory.setLogDocumentId(0);
                    logSysHistory.setLogUserId(userId);
                    logSysHistory.setApproverId(userId);
                    logSysHistory.setApproveDate(nowDate);
                    logSysHistory.setLogLoginName(appUser.getLoginId());
                    logSysHistory.setLogDocumentNumber("");
                    logSysHistory.setLogDocumentType(className); //entity
                    logSysHistory.setLogUserAction("ADD"); // command
                    logSysHistory.setLogOpenUrl(reqUrl); // locate jsp
                    logSysHistory.setLogUpdateDate(nowDate);
                    logSysHistory.setLogApplication(I_LogHistory.SYSTEM_NAME[I_LogHistory.SYSTEM_HAIRISMA]); // interface
                                    /* Inisialisasi logField dengan menggambil field EmpEducation */
                    /* Tips: ambil data field dari persistent */
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_TYPE_LEAVE] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_REASON_ID] + " ;";
                    logField += PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_DIFFERENT_PERIOD] + " ;";

                    /* data logField yg telah terisi kemudian digunakan untuk setLogDetail */
                    logSysHistory.setLogDetail(logField); // apa yang dirubah
                                    /* inisialisasi value, yaitu logCurr */
                    logCurr += "" + leaveApplication.getOID() + ";";
                    logCurr += "" + leaveApplication.getSubmissionDate() + ";";
                    logCurr += "" + leaveApplication.getEmployeeId() + ";";
                    logCurr += "" + leaveApplication.getLeaveReason() + ";";
                    logCurr += "" + leaveApplication.getDepHeadApproval() + ";";
                    logCurr += "" + leaveApplication.getHrManApproval() + ";";
                    logCurr += "" + leaveApplication.getDocStatus() + ";";
                    logCurr += "" + leaveApplication.getDepHeadApproveDate() + ";";
                    logCurr += "" + leaveApplication.getHrManApproveDate() + ";";
                    logCurr += "" + leaveApplication.getGmApproval() + ";";
                    logCurr += "" + leaveApplication.getGmApprovalDate() + ";";
                    logCurr += "" + leaveApplication.getTypeLeave() + ";";
                    logCurr += "" + leaveApplication.getTypeFormLeave() + ";";
                    logCurr += "" + leaveApplication.getReasonId() + ";";
                    logCurr += "" + leaveApplication.getLeaveAppDiffPeriod() + ";";

                    /* data logCurr yg telah diinisalisasi kemudian dipakai untuk set ke logPrev, dan logCurr */
                    /* data struktur perusahaan didapat dari pengguna yang login melalui AppUser */
                    logSysHistory.setCompanyId(emp.getCompanyId());
                    logSysHistory.setDivisionId(emp.getDivisionId());
                    logSysHistory.setDepartmentId(emp.getDepartmentId());
                    logSysHistory.setSectionId(emp.getSectionId());
                    /* mencatat item yang diedit */
                    logSysHistory.setLogEditedUserId(leaveApplication.getOID());
                    /* setelah di set maka lakukan proses insert ke table logSysHistory */
                    try {

                        PstLogSysHistory.insertExc(logSysHistory);
                    } catch (Exception e) {
                    }
                }

                sendEmail(this.leaveApplication, approot, oidEmployeeLogin);
                //sen

//                Vector listDivHead = new Vector();
//                if(session.getValue("listDivHead")!=null){
//                    rekapitulasiAbsensi = (RekapitulasiAbsensi)session.getValue("listDivHead"); 
//                }
                //Vector listEmp = 
                //email.sendEmail(VECTOR, null, null, "Payslip " + payPeriod.getPeriod() + " of " + employee.getFullName(), strEmail, attachment, attachmentName);
                break;

            case Command.EDIT:
                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);

                        // get data detail
                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.POST:
                if (oidLeaveApplication != 0) {
                    try {

                        boolean status_excecution = SessLeaveApplication.processExecute(oidLeaveApplication);
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);

                        //update by satrya 2014-03-20
                        if (leaveConfig != null && leaveConfig.getConfigurationLeaveApprovall() == I_Leave.LEAVE_CONFIG_AFTER_APPROVALL_HRD_YES_EXECUTE) {
                            LeaveApplication objApplication = new LeaveApplication();
                            frmLeaveApplication.requestEntityObjectVer2(objApplication);
                            leaveApplication.setHrManApproval(objApplication.getHrManApproval());
                            leaveApplication.setHrManApproveDate(objApplication.getHrManApproveDate());
                            pstLeaveApplication.updateExc(this.leaveApplication);
                            sendEmail(this.leaveApplication, approot, oidEmployeeLogin);
                        }
                        // get data detail
                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;
                     //update by satrya 2012-07-27
            //untuk execute Leave Form

                  //end
            case Command.ASK:
                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);

                        // get data detail
                        Vector listOfDetail = PstLeaveApplicationDetail.listDetailByApplicationMain(oidLeaveApplication);
                        leaveApplication.setListOfDetail(listOfDetail);
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                if (oidLeaveApplication != 0) {
                    try {
                        // delete leave item in detail
                        int delResult = PstLeaveApplicationDetail.deleteByApplicationMain(oidLeaveApplication);

                        long oid = PstLeaveApplication.deleteExc(oidLeaveApplication);
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

            case Command.UNLOCK:

                if (oidLeaveApplication != 0) {
                    try {
                        leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);
                        docPrev = leaveApplication.getDocStatus();
                    } catch (Exception exc) {
                        System.out.println("Exc when fetch LeaveApplication entity : " + exc.toString());
                    }
                }

                LeaveApplication leaveApplicationx = new LeaveApplication();
                frmLeaveApplication.requestEntityObjectOnlyReason(leaveApplicationx);

                leaveApplication.setLeaveReason(leaveApplicationx.getLeaveReason());
                try {
                    long oid = pstLeaveApplication.updateExc(this.leaveApplication);
                } catch (DBException dbexc) {
                    excCode = dbexc.getErrorCode();
                    msgString = getSystemMessage(excCode);
                    return getControlMsgId(excCode);
                } catch (Exception exc) {
                    msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                }

                break;

            default:

        }
        return rsCode;
    }

    public static void sendEmail(LeaveApplication leaveApp, String approot) {

        String emailOn = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_ON"); //0= off 1=on
        if (!(emailOn.contains("1"))) {
            return;
        }

        String harismaURL = PstSystemProperty.getValueByName("HARISMA_URL");

        if (harismaURL != null) {

            if (!harismaURL.endsWith("/login.jsp")) {
                harismaURL = harismaURL + "/login.jsp";
            }
        }
        //update by satrya 2013-11-04
        String harismaUrlInternet = PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) || PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET").length() == 0 ? null : PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET");
        if (harismaUrlInternet != null && harismaUrlInternet.length() > 0 && !harismaUrlInternet.equalsIgnoreCase("-")) {
            if (!harismaUrlInternet.endsWith("/login.jsp")) {
                harismaUrlInternet = harismaUrlInternet + "/login.jsp";
            }
        }
        String from = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_FROM"); //"support@dimata.com"
        String host = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_HOST"); //"beagle2.webappcabaret.net";
        int port = 25;

        try {
            port = Integer.parseInt(PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_PORT")); //25;
        } catch (Exception exc) {
        }

        //update by satrya 2013-11-14
        boolean configEmailWithImage = false;
        try {
            configEmailWithImage = Boolean.parseBoolean(PstSystemProperty.getValueByName("CONFIGURASI_EMAIL_LEAVE_WITH_PICTURE")); //25;
        } catch (Exception exc) {
            configEmailWithImage = false;
        }

        String username = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_USERNAME"); //"support@dimata.com";
        String password = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_PASSWORD"); //"doxxxxx";
        boolean SSL = false;
        try {
            String sSSL = PstSystemProperty.getValueByName("EMAIL_SSL_SETTING");
            SSL = Boolean.parseBoolean(sSSL);
        } catch (Exception exc) {
            SSL = false;
        }

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        //update by satrya 2013-11-14
        Vector listLeaveApplication = new Vector();
        if (configEmailWithImage) {
            listLeaveApplication = PstLeaveApplication.searchNameLeaveWithEmail(leaveApp);
        }

        /**
         * update by satrya 2013-10-30 Employee employee = null; Department
         * department = new Department();
         */
        String attacment = /*approot+"/"+*/ "imgcache/no_photo.JPEG";
        String imgPicture = "no_photo.JPEG";
        Employee employee = new Employee();
        PayGeneral company = null;
        Division division = null;
        Department department = null;
        Section section = null;
        Position position = null;
        String imageDataString = null;
        try {
            employee = PstEmployee.fetchExc(leaveApp.getEmployeeId());
            company = employee.getCompanyId() == 0 ? new PayGeneral() : PstPayGeneral.fetchExc(employee.getCompanyId());
            division = employee.getDivisionId() == 0 ? new Division() : PstDivision.fetchExc(employee.getDivisionId());
            department = employee.getDepartmentId() == 0 ? new Department() : PstDepartment.fetchExc(employee.getDepartmentId());
            section = employee.getSectionId() == 0 ? new Section() : PstSection.fetchExc(employee.getSectionId());
            position = employee.getPositionId() == 0 ? new Position() : PstPosition.fetchExc(employee.getPositionId());
            SessEmployeePicture sessEmployeePicture = new SessEmployeePicture();
            attacment = /*approot+"/"+*/ sessEmployeePicture.fetchImageEmployeeVer1(employee.getOID());
            if (attacment != null && attacment.length() > 0) {
                /* hidden by satrya 2014-01-06 karena sudah pakai cid BufferedImage img = ImageIO.read(new File(attacment));
                 File file = new File(attacment);
            
                 int totypessebelumTitik = file.getName().indexOf(".");
                 String typeFile = file.getName().substring(totypessebelumTitik,  file.getName().length());
                 if(typeFile!=null){
                 if(typeFile.equalsIgnoreCase(".JPG") || typeFile.equalsIgnoreCase(".JPEG")|| typeFile.equalsIgnoreCase(".GIF")|| typeFile.equalsIgnoreCase(".PNG")){
                
                 }else{
                 typeFile = "JPG";
                 }
                 }*/

                imageDataString = "cid:logoimg";//logoimg fungsinya nnti sebagai ID saya //"data:image/jpeg;base64,"+MailSender.encodeToString(img, typeFile);

            }
        } catch (Exception exc) {
        }

        Vector<String> recipientsCC = new Vector();
        Vector<String> recipientsBCC = new Vector();
        String subject = "Leave Form";

        String directToDocumentLocal = "<a href=\"" + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "\"> Local </a>";
        String directToDocumentInet = harismaUrlInternet == null || harismaUrlInternet.length() == 0 || harismaUrlInternet.equalsIgnoreCase("-") ? "" : "<a href=\"" + harismaUrlInternet + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "\"> Out of Local Area Network </a>"; /*harismaURL +"?page_name=leave_app_edit.jsp&page_command="+Command.EDIT+"&data_oid="+leaveApp.getOID() + "&oid_employee="+leaveApp.getEmployeeId()*/;
        //update by satrya 2013-10-30 
        /**
         * String txtMessage =( (leaveApp.getDocStatus() ==
         * PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) ? " Leave
         * application has been final approved. Date : "+
         * Formater.formatDate(leaveApp.getHrManApproveDate(), "dd MMMM yyyy") :
         * ( "New/Update of leave application\n Date:" +
         * Formater.formatDate(leaveApp.getSubmissionDate(), "dd MMMM yyyy")))
         * +"\n" + " Name : " + employee.getFullName() + " Department : "+
         * department.getDepartment() +"\n" + " please access : \n" + "> Direct
         * to document : "+harismaURL +
         * "?page_name=leave_app_edit.jsp&page_command="+Command.EDIT+"&data_oid="+leaveApp.getOID()
         * + "&oid_employee="+leaveApp.getEmployeeId()+"\n" + "> List of
         * documents : "+harismaURL +
         * "?page_name=home.jsp&page_command="+Command.ACTIVATE + "\n\n sent by
         * Dimata Harisma System";
         */
        //update by satrya 2013-11-14
        String txtMessage = "";
        if (configEmailWithImage) {
            String dtOfRequest = "";
            if (listLeaveApplication != null && listLeaveApplication.size() > 0) {
                SessLeaveApplicationEmail sessLeaveAppEmail = (SessLeaveApplicationEmail) listLeaveApplication.get(0);
                dtOfRequest = sessLeaveAppEmail.getDtOfRequest() == null ? "-" : Formater.formatDate(sessLeaveAppEmail.getDtOfRequest(), "dd MMM yyyy");
            }
            txtMessage = "<table width=\"500px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                    + "<tr>"
                    + "<td><table width=\"500px\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
                    + "<tr>"
                    + "<td colspan=\"5\"><table width=\"500px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
                    + "<tr>"
                    + "<td height=\"38px\" colspan=\"9\" valign=\"bottom\" style=\"font-size:18px\" align=\"center\">" + (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED ? "Leave application has been final approved" : "New/Update of leave application") + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td height=\"22px\" colspan=\"9\">&nbsp;</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td height=\"22px\" colspan=\"9\" align=\"right\"><table>"
                    + "<tr>"
                    + "<td>Date of Request</td>"
                    + "<td>:" + dtOfRequest + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    + "</tr>"
                    + "<tr>"
                    //<!--untuk tampilan user -->
                    + "<td><table width=\"500px\">"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"124px\" rowspan=\"6\"><img width=\"124px\" height=\"124px\" src=\"" + (imageDataString != null ? imageDataString : "no.jpg") + "\" nowrap=\"nowrap\" /></td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Name</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + employee.getFullName() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Company</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + company.getCompanyName() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Division</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + division.getDivision() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Department</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + department.getDepartment() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Section</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + (section.getSection() == null || section.getSection().length() == 0 ? "-" : section.getSection()) + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Position</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + (position.getPosition()) + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"124px\">&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Reason</td>"
                    + "<td style=\"font-size:14px\" align=\"left\">:</td>"
                    // +"<td colspan=\"4px\" align=\"left\"><p>"+leaveApp.getLeaveReason()+"</p></td>"
                    + "<td colspan=\"4px\" align=\"left\">"
                    + "<table width=\"250px\">"
                    + "<tr>"
                    + "<td width=\"250px\">"
                    + "<p>" + leaveApp.getLeaveReason() + "</p>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- untuk tampilan user -->
                    + "</tr>";
            txtMessage = txtMessage
                    + "<tr>"
                    //<!-- untuk detail Leave -->
                    + "<td><table width=\"100%\" style=\"border:0.5px;border-style:solid\">"
                    + "<tr>"
                    + "<td colspan=\"2\" align=\"left\"  style=\"font-size:14px;border:0.5px;border-style:solid\">Type Leave</td>"
                    + "<td align=\"left\" style=\"font-size:14px;border:0.5px;border-style:solid\">Date From </td>"
                    + "<td style=\"border:0.5px;border-style:solid\" width=\"178\">Date To </td>"
                    + "<td style=\"border:0.5px;border-style:solid\" width=\"49\">Total</td>"
                    + "</tr>";
            txtMessage = txtMessage;
            String loopDataCuti = "";
            String depHeadFullNameApprovall = "";
            String HrFullNameApprovall = "";
            Hashtable hashSymbolCuty = new Hashtable();
            if (listLeaveApplication != null && listLeaveApplication.size() > 0) {
                boolean leaveConfigCalculationCategoryOff = false;
                try {
                    leaveConfigCalculationCategoryOff = Boolean.parseBoolean(PstSystemProperty.getValueByName("LEAVE_CONFIG_CALCULATE_CATEGORY_SCHEDULE_OFF"));
                } catch (Exception exc) {
                    System.out.println("Exc FrmAlStockTaken" + exc);
                }
                for (int x = 0; x < listLeaveApplication.size(); x++) {
                    SessLeaveApplicationEmail sessLeaveApplicationEmail = (SessLeaveApplicationEmail) listLeaveApplication.get(x);
                    float alQty = sessLeaveApplicationEmail.getTakenDateStart() == null || sessLeaveApplicationEmail.getTakenFinish() == null ? 0 : DateCalc.workDayDifference(sessLeaveApplicationEmail.getTakenDateStart(), sessLeaveApplicationEmail.getTakenFinish(), leaveConfig.getHourOneWorkday());
                    float intersec = sessLeaveApplicationEmail.getTakenDateStart() == null || sessLeaveApplicationEmail.getTakenFinish() == null ? 0 : PstEmpSchedule.breakTimeIntersection(leaveApp.getEmployeeId(), sessLeaveApplicationEmail.getTakenDateStart(), sessLeaveApplicationEmail.getTakenFinish()) / (leaveConfig.getHourOneWorkday() * 60f * 60f * 1000f);
                //update by satrya 2014-01-17

                    if (leaveConfigCalculationCategoryOff) {
                        int diffDay = (int) alQty;//di cari bentuk int'nya
                        //
                        //update by satrya 2013-12-11
                        //untuk mencari hari libur
                        Vector vctSchIDOff = PstScheduleSymbol.getScheduleId(PstScheduleCategory.CATEGORY_OFF);
                        float dayOffSchedule = 0;
                        if (sessLeaveApplicationEmail.getTakenDateStart() != null) {
                            for (int ix = 0; ix < diffDay; ix++) {
                                Date selectedDate = new Date(sessLeaveApplicationEmail.getTakenDateStart().getYear(), sessLeaveApplicationEmail.getTakenDateStart().getMonth(), (sessLeaveApplicationEmail.getTakenDateStart().getDate() + ix));
                                Period period = PstPeriod.getPeriodBySelectedDate(selectedDate);
                                dayOffSchedule = dayOffSchedule + PstEmpSchedule.getStatusDayOff(sessLeaveApplicationEmail.getEmployeeId(), period.getOID(), 0, vctSchIDOff, selectedDate);
                            }
                        }
                        alQty = alQty - dayOffSchedule;
                    }
                    String hours = Formater.formatWorkDayHoursMinutes(alQty - intersec, leaveConfig.getHourOneWorkday(), leaveConfig.getFormatLeave());
                    depHeadFullNameApprovall = sessLeaveApplicationEmail.getFullNameDepHead();
                    HrFullNameApprovall = sessLeaveApplicationEmail.getFullNameHr();

                    String symbolCuty = (String) (hashSymbolCuty.get(sessLeaveApplicationEmail.getSymbole()) == null ? sessLeaveApplicationEmail.getSymbole() : "&nbsp;");
                    hashSymbolCuty.put(sessLeaveApplicationEmail.getSymbole(), sessLeaveApplicationEmail.getSymbole());
                    loopDataCuti = loopDataCuti + "<tr>"
                            + "<td align=\"left\"  style=\"font-size:14px;border:0.5px;border-style:solid\" colspan=\"2\">" + symbolCuty + "</td>"
                            // update by satrya 2014-01-15 +"<td  style=\"font-size:14px;border:0.5px;border-style:solid\" align=\"left\">:</td>"
                            + "<td align=\"left\" style=\"font-size:14px;border:0.5px;border-style:solid\">" + (sessLeaveApplicationEmail.getTakenDateStart() == null ? "-" : Formater.formatDate(sessLeaveApplicationEmail.getTakenDateStart(), "dd/MM/yyyy HH:mm ")) + "</td>"
                            + "<td style=\"border:0.5px;border-style:solid\" width=\"178\">" + (sessLeaveApplicationEmail.getTakenFinish() == null ? "-" : Formater.formatDate(sessLeaveApplicationEmail.getTakenFinish(), "dd/MM/yyyy HH:mm ")) + " </td>"
                            + "<td style=\"border:0.5px;border-style:solid\" width=\"49\">" + hours + "</td>"
                            + "</tr>";
                }
            }

            txtMessage = txtMessage + loopDataCuti + "</table></td></tr>";/*<!-- end untuk detail Leave -->*/

            ///loop dsini
            txtMessage = txtMessage
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk approvall -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">Approved by,</td>"
                    + "<td width=\"50%\" align=\"center\">HRD Approved by,</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk approvall -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk tgl yg approved -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">" + (leaveApp.getDepHeadApproveDate() == null ? "" : Formater.formatDate(leaveApp.getDepHeadApproveDate(), "dd/MM/yyyy")) + "</td>"
                    + "<td width=\"50%\" align=\"center\">" + (leaveApp.getHrManApproveDate() == null ? "" : Formater.formatDate(leaveApp.getHrManApproveDate(), "dd/MM/yyyy")) + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk tgl yg approved -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk nama yg approve -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">" + (depHeadFullNameApprovall == null ? "-" : depHeadFullNameApprovall) + "</td>"
                    + "<td width=\"50%\" align=\"center\">" + (HrFullNameApprovall == null ? "-" : HrFullNameApprovall) + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end nama yg approve -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td align=\"left\">Please click here to approve : </td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk link -->
                    + "<td><table width=\"200px\">"
                    + "<tr>"
                    + "<td width=\"200px\" align=\"left\"><p>" + directToDocumentLocal + "</p></td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>";
            if (directToDocumentInet != null && directToDocumentInet.length() > 0) {
                txtMessage = txtMessage + "<tr>"
                        //<!-- untuk spasi -->
                        + "<td><table width=\"100%\">"
                        + "<tr>"
                        + "<td align=\"left\">If you are out of Local Area Network, please click this link to approve :</td>"
                        + "</tr>"
                        + "</table></td>"
                        //<!-- end untuk spasi -->
                        + "</tr>"
                        + "<tr>"
                        //<!-- untuk link -->
                        + "<td><table width=\"200px\">"
                        + "<tr>"
                        + "<td width=\"200px\" align=\"left\"><p>" + directToDocumentInet + "</p></td>"
                        + "</tr>"
                        + "</table></td>"
                        //<!-- end untuk spasi -->
                        + "</tr>";
            }

            txtMessage = txtMessage + "</table></td>"
                    + "</tr>"
                    + "</table></td>"
                    + "</tr>"
                    + "</table>";
        } else {
            txtMessage = ((leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED)
                    ? " Leave application has been final approved. Date : " + Formater.formatDate(leaveApp.getHrManApproveDate(), "dd MMMM yyyy")
                    : ("New/Update of leave application\n Date:" + Formater.formatDate(leaveApp.getSubmissionDate(), "dd MMMM yyyy"))) + "\n"
                    + " Name : " + employee.getFullName() + " Department : " + department.getDepartment() + "\n"
                    + " please access : \n"
                    + "> Direct to document : " + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID()
                    + "&oid_employee=" + leaveApp.getEmployeeId() + "\n"
                    + "> List of documents  : " + harismaURL + "?page_name=home.jsp&page_command=" + Command.ACTIVATE
                    + "\n\n sent by Dimata Harisma System";
        }
        ///di hidden sementara karena ibu ayu belom mau merubah cuti

        //System.out.println("Check if email will be sent : "+txtMessage);
        Vector listRec = new Vector();

        //if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
        if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
            listRec = getListNextApproval(leaveApp, employee);

        } else {
            if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {
                listRec = getFinalBackApproval(leaveApp, employee);
            }
        }

        if (listRec == null || listRec.size() < 1) {
            System.out.println("No recipient defined ");
            return;
        }

        Vector<String> recx = new Vector();
        try {
            for (int i = 0; i < listRec.size(); i++) {
                //update by satrya karena hanya tiga saja yg d pakai 2014-01-15 for (int i = 0; i < listRec.size() && i < 3; i++) {
                Employee req = (Employee) listRec.get(i);
                if (req.getEmailAddress() != null && req.getEmailAddress().length() > 0 && !req.getEmailAddress().equalsIgnoreCase("-")) {
                    recx.add(req.getEmailAddress());
                }
            }

        } catch (Exception exc) {
            System.out.println(exc);
        }

        //update by satrya 2014-01-15
        //yg melakukan request dapat notip setelah execute atau approve
        if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED || leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED) {
            try {
                if (employee.getEmailAddress() != null && employee.getEmailAddress().length() > 0 && !employee.getEmailAddress().equalsIgnoreCase("-")) {
                    recipientsCC.add(employee.getEmailAddress());
                }
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }

        if (recx != null && recx.size() > 0) {
            try {// send email as a thread ..
                MailSender.postMailThread(recx, recipientsCC,
                        recipientsBCC, subject, txtMessage, from,
                        host, port, username, password, SSL, attacment, configEmailWithImage);
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }

    }

    /**
     * Keterangan: untuk sending email create by satrya 2013-12-10
     *
     * @param leaveApp
     * @param approot
     * @param oidEmployeeLogin
     */
    public static void sendEmail(LeaveApplication leaveApp, String approot, long oidEmployeeLogin) {
        String emailOn = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_ON"); //0= off 1=on
        if (!(emailOn.contains("1"))) {
            return;
        }

        String harismaURL = PstSystemProperty.getValueByName("HARISMA_URL");

        if (harismaURL != null) {

            if (!harismaURL.endsWith("/login.jsp")) {
                harismaURL = harismaURL + "/login.jsp";
            }
        }
        //update by satrya 2013-11-04

        String harismaUrlInternet = PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) || PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET").length() == 0 ? null : PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET");
        if (harismaUrlInternet != null && harismaUrlInternet.length() > 0 && !harismaUrlInternet.equalsIgnoreCase("-")) {
            if (!harismaUrlInternet.endsWith("/login.jsp")) {
                harismaUrlInternet = harismaUrlInternet + "/login.jsp";
            }
        }

        String from = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_FROM"); //"support@dimata.com"
        String host = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_HOST"); //"beagle2.webappcabaret.net";
        int port = 25;

        try {
            port = Integer.parseInt(PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_PORT")); //25;
        } catch (Exception exc) {
        }

        //update by satrya 2013-11-14
        boolean configEmailWithImage = false;
        try {
            configEmailWithImage = Boolean.parseBoolean(PstSystemProperty.getValueByName("CONFIGURASI_EMAIL_LEAVE_WITH_PICTURE")); //25;
        } catch (Exception exc) {
            configEmailWithImage = false;
        }
        String username = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_USERNAME"); //"support@dimata.com";
        String password = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_PASSWORD"); //"doxxxxx";
        boolean SSL = false;
        try {
            String sSSL = PstSystemProperty.getValueByName("EMAIL_SSL_SETTING");
            SSL = Boolean.parseBoolean(sSSL);
        } catch (Exception exc) {
            SSL = false;
        }

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        //update by satrya 2013-11-14
        Vector listLeaveApplication = new Vector();
        if (configEmailWithImage) {
            listLeaveApplication = PstLeaveApplication.searchNameLeaveWithEmail(leaveApp);
        }

        /**
         * update by satrya 2013-10-30 Employee employee = null; Department
         * department = new Department();
         */
        String attacment = /*approot+"/"+*/ "imgcache/no_photo.JPEG";
        String imgPicture = "no_photo.JPEG";
        Employee employee = new Employee();
        PayGeneral company = null;
        Division division = null;
        Department department = null;
        Section section = null;
        Position position = null;
        String imageDataString = null;
        try {
            if (leaveApp.getEmployeeId() != 0) {
                try {
                    employee = PstEmployee.fetchExc(leaveApp.getEmployeeId());
                } catch (Exception exc) {
                    employee = new Employee();
                    System.out.println("Exc" + exc);
                }
            } else {
                employee = new Employee();
            }

            if (employee.getCompanyId() != 0) {
                try {
                    company = PstPayGeneral.fetchExc(employee.getCompanyId());
                } catch (Exception exc) {
                    company = new PayGeneral();
                    System.out.println("Exc" + exc);
                }
            } else {
                company = new PayGeneral();
            }

            if (employee.getDivisionId() != 0) {
                try {
                    division = PstDivision.fetchExc(employee.getDivisionId());
                } catch (Exception exc) {
                    division = new Division();
                    System.out.println("Exc" + exc);
                }
            } else {
                division = new Division();
            }

            if (employee.getDepartmentId() != 0) {
                try {
                    department = PstDepartment.fetchExc(employee.getDepartmentId());
                } catch (Exception exc) {
                    department = new Department();
                    System.out.println("Exc" + exc);
                }
            } else {
                department = new Department();
            }

            if (employee.getSectionId() != 0) {
                try {
                    section = PstSection.fetchExc(employee.getSectionId());
                } catch (Exception exc) {
                    section = new Section();
                    System.out.println("Exc" + exc);
                }
            } else {
                section = new Section();
            }
            if (employee.getPositionId() != 0) {
                try {
                    position = PstPosition.fetchExc(employee.getPositionId());
                } catch (Exception exc) {
                    position = new Position();
                    System.out.println("Exc" + exc);
                }
            } else {
                position = new Position();
            }
            SessEmployeePicture sessEmployeePicture = new SessEmployeePicture();
            attacment = /*approot+"/"+*/ sessEmployeePicture.fetchImageEmployeeVer1(employee.getOID());
            if (attacment != null && attacment.length() > 0) {
                /* update by satrya 2014-01-16 BufferedImage img = ImageIO.read(new File(attacment));
                 File file = new File(attacment);
            
                 int totypessebelumTitik = file.getName().indexOf(".");
                 String typeFile = file.getName().substring(totypessebelumTitik,  file.getName().length());
                 if(typeFile!=null){
                 if(typeFile.equalsIgnoreCase(".JPG") || typeFile.equalsIgnoreCase(".JPEG")|| typeFile.equalsIgnoreCase(".GIF")|| typeFile.equalsIgnoreCase(".PNG")){
                
                 }else{
                 typeFile = "JPG";
                 }
                 }*/
                //imageDataString = "data:image/jpeg;base64,"+MailSender.encodeToString(img, "jpg");
                imageDataString = "cid:logoimg";
            }
        } catch (Exception exc) {
        }

        Vector<String> recipientsCC = new Vector();
        Vector<String> recipientsBCC = new Vector();
        String subject = "Leave Form";

        String directToDocumentLocal = "<a href=\"" + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "\">" + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "</a>";
        String directToDocumentInet = harismaUrlInternet == null || harismaUrlInternet.length() == 0 || harismaUrlInternet.equalsIgnoreCase("-") ? "" : "<a href=\"" + harismaUrlInternet + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "\">" + harismaUrlInternet + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "</a>"; /*harismaURL +"?page_name=leave_app_edit.jsp&page_command="+Command.EDIT+"&data_oid="+leaveApp.getOID() + "&oid_employee="+leaveApp.getEmployeeId()*/;
        //update by satrya 2013-10-30 
        /**
         * String txtMessage =( (leaveApp.getDocStatus() ==
         * PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) ? " Leave
         * application has been final approved. Date : "+
         * Formater.formatDate(leaveApp.getHrManApproveDate(), "dd MMMM yyyy") :
         * ( "New/Update of leave application\n Date:" +
         * Formater.formatDate(leaveApp.getSubmissionDate(), "dd MMMM yyyy")))
         * +"\n" + " Name : " + employee.getFullName() + " Department : "+
         * department.getDepartment() +"\n" + " please access : \n" + "> Direct
         * to document : "+harismaURL +
         * "?page_name=leave_app_edit.jsp&page_command="+Command.EDIT+"&data_oid="+leaveApp.getOID()
         * + "&oid_employee="+leaveApp.getEmployeeId()+"\n" + "> List of
         * documents : "+harismaURL +
         * "?page_name=home.jsp&page_command="+Command.ACTIVATE + "\n\n sent by
         * Dimata Harisma System";
         */
        //update by satrya 2013-11-14
        String txtMessage = "";
        if (configEmailWithImage) {
            String dtOfRequest = "";
            if (listLeaveApplication != null && listLeaveApplication.size() > 0) {
                SessLeaveApplicationEmail sessLeaveAppEmail = (SessLeaveApplicationEmail) listLeaveApplication.get(0);
                dtOfRequest = sessLeaveAppEmail.getDtOfRequest() == null ? "-" : Formater.formatDate(sessLeaveAppEmail.getDtOfRequest(), "dd MMM yyyy");
            }
            txtMessage = "<table width=\"500px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                    + "<tr>"
                    + "<td><table width=\"500px\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
                    + "<tr>"
                    + "<td colspan=\"5\"><table width=\"500px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
                    + "<tr>"
                    + "<td height=\"38px\" colspan=\"9\" valign=\"bottom\" style=\"font-size:18px\" align=\"center\">" + (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED || leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED ? "Leave application has been " + PstLeaveApplication.fieldStatusApplication[leaveApp.getDocStatus()] : "New/Update of leave application") + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td height=\"22px\" colspan=\"9\">&nbsp;</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td height=\"22px\" colspan=\"9\" align=\"right\"><table>"
                    + "<tr>"
                    + "<td>Date of Request</td>"
                    + "<td>:" + dtOfRequest + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    + "</tr>"
                    + "<tr>"
                    //<!--untuk tampilan user -->
                    + "<td><table width=\"500px\">"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"124px\" rowspan=\"6\"><img width=\"124px\" height=\"124px\" src=\"" + (imageDataString != null ? imageDataString : "no.jpg") + "\" nowrap=\"nowrap\" /></td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Name</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + employee.getFullName() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Company</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + company.getCompanyName() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Division</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + division.getDivision() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Department</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + department.getDepartment() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Section</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + (section.getSection() == null || section.getSection().length() == 0 ? "-" : section.getSection()) + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Position</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + (position.getPosition()) + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"124px\">&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Reason</td>"
                    + "<td style=\"font-size:14px\" align=\"left\">:</td>"
                    // +"<td colspan=\"4px\" align=\"left\"><p>"+leaveApp.getLeaveReason()+"</p></td>"
                    + "<td colspan=\"4px\" align=\"left\">"
                    + "<table width=\"250px\">"
                    + "<tr>"
                    + "<td width=\"250px\">"
                    + "<p>" + leaveApp.getLeaveReason() + "</p>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- untuk tampilan user -->
                    + "</tr>";
            txtMessage = txtMessage
                    + "<tr>"
                    //<!-- untuk detail Leave -->
                    + "<td><table width=\"100%\" style=\"border:0.5px;border-style:solid\">"
                    + "<tr>"
                    + "<td colspan=\"2\" align=\"left\"  style=\"font-size:14px;border:0.5px;border-style:solid\">Type Leave</td>"
                    + "<td align=\"left\" style=\"font-size:14px;border:0.5px;border-style:solid\">Date From </td>"
                    + "<td style=\"border:0.5px;border-style:solid\" width=\"178\">Date To </td>"
                    + "<td style=\"border:0.5px;border-style:solid\" width=\"49\">Total</td>"
                    + "</tr>";
            txtMessage = txtMessage;
            String loopDataCuti = "";
            String depHeadFullNameApprovall = "";
            String HrFullNameApprovall = "";
            Hashtable hashSymbolCuty = new Hashtable();
            if (listLeaveApplication != null && listLeaveApplication.size() > 0) {
                boolean leaveConfigCalculationCategoryOff = false;
                try {
                    leaveConfigCalculationCategoryOff = Boolean.parseBoolean(PstSystemProperty.getValueByName("LEAVE_CONFIG_CALCULATE_CATEGORY_SCHEDULE_OFF"));
                } catch (Exception exc) {
                    System.out.println("Exc FrmAlStockTaken" + exc);
                }
                for (int x = 0; x < listLeaveApplication.size(); x++) {
                    SessLeaveApplicationEmail sessLeaveApplicationEmail = (SessLeaveApplicationEmail) listLeaveApplication.get(x);
                    float alQty = sessLeaveApplicationEmail.getTakenDateStart() == null || sessLeaveApplicationEmail.getTakenFinish() == null ? 0 : DateCalc.workDayDifference(sessLeaveApplicationEmail.getTakenDateStart(), sessLeaveApplicationEmail.getTakenFinish(), leaveConfig.getHourOneWorkday());
                    float intersec = sessLeaveApplicationEmail.getTakenDateStart() == null || sessLeaveApplicationEmail.getTakenFinish() == null ? 0 : PstEmpSchedule.breakTimeIntersection(leaveApp.getEmployeeId(), sessLeaveApplicationEmail.getTakenDateStart(), sessLeaveApplicationEmail.getTakenFinish()) / (leaveConfig.getHourOneWorkday() * 60f * 60f * 1000f);
                //update by satrya 2014-01-17

                    if (leaveConfigCalculationCategoryOff) {
                        int diffDay = (int) alQty;//di cari bentuk int'nya
                        //
                        //update by satrya 2013-12-11
                        //untuk mencari hari libur
                        Vector vctSchIDOff = PstScheduleSymbol.getScheduleId(PstScheduleCategory.CATEGORY_OFF);
                        float dayOffSchedule = 0;
                        if (sessLeaveApplicationEmail.getTakenDateStart() != null) {
                            for (int ix = 0; ix < diffDay; ix++) {
                                Date selectedDate = new Date(sessLeaveApplicationEmail.getTakenDateStart().getYear(), sessLeaveApplicationEmail.getTakenDateStart().getMonth(), (sessLeaveApplicationEmail.getTakenDateStart().getDate() + ix));
                                Period period = PstPeriod.getPeriodBySelectedDate(selectedDate);
                                dayOffSchedule = dayOffSchedule + PstEmpSchedule.getStatusDayOff(sessLeaveApplicationEmail.getEmployeeId(), period.getOID(), 0, vctSchIDOff, selectedDate);
                            }
                        }
                        alQty = alQty - dayOffSchedule;
                    }
                    String hours = Formater.formatWorkDayHoursMinutes(alQty - intersec, leaveConfig.getHourOneWorkday(), leaveConfig.getFormatLeave());
                    depHeadFullNameApprovall = sessLeaveApplicationEmail.getFullNameDepHead();
                    HrFullNameApprovall = sessLeaveApplicationEmail.getFullNameHr();

                    String symbolCuty = (String) (hashSymbolCuty.get(sessLeaveApplicationEmail.getSymbole()) == null ? sessLeaveApplicationEmail.getSymbole() : "&nbsp;");
                    hashSymbolCuty.put(sessLeaveApplicationEmail.getSymbole(), sessLeaveApplicationEmail.getSymbole());
                    loopDataCuti = loopDataCuti + "<tr>"
                            + "<td align=\"left\"  style=\"font-size:14px;border:0.5px;border-style:solid\" colspan=\"2\">" + symbolCuty + "</td>"
                            // update by satrya 2014-01-15 +"<td  style=\"font-size:14px;border:0.5px;border-style:solid\" align=\"left\">:</td>"
                            + "<td align=\"left\" style=\"font-size:14px;border:0.5px;border-style:solid\">" + (sessLeaveApplicationEmail.getTakenDateStart() == null ? "-" : Formater.formatDate(sessLeaveApplicationEmail.getTakenDateStart(), "dd/MM/yyyy HH:mm ")) + "</td>"
                            + "<td style=\"border:0.5px;border-style:solid\" width=\"178\">" + (sessLeaveApplicationEmail.getTakenFinish() == null ? "-" : Formater.formatDate(sessLeaveApplicationEmail.getTakenFinish(), "dd/MM/yyyy HH:mm ")) + " </td>"
                            + "<td style=\"border:0.5px;border-style:solid\" width=\"49\">" + hours + "</td>"
                            + "</tr>";
                }
            }

            txtMessage = txtMessage + loopDataCuti + "</table></td></tr>";/*<!-- end untuk detail Leave -->*/

            ///loop dsini
            txtMessage = txtMessage
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk approvall -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">Approved by,</td>"
                    + "<td width=\"50%\" align=\"center\">HRD Approved by,</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk approvall -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk tgl yg approved -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">" + (leaveApp.getDepHeadApproveDate() == null ? "" : Formater.formatDate(leaveApp.getDepHeadApproveDate(), "dd/MM/yyyy")) + "</td>"
                    + "<td width=\"50%\" align=\"center\">" + (leaveApp.getHrManApproveDate() == null ? "" : Formater.formatDate(leaveApp.getHrManApproveDate(), "dd/MM/yyyy")) + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk tgl yg approved -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk nama yg approve -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">" + (depHeadFullNameApprovall == null ? "-" : depHeadFullNameApprovall) + "</td>"
                    + "<td width=\"50%\" align=\"center\">" + (HrFullNameApprovall == null ? "-" : HrFullNameApprovall) + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end nama yg approve -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td align=\"left\">Please click here to approve : </td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk link -->
                    + "<td><table width=\"200px\">"
                    + "<tr>"
                    + "<td width=\"200px\" align=\"left\"><p>" + directToDocumentLocal + "</p></td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>";
            if (directToDocumentInet != null && directToDocumentInet.length() > 0) {
                txtMessage = txtMessage + "<tr>"
                        //<!-- untuk spasi -->
                        + "<td><table width=\"100%\">"
                        + "<tr>"
                        + "<td align=\"left\">If you are out of Local Area Network, please click this link to approve :</td>"
                        + "</tr>"
                        + "</table></td>"
                        //<!-- end untuk spasi -->
                        + "</tr>"
                        + "<tr>"
                        //<!-- untuk link -->
                        + "<td><table width=\"200px\">"
                        + "<tr>"
                        + "<td width=\"200px\" align=\"left\"><p>" + directToDocumentInet + "</p></td>"
                        + "</tr>"
                        + "</table></td>"
                        //<!-- end untuk spasi -->
                        + "</tr>";
            }

            txtMessage = txtMessage + "</table></td>"
                    + "</tr>"
                    + "</table></td>"
                    + "</tr>"
                    + "</table>";
        } else {
            txtMessage = ((leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED)
                    ? " Leave application has been final approved. Date : " + Formater.formatDate(leaveApp.getHrManApproveDate(), "dd MMMM yyyy")
                    : ("New/Update of leave application\n Date:" + Formater.formatDate(leaveApp.getSubmissionDate(), "dd MMMM yyyy"))) + "\n"
                    + " Name : " + employee.getFullName() + " Department : " + department.getDepartment() + "\n"
                    + " please access : \n"
                    + "> Direct to document : " + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID()
                    + "&oid_employee=" + leaveApp.getEmployeeId() + "\n"
                    + "> List of documents  : " + harismaURL + "?page_name=home.jsp&page_command=" + Command.ACTIVATE
                    + "\n\n sent by Dimata Harisma System";
        }
        ///di hidden sementara karena ibu ayu belom mau merubah cuti

        //System.out.println("Check if email will be sent : "+txtMessage);
        Vector listRec = new Vector();

        //if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
        if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
            listRec = getListNextApproval(leaveApp, employee);
        } else {

            if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {
                listRec = getFinalBackApproval(leaveApp, employee);
            } //update by satrya 2014-03-20
            else if (leaveConfig != null && leaveConfig.getConfigurationLeaveApprovall() == I_Leave.LEAVE_CONFIG_AFTER_APPROVALL_HRD_YES_EXECUTE
                    && leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED) {
                listRec = getFinalBackApproval(leaveApp, employee);
            }
        }

        if (listRec == null || listRec.size() < 1) {
            System.out.println("No recipient defined ");
            return;
        }

        Vector<String> recx = new Vector();
        try {
            for (int i = 0; i < listRec.size() && i < 3; i++) {
                Employee req = (Employee) listRec.get(i);
                if (req.getEmailAddress() != null && req.getEmailAddress().length() > 0 && !req.getEmailAddress().equalsIgnoreCase("-")) {
                    recx.add(req.getEmailAddress());
                }

            }
            //update by satrya 2013-12-10
            if ((leaveConfig != null && leaveConfig.getConfigurationLeaveApprovall() == I_Leave.LEAVE_CONFIG_AFTER_APPROVALL_HRD_YES_EXECUTE
                    && leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED)
                    || leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {
                if (oidEmployeeLogin != 0) {
                    Employee req = new Employee();
                    try {
                        req = PstEmployee.fetchExc(oidEmployeeLogin);
                    } catch (Exception exc) {
                        System.out.println("Exc" + exc);
                    }
                    if (req.getEmailAddress() != null && req.getEmailAddress().length() > 0 && !req.getEmailAddress().equalsIgnoreCase("-")) {
                        recx.add(req.getEmailAddress());
                    }

                }

            }

        } catch (Exception exc) {
            System.out.println(exc);
        }

        if (recx != null && recx.size() > 0) {
            try {// send email as a thread .. 
                MailSender.postMailThread(recx, recipientsCC,
                        recipientsBCC, subject, txtMessage, from,
                        host, port, username, password, SSL, attacment, configEmailWithImage);
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }

    }

    public static void sendEmailNew(LeaveApplication leaveApp, String approot, long oidEmployeeLogin) {
        String emailOn = PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_ON"); //0= off 1=on
        if (!(emailOn.contains("1"))) {
            return;
        }

        String harismaURL = PstSystemProperty.getValueByName("HARISMA_URL");

        if (harismaURL != null) {

            if (!harismaURL.endsWith("/login.jsp")) {
                harismaURL = harismaURL + "/login.jsp";
            }
        }
        //update by satrya 2013-11-04

        String harismaUrlInternet = PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) || PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET").length() == 0 ? null : PstSystemProperty.getValueByName("HARISMA_URL_AKSES_INTERNET");
        if (harismaUrlInternet != null && harismaUrlInternet.length() > 0 && !harismaUrlInternet.equalsIgnoreCase("-")) {
            if (!harismaUrlInternet.endsWith("/login.jsp")) {
                harismaUrlInternet = harismaUrlInternet + "/login.jsp";
            }
        }

        //update by satrya 2013-11-14
        boolean configEmailWithImage = false;
        try {
            configEmailWithImage = Boolean.parseBoolean(PstSystemProperty.getValueByName("CONFIGURASI_EMAIL_LEAVE_WITH_PICTURE")); //25;
        } catch (Exception exc) {
            configEmailWithImage = false;
        }

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        //update by satrya 2013-11-14
        Vector listLeaveApplication = new Vector();
        if (configEmailWithImage) {
            listLeaveApplication = PstLeaveApplication.searchNameLeaveWithEmail(leaveApp);
        }

        /**
         * update by satrya 2013-10-30 Employee employee = null; Department
         * department = new Department();
         */
        String attacment = /*approot+"/"+*/ "imgcache/no_photo.JPEG";
        String imgPicture = "no_photo.JPEG";
        Employee employee = new Employee();
        PayGeneral company = null;
        Division division = null;
        Department department = null;
        Section section = null;
        Position position = null;
        String imageDataString = null;
        try {
            if (leaveApp.getEmployeeId() != 0) {
                try {
                    employee = PstEmployee.fetchExc(leaveApp.getEmployeeId());
                } catch (Exception exc) {
                    employee = new Employee();
                    System.out.println("Exc" + exc);
                }
            } else {
                employee = new Employee();
            }

            if (employee.getCompanyId() != 0) {
                try {
                    company = PstPayGeneral.fetchExc(employee.getCompanyId());
                } catch (Exception exc) {
                    company = new PayGeneral();
                    System.out.println("Exc" + exc);
                }
            } else {
                company = new PayGeneral();
            }

            if (employee.getDivisionId() != 0) {
                try {
                    division = PstDivision.fetchExc(employee.getDivisionId());
                } catch (Exception exc) {
                    division = new Division();
                    System.out.println("Exc" + exc);
                }
            } else {
                division = new Division();
            }

            if (employee.getDepartmentId() != 0) {
                try {
                    department = PstDepartment.fetchExc(employee.getDepartmentId());
                } catch (Exception exc) {
                    department = new Department();
                    System.out.println("Exc" + exc);
                }
            } else {
                department = new Department();
            }

            if (employee.getSectionId() != 0) {
                try {
                    section = PstSection.fetchExc(employee.getSectionId());
                } catch (Exception exc) {
                    section = new Section();
                    System.out.println("Exc" + exc);
                }
            } else {
                section = new Section();
            }
            if (employee.getPositionId() != 0) {
                try {
                    position = PstPosition.fetchExc(employee.getPositionId());
                } catch (Exception exc) {
                    position = new Position();
                    System.out.println("Exc" + exc);
                }
            } else {
                position = new Position();
            }
            SessEmployeePicture sessEmployeePicture = new SessEmployeePicture();
            attacment = /*approot+"/"+*/ sessEmployeePicture.fetchImageEmployeeVer1(employee.getOID());
            if (attacment != null && attacment.length() > 0) {
                /* update by satrya 2014-01-16 BufferedImage img = ImageIO.read(new File(attacment));
                 File file = new File(attacment);
            
                 int totypessebelumTitik = file.getName().indexOf(".");
                 String typeFile = file.getName().substring(totypessebelumTitik,  file.getName().length());
                 if(typeFile!=null){
                 if(typeFile.equalsIgnoreCase(".JPG") || typeFile.equalsIgnoreCase(".JPEG")|| typeFile.equalsIgnoreCase(".GIF")|| typeFile.equalsIgnoreCase(".PNG")){
                
                 }else{
                 typeFile = "JPG";
                 }
                 }*/
                //imageDataString = "data:image/jpeg;base64,"+MailSender.encodeToString(img, "jpg");
                imageDataString = "cid:logoimg";
            }
        } catch (Exception exc) {
        }

        Vector<String> recipientsCC = new Vector();
        Vector<String> recipientsBCC = new Vector();
        String subject = "Leave Form";

        String directToDocumentLocal = "<a href=\"" + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "\">" + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "</a>";
        String directToDocumentInet = harismaUrlInternet == null || harismaUrlInternet.length() == 0 || harismaUrlInternet.equalsIgnoreCase("-") ? "" : "<a href=\"" + harismaUrlInternet + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "\">" + harismaUrlInternet + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID() + "&oid_employee=" + leaveApp.getEmployeeId() + "</a>"; /*harismaURL +"?page_name=leave_app_edit.jsp&page_command="+Command.EDIT+"&data_oid="+leaveApp.getOID() + "&oid_employee="+leaveApp.getEmployeeId()*/;
        //update by satrya 2013-10-30 
        /**
         * String txtMessage =( (leaveApp.getDocStatus() ==
         * PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) ? " Leave
         * application has been final approved. Date : "+
         * Formater.formatDate(leaveApp.getHrManApproveDate(), "dd MMMM yyyy") :
         * ( "New/Update of leave application\n Date:" +
         * Formater.formatDate(leaveApp.getSubmissionDate(), "dd MMMM yyyy")))
         * +"\n" + " Name : " + employee.getFullName() + " Department : "+
         * department.getDepartment() +"\n" + " please access : \n" + "> Direct
         * to document : "+harismaURL +
         * "?page_name=leave_app_edit.jsp&page_command="+Command.EDIT+"&data_oid="+leaveApp.getOID()
         * + "&oid_employee="+leaveApp.getEmployeeId()+"\n" + "> List of
         * documents : "+harismaURL +
         * "?page_name=home.jsp&page_command="+Command.ACTIVATE + "\n\n sent by
         * Dimata Harisma System";
         */
        //update by satrya 2013-11-14
        String txtMessage = "";
        if (configEmailWithImage) {
            String dtOfRequest = "";
            if (listLeaveApplication != null && listLeaveApplication.size() > 0) {
                SessLeaveApplicationEmail sessLeaveAppEmail = (SessLeaveApplicationEmail) listLeaveApplication.get(0);
                dtOfRequest = sessLeaveAppEmail.getDtOfRequest() == null ? "-" : Formater.formatDate(sessLeaveAppEmail.getDtOfRequest(), "dd MMM yyyy");
            }
            txtMessage = "<table width=\"500px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">"
                    + "<tr>"
                    + "<td><table width=\"500px\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
                    + "<tr>"
                    + "<td colspan=\"5\"><table width=\"500px\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\">"
                    + "<tr>"
                    + "<td height=\"38px\" colspan=\"9\" valign=\"bottom\" style=\"font-size:18px\" align=\"center\">" + (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED || leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED ? "Leave application has been " + PstLeaveApplication.fieldStatusApplication[leaveApp.getDocStatus()] : "New/Update of leave application") + "</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td height=\"22px\" colspan=\"9\">&nbsp;</td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td height=\"22px\" colspan=\"9\" align=\"right\"><table>"
                    + "<tr>"
                    + "<td>Date of Request</td>"
                    + "<td>:" + dtOfRequest + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    + "</tr>"
                    + "<tr>"
                    //<!--untuk tampilan user -->
                    + "<td><table width=\"500px\">"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"124px\" rowspan=\"6\"><img width=\"124px\" height=\"124px\" src=\"" + (imageDataString != null ? imageDataString : "no.jpg") + "\" nowrap=\"nowrap\" /></td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Name</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + employee.getFullName() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Company</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + company.getCompanyName() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Division</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + division.getDivision() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Department</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + department.getDepartment() + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Section</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + (section.getSection() == null || section.getSection().length() == 0 ? "-" : section.getSection()) + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Position</td>"
                    + "<td  style=\"font-size:14px\" align=\"left\">:</td>"
                    + "<td colspan=\"4px\" align=\"left\"><p>" + (position.getPosition()) + "</p></td>"
                    + "</tr>"
                    + "<tr>"
                    + "<td width=\"4px\" >&nbsp;</td>"
                    + "<td width=\"124px\">&nbsp;</td>"
                    + "<td width=\"66px\" height=\"30\" style=\"font-size:14px\" align=\"left\" nowrap=\"nowrap\">Reason</td>"
                    + "<td style=\"font-size:14px\" align=\"left\">:</td>"
                    // +"<td colspan=\"4px\" align=\"left\"><p>"+leaveApp.getLeaveReason()+"</p></td>"
                    + "<td colspan=\"4px\" align=\"left\">"
                    + "<table width=\"250px\">"
                    + "<tr>"
                    + "<td width=\"250px\">"
                    + "<p>" + leaveApp.getLeaveReason() + "</p>"
                    + "</td>"
                    + "</tr>"
                    + "</table>"
                    + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- untuk tampilan user -->
                    + "</tr>";
            txtMessage = txtMessage
                    + "<tr>"
                    //<!-- untuk detail Leave -->
                    + "<td><table width=\"100%\" style=\"border:0.5px;border-style:solid\">"
                    + "<tr>"
                    + "<td colspan=\"2\" align=\"left\"  style=\"font-size:14px;border:0.5px;border-style:solid\">Type Leave</td>"
                    + "<td align=\"left\" style=\"font-size:14px;border:0.5px;border-style:solid\">Date From </td>"
                    + "<td style=\"border:0.5px;border-style:solid\" width=\"178\">Date To </td>"
                    + "<td style=\"border:0.5px;border-style:solid\" width=\"49\">Total</td>"
                    + "</tr>";
            txtMessage = txtMessage;
            String loopDataCuti = "";
            String depHeadFullNameApprovall = "";
            String HrFullNameApprovall = "";
            Hashtable hashSymbolCuty = new Hashtable();
            if (listLeaveApplication != null && listLeaveApplication.size() > 0) {
                boolean leaveConfigCalculationCategoryOff = false;
                try {
                    leaveConfigCalculationCategoryOff = Boolean.parseBoolean(PstSystemProperty.getValueByName("LEAVE_CONFIG_CALCULATE_CATEGORY_SCHEDULE_OFF"));
                } catch (Exception exc) {
                    System.out.println("Exc FrmAlStockTaken" + exc);
                }
                for (int x = 0; x < listLeaveApplication.size(); x++) {
                    SessLeaveApplicationEmail sessLeaveApplicationEmail = (SessLeaveApplicationEmail) listLeaveApplication.get(x);
                    float alQty = sessLeaveApplicationEmail.getTakenDateStart() == null || sessLeaveApplicationEmail.getTakenFinish() == null ? 0 : DateCalc.workDayDifference(sessLeaveApplicationEmail.getTakenDateStart(), sessLeaveApplicationEmail.getTakenFinish(), leaveConfig.getHourOneWorkday());
                    float intersec = sessLeaveApplicationEmail.getTakenDateStart() == null || sessLeaveApplicationEmail.getTakenFinish() == null ? 0 : PstEmpSchedule.breakTimeIntersection(leaveApp.getEmployeeId(), sessLeaveApplicationEmail.getTakenDateStart(), sessLeaveApplicationEmail.getTakenFinish()) / (leaveConfig.getHourOneWorkday() * 60f * 60f * 1000f);
                //update by satrya 2014-01-17

                    if (leaveConfigCalculationCategoryOff) {
                        int diffDay = (int) alQty;//di cari bentuk int'nya
                        //
                        //update by satrya 2013-12-11
                        //untuk mencari hari libur
                        Vector vctSchIDOff = PstScheduleSymbol.getScheduleId(PstScheduleCategory.CATEGORY_OFF);
                        float dayOffSchedule = 0;
                        if (sessLeaveApplicationEmail.getTakenDateStart() != null) {
                            for (int ix = 0; ix < diffDay; ix++) {
                                Date selectedDate = new Date(sessLeaveApplicationEmail.getTakenDateStart().getYear(), sessLeaveApplicationEmail.getTakenDateStart().getMonth(), (sessLeaveApplicationEmail.getTakenDateStart().getDate() + ix));
                                Period period = PstPeriod.getPeriodBySelectedDate(selectedDate);
                                dayOffSchedule = dayOffSchedule + PstEmpSchedule.getStatusDayOff(sessLeaveApplicationEmail.getEmployeeId(), period.getOID(), 0, vctSchIDOff, selectedDate);
                            }
                        }
                        alQty = alQty - dayOffSchedule;
                    }
                    String hours = Formater.formatWorkDayHoursMinutes(alQty - intersec, leaveConfig.getHourOneWorkday(), leaveConfig.getFormatLeave());
                    depHeadFullNameApprovall = sessLeaveApplicationEmail.getFullNameDepHead();
                    HrFullNameApprovall = sessLeaveApplicationEmail.getFullNameHr();

                    String symbolCuty = (String) (hashSymbolCuty.get(sessLeaveApplicationEmail.getSymbole()) == null ? sessLeaveApplicationEmail.getSymbole() : "&nbsp;");
                    hashSymbolCuty.put(sessLeaveApplicationEmail.getSymbole(), sessLeaveApplicationEmail.getSymbole());
                    loopDataCuti = loopDataCuti + "<tr>"
                            + "<td align=\"left\"  style=\"font-size:14px;border:0.5px;border-style:solid\" colspan=\"2\">" + symbolCuty + "</td>"
                            // update by satrya 2014-01-15 +"<td  style=\"font-size:14px;border:0.5px;border-style:solid\" align=\"left\">:</td>"
                            + "<td align=\"left\" style=\"font-size:14px;border:0.5px;border-style:solid\">" + (sessLeaveApplicationEmail.getTakenDateStart() == null ? "-" : Formater.formatDate(sessLeaveApplicationEmail.getTakenDateStart(), "dd/MM/yyyy HH:mm ")) + "</td>"
                            + "<td style=\"border:0.5px;border-style:solid\" width=\"178\">" + (sessLeaveApplicationEmail.getTakenFinish() == null ? "-" : Formater.formatDate(sessLeaveApplicationEmail.getTakenFinish(), "dd/MM/yyyy HH:mm ")) + " </td>"
                            + "<td style=\"border:0.5px;border-style:solid\" width=\"49\">" + hours + "</td>"
                            + "</tr>";
                }
            }

            txtMessage = txtMessage + loopDataCuti + "</table></td></tr>";/*<!-- end untuk detail Leave -->*/

            ///loop dsini
            txtMessage = txtMessage
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk approvall -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">Approved by,</td>"
                    + "<td width=\"50%\" align=\"center\">HRD Approved by,</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk approvall -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk tgl yg approved -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">" + (leaveApp.getDepHeadApproveDate() == null ? "" : Formater.formatDate(leaveApp.getDepHeadApproveDate(), "dd/MM/yyyy")) + "</td>"
                    + "<td width=\"50%\" align=\"center\">" + (leaveApp.getHrManApproveDate() == null ? "" : Formater.formatDate(leaveApp.getHrManApproveDate(), "dd/MM/yyyy")) + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk tgl yg approved -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk nama yg approve -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">" + (depHeadFullNameApprovall == null ? "-" : depHeadFullNameApprovall) + "</td>"
                    + "<td width=\"50%\" align=\"center\">" + (HrFullNameApprovall == null ? "-" : HrFullNameApprovall) + "</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end nama yg approve -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "<td width=\"50%\" align=\"center\">&nbsp;</td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk spasi -->
                    + "<td><table width=\"100%\">"
                    + "<tr>"
                    + "<td align=\"left\">Please click here to approve : </td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>"
                    + "<tr>"
                    //<!-- untuk link -->
                    + "<td><table width=\"200px\">"
                    + "<tr>"
                    + "<td width=\"200px\" align=\"left\"><p>" + directToDocumentLocal + "</p></td>"
                    + "</tr>"
                    + "</table></td>"
                    //<!-- end untuk spasi -->
                    + "</tr>";
            if (directToDocumentInet != null && directToDocumentInet.length() > 0) {
                txtMessage = txtMessage + "<tr>"
                        //<!-- untuk spasi -->
                        + "<td><table width=\"100%\">"
                        + "<tr>"
                        + "<td align=\"left\">If you are out of Local Area Network, please click this link to approve :</td>"
                        + "</tr>"
                        + "</table></td>"
                        //<!-- end untuk spasi -->
                        + "</tr>"
                        + "<tr>"
                        //<!-- untuk link -->
                        + "<td><table width=\"200px\">"
                        + "<tr>"
                        + "<td width=\"200px\" align=\"left\"><p>" + directToDocumentInet + "</p></td>"
                        + "</tr>"
                        + "</table></td>"
                        //<!-- end untuk spasi -->
                        + "</tr>";
            }

            txtMessage = txtMessage + "</table></td>"
                    + "</tr>"
                    + "</table></td>"
                    + "</tr>"
                    + "</table>";
        } else {
            txtMessage = ((leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED)
                    ? " Leave application has been final approved. Date : " + Formater.formatDate(leaveApp.getHrManApproveDate(), "dd MMMM yyyy")
                    : ("New/Update of leave application\n Date:" + Formater.formatDate(leaveApp.getSubmissionDate(), "dd MMMM yyyy"))) + "\n"
                    + " Name : " + employee.getFullName() + " Department : " + department.getDepartment() + "\n"
                    + " please access : \n"
                    + "> Direct to document : " + harismaURL + "?page_name=leave_app_edit.jsp&page_command=" + Command.EDIT + "&data_oid=" + leaveApp.getOID()
                    + "&oid_employee=" + leaveApp.getEmployeeId() + "\n"
                    + "> List of documents  : " + harismaURL + "?page_name=home.jsp&page_command=" + Command.ACTIVATE
                    + "\n\n sent by Dimata Harisma System";
        }
        ///di hidden sementara karena ibu ayu belom mau merubah cuti

        //System.out.println("Check if email will be sent : "+txtMessage);
        Vector listRec = new Vector();

        //if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
        if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {

            listRec = leaveConfig.getApprovalEmployeeTopLink(employee.getOID(), 3);
            if (listRec.size() == 0) {
                listRec = getListNextApproval(leaveApp, employee);
            }

        } else {

            if (leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {

                listRec = leaveConfig.getApprovalEmployeeTopLink(employee.getOID(), 3);
                if (listRec.size() == 0) {
                    listRec = getFinalBackApproval(leaveApp, employee);
                }
            } //update by satrya 2014-03-20
            else if (leaveConfig != null && leaveConfig.getConfigurationLeaveApprovall() == I_Leave.LEAVE_CONFIG_AFTER_APPROVALL_HRD_YES_EXECUTE
                    && leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED) {
                listRec = leaveConfig.getApprovalEmployeeTopLink(employee.getOID(), 3);
                if (listRec.size() == 0) {
                    listRec = getFinalBackApproval(leaveApp, employee);
                }

            }
        }

        if (listRec == null || listRec.size() < 1) {
            System.out.println("No recipient defined ");
            return;
        }

        Vector<String> recx = new Vector();
        try {
            for (int i = 0; i < listRec.size() && i < 3; i++) {
                Employee req = (Employee) listRec.get(i);
                if (req.getEmailAddress() != null && req.getEmailAddress().length() > 0 && !req.getEmailAddress().equalsIgnoreCase("-")) {
                    recx.add(req.getEmailAddress());
                }

            }
            //update by satrya 2013-12-10
            if ((leaveConfig != null && leaveConfig.getConfigurationLeaveApprovall() == I_Leave.LEAVE_CONFIG_AFTER_APPROVALL_HRD_YES_EXECUTE
                    && leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED)
                    || leaveApp.getDocStatus() == PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {
                if (oidEmployeeLogin != 0) {
                    Employee req = new Employee();
                    try {
                        req = PstEmployee.fetchExc(oidEmployeeLogin);
                    } catch (Exception exc) {
                        System.out.println("Exc" + exc);
                    }
                    if (req.getEmailAddress() != null && req.getEmailAddress().length() > 0 && !req.getEmailAddress().equalsIgnoreCase("-")) {
                        recx.add(req.getEmailAddress());
                    }

                }

            }

        } catch (Exception exc) {
            System.out.println(exc);
        }

        if (recx != null && recx.size() > 0) {
            try {// send email as a thread .. 
//                MailSender.postMailThread(recx, recipientsCC,
//                        recipientsBCC, subject, txtMessage, from,
//                        host, port, username, password, SSL,attacment,configEmailWithImage);
                Vector<DataSource> dataAttch = new Vector();
                //email.sendEmail(recx, null, null, " Custom List", "Custom Report", null, null);
                email.sendEmail(recx, recipientsCC, recipientsBCC, subject, txtMessage, null, null);
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }

    }

    public static Vector getListNextApproval(LeaveApplication objLeaveApplication, Employee objEmployee) {
        Vector listNextApproval = new Vector();
        I_Leave leaveConfig = null;
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        if (leaveConfig == null) {
            return new Vector();
        }
        int maxApproval = leaveConfig.getMaxApproval(objLeaveApplication.getEmployeeId());

        boolean deptHead = false;
        boolean HRDMan = false;

        /* untuk Dep Head Approval */
        if ((objLeaveApplication.getOID() != 0) && (maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)
                && objLeaveApplication.getDepHeadApproval() == 0) {
            listNextApproval = leaveConfig.getApprovalDepartmentHead(objLeaveApplication.getEmployeeId());
        }

        /* untuk HR approval */
        if (objLeaveApplication.getOID() != 0 && ( /* jika  bukan level manager ke bawah maka setelah approval dep head approval HRD */((maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)
                && objLeaveApplication.getDepHeadApproval() != 0 && objLeaveApplication.getHrManApproval() == 0)
                || /* jika level manager ke atas maka  , dephead approval */ ((maxApproval == I_Leave.LEAVE_APPROVE_3 || maxApproval == I_Leave.LEAVE_APPROVE_4 || maxApproval == I_Leave.LEAVE_APPROVE_5)
                && objLeaveApplication.getDepHeadApproval() == 0 && objLeaveApplication.getHrManApproval() == 0))) {
            listNextApproval = leaveConfig.listHRManager(objLeaveApplication.getEmployeeId());
        }

        if (objLeaveApplication.getOID() != 0
                && (maxApproval == I_Leave.LEAVE_APPROVE_3 || maxApproval == I_Leave.LEAVE_APPROVE_5)
                /*&& objLeaveApplication.getDepHeadApproval()==0*/ && objLeaveApplication.getHrManApproval() != 0
                && objLeaveApplication.getGmApproval() == 0) {
            try {
                String emailNotiftoFinalApprover = "" + PstSystemProperty.getValueByName("EMAIL_NOTIFICATION_GM"); //0= off 1=on
                if (!emailNotiftoFinalApprover.contains("1")) {
                    long oidGM = Long.parseLong(PstSystemProperty.getValueByName("GM_POS_ID"));
                    if (oidGM != 0) {
                        Employee emGM = PstEmployee.getEmployeeByPositionId(oidGM);
                        if (emGM != null && emGM.getFullName().length() > 1) {
                            listNextApproval.add(emGM);
                        }
                    }
                }
            } catch (Exception E) {
            }

            try {
                long oidHRDir = Long.parseLong(PstSystemProperty.getValueByName("HR_DIR_POS_ID"));
                if (oidHRDir != 0) {
                    Employee emHRDir = PstEmployee.getEmployeeByPositionId(oidHRDir);
                    if (emHRDir != null && emHRDir.getFullName().length() > 1) {
                        listNextApproval.add(emHRDir);
                    }
                }
            } catch (Exception E) {
                System.out.println(" HR Director not set in System Property : HR_DIRECTOR");
            }

            if (listNextApproval == null || listNextApproval.size() == 0) {
                Vector vectPositionLvl1 = new Vector(1, 1);
                vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                listNextApproval = SessEmployee.listEmployeeByPositionLevelGeneralM(objEmployee, vectPositionLvl1);
            }

        }
        return listNextApproval;
    }

    public static Vector getFinalBackApproval(LeaveApplication objLeaveApplication, Employee objEmployee) {
        Vector listFinalbackApproval = new Vector();
        I_Leave leaveConfig = null;
        String oidHrdEmpNotSendEmail = "";
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            oidHrdEmpNotSendEmail = PstSystemProperty.getValueByName("OID_EMP_HRD_NOT_SEND_EMAIL");
            if (oidHrdEmpNotSendEmail != null && oidHrdEmpNotSendEmail.equalsIgnoreCase("-")) {
                oidHrdEmpNotSendEmail = "";
            }
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        if (leaveConfig == null) {
            return new Vector();
        }
        int maxApproval = leaveConfig.getMaxApproval(objLeaveApplication.getEmployeeId());

        boolean deptHead = false;
        boolean HRDMan = false;

        /* untuk Dep Head Approval */
        if ((objLeaveApplication.getOID() != 0) && (maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)
                && objLeaveApplication.getDepHeadApproval() != 0) {
            listFinalbackApproval = leaveConfig.getApprovalDepartmentHead(objLeaveApplication.getEmployeeId());
        }

        /* untuk HR approval */
        if (objLeaveApplication.getOID() != 0 && ( /* jika  bukan level manager ke bawah maka setelah approval dep head approval HRD */((maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)
                && objLeaveApplication.getDepHeadApproval() != 0 && objLeaveApplication.getHrManApproval() != 0)
                || /* jika level manager ke atas maka  , dephead approval */ ((maxApproval == I_Leave.LEAVE_APPROVE_3 || maxApproval == I_Leave.LEAVE_APPROVE_4 || maxApproval == I_Leave.LEAVE_APPROVE_5)
                && objLeaveApplication.getDepHeadApproval() == 0 && objLeaveApplication.getHrManApproval() != 0))) {
            listFinalbackApproval = leaveConfig.listHRManagerSendEmail(objLeaveApplication.getEmployeeId(), oidHrdEmpNotSendEmail);
        }

        if (objLeaveApplication.getOID() != 0
                && (maxApproval == I_Leave.LEAVE_APPROVE_3 || maxApproval == I_Leave.LEAVE_APPROVE_5)
                /*&& objLeaveApplication.getDepHeadApproval()==0*/ && objLeaveApplication.getHrManApproval() != 0
                && objLeaveApplication.getGmApproval() != 0) {
            /*try {
             long oidGM = Long.parseLong(PstSystemProperty.getValueByName("GM_POS_ID"));
             if (oidGM != 0) {
             Employee emGM = PstEmployee.getEmployeeByPositionId(oidGM);
             if (emGM != null && emGM.getFullName().length() > 1) {
             listFinalbackApproval.add(emGM);
             }
             }
             } catch (Exception E) {
             }*/

            try {
                long oidHRDir = Long.parseLong(PstSystemProperty.getValueByName("HR_DIR_POS_ID"));
                if (oidHRDir != 0) {
                    Employee emHRDir = PstEmployee.getEmployeeByPositionId(oidHRDir);
                    if (emHRDir != null && emHRDir.getFullName().length() > 1) {
                        listFinalbackApproval.add(emHRDir);
                    }
                }
            } catch (Exception E) {
                System.out.println(" HR Director not set in System Property : HR_DIRECTOR");
            }

            /*  if (listFinalbackApproval == null || listFinalbackApproval.size() == 0) {
             Vector vectPositionLvl1 = new Vector(1, 1);
             vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);                                    
             listFinalbackApproval = SessEmployee.listEmployeeByPositionLevelGeneralM(objEmployee, vectPositionLvl1);
             }*/
        }
        //yg cuti juga akan mendapatkan email
        //di hidden karena sudah ada d depannya mekakai cc  by satrya 2014-01-15 listFinalbackApproval.add(objEmployee);
        return listFinalbackApproval;
    }
    
    public void setLeavePreparedBy(long oidLeaveApplication, long oidEmployePrepare) {
        try {
            LeaveApplication leaveApplication = PstLeaveApplication.fetchExc(oidLeaveApplication);
            leaveApplication.setEmployeePrepareId(oidEmployePrepare);
            PstLeaveApplication.updateExc(leaveApplication);
        } catch (Exception e) {
        }
    }

}
