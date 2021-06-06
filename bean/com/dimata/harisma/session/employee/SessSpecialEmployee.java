/* 
 * Session Name  	:  SessSpecialEmployee.java 
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
package com.dimata.harisma.session.employee;
/* java package */ 
import java.io.*; 
import java.util.*; 
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*; 
/* qdep package */ 
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;
import com.dimata.util.*;
import com.dimata.gui.jsp.*;
/* project package */
import com.dimata.harisma.entity.search.*;
import com.dimata.harisma.form.search.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.locker.*;
import com.dimata.harisma.entity.masterdata.location.PstLocation;
import com.dimata.harisma.entity.payroll.PayGeneral;
import com.dimata.harisma.entity.payroll.PstPayEmpLevel;
import com.dimata.harisma.entity.payroll.PstPayGeneral;

public class SessSpecialEmployee{
    public static final String SESS_SRC_SPECIAL_EMPLOYEE = "SESSION_SRC_SPECIAL_EMPLOYEE";
    public static final double DAYS_IN_A_MONTH=30.4375;//365.25 hari /12 bulan  
    
    private static Vector logicParser(String text) {
        Vector vector = LogicParser.textSentence(text);
        for (int i = 0; i < vector.size(); i++) {
            String code = (String) vector.get(i);
            if (((vector.get(vector.size() - 1)).equals(LogicParser.SIGN))
                    && ((vector.get(vector.size() - 1)).equals(LogicParser.ENGLISH))) {
                vector.remove(vector.size() - 1);
            }
        }

        return vector;
    }
    
 public static Vector searchSpecialEmployee(SearchSpecialQuery searchSpecialQuery){
    return searchSpecialEmployee(searchSpecialQuery, 0, 0);
 }
    //public static Vector searchSpecialEmployee(SrcSpecialEmployee srcspecialemployee, int start, int recordToGet){
    public static Vector searchSpecialEmployee(SearchSpecialQuery searchSpecialQuery,int limit,int recordToget) {
        //public static Vector searchSpecialEmployee(Vector vparam) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1,1);
        
        try {
            String sql = " SELECT "+
" EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
+",COMP."+PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMPANY_NAME]
+",DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION]
+",DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
+",SEC."+PstSection.fieldNames[PstSection.FLD_SECTION]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_NR]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
+",EMP.*"
                    //add by priska 20150831
+",COMP1."+PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMPANY_NAME]
+",DIVX1."+PstDivision.fieldNames[PstDivision.FLD_DIVISION]
+",DEPT1."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
+",SEC1."+PstSection.fieldNames[PstSection.FLD_SECTION]  
+",POST1."+PstPosition.fieldNames[PstPosition.FLD_POSITION]                  
                    
+",EMPCAT."+PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
+",LEV."+PstLevel.fieldNames[PstLevel.FLD_LEVEL]
+",POST."+PstPosition.fieldNames[PstPosition.FLD_POSITION]
+",REL."+PstReligion.fieldNames[PstReligion.FLD_RELIGION]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SEX]
+",MAR."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_STATUS]
+",MAR."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_CODE]
+",MAR."+PstMarital.fieldNames[PstMarital.FLD_NUM_OF_CHILDREN]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
+",RSGNSON."+PstResignedReason.fieldNames[PstResignedReason.FLD_RESIGNED_REASON]
+",RSGNSON."+PstResignedReason.fieldNames[PstResignedReason.FLD_RESIGNED_REASON_ID]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC]
+",LOCLOC."+PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION]
+",LOC."+PstLocker.fieldNames[PstLocker.FLD_LOCKER_NUMBER]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_PHONE]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMERGENCY]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_NAME_EMG]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
+",RAC."+PstRace.fieldNames[PstRace.FLD_RACE_NAME]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FATHER]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_MOTHER]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
+",MARTAX."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_STATUS]
+",MARTAX."+PstMarital.fieldNames[PstMarital.FLD_NUM_OF_CHILDREN]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_PARENTS_ADDRESS]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_CURIER]
+",NEGARA."+PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA]
+",PRO."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI]
+",KAB."+PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN]
+",KEC."+PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN]
+",DIS."+PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT]
+",NEGARAPERMANENT."+PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA]
+",PROPERMANENT."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI]
+",KABPERMANENT."+PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN]
+",KECPERMANENT."+PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN]
+",DISPERMANENT."+PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT]
                    //by priska 2014-10-30
+",LOCA."+PstLocation.fieldNames[PstLocation.FLD_NAME]
+",GRADE."+PstGradeLevel.fieldNames[PstGradeLevel.FLD_GRADE_CODE]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMAIL_ADDRESS]
+",EMP."+PstEmployee.fieldNames[PstEmployee.FLD_NO_KK];
            // parameter : EDUCATION = 4
            String[] educationId = searchSpecialQuery.getArrEducationId(0);
//            if(searchSpecialQuery.getArrEducationId(0) !=null){
//                //if((String[]) vparam.get(5)!=null){
//                   educationId = searchSpecialQuery.getArrEducationId(0);
//                if (!(educationId[0].equals("0"))) {
//                    sql +=   ", EDU."+PstEducation.fieldNames[PstEducation.FLD_EDUCATION];
//                    sql +=   ", EDU."+PstEducation.fieldNames[PstEducation.FLD_EDUCATION_DESC];
//                }
//            }
            
            // parameter : LANGUAGE = 8
            //String[] languageId = (String[]) vparam.get(9);
             String[] languageId = searchSpecialQuery.getArrLanguageId(0);
//          if(languageId!=null){
//            if (!(languageId[0].equals("0"))) {
//                sql +=   ", LANG."+PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE];
//            }
//          }
            
//                sql +=   " FROM "+PstEmployee.TBL_HR_EMPLOYEE + " EMP "+
//                         " , "+PstDepartment.TBL_HR_DEPARTMENT +" DEPT "+
//                         " , "+PstPosition.TBL_HR_POSITION + " POST "+
//			 " , "+PstEmpCategory.TBL_HR_EMP_CATEGORY + " EMPCAT "+
//                         " , "+PstSection.TBL_HR_SECTION+ " SEC "+
//                         " , "+PstLevel.TBL_HR_LEVEL + " LEV "+
//                         " , "+PstMarital.TBL_HR_MARITAL + " MAR "+
//                         " , "+PstReligion.TBL_HR_RELIGION + " REL " +
//                         " , "+PstRace.TBL_RACE + " RAC ";
                         
            //update by satrya 2012-12-24
            sql += " FROM "+ PstEmployee.TBL_HR_EMPLOYEE +" AS EMP "   
+ " LEFT JOIN  " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +")"
+ " LEFT JOIN  " + PstPosition.TBL_HR_POSITION   + " AS POST  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POST."+PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]+")"
+ " LEFT JOIN  " + PstEmpCategory.TBL_HR_EMP_CATEGORY +"  AS EMPCAT ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = EMPCAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]+")" 
+ " LEFT JOIN  " + PstSection.TBL_HR_SECTION + " AS SEC  ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID]+")" 
                    
                    //add by priska 20150831
+ " LEFT JOIN  " + PstPayGeneral.TBL_PAY_GENERAL + " AS COMP1  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_COMPANY_ID] + " = COMP1."+PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +")"
+ " LEFT JOIN  " + PstDivision.TBL_HR_DIVISION + " AS DIVX1 ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DIVISION_ID]+" = DIVX1."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+")"
+ " LEFT JOIN  " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT1  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_DEPARTMENT_ID] + " = DEPT1."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +")"
+ " LEFT JOIN  " + PstSection.TBL_HR_SECTION + " AS SEC1  ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_SECTION_ID] + " = SEC1." + PstSection.fieldNames[PstSection.FLD_SECTION_ID]+")" 
+ " LEFT JOIN  " + PstPosition.TBL_HR_POSITION   + " AS POST1  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_WORK_ASSIGN_POSITION_ID] + " = POST1."+PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]+")"
                         
                    
+ " LEFT JOIN  " + PstLevel.TBL_HR_LEVEL + " AS LEV   ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]+ ")" 
+ " LEFT JOIN  " + PstMarital.TBL_HR_MARITAL + " AS MAR  ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID] + "  = MAR." + PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID]+ ")" 
+ " LEFT JOIN  " + PstReligion.TBL_HR_RELIGION + " AS REL  ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]+ " = REL." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID]+ ")" 
+ " LEFT JOIN  " + PstRace.TBL_RACE + " AS RAC ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RACE] + " = RAC."+ PstRace.fieldNames[PstRace.FLD_RACE_ID]+ ")"
                    //update by satrrya 2013-07-30
+ " LEFT JOIN  " + PstDivision.TBL_HR_DIVISION + " AS DIVX ON (DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+"=EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+" ) "
+ " LEFT JOIN  " + PstPayGeneral.TBL_PAY_GENERAL + " AS COMP ON (COMP."+PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID]+"=DIVX."+PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+")"
+ " LEFT JOIN  " + PstResignedReason.TBL_HR_RESIGNED_REASON + " AS RSGNSON ON RSGNSON."+PstResignedReason.fieldNames[PstResignedReason.FLD_RESIGNED_REASON_ID]+"=EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID]
+ " LEFT JOIN  " + PstLocker.TBL_HR_LOCKER + " AS LOC ON LOC."+PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]+"=EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]
+ " LEFT JOIN  " + PstLockerLocation.TBL_HR_LOCKER_LOCATION + " AS LOCLOC ON LOCLOC."+PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION_ID]+"=LOC."+PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]
+ " LEFT JOIN  " + PstMarital.TBL_HR_MARITAL + " AS MARTAX  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID] + "  = MARTAX."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID]+")"
                    
+ " LEFT JOIN  " + PstNegara.TBL_BKD_NEGARA + " AS NEGARA  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_COUNTRY_ID] + "  = NEGARA."+PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA]+")"
+ " LEFT JOIN  " + PstProvinsi.TBL_HR_PROPINSI + " AS PRO  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PROVINCE_ID] + "  = PRO."+PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]+")"
+ " LEFT JOIN  " + PstKabupaten.TBL_HR_KABUPATEN + " AS KAB  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_REGENCY_ID] + "  = KAB."+PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]+")"
+ " LEFT JOIN  " + PstKecamatan.TBL_HR_KECAMATAN + " AS KEC  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_SUBREGENCY_ID] + "  = KEC."+PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN]+")"
+ " LEFT JOIN  " + PstDistrict.TBL_HR_DISTRICT + " AS DIS  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_DISTRICT_ID] + "  = DIS."+PstDistrict.fieldNames[PstDistrict.FLD_DISTRICT_ID]+")"

