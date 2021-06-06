/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll.value_mapping;

/**
 *
 * @author GUSWIK
 */
/* package java */ 
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.PstKabupaten;
import com.dimata.harisma.entity.masterdata.PstKecamatan;
import com.dimata.harisma.entity.masterdata.PstNegara;
import com.dimata.harisma.entity.masterdata.PstProvinsi;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;

import com.dimata.harisma.entity.payroll.*;
import com.dimata.harisma.entity.payroll.PstPayComponent;
import com.dimata.harisma.entity.payroll.SalaryLevelDetail;

public class PstValue_Mapping extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {
     public static final  String TBL_VALUE_MAPPING = "pay_value_mapping";
    
     public static final  int FLD_VALUE_MAPPING_ID = 0;
     public static final  int FLD_COMP_CODE = 1;
     public static final  int FLD_PARAMETER= 2;
     public static final  int FLD_NUMBER_OF_MAPS= 3;
     public static final  int FLD_START_DATE= 4;
     public static final  int FLD_END_DATE= 5;
     public static final  int FLD_COMPANY_ID= 6;
     public static final  int FLD_DIVISION_ID= 7;
     public static final  int FLD_DEPARTMENT_ID= 8;
     public static final  int FLD_SECTION_ID= 9;
     public static final  int FLD_LEVEL_ID= 10;
     public static final  int FLD_MARITAL_ID= 11;
     public static final  int FLD_LENGTH_OF_SERVICE= 12;
     public static final  int FLD_EMPLOYEE_CATEGORY= 13;
     public static final  int FLD_POSITION_ID= 14;
     public static final  int FLD_EMPLOYEE_ID= 15;
     public static final  int FLD_ADDR_COUNTRY_ID= 16;
     public static final  int FLD_ADDR_PROVINCE_ID= 17;
     public static final  int FLD_ADDR_REGENCY_ID= 18;
     public static final  int FLD_ADDR_SUBREGENCY_ID= 19;
     public static final  int FLD_VALUE= 20;
     public static final  int FLD_GRADE= 21;
     public static final  int FLD_LOS_FROM_IN_DAY   = 22;
     public static final  int FLD_LOS_FROM_IN_MONTH = 23;
     public static final  int FLD_LOS_FROM_IN_YEAR  = 24;
     public static final  int FLD_LOS_TO_IN_DAY     = 25;
     public static final  int FLD_LOS_TO_IN_MONTH   = 26;
     public static final  int FLD_LOS_TO_IN_YEAR    = 27;
     public static final  int FLD_LOS_CURRENT_DATE  = 28;
     public static final  int FLD_LOS_PER_CURRENT_DATE  = 29;
     public static final  int FLD_TAX_MARITAL_ID = 30;
	 public static final  int FLD_REMARK = 31;
     
     public static final  String[] fieldNames = {
                 "VALUE_MAPPING_ID",
                 "COMP_CODE",
                 "PARAMETER",
                 "NUMBER_OF_MAPS",
                 "START_DATE",
                 "END_DATE",
                 "COMPANY_ID",
                 "DIVISION_ID",
                 "DEPARTMENT_ID",
                 "SECTION_ID",
                 "LEVEL_ID",
                 "MARITAL_ID",
                 "LENGTH_OF_SERVICE",
                 "EMPLOYEE_CATEGORY",
                 "POSITION_ID",
                 "EMPLOYEE_ID",
                 "ADDR_COUNTRY_ID",
                 "ADDR_PROVINCE_ID",
                 "ADDR_REGENCY_ID",
                 "ADDR_SUBREGENCY_ID",
                 "VALUE",
                 "GRADE",
                 "LOS_FROM_IN_DAY",
                 "LOS_FROM_IN_MONTH",
                 "LOS_FROM_IN_YEAR",
                 "LOS_TO_IN_DAY",
                 "LOS_TO_IN_MONTH",
                 "LOS_TO_IN_YEAR",
                 "LOS_CURRENT_DATE",
                 "LOS_PER_CURRENT_DATE",
                 "TAX_MARITAL_ID",
				 "REMARK"
             
      };
      
