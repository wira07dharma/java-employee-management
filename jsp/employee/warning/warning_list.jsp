
<%@ page language = "java" %>

<!-- package java -->
<%@ page import = "java.util.*" %>

<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>

<%@ include file = "../../main/javainit.jsp" %>

<%-- YANG INI BELUM DIEDIT --%>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_MARITAL); %>
<%@ include file = "../../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!
       
    public String drawList(Vector objectList, String[] listTitles) 
    {
	if(objectList!=null && objectList.size()>0) {
            
            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");

            ctrlist.addHeader(listTitles[0],"5%");   
            ctrlist.addHeader(listTitles[1],"8%");
            ctrlist.addHeader(listTitles[2],"20%"); 
            ctrlist.addHeader(listTitles[3],"12%");    
            ctrlist.addHeader(listTitles[4],"12%");
            ctrlist.addHeader(listTitles[5],"12%");
            ctrlist.addHeader(listTitles[6],"8%"); 
            ctrlist.addHeader(listTitles[7],"15%");    
            ctrlist.addHeader(listTitles[8],"12%"); 
            
            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdList('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();	
            
            int recordNo = 0;

            for(int i=0; i<objectList.size(); i++) {                
                Vector list = (Vector)objectList.get(i);

                Employee emp = (Employee)list.get(0);
                EmpWarning wrn = (EmpWarning)list.get(1);
                Department dep = (Department)list.get(2);
                Section sec = (Section)list.get(3);
                Position pos = (Position)list.get(4);
           
                Vector rowx = new Vector();
                rowx.add(String.valueOf(recordNo++));
                rowx.add(emp.getEmployeeNum());
                rowx.add(emp.getFullName());
                rowx.add(dep.getDepartment());
                rowx.add(sec.getSection());
                rowx.add(pos.getPosition());
                //rowx.add(String.valueOf(wrn.getBreakDate()));
                rowx.add(Formater.formatDate(wrn.getBreakDate(), "d-MMM-yyyy"));
                rowx.add(wrn.getBreakFact());
                rowx.add(wrn.getWarningBy());

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(wrn.getEmployeeId()));
            }		
							
            return ctrlist.draw();						  														
	}
	else {				
            return "<div class=\"msginfo\">&nbsp;&nbsp;No warning data found ...</div>";																				
	}
    }
    
%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start_list");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");

String[] listTitles =
{
    "NO",
    "PAYROLL",
    "EMPLOYEE",
    "DEPARTMENT",
    "SECTION",
    "POSITION",
    "DATE",
    "FACTS",
    "WARN BY"
};

String msgString = "";
int errCode = FRMMessage.NONE;

int recordToGet = 10;
String whereClause = "";
String orderClause = "";

Vector listWarning = new Vector();
int vectSize;

SrcEmpWarning srcWarning = new SrcEmpWarning();
FrmSrcEmpWarning frmSrcWarning = new FrmSrcEmpWarning(request, srcWarning);
CtrlEmpWarning ctrlWarning = new CtrlEmpWarning(request);


/* GET SEARCH PARAMETER */

// command list from search warning
if(iCommand == Command.LIST) {
    frmSrcWarning.requestEntityObject(srcWarning);
}
else if(iCommand == Command.BACK) {
    // back from warning detail
    try {
        srcWarning = (SrcEmpWarning)session.getValue("SRC_EMP_WARNING");
        
        if(srcWarning == null){
            srcWarning = new SrcEmpWarning();
        }
    }
    catch(Exception e) {
        srcWarning = new SrcEmpWarning();
    }
}
else {
    // command for navigation
    if((iCommand == Command.FIRST)||(iCommand == Command.PREV)||
       (iCommand == Command.NEXT)||(iCommand == Command.LAST)){

        try {  
            srcWarning = (SrcEmpWarning)session.getValue("SRC_EMP_WARNING"); 

            if (srcWarning == null) {
                srcWarning = new SrcEmpWarning();
            }
        }
        catch(Exception e) {
            srcWarning = new SrcEmpWarning();
        }
    }
}

session.putValue("SRC_EMP_WARNING", srcWarning);


/* GET NUMBER OF RECORDS AND STARTING INDEX */

if(iCommand == Command.SAVE && prevCommand == Command.ADD) {    // view all    
    vectSize = PstEmpWarning.getCount(whereClause);
    start = PstEmpWarning.findLimitStart(oidEmployee, recordToGet, whereClause, orderClause);
}
else {
    vectSize = SessEmpWarning.countEmployee(srcWarning);
    
    if((iCommand==Command.FIRST)||(iCommand==Command.PREV)||(iCommand==Command.NEXT)||
       (iCommand==Command.LAST)||(iCommand==Command.LIST)) {
    
        start = ctrlWarning.actionList(iCommand, start, vectSize, recordToGet);
    }
}


/* GET WARNING DATA LIST */

