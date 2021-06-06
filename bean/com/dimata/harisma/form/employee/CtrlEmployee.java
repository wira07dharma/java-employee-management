/* 
 * Ctrl Name  		:  CtrlEmployee.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  		: karya 
 * @version  		: 01 
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...] 
 ******************************************************************
 */
package com.dimata.harisma.form.employee;
/* java package */

import com.dimata.common.entity.contact.ContactList;
import com.dimata.common.entity.contact.PstContactList;
import com.dimata.harisma.entity.admin.AppUser;
import com.dimata.harisma.entity.admin.PstAppUser;
import java.util.Date;
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
import com.dimata.harisma.entity.locker.*;
import com.dimata.harisma.form.locker.*;
import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.entity.log.I_LogHistory;
import com.dimata.harisma.entity.log.LogSysHistory;
import com.dimata.harisma.entity.log.PstLogSysHistory;
import com.dimata.harisma.entity.masterdata.Company;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.Division;
import com.dimata.harisma.entity.masterdata.EmpCategory;
import com.dimata.harisma.entity.masterdata.GradeLevel;
import com.dimata.harisma.entity.masterdata.Level;
import com.dimata.harisma.entity.masterdata.LockerLocation;
import com.dimata.harisma.entity.masterdata.Marital;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstCompany;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.harisma.entity.masterdata.PstDivision;
import com.dimata.harisma.entity.masterdata.PstEmpCategory;
import com.dimata.harisma.entity.masterdata.PstGradeLevel;
import com.dimata.harisma.entity.masterdata.PstLevel;
import com.dimata.harisma.entity.masterdata.PstLockerLocation;
import com.dimata.harisma.entity.masterdata.PstMarital;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.harisma.entity.masterdata.PstRace;
import com.dimata.harisma.entity.masterdata.PstReligion;
import com.dimata.harisma.entity.masterdata.PstResignedReason;
import com.dimata.harisma.entity.masterdata.PstSection;
import com.dimata.harisma.entity.masterdata.Race;
import com.dimata.harisma.entity.masterdata.Religion;
import com.dimata.harisma.entity.masterdata.ResignedReason;
import com.dimata.harisma.entity.masterdata.Section;
import com.dimata.harisma.session.employee.SessEmployee;
import com.dimata.harisma.session.leave.SessLeaveClosing;
import com.dimata.qdep.entity.Entity;
import com.dimata.system.entity.system.PstSystemProperty;
import java.sql.*;
import java.util.Vector;
import org.apache.jasper.tagplugins.jstl.core.Catch;

public class CtrlEmployee extends Control implements I_Language {

    public static int RSLT_OK = 0;
    
    
    public static int RSLT_UNKNOWN_ERROR = 1;
    public static int RSLT_EST_CODE_EXIST = 2;
    public static int RSLT_FORM_INCOMPLETE = 3;
    public static int RSLT_EMPLYEE_NUM_EXIST = 4;
    public static String[][] resultText = {
        {"Berhasil", "Tidak dapat diproses", "NoPerkiraan sudah ada", "Data tidak lengkap", " employee number sudah ada"},
        {"Succes", "Can not process", "Estimation code exist", "Data incomplete", "Employee number exist"}};
    private int start;
    private String msgString;
    private Employee employee;
    private PstEmployee pstEmployee;
    private FrmEmployee frmEmployee;
    //locker;
    private Locker locker;
    private PstLocker pstLocker;
    private FrmLocker frmLocker;
    //mutation
    private FrmEmployeeMutation frmEmployeeMutation;
    
    int language = LANGUAGE_DEFAULT;

    public CtrlEmployee(HttpServletRequest request) {
        msgString = "";
        employee = new Employee();
        locker = new Locker();
        try {
            pstEmployee = new PstEmployee(0);
            pstLocker = new PstLocker(0);
        } catch (Exception e) {
            ;
        }

        frmEmployee = new FrmEmployee(request, employee);

        frmLocker = new FrmLocker(request, locker);
    }

