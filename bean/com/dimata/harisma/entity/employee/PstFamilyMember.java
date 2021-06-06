
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: lkarunia
 * @version  	: 01
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...]
 * *****************************************************************
 */
package com.dimata.harisma.entity.employee;

/* package java */
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

/* package qdep */
import com.dimata.util.lang.I_Language;
import com.dimata.util.*;
import com.dimata.qdep.db.*;
import com.dimata.qdep.entity.*;
import com.dimata.harisma.entity.masterdata.*;

/* package  harisma */
//import com.dimata.harisma.db.DBHandler;
//import com.dimata.harisma.db.DBException;
//import com.dimata.harisma.db.DBLogger;
import com.dimata.harisma.entity.employee.*;

//Gede_7Feb2012 {
import com.dimata.harisma.session.employee.*;
import com.dimata.harisma.entity.search.*;
import com.dimata.harisma.entity.payroll.*;
//}

public class PstFamilyMember extends DBHandler implements I_DBInterface, I_DBType, I_PersintentExc, I_Language {

    public static final String TBL_HR_FAMILY_MEMBER = "hr_family_member";//"HR_FAMILY_MEMBER";
    public static final int FLD_FAMILY_MEMBER_ID = 0;
    public static final int FLD_EMPLOYEE_ID = 1;
    public static final int FLD_FULL_NAME = 2;
    public static final int FLD_RELATIONSHIP = 3;
    public static final int FLD_BIRTH_DATE = 4;
    public static final int FLD_JOB = 5;
    public static final int FLD_ADDRESS = 6;
    public static final int FLD_GUARANTEED = 7;
    public static final int FLD_IGNORE_BIRTH = 8;
    public static final int FLD_EDUCATION_ID = 9;
    public static final int FLD_RELIGION_ID = 10;
    public static final int FLD_SEX = 11;
    public static final int FLD_STATUS = 12;
    public static final int FLD_BLOOD_TYPE = 13;
    public static final int FLD_EDUCATION_DEGREE = 14;
    public static final int FLD_PHONE_NUMBER = 15;
    public static final int FLD_COMPANY_NAME = 16;
    public static final int FLD_COMPANY_ADDRESS = 17;
    public static final int FLD_COMPANY_PHONE_NUMBER = 18;
    public static final int FLD_COMPANY_POSTAL_CODE = 19;
    public static final int FLD_PRESENT_ADDRESS = 20;
    public static final int FLD_SUBVILLAGE_PRESENT_ADDRESS = 21;
    public static final int FLD_VILLAGE_PRESENT_ADDRESS = 22;
    public static final int FLD_SUBDISTRIC_PRESENT_ADDRESS = 23;
    public static final int FLD_DISTRIC_PRESENT_ADDRESS = 24;
    public static final int FLD_REGENCY_PRESENT_ADDRESS = 25;
    public static final int FLD_PHONE_NUMBER_PRESENT_ADDRESS = 26;
    public static final int FLD_POSTAL_CODE_PRESENT_ADDRESS = 27;
    public static final int FLD_ID_CARD_ADDRESS = 28;
    public static final int FLD_SUBVILLAGE_ID_CARD_ADDRESS = 29;
    public static final int FLD_VILLAGE_ID_CARD_ADDRESS = 30;
    public static final int FLD_SUBDISTRIC_ID_CARD_ADDRESS = 31;
    public static final int FLD_DISTRIC_ID_CARD_ADDRESS = 32;
    public static final int FLD_REGENCY_ID_CARD_ADDRESS = 33;
    public static final int FLD_PHONE_NUMBER_ID_CARD_ADDRESS = 34;
    public static final int FLD_POSTAL_CODE_ID_CARD_ADDRESS = 35;
    public static final int FLD_SINCE_ADDRESS = 36;
    public static final int FLD_SUBVILLAGE_SINCE_ADDRESS = 37;
    public static final int FLD_VILLAGE_SINCE_ADDRESS = 38;
    public static final int FLD_SUBDISTRIC_SINCE_ADDRESS = 39;
    public static final int FLD_DISTRIC_SINCE_ADDRESS = 40;
    public static final int FLD_REGENCY_SINCE_ADDRESS = 41;
    public static final int FLD_PHONE_NUMBER_SINCE_ADDRESS = 42;
    public static final int FLD_POSTAL_CODE_SINCE_ADDRESS = 43;
    public static final int FLD_RELATION_TITLE = 44;
    public static final int FLD_RELATIONSHIP_INDEX = 45;
    public static final int FLD_JOB_INSTITUTIONS = 46;

