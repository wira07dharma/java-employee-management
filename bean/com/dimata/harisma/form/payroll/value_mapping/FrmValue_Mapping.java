/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.payroll.value_mapping;

/**
 *
 * @author GUSWIK
 */
/*
 * FrmPayComponent.java
 *
 * Created on April 2, 2007, 2:03 PM
 */

import com.dimata.harisma.entity.payroll.value_mapping.Value_Mapping;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.harisma.entity.payroll.*;

/**
 *
 * @author  yunny
 */
public class FrmValue_Mapping extends FRMHandler implements I_FRMInterface, I_FRMType {
     private Value_Mapping value_Mapping;

	public static final String FRM_VALUE_MAPPING =  "FRM_VALUE_MAPPING" ;
        
        
public static final int FRM_FIELD_VALUE_MAPPING_ID	= 0 ;
public static final int FRM_FIELD_COMP_CODE		= 1 ;
public static final int FRM_FIELD_PARAMETER             = 2 ;
public static final int FRM_FIELD_NUMBER_OF_MAPS        = 3 ;
public static final int FRM_FIELD_START_DATE            = 4 ;
public static final int FRM_FIELD_END_DATE              = 5 ;
public static final int FRM_FIELD_COMPANY_ID            = 6 ;
public static final int FRM_FIELD_DIVISION_ID           = 7 ;
public static final int FRM_FIELD_DEPARTMENT_ID         = 8 ;
public static final int FRM_FIELD_SECTION_ID            = 9 ;
public static final int FRM_FIELD_LEVEL_ID              =10 ;
public static final int FRM_FIELD_MARITAL_ID            =11 ;
public static final int FRM_FIELD_LENGTH_OF_SERVICE     =12 ;
public static final int FRM_FIELD_EMPLOYEE_CATEGORY     =13 ;
public static final int FRM_FIELD_POSITION_ID           =14 ;
public static final int FRM_FIELD_EMPLOYEE_ID           =15 ;
public static final int FRM_FIELD_ADDR_COUNTRY_ID       =16 ;
public static final int FRM_FIELD_ADDR_PROVINCE_ID      =17 ;
public static final int FRM_FIELD_ADDR_REGENCY_ID       =18 ;
public static final int FRM_FIELD_ADDR_SUBREGENCY_ID    =19 ;
public static final int FRM_FIELD_VALUE                 =20 ;
public static final int FRM_FIELD_GRADE                 =21 ;
public static final int FRM_FIELD_LOS_FROM_IN_DAY       =22 ;
public static final int FRM_FIELD_LOS_FROM_IN_MONTH     =23 ;
public static final int FRM_FIELD_LOS_FROM_IN_YEAR      =24 ;
public static final int FRM_FIELD_LOS_TO_IN_DAY         =25 ;
public static final int FRM_FIELD_LOS_TO_IN_MONTH       =26 ;
public static final int FRM_FIELD_LOS_TO_IN_YEAR        =27 ;
public static final int FRM_FIELD_LOS_CURRENT_DATE      =28 ;
public static final int FRM_FIELD_LOS_PER_CURRENT_DATE  =29 ;
public static final int FRM_FIELD_TAX_MARITAL_ID        =30 ;
public static final int FRM_FIELD_REMARK				=31 ;
        
        
        
        public static String[] fieldNames = {
	
         "FRM_FIELD_VALUE_MAPPING_ID",
         "FRM_FIELD_COMP_CODE",
         "FRM_FIELD_PARAMETER",
         "FRM_FIELD_NUMBER_OF_MAPS",
         "FRM_FIELD_START_DATE",
         "FRM_FIELD_END_DATE",
         "FRM_FIELD_COMPANY_ID",
         "FRM_FIELD_DIVISION_ID",
         "FRM_FIELD_DEPARTMENT_ID",
         "FRM_FIELD_SECTION_ID",
         "FRM_FIELD_LEVEL_ID",
         "FRM_FIELD_MARITAL_ID",
         "FRM_FIELD_LENGTH_OF_SERVICE",
         "FRM_FIELD_EMPLOYEE_CATEGORY",
         "FRM_FIELD_POSITION_ID",
         "FRM_FIELD_EMPLOYEE_ID",
         "FRM_FIELD_ADDR_COUNTRY_ID",
         "FRM_FIELD_ADDR_PROVINCE_ID",
         "FRM_FIELD_ADDR_REGENCY_ID",
         "FRM_FIELD_ADDR_SUBREGENCY_ID",
         "FRM_FIELD_VALUE",
         "FRM_FIELD_GRADE",
         "FRM_FIELD_LOS_FROM_IN_DAY",
         "FRM_FIELD_LOS_FROM_IN_MONTH",
         "FRM_FIELD_LOS_FROM_IN_YEAR",
         "FRM_FIELD_LOS_TO_IN_DAY",
         "FRM_FIELD_LOS_TO_IN_MONTH",
         "FRM_FIELD_LOS_TO_IN_YEAR",
         "FRM_FIELD_LOS_CURRENT_DATE",
         "FRM_FIELD_LOS_PER_CURRENT_DATE",
         "FRM_FIELD_TAX_MARITAL_ID",
		 "FRM_FIELD_REMARK"
        
       };
       
