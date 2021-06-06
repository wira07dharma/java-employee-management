
/* Created on 	:  [date] [time] AM/PM 
 * 
 * @author	 :
 * @version	 :
 */

/*******************************************************************
 * Class Description 	: [project description ... ] 
 * Imput Parameters 	: [input parameter ...] 
 * Output 		: [output ...] 
 *******************************************************************/

package com.dimata.harisma.entity.search; 
 
/* package java */ 
import java.util.*;

public class SrcSpecialEmployee{ 
	private long department = 0;
	private long position = 0;
	private long section = 0;
	private long level = 0;
        /* Update by Hendra Putu | 2015-11-25 | GradeLevel*/
        private long gradeLevel = 0;
        
	private Date commdatefrom;
	private Date commdateto;
	private int workyearfrom = 0;
	private int workmonthfrom = 0;
	private int workyearto = 0;
	private int workmonthto = 0;
	private int ageyearfrom = 0;
	private int agemonthfrom = 0;
	private int ageyearto = 0;
	private int agemonthto = 0;
	private long education = 0;
	private long religion = 0;
	private long marital = 0;
	private int blood = 0;
	private long language = 0;
	private int sex = 0;
	private int resigned = 0;
	private int sortby = 0;
        private long race = 0;
        private long companyId=0;
        private long empCategoryId=0;
        
        private Date dtBirthFrom;
        private Date dtBirthTo;
        private long provinsiId;
        private long kabupatenId;
        private long kecamatanId;
        private String empAddress;
        private String empPermanentAddress;
        private int birthMonth;
        
        private long divisionId=0;
        
        private long countryId;
        private long countryIdPermanent;
        private long provinsiIdPermanent;
        private long kabupatenIdPermanent;
        private long kecamatanIdPermanent;
        
        
        private long districtId;
        private long districtIdPermanent;
        
        private int birthYearFrom;
        private int birthYearTo;
        
        //update by satrya 2013-10-16
        private Date dtPeriodStart;
        private Date dtPeriodEnd;
        
        //update by satrya 2013-11-13
        private Date dtCarrierWorkStart;
        private Date dtCarrierWorkEnd;
        private long carrierCategoryEmp;
        
        
        //update by priska 2014-10-30
        private Date endcontractfrom;
	private Date endcontractto;
        
        
        public static final int ORDER_EMPLOYEE_NAME	= 0;
        public static final int ORDER_DEPARTMENT	= 1;
        public static final int ORDER_EMPLOYEE_NUM	= 2;
        public static final int ORDER_COMM_DATE		= 3;

        public static final int[] orderValue = { 0,1,2,3 };

        public static final String[] orderKey  = {"Name", "Department","Employee Number","Commencing Date"};

        public static Vector getOrderValue() {
            Vector order = new Vector();
            for(int i=0;i<orderValue.length;i++) {
                order.add(String.valueOf(orderValue[i]));
            }
            return order;
        }

        public static Vector getOrderKey() {
            Vector order = new Vector();
            for(int i=0;i<orderKey.length;i++) {
                order.add(orderKey[i]);
            }
            return order;
        }

	public long getDepartment(){ 
		return department; 
	} 

	public void setDepartment(long department){ 
		this.department = department; 
	} 

	public long getPosition(){ 
		return position; 
	} 

	public void setPosition(long position){ 
		this.position = position; 
	} 

	public long getSection(){ 
		return section; 
	} 

	public void setSection(long section){ 
		this.section = section; 
	} 

	public long getLevel(){ 
		return level; 
	} 

	public void setLevel(long level){ 
		this.level = level; 
	} 

	public Date getCommdatefrom(){ 
		return commdatefrom; 
	} 

	public void setCommdatefrom(Date commdatefrom){ 
		this.commdatefrom = commdatefrom; 
	} 

	public Date getCommdateto(){ 
		return commdateto; 
	} 

	public void setCommdateto(Date commdateto){ 
		this.commdateto = commdateto; 
	} 

	public int getWorkyearfrom(){ 
		return workyearfrom; 
	} 

	public void setWorkyearfrom(int workyearfrom){ 
		this.workyearfrom = workyearfrom; 
	} 

	public int getWorkmonthfrom(){ 
		return workmonthfrom; 
	} 

	public void setWorkmonthfrom(int workmonthfrom){ 
		this.workmonthfrom = workmonthfrom; 
	} 

	public int getWorkyearto(){ 
		return workyearto; 
	} 

	public void setWorkyearto(int workyearto){ 
		this.workyearto = workyearto; 
	} 

	public int getWorkmonthto(){ 
		return workmonthto; 
	} 

	public void setWorkmonthto(int workmonthto){ 
		this.workmonthto = workmonthto; 
	} 

	public int getAgeyearfrom(){ 
		return ageyearfrom; 
	} 

	public void setAgeyearfrom(int ageyearfrom){ 
		this.ageyearfrom = ageyearfrom; 
	} 

	public int getAgemonthfrom(){ 
		return agemonthfrom; 
	} 