    private String getSystemMessage(int msgCode) {
        switch (msgCode) {
            case I_DBExceptionInfo.MULTIPLE_ID:
                this.frmEmployee.addError(frmEmployee.FRM_FIELD_EMPLOYEE_ID, resultText[language][RSLT_EST_CODE_EXIST]);
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

    public Employee getEmployee() {
        return employee;
    }

    public FrmEmployee getForm() {
        return frmEmployee;
    }

    public Locker getLocker() {
        return locker;
    }

    public FrmLocker getFormLocker() {
        return frmLocker;
    }
    
    public FrmEmployeeMutation getFormMutation() {
        return frmEmployeeMutation;
    }

    public String getMessage() {
        return msgString;
    }

    public int getStart() {
        return start;
    }

    public int action(int cmd, long oidEmployee, HttpServletRequest request, String loginName, long userId) {

        String MachineFnSpot = "";

        try {
            MachineFnSpot = PstSystemProperty.getValueByName("MACHINE_FN_SPOT");
        } catch (Exception e) {
            MachineFnSpot = "";
            System.out.println("Exception " + e.toString());
        }

        

        msgString = "";
        String tmpBarcodeNumber = "";
        String tmpFullName = "";
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
        try {
            appUser = PstAppUser.fetch(userId);
            emp = PstEmployee.fetchExc(appUser.getEmployeeId());
        } catch(Exception e){
            System.out.println("Get AppUser: userId: "+e.toString());
        }
        switch (cmd) {
            case Command.ADD:
                break;

            case Command.SAVE:
                Employee prevEmployee = null;
                if (oidEmployee != 0) {
                    try {
                        employee = PstEmployee.fetchExc(oidEmployee);
                        
                        if(sysLog == 1){
                            prevEmployee = PstEmployee.fetchExc(oidEmployee);
                            
                        }
                            
                        tmpBarcodeNumber = employee.getBarcodeNumber();
                        tmpFullName = employee.getFullName();
                    } catch (Exception exc) {
                        System.out.println("Exception Save Employee " + exc);
                    }
                }
                String[] SelectedValues = FRMQueryString.requestStringValues(request, "medicalinfo");// 2015-01-12 update by Hendra McHen
                if(SelectedValues == null){
                    SelectedValues = new String[1];
                }//
                frmEmployee.setSelectedValues(SelectedValues);// 2015-01-12 update by Hendra McHen
                frmEmployee.requestEntityObject(employee);
                if (frmEmployee.errorSize() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_INCOMPLATE);
                    return RSLT_FORM_INCOMPLETE;
                }
                if (employee.getResigned() == PstEmployee.YES_RESIGN) {
                    employee.setLockerId(0);
                    //update by satrya 2012-11-08
                    //employee.setBarcodeNumber(null);
                    //employee.setBarcodeNumber(null);
                }

                // ---- untuk bali dynasty karena tidak memakai locker maka di comment ----
                // get Request Value Of  Locker
                frmLocker.requestEntityObject(locker);
                locker.setOID(employee.getLockerId());
                long locationidX = 0;
                try {
                    locationidX = FRMQueryString.requestLong(request, "LOCKER_LOCATION");
                } catch (Exception ex) {
                    System.out.println("Exception LOCKER_LOCATION not Set" + ex);
                }
                locker.setLocationId(locationidX);
                //locker.setLocationId(employee.getLockerId());
                //locker.setOID(employee.getLockerId());

                //System.out.println("PstEmployee.checkLocker(locker) : " + PstEmployee.checkLocker(locker));
                String strIsCheck = "0";
                try {
                    strIsCheck = PstSystemProperty.getValueByName("LOCKER_MANY_USER");
                } catch (Exception ex) {
                    System.out.println("Exception LOCKER_MANY_USER not Set" + ex);
                }

                if (strIsCheck.equals("0")) { // satu locaker satu pemakai
                    //Dimatikan untuk di hardrock
                    // if(PstEmployee.checkLocker(locker)){
                    //     msgString = "Locker has been used by another employee";
                    //     return RSLT_FORM_INCOMPLETE ;
                    // }
                }

                if ((locker.getLocationId() != 0) && (locker.getLockerNumber() != null && locker.getLockerNumber().length() > 0)) {
                    try {
                        if (locker.getOID() == 0) {
                            long oid = pstLocker.insertExc(locker);
                            employee.setLockerId(oid);
                        } else {
                            System.out.println("++++" + locker.getOID());
                            long oid = PstLocker.updateLocker(locker);
                        }

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                        return getControlMsgId(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                        return getControlMsgId(I_DBExceptionInfo.UNKNOWN);
                    }
                }

                /* pengecekan untuk employee number yang sama */
                String where = PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = \"" + employee.getEmployeeNum() + "\" AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " != " + employee.getOID();

                Vector resultEmp = new Vector();

                try {
                    resultEmp = PstEmployee.list(0, 0, where, null);
                } catch (Exception e) {
                    System.out.println("[Exc] " + e.toString());
                }

                if (resultEmp != null && resultEmp.size() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_EMP_NUM_IN_EXIST);
                    return RSLT_EMPLYEE_NUM_EXIST;
                }

                /* Untuk pengecekan barcode number */
                Vector resultEmpBarcode = new Vector();
                //update by satrya 2012-11-09
                if (employee.getBarcodeNumber() != null) {
                    String whereBarcode = PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = " + "\"" + employee.getBarcodeNumber() + "\"" + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " != " + employee.getOID();
                    try {
                        resultEmpBarcode = PstEmployee.list(0, 0, whereBarcode, null);
                    } catch (Exception e) {
                        System.out.println("Exc " + e.toString());
                    }
                }
                if (resultEmpBarcode != null && resultEmpBarcode.size() > 0) {
                    msgString = FRMMessage.getMsg(FRMMessage.MSG_BARCODE_IN_EXIST);
                    return RSLT_EMPLYEE_NUM_EXIST;
                }

                /* Pengecekan untuk menghindari pin yang sama 
                
                 String wherePin = PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN] + " = " + employee.getEmpPin() + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " != " + employee.getOID() + " AND ( " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN] + " is not null OR " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN] + " != '' ) ";

                 Vector resultEmpPin = new Vector();

                 try {
                 resultEmpPin = PstEmployee.list(0, 0, wherePin, null);
                 } catch (Exception e) {
                 System.out.println("Exc " + e.toString());
                 }

                 if (resultEmpPin != null && resultEmpPin.size() > 0) {
                 msgString = FRMMessage.getMsg(FRMMessage.MSG_PIN_IN_EXIST);
                 return RSLT_EMPLYEE_NUM_EXIST;
                 }
                 */
                employee.getEnd_contract();
                if (employee.getOID() == 0) {
                    try {

                        long oid = pstEmployee.insertExc(this.employee);

                        if (oid != 0) {
                            
                            //buatkan save carrer path
                            if (sysLog != 0) { /* kondisi jika sysLog == 1, maka proses di bawah ini dijalankan*/
                            String className = employee.getClass().getName();
                            LogSysHistory logSysHistory = new LogSysHistory();

                            String reqUrl = request.getRequestURI().toString() + "?employee_oid=" + oidEmployee;
                            /* Lakukan set data ke entity logSysHistory */
                            logSysHistory.setLogDocumentId(0);
                            logSysHistory.setLogUserId(userId);
                            logSysHistory.setApproverId(userId);
                            logSysHistory.setApproveDate(nowDate);
                            logSysHistory.setLogLoginName(loginName);
                            logSysHistory.setLogDocumentNumber("");
                            logSysHistory.setLogDocumentType(className); //entity
                            logSysHistory.setLogUserAction("ADD"); // command
                            logSysHistory.setLogOpenUrl(reqUrl); // locate jsp
                            logSysHistory.setLogUpdateDate(nowDate);
                            logSysHistory.setLogApplication(I_LogHistory.SYSTEM_NAME[I_LogHistory.SYSTEM_HAIRISMA]); // interface
                            /* Inisialisasi logField dengan menggambil field EmpEducation */
                            /* Tips: ambil data field dari persistent */
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PHONE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_SEX]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY]+";";
                            //logField += PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_CURIER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_NR]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_VALID_TO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_TAX_REG_NR]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_FOR_TAX]+";";
                            //logField += PstEmployee.fieldNames[PstEmployee.FLD_NATIONALITY_TYPE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMAIL_ADDRESS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_CATEGORY_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LEAVE_STATUS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RACE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMERGENCY]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_FATHER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MOTHER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PARENTS_ADDRESS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_NAME_EMG]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMG]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_EMG]+";";
                            /*logField += PstEmployee.fieldNames[PstEmployee.FLD_HOD_EMPLOYEE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_COUNTRY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PROVINCE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_REGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_SUBREGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_COUNTRY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_PROVINCE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_REGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_SUBREGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_ISSUED_BY]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_BIRTH_DATE]+";";*/
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_NO_REKENING]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_COMPANY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DIVISION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DEPARTMENT_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_SECTION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_POSITION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_TYPE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_NPWP]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BPJS_NO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BPJS_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_SHIO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ELEMEN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_IQ]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EQ]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PROBATION_END_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_STATUS_PENSIUN_PROGRAM]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_START_DATE_PENSIUN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PRESENCE_CHECK_PARAMETER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MEDICAL_INFO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_DANA_PENDIDIKAN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PROVIDER_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MEMBER_OF_KESEHATAN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MEMBER_OF_KETENAGAKERJAAN]+";";
                            /* data logField yg telah terisi kemudian digunakan untuk setLogDetail */
                            logSysHistory.setLogDetail(logField); // apa yang dirubah
                            /* inisialisasi value, yaitu logCurr */
                            logCurr += ""+employee.getOID()+";";
                            logCurr += ""+employee.getDepartmentId()+";";
                            logCurr += ""+employee.getPositionId()+";";
                            logCurr += ""+employee.getSectionId()+";";
                            logCurr += ""+employee.getEmployeeNum()+";";
                            logCurr += ""+employee.getEmpCategoryId()+";";
                            logCurr += ""+employee.getLevelId()+";";
                            logCurr += ""+employee.getFullName()+";";
                            logCurr += ""+employee.getAddress()+";";
                            logCurr += ""+employee.getPhone()+";";
                            logCurr += ""+employee.getHandphone()+";";
                            logCurr += ""+employee.getPostalCode()+";";
                            logCurr += ""+employee.getSex()+";";
                            logCurr += ""+employee.getBirthPlace()+";";
                            logCurr += ""+employee.getBirthDate()+";";
                            logCurr += ""+employee.getReligionId()+";";
                            logCurr += ""+employee.getBloodType()+";";
                            logCurr += ""+employee.getAstekNum()+";";
                            logCurr += ""+employee.getAstekDate()+";";
                            logCurr += ""+employee.getMaritalId()+";";
                            logCurr += ""+employee.getTaxMaritalId()+";";
                            logCurr += ""+employee.getLockerId()+";";
                            logCurr += ""+employee.getCommencingDate()+";";
                            logCurr += ""+employee.getResigned()+";";
                            logCurr += ""+employee.getResignedDate()+";";
                            logCurr += ""+employee.getBarcodeNumber()+";";
                            logCurr += ""+employee.getResignedReasonId()+";";
                            logCurr += ""+employee.getResignedDesc()+";";
                            logCurr += ""+employee.getBasicSalary()+";";
                            //logCurr += ""+employee.getIsAssignToAccounting()+";";
                            logCurr += ""+employee.getDivisionId()+";";
                            logCurr += ""+employee.getCurier()+";";
                            logCurr += ""+employee.getIndentCardNr()+";";
                            logCurr += ""+employee.getIndentCardValidTo()+";";
                            logCurr += ""+employee.getTaxRegNr()+";";
                            logCurr += ""+employee.getAddressForTax()+";";
                            //logCurr += ""+employee.getNationalityType()+";";
                            logCurr += ""+employee.getEmailAddress()+";";
                            logCurr += ""+employee.getCategoryDate()+";";
                            logCurr += ""+employee.getLeaveStatus()+";";
                            logCurr += ""+employee.getEmpPin()+";";
                            logCurr += ""+employee.getRace()+";";
                            logCurr += ""+employee.getAddressPermanent()+";";
                            logCurr += ""+employee.getPhoneEmergency()+";";
                            logCurr += ""+employee.getCompanyId()+";";
                            logCurr += ""+employee.getFather()+";";
                            logCurr += ""+employee.getMother()+";";
                            logCurr += ""+employee.getParentsAddress()+";";
                            logCurr += ""+employee.getNameEmg()+";";
                            logCurr += ""+employee.getPhoneEmg()+";";
                            logCurr += ""+employee.getAddressEmg()+";";
                            /*logCurr += ""+employee.getHodEmployeeId()+";";
                            logCurr += ""+employee.getAddrCountryId()+";";
                            logCurr += ""+employee.getAddrProvinceId()+";";
                            logCurr += ""+employee.getAddrRegencyId()+";";
                            logCurr += ""+employee.getAddrSubRegencyId()+";";
                            logCurr += ""+employee.getAddrPmntCountryId()+";";
                            logCurr += ""+employee.getAddrPmntProvinceId()+";";
                            logCurr += ""+employee.getAddrPmntRegencyId()+";";
                            logCurr += ""+employee.getAddrPmntSubRegencyId()+";";
                            logCurr += ""+employee.getIndentCardIssuedBy()+";";
                            logCurr += ""+employee.getIndentCardBirthDate()+";";*/
                            logCurr += ""+employee.getNoRekening()+";";
                            logCurr += ""+employee.getGradeLevelId()+";";
                            logCurr += ""+employee.getLocationId()+";";
                            logCurr += ""+employee.getEnd_contract()+";";
                            logCurr += ""+employee.getWorkassigncompanyId()+";";
                            logCurr += ""+employee.getWorkassigndivisionId()+";";
                            logCurr += ""+employee.getWorkassigndepartmentId()+";";
                            logCurr += ""+employee.getWorkassignsectionId()+";";
                            logCurr += ""+employee.getWorkassignpositionId()+";";
                            logCurr += ""+employee.getIdcardtype()+";";
                            logCurr += ""+employee.getNpwp()+";";
                            logCurr += ""+employee.getBpjs_no()+";";
                            logCurr += ""+employee.getBpjs_date()+";";
                            logCurr += ""+employee.getShio()+";";
                            logCurr += ""+employee.getElemen()+";";
                            logCurr += ""+employee.getIq()+";";
                            logCurr += ""+employee.getEq()+";";
                            logCurr += ""+employee.getProbationEndDate()+";";
                            logCurr += ""+employee.getStatusPensiunProgram()+";";
                            logCurr += ""+employee.getStartDatePensiun()+";";
                            logCurr += ""+employee.getPresenceCheckParameter()+";";
                            logCurr += ""+employee.getMedicalInfo()+";";
                            logCurr += ""+employee.getDanaPendidikan()+";";
                            logCurr += ""+employee.getPayrollGroup()+";";
                            logCurr += ""+employee.getProviderID()+";";
                            logCurr += ""+employee.getMemOfBpjsKesahatan()+";";
                            logCurr += ""+employee.getMemOfBpjsKetenagaKerjaan()+";";
                            /* data logCurr yg telah diinisalisasi kemudian dipakai untuk set ke logPrev, dan logCurr */
                            /* data struktur perusahaan didapat dari pengguna yang login melalui AppUser */
                            logSysHistory.setCompanyId(emp.getCompanyId());
                            logSysHistory.setDivisionId(emp.getDivisionId());
                            logSysHistory.setDepartmentId(emp.getDepartmentId());
                            logSysHistory.setSectionId(emp.getSectionId());
                            /* mencatat item yang diedit */
                            logSysHistory.setLogEditedUserId(employee.getOID());
                            /* setelah di set maka lakukan proses insert ke table logSysHistory */
                            PstLogSysHistory.insertExc(logSysHistory);
                        }
                             
                             String whereClause = PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + " = " + oid;   
                             Vector listcareerpath = PstCareerPath.listcheckcareer(0, 0, whereClause, null);
                                                        
                            if ((listcareerpath.size()==0) &&(this.employee.getEnd_contract()!=null)){
                                
                               
                                CareerPath careerPath= new CareerPath();
                                careerPath.setEmployeeId(oid);
                                careerPath.setCompanyId(this.employee.getCompanyId());
                                careerPath.setCompany(PstCareerPath.getCompany(String.valueOf(this.employee.getCompanyId()).toString()));
                                careerPath.setDepartmentId(this.employee.getDepartmentId());
                                careerPath.setDepartment(PstCareerPath.getDepartment(String.valueOf(this.employee.getDepartmentId()).toString()));
                                careerPath.setPositionId(this.employee.getPositionId());
                                careerPath.setPosition(PstCareerPath.getDepartment(String.valueOf(this.employee.getDepartmentId()).toString()));
                                careerPath.setSectionId(this.employee.getSectionId());
                                careerPath.setSection(PstCareerPath.getSection(String.valueOf(this.employee.getSectionId()).toString()));
                                careerPath.setWorkFrom(this.employee.getCommencingDate());
                                careerPath.setWorkTo(this.employee.getEnd_contract());
                                careerPath.setSalary(0);
                                careerPath.setDescription("First carrier");
                                careerPath.setEmpCategoryId(this.employee.getEmpCategoryId());
                                careerPath.setEmpCategory(PstCareerPath.getCategory(String.valueOf(this.employee.getEmpCategoryId()).toString()));
                              //  careerPath.set(this.employee.getCompanyId());
                                careerPath.setDivisionId(this.employee.getDivisionId());
                                careerPath.setDivision(PstCareerPath.getDivision(String.valueOf(this.employee.getDivisionId()).toString()));
                                careerPath.setLevelId(this.employee.getLevelId());
                                careerPath.setLevel(PstCareerPath.getLevel(String.valueOf(this.employee.getLevelId()).toString()));
                                careerPath.setLocationId(this.employee.getLocationId());
                                careerPath.setLocation(PstCareerPath.getLocation(String.valueOf(this.employee.getLocationId()).toString()));
                               //hilangkan sementara karena  metode masuk ke carier path untuk karyawan baru itu salah 20151212 
                                //  PstCareerPath.insertExc(careerPath);
                            }
                            
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_SAVED);
                            //Untuk Nikko karena database ada 2, jadi setiap ada perubahan di database yang satu, akan mengupdate data base yang lain
                            try {

                                String db_backup_url = PstSystemProperty.getValueByName("DB_BACKUP_URL");
                                String db_backup_usr = PstSystemProperty.getValueByName("DB_BACKUP_USR");
                                String db_backup_psd = PstSystemProperty.getValueByName("DB_BACKUP_PSWD");

                                /* Pengecekan kelengkapan konfigurasi di system property */
                                if (db_backup_url.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0
                                        && db_backup_usr.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0
                                        && db_backup_psd.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {

                                    try {
                                        Class.forName("com.mysql.jdbc.Driver");
                                        System.out.println("Driver Found");
                                    } catch (ClassNotFoundException e) {
                                        javax.swing.JOptionPane.showMessageDialog(null, "Driver Not Found " + e.toString());
                                    }

                                    Connection con = null;
                                    Statement stmt = null;
                                    try {

                                        con = DriverManager.getConnection(db_backup_url, db_backup_usr, db_backup_psd);

                                        String strBirthDate = "";
                                        if (this.employee.getBirthDate() != null) {
                                            try {
                                                strBirthDate = Formater.formatDate(this.employee.getBirthDate(), "yyyy-MM-dd");
                                            } catch (Exception E) {
                                                System.out.println("[exc] Parsing Commencing Date" + E.toString());
                                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                            }
                                        }

                                        String strEndContract = "";
                                        if (this.employee.getEnd_contract() != null) {
                                            try {
                                                strEndContract = Formater.formatDate(this.employee.getEnd_contract(), "yyyy-MM-dd");
                                            } catch (Exception E) {
                                                System.out.println("[exc] Parsing Commencing Date" + E.toString());
                                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                            }
                                        }
                                        
                                        String strCommencingDate = "";
                                        if (this.employee.getCommencingDate() != null) {
                                            try {
                                                strCommencingDate = Formater.formatDate(this.employee.getCommencingDate(), "yyyy-MM-dd");
                                            } catch (Exception E) {
                                                System.out.println("[exc] Parsing Commencing Date" + E.toString());
                                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                            }
                                        }

                                        String sql = "INSERT INTO " + PstEmployee.TBL_HR_EMPLOYEE
                                                + " (" + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_SEX] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_TAX_REG_NR] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RACE] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID] + ","//Gede_15Nov2011{
                                                + PstEmployee.fieldNames[PstEmployee.FLD_FATHER] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_MOTHER] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_PARENTS_ADDRESS] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_NAME_EMG] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMG] + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_EMG] + "," //}
                                                //Gede_27Nov2011{
                                                + PstEmployee.fieldNames[PstEmployee.FLD_HOD_EMPLOYEE_ID] +","//}
                                                //Ganki_27okt2014{
                                                + PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID] +","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT] +","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_COMPANY_ID] +","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DIVISION_ID] +","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DEPARTMENT_ID] +","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_SECTION_ID] +","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_POSITION_ID] +
                                                
                                                " ) VALUES ( "
                                                + oid + ","
                                                + this.employee.getCompanyId() + ","
                                                + this.employee.getDivisionId() + ","
                                                + this.employee.getDepartmentId() + ","
                                                + this.employee.getPositionId() + ","
                                                + this.employee.getSectionId() + ","
                                                + "'" + this.employee.getEmployeeNum() + "',"
                                                + this.employee.getEmpCategoryId() + ","
                                                + this.employee.getLevelId() + ","
                                                + "'" + this.employee.getFullName() + "',"
                                                + "'" + this.employee.getAddress() + "',"
                                                + this.employee.getSex() + ","
                                                + "'" + this.employee.getBirthPlace() + "',"
                                                + "'" + strBirthDate + "',"
                                                + this.employee.getReligionId() + ","
                                                + "'" + this.employee.getBloodType() + "',"
                                                + "'" + strCommencingDate + "',"
                                                + this.employee.getResigned() + ","
                                                + "'" + this.employee.getResignedDesc() + "',"
                                                + this.employee.getResignedReasonId() + ","
                                                + "'" + this.employee.getBarcodeNumber() + "',"
                                                + "'" + this.employee.getEmpPin() + "',"
                                                + "'" + this.employee.getTaxRegNr() + "',"
                                                + this.employee.getRace() + ","
                                                + +this.employee.getMaritalId() + ","//Gede_15Nov2011{
                                                + "'" + this.employee.getFather() + "',"
                                                + "'" + this.employee.getMother() + "',"
                                                + "'" + this.employee.getParentsAddress() + "'"
                                                + "'" + this.employee.getNameEmg() + "',"
                                                + "'" + this.employee.getPhoneEmg() + "',"
                                                + "'" + this.employee.getAddressEmg() + "',"//}
                                                //Gede_27Nov2011{
                                                + "'" + this.employee.getHodEmployeeId() + "',"//}
                                                //Ganki_27okt2014{
                                                + this.employee.getLocationId() + ","
                                                + "'" + strEndContract  + "',"
                                                + this.employee.getWorkassigncompanyId() +","
                                                + this.employee.getWorkassigndivisionId() +","
                                                + this.employee.getWorkassigndepartmentId() +","
                                                + this.employee.getWorkassignsectionId() +","
                                                + this.employee.getWorkassignpositionId() +"')";//}


                                        
                                        
                                        stmt = con.createStatement();
                                        stmt.executeUpdate(sql);

                                    } catch (Exception E) {
                                        System.out.println("[exception] INSERT INTO DATABASE BACKUP " + E.toString());
                                    } finally {
                                        try {
                                            stmt.close();
                                            con.close();
                                        } catch (Exception e) {
                                            System.out.println("EXCEPTION " + e.toString());
                                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                        }
                                    }
                                }

                            } catch (Exception E) {
                                System.out.println("EXCEPTION " + E.toString());
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                            /* Pengecekan untuk Database Finger Spot ( Exist or not )*/
                            if (!MachineFnSpot.equals("") && MachineFnSpot.equals("ok")) {

                                SessEmployee.insertUserInfo(this.employee.getBarcodeNumber(), this.employee.getFullName());

                            }

                        }

                        /**
                         * using in aiso to set employee to be contact
                         * PstContactList pstContactList = new PstContactList();
                         * if(employee.getIsAssignToAccounting()){ long
                         * oidContact =
                         * pstContactList.insertEmployeeToContact(this.employee);
                         * }else{ long oidContact =
                         * pstContactList.deleteEmployeeFromContact(oidEmployee);
                         * }
                         */
                        //on add new employee -- add also to leave stock
                        /*
                         if (oid != 0) {
                         LeaveStock stock = new LeaveStock();
                         stock.setEmployeeId(oid);
                         PstLeaveStock.insertExc(stock);
                         }
                         */
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

                        long oid = pstEmployee.updateExc(this.employee);
                        employee = pstEmployee.fetchExc(this.employee.getOID());
                        // logHistory
                        
                        if (sysLog != 0) {
                            employee = PstEmployee.fetchExc(this.employee.getOID());
                            /* Inisialisasi logField dengan menggambil field EmpEducation */
                            /* Tips: ambil data field dari persistent */
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PHONE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_SEX]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY]+";";
                            //logField += PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_CURIER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_NR]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_VALID_TO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_TAX_REG_NR]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_FOR_TAX]+";";
                            //logField += PstEmployee.fieldNames[PstEmployee.FLD_NATIONALITY_TYPE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMAIL_ADDRESS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_CATEGORY_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LEAVE_STATUS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_RACE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMERGENCY]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_FATHER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MOTHER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PARENTS_ADDRESS]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_NAME_EMG]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMG]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_EMG]+";";
                            /*logField += PstEmployee.fieldNames[PstEmployee.FLD_HOD_EMPLOYEE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_COUNTRY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PROVINCE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_REGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_SUBREGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_COUNTRY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_PROVINCE_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_REGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_SUBREGENCY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_ISSUED_BY]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_BIRTH_DATE]+";";*/
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_NO_REKENING]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_COMPANY_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DIVISION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DEPARTMENT_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_SECTION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_POSITION_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_TYPE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_NPWP]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BPJS_NO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_BPJS_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_SHIO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_ELEMEN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_IQ]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_EQ]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PROBATION_END_DATE]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_STATUS_PENSIUN_PROGRAM]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_START_DATE_PENSIUN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PRESENCE_CHECK_PARAMETER]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MEDICAL_INFO]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_DANA_PENDIDIKAN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_PROVIDER_ID]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MEMBER_OF_KESEHATAN]+";";
                            logField += PstEmployee.fieldNames[PstEmployee.FLD_MEMBER_OF_KETENAGAKERJAAN]+";";
                            
                            logPrev += ""+prevEmployee.getOID()+";";
                            logPrev += ""+prevEmployee.getDepartmentId()+";";
                            logPrev += ""+prevEmployee.getPositionId()+";";
                            logPrev += ""+prevEmployee.getSectionId()+";";
                            logPrev += ""+prevEmployee.getEmployeeNum()+";";
                            logPrev += ""+prevEmployee.getEmpCategoryId()+";";
                            logPrev += ""+prevEmployee.getLevelId()+";";
                            logPrev += ""+prevEmployee.getFullName()+";";
                            logPrev += ""+prevEmployee.getAddress()+";";
                            logPrev += ""+prevEmployee.getPhone()+";";
                            logPrev += ""+prevEmployee.getHandphone()+";";
                            logPrev += ""+prevEmployee.getPostalCode()+";";
                            logPrev += ""+prevEmployee.getSex()+";";
                            logPrev += ""+prevEmployee.getBirthPlace()+";";
                            logPrev += ""+prevEmployee.getBirthDate()+";";
                            logPrev += ""+prevEmployee.getReligionId()+";";
                            logPrev += ""+prevEmployee.getBloodType()+";";
                            logPrev += ""+prevEmployee.getAstekNum()+";";
                            logPrev += ""+prevEmployee.getAstekDate()+";";
                            logPrev += ""+prevEmployee.getMaritalId()+";";
                            logPrev += ""+prevEmployee.getTaxMaritalId()+";";
                            logPrev += ""+prevEmployee.getLockerId()+";";
                            logPrev += ""+prevEmployee.getCommencingDate()+";";
                            logPrev += ""+prevEmployee.getResigned()+";";
                            logPrev += ""+prevEmployee.getResignedDate()+";";
                            logPrev += ""+prevEmployee.getBarcodeNumber()+";";
                            logPrev += ""+prevEmployee.getResignedReasonId()+";";
                            logPrev += ""+prevEmployee.getResignedDesc()+";";
                            logPrev += ""+prevEmployee.getBasicSalary()+";";
                            //logPrev += ""+prevEmployee.getIsAssignToAccounting()+";";
                            logPrev += ""+prevEmployee.getDivisionId()+";";
                            logPrev += ""+prevEmployee.getCurier()+";";
                            logPrev += ""+prevEmployee.getIndentCardNr()+";";
                            logPrev += ""+prevEmployee.getIndentCardValidTo()+";";
                            logPrev += ""+prevEmployee.getTaxRegNr()+";";
                            logPrev += ""+prevEmployee.getAddressForTax()+";";
                            //logPrev += ""+prevEmployee.getNationalityType()+";";
                            logPrev += ""+prevEmployee.getEmailAddress()+";";
                            logPrev += ""+prevEmployee.getCategoryDate()+";";
                            logPrev += ""+prevEmployee.getLeaveStatus()+";";
                            logPrev += ""+prevEmployee.getEmpPin()+";";
                            logPrev += ""+prevEmployee.getRace()+";";
                            logPrev += ""+prevEmployee.getAddressPermanent()+";";
                            logPrev += ""+prevEmployee.getPhoneEmergency()+";";
                            logPrev += ""+prevEmployee.getCompanyId()+";";
                            logPrev += ""+prevEmployee.getFather()+";";
                            logPrev += ""+prevEmployee.getMother()+";";
                            logPrev += ""+prevEmployee.getParentsAddress()+";";
                            logPrev += ""+prevEmployee.getNameEmg()+";";
                            logPrev += ""+prevEmployee.getPhoneEmg()+";";
                            logPrev += ""+prevEmployee.getAddressEmg()+";";
                           /* logPrev += ""+prevEmployee.getHodEmployeeId()+";";
                            logPrev += ""+prevEmployee.getAddrCountryId()+";";
                            logPrev += ""+prevEmployee.getAddrProvinceId()+";";
                            logPrev += ""+prevEmployee.getAddrRegencyId()+";";
                            logPrev += ""+prevEmployee.getAddrSubRegencyId()+";";
                            logPrev += ""+prevEmployee.getAddrPmntCountryId()+";";
                            logPrev += ""+prevEmployee.getAddrPmntProvinceId()+";";
                            logPrev += ""+prevEmployee.getAddrPmntRegencyId()+";";
                            logPrev += ""+prevEmployee.getAddrPmntSubRegencyId()+";";
                            logPrev += ""+prevEmployee.getIndentCardIssuedBy()+";";
                            logPrev += ""+prevEmployee.getIndentCardBirthDate()+";";*/
                            logPrev += ""+prevEmployee.getNoRekening()+";";
                            logPrev += ""+prevEmployee.getGradeLevelId()+";";
                            logPrev += ""+prevEmployee.getLocationId()+";";
                            logPrev += ""+prevEmployee.getEnd_contract()+";";
                            logPrev += ""+prevEmployee.getWorkassigncompanyId()+";";
                            logPrev += ""+prevEmployee.getWorkassigndivisionId()+";";
                            logPrev += ""+prevEmployee.getWorkassigndepartmentId()+";";
                            logPrev += ""+prevEmployee.getWorkassignsectionId()+";";
                            logPrev += ""+prevEmployee.getWorkassignpositionId()+";";
                            logPrev += ""+prevEmployee.getIdcardtype()+";";
                            logPrev += ""+prevEmployee.getNpwp()+";";
                            logPrev += ""+prevEmployee.getBpjs_no()+";";
                            logPrev += ""+prevEmployee.getBpjs_date()+";";
                            logPrev += ""+prevEmployee.getShio()+";";
                            logPrev += ""+prevEmployee.getElemen()+";";
                            logPrev += ""+prevEmployee.getIq()+";";
                            logPrev += ""+prevEmployee.getEq()+";";
                            logPrev += ""+prevEmployee.getProbationEndDate()+";";
                            logPrev += ""+prevEmployee.getStatusPensiunProgram()+";";
                            logPrev += ""+prevEmployee.getStartDatePensiun()+";";
                            logPrev += ""+prevEmployee.getPresenceCheckParameter()+";";
                            logPrev += ""+prevEmployee.getMedicalInfo()+";";
                            logPrev += ""+prevEmployee.getDanaPendidikan()+";";
                            logPrev += ""+prevEmployee.getPayrollGroup()+";";
                            logPrev += ""+prevEmployee.getProviderID()+";";
                            logPrev += ""+prevEmployee.getMemOfBpjsKesahatan()+";";
                            logPrev += ""+prevEmployee.getMemOfBpjsKetenagaKerjaan()+";";
                            
                            logCurr += ""+employee.getOID()+";";
                            logCurr += ""+employee.getDepartmentId()+";";
                            logCurr += ""+employee.getPositionId()+";";
                            logCurr += ""+employee.getSectionId()+";";
                            logCurr += ""+employee.getEmployeeNum()+";";
                            logCurr += ""+employee.getEmpCategoryId()+";";
                            logCurr += ""+employee.getLevelId()+";";
                            logCurr += ""+employee.getFullName()+";";
                            logCurr += ""+employee.getAddress()+";";
                            logCurr += ""+employee.getPhone()+";";
                            logCurr += ""+employee.getHandphone()+";";
                            logCurr += ""+employee.getPostalCode()+";";
                            logCurr += ""+employee.getSex()+";";
                            logCurr += ""+employee.getBirthPlace()+";";
                            logCurr += ""+employee.getBirthDate()+";";
                            logCurr += ""+employee.getReligionId()+";";
                            logCurr += ""+employee.getBloodType()+";";
                            logCurr += ""+employee.getAstekNum()+";";
                            logCurr += ""+employee.getAstekDate()+";";
                            logCurr += ""+employee.getMaritalId()+";";
                            logCurr += ""+employee.getTaxMaritalId()+";";
                            logCurr += ""+employee.getLockerId()+";";
                            logCurr += ""+employee.getCommencingDate()+";";
                            logCurr += ""+employee.getResigned()+";";
                            logCurr += ""+employee.getResignedDate()+";";
                            logCurr += ""+employee.getBarcodeNumber()+";";
                            logCurr += ""+employee.getResignedReasonId()+";";
                            logCurr += ""+employee.getResignedDesc()+";";
                            logCurr += ""+employee.getBasicSalary()+";";
                            //logCurr += ""+employee.getIsAssignToAccounting()+";";
                            logCurr += ""+employee.getDivisionId()+";";
                            logCurr += ""+employee.getCurier()+";";
                            logCurr += ""+employee.getIndentCardNr()+";";
                            logCurr += ""+employee.getIndentCardValidTo()+";";
                            logCurr += ""+employee.getTaxRegNr()+";";
                            logCurr += ""+employee.getAddressForTax()+";";
                            //logCurr += ""+employee.getNationalityType()+";";
                            logCurr += ""+employee.getEmailAddress()+";";
                            logCurr += ""+employee.getCategoryDate()+";";
                            logCurr += ""+employee.getLeaveStatus()+";";
                            logCurr += ""+employee.getEmpPin()+";";
                            logCurr += ""+employee.getRace()+";";
                            logCurr += ""+employee.getAddressPermanent()+";";
                            logCurr += ""+employee.getPhoneEmergency()+";";
                            logCurr += ""+employee.getCompanyId()+";";
                            logCurr += ""+employee.getFather()+";";
                            logCurr += ""+employee.getMother()+";";
                            logCurr += ""+employee.getParentsAddress()+";";
                            logCurr += ""+employee.getNameEmg()+";";
                            logCurr += ""+employee.getPhoneEmg()+";";
                            logCurr += ""+employee.getAddressEmg()+";";
                            /*logCurr += ""+employee.getHodEmployeeId()+";";
                            logCurr += ""+employee.getAddrCountryId()+";";
                            logCurr += ""+employee.getAddrProvinceId()+";";
                            logCurr += ""+employee.getAddrRegencyId()+";";
                            logCurr += ""+employee.getAddrSubRegencyId()+";";
                            logCurr += ""+employee.getAddrPmntCountryId()+";";
                            logCurr += ""+employee.getAddrPmntProvinceId()+";";
                            logCurr += ""+employee.getAddrPmntRegencyId()+";";
                            logCurr += ""+employee.getAddrPmntSubRegencyId()+";";
                            logCurr += ""+employee.getIndentCardIssuedBy()+";";
                            logCurr += ""+employee.getIndentCardBirthDate()+";";*/
                            logCurr += ""+employee.getNoRekening()+";";
                            logCurr += ""+employee.getGradeLevelId()+";";
                            logCurr += ""+employee.getLocationId()+";";
                            logCurr += ""+employee.getEnd_contract()+";";
                            logCurr += ""+employee.getWorkassigncompanyId()+";";
                            logCurr += ""+employee.getWorkassigndivisionId()+";";
                            logCurr += ""+employee.getWorkassigndepartmentId()+";";
                            logCurr += ""+employee.getWorkassignsectionId()+";";
                            logCurr += ""+employee.getWorkassignpositionId()+";";
                            logCurr += ""+employee.getIdcardtype()+";";
                            logCurr += ""+employee.getNpwp()+";";
                            logCurr += ""+employee.getBpjs_no()+";";
                            logCurr += ""+employee.getBpjs_date()+";";
                            logCurr += ""+employee.getShio()+";";
                            logCurr += ""+employee.getElemen()+";";
                            logCurr += ""+employee.getIq()+";";
                            logCurr += ""+employee.getEq()+";";
                            logCurr += ""+employee.getProbationEndDate()+";";
                            logCurr += ""+employee.getStatusPensiunProgram()+";";
                            logCurr += ""+employee.getStartDatePensiun()+";";
                            logCurr += ""+employee.getPresenceCheckParameter()+";";
                            logCurr += ""+employee.getMedicalInfo()+";";
                            logCurr += ""+employee.getDanaPendidikan()+";";
                            logCurr += ""+employee.getPayrollGroup()+";";
                            logCurr += ""+employee.getProviderID()+";";
                            logCurr += ""+employee.getMemOfBpjsKesahatan()+";";
                            logCurr += ""+employee.getMemOfBpjsKetenagaKerjaan()+";";

                            String className = employee.getClass().getName();

                            LogSysHistory logSysHistory = new LogSysHistory();

                            String reqUrl = request.getRequestURI().toString() + "?employee_oid=" + oidEmployee;

                            logSysHistory.setLogDocumentId(0);
                            logSysHistory.setLogUserId(userId);
                            logSysHistory.setApproverId(userId);
                            logSysHistory.setApproveDate(nowDate);
                            logSysHistory.setLogLoginName(loginName);
                            logSysHistory.setLogDocumentNumber("");
                            logSysHistory.setLogDocumentType(className); //entity
                            logSysHistory.setLogUserAction("EDIT"); // command
                            logSysHistory.setLogOpenUrl(reqUrl); // locate jsp
                            logSysHistory.setLogUpdateDate(nowDate);
                            logSysHistory.setLogApplication(I_LogHistory.SYSTEM_NAME[I_LogHistory.SYSTEM_HAIRISMA]); // interface
                            logSysHistory.setLogDetail(logField); // apa yang dirubah
                            logSysHistory.setLogStatus(0);
                            logSysHistory.setLogPrev(logPrev);
                            logSysHistory.setLogCurr(logCurr);
                            logSysHistory.setLogModule("Data Pribadi");
                            logSysHistory.setCompanyId(emp.getCompanyId());
                            logSysHistory.setDivisionId(emp.getDivisionId());
                            logSysHistory.setDepartmentId(emp.getDepartmentId());
                            logSysHistory.setSectionId(emp.getSectionId());
                            logSysHistory.setLogEditedUserId(employee.getOID());

                            PstLogSysHistory.insertExc(logSysHistory);

                        }

                        /* Bila Proses yang dilakukan adalah edit */
                        if (oid != 0) {
                            
                            
                            String whereClause = PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + " = " + oid;   
                             Vector listcareerpath = PstCareerPath.listcheckcareer(0, 0, whereClause, null);
                                                        
                            if ((listcareerpath.size()==0) &&(this.employee.getEnd_contract()!=null)){
                                
                               
                                CareerPath careerPath= new CareerPath();
                                careerPath.setEmployeeId(oid);
                                careerPath.setCompanyId(this.employee.getCompanyId());
                                careerPath.setCompany(PstCareerPath.getCompany(String.valueOf(this.employee.getCompanyId()).toString()));
                                careerPath.setDepartmentId(this.employee.getDepartmentId());
                                careerPath.setDepartment(PstCareerPath.getDepartment(String.valueOf(this.employee.getDepartmentId()).toString()));
                                careerPath.setPositionId(this.employee.getPositionId());
                                careerPath.setPosition(PstCareerPath.getDepartment(String.valueOf(this.employee.getDepartmentId()).toString()));
                                careerPath.setSectionId(this.employee.getSectionId());
                                careerPath.setSection(PstCareerPath.getSection(String.valueOf(this.employee.getSectionId()).toString()));
                                careerPath.setWorkFrom(this.employee.getCommencingDate());
                                careerPath.setWorkTo(this.employee.getEnd_contract());
                                careerPath.setSalary(0);
                                careerPath.setDescription("First carrier");
                                careerPath.setEmpCategoryId(this.employee.getEmpCategoryId());
                                careerPath.setEmpCategory(PstCareerPath.getCategory(String.valueOf(this.employee.getEmpCategoryId()).toString()));
                              //  careerPath.set(this.employee.getCompanyId());
                                careerPath.setDivisionId(this.employee.getDivisionId());
                                careerPath.setDivision(PstCareerPath.getDivision(String.valueOf(this.employee.getDivisionId()).toString()));
                                careerPath.setLevelId(this.employee.getLevelId());
                                careerPath.setLevel(PstCareerPath.getLevel(String.valueOf(this.employee.getLevelId()).toString()));
                                careerPath.setLocationId(this.employee.getLocationId());
                                careerPath.setLocation(PstCareerPath.getLocation(String.valueOf(this.employee.getLocationId()).toString()));
                                //salah metode karena kariawan baru seharusnya belum masuk ke carrier path 20151212
                                //PstCareerPath.insertExc(careerPath);
                            }
                            
                            
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_UPDATED);
                            try {

                                String db_backup_url = PstSystemProperty.getValueByName("DB_BACKUP_URL");
                                String db_backup_usr = PstSystemProperty.getValueByName("DB_BACKUP_USR");
                                String db_backup_psd = PstSystemProperty.getValueByName("DB_BACKUP_PSWD");

                                /* Pengecekan kelengkapan konfigurasi di system property */
                                if (db_backup_url.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0
                                        && db_backup_usr.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0
                                        && db_backup_psd.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {

                                    try {
                                        Class.forName("com.mysql.jdbc.Driver");
                                        System.out.println("Driver Found");
                                    } catch (ClassNotFoundException e) {
                                        javax.swing.JOptionPane.showMessageDialog(null, "Driver Not Found " + e.toString());
                                    }

                                    Connection con = null;
                                    Statement stmt = null;
                                    try {

                                        con = DriverManager.getConnection(db_backup_url, db_backup_usr, db_backup_psd);

                                        String strBirthDate = "";
                                        if (this.employee.getBirthDate() != null) {
                                            try {
                                                strBirthDate = Formater.formatDate(this.employee.getBirthDate(), "yyyy-MM-dd");
                                            } catch (Exception E) {
                                                System.out.println("[exc] Parsing Commencing Date" + E.toString());
                                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                            }
                                        }
                                        
                                        String strEndContract = "";
                                        if (this.employee.getEnd_contract() != null) {
                                            try {
                                                strEndContract = Formater.formatDate(this.employee.getEnd_contract(), "yyyy-MM-dd");
                                            } catch (Exception E) {
                                                System.out.println("[exc] Parsing Commencing Date" + E.toString());
                                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                            }
                                        }
                                        

                                        String strCommencingDate = "";
                                        if (this.employee.getCommencingDate() != null) {
                                            try {
                                                strCommencingDate = Formater.formatDate(this.employee.getCommencingDate(), "yyyy-MM-dd");
                                            } catch (Exception E) {
                                                System.out.println("[exc] Parsing Commencing Date" + E.toString());
                                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                            }
                                        }

                                        String sql = "UPDATE " + PstEmployee.TBL_HR_EMPLOYEE + " SET "
                                                + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + this.employee.getCompanyId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + this.employee.getDivisionId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + this.employee.getDepartmentId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = " + this.employee.getPositionId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + this.employee.getSectionId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = '" + this.employee.getEmployeeNum() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = " + this.employee.getEmpCategoryId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = " + this.employee.getLevelId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " = '" + this.employee.getFullName() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS] + " = '" + this.employee.getAddress() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_SEX] + " = " + this.employee.getSex() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE] + "= '" + this.employee.getBirthPlace() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " = '" + strBirthDate + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID] + " = " + this.employee.getReligionId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE] + " = '" + this.employee.getBloodType() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " = '" + strCommencingDate + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + this.employee.getResigned() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC] + " = '" + this.employee.getResignedDesc() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID] + " = " + this.employee.getResignedReasonId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_TAX_REG_NR] + " = '" + this.employee.getTaxRegNr() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = '" + this.employee.getBarcodeNumber() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN] + " = '" + this.employee.getEmpPin() + "' " //Gede_15Nov2011{
                                                + PstEmployee.fieldNames[PstEmployee.FLD_FATHER] + " = '" + this.employee.getFather() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_MOTHER] + " = '" + this.employee.getMother() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_PARENTS_ADDRESS] + " = '" + this.employee.getParentsAddress() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_NAME_EMG] + " = '" + this.employee.getNameEmg() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMG] + " = '" + this.employee.getPhoneEmg() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_EMG] + " = '" + this.employee.getAddressEmg() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_HOD_EMPLOYEE_ID] + " = " + this.employee.getHodEmployeeId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID] + " = " + this.employee.getLocationId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT] + " = '" + this.employee.getEnd_contract() + "',"
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_COMPANY_ID] + " = " + this.employee.getWorkassigncompanyId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DIVISION_ID] + " = " + this.employee.getWorkassigndivisionId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DEPARTMENT_ID] + " = " + this.employee.getWorkassigndepartmentId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_SECTION_ID] + " = " + this.employee.getWorkassignsectionId() + ","
                                                + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_POSITION_ID] + " = " + this.employee.getWorkassignpositionId() + ","
                                                
                                                + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + oid;

                                        stmt = con.createStatement();
                                        stmt.executeUpdate(sql);

                                    } catch (Exception E) {
                                        System.out.println("[exception] UPDATE INTO DATABASE BACKUP " + E.toString());
                                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                    } finally {
                                        try {
                                            stmt.close();
                                            con.close();
                                        } catch (Exception e) {
                                            System.out.println("EXCEPTION " + e.toString());
                                            msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                                        }
                                    }
                                }

                            } catch (Exception E) {
                                System.out.println("EXCEPTION " + E.toString());
                                msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                            }

                        }


                        /* Pengecekan bila ada perubahan barcode number, karena akan mengupdate database finger spot */
                        boolean updateBarcode = true;
                        boolean upMachine = false;

                        /* Penegcekan untuk mesin, apakah ada database mesin atau tidak */
                        //update by satrya 2012-11-09
                        if (employee.getBarcodeNumber() != null) {
                            if (!tmpBarcodeNumber.equals(employee.getBarcodeNumber()) && oid != 0) {

                                if (!MachineFnSpot.equals("") && MachineFnSpot.equals("ok")) {

                                    updateBarcode = SessEmployee.updateBarcodeAtt2010(tmpBarcodeNumber, employee.getBarcodeNumber(), employee.getFullName());
                                    upMachine = true;

                                }
                            }
                        }

                        if (!tmpFullName.equals(employee.getFullName()) && oid != 0 && updateBarcode == true) {

                            if (!MachineFnSpot.equals("") && MachineFnSpot.equals("ok")) {

                                SessEmployee.updateFullNameAtt2010(employee.getBarcodeNumber(), employee.getFullName());
                                upMachine = true;

                            }

                        }

                        if (upMachine) {
                            msgString = FRMMessage.getMsg(FRMMessage.MSG_UPDATE_DB_MACHINE);
                            return RSLT_FORM_INCOMPLETE;
                        }

                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.EDIT:
                if (oidEmployee != 0) {
                    try {
                        employee = PstEmployee.fetchExc(oidEmployee);
                        if (employee.getLockerId() != 0) {
                            locker = PstLocker.fetchExc(employee.getLockerId());
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.ASK:
                if (oidEmployee != 0) {
                    try {
                        employee = PstEmployee.fetchExc(oidEmployee);
                        if (employee.getLockerId() != 0) {
                            locker = PstLocker.fetchExc(employee.getLockerId());
                        }
                    } catch (DBException dbexc) {
                        excCode = dbexc.getErrorCode();
                        msgString = getSystemMessage(excCode);
                    } catch (Exception exc) {
                        msgString = getSystemMessage(I_DBExceptionInfo.UNKNOWN);
                    }
                }
                break;

            case Command.DELETE:
                System.out.println("oidEmployee " + oidEmployee);
                if (oidEmployee != 0) {
                    try {

                        Employee objEmployee = new Employee();

                        try {
                            objEmployee = PstEmployee.fetchExc(oidEmployee);
                        } catch (Exception e) {
                            System.out.println("Exception " + e.toString());
                        }

                        long oid = PstEmpLanguage.deleteByEmployee(oidEmployee);
                        oid = PstFamilyMember.deleteByEmployee(oidEmployee);
                        oid = PstExperience.deleteByEmployee(oidEmployee);

                        oid = PstCareerPath.deleteByEmployee(oidEmployee);
                        oid = PstEmpSalary.deleteByEmployee(oidEmployee);
                        oid = PstEmpSchedule.deleteByEmployee(oidEmployee);
                        oid = PstLeave.deleteByEmployee(oidEmployee);
                        oid = PstDayOfPayment.deleteByEmployee(oidEmployee);
                        oid = PstPresence.deleteByEmployee(oidEmployee);
                        oid = PstLocker.deleteByEmployee(oidEmployee);
                        oid = PstEmployee.deleteExc(oidEmployee);
                        
                        // logHistory
                        
                        if (sysLog != 0) {

                            String className = employee.getClass().getName();

                            LogSysHistory logSysHistory = new LogSysHistory();

                            String reqUrl = request.getRequestURI().toString() + "?employee_oid=" + oidEmployee;

                            logSysHistory.setLogDocumentId(0);
                            logSysHistory.setLogUserId(userId);
                            logSysHistory.setApproverId(userId);
                            logSysHistory.setLogLoginName(loginName);
                            logSysHistory.setLogDocumentNumber("");
                            logSysHistory.setLogDocumentType(className); //entity
                            logSysHistory.setLogUserAction("DELETE"); // command
                            logSysHistory.setLogOpenUrl(reqUrl); // locate jsp
                            logSysHistory.setLogUpdateDate(nowDate);
                            logSysHistory.setApproveDate(nowDate);
                            logSysHistory.setLogApplication(I_LogHistory.SYSTEM_NAME[I_LogHistory.SYSTEM_HAIRISMA]); // interface
                            logSysHistory.setLogDetail(logField); // apa yang dirubah
                            logSysHistory.setLogPrev(logPrev);
                            logSysHistory.setLogCurr(logPrev);
                            logSysHistory.setLogStatus(0);
                            logSysHistory.setLogModule("Data Pribadi");
                            logSysHistory.setLogEditedUserId(employee.getOID());
                            logSysHistory.setCompanyId(emp.getCompanyId());
                            logSysHistory.setDivisionId(emp.getDivisionId());
                            logSysHistory.setDepartmentId(emp.getDepartmentId());
                            logSysHistory.setSectionId(emp.getSectionId());

                            PstLogSysHistory.insertExc(logSysHistory);

                        }


                        /*Untuk penghapusan data di database backup untuk kasus NIKKO*/
                        try {

                            String db_backup_url = PstSystemProperty.getValueByName("DB_BACKUP_URL");
                            String db_backup_usr = PstSystemProperty.getValueByName("DB_BACKUP_USR");
                            String db_backup_psd = PstSystemProperty.getValueByName("DB_BACKUP_PSWD");

                            if (db_backup_url.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0
                                    && db_backup_usr.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0
                                    && db_backup_psd.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {

                                try {
                                    Class.forName("com.mysql.jdbc.Driver");
                                    System.out.println("Driver Found");
                                } catch (ClassNotFoundException e) {
                                    javax.swing.JOptionPane.showMessageDialog(null, "Driver Not Found " + e.toString());
                                }

                                Connection con = null;
                                Statement stmt = null;
                                try {

                                    con = DriverManager.getConnection(db_backup_url, db_backup_usr, db_backup_psd);

                                    String sql = "DELETE FROM " + PstEmployee.TBL_HR_EMPLOYEE + " WHERE "
                                            + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + oidEmployee;

                                    stmt = con.createStatement();
                                    stmt.executeUpdate(sql);

                                } catch (Exception E) {
                                    System.out.println("[exception] UPDATE INTO DATABASE BACKUP " + E.toString());
                                } finally {
                                    try {
                                        stmt.close();
                                        con.close();
                                    } catch (Exception e) {
                                        System.out.println("EXCEPTION " + e.toString());
                                    }
                                }
                            }

                        } catch (Exception E) {

                            System.out.println("[exception] " + E.toString());

                        }

                        /* Penghapusan untuk data pada database mesin finger spot*/

                        if (objEmployee.getOID() != 0) {
                            if (!MachineFnSpot.equals("") && MachineFnSpot.equals("ok")) {
                                SessEmployee.delDbFingerSpot(objEmployee.getBarcodeNumber());
                            }
                        }

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
