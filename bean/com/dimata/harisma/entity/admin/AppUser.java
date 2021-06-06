/*
 * AppUser.java
 *
 * Created on April 3, 2002, 9:29 AM
 */

package com.dimata.harisma.entity.admin;

/**
 *
 * @author  ktanjana
 * @version 
 */


import java.util.*;
import com.dimata.qdep.entity.*;

public class AppUser extends Entity {
    public final static int STATUS_NEW =0;
    public final static int STATUS_LOGOUT =1;
    public final static int STATUS_LOGIN =2;
    
    public final static String[] statusTxt= {"New", "Logged out", "Logged In"};
    
    
    private String loginId="";
    
    private String password="";
    
    private String fullName="";
    
    private String email="";
    
    private String description="";
    
    private java.util.Date regDate=new Date();
    
    private java.util.Date updateDate=new Date();
    
    private int userStatus=-1;
    
    private java.util.Date lastLoginDate=new Date();
    
    private String lastLoginIp="";
    private long employeeId;
    // 2014-11-26 McHen
    private String positionLevelId = "";
    /*
     * Description : berguna untuk enable or disable fitur import or print excel
     * Date : 2015-01-22
     * Author : Hendra Putu
     */
    private int excelIO = 0;
    /* Update by Hendra Putu | 2016-05-10 */
    private int adminStatus = 0;
    
    public AppUser() {
    }
        
    public String getLoginId() {
        return loginId;
    }
    
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public java.util.Date getRegDate() {
        return regDate;
    }
    
    public void setRegDate(java.util.Date regDate) {
        this.regDate = regDate;
    }
    
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    
    public int getUserStatus() {
        return userStatus;
    }

    public static String getStatusTxt(int sts){
        if((sts<0) || (sts> statusTxt.length))
            return "";
        return statusTxt[sts];
    }
    
    public static Vector getStatusTxts(){
        Vector vct = new Vector(1,1);
        for(int i =0 ; i < statusTxt.length ; i++){
            vct.add(statusTxt[i]);
        }
        return vct;
    }
    // Update By Agus 10-02-2014
    public static Vector getStatusTxtsVer2(){
        Vector vct = new Vector(1,1);
        vct.add("All");
        for(int i =0 ; i < statusTxt.length ; i++){
            vct.add(statusTxt[i]);
        }
        return vct;
    }
    
    public static Vector getStatusValsVer2(){
        Vector vct = new Vector(1,1);
        vct.add(""+-1); 
        for(int i =0 ; i < statusTxt.length ; i++){
            vct.add(Integer.toString(i));
        }
        return vct;
    }
    
    public static Vector getStatusVals(){
        Vector vct = new Vector(1,1);
        for(int i =0 ; i < statusTxt.length ; i++){
            vct.add(Integer.toString(i));
        }
        return vct;
    }
    
    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
    
    public java.util.Date getLastLoginDate() {
        return lastLoginDate;
    }
    
    public void setLastLoginDate(java.util.Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
        
    public long getEmployeeId(){ return employeeId; }        

    public void setEmployeeId(long employeeId){ this.employeeId = employeeId; }

    /**
     * @return the positionLevelId
     */
    public String getPositionLevelId() {
        return positionLevelId;
    }

    /**
     * @param positionLevelId the positionLevelId to set
     */
    public void setPositionLevelId(String positionLevelId) {
        this.positionLevelId = positionLevelId;
    }

    /**
     * @return the excelIO
     */
    public int getExcelIO() {
        return excelIO;
    }

    /**
     * @param excelIO the excelIO to set
     */
    public void setExcelIO(int excelIO) {
        this.excelIO = excelIO;
    }

    /**
     * @return the adminStatus
     */
    public int getAdminStatus() {
        return adminStatus;
}

    /**
     * @param adminStatus the adminStatus to set
     */
    public void setAdminStatus(int adminStatus) {
        this.adminStatus = adminStatus;
    }
}