	public void setAgemonthfrom(int agemonthfrom){ 
		this.agemonthfrom = agemonthfrom; 
	} 

	public int getAgeyearto(){ 
		return ageyearto; 
	} 

	public void setAgeyearto(int ageyearto){ 
		this.ageyearto = ageyearto; 
	} 

	public int getAgemonthto(){ 
		return agemonthto; 
	} 

	public void setAgemonthto(int agemonthto){ 
		this.agemonthto = agemonthto; 
	} 

	public long getEducation(){ 
		return education; 
	} 

	public void setEducation(long education){ 
		this.education = education; 
	} 

	public long getReligion(){ 
		return religion; 
	} 

	public void setReligion(long religion){ 
		this.religion = religion; 
	} 

	public long getMarital(){ 
		return marital; 
	} 

	public void setMarital(long marital){ 
		this.marital = marital; 
	} 

	public int getBlood(){ 
		return blood; 
	} 

	public void setBlood(int blood){ 
		this.blood = blood; 
	} 

	public long getLanguage(){ 
		return language; 
	} 

	public void setLanguage(long language){ 
		this.language = language; 
	} 

	public int getSex(){ 
		return sex; 
	} 

	public void setSex(int sex){ 
		this.sex = sex; 
	} 

	public int getResigned(){ 
		return resigned; 
	} 

	public void setResigned(int resigned){ 
		this.resigned = resigned; 
	} 

	public int getSortby(){ 
		return sortby; 
	} 

	public void setSortby(int sortby){ 
		this.sortby = sortby; 
	} 
        
        public long getRace() {
            return race;
        }

        public void setRace(long race) {
            this.race = race;
        }

    /**
     * @return the companyId
     */
    public long getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the empCategoryId
     */
    public long getEmpCategoryId() {
        return empCategoryId;
    }

    /**
     * @param empCategoryId the empCategoryId to set
     */
    public void setEmpCategoryId(long empCategoryId) {
        this.empCategoryId = empCategoryId;
    }

    /**
     * @return the divisionId
     */
    public long getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId the divisionId to set
     */
    public void setDivisionId(long divisionId) {
        this.divisionId = divisionId;
    }

    /**
     * @return the dtBirthFrom
     */
    public Date getDtBirthFrom() {
        return dtBirthFrom;
    }

    /**
     * @param dtBirthFrom the dtBirthFrom to set
     */
    public void setDtBirthFrom(Date dtBirthFrom) {
        this.dtBirthFrom = dtBirthFrom;
    }

    /**
     * @return the dtBirthTo
     */
    public Date getDtBirthTo() {
        return dtBirthTo;
    }

    /**
     * @param dtBirthTo the dtBirthTo to set
     */
    public void setDtBirthTo(Date dtBirthTo) {
        this.dtBirthTo = dtBirthTo;
    }

    /**
     * @return the provinsiId
     */
    public long getProvinsiId() {
        return provinsiId;
    }

    /**
     * @param provinsiId the provinsiId to set
     */
    public void setProvinsiId(long provinsiId) {
        this.provinsiId = provinsiId;
    }

    /**
     * @return the kabupatenId
     */
    public long getKabupatenId() {
        return kabupatenId;
    }

    /**
     * @param kabupatenId the kabupatenId to set
     */
    public void setKabupatenId(long kabupatenId) {
        this.kabupatenId = kabupatenId;
    }

    /**
     * @return the kecamatanId
     */
    public long getKecamatanId() {
        return kecamatanId;
    }

    /**
     * @param kecamatanId the kecamatanId to set
     */
    public void setKecamatanId(long kecamatanId) {
        this.kecamatanId = kecamatanId;
    }

    /**
     * @return the empAddress
     */
    public String getEmpAddress() {
        return empAddress;
    }

    /**
     * @param empAddress the empAddress to set
     */
    public void setEmpAddress(String empAddress) {
        this.empAddress = empAddress;
    }

    /**
     * @return the empPermanentAddress
     */
    public String getEmpPermanentAddress() {
        return empPermanentAddress;
    }

    /**
     * @param empPermanentAddress the empPermanentAddress to set
     */
    public void setEmpPermanentAddress(String empPermanentAddress) {
        this.empPermanentAddress = empPermanentAddress;
    }

    /**
     * @return the countryId
     */
    public long getCountryId() {
        return countryId;
    }

    /**
     * @param countryId the countryId to set
     */
    public void setCountryId(long countryId) {
        this.countryId = countryId;
    }

    /**
     * @return the countryIdPermanent
     */
    public long getCountryIdPermanent() {
        return countryIdPermanent;
    }

    /**
     * @param countryIdPermanent the countryIdPermanent to set
     */
    public void setCountryIdPermanent(long countryIdPermanent) {
        this.countryIdPermanent = countryIdPermanent;
    }

    /**
     * @return the provinsiIdPermanent
     */
    public long getProvinsiIdPermanent() {
        return provinsiIdPermanent;
    }

