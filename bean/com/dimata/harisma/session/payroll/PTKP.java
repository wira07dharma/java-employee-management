/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.session.payroll;
import com.dimata.system.entity.PstSystemProperty;
import java.util.Date;
/**
 *
 * @author Kartika
 */
public class PTKP {
    
    
        private Date startDate = new Date(2015-1900, 1-1, 1);
        /* PTKP : 2015 */
        private double PTKP_DIRI_SENDIRI = 36000000.0;// 
        private double PTKP_KAWIN        = 39000000.0;// 
        private double PTKP_KAWIN_ANAK_1 = 42000000.0;// 
        private double PTKP_KAWIN_ANAK_2 = 45000000.0;// 
        private double PTKP_KAWIN_ANAK_3 = 48000000.0;// 
        
        /* PTKP : 2014
        double PTKP_DIRI_SENDIRI = 24300000.0;//    15.840.000.0;
        double PTKP_KAWIN = 26325000.0;//    17.160.000.0;
        double PTKP_KAWIN_ANAK_1 = 28350000.0;//    18.480.000.0;
        double PTKP_KAWIN_ANAK_2 = 30375000.0;//    19.800.000.0;
        double PTKP_KAWIN_ANAK_3 = 32400000.0;//    21.120.000.0;          
        */

    /**
     * @return the startDate
     */
     

    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    
    public Date getStartDate() {
        return startDate;
    }

   

    /**
     * @return the PTKP_DIRI_SENDIRI
     */
    public double getPTKP_DIRI_SENDIRI() {
            try {
                PTKP_DIRI_SENDIRI = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_DIRI_SENDIRI"));//menambahkan system properties
            } catch (Exception e) {
                System.out.println("Exeception PAYROLL_PTKP_DIRI_SENDIRI:" + e);
            }
        return PTKP_DIRI_SENDIRI;
    }

    /**
     * @param PTKP_DIRI_SENDIRI the PTKP_DIRI_SENDIRI to set
     */
    public void setPTKP_DIRI_SENDIRI(double PTKP_DIRI_SENDIRI) {
        this.PTKP_DIRI_SENDIRI = PTKP_DIRI_SENDIRI;
    }

    /**
     * @return the PTKP_KAWIN
     */
    public double getPTKP_KAWIN() {
            try {
                PTKP_KAWIN = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN"));//menambahkan system properties
            } catch (Exception e) {
                System.out.println("Exeception PAYROLL_PTKP_KAWIN:" + e);
            }
        return PTKP_KAWIN;
    }

    /**
     * @param PTKP_KAWIN the PTKP_KAWIN to set
     */
    public void setPTKP_KAWIN(double PTKP_KAWIN) {
        this.PTKP_KAWIN = PTKP_KAWIN;
    }

    /**
     * @return the PTKP_KAWIN_ANAK_1
     */
    public double getPTKP_KAWIN_ANAK_1() {
        
        try {
                PTKP_KAWIN_ANAK_1 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_1"));//menambahkan system properties
            } catch (Exception e) {
                System.out.println("Exeception PAYROLL_PTKP_KAWIN:" + e);
            }
        return PTKP_KAWIN_ANAK_1;
    }

    /**
     * @param PTKP_KAWIN_ANAK_1 the PTKP_KAWIN_ANAK_1 to set
     */
    public void setPTKP_KAWIN_ANAK_1(double PTKP_KAWIN_ANAK_1) {
        this.PTKP_KAWIN_ANAK_1 = PTKP_KAWIN_ANAK_1;
    }

    /**
     * @return the PTKP_KAWIN_ANAK_2
     */
    public double getPTKP_KAWIN_ANAK_2() {
          try {
                PTKP_KAWIN_ANAK_2 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_2"));//menambahkan system properties
            } catch (Exception e) {
                System.out.println("Exeception PAYROLL_PTKP_KAWIN_ANAK_2:" + e);
            }
        return PTKP_KAWIN_ANAK_2;
    }

    /**
     * @param PTKP_KAWIN_ANAK_2 the PTKP_KAWIN_ANAK_2 to set
     */
    public void setPTKP_KAWIN_ANAK_2(double PTKP_KAWIN_ANAK_2) {
        this.PTKP_KAWIN_ANAK_2 = PTKP_KAWIN_ANAK_2;
    }

    /**
     * @return the PTKP_KAWIN_ANAK_3
     */
    public double getPTKP_KAWIN_ANAK_3() {
        try {
                PTKP_KAWIN_ANAK_3 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_3"));//menambahkan system properties
            } catch (Exception e) {
                System.out.println("Exeception PAYROLL_PTKP_KAWIN_ANAK_3:" + e);
            }
        return PTKP_KAWIN_ANAK_3;
    }

    /**
     * @param PTKP_KAWIN_ANAK_3 the PTKP_KAWIN_ANAK_3 to set
     */
    public void setPTKP_KAWIN_ANAK_3(double PTKP_KAWIN_ANAK_3) {
        this.PTKP_KAWIN_ANAK_3 = PTKP_KAWIN_ANAK_3;
    }
        
   
}
