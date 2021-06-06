
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 : Priska
 * @version	 : 20151201
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.masterdata; 

/* package java */ 
import java.io.*
;
import java.sql.*
;import java.util.*
;import java.util.Date;

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

public class PstEmpDocListMutation extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

   public static final String TBL_EMP_DOC_LIST_MUTATION = "hr_emp_doc_list_mutation";
   public static final int FLD_EMP_DOC_LIST_MUTATION_ID = 0;
   public static final int FLD_EMPLOYEE_ID = 1;
   public static final int FLD_EMP_DOC_ID = 2;
   public static final int FLD_OBJECT_NAME = 3;
   public static final int FLD_COMPANY_ID = 4;
   public static final int FLD_DIVISION_ID = 5;
   public static final int FLD_DEPARTMENT_ID = 6;
   public static final int FLD_SECTION_ID = 7;
   public static final int FLD_POSITION_ID = 8;
   public static final int FLD_EMP_CAT_ID = 9;
   public static final int FLD_WORK_FROM = 10;
   public static final int FLD_LEVEL_ID = 11;
        
	public static final  String[] fieldNames = {
      "EMP_DOC_LIST_MUTATION_ID",
      "EMPLOYEE_ID",
      "EMP_DOC_ID",
      "OBJECT_NAME",
      "COMPANY_ID",
      "DIVISION_ID",
      "DEPARTMENT_ID",
      "SECTION_ID",
      "POSITION_ID",
      "EMP_CAT_ID",
      "WORK_FROM",
      "LEVEL_ID"
	 }; 

	public static final  int[] fieldTypes = {
      TYPE_LONG + TYPE_PK + TYPE_ID,
      TYPE_LONG,
      TYPE_LONG,
      TYPE_STRING,
      TYPE_LONG,
      TYPE_LONG,
      TYPE_LONG,
      TYPE_LONG,
      TYPE_LONG,
      TYPE_LONG,
      TYPE_DATE,
      TYPE_LONG
	 }; 

	public PstEmpDocListMutation(){
	}

	public PstEmpDocListMutation(int i) throws DBException { 
		super(new PstEmpDocListMutation()); 
	}

	public PstEmpDocListMutation(String sOid) throws DBException { 
		super(new PstEmpDocListMutation(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstEmpDocListMutation(long lOid) throws DBException { 
		super(new PstEmpDocListMutation(0)); 
		String sOid = "0"; 
		try { 
			sOid = String.valueOf(lOid); 
		}catch(Exception e) { 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	} 

	public int getFieldSize(){ 
		return fieldNames.length; 
	}

	public String getTableName(){ 
		return TBL_EMP_DOC_LIST_MUTATION;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstEmpDocListMutation().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		EmpDocListMutation empDocListMutation = fetchExc(ent.getOID()); 
		ent = (Entity)empDocListMutation; 
		return empDocListMutation.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((EmpDocListMutation) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((EmpDocListMutation) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}

	public static EmpDocListMutation fetchExc(long oid) throws DBException{ 
		try{ 
                            EmpDocListMutation empDocListMutation = new EmpDocListMutation();
                            PstEmpDocListMutation pstEmpDocListMutation = new PstEmpDocListMutation(oid);
                            empDocListMutation.setOID(oid);
                            empDocListMutation.setEmployeeId(pstEmpDocListMutation.getLong(FLD_EMPLOYEE_ID));
                            empDocListMutation.setEmpDocId(pstEmpDocListMutation.getLong(FLD_EMP_DOC_ID));
                            empDocListMutation.setObjectName(pstEmpDocListMutation.getString(FLD_OBJECT_NAME));
                            empDocListMutation.setCompanyId(pstEmpDocListMutation.getLong(FLD_COMPANY_ID));
                            empDocListMutation.setDivisionId(pstEmpDocListMutation.getLong(FLD_DIVISION_ID));
                            empDocListMutation.setDepartmentId(pstEmpDocListMutation.getLong(FLD_DEPARTMENT_ID));
                            empDocListMutation.setSectionId(pstEmpDocListMutation.getLong(FLD_SECTION_ID));
                            empDocListMutation.setPositionId(pstEmpDocListMutation.getLong(FLD_POSITION_ID));
                            empDocListMutation.setEmpCatId(pstEmpDocListMutation.getLong(FLD_EMP_CAT_ID));
                            empDocListMutation.setWorkFrom(pstEmpDocListMutation.getDate(FLD_WORK_FROM));
                            empDocListMutation.setLevelId(pstEmpDocListMutation.getLong(FLD_LEVEL_ID));
                            
                            return empDocListMutation;
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpDocListMutation(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(EmpDocListMutation empDocListMutation) throws DBException{ 
		try{ 
            PstEmpDocListMutation pstEmpDocListMutation = new PstEmpDocListMutation(0);
            pstEmpDocListMutation.setLong(FLD_EMPLOYEE_ID, empDocListMutation.getEmployeeId());
            pstEmpDocListMutation.setLong(FLD_EMP_DOC_ID, empDocListMutation.getEmpDocId());
            pstEmpDocListMutation.setString(FLD_OBJECT_NAME, empDocListMutation.getObjectName());
            pstEmpDocListMutation.setLong(FLD_COMPANY_ID, empDocListMutation.getCompanyId());
            pstEmpDocListMutation.setLong(FLD_DIVISION_ID, empDocListMutation.getDivisionId());
            pstEmpDocListMutation.setLong(FLD_DEPARTMENT_ID, empDocListMutation.getDepartmentId());
            pstEmpDocListMutation.setLong(FLD_SECTION_ID, empDocListMutation.getSectionId());
            pstEmpDocListMutation.setLong(FLD_POSITION_ID, empDocListMutation.getPositionId());
            pstEmpDocListMutation.setLong(FLD_EMP_CAT_ID, empDocListMutation.getEmpCatId());
            pstEmpDocListMutation.setDate(FLD_WORK_FROM, empDocListMutation.getWorkFrom());
            pstEmpDocListMutation.setLong(FLD_LEVEL_ID, empDocListMutation.getLevelId());
            pstEmpDocListMutation.insert();
            empDocListMutation.setOID(pstEmpDocListMutation.getLong(FLD_EMP_DOC_LIST_MUTATION_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpDocListMutation(0),DBException.UNKNOWN); 
		}
		return empDocListMutation.getOID();
	}

	public static long updateExc(EmpDocListMutation empDocListMutation) throws DBException{ 
		try{ 
			if(empDocListMutation.getOID() != 0){ 
				PstEmpDocListMutation pstEmpDocListMutation = new PstEmpDocListMutation(empDocListMutation.getOID());
                                pstEmpDocListMutation.setLong(FLD_EMPLOYEE_ID, empDocListMutation.getEmployeeId());
                                pstEmpDocListMutation.setLong(FLD_EMP_DOC_ID, empDocListMutation.getEmpDocId());
                                pstEmpDocListMutation.setString(FLD_OBJECT_NAME, empDocListMutation.getObjectName());
                                pstEmpDocListMutation.setLong(FLD_COMPANY_ID, empDocListMutation.getCompanyId());
                                pstEmpDocListMutation.setLong(FLD_DIVISION_ID, empDocListMutation.getDivisionId());
                                pstEmpDocListMutation.setLong(FLD_DEPARTMENT_ID, empDocListMutation.getDepartmentId());
                                pstEmpDocListMutation.setLong(FLD_SECTION_ID, empDocListMutation.getSectionId());
                                pstEmpDocListMutation.setLong(FLD_POSITION_ID, empDocListMutation.getPositionId());
                                pstEmpDocListMutation.setLong(FLD_EMP_CAT_ID, empDocListMutation.getEmpCatId());
                                pstEmpDocListMutation.setDate(FLD_WORK_FROM, empDocListMutation.getWorkFrom());
                                pstEmpDocListMutation.setLong(FLD_LEVEL_ID, empDocListMutation.getLevelId());
                                pstEmpDocListMutation.update();
                                return empDocListMutation.getOID();

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpDocListMutation(0),DBException.UNKNOWN); 
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstEmpDocListMutation pstEmpDocListMutation = new PstEmpDocListMutation(oid);
			pstEmpDocListMutation.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstEmpDocListMutation(0),DBException.UNKNOWN); 
		}
		return oid;
	}

	public static Vector listAll(){ 
		return list(0, 500, "",""); 
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_EMP_DOC_LIST_MUTATION; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				EmpDocListMutation empDocListMutation = new EmpDocListMutation();
				resultToObject(rs, empDocListMutation);
				lists.add(empDocListMutation);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}

	private static void resultToObject(ResultSet rs, EmpDocListMutation empDocListMutation){
		try{
			empDocListMutation.setOID(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_LIST_MUTATION_ID]));
                        empDocListMutation.setEmployeeId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMPLOYEE_ID]));
                        empDocListMutation.setEmpDocId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_ID]));
                        empDocListMutation.setObjectName(rs.getString(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_OBJECT_NAME]));
                        empDocListMutation.setCompanyId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_COMPANY_ID]));
                        empDocListMutation.setDivisionId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_DIVISION_ID]));
                        empDocListMutation.setDepartmentId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_DEPARTMENT_ID]));
                        empDocListMutation.setSectionId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_SECTION_ID]));
                        empDocListMutation.setPositionId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_POSITION_ID]));
                        empDocListMutation.setEmpCatId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_CAT_ID]));
                        empDocListMutation.setWorkFrom(rs.getDate(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_WORK_FROM]));
                        empDocListMutation.setLevelId(rs.getLong(PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_LEVEL_ID]));
		}catch(Exception e){ }
	}

	public static boolean checkOID(long empDocListMutationId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_EMP_DOC_LIST_MUTATION + " WHERE " + 
						PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_LIST_MUTATION_ID] + " = " + empDocListMutationId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_LIST_MUTATION_ID] + ") FROM " + TBL_EMP_DOC_LIST_MUTATION;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while(rs.next()) { count = rs.getInt(1); }

			rs.close();
			return count;
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
	}

        
        	public static String getNewPosition(long empDocId, long empId, String ObjectName){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_EMP_DOC_LIST_MUTATION;
                            
				sql = sql + " WHERE " + PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_ID] + "=" +empDocId;

				sql = sql + " AND " + PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMPLOYEE_ID] + "=" +empId;
				sql = sql + " AND " + PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_OBJECT_NAME] + "= \"" +ObjectName+"\"";
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			String count = "";
			while(rs.next()) { 
                            EmpDocListMutation empDocListMutation = new EmpDocListMutation();
                            resultToObject(rs, empDocListMutation);
                            
                            Company company = new Company();
                            try{company = PstCompany.fetchExc(empDocListMutation.getCompanyId());}catch (Exception e){}
                            
                            Division division = new Division();
                            try{division = PstDivision.fetchExc(empDocListMutation.getDivisionId());} catch (Exception e){}
                            
                            Department department = new Department();
                            try{department = PstDepartment.fetchExc(empDocListMutation.getDepartmentId());}catch (Exception e){}
                            
                            Section section  = new Section();
                            try{section = PstSection.fetchExc(empDocListMutation.getSectionId());}catch (Exception e){}
                            
                            Position position  = new Position();
                            try{position = PstPosition.fetchExc(empDocListMutation.getPositionId());}catch (Exception e){}
                            
                            
                        
                            
                            count = company.getCompany()+","+division.getDivision()+","+department.getDepartment()+","+section.getSection()+","+position.getPosition(); 
                        }

			rs.close();
			return count;
		}catch(Exception e) {
			return "";
		}finally {
			DBResultSet.close(dbrs);
		}
	}
                
                
        public static Long getEmpDocFinalId(long empId, Date startdate){
		DBResultSet dbrs = null;
                long value = 0;
		try {
			String sql = "SELECT HEDLM."+ fieldNames[FLD_EMP_DOC_ID] +" FROM " + TBL_EMP_DOC_LIST_MUTATION +" HEDLM ";
                                sql = sql + " INNER JOIN " + PstEmpDoc.TBL_HR_EMP_DOC + " HED "
                                          + " ON HED." + PstEmpDoc.fieldNames[PstEmpDoc.FLD_EMP_DOC_ID] + " = HEDLM." + fieldNames[FLD_EMP_DOC_ID];
                                sql = sql + " INNER JOIN " + PstEmpDocAction.TBL_HR_EMP_DOC_ACTION + " HEDA "
                                          + " ON HEDA." + PstEmpDocAction.fieldNames[PstEmpDocAction.FLD_EMP_DOC_ID] + " = HEDLM." + fieldNames[FLD_EMP_DOC_ID];
				sql = sql + " WHERE HEDLM." + fieldNames[FLD_EMPLOYEE_ID] + " = " +empId;
                                sql = sql + " AND HEDLM." + fieldNames[FLD_WORK_FROM] + " = \"" +startdate+"\"";
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			
			while(rs.next()) { 
                            value = rs.getInt(1); 
                        }

			rs.close();
			return value;
		}catch(Exception e) {
			return value;
		}finally {
			DBResultSet.close(dbrs);
		}
	}
                 
                
                
                public static EmpDocListMutation getNewEmpDocListMutation(long empDocId, long empId, String ObjectName){
		DBResultSet dbrs = null;
                EmpDocListMutation empDocListMutation = new EmpDocListMutation();
		try {
			String sql = "SELECT * FROM " + TBL_EMP_DOC_LIST_MUTATION;
                            
				sql = sql + " WHERE " + PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_ID] + "=" +empDocId;

				sql = sql + " AND " + PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMPLOYEE_ID] + "=" +empId;
				sql = sql + " AND " + PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_OBJECT_NAME] + "= \"" +ObjectName+"\"";
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			String count = "";
			while(rs.next()) { 
                            
                            resultToObject(rs, empDocListMutation);
                            
                        }

			rs.close();
			return empDocListMutation;
		}catch(Exception e) {
			return empDocListMutation;
		}finally {
			DBResultSet.close(dbrs);
		}
	}
        

	/* This method used to find current data */
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   EmpDocListMutation empDocListMutation = (EmpDocListMutation)list.get(ls);
				   if(oid == empDocListMutation.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
	/* This method used to find command where current data */
	public static int findLimitCommand(int start, int recordToGet, int vectSize){
		 int cmd = Command.LIST;
		 int mdl = vectSize % recordToGet;
		 vectSize = vectSize + (recordToGet - mdl);
		 if(start == 0)
			 cmd =  Command.FIRST;
		 else{
			 if(start == (vectSize-recordToGet))
				 cmd = Command.LAST;
			 else{
				 start = start + recordToGet;
				 if(start <= (vectSize - recordToGet)){
					 cmd = Command.NEXT;
					 System.out.println("next.......................");
				 }else{
					 start = start - recordToGet;
					 if(start > 0){
						 cmd = Command.PREV; 
						 System.out.println("prev.......................");
					 } 
				 }
			 } 
		 }

		 return cmd;
	}
        
        public static boolean checkEmpDocListMutation(long empDocListMutationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_EMP_DOC_LIST_MUTATION + " WHERE "
                    + PstEmpDocListMutation.fieldNames[PstEmpDocListMutation.FLD_EMP_DOC_LIST_MUTATION_ID] + " = '" + empDocListMutationId + "'";

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

          public static boolean checkMaster(long oid){
    	if(PstEmpDocListMutation.checkEmpDocListMutation(oid))
            return true;
    	else
            return false;
    }
 
}
