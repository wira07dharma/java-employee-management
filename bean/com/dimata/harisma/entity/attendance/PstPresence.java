
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
 * @version  	: 01
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.attendance; 

/* package java */ 
import java.io.*;
import java.sql.ResultSet;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;

/* package harisma */
import com.dimata.harisma.entity.attendance.*; 
import com.dimata.harisma.entity.masterdata.*; 
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.utility.service.presence.PresenceAnalyser;
import com.dimata.util.CalendarCalc;
import com.dimata.util.Formater;
import com.dimata.util.LogicParser;

public class PstPresence extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language { 

	public static final  String TBL_HR_PRESENCE = "hr_presence";//"HR_PRESENCE";

	public static final  int FLD_PRESENCE_ID = 0;
	public static final  int FLD_EMPLOYEE_ID = 1;
	public static final  int FLD_PRESENCE_DATETIME = 2;
	public static final  int FLD_STATUS = 3;
	public static final  int FLD_ANALYZED = 4;
        public static final  int FLD_TRANSFERRED = 5;
        //update by satrya 2012-08-19
        public static final  int FLD_SCHEDULE_DATETIME = 6;
        public static final  int FLD_SCHEDULE_TYPE = 7;
        public static final  int FLD_OID_SCHEDULE_LEAVE = 8;
        public static final  int FLD_PERIOD_ID = 9;
        
        //public static final  int FLD_STATION = 10;

