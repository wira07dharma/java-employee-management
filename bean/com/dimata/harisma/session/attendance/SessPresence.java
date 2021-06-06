/*
 * Session Name  	:  SessPresence.java
 * Created on 	:  [date] [time] AM/PM
 *
 * @author  	: karya
 * @version  	: 01
 */
/*******************************************************************
 * Class Description 	: [project description ... ]
 * Imput Parameters 	: [input parameter ...]
 * Output 		: [output ...]
 *******************************************************************/
package com.dimata.harisma.session.attendance;

/* java package */
import java.io.*;
import java.util.*;
import java.sql.ResultSet;
import javax.servlet.*;
import javax.servlet.http.*;

/* qdep package */
import com.dimata.util.*;
import com.dimata.qdep.form.*;
import com.dimata.qdep.db.*;

/* project package */
import com.dimata.harisma.entity.search.*;
import com.dimata.harisma.form.search.*;
import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.overtime.HashTblOvertimeDetail;

import com.dimata.harisma.entity.overtime.PstOvertimeDetail;
import com.dimata.harisma.session.leave.RepItemLeaveAndDp;
import com.dimata.harisma.session.payroll.I_PayrollCalculator;
import com.dimata.harisma.utility.machine.AnalyseStatusDataPresence;
import com.dimata.harisma.utility.machine.SaverData;
import com.dimata.harisma.utility.service.presence.PresenceAnalyser;
import com.dimata.system.session.system.*;
import com.dimata.system.entity.system.*;
import org.apache.xerces.utils.Hash2intTable;

public class SessPresence {

    public static final String SESS_SRC_PRESENCE = "SESSION_SRC_PRESENCE";
    //private static String PRESENCE_RANGE = SessSystemProperty.PRESENCE_RANGE;
    //update by satrya 2012-07-18
    private String empNum ;
    private String empFullName;

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

    public static Vector searchPresence(SrcPresence srcpresence, int start, int recordToGet){
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        if (srcpresence == null) {
            return new Vector(1, 1);
        }

        try {

            //String sql = " SELECT DISTINCT P." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]
            String sql = " SELECT  P." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]
                    + ", P." + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + ", P." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                    + ", P." + PstPresence.fieldNames[PstPresence.FLD_STATUS]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                    + " FROM " + PstPresence.TBL_HR_PRESENCE + " P "
                    + "  LEFT JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " ON " + " P." + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE (1=1) ";

            String whereClause = "";
            if ((srcpresence.getFullname() != null) && (srcpresence.getFullname().length() > 0)) {
                Vector vectName = logicParser(srcpresence.getFullname());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") AND ";
                }
            }

            if ((srcpresence.getEmpnumber() != null) && (srcpresence.getEmpnumber().length() > 0)) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " = \"" + srcpresence.getEmpnumber() + "\" AND ";
            }

            if (srcpresence.getDepartment().compareToIgnoreCase("0") > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + srcpresence.getDepartment() + " AND ";
            }

            if (srcpresence.getPosition().compareToIgnoreCase("0") > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + srcpresence.getPosition() + " AND ";
            }

            if (srcpresence.getSection().compareToIgnoreCase("0") > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + srcpresence.getSection() + " AND ";
            }

            if (!srcpresence.isPeriodCheck()) {
                if ((srcpresence.getDatefrom() != null) && (srcpresence.getDateto() != null)) {
                    String strDateFrom = Formater.formatDate(srcpresence.getDatefrom(), "yyyy-MM-dd");
                    String strDateTo = Formater.formatDate(srcpresence.getDateto(), "yyyy-MM-dd");

                    whereClause = whereClause + " P." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                            + " BETWEEN \"" + strDateFrom + " 00:00:00\""
                            + " AND \"" + strDateTo + " 23:59:59\" AND ";
                }
            }

            if( srcpresence.getStatusCheck()!=null && srcpresence.getStatusCheck().size()>0 ){
                Vector vSts = srcpresence.getStatusCheck();
                whereClause = whereClause + " ( ";
                for(int idx =0; idx < vSts.size() ; idx ++){
                        Integer st = (Integer) vSts.get(idx);
                        whereClause = whereClause + " ( P."+ PstPresence.fieldNames[PstPresence.FLD_STATUS]+
                                "="+ st.toString()+ " ) OR";
                }
                whereClause = whereClause + " (1=0 )) AND ";
            }


            if (whereClause != null && whereClause.length() > 0) {
                whereClause = whereClause + "1 = 1";
                sql = sql + " AND " + whereClause;
            }
            //update by satrya 2012-07-10
            //sql = sql + " GROUP BY P. " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
            // end update
            sql = sql + " ORDER BY " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + ", " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];

            if (start > 0 || recordToGet > 0) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }


//            System.out.println("SessPresence search presence sql : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                Vector vect = new Vector(1, 1);

                Presence presence = new Presence();
                Employee employee = new Employee();

                presence.setOID(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]));
                presence.setEmployeeId(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]));

                //System.out.println("\tgetDate : " + rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
                //System.out.println("\tgetTime : " + rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
                int y = rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]).getYear();
                int M = rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]).getMonth();
                int d = rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]).getDate();
                int h = rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]).getHours();
                int m = rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]).getMinutes();
                int s = rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]).getSeconds();
                java.util.Date dt = new java.util.Date(y, M, d, h, m, s);
                //presence.setPresenceDatetime(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
                presence.setPresenceDatetime(dt);

                presence.setStatus(rs.getInt(PstPresence.fieldNames[PstPresence.FLD_STATUS]));
                vect.add(presence);

                employee.setFullName(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]));
                employee.setDepartmentId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]));
                employee.setPositionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]));
                employee.setSectionId(rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]));
                employee.setEmployeeNum(rs.getString(PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]));
                vect.add(employee);

                result.add(vect);
            }

            return result;
        } catch (Exception e) {
            System.out.println("\t Exception on searchPresence : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector(1, 1);
    }

    public static int getCountSearch(SrcPresence srcpresence) {
        DBResultSet dbrs = null;
        Vector result = new Vector(1, 1);
        if (srcpresence == null) {
            return 0;
        }

        try {
            String sql = " SELECT COUNT(P." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] + ") "
                    + " FROM "
                    + " " + PstPresence.TBL_HR_PRESENCE + " P "
                    + " , " + PstEmployee.TBL_HR_EMPLOYEE + " EMP "
                    + " WHERE "
                    + " P." + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            String whereClause = "";
            if ((srcpresence.getFullname() != null) && (srcpresence.getFullname().length() > 0)) {
                Vector vectName = logicParser(srcpresence.getFullname());
                if (vectName != null && vectName.size() > 0) {
                    whereClause = whereClause + " (";
                    for (int i = 0; i < vectName.size(); i++) {
                        String str = (String) vectName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            whereClause = whereClause + str.trim();
                        }
                    }
                    whereClause = whereClause + ") AND ";
                }
            }

            if ((srcpresence.getEmpnumber() != null) && (srcpresence.getEmpnumber().length() > 0)) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE \"%" + srcpresence.getEmpnumber() + "%\" AND ";
            }

            if (srcpresence.getDepartment().compareToIgnoreCase("0") > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + srcpresence.getDepartment() + " AND ";
            }

            if (srcpresence.getPosition().compareToIgnoreCase("0") > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]
                        + " = " + srcpresence.getPosition() + " AND ";
            }

            if (srcpresence.getSection().compareToIgnoreCase("0") > 0) {
                whereClause = whereClause + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + srcpresence.getSection() + " AND ";
            }

            if (!srcpresence.isPeriodCheck()) {
                if ((srcpresence.getDatefrom() != null) && (srcpresence.getDateto() != null)) {
                    String strDateFrom = Formater.formatDate(srcpresence.getDatefrom(), "yyyy-MM-dd");
                    String strDateTo = Formater.formatDate(srcpresence.getDateto(), "yyyy-MM-dd");

                    whereClause = whereClause + " P." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                            + " BETWEEN \"" + strDateFrom + " 00:00:00\""
                            + " AND \"" + strDateTo + " 23:59:59\" AND ";
                }
            }

            if( srcpresence.getStatusCheck()!=null && srcpresence.getStatusCheck().size()>0 ){
                Vector vSts = srcpresence.getStatusCheck();
                whereClause = whereClause + " ( ";
                for(int idx =0; idx < vSts.size() ; idx ++){
                        Integer st = (Integer) vSts.get(idx);
                        whereClause = whereClause + " ( P."+ PstPresence.fieldNames[PstPresence.FLD_STATUS]+
                                "="+ st.toString()+ " ) OR";
                }
                whereClause = whereClause + " (1=0)) AND ";
            }

            if (whereClause != null && whereClause.length() > 0) {
                whereClause = whereClause + "1 = 1";
                sql = sql + " AND " + whereClause;
            }

//            System.out.println("SQL count : " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            int num = 0;
            while (rs.next()) {
                num = rs.getInt(1);
            }