      public static final  int[] fieldTypes = {
                TYPE_LONG + TYPE_PK + TYPE_ID,
                TYPE_STRING,//"COMP_CODE",
                TYPE_STRING,//"PARAMETER",
                TYPE_INT,//"NUMBER_OF_MAPS",
                TYPE_DATE,//"START_DATE",
                TYPE_DATE,//"END_DATE",
                TYPE_LONG,//"COMPANY_ID",
                TYPE_LONG,//"DIVISION_ID",
                TYPE_LONG,//"DEPARTMENT_ID",
                TYPE_LONG,//"SECTION_ID",
                TYPE_LONG,//"LEVEL_ID",
                TYPE_LONG,//"MARITAL_ID",
                TYPE_FLOAT,//"LENGTH_OF_SERVICE",
                TYPE_LONG,//"EMPLOYEE_CATEGORY",
                TYPE_LONG,//"POSITION",
                TYPE_LONG,//"EMPLOYEE_ID",
                TYPE_LONG,//"ADDR_COUNTRY_ID",
                TYPE_LONG,//"ADDR_PROVINCE_ID",
                TYPE_LONG,//"ADDR_REGENCY_ID",
                TYPE_LONG,//"ADDR_SUBREGENCY_ID",
                TYPE_FLOAT,//"VALUE"
                TYPE_LONG,//""
                TYPE_INT,//""
                TYPE_INT,//""
                TYPE_INT,//""
                TYPE_INT,//""
                TYPE_INT,//""
                TYPE_INT,
                TYPE_INT,
                TYPE_DATE,
                TYPE_LONG,
				TYPE_STRING
       };
       
       
    /** Creates a new instance of PstPayComponent */
    public PstValue_Mapping() {
    }
    
     public PstValue_Mapping(int i) throws DBException { 
		super(new PstValue_Mapping()); 
    }
     
