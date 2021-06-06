
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 * *****************************************************************
 */
package com.dimata.harisma.entity.employee;

/* package java */
import com.dimata.harisma.entity.admin.PstAppUser;
import com.dimata.harisma.entity.attendance.I_Atendance;
import com.dimata.harisma.entity.attendance.PstEmpSchedule;
import com.dimata.harisma.entity.attendance.employeeoutlet.PstEmployeeOutlet;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;

//Gede_2April2012{
import com.dimata.harisma.entity.overtime.*;
//}

/* package harisma */
//import com.dimata. harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.locker.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.masterdata.sessemployee.EmployeeMinimalis;
import com.dimata.harisma.entity.masterdata.sessemployee.SessEmployee;
import com.dimata.harisma.entity.payroll.PayBanks;
import com.dimata.harisma.entity.payroll.PayComponent;
import com.dimata.harisma.entity.payroll.PayComponenttemp;
import com.dimata.harisma.entity.payroll.PayEmpLevel;
import com.dimata.harisma.entity.payroll.PayPeriod;
import com.dimata.harisma.entity.payroll.PstPayBanks;
import com.dimata.harisma.entity.payroll.PstPayComponent;
import com.dimata.harisma.entity.payroll.PstPayEmpLevel;
import com.dimata.harisma.entity.payroll.PstPayGeneral;
import com.dimata.harisma.entity.payroll.PstPayPeriod;
import com.dimata.harisma.entity.payroll.PstPaySlip;
import com.dimata.harisma.entity.payroll.PstPaySlipComp;
import com.dimata.harisma.report.payroll.ListBenefitDeduction;
import com.dimata.harisma.session.aplikasidesktop.attendance.SessDestopApplication;
import com.dimata.system.entity.PstSystemProperty;

