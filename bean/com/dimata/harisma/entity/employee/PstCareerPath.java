
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */
/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/
package com.dimata.harisma.entity.employee;

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

//Gede_7Feb2012 {
import com.dimata.harisma.session.employee.*;
import com.dimata.harisma.entity.search.*;
import com.dimata.harisma.entity.payroll.*;
import com.dimata.harisma.entity.masterdata.*;
//}

/* package harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.masterdata.location.PstLocation;

/**
 * Ari_20111002
 * Menambah Company, Division, Level dan EmpCategory
 * @author Wiweka
 */
public class PstCareerPath extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_HR_WORK_HISTORY_NOW = "hr_work_history_now";//"HR_WORK_HISTORY_NOW";
    public static final int FLD_WORK_HISTORY_NOW_ID = 0;
    public static final int FLD_EMPLOYEE_ID = 1;
    public static final int FLD_COMPANY_ID = 2;
    public static final int FLD_COMPANY = 3;
    public static final int FLD_DEPARTMENT_ID = 4;
    public static final int FLD_DEPARTMENT = 5;
    public static final int FLD_POSITION_ID = 6;
    public static final int FLD_POSITION = 7;
    public static final int FLD_SECTION_ID = 8;
    public static final int FLD_SECTION = 9;
    public static final int FLD_DIVISION_ID = 10;
    public static final int FLD_DIVISION = 11;
    public static final int FLD_LEVEL_ID = 12;
    public static final int FLD_LEVEL = 13;
    public static final int FLD_EMP_CATEGORY_ID = 14;
    public static final int FLD_EMP_CATEGORY = 15;
    public static final int FLD_WORK_FROM = 16;
    public static final int FLD_WORK_TO = 17;
    public static final int FLD_DESCRIPTION = 18;
    public static final int FLD_SALARY = 19;
    //priska 2014-11-03
    public static final int FLD_LOCATION_ID = 20;
    public static final int FLD_LOCATION = 21;
    public static final int FLD_NOTE = 22;
        //kartika 2015-09-16
    public static final int FLD_PROVIDER_ID = 23;  // untuk karyawan yg outsource
    /* Update by Hendra Putu | 2015-10-09 */
    public static final int FLD_HISTORY_TYPE = 24;
    public static final int FLD_NOMOR_SK = 25;
    public static final int FLD_TANGGAL_SK = 26;
    public static final int FLD_EMP_DOC_ID = 27;
    public static final int FLD_HISTORY_GROUP = 28;
    /* Update by Hendra Putu | 2015-11-25 | GRADE_LEVEL_ID */
    public static final int FLD_GRADE_LEVEL_ID = 29;
    // Gunadi 2017-12-14
    public static final int FLD_MUTATION_TYPE = 30; // untuk jenis mutasi
    
    public static final String[] fieldNames = {
        "WORK_HISTORY_NOW_ID",
        "EMPLOYEE_ID",
        "COMPANY_ID",
        "COMPANY",
        "DEPARTMENT_ID",
        "DEPARTMENT",
        "POSITION_ID",
        "POSITION",
        "SECTION_ID",
        "SECTION",
        "DIVISION_ID",
        "DIVISION",
        "LEVEL_ID",
        "LEVEL",
        "EMP_CATEGORY_ID",
        "EMP_CATEGORY",
        "WORK_FROM",
        "WORK_TO",
        "DESCRIPTION",
        "SALARY",
        //2014-11-03
        "LOCATION_ID",
        "LOCATION",
        "NOTE",
        "PROVIDER_ID", // kartika 2015-09-16
        "HISTORY_TYPE",
        "NOMOR_SK",
        "TANGGAL_SK",
        "EMP_DOC_ID",
        "HISTORY_GROUP",
        "GRADE_LEVEL_ID",
        "MUTATION_TYPE"
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_FLOAT,
        //priska 2014-11-03
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_LONG + TYPE_FK,
        TYPE_INT,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_LONG + TYPE_FK,
        TYPE_INT,
        TYPE_LONG,
        TYPE_INT
    };
    
    public static String[] historyType = {"Career", "Pejabat Sementara", "Pelaksana Tugas", "Datasir"};
    
    public static int RIWAYAT_JABATAN = 0;
    public static int RIWAYAT_GRADE = 1;
    public static int RIWAYAT_CAREER_N_GRADE = 2;
    public static String[] historyGroup = {"Riwayat Jabatan", "Riwayat Grade", "Riwayat Jabatan dan Grade"};
    
    public static int TYPE_TRANSFER = 0;
    public static int TYPE_DEMOTION = 1;
    public static int TYPE_PROMOTION = 2;
    public static String[] mutationType = {"Transfer", "Demotion", "Promotion"};

    public PstCareerPath() {
    }

    public PstCareerPath(int i) throws DBException {
        super(new PstCareerPath());
    }

    public PstCareerPath(String sOid) throws DBException {
        super(new PstCareerPath(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstCareerPath(long lOid) throws DBException {
        super(new PstCareerPath(0));
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
        return TBL_HR_WORK_HISTORY_NOW;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstCareerPath().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        CareerPath careerpath = fetchExc(ent.getOID());
        ent = (Entity) careerpath;
        return careerpath.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((CareerPath) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((CareerPath) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static CareerPath fetchExc(long oid) throws DBException {
        try {
            CareerPath careerpath = new CareerPath();
            PstCareerPath pstCareerPath = new PstCareerPath(oid);
            careerpath.setOID(oid);

            careerpath.setEmployeeId(pstCareerPath.getlong(FLD_EMPLOYEE_ID));
            careerpath.setCompanyId(pstCareerPath.getlong(FLD_COMPANY_ID));
            careerpath.setCompany(pstCareerPath.getString(FLD_COMPANY));
            careerpath.setDepartmentId(pstCareerPath.getlong(FLD_DEPARTMENT_ID));
            careerpath.setDepartment(pstCareerPath.getString(FLD_DEPARTMENT));
            careerpath.setPositionId(pstCareerPath.getlong(FLD_POSITION_ID));
            careerpath.setPosition(pstCareerPath.getString(FLD_POSITION));
            careerpath.setSectionId(pstCareerPath.getlong(FLD_SECTION_ID));
            careerpath.setSection(pstCareerPath.getString(FLD_SECTION));
            careerpath.setDivisionId(pstCareerPath.getlong(FLD_DIVISION_ID));
            careerpath.setDivision(pstCareerPath.getString(FLD_DIVISION));
            careerpath.setLevelId(pstCareerPath.getlong(FLD_LEVEL_ID));
            careerpath.setLevel(pstCareerPath.getString(FLD_LEVEL));
            careerpath.setEmpCategoryId(pstCareerPath.getlong(FLD_EMP_CATEGORY_ID));
            careerpath.setEmpCategory(pstCareerPath.getString(FLD_EMP_CATEGORY));
            careerpath.setWorkFrom(pstCareerPath.getDate(FLD_WORK_FROM));
            careerpath.setWorkTo(pstCareerPath.getDate(FLD_WORK_TO));
            careerpath.setDescription(pstCareerPath.getString(FLD_DESCRIPTION));
            careerpath.setSalary(pstCareerPath.getdouble(FLD_SALARY));
            careerpath.setLocationId(pstCareerPath.getlong(FLD_LOCATION_ID));
            careerpath.setLocation(pstCareerPath.getString(FLD_LOCATION));
            careerpath.setNote(pstCareerPath.getString(FLD_NOTE));
            careerpath.setProviderID(pstCareerPath.getlong(FLD_PROVIDER_ID));
            careerpath.setHistoryType(pstCareerPath.getInt(FLD_HISTORY_TYPE));
            careerpath.setNomorSk(pstCareerPath.getString(FLD_NOMOR_SK));
            careerpath.setTanggalSk(pstCareerPath.getDate(FLD_TANGGAL_SK));
            careerpath.setEmpDocId(pstCareerPath.getLong(FLD_EMP_DOC_ID));
            careerpath.setHistoryGroup(pstCareerPath.getInt(FLD_HISTORY_GROUP));
            careerpath.setGradeLevelId(pstCareerPath.getLong(FLD_GRADE_LEVEL_ID));
            careerpath.setMutationType(pstCareerPath.getInt(FLD_MUTATION_TYPE));

            return careerpath;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCareerPath(0), DBException.UNKNOWN);
        }
    }
    public static Vector dateCareerPath(long oidEmployee){
        Vector result =new Vector();
        DBResultSet dbrs=null;
        try{
            String sql=" SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE " + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + " = "  + oidEmployee;
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                CareerPath careerPath = new CareerPath();
                careerPath.setWorkFrom(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]));
                careerPath.setWorkTo(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO]));
                careerPath.setOID(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID]));
                result.add(careerPath);
            }
        }catch(Exception exc){

        }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }

    
        public static Vector dateCareerPathwithlocation(long oidEmployee,long location){
        Vector result =new Vector();
        DBResultSet dbrs=null;
        try{
            String sql=" SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE " + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + " = "  + oidEmployee;
            dbrs=DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while(rs.next()){
                CareerPath careerPath = new CareerPath();
                careerPath.setLocationId(location);
                careerPath.setWorkFrom(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]));
                careerPath.setWorkTo(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO]));
                careerPath.setOID(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID]));
                result.add(careerPath);
            }
        }catch(Exception exc){

        }finally{
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static long insertExc(CareerPath careerpath) throws DBException {
        try {
            PstCareerPath pstCareerPath = new PstCareerPath(0);

            pstCareerPath.setLong(FLD_EMPLOYEE_ID, careerpath.getEmployeeId());
            pstCareerPath.setLong(FLD_COMPANY_ID, careerpath.getCompanyId());
            pstCareerPath.setString(FLD_COMPANY, careerpath.getCompany());
            pstCareerPath.setLong(FLD_DEPARTMENT_ID, careerpath.getDepartmentId());
            pstCareerPath.setString(FLD_DEPARTMENT, careerpath.getDepartment());
            pstCareerPath.setLong(FLD_POSITION_ID, careerpath.getPositionId());
            pstCareerPath.setString(FLD_POSITION, careerpath.getPosition());
            pstCareerPath.setLong(FLD_SECTION_ID, careerpath.getSectionId());
            pstCareerPath.setString(FLD_SECTION, careerpath.getSection());
            pstCareerPath.setLong(FLD_DIVISION_ID, careerpath.getDivisionId());
            pstCareerPath.setString(FLD_DIVISION, careerpath.getDivision());
            pstCareerPath.setLong(FLD_LEVEL_ID, careerpath.getLevelId());
            pstCareerPath.setString(FLD_LEVEL, careerpath.getLevel());
            pstCareerPath.setLong(FLD_EMP_CATEGORY_ID, careerpath.getEmpCategoryId());
            pstCareerPath.setString(FLD_EMP_CATEGORY, careerpath.getEmpCategory());
            pstCareerPath.setDate(FLD_WORK_FROM, careerpath.getWorkFrom());
            pstCareerPath.setDate(FLD_WORK_TO, careerpath.getWorkTo());
            pstCareerPath.setString(FLD_DESCRIPTION, careerpath.getDescription());
            pstCareerPath.setDouble(FLD_SALARY, careerpath.getSalary());
            pstCareerPath.setLong(FLD_LOCATION_ID, careerpath.getLocationId());
            pstCareerPath.setLong(FLD_PROVIDER_ID, careerpath.getProviderID());
            pstCareerPath.setString(FLD_LOCATION, careerpath.getLocation());
            pstCareerPath.setString(FLD_NOTE, careerpath.getNote());
            pstCareerPath.setInt(FLD_HISTORY_TYPE, careerpath.getHistoryType());
            pstCareerPath.setString(FLD_NOMOR_SK, careerpath.getNomorSk());
            pstCareerPath.setDate(FLD_TANGGAL_SK, careerpath.getTanggalSk());
            pstCareerPath.setLong(FLD_EMP_DOC_ID, careerpath.getEmpDocId());
            pstCareerPath.setInt(FLD_HISTORY_GROUP, careerpath.getHistoryGroup());
            pstCareerPath.setLong(FLD_GRADE_LEVEL_ID, careerpath.getGradeLevelId());
            pstCareerPath.setInt(FLD_MUTATION_TYPE, careerpath.getMutationType());
            
            pstCareerPath.insert();
            careerpath.setOID(pstCareerPath.getlong(FLD_WORK_HISTORY_NOW_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCareerPath(0), DBException.UNKNOWN);
        }
        return careerpath.getOID();
    }

    public static long updateExc(CareerPath careerpath) throws DBException {
        try {
            if (careerpath.getOID() != 0) {
                PstCareerPath pstCareerPath = new PstCareerPath(careerpath.getOID());

                pstCareerPath.setLong(FLD_EMPLOYEE_ID, careerpath.getEmployeeId());
                pstCareerPath.setLong(FLD_COMPANY_ID, careerpath.getCompanyId());
                pstCareerPath.setString(FLD_COMPANY, careerpath.getCompany());
                pstCareerPath.setLong(FLD_DEPARTMENT_ID, careerpath.getDepartmentId());
                pstCareerPath.setString(FLD_DEPARTMENT, careerpath.getDepartment());
                pstCareerPath.setLong(FLD_POSITION_ID, careerpath.getPositionId());
                pstCareerPath.setString(FLD_POSITION, careerpath.getPosition());
                pstCareerPath.setLong(FLD_SECTION_ID, careerpath.getSectionId());
                pstCareerPath.setString(FLD_SECTION, careerpath.getSection());
                pstCareerPath.setLong(FLD_DIVISION_ID, careerpath.getDivisionId());
                pstCareerPath.setString(FLD_DIVISION, careerpath.getDivision());
                pstCareerPath.setLong(FLD_LEVEL_ID, careerpath.getLevelId());
                pstCareerPath.setString(FLD_LEVEL, careerpath.getLevel());
                pstCareerPath.setLong(FLD_EMP_CATEGORY_ID, careerpath.getEmpCategoryId());
                pstCareerPath.setString(FLD_EMP_CATEGORY, careerpath.getEmpCategory());
                pstCareerPath.setDate(FLD_WORK_FROM, careerpath.getWorkFrom());
                pstCareerPath.setDate(FLD_WORK_TO, careerpath.getWorkTo());
                pstCareerPath.setString(FLD_DESCRIPTION, careerpath.getDescription());
                pstCareerPath.setDouble(FLD_SALARY, careerpath.getSalary());
                pstCareerPath.setLong(FLD_LOCATION_ID, careerpath.getLocationId());
                pstCareerPath.setLong(FLD_PROVIDER_ID, careerpath.getProviderID());
                pstCareerPath.setString(FLD_LOCATION, careerpath.getLocation());
                pstCareerPath.setString(FLD_NOTE, careerpath.getNote());
                pstCareerPath.setInt(FLD_HISTORY_TYPE, careerpath.getHistoryType());
                pstCareerPath.setString(FLD_NOMOR_SK, careerpath.getNomorSk());
                pstCareerPath.setDate(FLD_TANGGAL_SK, careerpath.getTanggalSk());
                pstCareerPath.setLong(FLD_EMP_DOC_ID, careerpath.getEmpDocId());
                pstCareerPath.setInt(FLD_HISTORY_GROUP, careerpath.getHistoryGroup());
                pstCareerPath.setLong(FLD_GRADE_LEVEL_ID, careerpath.getGradeLevelId());
                pstCareerPath.setInt(FLD_MUTATION_TYPE, careerpath.getMutationType());
                
                pstCareerPath.update();
                return careerpath.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCareerPath(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public static long deleteExc(long oid) throws DBException {
        try {
            PstCareerPath pstCareerPath = new PstCareerPath(oid);
            pstCareerPath.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstCareerPath(0), DBException.UNKNOWN);
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
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW;
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
                CareerPath careerpath = new CareerPath();
                resultToObject(rs, careerpath);
                lists.add(careerpath);
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
    
    public static Vector listHistory(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
     
         
            String sql = "SELECT p.POSITION, e.EMPLOYEE_NUM, e.FULL_NAME, h.* FROM " + TBL_HR_WORK_HISTORY_NOW + " h "+
                    " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " e ON e."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +" = h."+ fieldNames[FLD_EMPLOYEE_ID] +
                    " INNER JOIN " + PstPosition.TBL_HR_POSITION + " p ON p."+PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] +" = h."+ fieldNames[FLD_POSITION_ID];
                   
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
                CareerPath careerpath = new CareerPath();
                resultToObjectHistory(rs, careerpath);
                lists.add(careerpath);
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


    
     public static Vector listcheckcareer(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW;
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
                CareerPath careerpath = new CareerPath();
                resultToObject(rs, careerpath);
                lists.add(careerpath);
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
     * query menampilkan workDate to terakhir dari employee {
     * ari_20110912
     * @param rs
     * @param careerpath
     */
    public static Date workDate(long employeeId) {
        Date LastworkDate = null;
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT MAX(" + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO] + ") FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + " = " + employeeId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                LastworkDate = rs.getDate(1);
            }
            rs.close();
            return LastworkDate;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Date();
    }
    /*}*/

    private static void resultToObject(ResultSet rs, CareerPath careerpath) {
        try {
            careerpath.setOID(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID]));
            careerpath.setEmployeeId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]));
            careerpath.setCompanyId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_COMPANY_ID]));
            careerpath.setCompany(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_COMPANY]));
            careerpath.setDepartmentId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT_ID]));
            careerpath.setDepartment(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT]));
            careerpath.setPositionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION_ID]));
            careerpath.setPosition(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION]));
            careerpath.setSectionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION_ID]));
            careerpath.setSection(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION]));
            careerpath.setDivisionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_DIVISION_ID]));
            careerpath.setDivision(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DIVISION]));
            careerpath.setLevelId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_LEVEL_ID]));
            careerpath.setLevel(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_LEVEL]));
            careerpath.setEmpCategoryId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_CATEGORY_ID]));
            careerpath.setEmpCategory(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_CATEGORY]));
            careerpath.setWorkFrom(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]));
            careerpath.setWorkTo(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO]));
            careerpath.setDescription(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DESCRIPTION]));
            careerpath.setSalary(rs.getDouble(PstCareerPath.fieldNames[PstCareerPath.FLD_SALARY]));
            careerpath.setLocationId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_LOCATION_ID]));
            careerpath.setProviderID(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_PROVIDER_ID]));
            careerpath.setLocation(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_LOCATION]));
            careerpath.setNote(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_NOTE]));
            careerpath.setHistoryType(rs.getInt(PstCareerPath.fieldNames[PstCareerPath.FLD_HISTORY_TYPE]));
            careerpath.setNomorSk(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_NOMOR_SK]));
            careerpath.setTanggalSk(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_TANGGAL_SK]));
            careerpath.setEmpDocId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_DOC_ID]));
            careerpath.setHistoryGroup(rs.getInt(PstCareerPath.fieldNames[PstCareerPath.FLD_HISTORY_GROUP]));
            careerpath.setGradeLevelId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_GRADE_LEVEL_ID]));
            careerpath.setMutationType(rs.getInt(PstCareerPath.fieldNames[PstCareerPath.FLD_MUTATION_TYPE]));

        } catch (Exception e) {
        }
    }
    
    private static void resultToObjectHistory(ResultSet rs, CareerPath careerpath) {
        try {
            careerpath.setOID(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID]));
            careerpath.setEmployeeId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]));
            careerpath.setCompanyId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_COMPANY_ID]));
            careerpath.setCompany(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_COMPANY]));
            careerpath.setDepartmentId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT_ID]));
            careerpath.setDepartment(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT]));
            careerpath.setPositionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION_ID]));
            String posPrev = rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION]);
            String posNow = rs.getString(PstPosition.fieldNames[PstPosition.FLD_POSITION]);
            careerpath.setPosition(((posPrev!=null && posPrev.length()>1 && posPrev.compareToIgnoreCase(posNow)==0) ? posPrev+"/" : "" ) +  posNow );
            
            careerpath.setSectionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION_ID]));
            careerpath.setSection(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION]));
            careerpath.setDivisionId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_DIVISION_ID]));
            careerpath.setDivision(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DIVISION]));
            careerpath.setLevelId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_LEVEL_ID]));
            careerpath.setLevel(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_LEVEL]));
            careerpath.setEmpCategoryId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_CATEGORY_ID]));
            careerpath.setEmpCategory(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_CATEGORY]));
            careerpath.setWorkFrom(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]));
            careerpath.setWorkTo(rs.getDate(PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO]));
            careerpath.setDescription(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_DESCRIPTION]));
            careerpath.setSalary(rs.getDouble(PstCareerPath.fieldNames[PstCareerPath.FLD_SALARY]));
            careerpath.setLocationId(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_LOCATION_ID]));
            careerpath.setProviderID(rs.getLong(PstCareerPath.fieldNames[PstCareerPath.FLD_PROVIDER_ID]));
            careerpath.setLocation(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_LOCATION]));
            careerpath.setNote(rs.getString(PstCareerPath.fieldNames[PstCareerPath.FLD_NOTE]));
            careerpath.setMutationType(rs.getInt(PstCareerPath.fieldNames[PstCareerPath.FLD_MUTATION_TYPE]));
            Employee employee = new Employee();
            employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
            employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
            careerpath.setEmployee(employee);

        } catch (Exception e) {
        }
    }


    public static boolean checkOID(long workHistoryNowId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID] + " = " + workHistoryNowId;

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
            String sql = "SELECT COUNT(" + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID] + ") FROM " + TBL_HR_WORK_HISTORY_NOW;
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
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    CareerPath careerpath = (CareerPath) list.get(ls);
                    if (oid == careerpath.getOID()) {
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

    public static long deleteByEmployee(long emplOID) {
        try {
            String sql = " DELETE FROM " + PstCareerPath.TBL_HR_WORK_HISTORY_NOW
                    + " WHERE " + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]
                    + " = " + emplOID;

            int status = DBHandler.execUpdate(sql);
        } catch (Exception exc) {
            System.out.println("error delete experience by employee " + exc.toString());
        }

        return emplOID;
    }

    public static boolean checkDepartment(long departmentId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_DEPARTMENT_ID] + " = " + departmentId;

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

    public static boolean checkPosition(long positionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_POSITION_ID] + " = " + positionId;

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

    public static boolean checkDivision(long divisionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_DIVISION] + " = " + divisionId;

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
    
    //priska menambahkan location
    //27okt2014
    public static boolean checkLocation(long locationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_LOCATION] + " = " + locationId;

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

    /**
     * Ari_20110930
     * Menambah checkLevel dan checkEmpCategory {
     * @param
     * @return
     */
    public static boolean checkLevel(long levelId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_LEVEL_ID] + " = " + levelId;

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

    public static boolean checkEmpCategory(long empCategoryId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_CATEGORY_ID] + " = " + empCategoryId;

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
    /* } */

    public static boolean checkSection(long sectionId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_SECTION] + " = " + sectionId;

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

    /**
     * Ari_20110930
     * Menambah checkCompany {
     * @param companyId
     * @return
     */
    public static boolean checkCompany(long companyId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_WORK_HISTORY_NOW + " WHERE "
                    + PstCareerPath.fieldNames[PstCareerPath.FLD_COMPANY_ID] + " = " + companyId;

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
    /* } */

    //Gede_7Feb2012 {
    //untuk report excel
    public static int getCount2(SrcEmployee srcEmployee) {
        DBResultSet dbrs = null;
        SessEmployee sessEmployee = new SessEmployee();

        try {
            String sql = "SELECT COUNT(WHN." + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID] + ") FROM " + PstCareerPath.TBL_HR_WORK_HISTORY_NOW + " WHN INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON WHN." + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + (srcEmployee.getSalaryLevel().length() > 0
                    ? " LEFT JOIN  " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " LEV " + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = LEV." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                    : " " + " LEFT JOIN " + PstLevel.TBL_HR_LEVEL + " HR_LEV " + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]
                    + " = HR_LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]) + " WHERE " + sessEmployee.whereList(srcEmployee) + "GROUP BY WHN." + PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]
                    + " ORDER BY COUNT(WHN." + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_HISTORY_NOW_ID] + ") DESC LIMIT 1";

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
    //}
    //Gede_14Feb2012 {
    //Company

    public static String getCompany(String comp) {
        String company = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstCompany.fieldNames[PstCompany.FLD_COMPANY]
                    + " FROM " + PstCompany.TBL_HR_COMPANY + " WHERE " + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]
                    + "=" + comp;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                company = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return company;
    }
    //Division

    public static String getDivision(String div) {
        String division = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstDivision.fieldNames[PstDivision.FLD_DIVISION]
                    + " FROM " + PstDivision.TBL_HR_DIVISION + " WHERE " + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]
                    + "=" + div;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                division = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return division;
    }
    //priksa - location
    //27okt2014
    public static String getLocation(String loc) {
        String location = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstLocation.fieldNames[PstLocation.FLD_NAME]
                    + " FROM " + PstLocation.TBL_P2_LOCATION + " WHERE " + PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]
                    + "=" + loc;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                location = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return location;
    }
    
    //Department

    public static String getDepartment(String dept) {
        String department = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstDepartment.TBL_HR_DEPARTMENT + " WHERE " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + "=" + dept;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                department = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return department;
    }
    //Section

    public static String getSection(String sect) {
        String section = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstSection.fieldNames[PstSection.FLD_SECTION]
                    + " FROM " + PstSection.TBL_HR_SECTION + " WHERE " + PstSection.fieldNames[PstSection.FLD_SECTION_ID]
                    + "=" + sect;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                section = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return section;
    }
    //Position

    public static String getPosition(String pos) {
        String position = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstPosition.fieldNames[PstPosition.FLD_POSITION]
                    + " FROM " + PstPosition.TBL_HR_POSITION + " WHERE " + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]
                    + "=" + pos;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                position = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return position;
    }
    //Level

    public static String getLevel(String lev) {
        String level = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstLevel.fieldNames[PstLevel.FLD_LEVEL]
                    + " FROM " + PstLevel.TBL_HR_LEVEL + " WHERE " + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + "=" + lev;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                level = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return level;
    }
    //Category

    public static String getCategory(String cat) {
        String category = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
                    + " FROM " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " WHERE " + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]
                    + "=" + cat;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                category = rs.getString(1);
            }

            rs.close();
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return category;
    }
    //}
}
