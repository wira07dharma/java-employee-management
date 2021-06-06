/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.payroll;

import com.dimata.harisma.entity.payroll.PayThrEmployee;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Gunadi
 */
public class FrmPayThrEmployee extends FRMHandler implements I_FRMInterface, I_FRMType {

    private PayThrEmployee entPayThrEmployee;
    public static final String FRM_NAME_PAY_THR_EMPLOYEE = "FRM_NAME_PAY_THR_EMPLOYEE";
    public static final int FRM_FIELD_PAY_THR_EMPLOYEE_ID = 0;
    public static final int FRM_FIELD_PAY_THR_ID = 1;
    public static final int FRM_FIELD_EMPLOYEE_ID = 2;
    public static final int FRM_FIELD_VALUE = 3;
    public static String[] fieldNames = {
        "FRM_FIELD_PAY_THR_EMPLOYEE_ID",
        "FRM_FIELD_PAY_THR_ID",
        "FRM_FIELD_EMPLOYEE_ID",
        "FRM_FIELD_VALUE"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_FLOAT
    };

    public FrmPayThrEmployee() {
    }

    public FrmPayThrEmployee(PayThrEmployee entPayThrEmployee) {
        this.entPayThrEmployee = entPayThrEmployee;
    }

    public FrmPayThrEmployee(HttpServletRequest request, PayThrEmployee entPayThrEmployee) {
        super(new FrmPayThrEmployee(entPayThrEmployee), request);
        this.entPayThrEmployee = entPayThrEmployee;
    }

    public String getFormName() {
        return FRM_NAME_PAY_THR_EMPLOYEE;
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

    public PayThrEmployee getEntityObject() {
        return entPayThrEmployee;
    }

    public void requestEntityObject(PayThrEmployee entPayThrEmployee) {
        try {
            this.requestParam();
            entPayThrEmployee.setPayThrId(getLong(FRM_FIELD_PAY_THR_ID));
            entPayThrEmployee.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
            entPayThrEmployee.setValue(getFloat(FRM_FIELD_VALUE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
