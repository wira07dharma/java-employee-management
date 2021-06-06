/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.session.payroll;

import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.employee.PstFamilyMember;
import com.dimata.harisma.entity.masterdata.Marital;
import com.dimata.harisma.entity.masterdata.PstMarital;
import com.dimata.harisma.entity.payroll.PayPeriod;
import com.dimata.harisma.entity.payroll.PstPayComponent;
import com.dimata.harisma.entity.payroll.PstPayPeriod;
import com.dimata.harisma.entity.payroll.PstPaySlip;
import com.dimata.harisma.entity.payroll.PstPaySlipComp;
import com.dimata.harisma.entity.payroll.TaxComponent;
import com.dimata.system.entity.system.PstSystemProperty;
import com.dimata.util.Formater;
import java.util.Vector;

/**
 *
 * @author Gunadi
 */
public class SessTaxReport {
    
    public static TaxComponent calcSalaryTax(long employeeId, PayPeriod payPeriod, PayPeriod prevPeriod, Pajak pajak, Vector withoutComponents, boolean returnPeriodToEndYear, int backPeriod) {
        
        Vector listPajak = new Vector();
        
        TaxComponent taxComponent = new TaxComponent();
        //public static double calcSalaryTax(long employeeId, Period period, Period prevPeriod){

        String Tax_Advance_Deduc = PstSystemProperty.getValueByName("TAX_ADVANCE_DEDUC");


        double pph21KurangBayarBulanTerhitung = 0.0;
        if (payPeriod == null || payPeriod.getOID() == 0) {
            return taxComponent;
        }

        Employee employee = null;
        try {
            employee = PstEmployee.fetchExc(employeeId);
        } catch (Exception exc) {
            System.out.println(exc);
        }

        if (employee == null || employee.getOID() == 0) {
            return taxComponent;
        }

        String whereGajiTidakRutin = " AND " + PstPayComponent.fieldNames[PstPayComponent.FLD_TAX_ITEM]
                + " IN ( " + PstPayComponent.TUNJANGAN + ", " + PstPayComponent.BONUS_THR + ") ";

        double gajiNonRutinThisPeriod = 0;
        if (employee.getResigned() == 1) { //just check if the gaji non rutin exist
            gajiNonRutinThisPeriod = PstPaySlip.getSumSalaryBenefit(payPeriod.getOID(), employeeId, whereGajiTidakRutin);
        }

        try {
            String sWithout = "";
            String sComps = "";
            if (withoutComponents != null && withoutComponents.size() > 0) {
                sWithout = " AND (";
                for (int idx = 0; idx < withoutComponents.size(); idx++) {
                    String sCode = (String) withoutComponents.get(idx);
                    sWithout = sWithout + " COMP." + PstPaySlipComp.fieldNames[PstPaySlipComp.FLD_COMP_CODE] + " = \"" + sCode + "\" "
                            + (idx < (withoutComponents.size() - 1) ? ") AND  " : "");
                    sComps = sComps + sCode + ";";
                }
                sWithout = sWithout + ") ";
            }

            if (employee.getResignedDate() == null && employee.getResigned() == 1) {
                employee.setResignedDate(payPeriod.getEndDate());
            }
            /*
             if( employee.getResignedDate()!=null && employee.getResigned() == 1) {
             employee.getResignedDate().setHours(0);
             employee.getResignedDate().setMinutes(0);
             employee.getResignedDate().setSeconds(0);
             payPeriod.getEndDate().setHours(0);
             payPeriod.getEndDate().setMinutes(0);
             payPeriod.getEndDate().setSeconds(0);}*/
            //untuk karyawn yang resign check apakah tanggal resign cocok perperiode ini
            if (employee.getResigned() == 0 || (employee.getResigned() == 1 && employee.getResignedDate().after(payPeriod.getEndDate()))) {
                ///String where = PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID] + "=" + employeeId;
                //String order = PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP];
                //Vector<FamilyMember> familymember = PstFamilyMember.list(0, 0, where, order);                        

                String whereGajiRutin = " AND " + PstPayComponent.fieldNames[PstPayComponent.FLD_TAX_ITEM] + " = " + PstPayComponent.GAJI;

                double gajiRutinYearToPrevPeriod = prevPeriod != null && prevPeriod.getEndDate().getYear() == payPeriod.getEndDate().getYear() ? PstPaySlip.getSumSalaryYearToPeriod(prevPeriod.getOID(), employeeId, whereGajiRutin) : 0.0d; // gaji tetap dari awal tahun sampai periode sebelumnya, dalam tahun yg sama               
                double gajiRutinOfPeriod = 0;

                if (backPeriod < 0) {
                    backPeriod = 0;
                }

                if (backPeriod == 0) {
                    gajiRutinOfPeriod = PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, whereGajiRutin);  // gaji tetap dari di periode terhitung
                } else { //backPeriod > 0                    
                    gajiRutinOfPeriod = PstPaySlip.getSumSalaryOfPeriod(prevPeriod.getOID(), employeeId, whereGajiRutin); // gaji tetap dari di periode terhitung di asumsikan = gaji rutin periode sebelumnya
                }

                taxComponent.setGajiRutinYearToPrevPeriod(gajiRutinYearToPrevPeriod);
                taxComponent.setGajiRutinOfPeriod(gajiRutinOfPeriod);
                //pajak.setHtmlDetailCalc("<table><tr><td>A. " + GAJI_RUTIN_YEAR_TO_PREV_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(gajiRutinYearToPrevPeriod, FORMAT_DECIMAL) + "</td></tr>");
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>B. " + GAJI_RUTIN_THIS_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(gajiRutinOfPeriod, FORMAT_DECIMAL) + "</td></tr>");

                int jumlahPeriodeKedepan = 0; //  hitung dari awal tahun            

              //  if (payPeriod.getPayProcDateClose() != null) {
             //       jumlahPeriodeKedepan = 12 - payPeriod.getPayProcDateClose().getMonth();
             //   } else {
                    jumlahPeriodeKedepan = 12 - payPeriod.getEndDate().getMonth();
             //   }
                taxComponent.setJumlahPeriodeKedepan(jumlahPeriodeKedepan);

                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>C. " + NUMBER_OF_NEXT_PERIOD + "</td><td>:</td><td>" + jumlahPeriodeKedepan + "</td></tr>");

                double gajiRutinSetahun = gajiRutinYearToPrevPeriod + gajiRutinOfPeriod * jumlahPeriodeKedepan;
                taxComponent.setGajiRutinSetahun(gajiRutinSetahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>D. " + TOTAL_OF_CONTINUE_SALARY + "=A+BxC</td><td>:</td><td>" + Formater.formatNumber(gajiRutinSetahun, FORMAT_DECIMAL) + "</td></tr>");

                whereGajiTidakRutin = " AND " + PstPayComponent.fieldNames[PstPayComponent.FLD_TAX_ITEM]
                        + " IN ( " + PstPayComponent.TUNJANGAN + ", " + PstPayComponent.BONUS_THR + ") ";

                double gajiNonRutinYearToPeriod = PstPaySlip.getSumSalaryYearToPeriod(payPeriod.getOID(), employeeId, whereGajiTidakRutin);

                taxComponent.setGajiNonRutinYearToPeriod(gajiNonRutinYearToPeriod);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>E. " + GAJI_NON_RUTIN_YTD + "</td><td>:</td><td>" + Formater.formatNumber(gajiNonRutinYearToPeriod, FORMAT_DECIMAL) + "</td></tr>");

                double compGajiOfPeriodWithout = (sWithout == null || sWithout.trim().length() < 1) ? 0.0d
                        : (PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, whereGajiRutin + " " + sWithout)
                        + PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, whereGajiTidakRutin + " " + sWithout));  // total component gaji yang ppph nya sudah dipotong terpisah
                if (compGajiOfPeriodWithout != 0) {
                    taxComponent.setCompGajiOfPeriodWithout(compGajiOfPeriodWithout);
                    //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>E.1 " + GAJI_PPH_DIPOTONG_TERPISAH_THIS_PERIOD + "( " + sComps + " ) </td><td>:</td><td>(" + Formater.formatNumber(compGajiOfPeriodWithout, FORMAT_DECIMAL) + ")</td></tr>");
                }

                double gajiTotalSetahun = gajiRutinSetahun + gajiNonRutinYearToPeriod - compGajiOfPeriodWithout;

                taxComponent.setGajiTotalSetahun(gajiTotalSetahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>F. " + TOTAL_GAJI_SETAHUN + "= D + E</td><td>:</td><td>" + Formater.formatNumber(gajiTotalSetahun, FORMAT_DECIMAL) + "</td></tr>");

                int employeeTaxType = TaxCalculator.getEmployeeTypeMap(employee.getEmpCategoryId());  // dapat dari jenis karyawan                        
                double pendapatanNettoSetahun = 0.0;
                taxComponent.setEmployeeTaxType(employeeTaxType);
                switch (employeeTaxType) {
                    case TaxCalculator.EMPLOYEE_TAX_TYPE_PERMANENT:

                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + EMPLOYEE_TAX_TYPE[EMPLOYEE_TAX_TYPE_PERMANENT] + "</td></tr>");

                        //update by devin 2014-02-15                    
                        double biayaJabatan = Math.round(gajiTotalSetahun * (pajak != null && pajak.getBiayaJabatanPersen() != 0 ? pajak.getBiayaJabatanPersen() : TaxCalculator.BIAYA_JABATAN_PERSEN));
                        System.out.println("biaya jabatan = "+gajiTotalSetahun+" * "+(pajak != null && pajak.getBiayaJabatanPersen() != 0 ? pajak.getBiayaJabatanPersen() : TaxCalculator.BIAYA_JABATAN_PERSEN));

                        //update by priska karena biaya jabatan tidak boleh melebihi jumlah payslip dikali rata rata bulan dalam pajak 20151217
                        int countJumlahPaySlip = PstPaySlip.getJumlahPayslip(employeeId, ""+(payPeriod.getEndDate().getYear()+1900));
                        double nilaiMaksimum = (countJumlahPaySlip * TaxCalculator.BIAYA_JABATAN_BULANAN);
                        System.out.println("nilaiMaksimum =  "+nilaiMaksimum);
                        if (biayaJabatan > nilaiMaksimum){
                            biayaJabatan = nilaiMaksimum;
                            System.out.println("biayaJabatan > nilaiMaksimum = true ");
                        }


                        if ((biayaJabatan) <= (pajak != null && pajak.getBiayaJabatanMaksimal() != 0 ? pajak.getBiayaJabatanMaksimal() : TaxCalculator.BIAYA_JABATAN_MAX)) {
                            pendapatanNettoSetahun = gajiTotalSetahun - biayaJabatan;
                            taxComponent.setBiayaJabatan(biayaJabatan);
                            //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>H. " + TITLE_BIAYA_JABATAN + "= F x  " + (pajak != null && pajak.getBiayaJabatanPersen() != 0 ? pajak.getBiayaJabatanPersen() : BIAYA_JABATAN_PERSEN) * 100 + " %</td><td>:</td><td>" + Formater.formatNumber(biayaJabatan, FORMAT_DECIMAL) + "</td></tr>");
                        } else {
                            pendapatanNettoSetahun = gajiTotalSetahun - (pajak != null && pajak.getBiayaJabatanMaksimal() != 0 ? pajak.getBiayaJabatanMaksimal() : TaxCalculator.BIAYA_JABATAN_MAX);
                            if (pajak != null && pajak.getBiayaJabatanMaksimal() != 0 ) {
                                taxComponent.setBiayaJabatan(pajak.getBiayaJabatanMaksimal());
                            } else {
                                taxComponent.setBiayaJabatan(TaxCalculator.BIAYA_JABATAN_MAX);
                            }
                            //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>H. " + TITLE_BIAYA_JABATAN + "= F x  " + (pajak != null && pajak.getBiayaJabatanPersen() != 0 ? pajak.getBiayaJabatanPersen() : BIAYA_JABATAN_PERSEN) * 100 + " %</td><td>:</td><td>" + Formater.formatNumber((pajak != null && pajak.getBiayaJabatanMaksimal() != 0 ? pajak.getBiayaJabatanMaksimal() : BIAYA_JABATAN_MAX), FORMAT_DECIMAL) + "</td></tr>");
                        }

                        break;

                    case TaxCalculator.EMPLOYEE_TAX_TYPE_NON_PERMANENT:
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + EMPLOYEE_TAX_TYPE[EMPLOYEE_TAX_TYPE_NON_PERMANENT] + "</td></tr>");

                        pendapatanNettoSetahun = gajiTotalSetahun;
                        break;

                    case TaxCalculator.EMPLOYEE_TAX_TYPE_CONSULTANT:
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + EMPLOYEE_TAX_TYPE[EMPLOYEE_TAX_TYPE_CONSULTANT] + "</td></tr>");

