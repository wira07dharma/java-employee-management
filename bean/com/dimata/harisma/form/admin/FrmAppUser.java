/*
 * FrmAppUser.java
 *
 * Created on April 3, 2002, 10:38 AM
 */

package com.dimata.harisma.form.admin;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;
import com.dimata.qdep.db.*;
import com.dimata.qdep.form.*;
import com.dimata.harisma.entity.admin.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author  ktanjana
 * @version 
 */
public class FrmAppUser extends FRMHandler implements I_FRMInterface, I_FRMType {

    public static final String FRM_APP_USER = "APP_USER";
    
    public static final int FRM_LOGIN_ID		= 0;
    public static final int FRM_PASSWORD		= 1;
    public static final int FRM_CFRM_PASSWORD		= 2;
    public static final int FRM_FULL_NAME		= 3;
    public static final int FRM_EMAIL			= 4;
    public static final int FRM_DESCRIPTION		= 5;
    public static final int FRM_REG_DATE		= 6;
    public static final int FRM_UPDATE_DATE		= 7;
    public static final int FRM_USER_STATUS		= 8;
    public static final int FRM_LAST_LOGIN_DATE         = 9;
    public static final int FRM_LAST_LOGIN_IP		= 10;
    public static final int FRM_USER_GROUP		= 11;
    public static final int FRM_EMPLOYEE_ID		= 12;
    // 2014-11-26 Update by McHen
    public static final int FRM_POSITION_LEVEL_ID       = 13;
    /*
     * Description : berguna untuk enable or disable fitur import or print excel
     * Date : 2015-01-22
     * Author : Hendra Putu
     */
    public static final int FRM_EXCEL_IO = 14;

    public static  final String[] fieldNames = {
        "LOGIN_ID", "PASSWORD", "CFRM_PASSWORD","FULL_NAME", "EMAIL", "DESCRIPTION"
        ,"REG_DATE", "UPDATE_DATE", "USER_STATUS", "LAST_LOGIN_DATE", "LAST_LOGIN_IP"
        , "USER_GROUP","EMPLOYEE_ID","POSITION_LEVEL_ID","EXCEL_IO"
    } ;

    public static int[] fieldTypes = {
        TYPE_STRING + ENTRY_REQUIRED , TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + ENTRY_REQUIRED,  
        TYPE_STRING + ENTRY_REQUIRED, TYPE_STRING + FORMAT_EMAIL,  TYPE_STRING,
        TYPE_DATE, TYPE_DATE, TYPE_INT, TYPE_DATE, TYPE_STRING,  TYPE_COLLECTION,TYPE_LONG,TYPE_STRING,TYPE_INT
    };
    // 2014-11-27 update by Hendra McHen
    private String[] SelectedValues;
    
    private AppUser appUser = new AppUser();
    
    
    
    /** Creates new FrmAppUser */
    public FrmAppUser() {
    }

    public FrmAppUser(HttpServletRequest request) {
        super(new FrmAppUser(), request);
    }
    
    public String getFormName() {
        return FRM_APP_USER;
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
    
    
    public AppUser getEntityObject()
    {
        return appUser;
    }
    
        
    public void requestEntityObject(AppUser entObj)
    {        
        try {
            this.requestParam();                    
            entObj.setLoginId(this.getString(FRM_LOGIN_ID));
            entObj.setPassword(this.getString(FRM_PASSWORD));
            entObj.setFullName(this.getString(FRM_FULL_NAME));
            entObj.setEmail(this.getString(FRM_EMAIL));
            entObj.setDescription(this.getString(FRM_DESCRIPTION));
            entObj.setRegDate(this.getDate(FRM_REG_DATE));
            entObj.setUpdateDate(this.getDate(FRM_UPDATE_DATE));
            entObj.setUserStatus(this.getInt(FRM_USER_STATUS));
            entObj.setLastLoginDate(this.getDate(FRM_LAST_LOGIN_DATE));
            entObj.setLastLoginIp(this.getString(FRM_LAST_LOGIN_IP));
            entObj.setEmployeeId(this.getLong(FRM_EMPLOYEE_ID));           
            // 2014-11-27 update by Hendra McHen
            entObj.setPositionLevelId(getSelectedCheckBox());
            // 2015-01-22 Update by Hendra McHen
            entObj.setExcelIO(this.getInt(FRM_EXCEL_IO));
            this.appUser = entObj;
        }catch(Exception e) {
            System.out.println("EXC... "+e);
            entObj = new AppUser();
        }       
    }
    /**
     * has to be call after requestEntityObject
     * return Vector of UserGroup objects
     **/ 
    public Vector getUserGroup(long userOID){
        Vector userGroups = new Vector(1,1);
        
        Vector groupOIDs = this.getVectorLong(this.fieldNames[FRM_USER_GROUP]);        
        
        if (groupOIDs==null)
            return userGroups;
        int max = groupOIDs.size();
        
        for(int i=0; i< max; i++){
            long groupOID = ( (Long)groupOIDs.get(i)).longValue();
            UserGroup ug = new UserGroup();
            ug.setUserID(userOID);
            ug.setGroupID(groupOID);
            userGroups.add(ug);
        }
        return userGroups;
    }
    
    public String[] getSelectedValues() {
        return SelectedValues;
    }

    /**
     * @param SelectedValues the SelectedValues to set
     */
    public void setSelectedValues(String[] SelectedValues) {
        this.SelectedValues = SelectedValues;
    }
    // 2014-11-27 update by Hendra McHen
    public String getSelectedCheckBox(){
        String[] checks = getSelectedValues();
        String checkValues = "";
        if (checks != null){
            for (int i = 0; i < checks.length; ++i){
                if (i != checks.length-1) {
                    checkValues = checkValues + checks[i] + "-";
                }
                else {
                    checkValues = checkValues + checks[i];
                }
            }
        
        } else {
            checkValues = "-";
        }
        return checkValues;
    }
}

