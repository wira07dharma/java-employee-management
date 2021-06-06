<%@ page language = "java" %>

<!-- package java -->
<%@ page import = "java.util.*" %>

<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ include file = "../../main/javainit.jsp" %>

<%-- YANG INI BELUM DIEDIT --%>
<% int  appObjCode = 0;//AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_TRAINING, AppObjInfo.OBJ_TRAINING_HISTORY); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
  privAdd = true;
  privDelete = true;
  privUpdate = true;
  privPrint = true;
%>
<!-- Ari_20110909
    Menambah Warning Point { -->
<!-- Jsp Block -->
<%!

	public String drawList(Vector objectClass ,  long empWarningId)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
                ctrlist.setListStyle("tblStyle");
                ctrlist.setTitleStyle("title_tbl");
		ctrlist.setCellStyle("listgensell");
                ctrlist.setHeaderStyle("title_tbl");
                ctrlist.setCellSpacing("0");
		ctrlist.addHeader("NO.","","align='center'");
		ctrlist.addHeader("BREAK DATE","");
		ctrlist.addHeader("BREAK FACT","");
		ctrlist.addHeader("WARNING DATE","");
		ctrlist.addHeader("WARNING BY","");
                ctrlist.addHeader("VALID UNTIL","");
                ctrlist.addHeader("LEVEL","");
                ctrlist.addHeader("POINT","","align='center'");

		ctrlist.setLinkRow(1);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;
                int recordNo = 1;

		for (int i = 0; i < objectClass.size(); i++) {
			EmpWarning empWarning = (EmpWarning)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(empWarningId == empWarning.getOID())
				 index = i;

			Warning warning = new Warning();
			if(empWarning.getWarnLevelId() != -1){
				try{
					warning = PstWarning.fetchExc(empWarning.getWarnLevelId());
				}catch(Exception exc){
					warning = new Warning();
				}
			}

			rowx.add(String.valueOf(recordNo++));
                        rowx.add(Formater.formatDate(empWarning.getBreakDate(), "MMM d, yyyy"));
                        rowx.add(empWarning.getBreakFact());
                        rowx.add(Formater.formatDate(empWarning.getWarningDate(), "MMM d, yyyy"));
                        rowx.add(empWarning.getWarningBy());
                        rowx.add(Formater.formatDate(empWarning.getValidityDate(), "MMM d, yyyy"));
                        rowx.add(String.valueOf(warning.getWarnDesc()));
                        rowx.add(String.valueOf(warning.getWarnPoint()));
                        lstData.add(rowx);
			lstLinkData.add(String.valueOf(empWarning.getOID()));

		}

		return ctrlist.draw(index);
	}

%>


<%
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request,"prev_command");
    long oidEmployee = FRMQueryString.requestLong(request,"employee_oid");
    long oidWarning = FRMQueryString.requestLong(request, "warning_id");
    int start = FRMQueryString.requestInt(request, "start");

    /*String[] listTitles =
    {
        "NO",
        "BREAK DATE",
        "BREAK FACT",
        "WARNING DATE",
        "WARNING BY",
        "VALID UNTIL",
        "WARNING_LEVEL"
    };*/

    int recordToGet = 10;
    //String errMsg = "";
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = PstEmpWarning.fieldNames[PstEmpWarning.FLD_EMPLOYEE_ID] + "=" + oidEmployee;
    String orderClause = PstEmpWarning.fieldNames[PstEmpWarning.FLD_BREAK_DATE];

    //int defaultWarnLevel = 0;

    Employee employee = new Employee();
    Department department = new Department();
    Section section = new Section();

    // GET EMPLOYEE DATA TO DISPLAY
    try {
        employee = PstEmployee.fetchExc(oidEmployee);

        long oidDepartment = employee.getDepartmentId();
        department = PstDepartment.fetchExc(oidDepartment);

        long oidSection = employee.getSectionId();
        section = PstSection.fetchExc(oidSection);
    }
    catch(Exception e) {
        employee = new Employee();
        department = new Department();
        section = new Section();
    }

    CtrlEmpWarning ctrlWarning = new CtrlEmpWarning(request);
    ControlLine ctrLine = new ControlLine();
    Vector listWarning = new Vector(1,1);

    /* EXECUTE ACTION COMMAND */
    iErrCode = ctrlWarning.action(iCommand, oidWarning, request, emplx.getFullName(), appUserIdSess);
    //errMsg = ctrlWarning.getMessage();


    /*count list All EmpEducation*/



    msgString =  ctrlWarning.getMessage();

    EmpWarning empWarning = ctrlWarning.getEmpWarning();
    FrmEmpWarning frmEmpWarning = ctrlWarning.getForm();
    EmpWarning warning = ctrlWarning.getEmpWarning();

    int vectSize = PstEmpWarning.getCount(whereClause);

