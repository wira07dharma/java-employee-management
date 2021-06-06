<%@page import="java.text.DateFormat"%>
<%@page import="java.text.ParseException"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
            /*
             * Page Name  		:  familymember.jsp
             * Created on 		:  [25-9-2002] [9.00] AM
             *
             * @author  		: lkarunia
             * @version  		: 01
             */

            /*******************************************************************
             * Page Description 	: this page represent family member of employee
             * Imput Parameters 	: [input parameter ...]
             * Output 			: [output ...]
             *******************************************************************/
%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>

<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>

<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ include file = "../../main/javainit.jsp" %>

<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%
            /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>

<!-- Jsp Block -->

<%

            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            long oidFamilyMember = FRMQueryString.requestLong(request, "family_member_oid");
            long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");

      if (iCommand == Command.DELETE){
          if (oidFamilyMember != 0){
              try {
                  PstFamilyMember.deleteExc(oidFamilyMember);
              } catch(Exception e){
                  System.out.println(e.toString());
              }
          }
      }
//variable declaration
            int recordToGet = 50;
            String msgString = "";
            int iErrCode = FRMMessage.NONE;
            String whereClause = PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID] + " = " + oidEmployee;
            String orderClause = "";

            CtrlFamilyMember ctrlFamilyMember = new CtrlFamilyMember(request);
            ControlLine ctrLine = new ControlLine();
            Vector listFamilyMember = new Vector(1, 1);

            FamilyMember familyMember = ctrlFamilyMember.getFamilyMember();
            FrmFamilyMember frmFamilyMember = ctrlFamilyMember.getForm();
           
            Vector vctMember = new Vector(1, 1);
            if (iCommand == Command.SAVE) {
                for (int i = 0; i < 50; i++) {
                    String famName = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_FULL_NAME] + "_" + i);
                    String famRelation = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATIONSHIP] + "_" + i);
                    boolean famGuaranted = FRMQueryString.requestBoolean(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_GUARANTEED] + "_" + i);
                    boolean famIgnore = FRMQueryString.requestBoolean(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_IGNORE_BIRTH] + "_" + i);
                    Date famBirth = FRMQueryString.requestDate(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_BIRTH_DATE] + "_" + i);
                    String famJob = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_JOB] + "_" + i);
					String famJobIns = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_JOB_INSTITUTIONS] + "_" + i);
                    String famAddress = FRMQueryString.requestString(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_ADDRESS] + "_" + i);
                    long educationId = FRMQueryString.requestLong(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_EDUCATION_ID] + "_" + i);
                    long relegionId = FRMQueryString.requestLong(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELIGION_ID] + "_" + i);
                    int sex = FRMQueryString.requestInt(request, FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SEX] + "_" + i);
                    
                    FamilyMember famMember = new FamilyMember();
                    if (famIgnore == true){
                        String startDateString = "15/31/0000";
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy"); 
                        try {
                            famBirth = df.parse(startDateString);
                        } catch(ParseException e){
                            e.printStackTrace();
                        } catch(Exception e){
                         //handle exception
                        }
                    }
                    famMember.setFullName(famName);
                    famMember.setRelationship(famRelation);
                    famMember.setGuaranteed(famGuaranted);
                    famMember.setBirthDate(famBirth);
                    famMember.setIgnoreBirth(famIgnore);
                    famMember.setJob(famJob);
                    famMember.setAddress(famAddress);
                    famMember.setEducationId(educationId);
                    famMember.setReligionId(relegionId);
                    famMember.setSex(sex);
					famMember.setJobInstitutions(famJobIns);
                    vctMember.add(famMember);
                }
            }


//out.println(vctMember);
//for(int i=0; i<4; i++){
//	FamilyMember famMember = (FamilyMember)vctMember.get(i);
//	out.println("i :"+i);
            //out.println(famMember.getFullName());
            //out.println(famMember.getRelationship());
            //out.println(famMember.getGuaranteed());
            //out.println(famMember.getBirthDate());
            //out.println(famMember.getJob());
            //out.println(famMember.getAddress());
//}