public class PstEmployee extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_HR_EMPLOYEE = "hr_employee";//"HR_EMPLOYEE";
    public static final int FLD_EMPLOYEE_ID = 0;
    public static final int FLD_DEPARTMENT_ID = 1;
    public static final int FLD_POSITION_ID = 2;
    public static final int FLD_SECTION_ID = 3;
    public static final int FLD_EMPLOYEE_NUM = 4;
    public static final int FLD_EMP_CATEGORY_ID = 5;
    public static final int FLD_LEVEL_ID = 6;
    public static final int FLD_FULL_NAME = 7;
    public static final int FLD_ADDRESS = 8;
    public static final int FLD_PHONE = 9;
    public static final int FLD_HANDPHONE = 10;
    public static final int FLD_POSTAL_CODE = 11;
    public static final int FLD_SEX = 12;
    public static final int FLD_BIRTH_PLACE = 13;
    public static final int FLD_BIRTH_DATE = 14;
    public static final int FLD_RELIGION_ID = 15;
    public static final int FLD_BLOOD_TYPE = 16;
    public static final int FLD_ASTEK_NUM = 17;
    public static final int FLD_ASTEK_DATE = 18;
    public static final int FLD_MARITAL_ID = 19;
    public static final int FLD_LOCKER_ID = 20;
    public static final int FLD_COMMENCING_DATE = 21;
    public static final int FLD_RESIGNED = 22;
    public static final int FLD_RESIGNED_DATE = 23;
    public static final int FLD_BARCODE_NUMBER = 24;
    public static final int FLD_RESIGNED_REASON_ID = 25;
    public static final int FLD_RESIGNED_DESC = 26;
    public static final int FLD_BASIC_SALARY = 27;
    public static final int FLD_ASSIGN_TO_ACCOUNTING = 28;
    public static final int FLD_DIVISION_ID = 29;
    /*kususus untuk keperluan intimas
     *updated by Yunny
     */
    public static final int FLD_CURIER = 30;
    /*penambahan kolom sehubungan dengan payroll
     *Updated By Yunny
     */
    public static final int FLD_INDENT_CARD_NR = 31;
    public static final int FLD_INDENT_CARD_VALID_TO = 32;
    public static final int FLD_TAX_REG_NR = 33;
    public static final int FLD_ADDRESS_FOR_TAX = 34;
    public static final int FLD_NATIONALITY_TYPE = 35;
    public static final int FLD_EMAIL_ADDRESS = 36;
    public static final int FLD_CATEGORY_DATE = 37;
    public static final int FLD_LEAVE_STATUS = 38;
    public static final int FLD_EMP_PIN = 39;
    public static final int FLD_RACE = 40;
    //add by artha
    public static final int FLD_ADDRESS_PERMANENT = 41;
    public static final int FLD_PHONE_EMERGENCY = 42;
    public static final int FLD_COMPANY_ID = 43;//Gede_15N0v2011{
    public static final int FLD_FATHER = 44;
    public static final int FLD_MOTHER = 45;
    public static final int FLD_PARENTS_ADDRESS = 46;
    public static final int FLD_NAME_EMG = 47;
    public static final int FLD_PHONE_EMG = 48;
    public static final int FLD_ADDRESS_EMG = 49;//}
    //Gede_27Nov2011{
    public static final int FLD_HOD_EMPLOYEE_ID = 50;//
    //add by Kartika 1 Dec 2011
    public static final int FLD_ADDR_COUNTRY_ID = 51;//
    public static final int FLD_ADDR_PROVINCE_ID = 52;//
    public static final int FLD_ADDR_REGENCY_ID = 53;//
    public static final int FLD_ADDR_SUBREGENCY_ID = 54;//
    public static final int FLD_ADDR_PMNT_COUNTRY_ID = 55;//
    public static final int FLD_ADDR_PMNT_PROVINCE_ID = 56;//
    public static final int FLD_ADDR_PMNT_REGENCY_ID = 57;//
    public static final int FLD_ADDR_PMNT_SUBREGENCY_ID = 58;
    public static final int FLD_ID_CARD_ISSUED_BY = 59;
    public static final int FLD_ID_CARD_BIRTH_DATE = 60;
    public static final int FLD_TAX_MARITAL_ID = 61; //Kartika; 2012-05-08
    //update by satrya 2012-11-14
    public static final int FLD_EDUCATION_ID = 62;
    public static final int FLD_NO_REKENING = 63;
    public static final int FLD_GRADE_LEVEL_ID = 64;
    //update by satrya 2014-07-03
    public static final int FLD_COUNT_IDX = 65;
    //update by priska 2014-10-27
    public static final int FLD_LOCATION_ID = 66;
    public static final int FLD_END_CONTRACT = 67;
    public static final int FLD_WORK_ASSIGN_COMPANY_ID = 68;
    public static final int FLD_WORK_ASSIGN_DIVISION_ID = 69;
    public static final int FLD_WORK_ASSIGN_DEPARTMENT_ID = 70;
    public static final int FLD_WORK_ASSIGN_SECTION_ID = 71;
    public static final int FLD_WORK_ASSIGN_POSITION_ID = 72;
    public static final int FLD_ID_CARD_TYPE = 73;
    public static final int FLD_NPWP = 74;
    public static final int FLD_BPJS_NO = 75;
    public static final int FLD_BPJS_DATE = 76;
    public static final int FLD_SHIO = 77;
    public static final int FLD_ELEMEN = 78;
    public static final int FLD_IQ = 79;
    public static final int FLD_EQ = 80;
    // Add Field by Hendra McHen 2015-01-09
    public static final int FLD_PROBATION_END_DATE = 81;
    public static final int FLD_STATUS_PENSIUN_PROGRAM = 82;
    public static final int FLD_START_DATE_PENSIUN = 83;
    public static final int FLD_PRESENCE_CHECK_PARAMETER = 84;
    public static final int FLD_MEDICAL_INFO = 85;
    /* Add Field by Hendra McHen | 2015-04-24 */
    public static final int FLD_DANA_PENDIDIKAN = 86;
    //priska 20150731
    public static final int FLD_PAYROLL_GROUP = 87;
    //kartika 2015-09-16
    public static final int FLD_PROVIDER_ID = 88;
    // untuk karyawan yg outsource
    //priska 2015-11-04
    public static final int FLD_MEMBER_OF_KESEHATAN = 89;
    public static final int FLD_MEMBER_OF_KETENAGAKERJAAN = 90;
    public static final int FLD_ADDR_DISTRICT_ID = 91;
    public static final int FLD_ADDR_PMNT_DISTRICT_ID = 92;
    public static final int FLD_NO_KK = 93;
    //added by dewok 20190610
    public static final int FLD_NAMA_PEMILIK_REKENING = 94;
    public static final int FLD_CABANG_BANK = 95;
    public static final int FLD_KODE_BANK = 96;
    public static final int FLD_TANGGAL_PAJAK_TERDAFTAR = 97;
    public static final int FLD_NAMA_BANK = 98;
    public static final int FLD_ADDRESS_ID_CARD = 99;

    public static final String[] fieldNames = {
        "EMPLOYEE_ID",
        "DEPARTMENT_ID",
        "POSITION_ID",
        "SECTION_ID",
        "EMPLOYEE_NUM",
        "EMP_CATEGORY_ID",
        "LEVEL_ID",
        "FULL_NAME",
        "ADDRESS",
        "PHONE",
        "HANDPHONE",
        "POSTAL_CODE",
        "SEX",
        "BIRTH_PLACE",
        "BIRTH_DATE",
        "RELIGION_ID",
        "BLOOD_TYPE",
        "ASTEK_NUM",
        "ASTEK_DATE",
        "MARITAL_ID",
        "LOCKER_ID",
        "COMMENCING_DATE",
        "RESIGNED",
        "RESIGNED_DATE",
        "BARCODE_NUMBER",
        "RESIGNED_REASON_ID",
        "RESIGNED_DESC",
        "BASIC_SALARY",
        "IS_ASSIGN_TO_ACCOUNTING",
        "DIVISION_ID",
        "CURIER",
        "INDENT_CARD_NR",
        "INDENT_CARD_VALID_TO",
        "TAX_REG_NR",
        "ADDRESS_FOR_TAX",
        "NATIONALITY_TYPE",
        "EMAIL_ADDRESS",
        "CATEGORY_DATE",
        "LEAVE_STATUS",
        "EMP_PIN",
        "RACE",
        "ADDRESS_PERMANENT",
        "PHONE_EMERGENCY",
        "COMPANY_ID", //Gede_15Nov2011{
        "FATHER",
        "MOTHER",
        "PARENTS_ADDRESS",
        "NAME_EMG",
        "PHONE_EMG",
        "ADDRESS_EMG",//}
        //Gede_15Nov2011{
        "HOD_EMPLOYEE_ID",
        //add by Kartika 1 Dec 2011
        "ADDR_COUNTRY_ID",
        "ADDR_PROVINCE_ID",
        "ADDR_REGENCY_ID",
        "ADDR_SUBREGENCY_ID",
        "ADDR_PMNT_COUNTRY_ID",
        "ADDR_PMNT_PROVINCE_ID",
        "ADDR_PMNT_REGENCY_ID",
        "ADDR_PMNT_SUBREGENCY_ID",
        "ID_CARD_ISSUED_BY",
        "ID_CARD_BIRTH_DATE",
        "TAX_MARITAL_ID", //Kartika; 2012-05-08
        "EDUCATION_ID",//update by satrya 2012-11-14
        "NO_REKENING",
        "GRADE_LEVEL_ID",//update by satrya 2014-06-13
        //update by satrya 2014-07-03
        "COUNT_IDX",
        //update by ganki priska 2014-10-27
        "LOCATION_ID",
        "END_CONTRACT",
        "WORK_ASSIGN_COMPANY_ID",
        "WORK_ASSIGN_DIVISION_ID",
        "WORK_ASSIGN_DEPARTMENT_ID",
        "WORK_ASSIGN_SECTION_ID",
        "WORK_ASSIGN_POSITION_ID",
        "ID_CARD_TYPE",
        "NPWP",
        "BPJS_NO",
        "BPJS_DATE",
        "SHIO",
        "ELEMEN",
        "IQ",
        "EQ",
        // Add Field by Hendra McHen 2015-01-09
        "PROBATION_END_DATE",
        "STATUS_PENSIUN_PROGRAM",
        "START_DATE_PENSIUN",
        "PRESENCE_CHECK_PARAMETER",
        "MEDICAL_INFO",
        /*Add Field by Hendra McHen 2015-04-24*/
        "DANA_PENDIDIKAN",
        "PAYROLL_GROUP", //priska 20150730
        "PROVIDER_ID", // kartika 2015-09-16
        "MEMBER_OF_KESEHATAN",
        "MEMBER_OF_KETENAGAKERJAAN",
        "ADDR_DISTRICT_ID",
        "ADDR_PMNT_DISTRICT_ID",
        "NO_KK",
        //added by dewok 20190610
        "NAMA_PEMILIK_REKENING",
        "CABANG_BANK",
        "KODE_BANK",
        "TANGGAL_PAJAK_TERDAFTAR",
        "NAMA_BANK",
        "ADDRESS_ID_CARD"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG + TYPE_FK,//Gede_28Nov2011 tambah TYPE_FK
        TYPE_DATE,
        TYPE_INT,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_STRING,
        //
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        //Gede_15Nov2011{
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,//}
        //Gede_27Nov2011{
        TYPE_LONG,
        // add by Kartika  1 Dec 2011
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,// update by satrya 2012-11-14
        TYPE_STRING,
        TYPE_LONG,
        TYPE_INT,
        //update by agus priska 2014-10-27
        TYPE_LONG,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        // Add TYPE by Hendra McHen 2015-01-09
        TYPE_DATE,
        TYPE_INT,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING,
        /*Add Field by Hendra McHen 2015-04-24*/
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG + TYPE_FK,
        TYPE_INT,
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        //added by dewok 20190610
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING
    };
    //gender----
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final String[] sexKey = {"Male", "Female"};
    public static final int[] sexValue = {0, 1};
    //resigned
    public static final int NO_RESIGN = 0;
    public static final int YES_RESIGN = 1;
    public static final String[] resignKey = {"No", "Yes"};
    public static final int[] resignValue = {0, 1};
    public static final String[] memberOfBPJSKesehatanKey = {"No", "Yes"};
    public static final int[] memberOfBPJSKesehatanValue = {0, 1};

    public static final String[] memberOfBPJSKetenagaKerjaanKey = {"No", "Yes"};
    public static final int[] memberOfBPJSKetenagaKerjaanValue = {0, 1};
    public static final String[] leaveKey = {"Paid", "Unpaid"};
    public static final int[] leaveValue = {0, 1};
    //change leave with money
    public static final int YES_CHANGE = 0;
    public static final int NO_CHANGE = 1;
    // public static int STS_BOTH = 1;
    public static int STS_COMMING = 0;
    public static int STS_RESIGN = 1;
    public static String COMMING_STR = "COMMING";
    public static String[] stResignation = {
        "COMMING", "RESIGN"
    };
    //resigned
    public static final String[] blood = {"?", "A", "B", "O", "AB"/*"A", "B", "O", "AB", "?"*/};
    private static final String[] Id_Card_Type = {"?", "KTP", "SIM", "KARTU PELAJAR", "KITAS"};
    private static final String[] Shio = {"?", "TIKUS", "KERBAU", "MACAN", "KELINCI", "NAGA", "ULAR", "KUDA", "KAMBING", "MONYET", "AYAM", "ANJING", "BABI"};

    // Presence Check Parameter | Update 2015-01-09 | Hendra McHen ----
    public static final int PRESENCE_CHECK_DEFAULT = 0;
    public static final int PRESENCE_CHECK_ALWAYS_OK = 1;
    public static final String[] presenceCheckKey = {"Default", "Always OK"};
    public static final int[] presenceCheckValue = {0, 1};
    // Status Pensiun Program | Update 2015-01-09 | Hendra McHen
    public static final int STATUS_PENSIUN_PROGRAM_INCLUDE = 1;
    public static final int STATUS_PENSIUN_PROGRAM_NOT_INCLUDE = 0;
    public static final String[] statusPensiunProgramKey = {"Not Include", "Include"};
    public static final int[] statusPensiunProgramValue = {0, 1};

    public static final String[] statusDanaPendidikanKey = {"No", "Yes"};
    public static final int[] statusDanaPendidikanValue = {0, 1};

    /**
     * create by satrya 2014-01-31 keterangan: untuk pencarian data menggunakan
     * koma
     *
     * @param text
     * @return
     */
    private static Vector logicParser(String text) {
        Vector vector = LogicParser.textSentence(text);
        for (int i = 0; i < vector.size(); i++) {
            String code = (String) vector.get(i);
            if (((vector.get(vector.size() - 1)).equals(LogicParser.SIGN))
                    && ((vector.get(vector.size() - 1)).equals(LogicParser.ENGLISH))) {
                vector.remove(vector.size() - 1);
            }
        }

        return vector;
    }

    public static Vector getBlood() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < blood.length; i++) {
            result.add(blood[i]);
        }
        return result;
    }
    //nationality type
    public static int LOCAL = 0;
    public static int FOREIGNER = 1;
    public static String[] nationalityType = {
        "Local", "Foreigner"
    };
    //statutPayroll
    public static int ACTIVE = 0;
    public static int RESIGN_THIS_MONTH = 1;
    public static String[] resignPayroll = {
        "Active", "Resign This Month"
    };

    public static Vector getStatusPayroll() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < resignPayroll.length; i++) {
            result.add(resignPayroll[i]);
        }
        return result;
    }

    /**
     * @return the Id_Card_Type
     */
    public static Vector getId_Card_Type() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < Id_Card_Type.length; i++) {
            result.add(Id_Card_Type[i]);
        }
        return result;
    }

    /**
     * @return the Shio
     */
    public static Vector getShio() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < Shio.length; i++) {
            result.add(Shio[i]);
        }
        return result;
    }

    public PstEmployee() {
    }

    public PstEmployee(int i) throws DBException {
        super(new PstEmployee());
    }

    public PstEmployee(String sOid) throws DBException {
        super(new PstEmployee(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstEmployee(long lOid) throws DBException {
        super(new PstEmployee(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_HR_EMPLOYEE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstEmployee().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Employee employee = fetchExc(ent.getOID());
        ent = (Entity) employee;
        return employee.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Employee) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Employee) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static Employee fetchExc(long oid) throws DBException {
        try {
            Employee employee = new Employee();
            PstEmployee pstEmployee = new PstEmployee(oid);
            employee.setOID(oid);

            employee.setDepartmentId(pstEmployee.getlong(FLD_DEPARTMENT_ID));
            employee.setPositionId(pstEmployee.getlong(FLD_POSITION_ID));
            employee.setSectionId(pstEmployee.getlong(FLD_SECTION_ID));
            employee.setEmployeeNum(pstEmployee.getString(FLD_EMPLOYEE_NUM));
            employee.setEmpCategoryId(pstEmployee.getlong(FLD_EMP_CATEGORY_ID));
            employee.setLevelId(pstEmployee.getlong(FLD_LEVEL_ID));
            employee.setFullName(pstEmployee.getString(FLD_FULL_NAME));
            employee.setAddress(pstEmployee.getString(FLD_ADDRESS));
            employee.setPhone(pstEmployee.getString(FLD_PHONE));
            employee.setHandphone(pstEmployee.getString(FLD_HANDPHONE));
            employee.setPostalCode(pstEmployee.getInt(FLD_POSTAL_CODE));
            employee.setSex(pstEmployee.getInt(FLD_SEX));
            employee.setBirthPlace(pstEmployee.getString(FLD_BIRTH_PLACE));
            employee.setBirthDate(pstEmployee.getDate(FLD_BIRTH_DATE));
            employee.setReligionId(pstEmployee.getlong(FLD_RELIGION_ID));
            employee.setBloodType(pstEmployee.getString(FLD_BLOOD_TYPE));
            employee.setAstekNum(pstEmployee.getString(FLD_ASTEK_NUM));
            employee.setAstekDate(pstEmployee.getDate(FLD_ASTEK_DATE));
            employee.setMaritalId(pstEmployee.getlong(FLD_MARITAL_ID));
            employee.setTaxMaritalId(pstEmployee.getlong(FLD_TAX_MARITAL_ID));
            //employee.setTaxMaritalId(pstEmployee.getlong(FLD_TAX_MARITAL_ID)); //??
            employee.setLockerId(pstEmployee.getlong(FLD_LOCKER_ID));
            employee.setCommencingDate(pstEmployee.getDate(FLD_COMMENCING_DATE));
            employee.setResigned(pstEmployee.getInt(FLD_RESIGNED));
            employee.setResignedDate(pstEmployee.getDate(FLD_RESIGNED_DATE));
            employee.setBarcodeNumber(pstEmployee.getString(FLD_BARCODE_NUMBER));
            employee.setResignedReasonId(pstEmployee.getlong(FLD_RESIGNED_REASON_ID));
            employee.setResignedDesc(pstEmployee.getString(FLD_RESIGNED_DESC));
            employee.setBasicSalary(pstEmployee.getdouble(FLD_BASIC_SALARY));
            employee.setIsAssignToAccounting(pstEmployee.getboolean(FLD_ASSIGN_TO_ACCOUNTING));
            employee.setDivisionId(pstEmployee.getlong(FLD_DIVISION_ID));
            //untuk keperluan intimas,yaitu pembawa
            employee.setCurier(pstEmployee.getString(FLD_CURIER));
            /* penambahan kolom sehubungan dengan payroll
             *updated by Yunny
             */
            employee.setIndentCardNr(pstEmployee.getString(FLD_INDENT_CARD_NR));
            employee.setIndentCardValidTo(pstEmployee.getDate(FLD_INDENT_CARD_VALID_TO));
            employee.setTaxRegNr(pstEmployee.getString(FLD_TAX_REG_NR));
            employee.setAddressForTax(pstEmployee.getString(FLD_ADDRESS_FOR_TAX));
            employee.setNationalityType(pstEmployee.getInt(FLD_NATIONALITY_TYPE));
            employee.setEmailAddress(pstEmployee.getString(FLD_EMAIL_ADDRESS));
            employee.setCategoryDate(pstEmployee.getDate(FLD_CATEGORY_DATE));
            employee.setLeaveStatus(pstEmployee.getInt(FLD_LEAVE_STATUS));

            employee.setEmpPin(pstEmployee.getString(FLD_EMP_PIN));
            employee.setRace(pstEmployee.getlong(FLD_RACE));

            employee.setAddressPermanent(pstEmployee.getString(FLD_ADDRESS_PERMANENT));
            employee.setPhoneEmergency(pstEmployee.getString(FLD_PHONE_EMERGENCY));
            employee.setCompanyId(pstEmployee.getlong(FLD_COMPANY_ID));
            //Gede_15Nov2011{
            employee.setFather(pstEmployee.getString(FLD_FATHER));
            employee.setMother(pstEmployee.getString(FLD_MOTHER));
            employee.setParentsAddress(pstEmployee.getString(FLD_PARENTS_ADDRESS));
            employee.setNameEmg(pstEmployee.getString(FLD_NAME_EMG));
            employee.setPhoneEmg(pstEmployee.getString(FLD_PHONE_EMG));
            employee.setAddressEmg(pstEmployee.getString(FLD_ADDRESS_EMG));//}
            //Gede_27Nov2011
            employee.setHodEmployeeId(pstEmployee.getlong(FLD_HOD_EMPLOYEE_ID));//}
            // add by Kartika 1 dec 2011
            employee.setAddrCountryId(pstEmployee.getlong(FLD_ADDR_COUNTRY_ID));
            employee.setAddrProvinceId(pstEmployee.getlong(FLD_ADDR_PROVINCE_ID));
            employee.setAddrRegencyId(pstEmployee.getlong(FLD_ADDR_REGENCY_ID));
            employee.setAddrSubRegencyId(pstEmployee.getlong(FLD_ADDR_SUBREGENCY_ID));
            employee.setAddrPmntCountryId(pstEmployee.getlong(FLD_ADDR_PMNT_COUNTRY_ID));
            employee.setAddrPmntProvinceId(pstEmployee.getlong(FLD_ADDR_PMNT_PROVINCE_ID));
            employee.setAddrPmntRegencyId(pstEmployee.getlong(FLD_ADDR_PMNT_REGENCY_ID));
            employee.setAddrPmntSubRegencyId(pstEmployee.getlong(FLD_ADDR_PMNT_SUBREGENCY_ID));

            employee.setIndentCardIssuedBy(pstEmployee.getString(FLD_ID_CARD_ISSUED_BY));//}
            employee.setIndentCardBirthDate(pstEmployee.getDate(FLD_ID_CARD_BIRTH_DATE));
            //update by satrya 2012-11-14
            employee.setEducationId(pstEmployee.getLong(FLD_EDUCATION_ID));
            employee.setNoRekening(pstEmployee.getString(FLD_NO_REKENING));
            employee.setGradeLevelId(pstEmployee.getlong(FLD_GRADE_LEVEL_ID));
            //update by satrya 2014-07-03
            employee.setCountIdx(pstEmployee.getInt(FLD_COUNT_IDX));
            //update by ganki priska 2014 - 10 30
            employee.setLocationId(pstEmployee.getLong(FLD_LOCATION_ID));
            employee.setEnd_contract(pstEmployee.getDate(FLD_END_CONTRACT));
            employee.setWorkassigncompanyId(pstEmployee.getLong(FLD_WORK_ASSIGN_COMPANY_ID));
            employee.setWorkassigndivisionId(pstEmployee.getLong(FLD_WORK_ASSIGN_DIVISION_ID));
            employee.setWorkassigndepartmentId(pstEmployee.getLong(FLD_WORK_ASSIGN_DEPARTMENT_ID));
            employee.setWorkassignsectionId(pstEmployee.getLong(FLD_WORK_ASSIGN_SECTION_ID));
            employee.setWorkassignpositionId(pstEmployee.getLong(FLD_WORK_ASSIGN_POSITION_ID));
            employee.setIdcardtype(pstEmployee.getString(FLD_ID_CARD_TYPE));
            employee.setNpwp(pstEmployee.getString(FLD_NPWP));
            employee.setBpjs_no(pstEmployee.getString(FLD_BPJS_NO));
            employee.setBpjs_date(pstEmployee.getDate(FLD_BPJS_DATE));
            employee.setShio(pstEmployee.getString(FLD_SHIO));
            employee.setElemen(pstEmployee.getString(FLD_ELEMEN));
            employee.setIq(pstEmployee.getString(FLD_IQ));
            employee.setEq(pstEmployee.getString(FLD_EQ));

            // Add Field by Hendra McHen 2015-01-09
            employee.setProbationEndDate(pstEmployee.getDate(FLD_PROBATION_END_DATE));
            employee.setStatusPensiunProgram(pstEmployee.getInt(FLD_STATUS_PENSIUN_PROGRAM));
            employee.setStartDatePensiun(pstEmployee.getDate(FLD_START_DATE_PENSIUN));
            employee.setPresenceCheckParameter(pstEmployee.getInt(FLD_PRESENCE_CHECK_PARAMETER));
            employee.setMedicalInfo(pstEmployee.getString(FLD_MEDICAL_INFO));
            /* Add Field by Hendra McHen 2015-04-24 */
            employee.setDanaPendidikan(pstEmployee.getInt(FLD_DANA_PENDIDIKAN));
            //priska 20150731
            employee.setPayrollGroup(pstEmployee.getlong(FLD_PAYROLL_GROUP));
            employee.setProviderID(pstEmployee.getlong(FLD_PROVIDER_ID));// by kartika 2015-09-16
            //priska 20151104
            employee.setMemOfBpjsKesahatan(pstEmployee.getInt(FLD_MEMBER_OF_KESEHATAN));
            employee.setMemOfBpjsKetenagaKerjaan(pstEmployee.getInt(FLD_MEMBER_OF_KETENAGAKERJAAN));
            employee.setAddrDistrictId(pstEmployee.getlong(FLD_ADDR_DISTRICT_ID));
            employee.setAddrPmntDistrictId(pstEmployee.getlong(FLD_ADDR_PMNT_DISTRICT_ID));
            employee.setNoKK(pstEmployee.getString(FLD_NO_KK));
            //added by dewok 20190610
            employee.setNamaPemilikRekening(pstEmployee.getString(FLD_NAMA_PEMILIK_REKENING));
            employee.setCabangBank(pstEmployee.getString(FLD_CABANG_BANK));
            employee.setKodeBank(pstEmployee.getString(FLD_KODE_BANK));
            employee.setTanggalPajakTerdaftar(pstEmployee.getDate(FLD_TANGGAL_PAJAK_TERDAFTAR));
            employee.setNamaBank(pstEmployee.getString(FLD_NAMA_BANK));
            employee.setAddressIdCard(pstEmployee.getString(FLD_ADDRESS_ID_CARD));
            
            fetchGeoAddress(employee);

            return employee;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
    }

    public static Hashtable fetchExcHashtable(long oid) throws DBException {

        try {
            Hashtable hashtableEmp = new Hashtable();
            Employee employee = new Employee();
            PstEmployee pstEmployee = new PstEmployee(oid);
            employee.setOID(oid);

            employee.setDepartmentId(pstEmployee.getlong(FLD_DEPARTMENT_ID));
            Department department = new Department();
            try {
                department = PstDepartment.fetchExc(employee.getDepartmentId());
            } catch (Exception e) {
            }
            employee.setDepartmentName(department.getDepartment());
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID], department.getDepartment());

            employee.setPositionId(pstEmployee.getlong(FLD_POSITION_ID));
            Position position = new Position();
            try {
                position = PstPosition.fetchExc(employee.getPositionId());
            } catch (Exception e) {
            }
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID], position.getPosition());

            employee.setSectionId(pstEmployee.getlong(FLD_SECTION_ID));
            Section section = new Section();
            try {
                section = PstSection.fetchExc(employee.getSectionId());
            } catch (Exception e) {
            }
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID], section.getSection());

            employee.setEmployeeNum(pstEmployee.getString(FLD_EMPLOYEE_NUM));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM], employee.getEmployeeNum());

            employee.setEmpCategoryId(pstEmployee.getlong(FLD_EMP_CATEGORY_ID));
            EmpCategory empCategory = new EmpCategory();
            try {
                empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
            } catch (Exception e) {
            }
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID], empCategory.getEmpCategory());

            employee.setLevelId(pstEmployee.getlong(FLD_LEVEL_ID));
            Level level = new Level();
            try {
                level = PstLevel.fetchExc(employee.getLevelId());
            } catch (Exception e) {
            }
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID], level.getLevel());

            employee.setFullName(pstEmployee.getString(FLD_FULL_NAME));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME], employee.getFullName());
            employee.setAddress(pstEmployee.getString(FLD_ADDRESS));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS], employee.getAddress());
            employee.setPhone(pstEmployee.getString(FLD_PHONE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PHONE], employee.getPhone());
            employee.setHandphone(pstEmployee.getString(FLD_HANDPHONE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE], employee.getHandphone());
            employee.setPostalCode(pstEmployee.getInt(FLD_POSTAL_CODE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE], employee.getPostalCode());
            employee.setSex(pstEmployee.getInt(FLD_SEX));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_SEX], employee.getSex());
            employee.setBirthPlace(pstEmployee.getString(FLD_BIRTH_PLACE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE], employee.getBirthPlace());
            employee.setBirthDate(pstEmployee.getDate(FLD_BIRTH_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE], (employee.getBirthDate() != null ? employee.getBirthDate() : ""));
            employee.setReligionId(pstEmployee.getlong(FLD_RELIGION_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PHONE], employee.getPhone());
            employee.setBloodType(pstEmployee.getString(FLD_BLOOD_TYPE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE], employee.getBloodType());
            employee.setAstekNum(pstEmployee.getString(FLD_ASTEK_NUM));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM], employee.getAstekNum());
            employee.setAstekDate(pstEmployee.getDate(FLD_ASTEK_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE], (employee.getAstekDate() != null ? employee.getAstekDate() : ""));
            employee.setMaritalId(pstEmployee.getlong(FLD_MARITAL_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID], employee.getMaritalId());
            employee.setTaxMaritalId(pstEmployee.getlong(FLD_TAX_MARITAL_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID], employee.getTaxMaritalId());
            //employee.setTaxMaritalId(pstEmployee.getlong(FLD_TAX_MARITAL_ID)); //??
            employee.setLockerId(pstEmployee.getlong(FLD_LOCKER_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID], employee.getLockerId());
            employee.setCommencingDate(pstEmployee.getDate(FLD_COMMENCING_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE], (employee.getCommencingDate() != null ? employee.getCommencingDate() : ""));
            employee.setResigned(pstEmployee.getInt(FLD_RESIGNED));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED], employee.getResigned());
            employee.setResignedDate(pstEmployee.getDate(FLD_RESIGNED_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE], (employee.getResignedDate() != null ? employee.getResignedDate() : ""));
            employee.setBarcodeNumber(pstEmployee.getString(FLD_BARCODE_NUMBER));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER], employee.getBarcodeNumber());
            employee.setResignedReasonId(pstEmployee.getlong(FLD_RESIGNED_REASON_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID], employee.getResignedReasonId());
            employee.setResignedDesc(pstEmployee.getString(FLD_RESIGNED_DESC));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC], employee.getResignedDesc());
            employee.setBasicSalary(pstEmployee.getdouble(FLD_BASIC_SALARY));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY], employee.getBasicSalary());
            employee.setIsAssignToAccounting(pstEmployee.getboolean(FLD_ASSIGN_TO_ACCOUNTING));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING], employee.getIsAssignToAccounting());

            employee.setDivisionId(pstEmployee.getlong(FLD_DIVISION_ID));
            Division division = new Division();
            try {
                division = PstDivision.fetchExc(employee.getDivisionId());
            } catch (Exception e) {
            }
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID], division.getDivision());

            //untuk keperluan intimas,yaitu pembawa
            employee.setCurier(pstEmployee.getString(FLD_CURIER));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_CURIER], employee.getCurier());
            /* penambahan kolom sehubungan dengan payroll
             *updated by Yunny
             */
            employee.setIndentCardNr(pstEmployee.getString(FLD_INDENT_CARD_NR));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_NR], employee.getIndentCardNr());
            employee.setIndentCardValidTo(pstEmployee.getDate(FLD_INDENT_CARD_VALID_TO));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_VALID_TO], "");
            employee.setTaxRegNr(pstEmployee.getString(FLD_TAX_REG_NR));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_TAX_REG_NR], employee.getTaxRegNr());
            employee.setAddressForTax(pstEmployee.getString(FLD_ADDRESS_FOR_TAX));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_FOR_TAX], employee.getAddressForTax());
            employee.setNationalityType(pstEmployee.getInt(FLD_NATIONALITY_TYPE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_NATIONALITY_TYPE], employee.getNationalityType());
            employee.setEmailAddress(pstEmployee.getString(FLD_EMAIL_ADDRESS));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_EMAIL_ADDRESS], employee.getEmailAddress());
            employee.setCategoryDate(pstEmployee.getDate(FLD_CATEGORY_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_CATEGORY_DATE], "");
            employee.setLeaveStatus(pstEmployee.getInt(FLD_LEAVE_STATUS));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_LEAVE_STATUS], employee.getLeaveStatus());

            employee.setEmpPin(pstEmployee.getString(FLD_EMP_PIN));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN], employee.getEmpPin());
            employee.setRace(pstEmployee.getlong(FLD_RACE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_RACE], employee.getRace());

            employee.setAddressPermanent(pstEmployee.getString(FLD_ADDRESS_PERMANENT));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT], employee.getAddressPermanent());
            employee.setPhoneEmergency(pstEmployee.getString(FLD_PHONE_EMERGENCY));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMERGENCY], employee.getPhoneEmergency());

            employee.setCompanyId(pstEmployee.getlong(FLD_COMPANY_ID));
            Company company = new Company();
            try {
                company = PstCompany.fetchExc(employee.getCompanyId());
            } catch (Exception e) {
            }
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID], company.getCompany());
            //Gede_15Nov2011{
            employee.setFather(pstEmployee.getString(FLD_FATHER));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_FATHER], employee.getFather());
            employee.setMother(pstEmployee.getString(FLD_MOTHER));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_MOTHER], employee.getMother());
            employee.setParentsAddress(pstEmployee.getString(FLD_PARENTS_ADDRESS));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PARENTS_ADDRESS], employee.getParentsAddress());
            employee.setNameEmg(pstEmployee.getString(FLD_NAME_EMG));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_NAME_EMG], employee.getNameEmg());
            employee.setPhoneEmg(pstEmployee.getString(FLD_PHONE_EMG));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMG], employee.getPhoneEmg());
            employee.setAddressEmg(pstEmployee.getString(FLD_ADDRESS_EMG));//}
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_EMG], employee.getCompanyId());
            //Gede_27Nov2011
            employee.setHodEmployeeId(pstEmployee.getlong(FLD_HOD_EMPLOYEE_ID));//}
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_HOD_EMPLOYEE_ID], employee.getHodEmployeeId());
            // add by Kartika 1 dec 2011
            employee.setAddrCountryId(pstEmployee.getlong(FLD_ADDR_COUNTRY_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_COUNTRY_ID], employee.getAddrCountryId());
            employee.setAddrProvinceId(pstEmployee.getlong(FLD_ADDR_PROVINCE_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PROVINCE_ID], employee.getAddrProvinceId());
            employee.setAddrRegencyId(pstEmployee.getlong(FLD_ADDR_REGENCY_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_REGENCY_ID], employee.getAddrRegencyId());
            employee.setAddrSubRegencyId(pstEmployee.getlong(FLD_ADDR_SUBREGENCY_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_SUBREGENCY_ID], employee.getAddrSubRegencyId());
            employee.setAddrDistrictId(pstEmployee.getlong(FLD_ADDR_DISTRICT_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_DISTRICT_ID], employee.getAddrDistrictId());
            employee.setAddrPmntCountryId(pstEmployee.getlong(FLD_ADDR_PMNT_COUNTRY_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_COUNTRY_ID], employee.getAddrPmntCountryId());
            employee.setAddrPmntProvinceId(pstEmployee.getlong(FLD_ADDR_PMNT_PROVINCE_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_PROVINCE_ID], employee.getAddrPmntProvinceId());
            employee.setAddrPmntRegencyId(pstEmployee.getlong(FLD_ADDR_PMNT_REGENCY_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_REGENCY_ID], employee.getAddrPmntRegencyId());
            employee.setAddrPmntSubRegencyId(pstEmployee.getlong(FLD_ADDR_PMNT_SUBREGENCY_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_SUBREGENCY_ID], employee.getAddrSubRegencyId());
            employee.setAddrPmntDistrictId(pstEmployee.getlong(FLD_ADDR_PMNT_DISTRICT_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_DISTRICT_ID], employee.getAddrDistrictId());

            employee.setIndentCardIssuedBy(pstEmployee.getString(FLD_ID_CARD_ISSUED_BY));//}
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_ISSUED_BY], employee.getIndentCardIssuedBy());
            employee.setIndentCardBirthDate(pstEmployee.getDate(FLD_ID_CARD_BIRTH_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_BIRTH_DATE], (employee.getIndentCardBirthDate() != null ? employee.getIndentCardBirthDate() : ""));
            //update by satrya 2012-11-14
            employee.setEducationId(pstEmployee.getLong(FLD_EDUCATION_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_EDUCATION_ID], employee.getEducationId());
            employee.setNoRekening(pstEmployee.getString(FLD_NO_REKENING));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_NO_REKENING], employee.getNoRekening());
            //update by satrya 2014-07-03
            employee.setCountIdx(pstEmployee.getInt(FLD_COUNT_IDX));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_COUNT_IDX], employee.getCountIdx());
            //update by ganki priska 2014 - 10 30
            employee.setLocationId(pstEmployee.getLong(FLD_LOCATION_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID], employee.getLocationId());
            employee.setEnd_contract(pstEmployee.getDate(FLD_END_CONTRACT));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT], (employee.getEnd_contract() != null ? employee.getEnd_contract() : ""));
            employee.setWorkassigncompanyId(pstEmployee.getLong(FLD_WORK_ASSIGN_COMPANY_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_COMPANY_ID], employee.getWorkassigncompanyId());
            employee.setWorkassigndivisionId(pstEmployee.getLong(FLD_WORK_ASSIGN_DIVISION_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DIVISION_ID], employee.getWorkassigndivisionId());
            employee.setWorkassigndepartmentId(pstEmployee.getLong(FLD_WORK_ASSIGN_DEPARTMENT_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DEPARTMENT_ID], employee.getWorkassigndepartmentId());
            employee.setWorkassignsectionId(pstEmployee.getLong(FLD_WORK_ASSIGN_SECTION_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_SECTION_ID], employee.getWorkassignsectionId());
            employee.setWorkassignpositionId(pstEmployee.getLong(FLD_WORK_ASSIGN_POSITION_ID));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_POSITION_ID], employee.getWorkassignpositionId());
            employee.setIdcardtype(pstEmployee.getString(FLD_ID_CARD_TYPE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_TYPE], (employee.getIdcardtype() != null ? employee.getIdcardtype() : ""));
            employee.setNpwp(pstEmployee.getString(FLD_NPWP));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_NPWP], (employee.getNpwp() != null ? employee.getNpwp() : ""));
            employee.setBpjs_no(pstEmployee.getString(FLD_BPJS_NO));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_BPJS_NO], (employee.getBpjs_no() != null ? employee.getBpjs_no() : ""));
            employee.setBpjs_date(pstEmployee.getDate(FLD_BPJS_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_BPJS_DATE], (employee.getBpjs_date() != null ? employee.getBpjs_date() : ""));
            employee.setShio(pstEmployee.getString(FLD_SHIO));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_SHIO], (employee.getShio() != null ? employee.getShio() : ""));
            employee.setElemen(pstEmployee.getString(FLD_ELEMEN));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ELEMEN], (employee.getElemen() != null ? employee.getElemen() : ""));
            employee.setIq(pstEmployee.getString(FLD_IQ));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_IQ], (employee.getIq() != null ? employee.getIq() : ""));
            employee.setEq(pstEmployee.getString(FLD_EQ));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_EQ], (employee.getEq() != null ? employee.getEq() : ""));

            // Add Field by Hendra McHen 2015-01-09
            employee.setProbationEndDate(pstEmployee.getDate(FLD_PROBATION_END_DATE));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PROBATION_END_DATE], (employee.getProbationEndDate() != null ? employee.getProbationEndDate() : ""));
            employee.setStatusPensiunProgram(pstEmployee.getInt(FLD_STATUS_PENSIUN_PROGRAM));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_STATUS_PENSIUN_PROGRAM], (employee.getStatusPensiunProgram() != 0 ? employee.getStatusPensiunProgram() : ""));
            employee.setStartDatePensiun(pstEmployee.getDate(FLD_START_DATE_PENSIUN));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_START_DATE_PENSIUN], (employee.getStartDatePensiun() != null ? employee.getStartDatePensiun() : ""));
            employee.setPresenceCheckParameter(pstEmployee.getInt(FLD_PRESENCE_CHECK_PARAMETER));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PRESENCE_CHECK_PARAMETER], (employee.getPresenceCheckParameter() != 0 ? employee.getPresenceCheckParameter() : ""));
            employee.setMedicalInfo(pstEmployee.getString(FLD_MEDICAL_INFO));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_MEDICAL_INFO], (employee.getMedicalInfo() != null ? employee.getMedicalInfo() : ""));
            /* Add Field by Hendra McHen 2015-04-24 */
            employee.setDanaPendidikan(pstEmployee.getInt(FLD_DANA_PENDIDIKAN));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_DANA_PENDIDIKAN], (employee.getDanaPendidikan() != 0 ? employee.getDanaPendidikan() : ""));
            //priska 20150731
            employee.setPayrollGroup(pstEmployee.getlong(FLD_PAYROLL_GROUP));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP], (employee.getPayrollGroup() != 0 ? employee.getPayrollGroup() : ""));

            employee.setProviderID(pstEmployee.getlong(FLD_PROVIDER_ID));// by kartika 2015-09-16
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_PROVIDER_ID], (employee.getProviderID() != 0 ? employee.getProviderID() : ""));

            employee.setGradeLevelId(pstEmployee.getlong(FLD_GRADE_LEVEL_ID));
            GradeLevel gradeLevel = new GradeLevel();
            try {
                gradeLevel = PstGradeLevel.fetchExc(employee.getGradeLevelId());
            } catch (Exception e) {
            }
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID], gradeLevel.getCodeLevel());

            hashtableEmp.put("NOW_POSITION", company.getCompany() + "," + division.getDivision() + "," + department.getDepartment() + "," + section.getSection() + "," + position.getPosition());

            Vector careerV = PstCareerPath.list(0, 0, "" + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + "=" + employee.getOID(), "WORK_FROM");

            CareerPath careerPath = new CareerPath();

            try {
                careerPath = (CareerPath) careerV.get(careerV.size());
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            hashtableEmp.put("HISTORY_TYPE", "" + PstCareerPath.historyType[careerPath.getHistoryType()]);

            fetchGeoAddress(employee);

            employee.setNoKK(pstEmployee.getString(FLD_NO_KK));// by kartika 2015-09-16
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_NO_KK], (!employee.getNoKK().equals("") ? employee.getNoKK() : "-"));
            //added by dewok 20190610
            employee.setNamaPemilikRekening(pstEmployee.getString(FLD_NAMA_PEMILIK_REKENING));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_NAMA_PEMILIK_REKENING], (!employee.getNamaPemilikRekening().equals("") ? employee.getNamaPemilikRekening() : "-"));
            employee.setCabangBank(pstEmployee.getString(FLD_CABANG_BANK));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_CABANG_BANK], (!employee.getCabangBank().equals("") ? employee.getCabangBank() : "-"));
            employee.setKodeBank(pstEmployee.getString(FLD_KODE_BANK));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_KODE_BANK], (!employee.getKodeBank().equals("") ? employee.getKodeBank() : "-"));
            employee.setTanggalPajakTerdaftar(pstEmployee.getDate(FLD_TANGGAL_PAJAK_TERDAFTAR));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_TANGGAL_PAJAK_TERDAFTAR], (employee.getTanggalPajakTerdaftar() != null ? employee.getTanggalPajakTerdaftar() : "-"));
            employee.setNamaBank(pstEmployee.getString(FLD_NAMA_BANK));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_NAMA_BANK], (!employee.getNamaBank().equals("") ? employee.getNamaBank(): "-"));
            employee.setAddressIdCard(pstEmployee.getString(FLD_ADDRESS_ID_CARD));
            hashtableEmp.put(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_ID_CARD], (!employee.getAddressIdCard().equals("") ? employee.getAddressIdCard(): "-"));

            return hashtableEmp;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
    }

    /**
     * @Author : Kartika , 2 December 2011 Mengambil gabungan data alamat
     * address dan address permanent ke employee geoAddress dan geoAdressPmnt
     * @param employee
     */
    public static void fetchGeoAddress(Employee employee) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        if ((employee == null) || (employee.getOID() == 0)) {
            return;
        }
        try {
            String sql
                    = "SELECT e." + fieldNames[FLD_EMPLOYEE_ID]
                    + ", n." + PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA] + " AS NEG "
                    + ", p." + PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + " AS PROV "
                    + ", k." + PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN] + " AS KAB "
                    + ", c." + PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN] + " AS KEC "
                    + ", d." + PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT] + " AS DIS "
                    + ", n1." + PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA] + " AS NEG1 "
                    + ", p1." + PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + " AS PROV1 "
                    + ", k1." + PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN] + " AS KAB1 "
                    + ", c1." + PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN] + " AS KEC1 "
                    + ", d1." + PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT] + " AS DIS1 "
                    + " FROM " + TBL_HR_EMPLOYEE + " e "
                    + " LEFT JOIN " + PstNegara.TBL_BKD_NEGARA + " n ON e." + fieldNames[FLD_ADDR_COUNTRY_ID] + "=n." + PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA]
                    + " LEFT JOIN " + PstProvinsi.TBL_HR_PROPINSI + " p ON e." + fieldNames[FLD_ADDR_PROVINCE_ID] + "= p." + PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]
                    + " LEFT JOIN " + PstKabupaten.TBL_HR_KABUPATEN + " k ON e." + fieldNames[FLD_ADDR_REGENCY_ID] + " = k." + PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]
                    + " LEFT JOIN " + PstKecamatan.TBL_HR_KECAMATAN + " c ON e." + fieldNames[FLD_ADDR_SUBREGENCY_ID] + "= c." + PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN]
                    + " LEFT JOIN " + PstDistrict.TBL_HR_DISTRICT + " d ON e." + fieldNames[FLD_ADDR_DISTRICT_ID] + "= d." + PstDistrict.fieldNames[PstDistrict.FLD_DISTRICT_ID]
                    + " LEFT JOIN " + PstNegara.TBL_BKD_NEGARA + " n1 ON e." + fieldNames[FLD_ADDR_PMNT_COUNTRY_ID] + "=n1." + PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA]
                    + " LEFT JOIN " + PstProvinsi.TBL_HR_PROPINSI + " p1 ON e." + fieldNames[FLD_ADDR_PMNT_PROVINCE_ID] + "= p1." + PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]
                    + " LEFT JOIN " + PstKabupaten.TBL_HR_KABUPATEN + " k1 ON e." + fieldNames[FLD_ADDR_PMNT_REGENCY_ID] + " = k1." + PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]
                    + " LEFT JOIN " + PstKecamatan.TBL_HR_KECAMATAN + " c1 ON e." + fieldNames[FLD_ADDR_PMNT_SUBREGENCY_ID] + "= c1." + PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN]
                    + " LEFT JOIN " + PstDistrict.TBL_HR_DISTRICT + " d1 ON e." + fieldNames[FLD_ADDR_DISTRICT_ID] + "= d1." + PstDistrict.fieldNames[PstDistrict.FLD_DISTRICT_ID]
                    + " WHERE " + fieldNames[FLD_EMPLOYEE_ID] + "=\"" + employee.getOID() + "\"";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                employee.setGeoAddress(
                        "" + rs.getString("NEG") + ", "
                        + rs.getString("PROV") + ", "
                        + rs.getString("KAB") + ", "
                        + rs.getString("KEC") + ", "
                        + rs.getString("DIS"));
                employee.setGeoAddressPmnt(
                        rs.getString("NEG1") + ", "
                        + rs.getString("PROV1") + ", "
                        + rs.getString("KAB1") + ", "
                        + rs.getString("KEC1") + ", "
                        + rs.getString("DIS1"));
            }
            employee.setGeoAddress(employee.getGeoAddress().replaceAll("null", "-"));
            employee.setGeoAddressPmnt(employee.getGeoAddressPmnt().replaceAll("null", "-"));
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return;
    }

    public static long insertExc(Employee employee) throws DBException {
        try {
            PstEmployee pstEmployee = new PstEmployee(0);

            pstEmployee.setLong(FLD_DEPARTMENT_ID, employee.getDepartmentId());
            pstEmployee.setLong(FLD_POSITION_ID, employee.getPositionId());
            pstEmployee.setLong(FLD_SECTION_ID, employee.getSectionId());
            pstEmployee.setString(FLD_EMPLOYEE_NUM, employee.getEmployeeNum());
            pstEmployee.setLong(FLD_EMP_CATEGORY_ID, employee.getEmpCategoryId());
            pstEmployee.setLong(FLD_LEVEL_ID, employee.getLevelId());
            pstEmployee.setString(FLD_FULL_NAME, employee.getFullName());
            pstEmployee.setString(FLD_ADDRESS, employee.getAddress());
            pstEmployee.setString(FLD_PHONE, employee.getPhone());
            pstEmployee.setString(FLD_HANDPHONE, employee.getHandphone());
            pstEmployee.setInt(FLD_POSTAL_CODE, employee.getPostalCode());
            pstEmployee.setInt(FLD_SEX, employee.getSex());
            pstEmployee.setString(FLD_BIRTH_PLACE, employee.getBirthPlace());
            pstEmployee.setDate(FLD_BIRTH_DATE, employee.getBirthDate());
            pstEmployee.setLong(FLD_RELIGION_ID, employee.getReligionId());
            pstEmployee.setString(FLD_BLOOD_TYPE, employee.getBloodType());
            pstEmployee.setString(FLD_ASTEK_NUM, employee.getAstekNum());
            pstEmployee.setDate(FLD_ASTEK_DATE, employee.getAstekDate());
            pstEmployee.setLong(FLD_MARITAL_ID, employee.getMaritalId());
            pstEmployee.setLong(FLD_TAX_MARITAL_ID, employee.getTaxMaritalId());
            pstEmployee.setLong(FLD_LOCKER_ID, employee.getLockerId());
            pstEmployee.setDate(FLD_COMMENCING_DATE, employee.getCommencingDate());
            pstEmployee.setInt(FLD_RESIGNED, employee.getResigned());
            pstEmployee.setDate(FLD_RESIGNED_DATE, employee.getResignedDate());
            pstEmployee.setString(FLD_BARCODE_NUMBER, employee.getBarcodeNumber());
            pstEmployee.setLong(FLD_RESIGNED_REASON_ID, employee.getResignedReasonId());
            pstEmployee.setString(FLD_RESIGNED_DESC, employee.getResignedDesc());
            pstEmployee.setDouble(FLD_BASIC_SALARY, employee.getBasicSalary());
            pstEmployee.setboolean(FLD_ASSIGN_TO_ACCOUNTING, employee.getIsAssignToAccounting());
            pstEmployee.setLong(FLD_DIVISION_ID, employee.getDivisionId());
            //khusus untuk intimas
            pstEmployee.setString(FLD_CURIER, employee.getCurier());
            /*penambahan kolom sehubungan dengan payroll
             *Updated By Yunny
             */
            pstEmployee.setString(FLD_INDENT_CARD_NR, employee.getIndentCardNr());
            pstEmployee.setDate(FLD_INDENT_CARD_VALID_TO, employee.getIndentCardValidTo());
            pstEmployee.setString(FLD_TAX_REG_NR, employee.getTaxRegNr());
            pstEmployee.setString(FLD_ADDRESS_FOR_TAX, employee.getAddressForTax());
            pstEmployee.setInt(FLD_NATIONALITY_TYPE, employee.getNationalityType());
            pstEmployee.setString(FLD_EMAIL_ADDRESS, employee.getEmailAddress());
            pstEmployee.setDate(FLD_CATEGORY_DATE, employee.getCategoryDate());
            pstEmployee.setInt(FLD_LEAVE_STATUS, employee.getLeaveStatus());

            pstEmployee.setLong(FLD_RACE, employee.getRace());

            //hard rock
            pstEmployee.setString(FLD_ADDRESS_PERMANENT, employee.getAddressPermanent());
            pstEmployee.setString(FLD_PHONE_EMERGENCY, employee.getPhoneEmergency());

            pstEmployee.setLong(FLD_COMPANY_ID, employee.getCompanyId());
            //Gede_15Nov2011{
            pstEmployee.setString(FLD_FATHER, employee.getFather());
            pstEmployee.setString(FLD_MOTHER, employee.getMother());
            pstEmployee.setString(FLD_PARENTS_ADDRESS, employee.getParentsAddress());
            pstEmployee.setString(FLD_NAME_EMG, employee.getNameEmg());
            pstEmployee.setString(FLD_PHONE_EMG, employee.getPhoneEmg());
            pstEmployee.setString(FLD_ADDRESS_EMG, employee.getAddressEmg());//}
            //Gede_27Nov2011{
            pstEmployee.setLong(FLD_HOD_EMPLOYEE_ID, employee.getHodEmployeeId());//}
            // add by Kartika 1 dec 2011
            pstEmployee.setLong(FLD_ADDR_COUNTRY_ID, employee.getAddrCountryId());
            pstEmployee.setLong(FLD_ADDR_PROVINCE_ID, employee.getAddrProvinceId());
            pstEmployee.setLong(FLD_ADDR_REGENCY_ID, employee.getAddrRegencyId());
            pstEmployee.setLong(FLD_ADDR_SUBREGENCY_ID, employee.getAddrSubRegencyId());
            pstEmployee.setLong(FLD_ADDR_DISTRICT_ID, employee.getAddrDistrictId());
            pstEmployee.setLong(FLD_ADDR_PMNT_COUNTRY_ID, employee.getAddrPmntCountryId());
            pstEmployee.setLong(FLD_ADDR_PMNT_PROVINCE_ID, employee.getAddrPmntProvinceId());
            pstEmployee.setLong(FLD_ADDR_PMNT_REGENCY_ID, employee.getAddrPmntRegencyId());
            pstEmployee.setLong(FLD_ADDR_PMNT_SUBREGENCY_ID, employee.getAddrPmntSubRegencyId());
            pstEmployee.setLong(FLD_ADDR_PMNT_DISTRICT_ID, employee.getAddrPmntDistrictId());
            pstEmployee.setString(FLD_ID_CARD_ISSUED_BY, employee.getIndentCardIssuedBy());
            pstEmployee.setDate(FLD_ID_CARD_BIRTH_DATE, employee.getIndentCardBirthDate());

            pstEmployee.setString(FLD_NO_REKENING, employee.getNoRekening());
            pstEmployee.setLong(FLD_GRADE_LEVEL_ID, employee.getGradeLevelId());
            //priska 2014-10-30
            pstEmployee.setLong(FLD_LOCATION_ID, employee.getLocationId());
            pstEmployee.setDate(FLD_END_CONTRACT, employee.getEnd_contract());
            pstEmployee.setLong(FLD_WORK_ASSIGN_COMPANY_ID, employee.getWorkassigncompanyId());
            pstEmployee.setLong(FLD_WORK_ASSIGN_DIVISION_ID, employee.getWorkassigndivisionId());
            pstEmployee.setLong(FLD_WORK_ASSIGN_DEPARTMENT_ID, employee.getWorkassigndepartmentId());
            pstEmployee.setLong(FLD_WORK_ASSIGN_SECTION_ID, employee.getWorkassignsectionId());
            pstEmployee.setLong(FLD_WORK_ASSIGN_POSITION_ID, employee.getWorkassignpositionId());
            pstEmployee.setString(FLD_ID_CARD_TYPE, employee.getIdcardtype());
            pstEmployee.setString(FLD_NPWP, employee.getNpwp());
            pstEmployee.setString(FLD_BPJS_NO, employee.getBpjs_no());
            pstEmployee.setDate(FLD_BPJS_DATE, employee.getBpjs_date());
            pstEmployee.setString(FLD_SHIO, employee.getShio());
            pstEmployee.setString(FLD_ELEMEN, employee.getElemen());
            pstEmployee.setString(FLD_IQ, employee.getIq());
            pstEmployee.setString(FLD_EQ, employee.getEq());
            // Add Field by Hendra McHen 2015-01-09
            pstEmployee.setDate(FLD_PROBATION_END_DATE, employee.getProbationEndDate());
            pstEmployee.setInt(FLD_STATUS_PENSIUN_PROGRAM, employee.getStatusPensiunProgram());
            pstEmployee.setDate(FLD_START_DATE_PENSIUN, employee.getStartDatePensiun());
            pstEmployee.setInt(FLD_PRESENCE_CHECK_PARAMETER, employee.getPresenceCheckParameter());
            pstEmployee.setString(FLD_MEDICAL_INFO, employee.getMedicalInfo());
            /* Add Field by Hendra Putu | 2015-04-24 */
            pstEmployee.setInt(FLD_DANA_PENDIDIKAN, employee.getDanaPendidikan());
            //update by priska 2015-09-04
            pstEmployee.setLong(FLD_PAYROLL_GROUP, employee.getPayrollGroup());
            pstEmployee.setLong(FLD_PROVIDER_ID, employee.getProviderID());// by kartika 2015-09-16
            //update by priska 2015-11-04
            pstEmployee.setInt(FLD_MEMBER_OF_KESEHATAN, employee.getMemOfBpjsKesahatan());
            pstEmployee.setInt(FLD_MEMBER_OF_KETENAGAKERJAAN, employee.getMemOfBpjsKetenagaKerjaan());
            I_Atendance attdConfig = null;
            try {
                attdConfig = (I_Atendance) (Class.forName(PstSystemProperty.getValueByName("ATTENDANCE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
                System.out.println("Please contact your system administration to setup system property: ATTENDANCE_CONFIG ");
            }

            String clientName = "";
            try {
                clientName = PstSystemProperty.getValueByName("CLIENT_NAME");
            } catch (Exception ex) {
                System.out.println("Execption CLIENT_NAME " + ex);
            }

            int count = 0;
            if (attdConfig != null && attdConfig.getConfigurasiInputEmpNum() == I_Atendance.CONFIGURATION_II_GENERATE_AUTOMATIC_EMPLOYEE_NUMBER) {

                Company company = new Company();
                if (employee.getCompanyId() != 0) {
                    try {
                        company = PstCompany.fetchExc(employee.getCompanyId());
                    } catch (Exception exc) {
                    }
                }

                if (clientName.equals("QUEEN_TANDOOR")) {

                    if (employee.getCommencingDate() != null) {
                        Employee empNMax = fetchEmployeeNumMax();
                        String empNumber = Formater.formatDate(employee.getBirthDate(), "yyyyMMdd");
                        if (empNMax != null && empNMax.getOID() != 0 && empNMax.getCountIdx() != 0) {
                            String n = "00000";
                            try {
                                //n = n+(empNMax.getCountIdx() + 1);
                                count = empNMax.getCountIdx() + 1;
                            } catch (Exception exc) {
                                    // klu gagal
                                //n = n+(empNMax.getCountIdx() + 1); // 
                                count = empNMax.getCountIdx() + 1;
                            }
                            String tmp = "" + (empNMax.getCountIdx() + 1);
                            if (tmp.length() > 0 && n.length() >= tmp.length()) {
                                n = n.substring(0, n.length() - (tmp.length())) + (empNMax.getCountIdx() + 1);
                            }
                            empNMax.setEmployeeNum("" + empNumber + n);
                            employee.setEmployeeNum(empNMax.getEmployeeNum());
                        } else {
                            count = empNMax.getCountIdx() + 1;
                            empNMax.setEmployeeNum("" + empNumber + "00001");
                            employee.setEmployeeNum(empNMax.getEmployeeNum());
                        }
                    }

                } else {

                    if (company.getCodeCompany() != null && company.getCodeCompany().length() > 0 && employee.getBirthDate() != null) {
                        Employee empNMax = fetchEmployeeNumMax();
                        String empNumber = company.getCodeCompany() + Formater.formatDate(employee.getBirthDate(), "yy");
                        if (empNMax != null && empNMax.getOID() != 0 && empNMax.getCountIdx() != 0) {
                            String n = "00000";
                            try {
                                //n = n+(empNMax.getCountIdx() + 1);
                                count = empNMax.getCountIdx() + 1;
                            } catch (Exception exc) {
                                    // klu gagal
                                //n = n+(empNMax.getCountIdx() + 1); // 
                                count = empNMax.getCountIdx() + 1;
                            }
                            String tmp = "" + (empNMax.getCountIdx() + 1);
                            if (tmp.length() > 0 && n.length() >= tmp.length()) {
                                n = n.substring(0, n.length() - (tmp.length())) + (empNMax.getCountIdx() + 1);
                            }
                            empNMax.setEmployeeNum("" + empNumber + n);
                            employee.setEmployeeNum(empNMax.getEmployeeNum());
                        } else {
                            count = empNMax.getCountIdx() + 1;
                            empNMax.setEmployeeNum("" + empNumber + "00001");
                            employee.setEmployeeNum(empNMax.getEmployeeNum());
                        }
                    }

                }

            }
            int autoPinOk = 0;
            try {
                autoPinOk = Integer.valueOf(PstSystemProperty.getValueByName("AKTIF_AUTO_PIN"));
            } catch (Exception e) {
            }
            if (autoPinOk == 1) {
                int eYear = (employee.getBirthDate().getYear() + 1900);
                int eMonth = employee.getBirthDate().getMonth() + 1;
                int eDate = employee.getBirthDate().getDate();

                String sMonth = "" + eMonth;
                if (sMonth.length() == 1) {
                    sMonth = "0" + sMonth;
                }
                String sDate = "" + eDate;
                if (sDate.length() == 1) {
                    sDate = "0" + sDate;
                }

                String digitEmpNum = "";
                digitEmpNum = employee.getEmployeeNum().substring((employee.getEmployeeNum().length() - 3), employee.getEmployeeNum().length());
                String newEmpPin = "";
                newEmpPin = "" + eYear + "" + sMonth + "" + sDate + "" + digitEmpNum;
                employee.setEmpPin(newEmpPin);

            }
            pstEmployee.setString(FLD_EMP_PIN, employee.getEmpPin());
            pstEmployee.setString(FLD_EMPLOYEE_NUM, employee.getEmployeeNum());
            pstEmployee.setLong(FLD_COUNT_IDX, count);
            pstEmployee.setString(FLD_NO_KK, employee.getNoKK());
            //added by dewok 20190610
            pstEmployee.setString(FLD_NAMA_PEMILIK_REKENING, employee.getNamaPemilikRekening());
            pstEmployee.setString(FLD_CABANG_BANK, employee.getCabangBank());
            pstEmployee.setString(FLD_KODE_BANK, employee.getKodeBank());
            pstEmployee.setDate(FLD_TANGGAL_PAJAK_TERDAFTAR, employee.getTanggalPajakTerdaftar());
            pstEmployee.setString(FLD_NAMA_BANK, employee.getNamaBank());
            pstEmployee.setString(FLD_ADDRESS_ID_CARD, employee.getAddressIdCard());
            
            pstEmployee.insert();
            employee.setOID(pstEmployee.getlong(FLD_EMPLOYEE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
        return employee.getOID();
    }

    public static long updateExc(Employee employee) throws DBException {
        try {
            if (employee.getOID() != 0) {
                PstEmployee pstEmployee = new PstEmployee(employee.getOID());

                pstEmployee.setLong(FLD_DEPARTMENT_ID, employee.getDepartmentId());
                pstEmployee.setLong(FLD_POSITION_ID, employee.getPositionId());
                pstEmployee.setLong(FLD_SECTION_ID, employee.getSectionId());
                pstEmployee.setString(FLD_EMPLOYEE_NUM, employee.getEmployeeNum());
                pstEmployee.setLong(FLD_EMP_CATEGORY_ID, employee.getEmpCategoryId());
                pstEmployee.setLong(FLD_LEVEL_ID, employee.getLevelId());
                pstEmployee.setString(FLD_FULL_NAME, employee.getFullName());
                pstEmployee.setString(FLD_ADDRESS, employee.getAddress());
                pstEmployee.setString(FLD_PHONE, employee.getPhone());
                pstEmployee.setString(FLD_HANDPHONE, employee.getHandphone());
                pstEmployee.setInt(FLD_POSTAL_CODE, employee.getPostalCode());
                pstEmployee.setInt(FLD_SEX, employee.getSex());
                pstEmployee.setString(FLD_BIRTH_PLACE, employee.getBirthPlace());
                pstEmployee.setDate(FLD_BIRTH_DATE, employee.getBirthDate());
                pstEmployee.setLong(FLD_RELIGION_ID, employee.getReligionId());
                pstEmployee.setString(FLD_BLOOD_TYPE, employee.getBloodType());
                pstEmployee.setString(FLD_ASTEK_NUM, employee.getAstekNum());
                pstEmployee.setDate(FLD_ASTEK_DATE, employee.getAstekDate());
                pstEmployee.setLong(FLD_MARITAL_ID, employee.getMaritalId());
                pstEmployee.setLong(FLD_TAX_MARITAL_ID, employee.getTaxMaritalId());
                pstEmployee.setLong(FLD_LOCKER_ID, employee.getLockerId());
                pstEmployee.setDate(FLD_COMMENCING_DATE, employee.getCommencingDate());
                pstEmployee.setInt(FLD_RESIGNED, employee.getResigned());
                pstEmployee.setDate(FLD_RESIGNED_DATE, employee.getResignedDate());
                pstEmployee.setString(FLD_BARCODE_NUMBER, employee.getBarcodeNumber());
                pstEmployee.setLong(FLD_RESIGNED_REASON_ID, employee.getResignedReasonId());
                pstEmployee.setString(FLD_RESIGNED_DESC, employee.getResignedDesc());
                pstEmployee.setDouble(FLD_BASIC_SALARY, employee.getBasicSalary());
                pstEmployee.setboolean(FLD_ASSIGN_TO_ACCOUNTING, employee.getIsAssignToAccounting());
                pstEmployee.setLong(FLD_DIVISION_ID, employee.getDivisionId());
                //khusus untuk intimas menampilkan pembawa
                pstEmployee.setString(FLD_CURIER, employee.getCurier());
                /*penambahan kolom sehubungan dengan payroll
                 *Updated By Yunny
                 */
                pstEmployee.setString(FLD_INDENT_CARD_NR, employee.getIndentCardNr());
                pstEmployee.setDate(FLD_INDENT_CARD_VALID_TO, employee.getIndentCardValidTo());
                pstEmployee.setString(FLD_TAX_REG_NR, employee.getTaxRegNr());
                pstEmployee.setString(FLD_ADDRESS_FOR_TAX, employee.getAddressForTax());
                pstEmployee.setInt(FLD_NATIONALITY_TYPE, employee.getNationalityType());
                pstEmployee.setString(FLD_EMAIL_ADDRESS, employee.getEmailAddress());
                pstEmployee.setDate(FLD_CATEGORY_DATE, employee.getCategoryDate());
                pstEmployee.setInt(FLD_LEAVE_STATUS, employee.getLeaveStatus());

                pstEmployee.setLong(FLD_RACE, employee.getRace());

                //hard rock
                pstEmployee.setString(FLD_ADDRESS_PERMANENT, employee.getAddressPermanent());
                pstEmployee.setString(FLD_PHONE_EMERGENCY, employee.getPhoneEmergency());

                pstEmployee.setLong(FLD_COMPANY_ID, employee.getCompanyId());
                //Gede_15Nov2011{
                pstEmployee.setString(FLD_FATHER, employee.getFather());
                pstEmployee.setString(FLD_MOTHER, employee.getMother());
                pstEmployee.setString(FLD_PARENTS_ADDRESS, employee.getParentsAddress());
                pstEmployee.setString(FLD_NAME_EMG, employee.getNameEmg());
                pstEmployee.setString(FLD_PHONE_EMG, employee.getPhoneEmg());
                pstEmployee.setString(FLD_ADDRESS_EMG, employee.getAddressEmg());//}
                //Gede_27Nov2011{
                pstEmployee.setLong(FLD_HOD_EMPLOYEE_ID, employee.getHodEmployeeId());//}
                // add by Kartika 1 dec 2011
                pstEmployee.setLong(FLD_ADDR_COUNTRY_ID, employee.getAddrCountryId());
                pstEmployee.setLong(FLD_ADDR_PROVINCE_ID, employee.getAddrProvinceId());
                pstEmployee.setLong(FLD_ADDR_REGENCY_ID, employee.getAddrRegencyId());
                pstEmployee.setLong(FLD_ADDR_SUBREGENCY_ID, employee.getAddrSubRegencyId());
                pstEmployee.setLong(FLD_ADDR_DISTRICT_ID, employee.getAddrDistrictId());
                pstEmployee.setLong(FLD_ADDR_PMNT_COUNTRY_ID, employee.getAddrPmntCountryId());
                pstEmployee.setLong(FLD_ADDR_PMNT_PROVINCE_ID, employee.getAddrPmntProvinceId());
                pstEmployee.setLong(FLD_ADDR_PMNT_REGENCY_ID, employee.getAddrPmntRegencyId());
                pstEmployee.setLong(FLD_ADDR_PMNT_SUBREGENCY_ID, employee.getAddrPmntSubRegencyId());
                pstEmployee.setLong(FLD_ADDR_PMNT_DISTRICT_ID, employee.getAddrPmntDistrictId());

                pstEmployee.setString(FLD_ID_CARD_ISSUED_BY, employee.getIndentCardIssuedBy());
                pstEmployee.setDate(FLD_ID_CARD_BIRTH_DATE, employee.getIndentCardBirthDate());

                pstEmployee.setString(FLD_NO_REKENING, employee.getNoRekening());
                pstEmployee.setLong(FLD_GRADE_LEVEL_ID, employee.getGradeLevelId());
                pstEmployee.setInt(FLD_COUNT_IDX, employee.getCountIdx());
                //priska
                pstEmployee.setLong(FLD_LOCATION_ID, employee.getLocationId());
                pstEmployee.setDate(FLD_END_CONTRACT, employee.getEnd_contract());
                pstEmployee.setLong(FLD_WORK_ASSIGN_COMPANY_ID, employee.getWorkassigncompanyId());
                pstEmployee.setLong(FLD_WORK_ASSIGN_DIVISION_ID, employee.getWorkassigndivisionId());
                pstEmployee.setLong(FLD_WORK_ASSIGN_DEPARTMENT_ID, employee.getWorkassigndepartmentId());
                pstEmployee.setLong(FLD_WORK_ASSIGN_SECTION_ID, employee.getWorkassignsectionId());
                pstEmployee.setLong(FLD_WORK_ASSIGN_POSITION_ID, employee.getWorkassignpositionId());
                pstEmployee.setString(FLD_ID_CARD_TYPE, employee.getIdcardtype());
                pstEmployee.setString(FLD_NPWP, employee.getNpwp());
                pstEmployee.setString(FLD_BPJS_NO, employee.getBpjs_no());
                pstEmployee.setDate(FLD_BPJS_DATE, employee.getBpjs_date());
                pstEmployee.setString(FLD_SHIO, employee.getShio());
                pstEmployee.setString(FLD_ELEMEN, employee.getElemen());
                pstEmployee.setString(FLD_IQ, employee.getIq());
                pstEmployee.setString(FLD_EQ, employee.getEq());
                // Add Field by Hendra McHen 2015-01-09
                pstEmployee.setDate(FLD_PROBATION_END_DATE, employee.getProbationEndDate());
                pstEmployee.setInt(FLD_STATUS_PENSIUN_PROGRAM, employee.getStatusPensiunProgram());
                pstEmployee.setDate(FLD_START_DATE_PENSIUN, employee.getStartDatePensiun());
                pstEmployee.setInt(FLD_PRESENCE_CHECK_PARAMETER, employee.getPresenceCheckParameter());
                pstEmployee.setString(FLD_MEDICAL_INFO, employee.getMedicalInfo());
                /* Add Field by Hendra Putu | 2015-04-24 */
                pstEmployee.setInt(FLD_DANA_PENDIDIKAN, employee.getDanaPendidikan());
                //priska 20150731
                pstEmployee.setLong(FLD_PAYROLL_GROUP, employee.getPayrollGroup());
                pstEmployee.setLong(FLD_PROVIDER_ID, employee.getProviderID());// by kartika 2015-09-16
                //priska 20151104
                pstEmployee.setInt(FLD_MEMBER_OF_KESEHATAN, employee.getMemOfBpjsKesahatan());
                pstEmployee.setInt(FLD_MEMBER_OF_KETENAGAKERJAAN, employee.getMemOfBpjsKetenagaKerjaan());

                //update by priska 20151102
                int autoPinOk = 0;
                try {
                    autoPinOk = Integer.valueOf(PstSystemProperty.getValueByName("AKTIF_AUTO_PIN"));
                } catch (Exception e) {
                }
                if (autoPinOk == 1) {
                    int eYear = (employee.getBirthDate().getYear() + 1900);
                    int eMonth = employee.getBirthDate().getMonth() + 1;
                    int eDate = employee.getBirthDate().getDate();

                    String sMonth = "" + eMonth;
                    if (sMonth.length() == 1) {
                        sMonth = "0" + sMonth;
                    }
                    String sDate = "" + eDate;
                    if (sDate.length() == 1) {
                        sDate = "0" + sDate;
                    }

                    String digitEmpNum = "";
                    digitEmpNum = employee.getEmployeeNum().substring((employee.getEmployeeNum().length() - 3), employee.getEmployeeNum().length());
                    String newEmpPin = "";
                    newEmpPin = "" + eYear + "" + sMonth + "" + sDate + "" + digitEmpNum;
                    employee.setEmpPin(newEmpPin);

                }
                pstEmployee.setString(FLD_EMP_PIN, employee.getEmpPin());
                pstEmployee.setString(FLD_NO_KK, employee.getNoKK());
                //added by dewok 20190610
                pstEmployee.setString(FLD_NAMA_PEMILIK_REKENING, employee.getNamaPemilikRekening());
                pstEmployee.setString(FLD_CABANG_BANK, employee.getCabangBank());
                pstEmployee.setString(FLD_KODE_BANK, employee.getKodeBank());
                pstEmployee.setDate(FLD_TANGGAL_PAJAK_TERDAFTAR, employee.getTanggalPajakTerdaftar());
                pstEmployee.setString(FLD_NAMA_BANK, employee.getNamaBank());
                pstEmployee.setString(FLD_ADDRESS_ID_CARD, employee.getAddressIdCard());
                
                pstEmployee.update();
                return employee.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static Employee fetchEmployeeNumMax() {
        Employee employee = new Employee();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + "  ORDER BY " + fieldNames[FLD_COUNT_IDX] + " DESC LIMIT 0,1";
            dbrs = DBHandler.execQueryResult(sql);

            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                resultToObject(rs, employee);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
            return employee;
        }

    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstEmployee pstEmployee = new PstEmployee(oid);
            pstEmployee.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmployee(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 1000, "", "");
    }

    /**
     * create by devin 2014-04-09
     *
     * @param limitStart
     * @param recordToGet
     * @param order
     * @param empNumber
     * @param fullName
     * @param departementId
     * @param section
     * @param oidCompany
     * @param oidDivision
     * @return
     */
    public static Vector listEmp(int limitStart, int recordToGet, String order, String empNumber, String fullName, long departementId, long section, long oidCompany, long oidDivision, int resign) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ","
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ","
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + ","
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ","
                    + " DIVX." + PstDivision.fieldNames[PstDivision.FLD_DIVISION] + ","
                    + " DPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ","
                    + " SEC." + PstSection.fieldNames[PstSection.FLD_SECTION] + ","
                    + " EMPCAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=DPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")"
                    + " INNER JOIN " + PstDivision.TBL_HR_DIVISION + " AS DIVX ON (EMP." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "=DIVX." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + ")"
                    + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " AS SEC ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + ")"
                    + " INNER JOIN " + PstCompany.TBL_HR_COMPANY + " AS CMP ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + "=CMP." + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] + ")"
                    + " INNER JOIN " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " AS EMPCAT ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = EMPCAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID] + ")"
                    + " WHERE (1=1) ";
            if (empNumber != null && empNumber.length() > 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + "=\"" + empNumber + "\"";
            }
            if (fullName != null && fullName.length() > 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + "=\"" + fullName + "\"";
            }
            if (departementId != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=\"" + departementId + "\"";
            }
            if (section != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=\"" + section + "\"";
            }
            //update by devin 2014-04-10
            if (resign == 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=\"" + resign + "\"";
            }

            //update by devin 2014-02-18
            if (oidCompany > 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + oidCompany;
            }
            if (oidDivision > 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + oidDivision;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                //resultToObject(rs, employee);
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static Hashtable hlist(int limitStart, int recordToGet, String whereClause, String order) {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.put("" + employee.getEmployeeNum(), employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    public static Vector listEmployeeApprovalTopLink(int limitStart, int recordToGet, String positionId, String order, long SectionId, long DepartmentId, long DivisionId) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;

            sql = sql + " WHERE " + positionId;

            if (SectionId != 0 && SectionId > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + SectionId;
            }
            if (DepartmentId != 0 && DepartmentId > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + DepartmentId;
            }
            if (DivisionId != 0 && DivisionId > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + DivisionId;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    /*
     * SELECT e.EMPLOYEE_NUM, e.FULL_NAME, p.POSITION, e.ADDRESS, e.ADDRESS_PERMANENT, e.phone, e.NPWP, e.INDENT_CARD_NR,
     e.BIRTH_PLACE, e.BIRTH_DATE, e.SEX, e.COMMENCING_DATE, e.RESIGNED_DATE, l.LEVEL
     FROM hr_employee AS e
     INNER JOIN hr_position AS p
     ON
     e.POSITION_ID = p.POSITION_ID
     INNER JOIN hr_level AS l
     ON
     e.LEVEL_ID = l.LEVEL_ID
     WHERE l.LEVEL='KADIV' && e.COMMENCING_DATE LIKE '%2015%'
     */
    public static Vector listEmployeeKadiv(int whereYear) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT e.EMPLOYEE_ID, e.EMPLOYEE_NUM, e.FULL_NAME, p.POSITION, e.ADDRESS, e.ADDRESS_PERMANENT, e.phone, e.NPWP, e.INDENT_CARD_NR,"
                    + "e.BIRTH_PLACE, e.BIRTH_DATE, e.SEX, e.COMMENCING_DATE, e.RESIGNED_DATE, l.LEVEL, d.DEPARTMENT FROM " + TBL_HR_EMPLOYEE
                    + " AS e INNER JOIN hr_position AS p ON e.POSITION_ID = p.POSITION_ID INNER JOIN hr_level AS l ON e.LEVEL_ID = l.LEVEL_ID"
                    + " INNER JOIN hr_department AS d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID"
                    + " WHERE l.LEVEL='KADIV' && e.COMMENCING_DATE LIKE '%" + whereYear + "%'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObjectKadiv(rs, employee);
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    //lkpbu 802
    /*
     * SELECT e.EMPLOYEE_NUM, g.COMPANY, e.COMMENCING_DATE, e.RESIGNED_DATE, p.POSITION, d.DEPARTMENT
     FROM hr_employee AS e
     INNER JOIN pay_general AS g
     ON
     e.COMPANY_ID = g.gen_id
     INNER JOIN hr_department AS d
     ON
     e.DEPARTMENT_ID = d.DEPARTMENT_ID
     INNER JOIN hr_position AS p
     ON
     e.POSITION_ID = p.POSITION_ID
     * where e.FULL_NAME LIKE '%dedy%' || e.EMPLOYEE_NUM LIKE '%1352%' || e.COMPANY_ID LIKE '%1352%' || e.DIVISION_ID LIKE '%%'|| e.DEPARTMENT_ID LIKE '%%' || e.LEVEL_ID LIKE '%%'
     */

    public static Vector listEmployeCompany(String name, String payroll, long company, long divisi, long department, long level) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT e.EMPLOYEE_NUM, g.COMPANY, e.COMMENCING_DATE, e.RESIGNED_DATE, p.POSITION, d.DEPARTMENT"
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " AS e INNER JOIN pay_general AS g ON e.COMPANY_ID = g.gen_id INNER JOIN hr_department AS d ON e.DEPARTMENT_ID = d.DEPARTMENT_ID"
                    + " INNER JOIN hr_position AS p ON e.POSITION_ID = p.POSITION_ID";
            if (!name.equals("")) {
                if (!payroll.equals("")) {
                    sql = sql + " WHERE e.FULL_NAME LIKE '%" + name + "%' && e.EMPLOYEE_NUM LIKE '%" + payroll + "%'";
                } else if (company != 0) {
                    sql = sql + " WHERE e.FULL_NAME LIKE '%" + name + "%' && e.COMPANY_ID='" + company + "'";
                } else if (divisi != 0) {
                    sql = sql + " WHERE e.FULL_NAME LIKE '%" + name + "%' && e.DIVISION_ID='" + divisi + "'";
                } else if (department != 0) {
                    sql = sql + " WHERE e.FULL_NAME LIKE '%" + name + "%' && e.DEPARTMENT_ID='" + department + "'";
                } else if (level != 0) {
                    sql = sql + " WHERE e.FULL_NAME LIKE '%" + name + "%' && e.LEVEL_ID='" + level + "'";
                } else {
                    sql = sql + " WHERE e.FULL_NAME LIKE '%" + name + "%'";
                }
            } else if (!payroll.equals("")) {
                if (company != 0) {
                    sql = sql + " WHERE e.EMPLOYEE_NUM LIKE '%" + payroll + "%' && e.COMPANY_ID='" + company + "'";
                } else if (divisi != 0) {
                    sql = sql + " WHERE e.EMPLOYEE_NUM LIKE '%" + payroll + "%' && e.DIVISION_ID='" + divisi + "'";
                } else if (department != 0) {
                    sql = sql + " WHERE e.EMPLOYEE_NUM LIKE '%" + payroll + "%' && e.DEPARTMENT_ID='" + department + "'";
                } else if (level != 0) {
                    sql = sql + " WHERE e.EMPLOYEE_NUM LIKE '%" + payroll + "%' && e.LEVEL_ID='" + level + "'";
                } else {
                    sql = sql + " WHERE e.EMPLOYEE_NUM LIKE '%" + payroll + "%'";
                }
            } else if (company != 0) {
                if (divisi != 0) {
                    sql = sql + " WHERE e.COMPANY_ID='" + company + "' && e.DIVISION_ID='" + divisi + "'";
                } else if (department != 0) {
                    sql = sql + " WHERE e.COMPANY_ID='" + company + "' && e.DEPARTMENT_ID='" + department + "'";
                } else if (level != 0) {
                    sql = sql + " WHERE e.COMPANY_ID='" + company + "' && e.LEVEL_ID='" + level + "'";
                } else {
                    sql = sql + " WHERE e.COMPANY_ID='" + company + "'";
                }
            } else if (divisi != 0) {
                if (department != 0) {
                    sql = sql + " WHERE e.DIVISION_ID='" + divisi + "' && e.DEPARTMENT_ID='" + department + "'";
                } else if (level != 0) {
                    sql = sql + " WHERE e.DIVISION_ID='" + divisi + "' && e.LEVEL_ID='" + level + "'";
                } else {
                    sql = sql + " WHERE e.DIVISION_ID='" + divisi + "'";
                }
            } else if (department != 0) {
                if (level != 0) {
                    sql = sql + " WHERE e.DEPARTMENT_ID='" + department + "' && e.LEVEL_ID='" + level + "'";
                } else {
                    sql = sql + " WHERE e.DEPARTMENT_ID='" + department + "'";
                }
            } else if (level != 0) {
                sql = sql + " WHERE e.LEVEL_ID='" + level + "'";
            } else {

            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObjectCompany(rs, employee);
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    /**
     * mecari employee ID
     *
     * @param whereClause
     * @param order
     * @return
     */
    public static String listEmployeeId(String whereClause, String order) {
        String lists = "";
        if (whereClause == null || whereClause.length() == 0) {
            return lists;
        }
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT " + fieldNames[FLD_EMPLOYEE_ID] + " FROM " + TBL_HR_EMPLOYEE + " AS emp "
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " AS pos ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=pos." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                lists = lists + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]) + ",";
            }
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
            return lists;
        }
    }
    //update by devin 2014-02-17

    /**
     * digunakan untuk mencari employee number
     *
     * @return
     */
    public static long getDataNum(long oid) {
        long result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + fieldNames[FLD_EMPLOYEE_NUM] + " FROM " + TBL_HR_EMPLOYEE + " WHERE " + fieldNames[FLD_EMPLOYEE_ID] + " = " + oid;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (Exception exc) {
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /**
     * create by satrya 2014-03-03 keterangan untuk list employee outlet
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static Vector listJoinLocationOutlet(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT DISTINCT HE.* FROM " + TBL_HR_EMPLOYEE + " AS HE "
                    + " LEFT JOIN " + PstEmployeeOutlet.TBL_HR_EMP_OUTLET + " AS eo ON HE." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=eo." + PstEmployeeOutlet.fieldNames[PstEmployeeOutlet.FLD_EMPLOYEE_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + " GROUP BY HE." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    /**
     * create by satrya 2013-07-04
     *
     * @return
     */
    public static Hashtable hashListEmployee() {
        Hashtable hashListEMp = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                hashListEMp.put(employee.getOID(), employee);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashListEMp;
    }

    //priska menambahkan 20151023
    public static Hashtable hashListEmployeeFullname() {
        Hashtable hashListEMp = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                hashListEMp.put(employee.getOID(), employee.getFullName());
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashListEMp;
    }

    /**
     * list Employee hashtable
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @return
     */
    public static Hashtable hashListEmployee(int limitStart, int recordToGet, String whereClause) {
        DBResultSet dbrs = null;
        Hashtable hashListEmployee = new Hashtable();
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + " ORDER BY " + fieldNames[FLD_DEPARTMENT_ID] + "," + fieldNames[FLD_EMPLOYEE_NUM] + " ASC ";
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            SessEmployee sessEmployee = new SessEmployee();
            long prevDepartementId = 0;
            while (rs.next()) {

                EmployeeMinimalis employeeMinimalis = new EmployeeMinimalis();
                long oidDepartment = rs.getLong(fieldNames[FLD_DEPARTMENT_ID]);
                if (prevDepartementId != oidDepartment) {
                    sessEmployee = new SessEmployee();
                    hashListEmployee.put(oidDepartment, sessEmployee);
                }

                resultToObjectEmployeeMinimalis(rs, employeeMinimalis);
                prevDepartementId = employeeMinimalis.getDepartmentId();
                sessEmployee.addObjEmployee(employeeMinimalis);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error pst EMployee add employee" + e);
        } finally {
            DBResultSet.close(dbrs);
            return hashListEmployee;
        }
    }

    /**
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @return
     */
    public static Hashtable hashListEmployeeSection(int limitStart, int recordToGet, String whereClause) {
        DBResultSet dbrs = null;
        Hashtable hashListEmployee = new Hashtable();
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + " ORDER BY " + fieldNames[FLD_SECTION_ID] + "," + fieldNames[FLD_EMPLOYEE_NUM] + " ASC ";
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            SessEmployee sessEmployee = new SessEmployee();
            long prevSectionId = 0;
            while (rs.next()) {

                EmployeeMinimalis employeeMinimalis = new EmployeeMinimalis();

                long oidSection = rs.getLong(fieldNames[FLD_SECTION_ID]);
                if (oidSection != 0 && prevSectionId != oidSection) {
                    sessEmployee = new SessEmployee();
                    hashListEmployee.put(oidSection, sessEmployee);
                }

                resultToObjectEmployeeMinimalis(rs, employeeMinimalis);
                prevSectionId = employeeMinimalis.getSectionId();
                sessEmployee.addObjEmployee(employeeMinimalis);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error pst EMployee add employee" + e);
        } finally {
            DBResultSet.close(dbrs);
            return hashListEmployee;
        }
    }

    /**
     * create by satrya 2013-04-22
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static String getSEmployeeId(int limitStart, int recordToGet, String whereClause, String order) {

        DBResultSet dbrs = null;
        String sEmployeeID = "";
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " AS HE ";

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                sEmployeeID = sEmployeeID + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]) + ",";
            }
            if (sEmployeeID != null && sEmployeeID.length() > 0) {
                sEmployeeID = sEmployeeID.substring(0, sEmployeeID.length() - 1);
            }
            rs.close();
            return sEmployeeID;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return sEmployeeID;
    }

    /**
     * Satrya Ramayu untuk filer obj Emp
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static EmployeeSrcRekapitulasiAbs getEmployeeFilter(int limitStart, int recordToGet, String whereClause, String order) {

        DBResultSet dbrs = null;
        String sEmployeeID = "";
        String sCompanyId = "";
        String sDivisionId = "";
        String sDepartementId = "";
        String sSectionId = "";
        EmployeeSrcRekapitulasiAbs employeeSrcRekapitulasiAbs = new EmployeeSrcRekapitulasiAbs();
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " AS HE ";

            if (whereClause != null && whereClause.length() > 0) {

                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                sEmployeeID = sEmployeeID + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]) + ",";
                sCompanyId = sCompanyId + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]) + ",";
                sDivisionId = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]) != 0 ? (sDivisionId + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]) + ",") : "";
                sDepartementId = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]) != 0 ? (sDepartementId + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]) + ",") : "";
                sSectionId = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]) != 0 ? (sSectionId + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]) + ",") : "";
            }
            if (sEmployeeID != null && sEmployeeID.length() > 0) {
                sEmployeeID = sEmployeeID.substring(0, sEmployeeID.length() - 1);
            }
            if (sCompanyId != null && sCompanyId.length() > 0) {
                sCompanyId = sCompanyId.substring(0, sCompanyId.length() - 1);
            }
            if (sDivisionId != null && sDivisionId.length() > 0) {
                sDivisionId = sDivisionId.substring(0, sDivisionId.length() - 1);
            }
            if (sDepartementId != null && sDepartementId.length() > 0) {
                sDepartementId = sDepartementId.substring(0, sDepartementId.length() - 1);
            }
            if (sSectionId != null && sSectionId.length() > 0) {
                sSectionId = sSectionId.substring(0, sSectionId.length() - 1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            employeeSrcRekapitulasiAbs.setCompanyId(sCompanyId);
            employeeSrcRekapitulasiAbs.setDepartmentId(sDepartementId);
            employeeSrcRekapitulasiAbs.setDivisionId(sDivisionId);
            employeeSrcRekapitulasiAbs.setEmpId(sEmployeeID);
            employeeSrcRekapitulasiAbs.setSectionId(sSectionId);
            DBResultSet.close(dbrs);
        }
        return employeeSrcRekapitulasiAbs;
    }

    public static String getSectionIdByEmpId(int limitStart, int recordToGet, String whereClause, String order) {

        DBResultSet dbrs = null;
        String sSectionId = "";

        try {
            String sql = "SELECT DISTINCT SECTION_ID FROM " + TBL_HR_EMPLOYEE + " AS HE ";

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                sSectionId = sSectionId + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]) + ",";

                //  sSectionId = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID])!=0? (sSectionId + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]) + ","):"";
            }

            if (sSectionId != null && sSectionId.length() > 0) {
                sSectionId = sSectionId.substring(0, sSectionId.length() - 1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {

            DBResultSet.close(dbrs);
        }
        return sSectionId;
    }

    /**
     * create by satrya 2014-03-06 keterangan: untuk menampilkan list outlet
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static String getSEmployeeIdOutlet(int limitStart, int recordToGet, String whereClause, String order) {

        DBResultSet dbrs = null;
        String sEmployeeID = "";
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " AS HE ";
            sql = sql + " LEFT JOIN " + PstEmployeeOutlet.TBL_HR_EMP_OUTLET + " AS eo ON HE." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=eo." + PstEmployeeOutlet.fieldNames[PstEmployeeOutlet.FLD_EMPLOYEE_ID];
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + "  GROUP BY HE." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                sEmployeeID = sEmployeeID + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]) + ",";
            }
            if (sEmployeeID != null && sEmployeeID.length() > 0) {
                sEmployeeID = sEmployeeID.substring(0, sEmployeeID.length() - 1);
            }
            rs.close();
            return sEmployeeID;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return sEmployeeID;
    }

    /**
     * keterangan: untuk hashtable emplopyee create by satrya 2014-03-18
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @param order
     * @return
     */
    public static Hashtable listHashEmployee(int limitStart, int recordToGet, String whereClause, String order) {

        DBResultSet dbrs = null;
        Hashtable hashEmployee = new Hashtable();
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " AS HE ";
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                hashEmployee.put("" + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]), employee);
            }

            rs.close();
            return hashEmployee;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashEmployee;
    }

    /**
     * untuk mencari employee ID berdasarkan parameter salary create by satrya
     * 2014-01-31
     *
     * @param limitStart
     * @param recordToGet
     * @param departmentName
     * @param sectionName
     * @param periodId
     * @param levelCode
     * @param searchNrFrom
     * @param searchNrTo
     * @param searchName
     * @param dataStatus
     * @param order
     * @return
     */
    public static String getSEmployeeIdJoinSalary(int limitStart, int recordToGet, long companyId, long divisionId, long departmentName, long sectionName, long periodId, String levelCode, String searchNrFrom, String searchNrTo, String searchName, int dataStatus, int isChekedRadioButtonSearchNr, String searchNr, String order) {

        DBResultSet dbrs = null;
        String sEmployeeID = "";
        try {
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " FROM " + TBL_HR_EMPLOYEE + " AS EMP "
                    + " INNER JOIN " + PstPaySlip.TBL_PAY_SLIP + " AS PAY"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PAY." + PstPaySlip.fieldNames[PstPaySlip.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS LEV"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = LEV." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " AS POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + " = POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];
            String whereClause = " WHERE (1=1) AND ";

            if (departmentName > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + departmentName + " AND ";
            }

            //update by satrya 2014-02-03
            if (companyId > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + companyId + " AND ";
            }

            if (divisionId > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + divisionId + " AND ";
            }

            //System.out.println("department"+srcEmployee.getDepartment());
            if (levelCode != null && levelCode.length() > 0 && !levelCode.equals("0")) {
                whereClause = whereClause + " LEV." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                        + " LIKE '%" + levelCode.trim() + "%' AND ";
            }

            if (sectionName > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + sectionName + " AND ";

            }
            if (periodId != 0) {
                whereClause = whereClause + " PAY." + PstPaySlip.fieldNames[PstPaySlip.FLD_PERIOD_ID]
                        + " = " + periodId + " AND ";

            }

            if (isChekedRadioButtonSearchNr == 1) {
                if (searchNr != null && searchNr != "") {
                    Vector vectNum = logicParser(searchNr);
                    sql = sql + " AND ";
                    if (vectNum != null && vectNum.size() > 0) {
                        sql = sql + " (";
                        for (int i = 0; i < vectNum.size(); i++) {
                            String str = (String) vectNum.get(i);
                            if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                sql = sql + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                        + " LIKE '%" + str.trim() + "%' ";
                            } else {
                                sql = sql + str.trim();
                            }
                        }
                        sql = sql + ")";
                    }

                }
            } else {
                if ((searchNrFrom != null) && (searchNrFrom.length() > 0)) {
                    if (searchNrTo == null || searchNrTo.length() == 0) {
                        searchNrTo = searchNrFrom;
                    }
                    Vector vectNrFrom = logicParser(searchNrFrom);
                    if (vectNrFrom != null && vectNrFrom.size() > 0) {
                        whereClause = whereClause + " (";
                        for (int i = 0; i < vectNrFrom.size(); i++) {
                            String str = (String) vectNrFrom.get(i);
                            if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                        + " BETWEEN '" + searchNrFrom + "' AND '" + searchNrTo + "'";
                            } else {
                                whereClause = whereClause + str.trim();
                            }
                        }
                        whereClause = whereClause + ") AND ";
                    }

                }

            }

            if ((searchName != null) && (searchName.length() > 0)) {
                Vector vectName = logicParser(searchName);
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") AND ";
                }

            }

            if (dataStatus < 2) {
                whereClause = whereClause + " PAY." + PstPaySlip.fieldNames[PstPaySlip.FLD_STATUS]
                        + " = " + dataStatus + " AND ";

            }

            if (whereClause != null && whereClause.length() > 0) {
                whereClause = whereClause.substring(0, whereClause.length() - 4);
                sql = sql + whereClause;
                //sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("list employee list of employee  " + sql);
            while (rs.next()) {
                sEmployeeID = sEmployeeID + rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]) + ",";
            }
            if (sEmployeeID != null && sEmployeeID.length() > 0) {
                sEmployeeID = sEmployeeID.substring(0, sEmployeeID.length() - 1);
            }
            rs.close();
            return sEmployeeID;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return sEmployeeID;
    }

    public static void resultToObject(ResultSet rs, Employee employee) {
        try {
            employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
            employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
            employee.setPositionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));
            employee.setSectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]));
            employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
            employee.setEmpCategoryId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]));
            employee.setLevelId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]));
            employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
            employee.setAddress(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]));
            employee.setPhone(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE]));
            employee.setHandphone(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE]));
            employee.setPostalCode(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE]));
            employee.setSex(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_SEX]));
            employee.setBirthPlace(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]));
            employee.setBirthDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]));
            employee.setReligionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]));
            employee.setBloodType(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]));
            employee.setAstekNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM]));
            employee.setAstekDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE]));
            employee.setMaritalId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]));
            employee.setTaxMaritalId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID]));
            employee.setLockerId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]));
            employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
            employee.setResigned(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]));
            employee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
            employee.setBarcodeNumber(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER]));
            employee.setResignedReasonId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID]));
            employee.setResignedDesc(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC]));
            employee.setBasicSalary(rs.getDouble(PstEmployee.fieldNames[PstEmployee.FLD_BASIC_SALARY]));
            employee.setIsAssignToAccounting(rs.getBoolean(PstEmployee.fieldNames[PstEmployee.FLD_ASSIGN_TO_ACCOUNTING]));
            employee.setDivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]));
            //khusus untuk intimas karena menambah field curier (pembawa)
            employee.setCurier(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_CURIER]));
            /*penambahan kolom sehubungan dengan payroll
             *Updated By Yunny
             */
            employee.setIndentCardNr(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_NR]));
            employee.setIndentCardValidTo(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_VALID_TO]));
            employee.setTaxRegNr(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_TAX_REG_NR]));
            employee.setAddressForTax(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_FOR_TAX]));
            employee.setNationalityType(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_NATIONALITY_TYPE]));
            employee.setEmailAddress(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMAIL_ADDRESS]));
            employee.setCategoryDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_CATEGORY_DATE]));
            employee.setLeaveStatus(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_LEAVE_STATUS]));

            employee.setEmpPin(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN]));
            employee.setRace(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_RACE]));

            employee.setAddressPermanent(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT]));
            employee.setPhoneEmergency(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMERGENCY]));

            employee.setCompanyId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]));
            // add by Kartika 1 dec 2011
            employee.setAddrCountryId(rs.getLong(FLD_ADDR_COUNTRY_ID));
            employee.setAddrProvinceId(rs.getLong(FLD_ADDR_PROVINCE_ID));
            employee.setAddrRegencyId(rs.getLong(FLD_ADDR_REGENCY_ID));
            employee.setAddrSubRegencyId(rs.getLong(FLD_ADDR_SUBREGENCY_ID));
            employee.setAddrDistrictId(rs.getLong(FLD_ADDR_DISTRICT_ID));
            employee.setAddrPmntCountryId(rs.getLong(FLD_ADDR_PMNT_COUNTRY_ID));
            employee.setAddrPmntProvinceId(rs.getLong(FLD_ADDR_PMNT_PROVINCE_ID));
            employee.setAddrPmntRegencyId(rs.getLong(FLD_ADDR_PMNT_REGENCY_ID));
            employee.setAddrPmntSubRegencyId(rs.getLong(FLD_ADDR_PMNT_SUBREGENCY_ID));
            employee.setAddrPmntDistrictId(rs.getLong(FLD_ADDR_PMNT_DISTRICT_ID));
            employee.setIndentCardIssuedBy(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_ISSUED_BY]));
            employee.setIndentCardBirthDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_BIRTH_DATE]));
            employee.setNoRekening(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NO_REKENING]));
            employee.setGradeLevelId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID]));
            employee.setCountIdx(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_COUNT_IDX]));
            //priska 2014-10-30
            employee.setLocationId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID]));
            employee.setEnd_contract(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]));
            employee.setWorkassigncompanyId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_COMPANY_ID]));
            employee.setWorkassigndivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DIVISION_ID]));
            employee.setWorkassigndepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DEPARTMENT_ID]));
            employee.setWorkassignsectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_SECTION_ID]));
            employee.setWorkassignpositionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_POSITION_ID]));
            employee.setIdcardtype(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_TYPE]));
            employee.setNpwp(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NPWP]));
            employee.setBpjs_no(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BPJS_NO]));
            employee.setBpjs_date(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BPJS_DATE]));
            employee.setShio(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_SHIO]));
            employee.setElemen(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ELEMEN]));
            employee.setIq(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_IQ]));
            employee.setEq(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EQ]));

            // Add Field by Hendra McHen 2015-01-09
            employee.setProbationEndDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_PROBATION_END_DATE]));
            employee.setStatusPensiunProgram(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_STATUS_PENSIUN_PROGRAM]));
            employee.setStartDatePensiun(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_START_DATE_PENSIUN]));
            employee.setPresenceCheckParameter(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_PRESENCE_CHECK_PARAMETER]));
            employee.setMedicalInfo(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_MEDICAL_INFO]));
            /* Add Field by Hendra Putu | 2015-04-24 */
            employee.setDanaPendidikan(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_DANA_PENDIDIKAN]));
            //add by priska 20150904
            employee.setPayrollGroup(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]));
            employee.setProviderID(rs.getLong(fieldNames[FLD_PROVIDER_ID]));// by kartika 2015-09-16
            employee.setMemOfBpjsKesahatan(rs.getInt(fieldNames[FLD_MEMBER_OF_KESEHATAN])); //add by priska 20150904
            employee.setMemOfBpjsKetenagaKerjaan(rs.getInt(fieldNames[FLD_MEMBER_OF_KETENAGAKERJAAN])); //add by priska 20150904
            employee.setNoKK(rs.getString(fieldNames[FLD_NO_KK]));
            //added by dewok 20190610
            employee.setNamaPemilikRekening(rs.getString(fieldNames[FLD_NAMA_PEMILIK_REKENING]));
            employee.setCabangBank(rs.getString(fieldNames[FLD_CABANG_BANK]));
            employee.setKodeBank(rs.getString(fieldNames[FLD_KODE_BANK]));
            employee.setTanggalPajakTerdaftar(rs.getDate(fieldNames[FLD_TANGGAL_PAJAK_TERDAFTAR]));
            employee.setNamaBank(rs.getString(fieldNames[FLD_NAMA_BANK]));
            employee.setAddressIdCard(rs.getString(fieldNames[FLD_ADDRESS_ID_CARD]));
            
        } catch (Exception e) {
        }
    }

    public static void resultToObjectKadiv(ResultSet rs, Employee employee) {
        try {
            employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
            employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
            employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
            employee.setPositionName(rs.getString(PstPosition.fieldNames[PstPosition.FLD_POSITION]));
            employee.setAddress(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]));
            employee.setAddressPermanent(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT]));
            employee.setPhone(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE]));
            employee.setNpwp(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NPWP]));
            employee.setIndentCardNr(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_NR]));
            employee.setBirthPlace(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]));
            employee.setBirthDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]));
            employee.setSex(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_SEX]));
            employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
            employee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
            employee.setLevelName(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]));
            employee.setDepartmentName(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

        } catch (Exception e) {
        }
    }

    public static void resultToObjectCompany(ResultSet rs, Employee employee) {
        try {
            employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
            employee.setCompanyName(rs.getString(PstCompany.fieldNames[PstCompany.FLD_COMPANY]));
            employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
            employee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
            employee.setPositionName(rs.getString(PstPosition.fieldNames[PstPosition.FLD_POSITION]));
            employee.setDepartmentName(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

        } catch (Exception e) {
        }
    }

    /**
     * update by ramayu 2014-09-17
     *
     * @param rs
     * @param employee
     */
    public static void resultToObjectEmployeeMinimalis(ResultSet rs, EmployeeMinimalis employee) {
        try {
            employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
            employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
            employee.setPositionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));
            employee.setSectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]));
            employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
            employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
            employee.setDivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]));
            employee.setCompanyId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]));
            employee.setEmpCategory(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]));

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long employeeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = '" + employeeId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ") FROM " + TBL_HR_EMPLOYEE;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static int getCountEmployeeHaveDefaultSchedule(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(DISTINCT(e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ")) "
                    + " FROM " + TBL_HR_EMPLOYEE + " e "
                    + " INNER JOIN " + PstDefaultSchedule.TBL_HR_DEFAULT_SCHEDULE
                    + " s ON s." + PstDefaultSchedule.fieldNames[PstDefaultSchedule.FLD_EMPLOYEE_ID]
                    + "=e." + fieldNames[FLD_EMPLOYEE_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static Vector getEmployeeHaveDefaultSchedule(int limitStart, int recordToGet, String whereClause, String order) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT(e." + fieldNames[FLD_EMPLOYEE_ID] + "), "
                    + fieldNames[FLD_EMPLOYEE_NUM] + " , " + fieldNames[FLD_FULL_NAME]
                    //update by satrya 2012-10-09
                    + " , " + fieldNames[FLD_RELIGION_ID]
                    + ", " + fieldNames[FLD_PRESENCE_CHECK_PARAMETER]
                    + " "
                    + " FROM " + TBL_HR_EMPLOYEE + " e "
                    + " INNER JOIN " + PstDefaultSchedule.TBL_HR_DEFAULT_SCHEDULE
                    + " s ON s." + PstDefaultSchedule.fieldNames[PstDefaultSchedule.FLD_EMPLOYEE_ID]
                    + "=e." + fieldNames[FLD_EMPLOYEE_ID];

            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector list = new Vector();
            int count = 0;
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setOID(rs.getLong(fieldNames[FLD_EMPLOYEE_ID]));
                emp.setEmployeeNum(rs.getString(fieldNames[FLD_EMPLOYEE_NUM]));
                emp.setFullName(rs.getString(fieldNames[FLD_FULL_NAME]));
                //update by satrya 2012-10-09
                emp.setReligionId(rs.getLong(fieldNames[FLD_RELIGION_ID]));
                emp.setPresenceCheckParameter(rs.getInt(fieldNames[FLD_PRESENCE_CHECK_PARAMETER]));
                list.add(emp);
            }

            rs.close();
            return list;
        } catch (Exception e) {
            return null;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static int getCount(long OIDHRD) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ") FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + OIDHRD;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }

            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static long getEmployeeIdByNum(String empNum) {
        DBResultSet dbrs = null;
        long empOid = 0;
        try {
            String sql = "SELECT " + fieldNames[FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = '" + empNum + "'";

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                empOid = rs.getLong(fieldNames[FLD_EMPLOYEE_ID]);
            }

            rs.close();
            return empOid;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return empOid;
        }
    }

    public static long getEmployeeIdByLikeNum(String empNum) {
        DBResultSet dbrs = null;
        long empOid = 0;
        try {
            String sql = "SELECT " + fieldNames[FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE '%" + empNum + "'";

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                empOid = rs.getLong(fieldNames[FLD_EMPLOYEE_ID]);
            }

            rs.close();
            return empOid;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return empOid;
        }
    }

    public static long getEmployeeIdLikeNum(String empNum) {
        DBResultSet dbrs = null;
        long empOid = 0;
        try {
            String sql = "SELECT " + fieldNames[FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE '%" + empNum + "%'";

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                empOid = rs.getLong(fieldNames[FLD_EMPLOYEE_ID]);
            }

            rs.close();
            return empOid;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return empOid;
        }
    }

    public static Employee getEmployeeByNum(String empNum) {
        DBResultSet dbrs = null;
        //long empOid = 0;
        Employee employee = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = '" + empNum + "'";

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                employee = new Employee();
                resultToObject(rs, employee);
            }

            rs.close();
            //return empOid;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return employee;
        }
    }

    /**
     * Keterangan: untuk mencari employee berdasarkan Name create by satrya
     * 2013-07-01
     *
     * @param fullName
     * @return
     */
    public static Employee getEmployeeByFullName(String fullName) {
        DBResultSet dbrs = null;
        //long empOid = 0;
        Employee employee = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " like '%" + fullName + "%'";

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                employee = new Employee();
                resultToObject(rs, employee);
            }

            rs.close();
            //return empOid;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return employee;
        }
    }

    public static Employee getEmployeeByPositionId(long positionId) {
        DBResultSet dbrs = null;
        //long empOid = 0;
        Employee employee = null;
        try {
            String sql = "SELECT * "
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = '" + positionId + "'";

            //System.out.println("sql : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            employee = new Employee();
            while (rs.next()) {
                resultToObject(rs, employee);
            }

            rs.close();
            //return empOid;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return employee;
        }
    }

    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    Employee employee = (Employee) list.get(ls);
                    if (oid == employee.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }

        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }

        return cmd;
    }

    public static void updateBarcode(long employeeId, String barcode) {
        DBResultSet dbrs = null;
        //boolean result = false;
        //String barcode = (barcodeNumber.equals(null)) ? "null" : barcodeNumber;

        try {
            String sql = "";
            if (barcode != null) {
                sql = " UPDATE " + TBL_HR_EMPLOYEE + " SET "
                        + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = '"
                        + barcode + "' WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;
            } else {
                sql = " UPDATE " + TBL_HR_EMPLOYEE + " SET "
                        + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = NULL"
                        + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;
            }

            //dbrs = DBHandler.execQueryResult(sql);
            int status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();
            System.out.println("\tupdateBarcode : " + sql);
            //while(rs.next()) { result = true; }
            //rs.close();
        } catch (Exception e) {
            System.err.println("\tupdateBarcode error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }

    /**
     * @param employeeId
     * @param barcode
     * @param empPin
     * @param pinValidation
     */
    public static void updateBarcodeAndPin(long employeeId, String barcode, String empPin) {
        DBResultSet dbrs = null;
        try {
            String sql = "";
            String strUpdate = "";

            // barcode tidak kosong  
            if (barcode != null && barcode.length() > 0) {
                sql = "UPDATE " + TBL_HR_EMPLOYEE
                        + " SET " + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER]
                        + " = \"" + barcode + "\"";

                if (empPin != null && empPin.length() > 0) {
                    sql = sql + ", " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN]
                            + " = \"" + empPin + "\"";
                }

                sql = sql + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " = " + employeeId;
            } // barcode kosong
            else {
                if (empPin != null && empPin.length() > 0) {
                    sql = "UPDATE " + TBL_HR_EMPLOYEE
                            + " SET " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_PIN]
                            + " = \"" + empPin + "\""
                            + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                            + " = " + employeeId;
                }
            }

            if (sql != null && sql.length() > 0) {
                int status = DBHandler.execUpdate(sql);
//                System.out.println("\tupdateBarcode : " + sql);
            }
        } catch (Exception e) {
            System.err.println("\tupdateBarcode error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static void deleteBarcode() {
        DBResultSet dbrs = null;
        //boolean result = false;

        try {
            String sql = " UPDATE " + TBL_HR_EMPLOYEE
                    + " SET " + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = NULL ";

            int status = DBHandler.execUpdate(sql);
        } catch (Exception e) {
            System.err.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static long getEmployeeByBarcode(String barcode) {

        long result = 0;
        DBResultSet dbrs = null;

        if (barcode.length() == 3) {

            int i = 0;
            StringTokenizer tokBarcode = new StringTokenizer(barcode);

            while (tokBarcode.hasMoreTokens()) {

                String tokenBarcode = (String) tokBarcode.nextToken();

                i++;

            }

            if (i > 1) {
                barcode = barcode.substring(0, 1);
                // barcode= "00"+barcode;
            } else {
                barcode = barcode;
            }

            //System.out.println(" Barcode"+ barcode);
        } else {
            barcode = barcode;
        }

        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE ( " + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " = '"
                    + barcode + "' )";
            //System.out.println("sql--->"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static long getEmployeeByLikeBarcode(String barcode) {
        long result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE ( " + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " LIKE '%"
                    + barcode + "' )";
            //System.out.println("sql--->"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static long getEmployeeLikeBarcode(String barcode) {
        long result = 0;
        DBResultSet dbrs = null;
        //dited by yunny
        //System.out.println("barcodePanjang"+barcode+"djfh");
        //System.out.println("barcodePanjang"+barcode.length());
        if (barcode.length() == 3) {
            int i = 0;
            StringTokenizer tokBarcode = new StringTokenizer(barcode);
            while (tokBarcode.hasMoreTokens()) {
                String tokenBarcode = (String) tokBarcode.nextToken();
                // System.out.println("token Barcode"+tokenBarcode);
                i++;

            }
            if (i > 1) {
                barcode = barcode.substring(0, 1);
                // barcode= "00"+barcode;
            } else {
                barcode = barcode;
            }

            //System.out.println(" Barcode"+ barcode);
        } else {
            barcode = barcode;
        }

        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER] + " LIKE '%"
                    + barcode + "%'";
            //System.out.println("sql--->"+sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();
            //return result;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    public static boolean checkDepartment(long employeeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = '" + employeeId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static boolean checkEmpCategory(long empCategoryId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = '" + empCategoryId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static boolean checkLevel(long levelId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = '" + levelId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static boolean checkMarital(long maritalId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID] + " = '" + maritalId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static boolean checkPosition(long positionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = '" + positionId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static boolean checkReligion(long religionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID] + " = '" + religionId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static boolean checkSection(long sectionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = '" + sectionId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /**
     * Ari_20110930 Menambah checkCompany {
     *
     * @param companyId
     * @return
     */
    public static boolean checkCompany(long companyId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = '" + companyId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /*}*/
    // check Locker
    public static boolean checkLocker(Locker locker) {
        DBResultSet dbrs = null;
        boolean ifExist = false;
        try {
            String sql = " SELECT EMP.* FROM " + TBL_HR_EMPLOYEE + " EMP "
                    + " ," + PstLocker.TBL_HR_LOCKER + " LOC "
                    + " WHERE LOC." + PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID];
            //" AND LOC."+PstLocker.fieldNames[PstLocker.FLD_CONDITION_ID]+
            //" = "+locker.getConditionId();

            if (locker.getLockerNumber().length() > 0) {
                sql = sql + " AND LOC." + PstLocker.fieldNames[PstLocker.FLD_LOCKER_NUMBER]
                        + " = '" + locker.getLockerNumber() + "'";
            }

            /* check for locker location */
            if (locker.getLocationId() > 0) {
                sql = sql + " AND LOC." + PstLocker.fieldNames[PstLocker.FLD_LOCATION_ID]
                        + " = " + locker.getLocationId();
            }

            sql = sql + " AND LOC." + PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]
                    + " != " + locker.getOID();

            System.out.println("SQL : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Employee emp = new Employee();

            while (rs.next()) {
                ifExist = true;
            }
            rs.close();
            return ifExist;
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return false;
    }

    /**
     * Used to check if employee number already exist
     *
     * @param empNum
     * @return
     * @created by edhy
     */
    public static boolean empNumExist(String empNum) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = '" + empNum + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    // ------------------------ start added by gedhy ---------------------------
    /**
     * this method used to get name of employee created by gedhy
     */
    public static String getEmployeeName(long employeeOid) {
        String result = "";
        try {
            PstEmployee pstEmployee = new PstEmployee();
            Employee employee = pstEmployee.fetchExc(employeeOid);
            return employee.getFullName();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return result;
    }

    /**
     * create by satrya 2014-06-15
     *
     * @param employeeOid
     * @return
     */
    public static String getEmployeeNumber(long employeeOid) {
        String result = "";
        try {
            PstEmployee pstEmployee = new PstEmployee();
            Employee employee = pstEmployee.fetchExc(employeeOid);
            return employee.getEmployeeNum();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return result;
    }

    public static Vector listEmployee(long departmentId, long sectionId, long positionId) {
        return listEmployee(departmentId, sectionId, positionId, 0);
    }

    /**
     * ,long payrollGroupId this method used to list all employee specified by
     * his/her department, section and department created by gedhy
     */
    public static Vector listEmployee(long departmentId, long sectionId, long positionId, long payrollGroupId) {
        Vector result = new Vector(1, 1);
        String whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + departmentId + " AND "
                + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + sectionId + " AND "
                + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + positionId;
        if (payrollGroupId != 0) {
            whereClause += " AND " + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + "=" + payrollGroupId;
        }
        Vector vectTemp = PstEmployee.list(0, 0, whereClause, "");
        if (vectTemp != null && vectTemp.size() > 0) {
            for (int i = 0; i < vectTemp.size(); i++) {
                Employee employee = (Employee) vectTemp.get(i);

                Vector temp = new Vector(1, 1);
                temp.add("" + employee.getOID());
                temp.add(employee.getFullName());

                result.add(temp);
            }
        }

        return result;
    }
    // ------------------ end added by gedhy -----------------------------------

    /**
     * Mencari employee yang mempunyai level di mulai minimum position type
     * sampai di maximum position type pada satu department
     *
     * @param departmentId
     * @param minPositionType
     * @param maxPositionType
     * @return
     */
    public static Vector listEmployeeSupervisi(long departmentId, long divisionId, int minPositionType, int maxPositionType) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        try {

            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMAIL_ADDRESS] + " FROM "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP INNER JOIN "
                    + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " LEFT JOIN "
                    + PstLevel.TBL_HR_LEVEL + " LEV ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " WHERE "
                    + (departmentId != 0 ? ((PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId) + " OR "
                            + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = (SELECT dd." + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + " FROM "
                            + PstDepartment.TBL_HR_DEPARTMENT + " dd WHERE dd." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=" + departmentId + " )") : "(1=1)")
                    + " AND "
                    + (divisionId != 0 ? (PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + divisionId) : "(1=1)")
                    + " AND ( POS."
                    + ((minPositionType <= PstPosition.LEVEL_SUPERVISOR || minPositionType <= PstPosition.LEVEL_SECRETARY)
                            ? PstPosition.fieldNames[PstPosition.FLD_DISABLED_APP_UNDER_SUPERVISOR] + " = " + PstPosition.DISABLED_APP_UNDER_SUPERVISOR_FALSE
                            : PstPosition.fieldNames[PstPosition.FLD_DISABLED_APP_DEPT_SCOPE] + " = " + PstPosition.DISABLED_APP_DEPT_SCOPE_FALSE) + ") AND ( POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " >= " + minPositionType + " AND POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " <= " + maxPositionType + " ) "
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN
                    + " ORDER BY " + fieldNames[FLD_FULL_NAME];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setEmailAddress(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMAIL_ADDRESS]));
                result.add(employee);
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return result;
    }

    /**
     * cari karyawan dengan minimum posisi level dan maximum posisi level di
     * dalam saru divisi dengan departmentId tertentu
     *
     * @param departmentId
     * @param posLevelMin
     * @param posLevelMax
     * @return
     */
    public static Vector listEmployeeInSameDivisionAsDepartmentId(long departmentId, int posLevelMin, int posLevelMax) {

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {

            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " FROM "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP INNER JOIN "
                    + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN "
                    + PstLevel.TBL_HR_LEVEL + " LEV ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " WHERE EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                    + " IN ( SELECT DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID]
                    + " FROM " + PstDepartment.TBL_HR_DEPARTMENT + " DEP "
                    + " WHERE DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=" + departmentId + ") AND POS."
                    + PstPosition.fieldNames[PstPosition.FLD_DISABLED_APP_DEPT_SCOPE] + " = " + PstPosition.DISABLED_APP_DEPT_SCOPE_FALSE
                    + " AND ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " >=  " + posLevelMin
                    + " AND POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " <=  " + posLevelMin + ")"
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                result.add(employee);
            }

            if (result != null && result.size() > 0) {
                return result;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;
    }

    public static Hashtable<String, Employee> listByKey(int limitStart, int recordToGet, String whereClauseEmp, String order, String keyField) {
        Hashtable<String, Employee> lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT emp.* FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " emp INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " dep ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " INNER JOIN " + PstDivision.TBL_HR_DIVISION + " d ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION
                    + " pos ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=pos." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];

            if (whereClauseEmp != null && whereClauseEmp.length() > 0) {
                sql = sql + " WHERE " + whereClauseEmp;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("list employee employee structure " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.put(rs.getString("" + keyField), employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    //Gede_3Maret2012
    //untuk employee structure{
    public static Vector list2(int limitStart, int recordToGet, String whereClauseEmp, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT emp.* FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " emp INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " dep ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " INNER JOIN " + PstDivision.TBL_HR_DIVISION + " d ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION
                    + " pos ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=pos." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];

            if (whereClauseEmp != null && whereClauseEmp.length() > 0) {
                sql = sql + " WHERE " + whereClauseEmp;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("list employee employee structure " + sql);
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    //}

    //priska 20150810
    public static Vector listEmployeeByLevel(Employee employee) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        //SELECT he.`FULL_NAME`, hl.`LEVEL`,he.`DEPARTMENT_ID` FROM `hr_employee` he INNER JOIN `hr_level` hl ON hl.`LEVEL_ID` = he.`LEVEL_ID`  
        //WHERE he.`SECTION_ID` =  4001 ORDER BY hl.`LEVEL_POINT` DESC
        try {
            String sql = "SELECT emp.*, lev.* FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " emp INNER JOIN " + PstLevel.TBL_HR_LEVEL + " lev ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = lev." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " hd ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = hd." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE emp." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + employee.getSectionId()
                    + " AND emp." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + employee.getDepartmentId();

            sql = sql + " ORDER BY lev.`LEVEL_POINT` DESC ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("list employee employee structure " + sql);
            while (rs.next()) {
                Employee employeeNew = new Employee();
                Level level = new Level();
                resultToObject(rs, employeeNew);

                level.setLevel(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]));
                level.setOID(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]));
                Vector data = new Vector();
                data.add(employeeNew);
                data.add(level);
                lists.add(data);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //Gede_2April2012
    //{
    public static Vector list3(int limitStart, int recordToGet, String whereClauseEmp, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT emp.* FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " as emp INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " as dep ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "= dep."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " INNER JOIN " + PstDivision.TBL_HR_DIVISION + " as d ON emp."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "= d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]
                    + " INNER JOIN " + PstCompany.TBL_HR_COMPANY + " as c ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + "= c." + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]
                    + " INNER JOIN " + PstReligion.TBL_HR_RELIGION + " as r ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID] + "= r." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID]
                    + " INNER JOIN " + PstOvertimeDetail.TBL_OVERTIME_DETAIL + " as odt ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "= odt." + PstOvertimeDetail.fieldNames[PstOvertimeDetail.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstOvertime.TBL_OVERTIME + " as o ON odt." + PstOvertimeDetail.fieldNames[PstOvertimeDetail.FLD_OVERTIME_ID] + "= o." + PstOvertime.fieldNames[PstOvertime.FLD_OVERTIME_ID]
                    + //update by satrya 2013-08-13
                    //penambahan section
                    " LEFT JOIN " + PstSection.TBL_HR_SECTION + " as sec ON sec." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "= emp." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID];
            if (whereClauseEmp != null && whereClauseEmp.length() > 0) {
                sql = sql + " WHERE " + whereClauseEmp;
            }
            sql = sql + " GROUP BY " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Employee employee = new Employee();
                resultToObject(rs, employee);
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {

            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //}
    /**
     * hanya menampil khan Id dari company,div,dept,sec
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClauseEmp
     * @param order
     * @return
     */
    public static Vector listEmployee(int limitStart, int recordToGet, String whereClauseEmp, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT emp.* FROM " + PstEmployee.TBL_HR_EMPLOYEE + " as emp";
            if (whereClauseEmp != null && whereClauseEmp.length() > 0) {
                sql = sql + " WHERE " + whereClauseEmp;
            }
            sql = sql + " GROUP BY " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setCompanyId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]));
                employee.setDivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]));
                employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                employee.setSectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]));
                lists.add(employee);
            }
            rs.close();
            return lists;

        } catch (Exception e) {

            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //}
    /**
     * Priska 2015-01-01
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClauseEmp
     * @param order
     * @return
     */
    public static Vector listBenefitDeduction(int limitStart, int recordToGet, String whereClauseEmp, String order, long periodid, long comtype, Vector PayCOmp, ListBenefitDeduction listBenefitDeduction, String employee_id) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        String sql = "";

        if (comtype == 0) {
            comtype = 1;
        } else {
            comtype = 2;
        }
        PayPeriod payPeriod = new PayPeriod();
        try {
            payPeriod = PstPayPeriod.fetchExc(periodid);
        } catch (Exception e) {
            System.out.printf("pay period belum set");
        }
        try {
            sql = sql + "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " , "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " , "
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " , "
                    + PstCompany.fieldNames[PstCompany.FLD_COMPANY] + " , "
                    + PstDivision.fieldNames[PstDivision.FLD_DIVISION] + " , "
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " , "
                    + PstSection.fieldNames[PstSection.FLD_SECTION] + " , "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " , "
                    + PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME] + " , "
                    + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR] + " , ";
            for (int j = 0; j < PayCOmp.size(); j++) {
                PayComponent payComponent = (PayComponent) PayCOmp.get(j);
                sql = sql + " SUM(" + payComponent.getCompCode() + ") AS " + payComponent.getCompCode() + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql = sql + " FROM ( ";

            for (int j = 0; j < PayCOmp.size(); j++) {
                PayComponent payComponent = (PayComponent) PayCOmp.get(j);
                sql = sql + " SELECT DISTINCT p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD]
                        + " , e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " , e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " , e." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                        + " , g." + PstCompany.fieldNames[PstCompany.FLD_COMPANY]
                        + " , dv." + PstDivision.fieldNames[PstDivision.FLD_DIVISION]
                        + " , dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                        + " , sec." + PstSection.fieldNames[PstSection.FLD_SECTION]
                        + " , e." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]
                        + " , b." + PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME]
                        + " , l." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR];
                for (int p = 0; p < PayCOmp.size(); p++) {
                    PayComponent payComponent1 = (PayComponent) PayCOmp.get(p);
                    if (payComponent.getOID() == payComponent1.getOID()) {
                        sql = sql + " , c." + PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_VALUE] + " AS \"" + payComponent1.getCompCode() + "\" ";
                    } else {
                        sql = sql + " , 0 AS \"" + payComponent1.getCompCode() + "\" ";
                    }
                }

                sql = sql + " FROM " + TBL_HR_EMPLOYEE + " AS e "
                        + " INNER JOIN " + PstPaySlip.TBL_PAY_SLIP + " AS s ON e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = s." + PstPaySlip.fieldNames[PstPaySlip.FLD_EMPLOYEE_ID]
                        + " INNER JOIN " + PstPaySlipComp.TBL_PAY_SLIP_COMP + " AS c ON s." + PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID] + " = c." + PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_PAY_SLIP_ID]
                        + " INNER JOIN " + PstPayPeriod.TBL_HR_PAY_PERIOD + " AS p ON p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD_ID] + " = s." + PstPaySlip.fieldNames[PstPaySlip.FLD_PERIOD_ID]
                        + " INNER JOIN " + PstPayComponent.TBL_PAY_COMPONENT + " AS co ON c." + PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_CODE] + " = co." + PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_CODE]
                        + " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS l ON e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = l." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                        + " LEFT JOIN " + PstPayBanks.TBL_PAY_BANKS + " AS b ON l." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ID] + " = b." + PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_ID]
                        + " INNER JOIN " + PstPayGeneral.TBL_PAY_GENERAL + " AS g ON g." + PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " INNER JOIN " + PstDivision.TBL_HR_DIVISION + " AS dv ON dv." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS dep ON dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " AS sec ON sec." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " INNER JOIN " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " AS ec ON ec." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]
                        + " WHERE p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD_ID] + " = " + periodid
                        + //update by priska 20150316
                        " AND (e." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] + " > p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_START_DATE] + " OR e." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + listBenefitDeduction.getResignSts() + " ) ";

                //AND e.COMPANY_ID = 1
                if ((listBenefitDeduction.getPayrollNumber() != null) && (listBenefitDeduction.getPayrollNumber().length() > 0)) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = \"" + listBenefitDeduction.getPayrollNumber() + " \" ";
                }
                if (listBenefitDeduction.getCompanyId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + listBenefitDeduction.getCompanyId();
                }
                if (listBenefitDeduction.getDivisionId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + listBenefitDeduction.getDivisionId();
                }
                if (listBenefitDeduction.getDeptId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + listBenefitDeduction.getDeptId();
                }
                if (listBenefitDeduction.getSectionId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + listBenefitDeduction.getSectionId();
                }
