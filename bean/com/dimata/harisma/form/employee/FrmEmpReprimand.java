package com.dimata.harisma.form.employee;

/* java package */ 
import java.io.*; 
import java.util.*; 
import javax.servlet.*;
import javax.servlet.http.*; 

/* qdep package */ 
import com.dimata.qdep.form.*;

/* project package */
import com.dimata.harisma.entity.employee.*;

/**
 *
 * @author bayu
 */

public class FrmEmpReprimand extends FRMHandler implements I_FRMInterface, I_FRMType {

    private EmpReprimand reprimand;
    
    public static final String FRM_NAME_EMP_REPRIMAND	=  "FRM_NAME_EMP_REPRIMAND" ;
    
    public static final int FRM_FIELD_EMPLOYEE_ID       = 0;
    public static final int FRM_FIELD_NUMBER            = 1;
    public static final int FRM_FIELD_CHAPTER           = 2;
    public static final int FRM_FIELD_ARTICLE           = 3;
    public static final int FRM_FIELD_VERSE             = 4;
    public static final int FRM_FIELD_PAGE              = 5;
    public static final int FRM_FIELD_DESCRIPTION       = 6;
    public static final int FRM_FIELD_REP_DATE          = 7;
    public static final int FRM_FIELD_VALID_UNTIL       = 8;
    /**
     * Ari_20110909
     * merubah reprimandLevel menjadi reprimandLevelId {
     */
    public static final int FRM_FIELD_REPRIMAND_LEVEL_ID   = 9;
    
    public static String[] fieldNames = {
        "FRM_EMPLOYEE_ID",
        "FRM_NUMBER",
        "FRM_CHAPTER",
        "FRM_ARTICLE",
        "FRM_VERSE",
        "FRM_PAGE",
        "FRM_DESCRIPTION",
        "FRM_REP_DATE",
        "FRM_VALID_UNTIL",
        "FRM_REPRIMAND_LEVEL_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG
    };
    
        
    public FrmEmpReprimand(EmpReprimand reprimand) {
        this.reprimand = reprimand;
    }
    
    public FrmEmpReprimand(HttpServletRequest request, EmpReprimand reprimand) {
        super(new FrmEmpReprimand(reprimand), request);
        this.reprimand = reprimand;
    }
    
      
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME_EMP_REPRIMAND;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public EmpReprimand getEntityObject() {
        return reprimand;
    }
    
    public void requestEntityObject(EmpReprimand reprimand) {
        try {
            this.requestParam();
            
            reprimand.setEmployeeId(this.getLong(FRM_FIELD_EMPLOYEE_ID));
            reprimand.setArticle(this.getString(FRM_FIELD_ARTICLE));
            reprimand.setChapter(this.getString(FRM_FIELD_CHAPTER));
            reprimand.setDescription(this.getString(FRM_FIELD_DESCRIPTION));
            reprimand.setPage(this.getString(FRM_FIELD_PAGE));
            reprimand.setVerse(this.getString(FRM_FIELD_VERSE));
            reprimand.setReprimandNumber(this.getInt(FRM_FIELD_NUMBER));
            reprimand.setReprimandDate(this.getDate(FRM_FIELD_REP_DATE));
            reprimand.setValidityDate(this.getDate(FRM_FIELD_VALID_UNTIL));
            reprimand.setReprimandLevelId(this.getLong(FRM_FIELD_REPRIMAND_LEVEL_ID));
        }
        catch(Exception e) {            
        }
    }
      /*}*/
}