+ " LEFT JOIN  " + PstNegara.TBL_BKD_NEGARA + " AS NEGARAPERMANENT  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_COUNTRY_ID] + "  = NEGARAPERMANENT."+PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA]+")"
+ " LEFT JOIN  " + PstProvinsi.TBL_HR_PROPINSI + " AS PROPERMANENT  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_PROVINCE_ID] + "  = PROPERMANENT."+PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]+")"
+ " LEFT JOIN  " + PstKabupaten.TBL_HR_KABUPATEN + " AS KABPERMANENT  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_REGENCY_ID] + "  = KABPERMANENT."+PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]+")"
+ " LEFT JOIN  " + PstKecamatan.TBL_HR_KECAMATAN + " AS KECPERMANENT  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_SUBREGENCY_ID] + "  = KECPERMANENT."+PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN]+")"
+ " LEFT JOIN  " + PstDistrict.TBL_HR_DISTRICT + " AS DISPERMANENT  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_DISTRICT_ID] + "  = DISPERMANENT."+PstDistrict.fieldNames[PstDistrict.FLD_DISTRICT_ID]+")"   
//update ganki priska
+ " LEFT JOIN  " + PstLocation.TBL_P2_LOCATION + " AS LOCA  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID] + "  = LOCA."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+")" 
+ " LEFT JOIN  " + PstGradeLevel.TBL_HR_GRADE_LEVEL + " AS GRADE  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID] + "  = GRADE."+PstGradeLevel.fieldNames[PstGradeLevel.FLD_GRADE_LEVEL_ID]+")"; 

            
            //update by satrya 2013-11-13
if(searchSpecialQuery.getDtCarrierWorkStart()!= null && searchSpecialQuery.getDtCarrierWorkEnd() != null || searchSpecialQuery.getCarrierCategoryEmp()!=0){
sql = sql + " INNER JOIN " + PstCareerPath.TBL_HR_WORK_HISTORY_NOW + " AS CRR  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "  = CRR."+PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]+")";
}

            // parameter : EDUCATION = 4
if(educationId!=null){
 if (!(educationId[0].equals("0"))) {
     sql +=   " LEFT JOIN "+PstEmpEducation.TBL_HR_EMP_EDUCATION + " AS EMPEDU ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+"=EMPEDU."+PstEmpEducation.fieldNames[PstEmpEducation.FLD_EMPLOYEE_ID];//", "+PstEducation.TBL_HR_EDUCATION + " EDU "; 
 }
} 
            // parameter : LANGUAGE = 8
if(languageId!=null){
 if (!(languageId[0].equals("0"))) {
     sql += " LEFT JOIN "+PstEmpLanguage.TBL_HR_EMP_LANGUAGE + " AS EMPLANG ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+"=EMPLANG."+PstEmpLanguage.fieldNames[PstEmpLanguage.FLD_EMPLOYEE_ID] ;//  ", "+PstLanguage.TBL_HR_LANGUAGE + " LANG ";
     //sql +=   ", "+PstEmpLanguage.TBL_HR_EMP_LANGUAGE + " EMPLANG ";
 }
}
                 sql +=   " WHERE ";
                         //" (1 = 1) " +
                         /*" AND  (1=1) " +
                         " AND (1=1) " +
                         " AND (1=1) " +
                         " AND (1=1) " +
                         " AND (1=1) " +
                         " AND (1=1) " +*/
                         //" AND (1=1)" ;
             /**
                  *  sql +=   " WHERE "+
                         " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+
                         " = DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+
                         " AND  EMP."+PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]+
                         " = POST."+PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]+
                         " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]+
                         " = EMPCAT."+PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]+
                         " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+
                         " = SEC."+PstSection.fieldNames[PstSection.FLD_SECTION_ID]+
                         " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+
                         " = LEV."+PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]+
                         " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]+
                         " = MAR."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID]+
                         " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]+
                         " = REL."+PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID]+
                         " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RACE]+
                         " = RAC."+PstRace.fieldNames[PstRace.FLD_RACE_ID];
             
                  */
               // sql +=   " WHERE ";
