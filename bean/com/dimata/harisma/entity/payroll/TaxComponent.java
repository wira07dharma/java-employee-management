/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.entity.payroll;

/**
 *
 * @author Gunadi
 */
public class TaxComponent {
    
    private double gajiRutinYearToPrevPeriod = 0;
    private double gajiRutinOfPeriod = 0;
    private int jumlahPeriodeKedepan = 0;
    private double gajiRutinSetahun = 0;
    private double gajiNonRutinYearToPeriod = 0;
    private double gajiTotalSetahun = 0;
    private int employeeTaxType = 0;
    private double biayaJabatan = 0;
    private double pendapatanNettoSetahun = 0;
    private double potonganGajiYearToPrevPeriod = 0;
    private double potonganGajiOfPeriod = 0;
    private double totalPotongan = 0;
    private double pendapatanNettoSetahunSetelahPotongan = 0;
    private double ptkpSetahun = 0;
    private double pkpSetahun = 0;
    private double pph21Setahun = 0;
    private double pph21SampaiBulanSebelumnya = 0;
    private double pph21PrepayBulanIni = 0;
    private double pph21PeriodeSampaiAkhirTahun = 0;
    private double pph21KurangBayarBulanTerhitung = 0;
    private double compGajiOfPeriodWithout = 0;

    /**
     * @return the gajiRutinYearToPrevPeriod
     */
    public double getGajiRutinYearToPrevPeriod() {
        return gajiRutinYearToPrevPeriod;
    }

    /**
     * @param gajiRutinYearToPrevPeriod the gajiRutinYearToPrevPeriod to set
     */
    public void setGajiRutinYearToPrevPeriod(double gajiRutinYearToPrevPeriod) {
        this.gajiRutinYearToPrevPeriod = gajiRutinYearToPrevPeriod;
    }

    /**
     * @return the gajiRutinOfPeriod
     */
    public double getGajiRutinOfPeriod() {
        return gajiRutinOfPeriod;
    }

    /**
     * @param gajiRutinOfPeriod the gajiRutinOfPeriod to set
     */
    public void setGajiRutinOfPeriod(double gajiRutinOfPeriod) {
        this.gajiRutinOfPeriod = gajiRutinOfPeriod;
    }

    /**
     * @return the jumlahPeriodeKedepan
     */
    public int getJumlahPeriodeKedepan() {
        return jumlahPeriodeKedepan;
    }

    /**
     * @param jumlahPeriodeKedepan the jumlahPeriodeKedepan to set
     */
    public void setJumlahPeriodeKedepan(int jumlahPeriodeKedepan) {
        this.jumlahPeriodeKedepan = jumlahPeriodeKedepan;
    }

    /**
     * @return the gajiRutinSetahun
     */
    public double getGajiRutinSetahun() {
        return gajiRutinSetahun;
    }

    /**
     * @param gajiRutinSetahun the gajiRutinSetahun to set
     */
    public void setGajiRutinSetahun(double gajiRutinSetahun) {
        this.gajiRutinSetahun = gajiRutinSetahun;
    }

    /**
     * @return the gajiNonRutinYearToPeriod
     */
    public double getGajiNonRutinYearToPeriod() {
        return gajiNonRutinYearToPeriod;
    }

    /**
     * @param gajiNonRutinYearToPeriod the gajiNonRutinYearToPeriod to set
     */
    public void setGajiNonRutinYearToPeriod(double gajiNonRutinYearToPeriod) {
        this.gajiNonRutinYearToPeriod = gajiNonRutinYearToPeriod;
    }

    /**
     * @return the gajiTotalSetahun
     */
    public double getGajiTotalSetahun() {
        return gajiTotalSetahun;
    }

    /**
     * @param gajiTotalSetahun the gajiTotalSetahun to set
     */
    public void setGajiTotalSetahun(double gajiTotalSetahun) {
        this.gajiTotalSetahun = gajiTotalSetahun;
    }

    /**
     * @return the employeeTaxType
     */
    public int getEmployeeTaxType() {
        return employeeTaxType;
    }

    /**
     * @param employeeTaxType the employeeTaxType to set
     */
    public void setEmployeeTaxType(int employeeTaxType) {
        this.employeeTaxType = employeeTaxType;
    }

    /**
     * @return the biayaJabatan
     */
    public double getBiayaJabatan() {
        return biayaJabatan;
    }

    /**
     * @param biayaJabatan the biayaJabatan to set
     */
    public void setBiayaJabatan(double biayaJabatan) {
        this.biayaJabatan = biayaJabatan;
    }

    /**
     * @return the pendapatanNettoSetahun
     */
    public double getPendapatanNettoSetahun() {
        return pendapatanNettoSetahun;
    }

    /**
     * @param pendapatanNettoSetahun the pendapatanNettoSetahun to set
     */
    public void setPendapatanNettoSetahun(double pendapatanNettoSetahun) {
        this.pendapatanNettoSetahun = pendapatanNettoSetahun;
    }

    /**
     * @return the potonganGajiYearToPrevPeriod
     */
    public double getPotonganGajiYearToPrevPeriod() {
        return potonganGajiYearToPrevPeriod;
    }

