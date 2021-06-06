/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.report.payroll;

import com.dimata.harisma.entity.attendance.AttendanceReportDaily;
import com.dimata.harisma.entity.attendance.Presence;
import com.dimata.harisma.entity.attendance.PstPresence;
import com.dimata.harisma.entity.employee.EmpSalary;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.leave.LeaveOidSym;
import com.dimata.harisma.entity.leave.PstLeaveApplication;
import com.dimata.harisma.entity.masterdata.Company;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.Division;
import com.dimata.harisma.entity.masterdata.Level;
import com.dimata.harisma.entity.masterdata.Marital;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.PstCompany;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.harisma.entity.masterdata.PstDivision;
import com.dimata.harisma.entity.masterdata.PstReason;
import com.dimata.harisma.entity.masterdata.PstScheduleCategory;
import com.dimata.harisma.entity.masterdata.PstScheduleSymbol;
import com.dimata.harisma.entity.masterdata.PstSection;
import com.dimata.harisma.entity.masterdata.ScheduleSymbol;
import com.dimata.harisma.entity.masterdata.Section;
import com.dimata.harisma.entity.payroll.PayComponent;
import com.dimata.harisma.entity.payroll.PayGeneral;
import com.dimata.harisma.entity.payroll.PayPeriod;
import com.dimata.harisma.entity.payroll.PstPayGeneral;
import com.dimata.harisma.entity.payroll.PstPayPeriod;
import com.dimata.harisma.report.SrcEsptMonth;
import com.dimata.harisma.session.attendance.SessEmpSchedule;
import com.dimata.harisma.session.employee.SessEmployee;
import com.dimata.harisma.session.payroll.SessESPT;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.system.entity.PstSystemProperty;
import com.dimata.util.Formater;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 *
 * @author Dimata 007
 */
public class EsptReportXLS extends HttpServlet {

    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {
    }

