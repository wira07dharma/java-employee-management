/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.report.payroll;

import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.harisma.entity.payroll.PayPeriod;
import com.dimata.harisma.entity.payroll.PstPayPeriod;
import com.dimata.harisma.entity.payroll.TaxComponent;
import com.dimata.harisma.session.payroll.Pajak;
import com.dimata.harisma.session.payroll.SessTaxReport;
import com.dimata.harisma.session.payroll.TaxCalculator;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.system.entity.PstSystemProperty;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author Gunadi
 */
public class TaxDetail extends HttpServlet {
    
    public static final int GAJI_RUTIN_YEAR_TO_PREV_PERIOD = 0;
    public static final int GAJI_RUTIN_THIS_PERIOD = 1;
    public static final int NUMBER_OF_NEXT_PERIOD = 2;
    public static final int TOTAL_OF_CONTINUE_SALARY = 3;
    public static final int GAJI_NON_RUTIN_YTD = 4;
    public static final int TOTAL_GAJI_SETAHUN = 5;
    public static final int EMPLOYEE_TYPE = 6;
    public static final int TITLE_BIAYA_JABATAN = 7;
    public static final int TITLE_NETTO_GAJI_SETAHUN = 8;
    public static final int TITLE_DEDUCTION_YEAR_TO_PREV_PERIOD = 9;
    public static final int TITLE_DEDUCTION_THIS_PERIOD = 10;
    public static final int TITLE_TOTAL_DEDUCTION_A_YEAR = 11;
    public static final int TITLE_NETTO_GAJI_SETAHUN_MIN_DEDUC = 12;
    public static final int TITLE_PTKP_SETAHUN = 13;
    public static final int TITLE_PKP_SETAHUN = 14;
    public static final int TITLE_PPH21_SETAHUN = 15;
    public static final int TITLE_PPH21_SDGN_PREV_PERIOD = 16;
    public static final int TITLE_PPH21_PREPAY_THIS_PERIOD = 17;
    public static final int TITLE_PPH21_THIS_PERIOD_TO_END_YEAR = 18;
    public static final int TITLE_PPH21_THIS_PERIOD = 19;
    

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("---===| Excel Report |===---");
        response.setContentType("application/x-msexcel");

