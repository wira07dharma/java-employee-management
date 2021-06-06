/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.session.payroll;

import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.log.ChangeValue;
import com.dimata.harisma.entity.payroll.PayComponent;
import com.dimata.harisma.entity.payroll.PayConfigPotongan;
import com.dimata.harisma.entity.payroll.PayEmpLevel;
import com.dimata.harisma.entity.payroll.PayPeriod;
import com.dimata.harisma.entity.payroll.PaySlip;
import com.dimata.harisma.entity.payroll.PaySlipComp;
import com.dimata.harisma.entity.payroll.PstPayComponent;
import com.dimata.harisma.entity.payroll.PstPayConfigPotongan;
import com.dimata.harisma.entity.payroll.PstPayEmpLevel;
import com.dimata.harisma.entity.payroll.PstPaySlip;
import com.dimata.util.Formater;
import com.dimata.util.blob.TextLoader;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 *
 * @author mchen
 * Field excel :
* [0] No
* [1] Emp Num 
* [2] Name
* [3] Komponen Potongan
* [4] No. Rekening
* [5] Angsuran Perbulan
* [6] Tanggal Berlaku
* [7] Tanggal Berhenti
* [8] Status
 */
public class ImportPotonganProcess {
    public void drawImportResult(ServletConfig config, HttpServletRequest request, HttpServletResponse response, JspWriter output, long empDivisionId, long sdmDivisionId) {
        String html = "";
        int NUM_HEADER = 2;
        int NUM_CELL = 0;

        ChangeValue changeValue = new ChangeValue();
        String tdColor = "#FFF;";
        try {
            TextLoader uploader = new TextLoader();
            ByteArrayInputStream inStream = null;

            uploader.uploadText(config, request, response);
            Object obj = uploader.getTextFile("file");
            byte byteText[] = null;
            byteText = (byte[]) obj;
            inStream = new ByteArrayInputStream(byteText);

            POIFSFileSystem fs = new POIFSFileSystem(inStream);

            HSSFWorkbook wb = new HSSFWorkbook(fs);
            System.out.println("creating workbook");

            int numOfSheets = wb.getNumberOfSheets();
            long insertOid = 0;
            int incInsert = 0;
            for (int i = 0; i < numOfSheets; i++) {
                int r = 0;
                HSSFSheet sheet = (HSSFSheet) wb.getSheetAt(i);
                output.print("<strong> Sheet name : " + sheet.getSheetName() + "</strong></div>");
                if (sheet == null || sheet.getSheetName() == null || sheet.getSheetName().length() < 1) {
                    output.print(" NOT MATCH : Period name and sheet name ");
                    continue;
                }
                int rows = sheet.getPhysicalNumberOfRows();

                // loop untuk row dimulai dari numHeaderRow (0, .. numHeaderRow diabaikan) => untuk yang bukan sheet pertaman
                int start = (i == 0) ? 0 : NUM_HEADER;
                String empNum = "";
                output.print("<table class=\"tblStyle\">");
                for (r = start; r < rows; r++) {
                    Employee employee = null;
                    long employeeId = 0;
                    long divisionId = 0;
                    boolean errMsg = false;
                    PayConfigPotongan configPotongan = new PayConfigPotongan();
                    try {
                        HSSFRow row = sheet.getRow(r);
                        int cells = 0;
                        //if number of cell is static
                        if (NUM_CELL > 0) {
                            cells = NUM_CELL;
                        } else { //number of cell is dinamyc
                            cells = row.getPhysicalNumberOfCells();
                        }
                        tdColor = "#FFF;";
                        // ambil jumlah kolom yang sebenarnya
                        NUM_CELL = cells;
                        output.print("<tr>");
                        int caseValue = 0;
                        for (int kolom = 0; kolom < cells; kolom++) {
                            HSSFCell cell = row.getCell((short) kolom);
                            String value = null;
                            if (cell != null) {
                                /* proses mem-filter value */
                                switch (cell.getCellType()) {
                                    case HSSFCell.CELL_TYPE_FORMULA:
                                        value = String.valueOf(cell.getCellFormula());
                                        caseValue = 1;
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        value = Formater.formatNumber(cell.getNumericCellValue(), "###");
                                        caseValue = 2;
                                        break;
                                    case HSSFCell.CELL_TYPE_STRING:
                                        value = String.valueOf(cell.getStringCellValue());
                                        caseValue = 3;
                                        break;
                                    default:
                                        value = String.valueOf(cell.getStringCellValue() != null ? cell.getStringCellValue() : "");
                                }
                            }
                            
                            /* Ambil data employee num */
                            if (caseValue == 3 && kolom == 1 && r > 0){ /* colom ini adalah employee number */
                                empNum = value;
                            }
                            /* Ambil data Employee */
                            if (empNum.length()>0 && r > 0 && kolom == 1){
                                try {
                                    employee = PstEmployee.getEmployeeByNum(empNum);
                                } catch(Exception e){
                                    System.out.println("emp num is not available=>"+e.toString());
                                }
                            }

                            /* change color if nothing employee with emp num */
                            if (employee == null){
                                tdColor = "#fde1e8;";
                                employeeId = 0;
                            } else {
                                employeeId = employee.getOID();
                                divisionId = employee.getDivisionId();
                                if (empDivisionId == sdmDivisionId){
                                    tdColor = "#FFF;";
                                } else {
                                    if (employee.getDivisionId() != empDivisionId){
                                        tdColor = "#fde1e8;";
                                    }
                                }
                            }

                            /* Proses menampilkan data ke html table */
                            if (r == 0){ /* Baris Header table */
                                output.print("<td style=\"background-color:#DDD;\"><strong>"+ value + "</strong></td>");
                            } else {
                                /*
                                * [0] No
                                * [1] Emp Num 
                                * [2] Name
                                * [3] Komponen Potongan
                                * [4] No. Rekening
                                * [5] Angsuran Perbulan
                                * [6] Tanggal Berlaku
                                * [7] Tanggal Berhenti
                                * [8] Status
                                */
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                if (employeeId != 0){
                                    switch(kolom){
                                        case 1: configPotongan.setEmployeeId(employeeId); break;
                                        case 3:
                                            if (getComponentId(value) != 0){
                                                configPotongan.setComponentId(getComponentId(value));
                                            } else {
                                                errMsg = true;
                                                tdColor = "#fde1e8;";
                                            }
                                            break;
                                        case 4: configPotongan.setNoRekening(value);
                                            break;
                                        case 5:
                                            configPotongan.setAngsuranPerbulan(Double.valueOf(value));
                                            break;
                                        case 6:
                                            configPotongan.setStartDate(sdf.parse(value));
                                            break;
                                        case 7:
                                            configPotongan.setEndDate(sdf.parse(value));
                                            break;
                                        case 8:
                                            configPotongan.setValidStatus(Integer.valueOf(value));
                                            break;
                                    }
                                }
                                if (value.equals("NULL")){
                                    output.print("<td style=\"background-color:"+tdColor+"\">-</td>");
                                } else {
                                    
                                    output.print("<td style=\"background-color:"+tdColor+"\">"+value+"</td>");
                                }
                            }
                        } /*End For Cols*/
                        output.print("</tr>");
                        /* Insert Process */
                        if (configPotongan.getEmployeeId() != 0){
                            insertOid = PstPayConfigPotongan.insertExc(configPotongan);
                            if (insertOid > 0){
                                incInsert++;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("=> Can't get data ..sheet : " + i + ", row : " + r + ", => Exception e : " + e.toString());
                    }
                } //end of sheet
                output.print("</table>");
                output.print("<div>&nbsp;</div>");
                output.print("<div>Jumlah Data yang masuk = "+incInsert+"</div>");   
            } //end of all sheets
        } catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
    }
    
    public long getComponentId(String compCode){
        long componentId = 0;
        String where = PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_CODE]+"='"+compCode+"'";
        Vector compList = PstPayComponent.list(0, 0, where, "");
        if (compList != null && compList.size()>0){
            PayComponent payComp = (PayComponent)compList.get(0);
            componentId = payComp.getOID();
        }
        return componentId;
    }
}
