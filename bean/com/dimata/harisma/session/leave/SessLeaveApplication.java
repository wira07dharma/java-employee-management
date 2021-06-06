/*
 * SessLeaveApplication.java
 *
 * Created on October 22, 2004, 5:00 PM
 */
package com.dimata.harisma.session.leave;

// import package core java
import com.dimata.harisma.form.attendance.CtrlAlStockManagement;
import com.dimata.harisma.form.attendance.CtrlAlStockTaken;
import com.dimata.harisma.form.attendance.CtrlSpecialLeaveStock;
import com.dimata.util.Command;
import java.util.Vector;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import com.dimata.system.entity.system.*;
import com.dimata.harisma.session.attendance.*;
import com.dimata.harisma.session.leave.*;

// import package dimata
import com.dimata.qdep.db.*;
import com.dimata.util.LogicParser;
import com.dimata.util.Formater;

// import package project
import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.leave.*;
import com.dimata.harisma.entity.search.*;
import com.dimata.harisma.session.employee.SessEmployee;
import com.dimata.harisma.utility.service.presence.AbsenceAnalyser;
import com.dimata.harisma.utility.service.presence.LatenessAnalyser;
import com.dimata.util.DateCalc;
import java.util.Hashtable;

/**
 *
 * @author gedhy
 */
public class SessLeaveApplication {
    
    public static final String SESS_SRC_DP_APPLICATION = "SESS_SRC_DP_APPLICATION";
    public static final String SESS_SRC_LEAVE_APPLICATION = "SESS_SRC_LEAVE_APPLICATION";
    public static final int GM_SECTION_SCOPE = 0;
    public static final int GM_DEPARTMENT_SCOPE = 1;
    public static final int GM_DIVISION_SCOPE = 2;

    /**
     * @param text
     * @return
     */
    private static Vector logicParser(String text) {
        Vector vector = LogicParser.textSentence(text);
        for (int i = 0; i < vector.size(); i++) {
            String code = (String) vector.get(i);
            if (((vector.get(vector.size() - 1)).equals(LogicParser.SIGN))
                    && ((vector.get(vector.size() - 1)).equals(LogicParser.ENGLISH))) {
                vector.remove(vector.size() - 1);
            }
        }
        return vector;
    }

    /**
     * @param objSrcLeaveApplication
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector searchDpApplication(SrcLeaveApplication objSrcLeaveApplication, int start, int recordToGet) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        if (objSrcLeaveApplication == null) {
            return new Vector(1, 1);
        }
        
        try {
            String sql = " SELECT DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_DP_APPLICATION_ID]
                    + ", DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_EMPLOYEE_ID]
                    + ", DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_DP_ID]
                    + ", DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_SUBMISSION_DATE]
                    + ", DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_TAKEN_DATE]
                    + ", DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_APPROVAL_ID]
                    + ", DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_DOC_STATUS]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + " FROM " + PstDpApplication.TBL_DP_APPLICATION + " AS DP "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApplication.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strTaken = "";
            if (!objSrcLeaveApplication.isTaken()) {
                if ((objSrcLeaveApplication.getTakenDate() != null) && (objSrcLeaveApplication.getTakenDate() != null)) {
                    String strTakenDate = Formater.formatDate(objSrcLeaveApplication.getTakenDate(), "yyyy-MM-dd");
                    strTaken = strTaken + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_TAKEN_DATE]
                            + " = \"" + strTakenDate + "\"";
                }
            }
            
            String strStatus = "";
            if (objSrcLeaveApplication.getStatus() > -1) {
                strStatus = strStatus + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_DOC_STATUS]
                        + " = " + objSrcLeaveApplication.getStatus();
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case PstDpApplication.FLD_NOT_APPROVED:
                    strApprovalStatus = strApprovalStatus + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_APPROVAL_ID] + " = 0";
                    break;
                
                case PstDpApplication.FLD_APPROVE_BY_DEPT_HEAD:
                    strApprovalStatus = strApprovalStatus + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_APPROVAL_ID] + " > 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strTaken != null && strTaken.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strTaken;
                } else {
                    whereClause = whereClause + strTaken;
                }
            }
            
            if (strStatus != null && strStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatus;
                } else {
                    whereClause = whereClause + strStatus;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            sql = sql + " ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_TAKEN_DATE];
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

//            System.out.println("SQL Dp Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                DpApplication objDpApplication = new DpApplication();
                Employee employee = new Employee();
                
                objDpApplication.setOID(rs.getLong(PstDpApplication.fieldNames[PstDpApplication.FLD_DP_APPLICATION_ID]));
                objDpApplication.setEmployeeId(rs.getLong(PstDpApplication.fieldNames[PstDpApplication.FLD_EMPLOYEE_ID]));
                objDpApplication.setDpId(rs.getLong(PstDpApplication.fieldNames[PstDpApplication.FLD_DP_ID]));
                objDpApplication.setSubmissionDate(rs.getDate(PstDpApplication.fieldNames[PstDpApplication.FLD_SUBMISSION_DATE]));
                objDpApplication.setTakenDate(rs.getDate(PstDpApplication.fieldNames[PstDpApplication.FLD_TAKEN_DATE]));
                objDpApplication.setApprovalId(rs.getLong(PstDpApplication.fieldNames[PstDpApplication.FLD_APPROVAL_ID]));
                objDpApplication.setDocStatus(rs.getInt(PstDpApplication.fieldNames[PstDpApplication.FLD_DOC_STATUS]));
                vectTemp.add(objDpApplication);
                
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                vectTemp.add(employee);
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach DpApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);
    }

    /**
     * @param objSrcLeaveApplication
     * @return
     */
    public static int getCountSearchDpApplication(SrcLeaveApplication objSrcLeaveApplication) {
        DBResultSet dbrs = null;
        int result = 0;
        
        if (objSrcLeaveApplication == null) {
            return result;
        }
        
        try {
            String sql = " SELECT COUNT(DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_DP_APPLICATION_ID] + ")"
                    + " FROM " + PstDpApplication.TBL_DP_APPLICATION + " AS DP "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " = \"" + objSrcLeaveApplication.getEmpNum() + "\"";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strTaken = "";
            if (!objSrcLeaveApplication.isTaken()) {
                if ((objSrcLeaveApplication.getTakenDate() != null) && (objSrcLeaveApplication.getTakenDate() != null)) {
                    String strTakenDate = Formater.formatDate(objSrcLeaveApplication.getTakenDate(), "yyyy-MM-dd");
                    strTaken = strTaken + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strTakenDate + "\"";
                }
            }
            
            String strStatus = "";
            if (objSrcLeaveApplication.getStatus() > -1) {
                strStatus = strStatus + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_DOC_STATUS]
                        + " = " + objSrcLeaveApplication.getStatus();
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case PstDpApplication.FLD_NOT_APPROVED:
                    strApprovalStatus = strApprovalStatus + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_APPROVAL_ID] + " = 0";
                    break;
                
                case PstDpApplication.FLD_APPROVE_BY_DEPT_HEAD:
                    strApprovalStatus = strApprovalStatus + " DP." + PstDpApplication.fieldNames[PstDpApplication.FLD_APPROVAL_ID] + " > 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strTaken != null && strTaken.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strTaken;
                } else {
                    whereClause = whereClause + strTaken;
                }
            }
            
            if (strStatus != null && strStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatus;
                } else {
                    whereClause = whereClause + strStatus;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }

//            System.out.println("SQL getCountSearchDpApplication : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
                break;
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on getCountSearchDpApplication :  " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    // ------------------------- START LEAVE MANAGEMENT -------------------------
    /**
     * @param objSrcLeaveApplication
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector searchLeaveApplication(SrcLeaveApplication objSrcLeaveApplication, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        if (objSrcLeaveApplication == null) {
            return new Vector(1, 1);
        }
        
        try {
            String sql = " SELECT LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApplication.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strStatus = "";
            if (objSrcLeaveApplication.getStatus() > -1) {
                strStatus = strStatus + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                        + " = " + objSrcLeaveApplication.getStatus();
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case PstLeaveApplication.FLD_NOT_APPROVED:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_DEPT_HEAD:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_HR_MANAGER:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " > 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strStatus != null && strStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    //whereClause = whereClause + " AND " + strStatus;
                } else {
                    //whereClause = whereClause + strStatus;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    //whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    //whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }
            
            System.out.println("SQL Leave Application : " + sql);
            
            I_Leave leaveConfig = null;
            
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Employee employee = new Employee();
                Department department = new Department();
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                vectTemp.add(objLeaveApplication);
                
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                vectTemp.add(employee);
                
                result.add(vectTemp);
                
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication: " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);
    }
    
    public static int CountsearchLeaveApplication(SrcLeaveApp objSrcLeaveApplication) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        boolean status = false;
        if (objSrcLeaveApplication == null) {
            return 0;
        }
        
        try {
            String sql = " SELECT COUNT(*) "
                    + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + //update by satrya 2013-03-08
                    " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL + " AS PBL ON "
                    + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID]
                    + " = LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApplication.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strDraft = "";
            if (objSrcLeaveApplication.isDraft()) {
                strDraft = strDraft + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 0";
            }
            
            String strToBeApprove = "";
            if (objSrcLeaveApplication.isToBeApprove()) {
                strToBeApprove = strToBeApprove + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 1";                
            }            
            
            String strApproved = "";
            if (objSrcLeaveApplication.isApproved()) {
                strApproved = strApproved + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 2";
            }
            
            String strExecuted = "";
            if (objSrcLeaveApplication.isExecuted()) {
                strExecuted = strExecuted + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 3";
            }
            
            
            String strStatus = "";
            if (objSrcLeaveApplication.getStatus() > -1) {
                strStatus = strStatus + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                        + " = " + objSrcLeaveApplication.getStatus();
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case PstLeaveApplication.FLD_NOT_APPROVED:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_DEPT_HEAD:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_HR_MANAGER:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " > 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            //whereClause= " PBL."+ PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = "+objSrcLeaveApplication.getTypePublicLeave();
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }

            //update by satrya 2013-03-13
            if (objSrcLeaveApplication.getTypePublicLeave() != 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApplication.getTypePublicLeave();
                } else {
                    whereClause = whereClause + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApplication.getTypePublicLeave();
                }
            }
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strDraft != null && strDraft.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND (" + strDraft;
                } else {
                    whereClause = whereClause + "(" + strDraft;
                }
                status = true;
            }
            
            if (strToBeApprove != null && strToBeApprove.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    if (status == true) {
                        whereClause = whereClause + " OR " + strToBeApprove;
                    } else {
                        whereClause = whereClause + " AND (" + strToBeApprove;
                    }
                } else {
                    whereClause = whereClause + "(" + strToBeApprove;
                }
                status = true;
            }
            
            if (strApproved != null && strApproved.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    if (status == true) {
                        whereClause = whereClause + " OR " + strApproved;
                    } else {
                        whereClause = whereClause + " AND (" + strApproved;
                    }
                    status = true;
                } else {
                    whereClause = whereClause + "(" + strApproved;
                }
                status = true;
            }
            
            if (strExecuted != null && strExecuted.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    if (status == true) {
                        whereClause = whereClause + " OR " + strExecuted;
                    } else {
                        whereClause = whereClause + " AND (" + strExecuted;
                    }
                    status = true;
                } else {
                    whereClause = whereClause + "(" + strExecuted;
                }
            }
            
            if (status == true) {
                whereClause = whereClause + " ) ";
            }            
            
            if (strStatus != null && strStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    //whereClause = whereClause + " AND " + strStatus;
                } else {
                    //whereClause = whereClause + strStatus;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    //whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    //whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }


            //System.out.println("SQL Count List Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    
    public static Date getLeaveDateEnd(long appId) {
        
        LeaveApplication leaveApplication = new LeaveApplication();
        
        Vector resultAl = SessLeaveApp.getListVectTakenAl(appId);
        Vector resultLl = SessLeaveApp.getListVectTakenLl(appId);
        Vector resultDp = SessLeaveApp.getListVectTakenDp(appId);
        Vector resultSp = SessLeaveApp.getListVectTakenSp(appId);
        
        Date tempDate = new Date();
        Date dtTerbesar = null;
        
        if (resultAl != null) {
            
            AlStockTaken alStockTaken = new AlStockTaken();
            
            alStockTaken = (AlStockTaken) resultAl.get(0);
            
            dtTerbesar = alStockTaken.getTakenFinnishDate();
            
        } else if (resultLl != null) {
            LlStockTaken llStockTaken = new LlStockTaken();
            
            llStockTaken = (LlStockTaken) resultLl.get(0);
            
            dtTerbesar = llStockTaken.getTakenFinnishDate();
            
        } else if (resultDp != null) {
            DpStockTaken dpStockTaken = new DpStockTaken();
            
            dpStockTaken = (DpStockTaken) resultDp.get(0);
            
            dtTerbesar = dpStockTaken.getTakenFinnishDate();
            
        } else if (resultSp != null) {
            
            SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();
            
            specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) resultSp.get(0);
            
            dtTerbesar = specialUnpaidLeaveTaken.getTakenFinnishDate();
        }
        
        
        for (int indxAl = 0; indxAl < resultAl.size(); indxAl++) {
            
            AlStockTaken alStockTaken = new AlStockTaken();
            
            alStockTaken = (AlStockTaken) resultAl.get(indxAl);
            
            int bigDates = ((int) dtTerbesar.getTime() / (24 * 60 * 60 * 1000));
            
            int intdtDate = ((int) alStockTaken.getTakenFinnishDate().getTime() / (24 * 60 * 60 * 1000));
            
            if (intdtDate > bigDates) {
                
                dtTerbesar = alStockTaken.getTakenFinnishDate();
                
            }
        }
        
        for (int indxLl = 0; indxLl < resultLl.size(); indxLl++) {
            
            LlStockTaken llStockTaken = new LlStockTaken();
            
            llStockTaken = (LlStockTaken) resultLl.get(indxLl);
            
            int bigDates = ((int) dtTerbesar.getTime() / (24 * 60 * 60 * 1000));
            
            int intdtDate = ((int) llStockTaken.getTakenFinnishDate().getTime() / (24 * 60 * 60 * 1000));
            
            if (intdtDate > bigDates) {
                
                dtTerbesar = llStockTaken.getTakenFinnishDate();
                
            }
            
        }
        
        for (int indxDp = 0; indxDp < resultDp.size(); indxDp++) {
            
            DpStockTaken dpStockTaken = new DpStockTaken();
            
            dpStockTaken = (DpStockTaken) resultDp.get(indxDp);
            
            int bigDates = ((int) dtTerbesar.getTime() / (24 * 60 * 60 * 1000));
            
            int intdtDate = ((int) dpStockTaken.getTakenFinnishDate().getTime() / (24 * 60 * 60 * 1000));
            
            if (intdtDate > bigDates) {
                
                dtTerbesar = dpStockTaken.getTakenFinnishDate();
                
            }
            
        }
        
        for (int indxSp = 0; indxSp < resultSp.size(); indxSp++) {
            
            SpecialUnpaidLeaveTaken specialUnapaidLeaveTaken = new SpecialUnpaidLeaveTaken();
            
            specialUnapaidLeaveTaken = (SpecialUnpaidLeaveTaken) resultSp.get(indxSp);
            
            int bigDates = ((int) dtTerbesar.getTime() / (24 * 60 * 60 * 1000));
            
            int intdtDate = ((int) specialUnapaidLeaveTaken.getTakenFinnishDate().getTime() / (24 * 60 * 60 * 1000));
            
            if (intdtDate > bigDates) {
                
                dtTerbesar = specialUnapaidLeaveTaken.getTakenFinnishDate();
                
            }
            
        }
        return dtTerbesar;
    }
    
    public static Vector getApplicationApproved() {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            String sql = "SELECT " + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " FROM "
                    + PstLeaveApplication.TBL_LEAVE_APPLICATION + " WHERE " + PstLeaveApplication.FLD_DOC_STATUS + " = "
                    + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                LeaveApplication leaveApplication = new LeaveApplication();
                leaveApplication.setOID(rs.getLong(1));
                result.add(leaveApplication);
            }
            rs.close();
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        return null;
    }
    
    public static Vector searchLeaveApplicationExecute(SrcLeaveApplication objSrcLeaveApplication, int start, int recordToGet) {
        DBResultSet dbrs = null;
        
        
        Vector appliId = new Vector();
        Date fnsDate = new Date();
        Date dateNow = new Date();
        Vector listAppClosing = new Vector();
        
        int intDateNow = (int) dateNow.getTime() / (24 * 60 * 60 * 1000);
        
        appliId = getApplicationApproved();
        
        if (appliId == null) {
            return null;
        }
        
        if (objSrcLeaveApplication == null) {
            return new Vector(1, 1);
        }
        
        for (int inx = 0; inx < appliId.size(); inx++) {
            
            LeaveApplication leaveApplication = new LeaveApplication();
            
            leaveApplication = (LeaveApplication) appliId.get(inx);
            
            fnsDate = getLeaveDateEnd(leaveApplication.getOID());
            
            int intfnsDate = (int) fnsDate.getTime() / (24 * 60 * 60 * 1000);
            
            if (intfnsDate > intDateNow) {
                listAppClosing.add("" + leaveApplication.getOID());
            }
        }
        
        
        Vector result = new Vector(1, 1);
        
        try {
            String sql = " SELECT LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]
                    + ", LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApplication.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strStatus = "";
            if (objSrcLeaveApplication.getStatus() > -1) {
                strStatus = strStatus + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                        + " = " + objSrcLeaveApplication.getStatus();
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case PstLeaveApplication.FLD_NOT_APPROVED:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_DEPT_HEAD:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_HR_MANAGER:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " > 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strStatus != null && strStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatus;
                } else {
                    whereClause = whereClause + strStatus;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

//            System.out.println("SQL Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Employee employee = new Employee();
                Department department = new Department();
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                vectTemp.add(objLeaveApplication);
                
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                vectTemp.add(employee);
                
                result.add(vectTemp);
                
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);
    }

    /**
     * @param objSrcLeaveApplication
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector searchLeaveApplicationHR(SrcLeaveApplication objSrcLeaveApplication, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        if (objSrcLeaveApplication == null) {
            return new Vector(1, 1);
        }
        
        try {
            String sql = " SELECT LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + " FROM " + PstSpecialLeave.TBL_HR_SPECIAL_LEAVE + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApplication.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }

            /*String strStatus = "";
             if ( objSrcLeaveApplication.getStatus() > -1 ) 
             {                
             strStatus = strStatus + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] +
             " = " + objSrcLeaveApplication.getStatus();                    
             }*/
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                /*case PstLeaveApplication.FLD_NOT_APPROVED : 
                 strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                 break;
                 case PstLeaveApplication.FLD_APPROVE_BY_DEPT_HEAD :                    
                 strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0" +
                 " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";                    
                 break;
                 case PstLeaveApplication.FLD_APPROVE_BY_HR_MANAGER :                    
                 strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0" +
                 " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " > 0";                    
                 break;
                 */
                
                case 0:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }

            /*if(strStatus!=null && strStatus.length()>0)
             {
             if(whereClause!=null && whereClause.length()>0)
             {
             whereClause = whereClause + " AND " + strStatus;
             }
             else
             {
             whereClause = whereClause + strStatus;
             }                
             }*/
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

//            System.out.println("SQL Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                SpecialLeave objLeaveApplication = new SpecialLeave();
                Employee employee = new Employee();
                Employee empAppr1 = new Employee();
                Employee empAppr2 = new Employee();
                Employee empAppr3 = new Employee();
                
                objLeaveApplication.setOID(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setRequestDate(rs.getDate(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]));
                objLeaveApplication.setApprovalId(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID]));
                objLeaveApplication.setApproval2Id(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_ID]));
                objLeaveApplication.setApproval3Id(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_ID]));
                vectTemp.add(objLeaveApplication);
                
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                vectTemp.add(employee);
                
                try {
                    empAppr1 = PstEmployee.fetchExc(objLeaveApplication.getApprovalId());
                } catch (Exception e) {
                    empAppr1 = new Employee();
                }
                vectTemp.add(empAppr1);
                
                try {
                    empAppr2 = PstEmployee.fetchExc(objLeaveApplication.getApproval2Id());
                } catch (Exception e) {
                    empAppr2 = new Employee();
                }
                vectTemp.add(empAppr2);
                
                try {
                    empAppr3 = PstEmployee.fetchExc(objLeaveApplication.getApproval3Id());
                } catch (Exception e) {
                    empAppr3 = new Employee();
                }
                vectTemp.add(empAppr3);
                
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);
    }
    
    public static Vector searchLeaveApplicationHR(SrcLeaveApp objSrcLeaveApplication, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        if (objSrcLeaveApplication == null) {
            return new Vector(1, 1);
        }
        
        try {
            String sql = " SELECT LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_ID]
                    + ", LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + " FROM " + PstSpecialLeave.TBL_HR_SPECIAL_LEAVE + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApplication.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                
                case 0:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }
            
            System.out.println("SQL Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                SpecialLeave objLeaveApplication = new SpecialLeave();
                Employee employee = new Employee();
                Employee empAppr1 = new Employee();
                Employee empAppr2 = new Employee();
                Employee empAppr3 = new Employee();
                
                objLeaveApplication.setOID(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setRequestDate(rs.getDate(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]));
                objLeaveApplication.setApprovalId(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID]));
                objLeaveApplication.setApproval2Id(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_ID]));
                objLeaveApplication.setApproval3Id(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_ID]));
                vectTemp.add(objLeaveApplication);
                
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                vectTemp.add(employee);
                
                try {
                    empAppr1 = PstEmployee.fetchExc(objLeaveApplication.getApprovalId());
                } catch (Exception e) {
                    empAppr1 = new Employee();
                }
                vectTemp.add(empAppr1);
                
                try {
                    empAppr2 = PstEmployee.fetchExc(objLeaveApplication.getApproval2Id());
                } catch (Exception e) {
                    empAppr2 = new Employee();
                }
                vectTemp.add(empAppr2);
                
                try {
                    empAppr3 = PstEmployee.fetchExc(objLeaveApplication.getApproval3Id());
                } catch (Exception e) {
                    empAppr3 = new Employee();
                }
                vectTemp.add(empAppr3);
                
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);
    }
    
    public static Vector searchLeaveApplication(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        boolean status = false;
        
        if (objSrcLeaveApp == null) {
            return new Vector(1, 1);
        }
        
        try {
            
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEP ON "
                    + " DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + //update by satrya 2013-03-08
                    " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL + " AS PBL ON "
                    + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
            
            
            
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApp.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApp.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strDraft = "";
            if (objSrcLeaveApp.isDraft()) {
                strDraft = strDraft + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 0";
            }
            
            String strToBeApprove = "";
            if (objSrcLeaveApp.isToBeApprove()) {
                strToBeApprove = strToBeApprove + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 1";                
            }            
            
            String strApproved = "";
            if (objSrcLeaveApp.isApproved()) {
                strApproved = strApproved + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 2";
            }
            
            String strExecuted = "";
            if (objSrcLeaveApp.isExecuted()) {
                strExecuted = strExecuted + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 3";
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApp.getApprovalStatus()) {
                
                case 0:
                    strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String strApprovalStatusHR = "";
            switch (objSrcLeaveApp.getApprovalHRMan()) {
                
                case 0:
                    strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            
            String whereClause = "";
            //whereClause = 
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            //update by satrya 2013-03-13
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                } else {
                    whereClause = whereClause + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();;
                }
            }
            
            
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strDraft != null && strDraft.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND (" + strDraft;
                } else {
                    whereClause = whereClause + "(" + strDraft;
                }
                status = true;
            }
            
            if (strToBeApprove != null && strToBeApprove.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    if (status == true) {
                        whereClause = whereClause + " OR " + strToBeApprove;
                    } else {
                        whereClause = whereClause + " AND (" + strToBeApprove;
                    }
                } else {
                    whereClause = whereClause + "(" + strToBeApprove;
                }
                status = true;
            }
            
            if (strApproved != null && strApproved.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    if (status == true) {
                        whereClause = whereClause + " OR " + strApproved;
                    } else {
                        whereClause = whereClause + " AND (" + strApproved;
                    }
                    status = true;
                } else {
                    whereClause = whereClause + "(" + strApproved;
                }
                status = true;
            }
            
            if (strExecuted != null && strExecuted.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    if (status == true) {
                        whereClause = whereClause + " OR " + strExecuted;
                    } else {
                        whereClause = whereClause + " AND (" + strExecuted;
                    }
                    status = true;
                } else {
                    whereClause = whereClause + "(" + strExecuted;
                }
                status = true;
            }
            if (status == true) {
                whereClause = whereClause + " ) ";
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    //whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    //whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    //whereClause = whereClause + " AND " + strApprovalStatusHR;
                } else {
                    //whereClause = whereClause + strApprovalStatusHR;
                }
            }
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0) ";
            }
            //sql = sql + " AND PBL."+ PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = "+objSrcLeaveApp.getTypePublicLeave();
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }



            //System.out.println("SQL Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                
                SpecialLeave objLeaveApplication = new SpecialLeave();
                LeaveApplication objleaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                Employee objEmployee = new Employee();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objleaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objleaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                
                vectTemp.add(objleaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    /**
     *
     * @param objSrcLeaveApp
     * @param start
     * @param recordToGet
     * @return
     */
    public static int searchCountLeaveApplication(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet) {
        DBResultSet dbrs = null;
        //Vector result = new Vector(1, 1);
        boolean status = false;
        int countLeaveApp = 0;
        if (objSrcLeaveApp == null) {
            return 0/*new Vector(1, 1)*/;
        }
        
        try {
            
            String sql = "SELECT COUNT(LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + ") AS COUNTHASIL FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " LA "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL + " AS PBL ON PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID] + "=LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + " AND " + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "!=" + PstLeaveApplication.FLD_STATUS_APPlICATION_CANCELED
                    //update by satrya 2013-11-15
                    + " AND " + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_TYPE_FROM_LEAVE] + "=" + objSrcLeaveApp.getTypeForm();
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            if (strFullName != null && strFullName.length() > 0) {
                sql = sql + " AND " + strFullName;
            }
            
            
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }
            
            if (objSrcLeaveApp.getCompanyId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + objSrcLeaveApp.getCompanyId();
            }
            
            if (objSrcLeaveApp.getDivisionId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + objSrcLeaveApp.getDivisionId();
            }
            
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            
            if (objSrcLeaveApp.getPositionId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            
            if (objSrcLeaveApp.getSectionId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            //update by satrya 2014-06-19
            if (objSrcLeaveApp.getEmployeeIdLeaveConfig() != null && objSrcLeaveApp.getEmployeeIdLeaveConfig().length() > 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " IN(" + objSrcLeaveApp.getEmployeeIdLeaveConfig() + ")";
            }
            
            
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    sql = sql + " AND LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strDraft = "";
            if (objSrcLeaveApp.isDraft()) {
                strDraft = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 0";
            }
            
            String strToBeApprove = "";
            if (objSrcLeaveApp.isToBeApprove()) {
                strToBeApprove = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 1";                
            }            
            
            String strApproved = "";
            if (objSrcLeaveApp.isApproved()) {
                strApproved = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 2";
            }
            
            String strExecuted = "";
            if (objSrcLeaveApp.isExecuted()) {
                strExecuted = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 3";
            }
            
            String sWhere = "";
            if (strDraft != null && strDraft.length() > 0) {
                sWhere = " AND (" + strDraft;
                status = true;
            }
            if (strToBeApprove != null && strToBeApprove.length() > 0) {                
                if (sWhere != null && sWhere.length() > 0) {
                    if (status == true) {
                        sWhere = sWhere + " OR " + strToBeApprove;
                    } else {
                        sWhere = sWhere + " AND (" + strToBeApprove;
                    }
                } else {
                    sWhere = sWhere + " AND (" + strToBeApprove;
                }
                status = true;
            }
            if (strApproved != null && strApproved.length() > 0) {                
                if (sWhere != null && sWhere.length() > 0) {
                    if (status == true) {
                        sWhere = sWhere + " OR " + strApproved;
                    } else {
                        sWhere = sWhere + " AND (" + strApproved;
                    }
                } else {
                    sWhere = sWhere + " AND (" + strApproved;
                }
                status = true;
            }
            if (strExecuted != null && strExecuted.length() > 0) {                
                if (sWhere != null && sWhere.length() > 0) {
                    if (status == true) {
                        sWhere = sWhere + " OR " + strExecuted;
                    } else {
                        sWhere = sWhere + " AND (" + strExecuted;
                    }
                } else {
                    sWhere = sWhere + " AND (" + strExecuted;
                }
                status = true;
            }
            if (status == true) {
                sWhere = sWhere + " ) ";
            }
            
            if (sWhere != null && sWhere.length() > 0) {
                sql = sql + sWhere;
            }
            /*
             switch (objSrcLeaveApp.getApprovalStatus()) { 

             case 0:
             sql = sql +" AND LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0"; 
             break;

             case 1:
             sql = sql + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
             break;

             default:
             break;
             }

          
             switch (objSrcLeaveApp.getApprovalHRMan()) {

             case 0:
             sql = sql +" LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
             break;

             case 1:
             sql = sql + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
             break;

             default:
             break;
             }*/
            
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {
                
                sql = sql + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                
            }
            if (objSrcLeaveApp.getSelectedFrom() != null && objSrcLeaveApp.getSelectedTo() != null) {
                sql = sql + " AND "
                        + " (LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  AL." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " FROM " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " AL WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= AL." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " AND AL." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\")"
                        + " OR LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  DP." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + " FROM " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DP WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= DP." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " AND DP." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + ">= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\")"
                        + " OR LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  LL." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " FROM " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LL WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= LL." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " AND LL." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE] + ">= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\")"
                        + " OR LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  SU." + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " FROM " + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN + " SU WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= SU." + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " AND SU." + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE] + ">= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\"))";
            }
            
            if ((/*update by satrya 2013-10-29 start == 0 &&*/recordToGet != 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }




            //System.out.println("SQL Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                countLeaveApp = rs.getInt("COUNTHASIL");
                /*Vector vectTemp = new Vector(1, 1);

                 SpecialLeave objLeaveApplication = new SpecialLeave();
                 LeaveApplication objleaveApplication = new LeaveApplication();
                 Department objDepartment = new Department();
                 Employee objEmployee = new Employee();

                 objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                 objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                 objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));

                 vectTemp.add(objEmployee);

                 objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                 objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                 objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                 objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                 objleaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                 objleaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));

                 vectTemp.add(objleaveApplication);

                 objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

                 vectTemp.add(objDepartment);

                 result.add(vectTemp);*/
            }
            return countLeaveApp;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return countLeaveApp;
    }

    /**
     *
     * @param objSrcLeaveApp
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector searchLeaveApplicationList(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        boolean status = false;
        if (objSrcLeaveApp == null) {
            return new Vector(1, 1);
        }
        
        try {
            
            String sql = "SELECT LA.*,EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " LA "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT ON DPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL + " AS PBL ON PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID] + "=LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + " AND " + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "!=" + PstLeaveApplication.FLD_STATUS_APPlICATION_CANCELED
                    //update by satrya 2013-11-15
                    + " AND " + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_TYPE_FROM_LEAVE] + "=" + objSrcLeaveApp.getTypeForm();
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            if (strFullName != null && strFullName.length() > 0) {
                sql = sql + " AND " + strFullName;
            }
            
            
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }
            
            if (objSrcLeaveApp.getCompanyId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + objSrcLeaveApp.getCompanyId();
            }
            
            if (objSrcLeaveApp.getDivisionId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + objSrcLeaveApp.getDivisionId();
            }
            
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            
            if (objSrcLeaveApp.getPositionId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            
            if (objSrcLeaveApp.getSectionId() != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }

            //update by satrya 2014-06-19
            if (objSrcLeaveApp.getEmployeeIdLeaveConfig() != null && objSrcLeaveApp.getEmployeeIdLeaveConfig().length() > 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " IN( " + objSrcLeaveApp.getEmployeeIdLeaveConfig() + ")";
            }
            
            
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    sql = sql + " AND LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strDraft = "";
            if (objSrcLeaveApp.isDraft()) {
                strDraft = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 0";
            }
            
            String strToBeApprove = "";
            if (objSrcLeaveApp.isToBeApprove()) {
                strToBeApprove = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 1";                
            }            
            
            String strApproved = "";
            if (objSrcLeaveApp.isApproved()) {
                strApproved = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 2";
            }
            
            String strExecuted = "";
            if (objSrcLeaveApp.isExecuted()) {
                strExecuted = " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 3";
            }
            
            String sWhere = "";
            if (strDraft != null && strDraft.length() > 0) {
                sWhere = " AND (" + strDraft;
                status = true;                
            }
            if (strToBeApprove != null && strToBeApprove.length() > 0) {                
                if (sWhere != null && sWhere.length() > 0) {
                    if (status == true) {
                        sWhere = sWhere + " OR " + strToBeApprove;
                    } else {
                        sWhere = sWhere + " AND (" + strToBeApprove;
                    }
                } else {
                    sWhere = sWhere + " AND (" + strToBeApprove;
                }
                status = true;
            }
            if (strApproved != null && strApproved.length() > 0) {                
                if (sWhere != null && sWhere.length() > 0) {
                    if (status == true) {
                        sWhere = sWhere + " OR " + strApproved;
                    } else {
                        sWhere = sWhere + " AND (" + strApproved;
                    }
                } else {
                    sWhere = sWhere + " AND (" + strApproved;
                }
                status = true;
            }
            if (strExecuted != null && strExecuted.length() > 0) {                
                if (sWhere != null && sWhere.length() > 0) {
                    if (status == true) {
                        sWhere = sWhere + " OR " + strExecuted;
                    } else {
                        sWhere = sWhere + " AND (" + strExecuted;
                    }
                } else {
                    sWhere = sWhere + " AND (" + strExecuted;
                }
                status = true;
            }
            if (status == true) {
                sWhere = sWhere + " ) ";
            }


            /*switch (objSrcLeaveApp.getApprovalStatus()) {

             case 0:
             sql = sql +" AND LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
             break;

             case 1:
             sql = sql + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
             break;

             default:
             break;
             }

          
             switch (objSrcLeaveApp.getApprovalHRMan()) {

             case 0:
             sql = sql +" LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
             break;

             case 1:
             sql = sql + " LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
             break;

             default:
             break;
             }*/            
            
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {                
                
                sql = sql + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                
            }
            if (sWhere != null && sWhere.length() > 0) {
                sql = sql + sWhere;
            }
            if (objSrcLeaveApp.getSelectedFrom() != null && objSrcLeaveApp.getSelectedTo() != null) {
                sql = sql + " AND "
                        + " (LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  AL." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " FROM " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " AL WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= AL." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " AND AL." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\")"
                        + " OR LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  DP." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + " FROM " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DP WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= DP." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " AND DP." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + ">= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\")"
                        + " OR LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  LL." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " FROM " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LL WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= LL." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " AND LL." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE] + ">= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\")"
                        + " OR LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " IN ( SELECT  SU." + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " FROM " + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN + " SU WHERE \"" + Formater.formatDate(objSrcLeaveApp.getSelectedTo(), "yyyy-MM-dd HH:mm:ss") + "\" >= SU." + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " AND SU." + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE] + ">= \"" + Formater.formatDate(objSrcLeaveApp.getSelectedFrom(), "yyyy-MM-dd HH:mm:ss") + "\"))";
            }
            
            
            if ((/*update by satrya 2013-10-29 start == 0 &&*/recordToGet != 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }




            //System.out.println("SQL Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {


                /* LeaveApplication objleaveApplication = new LeaveApplication();
               
                 objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                 objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                 objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                 objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                 objleaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                 objleaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));

                 result.add(objleaveApplication);*/
                Vector vectTemp = new Vector(1, 1);
                
                
                LeaveApplication objleaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                Employee objEmployee = new Employee();
                
                objEmployee.setOID(rs.getLong("EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString("EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString("EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objleaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objleaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                
                vectTemp.add(objleaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    
    public static Vector searchLeaveLLApplication(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        if (objSrcLeaveApp == null) {
            return new Vector(1, 1);
        }
        
        try {
            
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = "
                    + "APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID];
            
            
            
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApp.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApp.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApp.getApprovalStatus()) {
                
                case 0:
                    strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_APPROVE_BY_DEPT_HEAD] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_APPROVE_BY_DEPT_HEAD] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String strApprovalStatusHR = "";
            switch (objSrcLeaveApp.getApprovalHRMan()) {
                
                case 0:
                    strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_APPROVE_BY_HR_MANAGER] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_APPROVE_BY_HR_MANAGER] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }
            
            System.out.println("SQL Leave Application : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                
                SpecialLeave objLeaveApplication = new SpecialLeave();
                LeaveApplication objleaveApplication = new LeaveApplication();
                Employee objEmployee = new Employee();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                
                vectTemp.add(objleaveApplication);
                
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    /**
     * @param objSrcLeaveApplication
     * @return
     */
    public static int getCountSearchLeaveApplication(SrcLeaveApplication objSrcLeaveApplication) {
        DBResultSet dbrs = null;
        int result = 0;
        
        if (objSrcLeaveApplication == null) {
            return result;
        }
        
        try {
            String sql = " SELECT COUNT(LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + ")"
                    + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " = \"" + objSrcLeaveApplication.getEmpNum() + "\"";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strStatus = "";
            if (objSrcLeaveApplication.getStatus() > -1) {
                strStatus = strStatus + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                        + " = " + objSrcLeaveApplication.getStatus();
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case PstLeaveApplication.FLD_NOT_APPROVED:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_DEPT_HEAD:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case PstLeaveApplication.FLD_APPROVE_BY_HR_MANAGER:
                    strApprovalStatus = " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " > 0"
                            + " AND LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " > 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = " WHERE " + whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = " WHERE " + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = " WHERE " + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = " WHERE " + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = " WHERE " + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = " WHERE " + strSubmission;
                }
            }
            
            if (strStatus != null && strStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strStatus;
                } else {
                    whereClause = " WHERE " + strStatus;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = " WHERE " + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            } else {
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            System.out.println("SQL getCountSearchLeaveApplication : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
                break;
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on getCountSearchLeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * @param objSrcLeaveApplication
     * @return
     */
    public static int getCountSearchLeaveApplicationHR(SrcLeaveApplication objSrcLeaveApplication) {
        DBResultSet dbrs = null;
        int result = 0;
        
        if (objSrcLeaveApplication == null) {
            return result;
        }
        
        try {
            String sql = " SELECT COUNT(LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID] + ")"
                    + " FROM " + PstSpecialLeave.TBL_HR_SPECIAL_LEAVE + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " = \"" + objSrcLeaveApplication.getEmpNum() + "\"";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }

            /*String strStatus = "";
             if ( objSrcLeaveApplication.getStatus() > -1 ) 
             {                
             strStatus = strStatus + " LV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] +
             " = " + objSrcLeaveApplication.getStatus();                    
             }*/
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case 0:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = " WHERE " + whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = " WHERE " + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = " WHERE " + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = " WHERE " + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = " WHERE " + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = " WHERE " + strSubmission;
                }
            }

            /*if(strStatus!=null && strStatus.length()>0)
             {
             if(whereClause!=null && whereClause.length()>0)
             {
             whereClause = whereClause + " AND " + strStatus;
             }
             else
             {
             whereClause = " WHERE " + strStatus;
             }                
             }*/
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = " WHERE " + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            } else {
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            System.out.println("SQL getCountSearchLeaveApplication : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
                break;
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on getCountSearchLeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * @param objSrcLeaveApplication
     * @return
     */
    public static int getCountSearchLeaveApplicationHR(SrcLeaveApp objSrcLeaveApplication) {
        DBResultSet dbrs = null;
        int result = 0;
        
        if (objSrcLeaveApplication == null) {
            return result;
        }
        
        try {
            String sql = " SELECT COUNT(LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID] + ")"
                    + " FROM " + PstSpecialLeave.TBL_HR_SPECIAL_LEAVE + " AS LV "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "
                    + " ON LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApplication.getFullName() != null) && (objSrcLeaveApplication.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApplication.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApplication.getEmpNum() != null) && (objSrcLeaveApplication.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " = \"" + objSrcLeaveApplication.getEmpNum() + "\"";
            }
            
            String strDepartment = "";
            if (objSrcLeaveApplication.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApplication.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApplication.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApplication.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApplication.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApplication.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApplication.isSubmission()) {
                if ((objSrcLeaveApplication.getSubmissionDate() != null) && (objSrcLeaveApplication.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApplication.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApplication.getApprovalStatus()) {
                case 0:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " = 0";
                    break;
                
                case 1:
                    strApprovalStatus = " LV." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = " WHERE " + whereClause + strFullName;
            }
            
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = " WHERE " + strEmpNum;
                }
            }
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = " WHERE " + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = " WHERE " + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = " WHERE " + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = " WHERE " + strSubmission;
                }
            }
            
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = " WHERE " + strApprovalStatus;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            } else {
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = 0)";
            }
            
            System.out.println("SQL getCountSearchLeaveApplication : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = rs.getInt(1);
                break;
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on getCountSearchLeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    // ------------------------- END LEAVE MANAGEMENT -------------------------
    // ----------------------- SPECIAL LEAVE ------------------------ //
    /**
     * @desc mencari simbol-simbol special leave / annual leave yang muncul
     * dalam satu periode untuk seorang employee
     * @param oidPeriod
     * @param oidEmployee
     * @return Vector : daftar object ScheduleSymbol untuk special / annual
     * leave
     */
    public static Vector searchSpecialLeaveScheduleSymbols(long oidPeriod, long oidEmployee) {
        Vector listLeaves = new Vector(1, 1);
        Vector listSymbols = new Vector(1, 1);
        Vector listDates = new Vector(1, 1);
        
        DBResultSet dbrs = null;
        
        try {
            // daftar tanggal dalam 1 periode
            Vector periodDates = new Vector(1, 1);

            // buat object periode
            try {
                
                Period period = PstPeriod.fetchExc(oidPeriod);
                
                Date dateStart = period.getStartDate();
                Date dateEnd = period.getEndDate();

                // ambil daftar tanggal
                if (dateStart.getMonth() == dateEnd.getMonth()) // schedule pada bulan yang sama
                {

                    // isi list tanggal
                    for (int k = dateStart.getDate(); k <= dateEnd.getDate(); k++) {
                        periodDates.add(new Date(dateStart.getYear(), dateStart.getMonth(), k));
                    }
                    
                } else // schedule pada bulan yang berbeda
                {
                    Calendar start = new GregorianCalendar(dateStart.getYear() + 1900, dateStart.getMonth(), dateStart.getDate());
                    Calendar end = new GregorianCalendar(dateEnd.getYear() + 1900, dateEnd.getMonth(), dateEnd.getDate());
                    
                    Calendar date = (Calendar) start.clone();

                    // bulan I
                    for (int k = start.get(Calendar.DATE); k <= start.getActualMaximum(Calendar.DATE); k++) {
                        date.set(Calendar.DATE, k);
                        periodDates.add(date.getTime());
                    }
                    
                    Calendar date2 = (Calendar) end.clone();

                    // bulan II
                    for (int k = 1; k <= end.get(Calendar.DATE); k++) {
                        date2.set(Calendar.DATE, k);
                        periodDates.add(date2.getTime());
                    }
                }
                
            } catch (Exception e) {
                System.out.println("Error creating period object " + e.getMessage());
                e.printStackTrace();
            }


            // ambil schedule selama 1 periode
            String sql = " SELECT * FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE
                    + " WHERE " + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                    + " = " + oidEmployee
                    + " AND " + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                    + " = " + oidPeriod;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {

                // cek setiap hari dalam schedule
                for (int i = 1; i <= 31; i++) {
                    
                    long oidSymbol = rs.getLong("D" + i);   // ambil schedule symbol id

                    try {
                        if (oidSymbol != 0) {

                            // ambil object schedule symbol
                            ScheduleSymbol symbol = PstScheduleSymbol.fetchExc(oidSymbol);
                            
                            if (oidSymbol > 0) {

                                // ambil object schedule category
                                ScheduleCategory category = PstScheduleCategory.fetchExc(symbol.getScheduleCategoryId());

                                // jika kategori = special leave / annual leave, tambahkan ke vector
                                if (category.getCategoryType() == PstScheduleCategory.CATEGORY_SPECIAL_LEAVE || category.getCategoryType() == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                                    listSymbols.add(symbol);
                                    listDates.add(periodDates.elementAt(i - 1));
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                
            }
            
            rs.close();
            
            listLeaves.add(listSymbols);
            listLeaves.add(listDates);
            
            return listLeaves;
        } catch (Exception e) {
            System.out.println("Error fetching special leave schedule");
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return new Vector();
        
    }

    /**
     * @desc mengambil daftar oid dan nama schedule symbol yang memiliki
     * kategori special leave / annual leave
     * @return Vector : merupakan vector yang terdiri dari list oid, list
     * category, list nama symbol
     */
    public static Vector getDistinctSpecialLeave() {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;
        
        try {
            String sql = " SELECT DISTINCT SS." + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE]
                    + ", SS." + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]
                    + ", SC." + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE]
                    + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL + " SS "
                    + " INNER JOIN " + PstScheduleCategory.TBL_HR_SCHEDULE_CATEGORY + " SC "
                    + " ON SS." + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID]
                    + " = SC." + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_SCHEDULE_CATEGORY_ID]
                    + " WHERE SC." + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE]
                    + " = " + PstScheduleCategory.CATEGORY_SPECIAL_LEAVE
                    + " OR SC." + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE]
                    + " = " + PstScheduleCategory.CATEGORY_ANNUAL_LEAVE
                    + " ORDER BY SS." + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE];
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector ids = new Vector();      // list id symbol
            Vector names = new Vector();    // list nama symbol
            Vector cats = new Vector();     // list category symbol

            while (rs.next()) {
                ids.add(rs.getString(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]));
                names.add(rs.getString(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE]));
                cats.add(rs.getString(PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE]));
            }
            
            result.add(ids);
            result.add(names);
            result.add(cats);
            
            return result;
        } catch (Exception e) {
            System.out.println("Error getting special leave names");
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return new Vector();
    }

    /**
     * @desc menghitung jumlah hari yang dapat diambil untuk leave
     * @param oidEmployee
     * @param oidSchedule
     * @return int : jumlah hari
     */
    public static float countEligibleDay(long oidEmployee, long oidSchedule, int categoryType) {
        DBResultSet dbrs = null;
        Employee employee = new Employee();
        
        int maxEntitle = 0;
        int period = 0;
        int periodUnit = 0;
        int minLengthOfService = 0;
        
        try {
            // ambil prasyarat untuk schedule symbol ybs.
            String sql = " SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_MAX_ENTITLE]
                    + "," + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_PERIODE]
                    + "," + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_PERIODE_TYPE]
                    + "," + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_MIN_SERVICE]
                    + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL
                    + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]
                    + " = " + oidSchedule;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                maxEntitle = rs.getInt(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_MAX_ENTITLE]);
                period = rs.getInt(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_PERIODE]);
                periodUnit = rs.getInt(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_PERIODE_TYPE]);
                minLengthOfService = rs.getInt(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_MIN_SERVICE]);
            }
        } catch (Exception e) {
            System.out.println("Error getting eligible day");
        } finally {
            DBResultSet.close(dbrs);
        }
        
        try {
            employee = PstEmployee.fetchExc(oidEmployee);
            Date commencingDate = employee.getCommencingDate();
            
            if (commencingDate != null) {
                Calendar commencing = new GregorianCalendar(commencingDate.getYear() - 1900, commencingDate.getMonth(), commencingDate.getDate());

                // cek apakah commencing day memenuhi syarat dengan menambahkan 
                // length of service minimal untuk pengambilan leave
                Calendar calendar = (Calendar) commencing.clone();
                calendar.add(Calendar.MONTH, minLengthOfService);

                // hitung waktu kerja
                long lengthOfService = new Date().getTime() - calendar.getTime().getTime();
                
                if (lengthOfService < 0) // belum mempunyai hak mengajukan leave
                {
                    return 0;
                } else {
                    return getEligibleDay(maxEntitle, period, periodUnit, commencing, oidEmployee, oidSchedule, categoryType);
                }
            }
        } catch (Exception e) {
            employee = new Employee();
        }
        
        return 0;
    }

    /**
     * @desc menghitung jumlah hari yang dapat diambil untuk leave
     * @param maxEntitle
     * @param period
     * @param periodUnit
     * @param oidEmployee
     * @param oidSchedule
     * @return int : jumlah hari
     */
    private static float getEligibleDay(float maxEntitle, int period, int periodUnit, Calendar commencingDate, long oidEmployee, long oidSchedule, int categoryType) {
        DBResultSet dbrs = null;
        String where = "";
        float entitleResidue = 0;

        /* ambil sisa entitle */

        // jika annual leave
        if (categoryType == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
            try {
                String sql = " SELECT SUM(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ")"
                        + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT
                        + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                        + " = " + oidEmployee
                        + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS]
                        + " = " + PstAlStockManagement.AL_STS_AKTIF;
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                
                while (rs.next()) {
                    entitleResidue = rs.getFloat(1);
                }
                
                return entitleResidue;
            } catch (Exception e) {
                System.out.println("Failed getting eligible day");
            } finally {
                DBResultSet.close(dbrs);
            }
        } // jika special leave
        else if (categoryType == PstScheduleCategory.CATEGORY_SPECIAL_LEAVE) {

            // hitung waktu pengecekan taken berdasarkan period unit
            String timeLimitCondition = "";
            
            
            if (periodUnit == PstScheduleSymbol.PERIODE_TYPE_TIME_AT_ALL) {
                // tak ada kondisi waktu, cek seluruh record yang ada
                ;
            } else if (periodUnit == PstScheduleSymbol.PERIODE_TYPE_YEAR) {

                // ambil tanggal sekarang
                Calendar now = new GregorianCalendar();
                now.set(Calendar.HOUR, 0);
                now.set(Calendar.MINUTE, 0);
                now.set(Calendar.SECOND, 0);

                // ambil commencing date sebagai tanggal awal pengecekan
                Calendar periodCheckStart = (Calendar) commencingDate.clone();

                // tentukan awal pengecekan per periode tahun
                while (now.compareTo(periodCheckStart) >= 0) {
                    periodCheckStart.add(Calendar.YEAR, period);    // tambah sejumlah period th
                }
                
                if (now.compareTo(periodCheckStart) < 0) {
                    periodCheckStart.add(Calendar.YEAR, -period);   // jika melewati tahun sekarang
                }                                                    // kembalikan ke waktu sebelumnya

                timeLimitCondition = PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE]
                        + " >= " + Formater.formatDate(periodCheckStart.getTime(), "yyyy-MM-dd");
            } else if (periodUnit == PstScheduleSymbol.PERIODE_TYPE_MONTH) {

                // ambil tanggal sekarang
                Calendar now = new GregorianCalendar();
                now.set(Calendar.HOUR, 0);
                now.set(Calendar.MINUTE, 0);
                now.set(Calendar.SECOND, 0);

                // ambil commencing date sebagai tanggal awal pengecekan
                Calendar periodCheckStart = (Calendar) commencingDate.clone();

                // tentukan awal pengecekan per periode bulan
                while (now.compareTo(periodCheckStart) >= 0) {
                    periodCheckStart.add(Calendar.MONTH, period);    // tambah sejumlah period bln
                }
                
                if (now.compareTo(periodCheckStart) < 0) {
                    periodCheckStart.add(Calendar.MONTH, -period);
                }   // jika melewati bulan sekarang
                // kembalikan ke waktu sebelumnya

                timeLimitCondition = PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE]
                        + " >= " + Formater.formatDate(periodCheckStart.getTime(), "yyyy-MM-dd");
                
            } // end period type


            // cek leave stock
            try {
                String sql = " SELECT SUM(" + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_QTY] + ")"
                        + " FROM " + PstSpecialLeaveStock.TBL_HR_SPECIAL_LEAVE_STOCK
                        + " WHERE " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_EMPLOYEE_ID]
                        + " = " + oidEmployee
                        + " AND " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SYMBOL_ID]
                        + " = " + oidSchedule
                        + (timeLimitCondition.equals("") ? "" : " AND " + timeLimitCondition);
                
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                
                while (rs.next()) {
                    entitleResidue = maxEntitle - rs.getFloat(1);
                }
                
                if (entitleResidue < 0) {
                    entitleResidue = 0;
                }
            } catch (Exception e) {
                System.out.println("Error checking leave stock");
            }
            
            return entitleResidue;
            
        } // end leave category

        return 0;
    }

    /**
     * @desc menghitung jumlah hari yang dapat diambil untuk annual leave
     * @return int : jumlah hari
     */
    public static float countEligibleAL(long oidEmployee) {
        //update by satrya 2012-10-11
        // public static int countEligibleAL(long oidEmployee) {
        DBResultSet dbrs = null;
        float entitleResidue = 0;

        /* ambil sisa entitle */
        try {
            String sql = " SELECT SUM(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ")"
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT
                    + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " = " + oidEmployee
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS]
                    + " = " + PstAlStockManagement.AL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                entitleResidue = rs.getFloat(1);
            }
            
            return entitleResidue;
        } catch (Exception e) {
            System.out.println("Failed getting eligible day");
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0f;
    }

    /**
     * @desc mengecek apakah suatu leave telah ada pada leave stock dan
     * mengambil oid-nya
     * @param symbol schedule oid
     * @param employee oid
     * @param date of taken
     * @return long : leave stock oid
     */
    public static long getLeaveStock(long symbolId, long employeeId, Date takenDate) {
        DBResultSet dbrs = null;
        long result = 0;
        
        try {
            String sql = " SELECT " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SPECIAL_LEAVE_STOCK_ID]
                    + " FROM " + PstSpecialLeaveStock.TBL_HR_SPECIAL_LEAVE_STOCK
                    + " WHERE " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SYMBOL_ID]
                    + " = " + symbolId
                    + " AND " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_EMPLOYEE_ID]
                    + " = " + employeeId
                    + " AND " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE]
                    + " = '" + Formater.formatDate(takenDate, "yyyy-MM-dd") + "'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                result = rs.getLong(1);
            }
            
            return result;
        } catch (Exception e) {
            System.out.println("Error checking special leave stock");
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    public static long getLeaveManagement(long employeeId, Date takenDate) {
        DBResultSet dbrs = null;
        long result = 0;
        
        try {
            String sql = " SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT
                    + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employeeId
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + "= 0 AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + "= 1 AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OWNING_DATE] + "= '"
                    + Formater.formatDate(takenDate, "yyyy-MM-dd") + "' ";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                result = rs.getLong(1);
            }
            
            return result;
        } catch (Exception e) {
            System.out.println("Error checking annual leave management");
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /* ******************** */
    /*public static long getALStockID(long employeeId, Date takenDate) {
     DBResultSet dbrs = null;
     long result = 0;
     try {
     String sql = " SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] +
     " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT +
     " WHERE " +  PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] +
     " = " + employeeId +
     " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OWNING_DATE] +
     " <= '" + Formater.formatDate(takenDate, "yyyy-MM-dd") + "'" +
     " ORDER BY " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OWNING_DATE] + " DESC ";
     //System.out.println("Get Stock ID = " + sql);
     dbrs = DBHandler.execQueryResult(sql);
     ResultSet rs = dbrs.getResultSet();
     if(rs.next()) {
     result = rs.getLong(1);
     }
     return result;
     }
     catch(Exception e) {
     System.out.println("Error checking AL stock id");
     }
     finally {
     DBResultSet.close(dbrs);
     }
     return 0;
     }*/
    /*public static long getALTakenID(long employeeId, long stockId, Date takenDate) {
     DBResultSet dbrs = null;
     long result = 0;
     try {
     String sql = " SELECT " + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_TAKEN_ID] +
     " FROM " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN +
     " WHERE " +  PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_EMPLOYEE_ID] +
     " = " + employeeId +
     " AND " + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] +
     " = " + stockId + 
     " AND " + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] +
     " = '" + Formater.formatDate(takenDate, "yyyy-MM-dd") + "'";
     //System.out.println("Get Taken ID = " + sql);
     dbrs = DBHandler.execQueryResult(sql);
     ResultSet rs = dbrs.getResultSet();
     if(rs.next()) {
     result = rs.getLong(1);
     }
     return result;
     }
     catch(Exception e) {
     System.out.println("Error checking AL stock id");
     }
     finally {
     DBResultSet.close(dbrs);
     }
     return 0;
     }*/
    /* menyimpan AL yang direquest melalui leave request form */
    public static long saveRequestedAL(int iCommand, long employeeId, Date date) {
        /*long stockId = getALStockID(employeeId, takenDate);
         long takenId = 0;
         if(stockId != 0) {
         takenId = getALTakenID(employeeId, stockId, takenDate);
         }
         try {
         CtrlAlStockTaken ctrlStockTaken = new CtrlAlStockTaken();
         AlStockTaken stockTaken = new AlStockTaken();
         stockTaken.setAlStockId(stockId);
         stockTaken.setTakenDate(takenDate);
         stockTaken.setTakenQty(1);
         stockTaken.setPaidDate(takenDate);
         stockTaken.setOID(takenId);
         stockTaken.setEmployeeId(employeeId);
         ctrlStockTaken.action(iCommand, takenId, stockTaken);
         }
         catch(Exception e) {
         System.out.println("Error save taken AL " + e.getMessage());
         }
         return 0;
         */

        /*
         * simpan AL request ke Al management
         * dengan nilai qty = 0 dan qty_used = 1
         * setelah diproses taken, AL management akan di-expired-kan
         */
        
        try {
            String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " ="
                    + employeeId + " AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OWNING_DATE] + " = '"
                    + Formater.formatDate(date, "yyyy-MM-dd") + "' AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + " = 0 AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + " = 1 AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;
            
            Vector temp = PstAlStockManagement.list(0, 0, where, "");
            
            if (temp != null && temp.size() > 0) {
                return ((AlStockManagement) temp.firstElement()).getOID();
            } else {
                AlStockManagement AlManagement = new AlStockManagement();
                
                AlManagement.setDtOwningDate(date);
                AlManagement.setEmployeeId(employeeId);
                AlManagement.setEntitled(0);
                AlManagement.setAlQty(0);
                AlManagement.setQtyUsed(1);
                AlManagement.setQtyResidue(0);
                
                long res = PstAlStockManagement.insertExc(AlManagement);
                
                return res;
            }
        } catch (Exception e) {
            System.out.println("Error save taken AL " + e.getMessage());
        }
        
        return 0;
    }

    /* ******************** */
    public static void parserDate(Calendar date, int iCommand, long symbolId, long oidEmployee, SpecialLeaveStock leaveStock,
            CtrlSpecialLeaveStock ctrlSpecialLeaveStock, long categId) {
        
        Calendar d = (Calendar) date.clone();
        
        for (int i = 1; i <= date.getActualMaximum(Calendar.DATE); i++) {
            d.set(Calendar.DATE, i);
            
            long oidStock = getLeaveStock(symbolId, oidEmployee, d.getTime());
            leaveStock.setOID(oidStock);
            leaveStock.setTakenDate(d.getTime());
            
            ctrlSpecialLeaveStock.action(iCommand, oidStock, leaveStock);
            
            if (categId == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                saveRequestedAL(iCommand, oidEmployee, d.getTime());
            }
        }
    }
    
    public static void saveLeaveStock(Date dateStart, Date dateEnd, SpecialLeaveStock leaveStock, int iCommand, long symbolId, long oidEmployee, long categId) {
        CtrlSpecialLeaveStock ctrlSpecialLeaveStock = new CtrlSpecialLeaveStock();
        
        if (dateStart.equals(dateEnd)) {
            // simpan stock                            
            long oidStock = getLeaveStock(symbolId, oidEmployee, dateStart);
            leaveStock.setOID(oidStock);
            leaveStock.setTakenDate(dateStart);
            
            ctrlSpecialLeaveStock.action(iCommand, oidStock, leaveStock);
            
            if (categId == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                saveRequestedAL(iCommand, oidEmployee, dateStart);
            }
        } else {
            // simpan stock
            Calendar startDate = new GregorianCalendar(dateStart.getYear() + 1900, dateStart.getMonth(), dateStart.getDate());
            Calendar endDate = new GregorianCalendar(dateEnd.getYear() + 1900, dateEnd.getMonth(), dateEnd.getDate());

            // jika range tgl lebih dari 1 bulan
            if (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH) > 1) {
                Calendar s = (Calendar) startDate.clone();
                Calendar e = (Calendar) endDate.clone();

                // proses bulan I
                for (int k = startDate.get(Calendar.DATE); k <= startDate.getActualMaximum(Calendar.DATE); k++) {

                    // simpan stock
                    s.set(Calendar.DATE, k);
                    
                    long oidStock = getLeaveStock(symbolId, oidEmployee, s.getTime());
                    leaveStock.setOID(oidStock);
                    leaveStock.setTakenDate(s.getTime());
                    
                    ctrlSpecialLeaveStock.action(iCommand, oidStock, leaveStock);
                    
                    if (categId == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                        saveRequestedAL(iCommand, oidEmployee, s.getTime());
                    }
                }

                // proses bulan berikutnya
                for (int k = startDate.get(Calendar.MONTH) + 1; k < endDate.get(Calendar.MONTH); k++) {

                    // simpan stock
                    s.set(Calendar.DATE, 1);
                    s.set(Calendar.MONTH, k);
                    
                    parserDate(s, iCommand, symbolId, oidEmployee, leaveStock, ctrlSpecialLeaveStock, categId);
                }


                // proses bulan terakhir
                for (int k = 1; k <= endDate.get(Calendar.DATE); k++) {

                    // simpan stock
                    e.set(Calendar.DATE, k);
                    
                    long oidStock = getLeaveStock(symbolId, oidEmployee, e.getTime());
                    leaveStock.setOID(oidStock);
                    leaveStock.setTakenDate(e.getTime());
                    
                    ctrlSpecialLeaveStock.action(iCommand, oidStock, leaveStock);
                    
                    if (categId == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                        saveRequestedAL(iCommand, oidEmployee, e.getTime());
                    }
                }
                
                return;
            }

            // jika range tgl berjarak 1 bulan
            if (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH) == 1) {
                Calendar s = (Calendar) startDate.clone();
                Calendar e = (Calendar) endDate.clone();

                // proses bulan I
                for (int k = startDate.get(Calendar.DATE); k <= startDate.getActualMaximum(Calendar.DATE); k++) {

                    // simpan stock
                    s.set(Calendar.DATE, k);
                    
                    long oidStock = getLeaveStock(symbolId, oidEmployee, s.getTime());
                    leaveStock.setOID(oidStock);
                    leaveStock.setTakenDate(s.getTime());
                    
                    ctrlSpecialLeaveStock.action(iCommand, oidStock, leaveStock);
                    
                    if (categId == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                        saveRequestedAL(iCommand, oidEmployee, s.getTime());
                    }
                }

                // proses bulan II
                for (int k = 1; k <= endDate.get(Calendar.DATE); k++) {

                    // simpan stock
                    e.set(Calendar.DATE, k);
                    
                    long oidStock = getLeaveStock(symbolId, oidEmployee, e.getTime());
                    leaveStock.setOID(oidStock);
                    leaveStock.setTakenDate(e.getTime());
                    
                    ctrlSpecialLeaveStock.action(iCommand, oidStock, leaveStock);
                    
                    if (categId == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                        saveRequestedAL(iCommand, oidEmployee, e.getTime());
                    }
                }
                
                return;
            }

            // jika range tgl kurang dari 1 bulan
            if (endDate.get(Calendar.MONTH) == startDate.get(Calendar.MONTH)) {
                Calendar d = (Calendar) startDate.clone();

                // proses 
                for (int k = startDate.get(Calendar.DATE); k <= endDate.get(Calendar.DATE); k++) {

                    // simpan stock
                    d.set(Calendar.DATE, k);
                    
                    long oidStock = getLeaveStock(symbolId, oidEmployee, d.getTime());
                    leaveStock.setOID(oidStock);
                    leaveStock.setTakenDate(d.getTime());
                    
                    ctrlSpecialLeaveStock.action(iCommand, oidStock, leaveStock);
                    
                    if (categId == PstScheduleCategory.CATEGORY_ANNUAL_LEAVE) {
                        saveRequestedAL(iCommand, oidEmployee, d.getTime());
                    }
                }
            }
            
        } // end date check
    }
    
    public static Vector getSpecialLeaveDetail(long empId, Date takenMaxDate) {
        Vector lists = new Vector(1, 1);
        DBResultSet dbrs = null;
        
        try {
            String query = "SELECT DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SPECIAL_LEAVE_STOCK_ID] + " ,DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SPECIAL_LEAVE_ID] + " ,DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_EMPLOYEE_ID] + " ,DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SYMBOL_ID] + " ,DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE] + " ,DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_QTY] + " ,DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_LEAVE_STATUS] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_UNPAID_REASON] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REMARK] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_ID] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_ID] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_DATE] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_DATE] + " ,MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_DATE] + " FROM " + PstSpecialLeaveStock.TBL_HR_SPECIAL_LEAVE_STOCK + " AS DETAIL " + " INNER JOIN " + PstSpecialLeave.TBL_HR_SPECIAL_LEAVE + " AS MAIN" + " ON DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SPECIAL_LEAVE_ID] + " = MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID] + " WHERE DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(takenMaxDate, "yyyy-MM-dd") + "'" + " AND MAIN." + PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID] + " = " + empId + " AND DETAIL." + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_LEAVE_STATUS] + " = " + PstSpecialLeaveStock.STATUS_NOT_PROCESSED;
            
            dbrs = DBHandler.execQueryResult(query);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                SpecialLeaveStock leaveDetail = new SpecialLeaveStock();
                
                leaveDetail.setOID(rs.getLong(PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SPECIAL_LEAVE_STOCK_ID]));
                leaveDetail.setSpecialLeaveId(rs.getLong(PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SPECIAL_LEAVE_ID]));
                leaveDetail.setLeaveStatus(rs.getInt(PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_LEAVE_STATUS]));
                leaveDetail.setSymbolId(rs.getLong(PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SYMBOL_ID]));
                leaveDetail.setEmployeeId(rs.getLong(PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_EMPLOYEE_ID]));
                leaveDetail.setTakenDate(rs.getDate(PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE]));
                leaveDetail.setTakenQty(rs.getInt(PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_QTY]));
                
                SpecialLeave leave = new SpecialLeave();
                
                leave.setOID(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_SPECIAL_LEAVE_ID]));
                leave.setEmployeeId(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_EMPLOYEE_ID]));
                leave.setRequestDate(rs.getDate(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REQUEST_DATE]));
                leave.setUnpaidLeaveReason(rs.getString(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_UNPAID_REASON]));
                leave.setOtherRemarks(rs.getString(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_REMARK]));
                leave.setApprovalId(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_ID]));
                leave.setApproval2Id(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_ID]));
                leave.setApproval3Id(rs.getLong(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_ID]));
                leave.setApprovalDate(rs.getDate(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL_DATE]));
                leave.setApproval2Date(rs.getDate(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL2_DATE]));
                leave.setApproval3Date(rs.getDate(PstSpecialLeave.fieldNames[PstSpecialLeave.FLD_APPROVAL3_DATE]));
                
                Vector vTemp = new Vector(1, 1);
                vTemp.add(leave);
                vTemp.add(leaveDetail);
                
                lists.add(vTemp);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return new Vector(1, 1);
    }
    
    public static String getEmployeeApp(long empId) {
        
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = '" + empId + "'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                
                String name = rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]);
                return name;
                
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    /* mengambil daftar annual leave management berdasarkan id employee & tgl parameter */
    public static Vector getAnnualLeave(long employeeId, Date date) {
        Vector list = new Vector();
        DBResultSet dbrs = null;
        
        try {
            String query = " SELECT * FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT
                    + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " = " + employeeId
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OWNING_DATE]
                    + " = '" + Formater.formatDate(date, "yyyy-MM-dd") + "'"
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + "= 1"
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + "= 0"
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS]
                    + " = " + PstAlStockManagement.AL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(query);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                AlStockManagement alStock = new AlStockManagement();
                
                PstAlStockManagement.resultToObject(rs, alStock);
                
                list.add(alStock);
            }
            rs.close();
            
            return list;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return new Vector(1, 1);
    }

    // mengambil daftar tanggal2 pengambilan untuk suatu special leave
    public static Vector getSpecialLeaveDetailDate(long oidLeave, long oidEmployee, long oidSymbol) {
        String where = PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SPECIAL_LEAVE_ID] + "="
                + oidLeave + " AND " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_EMPLOYEE_ID] + "="
                + oidEmployee + " AND " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_SYMBOL_ID] + "="
                + oidSymbol;
        
        String order = PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE];
        
        Vector listDetail = PstSpecialLeaveStock.list(0, 0, where, order);
        Vector result = getStartEndRange(listDetail);
        
        return result;
    }
    
    private static Vector getStartEndRange(Vector listDetail) {
        
        Vector vctList = new Vector();
        
        if (listDetail != null && listDetail.size() > 0) {
            // tampilkan daftar hari
            Vector adjacentDate = new Vector(); // penampung sementara untuk tgl yang berdampingan

            // cek tiap tanggal yang direquest
            for (int x = 0; x < listDetail.size(); x++) {
                SpecialLeaveStock leaveStock = (SpecialLeaveStock) listDetail.get(x);

                // ambil tanggal untuk iterasi sekarang
                Date currentDate = leaveStock.getTakenDate();
                
                
                if (x == 0) {

                    // cek jumlah tanggal yang direquest
                    if (listDetail.size() > 1) {

                        // jika > 1, masukkan tanggal pertama ke vector
                        adjacentDate.add(currentDate);
                        continue;
                    } else {

                        // jika 1, langsung simpan
                        Vector vctTemp = new Vector();
                        vctTemp.add("" + currentDate.getTime());
                        vctTemp.add("" + currentDate.getTime());
                        vctTemp.add("1");
                        vctList.add(vctTemp);
                    }
                } else {
                    // cek apakah tgl berdampingan
                    Date lastDate = (Date) adjacentDate.lastElement();   // tgl terakhir di penampung

                    // jika berjarak 24 jam, berarti tanggal berdampingan
                    if (currentDate.getTime() - lastDate.getTime() <= (24 * 60 * 60 * 1000)) {

                        // tambah ke penampung
                        adjacentDate.add(currentDate);

                        // cek apakah ada pengulangan selanjutnya, jika tidak, output data
                        if (x == listDetail.size() - 1) // iterasi terakhir
                        {

                            // ambil range bawah dan range atas
                            sort(adjacentDate);
                            Date startRange = (Date) adjacentDate.firstElement();
                            Date endRange = (Date) adjacentDate.lastElement();
                            
                            Vector vctTemp = new Vector();
                            vctTemp.add("" + startRange.getTime());
                            vctTemp.add("" + endRange.getTime());
                            long totalDay = 1 + (endRange.getTime() - startRange.getTime()) / (24 * 60 * 60 * 1000);
                            vctTemp.add("" + totalDay);
                            vctList.add(vctTemp);
                        } else {
                            continue;
                        }
                    } else {

                        // ambil range bawah dan range atas
                        sort(adjacentDate);
                        Date startRange = (Date) adjacentDate.firstElement();
                        Date endRange = (Date) adjacentDate.lastElement();

                        // buat output tanggal sebelumnya

                        if (startRange.compareTo(endRange) == 0) // hanya 1 hari
                        {
                            Vector vctTemp = new Vector();
                            vctTemp.add("" + startRange.getTime());
                            vctTemp.add("" + startRange.getTime());
                            vctTemp.add("1");
                            vctList.add(vctTemp);
                        } else {
                            Vector vctTemp = new Vector();
                            vctTemp.add("" + startRange.getTime());
                            vctTemp.add("" + endRange.getTime());
                            long totalDay = 1 + (endRange.getTime() - startRange.getTime()) / (24 * 60 * 60 * 1000);
                            vctTemp.add("" + totalDay);
                            vctList.add(vctTemp);
                            
                        }

                        // kosongkan penampung
                        adjacentDate.clear();

                        // tambah tanggal terakhir ke penampung
                        adjacentDate.add(currentDate);


                        // cek apakah ada pengulangan selanjutnya, jika tidak, output data
                        if (x == listDetail.size() - 1) // iterasi terakhir
                        {

                            // ambil range bawah dan range atas
                            startRange = (Date) adjacentDate.firstElement();
                            
                            Vector vctTemp = new Vector();
                            vctTemp.add("" + startRange.getTime());
                            vctTemp.add("" + startRange.getTime());
                            vctTemp.add("1");
                            vctList.add(vctTemp);
                            
                        } else {
                            continue;
                        }
                        
                    } // end check adjacent date

                } // end check iteration index

            }
            
        }
        
        return vctList;
        
    }
    
    private static void sort(Vector dates) {
        if (dates != null && dates.size() > 0) {
            
            for (int i = 0; i < dates.size() - 1; i++) {
                int idx = i;
                Date min = (Date) dates.get(i);
                
                for (int j = i + 1; j < dates.size(); j++) {
                    Date curr = (Date) dates.get(j);
                    
                    if (curr.getTime() < min.getTime()) {
                        idx = j;
                        min = curr;
                    }
                    
                }
                
                Date tmp = null;
                
                tmp = (Date) dates.get(i);
                dates.setElementAt(dates.get(idx), i);
                dates.setElementAt(tmp, idx);
            }
            
        }
    }

    /*public static boolean processTakenSpecialLeave(long employeeId, Date date) {
     int result = 0;
     try {
     String sql = " UPDATE " + PstSpecialLeaveStock.TBL_HR_SPECIAL_LEAVE_STOCK +
     " SET " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_LEAVE_STATUS] +
     " = " + PstSpecialLeaveStock.STATUS_PROCESSED +
     " WHERE " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_EMPLOYEE_ID] +
     " = " + employeeId + 
     " AND " + PstSpecialLeaveStock.fieldNames[PstSpecialLeaveStock.FLD_TAKEN_DATE] +
     " = '" + Formater.formatDate(date, "yyyy-MM-dd") + "'";
     result = DBHandler.execUpdate(sql);
     if(result > 0)
     return true;
     }
     catch(Exception e) {
     System.out.println("Failed updating special leave status!");
     }
     return false;
     }*/
    public static String typeLeave(long LeaveID) {
        DBResultSet dbrs = null;
        try {
            String sqlAL = "SELECT " + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_TAKEN_ID]
                    + " FROM " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN
                    + " WHERE LEAVE_APPLICATION_ID = " + LeaveID;
            String sqlLL = "SELECT " + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_TAKEN_ID]
                    + " FROM " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN
                    + " WHERE LEAVE_APPLICATION_ID = " + LeaveID;
            String sqlDP = "SELECT " + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_TAKEN_ID]
                    + " FROM " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN
                    + " WHERE LEAVE_APPLICATION_ID = " + LeaveID;
            
            boolean typeAl = getStatus(sqlAL);
            boolean typeLl = getStatus(sqlLL);
            boolean typeDp = getStatus(sqlDP);
            
            if (typeAl == true) {
                return "Annual Leave";
            } else if (typeLl == true) {
                return "Long Leave";
            } else if (typeDp == true) {
                return "Day of Payment";
            }
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        
        return null;
    }
    
    public static boolean getStatus(String sql) {
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                return true;
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return false;
    }
    
    public static float countAL(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ") FROM " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " WHERE "
                    + /*PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_EMPLOYEE_ID] + " = '" + EmpId + "' AND " + */ PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = '" + AppId + "'";
            dbrs = DBHandler.execQueryResult(sql);
            //System.out.println("SQL SUM AL " + sql);
            ResultSet rs = dbrs.getResultSet();
            float jumlah = 0;
            while (rs.next()) {
                jumlah = rs.getFloat(1);
            }
            rs.close();
            
            return jumlah;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return 0;
    }
    
    public static String tknAL(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        String tkn = "";
        try {
            String sql = "SELECT " + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + "," + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE] + " FROM " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " WHERE "
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_EMPLOYEE_ID] + " = '" + EmpId + "' AND "
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = '" + AppId + "'";
            dbrs = DBHandler.execQueryResult(sql);
            
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                tkn = tkn + "(";
                tkn = tkn + Formater.formatDate(rs.getDate(PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE]), "yyyy-MM-dd");
                tkn = tkn + "," + Formater.formatDate(rs.getDate(PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]), "yyyy-MM-dd") + ") ";
                tkn = tkn + "<BR>";
                
            }
            rs.close();
            if (tkn.equals("")) {
                return "-";
            }
            return tkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return "-";
    }
    
    public static String tknLL(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        String tkn = "";
        try {
            String sql = "SELECT " + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + "," + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE] + " FROM " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " WHERE "
                    + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_EMPLOYEE_ID] + " = '" + EmpId + "' AND "
                    + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = '" + AppId + "'";
            dbrs = DBHandler.execQueryResult(sql);
            
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                tkn = tkn + "(";
                tkn = tkn + Formater.formatDate(rs.getDate(PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE]), "yyyy-MM-dd");
                tkn = tkn + "," + Formater.formatDate(rs.getDate(PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]), "yyyy-MM-dd") + ") ";
                tkn = tkn + "<BR>";
                
            }
            rs.close();
            if (tkn.equals("")) {
                return "-";
            }
            return tkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return "-";
    }
    
    public static String tknDP(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        String tkn = "";
        try {
            String sql = "SELECT " + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + "," + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + " FROM " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " WHERE "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_EMPLOYEE_ID] + " = '" + EmpId + "' AND "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + " = '" + AppId + "'";
            dbrs = DBHandler.execQueryResult(sql);
            
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                tkn = tkn + "(";
                tkn = tkn + Formater.formatDate(rs.getDate(PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE]), "yyyy-MM-dd") + ") ";
                tkn = tkn + "<BR>";
                
            }
            rs.close();
            if (tkn.equals("")) {
                return "-";
            }
            return tkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return "-";
    }
    
    public static float countLL(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT SUM(" + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ") FROM " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " WHERE "
                    + /*PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_EMPLOYEE_ID] + " = '" + EmpId + "' AND " +*/ PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = '" + AppId + "'";

            //System.out.println("SQL SUM LL " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float jumlah = 0;
            while (rs.next()) {
                jumlah = rs.getFloat(1);
            }
            rs.close();
            return jumlah;
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return 0;
    }
    
    public static float countDP(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COALESCE(SUM(" + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + "),0) FROM " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " WHERE "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_EMPLOYEE_ID] + " = '" + EmpId + "' AND "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + " = '" + AppId + "'";
            
            dbrs = DBHandler.execQueryResult(sql);
            System.out.println("SQL SUM DP " + sql);
            ResultSet rs = dbrs.getResultSet();
            float jumlah = 0;
            while (rs.next()) {
                jumlah = rs.getFloat(1);
            }
            rs.close();
            
            return jumlah;
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return 0.0f;
    }
	
	public static float countSS(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COALESCE(SUM(" + PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_TAKEN_QTY] + "),0) FROM " + PstSpecialStockTaken.TBL_SPECIAL_STOCK_TAKEN + " WHERE "
                    + PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_EMPLOYEE_ID] + " = '" + EmpId + "' AND "
                    + PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_LEAVE_APPLICATION_ID] + " = '" + AppId + "'";
            System.out.println("Masuk Sini");
            dbrs = DBHandler.execQueryResult(sql);
            System.out.println("SQL SUM DP " + sql);
            ResultSet rs = dbrs.getResultSet();
            float jumlah = 0;
            while (rs.next()) {
                jumlah = rs.getFloat(1);
            }
            rs.close();
            
            return jumlah;
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return 0.0f;
    }
    
    public static Vector getVectorSpecialLeave() {
        String oidSpecialLeave = String.valueOf(PstSystemProperty.getValueByName("OID_SPECIAL"));
        DBResultSet dbrs = null;
        
        Vector resultSpecialLeave = new Vector();
        
        try {
            String sql = "SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " FROM "
                    + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID]
                    + " = " + oidSpecialLeave;
            dbrs = DBHandler.execQueryResult(sql);

            //System.out.println("SQL get Special Leave " + sql);
            ResultSet rs = dbrs.getResultSet();
            long scheduleOid = 0;
            while (rs.next()) {
                scheduleOid = rs.getLong(1);
                resultSpecialLeave.add("" + scheduleOid);
            }
            rs.close();
            return resultSpecialLeave;
            
        } catch (Exception e) {
            
        }
        return null;
    }

    /**
     * Create by satrya 2013-04-11
     *
     * @param oidSpecialLeave
     * @return
     */
    public static Vector getVectorSpecialLeave(long oidSpecialLeave) {
        
        DBResultSet dbrs = null;
        
        Vector resultSpecialLeave = new Vector();
        
        try {
            String sql = "SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " FROM "
                    + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID]
                    + " = " + oidSpecialLeave;
            dbrs = DBHandler.execQueryResult(sql);

            //System.out.println("SQL get Special Leave " + sql);
            ResultSet rs = dbrs.getResultSet();
            long scheduleOid = 0;
            while (rs.next()) {
                scheduleOid = rs.getLong(1);
                resultSpecialLeave.add("" + scheduleOid);
            }
            rs.close();
            return resultSpecialLeave;
            
        } catch (Exception e) {
            
        }
        return null;
    }
    
    public static Vector getVectorUnpaidLeave() {
        String oidUnpaidLeave = String.valueOf(PstSystemProperty.getValueByName("OID_UNPAID"));
        DBResultSet dbrs = null;
        
        Vector resultSpecialLeave = new Vector();
        
        try {
            String sql = "SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " FROM "
                    + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID]
                    + " = " + oidUnpaidLeave;
            dbrs = DBHandler.execQueryResult(sql);
            
            System.out.println("SQL get Unpaid Leave " + sql);
            ResultSet rs = dbrs.getResultSet();
            long scheduleOid = 0;
            while (rs.next()) {
                scheduleOid = rs.getLong(1);
                resultSpecialLeave.add("" + scheduleOid);
            }
            rs.close();
            return resultSpecialLeave;
            
        } catch (Exception e) {
            
        }
        return null;
    }
    
    public static Vector vectorgetData(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        Vector scheduleId = new Vector();
        try {
            String sql = "SELECT " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID]
                    + " FROM " + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN
                    + " WHERE " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID]
                    + " = " + EmpId
                    + " AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID]
                    + " = " + AppId;
            System.out.println("SQL get scheduled " + sql);
            ResultSet rs = dbrs.getResultSet();
            long scheduleOid = 0;
            while (rs.next()) {
                scheduleOid = rs.getLong(1);
                scheduleId.add("" + scheduleOid);
            }
            rs.close();
            return scheduleId;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        
        return null;
    }
    
    public static float countSpecialLeave(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        Vector oidSchedule = new Vector();
        //di hidden sementara 20130607
        //karna berpengaruh di leave_app_list.jsp jumlahnya tidak muncul special leavenya
        //String oidSpecialLeave = String.valueOf(PstSystemProperty.getValueByName("OID_EXCUSE_SCHEDULE_CATEGORY"));
        //long oidSpecialLeaveX=0;
        /*if(oidSpecialLeave!=null && oidSpecialLeave.length()>0){
         oidSpecialLeaveX = Long.parseLong(oidSpecialLeave);
         }
         oidSchedule = getVectorSpecialLeave(oidSpecialLeaveX);

         if (oidSchedule == null || oidSchedule.size() <= 0) {
         return 0;
         }*/
        
        try {
            //   String sql = "SELECT COUNT(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ") FROM " +
            //update by satrya 2012-10-23
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN
                    + " WHERE " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID]
                    + " = " + AppId /*+" AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] +
                     " = " + EmpId*/;
            
            if (oidSchedule.size() > 0) {
                sql = sql + " AND ( ";
                for (int index = 0; index < oidSchedule.size(); index++) {
                    
                    sql = sql + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    int max = oidSchedule.size() - 1;
                    if (index != max) {
                        sql = sql + " OR ";
                    }
                    
                }
                sql = sql + " ) ";
            }
            dbrs = DBHandler.execQueryResult(sql);
            //System.out.println("SQL SUM Special Leave " + sql);
            ResultSet rs = dbrs.getResultSet();
            float jumlah = 0;
            while (rs.next()) {
                jumlah = rs.getFloat(1);
            }
            rs.close();
            
            return jumlah;
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return 0;
    }
    
    public static String TknSP(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        Vector oidSchedule = new Vector();
        oidSchedule = getVectorSpecialLeave();
        String tkn = "";
        
        if (oidSchedule == null || oidSchedule.size() <= 0) {
            return "-";
        }
        
        try {
            String sql = "SELECT " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE] + " FROM "
                    + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN
                    + " WHERE " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID]
                    + " = " + AppId
                    + " AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID]
                    + " = " + EmpId;
            
            if (oidSchedule.size() > 0) {
                sql = sql + " AND ( ";
                for (int index = 0; index < oidSchedule.size(); index++) {
                    
                    sql = sql + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    int max = oidSchedule.size() - 1;
                    if (index != max) {
                        sql = sql + " OR ";
                    }
                    
                }
                sql = sql + " ) ";
            }
            dbrs = DBHandler.execQueryResult(sql);
            
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                tkn = tkn + "(";
                tkn = tkn + Formater.formatDate(rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]), "yyyy-MM-dd");
                tkn = tkn + "," + Formater.formatDate(rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]), "yyyy-MM-dd") + ") ";
                
            }
            rs.close();
            if (tkn.equals("")) {
                return "-";
            }
            return tkn;
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return "-";
    }
    
    public static float countUnpaidLeave(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        Vector oidSchedule = new Vector();
        oidSchedule = getVectorUnpaidLeave();
        
        if (oidSchedule == null || oidSchedule.size() <= 0) {
            return 0;
        }
        
        try {
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN
                    + " WHERE " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID]
                    + " = " + AppId /*+" AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] +
                     " = " + EmpId*/;
            
            if (oidSchedule.size() > 0) {
                sql = sql + " AND ( ";
                for (int index = 0; index < oidSchedule.size(); index++) {
                    
                    sql = sql + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    int max = oidSchedule.size() - 1;
                    
                    if (index != max) {
                        sql = sql + " OR ";
                    }
                    
                }
                sql = sql + " ) ";
            }
            dbrs = DBHandler.execQueryResult(sql);
            //System.out.println("SQL SUM Special Leave " + sql);
            ResultSet rs = dbrs.getResultSet();
            float jumlah = 0;
            while (rs.next()) {
                jumlah = rs.getFloat(1);
            }
            rs.close();
            
            return jumlah;
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return 0;
    }
    
    public static String tknUnpLeave(long AppId, long EmpId) {
        DBResultSet dbrs = null;
        Vector oidSchedule = new Vector();
        oidSchedule = getVectorUnpaidLeave();
        
        String tkn = "";
        
        if (oidSchedule == null || oidSchedule.size() <= 0) {
            return "-";
        }
        
        try {
            String sql = "SELECT " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE] + " FROM "
                    + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN
                    + " WHERE " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID]
                    + " = " + AppId
                    + " AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID]
                    + " = " + EmpId;
            
            if (oidSchedule.size() > 0) {
                sql = sql + " AND ( ";
                for (int index = 0; index < oidSchedule.size(); index++) {
                    
                    sql = sql + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    int max = oidSchedule.size() - 1;
                    
                    if (index != max) {
                        sql = sql + " OR ";
                    }
                    
                }
                sql = sql + " ) ";
            }
            dbrs = DBHandler.execQueryResult(sql);
            System.out.println("SQL SUM Special Leave " + sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                tkn = tkn + "(";
                tkn = tkn + Formater.formatDate(rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]), "yyyy-MM-dd");
                tkn = tkn + "," + Formater.formatDate(rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]), "yyyy-MM-dd") + ") ";
                
            }
            rs.close();
            
            if (tkn.equals("")) {
                return "-";
            }
            return tkn;
        } catch (Exception e) {
            System.out.println("EXCEPTION :: " + e.toString());
        }
        return "-";
    }
    
    public static String getStatusDocument(int FLD_STATUS) {
        
        String docStatus = "";
        
        try {
            docStatus = PstLeaveApplication.fieldStatusApplication[FLD_STATUS];
            return docStatus;
        } catch (Exception e) {
            docStatus = "";
            return null;
        }
    }
    
    public static int LeaveStatus(long scheduleId) {
        try {
            String where = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " = " + scheduleId;
            Vector vectSchedule = PstScheduleSymbol.list(0, 0, where, null);
            String oidSpecialLeave = String.valueOf(PstSystemProperty.getValueByName("OID_SPECIAL"));
            String oidUnpaidLeave = String.valueOf(PstSystemProperty.getValueByName("OID_UNPAID"));
            long Special = 0;
            long Unpaid = 0;
            int result = 0;
            
            if (oidSpecialLeave != null || oidUnpaidLeave != null) {
                
                ScheduleSymbol schedulesymbol = (ScheduleSymbol) vectSchedule.get(0);
                long scheduleCategory = schedulesymbol.getScheduleCategoryId();
                
                try {
                    Special = Long.parseLong(oidSpecialLeave);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    Unpaid = Long.parseLong(oidUnpaidLeave);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                if (scheduleCategory == Special) {
                    result = 1; //untuk special leave
                } else if (scheduleCategory == Unpaid) {
                    result = 2; //untuk unpaid leave 
                } else {
                    result = 0;
                }
                return result;
                
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return 0;
    }
    
    public static Vector ListSpecialUnpaidLeaveTaken(long AppId) {
        
        String oidSpecialLeave = String.valueOf(PstSystemProperty.getValueByName("OID_SPECIAL"));
        String oidUnpaidLeave = String.valueOf(PstSystemProperty.getValueByName("OID_UNPAID"));
        
        long LongoidSpecialLeave = 0;
        long LongoidUnpaidLeave = 0;
        try {
            LongoidSpecialLeave = Long.parseLong(oidSpecialLeave);
        } catch (Exception e) {
            
        }
        
        try {
            LongoidUnpaidLeave = Long.parseLong(oidUnpaidLeave);
        } catch (Exception e) {
        }
        
        if (LongoidSpecialLeave == AppId) {
            Vector oidSchedule = new Vector();
            oidSchedule = getVectorSpecialLeave();
            
            String sql = "SELECT * FROM " + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN + " WHERE " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " = " + AppId;
            
            if (oidSchedule.size() > 0) {
                
                sql = sql + " AND ( ";
                
                for (int index = 0; index < oidSchedule.size(); index++) {
                    
                    sql = sql + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    int max = oidSchedule.size() - 1;
                    
                    if (index != max) {
                        sql = sql + " OR ";
                    }
                }
                
                sql = sql + " )";
                
                Vector list = new Vector();
                
                list = PstSpecialUnpaidLeaveTaken.getSpecialUnpaidLeaveTakenList(sql);
                
                return list;
            }
            
        } else if (LongoidUnpaidLeave == AppId) {
            Vector oidSchedule = new Vector();
            oidSchedule = getVectorUnpaidLeave();
            
            String sql = "SELECT * " + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN + " WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " = " + AppId;
            
            if (oidSchedule.size() > 0) {
                
                sql = sql + " AND ( ";
                
                for (int index = 0; index < oidSchedule.size(); index++) {
                    
                    sql = sql + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    int max = oidSchedule.size() - 1;
                    
                    if (index != max) {
                        sql = sql + " OR ";
                    }
                    
                }
                sql = sql + " ) ";
                
                Vector list = new Vector();
                list = PstSpecialUnpaidLeaveTaken.getSpecialUnpaidLeaveTakenList(sql);
                
                return list;
                
            }
        } else {
            return null;
        }
        
        return null;
    }
    
    public static Vector ListSpecialLeaveTaken(long AppID) {
        //String oidSpecialLeave = String.valueOf(PstSystemProperty.getValueByName("OID_SPECIAL")); 
        DBResultSet dbrs = null;
        try {
            Vector oidSchedule = new Vector();
            oidSchedule = getVectorSpecialLeave();
            String whereClause = PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " = " + AppID;
            int max = oidSchedule.size() - 1;
            if (oidSchedule.size() > 0) {
                whereClause = whereClause + " AND ( ";
                for (int index = 0; index < oidSchedule.size(); index++) {
                    whereClause = whereClause + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    if (max != index) {
                        whereClause = whereClause + " OR ";
                    }
                }
                whereClause = whereClause + " ) ";
            }
            
            Vector listSpecialLeave = PstSpecialUnpaidLeaveTaken.list(0, 0, whereClause, null);
            
            return listSpecialLeave;
            
        } catch (Exception e) {
            
        }
        
        return null;
    }
    
    public static Vector ListSpecialUnpaidLeave(long AppID, int type_form) {
        //update by satrya 2013-11-15
        //public static Vector ListSpecialUnpaidLeave(long AppID) {

        try {
            //update by satrya 2013-11-15
            //String whereClause = "SU."+PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " = " + AppID
            String whereClause = "SU." + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " = " + AppID
                    + " AND LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_TYPE_FROM_LEAVE] + "=" + type_form;
            
            Vector listSpecialLeave = PstSpecialUnpaidLeaveTaken.listJointWithLeaveApplication(0, 0, whereClause, null);
            
            return listSpecialLeave;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        return null;
    }
    
    public static Vector ListUnpaidLeaveTake(long AppID) {
        
        DBResultSet dbrs = null;
        try {
            Vector oidSchedule = new Vector();
            oidSchedule = getVectorSpecialLeave();
            String whereClause = PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + " = " + AppID;
            
            int max = oidSchedule.size() - 1;
            
            if (oidSchedule.size() > 0) {
                whereClause = whereClause + " AND ( ";
                for (int index = 0; index < oidSchedule.size(); index++) {
                    whereClause = whereClause + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule.get(index);
                    
                    if (max != index) {
                        whereClause = whereClause + " OR ";
                    }
                }
                whereClause = whereClause + " ) ";
            }
            
            Vector listSpecialLeave = PstSpecialUnpaidLeaveTaken.list(0, 0, whereClause, null);
            
            return listSpecialLeave;
            
            
        } catch (Exception e) {
            
        }
        
        return null;
    }

    /**
     * @AUTHOR : ROY A.
     * @DESCRIPTION : Untuk mendapatkan apllication yang belum di execution.
     * @RETURN : Vector object leave application
     *
     */
    public static Vector getApplication(long empId) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        try {
            
            String sql = "SELECT " + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " FROM "
                    + PstLeaveApplication.TBL_LEAVE_APPLICATION + " WHERE "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = " + empId + " AND ( "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 0 or "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 1 or "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 2 )";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                LeaveApplication objLeaveApplication = new LeaveApplication();
                objLeaveApplication.setOID(rs.getLong(1));
                
                result.add(objLeaveApplication);
            }
            rs.close();
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    /**
     * create by satrya 2013-07-27
     *
     * @param empId
     * @param dpStockTaken
     * @return
     */
    public static Vector getApplicationDP(long empId, DpStockTaken dpStockTaken) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        if (dpStockTaken != null && dpStockTaken.getPaidDate() == null) {
            return null;
        }
        String sPaidDate = Formater.formatDate(dpStockTaken.getPaidDate(), "yyyy-MM-dd 00:00:00");
        try {
            
            String sql = "SELECT LA." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + " ,DSTKN." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_PAID_DATE]
                    + " FROM "
                    + PstLeaveApplication.TBL_LEAVE_APPLICATION + " AS LA "
                    + " INNER JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " AS DSTKN ON LA."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + " =DSTKN." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " WHERE LA."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = " + empId + " AND ( "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 0 or "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 1 or "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 2 ) AND "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_PAID_DATE] + " = \"" + sPaidDate + "\"";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                DpStockTaken objDpStockTaken = new DpStockTaken();
                objDpStockTaken.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objDpStockTaken.setPaidDate(rs.getDate(PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_PAID_DATE]));
                result.add(objDpStockTaken);
            }
            rs.close();
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    /**
     * @AUTHOR : ROY ANDIKA.
     * @DESCRIPTION : Untuk mendapatkan taken QTY
     * @RETURN : (float) Total taken al
     */
    public static float getTakenAL(long empID, long appID) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(" + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " WHERE "
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + appID + " AND "
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_EMPLOYEE_ID] + " = " + empID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float total = 0;
            
            while (rs.next()) {
                total = rs.getFloat(1);
            }
            rs.close();
            return total;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION ::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0.0f;
        
    }

    /**
     * create by satrya 2013-07-27
     *
     * @param empID
     * @param ApplicationId
     * @return
     */
    public static float getTakensDp(long empID, long ApplicationId, Date paidDate) {
        
        DBResultSet dbrs = null;
        if (empID == 0 || ApplicationId == 0 || paidDate == null) {
            return 0;
        }
        try {
            String sPaidDate = Formater.formatDate(paidDate, "yyyy-MM-dd 00:00:00");
            String sql = "SELECT SUM(" + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " WHERE "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + ApplicationId + " AND "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_PAID_DATE] + "=\"" + sPaidDate + "\" AND "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_EMPLOYEE_ID] + " = " + empID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float total = 0;
            
            while (rs.next()) {
                total = total + rs.getFloat(1);
            }
            rs.close();
            return total;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION ::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0.0f;
        
    }

    /**
     * @AUTHOR : Donald Siregar.
     * @DESCRIPTION : Untuk mendapatkan total taken QTY
     * @RETURN : (float) Total taken al
     */
    public static float getTotalTakenAL(long empID) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(a." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " a INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " b ON a."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = b." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + " WHERE b." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + "=" + empID
                    + " AND b." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = 3";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float total = 0.0f;
            
            while (rs.next()) {
                total = rs.getFloat(1);
            }
            rs.close();
            //AlStockManagement alStok = PstAlStockManagement.getAlStockPerEmpFirst(empID);
            return total;//+ alStok.getQtyUsed();
        } catch (Exception e) {
            System.out.println("EXCEPTION ::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0.0f;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA.
     * @DESCRIPTION : Untuk mendapatkan taken QTY
     * @RETURN : (float) Total taken al
     */
    public static float getTakenAL(long empID, long appID, long takenAlOid) {
        //update by satrya 2012-10-11
        //public static float getTakenAL(long empID, long appID, long takenAlOid) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(" + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " WHERE "
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + appID + " AND "
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_EMPLOYEE_ID] + " = " + empID + " AND "
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_TAKEN_ID] + " != " + takenAlOid;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float total = 0;
            
            while (rs.next()) {
                total = rs.getFloat(1);
                
            }
            rs.close();
            return total;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION ::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0.0f;
        
    }

    /**
     *
     * @param empID
     * @param appID
     * @return
     */
    public static float getTakenDp(long empID, long appID) {
        //public static int getTakenDp(long empID, long appID) {
        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(" + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " WHERE "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + appID + " AND "
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_EMPLOYEE_ID] + " = " + empID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float total = 0;
            while (rs.next()) {
                total = rs.getInt(1);
            }
            rs.close();
            return total;
            
        } catch (Exception e) {
            
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0.0f;
        
        
    }

    /**
     *
     * @param empId
     * @param appId
     * @return
     */
    public static float getTakenSpecial(long empId, long appId) {
        //public static int getTakenSpecial(long empId, long appId) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        Vector vectOidSpecialLeave = getVectorSpecialLeave();
        
        try {
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN + " WHERE "
                    + PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID + "=" + empId + " AND "
                    + PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID + "=" + appId;
            
            
            if (vectOidSpecialLeave.size() > 0) {
                int max = vectOidSpecialLeave.size() - 1;
                
                sql = sql + " AND (";
                
                for (int indx = 0; indx < vectOidSpecialLeave.size(); indx++) {
                    sql = sql + vectOidSpecialLeave.get(indx);
                    
                    if (indx != max) {
                        sql = sql + " OR ";
                    }
                    
                }
                
                sql = sql + " ) ";
            }
            
            System.out.println("sql sum special " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float total = 0;
            while (rs.next()) {
                total = rs.getFloat(1);
            }
            rs.close();
            return total;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return 0.0f;
    }

    /**
     *
     * @param empID
     * @param appID
     * @return
     */
    public static float getTakenLL(long empID, long appID) {
        //public static int getTakenLL(long empID, long appID) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
        try {
            String sql = "SELECT SUM(" + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " WHERE "
                    + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + appID + " AND "
                    + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_EMPLOYEE_ID] + " = " + empID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float total = 0;
            while (rs.next()) {
                total = rs.getFloat(1);
                
            }
            rs.close();
            return total;
            
        } catch (Exception e) {
            
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0.0f;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @TRUE : EXPIRED
     * @FALSE : NOT EXPIRED
     */
    public static boolean ExpiredAL(Date expDt, Date dtEntitle) {
        
        if (expDt == null || dtEntitle == null) {
            return false;
        }
        
        DBResultSet dbrs = null;
        
        try {

            /* karena masalahnya jika sdh melewati tahun tdk bisa mengajukan cuti tapi kasusnya belum closing
             * String sql = "SELECT DATEDIFF(CURRENT_DATE,'" + Formater.formatDate(expDt, "yyyy-MM-dd") + "')";

             dbrs = DBHandler.execQueryResult(sql);
             ResultSet rs = dbrs.getResultSet();

             int result = 0;

             while (rs.next()) {

             result = rs.getInt(1);

             if (result <= 0) {
             return false;
             } else {
             return true;
             }

             }*/
            if ((dtEntitle.getTime() / (24L * 60L * 60L * 1000L)) <= (expDt.getTime() / (24L * 60L * 60L * 1000L))) {
                return false;
            } else {
                return true;
            }
            
        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return false;
    }

    /**
     * @AUTHOR : ROY ANDIKA.
     * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL SISA STOCK UNTUK AL
     */
    public static float getALQtyResidue(long empId) {
        ///public static int getALQtyResidue(long empId){

        if (empId == 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EXPIRED_DATE] + ","
                    + //update by satrya 2013-12-04
                    PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + empId
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float residue = 0.0f;
            
            while (rs.next()) {
                
                float prv_blc = rs.getFloat(1);                
                float tkn = rs.getFloat(2);
                float qty = rs.getFloat(3);
                // int prv_blc = rs.getInt(1);                
                //int tkn = rs.getInt(2);
                //int qty = rs.getInt(3);
                Date exp = rs.getDate(4);
                float entitled = rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]);
                Date dtEntitled = rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]);
                /**
                 * @AUTHOR : ROY ANDIKA
                 * @DESC : TRUE - > EXPIRED FALSE - > NOT EXPIRED
                 */
                boolean exp_sts = ExpiredAL(exp, dtEntitled);
                
                if (exp_sts == true) {

                    //residue = qty - tkn;
                    //update by satrya 2013-12-04
                    residue = entitled - tkn;
                } else {
                    
                    residue = prv_blc + entitled - tkn;
                    //update by satrya 2013-12-04
                    //residue = prv_blc + qty - tkn;
                }
                
            }
            
            
            rs.close();
            return residue;
            
        } catch (Exception e) {
            System.out.println("EXCPETION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0.0f;
    }

    /**
     * create by satrya 2013-07-27
     *
     * @param empId
     * @param objDpStockTakenInSystem
     * @return
     */
    public static float getDpQtyResidue(long empId, DpStockTaken objDpStockTakenInSystem) {
        ///public static int getALQtyResidue(long empId){

        if (empId == 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT " + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ","
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_USED] + ","
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_RESIDUE] + ","
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EXPIRED_DATE] + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " WHERE " + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + "=" + empId
                    + " AND " + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = " + objDpStockTakenInSystem.getDpStockId()
                    + " AND " + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STATUS] + " = " + PstDpStockManagement.DP_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float residue = 0.0f;
            
            while (rs.next()) {

                //float prv_blc = rs.getFloat(1);                
                float tkn = rs.getFloat(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_USED]);
                float qty = rs.getFloat(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY]);
                Date exp = rs.getDate(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EXPIRED_DATE]);
                float expiredQTY = SessLeaveManagement.getDpExpired(empId, exp);
                float qtyResidue = rs.getFloat(PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_RESIDUE]);
                
                residue = qty - expiredQTY - tkn;
                // boolean exp_sts = ExpiredAL(exp);

                /* if (exp_sts == true) {

                 residue = qty - tkn;

                 } else {

                 residue = expiredQTY - qty - tkn;
                 }*/
                
            }
            
            
            rs.close();
            return residue;
            
        } catch (Exception e) {
            System.out.println("EXCPETION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0.0f;
    }

    /**
     * @Author Roy Andika
     * @Description Untuk mendapatkan total epired DP
     * @param employee Id
     * @return
     */
    public static float getExpiredDP(long dpStockId) {
        
        String where = PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_DP_STOCK_ID] + " = " + dpStockId;
        Vector list = PstDpStockExpired.list(0, 0, where, null);
        
        float sumExpired = 0;
        
        if (list != null && list.size() > 0) {
            
            for (int i = 0; i < list.size(); i++) {
                
                DpStockExpired dpStockExpired = (DpStockExpired) list.get(i);
                sumExpired = sumExpired + dpStockExpired.getExpiredQty();
                
            }
            return sumExpired;
        }
        return 0;
    }
    
    public static float getDPQtyResidue(long empId) {
        
        DBResultSet dbrs = null;
        try {
            
            String sql = "SELECT " + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ","
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_USED] + ","
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " WHERE " + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + "=" + empId
                    + " AND " + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STATUS] + " = 0";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tmpResidue = 0;
            float residue = 0;
            
            while (rs.next()) {
                
                float qty = rs.getFloat(1);
                float usd = rs.getFloat(2);

                /**
                 * @DESC : MENDAPATKAN TOTAL EXPIRED QTY
                 */
                float expired = getExpiredDP(rs.getLong(3));
                
                tmpResidue = qty - usd - expired;
                residue = residue + tmpResidue;
                
            }
            
            rs.close();
            return residue;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    
    public static float getLLQtyResidue(long empId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + ","
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + ","
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=" + empId
                    + " AND " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = 0 ";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            float residue = 0;
            
            while (rs.next()) {
                float prv_blnc = rs.getFloat(1);
                float qty = rs.getFloat(2);
                float tkn = rs.getFloat(3);
                residue = prv_blnc + qty - tkn;
            }
            
            rs.close();
            return residue;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    
    public static Float getLlQty(long empId) {
        
        if (empId == 0) {
            return 0f;
        }
        
        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY]
                    + " FROM " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + empId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Float LlQty = rs.getFloat(1);
                return LlQty;
            }
            
        } catch (Exception e) {
            System.out.println("EXCPETION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0f;
    }

    /*
     * AUTHOR   : ROY A
     * DESC     : UNTUK MENDAPATKAN STOCK AL YANG AKTIF
     * 
     */
    public static AlStockManagement GetAlAktif(long employeeID) {
        
        DBResultSet dbrs = null;
        
        try {
            
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
        
    }

    /*
     * TRUE     : EXPIRED
     * FALSE    : NO EXPIRED     
     */
    public static boolean getStatusLeaveAlExpired(long alStockId) {
        
        AlStockManagement alStockManagement = new AlStockManagement();
        
        try {
            alStockManagement = PstAlStockManagement.fetchExc(alStockId);
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        
        if (alStockManagement.getExpiredDate() != null) {
            
            boolean stsDateExpired = ExpiredAL(alStockManagement.getExpiredDate(), alStockManagement.getEntitleDate());
            
            if (stsDateExpired == true) {
                
                return true;
                
            } else {
                
                return false;
                
            }
            
        } else {
            
            return false;
            
        }
    }

    /**
     * @AUTHOR : ROY A.
     * @DESCRIPTION : Untuk mendapatkan eligable dari AL
     * @Return : (int)Total eligible day
     */
    public static float getAlEligbleDay(long empId) {
        
        float total = 0;
        
        if (empId == 0) {
            return 0;
        }
        
        Vector result = new Vector();

        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN APPLICATION YANG BELUM DIEKSEKUSI.
         */
        result = getApplication(empId);
        
        for (int indexResult = 0; indexResult < result.size(); indexResult++) {
            
            LeaveApplication objLeaveApplication = (LeaveApplication) result.get(indexResult);
            
            long ApplicationId = objLeaveApplication.getOID();

            /**
             * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL TAKEN AL MASING"
             * APPLICATION
             */
            float totalAlTaken = getTakenAL(empId, ApplicationId);
            
            total = total + totalAlTaken;
        }

        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL SISA DARI STOCK AL YANG AKTIF
         */        
        float residue = getALQtyResidue(empId);
        //System.out.println(residue);        

        float eligble = residue - total;
        
        return eligble;
    }

    /**
     * create by satrya 2013-07-27
     *
     * @param empId
     * @return
     */
    public static float getDpEligbleDays(long empId, DpStockTaken objDpStockTakenInSystem) {
        
        float total = 0;
        
        if (empId == 0) {
            return 0;
        }
        
        Vector result = new Vector();

        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN APPLICATION YANG BELUM DIEKSEKUSI.
         */
        result = getApplicationDP(empId, objDpStockTakenInSystem);
        if (result == null) {
            return 0;
        }
        for (int indexResult = 0; indexResult < result.size(); indexResult++) {
            
            DpStockTaken objDpStockTaken = (DpStockTaken) result.get(indexResult);
            
            long ApplicationId = objDpStockTaken.getOID();
            Date paidDate = objDpStockTaken.getPaidDate();
            
            float totalDpTaken = getTakensDp(empId, ApplicationId, paidDate);
            
            total = total + totalDpTaken;
        }
        //   float totalDpTaken = getTakensDp(empId, objDpStockTakenInSystem);
        //   total = totalDpTaken;
        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL SISA DARI STOCK DP YANG AKTIF
         */        
        float residue = getDpQtyResidue(empId, objDpStockTakenInSystem);
        //System.out.println(residue);        

        float eligble = residue - total;
        
        return eligble;
    }

    /**
     * @AUTHOR : ROY A.
     * @DESCRIPTION : Untuk mendapatkan eligable dari AL
     * @Return : (int)Total eligible day
     */
    public static float getAlEligbleDay(long empId, long takenId, int tkn) {
        
        float total = 0;
        
        if (empId == 0) {
            return 0;
        }
        
        Vector result = new Vector();

        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN APPLICATION YANG BELUM DIEKSEKUSI.
         */
        result = getApplication(empId);
        
        for (int indexResult = 0; indexResult < result.size(); indexResult++) {
            
            LeaveApplication objLeaveApplication = (LeaveApplication) result.get(indexResult);
            
            long ApplicationId = objLeaveApplication.getOID();

            /**
             * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL TAKEN AL MASING"
             * APPLICATION
             */
            float totalAlTaken = getTakenAL(empId, ApplicationId, takenId);
            
            total = total + totalAlTaken;
        }

        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL SISA DARI STOCK AL YANG AKTIF
         */
        float residue = getALQtyResidue(empId);
        
        float eligble = residue - total - tkn;
        
        return eligble;
    }

    /**
     * @AUTHOR : ROY A.
     * @DESCRIPTION : Untuk mendapatkan eligable dari AL
     * @Return : (int)Total eligible day
     */
    public static float getAlEligbleDayHaveStock(long empId, int tkn) {
        
        float total = 0;
        
        if (empId == 0) {
            return 0;
        }
        
        Vector result = new Vector();

        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN APPLICATION YANG BELUM DIEKSEKUSI.
         */
        result = getApplication(empId);
        
        for (int indexResult = 0; indexResult < result.size(); indexResult++) {
            
            LeaveApplication objLeaveApplication = (LeaveApplication) result.get(indexResult);
            
            long ApplicationId = objLeaveApplication.getOID();

            /**
             * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL TAKEN AL MASING"
             * APPLICATION
             */
            float totalAlTaken = getTakenAL(empId, ApplicationId);
            
            total = total + totalAlTaken;
        }

        /**
         * @DESCRIPTION : UNTUK MENDAPATKAN TOTAL SISA DARI STOCK AL YANG AKTIF
         */
        float residue = getALQtyResidue(empId);
        
        float eligble = residue - total - tkn;
        
        return eligble;
    }

    /**
     * @AUTHOR : ROY ANDIKA.
     * @DESC : MENDAPATKAN TOTAL LL ELIGBLE DAY
     */
    public static float getLlEligbleDay(long empId) {
        
        if (empId == 0) {
            return 0;
        }
        
        float total = 0;
        
        Vector result = new Vector();

        /**
         * @DESC : UNTUK MENDAPATKAN APPLICATION YANG BELUM DI EKSEKUSI
         */
        result = getApplication(empId);
        
        for (int indexResult = 0; indexResult < result.size(); indexResult++) {
            
            LeaveApplication objLeaveApplication = (LeaveApplication) result.get(indexResult);
            
            long ApplicationId = objLeaveApplication.getOID();
            
            float totalLlTaken = getTakenLL(empId, ApplicationId);
            
            total = total + totalLlTaken;
        }

        /**
         * @DESC : MENDAPATKAN TOTAL QTY YANG EXPIRED BERDASARKAN STOCK YANG
         * AKTIF
         */
        float expired = getExpiredQty(empId);

        /**
         * @DESC : MENDAPATKAN SISA STOCK
         */
        float residue = getLLQtyResidue(empId);
        
        float eligble = residue - total - expired;
        
        return eligble;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @Param : employee_id
     * @DESC : UNTUK MENDAPATKAN TOTAL EXPIRED QTY DARI LONG LEAVE
     */
    public static float getExpiredQty(long employee_id) {

        /**
         * @DESC : MENDAPATKAN STOCK MANAGEMENT LL YANG AKTIF
         *
         */
        long ll_Stock_id = getLLAktif(employee_id);
        
        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(" + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " WHERE "
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " = " + ll_Stock_id;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float result = rs.getInt(1);
                return result;
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0f;
    }
    
    public static Date DATE_ADD(Date tknDt, int interval) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DATE_ADD('" + Formater.formatDate(tknDt, "yyyy-MM-dd") + "', INTERVAL " + interval + " MONTH )";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                String result = rs.getString(1);
                return Formater.formatDate(result, "yyyy-MM-dd");
                
            }
            
        } catch (Exception e) {
            System.out.println("EXCEPTION ::::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENGECEK TANGGAL YANG DIMASUKAN APAKAH DALAM PERIODE YANG
     * SAMA
     */
    public static boolean getLastDayAlPeriod(long stock_id, Date dateTkn) {
        
        AlStockManagement alStockManagement = new AlStockManagement();
        
        
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }
        
        try {
            alStockManagement = PstAlStockManagement.fetchExc(stock_id);
        } catch (Exception e) {
            System.out.println("EXCEPTION :::" + e.toString());
        }
        
        int interval = leaveConfig.getIntervalALinMonths()[0];     //range mendapatkan AL (dalam bulan)
        System.out.println("Interval" + interval);
        //?berbeda
        Date fnsPeriod = DATE_ADD(alStockManagement.getEntitleDate(), interval);                    // finnish date 
        System.out.println("fnsPeriod" + fnsPeriod);
        //update by satrya 2013-11-19
        if (alStockManagement.getEntitleDate() == null) {
            return false;
        }
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT '" + Formater.formatDate(dateTkn, "yyyy-MM-dd") + "' BETWEEN '"
                    + Formater.formatDate(alStockManagement.getEntitleDate(), "yyyy-MM-dd") + " 00:00:00" + "' AND '"
                    + Formater.formatDate(fnsPeriod, "yyyy-MM-dd") + " 23:59:59'";
            
            dbrs = DBHandler.execQueryResult(sql);
            //hanya untuk test
            //System.out.println("sql query " + sql);
            ResultSet rs = dbrs.getResultSet();
            //update by satrya 2013-11-19
            String staken = dateTkn == null ? "" : Formater.formatDate(dateTkn, "yyyy-MM-dd");
            while (rs.next()) {
                int result = rs.getInt(1);
                //dibupdate karena jika user memilih takennya sama dengan entitle yg didapatkan
                if (result == 1 || staken.equalsIgnoreCase(Formater.formatDate(alStockManagement.getEntitleDate(), "yyyy-MM-dd"))) {
                    //update by satrya 2013-11-19
                    //if (result == 1) {
                    return true;
                } else {
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return false;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENGECEK TANGGAL YANG DIMASUKAN APAKAH DALAM PERIODE YANG
     * SAMA
     */
    public static boolean getBeforeDayExpired( Date dateTkn, Position positionOfUser) {
        
        boolean result = false;
        Date dateToday = new Date();
        Date dateDeadBefore = new Date();
        dateDeadBefore.setHours(dateDeadBefore.getHours() - positionOfUser.getDeadlineScheduleLeaveBefore());
        
        DBResultSet dbrs = null;
        
        try {
            
            if (dateTkn.getTime() > dateDeadBefore.getTime()){
                result = true;
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return result;
    }
    
    
    /**
     * create by satrya 2014-01-04 keterangan: untuk mengecek status apakah
     * masih aktive
     *
     * @param stock_id
     * @return
     */
    public static boolean getCekStatusAktive(long stock_id) {
        
        AlStockManagement alStockManagement = new AlStockManagement();
        
        
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }
        
        try {
            alStockManagement = PstAlStockManagement.fetchExc(stock_id);
        } catch (Exception e) {
            System.out.println("EXCEPTION :::" + e.toString());
        }
        
        int interval = leaveConfig.getIntervalALinMonths()[0];     //range mendapatkan AL (dalam bulan)
        System.out.println("Interval" + interval);
        //?berbeda
        Date fnsPeriod = DATE_ADD(alStockManagement.getEntitleDate(), interval);                    // finnish date 
        System.out.println("fnsPeriod" + fnsPeriod);
        //update by satrya 2013-11-19
        if (alStockManagement.getEntitleDate() == null) {
            return false;
        }
        DBResultSet dbrs = null;
        
        try {
            
            if (alStockManagement != null && alStockManagement.getEntitleDate() != null && (alStockManagement.getEntitleDate().getTime() / (24L * 60L * 60L * 1000L)) <= (alStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L))) {
                return true;
            } else {
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return false;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan commencing date dari employee yang ingin dicari
     * @param employeeId
     * @return
     */
    private static Date getCommencingDate(long employeeId) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " FROM "
                    + PstEmployee.TBL_HR_EMPLOYEE + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " = " + employeeId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                Date comDate = rs.getDate(1);
                return comDate;
            }
            
        } catch (Exception e) {
            System.out.println("[exception] " + e.toString());
        }
        
        return null;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan apakah tanggal yang dimasukan berada dalam range
     * periode al yang aktif
     * @param stock_id
     * @param dateTkn
     * @return
     */
    public static boolean getStatusAlAktif(long stock_id, Date dateTkn, int monthP, int dayP) {
        
        DBResultSet dbrs = null;
        AlStockManagement alStockManagement = new AlStockManagement();
        
        try {
            alStockManagement = PstAlStockManagement.fetchExc(stock_id);
        } catch (Exception e) {
            System.out.println("[exception] " + e.toString());
        }
        
        if (alStockManagement.getEmployeeId() == 0) {
            return false;
        }
        String expDate = "";
        
        Date comencingDT = getCommencingDate(alStockManagement.getEmployeeId()); // commencing date employee

        Date periodeDT = SessLeaveClosing.getPeriodeClosing(comencingDT, dayP);
        
        if (stock_id != 0) {
            
            int diff = DATEDIFF(periodeDT, alStockManagement.getEntitleDate());
            
            if (diff == 0) { /* Kondisi dimana ent. date closing sama dengan periode closing pertama */                
                
                int monthEnd = monthP/*update by satrya 2013-11-08 karena dia by period alStockManagement.getEntitleDate().getMonth() + 1*/;
                
                int year = 0;

                /**
                 * @Desc Untuk pengecekan, seperti misalnya entitle 2005-05-01,
                 * maka entitle berikutnya adalah 2005-12-26(pada tahun yang
                 * sama)
                 */
                if (monthEnd <= 11) {
                    year = alStockManagement.getEntitleDate().getYear() + 1900;
                } else {
                    year = alStockManagement.getEntitleDate().getYear() + 1900 + 1;
                }
                
                expDate = "" + year + "-" + monthEnd + "-" + dayP;
                
            } else {
                
                int year = alStockManagement.getEntitleDate().getYear() + 1900 + 1;
                
                expDate = "" + year + "-" + monthP + "-" + dayP;
                
            }            
            
            try {
                
                String sql = "SELECT '" + Formater.formatDate(dateTkn, "yyyy-MM-dd") + "' >= '"
                        + Formater.formatDate(alStockManagement.getEntitleDate(), "yyyy-MM-dd") + "' AND '"
                        + Formater.formatDate(dateTkn, "yyyy-MM-dd") + "' < '"
                        + expDate + "'";
                
                System.out.println("Sql Aktif = " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                
                while (rs.next()) {
                    int result = rs.getInt(1);
                    if (result == 1) {
                        return true;
                    } else {
                        return false;
                    }
                }
                
            } catch (Exception e) {
                System.out.println("[exception] " + e.toString());
            } finally {
                DBResultSet.close(dbrs);
            }
        }
        
        return false;
    }
    
    public static Date getExpiredAl(long stock_id) {
        
        AlStockManagement alStockManagement = new AlStockManagement();
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }
        
        try {
            alStockManagement = PstAlStockManagement.fetchExc(stock_id);
        } catch (Exception e) {
            System.out.println("EXCEPTION :::" + e.toString());
        }
        
        int interval = leaveConfig.getIntervalALinMonths()[LeaveConfigHR.INTERVAL_AL_HARDROCK];     //range mendapatkan AL (dalam bulan)

        Date fnsPeriod = DATE_ADD(alStockManagement.getEntitleDate(), interval);                     // finnish date 

        if (fnsPeriod != null) {
            
            return fnsPeriod;
            
        }
        
        return null;
    }
    
    public static float getSpecialEligbleDay(long empId) {
        
        float total = 0;
        Vector result = new Vector();
        
        result = getApplication(empId);
        
        for (int indexResult = 0; indexResult < result.size(); indexResult++) {
            LeaveApplication objLeaveApplication = (LeaveApplication) result.get(indexResult);
            
            float totalTakeApp = 0;
            
            long ApplicationId = objLeaveApplication.getOID();
            
            float totalSpecialTaken = getTakenSpecial(empId, ApplicationId);
            
            total = total + totalSpecialTaken;
        }
        
        float residue = getLLQtyResidue(empId);
        
        float eligble = residue - total;
        
        return eligble;
    }
    
    public static long getALAktif(long empId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + empId + " AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "= 0";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                long oid = rs.getLong(1);
                return oid;
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    public static long getLLAktif(long empId) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " FROM " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=" + empId + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "= 0";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                long oid = rs.getLong(1);
                return oid;
            }
            rs.close();
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author :Roy Andika
     * @Description :Untuk memproses Eligble Day
     * @param :EmpId
     * @return :(int)
     */
    public static float getDpEligbleDay(long empId) {
        
        float total = 0;
        
        Vector result = new Vector();
        
        result = getApplication(empId);
        
        for (int indexResult = 0; indexResult < result.size(); indexResult++) {
            
            LeaveApplication objLeaveApplication = (LeaveApplication) result.get(indexResult);
            
            long ApplicationId = objLeaveApplication.getOID();
            
            float totalDpTaken = getTakenDp(empId, ApplicationId);
            
            total = total + totalDpTaken;
        }
        
        float residue = getDPQtyResidue(empId);
        
        float eligble = residue - total;
        
        return eligble;
    }
    
    public static Vector getTakenAl(long ApplicationId) {
        try {
            String where = PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + ApplicationId;
            Vector list = PstAlStockTaken.list(0, 0, where, null);
            return list;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }
    
    public static Vector getTakenLl(long ApplicationId) {
        try {
            String where = PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + ApplicationId;
            Vector list = PstLlStockTaken.list(0, 0, where, null);
            return list;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }
    
    public static Vector getTakenDp(long ApplicationId) {
        try {
            String where = PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + ApplicationId;
            Vector list = PstDpStockTaken.list(0, 0, where, null);
            return list;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }
    
    public static Vector getTakenSpcUnpaid(long ApplicationId) {
        
        try {
            String where = PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_LEAVE_APLICATION_ID] + "=" + ApplicationId;
            Vector list = PstSpecialUnpaidLeaveTaken.list(0, 0, where, null);
            return list;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }
	
	public static Vector getTakenSs(long ApplicationId) {
        try {
            String where = PstSpecialStockTaken.fieldNames[PstSpecialStockTaken.FLD_LEAVE_APPLICATION_ID] + "=" + ApplicationId;
            Vector list = PstSpecialStockTaken.list(0, 0, where, null);
            return list;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

//    public static Vector searchLeaveApplicationExcecute(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet) {
//        DBResultSet dbrs = null;
//        Vector result = new Vector(1, 1);
//
//        if (objSrcLeaveApp == null) {
//            return new Vector(1, 1);
//        }
//
//        try {
//
//            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +
//                    ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] +
//                    ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] +
//                    ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] +
//                    ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE] +
//                    ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] +
//                    ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] +
//                    ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL] +
//                    ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] +
//                    ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_START_DATE] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_START_DATE] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_START_DATE] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_START_DATE] +
//                    ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] +
//                    " FROM " + PstEmployee.TBL_HR_EMPLOYEE +
//                    " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON " +
//                    " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " +
//                    " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] +
//                    " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEP ON " +
//                    " DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
//                    " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] +
//                    " INNER JOIN " + PstViewLeaveAppPeriod.TBL_VIEW_LEAVE_APP_PERIOD + " LAP ON " +
//                    " LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LEAVE_APPLICATION_ID] +
//                    " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] +
//                    //update by satrya 2013-03-08
//                    " LEFT JOIN "+ PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL 
//                            + " AS PBL ON PBL."+PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID] 
//                    + " = APP."+PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
//    
//
//
//            String strFullName = "";
//            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
//                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
//                if (vectName != null && vectName.size() > 0) {
//                    strFullName = strFullName + " (";
//                    for (int i = 0; i < vectName.size(); i++) {
//                        String str = (String) vectName.get(i);
//                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
//                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] +
//                                    " LIKE '%" + str.trim() + "%' ";
//                        } else {
//                            strFullName = strFullName + str.trim();
//                        }
//                    }
//                    strFullName = strFullName + ")";
//                }
//            }
//
//            String strEmpNum = "";
//            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
//                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] +
//                        " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
//            }
//            
//            //update by satrya 2013-09-19
//            String strCompanyId = "";
//            if (objSrcLeaveApp.getCompanyId() != 0) {
//                strCompanyId = strCompanyId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] +
//                        " = " + objSrcLeaveApp.getCompanyId();
//            }
//            //update by satrya 2013-09-19
//            String strDivisionId = "";
//            if (objSrcLeaveApp.getDivisionId() != 0) {
//                strDivisionId = strDivisionId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] +
//                        " = " + objSrcLeaveApp.getDivisionId();
//            }
//
//            String strDepartment = "";
//            if (objSrcLeaveApp.getDepartmentId() != 0) {
//                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] +
//                        " = " + objSrcLeaveApp.getDepartmentId();
//            }
//
//            String strPosition = "";
//            if (objSrcLeaveApp.getPositionId() != 0) {
//                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] +
//                        " = " + objSrcLeaveApp.getPositionId();
//            }
//
//            String strSection = "";
//            if (objSrcLeaveApp.getSectionId() != 0) {
//                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] +
//                        " = " + objSrcLeaveApp.getSectionId();
//            }
//
//            String strSubmission = "";
//            if (!objSrcLeaveApp.isSubmission()) {
//                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
//                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
//                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE] +
//                            " = \"" + strSubmissionDate + "\"";
//                }
//            }
//
//            String strApprovalStatus = "";
//            switch (objSrcLeaveApp.getApprovalStatus()) {
//
//                case 0:
//                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
//                    break;
//
//                case 1:
//                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
//                    break;
//
//                default:
//                    break;
//            }
//
//            String strApprovalStatusHR = "";
//            switch (objSrcLeaveApp.getApprovalHRMan()) {
//
//                case 0:
//                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
//                    break;
//
//                case 1:
//                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
//                    break;
//
//                default:
//                    break;
//            }
//
//
//            String whereClause = "";
//            if (strFullName != null && strFullName.length() > 0) {
//                whereClause = whereClause + strFullName;
//            }
//              //update by satrya 2013-03-13
//         if(objSrcLeaveApp.getTypePublicLeave()!=0){
//            if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + " PBL."+ PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = "+objSrcLeaveApp.getTypePublicLeave();
//            } else {
//                    whereClause = whereClause + " PBL."+ PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = "+objSrcLeaveApp.getTypePublicLeave();
//            }
//         }
//            if (strEmpNum != null && strEmpNum.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strEmpNum;
//                } else {
//                    whereClause = whereClause + strEmpNum;
//                }
//            }
//            
//            if (strCompanyId != null && strCompanyId.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strCompanyId;
//                } else {
//                    whereClause = whereClause + strCompanyId;
//                }
//            }
//            
//            if (strDivisionId != null && strDivisionId.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strDivisionId;
//                } else {
//                    whereClause = whereClause + strDivisionId;
//                }
//            }
//
//            if (strDepartment != null && strDepartment.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strDepartment;
//                } else {
//                    whereClause = whereClause + strDepartment;
//                }
//            }
//
//            if (strSection != null && strSection.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strSection;
//                } else {
//                    whereClause = whereClause + strSection;
//                }
//            }
//
//            if (strPosition != null && strPosition.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strPosition;
//                } else {
//                    whereClause = whereClause + strPosition;
//                }
//            }
//
//            if (strSubmission != null && strSubmission.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strSubmission;
//                } else {
//                    whereClause = whereClause + strSubmission;
//                }
//            }
//
//            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strApprovalStatus;
//                } else {
//                    whereClause = whereClause + strApprovalStatus;
//                }
//            }
//
//            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strApprovalStatusHR;
//                } else {
//                    whereClause = whereClause + strApprovalStatusHR;
//                }
//            }
//
//            if (whereClause != null && whereClause.length() > 0) {
//            sql = sql + " WHERE " + whereClause +
//            " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
//            } else {
//
//            sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
//            }
//            Date nowDate = new Date();
//            String strNowDate = Formater.formatDate(nowDate, "yyyy-MM-dd");
//
//            int maxDayExecution = 0;  /* MAX DAY EXECUTIN ON DAY */
//
//            try {
//
//                maxDayExecution = Integer.parseInt(PstSystemProperty.getValueByName("MAX_DAY_EXECUTION"));
//            } catch (Exception e) {
//                maxDayExecution = 0;
//            }
//if(objSrcLeaveApp.getTypePublicLeave()!=PstPublicLeaveDetail.TYPE_LEAVE){
//            sql = sql + " AND NOT ((" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is null AND " +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is null AND " +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is null AND " +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is null ) or (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "') OR (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "') OR (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "') OR (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "'))";
//}
//            sql = sql + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED;
//
//            if (!(start == 0 && recordToGet == 0)) {
//                sql = sql + " LIMIT " + start + "," + recordToGet;
//            }
//
//            //System.out.println("SQL Leave Application Execution : " + sql);
//            dbrs = DBHandler.execQueryResult(sql);
//            ResultSet rs = dbrs.getResultSet();
//            while (rs.next()) {
//                Vector vectTemp = new Vector(1, 1);
//
//                LeaveApplication objleaveApplication = new LeaveApplication();
//                Department objDepartment = new Department();
//                Employee objEmployee = new Employee();
//                ViewLeaveAppPeriod objViewLeaveAppPeriod = new ViewLeaveAppPeriod();
//
//                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
//                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
//                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
//
//                vectTemp.add(objEmployee);
//
//                objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
//                objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
//                objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
//                objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
//                objleaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
//                objleaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
//
//                vectTemp.add(objleaveApplication);
//
//                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
//
//                vectTemp.add(objDepartment);
//
//                objViewLeaveAppPeriod.setAl_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_START_DATE]));
//                objViewLeaveAppPeriod.setAl_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE]));
//                objViewLeaveAppPeriod.setLl_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_START_DATE]));
//                objViewLeaveAppPeriod.setLl_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE]));
//                objViewLeaveAppPeriod.setDp_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_START_DATE]));
//                objViewLeaveAppPeriod.setDp_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE]));
//                objViewLeaveAppPeriod.setSp_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_START_DATE]));
//                objViewLeaveAppPeriod.setSp_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE]));
//
//                vectTemp.add(objViewLeaveAppPeriod);
//
//                result.add(vectTemp);
//            }
//            return result;
//        } catch (Exception e) {
//            System.out.println("\t Exception on seach LeaveApplication : " + e);
//        } finally {
//            DBResultSet.close(dbrs);
//        }
//        return null;
//    }
    /**
     * create by satrya 2014-04-08 keterangan: untuk mengeset object
     * SessLeaveApplicationViewPeriod
     *
     * @param rs
     * @param sessLeaveApplicationViewPeriod
     */
    private static void resultToObjectJoinViewPeriod(ResultSet rs, SessLeaveApplicationViewPeriod sessLeaveApplicationViewPeriod) {
        try {
            sessLeaveApplicationViewPeriod.setLeaveAppId(rs.getLong("leave_application_id"));
            sessLeaveApplicationViewPeriod.setEmployeeId(rs.getLong("employee_id"));
            sessLeaveApplicationViewPeriod.setAlStartDate(DBHandler.convertDate(rs.getDate("al_start_date"), rs.getTime("al_start_date")));
            sessLeaveApplicationViewPeriod.setAlEndDate(DBHandler.convertDate(rs.getDate("al_end_date"), rs.getTime("al_end_date")));
            sessLeaveApplicationViewPeriod.setLlStartDate(DBHandler.convertDate(rs.getDate("ll_start_date"), rs.getTime("ll_start_date")));
            sessLeaveApplicationViewPeriod.setLlEndDate(DBHandler.convertDate(rs.getDate("ll_end_date"), rs.getTime("ll_end_date")));
            sessLeaveApplicationViewPeriod.setDpStartDate(DBHandler.convertDate(rs.getDate("dp_start_date"), rs.getTime("dp_start_date")));
            sessLeaveApplicationViewPeriod.setDpEndDate(DBHandler.convertDate(rs.getDate("dp_end_date"), rs.getTime("dp_end_date")));
            sessLeaveApplicationViewPeriod.setSpStartDate(DBHandler.convertDate(rs.getDate("sp_start_date"), rs.getTime("sp_start_date")));
            sessLeaveApplicationViewPeriod.setSpEndDate(DBHandler.convertDate(rs.getDate("sp_end_date"), rs.getTime("sp_end_date")));
            
        } catch (Exception e) {
        }
    }

    /**
     * keterangan: list hastable leave period create by satrya 2014-04-8
     *
     * @param objSrcLeaveApp
     * @param start
     * @param recordToGet
     * @return
     */
    public static Hashtable<String, SessLeaveApplicationViewPeriod> viewLeaveAppPeriod(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet, int docStatus) {
        DBResultSet dbrs = null;
        Hashtable result = new Hashtable();
        if (objSrcLeaveApp == null) {
            return result;
        }
        try {
            
            String sql =
                    " SELECT DISTINCT "
                    + " ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " AS leave_application_id ,"
                    + " ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " AS employee_id,"
                    + " al.al_start_date  AS al_start_date,"
                    + " al.al_end_date  AS al_end_date,"
                    + " ll.ll_start_date  AS ll_start_date,"
                    + " ll.ll_end_date  AS ll_end_date,"
                    + " dp.dp_start_date  AS  dp_start_date,"
                    + " dp.dp_end_date  AS dp_end_date,"
                    + " sp.sp_start_date  AS sp_start_date,"
                    + " sp.sp_end_date  AS sp_end_date "
                    + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " ap "
                    + " LEFT JOIN hr_view_al_taken_period al ON ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = al.leave_application_id "
                    + " LEFT JOIN hr_view_ll_taken_period ll ON  ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = ll.leave_application_id "
                    + " LEFT JOIN hr_view_dp_taken_period dp ON ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = dp.leave_application_id "
                    + " LEFT JOIN hr_view_sp_taken_period sp ON ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = sp.leave_application_id "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS emp ON emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL + " AS pbl ON pbl." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID] + " = ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " emp." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " emp." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }

            //update by satrya 2013-09-19
            String strCompanyId = "";
            if (objSrcLeaveApp.getCompanyId() != 0) {
                strCompanyId = strCompanyId + " emp." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + objSrcLeaveApp.getCompanyId();
            }
            //update by satrya 2013-09-19
            String strDivisionId = "";
            if (objSrcLeaveApp.getDivisionId() != 0) {
                strDivisionId = strDivisionId + " emp." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + objSrcLeaveApp.getDivisionId();
            }
            
            String strDepartment = "";
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                strDepartment = strDepartment + " emp." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApp.getPositionId() != 0) {
                strPosition = strPosition + " emp." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApp.getSectionId() != 0) {
                strSection = strSection + " emp." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApp.getApprovalStatus()) {
                
                case 0:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String strApprovalStatusHR = "";
            switch (objSrcLeaveApp.getApprovalHRMan()) {
                
                case 0:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            //update by satrya 2013-03-13
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + " pbl." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                } else {
                    whereClause = whereClause + " pbl." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                }
            }
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            
            if (strCompanyId != null && strCompanyId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCompanyId;
                } else {
                    whereClause = whereClause + strCompanyId;
                }
            }
            
            if (strDivisionId != null && strDivisionId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDivisionId;
                } else {
                    whereClause = whereClause + strDivisionId;
                }
            }
            
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatusHR;
                } else {
                    whereClause = whereClause + strApprovalStatusHR;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            } else {
                
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            }
            Date nowDate = new Date();
            String strNowDate = Formater.formatDate(nowDate, "yyyy-MM-dd");
            
            int maxDayExecution = 0;  /* MAX DAY EXECUTIN ON DAY */
            
            try {
                
                maxDayExecution = Integer.parseInt(PstSystemProperty.getValueByName("MAX_DAY_EXECUTION"));
            } catch (Exception e) {
                maxDayExecution = 0;
            }
            
            sql = sql + " AND NOT ((" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is null ))";
            
            sql = sql + " AND ap." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + docStatus;
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println("SQL Leave Application Execution : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                SessLeaveApplicationViewPeriod sessLeaveApplicationViewPeriod = new SessLeaveApplicationViewPeriod();
                resultToObjectJoinViewPeriod(rs, sessLeaveApplicationViewPeriod);
                result.put("" + sessLeaveApplicationViewPeriod.getLeaveAppId(), sessLeaveApplicationViewPeriod);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * Create by satrya 2013-03-08 Mencari leave yg statusnya To Be app
     *
     * @param objSrcLeaveApp
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector searchLeaveApplicationDocumentStatus(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet, int DocStatus) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        if (objSrcLeaveApp == null) {
            return new Vector(1, 1);
        }
        
        
        try {
            
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + /* di hidden karena menggunakan fungsi baru update by satrya 2014-04-08 
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_START_DATE] +
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] +
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_START_DATE] +
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] +
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_START_DATE] +
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] +
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_START_DATE] +
                     ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] +*/ " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEP ON "
                    + " DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + // di hidden karena menggunakan fungsi baru update by satrya 2014-04-08" INNER JOIN " + PstViewLeaveAppPeriod.TBL_VIEW_LEAVE_APP_PERIOD + " LAP ON " + " LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LEAVE_APPLICATION_ID] + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] +
                    //update by satrya 2013-03-08
                    " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL
                    + " AS PBL ON PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }

            //update by satrya 2013-09-19
            String strCompanyId = "";
            if (objSrcLeaveApp.getCompanyId() != 0) {
                strCompanyId = strCompanyId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + objSrcLeaveApp.getCompanyId();
            }
            //update by satrya 2013-09-19
            String strDivisionId = "";
            if (objSrcLeaveApp.getDivisionId() != 0) {
                strDivisionId = strDivisionId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + objSrcLeaveApp.getDivisionId();
            }
            
            String strDepartment = "";
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApp.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApp.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            //update by satrya 2014-06-19
            String strEmployeeLeaveConfig = "";
            if (objSrcLeaveApp.getEmployeeIdLeaveConfig() != null) {
                strEmployeeLeaveConfig = strEmployeeLeaveConfig + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " IN (" + objSrcLeaveApp.getEmployeeIdLeaveConfig() + ")";
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApp.getApprovalStatus()) {
                
                case 0:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String strApprovalStatusHR = "";
            switch (objSrcLeaveApp.getApprovalHRMan()) {
                
                case 0:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            //update by satrya 2013-03-13
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                } else {
                    whereClause = whereClause + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                }
            }
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            
            if (strCompanyId != null && strCompanyId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCompanyId;
                } else {
                    whereClause = whereClause + strCompanyId;
                }
            }
            
            if (strDivisionId != null && strDivisionId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDivisionId;
                } else {
                    whereClause = whereClause + strDivisionId;
                }
            }
            
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }

            //update by satrya 2014-06-19
            if (strEmployeeLeaveConfig != null && strEmployeeLeaveConfig.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmployeeLeaveConfig;
                } else {
                    whereClause = whereClause + strEmployeeLeaveConfig;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatusHR;
                } else {
                    whereClause = whereClause + strApprovalStatusHR;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            } else {
                
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            }
            Date nowDate = new Date();
            String strNowDate = Formater.formatDate(nowDate, "yyyy-MM-dd");
            
            int maxDayExecution = 0;  /* MAX DAY EXECUTIN ON DAY */
            
            try {
                
                maxDayExecution = Integer.parseInt(PstSystemProperty.getValueByName("MAX_DAY_EXECUTION"));
            } catch (Exception e) {
                maxDayExecution = 0;
            }

            /* di hidden karena sdh menggunakan fungsi barusql = sql + " AND NOT ((" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is null AND " +
             PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is null AND " +
             PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is null AND " +
             PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is null ))";*/
            
            sql = sql + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + DocStatus;//PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE;

            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println("SQL Leave Application Execution : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                
                LeaveApplication objleaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                Employee objEmployee = new Employee();
                //ViewLeaveAppPeriod objViewLeaveAppPeriod = new ViewLeaveAppPeriod();

                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objleaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objleaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                
                vectTemp.add(objleaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);

                /* update by satrya 2014-04-08 karena sdh menggunakan fungsi baru objViewLeaveAppPeriod.setAl_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_START_DATE]));
                 objViewLeaveAppPeriod.setAl_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE]));
                 objViewLeaveAppPeriod.setLl_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_START_DATE]));
                 objViewLeaveAppPeriod.setLl_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE]));
                 objViewLeaveAppPeriod.setDp_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_START_DATE]));
                 objViewLeaveAppPeriod.setDp_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE]));
                 objViewLeaveAppPeriod.setSp_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_START_DATE]));
                 objViewLeaveAppPeriod.setSp_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE]));

                 vectTemp.add(objViewLeaveAppPeriod);*/
                
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return result;
    }

    /**
     * Keterangan: untuk mencari status Approve Create by satrya 2013-03-13
     *
     * @param objSrcLeaveApp
     * @param start
     * @param recordToGet
     * @return
     */
    public static Vector searchLeaveApplicationStatusApprove(SrcLeaveApp objSrcLeaveApp, int start, int recordToGet) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        
        if (objSrcLeaveApp == null) {
            return new Vector(1, 1);
        }
        
        try {
            
            String sql = " SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]
                    + ",APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_START_DATE]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_START_DATE]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_START_DATE]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_START_DATE]
                    + ",LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEP ON "
                    + " DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + " INNER JOIN " + PstViewLeaveAppPeriod.TBL_VIEW_LEAVE_APP_PERIOD + " LAP ON "
                    + " LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + //update by satrya 2013-03-08
                    " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL
                    + " AS PBL ON PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
            
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }
            
            
            String strCompanyId = "";
            if (objSrcLeaveApp.getCompanyId() != 0) {
                strCompanyId = strCompanyId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + objSrcLeaveApp.getCompanyId();
            }
            
            String strDivisionId = "";
            if (objSrcLeaveApp.getDivisionId() != 0) {
                strDivisionId = strDivisionId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + objSrcLeaveApp.getDivisionId();
            }
            
            
            String strDepartment = "";
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApp.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApp.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApp.getApprovalStatus()) {
                
                case 0:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String strApprovalStatusHR = "";
            switch (objSrcLeaveApp.getApprovalHRMan()) {
                
                case 0:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            //update by satrya 2013-03-13
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                } else {
                    whereClause = whereClause + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                }
            }
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }
            
            
            if (strCompanyId != null && strCompanyId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCompanyId;
                } else {
                    whereClause = whereClause + strCompanyId;
                }
            }
            
            if (strDivisionId != null && strDivisionId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDivisionId;
                } else {
                    whereClause = whereClause + strDivisionId;
                }
            }
            
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatusHR;
                } else {
                    whereClause = whereClause + strApprovalStatusHR;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            } else {
                
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            }
            Date nowDate = new Date();
            String strNowDate = Formater.formatDate(nowDate, "yyyy-MM-dd");
            
            int maxDayExecution = 0;  /* MAX DAY EXECUTIN ON DAY */
            
            try {
                
                maxDayExecution = Integer.parseInt(PstSystemProperty.getValueByName("MAX_DAY_EXECUTION"));
            } catch (Exception e) {
                maxDayExecution = 0;
            }
            
            sql = sql + " AND NOT ((" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is null ))";
            
            sql = sql + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE;
            
            if (!(start == 0 && recordToGet == 0)) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            //System.out.println("SQL Leave Application Execution : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                
                LeaveApplication objleaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                Employee objEmployee = new Employee();
                ViewLeaveAppPeriod objViewLeaveAppPeriod = new ViewLeaveAppPeriod();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objleaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objleaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objleaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objleaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objleaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objleaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                
                vectTemp.add(objleaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                objViewLeaveAppPeriod.setAl_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_START_DATE]));
                objViewLeaveAppPeriod.setAl_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE]));
                objViewLeaveAppPeriod.setLl_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_START_DATE]));
                objViewLeaveAppPeriod.setLl_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE]));
                objViewLeaveAppPeriod.setDp_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_START_DATE]));
                objViewLeaveAppPeriod.setDp_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE]));
                objViewLeaveAppPeriod.setSp_start_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_START_DATE]));
                objViewLeaveAppPeriod.setSp_end_date(rs.getDate(PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE]));
                
                vectTemp.add(objViewLeaveAppPeriod);
                
                result.add(vectTemp);
            }
            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

//    public static int countSearchLeaveApplicationExcecute(SrcLeaveApp objSrcLeaveApp) {
//
//        DBResultSet dbrs = null;
//
//        try {
//
//            String sql = " SELECT COUNT(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ")" +
//                    " FROM " + PstEmployee.TBL_HR_EMPLOYEE +
//                    " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON " +
//                    " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " +
//                    " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] +
//                    " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEP ON " +
//                    " DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
//                    " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] +
//                    " INNER JOIN " + PstViewLeaveAppPeriod.TBL_VIEW_LEAVE_APP_PERIOD + " LAP ON " +
//                    " LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LEAVE_APPLICATION_ID] +
//                    " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] +
//                    //update by satrya 2013-03-08
//                    " LEFT JOIN "+ PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL 
//                    + " AS PBL ON PBL."+PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID] 
//                    + " = APP."+PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
//
//
//            String strFullName = "";
//            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
//                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
//                if (vectName != null && vectName.size() > 0) {
//                    strFullName = strFullName + " (";
//                    for (int i = 0; i < vectName.size(); i++) {
//                        String str = (String) vectName.get(i);
//                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
//                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] +
//                                    " LIKE '%" + str.trim() + "%' ";
//                        } else {
//                            strFullName = strFullName + str.trim();
//                        }
//                    }
//                    strFullName = strFullName + ")";
//                }
//            }
//
//            String strEmpNum = "";
//            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
//                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] +
//                        " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
//            }
//
//            
//            String strCompanyId = "";
//            if (objSrcLeaveApp.getCompanyId() != 0) {
//                strCompanyId = strCompanyId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] +
//                        " = " + objSrcLeaveApp.getCompanyId();
//            }
//            
//            String strDivisionId = "";
//            if (objSrcLeaveApp.getDivisionId() != 0) {
//                strDivisionId = strDivisionId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] +
//                        " = " + objSrcLeaveApp.getDivisionId();
//            }
//            
//            String strDepartment = "";
//            if (objSrcLeaveApp.getDepartmentId() != 0) {
//                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] +
//                        " = " + objSrcLeaveApp.getDepartmentId();
//            }
//
//            String strPosition = "";
//            if (objSrcLeaveApp.getPositionId() != 0) {
//                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] +
//                        " = " + objSrcLeaveApp.getPositionId();
//            }
//
//            String strSection = "";
//            if (objSrcLeaveApp.getSectionId() != 0) {
//                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] +
//                        " = " + objSrcLeaveApp.getSectionId();
//            }
//
//            String strSubmission = "";
//            if (!objSrcLeaveApp.isSubmission()) {
//                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
//                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
//                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE] +
//                            " = \"" + strSubmissionDate + "\"";
//                }
//            }
//
//            String strApprovalStatus = "";
//            switch (objSrcLeaveApp.getApprovalStatus()) {
//
//                case 0:
//                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
//                    break;
//
//                case 1:
//                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
//                    break;
//
//                default:
//                    break;
//            }
//
//            String strApprovalStatusHR = "";
//            switch (objSrcLeaveApp.getApprovalHRMan()) {
//
//                case 0:
//                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
//                    break;
//
//                case 1:
//                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
//                    break;
//
//                default:
//                    break;
//            }
//
//
//            String whereClause = "";
//            if (strFullName != null && strFullName.length() > 0) {
//                whereClause = whereClause + strFullName;
//            }
//             //update by satrya 2013-03-13
//         if(objSrcLeaveApp.getTypePublicLeave()!=0){
//            if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + " PBL."+ PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = "+objSrcLeaveApp.getTypePublicLeave();
//            } else {
//                    whereClause = whereClause + " PBL."+ PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = "+objSrcLeaveApp.getTypePublicLeave();
//            }
//         }
//            if (strEmpNum != null && strEmpNum.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strEmpNum;
//                } else {
//                    whereClause = whereClause + strEmpNum;
//                }
//            }
//
//            
//            if (strCompanyId != null && strCompanyId.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strCompanyId;
//                } else {
//                    whereClause = whereClause + strCompanyId;
//                }
//            }
//            
//            if (strDivisionId != null && strDivisionId.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strDivisionId;
//                } else {
//                    whereClause = whereClause + strDivisionId;
//                }
//            }
//            
//            
//            if (strDepartment != null && strDepartment.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strDepartment;
//                } else {
//                    whereClause = whereClause + strDepartment;
//                }
//            }
//
//            if (strSection != null && strSection.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strSection;
//                } else {
//                    whereClause = whereClause + strSection;
//                }
//            }
//
//            if (strPosition != null && strPosition.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strPosition;
//                } else {
//                    whereClause = whereClause + strPosition;
//                }
//            }
//
//            if (strSubmission != null && strSubmission.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strSubmission;
//                } else {
//                    whereClause = whereClause + strSubmission;
//                }
//            }
//
//
//            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strApprovalStatus;
//                } else {
//                    whereClause = whereClause + strApprovalStatus;
//                }
//            }
//
//            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
//                if (whereClause != null && whereClause.length() > 0) {
//                    whereClause = whereClause + " AND " + strApprovalStatusHR;
//                } else {
//                    whereClause = whereClause + strApprovalStatusHR;
//                }
//            }
//
//            if (whereClause != null && whereClause.length() > 0) {
//                sql = sql + " WHERE " + whereClause +
//                        " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
//            }else{
//
//            sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
//            }
//            Date nowDate = new Date();
//            String strNowDate = Formater.formatDate(nowDate, "yyyy-MM-dd");
//
//            int maxDayExecution = 0;  /* MAX DAY EXECUTIN ON DAY */
//
//            try {
//
//                maxDayExecution = Integer.parseInt(PstSystemProperty.getValueByName("MAX_DAY_EXECUTION"));
//            } catch (Exception e) {
//                maxDayExecution = 0;
//            }
//            //update by satrya 2013-03-13
//if(objSrcLeaveApp.getTypePublicLeave()!=PstPublicLeaveDetail.TYPE_LEAVE){
//            sql = sql + " AND NOT ((" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is null AND " +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is null AND " +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is null AND " +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is null ) or (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "') OR (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "') OR (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "') OR (" +
//                    PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is not null AND " +
//                    " DATE_ADD(" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + ",interval " + maxDayExecution + " day )" + " > '" + strNowDate + "'))";
//}
//            sql = sql + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED;
//
//            //System.out.println("SQL Count Leave Application Execution: " + sql);
//            dbrs = DBHandler.execQueryResult(sql);
//            ResultSet rs = dbrs.getResultSet();
//
//            int count = 0;
//
//            while (rs.next()) {
//
//                count = rs.getInt(1);
//
//            }
//
//            return count;
//
//        } catch (Exception e) {
//            System.out.println("\t Exception on seach LeaveApplication : " + e);
//        } finally {
//            DBResultSet.close(dbrs);
//        }
//        return 0;
//    }
    /**
     * creatre bt satrya 20130308 Keterangan menjumlahkan yg ada to be app
     *
     * @param objSrcLeaveApp
     * @return
     */
    public static int countSearchLeaveApplicationDocStatus(SrcLeaveApp objSrcLeaveApp, int docStatus) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = " SELECT COUNT(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ")"
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEP ON "
                    + " DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + // update by satrya 2014-04-08 karena sdh memakai fungsi baru " INNER JOIN " + PstViewLeaveAppPeriod.TBL_VIEW_LEAVE_APP_PERIOD + " LAP ON " +" LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LEAVE_APPLICATION_ID] +" = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] +
                    //update by satrya 2013-03-08
                    " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL
                    + " AS PBL ON PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
            
            
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }

            //update by satrya 2013-09-19
            String strCompanyId = "";
            if (objSrcLeaveApp.getCompanyId() != 0) {
                strCompanyId = strCompanyId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + objSrcLeaveApp.getCompanyId();
            }
            //update by satrya 2013-09-19
            String strDivisionId = "";
            if (objSrcLeaveApp.getDivisionId() != 0) {
                strDivisionId = strDivisionId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + objSrcLeaveApp.getDivisionId();
            }
            
            
            String strDepartment = "";
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApp.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApp.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            
            String strEmployeeLeaveConfig = "";
            if (objSrcLeaveApp.getEmployeeIdLeaveConfig() != null && objSrcLeaveApp.getEmployeeIdLeaveConfig().length() > 0) {
                strEmployeeLeaveConfig = strEmployeeLeaveConfig + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " IN( " + objSrcLeaveApp.getEmployeeIdLeaveConfig() + ")";
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApp.getApprovalStatus()) {
                
                case 0:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String strApprovalStatusHR = "";
            switch (objSrcLeaveApp.getApprovalHRMan()) {
                
                case 0:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            //update by satrya 2013-03-13
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                } else {
                    whereClause = whereClause + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();;
                }
            }
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }



            //update by satrya 2013-09-19
            if (strCompanyId != null && strCompanyId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCompanyId;
                } else {
                    whereClause = whereClause + strCompanyId;
                }
            }

            //update by satrya 2013-09-19
            if (strDivisionId != null && strDivisionId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDivisionId;
                } else {
                    whereClause = whereClause + strDivisionId;
                }
            }
            
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            //update by satrya 2014-06-19
            if (strEmployeeLeaveConfig != null && strEmployeeLeaveConfig.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmployeeLeaveConfig;
                } else {
                    whereClause = whereClause + strEmployeeLeaveConfig;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatusHR;
                } else {
                    whereClause = whereClause + strApprovalStatusHR;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            } else {
                
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            }
            Date nowDate = new Date();
            String strNowDate = Formater.formatDate(nowDate, "yyyy-MM-dd");
            
            int maxDayExecution = 0;  /* MAX DAY EXECUTIN ON DAY */
            
            try {
                
                maxDayExecution = Integer.parseInt(PstSystemProperty.getValueByName("MAX_DAY_EXECUTION"));
            } catch (Exception e) {
                maxDayExecution = 0;
            }

            /* update by satrya 2014-04-08  sql = sql + " AND NOT ((" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is null AND " +
             PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is null AND " +
             PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is null AND " +
             PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is null ))";*/
            
            sql = sql + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + docStatus;//PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE;

            //System.out.println("SQL Count Leave Application Execution: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            int count = 0;
            
            while (rs.next()) {
                
                count = rs.getInt(1);
                
            }
            
            return count;
            
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * Keterangan: untuk mencari status approve Create by satrya ramayu
     * 2013-03-13
     *
     * @param objSrcLeaveApp
     * @return
     */
    public static int countSearchLeaveApplicationStatusApprove(SrcLeaveApp objSrcLeaveApp) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = " SELECT COUNT(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ")"
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP ON "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEP ON "
                    + " DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + " INNER JOIN " + PstViewLeaveAppPeriod.TBL_VIEW_LEAVE_APP_PERIOD + " LAP ON "
                    + " LAP." + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + //update by satrya 2013-03-08
                    " LEFT JOIN " + PstPublicLeaveDetail.TBL_PUBLIC_LEAVE_DETAIL
                    + " AS PBL ON PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID];
            
            
            String strFullName = "";
            if ((objSrcLeaveApp.getFullName() != null) && (objSrcLeaveApp.getFullName().length() > 0)) {
                Vector vectName = logicParser(objSrcLeaveApp.getFullName());
                if (vectName != null && vectName.size() > 0) {
                    strFullName = strFullName + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strFullName = strFullName + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strFullName = strFullName + str.trim();
                        }
                    }
                    strFullName = strFullName + ")";
                }
            }
            
            String strEmpNum = "";
            if ((objSrcLeaveApp.getEmpNum() != null) && (objSrcLeaveApp.getEmpNum().length() > 0)) {
                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + objSrcLeaveApp.getEmpNum() + "%'";
            }

            //update by satrya 2013-09-19
            String strCompanyId = "";
            if (objSrcLeaveApp.getCompanyId() != 0) {
                strCompanyId = strCompanyId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + objSrcLeaveApp.getCompanyId();
            }
            //update by satrya 2013-09-19
            String strDivisionId = "";
            if (objSrcLeaveApp.getDivisionId() != 0) {
                strDivisionId = strDivisionId + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + objSrcLeaveApp.getDivisionId();
            }
            
            
            String strDepartment = "";
            if (objSrcLeaveApp.getDepartmentId() != 0) {
                strDepartment = strDepartment + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + objSrcLeaveApp.getDepartmentId();
            }
            
            String strPosition = "";
            if (objSrcLeaveApp.getPositionId() != 0) {
                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + objSrcLeaveApp.getPositionId();
            }
            
            String strSection = "";
            if (objSrcLeaveApp.getSectionId() != 0) {
                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + objSrcLeaveApp.getSectionId();
            }
            
            String strSubmission = "";
            if (!objSrcLeaveApp.isSubmission()) {
                if ((objSrcLeaveApp.getSubmissionDate() != null) && (objSrcLeaveApp.getSubmissionDate() != null)) {
                    String strSubmissionDate = Formater.formatDate(objSrcLeaveApp.getSubmissionDate(), "yyyy-MM-dd");
                    strSubmission = strSubmission + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]
                            + " = \"" + strSubmissionDate + "\"";
                }
            }
            
            String strApprovalStatus = "";
            switch (objSrcLeaveApp.getApprovalStatus()) {
                
                case 0:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatus = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            String strApprovalStatusHR = "";
            switch (objSrcLeaveApp.getApprovalHRMan()) {
                
                case 0:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " = 0";
                    break;
                
                case 1:
                    //strApprovalStatusHR = " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL] + " <> 0";
                    break;
                
                default:
                    break;
            }
            
            
            String whereClause = "";
            if (strFullName != null && strFullName.length() > 0) {
                whereClause = whereClause + strFullName;
            }
            //update by satrya 2013-03-13
            if (objSrcLeaveApp.getTypePublicLeave() != 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();
                } else {
                    whereClause = whereClause + " PBL." + PstPublicLeaveDetail.fieldNames[PstPublicLeaveDetail.FLD_TYPE_LEAVE_ID] + " = " + objSrcLeaveApp.getTypePublicLeave();;
                }
            }
            if (strEmpNum != null && strEmpNum.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strEmpNum;
                } else {
                    whereClause = whereClause + strEmpNum;
                }
            }

            //update by satrya 2013-09-19
            if (strCompanyId != null && strCompanyId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strCompanyId;
                } else {
                    whereClause = whereClause + strCompanyId;
                }
            }
            //update by satrya 2013-09-19
            if (strDivisionId != null && strDivisionId.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDivisionId;
                } else {
                    whereClause = whereClause + strDivisionId;
                }
            }
            
            
            if (strDepartment != null && strDepartment.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strDepartment;
                } else {
                    whereClause = whereClause + strDepartment;
                }
            }
            
            if (strSection != null && strSection.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSection;
                } else {
                    whereClause = whereClause + strSection;
                }
            }
            
            if (strPosition != null && strPosition.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strPosition;
                } else {
                    whereClause = whereClause + strPosition;
                }
            }
            
            if (strSubmission != null && strSubmission.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strSubmission;
                } else {
                    whereClause = whereClause + strSubmission;
                }
            }
            
            
            if (strApprovalStatus != null && strApprovalStatus.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatus;
                } else {
                    whereClause = whereClause + strApprovalStatus;
                }
            }
            
            if (strApprovalStatusHR != null && strApprovalStatusHR.length() > 0) {
                if (whereClause != null && whereClause.length() > 0) {
                    whereClause = whereClause + " AND " + strApprovalStatusHR;
                } else {
                    whereClause = whereClause + strApprovalStatusHR;
                }
            }
            
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause
                        + " AND (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            } else {
                
                sql = sql + " WHERE (" + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + ")";
            }
            Date nowDate = new Date();
            String strNowDate = Formater.formatDate(nowDate, "yyyy-MM-dd");
            
            int maxDayExecution = 0;  /* MAX DAY EXECUTIN ON DAY */
            
            try {
                
                maxDayExecution = Integer.parseInt(PstSystemProperty.getValueByName("MAX_DAY_EXECUTION"));
            } catch (Exception e) {
                maxDayExecution = 0;
            }
            
            sql = sql + " AND NOT ((" + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_AL_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_LL_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_SP_END_DATE] + " is null AND "
                    + PstViewLeaveAppPeriod.fieldNames[PstViewLeaveAppPeriod.FLD_DP_END_DATE] + " is null ))";
            
            sql = sql + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE;

            //System.out.println("SQL Count Leave Application Execution: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            int count = 0;
            
            while (rs.next()) {
                
                count = rs.getInt(1);
                
            }
            
            return count;
            
        } catch (Exception e) {
            System.out.println("\t Exception on seach LeaveApplication : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @param leaveAppId
     * @return
     * @DESC untuk mendapatkan status schedule
     */
    public static boolean CekScheduleExist(long leaveAppId) {
        
        LeaveApplication leaveApplication = new LeaveApplication();
        
        try {
            leaveApplication = PstLeaveApplication.fetchExc(leaveAppId);
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        
        Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
        Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
        Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
        Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());

        /*------------------ START MENGECEK KEBERADAAN SCHEDULE ------------------------------*/
        
        boolean KeberadaanSchedule = true;
        
        for (int TstAl = 0; TstAl < listTknAl.size(); TstAl++) {
            
            AlStockTaken TstalStockTaken = (AlStockTaken) listTknAl.get(TstAl);
            Date strtDate = TstalStockTaken.getTakenDate();
            Date newDate = new Date();
            
            for (int idxTstAlTkn = 0; idxTstAlTkn < TstalStockTaken.getTakenQty(); idxTstAlTkn++) {
                
                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                
                if (schId == 0) {
                    
                    KeberadaanSchedule = false;
                    
                }
                
                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                newDate = new Date(tmpDate);
                strtDate = newDate;
            }
        }
        
        for (int TstLl = 0; TstLl < listTknLl.size(); TstLl++) {
            
            LlStockTaken TstllStockTaken = (LlStockTaken) listTknLl.get(TstLl);
            Date strtDate = TstllStockTaken.getTakenDate();
            Date newDate = new Date();
            
            for (int idxTstLlTkn = 0; idxTstLlTkn < TstllStockTaken.getTakenQty(); idxTstLlTkn++) {
                
                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                
                if (schId == 0) {
                    KeberadaanSchedule = false;
                }
                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                newDate = new Date(tmpDate);
                strtDate = newDate;
                
            }
            
        }
        
        for (int TstDp = 0; TstDp < listTknDp.size(); TstDp++) {
            
            DpStockTaken TstdpStockTaken = (DpStockTaken) listTknDp.get(TstDp);
            Date strtDate = TstdpStockTaken.getTakenDate();
            Date newDate = new Date();
            
            for (int idxTstDpTkn = 0; idxTstDpTkn < TstdpStockTaken.getTakenQty(); idxTstDpTkn++) {
                
                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                
                if (schId == 0) {
                    
                    KeberadaanSchedule = false;
                    
                }
                
                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                newDate = new Date(tmpDate);
                strtDate = newDate;
                
            }
        }
        
        for (int TstidxSp = 0; TstidxSp < listTknSp.size(); TstidxSp++) {
            
            SpecialUnpaidLeaveTaken TstspecialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(TstidxSp);
            Date strtDate = TstspecialUnpaidLeaveTaken.getTakenDate();
            Date newDate = new Date();
            
            for (int TstidxSpTkn = 0; TstidxSpTkn < TstspecialUnpaidLeaveTaken.getTakenQty(); TstidxSpTkn++) {
                
                long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                
                if (schId == 0) {
                    
                    KeberadaanSchedule = false;
                    
                }
                
                long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                newDate = new Date(tmpDate);
                strtDate = newDate;
            }
        }
        
        if (KeberadaanSchedule) {
            return true;
        }
        
        return false;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk memproses eksekusi leave application
     * @param leaveAppId
     * @return
     */
    public static boolean processExecute(long leaveAppId) {
        
        try {
            
            LeaveApplication leaveApplication = new LeaveApplication();
            
            try {
                leaveApplication = PstLeaveApplication.fetchExc(leaveAppId);
                //update by satrya 2013-12-11
                I_Leave leaveConfig = null;
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
                if (leaveConfig != null && leaveConfig.getConfigurationLeaveApprovall() == I_Leave.LEAVE_CONFIG_AFTER_APPROVALL_HRD_YES_EXECUTE) {
                    //tidak ada prosess apa"
                } else if (leaveApplication.getDocStatus() != PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {
                    return false;
                }
                /**
                 * update by satrya 2013-12-11
                 * if(leaveApplication.getDocStatus()!=PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED){
                 * return false; }
                 */
            } catch (Exception e) {
                System.out.println("EXCEPTION " + e.toString());
            }
            
            AlStockTaken alStockTaken = new AlStockTaken();
            LlStockTaken llStockTaken = new LlStockTaken();
            DpStockTaken dpStockTaken = new DpStockTaken();
			SpecialStockTaken ssStockTaken = new SpecialStockTaken();
            SpecialUnpaidLeaveTaken specialUnpaidLeaveTaken = new SpecialUnpaidLeaveTaken();
            
            Vector listTknAl = SessLeaveApplication.getTakenAl(leaveApplication.getOID());
            Vector listTknLl = SessLeaveApplication.getTakenLl(leaveApplication.getOID());
            Vector listTknDp = SessLeaveApplication.getTakenDp(leaveApplication.getOID());
            Vector listTknSp = SessLeaveApplication.getTakenSpcUnpaid(leaveApplication.getOID());
			Vector listTknSs = SessLeaveApplication.getTakenSs(leaveApplication.getOID());
            
            String oidAl = String.valueOf(PstSystemProperty.getValueByName("OID_AL"));
            String oidLl = String.valueOf(PstSystemProperty.getValueByName("OID_LL"));
            String oidDp = String.valueOf(PstSystemProperty.getValueByName("OID_DP"));

            //double Qty_taken_al = 0;
            //double Qty_taken_ll = 0;
            //double Qty_taken_sp = 0;
            //double Qty_taken_dp = 0;

            /*------------------ START MENGECEK KEBERADAAN SCHEDULE ------------------------------*/
            
            boolean KeberadaanSchedule = true;
            
            for (int TstAl = 0; TstAl < listTknAl.size(); TstAl++) {
                
                AlStockTaken TstalStockTaken = (AlStockTaken) listTknAl.get(TstAl);
                Date strtDate = TstalStockTaken.getTakenDate();
                Date newDate = new Date();
                
                for (int idxTstAlTkn = 0; idxTstAlTkn < TstalStockTaken.getTakenQty(); idxTstAlTkn++) {
                    
                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                    
                    if (schId == 0) {
                        KeberadaanSchedule = false;
                    }
                    
                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                    newDate = new Date(tmpDate);
                    strtDate = newDate;
                }
            }
            
            for (int TstLl = 0; TstLl < listTknLl.size(); TstLl++) {
                
                LlStockTaken TstllStockTaken = (LlStockTaken) listTknLl.get(TstLl);
                Date strtDate = TstllStockTaken.getTakenDate();
                Date newDate = new Date();
                
                for (int idxTstLlTkn = 0; idxTstLlTkn < TstllStockTaken.getTakenQty(); idxTstLlTkn++) {
                    
                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                    
                    if (schId == 0) {
                        KeberadaanSchedule = false;
                    }
                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                    newDate = new Date(tmpDate);
                    strtDate = newDate;
                }
                
            }
            
            for (int TstDp = 0; TstDp < listTknDp.size(); TstDp++) {
                
                DpStockTaken TstdpStockTaken = (DpStockTaken) listTknDp.get(TstDp);
                Date strtDate = TstdpStockTaken.getTakenDate();
                Date newDate = new Date();
                
                for (int idxTstDpTkn = 0; idxTstDpTkn < TstdpStockTaken.getTakenQty(); idxTstDpTkn++) {
                    
                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                    
                    if (schId == 0) {
                        
                        KeberadaanSchedule = false;
                        
                    }
                    
                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                    newDate = new Date(tmpDate);
                    strtDate = newDate;
                    
                }
            }
            
            for (int TstidxSp = 0; TstidxSp < listTknSp.size(); TstidxSp++) {
                
                SpecialUnpaidLeaveTaken TstspecialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(TstidxSp);
                Date strtDate = TstspecialUnpaidLeaveTaken.getTakenDate();
                Date newDate = new Date();
                
                for (int TstidxSpTkn = 0; TstidxSpTkn < TstspecialUnpaidLeaveTaken.getTakenQty(); TstidxSpTkn++) {
                    
                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                    
                    if (schId == 0) {
                        
                        KeberadaanSchedule = false;
                        
                    }
                    
                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                    newDate = new Date(tmpDate);
                    strtDate = newDate;
                }
            }
			
			for (int TstidxSs = 0; TstidxSs < listTknSs.size(); TstidxSs++) {
                
                SpecialStockTaken TstspecialStockLeaveTaken = (SpecialStockTaken) listTknSs.get(TstidxSs);
                Date strtDate = TstspecialStockLeaveTaken.getTakenDate();
                Date newDate = new Date();
                
                for (int TstidxSsTkn = 0; TstidxSsTkn < TstspecialStockLeaveTaken.getTakenQty(); TstidxSsTkn++) {
                    
                    long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                    
                    if (schId == 0) {
                        
                        KeberadaanSchedule = false;
                        
                    }
                    
                    long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                    newDate = new Date(tmpDate);
                    strtDate = newDate;
                }
            }

            /*------------------ END KEBERADAAN SCHEDULE ------------------------------*/

            //update by priska 2014-12-24
            //ambill value AL dan DP dari system properties
            long intAl = -1;
            try {
                String sintAl = PstSystemProperty.getValueByName("VALUE_ANNUAL_LEAVE");                
                intAl = Integer.parseInt(sintAl);
            } catch (Exception ex) {
                System.out.println("VALUE_ANNUAL_LEAVE NOT Be SET" + ex);
                intAl = -1;
            }
            
            long intDp = -1;
            try {
                String sintDp = PstSystemProperty.getValueByName("VALUE_DAY_OF_PAYMENT");                
                intDp = Integer.parseInt(sintDp);
            } catch (Exception ex) {
                System.out.println("VALUE_DAY_OF_PAYMENT NOT Be SET" + ex);
                intDp = -1;
            }
            
            
            if (KeberadaanSchedule == true) {     // kondisi dimana semua schedule ditemukan

                try {
                    String sql = "UPDATE " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " SET "
                            + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                            + " WHERE " + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + "=" + leaveAppId;
                    
                    int i = DBHandler.execUpdate(sql);

                    // System.out.println("SQL update doc status " + sql);

                } catch (Exception e) {
                    System.out.println("[ exception ] SessLeaveApplication.processExecute() " + e.toString());
                }
                
                Vector listEmpSchedule = new Vector();
                
                for (int idxAl = 0; idxAl < listTknAl.size(); idxAl++) {
                    
                    alStockTaken = (AlStockTaken) listTknAl.get(idxAl);
                    Date strtDate = alStockTaken.getTakenDate();
                    Date newDate = new Date();
                    //priska 2014-12-31 menambhkan automatis al di schedule
                    Date strtDatenew = (Date) strtDate.clone();
                    Date newDatenew = new Date();
                    
                    procesTakenAl(alStockTaken.getAlStockId(), alStockTaken.getTakenQty());
                    int dayDiff = (int) DateCalc.dayDifference(alStockTaken.getTakenDate(),alStockTaken.getTakenFinnishDate());
                    for (int idxAlTkn = 0; idxAlTkn < (dayDiff+1); idxAlTkn++) {

                        // Qty_taken_al=Qty_taken_al+ alStockTaken.getTakenQty();
                        long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                        
                        if (schId != 0) {
                            
                            boolean same = false;
                            
                            if (listEmpSchedule != null && listEmpSchedule.size() > 0) {
                                
                                for (int i = 0; i < listEmpSchedule.size(); i++) {
                                    
                                    EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                    objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);
                                    
                                    if (objEmpScheduleProcess.getOID() == schId) {
                                        same = true;
                                    }
                                }
                            }
                            
                            if (same == false) {
                                EmpSchedule empScheduleProcess = new EmpSchedule();
                                empScheduleProcess.setOID(schId);
                                listEmpSchedule.add(empScheduleProcess);
                            }
                            
                            int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidAl);
                            
                        }
                        long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                        newDate = new Date(tmpDate);
                        strtDate = newDate;
                    }

                    if (intAl > -1 || intAl != -1){
                    //menambahkan AL ke schedule
                    for (int idxAlTkn = 0; idxAlTkn < (dayDiff + 1); idxAlTkn++) {
                        long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDatenew);
                        
                        if (schId != 0) {
                            int statusReason = SessEmpSchedule.updateScheduleReason(strtDatenew, schId, intAl);
                        }
                        long tmpDate = strtDatenew.getTime() + (24 * 60 * 60 * 1000);
                        newDatenew = new Date(tmpDate);
                        strtDatenew = newDatenew;
                    }
                    }
                }
                
                for (int idxLl = 0; idxLl < listTknLl.size(); idxLl++) {
                    
                    llStockTaken = (LlStockTaken) listTknLl.get(idxLl);
                    Date strtDate = llStockTaken.getTakenDate();
                    Date newDate = new Date();
                    
                    procesTakenLl(llStockTaken.getLlStockId(), llStockTaken.getTakenQty());
                    int dayDiff = (int) DateCalc.dayDifference(llStockTaken.getTakenDate(),llStockTaken.getTakenFinnishDate());
                    for (int idxLlTkn = 0; idxLlTkn < (dayDiff+1); idxLlTkn++) {
                        //Qty_taken_ll=Qty_taken_ll+llStockTaken.getTakenQty();
                        long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                        
                        if (schId != 0) {
                            
                            boolean same = false;
                            
                            if (listEmpSchedule != null && listEmpSchedule.size() > 0) {
                                
                                for (int i = 0; i < listEmpSchedule.size(); i++) {
                                    
                                    EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                    objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);
                                    
                                    if (objEmpScheduleProcess.getOID() == schId) {
                                        same = true;
                                    }
                                }
                            }
                            
                            if (same == false) {
                                EmpSchedule empScheduleProcess = new EmpSchedule();
                                empScheduleProcess.setOID(schId);
                                listEmpSchedule.add(empScheduleProcess);
                            }
                            
                            int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidLl);
                            
                        }
                        long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                        newDate = new Date(tmpDate);
                        strtDate = newDate;
                    }
                }
                
                for (int idxDp = 0; idxDp < listTknDp.size(); idxDp++) {
                    
                    dpStockTaken = (DpStockTaken) listTknDp.get(idxDp);
                    Date strtDate = dpStockTaken.getTakenDate();
                    //priska 2014-12-31
                    Date strtDatenew = (Date) strtDate.clone();
                    Date newDatenew = new Date();
                    
                    Date newDate = new Date();

                    //prosess update Taken ke DP_Management
                    procesTakenDp(dpStockTaken.getDpStockId(), dpStockTaken.getTakenQty());
                    int dayDiff = (int) DateCalc.dayDifference(dpStockTaken.getTakenDate(),dpStockTaken.getTakenFinnishDate());
                    
                    for (int idxDpTkn = 0; idxDpTkn < (dayDiff+1); idxDpTkn++) {
                        //Qty_taken_dp++;
                        long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                        if (schId != 0) {
                            boolean same = false;
                            if (listEmpSchedule != null && listEmpSchedule.size() > 0) {
                                for (int i = 0; i < listEmpSchedule.size(); i++) {
                                    
                                    EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                    objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);
                                    
                                    if (objEmpScheduleProcess.getOID() == schId) {
                                        same = true;
                                    }
                                }
                            }
                            
                            if (same == false) {
                                EmpSchedule empScheduleProcess = new EmpSchedule();
                                empScheduleProcess.setOID(schId);
                                listEmpSchedule.add(empScheduleProcess);
                            }
                            
                            int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidDp);
                            //int statusReason = SessEmpSchedule.updateScheduleReason(strtDate, schId, intAl);
                        }
                        
                        long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                        newDate = new Date(tmpDate);
                        strtDate = newDate;
                    }

                    //priska 2014-12-31
                    if (intDp > -1 || intDp != -1){
                    for (int idxDpTkn = 0; idxDpTkn < (dayDiff + 1); idxDpTkn++) {
                        //Qty_taken_dp++;
                        long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDatenew);
                        if (schId != 0) {
                            int statusReason = SessEmpSchedule.updateScheduleReason(strtDatenew, schId, intAl);
                        }
                        
                        long tmpDate = strtDatenew.getTime() + (24 * 60 * 60 * 1000);
                        newDatenew = new Date(tmpDate);
                        strtDatenew = newDatenew;
                    }
                    }
                    
                }
                
                for (int idxSp = 0; idxSp < listTknSp.size(); idxSp++) {
                    
                    specialUnpaidLeaveTaken = (SpecialUnpaidLeaveTaken) listTknSp.get(idxSp);
                    Date strtDate = specialUnpaidLeaveTaken.getTakenDate();
                    Date newDate = new Date();
                    String oidSp = "" + specialUnpaidLeaveTaken.getScheduledId();
                    int dayDiff = (int) DateCalc.dayDifference(specialUnpaidLeaveTaken.getTakenDate(),specialUnpaidLeaveTaken.getTakenFinnishDate());
                    
                    for (int idxSpTkn = 0; idxSpTkn < (dayDiff+1); idxSpTkn++) {
                        //Qty_taken_sp++;
                        long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                        if (schId != 0) {
                            
                            boolean same = false;
                            
                            if (listEmpSchedule != null && listEmpSchedule.size() > 0) {
                                
                                for (int i = 0; i < listEmpSchedule.size(); i++) {
                                    
                                    EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                    objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);
                                    
                                    if (objEmpScheduleProcess.getOID() == schId) {
                                        same = true;
                                    }
                                }
                            }
                            
                            if (same == false) {
                                EmpSchedule empScheduleProcess = new EmpSchedule();
                                empScheduleProcess.setOID(schId);
                                listEmpSchedule.add(empScheduleProcess);
                            }
                            
                            int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidSp);
                            
                        }
                        long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                        newDate = new Date(tmpDate);
                        strtDate = newDate;
                    }
                }
				
				for (int idxSs = 0; idxSs < listTknSs.size(); idxSs++) {
                    
                    ssStockTaken = (SpecialStockTaken) listTknSs.get(idxSs);
                    Date strtDate = ssStockTaken.getTakenDate();
                    Date newDate = new Date();
                    String oidSs = "" + ssStockTaken.getScheduleId();
                    int dayDiff = (int) DateCalc.dayDifference(ssStockTaken.getTakenDate(),ssStockTaken.getTakenFinishDate());
                    
                    for (int idxSsTkn = 0; idxSsTkn < (dayDiff+1); idxSsTkn++) {
                        //Qty_taken_sp++;
                        long schId = SessEmpSchedule.getSchId(leaveApplication.getEmployeeId(), strtDate);
                        if (schId != 0) {
                            
                            boolean same = false;
                            
                            if (listEmpSchedule != null && listEmpSchedule.size() > 0) {
                                
                                for (int i = 0; i < listEmpSchedule.size(); i++) {
                                    
                                    EmpSchedule objEmpScheduleProcess = new EmpSchedule();
                                    objEmpScheduleProcess = (EmpSchedule) listEmpSchedule.get(i);
                                    
                                    if (objEmpScheduleProcess.getOID() == schId) {
                                        same = true;
                                    }
                                }
                            }
                            
                            if (same == false) {
                                EmpSchedule empScheduleProcess = new EmpSchedule();
                                empScheduleProcess.setOID(schId);
                                listEmpSchedule.add(empScheduleProcess);
                            }
                            
                            int status = SessEmpSchedule.updateSchedule(strtDate, schId, oidSs);
                            
                        }
                        long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
                        newDate = new Date(tmpDate);
                        strtDate = newDate;
                    }
                }

                /*untuk memproses schedule */
                
                if (listEmpSchedule != null & listEmpSchedule.size() > 0) {
                    
                    for (int k = 0; k < listEmpSchedule.size(); k++) {
                        
                        EmpSchedule objAktifSchedule = new EmpSchedule();
                        objAktifSchedule = (EmpSchedule) listEmpSchedule.get(k);
                        
                        EmpSchedule objSchedule = new EmpSchedule();
                        
                        try {
                            objSchedule = PstEmpSchedule.fetchExc(objAktifSchedule.getOID());
                        } catch (Exception e) {
                            System.out.println("Exeption " + e.toString());
                        }
                        
                        Period period = new Period();
                        
                        try {
                            period = PstPeriod.fetchExc(objSchedule.getPeriodId());
                        } catch (Exception e) {
                            System.out.println("Exception " + e.toString());
                        }
                        
                        int diffPeriod = 0;
                        
                        diffPeriod = SessLeaveApplication.DATEDIFF(period.getEndDate(), period.getStartDate());
                        
                        diffPeriod = diffPeriod + 1;
                        
                        Date startDate = new Date();
                        
                        Date newDate = new Date();
                        
                        startDate = period.getStartDate();
                        
                        for (int x = 0; x < diffPeriod; x++) {
                            
                            AbsenceAnalyser.checkEmployeeAbsenceAutomatic(startDate, leaveApplication.getEmployeeId());
                            LatenessAnalyser.EmployeeLateness(startDate, leaveApplication.getEmployeeId());
                            
                            long tmpDate = startDate.getTime() + (24 * 60 * 60 * 1000);
                            newDate = new Date(tmpDate);
                            startDate = newDate;
                        }
                    }
                    
                    
                }
                
                
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return false;
    }
    
    public static void procesTakenAl(long Stock_id, float qty_taken) {
        
        AlStockManagement alStockManagement = new AlStockManagement();
        
        try {
            alStockManagement = PstAlStockManagement.fetchExc(Stock_id);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        float nw_tkn = alStockManagement.getQtyUsed() + qty_taken;
        
        try {
            
            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + "=" + nw_tkn
                    + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]
                    + " = " + Stock_id;
            
            System.out.println("nw" + nw_tkn);
            int ix = DBHandler.execUpdate(sql);
            
            
        } catch (Exception e) {
            System.out.println("[ exception ] SessLeaveApplication.procesTakenAl() : " + e.toString());
        }
    }
    
    public static void procesTakenLl(long Stock_id, float qty_taken) {
        
        LLStockManagement llStockManagement = new LLStockManagement();
        
        try {
            llStockManagement = PstLLStockManagement.fetchExc(Stock_id);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        float nw_tkn = llStockManagement.getQtyUsed() + qty_taken;
        
        try {
            
            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + "=" + nw_tkn
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = " + Stock_id;
            
            int ix = DBHandler.execUpdate(sql);
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    /**
     * Keterangan : prosess Update Qty_residue
     *
     * @param stock_id
     * @param dp_tkn
     */
    public static void procesTakenDp(long stock_id, float dp_tkn) {
        
        DBResultSet dbrs = null;
        DpStockManagement dpStockManagement = new DpStockManagement();
        
        try {
            dpStockManagement = PstDpStockManagement.fetchExc(stock_id);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        float qty_residue = 0;
        float qty_taken = 0;
        
        qty_taken = dpStockManagement.getQtyUsed() + dp_tkn;
        qty_residue = dpStockManagement.getiDpQty() - qty_taken;
        
        int sts = dpStockManagement.getiDpStatus();
        
        if (qty_residue <= 0) {
            
            sts = PstDpStockManagement.DP_STS_TAKEN;
            
        }
        
        try {
            
            String sql = "UPDATE " + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " SET "
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_USED] + "="
                    + qty_taken + ","
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_RESIDUE] + "="
                    + qty_residue + ","
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STATUS] + "="
                    + sts + " WHERE "
                    + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + "=" + stock_id;
            
            int ix = DBHandler.execUpdate(sql);
            
        } catch (Exception e) {
            System.out.println("Exception procesTakenDp update to DP_STOCK_MANAGEMENT" + e.toString());
        }
    }

    /**
     * *
     * @Author Roy Andika
     * @param employee_id
     * @return
     */
    public static boolean statusAlStockNotExist(long employee_id) {
        
        if (employee_id == 0) {
            return true;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id + " AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return true;
    }

    /*
     * AUTHOR   : ROY A
     * DESC     : UNTUK MENDAPATKAN STATUS Al yang aktif berdasarkan employee yang dimasukan dalam parameter
     * 
     */
    public static long OIDALStockManagement(long employee_id) {
        
        if (employee_id == 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id + " AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                long alId = rs.getLong(1);
                return alId;
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        return 0;
    }
    
    public static boolean statusLlStockNotExist(long employee_id) {
        
        if (employee_id == 0) {
            return true;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                return false;
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return true;
    }
    
    public static Vector listEmployeeSearcApplication() {
        
        DBResultSet dbrs = null;
        
        try {


            //String sql = "SELECT "+PstLeaveApplication.fieldNames[PstLeaveApplication.];

        } catch (Exception e) {
            System.out.println("ESCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        
        return null;
    }
    
    public static boolean GetAktifLLManagement(long ll_stock_id) {
        
        LLStockManagement llStockManagement = new LLStockManagement();
        
        try {
            
            llStockManagement = PstLLStockManagement.fetchExc(ll_stock_id);
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        
        if (llStockManagement.getExpiredDate() != null && llStockManagement.getExpiredDate2() != null) {
            
            if (llStockManagement.getExpiredDate().getTime() >= llStockManagement.getExpiredDate2().getTime()) {
                
                if (llStockManagement.getExpiredDate().getTime() < new Date().getTime()) {
                    
                    return true;
                    
                }
                
            } else {
                
                if (llStockManagement.getExpiredDate2().getTime() < new Date().getTime()) {
                    
                    return true;
                    
                }
                
            }
            
            
        } else if (llStockManagement.getExpiredDate() != null) {
            
            if (new Date().getTime() > llStockManagement.getExpiredDate().getTime()) {
                
                return true;
                
            }
        } else if (llStockManagement.getExpiredDate2() != null) {
            
            if (new Date().getTime() > llStockManagement.getExpiredDate2().getTime()) {
                
                return true;
                
            }
        }
        
        return false;
    }

    /**
     * @AUTHOR : ROY A.
     * @DESC : UNTUK MENDAPATKAN STATUS LL AKTIF ATAU TIDAK
     * @OUT : TRUE - > EXPIRED ; FALSE - > NOT EXPIRED
     */
    public static boolean getStatusLLExp(long employee_id) {
        
        String whereClause = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id + " AND "
                + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF;
        
        Vector listLLStock = PstLLStockManagement.list(0, 0, whereClause, null);
        
        LLStockManagement llStockManagement = new LLStockManagement();
        
        if (listLLStock != null && listLLStock.size() > 0) {
            
            llStockManagement = (LLStockManagement) listLLStock.get(0);
            
            if (llStockManagement.getExpiredDate2() != null) {
                
                if (llStockManagement.getExpiredDate().getTime() < new Date().getTime() && llStockManagement.getExpiredDate2().getTime() < new Date().getTime()) {
                    
                    return true;
                    
                } else {
                    
                    return false;
                    
                }
            } else {
                if (llStockManagement.getExpiredDate().getTime() < new Date().getTime()) {
                    
                    return true;
                    
                } else {
                    
                    return false;
                    
                }
                
            }
            
        } else {
            
            return false;
            
        }
    }

    /**
     * @AUTHOR : ROY A.
     * @DESC : UNTUK MENDAPATKAN STATUS LL AKTIF ATAU TIDAK
     * @OUT : TRUE - > EXPIRED ; FALSE - > NOT EXPIRED
     */
    public static boolean getStatusLLExpired(long employee_id) {
        
        String whereClause = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id + " AND "
                + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF;
        
        Vector listLLStock = PstLLStockManagement.list(0, 0, whereClause, null);
        
        LLStockManagement llStockManagement = new LLStockManagement();
        /*if((llStockManagement.getExpiredDate()==null) ||  (llStockManagement.getExpiredDate2()==null)){
         return true;
         }*/
        if (listLLStock != null && listLLStock.size() > 0) {            
            llStockManagement = (LLStockManagement) listLLStock.get(0);
            if (llStockManagement.getExpiredDate2() != null) {
                
                if (llStockManagement.getExpiredDate2().getTime() < new Date().getTime()) {
                    
                    return true;
                    
                } else {
                    
                    return false;
                    
                }
            } else {
                if (llStockManagement.getExpiredDate().getTime() < new Date().getTime()) {
                    
                    return true;
                    
                } else {
                    
                    return false;
                    
                }
                
            }
            
        } else {
            
            return false;
            
        }
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN OID SCHEDULE SPECIAL LEAVE
     */
    public static Vector ListScheduleSpecialLeave() {
        
        DBResultSet dbrs = null;
        
        long specialOID = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName("OID_SPECIAL")));
        
        try {
            
            String sql = "SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + " = " + specialOID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector listSpecialOID = new Vector();
            
            while (rs.next()) {
                
                long OidSp = rs.getLong(1);
                listSpecialOID.add("" + OidSp);
                
            }
            
            return listSpecialOID;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString()+ " "+specialOID);
        }
        
        return null;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN OID SCHEDULE UNPAID LEAVE
     */
    public static Vector ListScheduleUnpLeave() {
        
        DBResultSet dbrs = null;
        
        long unpOID = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName("OID_UNPAID")));
        
        try {
            
            String sql = "SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + " = " + unpOID;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector listSpecialOID = new Vector();
            
            while (rs.next()) {
                
                long OidSp = rs.getLong(1);
                listSpecialOID.add("" + OidSp);
                
            }
            
            return listSpecialOID;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }
    
    private static Vector listOidEmployee(SrcLeaveManagement srcLeaveManagement) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            if (srcLeaveManagement.getEmpName().length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " like '%" + srcLeaveManagement.getEmpName() + "%'";
            }
            
            if (srcLeaveManagement.getEmpNum().length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " like '%" + srcLeaveManagement.getEmpNum() + "%'";
            }
            
            if (srcLeaveManagement.getEmpDeptId() != 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + srcLeaveManagement.getEmpDeptId();
            }
            
            if (srcLeaveManagement.getEmpLevelId() != 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = " + srcLeaveManagement.getEmpLevelId();
            }
            
            if (srcLeaveManagement.getEmpSectionId() != 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + srcLeaveManagement.getEmpSectionId();
            }
            
            sql = sql + " ORDER BY " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector result = new Vector();
            
            while (rs.next()) {
                
                Employee employee = new Employee();
                
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                result.add(employee);
                
            }
            
            return result;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }
    //update by devin 2014-04-16

    /**
     * digunakan untuk mencari employee dengan menambahkan parameter section
     *
     * @param srcLeaveManagement
     * @return
     */
    private static Vector listOidEmployeeBySection(SrcLeaveManagement srcLeaveManagement, Vector section, int valueSection) {
        
        DBResultSet dbrs = null;
        String variable = "";
        
        try {
            
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE
                    + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            if (srcLeaveManagement.getEmpName().length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " like '%" + srcLeaveManagement.getEmpName() + "%'";
            }
            
            if (srcLeaveManagement.getEmpNum().length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " like '%" + srcLeaveManagement.getEmpNum() + "%'";
            }
            
            if (srcLeaveManagement.getEmpDeptId() != 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + srcLeaveManagement.getEmpDeptId();
            }
            if (srcLeaveManagement.getDivisionId() != 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + srcLeaveManagement.getDivisionId();
            }
            if (srcLeaveManagement.getEmpCatId() != 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = " + srcLeaveManagement.getEmpCatId();
            }
            if (srcLeaveManagement.getEmpLevelId() != 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = " + srcLeaveManagement.getEmpLevelId();
            }
            if (valueSection == 0) {
                if (section != null && section.size() > 0) {
                    sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                    for (int x = 0; x < section.size(); x++) {
                        int hasil = x - section.size();
                        Section sectionn = (Section) section.get(x);
                        variable += sectionn.getOID();
                        if (hasil != -1) {
                            variable = variable + ",";
                        }
                    }
                    sql = sql + variable + ")";
                }
            } else {
                if (srcLeaveManagement.getEmpSectionId() != 0 || section != null && section.size() > 0) {
                    sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + srcLeaveManagement.getEmpSectionId();
                }
            }
            
            sql = sql + " ORDER BY " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID];
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector result = new Vector();
            
            while (rs.next()) {
                
                Employee employee = new Employee();
                
                employee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                result.add(employee);
                
            }
            
            return result;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    public static Vector listSpecialUnpaidLeave(SrcLeaveManagement srcLeaveManagement) {
        
        if (srcLeaveManagement == null) {
            return null;
        }
        
        Vector listEmployee = new Vector();
        
        try {
            listEmployee = listOidEmployee(srcLeaveManagement);
        } catch (Exception e) {
            System.out.println("[exception] SessLeaveApplication.ListSpecialUnpaidLeave() " + e.toString());
        }
        
        if (listEmployee != null && listEmployee.size() > 0) {
            
            Vector result = new Vector();
            
            for (int i = 0; i < listEmployee.size(); i++) {
                
                Employee employee = new Employee();
                employee = (Employee) listEmployee.get(i);
                float toBeTknSp = 0;
                float tknSp = 0;
                float toBeTknUnp = 0;
                float tknUnp = 0;
                
                if (srcLeaveManagement.getTime() == 0) { /* Pencarian berdasarkan period*/
                    
                    Period period = new Period();
                    try {
                        
                        period = PstPeriod.fetchExc(srcLeaveManagement.getPeriodId());
                        
                    } catch (Exception E) {
                        System.out.println("[exception] SessLeaveApplication.listSpecialUnpaidLeave(SrcLeaveManagement srcLeaveManagement) :: " + E.toString());
                    }
                    
                    toBeTknSp = totalSpLeaveToBeTkn(employee.getOID(), period.getStartDate(), period.getEndDate());
                    tknSp = totalSpLeaveTkn(employee.getOID(), period.getStartDate(), period.getEndDate());
                    toBeTknUnp = totalUnpLeaveToBeTkn(employee.getOID(), period.getStartDate(), period.getEndDate());
                    tknUnp = totalUnpLeaveTkn(employee.getOID(), period.getStartDate(), period.getEndDate());
                    
                } else { /* Pencarian berdasarkan date start and date end*/
                    
                    toBeTknSp = totalSpLeaveToBeTkn(employee.getOID(), srcLeaveManagement.getStartDate(), srcLeaveManagement.getEndDate());
                    tknSp = totalSpLeaveTkn(employee.getOID(), srcLeaveManagement.getStartDate(), srcLeaveManagement.getEndDate());
                    toBeTknUnp = totalUnpLeaveToBeTkn(employee.getOID(), srcLeaveManagement.getStartDate(), srcLeaveManagement.getEndDate());
                    tknUnp = totalUnpLeaveTkn(employee.getOID(), srcLeaveManagement.getStartDate(), srcLeaveManagement.getEndDate());
                    
                }
                
                ListSp listSp = new ListSp();
                listSp.setEmployeeId(employee.getOID());
                listSp.setFullName(employee.getFullName());
                listSp.setDepartmentId(employee.getDepartmentId());
                listSp.setEmployeeNum(employee.getEmployeeNum());
                listSp.setToBeTakenSp(toBeTknSp);
                listSp.setTakenSp(tknSp);
                listSp.setTobeTakenUnp(toBeTknUnp);
                listSp.setTakenUnp(tknUnp);
                result.add(listSp);
                
            }
            
            return result;
        }
        
        return null;
    }

    /**
     *
     * @param employee_id
     * @param startDate
     * @param endDate
     * @return
     */
    private static float totalSpLeaveToBeTkn(long employee_id, Date startDate, Date endDate) {
//  private static int totalSpLeaveToBeTkn(long employee_id, Date startDate, Date endDate) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidSpLeave = ListScheduleSpecialLeave();
        
        if (vectOidSpLeave == null || vectOidSpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM hr_view_sp_to_be_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " >= '"
                    + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " <= '"
                    + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND ";
            
            String sql_schedule = "";
            
            if (vectOidSpLeave != null && vectOidSpLeave.size() > 0) {
                
                if (vectOidSpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidSpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidSpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            // int sumTkn = 0;

            while (rs.next()) {
                
                float qty = rs.getFloat(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]);
                // int qty = rs.getInt(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]);
                Date tknStart = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]);
                Date tknFns = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]);

                //float dateDiff = 0;
                int dateDiff = 0;
                
                dateDiff = DATEDIFF(endDate, tknFns);
                
                if (dateDiff < 0) {

                    //float diff2 = 0;
                    int diff2 = 0;
                    diff2 = DATEDIFF(endDate, tknStart);
                    sumTkn = sumTkn + diff2 + 1;
                    
                } else {
                    sumTkn = sumTkn + qty;
                }
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL TAKEN SPECIAL LEAVE
     */
    private static float totalSpLeaveTkn(long employee_id, Date startDate, Date endDate) {
        //private static int totalSpLeaveTkn(long employee_id, Date startDate, Date endDate) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidSpLeave = ListScheduleSpecialLeave();
        
        if (vectOidSpLeave == null || vectOidSpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM hr_view_sp_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " >= '"
                    + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " <= '"
                    + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND ";
            
            String sql_schedule = "";
            
            if (vectOidSpLeave != null && vectOidSpLeave.size() > 0) {
                
                if (vectOidSpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidSpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidSpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            //int sumTkn = 0;
            while (rs.next()) {
                
                float tknQty = rs.getFloat(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]);
                // int tknQty = rs.getInt(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]);
                Date startDt = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]);
                Date fnsDt = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]);

                //float dateDiff = 0;
                int dateDiff = 0;
                
                dateDiff = DATEDIFF(endDate, fnsDt);
                
                if (dateDiff < 0) {

                    //float diff2 = 0;
                    int diff2 = 0;
                    diff2 = DATEDIFF(endDate, startDt);
                    sumTkn = sumTkn + diff2 + 1;
                    
                } else {
                    sumTkn = sumTkn + tknQty;
                }
                
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL TO BE TAKEN UNPAID LEAVE
     */
    public static float totalUnpLeaveToBeTkn(long employee_id, Date startDate, Date endDate) {
        
        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidUnpLeave = ListScheduleUnpLeave();
        
        if (vectOidUnpLeave == null || vectOidUnpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM hr_view_sp_to_be_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " >= '"
                    + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " <= '"
                    + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND ";
            
            String sql_schedule = "";
            
            if (vectOidUnpLeave != null && vectOidUnpLeave.size() > 0) {
                
                if (vectOidUnpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidUnpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidUnpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            
            while (rs.next()) {
                
                float tknQty = rs.getFloat(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]);
                Date startDt = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]);
                Date fnsDt = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]);

                /*int dateDiff = 0;
                    
                 dateDiff = DATEDIFF(endDate, fnsDt);

                 if (dateDiff < 0) {

                 int diff2 = 0;
                 diff2 = DATEDIFF(endDate, startDt);
                 sumTkn = sumTkn + diff2 + 1;

                 } else {
                 sumTkn = sumTkn + tknQty;
                 }*/
                sumTkn = sumTkn + DateCalc.workDayDifference(startDt, endDate, tknQty);
                
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL TAKEN UNPAID LEAVE
     */
    public static float totalUnpLeaveTkn(long employee_id, Date startDate, Date endDate) {
        // public static int totalUnpLeaveTkn(long employee_id, Date startDate, Date endDate) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidUnpLeave = ListScheduleUnpLeave();
        
        if (vectOidUnpLeave == null || vectOidUnpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + ","
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM hr_view_sp_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " >= '"
                    + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND " + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE] + " <= '"
                    + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND ";
            
            String sql_schedule = "";
            
            if (vectOidUnpLeave != null && vectOidUnpLeave.size() > 0) {
                
                if (vectOidUnpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidUnpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidUnpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            //  int sumTkn = 0;

            while (rs.next()) {
                
                float tknQty = rs.getFloat(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]);
                // int tknQty = rs.getInt(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]);
                Date startDt = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_DATE]);
                Date fnsDt = rs.getDate(PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_FINNISH_DATE]);

                //float dateDiff = 0;
                int dateDiff = 0;
                
                dateDiff = DATEDIFF(endDate, fnsDt);
                
                if (dateDiff < 0) {

                    //float diff2 = 0;
                    int diff2 = 0;
                    diff2 = DATEDIFF(endDate, startDt);
                    sumTkn = sumTkn + diff2 + 1;
                    
                } else {
                    sumTkn = sumTkn + tknQty;
                }
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN LIST SPECIAL UNPAID LEAVE
     */
    //update by devin 2014-04-17
    //public static Vector listSchedule(SrcLeaveManagement srcLeaveManagement) {
    public static Vector listSchedule(SrcLeaveManagement srcLeaveManagement, int checkSection, Vector valueSection) {
        String variable = "";
        String where =
                (srcLeaveManagement.getEmpName().equals("") ? "" : (PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " like '%" + srcLeaveManagement.getEmpName() + "%' AND "))
                + (srcLeaveManagement.getEmpNum().equals("") ? "" : (PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " like '%" + srcLeaveManagement.getEmpNum() + "%' AND "))
                + (srcLeaveManagement.getEmpDeptId() != 0 ? (PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + srcLeaveManagement.getEmpDeptId() + " AND ") : "")
                + (srcLeaveManagement.getDivisionId() != 0 ? (PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + srcLeaveManagement.getDivisionId() + " AND ") : "")
                + //(srcLeaveManagement.getEmpSectionId() != 0 ? (PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + srcLeaveManagement.getEmpSectionId() + " AND ") : "") +
                (srcLeaveManagement.getEmpLevelId() != 0 ? (PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = " + srcLeaveManagement.getEmpLevelId() + " AND ") : "");
        //Update by devin 2014-04-17
        if (checkSection == 0) {            
            if (valueSection != null && valueSection.size() > 0) {
                where = where + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " NOT IN (";                
                for (int x = 0; x < valueSection.size(); x++) {
                    int hasil = x - valueSection.size();
                    Section sectionn = (Section) valueSection.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                where = where + variable + ") AND ";
            }
        } else {
            if (srcLeaveManagement.getEmpSectionId() != 0 || valueSection != null && valueSection.size() > 0) {
                where = where + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + srcLeaveManagement.getEmpSectionId() + " AND ";
            }
        }
        where = where + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "='" + PstEmployee.NO_RESIGN + "' " + " ORDER BY " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "," + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID];
        
        
        Vector vectEmployee = PstEmployee.list(0, 0, where, null);

        /**
         * @Desc indx : 0. EMPLOYEE_ID : 1. FULL_NAME : 2. DEPARTMENT_ID : 3.
         * EMPLOYEE_NUM : 4. TOTAL_TO_BE_TKN_SPECIAL_LEAVE : 5.
         * TOTAL_TKN_SPECIAL_LEAVE : 6. TOTAL_TO_BE_TKN_UNPAID_LEAVE : 7.
         * TOTAL_TKN_UNPAID_LEAVE
         */
        Vector EmployeeList = new Vector();
        
        if (vectEmployee != null && vectEmployee.size() > 0) {
            
            for (int i = 0; i < vectEmployee.size(); i++) {
                
                Employee employee = (Employee) vectEmployee.get(i);
                
                float toBeTknSp = totalSpLeaveToBeTkn(employee.getOID());
                float tknSp = totalSpLeaveTkn(employee.getOID());
                float toBeTknUnp = totalUnpLeaveToBeTkn(employee.getOID());
                float tknUnp = totalUnpLeaveTkn(employee.getOID());
                /*
                 * int toBeTknSp = totalSpLeaveToBeTkn(employee.getOID());
                 int tknSp = totalSpLeaveTkn(employee.getOID());
                 int toBeTknUnp = totalUnpLeaveToBeTkn(employee.getOID());
                 int tknUnp = totalUnpLeaveTkn(employee.getOID());
                 */
                
                ListSp listSp = new ListSp();
                listSp.setEmployeeId(employee.getOID());
                listSp.setFullName(employee.getFullName());
                listSp.setDepartmentId(employee.getDepartmentId());
                listSp.setEmployeeNum(employee.getEmployeeNum());
                listSp.setToBeTakenSp(toBeTknSp);
                listSp.setTakenSp(tknSp);
                listSp.setTobeTakenUnp(toBeTknUnp);
                listSp.setTakenUnp(tknUnp);
                
                EmployeeList.add(listSp);
                
            }
            
            return EmployeeList;
        }
        
        return null;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL TO BE TAKEN SPECIAL LEAVE
     */
    public static float totalSpLeaveToBeTkn(long employee_id) {
        // public static int totalSpLeaveToBeTkn(long employee_id) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidSpLeave = ListScheduleSpecialLeave();
        
        if (vectOidSpLeave == null || vectOidSpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]
                    + ") FROM hr_view_sp_to_be_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND ";
            
            String sql_schedule = "";
            
            if (vectOidSpLeave != null && vectOidSpLeave.size() > 0) {
                
                if (vectOidSpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidSpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidSpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            // int sumTkn = 0;

            while (rs.next()) {
                
                sumTkn = rs.getFloat(1);
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL TAKEN SPECIAL LEAVE
     */
    public static float totalSpLeaveTkn(long employee_id) {
        // public static int totalSpLeaveTkn(long employee_id) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidSpLeave = ListScheduleSpecialLeave();
        
        if (vectOidSpLeave == null || vectOidSpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]
                    + ") FROM hr_view_sp_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND ";
            
            String sql_schedule = "";
            
            if (vectOidSpLeave != null && vectOidSpLeave.size() > 0) {
                
                if (vectOidSpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidSpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidSpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidSpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            //int sumTkn = 0;

            while (rs.next()) {
                
                sumTkn = rs.getFloat(1);
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL TO BE TAKEN UNPAID LEAVE
     */
    public static float totalUnpLeaveToBeTkn(long employee_id) {
        // public static int totalUnpLeaveToBeTkn(long employee_id) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidUnpLeave = ListScheduleUnpLeave();
        
        if (vectOidUnpLeave == null || vectOidUnpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]
                    + ") FROM hr_view_sp_to_be_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND ";
            
            String sql_schedule = "";
            
            if (vectOidUnpLeave != null && vectOidUnpLeave.size() > 0) {
                
                if (vectOidUnpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidUnpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidUnpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            // int sumTkn = 0;

            while (rs.next()) {
                
                sumTkn = rs.getFloat(1);
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL TAKEN UNPAID LEAVE
     */
    public static float totalUnpLeaveTkn(long employee_id) {
//    public static int totalUnpLeaveTkn(long employee_id) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidUnpLeave = ListScheduleUnpLeave();
        
        if (vectOidUnpLeave == null || vectOidUnpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY]
                    + ") FROM hr_view_sp_tkn WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND ";
            
            String sql_schedule = "";
            
            if (vectOidUnpLeave != null && vectOidUnpLeave.size() > 0) {
                
                if (vectOidUnpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidUnpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidUnpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
            //  int sumTkn = 0;

            while (rs.next()) {
                
                sumTkn = rs.getFloat(1);
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
        
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENDAPATKAN TOTAL UNPAID LEAVE
     */
    public static float TotalUnpLeave(long employee_id) {
//    public static int TotalUnpLeave(long employee_id) {

        if (employee_id == 0) {
            return 0;
        }
        
        Vector vectOidUnpLeave = ListScheduleUnpLeave();
        
        if (vectOidUnpLeave == null || vectOidUnpLeave.size() <= 0) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(" + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstSpecialUnpaidLeaveTaken.TBL_SPECIAL_UNPAID_LEAVE_TAKEN + " WHERE "
                    + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_EMPLOYEE_ID] + " = " + employee_id
                    + " AND ";
            
            String sql_schedule = "";
            
            if (vectOidUnpLeave != null && vectOidUnpLeave.size() > 0) {
                
                if (vectOidUnpLeave.size() == 1) {
                    
                    long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(0));
                    
                    sql_schedule = "" + oidSchedule;
                    
                } else {
                    
                    sql_schedule = sql_schedule + " ( ";
                    
                    int max = vectOidUnpLeave.size() - 1;
                    
                    for (int i = 0; i < vectOidUnpLeave.size(); i++) {
                        
                        long oidSchedule = Long.parseLong((String) vectOidUnpLeave.get(i));
                        
                        sql_schedule = sql_schedule + PstSpecialUnpaidLeaveTaken.fieldNames[PstSpecialUnpaidLeaveTaken.FLD_SCHEDULED_ID] + " = " + oidSchedule;
                        
                        if (i != max) {
                            sql_schedule = sql_schedule + " OR ";
                        }
                        
                    }
                    
                    sql_schedule = sql_schedule + " ) ";
                }
            }
            
            String qrySql = sql + sql_schedule;
            
            dbrs = DBHandler.execQueryResult(qrySql);
            ResultSet rs = dbrs.getResultSet();
            
            float sumTkn = 0;
// int sumTkn = 0;
            while (rs.next()) {
                
                sumTkn = rs.getFloat(1);
            }
            
            return sumTkn;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    
    public static float getTotal2BeTakenDP(long departmentId) {
//    public static int getTotal2BeTakenDP(long departmentId) {

        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DP." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ") FROM "
                    + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DP INNER JOIN "
                    + PstLeaveApplication.TBL_LEAVE_APPLICATION + " LEV ON DP."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + " = LEV."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND "
                    + " ( LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT
                    + " OR LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE
                    + " OR LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED + ")";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float total = rs.getFloat(1);
                //  int total = rs.getInt(1);
                return total;
            }
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
        
    }
    
    public static Vector getListPeriodAlAktif(long departmentId) {
        
        Vector result = new Vector();
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " as empId "
                    + ",AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + " as prevBal"
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " as depId"
                    + ",AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " as alStockId "
                    + ",AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + " as qty "
                    + ",AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + " as qtyUsed "
                    + ",AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " as entDate "
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " AL INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND "
                    + " AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF + " AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                AlPrevBalanceAktif alPrevBalanceAktif = new AlPrevBalanceAktif();
                
                alPrevBalanceAktif.setAlStockId(rs.getLong("alStockId"));
                alPrevBalanceAktif.setQty(rs.getInt("qty"));
                alPrevBalanceAktif.setQtyTkn(rs.getInt("qtyUsed"));
                alPrevBalanceAktif.setEmployeeId(rs.getLong("empId"));
                alPrevBalanceAktif.setDepartementId(rs.getLong("depId"));
                alPrevBalanceAktif.setEntitleDate(rs.getDate("entDate"));
                alPrevBalanceAktif.setPrevBalance(rs.getInt("prevBal"));
                
                result.add(alPrevBalanceAktif);
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
    }
    
    public static Vector getListPeriodLlAktif(long departmentId) {
        
        Vector result = new Vector();
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " as empId "
                    + ",LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + " as prevBal"
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " as depId"
                    + ",LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " as llStockId "
                    + ",LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + " as qty "
                    + ",LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + " as qtyUsed "
                    + ",LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " as entDate "
                    + " FROM " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND "
                    + " LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF + " AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                LlPrevBalanceAktif llPrevBalanceAktif = new LlPrevBalanceAktif();
                
                llPrevBalanceAktif.setLlStockId(rs.getLong("llStockId"));
                llPrevBalanceAktif.setQty(rs.getInt("qty"));
                llPrevBalanceAktif.setQtyTkn(rs.getInt("qtyUsed"));
                llPrevBalanceAktif.setEmployeeId(rs.getLong("empId"));
                llPrevBalanceAktif.setDepartementId(rs.getLong("depId"));
                llPrevBalanceAktif.setEntitleDate(rs.getDate("entDate"));
                llPrevBalanceAktif.setPrevBalance(rs.getInt("prevBal"));
                
                result.add(llPrevBalanceAktif);
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
    }
    
    public static AlStockManagement getTotalPeriodAlAktif(long departmentId) {
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        
        int interval = leaveConfig.getIntervalALinMonths()[LeaveConfigHR.INTERVAL_AL_HARDROCK];     //range mendapatkan AL (dalam bulan)        

        Vector vlistPrevBal = new Vector();
        
        vlistPrevBal = getListPeriodAlAktif(departmentId);
        
        float totalPrev = 0;
        float totalQty = 0;
        float totalTaken = 0;
        float total2BeTaken = 0;
        /**
         * int totalPrev = 0; int totalQty = 0; int totalTaken = 0; int
         * total2BeTaken = 0;
         */
        if (vlistPrevBal != null && vlistPrevBal.size() > 0) {
            
            for (int i = 0; i < vlistPrevBal.size(); i++) {
                
                AlPrevBalanceAktif alPrevBalanceAktif = new AlPrevBalanceAktif();
                
                alPrevBalanceAktif = (AlPrevBalanceAktif) vlistPrevBal.get(i);
                
                boolean stsAlAktf = true;
                
                stsAlAktf = SessLeaveApplication.getPeriodAktif(new Date(), interval, alPrevBalanceAktif.getEntitleDate());
                
                if (stsAlAktf == true) {
                    
                    boolean statusExpired = SessLeaveApplication.getStatusLeaveAlExpired(alPrevBalanceAktif.getAlStockId());
                    
                    totalQty = totalQty + alPrevBalanceAktif.getQty();
                    
                    totalTaken = totalTaken + alPrevBalanceAktif.getQtyTkn();
                    
                    total2BeTaken = total2BeTaken + get2BeTaknAl(alPrevBalanceAktif.getEmployeeId());
                    
                    if (statusExpired == false) {
                        
                        totalPrev = totalPrev + alPrevBalanceAktif.getPrevBalance();
                        
                    }
                }
            }
        }
        
        AlStockManagement alStockManagement = new AlStockManagement();
        
        alStockManagement.setAlQty(totalQty);
        alStockManagement.setPrevBalance(totalPrev);
        alStockManagement.setQtyUsed(totalTaken);
        alStockManagement.setALtoBeTaken(total2BeTaken);
        return alStockManagement;
        
    }
    
    public static LLStockManagement getTotalPeriodLlAktif(long departmentId) {
        
        Vector vlistPrevBal = new Vector();
        
        vlistPrevBal = getListPeriodLlAktif(departmentId);
        
        float totalPrev = 0;
        float totalQty = 0;
        float totalTaken = 0;
        float total2BeTaken = 0;
        /**
         * int totalPrev = 0; int totalQty = 0; int totalTaken = 0; int
         * total2BeTaken = 0;
         */
        if (vlistPrevBal != null && vlistPrevBal.size() > 0) {
            
            for (int i = 0; i < vlistPrevBal.size(); i++) {
                
                LlPrevBalanceAktif llPrevBalanceAktif = new LlPrevBalanceAktif();
                
                llPrevBalanceAktif = (LlPrevBalanceAktif) vlistPrevBal.get(i);
                
                boolean stsAlAktf = true;
                
                stsAlAktf = SessLeaveApplication.getStatusLLExpired(llPrevBalanceAktif.getEmployeeId());
                
                if (stsAlAktf == false) {
                    
                    totalQty = totalQty + llPrevBalanceAktif.getQty();
                    
                    totalTaken = totalTaken + llPrevBalanceAktif.getQtyTkn();
                    
                    total2BeTaken = total2BeTaken + get2BeTaknLl(llPrevBalanceAktif.getEmployeeId());
                    
                    totalPrev = totalPrev + llPrevBalanceAktif.getPrevBalance();
                    
                }
            }
        }
        
        LLStockManagement llStockManagement = new LLStockManagement();
        
        llStockManagement.setLLQty(totalQty);
        llStockManagement.setPrevBalance(totalPrev);
        llStockManagement.setQtyUsed(totalTaken);
        llStockManagement.setToBeTaken(total2BeTaken);
        return llStockManagement;
        
    }
    
    public static float get2BeTaknAl(long employeeId) {
//    public static int get2BeTaknAl(long employeeId) {

        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ") as total FROM "
                    + PstLeaveApplication.TBL_LEAVE_APPLICATION + " LEV INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT ON LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " WHERE "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                    + " ( LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT + " OR "
                    + " LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " OR "
                    + " LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED + " ) ";
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float sum = rs.getInt("total");
// int sum = rs.getInt("total");
                return sum;
                
            }
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        
        return 0;
    }
    
    public static float get2BeTaknLl(long employeeId) {
//    public static int get2BeTaknLl(long employeeId) {

        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ") as total FROM "
                    + PstLeaveApplication.TBL_LEAVE_APPLICATION + " LEV INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT ON LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = "
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " WHERE "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                    + " ( LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT + " OR "
                    + " LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " OR "
                    + " LEV." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED + " ) ";
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float sum = rs.getInt("total");
                //int sum = rs.getInt("total");
                
                return sum;
                
            }
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        
        return 0;
    }

    /**
     * @AUTHOR : ROY ANDIKA
     * @DESC : UNTUK MENGECEK TANGGAL YANG DIMASUKAN APAKAH DALAM PERIODE YANG
     * SAMA
     */
    public static boolean getPeriodAktif(Date dateTkn, int intervalMonth, Date entitleDate) {
        
        Date fnsPeriod = DATE_ADD(entitleDate, intervalMonth);                     // finnish date 

        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT '" + Formater.formatDate(dateTkn, "yyyy-MM-dd") + "' >= '"
                    + Formater.formatDate(entitleDate, "yyyy-MM-dd") + "' AND '"
                    + Formater.formatDate(dateTkn, "yyyy-MM-dd") + "' < '"
                    + Formater.formatDate(fnsPeriod, "yyyy-MM-dd") + "'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                int result = rs.getInt(1);
                if (result == 1) {
                    return true;
                } else {
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return false;
    }
    
    public static Vector getExpLongLeave(long departmentId) {
        
        DBResultSet dbrs = null;
        
        Vector Vresult = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " as empId ,"
                    + "LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " as llId "
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " EMP INNER JOIN " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL "
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                LLStockManagement llStockManagement = new LLStockManagement();
                llStockManagement.setEmployeeId(rs.getLong("empId"));
                llStockManagement.setOID(rs.getLong("llId"));
                Vresult.add(llStockManagement);
            }
            
            return Vresult;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        
        return null;
    }
    
    public static float totExpired(long departmentId) {
//    public static int totExpired(long departmentId) {

        Vector VLeaveAppOid = new Vector();
        
        VLeaveAppOid = getExpLongLeave(departmentId);
        
        float total = 0;
//int total = 0;
        if (VLeaveAppOid != null && VLeaveAppOid.size() > 0) {
            
            for (int i = 0; i < VLeaveAppOid.size(); i++) {
                
                LLStockManagement llStockManagement = (LLStockManagement) VLeaveAppOid.get(i);
                
                boolean stsLLExp = false;
                
                stsLLExp = SessLeaveApplication.getStatusLLExpired(llStockManagement.getEmployeeId());
                
                if (stsLLExp == false) {
                    float exp = GetExpired(llStockManagement.getOID());
                    //   int exp = GetExpired(llStockManagement.getOID());
                    total = total + exp;
                }
                
            }
            
        }
        
        return 0;
    }
    
    public static float GetExpired(long llStockId) {
//    public static int GetExpired(long llStockId) {

        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(" + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " WHERE "
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + "=" + llStockId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float i = 0;
//int i = 0;
            while (rs.next()) {
                
                i = rs.getFloat(1);
                
            }
            
            return i;
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        
        return 0;
    }

    /**
     * @Author : Roy Andika
     * @Desc : Untuk mengupdate prev opname yang prev balance pada stocknya
     * sudah tidak aktif
     */
    public static void updateOpnamePrevBalance(long employeeId, long alUploadId) {
        //update by satrya 2013-01-06
        // public static void updateOpnamePrevBalance(long employeeId) {
        //update by satrya 2013-01-06
        // AlUpload alUpload = SessAlUpload.getOpnamePrevious(employeeId);
        AlUpload alUpload = SessAlUpload.getALOpnamePrevious(employeeId, alUploadId);
        if (alUpload == null) {
            return;
        }
        AlStockManagement alStockManagement = new AlStockManagement();
        boolean statusPrevExpired = false;
        
        try {
            
            alStockManagement = PstAlStockManagement.fetchExc(alUpload.getStockId());
            statusPrevExpired = SessLeaveApplication.getStatusLeaveAlExpired(alStockManagement.getOID());
            
            if (statusPrevExpired == true) {
                
                alUpload.setLastPerToClear(0);
                try {
                    PstAlUpload.updateExc(alUpload);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        
    }
    
    public static Vector getListApprovalHRManager() {
        
        String levelExcomLocal = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        String levelExcomExpat = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SECRETARY + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SUPERVISOR + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + ") AND ( LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " != " + levelExcomLocal + " AND LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " != " + levelExcomExpat + " ) AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalDepHead(long departmentId) {
        
        String levelExcomLocal = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        String levelExcomExpat = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SECRETARY + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SUPERVISOR + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_ASST_MANAGER + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + " ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND ( LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " != " + levelExcomLocal + " AND LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " != " + levelExcomExpat + " ) AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalGM() {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        String levelExcomLocal = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        String levelExcomExpat = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV "
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + " AND ( LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " = " + levelExcomLocal + " OR LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " = " + levelExcomExpat + " ) AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE;
            
            
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {
                    
                    objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    
                    vectTemp.add(objEmployee);
                    
                    objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                    objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                    objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                    objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                    objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                    objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                    objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                    objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                    objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                    objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                    objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                    
                    vectTemp.add(objLeaveApplication);
                    
                    objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    
                    vectTemp.add(objDepartment);
                    
                    result.add(vectTemp);
                }
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan employee yang perlu approval yang level excom
     * local and excom expatriat
     * @return
     */
    public static Vector getListGmExcomAndExpat(long ParameterOid, int Approval) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        String levelExcomLocal = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        String levelExcomExpat = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV "
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + " AND ( LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " = " + levelExcomLocal + " OR LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " = " + levelExcomExpat + " ) AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL] + " = 0";
            
            
            if (Approval == GM_SECTION_SCOPE && ParameterOid != 0) {
                
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " != " + ParameterOid;
                
            } else if (Approval == GM_DEPARTMENT_SCOPE) {
                
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " != " + ParameterOid;
                
            } else if (Approval == GM_DIVISION_SCOPE) {
                
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " != " + ParameterOid;
                
            }
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();

                //if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {

                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                //}

            }
            
            Vector objectClass = new Vector();
            
            try {
                
                objectClass = getListGmDepartment(ParameterOid, Approval);
                
            } catch (Exception E) {
                System.out.println("Exception " + E.toString());
            }
            
            
            if (objectClass != null && objectClass.size() > 0) {
                
                for (int i = 0; i < objectClass.size(); i++) {
                    
                    Vector temp = new Vector();
                    temp = (Vector) objectClass.get(i);
                    
                    Vector tmpVect = new Vector();
                    Employee employee = (Employee) temp.get(0);
                    tmpVect.add(employee);
                    LeaveApplication objleaveApplication = (LeaveApplication) temp.get(1);
                    tmpVect.add(objleaveApplication);
                    Department department = (Department) temp.get(2);
                    tmpVect.add(department);
                    
                    result.add(tmpVect);
                    
                }
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
    }
    
    public static Vector getListApprovallConfig(String sEmployeeId, long empLoginId) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        if (sEmployeeId == null || sEmployeeId.length() == 0) {
            return result;
        }
        
        
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV "
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE (1=1) AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL] + " = 0"
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " IN(" + sEmployeeId + ")"
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            I_Leave leaveConfig = null;
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                boolean needApproval = false;
                
                int maxApproval = leaveConfig.getMaxApproval(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                long empOID = 0;
                
                empOID = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
                
                if (maxApproval == I_Leave.LEAVE_APPROVE_1) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (empLoginId == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_2) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (empLoginId == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (empLoginId == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_3) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (empLoginId == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {
                        
                        Vector vectPositionLvl1 = new Vector(1, 1);
                        
                        vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                        
                        Vector listDivHead = SessEmployee.listEmployeeByPositionLevelGeneralM(vectPositionLvl1);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (empLoginId == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                }
                
                if (needApproval == true) {
                    
                    objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    
                    vectTemp.add(objEmployee);
                    
                    objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                    objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                    objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                    objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                    objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                    objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                    objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                    objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                    objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                    objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                    objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                    
                    vectTemp.add(objLeaveApplication);
                    
                    objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    
                    vectTemp.add(objDepartment);
                    
                    result.add(vectTemp);
                    
                }
                
            }
            
            return result;
            
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan list approval dimana employeenya memiliki
     * department yang sama dengan GM
     * @param department_Id
     * @return
     */
    private static Vector getListGmDepartment(long ParameterOid, int Approval) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV "
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL] + " = 0 ";
            
            if (Approval == GM_SECTION_SCOPE && ParameterOid != 0) {
                
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + ParameterOid;
                
            } else if (Approval == GM_DEPARTMENT_SCOPE) {
                
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + ParameterOid;
                
            } else if (Approval == GM_DIVISION_SCOPE) {
                
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + ParameterOid;
                
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();

                //if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {

                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                //}

            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListEmployeeLeaveApplication(long employeeId) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalSecurity(long employeeId) {
        
        String posSecurityStr = PstSystemProperty.getValueByName("OID_SECURITY");
        long posSecurity = 0;
        try {
            posSecurity = Long.parseLong(posSecurityStr);
        } catch (Exception exc) {
            
        }
        String hrdDepartment = PstSystemProperty.getValueByName("OID_HRD_DEPARTMENT");
        //String levelExcomLocal  = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        //String levelExcomExpat  = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");

        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " SEC ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID]
                    + " WHERE ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SECRETARY + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SUPERVISOR + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + ") AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + hrdDepartment
                    + (posSecurity != 0 ? (" AND SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " = " + posSecurity) : "") + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " != " + employeeId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalSection(long section_id, long department_id, long employee_id) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " SEC ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID]
                    + " WHERE ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SECRETARY + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SUPERVISOR + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_ASST_MANAGER + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + " OR POS."
                    + //update by satrya 2013-06-18
                    PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_DIRECTOR
                    + //update by satrya 2013-06-18
                    //PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER +
                    ") AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE
                    + (department_id != 0 ? (" AND DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + department_id) : "")
                    + (section_id != 0 ? (" AND SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " = " + section_id) : " ") + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            I_Leave leaveConfig = null;
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                boolean needApproval = false;
                
                int maxApproval = leaveConfig.getMaxApproval(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                long empOID = 0;
                
                empOID = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
                
                if (maxApproval == I_Leave.LEAVE_APPROVE_1) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_2) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_3) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {
                        
                        Vector vectPositionLvl1 = new Vector(1, 1);
                        
                        vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                        
                        Vector listDivHead = SessEmployee.listEmployeeByPositionLevelGeneralM(vectPositionLvl1);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                }
                
                if (needApproval == true) {
                    
                    objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    
                    vectTemp.add(objEmployee);
                    
                    objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                    objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                    objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                    objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                    objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                    objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                    objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                    objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                    objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                    objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                    objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                    
                    vectTemp.add(objLeaveApplication);
                    
                    objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    
                    vectTemp.add(objDepartment);
                    
                    result.add(vectTemp);
                    
                }
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalHRManagerUnderDept(long employee_Id) {
        
        String levelExcomLocal = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        String levelExcomExpat = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SECRETARY + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SUPERVISOR + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_ASST_MANAGER + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + ") AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " != " + employee_Id;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalAllDepartment(long employee_id, boolean levelExcom) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " WHERE APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE
                    + " AND ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " IN (" + PstPosition.LEVEL_GENERAL + ","
                    + +PstPosition.LEVEL_SECRETARY + ", " + PstPosition.LEVEL_SUPERVISOR + ", " + PstPosition.LEVEL_ASST_MANAGER
                    + ", " + PstPosition.LEVEL_MANAGER + ", " + PstPosition.LEVEL_ASST_DIRECTOR + ") ";
            
            if (levelExcom == true) {
                
                sql = sql + " OR POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL_MANAGER;
            }
            
            sql = sql + " )";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            I_Leave leaveConfig = null;
            
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                boolean needApproval = false;
                
                int maxApproval = leaveConfig.getMaxApproval(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                long empOID = 0;
                
                empOID = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
                
                System.out.println("Employee Id = " + empOID);
                
                if (maxApproval == I_Leave.LEAVE_APPROVE_1) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        if (listDivHead != null && listDivHead.size() > 0) {
                            
                            for (int i = 0; i < listDivHead.size(); i++) {
                                
                                Employee objEmp = (Employee) listDivHead.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_2) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        if (listDivHead != null && listDivHead.size() > 0) {
                            
                            for (int i = 0; i < listDivHead.size(); i++) {
                                
                                Employee objEmp = (Employee) listDivHead.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_3) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {
                        
                        Vector vectPositionLvl1 = new Vector(1, 1);
                        
                        vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                        
                        Vector listDivHead = SessEmployee.listEmployeeByPositionLevelGeneralM(vectPositionLvl1);
                        
                        if (listDivHead != null && listDivHead.size() > 0) {
                            
                            for (int i = 0; i < listDivHead.size(); i++) {
                                
                                Employee objEmp = (Employee) listDivHead.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                }
                
                if (needApproval == true) {
                    objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    
                    vectTemp.add(objEmployee);
                    
                    objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                    objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                    objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                    objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                    objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                    objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                    objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                    objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                    objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                    objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                    objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                    
                    vectTemp.add(objLeaveApplication);
                    
                    objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    
                    vectTemp.add(objDepartment);
                    
                    result.add(vectTemp);
                }
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalDepartment(long employee_id, long department_id) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ( POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " IN ( " + PstPosition.LEVEL_GENERAL + " ,"
                    + PstPosition.LEVEL_SECRETARY + "," + PstPosition.LEVEL_SUPERVISOR + "," + PstPosition.LEVEL_ASST_MANAGER
                    + ") OR ( POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + "=" + PstPosition.LEVEL_MANAGER + " AND "
                    + PstPosition.fieldNames[PstPosition.FLD_DISABLED_APP_DEPT_SCOPE] + "=" + PstPosition.DISABLED_APP_DEPT_SCOPE_FALSE + " )) AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department_id;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            I_Leave leaveConfig = null;
            
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                boolean needApproval = false;
                
                int maxApproval = leaveConfig.getMaxApproval(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                long empOID = 0;
                
                empOID = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
                
                if (maxApproval == I_Leave.LEAVE_APPROVE_1) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_2) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_3) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {
                        
                        Vector vectPositionLvl1 = new Vector(1, 1);
                        
                        vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                        
                        Vector listDivHead = SessEmployee.listEmployeeByPositionLevelGeneralM(vectPositionLvl1);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                }
                
                if (needApproval == true) {
                    
                    objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    
                    vectTemp.add(objEmployee);
                    
                    objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                    objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                    objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                    objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                    objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                    objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                    objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                    objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                    objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                    objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                    objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                    
                    vectTemp.add(objLeaveApplication);
                    
                    objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    
                    vectTemp.add(objDepartment);
                    
                    result.add(vectTemp);
                }
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }

    /**
     * create by satrya 2013-08-26
     *
     * @param employee_id
     * @param department_id
     * @param minAppoveLevel
     * @param maxAppoveLevel
     * @return
     */
    public static Vector getListApprovalDepartment(long employee_id, long department_id, int minAppoveLevel, int maxAppoveLevel) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE "
                    /*+ "( POS." +
                     PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " IN ( " + PstPosition.LEVEL_GENERAL + " ," +
                     PstPosition.LEVEL_SECRETARY + "," + PstPosition.LEVEL_SUPERVISOR + "," + PstPosition.LEVEL_ASST_MANAGER +
                     ") OR ( POS." +
                     PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + "=" + PstPosition.LEVEL_MANAGER+" AND "+ 
                     PstPosition.fieldNames[PstPosition.FLD_DISABLED_APP_DEPT_SCOPE] + "=" + PstPosition.DISABLED_APP_DEPT_SCOPE_FALSE +" )) "*/
                    //+ PstPosition.fieldNames[PstPosition.FLD_DISABLED_APP_DEPT_SCOPE] + " = " + PstPosition.DISABLED_APP_DEPT_SCOPE_FALSE 
                    + "( POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " >= " + minAppoveLevel + " AND POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " <= " + maxAppoveLevel + ") "
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "<>" + employee_id
                    + " AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department_id;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            I_Leave leaveConfig = null;
            
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            
            while (rs.next()) {
                Vector vectTemp = new Vector(1, 1);
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                boolean needApproval = false;
                
                int maxApproval = leaveConfig.getMaxApproval(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                long empOID = 0;
                
                empOID = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
                
                if (maxApproval == I_Leave.LEAVE_APPROVE_1) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_2) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_3) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {
                        
                        Vector vectPositionLvl1 = new Vector(1, 1);
                        
                        vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                        
                        Vector listDivHead = SessEmployee.listEmployeeByPositionLevelGeneralM(vectPositionLvl1);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                }
                
                if (needApproval == true) {
                    
                    objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    
                    vectTemp.add(objEmployee);
                    
                    objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                    objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                    objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                    objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                    objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                    objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                    objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                    objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                    objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                    objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                    objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                    
                    vectTemp.add(objLeaveApplication);
                    
                    objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    
                    vectTemp.add(objDepartment);
                    
                    result.add(vectTemp);
                }
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalDivision(long employee_id, long division_id) {
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ( POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " IN ( " + PstPosition.LEVEL_GENERAL + " ,"
                    + PstPosition.LEVEL_SECRETARY + "," + PstPosition.LEVEL_SUPERVISOR + "," + PstPosition.LEVEL_ASST_MANAGER
                    + "," + PstPosition.LEVEL_MANAGER + "," + PstPosition.LEVEL_ASST_DIRECTOR
                    + ")) AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + division_id;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            I_Leave leaveConfig = null;
            
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                boolean needApproval = false;
                
                int maxApproval = leaveConfig.getMaxApproval(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                long empOID = 0;
                
                empOID = rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]);
                
                if (maxApproval == I_Leave.LEAVE_APPROVE_1) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_2) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]) == 0) {
                        
                        Vector listDivHead = leaveConfig.getApprovalDepartmentHead(empOID);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                } else if (maxApproval == I_Leave.LEAVE_APPROVE_3) {
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]) == 0) {
                        
                        Vector listHRMan = leaveConfig.listHRManager(empOID);
                        
                        if (listHRMan != null && listHRMan.size() > 0) {
                            
                            for (int i = 0; i < listHRMan.size(); i++) {
                                
                                Employee objEmp = (Employee) listHRMan.get(i);
                                
                                if (employee_id == objEmp.getOID()) {
                                    
                                    needApproval = true;
                                    
                                }
                            }
                        }
                    }
                    
                    if (rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]) == 0) {
                        
                        Vector vectPositionLvl1 = new Vector(1, 1);
                        
                        vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                        
                        Vector listDivHead = SessEmployee.listEmployeeByPositionLevelGeneralM(vectPositionLvl1);
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (employee_id == objEmp.getOID()) {
                                
                                needApproval = true;
                                
                            }
                        }
                    }
                }
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalDepHeadUnderDepartment(long employee_id, long departmentId) {
        
        String levelExcomLocal = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        String levelExcomExpat = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SECRETARY + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SUPERVISOR + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + " ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " != " + employee_id;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }
    
    public static Vector getListApprovalDepHeadUnderSupervisor(long employee_id, long departmentId, long sectionId) {
        
        String levelExcomLocal = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_LOCAL");
        String levelExcomExpat = PstSystemProperty.getValueByName("OID_LEVEL_EXCOM_EXPATRIAT");
        
        DBResultSet dbrs = null;
        Vector result = new Vector();
        
        try {
            
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ",DEP." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + ",APP.* FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstPosition.TBL_HR_POSITION + " POS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + " = POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_ID] + " INNER JOIN " + PstLevel.TBL_HR_LEVEL + " LEV ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEP ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEP."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " LEFT JOIN "
                    + PstSection.TBL_HR_SECTION + " SEC ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = SEC."
                    + PstSection.fieldNames[PstSection.FLD_SECTION_ID]
                    + " WHERE ( POS." + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_GENERAL + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SECRETARY + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_SUPERVISOR + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_ASST_MANAGER + " OR POS."
                    + PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + PstPosition.LEVEL_MANAGER + " ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " != " + employee_id
                    + (sectionId != 0 ? (" AND SEC." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " = " + sectionId) : "");
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Vector vectTemp = new Vector(1, 1);
                
                Employee objEmployee = new Employee();
                LeaveApplication objLeaveApplication = new LeaveApplication();
                Department objDepartment = new Department();
                
                objEmployee.setOID(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                objEmployee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                objEmployee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                
                vectTemp.add(objEmployee);
                
                objLeaveApplication.setOID(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]));
                objLeaveApplication.setSubmissionDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_SUBMISSION_DATE]));
                objLeaveApplication.setEmployeeId(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]));
                objLeaveApplication.setLeaveReason(rs.getString(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_REASON]));
                objLeaveApplication.setDepHeadApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVAL]));
                objLeaveApplication.setHrManApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVAL]));
                objLeaveApplication.setDocStatus(rs.getInt(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS]));
                objLeaveApplication.setDepHeadApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DEP_HEAD_APPROVE_DATE]));
                objLeaveApplication.setHrManApproveDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_HR_MAN_APPROVE_DATE]));
                objLeaveApplication.setGmApproval(rs.getLong(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL]));
                objLeaveApplication.setGmApprovalDate(rs.getDate(PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_GM_APPROVAL_DATE]));
                
                vectTemp.add(objLeaveApplication);
                
                objDepartment.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                
                vectTemp.add(objDepartment);
                
                result.add(vectTemp);
                
            }
            
            return result;
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
        
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int DATEDIFF(Date endDate, Date startDate) {
        //public static int DATEDIFF(Date endDate, Date startDate) {
        String start = Formater.formatDate(startDate, "yyyy-MM-dd");
        String end = Formater.formatDate(endDate, "yyyy-MM-dd");
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DATEDIFF('" + end + "','" + start + "')";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                int diferent = 0;
                diferent = rs.getInt(1);
                //  int diferent = 0;
                //diferent = rs.getInt(1);
                return diferent;
            }
            
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;        
    }

    /**
     * @AUTHOR ROY A.
     * @DESC UNTUK MENG-EXPIREDKAN DP YANG TERDAPAT PADA LEAVE FORM
     * @PARAM applicationId
     */
    public static void ExpiredDpLeaveForm(long leaveApplicationId) {
        
        Vector vectorDpStockTaken = new Vector();
        
        String whereClause = PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID] + " = " + leaveApplicationId;
        vectorDpStockTaken = PstDpStockTaken.list(0, 0, whereClause, null);
        
        String where = "";
        
        if (vectorDpStockTaken != null && vectorDpStockTaken.size() > 0) {
            
            for (int i = 0; i < vectorDpStockTaken.size(); i++) {
                
                DpStockTaken dpStockTaken = new DpStockTaken();
                dpStockTaken = (DpStockTaken) vectorDpStockTaken.get(i);
                
                DpStockManagement dpStockManagement = new DpStockManagement();
                
                try {
                    dpStockManagement = PstDpStockManagement.fetchExc(dpStockTaken.getDpStockId());
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                if (dpStockManagement.getiDpStatus() == PstDpStockManagement.DP_STS_EXPIRED) {// JIKA KONDISI STOCK DP SUDAH EXPIRED

                    try {
                        
                        String sql = "DELETE FROM " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " WHERE "
                                + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_TAKEN_ID] + " = " + dpStockTaken.getOID();
                        
                        System.out.println("DELETE DP TAKEN : " + sql);
                        
                        int j = DBHandler.execUpdate(sql);
                        
                    } catch (Exception e) {
                        System.out.println("Exception " + e.toString());
                    }
                }
            }
        }
    }

    /**
     * @Author : ROY ANDIKA
     * @param employeeId
     * @param tknDate
     * @return
     * @DESC
     */
    public static boolean getExpiredLongLeave(long employeeId, Date tknDate) {
        
        DBResultSet dbrs = null;
        
        String whereLL = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId;
        
        LLStockManagement llStockManagement = new LLStockManagement();
        
        Vector longLeave = PstLLStockManagement.list(0, 0, whereLL, null);
        
        boolean statusTkn = false;
        
        if (longLeave != null && longLeave.size() > 0) {
            
            llStockManagement = (LLStockManagement) longLeave.get(0);
            
            if (llStockManagement.getExpiredDate2() != null) {
                
                if (llStockManagement.getExpiredDate2().getTime() / (24L * 60L * 60L * 1000L) >= llStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L)) {
                    
                    statusTkn = getLastDayLlPeriod(llStockManagement.getEntitledDate(), tknDate, llStockManagement.getExpiredDate2());
                    
                } else {
                    
                    statusTkn = getLastDayLlPeriod(llStockManagement.getEntitledDate(), tknDate, llStockManagement.getExpiredDate());
                    
                }
                
                return statusTkn;
                
            } else {
                
                return false;
                
            }
        }
        return false;
    }
    
    public static boolean getLastDayLlPeriod(Date entitleDate, Date tknDate, Date expDate) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT '" + Formater.formatDate(tknDate, "yyyy-MM-dd") + "' >= '"
                    + Formater.formatDate(entitleDate, "yyyy-MM-dd") + "' AND '"
                    + Formater.formatDate(tknDate, "yyyy-MM-dd") + "' < '"
                    + Formater.formatDate(expDate, "yyyy-MM-dd") + "'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                int result = rs.getInt(1);
                if (result == 1) {
                    return true;
                } else {
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return false;
    }
    
    public static void updateSchAl(long oidAl) {
        
        AlStockTaken alStockTaken = new AlStockTaken();
        
        try {
            alStockTaken = PstAlStockTaken.fetchExc(oidAl);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        long OidAL = 0;
        try {
            OidAL = Long.parseLong(PstSystemProperty.getValueByName("OID_AL"));
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        Date strtDate = alStockTaken.getTakenDate();
        Date newDate = new Date();
        
        for (int i = 0; i < alStockTaken.getTakenQty(); i++) {
            
            long schId = SessEmpSchedule.getSchId(alStockTaken.getEmployeeId(), strtDate);
            
            if (schId != 0) {
                
                int tknDt = strtDate.getDate();
                
                String Fld = "D" + tknDt;
                
                long sch = getSchedule(schId, tknDt);
                
                if (sch != OidAL) {
                    
                    int status = SessEmpSchedule.updateSchedule(strtDate, schId, "0");
                    
                }
                
            }
            
            long tmpDate = strtDate.getTime() + (24 * 60 * 60 * 1000);
            newDate = new Date(tmpDate);
            strtDate = newDate;
            
            
        }
    }
    
    public static long getSchedule(long schId, int dt) {
        
        String FldDt = "D" + dt;
        DBResultSet dbrs = null;
        try {
            
            String sql = "SELECT " + FldDt + " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " WHERE "
                    + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMP_SCHEDULE_ID] + " = " + schId;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                long schOid = rs.getInt(1);
                return schOid;
            }
            
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @param scheduleID
     */
    public static void updateStatusSchedule(long scheduleID) {
        
        DBResultSet dbrs = null;
        
        try {
            String sql = "UPDATE " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " SET "
                    + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_SCHEDULE_TYPE] + " = " + PstEmpSchedule.SCHEDULE_ORIGINAL
                    + " WHERE " + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMP_SCHEDULE_ID] + " = " + scheduleID;
            
            System.out.println("SQL update doc status " + sql);
            
            int i = DBHandler.execUpdate(sql);
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
    }

    /**
     * @Author Roy Andika
     * @param employeeId
     * @param TknDt
     * @param FnsDt
     * @return true (taken melebihi date expired), false (taken kurang dari
     * waktu expired)
     */
    public static boolean getStatusLL(long employeeId, Date TknDt, Date FnsDt) {

        /* pengambilan data stock yang aktif */
        String whereLL = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF;
        
        Vector listLL = new Vector();
        
        try {
            listLL = PstLLStockManagement.list(0, 0, whereLL, null);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        if (listLL != null && listLL.size() > 0) {
            
            LLStockManagement llStockManagement = new LLStockManagement();
            
            llStockManagement = (LLStockManagement) listLL.get(0);
            
            if (llStockManagement.getExpiredDate() != null) {

                /* kondisi dimana expired 1 dan expired 2 sudah ada */
                if (llStockManagement.getExpiredDate2() != null) {

                    /* kondisi exp 1 > exp 2 */
                    if (llStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L) > llStockManagement.getExpiredDate2().getTime() / (24L * 60L * 60L * 1000L)) {

                        /* status taken apakah berada dalam range entitle date dan expired date */
                        boolean LlExp = false;
                        
                        LlExp = getStatusTknLongLeave(llStockManagement.getEntitledDate(), llStockManagement.getExpiredDate(), TknDt);
                        
                        if (LlExp == true) {
                            
                            return false;
                            
                        } else {
                            
                            return true;
                            
                        }

                        /*
                         if(TknDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate().getTime()/(24L*60L*60L*1000L) || 
                         FnsDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate().getTime()/(24L*60L*60L*1000L)){
                         return true;
                         }else{
                         return false;
                         }*/


                        /* kondisi exp 1 < exp 2 */
                    } else if (llStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L) < llStockManagement.getExpiredDate2().getTime() / (24L * 60L * 60L * 1000L)) {
                        
                        boolean LlExp = false;
                        
                        LlExp = getStatusTknLongLeave(llStockManagement.getEntitledDate(), llStockManagement.getExpiredDate2(), TknDt);
                        
                        if (LlExp == true) {
                            
                            return false;
                            
                        } else {
                            
                            return true;
                            
                        }

                        /*if(TknDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate2().getTime()/(24L*60L*60L*1000L) || 
                         FnsDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate2().getTime()/(24L*60L*60L*1000L)){
                         return true;
                         }else{
                         return false;
                         }*/

                        /* kondisi exp 1 = exp 2 */
                    } else {
                        
                        boolean LlExp = false;
                        
                        LlExp = getStatusTknLongLeave(llStockManagement.getEntitledDate(), llStockManagement.getExpiredDate2(), TknDt);
                        
                        if (LlExp == true) {
                            
                            return false;
                            
                        } else {
                            
                            return true;
                            
                        }

                        /*if(TknDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate().getTime()/(24L*60L*60L*1000L) || 
                         FnsDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate().getTime()/(24L*60L*60L*1000L)){
                         return true;
                         }else{
                         return false;
                         }*/
                    }

                    /* exp 2 = null */
                } else {
                    
                    boolean LlExp = false;
                    
                    LlExp = getStatusTknLongLeave(llStockManagement.getEntitledDate(), llStockManagement.getExpiredDate(), TknDt);
                    
                    if (LlExp == true) {
                        
                        return false;
                        
                    } else {
                        
                        return true;
                        
                    }

                    /*if(TknDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate().getTime()/(24L*60L*60L*1000L) || 
                     FnsDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate().getTime()/(24L*60L*60L*1000L)){
                     return true;
                     }else{
                     return false;
                     }*/
                }


                /* kondisi dimana expired 1 = null && expired 2 != null  */
            } else if (llStockManagement.getExpiredDate() == null && llStockManagement.getExpiredDate2() != null) {
                
                boolean LlExp = false;
                
                LlExp = getStatusTknLongLeave(llStockManagement.getEntitledDate(), llStockManagement.getExpiredDate(), TknDt);
                
                if (LlExp == true) {
                    
                    return false;
                    
                } else {
                    
                    return true;
                    
                }

                /*if(TknDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate2().getTime()/(24L*60L*60L*1000L) || 
                 FnsDt.getTime()/(24L*60L*60L*1000L) > llStockManagement.getExpiredDate2().getTime()/(24L*60L*60L*1000L)){
                 return true;
                 }else{
                 return false;
                 }*/
                
            }
            
        }
        return false;
    }

    /**
     * @Author Roy Andika
     * @param entitleDt
     * @param tknDt
     * @param expDate
     * @return true - > tkn berada dalam range entitle dan exp date
     */
    public static boolean getStatusTknLongLeave(Date entDt, Date expDt, Date tknDt) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT '" + Formater.formatDate(tknDt, "yyyy-MM-dd") + "' >= '"
                    + Formater.formatDate(entDt, "yyyy-MM-dd") + "' AND '" + Formater.formatDate(tknDt, "yyyy-MM-dd")
                    + "' < '" + Formater.formatDate(expDt, "yyyy-MM-dd") + "'";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                int i = 0;
                
                i = rs.getInt(1);
                
                if (i == 1) {
                    return true;
                } else {
                    return false;
                }
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return false;
    }

    /**
     * @Author Roy Andika
     * @param start
     * @param fns
     * @return selisih antara finish taken date dan start taken date
     */
    public static int DATE_DIFERENT(Date start, Date fns) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DATEDIFF('" + Formater.formatDate(fns, "yyyy-MM-dd") + "',"
                    + "'" + Formater.formatDate(start, "yyyy-MM-dd") + "')+1";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                int x = 0;
                
                x = rs.getInt(1);
                
                if (x >= 0) {
                    
                    return x;
                    
                } else {
                    
                    return 0;
                    
                }
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        return 0;
    }
    
    public static void updateApproval(long OidleaveApplication, long oidApproval, Date dateApp, int indexApp) {
        
        LeaveApplication leaveApplication = new LeaveApplication();
        
        try {
            leaveApplication = (LeaveApplication) PstLeaveApplication.fetchExc(OidleaveApplication);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        
        if (indexApp == 1) {
            leaveApplication.setDepHeadApproval(oidApproval);
            leaveApplication.setDepHeadApproveDate(dateApp);
        } else if (indexApp == 2) {
            leaveApplication.setHrManApproval(oidApproval);
            leaveApplication.setHrManApproveDate(dateApp);
        } else {
            leaveApplication.setGmApproval(oidApproval);
            leaveApplication.setGmApprovalDate(dateApp);
        }
        
        try {
            PstLeaveApplication.updateExc(leaveApplication);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    /**
     * @Author Roy Andika
     * @param ApprovalOid
     * @param listNeedApproval
     * @Desc UNTUK MENG-APPROVE LIST APPROVAL SESUAI DENGAN YANG DIPILIH
     */
    public static void approvalCheckBox(long ApprovalOid, Vector listNeedApproval) {
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        
        if (ApprovalOid != 0 && listNeedApproval != null && listNeedApproval.size() > 0) {
            
            for (int i = 0; i < listNeedApproval.size(); i++) {
                
                LeaveApplication objleaveApplication = new LeaveApplication();
                LeaveApplication leaveApplication = new LeaveApplication();
                
                try {
                    objleaveApplication = (LeaveApplication) listNeedApproval.get(i);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    leaveApplication = PstLeaveApplication.fetchExc(objleaveApplication.getOID());
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* jika form leave application di temukan */
                if (leaveApplication.getOID() != 0) {
                    
                    boolean haveApproval = false;
                    
                    int maxApproval = leaveConfig.getMaxApproval(leaveApplication.getEmployeeId());
                    
                    if ((maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)) {
                        
                        if (leaveApplication.getDepHeadApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setDepHeadApproval(ApprovalOid);
                            objApplication.setDepHeadApproveDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            
                        }
                    }
                    
                    if (haveApproval == false && (maxApproval == I_Leave.LEAVE_APPROVE_2 || maxApproval == I_Leave.LEAVE_APPROVE_3)) {
                        
                        if (leaveApplication.getHrManApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setHrManApproval(ApprovalOid);
                            objApplication.setHrManApproveDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                        }
                    }
                    
                    if (haveApproval == false && maxApproval == I_Leave.LEAVE_APPROVE_3) {
                        
                        if (leaveApplication.getGmApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setGmApproval(ApprovalOid);
                            objApplication.setGmApprovalDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            
                        }
                    }
                }
            }
        }
    }

    /**
     * Keterangqan: untuk melakukan approvall dan merubah statusnya menjadi
     * Approve
     *
     * @param ApprovalOid
     * @param listNeedApproval
     */
    public static void approvalandSetApproveCheckBox(long ApprovalOid, Vector listNeedApproval) {
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        
        if (ApprovalOid != 0 && listNeedApproval != null && listNeedApproval.size() > 0) {
            
            for (int i = 0; i < listNeedApproval.size(); i++) {
                
                LeaveApplication objleaveApplication = new LeaveApplication();
                LeaveApplication leaveApplication = new LeaveApplication();
                
                try {
                    objleaveApplication = (LeaveApplication) listNeedApproval.get(i);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    leaveApplication = PstLeaveApplication.fetchExc(objleaveApplication.getOID());
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* jika form leave application di temukan */
                if (leaveApplication.getOID() != 0) {
                    
                    boolean haveApproval = false;
                    
                    int maxApproval = leaveConfig.getMaxApproval(leaveApplication.getEmployeeId());
                    
                    if ((maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)) {
                        
                        if (leaveApplication.getDepHeadApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setDepHeadApproval(ApprovalOid);
                            objApplication.setDepHeadApproveDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            boolean status_excecution = SessLeaveApplication.processSetStatusToBeApp(objApplication.getOID());
                        }
                    }
                    
                    if (haveApproval == false && (maxApproval == I_Leave.LEAVE_APPROVE_2 || maxApproval == I_Leave.LEAVE_APPROVE_3)) {
                        
                        if (leaveApplication.getHrManApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setHrManApproval(ApprovalOid);
                            objApplication.setHrManApproveDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            boolean status_excecution = SessLeaveApplication.processSetStatusToBeApp(objApplication.getOID());
                        }
                    }
                    
                    if (haveApproval == false && maxApproval == I_Leave.LEAVE_APPROVE_3) {
                        
                        if (leaveApplication.getGmApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setGmApproval(ApprovalOid);
                            objApplication.setGmApprovalDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            boolean status_excecution = SessLeaveApplication.processSetStatusToBeApp(objApplication.getOID());
                            
                        }
                    }
                }
            }
        }
    }

    /**
     * Keterangan: memebrikan dan set oid approval create by satrya ramayu
     * 2013-03-13
     *
     * @param ApprovalOid
     * @param listNeedApproval
     */
    public static void approvalandSetStatusApproveCheckBox(long ApprovalOid, Vector listNeedApproval) {
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        
        if (ApprovalOid != 0 && listNeedApproval != null && listNeedApproval.size() > 0) {
            
            for (int i = 0; i < listNeedApproval.size(); i++) {
                
                LeaveApplication objleaveApplication = new LeaveApplication();
                LeaveApplication leaveApplication = new LeaveApplication();
                
                try {
                    objleaveApplication = (LeaveApplication) listNeedApproval.get(i);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    leaveApplication = PstLeaveApplication.fetchExc(objleaveApplication.getOID());
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* jika form leave application di temukan */
                if (leaveApplication.getOID() != 0) {
                    
                    boolean haveApproval = false;
                    
                    int maxApproval = leaveConfig.getMaxApproval(leaveApplication.getEmployeeId());
                    
                    if ((maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)) {
                        
                        if (leaveApplication.getDepHeadApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setDepHeadApproval(ApprovalOid);
                            objApplication.setDepHeadApproveDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            boolean status_excecution = SessLeaveApplication.processSetStatusApprove(objApplication.getOID());
                        }
                    }
                    
                    if (haveApproval == false && (maxApproval == I_Leave.LEAVE_APPROVE_2 || maxApproval == I_Leave.LEAVE_APPROVE_3)) {
                        
                        if (leaveApplication.getHrManApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setHrManApproval(ApprovalOid);
                            objApplication.setHrManApproveDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            boolean status_excecution = SessLeaveApplication.processSetStatusApprove(objApplication.getOID());
                        }
                    }
                    
                    if (haveApproval == false && maxApproval == I_Leave.LEAVE_APPROVE_3) {
                        
                        if (leaveApplication.getGmApproval() == 0) {
                            
                            LeaveApplication objApplication = new LeaveApplication();
                            try {
                                objApplication = PstLeaveApplication.fetchExc(leaveApplication.getOID());
                            } catch (Exception E) {
                                System.out.println("Exception " + E.toString());
                            }
                            
                            objApplication.setGmApproval(ApprovalOid);
                            objApplication.setGmApprovalDate(new Date());
                            
                            haveApproval = true;
                            
                            processAppDepHead(objApplication, maxApproval);
                            boolean status_excecution = SessLeaveApplication.processSetStatusApprove(objApplication.getOID());
                            
                        }
                    }
                }
            }
        }
    }

    /**
     * @Author Roy Andika
     * @param ObjLeaveApplication
     */
    private static void processAppDepHead(LeaveApplication objLeaveApplication, int maxApproval) {
        
        if (objLeaveApplication.getOID() != 0) {
            
            boolean stsDokApprov1 = false;
            
            if (objLeaveApplication.getDocStatus() == 1) {
                
                if (objLeaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_1) {
                    if (objLeaveApplication.getDepHeadApproval() != 0) {
                        stsDokApprov1 = true;
                        objLeaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                    }
                } else if (objLeaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_2) {
                    if (objLeaveApplication.getDepHeadApproval() != 0 && objLeaveApplication.getHrManApproval() != 0) {
                        stsDokApprov1 = true;
                        objLeaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                    }
                } else if (objLeaveApplication.getEmployeeId() != 0 && maxApproval == I_Leave.LEAVE_APPROVE_3) {
                    if (objLeaveApplication.getGmApproval() != 0) {
                        stsDokApprov1 = true;
                        objLeaveApplication.setDocStatus(PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED);
                    }
                }
            }
            
            PstLeaveApplication.updateScheduleLeave(objLeaveApplication, stsDokApprov1);
            
        }
    }

    /**
     * @Author : Roy Andika
     * @Desc : UNTUK MENGECEK LIST APAKAH HARUS DI APPROVE or MENUNGGU APPROVE
     * ORANG LAIN
     * @return
     */
    public static boolean getMustApprove(LeaveApplication leaveApplication, long oidApproval) {
        
        if (leaveApplication.getOID() != 0 && oidApproval != 0) {
            
            I_Leave leaveConfig = null;
            
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }
            
            int maxApproval = leaveConfig.getMaxApproval(leaveApplication.getEmployeeId());
            
            boolean app_dept_empty = false;
            boolean app_hr_empty = false;
            boolean app_gm_empty = false;
            
            boolean haveApproval_Dept = false;
            boolean haveApproval_HR = false;
            boolean haveApproval_GM = false;
            
            if ((maxApproval == I_Leave.LEAVE_APPROVE_1 || maxApproval == I_Leave.LEAVE_APPROVE_2)) {
                
                if (leaveApplication.getDepHeadApproval() == 0) {
                    
                    app_dept_empty = true;
                    
                    Vector listDivHead = leaveConfig.getApprovalDepartmentHead(leaveApplication.getEmployeeId());
                    
                    if (listDivHead != null && listDivHead.size() > 0) {
                        
                        for (int i = 0; i < listDivHead.size(); i++) {
                            
                            Employee objEmp = (Employee) listDivHead.get(i);
                            
                            if (objEmp.getOID() == oidApproval) {
                                
                                haveApproval_Dept = true;
                                
                            }
                        }
                    }
                }
            }
            
            if ((maxApproval == I_Leave.LEAVE_APPROVE_2 || maxApproval == I_Leave.LEAVE_APPROVE_3)) {
                
                if (leaveApplication.getHrManApproval() == 0) {
                    
                    app_hr_empty = true;
                    
                    Vector listHRMan = leaveConfig.listHRManager(leaveApplication.getEmployeeId());
                    
                    if (listHRMan != null && listHRMan.size() > 0) {
                        
                        for (int i = 0; i < listHRMan.size(); i++) {
                            
                            Employee objEmp = (Employee) listHRMan.get(i);
                            
                            if (objEmp.getOID() == oidApproval) {
                                
                                haveApproval_HR = true;
                                
                            }
                            
                        }
                        
                    }
                }
            }
            
            if (maxApproval == I_Leave.LEAVE_APPROVE_3) {
                
                if (leaveApplication.getGmApproval() == 0) {
                    
                    app_gm_empty = true;
                    
                    Vector vectPositionLvl1 = new Vector(1, 1);
                    vectPositionLvl1.add("" + PstPosition.LEVEL_GENERAL_MANAGER);
                    
                    Employee objEmployee = new Employee();
                    
                    try {
                        objEmployee = PstEmployee.fetchExc(leaveApplication.getEmployeeId());
                    } catch (Exception E) {
                        System.out.println("EXCEPTION " + E.toString());
                    }
                    
                    Vector listDivHead = SessEmployee.listEmployeeByPositionLevelGeneralM(objEmployee, vectPositionLvl1);
                    
                    for (int i = 0; i < listDivHead.size(); i++) {
                        
                        Employee objEmp = (Employee) listDivHead.get(i);
                        
                        if (objEmp.getOID() == oidApproval) {
                            
                            haveApproval_GM = true;
                            
                        }
                    }
                }
            }
            
            if (app_dept_empty == true) {
                
                if (haveApproval_Dept == true) {
                    
                    return true;
                    
                } else {
                    
                    return false;
                    
                }
                
            } else if (app_hr_empty == true) {
                
                if (haveApproval_HR == true) {
                    
                    return true;
                    
                } else {
                    
                    return false;
                    
                }
                
            } else if (app_gm_empty == true) {
                
                if (haveApproval_GM == true) {
                    
                    return true;
                    
                } else {
                    
                    return false;
                    
                }
            }
        }
        
        return false;
        
    }

    /**
     * @Author : Roy Andika
     * @Desc : UNTUK MENDAPATKAN LEAVE REPORT BERDASARKAN PERIODE YANG
     * DIINGINKAN ( AL, LL, and DP)
     * @param : startPeriod
     * @param : endPeriod
     * @param : srcLeaveManagement
     */
    public static Vector sumLeave_Department(Date startPeriod, Date endPeriod) {
        Vector section = new Vector();
        String joinDept = "";
        
        try {
            joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT_REPORT_EXCEPTION");
        } catch (Exception E) {
            System.out.println("[excption] " + E.toString());
        }
        
        Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);
        
        String[] grp = (String[]) depGroup.get(0);
        
        String depException = "";
        
        if (grp.length > 0) {
            
            depException = depException + " ( ";
            for (int g = 0; g < grp.length; g++) {
                
                if (g == 0) {
                    depException = depException + grp[g];
                } else {
                    depException = depException + "," + grp[g];
                }
                
            }
            depException = depException + " ) ";
            
        }
        
        if (startPeriod == null || endPeriod == null) {
            return null;
        }
        
        DBResultSet dbrs = null;
        
        try {

            /* sql untuk mendapatkan department id and name department */
            String sql = "SELECT " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ","
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " FROM "
                    + PstDepartment.TBL_HR_DEPARTMENT;
            
            if (depException.length() > 0) {
                sql = sql + " WHERE " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " NOT IN " + depException;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector result = new Vector();
            
            while (rs.next()) {
                
                RepLevDepartment repLevDepartment = new RepLevDepartment();
                
                long deptId = 0;
                String department = "";
                int countEmployee = 0;

                /* ==== DP ===== */
                float qty_dp_before = 0;
                float tkn_dp_before = 0;
                float tkn_dp_expired_before = 0;
                float qty_dp_current = 0;
                float tkn_dp_current = 0;
                float tkn_dp_expired_current = 0;

                /* ==== AL ==== */
                float qty_al_before = 0;
                float tkn_al_before = 0;
                float qty_al_current = 0;
                float tkn_al_current = 0;

                /* ==== LL ===== */
                float qty_ll_before = 0;
                float tkn_ll_before = 0;
                float tkn_ll_expired_before = 0;
                float qty_ll_current = 0;
                float tkn_ll_current = 0;
                float tkn_ll_expired_current = 0;
//                /**
//                 *                 /* ==== DP ===== */
//                int qty_dp_before = 0;
//                int tkn_dp_before = 0;
//                int tkn_dp_expired_before = 0;
//                int qty_dp_current = 0;
//                int tkn_dp_current = 0;
//                int tkn_dp_expired_current = 0;
//
//                /* ==== AL ==== */
//                int qty_al_before = 0;
//                int tkn_al_before = 0;
//                int qty_al_current = 0;
//                int tkn_al_current = 0;
//
//                /* ==== LL ===== */
//                int qty_ll_before = 0;
//                int tkn_ll_before = 0;
//                int tkn_ll_expired_before = 0;
//                int qty_ll_current = 0;
//                int tkn_ll_current = 0;
//                int tkn_ll_expired_current = 0;
//                 */

                /* Untuk mendapatkan id department */
                try {
                    deptId = rs.getLong(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan nama department */
                try {
                    department = rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan jumlah employee yang aktif */
                try {
                    countEmployee = sumEmployee(deptId, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp before start period */
                try {
                    qty_dp_before = getPrevQtyReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken before start period */
                try {
                    tkn_dp_before = getPrevTakenReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken expired before */
                try {
                    tkn_dp_expired_before = getPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp current period */
                try {
                    qty_dp_current = getCurrentQtyReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp current period */
                try {
                    tkn_dp_current = getCurrentTakenReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp expired current */
                try {
                    tkn_dp_expired_current = getCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan AL before start period */
                try {
                    qty_al_before = getPrevQtyALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken before start period */
                try {
                    tkn_al_before = getPrevTakenALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al qty current period */
                try {
                    qty_al_current = getCurrentQtyAlReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken current period */
                try {
                    tkn_al_current = getCurrentTakenAL(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_before = getPrevTakenLLReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_before = getLLPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    qty_ll_current = getLLCurrentPeriod(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_current = getCurrentTakenLLReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_current = getLLCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* ==== DEPARTMENT ==== */
                repLevDepartment.setDepartmentId(deptId);
                repLevDepartment.setDepartment(department);
                repLevDepartment.setCountEmployee(countEmployee);

                /* === DP ==== */
                repLevDepartment.setDpQtyBeforeStartPeriod(qty_dp_before);                
                repLevDepartment.setDpTknBeforeStartPeriod(tkn_dp_before);
                repLevDepartment.setDpTknExpBeforeStartPeriod(tkn_dp_expired_before);
                repLevDepartment.setDpQtyCurrentPeriod(qty_dp_current);
                repLevDepartment.setDpTknCurrentPeriod(tkn_dp_current);
                repLevDepartment.setDpTknExpiredCurrentPeriod(tkn_dp_expired_current);

                /* ==== AL ==== */
                repLevDepartment.setAlQtyBeforeStartPeriod(qty_al_before);
                repLevDepartment.setAlTknBeforeStartPeriod(tkn_al_before);
                repLevDepartment.setAlQtyCurrentPeriod(qty_al_current);
                repLevDepartment.setAlTknCurrentPeriod(tkn_al_current);

                /* ===== LL ==== */
                repLevDepartment.setLLQtyBeforeStartPeriod(qty_ll_before);
                repLevDepartment.setLLTknBeforeStartPeriod(tkn_ll_before);
                repLevDepartment.setLLTknExpBeforeStartPeriod(tkn_ll_expired_before);
                repLevDepartment.setLLQtyCurrentPeriod(qty_ll_current);
                repLevDepartment.setLLTknCurrentPeriod(tkn_ll_current);
                repLevDepartment.setLLTknExpiredCurrentPeriod(tkn_ll_expired_current);
                result.add(repLevDepartment);
                
            }
            
            return result;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan Data leave per periode (bedasarkan periode)
     * @param periodId
     * @return
     */
    public static Vector sumLeave_Department(long periodId) {
        Vector section = new Vector();
        
        if (periodId == 0) {
            
            return null;
        }

        /* Untuk mendapatkan join department yang tidak akan di tampilkan */
        
        String joinDept = "";
        
        try {
            joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT_REPORT_EXCEPTION");
        } catch (Exception E) {
            System.out.println("[excption] " + E.toString());
        }
        
        Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);
        
        String[] grp = (String[]) depGroup.get(0);
        
        String depException = "";
        
        if (grp.length > 0) {
            
            depException = depException + " ( ";
            for (int g = 0; g < grp.length; g++) {
                
                if (g == 0) {
                    depException = depException + grp[g];
                } else {
                    depException = depException + "," + grp[g];
                }
                
            }
            depException = depException + " ) ";
            
        }
        
        Period period = new Period();
        
        try {
            period = PstPeriod.fetchExc(periodId);
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        }
        
        Date startPeriod = new Date();
        Date endPeriod = new Date();
        
        try {
            startPeriod = period.getStartDate();
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        }
        
        try {
            endPeriod = period.getEndDate();
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        }
        
        DBResultSet dbrs = null;
        
        try {

            /* sql untuk mendapatkan department id and name department */
            String sql = "SELECT " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ","
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " FROM "
                    + PstDepartment.TBL_HR_DEPARTMENT;
            
            if (depException.length() > 0) {
                sql = sql + " WHERE " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " NOT IN " + depException;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector result = new Vector();
            
            while (rs.next()) {
                
                RepLevDepartment repLevDepartment = new RepLevDepartment();
                
                long deptId = 0;
                String department = "";
                int countEmployee = 0;

                /* ==== DP ===== */
                float qty_dp_before = 0;
                float tkn_dp_before = 0;
                float tkn_dp_expired_before = 0;
                float qty_dp_current = 0;
                float tkn_dp_current = 0;
                float tkn_dp_expired_current = 0;

                /* ==== AL ==== */
                float qty_al_before = 0;
                float tkn_al_before = 0;
                float qty_al_current = 0;
                float tkn_al_current = 0;

                /* ==== LL ===== */
                float qty_ll_before = 0;
                float tkn_ll_before = 0;
                float tkn_ll_expired_before = 0;
                float qty_ll_current = 0;
                float tkn_ll_current = 0;
                float tkn_ll_expired_current = 0;
//                    /* ==== DP ===== */
//                int qty_dp_before = 0;
//                int tkn_dp_before = 0;
//                int tkn_dp_expired_before = 0;
//                int qty_dp_current = 0;
//                int tkn_dp_current = 0;
//                int tkn_dp_expired_current = 0;
//
//                /* ==== AL ==== */
//                int qty_al_before = 0;
//                int tkn_al_before = 0;
//                int qty_al_current = 0;
//                int tkn_al_current = 0;
//
//                /* ==== LL ===== */
//                int qty_ll_before = 0;
//                int tkn_ll_before = 0;
//                int tkn_ll_expired_before = 0;
//                int qty_ll_current = 0;
//                int tkn_ll_current = 0;
//                int tkn_ll_expired_current = 0;

                /* Untuk mendapatkan id department */
                try {
                    deptId = rs.getLong(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan nama department */
                try {
                    department = rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan jumlah employee yang aktif */
                try {
                    countEmployee = sumEmployee(deptId, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp before start period */
                try {
                    qty_dp_before = getPrevQtyReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken before start period */
                try {
                    tkn_dp_before = getPrevTakenReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken expired before */
                try {
                    tkn_dp_expired_before = getPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp current period */
                try {
                    qty_dp_current = getCurrentQtyReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp current period */
                try {
                    tkn_dp_current = getCurrentTakenReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp expired current */
                try {
                    tkn_dp_expired_current = getCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan AL before start period */
                try {
                    qty_al_before = getPrevQtyALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken before start period */
                try {
                    tkn_al_before = getPrevTakenALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al qty current period */
                try {
                    qty_al_current = getCurrentQtyAlReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken current period */
                try {
                    tkn_al_current = getCurrentTakenAL(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    qty_ll_before = getLLPrevPeriod(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_before = getPrevTakenLLReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_before = getLLPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    qty_ll_current = getLLCurrentPeriod(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_current = getCurrentTakenLLReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_current = getLLCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* ==== DEPARTMENT ==== */
                repLevDepartment.setDepartmentId(deptId);
                repLevDepartment.setDepartment(department);
                repLevDepartment.setCountEmployee(countEmployee);

                /* === DP ==== */
                repLevDepartment.setDpQtyBeforeStartPeriod(qty_dp_before);
                repLevDepartment.setDpTknBeforeStartPeriod(tkn_dp_before);
                repLevDepartment.setDpTknExpBeforeStartPeriod(tkn_dp_expired_before);
                repLevDepartment.setDpQtyCurrentPeriod(qty_dp_current);
                repLevDepartment.setDpTknCurrentPeriod(tkn_dp_current);
                repLevDepartment.setDpTknExpiredCurrentPeriod(tkn_dp_expired_current);

                /* ==== AL ==== */
                repLevDepartment.setAlQtyBeforeStartPeriod(qty_al_before);
                repLevDepartment.setAlTknBeforeStartPeriod(tkn_al_before);
                repLevDepartment.setAlQtyCurrentPeriod(qty_al_current);
                repLevDepartment.setAlTknCurrentPeriod(tkn_al_current);

                /* ===== LL ==== */
                repLevDepartment.setLLQtyBeforeStartPeriod(qty_ll_before);
                repLevDepartment.setLLTknBeforeStartPeriod(tkn_ll_before);
                repLevDepartment.setLLTknExpBeforeStartPeriod(tkn_ll_expired_before);
                repLevDepartment.setLLQtyCurrentPeriod(qty_ll_current);
                repLevDepartment.setLLTknCurrentPeriod(tkn_ll_current);
                repLevDepartment.setLLTknExpiredCurrentPeriod(tkn_ll_expired_current);
                
                result.add(repLevDepartment);
                
            }
            
            return result;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    /**
     * @Author Roy A.
     * @Decs Untuk mendapatkan Report leave summary department.
     * @param srcLeaveAppAlClosing
     * @return
     */
    public static Vector sumLeave_Department(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {
        Vector section = new Vector();
        if (srcLeaveAppAlClosing == null) {
            
            return null;
            
        }


        /* Declarasi untuk strat period and end period */
        Date startPeriod = new Date();
        Date endPeriod = new Date();
        
        if (srcLeaveAppAlClosing.getRadioBtn() == 0) { /* Pengecekan jika yang dipilih berdasarkan periode */
            
            if (srcLeaveAppAlClosing.getPeriodId() == 0) {
                return null;
            }
            
            Period period = new Period();
            try {
                period = PstPeriod.fetchExc(srcLeaveAppAlClosing.getPeriodId());
            } catch (Exception e) {
                System.out.println("[exception] SessLeaveApplication.sumLeave_Department(SrcLeaveAppAlClosing srcLeaveAppAlClosing) :: " + e.toString());
            }
            
            startPeriod = period.getStartDate();
            endPeriod = period.getEndDate();
            
        } else { /* Pengecekan jika waktu yang dipilih berdasarkan time */
            
            startPeriod = srcLeaveAppAlClosing.getEmpCommancingDateStart();
            endPeriod = srcLeaveAppAlClosing.getEmpCommancingDateEnd();
            
        }

        /* Untuk mendapatkan join department yang tidak akan di tampilkan */
        
        String joinDept = "";
        
        try {
            joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT_REPORT_EXCEPTION");
        } catch (Exception E) {
            System.out.println("[excption] " + E.toString());
        }
        
        Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);
        
        String[] grp = (String[]) depGroup.get(0);
        
        String depException = "";
        
        if (grp.length > 0) {
            
            depException = depException + " ( ";
            for (int g = 0; g < grp.length; g++) {
                
                if (g == 0) {
                    depException = depException + grp[g];
                } else {
                    depException = depException + "," + grp[g];
                }
                
            }
            depException = depException + " ) ";
            
        }
        
        DBResultSet dbrs = null;
        
        try {

            /* sql untuk mendapatkan department id and name department */
            String sql = "SELECT " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ","
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " FROM "
                    + PstDepartment.TBL_HR_DEPARTMENT;
            
            if (depException.length() > 0) {
                sql = sql + " WHERE " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " NOT IN " + depException;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector result = new Vector();
            
            while (rs.next()) {
                
                RepLevDepartment repLevDepartment = new RepLevDepartment();
                
                long deptId = 0;
                String department = "";
                int countEmployee = 0;

                /* ==== DP ===== */
                float qty_dp_before = 0;
                float tkn_dp_before = 0;
                float tkn_dp_expired_before = 0;
                float qty_dp_current = 0;
                float tkn_dp_current = 0;
                float tkn_dp_expired_current = 0;

                /* ==== AL ==== */
                float qty_al_before = 0;
                float tkn_al_before = 0;
                float qty_al_current = 0;
                float tkn_al_current = 0;

                /* ==== LL ===== */
                float qty_ll_before = 0;
                float tkn_ll_before = 0;
                float tkn_ll_expired_before = 0;
                float qty_ll_current = 0;
                float tkn_ll_current = 0;
                float tkn_ll_expired_current = 0;
/////* ==== DP ===== */
//                int qty_dp_before = 0;
//                int tkn_dp_before = 0;
//                int tkn_dp_expired_before = 0;
//                int qty_dp_current = 0;
//                int tkn_dp_current = 0;
//                int tkn_dp_expired_current = 0;
//
//                /* ==== AL ==== */
//                int qty_al_before = 0;
//                int tkn_al_before = 0;
//                int qty_al_current = 0;
//                int tkn_al_current = 0;
//
//                /* ==== LL ===== */
//                int qty_ll_before = 0;
//                int tkn_ll_before = 0;
//                int tkn_ll_expired_before = 0;
//                int qty_ll_current = 0;
//                int tkn_ll_current = 0;
//                int tkn_ll_expired_current = 0;

                /* Untuk mendapatkan id department */
                try {
                    deptId = rs.getLong(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan nama department */
                try {
                    department = rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan jumlah employee yang aktif */
                try {
                    countEmployee = sumEmployee(deptId, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp before start period */
                try {
                    qty_dp_before = getPrevQtyReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken before start period */
                try {
                    tkn_dp_before = getPrevTakenReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken expired before */
                try {
                    tkn_dp_expired_before = getPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp current period */
                try {
                    qty_dp_current = getCurrentQtyReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp current period */
                try {
                    tkn_dp_current = getCurrentTakenReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp expired current */
                try {
                    tkn_dp_expired_current = getCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan AL before start period */
                try {
                    qty_al_before = getPrevQtyALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken before start period */
                try {
                    tkn_al_before = getPrevTakenALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al qty current period */
                try {
                    qty_al_current = getCurrentQtyAlReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken current period */
                try {
                    tkn_al_current = getCurrentTakenAL(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                //UPDATE BY DEVIN 2014-04-03
                float totalToBeTaken = 0;
                
                try {
                    totalToBeTaken = SessLeaveApp.getTobeTakenDept(deptId, startPeriod, endPeriod, section);                    
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    qty_ll_before = getLLPrevPeriod(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_before = getPrevTakenLLReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_before = getLLPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    qty_ll_current = getLLCurrentPeriod(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_current = getCurrentTakenLLReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_current = getLLCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* ==== DEPARTMENT ==== */
                repLevDepartment.setDepartmentId(deptId);
                repLevDepartment.setDepartment(department);
                repLevDepartment.setCountEmployee(countEmployee);

                /* === DP ==== */
                repLevDepartment.setDpQtyBeforeStartPeriod(qty_dp_before);
                repLevDepartment.setDpTknBeforeStartPeriod(tkn_dp_before);
                repLevDepartment.setDpTknExpBeforeStartPeriod(tkn_dp_expired_before);
                repLevDepartment.setDpQtyCurrentPeriod(qty_dp_current);
                repLevDepartment.setDpTknCurrentPeriod(tkn_dp_current);
                repLevDepartment.setDpTknExpiredCurrentPeriod(tkn_dp_expired_current);

                /* ==== AL ==== */
                repLevDepartment.setAlQtyBeforeStartPeriod(qty_al_before);
                repLevDepartment.setAlTknBeforeStartPeriod(tkn_al_before);
                repLevDepartment.setAlQtyCurrentPeriod(qty_al_current);
                repLevDepartment.setAlTknCurrentPeriod(tkn_al_current);
                repLevDepartment.setAlToBeTaken(totalToBeTaken);
                /* ===== LL ==== */
                repLevDepartment.setLLQtyBeforeStartPeriod(qty_ll_before);
                repLevDepartment.setLLTknBeforeStartPeriod(tkn_ll_before);
                repLevDepartment.setLLTknExpBeforeStartPeriod(tkn_ll_expired_before);
                repLevDepartment.setLLQtyCurrentPeriod(qty_ll_current);
                repLevDepartment.setLLTknCurrentPeriod(tkn_ll_current);
                repLevDepartment.setLLTknExpiredCurrentPeriod(tkn_ll_expired_current);

                //update by devin 2014-04-04
                repLevDepartment.setDateFrom(endPeriod);
                repLevDepartment.setDateTo(startPeriod);
                repLevDepartment.setRadioButton(srcLeaveAppAlClosing.getRadioBtn());
                repLevDepartment.setOidPeriod(srcLeaveAppAlClosing.getPeriodId());
                
                result.add(repLevDepartment);
                
            }
            
            return result;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    //uodate by devin 2014-04-11
    public static Vector sumLeave_DepartmentGetSection(long oidDept, SrcLeaveAppAlClosing srcLeaveAppAlClosing, Date dateStart, Date dateEnd, int radioButton, long periodId) {
        Vector section = new Vector();
        if (srcLeaveAppAlClosing == null) {
            
            return new Vector();
            
        }
        if (oidDept != 0) {
            String whereClouse = PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=" + oidDept;
            section = PstSection.list(0, 0, whereClouse, "");
        }

        /* Declarasi untuk strat period and end period */
        Date startPeriod = new Date();
        Date endPeriod = new Date();
        
        if (radioButton == 0) { /* Pengecekan jika yang dipilih berdasarkan periode */
            
            if (periodId == 0) {
                return new Vector();
            }
            
            Period period = new Period();
            try {
                period = PstPeriod.fetchExc(periodId);
            } catch (Exception e) {
                System.out.println("[exception] SessLeaveApplication.sumLeave_Department(SrcLeaveAppAlClosing srcLeaveAppAlClosing) :: " + e.toString());
            }
            
            startPeriod = period.getStartDate();
            endPeriod = period.getEndDate();
            
        } else { /* Pengecekan jika waktu yang dipilih berdasarkan time */
            
            startPeriod = srcLeaveAppAlClosing.getEmpCommancingDateStart();
            endPeriod = srcLeaveAppAlClosing.getEmpCommancingDateEnd();
            
        }

        /* Untuk mendapatkan join department yang tidak akan di tampilkan */
        
        
        
        DBResultSet dbrs = null;
        
        try {

            /* sql untuk mendapatkan department id and name department */
            String sql = "SELECT " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ","
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " FROM "
                    + PstDepartment.TBL_HR_DEPARTMENT;
            
            if (oidDept > 0) {
                sql = sql + " WHERE " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + oidDept;
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            Vector result = new Vector();
            
            while (rs.next()) {
                
                RepLevDepartment repLevDepartment = new RepLevDepartment();
                
                long deptId = 0;
                String department = "";
                int countEmployee = 0;

                /* ==== DP ===== */
                float qty_dp_before = 0;
                float tkn_dp_before = 0;
                float tkn_dp_expired_before = 0;
                float qty_dp_current = 0;
                float tkn_dp_current = 0;
                float tkn_dp_expired_current = 0;

                /* ==== AL ==== */
                float qty_al_before = 0;
                float tkn_al_before = 0;
                float qty_al_current = 0;
                float tkn_al_current = 0;

                /* ==== LL ===== */
                float qty_ll_before = 0;
                float tkn_ll_before = 0;
                float tkn_ll_expired_before = 0;
                float qty_ll_current = 0;
                float tkn_ll_current = 0;
                float tkn_ll_expired_current = 0;
/////* ==== DP ===== */
//                int qty_dp_before = 0;
//                int tkn_dp_before = 0;
//                int tkn_dp_expired_before = 0;
//                int qty_dp_current = 0;
//                int tkn_dp_current = 0;
//                int tkn_dp_expired_current = 0;
//
//                /* ==== AL ==== */
//                int qty_al_before = 0;
//                int tkn_al_before = 0;
//                int qty_al_current = 0;
//                int tkn_al_current = 0;
//
//                /* ==== LL ===== */
//                int qty_ll_before = 0;
//                int tkn_ll_before = 0;
//                int tkn_ll_expired_before = 0;
//                int qty_ll_current = 0;
//                int tkn_ll_current = 0;
//                int tkn_ll_expired_current = 0;

                /* Untuk mendapatkan id department */
                try {
                    deptId = rs.getLong(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan nama department */
                try {
                    department = rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan jumlah employee yang aktif */
                try {
                    countEmployee = sumEmployee(deptId, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp before start period */
                try {
                    qty_dp_before = getPrevQtyReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken before start period */
                try {
                    tkn_dp_before = getPrevTakenReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken expired before */
                try {
                    tkn_dp_expired_before = getPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp current period */
                try {
                    qty_dp_current = getCurrentQtyReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp current period */
                try {
                    tkn_dp_current = getCurrentTakenReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp expired current */
                try {
                    tkn_dp_expired_current = getCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan AL before start period */
                try {
                    qty_al_before = getPrevQtyALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken before start period */
                try {
                    tkn_al_before = getPrevTakenALReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al qty current period */
                try {
                    qty_al_current = getCurrentQtyAlReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken current period */
                try {
                    tkn_al_current = getCurrentTakenAL(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                //UPDATE BY DEVIN 2014-04-03
                float totalToBeTaken = 0;
                
                try {
                    totalToBeTaken = SessLeaveApp.getTobeTakenDept(deptId, startPeriod, endPeriod, section);                    
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    qty_ll_before = getLLPrevPeriod(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_before = getPrevTakenLLReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_before = getLLPrevTakenExpiredReport(deptId, startPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    qty_ll_current = getLLCurrentPeriod(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_current = getCurrentTakenLLReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_current = getLLCurrentTakenExpiredReport(deptId, startPeriod, endPeriod, section);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* ==== DEPARTMENT ==== */
                repLevDepartment.setDepartmentId(deptId);
                repLevDepartment.setDepartment(department);
                repLevDepartment.setCountEmployee(countEmployee);

                /* === DP ==== */
                repLevDepartment.setDpQtyBeforeStartPeriod(qty_dp_before);
                repLevDepartment.setDpTknBeforeStartPeriod(tkn_dp_before);
                repLevDepartment.setDpTknExpBeforeStartPeriod(tkn_dp_expired_before);
                repLevDepartment.setDpQtyCurrentPeriod(qty_dp_current);
                repLevDepartment.setDpTknCurrentPeriod(tkn_dp_current);
                repLevDepartment.setDpTknExpiredCurrentPeriod(tkn_dp_expired_current);

                /* ==== AL ==== */
                repLevDepartment.setAlQtyBeforeStartPeriod(qty_al_before);
                repLevDepartment.setAlTknBeforeStartPeriod(tkn_al_before);
                repLevDepartment.setAlQtyCurrentPeriod(qty_al_current);
                repLevDepartment.setAlTknCurrentPeriod(tkn_al_current);
                repLevDepartment.setAlToBeTaken(totalToBeTaken);
                /* ===== LL ==== */
                repLevDepartment.setLLQtyBeforeStartPeriod(qty_ll_before);
                repLevDepartment.setLLTknBeforeStartPeriod(tkn_ll_before);
                repLevDepartment.setLLTknExpBeforeStartPeriod(tkn_ll_expired_before);
                repLevDepartment.setLLQtyCurrentPeriod(qty_ll_current);
                repLevDepartment.setLLTknCurrentPeriod(tkn_ll_current);
                repLevDepartment.setLLTknExpiredCurrentPeriod(tkn_ll_expired_current);

                //update by devin 2014-04-04
                repLevDepartment.setDateFrom(endPeriod);
                repLevDepartment.setDateTo(startPeriod);
                repLevDepartment.setRadioButton(srcLeaveAppAlClosing.getRadioBtn());
                repLevDepartment.setOidPeriod(srcLeaveAppAlClosing.getPeriodId());
                
                result.add(repLevDepartment);
                
            }
            
            return result;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    //update by devin 2014-04-03
    public static Vector sumLeave_Department2(long oidDept, SrcLeaveAppAlClosing srcLeaveAppAlClosing, Date dateStart, Date dateEnd, int radioButton, long periodId) {
        
        if (oidDept == 0) {
            
            return new Vector();
            
        }
        Date startPeriod = new Date();
        Date endPeriod = new Date();
        
        if (radioButton == 0) { /* Pengecekan jika yang dipilih berdasarkan periode */
            
            if (periodId == 0) {
                return new Vector();
            }
            
            Period period = new Period();
            try {
                period = PstPeriod.fetchExc(periodId);
            } catch (Exception e) {
                System.out.println("[exception] SessLeaveApplication.sumLeave_Department(SrcLeaveAppAlClosing srcLeaveAppAlClosing) :: " + e.toString());
            }
            
            startPeriod = period.getStartDate();            
            endPeriod = period.getEndDate();
            
        } else { /* Pengecekan jika waktu yang dipilih berdasarkan time */
            
            startPeriod = dateStart;
            endPeriod = dateEnd;
            
        }
        Vector result = new Vector();
        DBResultSet dbrs = null;
        /* Declarasi untuk strat period and end period */
        
        
        
        
        
        
        
        try {

            /* sql untuk mendapatkan department id and name department */
            String sql = "SELECT " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + ","
                    + PstSection.fieldNames[PstSection.FLD_SECTION] + " FROM "
                    + PstSection.TBL_HR_SECTION + " WHERE " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " = " + oidDept;
            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            
            
            while (rs.next()) {
                
                RepLevDepartment repLevDepartment = new RepLevDepartment();
                
                long deptId = 0;
                String department = "";
                int countEmployee = 0;

                /* ==== DP ===== */
                float qty_dp_before = 0;
                float tkn_dp_before = 0;
                float tkn_dp_expired_before = 0;
                float qty_dp_current = 0;
                float tkn_dp_current = 0;
                float tkn_dp_expired_current = 0;

                /* ==== AL ==== */
                float qty_al_before = 0;
                float tkn_al_before = 0;
                float qty_al_current = 0;
                float tkn_al_current = 0;

                /* ==== LL ===== */
                float qty_ll_before = 0;
                float tkn_ll_before = 0;
                float tkn_ll_expired_before = 0;
                float qty_ll_current = 0;
                float tkn_ll_current = 0;
                float tkn_ll_expired_current = 0;
/////* ==== DP ===== */
//                int qty_dp_before = 0;
//                int tkn_dp_before = 0;
//                int tkn_dp_expired_before = 0;
//                int qty_dp_current = 0;
//                int tkn_dp_current = 0;
//                int tkn_dp_expired_current = 0;
//
//                /* ==== AL ==== */
//                int qty_al_before = 0;
//                int tkn_al_before = 0;
//                int qty_al_current = 0;
//                int tkn_al_current = 0;
//
//                /* ==== LL ===== */
//                int qty_ll_before = 0;
//                int tkn_ll_before = 0;
//                int tkn_ll_expired_before = 0;
//                int qty_ll_current = 0;
//                int tkn_ll_current = 0;
//                int tkn_ll_expired_current = 0;

                /* Untuk mendapatkan id department */
                try {
                    deptId = rs.getLong(PstSection.fieldNames[PstSection.FLD_SECTION_ID]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan nama department */
                try {
                    department = rs.getString(PstSection.fieldNames[PstSection.FLD_SECTION]);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan jumlah employee yang aktif */
                try {
                    countEmployee = sumEmployeee(deptId);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp before start period */
                try {
                    qty_dp_before = getPrevQtyReportt(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken before start period */
                try {
                    tkn_dp_before = getPrevTakenReportt(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty taken expired before */
                try {
                    tkn_dp_expired_before = getPrevTakenExpiredReportt(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan qty dp current period */
                try {
                    qty_dp_current = getCurrentQtyReportt(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp current period */
                try {
                    tkn_dp_current = getCurrentTakenReportt(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan taken dp expired current */
                try {
                    tkn_dp_expired_current = getCurrentTakenExpiredReportt(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan AL before start period */
                try {
                    qty_al_before = getPrevQtyALReportt(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken before start period */
                try {
                    tkn_al_before = getPrevTakenALReportt(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al qty current period */
                try {
                    qty_al_current = getCurrentQtyAlReportt(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* Untuk mendapatkan Al taken current period */
                try {
                    tkn_al_current = getCurrentTakenALl(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                //UPDATE BY DEVIN 2014-04-03
                float totalToBeTaken = 0;
                
                try {
                    totalToBeTaken = SessLeaveApp.getTobeTakenDeptt(deptId, startPeriod, endPeriod);                    
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    qty_ll_before = getLLPrevPeriodd(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_before = getPrevTakenLLReportt(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_before = getLLPrevTakenExpiredReportt(deptId, startPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    qty_ll_current = getLLCurrentPeriodd(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_current = getCurrentTakenLLReportt(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }
                
                try {
                    tkn_ll_expired_current = getLLCurrentTakenExpiredReportt(deptId, startPeriod, endPeriod);
                } catch (Exception E) {
                    System.out.println("Exception " + E.toString());
                }

                /* ==== DEPARTMENT ==== */
                repLevDepartment.setDepartmentId(deptId);
                repLevDepartment.setDepartment(department);
                repLevDepartment.setCountEmployee(countEmployee);

                /* === DP ==== */
                repLevDepartment.setDpQtyBeforeStartPeriod(qty_dp_before);
                repLevDepartment.setDpTknBeforeStartPeriod(tkn_dp_before);
                repLevDepartment.setDpTknExpBeforeStartPeriod(tkn_dp_expired_before);
                repLevDepartment.setDpQtyCurrentPeriod(qty_dp_current);
                repLevDepartment.setDpTknCurrentPeriod(tkn_dp_current);
                repLevDepartment.setDpTknExpiredCurrentPeriod(tkn_dp_expired_current);

                /* ==== AL ==== */
                repLevDepartment.setAlQtyBeforeStartPeriod(qty_al_before);
                repLevDepartment.setAlTknBeforeStartPeriod(tkn_al_before);
                repLevDepartment.setAlQtyCurrentPeriod(qty_al_current);
                repLevDepartment.setAlTknCurrentPeriod(tkn_al_current);
                repLevDepartment.setAlToBeTaken(totalToBeTaken);
                /* ===== LL ==== */
                repLevDepartment.setLLQtyBeforeStartPeriod(qty_ll_before);
                repLevDepartment.setLLTknBeforeStartPeriod(tkn_ll_before);
                repLevDepartment.setLLTknExpBeforeStartPeriod(tkn_ll_expired_before);
                repLevDepartment.setLLQtyCurrentPeriod(qty_ll_current);
                repLevDepartment.setLLTknCurrentPeriod(tkn_ll_current);
                repLevDepartment.setLLTknExpiredCurrentPeriod(tkn_ll_expired_current);
                
                result.add(repLevDepartment);
                
            }
            
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;            
        }
        
    }

    /**
     * @Author : Roy Andika
     * @Description : Untuk mendapatkan jumlah employee yang masih aktif
     * berdasarkan inputan department
     * @return : sum employee
     */
    private static int sumEmployee(long departmentOid, Vector section) {
        
        DBResultSet dbrs = null;
        String variable = "";
        
        try {
            
            String sql = "SELECT COUNT(" + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ") FROM "
                    + PstEmployee.TBL_HR_EMPLOYEE + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN
                    + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentOid;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                int count_employee = 0;
                
                count_employee = rs.getInt(1);
                return count_employee;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    //update by devin 2014-04-04
    private static int sumEmployeee(long departmentOid) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT COUNT(" + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ") FROM "
                    + PstEmployee.TBL_HR_EMPLOYEE + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN
                    + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + departmentOid;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                int count_employee = 0;
                
                count_employee = rs.getInt(1);
                return count_employee;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp qty sebelum start date / mendapatkan qty
     * previous
     * @param department, startDate
     * @return
     */
    private static float getPrevQtyReport(long department, Date startDate, Vector section) {
//    private static int getPrevQtyReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ")"
                    + " FROM " + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float dp_qty = rs.getFloat(1);
                // int dp_qty = rs.getInt(1);
                return dp_qty;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    //update_by_devin 2014-04-07
    private static float getPrevQtyReportt(long department, Date startDate) {
//    private static int getPrevQtyReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ")"
                    + " FROM " + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float dp_qty = rs.getFloat(1);
                // int dp_qty = rs.getInt(1);
                return dp_qty;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken qty sebelum start date / mendapatkan
     * taken previous
     * @param department, startDate
     * @return
     */
    private static float getPrevTakenReport(long department, Date startDate, Vector section) {
//    private static int getPrevTakenReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float qtyTotal = 0;
//int qtyTotal = 0;

            while (rs.next()) {

                /* Pengecekan untuk kondisi taken date yang melewati start date */
                
                float dpQty = rs.getFloat(1);    // field tkn qty
                // int dpQty = rs.getInt(1);    // field tkn qty
                Date strtDate = rs.getDate(2);   // field start date
                Date endDate = rs.getDate(3);   // field end date

                // float dateDiff = 0;
                int dateDiff = 0;
                /* di compare dengan start and end date tkn*/
                dateDiff = DATEDIFF(startDate, endDate);
                
                if (dateDiff >= 1) { /* kondisi dimana taken date tidak melewati start date*/
                    
                    qtyTotal = qtyTotal + dpQty;
                    
                } else {

                    //float new_qty = 0;
                    int new_qty = 0;
                    new_qty = DATEDIFF(startDate, strtDate);
                    qtyTotal = qtyTotal + new_qty;
                    
                }
            }
            
            return qtyTotal;
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    //update by devin 2014-04-07
    private static float getPrevTakenReportt(long department, Date startDate) {
//    private static int getPrevTakenReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float qtyTotal = 0;
//int qtyTotal = 0;

            while (rs.next()) {

                /* Pengecekan untuk kondisi taken date yang melewati start date */
                
                float dpQty = rs.getFloat(1);    // field tkn qty
                // int dpQty = rs.getInt(1);    // field tkn qty
                Date strtDate = rs.getDate(2);   // field start date
                Date endDate = rs.getDate(3);   // field end date

                // float dateDiff = 0;
                int dateDiff = 0;
                /* di compare dengan start and end date tkn*/
                dateDiff = DATEDIFF(startDate, endDate);
                
                if (dateDiff >= 1) { /* kondisi dimana taken date tidak melewati start date*/
                    
                    qtyTotal = qtyTotal + dpQty;
                    
                } else {

                    //float new_qty = 0;
                    int new_qty = 0;
                    new_qty = DATEDIFF(startDate, strtDate);
                    qtyTotal = qtyTotal + new_qty;
                    
                }
            }
            
            return qtyTotal;
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken expired sebelum start date / mendapatkan
     * expired previous
     * @param department, startDate
     * @return
     */
    private static float getPrevTakenExpiredReport(long department, Date startDate, Vector section) {
//    private static int getPrevTakenExpiredReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockExpired.TBL_HR_DP_STOCK_EXPIRED + " DPE ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_DP_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                //  int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    //update by devin 2014-04-07
    private static float getPrevTakenExpiredReportt(long department, Date startDate) {
//    private static int getPrevTakenExpiredReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockExpired.TBL_HR_DP_STOCK_EXPIRED + " DPE ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_DP_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                //  int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan report current qty DP (between start and end
     * period)
     * @Desc Untuk mendapatkan stock DP
     * @param department
     * @param startDate, endDate
     * @return
     */
    private static float getCurrentQtyReport(long department, Date startDate, Date endDate, Vector section) {
// private static int getCurrentQtyReport(long department, Date startDate, Date endDate){

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ")"
                    + " FROM " + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_qty = rs.getFloat(1);
                //int dp_qty = rs.getInt(1);
                return dp_qty;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
//update by devin 2014-04-07

    private static float getCurrentQtyReportt(long department, Date startDate, Date endDate) {
// private static int getCurrentQtyReport(long department, Date startDate, Date endDate){

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ")"
                    + " FROM " + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_qty = rs.getInt(1);
                //int dp_qty = rs.getInt(1);
                return dp_qty;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken qty between start and finnish period
     * @Desc Mendapatkan taken current
     * @param department, startDate
     * @return
     */
    private static float getCurrentTakenReport(long department, Date startDate, Date finnishDate, Vector section) {
//    private static int getCurrentTakenReport(long department, Date startDate, Date finnishDate){

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + " , "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + " , "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tkn_current = 0;
//  int tkn_current = 0;
            while (rs.next()) {
                
                float tkn_qty = rs.getFloat(1);
                // int tkn_qty = rs.getInt(1);
                Date FnsDate = rs.getDate(2);
                Date tknDt = rs.getDate(3);

                //float datediff = DATEDIFF(finnishDate, FnsDate);
                int datediff = DATEDIFF(finnishDate, FnsDate);
                if (datediff >= 0) { /* kondisi dimana finish taken date berada pada renge period yang dicari */
                    
                    tkn_current = tkn_current + tkn_qty;
                    
                } else { /* kondisi dimana finish date berada lebih besar dibandingkan periode pencarian */

                    //float diff = DATEDIFF(finnishDate, tknDt);
                    int diff = DATEDIFF(finnishDate, tknDt);
                    tkn_current = tkn_current + diff + 1;
                    
                }
            }
            
            float tknCrosPeriod = 0;
            //int tknCrosPeriod = 0;
            tknCrosPeriod = getCurrentTakenReportCrossPeriod(department, startDate, finnishDate);
            
            tkn_current = tkn_current + tknCrosPeriod;
            
            return tkn_current;
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
//update by devin 2014-04-07

    private static float getCurrentTakenReportt(long department, Date startDate, Date finnishDate) {
//    private static int getCurrentTakenReport(long department, Date startDate, Date finnishDate){

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + " , "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + " , "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tkn_current = 0;
//  int tkn_current = 0;
            while (rs.next()) {
                
                float tkn_qty = rs.getFloat(1);
                // int tkn_qty = rs.getInt(1);
                Date FnsDate = rs.getDate(2);
                Date tknDt = rs.getDate(3);

                //float datediff = DATEDIFF(finnishDate, FnsDate);
                int datediff = DATEDIFF(finnishDate, FnsDate);
                if (datediff >= 0) { /* kondisi dimana finish taken date berada pada renge period yang dicari */
                    
                    tkn_current = tkn_current + tkn_qty;
                    
                } else { /* kondisi dimana finish date berada lebih besar dibandingkan periode pencarian */

                    //float diff = DATEDIFF(finnishDate, tknDt);
                    int diff = DATEDIFF(finnishDate, tknDt);
                    tkn_current = tkn_current + diff + 1;
                    
                }
            }
            
            float tknCrosPeriod = 0;
            //int tknCrosPeriod = 0;
            tknCrosPeriod = getCurrentTakenReportCrossPeriod(department, startDate, finnishDate);
            
            tkn_current = tkn_current + tknCrosPeriod;
            
            return tkn_current;
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @param department
     * @param startDate
     * @param finnishDate
     * @return
     */
    private static float getCurrentTakenReportCrossPeriod(long department, Date startDate, Date endDate) {
//    private static int getCurrentTakenReportCrossPeriod(long department, Date startDate, Date endDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float qty = 0;
//int qty = 0;
            while (rs.next()) {

                //float tkn_qty = rs.getFloat(1);
                int tkn_qty = rs.getInt(1);
                Date strDt = rs.getDate(2);
                Date endDt = rs.getDate(3);

                //float datediff = DATEDIFF(endDate, endDt);
                int datediff = DATEDIFF(endDate, endDt);
                if (datediff >= 0) { /* jika kondisi finish date berada pada range period yang dicari */

                    /* 1. mencari taken sebelum start periode
                     * 2. mengurangi taken qty dengan taken sebelum start periode                     
                     */
                    int takenb4StartPeriod = DATEDIFF(startDate, strDt);
                    int tknInPeriod = tkn_qty - takenb4StartPeriod;
                    /**
                     * int takenb4StartPeriod = DATEDIFF(startDate, strDt); int
                     * tknInPeriod = tkn_qty - takenb4StartPeriod;
                     */
                    qty = qty + tknInPeriod;
                    
                } else { /* Jika kondisi finish date berada lebih besar dari pada end period yang dicari */

                    /* 1. Karena taken berada sebelum start date dan finish date lebih besar dari pada end date 
                     * 2. Maka tinggal hitung lama start period dan end period                                          
                     */
                    //float taknInPeriod = DATEDIFF(endDate, startDate) + 1;
                    int taknInPeriod = DATEDIFF(endDate, startDate) + 1;
                    qty = qty + taknInPeriod;
                    
                }
            }
            
            return qty;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken expired between start and finnish period
     * @param department, startDate
     * @return
     */
    private static float getCurrentTakenExpiredReport(long department, Date startDate, Date finnishDate, Vector section) {
//   private static int getCurrentTakenExpiredReport(long department, Date startDate, Date finnishDate){

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockExpired.TBL_HR_DP_STOCK_EXPIRED + " DPE ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_DP_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' "
                    + " AND DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' "
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();            
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    //update by devin 2014-04-07

    private static float getCurrentTakenExpiredReportt(long department, Date startDate, Date finnishDate) {
//   private static int getCurrentTakenExpiredReport(long department, Date startDate, Date finnishDate){

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockExpired.TBL_HR_DP_STOCK_EXPIRED + " DPE ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_DP_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' "
                    + " AND DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' "
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();            
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan AL QTY before start date
     * @param department, startDate
     * @return
     */
    private static float getPrevQtyALReport(long department, Date startDate, Vector section) {
//  private static int getPrevQtyALReport(long department, Date startDate) {
        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + "),"
                    + //update by devin 2014-04-09 String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ")," +        
                    " SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ")"
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float al_Entitle = rs.getFloat(1);
                float al_prev = rs.getFloat(2);
                float total = 0;
                total = al_Entitle + al_prev;
                // int al_qty = rs.getInt(1);
                return total;
            }
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    //update by devin 2014-04-07

    private static float getPrevQtyALReportt(long department, Date startDate) {
//  private static int getPrevQtyALReport(long department, Date startDate) {
        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + "),"
                    + " SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ")"
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float al_qty = rs.getFloat(1);
                float al_balance = rs.getFloat(2);
                // int al_qty = rs.getInt(1);
                return al_qty + al_balance;
            }
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan Al taken qty sebelum start date / mendapatkan
     * taken previous
     * @param department, startDate
     * @return
     */
    private static float getPrevTakenALReport(long department, Date startDate, Vector section) {
//  private static int getPrevTakenALReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + " , "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " , "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT "
                    + " ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]
                    + " = ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknAlPrevious = 0;
// int tknAlPrevious = 0;
            while (rs.next()) {

                //float tkn_qty = rs.getFloat(1);
                float tkn_qty = rs.getFloat(1);
                Date startTkn = rs.getDate(2);
                Date endTkn = rs.getDate(3);

                // float tknDiff = DATEDIFF(startDate, endTkn);
                int tknDiff = DATEDIFF(startDate, endTkn);
                if (tknDiff >= 1) { /* Kondisi dimana end taken kurang dari pada end period */
                    
                    tknAlPrevious = tknAlPrevious + tkn_qty;
                    
                } else { /* Kondisi dimana end taken date lebih besar dari start date */

                    //float tknAlBefore = DATEDIFF(startDate, startTkn);
                    int tknAlBefore = DATEDIFF(startDate, startTkn);
                    tknAlPrevious = tknAlPrevious + tknAlBefore;
                    
                }
            }
            return tknAlPrevious;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    //update by devin 2014-04-07
    private static float getPrevTakenALReportt(long department, Date startDate) {
//  private static int getPrevTakenALReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + " , "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " , "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT "
                    + " ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]
                    + " = ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknAlPrevious = 0;
// int tknAlPrevious = 0;
            while (rs.next()) {

                //float tkn_qty = rs.getFloat(1);
                float tkn_qty = rs.getFloat(1);
                Date startTkn = rs.getDate(2);
                Date endTkn = rs.getDate(3);

                // float tknDiff = DATEDIFF(startDate, endTkn);
                int tknDiff = DATEDIFF(startDate, endTkn);
                if (tknDiff >= 1) { /* Kondisi dimana end taken kurang dari pada end period */
                    
                    tknAlPrevious = tknAlPrevious + tkn_qty;
                    
                } else { /* Kondisi dimana end taken date lebih besar dari start date */

                    //float tknAlBefore = DATEDIFF(startDate, startTkn);
                    int tknAlBefore = DATEDIFF(startDate, startTkn);
                    tknAlPrevious = tknAlPrevious + tknAlBefore;
                    
                }
            }
            return tknAlPrevious;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan report current qty AL (between start and end
     * period)
     * @param department
     * @param startDate, endDate
     * @return Qty
     */
    private static float getCurrentQtyAlReport(long department, Date startDate, Date endDate, Vector section) {
//  private static int getCurrentQtyAlReport(long department, Date startDate, Date endDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ")"
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON ALM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float dp_qty = 0;
// int dp_qty = 0;

                dp_qty = rs.getFloat(1);
                
                return dp_qty;
            }
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }
    //update by devin 2014-04-07

    private static float getCurrentQtyAlReportt(long department, Date startDate, Date endDate) {
//  private static int getCurrentQtyAlReport(long department, Date startDate, Date endDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ")"
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON ALM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float dp_qty = 0;
// int dp_qty = 0;

                dp_qty = rs.getFloat(1);
                
                return dp_qty;
            }
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatakan taken Al current
     * @param department
     * @param startDate
     * @param endDate
     * @return
     */
    private static float getCurrentTakenAL(long department, Date startDate, Date finnishDate, Vector section) {
//    private static int getCurrentTakenAL(long department, Date startDate, Date finnishDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT ON "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknCurrentAl = 0;
// int tknCurrentAl = 0;
            while (rs.next()) {
                
                float dp_tkn = rs.getFloat(1);
                //   int dp_tkn = rs.getInt(1);
                Date StartTkn = rs.getDate(2);
                Date EndTkn = rs.getDate(3);

                //float dateDiff = DATEDIFF(finnishDate, EndTkn);
                int dateDiff = DATEDIFF(finnishDate, EndTkn);
                if (dateDiff > 0) {
                    
                    tknCurrentAl = tknCurrentAl + dp_tkn;
                    
                } else {

                    //float tknCurrent = DATEDIFF(finnishDate, StartTkn) + 1;
                    int tknCurrent = DATEDIFF(finnishDate, StartTkn) + 1;
                    tknCurrentAl = tknCurrentAl + tknCurrent;
                    
                }
                
            }
            /**
             * @Desc : Untuk mendapatkan taken dimana start taken dan end taken
             * cross period
             */
            float tknCurrentAndB4 = getCurrentTakenALB4StartDate(department, startDate, finnishDate);
// int tknCurrentAndB4 = getCurrentTakenALB4StartDate(department, startDate, finnishDate);
            tknCurrentAl = tknCurrentAl + tknCurrentAndB4;
            
            return tknCurrentAl;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    //update by devin 2014-04-07
    private static float getCurrentTakenALl(long department, Date startDate, Date finnishDate) {
//    private static int getCurrentTakenAL(long department, Date startDate, Date finnishDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT ON "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknCurrentAl = 0;
// int tknCurrentAl = 0;
            while (rs.next()) {
                
                float dp_tkn = rs.getFloat(1);
                //   int dp_tkn = rs.getInt(1);
                Date StartTkn = rs.getDate(2);
                Date EndTkn = rs.getDate(3);

                //float dateDiff = DATEDIFF(finnishDate, EndTkn);
                int dateDiff = DATEDIFF(finnishDate, EndTkn);
                if (dateDiff > 0) {
                    
                    tknCurrentAl = tknCurrentAl + dp_tkn;
                    
                } else {

                    //float tknCurrent = DATEDIFF(finnishDate, StartTkn) + 1;
                    int tknCurrent = DATEDIFF(finnishDate, StartTkn) + 1;
                    tknCurrentAl = tknCurrentAl + tknCurrent;
                    
                }
                
            }
            /**
             * @Desc : Untuk mendapatkan taken dimana start taken dan end taken
             * cross period
             */
            float tknCurrentAndB4 = getCurrentTakenALB4StartDate(department, startDate, finnishDate);
// int tknCurrentAndB4 = getCurrentTakenALB4StartDate(department, startDate, finnishDate);
            tknCurrentAl = tknCurrentAl + tknCurrentAndB4;
            
            return tknCurrentAl;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getCurrentTakenALB4StartDate(long department, Date startDate, Date finnishDate) {
//   private static int getCurrentTakenALB4StartDate(long department, Date startDate, Date finnishDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT ON "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknCurrentAl = 0;
//  int tknCurrentAl = 0;

            while (rs.next()) {
                
                float dp_tkn = rs.getFloat(1);
// int dp_tkn = rs.getInt(1);
                Date StartTkn = rs.getDate(2);
                Date EndTkn = rs.getDate(3);

                //float dateDiff = DATEDIFF(startDate, StartTkn);
                int dateDiff = DATEDIFF(startDate, StartTkn);
                if (dateDiff > 0) {

                    ///float tknCurrent = DATEDIFF(EndTkn, startDate) + 1;
                    int tknCurrent = DATEDIFF(EndTkn, startDate) + 1;
                    tknCurrentAl = tknCurrentAl + tknCurrent;
                    
                } else {

                    //float tknCurrent = DATEDIFF(finnishDate, startDate) + 1;
                    int tknCurrent = DATEDIFF(finnishDate, startDate) + 1;
                    tknCurrentAl = tknCurrentAl + tknCurrent;
                }
            }
            
            return tknCurrentAl;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /* ============ LONG LEAVE REPORT ================= */
    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan long leave prev period
     * @param departmentId
     * @param startDate
     * @return
     */
    private static float getLLPrevPeriod(long departmentId, Date startDate, Vector section) {
//    private static int getLLPrevPeriod(long departmentId, Date startDate) {

        try {
            
            float LL1 = 0;
            float LL2 = 0;
            float tot = 0;
            /**
             * int LL1 = 0; int LL2 = 0; int tot = 0;
             */
            LL1 = getLongPrevPeriodEntitle1(departmentId, startDate, section);
            LL2 = getLongPrevPeriodEntitle2(departmentId, startDate, section);
            
            tot = LL1 + LL2;
            
            return tot;
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return 0;
        
    }

    //update by devin 2014-04-07
    private static float getLLPrevPeriodd(long departmentId, Date startDate) {
//    private static int getLLPrevPeriod(long departmentId, Date startDate) {

        try {
            
            float LL1 = 0;
            float LL2 = 0;
            float tot = 0;
            /**
             * int LL1 = 0; int LL2 = 0; int tot = 0;
             */
            LL1 = getLongPrevPeriodEntitlee1(departmentId, startDate);
            LL2 = getLongPrevPeriodEntitlee2(departmentId, startDate);
            
            tot = LL1 + LL2;
            
            return tot;
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return 0;
        
    }
    
    private static float getLongPrevPeriodEntitle1(long departmentId, Date startDate, Vector section) {
//  private static int getLongPrevPeriodEntitle1(long departmentId, Date startDate) {

        DBResultSet dbrs = null;
        String variable = "";
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                //int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
//update by devin 2014-04-07

    private static float getLongPrevPeriodEntitlee1(long departmentId, Date startDate) {
//  private static int getLongPrevPeriodEntitle1(long departmentId, Date startDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                //int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    private static float getLongPrevPeriodEntitle2(long departmentId, Date startDate, Vector section) {
//    private static int getLongPrevPeriodEntitle2(long departmentId, Date startDate) {

        DBResultSet dbrs = null;
        String variable = "";
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {                
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {                    
                    int hasil = x - section.size();                    
                    Section sectionn = (Section) section.get(x);                    
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                // int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
        
    }
    //update by devin 2014-04-07

    private static float getLongPrevPeriodEntitlee2(long departmentId, Date startDate) {
//    private static int getLongPrevPeriodEntitle2(long departmentId, Date startDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                // int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
        
    }
    
    private static float getPrevTakenLLReport(long department, Date startDate, Vector section) {
//   private static int getPrevTakenLLReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknLL = 0;
//int tknLL = 0;
            while (rs.next()) {
                
                float ll_tkn = rs.getFloat(1);
                // int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknFinishDate = rs.getDate(3);

                // float tknDiff = DATEDIFF(startDate, tknFinishDate);
                int tknDiff = DATEDIFF(startDate, tknFinishDate);
                
                if (tknDiff >= 1) { /* Kondisi dimana start tkn dan finish tkn berada sebelum start period */
                    
                    tknLL = tknLL + ll_tkn;
                    
                } else {

                    //float diffTknB4 = DATEDIFF(startDate, tknStartDate);
                    int diffTknB4 = DATEDIFF(startDate, tknStartDate);
                    tknLL = tknLL + diffTknB4;
                    
                }
                
            }
            
            return tknLL;
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    //update by devin 2014-04-07

    private static float getPrevTakenLLReportt(long department, Date startDate) {
//   private static int getPrevTakenLLReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknLL = 0;
//int tknLL = 0;
            while (rs.next()) {
                
                float ll_tkn = rs.getFloat(1);
                // int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknFinishDate = rs.getDate(3);

                // float tknDiff = DATEDIFF(startDate, tknFinishDate);
                int tknDiff = DATEDIFF(startDate, tknFinishDate);
                
                if (tknDiff >= 1) { /* Kondisi dimana start tkn dan finish tkn berada sebelum start period */
                    
                    tknLL = tknLL + ll_tkn;
                    
                } else {

                    //float diffTknB4 = DATEDIFF(startDate, tknStartDate);
                    int diffTknB4 = DATEDIFF(startDate, tknStartDate);
                    tknLL = tknLL + diffTknB4;
                    
                }
                
            }
            
            return tknLL;
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLLPrevTakenExpiredReport(long department, Date startDate, Vector section) {
// private static int getLLPrevTakenExpiredReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " LLE ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " = LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    //update by devin 2014-04-07
    private static float getLLPrevTakenExpiredReportt(long department, Date startDate) {
// private static int getLLPrevTakenExpiredReport(long department, Date startDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " LLE ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " = LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLLCurrentPeriod(long departmentId, Date startDate, Date endDate, Vector section) {
//   private static int getLLCurrentPeriod(long departmentId, Date startDate, Date endDate) {

        try {
            float LL1 = 0;
            float LL2 = 0;
            float tot = 0;
            /**
             * int LL1 = 0; int LL2 = 0; int tot = 0;
             */
            LL1 = getLongCurrentPeriodEntitle1(departmentId, startDate, endDate, section);
            LL2 = getLongCurrentPeriodEntitle2(departmentId, startDate, endDate, section);
            
            tot = LL1 + LL2;
            
            return tot;
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return 0;
        
    }
    //update by devin 2014-04-07

    private static float getLLCurrentPeriodd(long departmentId, Date startDate, Date endDate) {
//   private static int getLLCurrentPeriod(long departmentId, Date startDate, Date endDate) {

        try {
            float LL1 = 0;
            float LL2 = 0;
            float tot = 0;
            /**
             * int LL1 = 0; int LL2 = 0; int tot = 0;
             */
            LL1 = getLongCurrentPeriodEntitlee1(departmentId, startDate, endDate);
            LL2 = getLongCurrentPeriodEntitlee2(departmentId, startDate, endDate);
            
            tot = LL1 + LL2;
            
            return tot;
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return 0;
        
    }
    
    private static float getLongCurrentPeriodEntitle1(long departmentId, Date startDate, Date endDate, Vector section) {
//   private static int getLongCurrentPeriodEntitle1(long departmentId, Date startDate, Date endDate) {

        DBResultSet dbrs = null;
        String variable = "";
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                // int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    //update by devin 2014-04-07

    private static float getLongCurrentPeriodEntitlee1(long departmentId, Date startDate, Date endDate) {
//   private static int getLongCurrentPeriodEntitle1(long departmentId, Date startDate, Date endDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                // int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLongCurrentPeriodEntitle2(long departmentId, Date startDate, Date endDate, Vector section) {
//private static int getLongCurrentPeriodEntitle2(long departmentId, Date startDate, Date endDate) {

        DBResultSet dbrs = null;
        String variable = "";
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                // int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
        
    }
    //update by devin 2014-04-07

    private static float getLongCurrentPeriodEntitlee2(long departmentId, Date startDate, Date endDate) {
//private static int getLongCurrentPeriodEntitle2(long departmentId, Date startDate, Date endDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + departmentId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                // int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
        
    }
    
    private static float getCurrentTakenLLReport(long department, Date startDate, Date endDate, Vector section) {
// private static int getCurrentTakenLLReport(long department, Date startDate, Date endDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknQty = 0;
// int tknQty = 0;
            while (rs.next()) {
                
                float ll_tkn = rs.getFloat(1);
                //int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknfnsDate = rs.getDate(3);

                //float dateDiff = DATEDIFF(endDate, tknfnsDate);
                int dateDiff = DATEDIFF(endDate, tknfnsDate);
                
                if (dateDiff >= 0) {
                    
                    tknQty = tknQty + ll_tkn;
                    
                } else {

                    //float tknCurrent = 0;
                    int tknCurrent = 0;
                    tknCurrent = DATEDIFF(tknfnsDate, tknStartDate) + 1;
                    
                    tknQty = tknQty + tknCurrent;
                    
                }
                
            }
            
            float tknCrosPeriod = getCurrentTakenLLReportCroosPeriod(department, startDate, endDate);
//  int tknCrosPeriod = getCurrentTakenLLReportCroosPeriod(department, startDate, endDate);

            tknQty = tknQty + tknCrosPeriod;
            
            return tknQty;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    //update by devin 2014-04-07
    private static float getCurrentTakenLLReportt(long department, Date startDate, Date endDate) {
// private static int getCurrentTakenLLReport(long department, Date startDate, Date endDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknQty = 0;
// int tknQty = 0;
            while (rs.next()) {
                
                float ll_tkn = rs.getFloat(1);
                //int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknfnsDate = rs.getDate(3);

                //float dateDiff = DATEDIFF(endDate, tknfnsDate);
                int dateDiff = DATEDIFF(endDate, tknfnsDate);
                
                if (dateDiff >= 0) {
                    
                    tknQty = tknQty + ll_tkn;
                    
                } else {

                    //float tknCurrent = 0;
                    int tknCurrent = 0;
                    tknCurrent = DATEDIFF(tknfnsDate, tknStartDate) + 1;
                    
                    tknQty = tknQty + tknCurrent;
                    
                }
                
            }
            
            float tknCrosPeriod = getCurrentTakenLLReportCroosPeriod(department, startDate, endDate);
//  int tknCrosPeriod = getCurrentTakenLLReportCroosPeriod(department, startDate, endDate);

            tknQty = tknQty + tknCrosPeriod;
            
            return tknQty;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getCurrentTakenLLReportCroosPeriod(long department, Date startDate, Date endDate) {
//  private static int getCurrentTakenLLReportCroosPeriod(long department, Date startDate, Date endDate) {
        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknQty = 0;
//  int tknQty = 0;
            while (rs.next()) {
                
                float ll_tkn = rs.getInt(1);
                // int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknfnsDate = rs.getDate(3);

                /* Pengecekan untuk taken yang melewati end period */
                //float dateDiff = DATEDIFF(endDate, tknfnsDate);
                int dateDiff = DATEDIFF(endDate, tknfnsDate);
                
                if (dateDiff >= 0) {

                    //float tknCurrent = DATEDIFF(tknfnsDate, startDate) + 1;
                    int tknCurrent = DATEDIFF(tknfnsDate, startDate) + 1;
                    tknQty = tknQty + tknCurrent;
                    
                } else {

                    //float tknCurrent = DATEDIFF(endDate, startDate) + 1;
                    int tknCurrent = DATEDIFF(endDate, startDate) + 1;
                    tknQty = tknQty + tknCurrent;
                    
                }
            }
            
            return tknQty;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLLCurrentTakenExpiredReport(long department, Date startDate, Date endDate, Vector section) {
// private static int getLLCurrentTakenExpiredReport(long department, Date startDate, Date endDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        String variable = "";
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " LLE ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " = LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            if (section != null && section.size() > 0) {
                sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + " NOT IN (";
                for (int x = 0; x < section.size(); x++) {
                    int hasil = x - section.size();
                    Section sectionn = (Section) section.get(x);
                    variable += sectionn.getOID();
                    if (hasil != -1) {
                        variable = variable + ",";
                    }
                }
                sql = sql + variable + ")";
            }
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    //update by devin 2014-04-07

    private static float getLLCurrentTakenExpiredReportt(long department, Date startDate, Date endDate) {
// private static int getLLCurrentTakenExpiredReport(long department, Date startDate, Date endDate) {

        if (department == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " LLE ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " = LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + department + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * Author Roy Andika
     *
     * @param scheduleId
     * @return
     */
    public static boolean scheduleLeave(long scheduleId) {
        
        if (scheduleId == 0) {
            return false;
        }
        long oidAl = 0;
        long oidDp = 0;
        long oidLL = 0;
        
        try {
            oidAl = Long.parseLong(PstSystemProperty.getValueByName("OID_AL"));
            
            if (oidAl == scheduleId) {
                return true;
            }
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        
        try {
            oidDp = Long.parseLong(PstSystemProperty.getValueByName("OID_DP"));
            
            if (oidDp == scheduleId) {
                return true;
            }
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        
        try {
            oidLL = Long.parseLong(PstSystemProperty.getValueByName("OID_LL"));
            if (oidLL == scheduleId) {
                return true;
            }
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        
        String whereSpUpLeave = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + "=" + PstScheduleCategory.CATEGORY_SPECIAL_LEAVE
                + " OR " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + "=" + PstScheduleCategory.CATEGORY_UNPAID_LEAVE;
        
        Vector scheduleSpUp = new Vector();
        
        scheduleSpUp = PstScheduleSymbol.list(0, 0, whereSpUpLeave, null);
        
        for (int i = 0; i < scheduleSpUp.size(); i++) {

            /* schedule special leave */
            ScheduleSymbol scheduleSymbol = new ScheduleSymbol();
            scheduleSymbol = (ScheduleSymbol) scheduleSpUp.get(i);
            if (scheduleSymbol.getOID() == scheduleId) {
                return true;
            }
            
        }
        
        return false;
    }
    
    public static boolean scheduleLeave(long scheduleId, Vector specialSchedule) {
        
        if (scheduleId == 0) {
            return false;
        }
        long oidAl = 0;
        long oidDp = 0;
        long oidLL = 0;
        
        try {
            oidAl = Long.parseLong(PstSystemProperty.getValueByName("OID_AL"));
            
            if (oidAl == scheduleId) {
                return true;
            }
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        
        try {
            oidDp = Long.parseLong(PstSystemProperty.getValueByName("OID_DP"));
            
            if (oidDp == scheduleId) {
                return true;
            }
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        
        try {
            oidLL = Long.parseLong(PstSystemProperty.getValueByName("OID_LL"));
            if (oidLL == scheduleId) {
                return true;
            }
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        
        
        if (specialSchedule != null && specialSchedule.size() > 0) {
            
            for (int i = 0; i < specialSchedule.size(); i++) {
                
                ScheduleSymbol scheduleSymbol = new ScheduleSymbol();
                scheduleSymbol = (ScheduleSymbol) specialSchedule.get(i);
                if (scheduleSymbol.getOID() == scheduleId) {
                    return true;
                }
            }
        }
        
        return false;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan schedule id untuk special dan unpaid leave
     * @return
     */
    public static Vector getSpecialUnpaidLeave() {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SYM." + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID] + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL
                    + " SYM INNER JOIN " + PstScheduleCategory.TBL_HR_SCHEDULE_CATEGORY + " CAT ON SYM." + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID]
                    + "= CAT." + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_SCHEDULE_CATEGORY_ID] + " WHERE CAT." + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE] + "="
                    + PstScheduleCategory.CATEGORY_SPECIAL_LEAVE + " OR CAT." + PstScheduleCategory.fieldNames[PstScheduleCategory.FLD_CATEGORY_TYPE] + " = "
                    + PstScheduleCategory.CATEGORY_UNPAID_LEAVE;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector result = new Vector();
            
            while (rs.next()) {
                
                ScheduleSymbol scheduleSymbol = new ScheduleSymbol();
                
                long scheduleId = 0;
                scheduleId = rs.getLong(1);
                
                scheduleSymbol.setOID(scheduleId);
                result.add(scheduleSymbol);
                
            }
            
            return result;
            
        } catch (Exception E) {
            System.out.println("[exception] SessLeaveApplication.getSpecialUnpaidLeave() ::: " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return null;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk emndapatkan leave employee detail dari masing-masing employee
     * @param srcLeaveAppAlClosing
     * @return
     */
    public static Vector leave_Detail(SrcLeaveManagement srcLeaveManagement, Vector section, int valueSection) {
        
        if (srcLeaveManagement == null) {
            return null;
        }
        
        Vector listEmployee = new Vector();
        
        try {
            listEmployee = listOidEmployeeBySection(srcLeaveManagement, section, valueSection);
        } catch (Exception e) {
            System.out.println("[exception] SessLeaveApplication.ListSpecialUnpaidLeave() " + e.toString());
        }

        /* Declarasi untuk strat period and end period */
        Date startPeriod = new Date();
        Date endPeriod = new Date();
        
        if (srcLeaveManagement.getTime() == 0) { /* Pengecekan jika yang dipilih berdasarkan periode */
            
            if (srcLeaveManagement.getPeriodId() == 0) {
                return null;
            }
            
            Period period = new Period();
            try {
                period = PstPeriod.fetchExc(srcLeaveManagement.getPeriodId());
            } catch (Exception e) {
                System.out.println("[exception] SessLeaveApplication.sumLeave_Department(SrcLeaveAppAlClosing srcLeaveAppAlClosing) :: " + e.toString());
            }
            
            startPeriod = period.getStartDate();
            endPeriod = period.getEndDate();
            
        } else { /* Pengecekan jika waktu yang dipilih berdasarkan time */
            
            startPeriod = srcLeaveManagement.getStartDate();
            endPeriod = srcLeaveManagement.getEndDate();
            
        }
        
        try {
            //listEmployee = listOidEmployee(srcLeaveManagement);
        } catch (Exception e) {
            System.out.println("[exception] SessLeaveApplication.ListSpecialUnpaidLeave() " + e.toString());
        }
        
        Vector result = new Vector();
        
        if (listEmployee != null && listEmployee.size() > 0) {
            
            for (int i = 0; i < listEmployee.size(); i++) {
                
                Employee employee = new Employee();

                /*
                 * OID
                 * FULL_NAME
                 * EMPLOYEE_NUM
                 * DEPARTMENT_ID                 
                 */

                /* DP */
                float qty_dp_prev = 0;
                float tkn_dp_prev = 0;
                float tkn_exp_prev = 0;
                float qty_dp = 0;
                float tkn_dp = 0;
                float exp_dp = 0;

                /* AL */
                float qty_al_prev = 0;
                float tkn_al_prev = 0;
                float qty_al = 0;
                float tkn_al = 0;

                /* LL */
                float qty_ll_Prev = 0;
                float tkn_ll_prev = 0;
                float tkn_ll_exp_prev = 0;
                float qty_ll = 0;
                float tkn_ll = 0;
                float tkn_ll_exp = 0;
                /* DP */
//                int qty_dp_prev = 0;
//                int tkn_dp_prev = 0;
//                int tkn_exp_prev = 0;
//                int qty_dp = 0;
//                int tkn_dp = 0;
//                int exp_dp = 0;
//
//                /* AL */
//                int qty_al_prev = 0;
//                int tkn_al_prev = 0;
//                int qty_al = 0;
//                int tkn_al = 0;
//
//                /* LL */
//                int qty_ll_Prev = 0;
//                int tkn_ll_prev = 0;
//                int tkn_ll_exp_prev = 0;
//                int qty_ll = 0;
//                int tkn_ll = 0;
//                int tkn_ll_exp = 0;

                employee = (Employee) listEmployee.get(i);
                
                try {
                    qty_dp_prev = getPrevQtyReportDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_dp_prev = getPrevTakenReportDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_exp_prev = getPrevTakenExpiredReportDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    qty_dp = getCurrentQtyReportDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_dp = getCurrentTakenReportDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    exp_dp = getCurrentTakenExpiredReportDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    qty_al_prev = getPrevQtyALReportDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_al_prev = getPrevTakenALReportDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    qty_al = getCurrentQtyAlReportDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_al = getCurrentTakenALDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                
                float totalToBeTaken = 0;
                long oidEmp = employee.getOID();
                try {
                    totalToBeTaken = SessLeaveApp.getTobeTaken(oidEmp, startPeriod, endPeriod);                    
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }

                //test devin
                String getOidEmp = String.valueOf(employee.getOID());
                Hashtable listTakenLeave = new Hashtable();
                float totalTakenAl = 0;
                try {
                    listTakenLeave = SessLeaveApp.leaveTakenExecute(getOidEmp, startPeriod, endPeriod);                    
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                if (listTakenLeave != null && listTakenLeave.size() > 0 && listTakenLeave.get(employee.getOID()) != null) {
                    /*cell = row.createCell((short) (collPos));
                     cell.setCellValue(Float.valueOf(0));
                     cell.setCellStyle(styleValue);
                     int collResx = collPos;
                     collPos++;*/
                    //for (int otPaidMonIdx = 0; otPaidMonIdx < sumOvertimeDailyPaidBySalary.size(); otPaidMonIdx++) {
                    LeaveApplicationSummary leaveApplicationSummary = (LeaveApplicationSummary) listTakenLeave.get(employee.getOID());
                    if (leaveApplicationSummary.getEmployeeId() != 0 && leaveApplicationSummary.getEmployeeId() == employee.getOID()) {
                        //totPaidSalary= overtimeDetailPaidMon.getTotPaidBySalary();
                                   /* cell = row.createCell((short)collPos);
                         cell.setCellValue(Float.valueOf(overtimeDetailPaidMon.getTotPaidBySalary()));//nnti di rubah
                         cell.setCellStyle(styleValue);
                                    
                         collPos++;*/
                        /* totalTimePaidSalary = overtimeDetailPaidMon.getTotDuration();
                         if (SessOvertime.getOvertimeRoundTo() > 0 && (totalTimePaidSalary * 60) > SessOvertime.getOvertimeRoundStart()) {
                         totalTimePaidSalary = ((double) ((Math.round(totalTimePaidSalary * 60) / SessOvertime.getOvertimeRoundTo()) * SessOvertime.getOvertimeRoundTo())) / 60d;
                         }*/
                        
                        
                        float totalTakenDP = 0;
                        float totalTakenLL = 0;
                        if (leaveApplicationSummary.getVsymbol() != null && leaveApplicationSummary.getVsymbol().size() > 0) {
                            for (int idxCT = 0; idxCT < leaveApplicationSummary.getVsymbol().size(); idxCT++) {
                                if (leaveApplicationSummary.getSymbol(idxCT).equalsIgnoreCase("AL")) {
                                    totalTakenAl = totalTakenAl + (float) leaveApplicationSummary.getJumlahTaken(idxCT);
                                    leaveApplicationSummary.getVsymbol().remove(idxCT);
                                    idxCT = idxCT - 1;
                                } else if (leaveApplicationSummary.getSymbol(idxCT).equalsIgnoreCase("DP")) {
                                    totalTakenDP = totalTakenDP + (float) leaveApplicationSummary.getJumlahTaken(idxCT);
                                    leaveApplicationSummary.getVsymbol().remove(idxCT);
                                    idxCT = idxCT - 1;
                                } else if (leaveApplicationSummary.getSymbol(idxCT).equalsIgnoreCase("LL")) {
                                    totalTakenLL = totalTakenLL + (float) leaveApplicationSummary.getJumlahTaken(idxCT);
                                    leaveApplicationSummary.getVsymbol().remove(idxCT);
                                    idxCT = idxCT - 1;
                                }
                                
                            }
                        }
                        // sumLeaveTotal = sumLeaveTotal + totalTakenAl + totalTakenDP + totalTakenLL;
                        
                    }

                    // }

                }
                try {
                    qty_ll_Prev = getLLPrevPeriodDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_ll_prev = getPrevTakenLLReportDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_ll_exp_prev = getLLPrevTakenExpiredReportDetail(employee.getOID(), startPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    qty_ll = getLLCurrentPeriodDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_ll = getCurrentTakenLLReportDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                try {
                    tkn_ll_exp = getLLCurrentTakenExpiredReportDetail(employee.getOID(), startPeriod, endPeriod);
                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
                
                RepLevDepartment repLevDepartment = new RepLevDepartment();

                /* ==== EMPLOYEE ==== */
                repLevDepartment.setEmployeeId(employee.getOID());
                repLevDepartment.setFull_name(employee.getFullName());
                repLevDepartment.setEmp_num(employee.getEmployeeNum());
                repLevDepartment.setDepartmentId(employee.getDepartmentId());

                /* ======== DP =========== */
                repLevDepartment.setDpQtyBeforeStartPeriod(qty_dp_prev);
                repLevDepartment.setDpTknBeforeStartPeriod(tkn_dp_prev);
                repLevDepartment.setDpTknExpBeforeStartPeriod(tkn_exp_prev);
                repLevDepartment.setDpQtyCurrentPeriod(qty_dp);
                repLevDepartment.setDpTknCurrentPeriod(tkn_dp);
                repLevDepartment.setDpTknExpiredCurrentPeriod(exp_dp);

                /* ========= AL ============ */
                repLevDepartment.setAlQtyBeforeStartPeriod(qty_al_prev);
                repLevDepartment.setAlTknBeforeStartPeriod(tkn_al_prev);
                repLevDepartment.setAlQtyCurrentPeriod(qty_al);
                repLevDepartment.setAlTknCurrentPeriod(tkn_al);
                repLevDepartment.setAlToBeTaken(totalToBeTaken);
                /**
                 * update by devin 2014-04-09
                 *
                 * repLevDepartment.setAlTknBeforeStartPeriod(tkn_al_prev);
                 * repLevDepartment.setAlQtyCurrentPeriod(qty_al);
                 * repLevDepartment.setAlTknCurrentPeriod(tkn_al);
                 */
                /* =========== LL =========== */
                repLevDepartment.setLLQtyBeforeStartPeriod(qty_ll_Prev);
                repLevDepartment.setLLTknBeforeStartPeriod(tkn_ll_prev);
                repLevDepartment.setLLTknExpBeforeStartPeriod(tkn_ll_exp_prev);
                repLevDepartment.setLLQtyCurrentPeriod(qty_ll);
                repLevDepartment.setLLTknCurrentPeriod(tkn_ll);
                repLevDepartment.setLLTknExpiredCurrentPeriod(tkn_ll_exp);
                
                result.add(repLevDepartment);
            }
            return result;
        }
        return null;
    }

    /* ======================= DETAIL LEAVE EMPLOYEE ============================================== */
    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp qty sebelum start date / mendapatkan qty
     * previous
     * @param department, startDate
     * @return
     */
    private static float getPrevQtyReportDetail(long empId, Date startDate) {
//  private static int getPrevQtyReportDetail(long empId, Date startDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ")"
                    + " FROM " + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float dp_qty = rs.getFloat(1);
                // int dp_qty = rs.getInt(1);
                return dp_qty;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken qty sebelum start date / mendapatkan
     * taken previous
     * @param department, startDate
     * @return
     */
    private static float getPrevTakenReportDetail(long empId, Date startDate) {
//    private static int getPrevTakenReportDetail(long empId, Date startDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float qtyTotal = 0;
// int qtyTotal = 0;
            while (rs.next()) {

                /* Pengecekan untuk kondisi taken date yang melewati start date */
                
                float dpQty = rs.getFloat(1);    // field tkn qty
                // int dpQty = rs.getInt(1);    // field tkn qty
                Date strtDate = rs.getDate(2);   // field start date
                Date endDate = rs.getDate(3);   // field end date

                //float dateDiff = 0;
                int dateDiff = 0;
                /* di compare dengan start and end date tkn*/
                dateDiff = DATEDIFF(startDate, endDate);
                
                if (dateDiff >= 1) { /* kondisi dimana taken date tidak melewati start date*/
                    
                    qtyTotal = qtyTotal + dpQty;
                    
                } else {

                    //float new_qty = 0;
                    int new_qty = 0;
                    new_qty = DATEDIFF(startDate, strtDate);
                    qtyTotal = qtyTotal + new_qty;
                    
                }
                
                float dp_tkn = rs.getFloat(1);
                //int dp_tkn = rs.getInt(1);

            }
            
            return qtyTotal;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken expired sebelum start date / mendapatkan
     * expired previous
     * @param department, startDate
     * @return
     */
    private static float getPrevTakenExpiredReportDetail(long empId, Date startDate) {
//private static int getPrevTakenExpiredReportDetail(long empId, Date startDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockExpired.TBL_HR_DP_STOCK_EXPIRED + " DPE ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_DP_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan report current qty DP (between start and end
     * period)
     * @param department
     * @param startDate, endDate
     * @return
     */
    private static float getCurrentQtyReportDetail(long empId, Date startDate, Date endDate) {
// private static int getCurrentQtyReportDetail(long empId, Date startDate, Date endDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ")"
                    + " FROM " + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_OWNING_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_qty = rs.getFloat(1);
                //int dp_qty = rs.getInt(1);
                return dp_qty;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken qty between start and finnish period
     * @param department, startDate
     * @return
     */
    private static float getCurrentTakenReportDetail(long empId, Date startDate, Date finnishDate) {
// private static int getCurrentTakenReportDetail(long empId, Date startDate, Date finnishDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + " , "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + " , "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tkn_current = 0;
// int tkn_current = 0;
            while (rs.next()) {
                
                float tkn_qty = rs.getFloat(1);
                // int tkn_qty = rs.getInt(1);
                Date FnsDate = rs.getDate(2);
                Date tknDt = rs.getDate(3);

                //float datediff = DATEDIFF(finnishDate, FnsDate);
                int datediff = DATEDIFF(finnishDate, FnsDate);
                if (datediff >= 0) { /* kondisi dimana finish taken date berada pada renge period yang dicari */
                    
                    tkn_current = tkn_current + tkn_qty;
                    
                } else { /* kondisi dimana finish date berada lebih besar dibandingkan periode pencarian */

                    //float diff = DATEDIFF(finnishDate, tknDt);
                    int diff = DATEDIFF(finnishDate, tknDt);
                    tkn_current = tkn_current + diff + 1;
                    
                }
            }
            
            float tknCrosPeriod = 0;
            // int tknCrosPeriod = 0;
            tknCrosPeriod = getCurrentTakenReportCrossPeriodDetail(empId, startDate, finnishDate);
            
            tkn_current = tkn_current + tknCrosPeriod;
            
            return tkn_current;
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @param department
     * @param startDate
     * @param finnishDate
     * @return
     */
    private static float getCurrentTakenReportCrossPeriodDetail(long empId, Date startDate, Date endDate) {
//private static int getCurrentTakenReportCrossPeriodDetail(long empId, Date startDate, Date endDate) {
        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_QTY] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + ","
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockTaken.TBL_HR_DP_STOCK_TAKEN + " DPT ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_DP_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPT."
                    + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " DPT." + PstDpStockTaken.fieldNames[PstDpStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float qty = 0;
// int qty = 0;
            while (rs.next()) {
                
                float tkn_qty = rs.getFloat(1);
                // int tkn_qty = rs.getInt(1);
                Date strDt = rs.getDate(2);
                Date endDt = rs.getDate(3);

                //float datediff = DATEDIFF(endDate, endDt);
                int datediff = DATEDIFF(endDate, endDt);
                if (datediff >= 0) { /* jika kondisi finish date berada pada range period yang dicari */

                    /* 1. mencari taken sebelum start periode
                     * 2. mengurangi taken qty dengan taken sebelum start periode                     
                     */
                    //float takenb4StartPeriod = DATEDIFF(startDate, strDt);
                    int takenb4StartPeriod = DATEDIFF(startDate, strDt);
                    float tknInPeriod = tkn_qty - takenb4StartPeriod;
//   int tknInPeriod = tkn_qty - takenb4StartPeriod;
                    qty = qty + tknInPeriod;
                    
                } else { /* Jika kondisi finish date berada lebih besar dari pada end period yang dicari */

                    /* 1. Karena taken berada sebelum start date dan finish date lebih besar dari pada end date 
                     * 2. Maka tinggal hitung lama start period dan end period                                          
                     */
                    //float taknInPeriod = DATEDIFF(endDate, startDate) + 1;
                    int taknInPeriod = DATEDIFF(endDate, startDate) + 1;
                    qty = qty + taknInPeriod;
                    
                }
            }
            
            return qty;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan dp taken expired between start and finnish period
     * @param department, startDate
     * @return
     */
    private static float getCurrentTakenExpiredReportDetail(long empId, Date startDate, Date finnishDate) {
//private static int getCurrentTakenExpiredReportDetail(long empId, Date startDate, Date finnishDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstDpStockManagement.TBL_DP_STOCK_MANAGEMENT + " DPM LEFT JOIN " + PstDpStockExpired.TBL_HR_DP_STOCK_EXPIRED + " DPE ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_STOCK_ID] + " = DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_DP_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " DPM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE DPE."
                    + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' "
                    + " AND DPE." + PstDpStockExpired.fieldNames[PstDpStockExpired.FLD_EXPIRED_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' "
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                //int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan AL QTY before start date
     * @param department, startDate
     * @return
     */
    private static float getPrevQtyALReportDetail(long empId, Date startDate) {
//   private static int getPrevQtyALReportDetail(long empId, Date startDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + "),"
                    + //update by devin 2014-04-09 String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ")," +        
                    " SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ")"
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float al_qty = rs.getFloat(1);
                float al_prev = rs.getFloat(2);
                /**
                 * int al_qty = rs.getInt(1); int al_prev = rs.getInt(2);
                 */
                return al_qty + al_prev;
            }
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan Al taken qty sebelum start date / mendapatkan
     * taken previous
     * @param department, startDate
     * @return
     */
    private static float getPrevTakenALReportDetail(long empId, Date startDate) {
//private static int getPrevTakenALReportDetail(long empId, Date startDate){

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + " , "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " , "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT "
                    + " ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]
                    + " = ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknAlPrevious = 0;
//  int tknAlPrevious = 0;
            while (rs.next()) {
                
                float tkn_qty = rs.getFloat(1);
                // int tkn_qty = rs.getInt(1);
                Date startTkn = rs.getDate(2);
                Date endTkn = rs.getDate(3);

                //float tknDiff = DATEDIFF(startDate, endTkn);
                int tknDiff = DATEDIFF(startDate, endTkn);
                
                if (tknDiff >= 1) { /* Kondisi dimana end taken kurang dari pada end period */
                    
                    tknAlPrevious = tknAlPrevious + tkn_qty;
                    
                } else { /* Kondisi dimana end taken date lebih besar dari start date */
                    
                    int tknAlBefore = DATEDIFF(startDate, startTkn);
                    tknAlPrevious = tknAlPrevious + tknAlBefore;
                    
                }
            }
            return tknAlPrevious;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan report current qty AL (between start and end
     * period)
     * @param department
     * @param startDate, endDate
     * @return Qty
     */
    private static float getCurrentQtyAlReportDetail(long empId, Date startDate, Date endDate) {
//  private static int getCurrentQtyAlReportDetail(long empId, Date startDate, Date endDate) {
        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ")"
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON ALM." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                float dp_qty = 0;
// int dp_qty = 0;
                dp_qty = rs.getFloat(1);
                
                return dp_qty;
            }
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatakan taken Al current
     * @param department
     * @param startDate
     * @param endDate
     * @return
     */
    private static float getCurrentTakenALDetail(long empId, Date startDate, Date finnishDate) {
// private static int getCurrentTakenALDetail(long empId, Date startDate, Date finnishDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT ON "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(finnishDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknCurrentAl = 0;
// int tknCurrentAl = 0;
            while (rs.next()) {
                
                float dp_tkn = rs.getFloat(1);
// int dp_tkn = rs.getInt(1);
                Date StartTkn = rs.getDate(2);
                Date EndTkn = rs.getDate(3);
                
                int dateDiff = DATEDIFF(finnishDate, EndTkn);
                
                if (dateDiff > 0) {
                    
                    tknCurrentAl = tknCurrentAl + dp_tkn;
                    
                } else {

                    //float tknCurrent = DATEDIFF(finnishDate, StartTkn) + 1;
                    int tknCurrent = DATEDIFF(finnishDate, StartTkn) + 1;
                    
                    tknCurrentAl = tknCurrentAl + tknCurrent;
                    
                }
                
            }
            /**
             * @Desc : Untuk mendapatkan taken dimana start taken dan end taken
             * cross period
             */
            float tknCurrentAndB4 = getCurrentTakenALB4StartDateDetail(empId, startDate, finnishDate);
//  int tknCurrentAndB4 = getCurrentTakenALB4StartDateDetail(empId, startDate, finnishDate);

            tknCurrentAl = tknCurrentAl + tknCurrentAndB4;
            
            return tknCurrentAl;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getCurrentTakenALB4StartDateDetail(long empId, Date startDate, Date finnishDate) {
// private static int getCurrentTakenALB4StartDateDetail(long empId, Date startDate, Date finnishDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_QTY] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + ","
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM LEFT JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN + " ALT ON "
                    + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID]
                    + " = APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE ALT."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " ALT." + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + "EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknCurrentAl = 0;
// int tknCurrentAl = 0;

            while (rs.next()) {
                
                float dp_tkn = rs.getFloat(1);
                // int dp_tkn = rs.getInt(1);
                Date StartTkn = rs.getDate(2);
                Date EndTkn = rs.getDate(3);
                
                int dateDiff = DATEDIFF(startDate, StartTkn);
                
                if (dateDiff > 0) {
                    
                    int tknCurrent = DATEDIFF(EndTkn, startDate) + 1;
                    
                    tknCurrentAl = tknCurrentAl + tknCurrent;
                    
                } else {
                    
                    int tknCurrent = DATEDIFF(finnishDate, startDate) + 1;
                    
                    tknCurrentAl = tknCurrentAl + tknCurrent;
                }
            }
            
            return tknCurrentAl;
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }

    /* ============ LONG LEAVE REPORT ================= */
    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan long leave prev period
     * @param departmentId
     * @param startDate
     * @return
     */
    private static float getLLPrevPeriodDetail(long empId, Date startDate) {
// private static int getLLPrevPeriodDetail(long empId, Date startDate) {

        try {
            
            float LL1 = 0;
            float LL2 = 0;
            float tot = 0;
            /**
             * int LL1 = 0; int LL2 = 0; int tot = 0;
             */
            LL1 = getLongPrevPeriodEntitle1Detail(empId, startDate);
            LL2 = getLongPrevPeriodEntitle2Detail(empId, startDate);
            
            tot = LL1 + LL2;
            
            return tot;
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return 0;
        
    }
    
    private static float getLongPrevPeriodEntitle1Detail(long empId, Date startDate) {
// private static int getLongPrevPeriodEntitle1Detail(long empId, Date startDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                // int sum = 0;
                sum = rs.getFloat(1);
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLongPrevPeriodEntitle2Detail(long empId, Date startDate) {
//private static int getLongPrevPeriodEntitle2Detail(long empId, Date startDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                sum = rs.getFloat(1);
                /**
                 * int sum = 0; sum = rs.getInt(1);
                 */
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
        
    }
    
    private static float getPrevTakenLLReportDetail(long empId, Date startDate) {
//private static int getPrevTakenLLReportDetail(long empId, Date startDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknLL = 0;
//int tknLL = 0;
            while (rs.next()) {
                
                float ll_tkn = rs.getFloat(1);
                //  int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknFinishDate = rs.getDate(3);
                
                int tknDiff = DATEDIFF(startDate, tknFinishDate);
                
                if (tknDiff >= 1) { /* Kondisi dimana start tkn dan finish tkn berada sebelum start period */
                    
                    tknLL = tknLL + ll_tkn;
                    
                } else {
                    
                    int diffTknB4 = DATEDIFF(startDate, tknStartDate);
                    tknLL = tknLL + diffTknB4;
                    
                }
                
            }
            
            return tknLL;
            
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLLPrevTakenExpiredReportDetail(long empId, Date startDate) {
// private static int getLLPrevTakenExpiredReportDetail(long empId, Date startDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " LLE ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " = LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                // int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLLCurrentPeriodDetail(long empId, Date startDate, Date endDate) {
//private static int getLLCurrentPeriodDetail(long empId, Date startDate, Date endDate) {

        try {
            float LL1 = 0;
            float LL2 = 0;
            float tot = 0;
            /**
             * int LL1 = 0; int LL2 = 0; int tot = 0;
             */
            LL1 = getLongCurrentPeriodEntitle1Detail(empId, startDate, endDate);
            LL2 = getLongCurrentPeriodEntitle2Detail(empId, startDate, endDate);
            
            tot = LL1 + LL2;
            
            return tot;
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return 0;
        
    }
    
    private static float getLongCurrentPeriodEntitle1Detail(long empId, Date startDate, Date endDate) {
// private static int getLongCurrentPeriodEntitle1Detail(long empId, Date startDate, Date endDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                sum = rs.getFloat(1);
                /**
                 * int sum = 0; sum = rs.getInt(1);
                 */
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLongCurrentPeriodEntitle2Detail(long empId, Date startDate, Date endDate) {
// private static int getLongCurrentPeriodEntitle2Detail(long empId, Date startDate, Date endDate) {

        DBResultSet dbrs = null;
        
        try {
            String sql = "SELECT SUM(LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LL INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND LL."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "=" + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float sum = 0;
                sum = rs.getFloat(1);
                /**
                 * int sum = 0; sum = rs.getInt(1);
                 */
                return sum;
            }
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
        
    }
    
    private static float getCurrentTakenLLReportDetail(long empId, Date startDate, Date endDate) {
//  private static int getCurrentTakenLLReportDetail(long empId, Date startDate, Date endDate) {

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknQty = 0;
//int tknQty = 0;
            while (rs.next()) {
                
                int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknfnsDate = rs.getDate(3);
                
                int dateDiff = DATEDIFF(endDate, tknfnsDate);
                
                if (dateDiff >= 0) {
                    
                    tknQty = tknQty + ll_tkn;
                    
                } else {
                    
                    int tknCurrent = 0;
                    tknCurrent = DATEDIFF(tknfnsDate, tknStartDate) + 1;
                    
                    tknQty = tknQty + tknCurrent;
                    
                }
                
            }
            
            float tknCrosPeriod = getCurrentTakenLLReportCroosPeriodDetail(empId, startDate, endDate);
// int tknCrosPeriod = getCurrentTakenLLReportCroosPeriodDetail(empId, startDate, endDate);
            tknQty = tknQty + tknCrosPeriod;
            
            return tknQty;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getCurrentTakenLLReportCroosPeriodDetail(long empId, Date startDate, Date endDate) {
// private static int getCurrentTakenLLReportCroosPeriodDetail(long empId, Date startDate, Date endDate){

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_QTY] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + ","
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN + " LLT "
                    + " ON LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " = LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " INNER JOIN " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP "
                    + " ON LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " = APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_DATE] + " < '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLT." + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_TAKEN_FINNISH_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId
                    + " AND APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_EXECUTED
                    + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            float tknQty = 0;
//   int tknQty = 0;
            while (rs.next()) {
                
                float ll_tkn = rs.getFloat(1);
                // int ll_tkn = rs.getInt(1);
                Date tknStartDate = rs.getDate(2);
                Date tknfnsDate = rs.getDate(3);

                /* Pengecekan untuk taken yang melewati end period */
                int dateDiff = DATEDIFF(endDate, tknfnsDate);
                
                if (dateDiff >= 0) {
                    
                    int tknCurrent = DATEDIFF(tknfnsDate, startDate) + 1;
                    tknQty = tknQty + tknCurrent;
                    
                } else {
                    
                    int tknCurrent = DATEDIFF(endDate, startDate) + 1;
                    
                    tknQty = tknQty + tknCurrent;
                    
                }
            }
            
            return tknQty;
            
        } catch (Exception E) {
            System.out.println("[ exception ] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    
    private static float getLLCurrentTakenExpiredReportDetail(long empId, Date startDate, Date endDate) {
//   private static int getLLCurrentTakenExpiredReportDetail(long empId, Date startDate, Date endDate){

        if (empId == 0 || startDate == null) {
            return 0;
        }
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT SUM(LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_QTY] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM LEFT JOIN " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " LLE ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " = LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " WHERE LLE."
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " >= '" + Formater.formatDate(startDate, "yyyy-MM-dd") + "' AND "
                    + " LLE." + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " <= '" + Formater.formatDate(endDate, "yyyy-MM-dd") + "' AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + empId + " AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                float dp_exp = rs.getFloat(1);
                //   int dp_exp = rs.getInt(1);
                return dp_exp;
            }
            
            
        } catch (Exception E) {
            System.out.println("Exception " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return 0;
    }
    /* ================ END ========== */
    
    public static boolean statusLeaveExpired(long empId) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EXPIRED_DATE] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT
                    + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "=" + PstAlStockManagement.AL_STS_AKTIF;
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                if (rs.getLong(1) > 0) {
                    return true;
                } else {
                    return false;
                }                
            }
            
        } catch (Exception E) {
            System.out.println();
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return false;
        
    }
    
    public static int getStatusAL() {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT ";
            
        } catch (Exception E) {
            System.out.println();
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk pengecekan apakah employee itu sudah bisa closing atau tidak
     * @Desc Closing digunakan untuk memberikan stock Leave pada employee,
     * @Desc yang mempunyai stock blum tetntu boleh mengambil cuti, tergantung
     * dari kapan dya boleh mengambil cuti
     * @param commencingDt
     * @param LeaveConfig
     * @param interval
     * @param intervalParamter :
     * @return true - > can closing, false - > can't closing
     */
    public static boolean canClosing(Date commencingDt, int interval, String intervalParamter) {
        
        I_Leave leaveConfig = null;
        
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        
        DBResultSet dbrs = null;
        
        try {

            /* sql untuk mendapatkan date setelah ditambahkan dengan interval */
            /* sql script */
            String sql = "SELECT DATE_ADD('" + Formater.formatDate(commencingDt, "yyyy-MM-dd") + "',INTERVAL " + leaveConfig.getMinimalWorkAL() + " " + intervalParamter + ")";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                Date tmpDt = Formater.formatDate(rs.getString(1), "yyyy-MM-dd");
                
                int diferent = getInterval(tmpDt, new Date());
                
                if (diferent > 0) {
                    return true;
                } else {
                    return false;
                }
            }
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return false;
        
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan interval antara after date dengan before date
     * @Desc Digunakan untuk pengecekan leave, antara commencing dengan waktu
     * pertama mendapatlkan leave
     * @param after
     * @param before
     * @return
     */
    public static int getInterval(Date after, Date before) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT DATEDIFF(" + Formater.formatDate(after, "yyyy-MM-dd") + "," + Formater.formatDate(before, "yyyy-MM-dd") + ")";
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                try {
                    return rs.getInt(1);
                } catch (Exception E) {
                    System.out.println("[exception] " + E.toString());
                }
            }
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
        
    }

    /**
     * @Author Roy Andika
     * @param employeeId
     * @return true - > can
     */
    public static boolean cekExpiredDate(long employeeId, I_Leave leaveConfig, AlStockTaken alStockTaken) {

        //update by satrya 2014-01-02
        
        if (employeeId == 0) {
            return false;
        }
        
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;
        
        Vector listAlStockManagement = PstAlStockManagement.list(0, 0, where, null);
        
        if (listAlStockManagement != null && listAlStockManagement.size() > 0) { /* Bila ada stock yang aktiv */
            
            AlStockManagement alStockManagement = new AlStockManagement();
            alStockManagement = (AlStockManagement) listAlStockManagement.get(0);
            
            if (alStockManagement.getExpiredDate() != null) { /* Jika kondisi expired date tidak sama dengan null */
                
                if (alStockManagement.getEntitleDate() != null) {

                    /* Bila entitle date dan expired date tidak sama dengan null*/
                    //if((new Date().getTime()/(24L*60L*60L*1000L)) < (alStockManagement.getExpiredDate().getTime()/(24L*60L*60L*1000L))){
                    if (alStockTaken != null && alStockTaken.getTakenDate() != null && (alStockTaken.getTakenDate().getTime() / (24L * 60L * 60L * 1000L)) <= (alStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L))) {
                        if (alStockTaken.getTakenFinnishDate() != null && (alStockTaken.getTakenFinnishDate().getTime() / (24L * 60L * 60L * 1000L)) <= (alStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L))) {
                            return true;
                        } else {
                            return false;
                        }
                        
                    } else {
                        
                        return false;
                        
                    }
                    
                } else {
                    
                    return false;
                    
                }
                
            } else {  /* Jika kondisi expired date sama dengan null */
                
                if (alStockManagement.getEntitleDate() != null) { //jika entitle date tidak sama dengan null or entitle date ada tetapi expired date tidak ada

                    int date = alStockManagement.getEntitleDate().getDate();
                    int month = alStockManagement.getEntitleDate().getMonth() + 1;
                    
                    if (date == leaveConfig.getDatePeriod() && month == leaveConfig.getMonthPeriod()) {
                        
                        Date expDt = DATE_ADD(alStockManagement.getEntitleDate(), 12); // masa expired menggunakan 12 bulan

                        if (new Date().getTime() / (24L * 60L * 60L * 1000L) <= expDt.getTime() / (24L * 60L * 60L * 1000L)) {
                            // updateby satrya 2013-11-12 if(new Date().getTime()/(24L*60L*60L*1000L)  <   expDt.getTime()/(24L*60L*60L*1000L)){

                            return true;
                            
                        } else {
                            
                            return false;
                            
                        }                        
                        
                    } else {

                        /* Jika kondisi entitle date tidak sama dengan periode tahunan perusahan*/
                        Date expDt = getDateExp(alStockManagement.getEntitleDate(), leaveConfig);
                        
                        if (new Date().getTime() / (24L * 60L * 60L * 1000L) <= expDt.getTime() / (24L * 60L * 60L * 1000L)) {
                            //update by satrya 2013-11-12 if(new Date().getTime()/(24L*60L*60L*1000L)  <   expDt.getTime()/(24L*60L*60L*1000L)){

                            return true;
                            
                        } else {
                            
                            return false;
                        }
                        
                    }
                    
                } else { //jika entitle date sama dengan null

                    return false;
                    
                }
            }
            
        } else {
            
            return false;
            
        }
    }

    /**
     * Create by satrya 2014-01-02 Keterangan: untuk mengecek expired date yg
     * belum di closing
     *
     * @param employeeId
     * @param alStockTaken
     * @param leaveConfig
     * @return
     */
    public static boolean cekExpiredTakenFinishDate(long employeeId, Date TakenFinishDate, I_Leave leaveConfig) {

        //update by satrya 2014-01-02
        
        if (employeeId == 0) {
            return false;
        }
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;
        
        Vector listAlStockManagement = PstAlStockManagement.list(0, 0, where, null);
        
        if (listAlStockManagement != null && listAlStockManagement.size() > 0) { /* Bila ada stock yang aktiv */
            
            AlStockManagement alStockManagement = new AlStockManagement();
            alStockManagement = (AlStockManagement) listAlStockManagement.get(0);
            
            if (alStockManagement.getExpiredDate() != null) { /* Jika kondisi expired date tidak sama dengan null */
                
                if (alStockManagement.getEntitleDate() != null) {

                    /* Bila entitle date dan expired date todak sama dengan null*/
                    if (TakenFinishDate != null && (TakenFinishDate.getTime() / (24L * 60L * 60L * 1000L)) <= (alStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L))) {
                        return true;
                    } else {
                        return false;
                    }
                    
                } else {
                    
                    return false;
                    
                }
                
            } else {  /* Jika kondisi expired date sama dengan null */
                
                if (alStockManagement.getEntitleDate() != null) { //jika entitle date tidak sama dengan null or entitle date ada tetapi expired date tidak ada

                    int date = alStockManagement.getEntitleDate().getDate();
                    int month = alStockManagement.getEntitleDate().getMonth() + 1;
                    
                    if (date == leaveConfig.getDatePeriod() && month == leaveConfig.getMonthPeriod()) {
                        
                        Date expDt = DATE_ADD(alStockManagement.getEntitleDate(), 12); // masa expired menggunakan 12 bulan

                        if (TakenFinishDate != null && (TakenFinishDate.getTime() / (24L * 60L * 60L * 1000L)) <= expDt.getTime() / (24L * 60L * 60L * 1000L)) {
                            // updateby satrya 2013-11-12 if(new Date().getTime()/(24L*60L*60L*1000L)  <   expDt.getTime()/(24L*60L*60L*1000L)){

                            return true;
                            
                        } else {
                            
                            return false;
                            
                        }                        
                        
                    } else {

                        /* Jika kondisi entitle date tidak sama dengan periode tahunan perusahan*/
                        Date expDt = getDateExp(alStockManagement.getEntitleDate(), leaveConfig);
                        
                        if (TakenFinishDate != null && (TakenFinishDate.getTime() / (24L * 60L * 60L * 1000L)) <= expDt.getTime() / (24L * 60L * 60L * 1000L)) {
                            //update by satrya 2013-11-12 if(new Date().getTime()/(24L*60L*60L*1000L)  <   expDt.getTime()/(24L*60L*60L*1000L)){

                            return true;
                            
                        } else {
                            
                            return false;
                        }
                        
                    }
                    
                } else { //jika entitle date sama dengan null

                    return false;
                    
                }
            }
            
        } else {
            
            return false;
            
        }
    }
    
    public static boolean cekExpiredTakenDate(long employeeId, Date TakenDate, I_Leave leaveConfig) {

        //update by satrya 2014-01-02
        
        if (employeeId == 0) {
            return false;
        }
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;
        
        Vector listAlStockManagement = PstAlStockManagement.list(0, 0, where, null);
        
        if (listAlStockManagement != null && listAlStockManagement.size() > 0) { /* Bila ada stock yang aktiv */
            
            AlStockManagement alStockManagement = new AlStockManagement();
            alStockManagement = (AlStockManagement) listAlStockManagement.get(0);
            
            if (alStockManagement.getExpiredDate() != null) { /* Jika kondisi expired date tidak sama dengan null */
                
                if (alStockManagement.getEntitleDate() != null) {

                    /* Bila entitle date dan expired date todak sama dengan null*/
                    if (TakenDate != null && (TakenDate.getTime() / (24L * 60L * 60L * 1000L)) <= (alStockManagement.getExpiredDate().getTime() / (24L * 60L * 60L * 1000L))) {
                        return true;
                    } else {
                        
                        return false;
                        
                    }
                    
                } else {
                    
                    return false;
                    
                }
                
            } else {  /* Jika kondisi expired date sama dengan null */
                
                if (alStockManagement.getEntitleDate() != null) { //jika entitle date tidak sama dengan null or entitle date ada tetapi expired date tidak ada

                    int date = alStockManagement.getEntitleDate().getDate();
                    int month = alStockManagement.getEntitleDate().getMonth() + 1;
                    
                    if (date == leaveConfig.getDatePeriod() && month == leaveConfig.getMonthPeriod()) {
                        
                        Date expDt = DATE_ADD(alStockManagement.getEntitleDate(), 12); // masa expired menggunakan 12 bulan

                        if (TakenDate != null && (TakenDate.getTime() / (24L * 60L * 60L * 1000L)) <= expDt.getTime() / (24L * 60L * 60L * 1000L)) {
                            // updateby satrya 2013-11-12 if(new Date().getTime()/(24L*60L*60L*1000L)  <   expDt.getTime()/(24L*60L*60L*1000L)){

                            return true;
                            
                        } else {
                            
                            return false;
                            
                        }                        
                        
                    } else {

                        /* Jika kondisi entitle date tidak sama dengan periode tahunan perusahan*/
                        Date expDt = getDateExp(alStockManagement.getEntitleDate(), leaveConfig);
                        
                        if (TakenDate != null && (TakenDate.getTime() / (24L * 60L * 60L * 1000L)) <= expDt.getTime() / (24L * 60L * 60L * 1000L)) {
                            //update by satrya 2013-11-12 if(new Date().getTime()/(24L*60L*60L*1000L)  <   expDt.getTime()/(24L*60L*60L*1000L)){

                            return true;
                            
                        } else {
                            
                            return false;
                        }
                        
                    }
                    
                } else { //jika entitle date sama dengan null

                    return false;
                    
                }
            }
            
        } else {
            
            return false;
            
        }
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan expired date dari employee dimana entitle date di
     * ketahui
     * @param entitleDt
     * @param leaveConfig
     * @return
     */
    public static Date getDateExp(Date entitleDt, I_Leave leaveConfig) {
        
        int mnthEnt = entitleDt.getMonth() + 1;

        //compare antara month

        int mont = 0;
        int date = 0;
        int year = 0;
        
        if (leaveConfig.getMonthPeriod() < mnthEnt) {
            
            mont = leaveConfig.getMonthPeriod();
            date = leaveConfig.getDatePeriod();
            year = entitleDt.getYear() + 1900 + 1; // next year dari entitle date
            
            String dt = "" + year + "-" + mont + "-" + date;
            
            Date dtExpired = Formater.formatDate(dt, "yyyy-MM-dd  23:59:59");
            
            return dtExpired;
            
        } else {
            
            mont = leaveConfig.getMonthPeriod();
            date = leaveConfig.getDatePeriod();
            year = entitleDt.getYear() + 1900; // next year dari entitle date

            String dt = "" + year + "-" + mont + "-" + date;
            
            Date dtExpired = Formater.formatDate(dt, "yyyy-MM-dd");
            dtExpired.setHours(23);
            dtExpired.setMinutes(59);
            dtExpired.setSeconds(59);
            return dtExpired;
            
        }
    }

    /**
     * @Author Putu Roy Andika
     * @param dtCheck
     * @param dtStart
     * @param dtEnd
     * @Desc true -> dateCheck berada dalam renge period, false -> dateCheck
     * berada di luar range period
     * @return
     */
    public static boolean range(Date dtCheck, Date dtStart, Date dtEnd) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT '" + Formater.formatDate(dtCheck, "yyyy-MM-dd") + "' >= '"
                    + Formater.formatDate(dtCheck, "yyyy-MM-dd") + "' AND '" + Formater.formatDate(dtCheck, "yyyy-MM-dd") + "' < '" + Formater.formatDate(dtEnd, "yyyy-MM-dd") + "'";            
            
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            
            while (rs.next()) {
                
                int value = rs.getInt(1);
                
                if (value == 1) {
                    return true;
                } else {
                    return false;
                }                
            }
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        
        return false;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan entitle date yang ke dua dan seterusnya
     * @param commencingDt
     * @param leaveConfig
     * @return
     */
    public static Date getEntitle(Date entDt, I_Leave leaveConfig) {
        
        int dt_ent = entDt.getDate();
        int mnth_ent = entDt.getMonth() + 1;
        
        if (leaveConfig.getALEntitleBy() == I_Leave.AL_ENTITLE_BY_PERIOD) {
            /* jika periode sudah berada pada peride closing tahunan */
            if (dt_ent == leaveConfig.getDateClosingAnnual() && mnth_ent == leaveConfig.getMonthClosingAnnual()) {

                /* untuk mendapatkan periode closing berikutnya*/
                Date dtNxtClosing = DATE_ADD(entDt, 12);
                return dtNxtClosing;
                
            } else {
                
                if (entDt.getMonth() >= leaveConfig.getMonthClosingAnnual()) {
                    
                    int dt_per = leaveConfig.getDateClosingAnnual();
                    int mn_per = leaveConfig.getMonthClosingAnnual();
                    int yr_per = entDt.getYear() + 1900 + 1;
                    
                    String formtDt = "" + yr_per + "-" + mn_per + "-" + dt_per;
                    
                    return Formater.formatDate(formtDt, "yyyy-MM-dd");
                    
                } else {
                    
                    
                    int dt_per = leaveConfig.getDateClosingAnnual();
                    int mn_per = leaveConfig.getMonthClosingAnnual();
                    int yr_per = entDt.getYear() + 1900;
                    
                    String formtDt = "" + yr_per + "-" + mn_per + "-" + dt_per;
                    
                    return Formater.formatDate(formtDt, "yyyy-MM-dd");
                    
                }
            }
        } else {
            /* jika periode sudah berada pada peride closing tahunan */
            if (dt_ent == leaveConfig.getDatePeriod() && mnth_ent == leaveConfig.getMonthPeriod()) {

                /* untuk mendapatkan periode closing berikutnya*/
                Date dtNxtClosing = DATE_ADD(entDt, 12);
                return dtNxtClosing;
                
            } else {
                
                if (entDt.getMonth() >= leaveConfig.getMonthPeriod()) {
                    
                    int dt_per = leaveConfig.getDatePeriod();
                    int mn_per = leaveConfig.getMonthPeriod();
                    int yr_per = entDt.getYear() + 1900 + 1;
                    
                    String formtDt = "" + yr_per + "-" + mn_per + "-" + dt_per;
                    
                    return Formater.formatDate(formtDt, "yyyy-MM-dd");
                    
                } else {
                    
                    int dt_per = leaveConfig.getDatePeriod();
                    int mn_per = leaveConfig.getMonthPeriod();
                    int yr_per = entDt.getYear() + 1900;
                    
                    String formtDt = "" + yr_per + "-" + mn_per + "-" + dt_per;
                    
                    return Formater.formatDate(formtDt, "yyyy-MM-dd");
                    
                }
            }
        }
    }

    /**
     * @Author Roy Andika
     * @param commencingDt
     * @param leaveConfig
     * @return
     */
    public static Date getEntitleFirstClosing(Date commencingDt, I_Leave leaveConfig) {

        /* Mendapatkan entitle pertama */
        Date ent = leaveConfig.getEntitle_I(commencingDt, "", "");
        int maxLoop = 0;
        Date entite = ent;
        if (/*update by satrya 2013-11-09 ent==null */ent == null || commencingDt == null) {
            return null;
        }
        while (ent.getTime() / (24L * 60L * 60L * 1000L) <= new Date().getTime() / (24L * 60L * 60L * 1000L) && maxLoop < 100) {
            
            ent = new Date(getEntitle(ent, leaveConfig).getTime() + (24 * 60 * 60 * 1000)) /*update by satrya 2013-11-12, konsepnya besoknya baru dapat entitle getEntitle(ent,leaveConfig)*/;
            
            if (ent.getTime() / (24L * 60L * 60L * 1000L) <= new Date().getTime() / (24L * 60L * 60L * 1000L)) {
                entite = ent;
            }
            maxLoop++;
            
        }
        
        return entite;
        
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mengecek diferent antara commencing date dan waktu minimal
     * untuk mendapatkan AL
     * @param cmmencingDt
     * @param leaveConfig
     * @return
     */
    public static boolean getStsHaveStock(Date cmmencingDt, I_Leave leaveConfig) {
        
        DBResultSet dbrs = null;
        
        if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_PERIOD) {
            int cmmencingYear = 0;
            if (cmmencingDt != null) {
                cmmencingYear = cmmencingDt.getYear();
            }
            Date dtClosing = new Date();
            Date dtNow = new Date();
            dtClosing.setDate(leaveConfig.getDateClosingAnnual());
            dtClosing.setMonth(leaveConfig.getMonthClosingAnnual());
            dtClosing.setYear(cmmencingYear);
            System.out.println(dtClosing);
            if (DATEDIFF(dtNow, dtClosing) > 0) {
                return true;
            } else {
                return false;
            }
            
        } else {
            try {
                
                String sql = "SELECT DATE_ADD('" + Formater.formatDate(cmmencingDt, "yyyy-MM-dd") + "',INTERVAL  " + leaveConfig.getMinimalWorkAL() + " MONTH )";
                
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                System.out.println("SQL ADD = " + sql);
                while (rs.next()) {
                    
                    Date tmp = rs.getDate(1);
                    if (tmp.getTime() / (24L * 60L * 60L * 1000L) < new Date().getTime() / (24L * 60L * 60L * 1000L)) {
                        return true;
                    } else {
                        return false;
                    }
                }
                
            } catch (Exception E) {
                System.out.println("[exception] " + E.toString());
            } finally {
                DBResultSet.close(dbrs);
            }
        }
        
        return false;
    }

    /**
     * @Author Roy Andika
     * @Desc Untuk mendapatkan entitle date
     * @param employee
     * @param al_stock_id
     * @return
     */
    public static float getEligibleAnnualLeave(I_Leave leaveConfig, AlStockTaken alStockTaken) {
        
        if (alStockTaken.getOID() != 0) {
            
            AlStockTaken objAlStockTakenInSystem = new AlStockTaken();
            
            try {
                objAlStockTakenInSystem = PstAlStockTaken.fetchExc(alStockTaken.getOID());
            } catch (Exception E) {
                System.out.println("[exception] " + E.toString());
            }
            
            float qty = (SessLeaveApplication.getAlEligbleDay(alStockTaken.getEmployeeId()) + objAlStockTakenInSystem.getTakenQty()) - alStockTaken.getTakenQty();
            
            return qty;
            
        } else {
            
            float qty = SessLeaveApplication.getAlEligbleDay(alStockTaken.getEmployeeId()) - alStockTaken.getTakenQty();
            
            return qty;
            
        }
    }

    /*
     *
     */
    public static Vector getDpApproval(long employeeId) {
        
        DBResultSet dbrs = null;
        
        try {
            
            String sql = "SELECT";
            
            
            
        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        
        
        return null;
    }

    /**
     * Crezate by satrya 2013-03-08 prosess set status to be app
     *
     * @param leaveAppId
     * @return
     */
    public static boolean processSetStatusToBeApp(long leaveAppId) {
        
        try {
            
            LeaveApplication leaveApplication = new LeaveApplication();
            
            try {
                leaveApplication = PstLeaveApplication.fetchExc(leaveAppId);
                if (leaveApplication.getDocStatus() != PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE) {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("EXCEPTION " + e.toString());
            }
            
            try {
                String sql = "UPDATE " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " SET "
                        + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE
                        + " WHERE " + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + "=" + leaveAppId;
                
                int i = DBHandler.execUpdate(sql);

                // System.out.println("SQL update doc status " + sql);

            } catch (Exception e) {
                System.out.println("[ exception ] SessLeaveApplication.processExecute() " + e.toString());
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return false;
    }

    /**
     * Keterangan: melakukan set status Approve Create by satrya ramayu
     * 2013-03-13
     *
     * @param leaveAppId
     * @return
     */
    public static boolean processSetStatusApprove(long leaveAppId) {
        
        try {
            
            LeaveApplication leaveApplication = new LeaveApplication();
            
            try {
                leaveApplication = PstLeaveApplication.fetchExc(leaveAppId);
                if (leaveApplication.getDocStatus() != PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED) {
                    return false;
                }
            } catch (Exception e) {
                System.out.println("EXCEPTION " + e.toString());
            }
            
            try {
                String sql = "UPDATE " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " SET "
                        + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "=" + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED
                        + " WHERE " + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + "=" + leaveAppId;
                
                int i = DBHandler.execUpdate(sql);

                // System.out.println("SQL update doc status " + sql);

            } catch (Exception e) {
                System.out.println("[ exception ] SessLeaveApplication.processExecute() " + e.toString());
            }
            
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return false;
    }

    /**
     * created by Kartika 2015-01-08
     *
     * @param alStockManagement
     * @param leaveConfig
     * @return Expired Date of Employee's AL
     */
    public static java.util.Date getALExpiredDateByConfig(AlStockManagement alStockManagement, I_Leave leaveConfig) {
        if (alStockManagement != null && alStockManagement.getOID() != 0) { /* Bila ada stock yang aktiv */
            if (alStockManagement.getExpiredDate() != null) { /* Jika kondisi expired date tidak sama dengan null */
                return alStockManagement.getExpiredDate();
            } else {  /* Jika kondisi expired date sama dengan null */
                
                if (alStockManagement.getEntitleDate() != null) { //jika entitle date tidak sama dengan null or entitle date ada tetapi expired date tidak ada

                    int date = alStockManagement.getEntitleDate().getDate();
                    int month = alStockManagement.getEntitleDate().getMonth() + 1;
                    
                    if (date == leaveConfig.getDatePeriod() && month == leaveConfig.getMonthPeriod()) {                        
                        return DATE_ADD(alStockManagement.getEntitleDate(), 12); // masa expired menggunakan 12 bulan                              
                    } else {

                        /* Jika kondisi entitle date tidak sama dengan periode tahunan perusahan*/
                        return getDateExp(alStockManagement.getEntitleDate(), leaveConfig);
                    }
                    
                } else { //jika entitle date sama dengan null
                    return null;
                }
            }
            
        } else {
            return null;
            
        }
    }
}