    public PstValue_Mapping(String sOid) throws DBException { 
		super(new PstValue_Mapping(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}
     public PstValue_Mapping(long lOid) throws DBException { 
		super(new PstValue_Mapping(0)); 
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
    
     
    public long deleteExc(Entity ent) throws Exception {
        if(ent==null){ 
		throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
	return deleteExc(ent.getOID());
    }
    
    public long fetchExc(Entity ent) throws Exception {
         Value_Mapping value_Mapping = fetchExc(ent.getOID()); 
		ent = (Entity)value_Mapping; 
		return value_Mapping.getOID(); 
    }
    
    public long insertExc(Entity ent) throws Exception {
         return insertExc((Value_Mapping) ent);
    }
    
    public long updateExc(Entity ent) throws Exception {
         return updateExc((Value_Mapping) ent);
    }
    
    public String[] getFieldNames() {
        return fieldNames; 
    }
    
    public int getFieldSize() {
         return fieldNames.length; 
    }
    
    public int[] getFieldTypes() {
        return fieldTypes; 
    }
    
    public String getPersistentName() {
         return new PstValue_Mapping().getClass().getName(); 
    }
    
    public String getTableName() {
         return TBL_VALUE_MAPPING;
    }
    
    public static Value_Mapping fetchExc(long oid) throws DBException{ 
		try{ 
                    
			 Value_Mapping value_Mapping = new Value_Mapping();
                         PstValue_Mapping pstValue_Mapping = new PstValue_Mapping(oid); 
			 value_Mapping.setOID(oid);
                         value_Mapping.setCompCode(pstValue_Mapping.getString(FLD_COMP_CODE));
                         value_Mapping.setParameter(pstValue_Mapping.getString(FLD_PARAMETER));//"PARAMETER",
                         value_Mapping.setNumber_of_map(pstValue_Mapping.getInt(FLD_NUMBER_OF_MAPS));//"NUMBER_OF_MAPS",
                         value_Mapping.setStartdate(pstValue_Mapping.getDate(FLD_START_DATE));//"START_DATE",
                         value_Mapping.setEnddate(pstValue_Mapping.getDate(FLD_END_DATE));//"END_DATE",
                         value_Mapping.setCompany_id(pstValue_Mapping.getLong(FLD_COMPANY_ID));//"COMPANY_ID",
                         value_Mapping.setDivision_id(pstValue_Mapping.getLong(FLD_DIVISION_ID));//"DIVISION_ID",
                         value_Mapping.setDepartment_id(pstValue_Mapping.getLong(FLD_DEPARTMENT_ID));//"DEPARTMENT_ID",
                         value_Mapping.setSection_id(pstValue_Mapping.getLong(FLD_SECTION_ID));//"SECTION_ID",
                         value_Mapping.setLevel_id(pstValue_Mapping.getLong(FLD_LEVEL_ID));//"LEVEL_ID",
                         value_Mapping.setMarital_id(pstValue_Mapping.getLong(FLD_MARITAL_ID));//"MARITAL_ID",
                         value_Mapping.setLength_of_service(pstValue_Mapping.getfloat(FLD_LENGTH_OF_SERVICE));//"LENGTH_OF_SERVICE",
                         value_Mapping.setEmployee_category(pstValue_Mapping.getLong(FLD_EMPLOYEE_CATEGORY));//"EMPLOYEE_CATEGORY",
                         value_Mapping.setPosition_id(pstValue_Mapping.getLong(FLD_POSITION_ID));//"POSITION",
                         value_Mapping.setEmployee_id(pstValue_Mapping.getLong(FLD_EMPLOYEE_ID));//"EMPLOYEE_ID",
                         value_Mapping.setAddrCountryId(pstValue_Mapping.getLong(FLD_ADDR_COUNTRY_ID));//"ADDR_COUNTRY_ID",
                         value_Mapping.setAddrProvinceId(pstValue_Mapping.getLong(FLD_ADDR_PROVINCE_ID));//"ADDR_PROVINCE_ID",
                         value_Mapping.setAddrRegencyId(pstValue_Mapping.getLong(FLD_ADDR_REGENCY_ID));//"ADDR_REGENCY_ID",
                         value_Mapping.setAddrSubRegencyId(pstValue_Mapping.getLong(FLD_ADDR_SUBREGENCY_ID));////"ADDR_SUBREGENCY_ID",
                         value_Mapping.setValue(pstValue_Mapping.getdouble(FLD_VALUE));//"VALUE"
                         value_Mapping.setGrade(pstValue_Mapping.getlong(FLD_GRADE));
                         value_Mapping.setLosFromInDay(pstValue_Mapping.getInt(FLD_LOS_FROM_IN_DAY));
                         value_Mapping.setLosFromInMonth(pstValue_Mapping.getInt(FLD_LOS_FROM_IN_MONTH));
                         value_Mapping.setLosFromInYear(pstValue_Mapping.getInt(FLD_LOS_FROM_IN_YEAR));
                         value_Mapping.setLosToInDay(pstValue_Mapping.getInt(FLD_LOS_TO_IN_DAY));
                         value_Mapping.setLosToInMonth(pstValue_Mapping.getInt(FLD_LOS_TO_IN_MONTH));
                         value_Mapping.setLosToInYear(pstValue_Mapping.getInt(FLD_LOS_TO_IN_YEAR));
                         value_Mapping.setLosCurrentDate(pstValue_Mapping.getInt(FLD_LOS_CURRENT_DATE));
                         value_Mapping.setLosPerCurrentDate(pstValue_Mapping.getDate(FLD_LOS_PER_CURRENT_DATE));
                         value_Mapping.setTaxMaritalId(pstValue_Mapping.getLong(FLD_TAX_MARITAL_ID));
						 value_Mapping.setRemark(pstValue_Mapping.getString(FLD_REMARK));
                	return value_Mapping;
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstValue_Mapping(0),DBException.UNKNOWN); 
		} 
	}
    public static long insertExc(Value_Mapping value_Mapping) throws DBException{ 
		try{ 
                        PstValue_Mapping pstValue_Mapping = new PstValue_Mapping(0);
                	pstValue_Mapping.setString(FLD_COMP_CODE, value_Mapping.getCompCode());                        
                        pstValue_Mapping.setString(FLD_PARAMETER , value_Mapping.getParameter());//"PARAMETER",
                        pstValue_Mapping.setInt(FLD_NUMBER_OF_MAPS , value_Mapping.getNumber_of_map());// "NUMBER_OF_MAPS",
                        pstValue_Mapping.setDate(FLD_START_DATE, value_Mapping.getStartdate());// "START_DATE",
                        pstValue_Mapping.setDate(FLD_END_DATE, value_Mapping.getEnddate());// "END_DATE",
                        pstValue_Mapping.setLong(FLD_COMPANY_ID, value_Mapping.getCompany_id());// "COMPANY_ID",
                        pstValue_Mapping.setLong(FLD_DIVISION_ID, value_Mapping.getDivision_id());// "DIVISION_ID",
                        pstValue_Mapping.setLong(FLD_DEPARTMENT_ID, value_Mapping.getDepartment_id());// "DEPARTMENT_ID",
                        pstValue_Mapping.setLong(FLD_SECTION_ID, value_Mapping.getSection_id());// "SECTION_ID",
                        pstValue_Mapping.setLong(FLD_LEVEL_ID, value_Mapping.getLevel_id());// "LEVEL_ID",
                        pstValue_Mapping.setLong(FLD_MARITAL_ID, value_Mapping.getMarital_id());// "MARITAL_ID",
                        pstValue_Mapping.setFloat(FLD_LENGTH_OF_SERVICE, value_Mapping.getLength_of_service());// "LENGTH_OF_SERVICE",
                        pstValue_Mapping.setLong(FLD_EMPLOYEE_CATEGORY, value_Mapping.getEmployee_category());// "EMPLOYEE_CATEGORY",
                        pstValue_Mapping.setLong(FLD_POSITION_ID, value_Mapping.getPosition_id());// "POSITION",
                        pstValue_Mapping.setLong(FLD_EMPLOYEE_ID, value_Mapping.getEmployee_id());// "EMPLOYEE_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_COUNTRY_ID, value_Mapping.getAddrCountryId());// "ADDR_COUNTRY_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_PROVINCE_ID, value_Mapping.getAddrProvinceId());// "ADDR_PROVINCE_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_REGENCY_ID, value_Mapping.getAddrRegencyId());// "ADDR_REGENCY_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_SUBREGENCY_ID, value_Mapping.getAddrSubRegencyId());// "ADDR_SUBREGENCY_ID",
                        pstValue_Mapping.setDouble(FLD_VALUE, value_Mapping.getValue());// "VALUE"
                        pstValue_Mapping.setLong(FLD_GRADE, value_Mapping.getGrade());// "GRADE"
                        pstValue_Mapping.setInt(FLD_LOS_FROM_IN_DAY, value_Mapping.getLosFromInDay());
                        pstValue_Mapping.setInt(FLD_LOS_FROM_IN_MONTH, value_Mapping.getLosFromInMonth());
                        pstValue_Mapping.setInt(FLD_LOS_FROM_IN_YEAR, value_Mapping.getLosFromInYear());
                        pstValue_Mapping.setInt(FLD_LOS_TO_IN_DAY, value_Mapping.getLosToInDay());
                        pstValue_Mapping.setInt(FLD_LOS_TO_IN_MONTH, value_Mapping.getLosToInMonth());
                        pstValue_Mapping.setInt(FLD_LOS_TO_IN_YEAR, value_Mapping.getLosToInYear());
                        pstValue_Mapping.setInt(FLD_LOS_CURRENT_DATE, value_Mapping.getLosCurrentDate());
                        pstValue_Mapping.setDate(FLD_LOS_PER_CURRENT_DATE, value_Mapping.getLosPerCurrentDate());
                        pstValue_Mapping.setLong(FLD_TAX_MARITAL_ID, value_Mapping.getTaxMaritalId());
                        pstValue_Mapping.setString(FLD_REMARK, value_Mapping.getRemark());
                        pstValue_Mapping.insert();
			value_Mapping.setOID(pstValue_Mapping.getlong(FLD_VALUE_MAPPING_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPayComponent(0),DBException.UNKNOWN); 
		}
		return value_Mapping.getOID();
	}
    
     public static long updateExc(Value_Mapping value_Mapping) throws DBException{ 
		try{ 
		  if(value_Mapping.getOID() != 0){ 
                        PstValue_Mapping pstValue_Mapping = new PstValue_Mapping(value_Mapping.getOID());
                        pstValue_Mapping.setString(FLD_COMP_CODE, value_Mapping.getCompCode());                        
                        pstValue_Mapping.setString(FLD_PARAMETER , value_Mapping.getParameter());//"PARAMETER",
                        pstValue_Mapping.setInt(FLD_NUMBER_OF_MAPS , value_Mapping.getNumber_of_map());// "NUMBER_OF_MAPS",
                        pstValue_Mapping.setDate(FLD_START_DATE, value_Mapping.getStartdate());// "START_DATE",
                        pstValue_Mapping.setDate(FLD_END_DATE, value_Mapping.getEnddate());// "END_DATE",
                        pstValue_Mapping.setLong(FLD_COMPANY_ID, value_Mapping.getCompany_id());// "COMPANY_ID",
                        pstValue_Mapping.setLong(FLD_DIVISION_ID, value_Mapping.getDivision_id());// "DIVISION_ID",
                        pstValue_Mapping.setLong(FLD_DEPARTMENT_ID, value_Mapping.getDepartment_id());// "DEPARTMENT_ID",
                        pstValue_Mapping.setLong(FLD_SECTION_ID, value_Mapping.getSection_id());// "SECTION_ID",
                        pstValue_Mapping.setLong(FLD_LEVEL_ID, value_Mapping.getLevel_id());// "LEVEL_ID",
                        pstValue_Mapping.setLong(FLD_MARITAL_ID, value_Mapping.getMarital_id());// "MARITAL_ID",
                        pstValue_Mapping.setFloat(FLD_LENGTH_OF_SERVICE, value_Mapping.getLength_of_service());// "LENGTH_OF_SERVICE",
                        pstValue_Mapping.setLong(FLD_EMPLOYEE_CATEGORY, value_Mapping.getEmployee_category());// "EMPLOYEE_CATEGORY",
                        pstValue_Mapping.setLong(FLD_POSITION_ID, value_Mapping.getPosition_id());// "POSITION",
                        pstValue_Mapping.setLong(FLD_EMPLOYEE_ID, value_Mapping.getEmployee_id());// "EMPLOYEE_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_COUNTRY_ID, value_Mapping.getAddrCountryId());// "ADDR_COUNTRY_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_PROVINCE_ID, value_Mapping.getAddrProvinceId());// "ADDR_PROVINCE_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_REGENCY_ID, value_Mapping.getAddrRegencyId());// "ADDR_REGENCY_ID",
                        pstValue_Mapping.setLong(FLD_ADDR_SUBREGENCY_ID, value_Mapping.getAddrSubRegencyId());// "ADDR_SUBREGENCY_ID",
                        pstValue_Mapping.setDouble(FLD_VALUE, value_Mapping.getValue());// "VALUE"
                        pstValue_Mapping.setLong(FLD_GRADE, value_Mapping.getGrade());
                        pstValue_Mapping.setInt(FLD_LOS_FROM_IN_DAY, value_Mapping.getLosFromInDay());
                        pstValue_Mapping.setInt(FLD_LOS_FROM_IN_MONTH, value_Mapping.getLosFromInMonth());
                        pstValue_Mapping.setInt(FLD_LOS_FROM_IN_YEAR, value_Mapping.getLosFromInYear());
                        pstValue_Mapping.setInt(FLD_LOS_TO_IN_DAY, value_Mapping.getLosToInDay());
                        pstValue_Mapping.setInt(FLD_LOS_TO_IN_MONTH, value_Mapping.getLosToInMonth());
                        pstValue_Mapping.setInt(FLD_LOS_TO_IN_YEAR, value_Mapping.getLosToInYear());
                        pstValue_Mapping.setInt(FLD_LOS_CURRENT_DATE, value_Mapping.getLosCurrentDate());
                        pstValue_Mapping.setDate(FLD_LOS_PER_CURRENT_DATE, value_Mapping.getLosPerCurrentDate());
                        pstValue_Mapping.setLong(FLD_TAX_MARITAL_ID, value_Mapping.getTaxMaritalId());// "MARITAL_ID",
                        pstValue_Mapping.setString(FLD_REMARK, value_Mapping.getRemark());
                        pstValue_Mapping.update();
				return value_Mapping.getOID();
                }
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPayComponent(0),DBException.UNKNOWN); 
		}
		return 0;
	}
     
     public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstValue_Mapping pstValue_Mapping = new PstValue_Mapping(oid);
			pstValue_Mapping.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstValue_Mapping(0),DBException.UNKNOWN); 
		}
		return oid;
	}
     
