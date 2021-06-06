<%@page import="com.dimata.harisma.entity.log.PstLogSysHistory"%>
<%@page import="com.dimata.harisma.entity.log.LogSysHistory"%>
<%@ page language = "java" %>
<%@page import="com.dimata.harisma.entity.payroll.PstPayComponent"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.System"%>
<%@ page import="java.io.ByteArrayInputStream"%>
<%@ page import="java.io.IOException"%>
<%@ page import="java.text.NumberFormat"%>

<%@ page import="com.dimata.util.Excel"%>
<%@ page import="com.dimata.util.Formater"%>
<%@ page import="com.dimata.util.blob.TextLoader" %>
<%@ page import="org.apache.poi.hssf.usermodel.*" %>

<%@ page import="org.apache.poi.poifs.filesystem.POIFSFileSystem" %>
<%@ page import="org.apache.poi.hssf.record.*" %>
<%@ page import="org.apache.poi.hssf.model.*" %>
<%@ page import="org.apache.poi.hssf.usermodel.*" %>
<%@ page import="org.apache.poi.hssf.util.*" %>


<%@ page import="com.dimata.util.*" %>
<%@ page import="com.dimata.gui.jsp.*" %>
<%@ page import="com.dimata.qdep.form.*" %>
<%@ page import="com.dimata.harisma.entity.masterdata.*" %>
<%@ page import="com.dimata.harisma.entity.employee.*" %>
<%@ page import="com.dimata.harisma.entity.attendance.*" %>
<%@ page import="com.dimata.harisma.entity.payroll.*" %>

<%@ include file = "../../main/javainit.jsp" %>


