/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

/**
 *
 * @author mchen
 */
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

public class PstPayConfigPotongan extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PAY_CONFIG_POTONGAN = "pay_config_potongan";
    public static final int FLD_POTONGAN_KREDIT_ID = 0;
    public static final int FLD_EMPLOYEE_ID = 1;
    public static final int FLD_START_DATE = 2;
    public static final int FLD_END_DATE = 3;
    public static final int FLD_COMPONENT_ID = 4;
    public static final int FLD_ANGSURAN_PERBULAN = 5;
    public static final int FLD_NO_REKENING = 6;
    public static final int FLD_VALID_STATUS = 7;

    public static String[] fieldNames = {
        "POTONGAN_KREDIT_ID",
        "EMPLOYEE_ID",
        "START_DATE",
        "END_DATE",
        "COMPONENT_ID",
        "ANGSURAN_PERBULAN",
        "NO_REKENING",
        "VALID_STATUS"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_STRING,
        TYPE_INT
    };

    public PstPayConfigPotongan() {
    }

    public PstPayConfigPotongan(int i) throws DBException {
        super(new PstPayConfigPotongan());
    }

    public PstPayConfigPotongan(String sOid) throws DBException {
        super(new PstPayConfigPotongan(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPayConfigPotongan(long lOid) throws DBException {
        super(new PstPayConfigPotongan(0));
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
        return TBL_PAY_CONFIG_POTONGAN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPayConfigPotongan().getClass().getName();
    }

    public static PayConfigPotongan fetchExc(long oid) throws DBException {
        try {
            PayConfigPotongan entPayConfigPotongan = new PayConfigPotongan();
            PstPayConfigPotongan pstPayConfigPotongan = new PstPayConfigPotongan(oid);
            entPayConfigPotongan.setOID(oid);
            entPayConfigPotongan.setEmployeeId(pstPayConfigPotongan.getLong(FLD_EMPLOYEE_ID));
            entPayConfigPotongan.setStartDate(pstPayConfigPotongan.getDate(FLD_START_DATE));
            entPayConfigPotongan.setEndDate(pstPayConfigPotongan.getDate(FLD_END_DATE));
            entPayConfigPotongan.setComponentId(pstPayConfigPotongan.getLong(FLD_COMPONENT_ID));
            entPayConfigPotongan.setAngsuranPerbulan(pstPayConfigPotongan.getdouble(FLD_ANGSURAN_PERBULAN));
            entPayConfigPotongan.setNoRekening(pstPayConfigPotongan.getString(FLD_NO_REKENING));
            entPayConfigPotongan.setValidStatus(pstPayConfigPotongan.getInt(FLD_VALID_STATUS));
            return entPayConfigPotongan;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayConfigPotongan(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        PayConfigPotongan entPayConfigPotongan = fetchExc(entity.getOID());
        entity = (Entity) entPayConfigPotongan;
        return entPayConfigPotongan.getOID();
    }

    public static synchronized long updateExc(PayConfigPotongan entPayConfigPotongan) throws DBException {
        try {
            if (entPayConfigPotongan.getOID() != 0) {
                PstPayConfigPotongan pstPayConfigPotongan = new PstPayConfigPotongan(entPayConfigPotongan.getOID());
                pstPayConfigPotongan.setLong(FLD_EMPLOYEE_ID, entPayConfigPotongan.getEmployeeId());
                pstPayConfigPotongan.setDate(FLD_START_DATE, entPayConfigPotongan.getStartDate());
                pstPayConfigPotongan.setDate(FLD_END_DATE, entPayConfigPotongan.getEndDate());
                pstPayConfigPotongan.setLong(FLD_COMPONENT_ID, entPayConfigPotongan.getComponentId());
                pstPayConfigPotongan.setDouble(FLD_ANGSURAN_PERBULAN, entPayConfigPotongan.getAngsuranPerbulan());
                pstPayConfigPotongan.setString(FLD_NO_REKENING, entPayConfigPotongan.getNoRekening());
                pstPayConfigPotongan.setInt(FLD_VALID_STATUS, entPayConfigPotongan.getValidStatus());
                pstPayConfigPotongan.update();
                return entPayConfigPotongan.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayConfigPotongan(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((PayConfigPotongan) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstPayConfigPotongan pstPayConfigPotongan = new PstPayConfigPotongan(oid);
            pstPayConfigPotongan.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayConfigPotongan(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(PayConfigPotongan entPayConfigPotongan) throws DBException {
        try {
            PstPayConfigPotongan pstPayConfigPotongan = new PstPayConfigPotongan(0);
            pstPayConfigPotongan.setLong(FLD_EMPLOYEE_ID, entPayConfigPotongan.getEmployeeId());
            pstPayConfigPotongan.setDate(FLD_START_DATE, entPayConfigPotongan.getStartDate());
            pstPayConfigPotongan.setDate(FLD_END_DATE, entPayConfigPotongan.getEndDate());
            pstPayConfigPotongan.setLong(FLD_COMPONENT_ID, entPayConfigPotongan.getComponentId());
            pstPayConfigPotongan.setDouble(FLD_ANGSURAN_PERBULAN, entPayConfigPotongan.getAngsuranPerbulan());
            pstPayConfigPotongan.setString(FLD_NO_REKENING, entPayConfigPotongan.getNoRekening());
            pstPayConfigPotongan.setInt(FLD_VALID_STATUS, entPayConfigPotongan.getValidStatus());
            pstPayConfigPotongan.insert();
            entPayConfigPotongan.setOID(pstPayConfigPotongan.getLong(FLD_POTONGAN_KREDIT_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayConfigPotongan(0), DBException.UNKNOWN);
        }
        return entPayConfigPotongan.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((PayConfigPotongan) entity);
    }

    public static void resultToObject(ResultSet rs, PayConfigPotongan entPayConfigPotongan) {
        try {
            entPayConfigPotongan.setOID(rs.getLong(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_POTONGAN_KREDIT_ID]));
            entPayConfigPotongan.setEmployeeId(rs.getLong(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_EMPLOYEE_ID]));
            entPayConfigPotongan.setStartDate(rs.getDate(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_START_DATE]));
            entPayConfigPotongan.setEndDate(rs.getDate(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_END_DATE]));
            entPayConfigPotongan.setComponentId(rs.getLong(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_COMPONENT_ID]));
            entPayConfigPotongan.setAngsuranPerbulan(rs.getDouble(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_ANGSURAN_PERBULAN]));
            entPayConfigPotongan.setNoRekening(rs.getString(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_NO_REKENING]));
            entPayConfigPotongan.setValidStatus(rs.getInt(PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_VALID_STATUS]));
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
            String sql = "SELECT * FROM " + TBL_PAY_CONFIG_POTONGAN;
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
                PayConfigPotongan entPayConfigPotongan = new PayConfigPotongan();
                resultToObject(rs, entPayConfigPotongan);
                lists.add(entPayConfigPotongan);
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

    public static boolean checkOID(long entPayConfigPotonganId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_CONFIG_POTONGAN + " WHERE "
                    + PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_POTONGAN_KREDIT_ID] + " = " + entPayConfigPotonganId;
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
            String sql = "SELECT COUNT(" + PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_POTONGAN_KREDIT_ID] + ") FROM " + TBL_PAY_CONFIG_POTONGAN;
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
                    PayConfigPotongan entPayConfigPotongan = (PayConfigPotongan) list.get(ls);
                    if (oid == entPayConfigPotongan.getOID()) {
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
