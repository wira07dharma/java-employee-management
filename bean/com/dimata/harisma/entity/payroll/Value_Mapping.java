/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author GUSWIK
 */
public class Value_Mapping extends Entity{
    private long valuemappingid ;
    private String compCode="";
    private String parameter="";
    private int number_of_map;
    private Date startdate;
    private Date enddate;
    private long company_id;
    private long division_id;
    private long department_id;
    private long section_id;
    private long level_id;
    private long marital_id;
    private float length_of_service;
    private long employee_category;
    private long position_id;
    private long employee_id;
    private long addrCountryId=0;
    private long addrProvinceId=0;
    private long addrRegencyId=0;
    private long addrSubRegencyId=0;     
    private double value;
    private String geoAddressPmnt="";
    private long grade= 0;
    private int sex= -1;
    private int status= 0;
    private long religion= 0;
    
    private int losFromInDay   = 0;
    private int losFromInMonth = 0;
    private int losFromInYear  = 0;
    private int losToInDay     = 0;
    private int losToInMonth   = 0;
    private int losToInYear    = 0;
    private int resignStatus    = 0;
    
    
    private int losCurrentDate = 0;
    private Date losPerCurrentDate ;
	private String remark = "";
    /**
     * @return the compCode
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * @param compCode the compCode to set
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

   
    /**
     * @return the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the value
     */
    public double getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(double value) {
        this.value = value;
    }

    /**
     * @return the number_of_map
     */
    public int getNumber_of_map() {
        return number_of_map;
    }

    /**
     * @param number_of_map the number_of_map to set
     */
    public void setNumber_of_map(int number_of_map) {
        this.number_of_map = number_of_map;
    }

    /**
     * @return the startdate
     */
    public Date getStartdate() {
        return startdate;
    }

    /**
     * @param startdate the startdate to set
     */
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    /**
     * @return the enddate
     */
    public Date getEnddate() {
        return enddate;
    }

    /**
     * @param enddate the enddate to set
     */
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    /**
     * @return the company_id
     */
    public long getCompany_id() {
        return company_id;
    }

    /**
     * @param company_id the company_id to set
     */
    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    /**
     * @return the division_id
     */
    public long getDivision_id() {
        return division_id;
    }

    /**
     * @param division_id the division_id to set
     */
    public void setDivision_id(long division_id) {
        this.division_id = division_id;
    }

    /**
     * @return the department_id
     */
    public long getDepartment_id() {
        return department_id;
    }

    /**
     * @param department_id the department_id to set
     */
    public void setDepartment_id(long department_id) {
        this.department_id = department_id;
    }

    /**
     * @return the section_id
     */
    public long getSection_id() {
        return section_id;
    }

    /**
     * @param section_id the section_id to set
     */
    public void setSection_id(long section_id) {
        this.section_id = section_id;
    }

    /**
     * @return the level_id
     */
    public long getLevel_id() {
        return level_id;
    }

    /**
     * @param level_id the level_id to set
     */
    public void setLevel_id(long level_id) {
        this.level_id = level_id;
    }

    /**
     * @return the marital_id
     */
    public long getMarital_id() {
        return marital_id;
    }

    /**
     * @param marital_id the marital_id to set
     */
    public void setMarital_id(long marital_id) {
        this.marital_id = marital_id;
    }


    /**
     * @return the employee_category
     */
    public long getEmployee_category() {
        return employee_category;
    }

    /**
     * @param employee_category the employee_category to set
     */
    public void setEmployee_category(long employee_category) {
        this.employee_category = employee_category;
    }



    /**
     * @return the addrCountryId
     */
    public long getAddrCountryId() {
        return addrCountryId;
    }

    /**
     * @param addrCountryId the addrCountryId to set
     */
    public void setAddrCountryId(long addrCountryId) {
        this.addrCountryId = addrCountryId;
    }

    /**
     * @return the addrProvinceId
     */
    public long getAddrProvinceId() {
        return addrProvinceId;
    }

    /**
     * @param addrProvinceId the addrProvinceId to set
     */
    public void setAddrProvinceId(long addrProvinceId) {
        this.addrProvinceId = addrProvinceId;
    }

    /**
     * @return the addrRegencyId
     */
    public long getAddrRegencyId() {
        return addrRegencyId;
    }

    /**
     * @param addrRegencyId the addrRegencyId to set
     */
    public void setAddrRegencyId(long addrRegencyId) {
        this.addrRegencyId = addrRegencyId;
    }

    /**
     * @return the addrSubRegencyId
     */
    public long getAddrSubRegencyId() {
        return addrSubRegencyId;
    }

