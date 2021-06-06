/*
 * PstPayEmpLevel.java
 *
 * Created on April 5, 2007, 11:07 AM
 */
package com.dimata.harisma.entity.payroll;
/* package java */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;

/* package harisma */
//import com.dimata. harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.payroll.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstLevel;
import com.dimata.harisma.entity.masterdata.PstPosition;
import com.dimata.harisma.form.payroll.CtrlPayEmpLevel;
import com.dimata.system.entity.PstSystemProperty;
//import com.dimata.harisma.entity.locker.*;

/**
 *
 * @author yunny
 */
public class PstPayEmpLevel extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PAY_EMP_LEVEL = "pay_emp_level";//"PAY_EMP_LEVEL";
    public static final int FLD_PAY_EMP_LEVEL_ID = 0;
    public static final int FLD_EMPLOYEE_ID = 1;
    public static final int FLD_LEVEL_CODE = 2;
    public static final int FLD_START_DATE = 3;
    public static final int FLD_BANK_ID = 4;
    public static final int FLD_BANK_ACC_NR = 5;
    public static final int FLD_POS_FOR_TAX = 6;
    public static final int FLD_PAY_PER_BEGIN = 7;
    public static final int FLD_PAY_PER_END = 8;
    public static final int FLD_COMMENCING_ST = 9;
    public static final int FLD_PREV_INCOME = 10;
    public static final int FLD_PREV_TAX_PAID = 11;
    public static final int FLD_STATUS_DATA = 12;
    public static final int FLD_MEAL_ALLOWANCE = 13;
    public static final int FLD_OVT_IDX_TYPE = 14;
    public static final int FLD_END_DATE = 15;
    public static final String[] fieldNames = {
        "PAY_EMP_LEVEL_ID",
        "EMPLOYEE_ID",
        "LEVEL_CODE",
        "START_DATE",
        "BANK_ID",
        "BANK_ACC_NR",
        "POS_FOR_TAX",
        "PAY_PER_BEGIN",
        "PAY_PER_END",
        "COMMENCING_ST",
        "PREV_INCOME",
        "PREV_TAX_PAID",
        "STATUS_DATA",
        "MEAL_ALLOWANCE",
        "OVT_IDX_TYPE",
        "END_DATE"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE
    };
    //status commencing 
    public static int KARYAWAN_LAMA = 0;
    public static int KARYAWAN_BARU = 1;
    public static int PINDAHAN = 2;
    public static int EXPATRIAT = 3;
    public static String[] stComm = {
        "Karyawan Lama", "Karyawan Baru", "Pindahan", "Expatriat"
    };
    //status Data
    public static int CURRENT = 0;
    public static int HISTORY = 1;
    public static String[] stData = {
        "Current", "History"
    };
    //meal allowance
    public static int WEEKLY = 0;
    public static int MONTLY = 1;
    public static String[] stAllowance = {
        "Weekly", "Monthly"
    };
    // index overtime
    public static int WITH_INDEX = 0;
    public static int NO_INDEX = 1;
    public static String[] stOvtIndex = {
        "With Index", "Without Index"
    };
    // transfered
    public static int TRANSFERED_YES = 0;
    public static int TRANSFERED_NO = 1;
    public static String[] stTransfered = {
        "Yes", "No"
    };

    /**
     * Creates a new instance of PstPayEmpLevel
     */
    public PstPayEmpLevel() {
    }

    public PstPayEmpLevel(int i) throws DBException {
        super(new PstPayEmpLevel());
    }

    public PstPayEmpLevel(String sOid) throws DBException {
        super(new PstPayEmpLevel(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPayEmpLevel(long lOid) throws DBException {
        super(new PstPayEmpLevel(0));
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

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public long fetchExc(Entity ent) throws Exception {
        PayEmpLevel payEmpLevel = fetchExc(ent.getOID());
        ent = (Entity) payEmpLevel;
        return payEmpLevel.getOID();
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPayEmpLevel().getClass().getName();
    }

    public String getTableName() {
        return TBL_PAY_EMP_LEVEL;
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((PayExecutive) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((PayExecutive) ent);
    }

    public static PayEmpLevel fetchExc(long oid) throws DBException {
        try {
            PayEmpLevel payEmpLevel = new PayEmpLevel();
            PstPayEmpLevel pstPayEmpLevel = new PstPayEmpLevel(oid);
            payEmpLevel.setOID(oid);
            payEmpLevel.setEmployeeId(pstPayEmpLevel.getlong(FLD_EMPLOYEE_ID));
            payEmpLevel.setLevelCode(pstPayEmpLevel.getString(FLD_LEVEL_CODE));
            payEmpLevel.setStartDate(pstPayEmpLevel.getDate(FLD_START_DATE));
            payEmpLevel.setEndDate(pstPayEmpLevel.getDate(FLD_END_DATE));
            payEmpLevel.setBankId(pstPayEmpLevel.getlong(FLD_BANK_ID));
            payEmpLevel.setBankAccNr(pstPayEmpLevel.getString(FLD_BANK_ACC_NR));
            payEmpLevel.setPosForTax(pstPayEmpLevel.getString(FLD_POS_FOR_TAX));
            payEmpLevel.setPayPerBegin(pstPayEmpLevel.getInt(FLD_PAY_PER_BEGIN));
            payEmpLevel.setPayPerEnd(pstPayEmpLevel.getInt(FLD_PAY_PER_END));
            payEmpLevel.setCommencingSt(pstPayEmpLevel.getInt(FLD_COMMENCING_ST));
            payEmpLevel.setPrevIncome(pstPayEmpLevel.getdouble(FLD_PREV_INCOME));
            payEmpLevel.setPrevTaxPaid(pstPayEmpLevel.getInt(FLD_PREV_TAX_PAID));
            payEmpLevel.setStatusData(pstPayEmpLevel.getInt(FLD_STATUS_DATA));
            payEmpLevel.setMealAllowance(pstPayEmpLevel.getInt(FLD_MEAL_ALLOWANCE));
            payEmpLevel.setOvtIdxType(pstPayEmpLevel.getInt(FLD_OVT_IDX_TYPE));

            return payEmpLevel;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayEmpLevel(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(PayEmpLevel payEmpLevel) throws DBException {
        try {
            PstPayEmpLevel pstPayEmpLevel = new PstPayEmpLevel(0);

            pstPayEmpLevel.setLong(FLD_EMPLOYEE_ID, payEmpLevel.getEmployeeId());
            pstPayEmpLevel.setString(FLD_LEVEL_CODE, payEmpLevel.getLevelCode());
            pstPayEmpLevel.setDate(FLD_START_DATE, payEmpLevel.getStartDate());
            pstPayEmpLevel.setDate(FLD_END_DATE, payEmpLevel.getEndDate());
            pstPayEmpLevel.setLong(FLD_BANK_ID, payEmpLevel.getBankId());
            pstPayEmpLevel.setString(FLD_BANK_ACC_NR, payEmpLevel.getBankAccNr());
            pstPayEmpLevel.setString(FLD_POS_FOR_TAX, payEmpLevel.getPosForTax());
            pstPayEmpLevel.setInt(FLD_PAY_PER_BEGIN, payEmpLevel.getPayPerBegin());
            pstPayEmpLevel.setInt(FLD_PAY_PER_END, payEmpLevel.getPayPerEnd());
            pstPayEmpLevel.setInt(FLD_COMMENCING_ST, payEmpLevel.getCommencingSt());
            pstPayEmpLevel.setDouble(FLD_PREV_INCOME, payEmpLevel.getPrevIncome());
            pstPayEmpLevel.setInt(FLD_PREV_TAX_PAID, payEmpLevel.getPrevTaxPaid());
            pstPayEmpLevel.setInt(FLD_STATUS_DATA, payEmpLevel.getStatusData());
            pstPayEmpLevel.setInt(FLD_MEAL_ALLOWANCE, payEmpLevel.getMealAllowance());
            pstPayEmpLevel.setInt(FLD_OVT_IDX_TYPE, payEmpLevel.getOvtIdxType());
            //pstPayExecutive.setString(FLD_EXECUTIVE_NAME, payExecutive.getExecutiveName());

            pstPayEmpLevel.insert();
            payEmpLevel.setOID(pstPayEmpLevel.getlong(FLD_PAY_EMP_LEVEL_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayEmpLevel(0), DBException.UNKNOWN);
        }
        return payEmpLevel.getOID();
    }

    public static long updateExc(PayEmpLevel payEmpLevel) throws DBException {
        try {
            if (payEmpLevel.getOID() != 0) {
                PstPayEmpLevel pstPayEmpLevel = new PstPayEmpLevel(payEmpLevel.getOID());
                pstPayEmpLevel.setLong(FLD_EMPLOYEE_ID, payEmpLevel.getEmployeeId());
                pstPayEmpLevel.setString(FLD_LEVEL_CODE, payEmpLevel.getLevelCode());
                pstPayEmpLevel.setDate(FLD_START_DATE, payEmpLevel.getStartDate());
                pstPayEmpLevel.setDate(FLD_END_DATE, payEmpLevel.getEndDate());
                pstPayEmpLevel.setLong(FLD_BANK_ID, payEmpLevel.getBankId());
                pstPayEmpLevel.setString(FLD_BANK_ACC_NR, payEmpLevel.getBankAccNr());
                pstPayEmpLevel.setString(FLD_POS_FOR_TAX, payEmpLevel.getPosForTax());
                pstPayEmpLevel.setInt(FLD_PAY_PER_BEGIN, payEmpLevel.getPayPerBegin());
                pstPayEmpLevel.setInt(FLD_PAY_PER_END, payEmpLevel.getPayPerEnd());
                pstPayEmpLevel.setInt(FLD_COMMENCING_ST, payEmpLevel.getCommencingSt());
                pstPayEmpLevel.setDouble(FLD_PREV_INCOME, payEmpLevel.getPrevIncome());
                pstPayEmpLevel.setInt(FLD_PREV_TAX_PAID, payEmpLevel.getPrevTaxPaid());
                pstPayEmpLevel.setInt(FLD_STATUS_DATA, payEmpLevel.getStatusData());
                pstPayEmpLevel.setInt(FLD_MEAL_ALLOWANCE, payEmpLevel.getMealAllowance());
                pstPayEmpLevel.setInt(FLD_OVT_IDX_TYPE, payEmpLevel.getOvtIdxType());
                // pstPayExecutive.setString(FLD_EXECUTIVE_NAME, payExecutive.getExecutiveName());

                pstPayEmpLevel.update();
                return payEmpLevel.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayEmpLevel(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstPayEmpLevel pstPayEmpLevel = new PstPayEmpLevel(oid);
            pstPayEmpLevel.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayEmpLevel(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 1000, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_EMP_LEVEL;
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
            //System.out.println("SQL LIST1"+sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PayEmpLevel payEmpLevel = new PayEmpLevel();
                resultToObject(rs, payEmpLevel);
                lists.add(payEmpLevel);
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

    public static void resultToObject(ResultSet rs, PayEmpLevel payEmpLevel) {
        try {
            payEmpLevel.setOID(rs.getLong(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]));
            payEmpLevel.setEmployeeId(rs.getLong(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]));
            payEmpLevel.setLevelCode(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]));
            payEmpLevel.setStartDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]));
            payEmpLevel.setEndDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]));
            payEmpLevel.setBankId(rs.getLong(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ID]));
            payEmpLevel.setBankAccNr(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR]));
            payEmpLevel.setPosForTax(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_POS_FOR_TAX]));
            payEmpLevel.setPayPerBegin(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_PER_BEGIN]));
            payEmpLevel.setPayPerEnd(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_PER_END]));
            payEmpLevel.setCommencingSt(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_COMMENCING_ST]));
            payEmpLevel.setPrevIncome(rs.getDouble(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PREV_INCOME]));
            payEmpLevel.setPrevTaxPaid(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PREV_TAX_PAID]));
            payEmpLevel.setStatusData(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]));
            payEmpLevel.setMealAllowance(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_MEAL_ALLOWANCE]));
            payEmpLevel.setOvtIdxType(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_OVT_IDX_TYPE]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long payEmpLevelId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_EMP_LEVEL + " WHERE "
                    + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID] + " = '" + payEmpLevelId + "'";

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
            String sql = "SELECT COUNT(" + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID] + ") FROM " + TBL_PAY_EMP_LEVEL;
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
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    PayEmpLevel payEmpLevel = (PayEmpLevel) list.get(ls);
                    if (oid == payEmpLevel.getOID()) {
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
    //PstPayEmpLevel.setupEmployee(Long.parseLong(employee_id[i]), s_salary_level, new Date(), Long.parseLong(bank_name[i]),s_bank_acc_nr,s_pos_for_tax,Integer.parseInt(s_pay_per_begin[i]),Integer.parseInt(s_pay_per_end[i]),statusCoba,prevIncome,Double.parseDouble(s_prev_tax_paid[i] ));

    public static void setupEmployee(long employeeId, String barcode, String start_date, long bankId, String bank_acc, String pos_for_tax, int begin, int end, String statusCoba, String prevIncome, int prevTaxPaid) {
        setupEmployee(employeeId, barcode, start_date, null, bankId, bank_acc, pos_for_tax, begin, end, statusCoba, prevIncome, prevTaxPaid);
    }

    public static void setupEmployee(long employeeId, String barcode, String start_date, String end_date,
            long bankId, String bank_acc, String pos_for_tax, int begin, int end, String statusCoba, String prevIncome, int prevTaxPaid) {
        DBResultSet dbrs = null;

        if (end_date == null || end_date.length() < 6) {
            try {
                Date theStart = new Date(start_date);
                end_date = "" + (theStart.getYear() + 1900 + 1) + "-" + (theStart.getMonth() + 1) + "-" + theStart.getDate();
            } catch (Exception exc) {
                Date theStart = new Date();
                start_date = "" + (theStart.getYear() + 1900) + "-" + (theStart.getMonth() + 1) + "-" + theStart.getDate();
                end_date = "" + (theStart.getYear() + 1900 + 1) + "-" + (theStart.getMonth() + 1) + "-" + theStart.getDate();
            }
        }

        try {
            String sql = "";
            int sec = new Date().getSeconds();
            int min = new Date().getMinutes();
            int date = new Date().getDate();
            int month = new Date().getMonth() + 1;
            int year = new Date().getYear() + 1900;

            //set oid
            PstPayEmpLevel pstPayEmpLevel = new PstPayEmpLevel(0);
            PayEmpLevel payEmpLevel = new PayEmpLevel();
            // payEmpLevel.setOID(pstPayEmpLevel.getLong(FLD_PAY_EMP_LEVEL_ID));
            //String id=Formater.formatDate(new Date(), "yyyyMddHHmmss");
            long id = employeeId + sec + min + date + month + year;
            System.out.println("format date    " + id);
            if (barcode != null) {
                sql = " INSERT INTO " + TBL_PAY_EMP_LEVEL + "( "
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ID] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_POS_FOR_TAX] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_PER_BEGIN] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_PER_END] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_COMMENCING_ST] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PREV_INCOME] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PREV_TAX_PAID] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID] + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA] + ") VALUES ("
                        + employeeId + ",'" + barcode + "','" + start_date + "','" + end_date + "'," + bankId + ",'" + bank_acc + "','" + pos_for_tax + "'," + begin + "," + end + ",'" + statusCoba + "','" + prevIncome + "'," + prevTaxPaid + "," + id + "," + PstPayEmpLevel.CURRENT + ")";//,"+bankId+,",'"+bank_acc+"','"+pos_for_tax"',"+begin+","+end+","+statusCoba+","+prevIncome+","+prevTaxPaid+","+employee_id+")";

            }
            System.out.println("\tsetupEmployee : " + sql);
            //dbrs = DBHandler.execQueryResult(sql);
            int status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();

            //while(rs.next()) { result = true; }
            //rs.close();
        } catch (Exception e) {
            System.err.println("\tsetupEmployee error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }

    public static void updateSetupEmployee(String salaryLevel, String start_date, long bankId, String bank_acc, String pos_for_tax, int begin, int end, int statusCoba, double prevIncome, int prevTaxPaid, long empLevelId, int mealAllowance, int ovtTypeIdx) {
        updateSetupEmployee(salaryLevel, start_date, null, bankId, bank_acc, pos_for_tax, begin, end, statusCoba, prevIncome,
                prevTaxPaid, empLevelId, mealAllowance, ovtTypeIdx);
    }

    public static void updateSetupEmployee(String salaryLevel, String start_date, String end_date, long bankId, String bank_acc, String pos_for_tax, int begin, int end, int statusCoba, double prevIncome, int prevTaxPaid, long empLevelId, int mealAllowance, int ovtTypeIdx) {
        DBResultSet dbrs = null;

        if (end_date == null || end_date.length() < 6) {
            try {
                Date theStart = new Date(start_date);
                end_date = "" + (theStart.getYear() + 1900 + 1) + "-" + (theStart.getMonth() + 1) + "-" + theStart.getDate();
            } catch (Exception exc) {
                Date theStart = new Date();
                start_date = "" + (theStart.getYear() + 1900) + "-" + (theStart.getMonth() + 1) + "-" + theStart.getDate();
                end_date = "" + (theStart.getYear() + 1900 + 1) + "-" + (theStart.getMonth() + 1) + "-" + theStart.getDate();
            }
        }

        try {
            String sql = "";
            if (salaryLevel != null) {
                sql = " UPDATE " + TBL_PAY_EMP_LEVEL + " SET "
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE] + "='" + salaryLevel + "',"
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE] + "='" + start_date + "',"
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE] + "='" + end_date + "',"
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ID] + "=" + bankId + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR] + "='" + bank_acc + "',"
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_POS_FOR_TAX] + "='" + pos_for_tax + "',"
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_PER_BEGIN] + "=" + begin + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_PER_END] + "=" + end + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_COMMENCING_ST] + "=" + statusCoba + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PREV_INCOME] + "=" + prevIncome + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_MEAL_ALLOWANCE] + "=" + mealAllowance + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_OVT_IDX_TYPE] + "=" + ovtTypeIdx + ","
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PREV_TAX_PAID] + "=" + prevTaxPaid + " WHERE "
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID] + "=" + empLevelId;

            }
            System.out.println("\tupdateSetupEmployee : " + sql);
            //dbrs = DBHandler.execQueryResult(sql);
            int status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();

            //while(rs.next()) { result = true; }
            //rs.close();
        } catch (Exception e) {
            System.err.println("\tupdateSetupEmployee error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }

    public static void UpdateStatus(long employeeId) {
        DBResultSet dbrs = null;
        try {
            String sql = "";
            if (employeeId != 0) {
                sql = " UPDATE " + TBL_PAY_EMP_LEVEL + " SET "
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA] + "=" + PstPayEmpLevel.HISTORY + " WHERE "
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] + "=" + employeeId;

            }
            System.out.println("\tupdateSetupEmployee : " + sql);
            //dbrs = DBHandler.execQueryResult(sql);
            int status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();

            //while(rs.next()) { result = true; }
            //rs.close();
        } catch (Exception e) {
            System.err.println("\tupdateSetupEmployee error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }

    public static void updateBankAcc(long employeeId, String noRek) {
        DBResultSet dbrs = null;

        try {
            String sql = "";
            if (employeeId != 0) {
                sql = " UPDATE " + TBL_PAY_EMP_LEVEL + " SET "
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_BANK_ACC_NR] + "='" + noRek
                        + "' WHERE "
                        + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] + "='" + employeeId + "'";

            }
            System.out.println("\tupdateSetupEmployee : " + sql);
            //dbrs = DBHandler.execQueryResult(sql);
            int status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();

            //while(rs.next()) { result = true; }
            //rs.close();
        } catch (Exception e) {
            System.err.println("\tupdateSetupEmployee error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }

    /* this method used to get the employees with salary level 
     @ param : salaryLevel
     */
    public static Vector listEmpLevel(String salaryLevel) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        System.out.println("salaryLevel" + salaryLevel);
        if (salaryLevel == "") {
            return new Vector(1, 1);
        }
        try {
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + ", LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS PAY"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstSalaryLevel.TBL_SALARY_LEVEL + " AS LEV"
                    + " ON PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + " = LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_CODE]
                    + " WHERE "
                    + (salaryLevel != null && (salaryLevel.equalsIgnoreCase("ALL")
                    || salaryLevel.equalsIgnoreCase("ALL LEVELS")) ? "(1=1)" : (" PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE] + "="
                    + "'" + salaryLevel + "'"))
                    + " AND " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + "=" + PstPayEmpLevel.CURRENT
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=0";

            String whereClause = "";
            if (whereClause != null && whereClause.length() > 0) {
                whereClause = " AND " + whereClause.substring(0, whereClause.length() - 4);
                sql = sql + whereClause;
                //sql = sql + " WHERE " + whereClause;
            }


            //sql = sql + " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            //sql = sql + " LIMIT " + start + "," + recordToGet;

            System.out.println("\t SQL listEmpLevel : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vect = new Vector(1, 1);

                Employee employee = new Employee();
                PayEmpLevel payEmpLevel = new PayEmpLevel();
                SalaryLevel salary = new SalaryLevel();

                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));

                vect.add(employee);
                payEmpLevel.setOID(rs.getLong(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]));
                payEmpLevel.setLevelCode(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]));
                payEmpLevel.setStartDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]));
                payEmpLevel.setEndDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]));
                payEmpLevel.setStatusData(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]));
                vect.add(payEmpLevel);

                salary.setLevelName(rs.getString(PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]));
                vect.add(salary);

                result.add(vect);
            }

            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on  searchEmployee : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);

    }

    public static Vector listEmpLevel(String salaryLevel, long oidDivision, long oidDepartment) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        System.out.println("salaryLevel" + salaryLevel);
        if (salaryLevel == "") {
            return new Vector(1, 1);
        }
        try {
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + ", LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS PAY"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstSalaryLevel.TBL_SALARY_LEVEL + " AS LEV"
                    + " ON PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + " = LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_CODE]
                    + " WHERE "
                    + (salaryLevel != null && (salaryLevel.equalsIgnoreCase("ALL")
                    || salaryLevel.equalsIgnoreCase("ALL LEVELS")) ? "(1=1)" : (" PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE] + "="
                    + "'" + salaryLevel + "'"))
                    + " AND " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + "=" + PstPayEmpLevel.CURRENT
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=0";

            sql = sql + (oidDepartment == 0 ? "" : (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + oidDepartment));
            sql = sql + (oidDivision == 0 ? "" : (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision));
            /*if(whereClause != null && whereClause.length()>0){
             whereClause = " AND "+ whereClause.substring(0,whereClause.length()-4);
             sql = sql + whereClause;
             //sql = sql + " WHERE " + whereClause;
             }*/


            //sql = sql + " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            //sql = sql + " LIMIT " + start + "," + recordToGet;

            System.out.println("\t SQL listEmpLevel : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vect = new Vector(1, 1);

                Employee employee = new Employee();
                PayEmpLevel payEmpLevel = new PayEmpLevel();
                SalaryLevel salary = new SalaryLevel();

                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));

                vect.add(employee);
                payEmpLevel.setOID(rs.getLong(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]));
                payEmpLevel.setLevelCode(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]));
                payEmpLevel.setStartDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]));
                payEmpLevel.setEndDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]));
                payEmpLevel.setStatusData(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]));
                vect.add(payEmpLevel);

                salary.setLevelName(rs.getString(PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]));
                vect.add(salary);

                result.add(vect);
            }

            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on  searchEmployee : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);

    }

    public static Vector listEmpLevel(String salaryLevel, Vector posLevelIdx, long payrollG) {
        return listEmpLevel(salaryLevel, posLevelIdx, 0, 0, payrollG);
    }

    public static Vector listEmpLevelX(String salaryLevel, Vector posLevelIdx, long oidDepartment, long payrollG, long payperiodId) {
        return listEmpLevel(salaryLevel, posLevelIdx, 0, oidDepartment, null, PstEmployee.NO_RESIGN, payrollG, payperiodId, 0);
    }

    public static Vector listEmpLevelX(String salaryLevel, Vector posLevelIdx, long oidDepartment, long payrollG, long payperiodId, long sectionId) {
        return listEmpLevel(salaryLevel, posLevelIdx, 0, oidDepartment, null, PstEmployee.NO_RESIGN, payrollG, payperiodId, sectionId);
    }

    public static Vector listEmpLevelX(String salaryLevel, Vector posLevelIdx, long oidDivision, long oidDepartment, long payrollG, long payperiodId, long sectionId) {
        return listEmpLevel(salaryLevel, posLevelIdx, oidDivision, oidDepartment, null, PstEmployee.NO_RESIGN, payrollG, payperiodId, sectionId);
    }

    public static Vector listEmpLevel(String salaryLevel, Vector posLevelIdx, long oidDepartment, long payrollG) {
        return listEmpLevel(salaryLevel, posLevelIdx, 0, oidDepartment, null, PstEmployee.NO_RESIGN, payrollG, 0, 0);
    }

    public static Vector listEmpLevel(String salaryLevel, Vector posLevelIdx, long oidDivision, long oidDepartment, long payrollG) {
        return listEmpLevel(salaryLevel, posLevelIdx, oidDivision, oidDepartment, null, PstEmployee.NO_RESIGN, payrollG, 0, 0);
    }

    public static Vector listEmpLevel(String salaryLevel, Vector posLevelIdx, long oidDivision, long oidDepartment, Date minCommencingDate, int status_resign, long payrollG, long payPeriodId, long sectionId) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        System.out.println("salaryLevel" + salaryLevel);
        if (salaryLevel == "") {
            return new Vector(1, 1);
        }
        try {
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + ", LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS PAY"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstSalaryLevel.TBL_SALARY_LEVEL + " AS LEV"
                    + " ON PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + " = LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_CODE]
                    + " INNER JOIN " + PstPaySlip.TBL_PAY_SLIP + " AS PAYS"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = PAYS." + PstPaySlip.fieldNames[PstPaySlip.FLD_EMPLOYEE_ID]
                    + (posLevelIdx != null && posLevelIdx.size() > 0
                    ? " INNER JOIN " + PstPosition.TBL_HR_POSITION + "  AS POS ON POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]
                    + "= EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] : "")
                    + " WHERE ";

            String whereClause = (salaryLevel != null && !(salaryLevel.equalsIgnoreCase("ALL")
                    || salaryLevel.equalsIgnoreCase("ALL LEVELS")) ? (" PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE] + "="
                    + "'" + salaryLevel + "'") : "(1=1)")
                    + " AND " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + "=" + PstPayEmpLevel.CURRENT
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + status_resign
                    + (minCommencingDate == null ? "" : (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_CATEGORY_DATE]));

            if (payrollG > 0) {
                whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + " = " + payrollG;
            }
            if (sectionId > 0) {
                whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
            }
            if (payPeriodId > 0) {
                whereClause = whereClause + " AND PAYS." + PstPaySlip.fieldNames[PstPaySlip.FLD_PERIOD_ID] + " = " + payPeriodId;
            }
            /*
             INNER JOIN pay_sal_level AS LEV ON PAY.LEVEL_CODE = LEV.LEVEL_CODE 
             INNER JOIN hr_position AS POS ON EMP.POSITION_ID = POS.POSITION_ID
             WHERE STATUS_DATA=0 AND EMP.RESIGNED=0 AND POS.POSITION_LEVEL_PAYROL IN(0)
             */

            if (posLevelIdx != null && posLevelIdx.size() > 0) {
                // 2014-11-27 update by Hendra McHen
                whereClause = whereClause + " AND POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL_PAYROL] + " IN (";
                for (int i = 0; i < posLevelIdx.size(); i++) {
                    whereClause = whereClause + posLevelIdx.get(i) + ",";
                }
                whereClause = whereClause.substring(0, whereClause.length() - 1);
                whereClause = whereClause + ")";
            }

            /*  if(whereClause != null && whereClause.length()>0){
             whereClause = " AND "+ whereClause;
             sql = sql + whereClause;
             //sql = sql + " WHERE " + whereClause;
             }*/
            sql = sql + whereClause;
            if (whereClause != null && whereClause.length() > 0) {
                if (oidDepartment != 0) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + oidDepartment);
                }
                if (oidDivision != 0) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision);
                }
            } else {
                if (oidDepartment != 0) {
                    sql = sql + (" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + oidDepartment);
                }
                if (oidDivision != 0) {
                    sql = sql + (" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision);
                }
            }


            //sql = sql + " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            //sql = sql + " LIMIT " + start + "," + recordToGet;

            System.out.println("\t SQL listEmpLevel : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vect = new Vector(1, 1);

                Employee employee = new Employee();
                PayEmpLevel payEmpLevel = new PayEmpLevel();
                SalaryLevel salary = new SalaryLevel();

                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                employee.setDivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]));
                employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                employee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
                //update by satrya 2014-05-14
                employee.setPositionId(rs.getLong("EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));

                vect.add(employee);
                payEmpLevel.setOID(rs.getLong(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]));
                payEmpLevel.setLevelCode(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]));
                payEmpLevel.setStartDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]));
                payEmpLevel.setEndDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]));
                payEmpLevel.setStatusData(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]));
                vect.add(payEmpLevel);

                salary.setLevelName(rs.getString(PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]));
                vect.add(salary);

                result.add(vect);
            }

            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on  searchEmployee : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;

    }

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
//add by priska 20151214   

    public static String listUpdateEmpLevel(String payNum, String fullname, long oidCompany, long oidDivision, long oidDepartment, long oidSection, long oidPosition, long payrollGroupId, Date selectedDate) {
        DBResultSet dbrs = null;
        String result = "";

        try {
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_NPWP]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + ", PAY.*"
                    + "  FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + "  INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS PAY"
                    + "  ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + "  = PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] + " WHERE ";

            String whereClause = " (1=1) ";
            whereClause = whereClause + " AND PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA] + " = " + 0;

            if ((payNum != null) && (payNum.length() > 0)) {
                Vector vectNum = logicParser(payNum);
                if (vectNum != null && vectNum.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }


            if ((fullname != null) && (fullname.length() > 0)) {
                Vector vectName = logicParser(fullname);
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }

            if (oidCompany != 0) {
                whereClause += " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + oidCompany;
            }
            if (oidDivision != 0) {
                whereClause += " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + oidDivision;
            }
            if (oidDepartment != 0) {
                whereClause += " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + oidDepartment;
            }
            if (oidSection != 0) {
                whereClause += " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + oidSection;
            }
            if (oidPosition != 0) {
                whereClause += " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = " + oidPosition;
            }
            if (payrollGroupId != 0) {
                whereClause += " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + " = " + payrollGroupId;
            }
            sql = sql + whereClause;
            sql = sql + " ORDER BY " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE] + " ASC ";


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("\t SQL listEmpLevel : " + sql);
            while (rs.next()) {
                Vector vect = new Vector(1, 1);

                Employee employee = new Employee();
                PayEmpLevel payEmpLevel = new PayEmpLevel();

                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                employee.setDivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]));
                employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                employee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
                employee.setBirthDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]));
                employee.setNpwp(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NPWP]));

                try {
                    employee.setPositionId(rs.getLong("EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));
                } catch (Exception exc1) {
                    System.out.println(exc1);
                }

                vect.add(employee);
                resultToObject(rs, payEmpLevel);
                vect.add(payEmpLevel);

                //result.add(vect);

                //update payempLevel
                payEmpLevel.setEndDate(selectedDate);
                payEmpLevel.setStatusData(1); //sdata history
                try {

                    long oid = PstPayEmpLevel.updateExc(payEmpLevel);

                    result = result + " Data for emp num[" + employee.getEmployeeNum() + "] - " + payEmpLevel.getLevelCode() + " = Succes <br> ";
                } catch (Exception e) {
                }

            }

            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on  searchEmployee : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;

    }

    public static Vector listEmpLevelwithDateResign(String salaryLevel, Vector posLevelIdx, long oidDepartment, Date minCommencingDate, int status_resign, Date startDate, Date endDate, Date resignDateFrom, Date resignDateTo, long payrollGroupId) {
        return listEmpLevelwithDateResign(salaryLevel, posLevelIdx, 0, oidDepartment, minCommencingDate, status_resign, startDate, endDate, resignDateFrom, resignDateTo, payrollGroupId, 0);
    }

    public static Vector listEmpLevelwithDateResign(String salaryLevel, Vector posLevelIdx, long oidDivision, long oidDepartment, Date minCommencingDate, int status_resign, Date startDate, Date endDate, Date resignDateFrom, Date resignDateTo, long payrollGroupId) {
        return listEmpLevelwithDateResign(salaryLevel, posLevelIdx, oidDivision, oidDepartment, minCommencingDate, status_resign, startDate, endDate, resignDateFrom, resignDateTo, payrollGroupId, 0);
    }

    public static Vector listEmpLevelwithDateResign(String salaryLevel, Vector posLevelIdx, long oidDivision, long oidDepartment, Date minCommencingDate, int status_resign, Date startDate, Date endDate, Date resignDateFrom, Date resignDateTo, long payrollGroupId, long periodId) {
        return listEmpLevelwithDateResign(salaryLevel, posLevelIdx, oidDivision, oidDepartment, minCommencingDate, status_resign, startDate, endDate, resignDateFrom, resignDateTo, payrollGroupId, periodId, 0, "");
    }

    public static Vector listEmpLevelwithDateResign(String salaryLevel, Vector posLevelIdx, long oidDivision, long oidDepartment, Date minCommencingDate, int status_resign, Date startDate, Date endDate, Date resignDateFrom, Date resignDateTo, long payrollGroupId, long periodId, long sectionId, String empNum) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        System.out.println("salaryLevel" + salaryLevel);
        if (salaryLevel == "") {
            return new Vector(1, 1);
        }
        try {
            String sql2 = "";
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_NPWP]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DANA_PENDIDIKAN]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]
                    + ", PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + ", LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + " INNER JOIN " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS PAY"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstSalaryLevel.TBL_SALARY_LEVEL + " AS LEV"
                    + " ON PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + " = LEV." + PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_CODE]
                    + " INNER JOIN " + PstPaySlip.TBL_PAY_SLIP + " AS PAYS"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = PAYS." + PstPaySlip.fieldNames[PstPaySlip.FLD_EMPLOYEE_ID]
                    + (posLevelIdx != null && posLevelIdx.size() > 0
                    ? " INNER JOIN " + PstPosition.TBL_HR_POSITION + "  AS POS ON POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]
                    + "= EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] : "")
                    + " WHERE ";

            sql2 = new String(sql);

            String whereClause = (salaryLevel != null && !(salaryLevel.equalsIgnoreCase("ALL")
                    || salaryLevel.equalsIgnoreCase("ALL LEVELS")) ? (" PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE] + "="
                    + "'" + salaryLevel + "'") : "(1=1)")
                    + " AND " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + "=" + PstPayEmpLevel.CURRENT
                    + " AND (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + status_resign + " OR EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] + " >= \"" + Formater.formatDate(startDate, "yyyy-MM-dd HH:mm:ss") + "\" ) "
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " <= \"" + Formater.formatDate(endDate, "yyyy-MM-dd HH:mm:ss") + "\"  ";
            // " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+"="+status_resign +
            //( minCommencingDate==null? "": (" AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " < \"" + Formater.formatDate(minCommencingDate, "yyyy-MM-dd HH:mm:ss")+ "\" " ) );
            if (payrollGroupId != 0 || payrollGroupId > 0) {
                whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + " = " + payrollGroupId;
            }

            if (periodId != 0 || periodId > 0) {
                whereClause = whereClause + " AND PAYS." + PstPaySlip.fieldNames[PstPaySlip.FLD_PERIOD_ID] + " = " + periodId;
            }

            if (sectionId != 0 || sectionId > 0) {
                whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
            }
            String whereClause2 = (salaryLevel != null && !(salaryLevel.equalsIgnoreCase("ALL")
                    || salaryLevel.equalsIgnoreCase("ALL LEVELS")) ? (" PAY." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE] + "="
                    + "'" + salaryLevel + "'") : "(1=1)")
                    + " AND " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]
                    + "=" + PstPayEmpLevel.CURRENT
                    + " AND (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 1 "
                    + (resignDateFrom == null ? "" : (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] + " >= \"" + Formater.formatDate(resignDateFrom, "yyyy-MM-dd HH:mm:ss") + "\"  "))
                    + (resignDateTo == null ? "" : (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] + " <= \"" + Formater.formatDate(resignDateTo, "yyyy-MM-dd HH:mm:ss") + "\" ) "))
                    + (minCommencingDate == null ? "" : (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " <= \"" + Formater.formatDate(minCommencingDate, "yyyy-MM-dd HH:mm:ss") + "\" "))
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " <= \"" + Formater.formatDate(endDate, "yyyy-MM-dd HH:mm:ss") + "\"  ";

            if (payrollGroupId != 0 || payrollGroupId > 0) {
                whereClause2 = whereClause2 + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + " = " + payrollGroupId;
            }
            if (periodId != 0 || periodId > 0) {
                whereClause2 = whereClause2 + " AND PAYS." + PstPaySlip.fieldNames[PstPaySlip.FLD_PERIOD_ID] + " = " + periodId;
            }
            if (sectionId != 0 || sectionId > 0) {
                whereClause2 = whereClause2 + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
            }
            /*
             INNER JOIN pay_sal_level AS LEV ON PAY.LEVEL_CODE = LEV.LEVEL_CODE 
             INNER JOIN hr_position AS POS ON EMP.POSITION_ID = POS.POSITION_ID
             WHERE STATUS_DATA=0 AND EMP.RESIGNED=0 AND POS.POSITION_LEVEL_PAYROL IN(0)
             */

            if (posLevelIdx != null && posLevelIdx.size() > 0) {
                // 2014-11-27 update by Hendra McHen
                whereClause = whereClause + " AND POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL_PAYROL] + " IN (";
                whereClause2 = whereClause2 + " AND POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL_PAYROL] + " IN (";
                for (int i = 0; i < posLevelIdx.size(); i++) {
                    whereClause = whereClause + posLevelIdx.get(i) + ",";
                    whereClause2 = whereClause2 + posLevelIdx.get(i) + ",";
                }
                whereClause = whereClause.substring(0, whereClause.length() - 1);
                whereClause2 = whereClause2.substring(0, whereClause2.length() - 1);
                whereClause = whereClause + ")";
                whereClause2 = whereClause2 + ")";
            }

            /*  if(whereClause != null && whereClause.length()>0){
             whereClause = " AND "+ whereClause;
             sql = sql + whereClause;
             //sql = sql + " WHERE " + whereClause;
             }*/
            sql = sql + whereClause;
            sql2 = sql2 + whereClause2;

            if (whereClause != null && whereClause.length() > 0) {
                if (oidDepartment != 0) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + oidDepartment);
                }
                if (oidDivision != 0) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision);
                }
                if (!empNum.equals("")) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + "= '" + empNum + "'");
                }
            } else {
                if (oidDepartment != 0) {
                    sql = sql + (" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + oidDepartment);
                }
                if (oidDivision != 0) {
                    sql = sql + (" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision);
                }
                if (!empNum.equals("")) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + "= '" + empNum + "'");
                }
            }

            if (whereClause2 != null && whereClause2.length() > 0) {
                if (oidDepartment != 0) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + oidDepartment);
                }
                if (oidDivision != 0) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision);
                }
                if (!empNum.equals("")) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + "= '" + empNum + "'");
                }
            } else {
                if (oidDepartment != 0) {
                    sql = sql + (" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + oidDepartment);
                }
                if (oidDivision != 0) {
                    sql = sql + (" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + oidDivision);
                }
                if (!empNum.equals("")) {
                    sql = sql + (" AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + "= '" + empNum + "'");
                }
            }

            //sql = sql + " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            //sql = sql + " LIMIT " + start + "," + recordToGet;

            System.out.println("\t SQL listEmpLevel : " + sql);
            System.out.println("\t SQL listEmpLevel : " + sql2);


            int withoutDH = 0;
            try {
                withoutDH = Integer.valueOf(PstSystemProperty.getValueByName("SAL_LEVEL_WITHOUT_DH"));
            } catch (Exception e) {
                System.out.printf("VALUE_NOTDC TIDAK DI SET?");
            }
            if (resignDateFrom != null || resignDateTo != null) {

                if (withoutDH == 1) {
                    sql = sql + " AND LEV.`LEVEL_NAME` NOT LIKE '%-DH%' UNION " + sql2 + " AND LEV.`LEVEL_NAME` NOT LIKE '%-DH%' ";
                    //sql=sql+" AND LEV.`LEVEL_NAME` NOT LIKE '%-DH%'";
                } else {
                    sql = sql + " UNION " + sql2;
                }


            }
            //mendadak untuk borobudur 2015-07-23


            sql = sql + " ORDER BY " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE] + " ASC ";


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("\t SQL listEmpLevel : " + sql);
            while (rs.next()) {
                Vector vect = new Vector(1, 1);

                Employee employee = new Employee();
                PayEmpLevel payEmpLevel = new PayEmpLevel();
                SalaryLevel salary = new SalaryLevel();

                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                employee.setDivisionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]));
                employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                employee.setDanaPendidikan(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_DANA_PENDIDIKAN]));
                employee.setEmpCategoryId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]));

                try {
                    employee.setSectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]));
                } catch (Exception e) {
                }

                employee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
                employee.setResigned(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]));
                employee.setBirthDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]));
                employee.setNpwp(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NPWP]));
                //update by satrya 2014-05-14
                try {
                    employee.setPositionId(rs.getLong("EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));
                } catch (Exception exc1) {
                    System.out.println(exc1);
                }
                vect.add(employee);
                payEmpLevel.setOID(rs.getLong(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_PAY_EMP_LEVEL_ID]));
                payEmpLevel.setLevelCode(rs.getString(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]));
                payEmpLevel.setStartDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE]));
                payEmpLevel.setEndDate(rs.getDate(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE]));
                payEmpLevel.setStatusData(rs.getInt(PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA]));
                vect.add(payEmpLevel);

                salary.setLevelName(rs.getString(PstSalaryLevel.fieldNames[PstSalaryLevel.FLD_LEVEL_NAME]));
                vect.add(salary);

                result.add(vect);
            }

            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on  searchEmployee : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;

    }

    /**
     * this method used to get bankAccount
     *
     * @param : employeeId created by Yunny
     */
    public static String getBankAccNr(long employeeId) {
        DBResultSet dbrs = null;
        String result = "";
        try {
            String sql = "SELECT BANK_ACC_NR FROM " + TBL_PAY_EMP_LEVEL + " WHERE "
                    + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] + "=" + employeeId
                    + " AND " + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_STATUS_DATA] + "=" + PstPayEmpLevel.CURRENT;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("sql getBankAccNr  " + sql);
            while (rs.next()) {
                result = rs.getString(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return result;
    }

    public static int getTaxHomePayStatus(long employeeId, String comcode) {
        DBResultSet dbrs = null;
        int result = 0;
        try {
            String sql = "select DISTINCT `plc`.`TAKE_HOME_PAY` from `pay_level_comp` plc inner join `pay_emp_level` pel on pel.`LEVEL_CODE` = `plc`.`LEVEL_CODE` where pel.`EMPLOYEE_ID` =  " + employeeId + " and `plc`.`COMP_CODE` =  \"" + comcode + "\"";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            // System.out.println(sql);
            while (rs.next()) {
                result = rs.getInt(1);
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        }
        return result;
    }

    public static PayEmpLevel getActiveLevelByEmployeeOid(long oid) {
        String whereClause = fieldNames[FLD_EMPLOYEE_ID] + "=" + oid + " AND " + fieldNames[FLD_STATUS_DATA] + "=" + CURRENT;
        String orderClause = fieldNames[FLD_START_DATE] + " DESC ";
        Vector v = list(0, 1, whereClause, orderClause);
        if (v != null && v.size() > 0) {
            return (PayEmpLevel) v.get(0);
        }
        return null;
    }

    public static PayEmpLevel getPayLevelByEmployeeOid(long oid, Date fromDate, Date toDate, int Status, String orderClause) {
        String whereClause = fieldNames[FLD_EMPLOYEE_ID] + "=" + oid + " AND " + fieldNames[FLD_STATUS_DATA] + "=" + Status
                + " AND ( (\"" + Formater.formatDate(fromDate, "yyyy-MM-dd") + "\" BETWEEN " + fieldNames[FLD_START_DATE]
                + " AND " + fieldNames[FLD_END_DATE] + ") OR (\"" + Formater.formatDate(toDate, "yyyy-MM-dd") + "\" BETWEEN " + fieldNames[FLD_START_DATE]
                + " AND " + fieldNames[FLD_END_DATE] + ") )";
        Vector v = list(0, 1, whereClause, orderClause);
        if (v != null && v.size() > 0) {
            return (PayEmpLevel) v.get(0);
        }
        return null;
    }

    public static PayEmpLevel getActiveLevelByEmployeeOid(long oid, Date fromDate, Date toDate, String orderClause) {
        String whereClause = fieldNames[FLD_EMPLOYEE_ID] + "=" + oid + " AND " + fieldNames[FLD_STATUS_DATA] + "=" + CURRENT
                + " AND ( (\"" + Formater.formatDate(fromDate, "yyyy-MM-dd") + "\" BETWEEN " + fieldNames[FLD_START_DATE]
                + " AND " + fieldNames[FLD_END_DATE] + ") OR (\"" + Formater.formatDate(toDate, "yyyy-MM-dd") + "\" BETWEEN " + fieldNames[FLD_START_DATE]
                + " AND " + fieldNames[FLD_END_DATE] + ") )";
        Vector v = list(0, 1, whereClause, orderClause);
        if (v != null && v.size() > 0) {
            return (PayEmpLevel) v.get(0);
        }
        return null;
    }

    public static PayEmpLevel getPayLevelByEmployeeOid(long oid, Date fromDate, Date toDate, String orderClause) {
        String whereClause = fieldNames[FLD_EMPLOYEE_ID] + "=" + oid
                + " AND ( (\"" + Formater.formatDate(fromDate, "yyyy-MM-dd") + "\" BETWEEN " + fieldNames[FLD_START_DATE]
                + " AND " + fieldNames[FLD_END_DATE] + ") OR (\"" + Formater.formatDate(toDate, "yyyy-MM-dd") + "\" BETWEEN " + fieldNames[FLD_START_DATE]
                + " AND " + fieldNames[FLD_END_DATE] + ") )";
        Vector v = list(0, 1, whereClause, orderClause);
        if (v != null && v.size() > 0) {
            return (PayEmpLevel) v.get(0);
        }
        return null;
    }

    public static long getOidPayLevelByEmployeeOid(long oid, String levelCode, Date startDate) {
        DBResultSet dbrs = null;
        try {
            long lOid = 0;
            String sql = "SELECT " + fieldNames[FLD_PAY_EMP_LEVEL_ID] + " FROM " + TBL_PAY_EMP_LEVEL
                    + " WHERE " + fieldNames[FLD_EMPLOYEE_ID] + "=" + oid
                    + " AND ( (\"" + Formater.formatDate(startDate, "yyyy-MM-dd") + "\" = " + fieldNames[FLD_START_DATE]
                    + "  AND " + fieldNames[FLD_LEVEL_CODE] + "=\"" + levelCode + "\"))";

            dbrs = DBHandler.execQueryResult(sql);
            //System.out.println("SQL LIST1"+sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                lOid = rs.getLong(fieldNames[FLD_PAY_EMP_LEVEL_ID]);
            }
            rs.close();
            return lOid;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * Mencari hashtable untuk pay day from salary level 20150617 priska
     *
     * @param schCategType
     * @return Hashtable
     */
    public static Hashtable getHashPayDayFromSalLev(String employeeId, Date EndDate) {
        DBResultSet dbrs = null;
        Hashtable result = new Hashtable();
//        SELECT plc.`FORMULA` FROM `pay_emp_level` pel INNER JOIN `pay_level_comp` plc ON plc.`LEVEL_CODE` = pel.`LEVEL_CODE`
//WHERE pel.`EMPLOYEE_ID` = 504404573609217277 AND plc.`COMP_CODE` = "GPH" AND ("2015-05-18 00:00:00" BETWEEN pel.`START_DATE`  AND pel.`END_DATE`)
//       
        try {
            String sql = "SELECT plc." + PstSalaryLevelDetail.fieldNames[PstSalaryLevelDetail.FLD_FORMULA]
                    + ", pel." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                    + " FROM " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " AS pel "
                    + " INNER JOIN " + PstSalaryLevelDetail.TBL_PAY_LEVEL_COM + " AS plc"
                    + " ON plc." + PstSalaryLevelDetail.fieldNames[PstSalaryLevelDetail.FLD_LEVEL_CODE]
                    + " = pel." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                    + " WHERE ";

            if (employeeId.length() > 0) {
                sql += " pel." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID] + " in ( " + employeeId + " ) AND ";
            }
            sql += " plc." + PstSalaryLevelDetail.fieldNames[PstSalaryLevelDetail.FLD_COMP_CODE] + " = \"GPH\""
                    + " AND \"" + Formater.formatDate(EndDate, "yyyy-MM-dd HH:mm:ss") + "\" BETWEEN pel." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_START_DATE] + " AND pel." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_END_DATE];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                String formula = rs.getString("plc." + PstSalaryLevelDetail.fieldNames[PstSalaryLevelDetail.FLD_FORMULA]);
                long oid = rs.getLong("pel." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]);
                formula = formula.replaceAll("=", "");
                double nilai = Double.valueOf(formula);
                result.put(oid, nilai);
                // update by satrya 2013-12-12 break;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /* Update by Hendra | 20160816 | get data emp level */
    public static Vector getDataEmpLevelActive(String whereClause) {
        Vector listData = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT pay_emp_level.EMPLOYEE_ID FROM hr_employee ";
            sql += " INNER JOIN pay_emp_level ON hr_employee.EMPLOYEE_ID=pay_emp_level.EMPLOYEE_ID ";
            sql += " WHERE hr_employee.RESIGNED=0 ";
            if (whereClause.length() > 0) {
                sql += " AND " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            System.out.println("sql list  " + sql);
            while (rs.next()) {
                long employeeId = rs.getLong("EMPLOYEE_ID");
                listData.add(employeeId);
            }
            rs.close();
            return listData;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return listData;
    }

    public static String getRiwayatJabatan(String whereClause, String dateFrom, String dateTo) {
        String output = "default";
        String data = "";
        String[] arrDFrom = dateFrom.split("-");
        String[] arrDTo = dateTo.split("-");
        int intPeriodFrom = Integer.valueOf(arrDFrom[0] + arrDFrom[1] + arrDFrom[2]);
        int intPeriodTo = Integer.valueOf(arrDTo[0] + arrDTo[1] + arrDTo[2]);
        String strBiner = "";
        int[] biner = new int[8];
        String orderBy = PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM];
        Vector listCareer = PstCareerPath.list(0, 0, whereClause, orderBy);
        if (listCareer != null && listCareer.size() > 0) {
            data = "<div>Jumlah riwayat : " + listCareer.size() + "</div>";
            data += "<div>Period: " + dateFrom + " s/d " + dateTo + "</div>";
            for (int i = 0; i < listCareer.size(); i++) {
                CareerPath career = (CareerPath) listCareer.get(i);
                String workFrom = "" + career.getWorkFrom();
                String workTo = "" + career.getWorkTo();
                String[] arrWorkFrom = workFrom.split("-");
                String[] arrWorkTo = workTo.split("-");
                int intWorkFrom = Integer.valueOf(arrWorkFrom[0] + arrWorkFrom[1] + arrWorkFrom[2]);
                int intWorkTo = Integer.valueOf(arrWorkTo[0] + arrWorkTo[1] + arrWorkTo[2]);
                for (int b = 0; b < biner.length; b++) {
                    biner[b] = 0;
                }
                strBiner = "";
                if (intWorkFrom > intPeriodFrom) {
                    biner[0] = 1;
                } else { /* intWorkFrom < intPeriodFrom */
                    biner[1] = 1;
                }
                if (intWorkFrom > intPeriodTo) {
                    biner[2] = 1;
                } else { /* intWorkFrom < intPeriodTo */
                    biner[3] = 1;
                }

                if (intWorkTo > intPeriodFrom) {
                    biner[4] = 1;
                } else { /* intWorkTo < intPeriodFrom */
                    biner[5] = 1;
                }
                if (intWorkTo > intPeriodTo) {
                    biner[6] = 1;
                } else { /* intWorkTo < intPeriodTo */
                    biner[7] = 1;
                }

                for (int b = 0; b < biner.length; b++) {
                    strBiner = strBiner + biner[b];
                }
                if (strBiner.equals("10011001")) {
                    /*
                     * Pf ===================== Pt
                     *      Sd =========== Ed
                     */
                    output = "----------------------------------------";
                    output += "<div>Kondisi ke-1:</div>";
                    output += "Work from : " + career.getWorkFrom() + "<br>";
                    output += "Work to : " + career.getWorkTo() + "<br>";
                    output += "Position :" + career.getPosition() + "<br>";
                    output += "----------------------------------------";
                    break;
                }
                if (strBiner.equals("01011010")) {
                    /*
                     *      Pf ======= Pt
                     * Sd ================== Ed
                     */
                    output = "----------------------------------------";
                    output += "<div>Kondisi ke-2:</div>";
                    output += "Work from : " + career.getWorkFrom() + "<br>";
                    output += "Work to : " + career.getWorkTo() + "<br>";
                    output += "Position :" + career.getPosition() + "<br>";
                    output += "----------------------------------------";
                    break;
                }
                if (strBiner.equals("10011010")) {
                    /* 
                     * Pf ================== Pt
                     *          Sd ================ Ed
                     */
                    output = "----------------------------------------";
                    output += "<div>Kondisi ke-3:</div>";
                    output += "Work from : " + career.getWorkFrom() + "<br>";
                    output += "Work to : " + career.getWorkTo() + "<br>";
                    output += "Position :" + career.getPosition() + "<br>";
                    output += "----------------------------------------";
                    break;
                }
                if (strBiner.equals("01011001")) {
                    /*
                     *          Pf ============ Pt
                     * Sd ============= Ed
                     */
                    output = "----------------------------------------";
                    output += "<div>Kondisi ke-4:</div>";
                    output += "Work from : " + career.getWorkFrom() + "<br>";
                    output += "Work to : " + career.getWorkTo() + "<br>";
                    output += "Position :" + career.getPosition() + "<br>";
                    output += "----------------------------------------";
                    break;
                }
                if (strBiner.equals("01010101")) {
                    /*
                     *              Pf ========== Pt
                     * Sd ===== Ed
                     */
                    output = "<div>Kondisi ke-5:</div>";
                    output += "<div>Cari ke databank</div>";
                    output += getEmployeeData(career.getEmployeeId());
                }
                if (strBiner.equals("10101010")) {
                    /* 
                     * Pf ========== Pt
                     *                  Sd ========= Ed
                     */
                    output = "<div>Kondisi ke-6:</div>";
                }

            }
        }

        return data + output;
    }

    public static String getEmployeeData(long employeeId) {
        String out = "-";
        try {
            Employee emp = PstEmployee.fetchExc(employeeId);
            Position pos = PstPosition.fetchExc(emp.getPositionId());
            out = "<div>Position : " + pos.getPosition() + "</div>";
        } catch (Exception e) {
        }
        return out;
    }
}
