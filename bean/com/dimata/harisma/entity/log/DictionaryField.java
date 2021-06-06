/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.log;

import java.util.Hashtable;

/**
 *
 * @author Dimata 007
 */
public class DictionaryField {
    /*Employee Education*/
    public final static String EMP_EDUCATION_ID = "EMP_EDUCATION_ID" ;
    public final static String EDUCATION_ID = "EDUCATION_ID" ;
    public final static String EMPLOYEE_ID = "EMPLOYEE_ID" ;
    public final static String START_DATE = "START_DATE" ;
    public final static String END_DATE = "END_DATE" ;
    public final static String GRADUATION = "GRADUATION" ;
    public final static String EDUCATION_DESC = "EDUCATION_DESC" ;
    public final static String POINT = "POINT" ;
    public final static String INSTITUTION_ID = "INSTITUTION_ID" ;
    
    /*Company*/
    public final static String COMPANY_ID = "COMPANY_ID";
    public final static String DIVISION_ID = "DIVISION_ID";
    public final static String DEPARTMENT_ID = "DEPARTMENT_ID";
    public final static String SECTION_ID = "SECTION_ID";
    public final static String POSITION_ID = "POSITION_ID";
    public final static String LEVEL_ID = "LEVEL_ID";
    public final static String EMP_CATEGORY_ID = "EMP_CATEGORY_ID";
    
    /*Employee Competency*/
    public final static String EMP_COMP_ID = "EMP_COMP_ID";
    public final static String COMPETENCY_ID = "COMPETENCY_ID";
    public final static String LEVEL_VALUE = "LEVEL_VALUE";
    public final static String SPECIAL_ACHIEVEMENT = "SPECIAL_ACHIEVEMENT";
    public final static String DATE_OF_ACHVMT = "DATE_OF_ACHVMT";
    public final static String HISTORY = "HISTORY";
    public final static String PROVIDER_ID = "PROVIDER_ID";
    
    /*Employee Reprimand*/
    public final static String REPRIMAND_ID = "REPRIMAND_ID";
    public final static String NUMBER = "NUMBER";
    public final static String CHAPTER = "CHAPTER";
    public final static String ARTICLE = "ARTICLE";
    public final static String PAGE = "PAGE";
    public final static String DESCRIPTION = "DESCRIPTION";
    public final static String REP_DATE = "REPRIMAND_DATE";
    public final static String VALIDITY = "VALID_UNTIL";
    public final static String REPRIMAND_LEVEL_ID = "REPRIMAND_LEVEL_ID";
    public final static String VERSE = "VERSE";
    
    /*Employee Warning*/
    public final static String WARNING_ID = "WARNING_ID";
    public final static String BREAK_FACT = "BREAK_FACT";
    public final static String BREAK_DATE = "BREAK_DATE";
    public final static String WARN_BY = "WARN_BY";
    public final static String WARN_DATE = "WARN_DATE";
    public final static String WARN_LEVEL_ID = "WARN_LEVEL_ID";
    
    /*Employee Training*/
    public final static String TRAINING_HISTORY_ID = "TRAINING_HISTORY_ID";
    public final static String TRAINING_PROGRAM = "TRAINING_PROGRAM";
    public final static String TRAINER = "TRAINER";
    public final static String REMARK = "REMARK";
    public final static String TRAINING_ID = "TRAINING_ID";
    public final static String DURATION = "DURATION";
    public final static String PRESENCE = "PRESENCE";
    public final static String START_TIME = "START_TIME";
    public final static String END_TIME = "END_TIME";
    public final static String TRAINING_ACTIVITY_PLAN_ID = "TRAINING_ACTIVITY_PLAN_ID";
    public final static String TRAINING_ACTIVITY_ACTUAL_ID = "TRAINING_ACTIVITY_ACTUAL_ID";
    public final static String NOMOR_SK = "NOMOR_SK";
    public final static String TANGGAL_SK = "TANGGAL_SK";
    public final static String EMP_DOC_ID  = "EMP_DOC_ID";
    public final static String TRAINING_TITLE = "TRAINING_TITLE";
    
