/*
 * AppObjInfo.java
 *
 * Created on April 3, 2002, 4:09 PM
 * Modified on November 27, 2002, 10:14 AM by karya
 */

package com.dimata.harisma.entity.admin;

import java.util.*;

/**
 *
 * @author  ktanjana
 * @version
 * @Purpose Describe application object as binary coded (integer)
 * @CodeMapping
 * bit 0 - 7   : command CMD
 * bit 8 - 15  : page , menu or other objects  OBJ
 * bit 16 - 23 :  level 2 sub-application G2
 * bit 24 - 31 :  level 1 sub-application G1
 */
public class AppObjInfo {
    
    /** Creates new AppObjInfo */
    public AppObjInfo() {
    }
    
    /*
     * =========================================================================
     * FILTER CODE
     * =========================================================================
     */
    public static final int FILTER_CODE_G1  =   0xFF000000;
    public static final int FILTER_CODE_G2  =   0x00FF0000;
    public static final int FILTER_CODE_OBJ =   0x0000FF00;
    public static final int FILTER_CODE_CMD =   0x000000FF;
    
    public static final int SHIFT_CODE_G1   = 24;
    public static final int SHIFT_CODE_G2   = 16;
    public static final int SHIFT_CODE_OBJ  = 8;
    //public static final int SHIFT_CODE_CMD=0;
    /*
     * =========================================================================
     * OBJECT COMMAND
     * =========================================================================
     */
    public static final int COMMAND_VIEW    = 0;
    public static final int COMMAND_ADD     = 1;
    public static final int COMMAND_UPDATE  = 2;
    public static final int COMMAND_DELETE  = 3;
    public static final int COMMAND_PRINT   = 4;
    public static final int COMMAND_SUBMIT  = 5;
    public static final int COMMAND_START   = 6;
    public static final int COMMAND_STOP    = 7;
    //update 2013-05-6
    public static final int COMMAND_VIEW_DETAIL    = 8;
    /* update 2015-05-05 */
    public static final int COMMAND_GENERATE_SALARY_LEVEL = 9;
    /* Update by Hendra Putu | 2016-04-05 */
    public static final int COMMAND_RUN_PROCESS = 10;
    public static final String[] strCommand = {
        "View", "Add New", "Update", "Delete",
        "Print", "Submit", "Start", "Stop", "View Detail", "Generate Salary Level", "Run Process"
    };

    /*
     * =========================================================================
     * APPLICATION STRUCTURE
     * =========================================================================
     */
        
    /**
     * ***** [0] MODULE LOGGIN ******
     */
    public static final int G1_LOGIN = 0;
        public static final int G2_LOGIN = 0;
            public static final int OBJ_LOGIN_LOGIN = 0;
            public static final int OBJ_LOGIN_LOGOUT = 1;
        public static final int G2_MENU = 1;
            public static final int OBJ_MENU = 0;
            public static final int OBJ_MENU_EMPLOYEE = 1;
        
    /**
     * ***** [1] MODULE EMPLOYEE ******
     */
    public static final int G1_EMPLOYEE = 1;
        public static final int G2_DATABANK = 0;
            public static final int OBJ_DATABANK = 0;
            /* Add object by Hendra McHen | 2016-03-23 */
            /* Description ::
             * personal data, family member, competence, education, experience, 
             * career path, training, warning, reprimand, award, picture, relevant document
             */
            public static final int OBJ_DATABANK_PERSONAL_DATA = 1;
            public static final int OBJ_DATABANK_FAMILY_MEMBER = 2;
            public static final int OBJ_DATABANK_LANG_N_COMPETENCE = 3;
            public static final int OBJ_DATABANK_EDUCATION = 4;
            public static final int OBJ_DATABANK_EXPERIENCE = 5;
            public static final int OBJ_DATABANK_CAREERPATH = 6;
            public static final int OBJ_DATABANK_TRAINING = 7;
            public static final int OBJ_DATABANK_WARNING = 8;
            public static final int OBJ_DATABANK_REPRIMAND = 9;
            public static final int OBJ_DATABANK_AWARD = 10;
            public static final int OBJ_DATABANK_PICTURE = 11;
            public static final int OBJ_DATABANK_RELEVANT_DOC = 12;
        public static final int G2_ATTENDANCE = 1;
            public static final int OBJ_WORKING_SCHEDULE = 0;
            public static final int OBJ_LEAVE_OPNAME = 1;
            public static final int OBJ_LEAVE_MANAGEMENT = 2;
            public static final int OBJ_DAY_OFF_PAYMENT = 3;
            public static final int OBJ_PRESENCE = 4;
            public static final int OBJ_MANUAL_PRESENCE = 5;
            public static final int OBJ_MANUAL_REGISTRATION = 6;
            public static final int OBJ_REGENERATE_SCHEDULE_HOLIDAYS = 7;
        public static final int G2_SALARY = 2;
            public static final int OBJ_SALARY = 0;
        public static final int G2_APPRAISAL = 3;
            public static final int OBJ_EXPLANATION_COVERAGE = 0;
            public static final int OBJ_PERFORMANCE_APPRAISAL = 1;
        public static final int G2_RECOGNITION = 4;
            public static final int OBJ_ENTRY_PER_DEPT = 0;
            public static final int OBJ_UPDATE_PER_EMPLOYEE = 1;
        public static final int G2_TRAINING = 5;
            public static final int OBJ_TRAINING_HISTORY = 0;
            public static final int OBJ_SPECIAL_ARCHIEVEMENT = 1;
            public static final int OBJ_TRAINING_ACTIVITIES = 2;
            public static final int OBJ_TRAINING_SEARCH = 3;
            public static final int OBJ_TRAINING_ACHIEVE_EDIT = 4;
            public static final int OBJ_TRAINING_ACHIEVE_LIST = 5;
            public static final int OBJ_GENERAL_TRAINING = 6;
            public static final int OBJ_DEPARTMENTAL_TRAINING = 7;
        public static final int G2_RECRUITMENT = 6;
            public static final int OBJ_STAFF_REQUISITION = 0;
            public static final int OBJ_EMPLOYEE_APPLICATION = 1;
            public static final int OBJ_ORIENTATION_CHECK_LIST = 2;
            public static final int OBJ_REMINDER = 3;
            public static final int OBJ_EMPLOYEE_RECRUITMENT = 4;
            public static final int OBJ_EMPLOYEE_RECRUITMENT_EDUCATION = 5;
            public static final int OBJ_EMPLOYEE_RECRUITMENT_HISTORY = 6;
            public static final int OBJ_EMPLOYEE_RECRUITMENT_LANGGUAGE = 7;
            public static final int OBJ_EMPLOYEE_RECRUITMENT_REFERENCES = 8;
            public static final int OBJ_EMPLOYEE_RECRUITMENT_GENERAL = 9;
            public static final int OBJ_EMPLOYEE_RECRUITMENT_INTERVIEW = 10;
        public static final int G2_LEAVE_MANAGEMENT = 7;
            public static final int OBJ_LEAVE_DP_MANAGEMENT = 0;
            public static final int OBJ_LEAVE_AL_MANAGEMENT = 1;
            public static final int OBJ_LEAVE_LL_MANAGEMENT = 2;
            public static final int OBJ_LEAVE_OPNAME_AL = 3;
            public static final int OBJ_LEAVE_OPNAME_LL = 4;
            public static final int OBJ_LEAVE_OPNAME_DP = 5;
            public static final int OBJ_LEAVE_AL_CLOSING = 6;
            public static final int OBJ_LEAVE_LL_CLOSING = 7;
            public static final int OBJ_LEAVE_APP_SRC = 8;
        public static final int G2_LEAVE_APPLICATION = 8;
            public static final int OBJ_DP_APPLICATION = 0;
            public static final int OBJ_LEAVE_APPLICATION = 1;
            public static final int OBJ_LEAVE_FORM = 2;
            public static final int OBJ_LEAVE_APPLICATION_AL_CLOSING = 3;
            public static final int OBJ_LEAVE_LI_CLOSING = 4;
            public static final int OBJ_DP_MANAGEMENT = 5;
            public static final int OBJ_DP_RECALCULATE = 6;
        public static final int G2_ABSENCE_MANAGEMENT = 9;
            public static final int OBJ_ABSENCE_MANAGEMENT = 0;
        public static final int G2_COMPANY_STRUCTURE = 10;
            public static final int OBJ_COMPANY_STRUCTURE = 0;
        public static final int G2_NEW_EMPLOYEE = 11;
            public static final int OBJ_NEW_EMPLOYEE = 0;
        public static final int G2_WARNING_AND_REPRIMAND = 12;
            public static final int OBJ_WARNING = 0;
            public static final int OBJ_REPRIMAND = 1;
        public static final int G2_EXCUSE_APPLICATION = 13;
            public static final int OBJ_EXCUSE_FORM = 0;
        public static final int G2_LEAVE_BALANCING = 14;
            public static final int OBJ_LEAVE_BALANCING_ANNUAL_LEAVE = 0;
            public static final int OBJ_LEAVE_BALANCING_LONG_LEAVE = 1;
            public static final int OBJ_LEAVE_BALANCING_DAY_OFF_PAYMENT = 2;
        public static final int G2_ASSESSMENT = 15;
            public static final int OBJ_ASSESSMENT_EXPLANATION_COVERAGE = 0;
            public static final int OBJ_ASSESSMENT_PERFORMANCE_ASSESSMENT = 1;
        public static final int G2_OUTLET = 16;
            public static final int OBJ_EMPLOYEE_OUTLET = 0;
            public static final int OBJ_MAPPING_KADIV = 1;
        //update by satrya 2014-03-20
        public static final int G2_EXTRA_SCHEDULE = 17;
            public static final int OBJ_EXTRA_SCHEDULE_FORM = 0;
            public static final int OBJ_EXTRA_SCHEDULE_LIST = 1;
        /* Add By Hendra Putu | Desember 22, 2015 */
        /* [18] Candidate */
        public static final int G2_CANDIDATE = 18;
            public static final int OBJ_CANDIDATE_SEARCH = 0;
            public static final int OBJ_CANDIDATE_ROTATION = 1;
            public static final int OBJ_CANDIDATE_ANALYSIS = 2;
            public static final int OBJ_CANDIDATE_LIST = 3;
            public static final int OBJ_CANDIDATE_POSITION_REQ = 4;
        /* [19] Kompetensi */
        public static final int G2_COMPETENCE = 19;
            public static final int OBJ_COMPETENCE = 0;
            public static final int OBJ_COMPETENCE_TYPE = 1;
            public static final int OBJ_COMPETENCE_GROUP = 2;
            public static final int OBJ_COMPETENCE_LEVEL = 3;
            public static final int OBJ_COMPETENCE_DETAIL = 4;
        /* [20] Performa */
        public static final int G2_PERFORMANCE = 20;
            public static final int OBJ_KPI_COMPANY_TARGET = 0;
            public static final int OBJ_KPI_TYPE = 1;
            public static final int OBJ_KPI_GROUP = 2;
            public static final int OBJ_KPI_LIST = 3;
            public static final int OBJ_KPI_ACHIEV_SCORE = 4;
            public static final int OBJ_COMPETITOR = 5;
            
    /**
     * ***** [2] MODULE LOCKER ******
     */
    public static final int G1_LOCKER = 2;
        public static final int G2_LOCKER = 0;
            public static final int OBJ_LOCKER = 0;
        public static final int G2_LOCKER_TREATMENT = 1;
            public static final int OBJ_LOCKER_TREATMENT = 0;    
  

    /**
     * ***** [3] MODULE CANTEEN ******
     */
    public static final int G1_CANTEEN = 3;
        public static final int G2_MENU_LIST_MONTHLY = 0;
            public static final int OBJ_MENU_LIST_MONTHLY = 0;
        public static final int G2_DAILY_EVALUATION = 1;
            public static final int OBJ_DAILY_EVALUATION = 0;
        public static final int G2_COMMENT_CARD = 2;
            public static final int OBJ_COMMENT_CARD = 0;
        public static final int G2_REPORT_SUMMARY = 3;
            public static final int OBJ_SUMMARY_DAILY = 0;
            public static final int OBJ_SUMMARY_WEEKLY = 1;
            public static final int OBJ_SUMMARY_MONTHLY = 2;
        public static final int G2_REPORT_DETAIL = 4;
            public static final int OBJ_DETAIL_DAILY = 0;
            public static final int OBJ_DETAIL_WEEKLY = 1;
            public static final int OBJ_DETAIL_MONTHLY = 2;
        public static final int G2_CANTEEN_SCHEDULE = 5;
            public static final int OBJ_CANTEEN_SCHEDULE = 0;
        public static final int G2_CANTEEN_CAFE = 6;
            public static final int OBJ_CANTEEN_CAFE_CHECKLIST = 0;
            public static final int OBJ_CANTEEN_CAFE_EVALUATION = 1;
        public static final int G2_DATA_CANTEEN = 7;
            public static final int OBJ_MANUAL_VISITIATION = 0;
            public static final int OBJ_DATA_CANTEEN_SCHEDULE = 1;
        public static final int G2_CANTEEN = 8;
            public static final int OBJ_CANTEEN_CHECKLIST_GROUP = 0;
            public static final int OBJ_CANTEEN_CHECKLIST_ITEM = 1;
            public static final int OBJ_CANTEEN_CHECKLIST_MARK = 2;
            public static final int OBJ_CANTEEN_MENU_ITEM = 3;
            public static final int OBJ_CANTEEN_MEAL_TIME = 4;
            public static final int OBJ_CANTEEN_COMMENT_GROUP = 5;
            public static final int OBJ_CANTEEN_COMMENT_QUESTION = 6;
        public static final int G2_CANTEEN_REPORT_DETAIL = 9;
            public static final int OBJ_CANTEEN_DAILY_REPORT = 0;
            public static final int OBJ_CANTEEN_WEEKLY_REPORT = 1;
            public static final int OBJ_CANTEEN_MONTHLY_REPORT = 2;
        public static final int G2_CANTEEN_REPORT_SUMMARY = 10;
            public static final int OBJ_DAILY_MEAL_RECORD_REPORT = 0;
            public static final int OBJ_PERIODIC_MEAL_RECORD_REPORT = 1;
            public static final int OBJ_MEAL_REPORT = 2;
            public static final int OBJ_MEAL_REPORT_DEPARTEMENT = 3;
            public static final int OBJ_MONTHLY_CANTEEN_REPORT = 4;

    /**
     * ***** [4] MODULE CLINIC ******
     */
    public static final int G1_CLINIC = 4;
        public static final int G2_MEDICINE = 0;
            public static final int OBJ_LIST_OF_MEDICINE = 0;
            public static final int OBJ_MEDICINE_CONSUMPTION = 0;
        public static final int G2_EMPLOYEE_VISIT = 1;
            public static final int OBJ_EMPLOYEE_VISIT = 0;
        public static final int G2_GUEST_HANDLING = 2;
            public static final int OBJ_GUEST_HANDLING = 0;
        public static final int G2_DISEASE = 3;
            public static final int OBJ_DISEASE_TYPE = 0;
        public static final int G2_MEDICAL = 4;
            public static final int OBJ_MEDICAL_GROUP = 0;
            public static final int OBJ_MEDICAL_TYPE = 1;
            public static final int OBJ_MEDICAL_RECORD = 2;
            public static final int OBJ_MEDICAL_LEVEL = 3;
            public static final int OBJ_MEDICAL_CASE = 4;
            public static final int OBJ_MEDICAL_BUDGET = 5;
        public static final int G2_EMPLOYEE_AND_FAMILY = 0; // masi rancu
            public static final int OBJ_EMPLOYEE_AND_FAMILY = 0;
        public static final int G2_CLINIC_MEDICAL_RECORD = 1; // masi rancu
            public static final int OBJ_CLINIC_MEDICAL_RECORD = 0;    

    /**
     * ***** [5] MODULE REPORTS ******
     */
    public static final int G1_REPORTS = 5;
        public static final int G2_EMPLOYMENT_REPORT = 0;
            public static final int OBJ_STAFF_RECAPITULATION_REPORT = 0;
        public static final int G2_LEAVE_DP_RECORD_REPORT = 1;
            public static final int OBJ_LEAVE_GENERAL_REPORT = 0;
            public static final int OBJ_LEAVE_DETAIL_REPORT = 1;
        public static final int G2_STAFF_CONTROL_REPORT = 2;
            public static final int OBJ_ATTENDANCE_RECORD_REPORT = 0;
            public static final int OBJ_MANNING_SUMMARY_REPORT = 1;
        public static final int G2_LOCKERS_REPORT = 3;
            public static final int OBJ_LOCKERS_REPORT = 0;
        public static final int G2_CLINIC_REPORT = 4;
            public static final int OBJ_MEDICAL_EXPENSES_REPORT = 0;
            public static final int OBJ_SUMMARY_RECEIPTS_REPORT = 1;
            public static final int OBJ_EMPLOYEE_VISITS_REPORT = 2;
            public static final int OBJ_GUEST_HANDLING_REPORT = 3;
        public static final int G2_RECOGNITION_REPORT = 5;
            public static final int OBJ_RECOGNITION_MONTHLY_REPORT = 0;
            public static final int OBJ_RECOGNITION_QUARTERLY_REPORT = 1;
            public static final int OBJ_RECOGNITION_YEARLY_REPORT = 2;
        public static final int G2_PRESENCE_REPORT = 6;
            public static final int OBJ_PRESENCE_REPORT = 0;
            public static final int OBJ_PRESENCE_DAILY_REPORT = 1;
            public static final int OBJ_PRESENCE_WEEKLY_REPORT = 2;
            public static final int OBJ_PRESENCE_MONTHLY_REPORT = 3;
            public static final int OBJ_YEAR_REPORT = 4;
            public static final int OBJ_ATTENDANCE_SUMMARY = 5;
            public static final int OBJ_REKAPITULASI_ABSENSI = 6;
        public static final int G2_TRAINING_REPORT = 7;
            public static final int OBJ_TRAINING_MONTHLY_REPORT = 0;
            public static final int OBJ_TRAINING_PROFILES_REPORT = 1;
            public static final int OBJ_TRAINING_TARGET_REPORT = 2;
            public static final int OBJ_TRAINING_REPORT_BY_DEPARTEMENT = 3;
            public static final int OBJ_TRAINING_REPORT_BY_EMPLOYEE = 4;
            public static final int OBJ_TRAINING_REPORT_BY_TRAINNER = 5;
            public static final int OBJ_TRAINING_REPORT_BY_COURSE_DETAIL = 6;
            public static final int OBJ_TRAINING_REPORT_BY_COURSE_DATE = 7;
        public static final int G2_LATENESS_REPORT = 8;
            public static final int OBJ_LATENESS_DAILY_REPORT = 0;
            public static final int OBJ_LATENESS_WEEKLY_REPORT = 1;
            public static final int OBJ_LATENESS_MONTHLY_REPORT = 2;
        public static final int G2_ABSENTEEISM_REPORT = 9;
            public static final int OBJ_ABSENTEEISM_DAILY_REPORT = 0;
            public static final int OBJ_ABSENTEEISM_WEEKLY_REPORT = 1;
            public static final int OBJ_ABSENTEEISM_MONTHLY_REPORT = 2;
        public static final int G2_SPECIAL_DISCREPANCIES_REPORT = 10;
            public static final int OBJ_SPECIAL_DISCREPANCIES_DAILY_REPORT = 0;
            public static final int OBJ_SPECIAL_DISCREPANCIES_WEEKLY_REPORT = 1;
            public static final int OBJ_SPECIAL_DISCREPANCIES_MONTHLY_REPORT = 2;
        public static final int G2_SICK_DAYS_REPORT = 11;
            public static final int OBJ_SICK_DAYS_DAILY_REPORT = 0;
            public static final int OBJ_SICK_DAYS_WEEKLY_REPORT = 1;
            public static final int OBJ_SICK_DAYS_MONTHLY_REPORT = 2;
            public static final int OBJ_SICK_DAYS_ZERO_REPORT = 3;
        public static final int G2_SPLIT_SHIFT_REPORT = 12;
            public static final int OBJ_SPLIT_SHIFT_DAILY_REPORT = 0;
            public static final int OBJ_SPLIT_SHIFT_WEEKLY_REPORT = 1;
            public static final int OBJ_SPLIT_SHIFT_MONTHLY_REPORT = 2;
        public static final int G2_NIGHT_SHIFT_REPORT = 13;
            public static final int OBJ_NIGHT_SHIFT_DAILY_REPORT = 0;
            public static final int OBJ_NIGHT_SHIFT_WEEKLY_REPORT = 1;
            public static final int OBJ_NIGHT_SHIFT_MONTHLY_REPORT = 2;
        public static final int G2_LEAVE_REPORT = 14;
            public static final int OBJ_LEAVE_DP_SUMMARY = 0;
            public static final int OBJ_LEAVE_DP_DETAIL = 1;
            public static final int OBJ_SPECIAL_UNPAID_LEAVE = 2;
            public static final int OBJ_DP_EXPIRED = 3;
            public static final int OBJ_LEAVE_DP_SUMMARY_PERIOD = 4;
            public static final int OBJ_LEAVE_DP_DETAIL_PERIOD = 5;
        public static final int G2_SPECIAL_DISPENSATION_REPORT = 15;
            public static final int OBJ_SPECIAL_DISPENSATION_DAILY_REPORT = 0;
            public static final int OBJ_SPECIAL_DISPENSATION_WEEKLY_REPORT = 1;
            public static final int OBJ_SPECIAL_DISPENSATION_MONTHLY_REPORT = 2;
        public static final int G2_TRAINEE_REPORT = 16;
            public static final int OBJ_TRAINEE_MONTHLY_REPORT = 0;
            public static final int OBJ_TRAINEE_END_PERIOD = 1;
        public static final int G2_EMPLOYEE_REPORT = 17;
            public static final int OBJ_LIST_EMPLOYEE_CATEGORY_REPORT = 0;
            public static final int OBJ_LIST_EMPLOYEE_RESIGNATION_REPORT = 1;
            public static final int OBJ_LIST_EMPLOYEE_EDUCATION_REPORT = 2;
            public static final int OBJ_LIST_EMPLOYEE_CATEGORY_BY_NAME_REPORT = 3;
            public static final int OBJ_LIST_NUMBER_ABSENCES_REPORT = 4;
            public static final int OBJ_LIST_EMPLOYEE_RACE_REPORT = 5;
        public static final int G2_PAYROLL_REPORT = 18;
            public static final int OBJ_LIST_SALARY_SUMMARY_REPORT = 0;
            /* Update by Hendra Putu | 2016-08-06 */
            public static final int OBJ_JURNAL_REPORT = 1;
            public static final int OBJ_CUSTOM_RPT_MAIN = 2;
            public static final int OBJ_CUSTOM_RPT_GENERATE = 3;
            public static final int OBJ_ESPT_MONTH = 4;
            public static final int OBJ_ESPT_A1 = 5;
            /* Update by Hendra Putu | 2016-09-21 */
            public static final int OBJ_GAJI_TRANSFER = 6;
            public static final int OBJ_REKAP_GAJI = 7;
            public static final int OBJ_REKONSILIASI = 8;
            public static final int OBJ_LEAVE_ENTITLE = 9;
            public static final int OBJ_TAKEN_LEAVE = 10;
            
