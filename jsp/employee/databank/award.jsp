<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
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

<%@ include file = "../../main/javainit.jsp" %>

<%-- YANG INI BELUM DIEDIT --%>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK);%>
<%@ include file = "../../main/checkuser.jsp" %>
<!-- update by devin 2014-02-04 -->
<%
    /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privPrint = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_PRINT));
    long hrdDepOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
    boolean isHRDLogin = hrdDepOid == departmentOid ? true : false;
    long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
    boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
    boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;
%>
<!-- Jsp Block -->
<%!    public String drawList(Vector objectList, String[] listTitles) {
        if (objectList != null && objectList.size() > 0) {

            ControlList ctrlist = new ControlList();
            ctrlist.setAreaWidth("100%");
            ctrlist.setListStyle("tblStyle");
            ctrlist.setTitleStyle("title_tbl");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("title_tbl");
            ctrlist.setCellSpacing("0");

            ctrlist.addHeader(listTitles[0], "");
            ctrlist.addHeader(listTitles[1], "");
            ctrlist.addHeader("Title of award", "");
            ctrlist.addHeader(listTitles[2] + " (Internal)", "");
            ctrlist.addHeader(listTitles[3] + " (Internal)", "");
            ctrlist.addHeader("Award from (External)", "");
            ctrlist.addHeader(listTitles[4], "");
            ctrlist.addHeader(listTitles[5], "");

            ctrlist.setLinkRow(1);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData();
            Vector lstLinkData = ctrlist.getLinkData();
            ctrlist.setLinkPrefix("javascript:cmdEdit('");
            ctrlist.setLinkSufix("')");
            ctrlist.reset();

            int recordNo = 1;

            for (int i = 0; i < objectList.size(); i++) {
                EmpAward awd = (EmpAward) objectList.get(i);
                Department dept = new Department();
                Section sect = new Section();
                AwardType typ = new AwardType();
                String deptString = "-";
                String secString = "-";
                try {
                    dept = PstDepartment.fetchExc(awd.getDepartmentId());
                    deptString = dept.getDepartment();
                } catch (Exception e) {
                    dept = new Department();
                }

                try {
                    sect = PstSection.fetchExc(awd.getSectionId());
                    secString = sect.getSection();
                } catch (Exception e) {
                    sect = new Section();
                }

                try {
                    typ = PstAwardType.fetchExc(awd.getAwardType());
                } catch (Exception e) {
                    typ = new AwardType();
                }

                Vector rowx = new Vector();
                rowx.add(String.valueOf(recordNo));
                rowx.add(Formater.formatDate(awd.getAwardDate(), "d-MMM-yyyy"));
                rowx.add(awd.getTitle());
                rowx.add(deptString);
                rowx.add(secString);
                String provider = "-";
                try {
                    ContactList contList = PstContactList.fetchExc(awd.getProviderId());
                    provider = contList.getCompName();
                } catch (Exception e) {
                    System.out.println("provider" + e.toString());
                }
                rowx.add(provider);
                rowx.add(typ.getAwardType());
                rowx.add((awd.getAwardDescription().length() > 100) ? awd.getAwardDescription().substring(0, 100) + " ..." : awd.getAwardDescription());

                lstData.add(rowx);
                lstLinkData.add(String.valueOf(awd.getOID()));

                recordNo++;
            }

            return ctrlist.draw();
        } else {
            return "<div class=\"msginfo\">&nbsp;&nbsp;No award data found ...</div>";
        }
    }
%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
    long oidAward = FRMQueryString.requestLong(request, "award_id");
    long oidCompany = FRMQueryString.requestLong(request, "hidden_companyId");
    long oidDivision = FRMQueryString.requestLong(request, "hidden_divisionId");
    int rb = FRMQueryString.requestInt(request, "rb");

    int start = FRMQueryString.requestInt(request, "start");

    String[] listTitles
            = {
                "NO",
                "DATE",
                "DEPT. ON AWARD",
                "SECT. ON AWARD",
                "TYPE",
                "DESCRIPTION"
            };

    EmpAward award = new EmpAward();
    FrmEmpAward frmEmpAward = new FrmEmpAward(award);

    int recordToGet = 10;
    int iErrCode = FRMMessage.ERR_NONE;
    String errMsg = "";
    String whereClause = PstEmpAward.fieldNames[PstEmpAward.FLD_EMPLOYEE_ID] + "=" + oidEmployee;
    String orderClause = PstEmpAward.fieldNames[PstEmpAward.FLD_AWARD_DATE];

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
    } catch (Exception e) {
        employee = new Employee();
        department = new Department();
    }

    CtrlEmpAward ctrlAward = new CtrlEmpAward(request);
    ControlLine ctrLine = new ControlLine();
    Vector listAward = new Vector();

    /* EXECUTE ACTION COMMAND */
    iErrCode = ctrlAward.action(iCommand, oidAward);
    errMsg = ctrlAward.getMessage();

    frmEmpAward = ctrlAward.getForm();
    award = ctrlAward.getEmpAward();

    int vectSize = PstEmpAward.getCount(whereClause);

    /* CASE NAVIGATION COMMAND */
    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {

        start = ctrlAward.actionList(iCommand, start, vectSize, recordToGet);
    }

    if (iCommand == Command.GOTO) {
        frmEmpAward = new FrmEmpAward(request, award);
        frmEmpAward.requestEntityObject(award);
    }

    /* GET REWARD DATA TO DISPLAY */
    listAward = PstEmpAward.list(start, recordToGet, whereClause, orderClause);

    long oidSec = award.getSectionId();
    long oidDep = award.getDepartmentId();
