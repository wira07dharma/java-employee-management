/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.attendance;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

/**
 *
 * @author Dimata 007
 */
public class SpecialStockTaken extends Entity {

    private long employeeId = 0;
    private long scheduleId = 0;
    private long specialStockId = 0;
    private Date takenDate = null;
    private float takenQty = 0;
    private Date paidDate = null;
    private long leaveApplicationId = 0;
    private Date takenFinishDate = null;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public long getSpecialStockId() {
        return specialStockId;
    }

    public void setSpecialStockId(long specialStockId) {
        this.specialStockId = specialStockId;
    }

    public Date getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    public float getTakenQty() {
        return takenQty;
    }

    public void setTakenQty(float takenQty) {
        this.takenQty = takenQty;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

    public long getLeaveApplicationId() {
        return leaveApplicationId;
    }

    public void setLeaveApplicationId(long leaveApplicationId) {
        this.leaveApplicationId = leaveApplicationId;
    }

    public Date getTakenFinishDate() {
        return takenFinishDate;
    }

    public void setTakenFinishDate(Date takenFinishDate) {
        this.takenFinishDate = takenFinishDate;
    }

}
