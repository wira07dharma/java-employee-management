/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.payroll;

import com.dimata.harisma.entity.payroll.ThrCalculationMain;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Gunadi
 */
public class FrmThrCalculationMain extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ThrCalculationMain entThrCalculationMain;
    public static final String FRM_NAME_THR_CALCULATION_MAIN = "FRM_NAME_THR_CALCULATION_MAIN";
    public static final int FRM_FIELD_CALCULATION_MAIN_ID = 0;
    public static final int FRM_FIELD_CALCULATION_MAIN_TITLE = 1;
    public static final int FRM_FIELD_CALCULATION_MAIN_DESC = 2;
    public static final int FRM_FIELD_CALCULATION_MAIN_DATE_CREATE = 3;
    public static String[] fieldNames = {
        "FRM_FIELD_CALCULATION_MAIN_ID",
        "FRM_FIELD_CALCULATION_MAIN_TITLE",
        "FRM_FIELD_CALCULATION_MAIN_DESC",
        "FRM_FIELD_CALCULATION_MAIN_DATE_CREATE"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public FrmThrCalculationMain() {
    }

    public FrmThrCalculationMain(ThrCalculationMain entThrCalculationMain) {
        this.entThrCalculationMain = entThrCalculationMain;
    }

    public FrmThrCalculationMain(HttpServletRequest request, ThrCalculationMain entThrCalculationMain) {
        super(new FrmThrCalculationMain(entThrCalculationMain), request);
        this.entThrCalculationMain = entThrCalculationMain;
    }

    public String getFormName() {
        return FRM_NAME_THR_CALCULATION_MAIN;
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

    public ThrCalculationMain getEntityObject() {
        return entThrCalculationMain;
    }

    public void requestEntityObject(ThrCalculationMain entThrCalculationMain) {
        try {
            this.requestParam();
            entThrCalculationMain.setCalculationMainTitle(getString(FRM_FIELD_CALCULATION_MAIN_TITLE));
            entThrCalculationMain.setCalculationMainDesc(getString(FRM_FIELD_CALCULATION_MAIN_DESC));
            entThrCalculationMain.setCalculationMainDateCreate(java.sql.Date.valueOf(getString(FRM_FIELD_CALCULATION_MAIN_DATE_CREATE)));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}