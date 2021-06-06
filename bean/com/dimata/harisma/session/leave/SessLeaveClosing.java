/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.session.leave;

/**
 *
 * @author Tu Roy
 */
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

import com.dimata.qdep.db.*;
import com.dimata.util.LogicParser;
import com.dimata.util.Formater;

import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.leave.*;
import com.dimata.harisma.entity.search.*;
import com.dimata.util.DateCalc;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class SessLeaveClosing {

    public static int STS_CLOSE_NORMAL = 1;
    public static int STS_CLOSE_WITHOUT_TRANSFER = 2;
    public static int STS_CLOSE_INVLID = 3;
    public static int AL_CLOSE_FIRST = 4;
    //----------untuk LL --------------------------
    public static int LL_AKTIF = 0;
    public static int LL_INVALID = 1;
    public static int LL_CLOSE = 2;
    
    public static int LL_CLOSE_AKTIF_EXIST = 4;
    public static int LL_EXPIRED = 5;
    public static int LL_AKTIF_ENTITLE_2 = 6;
    public static int LL_AKTIF_EXP_1 = 7;
    public static int LL_AKTIF_EXP_2 = 8;
    public static int LL_EXTEND_1 = 9;
    public static int LL_EXTEND_2 = 10;
    public static int INTERVAL_DAY = 1;
    public static int INTERVAL_MONTH = 2;
    public static int INTERVAL_YEAR = 3;

    //-----------------------
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
    public static Vector getEmpCategoryEntitle() {

        DBResultSet dbrs = null;
        EmpCategory empCategory = new EmpCategory();
        Level level = new Level();

        I_Leave leaveConfig = null;
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        Vector lstEmpCat = PstEmpCategory.listAll();
        Vector lstLevel = PstLevel.listAll();

        Vector lstEmpCatEntitle = new Vector();
        boolean status = true;

        for (int i = 0; i < lstEmpCat.size(); i++) {

            empCategory = (EmpCategory) lstEmpCat.get(i);
            status = true;
            for (int idxLev = 0; idxLev < lstLevel.size(); idxLev++) {

                level = (Level) lstLevel.get(idxLev);
                
                float entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(),0,null, null);
                //update by satrya 2012-11-08
                // int entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(),0,null, null);

                if (entitle <= 0) {
                    status = false;
                }
            }
            if (status) {
                lstEmpCatEntitle.add(empCategory);
            }
        }

        if (lstEmpCatEntitle.size() > 0) {
            return lstEmpCatEntitle;
        } else {
            return null;
        }
    }

    public static Vector getLevelEntitle() {
        DBResultSet dbrs = null;
        EmpCategory empCategory = new EmpCategory();
        Level level = new Level();

        I_Leave leaveConfig = null;
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        Vector lstEmpCat = PstEmpCategory.listAll();
        Vector lstLevel = PstLevel.listAll();

        Vector tmpCatId = new Vector();
        Vector tmpCat = new Vector();
        Vector lstEmpLevEntitle = new Vector();
        boolean status = true;

        for (int idxLev = 0; idxLev < lstEmpCat.size(); idxLev++) {

            level = (Level) lstLevel.get(idxLev);
            status = true;
            for (int i = 0; i < lstLevel.size(); i++) {

                empCategory = (EmpCategory) lstEmpCat.get(i);

                float entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(),0, null, null);
                //int entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(),0, null, null);

                if (entitle <= 0) {
                    status = false;
                }
            }
            if (status) {
                lstEmpLevEntitle.add(level);
            }
        }

        if (lstEmpLevEntitle.size() > 0) {
            return lstEmpLevEntitle;
        } else {
            return null;
        }
    }

    public static Vector getEmployeeClosingAll() {
        DBResultSet dbrs = null;

        Vector empCatHaveClosing = getEmpCategoryEntitle();

        try {
            String sql = "";

            String whereCategory = "";

            if (empCatHaveClosing != null) {

                if (whereCategory.equals("")) {
                    whereCategory = " AND ";
                }
                int max = empCatHaveClosing.size() - 1;

                for (int idxCat = 0; idxCat < empCatHaveClosing.size(); idxCat++) {
                    EmpCategory empCategory = (EmpCategory) empCatHaveClosing.get(idxCat);
                    whereCategory = whereCategory + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]
                            + "=" + empCategory.getOID();

                    if (idxCat != max) {
                        whereCategory = whereCategory + " AND ";
                    }
                }
            }

            sql = sql + " AND " + whereCategory;

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    public static Vector getEmployeeActive(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {

        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs;
        Vector result = new Vector();

        try {

            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="
                    + PstEmployee.NO_RESIGN + " AND ( DATE_ADD(ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) <= CURRENT_DATE ) ";

            String whereClause = "";
            String strEmpNum = "";

          /*  update by satrya 2013-11-22 if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + "=" + srcLeaveAppAlClosing.getEmpNum();

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }*/
 if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(srcLeaveAppAlClosing.getEmpNum());
               // sql = sql + "  ";
                if (vectNum != null && vectNum.size() > 0) {
                    strEmpNum = strEmpNum + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strEmpNum = strEmpNum +" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strEmpNum = strEmpNum + str.trim();
                        }
                    }
                    strEmpNum = strEmpNum + ")";
                }
                
            }
             if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }


            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }

            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }
            }


            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }

            sql = sql + whereClause;

            sql = sql + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE ";

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            System.out.println("SQL LEAVE CLOSING (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();
//update by satrya 2012-11-08
                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                result.add(leaveAlClosingList);
            }
            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    public static Vector getEmployeeActiveAll(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {
        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs;
        Vector result = new Vector();

        try {
            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = "
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND DATE_ADD(ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) < CURRENT_DATE "
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];


            System.out.println("SQL Leave Application Closing all : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Employee employee = new Employee();
                AlStockManagement alStockManagement = new AlStockManagement();
                Department department = new Department();
                Vector temp = new Vector();

                alStockManagement.setOID(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                alStockManagement.setEmployeeId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                alStockManagement.setRecordDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE]));
                alStockManagement.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                alStockManagement.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                alStockManagement.setEntitleDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                alStockManagement.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                alStockManagement.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                alStockManagement.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                alStockManagement.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                temp.add(alStockManagement);

                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                temp.add(employee);

                department.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                temp.add(department);

                result.add(temp);
            }

            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    public static Vector getEmployeeActiveNextAll(long employeeId, Date entitleDate) {

        DBResultSet dbrs = null;
        Vector result = new Vector();
        String strEntitle = Formater.formatDate(entitleDate, "yyyy-MM-dd");

        try {
            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="
                    + PstEmployee.NO_RESIGN + " AND ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " > '"
                    + strEntitle + "' AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = " + employeeId
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            System.out.println("SQL Leave Application Closing all : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();


            while (rs.next()) {

                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();

                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

                result.add(leaveAlClosingList);

            }

            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    public static Vector getEmployeeActiveALL() {

        DBResultSet dbrs;
        Vector result = new Vector();
        Date dtNow = new Date();
        String strDtNow = Formater.formatDate(dtNow, "yyyy-MM-dd");

        try {
            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="
                    + PstEmployee.NO_RESIGN + " AND ( DATE_ADD(ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) <= '" + strDtNow + "' ) AND ( EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != '0000-00-00' OR EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != null ) "
                    + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE "
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            System.out.println("SQL LEAVE CLOSING ( NOT BY SEARCHING ) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long employee_id = 0;
            Date entitle_date = new Date();
            int idx = 0;

            while (rs.next()) {

                if (employee_id != 0 && employee_id != rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID])) {
                    Vector list = getEmployeeActiveNextAll(employee_id, entitle_date);

                    for (int j = 0; j < list.size(); j++) {

                        LeaveAlClosingList objleaveAlClosingList = new LeaveAlClosingList();
                        objleaveAlClosingList = (LeaveAlClosingList) list.get(j);
                        result.add(objleaveAlClosingList);

                        idx++;
                    }
                }
                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();
//update by satrya 2012-11-08
                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

                employee_id = rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]);
                entitle_date = rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]);

                result.add(leaveAlClosingList);

                idx++;
            }

            LeaveAlClosingList objleaveAlClosingList = new LeaveAlClosingList();
            objleaveAlClosingList = (LeaveAlClosingList) result.get(idx - 1);

            Vector listSt = getEmployeeActiveNextAll(objleaveAlClosingList.getEmpId(), objleaveAlClosingList.getEntitledDate());

            for (int ji = 0; ji < listSt.size(); ji++) {
                LeaveAlClosingList obj = new LeaveAlClosingList();
                obj = (LeaveAlClosingList) listSt.get(ji);
                result.add(obj);
            }

            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    public static int getCountStockAktiv(long employeeId) {

        DBResultSet dbrs = null;

        try {
            String sql = "SELECT COUNT(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ") FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + "=" + employeeId + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                    + PstAlStockManagement.AL_STS_AKTIF;

            System.out.println("SQL sum Al Aktiv : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int sum = 0;

            while (rs.next()) {
                sum = rs.getInt(1);
            }

            rs.close();
            return sum;

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return 0;
    }

    public static boolean getApplicationByStockManagement(long employeeId, long StockId) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstAlStockTaken.TBL_HR_AL_STOCK_TAKEN
                    + " ALM ON APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = ALM."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_LEAVE_APPLICATION_ID] + " WHERE ALM."
                    + PstAlStockTaken.fieldNames[PstAlStockTaken.FLD_AL_STOCK_ID] + " = " + StockId + " AND ( APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED
                    + " OR APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT
                    + " OR APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE
                    + " ) AND APP."+PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID]+"="+employeeId;

            //System.out.println("SQL get Leave Application active : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return false;
    }

    public static boolean getDataAplicationNotClosing(long empId) {

        DBResultSet dbrs = null;
        int count = 0;
        try {
            String sql = "SELECT " + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " FROM "
                    + PstLeaveApplication.TBL_LEAVE_APPLICATION + " WHERE "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_EMPLOYEE_ID] + "=" + empId + " AND ( "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "="
                    + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED + " OR "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "="
                    + PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT + " OR "
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + "="
                    + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE + " )";

            System.out.println("SQL count application active : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

            rs.close();

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        return false;

    }

    /**
     * @Author  Roy Andika
     * @Desc    
     * @param   stockId
     * @return
     */
    public static boolean getSatatusCurrentAktiv(long stockId) {
        Date current_date = new Date();
        String strCurrent_date = Formater.formatDate(current_date, "yyyy-MM-dd");

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE '"
                    + strCurrent_date + "' > " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]
                    + " AND '" + strCurrent_date + "' < DATE_ADD(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = " + stockId;

            //System.out.println("SQL cek status stock (Aktiv or Not) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return false;
    }

    public static Vector getEmployeeNotYetHaveStockAll() {

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {

            String sql = "SELECT EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " EMP LEFT JOIN "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " INNER JOIN "
                    + PstLevel.TBL_HR_LEVEL + " LEV ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " INNER JOIN " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " CAT ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "= CAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]
                    + " WHERE ( ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " is null OR ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = 0 ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + " AND ( EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != '0000-00-00' OR EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != null ) "
                    + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE "
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            //System.out.println("SQL CLOSING NO STOCK ( NOT BY SEARCHING ) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            I_Leave leaveConfig = null;
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }

            while (rs.next()) {
                int LoS = (int) DateCalc.dayDifference( rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date());
                float entitled = leaveConfig.getALEntitleAnualLeave(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]), 
                        //int entitled = leaveConfig.getALEntitleAnualLeave(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]), 
                        rs.getString(PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]), LoS, 
                        rs.getDate("EMP."+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date() );

                if (entitled > 0) {

                    LeaveAlClosingNoStockList leaveAlClosingNoStockList = new LeaveAlClosingNoStockList();

                    leaveAlClosingNoStockList.setEmpId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    leaveAlClosingNoStockList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    leaveAlClosingNoStockList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    leaveAlClosingNoStockList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                    leaveAlClosingNoStockList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                    leaveAlClosingNoStockList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    leaveAlClosingNoStockList.setEntitled(entitled);
                    result.add(leaveAlClosingNoStockList);
                }
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    public static EmployeeCommencing getCommencingDate(long empId) {
        DBResultSet dbrs = null;
        EmployeeCommencing employeeCommencing = new EmployeeCommencing();
        Vector result = new Vector();
        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " FROM "
                    + PstEmployee.TBL_HR_EMPLOYEE + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=" + empId;

            System.out.println("SQL Employee : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                employeeCommencing.setEmployeeId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employeeCommencing.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));

            }
            return employeeCommencing;

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;
    }

    public static boolean getStatusAktif(long empId, long stockId) {

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {
            String sql = "SELECT " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ","
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " FROM "
                    + PstEmployee.TBL_HR_EMPLOYEE + " WHERE " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=" + empId;

            System.out.println("SQL Employee : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                EmployeeCommencing employeeCommencing = new EmployeeCommencing();
                employeeCommencing.setEmployeeId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                employeeCommencing.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                result.add(employeeCommencing);
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return false;
    }

    public static void updateEntitledDate(long employeeId) {

        EmployeeCommencing employeeCommencing = getCommencingDate(employeeId);

        String month_date = getDateByParameter(employeeCommencing.getCommencingDate(), 1);
//update by satrya 2012-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId;

        Vector listStockManagement = PstAlStockManagement.list(0, 0, where, null);

        if (listStockManagement.size() > 0) {

            for (int index = 0; index < listStockManagement.size(); index++) {

                AlStockManagement alStockManagement = (AlStockManagement) listStockManagement.get(index);
                String year = getDateByParameter(alStockManagement.getEntitleDate(), 2);
                String year_month_date = year + "-" + month_date;

                DBResultSet dbrs = null;

                try {
                    String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                            + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " = '"
                            + year_month_date + "' WHERE " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                            + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = " + alStockManagement.getOID();

                    int i = DBHandler.execUpdate(sql);

                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                } finally {
                    DBResultSet.close(dbrs);
                }
            }

        }
    }

    public static String getDateMustClosing(Date inputDate) {

        String strDate = Formater.formatDate(inputDate, "yyyy-MM-dd");
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT DATE_ADD('" + strDate + "', INTERVAL 1 YEAR)";
            System.out.println("SQL Employee : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String result = rs.getString(1);
                return result;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return "";
    }

    public static void ProcessClosing(Vector resultClosing) {

        if (resultClosing.size() > 0) {

            for (int idx = 0; idx < resultClosing.size(); idx++) {

                LeaveAlClosing leaveAlClosing = (LeaveAlClosing) resultClosing.get(idx);
                
               int resultSts = SessLeaveClosing.statusAlStockManagement(leaveAlClosing.getEmployeeId(), leaveAlClosing.getStockId());
                //int resultSts = leaveAlClosing.getStatus();
                boolean stockNotEmpty = StockExist(leaveAlClosing.getEmployeeId());

                // update commencing date

                //sementara 20151211
                
                 //sementara untuk borobudur
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    Date commDate = new Date();
                    Date entiDate = new Date();
                     try{
                         commDate = formatter.parse(leaveAlClosing.getCommencingDate());
                         entiDate = formatter.parse(leaveAlClosing.getEntitledDate());
                     }catch(Exception e){}
                     Date nDate = new Date();
                    if ((commDate.getDate() != entiDate.getDate()) || (commDate.getMonth() != entiDate.getMonth()) ){
                        resultSts = 2;
                    }                
                
                if(false){
//                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                Date commDate = new Date();
//                Date entiDate = new Date();
//                     try{
//                         commDate = formatter.parse(leaveAlClosing.getCommencingDate());
//                         entiDate = formatter.parse(leaveAlClosing.getEntitledDate());
//                     }catch(Exception e){}
//                Date nDate = new Date();
//        
//                I_Leave leaveConfig = null;
//
//                try {
//                    leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
//                } catch (Exception e) {
//                    System.out.println("Exception : " + e.getMessage());
//                }
//                if (((commDate.getDate() != entiDate.getDate()) || (commDate.getMonth() != entiDate.getMonth()) ) && (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING)){
//                stockNotEmpty=false;
//                }
                }
                
                
                updateEntitledDate(leaveAlClosing.getEmployeeId());

                if (stockNotEmpty == true) {


                    if (leaveAlClosing.getStatus() == 1) {

                        // only close

                        if (resultSts == 3) {

                            updateStokManagementNotValid(leaveAlClosing.getStockId());

                        } else if (resultSts == 6) {

                            // kondisi normal

                            updateStockManagementNormal(leaveAlClosing);

                        } else if (resultSts == 5) {

                            updateStockManagementNoTransfer(leaveAlClosing);

                        } else if (resultSts == 2) {

                            updateStockManagementNormal(leaveAlClosing);

                            //updateStockManagementNoTransfer(leaveAlClosing);

                        }

                    } else if (leaveAlClosing.getStatus() == 2) {

                        // transfer dan close

                        UpdateStockDataTransfer(leaveAlClosing.getStockId(), leaveAlClosing.getEmployeeId(), leaveAlClosing.getExpiredDate(), leaveAlClosing.getExtended());

                    }
                } else {

                    //jika employee tidak memiliki stock

                    insertStockEmpty(leaveAlClosing.getEmployeeId());

                }
            }
        }
    }

    /**
     * @Author Roy Andika
     * @param resultClosing
     */
    public static void ProcessClosingByPeriod(Vector resultClosing) {

        if (resultClosing.size() > 0) {

            for (int idx = 0; idx < resultClosing.size(); idx++) {

                LeaveAlClosing leaveAlClosing = (LeaveAlClosing) resultClosing.get(idx);

                int resultSts = SessLeaveClosing.statusAlStockManagement(leaveAlClosing.getEmployeeId(), leaveAlClosing.getStockId());

                boolean stockNotEmpty = StockExist(leaveAlClosing.getEmployeeId());

                // update commencing date

                updateEntitledDate(leaveAlClosing.getEmployeeId());

                if (stockNotEmpty == true) {


                    if (leaveAlClosing.getStatus() == 1) {

                        // only close

                        if (resultSts == 3) {

                            updateStokManagementNotValid(leaveAlClosing.getStockId());

                        } else if (resultSts == 6) {

                            // kondisi normal

                            updateStockManagementNormal(leaveAlClosing);

                        } else if (resultSts == 5) {

                            updateStockManagementNoTransfer(leaveAlClosing);

                        } else if (resultSts == 2) {

                            updateStockManagementNormal(leaveAlClosing);

                            //updateStockManagementNoTransfer(leaveAlClosing);

                        }

                    } else if (leaveAlClosing.getStatus() == 2) {

                        // transfer dan close

                        UpdateStockDataTransfer(leaveAlClosing.getStockId(), leaveAlClosing.getEmployeeId(), leaveAlClosing.getExpiredDate(), leaveAlClosing.getExtended());

                    }
                } else {

                    //jika employee tidak memiliki stock

                    insertStockEmpty(leaveAlClosing.getEmployeeId());

                }
            }
        }
    }

    public static AlStockManagement getAlStokTakenAktifFirst(long empId) {
        DBResultSet dbrs = null;
        Vector result = new Vector();
//update by satrya 2012-10-16
        String where =PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                + "=" + empId + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                + PstAlStockManagement.AL_STS_AKTIF;

        Vector list = PstAlStockManagement.list(0, 0, where, PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " ASC ");

        if (list != null && list.size() > 1) {
            AlStockManagement alStockManagement = new AlStockManagement();
            alStockManagement = (AlStockManagement) list.get(0);
            return alStockManagement;
        }

        return null;
    }

    public static int getCountStockManagement(long employeeId) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT COUNT(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ") FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employeeId
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                    + PstAlStockManagement.AL_STS_AKTIF;

           // System.out.println("SQL count stock : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                int count = rs.getInt(1);
                return count;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {

            DBResultSet.close(dbrs);
        }
        return 0;
    }

    public static boolean getStatusStockTrue(long employee_id, long stock_id) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employee_id
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                    + PstAlStockManagement.AL_STS_AKTIF + " AND " + " ( DATE_ADD("
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) < CURRENT_DATE ) AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = " + stock_id;

            System.out.println("SQL status stock : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {

            DBResultSet.close(dbrs);

        }
        return false;
    }

    public static Vector getAlStockManagementAktif(long employeeId) {
//update by satrya 2012-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employeeId
                + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                + PstAlStockManagement.AL_STS_AKTIF;
        String order = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " ASC ";

        Vector list = PstAlStockManagement.list(0, 0, where, order);

        if (list != null || list.size() > 0) {
            return list;
        }

        return null;
    }

    public static AlStockManagement getDataStockTransfer(long employee_id) {
//update by satrya 2012-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employee_id
                + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                + PstAlStockManagement.AL_STS_AKTIF;

        String order = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ","
                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " ASC ";

        Vector list = PstAlStockManagement.list(0, 0, where, order);
        boolean sts_data = false;
        int idx = 0;

        for (int i = 0; i < list.size(); i++) {

            AlStockManagement alStockManagement = (AlStockManagement) list.get(i);
            sts_data = getSatatusCurrentAktiv(alStockManagement.getOID());

            if (sts_data == true) {

                idx = i;

            }

        }

        AlStockManagement dataTransfer = (AlStockManagement) list.get(idx - 1);

        return dataTransfer;

    }

    public static Date getExpiredDate(long stockId) {
        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EXPIRED_DATE] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = " + stockId;

            dbrs = DBHandler.execQueryResult(sql);

            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Date exp = rs.getDate(1);
                return exp;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    public static void UpdateStockDataTransfer(long stockIdTransfer, long employee_id, int expired, float extend) {

        DBResultSet dbrs = null;

        long stockAktif = getDataAktif(employee_id);
        AlStockManagement alStockAktif = new AlStockManagement();
        AlStockManagement alStockTransf = new AlStockManagement();

        try {
            alStockAktif = PstAlStockManagement.fetchExc(stockAktif);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            alStockTransf = PstAlStockManagement.fetchExc(stockIdTransfer);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        //AlStockManagement alStockWillTransfer = getDataStockTransfer(employee_id);

        //----------------data old ----------------------        
        //int opening_old = alStockTransf.getPrevBalance() + alStockTransf.getAlQty() - alStockTransf.getQtyUsed();
        float opening_old = extend;

        //---------------data new -----------------------
        float opening_nw = extend + alStockAktif.getOpening();
        float previous_bal_nw = extend + alStockAktif.getPrevBalance();

        Date ent = new Date();

        ent = getExpiredDate(alStockAktif.getOID());

        if (ent == null) {

            ent = new Date();

        }

        String strexpird = getDateIntervalMonth(expired, ent);

        updateStockManagement(stockIdTransfer);

        try {

            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + "=" + opening_nw + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EXPIRED_DATE] + " = '" + strexpird + "',"
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + "=" + previous_bal_nw + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "=" + alStockAktif.getOID();

            int i = DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static void updateStockManagement(long stock_id) {
        try {

            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "=" + PstAlStockManagement.AL_STS_NOT_AKTIF + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "=" + stock_id;

            int i = DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
    }

    public static long getDataAktif(long employee_id) {
//update by satrya 2012-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employee_id
                + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                + PstAlStockManagement.AL_STS_AKTIF;
        String order = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ","
                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " ASC ";

        Vector result = PstAlStockManagement.list(0, 0, where, order);

        boolean stsStockAktf = false;
        long stock_aktif = 0;

        for (int i = 0; i < result.size(); i++) {

            AlStockManagement alStockManagement = (AlStockManagement) result.get(i);

            stsStockAktf = getSatatusCurrentAktiv(alStockManagement.getOID());

            if (stsStockAktf == true) {

                stock_aktif = alStockManagement.getOID();
                return stock_aktif;
            }
        }

        return 0;

    }

    public static int getStatusNextAktif(long employeeId, long stockId) {

        int idx = 0;
        int idxTrf = 0;
//update by satrya 2012-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employeeId
                + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                + PstAlStockManagement.AL_STS_AKTIF;

        String order = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ","
                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " ASC ";

        LeaveAppListIndex objNextPeriod = new LeaveAppListIndex();
        LeaveAppListIndex objPeriod = new LeaveAppListIndex();

        Vector list = PstAlStockManagement.list(0, 0, where, order);


        int i = 0;

        for (i = 0; i < list.size(); i++) {

            boolean stsData = false;

            AlStockManagement alStockManagement = (AlStockManagement) list.get(i);

            stsData = getSatatusCurrentAktiv(alStockManagement.getOID());

            if (stsData == true) {

                objNextPeriod.setStockManagementId(alStockManagement.getOID());
                objNextPeriod.setIndex(i);
                idx = i;

            } else {

                String dateClose = getDateMustClosing(alStockManagement.getEntitleDate());

            }
        }

        idxTrf = idx - 1;

        if (idxTrf < 0) { //kondisi stock yang aktif belum ada

            AlStockManagement objAlStock = (AlStockManagement) list.get(i - 1);

            if (objAlStock.getOID() == stockId) {

                return 3;

            } else {

                return 0;

            }

        }

        AlStockManagement objAlStockManagement = (AlStockManagement) list.get(idxTrf);

        int strResult;

        if (stockId == objNextPeriod.getStockManagementId()) {

            strResult = 1;

        } else if (stockId == objAlStockManagement.getOID()) {

            strResult = 2;

        } else {

            strResult = 0;

        }

        return strResult;
    }

    public static Vector ListAlStockManagementWithIndex(long employeeId) {

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {

            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employeeId + " ORDER BY "
                    + PstAlStockManagement.FLD_ENTITLE_DATE;

            System.out.println("SQL list stock index : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int idx = 0;

            while (rs.next()) {
                LeaveAppListIndex leaveAppListIndex = new LeaveAppListIndex();

                leaveAppListIndex.setIndex(idx);
                leaveAppListIndex.setStockManagementId(rs.getLong(1));

                idx++;

            }

            return result;

        } catch (Exception e) {

            System.out.println("Exception " + e.toString());

        } finally {
            DBResultSet.close(dbrs);
        }

        return null;
    }

    public static String getDateByParameter(Date inputDate, int status) {
        String strInputdate = Formater.formatDate(inputDate, "yyyy-MM-dd");
        DBResultSet dbrs = null;
        try {
            String sql = "";

            if (status == 1) {
                sql = "SELECT DATE_FORMAT('" + strInputdate + "','%m-%d')";
            } else if (status == 2) {
                sql = "SELECT DATE_FORMAT('" + strInputdate + "','%Y')";
            }
            System.out.println("SQL Employee : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String result = rs.getString(1);
                return result;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return "";
    }

    public static void updateStockManagementNormal(LeaveAlClosing leaveAlClosing) {

        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = " + leaveAlClosing.getStockId();

        Vector result = PstAlStockManagement.list(0, 0, where, null);

        AlStockManagement alStockManagement = (AlStockManagement) result.get(0);

        AlStockManagement insertStock = new AlStockManagement();

        Employee employee = new Employee();
        EmpCategory empCategory = new EmpCategory();
        Level level = new Level();

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        int interval_LL_I = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];      // range mendapatkan LL (dalam bulan)
        int interval_LL_II = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];  // range mendapatkan LL (dalam bulan)

        try {
            employee = PstEmployee.fetchExc(leaveAlClosing.getEmployeeId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            level = PstLevel.fetchExc(employee.getLevelId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        String entitleDte = getEntitleDate(alStockManagement.getEntitleDate());

        Date dtEnt = Formater.formatDate(entitleDte, "yyyy-MM-dd");

        int startIntrvlLL_1 = interval_LL_I;
        int startIntrvlLL_2 = interval_LL_II;

        boolean stsGetLL = false;

        boolean next = true;
        int iterasi = 0;

        while (next == true && iterasi < 50) {

            
        Date empDate1years = employee.getCommencingDate();
        empDate1years.setYear(empDate1years.getYear()+1);
            String entitle_ll_1 = getDateIntervalMonth(startIntrvlLL_1, empDate1years);
            String entitle_ll_2 = getDateIntervalMonth(startIntrvlLL_2, empDate1years);

            Date ent_LL_1 = Formater.formatDate(entitle_ll_1, "yyyy-MM-dd");
            Date ent_LL_2 = Formater.formatDate(entitle_ll_2, "yyyy-MM-dd");

            int DateDiff1 = DATEDIFF(dtEnt, ent_LL_1);
            int DateDiff2 = DATEDIFF(dtEnt, ent_LL_2);

            if (DateDiff1 == 0 || DateDiff2 == 0) {

                stsGetLL = true;
                next = false;

            } else if (DateDiff1 < 0 || DateDiff2 < 0) {

                next = false;

            }
            startIntrvlLL_1 = startIntrvlLL_1 + startIntrvlLL_1;
            startIntrvlLL_2 = startIntrvlLL_2 + startIntrvlLL_2;

            iterasi++;
        }

        float entitle = 0;

        if (stsGetLL == true) {
            entitle = leaveConfig.getALEntitlebyLL(level.getLevel(), empCategory.getEmpCategory());
        } else {
            int LoS = (int) DateCalc.dayDifference( employee.getCommencingDate(), new Date() );
            entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS,
                    employee.getCommencingDate(), new Date());
        }

        //insert new data

        insertStock.setLeavePeriodeId(0);
        insertStock.setEmployeeId(alStockManagement.getEmployeeId());
        insertStock.setOpening(leaveAlClosing.getExtended());
        insertStock.setPrevBalance(leaveAlClosing.getExtended());
        insertStock.setAlQty(entitle + leaveAlClosing.getExtended());//update by satrya 2013-10-28 insertStock.setAlQty(entitle);
        insertStock.setQtyUsed(0);
        insertStock.setAlStatus(PstAlStockManagement.AL_STS_AKTIF);
        insertStock.setStNote("");
        insertStock.setEntitled(entitle);
        insertStock.setRecordDate(new Date());

        Date strDt = Formater.formatDate(leaveAlClosing.getEntitledDate(), "yyyy-MM-dd");

        String year = getDateByParameter(strDt, 2);

        int tmp = 0;

        try {
            tmp = Integer.parseInt(year);

        } catch (Exception e) {

            System.out.println("Exception " + e.toString());

        }

        tmp = tmp + 1;

        String month_day = getDateByParameter(employee.getCommencingDate(), 1);

        String entitleDt = tmp + "-" + month_day;

        Date dtEntitle = Formater.formatDate(entitleDt, "yyyy-MM-dd");

        String expired = getDateIntervalMonth(leaveAlClosing.getExpiredDate(), dtEntitle);

        Date dtExp = Formater.formatDate(expired, "yyyy-MM-dd");

        insertStock.setEntitleDate(dtEnt);

        insertStock.setExpiredDate(dtExp);

        

        DBResultSet dbrs = null;

        try {

            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_NOT_AKTIF + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "="
                    + leaveAlClosing.getStockId();

            int i = DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        try {

            PstAlStockManagement.insertExc(insertStock);

        } catch (Exception e) {

            System.out.println("Exception " + e.toString());

        }
    }

    public static boolean getStatusLL(Date comencingEmployee, int interval) {

        DBResultSet dbrs = null;

        try {

            String sql = "";


        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }


        return false;
    }

    public static void updateStockManagementNoTransfer(LeaveAlClosing leaveAlClosing) {

        DBResultSet dbrs = null;

        try {

            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_NOT_AKTIF + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "="
                    + leaveAlClosing.getStockId();

            int i = DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

    }

    public static void updateStockManagementTransfer(LeaveAlClosing leaveAlClosing) {

        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = " + leaveAlClosing.getStockId();

        Vector result = PstAlStockManagement.list(0, 0, where, null);

        AlStockManagement alStockManagement = (AlStockManagement) result.get(0);

        AlStockManagement insertStock = new AlStockManagement();

        Employee employee = new Employee();
        EmpCategory empCategory = new EmpCategory();
        Level level = new Level();

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        try {
            employee = PstEmployee.fetchExc(leaveAlClosing.getEmployeeId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }


        try {
            level = PstLevel.fetchExc(employee.getLevelId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        int LoS= (int)DateCalc.dayDifference( employee.getCommencingDate(), new Date());
        float entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, 
                // int entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, 
                employee.getCommencingDate(), new Date());

        //insert new data

        insertStock.setLeavePeriodeId(0);
        insertStock.setEmployeeId(alStockManagement.getEmployeeId());
        insertStock.setOpening(leaveAlClosing.getExtended());
        insertStock.setPrevBalance(leaveAlClosing.getExtended());
        insertStock.setAlQty(0);
        insertStock.setQtyUsed(0);
        insertStock.setAlStatus(PstAlStockManagement.AL_STS_AKTIF);
        insertStock.setStNote("");
        insertStock.setEntitled(entitle);
        insertStock.setRecordDate(new Date());


        Date strDt = new Date();

        String year = getDateByParameter(strDt, 2);

        String month_day = getDateByParameter(employee.getCommencingDate(), 1);

        String entitleDt = year + "-" + month_day;

        Date dtEntitle = Formater.formatDate(entitleDt, "yyyy-MM-dd");

        String expired = getDateIntervalMonth(leaveAlClosing.getExpiredDate(), dtEntitle);

        Date dtExp = Formater.formatDate(expired, "yyyy-MM-dd");
        insertStock.setEntitleDate(dtEntitle);

        insertStock.setExpiredDate(dtExp);

        try {
            PstAlStockManagement.insertExc(insertStock);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        DBResultSet dbrs = null;

        try {

            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_NOT_AKTIF + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "="
                    + leaveAlClosing.getStockId();

            int i = DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

    }

    public static void updateStokManagementNotValid(long stockId) {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_NOT_VALID + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "="
                    + stockId;

            int i = DBHandler.execUpdate(sql);

            System.out.println("---------UPDATE STOCK " + stockId + " SUCCESS----------------------");

        } catch (Exception e) {
            System.out.println("---------UPDATE STOCK " + stockId + " GAGAL----------------------");
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static void updateStokManagementNotAktif(long stockId, String expired) {
        DBResultSet dbrs = null;

        try {
            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_NOT_AKTIF + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "="
                    + stockId;

            int i = DBHandler.execUpdate(sql);

            System.out.println("---------UPDATE STOCK " + stockId + " SUCCESS----------------------");

        } catch (Exception e) {
            System.out.println("---------UPDATE STOCK " + stockId + " GAGAL----------------------");
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static void updateStokManagementNotAktifWithoutTransfer(long stockId) {
        DBResultSet dbrs = null;

        try {
            String sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_NOT_AKTIF + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + "="
                    + stockId;

            int i = DBHandler.execUpdate(sql);

            System.out.println("---------UPDATE STOCK NOT AKTIF " + stockId + " SUCCESS----------------------");

        } catch (Exception e) {
            System.out.println("---------UPDATE STOCK NOT AKTIF " + stockId + " GAGAL----------------------");
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static String getDateIntervalMonth(int interval, Date now) {
        DBResultSet dbrs = null;
        String sekarang = Formater.formatDate(now, "yyyy-MM-dd");
        String result = "";
        try {

            String sql = "SELECT DATE_ADD('" + sekarang + "', INTERVAL " + interval + " MONTH)";

            System.out.println("SQL interval : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result = rs.getString(1);
                return result;
            }


        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    public static int statusAlStockManagement(long employee_id, long stock_id) {
//update by satrya 2012-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id;
        String order = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " ASC ";

        Vector listStock = PstAlStockManagement.list(0, 0, where, order);

        if (listStock.size() == 1) {

            AlStockManagement alStockManagement = (AlStockManagement) listStock.get(0);

            boolean sts = getSatatusCurrentAktiv(alStockManagement.getOID());

            if (sts) {

                //kondisi aktif

                if (alStockManagement.getOID() == stock_id) {

                    return 1; //situsi stock aktif

                } else {

                    boolean sts_close = stsAlStockManagementMustClose(alStockManagement.getOID());

                    if (sts_close) {

                        return 2; //entitle < current 

                    } else {

                        return 3; //entitle stock invalid

                    }
                }
            } else {

                boolean sts_close = stsAlStockManagementMustClose(alStockManagement.getOID());

                if (sts_close) {

                    return 2; //entitle < current 

                } else {

                    return 3; //entitle stock invalid

                }
                // }
            }
        } else if (listStock.size() > 1) {

            boolean aktif_true = false;

            for (int i = 0; i < listStock.size(); i++) {

                AlStockManagement alStockManagement = (AlStockManagement) listStock.get(i);

                boolean sts = getSatatusCurrentAktiv(alStockManagement.getOID());

                if (sts) {

                    //kondisi yang aktif ada dan jumlah stock banyak

                    if (alStockManagement.getOID() == stock_id) {

                        return 4;

                    }

                    aktif_true = true;
                }
            }

            if (aktif_true) {

                boolean sts_close = stsAlStockManagementMustClose(stock_id);

                if (sts_close) {

                    long stock = stockIdMustClose(employee_id);

                    if (stock == stock_id) {

                        return 5; // kondisi akan ditransfer apa tidak

                    } else {

                        return 3;// kondisi stock tidak valid
                    }

                } else {

                    return 3; // kondisi stock tidak valid

                }

            } else {

                boolean sts_close = stsAlStockManagementMustClose(stock_id);

                if (sts_close) {

                    long stock = stockIdMustClose(employee_id);

                    if (stock == stock_id) {

                        return 6; // kondisi valid

                    } else {

                        return 3;// kondisi stock tidak valid
                    }

                } else {

                    return 3; // kondisi stock tidak valid

                }

            }
        }
        return -1;
    }

    public static int statusAlStockManagementByPeriod(long employee_id, long stock_id) {
//update by satrya 2010-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id;
        String order = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " ASC ";

        Vector listStock = PstAlStockManagement.list(0, 0, where, order);

        if (listStock.size() == 1) {

            AlStockManagement alStockManagement = (AlStockManagement) listStock.get(0);

            boolean sts = getSatatusCurrentAktiv(alStockManagement.getOID());

            if (sts) {

                //kondisi aktif

                if (alStockManagement.getOID() == stock_id) {

                    return 1; //situsi stock aktif

                } else {

                    boolean sts_close = stsAlStockManagementMustClose(alStockManagement.getOID());

                    if (sts_close) {

                        return 2; //entitle < current 

                    } else {

                        return 3; //entitle stock invalid

                    }
                }
            } else {

                //if (alStockManagement.getOID() == stock_id) {

                //    return 1; //situsi stock aktif

                //} else {

                boolean sts_close = stsAlStockManagementMustClose(alStockManagement.getOID());

                if (sts_close) {

                    return 2; //entitle < current 

                } else {

                    return 3; //entitle stock invalid

                }
                // }
            }
        } else if (listStock.size() > 1) {

            boolean aktif_true = false;

            for (int i = 0; i < listStock.size(); i++) {

                AlStockManagement alStockManagement = (AlStockManagement) listStock.get(i);

                boolean sts = getSatatusCurrentAktiv(alStockManagement.getOID());

                if (sts) {

                    //kondisi yang aktif ada dan jumlah stock banyak

                    if (alStockManagement.getOID() == stock_id) {

                        return 4;

                    }

                    aktif_true = true;
                }
            }

            if (aktif_true) {

                boolean sts_close = stsAlStockManagementMustClose(stock_id);

                if (sts_close) {

                    long stock = stockIdMustClose(employee_id);

                    if (stock == stock_id) {

                        return 5; // kondisi akan ditransfer apa tidak

                    } else {

                        return 3;// kondisi stock tidak valid
                    }

                } else {

                    return 3; // kondisi stock tidak valid

                }

            } else {

                boolean sts_close = stsAlStockManagementMustClose(stock_id);

                if (sts_close) {

                    long stock = stockIdMustClose(employee_id);

                    if (stock == stock_id) {

                        return 6; // kondisi valid

                    } else {

                        return 3;// kondisi stock tidak valid
                    }

                } else {

                    return 3; // kondisi stock tidak valid

                }

            }
        }
        return -1;
    }

    public static boolean stsAlStockManagementMustClose(long stock_id) {

        Date current_date = new Date();
        String strCurrent_date = Formater.formatDate(current_date, "yyyy-MM-dd");

        DBResultSet dbrs = null;

        try {
            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + " DATE_ADD(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH ) <= CURRENT_DATE AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = " + stock_id;

            //System.out.println("SQL cek status stock : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return false;
    }

    public static long stockIdMustClose(long employee_id) {

        Date current_date = new Date();
        String strCurrent_date = Formater.formatDate(current_date, "yyyy-MM-dd");

        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + " DATE_ADD(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH ) <= CURRENT_DATE AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id + " ORDER BY "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " DESC ";

            //System.out.println("SQL cek status stock : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                long stock_id = rs.getLong(1);
                return stock_id;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    public static String getEntitleDate(Date entitle_date) {

        DBResultSet dbrs = null;

        Date current_date = new Date();

        String frmt = Formater.formatDate(entitle_date, "yyyy-MM-dd");

        String year = getDateByParameter(entitle_date, 2);

        int int_year = 0;

        try {

            int_year = Integer.parseInt(year);

        } catch (Exception e) {

            System.out.println("Exception " + e.toString());

        }


        boolean sts = DateDiff(current_date, entitle_date); //status entitle < current

        //if (((int) entitle_date.getTime() / (24 * 60 * 60 * 1000)) < ((int) current_date.getTime() / (24 * 60 * 60 * 1000))) {

        if (sts) {
            for (int i = 0; i < 50; i++) {

                boolean status = getStsAktiv(frmt);

                if (status == true) {

                    return frmt;

                }

                frmt = getNextMonth(frmt);
            }
        }
        return null;
    }

    public static String getEntitleDateLL(Date entitle_date, int interval) {

        Date current_date = new Date();

        String frmt = Formater.formatDate(entitle_date, "yyyy-MM-dd");

        boolean sts = DateDiff(current_date, entitle_date); //status entitle < current

        if (sts) {
            for (int i = 0; i < 50; i++) {

                boolean status = getStsAktivLL(frmt, interval);

                if (status == true) {
                    if (i == 0) {
                        return null;
                    } else {
                        return frmt;
                    }
                }

                frmt = getNextMonth(frmt, interval);
            }
        }
        return null;
    }

    public static String getEntitleDateLLFirst(Date entitle_date, int interval) {

        Date current_date = new Date();

        String frmt = Formater.formatDate(entitle_date, "yyyy-MM-dd");

        boolean sts = DateDiff(current_date, entitle_date); //status entitle < current

        if (sts) {
            for (int i = 0; i < 50; i++) {

                boolean status = getStsAktivLL(frmt, interval);

                if (status == true) {
                    return frmt;
                }

                frmt = getNextMonth(frmt, interval);
            }
        }
        return null;
    }

    public static boolean DateDiff(Date from, Date to) {

        String str_from = Formater.formatDate(from, "yyyy-MM-dd");
        String str_to = Formater.formatDate(to, "yyyy-MM-dd");

        DBResultSet dbrs = null;

        try {
            String sql = "SELECT DATEDIFF('" + str_from + "','" + str_to + "');";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                int result = rs.getInt(1);

                if (result > 0) {
                    return true;
                }

            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        return false;
    }

    public static String getNextMonth(String date) {

        DBResultSet dbrs = null;
        try {

            String sql = "SELECT DATE_ADD('" + date + "', INTERVAL 1 YEAR)";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String nextMonth = rs.getString(1);
                return nextMonth;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    public static String getNextMonth(String date, int interval) {

        DBResultSet dbrs = null;
        try {

            String sql = "SELECT DATE_ADD('" + date + "', INTERVAL " + interval + " MONTH )";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String nextMonth = rs.getString(1);
                return nextMonth;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    public static boolean getStsAktiv(String entitle_date) {

        DBResultSet dbrs = null;
        try {

            //String sql = "SELECT CURRENT_DATE BETWEEN '" + entitle_date + "' AND  DATE_ADD('" + entitle_date + "', INTERVAL 12 MONTH)";
            String sql = "SELECT CURRENT_DATE >= '" + entitle_date + "' AND CURRENT_DATE < DATE_ADD('" + entitle_date + "', INTERVAL 12 MONTH)";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                int result = rs.getInt(1);

                if (result == 1) {
                    return true;
                }
                if (result == 0) {
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

    public static boolean getStsAktivLL(String entitle_date, int interval) {

        DBResultSet dbrs = null;
        try {

            String sql = "SELECT CURRENT_DATE >= '" + entitle_date + "' AND CURRENT_DATE < DATE_ADD('" + entitle_date + "', INTERVAL " + interval + " MONTH)";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                int result = rs.getInt(1);

                if (result == 1) {
                    return true;
                }
                if (result == 0) {
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

    //cek stock ada atau tidak
    public static boolean StockExist(long employee_id) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        return false;
    }

    public static void insertStockEmpty(long employee_id) {

        Employee employee = new Employee();
        EmpCategory empCategory = new EmpCategory();
        Level level = new Level();

        try {
            employee = PstEmployee.fetchExc(employee_id);

        } catch (Exception e) {

            System.out.println("Exception : " + e.getMessage());
        }

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        int interval_LL_I = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];      // range mendapatkan LL (dalam bulan)
        int interval_LL_II = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];  // range mendapatkan LL (dalam bulan)

        try {
            level = PstLevel.fetchExc(employee.getLevelId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        String entitleDte = getEntitleDate(employee.getCommencingDate());

        Date dtEnt = Formater.formatDate(entitleDte, "yyyy-MM-dd");

        int startIntrvlLL_1 = interval_LL_I;
        int startIntrvlLL_2 = interval_LL_II;

        boolean stsGetLL = false;

        boolean next = true;
        int iterasi = 0;

        while (next == true && iterasi < 50) {

            
        Date empDate1years = employee.getCommencingDate();
        empDate1years.setYear(empDate1years.getYear()+1);
            
            String entitle_ll_1 = getDateIntervalMonth(startIntrvlLL_1, empDate1years);
            String entitle_ll_2 = getDateIntervalMonth(startIntrvlLL_2, empDate1years);

            Date ent_LL_1 = Formater.formatDate(entitle_ll_1, "yyyy-MM-dd");
            Date ent_LL_2 = Formater.formatDate(entitle_ll_2, "yyyy-MM-dd");

            int DateDiff1 = DATEDIFF(dtEnt, ent_LL_1);
            int DateDiff2 = DATEDIFF(dtEnt, ent_LL_2);

            if (DateDiff1 == 0 || DateDiff2 == 0) {

                stsGetLL = true;
                next = false;

            } else if (DateDiff1 < 0 || DateDiff2 < 0) {

                next = false;

            }
            startIntrvlLL_1 = startIntrvlLL_1 + startIntrvlLL_1;
            startIntrvlLL_2 = startIntrvlLL_2 + startIntrvlLL_2;

            iterasi++;
        }

        float entitle = 0;
        float entitleAlExtra=0;
        Date dtEntitleAlExtra=null;
        if (stsGetLL == true) {

            entitle = leaveConfig.getALEntitlebyLL(level.getLevel(), empCategory.getEmpCategory());

        } else {
            int LoS = (int) DateCalc.dayDifference(employee.getCommencingDate(), new Date());
            entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS,
                    employee.getCommencingDate(), new Date());
            //update by satrya 2013-11-22
           entitleAlExtra = 0;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraAl(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(), new Date());
           dtEntitleAlExtra = null; //update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraDateAl(entitleAlExtra, employee.getCommencingDate());
        }

        AlStockManagement alStockManagement = new AlStockManagement();

        alStockManagement.setLeavePeriodeId(0);
        alStockManagement.setEmployeeId(employee.getOID());
        alStockManagement.setOpening(0);
        alStockManagement.setPrevBalance(0);
        alStockManagement.setEntitled(entitle);
        alStockManagement.setAlQty(alStockManagement.getPrevBalance()+ alStockManagement.getEntitled());//update by satrya 2013-10-28 alStockManagement.setAlQty(entitle);
        alStockManagement.setQtyUsed(0);
        alStockManagement.setStNote("");
        alStockManagement.setRecordDate(new Date());
        alStockManagement.setEntitleDate(dtEnt);
        //update by satrya 2013-11-22
        alStockManagement.setExtraAlDate(dtEntitleAlExtra);
        alStockManagement.setExtraAl(entitleAlExtra); 

        try {
            PstAlStockManagement.insertExc(alStockManagement);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    public static void procesDataFirstAL() {
//update by satrya 2012-10-16
        String where = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID];
        Vector listAlStockManagement = PstAlStockManagement.list(0, 0, where, null);

        for (int i = 0; i < listAlStockManagement.size(); i++) {

            AlStockManagement alStockManagement = (AlStockManagement) listAlStockManagement.get(i);
            Employee employee = new Employee();
            Level level = new Level();
            EmpCategory empCategory = new EmpCategory();

            try {
                employee = PstEmployee.fetchExc(alStockManagement.getEmployeeId());

            } catch (Exception e) {

                System.out.println("Exception : " + e.getMessage());
            }

            I_Leave leaveConfig = null;

            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }

            try {
                level = PstLevel.fetchExc(employee.getLevelId());
            } catch (Exception e) {
                System.out.println("Exception " + e.toString());
            }

            try {
                empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
            } catch (Exception e) {
                System.out.println("Exception " + e.toString());
            }

            boolean entitle_sts = false;
            float entitle = 0;

            if (level.getLevel().length() > 0 && empCategory.getEmpCategory().length() > 0) {
                entitle_sts = true;
                int LoS = (int) DateCalc.dayDifference(employee.getCommencingDate(), new Date());
                entitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS,
                        employee.getCommencingDate(), new Date());
            }
            Date current_date = new Date();

            String str_date = Formater.formatDate(current_date, "yyyy-MM-dd");

            AlStockManagement insertAlStockManagement = new AlStockManagement();

            float taken = entitle - alStockManagement.getQtyResidue();  // selisih dengan residu dengan entitle

            float prev_balance = alStockManagement.getQtyResidue() - entitle; // selisih residu 

            insertAlStockManagement.setRecordDate(current_date);

            insertAlStockManagement.setEntitleDate(current_date);

            String entitledt = getEntitleDate(employee.getCommencingDate());

            DBResultSet dbrs = null;

            if (entitle_sts == true) {
                try {
                    String sql;

                    if (taken >= 0) {

                        sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + "=" + 0 + entitle  /* yg benar rumusnya  prev balance + entitle *//* update by satrya 2013-10-28 entitle*/ + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + "=" + entitle + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + "=" + 0 + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + "= 0 " + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "=" + PstAlStockManagement.AL_STS_AKTIF + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + "= 0 " + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " = '" + entitledt + "',"
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + "=" + taken + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + "= '" + str_date + "' WHERE "
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + alStockManagement.getEmployeeId();
                    } else {

                        sql = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + "=" + + prev_balance + entitle  /* yg benar rumusnya  prev balance + entitle *//* update by satrya 2013-10-28 entitle*/ + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + "=" + entitle + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + "=" + prev_balance + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + "= 0 " + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "=" + PstAlStockManagement.AL_STS_AKTIF + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + "= 0 " + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " = '" + entitledt + "',"
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + "=" + 0 + ","
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + "= '" + str_date + "' WHERE "
                                + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + alStockManagement.getEmployeeId();

                    }

                    int ix = DBHandler.execUpdate(sql);

                    System.out.println("---------UPDATE STOCK AKTIF " + alStockManagement.getEmployeeId() + " SUCCESS----------------------");

                } catch (Exception e) {
                    System.out.println("Exception " + e.toString());
                }
            }
        }
    }

    public static void procesDataFirstLL() {

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }

        int interval = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];      // range mendapatkan LL (dalam bulan)
        int interval_II = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];  // range mendapatkan LL (dalam bulan)


        Vector listLlStockManagement = PstLLStockManagement.list(0, 0, null, null);

        for (int i = 0; i < listLlStockManagement.size(); i++) {

            LLStockManagement llStockManagement = (LLStockManagement) listLlStockManagement.get(i);
            Employee employee = new Employee();
            Level level = new Level();
            EmpCategory empCategory = new EmpCategory();

            try {
                employee = PstEmployee.fetchExc(llStockManagement.getEmployeeId());

            } catch (Exception e) {

                System.out.println("Exception : " + e.getMessage());
            }

            try {
                level = PstLevel.fetchExc(employee.getLevelId());
            } catch (Exception e) {
                System.out.println("Exception " + e.toString());
            }

            try {
                empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
            } catch (Exception e) {
                System.out.println("Exception " + e.toString());
            }

            int extend_1 = leaveConfig.getLLValidityMonths(level.getLevel(), empCategory.getEmpCategory(), interval);
            int extend_2 = leaveConfig.getLLValidityMonths(level.getLevel(), empCategory.getEmpCategory(), interval_II);

            int diff = interval_II - interval;

            int entitle_1 = leaveConfig.getLLEntile(level.getLevel(), empCategory.getEmpCategory(), interval);
            int entitle_2 = leaveConfig.getLLEntile(level.getLevel(), empCategory.getEmpCategory(), interval_II);

            //int entitle_6 = 0;
            //int entitle_12 = 0;

            //boolean entitle_sts = false;
/*
            if (level.getLevel().length() > 0 && empCategory.getEmpCategory().length() > 0) {
            entitle_6 = leaveConfig.getLLEntile(level.getLevel(), empCategory.getEmpCategory(), 6 * 12);
            entitle_12 = leaveConfig.getLLEntile(level.getLevel(), empCategory.getEmpCategory(), 8 * 12);
            entitle_sts = true;
            }
             */
            float entitle = entitle_1 + entitle_2;

            Date current_date = new Date();

            String str_date = Formater.formatDate(current_date, "yyyy-MM-dd");

            LLStockManagement insertllStockManagement = new LLStockManagement();

            float taken = entitle - llStockManagement.getQtyResidue();  // selisih dengan residu dengan entitle

            float prev_balance = llStockManagement.getQtyResidue() - entitle; // selisih residu 

            insertllStockManagement.setRecordDate(current_date);

            insertllStockManagement.setEntitledDate(current_date);

            String entitledt = getEntitleDateLLFirst(employee.getCommencingDate(), interval);

            String var_date_exp_I = getDateIntervalMonth(extend_1, Formater.formatDate(entitledt, "yyyy-MM-dd"));

            String entitle_date_2 = getDateIntervalMonth(diff, Formater.formatDate(entitledt, "yyyy-MM-dd"));

            String var_date_exp_II = getDateIntervalMonth(extend_2, Formater.formatDate(entitle_date_2, "yyyy-MM-dd"));

            if (true) {

                if (entitledt != null) {

                    try {
                        String sql;

                        if (taken >= 0) {
                            sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + "=" + entitle + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + "=" + entitle_1 + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + "=" + entitle_2 + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + "=" + 0 + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE] + "= 0 " + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "=" + PstLLStockManagement.LL_STS_AKTIF + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL] + "= 0 " + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " = '" + entitledt + "',"
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " = '" + entitle_date_2 + "',"
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + "=" + taken + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + "= '" + var_date_exp_I + "' , "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + "= '" + var_date_exp_II + "' , "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE_2] + "= '" + str_date + "' , "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE] + "= '" + str_date + "' WHERE "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=" + llStockManagement.getEmployeeId();
                        } else {
                            sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + "=" + entitle + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + "=" + entitle_1 + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + "=" + entitle_2 + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + "=" + prev_balance + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE] + "= 0 " + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "=" + PstLLStockManagement.LL_STS_AKTIF + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL] + "= 0 " + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " = '" + entitledt + "',"
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " = '" + entitle_date_2 + "',"
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + "=" + 0 + ","
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + "= '" + var_date_exp_I + "' , "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + "= '" + var_date_exp_II + "' , "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE_2] + "= '" + str_date + "' , "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE] + "= '" + str_date + "' WHERE "
                                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=" + llStockManagement.getEmployeeId();
                        }

                        DBHandler.execUpdate(sql);

                        System.out.println("---------UPDATE STOCK AKTIF " + llStockManagement.getEmployeeId() + " SUCCESS----------------------");

                    } catch (Exception e) {
                        System.out.println("Exception " + e.toString());
                    }
                }
            }
        }
    }

    public static Vector getEmployeeNotYetHaveStock(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {

            String sql = "SELECT EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " EMP LEFT JOIN "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " INNER JOIN "
                    + PstLevel.TBL_HR_LEVEL + " LEV ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " INNER JOIN " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " CAT ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "= CAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]
                    + " WHERE ( ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " is null OR ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = 0 ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;


            String whereClause = "";
            String strEmpNum = "";

           /* update by satrya 2013-11-22 if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + "=" + srcLeaveAppAlClosing.getEmpNum();

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }*/
            if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(srcLeaveAppAlClosing.getEmpNum());
               // sql = sql + "  ";
                if (vectNum != null && vectNum.size() > 0) {
                    strEmpNum = strEmpNum + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strEmpNum = strEmpNum +" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strEmpNum = strEmpNum + str.trim();
                        }
                    }
                    strEmpNum = strEmpNum + ")";
                }
                
            }
             if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }


            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }


            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }

            }


            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }

            sql = sql + whereClause;

            sql = sql + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE ";

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];


            System.out.println("SQL CLOSING NO STOCK (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            I_Leave leaveConfig = null;
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }

            while (rs.next()) {
                int LoS = (int) DateCalc.dayDifference( srcLeaveAppAlClosing.getEmpCommancingDateStart(), new Date());
                float entitled = leaveConfig.getALEntitleAnualLeave(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]), 
                        rs.getString(PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]), LoS,
                    rs.getDate("EMP."+ PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date());

                if (entitled > 0) {
                    LeaveAlClosingNoStockList leaveAlClosingNoStockList = new LeaveAlClosingNoStockList();

                    leaveAlClosingNoStockList.setEmpId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    leaveAlClosingNoStockList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    leaveAlClosingNoStockList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    leaveAlClosingNoStockList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                    leaveAlClosingNoStockList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                    leaveAlClosingNoStockList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    leaveAlClosingNoStockList.setEntitled(entitled);
                    result.add(leaveAlClosingNoStockList);
                }
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    /*
     * AUTHOR   : ROY A
     * DESC     : UNTUK MENDAPATKAN LIST LL YANG CLOSING
     * 
     */
    public static Vector listLLClosingLLByParameter(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {
        I_Leave leaveConfig = null;
        Vector result = new Vector();

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        int configuration = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];
        int configuration_II = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];

        int diff = configuration_II - configuration;

        DBResultSet dbrs = null;

        try {

            String whereClause = "";
            String strEmpNum = "";

            if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + "='" + srcLeaveAppAlClosing.getEmpNum() + "'";

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }
            }

            String strDivision = "";

            if ((srcLeaveAppAlClosing.getDivisionId() != 0)) {

                strDivision = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=" + srcLeaveAppAlClosing.getDivisionId();

                if (strDivision.length() > 0) {

                    whereClause = whereClause + " AND " + strDivision;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }


            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }


            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }
            }


            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            
            String strPayGroupId = "";

            if ((srcLeaveAppAlClosing.getPayGroupId() != 0)) {

                strPayGroupId = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + "=" + srcLeaveAppAlClosing.getPayGroupId();

                if (strPayGroupId.length() > 0) {

                    whereClause = whereClause + " AND " + strPayGroupId;

                }

            }
            
            String strEnt = "";

            if (srcLeaveAppAlClosing.getPeriodId() != 0 ) {
                Period period = new Period();
                try{
                   period = PstPeriod.fetchExc(srcLeaveAppAlClosing.getPeriodId()); 
                }catch(Exception e){}
                
                String strDateStart = Formater.formatDate(period.getStartDate(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(period.getEndDate(), "yyyy-MM-dd");
                strEnt = strEnt + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strEnt.length() > 0) {

                    whereClause = whereClause + " AND " + strEnt;
                }
            }
            
            
            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }
            
            String sql = "SELECT LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " INNER JOIN " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM  ON LLM."  
                    /**
                     * //update by satrya 2013-02-16
                     *  + " FROM " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                     */
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE "
                    
                     //kondisi bisa diclose (mendapatkan entitle I)
                    + "( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + (srcLeaveAppAlClosing.getStatus()==2?
                      // update by satrya 2014-07-24 " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+")"
                    " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+","+PstLLStockManagement.LL_STS_TAKEN+","+PstLLStockManagement.LL_STS_EXPIRED+","+PstLLStockManagement.LL_STS_INVALID+")"
                    :"="+srcLeaveAppAlClosing.getStatus()) /*update by satrya 2013-10-01 PstLLStockManagement.LL_STS_AKTIF*/ + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " < CURRENT_DATE AND "
                    + " DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL "
                    + configuration + " MONTH) <= CURRENT_DATE " + whereClause + " ) "
                    
                    //kondisi dapat entitle II
                    +" OR ( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + (srcLeaveAppAlClosing.getStatus()==2? 
                    // update by satrya 2014-07-24 " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+")"
                    //" IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+")"
                    " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+","+PstLLStockManagement.LL_STS_TAKEN+","+PstLLStockManagement.LL_STS_EXPIRED+","+PstLLStockManagement.LL_STS_INVALID+")"
                    :"="+srcLeaveAppAlClosing.getStatus()) /*update by satrya 2013-10-01 == PstLLStockManagement.LL_STS_AKTIF*/
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + configuration + " MONTH) > CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + diff + " MONTH) <= CURRENT_DATE " + whereClause + " ) "
                   
                    //kondisi status expired I
                    + " OR ( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + (srcLeaveAppAlClosing.getStatus()==2? 
                    // update by satrya 2014-07-24 " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+")"
                    " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+","+PstLLStockManagement.LL_STS_TAKEN+","+PstLLStockManagement.LL_STS_EXPIRED+","+PstLLStockManagement.LL_STS_INVALID+")"
                    :"="+srcLeaveAppAlClosing.getStatus()) /*update by satrya 2013-10-01 PstLLStockManagement.LL_STS_AKTIF*/
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + configuration + " MONTH) > CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ", INTERVAL " + diff + " MONTH ) != LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + " " + whereClause + " )"
                     
                    //kondis status expired II
                    +" OR ( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + (srcLeaveAppAlClosing.getStatus()==2? 
                    // update by satrya 2014-07-24 " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+")"
                    " IN("+PstLLStockManagement.LL_STS_AKTIF+","+PstLLStockManagement.LL_STS_NOT_AKTIF+","+PstLLStockManagement.LL_STS_TAKEN+","+PstLLStockManagement.LL_STS_EXPIRED+","+PstLLStockManagement.LL_STS_INVALID+")"
                    :"="+srcLeaveAppAlClosing.getStatus()) /*update by satrya 2013-10-01 PstLLStockManagement.LL_STS_AKTIF*/
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= CURRENT_DATE "
                    + " AND DATE_ADD(" + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + configuration + " MONTH) > CURRENT_DATE "
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + " <= CURRENT_DATE " + whereClause + " ) "
                     
                    //order by
                    +" ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE];


            //System.out.println("SQL QUERY: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                LeaveLlClosingList leaveLlClosingList = new LeaveLlClosingList();

                leaveLlClosingList.setLlStockManagementId(rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]));
                leaveLlClosingList.setPrevBalance(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE]));
                leaveLlClosingList.setEntitled(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED]));
                leaveLlClosingList.setQtyUsed(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED]));
                leaveLlClosingList.setOpening(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL]));
                leaveLlClosingList.setQtyResidue(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE]));
                leaveLlClosingList.setLlQty(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY]));
                leaveLlClosingList.setEntitled2(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2]));
                leaveLlClosingList.setExpiredDate(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE]));
                leaveLlClosingList.setExpiredDate2(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2]));
                leaveLlClosingList.setEntitledDate(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]));
                leaveLlClosingList.setEntitleDate2(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2]));
                leaveLlClosingList.setEmpId(rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]));
                leaveLlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveLlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveLlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                Date newComm=rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]);
                newComm.setYear(newComm.getYear()+1);
                
                leaveLlClosingList.setCommancingDate(newComm);
                leaveLlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveLlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

                result.add(leaveLlClosingList);

            }

            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    //Untuk mencari employee yang closing Long leave    
    public static Vector listLLClosingALL() {

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }

        int configuration = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];
        int configuration_II = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];
        int diff = configuration_II - configuration;   //selisi antara config 1 dengan config 2

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + //kondisi bisa diclose (mendapatkan entitle I)
                    " WHERE ( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "=" + PstLLStockManagement.LL_STS_AKTIF + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " < CURRENT_DATE AND "
                    + " DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL "
                    + configuration + " MONTH) <= CURRENT_DATE ) "
                    + //kondisi dapat entitle II
                    " OR ( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "=" + PstLLStockManagement.LL_STS_AKTIF
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + configuration + " MONTH) > CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + diff + " MONTH) <= CURRENT_DATE ) "
                    + //kondisi status expired I
                    " OR ( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + configuration + " MONTH) > CURRENT_DATE "
                    + " AND DATE_ADD(LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ", INTERVAL " + diff + " MONTH ) != LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + ")"
                    + //kondis status expired II
                    " OR ( LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " <= CURRENT_DATE "
                    + " AND DATE_ADD(" + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ", INTERVAL " + configuration + " MONTH) > CURRENT_DATE "
                    + " AND LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + " <= CURRENT_DATE ) "
                    + //order by
                    " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector result = new Vector();
            Date entitle = new Date();

            long employee_id = 0;
            int idx = 0;

            while (rs.next()) {

                if (employee_id != 0 && employee_id != rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID])) {

                    Vector list = getLLAktifNext(employee_id, entitle);

                    for (int j = 0; j < list.size(); j++) {

                        LeaveAlClosingList objleaveAlClosingList = new LeaveAlClosingList();
                        objleaveAlClosingList = (LeaveAlClosingList) list.get(j);
                        result.add(objleaveAlClosingList);

                        idx++;
                    }
                }


                LeaveLlClosingList leaveLlClosingList = new LeaveLlClosingList();

                leaveLlClosingList.setLlStockManagementId(rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]));
                leaveLlClosingList.setPrevBalance(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE]));
                leaveLlClosingList.setEntitled(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED]));
                leaveLlClosingList.setQtyUsed(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED]));
                leaveLlClosingList.setOpening(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL]));
                leaveLlClosingList.setQtyResidue(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE]));
                leaveLlClosingList.setLlQty(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY]));
                leaveLlClosingList.setEntitled2(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2]));
                leaveLlClosingList.setExpiredDate(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE]));
                leaveLlClosingList.setExpiredDate2(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2]));
                leaveLlClosingList.setEntitledDate(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]));
                leaveLlClosingList.setEntitleDate2(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2]));
                leaveLlClosingList.setEmpId(rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]));
                leaveLlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveLlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveLlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                
                 Date newComm=rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]);
                newComm.setYear(newComm.getYear()+1);
                
                leaveLlClosingList.setCommancingDate(newComm);
                
                leaveLlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveLlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

                employee_id = rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]);
                entitle = rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]);

                result.add(leaveLlClosingList);

                idx++;
            }

            LeaveLlClosingList objleaveLlClosingList = new LeaveLlClosingList();
            objleaveLlClosingList = (LeaveLlClosingList) result.get(idx - 1);

            Vector listSt = getLLAktifNext(objleaveLlClosingList.getEmpId(), objleaveLlClosingList.getEntitledDate());

            for (int ji = 0; ji < listSt.size(); ji++) {
                LeaveAlClosingList obj = new LeaveAlClosingList();
                obj = (LeaveAlClosingList) listSt.get(ji);
                result.add(obj);
            }

            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        }
        return null;
    }

    public static String getYear(long employee_id, Date commencing_date, int intervals) {

        String date = Formater.formatDate(commencing_date, "yyyy-MM-dd");

        for (int i = 0; i < 50; i++) {

            String nxt_date = getIntervalYears(date, intervals);

        }

        return null;

    }

    public static String getIntervalYears(String date, int intervals) { //intervals in month

        DBResultSet dbrs = null;

        try {
            String sql = "SELECT DATE_ADD('" + date + "',INTERVAL " + intervals + " MONTH)";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                return rs.getString(1);

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;
    }

    public static Vector getLLAktifNext(long employee_id, Date entitle_date) {

        DBResultSet dbrs = null;

        String str_entitle_date = Formater.formatDate(entitle_date, "yyyy-MM-dd");

        try {

            String sql = "SELECT LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " LLM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=" + employee_id + " AND "
                    + " LLM." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "="
                    + PstLLStockManagement.LL_STS_AKTIF + " AND LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " > '" + str_entitle_date + "'"
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",LLM."
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            Vector result = new Vector();

            while (rs.next()) {

                LeaveLlClosingList leaveLlClosingList = new LeaveLlClosingList();

                leaveLlClosingList.setLlStockManagementId(rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]));
                leaveLlClosingList.setPrevBalance(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE]));
                leaveLlClosingList.setEntitled(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED]));
                leaveLlClosingList.setQtyUsed(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED]));
                leaveLlClosingList.setOpening(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_OPENING_LL]));
                leaveLlClosingList.setQtyResidue(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_RESIDUE]));
                leaveLlClosingList.setLlQty(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY]));
                leaveLlClosingList.setEntitledDate(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]));
                leaveLlClosingList.setEntitleDate2(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2]));
                leaveLlClosingList.setEmpId(rs.getLong(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID]));
                leaveLlClosingList.setEntitled2(rs.getFloat(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2]));
                leaveLlClosingList.setExpiredDate(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE]));
                leaveLlClosingList.setExpiredDate2(rs.getDate(PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2]));
                leaveLlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveLlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveLlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveLlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveLlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

                result.add(leaveLlClosingList);
            }



            return result;

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;

    }

    //Memberikan status untuk LL stock apakah invalid, akan close, atau sedang aktif
    public static int statusDataLLStockManagement(long ll_stock_id, long employee_id, Date commencing_date, int intervals, Date entitle_date, int intervals_2) {

        LLStockManagement llStockManagement = new LLStockManagement();

        try {
            llStockManagement = PstLLStockManagement.fetchExc(ll_stock_id);

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        boolean status_stock = validasiStockAktif(employee_id, commencing_date, intervals);  //true - > ada stock aktif, false - > belum ada stock yang aktif
        String date_aktiv = getStockAktif(commencing_date, intervals); // untuk mendapatkan commencing date yang aktif

        int diff_config = intervals_2 - intervals;

        String str_entitle_date = Formater.formatDate(entitle_date, "yyyy-MM-dd");

        boolean sts_entitle_2 = false;
        boolean sts_exp_1 = false;
        boolean sts_exp_2 = false;

        int count_data = sumLLStock(employee_id);

       I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }
        
        if (llStockManagement.getEntitleDate2() == null && (leaveConfig==null || leaveConfig.getLLShowEntile2())) {
            //update by satrya 2013-10-01
            //if (llStockManagement.getEntitleDate2() == null) {
            sts_entitle_2 = stsStockGetEntitleII(entitle_date, diff_config);
        }

        sts_exp_1 = stsStockGetExpired1(llStockManagement.getExpiredDate());
        Date dt_exp_date = getExpiredDate(llStockManagement.getOID(), PstLlStockExpired.EXPIRED_LL_I); 
        if(dt_exp_date !=null ){
            sts_exp_1 = false;
        } 
        sts_exp_2 = stsStockGetExpired2(llStockManagement.getExpiredDate2());
        Date dt_exp_date2 = getExpiredDate(llStockManagement.getOID(), PstLlStockExpired.EXPIRED_LL_II); 
        if(dt_exp_date2 !=null ){
            sts_exp_2 = false;
        } 
        boolean status_data_ent_2 = stsStockGetEntitle2(ll_stock_id);

        Vector lastLL = LastLLStock(employee_id, intervals);  // Dapat yang lebh kecil atau yang aktif

        long stock_id_last = 0;

        if (lastLL != null) {

            try {

                stock_id_last = Long.parseLong(lastLL.get(0).toString());

            } catch (Exception e) {

                System.out.println("EXCEPTION PARSING LONG " + e.toString());

            }
        }
        if (count_data == 1) {                                              // Jumlah data 1

            if (status_stock) {                                             // Jika sudah ada stock yang aktif                

                if (date_aktiv.equals(str_entitle_date)) {

                    if (sts_entitle_2 && sts_exp_1) {

                        int diference = getDiffBetweenEnt2AndExp1(entitle_date, diff_config, llStockManagement.getExpiredDate());

                        if (diference < 0) {

                            return LL_AKTIF_ENTITLE_2;

                        } else {

                            return LL_AKTIF_EXP_1;   // bila expired 1 dan entitle 1 sama, maka didahulukan exp 1

                        }

                    } else if (sts_entitle_2) {

                        return LL_AKTIF_ENTITLE_2;


                    } else if (llStockManagement.getExpiredDate() != null || llStockManagement.getExpiredDate2() != null) {

                        if (llStockManagement.getExpiredDate() != null) {

                            if (llStockManagement.getExpiredDate2() != null) {

                                if (sts_exp_1 && sts_exp_2) {

                                    int diff = DATEDIFF(llStockManagement.getExpiredDate(), llStockManagement.getExpiredDate2());

                                    if (diff > 0) {

                                        return LL_AKTIF_EXP_2;

                                    } else {

                                        return LL_AKTIF_EXP_1;

                                    }

                                } else if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                } else if (sts_exp_2) {

                                    return LL_AKTIF_EXP_2;

                                } else {

                                    return LL_AKTIF;

                                }

                            } else {

                                if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                } else {
                                    return LL_AKTIF;
                                }
                            }
                        } else if (llStockManagement.getExpiredDate2() != null) {

                            if (llStockManagement.getExpiredDate() != null) {

                                if (sts_exp_1 && sts_exp_2) {

                                    int diff = DATEDIFF(llStockManagement.getExpiredDate(), llStockManagement.getExpiredDate2());

                                    if (diff > 0) {

                                        return LL_AKTIF_EXP_2;

                                    } else {

                                        return LL_AKTIF_EXP_1;

                                    }

                                } else if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                } else if (sts_exp_2) {

                                    return LL_AKTIF_EXP_2;

                                } else {

                                    return LL_AKTIF;

                                }

                            } else {

                                if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                } else {
                                    return LL_AKTIF;
                                }
                            }
                        } else {

                            return LL_AKTIF;

                        }
                    } else {

                        return LL_AKTIF;

                    }

                } else if (ll_stock_id == stock_id_last) {

                    return LL_CLOSE_AKTIF_EXIST;            // Stock hanya akan di close karena sudah ada stock yang valid

                } else {

                    return LL_INVALID;                      // Stock hanya akan diclose karena tidak valid
                }

            } else {                                        // Jika stock yang aktif belum ada

                if (ll_stock_id == stock_id_last) {

                    return LL_CLOSE;                        // Stock akan di close dan create stock baru

                } else {

                    return LL_INVALID;                      // Stock hanya akan diclose karena tidak valid

                }
            }

        } else if (count_data > 1) {                        // jumlah data yang aktif lebih dari 1 buah

            if (status_stock) {                              // Jika sudah ada stock yang aktif                

                if (date_aktiv.equals(str_entitle_date)) {

                    if (sts_entitle_2 && sts_exp_1) {

                        return LL_AKTIF_EXP_1;              // bila expired 1 dan entitle 1 sama, maka didahulukan exp 1

                    } else if (sts_entitle_2) {

                        return LL_AKTIF_ENTITLE_2;

                    } else {

                        if (llStockManagement.getExpiredDate() != null) {

                            if (llStockManagement.getExpiredDate2() != null) {

                                if (sts_exp_1 && sts_exp_2) {

                                    int diff = DATEDIFF(llStockManagement.getExpiredDate(), llStockManagement.getExpiredDate2());

                                    if (diff > 0) {

                                        return LL_AKTIF_EXP_2;

                                    } else {

                                        return LL_AKTIF_EXP_1;

                                    }

                                } else if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                } else if (sts_exp_2) {

                                    return LL_AKTIF_EXP_2;

                                }

                            } else {

                                if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                }
                            }
                        } else if (llStockManagement.getExpiredDate2() != null) {

                            if (llStockManagement.getExpiredDate() != null) {

                                if (sts_exp_1 && sts_exp_2) {

                                    int diff = DATEDIFF(llStockManagement.getExpiredDate(), llStockManagement.getExpiredDate2());

                                    if (diff > 0) {

                                        return LL_AKTIF_EXP_2;

                                    } else {

                                        return LL_AKTIF_EXP_1;

                                    }

                                } else if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                } else if (sts_exp_2) {

                                    return LL_AKTIF_EXP_2;

                                }

                            } else {

                                if (sts_exp_1) {

                                    return LL_AKTIF_EXP_1;

                                }
                            }
                        } else {

                            return LL_AKTIF;

                        }
                    }

                } else {

                    return LL_INVALID;                      // Stock hanya akan diclose karena tidak valid
                }

            } else {                                        // Jika stock yang aktif belum ada

                if (ll_stock_id == stock_id_last) {

                    return LL_CLOSE;                        // Stock akan di close dan create stock baru

                } else {

                    return LL_INVALID;                      // Stock hanya akan diclose karena tidak valid

                }
            }

        }
        return 0;
    }

    public static boolean stsStockGetEntitle2(long stock_id) {

        DBResultSet dbrs = null;
        try {

            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=" + stock_id;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getDate(1) == null || rs.getDate(1).equals("0000-00-00")) {
                    return false;
                } else {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        return false;
    }

    //untuk mencari stock yang expired-1
    public static boolean stsStockGetExpired1(Date expired_1) {

        String exp_date = Formater.formatDate(expired_1, "yyyy-MM-dd");  // get expired date 1

        DBResultSet dbrs = null;
        try {
            String sql = "SELECT '" + exp_date + "' <= CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return false;
    }

    public static boolean stsStockGetExpiredDate1(long stock_id) {

        LLStockManagement llStockManagement = new LLStockManagement();

        try {

            llStockManagement = PstLLStockManagement.fetchExc(stock_id);

        } catch (Exception e) {

            System.out.println("Exceptinon " + e.toString());

        }

        String exp_date = Formater.formatDate(llStockManagement.getExpiredDate(), "yyyy-MM-dd");
        DBResultSet dbrs = null;

        try {
            String sql = "SELECT '" + exp_date + "' <= CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        return false;
    }

    //untuk mencari stock yang expired-2
    public static boolean stsStockGetExpired2(Date expired_2) {

        String exp_date = Formater.formatDate(expired_2, "yyyy-MM-dd");

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT '" + exp_date + "' <= CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return false;
    }

    public static String strStockGetExpired2(long stock_id) {
        LLStockManagement llStockManagement = new LLStockManagement();

        try {
            llStockManagement = PstLLStockManagement.fetchExc(stock_id);
        } catch (Exception e) {
            System.out.println("Exceptinon " + e.toString());
        }
        String exp_date = Formater.formatDate(llStockManagement.getExpiredDate2(), "yyyy-MM-dd");
        DBResultSet dbrs = null;

        try {

            String sql = "SELECT '" + exp_date + "' <= CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String date = rs.getString(1);
                return date;

            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        return null;
    }

    //untuk mencari stock yang mendapatkan entitle ke-2
    public static boolean stsStockGetEntitleII(Date entitle_2, int diff_config) {

        String str_date = Formater.formatDate(entitle_2, "yyyy-MM-dd");
        DBResultSet dbrs = null;
        try {

            String sql = "SELECT DATE_ADD('" + str_date + "',INTERVAL " + diff_config + " MONTH ) <= CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    return true;
                }
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        }

        return false;
    }

    public static int getDiffBetweenEnt2AndExp1(Date entitle_date, int diff_config, Date exp_1) {

        String str_ent = Formater.formatDate(entitle_date, "yyyy-MM-dd");
        String exp_date = Formater.formatDate(exp_1, "yyyy-MM-dd");

        DBResultSet dbrs = null;
        try {

            String sql = "SELECT DATEDIFF(DATE_ADD('" + str_ent + "', INTERVAL " + diff_config + " MONTH ),'" + exp_date + "')";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int result = 0;

            while (rs.next()) {
                result = rs.getInt(1);
            }

            return result;

        } catch (Exception e) {

            System.out.println("Exception " + e.toString());

        } finally {
            DBResultSet.close(dbrs);
        }

        return -1;
    }

    public static String strStockGetEntitleII(Date entitle_2, int diff_config) {

        String str_date = Formater.formatDate(entitle_2, "yyyy-MM-dd");
        DBResultSet dbrs = null;
        try {

            String sql = "SELECT DATE_ADD('" + str_date + "',INTERVAL " + diff_config + " MONTH ) <= CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String date = rs.getString(1);
                return date;

            }

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        }

        return null;
    }

    //untuk mencari stock yang aktif
    public static boolean validasiStockAktif(long employee_id, Date commencing_date, int intervals) {

        String where = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + employee_id + " AND "
                + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF;

        String order = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + ","
                + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE];

        Vector listLlStockManagement = PstLLStockManagement.list(0, 0, where, order);

        String date_aktiv = getStockAktif(commencing_date, intervals);

        for (int idx = 0; idx < listLlStockManagement.size(); idx++) {

            LLStockManagement llStockManagement = (LLStockManagement) listLlStockManagement.get(idx);

            String entDate = Formater.formatDate(llStockManagement.getEntitledDate(), "yyyy-MM-dd");

            if (date_aktiv.equals(entDate)) {
                return true;
            }
        }
        return false;
    }

    public static String getStockAktif(Date commencing_date, int intervals) {

        boolean ll_stock_aktif = false;

        Date nextDate = commencing_date;

        for (int i = 0; i < 100; i++) {

            String str_comn_date = Formater.formatDate(nextDate, "yyyy-MM-dd");

            ll_stock_aktif = StsLlStock(str_comn_date, intervals);

            if (ll_stock_aktif == true) {

                return str_comn_date;

            }

            String nxtdate = getIntervalYears(str_comn_date, intervals);

            nextDate = Formater.formatDate(nxtdate, "yyyy-MM-dd");

        }

        return null;
    }

    public static boolean StsLlStock(String entitle_date, int intervals) {
        DBResultSet dbrs = null;

        try {

            String sql = "SELECT '" + entitle_date + "' <= CURRENT_DATE AND CURRENT_DATE < DATE_ADD('" + entitle_date + "',INTERVAL " + intervals + " MONTH )";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) == 1) {
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

    public static boolean StsLlStockSmallerThanCurrent(String entitle_date, int intervals) {
        DBResultSet dbrs = null;

        try {

            String sql = "SELECT '" + entitle_date + "' < CURRENT_DATE AND DATE_ADD('" + entitle_date + "',INTERVAL " + intervals + " MONTH ) <= CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) == 1) {
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

    public static boolean StsLlStockBiggerThanCurrent(String entitle_date, int interval) {
        DBResultSet dbrs = null;

        try {

            String sql = "SELECT DATE_ADD('" + entitle_date + "',INTERVAL " + interval + " MONTH)  > CURRENT_DATE ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                if (rs.getInt(1) == 1) {
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

    public static int sumLLStock(long employee_id) {

        DBResultSet dbrs = null;

        try {
            String sql = "SELECT COUNT(" + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + ") FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=" + employee_id
                    + " AND " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "="
                    + PstLLStockManagement.LL_STS_AKTIF;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author  Roy Andika
     * @param   employee_id
     * @param   interval
     * @return  
     */
    public static Vector LastLLStock(long employee_id, int interval) {

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {
            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + ","
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=" + employee_id
                    + " AND " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "="
                    + PstLLStockManagement.LL_STS_AKTIF + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]
                    + " < CURRENT_DATE AND DATE_ADD(" + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]
                    + ",INTERVAL " + interval + " MONTH ) <= CURRENT_DATE ORDER BY "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " DESC LIMIT 1";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                result.add("" + rs.getLong(1));
                result.add(rs.getDate(2));
                return result;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return null;
    }

    public static long FirstLLStock(long employee_id) {

        DBResultSet dbrs = null;

        try {
            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + ","
                    + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + "=" + employee_id
                    + " AND " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "="
                    + PstLLStockManagement.LL_STS_AKTIF + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE]
                    + " <= CURRENT_DATE ORDER "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " ASC LIMIT 1";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return rs.getLong(1);
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    /**
     * @Author  Roy A
     * @param   StockId
     * @return  
     */
    public static boolean getApplicationLLNotClose(long StockId) {

        DBResultSet dbrs = null;

        try {
            String sql = "SELECT APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID]
                    + " FROM " + PstLeaveApplication.TBL_LEAVE_APPLICATION + " APP INNER JOIN " + PstLlStockTaken.TBL_HR_LL_STOCK_TAKEN
                    + " ALM ON APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_LEAVE_APPLICATION_ID] + " = ALM."
                    + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LEAVE_APPLICATION_ID] + " WHERE ALM."
                    + PstLlStockTaken.fieldNames[PstLlStockTaken.FLD_LL_STOCK_ID] + " = " + StockId + " AND ( APP."
                    + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_APPROVED
                    + " OR APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_DRAFT
                    + " OR APP." + PstLeaveApplication.fieldNames[PstLeaveApplication.FLD_DOC_STATUS] + " = " + PstLeaveApplication.FLD_STATUS_APPlICATION_TO_BE_APPROVE
                    + " ) ";

            ///System.out.println("SQL get Leave Application active : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return false;
    }

    public static boolean getStockExpired(long stock_id) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + " FROM "
                    + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=" + stock_id + " AND ( "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + " <= CURRENT_DATE OR "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + " <= CURRENT_DATE ) ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                return true;
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return false;
    }

    public static void ProcessClosingLL(Vector ll_closing_selected) {

        for (int i = 0; i < ll_closing_selected.size(); i++) {

            LlClosingSelected llClosingSelected = new LlClosingSelected();

            llClosingSelected = (LlClosingSelected) ll_closing_selected.get(i);

            if (llClosingSelected.getStatusData() == LL_INVALID) {

                ProcessClosingLLInvalid(llClosingSelected.getLlStockId()); // bila stock invalid

            } else if (llClosingSelected.getStatusData() == LL_CLOSE) {

                ProcessClosingLLClose(llClosingSelected.getLlStockId(), llClosingSelected.getEmployeeId(), llClosingSelected.getEntitleDate()); // bila stock close normal

            } else if (llClosingSelected.getStatusData() == LL_CLOSE_AKTIF_EXIST) {

                ProcesCloseLLExist(llClosingSelected.getLlStockId());

            } else if (llClosingSelected.getStatusData() == LL_AKTIF_ENTITLE_2) {

                processClosingEntitle2(llClosingSelected.getLlStockId());

            } else if (llClosingSelected.getStatusData() == LL_AKTIF_EXP_1) {

                processExpired_I(llClosingSelected.getLlStockId(), llClosingSelected.getExtended_value(), llClosingSelected.getIterval_date(), llClosingSelected.getQty_taken(), llClosingSelected.getQty(), llClosingSelected.getExp_date_1(), llClosingSelected.getPrev_balance());

            } else if (llClosingSelected.getStatusData() == LL_AKTIF_EXP_2) {

                processExpired_II(llClosingSelected.getLlStockId(), llClosingSelected.getExtended_value(), llClosingSelected.getIterval_date(), llClosingSelected.getQty_taken(), llClosingSelected.getQty(), llClosingSelected.getExp_date_2(), llClosingSelected.getPrev_balance());

            }
        }
    }

    public static void ProcesCloseLLExist(long stock_id) {

        try {

            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "=" + PstLLStockManagement.LL_STS_INVALID
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=" + stock_id;

            DBHandler.execUpdate(sql);
            System.out.println("------------- UPDATE STOCK LL " + stock_id + " BERHASIL -------------------");
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
    }

    public static Date getExpiredDate(long stock_id, int expired_ll) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE]
                    + " FROM " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED
                    + " WHERE " + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " = " + stock_id
                    + " AND " + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_LL] + " = " + expired_ll
                    + " AND " + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_LAST] + " = " + PstLlStockExpired.LAST_TRUE
                    + " ORDER BY " + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " ASC LIMIT 1";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                Date exp_date = rs.getDate(1);
                return exp_date;

            }

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;

    }

    //untuk memproses expired I
    public static void processExpired_I(long ll_stock_id, float extend_value, int extend_month, float qty_used, float qty, Date expired_1, float prev_balance) {

        LlStockExpired llStockExpired = new LlStockExpired();
        String whereClause = PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " = " + ll_stock_id;
        String order = PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " ASC ";

        float sum_expired = 0;

        Vector lstLlStockExpired = PstLlStockExpired.list(0, 0, whereClause, order);

        Date dt_exp_date = getExpiredDate(ll_stock_id, PstLlStockExpired.EXPIRED_LL_I);    // expired previous

        if (lstLlStockExpired.size() > 0) {

            for (int i = 0; i < lstLlStockExpired.size(); i++) {
                LlStockExpired getLlStockExpired = (LlStockExpired) lstLlStockExpired.get(i);
                sum_expired = sum_expired + getLlStockExpired.getExpiredQty();
            }
        }

        float expired_stock = (prev_balance + qty - qty_used - sum_expired) - extend_value;
        String expired_date = getIntervalYears(Formater.formatDate(expired_1, "yyyy-MM-dd"), extend_month);
        Date dt_expired_date = Formater.formatDate(expired_date, "yyyy-MM-dd");

        /*update stock expired  */
        updateStockExpired(ll_stock_id, PstLlStockExpired.EXPIRED_LL_I);

        /* insert into ll stock expired */
        llStockExpired.setLlStockId(ll_stock_id);
        llStockExpired.setExpiredQty(expired_stock);
        llStockExpired.setExpiredDate(dt_expired_date);
        llStockExpired.setExpiredLL(PstLlStockExpired.EXPIRED_LL_I);
        llStockExpired.setExpiredLast(PstLlStockExpired.LAST_TRUE);

        try {
            System.out.println("------- insert into table expired --------");
            PstLlStockExpired.insertExc(llStockExpired);
        } catch (Exception e) {
            System.out.println("EXCEPTION ::::: " + e.toString());
        }

        /*update stock expired date */
        updateStockExpiredI(ll_stock_id, expired_date);

    }

    public static void updateStockExpiredI(long stock_id, String expired_date) {

        try {

            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + "='" + expired_date + "'"
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=" + stock_id;

            DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("EXCEPTION ::: " + e.toString());
        }

    }

    public static void updateStockExpiredII(long stock_id, String expired_date) {

        try {

            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + "= '" + expired_date + "' "
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=" + stock_id;

            DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    //untuk memproses expired 2
    public static void processExpired_II(long ll_stock_id, float extend_value, int extend_month, float qty_used, float qty, Date expired_date_2, float prev_balance) {

        LlStockExpired llStockExpired = new LlStockExpired();
        String whereClause = PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " = " + ll_stock_id + " AND "
                + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " < CURRENT_DATE ";
        String order = PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_DATE] + " ASC ";
        float sum_expired = 0;
        Date exp_date = new Date();

        Vector lstLlStockExpired = PstLlStockExpired.list(0, 0, whereClause, order);

        int last = lstLlStockExpired.size() - 1;
        if (lstLlStockExpired.size() > 0) {

            for (int i = 0; i < lstLlStockExpired.size(); i++) {

                LlStockExpired getLlStockExpired = (LlStockExpired) lstLlStockExpired.get(i);
                sum_expired = sum_expired + getLlStockExpired.getExpiredQty();

                if (i == last) {
                    exp_date = getLlStockExpired.getExpiredDate();
                }
            }
        } else {
            exp_date = expired_date_2;
        }

        float expired_stock = (prev_balance + qty - qty_used - sum_expired) - extend_value;

        String expired_date = getIntervalYears(Formater.formatDate(expired_date_2, "yyyy-MM-dd"), extend_month);
        Date dt_expired_date = Formater.formatDate(expired_date, "yyyy-MM-dd");

        /*update stock expired  */
        updateStockExpired(ll_stock_id, PstLlStockExpired.EXPIRED_LL_II);

        /* insert into ll stock expired */
        llStockExpired.setLlStockId(ll_stock_id);
        llStockExpired.setExpiredQty(expired_stock);
        llStockExpired.setExpiredDate(dt_expired_date);
        llStockExpired.setExpiredLL(PstLlStockExpired.EXPIRED_LL_II);
        llStockExpired.setExpiredLast(PstLlStockExpired.LAST_TRUE);

        try {
            System.out.println("------- insert into table expired --------");
            PstLlStockExpired.insertExc(llStockExpired);
        } catch (Exception e) {
            System.out.println("EXCEPTION ::::: " + e.toString());
        }

        /*update stock expired date */
        updateStockExpiredII(ll_stock_id, expired_date);

    }

    public static void processClosingEntitle2(long stock_id) {

        LLStockManagement llStockManagement = new LLStockManagement();
        I_Leave leaveConfig = null;

        try {
            llStockManagement = PstLLStockManagement.fetchExc(stock_id);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }

        Employee employee = new Employee();
        Level level = new Level();
        EmpCategory empCategory = new EmpCategory();

        try {
            employee = PstEmployee.fetchExc(llStockManagement.getEmployeeId());
            Date newcomm = employee.getCommencingDate();
            newcomm.setYear(newcomm.getYear()+1);
            employee.setCommencingDate(newcomm);
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.toString());
        }

        try {
            level = PstLevel.fetchExc(employee.getLevelId());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.toString());
        }

        try {
            empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.toString());
        }

        int var_exp_ll_I = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];
        int var_exp_ll_II = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];
        int extend_2 = leaveConfig.getLLValidityMonths(level.getLevel(), empCategory.getEmpCategory(), var_exp_ll_II);
        int var_ll_entitle_II = leaveConfig.getLLEntile(level.getLevel(), empCategory.getEmpCategory(), var_exp_ll_II);
        String date_entitle_2 = getIntervalYears(Formater.formatDate(llStockManagement.getEntitledDate(), "yyyy-MM-dd"), var_exp_ll_II - var_exp_ll_I);
        String expired_date_2 = getIntervalYears(date_entitle_2, extend_2);
        float qty = llStockManagement.getLLQty() + var_ll_entitle_II;

        LlStockExpired llStockExpired = new LlStockExpired();
        llStockExpired.setLlStockId(stock_id);
        llStockExpired.setExpiredDate(Formater.formatDate(expired_date_2, "yyyy-MM-dd"));
        llStockExpired.setExpiredQty(0);
        llStockExpired.setExpiredLL(PstLlStockExpired.EXPIRED_LL_II);
        llStockExpired.setExpiredLast(PstLlStockExpired.LAST_TRUE);

        try {
            System.out.println("-------insert into tabel stock expired-------");
            PstLlStockExpired.insertExc(llStockExpired);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        DBResultSet dbrs = null;

        try {
            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE_2] + "= CURRENT_DATE ,"
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + "=" + var_ll_entitle_II + ","
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + "='" + date_entitle_2 + "',"
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + "='" + expired_date_2 + "',"
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + "=" + qty
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=" + stock_id;

            DBHandler.execUpdate(sql);

            
            String sql2 = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + " = "+ 0 +", "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + " = "+ 0 +", "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + " = "+ 0 +" " 
                    + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "="
                    + llStockManagement.getEmployeeId() +" AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF;

            int ix2 = DBHandler.execUpdate(sql2);
            
            System.out.println("----- UPDATE STOCK LL " + stock_id + " SUKSES ------------");

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    /*update stock expired menjadi not last*/
    public static void updateStockExpired(long stock_id, int exp_ll) {
        try {

            String sql = "UPDATE " + PstLlStockExpired.TBL_HR_LL_STOCK_EXPIRED + " SET "
                    + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_LAST] + " = " + PstLlStockExpired.LAST_FALSE
                    + " WHERE " + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " = " + stock_id
                    + " AND " + PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_EXPIRED_LL] + " = " + exp_ll;

            DBHandler.execUpdate(sql);

        } catch (Exception e) {
            System.out.println("EXCEPTION ::: " + e.toString());
        }
    }

    public static void ProcessClosingLLInvalid(long ll_stock_id) {

        try {

            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "="
                    + PstLLStockManagement.LL_STS_INVALID + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "="
                    + ll_stock_id;

            int ix = DBHandler.execUpdate(sql);

            System.out.println("----- UPDATE STOCK LL " + ll_stock_id + " SUKSES ------------");

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        }
    }

    public static void ProcessClosingLLCloseII(long ll_stock_id, long employee_id, Date entitle_date) {

        LLStockManagement llStockManagement = new LLStockManagement();
        EmpCategory empCategory = new EmpCategory();
        Level level = new Level();

        try {
            llStockManagement = PstLLStockManagement.fetchExc(ll_stock_id);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        int var_exp_ll_II = 0;
        int var_ll_entitle_II = 0;
        String var_date_exp_II;
        int extend_2 = 0;
        String nw_entitle_date = "";

        Employee employee = new Employee();

        try {
            employee = PstEmployee.fetchExc(employee_id);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            level = PstLevel.fetchExc(employee.getLevelId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        //## Untuk hardrock - > setahun dapet LL dua kali

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }

        var_exp_ll_II = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];
        extend_2 = leaveConfig.getLLValidityMonths(level.getLevel(), empCategory.getEmpCategory(), var_exp_ll_II);
        var_ll_entitle_II = leaveConfig.getLLEntile(level.getLevel(), empCategory.getEmpCategory(), var_exp_ll_II);

        String record_date_2 = Formater.formatDate(new Date(), "yyyy-MM-dd");       // record date 2
        String entitle_date_2 = getDateIntervalMonth(var_exp_ll_II, entitle_date);   // entitle date 2        
        var_date_exp_II = getDateIntervalMonth(var_exp_ll_II, Formater.formatDate(nw_entitle_date, "yyyy-MM-dd")); //  expired date 2
        float qty = llStockManagement.getLLQty() + var_ll_entitle_II;                 // qty 2

        try {
            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + " = " + qty + ","
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE_2] + "'" + var_exp_ll_II + "',"
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_RECORD_DATE_2] + "'" + record_date_2 + "',"
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_2] + "=" + var_ll_entitle_II + ","
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE_2] + "'" + entitle_date_2 + "'"
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=" + ll_stock_id;

            int ix = DBHandler.execUpdate(sql);

            System.out.println("----- UPDATE STOCK LL " + ll_stock_id + " SUKSES ------------");

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
    }

    /*untuk close normal */
    public static void ProcessClosingLLClose(long ll_stock_id, long employee_id, Date entitle_date) {

        LLStockManagement llStockManagement = new LLStockManagement();
        EmpCategory empCategory = new EmpCategory();
        Level level = new Level();

        int var_exp_ll_I = 0;
        int var_ll_entitle_I = 0;
        String var_date_exp_I;
        int extend_1 = 0;
        String old_entitle_date;
        String nw_entitle_date;
        long current_stock_id = 0;
        Employee employee = new Employee();

        try {
            employee = PstEmployee.fetchExc(employee_id);
            
            Date newcomm = employee.getCommencingDate();
            newcomm.setYear(newcomm.getYear()+1);
            employee.setCommencingDate(newcomm);
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        try {
            level = PstLevel.fetchExc(employee.getLevelId());
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        //## Untuk hardrock - > setahun dapet LL dua kali

        I_Leave leaveConfig = null;

        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }

        var_exp_ll_I = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];
        nw_entitle_date = getDateIntervalMonth(var_exp_ll_I, entitle_date);
        extend_1 = leaveConfig.getLLValidityMonths(level.getLevel(), empCategory.getEmpCategory(), var_exp_ll_I);
        var_ll_entitle_I = leaveConfig.getLLEntile(level.getLevel(), empCategory.getEmpCategory(), var_exp_ll_I);
        var_date_exp_I = getDateIntervalMonth(extend_1, Formater.formatDate(nw_entitle_date, "yyyy-MM-dd"));

        llStockManagement.setEmployeeId(employee_id);
        llStockManagement.setLeavePeriodeId(0);
        llStockManagement.setLLStatus(PstLLStockManagement.LL_STS_AKTIF);
        llStockManagement.setRecordDate(new Date());
        llStockManagement.setEntitled(var_ll_entitle_I);
        llStockManagement.setLLQty(var_ll_entitle_I);
        llStockManagement.setOpeningLL(0);
        llStockManagement.setQtyUsed(0);
        llStockManagement.setEntitledDate(Formater.formatDate(nw_entitle_date, "yyyy-MM-dd"));
        llStockManagement.setExpiredDate(Formater.formatDate(var_date_exp_I, "yyyy-MM-dd"));

        try {
            /*insert into table stock ll */
            current_stock_id = PstLLStockManagement.insertExc(llStockManagement);
        } catch (Exception e) {
            System.out.println("Exception ::::: " + e.getMessage());
        }

        //LlStockExpired llStockExpired = new LlStockExpired();
        //llStockExpired.setLlStockId(current_stock_id);
        //llStockExpired.setExpiredDate(Formater.formatDate(var_date_exp_I, "yyyy-MM-dd"));
        //llStockExpired.setExpiredQty(0);
        //llStockExpired.setExpiredLL(PstLlStockExpired.EXPIRED_LL_I);
        //llStockExpired.setExpiredLast(PstLlStockExpired.LAST_TRUE);

        //try {
            /*insert into table stock ll expired */
        //    PstLlStockExpired.insertExc(llStockExpired);
        //} catch (Exception e) {
        //    System.out.println("EXCEPTION :::: " + e.toString());
        //}

        try {

            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + "="
                    + PstLLStockManagement.LL_STS_NOT_AKTIF + " WHERE "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "="
                    + ll_stock_id;

            int ix = DBHandler.execUpdate(sql);

            
            
            String sql2 = "UPDATE " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " SET "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + " = "+ 0 +", "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + " = "+ 0 +", "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + " = "+ 0 +" " 
                    + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "="
                    + employee_id +" AND "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF;

            int ix2 = DBHandler.execUpdate(sql2);
            
            System.out.println("----- UPDATE STOCK LL " + ll_stock_id + " SUKSES ------------");

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        }
    }

    public static void ProcessClosingExpired(long stock_id, Date exp_date, Date exp_date_2, int var_extended_value, int interval) {

        int diff = DATEDIFF(exp_date, exp_date_2);

        String str_exp_date_1 = getDateIntervalMonth(interval, exp_date);
        String str_exp_date_2 = getDateIntervalMonth(interval, exp_date_2);

        if (diff == 0) {          // exp date = exp date 2

            String cond_exp_date = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + " = '" + str_exp_date_1 + "'";

            updatePrevPeriod(stock_id, var_extended_value, cond_exp_date);

        } else if (diff > 0) {     // exp date > exp date 2

            String cond_exp_date = " " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + " = '" + str_exp_date_2 + "' ";

            updatePrevPeriod(stock_id, var_extended_value, cond_exp_date);

        } else if (diff < 0) {     // exp date < exp date 2

            String cond_exp_date = " " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EXPIRED_DATE] + " = '" + str_exp_date_1 + "' ";

            updatePrevPeriod(stock_id, var_extended_value, cond_exp_date);

        }

    }

    public static void updatePrevPeriod(long stock_id, int prev_period, String setExpDate) {

        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " SET "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + " = "
                    + prev_period + "," + setExpDate + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "="
                    + stock_id;

            int ix = DBHandler.execUpdate(sql);

            System.out.println("----- UPDATE STOCK LL " + stock_id + " SUKSES ------------");

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

    }

    public static int DATEDIFF(Date exp_date_1, Date exp_date_2) {

        String str_date_1 = Formater.formatDate(exp_date_1, "yyyy-MM-dd");
        String str_date_2 = Formater.formatDate(exp_date_2, "yyyy-MM-dd");
        DBResultSet dbrs = null;

        try {

            String sql = "SELECT DATEDIFF('" + str_date_1 + "','" + str_date_2 + "')";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                int diff = rs.getInt(1);
                return diff;
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION :::: " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    public static int getStsLeave() {

        DBResultSet dbrs = null;

        try {

            String sql = "";

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;

    }

    public static float getSumExpired(long stock_id) {

        String where = PstLlStockExpired.fieldNames[PstLlStockExpired.FLD_LL_STOCK_ID] + " = " + stock_id;
        Vector list = PstLlStockExpired.list(0, 0, where, null);
        float sum = 0;

        for (int i = 0; i < list.size(); i++) {

            LlStockExpired llStockExpired = (LlStockExpired) list.get(i);
            sum = sum + llStockExpired.getExpiredQty();

        }

        return sum;
    }

    public static long getLLStock_Id_First(long employee_id) {

        DBResultSet dbrs = null;
        try {
            String sql = "SELECT " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID]
                    + " FROM " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT
                    + " WHERE " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = "
                    + employee_id + " AND "
                    + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = "
                    + PstLLStockManagement.LL_STS_AKTIF
                    + " ORDER BY " + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_ENTITLED_DATE] + " ASC "
                    + " LIMIT 1 ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                long diff = rs.getLong(1);
                return diff;
            }

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return 0;

    }

    /**
     * @Desc    : Untuk mengupdate Entitle date stock LL yang aktif
     */
    public static void updateEntitleDateLL(long employeeId, Date oldCommencingDate, Date NewCommencingDate) {

        String where = PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId + " AND "
                + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = " + PstLLStockManagement.LL_STS_AKTIF;

        Vector vectLLStock = PstLLStockManagement.list(0, 0, where, null);

        LLStockManagement llStockManagement = new LLStockManagement();

        if (vectLLStock != null && vectLLStock.size() > 0) {

            llStockManagement = (LLStockManagement) vectLLStock.get(0);

        }

        if (oldCommencingDate.getTime() < NewCommencingDate.getTime() || oldCommencingDate.getTime() > NewCommencingDate.getTime()) {

            int diferentDate = DATEDIFF(NewCommencingDate, oldCommencingDate);

            String newEntitleDate = "";
            String newEntitleDate_2 = "";
            String newExpiredDate = "";
            String newExpiredDate_2 = "";

            if (llStockManagement.getEntitledDate() != null) {

                newEntitleDate = DATE_ADD(llStockManagement.getEntitledDate(), diferentDate, 1);
                llStockManagement.setEntitledDate(Formater.formatDate(newEntitleDate, "yyyy-MM-dd"));

            }

            if (llStockManagement.getEntitleDate2() != null) {

                newEntitleDate_2 = DATE_ADD(llStockManagement.getEntitleDate2(), diferentDate, 1);
                llStockManagement.setEntitleDate2(Formater.formatDate(newEntitleDate_2, "yyyy-MM-dd"));

            }

            if (llStockManagement.getExpiredDate() != null) {

                newExpiredDate = DATE_ADD(llStockManagement.getExpiredDate(), diferentDate, 1);
                llStockManagement.setExpiredDate(Formater.formatDate(newExpiredDate, "yyyy-MM-dd"));

            }

            if (llStockManagement.getExpiredDate2() != null) {

                newExpiredDate = DATE_ADD(llStockManagement.getExpiredDate2(), diferentDate, 1);
                llStockManagement.setExpiredDate2(Formater.formatDate(newExpiredDate_2, "yyyy-MM-dd"));

            }

            try {
                PstLLStockManagement.updateExc(llStockManagement);
            } catch (Exception e) {
                System.out.println("EXCEPTION " + e.toString());
            }
        }
    }

    public static String DATE_ADD(Date dateOld, int interval, int typeInterval) {

        String strTypeInterval = "";
        String strDateOld = Formater.formatDate(dateOld, "yyyy-MM-dd");

        if (typeInterval == INTERVAL_DAY) {

            strTypeInterval = "DAY";

        } else if (typeInterval == INTERVAL_MONTH) {

            strTypeInterval = "MONTH";

        } else if (typeInterval == INTERVAL_YEAR) {

            strTypeInterval = "YEAR";

        } else {

            strTypeInterval = "DAY";

        }

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT DATE_ADD('" + strDateOld + "',INTERVAL " + interval + " " + strTypeInterval + " ) ";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                String dateNew = rs.getString(1);
                return dateNew;
            }

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan al closing periode yang belum mempunyai entitle by periode.
     * @Desc    Untuk beberapa kasus, seperti di NIKKO dan INTIMAS
     * @Desc    untuk masing-masing periode
     */
    public static Vector getEmployeeActiveByPeriod(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {

        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs;
        Vector result = new Vector();

        try {

            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="
                    + PstEmployee.NO_RESIGN + " AND ( DATE_ADD(ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) <= CURRENT_DATE ) ";

            String whereClause = "";
            String strEmpNum = "";
            
             if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(srcLeaveAppAlClosing.getEmpNum());
               // sql = sql + "  ";
                if (vectNum != null && vectNum.size() > 0) {
                    strEmpNum = strEmpNum + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strEmpNum = strEmpNum +" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strEmpNum = strEmpNum + str.trim();
                        }
                    }
                    strEmpNum = strEmpNum + ")";
                }
                
            }
             if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            /* update by satrya 2013-11-22 if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " =" + srcLeaveAppAlClosing.getEmpNum();

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }*/

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }

            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }

            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }

            }

            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }

            sql = sql + whereClause;

            sql = sql + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE ";

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            System.out.println("SQL LEAVE CLOSING PERIOD (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                /* Untuk mendapatkan employee yang mesti closing berdasarkan periode */

                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();

                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                result.add(leaveAlClosingList);
            }
            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan al closing periode yang belum mempunyai entitle by periode.
     * @Desc    Untuk beberapa kasus, seperti di NIKKO dan INTIMAS
     * @Desc    untuk masing-masing periode
     */
    public static Vector getEmployeeActiveByPeriod2(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {

        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs;
        Vector result = new Vector();

        try {

            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="
                    + PstEmployee.NO_RESIGN;

            String whereClause = "";
            String strEmpNum = "";

            /* update by satrya 2013-11-22 if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + "=" + srcLeaveAppAlClosing.getEmpNum();

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }*/
             if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(srcLeaveAppAlClosing.getEmpNum());
               // sql = sql + "  ";
                if (vectNum != null && vectNum.size() > 0) {
                    strEmpNum = strEmpNum + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strEmpNum = strEmpNum +" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strEmpNum = strEmpNum + str.trim();
                        }
                    }
                    strEmpNum = strEmpNum + ")";
                }
                
            }
             if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }

            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }

            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }

            }

            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }

            sql = sql + whereClause;

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            //System.out.println("SQL LEAVE CLOSING PERIOD (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                /* Untuk mendapatkan employee yang mesti closing berdasarkan periode */

                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();

                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                result.add(leaveAlClosingList);
            }
            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan employee yang belum mempunyai stock by period
     * @param   srcLeaveAppAlClosing
     * @return
     */
    public static Vector getEmployeeNotYetHaveStockByPeriod(SrcLeaveAppAlClosing srcLeaveAppAlClosing,I_Leave leaveConfig) {

        DBResultSet dbrs = null;
        Vector result = new Vector();


        try {

            String sql = "SELECT EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " EMP LEFT JOIN "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " INNER JOIN "
                    + PstLevel.TBL_HR_LEVEL + " LEV ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " INNER JOIN " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " CAT ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "= CAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]
                    + " WHERE ( ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " is null OR ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = 0 ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN+ " AND DATE_ADD(EMP."
                    +PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+", INTERVAL "+leaveConfig.getMinimalWorkAL()+" MONTH ) <= '"
                    +Formater.formatDate(new Date(),"yyyy-MM-dd")+"'";


            String whereClause = "";
            String strEmpNum = "";

            /*if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE '%" + srcLeaveAppAlClosing.getEmpNum()+"%'";

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }*/
            if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(srcLeaveAppAlClosing.getEmpNum());
               // sql = sql + "  ";
                if (vectNum != null && vectNum.size() > 0) {
                    strEmpNum = strEmpNum + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strEmpNum = strEmpNum +" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strEmpNum = strEmpNum + str.trim();
                        }
                    }
                    strEmpNum = strEmpNum + ")";
                }
                
            }
             if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }


            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }


            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }

            }


            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }

            sql = sql + whereClause;

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];


            //System.out.println("SQL CLOSING NO STOCK (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                int LoS = (int) DateCalc.dayDifference(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date());
                        
                float entitled = leaveConfig.getALEntitleAnualLeave(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]), 
                        rs.getString(PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]), LoS,
                        rs.getDate("EMP."+ PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date());

                if (entitled > 0) {
                    LeaveAlClosingNoStockList leaveAlClosingNoStockList = new LeaveAlClosingNoStockList();

                    leaveAlClosingNoStockList.setEmpId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    leaveAlClosingNoStockList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    leaveAlClosingNoStockList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    leaveAlClosingNoStockList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                    leaveAlClosingNoStockList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                    leaveAlClosingNoStockList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    
                    
                    leaveAlClosingNoStockList.setEntitled(entitled);
                    result.add(leaveAlClosingNoStockList);
                }
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    public static Vector getEmployeeActiveAllByPeriod(SrcLeaveAppAlClosing srcLeaveAppAlClosing) {

        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs;
        Vector result = new Vector();

        try {

            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON " + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = "
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND DATE_ADD(ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) < CURRENT_DATE "
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];


            System.out.println("SQL Leave Application Closing all by period : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Employee employee = new Employee();
                AlStockManagement alStockManagement = new AlStockManagement();
                Department department = new Department();
                Vector temp = new Vector();

                alStockManagement.setOID(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                alStockManagement.setEmployeeId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                alStockManagement.setRecordDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE]));
                alStockManagement.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                alStockManagement.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                alStockManagement.setEntitleDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                alStockManagement.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                alStockManagement.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                alStockManagement.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                alStockManagement.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                temp.add(alStockManagement);

                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setCommencingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                temp.add(employee);

                department.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                temp.add(department);

                result.add(temp);
            }

            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    public static Vector getEmployeeNotYetHaveStockAllByPeriod() {

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {

            String sql = "SELECT EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " EMP LEFT JOIN "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " INNER JOIN "
                    + PstLevel.TBL_HR_LEVEL + " LEV ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " INNER JOIN " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " CAT ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "= CAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]
                    + " WHERE ( ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " is null OR ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = 0 ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN + " AND ( EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != '0000-00-00' OR EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != null ) "
                    + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE "
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            System.out.println("Sql closing not have stock by period : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            I_Leave leaveConfig = null;
            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }

            while (rs.next()) {
                int LoS = (int) DateCalc.dayDifference(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date());
                float entitled = leaveConfig.getALEntitleAnualLeave(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]), 
                        rs.getString(PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]), LoS,
                        rs.getDate("EMP."+ PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date());

                if (entitled > 0) {

                    LeaveAlClosingNoStockList leaveAlClosingNoStockList = new LeaveAlClosingNoStockList();

                    leaveAlClosingNoStockList.setEmpId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    leaveAlClosingNoStockList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    leaveAlClosingNoStockList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    leaveAlClosingNoStockList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                    leaveAlClosingNoStockList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                    leaveAlClosingNoStockList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    leaveAlClosingNoStockList.setEntitled(entitled);
                    result.add(leaveAlClosingNoStockList);
                }
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan employee yang mesti closing by period     
     * @return  Employee must closing 
     */
    public static Vector getEmployeeActiveALLByPeriod() {

        DBResultSet dbrs;
        Vector result = new Vector();
        Date dtNow = new Date();
        String strDtNow = Formater.formatDate(dtNow, "yyyy-MM-dd");

        try {

            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="
                    + PstEmployee.NO_RESIGN + " AND ( DATE_ADD(ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) <= '" + strDtNow + "' ) AND ( EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != '0000-00-00' OR EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " != null ) "
                    + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE "
                    + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            System.out.println("Sql leave closing period ( NOT BY SEARCHING ) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long employee_id = 0;
            Date entitle_date = new Date();
            int idx = 0;

            while (rs.next()) {

                if (employee_id != 0 && employee_id != rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID])) {
                    Vector list = getEmployeeActiveNextAll(employee_id, entitle_date);

                    for (int j = 0; j < list.size(); j++) {

                        LeaveAlClosingList objleaveAlClosingList = new LeaveAlClosingList();
                        objleaveAlClosingList = (LeaveAlClosingList) list.get(j);
                        result.add(objleaveAlClosingList);

                        idx++;
                    }
                }

                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();
//update by satrya 2012-11-08
                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));

                employee_id = rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]);
                entitle_date = rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]);

                result.add(leaveAlClosingList);

                idx++;
            }

            LeaveAlClosingList objleaveAlClosingList = new LeaveAlClosingList();
            objleaveAlClosingList = (LeaveAlClosingList) result.get(idx - 1);

            Vector listSt = getEmployeeActiveNextAll(objleaveAlClosingList.getEmpId(), objleaveAlClosingList.getEntitledDate());

            for (int ji = 0; ji < listSt.size(); ji++) {
                LeaveAlClosingList obj = new LeaveAlClosingList();
                obj = (LeaveAlClosingList) listSt.get(ji);
                result.add(obj);
            }

            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }
        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatakan Priode start dan end bila menggunakan periode
     * @param   commancingDate, dtStart
     * @return
     */
    public static Date getPeriode(Date commancingDate, int dtStart) {

        int tgl = commancingDate.getDate();
        int mont = commancingDate.getMonth() + 1;
        int year = commancingDate.getYear() + 1900;

        String date = "";

        if (tgl >= 1 && tgl < dtStart) { //1 periode mulai dari tgl 26 sampai 25

            int tglPeriode = dtStart;
            int monPeriode = mont;
            int yearPeriode = year;

            date = "" + yearPeriode + "-" + monPeriode + "-" + tglPeriode;

        } else if (tgl >= dtStart) {

            int tglPeriode = dtStart;
            int monthPeriod;
            int yearPeriode;

            if (mont == 12) { /* Bila sudah bulan desember, maka bulan kembali ke januari dan tahun bertambah 1 */

                monthPeriod = 1;
                yearPeriode = year + 1;

            } else {

                monthPeriod = mont + 1;
                yearPeriode = year;
            }
            date = "" + yearPeriode + "-" + monthPeriod + "-" + tglPeriode;
        }

        try {
            Date tmp = Formater.formatDate(date, "yyyy-MM-dd");
            return tmp;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }
    
    /**
     * @Author Roy Andika
     * @param employeeId
     * @Desc Untuk mendapatkan entitle al setelah comencing date (setelah 1 tahun bekerja)
     * @return
     */
    public static float getEntitleEmployee(Date commancingDate, String level, String empCategory, I_Leave leaveConfig) {

        if (commancingDate == null) {
            return 0;
        }

        Date datePeriod = leaveConfig.getPeriode(commancingDate, leaveConfig.getDatePeriod());
        int LoS = (int) DateCalc.dayDifference(commancingDate , datePeriod);
        float monthPeriod = leaveConfig.getALEntitleAnualLeave(level, empCategory, LoS, commancingDate, new Date() );
        //int monthPeriod = leaveConfig.getALEntitleAnualLeave(level, empCategory, LoS, commancingDate, new Date() );

        int date = 0;

        date = datePeriod.getMonth() + 1; //januari nilainya 0 makanya harus ditambah 1 

        float entitle = 0;

        if (date == 1) { //januari -> 26 desember to 25 januari

            entitle = monthPeriod - 1;

        } else if (date == 2) { // februari -> periode 26 januari sampai 25 januari

            entitle = monthPeriod - 2;

        } else if (date == 3) { // maret

            entitle = monthPeriod - 3;

        } else if (date == 4) { // april

            entitle = monthPeriod - 4;

        } else if (date == 5) { // mei

            entitle = monthPeriod - 5;

        } else if (date == 6) { // juni

            entitle = monthPeriod - 6;

        } else if (date == 7) { // juli

            entitle = monthPeriod - 7;

        } else if (date == 8) { // agustus

            entitle = monthPeriod - 8;

        } else if (date == 9) { // september

            entitle = monthPeriod - 9;

        } else if (date == 10) { // oktober

            entitle = monthPeriod - 10;

        } else if (date == 11) { // november

            entitle = monthPeriod - 11;

        } else if (date == 12) { // desember - > periode 26 desember to 25 januari

            entitle = monthPeriod;

        }

        /* Tujuannya untuk mengecek apakah berhak dapat entitle atau tidak */
        return entitle; 

    }

    /**
     * @Author Roy Andika
     * @return status Annual leave
     */
    public static long getStatusAl(long employeeId) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstAlStockManagement.fieldStatus[PstAlStockManagement.FLD_AL_STOCK_ID] + " FROM "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employeeId;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            long alStockId = 0;
            while (rs.next()) {
                alStockId = rs.getLong(1);
            }

            return alStockId;

        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }

        return 0;
    }

    /**
     * @Author  Roy Andika
     * @Desc
     * @param   yer
     * @param   dtPeriod
     * @return
     */
    public static Date getPeriodeClosing(Date yer, int dtPeriod) {

        Date cmDate = SessLeaveApplication.DATE_ADD(yer, 12);

        int diff = 0;

        diff = DATEDIFF(cmDate, new Date());

        if (diff > 0) { //jika belum satu tahun, maka entitle yang digunakan adalah commencing date.

            return yer;

        } else {

            int date = cmDate.getDate();
            int month = cmDate.getMonth() + 1;
            int year = cmDate.getYear() + 1900;

            int dateP = 0;
            int mnthP = 0;
            int yerP = 0;

            if (date < dtPeriod) {

                dateP = dtPeriod;
                mnthP = month;
                yerP = year;

            } else {
                dateP = dtPeriod;
                if (month == 12) {
                    mnthP = 1;
                    yerP = year + 1;
                } else {
                    mnthP = month + 1;
                    yerP = year;
                }
            }

            String tmp = "" + yerP + "-" + mnthP + "-" + dateP;
            Date perDate = new Date();

            try {

                perDate = Formater.formatDate(tmp, "yyyy-MM-dd");
                return perDate;

            } catch (Exception e) {
                System.out.println("Exception " + e.toString());
            }
        }

        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk memproses closing AL yang menggunakan periode (etc untuk NIKKO )
     * @param   VleaveAlClosing
     */
    public static void ProcessClosingPeriod(Vector VleaveAlClosing, I_Leave leaveConfig){

        if (VleaveAlClosing != null && VleaveAlClosing.size() > 0) {

            for (int i = 0; i < VleaveAlClosing.size(); i++){
                try{
                Thread.sleep(100L);
                } catch(Exception exc){
                    System.out.println(exc);
                }
                LeaveAlClosing leaveAlClosing = new LeaveAlClosing();
                leaveAlClosing = (LeaveAlClosing) VleaveAlClosing.get(i);
                //closed by Kar karena sepertinya tidak terpakai, return value tidak di tampung
                //getEntitleEmployeeFirst(Formater.formatDate(leaveAlClosing.getCommencingDate(), "yyyy-MM-dd"), leaveConfig);

                int countData = 0;

                try {
                    /* Untuk mendapatkan jumlah data stock yang aktif dan tidak aktif berdasarkan employee yang diinginkan */
                    countData = countAlStock(leaveAlClosing.getEmployeeId());
                } catch (Exception e) {
                    System.out.println("[exception] " + e.toString());
                }

                Employee employee = new Employee();

                try {
                    employee = PstEmployee.fetchExc(leaveAlClosing.getEmployeeId());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }

                Level level = new Level();

                try {
                    level = PstLevel.fetchExc(employee.getLevelId());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }

                EmpCategory empCategory = new EmpCategory();

                try {
                    empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }

                //int datePeriod = leaveConfig.getDatePeriod();
                //Date periodeDT = new Date();
                //periodeDT = getPeriodeClosing(employee.getCommencingDate(), datePeriod, leaveConfig);
                //update by satrya 2013-1-04
                 AlUpload alUpload = new AlUpload();
                if (countData == 0){ // jika stock aktif dan tidak aktif belum ada

                    float entitle = 0;
                    int LoS = (int) DateCalc.dayDifference(employee.getCommencingDate(), new Date());
                    
                  
                    float tmpentitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(),new Date());
                    //upsdate by satrya 2013-11-09 int tmpentitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(),new Date());

                     /*Untuk mendapatkan leave closing pertama */
                    Date tmpEntitleDt = SessLeaveApplication.getEntitleFirstClosing(employee.getCommencingDate(), leaveConfig);
                    
                    if (tmpEntitleDt != null && employee.getCommencingDate()!=null){

                        if (tmpentitle == 0) { /* Jika entitle yang sebenarnya yang harus dia dapatkan adalah 0 */

                            entitle = 0;

                        } else {

                            entitle = leaveConfig.getQtyEntitle(tmpEntitleDt, level.getLevel(), empCategory.getEmpCategory(), employee.getCommencingDate());
                            //entitle = getEntitleEmployee(employee.getCommencingDate(),level.getLevel(),empCategory.getEmpCategory(), leaveConfig);
                        }
                        //update by satrya 2013-11-22
                        float entitleAlExtra = 0;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraAl(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(), new Date());
                        Date dtEntitleAlExtra = null;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraDateAl(entitleAlExtra, employee.getCommencingDate());
                        
                        AlStockManagement alStockManagement = new AlStockManagement();
                        alStockManagement.setLeavePeriodeId(0);
                        alStockManagement.setEmployeeId(leaveAlClosing.getEmployeeId());
                        alStockManagement.setEntitleDate(tmpEntitleDt);
                        //alStockManagement.setEntitleDate(periodeDT);
                        alStockManagement.setOpening(0);
                        //alStockManagement.setEntitled(entitle);
                        alStockManagement.setEntitled(entitle ); //priska menambahkan auto 12
                        alStockManagement.setAlQty(entitle); 
                        alStockManagement.setQtyUsed(0);
                        alStockManagement.setPrevBalance(0);
                        alStockManagement.setStNote("");
                        alStockManagement.setRecordDate(new Date());
                        alStockManagement.setAlStatus(PstAlStockManagement.AL_STS_AKTIF);
                        //update by satrya 2013-11-22
                        alStockManagement.setExtraAl(entitleAlExtra);
                        alStockManagement.setExtraAlDate(dtEntitleAlExtra); 
                        try {
                            PstAlStockManagement.insertExc(alStockManagement);
                            //karena AL sudah closing, maka update data opname status menjadi 1
                           
                            alUpload.setEmployeeId(leaveAlClosing.getEmployeeId());
                            if(leaveAlClosing.getEmployeeId()!=0){
                                PstAlUpload.updateDataStatusByEmpId(alUpload.getEmployeeId()); 
                            }
                        } catch (Exception e) {
                            System.out.println("[exception] " + e.toString());
                        }
                   
                    }

                }else{
                    int maximumAlTakenLeave = 0;
                    try{
                         maximumAlTakenLeave = Integer.parseInt(PstSystemProperty.getValueByName("LEAVE_MAKSIMUM_AL_TAKEN_LEAVE"));
                    }catch(Exception exc){
                        System.out.println("Exc LEAVE_MAKSIMUM_AL_TAKEN_LEAVE"+exc);
                    }
                    Date entDtClosing = Formater.formatDate(leaveAlClosing.getEntitledDate(), "yyyy-MM-dd");
                    int LoS = (int) DateCalc.dayDifference(employee.getCommencingDate(), entDtClosing);
                    
                    float tmpentitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(), new Date());

                    Date tmpDtClosing = SessLeaveApplication.getEntitle(entDtClosing,leaveConfig); //mengambil date closing ex: 31 desember 2015
                    Date dateEntile = new Date(tmpDtClosing.getTime()+(24*60*60*1000)); //membuat date entitle yang baru ex: 1 januari 2015
                    float entitle = 0;
                    
                     //update by satrya 2013-11-22
                    float entitleAlExtra = 0;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraAl(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(), new Date());
                    Date dtEntitleAlExtra =  null;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraDateAl(entitleAlExtra, employee.getCommencingDate());
                        
                    if(tmpDtClosing != null){

                        /*if (tmpentitle == 0){ // Jika entitle yang sebenarnya yang harus dia dapatkan adalah 0 

                            entitle = 0;

                        } else{

                            entitle = leaveConfig.getQtyEntitle(tmpDtClosing);
                            
                        }*/

                        AlStockManagement alStockManagement = new AlStockManagement();

                        String expired = getDateIntervalMonth(leaveAlClosing.getExpiredDate(), tmpDtClosing);
                        alStockManagement.setDtOwningDate(dateEntile);
                        alStockManagement.setLeavePeriodeId(0);
                        alStockManagement.setEmployeeId(leaveAlClosing.getEmployeeId());
                        alStockManagement.setOpening(0);
                        alStockManagement.setPrevBalance(leaveAlClosing.getExtended());
                        //update by satrya 2014-06-10
                        //karena konfigurasinya, jika melebihi dari 24 maka akan di prev'nya akan di kurangi berapa maksimum prev yg didapat
                        /*if(maximumAlTakenLeave>0){
                            alStockManagement.setPrevBalance(leaveAlClosing.getExtended() + tmpentitle>maximumAlTakenLeave?(maximumAlTakenLeave-tmpentitle):leaveAlClosing.getExtended());
                        }else{
                           alStockManagement.setPrevBalance(leaveAlClosing.getExtended());
                        }*/
                        
                        alStockManagement.setEntitled(tmpentitle);//entitle);
                        //update by satrya 2014-01-18 alStockManagement.setAlQty(leaveAlClosing.getExtended() + tmpentitle);//update by satrya 2013-10-28 tmpentitle //entitle);
                        alStockManagement.setAlQty(alStockManagement.getPrevBalance() + tmpentitle);//update by satrya 2013-10-28 tmpentitle //entitle);
                        
                        alStockManagement.setQtyUsed(0);
                        alStockManagement.setAlStatus(PstAlStockManagement.AL_STS_AKTIF);
                        alStockManagement.setStNote("");
                        
                        alStockManagement.setRecordDate(new Date());
                        alStockManagement.setEntitleDate(dateEntile);//tmpDtClosing);
                        alStockManagement.setExpiredDate(Formater.formatDate(expired, "yyyy-MM-dd"));

                         //update by satrya 2013-11-22
                        alStockManagement.setExtraAl(entitleAlExtra);
                        alStockManagement.setExtraAlDate(dtEntitleAlExtra); 
                        try {

                            PstAlStockManagement.insertExc(alStockManagement);
                            
                             alUpload.setEmployeeId(leaveAlClosing.getEmployeeId());
                             //update by satrya 2013-1-04
                            if(leaveAlClosing.getEmployeeId()!=0){
                                PstAlUpload.updateDataStatusByEmpId(alUpload.getEmployeeId()); 
                            }
                        } catch (Exception e) {
                            System.out.println("[exception] " + e.toString());
                        }

                        AlStockManagement objAlStockManagement = new AlStockManagement();

                        try {
                            objAlStockManagement = PstAlStockManagement.fetchExc(leaveAlClosing.getStockId());
                        } catch (Exception e) {
                            System.out.println("[exception] :: " + e.toString());
                        }

                        objAlStockManagement.setAlStatus(PstAlStockManagement.AL_STS_NOT_AKTIF);

                        /* Untuk mengupdate status al stock management dari aktif ke not aktif*/
                        try {
                            PstAlStockManagement.updateExc(objAlStockManagement);
                        } catch (Exception e) {
                            System.out.println("[exception] :: " + e.toString());
                        }
                    }
                }
            }
        }
    }

    
     /**
     * @Author  Roy Andika
     * @Desc    Untuk memproses closing AL yang menggunakan periode (etc untuk NIKKO )
     * @param   VleaveAlClosing
     */
    public static void ProcessClosingPeriodAndComencing(Vector VleaveAlClosing, I_Leave leaveConfig){

        if (VleaveAlClosing != null && VleaveAlClosing.size() > 0) {

            for (int i = 0; i < VleaveAlClosing.size(); i++){
                
                try{
                Thread.sleep(100L);
                } catch(Exception exc){
                    System.out.println(exc);
                }
                
                LeaveAlClosing leaveAlClosing = new LeaveAlClosing();
                leaveAlClosing = (LeaveAlClosing) VleaveAlClosing.get(i);
                
                //jika ada stok maka perhitunganya seperti tahun karna comencing hanya diawal
                boolean adaStok = PstAlStockManagement.CekAdaDataStok(""+PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]+" = "+leaveAlClosing.getEmployeeId());
                if (leaveConfig.getALEntitleBy() == I_Leave.AL_ENTITLE_BY_COMMENCING_PERIODE && adaStok) {
                
                //closed by Kar karena sepertinya tidak terpakai, return value tidak di tampung
                //getEntitleEmployeeFirst(Formater.formatDate(leaveAlClosing.getCommencingDate(), "yyyy-MM-dd"), leaveConfig);

                int countData = 0;

                try {
                    /* Untuk mendapatkan jumlah data stock yang aktif dan tidak aktif berdasarkan employee yang diinginkan */
                    countData = countAlStock(leaveAlClosing.getEmployeeId());
                } catch (Exception e) {
                    System.out.println("[exception] " + e.toString());
                }

                Employee employee = new Employee();

                try {
                    employee = PstEmployee.fetchExc(leaveAlClosing.getEmployeeId());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }

                Level level = new Level();

                try {
                    level = PstLevel.fetchExc(employee.getLevelId());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }

                EmpCategory empCategory = new EmpCategory();

                try {
                    empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                }

                //int datePeriod = leaveConfig.getDatePeriod();
                //Date periodeDT = new Date();
                //periodeDT = getPeriodeClosing(employee.getCommencingDate(), datePeriod, leaveConfig);
                //update by satrya 2013-1-04
                 AlUpload alUpload = new AlUpload();
                if (countData == 0){ // jika stock aktif dan tidak aktif belum ada

                    float entitle = 0;
                    int LoS = (int) DateCalc.dayDifference(employee.getCommencingDate(), new Date());
                    
                  
                    float tmpentitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(),new Date());
                    //upsdate by satrya 2013-11-09 int tmpentitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(),new Date());

                     /*Untuk mendapatkan leave closing pertama */
                    Date tmpEntitleDt = SessLeaveApplication.getEntitleFirstClosing(employee.getCommencingDate(), leaveConfig);
                    
                    if (tmpEntitleDt != null && employee.getCommencingDate()!=null){

                        if (tmpentitle == 0) { /* Jika entitle yang sebenarnya yang harus dia dapatkan adalah 0 */

                            entitle = 0;

                        } else {

                            entitle = leaveConfig.getQtyEntitle(tmpEntitleDt, level.getLevel(), empCategory.getEmpCategory(), employee.getCommencingDate());
                            //entitle = getEntitleEmployee(employee.getCommencingDate(),level.getLevel(),empCategory.getEmpCategory(), leaveConfig);
                        }
                        //update by satrya 2013-11-22
                        float entitleAlExtra = 0;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraAl(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(), new Date());
                        Date dtEntitleAlExtra = null;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraDateAl(entitleAlExtra, employee.getCommencingDate());
                       
                       
                        AlStockManagement alStockManagement = new AlStockManagement();
                        
                        alStockManagement.setLeavePeriodeId(0);
                        alStockManagement.setEmployeeId(leaveAlClosing.getEmployeeId());
                        
                         //entitle teap karena pertama
                        boolean pertamaClosing = PstAlStockManagement.CekPertamaClosing(""+PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]+" = "+leaveAlClosing.getEmployeeId());
                        if (pertamaClosing){
                            Vector AlStockManagementV = PstAlStockManagement.list(0, 0, ""+PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]+" = "+leaveAlClosing.getEmployeeId(), "");
                            AlStockManagement alStockManagementFirst = (AlStockManagement) AlStockManagementV.get(0);
                            alStockManagement.setEntitleDate(alStockManagementFirst.getEntitleDate());
                        } else {
                        
                        alStockManagement.setEntitleDate(tmpEntitleDt);
                        }
                        //alStockManagement.setEntitleDate(periodeDT);
                        alStockManagement.setOpening(0);
                        //alStockManagement.setEntitled(entitle);
                        alStockManagement.setEntitled(entitle ); //priska menambahkan auto 12
                        alStockManagement.setAlQty(entitle); 
                        alStockManagement.setQtyUsed(0);
                        alStockManagement.setPrevBalance(0);
                        alStockManagement.setStNote("");
                        alStockManagement.setRecordDate(new Date());
                        alStockManagement.setAlStatus(PstAlStockManagement.AL_STS_AKTIF);
                        //update by satrya 2013-11-22
                        alStockManagement.setExtraAl(entitleAlExtra);
                        alStockManagement.setExtraAlDate(dtEntitleAlExtra); 
                        try {
                            PstAlStockManagement.insertExc(alStockManagement);
                            //karena AL sudah closing, maka update data opname status menjadi 1
                           
                            alUpload.setEmployeeId(leaveAlClosing.getEmployeeId());
                            if(leaveAlClosing.getEmployeeId()!=0){
                                PstAlUpload.updateDataStatusByEmpId(alUpload.getEmployeeId()); 
                            }
                        } catch (Exception e) {
                            System.out.println("[exception] " + e.toString());
                        }
                   
                    }

                }else{
                    int maximumAlTakenLeave = 0;
                    try{
                         maximumAlTakenLeave = Integer.parseInt(PstSystemProperty.getValueByName("LEAVE_MAKSIMUM_AL_TAKEN_LEAVE"));
                    }catch(Exception exc){
                        System.out.println("Exc LEAVE_MAKSIMUM_AL_TAKEN_LEAVE"+exc);
                    }
                    Date entDtClosing = Formater.formatDate(leaveAlClosing.getEntitledDate(), "yyyy-MM-dd");
                    int LoS = (int) DateCalc.dayDifference(employee.getCommencingDate(), entDtClosing);
                    
                    float tmpentitle = leaveConfig.getALEntitleAnualLeave(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(), new Date());

                    Date tmpDtClosing = SessLeaveApplication.getEntitle(entDtClosing,leaveConfig); //mengambil date closing ex: 31 desember 2015
                    Date dateEntile = new Date(tmpDtClosing.getTime()+(24*60*60*1000)); //membuat date entitle yang baru ex: 1 januari 2015
                    float entitle = 0;
                    
                     //update by satrya 2013-11-22
                    float entitleAlExtra = 0;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraAl(level.getLevel(), empCategory.getEmpCategory(), LoS, employee.getCommencingDate(), new Date());
                    Date dtEntitleAlExtra =  null;//update by satrya 2014-06-10 karena al Extra sdh di tambahkan di service manager leaveConfig.getAlExtraDateAl(entitleAlExtra, employee.getCommencingDate());
                        
                    if(tmpDtClosing != null){

                        /*if (tmpentitle == 0){ // Jika entitle yang sebenarnya yang harus dia dapatkan adalah 0 

                            entitle = 0;

                        } else{

                            entitle = leaveConfig.getQtyEntitle(tmpDtClosing);
                            
                        }*/

                        AlStockManagement alStockManagement = new AlStockManagement();

                        String expired = getDateIntervalMonth(leaveAlClosing.getExpiredDate(), tmpDtClosing);
                        alStockManagement.setDtOwningDate(dateEntile);
                        alStockManagement.setLeavePeriodeId(0);
                        alStockManagement.setEmployeeId(leaveAlClosing.getEmployeeId());
                        alStockManagement.setOpening(0);
                        alStockManagement.setPrevBalance(leaveAlClosing.getExtended());
                        //update by satrya 2014-06-10
                        //karena konfigurasinya, jika melebihi dari 24 maka akan di prev'nya akan di kurangi berapa maksimum prev yg didapat
                        /*if(maximumAlTakenLeave>0){
                            alStockManagement.setPrevBalance(leaveAlClosing.getExtended() + tmpentitle>maximumAlTakenLeave?(maximumAlTakenLeave-tmpentitle):leaveAlClosing.getExtended());
                        }else{
                           alStockManagement.setPrevBalance(leaveAlClosing.getExtended());
                        }*/
                        
                        alStockManagement.setEntitled(tmpentitle);//entitle);
                        //update by satrya 2014-01-18 alStockManagement.setAlQty(leaveAlClosing.getExtended() + tmpentitle);//update by satrya 2013-10-28 tmpentitle //entitle);
                        alStockManagement.setAlQty(alStockManagement.getPrevBalance() + tmpentitle);//update by satrya 2013-10-28 tmpentitle //entitle);
                        
                        alStockManagement.setQtyUsed(0);
                        alStockManagement.setAlStatus(PstAlStockManagement.AL_STS_AKTIF);
                        alStockManagement.setStNote("");
                        
                        alStockManagement.setRecordDate(new Date());
                         //entitle teap karena pertama
                        boolean pertamaClosing = PstAlStockManagement.CekPertamaClosing(""+PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]+" = "+leaveAlClosing.getEmployeeId());
                        if (pertamaClosing){
                            Vector AlStockManagementV = PstAlStockManagement.list(0, 0, ""+PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]+" = "+leaveAlClosing.getEmployeeId(), "");
                            AlStockManagement alStockManagementFirst = (AlStockManagement) AlStockManagementV.get(0);
                            alStockManagement.setEntitleDate(alStockManagementFirst.getEntitleDate());
                        } else {
                        
                        alStockManagement.setEntitleDate(dateEntile);
                        }
                        
                        alStockManagement.setExpiredDate(Formater.formatDate(expired, "yyyy-MM-dd"));

                         //update by satrya 2013-11-22
                        alStockManagement.setExtraAl(entitleAlExtra);
                        alStockManagement.setExtraAlDate(dtEntitleAlExtra); 
                        try {

                            PstAlStockManagement.insertExc(alStockManagement);
                            
                             alUpload.setEmployeeId(leaveAlClosing.getEmployeeId());
                             //update by satrya 2013-1-04
                            if(leaveAlClosing.getEmployeeId()!=0){
                                PstAlUpload.updateDataStatusByEmpId(alUpload.getEmployeeId()); 
                            }
                        } catch (Exception e) {
                            System.out.println("[exception] " + e.toString());
                        }

                        AlStockManagement objAlStockManagement = new AlStockManagement();

                        try {
                            objAlStockManagement = PstAlStockManagement.fetchExc(leaveAlClosing.getStockId());
                        } catch (Exception e) {
                            System.out.println("[exception] :: " + e.toString());
                        }

                        objAlStockManagement.setAlStatus(PstAlStockManagement.AL_STS_NOT_AKTIF);

                        /* Untuk mengupdate status al stock management dari aktif ke not aktif*/
                        try {
                            PstAlStockManagement.updateExc(objAlStockManagement);
                        } catch (Exception e) {
                            System.out.println("[exception] :: " + e.toString());
                        }
                    }
                }
              } else {
                int resultSts = SessLeaveClosing.statusAlStockManagement(leaveAlClosing.getEmployeeId(), leaveAlClosing.getStockId());

                boolean stockNotEmpty = StockExist(leaveAlClosing.getEmployeeId());

                // update commencing date

                updateEntitledDate(leaveAlClosing.getEmployeeId());

                if (stockNotEmpty == true) {


                    if (leaveAlClosing.getStatus() == 1) {

                        // only close

                        if (resultSts == 3) {

                            updateStokManagementNotValid(leaveAlClosing.getStockId());

                        } else if (resultSts == 6) {

                            // kondisi normal

                            updateStockManagementNormal(leaveAlClosing);

                        } else if (resultSts == 5) {

                            updateStockManagementNoTransfer(leaveAlClosing);

                        } else if (resultSts == 2) {

                            updateStockManagementNormal(leaveAlClosing);

                            //updateStockManagementNoTransfer(leaveAlClosing);

                        }

                    } else if (leaveAlClosing.getStatus() == 2) {

                        // transfer dan close

                        UpdateStockDataTransfer(leaveAlClosing.getStockId(), leaveAlClosing.getEmployeeId(), leaveAlClosing.getExpiredDate(), leaveAlClosing.getExtended());

                    }
                } else {

                    //jika employee tidak memiliki stock

                    insertStockEmpty(leaveAlClosing.getEmployeeId());

                }  
              }  
            }
        }
    }
    
    
    /**
     * @Author  Roy A.
     * @Desc    Untuk mendapatkan jumlah data al stock yang aktif dan yang tidak aktif 
     *          berdasarkan employee yang diinputkan
     * @param   employeeId
     * @return
     */
    public static int countAlStock(long employeeId) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT COUNT(*) FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId
                    + " AND (" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF + " OR "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "=" + PstAlStockManagement.AL_STS_NOT_AKTIF + " )";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int countData = 0;

            while (rs.next()) {
                countData = rs.getInt(1);
                return countData;
            }

        } catch (Exception e) {
            System.out.println("[exception] ");
        } finally {
            DBResultSet.close(dbrs);
        }

        return 0;
    }

    /**
     * @Author  Roy A.
     * @Desc    Untuk mendapatkan jumlah data al stock yang aktif 
     *          berdasarkan employee yang diinputkan
     * @param   employeeId
     * @return
     */
    private static int countAlStockAktif(long employeeId) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT COUNT(*) FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + employeeId
                    + " AND " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int countData = 0;

            while (rs.next()) {
                countData = rs.getInt(1);
                return countData;
            }

        } catch (Exception e) {
            System.out.println("[exception] ");
        } finally {
            DBResultSet.close(dbrs);
        }

        return 0;
    }

    public static int statusGetAlPeriod(long employeeId, long alStockId) {

        if (employeeId == 0 || alStockId == 0) {
            return -1;
        }
//update by satrya 2012-10-16
        String whereAl = PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + "=" + employeeId;

        Vector listAlStock = new Vector();

        try {

            listAlStock = PstAlStockManagement.list(0, 0, whereAl, null);

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }

        if (listAlStock == null && listAlStock.size() > 0) {
            return AL_CLOSE_FIRST; // kondisi jika stock al blum ada dan akan dilakukan closing pertama
        } else {
        }
        return -1;

    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan list employee yang akan mendapatkan entitle
     * @Desc    
     * @param   entDt
     * @return
     */
    public static Vector getEmployeeEntitlePerMonth(int entDt) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT " + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ","
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " WHERE "
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = " + PstAlStockManagement.AL_STS_AKTIF
                    + " AND DATE_ADD(" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL " + entDt + " MONTH )";


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                Employee employee = new Employee();
                AlStockManagement alStockManagement = new AlStockManagement();
                Department department = new Department();

                alStockManagement.setOID(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                alStockManagement.setEmployeeId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));

            }

        } catch (Exception e) {
            System.out.println("[exception] " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan al closing periode yang belum mempunyai entitle by periode.
     * @Desc    Untuk beberapa kasus, seperti di NIKKO dan INTIMAS
     * @Desc    untuk masing-masing periode
     */
    public static Vector getAlActiveByPeriod(SrcLeaveAppAlClosing srcLeaveAppAlClosing, int monthService) {

        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs;
        Vector result = new Vector();

        try {
            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + "="
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + "="
                    + PstEmployee.NO_RESIGN + " AND ( DATE_ADD(ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ", INTERVAL 12 MONTH) <= CURRENT_DATE ) ";

            String whereClause = "";
            String strEmpNum = "";

            if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + "=" + srcLeaveAppAlClosing.getEmpNum();

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }

            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }

            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }

            }

            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }

            sql = sql + whereClause;

            sql = sql + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL 1 YEAR ) <= CURRENT_DATE ";

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            System.out.println("SQL LEAVE CLOSING PERIOD (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                /* Untuk mendapatkan employee yang mesti closing berdasarkan periode */

                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();

                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getInt(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                result.add(leaveAlClosingList);
            }
            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    /**
     * @Author  Roy Andika
     * @param   srcLeaveAppAlClosing
     * @param   intrvGetStockAl dalam month
     * @Desc    Untuk mencapatkan list employee yang belum mempunyai stock
     * @return
     */
    public static Vector getEmployeeNotYetHaveStock(SrcLeaveAppAlClosing srcLeaveAppAlClosing, I_Leave leaveConfig) {

        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs = null;
        Vector result = new Vector();

        try {

            String sql = "SELECT EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + ",LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID] + ",CAT."
                    + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]
                    + " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " EMP LEFT JOIN "
                    + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " INNER JOIN "
                    + PstLevel.TBL_HR_LEVEL + " LEV ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID] + " = LEV."
                    + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID] + " INNER JOIN " + PstEmpCategory.TBL_HR_EMP_CATEGORY + " CAT ON EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "= CAT." + PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY_ID]
                    + " WHERE ( ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " is null OR ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + " = 0 ) AND EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN;


            String whereClause = "";
            String strEmpNum = "";

            /* update by satrya 2013-11-22 if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + "=" + srcLeaveAppAlClosing.getEmpNum();

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }*/
             if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(srcLeaveAppAlClosing.getEmpNum());
               // sql = sql + "  ";
                if (vectNum != null && vectNum.size() > 0) {
                    strEmpNum = strEmpNum + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strEmpNum = strEmpNum +" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strEmpNum = strEmpNum + str.trim();
                        }
                    }
                    strEmpNum = strEmpNum + ")";
                }
                
            }
             if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }

            
            String strPayGroupId = "";

            if ((srcLeaveAppAlClosing.getPayGroupId() != 0)) {

                strPayGroupId = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + "=" + srcLeaveAppAlClosing.getPayGroupId();

                if (strPayGroupId.length() > 0) {

                    whereClause = whereClause + " AND " + strPayGroupId;

                }

            }

            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }


            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }

            }


            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }
            
            
            String strEnt = "";

            if (srcLeaveAppAlClosing.getPeriodId() != 0 ) {
                Period period = new Period();
                try{
                   period = PstPeriod.fetchExc(srcLeaveAppAlClosing.getPeriodId()); 
                }catch(Exception e){}
                
                String strDateStart = Formater.formatDate(period.getStartDate(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(period.getEndDate(), "yyyy-MM-dd");
                strEnt = strEnt + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strEnt.length() > 0) {

                    whereClause = whereClause + " AND " + strEnt;
                }
            }

            
            sql = sql + whereClause;

            /* var intrvGetStockAl dalam month */
            sql = sql + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL " + leaveConfig.getMinimalWorkAL() + " MONTH ) <= CURRENT_DATE ";

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            //System.out.println("[ SQL ] CLOSING NO STOCK (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();


            while (rs.next()) {
                int LoS = (int) DateCalc.dayDifference(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), srcLeaveAppAlClosing.getEmpCommancingDateEnd());
                float entitled = leaveConfig.getALEntitleAnualLeave(rs.getString(PstLevel.fieldNames[PstLevel.FLD_LEVEL]), 
                        rs.getString(PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY]), LoS, 
                        rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]), new Date());

                /*  */
                if (entitled > 0) {

                    LeaveAlClosingNoStockList leaveAlClosingNoStockList = new LeaveAlClosingNoStockList();
                    leaveAlClosingNoStockList.setEmpId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]));
                    leaveAlClosingNoStockList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                    leaveAlClosingNoStockList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                    leaveAlClosingNoStockList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                    leaveAlClosingNoStockList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                    leaveAlClosingNoStockList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                    leaveAlClosingNoStockList.setEntitled(entitled);
                    result.add(leaveAlClosingNoStockList);

                }
            }
            rs.close();
            return result;
        } catch (Exception e) {
            System.out.println("Exception " + e.toString());
        }
        return null;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan Al yang closing periode
     * @param   srcLeaveAppAlClosing
     * @return
     */
    public static Vector getEmployeeActiveByPeriode(SrcLeaveAppAlClosing srcLeaveAppAlClosing, I_Leave leaveConfig) {

        if (srcLeaveAppAlClosing == null) {
            return null;
        }

        DBResultSet dbrs;
        Vector result = new Vector();

        Date periodeDt = getPeriodDate(leaveConfig.getMonthPeriod(), leaveConfig.getDatePeriod());

        try {

            String sql = "SELECT ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_RECORD_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ",EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + ",DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]
                    + " FROM " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " ALM "
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN "
                    + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = "
                    + PstAlStockManagement.AL_STS_AKTIF + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = "
                    + PstEmployee.NO_RESIGN + " AND (" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " < '" + Formater.formatDate(periodeDt, "yyyy-MM-dd") + "'"
                    + "  OR "+PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " IS NULL ) ";

            String whereClause = "";
            String strEmpNum = "";

            /*if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {

                strEmpNum = strEmpNum + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + "like '%" + srcLeaveAppAlClosing.getEmpNum() +"%'";

                if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }
            }*/
            
            if ((srcLeaveAppAlClosing.getEmpNum() != null) && (srcLeaveAppAlClosing.getEmpNum().length() > 0)) {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(srcLeaveAppAlClosing.getEmpNum());
               // sql = sql + "  ";
                if (vectNum != null && vectNum.size() > 0) {
                    strEmpNum = strEmpNum + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            strEmpNum = strEmpNum +" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            strEmpNum = strEmpNum + str.trim();
                        }
                    }
                    strEmpNum = strEmpNum + ")";
                }
                
            }
             if (strEmpNum.length() > 0) {

                    whereClause = whereClause + " AND " + strEmpNum;

                }

            String strFullName = "";

            if ((srcLeaveAppAlClosing.getFullName() != null) && (srcLeaveAppAlClosing.getFullName().length() > 0)) {

                strFullName = strFullName + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE '%" + srcLeaveAppAlClosing.getFullName() + "%'";

                if (strFullName.length() > 0) {

                    whereClause = whereClause + " AND " + strFullName;

                }

            }

            String strDepartment = "";

            if ((srcLeaveAppAlClosing.getDepartmentId() != 0)) {

                strDepartment = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=" + srcLeaveAppAlClosing.getDepartmentId();

                if (strDepartment.length() > 0) {

                    whereClause = whereClause + " AND " + strDepartment;

                }

            }


            
            
            String strCategory = "";

            if (srcLeaveAppAlClosing.getCategoryId() != 0) {

                strCategory = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + "=" + srcLeaveAppAlClosing.getCategoryId();

                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strCategory;

                }

            }

            String strSection = "";

            if (srcLeaveAppAlClosing.getSectionId() != 0) {

                strSection = strSection + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + "=" + srcLeaveAppAlClosing.getSectionId();

                if (strSection.length() > 0) {

                    whereClause = whereClause + " AND " + strSection;
                }
            }


            String strPosition = "";

            if (srcLeaveAppAlClosing.getPositionId() != 0) {

                strPosition = strPosition + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID] + "=" + srcLeaveAppAlClosing.getPositionId();
                if (strCategory.length() > 0) {

                    whereClause = whereClause + " AND " + strPosition;
                }
            }

            String strComencingDate = "";

            if (srcLeaveAppAlClosing.getEmpCommancingDateStart() != null && srcLeaveAppAlClosing.getEmpCommancingDateEnd() != null) {

                String strDateStart = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateStart(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(srcLeaveAppAlClosing.getEmpCommancingDateEnd(), "yyyy-MM-dd");
                strComencingDate = strComencingDate + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strComencingDate.length() > 0) {

                    whereClause = whereClause + " AND " + strComencingDate;
                }
            }
            
            String strPayGroupId = "";

            if ((srcLeaveAppAlClosing.getPayGroupId() != 0)) {

                strPayGroupId = " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + "=" + srcLeaveAppAlClosing.getPayGroupId();

                if (strPayGroupId.length() > 0) {

                    whereClause = whereClause + " AND " + strPayGroupId;

                }

            }
            
            String strEnt = "";

            if (srcLeaveAppAlClosing.getPeriodId() != 0 ) {
                Period period = new Period();
                try{
                   period = PstPeriod.fetchExc(srcLeaveAppAlClosing.getPeriodId()); 
                }catch(Exception e){}
                
                String strDateStart = Formater.formatDate(period.getStartDate(), "yyyy-MM-dd");
                String strDateEnd = Formater.formatDate(period.getEndDate(), "yyyy-MM-dd");
                strEnt = strEnt + " ALM." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE] + " BETWEEN '"
                        + strDateStart + "' AND '" + strDateEnd + "'";

                if (strEnt.length() > 0) {

                    whereClause = whereClause + " AND " + strEnt;
                }
            }

            sql = sql + whereClause;

            sql = sql + " AND DATE_ADD(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE] + ", INTERVAL " + leaveConfig.getMinimalWorkAL() + " MONTH ) <= CURRENT_DATE ";

            sql = sql + " ORDER BY EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + ",ALM."
                    + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE];

            //System.out.println("SQL LEAVE CLOSING (BY SEARCHING) : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                LeaveAlClosingList leaveAlClosingList = new LeaveAlClosingList();

                leaveAlClosingList.setAlStockManagementId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STOCK_ID]));
                leaveAlClosingList.setPrevBalance(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE]));
                leaveAlClosingList.setEntitled(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLED]));
                leaveAlClosingList.setQtyUsed(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED]));
                leaveAlClosingList.setOpening(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_OPENING]));
                leaveAlClosingList.setQtyResidue(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_RESIDUE]));
                leaveAlClosingList.setAlQty(rs.getFloat(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY]));
                leaveAlClosingList.setEntitledDate(rs.getDate(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_ENTITLE_DATE]));
                leaveAlClosingList.setEmpId(rs.getLong(PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID]));
                leaveAlClosingList.setEmpNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                leaveAlClosingList.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                leaveAlClosingList.setCommancingDate(rs.getDate(PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]));
                leaveAlClosingList.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                leaveAlClosingList.setDepartment(rs.getString(PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]));
                result.add(leaveAlClosingList);
            }
            rs.close();
            return result;

        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        }

        return null;
    }

    
    public static Date getPeriodDate(int monthPeriod, int datePeriod) {

        int yearCurnt = new Date().getYear() + 1900;//2900;
        int yearPrev = new Date().getYear() + 1900;//2900;

        String periodCurnt = yearCurnt + "-" + monthPeriod + "-" + datePeriod;

        String periodPrev = yearPrev + "-" + monthPeriod + "-" + datePeriod;

        Date PeriodDtCurrent = Formater.formatDate(periodCurnt, "yyyy-MM-dd");

        Date PeriodDtPrev = Formater.formatDate(periodPrev, "yyyy-MM-dd");

        if ((PeriodDtCurrent.getTime() / (24L * 60L * 60L * 1000L)) >= (new Date().getTime() / (24L * 60L * 60L * 1000L))) {

            return PeriodDtCurrent;

        } else {

            return PeriodDtPrev;

        }

    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan periode sebelum current date / Periode AL yang aktif
     * @param   leaveConfig
     * @return
     */
    public static Date getPeriodeCurrent(I_Leave leaveConfig) {

        int year = new Date().getYear() + 1900;
        int mont = leaveConfig.getMonthPeriod();
        int date = leaveConfig.getDatePeriod();

        return Formater.formatDate(year + "-" + mont + "-" + date, "yyyy-MM-dd");

    }

    public static int getEntitle(Date commencingDate, I_Leave leaveConfig) {

        Date Period = getPeriodeCurrent(leaveConfig);

        int DateDiff = DATEDIFF(commencingDate, Period);

        int entitle = 0;

        if (DateDiff > 0) { /* Jika kondisi commencing date berada setelah period AL aktiv */

            int monthComn = commencingDate.getDate();

            int Dt;
            int Mnt;
            if (commencingDate.getDate() < leaveConfig.getLimitPeriode()) {
                Dt = leaveConfig.getDatePeriod();
                Mnt = leaveConfig.getMonthPeriod();

            }

            int monthPeriod = leaveConfig.getMonthPeriod();

        } else {
        }


        return 0;
    }

    /**
     * @Author  Roy Andika
     * @Desc    Untuk mendapatkan entitle date employee yang baru pertama kali closing
     * @param   commencingDate
     * @param   leaveConfig
     * @return
     */
    public static Date getEntitleEmployeeFirst(Date commencingDate, I_Leave leaveConfig){

        try {

            int date;
            int month;
            int year = new Date().getYear() + 1900;

            Date PeriodCurrent = Formater.formatDate("" + year + "-" + leaveConfig.getMonthPeriod() + "-" + leaveConfig.getDatePeriod(), "yyyy-MM-dd");

            int dif = DATEDIFERENT(new Date(), PeriodCurrent);

            if (dif < 0) {
                /*Jika current date kurang dari current period awal maka akan dicari periode tahun sebelumnya */
                year = year - 1;
                PeriodCurrent = Formater.formatDate("" + year + "-" + leaveConfig.getMonthPeriod() + "-" + leaveConfig.getDatePeriod(), "yyyy-MM-dd");

            }

            /**
             * @Desc    : Untuk melakukan pengecekan tanggal periode
             */
            Date DtComDt = null;

            if (commencingDate.getDate() < leaveConfig.getLimitPeriode()) { // maka periode yang di gunakan adalah periode current

                month = new Date().getMonth() + 1;
                int monthNext = new Date().getMonth() + 2;

                int curDt = new Date().getYear() + 1900;

                String DtCurrent = "" + curDt + "-" + month + "-" + leaveConfig.getDatePeriod();
                String DtNext = "" + curDt + "-" + monthNext + "-" + leaveConfig.getDatePeriod();

                int intDt = DATEDIFERENT(Formater.formatDate(DtNext, "yyyy-MM-dd"), Formater.formatDate(DtCurrent, "yyyy-MM-dd")) - 1;

                String curntDt = DATE_ADD(Formater.formatDate(DtCurrent, "yyyy-MM-dd"), intDt, INTERVAL_DAY);

                DtComDt = Formater.formatDate(curntDt, "yyyy-MM-dd");

            } else {

                date = leaveConfig.getDatePeriod();

                month = new Date().getMonth() + 2;

                int nextYear;
                int monthNext;

                if (month == 12) {

                    monthNext = 1;
                    nextYear = year + 1;
                } else {
                    monthNext = new Date().getMonth() + 3;
                    nextYear = year;
                }
                year = new Date().getYear();

                String DtCurrent = "" + year + "-" + month + "-" + date;
                String DtNext = "" + nextYear + "-" + monthNext + "-" + date;

                int intDt = DATEDIFERENT(Formater.formatDate(DtNext, "yyyy-MM-dd"), Formater.formatDate(DtCurrent, "yyyy-MM-dd")) - 1;

                String curDt = DATE_ADD(Formater.formatDate(DtCurrent, "yyyy-MM-dd"), intDt, INTERVAL_DAY);

                DtComDt = Formater.formatDate(curDt, "yyyy-MM-dd");

            }

            boolean aktif = false;
            int loop = 0;

            Date ProcesDt = DtComDt;

            while (aktif == true && loop < 100) {

                if (ProcesDt.getDate() != leaveConfig.getDatePeriod() || ProcesDt.getMonth() != leaveConfig.getMonthPeriod()) {

                    int tmpYear = ProcesDt.getYear() + 1900;
                    int tmpMonth = ProcesDt.getMonth() + 1;
                    int tmpDate = ProcesDt.getDate();

                    Date tmpProcesDt = ProcesDt;
                    tmpProcesDt.setDate(leaveConfig.getDatePeriod());
                    tmpProcesDt.setMonth(leaveConfig.getMonthPeriod() + 1);
                    int diferent = DATEDIFERENT(ProcesDt, tmpProcesDt);


                }

                Date nextProcess = (Date) ProcesDt.clone();
                nextProcess.setDate(nextProcess.getDate() + 1);
                ProcesDt = nextProcess;
                loop++;
            }

            /**
             * @Desc    : Ubntuk melakukan pengecekan tanggal periode
             */
            if (commencingDate.getDate() < leaveConfig.getLimitPeriode()) { // maka periode yang di gunakan adalah periode current

                date = leaveConfig.getDatePeriod();
                month = new Date().getMonth() + 1;
                int monthNext = new Date().getMonth() + 2;

                int curDt = new Date().getYear() + 1900;

                String DtCurrent = "" + curDt + "-" + month + "-" + leaveConfig.getDatePeriod();
                String DtNext = "" + curDt + "-" + monthNext + "-" + leaveConfig.getDatePeriod();

                int intDt = DATEDIFERENT(Formater.formatDate(DtNext, "yyyy-MM-dd"), Formater.formatDate(DtCurrent, "yyyy-MM-dd")) - 1;

                String curntDt = DATE_ADD(Formater.formatDate(DtCurrent, "yyyy-MM-dd"), intDt, INTERVAL_DAY);

                return Formater.formatDate(curntDt, "yyyy-MM-dd");

            } else { // maka akan diambil periode berikutnya

                date = leaveConfig.getDatePeriod();
                month = new Date().getMonth() + 2;
                int monthNext = new Date().getMonth() + 3;
                year = new Date().getYear();

                String DtCurrent = "" + year + "-" + month + "-" + date;
                String DtNext = "" + year + "-" + monthNext + "-" + date;

                int intDt = DATEDIFERENT(Formater.formatDate(DtNext, "yyyy-MM-dd"), Formater.formatDate(DtCurrent, "yyyy-MM-dd")) - 1;

                String curDt = DATE_ADD(Formater.formatDate(DtCurrent, "yyyy-MM-dd"), intDt, INTERVAL_DAY);

                return Formater.formatDate(curDt, "yyyy-MM-dd");

            }

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }

        return null;

    }

    /**
     * @Aythor  Roy Andika
     * @Desc    Untuk mengecek selisih tanggal first dan second
     * @param   first
     * @param   second
     * @return
     */
    public static int DATEDIFERENT(Date first, Date second) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT DATEDIFF('" + Formater.formatDate(first, "yyyy-MM-dd") + "','" + Formater.formatDate(second, "yyyy-MM-dd") + "')";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                return rs.getInt(1);

            }

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return 0;
    }

    /**
     * @Author  Roy Andika
     * @Desc         
     */
    public static Date getPeriodeEntitle(Date commencingDt, I_Leave leaveConfig) {

        DBResultSet dbrs = null;

        try {

            int date;
            int month;
            int year;

            /* Proses untuk entitle date */
            if (commencingDt.getDate() < leaveConfig.getLimitPeriode()) {

                date = leaveConfig.getDatePeriod();
                month = commencingDt.getMonth() + 1;
                year = commencingDt.getYear() + 1900;

                /* akan di kombinasikan antara year, month, date */
                String dtEntitle = "" + year + "-" + month + "-" + date;

                return Formater.formatDate(dtEntitle, "yyyy-MM-dd");

            } else {

                date = leaveConfig.getDatePeriod();
                month = commencingDt.getMonth() + 2;
                year = commencingDt.getYear() + 1900;

                if (month == 12) { /* Jika month sudah berada pada bulan desember, maka tahun akan di tambah 1, tahun berikutnya */
                    year = year + 1;
                }

                /* akan di kombinasikan antara year, month, date */
                String dtEntitle = "" + year + "-" + month + "-" + date;
                return Formater.formatDate(dtEntitle, "yyyy-MM-dd");

            }


        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }
        return null;

    }
}