    /**
     * @param addrSubRegencyId the addrSubRegencyId to set
     */
    public void setAddrSubRegencyId(long addrSubRegencyId) {
        this.addrSubRegencyId = addrSubRegencyId;
    }

    /**
     * @param length_of_service the length_of_service to set
     */
    public void setLength_of_service(float length_of_service) {
        this.length_of_service = length_of_service;
    }

    /**
     * @return the length_of_service
     */
    public float getLength_of_service() {
        return length_of_service;
    }

    /**
     * @return the position_id
     */
    public long getPosition_id() {
        return position_id;
    }

    /**
     * @param position_id the position_id to set
     */
    public void setPosition_id(long position_id) {
        this.position_id = position_id;
    }

    /**
     * @return the geoAddressPmnt
     */
    public String getGeoAddressPmnt() {
        return geoAddressPmnt;
    }

    /**
     * @param geoAddressPmnt the geoAddressPmnt to set
     */
    public void setGeoAddressPmnt(String geoAddressPmnt) {
        this.geoAddressPmnt = geoAddressPmnt;
    }

    /**
     * @return the valuemappingid
     */
    public long getValuemappingid() {
        return valuemappingid;
    }

    /**
     * @param valuemappingid the valuemappingid to set
     */
    public void setValuemappingid(long valuemappingid) {
        this.valuemappingid = valuemappingid;
    }

    /**
     * @return the employee_id
     */
    public long getEmployee_id() {
        return employee_id;
    }

    /**
     * @param employee_id the employee_id to set
     */
    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
    }

    /**
     * @return the grade
     */
    public long getGrade() {
        return grade;
    }

    /**
     * @param grade the grade to set
     */
    public void setGrade(long grade) {
        this.grade = grade;
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
     * @return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the religion
     */
    public long getReligion() {
        return religion;
    }

    /**
     * @param religion the religion to set
     */
    public void setReligion(long religion) {
        this.religion = religion;
    }

    /**
     * @return the losFromInDay
     */
    public int getLosFromInDay() {
        return losFromInDay;
    }

    /**
     * @param losFromInDay the losFromInDay to set
     */
    public void setLosFromInDay(int losFromInDay) {
        this.losFromInDay = losFromInDay;
    }

    /**
     * @return the losFromInMonth
     */
    public int getLosFromInMonth() {
        return losFromInMonth;
    }

    /**
     * @param losFromInMonth the losFromInMonth to set
     */
    public void setLosFromInMonth(int losFromInMonth) {
        this.losFromInMonth = losFromInMonth;
    }

    /**
     * @return the losFromInYear
     */
    public int getLosFromInYear() {
        return losFromInYear;
    }

    /**
     * @param losFromInYear the losFromInYear to set
     */
    public void setLosFromInYear(int losFromInYear) {
        this.losFromInYear = losFromInYear;
    }

    /**
     * @return the losToInDay
     */
    public int getLosToInDay() {
        return losToInDay;
    }

    /**
     * @param losToInDay the losToInDay to set
     */
    public void setLosToInDay(int losToInDay) {
        this.losToInDay = losToInDay;
    }

    /**
     * @return the losToInMonth
     */
    public int getLosToInMonth() {
        return losToInMonth;
    }

    /**
     * @param losToInMonth the losToInMonth to set
     */
    public void setLosToInMonth(int losToInMonth) {
        this.losToInMonth = losToInMonth;
    }

    /**
     * @return the losToInYear
     */
    public int getLosToInYear() {
        return losToInYear;
    }

    /**
     * @param losToInYear the losToInYear to set
     */
    public void setLosToInYear(int losToInYear) {
        this.losToInYear = losToInYear;
    }

    /**
     * @return the losCurrentDate
     */
    public int getLosCurrentDate() {
        return losCurrentDate;
    }

    /**
     * @param losCurrentDate the losCurrentDate to set
     */
    public void setLosCurrentDate(int losCurrentDate) {
        this.losCurrentDate = losCurrentDate;
    }

    /**
     * @return the losPerCurrentDate
     */
    public Date getLosPerCurrentDate() {
        return losPerCurrentDate;
    }

    /**
     * @param losPerCurrentDate the losPerCurrentDate to set
     */
    public void setLosPerCurrentDate(Date losPerCurrentDate) {
        this.losPerCurrentDate = losPerCurrentDate;
    }

    /**
     * @return the resignStatus
     */
    public int getResignStatus() {
        return resignStatus;
    }

    /**
     * @param resignStatus the resignStatus to set
     */
    public void setResignStatus(int resignStatus) {
        this.resignStatus = resignStatus;
    }

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}


   
}
