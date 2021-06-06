/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.report.payroll;

import com.dimata.harisma.entity.masterdata.Company;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.Division;
import com.dimata.harisma.entity.masterdata.PstCompany;
import com.dimata.harisma.entity.masterdata.PstDepartment;
import com.dimata.harisma.entity.masterdata.PstDivision;
import com.dimata.harisma.entity.masterdata.PstSection;
import com.dimata.harisma.entity.masterdata.Section;
import com.dimata.harisma.entity.payroll.SalaryChangeEntity;
import com.dimata.harisma.entity.payroll.SalaryChangeModel;
import com.dimata.harisma.report.SrcEsptMonth;
import com.dimata.harisma.session.payroll.SessESPT;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.system.entity.PstSystemProperty;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
public class SalaryChangeXLS extends HttpServlet {
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }
    
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

        CellStyle styleFont = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.RED.index);
        styleFont.setFont(font);

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);

        String printHeader = PstSystemProperty.getValueByName("PRINT_HEADER");
        long periodId = FRMQueryString.requestLong(request, "period_id");
        long payrollGroupId = FRMQueryString.requestLong(request, "payrollGroupId");
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = formatDate.format(cal.getTime());
        

        String[] tableTitle = {
            printHeader,
            "Salary Change Report",
            "DATE : "+dateFormat
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
            "No", "Payroll Num", "Full Name",
            "Department old", "Section old", "Position old", "Level old",
            "Department new", "Section new", "Position new", "Level new",
            "Basic old", "Merit old", "Grade old", "Total old",
            "Basic new", "Merit new", "Grade new", "Total new"
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
        
        Vector listSalaryChange = new Vector();
        listSalaryChange = SalaryChangeModel.listSalaryChange(periodId,payrollGroupId);
        if (listSalaryChange != null && listSalaryChange.size() > 0) {
            for (int i = 0; i < listSalaryChange.size(); i++) {
                SalaryChangeEntity salaryChange = (SalaryChangeEntity)listSalaryChange.get(i);
                countRow++;
                no++;
                //countRow = countRow + addRow;
                row = sheet.createRow((short) (countRow));
                sheet.createFreezePane(7, 7);
                cell = row.createCell((short) 0);
                cell.setCellValue(no);
                cell.setCellStyle(style2);

                cell = row.createCell((short) 1);
                cell.setCellValue(salaryChange.getPayrollNumber());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 2);
                cell.setCellValue(salaryChange.getFullName());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 3);
                cell.setCellValue(salaryChange.getDepartmentOld());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 4);
                cell.setCellValue(salaryChange.getSectionOld());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 5);
                cell.setCellValue(salaryChange.getPositionOld());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 6);
                cell.setCellValue(salaryChange.getLevelOld());
                cell.setCellStyle(style2);
                
                cell = row.createCell((short) 7);
                cell.setCellValue(salaryChange.getDepartmentNew());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 8);
                cell.setCellValue(salaryChange.getSectionNew());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 9);
                cell.setCellValue(salaryChange.getPositionNew());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 10);
                cell.setCellValue(salaryChange.getLevelNew());
                cell.setCellStyle(style2);
                
                cell = row.createCell((short) 11);
                cell.setCellValue(salaryChange.getBasicOld());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 12);
                cell.setCellValue(salaryChange.getMeritOld());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 13);
                cell.setCellValue(salaryChange.getGradeOld());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 14);
                cell.setCellValue(salaryChange.getTotalOld());
                cell.setCellStyle(style2);
                
                cell = row.createCell((short) 15);
                cell.setCellValue(salaryChange.getBasicNew());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 16);
                cell.setCellValue(salaryChange.getMeritNew());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 17);
                cell.setCellValue(salaryChange.getGradeNew());
                cell.setCellStyle(style2);
                cell = row.createCell((short) 18);
                cell.setCellValue(salaryChange.getTotalNew());
                cell.setCellStyle(style2);

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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }
}
