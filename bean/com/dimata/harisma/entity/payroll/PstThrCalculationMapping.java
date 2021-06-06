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
public class PstThrCalculationMapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_THR_CALCULATION_MAPPING = "pay_thr_calculation_mapping";
    public static final int FLD_THR_CALCULATION_MAPPING_ID = 0;
    public static final int FLD_CALCULATION_MAIN_ID = 1;
    public static final int FLD_START_DATE = 2;
    public static final int FLD_END_DATE = 3;
    public static final int FLD_COMPANY_ID = 4;
    public static final int FLD_DIVISION_ID = 5;
    public static final int FLD_DEPARTMENT_ID = 6;
    public static final int FLD_SECTION_ID = 7;
    public static final int FLD_LEVEL_ID = 8;
    public static final int FLD_MARITAL_ID = 9;
    public static final int FLD_EMPLOYEE_CATEGORY = 10;
    public static final int FLD_POSITION_ID = 11;
    public static final int FLD_EMPLOYEE_ID = 12;
    public static final int FLD_GRADE = 13;
    public static final int FLD_LOS_FROM_IN_DAY = 14;
    public static final int FLD_LOS_FROM_IN_MONTH = 15;
    public static final int FLD_LOS_FROM_IN_YEAR = 16;
    public static final int FLD_LOS_TO_IN_DAY = 17;
    public static final int FLD_LOS_TO_IN_MONTH = 18;
    public static final int FLD_LOS_TO_IN_YEAR = 19;
    public static final int FLD_LOS_CURRENT_DATE = 20;
    public static final int FLD_LOS_PER_CURRENT_DATE = 21;
    public static final int FLD_TAX_MARITAL_ID = 22;
    public static final int FLD_MAPPING_TYPE = 23;
    public static final int FLD_VALUE =24;
    public static final int FLD_PROPOTIONAL =25;
    public static final int FLD_PAYROLL_GROUP_ID = 26;
    public static String[] fieldNames = {
        "THR_CALCULATION_MAPPING_ID",
        "CALCULATION_MAIN_ID",
        "START_DATE",
        "END_DATE",
        "COMPANY_ID",
        "DIVISION_ID",
        "DEPARTMENT_ID",
        "SECTION_ID",
        "LEVEL_ID",
        "MARITAL_ID",
        "EMPLOYEE_CATEGORY",
        "POSITION_ID",
        "EMPLOYEE_ID",
        "GRADE",
        "LOS_FROM_IN_DAY",
        "LOS_FROM_IN_MONTH",
        "LOS_FROM_IN_YEAR",
        "LOS_TO_IN_DAY",
        "LOS_TO_IN_MONTH",
        "LOS_TO_IN_YEAR",
        "LOS_CURRENT_DATE",
        "LOS_PER_CURRENT_DATE",
        "TAX_MARITAL_ID",
        "MAPPING_TYPE",
        "VALUE",
        "PROPOTIONAL",
        "PAYROLL_GROUP_ID"
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG
    };
    
    // komponen gaji
    public static int TYPE_EXCLUSION = 0;
    public static int TYPE_MAPPING = 1;

    public static String[] type = {
        "Exclusion", "Mapping"
    };
    
    public static int PROPOTIONAL_YES = 0;
    public static int PROPOTIONAL_NO = 1;

    public PstThrCalculationMapping() {
    }

    public PstThrCalculationMapping(int i) throws DBException {
        super(new PstThrCalculationMapping());
    }

    public PstThrCalculationMapping(String sOid) throws DBException {
        super(new PstThrCalculationMapping(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstThrCalculationMapping(long lOid) throws DBException {
        super(new PstThrCalculationMapping(0));
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
        return TBL_THR_CALCULATION_MAPPING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstThrCalculationMapping().getClass().getName();
    }

    public static ThrCalculationMapping fetchExc(long oid) throws DBException {
        try {
            ThrCalculationMapping entThrCalculationMapping = new ThrCalculationMapping();
            PstThrCalculationMapping pstThrCalculationMapping = new PstThrCalculationMapping(oid);
            entThrCalculationMapping.setOID(oid);
            entThrCalculationMapping.setCalculationMainId(pstThrCalculationMapping.getLong(FLD_CALCULATION_MAIN_ID));
            entThrCalculationMapping.setStartDate(pstThrCalculationMapping.getDate(FLD_START_DATE));
            entThrCalculationMapping.setEndDate(pstThrCalculationMapping.getDate(FLD_END_DATE));
            entThrCalculationMapping.setCompanyId(pstThrCalculationMapping.getLong(FLD_COMPANY_ID));
            entThrCalculationMapping.setDivisionId(pstThrCalculationMapping.getLong(FLD_DIVISION_ID));
            entThrCalculationMapping.setDepartmentId(pstThrCalculationMapping.getLong(FLD_DEPARTMENT_ID));
            entThrCalculationMapping.setSectionId(pstThrCalculationMapping.getLong(FLD_SECTION_ID));
            entThrCalculationMapping.setLevelId(pstThrCalculationMapping.getLong(FLD_LEVEL_ID));
            entThrCalculationMapping.setMaritalId(pstThrCalculationMapping.getLong(FLD_MARITAL_ID));
            entThrCalculationMapping.setEmployeeCategory(pstThrCalculationMapping.getLong(FLD_EMPLOYEE_CATEGORY));
            entThrCalculationMapping.setPositionId(pstThrCalculationMapping.getLong(FLD_POSITION_ID));
            entThrCalculationMapping.setEmployeeId(pstThrCalculationMapping.getLong(FLD_EMPLOYEE_ID));
            entThrCalculationMapping.setGrade(pstThrCalculationMapping.getLong(FLD_GRADE));
            entThrCalculationMapping.setLosFromInDay(pstThrCalculationMapping.getInt(FLD_LOS_FROM_IN_DAY));
            entThrCalculationMapping.setLosFromInMonth(pstThrCalculationMapping.getInt(FLD_LOS_FROM_IN_MONTH));
            entThrCalculationMapping.setLosFromInYear(pstThrCalculationMapping.getInt(FLD_LOS_FROM_IN_YEAR));
            entThrCalculationMapping.setLosToInDay(pstThrCalculationMapping.getInt(FLD_LOS_TO_IN_DAY));
            entThrCalculationMapping.setLosToInMonth(pstThrCalculationMapping.getInt(FLD_LOS_TO_IN_MONTH));
            entThrCalculationMapping.setLosToInYear(pstThrCalculationMapping.getInt(FLD_LOS_TO_IN_YEAR));
            entThrCalculationMapping.setLosCurrentDate(pstThrCalculationMapping.getInt(FLD_LOS_CURRENT_DATE));
            entThrCalculationMapping.setLosPerCurrentDate(pstThrCalculationMapping.getDate(FLD_LOS_PER_CURRENT_DATE));
            entThrCalculationMapping.setTaxMaritalId(pstThrCalculationMapping.getLong(FLD_TAX_MARITAL_ID));
            entThrCalculationMapping.setMappingType(pstThrCalculationMapping.getInt(FLD_MAPPING_TYPE));
            entThrCalculationMapping.setValue(pstThrCalculationMapping.getfloat(FLD_VALUE));
            entThrCalculationMapping.setPropotional(pstThrCalculationMapping.getInt(FLD_PROPOTIONAL));
            entThrCalculationMapping.setPayrollGroupId(pstThrCalculationMapping.getLong(FLD_PAYROLL_GROUP_ID));
            return entThrCalculationMapping;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMapping(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ThrCalculationMapping entThrCalculationMapping = fetchExc(entity.getOID());
        entity = (Entity) entThrCalculationMapping;
        return entThrCalculationMapping.getOID();
    }

    public static synchronized long updateExc(ThrCalculationMapping entThrCalculationMapping) throws DBException {
        try {
            if (entThrCalculationMapping.getOID() != 0) {
                PstThrCalculationMapping pstThrCalculationMapping = new PstThrCalculationMapping(entThrCalculationMapping.getOID());
                pstThrCalculationMapping.setLong(FLD_CALCULATION_MAIN_ID, entThrCalculationMapping.getCalculationMainId());
                pstThrCalculationMapping.setDate(FLD_START_DATE, entThrCalculationMapping.getStartDate());
                pstThrCalculationMapping.setDate(FLD_END_DATE, entThrCalculationMapping.getEndDate());
                pstThrCalculationMapping.setLong(FLD_COMPANY_ID, entThrCalculationMapping.getCompanyId());
                pstThrCalculationMapping.setLong(FLD_DIVISION_ID, entThrCalculationMapping.getDivisionId());
                pstThrCalculationMapping.setLong(FLD_DEPARTMENT_ID, entThrCalculationMapping.getDepartmentId());
                pstThrCalculationMapping.setLong(FLD_SECTION_ID, entThrCalculationMapping.getSectionId());
                pstThrCalculationMapping.setLong(FLD_LEVEL_ID, entThrCalculationMapping.getLevelId());
                pstThrCalculationMapping.setLong(FLD_MARITAL_ID, entThrCalculationMapping.getMaritalId());
                pstThrCalculationMapping.setLong(FLD_EMPLOYEE_CATEGORY, entThrCalculationMapping.getEmployeeCategory());
                pstThrCalculationMapping.setLong(FLD_POSITION_ID, entThrCalculationMapping.getPositionId());
                pstThrCalculationMapping.setLong(FLD_EMPLOYEE_ID, entThrCalculationMapping.getEmployeeId());
                pstThrCalculationMapping.setLong(FLD_GRADE, entThrCalculationMapping.getGrade());
                pstThrCalculationMapping.setInt(FLD_LOS_FROM_IN_DAY, entThrCalculationMapping.getLosFromInDay());
                pstThrCalculationMapping.setInt(FLD_LOS_FROM_IN_MONTH, entThrCalculationMapping.getLosFromInMonth());
                pstThrCalculationMapping.setInt(FLD_LOS_FROM_IN_YEAR, entThrCalculationMapping.getLosFromInYear());
                pstThrCalculationMapping.setInt(FLD_LOS_TO_IN_DAY, entThrCalculationMapping.getLosToInDay());
                pstThrCalculationMapping.setInt(FLD_LOS_TO_IN_MONTH, entThrCalculationMapping.getLosToInMonth());
                pstThrCalculationMapping.setInt(FLD_LOS_TO_IN_YEAR, entThrCalculationMapping.getLosToInYear());
                pstThrCalculationMapping.setInt(FLD_LOS_CURRENT_DATE, entThrCalculationMapping.getLosCurrentDate());
                pstThrCalculationMapping.setDate(FLD_LOS_PER_CURRENT_DATE, entThrCalculationMapping.getLosPerCurrentDate());
                pstThrCalculationMapping.setLong(FLD_TAX_MARITAL_ID, entThrCalculationMapping.getTaxMaritalId());
                pstThrCalculationMapping.setInt(FLD_MAPPING_TYPE, entThrCalculationMapping.getMappingType());
                pstThrCalculationMapping.setFloat(FLD_VALUE, entThrCalculationMapping.getValue());
                pstThrCalculationMapping.setInt(FLD_PROPOTIONAL, entThrCalculationMapping.getPropotional());
                pstThrCalculationMapping.setLong(FLD_PAYROLL_GROUP_ID, entThrCalculationMapping.getPayrollGroupId());
                pstThrCalculationMapping.update();
                return entThrCalculationMapping.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMapping(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ThrCalculationMapping) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstThrCalculationMapping pstThrCalculationMapping = new PstThrCalculationMapping(oid);
            pstThrCalculationMapping.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMapping(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ThrCalculationMapping entThrCalculationMapping) throws DBException {
        try {
            PstThrCalculationMapping pstThrCalculationMapping = new PstThrCalculationMapping(0);
            pstThrCalculationMapping.setLong(FLD_CALCULATION_MAIN_ID, entThrCalculationMapping.getCalculationMainId());
            pstThrCalculationMapping.setDate(FLD_START_DATE, entThrCalculationMapping.getStartDate());
            pstThrCalculationMapping.setDate(FLD_END_DATE, entThrCalculationMapping.getEndDate());
            pstThrCalculationMapping.setLong(FLD_COMPANY_ID, entThrCalculationMapping.getCompanyId());
            pstThrCalculationMapping.setLong(FLD_DIVISION_ID, entThrCalculationMapping.getDivisionId());
            pstThrCalculationMapping.setLong(FLD_DEPARTMENT_ID, entThrCalculationMapping.getDepartmentId());
            pstThrCalculationMapping.setLong(FLD_SECTION_ID, entThrCalculationMapping.getSectionId());
            pstThrCalculationMapping.setLong(FLD_LEVEL_ID, entThrCalculationMapping.getLevelId());
            pstThrCalculationMapping.setLong(FLD_MARITAL_ID, entThrCalculationMapping.getMaritalId());
            pstThrCalculationMapping.setLong(FLD_EMPLOYEE_CATEGORY, entThrCalculationMapping.getEmployeeCategory());
            pstThrCalculationMapping.setLong(FLD_POSITION_ID, entThrCalculationMapping.getPositionId());
            pstThrCalculationMapping.setLong(FLD_EMPLOYEE_ID, entThrCalculationMapping.getEmployeeId());
            pstThrCalculationMapping.setLong(FLD_GRADE, entThrCalculationMapping.getGrade());
            pstThrCalculationMapping.setInt(FLD_LOS_FROM_IN_DAY, entThrCalculationMapping.getLosFromInDay());
            pstThrCalculationMapping.setInt(FLD_LOS_FROM_IN_MONTH, entThrCalculationMapping.getLosFromInMonth());
            pstThrCalculationMapping.setInt(FLD_LOS_FROM_IN_YEAR, entThrCalculationMapping.getLosFromInYear());
            pstThrCalculationMapping.setInt(FLD_LOS_TO_IN_DAY, entThrCalculationMapping.getLosToInDay());
            pstThrCalculationMapping.setInt(FLD_LOS_TO_IN_MONTH, entThrCalculationMapping.getLosToInMonth());
            pstThrCalculationMapping.setInt(FLD_LOS_TO_IN_YEAR, entThrCalculationMapping.getLosToInYear());
            pstThrCalculationMapping.setInt(FLD_LOS_CURRENT_DATE, entThrCalculationMapping.getLosCurrentDate());
            pstThrCalculationMapping.setDate(FLD_LOS_PER_CURRENT_DATE, entThrCalculationMapping.getLosPerCurrentDate());
            pstThrCalculationMapping.setLong(FLD_TAX_MARITAL_ID, entThrCalculationMapping.getTaxMaritalId());
            pstThrCalculationMapping.setInt(FLD_MAPPING_TYPE, entThrCalculationMapping.getMappingType());
            pstThrCalculationMapping.setFloat(FLD_VALUE, entThrCalculationMapping.getValue());
            pstThrCalculationMapping.setInt(FLD_PROPOTIONAL, entThrCalculationMapping.getPropotional());
            pstThrCalculationMapping.setLong(FLD_PAYROLL_GROUP_ID, entThrCalculationMapping.getPayrollGroupId());
            pstThrCalculationMapping.insert();
            entThrCalculationMapping.setOID(pstThrCalculationMapping.getLong(FLD_THR_CALCULATION_MAPPING_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMapping(0), DBException.UNKNOWN);
        }
        return entThrCalculationMapping.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ThrCalculationMapping) entity);
    }

    public static void resultToObject(ResultSet rs, ThrCalculationMapping entThrCalculationMapping) {
        try {
            entThrCalculationMapping.setOID(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_THR_CALCULATION_MAPPING_ID]));
            entThrCalculationMapping.setCalculationMainId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_CALCULATION_MAIN_ID]));
            entThrCalculationMapping.setStartDate(rs.getDate(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_START_DATE]));
            entThrCalculationMapping.setEndDate(rs.getDate(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_END_DATE]));
            entThrCalculationMapping.setCompanyId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_COMPANY_ID]));
            entThrCalculationMapping.setDivisionId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_DIVISION_ID]));
            entThrCalculationMapping.setDepartmentId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_DEPARTMENT_ID]));
            entThrCalculationMapping.setSectionId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_SECTION_ID]));
            entThrCalculationMapping.setLevelId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LEVEL_ID]));
            entThrCalculationMapping.setMaritalId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_MARITAL_ID]));
            entThrCalculationMapping.setEmployeeCategory(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_EMPLOYEE_CATEGORY]));
            entThrCalculationMapping.setPositionId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_POSITION_ID]));
            entThrCalculationMapping.setEmployeeId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_EMPLOYEE_ID]));
            entThrCalculationMapping.setGrade(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_GRADE]));
            entThrCalculationMapping.setLosFromInDay(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_FROM_IN_DAY]));
            entThrCalculationMapping.setLosFromInMonth(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_FROM_IN_MONTH]));
            entThrCalculationMapping.setLosFromInYear(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_FROM_IN_YEAR]));
            entThrCalculationMapping.setLosToInDay(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_TO_IN_DAY]));
            entThrCalculationMapping.setLosToInMonth(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_TO_IN_MONTH]));
            entThrCalculationMapping.setLosToInYear(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_TO_IN_YEAR]));
            entThrCalculationMapping.setLosCurrentDate(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_CURRENT_DATE]));
            entThrCalculationMapping.setLosPerCurrentDate(rs.getDate(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_LOS_PER_CURRENT_DATE]));
            entThrCalculationMapping.setTaxMaritalId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_TAX_MARITAL_ID]));
            entThrCalculationMapping.setMappingType(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_MAPPING_TYPE]));
            entThrCalculationMapping.setValue(rs.getFloat(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_VALUE]));
            entThrCalculationMapping.setPropotional(rs.getInt(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_PROPOTIONAL]));
            entThrCalculationMapping.setPayrollGroupId(rs.getLong(PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_PAYROLL_GROUP_ID]));
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
            String sql = "SELECT * FROM " + TBL_THR_CALCULATION_MAPPING;
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
                ThrCalculationMapping entThrCalculationMapping = new ThrCalculationMapping();
                resultToObject(rs, entThrCalculationMapping);
                lists.add(entThrCalculationMapping);
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

    public static boolean checkOID(long entThrCalculationMappingId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_THR_CALCULATION_MAPPING + " WHERE "
                    + PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_THR_CALCULATION_MAPPING_ID] + " = " + entThrCalculationMappingId;
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
            String sql = "SELECT COUNT(" + PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_THR_CALCULATION_MAPPING_ID] + ") FROM " + TBL_THR_CALCULATION_MAPPING;
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
                    ThrCalculationMapping entThrCalculationMapping = (ThrCalculationMapping) list.get(ls);
                    if (oid == entThrCalculationMapping.getOID()) {
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