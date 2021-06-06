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

public class FrmEmpWarning extends FRMHandler implements I_FRMInterface, I_FRMType {

    private EmpWarning warning;
    
    public static final String FRM_NAME_EMP_WARNING	=  "FRM_NAME_EMP_WARNING" ;
    
    public static final int FRM_FIELD_EMPLOYEE_ID	=  0 ;
    public static final int FRM_FIELD_BREAK_FACT	=  1 ;
    public static final int FRM_FIELD_BREAK_DATE	=  2 ;
    public static final int FRM_FIELD_WARN_BY   	=  3 ;
    public static final int FRM_FIELD_WARN_DATE         =  4 ;
    public static final int FRM_FIELD_VALID_DATE        =  5 ;
    //public static final int FRM_FIELD_WARN_LEVEL        =  6 ;
    /**
     * Ari_20110909
     * merubah WarnLevel menjadi WarnLevelId {
     */
    public static final int FRM_FIELD_WARN_LEVEL_ID        =  6 ;

    public static String[] fieldNames = {
        "EMPLOYEE_ID",
        "BREAK_FACT",
        "BREAK_DATE",
        "WARN_BY",
        "WARN_DATE",
        "VALID_DATE",
        "WARNING_LEVEL_ID"
    };
    
    public static int[] fieldTypes = {
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_DATE,
        TYPE_DATE,
        TYPE_LONG
    };
    
        
    public FrmEmpWarning(EmpWarning warning) {
        this.warning = warning;
    }
    
    public FrmEmpWarning(HttpServletRequest request, EmpWarning warning) {
        super(new FrmEmpWarning(warning), request);
        this.warning = warning;
    }
    
      
    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getFormName() {
        return FRM_NAME_EMP_WARNING;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }
    
    public EmpWarning getEntityObject() {
        return warning;
    }
    
    public void requestEntityObject(EmpWarning warning) {
        try {
            this.requestParam();
            
            warning.setEmployeeId(this.getLong(FRM_FIELD_EMPLOYEE_ID));
            warning.setBreakDate(this.getDate(FRM_FIELD_BREAK_DATE));
            warning.setBreakFact(this.getString(FRM_FIELD_BREAK_FACT));
            warning.setWarningDate(this.getDate(FRM_FIELD_WARN_DATE));
            warning.setWarningBy(this.getString(FRM_FIELD_WARN_BY));
            warning.setValidityDate(this.getDate(FRM_FIELD_VALID_DATE));
            //warning.setWarnLevel(this.getInt(FRM_FIELD_WARN_LEVEL));
            warning.setWarnLevelId(this.getLong(FRM_FIELD_WARN_LEVEL_ID));
        }
        catch(Exception e) {            
        }
    }
     /*}*/
}
