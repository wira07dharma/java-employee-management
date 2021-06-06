
/* Created on   :  [date] [time] AM/PM 
 * 
 * @author      :  [authorName] 
 * @version     :  [version] 
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...] 
 ******************************************************************
 */
package com.dimata.harisma.entity.masterdata;

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
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.employee.*;

public class PstLevel extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {    
    
    public static final String TBL_HR_LEVEL = "hr_level";//"HR_LEVEL";
    public static final int FLD_LEVEL_ID = 0;
    public static final int FLD_GROUP_RANK_ID = 1;
    public static final int FLD_LEVEL = 2;
    public static final int FLD_DESCRIPTION = 3;
    // added for HR
    public static final int FLD_EMP_MEDIC_LEVEL = 4;
    public static final int FLD_FMLY_MEDIC_LEVEL = 5;
    public static final int FLD_GRADE_LEVEL_ID = 6;
    public static final int FLD_LEVEL_POINT = 7; // added by Hendra McHen 2015-01-08
    public static final int FLD_CODE = 8;
    /* Update by Hendra | 2015-09-09 */
    public static final int FLD_LEVEL_RANK = 9;
    
    /**
    * @Author Gunadi Wirawan
    * @Desc field untuk menentukan entitled DP/EO untuk report EO/PH
    * @Request by Borobudur Jakarta
    */
    public static final int FLD_ENTITLE_DP = 10;
    public static final int FLD_ENTITLED_DP_QTY = 11;
    public static final int FLD_ENTITLE_PH = 12;
    public static final int FLD_ENTITLED_PH_QTY = 13;
    