    private static HSSFFont createFont(HSSFWorkbook wb, int size, int color, boolean isBold) {
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) size);
        font.setColor((short) color);
        if (isBold) {
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        }
        return font;
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        System.out.println("---===| Excel Report |===---");
        response.setContentType("application/x-msexcel");

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("ESPT Monthly Report");

        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);


        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleTitle.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styleTitle.setFont(createFont(wb, 12, HSSFFont.COLOR_NORMAL /*HSSFFont.BLACK*/, true));
        // Create a row and put some cells in it. Rows are 0 based.
        // Create a row and put some cells in it. Rows are 0 based.
        //HSSFRow row = sheet.createRow((short) 0);
        //HSSFCell cell = row.createCell((short) 0);

        //update by satrya 2014-02-01
        //pemberian warna font
        CellStyle styleFont = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.RED.index);
        styleFont.setFont(font);

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);

        String printHeader = PstSystemProperty.getValueByName("PRINT_HEADER");
        long periodId = FRMQueryString.requestLong(request, "inp_period_id");
        long companyId = FRMQueryString.requestLong(request, "company_id");
        long divisionId = FRMQueryString.requestLong(request, "division_id");
        long departmentId = FRMQueryString.requestLong(request, "department_id");
        long sectionId = FRMQueryString.requestLong(request, "inp_section_id");
        long payrollGroupId = FRMQueryString.requestLong(request, "payrollGroupId");
        
        int inp_resignStatus = FRMQueryString.requestInt(request, "inp_resignStatus");
        Company company = new Company();
        Division division = new Division();
        Department department = new Department();
        Section section = new Section();
        try {
            company = PstCompany.fetchExc(companyId);
            division = PstDivision.fetchExc(divisionId);
            department = PstDepartment.fetchExc(departmentId);
            section = PstSection.fetchExc(sectionId);
        } catch(Exception e){
            System.out.println(e + " Company Structur ");
        }
        
        //set file name
        response.setHeader("Content-Disposition", "attachment; filename=Report_PPH_21.xls");
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = formatDate.format(cal.getTime());
        
        String companyName = "-" + company.getCompany();
        String divisionName = division != null && division.getDivision().length() > 0 ? division.getDivision() : "-";
        String departmenName = department != null && department.getDepartment().length() > 0 ? department.getDepartment() : "-";
        String sectionName = section != null && section.getSection().length() > 0 ? section.getSection() : "-";
        String[] tableTitle = {
            printHeader,
            "ESPT MONTHLY",
            "DATE : "+dateFormat,
            "COMPANY : " + companyName,
            "DIVISION : " + divisionName,
            "DEPARTEMENT : " + departmenName,
            "SECTION : " + sectionName
        };

        int countRow = 0;

        for (int k = 0; k < tableTitle.length; k++) {
            row = sheet.createRow((short) (countRow));
            countRow++;
            cell = row.createCell((short) 0);
            cell.setCellValue(tableTitle[k]);
            cell.setCellStyle(styleTitle);
        }



        row = sheet.createRow((short) 7);
        cell = row.createCell((short) 0);
        
        String[] tableHeaderItem = {
            "No", "Period", "Employee Num.",
            "Employee Nama", "NPWP", "Tax Code",
            "Jumlah Bruto", "Jumlah PPH21"
        };


        // Header Pertama
        //row = sheet.createRow((short) (2));
        for (int k = 0; k < tableHeaderItem.length; k++) {
            cell = row.createCell((short) k);
            cell.setCellValue(tableHeaderItem[k]);
            cell.setCellStyle(style3);
        }

        countRow = 7;
        int no = 0;
        String kodePajak = "";
        Vector listEspt = new Vector();
        String inSalBrutto =  PstSystemProperty.getValueByName("PAYROLL_ESPT_SALARY_BRUTTO_COMP") ;//'TI','ALW28', 'ALW02';         
        inSalBrutto="'"+inSalBrutto.replaceAll(",", "','")+"'";
        String inPPHBrutto =  PstSystemProperty.getValueByName("PAYROLL_ESPT_PPH_COMP");
        inPPHBrutto="'"+inPPHBrutto.replaceAll(",", "','")+"'";
        
        listEspt = SessESPT.getListEsptMonth(periodId, divisionId, departmentId, sectionId, inSalBrutto, inPPHBrutto ,inp_resignStatus,payrollGroupId );
        if (listEspt != null && listEspt.size() > 0) {
            for (int i = 0; i < listEspt.size(); i++) {
                // membuat object WarningReprimandAyat berdasarkan objectClass ke-i
                SrcEsptMonth espt = (SrcEsptMonth) listEspt.get(i);
                countRow++;
                no++;
                //countRow = countRow + addRow;
                row = sheet.createRow((short) (countRow));
                sheet.createFreezePane(7, 7);
                cell = row.createCell((short) 0);
                cell.setCellValue(no);
                cell.setCellStyle(style2);

                cell = row.createCell((short) 1);
                cell.setCellValue(espt.getPeriod());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 2);
                cell.setCellValue(espt.getEmployeeNum());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 3);
                cell.setCellValue(espt.getEmployeeName());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 4);
                cell.setCellValue(espt.getNpwp());
                cell.setCellStyle(style2);
//              
                switch (espt.getTypeForTax()) {
                    case 1:
                        kodePajak = "21-100-01";
                        break;
                    case 2:
                        kodePajak = "21-100-03";
                        break;
                    case 3:
                        kodePajak = "21-100-07";
                        break;
                    case 4:
                        kodePajak = "21-100-02";
                        break;
                    default:
                        kodePajak = "00-000-00";
                }
                cell = row.createCell((short) 5);
                cell.setCellValue(kodePajak);
                cell.setCellStyle(style2);
                cell = row.createCell((short) 6);
                cell.setCellValue(convertLong(espt.getJumlahBruto()));
                cell.setCellStyle(style2);
                cell = row.createCell((short) 7);
                cell.setCellValue(convertLong(espt.getJumlahPph()));
                cell.setCellStyle(style2);


            }
        }

        ServletOutputStream sos = response.getOutputStream();
        wb.write(sos);
        sos.close();
    }

    /* Convert Long */
    public long convertLong(double val){
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(0, RoundingMode.HALF_DOWN);
        return bDecimal.longValue();
    }
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
