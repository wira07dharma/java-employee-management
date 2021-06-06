/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.payroll;

import com.dimata.harisma.entity.payroll.ThrRptSetup;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Gunadi
 */
public class FrmThrRptSetup extends FRMHandler implements I_FRMInterface, I_FRMType {

    private ThrRptSetup entThrRptSetup;
    public static final String FRM_NAME_THR_RPT_SETUP = "FRM_NAME_THR_RPT_SETUP";
    public static final int FRM_FIELD_RPT_SETUP_ID = 0;
    public static final int FRM_FIELD_RPT_SETUP_SHOW_IDX = 1;
    public static final int FRM_FIELD_RPT_SETUP_TABLE_GROUP = 2;
    public static final int FRM_FIELD_RPT_SETUP_TABLE_NAME = 3;
    public static final int FRM_FIELD_RPT_SETUP_FIELD_NAME = 4;
    public static final int FRM_FIELD_RPT_SETUP_FIELD_TYPE = 5;
    public static final int FRM_FIELD_RPT_SETUP_FIELD_HEADER = 6;
    public static final int FRM_FIELD_RPT_SETUP_DATA_TYPE = 7;
    public static final int FRM_FIELD_RPT_SETUP_DATA_GROUP = 8;
    public static String[] fieldNames = {
        "FRM_FIELD_RPT_SETUP_ID",
        "FRM_FIELD_RPT_SETUP_SHOW_IDX",
        "FRM_FIELD_RPT_SETUP_TABLE_GROUP",
        "FRM_FIELD_RPT_SETUP_TABLE_NAME",
        "FRM_FIELD_RPT_SETUP_FIELD_NAME",
        "FRM_FIELD_RPT_SETUP_FIELD_TYPE",
        "FRM_FIELD_RPT_SETUP_FIELD_HEADER",
        "FRM_FIELD_RPT_SETUP_DATA_TYPE",
        "FRM_FIELD_RPT_SETUP_DATA_GROUP"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_INT,
        TYPE_STRING,
        TYPE_INT,
        TYPE_INT
    };

    public FrmThrRptSetup() {
    }

    public FrmThrRptSetup(ThrRptSetup entThrRptSetup) {
        this.entThrRptSetup = entThrRptSetup;
    }

    public FrmThrRptSetup(HttpServletRequest request, ThrRptSetup entThrRptSetup) {
        super(new FrmThrRptSetup(entThrRptSetup), request);
        this.entThrRptSetup = entThrRptSetup;
    }

    public String getFormName() {
        return FRM_NAME_THR_RPT_SETUP;
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

    public ThrRptSetup getEntityObject() {
        return entThrRptSetup;
    }

    public void requestEntityObject(ThrRptSetup entThrRptSetup) {
        try {
            this.requestParam();
            entThrRptSetup.setRptSetupShowIdx(getInt(FRM_FIELD_RPT_SETUP_SHOW_IDX));
            entThrRptSetup.setRptSetupTableGroup(getString(FRM_FIELD_RPT_SETUP_TABLE_GROUP));
            entThrRptSetup.setRptSetupTableName(getString(FRM_FIELD_RPT_SETUP_TABLE_NAME));
            entThrRptSetup.setRptSetupFieldName(getString(FRM_FIELD_RPT_SETUP_FIELD_NAME));
            entThrRptSetup.setRptSetupFieldType(getInt(FRM_FIELD_RPT_SETUP_FIELD_TYPE));
            entThrRptSetup.setRptSetupFieldHeader(getString(FRM_FIELD_RPT_SETUP_FIELD_HEADER));
            entThrRptSetup.setRptSetupDataType(getInt(FRM_FIELD_RPT_SETUP_DATA_TYPE));
            entThrRptSetup.setRptSetupDataGroup(getInt(FRM_FIELD_RPT_SETUP_DATA_GROUP));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}