//            System.out.println("\tsearchPresence - getCountSearch = " + num);
            return num;
        } catch (Exception e) {
            System.out.println("\t Exception on getCountSearch : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;
    }

    public static java.util.Date getPresenceInOut(long empId, java.util.Date dtPresence, int status) {
        String strDtPresence = (dtPresence.getYear() + 1900) + "-" + (dtPresence.getMonth() + 1) + "-" + dtPresence.getDate();

        DBResultSet dbrsIn = null;
        java.util.Date timeIn = new java.util.Date();
        String sqlIn = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                + " FROM " + PstPresence.TBL_HR_PRESENCE
                + " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + " = " + String.valueOf(empId)
                + " AND TO_DAYS(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + ") = TO_DAYS('" + strDtPresence + "') ";
        sqlIn += " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = 0";
        sqlIn += " ORDER BY " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + ", " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];
        sqlIn += " LIMIT 0, 1 ";

        //System.out.println("\t SQL In searchPresence : " + sqlIn);

        try {
            dbrsIn = DBHandler.execQueryResult(sqlIn);
            ResultSet rsIn = dbrsIn.getResultSet();
            while (rsIn.next()) {
                timeIn = rsIn.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]);
                //System.out.println("\ttimeIn ===> "+timeIn);
            }
        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceInOut (getTimeIn) : " + e);
        } finally {
            DBResultSet.close(dbrsIn);
        }

        //=======================|>----------------<|=======================
        DBResultSet dbrs = null;
        //java.util.Date dt = null;
        java.util.Date tm = null;
        try {
            String sql = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]
                    + ", " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + ", " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                    + ", " + PstPresence.fieldNames[PstPresence.FLD_STATUS]
                    + " FROM "
                    + " " + PstPresence.TBL_HR_PRESENCE
                    + " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + " = " + String.valueOf(empId);
            sql = sql + " AND TO_DAYS(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + ") = TO_DAYS('" + strDtPresence + "') ";
            sql = sql + " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = " + String.valueOf(status);

            if (status == 1) { // out time
                //sql += " AND TIME_TO_SEC(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
                //    ") > TIME_TO_SEC('" + timeIn + "') ";
            }
            sql = sql + " ORDER BY " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + ", " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];
            sql = sql + " LIMIT 0, 1 ";

            //System.out.println("\t SQL searchPresence : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                //dt = rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]);
                tm = rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]);
                //System.out.println("\t" + tm);
            }

            //return dt;
            return tm;
        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceInOut : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        //return dt;
        return tm;
    }

    public static java.util.Date getPresenceActualIn(long empId, java.util.Date paramTimeIn) {
        DBResultSet dbrs = null;
        java.util.Date tm = null;

        try {
            /*String sql = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            " FROM " + PstPresence.TBL_HR_PRESENCE +
            " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] +
            " = " + String.valueOf(empId) +
            " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = 0 " +
            " AND (UNIX_TIMESTAMP(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            ") > (UNIX_TIMESTAMP('" + Formater.formatDate(paramTimeIn, "yyyy-MM-dd HH:mm:ss") +
            "') - (" + PRESENCE_RANGE + "))) " +
            " AND (UNIX_TIMESTAMP(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            ") < (UNIX_TIMESTAMP('" + Formater.formatDate(paramTimeIn, "yyyy-MM-dd HH:mm:ss") +
            "') + (" + PRESENCE_RANGE + "))) " +
            " ORDER BY " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            " LIMIT 0, 1 ";*/

            String sql = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                    + " FROM " + PstPresence.TBL_HR_PRESENCE
                    + " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + " = " + String.valueOf(empId)
                    + " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = 0 "
                    + " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                    + " BETWEEN '" + Formater.formatDate(paramTimeIn, "yyyy-MM-dd 00:00:00") + "'"
                    + " AND '" + Formater.formatDate(paramTimeIn, "yyyy-MM-dd 23:59:59") + "'";

            System.out.println("\t-------------" + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                tm = DBHandler.convertDate(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]), rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
                //System.out.println("\t" + tm);
            }
            return tm;
        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceActualIn : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return tm;
    }

    public static java.util.Date getPresenceActualOut(long empId, java.util.Date paramTimeOut) {
        DBResultSet dbrs = null;
        java.util.Date tm = null;
        System.out.println("paramTimeOut " + paramTimeOut);
        try {
            /* String
            sql = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            " FROM " + PstPresence.TBL_HR_PRESENCE +
            " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] +
            " = " + String.valueOf(empId) +
            " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = 1 " +
            " AND (UNIX_TIMESTAMP(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            ") > (UNIX_TIMESTAMP('" + Formater.formatDate(paramTimeOut, "yyyy-MM-dd HH:mm:ss") +
            "') - (" + PRESENCE_RANGE + "))) " +
            " AND (UNIX_TIMESTAMP(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            ") < (UNIX_TIMESTAMP('" + Formater.formatDate(paramTimeOut, "yyyy-MM-dd HH:mm:ss") +
            "') + (" + PRESENCE_RANGE + "))) " +
            " ORDER BY " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
            " DESC " +
            " LIMIT 0, 1 ";*/


            String sql = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                    + " FROM " + PstPresence.TBL_HR_PRESENCE
                    + " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + " = " + String.valueOf(empId)
                    + " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = 1 "
                    + " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                    + " BETWEEN '" + Formater.formatDate(paramTimeOut, "yyyy-MM-dd 00:00:00") + "'"
                    + " AND '" + Formater.formatDate(paramTimeOut, "yyyy-MM-dd 23:59:59") + "'";


            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                tm = DBHandler.convertDate(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]), rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
                //System.out.println("\t" + tm);
            }
            return tm;
        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceActualIn : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return tm;
    }

    /*
    public static long getPresenceId(long empId, java.util.Date dtPresence, int status) {
    DBResultSet dbrs = null;
    long tm = 0;
    try {
    String sql = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] +
    " FROM " + PstPresence.TBL_HR_PRESENCE +
    " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + " = " + String.valueOf(empId);
    sql = sql + " AND TO_DAYS(" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + ") = TO_DAYS('" + dtPresence.toString() + "') ";
    sql = sql + " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = " + String.valueOf(status);
    sql = sql + " ORDER BY " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + ", " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];
    sql = sql + " LIMIT 0, 1 ";
    dbrs = DBHandler.execQueryResult(sql);
    ResultSet rs = dbrs.getResultSet();
    while(rs.next()) {
    tm = rs.getLong(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]);
    }
    return tm;
    } catch (Exception e) {
    System.out.println("\t Exception on getPresenceId : " + e);
    }
    finally {
    DBResultSet.close(dbrs);
    }
    return tm;
    }
     */
    public static long getPresenceSchedule(long empId, long period, String whichDt) {
        DBResultSet dbrs = null;
        long schedule = 0;

        try {
            String sql = " SELECT " + whichDt + " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE
                    + " WHERE " + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID] + " = " + String.valueOf(empId);
            sql = sql + " AND " + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID] + " = " + String.valueOf(period) + " ";

            System.out.println("\t SQL searchPresence : " + sql);

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                schedule = rs.getLong(whichDt);
                //System.out.println("\t" + schedule);
            }

            return schedule;
        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceSchedule : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return schedule;
    }

    public static String getPresenceScdSymbol(long scheduleId) {
        DBResultSet dbrs = null;
        String scheduleSymbol = "";

        try {
            String sql = " SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL]
                    + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL
                    + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]
                    + " = " + String.valueOf(scheduleId);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                scheduleSymbol = rs.getString(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL]);
            }
            return scheduleSymbol;
        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceScdSymbol : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return scheduleSymbol;
    }

    public static java.util.Date getPresenceScdTimeInOut(long scheduleId, int iWhich) {
        DBResultSet dbrs = null;
        java.util.Date scdTimeInOut = null;

        String strWhich = "";
        switch (iWhich) {
            case 0:
                strWhich = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN];
                break;
            case 1:
                strWhich = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT];
                break;
            default:
                break;
        }
        try {
            String sql = " SELECT " + strWhich
                    + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL
                    + " WHERE " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]
                    + " = " + String.valueOf(scheduleId);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                scdTimeInOut = rs.getTime(strWhich);
            }
            return scdTimeInOut;
        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceScdTimeIn : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return scdTimeInOut;
    }

    public static ScheduleSymbol getSchedule(long employeeId, Date date) {
        DBResultSet dbrs = null;
        try {
            String sql = " SELECT " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]
                    + ", " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL]
                    + ", " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN]
                    + ", " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT]
                    + ", " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE]
                    + " FROM " + PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL + " SS "
                    + " INNER JOIN " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " ES "
                    + " ON ES." + PstEmpSchedule.FLD_SCHEDULE + date.getDate()
                    + " = SS." + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]
                    + " INNER JOIN " + PstPeriod.TBL_HR_PERIOD + " PRD "
                    + " ON PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID]
                    + " = ES." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                    + " WHERE ES." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                    + " = " + employeeId
                    + " AND TO_DAYS('" + Formater.formatDate(date, "yyyy-MM-dd") + "')"
                    + " BETWEEN TO_DAYS(PRD." + PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ")"
                    + " AND TO_DAYS(PRD." + PstPeriod.fieldNames[PstPeriod.FLD_END_DATE] + ")";

            System.out.println(">>>>getSchedule " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            ScheduleSymbol scheduleSymbol = new ScheduleSymbol();
            while (rs.next()) {
                scheduleSymbol.setOID(rs.getLong(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]));
                scheduleSymbol.setSymbol(rs.getString(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL]));
                scheduleSymbol.setTimeIn(rs.getTime(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN]));
                scheduleSymbol.setTimeOut(rs.getTime(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT]));
                scheduleSymbol.setSchedule(rs.getString(PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE]));
            }
            rs.close();
            return scheduleSymbol;

        } catch (Exception e) {
            System.out.println("\t Exception on getPresenceScdSymbol : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new ScheduleSymbol();
    }

    /**
     *
     * @param transfStatus : transfer status dari attendance records yang akan di fix :
     *                      PRESENCE_NOT_TRANSFERRED = 0;  PRESENCE_TRANSFERRED = 1;
     * @param employeeID : employee OID yang akan di fix, jika semua maka set to 0
     * @return String : "OK : ..." = prosess OK
     */
    public static String fixAttendanceRecords(int transfStatus, long employeeID, int rangeInMin, int rangeOutMin) {
        String msg = "";
        int startDatePeriode = 1;
        String strTmp = PstSystemProperty.getValueByName("START_DATE_PERIOD");
        if (strTmp != null) {
            try {
                startDatePeriode = Integer.parseInt(strTmp);
            } catch (Exception exc) {
                System.out.println("EXC in fixAttendanceRecords - parse START_DATE_PERIOD " + exc);
            }
        }

        // get employee list yang mempunyai attendance belum di proses, distinct employee
        Vector empLst = listEmployeeAttendance(0, 0, employeeID);
        if ((empLst == null) || (empLst.size() < 1)) {
            return "OK : no new attendance data was found";
        }
        msg = msg + "Found " + empLst.size() + " employee with new attendance records.";
        int totalPrc = 0;
        // list attendance per employee order by date time
        for (int empIdx = 0; empIdx < empLst.size(); empIdx++) {
            long empId = ((Long) empLst.get(empIdx)).longValue();
            String whereCls = "" + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + "=" + empId + " AND "
                    + PstPresence.fieldNames[PstPresence.FLD_ANALYZED] + " = 0 " + " AND "
                    + PstPresence.fieldNames[PstPresence.FLD_TRANSFERRED] + " =  " + transfStatus;

            String orderCls = "" + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];

            Vector attLst = PstPresence.list(0, 20000, whereCls, orderCls);
            msg = msg + "\n" + (empIdx + 1) + ") " + empId + " has Attendance of " + (attLst != null ? attLst.size() : 0);

            if ((attLst != null) && (attLst.size() > 0)) {
                Period hrCurrPrd = null;
                Period hrPrevPrd = null;
                EmpSchedule schCurr = null;
                EmpSchedule schPrev = null;
                totalPrc = totalPrc + attLst.size();
                for (int attIdx = 0; attIdx < attLst.size(); attIdx++) {  // loop for each attendance
                    Presence prc = (Presence) attLst.get(attIdx);

                    if ((hrCurrPrd == null) || (schCurr == null)) { // periode dan schedule untuk curr AT(attendance) belum diambil
                        //ambil current periode
                        long periodId = PstPeriod.getPeriodIdBySelectedDate(prc.getPresenceDatetime());
                        //ambil periode sehari sebelumnya untuk handling cross date schedule
                        long prevPrdId = PstPeriod.getPeriodIdBySelectedDate(new Date(prc.getPresenceDatetime().getTime() - (24 * 60 * 60 * 10000)));
                        try {
                            hrCurrPrd = PstPeriod.fetchExc(periodId);
                            // ambil schedule dalam satu periode yang cocok dengan date AT
                            schCurr = PstEmpSchedule.fetchExc(PstEmpSchedule.getEmpScheduleId(periodId, empId));
                            if (prevPrdId != periodId) { // ambil juga schedule sebelumnya jika periode sebelumnya beda.
                                hrPrevPrd = PstPeriod.fetchExc(prevPrdId);
                                schPrev = PstEmpSchedule.fetchExc(PstEmpSchedule.getEmpScheduleId(prevPrdId, empId));
                            }

                        } catch (Exception exc) {
                            System.out.println("Exc by fetchExc(periodId=" + periodId + ") ");
                        }
                    } else { // check AT date vs periode
                        Date dtPrc = prc.getPresenceDatetime();
                        Date dtStart = hrCurrPrd.getStartDate();
                        Date dtEnd = hrCurrPrd.getEndDate();

                        if (!((dtPrc.after(dtStart) && dtPrc.before(dtEnd)) || (dtPrc.equals(dtStart)) || (dtPrc.equals(dtEnd)))) {
                            // date attendance diluar dalam current periode
                            // ambil periode baru
                            long periodId = PstPeriod.getPeriodIdBySelectedDate(prc.getPresenceDatetime());
                            try {
                                hrCurrPrd = PstPeriod.fetchExc(periodId);
                            } catch (Exception exc) {
                                System.out.println("Exc by fetchExc(periodId=" + periodId + ") ");
                            }
                            long prevPrdId = PstPeriod.getPeriodIdBySelectedDate(new Date(prc.getPresenceDatetime().getTime() - (24 * 60 * 60 * 10000)));
                            try {
                                // ambil schedule dalam satu periode yang cocok dengan date AT
                                schCurr = PstEmpSchedule.fetchExc(PstEmpSchedule.getEmpScheduleId(periodId, empId));
                                if ((prevPrdId != periodId) && prevPrdId != 0) { // ambil juga schedule sebelumnya jika periode sebelumnya beda.
                                    hrPrevPrd = PstPeriod.fetchExc(prevPrdId);
                                    schPrev = PstEmpSchedule.fetchExc(PstEmpSchedule.getEmpScheduleId(prevPrdId, empId));
                                } else {
                                    hrPrevPrd = null; // set to null jika period
                                    schPrev = null;
                                    prevPrdId = 0;
                                }

                            } catch (Exception exc) {
                                System.out.println("Exc by fetchExc(" + periodId + ") ");
                            }
                        }
                    }
                    // pada titik ini periode dan schedule sudah relevan dengan AT
                    long checkEmpID = 1033;
                    int chkIdx = 2;
                    if ((empId == checkEmpID) && (attIdx == chkIdx)) {
                        System.out.println("check debug");
                    }


                    // ambil schedule normal pada current AT
                    int atDate = prc.getPresenceDatetime().getDate();
                    ScheduleSymbol atSch = null;
                    ScheduleSymbol atSch2nd = null;
                    try {
                        atSch = PstScheduleSymbol.fetchExc(schCurr.getD(atDate));
                        if (schCurr.getD2nd(atDate) != 0) { // split shift exist
                            atSch2nd = PstScheduleSymbol.fetchExc(schCurr.getD2nd(atDate));
                        }
                    } catch (Exception exc) {
                        //System.out.println(" PstScheduleSymbol.fetchExc(schSymId) " + exc);
                    }


                    // check in normal schedule or first schedule in case of split save
                    boolean foundInFirst = false;

                    // check apakah schedule real, kalau real continue dengan analisa kalau tidak diamkan as is it

                    boolean schIsReal = scheduleIsReal(atSch, rangeInMin, rangeOutMin);
                    if (!schIsReal) {
                        System.out.println("Schedule for emp OID " + empId + " Date " + prc.getPresenceDatetime() + " not set (?)");
                    }

                    if ((atSch != null) && (schIsReal)) {
                        Date schIn = atSch.getTimeIn();
                        Date schOut = atSch.getTimeOut();

                        // ambil category schedule
                        long schCtId = atSch.getScheduleCategoryId();
                        ScheduleCategory schCat = null;
                        try {
                            schCat = PstScheduleCategory.fetchExc(schCtId);
                        } catch (Exception exc) {
                            System.out.println(" PstScheduleCategory.fetchExc(schCtId) " + exc);
                        }

                        //trial apakah AT (presence masuk dalam range schedule IN  hari sesuai presence
                        if (chkTimeInRange(prc.getPresenceDatetime(), prc.getPresenceDatetime(), schIn, rangeInMin)) {
                            // presence masuk dalam range schedule IN , maka set presence as IN
                            foundInFirst = true;
                            int prevStatus = prc.getStatus();
                            //if (prevStatus != Presence.STATUS_IN) {
                            prc.setStatus(Presence.STATUS_IN);
                            // update status menjadi STATUS_IN , untuk STATUS_IN_LUNCH, STATUS_IN_BREAK, STATUS_IN_CALLBACK belum di handle
                            //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_IN);
                            //}

                            // update schedule data and presence
                            int updateStatus = 0;
                            try {
                                int fldIndex = PstEmpSchedule.FLD_IN1 + prc.getPresenceDatetime().getDate() - 1;
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                if (updateStatus > 0) {
                                    prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                    PstPresence.updateExc(prc);
                                }
                            } catch (Exception e) {
                                System.out.println("Update Presence exc : " + e.toString());
                            }




                        } else {
                            // jika tidak masuk dalam IN
                            //trial apakah AT (presence masuk dalam range schedule OUT
                            //check apakah scheduleny crosss day, tapi dalam hal ini presence terjadi di hari ini , misal karena pulang duluan

                            if ((schCat.getCategoryType() == PstScheduleCategory.CATEGORY_ACCROSS_DAY)
                                    || (atSch.getTimeIn().getHours() > atSch.getTimeOut().getHours())) {
                                // schedule termasuk cross day, maka acuan tanggal yang dipakai untuk peradingan time out range adalah satu hari setelahnya
                                // walaupun ada kemungkinan OUT nya lebih dulu ( terjadi sebelum jam 00:00 , misal pulang duluan
                                Date dtPrcNxtDay = new Date(prc.getPresenceDatetime().getTime() + 24 * 60 * 60 * 1000);

                                if (chkTimeInRange(prc.getPresenceDatetime(), dtPrcNxtDay, schOut, rangeOutMin)) {
                                    // presence masuk dalam range schedule OUT , maka set presence as OUT
                                    foundInFirst = true;
                                    int prevStatus = prc.getStatus();
                                    //if (prevStatus != Presence.STATUS_OUT) {
                                    prc.setStatus(Presence.STATUS_OUT);
                                    // update status menjadi STATUS_OUT_HOME , untuk STATUS_OUT_ON_DUTY belum di handle
                                    //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_OUT);
                                    //}
                                    // update schedule data and presence
                                    int updateStatus = 0;
                                    try {
                                        int fldIndex = PstEmpSchedule.FLD_OUT1 + prc.getPresenceDatetime().getDate() - 1;
                                        updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                        if (updateStatus > 0) {
                                            prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                            PstPresence.updateExc(prc);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Update Presence exc : " + e.toString());
                                    }
                                }
                            } else {
                                // schedule normal
                                if (chkTimeInRange(prc.getPresenceDatetime(), prc.getPresenceDatetime(), schOut, rangeOutMin)) {
                                    // presence masuk dalam range schedule OUT , maka set presence as OUT
                                    foundInFirst = true;
                                    int prevStatus = prc.getStatus();
                                    //if (prevStatus != Presence.STATUS_OUT) {
                                    prc.setStatus(Presence.STATUS_OUT);
                                    // update status menjadi STATUS_OUT_HOME , untuk STATUS_OUT_ON_DUTY belum di handle
                                    //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_OUT);
                                    //}
                                    // update schedule data and presence
                                    int updateStatus = 0;
                                    try {
                                        int fldIndex = PstEmpSchedule.FLD_OUT1 + prc.getPresenceDatetime().getDate() - 1;
                                        updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                        if (updateStatus > 0) {
                                            prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                            PstPresence.updateExc(prc);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Update Presence exc : " + e.toString());
                                    }
                                }
                            }


                        }
                    }

                    if (!foundInFirst) {
                        // Jika tidak ketemu schedule pada current date ada kemungkinan OUT ini milik schedule sehari sebelumnya
                        // check dulu apakah schedule sehari sebelumnya dalam satu periode
                        // kemudian check apakah cross day night shift
                        if (empId == 10104L) { // check untuk debuging
                            System.out.println("10104L");
                        }
                        Date prevDate = new Date(prc.getPresenceDatetime().getTime() - 24 * 60 * 60 * 1000);

                        if (schPrev == null) { // check apakah masih masuk dalam periode current
                            // schedule sehari sebelumnya masih dalam satu periode
                            // ambil schedule sehari sebemumnya
                            // ambil schedule normal pada current AT
                            Date atPrevDay = new Date(prc.getPresenceDatetime().getTime() - 24 * 60 * 60 * 1000);
                            int atPrevDate = atPrevDay.getDate();
                            ScheduleSymbol atPrevSch = null;
                            ScheduleSymbol atPrevSch2nd = null;

                            try {
                                atPrevSch = PstScheduleSymbol.fetchExc(schCurr.getD(atPrevDate));
                                if (schCurr.getD2nd(atPrevDate) != 0) { // split shift exist
                                    atPrevSch2nd = PstScheduleSymbol.fetchExc(schCurr.getD2nd(atPrevDate));
                                }
                            } catch (Exception exc) {
                                //System.out.println(" PstScheduleSymbol.fetchExc(schCurr.getD2nd(atPrevDate)) " + exc);
                            }

                            boolean prevSchIsReal = scheduleIsReal(atPrevSch, rangeInMin, rangeOutMin);
                            if ((atPrevSch != null) && prevSchIsReal) { // schedule sehari sebelum attendance record ada dan real
                                // ambil category schedule sehari sebelumnya
                                //long prevSchCtId = atPrevSch.getScheduleCategoryId();
                                ScheduleCategory prevSchCat = null;
                                try {
                                    prevSchCat = PstScheduleCategory.fetchExc(atPrevSch.getScheduleCategoryId());
                                } catch (Exception exc) {
                                    System.out.println(" PstScheduleCategory.fetchExc(schCtId) " + exc);
                                }

                                //check apakah scheduleny crosss day
                                if ((prevSchCat.getCategoryType() == PstScheduleCategory.CATEGORY_NIGHT_WORKER)
                                        || (prevSchCat.getCategoryType() == PstScheduleCategory.CATEGORY_ACCROSS_DAY)
                                        || (atPrevSch.getTimeIn().getHours() > atPrevSch.getTimeOut().getHours())) {
                                    // schedule termasuk cross day, maka acuan tanggal yang dipakai untuk peradingan time out range adalah satu hari
                                    // setelah data schedule yang di check, sehingga menjadi hari sesuai presence
                                    // walaupun ada kemungkinan OUT nya lebih dulu ( terjadi sebelum jam 00:00 , misal pulang duluan
                                    Date dtPrcNxtDay = new Date(prc.getPresenceDatetime().getTime()); // prc/attendance sudah sehari setelah schedule
                                    if (chkTimeInRange(prc.getPresenceDatetime(), dtPrcNxtDay, atPrevSch.getTimeOut(), rangeOutMin)) {
                                        // presence masuk dalam range schedule OUT , maka set presence as OUT
                                        foundInFirst = true;
                                        //int prevStatus = prc.getStatus();
                                        //if (prevStatus != Presence.STATUS_OUT) {
                                        prc.setStatus(Presence.STATUS_OUT);
                                        // update status menjadi STATUS_OUT_HOME , untuk STATUS_OUT_ON_DUTY belum di handle
                                        //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_OUT);
                                        //}
                                        // update schedule data and presence
                                        int updateStatus = 0;
                                        try {
                                            Date aPrevDay = new Date(prc.getPresenceDatetime().getTime() - 24 * 60 * 60 * 1000); //scedule hari sebelum attendance
                                            int fldIndex = PstEmpSchedule.FLD_OUT1 + aPrevDay.getDate() - 1;
                                            updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                            if (updateStatus > 0) {
                                                prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                                PstPresence.updateExc(prc);
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Update Presence exc : " + e.toString());
                                        }
                                    }
                                } else {
                                    // schedule normal di hari sebelum attendance record

                                    if (chkTimeInRange(prc.getPresenceDatetime(), atPrevDay, atPrevSch.getTimeOut(), rangeOutMin)) {
                                        // presence masuk dalam range schedule OUT , maka set presence as OUT
                                        foundInFirst = true;
                                        //int prevStatus = prc.getStatus();
                                        //if (prevStatus != Presence.STATUS_OUT) {
                                        prc.setStatus(Presence.STATUS_OUT);
                                        // update status menjadi STATUS_OUT_HOME , untuk STATUS_OUT_ON_DUTY belum di handle
                                        //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_OUT);
                                        //}
                                        // update schedule data and presence
                                        int updateStatus = 0;
                                        try {
                                            Date aPrevDay = new Date(prc.getPresenceDatetime().getTime() - 24 * 60 * 60 * 1000);
                                            int fldIndex = PstEmpSchedule.FLD_OUT1 + aPrevDay.getDate() - 1;
                                            updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                            if (updateStatus > 0) {
                                                prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                                PstPresence.updateExc(prc);
                                            }

                                        } catch (Exception e) {
                                            System.out.println("Update Presence exc : " + e.toString());
                                        }
                                    }
                                }
                            }

                        }
                    }

                    if (!foundInFirst) {
                        // jika belum ditemukan pada IN schedule di hari sesuai presense dan OUT schedule di hari sebelumnya
                        // disini akan di check di apakah ada IN schedule yang cocok di hari sebelumnya, contoh :
                        // jika IN schedule jam 23:00, tapi karyawan terlambat satu jam lebih sehingga IN 0:15 sehari setelahnya
                        Date prevDate = new Date(prc.getPresenceDatetime().getTime() - 24 * 60 * 60 * 1000);

                        if (schPrev == null) { // check apakah masih masuk dalam periode current
                            // schedule sehari sebelumnya masih dalam satu periode
                            // ambil schedule sehari sebemumnya
                            // ambil schedule normal pada current AT
                            Date atPrevDay = new Date(prc.getPresenceDatetime().getTime() - 24 * 60 * 60 * 1000);
                            int atPrevDate = atPrevDay.getDate();
                            ScheduleSymbol atPrevSch = null;
                            ScheduleSymbol atPrevSch2nd = null;

                            try {
                                atPrevSch = PstScheduleSymbol.fetchExc(schCurr.getD(atPrevDate));
                                if (schCurr.getD2nd(atPrevDate) != 0) { // split shift exist
                                    atPrevSch2nd = PstScheduleSymbol.fetchExc(schCurr.getD2nd(atPrevDate));
                                }
                            } catch (Exception exc) {
                                //System.out.println(" PstScheduleSymbol.fetchExc(schCurr.getD2nd(atPrevDate)) " + exc);
                            }
                            boolean prevSchIsReal = scheduleIsReal(atPrevSch, rangeInMin, rangeOutMin);
                            if ((atPrevSch != null) && prevSchIsReal) { // schedule sehari sebelum attendance record ada dan real
                                if (chkTimeInRange(prc.getPresenceDatetime(), atPrevDay, atPrevSch.getTimeIn(), rangeInMin)) {
                                    // presence masuk dalam range schedule IN, maka set presence as IN
                                    foundInFirst = true;
                                    //int prevStatus = prc.getStatus();
                                    //if (prevStatus != Presence.STATUS_IN) {
                                    prc.setStatus(Presence.STATUS_IN);
                                    // update status menjadi STATUS_IN , untuk STATUS_IN_LUNCH, IN_CALL_BACK belum di handle
                                    //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_IN);
                                    //}
                                    // update schedule data and presence
                                    int updateStatus = 0;
                                    try {
                                        int fldIndex = PstEmpSchedule.FLD_IN1 + prc.getPresenceDatetime().getDate() - 1;
                                        updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                        if (updateStatus > 0) {
                                            prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                            PstPresence.updateExc(prc);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Update Presence exc : " + e.toString());
                                    }
                                }
                            }
                        }
                    }


                    if ((!foundInFirst) && (atSch2nd != null) && schIsReal) {
                        //jika belum di temukan di first schedule dan ada split schedule , maka check di split schedule (2nd schedule )
                        Date schIn = atSch2nd.getTimeIn();
                        Date schOut = atSch2nd.getTimeOut();

                        // ambil category schedule
                        long schCtId = atSch2nd.getScheduleCategoryId();
                        ScheduleCategory schCat = null;
                        try {
                            schCat = PstScheduleCategory.fetchExc(schCtId);
                        } catch (Exception exc) {
                            System.out.println(" PstScheduleCategory.fetchExc(schCtId) " + exc);
                        }

                        //trial apakah AT (presence masuk dalam range schedule IN
                        if (chkTimeInRange(prc.getPresenceDatetime(), prc.getPresenceDatetime(), schIn, rangeInMin)) {
                            // presence masuk dalam range schedule IN , maka set presence as IN
                            foundInFirst = true;
                            int prevStatus = prc.getStatus();
                            //if (prevStatus != Presence.STATUS_IN) {
                            prc.setStatus(Presence.STATUS_IN);
                            // update status menjadi STATUS_IN , untuk STATUS_IN_LUNCH, STATUS_IN_BREAK, STATUS_IN_CALLBACK belum di handle
                            //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_IN);
                            //}
                            // update schedule split 2 data and presence
                            int updateStatus = 0;
                            try {
                                int fldIndex = PstEmpSchedule.FLD_IN2ND1 + prc.getPresenceDatetime().getDate() - 1;
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                if (updateStatus > 0) {
                                    prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                    PstPresence.updateExc(prc);
                                }
                            } catch (Exception e) {
                                System.out.println("Update Presence exc : " + e.toString());
                            }
                        } else {
                            // jika tidak masuk dalam IN
                            //trial apakah AT (presence masuk dalam range schedule OUT

                            if ((schCat.getCategoryType() == PstScheduleCategory.CATEGORY_NIGHT_WORKER)
                                    || (schCat.getCategoryType() == PstScheduleCategory.CATEGORY_ACCROSS_DAY)
                                    || (atSch.getTimeIn().getHours() > atSch.getTimeOut().getHours())) {
                                // schedule termasuk cross day, maka acuan tanggal yang dipakai untuk peradingan time out range adalah satu hari setelahnya
                                Date dtPrcNxtDay = new Date(prc.getPresenceDatetime().getTime() + 24 * 60 * 60 * 1000);

                                if (chkTimeInRange(prc.getPresenceDatetime(), dtPrcNxtDay, schOut, rangeOutMin)) {
                                    // presence masuk dalam range schedule OUT , maka set presence as OUT
                                    foundInFirst = true;
                                    int prevStatus = prc.getStatus();
                                    //if (prevStatus != Presence.STATUS_OUT) {
                                    prc.setStatus(Presence.STATUS_OUT);
                                    // update status menjadi STATUS_OUT_HOME , untuk STATUS_OUT_ON_DUTY belum di handle
                                    //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_OUT);
                                    //}
                                    // update schedule split 2 data and presence
                                    int updateStatus = 0;
                                    try {
                                        int fldIndex = PstEmpSchedule.FLD_OUT2ND1 + prc.getPresenceDatetime().getDate() - 1;
                                        updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                        if (updateStatus > 0) {
                                            prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                            PstPresence.updateExc(prc);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Update Presence exc : " + e.toString());
                                    }
                                }
                            } else {
                                // schedule normal
                                if (chkTimeInRange(prc.getPresenceDatetime(), prc.getPresenceDatetime(), schOut, rangeOutMin)) {
                                    // presence masuk dalam range schedule OUT , maka set presence as OUT
                                    foundInFirst = true;
                                    int prevStatus = prc.getStatus();
                                    //if (prevStatus != Presence.STATUS_OUT) {
                                    prc.setStatus(Presence.STATUS_OUT);
                                    // update status menjadi STATUS_OUT_HOME , untuk STATUS_OUT_ON_DUTY belum di handle
                                    //PstPresence.updatePresenceStatus(prc.getOID(), Presence.STATUS_OUT);
                                    //}
                                    // update schedule split 2 data and presence
                                    int updateStatus = 0;
                                    try {
                                        int fldIndex = PstEmpSchedule.FLD_OUT2ND1 + prc.getPresenceDatetime().getDate() - 1;
                                        updateStatus = PstEmpSchedule.updateScheduleDataByPresence(hrCurrPrd.getOID(), empId, fldIndex, prc.getPresenceDatetime());
                                        if (updateStatus > 0) {
                                            prc.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                            PstPresence.updateExc(prc);
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Update Presence exc : " + e.toString());
                                    }


                                }
                            }
                            // sampai titik ini jika tidak ketemu IN atau OUT karena diluar range : rangeInMin dan rangeOutMin
                            // biarkan presence tidak berubah
                        }


                    }

                }
            }

        }
        return msg;
    }

    public static boolean chkTimeInRange(Date toCompare, Date baseDate, Date baseTime, int rangeMinutes) {
        try {
            Date minDate = new Date(baseDate.getTime());
            minDate.setHours(baseTime.getHours());
            minDate.setMinutes(baseTime.getMinutes());
            minDate.setTime(minDate.getTime() - (rangeMinutes * 60 * 1000));

            Date maxDate = new Date(baseDate.getTime());
            maxDate.setHours(baseTime.getHours());
            maxDate.setMinutes(baseTime.getMinutes());
            maxDate.setTime(maxDate.getTime() + (rangeMinutes * 60 * 1000));

            if ((toCompare.after(minDate) && toCompare.before(maxDate)) || toCompare.equals(minDate) || toCompare.equals(maxDate)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception exc) {
            System.out.println(" EXC : TimeInRange " + exc);
        }

        return false;
    }

    public static boolean scheduleIsReal(ScheduleSymbol atSch, int rangeInMin, int rangeOutMin) {
        boolean schIsReal = false;
        if (atSch != null) {
            try {
                int hrsOut = atSch.getTimeOut().getHours();
                int minsOut = atSch.getTimeOut().getMinutes();
                int hrsIn = atSch.getTimeIn().getHours();
                int minsIn = atSch.getTimeIn().getMinutes();
                // check minutes difference between in and out schedule, if hours of Out < hours of in => cross day
                int minDif = hrsOut >= hrsIn ? (hrsOut * 60 + minsOut) - (hrsIn * 60 + minsIn) : ((hrsOut + 24) * 60 + minsOut) - (hrsIn * 60 + minsIn);
                schIsReal = ((minDif > ((rangeInMin + rangeOutMin) / 2L)) ? true : false);

            } catch (Exception exc) {
                //System.out.println("On get Schedule dif Emp OID" + empId + " Date " + prc.getPresenceDatetime() + " not set (?) " + exc);
            }
            if (schIsReal) { // jika dari time in out ada beda check dari category
                // ambil category schedule
                long schCtId = atSch.getScheduleCategoryId();
                ScheduleCategory schCat = null;
                try {
                    schCat = PstScheduleCategory.fetchExc(schCtId);
                } catch (Exception exc) {
                    System.out.println(" PstScheduleCategory.fetchExc(schCtId) " + exc);
                }

                if ((schCat.getCategoryType() == PstScheduleCategory.CATEGORY_NIGHT_WORKER)
                        || (schCat.getCategoryType() == PstScheduleCategory.CATEGORY_ACCROSS_DAY)
                        || (schCat.getCategoryType() == PstScheduleCategory.CATEGORY_ALL)
                        || (schCat.getCategoryType() == PstScheduleCategory.CATEGORY_EXTRA_ON_DUTY)
                        || (schCat.getCategoryType() == PstScheduleCategory.CATEGORY_NEAR_ACCROSS_DAY)
                        || (schCat.getCategoryType() == PstScheduleCategory.CATEGORY_REGULAR)
                        || (schCat.getCategoryType() == PstScheduleCategory.CATEGORY_SPLIT_SHIFT)) {
                    schIsReal = true;
                } else {
                    schIsReal = false;
                }




            }



        }
        return schIsReal;

    }

    /**
     *
     */
    public static Vector listEmployeeAttendance(
            int attStatus, int transSatus, long empId) {
        DBResultSet dbrs = null;
        java.util.Date tm = null;
        Vector rslt = new Vector();

        try {
            String sql = " SELECT " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + " FROM " + PstPresence.TBL_HR_PRESENCE
                    + " WHERE "
                    + (empId != 0 ? (PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + " = " + String.valueOf(empId) + " AND ") : "")
                    + PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = " + attStatus
                    + " AND " + PstPresence.fieldNames[PstPresence.FLD_TRANSFERRED] + "=" + transSatus
                    + " GROUP BY " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                    + " ORDER BY " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID];

            System.out.println("\t-------------" + sql);

            dbrs =
                    DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {
                rslt.add(new Long(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID])));
            }

        } catch (Exception e) {
            System.out.println("\t Exception on listEmployeeAttendance : " + e);
        } finally {
            DBResultSet.close(dbrs);
        }

        return rslt;
    }
//update by devin 2014-01-28
    public static Vector listPresenceDataMonthly(long departmentId,long companyId,long divisionId, Date selectedMonth, long sectionId, String empNumb, String fullName) {

    // hiden by devin public static Vector listPresenceDataMonthly(long departmentId, Date selectedMonth, long sectionId, String empNumb, String fullName) {

        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        // get maximum date on selected month
        GregorianCalendar calenderWeek = new GregorianCalendar(selectedMonth.getYear() + 1900, selectedMonth.getMonth(), 1);
        int maxDayOnSelectedMonth = calenderWeek.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        // get periodId of selected date
        long periodId = PstPeriod.getPeriodIdBySelectedDate(selectedMonth);

        try {
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME];

            // fields schedule
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i];
            }

            // fields actual in
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i];
            }

            // fields actual out
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i];
            }

            ////////////////////Tambahan di hardrock
            // fields schedule 2nd
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i];
            }

            //Tambahan di hardrock
            // fields actual in 2nd
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i];
            }

            //Tambahan di hardrock
            // fields actual out 2nd
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i];
            }

            sql += " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS SCH"
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + " ON SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT"
                    + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                    + " = DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                    + " = " + periodId;
            if (departmentId != 0) {
                sql = sql + " AND DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                        + " = " + departmentId;
            }
              if (companyId != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                        + " = " + companyId;
            }
                if (divisionId != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + divisionId;
            }

            if (sectionId != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                        + " = " + sectionId;
            }
              if (empNumb != null && empNumb.length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + " LIKE " + "\"%" + empNumb + "%\"";
            }
            //update by satrya 2012-07-16
            if (fullName !=null && fullName.length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                        + " LIKE " + "\"%" + fullName + "%\"";
            }
            sql = sql + " ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];

           // System.out.println("\tlistPresenceMonthly: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PresenceMonthly presenceMonthly = new PresenceMonthly();

                presenceMonthly.setEmpNum(rs.getString(1) != null ? rs.getString(1) : "");
                presenceMonthly.setEmpName(rs.getString(2) != null ? rs.getString(2) : "");

                // values schedule
                Vector vectSchedule = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    vectSchedule.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i])));
                }
                presenceMonthly.setEmpSchedules(vectSchedule);

                // values actual in
                 //update by devin 2014-02-03
                    for (int i = 0; i <= maxDayOnSelectedMonth; i++) {
                        try{
                            presenceMonthly.setDayIdx(DBHandler.convertDate(rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1]))); 
                        }catch(Exception exc){
                            
                        }
                        
                    }
                Vector vectActualIn = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i]) != null) {
                        vectActualIn.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i])));
                    } else {
                        vectActualIn.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i]));
                    }
                }
                presenceMonthly.setEmpIn(vectActualIn);

                // values actual out
                Vector vectActualOut = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i]) != null) {
                        vectActualOut.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i])));
                    } else {
                        vectActualOut.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i]));
                    }
                }
                presenceMonthly.setEmpOut(vectActualOut);

                //Tambahan di Hardrock
                // values schedule
                Vector vectSchedule2 = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    vectSchedule2.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i])));
                }
                presenceMonthly.setEmpSchedules2(vectSchedule2);

                // values actual in
                Vector vectActualIn2 = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i]) != null) {
                        vectActualIn2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i])));
                    } else {
                        vectActualIn2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i]));
                    }
                }
                presenceMonthly.setEmpIn2(vectActualIn2);

                // values actual out
                Vector vectActualOut2 = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i]) != null) {
                        vectActualOut2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i])));
                    } else {
                        vectActualOut2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i]));
                    }
                }
                presenceMonthly.setEmpOut2(vectActualOut2);
                //////////Akhir tambahan di hardrock

                result.add(presenceMonthly);
            }
        } catch (Exception e) {
            System.out.println("listPresenceDataMonthly exc : " + e.toString());
            return result;
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /**
     * get list night shift data weekly
     * @param <CODE>departmentId</CODE>selected department
     * @param <CODE>selectedMonth</CODE>selected month
     * @param <CODE>weekIndex</CODE>selected week of selected month
     * @param <CODE>sectionId</CODE>selected week of selected month
     * @created by Yunny
     */
    public static Vector listPresenceDataWeekly(long departmentId,long companyId,long divisionId, int iCalendarType, Date selectedMonth, int weekIndex, long sectionId, String empNum, String fullName) {
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        // get start and end date of selected week of selected month
        CalendarCalc objCalendarCalc = new CalendarCalc(iCalendarType);
        Date startDateWeek = objCalendarCalc.getStartDateOfTheWeek(selectedMonth, weekIndex);
        Date endDateWeek = objCalendarCalc.getEndDateOfTheWeek(selectedMonth, weekIndex);
        int intStartDate = startDateWeek.getDate();
        int intEndDate = endDateWeek.getDate();

        int intStartDate2 = intStartDate;
        int intEndDate2 = intEndDate;

        // get periodId of selected date
        long periodId = PstPeriod.getPeriodIdBySelectedDate(startDateWeek);
        // long periodId = PstPeriod.getPeriodIdBySelectedDate(selectedMonth);
        long periodId2 = PstPeriod.getPeriodIdBySelectedDate(endDateWeek);

        // weekly presence in one Period
        /*Period period1 = null;
        try{
        period1 = PstPeriod.fetchExc(periodId);
        Date endDatePer = period1.getEndDate();
        if(intEndDate>endDatePer.getDate()){ // weekly report cross periode
        }
        } catch(Exception exc){
        }*/




        try {
            if (periodId == periodId2) {  // in one period
                String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                        //update by satrya 2012-10-18
                        + " ,EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

                // fields schedule
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + (i - 1)];
                }

                // fields actual in
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)];
                }

                // fields actual out
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)];
                }
                
                //update by devin 2014-01-30
               
                 for (int i = intStartDate; i <= intEndDate; i++) {
                    sql +=  ", IF(((p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY) < p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+"),"
                            + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+ " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+") - "+ (PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY),"
                            + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+ " -  INTERVAL(DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY)) AS SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1];
                }
                ////////////////////Tambahan di hardrock
                // fields schedule 2nd
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }

                //Tambahan di hardrock
                // fields actual in 2nd
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }

                //Tambahan di hardrock
                // fields actual out 2nd
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }
                
                sql += " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS SCH"
                        + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                        + " ON SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                        + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT"
                        + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                        //update by devin 2014-01-30
                         + " INNER JOIN "+PstPeriod.TBL_HR_PERIOD + " AS p ON p."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] 
                        + "=SCH."+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                        + " WHERE SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                        + " = " + periodId;
                if (departmentId != 0) {
                    sql = sql + " AND DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                            + " = " + departmentId;
                }
                //update by devin 2014-01-27
                 if (companyId != 0) {
                    sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]
                            + " = " + companyId;
                }
                  if (divisionId != 0) {
                    sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                            + " = " + divisionId;
                }

                if (sectionId != 0) {
                    sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                            + " = " + sectionId;
                }
               //update by devin 2014-01-30

                 if (empNum != null && empNum != "") {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(empNum);
                sql = sql + " AND ";
                if (vectNum != null && vectNum.size() > 0) {
                    sql = sql + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
            }
            }
                    sql = sql + ")";
                }
                
            }
            //update by devin 2014-01-30
            if (fullName != null && fullName != "") {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
//                        + " LIKE " + "\"%" + fullName.trim() + "%\"";//penambahan trim
                Vector vectFullName = logicParser(fullName);
                  sql = sql + " AND ";
                if (vectFullName != null && vectFullName.size() > 0) {
                    sql = sql + " ( ";
                    for (int i = 0; i < vectFullName.size(); i++) {
                        String str = (String) vectFullName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+ PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + " ) ";
                }
                 
            }
                sql = sql + " ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];

                //System.out.println("\tlistNightShiftDataWeekly : "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    PresenceWeekly presenceWeekly = new PresenceWeekly();

                    presenceWeekly.setEmpNum(rs.getString(1) != null ? rs.getString(1) : "");
                    presenceWeekly.setEmpName(rs.getString(2) != null ? rs.getString(2) : "");
                    presenceWeekly.setEmployeId(rs.getLong(3));
                    
                    //update by devin 2014-01-30
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        try{
                            presenceWeekly.addDayIdx(DBHandler.convertDate(rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1]))); 
                        }catch(Exception exc){
                            
                        }
                        
                    }
                    
                    // values schedule
                    Vector vectSchedule = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        vectSchedule.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + (i - 1)])));
                    }
                    presenceWeekly.setEmpSchedules(vectSchedule);
                    
                    // values actual in
                    Vector vectActualIn = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]) != null) {
                            vectActualIn.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)])));
                        } else {
                            vectActualIn.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]));
                        }
                    }
                    presenceWeekly.setEmpIn(vectActualIn);

                    // values actual out
                    Vector vectActualOut = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]) != null) {
                            vectActualOut.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)])));
                        } else {
                            vectActualOut.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]));
                        }
                    }
                    presenceWeekly.setEmpOut(vectActualOut);

                    //////////////////////////////////Tambahan di Hardrock
                    // values schedule
                    Vector vectSchedule2 = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        vectSchedule2.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                    }
                    presenceWeekly.setEmpSchedules2(vectSchedule2);

                    // values actual in
                    Vector vectActualIn2 = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]) != null) {
                            vectActualIn2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                        } else {
                            vectActualIn2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]));
                        }
                    }
                    presenceWeekly.setEmpIn2(vectActualIn2);

                    // values actual out
                    Vector vectActualOut2 = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]) != null) {
                            vectActualOut2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                        } else {
                            vectActualOut2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]));
                        }
                    }
                    presenceWeekly.setEmpOut2(vectActualOut2);
                    ////////////////////////////////Akhir tambahan di hardrock

                    result.add(presenceWeekly);
                }
            } else {
                // weekly report in 2 periods

                // get in the first period
                //update by satrya 2012-09-26 ????
                Period period1 = null;
                try {
                    period1 = PstPeriod.fetchExc(periodId);
                    Date endDatePer = period1.getEndDate();
                    intEndDate = endDatePer.getDate();
                } catch (Exception exc) {
                    System.out.println("Ex"+exc);
                }

                String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME];

                 //update by devin 2014-01-30
               
                 for (int i = intStartDate; i <= intEndDate; i++) {
                    sql +=  ", IF(((p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY) < p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+"),"
                            + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+ " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+") - "+ (PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY),"
                            + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+ " -  INTERVAL(DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY)) AS SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1];
                }
                // fields schedule
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + (i - 1)];
                }

                // fields actual in
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)];
                }

                // fields actual out
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)];
                }

                ////////////////////Tambahan di hardrock
                // fields schedule 2nd
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }

                //Tambahan di hardrock
                // fields actual in 2nd
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }

                //Tambahan di hardrock
                // fields actual out 2nd
                for (int i = intStartDate; i <= intEndDate; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }

                sql += " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS SCH"
                        + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                        + " ON SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                        + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT"
                        + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                         //update by devin 2014-01-30
                         + " INNER JOIN "+PstPeriod.TBL_HR_PERIOD + " AS p ON p."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] 
                        + "=SCH."+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                        + " WHERE SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                        + " = " + periodId;
                if (departmentId != 0) {
                    sql = sql + " AND DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                            + " = " + departmentId;
                }

                if (sectionId != 0) {
                    sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                            + " = " + sectionId;
                }
                //update by devin 2014-01-30

                 if (empNum != null && empNum != "") {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(empNum);
                sql = sql + " AND ";
                if (vectNum != null && vectNum.size() > 0) {
                    sql = sql + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
            }
            }
                    sql = sql + ")";
                }

            }
            //update by devin 2014-01-30
            if (fullName != null && fullName != "") {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
//                        + " LIKE " + "\"%" + fullName.trim() + "%\"";//penambahan trim
                Vector vectFullName = logicParser(fullName);
                  sql = sql + " AND ";
                if (vectFullName != null && vectFullName.size() > 0) {
                    sql = sql + " ( ";
                    for (int i = 0; i < vectFullName.size(); i++) {
                        String str = (String) vectFullName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+ PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + " ) ";
                }
                 
            }
                sql = sql + " ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];

                //System.out.println("\tlistNightShiftDataWeekly : "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();
                while (rs.next()) {
                    PresenceWeekly presenceWeekly = new PresenceWeekly();

                    presenceWeekly.setEmpNum(rs.getString(1) != null ? rs.getString(1) : "");
                    presenceWeekly.setEmpName(rs.getString(2) != null ? rs.getString(2) : "");

                    //update by devin 2014-01-30
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        try{
                            presenceWeekly.addDayIdx(DBHandler.convertDate(rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1]))); 
                        }catch(Exception exc){
                            
                        }
                        
                    }
                    // values schedule
                    Vector vectSchedule = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        vectSchedule.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + (i - 1)])));
                    }
                    presenceWeekly.setEmpSchedules(vectSchedule);

                    // values actual in
                    Vector vectActualIn = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]) != null) {
                            vectActualIn.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)])));
                        } else {
                            vectActualIn.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]));
                        }
                    }
                    presenceWeekly.setEmpIn(vectActualIn);

                    // values actual out
                    Vector vectActualOut = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]) != null) {
                            vectActualOut.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)])));
                        } else {
                            vectActualOut.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]));
                        }
                    }
                    presenceWeekly.setEmpOut(vectActualOut);

                    result.add(presenceWeekly);

                    //////////////////////////////////Tambahan di Hardrock
                    // values schedule
                    Vector vectSchedule2 = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        vectSchedule2.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                    }
                    presenceWeekly.setEmpSchedules2(vectSchedule2);

                    // values actual in
                    Vector vectActualIn2 = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]) != null) {
                            vectActualIn2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                        } else {
                            vectActualIn2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]));
                        }
                    }
                    presenceWeekly.setEmpIn2(vectActualIn2);

                    // values actual out
                    Vector vectActualOut2 = new Vector();
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]) != null) {
                            vectActualOut2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                        } else {
                            vectActualOut2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]));
                        }
                    }
                    presenceWeekly.setEmpOut2(vectActualOut2);
                    ////////////////////////////////Akhir tambahan di hardrock
                }



                // get in the second period
                Period period2 = null;
                try {
                    period2 = PstPeriod.fetchExc(periodId2);
                    Date startDatePer = period2.getStartDate();
                    intStartDate2 = startDatePer.getDate();
                } catch (Exception exc) {
                }

                sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                        + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME];

                // fields schedule
                for (int i = intStartDate2; i <= intEndDate2; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + (i - 1)];
                }

                // fields actual in
                for (int i = intStartDate2; i <= intEndDate2; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)];
                }

                // fields actual out
                for (int i = intStartDate2; i <= intEndDate2; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)];
                }

                ////////////////////Tambahan di hardrock
                // fields schedule 2nd
                for (int i = intStartDate2; i <= intEndDate2; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }

                //Tambahan di hardrock
                // fields actual in 2nd
                for (int i = intStartDate2; i <= intEndDate2; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }

                 //update by devin 2014-01-30
               
                 for (int i = intStartDate; i <= intEndDate; i++) {
                    sql +=  ", IF(((p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY) < p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+"),"
                            + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+ " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+") - "+ (PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY),"
                            + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+ " -  INTERVAL(DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1].substring(1))+") DAY)) AS SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1];
                }

                //Tambahan di hardrock
                // fields actual out 2nd
                for (int i = intStartDate2; i <= intEndDate2; i++) {
                    sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1];
                }


                sql += " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS SCH"
                        + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                        + " ON SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                        + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                        + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DEPT"
                        + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                         //update by devin 2014-01-30
                         + " INNER JOIN "+PstPeriod.TBL_HR_PERIOD + " AS p ON p."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] 
                        + "=SCH."+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                        + " WHERE SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                        + " = " + periodId2;
               //update by satrya 2012-09-26
                if (departmentId != 0) {
                    sql = sql + " AND DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                            + " = " + departmentId;
                }

                if (sectionId != 0) {
                    sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]
                            + " = " + sectionId;
                }
                    //update by satrya 2012-07-30

                     if (empNum != "" && !empNum.equals("")) {
                    sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                            + " = " + "\"" + empNum + "\"";
                }
                //update by satrya 2012-07-16
                if (fullName !="" && !fullName.equals("")) {
                    sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                            + " LIKE " + "\"%" + fullName + "%\"";
                }


                sql = sql + " ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];

                //System.out.println("\tlistNightShiftDataWeekly : "+sql);
                dbrs = DBHandler.execQueryResult(sql);
                rs = dbrs.getResultSet();
                int irs = 0;
                while (rs.next()) {
                    PresenceWeekly presenceWeekly = (PresenceWeekly) result.get(irs);
                    irs++;

                    //presenceWeekly.setEmpNum(rs.getString(1) != null ? rs.getString(1) : "");
                    //presenceWeekly.setEmpName(rs.getString(2) != null ? rs.getString(2) : "");

                    //update by devin 2014-01-30
                    for (int i = intStartDate; i <= intEndDate; i++) {
                        try{
                            presenceWeekly.addDayIdx(DBHandler.convertDate(rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i - 1]))); 
                        }catch(Exception exc){
                            
                        }
                        
                    }
                    // values schedule
                    Vector vectSchedule = presenceWeekly.getEmpSchedules();
                    for (int i = intStartDate2; i <= intEndDate2; i++) {
                        vectSchedule.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + (i - 1)])));
                    }
                    presenceWeekly.setEmpSchedules(vectSchedule);

                    // values actual in
                    Vector vectActualIn = presenceWeekly.getEmpIn();
                    for (int i = intStartDate2; i <= intEndDate2; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]) != null) {
                            vectActualIn.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)])));
                        } else {
                            vectActualIn.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + (i - 1)]));
                        }
                    }
                    presenceWeekly.setEmpIn(vectActualIn);

                    // values actual out
                    Vector vectActualOut = presenceWeekly.getEmpOut();
                    for (int i = intStartDate2; i <= intEndDate2; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]) != null) {
                            vectActualOut.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)])));
                        } else {
                            vectActualOut.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + (i - 1)]));
                        }
                    }
                    presenceWeekly.setEmpOut(vectActualOut);

                    //////////////////////////////////Tambahan di Hardrock
                    // values schedule
                    //update by satrya 2012-10-10
                    //Vector vectSchedule2 = new Vector();
                    Vector vectSchedule2 = presenceWeekly.getEmpSchedules2();
                    for (int i = intStartDate2; i <= intEndDate2; i++) {
                        vectSchedule2.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                    }
                    presenceWeekly.setEmpSchedules2(vectSchedule2);

                    // values actual in
                   // Vector vectActualIn2 = new Vector();
                      Vector vectActualIn2 = presenceWeekly.getEmpIn2();
                    for (int i = intStartDate2; i <= intEndDate2; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]) != null) {
                            vectActualIn2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                        } else {
                            vectActualIn2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]));
                        }
                    }
                    presenceWeekly.setEmpIn2(vectActualIn2);

                    // values actual out
                    //Vector vectActualOut2 = new Vector();
                    Vector vectActualOut2 = presenceWeekly.getEmpOut2();
                    for (int i = intStartDate2; i <= intEndDate2; i++) {
                        if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]) != null) {
                            vectActualOut2.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1])));
                        } else {
                            vectActualOut2.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + PstEmpSchedule.INTERVAL_INDEX_HALF_CALENDAR + i - 1]));
                        }
                    }
                    presenceWeekly.setEmpOut2(vectActualOut2);
                    ////////////////////////////////Akhir tambahan di hardrock

                    //result.add(presenceWeekly);
                }
            }


        } catch (Exception e) {
            System.out.println("listPresenceDataWeekly exc : " + e.toString());
            return result;
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    /**
     * Membuat menghitung perolehan Dp, AL, LL
     * Tambahan untuk hardrock
     * return vector :
     * 0 emp_id
     * 1 emp_num
     * 2 emp_name
     * 3 emp_period_id
     * 4 vector schedule
     * 5 vector schedule symbol
     * 6 dpStockManagement
     * 7 alStockManagement
     * 8 llStockManagement
     * 9 total present
     * 10 total dp taken
     * 11 total al taken
     * 12 total ll taken
     * 13 new Dp
     */
    public static Vector listAttendanceData(long oidDivision, long oidDepartment, long oidPeriod, int start, int recordToGet) {
        Vector vData = new Vector(1, 1);

        String strOid = "";
        strOid = PstSystemProperty.getValueByName("SYMBOL_SCHEDULE_ID");

        final int POS_DAY_OFF = 0;
        final int POS_EXTRA_OFF = 1;
        final int POS_DAY_PAYMENT = 2;
        final int POS_ANNUAL_LEAVE = 3;
        final int POS_LONG_LEAVE = 4;
        final int POS_PUBLIC_HOLIDAY = 5;
        final int POS_PATERNITY_LEAVE = 6;
        final int POS_MATERNITY_LEAVE = 7;
        final int POS_UNPAID_LEAVE = 8;
        final int POS_BEREAVEMENT_LEAVE = 9;
        final int POS_ABSENCE = 10;
        final int POS_PRESENT = 11;
        final int POS_PRESENT_IN_ONLY = 12;
        final int POS_PRESENT_OUT_ONLY = 13;

        final String[] fieldCode = {
            "X",
            "EO",
            "DP",
            "AL",
            "LL",
            "H",
            "PL",
            "ML",
            "UL",
            "BL",
            "A",
            "&radic;",
            "Vi",
            "Vo"
        };

        /*
        DO,
        EO(Extra Off),
        DP,
        AL,
        LL,
        H(Holiday),
        PL,
        ML,
        UL,
        BL
         */
        String strDO = PstSystemProperty.getValueByName("OID_DAY_OFF");
        String strEO = PstSystemProperty.getValueByName("OID_EXTRA_OFF");
        String strDP = PstSystemProperty.getValueByName("OID_DAY_OFF_PAYMENT");
        String strAL = PstSystemProperty.getValueByName("OID_AL_LEAVE");
        String strLL = PstSystemProperty.getValueByName("OID_LL_LEAVE");
        String strH = PstSystemProperty.getValueByName("OID_PUBLIC_HOLIDAY");
        String strPL = PstSystemProperty.getValueByName("OID_PATERNITY_LEAVE");
        String strML = PstSystemProperty.getValueByName("OID_MATERNITY_LEAVE");
        String strUL = PstSystemProperty.getValueByName("OID_UNPAID_LEAVE");
        String strBL = PstSystemProperty.getValueByName("OID_BREAVEMENT_LEAVE");

        Hashtable hSymbl = new Hashtable();
        hSymbl.put(strDO, fieldCode[POS_DAY_OFF]);
        hSymbl.put(strEO, fieldCode[POS_EXTRA_OFF]);
        hSymbl.put(strDP, fieldCode[POS_DAY_PAYMENT]);
        hSymbl.put(strAL, fieldCode[POS_ANNUAL_LEAVE]);
        hSymbl.put(strLL, fieldCode[POS_LONG_LEAVE]);
        hSymbl.put(strH, fieldCode[POS_PUBLIC_HOLIDAY]);
        hSymbl.put(strPL, fieldCode[POS_PATERNITY_LEAVE]);
        hSymbl.put(strML, fieldCode[POS_MATERNITY_LEAVE]);
        hSymbl.put(strUL, fieldCode[POS_UNPAID_LEAVE]);
        hSymbl.put(strBL, fieldCode[POS_BEREAVEMENT_LEAVE]);

        Vector vDp = new Vector(1, 1);
        Vector vAl = new Vector(1, 1);
        Vector vLl = new Vector(1, 1);

        Hashtable hDp = new Hashtable();
        Hashtable hAl = new Hashtable();
        Hashtable hLl = new Hashtable();

        //Dp,AL,LL yang diambil adalah yang pada periode sebelumnya
        //Mencari oid periode sebelumnya
        Period periodPrev = new Period();
        periodPrev = PstPeriod.getPreviousPeriod(oidPeriod);

        SrcLeaveManagement srcLeaveManagement = new SrcLeaveManagement();
        srcLeaveManagement.setEmpDeptId(oidDepartment);
        srcLeaveManagement.setPeriodId(periodPrev.getOID()); //Menggunakan prev period, digunakan leave periode pada proses

        vDp = SessLeaveManagement.listSummaryDpStockValid(srcLeaveManagement, 0, 0);
        for (int i = 0; i < vDp.size(); i++) {
            Vector ls = (Vector) vDp.get(i);
            DpStockManagement dpStockManagement = (DpStockManagement) ls.get(0);
            hDp.put(String.valueOf(dpStockManagement.getEmployeeId()), dpStockManagement);
        }

        srcLeaveManagement.setLeavePeriod(periodPrev.getEndDate());
        srcLeaveManagement.setPeriodChecked(false);
        vAl = SessLeaveManagement.listSummaryAlStockIntOnMan(srcLeaveManagement, 0, 0);
        for (int i = 0; i < vAl.size(); i++) {
            Vector ls = (Vector) vAl.get(i);
            AlStockManagement alStockMan = (AlStockManagement) ls.get(0);
            hAl.put(String.valueOf(alStockMan.getEmployeeId()), alStockMan);
        }
        srcLeaveManagement.setPeriodChecked(true);
        vLl = SessLeaveManagement.listSummaryLlStockValid(srcLeaveManagement, 0, 0);
        for (int i = 0; i < vLl.size(); i++) {
            Vector ls = (Vector) vLl.get(i);
            LLStockManagement llStockManagement = (LLStockManagement) ls.get(0);
            hLl.put(String.valueOf(llStockManagement.getEmployeeId()), llStockManagement);
        }

        Vector vDpEntitle = new Vector();
        Hashtable hDpEntitled = new Hashtable();
        SrcEmployee srcEmp = new SrcEmployee();
        srcEmp.setDivisionId(oidDivision);
        srcEmp.setDepartment(oidDepartment);
        vDpEntitle = SessDayOfPayment.getDpEntitledAtPeriod(srcEmp, oidPeriod);
        for (int i = 0; i < vDpEntitle.size(); i++) {
            Vector dpNew = (Vector) vDpEntitle.get(i);
            long empOid = Long.parseLong((String) dpNew.get(0));
            int iDp = Integer.parseInt((String) dpNew.get(1));
            hDpEntitled.put(String.valueOf(empOid), String.valueOf(iDp));
        }

        Vector vPresenceMountly = new Vector(1, 1);
        vPresenceMountly = listAttendanceSummaryList(oidDivision, oidDepartment, oidPeriod, start, recordToGet);
        for (int i = 0; i < vPresenceMountly.size(); i++) {
            Vector vTemp = new Vector(1, 1);
            PresenceMonthly presenceMonthly = (PresenceMonthly) vPresenceMountly.get(i);
            String empNum = presenceMonthly.getEmpNum();
            String empName = presenceMonthly.getEmpName();
            Employee emp = new Employee();
            emp = PstEmployee.getEmployeeByNum(empNum);

            Vector vEmpSchedules = presenceMonthly.getEmpSchedules();
            Vector vEmpActualIn = presenceMonthly.getEmpIn();
            Vector vEmpActualOut = presenceMonthly.getEmpOut();

            //Menyimpan ke penampung
            vTemp.add(String.valueOf(emp.getOID()));
            vTemp.add(String.valueOf(empNum));
            vTemp.add(String.valueOf(empName));
            vTemp.add(String.valueOf(oidPeriod));

            Vector vSymbolSchedule = new Vector(1, 1);
            //Mengecek schedule yang sesuai dengan sechedule yang ada
            int totalPresence = 0;
            int totalDp = 0;
            int totalAl = 0;
            int totalLl = 0;
            for (int isch = 0; isch < vEmpSchedules.size(); isch++) {
                long PresenceScheduleId = Long.parseLong(String.valueOf(vEmpSchedules.get(isch)));
                //String schldSymbol = PstScheduleSymbol.getScheduleSymbol(PresenceScheduleId);
                if (PresenceScheduleId > 0) {
                    String strDuration = "";
                    Date dateActualIn = (Date) vEmpActualIn.get(isch);
                    Date dateActualOut = (Date) vEmpActualOut.get(isch);

                    if (dateActualIn != null || dateActualOut != null) {
                        if (dateActualIn != null && dateActualOut != null) {
                            long iDuration = dateActualOut.getTime() / 60000 - dateActualIn.getTime() / 60000;
                            long iDurationHour = (iDuration - (iDuration % 60)) / 60;
                            long iDurationMin = iDuration % 60;
                            String strDurationHour = iDurationHour + "h, ";
                            String strDurationMin = iDurationMin + "m";
                            //strDuration = strDurationHour + strDurationMin;
                            //presenceNull += 1;

                            if (strDuration != null && strDuration.length() > 0) {
                                totalPresence += 1;
                            }
                            strDuration = fieldCode[POS_PRESENT];
                        } else if (dateActualIn != null) {
                            totalPresence += 1;
                            strDuration = fieldCode[POS_PRESENT_IN_ONLY];
                        } else {
                            totalPresence += 1;
                            strDuration = fieldCode[POS_PRESENT_OUT_ONLY];
                        }
                    } else {
                        //Mengetahui alasan tidak hadir
                        if (hSymbl.containsKey(String.valueOf(PresenceScheduleId))) {
                            strDuration = (String) hSymbl.get(String.valueOf(PresenceScheduleId));
                            if (strDuration.equals(fieldCode[POS_DAY_PAYMENT])) {
                                totalDp += 1;
                            } else if (strDuration.equals(fieldCode[POS_ANNUAL_LEAVE])) {
                                totalAl += 1;
                            } else if (strDuration.equals(fieldCode[POS_LONG_LEAVE])) {
                                totalLl += 1;
                            }
                        } else {
                            strDuration = fieldCode[POS_ABSENCE];
                        }
                    }
                    vSymbolSchedule.add(strDuration);

                } else {
                    vSymbolSchedule.add("");
                }
            }
            vTemp.add(vEmpSchedules);
            vTemp.add(vSymbolSchedule);
            if (hDp.get(String.valueOf(emp.getOID())) != null) {
                vTemp.add(hDp.get(String.valueOf(emp.getOID())));
            } else {
                vTemp.add(new DpStockManagement());
            }
            if (hAl.get(String.valueOf(emp.getOID())) != null) {
                vTemp.add(hAl.get(String.valueOf(emp.getOID())));
            } else {
                vTemp.add(new AlStockManagement());
            }
            if (hLl.get(String.valueOf(emp.getOID())) != null) {
                vTemp.add(hLl.get(String.valueOf(emp.getOID())));
            } else {
                vTemp.add(new LLStockManagement());
            }
            vTemp.add(String.valueOf(totalPresence));
            vTemp.add(String.valueOf(totalDp));
            vTemp.add(String.valueOf(totalAl));
            vTemp.add(String.valueOf(totalLl));
            vTemp.add(hDpEntitled.get(String.valueOf(emp.getOID())));
            vData.add(vTemp);

        }

        return vData;
    }

    /**
     * list emp attendance summary sheet
     */
    public static Vector listAttendanceSummaryList(long oidDivision, long oidDepartment, long oidPeriod, int start, int recordToGet) {
        Vector vList = new Vector();
        //========================= LIST ATTENDANCE SUMMARY ============================
        Vector result = new Vector(1, 1);
        DBResultSet dbrs = null;

        // get maximum date on selected month
        Period period = new Period();
        try {
            period = PstPeriod.fetchExc(oidPeriod);
        } catch (Exception ex) {
            System.out.println("[ERROR] SessPresence.listAttendanceSummaryList ::::::: " + ex.toString());
        }
        GregorianCalendar calenderWeek = new GregorianCalendar(period.getStartDate().getYear() + 1900, period.getStartDate().getMonth(), 1);
        int maxDayOnSelectedMonth = calenderWeek.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

        // get periodId of selected date
        long periodId = oidPeriod;

        try {
            String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                    + ", EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME];

            // fields schedule
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i];
            }

            // fields actual in
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i];
            }

            // fields actual out
            for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                sql += ", SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i];
            }

            sql += " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS SCH"
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + " ON SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                    + " = " + periodId;
            if (oidDepartment != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + oidDepartment;
            }
            if (oidDivision != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + oidDivision;
            }

            sql = sql + " ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];

            if (start > 0 || recordToGet > 0) {
                sql = sql + " LIMIT " + start + "," + recordToGet;
            }

            System.out.println("\tSessPresence.listAttendanceSummaryList: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PresenceMonthly presenceMonthly = new PresenceMonthly();

                presenceMonthly.setEmpNum(rs.getString(1) != null ? rs.getString(1) : "");
                presenceMonthly.setEmpName(rs.getString(2) != null ? rs.getString(2) : "");

                // values schedule
                Vector vectSchedule = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    vectSchedule.add(String.valueOf(rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + i])));
                }
                presenceMonthly.setEmpSchedules(vectSchedule);

                // values actual in
                Vector vectActualIn = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i]) != null) {
                        vectActualIn.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i])));
                    } else {
                        vectActualIn.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN + i]));
                    }
                }
                presenceMonthly.setEmpIn(vectActualIn);

                // values actual out
                Vector vectActualOut = new Vector();
                for (int i = 0; i < maxDayOnSelectedMonth; i++) {
                    if (rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i]) != null) {
                        vectActualOut.add(DBHandler.convertDate(rs.getDate(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i]), rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i])));
                    } else {
                        vectActualOut.add(rs.getTime(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT + i]));
                    }
                }
                presenceMonthly.setEmpOut(vectActualOut);

                result.add(presenceMonthly);
            }
        } catch (Exception e) {
            System.out.println("listPresenceDataMonthly exc : " + e.toString());
            return result;
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
        //========================= END ATTENDANCE SUMMARY =============================
        //return vList;
    }

    /**
     * list emp attendance summary sheet
     */
    public static int countAttendanceSummaryList(long oidDivision, long oidDepartment, long oidPeriod) {
        //========================= LIST ATTENDANCE SUMMARY ============================
        int iResult = 0;
        DBResultSet dbrs = null;

        // get periodId of selected date
        long periodId = oidPeriod;

        try {
            String sql = "SELECT COUNT(EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + ")";
            sql += " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS SCH"
                    + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP"
                    + " ON SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                    + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " WHERE SCH." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                    + " = " + periodId;
            if (oidDepartment != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]
                        + " = " + oidDepartment;
            }
            if (oidDivision != 0) {
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]
                        + " = " + oidDivision;
            }

            sql = sql + " ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];

            System.out.println("\tSessPresence.countAttendanceSummaryList: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                iResult = rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("countAttendanceSummaryList exc : " + e.toString());
            return iResult;
        } finally {
            DBResultSet.close(dbrs);
            return iResult;
        }
        //========================= END ATTENDANCE SUMMARY =============================
        //return vList;
    }

    public static void main(String[] args) {
        /*
        Date dt = new Date(103, 2, 3, 23, 0);
        System.out.println(dt);
        System.out.println(dt.getTime());
        long ln = 1046703600000L + 4080000;
        Date mm = new Date(ln);
        System.out.println(">>>>>>>" + mm);
        dt = new Date(103, 2, 4, 0, 0);
        System.out.println(dt);
        System.out.println(dt.getTime());
        long iDuration = 1046707680000L / 60000 - 1046703600000L / 60000;
        System.out.println("iDuration " + iDuration);
        long iDurationHour = (iDuration - (iDuration % 60)) / 60;
        long iDurationMin = iDuration % 60;
        String strDurationHour = iDurationHour + "h, ";
        String strDurationMin = iDurationMin + "m ";
        System.out.println(strDurationHour + strDurationMin);
         * */
        /*String msg = fixAttendanceRecords(0, 0, 120, 120);
        System.out.println(msg); */
        Date dDate = new Date(108, 1, 23);

        //update by devin 2014-01-28 Vector listPresence = listPresenceDataMonthly(2001, dDate, 200101,"","");
        Vector listPresence = listPresenceDataMonthly(2001,0,0, dDate, 200101,"","");
        if (listPresence != null) {
            for (int i = 0; i < listPresence.size(); i++) {
                System.out.println(listPresence);
            }
        }
    }

    public static int CountYearPresence(long departmentId, int year) {

        DBResultSet dbrs = null;

        try {

            String sql = "SELECT COUNT(PRD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMP_SCHEDULE_ID] + ") "
                    + " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS EMPSCD INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP ON EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN " + PstPeriod.TBL_HR_PERIOD
                    + " AS PRD ON EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID] + "= PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId
                    + " AND YEAR(PRD." + PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ") = " + year
                    + " ORDER PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while (rs.next()) {

                return rs.getInt(1);

            }


        } catch (Exception E) {
            System.out.println("[excp] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }
        return 0;

    }

    public static Vector employeeYearPresence(long departmentId,long divisionId,long companyId, long sectionId, int year,String empNumb,String fullName) {

        DBResultSet dbrs = null;

        /* Mengambil setingan system property resin yang akan di tampilkan pada report tahunan */
        String reasonProperty = PstSystemProperty.getValueByName("REASON_PROPERTY");

        String reasonRpt = "";

        if (reasonProperty.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {

            Vector reason = com.dimata.util.StringParser.parseGroup(reasonProperty);
            String[] strReason = (String[]) reason.get(0);

            if (strReason.length > 0) {

                reasonRpt = reasonRpt + " ( ";

                for (int g = 0; g < strReason.length; g++) {

                    if (g == 0) {
                        reasonRpt = PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    } else {
                        reasonRpt = " AND " + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    }
                }
                reasonRpt = reasonRpt + " ) ";
            }
        }

        try {

            String sql = "SELECT DISTINCT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " as empId ,"+
                    " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+" as empNum ,"+
                    " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+" as fullName ,"+
                    " DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]+" as depId ,"+
                    " DEPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]+" as dep "
                    + " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS EMPSCD INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP ON EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN " + PstPeriod.TBL_HR_PERIOD
                    + " AS PRD ON EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID] + "= PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE YEAR(PRD." + PstPeriod.fieldNames[PstPeriod.FLD_END_DATE] + ") = " + year+" AND EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+"="+PstEmployee.NO_RESIGN;

            if(departmentId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
            }

            if(sectionId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
            }
            //update by devin 2014-01-28
            if(companyId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + companyId;
            }
            if(divisionId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + divisionId;
            }
            
                       //update by satrya 2012-07-30

                 if (empNumb != null && empNumb.length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                         + " LIKE " + "\"%" + empNumb + "%\"";
            }
            //update by satrya 2012-07-16
            if (fullName !=null && fullName.length() > 0) {
                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                        + " LIKE " + "\"%" + fullName + "%\"";
            }
            sql = sql + " ORDER BY PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + ",EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];

            //System.out.println("\tEMPLOYEE REPORT YEAR PRESENCE: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector listRpt = new Vector();

            while(rs.next()){
                YearPresence yearPresence = new YearPresence();
                yearPresence.setEmployeeId(rs.getLong("empId"));
                yearPresence.setEmployeeNum(rs.getString("empNum"));
                yearPresence.setFullName(rs.getString("fullName"));
                yearPresence.setDepId(rs.getLong("depId"));
                yearPresence.setDep(rs.getString("dep"));
                listRpt.add(yearPresence);

            }
            return listRpt;

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally{
            DBResultSet.close(dbrs);
        }
        return null;
    }
/**
     * Create By satrya 2012-12-27
     * @param departmentId
     * @param sectionId
     * @param empNumb
     * @param fullName
     * @param fromDate
     * @param toDate
     * @return 
     */
    public static Vector employeeAttendance(long oidCompany,long oidDivision,long departmentId, long sectionId,String empNumb,String fullName,Date fromDate, Date toDate) {

        DBResultSet dbrs = null;

    
        try {

             String sql = "SELECT DISTINCT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " AS EMP_OID, EMP." +
                    PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " AS EMP_NUM, EMP." + 
                    PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " AS EMP_NAME, " + 
                    " DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION]+" AS DIVISION," +
                    " DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " AS DEPARTEMENT,"+
                    " SEC."+PstSection.fieldNames[PstSection.FLD_SECTION] + " AS SECTION ," +
                    " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " AS EMP_DEP_ID " + 
                    //" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " AS EMP_SEC_ID," + 
             /*       " SUM(DP." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ") AS DP_QTY, SUM(DP." + 
                    PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_USED] + ") AS DP_TAKEN, DT.DT_TB_TKN AS DP_TB_TKN, " + 
                    " SUM(AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ") AS AL_QTY, SUM(AL." + 
                    PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ") AS AL_PREV, SUM(AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ") AS AL_TAKEN, AT.AL_TB_TKN AS AL_TB_TKN, " + 
                    " (LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + ") AS LL_QTY, (LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + ") AS LL_PREV, (LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + ") AS LL_TAKEN, LT.LL_TB_TKN AS LL_TB_TKN, EXPD.EXPD_QTY AS LL_EXPIRED " + */
                    " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP " 
                   /*+ " LEFT JOIN hr_view_dp AS DP ON DP." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " LEFT JOIN sum_dp_tb_taken_by_emp AS DT ON DT.EMPLOYEE_ID = EMP.EMPLOYEE_ID " +
                    " LEFT JOIN " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " AS AL ON AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " AND AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = '" + PstAlStockManagement.AL_STS_AKTIF + "' " + " LEFT JOIN sum_al_tb_taken_by_emp AS AT ON AT.EMPLOYEE_ID = EMP.EMPLOYEE_ID" + " LEFT JOIN " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " AS LL ON LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " AND LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = '" + PstLLStockManagement.LL_STS_AKTIF + "' " + " LEFT JOIN sum_ll_tb_taken_by_emp AS LT ON LT.EMPLOYEE_ID = EMP.EMPLOYEE_ID" +
                    " LEFT JOIN hr_view_sum_stock_expired EXPD on EXPD." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] +*/
                    + " INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT + " AS DPT ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+ " =DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
                      " INNER JOIN "+PstDivision.TBL_HR_DIVISION + " AS DIVX ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+ " = DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +
                      " LEFT JOIN  "+PstSection.TBL_HR_SECTION + " AS SEC ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] +" = SEC."+PstSection.fieldNames[PstSection.FLD_SECTION_ID] + 
                      " WHERE (1=1)" ;
            if(fromDate!=null && toDate!=null){
                    sql= sql + "  AND (( EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
                    + " = " +PstEmployee.YES_RESIGN + " AND " + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
                    + " BETWEEN \"" + Formater.formatDate(fromDate, "yyyy-MM-dd  00:00:00")+ "\""  +" AND "+ "\"" + Formater.formatDate(toDate, "yyyy-MM-dd  23:59:59")+ "\"" 
                    + " ) OR (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
                    + " = " + PstEmployee.NO_RESIGN + "))";
                } 
            if(departmentId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
            }
            
            //update by satrya 2014-01-20
            if(oidCompany != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + oidCompany;
            }
            
             if(oidDivision != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + oidDivision;
            }

            if(sectionId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
            }
            
                            if(departmentId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
                    }

                    if(sectionId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
                    }
            if (empNumb != null && empNumb != "") {
                Vector vectNum = logicParser(empNumb);
                sql = sql + " AND ";
                if (vectNum != null && vectNum.size() > 0) {
                    sql = sql + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + ")";
                }
                
            }
            if (fullName != null && fullName != "") {
                Vector vectFullName = logicParser(fullName);
                  sql = sql + " AND ";
                if (vectFullName != null && vectFullName.size() > 0) {
                    sql = sql + " ( ";
                    for (int i = 0; i < vectFullName.size(); i++) {
                        String str = (String) vectFullName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+ PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + " ) ";
                }
                 
            }
             sql += " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            sql += " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];
           
            
            

            //System.out.println("\tEMPLOYEE REPORT YEAR PRESENCE: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector listRpt = new Vector();

            while(rs.next()){
                
                    SummaryEmployeeAttendance summaryEmployeeAttendance = new SummaryEmployeeAttendance();
                    summaryEmployeeAttendance.setEmployeeId(rs.getLong("EMP_OID"));
                    summaryEmployeeAttendance.setEmployeeNum(rs.getString("EMP_NUM"));
                    summaryEmployeeAttendance.setFullName(rs.getString("EMP_NAME"));
                    summaryEmployeeAttendance.setDivision(rs.getString("DIVISION"));
                    summaryEmployeeAttendance.setDepartment(rs.getString("DEPARTEMENT"));
                    summaryEmployeeAttendance.setSection(rs.getString("SECTION"));
                    
                    summaryEmployeeAttendance.setDepartmentId(rs.getLong("EMP_DEP_ID"));
                    //attendanceSummary.setSection(rs.getLong("EMP_SEC_ID"));

                    /*summaryEmployeeAttendance.setDPQty(rs.getFloat("DP_QTY"));
                    summaryEmployeeAttendance.setDPTaken(rs.getFloat("DP_TAKEN"));
                    summaryEmployeeAttendance.setDP2BTaken(rs.getFloat("DP_TB_TKN"));

                    summaryEmployeeAttendance.setALPrev(rs.getFloat("AL_PREV"));
                    summaryEmployeeAttendance.setALQty(rs.getFloat("AL_QTY"));
                    summaryEmployeeAttendance.setALTaken(rs.getFloat("AL_TAKEN"));
                    summaryEmployeeAttendance.setAL2BTaken(rs.getFloat("AL_TB_TKN"));

                    summaryEmployeeAttendance.setLLPrev(rs.getFloat("LL_PREV"));
                    summaryEmployeeAttendance.setLLQty(rs.getFloat("LL_QTY"));
                    summaryEmployeeAttendance.setLLTaken(rs.getFloat("LL_TAKEN"));
                    summaryEmployeeAttendance.setLL2BTaken(rs.getFloat("LL_TB_TKN"));
                    summaryEmployeeAttendance.setLLExpdQty(rs.getFloat("LL_EXPIRED"));*/

                listRpt.add(summaryEmployeeAttendance);

            }
            return listRpt;

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally{
            DBResultSet.close(dbrs);
        }
        return null;
    }
    
    
    /**
     * Create By Gunadi 2017-11-30
     * @param departmentId
     * @param sectionId
     * @param empNumb
     * @param fullName
     * @param fromDate
     * @param toDate
     * @return 
     */
    public static Vector employeeAttendanceWithResignStatus(long oidCompany,long oidDivision,
            long departmentId, long sectionId,String empNumb,String fullName,Date fromDate, 
            Date toDate, int statusResign, long payrollGroupId, long empCategoryId) {

        DBResultSet dbrs = null;

    
        try {

             String sql = "SELECT DISTINCT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " AS EMP_OID, EMP." +
                    PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " AS EMP_NUM, EMP." + 
                    PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " AS EMP_NAME, " + 
                    " DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION]+" AS DIVISION," +
                    " DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " AS DEPARTEMENT,"+
                    " SEC."+PstSection.fieldNames[PstSection.FLD_SECTION] + " AS SECTION ," +
                    " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " AS EMP_DEP_ID " + 
                    //" EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " AS EMP_SEC_ID," + 
             /*       " SUM(DP." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_DP_QTY] + ") AS DP_QTY, SUM(DP." + 
                    PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_QTY_USED] + ") AS DP_TAKEN, DT.DT_TB_TKN AS DP_TB_TKN, " + 
                    " SUM(AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_QTY] + ") AS AL_QTY, SUM(AL." + 
                    PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_PREV_BALANCE] + ") AS AL_PREV, SUM(AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_QTY_USED] + ") AS AL_TAKEN, AT.AL_TB_TKN AS AL_TB_TKN, " + 
                    " (LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_QTY] + ") AS LL_QTY, (LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_PREV_BALANCE] + ") AS LL_PREV, (LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_QTY_USED] + ") AS LL_TAKEN, LT.LL_TB_TKN AS LL_TB_TKN, EXPD.EXPD_QTY AS LL_EXPIRED " + */
                    " FROM " + PstEmployee.TBL_HR_EMPLOYEE + " AS EMP " 
                   /*+ " LEFT JOIN hr_view_dp AS DP ON DP." + PstDpStockManagement.fieldNames[PstDpStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " LEFT JOIN sum_dp_tb_taken_by_emp AS DT ON DT.EMPLOYEE_ID = EMP.EMPLOYEE_ID " +
                    " LEFT JOIN " + PstAlStockManagement.TBL_AL_STOCK_MANAGEMENT + " AS AL ON AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " AND AL." + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_AL_STATUS] + " = '" + PstAlStockManagement.AL_STS_AKTIF + "' " + " LEFT JOIN sum_al_tb_taken_by_emp AS AT ON AT.EMPLOYEE_ID = EMP.EMPLOYEE_ID" + " LEFT JOIN " + PstLLStockManagement.TBL_LL_STOCK_MANAGEMENT + " AS LL ON LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_EMPLOYEE_ID] + " = EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " AND LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STATUS] + " = '" + PstLLStockManagement.LL_STS_AKTIF + "' " + " LEFT JOIN sum_ll_tb_taken_by_emp AS LT ON LT.EMPLOYEE_ID = EMP.EMPLOYEE_ID" +
                    " LEFT JOIN hr_view_sum_stock_expired EXPD on EXPD." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] + "=LL." + PstLLStockManagement.fieldNames[PstLLStockManagement.FLD_LL_STOCK_ID] +*/
                    + " INNER JOIN "+PstDepartment.TBL_HR_DEPARTMENT + " AS DPT ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+ " =DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] +
                      " INNER JOIN "+PstDivision.TBL_HR_DIVISION + " AS DIVX ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+ " = DIVX."+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] +
                      " LEFT JOIN  "+PstSection.TBL_HR_SECTION + " AS SEC ON EMP."+PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] +" = SEC."+PstSection.fieldNames[PstSection.FLD_SECTION_ID] + 
                      " WHERE (1=1)" ;
//            if(fromDate!=null && toDate!=null){
//                    sql= sql + "  AND (( EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " +PstEmployee.YES_RESIGN + " AND " + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
//                    + " BETWEEN \"" + Formater.formatDate(fromDate, "yyyy-MM-dd  00:00:00")+ "\""  +" AND "+ "\"" + Formater.formatDate(toDate, "yyyy-MM-dd  23:59:59")+ "\"" 
//                    + " ) OR (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " + PstEmployee.NO_RESIGN + "))";
//                } 
             
            if (statusResign == PstEmployee.YES_RESIGN) {
                sql = sql + " AND (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.YES_RESIGN
                        + " AND EMP."+ PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+ " BETWEEN '" + Formater.formatDate(fromDate, "yyyy-MM-dd")+"'"
                        + " AND '" + Formater.formatDate(toDate, "yyyy-MM-dd")+"')";
            } else if (statusResign == PstEmployee.NO_RESIGN){
                sql = sql + " AND (EMP." + PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] + " = " + PstEmployee.NO_RESIGN
                        + " OR EMP."+ PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE] +" >= '" + Formater.formatDate(fromDate, "yyyy-MM-dd")+"')";
            }
             
            if(departmentId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
            }
            
            //update by satrya 2014-01-20
            if(oidCompany != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + oidCompany;
            }
            
             if(oidDivision != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + oidDivision;
            }

            if(sectionId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
            }
            
//            if(departmentId != 0){
//                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
//            }
//
//            if(sectionId != 0){
//                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
//            }
            
            if (payrollGroupId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP] + " = " + payrollGroupId;
            }
            if (empCategoryId != 0){
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " = " + empCategoryId;
            }
            
            if (empNumb != null && empNumb != "") {
                Vector vectNum = logicParser(empNumb);
                sql = sql + " AND ";
                if (vectNum != null && vectNum.size() > 0) {
                    sql = sql + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + ")";
                }
                
            }
            if (fullName != null && fullName != "") {
                Vector vectFullName = logicParser(fullName);
                  sql = sql + " AND ";
                if (vectFullName != null && vectFullName.size() > 0) {
                    sql = sql + " ( ";
                    for (int i = 0; i < vectFullName.size(); i++) {
                        String str = (String) vectFullName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+ PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + " ) ";
                }
                 
            }
             sql += " GROUP BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID];
            sql += " ORDER BY EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM];
           
            
            

            //System.out.println("\tEMPLOYEE REPORT YEAR PRESENCE: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector listRpt = new Vector();

            while(rs.next()){
                
                    SummaryEmployeeAttendance summaryEmployeeAttendance = new SummaryEmployeeAttendance();
                    summaryEmployeeAttendance.setEmployeeId(rs.getLong("EMP_OID"));
                    summaryEmployeeAttendance.setEmployeeNum(rs.getString("EMP_NUM"));
                    summaryEmployeeAttendance.setFullName(rs.getString("EMP_NAME"));
                    summaryEmployeeAttendance.setDivision(rs.getString("DIVISION"));
                    summaryEmployeeAttendance.setDepartment(rs.getString("DEPARTEMENT"));
                    summaryEmployeeAttendance.setSection(rs.getString("SECTION"));
                    
                    summaryEmployeeAttendance.setDepartmentId(rs.getLong("EMP_DEP_ID"));
                    //attendanceSummary.setSection(rs.getLong("EMP_SEC_ID"));

                    /*summaryEmployeeAttendance.setDPQty(rs.getFloat("DP_QTY"));
                    summaryEmployeeAttendance.setDPTaken(rs.getFloat("DP_TAKEN"));
                    summaryEmployeeAttendance.setDP2BTaken(rs.getFloat("DP_TB_TKN"));

                    summaryEmployeeAttendance.setALPrev(rs.getFloat("AL_PREV"));
                    summaryEmployeeAttendance.setALQty(rs.getFloat("AL_QTY"));
                    summaryEmployeeAttendance.setALTaken(rs.getFloat("AL_TAKEN"));
                    summaryEmployeeAttendance.setAL2BTaken(rs.getFloat("AL_TB_TKN"));

                    summaryEmployeeAttendance.setLLPrev(rs.getFloat("LL_PREV"));
                    summaryEmployeeAttendance.setLLQty(rs.getFloat("LL_QTY"));
                    summaryEmployeeAttendance.setLLTaken(rs.getFloat("LL_TAKEN"));
                    summaryEmployeeAttendance.setLL2BTaken(rs.getFloat("LL_TB_TKN"));
                    summaryEmployeeAttendance.setLLExpdQty(rs.getFloat("LL_EXPIRED"));*/

                listRpt.add(summaryEmployeeAttendance);

            }
            return listRpt;

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally{
            DBResultSet.close(dbrs);
        }
        return null;
    }    

    
    
    /**
     * @Author  Roy Andika
     * @param   departmentId
     * @param   year
     * @Desc    Untuk mendapatkan report presence tahunan
     * @return
     */
    public static Vector yearPresence(long departmentId,long divisionId,long companyId, long sectionId, int year,String empNumb,String fullName) {

        DBResultSet dbrs = null;

        /* Mengambil setingan system property reason yang tidak akan di tampilkan pada report tahunan */
        String reasonProperty = PstSystemProperty.getValueByName("REASON_PROPERTY");

        String reasonRpt = "";

        if (reasonProperty.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {

            Vector reason = com.dimata.util.StringParser.parseGroup(reasonProperty);
            String[] strReason = (String[]) reason.get(0);

            if (strReason.length > 0) {

                reasonRpt = reasonRpt + " ( ";

                for (int g = 0; g < strReason.length; g++) {

                    if (g == 0) {
                        reasonRpt = reasonRpt + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    } else {
                        reasonRpt = reasonRpt + " AND " + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    }
                }

                reasonRpt = reasonRpt + " ) ";
            }

        }

        Vector listReason = new Vector();

        try {

            listReason = PstReason.list(0, 0, reasonRpt, null);

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        }

        try {

            String sql = "SELECT PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + " as periodId,"
                    + " PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD] + " as period,"
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " as empId,"
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " as empNum,"
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " as fullName,"
                    + " DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " as depId,"
                    + " DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " as dep,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMP_SCHEDULE_ID] + ","
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS1] + " as status1 ,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS2] + " as status2,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS3] + " as status3,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS4] + " as status4,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS5] + " as status5,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS6] + " as status6,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS7] + " as status7,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS8] + " as status8,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS9] + " as status9,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS10] + " as status10,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS11] + " as status11,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS12] + " as status12,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS13] + " as status13,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS14] + " as status14,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS15] + " as status15,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS16] + " as status16,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS17] + " as status17,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS18] + " as status18,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS19] + " as status19,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS20] + " as status20,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS21] + " as status21,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS22] + " as status22,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS23] + " as status23,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS24] + " as status24,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS25] + " as status25,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS26] + " as status26,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS27] + " as status27,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS28] + " as status28,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS29] + " as status29,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS30] + " as status30,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_STATUS31] + " as status31,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON1] + " as reason1,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON2] + " as reason2,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON3] + " as reason3,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON4] + " as reason4,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON5] + " as reason5,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON6] + " as reason6,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON7] + " as reason7,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON8] + " as reason8,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON9] + " as reason9,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON10] + " as reason10,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON11] + " as reason11,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON12] + " as reason12,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON13] + " as reason13,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON14] + " as reason14,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON15] + " as reason15,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON16] + " as reason16,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON17] + " as reason17,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON18] + " as reason18,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON19] + " as reason19,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON20] + " as reason20,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON21] + " as reason21,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON22] + " as reason22,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON23] + " as reason23,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON24] + " as reason24,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON25] + " as reason25,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON26] + " as reason26,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON27] + " as reason27,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON28] + " as reason28,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON29] + " as reason29,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON30] + " as reason30,"
                    + " EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_REASON31] + " as reason31 "
                    + " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS EMPSCD INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE
                    + " EMP ON EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID] + " = EMP."
                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " INNER JOIN " + PstPeriod.TBL_HR_PERIOD
                    + " AS PRD ON EMPSCD." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID] + "= PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID]
                    + " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
                    + " WHERE YEAR(PRD." + PstPeriod.fieldNames[PstPeriod.FLD_END_DATE] + ") = " + year+" AND EMP."+
                    PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+"="+PstEmployee.NO_RESIGN;

                    if(departmentId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
                    }
                    if(divisionId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = " + divisionId;
                    }
                    if(companyId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + " = " + companyId;
                    }
                    if(sectionId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
                    }

                       //update by devin 2014-01-30

                 if (empNumb != null && empNumb != "") {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                        + " = " + "\"" + empNumId.trim() + "\"";//penambahan trima
                Vector vectNum = logicParser(empNumb);
               
                if (vectNum != null && vectNum.size() > 0) {
                     sql = sql + " AND ";
                    sql = sql + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
            }
            }
                    sql = sql + ")";
                }

            }
            //update by devin 2014-01-30
            if (fullName != null && fullName != "") {
//                sql = sql + " AND " + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
//                        + " LIKE " + "\"%" + fullName.trim() + "%\"";//penambahan trim
                Vector vectFullName = logicParser(fullName);
                 
                if (vectFullName != null && vectFullName.size() > 0) {
                     sql = sql + " AND ";
                    sql = sql + " ( ";
                    for (int i = 0; i < vectFullName.size(); i++) {
                        String str = (String) vectFullName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+ PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + " ) ";
                }
                 
            }
                    sql = sql + "  ORDER BY EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+",PRD." + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID];

            //System.out.println("\tREPORT YEAR PRESENCE: " + sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector listRpt = new Vector();

            while (rs.next()) {

                int[] val = new int[listReason.size()];

                YearPresence yearPresence = new YearPresence();

                yearPresence.setEmployeeId(rs.getLong("empId"));
                yearPresence.setFullName(rs.getString("fullName"));
                yearPresence.setEmployeeNum(rs.getString("empNum"));

                yearPresence.setPeriodId(rs.getLong("periodId"));
                yearPresence.setPeriod(rs.getString("period"));

                yearPresence.setDepId(rs.getLong("depId"));
                yearPresence.setDep(rs.getString("dep"));

                if (rs.getInt("reason1") > -1 && rs.getInt("status1")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD1 = 0; iD1 < listReason.size(); iD1++) {

                        Reason ReasonD1 = (Reason) listReason.get(iD1);

                        if (rs.getInt("reason1") == ReasonD1.getNo()) {
                            val[iD1]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason2") > -1 && rs.getInt("status2")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD2 = 0; iD2 < listReason.size(); iD2++) {

                        Reason ReasonD2 = (Reason) listReason.get(iD2);

                        if (rs.getInt("reason2") == ReasonD2.getNo()) {
                            val[iD2]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason3") > -1 && rs.getInt("status3")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD3 = 0; iD3 < listReason.size(); iD3++) {

                        Reason ReasonD3 = (Reason) listReason.get(iD3);

                        if (rs.getInt("reason3") == ReasonD3.getNo()) {
                            val[iD3]++;
                            break;
                        }
                    }
                }


                if (rs.getInt("reason4") > -1 && rs.getInt("status4")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD4 = 0; iD4 < listReason.size(); iD4++) {

                        Reason ReasonD4 = (Reason) listReason.get(iD4);

                        if (rs.getInt("reason4") == ReasonD4.getNo()) {
                            val[iD4]++;
                            break;
                        }
                    }

                }

                if (rs.getInt("reason5") > -1 && rs.getInt("status5")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD5 = 0; iD5 < listReason.size(); iD5++) {

                        Reason ReasonD5 = (Reason) listReason.get(iD5);

                        if (rs.getInt("reason5") == ReasonD5.getNo()) {
                            val[iD5]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason6") > -1 && rs.getInt("status6")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD6 = 0; iD6 < listReason.size(); iD6++) {

                        Reason ReasonD6 = (Reason) listReason.get(iD6);

                        if (rs.getInt("reason6") == ReasonD6.getNo()) {
                            val[iD6]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason7") > -1 && rs.getInt("status7")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD7 = 0; iD7 < listReason.size(); iD7++) {

                        Reason ReasonD7 = (Reason) listReason.get(iD7);

                        if (rs.getInt("reason7") == ReasonD7.getNo()) {
                            val[iD7]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason8") > -1 && rs.getInt("status8")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD8 = 0; iD8 < listReason.size(); iD8++) {

                        Reason ReasonD8 = (Reason) listReason.get(iD8);

                        if (rs.getInt("reason8") == ReasonD8.getNo()) {
                            val[iD8]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason9") > -1 && rs.getInt("status9")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD9 = 0; iD9 < listReason.size(); iD9++) {

                        Reason ReasonD9 = (Reason) listReason.get(iD9);

                        if (rs.getInt("reason9") == ReasonD9.getNo()) {
                            val[iD9]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason10") > -1 && rs.getInt("status10")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD10 = 0; iD10 < listReason.size(); iD10++) {

                        Reason ReasonD10 = (Reason) listReason.get(iD10);

                        if (rs.getInt("reason10") == ReasonD10.getNo()) {
                            val[iD10]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason11") > -1 && rs.getInt("status11")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD11 = 0; iD11 < listReason.size(); iD11++) {

                        Reason ReasonD11 = (Reason) listReason.get(iD11);

                        if (rs.getInt("reason11") == ReasonD11.getNo()) {
                            val[iD11]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason12") > -1 && rs.getInt("status12")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD12 = 0; iD12 < listReason.size(); iD12++) {

                        Reason ReasonD12 = (Reason) listReason.get(iD12);

                        if (rs.getInt("reason12") == ReasonD12.getNo()) {
                            val[iD12]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason13") > -1 && rs.getInt("status13")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD13 = 0; iD13 < listReason.size(); iD13++) {

                        Reason ReasonD13 = (Reason) listReason.get(iD13);

                        if (rs.getInt("reason13") == ReasonD13.getNo()) {
                            val[iD13]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason14") > -1 && rs.getInt("status14")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD14 = 0; iD14 < listReason.size(); iD14++) {

                        Reason ReasonD14 = (Reason) listReason.get(iD14);

                        if (rs.getInt("reason14") == ReasonD14.getNo()) {
                            val[iD14]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason15") > -1 && rs.getInt("status15")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD15 = 0; iD15 < listReason.size(); iD15++) {

                        Reason ReasonD15 = (Reason) listReason.get(iD15);

                        if (rs.getInt("reason15") == ReasonD15.getNo()) {
                            val[iD15]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason16") > -1 && rs.getInt("status16")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD16 = 0; iD16 < listReason.size(); iD16++) {

                        Reason ReasonD16 = (Reason) listReason.get(iD16);

                        if (rs.getInt("reason16") == ReasonD16.getNo()) {
                            val[iD16]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason17") > -1 && rs.getInt("status17")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD17 = 0; iD17 < listReason.size(); iD17++) {

                        Reason ReasonD17 = (Reason) listReason.get(iD17);

                        if (rs.getInt("reason17") == ReasonD17.getNo()) {
                            val[iD17]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason18") > -1 && rs.getInt("status18")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD18 = 0; iD18 < listReason.size(); iD18++) {

                        Reason ReasonD18 = (Reason) listReason.get(iD18);

                        if (rs.getInt("reason18") == ReasonD18.getNo()) {
                            val[iD18]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason19") > -1 && rs.getInt("status9")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD19 = 0; iD19 < listReason.size(); iD19++) {

                        Reason ReasonD19 = (Reason) listReason.get(iD19);

                        if (rs.getInt("reason19") == ReasonD19.getNo()) {
                            val[iD19]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason20") > -1 && rs.getInt("status20")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD20 = 0; iD20 < listReason.size(); iD20++) {

                        Reason ReasonD20 = (Reason) listReason.get(iD20);

                        if (rs.getInt("reason20") == ReasonD20.getNo()) {
                            val[iD20]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason21") > -1 && rs.getInt("status21")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD21 = 0; iD21 < listReason.size(); iD21++) {

                        Reason ReasonD21 = (Reason) listReason.get(iD21);

                        if (rs.getInt("reason21") == ReasonD21.getNo()) {
                            val[iD21]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason22") > -1 && rs.getInt("status22")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD22 = 0; iD22 < listReason.size(); iD22++) {

                        Reason ReasonD22 = (Reason) listReason.get(iD22);

                        if (rs.getInt("reason22") == ReasonD22.getNo()) {
                            val[iD22]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason23") > -1 && rs.getInt("status23")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD23 = 0; iD23 < listReason.size(); iD23++) {

                        Reason ReasonD23 = (Reason) listReason.get(iD23);

                        if (rs.getInt("reason23") == ReasonD23.getNo()) {
                            val[iD23]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason24") > -1 && rs.getInt("status24")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD24 = 0; iD24 < listReason.size(); iD24++) {

                        Reason ReasonD24 = (Reason) listReason.get(iD24);

                        if (rs.getInt("reason24") == ReasonD24.getNo()) {
                            val[iD24]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason25") > -1 && rs.getInt("status25")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD25 = 0; iD25 < listReason.size(); iD25++) {

                        Reason ReasonD25 = (Reason) listReason.get(iD25);

                        if (rs.getInt("reason25") == ReasonD25.getNo()) {
                            val[iD25]++;
                            break;
                        }
                    }
                }



                if (rs.getInt("reason26") > -1 && rs.getInt("status26")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD26 = 0; iD26 < listReason.size(); iD26++) {

                        Reason ReasonD26 = (Reason) listReason.get(iD26);

                        if (rs.getInt("reason26") == ReasonD26.getNo()) {
                            val[iD26]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason27") > -1 && rs.getInt("status27")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD27 = 0; iD27 < listReason.size(); iD27++) {

                        Reason ReasonD27 = (Reason) listReason.get(iD27);

                        if (rs.getInt("reason27") == ReasonD27.getNo()) {
                            val[iD27]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason28") > -1 && rs.getInt("status28")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD28 = 0; iD28 < listReason.size(); iD28++) {

                        Reason ReasonD28 = (Reason) listReason.get(iD28);

                        if (rs.getInt("reason28") == ReasonD28.getNo()) {
                            val[iD28]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason29") > -1 && rs.getInt("status29")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD29 = 0; iD29 < listReason.size(); iD29++) {

                        Reason ReasonD29 = (Reason) listReason.get(iD29);

                        if (rs.getInt("reason29") == ReasonD29.getNo()) {
                            val[iD29]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason30") > -1 && rs.getInt("status30")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD30 = 0; iD30 < listReason.size(); iD30++) {

                        Reason ReasonD30 = (Reason) listReason.get(iD30);

                        if (rs.getInt("reason30") == ReasonD30.getNo()) {
                            val[iD30]++;
                            break;
                        }
                    }
                }

                if (rs.getInt("reason31") > -1 && rs.getInt("status31")== PstEmpSchedule.STATUS_PRESENCE_ABSENCE) {

                    for (int iD31 = 0; iD31 < listReason.size(); iD31++) {

                        Reason ReasonD31 = (Reason) listReason.get(iD31);

                        if (rs.getInt("reason31") == ReasonD31.getNo()) {
                            val[iD31]++;
                            break;
                        }
                    }
                }

                yearPresence.setValueSchedule(val);

                listRpt.add(yearPresence);

            }

            return listRpt;

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

        return null;

    }

/**
     * Keterangan : belum terpakai karena tidak menghitung break IN dan break OUT
     * Create by satrya ramauyu
     * @param departmentId
     * @param fromDate
     * @param toDate
     * @param sectionId
     * @param empNumb
     * @param fullName
     * @return 
     */
// public static Vector listAttSummary(long departmentId, Date fromDate, Date toDate, long sectionId, String empNumb, String fullName) {
//      
//        DBResultSet dbrs = null;
//        Vector result = new Vector();
//        if (fromDate != null && toDate != null) {
//            if (fromDate.getTime() > toDate.getTime()) {
//                Date tempFromDate = fromDate;
//                Date tempToDate = toDate;
//                fromDate = tempToDate;
//                toDate = tempFromDate;
//            }
//        /* Mengambil setingan system property reason yang tidak akan di tampilkan */
//        String reasonProperty = PstSystemProperty.getValueByName("REASON_PROPERTY");
//
//        String reasonRpt = "";
//        int iReasonRpt=0;
//        if (reasonProperty.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {
//
//            Vector reason = com.dimata.util.StringParser.parseGroup(reasonProperty);
//            String[] strReason = (String[]) reason.get(0);
//
//            if (strReason.length > 0) {
//
//                reasonRpt = reasonRpt + " ( ";
//
//                for (int g = 0; g < strReason.length; g++) {
//
//                    if (g == 0) {
//                        reasonRpt = reasonRpt + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
//                    } else {
//                        reasonRpt = reasonRpt + " AND " + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
//                    }
//                }
//
//                reasonRpt = reasonRpt + " ) ";
//            }
//
//        }
//
//        if(reasonRpt!=null && reasonRpt.length()>0){
//           iReasonRpt = Integer.parseInt(reasonRpt);
//        }
//         
//        try {
//
//            String sql = "SELECT "
//                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " as EMPLOYEE_ID,"
//                    /*+ " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " as EMPLOYEE_NUMBER,"
//                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " as FULL_NAME,"
//                    + " DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " as DEPARTEMENT_ID,"
//                    + " DEPT." + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT] + " as DEPARTEMENT_NAME,"
//                    + " DIVS."+PstDivision.fieldNames[PstDivision.FLD_DIVISION] + " as DIVISION_NAME ,"
//                    + " SEC."+PstSection.fieldNames[PstSection.FLD_SECTION] + " as SECTION"*/
//                    
//                    + " , VATTD.SCH_DATE"
//                    + " , VATTD.REASON_ID"
//                    + " , VATTD.IN_TIME"
//                    + " , VATTD.OUT_TIME"
//                    + " , VATTD.REASON_ID"
//                    + " , VATTD.STATUS_IDX"
//                    
//                    + " ,SS."+PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL] + " as SYMBOL"
//                    + " ,SS."+PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN] + " as SCH_TIME_IN"
//                    + " ,SS."+PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT] + " as SCH_TIME_OUT"
//                    + " ,SS."+PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_BREAK_IN] + " as SCH_BREAK_IN"
//                    + " ,SS."+PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_BREAK_OUT] + " as SCH_BREAK_OUT "
//                    
//                    + " FROM hr_view_attendance AS VATTD INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE
//                    + " EMP ON VATTD.EMPLOYEE_ID = EMP."
//                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
//                    /*+ " INNER JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " DEPT ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = DEPT."
//                    + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID]
//                    
//                    + " INNER JOIN " + PstDivision.TBL_HR_DIVISION + " DIVS ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + " = DIVS."
//                    + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]
//                    + " LEFT JOIN " + PstSection.TBL_HR_SECTION + " SEC ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = SEC."
//                    + PstSection.fieldNames[PstSection.FLD_SECTION_ID]*/
//                    
//                    + " INNER JOIN " +PstScheduleSymbol.TBL_HR_SCHEDULE_SYMBOL 
//                    + " SS ON VATTD.SCH_ID"
//                    + " = SS."+PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_ID]
//                    
//                     + " WHERE (1=1)";
//                    if(iReasonRpt!=0){
//                        sql +=  "AND VATTD.REASON_ID ="+iReasonRpt;
//                    }
//                    //update by satrya 2012-09-10
//                    //untuk mencari karyawan risigned
//                if(fromDate!=null && toDate!=null){
//                    sql= sql + "  AND (( EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " +PstEmployee.YES_RESIGN + " AND " + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
//                    + " BETWEEN \"" + Formater.formatDate(fromDate, "yyyy-MM-dd  00:00:00")+ "\""  +" AND "+ "\"" + Formater.formatDate(toDate, "yyyy-MM-dd  23:59:59")+ "\"" 
//                    + " ) OR (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " + PstEmployee.NO_RESIGN + "))"
//                     + " AND SCH_DATE BETWEEN \""+Formater.formatDate(fromDate, "yyyy-MM-dd  00:00:00")+"\""+" AND "+"\""+Formater.formatDate(toDate, "yyyy-MM-dd  23:59:59")+ "\"" ;
//                }   
//                    if(departmentId != 0){
//                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
//                    }
//
//                    if(sectionId != 0){
//                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
//                    }
//                    
//            if (empNumb != null && empNumb != "") {
//                Vector vectNum = logicParser(empNumb);
//                sql = sql + " AND ";
//                if (vectNum != null && vectNum.size() > 0) {
//                    sql = sql + " (";
//                    for (int i = 0; i < vectNum.size(); i++) {
//                        String str = (String) vectNum.get(i); 
//                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
//                            sql = sql +" "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
//                                    + " LIKE '%" + str.trim() + "%' ";
//                        } else {
//                            sql = sql + str.trim();
//                        }
//                    }
//                    sql = sql + ")";
//                }
//                
//            }
//            if (fullName != null && fullName != "") {
//                Vector vectFullName = logicParser(fullName);
//                  sql = sql + " AND ";
//                if (vectFullName != null && vectFullName.size() > 0) {
//                    sql = sql + " ( ";
//                    for (int i = 0; i < vectFullName.size(); i++) {
//                        String str = (String) vectFullName.get(i);
//                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
//                            sql = sql +" "+ PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
//                                    + " LIKE '%" + str.trim() + "%' ";
//                        } else {
//                            sql = sql + str.trim();
//                        }
//                    }
//                    sql = sql + " ) ";
//                }
//                 
//            }
//            dbrs = DBHandler.execQueryResult(sql);
//            ResultSet rs = dbrs.getResultSet();
//
//            Vector listRpt = new Vector();
//             int i = 0;
//             long schIn=0;
//             long schOut=0;
//             long schBI=0;
//             long schBO=0;
//            while (rs.next()) {
//               i = i + 1;
//                Date selectDate = fromDate!=null ?new Date(fromDate.getTime() + i * 1000L * 60 * 60 * 24):null;
//
//                AttendanceSummary attendanceSummary = new AttendanceSummary();
//
//                attendanceSummary.setEmployeeId(rs.getLong("EMPLOYEE_ID"));
//                attendanceSummary.setEmployeeNum(rs.getString("EMPLOYEE_NUMBER"));
//                attendanceSummary.setFullName(rs.getString("FULL_NAME"));
//                attendanceSummary.setDepartmentId(rs.getLong("DEPARTEMENT_ID"));
//                attendanceSummary.setDepartment(rs.getString("DEPARTEMENT_NAME"));
//                attendanceSummary.setDivision("DIVISION_NAME");
//                attendanceSummary.setSection(rs.getString("SECTION"));
//                
//                attendanceSummary.setSchDate(rs.getDate("SCH_DATE"));
//                attendanceSummary.setStatus(rs.getInt("STATUS_IDX"));
//                attendanceSummary.setReason(rs.getInt("REASON_ID"));
//                attendanceSummary.setTimeIn(rs.getDate("IN_TIME"));
//                attendanceSummary.setTimeOut(rs.getDate("OUT_TIME"));
//                
//                if(selectDate!=null && rs.getTime("SCH_TIME_IN")!=null){
//                    schIn = selectDate.getTime()+rs.getTime("SCH_TIME_IN").getTime();
//                attendanceSummary.setSchTimeIn(new Date(schIn));
//                }
//                if(selectDate!=null && rs.getTime("SCH_TIME_OUT")!=null){
//                    schOut = selectDate.getTime()+rs.getTime("SCH_TIME_OUT").getTime();
//                 attendanceSummary.setSchTimeOut(new Date(schOut));
//                }
//                if(selectDate!=null && rs.getDate("SCH_BREAK_IN")!=null){
//                    schBI = selectDate.getTime()+rs.getDate("SCH_BREAK_IN").getTime();
//                 attendanceSummary.setSchBreakIn(new Date(schBI));
//                }
//                if(selectDate!=null && rs.getDate("SCH_BREAK_OUT")!=null){
//                    schBO = selectDate.getTime()+rs.getDate("SCH_BREAK_OUT").getTime();
//                 attendanceSummary.setSchBreakIn(new Date(schBO));
//                }
//                
//                attendanceSummary.setSymbol(rs.getString("SYMBOL"));
//                
//                listRpt.add(attendanceSummary);
//            }
//
//            return listRpt;
//
//        } catch (Exception E) {
//            System.out.println("[exception] " + E.toString());
//        } finally {
//            DBResultSet.close(dbrs);
//        }
//
//       // return null;
//    }
//        return result;
// }

 public static Vector getCountReason(long departmentId, Date fromDate, Date toDate, long sectionId, String empNumb, String fullName) {
      
        DBResultSet dbrs = null;
        Vector result = new Vector();
        if (fromDate != null && toDate != null) {
            if (fromDate.getTime() > toDate.getTime()) {
                Date tempFromDate = fromDate;
                Date tempToDate = toDate;
                fromDate = tempToDate;
                toDate = tempFromDate;
            }
        /* Mengambil setingan system property reason yang tidak akan di tampilkan */
        String reasonProperty = PstSystemProperty.getValueByName("REASON_PROPERTY");

        String reasonRpt = "";
        int iReasonRpt=0;
        if (reasonProperty.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {

            Vector reason = com.dimata.util.StringParser.parseGroup(reasonProperty);
            String[] strReason = (String[]) reason.get(0);

            if (strReason.length > 0) {

                reasonRpt = reasonRpt + " ( ";

                for (int g = 0; g < strReason.length; g++) {

                    if (g == 0) {
                        reasonRpt = reasonRpt + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    } else {
                        reasonRpt = reasonRpt + " AND " + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    }
                }

                reasonRpt = reasonRpt + " ) ";
            }

        }

        if(reasonRpt!=null && reasonRpt.length()>0){
           iReasonRpt = Integer.parseInt(reasonRpt);
        }
         
        try {

            
            String sql = "SELECT COUNT(VATTD.REASON_ID) AS COUNT_REASON , "
                    + " EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " as EMPLOYEE_ID, "
                    + " VATTD.REASON_ID "
                    + " FROM hr_view_attendance AS VATTD "
                    + " INNER JOIN "+PstEmployee.TBL_HR_EMPLOYEE + " as EMP "
                    + " ON(EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+ " = VATTD.EMPLOYEE_ID "+")"
                    
                     + " WHERE (1=1)";
                    if(iReasonRpt!=0){
                        sql +=  " AND VATTD.REASON_ID ="+iReasonRpt;
                    }
                    sql +=  " AND VATTD.REASON_ID !="+0;
                    //update by satrya 2012-09-10
                    //untuk mencari karyawan risigned
                if(fromDate!=null && toDate!=null){
                    sql= sql + "  AND (( EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
                    + " = " +PstEmployee.YES_RESIGN + " AND " + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
                    + " BETWEEN \"" + Formater.formatDate(fromDate, "yyyy-MM-dd  00:00:00")+ "\""  +" AND "+ "\"" + Formater.formatDate(toDate, "yyyy-MM-dd  23:59:59")+ "\"" 
                    + " ) OR (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
                    + " = " + PstEmployee.NO_RESIGN + "))"
                     + " AND SCH_DATE BETWEEN \""+Formater.formatDate(fromDate, "yyyy-MM-dd  00:00:00")+"\""+" AND "+"\""+Formater.formatDate(toDate, "yyyy-MM-dd  23:59:59")+ "\"" ;
                }   
                    if(departmentId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + " = " + departmentId;
                    }

                    if(sectionId != 0){
                        sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID] + " = " + sectionId;
                    }
                    
            if (empNumb != null && empNumb != "") {
                Vector vectNum = logicParser(empNumb);
                sql = sql + " AND ";
                if (vectNum != null && vectNum.size() > 0) {
                    sql = sql + " (";
                    for (int i = 0; i < vectNum.size(); i++) {
                        String str = (String) vectNum.get(i); 
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + ")";
                }
                
            }
            if (fullName != null && fullName != "") {
                Vector vectFullName = logicParser(fullName);
                  sql = sql + " AND ";
                if (vectFullName != null && vectFullName.size() > 0) {
                    sql = sql + " ( ";
                    for (int i = 0; i < vectFullName.size(); i++) {
                        String str = (String) vectFullName.get(i);
                        if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                            sql = sql +" "+ PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                    + " LIKE '%" + str.trim() + "%' ";
                        } else {
                            sql = sql + str.trim();
                        }
                    }
                    sql = sql + " ) ";
                }
                 
            }
            sql+= " GROUP BY  VATTD.REASON_ID ";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector listRpt = new Vector();

            while (rs.next()) {
              
                ReasonCount reasonCount = new ReasonCount();

                reasonCount.setEmpId(rs.getLong("EMPLOYEE_ID"));
                reasonCount.setCountreason(rs.getInt("COUNT_REASON"));
                reasonCount.setReasonNo(rs.getInt("VATTD.REASON_ID"));
                
                listRpt.add(reasonCount);
            }

            return listRpt;

        } catch (Exception E) {
            System.out.println("[exception] " + E.toString());
        } finally {
            DBResultSet.close(dbrs);
        }

       // return null;
    }
        return result;
 }
 /**
  * Keterangan: melakukan Analyze Presence Manual untuk di report daily
  * crate by satrya 2013-05-27
  * @param selectedDate
  * @param employeeId
  * @return 
  */
public static boolean analysePresenceManual(Date selectedDate,long employeeId) {
    //DBResultSet dbrs = null;
    //Vector list = new Vector();
    boolean isSuccess=false;
    Date fromDate = (Date)selectedDate.clone();
    fromDate.setDate(fromDate.getDate()-1);//mencari hari yang sebelumnya
    Date toDate = (Date)selectedDate.clone();
    toDate.setDate(toDate.getDate()+1);//mecari hari setelahnya
    
    
    try{
      if(selectedDate!=null && fromDate!=null && toDate!=null){
         //update by satrya 2013-07-08
         I_Atendance attdConfig = null;
                try {
                    attdConfig = (I_Atendance) (Class.forName(com.dimata.system.entity.PstSystemProperty.getValueByName("ATTENDANCE_CONFIG")).newInstance());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                    System.out.println("Please contact your system administration to setup system property: LEAVE_CONFIG ");
                }
        String whereClause = PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] 
                + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd 00:00:00")
                + "\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd 23:59:59") +"\" AND " 
                +PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]+"="+employeeId ;
                //+ " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS]+ " NOT IN ("+Presence.STATUS_OUT_ON_DUTY+","+Presence.STATUS_CALL_BACK+")";
        Vector vPresence = PstPresence.list(0, 0, whereClause, PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]+" ASC ");
        
        Date tmpDate = (Date)fromDate.clone();
        if (vPresence != null && vPresence.size() > 0) {
            
           while(tmpDate.before(toDate) || tmpDate.equals(toDate)){
               PstEmpSchedule.resetScheduleDataFromDateToDateVer2(tmpDate, tmpDate, employeeId);
                tmpDate =  new Date(tmpDate.getTime()+ 24*60*60*1000);
           }
            Period period = PstPeriod.getPeriodBySelectedDate(tmpDate);
             for (int i = 0; i < vPresence.size(); i++) {
              
               Presence  presence = (Presence) vPresence.get(i);
              //isSuccess = SaverData.saveManualPresence(presence, 0);
               //update by satrya 2013-07-29
              
               AnalyseStatusDataPresence analyseStsDataPresence = attdConfig!=null && attdConfig.getConfigurasiInOut()==I_Atendance.CONFIGURASI_III_SEARCH_BY_SCHEDULE_NO_OVERTIME?SaverData.saveManualPresenceNoOvertime(presence, 0):SaverData.saveManualPresence(presence, 0);
               // AnalyseStatusDataPresence analyseStsDataPresence = SaverData.saveManualPresence(presence, 0);
               isSuccess = analyseStsDataPresence.isSuccess();
              //if(tmpDate.getDate()){
                //int updateStatus = PstEmpSchedule.updateScheduleDataByPresence(period.getOID(), presence.getEmployeeId(), tmpDate.getDate(),  presence.getPresenceDatetime());
             //}
             }
            // tmpDate =  new Date(tmpDate.getTime()+ 24*60*60*1000);;
           //}
        }
        //analisa statusnya
        tmpDate = (Date)fromDate.clone();
       while(tmpDate.before(toDate) || tmpDate.equals(toDate)){
            PresenceAnalyser.analyzePresencePerEmployeeByEmployeeId(tmpDate, employeeId);
             tmpDate =  new Date(tmpDate.getTime()+ 24*60*60*1000);
       }
      }
     
      
    }catch(Exception exc){
        System.out.println("Exception AnalysePresenceManual"+exc);
    }
     return  isSuccess;
}

//pembaruan analyse dari report daily
public static boolean analysePresenceManualReportDaily(Date selectedDate,long employeeId, int reason , String note) {
    //DBResultSet dbrs = null;
    //Vector list = new Vector();
    boolean isSuccess=false;
    Date fromDate = (Date)selectedDate.clone();
    fromDate.setDate(fromDate.getDate()-1);//mencari hari yang sebelumnya
    Date toDate = (Date)selectedDate.clone();
    toDate.setDate(toDate.getDate()+1);//mecari hari setelahnya
    
    
    try{
      if(selectedDate!=null && fromDate!=null && toDate!=null){
         //update by satrya 2013-07-08
         I_Atendance attdConfig = null;
                try {
                    attdConfig = (I_Atendance) (Class.forName(com.dimata.system.entity.PstSystemProperty.getValueByName("ATTENDANCE_CONFIG")).newInstance());
                } catch (Exception e) {
                    System.out.println("Exception : " + e.getMessage());
                    System.out.println("Please contact your system administration to setup system property: LEAVE_CONFIG ");
                }
        String whereClause = PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] 
                + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd 00:00:00")
                + "\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd 23:59:59") +"\" AND " 
                +PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]+"="+employeeId ;
                //+ " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS]+ " NOT IN ("+Presence.STATUS_OUT_ON_DUTY+","+Presence.STATUS_CALL_BACK+")";
        Vector vPresence = PstPresence.list(0, 0, whereClause, PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]+" ASC ");
        
        Date tmpDate = (Date)fromDate.clone();
        if (vPresence != null && vPresence.size() > 0) {
            
           while(tmpDate.before(toDate) || tmpDate.equals(toDate)){
               PstEmpSchedule.resetScheduleDataFromDateToDateVer2(tmpDate, tmpDate, employeeId);
                tmpDate =  new Date(tmpDate.getTime()+ 24*60*60*1000);
           }
            Period period = PstPeriod.getPeriodBySelectedDate(tmpDate);
             for (int i = 0; i < vPresence.size(); i++) {
              
               Presence  presence = (Presence) vPresence.get(i);
              //isSuccess = SaverData.saveManualPresence(presence, 0);
               //update by satrya 2013-07-29
              
               AnalyseStatusDataPresence analyseStsDataPresence = attdConfig!=null && attdConfig.getConfigurasiInOut()==I_Atendance.CONFIGURASI_III_SEARCH_BY_SCHEDULE_NO_OVERTIME?SaverData.saveManualPresenceNoOvertime(presence, 0):SaverData.saveManualPresence(presence, 0);
               // AnalyseStatusDataPresence analyseStsDataPresence = SaverData.saveManualPresence(presence, 0);
               isSuccess = analyseStsDataPresence.isSuccess();
              //if(tmpDate.getDate()){
                //int updateStatus = PstEmpSchedule.updateScheduleDataByPresence(period.getOID(), presence.getEmployeeId(), tmpDate.getDate(),  presence.getPresenceDatetime());
             //}
             }
            // tmpDate =  new Date(tmpDate.getTime()+ 24*60*60*1000);;
           //}
        }
        //analisa statusnya
        tmpDate = (Date)fromDate.clone();
       while(tmpDate.before(toDate) || tmpDate.equals(toDate)){
             PresenceAnalyser.analyzePresencePerEmployeeByEmployeeIdReportDaily(tmpDate, employeeId,reason, note );
             tmpDate =  new Date(tmpDate.getTime()+ 24*60*60*1000);
       }
      }
     
      
    }catch(Exception exc){
        System.out.println("Exception AnalysePresenceManual"+exc);
    }
     return  isSuccess;
}

/**
 * Keterangan mencari total summary insentif yg di terima
 * create by satrya 2014-01-31
 * @param fromDate
 * @param toDate
 * @param employee_id
 * @param status
 * @param iPropInsentifLevel
 * @param lHolidays
 * @param payrollCalculatorConfig
 * @param hashPositionLevel
 * @param hashSchOff
 * @param holidaysTable
 * @return 
 */
 public static Vector getSumEMployeeInstif(Date fromDate, Date toDate,String employee_id,int status,int iPropInsentifLevel,long lHolidays,I_PayrollCalculator payrollCalculatorConfig,Hashtable hashPositionLevel,Hashtable hashSchOff,HolidaysTable holidaysTable,HashTblOvertimeDetail hashTblOvertimeDetail,Hashtable hashPeriodTbl) {
       //String untukTestSaja="";
       String idEmployee="";
        DBResultSet dbrs = null;
        Vector list = new Vector();
        int start =0;
        int recordToGet =100;
        boolean finishLoop=true;
        Vector listPeriod = PstPeriod.getListStartEndDatePeriod(fromDate, toDate);
        String periodId="";
        boolean cekMaxMonth= false;//inisilaisai awal pengambilan maxMonth
        Hashtable cekIdxSama = new Hashtable();
        String sql="";
         //update by satrya 2014-03-10
        if(payrollCalculatorConfig!=null){
            payrollCalculatorConfig.loadEmpCategoryInsentif();
        }
        try{
           sql = "SELECT EMP." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID] +" , "
                   + " p."+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID] +" , "
                   + "EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] +" , "
                   + "EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID] +" , "
                   //update by satrya 2014-02-10
                   +"EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] +" , ";
           if(listPeriod!=null && listPeriod.size()>0){     
               for(int idxPeriod=0; idxPeriod<listPeriod.size();idxPeriod++){
                   Period period = (Period)listPeriod.get(idxPeriod);
                   Calendar calendar = Calendar.getInstance();
                  if(cekMaxMonth){
                   calendar.setTime(period.getStartDate());
                  }else{
                      calendar.setTime(fromDate);
                  }
                   int maxMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                   
                    long diffStartToFinish = 0;//toDate.getTime() - fromDate.getTime();
                  //pengecekan yg mana yg akan di pakai sebagai pengurang
                  //jika pencariannya hanya d dalam 1 periode
                  if(listPeriod.size()==1){
                      //update by satrya 2013-09-13
                      //kasusu jika user memilih tanggal 21 agustus s/d 11 september hanya sampai 10 september saja yg di hitung
                      // diffStartToFinish = (toDate.getTime() - fromDate.getTime())
                      diffStartToFinish = (toDate.getTime() - fromDate.getTime()) /*+ 1000 * 60 * 60 * 24*/;
                  }
                  else if(cekMaxMonth){
                    diffStartToFinish = (toDate.getTime() - period.getStartDate().getTime());//+ 86400000);//di tambah 1 hari karena misalkan kita mencari tagnggal 21 maret smpe 3 mei tpi 3 meinya tdk kehitung
                  }else{
                      diffStartToFinish = period.getEndDate().getTime() - fromDate.getTime();
                  }
                    int itDate = Integer.parseInt(String.valueOf(diffStartToFinish / 86400000));
                    periodId = periodId + period.getOID() + ", ";
                 Date cloneFromDate = (Date) fromDate.clone();
                  if(cekMaxMonth){
                    cloneFromDate = (Date) period.getStartDate().clone();
                  }else{
                      cloneFromDate = (Date) fromDate.clone();
                  }
                    int idxFieldName = cloneFromDate.getDate();
                    int idxDt=0;
                    do{
                        //update by satrya 2014-01-22 for(int idxDt=0; idxDt<itDate;idxDt++){
                        // int idxFieldName = fromDate.getDate()+idxDt;
                        
                         if(idxFieldName > maxMonth){
                                idxFieldName = 1;
                               // fromDate.setDate(1);
                                cloneFromDate.setDate(1);
                                cloneFromDate.setMonth(cloneFromDate.getMonth()+1);
                                //fromDate.setYear(reason);
                                calendar = Calendar.getInstance();
                                calendar.setTime(cloneFromDate);
                                maxMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                         }
                         
                         if(!cekIdxSama.containsKey(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1])){
                            sql = sql + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1] 
                            + ", " + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_IN +  idxFieldName - 1] + ", " 
                            + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_OUT +  idxFieldName - 1] + ", "
                            + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_STATUS +  idxFieldName - 1] + ", "
                            + PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_REASON +  idxFieldName - 1] + ", "
                  
      + " IF(((p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1].substring(1))+") DAY) < p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+"),"
      + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+ " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+") - "+ (PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1].substring(1))+") DAY),"
    + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+ " -  INTERVAL(DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1].substring(1))+") DAY)) AS SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]+" , "                      
    
   /* + " DATE_FORMAT(IF(((p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1].substring(1))+") DAY) < p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+"),"
    + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+ " - INTERVAL (DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]+") - "+ (PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1].substring(1))+") DAY),"
    + " (p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+ " -  INTERVAL(DAYOFMONTH(p."+PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]+") - "+(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1].substring(1))+") DAY)),'%a') AS SCH_DAYS , "*/;                              
    
                            
                            cekIdxSama.put(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1], PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]);
                         }
                         idxFieldName =idxFieldName+1;
                         idxDt=idxDt+1;
                    }while(idxDt<=itDate);
                    cekMaxMonth = true;//artinya sudah menggunakan berdasarkan period start
                   
               }
                    periodId = periodId.substring(0,periodId.length()-2);
                    sql = sql.substring(0, sql.length() - 2);
                    sql = sql
                    + " FROM " + PstEmpSchedule.TBL_HR_EMP_SCHEDULE + " AS EMPSCH "
                    + " INNER JOIN "+ PstEmployee.TBL_HR_EMPLOYEE
                    + " AS EMP ON EMPSCH."+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]+"=EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + " INNER JOIN "+ PstPeriod.TBL_HR_PERIOD
                    + " AS p ON EMPSCH."+PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]+"=p."+PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID]
                    + " WHERE p." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]
                    + " IN ( " + periodId + ")"
                    + " AND EMP." + PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]
                    + " IN ( " + employee_id + ")";
                   
                sql = sql + " ORDER BY EMP."+ PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID] + " , " +PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]+ " ASC ";
               // System.out.println("Sql:"+sql);
           }
        }catch(Exception exc){
            System.out.println("Exception sessPresence"+exc);
        }
           
        do{
         finishLoop=true;
        try {

           if(listPeriod!=null && listPeriod.size()>0){
                 //update by satrya 2014-01-22
                 Hashtable hashCekTglSama=new Hashtable();
                String sql2= sql + " LIMIT "+ start+","+recordToGet;
                start = start + recordToGet;
                    dbrs = DBHandler.execQueryResult(sql2);
                    ResultSet rs = dbrs.getResultSet();
                   long empId=0;
                    AttdSumInsentif attdSumInsentif = new AttdSumInsentif();
                    cekMaxMonth = false;
                    Period period = new Period();
                    long currPeriodId =0;
                    Hashtable hashPeriod = new Hashtable();
                    int countInstf=0;
                    while (rs.next()) {
                       finishLoop =false;
                       currPeriodId=rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_PERIOD_ID]);
                       if(period.getOID()!= currPeriodId ){
                            period = (Period)hashPeriod.get(""+currPeriodId);
                        if(period==null){
                            //update by satrya 2014-02-03
                             if(currPeriodId!=0 && (hashPeriodTbl!=null && hashPeriodTbl.get(currPeriodId)!=null)){
                                period  = (Period)hashPeriodTbl.get(currPeriodId);
                            }
                          //period  = PstPeriod.fetchExc(currPeriodId);
                         hashPeriod.put(""+currPeriodId,period );
                        }                        
                       }
                    empId= rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.FLD_EMPLOYEE_ID]);
                    if(attdSumInsentif.getEmployeeId() != empId){
                           attdSumInsentif = new AttdSumInsentif();
                           countInstf =0;
                           attdSumInsentif.setEmployeeId(empId);
                           cekMaxMonth = false;
                           //update by satrya 2014-01-22
                           hashCekTglSama=new Hashtable();
                           list.add(attdSumInsentif);
                      }                      
                        
                   Calendar calendar = Calendar.getInstance();
                   if(cekMaxMonth){
                            calendar.setTime(period.getStartDate());
                        }else{
                            calendar.setTime(fromDate);
                        }
                   int maxMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                   long diffStartToFinish = 0;//toDate.getTime() - fromDate.getTime();
                  //pengecekan yg mana yg akan di pakai sebagai pengurang
                  //jika pencariannya hanya d dalam 1 periode
                  if(listPeriod.size()==1){
                      //kasusu jika user memilih tanggal 21 agustus s/d 11 september hanya sampai 10 september saja yg di hitung
                      // diffStartToFinish = (toDate.getTime() - fromDate.getTime())
                      diffStartToFinish = (toDate.getTime() - fromDate.getTime()) /*+ 1000 * 60 * 60 * 24*/;
                  }
                   else if(cekMaxMonth){
                    diffStartToFinish = (toDate.getTime() - period.getStartDate().getTime() );//di tambah 1 hari karena misalkan kita mencari tagnggal 21 maret smpe 3 mei tpi 3 meinya tdk kehitung
                  }else{
                      diffStartToFinish = period.getEndDate().getTime() - fromDate.getTime();
                  }
                    int itDate = Integer.parseInt(String.valueOf(diffStartToFinish / 86400000));
                    periodId = periodId + period.getOID() + ", ";
                    Date cloneFromDate = new Date();
                  if(cekMaxMonth){
                    cloneFromDate = (Date) period.getStartDate().clone();
                  }else{
                      cloneFromDate = (Date) fromDate.clone();
                  }
                    
                    int idxFieldName = cloneFromDate.getDate();
                    //jika dia memilih tanggal 20 juni smpai 21 juli masih kebaca di terakhirnya 21 juninya
                    // for(int idxDt=0; idxDt<=itDate;idxDt++){
                    //update by satrya 2014-01-21 karena jika user memilih tanggal 1 smpe 21 tgl 20 tidak d pilih for(int idxDt=0; idxDt<(itDate);idxDt++){
                    int idxDt=0;
                    //update by satrya 2014-01-22
                   do{
                        // int idxFieldName = fromDate.getDate()+idxDt;
                         if(idxFieldName > maxMonth){
                             //di update karena exception d tanggal 31
                             //if(idxFieldName > maxMonth){
                                idxFieldName = 1;
                               // fromDate.setDate(1);
                                cloneFromDate.setDate(1);
                                cloneFromDate.setMonth(cloneFromDate.getMonth()+1);
                                //fromDate.setYear(reason);
                                 calendar = Calendar.getInstance();
                                calendar.setTime(cloneFromDate);
                                maxMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                         }
                     //update by satrya 2014-01-22
                idEmployee =""+empId;
            try{
                //untukTestSaja = untukTestSaja + rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1])+",";
                if(hashCekTglSama.size()==0 || !hashCekTglSama.containsKey(rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]))){
                       //update by satrya 2013-04-30
                         int sts = rs.getInt(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_STATUS + idxFieldName - 1]);
                   if(status == sts){
                       //untuk karyawan
                       Date dtSelect =rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]);
                       long religionId=rs.getLong("EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]);
                       long deptId=rs.getLong("EMP."+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]);
                       long empCatgoryId=rs.getLong("EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]);
                       int stsSchedule =rs.getInt(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_STATUS + idxFieldName - 1]);
                       int positionLevel=0;
                       if(hashPositionLevel!=null && hashPositionLevel.get(empId)!=null){
                            positionLevel = (Integer)hashPositionLevel.get(empId);
                       }
                       long schOffId=rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]);
                       
                       if(payrollCalculatorConfig.checkInsentif(empId, religionId,deptId, stsSchedule, positionLevel, iPropInsentifLevel, schOffId, hashSchOff, holidaysTable.isHoliday(religionId !=0 ? religionId : 0, dtSelect), dtSelect,hashTblOvertimeDetail,empCatgoryId)) {
                           //update by satrya 2014-01-31 if(payrollCalculatorConfig.checkInsentif(empId, rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_RELIGION_ID]), rs.getLong(PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]), status, iPropInsentifLevel, iPropInsentifLevel, rs.getLong(PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]), lHolidays, rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]))){
                            countInstf = countInstf + 1;
                       }
                   }
                     //update by satrya 2014-01-22
                   hashCekTglSama.put(rs.getDate("SCH_DATE_"+PstEmpSchedule.fieldNames[PstEmpSchedule.OFFSET_INDEX_CALENDAR + idxFieldName - 1]), true);
                 }
            }catch(Exception exc){
                System.out.println("idEmployee di tengah "+idEmployee + exc);
            }
                        attdSumInsentif.setTotalInsentif(countInstf);
                         idxFieldName =idxFieldName+1;
                         idxDt=idxDt+1;
            
                    }while(idxDt<=itDate); 
              
                   cekMaxMonth = true;//artinya sudah menggunakan berdasarkan period start
               //}
             //}
            }
                   
            rs.close();
           }
           //System.out.println("Insnetif"+untukTestSaja);
           // return list;
        } catch (Exception e) {
            System.out.println("Exception sumInsentif " +e);
            //System.out.println("idEmployee"+idEmployee);
           return list;
        } finally {
            DBResultSet.close(dbrs);
        }
    } while (!finishLoop);
        
        return list;
       
    }

 
 
    
    
 
        
    public static Vector getPeriodPresence(int year) {

        DBResultSet dbrs = null;

        try {
            int nextYear = year + 1;

            String sql = "SELECT " + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID] + ","
                    + PstPeriod.fieldNames[PstPeriod.FLD_PERIOD] + ","
                    + PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ","
                    + PstPeriod.fieldNames[PstPeriod.FLD_END_DATE] + " FROM "
                    + PstPeriod.TBL_HR_PERIOD
                    + " WHERE (YEAR(" + PstPeriod.fieldNames[PstPeriod.FLD_START_DATE] + ") = '" + year + "' OR YEAR("
                    + PstPeriod.fieldNames[PstPeriod.FLD_END_DATE] + ") = '" + year + "' ) AND YEAR(" + PstPeriod.fieldNames[PstPeriod.FLD_END_DATE] + ") != " + nextYear;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector result = new Vector();

            while (rs.next()) {

                Period period = new Period();
                period.setOID(rs.getLong(PstPeriod.fieldNames[PstPeriod.FLD_PERIOD_ID]));
                period.setPeriod(rs.getString(PstPeriod.fieldNames[PstPeriod.FLD_PERIOD]));
                period.setStartDate(rs.getDate(PstPeriod.fieldNames[PstPeriod.FLD_START_DATE]));
                period.setEndDate(rs.getDate(PstPeriod.fieldNames[PstPeriod.FLD_END_DATE]));

                result.add(period);

            }

            return result;

        } catch (Exception E) {
            System.out.println("[exception] "+E.toString());
        }
    
        return null;
    
  }

    public static Vector reasonYear(){

        /* Mengambil setingan system property resin yang akan di tampilkan pada report tahunan */
        String reasonProperty = PstSystemProperty.getValueByName("REASON_PROPERTY");

        String reasonRpt = "";

        if (reasonProperty.compareTo(PstSystemProperty.SYS_NOT_INITIALIZED) != 0) {

            Vector reason = com.dimata.util.StringParser.parseGroup(reasonProperty);
            String[] strReason = (String[]) reason.get(0);

            if (strReason.length > 0) {

                reasonRpt = reasonRpt + " ( ";

                for (int g = 0; g < strReason.length; g++) {

                    if (g == 0) {
                        reasonRpt = reasonRpt + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    } else {
                        reasonRpt = reasonRpt + " AND " + PstReason.fieldNames[PstReason.FLD_REASON_ID] + " != " + strReason[g];
                    }
                }

                reasonRpt = reasonRpt + " ) ";
            }

        }

        DBResultSet dbrs = null;

        try{


            String sql = "SELECT "+PstReason.fieldNames[PstReason.FLD_REASON_ID]+","+
                    PstReason.fieldNames[PstReason.FLD_REASON]+
                    " FROM "+PstReason.TBL_HR_REASON;

            if(reasonRpt.length()>0){
                sql = sql + " WHERE "+reasonRpt;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            Vector list = new Vector();

            while(rs.next()){

                Reason reason = new Reason();
                reason.setOID(rs.getLong(PstReason.fieldNames[PstReason.FLD_REASON_ID]));
                reason.setReason(rs.getString(PstReason.fieldNames[PstReason.FLD_REASON]));
                list.add(reason);
            }
            return list;

        }catch(Exception E){
            System.out.println("[exception] "+E.toString());
        }finally{
            DBResultSet.close(dbrs);
        }

        return null;
    }



    /**
     * @return the empNum
     */
    public String getEmpNum() {
        return empNum;
}

    /**
     * @param empNum the empNum to set
     */
    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

    /**
     * @return the empFullName
     */
    public String getEmpFullName() {
        return empFullName;
    }

    /**
     * @param empFullName the empFullName to set
     */
    public void setEmpFullName(String empFullName) {
        this.empFullName = empFullName;
    }
}
