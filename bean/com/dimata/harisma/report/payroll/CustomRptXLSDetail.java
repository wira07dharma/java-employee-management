/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.report.payroll;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.payroll.CustomRptConfig;
import com.dimata.harisma.entity.payroll.CustomRptDynamic;
import com.dimata.harisma.entity.payroll.CustomRptMain;
import com.dimata.harisma.entity.payroll.PayComponent;
import com.dimata.harisma.entity.payroll.PstCustomRptConfig;
import com.dimata.harisma.entity.payroll.PstCustomRptMain;
import com.dimata.harisma.entity.payroll.PstPayComponent;
import com.dimata.qdep.form.FRMQueryString;
import com.dimata.system.entity.PstSystemProperty;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Dimata 007
 */
public class CustomRptXLSDetail extends HttpServlet {

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

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String dateFormat = formatDate.format(cal.getTime());

        cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        System.out.println("---===| Excel Report |===---");
        response.setContentType("application/x-msexcel");

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Custom Report " + dateFormat);
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
        
        HSSFCellStyle style2Currency = wb.createCellStyle();
        style2Currency.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2Currency.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2Currency.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2Currency.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2Currency.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2Currency.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFDataFormat dfStyle2 = wb.createDataFormat();
        style2Currency.setDataFormat(dfStyle2.getFormat("#,##0"));

