/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Gunadi
 */
public class PstPayThrEmployee extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PAY_THR_EMPLOYEE = "pay_thr_employee";
    public static final int FLD_PAY_THR_EMPLOYEE_ID = 0;
    public static final int FLD_PAY_THR_ID = 1;
    public static final int FLD_EMPLOYEE_ID = 2;
    public static final int FLD_VALUE = 3;
    public static String[] fieldNames = {
        "PAY_THR_EMPLOYEE_ID",
        "PAY_THR_ID",
        "EMPLOYEE_ID",
        "VALUE"
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public PstPayThrEmployee() {
    }

    public PstPayThrEmployee(int i) throws DBException {
        super(new PstPayThrEmployee());
    }

    public PstPayThrEmployee(String sOid) throws DBException {
        super(new PstPayThrEmployee(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPayThrEmployee(long lOid) throws DBException {
        super(new PstPayThrEmployee(0));
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
        return TBL_PAY_THR_EMPLOYEE;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPayThrEmployee().getClass().getName();
    }

    public static PayThrEmployee fetchExc(long oid) throws DBException {
        try {
            PayThrEmployee entPayThrEmployee = new PayThrEmployee();
            PstPayThrEmployee pstPayThrEmployee = new PstPayThrEmployee(oid);
            entPayThrEmployee.setOID(oid);
            entPayThrEmployee.setPayThrId(pstPayThrEmployee.getLong(FLD_PAY_THR_ID));
            entPayThrEmployee.setEmployeeId(pstPayThrEmployee.getLong(FLD_EMPLOYEE_ID));
            entPayThrEmployee.setValue(pstPayThrEmployee.getfloat(FLD_VALUE));
            return entPayThrEmployee;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThrEmployee(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        PayThrEmployee entPayThrEmployee = fetchExc(entity.getOID());
        entity = (Entity) entPayThrEmployee;
        return entPayThrEmployee.getOID();
    }

    public static synchronized long updateExc(PayThrEmployee entPayThrEmployee) throws DBException {
        try {
            if (entPayThrEmployee.getOID() != 0) {
                PstPayThrEmployee pstPayThrEmployee = new PstPayThrEmployee(entPayThrEmployee.getOID());
                pstPayThrEmployee.setLong(FLD_PAY_THR_ID, entPayThrEmployee.getPayThrId());
                pstPayThrEmployee.setLong(FLD_EMPLOYEE_ID, entPayThrEmployee.getEmployeeId());
                pstPayThrEmployee.setFloat(FLD_VALUE, entPayThrEmployee.getValue());
                pstPayThrEmployee.update();
                return entPayThrEmployee.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThrEmployee(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((PayThrEmployee) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstPayThrEmployee pstPayThrEmployee = new PstPayThrEmployee(oid);
            pstPayThrEmployee.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThrEmployee(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(PayThrEmployee entPayThrEmployee) throws DBException {
        try {
            PstPayThrEmployee pstPayThrEmployee = new PstPayThrEmployee(0);
            pstPayThrEmployee.setLong(FLD_PAY_THR_ID, entPayThrEmployee.getPayThrId());
            pstPayThrEmployee.setLong(FLD_EMPLOYEE_ID, entPayThrEmployee.getEmployeeId());
            pstPayThrEmployee.setFloat(FLD_VALUE, entPayThrEmployee.getValue());
            pstPayThrEmployee.insert();
            entPayThrEmployee.setOID(pstPayThrEmployee.getLong(FLD_PAY_THR_EMPLOYEE_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThrEmployee(0), DBException.UNKNOWN);
        }
        return entPayThrEmployee.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((PayThrEmployee) entity);
    }

    public static void resultToObject(ResultSet rs, PayThrEmployee entPayThrEmployee) {
        try {
            entPayThrEmployee.setOID(rs.getLong(PstPayThrEmployee.fieldNames[PstPayThrEmployee.FLD_PAY_THR_EMPLOYEE_ID]));
            entPayThrEmployee.setPayThrId(rs.getLong(PstPayThrEmployee.fieldNames[PstPayThrEmployee.FLD_PAY_THR_ID]));
            entPayThrEmployee.setEmployeeId(rs.getLong(PstPayThrEmployee.fieldNames[PstPayThrEmployee.FLD_EMPLOYEE_ID]));
            entPayThrEmployee.setValue(rs.getFloat(PstPayThrEmployee.fieldNames[PstPayThrEmployee.FLD_VALUE]));
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
            String sql = "SELECT * FROM " + TBL_PAY_THR_EMPLOYEE;
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
                PayThrEmployee entPayThrEmployee = new PayThrEmployee();
                resultToObject(rs, entPayThrEmployee);
                lists.add(entPayThrEmployee);
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

    public static boolean checkOID(long entPayThrEmployeeId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_THR_EMPLOYEE + " WHERE "
                    + PstPayThrEmployee.fieldNames[PstPayThrEmployee.FLD_PAY_THR_EMPLOYEE_ID] + " = " + entPayThrEmployeeId;
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
            String sql = "SELECT COUNT(" + PstPayThrEmployee.fieldNames[PstPayThrEmployee.FLD_PAY_THR_EMPLOYEE_ID] + ") FROM " + TBL_PAY_THR_EMPLOYEE;
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
                    PayThrEmployee entPayThrEmployee = (PayThrEmployee) list.get(ls);
                    if (oid == entPayThrEmployee.getOID()) {
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
