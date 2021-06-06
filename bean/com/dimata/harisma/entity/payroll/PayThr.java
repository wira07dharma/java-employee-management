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
public class PayThr extends Entity {

    private long calculationMainId = 0;
    private long periodId = 0;
    private int status = 0;
    private Date cutOffDate = null;

    public long getCalculationMainId() {
        return calculationMainId;
    }

    public void setCalculationMainId(long calculationMainId) {
        this.calculationMainId = calculationMainId;
    }

    public long getPeriodId() {
        return periodId;
    }

    public void setPeriodId(long periodId) {
        this.periodId = periodId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the cutOffDate
     */
    public Date getCutOffDate() {
        return cutOffDate;
    }

    /**
     * @param cutOffDate the cutOffDate to set
     */
    public void setCutOffDate(Date cutOffDate) {
        this.cutOffDate = cutOffDate;
    }
}