        public static int[] fieldTypes = {
                TYPE_LONG,
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
                TYPE_LONG,//"VALUE"
                TYPE_INT,//"VALUE"
                TYPE_INT,//"VALUE"
                TYPE_INT,//"VALUE"
                TYPE_INT,//"VALUE"
                TYPE_INT,//"VALUE"
                TYPE_INT,//"VALUE"
                TYPE_INT,
                TYPE_DATE,
                TYPE_LONG,
				TYPE_STRING
       };
        
    
    /** Creates a new instance of FrmPayComponent */
    public FrmValue_Mapping() {
    }
    
     public FrmValue_Mapping(Value_Mapping value_Mapping){
		this.value_Mapping = value_Mapping;
	}
     
     public FrmValue_Mapping(HttpServletRequest request, Value_Mapping value_Mapping){
		super(new FrmValue_Mapping(value_Mapping), request);
		this.value_Mapping = value_Mapping;
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
    
    public String getFormName() {
        return FRM_VALUE_MAPPING;
    }
    
   public Value_Mapping getEntityObject(){ return value_Mapping; }
   
     public void requestEntityObject(Value_Mapping value_Mapping) {
		try{
			this.requestParam();
                        //employee.setPositionId(getLong(FRM_FIELD_POSITION_ID));
                        value_Mapping.setCompCode(getString(FRM_FIELD_COMP_CODE));
                        value_Mapping.setParameter(getString(FRM_FIELD_PARAMETER));
                        value_Mapping.setNumber_of_map(getInt(FRM_FIELD_NUMBER_OF_MAPS));
                        value_Mapping.setStartdate(getDate(FRM_FIELD_START_DATE));
                        value_Mapping.setEnddate(getDate(FRM_FIELD_END_DATE));
                        value_Mapping.setCompany_id(getLong(FRM_FIELD_COMPANY_ID));
                        value_Mapping.setDivision_id(getLong(FRM_FIELD_DIVISION_ID));
                        value_Mapping.setDepartment_id(getLong(FRM_FIELD_DEPARTMENT_ID));
                        value_Mapping.setSection_id(getLong(FRM_FIELD_SECTION_ID));
                        value_Mapping.setLevel_id(getLong(FRM_FIELD_LEVEL_ID));
                        value_Mapping.setMarital_id(getLong(FRM_FIELD_MARITAL_ID));
                        value_Mapping.setLength_of_service(getFloat(FRM_FIELD_LENGTH_OF_SERVICE));
                        value_Mapping.setEmployee_category(getLong(FRM_FIELD_EMPLOYEE_CATEGORY));
                        value_Mapping.setPosition_id(getLong(FRM_FIELD_POSITION_ID));
                        value_Mapping.setEmployee_id(getLong(FRM_FIELD_EMPLOYEE_ID));
                        value_Mapping.setAddrCountryId(getLong(FRM_FIELD_ADDR_COUNTRY_ID));
                        value_Mapping.setAddrProvinceId(getLong(FRM_FIELD_ADDR_PROVINCE_ID));
                        value_Mapping.setAddrRegencyId(getLong(FRM_FIELD_ADDR_REGENCY_ID));
                        value_Mapping.setAddrSubRegencyId(getLong(FRM_FIELD_ADDR_SUBREGENCY_ID));
                        value_Mapping.setValue(getLong(FRM_FIELD_VALUE));
                        value_Mapping.setGrade(getLong(FRM_FIELD_GRADE));
                        value_Mapping.setLosFromInDay(getInt(FRM_FIELD_LOS_FROM_IN_DAY));
                        value_Mapping.setLosFromInMonth(getInt(FRM_FIELD_LOS_FROM_IN_MONTH));
                        value_Mapping.setLosFromInYear(getInt(FRM_FIELD_LOS_FROM_IN_YEAR));
                        value_Mapping.setLosToInDay(getInt(FRM_FIELD_LOS_TO_IN_DAY));
                        value_Mapping.setLosToInMonth(getInt(FRM_FIELD_LOS_TO_IN_MONTH));
                        value_Mapping.setLosToInYear(getInt(FRM_FIELD_LOS_TO_IN_YEAR));
                        value_Mapping.setLosCurrentDate(getInt(FRM_FIELD_LOS_CURRENT_DATE));
                        value_Mapping.setLosPerCurrentDate(getDate(FRM_FIELD_LOS_PER_CURRENT_DATE));
                        value_Mapping.setTaxMaritalId(getLong(FRM_FIELD_TAX_MARITAL_ID));
						value_Mapping.setRemark(getString(FRM_FIELD_REMARK));
                     
               	}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
    
}