        HSSFWorkbook wb = new HSSFWorkbook();
        DataFormat format = wb.createDataFormat();
        HSSFSheet sheet = wb.createSheet("Tax Detail");
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);
        
        
        HSSFCellStyle formatNumber = wb.createCellStyle();
        formatNumber.setDataFormat(format.getFormat("#,##0.00"));
        
        long periodId = FRMQueryString.requestLong(request, "oidPeriod");
        long employeeId = FRMQueryString.requestLong(request, "oidEmployee");
        
        String[] tableHeaderItem = {
            "A. Gaji Rutin s/d Prev.Period", "B. Gaji Rutin Period Ini", "C. Jumlah Periode dari saat ini s/d akhir tahun",
            "D. Jumlah Gaji Rutin Setahun", "E. Gaji Non Rutin s/d Periode Ini", "F. Total Gaji Setahun", "G. Employee Type",
            "H. Biaya Jabatan", "I. Total Gaji Neto Setahun", "J. Potongan s/d prev.period", "K. Potongan Periode Ini", "L. Potongan Setahun",
            "M. Netto Gaji Setahun stlh potongan", "N. PTKP (Pendapatan Tidak Kena Pajak)Setahun", "O. PKP (Pendapatan Kena Pajak) Setahun", "P. PPH21 setahun",
            "Q. PPH21 s/d Periode Sebelumnya", "Q1. PPH21 PrePay Periode ini", "Q. PPH21 dari Periode s/d Akhir Tahun", "Q. PPH21 Periode ini"
        };
        
        if (periodId != 0 && employeeId != 0){
            PayPeriod payPeriod = new PayPeriod();
            try {
                payPeriod = PstPayPeriod.fetchExc(periodId);
            } catch (Exception exc){}
            Calendar cal = Calendar.getInstance();
            cal.setTime(payPeriod.getEndDate());
            int year = cal.get(Calendar.YEAR);
            String wherePeriod = PstPayPeriod.fieldNames[PstPayPeriod.FLD_END_DATE]+ " LIKE '%" +year+ "%' ";
            String orderPeriod = PstPayPeriod.fieldNames[PstPayPeriod.FLD_START_DATE];
            Vector listPeriod = PstPayPeriod.list(0, 0, wherePeriod, orderPeriod);
            
            Employee employee = new Employee();
            try {
                employee = PstEmployee.fetchExc(employeeId);
            } catch (Exception exc){}
            
            Department dept = new Department();
            try {
                dept = PstDepartment.fetchExc(employee.getDepartmentId());
            } catch (Exception exc){}
            
            cell.setCellValue("Employee Name");
            cell = row.createCell((short) 1);
            cell.setCellValue(employee.getFullName());
            sheet.addMergedRegion(new CellRangeAddress(
                    0, //first row (0-based)
                    0, //last row  (0-based)
                    1, //first column (0-based)
                    4  //last column  (0-based)
            ));
            
            row = sheet.createRow((short) 1);
            cell = row.createCell((short) 0);
            cell.setCellValue("Employee Number");
            cell = row.createCell((short) 1);
            cell.setCellValue(employee.getEmployeeNum());
            sheet.addMergedRegion(new CellRangeAddress(
                    1, //first row (0-based)
                    1, //last row  (0-based)
                    1, //first column (0-based)
                    4  //last column  (0-based)
            ));
            
            row = sheet.createRow((short) 2);
            cell = row.createCell((short) 0);
            cell.setCellValue("Department");
            cell = row.createCell((short) 1);
            cell.setCellValue(dept.getDepartment());
            sheet.addMergedRegion(new CellRangeAddress(
                    2, //first row (0-based)
                    2, //last row  (0-based)
                    1, //first column (0-based)
                    4  //last column  (0-based)
            ));
            
            Pajak pajak = new Pajak(); 
                                                         
            //update by priska
            try {
               double ptkpDiriSendiri = 0;
               double ptkpKawin = 0;
               double ptkpAnak1 = 0;
               double ptkpAnak2 = 0;
               double ptkpAnak3 = 0;
               double jbtanMax = 0;
               double jabatanPersen = 0;
               double nettoPercenKonsultan = 0;
               double percen1 = 0;
               double percen2 = 0;
               double percen3 = 0;
               double percen4 = 0;
               double range1 = 0;
               double range2 = 0;
               double range3 = 0;
               try {
                   ptkpDiriSendiri = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_DIRI_SENDIRI").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_PTKP_DIRI_SENDIRI"));
                   ptkpKawin = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN"));
                   ptkpAnak1 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_1").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_1"));
                   ptkpAnak2 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_2").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_2"));
                   ptkpAnak3 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_3").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_PTKP_KAWIN_ANAK_3"));
                   jbtanMax = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_BIAYA_JABATAN_MAX").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_BIAYA_JABATAN_MAX"));
                   jabatanPersen = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_BIAYA_JABATAN_PERSEN").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_BIAYA_JABATAN_PERSEN"));
                   nettoPercenKonsultan = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_NETTO_PERCENT_CONSULTANT").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_NETTO_PERCENT_CONSULTANT"));
                   percen1 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_percen_1").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_percen_1"));
                   percen2 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_percen_2").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_percen_2"));
                   percen3 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_percen_3").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_percen_3"));
                   percen4 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_percen_4").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_percen_4"));
                   range1 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_range_1").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_range_1"));
                   range2 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_range_2").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_range_2"));
                   range3 = Double.parseDouble(PstSystemProperty.getValueByName("PAYROLL_range_3").equalsIgnoreCase(PstSystemProperty.SYS_NOT_INITIALIZED) ? "0" : PstSystemProperty.getValueByName("PAYROLL_range_3"));
               } catch (Exception exc) {

               }
               pajak.setPtkpDiriSendiri(ptkpDiriSendiri);
               pajak.setPtkpKawin(ptkpKawin);
               pajak.setPtkpKawinAnak1(ptkpAnak1);
               pajak.setPtkpKawinAnak2(ptkpAnak2);
               pajak.setPtkpKawinAnak3(ptkpAnak3);
               pajak.setBiayaJabatanMaksimal(jbtanMax);
               pajak.setBiayaJabatanPersen(jabatanPersen);
               pajak.setNettoPercenKonsultan(nettoPercenKonsultan);
               pajak.setPercen1(percen1);
               pajak.setPercen2(percen2);
               pajak.setPercen3(percen3);
               pajak.setPercen4(percen4);
               pajak.setRange1(range1);
               pajak.setRange2(range2);
               pajak.setRange3(range3);

            } catch (Exception exc) {

            }

               if (employee.getNpwp().equals("000000000000000")){
                    pajak = new Pajak();
               } 
            
            if (listPeriod.size()>0){
                row = sheet.createRow((short) (3));
                cell = row.createCell((short) 0);
                for (int i=0; i < listPeriod.size(); i++){
                    PayPeriod pPeriod = (PayPeriod) listPeriod.get(i);
                    cell = row.createCell((short) i+1);
                    cell.setCellValue(pPeriod.getPeriod());
                }
                
                for (int x=0; x< tableHeaderItem.length;x++){
                    row = sheet.createRow((short) (x+6));
                    cell = row.createCell((short) 0);
                    cell.setCellValue(tableHeaderItem[x]);
                    for (int i=0; i < listPeriod.size(); i++){
                        PayPeriod pPeriod = (PayPeriod) listPeriod.get(i);
                        PayPeriod prevPeriod = PstPayPeriod.getPreviousPeriod(pPeriod.getOID()); 
                        TaxComponent taxComponent = SessTaxReport.calcSalaryTax(employeeId, pPeriod, prevPeriod, pajak, null, false, 0);
                        cell = row.createCell((short) i+1);
                        switch (x){
                            case GAJI_RUTIN_YEAR_TO_PREV_PERIOD :
                                cell.setCellValue(taxComponent.getGajiRutinYearToPrevPeriod());
                                cell.setCellStyle(formatNumber);
                                break;
                            case GAJI_RUTIN_THIS_PERIOD :
                                cell.setCellValue(taxComponent.getGajiRutinOfPeriod());
                                cell.setCellStyle(formatNumber);
                                break;
                            case NUMBER_OF_NEXT_PERIOD :
                                cell.setCellValue(taxComponent.getJumlahPeriodeKedepan());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TOTAL_OF_CONTINUE_SALARY :
                                cell.setCellValue(taxComponent.getGajiRutinSetahun());
                                cell.setCellStyle(formatNumber);
                                break;
                            case GAJI_NON_RUTIN_YTD :
                                cell.setCellValue(taxComponent.getGajiNonRutinYearToPeriod());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TOTAL_GAJI_SETAHUN :
                                cell.setCellValue(taxComponent.getGajiTotalSetahun());
                                cell.setCellStyle(formatNumber);
                                break;
                            case EMPLOYEE_TYPE :
                                cell.setCellValue(TaxCalculator.EMPLOYEE_TAX_TYPE[taxComponent.getEmployeeTaxType()]);
                                //cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_BIAYA_JABATAN :
                                cell.setCellValue(taxComponent.getBiayaJabatan());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_NETTO_GAJI_SETAHUN :
                                cell.setCellValue(taxComponent.getPendapatanNettoSetahun());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_DEDUCTION_YEAR_TO_PREV_PERIOD :
                                cell.setCellValue(taxComponent.getPotonganGajiYearToPrevPeriod());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_DEDUCTION_THIS_PERIOD :
                                cell.setCellValue(taxComponent.getPotonganGajiOfPeriod());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_TOTAL_DEDUCTION_A_YEAR :
                                cell.setCellValue(taxComponent.getTotalPotongan());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_NETTO_GAJI_SETAHUN_MIN_DEDUC :
                                cell.setCellValue(taxComponent.getPendapatanNettoSetahunSetelahPotongan());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_PTKP_SETAHUN :
                                cell.setCellValue(taxComponent.getPtkpSetahun());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_PKP_SETAHUN :
                                cell.setCellValue(taxComponent.getPkpSetahun());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_PPH21_SETAHUN :
                                cell.setCellValue(taxComponent.getPph21Setahun());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_PPH21_SDGN_PREV_PERIOD :
                                cell.setCellValue(taxComponent.getPph21SampaiBulanSebelumnya());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_PPH21_PREPAY_THIS_PERIOD :
                                cell.setCellValue(taxComponent.getPph21PrepayBulanIni());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_PPH21_THIS_PERIOD_TO_END_YEAR :
                                cell.setCellValue(taxComponent.getPph21PeriodeSampaiAkhirTahun());
                                cell.setCellStyle(formatNumber);
                                break;
                            case TITLE_PPH21_THIS_PERIOD :
                                cell.setCellValue(taxComponent.getPph21KurangBayarBulanTerhitung());
                                cell.setCellStyle(formatNumber);
                                break;
                            default:
                                cell.setCellValue("");
                                break;
                        }
                        
                    }
                }
                
                
                for (int i=0; i <= listPeriod.size(); i++){
                    sheet.autoSizeColumn(i);
                }
            }
            
        }
        
        ServletOutputStream sos = response.getOutputStream();
        wb.write(sos);
        sos.close();
        
        
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
