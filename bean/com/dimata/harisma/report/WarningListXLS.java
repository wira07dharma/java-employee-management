/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.report;

import com.dimata.harisma.entity.employee.EmpWarning;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.masterdata.Department;
import com.dimata.harisma.entity.masterdata.Division;
import com.dimata.harisma.entity.masterdata.Position;
import com.dimata.harisma.entity.masterdata.Section;
import com.dimata.harisma.entity.search.SrcEmpWarning;
import com.dimata.harisma.session.admin.SessUserSession;
import com.dimata.util.Formater;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 *
 * @author IanRizky
 */
public class WarningListXLS extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
        System.out.println("\t---===| Warning Excel Report |===---");
		response.setHeader("Content-Disposition", "attachment; filename=employee_warning.xls");
        // response.setContentType("text/html");
        // response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/x-msexcel");

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Warning List");
        HSSFCellStyle style = wb.createCellStyle();

        style.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());    // HSSFCellStyle.WHITE);
        style.setFillForegroundColor(new HSSFColor.WHITE().getIndex());    // HSSFCellStyle.WHITE);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);

        HSSFCellStyle style2 = wb.createCellStyle();

        style2.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());    // HSSFCellStyle.WHITE);
        style2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());    // HSSFCellStyle.WHITE);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle style3 = wb.createCellStyle();

        style3.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());    // HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());    // HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle style4 = wb.createCellStyle();

        style4.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());    // HSSFCellStyle.WHITE);
        style4.setFillForegroundColor(new HSSFColor.WHITE().getIndex());    // HSSFCellStyle.WHITE);
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle styleWrap = wb.createCellStyle();
        styleWrap.setWrapText(true);
        styleWrap.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        
        HSSFCellStyle styleTop = wb.createCellStyle();
        styleTop.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        
        HttpSession session = request.getSession();
        boolean isLoggedIn = false;
        SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME);
        int  SESS_LANGUAGE =0;
		try {
            if (userSession == null) {
                userSession = new SessUserSession();
            } else {
                if (userSession.isLoggedIn() == true) {
                    isLoggedIn = true;
		}
	}
            String strLanguage = "";
            if (session.getValue("APPLICATION_LANGUAGE") != null) {
                strLanguage = String.valueOf(session.getValue("APPLICATION_LANGUAGE"));
            }

            int langDefault = com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT;
            int langForeign = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
            SESS_LANGUAGE = (strLanguage!=null && strLanguage.length() > 0) ? Integer.parseInt(strLanguage) : langForeign;
            
        } catch (Exception exc) {
            return;
        }

        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);

        cell.setCellValue("WARNING LIST");
        cell.setCellStyle(style);

        for (int j = 1; j < 9; j++) {
            cell = row.createCell((short) j);
            cell.setCellStyle(style);
        }

        String[] tableHeaderEng = {
            "NO", "PAYROLL", "EMPLOYEE", "DEPARTMENT", "POSITION", "DATE", "FACTS", "WARN BY"
        };
        
        String[] tableHeaderID = {
            "NO", "NRK", "Nama Karyawan", "Unit", "Jabatan", "Tanggal", "Deskripsi", "Diberikan Oleh"
        };
        
        String[] tableHeader = SESS_LANGUAGE ==com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN ? tableHeaderEng : tableHeaderID;

        row = sheet.createRow((short) (2));

        for (int k = 0; k < tableHeader.length; k++) {
            cell = row.createCell((short) k);
            cell.setCellValue(tableHeader[k]);
            cell.setCellStyle(style3);
        }

        
        SrcEmpWarning sessEmpWarning = new SrcEmpWarning();

        sessEmpWarning = (SrcEmpWarning) session.getValue("SRC_EMP_WARNING");

        Date test = sessEmpWarning.getStartingFactDate();
        Vector listSource = new Vector();

        listSource = (Vector) session.getValue("session_list_report");
        row = sheet.createRow((short) (5));

        // cell = row.createCell((short)3);

        int a = 3;
        int no = 0;

        for (int i = 0; i < listSource.size(); i++) {
            Vector list = (Vector) listSource.get(i);
            Employee emp = (Employee)list.get(0);
            EmpWarning wrn = (EmpWarning)list.get(1);
            Department dep = (Department)list.get(2);
            Section sec = (Section)list.get(3);
            Position pos = (Position)list.get(4);
            
            row = sheet.createRow((short) (a + i));
            no++;
            cell = row.createCell((short) 0);
            cell.setCellValue(String.valueOf(no));
            cell.setCellStyle(styleTop);
            cell = row.createCell((short) 1);
            cell.setCellValue(emp.getEmployeeNum());
            cell.setCellStyle(styleTop);
            cell = row.createCell((short) 2);
            cell.setCellValue(emp.getFullName());
            cell.setCellStyle(styleTop);
            cell = row.createCell((short) 3);
            cell.setCellValue(dep.getDepartment());
            cell.setCellStyle(styleTop);
            //cell = row.createCell((short) 4);
            //cell.setCellValue(sec.getSection());
            //cell.setCellStyle(style);
            cell = row.createCell((short) 4);
            cell.setCellValue(pos.getPosition());
            cell.setCellStyle(styleTop);
            cell = row.createCell((short) 5);
            cell.setCellValue(Formater.formatDate(wrn.getBreakDate(), "d-MMM-yyyy"));
            cell.setCellStyle(styleTop);
            cell = row.createCell((short) 6);
            /*cell.setCellValue((rpm.getDescription().length() > 50)
                    ? rpm.getDescription().substring(0, 50) + " ..."
                    : rpm.getDescription());*/
            
            if (wrn.getBreakFact().length() > 100){
                cell.setCellStyle(styleWrap);
                cell.setCellValue(wrn.getBreakFact());
            } else {
                cell.setCellValue(wrn.getBreakFact());
            }
            cell = row.createCell((short) 7);
            cell.setCellValue(wrn.getWarningBy());
            cell.setCellStyle(styleTop);
            //cell.setCellStyle(style);
        }
        
        for (int i=1; i < 5;i++){
            sheet.autoSizeColumn(i);
            
        }
        sheet.setColumnWidth(6, 10*1000);
        sheet.autoSizeColumn(7);
        ServletOutputStream sos = response.getOutputStream();

        wb.write(sos);
        sos.close();
    }

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
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
	 * Handles the HTTP <code>POST</code> method.
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