    /**
     * ***** [6] MODULE MASTERDATA ******
     */
    public static final int G1_MASTERDATA = 6;
        public static final int G2_MD_COMPANY = 0;
            public static final int OBJ_DEPARTMENT = 0;
            public static final int OBJ_POSITION = 1;
            public static final int OBJ_SECTION = 2;
            public static final int OBJ_LOCKER_LOCATION = 3;
            public static final int OBJ_LOCKER_CONDITION = 4;
            public static final int OBJ_LEAVE_PERIOD = 5;
            public static final int OBJ_DIVISION = 6;
            public static final int OBJ_PUBLIC_HOLIDAY = 7;
            public static final int OBJ_COMMPANY = 8;
            public static final int OBJ_LEAVE_TARGET = 9;
            public static final int OBJ_LEAVE_CONFIGURATION = 10;
        public static final int G2_MD_EMPLOYEE = 1;
            public static final int OBJ_EMPLOYEE_EDUCATION = 0;
            public static final int OBJ_EMPLOYEE_LEVEL = 1;
            public static final int OBJ_EMPLOYEE_CATEGORY = 2;
            public static final int OBJ_EMPLOYEE_RELIGION = 3;
            public static final int OBJ_EMPLOYEE_MARITAL = 4;
            public static final int OBJ_EMPLOYEE_LANGUAGE = 5;
            public static final int OBJ_EMPLOYEE_RESIGNED_REASON = 6;
            public static final int OBJ_EMPLOYEE_TRAINING = 7;
            public static final int OBJ_EMPLOYEE_FAMILY_RELATION = 8;
            public static final int OBJ_EMPLOYEE_WARNING = 9;
            public static final int OBJ_EMPLOYEE_REPRIMAND = 10;
            public static final int OBJ_EMPLOYEE_RACE = 11;
            public static final int OBJ_EMPLOYEE_IMAGE_ASSIGN = 12;
            public static final int OBJ_EMPLOYEE_AWARD_TYPE = 13;
            public static final int OBJ_EMPLOYEE_ABSENCE_REASON = 14;
            //update by satrya 2014-06-13
            public static final int OBJ_EMPLOYEE_GRADE_LEVEL = 15;
            public static final int OBJ_EMPLOYEE_INFORMATION_HRD = 16;
            public static final int OBJ_EMPLOYEE_REWARD_PUNISMENT = 17;
            public static final int OBJ_EMPLOYEE_KOEFISIEN_POSITION = 18;
            public static final int OBJ_EMPLOYEE_ENTRI_OPNAME_SALES = 19;
        public static final int G2_MD_SCHEDULE = 2;
            public static final int OBJ_SCHEDULE_PERIOD = 0;
            public static final int OBJ_SCHEDULE_CATEGORY = 1;
            public static final int OBJ_SCHEDULE_SYMBOL = 2;
        public static final int G2_MD_PERFORMANCE_APPRAISAL = 3;
            public static final int OBJ_GROUP_RANK = 0;
            public static final int OBJ_CATEGORY_APPRAISAL = 1;
            public static final int OBJ_EVALUATION_CRITERIA = 2;
            public static final int OBJ_FORM_CREATOR = 3;
        public static final int G2_MD_CANTEEN = 4;
            public static final int OBJ_CHECK_LIST_GROUP = 0;
            public static final int OBJ_CHECK_LIST_ITEM = 1;
            public static final int OBJ_CHECK_LIST_MARK = 2;
            public static final int OBJ_MENU_ITEM = 3;
            public static final int OBJ_MEAL_TIME = 4;
            public static final int OBJ_COMMENT_GROUP = 5;
            public static final int OBJ_COMMENT_QUESTION = 6;
        public static final int G2_MD_RECRUITMENT = 5;
            public static final int OBJ_GENERAL_QUESTION = 0;
            public static final int OBJ_ILLNESS_TYPE = 1;
            public static final int OBJ_INTERVIEW_POINTS = 2;
            public static final int OBJ_INTERVIEWER = 3;
            public static final int OBJ_INTERVIEW_FACTOR = 4;
            public static final int OBJ_ORIENTATION_GROUP = 5;
            public static final int OBJ_ORIENTATION_ACTIVITY = 6;
        public static final int G2_MD_EMP_UPLOAD_DATA = 6;
            public static final int OBJ_EMP_UPLOAD_DATA_TRAINEE = 0;
            public static final int OBJ_EMP_UPLOAD_DATA_DW = 1;
        public static final int G2_MD_EMP_LEAVE_OPNAME = 7;
            public static final int OBJ_EMP_LEAVE_DP_OPNAME = 0;
            public static final int OBJ_EMP_LEAVE_AL_OPNAME = 1;
            public static final int OBJ_EMP_LEAVE_LL_OPNAME = 2;
        public static final int G2_MD_LOCKER_DATA = 8;
            public static final int OBJ_LOCKER_DATA_LOCATION = 0;
            public static final int OBJ_LOCKER_DATA_CONDITION = 1;
        public static final int G2_MD_ASSESSMENT = 9;
            public static final int OBJ_MD_ASSESSMENT_GROUP_RANK = 0;
            public static final int OBJ_MD_ASSESSMENT_EVALUATION_CRITERIA = 1;
            public static final int OBJ_MD_ASSESSMENT_FORM_CREATOR = 2;
        public static final int G2_MD_GEO_AREA = 10;
            public static final int OBJ_EMP_COUNTRY = 0;
            public static final int OBJ_EMP_PROVINCE = 1;
            public static final int OBJ_EMP_REGENCY = 2;
            public static final int OBJ_EMP_SUBREGENCY = 3;
        //update by satrya 2014-02-25
        public static final int G2_MD_LOCATION_OUTLET = 11;
            public static final int OBJ_LOCATION_OUTLET = 0;
            //update by satrya 2014-06-15
            public static final int OBJ_JENIS_SO = 1;
            public static final int OBJ_PERIODE_SO = 2;
  
            
    /**
     * ***** [7] MODULE SYSTEM ******
     */
    public static final int G1_SYSTEM = 7;
        public static final int G2_USER_MANAGEMENT = 0;
            public static final int OBJ_USER_LIST = 0;
            public static final int OBJ_USER_GROUP = 1;
            public static final int OBJ_USER_PRIVILEGE = 2;
            public static final int OBJ_USER_UPDATE_PASSWORD = 3;
            public static final int OBJ_USER_COMPARE = 4;
        public static final int G2_SYSTEM_MANAGEMENT = 1;
            public static final int OBJ_BACKUP_SERVICE = 0;
            public static final int OBJ_SYSTEM_PROPERTIES = 1;
            public static final int OBJ_LOGIN_HISTORY = 2;
            public static final int OBJ_SYSTEM_LOG = 3;
        public static final int G2_BARCODE = 2;
            public static final int OBJ_INSERT_TO_DB = 0;
            public static final int OBJ_INSERT_TO_MACHINE = 1;
            public static final int OBJ_INSERT_AND_UPLOAD_BARCODE = 1;
            public static final int OBJ_UPLOAD_PER_EMPLOYEE = 1;
        public static final int G2_TIMEKEEPING = 3;
            public static final int OBJ_SERVICE_MANAGER = 0;
            public static final int OBJ_DOWNLOAD_DATA = 1;
            public static final int OBJ_TIMEKEEPING_CHECK_MACHINE = 2;
            public static final int OBJ_TIMEKEEPING_RESET_MACHINE = 3;
        public static final int G2_WORKING_SCHEDULE = 4;
            public static final int OBJ_UPLOAD_INSERT_SCHEDULE = 0;
        public static final int G2_IMPORT_PRESENCE = 5;
            public static final int OBJ_IMPORT_PRESENCE = 0;
        public static final int G2_SERVICE_CENTER = 6;
            public static final int OBJ_SERVICE_CENTER = 0;
            public static final int OBJ_SERVICE_EMAIL = 1;
        public static final int G2_MANUAL_CHECKING = 7;
            public static final int OBJ_MANUAL_CHECKING_ALL = 0;
            public static final int OBJ_MANUAL_CHECKING_PRESENCE = 1;
            public static final int OBJ_MANUAL_CHECKING_ABSENTEEISM = 2;
            public static final int OBJ_MANUAL_CHECKING_LATENESS = 3;
            public static final int OBJ_MANUAL_CHECKING_DP = 4;
            public static final int OBJ_MANUAL_CHECKING_AL = 5;
            public static final int OBJ_MANUAL_CHECKING_LL = 6;
            public static final int OBJ_MANUAL_CHECKING_BACKUP_DB = 7;
        public static final int G2_MANUAL_PROCESS = 8;
            public static final int OBJ_MANUAL_PROCESS = 0;
        public static final int G2_ADMIN_TEST_MESIN = 9;
            public static final int OBJ_ADMIN_TEST_MESIN = 0;
        public static final int G2_ADMIN_QUERY_SETUP = 10;
            public static final int OBJ_ADMIN_QUERY_SETUP = 0;
            
    /**
     * ***** [8] MODULE SODEVI ******
     */
    public static final int G1_SODEVI = 8;
        public static final int G2_SODEVI_MASTER = 0;
            public static final int OBJ_SODEVI_MASTER_OPERATING_SYSTEM = 0;
        public static final int G2_SODEVI_TEST = 1;
            public static final int OBJ_SODEVI_TEST_SCRIPT = 0;
            public static final int OBJ_SODEVI_TEST_VIEW_RESULT = 1;          
   
    /**
     * ***** [9] MODULE PAYROLL ******
     */
    public static final int G1_PAYROLL = 9;
        public static final int G2_PAYROLL_SETUP = 0;
            public static final int OBJ_PAYROLL_SETUP_GENERAL = 0;
            public static final int OBJ_PAYROLL_SETUP_PERIOD = 1;
            public static final int OBJ_PAYROLL_SETUP_BANK = 2;
            public static final int OBJ_PAYROLL_SETUP_COMPONENT = 3;
            public static final int OBJ_PAYROLL_SETUP_LEVEL = 4;
            public static final int OBJ_PAYROLL_SETUP_CURRENCY = 5;
            public static final int OBJ_PAYROLL_SETUP_RATE = 6;
            public static final int OBJ_PAYROLL_PAY_SLIP_GROUP = 7;
            public static final int OBJ_PAYROLL_EMPLOYEE_SETUP = 8;
            /*public static final int OBJ_PAYROLL_SERVICE_CONFIG = 9;*/
            public static final int OBJ_PAYROLL_BENEFIT_CONFIG = 9;
            public static final int OBJ_PAYROLL_BENEFIT_INPUT = 10;
            public static final int OBJ_PAYROLL_BENEFIT_EMPLOYEE = 11;
            public static final int OBJ_PAYROLL_BENEFIT_HISTORY = 12;
        public static final int G2_PAYROLL_OVERTIME = 1;
            public static final int OBJ_PAYROLL_OVERTIME_INDEX = 0;
            public static final int OBJ_PAYROLL_OVERTIME_FORM = 1;
            public static final int OBJ_PAYROLL_OVERTIME_IMPORT = 2;
            public static final int OBJ_PAYROLL_OVERTIME_INPUT = 3;
            public static final int OBJ_PAYROLL_OVERTIME_POSTING = 4;
            public static final int OBJ_PAYROLL_OVERTIME_REPORT = 5;
            public static final int OBJ_PAYROLL_OVERTIME_SUMMARY = 6;
        public static final int G2_PAYROLL_PROCESS = 2;
            public static final int OBJ_PAYROLL_PROCESS_PREPARE = 0;
            public static final int OBJ_PAYROLL_PROCESS_INPUT = 1;
            public static final int OBJ_PAYROLL_PROCESS_PROCESS = 2;
            public static final int OBJ_PAYROLL_PROCESS_PRINT = 3;
            public static final int OBJ_PAYROLL_SIMULATION = 4;
            public static final int OBJ_PAYROLL_PAYSLIP_GROUP = 5;
        public static final int G2_PAYROLL_TAX = 3;
            public static final int OBJ_PAYROLL_TAX_SETUP = 0;
            public static final int OBJ_PAYROLL_TAX_CALCULATE = 1;
            public static final int OBJ_PAYROLL_TAX_REPORT = 2;
    //Update By Agus 12-02-2012        
    /**
     * ***** [10] MODULE TRAINING ******
     */
    public static final int G1_TRAINING = 10;
        public static final int G2_TRAINING_TYPE = 0;
            public static final int OBJ_MENU_TRAINING_TYPE = 0;
        public static final int G2_TRAINING_VANUE = 1;
            public static final int OBJ_MENU_TRAINING_VANUE = 0;
        public static final int G2_TRAINING_PROGRAM = 2;
            public static final int OBJ_MENU_TRAINING_PROGRAM = 0;
        public static final int G2_TRAINING_PLAN = 3;
            public static final int OBJ_MENU_TRAINING_PLAN = 0;
        public static final int G2_TRAINING_ACTUAL = 4;
            public static final int OBJ_MENU_TRAINING_ACTUAL = 0;
        public static final int G2_TRAINING_TRAINING_SEARCH = 5;
            public static final int OBJ_MENU_TRAINING_SEARCH = 0;
        public static final int G2_TRAINING_TRAINING_HISTORY = 6;
            public static final int OBJ_MENU_TRAINING_HISTORY = 0;
        public static final int G2_TRAINING_MENU = 7;
            public static final int OBJ_TRAINING_MENU = 0;
        public static final int G2_TRAINING_ORGANIZER = 8;
            public static final int OBJ_TRAINING_ORGANIZER_CONTACT = 0;   
      
      
    /*
     * =========================================================================
     * -------------------------------------------------------------------------
     * MODULE MENU | Akan ditampilkan di header
     * -------------------------------------------------------------------------
     * =========================================================================
     */ 
    /*===== [11] MENU EMPLOYEE =====*/        
    public static final int G1_MENU_EMPLOYEE = 11;
        public static final int G2_MENU_DATABANK = 0;
            public static final int OBJ_MENU_DATABANK = 0;
        public static final int G2_MENU_COMPANY_STRUCTURE = 1;
            public static final int OBJ_MENU_COMPANY_STRUCTURE = 0;
        public static final int G2_MENU_NEW_EMPLOYEE = 2;
            public static final int OBJ_MENU_NEW_EMPLOYEE = 0;
        public static final int G2_MENU_ABSENCE_MANAGEMENT = 3;
            public static final int OBJ_MENU_ABSENCE_MANAGEMENT = 0;
        public static final int G2_MENU_RECOGNITION = 4;
            public static final int OBJ_MENU_ENTRY_PER_DEPT = 0;
            public static final int OBJ_MENU_UPDATE_PER_EMPLOYEE = 1;
        public static final int G2_MENU_RECRUITMENT = 5;
            public static final int OBJ_MENU_STAFF_REQUISITION = 0;
            public static final int OBJ_MENU_EMPLOYEE_APPLICATION = 1;
            public static final int OBJ_MENU_ORIENTATION_CHECK_LIST = 2;
            public static final int OBJ_MENU_REMINDER = 3;
        public static final int G2_MENU_WARNING_AND_REPRIMAND = 6;
            public static final int OBJ_MENU_WARNING = 0;
            public static final int OBJ_MENU_REPRIMAND = 1;
        public static final int G2_MENU_EXCUSE_APPLICATION = 7;
            public static final int OBJ_MENU_EXCUSE_FORM = 0;
        public static final int G2_MENU_ATTENDANCE = 8;
            public static final int OBJ_MENU_WORKING_SCHEDULE = 0;
            public static final int OBJ_MENU_MANUAL_REGISTRATION = 1;
            public static final int OBJ_MENU_REGENERATE_SCHEDULE_HOLIDAYS = 2;
        public static final int G2_MENU_LEAVE_APPLICATION = 9;
            public static final int OBJ_MENU_LEAVE_FORM = 0;
            public static final int OBJ_MENU_LEAVE_AL_CLOSING = 1;
            public static final int OBJ_MENU_LEAVE_LI_CLOSING = 2;
            public static final int OBJ_MENU_DP_MANAGEMENT = 3;
            public static final int OBJ_MENU_DP_RECALCULATE = 4;
        public static final int G2_MENU_LEAVE_BALANCING = 10;
            public static final int OBJ_MENU_ANNUAL_LEAVE = 0;
            public static final int OBJ_MENU_LONG_LEAVE = 1;
            public static final int OBJ_MENU_DAY_OFF_PAYMENT = 2;
        public static final int G2_MENU_ASSESSMENT = 11;
            public static final int OBJ_MENU_EXPLANATION_COVERAGE = 0;
            public static final int OBJ_MENU_PERFORMANCE_ASSESSMENT = 1;
        public static final int G2_EMPLOYEE_MENU = 12;
            public static final int OBJ_EMPLOYEE_MENU = 0;
        //update by satrya 2014-03-22
        public static final int G2_MENU_EMPLOYEE_OUTLET = 13;
            public static final int OBJ_MENU_EMPLOYEE_OUTLET = 0;
        public static final int G2_MENU_FORM_SCHEDULE_CHANGE = 14;
            public static final int OBJ_MENU_FRM_SCHEDULE_CHANGE = 0;
            public static final int OBJ_MENU_FRM_SCHEDULE_EO = 1;
            public static final int OBJ_MENU_FRM_SCHEDULE_EH = 2;
            
