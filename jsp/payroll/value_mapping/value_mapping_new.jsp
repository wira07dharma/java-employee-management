<%-- 
    Document   : value_mapping_new
    Created on : Feb 5, 2016, 10:41:32 AM
    Author     : Dimata 007
--%>
<%@page import="com.dimata.harisma.entity.payroll.PayPeriod"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayPeriod"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.entity.payroll.ValueMappingExcel"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.dimata.harisma.entity.payroll.PayComponent"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayComponent"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.harisma.session.employee.SessEmployeeView"%>
<%@page import="com.dimata.harisma.entity.payroll.Value_Mapping"%>
<%@page import="com.dimata.harisma.form.payroll.FrmValue_Mapping"%>
<%@page import="com.dimata.harisma.form.payroll.CtrlValue_Mapping"%>
<%@page import="com.dimata.harisma.entity.payroll.PstValue_Mapping"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_SETUP, AppObjInfo.OBJ_PAYROLL_SETUP_COMPONENT);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    long oidValueMapping = FRMQueryString.requestLong(request, "oid_value_mapping");
    String compCode = FRMQueryString.requestString(request, "comp_code");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    
    
    long s_companyId = FRMQueryString.requestLong(request, "s_company_id");
    long s_divisionId = FRMQueryString.requestLong(request, "s_division_id");
    long s_departmentId = FRMQueryString.requestLong(request, "s_department_id");
    long s_sectionId = FRMQueryString.requestLong(request, "s_section_id");
    long s_levelId = FRMQueryString.requestLong(request, "s_level_id");
    long s_empcatId = FRMQueryString.requestLong(request, "s_empcat_id");
    long s_positionId = FRMQueryString.requestLong(request, "s_position_id");
    long s_gradeId = FRMQueryString.requestLong(request, "s_grade_id");
    long s_maritalId = FRMQueryString.requestLong(request, "s_marital_id");
    long s_religionId = FRMQueryString.requestLong(request, "s_religion_id");
    long s_empId = FRMQueryString.requestLong(request, "s_emp_id");
    long s_resign_status = FRMQueryString.requestInt(request, "s_resign_status");
    double s_value = FRMQueryString.requestDouble(request, "s_value");
    int s_type = FRMQueryString.requestInt(request, "s_type");   
    long s_period = FRMQueryString.requestLong(request, "s_period");
    
    
    String salaryComponent = "-";
    ControlLine ctrLine = new ControlLine();
    int recordToGet = 20;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = "";
    String orderClause = "";
    Value_Mapping valueMapping = new Value_Mapping();
    FrmValue_Mapping frmValueMapping = new FrmValue_Mapping();    
    CtrlValue_Mapping ctrlValueMapping = new CtrlValue_Mapping(request);
    iErrCode = ctrlValueMapping.action(iCommand , oidValueMapping);
    frmValueMapping = ctrlValueMapping.getForm();
    valueMapping = ctrlValueMapping.getValue_Mapping();
    /* Get Pay Component */
    if (compCode.length()>0){
       whereClause = PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_CODE]+"='"+compCode+"'";
       Vector listPayComp = PstPayComponent.list(0, 1, whereClause, "");
       if (listPayComp != null && listPayComp.size()>0){
           PayComponent pComp = (PayComponent)listPayComp.get(0);
           salaryComponent = pComp.getCompName()+" ("+pComp.getCompCode()+")";
       }
    }
    orderClause = PstLevel.fieldNames[PstLevel.FLD_LEVEL_RANK]+" DESC";
    Vector listLevel = PstLevel.list(0, 0, "", orderClause);
    Vector listEmpCat = PstEmpCategory.list(0, 0, "", "");
    Vector listPosition = PstPosition.list(0, 0, "", "");
    Vector listGrade = PstGradeLevel.list(0, 0, "", "");
    Vector listMarital = PstMarital.list(0, 0, "", "");
    Vector listPeriod = PstPayPeriod.list(0, 0, "", PstPayPeriod.fieldNames[PstPayPeriod.FLD_START_DATE]+" DESC");
    Vector listSexKey = new Vector();
    Vector listSexVal = new Vector();
    listSexKey.add("-SELECT-");
    listSexVal.add("-1");
    listSexKey.add("Pria");
    listSexVal.add("0");
    listSexKey.add("Wanita");
    listSexVal.add("1");
    
    Vector listRsKey = new Vector();
    Vector listRsVal = new Vector();
    listRsKey.add("-SELECT-");
    listRsVal.add("-1");
    listRsKey.add("No");
    listRsVal.add("0");
    listRsKey.add("Yes");
    listRsVal.add("1");
    String startDate = "-";
    String endDate = "-";
    if (valueMapping.getStartdate()==null){
        startDate = "2000-01-01";
    } else {
        startDate = ""+valueMapping.getStartdate();
    }
    if (valueMapping.getEnddate()==null){
        endDate = "2015-01-01";
    } else {
        endDate = ""+valueMapping.getEnddate();
    }
    String pilihEmpNum = "Select Data";
    if (valueMapping.getEmployee_id() != 0){
        pilihEmpNum = ValueMappingExcel.getEmployeeNum(valueMapping.getEmployee_id());
    }
    String s_pilihEmpNum = "Select Data";
    if (s_empId != 0){
        s_pilihEmpNum = PstEmployee.getEmployeeNumber(s_empId);
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Payroll - Value Mapping</title>
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <style type="text/css">
            .tblStyle {border-collapse: collapse;}
            .tblStyle td {padding: 5px 7px; border: 1px solid #CCC; font-size: 12px;}
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
            .title_page {color:#0db2e1; font-weight: bold; font-size: 14px; background-color: #EEE; border-left: 1px solid #0099FF; padding: 12px 18px;}

            body {color:#373737;}
            #menu_utama {padding: 9px 14px; font-weight: bold; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
            #menu_title {color:#0099FF; font-size: 14px;}
            #menu_teks {color:#CCC;}
            
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
            
            .title_part {color:#FF7E00; background-color: #F7F7F7; border-left: 1px solid #0099FF; padding: 9px 11px;}
            
            body {background-color: #EEE;}
            .header {
                
            }
            .content-main {
                padding: 5px 25px 25px 25px;
                margin: 0px 23px 59px 23px;
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
            .box {
                margin: 17px 7px;
                background-color: #FFF;
                color:#575757;
            }
            #box_title {
                padding:21px; 
                font-size: 14px; 
                color: #007fba;
                border-top: 1px solid #28A7D1;
                border-bottom: 1px solid #EEE;
            }
            #box_content {
                padding:21px; 
                font-size: 12px;
                color: #575757;
            }
            .box-info {
                padding:21px 43px; 
                background-color: #F7F7F7;
                border-bottom: 1px solid #CCC;
                -webkit-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.065);
                 -moz-box-shadow: 0 1px 4px rgba(0, 0, 0, 0.065);
                      box-shadow: 0 1px 4px rgba(0, 0, 0, 0.065);
            }
            #title-info-name {
                padding: 11px 15px;
                font-size: 35px;
                color: #535353;
            }
            #title-info-desc {
                padding: 7px 15px;
                font-size: 21px;
                color: #676767;
            }
            
            #photo {
                padding: 7px; 
                background-color: #FFF; 
                border: 1px solid #DDD;
            }
            .btn {
                background-color: #00a1ec;
                border-radius: 3px;
                font-family: Arial;
                color: #EEE;
                font-size: 12px;
                padding: 7px 15px 7px 15px;
                text-decoration: none;
            }

            .btn:hover {
                color: #FFF;
                background-color: #007fba;
                text-decoration: none;
            }
            
            .btn-small {
                font-family: sans-serif;
                text-decoration: none;
                padding: 5px 7px; 
                background-color: #676767; color: #F5F5F5; 
                font-size: 11px; cursor: pointer;
                border-radius: 3px;
            }
            .btn-small:hover { background-color: #474747; color: #FFF;}
            
            .btn-small-e {
                font-family: sans-serif;
                text-decoration: none;
                padding: 3px 5px; 
                background-color: #92C8E8; color: #F5F5F5; 
                font-size: 11px; cursor: pointer;
                border-radius: 3px;
            }
            .btn-small-e:hover { background-color: #659FC2; color: #FFF;}
            
            .btn-small-x {
                font-family: sans-serif;
                text-decoration: none;
                padding: 3px 5px; 
                background-color: #EB9898; color: #F5F5F5; 
                font-size: 11px; cursor: pointer;
                border-radius: 3px;
            }
            .btn-small-x:hover { background-color: #D14D4D; color: #FFF;}
            
            .tbl-main {border-collapse: collapse; font-size: 11px; background-color: #FFF; margin: 0px;}
            .tbl-main td {padding: 4px 7px; border: 1px solid #DDD; }
            #tbl-title {font-weight: bold; background-color: #F5F5F5; color: #575757;}
            
            .tbl-small {border-collapse: collapse; font-size: 11px; background-color: #FFF;}
            .tbl-small td {padding: 2px 3px; border: 1px solid #DDD; }
            
            #caption {
                font-size: 12px;
                color: #474747;
                font-weight: bold;
                padding: 2px 0px 3px 0px;
            }
            #divinput {
                margin-bottom: 5px;
                padding-bottom: 5px;
            }
            
            #div_item_sch {
                background-color: #EEE;
                color: #575757;
                padding: 5px 7px;
            }
            
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
                font-size: 12px;
                padding: 7px 0px 8px 15px;
                background-color: #FF6666;
                color: #FFF;
                visibility: hidden;
            }
            #btn-confirm-y {
                padding: 7px 15px 8px 15px;
                background-color: #F25757; color: #FFF; 
                font-size: 12px; cursor: pointer;
            }
            #btn-confirm-n {
                padding: 7px 15px 8px 15px;
                background-color: #E34949; color: #FFF; 
                font-size: 12px; cursor: pointer;
            }
            .footer-page {
                font-size: 12px;
            }
            #record_count{
                font-size: 12px;
                font-weight: bold;
                padding-bottom: 9px;
            }
            .caption {font-weight: bold; padding-bottom: 3px;}
            .divinput {margin-bottom: 7px;}
            #payroll_num {
                background-color: #DEDEDE;
                border-radius: 3px;
                font-family: Arial;
                font-weight: bold;
                color: #474747;
                font-size: 12px;
                padding: 5px 11px 5px 11px;
                cursor: pointer;
            }
        </style>
        <script type="text/javascript">
           function loadList(grade_code, comp_code,s_companyId,s_divisionId,s_departmentId,s_sectionId,s_levelId,s_empcatId,s_positionId,s_gradeId ,s_empId,s_maritalId,s_religionId,s_value,s_type,s_period) {
                if (grade_code.length == 0) { 
                    grade_code = "0";
                } 
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                        document.getElementById("div_respon").innerHTML = xmlhttp.responseText;
                    }
                };
                xmlhttp.open("GET", "list-valuemap-ajax.jsp?grade_code=" + grade_code + "&comp_code="+comp_code+ "&s_company_id="+s_companyId+ "&s_division_id="+s_divisionId+ "&s_department_id="+s_departmentId+ "&s_section_id="+s_sectionId+ "&s_level_id="+s_levelId+ "&s_empcat_id="+s_empcatId+ "&s_position_id="+s_positionId+ "&s_grade_id="+s_gradeId+ "&s_emp_id="+s_empId+ "&s_marital_id="+s_maritalId+ "&s_value="+s_value+ "&s_type="+s_type+ "&s_period="+s_period, true);
                xmlhttp.send();
                
            }
            
            function prepare(comp_code, oid_value_mapping,s_companyId,s_divisionId,s_departmentId,s_sectionId,s_levelId,s_empcatId,s_positionId,s_gradeId ,s_empId,s_maritalId,s_religionId,s_value,s_type,s_period){
                loadList("0", comp_code,s_companyId,s_divisionId,s_departmentId,s_sectionId,s_levelId,s_empcatId,s_positionId,s_gradeId ,s_empId,s_maritalId,s_religionId,s_value,s_type,s_period);
                pageLoad();
                loadCompany(oid_value_mapping);
                s_loadCompany(oid_value_mapping,s_companyId,s_divisionId,s_departmentId,s_sectionId);
            }
            
            function cmdListFirst(start, comp_code,s_companyId,s_divisionId,s_departmentId,s_sectionId,s_levelId,s_empcatId,s_positionId,s_gradeId ,s_empId,s_maritalId,s_religionId,s_value,s_type,s_period){                
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                        document.getElementById("div_respon").innerHTML = xmlhttp.responseText;
                    }
                };
                xmlhttp.open("GET", "list-valuemap-ajax.jsp?grade_code=0&comp_code="+comp_code+"&command=" + <%=Command.FIRST%> + "&start="+start+ "&s_company_id="+s_companyId+ "&s_division_id="+s_divisionId+ "&s_department_id="+s_departmentId+ "&s_section_id="+s_sectionId+ "&s_level_id="+s_levelId+ "&s_empcat_id="+s_empcatId+ "&s_position_id="+s_positionId+ "&s_grade_id="+s_gradeId+ "&s_emp_id="+s_empId+ "&s_marital_id="+s_maritalId+ "&s_value="+s_value+ "&s_type="+s_type+ "&s_period="+s_period, true);
                xmlhttp.send();
            }

            function cmdListPrev(start, comp_code,s_companyId,s_divisionId,s_departmentId,s_sectionId,s_levelId,s_empcatId,s_positionId,s_gradeId ,s_empId,s_maritalId,s_religionId,s_value,s_type,s_period){
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                        document.getElementById("div_respon").innerHTML = xmlhttp.responseText;
                    }
                };
                xmlhttp.open("GET", "list-valuemap-ajax.jsp?grade_code=0&comp_code="+comp_code+"&command=" + <%=Command.PREV%> + "&start="+start+ "&s_company_id="+s_companyId+ "&s_division_id="+s_divisionId+ "&s_department_id="+s_departmentId+ "&s_section_id="+s_sectionId+ "&s_level_id="+s_levelId+ "&s_empcat_id="+s_empcatId+ "&s_position_id="+s_positionId+ "&s_grade_id="+s_gradeId+ "&s_emp_id="+s_empId+ "&s_marital_id="+s_maritalId+ "&s_value="+s_value+ "&s_type="+s_type+ "&s_period="+s_period, true);
                xmlhttp.send();
            }

            function cmdListNext(start, comp_code,s_companyId,s_divisionId,s_departmentId,s_sectionId,s_levelId,s_empcatId,s_positionId,s_gradeId ,s_empId,s_maritalId,s_religionId,s_value,s_type,s_period){
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                        document.getElementById("div_respon").innerHTML = xmlhttp.responseText;
                    }
                };
                xmlhttp.open("GET", "list-valuemap-ajax.jsp?grade_code=0&comp_code="+comp_code+"&command=" + <%=Command.NEXT%> + "&start="+start+ "&s_company_id="+s_companyId+ "&s_division_id="+s_divisionId+ "&s_department_id="+s_departmentId+ "&s_section_id="+s_sectionId+ "&s_level_id="+s_levelId+ "&s_empcat_id="+s_empcatId+ "&s_position_id="+s_positionId+ "&s_grade_id="+s_gradeId+ "&s_emp_id="+s_empId+ "&s_marital_id="+s_maritalId+ "&s_value="+s_value+ "&s_type="+s_type+ "&s_period="+s_period, true);
                xmlhttp.send();
            }

            function cmdListLast(start, comp_code,s_companyId,s_divisionId,s_departmentId,s_sectionId,s_levelId,s_empcatId,s_positionId,s_gradeId ,s_empId,s_maritalId,s_religionId,s_value,s_type,s_period){
                var xmlhttp = new XMLHttpRequest();
                xmlhttp.onreadystatechange = function() {
                    if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                        document.getElementById("div_respon").innerHTML = xmlhttp.responseText;
                    }
                };
                xmlhttp.open("GET", "list-valuemap-ajax.jsp?grade_code=0&comp_code="+comp_code+"&command=" + <%=Command.LAST%> + "&start="+start+ "&s_company_id="+s_companyId+ "&s_division_id="+s_divisionId+ "&s_department_id="+s_departmentId+ "&s_section_id="+s_sectionId+ "&s_level_id="+s_levelId+ "&s_empcat_id="+s_empcatId+ "&s_position_id="+s_positionId+ "&s_grade_id="+s_gradeId+ "&s_emp_id="+s_empId+ "&s_marital_id="+s_maritalId+ "&s_value="+s_value+ "&s_type="+s_type+ "&s_period="+s_period, true);
                xmlhttp.send();
            }
            
            function cmdBack(){
                document.frm.command.value="<%=Command.BACK%>";
                document.frm.action="../setup/salary-comp.jsp";
                document.frm.submit();
            }
            
            function loadCompany(oid) {
                if (oid.length == 0) { 
                    document.getElementById("txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax.jsp?oid_value_mapping=" + oid, true);
                    xmlhttp.send();
                }
            }

            function loadDivision(str) {
                if (str.length == 0) { 
                    document.getElementById("txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax.jsp?company_id=" + str, true);
                    xmlhttp.send();
                }
            }

            function loadDepartment(comp_id, divisi_id) {
                if ((comp_id.length == 0)&&(divisi_id.length == 0)) { 
                    document.getElementById("txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax.jsp?company_id="+comp_id+"&division_id=" + divisi_id, true);
                    xmlhttp.send();
                }
            }

            function loadSection(comp_id, divisi_id, depart_id) {
                if ((comp_id.length == 0)&&(divisi_id.length == 0)&&(depart_id.length == 0)) { 
                    document.getElementById("txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax.jsp?company_id="+comp_id+"&division_id=" + divisi_id +"&department_id="+depart_id, true);
                    xmlhttp.send();
                }
            }
            
            
            function s_loadCompany(oid,s_companyId,s_divisionId,s_departmentId,s_sectionId) {
                if (oid.length == 0) { 
                    document.getElementById("s_txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("s_txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax_search.jsp?oid_value_mapping=" + oid + "&s_company_id="+s_companyId+ "&s_division_id="+s_divisionId+ "&s_department_id="+s_departmentId+ "&s_section_id="+s_sectionId, true);
                    xmlhttp.send();
                }
            }

            function s_loadDivision(str) {
                if (str.length == 0) { 
                    document.getElementById("s_txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("s_txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax_search.jsp?s_company_id=" + str, true);
                    xmlhttp.send();
                }
            }

            function s_loadDepartment(comp_id, divisi_id) {
                if ((comp_id.length == 0)&&(divisi_id.length == 0)) { 
                    document.getElementById("s_txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("s_txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax_search.jsp?s_company_id="+comp_id+"&s_division_id=" + divisi_id, true);
                    xmlhttp.send();
                }
            }

            function s_loadSection(comp_id, divisi_id, depart_id) {
                if ((comp_id.length == 0)&&(divisi_id.length == 0)&&(depart_id.length == 0)) { 
                    document.getElementById("s_txtHint").innerHTML = "";
                    return;
                } else {
                    var xmlhttp = new XMLHttpRequest();
                    xmlhttp.onreadystatechange = function() {
                        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
                            document.getElementById("s_txtHint").innerHTML = xmlhttp.responseText;
                        }
                    };
                    xmlhttp.open("GET", "structure-ajax_search.jsp?s_company_id="+comp_id+"&s_division_id=" + divisi_id +"&s_department_id="+depart_id, true);
                    xmlhttp.send();
                }
            }
            
            
            
            function cmdAdd(compCode){
                document.frm.oid_value_mapping.value="0";
                document.frm.command.value="<%=Command.ADD%>";
                document.frm.action="value_mapping_new.jsp?comp_code="+compCode+"";
                document.frm.submit();
            }
            
            function cmdImport(){
                document.frm.oid_value_mapping.value="0";
                document.frm.command.value="<%=Command.ASSIGN %>";
                document.frm.action="value_mapping_import.jsp";
                document.frm.submit();
            }

            function cmdCloseAllEndDate(compCode){
                window.open("close_all_end_date.jsp?comp_code="+compCode, null, "height=300,width=400, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");  
            }

            function cmdSave(){
                document.frm.command.value="<%=Command.SAVE%>";
                document.frm.action="value_mapping_new.jsp";
                document.frm.submit();
            }
            function cmdSearch(){
                document.frm.command.value="<%=Command.SEARCH%>";
                document.frm.action="value_mapping_new.jsp";
                document.frm.submit();
            }

            function cmdEdit(oid, compCode){
                document.frm.oid_value_mapping.value=oid;
                document.frm.command.value="<%=Command.EDIT%>";
                document.frm.action="value_mapping_new.jsp?comp_code="+compCode+"";
                document.frm.submit();
            }
            
            function cmdAsk(oid){
                document.getElementById("oid_value_mapping").value=oid;
                document.getElementById("confirm").style.visibility="visible";
            }
            
            function cmdNoDel(){
                document.getElementById("oid_value_mapping").value="0";
                document.getElementById("confirm").style.visibility="hidden";
            }
            
            function cmdDelete(compCode){
                var oid = document.getElementById("oid_value_mapping").value;
                document.frm.oid_value_mapping.value=oid;
                document.frm.command.value="<%=Command.DELETE%>";
                document.frm.action="value_mapping_new.jsp?comp_code="+compCode+"";
                document.frm.submit();
            }
            
            function cmdCancel(compCode){
                document.frm.command.value="<%=Command.CANCEL%>";
                document.frm.action="value_mapping_new.jsp?comp_code="+compCode+"";
                document.frm.submit();
            }
            
            function cmdBrowseUser(){
                newWindow=window.open("emp-search.jsp","UserData", "height=600,width=500,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
                newWindow.focus();
            }
            function cmdBrowseUser_s(){
                newWindow=window.open("emp-search-val_search.jsp","UserData", "height=600,width=500,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
                newWindow.focus();
            }
            
            function radio() {
                if (document.getElementById('vm').checked) {
                    document.getElementById('period').style.display = 'none';
                } else if (document.getElementById('he').checked) {
                    document.getElementById('period').style.display = 'none';
                } else if (document.getElementById('pay').checked) {
                    document.getElementById('period').style.display = 'block';
                }
            }
        </script>
        <link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css">
        <script src="<%=approot%>/javascripts/jquery.js"></script>
        <script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>
        <script>
        function pageLoad(){ $(".mydate").datepicker({ dateFormat: "yy-mm-dd" }); } 
	</script>
    </head>
    <body onload="prepare('<%=compCode%>','<%=oidValueMapping%>','<%=s_companyId%>','<%=s_divisionId%>','<%=s_departmentId%>','<%=s_sectionId%>','<%=s_levelId%>','<%=s_empcatId%>','<%=s_positionId%>','<%=s_gradeId %>','<%=s_empId%>','<%=s_maritalId%>','<%=s_religionId%>','<%=s_value%>','<%=s_type%>','<%=s_period%>')">
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
            <span id="menu_title">Payroll <strong style="color:#333;"> / </strong>Value Mapping</span> &HorizontalLine;
            &nbsp;<span style="color:#999"><%=salaryComponent%></span>
            <a style="color:#F5F5F5" class="btn-small" href="javascript:cmdBack()">Back</a>
        </div>
        <div class="content-main">
            <form name="frm" method ="post" action="">
            <input type="hidden" name="command" value="<%=iCommand%>">
            <input type="hidden" name="start" value="<%=start%>">
            <input type="hidden" name="prev_command" value="<%=prevCommand%>">
            <input type="hidden" id="oid_value_mapping" name="oid_value_mapping" value="<%=oidValueMapping%>">
            <input type="hidden" name="comp_code" value="<%= compCode %>">
            <input type="hidden" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_COMP_CODE] %>" value="<%= compCode %>">
            
            <div>&nbsp;</div>
            <a class="btn" style="color:#FFF" href="javascript:cmdAdd('<%=compCode%>')">Add Data</a>
            <a class="btn" style="color:#FFF" href="javascript:cmdImport()">Import Data from Excel</a>
            <a class="btn" style="color:#FFF" href="javascript:cmdCloseAllEndDate('<%=compCode%>')">Close All End Date</a>
            <span id="confirm">Are you sure to delete data? <a id="btn-confirm-y" href="javascript:cmdDelete('<%=compCode%>')">Yes</a><a id="btn-confirm-n" href="javascript:cmdNoDel()">No</a></span>
            <div>&nbsp;</div>
            
            <div style="padding: 17px; background-color: #FFF;">
                    <div id="menu_utama">
                        Form Search
                    </div>
                    <div>&nbsp;</div>
                    <table>
                        <tr>
                            <td valign="top">
                                <div class="caption">Search Type</div>
                                <%
                                    String chekr = "";
                                    String chekr1 = "";
                                    String chekr2 = "";
                                    if (s_type == 0) {
                                        chekr = " checked ";
                                    } 

                                    if (s_type == 1) {
                                        chekr1 = " checked ";
                                    }

                                    if (s_type == 2) {
                                        chekr2 = " checked ";
                                    }
                                %>
                                <input type="radio" name="s_type" value="0" id="vm" onclick="javascript:radio();" <%=chekr%>>
                                From Value Mapping
                                <input type="radio" name="s_type" value="1" id="he" onclick="javascript:radio();" <%=chekr1%>>
                                From Databank
                                <input type="radio" name="s_type" value="2" id="pay" onclick="javascript:radio();" <%=chekr2%>>
                                From Pay Slip 
                            </td>
                        </tr>
                        <%
                            String display = "";
                            if (s_type == 2){
                                display = "style=\"display:block;\"";
                            } else {
                                display = "style=\"display:none;\"";
                            }
                        %>
                        <tr id="period" style="<%=display%>">
                            <td valign="top">
                                <div class="caption">Period</div>
                                <div class="divinput">
                                    <select name="s_period">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listPeriod != null && listPeriod.size()>0){
                                            for(int i=0; i<listPeriod.size(); i++){
                                                PayPeriod payPeriod = (PayPeriod)listPeriod.get(i);
                                                if (s_period==payPeriod.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=payPeriod.getOID()%>"><%= payPeriod.getPeriod() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=payPeriod.getOID()%>"><%= payPeriod.getPeriod() %></option>
                                                    <%
                                                }
                                                
                                            }
                                        }
                                        %>
                                    </select>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">
                                <div id="s_txtHint"></div>
                                                                
                            </td>
                            
                            <td valign="top" style="padding-left: 21px">
                                <div class="caption">Level</div>
                                <div class="divinput">
                                    <select name="s_level_id">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listLevel != null && listLevel.size()>0){
                                            for(int i=0; i<listLevel.size(); i++){
                                                Level level = (Level)listLevel.get(i);
                                                if (s_levelId==level.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=level.getOID()%>"><%= level.getLevel() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=level.getOID()%>"><%= level.getLevel() %></option>
                                                    <%
                                                }
                                                
                                            }
                                        }
                                        %>
                                    </select>
                                </div>

                                <div class="caption">Employee Category</div>
                                <div class="divinput">
                                    <select name="s_empcat_id">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listEmpCat != null && listEmpCat.size()>0){
                                            for(int i=0; i<listEmpCat.size(); i++){
                                                EmpCategory empCat = (EmpCategory)listEmpCat.get(i);
                                                if (s_empcatId == empCat.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=empCat.getOID()%>"><%= empCat.getEmpCategory() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=empCat.getOID()%>"><%= empCat.getEmpCategory() %></option>
                                                    <%
                                                }
                                                
                                            }
                                        }
                                        %>
                                    </select>
                                </div>

                                <div class="caption">Position</div>
                                <div class="divinput">
                                    <select name="s_position_id">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listPosition != null && listPosition.size()>0){
                                            for(int i=0; i<listPosition.size(); i++){
                                                Position position = (Position)listPosition.get(i);
                                                if (s_positionId == position.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=position.getOID()%>"><%= position.getPosition() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=position.getOID()%>"><%= position.getPosition() %></option>
                                                    <%
                                                }
                                            }
                                        }
                                        %>
                                    </select>
                                </div>

                                <div class="caption">Grade</div>
                                <div class="divinput">
                                    <select name="s_grade_id">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listGrade != null && listGrade.size()>0){
                                            for(int i=0; i<listGrade.size(); i++){
                                                GradeLevel grade = (GradeLevel)listGrade.get(i);
                                                if (s_gradeId == grade.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=grade.getOID()%>"><%= grade.getCodeLevel() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=grade.getOID()%>"><%= grade.getCodeLevel() %></option>
                                                    <%
                                                }
                                            }
                                        }
                                        %>
                                    </select>
                                </div>
                            </td>
                            <td valign="top" style="padding-left: 21px">
                                <div class="caption">Payroll Num</div>
                                <div class="divinput">
                                    
                                    <div id="s_payroll_num" onclick="javascript:cmdBrowseUser_s()"><%= s_pilihEmpNum %></div>
                                    <input type="hidden" name="s_emp_id" value="<%= s_empId %>" />
                                </div>
                                
                                <div class="caption">Marital</div>
                                <div class="divinput">
                                    <select name="s_marital_id">
                                        <option value="0">-SELECT-</option>
                                        <%
                                        if (listMarital != null && listMarital.size()>0){
                                            for (int i=0; i<listMarital.size(); i++){
                                                Marital marital = (Marital)listMarital.get(i);
                                                if (s_maritalId == marital.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=marital.getOID()%>"><%=marital.getMaritalCode()%></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=marital.getOID()%>"><%=marital.getMaritalCode()%></option>
                                                    <%
                                                }
                                                
                                            }
                                        }
                                        %>
                                    </select>
                                </div>
                                    
                                <div class="caption">Religion</div>
                                <div class="divinput">
                                    <select name="s_religion_id">
                                        <option  value="0">Select</option>
                                        <%
                                        Vector listReligion = PstReligion.listAll();
                                        for(int s=0; s<listReligion.size(); s++){
                                            Religion religion = (Religion) listReligion.get(s);
                                            String key = religion.getReligion();
                                            String val = ""+religion.getOID();
                                            if (s_religionId == religion.getOID()){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                </div>
      
                                <div class="caption">Value</div>
                                <div class="divinput">
                                    <input type="text" name="s_value" value="<%= s_value %>" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">&nbsp;</td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a class="btn" style="color:#FFF" href="javascript:cmdSearch()">Search</a>
                            </td>
                        </tr>
                    </table> 
                </div>
            
            
            <div id="div_respon"></div>
            <div>&nbsp;</div>
            
            <%
            if (iCommand == Command.ADD || iCommand == Command.EDIT){
                %>
                <div style="padding: 17px; background-color: #FFF;">
                    <div id="menu_utama">
                        Form Input Data
                    </div>
                    <div>&nbsp;</div>
                    <table>
                        <tr>
                            <td valign="top">
                                <div id="txtHint"></div>
                                <div class="caption">Level</div>
                                <div class="divinput">
                                    <select name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LEVEL_ID] %>">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listLevel != null && listLevel.size()>0){
                                            for(int i=0; i<listLevel.size(); i++){
                                                Level level = (Level)listLevel.get(i);
                                                if (valueMapping.getLevel_id()==level.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=level.getOID()%>"><%= level.getLevel() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=level.getOID()%>"><%= level.getLevel() %></option>
                                                    <%
                                                }
                                                
                                            }
                                        }
                                        %>
                                    </select>
                                </div>

                                <div class="caption">Employee Category</div>
                                <div class="divinput">
                                    <select name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_CATEGORY] %>">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listEmpCat != null && listEmpCat.size()>0){
                                            for(int i=0; i<listEmpCat.size(); i++){
                                                EmpCategory empCat = (EmpCategory)listEmpCat.get(i);
                                                if (valueMapping.getEmployee_category() == empCat.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=empCat.getOID()%>"><%= empCat.getEmpCategory() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=empCat.getOID()%>"><%= empCat.getEmpCategory() %></option>
                                                    <%
                                                }
                                                
                                            }
                                        }
                                        %>
                                    </select>
                                </div>

                                <div class="caption">Position</div>
                                <div class="divinput">
                                    <select name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_POSITION_ID] %>">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listPosition != null && listPosition.size()>0){
                                            for(int i=0; i<listPosition.size(); i++){
                                                Position position = (Position)listPosition.get(i);
                                                if (valueMapping.getPosition_id() == position.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=position.getOID()%>"><%= position.getPosition() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=position.getOID()%>"><%= position.getPosition() %></option>
                                                    <%
                                                }
                                            }
                                        }
                                        %>
                                    </select>
                                </div>

                                <div class="caption">Grade</div>
                                <div class="divinput">
                                    <select name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_GRADE] %>">
                                        <option value="0">-select-</option>
                                        <%
                                        if (listGrade != null && listGrade.size()>0){
                                            for(int i=0; i<listGrade.size(); i++){
                                                GradeLevel grade = (GradeLevel)listGrade.get(i);
                                                if (valueMapping.getGrade() == grade.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=grade.getOID()%>"><%= grade.getCodeLevel() %></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=grade.getOID()%>"><%= grade.getCodeLevel() %></option>
                                                    <%
                                                }
                                            }
                                        }
                                        %>
                                    </select>
                                </div>                                
                            </td>
                            <td valign="top" style="padding-left: 21px">
                                <div class="caption">Payroll Num</div>
                                <div class="divinput">
                                    <div id="payroll_num" onclick="javascript:cmdBrowseUser()"><%= pilihEmpNum %></div>
                                    <input type="hidden" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_EMPLOYEE_ID] %>" value="<%= valueMapping.getEmployee_id() %>" />
                                </div>
                                
                                <div class="caption">Start date</div>
                                <div class="divinput">
                                    <input type="text" class="mydate" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_START_DATE] %>" value="<%= startDate %>" />
                                </div>

                                <div class="caption">End date</div>
                                <div class="divinput">
                                    <input type="text" class="mydate" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_END_DATE] %>" value="<%= endDate %>" />
                                </div>

                                <div class="caption">Marital</div>
                                <div class="divinput">
                                    <select name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_MARITAL_ID] %>">
                                        <option value="0">-SELECT-</option>
                                        <%
                                        if (listMarital != null && listMarital.size()>0){
                                            for (int i=0; i<listMarital.size(); i++){
                                                Marital marital = (Marital)listMarital.get(i);
                                                if (valueMapping.getMarital_id() == marital.getOID()){
                                                    %>
                                                    <option selected="selected" value="<%=marital.getOID()%>"><%=marital.getMaritalCode()%></option>
                                                    <%
                                                } else {
                                                    %>
                                                    <option value="<%=marital.getOID()%>"><%=marital.getMaritalCode()%></option>
                                                    <%
                                                }
                                                
                                            }
                                        }
                                        %>
                                    </select>
                                </div>

                                <div class="caption">Length of service</div>
                                <div class="divinput">
                                    <input type="text" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LENGTH_OF_SERVICE] %>" value="<%= valueMapping.getLength_of_service() %>" />
                                </div>

                                <div class="caption">GEO</div>
                                <div class="divinput">
                                    <input type="text" name="geo_address_pmnt" value="" onClick="javascript:updateGeoAddressPmnt()">
                                    <input type="hidden" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_COUNTRY_ID] %>" value=""> 
                                    <input type="hidden" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_PROVINCE_ID] %>" value=""> 
                                    <input type="hidden" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_REGENCY_ID] %>" value=""> 
                                    <input type="hidden" name="<%= FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_ADDR_SUBREGENCY_ID] %>" value="">
                                </div>

                                <div class="caption">Sex</div>
                                <div class="divinput">
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_SEX]%>">
                                        <%
                                        for(int s=0; s<listSexKey.size(); s++){
                                            String key = (String)listSexKey.get(s);
                                            String val = (String)listSexVal.get(s);
                                            if (valueMapping.getSex() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                </div>
                                    
                                <div class="caption">Resign Status</div>
                                <div class="divinput">
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_RESIGN_STATUS]%>">
                                        <%
                                        for(int s=0; s<listRsKey.size(); s++){
                                            String key = (String)listRsKey.get(s);
                                            String val = (String)listRsVal.get(s);
                                            if (valueMapping.getResignStatus() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                </div>    
                                    
                                <div class="caption">Religion</div>
                                <div class="divinput">
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_RELIGION]%>">
                                        <option  value="0">Select</option>
                                        <%
                                        //Vector listReligion = PstReligion.listAll();
                                        for(int s=0; s<listReligion.size(); s++){
                                            Religion religion = (Religion) listReligion.get(s);
                                            String key = religion.getReligion();
                                            String val = ""+religion.getOID();
                                            if (valueMapping.getReligion() == religion.getOID()){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                </div>
    
                                <div class="caption">Los</div>
                                <div class="divinput">
                                    <%
                                    
                    //day
                    Vector day_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 32; s++) {
                        day_keyVal.add(""+s);
                    }
                    
                    //month
                    Vector month_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 13; s++) {
                        month_keyVal.add(""+s);
                    }
                    
                    //year
                    Vector year_keyVal = new Vector(1, 1);
                    for (int s = 0; s < 13; s++) {
                        year_keyVal.add(""+s);
                    }
                    
                    
                    //current
                    Vector currentDate_value = new Vector(1, 1);
                    Vector currentDate_key = new Vector(1, 1);
                    currentDate_key.add("No");
                    currentDate_value.add("0");    
                    currentDate_key.add("Yes");
                    currentDate_value.add("1");
                                    %>
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_DAY]%>">
                                        <%
                                        for(int s=0; s<day_keyVal.size(); s++){
                                            
                                            String key = (String)day_keyVal.get(s);
                                            String val = (String)day_keyVal.get(s);
                                            if (valueMapping.getLosFromInDay() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_MONTH]%>">
                                        <%
                                        for(int s=0; s< month_keyVal.size(); s++){
                                            
                                            String key = (String)month_keyVal.get(s);
                                            String val = (String)month_keyVal.get(s);
                                            if (valueMapping.getLosFromInMonth() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_FROM_IN_YEAR]%>">
                                        <%
                                        for(int s=0; s< year_keyVal.size(); s++){
                                            
                                            String key = (String)year_keyVal.get(s);
                                            String val = (String)year_keyVal.get(s);
                                            if (valueMapping.getLosFromInYear() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                    
                                    x
                                    
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_DAY]%>">
                                        <%
                                        for(int s=0; s<day_keyVal.size(); s++){
                                            
                                            String key = (String)day_keyVal.get(s);
                                            String val = (String)day_keyVal.get(s);
                                            if (valueMapping.getLosToInDay() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_MONTH]%>">
                                        <%
                                        for(int s=0; s< month_keyVal.size(); s++){
                                            
                                            String key = (String)month_keyVal.get(s);
                                            String val = (String)month_keyVal.get(s);
                                            if (valueMapping.getLosToInMonth() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                    <select name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_LOS_TO_IN_YEAR]%>">
                                        <%
                                        for(int s=0; s< year_keyVal.size(); s++){
                                            
                                            String key = (String)year_keyVal.get(s);
                                            String val = (String)year_keyVal.get(s);
                                            if (valueMapping.getLosToInYear() == Integer.valueOf(val)){
                                            %>
                                            <option selected="selected" value="<%=val%>"><%=key%></option>
                                            <%
                                            } else {
                                            %>
                                            <option value="<%=val%>"><%=key%></option>
                                            <%
                                            }
                                        }
                                        %>
                                    </select>
                                
                                    
                                </div>    
                                <div class="caption">Remark</div>
                                <div class="divinput">
                                    <input type="text" name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_PARAMETER]%>" value="<%= valueMapping.getParameter()%>" />
                                </div>    
                                    
                                <div class="caption">Value</div>
                                <div class="divinput">
                                    <input type="text" name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_VALUE]%>" value="<%= valueMapping.getValue() %>" />
                                </div>
								
								<div class="caption">Remark</div>
                                <div class="divinput">
                                    <input type="text" name="<%=FrmValue_Mapping.fieldNames[FrmValue_Mapping.FRM_FIELD_REMARK]%>" value="<%= valueMapping.getRemark()%>" />
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">&nbsp;</td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a class="btn" style="color:#FFF" href="javascript:cmdSave()">Save</a>
                                <a class="btn" style="color:#FFF" href="javascript:cmdCancel('<%=compCode%>')">Cancel</a>
                            </td>
                        </tr>
                    </table> 
                </div>
                <%
            }
            %>
            
            <div>&nbsp;</div>
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
