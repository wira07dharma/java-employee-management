/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

/**
 *
 * @author GUSWIK
 */
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.util.*;

public class PstDistrict extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

        public static final String TBL_HR_DISTRICT = "hr_district";
	public static final String TBL_HR_KECAMATAN = "hr_kecamatan";
	public static final String TBL_HR_KABUPATEN = "hr_kabupaten";
        public static final String TBL_HR_PROPINSI = "hr_propinsi";
        public static final String TBL_HR_NEGARA = "hr_negara";

	public static final  int FLD_DISTRICT_ID = 0;
	public static final  int FLD_ID_NEGARA = 1;
	public static final  int FLD_ID_PROPINSI = 2;
        public static final  int FLD_ID_KABUPATEN = 3;
        public static final  int FLD_ID_KECAMATAN = 4;
        public static final  int FLD_NAMA_DISTRICT = 5;
        

    public static final  String[] fieldNames = {
		"DISTRICT_ID",
		"ID_NEGARA",
		"ID_PROPINSI",
		"ID_KABUPATEN",
		"ID_KECAMATAN",
                "NAMA_DISTRICT"
                
	 };

	public static final  int[] fieldTypes = {
		TYPE_LONG + TYPE_PK + TYPE_ID,
		TYPE_LONG,
                TYPE_LONG,
		TYPE_LONG,
		TYPE_LONG,
                TYPE_STRING
                
	 };

	public PstDistrict(){
	}

	public PstDistrict(int i) throws DBException {
		super(new PstDistrict());
	}

	public PstDistrict(String sOid) throws DBException {
		super(new PstDistrict(0));
		if(!locate(sOid))
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		else
			return;
	}

	public PstDistrict(long lOid) throws DBException {
		super(new PstDistrict(0));
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
		return TBL_HR_DISTRICT;
	}

	public String[] getFieldNames(){
		return fieldNames;
	}

	public int[] getFieldTypes(){
		return fieldTypes;
	}

	public String getPersistentName(){
		return new PstDistrict().getClass().getName();
	}

	public long fetchExc(Entity ent) throws Exception{
		District district = fetchExc(ent.getOID());
		ent = (Entity)district;
		return district.getOID();
	}

	public long insertExc(Entity ent) throws Exception{
		return insertExc((District) ent);
	}

	public long updateExc(Entity ent) throws Exception{
		return updateExc((District) ent);
	}

	public long deleteExc(Entity ent) throws Exception{
		if(ent==null){
			throw new DBException(this,DBException.RECORD_NOT_FOUND);
		}
		return deleteExc(ent.getOID());
	}

	public static District fetchExc(long oid) throws DBException{
		try{
			District district = new District();
			PstDistrict pstDistrict = new PstDistrict(oid);
			district.setOID(oid);

			district.setIdNegara(pstDistrict.getlong(FLD_ID_NEGARA));
			district.setIdPropinsi(pstDistrict.getlong(FLD_ID_PROPINSI));
                        district.setIdKabupaten(pstDistrict.getlong(FLD_ID_KABUPATEN));
                        district.setIdKecamatan(pstDistrict.getlong(FLD_ID_KECAMATAN));
			district.setNmDistrict(pstDistrict.getString(FLD_NAMA_DISTRICT));

			return district;
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstDistrict(0),DBException.UNKNOWN);
		}
	}

	public static long insertExc(District district) throws DBException{
		try{
			PstDistrict pstDistrict = new PstDistrict(0);

			pstDistrict.setLong(FLD_ID_NEGARA, district.getIdNegara());
			pstDistrict.setLong(FLD_ID_PROPINSI, district.getIdPropinsi());
			pstDistrict.setLong(FLD_ID_KABUPATEN, district.getIdKabupaten());
			pstDistrict.setLong(FLD_ID_KECAMATAN, district.getIdKecamatan());
			pstDistrict.setString(FLD_NAMA_DISTRICT, district.getNmDistrict());
                        

			pstDistrict.insert();
			district.setOID(pstDistrict.getlong(FLD_DISTRICT_ID));
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstDistrict(0),DBException.UNKNOWN);
		}
		return district.getOID();
	}

	public static long updateExc(District district) throws DBException{
		try{
			if(district.getOID() != 0){
				PstDistrict pstDistrict = new PstDistrict(district.getOID());

				pstDistrict.setLong(FLD_ID_NEGARA, district.getIdNegara());
				pstDistrict.setLong(FLD_ID_PROPINSI, district.getIdPropinsi());
                                pstDistrict.setLong(FLD_ID_KABUPATEN, district.getIdKabupaten());
                                pstDistrict.setLong(FLD_ID_KECAMATAN, district.getIdKecamatan());
				pstDistrict.setString(FLD_NAMA_DISTRICT, district.getNmDistrict());
                                

				pstDistrict.update();
				return district.getOID();

			}
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstDistrict(0),DBException.UNKNOWN);
		}
		return 0;
	}

	public static long deleteExc(long oid) throws DBException{
		try{
			PstDistrict pstDistrict = new PstDistrict(oid);
			pstDistrict.delete();
		}catch(DBException dbe){
			throw dbe;
		}catch(Exception e){
			throw new DBException(new PstDistrict(0),DBException.UNKNOWN);
		}
		return oid;
	}

	public static Vector listAll(){
		return list(0, 500, "","");
	}

	public static Vector list(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT * FROM " + TBL_HR_DISTRICT;
			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				District district = new District();
				resultToObject(rs, district);
				lists.add(district);
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

	public static void resultToObject(ResultSet rs, District district){
		try{
			district.setOID(rs.getLong(PstDistrict.fieldNames[PstDistrict.FLD_DISTRICT_ID]));
			district.setIdNegara(rs.getLong(PstDistrict.fieldNames[PstDistrict.FLD_ID_NEGARA]));
			district.setIdPropinsi(rs.getLong(PstDistrict.fieldNames[PstDistrict.FLD_ID_PROPINSI]));
			district.setIdKabupaten(rs.getLong(PstDistrict.fieldNames[PstDistrict.FLD_ID_KABUPATEN]));
			district.setIdKecamatan(rs.getLong(PstDistrict.fieldNames[PstDistrict.FLD_ID_KECAMATAN]));
			district.setNmDistrict(rs.getString(PstDistrict.fieldNames[PstDistrict.FLD_NAMA_DISTRICT]));
                        
		}catch(Exception e){ }
	}

	public static boolean checkOID(long idDistrict){
		DBResultSet dbrs = null;
		boolean result = false;
		try{
			String sql = "SELECT * FROM " + TBL_HR_DISTRICT + " WHERE " +
						PstDistrict.fieldNames[PstDistrict.FLD_DISTRICT_ID] + " = " + idDistrict;

			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();

			while(rs.next()) { result = true; }
			rs.close();
		}catch(Exception e){
			System.out.println("err : "+e.toString());
		}finally{
			DBResultSet.close(dbrs);

		}
        return result;
	}

	public static int getCount(String whereClause){
		DBResultSet dbrs = null;
		try {
			String sql = "SELECT COUNT("+ PstDistrict.fieldNames[PstDistrict.FLD_DISTRICT_ID] + ") FROM " + TBL_HR_DISTRICT;
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


	/* This method used to find current datainduk */
	public static int findLimitStart( long oid, int recordToGet, String whereClause, String order){
		//String order = "";
		int size = getCount(whereClause);
		int start = 0;
		boolean found =false;
		for(int i=0; (i < size) && !found ; i=i+recordToGet){
			 Vector list =  list(i,recordToGet, whereClause, order);
			 start = i;
			 if(list.size()>0){
			  for(int ls=0;ls<list.size();ls++){
			  	   District district = (District)list.get(ls);
				   if(oid == district.getOID())
					  found=true;
			  }
		  }
		}
		if((start >= size) && (size > 0))
		    start = start - recordToGet;

		return start;
	}

    public static Hashtable getListDisPeg() {
        Hashtable lists = new Hashtable();
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT * FROM " + TBL_HR_DISTRICT;

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                District pnsDist = new District();
                resultToObject(rs, pnsDist);
                lists.put(pnsDist.getNmDistrict().toUpperCase(),pnsDist);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return new Hashtable();
    }

    public static int findLimitCommand(int start, int recordToGet, int vectSize) {
        int cmd = Command.LIST;
        int mdl = vectSize % recordToGet;
        vectSize = vectSize + mdl;
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
    public static Vector listJoinDist(int limitStart,int recordToGet, String whereClause, String order){
		Vector lists = new Vector();
		DBResultSet dbrs = null;
		try {
			String sql = " SELECT * FROM " + TBL_HR_DISTRICT  + " d "+
                                     " INNER JOIN " + TBL_HR_KECAMATAN  + " kec "+
                                     " ON d."+PstDistrict.fieldNames[PstDistrict.FLD_ID_KECAMATAN]+
                                     " = kec." +PstKecamatan.fieldNames[PstKecamatan.FLD_ID_KECAMATAN]+
                                     " INNER JOIN " + TBL_HR_KABUPATEN  + " k "+
                                     " ON d."+PstDistrict.fieldNames[PstDistrict.FLD_ID_KABUPATEN]+
                                     " = k." +PstKabupaten.fieldNames[PstKabupaten.FLD_ID_KABUPATEN]+
                                     " INNER JOIN " + TBL_HR_PROPINSI  + " p "+
                                     " ON d."+PstDistrict.fieldNames[PstDistrict.FLD_ID_PROPINSI]+
                                     " = p." +PstProvinsi.fieldNames[PstProvinsi.FLD_ID_PROVINSI]+
                                     " INNER JOIN " + TBL_HR_NEGARA+ " n " +
                                     " ON d."+PstDistrict.fieldNames[PstDistrict.FLD_ID_NEGARA]+
                                     " = n." +PstNegara.fieldNames[PstNegara.FLD_ID_NEGARA];

			if(whereClause != null && whereClause.length() > 0)
				sql = sql + " WHERE " + whereClause;
			if(order != null && order.length() > 0)
				sql = sql + " ORDER BY " + order;
			if(limitStart == 0 && recordToGet == 0)
				sql = sql + "";
			else
				sql = sql + " LIMIT " + limitStart + ","+ recordToGet ;
			dbrs = DBHandler.execQueryResult(sql);
			ResultSet rs = dbrs.getResultSet();
			while(rs.next()) {
				District district = new District();
				resultToObject(rs, district);
				lists.add(district);
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

}

