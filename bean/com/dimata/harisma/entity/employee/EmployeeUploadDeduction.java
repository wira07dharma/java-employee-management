/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.employee;

import com.dimata.qdep.entity.Entity;
import java.util.Hashtable;

/**
 *
 * @author GUSWIK
 */
public class EmployeeUploadDeduction  extends Entity{
    
   private long empId;
   private String empName;
   private String empNumb;
   private double jumlahhutang=0;
   private double banyakBayar=0;
   private long periode=0;
   private String compCode;
   private String deskripsi;

    /**
     * @return the empId
     */
    public long getEmpId() {
        return empId;
    }

    /**
     * @param empId the empId to set
     */
    public void setEmpId(long empId) {
        this.empId = empId;
    }

    /**
     * @return the empName
     */
    public String getEmpName() {
        return empName;
    }

    /**
     * @param empName the empName to set
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }

    /**
     * @return the empNumb
     */
    public String getEmpNumb() {
        return empNumb;
    }

    /**
     * @param empNumb the empNumb to set
     */
    public void setEmpNumb(String empNumb) {
        this.empNumb = empNumb;
    }

    /**
     * @return the jumlahhutang
     */
    public double getJumlahhutang() {
        return jumlahhutang;
    }

    /**
     * @param jumlahhutang the jumlahhutang to set
     */
    public void setJumlahhutang(double jumlahhutang) {
        this.jumlahhutang = jumlahhutang;
    }

    /**
     * @return the banyakBayar
     */
    public double getBanyakBayar() {
        return banyakBayar;
    }

    /**
     * @param banyakBayar the banyakBayar to set
     */
    public void setBanyakBayar(double banyakBayar) {
        this.banyakBayar = banyakBayar;
    }

    
    /**
     * @return the compCode
     */
    public String getCompCode() {
        return compCode;
    }

    /**
     * @param compCode the compCode to set
     */
    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    /**
     * @return the deskripsi
     */
    public String getDeskripsi() {
        return deskripsi;
    }

    /**
     * @param deskripsi the deskripsi to set
     */
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    /**
     * @return the periode
     */
    public long getPeriode() {
        return periode;
    }

    /**
     * @param periode the periode to set
     */
    public void setPeriode(long periode) {
        this.periode = periode;
    }
    
}