                        //update by devin 2014-02-15
                        pendapatanNettoSetahun = (pajak != null && pajak.getNettoPercenKonsultan() != 0 ? pajak.getNettoPercenKonsultan() : TaxCalculator.NETTO_PERCENT_CONSULTANT) * gajiTotalSetahun;
                        break;
                    default:
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + NON_SPECIFIC_EMPLOYEE_TYPE + "</td></tr>");

                        pendapatanNettoSetahun = gajiTotalSetahun;
                }

                taxComponent.setPendapatanNettoSetahun(pendapatanNettoSetahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>I. " + TITLE_NETTO_GAJI_SETAHUN + "</td><td>:</td><td>" + Formater.formatNumber(pendapatanNettoSetahun, FORMAT_DECIMAL) + "</td></tr>");

                String wherePotonganGaji = " AND " + PstPayComponent.fieldNames[PstPayComponent.FLD_TAX_ITEM] + " = " + PstPayComponent.POTONGAN_GAJI;

                double potonganGajiYearToPrevPeriod = prevPeriod != null && prevPeriod.getEndDate().getYear() == payPeriod.getEndDate().getYear() ? PstPaySlip.getSumSalaryYearToPeriod(prevPeriod.getOID(), employeeId, wherePotonganGaji) : 0.0d; // potongan gaji dari awal tahun sampai periode sebelumnya, dalam tahun yg sama
                double potonganGajiOfPeriod = PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, wherePotonganGaji);  // potongan gaji dari di periode terhitung

                if (backPeriod == 0) {
                    potonganGajiOfPeriod = PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, wherePotonganGaji);  // gaji tetap dari di periode terhitung
                } else { //backPeriod > 0                    
                    potonganGajiOfPeriod = PstPaySlip.getSumSalaryOfPeriod(prevPeriod.getOID(), employeeId, wherePotonganGaji); // gaji tetap dari di periode terhitung di asumsikan = gaji rutin periode sebelumnya
                }

                double totalPotongan = Math.abs(potonganGajiYearToPrevPeriod) + Math.abs(potonganGajiOfPeriod) * jumlahPeriodeKedepan;// jumlahPeriodeKedepan; // update by Pak Tut 15 Dec 2015

                taxComponent.setPotonganGajiYearToPrevPeriod(potonganGajiYearToPrevPeriod);
                taxComponent.setPotonganGajiOfPeriod(potonganGajiOfPeriod);
                if (totalPotongan <= TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX){
                    taxComponent.setTotalPotongan(totalPotongan);
                } else {
                    taxComponent.setTotalPotongan(TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX);
                }
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>J. " + TITLE_DEDUCTION_YEAR_TO_PREV_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(potonganGajiYearToPrevPeriod, FORMAT_DECIMAL) + "</td></tr>");
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>K. " + TITLE_DEDUCTION_THIS_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(potonganGajiOfPeriod, FORMAT_DECIMAL) + "</td></tr>");
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>L. " + TITLE_TOTAL_DEDUCTION_A_YEAR /*+" ( Max ="+Formater.formatNumber(POTONGAN_GAJI_KENA_PAJAK_MAX, FORMAT_DECIMAL)+")"*/ + "</td><td>:</td><td>" + Formater.formatNumber((totalPotongan <= POTONGAN_GAJI_KENA_PAJAK_MAX ? totalPotongan : POTONGAN_GAJI_KENA_PAJAK_MAX), FORMAT_DECIMAL) + "</td></tr>");

                if (totalPotongan <= TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX) {
                    pendapatanNettoSetahun = pendapatanNettoSetahun - totalPotongan;
                } else {
                    pendapatanNettoSetahun = pendapatanNettoSetahun - TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX;
                }

                taxComponent.setPendapatanNettoSetahun(pendapatanNettoSetahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>M. " + TITLE_NETTO_GAJI_SETAHUN_MIN_DEDUC + "</td><td>:</td><td>" + Formater.formatNumber(pendapatanNettoSetahun, FORMAT_DECIMAL) + "</td></tr>");

                Marital marital = null;
                try {
                    if (employee.getTaxMaritalId() != 0) {
                        marital = PstMarital.fetchExc(employee.getTaxMaritalId());
                    } else {
                        marital = PstMarital.fetchExc(employee.getMaritalId());
                    }
                } catch (Exception e) {
                }

                int maritalStatus = marital == null ? TaxCalculator.STATUS_DIRI_SENDIRI : marital.getMaritalStatusTax(); // status perkawinan ambil dari status kawin pajak , default STATUS_DIRI_SENDIRI            
                PTKP ptkp = TaxCalculator.getPTKP(payPeriod.getEndDate());
                double ptkpSetahun = 0;
                switch (maritalStatus) {
                    case TaxCalculator.STATUS_DIRI_SENDIRI:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpDiriSendiri() != 0 ? pajak.getPtkpDiriSendiri() : ptkp.getPTKP_DIRI_SENDIRI());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_DIRI_SENDIRI] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN:
                        //update by devin 2014-02-15                    
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawin() != 0 ? pajak.getPtkpKawin() : ptkp.getPTKP_KAWIN());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN_ANAK_1:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawinAnak1() != 0 ? pajak.getPtkpKawinAnak1() : ptkp.getPTKP_KAWIN_ANAK_1());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN_ANAK_1] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN_ANAK_2:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawinAnak2() != 0 ? pajak.getPtkpKawinAnak2() : ptkp.getPTKP_KAWIN_ANAK_2());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN_ANAK_2] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN_ANAK_3:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawinAnak3() != 0 ? pajak.getPtkpKawinAnak3() : ptkp.getPTKP_KAWIN_ANAK_3());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN_ANAK_3] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                }

                double pkpSetahun = pendapatanNettoSetahun - ptkpSetahun;
                if (pkpSetahun < 0.00) {
                    pkpSetahun = 0.0;
                }
                pkpSetahun = Math.floor(pkpSetahun / 1000d) * 1000d;

                taxComponent.setPkpSetahun(pkpSetahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>O. " + TITLE_PKP_SETAHUN + " = M - N </td><td>:</td><td>" + Formater.formatNumber(pkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                double pph21Setahun = TaxCalculator.hitungTarifPPH21(pkpSetahun, pajak, employee.getNpwp());

                taxComponent.setPph21Setahun(pph21Setahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>P. " + TITLE_PPH21_SETAHUN + "</td><td>:</td><td>" + Formater.formatNumber(pph21Setahun, FORMAT_DECIMAL) + "</td></tr>");

                double pph21SampaiBulanSebelumnya = PstPaySlip.getSumTaxYearToPreviousPeriod(payPeriod.getOID(), employeeId);

                taxComponent.setPph21SampaiBulanSebelumnya(pph21SampaiBulanSebelumnya);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q. " + TITLE_PPH21_SDGN_PREV_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(pph21SampaiBulanSebelumnya, FORMAT_DECIMAL) + "</td></tr>");

                // update by Kartika 2015-07-23
                double pph21PrepayBulanIni = PstPaySlip.getTaxPrepayPeriod(payPeriod.getOID(), employeeId);
                if (pph21PrepayBulanIni != 0) {
                    taxComponent.setPph21PrepayBulanIni(pph21PrepayBulanIni);
                    //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q1. " + TITLE_PPH21_PREPAY_THIS_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(pph21PrepayBulanIni, FORMAT_DECIMAL) + "</td></tr>");
                }

                if (returnPeriodToEndYear) { // update by Kartika 2015-06-29
                    taxComponent.setPph21PeriodeSampaiAkhirTahun(pph21Setahun - pph21SampaiBulanSebelumnya);
                    taxComponent.setPph21KurangBayarBulanTerhitung(pph21Setahun - pph21SampaiBulanSebelumnya - pph21PrepayBulanIni);
                    //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q. " + TITLE_PPH21_THIS_PERIOD_TO_END_YEAR + " =( ( P - Q )) </td><td>:</td><td>" + Formater.formatNumber(pph21Setahun - pph21SampaiBulanSebelumnya, FORMAT_DECIMAL) + "</td></tr>");
                    listPajak.add(taxComponent);
                    return taxComponent;
                    //return pph21Setahun - pph21SampaiBulanSebelumnya - pph21PrepayBulanIni;
                }

                /*update by devin 2104-02-14 agar tidak dibulatkan  Math.round((pph21Setahun-pph21SampaiBulanSebelumnya)/jumlahPeriodeKedepan)*/
                //pph21KurangBayarBulanTerhitung = Math.floor(((double) (pph21Setahun - pph21SampaiBulanSebelumnya) / ((double) jumlahPeriodeKedepan)) - pph21PrepayBulanIni); // update by Kartika -pph21PrepayBulanIni
                //pph21KurangBayarBulanTerhitung = Math.floor(((double) (pph21Setahun - pph21SampaiBulanSebelumnya) / ((double) jumlahPeriodeKedepan))); // update by Kartika -pph21PrepayBulanIni
                if (Tax_Advance_Deduc.equals("1")){
                pph21KurangBayarBulanTerhitung = Math.floor(((double) (pph21Setahun - pph21SampaiBulanSebelumnya - pph21PrepayBulanIni) / ((double) jumlahPeriodeKedepan)));
                } else {
                pph21KurangBayarBulanTerhitung = Math.floor(((double) (pph21Setahun - pph21SampaiBulanSebelumnya) / ((double) jumlahPeriodeKedepan)));
                }
                // -  pph21SampaiBulanSebelumnya)/ (12 -jumlahBulanDihitung-1 );            

                taxComponent.setPph21KurangBayarBulanTerhitung(pph21KurangBayarBulanTerhitung);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q. " + TITLE_PPH21_THIS_PERIOD + " = ( P - Q ) / C </td><td>:</td><td>" + Formater.formatNumber(pph21KurangBayarBulanTerhitung, FORMAT_DECIMAL) + "</td></tr>");

            } else if (employee.getResigned() == 1 && (((employee.getResignedDate().after(payPeriod.getStartDate()) || (employee.getResignedDate().equals(payPeriod.getStartDate())))
                    && (employee.getResignedDate().before(payPeriod.getEndDate()) || employee.getResignedDate().equals(payPeriod.getEndDate())))
                    || (gajiNonRutinThisPeriod > 0))) { // bisa juga gajiNonRutinThisPeriod >0 untuk karyawan sdh resign diberikan  karyawan.
                // PERHITUNGAN KARYAWAN YANG RESIGN DALAM PERIODE GAJI 
                String where = PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID] + "=" + employeeId;
                String order = PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP];
                //Vector<FamilyMember> familymember = PstFamilyMember.list(0, 0, where, order);                        

                String whereGajiRutin = " AND " + PstPayComponent.fieldNames[PstPayComponent.FLD_TAX_ITEM] + " = " + PstPayComponent.GAJI;

                double gajiRutinUntilPrevPeriod = prevPeriod != null && prevPeriod.getEndDate().getYear() == payPeriod.getEndDate().getYear() ? PstPaySlip.getSumSalaryYearToPeriod(prevPeriod.getOID(), employeeId, whereGajiRutin) : 0.0d; // gaji tetap dari awal tahun sampai periode sebelumnya, dalam tahun yg sama

                if (backPeriod < 0) {
                    backPeriod = 0;
                }

                double gajiRutinOfPeriod = 0.0;
                if (backPeriod == 0) {
                    gajiRutinOfPeriod = PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, whereGajiRutin);  // gaji tetap dari di periode terhitung
                } else {
                    gajiRutinOfPeriod = PstPaySlip.getSumSalaryOfPeriod(prevPeriod.getOID(), employeeId, whereGajiRutin);  // gaji tetap dari di periode terhitung di asumsikan = gaji rutin periode sebelumnya
                }

                taxComponent.setGajiRutinYearToPrevPeriod(gajiRutinUntilPrevPeriod);
                taxComponent.setGajiRutinOfPeriod(gajiRutinOfPeriod);
                //pajak.setHtmlDetailCalc("<table><tr><td>A. " + GAJI_RUTIN_YEAR_TO_PREV_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(gajiRutinUntilPrevPeriod, FORMAT_DECIMAL) + "</td></tr>");
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>B. " + GAJI_RUTIN_THIS_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(gajiRutinOfPeriod, FORMAT_DECIMAL) + "</td></tr>");

                int jumlahPerioYTD = 0; //  hitung dari awal tahun            

                if (payPeriod.getPayProcDateClose() != null) {
                    jumlahPerioYTD = payPeriod.getPayProcDateClose().getMonth() + 1;
                } else {
                    jumlahPerioYTD = payPeriod.getEndDate().getMonth();
                }
                taxComponent.setJumlahPeriodeKedepan(jumlahPerioYTD);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>C. " + NUMBER_OF_MONTH_YTD + "</td><td>:</td><td>" + jumlahPerioYTD + "</td></tr>");

                double gajiRutinSdgnResign = gajiRutinUntilPrevPeriod + gajiRutinOfPeriod;

                taxComponent.setGajiRutinSetahun(gajiRutinSdgnResign);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>D. " + TOTAL_OF_CONTINUE_SALARY + "=A+B</td><td>:</td><td>" + Formater.formatNumber(gajiRutinSdgnResign, FORMAT_DECIMAL) + "</td></tr>");

                whereGajiTidakRutin = " AND " + PstPayComponent.fieldNames[PstPayComponent.FLD_TAX_ITEM]
                        + " IN ( " + PstPayComponent.TUNJANGAN + ", " + PstPayComponent.BONUS_THR + ") ";
                double gajiNonRutinYearToPeriod = PstPaySlip.getSumSalaryYearToPeriod(payPeriod.getOID(), employeeId, whereGajiTidakRutin);

                taxComponent.setGajiNonRutinYearToPeriod(gajiNonRutinYearToPeriod);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>E. " + GAJI_NON_RUTIN_YTD + "</td><td>:</td><td>" + Formater.formatNumber(gajiNonRutinYearToPeriod, FORMAT_DECIMAL) + "</td></tr>");

                double compGajiOfPeriodWithout = (sWithout == null || sWithout.trim().length() < 1) ? 0.0d : PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, whereGajiRutin + " " + sWithout);  // total component gaji yang ppph nya sudah dipotong terpisah // update by Pak Tut 14 Dec 2015
                if (compGajiOfPeriodWithout != 0) {
                    taxComponent.setCompGajiOfPeriodWithout(compGajiOfPeriodWithout);
                    //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>E.1 " + GAJI_PPH_DIPOTONG_TERPISAH_THIS_PERIOD + "( " + sComps + " ) </td><td>:</td><td>(" + Formater.formatNumber(compGajiOfPeriodWithout, FORMAT_DECIMAL) + ")</td></tr>");
                }

                double gajiTotalSdnPeriode = gajiRutinSdgnResign + gajiNonRutinYearToPeriod - compGajiOfPeriodWithout;

                taxComponent.setGajiTotalSetahun(gajiTotalSdnPeriode);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>F. " + TOTAL_GAJI_SDGN_PERIODE + "= D + E</td><td>:</td><td>" + Formater.formatNumber(gajiTotalSdnPeriode, FORMAT_DECIMAL) + "</td></tr>");

                int employeeTaxType = TaxCalculator.getEmployeeTypeMap(employee.getEmpCategoryId());  // dapat dari jenis karyawan                        
                double pendapatanNettoSdgnPeriode = 0.0;

                taxComponent.setEmployeeTaxType(employeeTaxType);
                switch (employeeTaxType) {
                    case TaxCalculator.EMPLOYEE_TAX_TYPE_PERMANENT:
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + EMPLOYEE_TAX_TYPE[EMPLOYEE_TAX_TYPE_PERMANENT] + "</td></tr>");

                        //update by devin 2014-02-15                    
                        double biayaJabatan = Math.round(gajiTotalSdnPeriode * (pajak != null && pajak.getBiayaJabatanPersen() != 0 ? pajak.getBiayaJabatanPersen() : TaxCalculator.BIAYA_JABATAN_PERSEN));

                        if ((biayaJabatan) <= (((pajak != null && pajak.getBiayaJabatanMaksimal() != 0 ? pajak.getBiayaJabatanMaksimal() : TaxCalculator.BIAYA_JABATAN_MAX)/ 12f) * ((float) jumlahPerioYTD ))) {
                            pendapatanNettoSdgnPeriode = gajiTotalSdnPeriode - biayaJabatan;
                            taxComponent.setBiayaJabatan(biayaJabatan);
                            //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>H. " + TITLE_BIAYA_JABATAN + "= F x  " + (pajak != null && pajak.getBiayaJabatanPersen() != 0 ? pajak.getBiayaJabatanPersen() : BIAYA_JABATAN_PERSEN) * 100 + " %</td><td>:</td><td>" + Formater.formatNumber(biayaJabatan, FORMAT_DECIMAL) + "</td></tr>");
                        } else {
                            pendapatanNettoSdgnPeriode = gajiTotalSdnPeriode - ((pajak != null && pajak.getBiayaJabatanMaksimal() != 0 ? pajak.getBiayaJabatanMaksimal() : TaxCalculator.BIAYA_JABATAN_MAX) * ((float) jumlahPerioYTD / 12f));
                            //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>H. " + TITLE_BIAYA_JABATAN + "= F x  " + (pajak != null && pajak.getBiayaJabatanPersen() != 0 ? pajak.getBiayaJabatanPersen() : BIAYA_JABATAN_PERSEN) * 100 + " %</td><td>:</td><td>" + Formater.formatNumber((((pajak != null && pajak.getBiayaJabatanMaksimal() != 0 ? pajak.getBiayaJabatanMaksimal() : BIAYA_JABATAN_MAX)/12f) * ((float) jumlahPerioYTD )), FORMAT_DECIMAL) + "</td></tr>");
                        }

                        break;

                    case TaxCalculator.EMPLOYEE_TAX_TYPE_NON_PERMANENT:
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + EMPLOYEE_TAX_TYPE[EMPLOYEE_TAX_TYPE_NON_PERMANENT] + "</td></tr>");

                        pendapatanNettoSdgnPeriode = gajiTotalSdnPeriode;
                        break;

                    case TaxCalculator.EMPLOYEE_TAX_TYPE_CONSULTANT:
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + EMPLOYEE_TAX_TYPE[EMPLOYEE_TAX_TYPE_CONSULTANT] + "</td></tr>");

                        //update by devin 2014-02-15
                        pendapatanNettoSdgnPeriode = (pajak != null && pajak.getNettoPercenKonsultan() != 0 ? pajak.getNettoPercenKonsultan() : TaxCalculator.NETTO_PERCENT_CONSULTANT) * gajiTotalSdnPeriode;
                        break;
                    default:
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>G. " + EMPLOYEE_TYPE + "</td><td>:</td><td>" + NON_SPECIFIC_EMPLOYEE_TYPE + "</td></tr>");

                        pendapatanNettoSdgnPeriode = gajiTotalSdnPeriode;
                }
                taxComponent.setPendapatanNettoSetahun(pendapatanNettoSdgnPeriode);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>I. " + TITLE_NETTO_GAJI_SETAHUN + "</td><td>:</td><td>" + Formater.formatNumber(pendapatanNettoSdgnPeriode, FORMAT_DECIMAL) + "</td></tr>");

                String wherePotonganGaji = " AND " + PstPayComponent.fieldNames[PstPayComponent.FLD_TAX_ITEM] + " = " + PstPayComponent.POTONGAN_GAJI;

                double potonganGajiUntilPrevPeriod = prevPeriod != null && prevPeriod.getEndDate().getYear() == payPeriod.getEndDate().getYear() ? PstPaySlip.getSumSalaryYearToPeriod(prevPeriod.getOID(), employeeId, wherePotonganGaji) : 0.0d; // potongan gaji dari awal tahun sampai periode sebelumnya, dalam tahun yg sama
                double potonganGajiOfPeriod = PstPaySlip.getSumSalaryOfPeriod(payPeriod.getOID(), employeeId, wherePotonganGaji);  // potongan gaji dari di periode terhitung
                double totalPotongan = Math.abs(potonganGajiUntilPrevPeriod) + Math.abs(potonganGajiOfPeriod) ;//* (12-jumlahPerioYTD); // update by Pak Tut 15 Dec 2015

                taxComponent.setPotonganGajiYearToPrevPeriod(potonganGajiUntilPrevPeriod);
                taxComponent.setPotonganGajiOfPeriod(potonganGajiOfPeriod);
                if (totalPotongan <= TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX){
                    taxComponent.setTotalPotongan(totalPotongan);
                } else {
                    taxComponent.setTotalPotongan(TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX);
                }
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>J. " + TITLE_DEDUCTION_YEAR_TO_PREV_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(potonganGajiUntilPrevPeriod, FORMAT_DECIMAL) + "</td></tr>");
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>K. " + TITLE_DEDUCTION_THIS_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(potonganGajiOfPeriod, FORMAT_DECIMAL) + "</td></tr>");
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>L. " + TITLE_TOTAL_DEDUCTION_A_YEAR /*+" ( Max ="+Formater.formatNumber(POTONGAN_GAJI_KENA_PAJAK_MAX, FORMAT_DECIMAL)+")"*/ + "</td><td>:</td><td>" + Formater.formatNumber((totalPotongan <= POTONGAN_GAJI_KENA_PAJAK_MAX ? totalPotongan : POTONGAN_GAJI_KENA_PAJAK_MAX), FORMAT_DECIMAL) + "</td></tr>");

                if (totalPotongan <= TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX) {
                    pendapatanNettoSdgnPeriode = pendapatanNettoSdgnPeriode - totalPotongan;
                } else {
                    pendapatanNettoSdgnPeriode = pendapatanNettoSdgnPeriode - TaxCalculator.POTONGAN_GAJI_KENA_PAJAK_MAX;
                }

                taxComponent.setPendapatanNettoSetahunSetelahPotongan(pendapatanNettoSdgnPeriode);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>M. " + TITLE_NETTO_GAJI_SETAHUN_MIN_DEDUC + "</td><td>:</td><td>" + Formater.formatNumber(pendapatanNettoSdgnPeriode, FORMAT_DECIMAL) + "</td></tr>");

                Marital marital = null;
                try {
                    if (employee.getTaxMaritalId() != 0) {
                        marital = PstMarital.fetchExc(employee.getTaxMaritalId());
                    } else {
                        marital = PstMarital.fetchExc(employee.getMaritalId());
                    }
                } catch (Exception e) {
                }

                int maritalStatus = marital == null ? TaxCalculator.STATUS_DIRI_SENDIRI : marital.getMaritalStatusTax(); // status perkawinan ambil dari status kawin pajak , default STATUS_DIRI_SENDIRI            
                PTKP ptkp = TaxCalculator.getPTKP(payPeriod.getEndDate());
                double ptkpSetahun = 0;
                switch (maritalStatus) {
                    case TaxCalculator.STATUS_DIRI_SENDIRI:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpDiriSendiri() != 0 ? pajak.getPtkpDiriSendiri() : ptkp.getPTKP_DIRI_SENDIRI());

                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_DIRI_SENDIRI] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN:
                        //update by devin 2014-02-15                    
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawin() != 0 ? pajak.getPtkpKawin() : ptkp.getPTKP_KAWIN());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN_ANAK_1:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawinAnak1() != 0 ? pajak.getPtkpKawinAnak1() : ptkp.getPTKP_KAWIN_ANAK_1());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN_ANAK_1] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN_ANAK_2:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawinAnak2() != 0 ? pajak.getPtkpKawinAnak2() : ptkp.getPTKP_KAWIN_ANAK_2());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN_ANAK_2] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                    case TaxCalculator.STATUS_KAWIN_ANAK_3:
                        //update by devin 2014-02-15
                        ptkpSetahun = (pajak != null && pajak.getPtkpKawinAnak3() != 0 ? pajak.getPtkpKawinAnak3() : ptkp.getPTKP_KAWIN_ANAK_3());
                        taxComponent.setPtkpSetahun(ptkpSetahun);
                        //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>N. " + TITLE_PTKP_SETAHUN + "(Status =" + STATUS_EMPLOYEE[STATUS_KAWIN_ANAK_3] + ")</td><td>:</td><td>" + Formater.formatNumber(ptkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                        break;
                }

                double pkpSetahun = pendapatanNettoSdgnPeriode - ptkpSetahun;
                if (pkpSetahun < 0.00) {
                    pkpSetahun = 0.0;
                }
                pkpSetahun = Math.floor(pkpSetahun / 1000d) * 1000d;

                taxComponent.setPkpSetahun(pkpSetahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>O. " + TITLE_PKP_SETAHUN + " = M - N </td><td>:</td><td>" + Formater.formatNumber(pkpSetahun, FORMAT_DECIMAL) + "</td></tr>");

                double pph21Setahun = TaxCalculator.hitungTarifPPH21(pkpSetahun, pajak, employee.getNpwp());

                taxComponent.setPph21Setahun(pph21Setahun);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>P. " + TITLE_PPH21_SETAHUN + "</td><td>:</td><td>" + Formater.formatNumber(pph21Setahun, FORMAT_DECIMAL) + "</td></tr>");

                double pph21SampaiBulanSebelumnya = PstPaySlip.getSumTaxYearToPreviousPeriod(payPeriod.getOID(), employeeId);
                taxComponent.setPph21SampaiBulanSebelumnya(pph21SampaiBulanSebelumnya);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q. " + TITLE_PPH21_SDGN_PREV_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(pph21SampaiBulanSebelumnya, FORMAT_DECIMAL) + "</td></tr>");

                // update by Kartika 2015-07-23
                double pph21PrepayBulanIni = PstPaySlip.getTaxPrepayPeriod(payPeriod.getOID(), employeeId);
                if (pph21PrepayBulanIni != 0) {
                    taxComponent.setPph21PrepayBulanIni(pph21PrepayBulanIni);
                    //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q1. " + TITLE_PPH21_PREPAY_THIS_PERIOD + "</td><td>:</td><td>" + Formater.formatNumber(pph21PrepayBulanIni, FORMAT_DECIMAL) + "</td></tr>");
                }

                if (returnPeriodToEndYear) { // update by Kartika 2015-06-29
                    //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q. " + TITLE_PPH21_THIS_PERIOD_TO_END_YEAR + " = ( P - Q ) </td><td>:</td><td>" + Formater.formatNumber(pph21Setahun - pph21SampaiBulanSebelumnya, FORMAT_DECIMAL) + "</td></tr>");
                    taxComponent.setPph21PeriodeSampaiAkhirTahun(pph21Setahun - pph21SampaiBulanSebelumnya);
                    taxComponent.setPph21KurangBayarBulanTerhitung(pph21Setahun - pph21SampaiBulanSebelumnya);
                    listPajak.add(taxComponent);
                }

                /*update by devin 2104-02-14 agar tidak dibulatkan  Math.round((pph21Setahun-pph21SampaiBulanSebelumnya)/jumlahPeriodeKedepan)*/
                // pph21KurangBayarBulanTerhitung = Math.floor((double) (pph21Setahun - pph21SampaiBulanSebelumnya) / ((double) jumlahPerioYTD));
                /* baris code di atas pph21KurangBayarBulanTerhitung diganti dengn code di bawah*/
                //pph21KurangBayarBulanTerhitung = Math.floor((double) (pph21Setahun - pph21SampaiBulanSebelumnya - pph21PrepayBulanIni));
                if (Tax_Advance_Deduc.equals("1")){
                pph21KurangBayarBulanTerhitung = Math.floor(((double) (pph21Setahun - pph21SampaiBulanSebelumnya - pph21PrepayBulanIni) / ((double) jumlahPerioYTD)));
                } else {
                    if (employee.getResigned() == 1){
                        pph21KurangBayarBulanTerhitung = Math.floor(((double) (pph21Setahun - pph21SampaiBulanSebelumnya)));
                    } else {
                        pph21KurangBayarBulanTerhitung = Math.floor(((double) (pph21Setahun - pph21SampaiBulanSebelumnya) / ((double) jumlahPerioYTD)));
                    }
                }
                // -  pph21SampaiBulanSebelumnya)/ (12 -jumlahBulanDihitung-1 );            

                taxComponent.setPph21KurangBayarBulanTerhitung(pph21KurangBayarBulanTerhitung);
                listPajak.add(taxComponent);
                //pajak.setHtmlDetailCalc(pajak.getHtmlDetailCalc() + "<tr><td>Q. " + TITLE_PPH21_THIS_PERIOD + " = P - Q - Q1 </td><td>:</td><td>" + Formater.formatNumber(pph21KurangBayarBulanTerhitung, FORMAT_DECIMAL) + "</td></tr>");

                //updateSlipComp(payPeriod.getOID(), employeeId, pph21KurangBayarBulanTerhitung);
            }

        } catch (Exception exc) {
            System.out.println(exc);
        }

    return taxComponent;
}
    
}
