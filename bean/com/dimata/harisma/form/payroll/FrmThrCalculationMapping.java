/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.payroll;

import com.dimata.harisma.entity.payroll.ThrCalculationMapping;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Gunadi
 */
public class FrmThrCalculationMapping extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ThrCalculationMapping entThrCalculationMapping;
    public static final String FRM_NAME_THR_CALCULATION_MAPPING = "FRM_NAME_THR_CALCULATION_MAPPING";
    public static final int FRM_FIELD_THR_CALCULATION_MAPPING_ID = 0;
    public static final int FRM_FIELD_CALCULATION_MAIN_ID = 1;
    public static final int FRM_FIELD_START_DATE = 2;
    public static final int FRM_FIELD_END_DATE = 3;
    public static final int FRM_FIELD_COMPANY_ID = 4;
    public static final int FRM_FIELD_DIVISION_ID = 5;
    public static final int FRM_FIELD_DEPARTMENT_ID = 6;
    public static final int FRM_FIELD_SECTION_ID = 7;
    public static final int FRM_FIELD_LEVEL_ID = 8;
    public static final int FRM_FIELD_MARITAL_ID = 9;
    public static final int FRM_FIELD_EMPLOYEE_CATEGORY = 10;
    public static final int FRM_FIELD_POSITION_ID = 11;
    public static final int FRM_FIELD_EMPLOYEE_ID = 12;
    public static final int FRM_FIELD_GRADE = 13;
    public static final int FRM_FIELD_LOS_FROM_IN_DAY = 14;
    public static final int FRM_FIELD_LOS_FROM_IN_MONTH = 15;
    public static final int FRM_FIELD_LOS_FROM_IN_YEAR = 16;
    public static final int FRM_FIELD_LOS_TO_IN_DAY = 17;
    public static final int FRM_FIELD_LOS_TO_IN_MONTH = 18;
    public static final int FRM_FIELD_LOS_TO_IN_YEAR = 19;
    public static final int FRM_FIELD_LOS_CURRENT_DATE = 20;
    public static final int FRM_FIELD_LOS_PER_CURRENT_DATE = 21;
    public static final int FRM_FIELD_TAX_MARITAL_ID = 22;
    public static final int FRM_FIELD_MAPPING_TYPE = 23;
    public static final int FRM_FIELD_VALUE = 24;
    public static final int FRM_FIELD_PROPOTIONAL = 25;
    public static final int FRM_FIELD_PAYROLL_GROUP_ID = 26;
    public static String[] fieldNames = {
        "FRM_FIELD_THR_CALCULATION_MAPPING_ID",
        "FRM_FIELD_CALCULATION_MAIN_ID",
        "FRM_FIELD_START_DATE",
        "FRM_FIELD_END_DATE",
        "FRM_FIELD_COMPANY_ID",
        "FRM_FIELD_DIVISION_ID",
        "FRM_FIELD_DEPARTMENT_ID",
        "FRM_FIELD_SECTION_ID",
        "FRM_FIELD_LEVEL_ID",
        "FRM_FIELD_MARITAL_ID",
        "FRM_FIELD_EMPLOYEE_CATEGORY",
        "FRM_FIELD_POSITION_ID",
        "FRM_FIELD_EMPLOYEE_ID",
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
        "FRM_FIELD_MAPPING_TYPE",
        "FRM_FIELD_VALUE",
        "FRM_FIELD_PROPOTIONAL",
        "FRM_FIELD_PAYROLL_GROUP_ID"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_INT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_INT,
        TYPE_FLOAT,
        TYPE_INT,
        TYPE_LONG
    };

    public FrmThrCalculationMapping() {
    }

    public FrmThrCalculationMapping(ThrCalculationMapping entThrCalculationMapping) {
        this.entThrCalculationMapping = entThrCalculationMapping;
    }

    public FrmThrCalculationMapping(HttpServletRequest request, ThrCalculationMapping entThrCalculationMapping) {
        super(new FrmThrCalculationMapping(entThrCalculationMapping), request);
        this.entThrCalculationMapping = entThrCalculationMapping;
    }

    public String getFormName() {
        return FRM_NAME_THR_CALCULATION_MAPPING;
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

    public ThrCalculationMapping getEntityObject() {
        return entThrCalculationMapping;
    }

    public void requestEntityObject(ThrCalculationMapping entThrCalculationMapping) {
        try {
            this.requestParam();
            entThrCalculationMapping.setCalculationMainId(getLong(FRM_FIELD_CALCULATION_MAIN_ID));
            entThrCalculationMapping.setStartDate(getDate(FRM_FIELD_START_DATE));
            entThrCalculationMapping.setEndDate(getDate(FRM_FIELD_END_DATE));
            entThrCalculationMapping.setCompanyId(getLong(FRM_FIELD_COMPANY_ID));
            entThrCalculationMapping.setDivisionId(getLong(FRM_FIELD_DIVISION_ID));
            entThrCalculationMapping.setDepartmentId(getLong(FRM_FIELD_DEPARTMENT_ID));
            entThrCalculationMapping.setSectionId(getLong(FRM_FIELD_SECTION_ID));
            entThrCalculationMapping.setLevelId(getLong(FRM_FIELD_LEVEL_ID));
            entThrCalculationMapping.setMaritalId(getLong(FRM_FIELD_MARITAL_ID));
            entThrCalculationMapping.setEmployeeCategory(getLong(FRM_FIELD_EMPLOYEE_CATEGORY));
            entThrCalculationMapping.setPositionId(getLong(FRM_FIELD_POSITION_ID));
            entThrCalculationMapping.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
            entThrCalculationMapping.setGrade(getLong(FRM_FIELD_GRADE));
            entThrCalculationMapping.setLosFromInDay(getInt(FRM_FIELD_LOS_FROM_IN_DAY));
            entThrCalculationMapping.setLosFromInMonth(getInt(FRM_FIELD_LOS_FROM_IN_MONTH));
            entThrCalculationMapping.setLosFromInYear(getInt(FRM_FIELD_LOS_FROM_IN_YEAR));
            entThrCalculationMapping.setLosToInDay(getInt(FRM_FIELD_LOS_TO_IN_DAY));
            entThrCalculationMapping.setLosToInMonth(getInt(FRM_FIELD_LOS_TO_IN_MONTH));
            entThrCalculationMapping.setLosToInYear(getInt(FRM_FIELD_LOS_TO_IN_YEAR));
            entThrCalculationMapping.setLosCurrentDate(getInt(FRM_FIELD_LOS_CURRENT_DATE));
            entThrCalculationMapping.setLosPerCurrentDate(getDate(FRM_FIELD_LOS_PER_CURRENT_DATE));
            entThrCalculationMapping.setTaxMaritalId(getLong(FRM_FIELD_TAX_MARITAL_ID));
            entThrCalculationMapping.setMappingType(getInt(FRM_FIELD_MAPPING_TYPE));
            entThrCalculationMapping.setValue(getFloat(FRM_FIELD_VALUE));
            entThrCalculationMapping.setPropotional(getInt(FRM_FIELD_PROPOTIONAL));
            entThrCalculationMapping.setPayrollGroupId(getLong(FRM_FIELD_PAYROLL_GROUP_ID));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}