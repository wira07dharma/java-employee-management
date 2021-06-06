/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

/**
 *
 * @author mchen
 */
import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class PayConfigPotongan extends Entity {
    private long employeeId = 0;
    private Date startDate = null;
    private Date endDate = null;
    private long componentId = 0;
    private double angsuranPerbulan = 0;
    private String noRekening = "";
    private int validStatus = 0;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
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

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public double getAngsuranPerbulan() {
        return angsuranPerbulan;
    }

    public void setAngsuranPerbulan(double angsuranPerbulan) {
        this.angsuranPerbulan = angsuranPerbulan;
    }

    public String getNoRekening() {
        return noRekening;
    }

    public void setNoRekening(String noRekening) {
        this.noRekening = noRekening;
    }

    public int getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(int validStatus) {
        this.validStatus = validStatus;
    }

}