	public static final  String[] fieldNames = {
		"PRESENCE_ID",
		"EMPLOYEE_ID",
		"PRESENCE_DATETIME",
		"STATUS",
                "ANALYZED",
                "TRANSFERRED",
                //update by satrya 2012-08-08
                "SCHEDULE_DATETIME",
                "SCHEDULE_TYPE",
                "OID_SCHEDULE_LEAVE",
                "PERIOD_ID"
                //,"STATION"
	 }; 

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
		TYPE_DATE,
		TYPE_INT,
		TYPE_INT,                
                TYPE_INT,
                //update by satrya 2012-08-19
                TYPE_DATE,
                TYPE_INT,
                TYPE_LONG,
                TYPE_LONG
                //,TYPE_STRING
	 }; 


        public static final int PRESENCE_NOT_TRANSFERRED = 0;
	public static final int PRESENCE_TRANSFERRED = 1;
        
        /**
         * @Desc untuk status transfer presence
         */
	public static final  String[] strPresenceTransfer = {
            "Presence not transferred yet",
            "Presence transferred"
	 };         
         
	public PstPresence(){
	}

	public PstPresence(int i) throws DBException { 
		super(new PstPresence()); 
	}

	public PstPresence(String sOid) throws DBException { 
		super(new PstPresence(0)); 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	}

	public PstPresence(long lOid) throws DBException { 
		super(new PstPresence(0)); 
		String sOid = "0"; 
		try { 
			sOid = String.valueOf(lOid); 
		}catch(Exception e) { 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		if(!locate(sOid)) 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		else 
			return; 
	} 

	public int getFieldSize(){ 
		return fieldNames.length; 
	}

	public String getTableName(){ 
		return TBL_HR_PRESENCE;
	}

	public String[] getFieldNames(){ 
		return fieldNames; 
	}

	public int[] getFieldTypes(){ 
		return fieldTypes; 
	}

	public String getPersistentName(){ 
		return new PstPresence().getClass().getName(); 
	}

	public long fetchExc(Entity ent) throws Exception{ 
		Presence presence = fetchExc(ent.getOID()); 
		ent = (Entity)presence; 
		return presence.getOID(); 
	}

	public long insertExc(Entity ent) throws Exception{ 
		return insertExc((Presence) ent); 
	}

	public long updateExc(Entity ent) throws Exception{ 
		return updateExc((Presence) ent); 
	}

	public long deleteExc(Entity ent) throws Exception{ 
		if(ent==null){ 
			throw new DBException(this,DBException.RECORD_NOT_FOUND); 
		} 
		return deleteExc(ent.getOID()); 
	}
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

	public static Presence fetchExc(long oid) throws DBException{ 
		try{ 
			Presence presence = new Presence();
			PstPresence pstPresence = new PstPresence(oid); 
			presence.setOID(oid);

			presence.setEmployeeId(pstPresence.getlong(FLD_EMPLOYEE_ID));
			presence.setPresenceDatetime(pstPresence.getDate(FLD_PRESENCE_DATETIME));
			presence.setStatus(pstPresence.getInt(FLD_STATUS));
			presence.setAnalyzed(pstPresence.getInt(FLD_ANALYZED));
                        presence.setTransferred(pstPresence.getInt(FLD_TRANSFERRED));
                        //update by satrya 2012-08-19
                        presence.setScheduleDatetime(pstPresence.getDate(FLD_SCHEDULE_DATETIME));
                        presence.setScheduleType(pstPresence.getInt(FLD_SCHEDULE_TYPE));
                        presence.setScheduleLeaveId(pstPresence.getlong(FLD_OID_SCHEDULE_LEAVE));
                        presence.setPeriodId(pstPresence.getlong(FLD_PERIOD_ID));
                        //presence.setStation(pstPresence.getString(FLD_STATION));
			return presence; 
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
		} 
	}

	public static long insertExc(Presence presence) throws DBException{ 
		try{ 
			PstPresence pstPresence = new PstPresence(0);

			pstPresence.setLong(FLD_EMPLOYEE_ID, presence.getEmployeeId());
			pstPresence.setDate(FLD_PRESENCE_DATETIME, presence.getPresenceDatetime());
			pstPresence.setInt(FLD_STATUS, presence.getStatus());
			pstPresence.setInt(FLD_ANALYZED, presence.getAnalyzed());
                        pstPresence.setInt(FLD_TRANSFERRED, presence.getTransferred());
                        //update by satrya 2012-08-19
                        pstPresence.setDate(FLD_SCHEDULE_DATETIME, presence.getScheduleDatetime());
                        pstPresence.setInt(FLD_SCHEDULE_TYPE, presence.getScheduleType());
                        pstPresence.setLong(FLD_OID_SCHEDULE_LEAVE, presence.getScheduleLeaveId());
                        pstPresence.setLong(FLD_PERIOD_ID, presence.getPeriodId());
                        //pstPresence.setString(FLD_STATION, presence.getStation());
			pstPresence.insert(); 
			presence.setOID(pstPresence.getlong(FLD_PRESENCE_ID));
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
		}
                //update by satrya 2013-08-12
                finally{
                    return presence.getOID();
                }
		
	}

	public static long updateExc(Presence presence) throws DBException{ 
		try{ 
			if(presence.getOID() != 0){ 
				PstPresence pstPresence = new PstPresence(presence.getOID());

				pstPresence.setLong(FLD_EMPLOYEE_ID, presence.getEmployeeId());
				pstPresence.setDate(FLD_PRESENCE_DATETIME, presence.getPresenceDatetime());
				pstPresence.setInt(FLD_STATUS, presence.getStatus());
				pstPresence.setInt(FLD_ANALYZED, presence.getAnalyzed());
                                pstPresence.setInt(FLD_TRANSFERRED, presence.getTransferred());
                               //update by satrya 2012-08-19
                                if(presence.getScheduleDatetime()!=null){
                                pstPresence.setDate(FLD_SCHEDULE_DATETIME, presence.getScheduleDatetime());
                                }
                                pstPresence.setInt(FLD_SCHEDULE_TYPE, presence.getScheduleType());
                                pstPresence.setLong(FLD_OID_SCHEDULE_LEAVE, presence.getScheduleLeaveId());
                                pstPresence.setLong(FLD_PERIOD_ID, presence.getPeriodId());
				//pstPresence.setString(FLD_STATION, presence.getStation());
				pstPresence.update(); 
                                //System.out.println("... updatePresence, set transferred to " + presence.getTransferred());                                  
				return presence.getOID();
                                

			}
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
		}
		return 0;
	}
//update by satrya 2012-09-03
/**
 * 
 * @param presence
 * @return
 * @throws DBException
 * Keterangan: untuk update status Presence berdasarkan Presence
 */
        public static int updateStatusByDateTimeAndEmployeeId(Presence presence) throws DBException{ 
           int result = 0;
            DBResultSet dbrs = null;

            try {
                String sql = " UPDATE " + TBL_HR_PRESENCE
                            + " SET " + PstPresence.fieldNames[PstPresence.FLD_STATUS] 
                            + "=\" "+ presence.getStatus()+"\""
                            + "," + PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_TYPE]
                            + "=\""+ presence.getScheduleType()+"\"" 
                            + "," + PstPresence.fieldNames[PstPresence.FLD_OID_SCHEDULE_LEAVE]
                            + "=\""+presence.getScheduleLeaveId()+"\""
                            + "," + PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME]
                        //update by satrya 2012-09-28
                            + "=\""+Formater.formatDate(presence.getScheduleDatetime(), "yyyy-MM-dd HH:mm:ss")+"\"" 
                            + " WHERE " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                            + "=\"" + Formater.formatDate(presence.getPresenceDatetime(), "yyyy-MM-dd HH:mm:ss")+"\""  
                            + " AND " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                            + " =\""+ presence.getEmployeeId()+ "\"";

                //System.out.println("\tupdateScheduleDataByPresence : "+sql);  
                result = DBHandler.execUpdate(sql);                
            }
            catch(Exception e) 
            {
                System.out.println("Exc updateStatusByDateTimeAndEmployeeId : "+"EmployeeId"+presence.getEmployeeId()+"presence:"+""+presence.getPresenceDatetime()+e.toString());
            }
            finally 
            {
                DBResultSet.close(dbrs);
                return result;
            }                  
        }
	public static long deleteExc(long oid) throws DBException{ 
		try{ 
			PstPresence pstPresence = new PstPresence(oid);
			pstPresence.delete();
		}catch(DBException dbe){ 
			throw dbe; 
		}catch(Exception e){ 
			throw new DBException(new PstPresence(0),DBException.UNKNOWN); 
		}
		return oid;
	}

	public static Vector listAll(){ 
		return list(0, 500, "",""); 
	}

        /**
         * mencari datePresence
         * create by satrya 2013-07-25
         * @param limitStart
         * @param recordToGet
         * @param whereClause
         * @param order
         * @return Date 
         */
	public static Date getDatePresence(int limitStart,int recordToGet, String whereClause, String order){
		Date dtPresence=null;
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_PRESENCE; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause; 
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + ""; 
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;                        
                       //System.out.println("presence list sql : " + sql);
			dbrs = DBHandler.execQueryResult(sql);
                        //System.out.println("presence list sql : " + sql);
			ResultSet rs = dbrs.getResultSet();                        
			while(rs.next()) {                                
				dtPresence = DBHandler.convertDate(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]),rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
			}                        
			rs.close();                        
			return dtPresence;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return dtPresence;
	}
        
        
        public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_PRESENCE; 
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause; 
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + ""; 
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;                        
                       //System.out.println("presence list sql : " + sql);
			dbrs = DBHandler.execQueryResult(sql);
                        //System.out.println("presence list sql : " + sql);
			ResultSet rs = dbrs.getResultSet();                        
			while(rs.next()) {                                
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}                        
			rs.close();                        
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}
        
        //update by satrya 2012-07-02
        /*public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection, String payrolNum){
            return list(limitStart, recordToGet, order,  oidDepartement, employeeName, fromDate, toDate , oidSection, payrolNum, -1);
        
        }*/
        
    public static String getEmployee(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection, String payrolNum,Vector stsPresence){

		String sEmployeeId = ""; 
             if(fromDate!=null && toDate!=null){   
		DBResultSet dbrs = null;
		try {
                        String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]  +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE + " AS PS INNER JOIN " +PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                      + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " WHERE (1=1)";
                                    
                                     
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        
                        if(employeeName !=null && employeeName.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE \"%"+employeeName+"%\"";
                           Vector vectFullName = logicParser(employeeName);
                           sql = sql + " AND ";
                         if (vectFullName != null && vectFullName.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vectFullName.size(); i++) {
                                 String str = (String) vectFullName.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP. "+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
                        if(payrolNum !=null && payrolNum.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE \"%"+payrolNum+"%\"";
                            Vector vecNum = logicParser(payrolNum);
                           sql = sql + " AND ";
                         if (vecNum != null && vecNum.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vecNum.size(); i++) {
                                 String str = (String) vecNum.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
                         if(fromDate !=null && toDate !=null){
                            //update by satrya 2012-10-15
                             if (fromDate.getTime() > toDate.getTime()) {
                                    Date tempFromDate = fromDate;
                                    Date tempToDate = toDate;
                                    fromDate = tempToDate;
                                    toDate = tempFromDate;
                            }
                             sql = sql + " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN \""+Formater.formatDate(fromDate,"yyyy-MM-dd HH:mm:00") +"\" AND \""+Formater.formatDate(toDate,"yyyy-MM-dd HH:mm:59")+"\"";
                         }
			if(oidSection !=0)
                            sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                          
                        
                         if(stsPresence!=null && stsPresence.size() > 0 ){
                            sql = sql + " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS]  + " IN (";
                                for(int i=0;i<stsPresence.size();i++){
                                    sql= sql + stsPresence.get(i) + ",";
                                }
                            sql= sql.substring(0,sql.length()-1);
                            sql=sql+")" ;
                        }     
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
                        
                        //update by satrya 2012-12-25
                        if(limitStart == 0 && recordToGet == 0){
				sql = sql + "";
                        }
                        else{
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        }
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				
				long employeeId = rs.getLong(fieldNames[FLD_EMPLOYEE_ID]);
                                sEmployeeId = sEmployeeId + employeeId + ",";
                                 
			}
                       //update by satrya 2012-09-27
                        if(sEmployeeId!=null && sEmployeeId.length()>0){
                         sEmployeeId= sEmployeeId.substring(0,sEmployeeId.length()-1);
                        }
			rs.close(); 
                        
			return sEmployeeId;

		}catch(Exception e) {
			System.out.println("Exception getEmployee"+e);
		}finally {
			DBResultSet.close(dbrs);
		}
            }
			return sEmployeeId;
	}
       
    /**
     * Keterangan: untuk mengambil data employee yg digunakan di report daily dan summary
     * create by satrya 2013-12-03
     * @param limitStart
     * @param recordToGet
     * @param order
     * @param oidDepartement
     * @param employeeName
     * @param fromDate
     * @param toDate
     * @param oidSection
     * @param payrolNum
     * @param stsPresence
     * @param oidCompany
     * @param oidDivision
     * @return 
     */
    public static String getEmployee(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection, String payrolNum,Vector stsPresence,long oidCompany,long oidDivision){

		String sEmployeeId = ""; 
             if(fromDate!=null && toDate!=null){   
		DBResultSet dbrs = null;
		try {
                        String sql = "SELECT EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]  +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE + " AS PS INNER JOIN " +PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                      + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " WHERE (1=1)";
                                    
                                     
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        //update by satrya 2013-11-03
                        if(oidCompany !=0){
                                sql = sql + " AND EMP." +PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + "=\""+oidCompany+"\"";
                        }
                         //update by satrya 2013-11-03
                        if(oidDivision !=0){
                                sql = sql + " AND EMP." +PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=\""+oidDivision+"\"";
                        }
                        if(employeeName !=null && employeeName.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE \"%"+employeeName+"%\"";
                           Vector vectFullName = logicParser(employeeName);
                           sql = sql + " AND ";
                         if (vectFullName != null && vectFullName.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vectFullName.size(); i++) {
                                 String str = (String) vectFullName.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP. "+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
                        if(payrolNum !=null && payrolNum.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE \"%"+payrolNum+"%\"";
                            Vector vecNum = logicParser(payrolNum);
                           sql = sql + " AND ";
                         if (vecNum != null && vecNum.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vecNum.size(); i++) {
                                 String str = (String) vecNum.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
                         if(fromDate !=null && toDate !=null){
                            //update by satrya 2012-10-15
                             if (fromDate.getTime() > toDate.getTime()) {
                                    Date tempFromDate = fromDate;
                                    Date tempToDate = toDate;
                                    fromDate = tempToDate;
                                    toDate = tempFromDate;
                            }
                             sql = sql + " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN \""+Formater.formatDate(fromDate,"yyyy-MM-dd HH:mm:00") +"\" AND \""+Formater.formatDate(toDate,"yyyy-MM-dd HH:mm:59")+"\"";
                         }
			if(oidSection !=0)
                            sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                          
                        
                         if(stsPresence!=null && stsPresence.size() > 0 ){
                            sql = sql + " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS]  + " IN (";
                                for(int i=0;i<stsPresence.size();i++){
                                    sql= sql + stsPresence.get(i) + ",";
                                }
                            sql= sql.substring(0,sql.length()-1);
                            sql=sql+")" ;
                        }     
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
                        
                        //update by satrya 2012-12-25
                        if(limitStart == 0 && recordToGet == 0){
				sql = sql + "";
                        }
                        else{
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        }
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				
				long employeeId = rs.getLong(fieldNames[FLD_EMPLOYEE_ID]);
                                sEmployeeId = sEmployeeId + employeeId + ",";
                                 
			}
                       //update by satrya 2012-09-27
                        if(sEmployeeId!=null && sEmployeeId.length()>0){
                         sEmployeeId= sEmployeeId.substring(0,sEmployeeId.length()-1);
                        }
			rs.close(); 
                        
			return sEmployeeId;

		}catch(Exception e) {
			System.out.println("Exception getEmployee"+e);
		}finally {
			DBResultSet.close(dbrs);
		}
            }
			return sEmployeeId;
	}
       
        
        
        ///mencari status list Status
        /**
         * Keterangan  : untuk mencari status break IN dan Out dan leave pada hari itu
         * yang di set adalah semua data Presence
         * @param limitStart
         * @param recordToGet
         * @param order
         * @param oidDepartement
         * @param employeeName
         * @param fromDate
         * @param toDate
         * @param oidSection
         * @param payrolNum
         * @return
         * 
         */
        public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection, String payrolNum,Vector stStatus,String stsEmpCategorySel,int statusResign){
	//public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection){
		Vector lists = new Vector(); 
             if(fromDate!=null && toDate!=null){   
		DBResultSet dbrs = null;
		try {
                        String sql = "SELECT PS.* " +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                            sql = sql + " AS PS INNER JOIN " +PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                      + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " LEFT JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " 
                                      + " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] 
                                      + " = DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" 
                                      + " WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0";
                                    
                                     
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        if(employeeName !=null && employeeName.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE \"%"+employeeName+"%\"";
                           Vector vectFullName = logicParser(employeeName);
                           sql = sql + " AND ";
                         if (vectFullName != null && vectFullName.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vectFullName.size(); i++) {
                                 String str = (String) vectFullName.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP. "+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
//update by satrya 2013-08-16
            if(statusResign==PstEmployee.YES_RESIGN){
                    //untuk mencari karyawan risigned
//               sql += "  AND (( EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " +PstEmployee.YES_RESIGN + " AND " + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
//                    + " BETWEEN \"" + Formater.formatDate(selectedDate, "yyyy-MM-dd  00:00:00")+ "\""  +" AND "+ "\"" + Formater.formatDate(selectedDate, "yyyy-MM-dd  23:59:59")+ "\"" 
//                    + " ) OR (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " + PstEmployee.NO_RESIGN + "))";
                sql += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] +" = "+ PstEmployee.YES_RESIGN;
            }else if(statusResign==PstEmployee.NO_RESIGN){
                 sql += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] +" = "+ PstEmployee.NO_RESIGN;
            }
            if(stsEmpCategorySel!=null && stsEmpCategorySel.length()>0){
                stsEmpCategorySel = stsEmpCategorySel.substring(0,stsEmpCategorySel.length()-1);
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]
                        + " IN  (" + stsEmpCategorySel +")";
            }
                        if(payrolNum !=null && payrolNum.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE \"%"+payrolNum+"%\"";
                            Vector vecNum = logicParser(payrolNum);
                           sql = sql + " AND ";
                         if (vecNum != null && vecNum.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vecNum.size(); i++) {
                                 String str = (String) vecNum.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
                         if(fromDate !=null && toDate !=null)
                            //update by satrya 2012-10-15
                             if (fromDate.getTime() > toDate.getTime()) {
                                    Date tempFromDate = fromDate;
                                    Date tempToDate = toDate;
                                    fromDate = tempToDate;
                                    toDate = tempFromDate;
                            }
                          //update by satrya 2013-03-30
            if(stStatus!=null && stStatus.size()>0){
                if(fromDate !=null && toDate !=null){
                            //update by satrya 2012-10-15
                             if (fromDate.getTime() > toDate.getTime()) {
                                    Date tempFromDate = fromDate;
                                    Date tempToDate = toDate;
                                    fromDate = tempToDate;
                                    toDate = tempFromDate;
                            }
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN \""+Formater.formatDate(fromDate,"yyyy-MM-dd HH:mm:00") +"\" AND \""+Formater.formatDate(toDate,"yyyy-MM-dd HH:mm:59")+"\"";
                         }
                
            }else{
                             //update by satrya 2012-10-01
                             sql = sql + " AND (("+ PstPresence.fieldNames[PstPresence.FLD_STATUS] 
                                       + " IN (" +Presence.STATUS_OUT_PERSONAL +" , " + Presence.STATUS_IN_PERSONAL +"," + Presence.STATUS_OUT_ON_DUTY +" , " + Presence.STATUS_CALL_BACK + ") "
                                       + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\")"
                                       + " OR ("+ PstPresence.fieldNames[PstPresence.FLD_STATUS] 
                                       + " IN (" + Presence.STATUS_OUT_ON_DUTY +" , " + Presence.STATUS_CALL_BACK + ") "
                                       + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\"))";
            }
			if(oidSection !=0)
                            sql = sql + " AND EMP." + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                          
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
                        
                        //update by satrya 2012-12-25
                        if(limitStart == 0 && recordToGet == 0){
				sql = sql + "";
                        }
                        else{
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        }
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
                       //update by satrya 2012-09-27
			rs.close();
                        
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
            }
			return new Vector();
	}
        
        /**
         * Keterangan: fungsi ini untuk report daily
         * create by satrya 2013-12-03
         * @param limitStart
         * @param recordToGet
         * @param order
         * @param oidDepartement
         * @param employeeName
         * @param fromDate
         * @param toDate
         * @param oidSection
         * @param payrolNum
         * @param stStatus
         * @param stsEmpCategorySel
         * @param statusResign
         * @param oidCompany
         * @param oidDivision
         * @return 
         */
        public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection, String payrolNum,Vector stStatus,String stsEmpCategorySel,int statusResign,long oidCompany,long oidDivision){
	//public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection){
		Vector lists = new Vector(); 
             if(fromDate!=null && toDate!=null){   
		DBResultSet dbrs = null;
		try {
                        String sql = "SELECT PS.* " +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                            sql = sql + " AS PS INNER JOIN " +PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                      + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " LEFT JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " 
                                      + " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] 
                                      + " = DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" 
                                      + " WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0";
                                    
                                     
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        if(employeeName !=null && employeeName.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE \"%"+employeeName+"%\"";
                           Vector vectFullName = logicParser(employeeName);
                           sql = sql + " AND ";
                         if (vectFullName != null && vectFullName.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vectFullName.size(); i++) {
                                 String str = (String) vectFullName.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP. "+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
//update by satrya 2013-08-16
            if(statusResign==PstEmployee.YES_RESIGN){
                    //untuk mencari karyawan risigned
//               sql += "  AND (( EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " +PstEmployee.YES_RESIGN + " AND " + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED_DATE]
//                    + " BETWEEN \"" + Formater.formatDate(selectedDate, "yyyy-MM-dd  00:00:00")+ "\""  +" AND "+ "\"" + Formater.formatDate(selectedDate, "yyyy-MM-dd  23:59:59")+ "\"" 
//                    + " ) OR (EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED]
//                    + " = " + PstEmployee.NO_RESIGN + "))";
                sql += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] +" = "+ PstEmployee.YES_RESIGN;
            }else if(statusResign==PstEmployee.NO_RESIGN){
                 sql += " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_RESIGNED] +" = "+ PstEmployee.NO_RESIGN;
            }
            if(stsEmpCategorySel!=null && stsEmpCategorySel.length()>0){
                stsEmpCategorySel = stsEmpCategorySel.substring(0,stsEmpCategorySel.length()-1);
                sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID]
                        + " IN  (" + stsEmpCategorySel +")";
            }
                        if(payrolNum !=null && payrolNum.length() > 0){
//                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE \"%"+payrolNum+"%\"";
                            Vector vecNum = logicParser(payrolNum);
                           sql = sql + " AND ";
                         if (vecNum != null && vecNum.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vecNum.size(); i++) {
                                 String str = (String) vecNum.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
                         if(fromDate !=null && toDate !=null)
                            //update by satrya 2012-10-15
                             if (fromDate.getTime() > toDate.getTime()) {
                                    Date tempFromDate = fromDate;
                                    Date tempToDate = toDate;
                                    fromDate = tempToDate;
                                    toDate = tempFromDate;
                            }
                          //update by satrya 2013-03-30
            if(stStatus!=null && stStatus.size()>0){
                if(fromDate !=null && toDate !=null){
                            //update by satrya 2012-10-15
                             if (fromDate.getTime() > toDate.getTime()) {
                                    Date tempFromDate = fromDate;
                                    Date tempToDate = toDate;
                                    fromDate = tempToDate;
                                    toDate = tempFromDate;
                            }
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN \""+Formater.formatDate(fromDate,"yyyy-MM-dd HH:mm:00") +"\" AND \""+Formater.formatDate(toDate,"yyyy-MM-dd HH:mm:59")+"\"";
                         }
                
            }else{
                             //update by satrya 2012-10-01
                             sql = sql + " AND (("+ PstPresence.fieldNames[PstPresence.FLD_STATUS] 
                                       + " IN (" +Presence.STATUS_OUT_PERSONAL +" , " + Presence.STATUS_IN_PERSONAL +"," + Presence.STATUS_OUT_ON_DUTY +" , " + Presence.STATUS_CALL_BACK + ") "
                                       + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\")"
                                       + " OR ("+ PstPresence.fieldNames[PstPresence.FLD_STATUS] 
                                       + " IN (" + Presence.STATUS_OUT_ON_DUTY +" , " + Presence.STATUS_CALL_BACK + ") "
                                       + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\"))";
            }
			if(oidSection !=0)
                            sql = sql + " AND " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                         
                        //update by satrya 2013-12-03
                        if(oidCompany !=0){
                            sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID] + "=\""+oidCompany+"\"";
                        }
                        //update by satrya 2013-12-03
                        if(oidDivision !=0){
                            sql = sql + " AND EMP." + PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID] + "=\""+oidDivision+"\"";
                        }
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
                        
                        //update by satrya 2012-12-25
                        if(limitStart == 0 && recordToGet == 0){
				sql = sql + "";
                        }
                        else{
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        }
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
                       //update by satrya 2012-09-27
			rs.close();
                        
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
            }
			return new Vector();
	}
       
        /**
         * create by satrya 2013-08-14
         * 
         * @param order
         * @param whereClause
         * @param stStatus
         * @return 
         */
        public static Vector listPresenceForSummaryAttd(String order,String whereClause,Vector stStatus){
	//public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection){
		Vector lists = new Vector(); 
                //berti paling tidak tanggal harus isi
             if(whereClause!=null && whereClause.length()>0){   
		DBResultSet dbrs = null;
		try {
                        String sql = "SELECT PS.* " +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                            sql = sql + " AS PS INNER JOIN " +PstEmployee.TBL_HR_EMPLOYEE + " AS HE "+ " ON HE. "
                                      + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] 
                                      + " LEFT JOIN " + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " 
                                      + " ON (HE. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] 
                                      + " = DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" 
                                      + " WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0";
                                    
                                     
                        if(whereClause!=null && whereClause.length()>0)
                                sql = sql + " AND " + whereClause;
                        
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
                        
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
                       //update by satrya 2012-09-27
			rs.close();
                        
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
            }
			return new Vector();
	}
        /**
         * Keterangan : mencari untuk mencari presence Break IN dan Out diambil dari OidEmployee
         * @param selectDate
         * @param oidEmployee
         * @return 
         */
        public static Vector listPresenceBreakByEmpId(Date selectDate, long oidEmployee){
        Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			
                String sql =  "SELECT HP.*"
                            + " FROM " + PstPresence.TBL_HR_PRESENCE + " AS HP "
                            + " WHERE HP."+PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME]
                            + " BETWEEN " +"\""+Formater.formatDate(selectDate, "yyyy-MM-dd 00:00:00") +"\" AND "
                            + "\""+Formater.formatDate(selectDate, "yyyy-MM-dd 23:59:59") +"\"" 
                            + " AND HP."+PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] 
                            + " = \""+oidEmployee+"\" AND (HP."
                            + PstPresence.fieldNames[PstPresence.FLD_STATUS]
                            + " = " + Presence.STATUS_OUT_PERSONAL +" OR HP. " 
                            + PstPresence.fieldNames[PstPresence.FLD_STATUS]
                            + " = " + Presence.STATUS_IN_PERSONAL + ")" 
                            + " ORDER BY HP." + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] +","
                            +PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]; 
			
                     dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
                    return lists;
	}
/**
 * Keterangan : mencari status presence
 * create by satrya 2013-06-03
 * @param selectDate
 * @param oidEmployee
 * @return 
 */       
public static int getStatusPresence(Date selectDate, long oidEmployee){
        int status=0;
		DBResultSet dbrs = null;
		try {
if(selectDate!=null && oidEmployee!=0){		
                String sql =  " SELECT HP."+PstPresence.fieldNames[PstPresence.FLD_STATUS]
                            + " FROM " + PstPresence.TBL_HR_PRESENCE + " AS HP "
                            + " WHERE HP."+PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                            + " = \""+Formater.formatDate(selectDate, "yyyy-MM-dd HH:mm:ss") + "\" AND " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]+ "="+oidEmployee
                            + " ORDER BY HP." + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] +","
                            +PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]; 
			
                     dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				status = rs.getInt(PstPresence.fieldNames[PstPresence.FLD_STATUS]);
			}
			rs.close();
}	

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}

                    return status;
	}
        /**
         * 
         * @param limitStart
         * @param recordToGet
         * @param order
         * @param oidDepartement
         * @param employeeName
         * @param fromDate
         * @param toDate
         * @param oidSection
         * @param payrolNum
         * @return mencari presence dan di hitung lewat manual calculation
         */
        public static Vector listManualCalculation(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection, String payrolNum, String empCat){
	//public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			//String sql = "SELECT * FROM " + TBL_HR_MACHINE_TRANS; 
                        
                        String sql = "SELECT PS.* " 
                                    +",EMP."+ PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                       // if(oidDepartement !=0 && employeeName !=null && employeeName !="" && oidSection !=0)
                        // if(oidDepartement !=0 && oidSection !=0)
                        //if(oidDepartement !=0)
                            sql = sql + " AS PS INNER JOIN " +
                                       PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +  
                                           " LEFT JOIN "
                        // if(oidDepartement != 0)
                             + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " +
                                           " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" +
                                           " WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0" 
                                           + " AND !("+PstPresence.fieldNames[PstPresence.FLD_STATUS] 
                                           +" IN("+Presence.STATUS_OUT_ON_DUTY+","+Presence.STATUS_CALL_BACK+"))";//jika ada karyawan yg satusnya call BACK aatu out ON dutty
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        
                        if(employeeName !=null && employeeName.length() > 0){
                          //sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE \"%"+employeeName+"%\"";
                            Vector vectFullName = logicParser(employeeName);
                           sql = sql + " AND ";
                         if (vectFullName != null && vectFullName.size() > 0) {
                             sql = sql + " (";
                             for (int i = 0; i < vectFullName.size(); i++) {
                                 String str = (String) vectFullName.get(i);
                                 if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                     sql = sql + " EMP. "+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                             + " LIKE '%" + str.trim() + "%' ";
                                 } else {
                                     sql = sql + str.trim();
                                 }
                             }
                             sql = sql + ")";
                         } 
                        }
                         //update by satrya 2012-08-01
                         if(payrolNum !=null && payrolNum.length() > 0){
                         // sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE \"%"+payrolNum+"%\"";
                             //update by satrya 2012-11-12
                             Vector vecNum = logicParser(payrolNum);
                                 sql = sql + " AND ";
                               if (vecNum != null && vecNum.size() > 0) {
                                   sql = sql + " (";
                                   for (int i = 0; i < vecNum.size(); i++) {
                                       String str = (String) vecNum.get(i);
                                       if (!LogicParser.isInSign(str) && !LogicParser.isInLogEnglish(str)) {
                                           sql = sql + " EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                                   + " LIKE '%" + str.trim() + "%' ";
                                       } else {
                                           sql = sql + str.trim();
                                       }
                                   }
                                   sql = sql + ")";
                               } 
                         }
                         if(fromDate !=null && toDate !=null)
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\"";
                                     
			if(oidSection !=0)
                            sql = sql + " AND EMP. " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                        
                        if(empCat != "" && empCat.length() > 0){
                            sql = sql + " AND EMP. " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " IN ( "+ empCat +" )";
                        }
                       /*if(statusPresence !=-1)
                            sql = sql + " AND PS. " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + "=\""+ statusPresence +"\"";
                        */
                       // sql = sql + " ORDER BY " + ;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0){
				sql = sql + "";
                        }else{
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                        }
			//System.out.println("SQL List : "+sql);
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
                                presence.setEmpNumb(rs.getString(2));//untuk employe number
				lists.add(presence);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}
       
        /**
         * Keterangan : mencari jumlah Presence yg ada berdasarkan parameter yang di pilih
         * @param order
         * @param oidDepartement
         * @param employeeName
         * @param fromDate
         * @param toDate
         * @param oidSection
         * @param payrolNum
         * @return count
         *  //update by satrya 2012-09-19
         */
                public static int getCountManualCalculation(String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection, String payrolNum, String empCat){
	//public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection){
		
		DBResultSet dbrs = null;
		try {
			
                        String sql = "SELECT COUNT(PS."+PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] +")" +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
          
                            sql = sql + " AS PS INNER JOIN " +
                                       PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +  
                                           " LEFT JOIN "
                       
                             + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " +
                                           " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" +
                                           "WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0" ;
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        if(employeeName !=null && employeeName.length() > 0)
                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME] + " LIKE \"%"+employeeName+"%\"";
                         
                         if(payrolNum !=null && payrolNum.length() > 0)
                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM] + " LIKE \"%"+payrolNum+"%\"";
                         
                         if(fromDate !=null && toDate !=null)
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\"";
                                     
			if(oidSection !=0)
                            sql = sql + " AND EMP. " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                        
                        if(empCat != "" && empCat.length() > 0)
                            sql = sql + " AND EMP. " + PstEmployee.fieldNames[PstEmployee.FLD_EMP_CATEGORY_ID] + " IN ( "+ empCat +" )";
                      
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			
			
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			int count = 0;
                        while(rs.next()) {
                        count = rs.getInt(1); 
                        }

                        rs.close();
                        return count;
		}catch(Exception e) {
			return 0; 
		}finally {
			DBResultSet.close(dbrs);
		}
			
	}
