/*
 * LeaveApplication.java
 *
 * Created on October 27, 2004, 11:51 AM
 */
package com.dimata.harisma.entity.leave;

import com.dimata.qdep.entity.Entity;

import java.util.Date;
import java.util.Vector;

/**
 *
 * @author gedhy
 */
public class LeaveApplication extends Entity {

    /**
     * Holds value of property submissionDate.
     */
    private Date submissionDate;

    /**
     * Holds value of property employeeId.
     */
    private long employeeId = 0;

    /**
     * Holds value of property leaveReason.
     */
    private String leaveReason = "";

    /**
     * Holds value of property depHeadApproval.
     */
    private long depHeadApproval = 0;

    /**
     * Holds value of property hrManApproval.
     */
    private long hrManApproval = 0;

    /**
     * Holds value of property docStatus.
     */
    private int docStatus;

    /**
     * Holds value of property listOfDetail.
     */
    private Vector listOfDetail;

    /**
     * Holds value of property depHeadApproveDate.
     */
    private Date depHeadApproveDate;

    /**
     * Holds value of property hrManApproveDate.
     */
    private Date hrManApproveDate;

    /**
     * Holds value of property vListOfDetailView.
     */
    private Vector vListOfDetailView;

    private long gmApproval;

    private Date gmApprovalDate;

    //update by satrya 2013-03-13
    private int typeLeave;

    //update by satrya 2013-04-11
    private int typeFormLeave = 0;

    //update by satrya 2013-04-17
    private long reasonId;

    private long leaveAppDiffPeriod;

    //priska menambahkan al ll allowance
    //20150805
    private int alAllowance;
    private int llAllowance;

    //added by dewok 20190610
    private long employeePrepareId = 0;

    /**
     * Getter for property submissionDate.
     *
     * @return Value of property submissionDate.
     *
     */
    public Date getSubmissionDate() {
        return this.submissionDate;
    }

    /**
     * Setter for property submissionDate.
     *
     * @param submissionDate New value of property submissionDate.
     *
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * Getter for property employeeId.
     *
     * @return Value of property employeeId.
     *
     */
    public long getEmployeeId() {
        return this.employeeId;
    }

    /**
     * Setter for property employeeId.
     *
     * @param employeeId New value of property employeeId.
     *
     */
    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Getter for property leaveReason.
     *
     * @return Value of property leaveReason.
     *
     */
    public String getLeaveReason() {
        return this.leaveReason;
    }

    /**
     * Setter for property leaveReason.
     *
     * @param leaveReason New value of property leaveReason.
     *
     */
    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    /**
     * Getter for property depHeadApproval.
     *
     * @return Value of property depHeadApproval.
     *
     */
    public long getDepHeadApproval() {
        return this.depHeadApproval;
    }

    /**
     * Setter for property depHeadApproval.
     *
     * @param depHeadApproval New value of property depHeadApproval.
     *
     */
    public void setDepHeadApproval(long depHeadApproval) {
        this.depHeadApproval = depHeadApproval;
    }

    /**
     * Getter for property hrManApproval.
     *
     * @return Value of property hrManApproval.
     *
     */
    public long getHrManApproval() {
        return this.hrManApproval;
    }

    /**
     * Setter for property hrManApproval.
     *
     * @param hrManApproval New value of property hrManApproval.
     *
     */
    public void setHrManApproval(long hrManApproval) {
        this.hrManApproval = hrManApproval;
    }

    /**
     * Getter for property docStatus.
     *
     * @return Value of property docStatus.
     *
     */
    public int getDocStatus() {
        return this.docStatus;
    }

    /**
     * Setter for property docStatus.
     *
     * @param docStatus New value of property docStatus.
     *
     */
    public void setDocStatus(int docStatus) {
        this.docStatus = docStatus;
    }

    /**
     * Getter for property listOfDetail.
     *
     * @return Value of property listOfDetail.
     *
     */
    public Vector getListOfDetail() {
        return this.listOfDetail;
    }

