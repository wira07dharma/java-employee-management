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
public class PstThrCalculationMain extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_THR_CALCULATION_MAIN = "pay_thr_calculation_main";
    public static final int FLD_CALCULATION_MAIN_ID = 0;
    public static final int FLD_CALCULATION_MAIN_TITLE = 1;
    public static final int FLD_CALCULATION_MAIN_DESC = 2;
    public static final int FLD_CALCULATION_MAIN_DATE_CREATE = 3;
    public static String[] fieldNames = {
        "CALCULATION_MAIN_ID",
        "CALCULATION_MAIN_TITLE",
        "CALCULATION_MAIN_DESC",
        "CALCULATION_MAIN_DATE_CREATE"
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE
    };

    public PstThrCalculationMain() {
    }

    public PstThrCalculationMain(int i) throws DBException {
        super(new PstThrCalculationMain());
    }

    public PstThrCalculationMain(String sOid) throws DBException {
        super(new PstThrCalculationMain(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstThrCalculationMain(long lOid) throws DBException {
        super(new PstThrCalculationMain(0));
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
        return TBL_THR_CALCULATION_MAIN;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstThrCalculationMain().getClass().getName();
    }

    public static ThrCalculationMain fetchExc(long oid) throws DBException {
        try {
            ThrCalculationMain entThrCalculationMain = new ThrCalculationMain();
            PstThrCalculationMain pstThrCalculationMain = new PstThrCalculationMain(oid);
            entThrCalculationMain.setOID(oid);
            entThrCalculationMain.setCalculationMainTitle(pstThrCalculationMain.getString(FLD_CALCULATION_MAIN_TITLE));
            entThrCalculationMain.setCalculationMainDesc(pstThrCalculationMain.getString(FLD_CALCULATION_MAIN_DESC));
            entThrCalculationMain.setCalculationMainDateCreate(pstThrCalculationMain.getDate(FLD_CALCULATION_MAIN_DATE_CREATE));
            return entThrCalculationMain;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMain(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ThrCalculationMain entThrCalculationMain = fetchExc(entity.getOID());
        entity = (Entity) entThrCalculationMain;
        return entThrCalculationMain.getOID();
    }

    public static synchronized long updateExc(ThrCalculationMain entThrCalculationMain) throws DBException {
        try {
            if (entThrCalculationMain.getOID() != 0) {
                PstThrCalculationMain pstThrCalculationMain = new PstThrCalculationMain(entThrCalculationMain.getOID());
                pstThrCalculationMain.setString(FLD_CALCULATION_MAIN_TITLE, entThrCalculationMain.getCalculationMainTitle());
                pstThrCalculationMain.setString(FLD_CALCULATION_MAIN_DESC, entThrCalculationMain.getCalculationMainDesc());
                pstThrCalculationMain.setDate(FLD_CALCULATION_MAIN_DATE_CREATE, entThrCalculationMain.getCalculationMainDateCreate());
                pstThrCalculationMain.update();
                return entThrCalculationMain.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMain(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ThrCalculationMain) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstThrCalculationMain pstThrCalculationMain = new PstThrCalculationMain(oid);
            pstThrCalculationMain.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMain(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ThrCalculationMain entThrCalculationMain) throws DBException {
        try {
            PstThrCalculationMain pstThrCalculationMain = new PstThrCalculationMain(0);
            pstThrCalculationMain.setString(FLD_CALCULATION_MAIN_TITLE, entThrCalculationMain.getCalculationMainTitle());
            pstThrCalculationMain.setString(FLD_CALCULATION_MAIN_DESC, entThrCalculationMain.getCalculationMainDesc());
            pstThrCalculationMain.setDate(FLD_CALCULATION_MAIN_DATE_CREATE, entThrCalculationMain.getCalculationMainDateCreate());
            pstThrCalculationMain.insert();
            entThrCalculationMain.setOID(pstThrCalculationMain.getLong(FLD_CALCULATION_MAIN_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstThrCalculationMain(0), DBException.UNKNOWN);
        }
        return entThrCalculationMain.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ThrCalculationMain) entity);
    }

    public static void resultToObject(ResultSet rs, ThrCalculationMain entThrCalculationMain) {
        try {
            entThrCalculationMain.setOID(rs.getLong(PstThrCalculationMain.fieldNames[PstThrCalculationMain.FLD_CALCULATION_MAIN_ID]));
            entThrCalculationMain.setCalculationMainTitle(rs.getString(PstThrCalculationMain.fieldNames[PstThrCalculationMain.FLD_CALCULATION_MAIN_TITLE]));
            entThrCalculationMain.setCalculationMainDesc(rs.getString(PstThrCalculationMain.fieldNames[PstThrCalculationMain.FLD_CALCULATION_MAIN_DESC]));
            entThrCalculationMain.setCalculationMainDateCreate(rs.getDate(PstThrCalculationMain.fieldNames[PstThrCalculationMain.FLD_CALCULATION_MAIN_DATE_CREATE]));
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
            String sql = "SELECT * FROM " + TBL_THR_CALCULATION_MAIN;
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
                ThrCalculationMain entThrCalculationMain = new ThrCalculationMain();
                resultToObject(rs, entThrCalculationMain);
                lists.add(entThrCalculationMain);
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

    public static boolean checkOID(long entThrCalculationMainId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_THR_CALCULATION_MAIN + " WHERE "
                    + PstThrCalculationMain.fieldNames[PstThrCalculationMain.FLD_CALCULATION_MAIN_ID] + " = " + entThrCalculationMainId;
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
            String sql = "SELECT COUNT(" + PstThrCalculationMain.fieldNames[PstThrCalculationMain.FLD_CALCULATION_MAIN_ID] + ") FROM " + TBL_THR_CALCULATION_MAIN;
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
                    ThrCalculationMain entThrCalculationMain = (ThrCalculationMain) list.get(ls);
                    if (oid == entThrCalculationMain.getOID()) {
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