//                         if (listBenefitDeduction.getResignSts() != 0){
//                           sql = sql + " AND e." +PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+ " = " + listBenefitDeduction.getResignSts();
//                         }
                if ((listBenefitDeduction.getFullName() != null) && (listBenefitDeduction.getFullName().length() > 0)) {
                    Vector vectName = logicParser(listBenefitDeduction.getFullName());
                    if (vectName != null && vectName.size() > 0) {
                        sql = sql + " AND (";
                        for (int i = 0; i < vectName.size(); i++) {
                            String str = (String) vectName.get(i);
                            if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                sql = sql + " e." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                        + " LIKE '%" + str.trim() + "%' ";
                            } else {
                                sql = sql + str.trim();
                            }
                        }
                        sql = sql + ")  ";
                    }

                }

                if (listBenefitDeduction.getEmpCategory() != null && listBenefitDeduction.getEmpCategory() != "") {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " IN ( " + listBenefitDeduction.getEmpCategory() + ")";
                }
                if (employee_id != null && employee_id != "") {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " IN (" + employee_id + ")";
                }
                sql = sql + " AND co." + PstPayComponent.fieldNames[PstPayComponent.FLD_COMPONENT_ID] + " = " + payComponent.getOID() + " UNION";
//                         sql = sql  + " AND co." +PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_TYPE]+ " = " +comtype 
//                           + " AND co." +PstPayComponent.fieldNames[PstPayComponent.FLD_COMPONENT_ID]+ " = " +payComponent.getOID()+ " UNION" ;

            }

            sql = sql.substring(0, sql.length() - 5);
            sql = sql + ") AS com GROUP BY " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " ORDER BY " + PstCompany.fieldNames[PstCompany.FLD_COMPANY] + ","
                    + PstDivision.fieldNames[PstDivision.FLD_DIVISION] + ","
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ","
                    + PstSection.fieldNames[PstSection.FLD_SECTION] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            sql = sql + " ASC";
