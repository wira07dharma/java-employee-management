/* 
 * Form Name  	:  FrmFamilyMember.java 
 * Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya 
 * @version  	: 01 
 */
/**
 * *****************************************************************
 * Class Description : [project description ... ] Imput Parameters : [input
 * parameter ...] Output : [output ...] 
 ******************************************************************
 */
package com.dimata.harisma.form.employee;

/* java package */
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
/* qdep package */
import com.dimata.qdep.form.*;
/* project package */
import com.dimata.harisma.entity.employee.*;

public class FrmFamilyMember extends FRMHandler implements I_FRMInterface, I_FRMType {

    private FamilyMember familyMember;

    public static final String FRM_NAME_FAMILYMEMBER = "FRM_NAME_FAMILYMEMBER";

    public static final int FRM_FIELD_FAMILY_MEMBER_ID = 0;
    public static final int FRM_FIELD_FULL_NAME = 1;
    public static final int FRM_FIELD_RELATIONSHIP = 2;
    public static final int FRM_FIELD_BIRTH_DATE = 3;
    public static final int FRM_FIELD_JOB = 4;
    public static final int FRM_FIELD_ADDRESS = 5;
    public static final int FRM_FIELD_GUARANTEED = 6;
    public static final int FRM_FIELD_IGNORE_BIRTH = 7;
    public static final int FRM_FIELD_EDUCATION_ID = 8;
    public static final int FRM_FIELD_RELIGION_ID = 9;
    public static final int FRM_FIELD_SEX = 10;
    public static final int FRM_FIELD_STATUS = 11;
    public static final int FRM_FIELD_BLOOD_TYPE = 12;
    public static final int FRM_FIELD_EDUCATION_DEGREE = 13;
    public static final int FRM_FIELD_PHONE_NUMBER = 14;
    public static final int FRM_FIELD_COMPANY_NAME = 15;
    public static final int FRM_FIELD_COMPANY_ADDRESS = 16;
    public static final int FRM_FIELD_COMPANY_PHONE_NUMBER = 17;
    public static final int FRM_FIELD_COMPANY_POSTAL_CODE = 18;
    public static final int FRM_FIELD_PRESENT_ADDRESS = 19;
    public static final int FRM_FIELD_SUBVILLAGE_PRESENT_ADDRESS = 20;
    public static final int FRM_FIELD_VILLAGE_PRESENT_ADDRESS = 21;
    public static final int FRM_FIELD_SUBDISTRIC_PRESENT_ADDRESS = 22;
    public static final int FRM_FIELD_DISTRIC_PRESENT_ADDRESS = 23;
    public static final int FRM_FIELD_REGENCY_PRESENT_ADDRESS = 24;
    public static final int FRM_FIELD_PHONE_NUMBER_PRESENT_ADDRESS = 25;
    public static final int FRM_FIELD_POSTAL_CODE_PRESENT_ADDRESS = 26;
    public static final int FRM_FIELD_ID_CARD_ADDRESS = 27;
    public static final int FRM_FIELD_SUBVILLAGE_ID_CARD_ADDRESS = 28;
    public static final int FRM_FIELD_VILLAGE_ID_CARD_ADDRESS = 29;
    public static final int FRM_FIELD_SUBDISTRIC_ID_CARD_ADDRESS = 30;
    public static final int FRM_FIELD_DISTRIC_ID_CARD_ADDRESS = 31;
    public static final int FRM_FIELD_REGENCY_ID_CARD_ADDRESS = 32;
    public static final int FRM_FIELD_PHONE_NUMBER_ID_CARD_ADDRESS = 33;
    public static final int FRM_FIELD_POSTAL_CODE_ID_CARD_ADDRESS = 34;
    public static final int FRM_FIELD_SINCE_ADDRESS = 35;
    public static final int FRM_FIELD_SUBVILLAGE_SINCE_ADDRESS = 36;
    public static final int FRM_FIELD_VILLAGE_SINCE_ADDRESS = 37;
    public static final int FRM_FIELD_SUBDISTRIC_SINCE_ADDRESS = 38;
    public static final int FRM_FIELD_DISTRIC_SINCE_ADDRESS = 39;
    public static final int FRM_FIELD_REGENCY_SINCE_ADDRESS = 40;
    public static final int FRM_FIELD_PHONE_NUMBER_SINCE_ADDRESS = 41;
    public static final int FRM_FIELD_POSTAL_CODE_SINCE_ADDRESS = 42;
    public static final int FRM_FIELD_RELATION_TITLE = 43;
    public static final int FRM_FIELD_RELATIONSHIP_INDEX = 44;
    public static final int FRM_FIELD_JOB_INSTITUTIONS = 45;

