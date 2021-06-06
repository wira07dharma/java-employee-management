/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.payroll;

import com.dimata.harisma.entity.payroll.PayThr;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Gunadi
 */
public class FrmPayThr extends FRMHandler implements I_FRMInterface, I_FRMType {

    private PayThr entPayThr;
    public static final String FRM_NAME_PAY_THR = "FRM_NAME_PAY_THR";
    public static final int FRM_FIELD_PAY_THR_ID = 0;
    public static final int FRM_FIELD_CALCULATION_MAIN_ID = 1;
    public static final int FRM_FIELD_PERIOD_ID = 2;
    public static final int FRM_FIELD_STATUS = 3;
    public static final int FRM_FIELD_CUT_OFF_DATE = 4;
    public static String[] fieldNames = {
        "FRM_FIELD_PAY_THR_ID",
        "FRM_FIELD_CALCULATION_MAIN_ID",
        "FRM_FIELD_PERIOD_ID",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_CUT_OFF_DATE"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE
    };

    public FrmPayThr() {
    }

    public FrmPayThr(PayThr entPayThr) {
        this.entPayThr = entPayThr;
    }

    public FrmPayThr(HttpServletRequest request, PayThr entPayThr) {
        super(new FrmPayThr(entPayThr), request);
        this.entPayThr = entPayThr;
    }

    public String getFormName() {
        return FRM_NAME_PAY_THR;
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

    public PayThr getEntityObject() {
        return entPayThr;
    }

    public void requestEntityObject(PayThr entPayThr) {
        try {
            this.requestParam();
            entPayThr.setCalculationMainId(getLong(FRM_FIELD_CALCULATION_MAIN_ID));
            entPayThr.setPeriodId(getLong(FRM_FIELD_PERIOD_ID));
            entPayThr.setStatus(getInt(FRM_FIELD_STATUS));
            entPayThr.setCutOffDate(getDate(FRM_FIELD_CUT_OFF_DATE));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}