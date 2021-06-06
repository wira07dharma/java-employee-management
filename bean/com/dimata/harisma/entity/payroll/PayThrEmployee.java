/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

import com.dimata.qdep.entity.Entity;

/**
 *
 * @author Gunadi
 */
public class PayThrEmployee extends Entity {

    private long payThrId = 0;
    private long employeeId = 0;
    private float value = 0;

    public long getPayThrId() {
        return payThrId;
    }

    public void setPayThrId(long payThrId) {
        this.payThrId = payThrId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}