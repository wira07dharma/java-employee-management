/* 
 * Form Name  	:  FrmSrcSpecialEmployee.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	:  [authorName] 
 * @version  	:  [version] 
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.form.search;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.harisma.entity.search.*;

public class FrmSrcSpecialEmployee extends FRMHandler implements I_FRMInterface, I_FRMType 
{
	private SrcSpecialEmployee srcSpecialEmployee;

	public static final String FRM_NAME_SRCEMPLOYEE		=  "FRM_NAME_SRCEMPLOYEE" ;

	public static final int FRM_FIELD_DEPARTMENT		=  0 ;
	public static final int FRM_FIELD_POSITION		=  1 ;
	public static final int FRM_FIELD_SECTION		=  2 ;
	public static final int FRM_FIELD_LEVEL			=  3 ;
	public static final int FRM_FIELD_COMMDATEFROM		=  4 ;
	public static final int FRM_FIELD_COMMDATETO		=  5 ;
	public static final int FRM_FIELD_WORKYEARFROM		=  6 ;
	public static final int FRM_FIELD_WORKMONTHFROM		=  7 ;
	public static final int FRM_FIELD_WORKYEARTO		=  8 ;
	public static final int FRM_FIELD_WORKMONTHTO		=  9 ;
	public static final int FRM_FIELD_AGEYEARFROM		=  10 ;
	public static final int FRM_FIELD_AGEMONTHFROM		=  11 ;
	public static final int FRM_FIELD_AGEYEARTO		=  12 ;
	public static final int FRM_FIELD_AGEMONTHTO		=  13 ;
	public static final int FRM_FIELD_EDUCATION		=  14 ;
	public static final int FRM_FIELD_RELIGION		=  15 ;
	public static final int FRM_FIELD_MARITAL		=  16 ;
	public static final int FRM_FIELD_BLOOD			=  17 ;
	public static final int FRM_FIELD_LANGUAGE		=  18 ;
	public static final int FRM_FIELD_SEX			=  19 ;
	public static final int FRM_FIELD_RESIGNED		=  20 ;
	public static final int FRM_FIELD_SORTBY		=  21 ;
        public static final int FRM_FIELD_RACE                  =  22 ;
        //update by satrya 2012-11-15
        public static final int FRM_FIELD_ORDER                  =  23 ;
        public static final int FRM_FIELD_COMPANY_ID            = 24;
        
        //update by satrya 2013-08-05
        public static final int FRM_FIELD_RADIO_COMMERCING_DATE            = 25;
        public static final int FRM_FIELD_EMP_CATEGORY           = 26;
        public static final int FRM_FIELD_EMP_DIVISION_ID           = 27;

        public static final int FRM_FIELD_BIRTH_DATE_FROM           = 28;
        public static final int FRM_FIELD_BIRTH_DATE_TO           = 29;
        
        public static final int FRM_FIELD_PROVINSI_ID           = 30;
        public static final int FRM_FIELD_KABUPATEN_ID           = 31;
        public static final int FRM_FIELD_KECAMATAN_ID           = 32;
        public static final int FRM_FIELD_EMP_BIRTH_DAY_MONTH           =33;
        
        public static final int FRM_FIELD_EMP_ADDRESS           =34;
        public static final int FRM_FIELD_ADDRESS_EMP_PERMANET           =35;
        
        public static final int FRM_FIELD_COUNTRY               = 36;
        public static final int FRM_FIELD_COUNTRY_PERMANENT     = 37;
        public static final int FRM_FIELD_PROVINSI_ID_PERMANENT = 38;
        public static final int FRM_FIELD_KABUPATEN_ID_PERMANENT= 39;
        public static final int FRM_FIELD_KECAMATAN_ID_PERMANENT= 40;
        
        public static final int FRM_FIELD_BIRTH_YEAR_FROM           = 41;
        public static final int FRM_FIELD_BIRTH_YEAR_TO           = 42;
        
        //update by satrya 2013-10-16
        public static final int FRM_FIELD_DATE_PERIOD_START          = 43;
        public static final int FRM_FIELD_DATE_PERIOD_END          = 44;
        //update by satrya 2013-11-13
        public static final int FRM_FIELD_DATE_CARRIER_WORK_START = 45;
        public static final int FRM_FIELD_DATE_CARRIER_WORK_END = 46;
        public static final int  FRM_FIELD_CARRIER_EMP_CATEGORY = 47;
      //update by priska
        public static final int FRM_FIELD_RADIO_END_CONTRACT   = 48;
      
	public static final int FRM_FIELD_ENDCONTRACTFROM	=  49 ;
	public static final int FRM_FIELD_ENDCONTRACTTO		=  50 ;
        
        public static final int FRM_FIELD_GRADE = 51;
        public static final int FRM_FIELD_GRADE_LEVEL = 52;
        
        public static final int FRM_FIELD_DISTRICT_ID= 53;
        public static final int FRM_FIELD_DISTRICT_ID_PERMANENT= 54;
        
	public static String[] fieldNames = {
		"FRM_FIELD_DEPARTMENT",  "FRM_FIELD_POSITION",
		"FRM_FIELD_SECTION",  "FRM_FIELD_LEVEL",
		"FRM_FIELD_COMMDATEFROM",  "FRM_FIELD_COMMDATETO",
		"FRM_FIELD_WORKYEARFROM",  "FRM_FIELD_WORKMONTHFROM",
		"FRM_FIELD_WORKYEARTO",  "FRM_FIELD_WORKMONTHTO",
		"FRM_FIELD_AGEYEARFROM",  "FRM_FIELD_AGEMONTHFROM",
		"FRM_FIELD_AGEYEARTO",  "FRM_FIELD_AGEMONTHTO",
		"FRM_FIELD_EDUCATION",  "FRM_FIELD_RELIGION",
		"FRM_FIELD_MARITAL",  "FRM_FIELD_BLOOD",
		"FRM_FIELD_LANGUAGE",  "FRM_FIELD_SEX",
		"FRM_FIELD_RESIGNED",  "FRM_FIELD_SORTBY",
                "FRM_FIELD_RACE","FRM_FIELD_ORDER",//update by satrya 2012-11-15
                "FRM_FIELD_COMPANY_ID",//update by satrya 2013-08-05
                "FRM_FIELD_RADIO_COMERCING_DATE","FRM_FIELD_EMP_CATEGORY_ID","FRM_FIELD_EMP_DIVISION_ID",
                "FRM_FIELD_BIRTH_DATE_FROM","FRM_FIELD_BIRTH_DATE_TO",
                "FRM_FIELD_PROVINSI_ID","FRM_FIELD_KABUPATEN_ID","FRM_FIELD_KECAMATAN_ID",
                "FRM_FIELD_EMP_BIRTH_DAY_MONTH","FRM_FIELD_EMP_ADDRESS","FRM_FIELD_ADDRESS_EMP_PERMANET",
                
                "FRM_FIELD_COUNTRY","FRM_FIELD_COUNTRY_PERMANENT","FRM_FIELD_PROVINSI_ID_PERMANENT",
                "FRM_FIELD_KABUPATEN_ID_PERMANENT","FRM_FIELD_KECAMATAN_ID_PERMANENT",
                "FRM_FIELD_BIRTH_YEAR_FROM",
                "FRM_FIELD_BIRTH_YEAR_TO",
                
                //update by satrya 2013-10-16
                "FRM_FIELD_DATE_PERIOD_START",
                "FRM_FIELD_DATE_PERIOD_END",
                //update by satrya 2013-11-13
                "FRM_FIELD_DATE_CARRIER_WORK_START",
                "FRM_FIELD_DATE_CARRIER_WORK_END",
                "FRM_FIELD_CARRIER_EMP_CATEGORY",
                //update by priska 2014-10-30
                "FRM_FIELD_RADIO_END_CONTRACT",
                "FRM_FIELD_ENDCONTRACTFROM",  "FRM_FIELD_ENDCONTRACTTO", "FRM_FIELD_GRADE",
                "FRM_FIELD_GRADE_LEVEL","FRM_FIELD_DISTRICT_ID","FRM_FIELD_DISTRICT_ID_PERMANENT"
	} ;

	public static int[] fieldTypes = {
		TYPE_LONG,  TYPE_LONG,
		TYPE_LONG,  TYPE_LONG,
		TYPE_DATE,  TYPE_DATE,
		TYPE_INT,  TYPE_INT,
		TYPE_INT,  TYPE_INT,
		TYPE_INT,  TYPE_INT,
		TYPE_INT,  TYPE_INT,
		TYPE_LONG,  TYPE_LONG,
		TYPE_LONG,  TYPE_INT,
		TYPE_LONG,  TYPE_INT,
		TYPE_INT,  TYPE_INT, TYPE_INT,//update by satrya 2012-11-15
                TYPE_LONG,
                TYPE_STRING,TYPE_LONG,TYPE_LONG
                ,TYPE_DATE,TYPE_DATE,
                TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_INT,
                TYPE_STRING,TYPE_STRING,
                TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,TYPE_LONG,
                TYPE_INT,TYPE_INT,
                //update by satrya 2013-10-16
                TYPE_DATE,TYPE_DATE,
                //update by satrya 2013-11-13
                TYPE_DATE,TYPE_DATE,TYPE_LONG, 
                //update by priska 2014-10-30
                TYPE_STRING,
                TYPE_DATE,  TYPE_DATE, TYPE_LONG,
                TYPE_LONG,TYPE_LONG,TYPE_LONG
                
	} ;

	public FrmSrcSpecialEmployee(){
	}
	public FrmSrcSpecialEmployee(SrcSpecialEmployee srcSpecialEmployee){
		this.srcSpecialEmployee = srcSpecialEmployee;
	}

	public FrmSrcSpecialEmployee(HttpServletRequest request, SrcSpecialEmployee srcSpecialEmployee){
		super(new FrmSrcSpecialEmployee(srcSpecialEmployee), request);
		this.srcSpecialEmployee = srcSpecialEmployee;
	}

	public String getFormName() { return FRM_NAME_SRCEMPLOYEE; } 

	public int[] getFieldTypes() { return fieldTypes; }

	public String[] getFieldNames() { return fieldNames; } 

	public int getFieldSize() { return fieldNames.length; } 

	public SrcSpecialEmployee getEntityObject(){ return srcSpecialEmployee; }

	public void requestEntityObject(SrcSpecialEmployee srcSpecialEmployee) {
		try{
			this.requestParam();
			srcSpecialEmployee.setDepartment(getLong(FRM_FIELD_DEPARTMENT));
			srcSpecialEmployee.setPosition(getLong(FRM_FIELD_POSITION));
			srcSpecialEmployee.setSection(getLong(FRM_FIELD_SECTION));
			srcSpecialEmployee.setLevel(getLong(FRM_FIELD_LEVEL));
			srcSpecialEmployee.setCommdatefrom(getDate(FRM_FIELD_COMMDATEFROM));
			srcSpecialEmployee.setCommdateto(getDate(FRM_FIELD_COMMDATETO));
			srcSpecialEmployee.setWorkyearfrom(getInt(FRM_FIELD_WORKYEARFROM));
			srcSpecialEmployee.setWorkmonthfrom(getInt(FRM_FIELD_WORKMONTHFROM));
			srcSpecialEmployee.setWorkyearto(getInt(FRM_FIELD_WORKYEARTO));
			srcSpecialEmployee.setWorkmonthto(getInt(FRM_FIELD_WORKMONTHTO));
			srcSpecialEmployee.setAgeyearfrom(getInt(FRM_FIELD_AGEYEARFROM));
			srcSpecialEmployee.setAgemonthfrom(getInt(FRM_FIELD_AGEMONTHFROM));
			srcSpecialEmployee.setAgeyearto(getInt(FRM_FIELD_AGEYEARTO));
			srcSpecialEmployee.setAgemonthto(getInt(FRM_FIELD_AGEMONTHTO));
			srcSpecialEmployee.setEducation(getLong(FRM_FIELD_EDUCATION));
			srcSpecialEmployee.setReligion(getLong(FRM_FIELD_RELIGION));
			srcSpecialEmployee.setMarital(getLong(FRM_FIELD_MARITAL));
			srcSpecialEmployee.setBlood(getInt(FRM_FIELD_BLOOD));
			srcSpecialEmployee.setLanguage(getLong(FRM_FIELD_LANGUAGE));
			srcSpecialEmployee.setSex(getInt(FRM_FIELD_SEX));
			srcSpecialEmployee.setResigned(getInt(FRM_FIELD_RESIGNED));
			srcSpecialEmployee.setSortby(getInt(FRM_FIELD_SORTBY));
                        srcSpecialEmployee.setRace(getLong(FRM_FIELD_RACE));
                        srcSpecialEmployee.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
                        //update by satrya 2013-08-05
                        srcSpecialEmployee.setEmpCategoryId(getLong(FRM_FIELD_EMP_CATEGORY));
                        srcSpecialEmployee.setDivisionId(getLong(FRM_FIELD_EMP_DIVISION_ID));
                        
                        srcSpecialEmployee.setDtBirthFrom(getDate(FRM_FIELD_BIRTH_DATE_FROM));
                        srcSpecialEmployee.setDtBirthTo(getDate(FRM_FIELD_BIRTH_DATE_TO));
                        
                        srcSpecialEmployee.setKabupatenId(getLong(FRM_FIELD_KABUPATEN_ID));
                        srcSpecialEmployee.setKecamatanId(getLong(FRM_FIELD_KECAMATAN_ID));
                        srcSpecialEmployee.setProvinsiId(getLong(FRM_FIELD_PROVINSI_ID));
                        srcSpecialEmployee.setBirthMonth(getInt(FRM_FIELD_EMP_BIRTH_DAY_MONTH)); 
                        
                        srcSpecialEmployee.setEmpAddress(getString(FRM_FIELD_EMP_ADDRESS));
                        srcSpecialEmployee.setEmpPermanentAddress(getString(FRM_FIELD_ADDRESS_EMP_PERMANET));
                        
                        srcSpecialEmployee.setCountryId(getLong(FRM_FIELD_COUNTRY));
                        srcSpecialEmployee.setCountryIdPermanent(getLong(FRM_FIELD_COUNTRY_PERMANENT));
                        srcSpecialEmployee.setKabupatenIdPermanent(getLong(FRM_FIELD_KABUPATEN_ID_PERMANENT));
                        srcSpecialEmployee.setKecamatanIdPermanent(getLong(FRM_FIELD_KECAMATAN_ID_PERMANENT));
                        srcSpecialEmployee.setProvinsiIdPermanent(getLong(FRM_FIELD_PROVINSI_ID_PERMANENT));
                        
                        srcSpecialEmployee.setBirthYearFrom(getInt(FRM_FIELD_BIRTH_DATE_FROM));
                        srcSpecialEmployee.setBirthYearTo(getInt(FRM_FIELD_BIRTH_DATE_TO));
                        
                        //update by satrya 2013-10-16
                        srcSpecialEmployee.setDtPeriodStart(getDate(FRM_FIELD_DATE_PERIOD_START));
                        srcSpecialEmployee.setDtPeriodEnd(getDate(FRM_FIELD_DATE_PERIOD_END));
                        
                        //update by satrya 2013-11-13
                        srcSpecialEmployee.setDtCarrierWorkStart(getDate(FRM_FIELD_DATE_CARRIER_WORK_START));
                        srcSpecialEmployee.setDtCarrierWorkEnd(getDate(FRM_FIELD_DATE_CARRIER_WORK_END));
                        srcSpecialEmployee.setCarrierCategoryEmp(getLong(FRM_FIELD_CARRIER_EMP_CATEGORY));
                        
			srcSpecialEmployee.setEndcontractfrom(getDate(FRM_FIELD_ENDCONTRACTFROM));
			srcSpecialEmployee.setEndcontractto(getDate(FRM_FIELD_ENDCONTRACTTO));
                        srcSpecialEmployee.setGradeLevel(getLong(FRM_FIELD_GRADE));
                        srcSpecialEmployee.setGradeLevel(getLong(FRM_FIELD_GRADE_LEVEL));
                        
                        srcSpecialEmployee.setDistrictId(getLong(FRM_FIELD_DISTRICT_ID));
                        srcSpecialEmployee.setDistrictIdPermanent(getLong(FRM_FIELD_DISTRICT_ID_PERMANENT));
		}catch(Exception e){
			System.out.println("Error on requestEntityObject : "+e.toString());
		}
	}
}
