/* Created on 	:  30 September 2011 [time] AM/PM
 *
 * @author  	:  Ari_20110930
 * @version  	:  [version]
 */

/*******************************************************************
 * Class Description 	: FrmCompany
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/

package com.dimata.harisma.form.masterdata;

/**
 *
 * @author Priska
 */

/* java package */

import com.dimata.harisma.entity.configrewardnpunisment.EntriOpnameSales;
import com.dimata.harisma.entity.configrewardnpunisment.PstEntriOpnameSales;
import com.dimata.harisma.entity.employee.Employee;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.harisma.entity.masterdata.*;

public class FrmEmpDocList extends FRMHandler implements I_FRMInterface, I_FRMType{
     private EmpDocList empDocList;
     private Vector vlistEmpDocList = new Vector();
    public static final String FRM_NAME_EMP_DOC_LIST = "FRM_NAME_EMP_DOC_LIST";

    public static final int FRM_FIELD_EMP_DOC_LIST_ID = 0;
    public static final int FRM_FIELD_EMP_DOC_ID = 1;
    public static final int FRM_FIELD_EMPLOYEE_ID = 2;
    public static final int FRM_FIELD_ASSIGN_AS = 3;
    public static final int FRM_FIELD_JOB_DESC = 4;
    public static final int FRM_FIELD_OBJECT_NAME = 5;
    
    public static String[] fieldNames = {
       
        "FRM_FIELD_EMP_DOC_LIST_ID",
        "FRM_FIELD_EMP_DOC_ID",
        "FRM_FIELD_EMPLOYEE_ID",
        "FRM_FIELD_ASSIGN_AS",
        "FRM_FIELD_JOB_DESC",
        "FRM_FIELD_OBJECT_NAME"
    };

    public static int[] fieldTypes = {
        TYPE_LONG, 
        TYPE_LONG,
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public FrmEmpDocList() {
    }

    public FrmEmpDocList(EmpDocList empDocList) {
        this.empDocList = empDocList;
    }

    public FrmEmpDocList(HttpServletRequest request, EmpDocList empDocList) {
        super(new FrmEmpDocList(empDocList), request);
        this.empDocList = empDocList;
    }

    public String getFormName() {
        return FRM_NAME_EMP_DOC_LIST;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public EmpDocList getEntityObject() {
        return empDocList;
    }

    public void requestEntityObject(EmpDocList empDocList) {
        try {
            this.requestParam();
            empDocList.setEmp_doc_id(getLong(FRM_FIELD_EMP_DOC_ID));
            empDocList.setEmployee_id(getLong(FRM_FIELD_EMPLOYEE_ID));
            empDocList.setAssign_as(getString(FRM_FIELD_ASSIGN_AS));
            empDocList.setJob_desc(getString(FRM_FIELD_JOB_DESC));
            empDocList.setObject_name(getString(FRM_FIELD_OBJECT_NAME));
            
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

       public void requestEntityMultipleObject(Vector listE) { //melakukan 
        ///pemanggilan terhadap Employee
        try {
            this.requestParam();
           // String[] selectedUser = this.getParamsStringValues("userSelect");
            
            if(listE.size()>0){
                    for (int x = 0; x < listE.size(); x++) {
                        Vector temp = (Vector)listE.get(x);
		Employee employee = (Employee)temp.get(0);
                EmpDocList empDocList = (EmpDocList)temp.get(12);
                String inputName = empDocList.getEmp_doc_id()+""+employee.getOID();
                        //String selectedUserx = this.getParamString("userSelect_"+inputName);
                //this.getParamDouble(fieldNames[FRM_FIELD_JOB_DESC]+x);
                String job = this.getParamString(fieldNames[FRM_FIELD_JOB_DESC]+x);
                        if(job.length() > 0){
                             //if(selectedUserx.equals("1")){
                             empDocList.setEmp_doc_id(this.getParamLong(fieldNames[FRM_FIELD_EMP_DOC_ID]+x));
                             empDocList.setEmployee_id(this.getParamLong(fieldNames[FRM_FIELD_EMPLOYEE_ID]+x));
                             empDocList.setAssign_as(this.getParamString(fieldNames[FRM_FIELD_ASSIGN_AS]+x));
                             empDocList.setJob_desc(this.getParamString(fieldNames[FRM_FIELD_JOB_DESC]+x));
                             empDocList.setObject_name(this.getParamString(fieldNames[FRM_FIELD_OBJECT_NAME]+x));
                            
                          
                            vlistEmpDocList.add(empDocList);
                        }
                    }
            }
            
            

        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
      public Vector getVlistEmpDocList() {
        return vlistEmpDocList;
    }
}