if(iCommand == Command.SAVE && prevCommand==Command.ADD) {  
    // view all
    listWarning = SessEmpWarning.getEmployeeWarning(new SrcEmpWarning(), start, recordToGet);
}
if (iCommand == Command.LIST) {
        listWarning = SessEmpWarning.getEmployeeWarning(srcWarning, start, recordToGet);
        Vector listRWarning = SessEmpWarning.getEmployeeWarning(srcWarning, 0, 100000);
    try {
            Vector listR = new Vector();
            // buat session dengan nama session list
            listR = (Vector) session.getValue("session_list");
            // menyimpan nilai session dengan method putValue("namaSession", nilai);
            session.putValue("session_list", listWarning);
            session.putValue("session_list_report", listRWarning);

        } catch (Exception e) {
        }
}
else {   
    listWarning = SessEmpWarning.getEmployeeWarning(srcWarning, start, recordToGet);
}

%>

<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Employee Warning List</title>
<script language="JavaScript">

	function cmdAdd(){
            document.frm_warning.command.value="<%=Command.ADD%>";
            document.frm_warning.prev_command.value="<%=Command.ADD%>";
            document.frm_warning.action="../databank/warning.jsp";
            document.frm_warning.submit();
	}

	function cmdEdit(oid){
            document.frm_warning.employee_oid.value=oid;
            document.frm_warning.command.value="<%=Command.EDIT%>";
            document.frm_warning.prev_command.value="<%=Command.EDIT%>";
            document.frm_warning.action="../databank/warning.jsp";
            document.frm_warning.submit();
	}

        /* list warning records based on employee's oid */
        function cmdList(oid){
            document.frm_warning.employee_oid.value=oid;
            document.frm_warning.command.value="<%=Command.LIST%>";
            document.frm_warning.action="../databank/warning.jsp";
            document.frm_warning.submit();
	}
        
	function cmdListFirst(){
            document.frm_warning.command.value="<%=Command.FIRST%>";
            document.frm_warning.action="warning_list.jsp";
            document.frm_warning.submit();
	}

	function cmdListPrev(){
            document.frm_warning.command.value="<%=Command.PREV%>";
            document.frm_warning.action="warning_list.jsp";
            document.frm_warning.submit();
	}

	function cmdListNext(){
            document.frm_warning.command.value="<%=Command.NEXT%>";
            document.frm_warning.action="warning_list.jsp";
            document.frm_warning.submit();
	}

	function cmdListLast(){
            document.frm_warning.command.value="<%=Command.LAST%>";
            document.frm_warning.action="warning_list.jsp";
            document.frm_warning.submit();
	}

	function cmdBack(){
            document.frm_warning.command.value="<%=Command.BACK%>";
            document.frm_warning.action="src_warning.jsp";
            document.frm_warning.submit();
	}
        
        function cmdPrintXLS(){
            window.open("<%=approot%>/WarningListXLS");
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
<!-- #EndEditable -->
</head> 

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnBackOn.jpg','<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
     <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
  <tr> 
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
      <!-- #BeginEditable "header" --> 
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --> 
    </td>
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
          <td height="20">
		    <font color="#FF6600" face="Arial"><strong>
			  <!-- #BeginEditable "contenttitle" --> 
                  Employee &gt; Warning Search Result<!-- #EndEditable --> 
            </strong></font>
	      </td>
        </tr>
        <tr> 
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td  style="background-color:<%=bgColorContent%>; "> 
                  <table width="100%" border="0" cellspacing="1" cellpadding="1" >
                    <tr> 
                      <td valign="top"> 
                        <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                          <tr> 
                            <td valign="top"> 
                                  <!-- #BeginEditable "content" --> 
                                    <form name="frm_warning" method="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">									  
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                                      <table border="0" width="100%">
                                        <tr> 
                                          <td height="8" width="100%" class="listtitle"><span class="listtitle">Employee Warning List</span> 
                                          </td>
                                        </tr>                                       
                                        <tr> 
                                          <td height="8" width="100%"><%=drawList(listWarning, listTitles)%></td>
                                        </tr>                                        
                                      </table>                                                                                                                                                                                              
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr> 
                                          <td> 
                                            <% 
                                                ControlLine ctrLine = new ControlLine();
                                                ctrLine.setLocationImg(approot+"/images");
						ctrLine.initDefault();						
                                            %>
                                            <%= ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet) %> 
                                          </td>
                                        </tr>
                                        <tr>
                                          <td nowrap align="left" class="command">&nbsp;</td>
                                        </tr>
                                        <tr> 
                                          <td nowrap align="left" class="command"> 
                                            <table border="0" cellspacing="0" cellpadding="0" align="left">
                                              <tr> 
                                                <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                                <td width="24"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image300','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image300" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back To List"></a></td>
                                                <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                                <td nowrap> <a href="javascript:cmdBack()" class="command">Back 
                                                  To Search Warning</a></td>                                                    
                                                  <td width="24"><a href="javascript:cmdPrintXLS()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1003" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Export to Excel"></a></td>
                                                <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                                <!-- mchen -->
                                                <td nowrap><a href="javascript:cmdPrintXLS()" class="command">Export to Excel</a></td>
                                               </tr>
                                            </table>
                                          </td>
                                        </tr>
                                      </table>
                                    </form>
                                    <!-- #EndEditable -->
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </td>
              </tr>
              <tr> 
                <td>&nbsp; </td>
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
</table>
</body>
<!-- #BeginEditable "script" -->
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
