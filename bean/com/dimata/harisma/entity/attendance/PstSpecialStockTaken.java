/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.attendance;

import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import java.util.Vector;

/**
 *
 * @author Dimata 007
 */
public class PstSpecialStockTaken extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_SPECIAL_STOCK_TAKEN = "hr_special_stock_taken";
    public static final int FLD_SPECIAL_STOCK_TAKEN_ID = 0;
    public static final int FLD_EMPLOYEE_ID = 1;
    public static final int FLD_SCHEDULE_ID = 2;
    public static final int FLD_SPECIAL_STOCK_ID = 3;
    public static final int FLD_TAKEN_DATE = 4;
    public static final int FLD_TAKEN_QTY = 5;
    public static final int FLD_PAID_DATE = 6;
    public static final int FLD_LEAVE_APPLICATION_ID = 7;
    public static final int FLD_TAKEN_FINISH_DATE = 8;

    public static String[] fieldNames = {
        "SPECIAL_STOCK_TAKEN_ID",
        "EMPLOYEE_ID",
        "SCHEDULE_ID",
        "SPECIAL_STOCK_ID",
        "TAKEN_DATE",
        "TAKEN_QTY",
        "PAID_DATE",
        "LEAVE_APPLICATION_ID",
        "TAKEN_FINISH_DATE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_DATE
    };

    public PstSpecialStockTaken() {
    }

    public PstSpecialStockTaken(int i) throws DBException {
        super(new PstSpecialStockTaken());
    }

    public PstSpecialStockTaken(String sOid) throws DBException {
        super(new PstSpecialStockTaken(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstSpecialStockTaken(long lOid) throws DBException {
        super(new PstSpecialStockTaken(0));
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
        return TBL_SPECIAL_STOCK_TAKEN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstSpecialStockTaken().getClass().getName();
    }

    public static SpecialStockTaken fetchExc(long oid) throws DBException {
        try {
            SpecialStockTaken entSpecialStockTaken = new SpecialStockTaken();
            PstSpecialStockTaken pstSpecialStockTaken = new PstSpecialStockTaken(oid);
            entSpecialStockTaken.setOID(oid);
            entSpecialStockTaken.setEmployeeId(pstSpecialStockTaken.getlong(FLD_EMPLOYEE_ID));
            entSpecialStockTaken.setScheduleId(pstSpecialStockTaken.getlong(FLD_SCHEDULE_ID));
            entSpecialStockTaken.setSpecialStockId(pstSpecialStockTaken.getlong(FLD_SPECIAL_STOCK_ID));
            entSpecialStockTaken.setTakenDate(pstSpecialStockTaken.getDate(FLD_TAKEN_DATE));
            entSpecialStockTaken.setTakenQty(pstSpecialStockTaken.getfloat(FLD_TAKEN_QTY));
            entSpecialStockTaken.setPaidDate(pstSpecialStockTaken.getDate(FLD_PAID_DATE));
            entSpecialStockTaken.setLeaveApplicationId(pstSpecialStockTaken.getlong(FLD_LEAVE_APPLICATION_ID));
            entSpecialStockTaken.setTakenFinishDate(pstSpecialStockTaken.getDate(FLD_TAKEN_FINISH_DATE));
            return entSpecialStockTaken;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockTaken(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        SpecialStockTaken entSpecialStockTaken = fetchExc(entity.getOID());
        entity = (Entity) entSpecialStockTaken;
        return entSpecialStockTaken.getOID();
    }

    public static synchronized long updateExc(SpecialStockTaken entSpecialStockTaken) throws DBException {
        try {
            if (entSpecialStockTaken.getOID() != 0) {
                PstSpecialStockTaken pstSpecialStockTaken = new PstSpecialStockTaken(entSpecialStockTaken.getOID());
                pstSpecialStockTaken.setLong(FLD_EMPLOYEE_ID, entSpecialStockTaken.getEmployeeId());
                pstSpecialStockTaken.setLong(FLD_SCHEDULE_ID, entSpecialStockTaken.getScheduleId());
                pstSpecialStockTaken.setLong(FLD_SPECIAL_STOCK_ID, entSpecialStockTaken.getSpecialStockId());
                pstSpecialStockTaken.setDate(FLD_TAKEN_DATE, entSpecialStockTaken.getTakenDate());
                pstSpecialStockTaken.setFloat(FLD_TAKEN_QTY, entSpecialStockTaken.getTakenQty());
                pstSpecialStockTaken.setDate(FLD_PAID_DATE, entSpecialStockTaken.getPaidDate());
                pstSpecialStockTaken.setLong(FLD_LEAVE_APPLICATION_ID, entSpecialStockTaken.getLeaveApplicationId());
                pstSpecialStockTaken.setDate(FLD_TAKEN_FINISH_DATE, entSpecialStockTaken.getTakenFinishDate());
                pstSpecialStockTaken.update();
                return entSpecialStockTaken.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockTaken(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((SpecialStockTaken) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstSpecialStockTaken pstSpecialStockTaken = new PstSpecialStockTaken(oid);
            pstSpecialStockTaken.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockTaken(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(SpecialStockTaken entSpecialStockTaken) throws DBException {
        try {
            PstSpecialStockTaken pstSpecialStockTaken = new PstSpecialStockTaken(0);
            pstSpecialStockTaken.setLong(FLD_EMPLOYEE_ID, entSpecialStockTaken.getEmployeeId());
            pstSpecialStockTaken.setLong(FLD_SCHEDULE_ID, entSpecialStockTaken.getScheduleId());
            pstSpecialStockTaken.setLong(FLD_SPECIAL_STOCK_ID, entSpecialStockTaken.getSpecialStockId());
            pstSpecialStockTaken.setDate(FLD_TAKEN_DATE, entSpecialStockTaken.getTakenDate());
            pstSpecialStockTaken.setFloat(FLD_TAKEN_QTY, entSpecialStockTaken.getTakenQty());
            pstSpecialStockTaken.setDate(FLD_PAID_DATE, entSpecialStockTaken.getPaidDate());
            pstSpecialStockTaken.setLong(FLD_LEAVE_APPLICATION_ID, entSpecialStockTaken.getLeaveApplicationId());
            pstSpecialStockTaken.setDate(FLD_TAKEN_FINISH_DATE, entSpecialStockTaken.getTakenFinishDate());
            pstSpecialStockTaken.insert();
            entSpecialStockTaken.setOID(pstSpecialStockTaken.getlong(FLD_SPECIAL_STOCK_TAKEN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockTaken(0), DBException.UNKNOWN);
        }
        return entSpecialStockTaken.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((SpecialStockTaken) entity);
    }

    public static void resultToObject(ResultSet rs, SpecialStockTaken entSpecialStockTaken) {
        try {
            entSpecialStockTaken.setOID(rs.getLong(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_SPECIAL_STOCK_TAKEN_ID]));
            entSpecialStockTaken.setEmployeeId(rs.getLong(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_EMPLOYEE_ID]));
            entSpecialStockTaken.setScheduleId(rs.getLong(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_SCHEDULE_ID]));
            entSpecialStockTaken.setSpecialStockId(rs.getLong(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_SPECIAL_STOCK_ID]));
            entSpecialStockTaken.setTakenDate(rs.getTimestamp(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_TAKEN_DATE]));
            entSpecialStockTaken.setTakenQty(rs.getFloat(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_TAKEN_QTY]));
            entSpecialStockTaken.setPaidDate(rs.getTimestamp(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_PAID_DATE]));
            entSpecialStockTaken.setLeaveApplicationId(rs.getLong(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_LEAVE_APPLICATION_ID]));
            entSpecialStockTaken.setTakenFinishDate(rs.getTimestamp(PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_TAKEN_FINISH_DATE]));
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
            String sql = "SELECT * FROM " + TBL_SPECIAL_STOCK_TAKEN;
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
                SpecialStockTaken entSpecialStockTaken = new SpecialStockTaken();
                resultToObject(rs, entSpecialStockTaken);
                lists.add(entSpecialStockTaken);
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

    public static boolean checkOID(long entSpecialStockTakenId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_SPECIAL_STOCK_TAKEN + " WHERE "
                    + PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_SPECIAL_STOCK_TAKEN_ID] + " = " + entSpecialStockTakenId;
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
            String sql = "SELECT COUNT(" + PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_SPECIAL_STOCK_TAKEN_ID] + ") FROM " + TBL_SPECIAL_STOCK_TAKEN;
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
                    SpecialStockTaken entSpecialStockTaken = (SpecialStockTaken) list.get(ls);
                    if (oid == entSpecialStockTaken.getOID()) {
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