    /*Employee Experience*/
    public final static String WORK_HISTORY_PAST_ID = "WORK_HISTORY_PAST_ID";
    public final static String COMPANY_NAME = "COMPANY_NAME";
    public final static String POSITION = "POSITION";
    public final static String MOVE_REASON = "MOVE_REASON";
    
    /*Employee Award*/
    public final static String AWARD_ID = "AWARD_ID";
    public final static String EMP_ID = "EMP_ID";
    public final static String DEPT_ID = "DEPT_ID";
    public final static String SECT_ID = "SECT_ID";
    public final static String DATE = "DATE";
    public final static String TYPE = "TYPE";
    public final static String TITLE = "TITLE";
    
    /*Employee Relevant Doc*/
    public final static String DOC_RELEVANT_ID = "DOC_RELEVANT_ID";
    public final static String DOC_TITLE = "DOC_TITLE";
    public final static String DOC_DESCRIPTION = "DOC_DESCRIPTION";
    public final static String FILE_NAME = "FILE_NAME";
    public final static String DOC_ATTACH_FILE = "DOC_ATTACH_FILE";
    public final static String EMP_RELVT_DOC_GRP_ID = "EMP_RELVT_DOC_GRP_ID";
    
    /*Employee Relevant Doc Page*/
    public final static String EMP_RELVT_DOC_PAGE_ID = "EMP_RELVT_DOC_PAGE_ID";
    public final static String PAGE_TITLE = "PAGE_TITLE";
    public final static String PAGE_DESC = "PAGE_DESC";
    
    /* Family Member */
    public final static String FAMILY_MEMBER_ID = "FAMILY_MEMBER_ID";
    public final static String ADDRESS = "ADDRESS";
    public final static String BIRTH_DATE = "BIRTH_DATE";
    public final static String BPJS_NUM = "BPJS_NUM";
    public final static String CARD_ID = "CARD_ID";
    public final static String FULL_NAME = "FULL_NAME";
    public final static String GUARANTEED = "GUARANTEED";
    public final static String IGNORE_BIRTH = "IGNORE_BIRTH";
    public final static String JOB = "JOB";
    public final static String JOB_PLACE = "JOB_PLACE";
    public final static String JOB_POSITION = "JOB_POSITION";
    public final static String NO_TELP = "NO_TELP";
    public final static String RELATIONSHIP = "RELATIONSHIP";
    public final static String RELIGION_ID = "RELIGION_ID";
    