%>
<!-- End of Jsp Block -->
<html>
    <!-- #BeginTemplate "/Templates/main.dwt" --> 
    <head>
        <!-- #BeginEditable "doctitle" --> 
        <title>HARISMA - Award</title>
        <script language="JavaScript">

    function cmdBackEmp(oid) {        
        document.fredit.<%= FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_EMPLOYEE_ID]%>.value = oid;        
                document.fredit.command.value = "<%=Command.EDIT%>";
                document.fredit.action = "employee_edit.jsp";
                document.fredit.submit();
            }
            //update by devin 2014-02-04
            function cmdUpdateDiv() {
                document.fredit.command.value = "<%=String.valueOf(Command.GOTO)%>";
                document.fredit.action = "award.jsp";
                document.fredit.target = "";
                document.fredit.submit();
            }
            function cmdUpdatePos() {
                document.fredit.command.value = "<%=String.valueOf(Command.GOTO)%>";
                document.fredit.action = "award.jsp";
                document.fredit.target = "";
                document.fredit.submit();
            }


            function cmdUpdateDep() {
        document.fredit.command.value = "<%=String.valueOf(Command.GOTO)%>";
                document.fredit.action = "award.jsp";
                document.fredit.target = "";
                document.fredit.submit();
            }

            function cmdUpdateSection() {
                document.fredit.prev_command.value = "<%=prevCommand%>";
                document.fredit.command.value = "<%=Command.GOTO%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdInternal() {
                document.getElementById("comp").disabled = false;
                document.getElementById("divi").disabled = false;
                document.getElementById("depart").disabled = false;
                document.getElementById("section").disabled = false;
                document.getElementById("awardfrom").disabled = true;
            }

            function cmdExternal() {
                document.getElementById("comp").disabled = true;
                document.getElementById("divi").disabled = true;
                document.getElementById("depart").disabled = true;
                document.getElementById("section").disabled = true;
                document.getElementById("awardfrom").disabled = false;
            }

    function cmdBack() {        
                document.fredit.command.value = "<%=Command.BACK%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdAdd() {
                document.fredit.award_id.value = "0";
                document.fredit.prev_command.value = "<%=Command.ADD%>";
                document.fredit.command.value = "<%=Command.ADD%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdEdit(oid) {
                document.fredit.award_id.value = oid;
                document.fredit.prev_command.value = "<%=Command.EDIT%>";
                document.fredit.command.value = "<%=Command.EDIT%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdSave() {
                document.fredit.award_id.value = "<%= oidAward%>";
                document.fredit.command.value = "<%=Command.SAVE%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdCancel() {
                document.fredit.command.value = "<%=Command.CANCEL%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdAsk(oid) {
                document.fredit.command.value = "<%=Command.ASK%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdDelete(oid) {
                document.fredit.command.value = "<%=Command.DELETE%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdListFirst() {
                document.fredit.command.value = "<%=Command.FIRST%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdListPrev() {
                document.fredit.command.value = "<%=Command.PREV%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdListNext() {
                document.fredit.command.value = "<%=Command.NEXT%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdListLast() {
                document.fredit.command.value = "<%=Command.LAST%>";
                document.fredit.action = "award.jsp";
                document.fredit.submit();
            }

            function cmdBackEmployeeList() {
                document.fredit.action = "employee_list.jsp?command=<%=Command.FIRST%>";
                document.fredit.submit();
            }

            //-------------- script control line -------------------
            function MM_swapImgRestore() { //v3.0
                var i, x, a = document.MM_sr;
                for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++)
                    x.src = x.oSrc;
            }

            function MM_preloadImages() { //v3.0
                var d = document;
                if (d.images) {
                    if (!d.MM_p)
                        d.MM_p = new Array();
                    var i, j = d.MM_p.length, a = MM_preloadImages.arguments;
                    for (i = 0; i < a.length; i++)
                        if (a[i].indexOf("#") != 0) {
                            d.MM_p[j] = new Image;
                            d.MM_p[j++].src = a[i];
                        }
                }
            }

            function MM_findObj(n, d) { //v4.0
                var p, i, x;
                if (!d)
                    d = document;
                if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
                    d = parent.frames[n.substring(p + 1)].document;
                    n = n.substring(0, p);
                }
                if (!(x = d[n]) && d.all)
                    x = d.all[n];
                for (i = 0; !x && i < d.forms.length; i++)
                    x = d.forms[i][n];
                for (i = 0; !x && d.layers && i < d.layers.length; i++)
                    x = MM_findObj(n, d.layers[i].document);
                if (!x && document.getElementById)
                    x = document.getElementById(n);
                return x;
            }

            function MM_swapImage() { //v3.0
                var i, j = 0, x, a = MM_swapImage.arguments;
                document.MM_sr = new Array;
                for (i = 0; i < (a.length - 2); i += 3)
                    if ((x = MM_findObj(a[i])) != null) {
                        document.MM_sr[j++] = x;
                        if (!x.oSrc)
                            x.oSrc = x.src;
                        x.src = a[i + 2];
                    }
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
                text-decoration: none;
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
            <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> <%=dictionaryD.getWord(I_Dictionary.AWARD)%> </span>
        </div>
        <% if (oidEmployee != 0) {%>
        <div class="navbar">
            <ul style="margin-left: 97px">
                <li class=""> <a href="employee_edit.jsp?employee_oid=<%=oidEmployee%>&prev_command=<%=Command.EDIT%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PERSONAL_DATA)%></a> </li>
                <li class=""> <a href="familymember.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER)%></a> </li>
                <li class=""> <a href="emplanguage.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.COMPETENCIES)%></a> </li>
                <li class=""> <a href="empeducation.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EDUCATION)%></a> </li>
                <li class=""> <a href="experience.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EXPERIENCE)%></a></li>
                <li class=""> <a href="careerpath.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.CAREER_PATH)%></a> </li>
                <li class=""> <a href="training.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.TRAINING_ON_DATABANK)%></a> </li>
                <li class=""> <a href="warning.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.WARNING)%></a> </li>
                <li class=""> <a href="reprimand.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.REPRIMAND)%></a> </li>
                <li class="active"> <a href="award.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.AWARD)%></a> </li>
                <li class=""> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PICTURE)%></a> </li>
                <li class=""> <a href="doc_relevant.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS)%></a> </li>
            </ul>
        </div>
        <%}%>
        <div class="content-main">
            <form name="fredit" method="post" action="">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                <input type="hidden" name="<%= FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_EMPLOYEE_ID]%>" value="<%=oidEmployee%>">
                <input type="hidden" name="award_id" value="<%=oidAward%>">

                <div class="content-info">

                    <% if (oidEmployee != 0) {
                            employee = new Employee();
                            try {
                                employee = PstEmployee.fetchExc(oidEmployee);
                            } catch (Exception exc) {
                                employee = new Employee();
                            }
                        }
                    %>
                    <table>
                        <tr>

                            <%-- EMPLOYEE MAIN DATA --%>
                            <td width="1%" nowrap>&nbsp;</td>
                            <td width="18%" nowrap>
                                <div align="left">Employee
                                    Number</div>
                            </td>
                            <td width="4%">:</td>
                            <td width="46%" nowrap> <%= employee.getEmployeeNum()%> </td>
                        </tr>
                        <tr>
                            <td width="1%" nowrap>&nbsp;</td>
                            <td width="18%" nowrap>
                                <div align="left">Employee Name</div>
                            </td>
                            <td width="4%">:</td>
                            <td width="46%" nowrap> <%= employee.getFullName()%> </td>
                        </tr>
                        <% department = new Department();
                            try {
                                department = PstDepartment.fetchExc(employee.getDepartmentId());
                            } catch (Exception exc) {
                                department = new Department();
                            }
                        %>
                        <tr>
                            <td width="1%" nowrap>&nbsp;</td>
                            <td width="18%" nowrap>
                                <div align="left"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT)%></div>
                            </td>
                            <td width="4%">:</td>
                            <td width="46%" nowrap>  <%=department.getDepartment()%> </td>
                            <td width="13%" nowrap>
                                <div align="left"></div>
                            </td>
                            <td width="18%">&nbsp; </td>
                        </tr>
                        <% section = new Section();
                            try {
                                section = PstSection.fetchExc(employee.getSectionId());
                            } catch (Exception exc) {
                                section = new Section();
                            }
                        %>
                        <tr>
                            <td width="1%" nowrap>&nbsp;</td>
                            <td width="18%"><%=dictionaryD.getWord(I_Dictionary.SECTION)%></td>
                            <td width="4%">:</td>
                            <td width="46%"><%=section.getSection() != null && section.getSection().length() > 0 ? section.getSection() : "-"%></td>
                        </tr>                                          
                    </table>
                </div>
                <div class="content-title">
                    <div id="title-large">Award History</div>
                    <div id="title-small">Daftar penghargaan yang telah diraih.</div>
                </div>
                <div class="content">
                    <table>

                        <tr> 
                            <td colspan="3" valign="top"  > 

                                <%-- EMPLOYEE WARNING TABLE DATA --%>                                         
                                <%=drawList(listAward, listTitles)%>
                            </td>
                        </tr>
                        <tr>

                            <%-- DRAW RECORDS INFORMATION --%>
                            <td colspan="3" valign="top"  >
                                <%
                                    ctrLine.setLocationImg(approot + "/images");
                                    ctrLine.initDefault();
                                %>
                                <%= ctrLine.drawImageListLimit(iCommand, vectSize, start, recordToGet)%>
                            </td>
                        </tr>
                        <tr> 
                            <td colspan="3" align="left" nowrap valign="top"  > 										 
                                <table cellpadding="0" cellspacing="0" border="0">
                                    <tr>                                                 
                                        <td><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                        <td><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image102', '', '<%=approot%>/images/BtnSearchOn.jpg', 1)"><img name="Image102" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Schedule"></a></td>
                                        <td><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                        <td class="command" nowrap width="180"> 
                                            <div align="left"><a href="javascript:cmdAdd()">Add New 
                                                    Award</a></div>
                                        </td>                                               
                                    </tr>
                                </table>				
                            </td>
                        </tr>
                        <tr> 
                            <td width="1%"  valign="top" align="left"  >&nbsp;</td>
                            <td width="11%" align="left" nowrap valign="top"  >&nbsp;</td>
                            <td  width="88%"  valign="top" align="left" class="comment">&nbsp;</td>
                        </tr>
                    </table>

                    <%-- FOR EDITING STATE --%>
                    <%if (iCommand == Command.ADD || iCommand == Command.EDIT || iCommand == Command.ASK
                                                    || iCommand == Command.GOTO || (iCommand == Command.SAVE && iErrCode != FRMMessage.NONE)) {%>
                    <table style="color:#373737;">
                        <tr>
                            <td valign="middle"><strong>Title of award</strong></td>
                            <td valign="middle"><input type="text" name="<%=FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_TITLE]%>" value="<%= award.getTitle()%>" size="37" /></td>
                        </tr>
                        <tr>
                            <td valign="middle"><strong>Date</strong></td>
                            <td valign="middle"><%=ControlDate.drawDateWithStyle(FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_AWARD_DATE], award.getAwardDate(), 2, -30, "formElemen", "")%> </td>
                        </tr>
                        <tr>
                            <td valign="middle"><strong>Award Type</strong></td>
                            <td valign="middle">
                                <%
                                    Vector typ_value = new Vector(1, 1);
                                    Vector typ_key = new Vector(1, 1);
                                    typ_value.add("0");
                                    typ_key.add("select...");

                                    Vector lisTyp = PstAwardType.list(0, 0, "", PstAwardType.fieldNames[PstAwardType.FLD_AWARD_TYPE]);

                                    for (int i = 0; i < lisTyp.size(); i++) {
                                        AwardType typ = (AwardType) lisTyp.get(i);
                                        typ_key.add(typ.getAwardType());
                                        typ_value.add(String.valueOf(typ.getOID()));
                                    }
                                %> 
                                <%= ControlCombo.draw(FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_AWARD_TYPE], "formElemen", null, "" + award.getAwardType(), typ_value, typ_key)%>
                                * <%= frmEmpAward.getErrorMsg(FrmEmpAward.FRM_FIELD_AWARD_TYPE)%>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <table>
                                    <tr>
                                        <td><input type="radio" name="rb" value="0" onclick="cmdInternal()" /> <strong>Internal</strong></td>
                                        <td><input type="radio" name="rb" value="1" onclick="cmdExternal()" /> <strong>External</strong></td>
                                    </tr>
                                    <tr>
                                        <%
                                            Vector comp_value = new Vector(1, 1);
                                            Vector comp_key = new Vector(1, 1);
                                            String whereComp = "";
                                            /*if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                                             whereComp = PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] +"="+srcOvertime.getCompanyId();
                                             }*/
                                            Vector div_value = new Vector(1, 1);
                                            Vector div_key = new Vector(1, 1);

                                            Vector dept_value = new Vector(1, 1);
                                            Vector dept_key = new Vector(1, 1);
                                            if (processDependOnUserDept) {
                                                if (emplx.getOID() > 0) {
                                                    if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                        //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                        comp_value.add("0");
                                                        comp_key.add("select ...");

                                                        div_value.add("0");
                                                        div_key.add("select ...");

                                                        dept_value.add("0");
                                                        dept_key.add("select ...");
                                                    } else {
                                                        Position position = null;
                                                        try {
                                                            position = PstPosition.fetchExc(emplx.getPositionId());
                                                        } catch (Exception exc) {
                                                        }
                                                        if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                                                            String whereDiv = " d." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + emplx.getDivisionId() + "";
                                                            //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereDiv, true);
                                                            // comp_value.add("0");
                                                            // comp_key.add("select ...");

                                                               //div_value.add("0");
                                                            //div_key.add("select ...");
                                                            dept_value.add("0");
                                                            dept_key.add("select ...");

                                                            whereComp = whereComp != null && whereComp.length() > 0 ? whereComp + " AND (" + whereDiv + ")" : whereDiv;

                                                        } else {

                                                            String whereClsDep = "(" + PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + departmentOid
                                                                    + ") OR (" + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + " = " + departmentOid + ") ";
                                                            try {
                                                                String joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT");
                                                                Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);

                                                                int grpIdx = -1;
                                                                int maxGrp = depGroup == null ? 0 : depGroup.size();
                                                                int countIdx = 0;
                                                                int MAX_LOOP = 10;
                                                                int curr_loop = 0;
                                                                do { // find group department belonging to curretn user base in departmentOid
                                                                    curr_loop++;
                                                                    String[] grp = (String[]) depGroup.get(countIdx);
                                                                    for (int g = 0; g < grp.length; g++) {
                                                                        String comp = grp[g];
                                                                        if (comp.trim().compareToIgnoreCase("" + departmentOid) == 0) {
                                                                            grpIdx = countIdx;   // A ha .. found here 
                                                                        }
                                                                    }
                                                                    countIdx++;
                                                                } while ((grpIdx < 0) && (countIdx < maxGrp) && (curr_loop < MAX_LOOP)); // if found then exit

                                                                // compose where clause
                                                                if (grpIdx >= 0) {
                                                                    String[] grp = (String[]) depGroup.get(grpIdx);
                                                                    for (int g = 0; g < grp.length; g++) {
                                                                        String comp = grp[g];
                                                                        whereClsDep = whereClsDep + " OR (DEPARTMENT_ID = " + comp + ")";
                                                                    }
                                                                }
                                                            } catch (Exception exc) {
                                                                System.out.println(" Parsing Join Dept" + exc);

                                                            }
                                                            //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, whereClsDep, false);

                                                            whereComp = whereComp != null && whereComp.length() > 0 ? whereComp + " AND (" + whereClsDep + ")" : whereClsDep;

                                                        }
                                                    }
                                                }
                                            } else {
                                                comp_value.add("0");
                                                comp_key.add("select ...");

                                                div_value.add("0");
                                                div_key.add("select ...");

                                                dept_value.add("0");
                                                dept_key.add("select ...");
                                            }
                                            Vector listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
                                            String prevCompany = "";
                                            String prevDivision = "";

                                            long prevCompanyTmp = 0;
                                            for (int i = 0; i < listCostDept.size(); i++) {
                                                Department dept = (Department) listCostDept.get(i);
                                                if (prevCompany.equals(dept.getCompany())) {
                                                    if (prevDivision.equals(dept.getDivision())) {
                                                        //if(srcOvertime!=null && srcOvertime.getCompanyId()!=0){
                                                        dept_key.add(dept.getDepartment());
                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                        //}
                                                    } else {
                                                        div_key.add(dept.getDivision());
                                                        div_value.add("" + dept.getDivisionId());
                                                        if (dept_key != null && dept_key.size() == 0) {
                                                            dept_key.add(dept.getDepartment());
                                                            dept_value.add(String.valueOf(dept.getOID()));
                                                        }
                                                        prevDivision = dept.getDivision();
                                                    }
                                                } else {
                                                    String chkAdaDiv = "";
                                                    if (div_key != null && div_key.size() > 0) {
                                                        chkAdaDiv = (String) div_key.get(0);
                                                    }
                                                    if ((div_key != null && div_key.size() == 0) || (chkAdaDiv.equalsIgnoreCase("select ..."))) {
                                                        if (prevCompanyTmp != dept.getCompanyId()) {
                                                            comp_key.add(dept.getCompany());
                                                            comp_value.add("" + dept.getCompanyId());
                                                            prevCompanyTmp = dept.getCompanyId();
                                                        }
                                                     //untuk karyawan admin yg hanya bisa akses departement tertentu (ketika di awal)
                                                        ////update
                                                        if (processDependOnUserDept) {
                                                            if (emplx.getOID() > 0) {
                                                                if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                                    if (oidCompany != 0) {
                                                                        div_key.add(dept.getDivision());
                                                                        div_value.add("" + dept.getDivisionId());

                                                                        dept_key.add(dept.getDepartment());
                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                        prevCompany = dept.getCompany();
                                                                        prevDivision = dept.getDivision();
                                                                    }
                                                                } else {
                                                                    Position position = null;
                                                                    try {
                                                                        position = PstPosition.fetchExc(emplx.getPositionId());
                                                                    } catch (Exception exc) {
                                                                    }
                                                                    if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                                                                        if (oidCompany != 0) {
                                                                            div_key.add(dept.getDivision());
                                                                            div_value.add("" + dept.getDivisionId());

                                                                            dept_key.add(dept.getDepartment());
                                                                            dept_value.add(String.valueOf(dept.getOID()));
                                                                            prevCompany = dept.getCompany();
                                                                            prevDivision = dept.getDivision();
                                                                        } //update by satrya 2013-09-19
                                                                        else if ((div_key != null && div_key.size() == 0) || (chkAdaDiv.equalsIgnoreCase("select ..."))) {
                                                                            div_key.add(dept.getDivision());
                                                                            div_value.add("" + dept.getDivisionId());

                                                                            //update by satrya 2013-09-19
                                                                            dept_key.add(dept.getDepartment());
                                                                            dept_value.add(String.valueOf(dept.getOID()));

                                                                            prevCompany = dept.getCompany();
                                                                            prevDivision = dept.getDivision();
                                                                        }

                                                                    } else {

                                                                        div_key.add(dept.getDivision());
                                                                        div_value.add("" + dept.getDivisionId());

                                                                        dept_key.add(dept.getDepartment());
                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                        prevCompany = dept.getCompany();
                                                                        prevDivision = dept.getDivision();
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if (oidCompany != 0) {
                                                                div_key.add(dept.getDivision());
                                                                div_value.add("" + dept.getDivisionId());

                                                                dept_key.add(dept.getDepartment());
                                                                dept_value.add(String.valueOf(dept.getOID()));
                                                                prevCompany = dept.getCompany();
                                                                prevDivision = dept.getDivision();
                                                            }
                                                        }

                                                    } else {
                                                        if (prevCompanyTmp != dept.getCompanyId()) {
                                                            comp_key.add(dept.getCompany());
                                                            comp_value.add("" + dept.getCompanyId());
                                                            prevCompanyTmp = dept.getCompanyId();
                                                        }

                                                    }

                                                }
                                            }
                                        %>
                                        <%
                                                                           //update by satrya 2013-08-13
                                            //jika user memilih select kembali
                                            if (oidCompany == 0) {
                                                oidDivision = 0;
                                            }

                                            if (oidCompany != 0) {
                                                whereComp = "(" + (whereComp != null && whereComp.length() == 0 ? "1=1" : whereComp) + ") AND " + PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] + "=" + oidCompany;
                                                listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
                                                prevCompany = "";
                                                prevDivision = "";

                                                div_value = new Vector(1, 1);
                                                div_key = new Vector(1, 1);

                                                dept_value = new Vector(1, 1);
                                                dept_key = new Vector(1, 1);

                                                prevCompanyTmp = 0;
                                                long tmpFirstDiv = 0;

                                                if (processDependOnUserDept) {
                                                    if (emplx.getOID() > 0) {
                                                        if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                            //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                            comp_value.add("0");
                                                            comp_key.add("select ...");

                                                            div_value.add("0");
                                                            div_key.add("select ...");

                                                            dept_value.add("0");
                                                            dept_key.add("select ...");
                                                        } else {
                                                            Position position = null;
                                                            try {
                                                                position = PstPosition.fetchExc(emplx.getPositionId());
                                                            } catch (Exception exc) {
                                                            }
                                                            if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                                                               //div_value.add("0");
                                                                //div_key.add("select ...");

                                                                dept_value.add("0");
                                                                dept_key.add("select ...");

                                                            }
                                                        }
                                                    }
                                                } else {
                                                    comp_value.add("0");
                                                    comp_key.add("select ...");

                                                    div_value.add("0");
                                                    div_key.add("select ...");

                                                    dept_value.add("0");
                                                    dept_key.add("select ...");
                                                }
                                                long prevDivTmp = 0;
                                                for (int i = 0; i < listCostDept.size(); i++) {
                                                    Department dept = (Department) listCostDept.get(i);
                                                    if (prevCompany.equals(dept.getCompany())) {
                                                        if (prevDivision.equals(dept.getDivision())) {
                                                            //update
                                                            if (oidDivision != 0) {
                                                                dept_key.add(dept.getDepartment());
                                                                dept_value.add(String.valueOf(dept.getOID()));
                                                            }
                                                        //lama
                                                        /*
                                                             dept_key.add(dept.getDepartment());
                                                             dept_value.add(String.valueOf(dept.getOID()));
                                                             */

                                                        } else {
                                                            div_key.add(dept.getDivision());
                                                            div_value.add("" + dept.getDivisionId());
                                                            if (dept_key != null && dept_key.size() == 0) {
                                                                dept_key.add(dept.getDepartment());
                                                                dept_value.add(String.valueOf(dept.getOID()));
                                                            }
                                                            prevDivision = dept.getDivision();
                                                        }
                                                    } else {
                                                        String chkAdaDiv = "";
                                                        if (div_key != null && div_key.size() > 0) {
                                                            chkAdaDiv = (String) div_key.get(0);
                                                        }
                                                        if ((div_key != null && div_key.size() == 0) || (chkAdaDiv.equalsIgnoreCase("select ..."))) {
                                                     //comp_key.add(dept.getCompany());
                                                            //comp_value.add(""+dept.getCompanyId());

                                                            if (prevDivTmp != dept.getDivisionId()) {
                                                                div_key.add(dept.getDivision());
                                                                div_value.add("" + dept.getDivisionId());
                                                                prevDivTmp = dept.getDivisionId();
                                                            }

                                                            tmpFirstDiv = dept.getDivisionId();

                                                           // dept_key.add(dept.getDepartment());
                                                            //   dept_value.add(String.valueOf(dept.getOID()));           
                                                            prevCompany = dept.getCompany();
                                                            prevDivision = dept.getDivision();
                                                        }
                                                        /*else{
                                                         if(prevCompanyTmp!=dept.getCompanyId()){
                                                         comp_key.add(dept.getCompany());
                                                         comp_value.add(""+dept.getCompanyId());
                                                         prevCompanyTmp=dept.getCompanyId();
                                                         }
              
                                                         }*/
                                                        String chkAdaDpt = "";
                                                        if (whereComp != null && whereComp.length() > 0) {
                                                            chkAdaDpt = "(" + (whereComp != null && whereComp.length() == 0 ? "1=1" : whereComp) + ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "=" + oidDivision;
                                                        }
                                                        Vector listCheckAdaDept = PstDepartment.listWithCompanyDiv(0, 0, chkAdaDpt);
                                                        if ((listCheckAdaDept == null || listCheckAdaDept.size() == 0)) {

                                                            if (processDependOnUserDept) {
                                                                if (emplx.getOID() > 0) {
                                                                    if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {

                                                                    } else {
                                                                        Position position = null;
                                                                        try {
                                                                            position = PstPosition.fetchExc(emplx.getPositionId());
                                                                        } catch (Exception exc) {
                                                                        }

                                                                        oidDivision = tmpFirstDiv;

                                                                    }
                                                                }
                                                            } else {
                                                                oidDivision = tmpFirstDiv;

                                                            }

                                                        }
                                                    }
                                                }
                                            }
                                        %>


                                        <td valign="top" style="background-color: #EEE; padding: 5px;">
                                            <div>Company</div>
                                            <div><%= ControlCombo.draw("hidden_companyId", "formElemen", null, "" + oidCompany, comp_value, comp_key, "id=\"comp\"  onChange=\"javascript:cmdUpdateDiv()\"")%></div>
                                            <div>Satuan Kerja</div>
                                            <div><%= ControlCombo.draw("hidden_divisionId", "formElemen", null, "" + oidDivision, div_value, div_key, "id=\"divi\" onChange=\"javascript:cmdUpdateDep()\"")%></div>
                                            <div>Department on award</div>
                                            <div>

                                                <%

            //update by satrya 2013-08-13
                                                    //jika user memilih select kembali
                                                    if (oidDep == 0) {

                                                        oidSec = 0;
                                                    }
                                                    if (oidDivision != 0) {
                                                        if (whereComp != null && whereComp.length() > 0) {
                                                            whereComp = "(" + whereComp + ") AND d." + PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "=" + oidDivision;
                                                        }

                                                        listCostDept = PstDepartment.listWithCompanyDiv(0, 0, whereComp);
                                                        prevCompany = "";
                                                        prevDivision = "";

                                                        div_value = new Vector(1, 1);
                                                        div_key = new Vector(1, 1);

                                                        dept_value = new Vector(1, 1);
                                                        dept_key = new Vector(1, 1);

                                                        prevCompanyTmp = 0;

                                                        if (processDependOnUserDept) {
                                                            if (emplx.getOID() > 0) {
                                                                if (isHRDLogin || isEdpLogin || isGeneralManager || isDirector) {
                                                                    //keyList = PstDepartment.genDepIDandNameWithCompanyDiv(0, 1000, "", true);
                                                                    comp_value.add("0");
                                                                    comp_key.add("select ...");

                                                                    div_value.add("0");
                                                                    div_key.add("select ...");

                                                                    dept_value.add("0");
                                                                    dept_key.add("select ...");
                                                                } else {
                                                                    Position position = null;
                                                                    try {
                                                                        position = PstPosition.fetchExc(emplx.getPositionId());
                                                                    } catch (Exception exc) {
                                                                    }
                                                                    if (position != null & position.getDisabedAppDivisionScope() == 0 & position.getPositionLevel() >= PstPosition.LEVEL_MANAGER) {
                       //div_value.add("0");
                                                                        //div_key.add("select ...");

                                                                        dept_value.add("0");
                                                                        dept_key.add("select ...");

                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            comp_value.add("0");
                                                            comp_key.add("select ...");

                                                            div_value.add("0");
                                                            div_key.add("select ...");

                                                            dept_value.add("0");
                                                            dept_key.add("select ...");
                                                        }

                                                        for (int i = 0; i < listCostDept.size(); i++) {
                                                            Department dept = (Department) listCostDept.get(i);
                                                            if (prevCompany.equals(dept.getCompany())) {
                                                                if (prevDivision.equals(dept.getDivision())) {
                                                                    dept_key.add(dept.getDepartment());
                                                                    dept_value.add(String.valueOf(dept.getOID()));
                                                                } else {
                                                                    div_key.add(dept.getDivision());
                                                                    div_value.add("" + dept.getDivisionId());
                                                                    if (dept_key != null && dept_key.size() == 0) {
                                                                        dept_key.add(dept.getDepartment());
                                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                                    }
                                                                    prevDivision = dept.getDivision();
                                                                }
                                                            } else {
                                                                String chkAdaDiv = "";
                                                                if (div_key != null && div_key.size() > 0) {
                                                                    chkAdaDiv = (String) div_key.get(0);
                                                                }
                                                                if ((div_key != null && div_key.size() == 0) || (chkAdaDiv.equalsIgnoreCase("select ..."))) {
                                                                    comp_key.add(dept.getCompany());
                                                                    comp_value.add("" + dept.getCompanyId());

                                                                    div_key.add(dept.getDivision());
                                                                    div_value.add("" + dept.getDivisionId());

                                                                    dept_key.add(dept.getDepartment());
                                                                    dept_value.add(String.valueOf(dept.getOID()));
                                                                    prevCompany = dept.getCompany();
                                                                    prevDivision = dept.getDivision();
                                                                } else {
                                                                    if (prevCompanyTmp != dept.getCompanyId()) {
                                                                        comp_key.add(dept.getCompany());
                                                                        comp_value.add("" + dept.getCompanyId());
                                                                        prevCompanyTmp = dept.getCompanyId();
                                                                    }

                                                                }

                                                            }
                                                        }
                                                    }
                                                %> 
                                                <%= ControlCombo.draw(FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, "" + oidDep, dept_value, dept_key, "id=\"depart\" onChange=\"javascript:cmdUpdateSection()\"")%> 

                                                * <%= frmEmpAward.getErrorMsg(FrmEmpAward.FRM_FIELD_DEPARTMENT_ID)%>

                                            </div>
                                            <div>Section on award</div>
                                            <div>

                                                <%

                                                    Vector sec_value = new Vector(1, 1);
                                                    Vector sec_key = new Vector(1, 1);
                                                    sec_value.add("0");
                                                    sec_key.add("select ...");

                                        //String sWhereClause = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + " = " + sSelectedDepartment;                                                       
                                                    //Vector listSec = PstSection.list(0, 0, sWhereClause, " SECTION ");
                                                    String secWhere = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + oidDep;
                                                    Vector listSec = PstSection.list(0, 0, secWhere, " SECTION ");
                                                    for (int i = 0; i < listSec.size(); i++) {
                                                        Section sec = (Section) listSec.get(i);
                                                        sec_key.add(sec.getSection());
                                                        sec_value.add(String.valueOf(sec.getOID()));
                                                    }
                                                %>
                                                <%=ControlCombo.draw(FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_SECTION_ID], null, "" + oidSec, sec_value, sec_key, "id=\"section\"")%>

                                            </div>
                                        </td>
                                        <td valign="top" style="background-color: #EEE; padding: 5px;">
                                            <div>Award from</div>
                                            <div>
                                                <input type="text" id="awardfrom" name="<%=FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_AWARD_FROM]%>" value="<%=award.getAwardFrom() %>">
                                                <%--
                                                <select id="awardfrom" name="<%=FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_PROVIDER_ID]%>">
                                                    <option value="0">select...</option>
                                                    <%
                                                    Vector listContact = PstContactList.list(0, 0, "", "");
                                                    if (listContact != null && listContact.size()>0){
                                                        for(int i=0; i<listContact.size(); i++){
                                                            ContactList contact = (ContactList)listContact.get(i);
                                                                award.getProviderId();
                                                                if (award.getProviderId()==contact.getOID()){
                                                                    %>
                                                                    <option selected="selected" value="<%=contact.getOID()%>"><%=contact.getCompName()%></option>
                                                                    <%
                                                                } else {
                                                                    %>
                                                                    <option value="<%=contact.getOID()%>"><%=contact.getCompName()%></option>
                                                                    <%
                                                                }
                                                                
                                                        }
                                                    }
                                                    %>
                                                </select>
                                                --%>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top" colspan="2">
                                <div><strong>Description</strong></div>
                                <div>
                                    <textarea cols="50" rows="5" name="<%= FrmEmpAward.fieldNames[FrmEmpAward.FRM_FIELD_AWARD_DESC]%>"><%= award.getAwardDescription()%></textarea>
                                </div> 
                            </td>
                        </tr>
                        <tr valign="top"> 
                            <td colspan="2"> 
                                <%
                                    ctrLine.setLocationImg(approot + "/images");
                                    ctrLine.initDefault();
                                    ctrLine.setTableWidth("90");
                                    ctrLine.setCommandStyle("buttonlink");

                                    String scomDel = "javascript:cmdDelete('" + oidAward + "')";
                                    String scomAsk = "javascript:cmdAsk('" + oidAward + "')";
                                    String scomEdit = "javascript:cmdEdit('" + oidAward + "')";

                                    ctrLine.setBackCaption("Back To List");
                                    ctrLine.setDeleteCaption("Delete Award Record");
                                    ctrLine.setSaveCaption("Save Award Record");
                                    ctrLine.setConfirmDelCaption("Yes, Delete Award Record");
                                    ctrLine.setAddCaption("Add New Data");

                                    if (privDelete) {
                                        ctrLine.setConfirmDelCommand(scomDel);
                                        ctrLine.setDeleteCommand(scomAsk);
                                        ctrLine.setEditCommand(scomEdit);
                                    } else {
                                        ctrLine.setConfirmDelCaption("");
                                        ctrLine.setDeleteCaption("");
                                        ctrLine.setEditCaption("");
                                    }

                                    if (!privAdd) {
                                        ctrLine.setAddCaption("");
                                    }

                                    if (!privAdd && !privUpdate) {
                                        ctrLine.setSaveCaption("");
                                    }

                                    if (iCommand == Command.GOTO) {
                                        iCommand = prevCommand;
                                    }

                                %>
                                <%= ctrLine.drawImage(iCommand, iErrCode, errMsg)%>

                            </td>                                 
                        </tr>  
                    </table>




                    <% } %>
                </div>
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