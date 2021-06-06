/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Vector;

/**
 *
 * @author Gunadi
 */
public class PstThrRptSetup extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_THR_RPT_SETUP = "pay_thr_report_setup";
    public static final int FLD_RPT_SETUP_ID = 0;
    public static final int FLD_RPT_SETUP_SHOW_IDX = 1;
    public static final int FLD_RPT_SETUP_TABLE_GROUP = 2;
    public static final int FLD_RPT_SETUP_TABLE_NAME = 3;
    public static final int FLD_RPT_SETUP_FIELD_NAME = 4;
    public static final int FLD_RPT_SETUP_FIELD_TYPE = 5;
    public static final int FLD_RPT_SETUP_FIELD_HEADER = 6;
    public static final int FLD_RPT_SETUP_DATA_TYPE = 7;
    public static final int FLD_RPT_SETUP_DATA_GROUP = 8;
    public static String[] fieldNames = {
        "RPT_SETUP_ID",
        "RPT_SETUP_SHOW_IDX",
        "RPT_SETUP_TABLE_GROUP",
        "RPT_SETUP_TABLE_NAME",
        "RPT_SETUP_FIELD_NAME",
        "RPT_SETUP_FIELD_TYPE",
        "RPT_SETUP_FIELD_HEADER",
        "RPT_SETUP_DATA_TYPE",
        "RPT_SETUP_DATA_GROUP"
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT
    };
    
    public static String[] showTableList = {
        "Employee Databank",
        "Payment",
        "Payment Type"
    };
    
    public static String[] realTableList = {
        "hr_employee",
        "pay_slip",
        "pay_emp_level"
    };
    public static String[][] showFieldList = {
        {
            "Payroll Number",
            "Full Name",
            "Company",
            "Division",
            "Department",
            "Section",
            "Position",
            "Level",
            "Marital Status",
            "Date of Birth",
            "Commencing Date",
            "Tax Status",
            "Bank Account",
            "NPWP",
            "Employee Category",
            "Resigned",
            "Resigned Reason",
            "Resigned Date",
            "BPJS Number",
            "BPJS Date",
            "Jamsostek Number",
            "Jamsostek Date",
            /* Update by Hendra | 2015-11-04 */
            "ID Card Number",
            "ID Card Type",
            /* Update By Hendra | 2016-03-01 */
            "Address",
            "Phone",
            "Address Permanent",
            "Phone Emergency",
            "Hand Phone",
            "Postal Code",
            "Sex",
            "Birth Place",
            "Probation Until",
            "Religion", /*RELIGION_ID hr_religion*/
            "Blood Type",
            "Resigned Description",
            "Barcode Number",
            "Valid ID Card",
            "Email Address",
            "Emergency Name",
            "Kecamatan 1",
            "Kabupaten 1",
            "Propinsi 1",
            "Kecamatan 2",
            "Kabupaten 2",
            "Propinsi 2",
            "Father",
            "Mother",
            "Parents Address",
            "Phone Emergency",
            "Address Emergency",
            "Grade Level", /* GRADE_LEVEL_ID */
            "End Contract",
            "Shio",
            "Element",
            "IQ",
            "EQ",
            "Probation End Date",
            "Status Pension Program",
            "Start Date Pension",
            "Member of Kesehatan",
            "Member of Ketenagakerjaan",
            "Payroll Group"
        },
        {
            "Period",
            "Day Present",
            "Day Absent",
            "Pay Slip ID"
        },
        {
            "Bank Account",
            "BANK or CASH",
            "Status Data"
        }
    };
    public static String[][] showFieldSystem = {
        {
            "EMPLOYEE_NUM",
            "FULL_NAME",
            "COMPANY",
            "DIVISION",
            "DEPARTMENT",
            "SECTION",
            "POSITION",
            "LEVEL",
            "MARITAL_STATUS",
            "BIRTH_DATE",
            "COMMENCING_DATE",
            "MARITAL_STATUS_TAX",
            "NO_REKENING",
            "NPWP",
            "EMP_CATEGORY",
            "RESIGNED",
            "RESIGNED_REASON",
            "RESIGNED_DATE",
            "BPJS_NO",
            "BPJS_DATE",
            "ASTEK_NUM",
            "ASTEK_DATE",
            /* Update by Hendra | 2015-11-04 */
            "INDENT_CARD_NR",
            "ID_CARD_TYPE",
            /* Update By Hendra | 2016-03-01 */
            "ADDRESS",
            "phone",
            "ADDRESS_PERMANENT",
            "phone_emergency",
            "HANDPHONE",
            "POSTAL_CODE",
            "SEX",
            "BIRTH_PLACE",
            "PROBATION_UNTIL",
            "RELIGION",
            "BLOOD_TYPE",
            "RESIGNED_DESC",
            "BARCODE_NUMBER",
            "INDENT_CARD_VALID_TO",
            "EMAIL_ADDRESS",
            "EMERGENCY_NAME",
            "KECAMATAN1",
            "KABUPATEN1",
            "PROPINSI1",
            "KECAMATAN2",
            "KABUPATEN2",
            "PROPINSI2",
            "FATHER",
            "MOTHER",
            "PARENTS_ADDRESS",
            "PHONE_EMG",
            "ADDRESS_EMG",
            "GRADE_CODE",
            "END_CONTRACT",
            "SHIO",
            "ELEMEN",
            "IQ",
            "EQ",
            "PROBATION_END_DATE",
            "STATUS_PENSIUN_PROGRAM",
            "START_DATE_PENSIUN",
            "MEMBER_OF_KESEHATAN",
            "MEMBER_OF_KETENAGAKERJAAN",
            "PAYROLL_GROUP_NAME"
        },
        {
            "PERIOD",
            "DAY_PRESENT",
            "DAY_ABSENT",
            "PAY_SLIP_ID"
        },
        {
            "BANK_ACC_NR",
            "BANK_ID",
            "STATUS_DATA"
        }
    };
    
    public static String[][] tableSystem = {
        {
            "hr_employee",
            "hr_employee",
            "pay_general",
            "hr_division",
            "hr_department",
            "hr_section",
            "hr_position",
            "hr_level",
            "hr_marital",
            "hr_employee",
            "hr_employee",
            "hr_marital",
            "hr_employee",
            "hr_employee",
            "hr_emp_category",
            "hr_employee",
            "hr_resigned_reason",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            /* Update by Hendra | 2015-11-04 */
            "hr_employee",
            "hr_employee",
            /* Update By Hendra | 2016-03-01 */
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_religion",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_kecamatan",
            "hr_kabupaten",
            "hr_propinsi",
            "hr_kecamatan",
            "hr_kabupaten",
            "hr_propinsi",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_grade_level",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "payroll_group"
        },
        {
            "pay_period",
            "pay_slip",
            "pay_slip",
            "pay_slip"
        },
        {
            "pay_emp_level",
            "pay_emp_level",
            "pay_emp_level"
        }
    };
    
    
    
    public static String[][] tableAsSystem = {
        {
            "hr_employee",
            "hr_employee",
            "pay_general",
            "hr_division",
            "hr_department",
            "hr_section",
            "hr_position",
            "hr_level",
            "hr_marital",
            "hr_employee",
            "hr_employee",
            "hr_marital_tax",
            "hr_employee",
            "hr_employee",
            "hr_emp_category",
            "hr_employee",
            "hr_resigned_reason",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            /* Update by Hendra | 2015-11-04 */
            "hr_employee",
            "hr_employee",
            /* Update By Hendra | 2016-03-01 */
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_religion",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_kecamatan",
            "hr_kabupaten",
            "hr_propinsi",
            "hr_kecamatan",
            "hr_kabupaten",
            "hr_propinsi",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_grade_level",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "hr_employee",
            "payroll_group"
        },
        {
            "pay_period",
            "pay_slip",
            "pay_slip",
            "pay_slip"
        },
        {
            "pay_emp_level",
            "pay_emp_level",
            "pay_emp_level"
        }
    };
    /* 0 = Int, 1 = String, 2 = Long, 3 = Date, 4 = Double*/
    public static int[][] fieldTypeSystem = {
        {
            1,//"EMPLOYEE_NUM",
            1,//"FULL_NAME",
            1,//"COMPANY",
            1,//"DIVISION",
            1,//"DEPARTMENT",
            1,//"SECTION",
            1,//"POSITION",
            1,//"LEVEL",
            1,//"MARITAL_STATUS",
            1,//"BIRTH_DATE",
            1,//"COMMENCING_DATE",
            1,//"MARITAL_STATUS_TAX",
            1,//"NO_REKENING",
            1,//"NPWP",
            1,//"EMP_CATEGORY",
            0,//"RESIGNED",
            1,//"RESIGNED_REASON",
            1,//"RESIGNED_DATE"
            1,//"BPJS_NO",
            1,//"BPJS_DATE",
            1,//"ASTEK_NUM",
            3,//"ASTEK_DATE"
            /* Update by Hendra | 2015-11-04 */
            1,/*"INDENT_CARD_NR",*/
            1, /*"ID_CARD_TYPE"*/
            /* Update By Hendra | 2016-03-01 */
            1,//"ADDRESS",
            1,//"phone",
            1,//"ADDRESS_PERMANENT",
            1,//"phone_emergency",
            1,//"HANDPHONE",
            1,//"POSTAL_CODE",
            0,//"SEX",
            1,//"BIRTH_PLACE",
            1,//"PROBATION_UNTIL",
            1,//"RELIGION",
            1,//"BLOOD_TYPE",
            1,//"RESIGNED_DESC",
            1,//"BARCODE_NUMBER",
            1,//"INDENT_CARD_VALID_TO",
            1,//"EMAIL_ADDRESS",
            1,//"EMERGENCY_NAME",
            1,//"KECAMATAN1",
            1,//"KABUPATEN1",
            1,//"PROPINSI1",
            1,//"KECAMATAN2",
            1,//"KABUPATEN2",
            1,//"PROPINSI2",
            1,//"FATHER",
            1,//"MOTHER",
            1,//"PARENTS_ADDRESS",
            1,//"PHONE_EMG",
            1,//"ADDRESS_EMG",
            1,//"GRADE_CODE",
            1,//"END_CONTRACT",
            1,//"SHIO",
            1,//"ELEMEN",
            1,//"IQ",
            1,//"EQ",
            1,//"PROBATION_END_DATE",
            0,//"STATUS_PENSIUN_PROGRAM",
            1,//"START_DATE_PENSIUN",
            0,//"MEMBER_OF_KESEHATAN",
            0,//"MEMBER_OF_KETENAGAKERJAAN"
            1//"PAYROLL_GROUP"
        },
        {
            1,//"PERIOD",
            4,//"DAY_PRESENT",
            4,//"DAY_ABSENT"
            2//PAY_SLIP_ID
        },
        {
            1,//"BANK_ACC_NR"
            2,//"BANK_ID"
            0//"STATUS_DATA"
        }
    };

    public static String[] joinData = {
        "INNER JOIN pay_general AS pay_general ON hr_employee.COMPANY_ID=pay_general.GEN_ID",
        "INNER JOIN hr_division AS hr_division ON hr_employee.DIVISION_ID=hr_division.DIVISION_ID",
        "INNER JOIN hr_department AS hr_department ON hr_employee.DEPARTMENT_ID=hr_department.DEPARTMENT_ID",
        "INNER JOIN hr_section AS hr_section ON hr_employee.SECTION_ID=hr_section.SECTION_ID",
        "INNER JOIN hr_position AS hr_position ON hr_employee.POSITION_ID=hr_position.POSITION_ID",
        "INNER JOIN hr_resigned_reason AS hr_resigned_reason ON hr_employee.RESIGNED_REASON_ID=hr_resigned_reason.RESIGNED_REASON_ID",
        "INNER JOIN hr_level AS hr_level ON hr_employee.LEVEL_ID=hr_level.LEVEL_ID",
        "INNER JOIN hr_marital AS hr_marital ON hr_employee.MARITAL_ID=hr_marital.MARITAL_ID",
        "INNER JOIN hr_emp_category AS hr_emp_category ON hr_employee.EMP_CATEGORY_ID=hr_emp_category.EMP_CATEGORY_ID",
        "INNER JOIN pay_slip AS pay_slip ON hr_employee.EMPLOYEE_ID=pay_slip.EMPLOYEE_ID",
        "INNER JOIN pay_emp_level AS pay_emp_level ON hr_employee.EMPLOYEE_ID=pay_emp_level.EMPLOYEE_ID",
        "INNER JOIN pay_slip_comp AS pay_slip_comp ON pay_slip.PAY_SLIP_ID=pay_slip_comp.PAY_SLIP_ID",
        "INNER JOIN pay_period AS pay_period ON pay_slip.PERIOD_ID=pay_period.PERIOD_ID",
        "INNER JOIN hr_religion AS hr_religion ON hr_employee.RELIGION_ID=hr_religion.RELIGION_ID",
        "INNER JOIN hr_marital AS hr_marital_tax ON hr_employee.TAX_MARITAL_ID=hr_marital_tax.MARITAL_ID",
        "INNER JOIN payroll_group AS payroll_group ON hr_employee.PAYROLL_GROUP=payroll_group.PAYROLL_GROUP_ID",
    };
    
    
