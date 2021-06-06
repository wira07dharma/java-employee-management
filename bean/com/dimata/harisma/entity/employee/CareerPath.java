
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
package com.dimata.harisma.entity.employee;

/* package java */
import java.util.Date;

/* package qdep */
import com.dimata.qdep.entity.*;

public class CareerPath extends Entity {

    private long employeeId;
    private Employee employee = null;// only to view to to save
    private long companyId;
    private String company = "";
    private long departmentId;
    private String department = "";
    private long positionId;
    private String position = "";
    private long sectionId;
    private String section = "";
    private long divisionId;
    private String division = "";
    private long levelId;
    private String level = "";    
    private long empCategoryId;
    private String empCategory = "";
    private Date workFrom;
    private Date workTo;
    private String description = "";
    private double salary;
    private long locationId;
    private String location="";
    private String note="";
    private long providerID=0;
    /* Update by Hendra Putu | 2015-10-09 */
    private int historyType = 0;
    private String nomorSk = "";
    private Date tanggalSk = new Date();
    private long empDocId = 0;
    /* Update by Hendra Putu | 2015-10-26 */
    private int historyGroup = 0;
    /* Update by Hendra Putu | Field GRADE_LEVEL_ID | 2015-11-25 */
    private long gradeLevelId = 0;
    //Gunadi 2017-12-14
    private int mutationType = 0;
    
    /**
     * @return the employeeId
     */
    public long getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
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
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the departmentId
     */
    public long getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId the departmentId to set
     */
    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the positionId
     */
    public long getPositionId() {
        return positionId;
    }

    /**
     * @param positionId the positionId to set
     */
    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    /**
     * @return the position
     */
    public String getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(String position) {
        this.position = position;
    }

    /**
     * @return the sectionId
     */
    public long getSectionId() {
        return sectionId;
    }

    /**
     * @param sectionId the sectionId to set
     */
    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * @return the section
     */
    public String getSection() {
        return section;
    }

    /**
     * @param section the section to set
     */
    public void setSection(String section) {
        this.section = section;
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
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * @param division the division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * @return the levelId
     */
    public long getLevelId() {
        return levelId;
    }

    /**
     * @param levelId the levelId to set
     */
    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    /**
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(String level) {
        this.level = level;
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
     * @return the empCategory
     */
    public String getEmpCategory() {
        return empCategory;
    }

    /**
     * @param empCategory the empCategory to set
     */
    public void setEmpCategory(String empCategory) {
        this.empCategory = empCategory;
    }

    /**
     * @return the workFrom
     */
    public Date getWorkFrom() {
        return workFrom;
    }

    /**
     * @param workFrom the workFrom to set
     */
    public void setWorkFrom(Date workFrom) {
        this.workFrom = workFrom;
    }

    /**
     * @return the workTo
     */
    public Date getWorkTo() {
        return workTo;
    }

    /**
     * @param workTo the workTo to set
     */
    public void setWorkTo(Date workTo) {
        this.workTo = workTo;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * @param salary the salary to set
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * @return the locationId
     */
    
    public long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    
    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    /**
     * @return the location
     */
    
    public String getLocation() {
        return location;
    }

    /**
     * @param location the location to set
     */
    
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }
    
    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
}

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the providerID
     */
    public long getProviderID() {
        return providerID;
    }

    /**
     * @param providerID the providerID to set
     */
    public void setProviderID(long providerID) {
        this.providerID = providerID;
    }

    /**
     * @return the historyType
     */
    public int getHistoryType() {
        return historyType;
    }

    /**
     * @param historyType the historyType to set
     */
    public void setHistoryType(int historyType) {
        this.historyType = historyType;
    }

    /**
     * @return the nomorSk
     */
    public String getNomorSk() {
        return nomorSk;
    }

    /**
     * @param nomorSk the nomorSk to set
     */
    public void setNomorSk(String nomorSk) {
        this.nomorSk = nomorSk;
    }

    /**
     * @return the tanggalSk
     */
    public Date getTanggalSk() {
        return tanggalSk;
    }

    /**
     * @param tanggalSk the tanggalSk to set
     */
    public void setTanggalSk(Date tanggalSk) {
        this.tanggalSk = tanggalSk;
    }

    /**
     * @return the empDocId
     */
    public long getEmpDocId() {
        return empDocId;
    }

    /**
     * @param empDocId the empDocId to set
     */
    public void setEmpDocId(long empDocId) {
        this.empDocId = empDocId;
    }

    /**
     * @return the historyGroup
     */
    public int getHistoryGroup() {
        return historyGroup;
    }

    /**
     * @param historyGroup the historyGroup to set
     */
    public void setHistoryGroup(int historyGroup) {
        this.historyGroup = historyGroup;
    }

    /**
     * @return the gradeLevelId
     */
    public long getGradeLevelId() {
        return gradeLevelId;
    }

    /**
     * @param gradeLevelId the gradeLevelId to set
     */
    public void setGradeLevelId(long gradeLevelId) {
        this.gradeLevelId = gradeLevelId;
    }

    /**
     * @return the mutationType
     */
    public int getMutationType() {
        return mutationType;
    }

    /**
     * @param mutationType the mutationType to set
     */
    public void setMutationType(int mutationType) {
        this.mutationType = mutationType;
    }



    
}
