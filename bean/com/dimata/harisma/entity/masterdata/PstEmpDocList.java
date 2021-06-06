/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.harisma.entity.configrewardnpunisment.PstRewardAndPunishmentDetail;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.lang.I_Language;
import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author GUSWIK
 */
public class PstEmpDocList extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

   public static final String TBL_HR_EMP_DOC_LIST = "hr_emp_doc_list";
   public static final int FLD_EMP_DOC_LIST_ID = 0;
   public static final int FLD_EMP_DOC_ID = 1;
   public static final int FLD_EMPLOYEE_ID = 2;
   public static final int FLD_ASSIGN_AS = 3;
   public static final int FLD_JOB_DESC = 4;
   public static final int FLD_OBJECT_NAME = 5;
   
    public static final String[] fieldNames = {
      "EMP_DOC_LIST_ID",
      "EMP_DOC_ID",
      "EMPLOYEE_ID",
      "ASSIGN_AS",
      "JOB_DESC",
      "OBJECT_NAME"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

   public PstEmpDocList() {
   }

    public PstEmpDocList(int i) throws DBException {
        super(new PstEmpDocList());
    }

    public PstEmpDocList(String sOid) throws DBException {
        super(new PstEmpDocList(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstEmpDocList(long lOid) throws DBException {
        super(new PstEmpDocList(0));
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
        return TBL_HR_EMP_DOC_LIST;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstEmpDocList().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        EmpDocList empDocList = fetchExc(ent.getOID());
        ent = (Entity) empDocList;
        return empDocList.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((EmpDocList) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((EmpDocList) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static EmpDocList fetchExc(long oid) throws DBException {
        try {
            EmpDocList empDocList = new EmpDocList();
            PstEmpDocList pstEmpDocList = new PstEmpDocList(oid);
            empDocList.setOID(oid);

            empDocList.setEmp_doc_id(pstEmpDocList.getLong(FLD_EMP_DOC_ID));
            empDocList.setEmployee_id(pstEmpDocList.getLong(FLD_EMPLOYEE_ID));
            empDocList.setAssign_as(pstEmpDocList.getString(FLD_ASSIGN_AS));
            empDocList.setJob_desc(pstEmpDocList.getString(FLD_JOB_DESC));
            empDocList.setObject_name(pstEmpDocList.getString(FLD_OBJECT_NAME));

            return empDocList;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpDocList(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(EmpDocList empDocList) throws DBException {
        try {
            PstEmpDocList pstEmpDocList = new PstEmpDocList(0);
            pstEmpDocList.setLong(FLD_EMP_DOC_ID, empDocList.getEmp_doc_id());
            pstEmpDocList.setLong(FLD_EMPLOYEE_ID, empDocList.getEmployee_id());
            pstEmpDocList.setString(FLD_ASSIGN_AS, empDocList.getAssign_as());
            pstEmpDocList.setString(FLD_JOB_DESC, empDocList.getJob_desc());
            pstEmpDocList.setString(FLD_OBJECT_NAME, empDocList.getObject_name());
          
            pstEmpDocList.insert();
            empDocList.setOID(pstEmpDocList.getlong(FLD_EMP_DOC_LIST_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpDocList(0), DBException.UNKNOWN);
        }
        return empDocList.getOID();
    }

    public static long updateExc(EmpDocList empDocList) throws DBException {
        try {
            if (empDocList.getOID() != 0) {
                PstEmpDocList pstEmpDocList = new PstEmpDocList(empDocList.getOID());

                 pstEmpDocList.setLong(FLD_EMP_DOC_ID, empDocList.getEmp_doc_id());
                 pstEmpDocList.setLong(FLD_EMPLOYEE_ID, empDocList.getEmployee_id());
                 pstEmpDocList.setString(FLD_ASSIGN_AS, empDocList.getAssign_as());
                 pstEmpDocList.setString(FLD_JOB_DESC, empDocList.getJob_desc());
                 pstEmpDocList.setString(FLD_OBJECT_NAME, empDocList.getObject_name());

                pstEmpDocList.update();
                return empDocList.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpDocList(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstEmpDocList pstEmpDocList = new PstEmpDocList(oid);
            pstEmpDocList.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstEmpDocList(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMP_DOC_LIST;
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
                EmpDocList empDocList = new EmpDocList();
                
            empDocList.setOID(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_LIST_ID]));
            empDocList.setEmp_doc_id(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_ID]));
            empDocList.setEmployee_id(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMPLOYEE_ID]));
            empDocList.setAssign_as(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_ASSIGN_AS]));
            empDocList.setJob_desc(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_JOB_DESC]));
            empDocList.setObject_name(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_OBJECT_NAME]));
                //resultToObject(rs, empDocList);
                lists.add(empDocList);
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
    
   public static Vector listObject(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DISTINCT(EDL."+ PstEmpDocList.fieldNames[PstEmpDocList.FLD_OBJECT_NAME] +"),EDL.* FROM " + TBL_HR_EMP_DOC_LIST + " EDL ";
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
            
                EmpDocList empDocList = new EmpDocList();
                
            empDocList.setOID(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_LIST_ID]));
            empDocList.setEmp_doc_id(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_ID]));
            empDocList.setEmployee_id(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMPLOYEE_ID]));
            empDocList.setAssign_as(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_ASSIGN_AS]));
            empDocList.setJob_desc(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_JOB_DESC]));
            empDocList.setObject_name(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_OBJECT_NAME]));
                //resultToObject(rs, empDocList);
                lists.add(empDocList);
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
    
    
      public static void resultToObject(ResultSet rs, EmpDocList empDocList) {
        try {
            empDocList.setOID(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_LIST_ID]));
            empDocList.setEmp_doc_id(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_ID]));
            empDocList.setEmployee_id(rs.getLong(PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMPLOYEE_ID]));
            empDocList.setAssign_as(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_ASSIGN_AS]));
            empDocList.setJob_desc(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_JOB_DESC]));
            empDocList.setObject_name(rs.getString(PstEmpDocList.fieldNames[PstEmpDocList.FLD_OBJECT_NAME]));
            
        } catch (Exception e) {
        }
    }
    
    public static boolean checkOID(long empDocListId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_EMP_DOC_LIST + " WHERE "
                    + PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_LIST_ID] + " = " + empDocListId;

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
            String sql = "SELECT COUNT(" + PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_LIST_ID] + ") FROM " + TBL_HR_EMP_DOC_LIST;
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

public static long deletewhere(long employeeId, long empDocId, String objekName) {
        DBResultSet dbrs = null;
        long resulthasil =0;
        try {
            String sql = "DELETE  FROM " + TBL_HR_EMP_DOC_LIST + " WHERE "
                    + PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMP_DOC_ID] + " = " + empDocId
                    + " AND "+ PstEmpDocList.fieldNames[PstEmpDocList.FLD_EMPLOYEE_ID] + " = " + employeeId
                    + " AND "+ PstEmpDocList.fieldNames[PstEmpDocList.FLD_OBJECT_NAME] + " = \"" + objekName+"\"";
            
            DBHandler.execSqlInsert(sql);
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
            
        } finally {
            DBResultSet.close(dbrs);
            return resulthasil;
        }
    }
    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause) {
        String order = "";
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, order);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    EmpDocList empDocList = (EmpDocList) list.get(ls);
                    if (oid == empDocList.getOID()) {
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

  
}