        HSSFCellStyle style3 = wb.createCellStyle();
        Font fontTitle = wb.createFont();
        fontTitle.setColor(HSSFColor.WHITE.index);
        style3.setFont(fontTitle);
        style3.setFillBackgroundColor(new HSSFColor.LIGHT_BLUE().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillForegroundColor(new HSSFColor.LIGHT_BLUE().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
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
        style4.setFillBackgroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style4.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle style4Currency = wb.createCellStyle();
        style4Currency.setFillBackgroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style4Currency.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style4Currency.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style4Currency.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style4Currency.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style4Currency.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFDataFormat dfStyle4 = wb.createDataFormat();
        style4Currency.setDataFormat(dfStyle4.getFormat("#,##0"));

        HSSFCellStyle style5 = wb.createCellStyle();
        style5.setFillBackgroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());//HSSFCellStyle.WHITE);
        style5.setFillForegroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());//HSSFCellStyle.WHITE);
        style5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style5.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle style5Currency = wb.createCellStyle();
        style5Currency.setFillBackgroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());//HSSFCellStyle.WHITE);
        style5Currency.setFillForegroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());//HSSFCellStyle.WHITE);
        style5Currency.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style5Currency.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style5Currency.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style5Currency.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFDataFormat dfStyle5 = wb.createDataFormat();
        style5Currency.setDataFormat(dfStyle5.getFormat("#,##0"));

        Font fontRed = wb.createFont();
        fontRed.setColor(HSSFColor.RED.index);

        HSSFCellStyle style6 = wb.createCellStyle();
        style6.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style6.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style6.setFont(fontRed);
        style6.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style6.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style6.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style6.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle style6Currency = wb.createCellStyle();
        style6Currency.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style6Currency.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style6Currency.setFont(fontRed);
        style6Currency.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style6Currency.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style6Currency.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style6Currency.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFDataFormat dfStyle6 = wb.createDataFormat();
        style6Currency.setDataFormat(dfStyle6.getFormat("#,##0"));

        HSSFCellStyle style7 = wb.createCellStyle();
        style7.setFillBackgroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style7.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style7.setFont(fontRed);
        style7.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style7.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style7.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        HSSFCellStyle style7Currency = wb.createCellStyle();
        style7Currency.setFillBackgroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style7Currency.setFillForegroundColor(new HSSFColor.LIGHT_YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style7Currency.setFont(fontRed);
        style7Currency.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style7Currency.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style7Currency.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style7Currency.setBorderRight(HSSFCellStyle.BORDER_THIN);
        HSSFDataFormat dfStyle7 = wb.createDataFormat();
        style7Currency.setDataFormat(dfStyle7.getFormat("#,##0"));

        //pemberian warna font
        CellStyle styleFont = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.RED.index);
        styleFont.setFont(font);

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);

        String printHeader = PstSystemProperty.getValueByName("PRINT_HEADER");

        long oidCustom = FRMQueryString.requestLong(request, "oid_custom");
        String[] whereField = FRMQueryString.requestStringValues(request, "where_field");
        String[] whereValue = FRMQueryString.requestStringValues(request, "where_value");
        String[] whereType = FRMQueryString.requestStringValues(request, "where_type");
        String[] operator = FRMQueryString.requestStringValues(request, "operator");
        int showValue = FRMQueryString.requestInt(request, "show_value");

        CustomRptMain customRptMain = new CustomRptMain();
        try {
            customRptMain = PstCustomRptMain.fetchExc(oidCustom);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        //set file name
        response.setHeader("Content-Disposition", "attachment; filename=" + customRptMain.getRptMainTitle() + "_detail.xls");

        Employee emp = new Employee();
        try {
            emp = PstEmployee.fetchExc(customRptMain.getRptMainCreatedBy());
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        /* Header setting */
        String strHeader = "Company : " + printHeader;
        strHeader += "\nReport Name : " + customRptMain.getRptMainTitle();
        strHeader += "\nPeriod : " + dateFormat;
        header.setLeft(strHeader);
        /* Footer setting */
        footer.setLeft("Print on : " + dateFormat + " " + sdf.format(cal.getTime()));
        footer.setCenter("Print by : " + emp.getFullName());
        footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());
        //////////////////////
        /*
         * Description : Code to find WHERE DATA 
         */
        String whereClause = "";
        String valueInp = "";
        String[] dataWhere = new String[whereValue.length];
        int a = 0;
        /* Jika input selection tidak kosong */
        if (whereValue != null && whereValue.length > 0) {
            whereClause = " WHERE ";
            /* Ulang sebanyak jumlah field */
            for (int w = 0; w < whereField.length; w++) {
                if (whereValue[w].length() > 0) {
                    /* Jika tipe data field = String maka..*/
                    if (whereType[w].equals("1")) {
                        /* manipulasi data dengan menambahkan apostrophe (') */
                        valueInp = apostrophe(whereValue[w], operator[w]);
                    } else {
                        valueInp = whereValue[w];
                    }
                    dataWhere[a] = whereField[w] + " " + operator[w] + " " + valueInp + " ";
                    a++;
                }
            }
            for (int x = 0; x < a; x++) {
                whereClause += dataWhere[x];
                if (x == a - 1) {
                    whereClause += " ";
                } else {
                    whereClause += " AND ";
                }
            }
        }
        /*
         * Description : Get SELECT DATA and JOIN DATA
         * Date : 2015-04-08 
         */
        String[] dataJoin = new String[7];

        String strSelect = "SELECT ";
        String strJoin = "";
        int inc = 0;
        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 0 */
        String where = " RPT_CONFIG_DATA_TYPE = 0 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID =" + oidCustom;
        Vector listEmployeeField = PstCustomRptConfig.list(0, 0, where, "");
        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 2 */
        String whereComp = " RPT_CONFIG_DATA_TYPE = 2 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID =" + oidCustom;
        Vector listSalaryComponentField = PstCustomRptConfig.list(0, 0, whereComp, "");
        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 1 */
        String whereComb = " RPT_CONFIG_DATA_TYPE = 1 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID =" + oidCustom;
        Vector listCombineField = PstCustomRptConfig.list(0, 0, whereComb, "");
        /* Array join */
        int[] joinCollection = new int[PstCustomRptConfig.joinDataPriority.length];
        /* inisialisasi joinCollection */
        for (int d = 0; d < PstCustomRptConfig.joinDataPriority.length; d++) {
            joinCollection[d] = -1;
        }

        //hide colom
        ArrayList<String> arrayHideColomName = new ArrayList<String>();
        arrayHideColomName.add("COMPANY");
        arrayHideColomName.add("PAYROLL_GROUP_NAME");
        arrayHideColomName.add("PERIOD");
        //hide index
        ArrayList<Integer> arrayIndexHideColom = new ArrayList<Integer>();

        boolean found = false;
        if (listEmployeeField != null && listEmployeeField.size() > 0) {
            int incC = 0;
            for (int i = 0; i < listEmployeeField.size(); i++) {
                CustomRptConfig rpt = (CustomRptConfig) listEmployeeField.get(i);
                //get index colom company & payroll group
                if (arrayHideColomName.contains(rpt.getRptConfigFieldName())) {
                    arrayIndexHideColom.add(i);
                }
                /* mendapatkan join data */

                for (int k = 0; k < PstCustomRptConfig.joinData.length; k++) {
                    for (String retval : PstCustomRptConfig.joinData[k].split(" ")) {
                        dataJoin[inc] = retval;
                        inc++;
                    }
                    inc = 0;

                    /* bandingkan nilai table dengan data join */
                    if (rpt.getRptConfigTableAsName() != null) {
                        if (rpt.getRptConfigTableAsName().equals(dataJoin[4])) {
                            /* cek data join pada array joinCollection */
                            for (int c = 0; c < joinCollection.length; c++) {
                                if (PstCustomRptConfig.joinDataPriority[k] == joinCollection[c]) {
                                    found = true; /* jika found == true, maka data sudah ada di joinCollection */

                                }
                            }
                            if (found == false) {
                                joinCollection[incC] = PstCustomRptConfig.joinDataPriority[k];
                            }
                            found = false;
                        }
                    } else {
                        if (rpt.getRptConfigTableName().equals(dataJoin[2])) {
                            /* cek data join pada array joinCollection */
                            for (int c = 0; c < joinCollection.length; c++) {
                                if (PstCustomRptConfig.joinDataPriority[k] == joinCollection[c]) {
                                    found = true; /* jika found == true, maka data sudah ada di joinCollection */

                                }
                            }
                            if (found == false) {
                                joinCollection[incC] = PstCustomRptConfig.joinDataPriority[k];
                            }
                            found = false;
                        }
                    }

                }
                incC++;
                /* mendapatkan data select */
                //strSelect += rpt.getRptConfigTableName()+"."+rpt.getRptConfigFieldName();

                if (rpt.getRptConfigTableAsName() != null) {
                    strSelect += (rpt.getRptConfigTableAsName().equals("") ? rpt.getRptConfigTableName() : rpt.getRptConfigTableAsName()) + "." + rpt.getRptConfigFieldName();
                } else {
                    strSelect += rpt.getRptConfigTableName() + "." + rpt.getRptConfigFieldName();
                }

                if (i == listEmployeeField.size() - 1) {
                    strSelect += " ";
                } else {
                    strSelect += ", ";
                }
            }
            /* join Collection */
            Arrays.sort(joinCollection); /* sorting array */

            for (int m = 0; m < joinCollection.length; m++) {
                if (joinCollection[m] != -1) {
                    strJoin += PstCustomRptConfig.joinData[joinCollection[m]] + " ";
                }
            }
        }
        /* ORDER BY */
        String strOrderBy = "";
        String whereOrder = " RPT_CONFIG_DATA_TYPE = 0 AND RPT_CONFIG_DATA_GROUP = 2 AND RPT_MAIN_ID =" + oidCustom;
        Vector listOrder = PstCustomRptConfig.list(0, 0, whereOrder, "");
        if (listOrder != null && listOrder.size() > 0) {
            strOrderBy = " ORDER BY ";
            for (int ord = 0; ord < listOrder.size(); ord++) {
                CustomRptConfig rptOrder = (CustomRptConfig) listOrder.get(ord);
                strOrderBy += rptOrder.getRptConfigTableName() + "." + rptOrder.getRptConfigFieldName();
                if (ord == listOrder.size() - 1) {
                    strOrderBy += " ";
                } else {
                    strOrderBy += ", ";
                }
            }
        }
        Vector listRecord = PstCustomRptConfig.listData(strSelect + " FROM hr_employee " + strJoin + " " + whereClause + strOrderBy, listEmployeeField);
        //======================================================================//

        //HIDE COLUMN HEADER EMPLOYEE
        ArrayList<String> arrayHideColumnNameEmployee = new ArrayList<String>();
        arrayHideColumnNameEmployee.add("PAY_SLIP_ID");
        arrayHideColumnNameEmployee.add("STATUS_DATA");
        arrayHideColumnNameEmployee.add("COMPANY");
        arrayHideColumnNameEmployee.add("PAYROLL_GROUP_NAME");
        arrayHideColumnNameEmployee.add("PERIOD");
        
        ArrayList<Integer> arrayIndexHideColumnEmployee = new ArrayList<Integer>();
        
        //GET LIST SUBTOTAL
        String whereSubTotal = " RPT_CONFIG_DATA_TYPE = 3 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID = " + oidCustom;
        Vector listSubTotal = PstCustomRptConfig.list(0, 0, whereSubTotal, "");
        
        //SET VARIABLE SUBTOTAL
        JSONArray jArraySubTotal = new JSONArray();
        for (int st = 0; st < listSubTotal.size(); st++) {
            CustomRptConfig sbt = (CustomRptConfig) listSubTotal.get(st);
            if (arrayHideColumnNameEmployee.contains(sbt.getRptConfigFieldName())) {
                continue;
            }
            JSONObject jObjectSubTotal = new JSONObject();
            //GET SUBTOTAL NAME
            if ("COUNT".equals(sbt.getRptConfigTableGroup())) {
                jObjectSubTotal.put("SUBTOTAL_NAME", sbt.getRptConfigTableName());
                //GET SUBTOTAL INDEX
                for (int ef = 0; ef < listEmployeeField.size(); ef++) {
                    CustomRptConfig fields = (CustomRptConfig) listEmployeeField.get(ef);
                    if (fields.getRptConfigFieldName().equals(sbt.getRptConfigTableName())) {
                        jObjectSubTotal.put("SUBTOTAL_INDEX", ef);
                        jObjectSubTotal.put("SUBTOTAL_TITLE", fields.getRptConfigFieldHeader());
                        break;
                    }
                }
            }
            jObjectSubTotal.put("SUBTOTAL_DATA", "");
            jObjectSubTotal.put("SUBTOTAL_COUNT", 0);
            jObjectSubTotal.put("SUBTOTAL_SHOW", false);
            jArraySubTotal.put(jObjectSubTotal);
        }
        
        //========== DRAW TABLE DATA ==========
        int countRow = 0;
        int cols = 0;
        
        //>>>>>>>>>> SHOW COLUMN HEADER
        row = sheet.createRow((short) countRow);
        cell = row.createCell((short) cols);
        cell.setCellValue("NO");
        cell.setCellStyle(style3);
        cols = 1;
        
        //SHOW COLUMN HEADER EMPLOYEE
        for (int ef = 0; ef < listEmployeeField.size(); ef++) {
            CustomRptConfig fields = (CustomRptConfig) listEmployeeField.get(ef);
            //GET INDEX COLUMN TO HIDE
            if (arrayHideColumnNameEmployee.contains(fields.getRptConfigFieldName())) {
                arrayIndexHideColumnEmployee.add(ef);
            } else {
                cell = row.createCell((short) cols);
                cell.setCellValue(fields.getRptConfigFieldHeader());
                cell.setCellStyle(style3);
                cols++;
            }
        }
        
        //SHOW COLUMN HEADER SALARY COMPONENT
        
        //GET SALARY COMPONENT DEDUCTION
        Vector<PayComponent> listCompDeduction = PstPayComponent.list(0, 0, "COMP_TYPE = 2", "");
        ArrayList<Integer> arrayIndexComponentDeduction = new ArrayList<Integer>();
        
        for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
            CustomRptConfig comp = (CustomRptConfig) listSalaryComponentField.get(sf);
            cell = row.createCell((short) cols);
            cell.setCellValue(comp.getRptConfigFieldHeader());
            cell.setCellStyle(style3);
            cols++;
            
            //GET INDEX SALARY COMPONENT DEDUCTION
            for (PayComponent pc : listCompDeduction) {
                if (pc.getCompName().equals(comp.getRptConfigFieldHeader())) {
                    arrayIndexComponentDeduction.add(sf);
                    break;
                }
            }
        }
        
        //SHOW COLUMN HEADER COMBINE
        for (int cf = 0; cf < listCombineField.size(); cf++) {
            CustomRptConfig comb = (CustomRptConfig) listCombineField.get(cf);
            cell = row.createCell((short) cols);
            cell.setCellValue(comb.getRptConfigFieldHeader());
            cell.setCellStyle(style3);
            cols++;
        }
        
        // ========== SHOW COLUMN DATA ==========
        String lastData = "";
        
        double[] grandTotalSalary = new double[listSalaryComponentField.size()];
        double[] grandTotalCombine = new double[listCombineField.size()];
        
        countRow = 0;
        int coloum = 0;
        int grandTotalEmployee = 0;
        for (int i = 0; i < listRecord.size(); i++) {
            CustomRptDynamic dyc = (CustomRptDynamic) listRecord.get(i);
            grandTotalEmployee++;
            int nomor = i + 1;
            countRow++;
            row = sheet.createRow((short) countRow);
            sheet.createFreezePane(2, 1);
            cell = row.createCell((short) coloum);
            cell.setCellValue(nomor);
            cell.setCellStyle(style2);
            coloum++;
            
            //SHOW DATA EMPLOYEE
            for (int ef = 0; ef < listEmployeeField.size(); ef++) {
                CustomRptConfig rpt = (CustomRptConfig) listEmployeeField.get(ef);
                if (arrayIndexHideColumnEmployee.contains(ef)) {
                    continue;
                }
                String currentData = dyc.getField(rpt.getRptConfigFieldName());
                cell = row.createCell((short) coloum);
                cell.setCellValue("" + currentData);
                cell.setCellStyle(style2);
                coloum++;
                
                
                //CHECK IF SUBTOTAL SHOULD SHOW
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.get("SUBTOTAL_INDEX").equals(ef)) {
                        jo.put("SUBTOTAL_DATA", currentData);
                        
                        //COUNT DATA
                        int count = jo.getInt("SUBTOTAL_COUNT");
                        jo.put("SUBTOTAL_COUNT", count + 1);
                        
                        //GET CURRENT DATA
                        if (!lastData.equals(currentData)) {
                            lastData = currentData;
                        }
                        
                        //CHECK IF CURRENT DATA IS EQUAL TO THE NEXT DATA (IF NEXT DATA IS EXIST)
                        try {
                            CustomRptDynamic dycNext = (CustomRptDynamic) listRecord.get(i + 1);
                            String nextData = dycNext.getField(rpt.getRptConfigFieldName());
                            if (lastData.equals(nextData)) {
                                jo.put("SUBTOTAL_SHOW", false);
                            } else {
                                jo.put("SUBTOTAL_SHOW", true);
                            }
                        } catch (Exception e) {
                            jo.put("SUBTOTAL_SHOW", true);
                        }
                    }
                }
            }
            
            //SHOW DATA SALARY COMPONENT
            for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
                CustomRptConfig rptComp = (CustomRptConfig)listSalaryComponentField.get(sf);
                double salary = convertDouble(dyc.getField(rptComp.getRptConfigFieldName()));
                grandTotalSalary[sf] += salary;
                
                cell = row.createCell((short) coloum);
                cell.setCellValue(salary);
                if (arrayIndexComponentDeduction.contains(sf)) {
                    cell.setCellStyle(style6Currency);
                } else {
                    cell.setCellStyle(style2Currency);
                }
                coloum++;
                
                //COUNT SUBTOTAL SALARY
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.isNull("" + sf)) {
                        jo.put("" + sf, 0);
                    }
                    double storedSalary = jo.getDouble("" + sf);
                    jo.put("" + sf, storedSalary + salary);
                }
            }
            
            //SHOW DATA COMBINE
            for (int cf = 0; cf < listCombineField.size(); cf++) {
                CustomRptConfig rptComb = (CustomRptConfig)listCombineField.get(cf);
                double combine = convertDouble(dyc.getField(rptComb.getRptConfigFieldName()));
                grandTotalCombine[cf] += combine;
                
                cell = row.createCell((short) coloum);
                cell.setCellValue(combine);
                if (arrayIndexComponentDeduction.contains(cf)) {
                    cell.setCellStyle(style6Currency);
                } else {
                    cell.setCellStyle(style2Currency);
                }
                coloum++;
                
                //COUNT SUBTOTAL COMBINE
                for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                    JSONObject jo = jArraySubTotal.getJSONObject(ja);
                    if (jo.isNull("" + cf)) {
                        jo.put("" + cf, 0);
                    }
                    double storedCombine = jo.getDouble("" + cf);
                    jo.put("" + cf, storedCombine + combine);
                }
            }
            
            for (int j = 0; j < coloum; j++) {
                sheet.autoSizeColumn(j);
            }
            
            //RESET CELL POSITION
            coloum = 0;
            
            //>>>>>>>>>> SHOW SUBTOTAL
            for (int ja = 0; ja < jArraySubTotal.length(); ja++) {
                JSONObject jo = jArraySubTotal.getJSONObject(ja);
                if (jo.get("SUBTOTAL_SHOW").equals(true) && i > 0) {
                    String addText = "";
                    if (jo.get("SUBTOTAL_NAME").equals("SECTION")) {
                        addText = "Sub ";
                    }
                    coloum = 0;
                    countRow++;
                    row = sheet.createRow((short) countRow);
                    cell = row.createCell((short) coloum);
                    cell.setCellValue(addText + "Total " + jo.get("SUBTOTAL_TITLE") + " " + jo.get("SUBTOTAL_DATA") + " : " + jo.get("SUBTOTAL_COUNT"));
                    cell.setCellStyle(style4);
                    int lastCol = listEmployeeField.size() - arrayIndexHideColumnEmployee.size();
                    sheet.addMergedRegion(new CellRangeAddress(countRow, countRow, 0, lastCol));
                    coloum++;
                    
                    for (int ef = 0; ef < listEmployeeField.size() - arrayIndexHideColumnEmployee.size(); ef++) {
                        cell = row.createCell((short) coloum);
                        cell.setCellValue("");
                        cell.setCellStyle(style4Currency);
                        coloum++;
                    }

                    //RESET DATA SUBTOTAL
                    jo.put("SUBTOTAL_COUNT", 0);
                    
                    for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
                        double subTotalSalary = jo.getDouble("" + sf);
                        cell = row.createCell((short) coloum);
                        cell.setCellValue(subTotalSalary);
                        if (arrayIndexComponentDeduction.contains(sf)) {
                            cell.setCellStyle(style7Currency);
                        } else {
                            cell.setCellStyle(style4Currency);
                        }
                        coloum++;
                        //RESET DATA SUBTOTAL SALARY
                        jo.put("" + sf, 0);
                    }
                    
                    for (int cf = 0; cf < listCombineField.size(); cf++) {
                        double subTotalCombine = jo.getDouble("" + cf);
                        cell = row.createCell((short) coloum);
                        cell.setCellValue(subTotalCombine);
                        if (arrayIndexComponentDeduction.contains(cf)) {
                            cell.setCellStyle(style7Currency);
                        } else {
                            cell.setCellStyle(style4);
                        }
                        coloum++;
                        //RESET DATA SUBTOTAL COMBINE
                        jo.put("" + cf, 0);
                    }
                    
                    //RESET CELL POSITION
                    coloum = 0;
                }
            }
        }
        //<<<<<<<<<<
        
        //>>>>>>>>>> SHOW GRAND TOTAL
        countRow++;
        row = sheet.createRow((short) countRow);
        cell = row.createCell((short) coloum);
        cell.setCellValue("Grand Total : " + grandTotalEmployee);
        cell.setCellStyle(style5);
        int lastCol = listEmployeeField.size() - arrayIndexHideColumnEmployee.size();
        sheet.addMergedRegion(new CellRangeAddress(countRow, countRow, 0, lastCol));
        coloum++;
        
        for (int ef = 0; ef < listEmployeeField.size() - arrayIndexHideColumnEmployee.size(); ef++) {
            cell = row.createCell((short) coloum);
            cell.setCellValue("");
            cell.setCellStyle(style5);
            sheet.autoSizeColumn(coloum);
            coloum++;
        }
        for (int sf = 0; sf < listSalaryComponentField.size(); sf++) {
            cell = row.createCell((short) coloum);
            cell.setCellValue(grandTotalSalary[sf]);
            cell.setCellStyle(style5Currency);
            sheet.autoSizeColumn(coloum);
            coloum++;
        }
        for (int cf = 0; cf < listCombineField.size(); cf++) {
            cell = row.createCell((short) coloum);
            cell.setCellValue(grandTotalCombine[cf]);
            cell.setCellStyle(style5Currency);
            sheet.autoSizeColumn(coloum);
            coloum++;
        }
        
        ServletOutputStream sos = response.getOutputStream();
        wb.write(sos);
        sos.close();
    }
    
    /* Convert double */
    public double convertDouble(String val) {
        BigDecimal bDecimal = new BigDecimal(Double.valueOf(val));
        bDecimal = bDecimal.setScale(0, RoundingMode.HALF_DOWN);
        return bDecimal.doubleValue();
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

    public String apostrophe(String value, String opr) {
        String str = "";
        if (opr.equals("=")) {
            str = "'" + value + "'";
        } else if (opr.equals("BETWEEN")) {
            String[] data = new String[5];
            int inc = 0;
            for (String retval : value.split(" ")) {
                data[inc] = retval;
                inc++;
            }
            str = "'" + data[0] + "' AND '" + data[1] + "'";
        } else if (opr.equals("LIKE")) {
            str = "'" + value + "'";
        } else if (opr.equals("IN")) {
            String stIn = "";
            for (String retval : value.split(",")) {
                stIn += " '" + retval + "', ";
            }
            stIn += "'0'";
            str += "(" + stIn + ")";
        } else if (opr.equals("!=")) {
            str = "'" + value + "'";
        } else {
            str = value;
        }
        return str;
    }
}