//             
//SELECT `EMPLOYEE_ID`, `FULL_NAME`,`BANK_NAME`, `BANK_ACC_NR`, SUM(GAJI),SUM(Insentif)
//FROM (SELECT DISTINCT  p.`PERIOD`, e.`EMPLOYEE_ID`, e.full_name, b.`BANK_NAME`, l.`BANK_ACC_NR`, c.`COMP_VALUE` AS "GAJI", 0 AS "Insentif" FROM hr_employee e 
//INNER JOIN pay_slip s ON e.`EMPLOYEE_ID` = s.`EMPLOYEE_ID`
//INNER JOIN pay_slip_comp c ON s.`PAY_SLIP_ID` = c.`PAY_SLIP_ID`
//INNER JOIN pay_period p ON p.`PERIOD_ID` = s.`PERIOD_ID`
//INNER JOIN pay_component co ON c.`COMP_CODE` = co.`COMP_CODE` 
//INNER JOIN pay_emp_level l ON e.`EMPLOYEE_ID` = l.`EMPLOYEE_ID`
//LEFT JOIN pay_banks b ON l.`BANK_ID` = b.`BANK_ID`
//WHERE p.`PERIOD` = "January 2014"
//AND co.`COMP_TYPE` = 1 AND co.`COMP_NAME` = "GAJI"
// UNION
//SELECT DISTINCT  p.`PERIOD`, e.`EMPLOYEE_ID`, e.full_name, b.`BANK_NAME`, l.`BANK_ACC_NR`, 0  AS "GAJI", c.`COMP_VALUE` AS "Insentif" FROM hr_employee e 
//INNER JOIN pay_slip s ON e.`EMPLOYEE_ID` = s.`EMPLOYEE_ID`
//INNER JOIN pay_slip_comp c ON s.`PAY_SLIP_ID` = c.`PAY_SLIP_ID`
//INNER JOIN pay_period p ON p.`PERIOD_ID` = s.`PERIOD_ID`
//INNER JOIN pay_component co ON c.`COMP_CODE` = co.`COMP_CODE` 
//INNER JOIN pay_emp_level l ON e.`EMPLOYEE_ID` = l.`EMPLOYEE_ID`
//LEFT JOIN pay_banks b ON l.`BANK_ID` = b.`BANK_ID`
//WHERE p.`PERIOD` = "January 2014"
//AND co.`COMP_TYPE` = 1 AND co.`COMP_NAME` = "Insentif") AS com GROUP BY `EMPLOYEE_ID`;