    public static String[] fieldNames = {
        "FRM_FIELD_FAMILY_MEMBER_ID",
        "FRM_FIELD_FULL_NAME",
        "FRM_FIELD_RELATIONSHIP",
        "FRM_FIELD_BIRTH_DATE",
        "FRM_FIELD_JOB",
        "FRM_FIELD_ADDRESS",
        "FRM_FIELD_GUARANTEED",
        "FRM_FIELD_IGNORE_BIRTH",
        "FRM_FIELD_EDUCATION_ID",
        "FRM_FIELD_RELIGION_ID",
        "FRM_FIELD_SEX",
        "FRM_FIELD_STATUS",
        "FRM_FIELD_BLOOD_TYPE",
        "FRM_FIELD_EDUCATION_DEGREE",
        "FRM_FIELD_PHONE_NUMBER",
        "FRM_FIELD_COMPANY_NAME",
        "FRM_FIELD_COMPANY_ADDRESS",
        "FRM_FIELD_COMPANY_PHONE_NUMBER",
        "FRM_FIELD_COMPANY_POSTAL_CODE",
        "FRM_FIELD_PRESENT_ADDRESS",
        "FRM_FIELD_SUBVILLAGE_PRESENT_ADDRESS",
        "FRM_FIELD_VILLAGE_PRESENT_ADDRESS",
        "FRM_FIELD_SUBDISTRIC_PRESENT_ADDRESS",
        "FRM_FIELD_DISTRIC_PRESENT_ADDRESS",
        "FRM_FIELD_REGENCY_PRESENT_ADDRESS",
        "FRM_FIELD_PHONE_NUMBER_PRESENT_ADDRESS",
        "FRM_FIELD_POSTAL_CODE_PRESENT_ADDRESS",
        "FRM_FIELD_ID_CARD_ADDRESS",
        "FRM_FIELD_SUBVILLAGE_ID_CARD_ADDRESS",
        "FRM_FIELD_VILLAGE_ID_CARD_ADDRESS",
        "FRM_FIELD_SUBDISTRIC_ID_CARD_ADDRESS",
        "FRM_FIELD_DISTRIC_ID_CARD_ADDRESS",
        "FRM_FIELD_REGENCY_ID_CARD_ADDRESS",
        "FRM_FIELD_PHONE_NUMBER_ID_CARD_ADDRESS",
        "FRM_FIELD_POSTAL_CODE_ID_CARD_ADDRESS",
        "FRM_FIELD_SINCE_ADDRESS",
        "FRM_FIELD_SUBVILLAGE_SINCE_ADDRESS",
        "FRM_FIELD_VILLAGE_SINCE_ADDRESS",
        "FRM_FIELD_SUBDISTRIC_SINCE_ADDRESS",
        "FRM_FIELD_DISTRIC_SINCE_ADDRESS",
        "FRM_FIELD_REGENCY_SINCE_ADDRESS",
        "FRM_FIELD_PHONE_NUMBER_SINCE_ADDRESS",
        "FRM_FIELD_POSTAL_CODE_SINCE_ADDRESS",
        "FRM_FIELD_RELATION_TITLE",
        "FRM_FIELD_RELATIONSHIP_INDEX",
        "FRM_FIELD_JOB_INSTITUTIONS"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_STRING + ENTRY_REQUIRED,
        TYPE_DATE,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_BOOL,
        TYPE_BOOL,
        TYPE_LONG,
        TYPE_LONG,
        TYPE_INT,
        TYPE_INT,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING,
        TYPE_STRING
    };

    public FrmFamilyMember() {
    }

    public FrmFamilyMember(FamilyMember familyMember) {
        this.familyMember = familyMember;
    }

    public FrmFamilyMember(HttpServletRequest request, FamilyMember familyMember) {
        super(new FrmFamilyMember(familyMember), request);
        this.familyMember = familyMember;
    }

