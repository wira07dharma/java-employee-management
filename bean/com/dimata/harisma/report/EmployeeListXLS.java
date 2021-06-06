/*
 * EmpScheduleListXLS.java
 *
 * Created on October 16, 2002, 12:08 PM
 */
package com.dimata.harisma.report;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

//test aja
import com.dimata.qdep.form.*;
//
import java.sql.*;//tambah
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;

import com.dimata.util.*;
import com.dimata.harisma.entity.search.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.attendance.*;
import com.dimata.harisma.entity.locker.*;
import com.dimata.harisma.session.employee.*;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.ref.Reference;
import java.net.URL;
import javax.imageio.ImageIO;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

/** 
 *
 * @author  karya
 * @version 
 */
public class EmployeeListXLS extends HttpServlet {

    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /** Destroys the servlet.
     */
    public void destroy() {
    }

    
    //}

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        System.out.println("---===| Excel Report |===---");
        //response.setContentType("text/html");
        //response.setContentType("application/vnd.ms-excel");
        response.setContentType("application/x-msexcel");
		response.setHeader("Content-Disposition", "attachment; filename=employee_data.xls");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("Employee List");
        /* BY KARTIKA , di hide sementara tgl. 25 September 2012
        HSSFSheet sheetFamily = wb.createSheet("Family List");
        HSSFSheet sheetCarrier = wb.createSheet("Caarier List");
        **/

        HSSFCellStyle style = wb.createCellStyle();
        style.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_DOUBLE);
        
        HSSFCellStyle style2 = wb.createCellStyle();
        style2.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);

        
        HSSFCellStyle style3 = wb.createCellStyle();
        style3.setFillBackgroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillForegroundColor(new HSSFColor.GREY_25_PERCENT().getIndex());//HSSFCellStyle.GREY_25_PERCENT);
        style3.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        //style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);

        HSSFCellStyle style4 = wb.createCellStyle();
        style4.setFillBackgroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style4.setFillForegroundColor(new HSSFColor.WHITE().getIndex());//HSSFCellStyle.WHITE);
        style4.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // Create a row and put some cells in it. Rows are 0 based.
    //    HSSFRow row = sheet.createRow((short) 0);
    //    HSSFCell cell = row.createCell((short) 0);
      
        /*for (int j = 1; j < 34; j++) {
        cell = row.createCell((short) j);
        cell.setCellStyle(style);
        }


        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) 20));

        row = sheet.createRow((short) (1));
        for (int k = 0; k < 34; k++) {
        cell = row.createCell((short) k);
        cell.setCellValue("");
        cell.setCellStyle(style4);
        } SEMENTARA INI BELUM TAU FUNGSINYA UNTUK APA, TAPI JK DIHILANGKAN, MENGURANGI SEDIKIT SIZE FILE
         */
        
/* BY KARTIKA , di hide sementara tgl. 25 September 2012        
        row = sheetFamily.createRow((short) 0);
        cell = row.createCell((short) 0);
        cell.setCellValue("FAMILY LIST");
        cell.setCellStyle(style);

        row = sheetCarrier.createRow((short) 0);
        cell = row.createCell((short) 0);
        cell.setCellValue("CARRIER LIST");
        cell.setCellStyle(style);
*/
             HSSFRow row = sheet.createRow((short) 0);
     HSSFCell cell = row.createCell((short) 0);
       cell.setCellValue("EMPLOYEE LIST");
        cell.setCellStyle(style);
//        SessEmployee sessEmployee = new SessEmployee();
//        SrcEmployee srcEmployee = new SrcEmployee();
        HttpSession session = request.getSession();
