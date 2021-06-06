/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.masterdata;

import com.dimata.harisma.entity.masterdata.EmpDocListMutation;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author GUSWIK
 */
public class FrmEmpDocListMutation extends FRMHandler implements I_FRMInterface, I_FRMType {
  private EmpDocListMutation entEmpDocListMutation;
  
  public static final String FRM_NAME_EMP_DOC_LIST_MUTATION = "FRM_NAME_EMP_DOC_LIST_MUTATION";
  public static final int FRM_FIELD_EMP_DOC_LIST_MUTATION_ID = 0;
  public static final int FRM_FIELD_EMPLOYEE_ID = 1;
  public static final int FRM_FIELD_EMP_DOC_ID = 2;
  public static final int FRM_FIELD_OBJECT_NAME = 3;
  public static final int FRM_FIELD_COMPANY_ID = 4;
  public static final int FRM_FIELD_DIVISION_ID = 5;
  public static final int FRM_FIELD_DEPARTMENT_ID = 6;
  public static final int FRM_FIELD_SECTION_ID = 7;
  public static final int FRM_FIELD_POSITION_ID = 8;
  public static final int FRM_FIELD_EMP_CAT_ID = 9;
  public static final int FRM_FIELD_WORK_FROM = 10;
  public static final int FRM_FIELD_LEVEL_ID = 11;

  public static String[] fieldNames = {
    "FRM_FIELD_EMP_DOC_LIST_MUTATION_ID",
    "FRM_FIELD_EMPLOYEE_ID",
    "FRM_FIELD_EMP_DOC_ID",
    "FRM_FIELD_OBJECT_NAME",
    "FRM_FIELD_COMPANY_ID",
    "FRM_FIELD_DIVISION_ID",
    "FRM_FIELD_DEPARTMENT_ID",
    "FRM_FIELD_SECTION_ID",
    "FRM_FIELD_POSITION_ID",
    "FRM_FIELD_EMP_CAT_ID",
    "FRM_FIELD_WORK_FROM",
    "FRM_FIELD_LEVEL_ID"
};
  
  public static int[] fieldTypes = {
    TYPE_LONG,
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
  
  public FrmEmpDocListMutation() {
}

public FrmEmpDocListMutation(EmpDocListMutation entEmpDocListMutation) {
this.entEmpDocListMutation = entEmpDocListMutation;
}

public FrmEmpDocListMutation(HttpServletRequest request, EmpDocListMutation entEmpDocListMutation) {
super(new FrmEmpDocListMutation(entEmpDocListMutation), request);
this.entEmpDocListMutation = entEmpDocListMutation;
}

public String getFormName() {
return FRM_NAME_EMP_DOC_LIST_MUTATION;
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

public EmpDocListMutation getEntityObject() {
return entEmpDocListMutation;
}

public void requestEntityObject(EmpDocListMutation entEmpDocListMutation) {
try {
this.requestParam();
    entEmpDocListMutation.setEmpDocListMutationId(getLong(FRM_FIELD_EMP_DOC_LIST_MUTATION_ID));
    entEmpDocListMutation.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
    entEmpDocListMutation.setEmpDocId(getLong(FRM_FIELD_EMP_DOC_ID));
    entEmpDocListMutation.setObjectName(getString(FRM_FIELD_OBJECT_NAME));
    entEmpDocListMutation.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
    entEmpDocListMutation.setDivisionId(getLong(FRM_FIELD_DIVISION_ID));
    entEmpDocListMutation.setDepartmentId(getLong(FRM_FIELD_DEPARTMENT_ID));
    entEmpDocListMutation.setSectionId(getLong(FRM_FIELD_SECTION_ID));
    entEmpDocListMutation.setPositionId(getLong(FRM_FIELD_POSITION_ID));
    entEmpDocListMutation.setEmpCatId(getLong(FRM_FIELD_EMP_CAT_ID));
    entEmpDocListMutation.setLevelId(getLong(FRM_FIELD_LEVEL_ID));
    entEmpDocListMutation.setWorkFrom(getDate(FRM_FIELD_WORK_FROM));
} catch (Exception e) {
System.out.println("Error on requestEntityObject : " + e.toString());
}
}

}