    /*===== [12] MENU REPORT =====*/
    public static final int G1_MENU_REPORTS = 12;
        public static final int G2_MENU_STAFF_CONTROL_REPORT = 0;
            public static final int OBJ_MENU_ATTENDANCE_RECORD_REPORT = 0;
            public static final int OBJ_MENU_MANNING_SUMMARY_REPORT = 1;
        public static final int G2_MENU_PRESENCE_REPORT = 1;
            public static final int OBJ_MENU_PRESENCE_DAILY_REPORT = 0;
            public static final int OBJ_MENU_PRESENCE_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_PRESENCE_MONTHLY_REPORT = 2;
            public static final int OBJ_MENU_YEAR_REPORT = 3;
            public static final int OBJ_MENU_ATTENDANCE_SUMMARY = 4;
        public static final int G2_MENU_LATENESS_REPORT = 2;
            public static final int OBJ_MENU_LATENESS_DAILY_REPORT = 0;
            public static final int OBJ_MENU_LATENESS_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_LATENESS_MONTHLY_REPORT = 2;
        public static final int G2_MENU_SPLIT_SHIFT_REPORT = 3;
            public static final int OBJ_MENU_SPLIT_SHIFT_DAILY_REPORT = 0;
            public static final int OBJ_MENU_SPLIT_SHIFT_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_SPLIT_SHIFT_MONTHLY_REPORT = 2;
        public static final int G2_MENU_NIGHT_SHIFT_REPORT = 4;
            public static final int OBJ_MENU_NIGHT_SHIFT_DAILY_REPORT = 0;
            public static final int OBJ_MENU_NIGHT_SHIFT_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_NIGHT_SHIFT_MONTHLY_REPORT = 2;
        public static final int G2_MENU_ABSENTEEISM_REPORT = 5;
            public static final int OBJ_MENU_ABSENTEEISM_DAILY_REPORT = 0;
            public static final int OBJ_MENU_ABSENTEEISM_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_ABSENTEEISM_MONTHLY_REPORT = 2;
        public static final int G2_MENU_SICKNESS_REPORT = 6;
            public static final int OBJ_MENU_SICKNESS_DAILY_REPORT = 0;
            public static final int OBJ_MENU_SICKNESS_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_SICKNESS_MONTHLY_REPORT = 2;
            public static final int OBJ_MENU_SICKNESS_ZERO_REPORT = 3;
        public static final int G2_MENU_SPECIAL_DISPENSATION_REPORT = 7;
            public static final int OBJ_MENU_SPECIAL_DISPENSATION_DAILY_REPORT = 0;
            public static final int OBJ_MENU_SPECIAL_DISPENSATION_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_SPECIAL_DISPENSATION_MONTHLY_REPORT = 2;
        public static final int G2_MENU_LEAVE_REPORT = 8;
            public static final int OBJ_MENU_LEAVE_DP_SUMMARY = 0;
            public static final int OBJ_MENU_LEAVE_DP_DETAIL = 1;
            public static final int OBJ_MENU_LEAVE_DP_SUMMARY_PERIOD = 2;
            public static final int OBJ_MENU_LEAVE_DP_DETAIL_PERIOD = 3;
            public static final int OBJ_MENU_SPECIAL_UNPAID_PERIOD = 4;
            public static final int OBJ_MENU_DP_EXPIRED = 5;
        public static final int G2_MENU_TRAINEE_REPORT = 9;
            public static final int OBJ_MENU_TRAINEE_MONTHLY_REPORT = 0;
            public static final int OBJ_MENU_TRAINEE_END_PERIOD = 1;
        public static final int G2_MENU_EMPLOYEE_REPORT = 10;
            public static final int OBJ_MENU_LIST_EMPLOYEE_CATEGORY_REPORT = 0;
            public static final int OBJ_MENU_LIST_EMPLOYEE_RESIGNATION_REPORT = 1;
            public static final int OBJ_MENU_LIST_EMPLOYEE_EDUCATION_REPORT = 2;
            public static final int OBJ_MENU_LIST_EMPLOYEE_CATEGORY_BY_NAME_REPORT = 3;
            public static final int OBJ_MENU_LIST_NUMBER_ABSENCES_REPORT = 4;
            public static final int OBJ_MENU_LIST_EMPLOYEE_RACE_REPORT = 5;
        public static final int G2_MENU_TRAINING_REPORT = 11;
            public static final int OBJ_MENU_MONTHLY_REPORT = 0;
            public static final int OBJ_MENU_PROFILES_REPORT = 1;
            public static final int OBJ_MENU_TARGET_REPORT = 2;
            public static final int OBJ_MENU_REPORT_BY_DEPARTEMENT = 3;
            public static final int OBJ_MENU_REPORT_BY_EMPLOYEE = 4;
            public static final int OBJ_MENU_REPORT_BY_TRAINNER = 5;
            public static final int OBJ_MENU_REPORT_BY_COURSE_DETAIL = 6;
            public static final int OBJ_MENU_REPORT_BY_COURSE_DATE = 7;
        public static final int G2_MENU_PAYROLL_REPORT = 12;
            public static final int OBJ_MENU_LIST_SALARY_SUMMARY_REPORT = 0;
        public static final int G2_REPORT_MENU = 13;
            public static final int OBJ_REPORT_MENU = 0;
            
    /*===== [13] MENU CANTEEN =====*/
    public static final int G1_MENU_CANTEEN = 13;
        public static final int G2_MENU_DATA_CANTEEN = 0;
            public static final int OBJ_MENU_MANUAL_VISITIATION = 0;
            public static final int OBJ_MENU_DATA_CANTEEN_SCHEDULE = 1;
        public static final int G2_MENU_CANTEEN = 1;
            public static final int OBJ_MENU_CANTEEN_CHECKLIST_GROUP = 0;
            public static final int OBJ_MENU_CANTEEN_CHECKLIST_ITEM = 1;
            public static final int OBJ_MENU_CANTEEN_CHECKLIST_MARK = 2;
            public static final int OBJ_MENU_CANTEEN_MENU_ITEM = 3;
            public static final int OBJ_MENU_CANTEEN_MEAL_TIME = 4;
            public static final int OBJ_MENU_CANTEEN_COMMENT_GROUP = 5;
            public static final int OBJ_MENU_CANTEEN_COMMENT_QUESTION = 6;
        public static final int G2_MENU_CANTEEN_REPORT_DETAIL = 2;
            public static final int OBJ_MENU_CANTEEN_DAILY_REPORT = 0;
            public static final int OBJ_MENU_CANTEEN_WEEKLY_REPORT = 1;
            public static final int OBJ_MENU_CANTEEN_MONTHLY_REPORT = 2;
        public static final int G2_MENU_CANTEEN_REPORT_SUMMARY = 3;
            public static final int OBJ_MENU_DAILY_MEAL_RECORD_REPORT = 0;
            public static final int OBJ_MENU_PERIODIC_MEAL_RECORD_REPORT = 1;
            public static final int OBJ_MENU_MEAL_REPORT = 2;
            public static final int OBJ_MENU_MEAL_REPORT_DEPARTEMENT = 3;
            public static final int OBJ_MENU_MONTHLY_CANTEEN_REPORT = 4;
        public static final int G2_CANTEEN_MENU = 4;
            public static final int OBJ_CANTEEN_MENU = 0;
            
    /*===== [14] MENU CLINIC =====*/
    public static final int G1_MENU_CLINIC = 14;
        public static final int G2_MENU_EMPLOYEE_AND_FAMILY = 0;
            public static final int OBJ_MENU_EMPLOYEE_AND_FAMILY = 0;
        public static final int G2_MENU_CLINIC_MEDICAL_RECORD = 1;
            public static final int OBJ_MENU_CLINIC_MEDICAL_RECORD = 0;
        public static final int G2_MENU_EMPLOYEE_VISIT = 2;
            public static final int OBJ_MENU_EMPLOYEE_VISIT = 0;
        public static final int G2_MENU_GUEST_HANDLING = 3;
            public static final int OBJ_MENU_GUEST_HANDLING = 0;
        public static final int G2_MENU_MEDICINE = 4;
            public static final int OBJ_MENU_LIST_OF_MEDICINE = 0;
            public static final int OBJ_MENU_MEDICINE_CONSUMPTION = 1;
        public static final int G2_MENU_DISEASE = 5;
            public static final int OBJ_MENU_DISEASE_TYPE = 0;
        public static final int G2_MENU_MEDICAL = 6;
            public static final int OBJ_MENU_MEDICAL_GROUP = 0;
            public static final int OBJ_MENU_MEDICAL_TYPE = 1;
            public static final int OBJ_MENU_MEDICAL_RECORD = 2;
            public static final int OBJ_MENU_MEDICAL_LEVEL = 3;
            public static final int OBJ_MENU_MEDICAL_CASE = 4;
            public static final int OBJ_MENU_MEDICAL_BUDGET = 5;
        public static final int G2_CLINIC_MENU = 7;
            public static final int OBJ_CLINIC_MENU = 0;
         
    /*===== [15] MENU LOCKER =====*/
    public static final int G1_MENU_LOCKER = 15;
        public static final int G2_MENU_LOCKER = 0;
            public static final int OBJ_MENU_LOCKER = 0;
        public static final int G2_MENU_LOCKER_TREATMENT = 1;
            public static final int OBJ_MENU_LOCKER_TREATMENT = 0;
        public static final int G2_LOCKER_MENU = 2;
            public static final int OBJ_LOCKER_MENU = 0;  
            
    /*===== [16] MENU MASTERDATA =====*/
    public static final int G1_MENU_MASTERDATA = 16;
        public static final int G2_MENU_MD_COMPANY = 0;
            public static final int OBJ_MENU_DEPARTMENT = 0;
            public static final int OBJ_MENU_POSITION = 1;
            public static final int OBJ_MENU_SECTION = 2;
            public static final int OBJ_MENU_LOCKER_LOCATION = 3;
            public static final int OBJ_MENU_LOCKER_CONDITION = 4;
            public static final int OBJ_MENU_LEAVE_PERIOD = 5;
            public static final int OBJ_MENU_DIVISION = 6;
            public static final int OBJ_MENU_PUBLIC_HOLIDAY = 7;
            public static final int OBJ_MENU_COMMPANY = 8;
            public static final int OBJ_MENU_LEAVE_TARGET = 9;
            public static final int OBJ_MENU_LEAVE_SETTING = 10;
        public static final int G2_MENU_MD_EMPLOYEE = 1;
            public static final int OBJ_MENU_EMPLOYEE_EDUCATION = 0;
            public static final int OBJ_MENU_EMPLOYEE_LEVEL = 1;
            public static final int OBJ_MENU_EMPLOYEE_CATEGORY = 2;
            public static final int OBJ_MENU_EMPLOYEE_RELIGION = 3;
            public static final int OBJ_MENU_EMPLOYEE_MARITAL = 4;
            public static final int OBJ_MENU_EMPLOYEE_LANGUAGE = 5;
            public static final int OBJ_MENU_EMPLOYEE_RESIGNED_REASON = 6;
            public static final int OBJ_MENU_EMPLOYEE_TRAINING = 7;
            public static final int OBJ_MENU_EMPLOYEE_FAMILY_RELATION = 8;
            public static final int OBJ_MENU_EMPLOYEE_WARNING = 9;
            public static final int OBJ_MENU_EMPLOYEE_REPRIMAND = 10;
            public static final int OBJ_MENU_EMPLOYEE_RACE = 11;
            public static final int OBJ_MENU_EMPLOYEE_IMAGE_ASSIGN = 12;
            public static final int OBJ_MENU_EMPLOYEE_AWARD_TYPE = 13;
            public static final int OBJ_MENU_EMPLOYEE_ABSENCE_REASON = 14;
        public static final int G2_MENU_MD_SCHEDULE = 2;
            public static final int OBJ_MENU_SCHEDULE_PERIOD = 0;
            public static final int OBJ_MENU_SCHEDULE_CATEGORY = 1;
            public static final int OBJ_MENU_SCHEDULE_SYMBOL = 2;
        public static final int G2_MENU_MD_LOCKER_DATA = 3;
            public static final int OBJ_MENU_LOCKER_DATA_LOCATION = 0;
            public static final int OBJ_MENU_LOCKER_DATA_CONDITION = 1;
        public static final int G2_MENU_MD_ASSESSMENT = 4;
            public static final int OBJ_MENU_MD_ASSESSMENT_GROUP_RANK = 0;
            public static final int OBJ_MENU_MD_ASSESSMENT_EVALUATION_CRITERIA = 1;
            public static final int OBJ_MENU_MD_ASSESSMENT_FORM_CREATOR = 2;
        public static final int G2_MENU_MD_RECRUITMENT = 5;
            public static final int OBJ_MENU_GENERAL_QUESTION = 0;
            public static final int OBJ_MENU_ILLNESS_TYPE = 1;
            public static final int OBJ_MENU_INTERVIEW_POINTS = 2;
            public static final int OBJ_MENU_INTERVIEWER = 3;
            public static final int OBJ_MENU_INTERVIEW_FACTOR = 4;
            public static final int OBJ_MENU_ORIENTATION_GROUP = 5;
            public static final int OBJ_MENU_ORIENTATION_ACTIVITY = 6;
        public static final int G2_MENU_MD_GEO_AREA = 6;
            public static final int OBJ_MENU_EMP_COUNTRY = 0;
            public static final int OBJ_MENU_EMP_PROVINCE = 1;
            public static final int OBJ_MENU_EMP_REGENCY = 2;
            public static final int OBJ_MENU_EMP_SUBREGENCY = 3;
            public static final int OBJ_MENU_EMP_SUBDISTRICT = 4;   
        public static final int G2_MASTERDATA_MENU = 7;
            public static final int OBJ_MASTERDATA_MENU = 0;
        public static final int G2_LOCATION_OUTLET_MENU = 8;
            public static final int OBJ_MENU_LOCATION_OUTLET = 0;
            
    /*===== [17] MENU SYSTEM =====*/
    public static final int G1_MENU_SYSTEM = 17;
        public static final int G2_MENU_SERVICE_CENTER = 0;
            public static final int OBJ_MENU_SERVICE_CENTER = 0;
        public static final int G2_MENU_MANUAL_PROCESS = 1;
            public static final int OBJ_MENU_MANUAL_PROCESS = 0;
        public static final int G2_MENU_ADMIN_TEST_MESIN = 2;
            public static final int OBJ_MENU_ADMIN_TEST_MESIN = 0;
        public static final int G2_MENU_ADMIN_QUERY_SETUP = 3;
            public static final int OBJ_MENU_ADMIN_QUERY_SETUP = 0;
        public static final int G2_MENU_USER_MANAGEMENT = 4;
            public static final int OBJ_MENU_USER_LIST = 0;
            public static final int OBJ_MENU_USER_GROUP = 1;
            public static final int OBJ_MENU_USER_PRIVILEGE = 2;
            public static final int OBJ_MENU_USER_UPDATE_PASSWORD = 3;
            public static final int OBJ_MENU_USER_COMPARE = 4;
        public static final int G2_MENU_SYSTEM_MANAGEMENT = 5;
            public static final int OBJ_MENU_SYSTEM_PROPERTIES = 0;
            public static final int OBJ_MENU_LOGIN_HISTORY = 1;
            public static final int OBJ_MENU_SYSTEM_LOG = 2;
        public static final int G2_MENU_TIMEKEEPING = 6;
            public static final int OBJ_MENU_SERVICE_MANAGER = 0;
        public static final int G2_SYSTEM_MENU = 7;
            public static final int OBJ_SYSTEM_MENU = 0;
        
     /*===== [18] MENU PAYROLL =====*/
    public static final int G1_MENU_PAYROLL = 18;
        public static final int G2_MENU_PAYROLL_SETUP = 0;
            public static final int OBJ_MENU_GENERAL = 0;
            public static final int OBJ_MENU_PAYROLL_PERIOD = 1;
            public static final int OBJ_MENU_BANK_LIST = 2;
            public static final int OBJ_MENU_SALARY_COMPONENT = 3;
            public static final int OBJ_MENU_SALARY_LEVEL = 4;
            public static final int OBJ_MENU_CURRENCY = 5;
            public static final int OBJ_MENU_CURRENCY_RATE = 6;
            public static final int OBJ_MENU_PAY_SLIP_GROUP = 7;
            public static final int OBJ_MENU_EMPLOYEE_SETUP = 8;
            public static final int OBJ_MENU_SERVICE_CONFIG = 9; // update by Hendra McHen 2015-01-29
        public static final int G2_MENU_PAYROLL_OVERTIME = 1;
            public static final int OBJ_MENU_PAYROLL_OVERTIME_INDEX = 0;
            public static final int OBJ_MENU_PAYROLL_OVERTIME_FORM = 1;
            public static final int OBJ_MENU_PAYROLL_OVERTIME_REPORT_PROCESS = 2;
            public static final int OBJ_MENU_PAYROLL_OVERTIME_SUMMARY = 3;
        public static final int G2_MENU_PAYROLL_PROCESS = 2;
            public static final int OBJ_MENU_PAYROLL_PROCESS_PREPARE_DATA = 0;
            public static final int OBJ_MENU_PAYROLL_PROCESS_INPUT = 1;
            public static final int OBJ_MENU_PAYROLL_PROCESS_PROCESS = 2;
            public static final int OBJ_MENU_PAYROLL_PROCESS_PRINTING = 3;
        public static final int G2_PAYROLL_SETUP_MENU = 3;
            public static final int OBJ_PAYROLL_SETUP_MENU = 0;
        public static final int G2_PAYROLL_OVERTIME_MENU = 4;
            public static final int OBJ_PAYROLL_OVERTIME_MENU = 0;
        public static final int G2_PAYROLL_PROCESS_MENU = 5;
            public static final int OBJ_PAYROLL_PROCESS_MENU = 0;
        
    /*===== [19] MENU HEADER MODIF =====*/ //update by satrya 2014-03-05
    public static final int G1_MENU_HEADER_MODIF = 19;
        public static final int G2_MENU_HEADER_MODIF_TEMPLATE = 0;
            public static final int OBJ_MENU_CHANGE_MENU = 0;
            public static final int OBJ_MENU_CHANGE_IMAGE_COMPANY = 1;
            
    // [20] add by Kartika 2015-09-14      
    public static final int G1_OUTSOURCE = 20;
        public static final int G2_OUTSOURCE_DATA_ENTRY = 0;
            public static final int OBJ_OUTSOURCE_MASTER_PLAN = 0;
            public static final int OBJ_OUTSOURCE_FORM_EVALUASI = 1;
            public static final int OBJ_OUTSOURCE_COST_MASTER = 2;
            public static final int OBJ_OUTSOURCE_COST_INPUT = 3;
        public static final int G2_OUTSOURCE_REPORT = 1;
            public static final int OBJ_OUTSOURCE_RPT_APPROVAL = 0;
            public static final int OBJ_OUTSOURCE_RPT_EVALUATION = 1;
            public static final int OBJ_OUTSOURCE_RPT_COST_PLAN = 2;
            public static final int OBJ_OUTSOURCE_RPT_SUMMARY = 3;
            public static final int OBJ_OUTSOURCE_RPT_IN_OUT = 4;
            public static final int OBJ_OUTSOURCE_RPT_COST_REAL = 5;  
            