    /* Data Pribadi*/
    public final static String EMPLOYEE_NUM = "EMPLOYEE_NUM";
    public final static String PHONE = "PHONE";
    public final static String HANDPHONE = "HANDPHONE";
    public final static String POSTAL_CODE = "POSTAL_CODE";
    public final static String SEX = "SEX";
    public final static String BIRTH_PLACE = "BIRTH_PLACE";
    public final static String BLOOD_TYPE = "BLOOD_TYPE";
    public final static String ASTEK_NUM = "ASTEK_NUM";
    public final static String ASTEK_DATE = "ASTEK_DATE";
    public final static String MARITAL_ID = "MARITAL_ID";
    public final static String LOCKER_ID = "LOCKER_ID";
    public final static String COMMENCING_DATE = "COMMENCING_DATE";
    public final static String RESIGNED = "RESIGNED";
    public final static String RESIGNED_DATE = "RESIGNED_DATE";
    public final static String BARCODE_NUMBER = "BARCODE_NUMBER";
    public final static String RESIGNED_REASON_ID = "RESIGNED_REASON_ID";
    public final static String RESIGNED_DESC = "RESIGNED_DESC";
    public final static String BASIC_SALARY = "BASIC_SALARY";
    public final static String ASSIGN_TO_ACCOUNTING = "ASSIGN_TO_ACCOUNTING";
    public final static String CURIER = "CURIER";
    public final static String INDENT_CARD_NR = "INDENT_CARD_NR";
    public final static String INDENT_CARD_VALID_TO = "INDENT_CARD_VALID_TO";
    public final static String TAX_REG_NR = "TAX_REG_NR";
    public final static String ADDRESS_FOR_TAX = "ADDRESS_FOR_TAX";
    public final static String NATIONALITY_TYPE = "NATIONALITY_TYPE";
    public final static String EMAIL_ADDRESS = "EMAIL_ADDRESS";
    public final static String CATEGORY_DATE = "CATEGORY_DATE";
    public final static String LEAVE_STATUS = "LEAVE_STATUS";
    public final static String EMP_PIN = "EMP_PIN";
    public final static String RACE = "RACE";
    public final static String ADDRESS_PERMANENT = "ADDRESS_PERMANENT";
    public final static String PHONE_EMERGENCY = "PHONE_EMERGENCY";
    public final static String FATHER = "FATHER";
    public final static String MOTHER = "MOTHER";
    public final static String PARENTS_ADDRESS = "PARENTS_ADDRESS";
    public final static String NAME_EMG = "NAME_EMG";
    public final static String PHONE_EMG = "PHONE_EMG";
    public final static String ADDRESS_EMG = "ADDRESS_EMG";
    public final static String HOD_EMPLOYEE_ID = "HOD_EMPLOYEE_ID";
    public final static String ADDR_COUNTRY_ID = "ADDR_COUNTRY_ID";
    public final static String ADDR_PROVINCE_ID = "ADDR_PROVINCE_ID";
    public final static String ADDR_REGENCY_ID = "ADDR_REGENCY_ID";
    public final static String ADDR_SUBREGENCY_ID = "ADDR_SUBREGENCY_ID";
    public final static String ADDR_PMNT_COUNTRY_ID = "ADDR_PMNT_COUNTRY_ID";
    public final static String ADDR_PMNT_PROVINCE_ID = "ADDR_PMNT_PROVINCE_ID";
    public final static String ADDR_PMNT_REGENCY_ID = "ADDR_PMNT_REGENCY_ID";
    public final static String ADDR_PMNT_SUBREGENCY_ID = "ADDR_PMNT_SUBREGENCY_ID";
    public final static String ID_CARD_ISSUED_BY = "ID_CARD_ISSUED_BY";
    public final static String ID_CARD_BIRTH_DATE = "ID_CARD_BIRTH_DATE";
    public final static String TAX_MARITAL_ID = "TAX_MARITAL_ID";
    public final static String NO_REKENING = "NO_REKENING";
    public final static String GRADE_LEVEL_ID = "GRADE_LEVEL_ID";
    public final static String LOCATION_ID = "LOCATION_ID";
    public final static String END_CONTRACT = "END_CONTRACT";
    public final static String WORK_ASSIGN_COMPANY_ID = "WORK_ASSIGN_COMPANY_ID";
    public final static String WORK_ASSIGN_DIVISION_ID = "WORK_ASSIGN_DIVISION_ID";
    public final static String WORK_ASSIGN_DEPARTMENT_ID = "WORK_ASSIGN_DEPARTMENT_ID";
    public final static String WORK_ASSIGN_SECTION_ID = "WORK_ASSIGN_SECTION_ID";
    public final static String WORK_ASSIGN_POSITION_ID = "WORK_ASSIGN_POSITION_ID";
    public final static String ID_CARD_TYPE = "ID_CARD_TYPE";
    public final static String NPWP = "NPWP";
    public final static String BPJS_NO = "BPJS_NO";
    public final static String BPJS_DATE = "BPJS_DATE";
    public final static String SHIO = "SHIO";
    public final static String ELEMEN = "ELEMEN";
    public final static String IQ = "IQ";
    public final static String EQ = "EQ";
    public final static String PROBATION_END_DATE = "PROBATION_END_DATE";
    public final static String STATUS_PENSIUN_PROGRAM = "STATUS_PENSIUN_PROGRAM";
    public final static String START_DATE_PENSIUN = "START_DATE_PENSIUN";
    public final static String PRESENCE_CHECK_PARAMETER = "PRESENCE_CHECK_PARAMETER";
    public final static String MEDICAL_INFO = "MEDICAL_INFO";
    public final static String DANA_PENDIDIKAN = "DANA_PENDIDIKAN";
    public final static String PAYROLL_GROUP = "PAYROLL_GROUP";
    public final static String MEMBER_OF_KESEHATAN = "MEMBER_OF_KESEHATAN";
    public final static String MEMBER_OF_KETENAGAKERJAAN = "MEMBER_OF_KETENAGAKERJAAN";
    public final static String HISTORY_GROUP = "HISTORY_GROUP";
    public final static String HISTORY_TYPE = "HISTORY_TYPE";
    
