/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.attendance;

import com.dimata.harisma.entity.attendance.SpecialStockId;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import javax.servlet.http.HttpServletRequest;

public class FrmSpecialStockId extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SpecialStockId entSpecialStockId;
    public static final String FRM_FIELD_SPECIAL_STOCK_ID = "FRM_FIELD_SPECIAL_STOCK_ID";
    public static final int FRM_FIELD_SCHEDULE_ID = 0;
    public static final int FRM_FIELD_QTY = 1;
    public static final int FRM_FIELD_OWNING_DATE = 2;
    public static final int FRM_FIELD_EXPIRED_DATE = 3;
    public static final int FRM_FIELD_STATUS = 4;
    public static final int FRM_FIELD_NOTE = 5;
    public static final int FRM_FIELD_EMPLOYEE_ID = 6;
    public static final int FRM_FIELD_QTY_USED = 7;
    public static final int FRM_FIELD_QTY_RESIDUE = 8;
    public static final int FRM_FIELD_ADDRESS_ID_CARD = 9;

    public static String[] fieldNames = {
        "FRM_FIELD_SCHEDULE_ID",
        "FRM_FIELD_QTY",
        "FRM_FIELD_OWNING_DATE",
        "FRM_FIELD_EXPIRED_DATE",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_NOTE",
        "FRM_FIELD_EMPLOYEE_ID",
        "FRM_FIELD_QTY_USED",
        "FRM_FIELD_QTY_RESIDUE",
        "FRM_FIELD_ADDRESS_ID_CARD"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_INT,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_FLOAT,
        TYPE_FLOAT,
        TYPE_LONG
    };

    public FrmSpecialStockId() {
    }

    public FrmSpecialStockId(SpecialStockId entSpecialStockId) {
        this.entSpecialStockId = entSpecialStockId;
    }

    public FrmSpecialStockId(HttpServletRequest request, SpecialStockId entSpecialStockId) {
        super(new FrmSpecialStockId(entSpecialStockId), request);
        this.entSpecialStockId = entSpecialStockId;
    }

    public String getFormName() {
        return FRM_FIELD_SPECIAL_STOCK_ID;
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

    public SpecialStockId getEntityObject() {
        return entSpecialStockId;
    }

    public void requestEntityObject(SpecialStockId entSpecialStockId) {
        try {
            this.requestParam();
            entSpecialStockId.setScheduleId(getLong(FRM_FIELD_SCHEDULE_ID));
            entSpecialStockId.setQty(getFloat(FRM_FIELD_QTY));
            entSpecialStockId.setOwningDate(getDate(FRM_FIELD_OWNING_DATE));
            entSpecialStockId.setExpiredDate(getDate(FRM_FIELD_EXPIRED_DATE));
            entSpecialStockId.setStatus(getInt(FRM_FIELD_STATUS));
            entSpecialStockId.setNote(getString(FRM_FIELD_NOTE));
            entSpecialStockId.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
            entSpecialStockId.setQtyUsed(getFloat(FRM_FIELD_QTY_USED));
            entSpecialStockId.setQtyResidue(getFloat(FRM_FIELD_QTY_RESIDUE));
            entSpecialStockId.setAddressIdCard(getLong(FRM_FIELD_ADDRESS_ID_CARD));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

    void addError(String FRM_FIELD_SPECIAL_STOCK_ID, String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
