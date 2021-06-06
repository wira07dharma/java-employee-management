/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

/**
 *
 * @author Hendra Putu
 */
import com.dimata.harisma.entity.payroll.PaySlip;
import com.dimata.harisma.entity.payroll.PstPaySlip;
import com.dimata.qdep.db.DBException;
import com.dimata.qdep.db.DBHandler;
import com.dimata.qdep.db.DBResultSet;
import com.dimata.qdep.db.I_DBInterface;
import com.dimata.qdep.db.I_DBType;
import com.dimata.qdep.entity.Entity;
import com.dimata.qdep.entity.I_PersintentExc;
import com.dimata.util.Command;
import com.dimata.util.lang.I_Language;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.util.Vector;

public class PstComponentCoaMap extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_COMPONENT_COA_MAP = "pay_component_coa_map";
    public static final int FLD_COMPONENT_COA_MAP_ID = 0;
    public static final int FLD_FORMULA = 1;
    public static final int FLD_GEN_ID = 2;
    public static final int FLD_DIVISION_ID = 3;
    public static final int FLD_DEPARTMENT_ID = 4;
    public static final int FLD_SECTION_ID = 5;
    public static final int FLD_ID_PERKIRAAN = 6;

    public static String[] fieldNames = {
        "COMPONENT_COA_MAP_ID",
        "FORMULA",
        "GEN_ID",
        "DIVISION_ID",
        "DEPARTMENT_ID",
        "SECTION_ID",
        "ID_PERKIRAAN"
    };

    public static int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,
        TYPE_STRING,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_LONG
    };

    public PstComponentCoaMap() {
    }

    public PstComponentCoaMap(int i) throws DBException {
        super(new PstComponentCoaMap());
    }

    public PstComponentCoaMap(String sOid) throws DBException {
        super(new PstComponentCoaMap(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstComponentCoaMap(long lOid) throws DBException {
        super(new PstComponentCoaMap(0));
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
        return TBL_COMPONENT_COA_MAP;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstComponentCoaMap().getClass().getName();
    }

    public static ComponentCoaMap fetchExc(long oid) throws DBException {
        try {
            ComponentCoaMap entComponentCoaMap = new ComponentCoaMap();
            PstComponentCoaMap pstComponentCoaMap = new PstComponentCoaMap(oid);
            entComponentCoaMap.setOID(oid);
            entComponentCoaMap.setFormula(pstComponentCoaMap.getString(FLD_FORMULA));
            entComponentCoaMap.setGenId(pstComponentCoaMap.getLong(FLD_GEN_ID));
            entComponentCoaMap.setDivisionId(pstComponentCoaMap.getLong(FLD_DIVISION_ID));
            entComponentCoaMap.setDepartmentId(pstComponentCoaMap.getLong(FLD_DEPARTMENT_ID));
            entComponentCoaMap.setSectionId(pstComponentCoaMap.getLong(FLD_SECTION_ID));
            entComponentCoaMap.setIdPerkiraan(pstComponentCoaMap.getLong(FLD_ID_PERKIRAAN));
            return entComponentCoaMap;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstComponentCoaMap(0), DBException.UNKNOWN);
        }
    }

    public long fetchExc(Entity entity) throws Exception {
        ComponentCoaMap entComponentCoaMap = fetchExc(entity.getOID());
        entity = (Entity) entComponentCoaMap;
        return entComponentCoaMap.getOID();
    }

    public static synchronized long updateExc(ComponentCoaMap entComponentCoaMap) throws DBException {
        try {
            if (entComponentCoaMap.getOID() != 0) {
                PstComponentCoaMap pstComponentCoaMap = new PstComponentCoaMap(entComponentCoaMap.getOID());
                pstComponentCoaMap.setString(FLD_FORMULA, entComponentCoaMap.getFormula());
                pstComponentCoaMap.setLong(FLD_GEN_ID, entComponentCoaMap.getGenId());
                pstComponentCoaMap.setLong(FLD_DIVISION_ID, entComponentCoaMap.getDivisionId());
                pstComponentCoaMap.setLong(FLD_DEPARTMENT_ID, entComponentCoaMap.getDepartmentId());
                pstComponentCoaMap.setLong(FLD_SECTION_ID, entComponentCoaMap.getSectionId());
                pstComponentCoaMap.setLong(FLD_ID_PERKIRAAN, entComponentCoaMap.getIdPerkiraan());
                pstComponentCoaMap.update();
                return entComponentCoaMap.getOID();
            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstComponentCoaMap(0), DBException.UNKNOWN);
        }
        return 0;
    }

    public long updateExc(Entity entity) throws Exception {
        return updateExc((ComponentCoaMap) entity);
    }

    public static synchronized long deleteExc(long oid) throws DBException {
        try {
            PstComponentCoaMap pstComponentCoaMap = new PstComponentCoaMap(oid);
            pstComponentCoaMap.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstComponentCoaMap(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public long deleteExc(Entity entity) throws Exception {
        if (entity == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(entity.getOID());
    }

    public static synchronized long insertExc(ComponentCoaMap entComponentCoaMap) throws DBException {
        try {
            PstComponentCoaMap pstComponentCoaMap = new PstComponentCoaMap(0);
            pstComponentCoaMap.setString(FLD_FORMULA, entComponentCoaMap.getFormula());
            pstComponentCoaMap.setLong(FLD_GEN_ID, entComponentCoaMap.getGenId());
            pstComponentCoaMap.setLong(FLD_DIVISION_ID, entComponentCoaMap.getDivisionId());
            pstComponentCoaMap.setLong(FLD_DEPARTMENT_ID, entComponentCoaMap.getDepartmentId());
            pstComponentCoaMap.setLong(FLD_SECTION_ID, entComponentCoaMap.getSectionId());
            pstComponentCoaMap.setLong(FLD_ID_PERKIRAAN, entComponentCoaMap.getIdPerkiraan());
            pstComponentCoaMap.insert();
            entComponentCoaMap.setOID(pstComponentCoaMap.getLong(FLD_COMPONENT_COA_MAP_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstComponentCoaMap(0), DBException.UNKNOWN);
        }
        return entComponentCoaMap.getOID();
    }

    public long insertExc(Entity entity) throws Exception {
        return insertExc((ComponentCoaMap) entity);
    }

    public static void resultToObject(ResultSet rs, ComponentCoaMap entComponentCoaMap) {
        try {
            entComponentCoaMap.setOID(rs.getLong(PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_COMPONENT_COA_MAP_ID]));
            entComponentCoaMap.setFormula(rs.getString(PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_FORMULA]));
            entComponentCoaMap.setGenId(rs.getLong(PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_GEN_ID]));
            entComponentCoaMap.setDivisionId(rs.getLong(PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_DIVISION_ID]));
            entComponentCoaMap.setDepartmentId(rs.getLong(PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_DEPARTMENT_ID]));
            entComponentCoaMap.setSectionId(rs.getLong(PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_SECTION_ID]));
            entComponentCoaMap.setIdPerkiraan(rs.getLong(PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_ID_PERKIRAAN]));
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
            String sql = "SELECT * FROM " + TBL_COMPONENT_COA_MAP;
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
                ComponentCoaMap entComponentCoaMap = new ComponentCoaMap();
                resultToObject(rs, entComponentCoaMap);
                lists.add(entComponentCoaMap);
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

    public static boolean checkOID(long entComponentCoaMapId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_COMPONENT_COA_MAP + " WHERE "
                    + PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_COMPONENT_COA_MAP_ID] + " = " + entComponentCoaMapId;
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
            String sql = "SELECT COUNT(" + PstComponentCoaMap.fieldNames[PstComponentCoaMap.FLD_COMPONENT_COA_MAP_ID] + ") FROM " + TBL_COMPONENT_COA_MAP;
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
                    ComponentCoaMap entComponentCoaMap = (ComponentCoaMap) list.get(ls);
                    if (oid == entComponentCoaMap.getOID()) {
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
    
    public static long getCoA(long idPerkiraan, long idPeriod){
        return getCoA(idPerkiraan, idPeriod, 0);
    }
    
    public static long getCoA(long idPerkiraan, long idPeriod, long oidPayrollGroup) {
        
        String whereCoaMap = " ID_PERKIRAAN="+idPerkiraan+" ";
        Vector listCoaMap = list(0, 0, whereCoaMap, "");
        double total = 0;
        String sql = "";
        if (listCoaMap != null && listCoaMap.size()>0){
            String[] wherePaySlip = new String[listCoaMap.size()];
            for (int i=0; i<listCoaMap.size(); i++){
                ComponentCoaMap compCoaMap = (ComponentCoaMap)listCoaMap.get(i);
                sql = "SELECT pay_slip.PAY_SLIP_ID, DIVISION, DEPARTMENT, SECTION FROM pay_slip ";
                if (oidPayrollGroup != 0){
                    sql+="INNER JOIN hr_employee on pay_slip.EMPLOYEE_ID = hr_employee.EMPLOYEE_ID ";
                }
                sql += "WHERE pay_slip.PERIOD_ID="+idPeriod;
                //Disabled 02-10-2017
//                if (compCoaMap.getDivisionId() != 0){
//                    wherePaySlip[i] = " AND DIVISION='"+getDivisionName(compCoaMap.getDivisionId())+"' ";
//                } else {
//                    wherePaySlip[i] = " ";
//                }
//                if (compCoaMap.getDepartmentId() != 0){
//                    wherePaySlip[i] += " AND DEPARTMENT='"+getDepartmentName(compCoaMap.getDepartmentId())+"' ";
//                } else {
//                    wherePaySlip[i] += " ";
//                }
                if (compCoaMap.getDepartmentId() != 0){
                    wherePaySlip[i] = " AND pay_slip.DEPARTMENT='"+getDepartmentName(compCoaMap.getDepartmentId())+"' ";
                } else {
                    wherePaySlip[i] = " ";
                }                
                if (compCoaMap.getSectionId() != 0){
                    wherePaySlip[i] += " AND pay_slip.SECTION='"+getSectionName(compCoaMap.getSectionId())+"' ";
                } else {
                    wherePaySlip[i] += " ";
                }
                
                if (oidPayrollGroup != 0){
                    wherePaySlip[i] += " AND hr_employee.PAYROLL_GROUP='"+oidPayrollGroup+"' ";
                } else {
                    wherePaySlip[i] += " ";
                }
                
                sql = sql + wherePaySlip[i] +" ";
                
                if (compCoaMap.getFormula().equals("TAKE_HOME_PAY")){
                    total = total + prosesPaySlipVer2(sql, "TI,TD");
                } else {
                    total = total + prosesPaySlip(sql, compCoaMap.getFormula());
                }
                
            }
        }
        return convertLong(total);
    }
    
    public static double prosesPaySlip(String sql, String formula){
        double total = 0;
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            long paySlipId = 0;
            String comp = "";
            
            for (String retval : formula.split(",")) {
                comp = comp + "'" + retval + "',";
            }

            comp = comp + "'0'";
            while (rs.next()) {
                String query = "";
                paySlipId = rs.getLong(PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID]);
                query = "SELECT SUM(COMP_VALUE) as total  FROM pay_slip_comp ";
                query +="WHERE PAY_SLIP_ID="+paySlipId+" AND COMP_CODE IN("+comp+")";
                total = total + prosesPaySlipComp(query);
            }
            rs.close();
            return total;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }
    
    public static double prosesPaySlipVer2(String sql, String formula){
        double total = 0;
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            long paySlipId = 0;
            String comp = "";
            
            for (String retval : formula.split(",")) {
                comp = comp + "'" + retval + "',";
            }

            comp = comp + "'0'";
            while (rs.next()) {
                String query = "";
                paySlipId = rs.getLong(PstPaySlip.fieldNames[PstPaySlip.FLD_PAY_SLIP_ID]);
                query = "SELECT pay_slip_comp.COMP_VALUE FROM pay_slip_comp ";
                query +="WHERE PAY_SLIP_ID="+paySlipId+" AND COMP_CODE IN("+comp+")";
                total = total + prosesPaySlipCompVer2(query);

            }
            rs.close();
            return total;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }
    
    public static double prosesPaySlipComp(String sql){
        double total = 0;
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                total = rs.getDouble("total");
            }
            rs.close();
            return total;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }
    
    public static double prosesPaySlipCompVer2(String sql){
        double total = 0;
        double[] nilai = new double[2];
        int inc = 0;
        DBResultSet dbrs = null;
        try {
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                nilai[inc] = rs.getDouble("COMP_VALUE");
                inc++;
            }
            rs.close();
            return nilai[1]-nilai[0];
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return total;
    }
    
    public static String getDivisionName(long oid) {
        String str = "-";
        try {
            Division div = PstDivision.fetchExc(oid);
            str = div.getDivision();
        } catch (Exception ex) {
            System.out.println("getDivisionName()=>" + ex.toString());
        }
        return str;
    }

    public static String getDepartmentName(long oid) {
        String str = "-";
        try {
            Department depart = PstDepartment.fetchExc(oid);
            str = depart.getDepartment();
        } catch (Exception ex) {
            System.out.println("getDepartmentName()=>" + ex.toString());
        }
        return str;
    }

    public static String getSectionName(long oid) {
        String str = "-";
        try {
            Section section = PstSection.fetchExc(oid);
            str = section.getSection();
        } catch (Exception ex) {
            System.out.println("getSectionName()=>" + ex.toString());
        }
        return str;
    }
    
    /* Convert Long */
    public static long convertLong(double val){
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(0, RoundingMode.HALF_DOWN);
        return bDecimal.longValue();
    }
}