    public static final String[] fieldNames = {
        "FAMILY_MEMBER_ID",//0
        "EMPLOYEE_ID",//1
        "FULL_NAME",//2
        "RELATIONSHIP",//3
        "BIRTH_DATE",//4
        "JOB",//5
        "ADDRESS",//6
        "GUARANTEED",//7
        "IGNORE_BIRTH",//8
        "EDUCATION_ID",//9
        "RELIGION_ID",//10
        "SEX",//11
        "STATUS",//12
        "BLOOD_TYPE",//13
        "EDUCATION_DEGREE",//14
        "PHONE_NUMBER",//15
        "COMPANY_NAME",//16
        "COMPANY_ADDRESS",//17
        "COMPANY_PHONE_NUMBER",//18
        "COMPANY_POSTAL_CODE",//19
        "PRESENT_ADDRESS",//20
        "SUBVILLAGE_PRESENT_ADDRESS",//21
        "VILLAGE_PRESENT_ADDRESS",//22
        "SUBDISTRIC_PRESENT_ADDRESS",//23
        "DISTRIC_PRESENT_ADDRESS",//24
        "REGENCY_PRESENT_ADDRESS",//25
        "PHONE_NUMBER_PRESENT_ADDRESS",//26
        "POSTAL_CODE_PRESENT_ADDRESS",//27
        "ID_CARD_ADDRESS",//28
        "SUBVILLAGE_ID_CARD_ADDRESS",//29
        "VILLAGE_ID_CARD_ADDRESS",//30
        "SUBDISTRIC_ID_CARD_ADDRESS",//31
        "DISTRIC_ID_CARD_ADDRESS",//32
        "REGENCY_ID_CARD_ADDRESS",//33
        "PHONE_NUMBER_ID_CARD_ADDRESS",//34
        "POSTAL_CODE_ID_CARD_ADDRESS",//35
        "SINCE_ADDRESS",//36
        "SUBVILLAGE_SINCE_ADDRESS",//37
        "VILLAGE_SINCE_ADDRESS",//38
        "SUBDISTRIC_SINCE_ADDRESS",//39
        "DISTRIC_SINCE_ADDRESS",//40
        "REGENCY_SINCE_ADDRESS",//41
        "PHONE_NUMBER_SINCE_ADDRESS",//42
        "POSTAL_CODE_SINCE_ADDRESS",//43
        "RELATION_TITLE",//44
        "RELATIONSHIP_INDEX",//45
        "JOB_INSTITUTIONS"//46
    };
    public static final int[] fieldTypes = {
        TYPE_LONG + TYPE_PK + TYPE_ID,//0
        TYPE_LONG,//1
        TYPE_STRING,//2
        TYPE_STRING,//3
        TYPE_DATE,//4
        TYPE_STRING,//5
        TYPE_STRING,//6
        TYPE_BOOL,//7
        TYPE_BOOL,//8
        TYPE_LONG + TYPE_FK,//9
        TYPE_LONG + TYPE_FK,//10
        TYPE_INT,//11
        TYPE_INT,//12
        TYPE_STRING,//13
        TYPE_STRING,//14
        TYPE_STRING,//15
        TYPE_STRING,//16
        TYPE_STRING,//17
        TYPE_STRING,//18
        TYPE_STRING,//19
        TYPE_STRING,//20
        TYPE_STRING,//21
        TYPE_STRING,//22
        TYPE_STRING,//23
        TYPE_STRING,//24
        TYPE_STRING,//25
        TYPE_STRING,//26
        TYPE_STRING,//27
        TYPE_STRING,//28
        TYPE_STRING,//29
        TYPE_STRING,//30
        TYPE_STRING,//31
        TYPE_STRING,//32
        TYPE_STRING,//33
        TYPE_STRING,//34
        TYPE_STRING,//35
        TYPE_STRING,//36
        TYPE_STRING,//37
        TYPE_STRING,//38
        TYPE_STRING,//39
        TYPE_STRING,//40
        TYPE_STRING,//41
        TYPE_STRING,//42
        TYPE_STRING,//43
        TYPE_STRING,//44
        TYPE_STRING,//45
        TYPE_STRING//46
    };
    //gender----
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final String[] sexKey = {"Male", "Female"};
    public static final int[] sexValue = {0, 1};
    //status~~~~
    public static final int SINGLE = 0;
    public static final int MARRIED = 1;
    public static final int WIDOWED = 2;
    public static final int DIVORCED = 3;
    public static final String[] statusKey = {"Single", "Married", "Widowed", "Divorced"};
    public static final int[] statusValue = {0, 1, 2, 3};
    //bloodtype~~~~
    public static final String[] blood = {"?", "A", "B", "O", "AB"};

    //relation index
    //private static final String[] relationshipIndex = {"?","1","2"};
    /*
     public static final int REL_WIFE			= 0;
     public static final int REL_HUSBAND			= 1;
     public static final int REL_CHILDREN		= 2;

     public static String[] relationValue = {"Wife","Husband","Children"};

     public static Vector getRelation(){
     Vector result = new Vector(1,1);
     for(int i=0;i<relationValue.length;i++){
     result.add(relationValue[i]);
     }
     return result;
     }*/

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
     * @return the blood
     */
    public static Vector getBlood() {
        Vector result = new Vector(1, 1);
        for (int i = 0; i < blood.length; i++) {
            result.add(blood[i]);
        }
        return result;
    }

    /**
     * @return the Relation Index
     */
//    public static Vector getRelationshipIndex() {
//        Vector result = new Vector(1, 1);
//        for (int i = 0; i < relationshipIndex.length; i++) {
//            result.add(relationshipIndex[i]);
//        }
//        return result;
//    }
    
    public PstFamilyMember() {
    }

    public PstFamilyMember(int i) throws DBException {
        super(new PstFamilyMember());
    }

