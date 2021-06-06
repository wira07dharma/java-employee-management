/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

import com.dimata.qdep.entity.Entity;

import java.util.Date;

/**
 *
 * @author Gunadi
 */
public class ThrCalculationMapping extends Entity {

    private long calculationMainId = 0;
    private Date startDate = null;
    private Date endDate = null;
    private long companyId = 0;
    private long divisionId = 0;
    private long departmentId = 0;
    private long sectionId = 0;
    private long levelId = 0;
    private long maritalId = 0;
    private long employeeCategory = 0;
    private long positionId = 0;
    private long employeeId = 0;
    private long grade = 0;
    private int losFromInDay = 0;
    private int losFromInMonth = 0;
    private int losFromInYear = 0;
    private int losToInDay = 0;
    private int losToInMonth = 0;
    private int losToInYear = 0;
    private int losCurrentDate = 0;
    private Date losPerCurrentDate = null;
    private long taxMaritalId = 0;
    private int mappingType = 0;
    private float value;
    private int propotional = 0;
    private long payrollGroupId = 0;

    public long getCalculationMainId() {
        return calculationMainId;
    }

    public void setCalculationMainId(long calculationMainId) {
        this.calculationMainId = calculationMainId;
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

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(long divisionId) {
        this.divisionId = divisionId;
    }

    public long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(long departmentId) {
        this.departmentId = departmentId;
    }

    public long getSectionId() {
        return sectionId;
    }

    public void setSectionId(long sectionId) {
        this.sectionId = sectionId;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    public long getMaritalId() {
        return maritalId;
    }

    public void setMaritalId(long maritalId) {
        this.maritalId = maritalId;
    }

    public long getEmployeeCategory() {
        return employeeCategory;
    }

    public void setEmployeeCategory(long employeeCategory) {
        this.employeeCategory = employeeCategory;
    }

    public long getPositionId() {
        return positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getGrade() {
        return grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
    }

    public int getLosFromInDay() {
        return losFromInDay;
    }

    public void setLosFromInDay(int losFromInDay) {
        this.losFromInDay = losFromInDay;
    }

    public int getLosFromInMonth() {
        return losFromInMonth;
    }

    public void setLosFromInMonth(int losFromInMonth) {
        this.losFromInMonth = losFromInMonth;
    }

    public int getLosFromInYear() {
        return losFromInYear;
    }

    public void setLosFromInYear(int losFromInYear) {
        this.losFromInYear = losFromInYear;
    }

    public int getLosToInDay() {
        return losToInDay;
    }

    public void setLosToInDay(int losToInDay) {
        this.losToInDay = losToInDay;
    }

    public int getLosToInMonth() {
        return losToInMonth;
    }

    public void setLosToInMonth(int losToInMonth) {
        this.losToInMonth = losToInMonth;
    }

    public int getLosToInYear() {
        return losToInYear;
    }

    public void setLosToInYear(int losToInYear) {
        this.losToInYear = losToInYear;
    }

    public int getLosCurrentDate() {
        return losCurrentDate;
    }

    public void setLosCurrentDate(int losCurrentDate) {
        this.losCurrentDate = losCurrentDate;
    }

    public Date getLosPerCurrentDate() {
        return losPerCurrentDate;
    }

    public void setLosPerCurrentDate(Date losPerCurrentDate) {
        this.losPerCurrentDate = losPerCurrentDate;
    }

    public long getTaxMaritalId() {
        return taxMaritalId;
    }

    public void setTaxMaritalId(long taxMaritalId) {
        this.taxMaritalId = taxMaritalId;
    }

    /**
     * @return the mappingType
     */
    public int getMappingType() {
        return mappingType;
    }

    /**
     * @param mappingType the mappingType to set
     */
    public void setMappingType(int mappingType) {
        this.mappingType = mappingType;
    }

    /**
     * @return the value
     */
    public float getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(float value) {
        this.value = value;
    }

    /**
     * @return the propotional
     */
    public int getPropotional() {
        return propotional;
    }

    /**
     * @param propotional the propotional to set
     */
    public void setPropotional(int propotional) {
        this.propotional = propotional;
    }

    /**
     * @return the payrollGroupId
     */
    public long getPayrollGroupId() {
        return payrollGroupId;
    }

    /**
     * @param payrollGroupId the payrollGroupId to set
     */
    public void setPayrollGroupId(long payrollGroupId) {
        this.payrollGroupId = payrollGroupId;
    }
}