if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)&& (oidWarning == 0))
            start = PstEmpWarning.findLimitStart(empWarning.getOID(),recordToGet, whereClause, orderClause);

    /* CASE NAVIGATION COMMAND */
    if((iCommand == Command.FIRST || iCommand == Command.PREV )||
       (iCommand == Command.NEXT || iCommand == Command.LAST)){

        start = ctrlWarning.actionList(iCommand, start, vectSize, recordToGet);
    }


    /* GET WARNING DATA TO DISPLAY */
    listWarning = PstEmpWarning.list(start, recordToGet, whereClause, orderClause);


    // design vector that handle data to store in session
    Vector warningReport = new Vector();

    warningReport.add(warning);
    warningReport.add(employee);

    session.putValue("LETTER_OF_WARNING", warningReport);


%>
<!-- End of Jsp Block -->
<html>
<!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" -->
<title>HARISMA - Warning</title>
<script language="JavaScript">

    function cmdBackToList(){
        document.fredit.start.value="0";
        document.fredit.command.value="<%=Command.BACK%>";
        document.fredit.action="../warning/warning_list.jsp";
        document.fredit.submit();
    }

    function cmdBackEmp(oid){
	document.fredit.employee_oid.value=oid;
	document.fredit.command.value="<%=Command.EDIT%>";
	document.fredit.action="employee_edit.jsp";
	document.fredit.submit();
    }

    function cmdBack(){
	document.fredit.command.value="<%=Command.BACK%>";
	document.fredit.action="warning.jsp";
	document.fredit.submit();
    }

    function cmdAdd(){
        document.fredit.warning_id.value="0";
        document.fredit.command.value="<%=Command.ADD%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdEdit(oid){
        document.fredit.warning_id.value=oid;
        document.fredit.command.value="<%=Command.EDIT%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdSave(){
        document.fredit.warning_id.value="<%= oidWarning %>";
	document.fredit.command.value="<%=Command.SAVE%>";
	document.fredit.prev_command.value="<%=iCommand%>";    /* edit atau add */
	document.fredit.action="warning.jsp";
	document.fredit.submit();
    }

    function cmdCancel(){
        document.fredit.command.value="<%=Command.CANCEL%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdAsk(oid){
        document.fredit.command.value="<%=Command.ASK%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdDelete(oidWarning){
        document.fredit.command.value="<%=Command.DELETE%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdPrint(){
	var rptSource = "<%=printroot%>.report.employee.EmployeeWarningPdf";
	window.open(rptSource);
    }


    function cmdListFirst(){
        document.fredit.command.value="<%=Command.FIRST%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdListPrev(){
        document.fredit.command.value="<%=Command.PREV%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdListNext(){
        document.fredit.command.value="<%=Command.NEXT%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdListLast(){
        document.fredit.command.value="<%=Command.LAST%>";
        document.fredit.action="warning.jsp";
        document.fredit.submit();
    }

    function cmdListEmployee(){
        document.fredit.command.value="<%=iCommand%>";
        win = window.open("<%=approot%>/employee/warning/src_emp.jsp?formName=fredit&empPathId=<%= FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_EMPLOYEE_ID]%>", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");

    }

    function cmdBackEmployeeList() {
        document.fredit.action = "employee_list.jsp?command=<%=Command.FIRST%>";
        document.fredit.submit();
    }

    //-------------- script control line -------------------
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
<style type="text/css">
    .tblStyle {border-collapse: collapse;font-size: 11px;}
    .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
    .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
    .title_page {color:#0db2e1; font-weight: bold; font-size: 14px; background-color: #EEE; border-left: 1px solid #0099FF; padding: 12px 18px;}

    body {color:#373737;}
    #menu_utama {padding: 9px 14px; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
    #menu_title {color:#0099FF; font-size: 14px; font-weight: bold;}
    #menu_teks {color:#CCC;}
    #box_title {padding:9px; background-color: #D5D5D5; font-weight: bold; color:#575757; margin-bottom: 7px; }
    #btn {
      background: #3498db;
      border: 1px solid #0066CC;
      border-radius: 3px;
      font-family: Arial;
      color: #ffffff;
      font-size: 12px;
      padding: 3px 9px 3px 9px;
    }

    #btn:hover {
      background: #3cb0fd;
      border: 1px solid #3498db;
    }
    .breadcrumb {
        background-color: #EEE;
        color:#0099FF;
        padding: 7px 9px;
    }
    .navbar {
        font-family: sans-serif;
        font-size: 12px;
        background-color: #0084ff;
        padding: 7px 9px;
        color : #FFF;
        border-top:1px solid #0084ff;
        border-bottom: 1px solid #0084ff;
    }
    .navbar ul {
        list-style-type: none;
        margin: 0;
        padding: 0;
    }

    .navbar li {
        padding: 7px 9px;
        display: inline;
        cursor: pointer;
    }

    .navbar li:hover {
        background-color: #0b71d0;
        border-bottom: 1px solid #033a6d;
    }

    .active {
        background-color: #0b71d0;
        border-bottom: 1px solid #033a6d;
    }
    .title_part {color:#FF7E00; background-color: #F7F7F7; border-left: 1px solid #0099FF; padding: 9px 11px;}
</style>
        <style type="text/css">
            body {background-color: #EEE;}
            .header {
                
            }
            .content-main {
                background-color: #FFF;
                margin: 25px 23px 59px 23px;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .content-info {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
            }
            .content-title {
                padding: 21px;
                border-bottom: 1px solid #E5E5E5;
                margin-bottom: 5px;
                background-color: #EEE;
            }
            #title-large {
                color: #575757;
                font-size: 16px;
                font-weight: bold;
            }
            #title-small {
                color:#797979;
                font-size: 11px;
            }
            .content {
                padding: 21px;
            }
            .btn {
                background: #ebebeb;
                background-image: -webkit-linear-gradient(top, #ebebeb, #dddddd);
                background-image: -moz-linear-gradient(top, #ebebeb, #dddddd);
                background-image: -ms-linear-gradient(top, #ebebeb, #dddddd);
                background-image: -o-linear-gradient(top, #ebebeb, #dddddd);
                background-image: linear-gradient(to bottom, #ebebeb, #dddddd);
                -webkit-border-radius: 5;
                -moz-border-radius: 5;
                border-radius: 3px;
                font-family: Arial;
                color: #7a7a7a;
                font-size: 12px;
                padding: 5px 11px 5px 11px;
                border: solid #d9d9d9 1px;
                text-decoration: none;
            }

            .btn:hover {
                color: #474747;
                background: #ddd;
                background-image: -webkit-linear-gradient(top, #ddd, #CCC);
                background-image: -moz-linear-gradient(top, #ddd, #CCC);
                background-image: -ms-linear-gradient(top, #ddd, #CCC);
                background-image: -o-linear-gradient(top, #ddd, #CCC);
                background-image: linear-gradient(to bottom, #ddd, #CCC);
                text-decoration: none;
                border: 1px solid #C5C5C5;
            }
            
            .btn-small {
                padding: 3px; border: 1px solid #CCC; 
                background-color: #EEE; color: #777777; 
                font-size: 11px; cursor: pointer;
            }
            .btn-small:hover {border: 1px solid #999; background-color: #CCC; color: #FFF;}
            
            .tbl-main {border-collapse: collapse; font-size: 11px; background-color: #FFF; margin: 0px;}
            .tbl-main td {padding: 4px 7px; border: 1px solid #DDD; }
            #tbl-title {font-weight: bold; background-color: #F5F5F5; color: #575757;}
            
            .tbl-small {border-collapse: collapse; font-size: 11px; background-color: #FFF;}
            .tbl-small td {padding: 2px 3px; border: 1px solid #DDD; }
            
            #caption {padding: 7px 0px 2px 0px; font-size: 12px; font-weight: bold; color: #575757;}
            #div_input {}
            
            .form-style {
                font-size: 12px;
                color: #575757;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .form-title {
                padding: 11px 21px;
                margin-bottom: 2px;
                border-bottom: 1px solid #DDD;
                background-color: #EEE;
                border-top-left-radius: 5px;
                border-top-right-radius: 5px;
                font-weight: bold;
            }
            .form-content {
                padding: 21px;
            }
            .form-footer {
                border-top: 1px solid #DDD;
                padding: 11px 21px;
                margin-top: 2px;
                background-color: #EEE;
                border-bottom-left-radius: 5px;
                border-bottom-right-radius: 5px;
            }
            #confirm {
                padding: 18px 21px;
                background-color: #FF6666;
                color: #FFF;
                border: 1px solid #CF5353;
            }
            #btn-confirm {
                padding: 3px; border: 1px solid #CF5353; 
                background-color: #CF5353; color: #FFF; 
                font-size: 11px; cursor: pointer;
            }
            .footer-page {
                
            }
            
        </style>
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" -->
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" -->
<!-- #EndEditable -->
</head>
<body>
    <div class="header">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%> 
            <%@include file="../../styletemplate/template_header.jsp" %>
            <%} else {%>
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
            </table>
        </div>
        <div id="menu_utama">
            <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> <%=dictionaryD.getWord(I_Dictionary.WARNING) %></span>
        </div>
        
        <% if (oidEmployee != 0) {%>
        <div class="navbar">

            <ul style="margin-left: 97px">
              <li class=""> <a href="employee_edit.jsp?employee_oid=<%=oidEmployee%>&prev_command=<%=Command.EDIT%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PERSONAL_DATA)%></a> </li>
              <li class=""> <a href="familymember.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER) %></a> </li>
              <li class=""> <a href="emplanguage.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.COMPETENCIES) %></a> </li>
              <li class=""> <a href="empeducation.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EDUCATION) %></a> </li>
              <li class=""> <a href="experience.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EXPERIENCE) %></a></li>
              <li class=""> <a href="careerpath.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.CAREER_PATH) %></a> </li>
              <li class=""> <a href="training.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.TRAINING_ON_DATABANK)%></a> </li>
              <li class="active"> <%=dictionaryD.getWord(I_Dictionary.WARNING) %> </li>
              <li class=""> <a href="reprimand.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.REPRIMAND) %></a> </li>
              <li class=""> <a href="award.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.AWARD) %></a> </li>
              <li class=""> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PICTURE) %></a> </li>
              <li class=""> <a href="doc_relevant.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></a> </li>
            </ul>
         </div>
        <%}%>
        
        <div class="content-main">
            <form name="fredit" method="post" action="">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                <input type="hidden" name="<%= FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_EMPLOYEE_ID]%>" value="<%=oidEmployee%>">
                <input type="hidden" name="warning_id" value="<%=oidWarning%>">

                <div class="content-info">
                    <% if(oidEmployee != 0){
                        employee = new Employee();
                        try{
                                 employee = PstEmployee.fetchExc(oidEmployee);
                        }catch(Exception exc){
                                 employee = new Employee();
                        }
                    
                    %>
                        <table border="0" cellspacing="0" cellpadding="0" style="color: #575757">
                        <tr> 
                                <td valign="top" style="padding-right: 11px;"><strong>Payroll Number</strong></td>
                              <td valign="top"><%=employee.getEmployeeNum()%></td>
                        </tr>
                        <tr> 
                              <td valign="top" style="padding-right: 11px;"><strong>Name</strong></td>
                              <td valign="top"><%=employee.getFullName()%></td>
                        </tr>
                        <% department = new Department();
                            try{
                                         department = PstDepartment.fetchExc(employee.getDepartmentId());
                            }catch(Exception exc){
                                         department = new Department();
                            }
                         %>
                        <tr> 
                              <td valign="top" style="padding-right: 11px;"><strong><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></strong></td>
                              <td valign="top"><%=department.getDepartment()%></td>
                        </tr>
                        <tr> 
                                <td valign="top" style="padding-right: 11px;"><strong>Address</strong></td>
                              <td valign="top"><%=employee.getAddress()%></td>
                        </tr>
                        </table>
                    <% } %>
                </div>
                <div class="content-title">
                    <div id="title-large"><%=dictionaryD.getWord(I_Dictionary.WARNING) %></div>
                    <div id="title-small">Daftar <%=dictionaryD.getWord(I_Dictionary.WARNING) %> karyawan.</div>
                </div>
                <div class="content">
                    <div><%=drawList(listWarning, oidWarning)%></div>
                    <div><%
                        ctrLine.setLocationImg(approot+"/images");
                        ctrLine.initDefault();
                    %>
                    <%= ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet) %>
                    </div>
                    <div>
                        <table cellpadding="0" cellspacing="0" border="0" width="25%">
                        <tr>
                          <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                          <td width="10"><a href="javascript:cmdBackToList()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image101','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image101" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back to List"></a></td>
                          <td width="6"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                          <td class="command" nowrap width="180">
                              <div align="left"><a href="javascript:cmdBackToList()">Back to
                              Warning List</a></div>
                          </td>
                          <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                          <td width="10"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image102','','<%=approot%>/images/BtnSearchOn.jpg',1)"><img name="Image102" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Schedule"></a></td>
                          <td width="6"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                          <td class="command" nowrap width="180">
                              <div align="left"><a href="javascript:cmdAdd()">Add New
                              Warning</a></div>
                          </td>
                        </tr>
                      </table>
                    </div>
                </div>
                  <%-- DISPLAY MESSAGE --%>
                  <%--
                     if(iCommand==Command.ADD) {
                        int warnLevel = SessEmpWarning.chekcActiveWarning(new Date(), oidEmployee);

                        if(warnLevel != -1) {
                            defaultWarnLevel = warnLevel + 1;
                 --%>
                                <%-- if(warnLevel < PstEmpWarning.levelNames.length - 1) {%>
                                  <p class="warningmsg">Note: This band member is still in effective warning period (<%=PstEmpWarning.levelNames[warnLevel]%>) !</p>
                                <% } else {%>
                                  <p class="warningmsg">Note: This band member has received "Warning Level 3", use reprimand instead !</p>
                                <% } --%>
                            
                      <%-- } %>
                  <% } --%>

                  <%-- FOR EDITING STATE --%>
                  <%if(iCommand==Command.ADD || iCommand==Command.EDIT || iCommand==Command.ASK ||
                      (iCommand==Command.SAVE && iErrCode!=FRMMessage.NONE)){ %>
                  <table>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td colspan="2" align="left" nowrap valign="top"  ><b>WARNING RECORD EDITOR</b></td>
                  </tr>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%" align="left" nowrap valign="top"  >&nbsp;</td>
                    <td  width="88%"  valign="top" align="left" class="comment">*)
                      entry required</td>
                  </tr>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%" align="left" nowrap valign="top"  >Break Date</td>
                    <td  width="88%"  valign="top" align="left">
                      <%=ControlDate.drawDateWithStyle(FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_BREAK_DATE], warning.getBreakDate(), 2, -30, "formElemen", "")%>
                    </td>
                  </tr>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%" align="left" nowrap valign="top"  >Break Fact </td>
                    <td  width="88%"  valign="top" align="left">
                      <textarea cols="22" rows="5" name="<%= FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_BREAK_FACT] %>"><%= warning.getBreakFact() %></textarea> * <%=frmEmpWarning.getErrorMsg(FrmEmpWarning.FRM_FIELD_BREAK_FACT)%>
                  </tr>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%" align="left" nowrap valign="top"  >Warning Date</td>
                    <td  width="88%"  valign="top" align="left">
                      <%=ControlDate.drawDateWithStyle(FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_WARN_DATE], warning.getWarningDate(), 2, -30, "formElemen", "")%>
                    </td>
                  </tr>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%" align="left" nowrap valign="top"  >Warning Level ( Point )</td>
                    <td  width="88%"  valign="top" align="left">
                         <%    Vector warning_value = new Vector(1,1);
                                  Vector warning_key = new Vector(1,1);
                                  Vector listWarn = PstWarning.listAll();
                                  for(int i=0;i<listWarn.size();i++){
                                      Warning warn = (Warning) listWarn.get(i);
                                      warning_value.add(""+warn.getOID());
                                      warning_key.add(""+warn.getWarnDesc()+ "   ("+""+warn.getWarnPoint()+")");
                                  }
                              %>
                                  <% if((listWarn != null) && (listWarn.size() > 0)){%>
                                  <%= ControlCombo.draw(frmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_WARN_LEVEL_ID],"formElemen",null, ""+empWarning.getWarnLevelId(), warning_value, warning_key) %>
                                  <% }else {%>
                                  <font class="comment">No
                                  Warning available</font>
                                  <% }%>
                                  * <%= frmEmpWarning.getErrorMsg(FrmEmpWarning.FRM_FIELD_WARN_LEVEL_ID) %>
                    </td>
                    </td>
                  </tr>
                  <% Warning wrn = new Warning();
                                         try{
                                                      wrn = PstWarning.fetchExc(empWarning.getWarnLevelId());
                                         }catch(Exception exc){
                                                      wrn = new Warning();
                                         }
                                      %>

                  <!-- } -->
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%" align="left" nowrap valign="top">Warning By</td>
                    <td  width="88%"  valign="top" align="left">
                      <table width="30%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                          <td>
                              <input type="text" readonly size="33" name="<%=FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_WARN_BY]%>" value="<%=warning.getWarningBy()%>" class="formElemen" maxlength="50">
                          </td>
                          <td>
                          <td>
                              <a href="javascript:cmdListEmployee()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10x','','<%=approot%>/images/icon/folderopen.gif',1)"><img name="Image10x" border="0" src="<%=approot%>/images/icon/folder.gif" width="24" height="24" alt="Search Employee"></a>
                          </td>
                          <td>
                              <a href="javascript:cmdListEmployee()">Search Employee</a>
                          </td>
                      </table>
                    </td>
                  </tr>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%" align="left" nowrap valign="top"  >Valid Until</td>
                    <td  width="88%"  valign="top" align="left">
                      <%
                          if(iCommand == Command.ADD) {
                              Calendar cal = new GregorianCalendar();
                              cal.add(Calendar.MONTH, 1);

                              Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                              out.println(ControlDate.drawDateWithStyle(FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_VALID_DATE], date, 2, -30, "formElemen", ""));
                          }
                          else {
                              out.println(ControlDate.drawDateWithStyle(FrmEmpWarning.fieldNames[FrmEmpWarning.FRM_FIELD_VALID_DATE], warning.getValidityDate(), 2, -30, "formElemen", ""));
                          }
                      %>
                    </td>
                  </tr>
                  <tr>
                    <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                    <td width="11%"  valign="top" align="left"  >&nbsp;</td>
                    <td  width="88%"  valign="top" align="left">&nbsp;</td>
                  </tr>
                  <tr align="left">
                    <td colspan="4">
                      <%
                          ctrLine.setLocationImg(approot+"/images");
                          ctrLine.initDefault();
                          ctrLine.setTableWidth("90");
                          ctrLine.setCommandStyle("buttonlink");

                          String scomDel = "javascript:cmdDelete('"+oidWarning+"')";
                          String scomAsk = "javascript:cmdAsk('"+oidWarning+"')";
                          String scomEdit = "javascript:cmdEdit('"+oidWarning+"')";

                          ctrLine.setBackCaption("Back To List");
                          ctrLine.setDeleteCaption("Delete Warning Record");
                          ctrLine.setSaveCaption("Save Warning Record");
                          ctrLine.setConfirmDelCaption("Yes, Delete Warning Record");
                          ctrLine.setAddCaption("Add New Data");

                          if(privDelete) {
                              ctrLine.setConfirmDelCommand(scomDel);
                              ctrLine.setDeleteCommand(scomAsk);
                              ctrLine.setEditCommand(scomEdit);
                          }
                          else{
                              ctrLine.setConfirmDelCaption("");
                              ctrLine.setDeleteCaption("");
                              ctrLine.setEditCaption("");
                          }

                          if(!privAdd){
                              ctrLine.setAddCaption("");
                          }

                          if(!privAdd  && !privUpdate){
                              ctrLine.setSaveCaption("");
                          }

                      %>
                      <%= ctrLine.drawImage(iCommand, iErrCode,  msgString)%>

                  </td>
                </tr>
                    <% if((iCommand != Command.ASK) && (iCommand == Command.EDIT) && privPrint) { %>
                    <tr>
                       <td>
                           <a href="javascript:cmdPrint()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image110','','<%=approot%>/images/BtnNewOn.jpg',1)" id="aSearch">
                           <img name="Image110" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Print Warning Report"></a>
                       </td>

                       <td>
                          <a href="javascript:cmdPrint()">Print Report</a>
                       </td>
                    </tr>
                    <% } %>
                <% } %>
                  </table>
                <div class="content">
                    <a id="btn" href="javascript:cmdBackEmployeeList()">Back to Employee List</a>
                </div>
             </form>
        </div>
                                    
        <div class="footer-page">
            <table>
                <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%>
                <tr>
                    <td valign="bottom"><%@include file="../../footer.jsp" %></td>
                </tr>
                <%} else {%>
                <tr> 
                    <td colspan="2" height="20" ><%@ include file = "../../main/footer.jsp" %></td>
                </tr>
                <%}%>
            </table>
        </div>                          
</body>

</html>