    public PstFamilyMember(String sOid) throws DBException {
        super(new PstFamilyMember(0));
        if (!locate(sOid)) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        } else {
            return;
        }
    }

    public PstFamilyMember(long lOid) throws DBException {
        super(new PstFamilyMember(0));
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
        return TBL_HR_FAMILY_MEMBER;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String getPersistentName() {
        return new PstFamilyMember().getClass().getName();
    }

    public long fetchExc(Entity ent) throws Exception {
        FamilyMember familymember = fetchExc(ent.getOID());
        ent = (Entity) familymember;
        return familymember.getOID();
    }

    public long insertExc(Entity ent) throws Exception {
        return insertExc((FamilyMember) ent);
    }

    public long updateExc(Entity ent) throws Exception {
        return updateExc((FamilyMember) ent);
    }

    public long deleteExc(Entity ent) throws Exception {
        if (ent == null) {
            throw new DBException(this, DBException.RECORD_NOT_FOUND);
        }
        return deleteExc(ent.getOID());
    }

    public static FamilyMember fetchExc(long oid) throws DBException {
        try {
            FamilyMember familymember = new FamilyMember();
            PstFamilyMember pstFamilyMember = new PstFamilyMember(oid);
            familymember.setOID(oid);

            familymember.setEmployeeId(pstFamilyMember.getlong(FLD_EMPLOYEE_ID));
            familymember.setFullName(pstFamilyMember.getString(FLD_FULL_NAME));
            familymember.setRelationship(pstFamilyMember.getString(FLD_RELATIONSHIP));
            familymember.setRelationshipIndex(pstFamilyMember.getString(FLD_RELATIONSHIP_INDEX));
            familymember.setRelationTitle(pstFamilyMember.getString(FLD_RELATION_TITLE));
            familymember.setBirthDate(pstFamilyMember.getDate(FLD_BIRTH_DATE));
            familymember.setBloodType(pstFamilyMember.getString(FLD_BLOOD_TYPE));
            familymember.setEducationDegree(pstFamilyMember.getString(FLD_EDUCATION_DEGREE));
            familymember.setPhoneNumber(pstFamilyMember.getString(FLD_PHONE_NUMBER));
            familymember.setJob(pstFamilyMember.getString(FLD_JOB));
            familymember.setAddress(pstFamilyMember.getString(FLD_ADDRESS));
            familymember.setGuaranteed(pstFamilyMember.getboolean(FLD_GUARANTEED));
            familymember.setIgnoreBirth(pstFamilyMember.getboolean(FLD_IGNORE_BIRTH));
            familymember.setEducationId(pstFamilyMember.getlong(FLD_EDUCATION_ID));
            familymember.setReligionId(pstFamilyMember.getlong(FLD_RELIGION_ID));
            familymember.setSex(pstFamilyMember.getInt(FLD_SEX));
            familymember.setStatus(pstFamilyMember.getInt(FLD_STATUS));
            familymember.setCompanyName(pstFamilyMember.getString(FLD_COMPANY_NAME));
            familymember.setCompanyAddress(pstFamilyMember.getString(FLD_COMPANY_ADDRESS));
            familymember.setCompanyPhone(pstFamilyMember.getString(FLD_COMPANY_PHONE_NUMBER));
            familymember.setCompanyPos(pstFamilyMember.getString(FLD_COMPANY_POSTAL_CODE));
            //sekarang
            familymember.setPresentAddress(pstFamilyMember.getString(FLD_PRESENT_ADDRESS));
            familymember.setSubvillagePresentAddress(pstFamilyMember.getString(FLD_SUBVILLAGE_PRESENT_ADDRESS));
            familymember.setVillagePresentAddress(pstFamilyMember.getString(FLD_VILLAGE_PRESENT_ADDRESS));
            familymember.setSubregencyPresentAddress(pstFamilyMember.getString(FLD_SUBDISTRIC_PRESENT_ADDRESS));
            familymember.setRegencyPresentAddress(pstFamilyMember.getString(FLD_DISTRIC_PRESENT_ADDRESS));
            familymember.setProvincePresentAddress(pstFamilyMember.getString(FLD_REGENCY_PRESENT_ADDRESS));
            familymember.setPhonePresentAddress(pstFamilyMember.getString(FLD_PHONE_NUMBER_PRESENT_ADDRESS));
            familymember.setPostalcodePresentAddress(pstFamilyMember.getString(FLD_POSTAL_CODE_PRESENT_ADDRESS));
            //id card
            familymember.setIdCardAddress(pstFamilyMember.getString(FLD_ID_CARD_ADDRESS));
            familymember.setSubvillageIdCardAddress(pstFamilyMember.getString(FLD_SUBVILLAGE_ID_CARD_ADDRESS));
            familymember.setVillageIdCardAddress(pstFamilyMember.getString(FLD_VILLAGE_ID_CARD_ADDRESS));
            familymember.setSubregencyIdCardAddress(pstFamilyMember.getString(FLD_SUBDISTRIC_ID_CARD_ADDRESS));
            familymember.setRegencyIdCardAddress(pstFamilyMember.getString(FLD_DISTRIC_ID_CARD_ADDRESS));
            familymember.setProvinceIdCardAddress(pstFamilyMember.getString(FLD_REGENCY_ID_CARD_ADDRESS));
            familymember.setPhoneIdCardAddress(pstFamilyMember.getString(FLD_PHONE_NUMBER_ID_CARD_ADDRESS));
            familymember.setPostalcodeIdCardAddress(pstFamilyMember.getString(FLD_POSTAL_CODE_ID_CARD_ADDRESS));
            //asal 
            familymember.setSinceAddress(pstFamilyMember.getString(FLD_SINCE_ADDRESS));
            familymember.setSubvillageSinceAddress(pstFamilyMember.getString(FLD_SUBVILLAGE_SINCE_ADDRESS));
            familymember.setVillageSinceAddress(pstFamilyMember.getString(FLD_VILLAGE_SINCE_ADDRESS));
            familymember.setSubregencySinceAddress(pstFamilyMember.getString(FLD_SUBDISTRIC_SINCE_ADDRESS));
            familymember.setRegencySinceAddress(pstFamilyMember.getString(FLD_DISTRIC_SINCE_ADDRESS));
            familymember.setProvinceSinceAddress(pstFamilyMember.getString(FLD_REGENCY_SINCE_ADDRESS));
            familymember.setPhoneSinceAddress(pstFamilyMember.getString(FLD_PHONE_NUMBER_SINCE_ADDRESS));
            familymember.setPostalcodeSinceAddress(pstFamilyMember.getString(FLD_POSTAL_CODE_SINCE_ADDRESS));
            familymember.setJobInstitutions(pstFamilyMember.getString(FLD_JOB_INSTITUTIONS));
            return familymember;
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFamilyMember(0), DBException.UNKNOWN);
        }
    }

    public static long insertExc(FamilyMember familymember) throws DBException {
        try {
            PstFamilyMember pstFamilyMember = new PstFamilyMember(0);

            pstFamilyMember.setLong(FLD_EMPLOYEE_ID, familymember.getEmployeeId());
            pstFamilyMember.setString(FLD_FULL_NAME, familymember.getFullName());
            pstFamilyMember.setString(FLD_RELATIONSHIP, familymember.getRelationship());
            pstFamilyMember.setString(FLD_RELATIONSHIP_INDEX, familymember.getRelationshipIndex());
            pstFamilyMember.setString(FLD_RELATION_TITLE, familymember.getRelationTitle());
            pstFamilyMember.setDate(FLD_BIRTH_DATE, familymember.getBirthDate());
            pstFamilyMember.setString(FLD_BLOOD_TYPE, familymember.getBloodType());
            pstFamilyMember.setString(FLD_EDUCATION_DEGREE, familymember.getEducationDegree());
            pstFamilyMember.setString(FLD_PHONE_NUMBER, familymember.getPhoneNumber());
            pstFamilyMember.setString(FLD_JOB, familymember.getJob());
            pstFamilyMember.setString(FLD_ADDRESS, familymember.getAddress());
            pstFamilyMember.setboolean(FLD_GUARANTEED, familymember.getGuaranteed());
            pstFamilyMember.setboolean(FLD_IGNORE_BIRTH, familymember.getIgnoreBirth());
            pstFamilyMember.setLong(FLD_EDUCATION_ID, familymember.getEducationId());
            pstFamilyMember.setLong(FLD_RELIGION_ID, familymember.getReligionId());
            pstFamilyMember.setInt(FLD_SEX, familymember.getSex());
            pstFamilyMember.setInt(FLD_STATUS, familymember.getStatus());
            pstFamilyMember.setString(FLD_COMPANY_NAME, familymember.getCompanyName());
            pstFamilyMember.setString(FLD_COMPANY_ADDRESS, familymember.getCompanyAddress());
            pstFamilyMember.setString(FLD_COMPANY_PHONE_NUMBER, familymember.getCompanyPhone());
            pstFamilyMember.setString(FLD_COMPANY_POSTAL_CODE, familymember.getCompanyPos());
            //Sekarang
            pstFamilyMember.setString(FLD_PRESENT_ADDRESS, familymember.getPresentAddress());
            pstFamilyMember.setString(FLD_SUBVILLAGE_PRESENT_ADDRESS, familymember.getSubvillagePresentAddress());
            pstFamilyMember.setString(FLD_VILLAGE_PRESENT_ADDRESS, familymember.getVillagePresentAddress());
            pstFamilyMember.setString(FLD_SUBDISTRIC_PRESENT_ADDRESS, familymember.getSubregencyPresentAddress());
            pstFamilyMember.setString(FLD_DISTRIC_PRESENT_ADDRESS, familymember.getRegencyPresentAddress());
            pstFamilyMember.setString(FLD_REGENCY_PRESENT_ADDRESS, familymember.getProvincePresentAddress());
            pstFamilyMember.setString(FLD_PHONE_NUMBER_PRESENT_ADDRESS, familymember.getPhonePresentAddress());
            pstFamilyMember.setString(FLD_POSTAL_CODE_PRESENT_ADDRESS, familymember.getPostalcodePresentAddress());
            //id card
            pstFamilyMember.setString(FLD_ID_CARD_ADDRESS, familymember.getIdCardAddress());
            pstFamilyMember.setString(FLD_SUBVILLAGE_ID_CARD_ADDRESS, familymember.getSubvillageIdCardAddress());
            pstFamilyMember.setString(FLD_VILLAGE_ID_CARD_ADDRESS, familymember.getVillageIdCardAddress());
            pstFamilyMember.setString(FLD_SUBDISTRIC_ID_CARD_ADDRESS, familymember.getSubregencyIdCardAddress());
            pstFamilyMember.setString(FLD_DISTRIC_ID_CARD_ADDRESS, familymember.getRegencyIdCardAddress());
            pstFamilyMember.setString(FLD_REGENCY_ID_CARD_ADDRESS, familymember.getProvinceIdCardAddress());
            pstFamilyMember.setString(FLD_PHONE_NUMBER_ID_CARD_ADDRESS, familymember.getPhoneIdCardAddress());
            pstFamilyMember.setString(FLD_POSTAL_CODE_ID_CARD_ADDRESS, familymember.getPostalcodeIdCardAddress());
            //since
            pstFamilyMember.setString(FLD_SINCE_ADDRESS, familymember.getSinceAddress());
            pstFamilyMember.setString(FLD_SUBVILLAGE_SINCE_ADDRESS, familymember.getSubvillageSinceAddress());
            pstFamilyMember.setString(FLD_VILLAGE_SINCE_ADDRESS, familymember.getVillageSinceAddress());
            pstFamilyMember.setString(FLD_SUBDISTRIC_SINCE_ADDRESS, familymember.getSubregencySinceAddress());
            pstFamilyMember.setString(FLD_DISTRIC_SINCE_ADDRESS, familymember.getRegencySinceAddress());
            pstFamilyMember.setString(FLD_REGENCY_SINCE_ADDRESS, familymember.getProvinceSinceAddress());
            pstFamilyMember.setString(FLD_PHONE_NUMBER_SINCE_ADDRESS, familymember.getPhoneSinceAddress());
            pstFamilyMember.setString(FLD_POSTAL_CODE_SINCE_ADDRESS, familymember.getPostalcodeSinceAddress());
            pstFamilyMember.setString(FLD_JOB_INSTITUTIONS, familymember.getJobInstitutions());
            pstFamilyMember.insert();
            familymember.setOID(pstFamilyMember.getlong(FLD_FAMILY_MEMBER_ID));
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFamilyMember(0), DBException.UNKNOWN);
        }
        return familymember.getOID();
    }

    public static long updateExc(FamilyMember familymember) throws DBException {
        try {
            if (familymember.getOID() != 0) {
                PstFamilyMember pstFamilyMember = new PstFamilyMember(familymember.getOID());

                pstFamilyMember.setLong(FLD_EMPLOYEE_ID, familymember.getEmployeeId());
                pstFamilyMember.setString(FLD_FULL_NAME, familymember.getFullName());
                pstFamilyMember.setString(FLD_RELATIONSHIP, familymember.getRelationship());
                pstFamilyMember.setString(FLD_RELATIONSHIP_INDEX, familymember.getRelationshipIndex());
                pstFamilyMember.setString(FLD_RELATION_TITLE, familymember.getRelationTitle());
                pstFamilyMember.setDate(FLD_BIRTH_DATE, familymember.getBirthDate());
                pstFamilyMember.setString(FLD_BLOOD_TYPE, familymember.getBloodType());
                pstFamilyMember.setString(FLD_EDUCATION_DEGREE, familymember.getEducationDegree());
                pstFamilyMember.setString(FLD_PHONE_NUMBER, familymember.getPhoneNumber());
                pstFamilyMember.setString(FLD_JOB, familymember.getJob());
                pstFamilyMember.setString(FLD_ADDRESS, familymember.getAddress());
                pstFamilyMember.setboolean(FLD_GUARANTEED, familymember.getGuaranteed());
                pstFamilyMember.setboolean(FLD_IGNORE_BIRTH, familymember.getIgnoreBirth());
                pstFamilyMember.setLong(FLD_EDUCATION_ID, familymember.getEducationId());
                pstFamilyMember.setLong(FLD_RELIGION_ID, familymember.getReligionId());
                pstFamilyMember.setInt(FLD_SEX, familymember.getSex());
                pstFamilyMember.setInt(FLD_STATUS, familymember.getStatus());
                pstFamilyMember.setString(FLD_COMPANY_NAME, familymember.getCompanyName());
                pstFamilyMember.setString(FLD_COMPANY_ADDRESS, familymember.getCompanyAddress());
                pstFamilyMember.setString(FLD_COMPANY_PHONE_NUMBER, familymember.getCompanyPhone());
                pstFamilyMember.setString(FLD_COMPANY_POSTAL_CODE, familymember.getCompanyPos());
                //Sekarang
                pstFamilyMember.setString(FLD_PRESENT_ADDRESS, familymember.getPresentAddress());
                pstFamilyMember.setString(FLD_SUBVILLAGE_PRESENT_ADDRESS, familymember.getSubvillagePresentAddress());
                pstFamilyMember.setString(FLD_VILLAGE_PRESENT_ADDRESS, familymember.getVillagePresentAddress());
                pstFamilyMember.setString(FLD_SUBDISTRIC_PRESENT_ADDRESS, familymember.getSubregencyPresentAddress());
                pstFamilyMember.setString(FLD_DISTRIC_PRESENT_ADDRESS, familymember.getRegencyPresentAddress());
                pstFamilyMember.setString(FLD_REGENCY_PRESENT_ADDRESS, familymember.getProvincePresentAddress());
                pstFamilyMember.setString(FLD_PHONE_NUMBER_PRESENT_ADDRESS, familymember.getPhonePresentAddress());
                pstFamilyMember.setString(FLD_POSTAL_CODE_PRESENT_ADDRESS, familymember.getPostalcodePresentAddress());
                //id card
                pstFamilyMember.setString(FLD_ID_CARD_ADDRESS, familymember.getIdCardAddress());
                pstFamilyMember.setString(FLD_SUBVILLAGE_ID_CARD_ADDRESS, familymember.getSubvillageIdCardAddress());
                pstFamilyMember.setString(FLD_VILLAGE_ID_CARD_ADDRESS, familymember.getVillageIdCardAddress());
                pstFamilyMember.setString(FLD_SUBDISTRIC_ID_CARD_ADDRESS, familymember.getSubregencyIdCardAddress());
                pstFamilyMember.setString(FLD_DISTRIC_ID_CARD_ADDRESS, familymember.getRegencyIdCardAddress());
                pstFamilyMember.setString(FLD_REGENCY_ID_CARD_ADDRESS, familymember.getProvinceIdCardAddress());
                pstFamilyMember.setString(FLD_PHONE_NUMBER_ID_CARD_ADDRESS, familymember.getPhoneIdCardAddress());
                pstFamilyMember.setString(FLD_POSTAL_CODE_ID_CARD_ADDRESS, familymember.getPostalcodeIdCardAddress());
                //since
                pstFamilyMember.setString(FLD_SINCE_ADDRESS, familymember.getSinceAddress());
                pstFamilyMember.setString(FLD_SUBVILLAGE_SINCE_ADDRESS, familymember.getSubvillageSinceAddress());
                pstFamilyMember.setString(FLD_VILLAGE_SINCE_ADDRESS, familymember.getVillageSinceAddress());
                pstFamilyMember.setString(FLD_SUBDISTRIC_SINCE_ADDRESS, familymember.getSubregencySinceAddress());
                pstFamilyMember.setString(FLD_DISTRIC_SINCE_ADDRESS, familymember.getRegencySinceAddress());
                pstFamilyMember.setString(FLD_REGENCY_SINCE_ADDRESS, familymember.getProvinceSinceAddress());
                pstFamilyMember.setString(FLD_PHONE_NUMBER_SINCE_ADDRESS, familymember.getPhoneSinceAddress());
                pstFamilyMember.setString(FLD_POSTAL_CODE_SINCE_ADDRESS, familymember.getPostalcodeSinceAddress());
                pstFamilyMember.setString(FLD_JOB_INSTITUTIONS, familymember.getJobInstitutions());
                pstFamilyMember.update();
                return familymember.getOID();

            }
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFamilyMember(0), DBException.UNKNOWN);
        }
        return 0;
    }
    private FamRelation famRelation;

    public static void updateFamRelation(String PrevFamRelation, String FamilyRelation) {
        try {
            String sql = " UPDATE " + PstFamilyMember.TBL_HR_FAMILY_MEMBER + " SET "
                    + PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + " = " + "'" + FamilyRelation + "'"
                    + " WHERE " + PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + " = " + "'" + PrevFamRelation + "'";

            int status = DBHandler.execUpdate(sql);
        } catch (Exception exc) {
            System.out.println("error delete experience by employee " + exc.toString());
        }

    }

    public static boolean existFamRelation(String FamilyRelation) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + " FROM " + PstFamilyMember.TBL_HR_FAMILY_MEMBER + " AS FM "
                    + " WHERE " + PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + " = '" + FamilyRelation + "'";
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            /*if (rs!=null){
             result = true;
             }
             } catch (Exception e) {
             System.out.println("Exception " + e.toString());
            
             }
             return result;*/
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

    public static long deleteExc(long oid) throws DBException {
        try {
            PstFamilyMember pstFamilyMember = new PstFamilyMember(oid);
            pstFamilyMember.delete();
        } catch (DBException dbe) {
            throw dbe;
        } catch (Exception e) {
            throw new DBException(new PstFamilyMember(0), DBException.UNKNOWN);
        }
        return oid;
    }

    public static Vector listAll() {
        return list(0, 500, "", "");
    }

    public static Vector list(int limitStart, int recordToGet, String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {
            /**
             * Ari_20111008
             */
            String sql = "SELECT FM.*,FR." + PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION_TYPE] + " FROM " + TBL_HR_FAMILY_MEMBER + " AS FM "
                    + " LEFT JOIN " + PstFamRelation.TBL_HR_FAM_RELATION + " AS FR "
                    + "ON FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + " = "
                    + "FR." + PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION];
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
            System.out.println(sql);
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                FamilyMember familymember = new FamilyMember();
                resultToObject(rs, familymember);
                lists.add(familymember);
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

    /**
     * create by satrya 2013-07-31
     *
     * @param whereClause
     * @param order
     * @return
     */
    public static Vector listEmpFamily(String whereClause, String order) {
        Vector lists = new Vector();
        DBResultSet dbrs = null;
        try {

            String sql = "SELECT FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID]
                    + ",FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_FULL_NAME]
                    + ",FR." + PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION]
                    + ",FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_BIRTH_DATE]
                    + ",FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_JOB]
                    + ",FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_ADDRESS]
                    + ",FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_GUARANTEED]
                    + ",REL." + PstReligion.fieldNames[PstReligion.FLD_RELIGION]
                    + ",EDU." + PstEducation.fieldNames[PstEducation.FLD_EDUCATION]
                    + ",FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_SEX] + " FROM " + PstFamilyMember.TBL_HR_FAMILY_MEMBER + " AS FM "
                    + " INNER JOIN " + PstFamRelation.TBL_HR_FAM_RELATION + " AS FR  ON FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + "=FR." + PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION_ID]
                    + " LEFT JOIN " + PstEducation.TBL_HR_EDUCATION + " AS EDU ON EDU." + PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID] + "=FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_EDUCATION_ID]
                    + " LEFT JOIN " + PstReligion.TBL_HR_RELIGION + " AS REL ON REL." + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID] + "=FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELIGION_ID];
            if (whereClause != null && whereClause.length() > 0) {
                sql = sql + " WHERE " + whereClause;
            }
            if (order != null && order.length() > 0) {
                sql = sql + " ORDER BY " + order;
            }

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            while (rs.next()) {
                //FamilyMember familymember = new FamilyMember(); xx
                SessEmpFamilyMember sessEmpFamilyMember = new SessEmpFamilyMember();
                sessEmpFamilyMember.setEmployeeId(rs.getLong(PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID]));
                sessEmpFamilyMember.setFamilyRelation(rs.getString(PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION]));
                sessEmpFamilyMember.setBirthDate(rs.getDate(PstFamilyMember.fieldNames[PstFamilyMember.FLD_BIRTH_DATE]));
                sessEmpFamilyMember.setJob(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_JOB]));
                sessEmpFamilyMember.setAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_ADDRESS]));
                sessEmpFamilyMember.setGuaranted(rs.getInt(PstFamilyMember.fieldNames[PstFamilyMember.FLD_GUARANTEED]));
                sessEmpFamilyMember.setReligion(rs.getString(PstReligion.fieldNames[PstReligion.FLD_RELIGION]));
                sessEmpFamilyMember.setEducation(rs.getString(PstEducation.fieldNames[PstEducation.FLD_EDUCATION]));
                sessEmpFamilyMember.setSex(rs.getInt(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SEX]));
                sessEmpFamilyMember.setFullName(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_FULL_NAME]));
                //resultToObject(rs, familymember);
                //familymember.set
                lists.add(sessEmpFamilyMember);
            }
            rs.close();
            return lists;

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
        return lists;
    }

    private static void resultToObject(ResultSet rs, FamilyMember familymember) {
        try {
            familymember.setOID(rs.getLong(PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID]));
            familymember.setEmployeeId(rs.getLong(PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID]));
            familymember.setFullName(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_FULL_NAME]));
            familymember.setRelationship(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP]));
            familymember.setRelationshipIndex(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP_INDEX]));
            familymember.setRelationTitle(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATION_TITLE]));
            familymember.setBirthDate(rs.getDate(PstFamilyMember.fieldNames[PstFamilyMember.FLD_BIRTH_DATE]));
            familymember.setBloodType(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_BLOOD_TYPE]));
            familymember.setEducationDegree(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_EDUCATION_DEGREE]));
            familymember.setPhoneNumber(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_PHONE_NUMBER]));
            familymember.setJob(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_JOB]));
            familymember.setAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_ADDRESS]));
            familymember.setGuaranteed(rs.getBoolean(PstFamilyMember.fieldNames[PstFamilyMember.FLD_GUARANTEED]));
            familymember.setIgnoreBirth(rs.getBoolean(PstFamilyMember.fieldNames[PstFamilyMember.FLD_IGNORE_BIRTH]));
            familymember.setRelationType(rs.getInt(PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION_TYPE]));
            familymember.setEducationId(rs.getLong(PstFamilyMember.fieldNames[PstFamilyMember.FLD_EDUCATION_ID]));
            familymember.setReligionId(rs.getLong(PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELIGION_ID]));
            familymember.setSex(rs.getInt(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SEX]));
            familymember.setStatus(rs.getInt(PstFamilyMember.fieldNames[PstFamilyMember.FLD_STATUS]));
            familymember.setCompanyName(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_COMPANY_NAME]));
            familymember.setCompanyAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_COMPANY_ADDRESS]));
            familymember.setCompanyPhone(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_COMPANY_PHONE_NUMBER]));
            familymember.setCompanyPos(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_COMPANY_POSTAL_CODE]));
            //sekarang
            familymember.setPresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_PRESENT_ADDRESS]));
            familymember.setSubvillagePresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SUBVILLAGE_PRESENT_ADDRESS]));
            familymember.setVillagePresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_VILLAGE_PRESENT_ADDRESS]));
            familymember.setSubregencyPresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SUBDISTRIC_PRESENT_ADDRESS]));
            familymember.setRegencyPresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_DISTRIC_PRESENT_ADDRESS]));
            familymember.setProvincePresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_REGENCY_PRESENT_ADDRESS]));
            familymember.setPhonePresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_PHONE_NUMBER_PRESENT_ADDRESS]));
            familymember.setPostalcodePresentAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_POSTAL_CODE_PRESENT_ADDRESS]));
            //id card
            familymember.setIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_ID_CARD_ADDRESS]));
            familymember.setSubregencyIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SUBVILLAGE_ID_CARD_ADDRESS]));
            familymember.setVillageIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_VILLAGE_ID_CARD_ADDRESS]));
            familymember.setSubregencyIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SUBDISTRIC_ID_CARD_ADDRESS]));
            familymember.setRegencyIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_DISTRIC_ID_CARD_ADDRESS]));
            familymember.setProvinceIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_REGENCY_ID_CARD_ADDRESS]));
            familymember.setPhoneIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_PHONE_NUMBER_ID_CARD_ADDRESS]));
            familymember.setPostalcodeIdCardAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_POSTAL_CODE_ID_CARD_ADDRESS]));
            //since
            familymember.setSinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SINCE_ADDRESS]));
            familymember.setSubvillageSinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SUBVILLAGE_SINCE_ADDRESS]));
            familymember.setVillageSinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_VILLAGE_SINCE_ADDRESS]));
            familymember.setSubregencySinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_SUBDISTRIC_SINCE_ADDRESS]));
            familymember.setRegencySinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_DISTRIC_SINCE_ADDRESS]));
            familymember.setProvinceSinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_REGENCY_SINCE_ADDRESS]));
            familymember.setPhoneSinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_PHONE_NUMBER_SINCE_ADDRESS]));
            familymember.setPostalcodeSinceAddress(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_POSTAL_CODE_SINCE_ADDRESS]));
            familymember.setJobInstitutions(rs.getString(PstFamilyMember.fieldNames[PstFamilyMember.FLD_JOB_INSTITUTIONS]));
        } catch (Exception e) {
        }
    }

    public static boolean checkOID(long familyMemberId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_FAMILY_MEMBER + " WHERE "
                    + PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID] + " = " + familyMemberId;

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

    /**
     * Ari_20110930 Menambah checkFamRelation {
     *
     * @param whereClause
     * @return
     */
    public static boolean checkFamRelation(long famRelationId) {
        DBResultSet dbrs = null;
        boolean result = false;
        try {
            String sql = "SELECT * FROM " + TBL_HR_FAMILY_MEMBER + " WHERE "
                    + PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID] + " = '" + famRelationId + "'";

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
    /* } */

    public static int getCount(String whereClause) {
        DBResultSet dbrs = null;
        try {
            String sql = "SELECT COUNT(" + PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID] + ") FROM " + TBL_HR_FAMILY_MEMBER;
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

    //Gede_6Feb2012 {
    //untuk report excel 
    public static int getCount2(SrcEmployee srcEmployee) {
        DBResultSet dbrs = null;
        SessEmployee sessEmployee = new SessEmployee();

        try {
            String sql = "SELECT COUNT(FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID] + ") FROM " + PstFamilyMember.TBL_HR_FAMILY_MEMBER + " FM INNER JOIN "
                    + PstEmployee.TBL_HR_EMPLOYEE + " EMP ON FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID] + "=EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                    + (srcEmployee.getSalaryLevel().length() > 0
                            ? " LEFT JOIN  " + PstPayEmpLevel.TBL_PAY_EMP_LEVEL + " LEV " + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_ID]
                            + " = LEV." + PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_EMPLOYEE_ID]
                            : " " + " LEFT JOIN " + PstLevel.TBL_HR_LEVEL + " HR_LEV " + " ON EMP." + PstEmployee.fieldNames[PstEmployee.FLD_LEVEL_ID]
                            + " = HR_LEV." + PstLevel.fieldNames[PstLevel.FLD_LEVEL_ID]) + " WHERE " + sessEmployee.whereList(srcEmployee) + "GROUP BY FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID]
                    + " ORDER BY COUNT(FM." + PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID] + ") DESC LIMIT 1";

            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("sql1:"+sql);

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

    /* Gak jadi di pake
     //relationship 
     public static String getRelation(String getRelationship) {
     String relation = "";
     DBResultSet dbrs = null;
     String sql="";
     try {
     sql = "SELECT " + PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION] +
     " FROM " + PstFamRelation.TBL_HR_FAM_RELATION+ " WHERE " + PstFamRelation.fieldNames[PstFamRelation.FLD_FAMILY_RELATION_ID]
     +"=" +getRelationship;
     dbrs = DBHandler.execQueryResult(sql);
     ResultSet rs = dbrs.getResultSet();
     //System.out.println("sql1:"+sql);
     while (rs.next()) {
     relation = rs.getString(1);
     }

     rs.close();
     //return relation;
     } catch (Exception e) {
     System.out.println("Error");
     }
     finally {
     DBResultSet.close(dbrs);
     }
     return relation;
     }

     //}
     * 
     */
    //Gede_13Februari2012{
    //religion name
    public static String getReligion(String religi) {
        String religion = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstReligion.fieldNames[PstReligion.FLD_RELIGION]
                    + " FROM " + PstReligion.TBL_HR_RELIGION + " WHERE " + PstReligion.fieldNames[PstReligion.FLD_RELIGION_ID]
                    + "=" + religi;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("sql1:"+sql);
            while (rs.next()) {
                religion = rs.getString(1);
            }

            rs.close();
            //return relation;
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return religion;
    }

    //education name
    public static String getEducation(String edu) {
        String education = "";
        DBResultSet dbrs = null;
        String sql = "";
        try {
            sql = "SELECT " + PstEducation.fieldNames[PstEducation.FLD_EDUCATION]
                    + " FROM " + PstEducation.TBL_HR_EDUCATION + " WHERE " + PstEducation.fieldNames[PstEducation.FLD_EDUCATION_ID]
                    + "=" + edu;
            dbrs = DBHandler.execQueryResult(sql);
            ResultSet rs = dbrs.getResultSet();
            //System.out.println("sql1:"+sql);
            while (rs.next()) {
                education = rs.getString(1);
            }

            rs.close();
            //return relation;
        } catch (Exception e) {
            System.out.println("Error");
        } finally {
            DBResultSet.close(dbrs);
        }
        return education;
    }
    //}

    /* This method used to find current data */
    public static int findLimitStart(long oid, int recordToGet, String whereClause, String orderClause) {
        int size = getCount(whereClause);
        int start = 0;
        boolean found = false;
        for (int i = 0; (i < size) && !found; i = i + recordToGet) {
            Vector list = list(i, recordToGet, whereClause, orderClause);
            start = i;
            if (list.size() > 0) {
                for (int ls = 0; ls < list.size(); ls++) {
                    FamilyMember familymember = (FamilyMember) list.get(ls);
                    if (oid == familymember.getOID()) {
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

    public static long deleteByEmployee(long emplOID) {
        try {
            String sql = " DELETE FROM " + PstFamilyMember.TBL_HR_FAMILY_MEMBER
                    + " WHERE " + PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID]
                    + " = " + emplOID;

            int status = DBHandler.execUpdate(sql);
        } catch (Exception exc) {
            System.out.println("error delete fam member by employee " + exc.toString());
        }

        return emplOID;
    }
    
    public static long deleteByFamilyMember(long familyMOID) {
        try {
            String sql = " DELETE FROM " + PstFamilyMember.TBL_HR_FAMILY_MEMBER
                    + " WHERE " + PstFamilyMember.fieldNames[PstFamilyMember.FLD_FAMILY_MEMBER_ID]
                    + " = " + familyMOID;

            int status = DBHandler.execUpdate(sql);
        } catch (Exception exc) {
            System.out.println("error delete fam member by employee " + exc.toString());
        }

        return familyMOID;
    }

}
