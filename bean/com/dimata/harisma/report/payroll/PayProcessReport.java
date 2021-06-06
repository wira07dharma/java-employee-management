/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.report.payroll;

import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.payroll.PayEmpLevel;
import com.dimata.harisma.entity.payroll.PayPeriod;
import com.dimata.harisma.entity.payroll.PstPayEmpLevel;
import com.dimata.harisma.entity.payroll.PstPayPeriod;
import com.dimata.harisma.entity.payroll.SalaryLevel;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.system.entity.PstSystemProperty;
import com.dimata.util.Formater;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 *
 * @author Dimata 007
 */
public class PayProcessReport extends HttpServlet {

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
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        System.out.println("---===| Excel Report |===---");
        response.setContentType("application/x-msexcel");
        String printHeader = PstSystemProperty.getValueByName("PRINT_HEADER");
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = formatDate.format(cal.getTime());
        
        cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Payroll Process Report "+dateFormat);
        /* Object header */
        HSSFHeader header = sheet.getHeader();
        /* Object footer */
        HSSFFooter footer = sheet.getFooter();

        /* Row and Columns to repeat */

        
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle style3 = wb.createCellStyle();
        Font fontTitle = wb.createFont();
        fontTitle.setColor(HSSFColor.WHITE.index);
        style3.setFont(fontTitle);
        style3.setFillBackgroundColor(new HSSFColor.BLUE().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillForegroundColor(new HSSFColor.BLUE().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style3.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style3.setBorderRight(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleTitle.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        styleTitle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFillBackgroundColor(new HSSFColor.YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style4.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle style5 = wb.createCellStyle();
        style5.setFillBackgroundColor(new HSSFColor.ORANGE().getIndex());//HSSFCellStyle.WHITE);
        style5.setFillForegroundColor(new HSSFColor.ORANGE().getIndex());//HSSFCellStyle.WHITE);
        style5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style5.setBorderRight(HSSFCellStyle.BORDER_THIN);
        //pemberian warna font
        CellStyle styleFont = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.RED.index);
        styleFont.setFont(font);
        
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);
        
        String levelCode = FRMQueryString.requestString(request, "level");
        String[] SelectedValues = FRMQueryString.requestStringValues(request, "POSITION_LEVEL");
        long oidDepartment = FRMQueryString.requestLong(request,"department");
        long periodId = FRMQueryString.requestLong(request, "periodId");
        long payrollGroupId = FRMQueryString.requestLong(request, "payrollGroupId");
        Date rsgn_startdate = FRMQueryString.requestDateYYYYMMDD(request,"rsgn_startdate", "-");
        Date rsgn_enddate   = FRMQueryString.requestDateYYYYMMDD(request,"rsgn_enddate", "-");
        Date min_commencing = FRMQueryString.requestDateYYYYMMDD(request,"min_commencing", "-"); 
        String empNum = FRMQueryString.requestString(request, "emp_num");
        long sectionId = FRMQueryString.requestLong(request, "sectionId");
        long oidDivision = FRMQueryString.requestLong(request, "division");
        PayPeriod payPeriod123 = new PayPeriod();
        if (periodId != 0){
            try {
                payPeriod123 = PstPayPeriod.fetchExc(periodId);
            } catch (Exception e) {

            }

        }
        
        Vector checkValues = new Vector(1,1);
        if(SelectedValues != null && SelectedValues.length > 0){
            for (int i = 0; i < SelectedValues.length; ++i){
                checkValues.add(SelectedValues[i]);
            }
        }
        
        Vector listEmpLevel = new Vector(1, 1);
        if (periodId != 0){
           // listEmpLevel = PstPayEmpLevel.listEmpLevelwithDateResign(levelCode, checkValues, oidDepartment, null , PstEmployee.NO_RESIGN, payPeriod123.getStartDate(), payPeriod123.getEndDate(),payPeriod123.getStartDate(), payPeriod123.getEndDate(),payrollGroupId );
             //listEmpLevel = PstPayEmpLevel.listEmpLevelwithDateResign(levelCode, checkValues, oidDepartment, min_commencing , PstEmployee.NO_RESIGN, payPeriod123.getStartDate(), payPeriod123.getEndDate(),rsgn_startdate, rsgn_enddate,payrollGroupId );
             listEmpLevel = PstPayEmpLevel.listEmpLevelwithDateResign(levelCode, checkValues,oidDivision, oidDepartment, min_commencing , PstEmployee.NO_RESIGN, payPeriod123.getStartDate(), payPeriod123.getEndDate(),rsgn_startdate, rsgn_enddate,payrollGroupId,payPeriod123.getOID(),sectionId, empNum );  
        
        } else {
            listEmpLevel = PstPayEmpLevel.listEmpLevel(levelCode, checkValues, oidDepartment );
        }
        

        /* Header setting */
        String strHeader = "Company : "+printHeader;
        strHeader += "\nReport Name : Payroll Process Report Excel";
        strHeader += "\nPeriod : "+dateFormat;
        header.setLeft(strHeader);
        /* Footer setting */
        footer.setLeft("Print on : "+dateFormat+" "+sdf.format(cal.getTime()));
        footer.setRight( "Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages() );
        
        int countRow = 1;
        int cols = 0;
        /* Header No */
        row = sheet.createRow((short) countRow);
        cell = row.createCell((short) cols);
        cell.setCellValue("No");
        cell.setCellStyle(style3);
        cols = 1;
        
        cell = row.createCell((short) cols);
        cell.setCellValue("Payroll Nr");
        cell.setCellStyle(style3);
        cols++;
        
        cell = row.createCell((short) cols);
        cell.setCellValue("Full Name");
        cell.setCellStyle(style3);
        cols++;
        
        cell = row.createCell((short) cols);
        cell.setCellValue("Commencing Date");
        cell.setCellStyle(style3);
        cols++;
        
        cell = row.createCell((short) cols);
        cell.setCellValue("Salary Level");
        cell.setCellStyle(style3);
        cols++;
        
        cell = row.createCell((short) cols);
        cell.setCellValue("Start Date");
        cell.setCellStyle(style3);
        cols++;
        
        countRow = 1;
        int coloum = 0;
        int nomor = 0;
        if (listEmpLevel != null && listEmpLevel.size()>0){
            for(int i=0; i<listEmpLevel.size(); i++){
                Vector temp = (Vector) listEmpLevel.get(i);
                Employee employee = (Employee) temp.get(0);
                PayEmpLevel payEmpLevel = (PayEmpLevel) temp.get(1);
                SalaryLevel salary = (SalaryLevel) temp.get(2);

                countRow++;
                nomor++;
                row = sheet.createRow((short) countRow);
                sheet.createFreezePane(2, 2);
                cell = row.createCell((short) coloum);
                cell.setCellValue("" + nomor);
                cell.setCellStyle(style2);
                coloum++;
                
                cell = row.createCell((short) coloum);
                cell.setCellValue("" + employee.getEmployeeNum());
                cell.setCellStyle(style2);
                coloum++;
                
                cell = row.createCell((short) coloum);
                cell.setCellValue("" + employee.getFullName());
                cell.setCellStyle(style2);
                coloum++;
                
                cell = row.createCell((short) coloum);
                cell.setCellValue("" + Formater.formatTimeLocale(employee.getCommencingDate(), "dd-MMM-yyyy"));
                cell.setCellStyle(style2);
                coloum++;
                
                cell = row.createCell((short) coloum);
                cell.setCellValue("" + salary.getLevelName());
                cell.setCellStyle(style2);
                coloum++;
                
                cell = row.createCell((short) coloum);
                cell.setCellValue("" + Formater.formatTimeLocale(payEmpLevel.getStartDate(), "dd-MMM-yyyy"));
                cell.setCellStyle(style2);
                coloum++;
                
                coloum = 0;
            }
        }
        
        ServletOutputStream sos = response.getOutputStream();
        wb.write(sos);
        sos.close();
    }
    
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