    /* [21] Add by Hendra Putu | 2015-12-10 | lokasi ngoding @McD */
    public static final int G1_PERINGATAN_DAN_TEGURAN = 21;
        public static final int G2_PERINGATAN = 0;
            public static final int OBJ_PERINGATAN_RINGAN = 0;
            public static final int OBJ_PERINGATAN_BERAT = 1;
        public static final int G2_TEGURAN = 1;
            public static final int OBJ_TEGURAN_BAB = 0;
            public static final int OBJ_TEGURAN_PASAL = 1;
            public static final int OBJ_TEGURAN_AYAT = 2;
            
    /* [22] MENU PERINGATAN DAN TEGURAN */      
    public static final int G1_MENU_PERINGATAN_DAN_TEGURAN = 22;
        public static final int G2_MENU_PERINGATAN = 0;
            public static final int OBJ_MENU_PERINGATAN_RINGAN = 0;
            public static final int OBJ_MENU_PERINGATAN_BERAT = 1;
        public static final int G2_MENU_TEGURAN = 1;
            public static final int OBJ_MENU_TEGURAN_BAB = 0;
            public static final int OBJ_MENU_TEGURAN_PASAL = 1;
            public static final int OBJ_MENU_TEGURAN_AYAT = 2;
            
    /* [23] MENU PELATIHAN*/
    public static final int G1_MENU_PELATIHAN = 23;
        public static final int G2_MENU_TIPE_PELATIHAN = 0;
            public static final int OBJ_MENU_TIPE_PELATIHAN = 0;
        public static final int G2_MENU_LOKASI_PELATIHAN = 1;
            public static final int OBJ_MENU_LOKASI_PELATIHAN = 0;
        public static final int G2_MENU_PROGRAM_PELATIHAN = 2;
            public static final int OBJ_MENU_PROGRAM_PELATIHAN = 0;
        public static final int G2_MENU_RENCANA_PELATIHAN = 3;
            public static final int OBJ_MENU_RENCANA_PELATIHAN = 0;
        public static final int G2_MENU_PELATIHAN_REAL = 4;
            public static final int OBJ_MENU_PELATIHAN_REAL = 0;
        public static final int G2_MENU_PENCARIAN_PELATIHAN = 5;
            public static final int OBJ_MENU_PENCARIAN_PELATIHAN = 0;
        public static final int G2_MENU_SEJARAH_PELATIHAN = 6;
            public static final int OBJ_MENU_SEJARAH_PELATIHAN = 0;
        public static final int G2_MENU_GAP_TRAINING = 7;
            public static final int OBJ_MENU_GAP_TRAINING = 0;
        public static final int G2_MENU_TRAINING_ORGANIZER = 8;
            public static final int OBJ_MENU_TRAINING_ORGANIZER = 0;
        public static final int G2_MENU_ORGANIZER_TYPE = 9;
            public static final int OBJ_MENU_ORGANIZER_TYPE = 0;
        public static final int G2_MENU_TRAINING_SCORE = 10;
            public static final int OBJ_MENU_TRAINING_SCORE = 0;
            
    /* [24] MENU ORGANISASI */     
    public static final int G1_MENU_ORGANISASI = 24;
        public static final int G2_MENU_STRUKTUR_ORGANISASI = 0;
            public static final int OBJ_MENU_STRUKTUR_ORGANISASI = 0;
        public static final int G2_MENU_CETAK_UNIT_KERJA = 1;
            public static final int OBJ_MENU_CETAK_UNIT_KERJA = 0;
        public static final int G2_MENU_SEJARAH_JABATAN = 2;
            public static final int OBJ_MENU_SEJARAH_JABATAN = 0;
        public static final int G2_MENU_POSISI_DAN_JOB_DESC = 3;
            public static final int OBJ_MENU_POSISI_DAN_JOB_DESC = 0;
        public static final int G2_MENU_INFORMASI_PEJABAT = 4;
            public static final int OBJ_MENU_INFORMASI_PEJABAT = 0;
        public static final int G2_MENU_STRUKTUR_PERUSAHAAN = 5;
            public static final int OBJ_MENU_PERUSAHAAN = 0;
            public static final int OBJ_MENU_SATUAN_KERJA = 1;
            public static final int OBJ_MENU_UNIT = 2;
            public static final int OBJ_MENU_SUB_UNIT = 3;
            public static final int OBJ_MENU_JABATAN = 4;
            public static final int OBJ_MENU_DIVISION_TYPE = 5;
            public static final int OBJ_MENU_DEPARTMENT_TYPE = 6;
            public static final int OBJ_MENU_PUBLIC_LIBURAN = 7;
        public static final int G2_MENU_STRUKTUR_TEMPLATE = 6;
            public static final int OBJ_MENU_STRUKTUR_TEMPLATE = 0;

    /* [25] MENU SURAT */
    public static final int G1_DOKUMEN_SURAT = 25;
        public static final int G2_MASTER_DOCUMENT = 0;
            public static final int OBJ_DOCUMENT_TYPE = 0;
            public static final int OBJ_DOCUMENT_EXPENSE = 1;
            public static final int OBJ_DOCUMENT_MASTER = 2;
        public static final int G2_EMP_DOCUMENT = 1;
            public static final int OBJ_EMP_DOCUMENT = 0;

    
            
    public static final String[] titleG1 = {
        /* [0] */ "LOGIN ACCESS",
        /* [1] */ "EMPLOYEE",
        /* [2] */ "LOCKER",
        /* [3] */ "CANTEEN",
        /* [4] */ "CLINIC",
        /* [5] */ "REPORT",
        /* [6] */ "MASTERDATA",
        /* [7] */ "SYSTEM",
        /* [8] */ "SODEVI",
        /* [9] */ "PAYROLL",
        /* [10] */ "TRAINING",
        /* [11] */ "MENU EMPLOYEE",
        /* [12] */ "MENU REPORTS",
        /* [13] */ "MENU CANTEEN",
        /* [14] */ "MENU CLINIC",
        /* [15] */ "MENU LOCKER",
        /* [16] */ "MENU MASTERDATA",
        /* [17] */ "MENU SYSTEM",
        /* [18] */ "MENU PAYROLL",
        /* [19] */ "MENU CHANGE TEMPLATE",
        /* [20] */ "OUTSOURCE",
        /* [21] */ "PERINGATAN DAN TEGURAN", /* Add by Hendra Putu | 2015-12-10 */
        /* [22] */ "MENU PERINGATAN DAN TEGURAN",
        /* [23] */ "MENU PELATIHAN",
        /* [24] */ "MENU ORGANISASI",
        /* [25] */ "DOKUMEN SURAT"
    };
    