//        
//            sql =sql + "SELECT p."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD]+
//                              ", e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+
//                              ", e."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+
//                              ", e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+
//                              ", l."+PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR]+
//                              ", b."+PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME]+
//                              ", co."+PstPayComponent.fieldNames[PstPayComponent.FLD_COMPONENT_ID]+
//                              ", co."+PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_NAME]+
//                              ", c."+PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_VALUE]+
//                    
//                        " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " as e "+ 
//                    
//                     //INNER JOIN pay_slip s ON e.`EMPLOYEE_ID` = s.`EMPLOYEE_ID`
//                        " INNER JOIN " + PstPaySlip.TBL_PAY_SLIP + " as s ON e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = s." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +
//                    //INNER JOIN pay_slip_comp c ON s.`PAY_SLIP_ID` = c.`PAY_SLIP_ID`
//                        " INNER JOIN " + PstPaySlipComp.TBL_PAY_SLIP_COMP + " as c ON s."+PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID] + " = c." +PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID] +
//                    //INNER JOIN pay_period p ON p.`PERIOD_ID` = s.`PERIOD_ID`
//                        " INNER JOIN " + PstPayPeriod.TBL_HR_PAY_PERIOD + " as p ON s."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + " = p." +PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] +
//                    //INNER JOIN pay_component co ON c.`COMP_CODE` = co.`COMP_CODE`
//                        " INNER JOIN " + PstPayComponent.TBL_PAY_COMPONENT + " as co ON c."+PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_CODE] + " = co." +PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_CODE] +
//                    //INNER JOIN pay_emp_level l ON e.`EMPLOYEE_ID` = l.`EMPLOYEE_ID`
//                        " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " as l ON e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = l." +PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] +
//                    //LEFT JOIN pay_banks b ON l.`BANK_ID` = b.`BANK_ID`
//                        " LEFT  JOIN " + PstPayBanks.TBL_PAY_BANKS + " as b ON l."+PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ID] + " = b." +PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_ID] +
//                    //WHERE p.`PERIOD_ID` = 504404574569837069
//                        " WHERE p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD_ID] + " = " + periodid +
//                    //AND co.`COMP_TYPE` = 1;   
//                        " AND co." + PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_TYPE] + " = " + comtype ;
//            
//            if (order != null && order.length() > 0) {
//                sql = sql + " ORDER BY " + order;
//            }
//            if (limitStart == 0 && recordToGet == 0) {
//                sql = sql + "";
//            } else {
//                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
//            }
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector result = new Vector();
                Employee employee = new Employee();
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));

                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                result.add(employee);

                result.add(rs.getString(PstCompany.fieldNames[PstCompany.FLD_COMPANY]));
                result.add(rs.getString(PstDivision.fieldNames[PstDivision.FLD_DIVISION]));
                result.add(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                result.add(rs.getString(PstSection.fieldNames[PstSection.FLD_SECTION]));

                PayBanks payBanks = new PayBanks();
                payBanks.setBankName(rs.getString(PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME]));
                result.add(payBanks);

                PayEmpLevel payEmpLevel = new PayEmpLevel();
                payEmpLevel.setBankAccNr(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR]));
                result.add(payEmpLevel);
                double totalpotongan = 0;
                Vector vPayComponent = new Vector();
                for (int j = 0; j < PayCOmp.size(); j++) {
                    PayComponent payComponentmini = (PayComponent) PayCOmp.get(j);
                    PayComponenttemp payComponenttemp = new PayComponenttemp();
                    payComponenttemp.setCompId(payComponentmini.getOID());
                    payComponenttemp.setCompCode(payComponentmini.getCompCode());
                    payComponenttemp.setCompValue(rs.getDouble(payComponentmini.getCompCode()));
                    payComponenttemp.setCompType(payComponentmini.getCompType());
                    payComponenttemp.setCompName(payComponentmini.getCompName());
                    vPayComponent.add(payComponenttemp);
                    if (payComponentmini.getCompType() != 1) {
                        totalpotongan = totalpotongan + rs.getDouble(payComponentmini.getCompCode());
                    }
                }
                result.add(vPayComponent);
                result.add(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]));
                result.add(totalpotongan);
                lists.add(result);
            }
            rs.close();
            return lists;

        } catch (Exception e) {

            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //}
    /**
     * Priska 2015-01-01
     *
     * @param limitStart
     * @param recordToGet
     * @param whereClauseEmp
     * @param order
     * @return
     */
    public static Hashtable listBenefitDeductionhas(int limitStart, int recordToGet, String whereClauseEmp, String order, long periodid, long comtype, Vector PayCOmp, ListBenefitDeduction listBenefitDeduction) {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        String sql = "";

        if (comtype == 0) {
            comtype = 1;
        } else {
            comtype = 2;
        }

        try {
            sql = sql + "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " , "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " , "
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " , "
                    + PstCompany.fieldNames[PstCompany.FLD_COMPANY] + " , "
                    + PstDivision.fieldNames[PstDivision.FLD_DIVISION] + " , "
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " , "
                    + PstSection.fieldNames[PstSection.FLD_SECTION] + " , "
                    + PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME] + " , "
                    + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR] + " , ";
            for (int j = 0; j < PayCOmp.size(); j++) {
                PayComponent payComponent = (PayComponent) PayCOmp.get(j);
                sql = sql + " SUM(" + payComponent.getCompCode() + ") AS " + payComponent.getCompCode() + ",";
            }
            sql = sql.substring(0, sql.length() - 1);
            sql = sql + " FROM ( ";

            for (int j = 0; j < PayCOmp.size(); j++) {
                PayComponent payComponent = (PayComponent) PayCOmp.get(j);
                sql = sql + " SELECT DISTINCT p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD]
                        + " , e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " , e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " , e." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                        + " , g." + PstCompany.fieldNames[PstCompany.FLD_COMPANY]
                        + " , dv." + PstDivision.fieldNames[PstDivision.FLD_DIVISION]
                        + " , dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                        + " , sec." + PstSection.fieldNames[PstSection.FLD_SECTION]
                        + " , b." + PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME]
                        + " , l." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR];
                for (int p = 0; p < PayCOmp.size(); p++) {
                    PayComponent payComponent1 = (PayComponent) PayCOmp.get(p);
                    if (payComponent.getOID() == payComponent1.getOID()) {
                        sql = sql + " , c." + PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_VALUE] + " AS \"" + payComponent1.getCompCode() + "\" ";
                    } else {
                        sql = sql + " , 0 AS \"" + payComponent1.getCompCode() + "\" ";
                    }
                }

                sql = sql + " FROM " + TBL_HR_EMPLOYEE + " AS e "
                        + " INNER JOIN " + PstPaySlip.TBL_PAY_SLIP + " AS s ON e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = s." + PstPaySlip.fieldNames[PstPaySlip.FLD_EMPLOYEE_ID]
                        + " INNER JOIN " + PstPaySlipComp.TBL_PAY_SLIP_COMP + " AS c ON s." + PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID] + " = c." + PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_PAY_SLIP_ID]
                        + " INNER JOIN " + PstPayPeriod.TBL_HR_PAY_PERIOD + " AS p ON p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD_ID] + " = s." + PstPaySlip.fieldNames[PstPaySlip.FLD_PERIOD_ID]
                        + " INNER JOIN " + PstPayComponent.TBL_PAY_COMPONENT + " AS co ON c." + PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_CODE] + " = co." + PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_CODE]
                        + " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS l ON e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = l." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                        + " LEFT JOIN " + PstPayBanks.TBL_PAY_BANKS + " AS b ON l." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ID] + " = b." + PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_ID]
                        + " INNER JOIN " + PstPayGeneral.TBL_PAY_GENERAL + " AS g ON g." + PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " INNER JOIN " + PstDivision.TBL_HR_DIVISION + " AS dv ON dv." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS dep ON dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " AS sec ON sec." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " = e." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " WHERE p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD_ID] + " = " + periodid;
                //AND e.COMPANY_ID = 1
                if ((listBenefitDeduction.getPayrollNumber() != null) && (listBenefitDeduction.getPayrollNumber().length() > 0)) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " = \"" + listBenefitDeduction.getPayrollNumber() + " \" ";
                }
                if (listBenefitDeduction.getCompanyId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + listBenefitDeduction.getCompanyId();
                }
                if (listBenefitDeduction.getDivisionId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + listBenefitDeduction.getDivisionId();
                }
                if (listBenefitDeduction.getDeptId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + listBenefitDeduction.getDeptId();
                }
                if (listBenefitDeduction.getSectionId() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + listBenefitDeduction.getSectionId();
                }
                if (listBenefitDeduction.getResignSts() != 0) {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + listBenefitDeduction.getResignSts();
                }
                if ((listBenefitDeduction.getFullName() != null) && (listBenefitDeduction.getFullName().length() > 0)) {
                    Vector vectName = logicParser(listBenefitDeduction.getFullName());
                    if (vectName != null && vectName.size() > 0) {
                        sql = sql + " AND (";
                        for (int i = 0; i < vectName.size(); i++) {
                            String str = (String) vectName.get(i);
                            if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                sql = sql + " e." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                        + " LIKE '%" + str.trim() + "%' ";
                            } else {
                                sql = sql + str.trim();
                            }
                        }
                        sql = sql + ")  ";
                    }

                }

                if (listBenefitDeduction.getEmpCategory() != null && listBenefitDeduction.getEmpCategory() != "") {
                    sql = sql + " AND e." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = " + listBenefitDeduction.getEmpCategory();
                }

                sql = sql + " AND co." + PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_TYPE] + " = " + comtype
                        + " AND co." + PstPayComponent.fieldNames[PstPayComponent.FLD_COMPONENT_ID] + " = " + payComponent.getOID() + " UNION";

            }

            sql = sql.substring(0, sql.length() - 5);
            sql = sql + ") AS com GROUP BY " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " ORDER BY " + PstCompany.fieldNames[PstCompany.FLD_COMPANY] + ","
                    + PstDivision.fieldNames[PstDivision.FLD_DIVISION] + ","
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ","
                    + PstSection.fieldNames[PstSection.FLD_SECTION];
            sql = sql + " ";