//    public static String[] joinData = {
//        "INNER JOIN pay_general ON hr_employee.COMPANY_ID=pay_general.GEN_ID",
//        "INNER JOIN hr_division ON hr_employee.DIVISION_ID=hr_division.DIVISION_ID",
//        "INNER JOIN hr_department ON hr_employee.DEPARTMENT_ID=hr_department.DEPARTMENT_ID",
//        "INNER JOIN hr_section ON hr_employee.SECTION_ID=hr_section.SECTION_ID",
//        "INNER JOIN hr_position ON hr_employee.POSITION_ID=hr_position.POSITION_ID",
//        "INNER JOIN hr_resigned_reason ON hr_employee.RESIGNED_REASON_ID=hr_resigned_reason.RESIGNED_REASON_ID",
//        "INNER JOIN hr_level ON hr_employee.LEVEL_ID=hr_level.LEVEL_ID",
//        "INNER JOIN hr_marital ON hr_employee.MARITAL_ID=hr_marital.MARITAL_ID",
//        "INNER JOIN hr_emp_category ON hr_employee.EMP_CATEGORY_ID=hr_emp_category.EMP_CATEGORY_ID",
//        "INNER JOIN pay_slip ON hr_employee.EMPLOYEE_ID=pay_slip.EMPLOYEE_ID",
//        "INNER JOIN pay_emp_level ON hr_employee.EMPLOYEE_ID=pay_emp_level.EMPLOYEE_ID",
//        "INNER JOIN pay_slip_comp ON pay_slip.PAY_SLIP_ID=pay_slip_comp.PAY_SLIP_ID",
//        "INNER JOIN pay_period ON pay_slip.PERIOD_ID=pay_period.PERIOD_ID",
//        "INNER JOIN hr_religion ON hr_employee.RELIGION_ID=hr_religion.RELIGION_ID",
//        "INNER JOIN hr_marital ON hr_employee.TAX_MARITAL_ID=hr_marital.MARITAL_ID"
//    };
    
    public static int[] joinDataPriority = {
        0,//"INNER JOIN pay_general ON hr_employee.COMPANY_ID=pay_general.GEN_ID",
        1,//"INNER JOIN hr_division ON hr_employee.DIVISION_ID=hr_division.DIVISION_ID",
        2,//"INNER JOIN hr_department ON hr_employee.DEPARTMENT_ID=hr_department.DEPARTMENT_ID",
        3,//"INNER JOIN hr_section ON hr_employee.SECTION_ID=hr_section.SECTION_ID",
        4,//"INNER JOIN hr_position ON hr_employee.POSITION_ID=hr_position.POSITION_ID",
        5,//"INNER JOIN hr_resigned_reason ON hr_employee.RESIGNED_REASON_ID=hr_resigned_reason.RESIGNED_REASON_ID",
        6,//"INNER JOIN hr_level ON hr_employee.LEVEL_ID=hr_level.LEVEL_ID",
        7,//"INNER JOIN hr_marital ON hr_employee.MARITAL_ID=hr_marital.MARITAL_ID",
        8,//"INNER JOIN hr_emp_category ON hr_employee.EMP_CATEGORY_ID=hr_emp_category.EMP_CATEGORY_ID",
        9,//"INNER JOIN pay_slip ON hr_employee.EMPLOYEE_ID=pay_slip.EMPLOYEE_ID",
        10,//"INNER JOIN pay_emp_level ON hr_employee.EMPLOYEE_ID=pay_emp_level.EMPLOYEE_ID",
        11,//"INNER JOIN pay_slip_comp ON pay_slip.PAY_SLIP_ID=pay_slip_comp.PAY_SLIP_ID",
        12,//"INNER JOIN pay_period ON pay_slip.PERIOD_ID=pay_period.PERIOD_ID"
        13,//"INNER JOIN hr_religion ON hr_employee.RELIGION_ID=hr_religion.RELIGION_ID"
        14,//"INNER JOIN hr_marital ON hr_employee.TAX_MARITAL_ID=hr_marital.MARITAL_ID",
        15//"INNER JOIN payroll_group AS payroll_group ON hr_employee.PAYROLL_GROUP_ID=payroll_group.PAYROLL_GROUP_ID",
    };

    public PstThrRptSetup() {
    }

    public PstThrRptSetup(int i) throws DBException {
        super(new PstThrRptSetup());
    }

    public PstThrRptSetup(String sOid) throws DBException {
        super(new PstThrRptSetup(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstThrRptSetup(long lOid) throws DBException {
        super(new PstThrRptSetup(0));
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
        return TBL_THR_RPT_SETUP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstThrRptSetup().getClass().getName();
    }

    public static ThrRptSetup fetchExc(long oid) throws DBException {
        try {
            ThrRptSetup entThrRptSetup = new ThrRptSetup();
            PstThrRptSetup pstThrRptSetup = new PstThrRptSetup(oid);
            entThrRptSetup.setOID(oid);
            entThrRptSetup.setRptSetupShowIdx(pstThrRptSetup.getInt(FLD_RPT_SETUP_SHOW_IDX));
            entThrRptSetup.setRptSetupTableGroup(pstThrRptSetup.getString(FLD_RPT_SETUP_TABLE_GROUP));
            entThrRptSetup.setRptSetupTableName(pstThrRptSetup.getString(FLD_RPT_SETUP_TABLE_NAME));
            entThrRptSetup.setRptSetupFieldName(pstThrRptSetup.getString(FLD_RPT_SETUP_FIELD_NAME));
            entThrRptSetup.setRptSetupFieldType(pstThrRptSetup.getInt(FLD_RPT_SETUP_FIELD_TYPE));
            entThrRptSetup.setRptSetupFieldHeader(pstThrRptSetup.getString(FLD_RPT_SETUP_FIELD_HEADER));
            entThrRptSetup.setRptSetupDataType(pstThrRptSetup.getInt(FLD_RPT_SETUP_DATA_TYPE));
            entThrRptSetup.setRptSetupDataGroup(pstThrRptSetup.getInt(FLD_RPT_SETUP_DATA_GROUP));
            return entThrRptSetup;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrRptSetup(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ThrRptSetup entThrRptSetup = fetchExc(entity.getOID());
        entity = (Entity) entThrRptSetup;
        return entThrRptSetup.getOID();
    }

    public static synchronized long updateExc(ThrRptSetup entThrRptSetup) throws DBException {
        try {
            if (entThrRptSetup.getOID() != 0) {
                PstThrRptSetup pstThrRptSetup = new PstThrRptSetup(entThrRptSetup.getOID());
                pstThrRptSetup.setInt(FLD_RPT_SETUP_SHOW_IDX, entThrRptSetup.getRptSetupShowIdx());
                pstThrRptSetup.setString(FLD_RPT_SETUP_TABLE_GROUP, entThrRptSetup.getRptSetupTableGroup());
                pstThrRptSetup.setString(FLD_RPT_SETUP_TABLE_NAME, entThrRptSetup.getRptSetupTableName());
                pstThrRptSetup.setString(FLD_RPT_SETUP_FIELD_NAME, entThrRptSetup.getRptSetupFieldName());
                pstThrRptSetup.setInt(FLD_RPT_SETUP_FIELD_TYPE, entThrRptSetup.getRptSetupFieldType());
                pstThrRptSetup.setString(FLD_RPT_SETUP_FIELD_HEADER, entThrRptSetup.getRptSetupFieldHeader());
                pstThrRptSetup.setInt(FLD_RPT_SETUP_DATA_TYPE, entThrRptSetup.getRptSetupDataType());
                pstThrRptSetup.setInt(FLD_RPT_SETUP_DATA_GROUP, entThrRptSetup.getRptSetupDataGroup());
                pstThrRptSetup.update();
                return entThrRptSetup.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrRptSetup(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ThrRptSetup) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstThrRptSetup pstThrRptSetup = new PstThrRptSetup(oid);
            pstThrRptSetup.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrRptSetup(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ThrRptSetup entThrRptSetup) throws DBException {
        try {
            PstThrRptSetup pstThrRptSetup = new PstThrRptSetup(0);
            pstThrRptSetup.setInt(FLD_RPT_SETUP_SHOW_IDX, entThrRptSetup.getRptSetupShowIdx());
            pstThrRptSetup.setString(FLD_RPT_SETUP_TABLE_GROUP, entThrRptSetup.getRptSetupTableGroup());
            pstThrRptSetup.setString(FLD_RPT_SETUP_TABLE_NAME, entThrRptSetup.getRptSetupTableName());
            pstThrRptSetup.setString(FLD_RPT_SETUP_FIELD_NAME, entThrRptSetup.getRptSetupFieldName());
            pstThrRptSetup.setInt(FLD_RPT_SETUP_FIELD_TYPE, entThrRptSetup.getRptSetupFieldType());
            pstThrRptSetup.setString(FLD_RPT_SETUP_FIELD_HEADER, entThrRptSetup.getRptSetupFieldHeader());
            pstThrRptSetup.setInt(FLD_RPT_SETUP_DATA_TYPE, entThrRptSetup.getRptSetupDataType());
            pstThrRptSetup.setInt(FLD_RPT_SETUP_DATA_GROUP, entThrRptSetup.getRptSetupDataGroup());
            pstThrRptSetup.insert();
            entThrRptSetup.setOID(pstThrRptSetup.getLong(FLD_RPT_SETUP_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrRptSetup(0), DBException.UNKNOWN);
        }
        return entThrRptSetup.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ThrRptSetup) entity);
    }

    public static void resultToObject(ResultSet rs, ThrRptSetup entThrRptSetup) {
        try {
            entThrRptSetup.setOID(rs.getLong(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_ID]));
            entThrRptSetup.setRptSetupShowIdx(rs.getInt(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_SHOW_IDX]));
            entThrRptSetup.setRptSetupTableGroup(rs.getString(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_TABLE_GROUP]));
            entThrRptSetup.setRptSetupTableName(rs.getString(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_TABLE_NAME]));
            entThrRptSetup.setRptSetupFieldName(rs.getString(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_FIELD_NAME]));
            entThrRptSetup.setRptSetupFieldType(rs.getInt(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_FIELD_TYPE]));
            entThrRptSetup.setRptSetupFieldHeader(rs.getString(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_FIELD_HEADER]));
            entThrRptSetup.setRptSetupDataType(rs.getInt(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_DATA_TYPE]));
            entThrRptSetup.setRptSetupDataGroup(rs.getInt(PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_DATA_GROUP]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_THR_RPT_SETUP;
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
            while (rs.next()) {
                ThrRptSetup entThrRptSetup = new ThrRptSetup();
                resultToObject(rs, entThrRptSetup);
                lists.add(entThrRptSetup);
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
    
    public static int getIdx(){
        DBResultSet dbrs = null;
        int idx = 0;
        try {
            String sql = "SELECT " + fieldNames[FLD_RPT_SETUP_SHOW_IDX] + " FROM " + TBL_THR_RPT_SETUP + " ORDER BY "
                    + PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_SHOW_IDX] + " DESC LIMIT 1 ";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                idx = rs.getInt(fieldNames[FLD_RPT_SETUP_SHOW_IDX]) + 1;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return idx;
        }
    }

    public static boolean checkOID(long entThrRptSetupId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_THR_RPT_SETUP + " WHERE "
                    + PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_ID] + " = " + entThrRptSetupId;
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
            String sql = "SELECT COUNT(" + PstThrRptSetup.fieldNames[PstThrRptSetup.FLD_RPT_SETUP_ID] + ") FROM " + TBL_THR_RPT_SETUP;
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

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    ThrRptSetup entThrRptSetup = (ThrRptSetup) list.get(ls);
                    if (oid == entThrRptSetup.getOID()) {
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
        vectSize = vectSize + (recordToGet - mdl);
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
}