//switch statement 
            iErrCode = ctrlFamilyMember.action(iCommand, oidEmployee, vctMember, request, emplx.getFullName(), appUserIdSess);//oidFamilyMember, oidEmployee);
// end switch
            FrmFamilyMember FrmFamilyMember = ctrlFamilyMember.getForm();

            msgString = ctrlFamilyMember.getMessage();

            whereClause = PstFamilyMember.fieldNames[PstFamilyMember.FLD_EMPLOYEE_ID] + "=" + oidEmployee;
            orderClause = PstFamilyMember.fieldNames[PstFamilyMember.FLD_RELATIONSHIP] + "," + PstFamilyMember.fieldNames[PstFamilyMember.FLD_BIRTH_DATE] + " ASC";

            vctMember = PstFamilyMember.list(0, 0, whereClause, orderClause);
            
            session.putValue("familymember", vctMember);
            session.putValue("oidemployee", oidEmployee);
//out.println(oidEmployee);
//out.println(vctMember.size());
      

%>

<html><!-- #BeginTemplate "/Templates/maintab.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>HARISMA - Family Member</title>
        <script language="JavaScript">
            <!--



            function cmdAdd(){
                document.FrmFamilyMember.family_member_oid.value="0";
                document.FrmFamilyMember.command.value="<%=Command.ADD%>";
                document.FrmFamilyMember.prev_command.value="<%=prevCommand%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdAsk(oidFamilyMember){
                document.FrmFamilyMember.family_member_oid.value=oidFamilyMember;
                document.FrmFamilyMember.command.value="<%=Command.ASK%>";
                document.FrmFamilyMember.prev_command.value="<%=prevCommand%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdConfirmDelete(oidFamilyMember){
                document.FrmFamilyMember.family_member_oid.value=oidFamilyMember;
                document.FrmFamilyMember.command.value="<%=Command.DELETE%>";
                document.FrmFamilyMember.prev_command.value="<%=prevCommand%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }
            function cmdSave(){
                document.FrmFamilyMember.command.value="<%=Command.SAVE%>";
                document.FrmFamilyMember.prev_command.value="<%=prevCommand%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdEdit(oidFamilyMember){
                document.FrmFamilyMember.family_member_oid.value=oidFamilyMember;
                document.FrmFamilyMember.command.value="<%=Command.EDIT%>";
                document.FrmFamilyMember.prev_command.value="<%=prevCommand%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }
	
            function cmdBackEmp(empOID){
                document.FrmFamilyMember.employee_oid.value=empOID;
                document.FrmFamilyMember.command.value="<%=Command.EDIT%>";
                document.FrmFamilyMember.action="employee_edit.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdCancel(oidFamilyMember){
                document.FrmFamilyMember.family_member_oid.value=oidFamilyMember;
                document.FrmFamilyMember.command.value="<%=Command.EDIT%>";
                document.FrmFamilyMember.prev_command.value="<%=prevCommand%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdBack(){
                document.FrmFamilyMember.command.value="<%=Command.BACK%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListFirst(){
                document.FrmFamilyMember.command.value="<%=Command.FIRST%>";
                document.FrmFamilyMember.prev_command.value="<%=Command.FIRST%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListPrev(){
                document.FrmFamilyMember.command.value="<%=Command.PREV%>";
                document.FrmFamilyMember.prev_command.value="<%=Command.PREV%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListNext(){
                document.FrmFamilyMember.command.value="<%=Command.NEXT%>";
                document.FrmFamilyMember.prev_command.value="<%=Command.NEXT%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }

            function cmdListLast(){
                document.FrmFamilyMember.command.value="<%=Command.LAST%>";
                document.FrmFamilyMember.prev_command.value="<%=Command.LAST%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }
            function fnTrapKD(){
                //alert(event.keyCode);
                switch(event.keyCode) {
                    case <%=LIST_PREV%>:
                            cmdListPrev();
                        break;
                    case <%=LIST_NEXT%>:
                            cmdListNext();
                        break;
                    case <%=LIST_FIRST%>:
                            cmdListFirst();
                        break;
                    case <%=LIST_LAST%>:
                            cmdListLast();
                        break;
                    default:
                        break;
                    }
                }
                
                function cmdExportExcel(){
                 
                    var linkPage = "<%=approot%>/employee/databank/export_excel/export_excel_family_member.jsp";    
                    var newWin = window.open(linkPage,"attdReportDaily","height=700,width=990,status=yes,toolbar=yes,menubar=no,resizable=yes,scrollbars=yes,location=yes"); 			
                     newWin.focus();
                }
                //-------------- script control line -------------------
                //-->
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
                <!--
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
                    //-->
        </SCRIPT>
        <style type="text/css">
            .tblStyle {border-collapse: collapse;font-size: 11px;}
            .tblStyle td {padding: 3px 5px; border: 1px solid #CCC; background-color: #F7F7F7;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757; }
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
        </style>
        <script type="text/javascript">
            function cmdAsking(oid){
                document.FrmFamilyMember.family_member_oid.value=oid;
                document.FrmFamilyMember.command.value="<%=Command.ASK%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }
            function cmdHapus(oid){
                document.FrmFamilyMember.family_member_oid.value=oid;
                document.FrmFamilyMember.command.value="<%=Command.DELETE%>";
                document.FrmFamilyMember.action="familymember.jsp";
                document.FrmFamilyMember.submit();
            }
        </script>
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
                border-radius: 3px;
                font-family: Arial;
                color: #7a7a7a;
                font-size: 12px;
                padding: 2px 3px ;
                border: solid #d9d9d9 1px;
                text-decoration: none;
            }

            .btn:hover {
                color: #FFF;
                background: #FF6666;
                text-decoration: none;
                border: 1px solid #d74e4e;
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
            <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> <%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER) %></span>
        </div>
        
        <% if (oidEmployee != 0) {%>
        <div class="navbar">
            <ul style="margin-left: 97px">
              <li class=""> <a href="employee_edit.jsp?employee_oid=<%=oidEmployee%>&prev_command=<%=Command.EDIT%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PERSONAL_DATA)%></a> </li>
              <li class="active"> <a href="familymember.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER) %></a> </li>
              <li class=""> <a href="emplanguage.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.COMPETENCIES) %></a> </li>
              <li class=""> <a href="empeducation.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EDUCATION) %></a> </li>
              <li class=""> <a href="experience.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EXPERIENCE) %></a> </li>
              <li class=""> <a href="careerpath.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.CAREER_PATH) %></a> </li>
              <li class=""> <a href="training.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.TRAINING_ON_DATABANK)%></a> </li>
              <li class=""> <a href="warning.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.WARNING) %></a> </li>
              <li class=""> <a href="reprimand.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.REPRIMAND) %></a> </li>
              <li class=""> <a href="award.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.AWARD) %></a> </li>
              <li class=""> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PICTURE) %></a> </li>
              <li class=""> <a href="doc_relevant.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></a> </li>
            </ul>
        </div>
        <%}%>
        
        <div class="content-main">
            <form name="FrmFamilyMember" method="post" action="">
                <input type="hidden" name="command" value="<%=iCommand%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="family_member_oid" value="<%=oidFamilyMember%>">
                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">

                <div class="content-info">
                    <% 
                        if(oidEmployee != 0){
                        Employee employee = new Employee();
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
                        <% Department department = new Department();
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
                    <div id="title-large"><%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER) %></div>
                    <div id="title-small">Daftar anggota keluarga.</div>
                </div>
                
                <% if (iCommand == Command.ASK){ %>
                <table>
                <tr>
                    <td colspan="3">
                        <div id="confirm" style="margin: 3px 5px;">
                            <strong>Are you sure to delete item ?</strong> &nbsp;
                            <button id="btn-confirm" onclick="javascript:cmdHapus('<%=oidFamilyMember%>')">Yes</button>
                            &nbsp;<button id="btn-confirm" onclick="javascript:cmdBack()">No</button>
                        </div>
                    </td>
                </tr>
                </table>
                <% } %>
                
                <div class="content">
                    
                    <table cellspacing="1" cellpadding="1" class="tblStyle">
                        <tr>
                            <td valign="top" class="title_tbl">
                                <div align="center"><b>No</b></div>
                            </td>
                            <td valign="top" class="title_tbl">
                                <div align="center"><b>Full
                                        Name / Sex </b></div>
                            </td>
                            <td valign="top" class="title_tbl">
                                <div align="center"><b>Relationship</b></div>
                            </td>
                            <td valign="top" class="title_tbl">
                                <div align="center"><b>Guaranted</b></div>
                            </td>
                            <td valign="top" class="title_tbl">
                                <div align="center"><b>Date
                                        Of Birth
                                    </b></div>
                            </td>
                            <td valign="top" class="title_tbl">
                                <div align="center"><b>Job / Job Institutions</b></div>
                            </td>
							<td valign="top" class="title_tbl">
                                <div align="center"><b>Address</b></div>
                            </td>
                            <td valign="top" class="title_tbl">
                                <div align="center"><b><%=dictionaryD.getWord(I_Dictionary.EDUCATION) %></b> / <b>Religion</b></div>
                            </td>
                            <td valign="top" class="title_tbl">
                                &nbsp;
                            </td> 

                        </tr>

                        <%  
                            String colorTd = "";
                            String btnDelete = "";
                            for (int idx=0; idx < 50; idx++ ){ 
                                FamilyMember famX = new FamilyMember();
                                if (vctMember != null && vctMember.size() > 0 && vctMember.size() > idx) {
                                    famX = (FamilyMember) vctMember.get(idx);
                                    colorTd = "style=\"background-color: #5CD3FA\"";
                                    btnDelete = "<button class=\"btn\" onclick=\"cmdAsking('"+famX.getOID()+"')\">&times;</button>";
                                } else {
                                    colorTd = "";
                                    btnDelete = "";
                                }
                        %>

                                <tr>
                                    <td valign="top" <%=colorTd%>>
                                        <div align="center"><%=(idx+1)%></div>
                                    </td>
                                    <td valign="top">

                                        <input type="text" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_FULL_NAME]%>_<%=idx%>"  value="<%= famX.getFullName()%>" class="elemenForm" size="30">
                                        <br>
                        <% for (int i = 0; i < PstEmployee.sexValue.length; i++) {
                            String str = "";
                            if (famX.getSex() == PstEmployee.sexValue[i]) {
                                str = "checked";
                            }
                            %> <input type="radio" name="<%=(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_SEX]+"_"+idx )%>" value="<%="" + PstEmployee.sexValue[i]%>" <%=str%> style="border:none">
                            <%=PstEmployee.sexKey[i]%> 
                      <% }%>
                                                                                                                                            * <%=FrmFamilyMember.getErrorMsg(FrmEmployee.FRM_FIELD_SEX)%> 
                                                                                                                                            </td>
                                                                                                                                            <td width="8%"><%--= ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATIONSHIP]+"_3","formElemen",null, ""+fam4.getRelationship(), PstFamilyMember.getRelation(), PstFamilyMember.getRelation()) --%>
                                <%  Vector relationX_value = new Vector(1, 1);
                                        Vector relationX_key = new Vector(1, 1);
                                        Vector listRelationX =PstFamRelation.listAll();
                                                for (int i = 0; i < listRelationX.size(); i++) {
                                                    FamRelation famRelation =(FamRelation) listRelationX.get(i);
                                                    //relation4_value.add(""+famRelation.getFamRelationType());
                                                    relationX_value.add(""+famRelation.getOID());
                                                    relationX_key.add(famRelation.getFamRelation());
                                                }
                                    %>                                                                                                                                               
                                        <%= ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELATIONSHIP]+"_"+idx, "formElemen", null,"" + famX.getRelationship(), relationX_value, relationX_key)%>
                                    </td>
                                    <td valign="top">
                                        <input type="checkbox" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_GUARANTEED]%>_<%=idx%>" value="1" <%if (famX.getGuaranteed()) {%>checked<%}%>>
                                        Yes </td>
                                    <td valign="top"><%=ControlDate.drawDateEmpty(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_BIRTH_DATE] + "_"+idx, famX.getBirthDate(), "formElemen", 0, -115)%>
                                        <input type="checkbox" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_IGNORE_BIRTH]%>_<%=idx%>" value="1" <%if (famX.getIgnoreBirth()) {%>checked<%}%>>
                                        Ignore </td>
                                    <td valign="top">
                                        <input type="text" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_JOB]%>_<%=idx%>"  value="<%= famX.getJob()%>" class="elemenForm" size="30"> / 
                                        <input type="text" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_JOB_INSTITUTIONS]%>_<%=idx%>"  value="<%= famX.getJobInstitutions()%>" class="elemenForm" size="30">
                                    </td>
									<td valign="top">
                                        <input type="text" name="<%=FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_ADDRESS]%>_<%=idx%>"  value="<%= famX.getAddress()%>" class="elemenForm" size="30">
                                    </td>
                                    <td valign="top">                                                                                                                                                                                                                                                                                           
                                    <%    Vector education_value = new Vector(1,1);
                    Vector education_key = new Vector(1,1);																	
                    Vector listEducation = PstEducation.listAll();
                    for(int i=0;i<listEducation.size();i++){
                        Education education = (Education) listEducation.get(i);
                        education_value.add(""+education.getOID());
                        education_key.add(""+education.getEducation());
                    }
                %>
                <% if((listEducation != null) && (listEducation.size() > 0)){%>
                <%= ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_EDUCATION_ID]+"_"+idx,"formElemen",null, ""+famX.getEducationId(), education_value, education_key) %> 
                <% }else {%>
                <font class="comment">No 
                Education available</font> 
                <% }%>
                * <%= FrmFamilyMember.getErrorMsg(FrmEmpEducation.FRM_FIELD_EDUCATION_ID) %>                                                                                                                                           
                                                                                                                                                                                                                                                                                                                                                                                                                                       
                            <% Vector relKey = new Vector(1, 1);
                            Vector relValue = new Vector(1, 1);
                            Vector listReligion = PstReligion.listAll();
                            for (int i = 0; i < listReligion.size(); i++) {
                                Religion religion = (Religion) listReligion.get(i);
                                relKey.add(religion.getReligion());
                                relValue.add("" + religion.getOID());
                            }
                            out.println(ControlCombo.draw(FrmFamilyMember.fieldNames[FrmFamilyMember.FRM_FIELD_RELIGION_ID]+"_"+idx, "formElemen", null, "" + famX.getReligionId(), relValue, relKey));
                            %> * <%=FrmFamilyMember.getErrorMsg(FrmFamilyMember.FRM_FIELD_RELIGION_ID)%>                                                                                                                                            
                            </td>                                                                                                                                            
                            <td>
                                <%=btnDelete%>
                            </td>
                        </tr>

                        <%
                        } 
                        %>
                    </table>
                    
                    <div class="command">
                    <%
                                ctrLine.setLocationImg(approot + "/images");

                                ctrLine.initDefault();
                                ctrLine.setTableWidth("80%");
                                String scomDel = "javascript:cmdAsk('" + oidFamilyMember + "')";
                                String sconDelCom = "javascript:cmdConfirmDelete('" + oidFamilyMember + "')";
                                String scancel = "javascript:cmdEdit('" + oidFamilyMember + "')";
                                ctrLine.setBackCaption("");
                                ctrLine.setCommandStyle("buttonlink");
                                ctrLine.setAddCaption("");
                                ctrLine.setSaveCaption("Save Family Member");
                                ctrLine.setDeleteCaption("");
                                ctrLine.setConfirmDelCaption("");

                                if (privDelete) {
                                    ctrLine.setConfirmDelCommand(sconDelCom);
                                    ctrLine.setDeleteCommand(scomDel);
                                    ctrLine.setEditCommand(scancel);
                                } else {
                                    ctrLine.setConfirmDelCaption("");
                                    ctrLine.setDeleteCaption("");
                                    ctrLine.setEditCaption("");
                                }

                                if (privAdd == false && privUpdate == false) {
                                    ctrLine.setSaveCaption("");
                                }

                                if (privAdd == false) {
                                    ctrLine.setAddCaption("");
                                }

                                iCommand = Command.EDIT;
                    %>
                    <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> 
                    </div>
                    <div>
                        <a href="javascript:cmdExportExcel()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1003" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Export to Excel">Export to Excel</a>
                    </div>
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