//             
//SELECT `EMPLOYEE_ID`, `FULL_NAME`,`BANK_NAME`, `BANK_ACC_NR`, SUM(GAJI),SUM(Insentif)
//FROM (SELECT DISTINCT  p.`PERIOD`, e.`EMPLOYEE_ID`, e.full_name, b.`BANK_NAME`, l.`BANK_ACC_NR`, c.`COMP_VALUE` AS "GAJI", 0 AS "Insentif" FROM hr_employee e 
//INNER JOIN pay_slip s ON e.`EMPLOYEE_ID` = s.`EMPLOYEE_ID`
//INNER JOIN pay_slip_comp c ON s.`PAY_SLIP_ID` = c.`PAY_SLIP_ID`
//INNER JOIN pay_period p ON p.`PERIOD_ID` = s.`PERIOD_ID`
//INNER JOIN pay_component co ON c.`COMP_CODE` = co.`COMP_CODE` 
//INNER JOIN pay_emp_level l ON e.`EMPLOYEE_ID` = l.`EMPLOYEE_ID`
//LEFT JOIN pay_banks b ON l.`BANK_ID` = b.`BANK_ID`
//WHERE p.`PERIOD` = "January 2014"
//AND co.`COMP_TYPE` = 1 AND co.`COMP_NAME` = "GAJI"
// UNION
//SELECT DISTINCT  p.`PERIOD`, e.`EMPLOYEE_ID`, e.full_name, b.`BANK_NAME`, l.`BANK_ACC_NR`, 0  AS "GAJI", c.`COMP_VALUE` AS "Insentif" FROM hr_employee e 
//INNER JOIN pay_slip s ON e.`EMPLOYEE_ID` = s.`EMPLOYEE_ID`
//INNER JOIN pay_slip_comp c ON s.`PAY_SLIP_ID` = c.`PAY_SLIP_ID`
//INNER JOIN pay_period p ON p.`PERIOD_ID` = s.`PERIOD_ID`
//INNER JOIN pay_component co ON c.`COMP_CODE` = co.`COMP_CODE` 
//INNER JOIN pay_emp_level l ON e.`EMPLOYEE_ID` = l.`EMPLOYEE_ID`
//LEFT JOIN pay_banks b ON l.`BANK_ID` = b.`BANK_ID`
//WHERE p.`PERIOD` = "January 2014"
//AND co.`COMP_TYPE` = 1 AND co.`COMP_NAME` = "Insentif") AS com GROUP BY `EMPLOYEE_ID`;

