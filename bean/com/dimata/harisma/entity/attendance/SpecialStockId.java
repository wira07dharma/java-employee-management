/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.attendance;

import com.dimata.qdep.entity.Entity;
import java.util.Date;

public class SpecialStockId extends Entity {

    private long scheduleId = 0;
    private float qty = 0;
    private Date owningDate = null;
    private Date expiredDate = null;
    private int status = 0;
    private String note = "";
    private long employeeId = 0;
    private float qtyUsed = 0;
    private float qtyResidue = 0;
    private long addressIdCard = 0;

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public Date getOwningDate() {
        return owningDate;
    }

    public void setOwningDate(Date owningDate) {
        this.owningDate = owningDate;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public float getQtyUsed() {
        return qtyUsed;
    }

    public void setQtyUsed(float qtyUsed) {
        this.qtyUsed = qtyUsed;
    }

    public float getQtyResidue() {
        return qtyResidue;
    }

    public void setQtyResidue(float qtyResidue) {
        this.qtyResidue = qtyResidue;
    }

    public long getAddressIdCard() {
        return addressIdCard;
    }

    public void setAddressIdCard(long addressIdCard) {
        this.addressIdCard = addressIdCard;
    }

}
