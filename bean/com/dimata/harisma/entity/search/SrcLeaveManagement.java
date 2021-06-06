/*
 * SrcLeaveDP.java
 *
 * Created on September 8, 2004, 9:36 AM
 */
package com.dimata.harisma.entity.search;

// import package core java
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author gedhy
 */
public class SrcLeaveManagement {

    private long employee_id = 0;
    private long divisionId = 0;
    private long companyId = 0;

    /**
     * Holds value of property empNum.
     */
    private String empNum = "";

    /**
     * Holds value of property empName.
     */
    private String empName = "";

    /**
     * Holds value of property empCatId.
     */
    private long empCatId = 0;

    /**
     * Holds value of property empDeptId.
     */
    private long empDeptId = 0;

    /**
     * Holds value of property empSectionId.
     */
    private long empSectionId = 0;

    /**
     * Holds value of property empPosId.
     */
    private long empPosId = 0;

    /**
     * Holds value of property leavePeriod.
     */
    private Date leavePeriod;

    /**
     * Holds value of property periodChecked.
     */
    private boolean periodChecked = true;

    /**
     * Holds value of property periodChecked.
     */
    private long empLevelId = 0;

    //add by artha
    private long periodId = 0;

    private Date startDate;

    private Date endDate;

    private int time = 0;

    private long payrolGroupId = 0;
	
	private long scheduleId = 0;

    private Vector arrDepartment = new Vector();
    private Vector arrPayrolGroup = new Vector();
    private String[] arrScheduleId = null;

    /**
     * Creates a new instance of SrcLeaveDP
     */
    public SrcLeaveManagement() {
    }

    public long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(long periodId) {
        this.periodId = periodId;
    }

    /**
     * Getter for property empNum.
     *
     * @return Value of property empNum.
     *
     */
    public String getEmpNum() {
        return this.empNum;
    }

    /**
     * Setter for property empNum.
     *
     * @param empNum New value of property empNum.
     *
     */
    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

    /**
     * Getter for property empName.
     *
     * @return Value of property empName.
     *
     */
    public String getEmpName() {
        return this.empName;
    }

    /**
     * Setter for property empName.
     *
     * @param empName New value of property empName.
     *
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * Getter for property empCatId.
     *
     * @return Value of property empCatId.
     *
     */
    public long getEmpCatId() {
        return this.empCatId;
    }

    /**
     * Setter for property empCatId.
     *
     * @param empCatId New value of property empCatId.
     *
     */
    public void setEmpCatId(long empCatId) {
        this.empCatId = empCatId;
    }

    /**
     * Getter for property empDeptId.
     *
     * @return Value of property empDeptId.
     *
     */
    public long getEmpDeptId() {
        return this.empDeptId;
    }

    /**
     * Setter for property empDeptId.
     *
     * @param empDeptId New value of property empDeptId.
     *
     */
    public void setEmpDeptId(long empDeptId) {
        this.empDeptId = empDeptId;
    }

    /**
     * Getter for property empSectionId.
     *
     * @return Value of property empSectionId.
     *
     */
    public long getEmpSectionId() {
        return this.empSectionId;
    }

    /**
     * Setter for property empSectionId.
     *
     * @param empSectionId New value of property empSectionId.
     * srcLeaveDP.getEmpDeptId()
     */
    public void setEmpSectionId(long empSectionId) {
        this.empSectionId = empSectionId;
    }

    /**
     * Getter for property empPosId.
     *
     * @return Value of property empPosId.
     *
     */
    public long getEmpPosId() {
        return this.empPosId;
    }

    /**
     * Setter for property empPosId.
     *
     * @param empPosId New value of property empPosId.
     *
     */
    public void setEmpPosId(long empPosId) {
        this.empPosId = empPosId;
    }

    /**
     * Getter for property dpPeriod.
     *
     * @return Value of property dpPeriod.
     *
     */
    public Date getLeavePeriod() {
        return this.leavePeriod;
    }

    /**
     * Setter for property dpPeriod.
     *
     * @param dpPeriod New value of property dpPeriod.
     *
     */
    public void setLeavePeriod(Date leavePeriod) {
        this.leavePeriod = leavePeriod;
    }

    /**
     * Getter for property periodChecked.
     *
     * @return Value of property periodChecked.
     *
     */
    public boolean isPeriodChecked() {
        return this.periodChecked;
    }

    /**
     * Setter for property periodChecked.
     *
     * @param periodChecked New value of property periodChecked.
     *
     */
    public void setPeriodChecked(boolean periodChecked) {
        this.periodChecked = periodChecked;
    }

    /**
     * Getter for property empLevelId.
     *
     * @return Value of property empLevelId.
     */
    public long getEmpLevelId() {
        return empLevelId;
    }

    /**
     * Setter for property empLevelId.
     *
     * @param empLevelId New value of property empLevelId.
     */
    public void setEmpLevelId(long empLevelId) {
        this.empLevelId = empLevelId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public long getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(long employee_id) {
        this.employee_id = employee_id;
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
     * @return the payrolGroupId
     */
    public long getPayrolGroupId() {
        return payrolGroupId;
    }

    /**
     * @param payrolGroupId the payrolGroupId to set
     */
    public void setPayrolGroupId(long payrolGroupId) {
        this.payrolGroupId = payrolGroupId;
    }

    /**
     * @return the arrDepartment
     */
    public String[] getArrDepartment(int idx) {
        if (idx >= arrDepartment.size()) {
            return null;
        }
        return (String[]) arrDepartment.get(idx);
    }

    /**
     * @param arrDepartment the arrDepartment to set
     */
    public void addArrDepartment(String[] arrDepartment) {
        this.arrDepartment.add(arrDepartment);
    }

    /**
     * @return the arrDepartment
     */
    public String[] getArrPayrolGroup(int idx) {
        if (idx >= arrPayrolGroup.size()) {
            return null;
        }
        return (String[]) arrPayrolGroup.get(idx);
    }

    /**
     * @param arrDepartment the arrDepartment to set
     */
    public void addArrPayrolGroup(String[] arrPayrolGroup) {
        this.arrPayrolGroup.add(arrPayrolGroup);
    }

    public String[] getArrScheduleId() {
        return arrScheduleId;
    }

    public void setArrScheduleId(String[] arrScheduleId) {
        this.arrScheduleId = arrScheduleId;
    }

	/**
	 * @return the scheduleId
	 */
	public long getScheduleId() {
		return scheduleId;
	}

	/**
	 * @param scheduleId the scheduleId to set
	 */
	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

}
