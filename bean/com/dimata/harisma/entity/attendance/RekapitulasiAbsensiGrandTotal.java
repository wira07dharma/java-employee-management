/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.attendance;

import java.util.Hashtable;

/**
 *
 * @author Satrya Ramayu
 */
public class RekapitulasiAbsensiGrandTotal {
    private int HK;
    private int H;
    private int lateLebihLimaMenit=0;
    private Hashtable reason=new Hashtable();
    private int totalRe=0;
    private double payDay;
    private double totalPayDay;
    /*2014-11-28 Update by Hendra*/
    private long totalAlasan = 0;
    private long annualLeave = 0;
    private long dPayment = 0;
    
    
    private int So4j = 0;
    private int So8j = 0;
    
    private int nightShift = 0;
    /**
     * @return the HK
     */
    public int getHK() {
        return HK;
    }

    /**
     * @param HK the HK to set
     */
    public void setHK(int HK) {
        this.HK = HK;
    }

    /**
     * @return the H
     */
    public int getH() {
        return H;
    }

    /**
     * @param H the H to set
     */
    public void setH(int H) {
        this.H = H;
    }

    /**
     * @return the Reason
     */
    public Hashtable getReason() {
        return reason;
    }
    
    public int getTotalReason(int reasonNo) {
        int totReason=0;
        if(reason!=null && reason.size()>0 && reason.containsKey(""+reasonNo)){
            totReason = (Integer)reason.get(""+reasonNo);
            //totalRe = totalRe + totReason;
        }
        
        return totReason;
    }
    
    public int getGrandTotalReason(int reasonNo) {
        int totReason=0;
        if(reason!=null && reason.size()>0 && reason.containsKey(""+reasonNo)){
            totReason = (Integer)reason.get(""+reasonNo);
            totalRe = totalRe + totReason;
        }
        
        return totReason;
    }

    /**
     * @param Reason the Reason to set
     */
    public void addReason(int reasonNo,int nilai) {
        if(reason==null){
            reason = new Hashtable();
        }
        this.reason.put(""+reasonNo, nilai);
    }

    /**
     * @return the lateLebihLimaMenit
     */
    public int getLateLebihLimaMenit() {
        return lateLebihLimaMenit;
    }

    /**
     * @param lateLebihLimaMenit the lateLebihLimaMenit to set
     */
    public void setLateLebihLimaMenit(int lateLebihLimaMenit) {
        this.lateLebihLimaMenit = lateLebihLimaMenit;
    }
    
    /**
     * @return the totalSemuanya
     */
    public int getTotalSemuanya() {
        return (H + HK  + totalRe);
    }

    /**
     * @return the payDay
     */
    public double getPayDay() {
        return payDay;
    }

    /**
     * @param payDay the payDay to set
     */
    public void setPayDay(double payDay) {
        this.payDay = payDay;
    }

    /**
     * @return the totalPayDay
     */
    public double getTotalPayDay() {
        return totalPayDay;
    }

    /**
     * @param totalPayDay the totalPayDay to set
     */
    public void setTotalPayDay(double totalPayDay) {
        this.totalPayDay = totalPayDay;
    }

    /**
     * @return the totalAlasan
     */
    public long getTotalAlasan() {
        return totalAlasan;
    }

    /**
     * @param totalAlasan the totalAlasan to set
     */
    public void setTotalAlasan(long totalAlasan) {
        this.totalAlasan = totalAlasan;
    }

    /**
     * @return the annualLeave
     */
    public long getAnnualLeave() {
        return annualLeave;
    }

    /**
     * @param annualLeave the annualLeave to set
     */
    public void setAnnualLeave(long annualLeave) {
        this.annualLeave = annualLeave;
    }

    /**
     * @return the dPayment
     */
    public long getdPayment() {
        return dPayment;
    }

    /**
     * @param dPayment the dPayment to set
     */
    public void setdPayment(long dPayment) {
        this.dPayment = dPayment;
    }

    /**
     * @return the So8j
     */
    public int getSo8j() {
        return So8j;
    }

    /**
     * @param So8j the So8j to set
     */
    public void setSo8j(int So8j) {
        this.So8j = So8j;
    }

    /**
     * @return the So4j
     */
    public int getSo4j() {
        return So4j;
    }

    /**
     * @param So4j the So4j to set
     */
    public void setSo4j(int So4j) {
        this.So4j = So4j;
    }

    /**
     * @return the nightShift
     */
    public int getNightShift() {
        return nightShift;
    }

    /**
     * @param nightShift the nightShift to set
     */
    public void setNightShift(int nightShift) {
        this.nightShift = nightShift;
    }

  

}
