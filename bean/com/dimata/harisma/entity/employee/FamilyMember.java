
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author  	: karya
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
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class FamilyMember extends Entity {

    private long employeeId;
    private String fullName = "";
    private String relationship = "";
    private String relationshipIndex = "";
    private int relationType = 0;
    private String relationTitle = "";
    //private String famRelation ="";
    private Date birthDate;
    private String bloodType = "";
    private String phoneNumber = "";
    private String educationDegree = "";
    private String job = "";
    private String companyName = "";
    private String companyAddress = "";
    private String companyPhone = "";
    private String companyPos = "";
    private String address = "";
    private boolean guaranteed = false;
    private boolean ignoreBirth;
    private long educationId = 0;
    private long religionId = 0;
    private int sex = 0;
    private int status = 0;
    private String sinceAddress = "";
    private String subvillageSinceAddress = "";
    private String villageSinceAddress = "";
    private String subregencySinceAddress = "";
    private String regencySinceAddress = "";
    private String provinceSinceAddress = "";
    private String phoneSinceAddress = "";
    private String postalcodeSinceAddress = "";
    private String idCardAddress = "";
    private String subvillageIdCardAddress = "";
    private String villageIdCardAddress = "";
    private String subregencyIdCardAddress = "";
    private String regencyIdCardAddress = "";
    private String provinceIdCardAddress = "";
    private String phoneIdCardAddress = "";
    private String postalcodeIdCardAddress = "";
    private String presentAddress = "";
    private String subvillagePresentAddress = "";
    private String villagePresentAddress = "";
    private String subregencyPresentAddress = "";
    private String regencyPresentAddress = "";
    private String provincePresentAddress = "";
    private String phonePresentAddress = "";
    private String postalcodePresentAddress = "";
    private String jobInstitutions = "";

    public String getRelationshipIndex() {
        if (relationshipIndex == null) {
            return "";
        }
        return relationshipIndex;

    }

    public void setRelationshipIndex(String relationshipIndex) {
        this.relationshipIndex = relationshipIndex == null ? "" : relationshipIndex;
    }

    public String getRelationTitle() {
        return relationTitle;
    }

    public void setRelationTitle(String relationTitle) {
        this.relationTitle = relationTitle == null ? "" : relationTitle;
    }

    public String getSubvillageSinceAddress() {
        return subvillageSinceAddress;
    }

    public void setSubvillageSinceAddress(String subvillageSinceAddress) {
        this.subvillageSinceAddress = subvillageSinceAddress == null ? "" : subvillageSinceAddress;
    }

    public String getSubvillageIdCardAddress() {
        return subvillageIdCardAddress;
    }

    public void setSubvillageIdCardAddress(String subvillageIdCardAddress) {
        this.subvillageIdCardAddress = subvillageIdCardAddress == null ? "" : subvillageIdCardAddress;
    }

    public String getSubvillagePresentAddress() {
        return subvillagePresentAddress;
    }

    public void setSubvillagePresentAddress(String subvillagePresentAddress) {
        this.subvillagePresentAddress = subvillagePresentAddress == null ? "" : subvillagePresentAddress;
    }

    public String getIdCardAddress() {
        return idCardAddress;
    }

    public void setIdCardAddress(String idCardAddress) {
        this.idCardAddress = idCardAddress == null ? "" : idCardAddress;
    }

    public String getVillageIdCardAddress() {
        return villageIdCardAddress;
    }

    public void setVillageIdCardAddress(String villageIdCardAddress) {
        this.villageIdCardAddress = villageIdCardAddress == null ? "" : villageIdCardAddress;
    }

    public String getSubregencyIdCardAddress() {
        return subregencyIdCardAddress;
    }

    public void setSubregencyIdCardAddress(String subregencyIdCardAddress) {
        this.subregencyIdCardAddress = subregencyIdCardAddress == null ? "" : subregencyIdCardAddress;
    }

    public String getRegencyIdCardAddress() {
        return regencyIdCardAddress;
    }

    public void setRegencyIdCardAddress(String regencyIdCardAddress) {
        this.regencyIdCardAddress = regencyIdCardAddress == null ? "" : regencyIdCardAddress;
    }

    public String getProvinceIdCardAddress() {
        return provinceIdCardAddress;
    }

    public void setProvinceIdCardAddress(String provinceIdCardAddress) {
        this.provinceIdCardAddress = provinceIdCardAddress == null ? "" : provinceIdCardAddress;
    }

    public String getPhoneIdCardAddress() {
        return phoneIdCardAddress;
    }

    public void setPhoneIdCardAddress(String phoneIdCardAddress) {
        this.phoneIdCardAddress = phoneIdCardAddress == null ? "" : phoneIdCardAddress;
    }

    public String getPostalcodeIdCardAddress() {
        return postalcodeIdCardAddress;
    }

    public void setPostalcodeIdCardAddress(String postalcodeIdCardAddress) {
        this.postalcodeIdCardAddress = postalcodeIdCardAddress == null ? "" : postalcodeIdCardAddress;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone == null ? "" : companyPhone;
    }

    public String getCompanyPos() {
        return companyPos;
    }

    public void setCompanyPos(String companyPos) {
        this.companyPos = companyPos == null ? "" : companyPos;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? "" : companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? "" : companyAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? "" : phoneNumber;
    }

    public String getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree == null ? "" : educationDegree;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType == null ? "" : bloodType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSinceAddress() {
        return sinceAddress;
    }

    public void setSinceAddress(String sinceAddress) {
        this.sinceAddress = sinceAddress == null ? "" : sinceAddress;
    }

    public String getVillageSinceAddress() {
        return villageSinceAddress;
    }

    public void setVillageSinceAddress(String villageSinceAddress) {
        this.villageSinceAddress = villageSinceAddress == null ? "" : villageSinceAddress;
    }

    public String getSubregencySinceAddress() {
        return subregencySinceAddress;
    }

    public void setSubregencySinceAddress(String subregencySinceAddress) {
        this.subregencySinceAddress = subregencySinceAddress == null ? "" : subregencySinceAddress;
    }

    public String getRegencySinceAddress() {
        return regencySinceAddress;
    }

    public void setRegencySinceAddress(String regencySinceAddress) {
        this.regencySinceAddress = regencySinceAddress == null ? "" : regencySinceAddress;
    }

    public String getProvinceSinceAddress() {
        return provinceSinceAddress;
    }

    public void setProvinceSinceAddress(String provinceSinceAddress) {
        this.provinceSinceAddress = provinceSinceAddress == null ? "" : provinceSinceAddress;
    }

    public String getPhoneSinceAddress() {
        return phoneSinceAddress;
    }

    public void setPhoneSinceAddress(String phoneSinceAddress) {
        this.phoneSinceAddress = phoneSinceAddress == null ? "" : phoneSinceAddress;
    }

    public String getPostalcodeSinceAddress() {
        return postalcodeSinceAddress;
    }

    public void setPostalcodeSinceAddress(String postalcodeSinceAddress) {
        this.postalcodeSinceAddress = postalcodeSinceAddress == null ? "" : postalcodeSinceAddress;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress == null ? "" : presentAddress;
    }

    public String getVillagePresentAddress() {
        return villagePresentAddress;
    }

    public void setVillagePresentAddress(String villagePresentAddress) {
        this.villagePresentAddress = villagePresentAddress == null ? "" : villagePresentAddress;
    }

    public String getSubregencyPresentAddress() {
        return subregencyPresentAddress;
    }

    public void setSubregencyPresentAddress(String subregencyPresentAddress) {
        this.subregencyPresentAddress = subregencyPresentAddress == null ? "" : subregencyPresentAddress;
    }

    public String getRegencyPresentAddress() {
        return regencyPresentAddress;
    }

    public void setRegencyPresentAddress(String regencyPresentAddress) {
        this.regencyPresentAddress = regencyPresentAddress == null ? "" : regencyPresentAddress;
    }

    public String getProvincePresentAddress() {
        return provincePresentAddress;
    }

    public void setProvincePresentAddress(String provincePresentAddress) {
        this.provincePresentAddress = provincePresentAddress == null ? "" : provincePresentAddress;
    }

    public String getPhonePresentAddress() {
        return phonePresentAddress;
    }

    public void setPhonePresentAddress(String phonePresentAddress) {
        this.phonePresentAddress = phonePresentAddress == null ? "" : phonePresentAddress;
    }

    public String getPostalcodePresentAddress() {
        return postalcodePresentAddress;
    }

    public void setPostalcodePresentAddress(String postalcodePresentAddress) {
        this.postalcodePresentAddress = postalcodePresentAddress == null ? "" : postalcodePresentAddress;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null) {
            fullName = "";
        }
        this.fullName = fullName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        if (job == null) {
            job = "";
        }
        this.job = job;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null) {
            address = "";
        }
        this.address = address;
    }

    public boolean getGuaranteed() {
        return guaranteed;
    }

    public void setGuaranteed(boolean guaranteed) {
        this.guaranteed = guaranteed;
    }

    public boolean getIgnoreBirth() {
        return ignoreBirth;
    }

    public void setIgnoreBirth(boolean ignoreBirth) {
        this.ignoreBirth = ignoreBirth;
    }

    /**
     * @return the relationship
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * @param relationship the relationship to set
     */
    public void setRelationship(String relationship) {
        this.relationship = relationship == null ? "" : relationship;
    }

    /**
     * @return the relationType
     */
    public int getRelationType() {
        return relationType;
    }

    /**
     * @param relationType the relationType to set
     */
    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }

    /**
     * @return the educationId
     */
    public long getEducationId() {
        return educationId;
    }

    /**
     * @param educationId the educationId to set
     */
    public void setEducationId(long educationId) {
        this.educationId = educationId;
    }

    /**
     * @return the religionId
     */
    public long getReligionId() {
        return religionId;
    }

    /**
     * @param religionId the religionId to set
     */
    public void setReligionId(long religionId) {
        this.religionId = religionId;
    }

    /**
     * @return the sex
     */
    public int getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(int sex) {
        this.sex = sex;
    }

    /**
     * @return the jobInstitutions
     */
    public String getJobInstitutions() {
        return jobInstitutions;
    }

    /**
     * @param jobInstitutions the jobInstitutions to set
     */
    public void setJobInstitutions(String jobInstitutions) {
        this.jobInstitutions = jobInstitutions == null ? "" : jobInstitutions;
    }

}