//            if(educationId!=null){
//                if (!(educationId[0].equals("0"))) {
//                    sql +=   " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
//                    sql +=   " = EMPEDU."+PstEmpEducation.fieldNames[PstEmpEducation.FLD_EMPLOYEE_ID];
//                    sql +=   " AND EDU."+PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID];
//                    sql +=   " = EMPEDU."+PstEmpEducation.fieldNames[PstEmpEducation.FLD_EDUCATION_ID];
//                }
//            }
//           if(languageId!=null){
//            if (!(languageId[0].equals("0"))) {
//                sql +=   " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
//                sql +=   " = EMPLANG."+PstEmpLanguage.fieldNames[PstEmpLanguage.FLD_EMPLOYEE_ID];
//                sql +=   " AND LANG."+PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE_ID];
//                sql +=   " = EMPLANG."+PstEmpLanguage.fieldNames[PstEmpLanguage.FLD_LANGUAGE_ID];
//            }
//           }
            
            String whereClause = "(1=1)";
           if (searchSpecialQuery.getBirthMonth() > 0) {
                    whereClause = whereClause + " AND MONTH(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                            + ") = '" + (searchSpecialQuery.getBirthMonth()) + "'  ";
                }  
          if ((searchSpecialQuery.getFullNameEmp() != null) && (searchSpecialQuery.getFullNameEmp().length() > 0)) {
                Vector vectName = logicParser(searchSpecialQuery.getFullNameEmp());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }

            if ((searchSpecialQuery.getAddrsEmployee() != null) && (searchSpecialQuery.getAddrsEmployee().length() > 0)) {
                Vector vectAddr = logicParser(searchSpecialQuery.getAddrsEmployee());
                if (vectAddr != null && vectAddr.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectAddr.size(); i++) {
                        String str = (String) vectAddr.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }

            if ((searchSpecialQuery.getEmpnumber() != null) && (searchSpecialQuery.getEmpnumber().length() > 0)) {
                Vector vectNum = logicParser(searchSpecialQuery.getEmpnumber());
                if (vectNum != null && vectNum.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }
            
            
            if ((searchSpecialQuery.getAddrsEmployee() != null) && (searchSpecialQuery.getAddrsEmployee().length() > 0)) {
                Vector vectName = logicParser(searchSpecialQuery.getAddrsEmployee());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }
            
            if ((searchSpecialQuery.getAddressPermanent() != null) && (searchSpecialQuery.getAddressPermanent().length() > 0)) {
                Vector vectName = logicParser(searchSpecialQuery.getAddressPermanent());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }
            
            //update by satrya 2013-08-15
             //
             String[] CountyId = searchSpecialQuery.getArrCountryId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (CountyId!=null && !(CountyId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<CountyId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_COUNTRY_ID]+
                                " = "+ CountyId[i] + " OR ";
                    if (i==(CountyId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] provinsiId = searchSpecialQuery.getArrProvinsiId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (provinsiId!=null && !(provinsiId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<provinsiId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PROVINCE_ID]+
                                " = "+ provinsiId[i] + " OR ";
                    if (i==(provinsiId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            String[] kabupatenId = searchSpecialQuery.getArrKabupatenId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kabupatenId!=null && !(kabupatenId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kabupatenId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_REGENCY_ID]+
                                " = "+ kabupatenId[i] + " OR ";
                    if (i==(kabupatenId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] kecamatanId = searchSpecialQuery.getArrKecamatanId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kecamatanId!=null && !(kecamatanId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kecamatanId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_SUBREGENCY_ID]+
                                " = "+ kecamatanId[i] + " OR ";
                    if (i==(kecamatanId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            
            String[] districtId = searchSpecialQuery.getArrDistrictId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (districtId!=null && !(districtId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<districtId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_DISTRICT_ID]+
                                " = "+ districtId[i] + " OR ";
                    if (i==(districtId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] districtIdPermanent = searchSpecialQuery.getArrDistrictIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (districtIdPermanent!=null && !(districtIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<districtIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_DISTRICT_ID]+
                                " = "+ districtIdPermanent[i] + " OR ";
                    if (i==(districtIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            
            //
             String[] CountyIdPermanent = searchSpecialQuery.getArrCountryIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (CountyIdPermanent!=null && !(CountyIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<CountyIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_COUNTRY_ID]+
                                " = "+ CountyIdPermanent[i] + " OR ";
                    if (i==(CountyIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] provinsiIdPermanent = searchSpecialQuery.getArrProvinsiIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (provinsiIdPermanent!=null && !(provinsiIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<provinsiIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_PROVINCE_ID]+
                                " = "+ provinsiIdPermanent[i] + " OR ";
                    if (i==(provinsiIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            String[] kabupatenIdPermanent = searchSpecialQuery.getArrKabupatenIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kabupatenIdPermanent!=null && !(kabupatenIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kabupatenIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_REGENCY_ID]+
                                " = "+ kabupatenIdPermanent[i] + " OR ";
                    if (i==(kabupatenIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] kecamatanIdPermanent = searchSpecialQuery.getArrKecamatanIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kecamatanIdPermanent!=null && !(kecamatanIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kecamatanIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_SUBREGENCY_ID]+
                                " = "+ kecamatanIdPermanent[i] + " OR ";
                    if (i==(kecamatanIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            //
            
            if (searchSpecialQuery.getArrDivision(0)!=null) {
                String[] divisionId = searchSpecialQuery.getArrDivision(0);
                    if (! (divisionId!=null && (divisionId[0].equals("0")))) {
                    whereClause += " AND (";
                    for (int i=0; i<divisionId.length; i++) {
                        whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+
                                    " = "+ divisionId[i] + " OR ";
                        if (i==(divisionId.length-1)) {
                            whereClause = whereClause.substring(0, whereClause.length()-4);
                        }
                    }
                    whereClause += " )";
                }
               
            }
            
            if ((searchSpecialQuery.getBirthYearFrom() != 0) || (searchSpecialQuery.getBirthYearTo() !=0)) {
                    if(searchSpecialQuery.getBirthYearFrom() != 0 && searchSpecialQuery.getBirthYearTo()==0){
                         whereClause = whereClause + " AND YEAR(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                            + ") = '" + (searchSpecialQuery.getBirthMonth()) + "'  ";
                         
                    }else if(searchSpecialQuery.getBirthYearTo() != 0 && searchSpecialQuery.getBirthYearFrom() == 0){
                        whereClause = whereClause + " AND YEAR(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                            + ") = '" + (searchSpecialQuery.getBirthYearTo()) + "'  ";
                    }else{
                        whereClause = whereClause + " AND YEAR(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + ") BETWEEN '"
                        + searchSpecialQuery.getBirthYearFrom() + "' AND '"
                        + searchSpecialQuery.getBirthYearTo() + "'  ";
                        
                    }
            }
            
              //update by satrya 2013-11-13
              if ((searchSpecialQuery.getDtCarrierWorkStart()!= null && searchSpecialQuery.getDtCarrierWorkEnd() != null)) {
                   
                        whereClause = whereClause + " AND \""+Formater.formatDate(searchSpecialQuery.getDtCarrierWorkEnd(), "yyyy-MM-dd")+"\" > CRR." 
                        + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]+ " AND CRR."
                        + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO] + " > \""+Formater.formatDate(searchSpecialQuery.getDtCarrierWorkStart(), "yyyy-MM-dd")+"\"";
                   
            }
            if(searchSpecialQuery.getCarrierCategoryEmp()!=0){
                whereClause = whereClause + " AND CRR."+PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_CATEGORY_ID]+"="+searchSpecialQuery.getCarrierCategoryEmp();
            }
          
            
             if ((searchSpecialQuery.getDtBirthFrom() != null) || (searchSpecialQuery.getDtBirthTo() != null)) {
                    if(searchSpecialQuery.getDtBirthFrom() != null && searchSpecialQuery.getDtBirthTo() == null){
                        whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthFrom(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthFrom(), "yyyy-MM-dd") + "'  ";
                    }else if(searchSpecialQuery.getDtBirthTo() != null && searchSpecialQuery.getDtBirthFrom() == null){
                        whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthTo(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthTo(), "yyyy-MM-dd") + "'  ";
                    }else{
                        whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthFrom(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthTo(), "yyyy-MM-dd") + "'  ";
                    }
            }
             
//            if (searchSpecialQuery.getMonthBirth()!=0) {
//                 whereClause = whereClause + " MONTH(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
//                            + ") = '" + (searchSpecialQuery.getMonthBirth()) + "' AND ";
//               
//            }
            
            
//            if (!searchSpecialQuery.isBirthdayChecked()) {
//                System.out.println("Bithday checked : " + searchSpecialQuery.isBirthdayChecked());
//               // System.out.println("Bithday : " + searchSpecialQuery.getBirthday());
//                System.out.println("Bithmon : " + searchSpecialQuery.getBirthMonth());

               
//            }
            
            if (searchSpecialQuery.getSalaryLevel()!=null && searchSpecialQuery.getSalaryLevel().length() > 0) {
                whereClause = whereClause + " AND LEV." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                        + " = '" + searchSpecialQuery.getSalaryLevel() + "'  ";
            }
            
            if (searchSpecialQuery.getArrEmpCategory(0)!=null) {
                String[] empCategory = searchSpecialQuery.getArrEmpCategory(0);
                if (! (empCategory!=null && (empCategory[0].equals("0")))) {
                    whereClause += " AND (";
                    for (int i=0; i<empCategory.length; i++) {
                        whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]+
                                    " = "+ empCategory[i] + " OR ";
                        if (i==(empCategory.length-1)) {
                            whereClause = whereClause.substring(0, whereClause.length()-4);
                        }
                    }
                    whereClause += " )";
                }
                
            }
            //update by satrya 2013-10-17
            boolean noresign=true;
            if(/*searchSpecialQuery.getDtPeriodStart()!=null &&*/ searchSpecialQuery.getDtPeriodEnd()!=null){
                noresign=false;
                whereClause = whereClause + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
                /*+ " BETWEEN \""+Formater.formatDate(searchSpecialQuery.getDtPeriodStart(), "yyyy-MM-dd") + "\""
                +" AND \""+Formater.formatDate(searchSpecialQuery.getDtPeriodEnd(), "yyyy-MM-dd")+"\"  "*/
                + " <= \" "+Formater.formatDate(searchSpecialQuery.getDtPeriodEnd(), "yyyy-MM-dd")+"\" "
                + " AND ((EMP."
                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="+PstEmployee.NO_RESIGN+") OR  "
                + " (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+ "="+PstEmployee.NO_RESIGN 
                + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+ ">=\""+Formater.formatDate(searchSpecialQuery.getDtPeriodEnd(), "yyyy-MM-dd")+"\"))";
            }
            if ( noresign && (searchSpecialQuery.getiResigned()==1) && (searchSpecialQuery.getStartResign() != null) && (searchSpecialQuery.getEndResign() != null)) {
                whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getStartResign(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getEndResign(), "yyyy-MM-dd") + "'  ";
            }

            // parameter : DEPARTMENT = 0
          if(searchSpecialQuery.getArrDepartmentId(0)!=null){
              //if((String[]) vparam.get(1)!=null){
            String[] departmentId = searchSpecialQuery.getArrDepartmentId(0);
            //String[] departmentId = (String[]) vparam.get(1);
            if (departmentId!=null && ! ( (departmentId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<departmentId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+
                                " = "+ departmentId[i] + " OR ";
                    if (i==(departmentId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
          }
            // parameter : POSITION = 2
            String[] positionId = searchSpecialQuery.getArrPositionId(0);
            //String[] positionId = (String[]) vparam.get(2);
            if (positionId!=null && !( (positionId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<positionId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]+
                                " = "+ positionId[i] + " OR ";
                    if (i==(positionId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : LOCATION = 2
            String[] locationId = searchSpecialQuery.getArrLocationId(0);
            //String[] locationId = (String[]) vparam.get(2);
            if (locationId!=null && !( (locationId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<locationId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID]+
                                " = "+ locationId[i] + " OR ";
                    if (i==(locationId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            if (searchSpecialQuery.getArrPayrollGroupId(0)!=null) {
                String[] payrollGId = searchSpecialQuery.getArrPayrollGroupId(0);
                    if (! (payrollGId!=null && (payrollGId[0].equals("0")))) {
                    whereClause += " AND (";
                    for (int i=0; i<payrollGId.length; i++) {
                        whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+
                                    " = "+ payrollGId[i] + " OR ";
                        if (i==(payrollGId.length-1)) {
                            whereClause = whereClause.substring(0, whereClause.length()-4);
                        }
                    }
                    whereClause += " )";
                }
               
            }
            // parameter : GRADE = 2
            String[] gradeId = searchSpecialQuery.getArrGradeId(0);
            //String[] gradeId = (String[]) vparam.get(2);
            if (gradeId!=null && !( (gradeId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<gradeId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID]+
                                " = "+ gradeId[i] + " OR ";
                    if (i==(gradeId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            
            String[] companyId = searchSpecialQuery.getArrCompany(0);
            //String[] positionId = (String[]) vparam.get(2);
            if (companyId!=null && !( (companyId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<companyId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+
                                " = "+ companyId[i] + " OR ";
                    if (i==(companyId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : SECTION = 3
            String[] sectionId = searchSpecialQuery.getArrSectionId(0);
            //String[] sectionId = (String[]) vparam.get(3);
            if (sectionId!=null && !( (sectionId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<sectionId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+
                                " = "+ sectionId[i] + " OR ";
                    if (i==(sectionId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : LEVEL = 4
            String[] levelId = searchSpecialQuery.getArrLevelId(0);
            //String[] levelId = (String[]) vparam.get(4);
            if (levelId!=null && !( (levelId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<levelId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+
                                " = "+ levelId[i] + " OR ";
                    if (i==(levelId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : EDUCATION = 5
            //String[] educationId = (String[]) vparam.get(4);
          if(educationId!=null){
            if (educationId!=null && !( (educationId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<educationId.length; i++) {
                    whereClause = whereClause + " EMPEDU."+PstEmpEducation.fieldNames[PstEmpEducation.FLD_EDUCATION_ID]+
                               " = '"+ educationId[i] + "' OR ";
                    if (i==(educationId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
          }
            // parameter : RELIGION = 6
            String[] religionId = searchSpecialQuery.getArrReligionId(0);
            //String[] religionId = (String[]) vparam.get(6);
           
            if (religionId!=null && !(religionId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<religionId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]+
                                " = "+ religionId[i] + " OR ";
                    if (i==(religionId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : MARITAL = 7
            String[] maritalId = searchSpecialQuery.getArrMaritalId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (maritalId!=null && !(maritalId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<maritalId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]+
                                " = "+ maritalId[i] + " OR ";
                    if (i==(maritalId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : BLOOD = 8
          if(searchSpecialQuery.getArrBlood(0)!=null){
              //if((String[]) vparam.get(8)!=null){
            String[] bloodId = searchSpecialQuery.getArrBlood(0);
            if (bloodId!=null && !( (bloodId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<bloodId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]+
                                " = '"+ bloodId[i] + "' OR ";
                    if (i==(bloodId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
          } 
             // parameter : RACE = 24
            String[] raceId = searchSpecialQuery.getArrRaceId(0);
            //String[] raceId = (String[]) vparam.get(24);
            if (raceId!=null && !( (raceId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<raceId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RACE]+
                                " = "+ raceId[i] + " OR ";
                    if (i==(raceId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : LANGUAGE = 9
            if (languageId!=null && !(languageId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<languageId.length; i++) {
                    whereClause = whereClause + " EMPLANG."+PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE_ID]+
                                " = "+ languageId[i] + " OR ";
                    if (i==(languageId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : SEX = 10
            int sex = searchSpecialQuery.getiSex();
                    //Integer.parseInt(vparam.get(10).toString());
            if(sex < 2) {
                whereClause += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SEX]+
                    " = "+ sex;
            }

            // parameter : RESIGNED = 11
            int resigned = searchSpecialQuery.getiResigned();
                    //Integer.parseInt(vparam.get(11).toString());
 java.util.Date newD = new java.util.Date();
            if(resigned < 2) {
                if (resigned == 0){
                    whereClause += " AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+
                    " = "+ resigned+" OR EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+ ">=\""+Formater.formatDate(newD, "yyyy-MM-dd")+"\")"; 
                } else {
                    whereClause += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+
                    " = "+ resigned;
                }
                    
            }
            
            //--- COMMENCING_DATE ----------------------------------------------
            // parameter : COMMENCING_DATE = 12, 13, 14
            int radiocommdate = searchSpecialQuery.getRadiocommdate();//Integer.parseInt(vparam.get(12).toString());
            if (radiocommdate > 0) {
//                java.util.Date commdatefrom;
//                java.util.Date commdateto;
                java.util.Date commdatefrom=null;
                java.util.Date commdateto = null;
                if(searchSpecialQuery.getCommdatefrom(0)!=null){//if (vparam.get(13) != null) {
                    commdatefrom = searchSpecialQuery.getCommdatefrom(0);//(java.util.Date) vparam.get(13);
                }
                if (searchSpecialQuery.getCommdateto(0) != null) {//if (vparam.get(14) != null) {
                    commdateto = searchSpecialQuery.getCommdateto(0);//(java.util.Date) vparam.get(14);
                }
                if((commdatefrom != null) && (commdateto != null)){
                    whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+ " BETWEEN '"+
                                  Formater.formatDate((java.util.Date) commdatefrom, "yyyy-MM-dd")+ "' AND '"+
                                  Formater.formatDate((java.util.Date) commdateto, "yyyy-MM-dd")+ "') ";
                }else if((commdatefrom != null) && (commdateto == null)){
                        whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+ " > '"+
                                  Formater.formatDate((java.util.Date) commdatefrom, "yyyy-MM-dd")+ "')";
                }else if((commdatefrom == null) && (commdateto != null)){
                         whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+ " < '"+
                                  Formater.formatDate((java.util.Date) commdateto, "yyyy-MM-dd")+ "')";
                }
            }
            
            
            //--- END_CONTRACT ----------------------------------------------
            // parameter : END_CONTRACT = 12, 13, 14
            int radioendcontract = searchSpecialQuery.getRadioendcontract();//Integer.parseInt(vparam.get(12).toString());
            if (radioendcontract > 0) {
                java.util.Date endcontractfrom=null;
                java.util.Date endcontractto = null;
                if(searchSpecialQuery.getEndcontractfrom(0)!=null){//if (vparam.get(13) != null) {
                    endcontractfrom = searchSpecialQuery.getEndcontractfrom(0);//(java.util.Date) vparam.get(13);
                }
                if (searchSpecialQuery.getEndcontractto(0) != null) {//if (vparam.get(14) != null) {
                    endcontractto = searchSpecialQuery.getEndcontractto(0);//(java.util.Date) vparam.get(14);
                }
                if((endcontractfrom != null) && (endcontractto != null)){
                    whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+ " BETWEEN '"+
                                  Formater.formatDate((java.util.Date) endcontractfrom, "yyyy-MM-dd")+ "' AND '"+
                                  Formater.formatDate((java.util.Date) endcontractto, "yyyy-MM-dd")+ "') ";
                }else if((endcontractfrom != null) && (endcontractto == null)){
                        whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+ " > '"+
                                  Formater.formatDate((java.util.Date) endcontractfrom, "yyyy-MM-dd")+ "')";
                }else if((endcontractfrom == null) && (endcontractto != null)){
                         whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+ " < '"+
                                  Formater.formatDate((java.util.Date) endcontractto, "yyyy-MM-dd")+ "')";
                }
            }
            

            //--- WORK ---------------------------------------------------------
            // parameter : WORKYEARFROM = 15
            int workyearfrom = searchSpecialQuery.getWorkyearfrom(0)!=null && searchSpecialQuery.getWorkyearfrom(0).length()>0? Integer.parseInt(searchSpecialQuery.getWorkyearfrom(0)):0;
            

            // parameter : WORKMONTHFROM = 16
            int workmonthfrom = searchSpecialQuery.getWorkmonthfrom(0)!=null && searchSpecialQuery.getWorkmonthfrom(0).length()>0?Integer.parseInt(searchSpecialQuery.getWorkmonthfrom(0)): 0;
//            try { workmonthfrom = Integer.parseInt(vparam.get(16).toString()); }
//            catch (Exception e) {}
            
            long workfrom = Math.round(((12d * workyearfrom) + workmonthfrom) * DAYS_IN_A_MONTH);
            
            // parameter : WORKYEARTO = 17
            int workyearto = searchSpecialQuery.getWorkyearto(0)!=null&& searchSpecialQuery.getWorkyearto(0).length()>0?Integer.parseInt(searchSpecialQuery.getWorkyearto(0)):  0;
            /*try { workyearto= Integer.parseInt(vparam.get(17).toString()); }
            catch (Exception e) {}*/

            // parameter : WORKMONTHTO = 18
            int workmonthto = searchSpecialQuery.getWorkmonthto(0)!=null&& searchSpecialQuery.getWorkmonthto(0).length()>0? Integer.parseInt(searchSpecialQuery.getWorkmonthto(0)):0;
//            try { workmonthto = Integer.parseInt(vparam.get(18).toString()); }
//            catch (Exception e) {}

            long workto = Math.round(((12d * workyearto) + workmonthto) * DAYS_IN_A_MONTH);
            
            if ((workfrom > 0) && (workto > 0) && (workfrom <= workto)) {
                whereClause += " AND ((TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") >= "+ workfrom + " ) AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") <= "+ workto + " ))";
            }
            else
            if (workfrom > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") >= "+ workfrom + ") ";
            }
            else
            if (workto > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") <= "+ workto + ") ";
            }

            //--- AGE ----------------------------------------------------------
            // parameter : AGEYEARFROM = 19
            int ageyearfrom = searchSpecialQuery.getAgeyearfrom(0)!=null&& searchSpecialQuery.getAgeyearfrom(0).length()>0?Integer.parseInt(searchSpecialQuery.getAgeyearfrom(0)):  0;
//            try { ageyearfrom = Integer.parseInt(vparam.get(19).toString()); }
//            catch (Exception e) {}

            // parameter : AGEMONTHFROM = 20
            int agemonthfrom = searchSpecialQuery.getAgemonthfrom(0)!=null&& searchSpecialQuery.getAgemonthfrom(0).length()>0?Integer.parseInt(searchSpecialQuery.getAgemonthfrom(0)):  0;
//            try { agemonthfrom = Integer.parseInt(vparam.get(20).toString()); }
//            catch (Exception e) {}
            
            long agefrom = Math.round(((12d * ageyearfrom) + agemonthfrom) * DAYS_IN_A_MONTH);
            
            // parameter : AGEYEARTO = 21
            int ageyearto = searchSpecialQuery.getAgeyearto(0)!=null&& searchSpecialQuery.getAgeyearto(0).length()>0? Integer.parseInt(searchSpecialQuery.getAgeyearto(0)):  0;
//            try { ageyearto= Integer.parseInt(vparam.get(21).toString()); }
//            catch (Exception e) {}

            // parameter : AGEMONTHTO = 22
            int agemonthto = searchSpecialQuery.getAgemonthto(0)!=null&& searchSpecialQuery.getAgemonthto(0).length()>0?Integer.parseInt(searchSpecialQuery.getAgemonthto(0)):  0;
//            try { agemonthto = Integer.parseInt(vparam.get(22).toString()); }
//            catch (Exception e) {}

            long ageto = Math.round(((12d * ageyearto) + agemonthto) * DAYS_IN_A_MONTH);
            
            if ((agefrom > 0) && (ageto > 0) && (agefrom <= ageto)) {
                whereClause += " AND ((TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") >= "+ agefrom + " ) AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") <= "+ ageto + " ))";
            }
            else
            if (agefrom > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") >= "+ agefrom + ") ";
            }
            else
            if (ageto > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") <= "+ ageto + ") ";
            }
            
            // Update by Hendra Putu | 2015-01-31
            if (searchSpecialQuery.getResignedReasonId()!=0 && searchSpecialQuery.getResignedReasonId() > 0) {
                whereClause = whereClause + " AND RSGNSON."+PstResignedReason.fieldNames[PstResignedReason.FLD_RESIGNED_REASON_ID]+" = " + searchSpecialQuery.getResignedReasonId()+ " ";
            }
            

            //--- terakhir...
            if (whereClause.length() > 0) {
                sql = sql + whereClause;            
            }
            
                       //update by satrya 2013-11-13
            //   sql = sql + " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            if(searchSpecialQuery.getDtCarrierWorkStart()!= null && searchSpecialQuery.getDtCarrierWorkEnd() != null || searchSpecialQuery.getCarrierCategoryEmp()!=0){
                sql = sql + " GROUP BY CRR."+PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID];
            }else{
                sql = sql + " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            }
            // parameter : SORTBY = 23
           if(searchSpecialQuery.getSort(0)!=null){
               //if(vparam.get(23)!=null){
            int sortby = Integer.parseInt(searchSpecialQuery.getSort(0));
            switch(sortby){
                case SrcSpecialEmployee.ORDER_EMPLOYEE_NAME :
                    sql = sql + " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] ;
                    break;
                case SrcSpecialEmployee.ORDER_DEPARTMENT:
                    sql = sql + " ORDER BY DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] ;
                    break;
                case SrcSpecialEmployee.ORDER_EMPLOYEE_NUM:
                    sql = sql + " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] ;
                    break;
                case SrcSpecialEmployee.ORDER_COMM_DATE:
                    sql = sql + " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] ;
                    break;
                default:
                    sql = sql + "";
            }
           }
           
           if(limit == 0 && recordToget == 0)
		sql = sql + "";
            else
		sql = sql + " LIMIT " + limit + ","+ recordToget ;
         
            //System.out.println(" SQL searchEmployee : \r" + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
          
            while(rs.next()) {
                
                SessTmpSpecialEmployee sessTmpSpecialEmployee = new SessTmpSpecialEmployee();
//                Employee employee = new Employee();
//                Department department = new Department();
//                Position position = new Position();
//                Section section = new Section();
//                EmpCategory empcategory = new EmpCategory();
//                Level level = new Level();
//                Religion religion = new Religion();
//                Marital marital = new Marital();
//                Race race = new Race();
                
                //Education education = new Education();
                //Language language = new Language();
                
                sessTmpSpecialEmployee.setEmployeeId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                sessTmpSpecialEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                sessTmpSpecialEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                sessTmpSpecialEmployee.setPhoneEmployee(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE]));
                sessTmpSpecialEmployee.setIndentCardNr(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_INDENT_CARD_NR]));
                sessTmpSpecialEmployee.setPostalCodeEmployee(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE]));
                sessTmpSpecialEmployee.setSexEmployee(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_SEX]));
                sessTmpSpecialEmployee.setBirthPlaceEmployee(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_PLACE]));
                sessTmpSpecialEmployee.setBirthDateEmployee(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]));
                sessTmpSpecialEmployee.setPostalCodeEmployee(rs.getInt(PstEmployee.fieldNames[PstEmployee.FLD_POSTAL_CODE]));
                sessTmpSpecialEmployee.setPhoneEmg(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PHONE_EMERGENCY]));
                sessTmpSpecialEmployee.setNameEmg(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NAME_EMG]));
                
                sessTmpSpecialEmployee.setFather(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FATHER]));
                sessTmpSpecialEmployee.setMother(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_MOTHER]));
                sessTmpSpecialEmployee.setParentsAddress(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PARENTS_ADDRESS]));
                //employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                //employee.setPositionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));
                sessTmpSpecialEmployee.setCommercingDateEmployee(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                //employee.setReligionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]));
                //employee.setMaritalId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]));
                sessTmpSpecialEmployee.setBloodTypeEmployee(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]));
                //employee.setLockerId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]));
                sessTmpSpecialEmployee.setAstekNumEMployee(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_NUM]));
                sessTmpSpecialEmployee.setAsktekDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_ASTEK_DATE]));
                sessTmpSpecialEmployee.setBarcodeNumber(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BARCODE_NUMBER]));
                //employee.setEmpCategoryId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]));
                //employee.setSectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]));
                //employee.setLevelId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]));
                //update by satrya 2013-08-01
                sessTmpSpecialEmployee.setDepartement(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                sessTmpSpecialEmployee.setPosition(rs.getString("POST."+PstPosition.fieldNames[PstPosition.FLD_POSITION]));
                sessTmpSpecialEmployee.setSection(rs.getString(PstSection.fieldNames[PstSection.FLD_SECTION]));
                sessTmpSpecialEmployee.setEmpCategory(rs.getString(PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]));
                sessTmpSpecialEmployee.setLevel(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]));
                sessTmpSpecialEmployee.setReligion(rs.getString(PstReligion.fieldNames[PstReligion.FLD_RELIGION]));
                sessTmpSpecialEmployee.setMaritalStatus(rs.getString("MAR."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_STATUS]));
		sessTmpSpecialEmployee.setMaritalNumberOfChildren(rs.getInt("MAR."+PstMarital.fieldNames[PstMarital.FLD_NUM_OF_CHILDREN]));
		sessTmpSpecialEmployee.setTaxMarital(rs.getString("MARTAX."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_STATUS]));
                sessTmpSpecialEmployee.setTaxNumberChildren(rs.getInt("MARTAX."+PstMarital.fieldNames[PstMarital.FLD_NUM_OF_CHILDREN]));
                sessTmpSpecialEmployee.setRaceName(rs.getString(PstRace.fieldNames[PstRace.FLD_RACE_NAME]));
                sessTmpSpecialEmployee.setCompanyName(rs.getString(PstPayGeneral.fieldNames[PstPayGeneral.FLD_COMPANY_NAME]));
                sessTmpSpecialEmployee.setDivision(rs.getString(PstDivision.fieldNames[PstDivision.FLD_DIVISION]));
                sessTmpSpecialEmployee.setResignedReason(rs.getString(PstResignedReason.fieldNames[PstResignedReason.FLD_RESIGNED_REASON]));
                sessTmpSpecialEmployee.setLockerNumber(rs.getString(PstLocker.fieldNames[PstLocker.FLD_LOCKER_NUMBER]));
                sessTmpSpecialEmployee.setLockerLocation(rs.getString(PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION]));
                
                sessTmpSpecialEmployee.setResignedDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]));
                sessTmpSpecialEmployee.setResignedDesc(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DESC]));
                sessTmpSpecialEmployee.setHandphone(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_HANDPHONE]));        
                sessTmpSpecialEmployee.setCurrier(rs.getString("EMP."+PstEmployee.fieldNames[PstEmployee.FLD_CURIER])); 
 
                
                sessTmpSpecialEmployee.setCountry(rs.getString("NEGARA."+PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA]));
                sessTmpSpecialEmployee.setProvinsi(rs.getString("PRO."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI]));
                sessTmpSpecialEmployee.setKabupaten(rs.getString("KAB."+PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN]));
                sessTmpSpecialEmployee.setKecamatan(rs.getString("KEC."+PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN]));
                sessTmpSpecialEmployee.setDistrict(rs.getString("DIS."+PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT]));
                sessTmpSpecialEmployee.setDistrictPermanent(rs.getString("DISPERMANENT."+PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT]));
                sessTmpSpecialEmployee.setCountryPermanent(rs.getString("NEGARAPERMANENT."+PstNegara.fieldNames[PstNegara.FLD_NM_NEGARA]));
                sessTmpSpecialEmployee.setProvinsi(rs.getString("PROPERMANENT."+PstProvinsi.fieldNames[PstProvinsi.FLD_NM_PROVINSI]));
                sessTmpSpecialEmployee.setKabupaten(rs.getString("KABPERMANENT."+PstKabupaten.fieldNames[PstKabupaten.FLD_NM_KABUPATEN]));
                sessTmpSpecialEmployee.setKecamatan(rs.getString("KECPERMANENT."+PstKecamatan.fieldNames[PstKecamatan.FLD_NM_KECAMATAN]));
                
                sessTmpSpecialEmployee.setAddressEmployee(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS])+","+sessTmpSpecialEmployee.getCountry()+","+sessTmpSpecialEmployee.getProvinsi()+","+sessTmpSpecialEmployee.getKabupaten()+","+sessTmpSpecialEmployee.getKecamatan());
                sessTmpSpecialEmployee.setAddressPermanent(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT])+","+sessTmpSpecialEmployee.getCountryPermanent()+","+sessTmpSpecialEmployee.getProvinsiPermanent()+","+sessTmpSpecialEmployee.getKabupatenPermanent()+","+sessTmpSpecialEmployee.getKecamatanPermanent());
                sessTmpSpecialEmployee.setLocation(rs.getString(PstLocation.fieldNames[PstLocation.FLD_NAME]));
                sessTmpSpecialEmployee.setGrade(rs.getString(PstGradeLevel.fieldNames[PstGradeLevel.FLD_GRADE_CODE]));
                sessTmpSpecialEmployee.setEndContractEmployee(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]));
                sessTmpSpecialEmployee.setIdCardType(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ID_CARD_TYPE]));
                sessTmpSpecialEmployee.setNpwp(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NPWP]));
                sessTmpSpecialEmployee.setBpjsNo(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_BPJS_NO]));
                sessTmpSpecialEmployee.setBpjsDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_BPJS_DATE]));
                sessTmpSpecialEmployee.setShio(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_SHIO]));
                sessTmpSpecialEmployee.setElemen(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_ELEMEN]));
                sessTmpSpecialEmployee.setIq(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_IQ]));
                sessTmpSpecialEmployee.setEq(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EQ]));
                sessTmpSpecialEmployee.setProbationEndDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_PROBATION_END_DATE]));
                sessTmpSpecialEmployee.setStatusPensionProgram(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_STATUS_PENSIUN_PROGRAM]));
                sessTmpSpecialEmployee.setStartDatePensiun(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_START_DATE_PENSIUN]));
                sessTmpSpecialEmployee.setPresenceCheckParameter(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_PRESENCE_CHECK_PARAMETER]));
                sessTmpSpecialEmployee.setMedicalInfo(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_MEDICAL_INFO]));
                sessTmpSpecialEmployee.setDanaPendidikan(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_DANA_PENDIDIKAN]));
				sessTmpSpecialEmployee.setNoKK(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_NO_KK]));
                
       
                sessTmpSpecialEmployee.setWaCompany(rs.getString("COMP1."+PstCompany.fieldNames[PstCompany.FLD_COMPANY]));
                sessTmpSpecialEmployee.setWaDivision(rs.getString("DIVX1."+PstDivision.fieldNames[PstDivision.FLD_DIVISION]));
                sessTmpSpecialEmployee.setWaDepartement(rs.getString("DEPT1."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                sessTmpSpecialEmployee.setWaPosition(rs.getString("POST1."+PstPosition.fieldNames[PstPosition.FLD_POSITION]));
                sessTmpSpecialEmployee.setWaSection(rs.getString("SEC1."+PstSection.fieldNames[PstSection.FLD_SECTION]));
                
                result.add(sessTmpSpecialEmployee);
                        
		/*department.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                vect.add(department);

                position.setPosition(rs.getString(PstPosition.fieldNames[PstPosition.FLD_POSITION]));
                vect.add(position);

                section.setSection(rs.getString(PstSection.fieldNames[PstSection.FLD_SECTION]));
                vect.add(section);

                empcategory.setEmpCategory(rs.getString(PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]));
                vect.add(empcategory);

                level.setLevel(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]));
                vect.add(level);

                religion.setReligion(rs.getString(PstReligion.fieldNames[PstReligion.FLD_RELIGION]));
                vect.add(religion);        

                marital.setMaritalStatus(rs.getString(PstMarital.fieldNames[PstMarital.FLD_MARITAL_STATUS]));
		marital.setNumOfChildren(rs.getInt(PstMarital.fieldNames[PstMarital.FLD_NUM_OF_CHILDREN]));
		marital.setMaritalCode(rs.getString(PstMarital.fieldNames[PstMarital.FLD_MARITAL_CODE]));
                vect.add(marital);*/
//              if(educationId!=null){
//                if (!(educationId[0].equals("0"))) {
//                    education.setEducation(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION]));
//                    education.setEducationDesc(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION_DESC]));
//                }
//              }
               // vect.add(education);

//               if (!(languageId[0].equals("0"))) {
//                    language.setLanguage(rs.getString(PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE]));
//                }
//                vect.add(language);
                
                /*race.setRaceName(rs.getString(PstRace.fieldNames[PstRace.FLD_RACE_NAME]));
                vect.add(race);*/
                
                //result.add(vect);
               // System.out.println(" Employee " +sessTmpSpecialEmployee.getEmployeeNum());
            }
            return result;
        } 
        catch (Exception e) {
            System.out.println("\t Exception on  searchEmployee : " + e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
/**
 * create by satrya 2013-08-05
 * @param searchSpecialQuery
 * @param limit
 * @param recordToget
 * @return 
 */    
public static int countSearchSpecialEmployee(SearchSpecialQuery searchSpecialQuery,int limit,int recordToget) {
       DBResultSet dbrs = null;
       int count = 0;
        try {
            String sql = " SELECT COUNT(EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+")";
            // parameter : EDUCATION = 4
            String[] educationId = searchSpecialQuery.getArrEducationId(0);
             String[] languageId = searchSpecialQuery.getArrLanguageId(0);

            //update by satrya 2012-12-24
            sql += " FROM "+ PstEmployee.TBL_HR_EMPLOYEE +" AS EMP "   
                + " LEFT JOIN  " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +")"
                + " LEFT JOIN  " + PstPosition.TBL_HR_POSITION   + " AS POST  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POST."+PstPosition.fieldNames[PstPosition.FLD_POSITION_ID]+")"
                + " LEFT JOIN  " + PstEmpCategory.TBL_HR_EMP_CATEGORY +"  AS EMPCAT ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = EMPCAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]+")" 
                + " LEFT JOIN  " + PstSection.TBL_HR_SECTION + " AS SEC  ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID]+")" 
                + " LEFT JOIN  " + PstLevel.TBL_HR_LEVEL + " AS LEV   ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]+ ")" 
                + " LEFT JOIN  " + PstMarital.TBL_HR_MARITAL + " AS MAR  ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID] + "  = MAR." + PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID]+ ")" 
                + " LEFT JOIN  " + PstReligion.TBL_HR_RELIGION + " AS REL  ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]+ " = REL." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID]+ ")" 
                + " LEFT JOIN  " + PstRace.TBL_RACE + " AS RAC ON (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RACE] + " = RAC."+ PstRace.fieldNames[PstRace.FLD_RACE_ID]+ ")"
                //update by satrrya 2013-07-30
                + " LEFT JOIN  " + PstDivision.TBL_HR_DIVISION + " AS DIVX ON (DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+"=EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+" ) "
                + " LEFT JOIN  " + PstPayGeneral.TBL_PAY_GENERAL + " AS COMP ON (COMP."+PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID]+"=DIVX."+PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+")"
                + " LEFT JOIN  " + PstResignedReason.TBL_HR_RESIGNED_REASON + " AS RSGNSON ON RSGNSON."+PstResignedReason.fieldNames[PstResignedReason.FLD_RESIGNED_REASON_ID]+"=EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_REASON_ID]
                + " LEFT JOIN  " + PstLocker.TBL_HR_LOCKER + " AS LOC ON LOC."+PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]+"=EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LOCKER_ID]
                + " LEFT JOIN  " + PstLockerLocation.TBL_HR_LOCKER_LOCATION + " AS LOCLOC ON LOCLOC."+PstLockerLocation.fieldNames[PstLockerLocation.FLD_LOCATION_ID]+"=LOC."+PstLocker.fieldNames[PstLocker.FLD_LOCKER_ID]
                + " LEFT JOIN  " + PstMarital.TBL_HR_MARITAL + " AS MARTAX  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID] + "  = MARTAX."+PstMarital.fieldNames[PstMarital.FLD_MARITAL_ID]+")"
                //priska 2014-10-30
                + " LEFT JOIN  " + PstLocation.TBL_P2_LOCATION + " AS LOCA  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID] + "  = LOCA."+PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID]+")"
                + " LEFT JOIN  " + PstGradeLevel.TBL_HR_GRADE_LEVEL + " AS GRADE  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID] + "  = GRADE."+PstGradeLevel.fieldNames[PstGradeLevel.FLD_GRADE_LEVEL_ID]+")"; 
                
            //update by satrya 2013-11-13
                if(searchSpecialQuery.getDtCarrierWorkStart()!= null && searchSpecialQuery.getDtCarrierWorkEnd() != null || searchSpecialQuery.getCarrierCategoryEmp()!=0){
                sql = sql + " INNER JOIN " + PstCareerPath.TBL_HR_WORK_HISTORY_NOW + " AS CRR  ON (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "  = CRR."+PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]+")";
                }
                
                
            // parameter : EDUCATION = 4
           