<%

 Vector payGroupX  = (Vector) session.getValue("payGroup");
 PayPeriod payPeriod = (PayPeriod) session.getValue("PERIOD_UPLOAD_EXCEL");
 String paygroupId = " ";
 
 if (payGroupX.size()>0){
     for (int x=0; x<payGroupX.size(); x++){
         String n = (String) payGroupX.get(x);
         paygroupId = paygroupId + n + ",";
     }
 }
 paygroupId = paygroupId.substring(0, paygroupId.length()-1);
 // Period period = (Period) session.getValue("PERIOD_UPLOAD_EXCEL");
 if(payPeriod==null){
     payPeriod = new PayPeriod();
 }
    int NUM_HEADER = 2;
	int NUM_CELL = 0;//22;
        
    int ROW_COMP_MIN =3;
    int ROW_COMP_MAX =5; 
    int COL_PAYROLL_MIN=0;
    int COL_PAYROLL_MAX=3;     
	
    int iCommand = FRMQueryString.requestCommand(request);
    Date curr_date = FRMQueryString.requestDate(request,"curr_date");
 

    String msgString = "";
    if(iCommand == Command.SAVE){
        if(curr_date == null)
            msgString = "<div class=\"errfont\">Please choose the Period of Working Schedule !</div>";
        else{
            String[] employeeId = request.getParameterValues("employee_id");
            String[] los1  = request.getParameterValues("LOS1");
            String[] los2  = request.getParameterValues("LOS2");
            String[] curr_basic = request.getParameterValues("CURR_BASIC");
            String[] curr_trans = request.getParameterValues("CURR_TRANS");
            String[] curr_total = request.getParameterValues("CURR_TOTAL");
            String[] new_basic  = request.getParameterValues("NEW_BASIC");
            String[] new_trans  = request.getParameterValues("NEW_TRANS");
            String[] new_total  = request.getParameterValues("NEW_TOTAL");
            String[] inc_basic  = request.getParameterValues("INC_BASIC");
            String[] inc_trans  = request.getParameterValues("INC_TRANS");
            String[] inc_add    = request.getParameterValues("INC_ADD");
            String[] inc_total  = request.getParameterValues("INC_TOTAL");
            String[] pct_basic  = request.getParameterValues("PCT_BASIC");
            String[] pct_trans  = request.getParameterValues("PCT_TRANS");
            String[] pct_total  = request.getParameterValues("PCT_TOTAL");

            for(int e=0; e<employeeId.length; e++){
                String where = "";
                    where += PstEmpSalary.fieldNames[PstEmpSalary.FLD_CURR_DATE] + "='" + Formater.formatDate(curr_date, "yyyy-MM-dd") + "' ";
                    where += " AND " + PstEmpSalary.fieldNames[PstEmpSalary.FLD_EMPLOYEE_ID];
                    where += "=" + employeeId[e];
                Vector vcheck = PstEmpSalary.list(0, 0, where, "");

                if (vcheck.size() > 0) {
                    System.out.println("...update");
                    try{
                        EmpSalary empSalary = (EmpSalary) vcheck.get(0);
                        empSalary.setEmployeeId(Long.parseLong(""+employeeId[e]));
                        empSalary.setCurrDate(curr_date);
                        empSalary.setLos1(Integer.parseInt(los1[e]));
                        empSalary.setLos2(Integer.parseInt(los2[e]));
                        empSalary.setCurrBasic(Double.parseDouble(curr_basic[e]));
                        empSalary.setCurrTransport(Double.parseDouble(curr_trans[e]));
                        empSalary.setCurrTotal(Double.parseDouble(curr_total[e]));
                        empSalary.setNewBasic(Double.parseDouble(new_basic[e]));
                        empSalary.setNewTransport(Double.parseDouble(new_trans[e]));
                        empSalary.setNewTotal(Double.parseDouble(new_total[e]));
                        empSalary.setIncSalary(Double.parseDouble(inc_basic[e]));
                        empSalary.setIncTotal(Double.parseDouble(inc_total[e]));
                        empSalary.setIncTransport(Double.parseDouble(inc_trans[e]));
                        empSalary.setAdditional(Double.parseDouble(inc_add[e]));
                        empSalary.setPercentageBasic(Double.parseDouble(pct_basic[e]));
                        empSalary.setPercentageTotal(Double.parseDouble(pct_total[e]));
                        empSalary.setPercentTransport(Double.parseDouble(pct_trans[e]));
                        PstEmpSalary.updateExc(empSalary);
                    }
                    catch(Exception exc){
                        msgString = msgString + "<div class=\"errfont\">\n\tCan't save data row "+(e+1)+"</div>";
                    }
                }
                else {
                    System.out.println("...insert");
                    try {
                        EmpSalary empSalary = new EmpSalary();
                        empSalary.setEmployeeId(Long.parseLong(""+employeeId[e]));
                        empSalary.setCurrDate(curr_date);
                        empSalary.setLos1(Integer.parseInt(los1[e]));
                        empSalary.setLos2(Integer.parseInt(los2[e]));
                        empSalary.setCurrBasic(Double.parseDouble(curr_basic[e]));
                        empSalary.setCurrTransport(Double.parseDouble(curr_trans[e]));
                        empSalary.setCurrTotal(Double.parseDouble(curr_total[e]));
                        empSalary.setNewBasic(Double.parseDouble(new_basic[e]));
                        empSalary.setNewTransport(Double.parseDouble(new_trans[e]));
                        empSalary.setNewTotal(Double.parseDouble(new_total[e]));
                        empSalary.setIncSalary(Double.parseDouble(inc_basic[e]));
                        empSalary.setIncTotal(Double.parseDouble(inc_total[e]));
                        empSalary.setIncTransport(Double.parseDouble(inc_trans[e]));
                        empSalary.setAdditional(Double.parseDouble(inc_add[e]));
                        empSalary.setPercentageBasic(Double.parseDouble(pct_basic[e]));
                        empSalary.setPercentageTotal(Double.parseDouble(pct_total[e]));
                        empSalary.setPercentTransport(Double.parseDouble(pct_trans[e]));
                        PstEmpSalary.insertExc(empSalary);
                    }
                    catch(Exception exc){
                        msgString = msgString + "<div class=\"errfont\">\n\tCan't save data row "+(e+1)+"</div>";
                    }
                }
            }
            if(msgString == null || msgString.length()<1)
                msgString = "<div class=\"msginfo\">Data have been saved</div>";
        }
    }
    double valueTotal = 0;
%>