      public static Vector listAll(){ 
		return list(0, 1000, "","");
	}
      
      public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_VALUE_MAPPING; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			dbrs = DBHandler.execQueryResult(sql);
                        //System.out.println("SQL LIST"+sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Value_Mapping value_Mapping = new Value_Mapping();
				resultToObject(rs, value_Mapping);
				lists.add(value_Mapping);
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
      
 
      
      public static void resultToObject(ResultSet rs, Value_Mapping value_Mapping){
		try{
			value_Mapping.setOID(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_VALUE_MAPPING_ID]));
                        value_Mapping.setCompCode(rs.getString(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE]));
                        value_Mapping.setParameter(rs.getString(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_PARAMETER]));
                        value_Mapping.setNumber_of_map(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_NUMBER_OF_MAPS]));//                 "NUMBER_OF_MAPS",
                        value_Mapping.setStartdate(rs.getDate(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_START_DATE]));//                 "START_DATE",
                        value_Mapping.setEnddate(rs.getDate(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_END_DATE]));//                 "END_DATE",
                        value_Mapping.setCompany_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMPANY_ID]));//                 "COMPANY_ID",
                        value_Mapping.setDivision_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_DIVISION_ID]));//                 "DIVISION_ID",
                        value_Mapping.setDepartment_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_DEPARTMENT_ID]));//                 "DEPARTMENT_ID",
                        value_Mapping.setSection_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_SECTION_ID]));//                 "SECTION_ID",
                        value_Mapping.setLevel_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LEVEL_ID]));//                 "LEVEL_ID",
                        value_Mapping.setMarital_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_MARITAL_ID]));//                 "MARITAL_ID",
                        value_Mapping.setLength_of_service(rs.getFloat(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LENGTH_OF_SERVICE]));//                 "LENGTH_OF_SERVICE",
                        value_Mapping.setEmployee_category(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_CATEGORY]));//                 "EMPLOYEE_CATEGORY",
                        value_Mapping.setPosition_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_POSITION_ID]));//                 "POSITION",
                        value_Mapping.setEmployee_id(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_ID]));//                 "EMPLOYEE_ID",
                        value_Mapping.setAddrCountryId(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_ADDR_COUNTRY_ID]));//                 "ADDR_COUNTRY_ID",
                        value_Mapping.setAddrProvinceId(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_ADDR_PROVINCE_ID]));//                 "ADDR_PROVINCE_ID",
                        value_Mapping.setAddrRegencyId(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_ADDR_REGENCY_ID]));//                 "ADDR_REGENCY_ID",
                        value_Mapping.setAddrSubRegencyId(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_ADDR_SUBREGENCY_ID]));//                 "ADDR_SUBREGENCY_ID",
                        value_Mapping.setValue(rs.getDouble(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_VALUE]));
                        value_Mapping.setGrade(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_GRADE]));
                        value_Mapping.setLosFromInDay(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_FROM_IN_DAY]));
                        value_Mapping.setLosFromInMonth(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_FROM_IN_MONTH]));
                        value_Mapping.setLosFromInYear(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_FROM_IN_YEAR]));
                        value_Mapping.setLosToInDay(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_TO_IN_DAY]));
                        value_Mapping.setLosToInMonth(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_TO_IN_MONTH]));
                        value_Mapping.setLosToInYear(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_TO_IN_YEAR]));
                        value_Mapping.setLosCurrentDate(rs.getInt(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_CURRENT_DATE]));
                        value_Mapping.setLosPerCurrentDate(rs.getDate(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LOS_PER_CURRENT_DATE]));          
                        value_Mapping.setTaxMaritalId(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_TAX_MARITAL_ID]));  
                        value_Mapping.setRemark(rs.getString(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_REMARK]));
		}catch(Exception e){ 
                    System.out.println("Exception PstPayCOmponen"+e);
                }
	}
   
      
      public static boolean checkOID(long valuemappingId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_VALUE_MAPPING + " WHERE " + 
						PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_VALUE_MAPPING_ID] + " = '" + valuemappingId+"'";

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
			String sql = "SELECT COUNT("+ PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_VALUE_MAPPING_ID] + ") FROM " + TBL_VALUE_MAPPING;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;

                        //System.out.println("SQL count"+sql);
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
    
          public static String GetGeoAddress(Value_Mapping value_Mapping) {
        Vector lists = new Vector();
        String geo = "";
        DBResultSet dbrs = null;
        if ((value_Mapping == null) || (value_Mapping.getOID() == 0)) {
            return null;
        }
        try {
            String sql =
                    "SELECT e." + fieldNames[FLD_VALUE_MAPPING_ID]
                    + ", n." + PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA] + " AS NEG "
                    + ", p." + PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI] + " AS PROV "
                    + ", k." + PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN] + " AS KAB "
                    + ", c." + PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN] + " AS KEC "
                    + " FROM " + TBL_VALUE_MAPPING + " e "
                    + " LEFT JOIN " + PstNegara.TBL_BKD_NEGARA + " n ON e." + fieldNames[FLD_ADDR_COUNTRY_ID] + "=n." + PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA]
                    + " LEFT JOIN " + PstProvinsi.TBL_HR_PROPINSI + " p ON e." + fieldNames[FLD_ADDR_PROVINCE_ID] + "= p." + PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]
                    + " LEFT JOIN " + PstKabupaten.TBL_HR_KABUPATEN + " k ON e." + fieldNames[FLD_ADDR_REGENCY_ID] + " = k." + PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]
                    + " LEFT JOIN " + PstKecamatan.TBL_HR_KECAMATAN + " c ON e." + fieldNames[FLD_ADDR_SUBREGENCY_ID] + "= c." + PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN]
                    + " WHERE " + fieldNames[FLD_VALUE_MAPPING_ID] + "=\"" + value_Mapping.getOID() + "\"";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                geo =  ("" + rs.getString("NEG") + ", " + rs.getString("PROV") + ", " + rs.getString("KAB") + ", " + rs.getString("KEC"));
            }
            geo = geo.replaceAll("null", "-");
            rs.close();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return geo;
    }
    
    public static int findLimitStart( long oid, int recordToGet, String whereClause,String orderClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   Value_Mapping value_Mapping = (Value_Mapping)list.get(ls);
				   if(oid == value_Mapping.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}
    
      public static double getValuemapping(Date fromdate, Date todate, long employeeId, SalaryLevelDetail salaryComp ) {
		DBResultSet dbrs = null;
                double nilai = 0;
                Employee employee = new Employee();
		 try {
            employee = PstEmployee.fetchExc(employeeId);
        } catch (Exception e){
            
        }
        try {
            String sql = " SELECT * FROM " + PstValue_Mapping.TBL_VALUE_MAPPING + " WHERE " + 
                    PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMP_CODE] + " = \"" + salaryComp.getCompCode() + "\" " 
                    + " AND " + PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_END_DATE]
                    + " >= \"" + Formater.formatDate(fromdate, "yyyy-MM-dd  00:00:00") + "\"" ;
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while (rs.next()) {
               // Employee employee = PstEmployee.fetchExc(employeeId);
                
		long VmCompanyId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_COMPANY_ID]);
                long VmDivisionId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_DIVISION_ID]);
                long VmDepartmentId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_DEPARTMENT_ID]);
                long VmSectionId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_SECTION_ID]);
                long VmLevelId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LEVEL_ID]);
                long VmMaritalId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_MARITAL_ID]);
                double VmLengthOfService = rs.getDouble(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_LENGTH_OF_SERVICE]);
                long VmEmpCategoryId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_CATEGORY]);
                long VmPositionId = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_POSITION_ID]);
                String VmEmployeeId = String.valueOf(rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_EMPLOYEE_ID]));
                double VmValue = rs.getDouble(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_VALUE]);
                double VmGrade = rs.getLong(PstValue_Mapping.fieldNames[PstValue_Mapping.FLD_GRADE]);
 
                Date today = new Date();
                  boolean  nilaitf = false;
                
                if ((VmCompanyId != 0) && (VmCompanyId > 0 )){
                    if (VmCompanyId != employee.getCompanyId()){
                         nilaitf = false;
                    } 
                } 
                
                if ((VmDivisionId != 0) && (VmDivisionId > 0 )){
                    if (VmDivisionId != employee.getDivisionId()){
                         nilaitf = false;
                    } 
                } 
                if ((VmDepartmentId != 0) && (VmDepartmentId > 0 )){
                    if (VmDepartmentId != employee.getDepartmentId()){
                         nilaitf = false;
                    } 
                } 
                if ((VmSectionId != 0) && (VmSectionId > 0 )){
                    if (VmSectionId != employee.getSectionId()){
                         nilaitf = false;
                    } 
                }
                if ((VmPositionId != 0) && (VmPositionId > 0 )){
                    if (VmPositionId != employee.getPositionId()){
                         nilaitf = false;
                    } 
                }
                 if ((VmGrade != 0) && (VmGrade > 0 )){
                    if (VmGrade != employee.getGradeLevelId()){
                         nilaitf = false;
                    } 
                }
                if ((VmEmpCategoryId != 0) && (VmEmpCategoryId > 0 )){
                    if (VmEmpCategoryId != employee.getEmpCategoryId()){
                         nilaitf = false;
                    } 
                }
                if ((VmEmpCategoryId != 0) && (VmEmpCategoryId > 0 )){
                    if (VmEmpCategoryId != employee.getEmpCategoryId()){
                         nilaitf = false;
                    } 
                }
                if ((VmLevelId != 0) && (VmLevelId > 0 )){
                    if (VmLevelId != employee.getLevelId()){
                         nilaitf = false;
                    } 
                }
                if ((VmMaritalId != 0) && (VmMaritalId > 0 )){
                    if (VmMaritalId != employee.getMaritalId()){
                         nilaitf = false;
                    } 
                }
                if((VmEmployeeId != null)  && (VmEmployeeId != "") && (VmEmployeeId.length() > 0)){
                    if (VmEmployeeId.equals(employee.getOID())){
                         nilaitf = false;
                    } 
                }
                 if ((VmLengthOfService != 0) && (VmLengthOfService > 0 )){
                       double diff = today.getTime() - employee.getCommencingDate().getTime();
                       double yeardiff = diff/(1000 * 60 * 60 * 24 * 365);
                       if ((yeardiff != VmLengthOfService) || (yeardiff < VmLengthOfService) ){
                       nilaitf = false;
                       }
                }
                 
                 if (nilaitf){
                     nilai = nilai + VmValue;
                 }
          
            }
            //rs.close();
            return nilai;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
            return 0;
     }
    
    
    
   public static int findLimitCommand(int start, int recordToGet, int vectSize)
    {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
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
    
}