    public String getFormName() {
        return FRM_NAME_FAMILYMEMBER;
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

    public FamilyMember getEntityObject() {
        return familyMember;
    }

    public void requestEntityObject(FamilyMember familyMember) {
        try {
            this.requestParam();
            familyMember.setFullName(getString(FRM_FIELD_FULL_NAME));
            familyMember.setRelationship(getString(FRM_FIELD_RELATIONSHIP));
            familyMember.setRelationshipIndex(getString(FRM_FIELD_RELATIONSHIP_INDEX));
            familyMember.setBirthDate(getDate(FRM_FIELD_BIRTH_DATE));
            familyMember.setBloodType(getString(FRM_FIELD_BLOOD_TYPE));
            familyMember.setEducationDegree(getString(FRM_FIELD_EDUCATION_DEGREE));
            familyMember.setPhoneNumber(getString(FRM_FIELD_PHONE_NUMBER));
            familyMember.setJob(getString(FRM_FIELD_JOB));
            familyMember.setAddress(getString(FRM_FIELD_ADDRESS));
            familyMember.setGuaranteed(getBoolean(FRM_FIELD_GUARANTEED));
            familyMember.setIgnoreBirth(getBoolean(FRM_FIELD_IGNORE_BIRTH));
            familyMember.setEducationId(getLong(FRM_FIELD_EDUCATION_ID));
            familyMember.setReligionId(getLong(FRM_FIELD_RELIGION_ID));
            familyMember.setSex(getInt(FRM_FIELD_SEX));
            familyMember.setStatus(getInt(FRM_FIELD_STATUS));
            familyMember.setCompanyName(getString(FRM_FIELD_COMPANY_NAME));
            familyMember.setCompanyAddress(getString(FRM_FIELD_COMPANY_ADDRESS));
            familyMember.setCompanyPhone(getString(FRM_FIELD_COMPANY_PHONE_NUMBER));
            familyMember.setCompanyPos(getString(FRM_FIELD_COMPANY_POSTAL_CODE));
            familyMember.setPresentAddress(getString(FRM_FIELD_PRESENT_ADDRESS));
            familyMember.setSubvillagePresentAddress(getString(FRM_FIELD_SUBVILLAGE_PRESENT_ADDRESS));
            familyMember.setVillagePresentAddress(getString(FRM_FIELD_VILLAGE_PRESENT_ADDRESS));
            familyMember.setSubregencyPresentAddress(getString(FRM_FIELD_SUBDISTRIC_PRESENT_ADDRESS));
            familyMember.setRegencyPresentAddress(getString(FRM_FIELD_DISTRIC_PRESENT_ADDRESS));
            familyMember.setProvincePresentAddress(getString(FRM_FIELD_REGENCY_PRESENT_ADDRESS));
            familyMember.setPhonePresentAddress(getString(FRM_FIELD_PHONE_NUMBER_PRESENT_ADDRESS));
            familyMember.setPostalcodePresentAddress(getString(FRM_FIELD_POSTAL_CODE_PRESENT_ADDRESS));
            familyMember.setIdCardAddress(getString(FRM_FIELD_ID_CARD_ADDRESS));
            familyMember.setVillageIdCardAddress(getString(FRM_FIELD_SUBVILLAGE_ID_CARD_ADDRESS));
            familyMember.setVillageIdCardAddress(getString(FRM_FIELD_VILLAGE_ID_CARD_ADDRESS));
            familyMember.setSubregencyIdCardAddress(getString(FRM_FIELD_SUBDISTRIC_ID_CARD_ADDRESS));
            familyMember.setRegencyIdCardAddress(getString(FRM_FIELD_DISTRIC_ID_CARD_ADDRESS));
            familyMember.setProvinceIdCardAddress(getString(FRM_FIELD_REGENCY_ID_CARD_ADDRESS));
            familyMember.setPhoneIdCardAddress(getString(FRM_FIELD_PHONE_NUMBER_ID_CARD_ADDRESS));
            familyMember.setPostalcodeIdCardAddress(getString(FRM_FIELD_POSTAL_CODE_ID_CARD_ADDRESS));
            familyMember.setSinceAddress(getString(FRM_FIELD_SINCE_ADDRESS));
            familyMember.setSubvillageSinceAddress(getString(FRM_FIELD_SUBVILLAGE_SINCE_ADDRESS));
            familyMember.setVillageSinceAddress(getString(FRM_FIELD_VILLAGE_SINCE_ADDRESS));
            familyMember.setSubregencySinceAddress(getString(FRM_FIELD_SUBDISTRIC_SINCE_ADDRESS));
            familyMember.setRegencySinceAddress(getString(FRM_FIELD_DISTRIC_SINCE_ADDRESS));
            familyMember.setProvinceSinceAddress(getString(FRM_FIELD_REGENCY_SINCE_ADDRESS));
            familyMember.setPhoneSinceAddress(getString(FRM_FIELD_PHONE_NUMBER_SINCE_ADDRESS));
            familyMember.setPostalcodeSinceAddress(getString(FRM_FIELD_POSTAL_CODE_SINCE_ADDRESS));
            familyMember.setJobInstitutions(getString(FRM_FIELD_JOB_INSTITUTIONS));
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }
}
