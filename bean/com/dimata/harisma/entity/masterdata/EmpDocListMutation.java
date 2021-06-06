/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.masterdata;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author GUSWIK
 */
public class EmpDocListMutation extends Entity { 
        private long empDocListMutationId = 0;
        private long employeeId = 0;        
        private long empDocId = 0;
        private String objectName = "";
        private long companyId = 0;
        private long divisionId = 0;
        private long departmentId = 0;
        private long sectionId = 0;
        private long positionId = 0;
        private long empCatId = 0;
        private long levelId = 0;
        private Date workFrom ;

    /**
     * @return the empDocListMutationId
     */
    public long getEmpDocListMutationId() {
        return empDocListMutationId;
    }

    /**
     * @param empDocListMutationId the empDocListMutationId to set
     */
    public void setEmpDocListMutationId(long empDocListMutationId) {
        this.empDocListMutationId = empDocListMutationId;
    }

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
     * @return the objectName
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * @param objectName the objectName to set
     */
    public void setObjectName(String objectName) {
        this.objectName = objectName;
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
     * @return the empCatId
     */
    public long getEmpCatId() {
        return empCatId;
    }

    /**
     * @param empCatId the empCatId to set
     */
    public void setEmpCatId(long empCatId) {
        this.empCatId = empCatId;
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


}