//        
//            sql =sql + "SELECT p."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD]+
//                              ", e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+
//                              ", e."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+
//                              ", e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+
//                              ", l."+PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR]+
//                              ", b."+PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME]+
//                              ", co."+PstPayComponent.fieldNames[PstPayComponent.FLD_COMPONENT_ID]+
//                              ", co."+PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_NAME]+
//                              ", c."+PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_VALUE]+
//                    
//                        " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " as e "+ 
//                    
//                     //INNER JOIN pay_slip s ON e.`EMPLOYEE_ID` = s.`EMPLOYEE_ID`
//                        " INNER JOIN " + PstPaySlip.TBL_PAY_SLIP + " as s ON e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = s." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +
//                    //INNER JOIN pay_slip_comp c ON s.`PAY_SLIP_ID` = c.`PAY_SLIP_ID`
//                        " INNER JOIN " + PstPaySlipComp.TBL_PAY_SLIP_COMP + " as c ON s."+PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID] + " = c." +PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID] +
//                    //INNER JOIN pay_period p ON p.`PERIOD_ID` = s.`PERIOD_ID`
//                        " INNER JOIN " + PstPayPeriod.TBL_HR_PAY_PERIOD + " as p ON s."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + " = p." +PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] +
//                    //INNER JOIN pay_component co ON c.`COMP_CODE` = co.`COMP_CODE`
//                        " INNER JOIN " + PstPayComponent.TBL_PAY_COMPONENT + " as co ON c."+PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_CODE] + " = co." +PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_CODE] +
//                    //INNER JOIN pay_emp_level l ON e.`EMPLOYEE_ID` = l.`EMPLOYEE_ID`
//                        " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " as l ON e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = l." +PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] +
//                    //LEFT JOIN pay_banks b ON l.`BANK_ID` = b.`BANK_ID`
//                        " LEFT  JOIN " + PstPayBanks.TBL_PAY_BANKS + " as b ON l."+PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ID] + " = b." +PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_ID] +
//                    //WHERE p.`PERIOD_ID` = 504404574569837069
//                        " WHERE p." + PstPayPeriod.fieldNames[PstPayPeriod.FLD_PERIOD_ID] + " = " + periodid +
//                    //AND co.`COMP_TYPE` = 1;   
//                        " AND co." + PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_TYPE] + " = " + comtype ;
//            
//            if (order != null && order.length() > 0) {
//                sql = sql + " ORDER BY " + order;
//            }
//            if (limitStart == 0 && recordToGet == 0) {
//                sql = sql + "";
//            } else {
//                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
//            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector result = new Vector();
                Employee employee = new Employee();
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));

                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                result.add(employee);

                result.add(rs.getString(PstCompany.fieldNames[PstCompany.FLD_COMPANY]));
                result.add(rs.getString(PstDivision.fieldNames[PstDivision.FLD_DIVISION]));
                result.add(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                result.add(rs.getString(PstSection.fieldNames[PstSection.FLD_SECTION]));

                PayBanks payBanks = new PayBanks();
                payBanks.setBankName(rs.getString(PstPayBanks.fieldNames[PstPayBanks.FLD_BANK_NAME]));
                result.add(payBanks);

                PayEmpLevel payEmpLevel = new PayEmpLevel();
                payEmpLevel.setBankAccNr(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR]));
                result.add(payEmpLevel);

                Vector vPayComponent = new Vector();
                for (int j = 0; j < PayCOmp.size(); j++) {
                    PayComponent payComponentmini = (PayComponent) PayCOmp.get(j);
                    PayComponenttemp payComponenttemp = new PayComponenttemp();
                    payComponenttemp.setCompId(payComponentmini.getOID());
                    payComponenttemp.setCompCode(payComponentmini.getCompCode());
                    payComponenttemp.setCompValue(rs.getDouble(payComponentmini.getCompCode()));
                    vPayComponent.add(payComponenttemp);
                }
                result.add(vPayComponent);
                lists.put(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]), result);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println("Exception get calcutaion aatendance untuk pay input" + e);
            return lists;
        } finally {
            DBResultSet.close(dbrs);
        }

    }

    public static int getEmployeePosLevelIdx(long employeeId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL]
                    + " FROM " + TBL_HR_EMPLOYEE + " AS EMP INNER JOIN " + PstPosition.TBL_HR_POSITION + " AS POS ON "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "="
                    + " POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int level = 0;
            while (rs.next()) {
                level = rs.getInt(1);
            }

            rs.close();
            return level;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static long getPositionId(long employeeId) {
        long positionId = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + " FROM " + TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                positionId = rs.getLong(1);
            }

            rs.close();

        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
            return positionId;
        }
    }

    public static int getEmployeePosLevelPayrollIdx(long employeeId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL_PAYROL]
                    + " FROM " + TBL_HR_EMPLOYEE + " AS EMP INNER JOIN " + PstPosition.TBL_HR_POSITION + " AS POS ON "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "="
                    + " POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int level = 0;
            while (rs.next()) {
                level = rs.getInt(1);
            }

            rs.close();
            return level;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /**
     * create by satrya 2013-08-18 untuk mencari komersing date
     *
     * @param employeeId
     * @return
     */
    public static Date getCommercingDatePerEmployee(long employeeId) {
        DBResultSet dbrs = null;
        Date dtComercing = null;
        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = '" + employeeId + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                dtComercing = rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return dtComercing;
        }
    }

    /**
     * Description : get Department, Section, Position Date : 2015-06-15 Author
     * : Putu Hendra
     */
    public static String getCompanyStructureName(long employeeId) {
        String companyStructure = "";
        DBResultSet dbrs = null;

        try {
            String sql = " SELECT " + fieldNames[FLD_DEPARTMENT_ID] + ", " + fieldNames[FLD_SECTION_ID] + ", " + fieldNames[FLD_POSITION_ID];
            sql += " FROM " + TBL_HR_EMPLOYEE;
            sql += " WHERE " + fieldNames[FLD_EMPLOYEE_ID] + "=" + employeeId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                if (rs.getLong(fieldNames[FLD_DEPARTMENT_ID]) != 0) {
                    companyStructure += getDepartmentName(rs.getLong(fieldNames[FLD_DEPARTMENT_ID])) + " | ";
                }
                if (rs.getLong(fieldNames[FLD_SECTION_ID]) != 0) {
                    companyStructure += getSectionName(rs.getLong(fieldNames[FLD_SECTION_ID])) + " | ";
                }
                if (rs.getLong(fieldNames[FLD_POSITION_ID]) != 0) {
                    companyStructure += getPositionName(rs.getLong(fieldNames[FLD_POSITION_ID]));
                }
            }
        } catch (Exception ex) {
            System.out.println("err : " + ex.toString());
        } finally {
            DBResultSet.close(dbrs);
            return companyStructure;
        }
    }

    public static String getDepartmentName(long departmentId) {
        String str = "";
        try {
            Department depart = PstDepartment.fetchExc(departmentId);
            str = depart.getDepartment();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return str;
    }

    public static String getSectionName(long sectionId) {
        String str = "";
        try {
            Section section = PstSection.fetchExc(sectionId);
            str = section.getSection();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return str;
    }

    public static String getPositionName(long positionId) {
        String str = "";
        try {
            Position position = PstPosition.fetchExc(positionId);
            str = position.getPosition();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return str;
    }

    public static boolean checkResignedOnPeriod(long employeeId, long periodId) {
        DBResultSet dbrs = null;
        boolean resigned = false;

        PayPeriod payPeriod = new PayPeriod();
        try {
            payPeriod = PstPayPeriod.fetchExc(periodId);
        } catch (Exception exc) {
        }

        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " FROM " + TBL_HR_EMPLOYEE + " WHERE "
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = '" + employeeId + "'"
                    + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] + " < '" + payPeriod.getStartDate() + "'";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                resigned = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return resigned;
        }
    }

}