    /**
     * Setter for property listOfDetail.
     *
     * @param listOfDetail New value of property listOfDetail.
     *
     */
    public void setListOfDetail(Vector listOfDetail) {
        this.listOfDetail = listOfDetail;
    }

    /**
     * Getter for property depHeadApproveDate.
     *
     * @return Value of property depHeadApproveDate.
     *
     */
    public Date getDepHeadApproveDate() {
        return this.depHeadApproveDate;
    }

    /**
     * Setter for property depHeadApproveDate.
     *
     * @param depHeadApproveDate New value of property depHeadApproveDate.
     *
     */
    public void setDepHeadApproveDate(Date depHeadApproveDate) {
        this.depHeadApproveDate = depHeadApproveDate;
    }

    /**
     * Getter for property hrManApproveDate.
     *
     * @return Value of property hrManApproveDate.
     *
     */
    public Date getHrManApproveDate() {
        return this.hrManApproveDate;
    }

    /**
     * Setter for property hrManApproveDate.
     *
     * @param hrManApproveDate New value of property hrManApproveDate.
     *
     */
    public void setHrManApproveDate(Date hrManApproveDate) {
        this.hrManApproveDate = hrManApproveDate;
    }

    /**
     * Getter for property vListOfDetailView.
     *
     * @return Value of property vListOfDetailView.
     *
     */
    public Vector getVListOfDetailView() {
        return this.vListOfDetailView;
    }

    /**
     * Setter for property vListOfDetailView.
     *
     * @param vListOfDetailView New value of property vListOfDetailView.
     *
     */
    public void setVListOfDetailView(Vector vListOfDetailView) {
        this.vListOfDetailView = vListOfDetailView;
    }

    public long getGmApproval() {
        return gmApproval;
    }

    public void setGmApproval(long gmApproval) {
        this.gmApproval = gmApproval;
    }

    public Date getGmApprovalDate() {
        return gmApprovalDate;
    }

    public void setGmApprovalDate(Date gmApprovalDate) {
        this.gmApprovalDate = gmApprovalDate;
    }

    /**
     * @return the typeLeave
     */
    public int getTypeLeave() {
        return typeLeave;
    }

    /**
     * @param typeLeave the typeLeave to set
     */
    public void setTypeLeave(int typeLeave) {
        this.typeLeave = typeLeave;
    }

    /**
     * @return the typeFormLeave
     */
    public int getTypeFormLeave() {
        return typeFormLeave;
    }

    /**
     * @param typeFormLeave the typeFormLeave to set
     */
    public void setTypeFormLeave(int typeFormLeave) {
        this.typeFormLeave = typeFormLeave;
    }

    /**
     * @return the reasonId
     */
    public long getReasonId() {
        return reasonId;
    }

    /**
     * @param reasonId the reasonId to set
     */
    public void setReasonId(long reasonId) {
        this.reasonId = reasonId;
    }

    /**
     * @return the leaveAppDiffPeriod
     */
    public long getLeaveAppDiffPeriod() {
        return leaveAppDiffPeriod;
    }

    /**
     * @param leaveAppDiffPeriod the leaveAppDiffPeriod to set
     */
    public void setLeaveAppDiffPeriod(long leaveAppDiffPeriod) {
        this.leaveAppDiffPeriod = leaveAppDiffPeriod;
    }

    /**
     * @return the alAllowance
     */
    public int getAlAllowance() {
        return alAllowance;
    }

    /**
     * @param alAllowance the alAllowance to set
     */
    public void setAlAllowance(int alAllowance) {
        this.alAllowance = alAllowance;
    }

    /**
     * @return the llAllowance
     */
    public int getLlAllowance() {
        return llAllowance;
    }

    /**
     * @param llAllowance the llAllowance to set
     */
    public void setLlAllowance(int llAllowance) {
        this.llAllowance = llAllowance;
    }

    public long getEmployeePrepareId() {
        return employeePrepareId;
    }

    public void setEmployeePrepareId(long employeePrepareId) {
        this.employeePrepareId = employeePrepareId;
    }

}
