/* 
 * Form Name  	:  FrmEmployee.java 
 * Created on 	:  [date] [time] AM/PM 
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
package com.dimata.harisma.form.employee;

/* java package */
import com.dimata.harisma.entity.attendance.I_Atendance;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.harisma.entity.employee.*;
import com.dimata.system.entity.PstSystemProperty;
import com.dimata.util.Formater;
import com.dimata.util.StringReplace;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FrmEmployee extends FRMHandler implements I_FRMInterface, I_FRMType {

    private Employee employee;
    public static final String FRM_NAME_EMPLOYEE = "FRM_NAME_EMPLOYEE";
    public static final int FRM_FIELD_EMPLOYEE_ID = 0;
    public static final int FRM_FIELD_DEPARTMENT_ID = 1;
    public static final int FRM_FIELD_POSITION_ID = 2;
    public static final int FRM_FIELD_SECTION_ID = 3;
    public static final int FRM_FIELD_EMPLOYEE_NUM = 4;
    public static final int FRM_FIELD_EMP_CATEGORY_ID = 5;
    public static final int FRM_FIELD_LEVEL_ID = 6;
    public static final int FRM_FIELD_FULL_NAME = 7;
    public static final int FRM_FIELD_ADDRESS = 8;//sampe sni
    public static final int FRM_FIELD_PHONE = 9;
    public static final int FRM_FIELD_HANDPHONE = 10;
    public static final int FRM_FIELD_POSTAL_CODE = 11;
    public static final int FRM_FIELD_SEX = 12;
    public static final int FRM_FIELD_BIRTH_PLACE = 13;//
    public static final int FRM_FIELD_BIRTH_DATE = 14;
    public static final int FRM_FIELD_RELIGION_ID = 15;//
    public static final int FRM_FIELD_BLOOD_TYPE = 16;
    public static final int FRM_FIELD_ASTEK_NUM = 17;
    public static final int FRM_FIELD_ASTEK_DATE = 18;
    public static final int FRM_FIELD_MARITAL_ID = 19;
    public static final int FRM_FIELD_LOCKER_ID = 20;
    public static final int FRM_FIELD_COMMENCING_DATE = 21;//
    public static final int FRM_FIELD_RESIGNED = 22;
    public static final int FRM_FIELD_RESIGNED_DATE = 23;
    public static final int FRM_FIELD_BARCODE_NUMBER = 24;//
    public static final int FRM_FIELD_RESIGNED_REASON_ID = 25;
    public static final int FRM_FIELD_RESIGNED_DESC = 26;
    public static final int FRM_FIELD_BASIC_SALARY = 27;
    public static final int FRM_FIELD_ASSIGN_TO_ACCOUNTING = 28;
    public static final int FRM_FIELD_DIVISION_ID = 29;
    public static final int FRM_FIELD_CURIER = 30;
    /*prnambahan kolom sehubungan dengan payroll
     *Updated By Yunny
     */
    public static final int FRM_FIELD_INDENT_CARD_NR = 31;
    public static final int FRM_FIELD_INDENT_CARD_VALID_TO = 32;
    public static final int FRM_FIELD_TAX_REG_NR = 33;
    public static final int FRM_FIELD_ADDRESS_FOR_TAX = 34;
    public static final int FRM_FIELD_NATIONALITY_TYPE = 35;
    public static final int FRM_FIELD_EMAIL_ADDRESS = 36;
    public static final int FRM_FIELD_CATEGORY_DATE = 37;
    public static final int FRM_FIELD_LEAVE_STATUS = 38;
    public static final int FRM_FIELD_EMP_PIN = 39;//
    public static final int FRM_FIELD_RACE = 40;
    public static final int FRM_FIELD_ADDRESS_PERMANENT = 41;
    public static final int FRM_FIELD_PHONE_EMERGENCY = 42;
    public static final int FRM_FIELD_COMPANY_ID = 43;//Gede_15N0v2011{
    public static final int FRM_FIELD_FATHER = 44;
    public static final int FRM_FIELD_MOTHER = 45;
    public static final int FRM_FIELD_PARENTS_ADDRESS = 46;
    public static final int FRM_FIELD_NAME_EMG = 47;
    public static final int FRM_FIELD_PHONE_EMG = 48;
    public static final int FRM_FIELD_ADDRESS_EMG = 49;//}
    //Gede_27Nov2011{
    public static final int FRM_FIELD_HOD_EMPLOYEE_ID = 50;
    //add by Kartika 1 Dec 2011
    public static final int FRM_FIELD_ADDR_COUNTRY_ID = 51;//
    public static final int FRM_FIELD_ADDR_PROVINCE_ID = 52;//
    public static final int FRM_FIELD_ADDR_REGENCY_ID = 53;//
    public static final int FRM_FIELD_ADDR_SUBREGENCY_ID = 54;//
    public static final int FRM_FIELD_ADDR_PMNT_COUNTRY_ID = 55;//
    public static final int FRM_FIELD_ADDR_PMNT_PROVINCE_ID = 56;//
    public static final int FRM_FIELD_ADDR_PMNT_REGENCY_ID = 57;//
    public static final int FRM_FIELD_ADDR_PMNT_SUBREGENCY_ID = 58;
    public static final int FRM_FIELD_ID_CARD_ISSUED_BY = 59;
    public static final int FRM_FIELD_ID_CARD_BIRTH_DATE = 60;
    public static final int FRM_FIELD_TAX_MARITAL_ID = 61;
    public static final int FRM_FIELD_NO_REKENING = 62;
    public static final int FRM_FIELD_GRADE_LEVEL_ID = 63;
    //add by ganki 27 nov 2014
    public static final int FRM_FIELD_LOCATION_ID = 64;
    public static final int FRM_FIELD_END_CONTRACT = 65;
    public static final int FRM_FIELD_WORK_ASSIGN_COMPANY_ID = 66;
    public static final int FRM_FIELD_WORK_ASSIGN_DIVISION_ID = 67;
    public static final int FRM_FIELD_WORK_ASSIGN_DEPARTMENT_ID = 68;
    public static final int FRM_FIELD_WORK_ASSIGN_SECTION_ID = 69;
    public static final int FRM_FIELD_WORK_ASSIGN_POSITION_ID = 70;
    public static final int FRM_FIELD_ID_CARD_TYPE = 71;
    public static final int FRM_FIELD_NPWP = 72;
    public static final int FRM_FIELD_BPJS_NO = 73;
    public static final int FRM_FIELD_BPJS_DATE = 74;
    public static final int FRM_FIELD_SHIO = 75;
    public static final int FRM_FIELD_ELEMEN = 76;
    public static final int FRM_FIELD_IQ = 77;
    public static final int FRM_FIELD_EQ = 78;
    // Add Field by Hendra McHen 2015-01-09
    public static final int FRM_FIELD_PROBATION_END_DATE = 79;
    public static final int FRM_FIELD_STATUS_PENSIUN_PROGRAM = 80;
    public static final int FRM_FIELD_START_DATE_PENSIUN = 81;
    public static final int FRM_FIELD_PRESENCE_CHECK_PARAMETER = 82;
    public static final int FRM_FIELD_MEDICAL_INFO = 83;
    public static final int FRM_FIELD_DANA_PENDIDIKAN = 84;
    public static final int FRM_FIELD_PAYROLL_GROUP = 85;
    public static final int FRM_FIELD_PROVIDER_ID = 86; // by kartika 2015-09-16
    public static final int FRM_FIELD_MEMBER_OF_BPJS_KESEHATAN = 87;//add by priska 20151104
    public static final int FRM_FIELD_MEMBER_OF_BPJS_KETENAGAKERJAAN = 88;//add by priska 20151104
    public static final int FRM_FIELD_ADDR_DISTRICT_ID = 89;
    public static final int FRM_FIELD_ADDR_PMNT_DISTRICT_ID = 90;
    public static final int FRM_FIELD_NO_KK = 91;
    //added by dewok 20190610
    public static final int FRM_FIELD_NAMA_PEMILIK_REKENING = 92;
    public static final int FRM_FIELD_CABANG_BANK = 93;
    public static final int FRM_FIELD_KODE_BANK = 94;
    public static final int FRM_FIELD_TANGGAL_PAJAK_TERDAFTAR = 95;
    public static final int FRM_FIELD_NAMA_BANK = 96;
    public static final int FRM_FIELD_ADDRESS_ID_CARD = 97;

    private String[] SelectedValues;// 2015-01-12 update by Hendra McHen
    public static String[] fieldNames = {
        "FRM_FIELD_EMPLOYEE_ID",//0
        "FRM_FIELD_DEPARTMENT_ID",//1
        "FRM_FIELD_POSITION_ID",//2
        "FRM_FIELD_SECTION_ID",//3
        "FRM_FIELD_EMPLOYEE_NUM",//4
        "FRM_FIELD_EMP_CATEGORY_ID",//5
        "FRM_FIELD_LEVEL_ID",//6
        "FRM_FIELD_FULL_NAME",//7
        "FRM_FIELD_ADDRESS",//8
        "FRM_FIELD_PHONE",//9
        //
        "FRM_FIELD_HANDPHONE",//10
        "FRM_FIELD_POSTAL_CODE",//11
        "FRM_FIELD_SEX",//12
        "FRM_FIELD_BIRTH_PLACE",//13
        "FRM_FIELD_BIRTH_DATE",//14
        "FRM_FIELD_RELIGION_ID",//15
        "FRM_FIELD_BLOOD_TYPE",//16
        "FRM_FIELD_ASTEK_NUM",//17
        "FRM_FIELD_ASTEK_DATE",//18
        "FRM_FIELD_MARITAL_ID", //19
        //
        "FRM_FIELD_LOCKER_ID",//20
        "FRM_FIELD_COMMENCING_DATE",//21
        "FRM_FIELD_RESIGNED",//22
        "FRM_FIELD_RESIGNED_DATE",//23
        "FRM_FIELD_BARCODE_NUMBER",//24
        "FRM_FIELD_RESIGNED_REASON_ID",//25
        "FRM_FIELD_RESIGNED_DESC",//26
        "FRM_FIELD_BASIC_SALARY",//27
        "FRM_FIELD_ASSIGN_TO_ACCOUNTING",//28
        "FRM_FIELD_DIVISION_ID",//29
        //
        "FRM_FIELD_CURIER",//30
        "FRM_FIELD_INDENT_CARD_NR",//31
        "FRM_FIELD_INDENT_CARD_VALID_TO",//32
        "FRM_FIELD_TAX_REG_NR",//33
        "FRM_FIELD_ADDRESS_FOR_TAX",//34
        "FRM_FIELD_NATIONALITY_TYPE",//35
        "FRM_FIELD_EMAIL_ADDRESS",//36
        "FRM_FIELD_CATEGORY_DATE",//37
        "FRM_FIELD_LEAVE_STATUS",//38
        "FRM_FIELD_EMP_PIN",//39
        //
        "FRM_FIELD_RACE",//40
        "FRM_FIELD_ADDRESS_PERMANENT",//41
        "FRM_FIELD_PHONE_EMERGENCY",//42
        "FRM_FIELD_COMPANY_ID", //Gede_15Nov2011{ 43
        "FRM_FIELD_FATHER",//44
        "FRM_FIELD_MOTHER",//45
        "FRM_FIELD_PARENTS_ADDRESS",//46
        "FRM_FIELD_NAME_EMG",//47
        "FRM_FIELD_PHONE_EMG",//48
        "FRM_FIELD_ADDRESS_EMG",//}//49
        "FRM_FIELD_HOD_EMPLOYEE_ID",//50
        //add by Kartika 1 Dec 2011
        "FRM_FIELD_ADDR_COUNTRY_ID",//51
        "FRM_FIELD_ADDR_PROVINCE_ID",//52
        "FRM_FIELD_ADDR_REGENCY_ID",//53
        "FRM_FIELD_ADDR_SUBREGENCY_ID",//54
        "FRM_FIELD_ADDR_PMNT_COUNTRY_ID",//55
        "FRM_FIELD_ADDR_PMNT_PROVINCE_ID",//56
        "FRM_FIELD_ADDR_PMNT_REGENCY_ID",//57
        "FRM_FIELD_ADDR_PMNT_SUBREGENCY_ID",//58
        "FRM_FIELD_ID_CARD_ISSUED_BY",//59
        "FRM_FIELD_ID_CARD_BIRTH_DATE",//60
        "FRM_FIELD_TAX_MARITAL_ID",//61
        "FRM_FIELD_NO_REKENING",//62
        "FRM_FIELD_GRADE_LEVEL_ID",//63
        //add by ganki
        "FRM_FIELD_LOCATION_ID",//64
        "FRM_FIELD_END_CONTRACT",//65
        "FRM_FIELD_WORK_ASSIGN_COMPANY_ID",//66
        "FRM_FIELD_WORK_ASSIGN_DIVISION_ID",//67
        "FRM_FIELD_WORK_ASSIGN_DEPARTMENT_ID",//68
        "FRM_FIELD_WORK_ASSIGN_SECTION_ID",//69
        "FRM_FIELD_WORK_ASSIGN_POSITION_ID",//70
        "FRM_FIELD_ID_CARD_TYPE",//71
        "FRM_FIELD_NPWP",//72
        "FRM_FIELD_BPJS_NO",//73
        "FRM_FIELD_BPJS_DATE",//74
        "FRM_FIELD_SHIO",//75
        "FRM_FIELD_ELEMEN",//76
        "FRM_FIELD_IQ",//77
        "FRM_FIELD_EQ",//78
        // Add Field by Hendra McHen 2015-01-09
        "FRM_FIELD_PROBATION_END_DATE", // 79;
        "FRM_FIELD_STATUS_PENSIUN_PROGRAM", // 80;
        "FRM_FIELD_START_DATE_PENSIUN", // 81;
        "FRM_FIELD_PRESENCE_CHECK_PARAMETER", // 82;
        "FRM_FIELD_MEDICAL_INFO", // 83;
        "FRM_FIELD_DANA_PENDIDIKAN",
        "FRM_FIELD_PAYROLL_GROUP",
        "FRM_FIELD_PROVIDER_ID",
        "FRM_FIELD_MEMBER_OF_BPJS_KESEHATAN",
        "FRM_FIELD_MEMBER_OF_BPJS_KETENAGAKERJAAN",
        "FRM_FIELD_ADDR_DISTRICT_ID",
        "FRM_FIELD_ADDR_PMNT_DISTRICT_ID",
        "FRM_FIELD_NO_KK",
        //added by dewok 20190610
        "FRM_FIELD_NAMA_PEMILIK_REKENING",
        "FRM_FIELD_CABANG_BANK",
        "FRM_FIELD_KODE_BANK",
        "FRM_FIELD_TANGGAL_PAJAK_TERDAFTAR",
        "FRM_FIELD_NAMA_BANK",
        "FRM_FIELD_ADDRESS_ID_CARD",
    };
    public static String[] fieldNamesForUser = {
        "FIELD_EMPLOYEE",//0
        "FIELD_DEPARTMENT",//1
        "FIELD_POSITION",//2
        "FIELD_SECTION",//3
        "FIELD_EMPLOYEE_NUM",//4
        "FIELD_EMP_CATEGORY",//5
        "FIELD_LEVEL",//6
        "FIELD_FULL_NAME",//7
        "FIELD_ADDRESS",//8
        "FIELD_PHONE",//9
        //
        "FIELD_HANDPHONE",//10
        "FIELD_POSTAL_CODE",//11
        "FIELD_SEX",//12
        "FIELD_BIRTH_PLACE",//13
        "FIELD_BIRTH_DATE",//14
        "FIELD_RELIGION_ID",//15
        "FIELD_BLOOD_TYPE",//16
        "FIELD_ASTEK_NUM",//17
        "FIELD_ASTEK_DATE",//18
        "FIELD_MARITAL STATUS", //19
        //
        "FIELD_LOCKER",//20
        "FIELD_COMMENCING_DATE",//21
        "FIELD_RESIGNED",//22
        "FIELD_RESIGNED_DATE",//23
        "FIELD_BARCODE_NUMBER",//24
        "FIELD_RESIGNED_REASON_ID",//25
        "FIELD_RESIGNED_DESC",//26
        "FIELD_BASIC_SALARY",//27
        "FIELD_ASSIGN_TO_ACCOUNTING",//28
        "FIELD_DIVISION",//29
        //
        "FIELD_CURIER",//30
        "FIELD_INDENT_CARD_NR",//31
        "FIELD_INDENT_CARD_VALID_TO",//32
        "FIELD_TAX_REG_NR",//33
        "FIELD_ADDRESS_FOR_TAX",//34
        "FIELD_NATIONALITY_TYPE",//35
        "FIELD_EMAIL_ADDRESS",//36
        "FIELD_CATEGORY_DATE",//37
        "FIELD_LEAVE_STATUS",//38
        "FIELD_EMPLOYEE_PIN",//39
        //
        "FIELD_RACE",//40
        "FIELD_ADDRESS_PERMANENT",//41
        "FIELD_PHONE_EMERGENCY",//42
        "FIELD_COMPANY", //Gede_15Nov2011{ 43
        "FIELD_FATHER",//44
        "FIELD_MOTHER",//45
        "FIELD_PARENTS_ADDRESS",//46
        "FIELD_NAME_EMERGENCY",//47
        "FIELD_PHONE_EMERGENCY",//48
        "FIELD_ADDRESS_EMERGENCY",//}//49
        "FIELD_HOD_EMPLOYEE",//50
        //add by Kartika 1 Dec 2011
        "FIELD_COUNTRY",//51
        "FIELD_PROVINCE",//52
        "FIELD_REGENCY",//53
        "FIELD_SUBREGENCY",//54
        "FIELD_PERMANEN_COUNTRY",//55
        "FIELD_PERMANEN_PROVINCE",//56
        "FIELD_PERMANEN_REGENCY",//57
        "FIELD_PERMANEN_SUBREGENCY",//58
        "FIELD_ID_CARD_ISSUED_BY",//59
        "FIELD_ID_CARD_BIRTH_DATE",//60
        "FIELD_TAX_MARITAL REPORT",//61
        //add by ganki priska
        "FRM_FIELD_LOCATION_ID",//65
        "FRM_FIELD_END_CONTRACT",//66
        "FRM_FIELD_WORK_ASSIGN_COMPANY_ID",//67
        "FRM_FIELD_WORK_ASSIGN_DIVISION_ID",//68
        "FRM_FIELD_WORK_ASSIGN_DEPARTMENT_ID",//69
        "FRM_FIELD_WORK_ASSIGN_SECTION_ID",//70
        "FRM_FIELD_WORK_ASSIGN_POSITION_ID",//71
        "FRM_FIELD_ID_CARD_TYPE",//72 2014-12-10
        "FRM_FIELD_NPWP",//73 2014-12-10
        "FRM_FIELD_BPJS_NO",//73
        "FRM_FIELD_BPJS_DATE",//74
        "FRM_FIELD_SHIO",//75
        "FRM_FIELD_ELEMEN",//76
        "FRM_FIELD_IQ",//77
        "FRM_FIELD_EQ",//78
        // Add Field by Hendra McHen 2015-01-09
        "FRM_FIELD_PROBATION_END_DATE", // 79;
        "FRM_FIELD_STATUS_PENSIUN_PROGRAM", // 80;
        "FRM_FIELD_START_DATE_PENSIUN", // 81;
        "FRM_FIELD_PRESENCE_CHECK_PARAMETER", // 82;
        "FRM_FIELD_MEDICAL_INFO",
        "FRM_FIELD_DANA_PENDIDIKAN",// 83;
        "FRM_FIELD_PAYROLL_GROUP",
        "FRM_FIELD_PROVIDER_ID", // by kartika 2015-09-16

        "FRM_FIELD_MEMBER_OF_BPJS_KESEHATAN",// by priska 20151104
        "FRM_FIELD_MEMBER_OF_BPJS_KETENAGAKERJAAN"
    };
    public static int[] fieldTypes = {
        //0
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        // 10
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,// birth Day
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG + ENTRY_REQUIRED,
        //20
        TYPE_LONG,
        TYPE_DATE,// Comencing date
        TYPE_INT,
        TYPE_DATE,
        //update by satrya 2012-11-09
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG + ENTRY_REQUIRED,
        //
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        //
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG + ENTRY_REQUIRED,
        //Gede_15Nov2011{
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,//}
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
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING,//no Rekening     
        TYPE_STRING,//grade level
        //add ganki priska
        TYPE_LONG, //location id
        TYPE_DATE, //end contract
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
        // Add Field by Hendra McHen 2015-01-09
        TYPE_DATE,//"FRM_FIELD_PROBATION_END_DATE", // 79;
        TYPE_INT,//"FRM_FIELD_STATUS_PENSIUN_PROGRAM", // 80;
        TYPE_DATE,//"FRM_FIELD_START_DATE_PENSIUN", // 81;
        TYPE_INT,//"FRM_FIELD_PRESENCE_CHECK_PARAMETER", // 82;
        TYPE_STRING,//"FRM_FIELD_MEDICAL_INFO" // 83;
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
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
    public static int[] fieldTypesNoRequeredEmpoyee = {
        //0
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING,
        // 10
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,// birth Day
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG + ENTRY_REQUIRED,
        //20
        TYPE_LONG,
        TYPE_DATE,// Comencing date
        TYPE_INT,
        TYPE_DATE,
        //update by satrya 2012-11-09
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG + ENTRY_REQUIRED,
        //
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING + ENTRY_REQUIRED,
        //
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG + ENTRY_REQUIRED,
        //Gede_15Nov2011{
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,//}
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
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING,//no Rekening     
        TYPE_STRING,//grade level
        //add by ganki Priska
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
        // Add Field by Hendra McHen 2015-01-09
        TYPE_DATE,//"FRM_FIELD_PROBATION_END_DATE", // 79;
        TYPE_INT,//"FRM_FIELD_STATUS_PENSIUN_PROGRAM", // 80;
        TYPE_DATE,//"FRM_FIELD_START_DATE_PENSIUN", // 81;
        TYPE_INT,//"FRM_FIELD_PRESENCE_CHECK_PARAMETER", // 82;
        TYPE_STRING,//"FRM_FIELD_MEDICAL_INFO" // 83;
        TYPE_INT,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT
    };

    public FrmEmployee() {
    }

    public FrmEmployee(Employee employee) {
        this.employee = employee;
    }

    public FrmEmployee(HttpServletRequest request, Employee employee) {
        super(new FrmEmployee(employee), request);
        this.employee = employee;
    }

    public String getFormName() {
        return FRM_NAME_EMPLOYEE;
    }

    public int[] getFieldTypes() {
        I_Atendance attdConfig = null;
        try {
            attdConfig = (I_Atendance) (Class.forName(PstSystemProperty.getValueByName("ATTENDANCE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            System.out.println("Please contact your system administration to setup system property: ATTENDANCE_CONFIG ");
        }
        if (attdConfig != null && attdConfig.getConfigurasiInputEmpNum() == I_Atendance.CONFIGURATION_II_GENERATE_AUTOMATIC_EMPLOYEE_NUMBER) {
            return fieldTypesNoRequeredEmpoyee;
        } else {
            return fieldTypes;
        }

    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public Employee getEntityObject() {
        return employee;
    }

    public void requestEntityObject(Employee employee) {
        try {
            this.requestParam();
            employee.setDepartmentId(getLong(FRM_FIELD_DEPARTMENT_ID));
            employee.setPositionId(getLong(FRM_FIELD_POSITION_ID));
            employee.setSectionId(getLong(FRM_FIELD_SECTION_ID));
            employee.setEmployeeNum(getString(FRM_FIELD_EMPLOYEE_NUM));
            employee.setEmpCategoryId(getLong(FRM_FIELD_EMP_CATEGORY_ID));
            employee.setLevelId(getLong(FRM_FIELD_LEVEL_ID));
            //update by satrya 2013-12-11
            String replaceSpace = StringReplace.replaceSpace(getString(FRM_FIELD_FULL_NAME), " ");
            employee.setFullName(replaceSpace);
            employee.setAddress(getString(FRM_FIELD_ADDRESS));
            employee.setPhone(getString(FRM_FIELD_PHONE));
            employee.setHandphone(getString(FRM_FIELD_HANDPHONE));
            employee.setPostalCode(getInt(FRM_FIELD_POSTAL_CODE));
            employee.setSex(getInt(FRM_FIELD_SEX));
            employee.setBirthPlace(getString(FRM_FIELD_BIRTH_PLACE));
            employee.setBirthDate(getDate(FRM_FIELD_BIRTH_DATE));
            employee.setReligionId(getLong(FRM_FIELD_RELIGION_ID));
            employee.setBloodType(getString(FRM_FIELD_BLOOD_TYPE));
            employee.setAstekNum(getString(FRM_FIELD_ASTEK_NUM));
            employee.setAstekDate(getDate(FRM_FIELD_ASTEK_DATE));
            employee.setMaritalId(getLong(FRM_FIELD_MARITAL_ID));
            employee.setTaxMaritalId(getLong(FRM_FIELD_TAX_MARITAL_ID));
            employee.setLockerId(getLong(FRM_FIELD_LOCKER_ID));
            employee.setCommencingDate(getDate(FRM_FIELD_COMMENCING_DATE));
            employee.setResigned(getInt(FRM_FIELD_RESIGNED));
            employee.setResignedDate(getDate(FRM_FIELD_RESIGNED_DATE));
            employee.setResignedReasonId(getLong(FRM_FIELD_RESIGNED_REASON_ID));
            employee.setResignedDesc(getString(FRM_FIELD_RESIGNED_DESC));
            employee.setBasicSalary(getDouble(FRM_FIELD_BASIC_SALARY));
            employee.setIsAssignToAccounting(getBoolean(FRM_FIELD_ASSIGN_TO_ACCOUNTING));
            employee.setDivisionId(getLong(FRM_FIELD_DIVISION_ID));
            //untuk keperluan intimas,yaitu pembawa
            employee.setCurier(getString(FRM_FIELD_CURIER));
            /* penambahan kolom sehubungan dengan payroll
             *Updated By Yunny
             */
            employee.setIndentCardNr(getString(FRM_FIELD_INDENT_CARD_NR));
            employee.setIndentCardValidTo(getDate(FRM_FIELD_INDENT_CARD_VALID_TO));
            employee.setTaxRegNr(getString(FRM_FIELD_TAX_REG_NR));
            employee.setAddressForTax(getString(FRM_FIELD_ADDRESS_FOR_TAX));
            employee.setNationalityType(getInt(FRM_FIELD_NATIONALITY_TYPE));
            employee.setEmailAddress(getString(FRM_FIELD_EMAIL_ADDRESS));
            employee.setCategoryDate(getDate(FRM_FIELD_CATEGORY_DATE));
            employee.setLeaveStatus(getInt(FRM_FIELD_LEAVE_STATUS));

            employee.setEmpPin(getString(FRM_FIELD_EMP_PIN));
            employee.setRace(getLong(FRM_FIELD_RACE));
            employee.setAddressPermanent(getString(FRM_FIELD_ADDRESS_PERMANENT));
            employee.setPhoneEmergency(getString(FRM_FIELD_PHONE_EMERGENCY));

            employee.setBarcodeNumber(getString(FRM_FIELD_BARCODE_NUMBER));

            employee.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
            //Gede_15Nov2011{
            employee.setFather(getString(FRM_FIELD_FATHER));
            employee.setMother(getString(FRM_FIELD_MOTHER));
            employee.setParentsAddress(getString(FRM_FIELD_PARENTS_ADDRESS));
            employee.setNameEmg(getString(FRM_FIELD_NAME_EMG));
            employee.setPhoneEmg(getString(FRM_FIELD_PHONE_EMG));
            employee.setAddressEmg(getString(FRM_FIELD_ADDRESS_EMG));//}
            //Gede_27Nov2011{
            employee.setHodEmployeeId(getLong(FRM_FIELD_HOD_EMPLOYEE_ID));//}
            // add by Kartika 1 dec 2011
            employee.setAddrCountryId(getLong(FRM_FIELD_ADDR_COUNTRY_ID));
            employee.setAddrProvinceId(getLong(FRM_FIELD_ADDR_PROVINCE_ID));
            employee.setAddrRegencyId(getLong(FRM_FIELD_ADDR_REGENCY_ID));
            employee.setAddrSubRegencyId(getLong(FRM_FIELD_ADDR_SUBREGENCY_ID));
            employee.setAddrDistrictId(getLong(FRM_FIELD_ADDR_DISTRICT_ID));
            employee.setAddrPmntCountryId(getLong(FRM_FIELD_ADDR_PMNT_COUNTRY_ID));
            employee.setAddrPmntProvinceId(getLong(FRM_FIELD_ADDR_PMNT_PROVINCE_ID));
            employee.setAddrPmntRegencyId(getLong(FRM_FIELD_ADDR_PMNT_REGENCY_ID));
            employee.setAddrPmntSubRegencyId(getLong(FRM_FIELD_ADDR_PMNT_SUBREGENCY_ID));
            employee.setAddrPmntDistrictId(getLong(FRM_FIELD_ADDR_PMNT_DISTRICT_ID));

            employee.setIndentCardIssuedBy(getString(FRM_FIELD_ID_CARD_ISSUED_BY));//}
            employee.setIndentCardBirthDate(getDate(FRM_FIELD_ID_CARD_BIRTH_DATE));
            employee.setNoRekening(getString(FRM_FIELD_NO_REKENING));
            employee.setGradeLevelId(getLong(FRM_FIELD_GRADE_LEVEL_ID));
            //add by ganki 27 okt 2014
            employee.setLocationId(getLong(FRM_FIELD_LOCATION_ID));
            employee.setEnd_contract(getDate(FRM_FIELD_END_CONTRACT));
            employee.setWorkassigncompanyId(getLong(FRM_FIELD_WORK_ASSIGN_COMPANY_ID));
            employee.setWorkassigndivisionId(getLong(FRM_FIELD_WORK_ASSIGN_DIVISION_ID));
            employee.setWorkassigndepartmentId(getLong(FRM_FIELD_WORK_ASSIGN_DEPARTMENT_ID));
            employee.setWorkassignsectionId(getLong(FRM_FIELD_WORK_ASSIGN_SECTION_ID));
            employee.setWorkassignpositionId(getLong(FRM_FIELD_WORK_ASSIGN_POSITION_ID));
            employee.setIdcardtype(getString(FRM_FIELD_ID_CARD_TYPE));
            employee.setNpwp(getString(FRM_FIELD_NPWP));
            employee.setBpjs_no(getString(FRM_FIELD_BPJS_NO));
            employee.setBpjs_date(getDate(FRM_FIELD_BPJS_DATE));
            // Add Field by Hendra McHen 2015-01-09
            employee.setProbationEndDate(getDate(FRM_FIELD_PROBATION_END_DATE));
            employee.setStatusPensiunProgram(getInt(FRM_FIELD_STATUS_PENSIUN_PROGRAM));
            employee.setStartDatePensiun(getDate(FRM_FIELD_START_DATE_PENSIUN));
            employee.setPresenceCheckParameter(getInt(FRM_FIELD_PRESENCE_CHECK_PARAMETER));
            /* Add Field by Hendra Putu | 2015-04-24*/
            employee.setDanaPendidikan(getInt(FRM_FIELD_DANA_PENDIDIKAN));
            //priska menambahkan 20150731 
            employee.setPayrollGroup(getLong(FRM_FIELD_PAYROLL_GROUP));
            employee.setProviderID(getLong(FRM_FIELD_PROVIDER_ID));
            //priska 20151104
            employee.setMemOfBpjsKesahatan(getInt(FRM_FIELD_MEMBER_OF_BPJS_KESEHATAN));
            employee.setMemOfBpjsKetenagaKerjaan(getInt(FRM_FIELD_MEMBER_OF_BPJS_KETENAGAKERJAAN));
            if ((getSelectedMedical().length() > 0)) {
                employee.setMedicalInfo("0");
            }

            Date caritahun = (getDate(FRM_FIELD_BIRTH_DATE));
            String shio = "";
            String S_elemen = "";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            if ((caritahun.after(sdf.parse("1880-02-10")) && caritahun.before(sdf.parse("1881-01-29")) || (caritahun.equals(sdf.parse("1880-02-10"))) || (caritahun.equals(sdf.parse("1881-01-29"))))) {
                shio = "Dragon";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1881-01-30")) && caritahun.before(sdf.parse("1882-02-17")) || (caritahun.equals(sdf.parse("1881-01-30"))) || (caritahun.equals(sdf.parse("1882-02-17"))))) {
                shio = "Snake";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1882-02-18")) && caritahun.before(sdf.parse("1883-02-07")) || (caritahun.equals(sdf.parse("1882-02-18"))) || (caritahun.equals(sdf.parse("1883-02-07"))))) {
                shio = "";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1883-02-08")) && caritahun.before(sdf.parse("1884-01-27")) || (caritahun.equals(sdf.parse("1883-02-08"))) || (caritahun.equals(sdf.parse("1884-01-27"))))) {
                shio = "Sheep";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1884-01-28")) && caritahun.before(sdf.parse("1885-02-14")) || (caritahun.equals(sdf.parse("1884-01-28"))) || (caritahun.equals(sdf.parse("1885-02-14"))))) {
                shio = "Monkey";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1885-02-15")) && caritahun.before(sdf.parse("1886-02-03")) || (caritahun.equals(sdf.parse("1885-02-15"))) || (caritahun.equals(sdf.parse("1886-02-03"))))) {
                shio = "Rooster";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1886-02-04")) && caritahun.before(sdf.parse("1887-01-23")) || (caritahun.equals(sdf.parse("1886-02-04"))) || (caritahun.equals(sdf.parse("1887-01-23"))))) {
                shio = "Dog";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1887-01-24")) && caritahun.before(sdf.parse("1888-02-11")) || (caritahun.equals(sdf.parse("1887-01-24"))) || (caritahun.equals(sdf.parse("1888-02-11"))))) {
                shio = "Boar";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1888-02-12")) && caritahun.before(sdf.parse("1889-01-30")) || (caritahun.equals(sdf.parse("1888-02-12"))) || (caritahun.equals(sdf.parse("1889-01-30"))))) {
                shio = "Rat";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1889-01-31")) && caritahun.before(sdf.parse("1890-01-20")) || (caritahun.equals(sdf.parse("1889-01-31"))) || (caritahun.equals(sdf.parse("1890-01-20"))))) {
                shio = "Buffalo";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1890-01-21")) && caritahun.before(sdf.parse("1891-02-08")) || (caritahun.equals(sdf.parse("1890-01-21"))) || (caritahun.equals(sdf.parse("1891-02-08"))))) {
                shio = "Tiger";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1891-02-09")) && caritahun.before(sdf.parse("1892-01-29")) || (caritahun.equals(sdf.parse("1891-02-09"))) || (caritahun.equals(sdf.parse("1892-01-29"))))) {
                shio = "Rabbit";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1892-01-30")) && caritahun.before(sdf.parse("1893-02-16")) || (caritahun.equals(sdf.parse("1892-01-30"))) || (caritahun.equals(sdf.parse("1893-02-16"))))) {
                shio = "Dragon";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1893-02-17")) && caritahun.before(sdf.parse("1894-02-05")) || (caritahun.equals(sdf.parse("1893-02-17"))) || (caritahun.equals(sdf.parse("1894-02-05"))))) {
                shio = "Snake";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1894-02-06")) && caritahun.before(sdf.parse("1895-01-25")) || (caritahun.equals(sdf.parse("1894-02-06"))) || (caritahun.equals(sdf.parse("1895-01-25"))))) {
                shio = "Horse";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1895-01-26")) && caritahun.before(sdf.parse("1896-02-12")) || (caritahun.equals(sdf.parse("1895-01-26"))) || (caritahun.equals(sdf.parse("1896-02-12"))))) {
                shio = "Sheep";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1896-02-13")) && caritahun.before(sdf.parse("1897-02-01")) || (caritahun.equals(sdf.parse("1896-02-13"))) || (caritahun.equals(sdf.parse("1897-02-01"))))) {
                shio = "Monkey";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1897-02-02")) && caritahun.before(sdf.parse("1898-01-21")) || (caritahun.equals(sdf.parse("1897-02-02"))) || (caritahun.equals(sdf.parse("1898-01-21"))))) {
                shio = "Rooster";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1898-01-22")) && caritahun.before(sdf.parse("1899-02-09")) || (caritahun.equals(sdf.parse("1898-01-22"))) || (caritahun.equals(sdf.parse("1899-02-09"))))) {
                shio = "Dog";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1899-02-10")) && caritahun.before(sdf.parse("1900-01-30")) || (caritahun.equals(sdf.parse("1899-02-10"))) || (caritahun.equals(sdf.parse("1900-01-30"))))) {
                shio = "Boar";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1900-01-31")) && caritahun.before(sdf.parse("1901-02-18")) || (caritahun.equals(sdf.parse("1900-01-31"))) || (caritahun.equals(sdf.parse("1901-02-18"))))) {
                shio = "Rat";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1901-02-19")) && caritahun.before(sdf.parse("1902-02-07")) || (caritahun.equals(sdf.parse("1901-02-19"))) || (caritahun.equals(sdf.parse("1902-02-07"))))) {
                shio = "Buffalo";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1902-02-08")) && caritahun.before(sdf.parse("1903-01-28")) || (caritahun.equals(sdf.parse("1902-02-08"))) || (caritahun.equals(sdf.parse("1903-01-28"))))) {
                shio = "Tiger";
                S_elemen = " Water ";
            }
            if ((caritahun.after(sdf.parse("1903-01-29")) && caritahun.before(sdf.parse("1904-02-15")) || (caritahun.equals(sdf.parse("1903-01-29"))) || (caritahun.equals(sdf.parse("1904-02-15"))))) {
                shio = "Rabbit";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1904-02-16")) && caritahun.before(sdf.parse("1905-02-03")) || (caritahun.equals(sdf.parse("1904-02-16"))) || (caritahun.equals(sdf.parse("1905-02-03"))))) {
                shio = "Dragon";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1905-02-04")) && caritahun.before(sdf.parse("1906-01-24")) || (caritahun.equals(sdf.parse("1905-02-04"))) || (caritahun.equals(sdf.parse("1906-01-24"))))) {
                shio = "Snake";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1906-01-25")) && caritahun.before(sdf.parse("1907-02-12")) || (caritahun.equals(sdf.parse("1906-01-25"))) || (caritahun.equals(sdf.parse("1907-02-12"))))) {
                shio = "Horse";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1907-02-13")) && caritahun.before(sdf.parse("1908-02-01")) || (caritahun.equals(sdf.parse("1907-02-13"))) || (caritahun.equals(sdf.parse("1908-02-01"))))) {
                shio = "Sheep";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1908-02-02")) && caritahun.before(sdf.parse("1909-01-21")) || (caritahun.equals(sdf.parse("1908-02-02"))) || (caritahun.equals(sdf.parse("1909-01-21"))))) {
                shio = "Monkey";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1909-01-22")) && caritahun.before(sdf.parse("1910-02-09")) || (caritahun.equals(sdf.parse("1909-01-22"))) || (caritahun.equals(sdf.parse("1910-02-09"))))) {
                shio = "Rooster";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1910-02-10")) && caritahun.before(sdf.parse("1911-01-29")) || (caritahun.equals(sdf.parse("1910-02-10"))) || (caritahun.equals(sdf.parse("1911-01-29"))))) {
                shio = "Dog";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1911-01-30")) && caritahun.before(sdf.parse("1912-02-17")) || (caritahun.equals(sdf.parse("1911-01-30"))) || (caritahun.equals(sdf.parse("1912-02-17"))))) {
                shio = "Boar";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1912-02-18")) && caritahun.before(sdf.parse("1913-02-05")) || (caritahun.equals(sdf.parse("1912-02-18"))) || (caritahun.equals(sdf.parse("1913-02-05"))))) {
                shio = "Rat";
                S_elemen = " Water ";
            }
            if ((caritahun.after(sdf.parse("1913-02-06")) && caritahun.before(sdf.parse("1914-01-25")) || (caritahun.equals(sdf.parse("1913-02-06"))) || (caritahun.equals(sdf.parse("1914-01-25"))))) {
                shio = "Buffalo";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1914-01-26")) && caritahun.before(sdf.parse("1915-02-13")) || (caritahun.equals(sdf.parse("1914-01-26"))) || (caritahun.equals(sdf.parse("1915-02-13"))))) {
                shio = "Tiger";
                S_elemen = " Wood";
            }
            if ((caritahun.after(sdf.parse("1915-02-14")) && caritahun.before(sdf.parse("1916-02-02")) || (caritahun.equals(sdf.parse("1915-02-14"))) || (caritahun.equals(sdf.parse("1916-02-02"))))) {
                shio = "Rabbit";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1916-02-03")) && caritahun.before(sdf.parse("1917-01-22")) || (caritahun.equals(sdf.parse("1916-02-03"))) || (caritahun.equals(sdf.parse("1917-01-22"))))) {
                shio = "Dragon";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1917-01-23")) && caritahun.before(sdf.parse("1918-02-10")) || (caritahun.equals(sdf.parse("1917-01-23"))) || (caritahun.equals(sdf.parse("1918-02-10"))))) {
                shio = "Snake";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1918-02-11")) && caritahun.before(sdf.parse("1919-01-31")) || (caritahun.equals(sdf.parse("1918-02-11"))) || (caritahun.equals(sdf.parse("1919-01-31"))))) {
                shio = "Horse";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1919-02-01")) && caritahun.before(sdf.parse("1920-02-19")) || (caritahun.equals(sdf.parse("1919-02-01"))) || (caritahun.equals(sdf.parse("1920-02-19"))))) {
                shio = "Sheep";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1920-02-20")) && caritahun.before(sdf.parse("1921-02-07")) || (caritahun.equals(sdf.parse("1920-02-20"))) || (caritahun.equals(sdf.parse("1921-02-07"))))) {
                shio = "Monkey";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1921-02-08")) && caritahun.before(sdf.parse("1922-01-27")) || (caritahun.equals(sdf.parse("1921-02-08"))) || (caritahun.equals(sdf.parse("1922-01-27"))))) {
                shio = "Rooster";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1922-01-28")) && caritahun.before(sdf.parse("1923-02-15")) || (caritahun.equals(sdf.parse("1922-01-28"))) || (caritahun.equals(sdf.parse("1923-02-15"))))) {
                shio = "Dog";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1923-02-16")) && caritahun.before(sdf.parse("1924-02-04")) || (caritahun.equals(sdf.parse("1923-02-16"))) || (caritahun.equals(sdf.parse("1924-02-04"))))) {
                shio = "Boar";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1924-02-05")) && caritahun.before(sdf.parse("1925-01-23")) || (caritahun.equals(sdf.parse("1924-02-05"))) || (caritahun.equals(sdf.parse("1925-01-23"))))) {
                shio = "Rat";
                S_elemen = " Wood";
            }
            if ((caritahun.after(sdf.parse("1925-01-24")) && caritahun.before(sdf.parse("1926-02-12")) || (caritahun.equals(sdf.parse("1925-01-24"))) || (caritahun.equals(sdf.parse("1926-02-12"))))) {
                shio = "Buffalo";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1926-02-13")) && caritahun.before(sdf.parse("1927-02-01")) || (caritahun.equals(sdf.parse("1926-02-13"))) || (caritahun.equals(sdf.parse("1927-02-01"))))) {
                shio = "Tiger";
                S_elemen = " Fire ";
            }
            if ((caritahun.after(sdf.parse("1927-02-02")) && caritahun.before(sdf.parse("1928-01-22")) || (caritahun.equals(sdf.parse("1927-02-02"))) || (caritahun.equals(sdf.parse("1928-01-22"))))) {
                shio = "Rabbit";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1928-01-23")) && caritahun.before(sdf.parse("1929-02-09")) || (caritahun.equals(sdf.parse("1928-01-23"))) || (caritahun.equals(sdf.parse("1929-02-09"))))) {
                shio = "Dragon";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1929-02-10")) && caritahun.before(sdf.parse("1930-01-29")) || (caritahun.equals(sdf.parse("1929-02-10"))) || (caritahun.equals(sdf.parse("1930-01-29"))))) {
                shio = "Snake";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1930-01-30")) && caritahun.before(sdf.parse("1931-02-16")) || (caritahun.equals(sdf.parse("1930-01-30"))) || (caritahun.equals(sdf.parse("1931-02-16"))))) {
                shio = "Horse";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1931-02-17")) && caritahun.before(sdf.parse("1932-02-05")) || (caritahun.equals(sdf.parse("1931-02-17"))) || (caritahun.equals(sdf.parse("1932-02-05"))))) {
                shio = "Sheep";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1932-02-06")) && caritahun.before(sdf.parse("1933-01-25")) || (caritahun.equals(sdf.parse("1932-02-06"))) || (caritahun.equals(sdf.parse("1933-01-25"))))) {
                shio = "Monkey";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1933-01-26")) && caritahun.before(sdf.parse("1934-02-13")) || (caritahun.equals(sdf.parse("1933-01-26"))) || (caritahun.equals(sdf.parse("1934-02-13"))))) {
                shio = "Rooster";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1934-02-14")) && caritahun.before(sdf.parse("1935-02-03")) || (caritahun.equals(sdf.parse("1934-02-14"))) || (caritahun.equals(sdf.parse("1935-02-03"))))) {
                shio = "Dog";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1935-02-04")) && caritahun.before(sdf.parse("1936-01-23")) || (caritahun.equals(sdf.parse("1935-02-04"))) || (caritahun.equals(sdf.parse("1936-01-23"))))) {
                shio = "Boar";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1936-01-24")) && caritahun.before(sdf.parse("1937-02-10")) || (caritahun.equals(sdf.parse("1936-01-24"))) || (caritahun.equals(sdf.parse("1937-02-10"))))) {
                shio = "Rat";
                S_elemen = " Fire ";
            }
            if ((caritahun.after(sdf.parse("1937-02-11")) && caritahun.before(sdf.parse("1938-01-30")) || (caritahun.equals(sdf.parse("1937-02-11"))) || (caritahun.equals(sdf.parse("1938-01-30"))))) {
                shio = "Buffalo";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1938-01-31")) && caritahun.before(sdf.parse("1939-02-18")) || (caritahun.equals(sdf.parse("1938-01-31"))) || (caritahun.equals(sdf.parse("1939-02-18"))))) {
                shio = "Tiger";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1939-02-19")) && caritahun.before(sdf.parse("1940-02-07")) || (caritahun.equals(sdf.parse("1939-02-19"))) || (caritahun.equals(sdf.parse("1940-02-07"))))) {
                shio = "Rabbit";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1940-02-08")) && caritahun.before(sdf.parse("1941-01-26")) || (caritahun.equals(sdf.parse("1940-02-08"))) || (caritahun.equals(sdf.parse("1941-01-26"))))) {
                shio = "Dragon";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1941-01-27")) && caritahun.before(sdf.parse("1942-02-14")) || (caritahun.equals(sdf.parse("1941-01-27"))) || (caritahun.equals(sdf.parse("1942-02-14"))))) {
                shio = "Snake";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1942-02-15")) && caritahun.before(sdf.parse("1943-02-04")) || (caritahun.equals(sdf.parse("1942-02-15"))) || (caritahun.equals(sdf.parse("1943-02-04"))))) {
                shio = "Horse";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1943-02-05")) && caritahun.before(sdf.parse("1944-01-24")) || (caritahun.equals(sdf.parse("1943-02-05"))) || (caritahun.equals(sdf.parse("1944-01-24"))))) {
                shio = "Sheep";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1944-01-25")) && caritahun.before(sdf.parse("1945-02-12")) || (caritahun.equals(sdf.parse("1944-01-25"))) || (caritahun.equals(sdf.parse("1945-02-12"))))) {
                shio = "Monkey";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1945-02-13")) && caritahun.before(sdf.parse("1946-02-01")) || (caritahun.equals(sdf.parse("1945-02-13"))) || (caritahun.equals(sdf.parse("1946-02-01"))))) {
                shio = "Rooster";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1946-02-02")) && caritahun.before(sdf.parse("1947-01-21")) || (caritahun.equals(sdf.parse("1946-02-02"))) || (caritahun.equals(sdf.parse("1947-01-21"))))) {
                shio = "Dog";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1947-01-22")) && caritahun.before(sdf.parse("1948-02-09")) || (caritahun.equals(sdf.parse("1947-01-22"))) || (caritahun.equals(sdf.parse("1948-02-09"))))) {
                shio = "Boar";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1948-02-10")) && caritahun.before(sdf.parse("1949-01-28")) || (caritahun.equals(sdf.parse("1948-02-10"))) || (caritahun.equals(sdf.parse("1949-01-28"))))) {
                shio = "Rat";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1949-01-29")) && caritahun.before(sdf.parse("1950-02-16")) || (caritahun.equals(sdf.parse("1949-01-29"))) || (caritahun.equals(sdf.parse("1950-02-16"))))) {
                shio = "Buffalo";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1950-02-17")) && caritahun.before(sdf.parse("1951-02-05")) || (caritahun.equals(sdf.parse("1950-02-17"))) || (caritahun.equals(sdf.parse("1951-02-05"))))) {
                shio = "Tiger";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1951-02-06")) && caritahun.before(sdf.parse("1952-01-26")) || (caritahun.equals(sdf.parse("1951-02-06"))) || (caritahun.equals(sdf.parse("1952-01-26"))))) {
                shio = "Rabbit";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1952-01-27")) && caritahun.before(sdf.parse("1953-02-13")) || (caritahun.equals(sdf.parse("1952-01-27"))) || (caritahun.equals(sdf.parse("1953-02-13"))))) {
                shio = "Dragon";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1953-02-14")) && caritahun.before(sdf.parse("1954-02-02")) || (caritahun.equals(sdf.parse("1953-02-14"))) || (caritahun.equals(sdf.parse("1954-02-02"))))) {
                shio = "Snake";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1954-02-03")) && caritahun.before(sdf.parse("1955-01-23")) || (caritahun.equals(sdf.parse("1954-02-03"))) || (caritahun.equals(sdf.parse("1955-01-23"))))) {
                shio = "Horse";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1955-01-24")) && caritahun.before(sdf.parse("1956-02-11")) || (caritahun.equals(sdf.parse("1955-01-24"))) || (caritahun.equals(sdf.parse("1956-02-11"))))) {
                shio = "Sheep";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1956-02-12")) && caritahun.before(sdf.parse("1957-01-30")) || (caritahun.equals(sdf.parse("1956-02-12"))) || (caritahun.equals(sdf.parse("1957-01-30"))))) {
                shio = "Monkey";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1957-01-31")) && caritahun.before(sdf.parse("1958-02-17")) || (caritahun.equals(sdf.parse("1957-01-31"))) || (caritahun.equals(sdf.parse("1958-02-17"))))) {
                shio = "Rooster";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1958-02-18")) && caritahun.before(sdf.parse("1959-02-07")) || (caritahun.equals(sdf.parse("1958-02-18"))) || (caritahun.equals(sdf.parse("1959-02-07"))))) {
                shio = "Dog";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1959-02-08")) && caritahun.before(sdf.parse("1960-01-27")) || (caritahun.equals(sdf.parse("1959-02-08"))) || (caritahun.equals(sdf.parse("1960-01-27"))))) {
                shio = "Boar";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1960-01-28")) && caritahun.before(sdf.parse("1961-02-14")) || (caritahun.equals(sdf.parse("1960-01-28"))) || (caritahun.equals(sdf.parse("1961-02-14"))))) {
                shio = "Rat";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1961-02-15")) && caritahun.before(sdf.parse("1962-02-04")) || (caritahun.equals(sdf.parse("1961-02-15"))) || (caritahun.equals(sdf.parse("1962-02-04"))))) {
                shio = "Buffalo";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1962-02-05")) && caritahun.before(sdf.parse("1963-01-24")) || (caritahun.equals(sdf.parse("1962-02-05"))) || (caritahun.equals(sdf.parse("1963-01-24"))))) {
                shio = "Tiger";
                S_elemen = " Water ";
            }
            if ((caritahun.after(sdf.parse("1963-01-25")) && caritahun.before(sdf.parse("1964-02-12")) || (caritahun.equals(sdf.parse("1963-01-25"))) || (caritahun.equals(sdf.parse("1964-02-12"))))) {
                shio = "Rabbit";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1964-02-13")) && caritahun.before(sdf.parse("1965-02-01")) || (caritahun.equals(sdf.parse("1964-02-13"))) || (caritahun.equals(sdf.parse("1965-02-01"))))) {
                shio = "Dragon";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1965-02-02")) && caritahun.before(sdf.parse("1966-01-20")) || (caritahun.equals(sdf.parse("1965-02-02"))) || (caritahun.equals(sdf.parse("1966-01-20"))))) {
                shio = "Snake";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1966-01-21")) && caritahun.before(sdf.parse("1967-02-08")) || (caritahun.equals(sdf.parse("1966-01-21"))) || (caritahun.equals(sdf.parse("1967-02-08"))))) {
                shio = "Horse";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1967-02-09")) && caritahun.before(sdf.parse("1968-01-29")) || (caritahun.equals(sdf.parse("1967-02-09"))) || (caritahun.equals(sdf.parse("1968-01-29"))))) {
                shio = "Sheep";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1968-01-30")) && caritahun.before(sdf.parse("1969-02-16")) || (caritahun.equals(sdf.parse("1968-01-30"))) || (caritahun.equals(sdf.parse("1969-02-16"))))) {
                shio = "Monkey";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1969-02-17")) && caritahun.before(sdf.parse("1970-02-05")) || (caritahun.equals(sdf.parse("1969-02-17"))) || (caritahun.equals(sdf.parse("1970-02-05"))))) {
                shio = "Rooster";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1970-02-06")) && caritahun.before(sdf.parse("1971-01-26")) || (caritahun.equals(sdf.parse("1970-02-06"))) || (caritahun.equals(sdf.parse("1971-01-26"))))) {
                shio = "Dog";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1971-01-27")) && caritahun.before(sdf.parse("1972-02-14")) || (caritahun.equals(sdf.parse("1971-01-27"))) || (caritahun.equals(sdf.parse("1972-02-14"))))) {
                shio = "Boar";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1972-02-15")) && caritahun.before(sdf.parse("1973-02-02")) || (caritahun.equals(sdf.parse("1972-02-15"))) || (caritahun.equals(sdf.parse("1973-02-02"))))) {
                shio = "Rat";
                S_elemen = " Water ";
            }
            if ((caritahun.after(sdf.parse("1973-02-03")) && caritahun.before(sdf.parse("1974-01-22")) || (caritahun.equals(sdf.parse("1973-02-03"))) || (caritahun.equals(sdf.parse("1974-01-22"))))) {
                shio = "Buffalo";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1974-01-23")) && caritahun.before(sdf.parse("1975-02-10")) || (caritahun.equals(sdf.parse("1974-01-23"))) || (caritahun.equals(sdf.parse("1975-02-10"))))) {
                shio = "Tiger";
                S_elemen = " Wood";
            }
            if ((caritahun.after(sdf.parse("1975-02-11")) && caritahun.before(sdf.parse("1976-01-30")) || (caritahun.equals(sdf.parse("1975-02-11"))) || (caritahun.equals(sdf.parse("1976-01-30"))))) {
                shio = "Rabbit";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1976-01-31")) && caritahun.before(sdf.parse("1977-02-17")) || (caritahun.equals(sdf.parse("1976-01-31"))) || (caritahun.equals(sdf.parse("1977-02-17"))))) {
                shio = "Dragon";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1977-02-18")) && caritahun.before(sdf.parse("1978-02-06")) || (caritahun.equals(sdf.parse("1977-02-18"))) || (caritahun.equals(sdf.parse("1978-02-06"))))) {
                shio = "Snake";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1978-02-07")) && caritahun.before(sdf.parse("1979-01-27")) || (caritahun.equals(sdf.parse("1978-02-07"))) || (caritahun.equals(sdf.parse("1979-01-27"))))) {
                shio = "Horse";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1979-01-28")) && caritahun.before(sdf.parse("1980-02-15")) || (caritahun.equals(sdf.parse("1979-01-28"))) || (caritahun.equals(sdf.parse("1980-02-15"))))) {
                shio = "Sheep";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1980-02-16")) && caritahun.before(sdf.parse("1981-02-04")) || (caritahun.equals(sdf.parse("1980-02-16"))) || (caritahun.equals(sdf.parse("1981-02-04"))))) {
                shio = "Monkey";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1981-02-05")) && caritahun.before(sdf.parse("1982-01-24")) || (caritahun.equals(sdf.parse("1981-02-05"))) || (caritahun.equals(sdf.parse("1982-01-24"))))) {
                shio = "Rooster";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1982-01-25")) && caritahun.before(sdf.parse("1983-02-12")) || (caritahun.equals(sdf.parse("1982-01-25"))) || (caritahun.equals(sdf.parse("1983-02-12"))))) {
                shio = "Dog";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1983-02-13")) && caritahun.before(sdf.parse("1984-02-01")) || (caritahun.equals(sdf.parse("1983-02-13"))) || (caritahun.equals(sdf.parse("1984-02-01"))))) {
                shio = "Boar";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1984-02-02")) && caritahun.before(sdf.parse("1985-02-19")) || (caritahun.equals(sdf.parse("1984-02-02"))) || (caritahun.equals(sdf.parse("1985-02-19"))))) {
                shio = "Rat";
                S_elemen = " Wood";
            }
            if ((caritahun.after(sdf.parse("1985-02-20")) && caritahun.before(sdf.parse("1986-02-08")) || (caritahun.equals(sdf.parse("1985-02-20"))) || (caritahun.equals(sdf.parse("1986-02-08"))))) {
                shio = "Buffalo";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1986-02-09")) && caritahun.before(sdf.parse("1987-01-28")) || (caritahun.equals(sdf.parse("1986-02-09"))) || (caritahun.equals(sdf.parse("1987-01-28"))))) {
                shio = "Tiger";
                S_elemen = " Fire ";
            }
            if ((caritahun.after(sdf.parse("1987-01-29")) && caritahun.before(sdf.parse("1988-02-16")) || (caritahun.equals(sdf.parse("1987-01-29"))) || (caritahun.equals(sdf.parse("1988-02-16"))))) {
                shio = "Rabbit";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1988-02-17")) && caritahun.before(sdf.parse("1989-02-05")) || (caritahun.equals(sdf.parse("1988-02-17"))) || (caritahun.equals(sdf.parse("1989-02-05"))))) {
                shio = "Dragon";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1989-02-06")) && caritahun.before(sdf.parse("1990-01-26")) || (caritahun.equals(sdf.parse("1989-02-06"))) || (caritahun.equals(sdf.parse("1990-01-26"))))) {
                shio = "Snake";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1990-01-27")) && caritahun.before(sdf.parse("1991-02-14")) || (caritahun.equals(sdf.parse("1990-01-27"))) || (caritahun.equals(sdf.parse("1991-02-14"))))) {
                shio = "Horse";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1991-02-15")) && caritahun.before(sdf.parse("1992-02-03")) || (caritahun.equals(sdf.parse("1991-02-15"))) || (caritahun.equals(sdf.parse("1992-02-03"))))) {
                shio = "Sheep";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("1992-02-04")) && caritahun.before(sdf.parse("1993-01-22")) || (caritahun.equals(sdf.parse("1992-02-04"))) || (caritahun.equals(sdf.parse("1993-01-22"))))) {
                shio = "Monkey";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1993-01-23")) && caritahun.before(sdf.parse("1994-02-09")) || (caritahun.equals(sdf.parse("1993-01-23"))) || (caritahun.equals(sdf.parse("1994-02-09"))))) {
                shio = "Rooster";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("1994-02-10")) && caritahun.before(sdf.parse("1995-01-30")) || (caritahun.equals(sdf.parse("1994-02-10"))) || (caritahun.equals(sdf.parse("1995-01-30"))))) {
                shio = "Dog";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("1995-01-31")) && caritahun.before(sdf.parse("1996-02-18")) || (caritahun.equals(sdf.parse("1995-01-31"))) || (caritahun.equals(sdf.parse("1996-02-18"))))) {
                shio = "Boar";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("1996-02-19")) && caritahun.before(sdf.parse("1997-02-06")) || (caritahun.equals(sdf.parse("1996-02-19"))) || (caritahun.equals(sdf.parse("1997-02-06"))))) {
                shio = "Rat";
                S_elemen = " Fire ";
            }
            if ((caritahun.after(sdf.parse("1997-02-07")) && caritahun.before(sdf.parse("1998-01-27")) || (caritahun.equals(sdf.parse("1997-02-07"))) || (caritahun.equals(sdf.parse("1998-01-27"))))) {
                shio = "Buffalo";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("1998-01-28")) && caritahun.before(sdf.parse("1999-02-15")) || (caritahun.equals(sdf.parse("1998-01-28"))) || (caritahun.equals(sdf.parse("1999-02-15"))))) {
                shio = "Tiger";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("1999-02-16")) && caritahun.before(sdf.parse("2000-02-04")) || (caritahun.equals(sdf.parse("1999-02-16"))) || (caritahun.equals(sdf.parse("2000-02-04"))))) {
                shio = "Rabbit";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("2000-02-05")) && caritahun.before(sdf.parse("2001-01-23")) || (caritahun.equals(sdf.parse("2000-02-05"))) || (caritahun.equals(sdf.parse("2001-01-23"))))) {
                shio = "Dragon";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("2001-01-24")) && caritahun.before(sdf.parse("2002-02-11")) || (caritahun.equals(sdf.parse("2001-01-24"))) || (caritahun.equals(sdf.parse("2002-02-11"))))) {
                shio = "Snake";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("2002-02-12")) && caritahun.before(sdf.parse("2003-01-31")) || (caritahun.equals(sdf.parse("2002-02-12"))) || (caritahun.equals(sdf.parse("2003-01-31"))))) {
                shio = "Horse";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("2003-02-01")) && caritahun.before(sdf.parse("2004-01-21")) || (caritahun.equals(sdf.parse("2003-02-01"))) || (caritahun.equals(sdf.parse("2004-01-21"))))) {
                shio = "Sheep";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("2004-01-22")) && caritahun.before(sdf.parse("2005-02-08")) || (caritahun.equals(sdf.parse("2004-01-22"))) || (caritahun.equals(sdf.parse("2005-02-08"))))) {
                shio = "Monkey";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("2005-02-09")) && caritahun.before(sdf.parse("2006-01-28")) || (caritahun.equals(sdf.parse("2005-02-09"))) || (caritahun.equals(sdf.parse("2006-01-28"))))) {
                shio = "Rooster";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("2006-01-29")) && caritahun.before(sdf.parse("2007-02-17")) || (caritahun.equals(sdf.parse("2006-01-29"))) || (caritahun.equals(sdf.parse("2007-02-17"))))) {
                shio = "Dog";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("2007-02-18")) && caritahun.before(sdf.parse("2008-02-06")) || (caritahun.equals(sdf.parse("2007-02-18"))) || (caritahun.equals(sdf.parse("2008-02-06"))))) {
                shio = "Boar";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("2008-02-07")) && caritahun.before(sdf.parse("2009-01-25")) || (caritahun.equals(sdf.parse("2008-02-07"))) || (caritahun.equals(sdf.parse("2009-01-25"))))) {
                shio = "Rat";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("2009-01-26")) && caritahun.before(sdf.parse("2010-02-13")) || (caritahun.equals(sdf.parse("2009-01-26"))) || (caritahun.equals(sdf.parse("2010-02-13"))))) {
                shio = "Buffalo";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("2010-02-14")) && caritahun.before(sdf.parse("2011-02-02")) || (caritahun.equals(sdf.parse("2010-02-14"))) || (caritahun.equals(sdf.parse("2011-02-02"))))) {
                shio = "Tiger";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("2011-02-03")) && caritahun.before(sdf.parse("2012-01-22")) || (caritahun.equals(sdf.parse("2011-02-03"))) || (caritahun.equals(sdf.parse("2012-01-22"))))) {
                shio = "Rabbit";
                S_elemen = "Metal";
            }
            if ((caritahun.after(sdf.parse("2012-01-23")) && caritahun.before(sdf.parse("2013-02-09")) || (caritahun.equals(sdf.parse("2012-01-23"))) || (caritahun.equals(sdf.parse("2013-02-09"))))) {
                shio = "Dragon";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("2013-02-10")) && caritahun.before(sdf.parse("2014-01-30")) || (caritahun.equals(sdf.parse("2013-02-10"))) || (caritahun.equals(sdf.parse("2014-01-30"))))) {
                shio = "Snake";
                S_elemen = "Water";
            }
            if ((caritahun.after(sdf.parse("2014-01-31")) && caritahun.before(sdf.parse("2015-02-18")) || (caritahun.equals(sdf.parse("2014-01-31"))) || (caritahun.equals(sdf.parse("2015-02-18"))))) {
                shio = "Horse";
                S_elemen = "Wood ";
            }
            if ((caritahun.after(sdf.parse("2015-02-19")) && caritahun.before(sdf.parse("2016-02-07")) || (caritahun.equals(sdf.parse("2015-02-19"))) || (caritahun.equals(sdf.parse("2016-02-07"))))) {
                shio = "Sheep";
                S_elemen = "Wood";
            }
            if ((caritahun.after(sdf.parse("2016-02-08")) && caritahun.before(sdf.parse("2017-01-27")) || (caritahun.equals(sdf.parse("2016-02-08"))) || (caritahun.equals(sdf.parse("2017-01-27"))))) {
                shio = "Monkey";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("2017-01-28")) && caritahun.before(sdf.parse("2018-02-15")) || (caritahun.equals(sdf.parse("2017-01-28"))) || (caritahun.equals(sdf.parse("2018-02-15"))))) {
                shio = "Rooster";
                S_elemen = "Fire";
            }
            if ((caritahun.after(sdf.parse("2018-02-16")) && caritahun.before(sdf.parse("2019-02-04")) || (caritahun.equals(sdf.parse("2018-02-16"))) || (caritahun.equals(sdf.parse("2019-02-04"))))) {
                shio = "Dog";
                S_elemen = "Earth";
            }
            if ((caritahun.after(sdf.parse("2019-02-05")) && caritahun.before(sdf.parse("2020-01-24")) || (caritahun.equals(sdf.parse("2019-02-05"))) || (caritahun.equals(sdf.parse("2020-01-24"))))) {
                shio = "Boar";
                S_elemen = "Earth";
            }

            employee.setShio(shio);

            employee.setElemen(String.valueOf(S_elemen));

            employee.setIq(getString(FRM_FIELD_IQ));
            employee.setEq(getString(FRM_FIELD_EQ));
            employee.setNoKK(getString(FRM_FIELD_NO_KK));
            //added by dewok 20190610
            employee.setNamaPemilikRekening(getString(FRM_FIELD_NAMA_PEMILIK_REKENING));
            employee.setCabangBank(getString(FRM_FIELD_CABANG_BANK));
            employee.setKodeBank(getString(FRM_FIELD_KODE_BANK));
            employee.setTanggalPajakTerdaftar(getDate(FRM_FIELD_TANGGAL_PAJAK_TERDAFTAR));
            employee.setNamaBank(getString(FRM_FIELD_NAMA_BANK));
            employee.setAddressIdCard(getString(FRM_FIELD_ADDRESS_ID_CARD));

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

    // 2015-01-12 update by Hendra McHen

    public String[] getSelectedValues() {
        return SelectedValues;
    }

    // 2015-01-12 update by Hendra McHen
    public void setSelectedValues(String[] SelectedValues) {
        this.SelectedValues = SelectedValues;
    }

    // 2015-01-12 update by Hendra McHen

    public String getSelectedMedical() {
        String[] checks = getSelectedValues();
        String checkValues = "";
        for (int i = 0; i < checks.length; ++i) {
            if (i != checks.length - 1) {
                checkValues = checkValues + checks[i] + ",";
            } else {
                checkValues = checkValues + checks[i];
            }
        }
        return checkValues;
    }
}