<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Salary</title>
<script language="JavaScript">
    function cmdSave(){
        document.frmupload.command.value="<%=Command.SAVE%>";
        document.frmupload.action="up_salary_process_v2.jsp";
        document.frmupload.submit();
    }
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"> 
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
<SCRIPT language=JavaScript>
    function hideObjectForEmployee(){    
    } 

    function hideObjectForLockers(){ 
    }

    function hideObjectForCanteen(){
    }

    function hideObjectForClinic(){
    }

    function hideObjectForMasterdata(){
    }

    function MM_swapImgRestore() { //v3.0
      var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
    }

    function MM_preloadImages() { //v3.0
      var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
        var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
        if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
    }

    function MM_findObj(n, d) { //v4.0
      var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
        d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
      if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
      for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
      if(!x && document.getElementById) x=document.getElementById(n); return x;
    }

    function MM_swapImage() { //v3.0
      var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
       if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
    }
</SCRIPT>
<!-- #EndEditable -->
</head> 
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
 <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
  <tr> 
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
      <!-- #BeginEditable "header" --> 
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --> </td>
  </tr> 
  <tr> 
    <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" --> 
      <%@ include file = "../../main/mnmain.jsp" %>
      <!-- #EndEditable --> </td> 
  </tr>
  <tr> 
    <td  bgcolor="#9BC1FF" height="10" valign="middle"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr>
          <td align="left"><img src="<%=approot%>/images/harismaMenuLeft1.jpg" width="8" height="8"></td>
          <td align="center" background="<%=approot%>/images/harismaMenuLine1.jpg" width="100%"><img src="<%=approot%>/images/harismaMenuLine1.jpg" width="8" height="8"></td>
          <td align="right"><img src="<%=approot%>/images/harismaMenuRight1.jpg" width="8" height="8"></td>
        </tr>
      </table>
    </td> 
  </tr>
   <%}%>
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="3" cellpadding="2"> 
        <tr> 
          <td width="100%">
      <table width="100%" border="0" cellspacing="0" cellpadding="0"> 
        <tr> 
            <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" --> 
              Uploader > Employee Salary |  <a  href="<%=approot%>/payroll/process/pay-pre-data.jsp?periodId=<%=payPeriod.getOID() %>"><font color="#30009D">Back to Select</font></a> | <a  href="<%=approot%>/home.jsp"><font color="#30009D">Back to Menu</font></a><!-- #EndEditable --> </strong></font> 
            </td>
        </tr>
        <tr> 
          <td>&nbsp;Period : <%=payPeriod.getPeriod()%> : <%=payPeriod.getStartDate() %> ~  <%=payPeriod.getEndDate() %> 
              Pay Slip Date : <% if(payPeriod.getPaySlipDate()==null ){ out.println("PLEASE specify payroll date");} else{ out.println(payPeriod.getPaySlipDate());} %>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td  style="background-color:<%=bgColorContent%>; "> 
                  <table width="100%" border="0" cellspacing="1" cellpadding="1" >
                    <tr> 
                      <td valign="top"> 
                          <%
            String query = " hr_employee.RESIGNED=1 AND hr_employee.RESIGNED_DATE < '"+payPeriod.getStartDate()+"'";
            if (paygroupId.length()>0){
               query += " OR hr_employee.PAYROLL_GROUP NOT IN("+paygroupId+") " ;
            }
            Vector listEmpResign = PstEmployee.list(0, 0, query, "");
            
            Hashtable listEmpResignHash = PstEmployee.hlist(0, 0, query, "");
                          %>
                        <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                          <tr> 
                            <td valign="top"> <!-- #BeginEditable "content" --> 
                                <%  String sMessage = "";
        try {                                
                                    NumberFormat numberFormatter;
                                    numberFormatter = NumberFormat.getNumberInstance();

                                    TextLoader uploader = new TextLoader();
                                    FileOutputStream fOut = null;
                                    ByteArrayInputStream inStream = null;
                                    Vector v = new Vector();
                                    int numcol = 22;//12;
                                    StringBuffer drawList =  new StringBuffer();
                                    org.apache.poi.hssf.record.crypto.Biff8EncryptionKey.setCurrentUserPassword("matadihati");
                                    uploader.uploadText(config, request, response);
                                    Object obj = uploader.getTextFile("file");
                                    byte byteText[] = null;
                                    byteText = (byte[]) obj;
                                    inStream = new ByteArrayInputStream(byteText);
                                            Excel tp = new Excel();
                                
        Vector result = new Vector();
        
         boolean resignStatus = false;   
           int rTemp = 0; 
            
            POIFSFileSystem fs = new POIFSFileSystem(inStream);

            HSSFWorkbook wb = new HSSFWorkbook(fs);
            System.out.println("creating workbook");
            
            int numOfSheets = wb.getNumberOfSheets();
            //System.out.println("Number Of Sheets = " + numOfSheets);
            Vector<PaySlip> listOfPaySlip = new Vector();
			for (int i=0; i<numOfSheets && payPeriod.getPaySlipDate()!=null ; i++) {
                
                int r = 0;
                
                HSSFSheet sheet = (HSSFSheet)wb.getSheetAt(i);
                out.println("<br><strong> Sheet name : "+ sheet.getSheetName()+"<br>");
				String jsonProccess = "{ \"periode\" : \""+payPeriod.getPeriod()
							+ "\", \"data\" : [";
                if(payPeriod.getPeriod()==null || sheet==null || sheet.getSheetName()==null || sheet.getSheetName().length()<1 ||
                        ( !payPeriod.getPeriod().trim().equalsIgnoreCase(sheet.getSheetName().trim())) ) {
                    out.println(" NOT MATCH : Period name and sheet name" );
                    continue;
                }
                int rows = sheet.getPhysicalNumberOfRows();                
                
                Hashtable payCompInput = new Hashtable();
                boolean payCompDone = false;
                int col_payroll = -1;
                int row_payroll = -1;
                //System.out.println("\tgetNumberOfSheets = " + wb.getNumberOfSheets());
                //System.out.println("\tgetPhysicalNumberOfRows = " + sheet.getPhysicalNumberOfRows());
                                %> <table class="tablecolor" > <%
                // loop untuk row dimulai dari numHeaderRow (0, .. numHeaderRow diabaikan) => untuk yang bukan sheet pertaman
                int start = (i == 0) ? 0 : NUM_HEADER;                
                Company company= new Company();                
                Division division = new Division();                
                Department department = new Department();                 
                Section section =  new Section();      
				String[] cellName = new String[100];
                for (r=start; r < rows; r++) {
                    Employee employee = null;
                    PaySlip paySlip =  null;
                    try{ 
	                    HSSFRow row = sheet.getRow(r);
	                    int cells = 0;
                        //if number of cell is static
                        if(NUM_CELL > 0){
                        	cells = NUM_CELL;
                        }
                        //number of cell is dinamyc
                        else{
                            cells = row.getPhysicalNumberOfCells();
                        }

                        // ambil jumlah kolom yang sebenarnya
                        NUM_CELL = cells;
                        // Variable support SUM by Hendra Putu | 2015-08-03
                        boolean actSUM = false;
                        int incCell = 0;
                           //cells = row.getPhysicalNumberOfCells();
	                    %> <tr> <%
	                    //System.out.println("ROW " + row.getRowNum());
                            String cellColor="";
							
							if (r == 6){
								jsonProccess += "{";
							} else if (r>6){
								jsonProccess += ",{";
							}
	                    for (int c = 0; c <= cells; c++)
	                    {   
                                cellColor="#CCCCCC";
	                        HSSFCell cell  = row.getCell((short) c);
	                        String   value = null;
                                if(cell!=null){
	                        switch (cell.getCellType())
	                        {
	                            case HSSFCell.CELL_TYPE_FORMULA :
	                                //value = "FORMULA ";
	                                value = String.valueOf(cell.getCellFormula());;
	                                break;
	                            case HSSFCell.CELL_TYPE_NUMERIC :
	                                //value = "NUMERIC value=" + cell.getNumericCellValue();
	                                value = Formater.formatNumber(cell.getNumericCellValue(), "###");
                                        /* c == 1 adalah kolom payroll number*/
                                        if (c == 1){
                                            for(int x=0; x<listEmpResign.size(); x++){
                                                Employee empResign = (Employee)listEmpResign.get(x);
                                                if (value.endsWith(empResign.getEmployeeNum())){
                                                    value = "0";
                                                    rTemp = r;
                                                    resignStatus = true;
                                                    break;
                                                }
                                            }
                                        }
                                        
                                        
// String.valueOf(cell.getNumericCellValue());
	                                break;
	                            case HSSFCell.CELL_TYPE_STRING :
	                                //value = "STRING value=" + cell.getStringCellValue();
	                                value = String.valueOf(cell.getStringCellValue());                                        
                                        if(!payCompDone && value!=null && (r>=ROW_COMP_MIN && r <= ROW_COMP_MAX)){                                             
                                            PayComponent comp=PstPayComponent.getManualInputComponent(value); 
                                            if(comp!=null && comp.getCompName().length()>0){
                                               payCompInput.put(""+c, comp);   
                                               cellColor="##FF9900";
                                            }
                                         }
                                         if (listEmpResignHash != null && listEmpResignHash.get(""+value)!= null){
                                             
                                                    value = "0";
                                                    rTemp = r;
                                                    resignStatus = true;
                                                    break;
                                            
                                        }
	                                break;                                                                        
	                            default :
                                        value = String.valueOf(cell.getStringCellValue()!=null?cell.getStringCellValue():"");
                                        ;
	                        }   
							
							if (r==4){
								cellName[c] = value;
							}
							
							if (r>=6){
								if (c==0){
									jsonProccess += cellName[c]+":\""+value+"\"";
								} else {
									jsonProccess += ","+cellName[c]+":\""+value+"\"";
								}
							}
                                
                                try{ // search for the row containts the first employee data
                                if(col_payroll<0 && r>ROW_COMP_MAX && c>=COL_PAYROLL_MIN && c<= COL_PAYROLL_MAX && value!=null){
                                     if(value.endsWith(".0")){
                                         value=value.substring(0, value.length()-2);
                                     };
                                     employee = PstEmployee.getEmployeeByNum(value);
                                     if(employee!=null && employee.getEmployeeNum().length()>0){
                                         row_payroll = r;
                                         col_payroll = c;                                        
                                         cellColor="##FF9900";
                                     } else{
                                        String tempPayroll = value.substring(0, value.lastIndexOf(""));
                                        // String tempPayroll = value.substring(0, value.lastIndexOf("."));
                                         employee =PstEmployee.getEmployeeByNum(tempPayroll);
                                         if(employee!=null && employee.getEmployeeNum().length()>0){
                                             row_payroll = r;
                                             col_payroll = c;
                                             cellColor="##FF9900"; 
                                             value = tempPayroll;
                                         } 
                                     }
                                }
                                } catch(Exception exc){
                                    System.out.println("r="+r+" c="+c+" "+exc);
                                } 
                                
                                if(row_payroll>=0 && r >=row_payroll && employee==null){ // position for employee payroll data
                                try{ 
                                    if( c== col_payroll && value!=null){
                                         if(value.endsWith(".0")){
                                             value=value.substring(0, value.length()-2);
                                         };
                                        
                                         employee =PstEmployee.getEmployeeByNum(value);
                                         if(employee!=null && employee.getEmployeeNum().length()>0){
                                             row_payroll = r;
                                             col_payroll = c;                                        
                                             cellColor="##FF9900";
                                         } else{
                                             String tempPayroll="";
                                             if(value!=null && value.length()>0){ 
                                                tempPayroll = value.substring(0, value.lastIndexOf("."));
                                             }
                                            // String tempPayroll = value.substring(0, value.lastIndexOf("."));
                                             employee =PstEmployee.getEmployeeByNum(tempPayroll);
                                             if(employee!=null && employee.getEmployeeNum().length()>0){
                                                 row_payroll = r;
                                                 col_payroll = c;
                                                 cellColor="##FF9900"; 
                                                 value = tempPayroll;
                                             } 
                                         }
                                    }                                    
                                } catch(Exception exc){
                                    System.out.println("Exception"+exc);
                                }                                                                         
                                }
                                if(employee!=null && employee.getEmployeeNum().length()>0 && paySlip==null ){ 
                                    PayEmpLevel emLv = null;
                                    if(payPeriod!=null ){
                                      emLv = PstPayEmpLevel.getPayLevelByEmployeeOid(employee.getOID(),payPeriod.getStartDate(), 
                                              payPeriod.getEndDate(), PstPayEmpLevel.fieldNames[PstPayEmpLevel.FLD_LEVEL_CODE]);
                                    } else{
                                      emLv = PstPayEmpLevel.getActiveLevelByEmployeeOid(employee.getOID());
                                    }
                                      if(emLv!=null){           
                                          paySlip = new PaySlip(); 
                                          paySlip.setEmployeeId(employee.getOID());
                                          paySlip.setPeriodId(payPeriod.getOID());
                                          paySlip.setCommencDate(employee.getCommencingDate());
                                          paySlip.setPaySlipDate(payPeriod.getPaySlipDate());                                       
                                      } else{
                                          sMessage=sMessage+(" No Employee Level for "+ employee.getEmployeeNum()+ "/"+ employee.getFullName()+
                                                  " for period of " + Formater.formatDate(payPeriod.getStartDate(),"dd MMMM yyyy") +  
                                                  Formater.formatDate(payPeriod.getEndDate(),"dd MMMM yyyy")+"<br>"); 
                                      }
                                }
                                
                                 if( cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC && value!=null && employee!=null && employee.getEmployeeNum().length()>0){ 
                                     // employee found
                                     if(paySlip!=null && payCompInput.containsKey(""+c) ){
                                          cellColor="##FFAA00"; 
                                          PaySlipComp slipComp = new PaySlipComp();
                                          PayComponent payComp = (PayComponent) payCompInput.get(""+c); 
                                          slipComp.setCompCode(payComp.getCompCode());
                                          try{
                                           slipComp.setCompValue(Double.parseDouble(value));
                                          } catch(Exception exc){
                                              System.out.println("r="+r+" c="+c+" "+exc);                                              
                                          }    
                                          paySlip.addPaySlipComp(slipComp);
                                     }                                             
                                 }                                                                                                         
                               }
                                
	                        %> 
                                <!--<td bgcolor="<//%//=cellColor%>" ><//%//=(value!=null? value:"")%></td> </%
	                        //result.addElement(value);
	                    }//end of row -->
                                <!-- update by Hendra 2015-05-18 -->
                              <% if ((resignStatus != true) && (rTemp != r)){ %>
                              <% if(cell!=null && cell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC) { %> 
                                    <td bgcolor="<%=cellColor%>" ><%=(value!=null? value:"")%></td> <%
                                } else{ 
                                    try{  
                                        if(value!=null && value.length()>1 && value.endsWith(".0")){
                                             value=value.substring(0, value.length()-2);
                                         }else if(value!=null && value.length()>0) {
                                            value = Formater.formatNumber(Double.parseDouble(value), "#############################.#####"); 
                                         };
                                      %> <td bgcolor="<%=cellColor%>" ><%=(value!=null && value.length()>0?""+value:"")%></td> <%
                                         
                                    } catch(Exception exc1){
                                        System.out.println("Exception"+exc1);
                                    }
                                    if (incCell == 3){
                                        valueTotal = valueTotal + Double.valueOf(value);
                                    }
                                    
                                }
                                } else {
                                  %>
                                  <td bgcolor="<%=cellColor%>" >&nbsp;</td>
                                  <%
                                  resignStatus = false;
                              }
                              if (incCell == 3){
                                    incCell = 0;
                                } else {
                                    incCell++;
                                }
                         }
							if (r >= 6){
								jsonProccess += "}";
							}
                        if(!payCompDone && payCompInput.size()>0){
                            payCompDone=true;
                        } 
                            %> </tr> <%
                    } catch(Exception e){
	                    System.out.println("=> Can't get data ..sheet : "+i+", row : "+r+", => Exception e : "+e.toString());
	                }
                    if(paySlip!=null){ 
                        listOfPaySlip.add(paySlip);
                        try{
                           if(employee!=null){ 
                              if(employee.getCompanyId()!= company.getOID() && employee.getCompanyId()!=0 ){                                  
                                  PstCompany pstCompany = new PstCompany();
                                  company= pstCompany.fetchExc(employee.getCompanyId());                                   
                              } 
                              if(employee.getDivisionId()!= division.getOID() && employee.getDivisionId()!=0 ){                                  
                                  PstDivision pstDivision = new PstDivision();
                                  division = pstDivision.fetchExc(employee.getDivisionId());                                  
                              }
                              if(employee.getDepartmentId() != department.getOID() && employee.getDepartmentId()!=0 ){                                  
                                  PstDepartment pstDepartment= new PstDepartment();
                                  department = pstDepartment.fetchExc(employee.getDepartmentId());                                  
                              }                              
                              if(employee.getSectionId() != section.getOID() && employee.getSectionId()!=0 ){                                  
                                  PstSection pstSection= new PstSection();
                                  section = pstSection.fetchExc(employee.getSectionId());                                  
                              }                                                                                                                      
                              paySlip.setCompCode(company.getCompany());
                              paySlip.setDivision(division.getDivision());
                              paySlip.setDepartment(department.getDepartment());
                              paySlip.setSection(section.getSection());
                           } 
                           if ((resignStatus != true) && (rTemp != r)){
                           PstPaySlip.insertExcWithDetail(paySlip);
                           }
                        }catch(Exception exc){ 
                            System.out.println("Exception "+exc);  
                        }
                    }
                            %><tr><td> <%=sMessage%></td></tr>   <%
	            } //end of sheet
                    %> </table> <%
                    jsonProccess += "]}";
					
					LogSysHistory logHist = new LogSysHistory();
					logHist.setLogDocumentId(0);
					logHist.setLogUserId(userSession.getAppUser().getOID());
					logHist.setLogLoginName(userSession.getAppUser().getLoginId());
					logHist.setLogDocumentNumber("-");
					logHist.setLogDocumentType("");
					logHist.setLogUserAction("Upload Excel");
					logHist.setLogOpenUrl("");
					logHist.setLogUpdateDate(new Date());
					logHist.setLogApplication("Payroll");
					logHist.setLogDetail(jsonProccess);
					logHist.setLogStatus(1);
					logHist.setApproverId(userSession.getAppUser().getOID());
					logHist.setApproveDate(new Date());
					logHist.setApproverNote("");
					logHist.setLogModule("Payroll");
					
					try {
						PstLogSysHistory.insertExc(logHist);
					} catch (Exception exc){}        
            } //end of all sheets
            session.putValue("PAYSLIP_TO_UPLOAD", listOfPaySlip);
            
            //return result;
        }
        catch (Exception e) {
            System.out.println("---=== Error : ReadStream ===---\n" + e);
        }
                        %>        
                                
                                
                                  <br><br>
                                <!-- #EndEditable --> 
                            </td>
                          </tr>
                          <tr>
                              <td>SUM Value = <%=Formater.formatNumber(valueTotal, "###")%></td>
                          </tr>
                          <tr>
                              <td>SUM Value = <%=Formater.formatNumber(valueTotal, "###")%></td>
                          </tr>
                          <tr>
                              <td>&nbsp;</td>
                          </tr>
                          <tr>
                              <td>&nbsp;</td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                    
                  </table>
                </td>
              </tr>
            </table>
          </td> 
        </tr>
      </table>
    </td> 
        </tr>
      </table>
    </td> 
  </tr>
  <tr> 
    <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%>
            <tr>
                            <td valign="bottom">
                               <!-- untuk footer -->
                                <%@include file="../../footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <%@ include file = "../../main/footer.jsp" %>
      <!-- #EndEditable --> </td>
  </tr>
            <%}%>
  </tr>
</table>
</body>
<!-- #BeginEditable "script" -->
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
