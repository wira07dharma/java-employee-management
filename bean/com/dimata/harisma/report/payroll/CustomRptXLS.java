/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.report.payroll;

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

/**
 * Description : Custom Report XLS Date : 2015-04-11
 *
 * @author Hendra Putu
 */
public class CustomRptXLS extends HttpServlet {

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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

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
        style5.setFillBackgroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());//HSSFCellStyle.WHITE);
        style5.setFillForegroundColor(new HSSFColor.LIGHT_TURQUOISE().getIndex());//HSSFCellStyle.WHITE);
        style5.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style5.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style5.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style5.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
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
        
        HSSFCellStyle style7 = wb.createCellStyle();
        style7.setFillBackgroundColor(new HSSFColor.YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style7.setFillForegroundColor(new HSSFColor.YELLOW().getIndex());//HSSFCellStyle.WHITE);
        style7.setFont(fontRed);
        style7.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style7.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style7.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style7.setBorderRight(HSSFCellStyle.BORDER_THIN);
        
        //pemberian warna font
        CellStyle styleFont = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.RED.index);
        styleFont.setFont(font);

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);

        HSSFRow row2 = sheet.createRow((short) 0);
        HSSFCell cell2 = row2.createCell((short) 0);

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
        response.setHeader("Content-Disposition", "attachment; filename=" + customRptMain.getRptMainTitle() + ".xls");
        
        int countRow = 0;

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
        Vector listData = PstCustomRptConfig.list(0, 0, where, "");
        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 2 */
        String whereComp = " RPT_CONFIG_DATA_TYPE = 2 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID =" + oidCustom;
        Vector listSalaryComp = PstCustomRptConfig.list(0, 0, whereComp, "");
        /* WHERE CLAUSE WITH RPT_CONFIG_DATA_TYPE = 1 */
        String whereComb = " RPT_CONFIG_DATA_TYPE = 1 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID =" + oidCustom;
        Vector listComb = PstCustomRptConfig.list(0, 0, whereComb, "");
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
        if (listData != null && listData.size() > 0) {
            int incC = 0;
            for (int i = 0; i < listData.size(); i++) {
                CustomRptConfig rpt = (CustomRptConfig) listData.get(i);
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

                if (i == listData.size() - 1) {
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
        Vector listResult = PstCustomRptConfig.listData(strSelect + " FROM hr_employee " + strJoin + " " + whereClause + strOrderBy, listData);
        //======================================================================//

        /* === get list of SUB TOTAL === */
        String whereSubTotal = " RPT_CONFIG_DATA_TYPE = 3 AND RPT_CONFIG_DATA_GROUP = 0 AND RPT_MAIN_ID =" + oidCustom;
        Vector listSubTotal = PstCustomRptConfig.list(0, 0, whereSubTotal, "");
        String[] countName = new String[listData.size()];
        int[] countIdx = new int[listData.size()];
        /* get name of count by SECTION or DEPARTMENT or else */
        if (listSubTotal != null && listSubTotal.size() > 0) {
            for (int st = 0; st < listSubTotal.size(); st++) {
                CustomRptConfig sbt = (CustomRptConfig) listSubTotal.get(st);
                if ("COUNT".equals(sbt.getRptConfigTableGroup())) {
                    countName[st] = sbt.getRptConfigTableName();
                }
            }
        }
        /* Some of array variable */
        String[][] arrField = new String[listResult.size() + 1][listData.size()]; /* for dinamic fields */

        double[][] totalComp = new double[listResult.size()][listSalaryComp.size()]; /* for total salary component */

        double[] totalCombine = new double[listResult.size()]; /* for total combine */

        double countValueComp = 0;
        boolean[] showRecord = new boolean[listResult.size()];
        /* mengambil field dinamis */
        /* Check count and get index */
        int idx = 0;
        for (int i = 0; i < listData.size(); i++) {
            CustomRptConfig fields = (CustomRptConfig) listData.get(i);
            for (int cn = 0; cn < countName.length; cn++) {
                if (fields.getRptConfigFieldName().equals(countName[cn])) {
                    countIdx[idx] = i;
                    idx++;
                }
            }
        }
        
        //get component deduction
        Vector<PayComponent> listCompDeduction = PstPayComponent.list(0, 0, "COMP_TYPE=2", "");
        ArrayList<Integer> arrayIndexComponentDeduction = new ArrayList<Integer>();
        
        /* 
         ===============================================================================
         * listRecord berisi baris data hasil query dan akan ditampung ke variabel array 
         ===============================================================================
         */
        int fieldIndexPosition = 1;
        if (listResult != null && listResult.size() > 0) {
            for (int y = 0; y < listResult.size(); y++) {
                CustomRptDynamic dyc = (CustomRptDynamic) listResult.get(y);
                /* Get dinamis field */
                if (listData != null && listData.size() > 0) {
                    for (int i = 0; i < listData.size(); i++) {
                        CustomRptConfig rpt = (CustomRptConfig) listData.get(i);
                        /* jika field name != PAY_SLIP_ID */
                        if (!rpt.getRptConfigFieldName().equals("PAY_SLIP_ID")) {
                            arrField[y][i] = dyc.getField(rpt.getRptConfigFieldName());
                        }
                    }
                }
                /* get value field */
                for (int k = 0; k < listSalaryComp.size(); k++) {
                    CustomRptConfig rptComp = (CustomRptConfig) listSalaryComp.get(k);
                    totalComp[y][k] = convertDouble(dyc.getField(rptComp.getRptConfigFieldName()));
                    countValueComp += totalComp[y][k];
                }
                /* get value field combine */
                for (int l = 0; l < listComb.size(); l++) {
                    CustomRptConfig rptComb = (CustomRptConfig) listComb.get(l);
                    totalCombine[y] = convertDouble(dyc.getField(rptComb.getRptConfigFieldName()));
                }
                if (showValue == 0) {
                    showRecord[y] = true;
                } else {
                    if (countValueComp == 0) {
                        showRecord[y] = false;
                    } else {
                        showRecord[y] = true;
                    }
                }
                countValueComp = 0;
            }
        }
        /* END of List Record */
        int[] countTotal = new int[idx];
        boolean[] countFind = new boolean[idx];
        /* inisialisasi */
        for (int d = 0; d < idx; d++) {
            countTotal[d] = 1;
            countFind[d] = false;
        }
        int nomor = 0;
        int yStart = 0;
        int yEnd = 0;
        double total = 0;
        boolean ketemu = false;

        countRow = 1;
        int cols = 0;
        /* Header No */
        row = sheet.createRow((short) countRow);
        cell = row.createCell((short) cols);
        cell.setCellValue("No");
        cell.setCellStyle(style3);
        cols = 1;

        /* mengambil field dinamis */
        for (int i = 0; i < listData.size(); i++) {
            CustomRptConfig fields = (CustomRptConfig) listData.get(i);
            if (!fields.getRptConfigFieldName().equals("PAY_SLIP_ID")) {
                if (arrayIndexHideColom.contains(i)) {
                    continue;
                }
                cell = row.createCell((short) cols);
                cell.setCellValue(fields.getRptConfigFieldHeader());
                cell.setCellStyle(style3);
                cols++;
            }
        }
        /* mengambil field salary component */
        for (int j = 0; j < listSalaryComp.size(); j++) {
            CustomRptConfig comp = (CustomRptConfig) listSalaryComp.get(j);
            cell = row.createCell((short) cols);
            cell.setCellValue(comp.getRptConfigFieldHeader());
            cell.setCellStyle(style3);
            cols++;
            
            for (PayComponent pc : listCompDeduction) {
                if (pc.getCompName().equals(comp.getRptConfigFieldHeader())) {
                    arrayIndexComponentDeduction.add(j);
                }
            }
        }
        /* mengambil field combine */
        for (int k = 0; k < listComb.size(); k++) {
            CustomRptConfig comb = (CustomRptConfig) listComb.get(k);
            cell = row.createCell((short) cols);
            cell.setCellValue(comb.getRptConfigFieldHeader());
            cell.setCellStyle(style3);
            cols++;
        }

        countRow = 1;
        int coloum = 0;
        /* ============== record result ==============*/
        if (listResult != null && listResult.size() > 0) {
            int grandTotalKaryawan = 0;
            for (int y = 0; y < listResult.size(); y++) {
                if (showRecord[y] == true) {
                    int nilai = -1;
                    if (listSalaryComp.size() > 0) {
                        for (int k = 0; k < listSalaryComp.size(); k++) {
                            nilai = nilai + convertInteger(totalComp[y][k]);
                        }
                    }
                    if (nilai != 0) {
                        countRow++;
                        nomor++;
                        row = sheet.createRow((short) countRow);
                        sheet.createFreezePane(2, 2);
                        cell = row.createCell((short) coloum);
                        cell.setCellValue("" + nomor);
                        cell.setCellStyle(style2);
                        coloum++;

                        /* Fields dinamic */
                        if (listData != null && listData.size() > 0) {
                            for (int i = 0; i < listData.size(); i++) {
                                if (arrayIndexHideColom.contains(i)) {
                                    continue;
                                }
                                if (arrField[y][i] != null) {
                                    cell = row.createCell((short) coloum);
                                    cell.setCellValue("" + arrField[y][i]);
                                    cell.setCellStyle(style2);
                                    coloum++;
                                }
                            }
                            /* count record by ... */
                            for (int sb = 0; sb < idx; sb++) {
                                if (arrField[y][countIdx[sb]].equals(arrField[y + 1][countIdx[sb]])) {
                                    countTotal[sb] = countTotal[sb] + 1;
                                } else {
                                    countFind[sb] = true;
                                    ketemu = true;
                                    yEnd = y + 1;
                                }
                            }
                        }

                        for (int k = 0; k < listSalaryComp.size(); k++) {
                            cell = row.createCell((short) coloum);
                            cell.setCellValue(convertInteger(totalComp[y][k]));
                            if (arrayIndexComponentDeduction.contains(k)) {
                                cell.setCellStyle(style6);
                            } else {
                                cell.setCellStyle(style2);
                            }
                            coloum++;
                        }
                        for (int l = 0; l < listComb.size(); l++) {
                            cell = row.createCell((short) coloum);
                            cell.setCellValue(convertInteger(totalCombine[y]));
                            cell.setCellStyle(style2);
                            coloum++;
                        }

                        boolean addColoum = false;
                        if (ketemu == true) {
                            countRow++;
                            row = sheet.createRow((short) countRow);
                            coloum = 0;
                            for (int f = 0; f < idx; f++) {
                                if (countFind[f] == true) {
                                    //total
                                    cell = row.createCell((short) coloum);
                                    cell.setCellValue("Total");
                                    cell.setCellStyle(style4);
                                    coloum++;
                                    
                                    cell = row.createCell((short) coloum);
                                    cell.setCellValue(countTotal[f]);
                                    cell.setCellStyle(style4);
                                    coloum++;
                                    
                                    grandTotalKaryawan += countTotal[f];
                                    countFind[f] = false;
                                    countTotal[f] = 1;
                                    
                                    if (addColoum == false) {
                                        for (int tot = 0; tot < countIdx[f] - arrayIndexHideColom.size(); tot++) {
                                            cell = row.createCell((short) coloum);
                                            cell.setCellValue("");
                                            cell.setCellStyle(style4);
                                            coloum++;
                                        }
                                        addColoum = true;
                                    }
                                }
                            }
                            addColoum = false;
                            for (int b = 0; b < listData.size() - (countIdx[idx - 1] + 2); b++) {
                                cell = row.createCell((short) coloum);
                                cell.setCellValue("");
                                cell.setCellStyle(style4);
                                coloum++;
                            }
                            for (int kolom = 0; kolom < listSalaryComp.size(); kolom++) {
                                for (int x = yStart; x < yEnd; x++) {
                                    total += totalComp[x][kolom];
                                }
                                cell = row.createCell((short) coloum);
                                cell.setCellValue(convertInteger(total));
                                if (arrayIndexComponentDeduction.contains(kolom)) {
                                    cell.setCellStyle(style7);
                                } else {
                                    cell.setCellStyle(style4);
                                }
                                coloum++;
                                total = 0;
                            }
                            for (int x = yStart; x < yEnd; x++) {
                                total += totalCombine[x];
                            }
                            //cell = row.createCell((short) coloum);
                            //cell.setCellValue(convertInteger(total));
                            //cell.setCellStyle(style4);
                            //coloum++;
                            total = 0;
                            ketemu = false;
                        }
                        coloum = 0;
                        yStart = yEnd;
                    }
                }
            }
            //kolom grand total
            countRow++;
            row = sheet.createRow((short) countRow);

            /* field dinamis */
            for (int i = 0; i < listData.size() - arrayIndexHideColom.size(); i++) {
                cell = row.createCell((short) coloum);
                if (i == 1) {
                    cell.setCellValue("Grand Total");
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(style5);
                coloum++;
            }

            /* total pegawai */
            //cell = row.createCell((short) coloum);
            //cell.setCellValue(grandTotalKaryawan);
            //cell.setCellStyle(style4);
            //coloum++;

            /* mengambil field salary component */
            double grandTotalComp = 0;
            for (int j = 0; j < listSalaryComp.size(); j++) {
                for (int y = 0; y < listResult.size(); y++) {
                    grandTotalComp = grandTotalComp + totalComp[y][j];
                }
                cell = row.createCell((short) coloum);
                cell.setCellValue(grandTotalComp);
                if (arrayIndexComponentDeduction.contains(j)) {
                    cell.setCellStyle(style5);
                } else {
                    cell.setCellStyle(style5);
                }
                coloum++;
                grandTotalComp = 0;
            }
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
    /* Convert int */

    public int convertInteger(double val) {
        BigDecimal bDecimal = new BigDecimal(val);
        bDecimal = bDecimal.setScale(0, RoundingMode.HALF_DOWN);
        return bDecimal.intValue();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
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