    public static final String[] fieldNames = {
        "LEVEL_ID",
        "GROUP_RANK_ID",
        "LEVEL",
        "DESCRIPTION",
        "MED_LEVEL_ID_EMPLOYEE",
        "MED_LEVEL_ID_FAMILY",
        "GRADE_LEVEL_ID",
        "LEVEL_POINT",
        "CODE", 
        "LEVEL_RANK",
        "ENTITLE_DP",
        "ENTITLED_DP_QTY",
        "ENTITLE_PH",
        "ENTITLED_PH_QTY"
    };    
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT
    };    
    
    public PstLevel() {
    }
    
    public PstLevel(int i) throws DBException {        
        super(new PstLevel());        
    }
    
    public PstLevel(String sOid) throws DBException {        
        super(new PstLevel(0));        
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }        
    }
    
    public PstLevel(long lOid) throws DBException {        
        super(new PstLevel(0));        
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
        return TBL_HR_LEVEL;
    }
    
    public String[] getFieldNames() {        
        return fieldNames;        
    }
    
    public int[] getFieldTypes() {        
        return fieldTypes;        
    }
    
    public String getPersistentName() {        
        return new PstLevel().getClass().getName();        
    }
    
    public long fetchExc(Entity ent) throws Exception {        
        Level level = fetchExc(ent.getOID());        
        ent = (Entity) level;        
        return level.getOID();        
    }
    
    public long insertExc(Entity ent) throws Exception {        
        return insertExc((Level) ent);        
    }
    
    public long updateExc(Entity ent) throws Exception {        
        return updateExc((Level) ent);        
    }
    
    public long deleteExc(Entity ent) throws Exception {        
        if (ent == null) {            
            throw new DBException(this, DBException.RECORD_NOT_FOUND);            
        }        
        return deleteExc(ent.getOID());        
    }
    
    public static Level fetchExc(long oid) throws DBException {        
        try {            
            Level level = new Level();
            PstLevel pstLevel = new PstLevel(oid);            
            level.setOID(oid);
            
            level.setGroupRankId(pstLevel.getlong(FLD_GROUP_RANK_ID));
            level.setLevel(pstLevel.getString(FLD_LEVEL));
            level.setDescription(pstLevel.getString(FLD_DESCRIPTION));
            level.setEmployeeMedicalId(pstLevel.getlong(FLD_EMP_MEDIC_LEVEL));
            level.setFamilyMedicalId(pstLevel.getlong(FLD_FMLY_MEDIC_LEVEL));
            level.setFamilyMedicalId(pstLevel.getlong(FLD_GRADE_LEVEL_ID));
            level.setLevelPoint(pstLevel.getInt(FLD_LEVEL_POINT));
            level.setCode(pstLevel.getString(FLD_CODE));
            level.setLevelRank(pstLevel.getInt(FLD_LEVEL_RANK));
            level.setEntitleDP(pstLevel.getInt(FLD_ENTITLE_DP));
            level.setEntitledDPQty(pstLevel.getInt(FLD_ENTITLED_DP_QTY));
            level.setEntitlePH(pstLevel.getInt(FLD_ENTITLE_PH));
            level.setEntitledPHQty(pstLevel.getInt(FLD_ENTITLED_PH_QTY));
            
            return level;            
        } catch (DBException dbe) {            
            throw dbe;            
        } catch (Exception e) {            
            throw new DBException(new PstLevel(0), DBException.UNKNOWN);            
        }        
    }
    
    public static long insertExc(Level level) throws DBException {        
        try {            
            PstLevel pstLevel = new PstLevel(0);
            
            pstLevel.setLong(FLD_GROUP_RANK_ID, level.getGroupRankId());
            pstLevel.setString(FLD_LEVEL, level.getLevel());
            pstLevel.setString(FLD_DESCRIPTION, level.getDescription());
            pstLevel.setLong(FLD_EMP_MEDIC_LEVEL, level.getEmployeeMedicalId());
            pstLevel.setLong(FLD_FMLY_MEDIC_LEVEL, level.getFamilyMedicalId());
            pstLevel.setLong(FLD_GRADE_LEVEL_ID, level.getGradeLevelId());
            pstLevel.setInt(FLD_LEVEL_POINT, level.getLevelPoint());
            pstLevel.setString(FLD_CODE, level.getCode());
            pstLevel.setInt(FLD_LEVEL_RANK, level.getLevelRank());
            pstLevel.setInt(FLD_ENTITLE_DP, level.getEntitleDP());
            pstLevel.setInt(FLD_ENTITLED_DP_QTY, level.getEntitledDPQty());
            pstLevel.setInt(FLD_ENTITLE_PH, level.getEntitlePH());
            pstLevel.setInt(FLD_ENTITLED_PH_QTY, level.getEntitledPHQty());
            pstLevel.insert();            
            level.setOID(pstLevel.getlong(FLD_LEVEL_ID));
        } catch (DBException dbe) {            
            throw dbe;            
        } catch (Exception e) {            
            throw new DBException(new PstLevel(0), DBException.UNKNOWN);            
        }
        return level.getOID();
    }
    
    public static long updateExc(Level level) throws DBException {        
        try {            
            if (level.getOID() != 0) {                
                PstLevel pstLevel = new PstLevel(level.getOID());
                
                pstLevel.setLong(FLD_GROUP_RANK_ID, level.getGroupRankId());
                pstLevel.setString(FLD_LEVEL, level.getLevel());
                pstLevel.setString(FLD_DESCRIPTION, level.getDescription());
                pstLevel.setLong(FLD_EMP_MEDIC_LEVEL, level.getEmployeeMedicalId());
                pstLevel.setLong(FLD_FMLY_MEDIC_LEVEL, level.getFamilyMedicalId());
                pstLevel.setLong(FLD_GRADE_LEVEL_ID, level.getGradeLevelId());
                pstLevel.setInt(FLD_LEVEL_POINT, level.getLevelPoint());
                pstLevel.setString(FLD_CODE, level.getCode());
                pstLevel.setInt(FLD_LEVEL_RANK, level.getLevelRank());
                pstLevel.setInt(FLD_ENTITLE_DP, level.getEntitleDP());
                pstLevel.setInt(FLD_ENTITLED_DP_QTY, level.getEntitledDPQty());
                pstLevel.setInt(FLD_ENTITLE_PH, level.getEntitlePH());
                pstLevel.setInt(FLD_ENTITLED_PH_QTY, level.getEntitledPHQty());
                pstLevel.update();                
                return level.getOID();
                
            }
        } catch (DBException dbe) {            
            throw dbe;            
        } catch (Exception e) {            
            throw new DBException(new PstLevel(0), DBException.UNKNOWN);            
        }
        return 0;
    }
    
    public static long deleteExc(long oid) throws DBException {        
        try {            
            PstLevel pstLevel = new PstLevel(oid);
            pstLevel.delete();
        } catch (DBException dbe) {            
            throw dbe;            
        } catch (Exception e) {            
            throw new DBException(new PstLevel(0), DBException.UNKNOWN);            
        }
        return oid;
    }
    
    public static Vector listAll() {        
        return list(0, 500, "", "");        
    }
    
    public static String[] listNameLevel(int limitStart, int recordToGet, String whereClause, String order) {
        String[] listLevel = null;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_LEVEL;            
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
            String strLevel = "";
            while (rs.next()) {
                strLevel = strLevel + rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]) + ",";
            }
            if (strLevel != null && strLevel.length() > 0) {
                strLevel = strLevel.substring(0, strLevel.length() - 1);
                listLevel = strLevel.split(",");
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
            return listLevel;
        }
    }
    
    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();        
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_LEVEL;            
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
                Level level = new Level();
                resultToObject(rs, level);
                lists.add(level);
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

    public static Hashtable<String, Level> toHashtable(Vector<Level> vList){
        Hashtable<String, Level>  hTbl = new Hashtable<String, Level>();
        if(vList!=null && vList.size()>0){
           for(int idx=0;idx < vList.size() ; idx++){
               Level level = vList.get(idx);
               hTbl.put(""+level.getOID(), level);
           }
        }
        return(hTbl);
    }
    
    
    /**
     * create by satrya 2013-07-04
     *
     * @return
     */
    public static Hashtable hashlistLevel() {
        Hashtable hashListLeavel = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_LEVEL;            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Level level = new Level();
                resultToObject(rs, level);
                hashListLeavel.put(level.getOID(), level);
            }
            rs.close();
            
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashListLeavel;
    }

    public static Hashtable hashlistLevelStringOid() {
        Hashtable hashListLeavel = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_LEVEL;            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Level level = new Level();
                resultToObject(rs, level);
                hashListLeavel.put(""+level.getOID(), level);
            }
            rs.close();
            
            
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return hashListLeavel;
    }

    public static void resultToObject(ResultSet rs, Level level) {
        try {
            level.setOID(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]));
            level.setGroupRankId(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_GROUP_RANK_ID]));
            level.setLevel(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]));
            level.setDescription(rs.getString(PstLevel.fieldNames[PstLevel.FLD_DESCRIPTION]));
            level.setEmployeeMedicalId(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_EMP_MEDIC_LEVEL]));;
            level.setFamilyMedicalId(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_FMLY_MEDIC_LEVEL]));
            level.setGradeLevelId(rs.getLong(PstLevel.fieldNames[PstLevel.FLD_GRADE_LEVEL_ID]));
            level.setLevelPoint(rs.getInt(PstLevel.fieldNames[PstLevel.FLD_LEVEL_POINT]));
            level.setCode(rs.getString(PstLevel.fieldNames[PstLevel.FLD_CODE]));
            level.setLevelRank(rs.getInt(PstLevel.fieldNames[PstLevel.FLD_LEVEL_RANK]));
            level.setEntitleDP(rs.getInt(PstLevel.fieldNames[PstLevel.FLD_ENTITLE_DP]));
            level.setEntitledDPQty(rs.getInt(PstLevel.fieldNames[PstLevel.FLD_ENTITLED_DP_QTY]));
            level.setEntitlePH(rs.getInt(PstLevel.fieldNames[PstLevel.FLD_ENTITLE_PH]));
            level.setEntitledPHQty(rs.getInt(PstLevel.fieldNames[PstLevel.FLD_ENTITLED_PH_QTY]));
            
        } catch (Exception e) {
        }
    }
    
    public static boolean checkOID(long levelId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_LEVEL + " WHERE "
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " = '" + levelId + "'";
            
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
            String sql = "SELECT COUNT(" + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + ") FROM " + TBL_HR_LEVEL;
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


    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String order) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);            
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {                    
                    Level level = (Level) list.get(ls);
                    if (oid == level.getOID()) {
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
    
    public static boolean checkGroupRank(long groupRankId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_LEVEL + " WHERE "
                    + PstLevel.fieldNames[PstLevel.FLD_GROUP_RANK_ID] + " = '" + groupRankId + "'";
            
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
    
    public static boolean checkMaster(long oid) {
        if (PstEmployee.checkLevel(oid)) {
            return true;
        } else {
            return false;
        }
    }
}