    public static final String[][] titleG2 = {
        /* [0] LOGIN ACCESS */
        {
            /*[0]*/ "LOGIN ACCESS",
            /*[1]*/ "MENU ACCESS"
        },
        /* [1] EMPLOYEE */
        {
            /*[0]*/ "EMP DATABANK",
            /*[1]*/ "EMP ATTENDANCE",
            /*[2]*/ "EMP SALARY",
            /*[3]*/ "EMP APPRAISAL",
            /*[4]*/ "EMP RECOGNITION",
            /*[5]*/ "EMP TRAINING",
            /*[6]*/ "EMP RECRUITMENT", 
            /*[7]*/ "EMP LEAVE MANAGEMENT", 
            /*[8]*/ "EMP LEAVE APPLICATION", 
            /*[9]*/ "EMP ABSENCE MANAGEMENT", 
            /*[10]*/ "EMP COMPANY STRUCTURE", 
            /*[11]*/ "EMP NEW EMPLOYEE", 
            /*[12]*/ "EMP WARNING & REPRIMAND", 
            /*[13]*/ "EMP EXCUSE APPLICATION", 
            /*[14]*/ "EMP LEAVE BALANCING", 
            /*[15]*/ "EMP ASSESSMENT",
            /*[16]*/ "EMPLOYEE OUTLET", 
            /*[17]*/ "EMP EXTRA SCHEDULE", /* sebelumnya FORM SCHEDULE */
            /*[18]*/ "EMP CANDIDATE", /* Add By Hendra Putu | Desember 22, 2015 */
            /*[19]*/ "EMP COMPETENCE", /* Add By Hendra Putu | Desember 22, 2015 */
            /*[20]*/ "EMP PERFORMANCE" /* Add By Hendra Putu | Desember 22, 2015 */
        },
        /* [2] LOCKER */
        {
            /*[0]*/ "LOC LOCKER",
            /*[1]*/ "LOC LOCKER TREATMENT"
        },
        /* [3] CANTEEN */
        {
            /*[0]*/ "CNT MENU LIST MONTHLY",
            /*[1]*/ "CNT DAILY EVALUATION",
            /*[2]*/ "CNT COMMENT_CARD",
            /*[3]*/ "CNT SUMMARY REPORT",
            /*[4]*/ "CNT DETAIL REPORT",
            /*[5]*/ "CNT SCHEDULE",
            /*[6]*/ "CNT CAFE", 
            /*[7]*/ "CNT DATA CANTEEN", 
            /*[8]*/ "CNT CANTEEN", 
            /*[9]*/ "CNT DETAIL CANTEEN REPORT", 
            /*[10]*/ "CNT SUMMARY CANTEEN REPORT"
        },
        /* [4] CLINIC */
        {
            /*[0]*/ "CLI MEDICINE",
            /*[1]*/ "CLI EMPLOYEE VISIT",
            /*[2]*/ "CLI GUEST HANDLING",
            /*[3]*/ "CLI DISEASE",
            /*[4]*/ "CLI MEDICAL", 
            /*[5]*/ "CLI EMPLOYEE & FAMILY", 
            /*[6]*/ "CLINIC MEDICAL RECORD"
        },
        /* [5] REPORT */
        {
            /*[0]*/ "RPT EMPLOYMENT", 
            /*[1]*/ "RPT LEAVE & DP RECORD", 
            /*[2]*/ "RPT STAFF CONTROL", 
            /*[3]*/ "RPT LOCKERS", 
            /*[4]*/"RPT CLINIC", 
            /*[5]*/ "RPT RECOGNITION", 
            /*[6]*/ "RPT PRESENCE", 
            /*[7]*/ "RPT TRAINING", 
            /*[8]*/ "RPT LATENESS", 
            /*[9]*/ "RPT ABSENTEEISM", 
            /*[10]*/ "RPT SPECIAL DISCREPANCY", 
            /*[11]*/ "RPT SICK DAYS", 
            /*[12]*/ "RPT SPLIT SHIFT", 
            /*[13]*/ "RPT NIGHT SHIFT", 
            /*[14]*/ "RPT LEAVE REPORT", 
            /*[15]*/ "RPT SPECIAL DISPENSATION", 
            /*[16]*/ "RPT TRAINEE", 
            /*[17]*/ "RPT EMPLOYEE", 
            /*[18]*/ "RPT PAYROLL"
        },
        /* [6] MASTERDATA */
        {
            /*[0]*/ "MD COMPANY",
            /*[1]*/ "MD EMPLOYEE",
            /*[2]*/ "MD SCHEDULE",
            /*[3]*/ "MD PERFORMANCE APPRAISAL",
            /*[4]*/ "MD CANTEEN",
            /*[5]*/ "MD RECRUITMENT", 
            /*[6]*/ "MD UPLOAD DATA", 
            /*[7]*/ "MD LEAVE OPNAME", 
            /*[8]*/ "MD LOCKER DATA", 
            /*[9]*/ "MD ASSESSMENT", 
            /*[10]*/ "MD GEO AREA",
            /*[11]*/ "MD LOCATION"
        },
        /* [7] SYSTEM */
        {
            /*[0]*/ "SYS USER MANAGEMENT",
            /*[1]*/ "SYS SYSTEM MANAGEMENT",
            /*[2]*/ "SYS BARCODE",
            /*[3]*/ "SYS TIMEKEEPING",
            /*[4]*/ "SYS WORKING SCHEDULE", 
            /*[5]*/ "SYS IMPORT PRESENCE", 
            /*[6]*/ "SYS SERVICE CENTER", 
            /*[7]*/ "SYS MANUAL CHECKING", 
            /*[8]*/ "SYS MANUAL PROCESS", 
            /*[9]*/ "SYS ADMIN TEST MESIN", 
            /*[10]*/ "SYS ADMIN QUERY SETUP"
        },
        /* [8] SOFT MASTER */
        {
            /*[0]*/ "SOFT MASTER",
            /*[1]*/ "SOFT TEST"
        },
        /* [9] PAYROLL */
        {
            /*[0]*/ "PAYROLL SETUP", 
            /*[1]*/ "PAYROLL OVER TIME", 
            /*[2]*/ "PAYROLL PROCESS", 
            /*[3]*/ "PAYROLL TAX"
        },   
        /* [10] TRAINING */
        {
            /*[0]*/ "TRA TRAINING TYPE", 
            /*[1]*/ "TRA TRAINING VANUE", 
            /*[2]*/ "TRA TRAINING PROGRAM", 
            /*[3]*/ "TRA TRAINING PLAN", 
            /*[4]*/ "TRA TRAINING ACTUAL", 
            /*[5]*/ "TRA TRAINING SEARCH", 
            /*[6]*/ "TRA TRAINING HISTORY", 
            /*[7]*/ "TRAINING MENU",
            /*[8]*/ "TRAINING ORGANIZER"
        },
        /* [11] MENU EMPLOYEE */
        {
            /*[0]*/ "MENU DATABANK", 
            /*[1]*/ "MENU COMPANY STRUCTURE", 
            /*[2]*/ "MENU NEW EMPLOYEE", 
            /*[3]*/ "MENU ABSENCE MANAGEMENT", 
            /*[4]*/ "MENU RECOGNITION", 
            /*[5]*/ "MENU RECRUITMENT", 
            /*[6]*/ "MENU WARNING & REPRIMAND", 
            /*[7]*/ "MENU EXCUSE APPLICATION", 
            /*[8]*/ "MENU ATTENDANCE", 
            /*[9]*/ "MENU LEAVE APPLICATION", 
            /*[10]*/ "MENU LEAVE BALANCING", 
            /*[11]*/ "MENU ASSESSMENT", 
            /*[12]*/ "EMPLOYEE MENU",
            /*[13]*/ "MENU EMPLOYEE OUTLET",
            /*[14]*/ "MENU FORM SCHEDULE CHANGE"
        },
        /* [12] MENU REPORT */
        {
            /*[0]*/ "MENU RPT STAFF CONTROL", 
            /*[1]*/ "MENU RPT PRESENCE", 
            /*[2]*/ "MENU RPT LATENESS", 
            /*[3]*/ "MENU RPT SPLIT SHIFT", 
            /*[4]*/ "MENU RPT NIGHT SHIFT", 
            /*[5]*/ "MENU RPT ABSENTEEISM",
            /*[6]*/ "MENU RPT SICKNESS",
            /*[7]*/ "MENU RPT SPECIAL DISPENSATION", 
            /*[8]*/ "MENU RPT LEAVE REPORT", 
            /*[9]*/ "MENU RPT TRAINEE", 
            /*[10]*/ "MENU RPT EMPLOYEE", 
            /*[11]*/ "MENU RPT TRAINING", 
            /*[12]*/ "MENU RPT PAYROLL", 
            /*[13]*/ "REPORT MENU"
        },
        /* [13] MENU CANTEEN */
        {
            /*[0]*/ "MENU DATA CANTEEN", 
            /*[1]*/ "MENU CANTEEN",
            /*[2]*/ "MENU CANTEEN REPORT DETAIL", 
            /*[3]*/ "CANTEEN REPORT SUMMARY", 
            /*[4]*/ "CANTEEN MENU" 
        },
        /* [14] MENU CLINIC */
        {
            /*[0]*/ "MENU EMPLOYEE & FAMILY", 
            /*[1]*/ "MENU CLINIC MEDICAL RECORD", 
            /*[2]*/ "MENU EMPLOYEE VISIT", 
            /*[3]*/ "MENU GUEST HANDLING", 
            /*[4]*/ "MENU MEDICINE",
            /*[5]*/ "MENU DISEASE",
            /*[6]*/ "MENU MEDICAL", 
            /*[7]*/ "CLINIC MENU"  
        },
        /* [15] MENU LOCKER */
        {
            /*[0]*/ "MENU LOCKER",
            /*[1]*/ "MENU LOCKER TREATMENT", 
            /*[2]*/ "LOCKER MENU"
        },
        /* [16] MENU MASTERDATA */
        {
            /*[0]*/ "MENU MD COMPANY",
            /*[1]*/ "MENU MD EMPLOYEE",
            /*[2]*/ "MENU MD SCHEDULE", 
            /*[3]*/ "MENU MD LOCKER DATA", 
            /*[4]*/ "MENU MD ASSESSMENT", 
            /*[5]*/ "MENU MD RECRUITMENT",
            /*[6]*/ "MENU MD GEO AREA", 
            /*[7]*/ "MASTERDATA MENU",
            /*[8]*/ "LOCATION OUTLET MENU"
        },
        /* [17] MENU SYSTEM */
        {
            /*[0]*/ "MENU SYS SERVICE CENTER", 
            /*[1]*/ "MENU SYS MANUAL PROCESS", 
            /*[2]*/ "MENU SYS ADMIN TEST MESIN", 
            /*[3]*/ "MENU SYS ADMIN QUERY SETUP", 
            /*[4]*/ "MENU SYS USER MANAGEMENT",
            /*[5]*/ "MENU SYS SYSTEM MANAGEMENT",
            /*[6]*/ "MENU SYS TIMEKEEPING", 
            /*[7]*/ "SYSTEM MENU"
        },
        /* [18] MENU PAYROLL */
        {
            /*[0]*/ "MENU PAYROLL SETUP", 
            /*[1]*/ "MENU PAYROLL OVER TIME", 
            /*[2]*/ "MENU PAYROLL PROCESS", 
            /*[3]*/ "PAYROLL SETUP MENU", 
            /*[4]*/ "PAYROLL OVERTIME MENU", 
            /*[5]*/ "PAYROLL PROCESS MENU"
        },   
        /* [19] Menu header template */
        {
            /*[0]*/ "MENU HEADER TEMPLATE"
        },
        /* [20] Menu Outsource */
        {
            /*[0]*/ "OUTSOURCE DATA ENTRY", 
            /*[1]*/ "OUTSOURCE REPORT"
        },
        /* [21] Peringatan dan Teguran */
        {
            /*[0]*/ "PERINGATAN", 
            /*[1]*/ "TEGURAN"
        },
        /* [22] Menu Peringatan dan Teguran */
        {
            /* [0] */ "MENU PERINGATAN",
            /* [1] */ "MENU TEGURAN"
        },
        /* [23] MENU PELATIHAN */
        {
            /* [0] */ "MENU TIPE PELATIHAN",
            /* [1] */ "MENU LOKASI PELATIHAN",
            /* [2] */ "MENU PROGRAM PELATIHAN",
            /* [3] */ "MENU RENCANA PELATIHAN",
            /* [4] */ "MENU PELATIHAN REAL",
            /* [5] */ "MENU PENCARIAN PELATIHAN",
            /* [6] */ "MENU SEJARAH PELATIHAN",
            /* [7] */ "MENU GAP TRAINING",
            /* [8] */ "MENU TRAINING ORGANIZER",
            /* [9] */ "MENU ORGANIZER TYPE",
            /* [10] */ "MENU TRAINING SCORE"
        },
        /* [24] MENU ORGANISASI */
        {
            "MENU STRUKTUR ORGANISASI",
            "MENU CETAK UNIT KERJA",
            "MENU SEJARAH JABATAN",
            "MENU POSISI DAN JOB DESC",
            "MENU INFORMASI PEJABAT",
            "MENU STRUKTUR PERUSAHAAN",
            "MENU STRUKTUR TEMPLATE"
        },
        /* [25] DOKUMEN SURAT */
        {
            "MASTER DOCUMENT",
            "EMP DOCUMENT"
        }

    };
    
    
    public static final String[][][] objectTitles = {
        /* [0] LOGIN */
        { 
            { /*[0] "LOGIN ACCESS" */
                "LOGIN PAGE", 
                "LOGOUT PAGE"
            },
            { /*[1] "MENU ACCESS" */
                "MENU PAGE", 
                "MENU EMPLOYEE PAGE"
            }
        },
        /* [1] EMPLOYEE */
        { 
            { /*[0] "EMP DATABANK" */
                "EMP DATABANK",
                /* Add object by Hendra McHen | 2016-03-23 */
                /* Description ::
                 * personal data, family member, competence, education, experience, 
                 * career path, training, warning, reprimand, award, picture, relevant document
                 */
                "DATABANK PERSONAL DATA",
                "DATABANK FAMILY MEMBER",
                "DATABANK LANGUANGE AND COMPETENCE",
                "DATABANK EDUCATION",
                "DATABANK EXPERIENCE",
                "DATABANK CAREERPATH",
                "DATABANK TRAINING",
                "DATABANK WARNING",
                "DATABANK REPRIMAND",
                "DATABANK AWARD",
                "DATABANK PICTURE",
                "DATABANK RELEVANT DOC"
            },
            
            { /*[1] "EMP ATTENDANCE" */
                "WORKING SCHEDULE", 
                "LEAVE OPNAME", 
                "LEAVE MANAGEMENT", 
                "DAY OFF PAYMENT", 
                "PRESENCE", 
                "MANUAL PRESENCE", 
                "MANUAL REGISTRASION", 
                "RE-GENERATE SCHEDULE HOLYDAYS"
            },
            { /*[2] "EMP SALARY" */
                "SALARY"
            },
            { /*[3] "EMP APPRAISAL" */
                "EXPLANATIONS AND COVERAGE", 
                "PERFORMANCE APPRAISAL"
            },
            { /*[4] "EMP RECOGNITION" */
                "ENTRY PER DEPARTMENT", 
                "UPDATE PER EMPLOYEE"
            },
            { /*[5] "EMP TRAINING" */
                "TRAINING HISTORY", 
                "SPECIAL ARCHIEVEMENT", 
                "TRAINING ACTIVITIES", 
                "TRAINING SEARCH", 
                "TRAINING ACHIEVE EDIT", 
                "TRAINING ACHIEVE LIST", 
                "GENERAL TRAINING", 
                "DEPARTEMENTAL TRAINING"
            },
            { /*[6] "EMP RECRUITMENT" */
                "STAFF REQUISITION", 
                "EMPLOYEE APPLICATION", 
                "ORIENTATION CHECK LIST", 
                "REMINDER", 
                "EMPLOYEE RECRUITMENT", 
                "EMPLOYEE RECRUITMENT EDUCATION", 
                "EMPLOYEE RECRUITMENT HISTORY", 
                "EMPLOYEE RECRUITMENT LANGGUAGE", 
                "EMPLOYEE RECRUITMENT REFERENCES", 
                "EMPLOYEE RECRUITMENT GENERAL", 
                "EMPLOYEE RECRUITMENT INTERVIEW"
            },
            { /*[7] "EMP LEAVE MANAGEMENT" */
                "DP MANAGEMENT", 
                "AL MANAGEMENT", 
                "LL MANAGEMENT", 
                "OPNAME AL", 
                "OPNAME LL", 
                "OPNAME DP", 
                "AL CLOSING", 
                "LL CLOSING", 
                "LEAVE SRC"
            },
            { /*[8] "EMP LEAVE APPLICATION" */
                "DP APPLICATION", 
                "LEAVE APPLICATION", 
                "LEAVE FORM", 
                "LEAVE APPLICATION AL CLOSING", 
                "LEAVE LI CLOSING", 
                "DP MANAGEMENT", 
                "DP RE-CALCULATE"
            },
            { /*[9] "EMP ABSENCE MANAGEMENT" */
                "ABSENCE MANAGEMENT"
            },
            { /*[10] "EMP COMPANY STRUCTURE" */
                "COMPANY STRUCTURE"
            },
            { /*[11] "EMP NEW EMPLOYEE" */
                "NEW EMPLOYEE"
            },
            { /*[12] "EMP WARNING & REPRIMAND" */
                "WARNING", 
                "REPRIMAND"
            },
            { /*[13] "EMP EXCUSE APPLICATION" */
                "EXCUSE APPLICATION"
            },
            { /*[14] "EMP LEAVE BALANCING" */
                "ANNUAL LEAVE", 
                "LONG LEAVE", 
                "DAY OFF PAYMENT"
            },
            { /*[15] "EMP ASSESSMENT" */
                "EXPLANATION COVERAGE", 
                "PERFORMANCE ASSESSMENT"
            },
            { /*[16] "EMPLOYEE OUTLET" */
                "EMPLOYEE OUTLET", 
                "MAPPING KADIV"
            },
            { /*[17] "EMP EXTRA SCHEDULE" */
                "EXTRA SCHEDULE FORM",
                "EXTRA SCHEDULE LIST"
            },
            { /* [18] EMP CANDIDATE */
                "CANDIDATE SEARCH",
                "CANDIDATE ROTATION",
                "CANDIDATE ANALYSIS",
                "CANDIDATE LIST",
                "CANDIDATE POSITION_REQ"
            },
            { /* [19] COMPETENCE */
                "COMPETENCE",
                "COMPETENCE TYPE",
                "COMPETENCE GROUP",
                "COMPETENCE LEVEL",
                "COMPETENCE DETAIL"
            },
            { /* [20] PERFORMANCE */
                "KPI COMPANY TARGET",
                "KPI TYPE",
                "KPI GROUP",
                "KPI LIST",
                "KPI ACHIEV SCORE",
                "COMPETITOR"
            }
        },
        /* [2] LOCKER */
        { // LOC LOCKER
            {
                "LOC LOCKER"
            },
            // LOC LOCKER TREATMENT
            {
                "LOC LOCKER TREATMENT"
            }
        },
        /* [3] CANTEEN */
        { // CNT MENU LIST MONTHLY
            {
                "CNT MENU LIST MONTHLY"
            },
            // CNT DAILY EVALUATION
            {
                "CNT DAILY EVALUATION"
            },
            // CNT COMMENT_CARD
            {
                "CNT COMMENT CARD"
            },
            // CNT REPORT SUMMARY
            {
                "CNT REPORT SUMMARY DAILY", 
                "CNT REPORT SUMMARY WEEKLY", 
                "CNT REPORT SUMMARY MONTHLY"
            },
            // CNT REPORT DETAIL
            {
                "CNT REPORT DETAIL DAILY", 
                "CNT REPORT DETAIL WEEKLY", 
                "CNT REPORT DETAIL MONTHLY"
            },
            // CNT SCHEDULE
            {
                "CNT SCHEDULE"
            },
            // CNT CAFE CHECK LIST, CNT CAFE EVALUATION
            {
                "CNT CAFE CHEAKLIST", 
                "CNT CAFE EVALUATION"
            },
            // CNT DATA CANTEEN
            {
                "MANUAL VISITIATION", 
                "DATA CANTEEN SCHEDULE"
            },
            // CNT CANTEEN
            {
                "CANTEEN CHECKLIST GROUP", 
                "CANTEEN CHECKLIST ITEM", 
                "CANTEEN CHECKLIST MARK", 
                "CANTEEN MENU ITEM", 
                "CANTEEN MEAL TIME", 
                "COMMENT GROUP", 
                "COMMENT QUESTION"
            },
            // CANTEEN REPORT DETAIL
            {
                "CANTEEN DAILY REPORT", 
                "CANTEEN WEEKLY REPORT", 
                "CANTEEN MONTHLY REPORT"
            },
            // CANTEEM REPORT SUMMARY
            {
                "DAILY MEAL RECORD REPORT", 
                "PERIODIC MEAL RECORD REPORT", 
                "MEAL REPORT", 
                "MEAL REPORT DEPARTEMENT", 
                "MONTHLY CANTEEN REPORT"
            }
        },
        /* [4] CLINIC */
        { // CLI MEDICINE
            {
                "LIST OF MEDICINE", 
                "MEDICINE CONSUMPTION"
            },
            // CLI EMPLOYEE VISIT
            {
                "EMPLOYEE VISIT"
            },
            // CLI GUEST HANDLING
            {
                "GUEST HANDLING"
            },
            // CLI DISEASE
            {
                "DISEASE TYPE"
            },
            // CLI MEDICAL
            {
                "MEDICAL GROUP", 
                "TYPE", 
                "MEDICAL RECORD", 
                "MEDICAL LEVEL", 
                "MEDICAL CASE", 
                "MEDICAL BUDGET"
            },
            // CLI EMPLOYEE & FAMILY
            {
                "EMPLOYEE & FAMILY"
            },
            // CLI CLINIC MEDICAL RECORD 
            {
                "CLINIC MEDICAL RECORD"
            }
        },
        /* [5] REPORT */
        { // RPT EMPLOYMENT
            {
                "STAFF RECAPITULATION"
            },
            // RPT LEAVE & DP RECORD
            {
                "GENERAL", "DETAIL"
            },
            // RPT STAFF CONTROL
            {
                "ATTENDANCE RECORD", 
                "MANNING SUMMARY"
            },
            // RPT LOCKERS
            {
                "LOCKERS"
            },
            // RPT CLINIC
            {
                "MEDICAL EXPENSES", 
                "SUMMARY RECEIPTS", 
                "EMPLOYEE VISITS", 
                "GUEST HANDLING"
            },
            // RPT RECOGNITION
            {
                "MONTHLY REPORT", 
                "QUARTERLY REPORT", 
                "YEARLY REPORT"
            },
            // RPT PRESENCE
            {
                "PRESENCE REPORT", 
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT", 
                "YEAR REPORT", 
                "ATTENDANCE SUMMARY",
                "REKAPITULASI ABSENSI"
            },
            // RPT TRAINING
            {
                "MONTHLY REPORT", 
                "PROFILES REPORT", 
                "TARGET REPORT", 
                "REPORT BY DEPARTEMENT", 
                "REPORT BY EMPLOYEE", 
                "REPORT BY TRAINER", 
                "REPORT BY COURCE DETAIL", 
                "REPORT BY COURCE DATE"
            },
            // RPT LATENESS
            {
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT"
            },
            // RPT ABSENTEEISM
            {
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT"
            },
            // RPT SPECIAL DISCREPANCY
            {
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT"
            },
            // RPT SICK DAYS
            {
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT", 
                "ZERO REPORT"
            },
            // RPT SPLIT SHIFT
            {
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT"
            },
            // RPT NIGHT SHIFT
            {
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT"
            },
            // RPT LEAVE REPORT
            {
                "LEAVE DP SUMMARY", 
                "LEAVE DP DETAIL", 
                "SPECIAL UNPAID PERIOD", 
                "DP EXPIRED", 
                "LEAVE DP SUMMARY PERIOD", 
                "LEAVE DP DETAIL PERIOD"
            },
            // RPT SPECIAL DISPENSATION REPORT
            {
                "DAILY REPORT", 
                "WEEKLY REPORT", 
                "MONTHLY REPORT"
            },
            // RPT TRAINEE REPORT
            {
                "MONTHLY REPORT", 
                "END PERIOD"
            },
            // RPT EMPLOYEE REPORT
            {
                "LIST EMPLOYEE CATEGORY REPORT", 
                "RESIGNATION REPORT", 
                "EDUCATION REPORT", 
                "CATEGORY BY NAME ERPORT", 
                "ABSENCES REPORT", 
                "RACE REPORT"
            },
            // RPT PAYROLL REPORT 
            {
                "LIST SALARY SUMMARY REPORT",
                "JURNAL REPORT",
                "CUSTOM REPORT MAIN",
                "CUSTOM REPORT GENERATE",
                "ESPT MONTH",
                "ESPT A1",
                /* Update by Hendra Putu | 2016-09-21 */
                "GAJI TRANSFER",
                "REKAP GAJI",
                "REKONSILIASI",
                "LEAVE ENTITLE",
                "TAKEN LEAVE"
            }
        },
        /* [6] MASTERDATA */
        { // MD COMPANY
            {
                "DEPARTMENT", 
                "POSITION", 
                "SECTION", 
                "LOCKER LOCATION", 
                "LOCKER CONDITION", 
                "LEAVE PERIOD", 
                "DIVISION", 
                "PUBLIC HOLIDAY", 
                "COMPANY", 
                "LEAVE TARGET", 
                "LEAVE KONFIGURATION"
            },
            // MD EMPLOYEE
            {
                "EDUCATION", 
                "LEVEL", 
                "CATEGORY", 
                "RELIGION", 
                "MARITAL", 
                "LANGUAGE", 
                "RESIGNED REASON", 
                "TRAINING", 
                "FAMILY RELATION", 
                "WARNING", 
                "REPRIMAND", 
                "RACE", 
                "IMAGE ASSIGN", 
                "AWARD TYPE", 
                "ABSENCE REASON", 
                "GRADE LEVEL", 
                "INFORMATION HRD", 
                "REWARD n PUSNIMENT", 
                "KOEFISIEN POSITION", 
                "ENTRI OPNAME SALES"
            },
            // MD SCHEDULE
            {
                "PERIOD", 
                "CATEGORY", 
                "SYMBOL"
            },
            // MD PERFORMANCE APPRAISAL
            {
                "GROUP RANK", 
                "CATEGORY APPRAISAL", 
                "EVALUATION CRITERIA", 
                "FORM CREATOR"
            },
            // MD CANTEEN
            {
                "CHECK LIST GROUP", 
                "CHECK LIST ITEM", 
                "CHECK LIST MARK", 
                "MENU ITEM", 
                "MEAL TIME", 
                "COMMENT GROUP", 
                "COMMENT QUESTION"
            },
            // MD RECRUITMENT
            {
                "GENERAL QUESTION", 
                "ILLNESS TYPE", 
                "INTERVIEW POINTS", 
                "INTERVIEWER", 
                "INTERVIEW FACTOR", 
                "ORIENTATION GROUP", 
                "ORIENTATION ACTIVITY"
            },
            // MD EMP UPLOAD DATA
            {
                "TRAINEE", 
                "DAILY WORKER"
            },
            // MD LEAVE OPNAME
            {
                "DP OPNAME", 
                "AL OPNAME", 
                "LL OPNAME"
            },
            // MD LOCKER DATA
            {
                "LOCKER DATA LOCATION", 
                "LOCKER DATA CONDITION"
            },
            // MD ASSESSMENT
            {
                "GROUP RANK", 
                "EVALUATION CRITERIA", 
                "FORM CREATOR"
            },
            // MD GEO AREA
            {
                "COUNTRY", 
                "PROVINCE", 
                "REGENCY", 
                "SUBREGENCY"
            },
            // MD Location
            //update by satrya 2014-02-25

            {
                "OUTLET LOCATION", 
                "JENIS SO", 
                "PERIODE SO"
            }
        },
        /* [7] SYSTEM */
        { // SYS USER MANAGEMENT
            {
                "USER LIST", 
                "GROUP PRIVILEGE", 
                "PRIVILEGE", 
                "UPDATE PASSWORD", 
                "USER COMPARE"
            },
            // SYS SYSTEM MANAGEMENT
            {
                "BACK UP SERVICE", 
                "SYSTEM PROPERTIES", 
                "LOGIN HISTORY", 
                "SYSTEM LOG"
            },
            // SYS BARCODE
            {
                "INSERT BARCODE TO DATABASE", 
                "UPLOAD BARCODE TO MACHINE", 
                "INSERT AND UPLOAD BARCODE", 
                "UPLOAD PER EMPLOYEE"
            },
            // SYS TIMEKEEPING
            {
                "SERVICE MANAGER", 
                "DOWNLOAD DATA", 
                "CHECK MACHINE", 
                "RESET MACHINE"
            },
            // SYS WORKING SCHEDULE
            {
                "UPLOAD & INSERT SCHEDULE"
            },
            // SYS IMPORT PRESENCE
            {
                "IMPORT PRESENCE"
            },
            // SYS SERVICE CENTER
            {
                "SERVICE CENTER", 
                "EMAIL SYSTEM"
            },
            // SYS MANUAL CHECKING
            {
                "ALL", "PRESENCE", 
                "ABSENTEEISM", 
                "LATENESS", 
                "DP", 
                "AL", 
                "LL", 
                "BACKUP DB"
            },
            // MANUAL PROCESS
            {
                "MANUAL PROCESS"
            },
            // ADMIN TEST MESIN
            {
                "ADMIN TEST MESIN"
            },
            // ADMIN QUERY SETUP 
            {
                "ADMIN QUERY SETUP"
            }
        },
        /* [8] SODEVI */
        { // MASTER
            {"OPERATING SYSTEM"},
            // TEST
            {"TEST SCRIPT", "VIEW RESULT"}
        },
        /* [9] PAYROLL */
        { //PAYROLL SETUP
            {
                "PAYROLL SETUP GENERAL",
                "PAYROLL SETUP PERIOD",
                "PAYROLL SETUP BANK",
                "PAYROLL SETUP COMPONENT",
                "PAYROLL SETUP LEVEL",
                "PAYROLL SETUP CURRENCY",
                "PAYROLL SETUP RATE",
                "PAYROLL SETUP PAY SLIP GROUP",
                "PAYROLL SETUP EMPLOYEE SETUP",
                "PAYROLL BENEFIT CONFIG",
                "PAYROLL BENEFIT INPUT",
                "PAYROLL BENEFIT EMPLOYEE",
                "PAYROLL BENEFIT HISTORY"
            },
            //PAYROLL OVERTIME
            {
                "PAYROLL OVERTIME INDEX",
                "PAYROLL OVERTIME FORM",
                "PAYROLL OVERTIME IMPORT",
                "PAYROLL OVERTIME INPUT",
                "PAYROLL OVERTIME POSTING",
                "PAYROLL OVERTIME REPORT",
                "PAYROLL OVERTIME SUMMARY"
            },
            //PAYROLL PROCESS
            {
                "PAYROLL PROCESS PREPARE",
                "PAYROLL PROCESS INPUT",
                "PAYROLL PROCESS PROCESS",
                "PAYROLL PROCESS PRINT",
                "PAYROLL PROCESS SIMULATION",
                "PAYROLL PAYSLIP GROUP"
            },
            //PAYROLL TAX
            {
                "PAYROLL TAX SETUP",
                "PAYROLL TAX CALCULATE",
                "PAYROLL TAX REPORT"
            }
        },
        /* [10] TRAINING */ 
        /* 
         * Prefix pada kata depan training sebenarnya tidak ditambah kata "MENU"
         * karena index dari array TRAINING sebelumnya tidak memakai kata MENU. 
         * Why? You can ask to previous Developer 2013
         */
        { //0 TRAINING TYPE
            {"MENU TRAINING TYPE"},
            //1 TRAINING VANUE
            {"MENU TRAINING VANUE"},
            //2 TRAINING PROGRAM
            {"MENU TRAINING PROGRAM"},
            //3 TRAINING PLAN
            {"MENU TRAINING PLAN"},
            //4 TRAINING ACTUAL
            {"MENU TRAINING ACTUAL"},
            //5 TRAINING SEARCH 
            {"MENU TRAINING SEARCH"},
            //6 TRAINING HISTORY
            {"MENU TRAINING HISTORY"},
            // 7 TRAINING MENU
            {"TRAINING MENU"},
            //8  TRAINING ORGANIZER
            {"TRAINING ORGANIZER"}
        },
        /* [11] MENU EMPLOYEE */
        { // MENU DATABANK
            {"MENU DATABANK"},
            // MENU COMPANY STRUCTURE
            {"MENU COMPANY STRUCTURE"},
            // MENU NEW EMPLOYEE
            {"MENU NEW EMPLOYEE"},
            // MENU ABSENCE MANAGEMENT
            {"MENU ABSENCE MANAGEMENT"},
            // MENU RECOGNITION
            {"MENU ENTRY PER DEPARTMENT", "MENU UPDATE PER EMPLOYEE"},
            // MENU RECRUITMENT
            {"MENU STAFF REQUISITION", "MENU EMPLOYEE APPLICATION", "MENU ORIENTATION CHECK LIST", "MENU REMINDER"},
            // MENU WARNING & REPRIMAND
            {"MENU WARNING", "MENU REPRIMAND"},
            // EMP EXCUSE APPLICATION
            {"EXCUSE APPLICATION"},
            // MENU ATTENDANCE
            {"MENU WORKING SCHEDULE", "MENU MANUAL REGISTRASION", "MENU RE-GENERATE SCHEDULE HOLYDAYS"},
            // MENU LEAVE APPLICATION
            {"MENU LEAVE FORM", "MENU LEAVE AL CLOSING", "MENU LEAVE LI CLOSING", "MENU DP MANAGEMENT", "MENU DP RE-CALCULATE"},
            // MENU LEAVE BALANCING
            {"MENU ANNUAL LEAVE", "MENU LONG LEAVE", "MENU DAY OFF PAYMENT"},
            // MENU ASSEESSMENT
            {"MENU EXPLANATION COVERAGE", "MENU PERFORMANCE ASSESSMENT"},
            {"EMPLOYEE MENU"},
            // MENU EMPLOYEE OUTLET
            {"MENU EMPLOYEE OUTLET"},
            // MENU FRM SCHEDULE CHANGE
            {
                "MENU FRM SCHEDULE CHANGE",
                "MENU FRM SCHEDULE EO",
                "MENU FRM SCHEDULE EH"
            }
        },
        /* [12] MENU REPORT */
        { // MENU RPT STAFF CONTROL
            {"MENU ATTENDANCE RECORD"},
            // MENU RPT PRESENCE
            {"MENU DAILY REPORT", "MENU WEEKLY REPORT", "MENU MONTHLY REPORT", "MENU YEAR REPORT", "MENU ATTENDANCE SUMMARY"},
            // MENU RPT LATENESS
            {"MENU DAILY REPORT", "MENU WEEKLY REPORT", "MENU MONTHLY REPORT"},
            // MENU RPT SPLIT SHIFT
            {"MENU DAILY REPORT", "MENU WEEKLY REPORT", "MENU MONTHLY REPORT"},
            // MENU RPT NIGHT SHIFT
            {"MENU DAILY REPORT", "MENU WEEKLY REPORT", "MENU MONTHLY REPORT"},
            // MENU RPT ABSENTEEISM
            {"MENU DAILY REPORT", "MENU WEEKLY REPORT", "MENU MONTHLY REPORT"},
            // MENU RPT SICKNESS
            {"MENU DAILY REPORT", "MENU WEEKLY REPORT", "MENU MONTHLY REPORT", "MENU ZERO SICKNESS"},
            // MENU RPT SPECIAL DISPENSATION REPORT
            {"MENU DAILY REPORT", "MENU WEEKLY REPORT", "MENU MONTHLY REPORT"},
            // MENU RPT LEAVE REPORT
            {"MENU LEAVE & DP SUMMARY", "MENU LEAVE & DP DETAIL", "MENU LEAVE & DP SUMMARY PERIOD", "MENU LEAVE & DP DETAIL PERIOD", "MENU SPECIAL & UNPAID PERIOD", "DP EXPIRED"},
            // MENU RPT TRAINEE REPORT
            {"MENU MONTHLY REPORT", "MENU END PERIOD"},
            // MENU RPT EMPLOYEE REPORT
            {"MENU LIST OF EMPLOYEE CATEGORY", "MENU LIST OF EMPLOYEE RESIGNATION", "MENU LIST OF EMPLOYEE EDUCATION", "MENU LIST OF EMPLOYEE CATEGORY BY NAME", "MENU LIST NUMBER OF ABSENCES", "MENU LIST OF EMPLOYEE RACE"},
            // MENU RPT TRAINING
            {"MENU MONTHLY TRAINING", "MENU TRAINING PROFILES", "MENU TRAINING TARGET", "MENU REPORT BY DEPARTEMENT", "MENU REPORT BY EMPLOYEE", "MENU REPORT BY TRAINER", "MENU REPORT BY COURCE DETAIL", "MENU REPORT BY COURCE DATE"},
            // MENU RPT PAYROLL REPORT 
            {"MENU LIST OF SALARY SUMMARY"},
            {"REPORT MENU"}
        },
        /* [13] CANTEEN */
        { // CNT DATA CANTEEN
            {"MENU MANUAL VISITIATION", "MENU DATA CANTEEN SCHEDULE"},
            // CNT CANTEEN
            {"MENU CHECKLIST GROUP", "MENU CHECKLIST ITEM", "MENU CHECKLIST MARK", "MENU MENU ITEM", "MENU MEAL TIME", "MENU COMMENT GROUP", "MENU COMMENT QUESTION"},
            // CNT REPORT DETAIL
            {"MENU REPORT DETAIL DAILY", "MENU REPORT DETAIL WEEKLY", "MENU REPORT DETAIL MONTHLY"},
            // CANTEEM REPORT SUMMARY
            {"MENU DAILY MEAL RECORD REPORT", "MENU PERIODIC MEAL RECORD REPORT", "MENU MEAL REPORT", "MENU MEAL REPORT DEPARTEMENT", "MENU MONTHLY CANTEEN REPORT"},
            {"CANTEEN MENU"}
        },
        /* [14] CLINIC */
        { // CLI EMPLOYEE & FAMILY
            {"MENU EMPLOYEE & FAMILY"},
            // CLI CLINIC MEDICAL RECORD 
            {"MENU CLINIC MEDICAL RECORD"},
            // CLI EMPLOYEE VISIT
            {"MENU EMPLOYEE VISIT"},
            // CLI GUEST HANDLING
            {"MENU GUEST HANDLING"},
            // CLI MEDICINE
            {"MENU LIST OF MEDICINE", "MENU MEDICINE CONSUMPTION"},
            // CLI DISEASE
            {"MENU DISEASE TYPE"},
            // CLI MEDICAL
            {"MENU MEDICAL GROUP", "MENU MEDICAL TYPE", "MENU MEDICAL RECORD", "MENU MEDICAL LEVEL", "MENU MEDICAL CASE", "MENU MEDICAL BUDGET"},
            {"CLINIC MENU"}
        },
        /* [15] LOCKER */
        { // LOC LOCKER
            {"MENU  LOCKER"},
            // LOC LOCKER TREATMENT
            {"MENU  LOCKER TREATMENT"},
            {"LOCKER MENU"}
        },
        /* [16] MASTERDATA */
        { // MD COMPANY
            {"MENU DEPARTMENT", "MENU POSITION", "MENU SECTION", "MENU LOCKER LOCATION", "MENU LOCKER CONDITION", "MENU LEAVE PERIOD", "MENU DIVISION", "MENU PUBLIC HOLIDAY", "MENU COMPANY", "MENU LEAVE TARGET", "MENU LEAVE SETTING"},
            // MD EMPLOYEE
            {"MENU EDUCATION", "MENU LEVEL", "MENU CATEGORY", "MENU RELIGION", "MENU MARITAL", "MENU LANGUAGE", "MENU RESIGNED REASON", "MENU TRAINING", "MENU FAMILY RELATION", "MENU WARNING", "MENU REPRIMAND", "MENU RACE", "MENU IMAGE ASSIGN", "MENU AWARD TYPE", "MENU ABSENCE REASON"},
            // MD SCHEDULE
            {"MENU PERIOD", "MENU CATEGORY", "MENU SYMBOL"},
            // MD LOCKER DATA
            {"MENU LOCKER DATA LOCATION", "MENU LOCKER DATA CONDITION"},
            // MD ASSESSMENT
            {"MENU GROUP RANK", "MENU EVALUATION CRITERIA", "MENU FORM CREATOR"},
            // MD RECRUITMENT
            {"MENU GENERAL QUESTION", "MENU ILLNESS TYPE", "MENU INTERVIEW POINTS", "MENU INTERVIEWER", "MENU INTERVIEW FACTOR", "MENU ORIENTATION GROUP", "MENU ORIENTATION ACTIVITY"},
            // MD GEO AREA
            {"MENU COUNTRY", "MENU PROVINCE", "MENU REGENCY", "MENU SUBREGENCY"},
            {"MASTERDATA MENU"},
            //update by satrya 2014-03-22
            //MENU LOCATION OUTLET
            {"MENU LOCATION OUTLET"}
        },
        /* [17] SYSTEM */
        { // SYS SERVICE CENTER
            {"MENU SERVICE CENTER"},
            // MANUAL PROCESS
            {"MENU MANUAL PROCESS"},
            // ADMIN TEST MESIN
            {"MENU ADMIN TEST MESIN"},
            // ADMIN QUERY SETUP 
            {"MENU ADMIN QUERY SETUP"},
            // SYS USER MANAGEMENT
            {"MENU USER LIST", "MENU GROUP PRIVILEGE", "MENU PRIVILEGE", "MENU UPDATE PASSWORD", "MENU USER COMPARE"},
            // SYS SYSTEM MANAGEMENT
            {"MENU SYSTEM PROPERTIES", "MENU LOGIN HISTORY", "MENU SYSTEM LOG"},
            // SYS TIMEKEEPING
            {"MENU SERVICE MANAGER"},
            {"SYSTEM MENU"}
        },
        /* [18] PAYROLL */
        { //PAYROLL SETUP
            {
                "MENU GENERAL",
                "MENU PAYROLL PERIOD",
                "MENU BANK LIST",
                "MENU SALARY COMPONENT",
                "MENU SALARY LEVEL",
                "MENU CURRENCY",
                "MENU CURRENCY RATE",
                "MENU PAY SLIP GROUP",
                "MENU EMPLOYEE SETUP",
                "MENU BENEFIT CONFIG",
                "MENU BENEFIT INPUT",
                "MENU BENEFIT EMPLOYEE",
                "MENU BENEFIT HISTORY"
            },
            //PAYROLL OVERTIME
            {
                "MENU OVERTIME INDEX",
                "MENU OVERTIME FORM",
                "MENU OVERTIME REPORT & PROCESS",
                "MENU OVERTIME SUMMARY"
            },
            //PAYROLL PROCESS
            {
                "MENU PREPARE DATA",
                "MENU INPUT",
                "MENU PROCESS",
                "MENU PRINTING"
            },
            {"PAYROLL SETUP MENU"}, 
            {"PAYROLL OVERTIME MENU"}, 
            {"PAYROLL PROCESS MENU"}
        },
        /* [19] MENU HEADER */ //update by satrya 2014-03-05
        {
            {"MENU CHANGE TEMPLATE", "MENU CHANGE COMPANY PICTURE"}
        },
        /* [20] OUTSOURCE */
        { // 20
            {"OUTSOURCE MASTER PLAN", "OUTSOURCE FORM EVALUASI", "OUTSOURCE COST MASTER", "OUTSOURCE COST INPUT"},
            {"OUTSOURCE RPT APPROVAL", "OUTSOURCE RPT EVALUATION", "OUTSOURCE RPT COST PLAN", "OUTSOURCE RPT SUMMARY", "RPT IN OUT", "OUTSOURCE RPT REAL COST"}
        },
        /* [21] PERINGATAN DAN TEGURAN */
        { // 21
            {"PERINGATAN RINGAN", "PERINGATAN BERAT"},
            {"TEGURAN BAB", "TEGURAN PASAL", "TEGURAN AYAT"}
        },
        /* [22] MENU PERINGATAN DAN TEGURAN */
        { // 21
            {"MENU PERINGATAN RINGAN", "MENU PERINGATAN BERAT"},
            {"MENU TEGURAN BAB", "MENU TEGURAN PASAL", "MENU TEGURAN AYAT"}
        },
        /* [23] MENU PELATIHAN */
        {
            /* [0]  "MENU TIPE PELATIHAN", */
            {"MENU TIPE PELATIHAN"},
            /* [1]  "MENU LOKASI PELATIHAN", */
            {"MENU LOKASI PELATIHAN"},
            /* [2]  "MENU PROGRAM PELATIHAN", */
            {"MENU PROGRAM PELATIHAN"},
            /* [3]  "MENU RENCANA PELATIHAN", */
            {"MENU RENCANA PELATIHAN"},
            /* [4]  "MENU PELATIHAN REAL", */
            {"MENU PELATIHAN REAL"},
            /* [5]  "MENU PENCARIAN PELATIHAN", */
            {"MENU PENCARIAN PELATIHAN"},
            /* [6]  "MENU SEJARAH PELATIHAN", */
            {"MENU SEJARAH PELATIHAN"},
            /* [7]  "MENU GAP TRAINING", */
            {"MENU GAP TRAINING"},
            /* [8]  "MENU TRAINING ORGANIZER", */
            {"MENU TRAINING ORGANIZER"},
            /* [9]  "MENU ORGANIZER TYPE", */
            {"MENU ORGANIZER TYPE"},
            /* [10]  "MENU TRAINING SCORE" */
            {"MENU TRAINING SCORE"}
        },
        /* [24] MENU ORGANISASI */
        {
            /* [0] "MENU STRUKTUR ORGANISASI" */
            {"MENU STRUKTUR ORGANISASI"},
            /* [1] "MENU CETAK UNIT KERJA" */
            {"MENU CETAK UNIT KERJA"},
            /* [2] "MENU SEJARAH JABATAN" */
            {"MENU SEJARAH JABATAN"},
            /* [3] "MENU POSISI DAN JOB DESC" */
            {"MENU POSISI DAN JOB DESC"},
            /* [4] "MENU INFORMASI PEJABAT" */
            {"MENU INFORMASI PEJABAT"},
            /* [5] "MENU STRUKTUR PERUSAHAAN" */
            {"MENU PERUSAHAAN", "MENU SATUAN KERJA", "MENU UNIT", "MENU SUB UNIT", "MENU JABATAN", "MENU DIVISION TYPE", "MENU DEPARTMENT TYPE", "MENU PUBLIC LIBURAN"},
            /* [6] "MENU STRUKTUR TEMPLATE" */
            {"MENU STRUKTUR TEMPLATE"}
        },
        /* [25] DOKUMEN SURAT */
        {
            {"DOCUMENT TYPE", "DOCUMENT EXPENSE", "DOCUMENT MASTER"},
            {"EMP DOCUMENT"}
        }
    };
    
    
    public static final int[][][][] objectCommands = {
        // 0 LOGIN ACCESS
        { // LOGIN ACCESS
            {
                // LOGIN PAGE
                {COMMAND_VIEW, COMMAND_SUBMIT},
                // LOGOUT PAGE
                {COMMAND_VIEW, COMMAND_SUBMIT},},
            //MENU ACCESS        
            {
                //MENU PAGE        
                {COMMAND_VIEW},
                //MENU EMPLOYYE PAGE        
                {COMMAND_VIEW}
            }
        },
        // 1 EMPLOYEE
        { // EMP DATABANK
            { // EMP DATABANK
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT, COMMAND_GENERATE_SALARY_LEVEL},
                /* "DATABANK PERSONAL DATA", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT, COMMAND_GENERATE_SALARY_LEVEL},
                /* "DATABANK FAMILY MEMBER", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK LANGUANGE AND COMPETENCE", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK EDUCATION", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK EXPERIENCE", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK CAREERPATH", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK TRAINING", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK WARNING", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK REPRIMAND", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK AWARD", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK PICTURE", */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                /* "DATABANK RELEVANT DOC" */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP ATTENDANCE
            { // WORKING SCHEDULE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE OPNAME
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE MANAGEMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // DAY OFF PAYMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // PRESENCE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // MANUAL PRESENCE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MANUAL REGISTRATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // RE-GENERATE SCHEDULE HOLIDAYS
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // EMP SALARY
            { // EMP SALARY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // EMP APPRAISAL
            { // EXPLANATIONS AND COVERAGE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // PERFORMANCE APPRAISAL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT, COMMAND_START}
            },
            // EMP RECOGNITION
            { // ENTRY PER DEPARTMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // UPDATE PER EMPLOYEE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // EMP TRAINING
            { // TRAINING HISTORY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // SPECIAL ARCHIEVEMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // TRAINING ACTIVITIES
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // TRAINING SEARCH
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // OBJ_TRAINING_ACHIEVE_EDIT         
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // OBJ_TRAINING_ACHIEVE_LIST         
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_PRINT},
                // OBJ_GENERAL_TRAINING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // OBJ_DEPARTMENTAL_TRAINING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP RECRUITMENT
            { // STAFF REQUISITION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // EMPLOYEE APPLICATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // ORIENTATION CHECK LIST
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // REMINDER
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //EMPLOYEE RECRUITMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //EMPLOYEE RECRUITMENT EDUCATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //EMPLOYEE RECRUITMENT HISTORY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //EMPLOYEE RECRITMENT LANGGUAGE 
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //EMPLOYEE RECRITMENT REFERENCES
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //EMPLOYEE RECRITMENT GENERAL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //EMPLOYEE RECRITMENT INTERVIEW
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP LEAVE MANAGEMENT
            { // DP MANAGEMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // AL MANAGEMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LL MANAGEMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // OPNAME AL        
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // OPNAME LL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // OPNAME DP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // AL CLOSING        
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LL CLOSING        
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LL APP SRC        
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},},
            // EMP LEAVE APPLICATION
            { // DP APPLICATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE APPLICATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE FORM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE APPLICATION AL CLOSING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE LI CLOSING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE DP MANAGEMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LEAVE DP RE-CALCULATE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP ABSENCE MANAGEMENT
            { // STAFF REQUISITION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP COMPANY STRUCTURE
            { // COMPANY STRUCTURE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP NEW EMPLOYEE
            { // NEW EMPLOYEE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP WARNING & REPRIMAND
            { // WARNING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // REPRIMAND
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // EMP EXCUSE APPLICATION
            { // EXCUSE FORM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // LEAVE BALANCING
            { // ANNUAL LEAVE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // LONG LEAVE LEAVE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // DAY OFF PAYMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // ASSESSMENT
            { // EXPLANATION COVERAGE 
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                // PERFORMANCE ASSESSMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            //employee outlet
            { // EMPLOYEE OUTLET
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            { // EMP EXTRA SCHEDULE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            { /* [18] EMP CANDIDATE */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            { /* [19] COMPETENCE */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            { /* [20] PERFORMANCE */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },
        // 2 LOCKER
        { // LOC LOCKER
            { // LOC LOCKER
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // LOC LOCKER TREATMENT
            { // LOC LOCKER TREATMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },
        // 3 CANTEEN
        {
            // CNT MENU LIST MONTHLY
            {
                // CNT MENU LIST MONTHLY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CNT DAILY EVALUATION
            {
                // CNT DAILY EVALUATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CNT COMMENT_CARD
            {
                // CNT COMMENT_CARD
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CNT REPORT SUMMARY
            {
                // CNT SUMMARY DAILY
                {COMMAND_VIEW, COMMAND_PRINT},
                // CNT SUMMARY WEEKLY
                {COMMAND_VIEW, COMMAND_PRINT},
                // CNT SUMMARY MONTHLY
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // CNT REPORT DETAIL
            {
                // CNT DETAIL DAILY
                {COMMAND_VIEW, COMMAND_PRINT},
                // CNT DETAIL WEEKLY
                {COMMAND_VIEW, COMMAND_PRINT},
                // CNT DETAIL MONTHLY
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // CNT SCHEDULE
            {
                // CNT SCHEDULE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CNT CAFE
            {
                // CNT CAFE CHECKLIST
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CNT CAFE EVALUATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CNT DATA CANTEEN
            {
                // MANUAL VISITIATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // DATA CANTEEN SCHEDULE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CNT CANTEEN
            {
                // CHECKLIST GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CHECKLIST ITEM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CHECKLIST MARK
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MENU ITEM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MEAL TIME
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // COMMENT GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // COMMENT QUESTION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CNT CANTEEN REPORT DETAIL
            {
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // CNT CANTEEN REPORT SUMMARY
            {
                // DAILY MEAL RECORD REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // PERIODIC MEAL RECORD REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MEAL REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MEAL REPORT DEPARTEMENT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY CANTEEN REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },},
        // 4 CLINIC
        { // CLI MEDICINE
            { // LIST OF MEDICINE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MEDICINE CONSUMPTION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            // CLI EMPLOYEE VISIT
            { // EMPLOYEE VISIT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CLI GUEST HANDLING
            { // GUEST HANDLING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CLI DISEASE
            { // DISEASE TYPE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CLI MEDICAL
            { // MEDICAL GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // TYPE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MEDICAL RECORD
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MEDICAL LEVEL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MEDICAL CASE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MEDICAL BUDGET
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CLI EMPLOYEE & FAMILY
            { // EMPLOYEE & FAMILY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // CLI CLINIC MEDICAL RECORD
            { // CLINIC MEDICAL RECORD
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },
        // 5 REPORT
        {
            // RPT EMPLOYMENT
            {
                // STAFF RECAPITULATION
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT LEAVE & DP RECORD
            {
                // GENERAL    
                {COMMAND_VIEW, COMMAND_PRINT},
                // DETAIL
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT STAFF CONTROL
            {
                // ATTENDANCE RECORD
                {COMMAND_VIEW, COMMAND_PRINT},
                // MANNING SUMMARY
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT LOCKERS
            {
                // LOCKERS
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT CLINIC
            {
                // MEDICAL EXPENSES
                {COMMAND_VIEW, COMMAND_PRINT},
                // SUMMARY RECEIPTS
                {COMMAND_VIEW, COMMAND_PRINT},
                // EMPLOYEE VISITS
                {COMMAND_VIEW, COMMAND_PRINT},
                // GUEST HANDLING
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT RECOGNITION
            {
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // QUARTERLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // YEARLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT PRESENCE
            {
                // PRESENCE REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // YEAR REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // ATTENDANCE SUMMARY 
                {COMMAND_VIEW, COMMAND_PRINT},
                // Rekapitulasi Absensi 2016-09-21
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT TRAINING
            {
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // PROFILES REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // TARGET REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // REPORT BY DEPARTEMENT
                {COMMAND_VIEW, COMMAND_PRINT},
                // REPORT BY EMPLOYEE
                {COMMAND_VIEW, COMMAND_PRINT},
                // REPORT BY TRAINER
                {COMMAND_VIEW, COMMAND_PRINT},
                // REPORT COURSE DETAIL
                {COMMAND_VIEW, COMMAND_PRINT},
                // REPORT COURSE DATE
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT LATENESS
            {
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT ABSENTEEISM
            {
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT SPECIAL DISCREPANCY
            {
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT SICK DAYS
            {
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // ZERO REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT SPLIT SHIFT
            {
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT NIGHT  SHIFT
            {
                // DAILY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT LEAVE REPORT
            {
                // LEAVE DP SUMMARY
                {COMMAND_VIEW, COMMAND_PRINT},
                // LEAVE DP DETAIL
                {COMMAND_VIEW, COMMAND_PRINT},
                // SPECIAL UNPAID PERIOD
                {COMMAND_VIEW, COMMAND_PRINT},
                // DP EXPIRED
                {COMMAND_VIEW, COMMAND_PRINT},
                // LEAVE DP SUMMARY PERIOD
                {COMMAND_VIEW, COMMAND_PRINT},
                // LEAVE DP DETAIL PERIOD
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT SPECIAL DISPENSATION REPORT
            {
                // DAILY
                {COMMAND_VIEW, COMMAND_PRINT},
                // WEEKLY
                {COMMAND_VIEW, COMMAND_PRINT},
                // MONTHLY
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT TRAINEE REPORT
            {
                // MONTHLY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // END REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT EMPLOYEE REPORT
            {
                // LIST EMPLOYEE CATEGORY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // LIST EMPLOYEE RESIGNATION REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // LIST EDUCATION REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // LIST CATEGORY BY NAME REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // LIST NUMBERABSENCES REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                // LIST EMPLOYEE RACE REPORT
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // RPT PAYROLL REPORT
            {
                // LIST SALARY SUMMARY REPORT
                {COMMAND_VIEW, COMMAND_PRINT},
                {COMMAND_VIEW, COMMAND_PRINT},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_PRINT},
                {COMMAND_VIEW, COMMAND_PRINT},
                {COMMAND_VIEW, COMMAND_PRINT},
                /* Update by Hendra Putu | 2016-09-21 */
                {COMMAND_VIEW, COMMAND_PRINT},//"GAJI TRANSFER",
                {COMMAND_VIEW, COMMAND_PRINT},//"REKAP GAJI",
                {COMMAND_VIEW, COMMAND_PRINT},//"REKONSILIASI",
                {COMMAND_VIEW, COMMAND_PRINT},//"LEAVE ENTITLE",
                {COMMAND_VIEW, COMMAND_PRINT}//"TAKEN LEAVE"
            }
        },
        // 6 MASTERDATA
        { // MD COMPANY
            { // DEPARTMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // POSITION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // SECTION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // LOCKER LOCATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // LOCKER CONDITION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // LEAVE PERIOD
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // DIVISION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // PUBLIC_HOLIDAY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // COMPANY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // LEAVE TARGET
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //leave setting
                //update by satrya 2014-06-12
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // MD EMPLOYEE
            { // EDUCATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // LEVEL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CATEGORY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // RELIGION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MARITAL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // LANGUAGE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // RESIGNED REASON
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // TRAINING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // FAMILY RELATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // WARNING
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // REPRIMAND
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // RACE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // IMAGE ASSIGN
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // AWARD TYPE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // ABSENCE REASON
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // GRADE LEVEL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // INFORMATION HRD
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // REWARD n PUNISMENT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // KOEFISIEN POSITION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // ENTRI OPNAME SALES
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // MD SCHEDULE
            { // PERIOD
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CATEGORY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // SYMBOL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // MD PERFORMANCE APPRAISAL
            { // GROUP RANK
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CATEGORY APPRAISAL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // EVALUATION CRITERIA
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // FORM CREATOR
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // MD CANTEEN
            { // CHECK LIST GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CHECK LIST ITEM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // CHECK LIST MARK
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MENU ITEM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // MEAL TIME
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // COMMENT GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // COMMENT QUESTION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // MD RECRUITMENT
            { // GENERAL QUESTION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // ILLNESS TYPE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // INTERVIEW POINTS
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // INTERVIEWER
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // INTERVIEW FACTOR
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // ORIENTATION GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // ORIENTATION ACTIVITY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            // MD EMP UPLOAD DATA
            { // TRAINEE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // DAILY WORKER
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            },
            // MD LEAVE OPNAME
            { // DP OPNAME
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // AL OPNAME
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // LL OPNAME
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            },
            // MD LOCKER DATA
            { // LOCKER DATA LOCATION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // LOCKER DATA CONDITION
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            },
            // MD ASSESSMENT
            { // GROUP RANK
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // EVALUATION CRITERIA
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // FORM CREATOR
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            },
            // MD GEO AREA
            { // COUNTRY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // PROVINCE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // REGENCY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                // SUBREGENCY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            },
            // MD LOCATION
            //update by satrya 2014-02-25
            { // COUNTRY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                /// Jenis SO
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                /// PERIODE SO
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            }
        },
        // 7 SYSTEM
        { // SYS USER MANAGEMENT
            {
                // USER LIST
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                // GROUP PRIVILEGE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                // PRIVILEGE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                // USER UPDATE PASSWORD
                {COMMAND_VIEW, COMMAND_UPDATE},
                // COMPARE USER
                {COMMAND_VIEW, COMMAND_PRINT}
            },
            // SYS SYSTEM MANAGEMENT
            {
                // BACK UP SERVICE
                {COMMAND_VIEW, COMMAND_START, COMMAND_STOP},
                // SYSTEM PROPERTIES
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // LOGIN_HISTORY
                {COMMAND_VIEW, COMMAND_DELETE},
                // SYSTEM LOG
                {COMMAND_VIEW, COMMAND_DELETE}
            },
            // SYS BARCODE
            {
                // INSERT BARCODE TO DATABASE
                {COMMAND_VIEW, COMMAND_START},
                // UPLOAD BARCODE TO MACHINE
                {COMMAND_VIEW, COMMAND_START},
                // INSERT AND UPLOAD BARCODE
                {COMMAND_VIEW, COMMAND_START},
                // UPLOAD PER EMPLOYEE
                {COMMAND_VIEW, COMMAND_START}
            },
            // SYS TIMEKEEPING
            {
                // SERVICE MANAGER
                {COMMAND_VIEW, COMMAND_START},
                // DOWNLOAD DATA
                {COMMAND_VIEW, COMMAND_START},
                // CHECK MACHINE
                {COMMAND_VIEW, COMMAND_START},
                // RESET MACHINE
                {COMMAND_VIEW, COMMAND_START}
            },
            // SYS WORKING SCHEDULE
            {
                // UPLOAD & INSERT SCHEDULE
                {COMMAND_VIEW, COMMAND_START}
            },
            // SYS IMPORT PRESENCE
            {
                // IMPORT PRESENCE
                {COMMAND_VIEW, COMMAND_START}
            },
            // SYS SERVICE CENTER
            {
                // SERVICE CENTER
                {COMMAND_VIEW, COMMAND_START},
                // SERVICE CENTER
                {COMMAND_VIEW, COMMAND_VIEW_DETAIL, COMMAND_UPDATE, COMMAND_START, COMMAND_STOP, COMMAND_SUBMIT},},
            // SYS MANUAL CHECKING
            {
                // ALL
                {COMMAND_VIEW, COMMAND_START},
                // PRESENCE
                {COMMAND_VIEW, COMMAND_START},
                // ABSENTEEISM
                {COMMAND_VIEW, COMMAND_START},
                // LATENESS
                {COMMAND_VIEW, COMMAND_START},
                // DP
                {COMMAND_VIEW, COMMAND_START},
                // AL
                {COMMAND_VIEW, COMMAND_START},
                // LL
                {COMMAND_VIEW, COMMAND_START},
                // BACKUP DB
                {COMMAND_VIEW, COMMAND_START}
            },
            // SYS MANUAL PROCESS
            {
                // MANUAL PROCESS
                {COMMAND_VIEW, COMMAND_START}
            },
            // SYS ADMIN TEST MESIN
            {
                // ADMIN TEST MESIN
                {COMMAND_VIEW, COMMAND_START}
            },
            // SYS ADMIN QUERY SETUP
            {
                // ADMIN TEST MESIN
                {COMMAND_VIEW, COMMAND_START}
            }
        },
        // 8 SODEVI
        { // MASTER
            {
                // OPERATING SYSTEM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // TEST
            {
                // TEST SCRIPT
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                // VIEW RESULT
                {COMMAND_VIEW}
            }
        },
        // PAYROLL 
        { // 9 PAYROLL SETUP
            { //"PAYROLL SETUP GENERAL"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP PERIOD"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP BANK"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP COMPONENT"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP LEVEL"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP CURRENCY"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP RATE"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP PAY SLIP GROUP"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL SETUP EMPLOYEE SETUP"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                /* PAYROLL BENEFIT CONFIG */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                /* PAYROLL BENEFIT INPUT */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                /* PAYROLL BENEFIT EMPLOYEE */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                /* PAYROLL BENEFIT HISTORY */
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            //PAYROLL OVERTIME
            { //"PAYROLL OVERTIME INDEX"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL OVERTIME FORM"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //"PAYROLL OVERTIME IMPORT"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL OVERTIME INPUT"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL OVERTIME POSTING"}
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //"PAYROLL OVERTIME REPORT"}
                {COMMAND_VIEW, COMMAND_PRINT},
                //"PAYROLL OVERTIME SUMMARY"}
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            },
            //PAYROLL PROCESS
            { //"PAYROLL PROCESS PREPARE"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL PROCESS INPUT"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_VIEW_DETAIL},
                //"PAYROLL PROCESS PROCESS"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_RUN_PROCESS},
                //"PAYROLL PROCESS PRINT"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"PAYROLL PROCESS SIMULATION"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                // PAYROLL PAYSLIP GROUP
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
                
            //PAYROLL TAX
            { //"PAYROLL TAX SETUP"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"PAYROLL TAX CALCULATE"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT},
                //PAYROLL TAX REPORT"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_PRINT}
            }
        },
        // 10 TRAINING
        { // TRAINING TYPE
            {
                // TRAINING TYPE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // TRAINING VANUE
            {
                // TRAINING VANUE
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // TRAINING PROGRAM
            {
                // TRAINING PROGRAM
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // TRAINING PLAN
            {
                // TRAINING PLAN
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // TRAINING ACTUAL
            {
                // TRAINING ACTUAL
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // TRAINING SEARCH
            {
                // TRAINING SEARCH
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            // TRAINING HISTORY
            {
                // TRAINING HISTORY
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            {
                {COMMAND_VIEW},},
            // TRAINING ORGANIZER
            {
                // TRAINING ORGANIZER
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },},
        // 11 MENU EMPLOYEE
        { // MENU DATABANK
            { // MENU DATABANK
                {COMMAND_VIEW}
            },
            // MENU COMPANY STRUCTURE
            { // MENU COMPANY STRUCTURE
                {COMMAND_VIEW}
            },
            // MENU NEW EMPLOYEE
            { // MENU NEW EMPLOYEE
                {COMMAND_VIEW}
            },
            // MENU ABSENCE MANAGEMENT
            { // MENU STAFF REQUISITION
                {COMMAND_VIEW}
            },
            // MENU RECOGNITION
            { // MENU ENTRY PER DEPARTMENT
                {COMMAND_VIEW},
                // MENU UPDATE PER EMPLOYEE
                {COMMAND_VIEW}
            },
            // MENU RECRUITMENT
            { // MENU STAFF REQUISITION
                {COMMAND_VIEW},
                // MENU EMPLOYEE APPLICATION
                {COMMAND_VIEW},
                // MENU ORIENTATION CHECK LIST
                {COMMAND_VIEW},
                // MENU REMINDER
                {COMMAND_VIEW}
            },
            // MENU WARNING & REPRIMAND
            { // MENU WARNING
                {COMMAND_VIEW},
                // MENU REPRIMAND
                {COMMAND_VIEW}
            },
            // MENU EXCUSE APPLICATION
            { // MENU EXCUSE FORM
                {COMMAND_VIEW}
            },
            // MENU ATTENDANCE
            { // MENU WORKING SCHEDULE
                {COMMAND_VIEW},
                // MENU MANUAL REGISTRATION
                {COMMAND_VIEW},
                // MENU RE-GENERATE SCHEDULE HOLIDAYS
                {COMMAND_VIEW}
            },
            // MENU LEAVE APPLICATION
            { // MENU LEAVE FORM
                {COMMAND_VIEW},
                // MENU LEAVE AL CLOSING
                {COMMAND_VIEW},
                // MENU LEAVE LI CLOSING
                {COMMAND_VIEW},
                // MENU LEAVE DP MANAGEMENT
                {COMMAND_VIEW},
                // MENU LEAVE DP RE-CALCULATE
                {COMMAND_VIEW}
            },
            // MENU LEAVE BALANCING
            { // MENU ANNUAL LEAVE
                {COMMAND_VIEW},
                // MENU LONG LEAVE LEAVE
                {COMMAND_VIEW},
                // MENU DAY OFF PAYMENT
                {COMMAND_VIEW}
            },
            // MENU ASSESSMENT
            { // MENU EXPLANATION COVERAGE 
                {COMMAND_VIEW},
                // MENU PERFORMANCE ASSESSMENT
                {COMMAND_VIEW}
            },
            //menu employee
            {
                {COMMAND_VIEW}
            },
            //update by satrya 2014-093-22
            //Menu EMployee Outlet
            {
                {COMMAND_VIEW}
            },
            { // Menu Form Schedule Change
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW}
            }
        },
        // 12 MENU REPORT
        {// MENU RPT EMPLOYMENT
            // RPT STAFF CONTROL
            {
                // MENU ATTENDANCE RECORD
                {COMMAND_VIEW}
            },
            // MENU RPT PRESENCE
            {// MENU PRESENCE REPORT
                {COMMAND_VIEW},
                // DAILY REPORT
                {COMMAND_VIEW},
                // WEEKLY REPORT
                {COMMAND_VIEW},
                // MONTHLY REPORT
                {COMMAND_VIEW},
                // YEAR REPORT
                {COMMAND_VIEW},
                // ATTENDANCE SUMMARY 
                {COMMAND_VIEW}
            },
            // MENU RPT LATENESS
            { // DAILY REPORT
                {COMMAND_VIEW},
                // WEEKLY REPORT
                {COMMAND_VIEW},
                // MONTHLY REPORT
                {COMMAND_VIEW}
            },
            // MENU RPT SPLIT SHIFT
            { // DAILY REPORT
                {COMMAND_VIEW},
                // WEEKLY REPORT
                {COMMAND_VIEW},
                // MONTHLY REPORT
                {COMMAND_VIEW}
            },
            // MENU RPT NIGHT  SHIFT
            { // DAILY REPORT
                {COMMAND_VIEW},
                // WEEKLY REPORT
                {COMMAND_VIEW},
                // MONTHLY REPORT
                {COMMAND_VIEW}
            },
            // MENU MENU RPT ABSENTEEISM
            { // DAILY REPORT
                {COMMAND_VIEW},
                // WEEKLY REPORT
                {COMMAND_VIEW},
                // MONTHLY REPORT
                {COMMAND_VIEW}
            },
            // MENU RPT SICKNESS
            { // DAILY REPORT
                {COMMAND_VIEW},
                // WEEKLY REPORT
                {COMMAND_VIEW},
                // MONTHLY REPORT
                {COMMAND_VIEW},
                // ZERO REPORT
                {COMMAND_VIEW}
            },
            // MENU RPT SPECIAL DISPENSATION REPORT
            { // DAILY
                {COMMAND_VIEW},
                // WEEKLY
                {COMMAND_VIEW},
                // MONTHLY
                {COMMAND_VIEW}
            },
            // MENU RPT LEAVE REPORT
            { // LEAVE DP SUMMARY
                {COMMAND_VIEW},
                // LEAVE DP DETAIL
                {COMMAND_VIEW},
                // LEAVE DP SUMMARY PERIOD
                {COMMAND_VIEW},
                // LEAVE DP DETAIL PERIOD
                {COMMAND_VIEW},
                // SPECIAL UNPAID PERIOD
                {COMMAND_VIEW},
                // DP EXPIRED
                {COMMAND_VIEW}
            },
            // MENU RPT TRAINEE REPORT
            { // MONTHLY REPORT
                {COMMAND_VIEW},
                // END REPORT
                {COMMAND_VIEW}
            },
            // MENU RPT EMPLOYEE REPORT
            { // LIST EMPLOYEE CATEGORY REPORT
                {COMMAND_VIEW},
                // LIST EMPLOYEE RESIGNATION REPORT
                {COMMAND_VIEW},
                // LIST EDUCATION REPORT
                {COMMAND_VIEW},
                // LIST CATEGORY BY NAME REPORT
                {COMMAND_VIEW},
                // LIST NUMBERABSENCES REPORT
                {COMMAND_VIEW},
                // LIST EMPLOYEE RACE REPORT
                {COMMAND_VIEW}
            },
            // MENU RPT TRAINING
            { // MONTHLY REPORT
                {COMMAND_VIEW},
                // PROFILES REPORT
                {COMMAND_VIEW},
                // TARGET REPORT
                {COMMAND_VIEW},
                // REPORT BY DEPARTEMENT
                {COMMAND_VIEW},
                // REPORT BY EMPLOYEE
                {COMMAND_VIEW},
                // REPORT BY TRAINER
                {COMMAND_VIEW},
                // REPORT COURSE DETAIL
                {COMMAND_VIEW},
                // REPORT COURSE DATE
                {COMMAND_VIEW}
            },
            // MENU RPT PAYROLL REPORT
            {
                // LIST SALARY SUMMARY REPORT
                {COMMAND_VIEW}
            },
            {
                {COMMAND_VIEW},}
        },
        // 13 CANTEEN
        {// CNT DATA CANTEEN
            {// MANUAL VISITIATION
                {COMMAND_VIEW},
                // DATA CANTEEN SCHEDULE
                {COMMAND_VIEW}
            },
            // CNT CANTEEN
            {// CHECKLIST GROUP
                {COMMAND_VIEW},
                // CHECKLIST ITEM
                {COMMAND_VIEW},
                // CHECKLIST MARK
                {COMMAND_VIEW},
                // MENU ITEM
                {COMMAND_VIEW},
                // MEAL TIME
                {COMMAND_VIEW},
                // COMMENT GROUP
                {COMMAND_VIEW},
                // COMMENT QUESTION
                {COMMAND_VIEW}
            },
            // CNT CANTEEN REPORT DETAIL
            {// DAILY REPORT
                {COMMAND_VIEW},
                // WEEKLY REPORT
                {COMMAND_VIEW},
                // MONTHLY REPORT
                {COMMAND_VIEW}
            },
            // CNT CANTEEN REPORT SUMMARY
            {// DAILY MEAL RECORD REPORT
                {COMMAND_VIEW},
                // PERIODIC MEAL RECORD REPORT
                {COMMAND_VIEW},
                // MEAL REPORT
                {COMMAND_VIEW},
                // MEAL REPORT DEPARTEMENT
                {COMMAND_VIEW},
                // MONTHLY CANTEEN REPORT
                {COMMAND_VIEW}
            },
            {
                {COMMAND_VIEW},}
        },
        // 14 CLINIC
        { // CLI EMPLOYEE & FAMILY
            { // EMPLOYEE & FAMILY
                {COMMAND_VIEW}
            },
            // CLI CLINIC MEDICAL RECORD
            { // CLINIC MEDICAL RECORD
                {COMMAND_VIEW}
            },
            // CLI EMPLOYEE VISIT
            { // EMPLOYEE VISIT
                {COMMAND_VIEW}
            },
            // CLI GUEST HANDLING
            { // GUEST HANDLING
                {COMMAND_VIEW}
            },
            // CLI MEDICINE
            { // LIST OF MEDICINE
                {COMMAND_VIEW},
                // MEDICINE CONSUMPTION
                {COMMAND_VIEW}
            },
            // CLI DISEASE
            { // DISEASE TYPE
                {COMMAND_VIEW}
            },
            // CLI MEDICAL
            { // MEDICAL GROUP
                {COMMAND_VIEW},
                // TYPE
                {COMMAND_VIEW},
                // MEDICAL RECORD
                {COMMAND_VIEW},
                // MEDICAL LEVEL
                {COMMAND_VIEW},
                // MEDICAL CASE
                {COMMAND_VIEW},
                // MEDICAL BUDGET
                {COMMAND_VIEW}
            },
            {
                {COMMAND_VIEW},}
        },
        // 15 LOCKER
        { // LOC LOCKER
            { // LOC LOCKER
                {COMMAND_VIEW}
            },
            // LOC LOCKER TREATMENT
            { // LOC LOCKER TREATMENT
                {COMMAND_VIEW}
            },
            {
                {COMMAND_VIEW},}
        },
        // 16 MASTERDATA
        { // MD COMPANY
            { // DEPARTMENT
                {COMMAND_VIEW},
                // POSITION
                {COMMAND_VIEW},
                // SECTION
                {COMMAND_VIEW},
                // LOCKER LOCATION
                {COMMAND_VIEW},
                // LOCKER CONDITION
                {COMMAND_VIEW},
                // LEAVE PERIOD
                {COMMAND_VIEW},
                // DIVISION
                {COMMAND_VIEW},
                // PUBLIC_HOLIDAY
                {COMMAND_VIEW},
                // COMPANY
                {COMMAND_VIEW},
                // LEAVE TARGET
                {COMMAND_VIEW},
                //leave setting
                //update by satrya 2014-06-12
                {COMMAND_VIEW}
            },
            // MD EMPLOYEE
            { // EDUCATION
                {COMMAND_VIEW},
                // LEVEL
                {COMMAND_VIEW},
                // CATEGORY
                {COMMAND_VIEW},
                // RELIGION
                {COMMAND_VIEW},
                // MARITAL
                {COMMAND_VIEW},
                // LANGUAGE
                {COMMAND_VIEW},
                // RESIGNED REASON
                {COMMAND_VIEW},
                // TRAINING
                {COMMAND_VIEW},
                // FAMILY RELATION
                {COMMAND_VIEW},
                // WARNING
                {COMMAND_VIEW},
                // REPRIMAND
                {COMMAND_VIEW},
                // RACE
                {COMMAND_VIEW},
                // IMAGE ASSIGN
                {COMMAND_VIEW},
                // AWARD TYPE
                {COMMAND_VIEW},
                // ABSENCE REASON
                {COMMAND_VIEW}
            },
            // MD SCHEDULE
            { // PERIOD
                {COMMAND_VIEW},
                // CATEGORY
                {COMMAND_VIEW},
                // SYMBOL
                {COMMAND_VIEW}
            },
            // MD LOCKER DATA
            { // LOCKER DATA LOCATION
                {COMMAND_VIEW},
                // LOCKER DATA CONDITION
                {COMMAND_VIEW}
            },
            // MD ASSESSMENT
            { // GROUP RANK
                {COMMAND_VIEW},
                // EVALUATION CRITERIA
                {COMMAND_VIEW},
                // FORM CREATOR
                {COMMAND_VIEW}
            },
            // MD RECRUITMENT
            { // GENERAL QUESTION
                {COMMAND_VIEW},
                // ILLNESS TYPE
                {COMMAND_VIEW},
                // INTERVIEW POINTS
                {COMMAND_VIEW},
                // INTERVIEWER
                {COMMAND_VIEW},
                // INTERVIEW FACTOR
                {COMMAND_VIEW},
                // ORIENTATION GROUP
                {COMMAND_VIEW},
                // ORIENTATION ACTIVITY
                {COMMAND_VIEW}
            },
            // MD GEO AREA
            { // COUNTRY
                {COMMAND_VIEW},
                // PROVINCE
                {COMMAND_VIEW},
                // REGENCY
                {COMMAND_VIEW},
                // SUBREGENCY
                {COMMAND_VIEW}
            },
            {
                {COMMAND_VIEW},},
            //update by satrya 2014-03-22
            //MNU LOCATION OUTLET
            {
                {COMMAND_VIEW},}
        },
        // 17 SYSTEM
        { // SYS SERVICE CENTER
            {// SERVICE CENTER
                {COMMAND_VIEW}
            },
            // SYS MANUAL PROCESS
            {// MANUAL PROCESS
                {COMMAND_VIEW}
            },
            // SYS ADMIN TEST MESIN
            {// ADMIN TEST MESIN
                {COMMAND_VIEW}
            },
            // SYS ADMIN QUERY SETUP
            {// ADMIN TEST MESIN
                {COMMAND_VIEW}
            },
            // SYS USER MANAGEMENT
            {
                // USER LIST
                {COMMAND_VIEW},
                // GROUP PRIVILEGE
                {COMMAND_VIEW},
                // PRIVILEGE
                {COMMAND_VIEW},
                // USER UPDATE PASSWORD
                {COMMAND_VIEW},
                // COMPARE USER
                {COMMAND_VIEW}
            },
            // SYS SYSTEM MANAGEMENT
            {// SYSTEM PROPERTIES
                {COMMAND_VIEW},
                // LOGIN_HISTORY
                {COMMAND_VIEW},
                // SYSTEM LOG
                {COMMAND_VIEW}
            },
            // SYS TIMEKEEPING
            {// SERVICE MANAGER
                {COMMAND_VIEW},},
            {
                {COMMAND_VIEW},}
        },
        // PAYROLL 
        { // 18 PAYROLL SETUP
            { //"PAYROLL SETUP GENERAL"
                {COMMAND_VIEW},
                //"PAYROLL SETUP PERIOD"
                {COMMAND_VIEW},
                //"PAYROLL SETUP BANK"
                {COMMAND_VIEW},
                //"PAYROLL SETUP COMPONENT"
                {COMMAND_VIEW},
                //"PAYROLL SETUP LEVEL"
                {COMMAND_VIEW},
                //"PAYROLL SETUP CURRENCY"
                {COMMAND_VIEW},
                //"PAYROLL SETUP RATE"
                {COMMAND_VIEW},
                //"PAYROLL SETUP PAY SLIP GROUP"
                {COMMAND_VIEW},
                //"PAYROLL SETUP EMPLOYEE SETUP"
                {COMMAND_VIEW}
            },
            //PAYROLL OVERTIME
            { //"PAYROLL OVERTIME INDEX"
                {COMMAND_VIEW},
                //"PAYROLL OVERTIME FORM"
                {COMMAND_VIEW},
                //"PAYROLL OVERTIME REPORT"}
                {COMMAND_VIEW},
                //"PAYROLL OVERTIME SUMMARY"}
                {COMMAND_VIEW}
            },
            //PAYROLL PROCESS
            { //"PAYROLL PROCESS PREPARE"
                {COMMAND_VIEW},
                //"PAYROLL PROCESS INPUT"
                {COMMAND_VIEW},
                //"PAYROLL PROCESS PROCESS"
                {COMMAND_VIEW},
                //"PAYROLL PROCESS PRINT"
                {COMMAND_VIEW}
            },
            {
                {COMMAND_VIEW},},
            {
                {COMMAND_VIEW},},
            {
                {COMMAND_VIEW},}
        },
        //update by satrya 2014-03-05
        // 19 MENU HEADER
        { //"TEMPALTE CHANGE"
            {
                {COMMAND_VIEW},
                //"PICTURE COMPANY"
                {COMMAND_VIEW}
            },} // by kartika 2015-09-14
        // 20 Menu Outsource
        ,
        {
            {//"OUTSOURCE MASTER PLAN"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                //"OUTSOURCE FORM EVALUASI"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT},
                //"OUTSOURCE COST MASTER"
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                //"OUTSOURCE COST INPUT"}
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE, COMMAND_SUBMIT}
            },
            { //"OUTSOURCE RPT APPROVAL"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"OUTSOURCE RPT EVALUATION"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"OUTSOURCE RPT COST PLAN"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"OUTSOURCE RPT SUMMARY"
                {COMMAND_VIEW, COMMAND_PRINT},
                //"RPT IN OUT"
                {COMMAND_VIEW, COMMAND_PRINT},
                //OBJ_OUTSOURCE_RPT_COST_REAL
                {COMMAND_VIEW, COMMAND_PRINT}
            }
        },
        /* ADD BY HENDRA PUTU - 2015-12-10 */
        { /* 21 PERINGATAN DAN TEGURAN*/
            {
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            },
            {
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_UPDATE, COMMAND_DELETE}
            }
        },
        { /* 22 MENU PERINGATAN DAN TEGURAN*/
            {
                {COMMAND_VIEW},
                {COMMAND_VIEW}
            },
            {
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW}
            }
        },
        /* [23] MENU PELATIHAN */
        {
            /* [0]  "MENU TIPE PELATIHAN", */
            {{COMMAND_VIEW}},
            /* [1]  "MENU LOKASI PELATIHAN", */
            {{COMMAND_VIEW}},
            /* [2]  "MENU PROGRAM PELATIHAN", */
            {{COMMAND_VIEW}},
            /* [3]  "MENU RENCANA PELATIHAN", */
            {{COMMAND_VIEW}},
            /* [4]  "MENU PELATIHAN REAL", */
            {{COMMAND_VIEW}},
            /* [5]  "MENU PENCARIAN PELATIHAN", */
            {{COMMAND_VIEW}},
            /* [6]  "MENU SEJARAH PELATIHAN", */
            {{COMMAND_VIEW}},
            /* [7]  "MENU GAP TRAINING", */
            {{COMMAND_VIEW}},
            /* [8]  "MENU TRAINING ORGANIZER", */
            {{COMMAND_VIEW}},
            /* [9]  "MENU ORGANIZER TYPE", */
            {{COMMAND_VIEW}},
            /* [10]  "MENU TRAINING SCORE" */
            {{COMMAND_VIEW}}
        },
        /* [24] MENU ORGANISASI */
        {
            /* [0] "MENU STRUKTUR ORGANISASI" */
            {{COMMAND_VIEW}},
            /* [1] "MENU CETAK UNIT KERJA" */
            {{COMMAND_VIEW}},
            /* [2] "MENU SEJARAH JABATAN" */
            {{COMMAND_VIEW}},
            /* [3] "MENU POSISI DAN JOB DESC" */
            {{COMMAND_VIEW}},
            /* [4] "MENU INFORMASI PEJABAT" */
            {{COMMAND_VIEW}},
            /* [5] "MENU STRUKTUR PERUSAHAAN" */
            {
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW},
                {COMMAND_VIEW}
            },
            /* [6] "MENU STRUKTUR TEMPLATE" */
            {{COMMAND_VIEW}}
        },
        /* [25] DOKUMEN SURAT */
        {
            {
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE},
                {COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}
            },
            {{COMMAND_VIEW, COMMAND_ADD, COMMAND_DELETE, COMMAND_UPDATE}}
        }
    };
    

    public static String getStrCommand(int command){
        if((command<0) || (command > strCommand.length) ){
            System.out.println(" ERR: getStrCommand - commmand out of range");
            return "";
        }
        return strCommand[command];
        
    }
    
    public static boolean existObject(int g1, int g2, int objIdx){
        if( (g1<0) || (g1> titleG1.length)){
            System.out.println(" ERR: composeCode g1 out of range");
            return false;
        }
             
        if((g2<0) || (g2 > (titleG2[g1]).length))  {
            System.out.println(" ERR: composeCode g2 out of range | existObject("+g1+", "+g2+", "+objIdx+")");
            return false;
        }
        
        if((objIdx<0) || (objIdx> (objectTitles[g1][g2]).length)){
            System.out.println(" ERR: composeCode objIdx out of range");
            return false;
        }
        
        return true;        
    }
    
    public static int composeCode(int g1, int g2, int objIdx, int command) {        
        if(!existObject(g1,g2, objIdx))
            return -1;
        
        if((command<0) || (command > strCommand.length)){ 
            System.out.println(" ERR: composeCode commmand out of range");
            return -1;
        }
        
        if(!privExistCommand(g1,g2, objIdx, command)){
            System.out.println(" ERR: composeCode commmand out not exist on object "+ 
            getTitleGroup1(g1)+"-"+getTitleGroup2(g2)+"-"+getTitleObject(objIdx));
            return -1;
        }
        
        return (g1 << SHIFT_CODE_G1) + (g2 << SHIFT_CODE_G2) + (objIdx << SHIFT_CODE_OBJ ) + command;
    }

    public static int composeCode(int objCode, int command) {        
        if((command<0) || (command > strCommand.length) ){
            System.out.println(" ERR: composeCode commmand out of range");
            return -1;
        }
        //System.out.println("objCode + command ="+objCode + command);
        return objCode + command;
    }
    
    public static int composeObjCode(int g1, int g2, int objIdx) {        
        if(!existObject(g1,g2, objIdx))
            return -1;
                
        return (g1 << SHIFT_CODE_G1) + (g2 << SHIFT_CODE_G2) + (objIdx << SHIFT_CODE_OBJ );
    }
    
    public static int composeObjCode(int g1, int g2) {
        if ((g1 < 0) || (g1 > titleG1.length)) {
            System.out.println(" ERR: composeCode g1 out of range");
            return -1;
        }

        if ((g2 < 0) || (g2 > (titleG2[g1]).length)) {
            System.out.println(" ERR: composeCode g2 out of range composeObjCode ("+g1+","+g2+") ");
            return -1;
        }

        return (g1 << SHIFT_CODE_G1) + (g2 << SHIFT_CODE_G2) ;
    }

  public static int composeObjCode(int g1) {
        if ((g1 < 0) || (g1 > titleG1.length)) {
            System.out.println(" ERR: composeCode g1 out of range");
            return -1;
        }

        return (g1 << SHIFT_CODE_G1) ;
    }
    
    private static boolean privExistCommand(int g1, int g2, int objIdx, int command){
        for(int i=0; i< objectCommands[g1][g2][objIdx].length;i++){
            if(objectCommands[g1][g2][objIdx][i]==command)
                return true;            
        }
        return false;
    }

    public static boolean existCommand(int g1, int g2, int objIdx, int command){
        if(!existObject(g1,g2, objIdx))
            return false;
        
        return privExistCommand(g1,g2, objIdx, command);
    }
    
    public static int getG1G2ObjIdx(int code){
        return (code & (FILTER_CODE_G1 + FILTER_CODE_G2 + FILTER_CODE_OBJ));
    }

    public static int getCommand(int code){
        return (code & FILTER_CODE_CMD);
    }
    
    public static int getIdxGroup1(int code){
        int g1 = (code & FILTER_CODE_G1) >> SHIFT_CODE_G1;
        if( (g1<0) || (g1> titleG1.length)){
            System.out.println(" ERR: getIdxGroup1 g1 on code out of range");
            return -1;
        }
        return g1;        
    }

    public static String getTitleGroup1(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return "";

        return titleG1[g1];
    }

    public static int getIdxGroup2(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return -1;
        
        int g2 = (code & FILTER_CODE_G2) >> SHIFT_CODE_G2;
        if( (g2<0) || (g2> titleG2[g1].length)){
            System.out.println(" ERR: getIdxGroup2 g2 on code out of range");
            return -1;
        }
        return g2;        
    }

    public static String getTitleGroup2(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return "";
        
        int g2 = getIdxGroup2(code);
        if(g2<0)
            return "";

        return titleG2[g1][g2];
    }

    public static int getIdxObject(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return -1;
        
        int g2 = getIdxGroup2(code);
        if(g2<0)
            return -1;
        
        int oidx = (code & FILTER_CODE_OBJ) >> SHIFT_CODE_OBJ;
        if( (oidx<0) || (oidx> objectTitles[g1][g2].length)){
            System.out.println(" ERR: getIdxObject, oidx on code out of range");
            return -1;
        }
        return oidx;        
    }

    public static String getTitleObject(int code){
        int g1 = getIdxGroup1(code);
        if(g1<0)
            return "";
        
        int g2 = getIdxGroup2(code);
        if(g2<0)
            return "";
        
        int oidx = getIdxObject(code);
        if(oidx<0)
            return "";
        
        return objectTitles[g1][g2][oidx];
    }
    
    /*
     * parse privobj code into title/string of g1, g2, objidx and command
     * return Vector of String: 0=g1, 1=g, 2=objIdx, 3=command, 4=Integer error code (0=false, -1=falses), 
     * 
     */
    public static Vector parseStringCode(int code){
        Vector titleCodes= new Vector(4,1);
        titleCodes.add(new String(""));
        titleCodes.add(new String(""));
        titleCodes.add(new String(""));
        titleCodes.add(new String(""));
        titleCodes.add(new Integer(0));
        
        int g1 = getIdxGroup1(code);
        if(g1<0){
            titleCodes.set(0, "Invalid G1 Idx");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(0, titleG1[g1]);

        int g2 = getIdxGroup2(code);
        if(g2<0){
            titleCodes.set(1, "Invalid G2 Idx");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(1, titleG2[g1][g2]);
        
        int oidx = getIdxObject(code);
        if(oidx<0){
            titleCodes.set(2, "Invalid Obj. Idx");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(2, objectTitles[g1][g2][oidx]);
        
        int cmd = getCommand(code);
        if(cmd<0){
            titleCodes.set(3, "Invalid Command");
            titleCodes.set(4, new Integer(-1));
            return titleCodes;
        }
        titleCodes.set(3, strCommand[cmd]);
                
        return titleCodes;
    }


    public static void main (String [] args){
    	for(int  i= 0;i <AppObjInfo.objectTitles[2][4].length;i++){
        	System.out.println("content "+  AppObjInfo.objectTitles[2][4][i]);
    	}
    }
    
}
