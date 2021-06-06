/* 
 * Form Name  	:  FrmDistrict.java
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
package com.dimata.harisma.form.masterdata;

/* java package */
import com.dimata.harisma.entity.masterdata.District;
import com.dimata.harisma.form.masterdata.*;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.harisma.entity.masterdata.District;

import javax.servlet.http.HttpServletRequest;

public class FrmDistrict extends FRMHandler implements I_FRMInterface, I_FRMType {

    private District district;
    public static final String FRM_NAME_DISTRICT = "FRM_NAME_DISTRICT";
    public static final int FRM_FIELD_DISTRICT_ID = 0;
    public static final int FRM_FIELD_ID_NEGARA = 1;
    public static final int FRM_FIELD_ID_PROPINSI = 2;
    public static final int FRM_FIELD_ID_KABUPATEN = 3;
    public static final int FRM_FIELD_ID_KECAMATAN = 4;
    public static final int FRM_FIELD_NAMA_DISTRICT = 5;
    public static String[] fieldNames = {
        "FRM_FIELD_DISTRICT_ID",
        "FRM_FIELD_ID_NEGARA",
        "FRM_FIELD_ID_PROPINSI",
        "FRM_FIELD_ID_KABUPATEN",
        "FRM_FIELD_ID_KECAMATAN", 
        "FRM_FIELD_NAMA_DISTRICT"
    };
    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING
    };

    public FrmDistrict() {
    }

    public FrmDistrict(District district) {
        this.district = district;
    }

    public FrmDistrict(HttpServletRequest request, District district) {
        super(new FrmDistrict(district), request);
        this.district = district;
    }

    public String getFormName() {
        return FRM_NAME_DISTRICT;
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

    public District getEntityObject() {
        return district;
    }

    public void requestEntityObject(District district) {
        try {
            this.requestParam();
            district.setDistrictId(getLong(FRM_FIELD_DISTRICT_ID));
            district.setIdNegara(getLong(FRM_FIELD_ID_NEGARA));
            district.setIdPropinsi(getLong(FRM_FIELD_ID_PROPINSI));
            district.setIdKabupaten(getLong(FRM_FIELD_ID_KABUPATEN));
            district.setIdKecamatan(getLong(FRM_FIELD_ID_KECAMATAN));
            district.setNmDistrict(getString(FRM_FIELD_NAMA_DISTRICT));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
