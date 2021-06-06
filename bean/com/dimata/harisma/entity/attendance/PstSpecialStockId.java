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

public class PstSpecialStockId extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_SPECIAL_STOCK_ID = "hr_special_stock_management";
    public static final int FLD_SPECIAL_STOCK_ID = 0;
    public static final int FLD_SCHEDULE_ID = 1;
    public static final int FLD_QTY = 2;
    public static final int FLD_OWNING_DATE = 3;
    public static final int FLD_EXPIRED_DATE = 4;
    public static final int FLD_STATUS = 5;
    public static final int FLD_NOTE = 6;
    public static final int FLD_EMPLOYEE_ID = 7;
    public static final int FLD_QTY_USED = 8;
    public static final int FLD_QTY_RESIDUE = 9;
    public static final int FLD_ADDRESS_ID_CARD = 10;

    public static String[] fieldNames = {
        "SPECIAL_STOCK_ID",
        "SCHEDULE_ID",
        "QTY",
        "OWNING_DATE",
        "EXPIRED_DATE",
        "STATUS",
        "NOTE",
        "EMPLOYEE_ID",
        "QTY_USED",
        "QTY_RESIDUE",
        "ADDRESS_ID_CARD"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG
    };

    public static int SS_STS_AKTIF = 0; // SS BISA DI AMBIL
    public static int SS_STS_NOT_AKTIF = 1; // SS BELUM BISA DI AMBIL
    public static int SS_STS_TAKEN = 2; // SS SUDAH HABIS
    public static int SS_STS_EXPIRED = 3; // SS MASA BERLAKUNYA SUDAH BERAKHIR
    public static int SS_STS_NOT_VALID = 4; // SS TIDAK VALID

    public static final String[] fieldStatus = {
        "ACTIVE",
        "NOT ACTIVE",
        "TAKEN",
        "EXPIRED",
        "NOT VALID"
    };

    public PstSpecialStockId() {
    }

    public PstSpecialStockId(int i) throws DBException {
        super(new PstSpecialStockId());
    }

    public PstSpecialStockId(String sOid) throws DBException {
        super(new PstSpecialStockId(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstSpecialStockId(long lOid) throws DBException {
        super(new PstSpecialStockId(0));
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
        return TBL_SPECIAL_STOCK_ID;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstSpecialStockId().getClass().getName();
    }

    public static SpecialStockId fetchExc(long oid) throws DBException {
        try {
            SpecialStockId entSpecialStockId = new SpecialStockId();
            PstSpecialStockId pstSpecialStockId = new PstSpecialStockId(oid);
            entSpecialStockId.setOID(oid);
            entSpecialStockId.setScheduleId(pstSpecialStockId.getLong(FLD_SCHEDULE_ID));
            entSpecialStockId.setQty(pstSpecialStockId.getfloat(FLD_QTY));
            entSpecialStockId.setOwningDate(pstSpecialStockId.getDate(FLD_OWNING_DATE));
            entSpecialStockId.setExpiredDate(pstSpecialStockId.getDate(FLD_EXPIRED_DATE));
            entSpecialStockId.setStatus(pstSpecialStockId.getInt(FLD_STATUS));
            entSpecialStockId.setNote(pstSpecialStockId.getString(FLD_NOTE));
            entSpecialStockId.setEmployeeId(pstSpecialStockId.getLong(FLD_EMPLOYEE_ID));
            entSpecialStockId.setQtyUsed(pstSpecialStockId.getfloat(FLD_QTY_USED));
            entSpecialStockId.setQtyResidue(pstSpecialStockId.getfloat(FLD_QTY_RESIDUE));
            entSpecialStockId.setAddressIdCard(pstSpecialStockId.getLong(FLD_ADDRESS_ID_CARD));
            return entSpecialStockId;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockId(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        SpecialStockId entSpecialStockId = fetchExc(entity.getOID());
        entity = (Entity) entSpecialStockId;
        return entSpecialStockId.getOID();
    }

    public static synchronized long updateExc(SpecialStockId entSpecialStockId) throws DBException {
        try {
            if (entSpecialStockId.getOID() != 0) {
                PstSpecialStockId pstSpecialStockId = new PstSpecialStockId(entSpecialStockId.getOID());
                pstSpecialStockId.setLong(FLD_SCHEDULE_ID, entSpecialStockId.getScheduleId());
                pstSpecialStockId.setDouble(FLD_QTY, entSpecialStockId.getQty());
                pstSpecialStockId.setDate(FLD_OWNING_DATE, entSpecialStockId.getOwningDate());
                pstSpecialStockId.setDate(FLD_EXPIRED_DATE, entSpecialStockId.getExpiredDate());
                pstSpecialStockId.setInt(FLD_STATUS, entSpecialStockId.getStatus());
                pstSpecialStockId.setString(FLD_NOTE, entSpecialStockId.getNote());
                pstSpecialStockId.setLong(FLD_EMPLOYEE_ID, entSpecialStockId.getEmployeeId());
                pstSpecialStockId.setDouble(FLD_QTY_USED, entSpecialStockId.getQtyUsed());
                pstSpecialStockId.setDouble(FLD_QTY_RESIDUE, entSpecialStockId.getQtyResidue());
                pstSpecialStockId.setLong(FLD_ADDRESS_ID_CARD, entSpecialStockId.getAddressIdCard());
                pstSpecialStockId.update();
                return entSpecialStockId.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockId(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((SpecialStockId) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstSpecialStockId pstSpecialStockId = new PstSpecialStockId(oid);
            pstSpecialStockId.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockId(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(SpecialStockId entSpecialStockId) throws DBException {
        try {
            PstSpecialStockId pstSpecialStockId = new PstSpecialStockId(0);
            pstSpecialStockId.setLong(FLD_SCHEDULE_ID, entSpecialStockId.getScheduleId());
            pstSpecialStockId.setDouble(FLD_QTY, entSpecialStockId.getQty());
            pstSpecialStockId.setDate(FLD_OWNING_DATE, entSpecialStockId.getOwningDate());
            pstSpecialStockId.setDate(FLD_EXPIRED_DATE, entSpecialStockId.getExpiredDate());
            pstSpecialStockId.setInt(FLD_STATUS, entSpecialStockId.getStatus());
            pstSpecialStockId.setString(FLD_NOTE, entSpecialStockId.getNote());
            pstSpecialStockId.setLong(FLD_EMPLOYEE_ID, entSpecialStockId.getEmployeeId());
            pstSpecialStockId.setDouble(FLD_QTY_USED, entSpecialStockId.getQtyUsed());
            pstSpecialStockId.setDouble(FLD_QTY_RESIDUE, entSpecialStockId.getQtyResidue());
            pstSpecialStockId.setLong(FLD_ADDRESS_ID_CARD, entSpecialStockId.getAddressIdCard());
            pstSpecialStockId.insert();
            entSpecialStockId.setOID(pstSpecialStockId.getLong(FLD_SPECIAL_STOCK_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstSpecialStockId(0), DBException.UNKNOWN);
        }
        return entSpecialStockId.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((SpecialStockId) entity);
    }

    public static void resultToObject(ResultSet rs, SpecialStockId entSpecialStockId) {
        try {
            entSpecialStockId.setOID(rs.getLong(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_SPECIAL_STOCK_ID]));
            entSpecialStockId.setScheduleId(rs.getLong(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_SCHEDULE_ID]));
            entSpecialStockId.setQty((float) rs.getDouble(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_QTY]));
            entSpecialStockId.setOwningDate(rs.getDate(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_OWNING_DATE]));
            entSpecialStockId.setExpiredDate(rs.getDate(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_EXPIRED_DATE]));
            entSpecialStockId.setStatus(rs.getInt(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_STATUS]));
            entSpecialStockId.setNote(rs.getString(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_NOTE]));
            entSpecialStockId.setEmployeeId(rs.getLong(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_EMPLOYEE_ID]));
            entSpecialStockId.setQtyUsed((float) rs.getDouble(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_QTY_USED]));
            entSpecialStockId.setQtyResidue((float) rs.getDouble(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_QTY_RESIDUE]));
            entSpecialStockId.setAddressIdCard(rs.getLong(PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_ADDRESS_ID_CARD]));
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
            String sql = "SELECT * FROM " + TBL_SPECIAL_STOCK_ID;
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
                SpecialStockId entSpecialStockId = new SpecialStockId();
                resultToObject(rs, entSpecialStockId);
                lists.add(entSpecialStockId);
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

    public static boolean checkOID(long entSpecialStockIdId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_SPECIAL_STOCK_ID + " WHERE "
                    + PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_SPECIAL_STOCK_ID] + " = " + entSpecialStockIdId;
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
            String sql = "SELECT COUNT(" + PstSpecialStockId.fieldNames[PstSpecialStockId.FLD_SPECIAL_STOCK_ID] + ") FROM " + TBL_SPECIAL_STOCK_ID;
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
                    SpecialStockId entSpecialStockId = (SpecialStockId) list.get(ls);
                    if (oid == entSpecialStockId.getOID()) {
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