/**
 * untuk mencari total count
 * create by satrya 2013-07-26
 * @param order
 * @param fromDate
 * @param toDate
 * @param whereClause
 * @return 
 */                
 public static int getCountAnalyseStatus(String order, Date fromDate, Date toDate,String whereClause){
	//public static Vector list(int limitStart,int recordToGet,  String order, long oidDepartement, String employeeName, Date fromDate, Date toDate , long oidSection){
		
		DBResultSet dbrs = null;
		try {
			
                        String sql = "SELECT COUNT(PS."+PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] +")" +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
          
                            sql = sql + " AS PS INNER JOIN " +
                                       PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +  
                                           " LEFT JOIN "
                       
                             + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " +
                                           " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" +
                                           "WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0" ;

                         if(fromDate !=null && toDate !=null)
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\"";
                                     
			if(whereClause!=null && whereClause.length()>0)
                            sql = sql + " AND " + whereClause;
                        
                      
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			
			
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			int count = 0;
                        while(rs.next()) {
                        count = rs.getInt(1); 
                        }

                        rs.close();
                        return count;
		}catch(Exception e) {
			return 0; 
		}finally {
			DBResultSet.close(dbrs);
		}
			
	}
        //update by Kartika 2012-07-02
        public static Vector listFrom00To2359(int limitStart,int recordToGet,  String order, long oidDepartement, long employeeId, Date fromDate, Date toDate , long oidSection){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			//String sql = "SELECT * FROM " + TBL_HR_MACHINE_TRANS; 
                        
                        String sql = "SELECT PS.* " +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                       // if(oidDepartement !=0 && employeeName !=null && employeeName !="" && oidSection !=0)
                        // if(oidDepartement !=0 && oidSection !=0)
                        //if(oidDepartement !=0)
                            sql = sql + " AS PS INNER JOIN " +
                                       PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +  
                                           " LEFT JOIN "
                        // if(oidDepartement != 0)
                             + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " +
                                           " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" +
                                           "WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0" ;
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        if(employeeId !=0)
                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=\""+employeeId+"\"";
                         
                         if(fromDate !=null && toDate !=null)
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd")+ " 00:00:00"+"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd ") + "23:59:59"+"\"";
                                     
			if(oidSection !=0)
                            sql = sql + " AND SC. " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                        
			if(order != null && order.length() > 0){
				sql = sql + " ORDER BY " + order;
                        }else{
				sql = sql + " ORDER BY " + fieldNames[FLD_PRESENCE_DATETIME];
                         }
                        
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			//System.out.println("SQL List : "+sql);
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}


        //update by Kartika 2012-07-02
        public static Vector listAsDateTimeFromToWithStatus(int limitStart,int recordToGet,  String order, long oidDepartement, long employeeId, Date fromDate, Date toDate , long oidSection, Vector status){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			//String sql = "SELECT * FROM " + TBL_HR_MACHINE_TRANS; 
                        
                        String sql = "SELECT PS.* " +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                       // if(oidDepartement !=0 && employeeName !=null && employeeName !="" && oidSection !=0)
                        // if(oidDepartement !=0 && oidSection !=0)
                        //if(oidDepartement !=0)
                            sql = sql + " AS PS INNER JOIN " +
                                       PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +  
                                           " LEFT JOIN "
                        // if(oidDepartement != 0)
                             + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " +
                                           " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" +
                                           "WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0" ;
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        if(employeeId !=0)
                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=\""+employeeId+"\"";
                         
                         if(fromDate !=null && toDate !=null)
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd HH:mm:ss") +"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd HH:mm:ss") +"\"";
                                     
			if(oidSection !=0)
                            sql = sql + " AND SC. " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                        String whereClause="";
                         if( status!=null && status.size()>0 ){
                            Vector vSts = status;
                            whereClause = whereClause + "  ( ";
                            for(int idx =0; idx < vSts.size() ; idx ++){
                                    Integer st = (Integer) vSts.get(idx);
                                    whereClause = whereClause + " ( "+ PstPresence.fieldNames[PstPresence.FLD_STATUS]+
                                            "="+ st.toString()+ " ) OR";
                            }
                            whereClause = whereClause + " (1=0)) AND ";
                        if (whereClause != null && whereClause.length() > 0) {
                            whereClause = whereClause + "1 = 1";
                            sql = sql + " AND " + whereClause;
                        }
			if(order != null && order.length() > 0){
				sql = sql + " ORDER BY " + order;
                        }else{
				sql = sql + " ORDER BY " + fieldNames[FLD_PRESENCE_DATETIME];
                         }
                         
                       
                        }
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
                       
			//System.out.println("SQL List : "+sql);
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}
        
        //update by satrya 2012-11-23
        public static Vector listAsDateTimeFromTo(int limitStart,int recordToGet,  String order, long oidDepartement, long employeeId, Date fromDate, Date toDate , long oidSection){
            return listAsDateTimeFromToWithStatus(limitStart,recordToGet,  order, oidDepartement, employeeId, fromDate, toDate , oidSection, null);  
        }

        //create by Kartika 2012-10-22
        /**
         * 
         * @param limitStart
         * @param recordToGet
         * @param order
         * @param oidDepartement
         * @param employeeId
         * @param fromDate
         * @param toDate
         * @param oidSection
         * @param addWhere : where tambahan contoh :   OR (ssssssss )  atau bisa juga AND (xxxxxxxxxxxxxxxxx)  => sql = ( sql ) + addWhere
         * @return 
         */
        public static Vector listAsDateTimeFromTo(int limitStart,int recordToGet,  String order, long oidDepartement, long employeeId, Date fromDate, Date toDate , long oidSection, String addWhere){
		Vector lists = new Vector(); 
		DBResultSet dbrs = null;
		try {
			//String sql = "SELECT * FROM " + TBL_HR_MACHINE_TRANS; 
                        
                        String sql = "SELECT PS.* " +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                       // if(oidDepartement !=0 && employeeName !=null && employeeName !="" && oidSection !=0)
                        // if(oidDepartement !=0 && oidSection !=0)
                        //if(oidDepartement !=0)
                            sql = sql + " AS PS INNER JOIN " +
                                       PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +  
                                           " LEFT JOIN "
                        // if(oidDepartement != 0)
                             + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " +
                                           " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" +
                                           "WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0" ;
                        if(oidDepartement !=0)
                                sql = sql + " AND EMP." +PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=\""+oidDepartement+"\"";
                        if(employeeId !=0)
                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=\""+employeeId+"\"";
                         
                         if(fromDate !=null && toDate !=null)
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(fromDate,"yyyy-MM-dd HH:mm:ss") +"\"" + " AND " + "\"" + Formater.formatDate(toDate, " yyyy-MM-dd HH:mm:ss") +"\"";
                                     
			if(oidSection !=0)
                            sql = sql + " AND SC. " + PstSection.fieldNames[PstSection.FLD_SECTION_ID] + "=\""+oidSection+"\"";
                        
                        if(addWhere!=null && addWhere.length()>0){
                            sql = sql + " "+addWhere;
                        }
                        
			if(order != null && order.length() > 0){
				sql = sql + " ORDER BY " + order;
                        }else{
				sql = sql + " ORDER BY " + fieldNames[FLD_PRESENCE_DATETIME];
                         }
                        
                                                
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			//System.out.println("SQL List : "+sql);
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}
        
        /**
         * untuk mencari list presence date from overtime
         * kasusnya ada yg overtime 19-11-2013 / 20:00 	20-11-2013 / 04:00 sedangkan attendancenya dari jam 19/11/13 06:58 and 20/11/13 04:58
         * @param limitStart
         * @param recordToGet
         * @param order
         * @param employeeId
         * @param fromDate
         * @param addWhere
         * @return 
         */
        public static Vector listAsDateTimeFromToVer2(int limitStart,int recordToGet,  String order,long employeeId, Date date, String addWhere){
		Vector lists = new Vector(); 
                if(date==null){
                    return null;
                }
		DBResultSet dbrs = null;
		try {
			//String sql = "SELECT * FROM " + TBL_HR_MACHINE_TRANS; 
                        
                        String sql = "SELECT PS.* " +
                                    " FROM " +
                                        PstPresence.TBL_HR_PRESENCE ;
                       // if(oidDepartement !=0 && employeeName !=null && employeeName !="" && oidSection !=0)
                        // if(oidDepartement !=0 && oidSection !=0)
                        //if(oidDepartement !=0)
                            sql = sql + " AS PS INNER JOIN " +
                                       PstEmployee.TBL_HR_EMPLOYEE + " AS EMP "+ " ON EMP. "
                                    + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + " = PS."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] +  
                                           " LEFT JOIN "
                        // if(oidDepartement != 0)
                             + PstDepartment.TBL_HR_DEPARTMENT + " AS DPT " +
                                           " ON (EMP. " + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + "=DPT."+PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + ")" +
                                           "WHERE PS." +PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "!=0" ;
                        if(employeeId !=0)
                          sql = sql + " AND EMP."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID] + "=\""+employeeId+"\"";
                         
                         if(date !=null)
                             sql = sql + " AND PS." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + " BETWEEN "+ "\""+Formater.formatDate(date,"yyyy-MM-dd 00:00:01") +"\"" + " AND " + "\"" + Formater.formatDate(date, " yyyy-MM-dd 23:59:59") +"\"";
                                     
			
                        if(addWhere!=null && addWhere.length()>0){
                            sql = sql + " "+addWhere;
                        }
                        
			if(order != null && order.length() > 0){
				sql = sql + " ORDER BY " + order;
                        }else{
				sql = sql + " ORDER BY " + fieldNames[FLD_PRESENCE_DATETIME];
                         }
                        
                                                
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			//System.out.println("SQL List : "+sql);
                        dbrs = DBHandler.execQueryResult(sql);
                        
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				Presence presence = new Presence();
				resultToObject(rs, presence);
				lists.add(presence);
			}
			rs.close();
			return lists;

		}catch(Exception e) {
			System.out.println(e);
		}finally {
			DBResultSet.close(dbrs);
		}
			return new Vector();
	}
        

	private static void resultToObject(ResultSet rs, Presence presence){
		try{
			presence.setOID(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]));
			presence.setEmployeeId(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]));
			//presence.setPresenceDatetime(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
                        Date tm = DBHandler.convertDate(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]),rs.getTime(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));

                        presence.setPresenceDatetime(tm); //rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
			presence.setStatus(rs.getInt(PstPresence.fieldNames[PstPresence.FLD_STATUS]));
			presence.setAnalyzed(rs.getInt(PstPresence.fieldNames[PstPresence.FLD_ANALYZED]));
                        presence.setTransferred(rs.getInt(PstPresence.fieldNames[PstPresence.FLD_TRANSFERRED]));
                        //update by satrya 2012-08-19
                         Date tmScheduleDateTime = DBHandler.convertDate(rs.getDate(PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME]),rs.getTime(PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME]));
                         presence.setScheduleDatetime(tmScheduleDateTime); //rs.getDate(PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]));
			 presence.setScheduleType(rs.getInt(PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_TYPE]));
                         presence.setScheduleLeaveId(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_OID_SCHEDULE_LEAVE]));
                        presence.setPeriodId(rs.getLong(PstPresence.fieldNames[PstPresence.FLD_PERIOD_ID]));
		}catch(Exception e){ 
                    System.out.println("resultToObject exception  : "+e.toString());
                }
	}

	public static boolean checkOID(long presenceId){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_PRESENCE + " WHERE " + 
						PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] + " = " + presenceId;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);
			return result;
		}
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] + ") FROM " + TBL_HR_PRESENCE;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			int count = 0;
			while(rs.next()) { count = rs.getInt(1); }

			rs.close();
			return count;
		}catch(Exception e) {
			return 0;
		}finally {
			DBResultSet.close(dbrs);
		}
	}


	/* This method used to find current data */
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String orderClause){
		String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, orderClause); 
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){ 
			  	   Presence presence = (Presence)list.get(ls);
				   if(oid == presence.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}


    public static long deleteByEmployee(long emplOID)
    {
    	try{
            String sql = " DELETE FROM "+PstPresence.TBL_HR_PRESENCE +
                 " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] +
                 " = '" + emplOID +"'";
            int status = DBHandler.execUpdate(sql);
    	}
        catch(Exception exc){
        	System.out.println("error delete day off presence by employee "+exc.toString());
    	}
    	return emplOID;  
    }
    
    
    /**
     * proses import presence
     * proses ini dilakukan jika terlambat insert schedule ke HARISMA
     * atau update schedule untuk masing-masing employee
     * update in schedule                                   
     * @param empSchedule
     * created by edhy
     */    
    public static void importPresenceTriggerByEmpSchedule(EmpSchedule empSchedule)
    {       
        
        // cari vector of presence untuk "employee ini" selama durasi waktu schedule period ini
        com.dimata.harisma.entity.masterdata.Period schedulePeriod = new com.dimata.harisma.entity.masterdata.Period();
        try
        {
                schedulePeriod = com.dimata.harisma.entity.masterdata.PstPeriod.fetchExc(empSchedule.getPeriodId());
        }
        catch(Exception e)
        {
                System.out.println("Exc when fetch period on PstPresence : " + e.toString());
        }

        String whClause = PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + 
                                          " = " + empSchedule.getEmployeeId() +  
                                          " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + 
                                          " BETWEEN \"" + com.dimata.util.Formater.formatDate(schedulePeriod.getStartDate(), "yyyy-MM-dd") + 
                                          "\" AND \"" + com.dimata.util.Formater.formatDate(schedulePeriod.getEndDate(), "yyyy-MM-dd") + "\"";   
        
        String ordBy    = PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];                  
        Vector vectEmpPresence = PstPresence.list(0, 0, whClause, ordBy);

        long periodId = schedulePeriod.getOID();
        if(vectEmpPresence!=null && vectEmpPresence.size()>0)
        {
                int maxPresence = vectEmpPresence.size();
                for(int i=0; i<maxPresence; i++)
                {
                        Presence presence = (Presence) vectEmpPresence.get(i);                                       

                        int updatedFieldIndex = -1;
                        long updatePeriodId = periodId;
                        Vector vectFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(periodId, presence.getEmployeeId(), presence.getStatus(), presence.getPresenceDatetime());
                        if(vectFieldIndex!=null && vectFieldIndex.size()==2) 
                        {
                                updatePeriodId = Long.parseLong(String.valueOf(vectFieldIndex.get(0)));
                                updatedFieldIndex = Integer.parseInt(String.valueOf(vectFieldIndex.get(1)));
                        }

                        int updateStatus = 0;
                        try 
                        {
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(updatePeriodId, presence.getEmployeeId(), updatedFieldIndex, presence.getPresenceDatetime());
                                if(updateStatus>0) 
                                {
                                        presence.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                }
                        }
                        catch(Exception e) 
                        {
                                System.out.println("Update Presence exc : "+e.toString());
                        }
                        
                        // process on absence and lateness
                        // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence
                        com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presence.getPresenceDatetime(), presence.getEmployeeId());
                        com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presence.getPresenceDatetime(), presence.getEmployeeId()); 
                        
                        try
                        {
                            Thread.sleep(100);
                        }
                        catch(Exception e)
                        {
                            System.out.println("Exc thread : " + e.toString());
                        }
                }  
        }					
        
    }

    /**
     * proses import presence
     * proses ini dilakukan jika terlambat insert schedule ke HARISMA
     * atau update schedule untuk masing-masing employee
     * update in schedule                                   
     * @param empSchedule
     * created by edhy   
     */    
    public static void importPresenceTriggerByEmpSchedule(EmpSchedule orgEmpSchedule, EmpSchedule empSchedule)
    {       
        // simpan schedule original I ke array of data long untuk nantinya dibandingkan
        long[] arrOriginalScheduleFirst = {
          orgEmpSchedule.getD1(),  
          orgEmpSchedule.getD2(),
          orgEmpSchedule.getD3(),
          orgEmpSchedule.getD4(),
          orgEmpSchedule.getD5(),
          orgEmpSchedule.getD6(),
          orgEmpSchedule.getD7(),
          orgEmpSchedule.getD8(),
          orgEmpSchedule.getD9(),
          orgEmpSchedule.getD10(),
          orgEmpSchedule.getD11(),
          orgEmpSchedule.getD12(),
          orgEmpSchedule.getD13(),
          orgEmpSchedule.getD14(),
          orgEmpSchedule.getD15(),
          orgEmpSchedule.getD16(),
          orgEmpSchedule.getD17(),
          orgEmpSchedule.getD18(),
          orgEmpSchedule.getD19(),
          orgEmpSchedule.getD20(),
          orgEmpSchedule.getD21(),
          orgEmpSchedule.getD22(),
          orgEmpSchedule.getD23(),
          orgEmpSchedule.getD24(),
          orgEmpSchedule.getD25(),
          orgEmpSchedule.getD26(),
          orgEmpSchedule.getD27(),
          orgEmpSchedule.getD28(),
          orgEmpSchedule.getD29(),
          orgEmpSchedule.getD30(),
          orgEmpSchedule.getD31()
        };
        

        // simpan schedule original II ke array of data long untuk nantinya dibandingkan
        long[] arrOriginalScheduleSecond = {
          orgEmpSchedule.getD2nd1(),
          orgEmpSchedule.getD2nd2(),
          orgEmpSchedule.getD2nd3(),
          orgEmpSchedule.getD2nd4(),
          orgEmpSchedule.getD2nd5(),
          orgEmpSchedule.getD2nd6(),
          orgEmpSchedule.getD2nd7(),
          orgEmpSchedule.getD2nd8(),
          orgEmpSchedule.getD2nd9(),
          orgEmpSchedule.getD2nd10(),
          orgEmpSchedule.getD2nd11(),
          orgEmpSchedule.getD2nd12(),
          orgEmpSchedule.getD2nd13(),
          orgEmpSchedule.getD2nd14(),
          orgEmpSchedule.getD2nd15(),
          orgEmpSchedule.getD2nd16(),
          orgEmpSchedule.getD2nd17(),
          orgEmpSchedule.getD2nd18(),
          orgEmpSchedule.getD2nd19(),
          orgEmpSchedule.getD2nd20(),
          orgEmpSchedule.getD2nd21(),
          orgEmpSchedule.getD2nd22(),
          orgEmpSchedule.getD2nd23(),
          orgEmpSchedule.getD2nd24(),
          orgEmpSchedule.getD2nd25(),
          orgEmpSchedule.getD2nd26(),
          orgEmpSchedule.getD2nd27(),
          orgEmpSchedule.getD2nd28(),
          orgEmpSchedule.getD2nd29(),
          orgEmpSchedule.getD2nd30(),
          orgEmpSchedule.getD2nd31()
        };
        
        // simpan schedule terupdate I ke array of data long untuk nantinya dibandingkan
        long[] arrUpdatedScheduleFirst = {
          empSchedule.getD1(),  
          empSchedule.getD2(),
          empSchedule.getD3(),
          empSchedule.getD4(),
          empSchedule.getD5(),
          empSchedule.getD6(),
          empSchedule.getD7(),
          empSchedule.getD8(),
          empSchedule.getD9(),
          empSchedule.getD10(),
          empSchedule.getD11(),
          empSchedule.getD12(),
          empSchedule.getD13(),
          empSchedule.getD14(),
          empSchedule.getD15(),
          empSchedule.getD16(),
          empSchedule.getD17(),
          empSchedule.getD18(),
          empSchedule.getD19(),
          empSchedule.getD20(),
          empSchedule.getD21(),
          empSchedule.getD22(),
          empSchedule.getD23(),
          empSchedule.getD24(),
          empSchedule.getD25(),
          empSchedule.getD26(),
          empSchedule.getD27(),
          empSchedule.getD28(),
          empSchedule.getD29(),
          empSchedule.getD30(),
          empSchedule.getD31()
        };


        // simpan schedule terupdate II ke array of data long untuk nantinya dibandingkan
        long[] arrUpdatedScheduleSecond = {
          empSchedule.getD2nd1(),
          empSchedule.getD2nd2(),
          empSchedule.getD2nd3(),
          empSchedule.getD2nd4(),
          empSchedule.getD2nd5(),
          empSchedule.getD2nd6(),
          empSchedule.getD2nd7(),
          empSchedule.getD2nd8(),
          empSchedule.getD2nd9(),
          empSchedule.getD2nd10(),
          empSchedule.getD2nd11(),
          empSchedule.getD2nd12(),
          empSchedule.getD2nd13(),
          empSchedule.getD2nd14(),
          empSchedule.getD2nd15(),
          empSchedule.getD2nd16(),
          empSchedule.getD2nd17(),
          empSchedule.getD2nd18(),
          empSchedule.getD2nd19(),
          empSchedule.getD2nd20(),
          empSchedule.getD2nd21(),
          empSchedule.getD2nd22(),
          empSchedule.getD2nd23(),
          empSchedule.getD2nd24(),
          empSchedule.getD2nd25(),
          empSchedule.getD2nd26(),
          empSchedule.getD2nd27(),
          empSchedule.getD2nd28(),
          empSchedule.getD2nd29(),
          empSchedule.getD2nd30(),
          empSchedule.getD2nd31()
        };

        
        // mengambil tanggal presence sesuaikan dengan period schedule
        Date dtNow = new Date();
        Period objPeriod = new Period();
        try
        {
            objPeriod = PstPeriod.fetchExc(empSchedule.getPeriodId());
            dtNow = objPeriod.getStartDate();
        }catch(Exception e){
            System.out.println("Exc when fetch period : " + e.toString());
        }        
        
        // proses import regular schedule (schedule I)         
        //int arrLengthFirst = arrOriginalScheduleFirst.length;

        int diff= DATEDIFF(objPeriod.getEndDate(),objPeriod.getStartDate());

        int iSch =  objPeriod.getStartDate().getDate();

        Date dtProces = objPeriod.getStartDate();        

        int countMax = 0;

        while(countMax < diff){
            
            Date nextProcess = (Date)dtProces.clone(); nextProcess.setDate(nextProcess.getDate()+1); 

            if(arrOriginalScheduleFirst[iSch-1] != arrUpdatedScheduleFirst[iSch-1]){

                importPresenceToScheduleBaseOnDate(dtProces, nextProcess, empSchedule,objPeriod.getOID());
                
            }

            dtProces = nextProcess;
            iSch = dtProces.getDate();
            countMax++;
        }

        /*
        for(int iFirst=0; iFirst<arrLengthFirst; iFirst++)
        {


            if(arrOriginalScheduleFirst[iFirst] != arrUpdatedScheduleFirst[iFirst])
            {                
                // Men-generate dateFrom dan dateTo untuk parameter pencarian datetime schedule                
                Date dateFrom = new Date(dtNow.getYear(), dtNow.getMonth(), iFirst+1);
                
                // Date dateTo = new Date(dtNow.getYear(), dtNow.getMonth(), iFirst+1);
                Date dateTo = new Date(dtNow.getYear(), dtNow.getMonth(), iFirst+2);

                // Proses import presence to schedule
                importPresenceToScheduleBaseOnDate(dateFrom, dateTo, empSchedule);
            }            
        }
        */
        // proses import split schedule (schedule II)


        int diff2= DATEDIFF(objPeriod.getEndDate(),objPeriod.getStartDate());

        int iSch2 =  objPeriod.getStartDate().getDate();

        Date dtProces2 = objPeriod.getStartDate();

        int countMax2 = 0;

        while(countMax2 < diff2){

            Date nextProcess2 = (Date)dtProces2.clone();
            nextProcess2.setDate(nextProcess2.getDate()+1);

            if(arrOriginalScheduleSecond[iSch2-1] != arrUpdatedScheduleSecond[iSch2-1]){

                importPresenceToScheduleBaseOnDate(dtProces2, nextProcess2, empSchedule,objPeriod.getOID());

            }

            dtProces2 = nextProcess2;
            iSch2 = dtProces2.getDate();
            countMax2++;
        }


        /*

        int arrLengthSecond = arrOriginalScheduleFirst.length;

        for(int iSecond=0; iSecond<arrLengthSecond; iSecond++)
        {
            if(arrOriginalScheduleSecond[iSecond] != arrUpdatedScheduleSecond[iSecond])
            {               
                
                // men-generate dateFrom dan dateTo untuk parameter pencarian datetime schedule                
                //Date dateFrom = new Date(dtNow.getYear(), dtNow.getMonth(), iSecond+1);
                Date dateFrom = new Date(dtNow.getYear(), dtNow.getMonth(), iSecond+1);
                
                // Date dateTo = new Date(dtNow.getYear(), dtNow.getMonth(), iSecond+1);
                Date dateTo = new Date(dtNow.getYear(), dtNow.getMonth(), iSecond+2);

                // proses import presence to schedule
                //importPresenceToScheduleBaseOnDate(dateFrom, dateTo, empSchedule);
                importPresenceToScheduleBaseOnDate(dateFrom, dateTo, empSchedule);
                
            }            
        }
        */


    }
    

    public static int DATEDIFF(Date start,Date end){
        
        DBResultSet dbrs = null;
        
        try{
            String sql = "SELECT DATEDIFF('"+Formater.formatDate(start,"yyyy-MM-dd")+"','"+Formater.formatDate(end,"yyyy-MM-dd")+"')";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();

            while(rs.next()){
                int count = 0;
                count = rs.getInt(1);
                return count;
            }
            
        }catch(Exception E){
            System.out.println("Exception "+E.toString());
        }finally{
            DBResultSet.close(dbrs);
        }

        return 0;
        
    }


    /**
     * proses import presence
     * proses ini dilakukan jika terlambat insert schedule ke HARISMA
     * atau update schedule untuk masing-masing employee
     * update in schedule                                   
     * @param empSchedule
     * created by edhy   
     */    
    public static void importPresenceTriggerByImportEmpScheduleExcel(EmpSchedule orgEmpSchedule, EmpSchedule empSchedule)
    {       
        /* Simpan schedule original ke array of data long untuk nantinya dibandingkan */
        
        long[] arrOriginalSchedule = {
          orgEmpSchedule.getD1(),  
          orgEmpSchedule.getD2(),
          orgEmpSchedule.getD3(),
          orgEmpSchedule.getD4(),
          orgEmpSchedule.getD5(),
          orgEmpSchedule.getD6(),
          orgEmpSchedule.getD7(),
          orgEmpSchedule.getD8(),
          orgEmpSchedule.getD9(),
          orgEmpSchedule.getD10(),
          orgEmpSchedule.getD11(),
          orgEmpSchedule.getD12(),
          orgEmpSchedule.getD13(),
          orgEmpSchedule.getD14(),
          orgEmpSchedule.getD15(),
          orgEmpSchedule.getD16(),
          orgEmpSchedule.getD17(),
          orgEmpSchedule.getD18(),
          orgEmpSchedule.getD19(),
          orgEmpSchedule.getD20(),
          orgEmpSchedule.getD21(),
          orgEmpSchedule.getD22(),
          orgEmpSchedule.getD23(),
          orgEmpSchedule.getD24(),
          orgEmpSchedule.getD25(),
          orgEmpSchedule.getD26(),
          orgEmpSchedule.getD27(),
          orgEmpSchedule.getD28(),
          
          orgEmpSchedule.getD29(),
          orgEmpSchedule.getD30(),
          orgEmpSchedule.getD31(),
          
          orgEmpSchedule.getD2nd1(),
          orgEmpSchedule.getD2nd2(),
          orgEmpSchedule.getD2nd3(),
          orgEmpSchedule.getD2nd4(),
          orgEmpSchedule.getD2nd5(),
          orgEmpSchedule.getD2nd6(),
          orgEmpSchedule.getD2nd7(),
          orgEmpSchedule.getD2nd8(),
          orgEmpSchedule.getD2nd9(),
          orgEmpSchedule.getD2nd10(),
          orgEmpSchedule.getD2nd11(),
          orgEmpSchedule.getD2nd12(),
          orgEmpSchedule.getD2nd13(),
          orgEmpSchedule.getD2nd14(),
          orgEmpSchedule.getD2nd15(),
          orgEmpSchedule.getD2nd16(),
          orgEmpSchedule.getD2nd17(),
          orgEmpSchedule.getD2nd18(),
          orgEmpSchedule.getD2nd19(),
          orgEmpSchedule.getD2nd20(),
          orgEmpSchedule.getD2nd21(),
          orgEmpSchedule.getD2nd22(),
          orgEmpSchedule.getD2nd23(),
          orgEmpSchedule.getD2nd24(),
          orgEmpSchedule.getD2nd25(),
          orgEmpSchedule.getD2nd26(),
          orgEmpSchedule.getD2nd27(),
          orgEmpSchedule.getD2nd28(),
          orgEmpSchedule.getD2nd29(),
          orgEmpSchedule.getD2nd30(),
          orgEmpSchedule.getD2nd31()          
        };
        

        // simpan schedule terupdate (current) ke array of data long untuk nantinya dibandingkan
        long[] arrUpdatedSchedule = {
          empSchedule.getD1(),  
          empSchedule.getD2(),
          empSchedule.getD3(),
          empSchedule.getD4(),
          empSchedule.getD5(),
          empSchedule.getD6(),
          empSchedule.getD7(),
          empSchedule.getD8(),
          empSchedule.getD9(),
          empSchedule.getD10(),
          empSchedule.getD11(),
          empSchedule.getD12(),
          empSchedule.getD13(),
          empSchedule.getD14(),
          empSchedule.getD15(),
          empSchedule.getD16(),
          empSchedule.getD17(),
          empSchedule.getD18(),
          empSchedule.getD19(),
          empSchedule.getD20(),
          empSchedule.getD21(),
          empSchedule.getD22(),
          empSchedule.getD23(),
          empSchedule.getD24(),
          empSchedule.getD25(),
          empSchedule.getD26(),
          empSchedule.getD27(),
          empSchedule.getD28(),
          empSchedule.getD29(),
          empSchedule.getD30(),
          empSchedule.getD31(),
          empSchedule.getD2nd1(),
          empSchedule.getD2nd2(),
          empSchedule.getD2nd3(),
          empSchedule.getD2nd4(),
          empSchedule.getD2nd5(),
          empSchedule.getD2nd6(),
          empSchedule.getD2nd7(),
          empSchedule.getD2nd8(),
          empSchedule.getD2nd9(),
          empSchedule.getD2nd10(),
          empSchedule.getD2nd11(),
          empSchedule.getD2nd12(),
          empSchedule.getD2nd13(),
          empSchedule.getD2nd14(),
          empSchedule.getD2nd15(),
          empSchedule.getD2nd16(),
          empSchedule.getD2nd17(),
          empSchedule.getD2nd18(),
          empSchedule.getD2nd19(),
          empSchedule.getD2nd20(),
          empSchedule.getD2nd21(),
          empSchedule.getD2nd22(),
          empSchedule.getD2nd23(),
          empSchedule.getD2nd24(),
          empSchedule.getD2nd25(),
          empSchedule.getD2nd26(),
          empSchedule.getD2nd27(),
          empSchedule.getD2nd28(),
          empSchedule.getD2nd29(),
          empSchedule.getD2nd30(),
          empSchedule.getD2nd31()          
        };
        
        // proses import regular schedule (schedule I) 
        if(orgEmpSchedule.getOID()!=0 && empSchedule.getOID()!=0)
        {
            
            int arrLength = arrOriginalSchedule.length;        
            
            for(int i=0; i<arrLength; i++)
            {
                if(arrOriginalSchedule[i] != arrUpdatedSchedule[i])
                {                                
                    // proses insert empSchedule History 
                    // System.out.println("--- proses insert empSchedule History ...");
                    PstEmpScheduleHistory.storeEmpScheduleTemporary(orgEmpSchedule, empSchedule.getOID());
                    break;
                }            
            }
            
        }
    }

    
    /** 
     * proses import presence per employee per hari sesuai dengan schedulenya     
     * @param scheduleDate
     * @param empSchedule 
     * @created by edhy
     */
    public static void importPresenceToScheduleBaseOnDate(Date scheduleDateFrom, Date scheduleDateTo, EmpSchedule empSchedule){
       importPresenceToScheduleBaseOnDate( scheduleDateFrom,  scheduleDateTo,  empSchedule,0L);
    }
    public static void importPresenceToScheduleBaseOnDate(Date scheduleDateFrom, Date scheduleDateTo, EmpSchedule empSchedule, long datePeriodID)
    {               
        // generate parameter dateFrom dan dateTo untuk pencarian data Presence
        String strDtFrom = "\"" + com.dimata.util.Formater.formatDate(scheduleDateFrom, "yyyy-MM-dd") + " 00:00:00\"";
        String strDtTo = "\"" + com.dimata.util.Formater.formatDate(scheduleDateTo, "yyyy-MM-dd") + " 23:59:59\"";
        
        // cari vector of presence untuk "employee ini" selama durasi "scheduleDate" pilihan ini
        String whClause = PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + 
                          " = " + empSchedule.getEmployeeId() +  
                          " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + 
                          " BETWEEN " + strDtFrom + 
                          " AND " + strDtTo;   
        
        String ordBy  = PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];                  
        Vector vectEmpPresence = PstPresence.list(0, 0, whClause, ordBy);

        if(vectEmpPresence!=null && vectEmpPresence.size()>0)
        {
                int maxPresence = vectEmpPresence.size();       
                for(int i=0; i<maxPresence; i++)
                {
                        Presence presence = (Presence) vectEmpPresence.get(i);                         

                        int updatedFieldIndex = -1;
                        
                        long periodId = datePeriodID!=0? datePeriodID: PstPeriod.getPeriodIdBySelectedDate(presence.getPresenceDatetime());
                        
                        if(periodId==0)
                        {
                            periodId = empSchedule.getPeriodId();
                        }
                        
                        long updatePeriodId = periodId;                         
                        
                        // status absence and lateness 
                        int intFirstAbsenceStatus = PstEmpSchedule.STATUS_PRESENCE_OK;;
                        int intSecondAbsenceStatus = PstEmpSchedule.STATUS_PRESENCE_OK;
                        int intFirstLatenessStatus = PstEmpSchedule.STATUS_PRESENCE_OK;
                        int intSecondLatenessStatus = PstEmpSchedule.STATUS_PRESENCE_OK;                        
                        
                        Vector vectFieldIndex = PstEmpSchedule.getIndexEmpScheduleTableWillUpdated(periodId, presence.getEmployeeId(), presence.getStatus(), presence.getPresenceDatetime());                        
                        
                        if(vectFieldIndex!=null && vectFieldIndex.size()==6)   
                        {
                                updatePeriodId = Long.parseLong(String.valueOf(vectFieldIndex.get(0)));
                                updatedFieldIndex = Integer.parseInt(String.valueOf(vectFieldIndex.get(1)));                                
                                intFirstAbsenceStatus = Integer.parseInt(String.valueOf(vectFieldIndex.get(2))); 
                                intSecondAbsenceStatus = Integer.parseInt(String.valueOf(vectFieldIndex.get(3))); 
                                intFirstLatenessStatus = Integer.parseInt(String.valueOf(vectFieldIndex.get(4))); 
                                intSecondLatenessStatus = Integer.parseInt(String.valueOf(vectFieldIndex.get(5)));                                 
                        }
                        
                        int updateStatus = 0;  
                        try    
                        {                                
                                updateStatus = PstEmpSchedule.updateEmpSchdlWithPrscAbsLate(updatePeriodId, presence.getEmployeeId(), updatedFieldIndex, presence.getPresenceDatetime(),
                                               intFirstAbsenceStatus, intSecondAbsenceStatus, intFirstLatenessStatus, intSecondLatenessStatus);                                
                                if(updateStatus>0) 
                                {
                                        presence.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                }
                        }
                        catch(Exception e) 
                        {
                                System.out.println("Update Presence exc : "+e.toString());
                        }
                        
                        try
                        {
                            Thread.sleep(10);    
                        }
                        catch(Exception e)
                        {
                            System.out.println("Exc thread : " + e.toString());
                        }
                }                  
        }
        
        
        else
        {
            int intIdxStatus = scheduleDateFrom.getDate();                        
            long lPeriodId = empSchedule.getPeriodId();
            long lEmployeeId = empSchedule.getEmployeeId();
            int intStatusPresenceOk = PstEmpSchedule.STATUS_PRESENCE_OK;                        
            
            try    
            {                         
                int updateStatus = PstEmpSchedule.updateEmpSchdlWithPrscAbsLate(lPeriodId, lEmployeeId, intIdxStatus, null, intStatusPresenceOk, intStatusPresenceOk, intStatusPresenceOk, intStatusPresenceOk);                                                
            }
            catch(Exception e) 
            {
                System.out.println("Update Presence exc : "+e.toString());
            }            
        }
    }    
    
    
    /**
     * @param objPresence
     * @return
     */    
    public static boolean presenceExist(Presence objPresence)
    {
        boolean result = false; 
        DBResultSet dbrs = null;
        try 
        {
                String sql = "SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] +
                             " FROM " + TBL_HR_PRESENCE + 
                             " WHERE " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + 
                             " = " + objPresence.getEmployeeId() + 
                             " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + 
                             " = \"" + com.dimata.util.Formater.formatDate(objPresence.getPresenceDatetime(),"yyyy-MM-dd HH:mm:ss") + "\"" + 
                             " AND " + PstPresence.fieldNames[PstPresence.FLD_STATUS] + 
                             " = " + objPresence.getStatus(); 
                //System.out.println("presenceExist sql : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();                        
                while(rs.next()) 
                {             
                    result = true;
                    break;
                }                        
                rs.close();                                        
        }
        catch(Exception e) 
        {
            System.out.println(e);
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }
        return result;
    }
    

    public static int synchDataPresence()
    {
        int iResult = 0;
        
        DBResultSet dbrs = null;
        try 
        {
                String sql = "SELECT " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID] +
                             ", " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] +
                             ", " + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] +
                             ", " + PstPresence.fieldNames[PstPresence.FLD_STATUS] +
                             ", " + PstPresence.fieldNames[PstPresence.FLD_ANALYZED] +
                             ", " + PstPresence.fieldNames[PstPresence.FLD_TRANSFERRED] +
                             " FROM " + TBL_HR_PRESENCE + 
                             " WHERE " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + 
                             " < '2004-01-01 00:00:00'";
                System.out.println("synchDataPresence sql : " + sql);
                dbrs = DBHandler.execQueryResult(sql);
                ResultSet rs = dbrs.getResultSet();                        
                while(rs.next()) 
                {             
                    long iPresenceOid = rs.getLong(1);                    
                    String str = rs.getString(2);
                    long lEmployeeOid = rs.getLong(3);                    
                    int iStatus = rs.getInt(4);                    
                    int iAnalyzed = rs.getInt(5);                    
                    int iTransferred = rs.getInt(6);                    
                    
                    //System.out.println("-- OID : " + iPresenceOid + ", Date : " + str + ", year : " + str.substring(0,4) + ", month : " + str.substring(5,7) + ", day : " + str.substring(8,10) + ", hour : " + str.substring(11,13) + ", minutes : " + str.substring(14,16));                    
                    int iYear = Integer.parseInt(str.substring(0,4));
                    int iMonth = Integer.parseInt(str.substring(5,7));
                    int iDate = Integer.parseInt(str.substring(8,10));
                    int iHour = Integer.parseInt(str.substring(11,13));
                    int iMinutes = Integer.parseInt(str.substring(14,16));                    
                    Date dNewDate = new Date(iYear-1900+5, iMonth-1, iDate, iHour, iMinutes, 0);                    
                    
                    Presence objPresence = new Presence();
                    objPresence.setOID(iPresenceOid);
                    objPresence.setEmployeeId(lEmployeeOid);
                    objPresence.setPresenceDatetime(dNewDate);                    
                    objPresence.setStatus(iStatus);
                    objPresence.setAnalyzed(iAnalyzed);
                    objPresence.setTransferred(iTransferred);
                    
                    try
                    {
                        long lUpdated = PstPresence.updateExc(objPresence); 
                        System.out.println("-- updated OID : " + iPresenceOid + ", Date : " + dNewDate);                    
                    }
                    catch(Exception exc)
                    {
                        System.out.println("Exc : " + exc.toString());
                    }
                    
                }                        
                rs.close();                                        
        }
        catch(Exception e) 
        {
            System.out.println(e);
        }
        finally 
        {
            DBResultSet.close(dbrs);
        }

        return iResult;
    }
    
    
     /*
     *  This method used to update presence
     *  Created By Yunny
     */
    /**
     * Keterangan : untuk update status In dan Out schedulenya
     * @param presenceId
     * @param status 
     */
    public static void updatePresenceStatusInOut(long presenceId, int status) {
        DBResultSet dbrs = null;
        //boolean result = false;
        //String barcode = (barcodeNumber.equals(null)) ? "null" : barcodeNumber;
        //String StrDate = (date.getYear()+1900)+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
        try{
            String sql = "";
            
                sql = " UPDATE " + TBL_HR_PRESENCE + " SET " ;
                   if(status == Presence.STATUS_IN){
                       sql = sql + PstPresence.fieldNames[PstPresence.FLD_STATUS]+" = "+ Presence.STATUS_IN;
                   }
                   else if(status == Presence.STATUS_OUT){
                       sql = sql + PstPresence.fieldNames[PstPresence.FLD_STATUS]+" = "+ Presence.STATUS_OUT;
                   }
                sql = sql + " WHERE "+PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]+" = "+ presenceId;
                          
            //dbrs = DBHandler.execQueryResult(sql);
            status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();
           // System.out.println("sql  "+sql);
            //while(rs.next()) { result = true; }
           
            //rs.close();
        } catch(Exception e) {
            System.err.println("\tupdateCompValue error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }
    
 /**
  * Keterangan : update Analyse status presence
  * create by satrrya 2013-07-25
  * @param employeeId
  * @param dtPresence
  * @param periodId
  * @return int
  */
 public static int updatePresenceStatusAnalyze(long employeeId, String sPresenceId,long periodId) {
       // DBResultSet dbrs = null;
        int status=0;
        try{
            if(sPresenceId!=null && sPresenceId.length()>0){
                String oidSPresence = sPresenceId.substring(0,sPresenceId.length()-1);
                String sql =" UPDATE " + TBL_HR_PRESENCE + " SET " 
                        + PstPresence.fieldNames[PstPresence.FLD_ANALYZED]+" = "+ Presence.ANALYZED_OK +","
                        + PstPresence.fieldNames[PstPresence.FLD_PERIOD_ID]+" = "+ periodId
                        + " WHERE "+PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]+ " IN (" + oidSPresence + ")";
                status = DBHandler.execUpdate(sql);
         
            }
        } catch(Exception e) {
            System.err.println("\tupdateCompValue error : " + e.toString());
        } finally {
         //   DBResultSet.close(dbrs);
            return status;
        }
    }
    /**
     * Keterangan : untuk melakukan update status Presence status antara Personal Out dan Personal IN
     * @param presenceId
     * @param status 
     */
     public static void updatePresenceStatusPersonalOutIn(long presenceId, int status, Date scheduleDateTime,int scheduleType,long oidScheduleLeaveSymbol) {
        DBResultSet dbrs = null;
        //boolean result = false;
        String sScheduleDateTime="null";
        if(scheduleDateTime !=null){
            sScheduleDateTime = Formater.formatDate(scheduleDateTime, "yyyy-MM-dd HH:mm:ss");
        }
        try{
            String sql = "";
            
                sql = " UPDATE " + TBL_HR_PRESENCE + " SET " 
                      +  PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME]+" = "+ "\""+sScheduleDateTime+"\""
                      + "," +PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_TYPE]+" = "+ scheduleType
                      + "," +PstPresence.fieldNames[PstPresence.FLD_OID_SCHEDULE_LEAVE]+" = "+ oidScheduleLeaveSymbol;
                   if(status == Presence.STATUS_OUT_PERSONAL){
                       sql = sql + " , " +PstPresence.fieldNames[PstPresence.FLD_STATUS]+" = "+ Presence.STATUS_OUT_PERSONAL;
                   }
                   else if(status == Presence.STATUS_IN_PERSONAL){
                       sql = sql +" , " +PstPresence.fieldNames[PstPresence.FLD_STATUS]+" = "+ Presence.STATUS_IN_PERSONAL;
                   }
                    sql = sql + " WHERE "+PstPresence.fieldNames[PstPresence.FLD_PRESENCE_ID]+" = "+ presenceId;
                          
            //dbrs = DBHandler.execQueryResult(sql);
            status = DBHandler.execUpdate(sql);
            //ResultSet rs = dbrs.getResultSet();
            System.out.println("sql  "+sql);
            //while(rs.next()) { result = true; }
           
            //rs.close();
        } catch(Exception e) {
            System.err.println("\tupdateCompValue error : " + e.toString());
        } finally {
            DBResultSet.close(dbrs);
            //return result;
        }
    }
    /**
     * Keterangan : untuk mereset data presence dengan mengeset semua status presence menjadi TBD
     * @param sPayrollNum
     * @param sFullName
     * @param SelectedDate
     * @return 
     * Create by : satrya 2012-09-20
     */
    public static int resetPresenceData(String sPayrollNum,String sFullName, Date SelectedDate) {
//update by satrya 2012-09-09
        int result = 0;
                    DBResultSet dbrs = null;
                    try {
                        String sql = "UPDATE " + PstPresence.TBL_HR_PRESENCE + " AS HP " 
                                     + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS HE  "
                                     + " ON (HP." +PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                                     + " = HE."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+")"
                                     + " SET " 
                                     + "HP."+ PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = " + Presence.STATUS_TBD_IN_OUT
                                     //update by satrya 2012-09-25
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME] + " = " + null
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_TYPE] + " = " + Presence.SCH_TYPE_NONE
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_OID_SCHEDULE_LEAVE] + " = " + 0
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_PERIOD_ID] + " = " + 0;
                                    
                          if(SelectedDate !=null){          
                           sql = sql + " WHERE HP." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                                + " BETWEEN "+ "\""+ Formater.formatDate(SelectedDate, "yyyy-MM-dd 00:00:00") +"\""
                                +" AND "+ "\""+ Formater.formatDate(SelectedDate, "yyyy-MM-dd 23:59:59") +"\""
                                ///update by satrya 2012-09-28
                                + " AND NOT (HP."+PstPresence.fieldNames[PstPresence.FLD_STATUS] + " IN ("+Presence.STATUS_OUT_ON_DUTY
                                   + ", "+Presence.STATUS_CALL_BACK +"))";
                          }      
    
                         if(sPayrollNum !=null && sPayrollNum.length()>0){
                              sql = sql  + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]
                                + " = " + "\""+ sPayrollNum +"\"";
                         }
                         if(sFullName !=null && sFullName.length()>0){
                              sql = sql  + " AND HE." + PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]
                                + " = " + "\""+ sFullName +"\"";
                         }
    
                        //System.out.println("updateScheduleDataByPresence : " + sql);
                        result = DBHandler.execUpdate(sql);
                    } catch (Exception e) {
                        System.out.println("Exc updateScheduleDataByPresence : " + e.toString());
                    } finally {
                        DBResultSet.close(dbrs);
                        return result;
                    }
    }
    /**
     * Ketranagn untu reset presence di gunakan di report daily
     * Create by satrya 2013-05-28
     * @param empId
     * @param SelectedDate
     * @return 
     */
    public static int resetPresenceDataVer2(long empId, Date SelectedDate) {
//update by satrya 2012-09-09
        int result = 0;
                    DBResultSet dbrs = null;
                    try {
                        String sql = "UPDATE " + PstPresence.TBL_HR_PRESENCE + " AS HP " 
                                     + " SET " 
                                     + "HP."+ PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = " + Presence.STATUS_TBD_IN_OUT
                                     //update by satrya 2012-09-25
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME] + " = " + null
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_TYPE] + " = " + Presence.SCH_TYPE_NONE
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_OID_SCHEDULE_LEAVE] + " = " + 0
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_PERIOD_ID] + " = " + 0;
                                    
                          if(SelectedDate !=null){          
                           sql = sql + " WHERE HP." + PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                                + " = " + empId +" AND HP." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                                + " BETWEEN "+ "\""+ Formater.formatDate(SelectedDate, "yyyy-MM-dd 00:00:00") +"\""
                                +" AND "+ "\""+ Formater.formatDate(SelectedDate, "yyyy-MM-dd 23:59:59") +"\""
                                ///update by satrya 2012-09-28
                                + " AND NOT (HP."+PstPresence.fieldNames[PstPresence.FLD_STATUS] + " IN ("+Presence.STATUS_OUT_ON_DUTY
                                   + ", "+Presence.STATUS_CALL_BACK +"))";
                          }      
    
                        //System.out.println("updateScheduleDataByPresence : " + sql);
                        result = DBHandler.execUpdate(sql);
                    } catch (Exception e) {
                        System.out.println("Exc updateScheduleDataByPresence : " + e.toString());
                    } finally {
                        DBResultSet.close(dbrs);
                        return result;
                    }
    }
    /**
     * Keterangan : Fungsi untuk mereset presence data berdasarkan selected from dan to
     * @param empOid
     * @param selectedDateFrom
     * @param selectedDateTo
     * @return 
     */
     public static int resetPresenceDataBySelectedDateFromTo(long empOid, Date selectedDateFrom, Date selectedDateTo) {
//update by satrya 2012-09-26
        int result = 0;
                    DBResultSet dbrs = null;
              if(selectedDateFrom !=null && selectedDateTo !=null){
                  
                    try {
                        String sql = "UPDATE " + PstPresence.TBL_HR_PRESENCE + " AS HP " 
                                     + " INNER JOIN " + PstEmployee.TBL_HR_EMPLOYEE + " AS HE  "
                                     + " ON (HP." +PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID]
                                     + " = HE."+PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]+")"
                                     + " SET " 
                                     + "HP."+ PstPresence.fieldNames[PstPresence.FLD_STATUS] + " = " + Presence.STATUS_TBD_IN_OUT
                                     //update by satrya 2012-09-25
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_DATETIME] + " = " + null
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_SCHEDULE_TYPE] + " = " + Presence.SCH_TYPE_NONE
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_OID_SCHEDULE_LEAVE] + " = " + 0
                                     + ",HP."+ PstPresence.fieldNames[PstPresence.FLD_PERIOD_ID] + " = " + 0;
                                    
                                   
                           sql = sql + " WHERE (HP."+ PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                                   + " > " +"\""+ Formater.formatDate(selectedDateFrom, "yyyy-MM-dd HH:mm:ss") +"\""
                                   + " AND HP." + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME]
                                   + " < " + "\""+ Formater.formatDate(selectedDateTo, "yyyy-MM-dd HH:mm:ss") +"\")"
                                   + " AND HP."+ PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + " = "+empOid
                                   //update by satrya 2012-09-28
                                   + " AND NOT (HP."+PstPresence.fieldNames[PstPresence.FLD_STATUS] + " IN ("+Presence.STATUS_OUT_ON_DUTY
                                   + ", "+Presence.STATUS_CALL_BACK +"))";

                        //System.out.println("updateScheduleDataByPresence : " + sql);
                        result = DBHandler.execUpdate(sql);
                    }catch (Exception e) {
                        System.out.println("Exc updateScheduleDataByPresence : " + e.toString());
                    } finally {
                        DBResultSet.close(dbrs);
                        return result;
                    }
            }
              return result;
    }
    
    public static void main(String args[])
    {
        System.out.println(" --- started ---");
        int iResult = PstPresence.synchDataPresence();
        System.out.println(" --- finished ---");
    }
    
    /*
    /** 
     * proses import presence per employee per hari sesuai dengan schedulenya     
     * @param scheduleDate
     * @param empSchedule 
     * @created by edhy
     */    
    /*
    public static void importPresenceToScheduleBaseOnDate(Date scheduleDateFrom, Date scheduleDateTo, EmpSchedule empSchedule)
    {               
        // generate parameter dateFrom dan dateTo untuk pencarian data Presence
        System.out.println(".::fetch vectEmpPresence before : " + (new Date()).getTime());
        String strDtFrom = "\"" + com.dimata.util.Formater.formatDate(scheduleDateFrom, "yyyy-MM-dd") + " 00:00:00\"";
        String strDtTo = "\"" + com.dimata.util.Formater.formatDate(scheduleDateTo, "yyyy-MM-dd") + " 23:59:59\"";
        
        // cari vector of presence untuk "employee ini" selama durasi "scheduleDate" pilihan ini
        String whClause = PstPresence.fieldNames[PstPresence.FLD_EMPLOYEE_ID] + 
                          " = " + empSchedule.getEmployeeId() +  
                          " AND " + PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME] + 
                          " BETWEEN " + strDtFrom + 
                          " AND " + strDtTo;   
        
        String ordBy    = PstPresence.fieldNames[PstPresence.FLD_PRESENCE_DATETIME];                  
        Vector vectEmpPresence = PstPresence.list(0, 0, whClause, ordBy);
        System.out.println(".::fetch vectEmpPresence after : " + (new Date()).getTime());

        System.out.println(".::total loop vectEmpPresence before : " + (new Date()).getTime());
        long periodId = empSchedule.getPeriodId();
        if(vectEmpPresence!=null && vectEmpPresence.size()>0)
        {
                int maxPresence = vectEmpPresence.size();    
                for(int i=0; i<maxPresence; i++)
                {
                        System.out.println(".::per loovectEmpPresence before : " + (new Date()).getTime());
                        Presence presence = (Presence) vectEmpPresence.get(i);                                       

                        System.out.println(".::fetch Field Schedule Index before : " + (new Date()).getTime());
                        int updatedFieldIndex = -1;
                        long updatePeriodId = periodId;
                        //Vector vectFieldIndex = PstEmpSchedule.getFieldIndexWillUpdated(periodId, presence.getEmployeeId(), presence.getStatus(), presence.getPresenceDatetime());
                        Vector vectFieldIndex = PstEmpSchedule.getIndexEmpScheduleTableWillUpdated(periodId, presence.getEmployeeId(), presence.getStatus(), presence.getPresenceDatetime());
                        if(vectFieldIndex!=null && vectFieldIndex.size()==2) 
                        {
                                updatePeriodId = Long.parseLong(String.valueOf(vectFieldIndex.get(0)));
                                updatedFieldIndex = Integer.parseInt(String.valueOf(vectFieldIndex.get(1)));
                        }
                        System.out.println(".::fetch Field Schedule Index after : " + (new Date()).getTime());
                        
                        
                        System.out.println(".::per updateScheduleDataByPresence & Presence before : " + (new Date()).getTime());
                        int updateStatus = 0;
                        try 
                        {
                                updateStatus = PstEmpSchedule.updateScheduleDataByPresence(updatePeriodId, presence.getEmployeeId(), updatedFieldIndex, presence.getPresenceDatetime());
                                if(updateStatus>0) 
                                {
                                        presence.setTransferred(PstPresence.PRESENCE_TRANSFERRED);
                                }
                        }
                        catch(Exception e) 
                        {
                                System.out.println("Update Presence exc : "+e.toString());
                        }
                        System.out.println(".::per updateScheduleDataByPresence & Presence after : " + (new Date()).getTime());
                        
                        // process on absence and lateness   
                        // absence dilakukan sebelum lateness karena lateness itu ada jika tidak absence
//                        System.out.println(".::----AbsenceAnalyser.processEmployeeAbsence before : " + (new Date()).getTime());
//                        com.dimata.harisma.utility.service.presence.AbsenceAnalyser.processEmployeeAbsence(presence.getPresenceDatetime(), presence.getEmployeeId());
//                        System.out.println(".::----AbsenceAnalyser.processEmployeeAbsence after : " + (new Date()).getTime());
//                        
//                        System.out.println(".::----per LatenessAnalyser.processEmployeeLateness before : " + (new Date()).getTime());
//                        com.dimata.harisma.utility.service.presence.LatenessAnalyser.processEmployeeLateness(presence.getPresenceDatetime(), presence.getEmployeeId()); 
//                        System.out.println(".::----per LatenessAnalyser.processEmployeeLateness after : " + (new Date()).getTime());
                        
                        try
                        {
                            Thread.sleep(100);    
                        }
                        catch(Exception e)
                        {
                            System.out.println("Exc thread : " + e.toString());
                        }
                        System.out.println(".::per loovectEmpPresence after : " + (new Date()).getTime());
                }  
        }					        
        System.out.println(".::total loop vectEmpPresence after : " + (new Date()).getTime());
    }    
     */
}
