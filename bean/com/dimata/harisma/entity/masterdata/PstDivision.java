
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */
/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/
package com.dimata.harisma.entity.masterdata;

/* package java */
import java.sql.*;
import java.util.*;


/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;


import com.dimata.harisma.entity.overtime.*;


import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.masterdata.sessdivision.SessDivision;
import com.dimata.harisma.entity.outsource.PstOutSourceEvaluation;
import com.dimata.harisma.entity.outsource.PstOutSourceEvaluationProvider;
import com.dimata.harisma.entity.outsource.PstOutSourcePlan;
import com.dimata.harisma.entity.outsource.PstOutSourcePlanLocation;
import com.dimata.harisma.entity.outsource.SrcObject;
import com.dimata.harisma.entity.payroll.PstPayGeneral;


public class PstDivision extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_HR_DIVISION = "hr_division";//"HR_DIVISION";
    public static final int FLD_DIVISION_ID = 0;
    public static final int FLD_DIVISION = 1;
    public static final int FLD_DESCRIPTION = 2;
    public static final int FLD_COMPANY_ID = 3;
    public static final int FLD_DIVISION_TYPE_ID = 4;
    /* addd by Kartika ; 2015-08-08 */
    public static final int FLD_ADDRESS = 5;
    public static final int FLD_CITY = 6;
    public static final int FLD_NPWP = 7;
    public static final int FLD_PROVINCE = 8;
    public static final int FLD_REGION = 9;
    public static final int FLD_SUB_REGION = 10;
    public static final int FLD_VILLAGE = 11;
    public static final int FLD_AREA = 12;
    public static final int FLD_TELEPHONE = 13;
    public static final int FLD_FAX_NUMBER = 14;
    public static final int FLD_VALID_STATUS = 15;
    public static final int FLD_VALID_START = 16;
    public static final int FLD_VALID_END = 17;
    public static final int FLD_PEMOTONG = 18;
    /* Update by Hendra Putu | 2016-09-27 | emp yg menanda tangani slip gaji */
    public static final int FLD_EMPLOYEE_ID = 19;
    
    public static final String[] fieldNames = {
        "DIVISION_ID",
        "DIVISION",
        "DESCRIPTION",
        "COMPANY_ID",
        "DIVISION_TYPE_ID",
         "ADDRESS",
         "CITY",
         "NPWP",
         "PROVINCE",
         "REGION",
         "SUB_REGION",
         "VILLAGE",
         "AREA",
         "TELEPHONE",
         "FAX_NUMBER",
         "VALID_STATUS",
         "VALID_START",
         "VALID_END",
         "PEMOTONG",
         "EMPLOYEE_ID"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
         TYPE_STRING,
         TYPE_STRING,
         TYPE_STRING,
         TYPE_STRING,
         TYPE_STRING,
         TYPE_STRING,
         TYPE_STRING,
         TYPE_INT,
         TYPE_DATE,
         TYPE_DATE,
         TYPE_STRING,
         TYPE_LONG
    };
    
    public static int[] valueOfType = {0,1,2,3};
    public static String[] captionOfType = {"Regular", "Branch", "BOD", "Shareholder"};

    public static final int VALID_ACTIVE = 1;
    public static final int VALID_HISTORY = 0;
    
    public static final String[] validStatusValue = {
         "HISTORY","ACTIVE"
    };

    public PstDivision() {
    }

    public PstDivision(int i) throws DBException {
        super(new PstDivision());
    }

    public PstDivision(String sOid) throws DBException {
        super(new PstDivision(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstDivision(long lOid) throws DBException {
        super(new PstDivision(0));
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
        return TBL_HR_DIVISION;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstDivision().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        Division division = fetchExc(ent.getOID());
        ent = (Entity) division;
        return division.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((Division) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((Division) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    //priska 20161003
     public static long getDivisionIdByName(String Name ) {
        
        long result = 0;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +
                    " FROM " + TBL_HR_DIVISION +
                    " WHERE " +  PstDivision.fieldNames[PstDivision.FLD_DIVISION] +
                    " = \"" + Name+ "\"";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }
    
    public static Division fetchExc(long oid) throws DBException {
        try {
            Division division = new Division();
            PstDivision pstDivision = new PstDivision(oid);
            division.setOID(oid);

            division.setDivision(pstDivision.getString(FLD_DIVISION));
            division.setDescription(pstDivision.getString(FLD_DESCRIPTION));
            division.setCompanyId(pstDivision.getlong(FLD_COMPANY_ID));
            division.setDivisionTypeId(pstDivision.getlong(FLD_DIVISION_TYPE_ID));
            /* add by Kartika ; 2015-08-08 */
            division.setAddress(pstDivision.getString(FLD_ADDRESS));                         
            division.setCity(pstDivision.getString(FLD_CITY));                         
            division.setNpwp(pstDivision.getString(FLD_NPWP));                         
            division.setProvince(pstDivision.getString(FLD_PROVINCE));                         
            division.setRegion(pstDivision.getString(FLD_REGION));                         
            division.setSubRegion(pstDivision.getString(FLD_SUB_REGION));                         
            division.setVillage(pstDivision.getString(FLD_VILLAGE));                         
            division.setArea(pstDivision.getString(FLD_AREA));                         
            division.setTelphone(pstDivision.getString(FLD_TELEPHONE));                         
            division.setFaxNumber(pstDivision.getString(FLD_FAX_NUMBER));                         
            division.setValidStatus(pstDivision.getInt(FLD_VALID_STATUS));
            division.setValidStart(pstDivision.getDate(FLD_VALID_START));
            division.setValidEnd(pstDivision.getDate(FLD_VALID_END));
            division.setPemotong(pstDivision.getString(FLD_PEMOTONG));
            division.setEmployeeId(pstDivision.getLong(FLD_EMPLOYEE_ID));
            /* end */
            return division;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDivision(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(Division division) throws DBException {
        try {
            PstDivision pstDivision = new PstDivision(0);

            pstDivision.setString(FLD_DIVISION, division.getDivision());
            pstDivision.setString(FLD_DESCRIPTION, division.getDescription());
            pstDivision.setLong(FLD_COMPANY_ID, division.getCompanyId());
            pstDivision.setLong(FLD_DIVISION_TYPE_ID, division.getDivisionTypeId());
            /* add by Kartika ; 2015-08-08 */
            pstDivision.setString(FLD_ADDRESS,division.getAddress());                         
            pstDivision.setString(FLD_CITY, division.getCity());                         
            pstDivision.setString(FLD_NPWP,division.getNpwp());                         
            pstDivision.setString(FLD_PROVINCE,division.getProvince());                         
            pstDivision.setString(FLD_REGION,division.getRegion());                         
            pstDivision.setString(FLD_SUB_REGION, division.getSubRegion());                         
            pstDivision.setString(FLD_VILLAGE, division.getVillage());                         
            pstDivision.setString(FLD_AREA, division.getArea());                         
            pstDivision.setString(FLD_TELEPHONE, division.getTelphone());                         
            pstDivision.setString(FLD_FAX_NUMBER, division.getFaxNumber());                         
            pstDivision.setInt(FLD_VALID_STATUS, division.getValidStatus());
            pstDivision.setDate(FLD_VALID_START, division.getValidStart());
            pstDivision.setDate(FLD_VALID_END, division.getValidEnd());
            pstDivision.setString(FLD_PEMOTONG, division.getPemotong());
            pstDivision.setLong(FLD_EMPLOYEE_ID, division.getEmployeeId());
            /* end */
            
            pstDivision.insert();
            division.setOID(pstDivision.getlong(FLD_DIVISION_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDivision(0), DBException.UNKNOWN);
        }
        return division.getOID();
    }

    public static long updateExc(Division division) throws DBException {
        try {
            if (division.getOID() != 0) {
                PstDivision pstDivision = new PstDivision(division.getOID());

                pstDivision.setString(FLD_DIVISION, division.getDivision());
                pstDivision.setString(FLD_DESCRIPTION, division.getDescription());
                pstDivision.setLong(FLD_COMPANY_ID, division.getCompanyId());
                pstDivision.setLong(FLD_DIVISION_TYPE_ID, division.getDivisionTypeId());
            /* add by Kartika ; 2015-08-08 */
            pstDivision.setString(FLD_ADDRESS,division.getAddress());                         
            pstDivision.setString(FLD_CITY, division.getCity());                         
            pstDivision.setString(FLD_NPWP,division.getNpwp());                         
            pstDivision.setString(FLD_PROVINCE,division.getProvince());                         
            pstDivision.setString(FLD_REGION,division.getRegion());                         
            pstDivision.setString(FLD_SUB_REGION, division.getSubRegion());                         
            pstDivision.setString(FLD_VILLAGE, division.getVillage());                         
            pstDivision.setString(FLD_AREA, division.getArea());                         
            pstDivision.setString(FLD_TELEPHONE, division.getTelphone());                         
            pstDivision.setString(FLD_FAX_NUMBER, division.getFaxNumber());                         
            pstDivision.setInt(FLD_VALID_STATUS, division.getValidStatus());
            pstDivision.setDate(FLD_VALID_START, division.getValidStart());
            pstDivision.setDate(FLD_VALID_END, division.getValidEnd());
            pstDivision.setString(FLD_PEMOTONG, division.getPemotong());
            pstDivision.setLong(FLD_EMPLOYEE_ID, division.getEmployeeId());
            /* end */

                pstDivision.update();
                return division.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDivision(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstDivision pstDivision = new PstDivision(oid);
            pstDivision.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstDivision(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_HR_DIVISION;
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

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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
    
    
    public static Vector listJoinOutSourceEval(int limitStart, int recordToGet, String order,SrcObject srcObject) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            
            String sql = " SELECT DISTINCT(hd."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+"),hd.*  FROM "+TBL_HR_DIVISION+" AS hd "+
                         " INNER JOIN "+ PstOutSourceEvaluation.TBL_OUTSOURCEEVALUATION+" AS ht ON hd."+ PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+" = ht."+ PstOutSourceEvaluation.fieldNames[PstOutSourceEvaluation.FLD_DIVISION_ID]+
                         " INNER JOIN "+ PstOutSourceEvaluationProvider.TBL_OUT_SOURCE_EVAL_PROVIDER+" AS osep ON osep."+ PstOutSourceEvaluationProvider.fieldNames[PstOutSourceEvaluationProvider.FLD_OUT_SOURCE_EVAL_ID]+" = ht."+ PstOutSourceEvaluation.fieldNames[PstOutSourceEvaluation.FLD_OUTSOURCE_EVAL_ID];
            if (srcObject != null ) {
                sql = sql + " WHERE 1=1 " ;
                
                if (srcObject.getDivisiId() > 0 ){
                    sql = sql + " AND hd." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " = " + srcObject.getDivisiId() ;
                }
                 if (srcObject.getPayPeriodId().length() > 0 ){
                    String periodeIn =  srcObject.getPayPeriodId().substring(0,srcObject.getPayPeriodId().length() - 1);
                    sql = sql + " AND ht." + PstOutSourceEvaluation.fieldNames[PstOutSourceEvaluation.FLD_PERIOD_ID] + " IN ( " + periodeIn + " ) " ;
                }
                if (srcObject.getProviderId() > 0 ){
                    sql = sql + " AND osep." + PstOutSourceEvaluationProvider.fieldNames[PstOutSourceEvaluationProvider.FLD_PROVIDER_ID] + " = " + srcObject.getProviderId() ;
                }
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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

    
    public static Vector listJoinOutSourcePlanLoc(int limitStart, int recordToGet, String order,SrcObject srcObject) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            
            String sql = " SELECT DISTINCT(hd."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+"),hd.*  FROM "+TBL_HR_DIVISION+" AS hd "+
                         " INNER JOIN "+ PstOutSourcePlanLocation.TBL_OUTSOURCEPLANLOCATION+" AS ospl ON hd."+ PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+" = ospl."+ PstOutSourcePlanLocation.fieldNames[PstOutSourcePlanLocation.FLD_DIVISIONID];
                         //" INNER JOIN "+ PstOutSourceEvaluationProvider.TBL_OUT_SOURCE_EVAL_PROVIDER+" AS osep ON osep."+ PstOutSourceEvaluationProvider.fieldNames[PstOutSourceEvaluationProvider.FLD_OUT_SOURCE_EVAL_ID]+" = ht."+ PstOutSourceEvaluation.fieldNames[PstOutSourceEvaluation.FLD_OUTSOURCE_EVAL_ID];
            if (srcObject != null ) {
                sql = sql + " WHERE 1=1 " ;
                
                if (srcObject.getDivisiId() > 0 ){
                    sql = sql + " AND hd." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " = " + srcObject.getDivisiId() ;
                }
                
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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
    
    public static Vector listJoin(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            
            String sql = " SELECT hd.*  FROM "+TBL_HR_DIVISION+" AS hd "+
                         " INNER JOIN "+ PstDivisionType.TBL_DIVISION_TYPE+" AS ht ON hd."+ PstDivisionType.fieldNames[PstDivisionType.FLD_DIVISION_TYPE_ID]+" = ht."+fieldNames[FLD_DIVISION_TYPE_ID];
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

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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
    
    public static Hashtable<String, Division> listMap(int limitStart, int recordToGet, String whereClause, String order) {
        Hashtable<String, Division> lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_DIVISION;
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

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.put(""+division.getOID(), division);
            }
            rs.close();
            return lists;

        } catch (Exception e) {

            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }
    
    
    public static Hashtable listMapDivisionName(int limitStart, int recordToGet, String whereClause, String order) {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_DIVISION;
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

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.put(division.getOID(), division.getDivision());
            }
            rs.close();
            return lists;

        } catch (Exception e) {

            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }
    
    
    public static Vector listwithTypeDivision(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT HD.* FROM " + TBL_HR_DIVISION + " HD ";
             sql = sql + " INNER JOIN "+PstDivisionType.TBL_DIVISION_TYPE+" AS HDT ON HDT."+PstDivisionType.fieldNames[PstDivisionType.FLD_DIVISION_TYPE_ID]+" = HD."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_TYPE_ID]+" " ;
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

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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
    
    /**
     * list Division hashtable
     * @param limitStart
     * @param recordToGet
     * @param whereClause
     * @return 
     */
     public static Hashtable hashListDivision(int limitStart, int recordToGet, String whereClause) {
        DBResultSet dbrs = null;
        Hashtable hashListDivision= new Hashtable();
        try {
            String sql = "SELECT * FROM " + TBL_HR_DIVISION;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
                sql = sql + " ORDER BY " + fieldNames[FLD_COMPANY_ID]+","+fieldNames[FLD_DIVISION]+" ASC ";
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            SessDivision sessDivision = new SessDivision();
            long prevCompany=0;
            while (rs.next()) {
                
                  Division division = new Division();
                long oidCompanyId = rs.getLong(fieldNames[FLD_COMPANY_ID]);
                if(prevCompany!=oidCompanyId){
                    sessDivision = new SessDivision();
                    hashListDivision.put(oidCompanyId, sessDivision);
                    
                }
                
                resultToObject(rs, division);
                prevCompany=division.getCompanyId();
                sessDivision.addObjDivision(division);
            }
            rs.close();
           

        } catch (Exception e) {

            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
            return hashListDivision;
        }
    }
 /**
 * Pencarian list division by depertment
 * create by satrya 2013-04-20
 * @param limitStart
 * @param recordToGet
 * @param where
 * @param order
 * @return 
 */
    public static Vector listDivisionByDept(int limitStart, int recordToGet,String where, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT divX.*"
                    //+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+",divX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION]
                   //+ ",dept."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+",dept."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] +
                    + " FROM "+ PstDivision.TBL_HR_DIVISION + " AS DIVX "
+ " INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT ON DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "=dept."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID];
            if (where != null && where.length() > 0) {
                sql = sql + " WHERE " + where;
            }        
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }

            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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

    public static void resultToObject(ResultSet rs, Division division) {
        try {
            division.setOID(rs.getLong(PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]));
            division.setDivision(rs.getString(PstDivision.fieldNames[PstDivision.FLD_DIVISION]));
            division.setDescription(rs.getString(PstDivision.fieldNames[PstDivision.FLD_DESCRIPTION]));
            division.setCompanyId(rs.getLong(PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]));
            division.setDivisionTypeId(rs.getLong(PstDivision.fieldNames[PstDivision.FLD_DIVISION_TYPE_ID]));
            /* add by Kartika ; 2015-08-08 */
            division.setAddress(rs.getString(PstDivision.fieldNames[PstDivision.FLD_ADDRESS]));                         
            division.setCity(rs.getString(PstDivision.fieldNames[PstDivision.FLD_CITY]));                         
            division.setNpwp(rs.getString(PstDivision.fieldNames[PstDivision.FLD_NPWP]));                         
            division.setProvince(rs.getString(PstDivision.fieldNames[PstDivision.FLD_PROVINCE]));                         
            division.setRegion(rs.getString(PstDivision.fieldNames[PstDivision.FLD_REGION]));                         
            division.setSubRegion(rs.getString(PstDivision.fieldNames[PstDivision.FLD_SUB_REGION]));                         
            division.setVillage(rs.getString(PstDivision.fieldNames[PstDivision.FLD_VILLAGE]));                         
            division.setArea(rs.getString(PstDivision.fieldNames[PstDivision.FLD_AREA]));                         
            division.setTelphone(rs.getString(PstDivision.fieldNames[PstDivision.FLD_TELEPHONE]));                         
            division.setFaxNumber(rs.getString(PstDivision.fieldNames[PstDivision.FLD_FAX_NUMBER]));                         
            division.setValidStatus(rs.getInt(PstDivision.fieldNames[PstDivision.FLD_VALID_STATUS]));
            division.setValidStart(rs.getDate(PstDivision.fieldNames[PstDivision.FLD_VALID_START]));
            division.setValidEnd(rs.getDate(PstDivision.fieldNames[PstDivision.FLD_VALID_END]));
            division.setPemotong(rs.getString(PstDivision.fieldNames[PstDivision.FLD_PEMOTONG]));
            division.setEmployeeId(rs.getLong(PstDivision.fieldNames[PstDivision.FLD_EMPLOYEE_ID]));
            /* end */

        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long positionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_DIVISION + " WHERE "
                    + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " = " + positionId;

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
            String sql = "SELECT COUNT(" + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + ") FROM " + TBL_HR_DIVISION;
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
                    Division division = (Division) list.get(ls);
                    if (oid == division.getOID()) {
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

    public static boolean checkMaster(long oid) {
        if (PstEmployee.checkPosition(oid)) {
            return true;
        } else {
            if (PstCareerPath.checkPosition(oid)) {
                return true;
            } else {
                return false;
            }

        }
    }

    //Gede_1Maret2012 { belum di pake
    public static Vector<Long> getDivId(long oidCompany) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " FROM " + PstDivision.TBL_HR_DIVISION
                    + " WHERE " + PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" + oidCompany;
            System.out.println("SQL " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector divId = new Vector();
            while (rs.next()) {
                divId.add(rs.getLong(1));
            }

            rs.close();
            return divId;
        } catch (Exception e) {
            System.out.println("exception " + e);

        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //for structure employee
    public static Vector list2(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT d.* FROM " + PstDivision.TBL_HR_DIVISION
                    + " d INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE
                    + " emp ON d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "=emp." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION
                    + " pos ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=pos." + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID];
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            sql = sql + " GROUP BY " + PstDivision.fieldNames[PstDivision.FLD_DIVISION];
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }


            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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
//}

    //Gede_27MARET2012
    public static long getId(String whereClause) {
        DBResultSet dbrs = null;
        long result = 0;
        try {
            String sql = "SELECT d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + " FROM " + PstDepartment.TBL_HR_DEPARTMENT + " dep INNER JOIN "
                    + PstDivision.TBL_HR_DIVISION + " d ON dep." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID];

            sql = sql + whereClause;

            System.out.println("SQL " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getLong(1);
            }
            rs.close();

        } catch (Exception e) {
            System.out.println("exception " + e);
            return 0;

        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    //}

    //Gede_2April2012
    //{
    public static Vector list3(int limitStart, int recordToGet, String whereClauseDiv, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT d.* FROM " + PstDivision.TBL_HR_DIVISION +
            " as d INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " as emp ON d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "= emp." +
            PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " as dep ON emp." +
            PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "= dep." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
            " INNER JOIN " + PstCompany.TBL_HR_COMPANY + " as c ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + "= c." + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +
            " INNER JOIN " + PstReligion.TBL_HR_RELIGION + " as r ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID] + "= r." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID] + 
            " INNER JOIN " + PstOvertimeDetail.TBL_OVERTIME_DETAIL + " as odt ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "= odt." + PstOvertimeDetail.fieldNames[PstOvertimeDetail.FLD_EMPLOYEE_ID] +
            " INNER JOIN " + PstOvertime.TBL_OVERTIME + " as o ON odt." + PstOvertimeDetail.fieldNames[PstOvertimeDetail.FLD_OVERTIME_ID] + "= o." + PstOvertime.fieldNames[PstOvertime.FLD_OVERTIME_ID];
            if (whereClauseDiv != null && whereClauseDiv.length() > 0) {
                sql = sql + " WHERE " + whereClauseDiv;
            }
            sql = sql + " GROUP BY " + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID];
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }


            //System.out.println("sql xxxxx " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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
//}
    
     //create by satrya 2013-08-23
   /**
    * 
    * @param whereClause
    * @param order
    * @return 
    */
  public static long getOidCompany(String whereClause, String order){
		long oidCompany = 0; 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_DIVISION; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			
			dbrs = DBHandler.execQueryResult(sql);
                       // System.out.println("SQL LIST"+sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				
				oidCompany = rs.getLong(PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]);
			}
			rs.close();
			return oidCompany;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
                        return oidCompany;
		}
			
	}
 /**
  * create by satrya 2013-09-11
  * menampilkan division
  * @param limitStart
  * @param recordToGet
  * @param whereClause
  * @param order
  * @return 
  */
  public static Vector listCompanyByDivisionIdAndDepartMent(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        
        try {
            String sql = " SELECT DIVX.* FROM " +PstCompany.TBL_HR_COMPANY + " AS COMP "  
        + " INNER JOIN "+PstDivision.TBL_HR_DIVISION 
        + " AS DIVX ON DIVX."+PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]
        +"=COMP."+PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]
        +" INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT ON DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID]
        +"=DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID];
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            
            sql = sql + " GROUP BY " + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID];
            
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
                Division division = new Division();
                resultToObject(rs, division);
                lists.add(division);
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
  
  public static String getDivisionName(long divId){
		long oidCompany = 0; 
                String divisionName = "";
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_DIVISION 
			+ " WHERE " + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]
                        + " = " + divId;
			
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				divisionName = rs.getString(PstDivision.fieldNames[PstDivision.FLD_DIVISION]);
			}
			rs.close();
			return divisionName;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
                        return divisionName;
		}
			
	}
  
  public static String getDivisionName(String whereClause, String order){
		long oidCompany = 0; 
                String divisionName = "";
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_DIVISION; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			
			dbrs = DBHandler.execQueryResult(sql);
                       // System.out.println("SQL LIST"+sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				
				divisionName = rs.getString(PstDivision.fieldNames[PstDivision.FLD_DIVISION]);
			}
			rs.close();
			return divisionName;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
                        return divisionName;
		}
			
	}
}
