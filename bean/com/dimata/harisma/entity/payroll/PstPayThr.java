/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.Marital;
import com.dimata.harisma.entity.masterdata.PstMarital;
import java.sql.*;
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.Command;
import com.dimata.util.Formater;
import java.util.Arrays;
import java.util.Vector;

/**
 *
 * @author Gunadi
 */
public class PstPayThr extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_PAY_THR = "pay_thr";
    public static final int FLD_PAY_THR_ID = 0;
    public static final int FLD_CALCULATION_MAIN_ID = 1;
    public static final int FLD_PERIOD_ID = 2;
    public static final int FLD_STATUS = 3;
    public static final int FLD_CUT_OFF_DATE = 4;
    public static String[] fieldNames = {
        "PAY_THR_ID",
        "CALCULATION_MAIN_ID",
        "PERIOD_ID",
        "STATUS",
        "CUT_OFF_DATE"
    };
    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_DATE
    };

    public PstPayThr() {
    }

    public PstPayThr(int i) throws DBException {
        super(new PstPayThr());
    }

    public PstPayThr(String sOid) throws DBException {
        super(new PstPayThr(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstPayThr(long lOid) throws DBException {
        super(new PstPayThr(0));
        String sOid = "0";
        try {
            sOid = String.valueOf(lOid);
        } catch (Exception e) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public String getTableName() {
        return TBL_PAY_THR;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstPayThr().getClass().getName();
    }

    public static PayThr fetchExc(long oid) throws DBException {
        try {
            PayThr entPayThr = new PayThr();
            PstPayThr pstPayThr = new PstPayThr(oid);
            entPayThr.setOID(oid);
            entPayThr.setCalculationMainId(pstPayThr.getLong(FLD_CALCULATION_MAIN_ID));
            entPayThr.setPeriodId(pstPayThr.getLong(FLD_PERIOD_ID));
            entPayThr.setStatus(pstPayThr.getInt(FLD_STATUS));
            entPayThr.setCutOffDate(pstPayThr.getDate(FLD_CUT_OFF_DATE));
            return entPayThr;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThr(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        PayThr entPayThr = fetchExc(entity.getOID());
        entity = (Entity) entPayThr;
        return entPayThr.getOID();
    }

    public static synchronized long updateExc(PayThr entPayThr) throws DBException {
        try {
            if (entPayThr.getOID() != 0) {
                PstPayThr pstPayThr = new PstPayThr(entPayThr.getOID());
                pstPayThr.setLong(FLD_CALCULATION_MAIN_ID, entPayThr.getCalculationMainId());
                pstPayThr.setLong(FLD_PERIOD_ID, entPayThr.getPeriodId());
                pstPayThr.setInt(FLD_STATUS, entPayThr.getStatus());
                pstPayThr.setDate(FLD_CUT_OFF_DATE, entPayThr.getCutOffDate());
                pstPayThr.update();
                return entPayThr.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThr(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((PayThr) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstPayThr pstPayThr = new PstPayThr(oid);
            pstPayThr.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThr(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(PayThr entPayThr) throws DBException {
        try {
            PstPayThr pstPayThr = new PstPayThr(0);
            pstPayThr.setLong(FLD_CALCULATION_MAIN_ID, entPayThr.getCalculationMainId());
            pstPayThr.setLong(FLD_PERIOD_ID, entPayThr.getPeriodId());
            pstPayThr.setInt(FLD_STATUS, entPayThr.getStatus());
            pstPayThr.setDate(FLD_CUT_OFF_DATE, entPayThr.getCutOffDate());
            pstPayThr.insert();
            entPayThr.setOID(pstPayThr.getLong(FLD_PAY_THR_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstPayThr(0), DBException.UNKNOWN);
        }
        return entPayThr.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((PayThr) entity);
    }

    public static void resultToObject(ResultSet rs, PayThr entPayThr) {
        try {
            entPayThr.setOID(rs.getLong(PstPayThr.fieldNames[PstPayThr.FLD_PAY_THR_ID]));
            entPayThr.setCalculationMainId(rs.getLong(PstPayThr.fieldNames[PstPayThr.FLD_CALCULATION_MAIN_ID]));
            entPayThr.setPeriodId(rs.getLong(PstPayThr.fieldNames[PstPayThr.FLD_PERIOD_ID]));
            entPayThr.setStatus(rs.getInt(PstPayThr.fieldNames[PstPayThr.FLD_STATUS]));
            entPayThr.setCutOffDate(rs.getDate(PstPayThr.fieldNames[PstPayThr.FLD_CUT_OFF_DATE]));
        } catch (Exception e) {
        }
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_THR;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }
            if (limitStart == 0 && recordToGet == 0) {
                sql = sql + "";
            } else {
                sql = sql + " LIMIT " + limitStart + "," + recordToGet;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                PayThr entPayThr = new PayThr();
                resultToObject(rs, entPayThr);
                lists.add(entPayThr);
            }
            rs.close();
            return lists;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }

    public static boolean checkOID(long entPayThrId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_PAY_THR + " WHERE "
                    + PstPayThr.fieldNames[PstPayThr.FLD_PAY_THR_ID] + " = " + entPayThrId;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                result = true;
            }
            rs.close();
        } catch (Exception e) {
            System.out.println("err : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            return result;
        }
    }

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstPayThr.fieldNames[PstPayThr.FLD_PAY_THR_ID] + ") FROM " + TBL_PAY_THR;
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt(1);
            }
            rs.close();
            return count;
        } catch (Exception e) {
            return 0;
        } finally {
            DBResultSet.close(dbrs);
        }
    }

    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    PayThr entPayThr = (PayThr) list.get(ls);
                    if (oid == entPayThr.getOID()) {
                        found = true;
                    }
                }
            }
        }
        if ((start >= size) && (size > 0)) {
            start = start - recordToGet;
        }
        return start;
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + (recordToGet - mdl);
        if (start == 0) {
            cmd = Command.FIRST;
        } else {
            if (start == (vectSize - recordToGet)) {
                cmd = Command.LAST;
            } else {
                start = start + recordToGet;
                if (start <= (vectSize - recordToGet)) {
                    cmd = Command.NEXT;
                    System.out.println("next.......................");
                } else {
                    start = start - recordToGet;
                    if (start > 0) {
                        cmd = Command.PREV;
                        System.out.println("prev.......................");
                    }
                }
            }
        }
        return cmd;
    }
    
    public static String getExclusionEmployee(long oidConfigMain, java.util.Date cutOffDate, long periodId){
        
        String whereExclusion  = PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_CALCULATION_MAIN_ID]+ " = "
                                + oidConfigMain + " AND "+ PstThrCalculationMapping.fieldNames[PstThrCalculationMapping.FLD_MAPPING_TYPE]
                                + " = " + PstThrCalculationMapping.TYPE_EXCLUSION;
        Vector listExclusion = PstThrCalculationMapping.list(0, 0, whereExclusion, "");
        String notIn = "";
        
        PayPeriod payPeriod = new PayPeriod();
        try {
            payPeriod = PstPayPeriod.fetchExc(periodId);
        } catch (Exception exc){
            
        }
        
        if (listExclusion != null && listExclusion.size()>0){
            for (int i=0; i < listExclusion.size();i++){
                ThrCalculationMapping mapping = (ThrCalculationMapping) listExclusion.get(i);
                
                String exclusion="";
                
                long mCompanyId = mapping.getCompanyId();
                long mDivisionId = mapping.getDivisionId();
                long mDepartmentId = mapping.getDepartmentId();
                long mSection = mapping.getSectionId();
                long mMarital = mapping.getMaritalId();
                long mTaxMarital = mapping.getTaxMaritalId();
                long mEmployeeCategory = mapping.getEmployeeCategory();
                long mPosition = mapping.getPositionId();
                long mGrade = mapping.getGrade();
                long mEmployee = mapping.getEmployeeId();
                long mPayrollGroupId = mapping.getPayrollGroupId();
                int losFromInDay = mapping.getLosFromInDay();
                int losFromInMonth = mapping.getLosFromInMonth();
                int losFromInYear = mapping.getLosFromInYear();
                int losToInDay = mapping.getLosToInDay();
                int losToInMonth = mapping.getLosToInMonth();
                int losToInYear = mapping.getLosToInYear();
                
                if ((mCompanyId != 0) && (mCompanyId > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+"!="+mCompanyId;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+"!="+mCompanyId;
                    }
                }

                if ((mDivisionId != 0) && (mDivisionId > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+"!="+mDivisionId;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+"!="+mDivisionId;
                    }
                }
                
                if ((mDepartmentId != 0) && (mDepartmentId > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+"!="+mDepartmentId;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+"!="+mDepartmentId;
                    }
                }
                if ((mSection != 0) && (mSection > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+"!="+mSection;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+"!="+mSection;
                    }
                }
                if ((mMarital != 0) && (mMarital > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]+"!="+mMarital;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_MARITAL_ID]+"!="+mMarital;
                    }
                }
                if ((mTaxMarital != 0) && (mTaxMarital > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID]+"!="+mTaxMarital;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_TAX_MARITAL_ID]+"!="+mTaxMarital;
                    }
                }
                if ((mEmployeeCategory != 0) && (mEmployeeCategory > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]+"!="+mEmployeeCategory;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]+"!="+mEmployeeCategory;
                    }
                }
                if ((mPosition != 0) && (mPosition > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]+"!="+mPosition;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_POSITION_ID]+"!="+mPosition;
                    }
                }
                if ((mGrade != 0) && (mGrade > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID]+"!="+mGrade;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_GRADE_LEVEL_ID]+"!="+mGrade;
                    }
                }
                if ((mEmployee != 0) && (mEmployee > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+"!="+mEmployee;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+"!="+mEmployee;
                    }
                }
                if ((mPayrollGroupId != 0) && (mPayrollGroupId > 0)) {
                    if (exclusion.equals("")){
                        exclusion += PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+"!="+mPayrollGroupId;
                    } else {
                        exclusion += " AND "+PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+"!="+mPayrollGroupId;
                    }
                }
                if ((losToInDay != 0) && (losToInDay > 0)) {
                    if (exclusion.equals("")){
                        exclusion += "TIMESTAMPDIFF(DAY, '"+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+"',"
                                + "'"+Formater.formatDate(cutOffDate,"yyyy-MM-dd")+"') BETWEEN "+losFromInDay+" AND "+losToInDay;
                    } else {
                        exclusion += " AND TIMESTAMPDIFF(DAY, '"+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+"',"
                                + "'"+Formater.formatDate(cutOffDate,"yyyy-MM-dd")+"') BETWEEN "+losFromInDay+" AND "+losToInDay;
                    }
                }
                if ((losToInMonth != 0) && (losToInMonth > 0)) {
                    if (exclusion.equals("")){
                        exclusion += "TIMESTAMPDIFF(MONTH, '"+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+"',"
                                + "'"+Formater.formatDate(cutOffDate,"yyyy-MM-dd")+"') BETWEEN "+losFromInMonth+" AND "+losToInMonth;
                    } else {
                        exclusion += " AND TIMESTAMPDIFF(MONTH, '"+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+"',"
                                + "'"+Formater.formatDate(cutOffDate,"yyyy-MM-dd")+"') BETWEEN "+losFromInMonth+" AND "+losToInMonth;
                    }
                }
                if ((losToInYear != 0) && (losToInYear > 0)) {
                    if (exclusion.equals("")){
                        exclusion += "TIMESTAMPDIFF(YEAR, '"+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+"',"
                                + "'"+Formater.formatDate(cutOffDate,"yyyy-MM-dd")+"') BETWEEN "+losFromInYear+" AND "+losToInYear;
                    } else {
                        exclusion += " AND TIMESTAMPDIFF(YEAR, '"+PstEmployee.fieldNames[PstEmployee.FLD_COMMENCING_DATE]+"',"
                                + "'"+Formater.formatDate(cutOffDate,"yyyy-MM-dd")+"') BETWEEN "+losFromInYear+" AND "+losToInYear;
                    }
                }
                
                if (notIn.equals("")){
                    notIn += "("+exclusion+")";
                } else {
                    notIn += " AND ("+exclusion+")";
                }
                
            }
        }
        
        if (notIn.equals("")){
            notIn += "("+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+"="+PstEmployee.NO_RESIGN
                    +" OR "+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+"> '"
                    +Formater.formatDate(payPeriod.getStartDate(),"yyyy-MM-dd")+"')";
        } else {
            notIn += " AND ("+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]+"="+PstEmployee.NO_RESIGN
                    +" OR "+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]+"> '"
                    +Formater.formatDate(payPeriod.getStartDate(),"yyyy-MM-dd")+"')";
        }
        
        return notIn;
    }
    
    
    public static String sqlData(String where, long periodId, int withEmpId){
        String sql = "";
        
        String[] dataJoin = new String[7];
        
        String strSelect = "SELECT DISTINCT ";
        if (withEmpId != 0){
            strSelect += " hr_employee.EMPLOYEE_ID, ";
        }
        String strJoin = "";
        int inc = 0;
        
        String whereData = " RPT_SETUP_DATA_TYPE = 0 AND RPT_SETUP_DATA_GROUP = 0";
        Vector listData = PstThrRptSetup.list(0, 0, whereData, "");
        
        /* Array join */
        int[] joinCollection = new int[PstThrRptSetup.joinDataPriority.length];
        /* inisialisasi joinCollection */
        for(int d=0; d<PstThrRptSetup.joinDataPriority.length; d++){
            joinCollection[d] = -1;
        }
        boolean found = false;
        if (listData != null && listData.size() > 0){
            int incC = 0;
            for(int i=0;i<listData.size(); i++){
                ThrRptSetup thrRpt = (ThrRptSetup)listData.get(i);
                /* mendapatkan join data */
                for(int k=0; k<PstThrRptSetup.joinData.length; k++){
                    for (String retval : PstThrRptSetup.joinData[k].split(" ")) {
                        dataJoin[inc] = retval;
                        inc++;
                    }
                    inc = 0;

                    /* bandingkan nilai table dengan data join */


                if (thrRpt.getRptSetupTableName().equals(dataJoin[2])){
                    /* cek data join pada array joinCollection */
                    for(int c=0; c<joinCollection.length; c++){
                        if (PstThrRptSetup.joinDataPriority[k] == joinCollection[c]){
                            found = true; /* jika found == true, maka data sudah ada di joinCollection */
                        }
                    }
                    if (found == false){
                        joinCollection[incC] = PstCustomRptConfig.joinDataPriority[k];
                    }
                    found = false;
                }    

                }
                incC++;
                /* mendapatkan data select */
                strSelect += thrRpt.getRptSetupTableName()+"."+thrRpt.getRptSetupFieldName();
                if (i == listData.size()-1){
                    strSelect +=" ";
                } else {
                    strSelect += ", ";
                }
            }
            /* join Collection */
            Arrays.sort(joinCollection); /* sorting array */
            for(int m=0; m<joinCollection.length; m++){
                if (joinCollection[m] != -1){
                    strJoin += PstCustomRptConfig.joinData[joinCollection[m]] + " ";
                }
            }
        }
        
        sql = strSelect+ " FROM hr_employee "+strJoin+" WHERE "+where+" AND pay_slip.PERIOD_ID = "+periodId;
        if (sql.contains("pay_emp_level")){
            PayPeriod payPeriod = new PayPeriod();
            try {
                payPeriod = PstPayPeriod.fetchExc(periodId);
            } catch (Exception exc){
                
            }
            sql += " AND '"+payPeriod.getStartDate()+"' BETWEEN `pay_emp_level`.`START_DATE` AND `pay_emp_level`.`END_DATE`";
        }
        return sql;
    }
    
    public static Vector listData(String sqlData, Vector data) {
        /*
         * sqlData : nilai query hasil generate
         * data : adalah data list yg di SELECT
         */
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = sqlData;       
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                CustomRptDynamic dyc = new CustomRptDynamic();
                for(int i=0;i<data.size(); i++){
                    ThrRptSetup thrRpt = (ThrRptSetup)data.get(i);
                    //if ini mendadak untuk tax status 
                    if (thrRpt.getRptSetupFieldName().equals("MARITAL_STATUS_TAX")){
                            Vector maritalS = PstMarital.list(0, 0, "MARITAL_STATUS_TAX = " + rs.getString(thrRpt.getRptSetupFieldName()), "");
                            Marital marital = (Marital) maritalS.get(0);
                            dyc.setFields(thrRpt.getRptSetupFieldName(), marital.getMaritalCode());
                    } else if (thrRpt.getRptSetupFieldName().equals("SEX")){
                            String sex = rs.getString(thrRpt.getRptSetupFieldName());
                            String sSex= (sex.equals("0")?"Laki-laki":"Perempuan");
                            dyc.setFields(thrRpt.getRptSetupFieldName(), sSex);
                    } else{
                        if (rs.getString(thrRpt.getRptSetupFieldName()) != null){
                            dyc.setFields(thrRpt.getRptSetupFieldName(), rs.getString(thrRpt.getRptSetupFieldName()));
                        } else {
                            dyc.setFields(thrRpt.getRptSetupFieldName(), "0");
                        }
                    }
                }
      
                lists.add(dyc);
                
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
    public static Vector listEmployee(String sqlData){
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            String sql = sqlData;       
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                
                long employeeId = rs.getLong(1);
      
                lists.add(employeeId);
                
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Vector();
    }
    
}