if(educationId!=null){
 if (!(educationId[0].equals("0"))) {
     sql +=   " LEFT JOIN "+PstEmpEducation.TBL_HR_EMP_EDUCATION + " AS EMPEDU ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+"=EMPEDU."+PstEmpEducation.fieldNames[PstEmpEducation.FLD_EMPLOYEE_ID];//", "+PstEducation.TBL_HR_EDUCATION + " EDU "; 
 }
} 
            // parameter : LANGUAGE = 8
if(languageId!=null){
 if (!(languageId[0].equals("0"))) {
     sql += " LEFT JOIN "+PstEmpLanguage.TBL_HR_EMP_LANGUAGE + " AS EMPLANG ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+"=EMPLANG."+PstEmpLanguage.fieldNames[PstEmpLanguage.FLD_EMPLOYEE_ID] ;//  ", "+PstLanguage.TBL_HR_LANGUAGE + " LANG ";
     //sql +=   ", "+PstEmpLanguage.TBL_HR_EMP_LANGUAGE + " EMPLANG ";
 }
}
                 sql +=   " WHERE ";
                         //" (1 = 1) " +
                         //" AND (1=1)" ;
         
            
             String whereClause = "(1=1)";
           if (searchSpecialQuery.getBirthMonth() > 0) {
                    whereClause = whereClause + " AND MONTH(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                            + ") = '" + (searchSpecialQuery.getBirthMonth()) + "'  ";
                }
        
          if ((searchSpecialQuery.getFullNameEmp() != null) && (searchSpecialQuery.getFullNameEmp().length() > 0)) {
                Vector vectName = logicParser(searchSpecialQuery.getFullNameEmp());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }

            if ((searchSpecialQuery.getAddrsEmployee() != null) && (searchSpecialQuery.getAddrsEmployee().length() > 0)) {
                Vector vectAddr = logicParser(searchSpecialQuery.getAddrsEmployee());
                if (vectAddr != null && vectAddr.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectAddr.size(); i++) {
                        String str = (String) vectAddr.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }

            if ((searchSpecialQuery.getEmpnumber() != null) && (searchSpecialQuery.getEmpnumber().length() > 0)) {
                Vector vectNum = logicParser(searchSpecialQuery.getEmpnumber());
                if (vectNum != null && vectNum.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }
            
            
            if ((searchSpecialQuery.getAddrsEmployee() != null) && (searchSpecialQuery.getAddrsEmployee().length() > 0)) {
                Vector vectName = logicParser(searchSpecialQuery.getAddrsEmployee());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }
            
            if ((searchSpecialQuery.getAddressPermanent() != null) && (searchSpecialQuery.getAddressPermanent().length() > 0)) {
                Vector vectName = logicParser(searchSpecialQuery.getAddressPermanent());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " AND (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_ADDRESS_PERMANENT]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") ";
                }
            }
            
            //update by satrya 2013-08-15
             //
             String[] CountyId = searchSpecialQuery.getArrCountryId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (CountyId!=null && !(CountyId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<CountyId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_COUNTRY_ID]+
                                " = "+ CountyId[i] + " OR ";
                    if (i==(CountyId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] provinsiId = searchSpecialQuery.getArrProvinsiId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (provinsiId!=null && !(provinsiId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<provinsiId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PROVINCE_ID]+
                                " = "+ provinsiId[i] + " OR ";
                    if (i==(provinsiId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            String[] kabupatenId = searchSpecialQuery.getArrKabupatenId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kabupatenId!=null && !(kabupatenId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kabupatenId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_REGENCY_ID]+
                                " = "+ kabupatenId[i] + " OR ";
                    if (i==(kabupatenId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] kecamatanId = searchSpecialQuery.getArrKecamatanId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kecamatanId!=null && !(kecamatanId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kecamatanId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_SUBREGENCY_ID]+
                                " = "+ kecamatanId[i] + " OR ";
                    if (i==(kecamatanId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            
            //
             String[] CountyIdPermanent = searchSpecialQuery.getArrCountryIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (CountyIdPermanent!=null && !(CountyIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<CountyIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_COUNTRY_ID]+
                                " = "+ CountyIdPermanent[i] + " OR ";
                    if (i==(CountyIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] provinsiIdPermanent = searchSpecialQuery.getArrProvinsiIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (provinsiIdPermanent!=null && !(provinsiIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<provinsiIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_PROVINCE_ID]+
                                " = "+ provinsiIdPermanent[i] + " OR ";
                    if (i==(provinsiIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            String[] kabupatenIdPermanent = searchSpecialQuery.getArrKabupatenIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kabupatenIdPermanent!=null && !(kabupatenIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kabupatenIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_REGENCY_ID]+
                                " = "+ kabupatenIdPermanent[i] + " OR ";
                    if (i==(kabupatenIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] kecamatanIdPermanent = searchSpecialQuery.getArrKecamatanIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (kecamatanIdPermanent!=null && !(kecamatanIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<kecamatanIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_SUBREGENCY_ID]+
                                " = "+ kecamatanIdPermanent[i] + " OR ";
                    if (i==(kecamatanIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] districtId = searchSpecialQuery.getArrDistrictId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (districtId!=null && !(districtId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<districtId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_DISTRICT_ID]+
                                " = "+ districtId[i] + " OR ";
                    if (i==(districtId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            String[] districtIdPermanent = searchSpecialQuery.getArrDistrictIdPermanent(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (districtIdPermanent!=null && !(districtIdPermanent[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<districtIdPermanent.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_ADDR_PMNT_DISTRICT_ID]+
                                " = "+ districtIdPermanent[i] + " OR ";
                    if (i==(districtIdPermanent.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            //
            
            if (searchSpecialQuery.getArrDivision(0)!=null) {
                String[] divisionId = searchSpecialQuery.getArrDivision(0);
                    if (! (divisionId!=null && (divisionId[0].equals("0")))) {
                    whereClause += " AND (";
                    for (int i=0; i<divisionId.length; i++) {
                        whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+
                                    " = "+ divisionId[i] + " OR ";
                        if (i==(divisionId.length-1)) {
                            whereClause = whereClause.substring(0, whereClause.length()-4);
                        }
                    }
                    whereClause += " )";
                }
               
            }
            
            if ((searchSpecialQuery.getBirthYearFrom() != 0) || (searchSpecialQuery.getBirthYearTo() !=0)) {
                    if(searchSpecialQuery.getBirthYearFrom() != 0 && searchSpecialQuery.getBirthYearTo()==0){
                         whereClause = whereClause + " AND YEAR(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                            + ") = '" + (searchSpecialQuery.getBirthMonth()) + "'  ";
                         
                    }else if(searchSpecialQuery.getBirthYearTo() != 0 && searchSpecialQuery.getBirthYearFrom() == 0){
                        whereClause = whereClause + " AND YEAR(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
                            + ") = '" + (searchSpecialQuery.getBirthYearTo()) + "'  ";
                    }else{
                        whereClause = whereClause + " AND YEAR(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + ") BETWEEN '"
                        + searchSpecialQuery.getBirthYearFrom() + "' AND '"
                        + searchSpecialQuery.getBirthYearTo() + "'  ";
                        
                    }
            }
            
             if ((searchSpecialQuery.getDtBirthFrom() != null) || (searchSpecialQuery.getDtBirthTo() != null)) {
                    if(searchSpecialQuery.getDtBirthFrom() != null && searchSpecialQuery.getDtBirthTo() == null){
                        whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthFrom(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthFrom(), "yyyy-MM-dd") + "'  ";
                    }else if(searchSpecialQuery.getDtBirthTo() != null && searchSpecialQuery.getDtBirthFrom() == null){
                        whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthTo(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthTo(), "yyyy-MM-dd") + "'  ";
                    }else{
                        whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthFrom(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getDtBirthTo(), "yyyy-MM-dd") + "'  ";
                    }
            }
             
             //update by satrya 2013-11-13
              if ((searchSpecialQuery.getDtCarrierWorkStart()!= null && searchSpecialQuery.getDtCarrierWorkEnd() != null)) {
                    
                        whereClause = whereClause + " AND \""+Formater.formatDate(searchSpecialQuery.getDtCarrierWorkEnd(), "yyyy-MM-dd")+"\" > CRR." 
                        + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]+ " AND CRR."
                        + PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_TO] + " > \""+Formater.formatDate(searchSpecialQuery.getDtCarrierWorkStart(), "yyyy-MM-dd")+"\"";
                    
            }
            if(searchSpecialQuery.getCarrierCategoryEmp()!=0){
                whereClause = whereClause + " AND CRR."+PstCareerPath.fieldNames[PstCareerPath.FLD_EMP_CATEGORY_ID]+"="+searchSpecialQuery.getCarrierCategoryEmp();
            }
            
            
             
//            if (searchSpecialQuery.getMonthBirth()!=0) {
//                 whereClause = whereClause + " MONTH(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]
//                            + ") = '" + (searchSpecialQuery.getMonthBirth()) + "' AND ";
//               
//            }
            
            
//            if (!searchSpecialQuery.isBirthdayChecked()) {
//                System.out.println("Bithday checked : " + searchSpecialQuery.isBirthdayChecked());
//               // System.out.println("Bithday : " + searchSpecialQuery.getBirthday());
//                System.out.println("Bithmon : " + searchSpecialQuery.getBirthMonth());

               
//            }
            
            if (searchSpecialQuery.getSalaryLevel()!=null && searchSpecialQuery.getSalaryLevel().length() > 0) {
                whereClause = whereClause + " AND LEV." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]
                        + " = '" + searchSpecialQuery.getSalaryLevel() + "'  ";
            }
            
            if (searchSpecialQuery.getArrEmpCategory(0)!=null) {
                String[] empCategory = searchSpecialQuery.getArrEmpCategory(0);
                if (! (empCategory!=null && (empCategory[0].equals("0")))) {
                    whereClause += " AND (";
                    for (int i=0; i<empCategory.length; i++) {
                        whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]+
                                    " = "+ empCategory[i] + " OR ";
                        if (i==(empCategory.length-1)) {
                            whereClause = whereClause.substring(0, whereClause.length()-4);
                        }
                    }
                    whereClause += " )";
                }
                
            }

            
            //update by satrya 2013-10-17
            boolean noresign=true;
            if(searchSpecialQuery.getDtPeriodEnd()!=null){
                noresign=false;
                whereClause = whereClause + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]
                /*+ " BETWEEN \""+Formater.formatDate(searchSpecialQuery.getDtPeriodStart(), "yyyy-MM-dd") + "\""
                +" AND \""+Formater.formatDate(searchSpecialQuery.getDtPeriodEnd(), "yyyy-MM-dd")+"\"  "*/
                + " <= \" "+Formater.formatDate(searchSpecialQuery.getDtPeriodEnd(), "yyyy-MM-dd")+"\" "
                + " AND ((EMP."
                + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="+PstEmployee.NO_RESIGN+") OR  "
                + " (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+ "="+PstEmployee.NO_RESIGN 
                + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+ ">=\""+Formater.formatDate(searchSpecialQuery.getDtPeriodEnd(), "yyyy-MM-dd")+"\"))";
            }
            if ( noresign && (searchSpecialQuery.getiResigned()==1) && (searchSpecialQuery.getStartResign() != null) && (searchSpecialQuery.getEndResign() != null)) {
                whereClause = whereClause + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] + " BETWEEN '"
                        + Formater.formatDate(searchSpecialQuery.getStartResign(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(searchSpecialQuery.getEndResign(), "yyyy-MM-dd") + "'  ";
            }

            // parameter : DEPARTMENT = 0
          if(searchSpecialQuery.getArrDepartmentId(0)!=null){
              //if((String[]) vparam.get(1)!=null){
            String[] departmentId = searchSpecialQuery.getArrDepartmentId(0);
            //String[] departmentId = (String[]) vparam.get(1);
            if (departmentId!=null && ! ( (departmentId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<departmentId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+
                                " = "+ departmentId[i] + " OR ";
                    if (i==(departmentId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
          }
            // parameter : POSITION = 2
            String[] positionId = searchSpecialQuery.getArrPositionId(0);
            //String[] positionId = (String[]) vparam.get(2);
            if (positionId!=null && !( (positionId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<positionId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]+
                                " = "+ positionId[i] + " OR ";
                    if (i==(positionId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : LOCATION = 2
            String[] locationId = searchSpecialQuery.getArrLocationId(0);
            //String[] locationId = (String[]) vparam.get(2);
            if (locationId!=null && !( (locationId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<locationId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LOCATION_ID]+
                                " = "+ locationId[i] + " OR ";
                    if (i==(locationId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : GRADE = 2
            String[] gradeId = searchSpecialQuery.getArrGradeId(0);
            //String[] gradeId = (String[]) vparam.get(2);
            if (gradeId!=null && !( (gradeId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<gradeId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID]+
                                " = "+ gradeId[i] + " OR ";
                    if (i==(gradeId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            if (searchSpecialQuery.getArrPayrollGroupId(0)!=null) {
                String[] payrollGId = searchSpecialQuery.getArrPayrollGroupId(0);
                    if (! (payrollGId!=null && (payrollGId[0].equals("0")))) {
                    whereClause += " AND (";
                    for (int i=0; i<payrollGId.length; i++) {
                        whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+
                                    " = "+ payrollGId[i] + " OR ";
                        if (i==(payrollGId.length-1)) {
                            whereClause = whereClause.substring(0, whereClause.length()-4);
                        }
                    }
                    whereClause += " )";
                }
               
            }
            String[] companyId = searchSpecialQuery.getArrCompany(0);
            //String[] positionId = (String[]) vparam.get(2);
            if (companyId!=null && !( (companyId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<companyId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+
                                " = "+ companyId[i] + " OR ";
                    if (i==(companyId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : SECTION = 3
            String[] sectionId = searchSpecialQuery.getArrSectionId(0);
            //String[] sectionId = (String[]) vparam.get(3);
            if (sectionId!=null && !( (sectionId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<sectionId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+
                                " = "+ sectionId[i] + " OR ";
                    if (i==(sectionId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : LEVEL = 4
            String[] levelId = searchSpecialQuery.getArrLevelId(0);
            //String[] levelId = (String[]) vparam.get(4);
            if (levelId!=null && !( (levelId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<levelId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]+
                                " = "+ levelId[i] + " OR ";
                    if (i==(levelId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
            
            // parameter : EDUCATION = 5
            //String[] educationId = (String[]) vparam.get(4);
          if(educationId!=null){
            if (educationId!=null && !( (educationId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<educationId.length; i++) {
                    whereClause = whereClause + " EMPEDU."+PstEmpEducation.fieldNames[PstEmpEducation.FLD_EDUCATION_ID]+
                               " = '"+ educationId[i] + "' OR ";
                    if (i==(educationId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
          }
            // parameter : RELIGION = 6
            String[] religionId = searchSpecialQuery.getArrReligionId(0);
            //String[] religionId = (String[]) vparam.get(6);
           
            if (religionId!=null && !(religionId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<religionId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]+
                                " = "+ religionId[i] + " OR ";
                    if (i==(religionId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : MARITAL = 7
            String[] maritalId = searchSpecialQuery.getArrMaritalId(0);
            //String[] maritalId = (String[]) vparam.get(7);
            if (maritalId!=null && !(maritalId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<maritalId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]+
                                " = "+ maritalId[i] + " OR ";
                    if (i==(maritalId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : BLOOD = 8
          if(searchSpecialQuery.getArrBlood(0)!=null){
              //if((String[]) vparam.get(8)!=null){
            String[] bloodId = searchSpecialQuery.getArrBlood(0);
            if (bloodId!=null && !( (bloodId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<bloodId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_BLOOD_TYPE]+
                                " = '"+ bloodId[i] + "' OR ";
                    if (i==(bloodId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }
          } 
             // parameter : RACE = 24
            String[] raceId = searchSpecialQuery.getArrRaceId(0);
            //String[] raceId = (String[]) vparam.get(24);
            if (raceId!=null && !( (raceId[0].equals("0")))) {
                whereClause += " AND (";
                for (int i=0; i<raceId.length; i++) {
                    whereClause = whereClause + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RACE]+
                                " = "+ raceId[i] + " OR ";
                    if (i==(raceId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : LANGUAGE = 9
            if (languageId!=null && !(languageId[0].equals("0"))) {
                whereClause += " AND (";
                for (int i=0; i<languageId.length; i++) {
                    whereClause = whereClause + " EMPLANG."+PstLanguage.fieldNames[PstLanguage.FLD_LANGUAGE_ID]+
                                " = "+ languageId[i] + " OR ";
                    if (i==(languageId.length-1)) {
                        whereClause = whereClause.substring(0, whereClause.length()-4);
                    }
                }
                whereClause += " )";
            }

            // parameter : SEX = 10
            int sex = searchSpecialQuery.getiSex();
                    //Integer.parseInt(vparam.get(10).toString());
            if(sex < 2) {
                whereClause += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SEX]+
                    " = "+ sex;
            }

            // parameter : RESIGNED = 11
            int resigned = searchSpecialQuery.getiResigned();
                        //Integer.parseInt(vparam.get(11).toString());
 java.util.Date newD = new java.util.Date();
            if(resigned < 2) {
                if (resigned == 0){
                    whereClause += " AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+
                    " = "+ resigned+" OR EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+ ">=\""+Formater.formatDate(newD, "yyyy-MM-dd")+"\")"; 
                } else {
                    whereClause += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+
                    " = "+ resigned;
                }
                    
            }
            
            //--- COMMENCING_DATE ----------------------------------------------
            // parameter : COMMENCING_DATE = 12, 13, 14
            int radiocommdate = searchSpecialQuery.getRadiocommdate();//Integer.parseInt(vparam.get(12).toString());
            if (radiocommdate > 0) {
//                java.util.Date commdatefrom;
//                java.util.Date commdateto;
                java.util.Date commdatefrom=null;
                java.util.Date commdateto = null;
                if(searchSpecialQuery.getCommdatefrom(0)!=null){//if (vparam.get(13) != null) {
                    commdatefrom = searchSpecialQuery.getCommdatefrom(0);//(java.util.Date) vparam.get(13);
                }
                if (searchSpecialQuery.getCommdateto(0) != null) {//if (vparam.get(14) != null) {
                    commdateto = searchSpecialQuery.getCommdateto(0);//(java.util.Date) vparam.get(14);
                }
                 if((commdatefrom != null) && (commdateto != null)){
                    whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+ " BETWEEN '"+
                                  Formater.formatDate((java.util.Date) commdatefrom, "yyyy-MM-dd")+ "' AND '"+
                                  Formater.formatDate((java.util.Date) commdateto, "yyyy-MM-dd")+ "') ";
                }else if((commdatefrom != null) && (commdateto == null)){
                        whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+ " > '"+
                                  Formater.formatDate((java.util.Date) commdatefrom, "yyyy-MM-dd")+ "')";
                }else if((commdatefrom == null) && (commdateto != null)){
                         whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+ " < '"+
                                  Formater.formatDate((java.util.Date) commdateto, "yyyy-MM-dd")+ "')";
                }
            }

            
            //--- END_CONTRACT ----------------------------------------------
            // parameter : END_CONTRACT = 12, 13, 14
            int radioendcontract = searchSpecialQuery.getRadioendcontract();//Integer.parseInt(vparam.get(12).toString());
            if (radioendcontract > 0) {
                java.util.Date endcontractfrom=null;
                java.util.Date endcontractto = null;
                if(searchSpecialQuery.getEndcontractfrom(0)!=null){//if (vparam.get(13) != null) {
                    endcontractfrom = searchSpecialQuery.getEndcontractfrom(0);//(java.util.Date) vparam.get(13);
                }
                if (searchSpecialQuery.getEndcontractto(0) != null) {//if (vparam.get(14) != null) {
                    endcontractto = searchSpecialQuery.getEndcontractto(0);//(java.util.Date) vparam.get(14);
                }
                 if((endcontractfrom != null) && (endcontractto != null)){
                    whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+ " BETWEEN '"+
                                  Formater.formatDate((java.util.Date) endcontractfrom, "yyyy-MM-dd")+ "' AND '"+
                                  Formater.formatDate((java.util.Date) endcontractto, "yyyy-MM-dd")+ "') ";
                }else if((endcontractfrom != null) && (endcontractto == null)){
                        whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+ " > '"+
                                  Formater.formatDate((java.util.Date) endcontractfrom, "yyyy-MM-dd")+ "')";
                }else if((endcontractfrom == null) && (endcontractto != null)){
                         whereClause = whereClause +" AND (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_END_CONTRACT]+ " < '"+
                                  Formater.formatDate((java.util.Date) endcontractto, "yyyy-MM-dd")+ "')";
                }
            }
            
            
            //--- WORK ---------------------------------------------------------
            // parameter : WORKYEARFROM = 15
            int workyearfrom = searchSpecialQuery.getWorkyearfrom(0)!=null && searchSpecialQuery.getWorkyearfrom(0).length()>0? Integer.parseInt(searchSpecialQuery.getWorkyearfrom(0)):0;
            

            // parameter : WORKMONTHFROM = 16
            int workmonthfrom = searchSpecialQuery.getWorkmonthfrom(0)!=null && searchSpecialQuery.getWorkmonthfrom(0).length()>0?Integer.parseInt(searchSpecialQuery.getWorkmonthfrom(0)): 0;
//            try { workmonthfrom = Integer.parseInt(vparam.get(16).toString()); }
//            catch (Exception e) {}
            
            long workfrom = Math.round(((12d * workyearfrom) + workmonthfrom) * DAYS_IN_A_MONTH);
            
            // parameter : WORKYEARTO = 17
            int workyearto = searchSpecialQuery.getWorkyearto(0)!=null&& searchSpecialQuery.getWorkyearto(0).length()>0?Integer.parseInt(searchSpecialQuery.getWorkyearto(0)):  0;
            /*try { workyearto= Integer.parseInt(vparam.get(17).toString()); }
            catch (Exception e) {}*/

            // parameter : WORKMONTHTO = 18
            int workmonthto = searchSpecialQuery.getWorkmonthto(0)!=null&& searchSpecialQuery.getWorkmonthto(0).length()>0? Integer.parseInt(searchSpecialQuery.getWorkmonthto(0)):0;
//            try { workmonthto = Integer.parseInt(vparam.get(18).toString()); }
//            catch (Exception e) {}

            long workto = Math.round(((12d * workyearto) + workmonthto) * DAYS_IN_A_MONTH);
            
            if ((workfrom > 0) && (workto > 0) && (workfrom <= workto)) {
                whereClause += " AND ((TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") >= "+ workfrom + " ) AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") <= "+ workto + " ))";
            }
            else
            if (workfrom > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") >= "+ workfrom + ") ";
            }
            else
            if (workto > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+
                    ") <= "+ workto + ") ";
            }

            //--- AGE ----------------------------------------------------------
            // parameter : AGEYEARFROM = 19
            int ageyearfrom = searchSpecialQuery.getAgeyearfrom(0)!=null&& searchSpecialQuery.getAgeyearfrom(0).length()>0?Integer.parseInt(searchSpecialQuery.getAgeyearfrom(0)):  0;
//            try { ageyearfrom = Integer.parseInt(vparam.get(19).toString()); }
//            catch (Exception e) {}

            // parameter : AGEMONTHFROM = 20
            int agemonthfrom = searchSpecialQuery.getAgemonthfrom(0)!=null&& searchSpecialQuery.getAgemonthfrom(0).length()>0?Integer.parseInt(searchSpecialQuery.getAgemonthfrom(0)):  0;
//            try { agemonthfrom = Integer.parseInt(vparam.get(20).toString()); }
//            catch (Exception e) {}
            
            long agefrom = Math.round(((12d * ageyearfrom) + agemonthfrom) * DAYS_IN_A_MONTH);
            
            // parameter : AGEYEARTO = 21
            int ageyearto = searchSpecialQuery.getAgeyearto(0)!=null&& searchSpecialQuery.getAgeyearto(0).length()>0? Integer.parseInt(searchSpecialQuery.getAgeyearto(0)):  0;
//            try { ageyearto= Integer.parseInt(vparam.get(21).toString()); }
//            catch (Exception e) {}

            // parameter : AGEMONTHTO = 22
            int agemonthto = searchSpecialQuery.getAgemonthto(0)!=null&& searchSpecialQuery.getAgemonthto(0).length()>0?Integer.parseInt(searchSpecialQuery.getAgemonthto(0)):  0;
//            try { agemonthto = Integer.parseInt(vparam.get(22).toString()); }
//            catch (Exception e) {}

            long ageto = Math.round(((12d * ageyearto) + agemonthto) * DAYS_IN_A_MONTH);
            
            if ((agefrom > 0) && (ageto > 0) && (agefrom <= ageto)) {
                whereClause += " AND ((TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") >= "+ agefrom + " ) AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") <= "+ ageto + " ))";
            }
            else
            if (agefrom > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") >= "+ agefrom + ") ";
            }
            else
            if (ageto > 0) {
                whereClause += " AND (TO_DAYS(NOW()) - TO_DAYS(EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_BIRTH_DATE]+
                    ") <= "+ ageto + ") ";
            }
            
            if (searchSpecialQuery.getResignedReasonId()!=0 && searchSpecialQuery.getResignedReasonId() > 0) {
                whereClause = whereClause + " AND RSGNSON."+PstResignedReason.fieldNames[PstResignedReason.FLD_RESIGNED_REASON_ID]+" = " + searchSpecialQuery.getResignedReasonId()+ " ";
            }

            //--- terakhir...
            if (whereClause.length() > 0) {
                sql = sql + whereClause;            
            }
            
 
             if(searchSpecialQuery.getDtCarrierWorkStart()!= null && searchSpecialQuery.getDtCarrierWorkEnd() != null || searchSpecialQuery.getCarrierCategoryEmp()!=0){
                sql = sql + " GROUP BY CRR."+PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID];
            }
             //sql = sql + " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            // parameter : SORTBY = 23
           if(searchSpecialQuery.getSort(0)!=null){
               //if(vparam.get(23)!=null){
            int sortby = Integer.parseInt(searchSpecialQuery.getSort(0));
            switch(sortby){
                case SrcSpecialEmployee.ORDER_EMPLOYEE_NAME :
                    sql = sql + " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] ;
                    break;
                case SrcSpecialEmployee.ORDER_DEPARTMENT:
                    sql = sql + " ORDER BY DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] ;
                    break;
                case SrcSpecialEmployee.ORDER_EMPLOYEE_NUM:
                    sql = sql + " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] ;
                    break;
                case SrcSpecialEmployee.ORDER_COMM_DATE:
                    sql = sql + " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] ;
                    break;
                default:
                    sql = sql + "";
            }
           }


           if(limit == 0 && recordToget == 0)
		sql = sql + "";
            else
		sql = sql + " LIMIT " + limit + ","+ recordToget ;

            //System.out.println(" SQL searchEmployee : \r" + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
          
            while(rs.next()) {
                 //karena countnya ada 2 nilainya count = rs.getInt(1);
                //update by satrya 2013-11-14
                  if(searchSpecialQuery.getDtCarrierWorkStart()!= null && searchSpecialQuery.getDtCarrierWorkEnd() != null || searchSpecialQuery.getCarrierCategoryEmp()!=0){
                        count = count + 1;
                  }else{
                      count = rs.getInt(1);
                  }
              
               
            }
            return count;
        } 
        catch (Exception e) {
            System.out.println("\t Exception on  searchEmployee : " + e);
        }
        finally {
            DBResultSet.close(dbrs);
        }
        return count;
    }
}