    /**
     * @param provinsiIdPermanent the provinsiIdPermanent to set
     */
    public void setProvinsiIdPermanent(long provinsiIdPermanent) {
        this.provinsiIdPermanent = provinsiIdPermanent;
    }

    /**
     * @return the kabupatenIdPermanent
     */
    public long getKabupatenIdPermanent() {
        return kabupatenIdPermanent;
    }

    /**
     * @param kabupatenIdPermanent the kabupatenIdPermanent to set
     */
    public void setKabupatenIdPermanent(long kabupatenIdPermanent) {
        this.kabupatenIdPermanent = kabupatenIdPermanent;
    }

    /**
     * @return the kecamatanIdPermanent
     */
    public long getKecamatanIdPermanent() {
        return kecamatanIdPermanent;
    }

    /**
     * @param kecamatanIdPermanent the kecamatanIdPermanent to set
     */
    public void setKecamatanIdPermanent(long kecamatanIdPermanent) {
        this.kecamatanIdPermanent = kecamatanIdPermanent;
    }

    /**
     * @return the birthMonth
     */
    public int getBirthMonth() {
        return birthMonth;
    }

    /**
     * @param birthMonth the birthMonth to set
     */
    public void setBirthMonth(int birthMonth) {
        this.birthMonth = birthMonth;
    }

    /**
     * @return the birthYearFrom
     */
    public int getBirthYearFrom() {
        return birthYearFrom;
    }

    /**
     * @param birthYearFrom the birthYearFrom to set
     */
    public void setBirthYearFrom(int birthYearFrom) {
        this.birthYearFrom = birthYearFrom;
    }

    /**
     * @return the birthYearTo
     */
    public int getBirthYearTo() {
        return birthYearTo;
    }

    /**
     * @param birthYearTo the birthYearTo to set
     */
    public void setBirthYearTo(int birthYearTo) {
        this.birthYearTo = birthYearTo;
    }

    /**
     * @return the dtPeriodStart
     */
    public Date getDtPeriodStart() {
        return dtPeriodStart;
    }

    /**
     * @param dtPeriodStart the dtPeriodStart to set
     */
    public void setDtPeriodStart(Date dtPeriodStart) {
        this.dtPeriodStart = dtPeriodStart;
    }

    /**
     * @return the dtPeriodEnd
     */
    public Date getDtPeriodEnd() {
        return dtPeriodEnd;
    }

    /**
     * @param dtPeriodEnd the dtPeriodEnd to set
     */
    public void setDtPeriodEnd(Date dtPeriodEnd) {
        this.dtPeriodEnd = dtPeriodEnd;
    }

    /**
     * @return the dtCarrierWorkStart
     */
    public Date getDtCarrierWorkStart() {
        return dtCarrierWorkStart;
    }

    /**
     * @param dtCarrierWorkStart the dtCarrierWorkStart to set
     */
    public void setDtCarrierWorkStart(Date dtCarrierWorkStart) {
        this.dtCarrierWorkStart = dtCarrierWorkStart;
    }

    /**
     * @return the dtCarrierWorkEnd
     */
    public Date getDtCarrierWorkEnd() {
        return dtCarrierWorkEnd;
    }

    /**
     * @param dtCarrierWorkEnd the dtCarrierWorkEnd to set
     */
    public void setDtCarrierWorkEnd(Date dtCarrierWorkEnd) {
        this.dtCarrierWorkEnd = dtCarrierWorkEnd;
    }

    /**
     * @return the carrierCategoryEmp
     */
    public long getCarrierCategoryEmp() {
        return carrierCategoryEmp;
    }

    /**
     * @param carrierCategoryEmp the carrierCategoryEmp to set
     */
    public void setCarrierCategoryEmp(long carrierCategoryEmp) {
        this.carrierCategoryEmp = carrierCategoryEmp;
    }

    
    
        public Date getEndcontractfrom(){ 
		return endcontractfrom; 
	} 

	public void setEndcontractfrom(Date endcontractfrom){ 
		this.endcontractfrom = endcontractfrom; 
	} 

	public Date getEndcontractto(){ 
		return endcontractto; 
	} 

	public void setEndcontractto(Date endcontractto){ 
		this.endcontractto = endcontractto; 
	} 

    /**
     * @return the gradeLevel
     */
    public long getGradeLevel() {
        return gradeLevel;
    }

    /**
     * @param gradeLevel the gradeLevel to set
     */
    public void setGradeLevel(long gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    /**
     * @return the districtId
     */
    public long getDistrictId() {
        return districtId;
    }

    /**
     * @param districtId the districtId to set
     */
    public void setDistrictId(long districtId) {
        this.districtId = districtId;
    }

    /**
     * @return the districtIdPermanent
     */
    public long getDistrictIdPermanent() {
        return districtIdPermanent;
    }

    /**
     * @param districtIdPermanent the districtIdPermanent to set
     */
    public void setDistrictIdPermanent(long districtIdPermanent) {
        this.districtIdPermanent = districtIdPermanent;
    }
}