//        srcEmployee = (SrcEmployee) session.getValue(SessEmployee.SESS_SRC_EMPLOYEE);
        //update by satrya 2013-08-05
        Vector listEmployee = new Vector(1, 1);
        //listEmployee = sessEmployee.searchEmployee(srcEmployee, 0, 5000);
        //update by satrya 2013-08-05
        SearchSpecialQuery searchSpecialQuery = new SearchSpecialQuery();
        searchSpecialQuery = (SearchSpecialQuery)session.getValue(SessEmployee.SESS_SRC_EMPLOYEE);
         listEmployee = SessSpecialEmployee.searchSpecialEmployee(searchSpecialQuery,0,50000); 
        //String d = "";

        String[] tableHeader = {

            "NO","FOTO","PAYROLL","BARCODE_NUMBER","ID CARD","FULL_NAME","COMPANY","DIVISION","DEPARTEMENT","SECTION","EMP. CATEGORY"
            ,"LEVEL","POSITION","RELIGION","GENDER","EDUCATION","GRADUATION","START_DATE_EDUCATION","END_DATE_EDUCATION"
            ,"LANGUAGE","ORAL","WRITTEN","DESCRIPTION","MARITAL STATUS","TAX REPORT","BLOOD"
            ,"BPJS KETENAGAKERJAAN NUMBER","BPJS KETENAGAKERJAAN DATE","COMMERCING DATE","RESIGN DATE","RESIGN REASON","RESIGN DESC"
            ,"LOCKER","ADDRESS","PERMANENT ADDRESS","PHONE","HAND PHONE","EMERGENSI PHONE / PERSON NAME"
            ,"POSTAL CODE","BIRTH PLACE","BIRTH DATE","AGE","RACE","FATHER NAME","MOTHER NAME",	"PARENTS ADDRESS"
            ,"FAMILY MEMBER NAME","FAMILY GENDER","FAMILY RELATIONSHIP","FAMLILY GUARANTED","FAMILY DATE OF BIRTH","FAMILY AGE","FAMILY RELIGION"
            ,"FAMILY JOB ADRESS","FAMILY LAST EDUCATION","FAMILY ADDRESS","END DATE CONTRACT","ID Card Type","NPWP","NO KK","BPJS KESEHATAN NUMBER","BPJS KESEHATAN DATE","SHIO","ELEMEN","IQ","EQ","PROBATION END DATE","STATUS PENSIUN","START DATE PENSIUN","PRESENCE CHECK PARAMETER",
            "MEDICAL INFO","WACOMPANY","WADIVISION","WADEPARTMENT","WASECTION","WAPOSITION","GRADE", "CAREERPATH COMPANY", "CAREERPATH DIVISION"
			,"CAREERPATH DEPARTMENT","CAREERPATH SECTION","CAREERPATH POSITION","CAREERPATH LEVEL","CAREERPATH CATEGORY","CAREERPATH FROM","CAREERPATH TO"

        };
        row = sheet.createRow((short) (0));
        for (int k = 0; k < tableHeader.length; k++) {
            cell = row.createCell((short) k);   
            cell.setCellValue(tableHeader[k]);
            cell.setCellStyle(style3);
        }
      int countRow = 1;
       int addRow =0;
     if(listEmployee!=null&&listEmployee.size()>0){
         SessTmpSpecialEmployee sessTmpSpecialEmployee = new SessTmpSpecialEmployee();
          Vector listEducation = PstEmpEducation.listEducation("", PstEmpEducation.fieldNames[PstEmpEducation.FLD_EMPLOYEE_ID]+","+PstEmpEducation.fieldNames[PstEmpEducation.FLD_START_DATE]+ " DESC ");
          Vector listLanguage = PstEmpLanguage.listEmpLanguage("", PstEmpLanguage.fieldNames[PstEmpLanguage.FLD_EMPLOYEE_ID]+ " ASC ");
          Vector listFamily = PstFamilyMember.listEmpFamily("", PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID]+","+PstFamilyMember.fieldNames[PstFamilyMember.FLD_BIRTH_DATE]+ " ASC ");
		  
          int no=1;
          int tmpx=0;
		  sheet.setColumnWidth(1, 4700);
        for (int i = 0; i < listEmployee.size(); i++) {
            sessTmpSpecialEmployee =  (SessTmpSpecialEmployee) listEmployee.get(i);
			Vector listCareer = PstCareerPath.list(0, 0, PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID]+"="+sessTmpSpecialEmployee.getEmployeeId(), PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM]);
          try{
              countRow =tmpx>0 && addRow==0?countRow+1:countRow++;
            countRow = countRow + addRow;
			int tmpAddRow = countRow;
			for(int idxAddRow=0;idxAddRow<8;idxAddRow++){
				row = sheet.createRow((short) (tmpAddRow));
				for(int idx=0;idx<86;idx++){
				   cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
				}
				tmpAddRow=tmpAddRow+1;
			}
            row = sheet.createRow((short) (countRow));
             
           // row = sheet.createRow((short) (i+1));
            
            //"Payroll", "Full Name", "Department", "Section", "Position", 
             //modif by satrya 2013-08-01
           
            sheet.createFreezePane(6, 1);
            if(sessTmpSpecialEmployee.getEmployeeNum()!=null && sessTmpSpecialEmployee.getEmployeeNum().length()>0){ // sessTmpSpecialEmployee.getEmployeeNum().equalsIgnoreCase("25043")){
                
                cell = row.createCell((short) 0);cell.setCellValue(no);cell.setCellStyle(style2);    
                no++;
            }
			String pictPath = "";
			ServletContext servletContext = getServletContext();
            String contextPath = servletContext.getRealPath("/");
			/* Read the input image into InputStream */
			InputStream my_banner_image = null;
			try {
				//SessEmployeePicture sessEmployeePicture = new SessEmployeePicture();
				//pictPath = sessEmployeePicture.fetchImageEmployee(sessTmpSpecialEmployee.getEmployeeId());
				pictPath = contextPath+"/imgcache/"+sessTmpSpecialEmployee.getEmployeeNum()+".JPG";
				my_banner_image = new FileInputStream(pictPath);
			} catch (Exception e) {
				pictPath = contextPath+"/imgcache/no-img.jpg";
				my_banner_image = new FileInputStream(pictPath);
			}
			
			int imgWidth = 0; // only initial if not known
			int imgHeight = 0; // only initial if not known
			
			
			/* Convert Image to byte array */
			//byte[] bytes = IOUtils.toByteArray(my_banner_image);
			BufferedImage img = resize(new File(pictPath), new Dimension(130, 135));
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(img, "jpg", bos );
			byte [] bytes = bos.toByteArray();
			/* Add Picture to workbook and get a index for the picture */
			int my_picture_id = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			/* Close Input Stream */
			my_banner_image.close();                
			/* Create the drawing container */
			HSSFPatriarch drawing = sheet.createDrawingPatriarch();
			/* Create an anchor point */
			ClientAnchor my_anchor = new HSSFClientAnchor();
			/* Define top left corner, and we can resize picture suitable from there */
			my_anchor.setAnchorType(0);
			my_anchor.setCol1(1);
			my_anchor.setRow1(countRow);
			/* Invoke createPicture and pass the anchor point and ID */
			HSSFPicture  my_picture = drawing.createPicture(my_anchor, my_picture_id);
			/* Call resize method, which resizes the image */
			my_picture.resize();
			
			cell = row.createCell((short) 2);cell.setCellValue(sessTmpSpecialEmployee.getEmployeeNum()!=null && sessTmpSpecialEmployee.getEmployeeNum().length()>0?sessTmpSpecialEmployee.getEmployeeNum():"-");cell.setCellStyle(style2);            
            cell = row.createCell((short) 3);cell.setCellValue(sessTmpSpecialEmployee.getBarcodeNumber()!=null&& sessTmpSpecialEmployee.getBarcodeNumber().length()>0?sessTmpSpecialEmployee.getBarcodeNumber():"-");cell.setCellStyle(style2);   
            cell = row.createCell((short) 4);cell.setCellValue(sessTmpSpecialEmployee.getIndentCardNr()!=null&& sessTmpSpecialEmployee.getIndentCardNr().length()>0?sessTmpSpecialEmployee.getIndentCardNr():"-");cell.setCellStyle(style2);   
            cell = row.createCell((short) 5);cell.setCellValue(sessTmpSpecialEmployee.getFullName()!=null&& sessTmpSpecialEmployee.getFullName().length()>0?sessTmpSpecialEmployee.getFullName():"-");cell.setCellStyle(style2);   
            cell = row.createCell((short) 6);cell.setCellValue(sessTmpSpecialEmployee.getCompanyName()!=null&& sessTmpSpecialEmployee.getCompanyName().length()>0?sessTmpSpecialEmployee.getCompanyName():"-");cell.setCellStyle(style2);   
            cell = row.createCell((short) 7);cell.setCellValue(sessTmpSpecialEmployee.getDivision()!=null&& sessTmpSpecialEmployee.getDivision().length()>0?sessTmpSpecialEmployee.getDivision():"-");cell.setCellStyle(style2); 
            cell = row.createCell((short) 8);cell.setCellValue(sessTmpSpecialEmployee.getDepartement()!=null&& sessTmpSpecialEmployee.getDepartement().length()>0?sessTmpSpecialEmployee.getDepartement():"-");cell.setCellStyle(style2); 
            cell = row.createCell((short) 9);cell.setCellValue(sessTmpSpecialEmployee.getSection()!=null&& sessTmpSpecialEmployee.getSection().length()>0?sessTmpSpecialEmployee.getSection():"-");cell.setCellStyle(style2); 
            cell = row.createCell((short) 10);cell.setCellValue(sessTmpSpecialEmployee.getEmpCategory()!=null&& sessTmpSpecialEmployee.getEmpCategory().length()>0?sessTmpSpecialEmployee.getEmpCategory():"-");cell.setCellStyle(style2); 
            cell = row.createCell((short) 11);cell.setCellValue(sessTmpSpecialEmployee.getLevel()!=null&& sessTmpSpecialEmployee.getLevel().length()>0?sessTmpSpecialEmployee.getLevel():"-");cell.setCellStyle(style2); 
            cell = row.createCell((short) 12);cell.setCellValue(sessTmpSpecialEmployee.getPosition()!=null&& sessTmpSpecialEmployee.getPosition().length()>0?sessTmpSpecialEmployee.getPosition():"-");cell.setCellStyle(style2); 
            cell = row.createCell((short) 13);cell.setCellValue(sessTmpSpecialEmployee.getReligion()!=null&& sessTmpSpecialEmployee.getReligion().length()>0?sessTmpSpecialEmployee.getReligion():"-");cell.setCellStyle(style2); 
            cell = row.createCell((short) 14);cell.setCellValue(PstEmployee.sexKey[sessTmpSpecialEmployee.getSexEmployee()]);cell.setCellStyle(style2); 
            //education
            // int cool = 13;
            cell = row.createCell((short) 15);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 16);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 17);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 18);cell.setCellValue("");cell.setCellStyle(style2);
            //language
            cell = row.createCell((short) 19);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 20);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 21);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 22);cell.setCellValue("");cell.setCellStyle(style2); 

            
            
            cell = row.createCell((short) 23);cell.setCellValue(sessTmpSpecialEmployee.getMaritalStatus()+"-"+sessTmpSpecialEmployee.getMaritalNumberOfChildren());cell.setCellStyle(style2);
             cell = row.createCell((short) 24);cell.setCellValue(sessTmpSpecialEmployee.getTaxMarital()+"-"+sessTmpSpecialEmployee.getTaxNumberChildren());cell.setCellStyle(style2);
             cell = row.createCell((short) 25);cell.setCellValue(sessTmpSpecialEmployee.getBloodTypeEmployee());cell.setCellStyle(style2);
             cell = row.createCell((short) 26);cell.setCellValue(sessTmpSpecialEmployee.getAstekNumEMployee()!=null && sessTmpSpecialEmployee.getAstekNumEMployee().length()>0?sessTmpSpecialEmployee.getAstekNumEMployee():"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 27);cell.setCellValue(sessTmpSpecialEmployee.getAsktekDate()!=null?Formater.formatDate(sessTmpSpecialEmployee.getAsktekDate(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
             
             cell = row.createCell((short) 28);cell.setCellValue(sessTmpSpecialEmployee.getCommercingDateEmployee()!=null?Formater.formatDate(sessTmpSpecialEmployee.getCommercingDateEmployee(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 29);cell.setCellValue(sessTmpSpecialEmployee.getResignedDate()!=null?Formater.formatDate(sessTmpSpecialEmployee.getResignedDate(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 30);cell.setCellValue(sessTmpSpecialEmployee.getResignedReason()!=null && sessTmpSpecialEmployee.getResignedReason().length()>0? sessTmpSpecialEmployee.getResignedReason():"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 31);cell.setCellValue(sessTmpSpecialEmployee.getResignedDesc()!=null && sessTmpSpecialEmployee.getResignedDesc().length()>0?sessTmpSpecialEmployee.getResignedDesc():"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 32);cell.setCellValue(sessTmpSpecialEmployee.getLockerLocation()!=null && sessTmpSpecialEmployee.getLockerLocation().length()>0 && sessTmpSpecialEmployee.getLockerNumber()!=null && sessTmpSpecialEmployee.getLockerNumber().length()>0? sessTmpSpecialEmployee.getLockerLocation()+"-"+sessTmpSpecialEmployee.getLockerNumber():"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 33);cell.setCellValue(sessTmpSpecialEmployee.getAddressEmployee()!=null && sessTmpSpecialEmployee.getAddressEmployee().length()>0?sessTmpSpecialEmployee.getAddressEmployee():"-");cell.setCellStyle(style2);
             
             cell = row.createCell((short) 34);cell.setCellValue(sessTmpSpecialEmployee.getAddressPermanent()!=null && sessTmpSpecialEmployee.getAddressPermanent().length()>0?sessTmpSpecialEmployee.getAddressPermanent():"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 35);cell.setCellValue(sessTmpSpecialEmployee.getPhoneEmployee()!=null && sessTmpSpecialEmployee.getPhoneEmployee().length()>0?sessTmpSpecialEmployee.getPhoneEmployee():"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 36);cell.setCellValue(sessTmpSpecialEmployee.getHandphone()!=null && sessTmpSpecialEmployee.getHandphone().length()>0?sessTmpSpecialEmployee.getHandphone():"-");cell.setCellStyle(style2);
            String phEmergensi="";
             if(sessTmpSpecialEmployee.getPhoneEmg()!=null && sessTmpSpecialEmployee.getPhoneEmg().length()>0){
                 phEmergensi = sessTmpSpecialEmployee.getPhoneEmg();
             }
             String personEmrg="";
             if(sessTmpSpecialEmployee.getNameEmg()!=null && sessTmpSpecialEmployee.getNameEmg().length()>0){
                 personEmrg = sessTmpSpecialEmployee.getNameEmg();
             }
             cell = row.createCell((short) 37);cell.setCellValue((phEmergensi.length()>0?phEmergensi:"-")+"/"+(personEmrg.length()>0?personEmrg:"-"));cell.setCellStyle(style2);
             cell = row.createCell((short) 38);cell.setCellValue(sessTmpSpecialEmployee.getPostalCodeEmployee());cell.setCellStyle(style2);
             cell = row.createCell((short) 39);cell.setCellValue(sessTmpSpecialEmployee.getBirthPlaceEmployee()!=null && sessTmpSpecialEmployee.getBirthPlaceEmployee().length()>0? sessTmpSpecialEmployee.getBirthPlaceEmployee():"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 40);cell.setCellValue(sessTmpSpecialEmployee.getBirthDateEmployee()!=null ? Formater.formatDate(sessTmpSpecialEmployee.getBirthDateEmployee(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 41);cell.setCellValue(sessTmpSpecialEmployee.getUmur());cell.setCellStyle(style2);
             cell = row.createCell((short) 42);cell.setCellValue( sessTmpSpecialEmployee.getRaceName()!=null && sessTmpSpecialEmployee.getRaceName().length()>0? sessTmpSpecialEmployee.getRaceName()  :"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 43);cell.setCellValue( sessTmpSpecialEmployee.getFather()!=null && sessTmpSpecialEmployee.getFather().length()>0 && !sessTmpSpecialEmployee.getFather().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getFather()  :"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 44);cell.setCellValue( sessTmpSpecialEmployee.getMother()!=null && sessTmpSpecialEmployee.getMother().length()>0 && !sessTmpSpecialEmployee.getMother().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getMother()  :"-");cell.setCellStyle(style2);
             cell = row.createCell((short) 45);cell.setCellValue( sessTmpSpecialEmployee.getParentsAddress()!=null && sessTmpSpecialEmployee.getParentsAddress().length()>0 && !sessTmpSpecialEmployee.getParentsAddress().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getParentsAddress()  :"-");cell.setCellStyle(style2);
             
             //family
            cell = row.createCell((short) 46);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 47);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 48);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 49);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 50);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 51);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 52);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 53);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 54);cell.setCellValue( "");cell.setCellStyle(style2);
            cell = row.createCell((short) 55);cell.setCellValue( "");cell.setCellStyle(style2);
           
            cell = row.createCell((short) 56);cell.setCellValue(sessTmpSpecialEmployee.getEndContractEmployee()!=null ? Formater.formatDate(sessTmpSpecialEmployee.getEndContractEmployee(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 57);cell.setCellValue(sessTmpSpecialEmployee.getIdCardType()!=null && sessTmpSpecialEmployee.getIdCardType().length()>0 && !sessTmpSpecialEmployee.getIdCardType().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getIdCardType()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 58);cell.setCellValue(sessTmpSpecialEmployee.getNpwp()!=null && sessTmpSpecialEmployee.getNpwp().length()>0 && !sessTmpSpecialEmployee.getNpwp().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getNpwp()  :"-");cell.setCellStyle(style2);
			cell = row.createCell((short) 59);cell.setCellValue(sessTmpSpecialEmployee.getNoKK()!=null && sessTmpSpecialEmployee.getNoKK().length()>0 && !sessTmpSpecialEmployee.getNoKK().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getNoKK()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 60);cell.setCellValue(sessTmpSpecialEmployee.getBpjsNo()!=null && sessTmpSpecialEmployee.getBpjsNo().length()>0 && !sessTmpSpecialEmployee.getBpjsNo().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getBpjsNo()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 61);cell.setCellValue(sessTmpSpecialEmployee.getBpjsDate()!=null ? Formater.formatDate(sessTmpSpecialEmployee.getBpjsDate(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 62);cell.setCellValue(sessTmpSpecialEmployee.getShio()!=null && sessTmpSpecialEmployee.getShio().length()>0 && !sessTmpSpecialEmployee.getShio().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getShio()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 63);cell.setCellValue(sessTmpSpecialEmployee.getElemen()!=null && sessTmpSpecialEmployee.getElemen().length()>0 && !sessTmpSpecialEmployee.getElemen().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getElemen()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 64);cell.setCellValue(sessTmpSpecialEmployee.getIq()!=null && sessTmpSpecialEmployee.getIq().length()>0 && !sessTmpSpecialEmployee.getIq().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getIq()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 65);cell.setCellValue(sessTmpSpecialEmployee.getEq()!=null && sessTmpSpecialEmployee.getEq().length()>0 && !sessTmpSpecialEmployee.getEq().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getEq()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 66);cell.setCellValue(sessTmpSpecialEmployee.getProbationEndDate()!=null ? Formater.formatDate(sessTmpSpecialEmployee.getProbationEndDate(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 67);cell.setCellValue(sessTmpSpecialEmployee.getStatusPensionProgram()!=null && sessTmpSpecialEmployee.getStatusPensionProgram().length()>0 && !sessTmpSpecialEmployee.getStatusPensionProgram().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getStatusPensionProgram()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 68);cell.setCellValue(sessTmpSpecialEmployee.getStartDatePensiun()!=null ? Formater.formatDate(sessTmpSpecialEmployee.getStartDatePensiun(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 69);cell.setCellValue(sessTmpSpecialEmployee.getPresenceCheckParameter()!=null && sessTmpSpecialEmployee.getPresenceCheckParameter().length()>0 && !sessTmpSpecialEmployee.getPresenceCheckParameter().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getPresenceCheckParameter()  :"-");cell.setCellStyle(style2);
            cell = row.createCell((short) 70);cell.setCellValue(sessTmpSpecialEmployee.getMedicalInfo()!=null && sessTmpSpecialEmployee.getMedicalInfo().length()>0 && !sessTmpSpecialEmployee.getMedicalInfo().equalsIgnoreCase("null") ? sessTmpSpecialEmployee.getMedicalInfo()  :"-");cell.setCellStyle(style2);
            
            cell = row.createCell((short) 71);cell.setCellValue(sessTmpSpecialEmployee.getWaCompany()!=null&& sessTmpSpecialEmployee.getWaCompany().length()>0?sessTmpSpecialEmployee.getWaCompany():sessTmpSpecialEmployee.getCompanyName());cell.setCellStyle(style2);   
            cell = row.createCell((short) 72);cell.setCellValue(sessTmpSpecialEmployee.getWaDivision()!=null&& sessTmpSpecialEmployee.getWaDivision().length()>0?sessTmpSpecialEmployee.getWaDivision():sessTmpSpecialEmployee.getDivision());cell.setCellStyle(style2); 
            cell = row.createCell((short) 73);cell.setCellValue(sessTmpSpecialEmployee.getWaDepartement()!=null&& sessTmpSpecialEmployee.getWaDepartement().length()>0?sessTmpSpecialEmployee.getWaDepartement():sessTmpSpecialEmployee.getDepartement());cell.setCellStyle(style2); 
            cell = row.createCell((short) 74);cell.setCellValue(sessTmpSpecialEmployee.getWaSection()!=null&& sessTmpSpecialEmployee.getWaSection().length()>0?sessTmpSpecialEmployee.getWaSection():sessTmpSpecialEmployee.getSection());cell.setCellStyle(style2); 
            cell = row.createCell((short) 75);cell.setCellValue(sessTmpSpecialEmployee.getWaPosition()!=null&& sessTmpSpecialEmployee.getWaPosition().length()>0?sessTmpSpecialEmployee.getWaPosition():sessTmpSpecialEmployee.getPosition());cell.setCellStyle(style2); 
            cell = row.createCell((short) 76);cell.setCellValue(sessTmpSpecialEmployee.getGrade()!=null&& sessTmpSpecialEmployee.getGrade().length()>0?sessTmpSpecialEmployee.getGrade():sessTmpSpecialEmployee.getGrade());cell.setCellStyle(style2); 
			
			//careerpath
            cell = row.createCell((short) 77);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 78);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 79);cell.setCellValue("");cell.setCellStyle(style2); 
            cell = row.createCell((short) 80);cell.setCellValue("");cell.setCellStyle(style2);
			cell = row.createCell((short) 81);cell.setCellValue("");cell.setCellStyle(style2);
			cell = row.createCell((short) 82);cell.setCellValue("");cell.setCellStyle(style2);
			cell = row.createCell((short) 83);cell.setCellValue("");cell.setCellStyle(style2);
			cell = row.createCell((short) 84);cell.setCellValue("");cell.setCellStyle(style2);
			cell = row.createCell((short) 85);cell.setCellValue("");cell.setCellStyle(style2);
            
			// cell = row.createCell((short) 13);cell.setCellValue(PstEmployee.sexKey[employee.getSex()]);cell.setCellStyle(style2); 
            //int coolmsEducation = 13;
           
            int tmpRowEdu = countRow;
            //update by satrya 2013-10-12
            boolean adaListEdu=false;
            if(listEducation!=null && listEducation.size()>0){
                for(int idxEdu=0;idxEdu<listEducation.size();idxEdu++){
                    SessEmpEducation sessEmpEducation = (SessEmpEducation)listEducation.get(idxEdu);
                    if(sessTmpSpecialEmployee.getEmployeeId()==sessEmpEducation.getEmployee_id()){
                       if( 10820==sessTmpSpecialEmployee.getEmployeeId()){
                           boolean test=true;
                       }
                       adaListEdu=true;
                        row = sheet.getRow(tmpRowEdu);
                       if(row==null){
                            row = sheet.createRow((short) (tmpRowEdu));
                            for(int idx=0;idx<15;idx++){
                               cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
                            }
                             for(int idx=19;idx<86;idx++){
                               cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
                            }
                       }
                       
                       
       
                        cell = row.createCell((short) 15);cell.setCellValue(sessEmpEducation.getEducation()!=null?sessEmpEducation.getEducation():"-");cell.setCellStyle(style2); 
                        cell = row.createCell((short) 16);cell.setCellValue(sessEmpEducation.getGraduation()!=null?sessEmpEducation.getGraduation():"-");cell.setCellStyle(style2); 
                        cell = row.createCell((short) 17);cell.setCellValue(sessEmpEducation.getStartDate());cell.setCellStyle(style2); 
                        cell = row.createCell((short) 18);cell.setCellValue(sessEmpEducation.getEndDate());cell.setCellStyle(style2); 
                        
                        listEducation.remove(idxEdu);
                        idxEdu=idxEdu-1;
                        tmpRowEdu=tmpRowEdu+1;
                        
                    }
                }
            }
            //update by satrya 2013-10-12
            //addRow = tmpRowEdu- countRow;
            //if(tmpRowEdu-countRow > addRow){
                     addRow = tmpRowEdu- countRow;
            // }
           /* if(adaListEdu==false){
                 addRow = tmpRowEdu- countRow;
            }*/
        
            int tmpRowLang = countRow;
            boolean adaListLanguange=false;
            if(listLanguage!=null && listLanguage.size()>0){
                for(int idxLang=0;idxLang<listLanguage.size();idxLang++){
                    SessEmpLanguage sessEmpLanguage = (SessEmpLanguage)listLanguage.get(idxLang);
                    if(sessTmpSpecialEmployee.getEmployeeId()==sessEmpLanguage.getEmployee_id()){
                         row = sheet.getRow(tmpRowLang);
                       adaListLanguange = true;  
                       if(row==null){
                            row = sheet.createRow((short) (tmpRowLang));
                           for(int idx=0;idx<19;idx++){
                            cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
                           }
                           for(int idx=23;idx<46;idx++){
                            cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
                           }
                           //update by satrya 2013-10-17
                           //karena ada yg pecah"
                           for(int idx=46;idx<86;idx++){
                            cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
                           }
                       }

                        cell = row.createCell((short) 19);cell.setCellValue(sessEmpLanguage.getLanguage()!=null?sessEmpLanguage.getLanguage():"-");cell.setCellStyle(style2); 
                        cell = row.createCell((short) 20);cell.setCellValue(sessEmpLanguage.getOral()!=null?sessEmpLanguage.getOral():"-");cell.setCellStyle(style2); 
                        cell = row.createCell((short) 21);cell.setCellValue(sessEmpLanguage.getWritten()!=null?sessEmpLanguage.getWritten():"-");cell.setCellStyle(style2); 
                        cell = row.createCell((short) 22);cell.setCellValue(sessEmpLanguage.getDescription()!=null?sessEmpLanguage.getDescription():"-");cell.setCellStyle(style2); 
                        tmpRowLang=tmpRowLang+1;
                        listLanguage.remove(idxLang);                              
                        idxLang=idxLang-1;
                        
                        
                        
                    }
                }
            }
            
           /**
            * update by satrya 2013-10-12
            *  if(tmpRowLang-countRow > addRow){
                     addRow = tmpRowLang - countRow;
             }
            */
           //if(adaListLanguange){
             if(tmpRowLang-countRow > addRow){
                     addRow = tmpRowLang - countRow;
             }
           //}  
             int tmpFam=countRow;
             boolean adaListFamily=false;
             if(listFamily!=null && listFamily.size()>0){
                 for(int idxFam=0; idxFam<listFamily.size();idxFam++){
                     SessEmpFamilyMember  sessEmpFamilyMember = (SessEmpFamilyMember)listFamily.get(idxFam);
                     if(sessEmpFamilyMember.getEmployeeId()==sessTmpSpecialEmployee.getEmployeeId()){
                          row = sheet.getRow(tmpFam);
                          adaListFamily=true;
                            if(row==null){
                                 row = sheet.createRow((short) (tmpFam));
                                 for(int idx=0;idx<46;idx++){
                                    cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
                                  } 
								 for(int idx=56;idx<86;idx++){
                                    cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
                                  } 
                            }

                            cell = row.createCell((short) 46);cell.setCellValue( sessEmpFamilyMember.getFullName()!=null && sessEmpFamilyMember.getFullName().length()>0? sessEmpFamilyMember.getFullName()  :"-");cell.setCellStyle(style2);
                            cell = row.createCell((short) 47);cell.setCellValue( PstEmployee.sexKey[sessEmpFamilyMember.getSex()]);cell.setCellStyle(style2);
                            cell = row.createCell((short) 48);cell.setCellValue( sessEmpFamilyMember.getFamilyRelation()!=null && sessEmpFamilyMember.getFamilyRelation().length()>0? sessEmpFamilyMember.getFamilyRelation() :"-");cell.setCellStyle(style2);
                            
                            cell = row.createCell((short) 49);cell.setCellValue( sessEmpFamilyMember.getGuaranted()==0? "no" :"yes");cell.setCellStyle(style2);
                            cell = row.createCell((short) 50);cell.setCellValue( sessEmpFamilyMember.getBirthDate()!=null? Formater.formatDate(sessEmpFamilyMember.getBirthDate(), "yyyy-MM-dd") :"-");cell.setCellStyle(style2);
                            cell = row.createCell((short) 51);cell.setCellValue( sessEmpFamilyMember.getUmur());cell.setCellStyle(style2);
                            cell = row.createCell((short) 52);cell.setCellValue( sessEmpFamilyMember.getReligion()!=null && sessEmpFamilyMember.getReligion().length()>0?sessEmpFamilyMember.getReligion():"-");cell.setCellStyle(style2);
                            cell = row.createCell((short) 53);cell.setCellValue( sessEmpFamilyMember.getJob()!=null && sessEmpFamilyMember.getJob().length()>0?sessEmpFamilyMember.getJob():"-");cell.setCellStyle(style2);
                            cell = row.createCell((short) 54);cell.setCellValue( sessEmpFamilyMember.getEducation()!=null && sessEmpFamilyMember.getEducation().length()>0?sessEmpFamilyMember.getEducation():"-");cell.setCellStyle(style2);
                            cell = row.createCell((short) 55);cell.setCellValue( sessEmpFamilyMember.getAddress()!=null && sessEmpFamilyMember.getAddress().length()>0?sessEmpFamilyMember.getAddress():"-");cell.setCellStyle(style2);
                            tmpFam=tmpFam+1;
                            listFamily.remove(idxFam);                              
                        idxFam=idxFam-1;
                     }
                 }
             }
             /**
              * update by satrya 2013-10-12
              * if(tmpFam-countRow > addRow){
                     addRow = tmpFam - countRow;
             }
              */
            //if(adaListFamily){
              if(tmpFam-countRow > addRow){
                     addRow = tmpFam - countRow;
             }
            //}
			
			int tmpCareer=countRow;
             if(listCareer!=null && listCareer.size()>0){
                 for(int idxCareer=0; idxCareer<listCareer.size();idxCareer++){
                     CareerPath  careerPath = (CareerPath)listCareer.get(idxCareer);
						row = sheet.getRow(tmpCareer);
					  if(row==null){
						   row = sheet.createRow((short) (tmpCareer));
						   for(int idx=0;idx<77;idx++){
							  cell = row.createCell((short) idx);cell.setCellValue("");cell.setCellStyle(style2); 
							} 
					  }

					  cell = row.createCell((short) 77);cell.setCellValue( careerPath.getCompanyId()>0 ? PstCareerPath.getCompany(""+careerPath.getCompanyId())  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 78);cell.setCellValue( careerPath.getDivisionId()>0 ? PstCareerPath.getDivision(""+careerPath.getDivisionId())  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 79);cell.setCellValue( careerPath.getDepartmentId()>0 ? PstCareerPath.getDepartment(""+careerPath.getDepartmentId())  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 80);cell.setCellValue( careerPath.getSectionId()>0 ? PstCareerPath.getSection(""+careerPath.getSectionId())  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 81);cell.setCellValue( careerPath.getPositionId()>0 ? PstCareerPath.getPosition(""+careerPath.getPositionId())  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 82);cell.setCellValue( careerPath.getLevelId()>0 ? PstCareerPath.getLevel(""+careerPath.getLevelId())  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 83);cell.setCellValue( careerPath.getEmpCategoryId()>0 ? PstCareerPath.getCategory(""+careerPath.getEmpCategoryId())  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 84);cell.setCellValue( careerPath.getWorkFrom() != null ? Formater.formatDate(careerPath.getWorkFrom(), "yyyy-MM-dd")  :"-");cell.setCellStyle(style2);
					  cell = row.createCell((short) 85);cell.setCellValue( careerPath.getWorkTo() != null ? Formater.formatDate(careerPath.getWorkTo(), "yyyy-MM-dd")  :"-");cell.setCellStyle(style2);
					  tmpCareer=tmpCareer+1;    
                 }
             }
             /**
              * update by satrya 2013-10-12
              * if(tmpFam-countRow > addRow){
                     addRow = tmpFam - countRow;
             }
              */
            //if(adaListFamily){
              if(tmpCareer-countRow > addRow){
                     addRow = tmpCareer - countRow;
             }
			  if (addRow < 8){
				  addRow = 8;
			  }
            //}
			
          }catch(Exception exc){
                System.out.println("Exc "+ exc+" Employe Num "+sessTmpSpecialEmployee.getEmployeeNum());
           }  
          tmpx=tmpx+1;
          }
        }    
	 
	 for (int i=2; i < 86;i++){
		 sheet.autoSizeColumn(i);
	 }
       
         
/* BY KARTIKA , di hide sementara tgl. 25 September 2012        
        row = sheetFamily.createRow((short) (listEmployee.size() + 4)); //ngakalin yang ilang saat ganjil
        cell = row.createCell((short) 0);
        cell.setCellValue("");
        cell.setCellStyle(style2);
        row = sheetCarrier.createRow((short) (listEmployee.size() + 4)); //ngakalin yang ilang saat ganjil
        cell = row.createCell((short) 0);
        cell.setCellValue("");
        cell.setCellStyle(style2);


        int akhir = 0;
        //int batas = 0;
        int awal = 0;
        batas[1] = 0;
        batas[2] = 0;
        batas[3] = 0;
        batas[4] = 0;
        batas[5] = 0;
        batas[6] = 0;


        //untuk language

        int loop = PstEmpLanguage.getCount2(srcEmployee);
        if (loop > 0) {
            int b = tableSubHeader.length;
            for (int a = 0; a < loop; a++) {

                String[] tableSubHeader2 = {
                    "Language", "Oral", "Written", "Description"
                };
                awal = tableSubHeader.length + (tableSubHeader2.length * a);
                akhir = tableSubHeader2.length + tableSubHeader.length + (tableSubHeader2.length * a);
                batas[1] = akhir;

                row = sheet.createRow((short) (2));

                for (int k = awal; k < akhir; k++) {

                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeader2[k - (tableSubHeader.length + (tableSubHeader2.length * a))]);
                    cell.setCellStyle(style3);

                }

                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);

                    row = sheet.createRow((short) (i + 3));
                    Vector vctLanguage = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstEmpLanguage.fieldNames[PstEmpLanguage.FLD_EMPLOYEE_ID] + " = " + oidEmployee;
                    String orderClause = "";
                    vctLanguage = PstEmpLanguage.list(0, 0, whereClause, orderClause);
                    EmpLanguage empLanguage = new EmpLanguage();

                    Education education = new Education();
                    Religion religion = new Religion();

                    if (vctLanguage != null && vctLanguage.size() > a) {
                        empLanguage = (EmpLanguage) vctLanguage.get(a);

                        String lang = String.valueOf(empLanguage.getLanguageId());
                        String langName = "";
                        langName = PstEmpLanguage.getLang(lang);



                        cell = row.createCell((short) b);
                        cell.setCellValue(PstEmpLanguage.getLang(lang));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(empLanguage.getOral() == 0 ? "Good" : empLanguage.getOral() == 1 ? "Fair" : "Poor");
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(empLanguage.getWritten() == 0 ? "Good" : empLanguage.getWritten() == 1 ? "Fair" : "Poor");
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(empLanguage.getDescription());
                        cell.setCellStyle(style2);
                    }

                }
                b = b + tableSubHeader2.length;
            }
        } else {
            batas[1] = batas[0];
            akhir = tableSubHeader.length;
        }

        //untuk education
        int loop2 = PstEmpEducation.getCount2(srcEmployee);
        int akhir2 = 0;
        if (loop2 > 0) {
            int b = akhir + akhir2;
            for (int a = 0; a < loop2; a++) {

                String[] tableSubHeader3 = {
                    "Education", "Education Desc", "Start Date", "End Date", "Graduation"
                };
                awal = akhir + akhir2;
                akhir2 = batas[1] + tableSubHeader3.length + (tableSubHeader3.length * a);
                akhir = 0;
                batas[2] = akhir2;
                row = sheet.createRow((short) (2));


                for (int k = awal; k < akhir2; k++) {

                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeader3[k - (batas[1] + (tableSubHeader3.length * a))]);
                    cell.setCellStyle(style3);

                }
                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);

                    row = sheet.createRow((short) (i + 3));
                    Vector vctEducation = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstEmpEducation.fieldNames[PstEmpEducation.FLD_EMPLOYEE_ID] + " = " + oidEmployee;
                    String orderClause = PstEmpEducation.fieldNames[PstEmpEducation.FLD_START_DATE];
                    vctEducation = PstEmpEducation.list(0, 0, whereClause, orderClause);
                    EmpEducation empEducation = new EmpEducation();

                    if (vctEducation != null && vctEducation.size() > a) {
                        empEducation = (EmpEducation) vctEducation.get(a);
                        String edu = String.valueOf(empEducation.getEducationId());
                        String eduName = "";
                        eduName = PstEmpEducation.getEducation(edu);

                        cell = row.createCell((short) b);
                        cell.setCellValue(PstEmpEducation.getEducation(edu));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(empEducation.getEducationDesc());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(empEducation.getStartDate());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(empEducation.getEndDate());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 4));
                        cell.setCellValue(empEducation.getGraduation());
                        cell.setCellStyle(style2);
                    }

                }
                b = b + tableSubHeader3.length;


            }
        } else {
            batas[2] = batas[1];
            akhir2 = akhir;
        }

        //untuk experience
        int loop3 = PstExperience.getCount2(srcEmployee);
        int akhir3 = 0;
        if (loop3 > 0) {
            int b = akhir2 + akhir3;
            for (int a = 0; a < loop3; a++) {

                String[] tableSubHeader4 = {
                    "Company Name", "Start Date", "End Date", "Position", "Move Reason"
                };
                awal = akhir2 + akhir3;
                akhir3 = batas[2] + tableSubHeader4.length + (tableSubHeader4.length * a);
                akhir2 = 0;
                batas[3] = akhir3;
                row = sheet.createRow((short) (2));

                for (int k = awal; k < akhir3; k++) {
                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeader4[k - (batas[2] + (tableSubHeader4.length * a))]);
                    cell.setCellStyle(style3);
                }
                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);

                    row = sheet.createRow((short) (i + 3));
                    Vector vctExperience = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstExperience.fieldNames[PstExperience.FLD_EMPLOYEE_ID] + " = " + oidEmployee;
                    String orderClause = PstExperience.fieldNames[PstExperience.FLD_START_DATE];
                    vctExperience = PstExperience.list(0, 0, whereClause, orderClause);
                    Experience experience = new Experience();



                    if (vctExperience != null && vctExperience.size() > a) {
                        experience = (Experience) vctExperience.get(a);

                        cell = row.createCell((short) b);
                        cell.setCellValue(experience.getCompanyName());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(experience.getStartDate());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(experience.getEndDate());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(experience.getPosition());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 4));
                        cell.setCellValue(experience.getMoveReason());
                        cell.setCellStyle(style2);
                    }

                }
                b = b + tableSubHeader4.length;

            }
        } else {
            batas[3] = batas[2];
            akhir3 = akhir2;
        }

        //untuk warning
        int loop4 = PstEmpWarning.getCount2(srcEmployee);
        int akhir4 = 0;
        if (loop4 > 0) {
            int b = akhir3 + akhir4;
            for (int a = 0; a < loop4; a++) {

                String[] tableSubHeader5 = {
                    "Break Date", "Break Fact", "Warning Date", "Warning By", "Valid Until", "Level", "Point"
                };
                awal = akhir3 + akhir4;
                akhir4 = batas[3] + tableSubHeader5.length + (tableSubHeader5.length * a);
                akhir3 = 0;
                batas[4] = akhir4;
                row = sheet.createRow((short) (2));

                for (int k = awal; k < akhir4; k++) {
                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeader5[k - (batas[3] + (tableSubHeader5.length * a))]);
                    cell.setCellStyle(style3);
                }
                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);

                    row = sheet.createRow((short) (i + 3));
                    Vector vctWarning = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstEmpWarning.fieldNames[PstEmpWarning.FLD_EMPLOYEE_ID] + "=" + oidEmployee;
                    String orderClause = PstEmpWarning.fieldNames[PstEmpWarning.FLD_BREAK_DATE];
                    vctWarning = PstEmpWarning.list(0, 0, whereClause, orderClause);
                    EmpWarning empWarning = new EmpWarning();



                    if (vctWarning != null && vctWarning.size() > a) {
                        empWarning = (EmpWarning) vctWarning.get(a);
                        String warningLevel = "";
                        String level = String.valueOf(empWarning.getWarnLevelId());
                        warningLevel = PstEmpWarning.getWarning(level);
                        String warningPoint = "";
                        String pointLvl = String.valueOf(empWarning.getWarnLevelId());
                        warningPoint = PstEmpWarning.getWarningPoint(pointLvl);

                        cell = row.createCell((short) b);
                        cell.setCellValue(empWarning.getBreakDate() == null ? "" : String.valueOf(Formater.formatDate(empWarning.getBreakDate(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(empWarning.getBreakFact());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(empWarning.getWarningDate() == null ? "" : String.valueOf(Formater.formatDate(empWarning.getWarningDate(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(empWarning.getWarningBy());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 4));
                        cell.setCellValue(empWarning.getValidityDate() == null ? "" : String.valueOf(Formater.formatDate(empWarning.getValidityDate(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 5));
                        cell.setCellValue(PstEmpWarning.getWarning(level));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 6));
                        cell.setCellValue(PstEmpWarning.getWarningPoint(pointLvl));
                        cell.setCellStyle(style2);//Point ??
                    }

                }
                b = b + tableSubHeader5.length;

            }
        } else {
            batas[4] = batas[3];
            akhir4 = akhir3;
        }

        //untuk reprimand
        int loop5 = PstEmpReprimand.getCount2(srcEmployee);
        int akhir5 = 0;
        if (loop5 > 0) {
            int b = akhir4 + akhir5;
            for (int a = 0; a < loop5; a++) {

                String[] tableSubHeader6 = {
                    "Date", "Chapter", "Article", "Page", "Description", "Valid Until", "Level", "Point"
                };
                awal = akhir4 + akhir5;
                akhir5 = batas[4] + tableSubHeader6.length + (tableSubHeader6.length * a);
                akhir4 = 0;
                batas[5] = akhir5;
                row = sheet.createRow((short) (2));

                for (int k = awal; k < akhir5; k++) {
                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeader6[k - (batas[4] + (tableSubHeader6.length * a))]);
                    cell.setCellStyle(style3);
                }
                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);

                    row = sheet.createRow((short) (i + 3));
                    Vector vctReprimand = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstEmpReprimand.fieldNames[PstEmpReprimand.FLD_EMPLOYEE_ID] + "=" + oidEmployee;
                    String orderClause = PstEmpReprimand.fieldNames[PstEmpReprimand.FLD_REP_DATE];
                    vctReprimand = PstEmpReprimand.list(0, 0, whereClause, orderClause);
                    EmpReprimand empReprimand = new EmpReprimand();



                    if (vctReprimand != null && vctReprimand.size() > a) {
                        empReprimand = (EmpReprimand) vctReprimand.get(a);
                        String reprimandLevel = "";
                        String level = String.valueOf(empReprimand.getReprimandLevelId());
                        reprimandLevel = PstEmpReprimand.getReprimand(level);
                        String reprimandPoint = "";
                        String pointLvl = String.valueOf(empReprimand.getReprimandLevelId());
                        reprimandPoint = PstEmpReprimand.getReprimandPoint(pointLvl);

                        cell = row.createCell((short) b);
                        cell.setCellValue(empReprimand.getReprimandDate() == null ? "" : String.valueOf(Formater.formatDate(empReprimand.getReprimandDate(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(empReprimand.getChapter());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(empReprimand.getArticle());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(empReprimand.getPage());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 4));
                        cell.setCellValue(empReprimand.getDescription());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 5));
                        cell.setCellValue(empReprimand.getValidityDate() == null ? "" : String.valueOf(Formater.formatDate(empReprimand.getValidityDate(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 6));
                        cell.setCellValue(PstEmpReprimand.getReprimand(level));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 7));
                        cell.setCellValue(PstEmpReprimand.getReprimandPoint(pointLvl));
                        cell.setCellStyle(style2);
                    }

                }
                b = b + tableSubHeader6.length;

            }
        } else {
            batas[5] = batas[4];
            akhir5 = akhir4;
        }

        //untuk award
        int loop6 = PstEmpAward.getCount2(srcEmployee);
        int akhir6 = 0;
        if (loop6 > 0) {
            int b = akhir5 + akhir6;
            for (int a = 0; a < loop6; a++) {

                String[] tableSubHeader7 = {
                    "Date", "Dept. On Award", "Sect. On Award", "Type", "Description"
                };
                awal = akhir5 + akhir6;
                akhir6 = batas[5] + tableSubHeader7.length + (tableSubHeader7.length * a);
                akhir5 = 0;
                batas[6] = akhir6;
                row = sheet.createRow((short) (2));

                for (int k = awal; k < akhir6; k++) {
                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeader7[k - (batas[5] + (tableSubHeader7.length * a))]);
                    cell.setCellStyle(style3);
                }
                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);

                    row = sheet.createRow((short) (i + 3));
                    Vector vctAward = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstEmpAward.fieldNames[PstEmpAward.FLD_EMPLOYEE_ID] + "=" + oidEmployee;
                    String orderClause = PstEmpAward.fieldNames[PstEmpAward.FLD_AWARD_DATE];
                    vctAward = PstEmpAward.list(0, 0, whereClause, orderClause);
                    EmpAward empAward = new EmpAward();



                    if (vctAward != null && vctAward.size() > a) {
                        empAward = (EmpAward) vctAward.get(a);
                        String awardLevel = "";
                        String level = String.valueOf(empAward.getAwardType());
                        awardLevel = PstEmpAward.getAward(level);
                        String department = "";
                        String dept = String.valueOf(empAward.getDepartmentId());
                        PstEmpAward.getDepartment(dept);
                        String section = "";
                        String sect = String.valueOf(empAward.getSectionId());
                        PstEmpAward.getSection(sect);



                        cell = row.createCell((short) b);
                        cell.setCellValue(empAward.getAwardDate() == null ? "" : String.valueOf(Formater.formatDate(empAward.getAwardDate(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(PstEmpAward.getDepartment(dept));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(PstEmpAward.getSection(sect));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(PstEmpAward.getAward(level));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 4));
                        cell.setCellValue(empAward.getAwardDescription());
                        cell.setCellStyle(style2);

                    }

                }
                b = b + tableSubHeader7.length;

            }
        } else {
            batas[6] = batas[5];
        }

        //sheetFamily
        //untuk personal data
        String[] tableSubHeaderFam = {
            "Payroll", "Full Name"
        };
        int[] batasFam;
        batasFam = new int[2];
        batasFam[0] = tableSubHeaderFam.length;
        row = sheetFamily.createRow((short) (2));
        for (int k = 0; k < tableSubHeaderFam.length; k++) {
            cell = row.createCell((short) k);
            cell.setCellValue(tableSubHeaderFam[k]);
            cell.setCellStyle(style3);
        }


        srcEmployee = (SrcEmployee) session.getValue(SessEmployee.SESS_SRC_EMPLOYEE);
        listEmployee = sessEmployee.searchEmployee(srcEmployee, 0, 5000);

        //String d = "";
        for (int i = 0; i < listEmployee.size(); i++) {
            Vector temp = (Vector) listEmployee.get(i);
            Employee employee = (Employee) temp.get(0);

            row = sheetFamily.createRow((short) (i + 3));

            cell = row.createCell((short) 0);
            cell.setCellValue(""+employee.getEmployeeNum());
            cell.setCellStyle(style2);
            cell = row.createCell((short) 1);
            cell.setCellValue(""+employee.getFullName());
            cell.setCellStyle(style2);

        }
        
        
        
        //untuk family member
        int loopFam = PstFamilyMember.getCount2(srcEmployee); //kalo count ganjil(ex 5) header2 terakhir gak muncul (religion)?
        if (loopFam > 0) {

            int b = batasFam[0];
            for (int a = 0; a < loopFam; a++) {

                String[] tableSubHeaderFam2 = {
                    "Full Name", "Sex", "Relationship", "Guaranted",
                    "Date Of Birth", "Job", "Address", "Education", "Religion"
                };
                awal = tableSubHeaderFam.length + (tableSubHeaderFam2.length * a);
                akhir = tableSubHeaderFam2.length + tableSubHeaderFam.length + (tableSubHeaderFam2.length * a);
                batasFam[1] = akhir;
                row = sheetFamily.createRow((short) (2));


                for (int k = awal; k < akhir; k++) {

                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeaderFam2[k - (tableSubHeaderFam.length + (tableSubHeaderFam2.length * a))]);
                    cell.setCellStyle(style3);

                }

                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);


                    row = sheetFamily.createRow((short) (i + 3));
                    Vector vctMember = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID] + " = " + oidEmployee;
                    String orderClause = "";
                    orderClause = PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + "," + PstFamilyMember.fieldNames[PstFamilyMember.FLD_BIRTH_DATE] + " ASC";
                    vctMember = PstFamilyMember.list(0, 0, whereClause, orderClause);
                    FamilyMember famX = new FamilyMember();
                    Education education = new Education();
                    Religion religion = new Religion();

                    if (vctMember != null && vctMember.size() > 0 && vctMember.size() > a) {
                        famX = (FamilyMember) vctMember.get(a);
                        //education = (Education) vctMember.get(a);
                        //religion = (Religion) vctMember.get(a);
                 //*gak jadi dipake
                   //     String relationshipName = "";
                    //    relationshipName = PstFamilyMember.getRelation(famX.getRelationship());
                     //    *
                      //   *
                        String edu = String.valueOf(famX.getEducationId());
                        String educationName = "";
                        educationName = PstFamilyMember.getEducation(edu);
                        String religi = String.valueOf(famX.getReligionId());
                        String religionName = "";
                        religionName = PstFamilyMember.getReligion(religi);

                        cell = row.createCell((short) b);
                        cell.setCellValue(famX.getFullName());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(famX.getSex() == 0 ? "Male" : "Female");
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(famX.getRelationship());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(famX.getGuaranteed() == true ? "Guaranted" : "");
                        cell.setCellStyle(style2);//ignore blum dimasukan
                        cell = row.createCell((short) (b + 4));
                        cell.setCellValue(famX.getBirthDate() == null ? "" : String.valueOf(Formater.formatDate(famX.getBirthDate(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 5));
                        cell.setCellValue(famX.getJob());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 6));
                        cell.setCellValue(famX.getAddress());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 7));
                        cell.setCellValue(PstFamilyMember.getEducation(edu));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 8));
                        cell.setCellValue(PstFamilyMember.getReligion(religi));
                        cell.setCellStyle(style2);

                    }

                }
                b = b + tableSubHeaderFam2.length;
            }
        } else {
            batasFam[1] = batasFam[0];
        }

        //sheetCarrier
        //untuk personal data
        String[] tableSubHeaderCarr = {
            "Payroll", "Full Name"
        };
        int[] batasCarr;
        batasCarr = new int[2];
        batasCarr[0] = tableSubHeaderCarr.length;
        row = sheetCarrier.createRow((short) (2));
        for (int k = 0; k < tableSubHeaderCarr.length; k++) {
            cell = row.createCell((short) k);
            cell.setCellValue(tableSubHeaderCarr[k]);
            cell.setCellStyle(style3);
        } // }


        srcEmployee = (SrcEmployee) session.getValue(SessEmployee.SESS_SRC_EMPLOYEE);
        listEmployee = sessEmployee.searchEmployee(srcEmployee, 0, 5000);

        for (int i = 0; i < listEmployee.size(); i++) {
            Vector temp = (Vector) listEmployee.get(i);
            Employee employee = (Employee) temp.get(0);

            row = sheetCarrier.createRow((short) (i + 3));

            cell = row.createCell((short) 0);
            cell.setCellValue(""+employee.getEmployeeNum());
            cell.setCellStyle(style2);
            cell = row.createCell((short) 1);
            cell.setCellValue(""+employee.getFullName());
            cell.setCellStyle(style2);

        }
        //untuk career path
        int loopCarr = PstCareerPath.getCount2(srcEmployee);
        if (loopCarr > 0) {
            int b = batasCarr[0];
            for (int a = 0; a < loopCarr; a++) {

                String[] tableSubHeaderCarr2 = {
                    "Company", "Division", "Department", "Section", "Position", "Level", "Emp Catagory", "Work From", "Work To", "Salary", "Description"
                };
                awal = tableSubHeaderCarr.length + (tableSubHeaderCarr2.length * a);
                akhir = tableSubHeaderCarr2.length + tableSubHeaderCarr.length + (tableSubHeaderCarr2.length * a);
                batasCarr[1] = akhir;
                row = sheetCarrier.createRow((short) (2));

                for (int k = awal; k < akhir; k++) {
                    cell = row.createCell((short) k);
                    cell.setCellValue(tableSubHeaderCarr2[k - (tableSubHeaderCarr.length + (tableSubHeaderCarr2.length * a))]);
                    cell.setCellStyle(style3);

                }
                for (int i = 0; i < listEmployee.size(); i++) {
                    Vector temp = (Vector) listEmployee.get(i);
                    Employee employee = (Employee) temp.get(0);

                    row = sheetCarrier.createRow((short) (i + 3));
                    Vector vctCareer = new Vector(1, 1);
                    long oidEmployee = employee.getOID();
                    String whereClause = PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + " = " + oidEmployee;
                    String orderClause = PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM];
                    vctCareer = PstCareerPath.list(0, 0, whereClause, orderClause);
                    CareerPath careerPath = new CareerPath();

                    if (vctCareer != null && vctCareer.size() > a) {
                        careerPath = (CareerPath) vctCareer.get(a);

                        String company = "";
                        String comp = String.valueOf(careerPath.getCompanyId());
                        PstCareerPath.getCompany(comp);
                        String division = "";
                        String div = String.valueOf(careerPath.getDivisionId());
                        PstCareerPath.getDivision(div);
                        String department = "";
                        String dept = String.valueOf(careerPath.getDepartmentId());
                        PstCareerPath.getDepartment(dept);
                        String section = "";
                        String sect = String.valueOf(careerPath.getSectionId());
                        PstCareerPath.getSection(sect);
                        String position = "";
                        String pos = String.valueOf(careerPath.getPositionId());
                        PstCareerPath.getPosition(pos);
                        String levelCareer = "";
                        String lev = String.valueOf(careerPath.getLevelId());
                        PstCareerPath.getLevel(lev);
                        String cat = String.valueOf(careerPath.getEmpCategoryId());
                        PstCareerPath.getCategory(cat);



                        cell = row.createCell((short) b);
                        cell.setCellValue(PstCareerPath.getCompany(comp));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 1));
                        cell.setCellValue(PstCareerPath.getDivision(div));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 2));
                        cell.setCellValue(PstCareerPath.getDepartment(dept));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 3));
                        cell.setCellValue(PstCareerPath.getSection(sect));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 4));
                        cell.setCellValue(PstCareerPath.getPosition(pos));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 5));
                        cell.setCellValue(PstCareerPath.getLevel(lev));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 6));
                        cell.setCellValue(PstCareerPath.getCategory(cat));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 7));
                        cell.setCellValue(careerPath.getWorkFrom() == null ? "" : String.valueOf(Formater.formatDate(careerPath.getWorkFrom(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 8));
                        cell.setCellValue(careerPath.getWorkTo() == null ? "" : String.valueOf(Formater.formatDate(careerPath.getWorkTo(), "dd MMM yyyy")));
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 9));
                        cell.setCellValue(careerPath.getSalary());
                        cell.setCellStyle(style2);
                        cell = row.createCell((short) (b + 10));
                        cell.setCellValue(careerPath.getDescription());
                        cell.setCellStyle(style2);

                    }

                }
                b = b + tableSubHeaderCarr2.length;

            }
        } else {
            batasCarr[1] = batasCarr[0];
        }

        //untuk header
        String[] tableHeader = {
            "Personal Data", "Language", "Education", "Experience", "Warning",
            "Reprimand", "Award"
        };

        int k = 0;
        int i = 0;
        row = sheet.createRow((short) (1));
        while (k < batas[6]) {
            cell = row.createCell((short) k);
            cell.setCellValue(tableHeader[i]);
            cell.setCellStyle(style);
            k = batas[i];
            i += 1;
        }

        //untuk headerFam
        String[] tableHeaderFam = {
            "Personal Data", "Family Member"
        };

        int l = 0;
        int j = 0;
        row = sheetFamily.createRow((short) (1));
        while (l < batasFam[1]) {
            cell = row.createCell((short) l);
            cell.setCellValue(tableHeaderFam[j]);
            cell.setCellStyle(style);
            l = batasFam[j];
            j += 1;
        }

        //untuk headerCarr
        String[] tableHeaderCarr = {
            "Personal Data", "Career Path"
        };
        int d = 0;
        int e = 0;
        row = sheetCarrier.createRow((short) (1));
        while (d < batasCarr[1]) {
            cell = row.createCell((short) d);
            cell.setCellValue(tableHeaderCarr[e]);
            cell.setCellStyle(style);
            d = batasCarr[e];
            e += 1;
        }


// }
*/
        // Write the output to a file
        //FileOutputStream fileOut = new FileOutputStream("workbook.xls");
        //wb.write(fileOut);
        //fileOut.close();        
        ServletOutputStream sos = response.getOutputStream();        
        wb.write(sos);
        sos.flush();
        sos.close();
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
	
	public BufferedImage resize(final File url, final Dimension size) throws IOException{
		final BufferedImage image = ImageIO.read(url);
		final BufferedImage resized = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = resized.createGraphics();
		g.drawImage(image, 0, 0, size.width, size.height, null);
		g.dispose();
		return resized;
	}
	
}