    static Hashtable <String,String> dictionary = null;
    
    public String getWord(String key) {
        return dictionary.get(key);
    }

    public void loadWord() {
        if (dictionary == null ){
            dictionary =  new Hashtable<String, String>();
            
        }
        /*Employee Education*/
        dictionary.put(EMP_EDUCATION_ID,"Emp Edu ID");
        dictionary.put(EDUCATION_ID,"Pendidikan");
        dictionary.put(EMPLOYEE_ID,"Karyawan");
        dictionary.put(START_DATE,"Tgl Mulai");
        dictionary.put(END_DATE,"Tgl Berakhir");
        dictionary.put(GRADUATION,"Angkatan");
        dictionary.put(EDUCATION_DESC,"Keterangan Pendidikan");
        dictionary.put(POINT,"Poin");
        dictionary.put(INSTITUTION_ID,"Institusi");
        /*Company*/
        dictionary.put(COMPANY_ID, "Company");
        dictionary.put(DIVISION_ID, "Division");
        dictionary.put(DEPARTMENT_ID, "Department");
        dictionary.put(SECTION_ID, "Section");
        dictionary.put(POSITION_ID, "Position");
        dictionary.put(LEVEL_ID, "Level");
        dictionary.put(EMP_CATEGORY_ID, "Employee Category");
        /*Employee Competency*/
        dictionary.put(EMP_COMP_ID, "Emp Comp ID");
        dictionary.put(COMPETENCY_ID, "Kompetensi");
        dictionary.put(LEVEL_VALUE,"Level Value");
        dictionary.put(SPECIAL_ACHIEVEMENT, "Special Achievement");
        dictionary.put(DATE_OF_ACHVMT, "Date of Achievement");
        dictionary.put(HISTORY, "History");
        dictionary.put(PROVIDER_ID, "Provider");
        /*Employee Reprimand*/
        dictionary.put(REPRIMAND_ID,"Reprimand ID");
        dictionary.put(NUMBER, "Number");
        dictionary.put(CHAPTER,"Chapter/Bab");
        dictionary.put(ARTICLE,"Article/Pasal");
        dictionary.put(PAGE,"Page/Halaman");
        dictionary.put(DESCRIPTION, "Deskripsi/Uraian");
        dictionary.put(REP_DATE, "Date/Tanggal");
        dictionary.put(VALIDITY, "Valid Until");
        dictionary.put(REPRIMAND_LEVEL_ID, "Reprimand Level (Point)");
        dictionary.put(VERSE ,"Verse/Ayat");
        /*Employee Warning*/
        dictionary.put(WARNING_ID,"Warning ID");
        dictionary.put(BREAK_FACT, "Break Fact");
        dictionary.put(BREAK_DATE, "Break Date");
        dictionary.put(WARN_BY, "Warning By");
        dictionary.put(WARN_DATE, "Warning Date");
        dictionary.put(WARN_LEVEL_ID, "Warning Level (Point)");
        /*Employee Training*/
        dictionary.put(TRAINING_HISTORY_ID, "Training ID");
        dictionary.put(TRAINING_PROGRAM, "Training Program");
        dictionary.put(TRAINER, "Trainer");
        dictionary.put(REMARK,"Remark");
        dictionary.put(TRAINING_ID, "Training Program ID");
        dictionary.put(DURATION, "Duration");
        dictionary.put(PRESENCE, "Presence");
        dictionary.put(START_TIME, "Start Time");
        dictionary.put(END_TIME, "End Time");
        dictionary.put(TRAINING_ACTIVITY_PLAN_ID, "Training Activity Plan ID");
        dictionary.put(TRAINING_ACTIVITY_ACTUAL_ID, "Training Activity Actual ID");
        dictionary.put(NOMOR_SK, "Nomor SK");
        dictionary.put(TANGGAL_SK, "Tanggal SK");
        dictionary.put(EMP_DOC_ID,  "ID Dokumen");
        dictionary.put(TRAINING_TITLE, "Training Title");
        /*Employee Experience*/
        dictionary.put(WORK_HISTORY_PAST_ID, "Experience ID");
        dictionary.put(COMPANY_NAME, "Company Name");
        dictionary.put(POSITION, "Position");
        dictionary.put(MOVE_REASON, "Move Reason");
        /*Employee Award*/
        dictionary.put(AWARD_ID, "Award ID");
        dictionary.put(EMP_ID, "Karyawan");
        dictionary.put(DEPT_ID, "Department");
        dictionary.put(SECT_ID, "Section");
        dictionary.put(DATE, "Tanggal");
        dictionary.put(TYPE, "Tipe");
        dictionary.put(TITLE, "Title");
        /*Employee Relevant Doc*/
        dictionary.put(DOC_RELEVANT_ID, "Relevant Doc ID");
        dictionary.put(DOC_TITLE, "Title");
        dictionary.put(DOC_DESCRIPTION, "Deskripsi");
        dictionary.put(FILE_NAME, "Nama Dokumen");
        dictionary.put(DOC_ATTACH_FILE, "Dokumen Terunggah");
        dictionary.put(EMP_RELVT_DOC_GRP_ID, "Dokumen Group");
        /*Employee Relevant Doc Page*/
        dictionary.put(EMP_RELVT_DOC_PAGE_ID, "Dokumen ID");
        dictionary.put(PAGE_TITLE, "Title");
        dictionary.put(PAGE_DESC, "Deskripsi");
        /* Family Member */
        dictionary.put(FAMILY_MEMBER_ID, "Family Member ID");
        dictionary.put(ADDRESS, "Address");
        dictionary.put(BIRTH_DATE, "Birth Date");
        dictionary.put(BPJS_NUM, "BPJS Num");
        dictionary.put(CARD_ID, "Card ID");
        dictionary.put(FULL_NAME, "Fullname");
        dictionary.put(GUARANTEED, "Guaranteed");
        dictionary.put(IGNORE_BIRTH, "Ignore Birth");
        dictionary.put(JOB, "Job");
        dictionary.put(JOB_PLACE, "Job Place");
        dictionary.put(JOB_POSITION, "Job Position");
        dictionary.put(NO_TELP, "No Telp");
        dictionary.put(RELATIONSHIP, "Relationship");
        dictionary.put(RELIGION_ID, "Religion ID");
        /* Data pribadi */
        dictionary.put(EMPLOYEE_NUM, "Employee Num");
        dictionary.put(PHONE, "Phone");
        dictionary.put(HANDPHONE, "Handphone");
        dictionary.put(POSTAL_CODE, "Postal Code");
        dictionary.put(SEX, "Sex");
        dictionary.put(BIRTH_PLACE, "Birth Place");
        dictionary.put(BLOOD_TYPE, "Blood Type");
        dictionary.put(ASTEK_NUM, "Astek Num");
        dictionary.put(ASTEK_DATE, "Astek Date");
        dictionary.put(MARITAL_ID, "Marital ID");
        dictionary.put(LOCKER_ID, "Locker ID");
        dictionary.put(COMMENCING_DATE, "Commencing Date");
        dictionary.put(RESIGNED, "Resigned");
        dictionary.put(RESIGNED_DATE, "Resigned Date");
        dictionary.put(BARCODE_NUMBER, "Barcode Number");
        dictionary.put(RESIGNED_REASON_ID, "Resigned Reason ID");
        dictionary.put(RESIGNED_DESC, "Resigned Desc");
        dictionary.put(BASIC_SALARY, "Basic Salary");
        dictionary.put(ASSIGN_TO_ACCOUNTING, "Assign to Accounting");
        dictionary.put(CURIER, "Curier");
        dictionary.put(INDENT_CARD_NR, "Indent Card NR");
        dictionary.put(INDENT_CARD_VALID_TO, "Indent Card Valid to");
        dictionary.put(TAX_REG_NR, "Tax Reg NR");
        dictionary.put(ADDRESS_FOR_TAX, "Address For Tax");
        dictionary.put(NATIONALITY_TYPE, "Nationality Type");
        dictionary.put(EMAIL_ADDRESS, "Email Address");
        dictionary.put(CATEGORY_DATE, "Category Date");
        dictionary.put(LEAVE_STATUS, "Leave Status");
        dictionary.put(EMP_PIN, "Emp Pin");
        dictionary.put(RACE, "Race");
        dictionary.put(ADDRESS_PERMANENT, "Address Permanent");
        dictionary.put(PHONE_EMERGENCY, "Phone Emergency");
        dictionary.put(FATHER, "Father");
        dictionary.put(MOTHER, "Mother");
        dictionary.put(PARENTS_ADDRESS, "Parents Address");
        dictionary.put(NAME_EMG, "Name Emg");
        dictionary.put(PHONE_EMG, "Phone Emg");
        dictionary.put(ADDRESS_EMG, "Address Emg");
        dictionary.put(HOD_EMPLOYEE_ID, "HOD Employee ID");
        dictionary.put(ADDR_COUNTRY_ID, "Address Country ID");
        dictionary.put(ADDR_PROVINCE_ID, "Address Province ID");
        dictionary.put(ADDR_REGENCY_ID, "Address Regency ID");
        dictionary.put(ADDR_SUBREGENCY_ID, "Address Subregency ID");
        dictionary.put(ADDR_PMNT_COUNTRY_ID, "Address Permanent Country ID");
        dictionary.put(ADDR_PMNT_PROVINCE_ID, "Address Permanent Province ID");
        dictionary.put(ADDR_PMNT_REGENCY_ID, "Address Permanent Regency ID");
        dictionary.put(ADDR_PMNT_SUBREGENCY_ID, "Address Permanent Subregency ID");
        dictionary.put(ID_CARD_ISSUED_BY, "ID Card Issued By");
        dictionary.put(ID_CARD_BIRTH_DATE, "ID Card Birth Date");
        dictionary.put(TAX_MARITAL_ID, "Tax Marital");
        dictionary.put(NO_REKENING, "No Rekening");
        dictionary.put(GRADE_LEVEL_ID, "Grade Level ID");
        dictionary.put(LOCATION_ID, "Location ID");
        dictionary.put(END_CONTRACT, "End Contract");
        dictionary.put(WORK_ASSIGN_COMPANY_ID, "Work Assign Company ID");
        dictionary.put(WORK_ASSIGN_DIVISION_ID, "Work Assign Division ID");
        dictionary.put(WORK_ASSIGN_DEPARTMENT_ID, "Work Assign Department ID");
        dictionary.put(WORK_ASSIGN_SECTION_ID, "Work Assign Section ID");
        dictionary.put(WORK_ASSIGN_POSITION_ID, "Work Assign Position ID");
        dictionary.put(ID_CARD_TYPE, "ID Card Type");
        dictionary.put(NPWP, "NPWP");
        dictionary.put(BPJS_NO, "BPJS No");
        dictionary.put(BPJS_DATE, "BPJS Date");
        dictionary.put(SHIO, "Shio");
        dictionary.put(ELEMEN, "Elemen");
        dictionary.put(IQ, "IQ");
        dictionary.put(EQ, "EQ");
        dictionary.put(PROBATION_END_DATE, "Probation End Date");
        dictionary.put(STATUS_PENSIUN_PROGRAM, "Status Pensiun Program");
        dictionary.put(START_DATE_PENSIUN, "Start Date Pensiun");
        dictionary.put(PRESENCE_CHECK_PARAMETER, "Presence Check Parameter");
        dictionary.put(MEDICAL_INFO, "Medical Info");
        dictionary.put(DANA_PENDIDIKAN, "Dana Pendidikan");
        dictionary.put(PAYROLL_GROUP, "Payroll Group");
        dictionary.put(MEMBER_OF_KESEHATAN, "Member of Kesehatan");
        dictionary.put(MEMBER_OF_KETENAGAKERJAAN, "Member of Ketenagakerjaan");
        dictionary.put(HISTORY_GROUP, "History Group");
        dictionary.put(HISTORY_TYPE, "History Type");
    }

}