    /**
     * @param potonganGajiYearToPrevPeriod the potonganGajiYearToPrevPeriod to set
     */
    public void setPotonganGajiYearToPrevPeriod(double potonganGajiYearToPrevPeriod) {
        this.potonganGajiYearToPrevPeriod = potonganGajiYearToPrevPeriod;
    }

    /**
     * @return the potonganGajiOfPeriod
     */
    public double getPotonganGajiOfPeriod() {
        return potonganGajiOfPeriod;
    }

    /**
     * @param potonganGajiOfPeriod the potonganGajiOfPeriod to set
     */
    public void setPotonganGajiOfPeriod(double potonganGajiOfPeriod) {
        this.potonganGajiOfPeriod = potonganGajiOfPeriod;
    }

    /**
     * @return the totalPotongan
     */
    public double getTotalPotongan() {
        return totalPotongan;
    }

    /**
     * @param totalPotongan the totalPotongan to set
     */
    public void setTotalPotongan(double totalPotongan) {
        this.totalPotongan = totalPotongan;
    }

    /**
     * @return the pendapatanNettoSetahunSetelahPotongan
     */
    public double getPendapatanNettoSetahunSetelahPotongan() {
        return pendapatanNettoSetahunSetelahPotongan;
    }

    /**
     * @param pendapatanNettoSetahunSetelahPotongan the pendapatanNettoSetahunSetelahPotongan to set
     */
    public void setPendapatanNettoSetahunSetelahPotongan(double pendapatanNettoSetahunSetelahPotongan) {
        this.pendapatanNettoSetahunSetelahPotongan = pendapatanNettoSetahunSetelahPotongan;
    }

    /**
     * @return the ptkpSetahun
     */
    public double getPtkpSetahun() {
        return ptkpSetahun;
    }

    /**
     * @param ptkpSetahun the ptkpSetahun to set
     */
    public void setPtkpSetahun(double ptkpSetahun) {
        this.ptkpSetahun = ptkpSetahun;
    }

    /**
     * @return the pkpSetahun
     */
    public double getPkpSetahun() {
        return pkpSetahun;
    }

    /**
     * @param pkpSetahun the pkpSetahun to set
     */
    public void setPkpSetahun(double pkpSetahun) {
        this.pkpSetahun = pkpSetahun;
    }

    /**
     * @return the pph21Setahun
     */
    public double getPph21Setahun() {
        return pph21Setahun;
    }

    /**
     * @param pph21Setahun the pph21Setahun to set
     */
    public void setPph21Setahun(double pph21Setahun) {
        this.pph21Setahun = pph21Setahun;
    }

    /**
     * @return the pph21SampaiBulanSebelumnya
     */
    public double getPph21SampaiBulanSebelumnya() {
        return pph21SampaiBulanSebelumnya;
    }

    /**
     * @param pph21SampaiBulanSebelumnya the pph21SampaiBulanSebelumnya to set
     */
    public void setPph21SampaiBulanSebelumnya(double pph21SampaiBulanSebelumnya) {
        this.pph21SampaiBulanSebelumnya = pph21SampaiBulanSebelumnya;
    }

    /**
     * @return the pph21PrepayBulanIni
     */
    public double getPph21PrepayBulanIni() {
        return pph21PrepayBulanIni;
    }

    /**
     * @param pph21PrepayBulanIni the pph21PrepayBulanIni to set
     */
    public void setPph21PrepayBulanIni(double pph21PrepayBulanIni) {
        this.pph21PrepayBulanIni = pph21PrepayBulanIni;
    }

    /**
     * @return the pph21PeriodeSampaiAkhirTahun
     */
    public double getPph21PeriodeSampaiAkhirTahun() {
        return pph21PeriodeSampaiAkhirTahun;
    }

    /**
     * @param pph21PeriodeSampaiAkhirTahun the pph21PeriodeSampaiAkhirTahun to set
     */
    public void setPph21PeriodeSampaiAkhirTahun(double pph21PeriodeSampaiAkhirTahun) {
        this.pph21PeriodeSampaiAkhirTahun = pph21PeriodeSampaiAkhirTahun;
    }

    /**
     * @return the pph21KurangBayarBulanTerhitung
     */
    public double getPph21KurangBayarBulanTerhitung() {
        return pph21KurangBayarBulanTerhitung;
    }

    /**
     * @param pph21KurangBayarBulanTerhitung the pph21KurangBayarBulanTerhitung to set
     */
    public void setPph21KurangBayarBulanTerhitung(double pph21KurangBayarBulanTerhitung) {
        this.pph21KurangBayarBulanTerhitung = pph21KurangBayarBulanTerhitung;
    }

    /**
     * @return the compGajiOfPeriodWithout
     */
    public double getCompGajiOfPeriodWithout() {
        return compGajiOfPeriodWithout;
    }

    /**
     * @param compGajiOfPeriodWithout the compGajiOfPeriodWithout to set
     */
    public void setCompGajiOfPeriodWithout(double compGajiOfPeriodWithout) {
        this.compGajiOfPeriodWithout = compGajiOfPeriodWithout;
    }
    
    
    
}
