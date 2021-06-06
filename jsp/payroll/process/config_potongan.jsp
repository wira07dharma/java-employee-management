<%-- 
    Document   : config_potongan
    Created on : Mar 4, 2017, 11:06:48 AM
    Author     : mchen
    Description :
        Insert : melalui import file excel
        Update : hnya update satus : aktif / non-aktif
        Delete : tidak bisa, karena ditujukan utk history data
        View   : menampilkan data ataupun filter
--%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="com.dimata.qdep.db.DBHandler"%>
<%@page import="com.dimata.qdep.db.DBResultSet"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayComponent"%>
<%@page import="com.dimata.harisma.entity.payroll.PayComponent"%>
<%@page import="com.dimata.harisma.entity.log.ChangeValue"%>
<%@page import="com.dimata.harisma.entity.payroll.PayConfigPotongan"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayConfigPotongan"%>
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_PROCESS, AppObjInfo.OBJ_PAYROLL_PROCESS_PRINT);%>
<%@ include file = "../../main/checkuser.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    String[] statusValue = {"Non-Active", "Active"};
    
    public String getComponentName(long componentId){
        String str = "";
        try {
            PayComponent payComp = PstPayComponent.fetchExc(componentId);
            str = payComp.getCompName();
        } catch(Exception e){
            System.out.println(e.toString());
        }
        return str;
    }

    public void getUpdateValidStatus(int status, long oid) {
        DBResultSet dbrs = null;
        try {
            String sql = "UPDATE pay_config_potongan SET valid_status="+status+" ";
            sql += " WHERE potongan_kredit_id="+oid;
            DBHandler.updateParsial(sql);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            DBResultSet.close(dbrs);
        }
    }
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    String srcNrk = FRMQueryString.requestString(request, "src_nrk");
    String srcName = FRMQueryString.requestString(request, "src_name");
    long srcDivision = FRMQueryString.requestLong(request, "src_division");
    long srcComponent = FRMQueryString.requestLong(request, "src_component");
    long srcCompany = FRMQueryString.requestLong(request, "src_company");
    long srcDepartment = FRMQueryString.requestLong(request, "src_department");
    long srcSection = FRMQueryString.requestLong(request, "src_section");
    long srcPayGroup = FRMQueryString.requestLong(request, "src_paygroup");
    int statusActive = FRMQueryString.requestInt(request, "src_status");
    long oid = FRMQueryString.requestLong(request, "oid");
    /* get data form */
    long empId = FRMQueryString.requestLong(request, "emp_id");
    long compId = FRMQueryString.requestLong(request, "comp_id");
    String startDate = FRMQueryString.requestString(request, "start_date");
    String endDate = FRMQueryString.requestString(request, "end_date");
    double angsuran = FRMQueryString.requestDouble(request, "angsuran");
    String rekening = FRMQueryString.requestString(request, "rekening");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    if (iCommand == Command.SAVE){
        PayConfigPotongan configPot = new PayConfigPotongan();
        configPot.setEmployeeId(empId);
        configPot.setComponentId(compId);
        Date dStart = sdf.parse(startDate);
        Date dEnd = sdf.parse(endDate);
        configPot.setStartDate(dStart);
        configPot.setEndDate(dEnd);
        configPot.setAngsuranPerbulan(angsuran);
        configPot.setNoRekening(rekening);
        configPot.setValidStatus(1);
        if (oid != 0){
            configPot.setOID(oid);
            try {
                oid = PstPayConfigPotongan.updateExc(configPot);
            } catch(Exception e){
                System.out.println(e.toString());
            }
        } else {
            try {
                oid = PstPayConfigPotongan.insertExc(configPot);
            } catch(Exception e){
                System.out.println(e.toString());
            }
        }
    }
    
    /* get data user login*/
    long employeeId = emplx.getOID();
    long divisionId = 0;
    long sdmDivisionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_SDM_DIVISION)));
    String clientName = PstSystemProperty.getValueByName("CLIENT_NAME");
    if (emplx.getDivisionId() == sdmDivisionOid){
        divisionId = 0;
    } else {
        divisionId = emplx.getDivisionId();
    }
    
    String whereClause = PstDivision.fieldNames[PstDivision.FLD_VALID_STATUS]+"="+PstDivision.VALID_ACTIVE
            + " AND "+PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+"="+srcCompany;
    if (divisionId != 0){
        whereClause += " AND "+PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID]+"="+divisionId;
    }
    
    Vector divisionList = PstDivision.list(0, 0, whereClause, "");
    ChangeValue changeValue = new ChangeValue();
    whereClause = "";
    /* pencarian where default utk potongan */
    if (divisionId != 0){
        whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+"="+divisionId;
        Vector empList = PstEmployee.list(0, 0, whereClause, "");
        String employeeIds = "";
        if (empList != null && empList.size()>0){
            if (empList.size() > 1){
                for (int e=0; e<empList.size(); e++){
                    Employee emp = (Employee)empList.get(e);
                    employeeIds += emp.getOID()+",";
                }
                employeeIds = " IN (" + employeeIds.substring(0, employeeIds.length()-1)+")";
            } else {
                Employee emp = (Employee)empList.get(0);
                employeeIds = "="+ emp.getOID();
            }
        }
        whereClause = PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_EMPLOYEE_ID]+" "+employeeIds;
    }
    
    if (iCommand == Command.ACTIVATE){
        getUpdateValidStatus(1, oid);
    }
    
    if (iCommand == Command.DELETE){
        getUpdateValidStatus(0, oid);
    }
    
    if (iCommand == Command.CONFIRM){
        if (oid != 0){
            try {
                PstPayConfigPotongan.deleteExc(oid);
            } catch(Exception e){
                System.out.println(e.toString());
            }
        }
    }
    
    if (iCommand == Command.LIST || iCommand == Command.CLOSE){
        
        if (srcNrk != null && srcNrk.length()>0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_EMPLOYEE_NUM]+"='"+srcNrk+"'";
        }
        if (srcName != null && srcName.length()>0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_FULL_NAME]+" LIKE '%"+srcName+"%'";
        }
        
        if (srcCompany != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_COMPANY_ID]+"="+srcCompany;
        }
        
        if (srcDivision != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DIVISION_ID]+"="+srcDivision;
        }

        if (srcDepartment != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_DEPARTMENT_ID]+"="+srcDepartment;
        }
        
        if (srcSection != 0){
            whereClause = PstEmployee.fieldNames[PstEmployee.FLD_SECTION_ID]+"="+srcSection;
        }
        
        if(srcPayGroup != 0){
            if (whereClause.equals("")){
                whereClause += PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+"="+srcPayGroup;                
            } else {
                whereClause += " AND " +PstEmployee.fieldNames[PstEmployee.FLD_PAYROLL_GROUP]+"="+srcPayGroup;                
            }
        }
        
        
                      
        Vector empList = PstEmployee.list(0, 0, whereClause, "");
        String employeeIds = "";
        if (empList != null && empList.size()>0){
            if (empList.size() > 1){
                for (int e=0; e<empList.size(); e++){
                    Employee emp = (Employee)empList.get(e);
                    employeeIds += emp.getOID()+",";
                }
                employeeIds = " IN (" + employeeIds.substring(0, employeeIds.length()-1)+")";
            } else {
                Employee emp = (Employee)empList.get(0);
                employeeIds = "="+ emp.getOID();
            }
        }
        
        whereClause = PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_EMPLOYEE_ID]+" "+employeeIds;
        
        if (srcComponent != 0){
            whereClause = whereClause + " AND " + PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_COMPONENT_ID]+ " = "+srcComponent;
        }
        
        whereClause += " AND " + PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_VALID_STATUS]+"='"+statusActive+"'";
        
    }
    
    if (iCommand == Command.CANCEL || iCommand == Command.EDIT || iCommand == Command.SAVE){
        if (oid != 0){
            whereClause = PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_POTONGAN_KREDIT_ID]+"="+oid;
        }
        
    }
    
    if (iCommand == Command.CLOSE){
        Date dt = new Date();
        String whereClauseClose = whereClause + " AND "+PstPayConfigPotongan.fieldNames[PstPayConfigPotongan.FLD_END_DATE]+" < '"+Formater.formatDate(dt, "yyyy-MM-dd")+"'";
        Vector payConfigListPotongan = PstPayConfigPotongan.list(0, 0, whereClauseClose, "");
        if (payConfigListPotongan.size()>0){
            for (int i=0; i < payConfigListPotongan.size(); i++){
                PayConfigPotongan potongan = (PayConfigPotongan) payConfigListPotongan.get(i);
                potongan.setValidStatus(0);
                try {
                    PstPayConfigPotongan.updateExc(potongan);
                } catch (Exception exc){}
            }
        }
    }
    
    Vector payConfigList = PstPayConfigPotongan.list(0, 0, whereClause, "");
    
    
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Konfig Potongan</title>
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <style type="text/css">
            .tblStyle {border-collapse: collapse; font-size: 12px; }
            .tblStyle td {padding: 5px 7px; border: 1px solid #CCC; font-size: 12px; }
            .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
            .title_page {color:#0db2e1; font-weight: bold; font-size: 14px; background-color: #EEE; border-left: 1px solid #0099FF; padding: 12px 18px;}
            
            .tblForm td{
                padding: 5px 7px; 
                font-size: 12px;
                font-weight: bold;
            }
            
            body {color:#373737;}
            #menu_utama {padding: 9px 14px; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
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
                padding: 21px 11px;
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
                padding: 9px 11px;
                margin: 5px 11px 5px 0px;
                background-color: #F5F5F5;
                border:1px solid #DDD;
                border-radius: 4px;
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
            .box-info-debit {
                padding:12px;
                background-color: #C0ECFA; 
                color: #38839C;
                margin: 5px 0px;
                margin-top: 0px;
                text-align: center;
                font-size: 16px;
                border-radius: 5px;
                cursor: pointer;
            }
            .box-info-debit:hover {
                color:#FFF;
                background-color: #85C3D6;
            }
            .box-info-credit {
                padding:12px;
                background-color: #E2FAC5; 
                color: #5A8C1D;
                margin: 5px 0px;
                margin-top: 0px;
                text-align: center;
                font-size: 16px;
                border-radius: 5px;
                cursor: pointer;
            }
            .box-info-credit:hover {
                color:#FFF;
                background-color: #B0CF53;
            }
            #box-item {
                font-weight: bold;
                margin: 3px 0px;
                padding: 5px 9px;
                color: #575757;
                background-color: #EEE;
                border:1px solid #DDD;
                border-right: none;
            }
            #box-times {
                font-weight: bold;
                margin: 3px 0px;
                padding: 5px 9px;
                color: #B09595;
                background-color: #EEE;
                border:1px solid #DDD;
                cursor: pointer;
            }
            #box-times:hover {
                font-weight: bold;
                margin: 3px 0px;
                padding: 5px 9px;
                color: #B09595;
                background-color: #FFD9D9;
                border:1px solid #D9B8B8;
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
            .btn {
                background-color: #00a1ec;
                border-radius: 3px;
                font-family: Arial;
                color: #EEE;
                font-size: 12px;
                padding: 7px 11px 7px 11px;
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
            
            .btn-small-1 {
                font-family: sans-serif;
                text-decoration: none;
                padding: 5px 7px; 
                background-color: #EEE; color: #575757; 
                font-size: 11px; cursor: pointer;
                border-radius: 3px;
            }
            .btn-small-1:hover { background-color: #DDD; color: #474747;}
            
            .tbl-main {border-collapse: collapse; font-size: 11px; background-color: #FFF; margin: 0px;}
            .tbl-main td {padding: 4px 7px; border: 1px solid #DDD; }
            #tbl-title {font-weight: bold; background-color: #F5F5F5; color: #575757;}
            
            .tbl-small {border-collapse: collapse; font-size: 11px; background-color: #FFF;}
            .tbl-small td {padding: 2px 3px; border: 1px solid #DDD; }
            
            .caption {
                font-size: 12px;
                color: #474747;
                font-weight: bold;
                padding: 2px 0px 3px 0px;
            }
            .divinput {
                margin-bottom: 5px;
                padding-bottom: 5px;
            }
            #debit_value {
                font-size: 12px;
                color: #38839C;
                background-color: #C0ECFA;
                font-weight: bold;
                border-radius: 3px;
                padding: 9px;
            }
            #credit_value {
                font-size: 12px;
                background-color: #E2FAC5; 
                color: #5A8C1D;
                font-weight: bold;
                border-radius: 3px;
                padding: 9px;
            }
            .mydate {
                font-weight: bold;
                color: #474747;
            }
            
            #level_select {
                margin-bottom: 5px;
                padding-bottom: 5px;
                visibility: hidden;
            }
            
            #category_select {
                margin-bottom: 5px;
                padding-bottom: 5px;
                visibility: hidden;
            }
            
            #position_select {
                margin-bottom: 5px;
                padding-bottom: 5px;
                visibility: hidden;
            }
            
            #div_item_sch {
                background-color: #EEE;
                color: #575757;
                padding: 5px 7px;
            }
            
            #record_count{
                font-size: 12px;
                font-weight: bold;
                padding-bottom: 9px;
            }
            #box-form {
                background-color: #EEE; 
                border-radius: 5px;
            }
            .form-style {
                font-size: 12px;
                color: #575757;
                border: 1px solid #DDD;
                border-radius: 5px;
            }
            .form-title {
                color:#474747;
                padding: 11px 21px;
                text-align: left;
                background-color: #CCC;
                border-top-left-radius: 5px;
                border-top-right-radius: 5px;
                font-weight: bold;
            }
            .form-content {
                text-align: left;
                padding: 21px;
                background-color: #EEE;
            }
            .form-footer {
                padding: 21px;
                background-color: #CCC;
                border-bottom-left-radius: 5px;
                border-bottom-right-radius: 5px;
            }
            #confirm {
                padding: 5px 7px;
                background-color: #FF6666;
                color: #FFF;
                border-radius: 3px;
                font-size: 12px;
                font-weight: bold;
                margin-bottom: 5px;
            }
            #btn-confirm {
                padding: 3px 5px; border-radius: 2px;
                background-color: #CF5353; color: #FFF; 
                font-size: 11px; cursor: pointer;
            }
            .footer-page {
                font-size: 12px; 
            }
            h2 {
                padding: 7px 0px 21px 0px;
                margin: 0px 0px 21px 0px;
                border-bottom: 1px solid #DDD;
            }
            .delete {
                background-color: #FAD7DB;
                border-radius: 3px;
                color: #BA5B66;
                font-weight: bold;
                padding: 11px;
            }
            .form-dialong {
                background-color: #FFF;
                padding: 19px;
                border: 1px solid #CCC;
            }
            #delete {
                color:#CCC;
            }
            #delete:hover {
                color:#CF5353;
            }
            #edit {
                color:#CCC;
            }
            #edit:hover {
                color:#004fba;
            }
            
            .delete_msg{
                font-size: 12px;
                padding: 14px;
                background-color: #FAD7DB;
                color: #CF5353;
                margin: 8px 0px;
            }
        </style>
        <link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css">
        <script src="<%=approot%>/javascripts/jquery.js"></script>
        <script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>
<script type="text/javascript">
    function pageLoad(){
        $(".mydate").datepicker({ dateFormat: "yy-mm-dd" });
    }
    function cmdSearch(){
        document.frm.command.value="<%= Command.LIST %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdExport(){
        document.frm.command.value="<%= Command.LIST %>";
        document.frm.action="excel/config_potongan_excel.jsp";
        document.frm.submit();
    }
    function cmdAdd(){
        document.frm.oid.value=0;
        document.frm.command.value="<%= Command.ADD %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdEdit(oid){
        document.frm.oid.value=oid;
        document.frm.command.value="<%= Command.EDIT %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdSave(){
        document.frm.command.value="<%= Command.SAVE %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdDelete(oid){
        document.frm.oid.value=oid;
        document.frm.command.value="<%= Command.CANCEL %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdYa(oid){
        document.frm.oid.value=oid;
        document.frm.command.value="<%= Command.CONFIRM %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdBatal(){
        document.frm.command.value="<%= Command.NONE %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdActive(oid){
        document.frm.command.value="<%= Command.ACTIVATE %>";
        document.frm.oid.value=oid;
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdNonActive(oid){
        document.frm.command.value="<%= Command.DELETE %>";
        document.frm.oid.value=oid;
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdNonActiveAll(){
        document.frm.command.value="<%= Command.CLOSE %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }
    function cmdBack(){
        document.frm.action="pay-pre-data.jsp";
        document.frm.submit();
    }
    function cmdGoToImport(){
        document.frm.action="import_potongan.jsp";
        document.frm.submit();
    }
    function cmdDownload(){
        document.frm.target="_blank";
        document.frm.action="sample_potongan_kredit.xls";
        document.frm.submit();
    }
    function cmdBrowseEmp(){
        newWindow=window.open("emp_list.jsp","EmpList", "height=600,width=500,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
        newWindow.focus();
    }
    function cmdBrowseComp(){
        newWindow=window.open("potongan_list.jsp","PotonganList", "height=600,width=500,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
        newWindow.focus();
    }
     function cmdReload(){
        document.frm.command.value="<%=Command.GOTO %>";
        document.frm.action="config_potongan.jsp";
        document.frm.submit();
    }    
</script>
    </head>
    <body onload="pageLoad()">
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
            <span id="menu_title"> Konfigurasi Potongan Kredit</span>
        </div>
        <div class="content-main">
            <form name="frm" method="post" action="" >
                <input type="hidden" name="command" value="" />
                <input type="hidden" name="oid" value="<%=oid%>" />
                <div class="caption">NRK</div>
                <div class="divinput"><input type="text" name="src_nrk" value="<%= srcNrk %>" /></div>
                <div class="caption">Nama</div>
                <div class="divinput"><input type="text" name="src_name" size="50" value="<%= srcName %>" /></div>
                <div class="caption">Perusahaan</div>
                <div class="divinput">
                    <select name="src_company" onChange="javascript:cmdReload()">
                        <option value="0">-select-</option>
                        <%
                            String whereCompany="";
                            if (divisionId != 0){
                                whereCompany= PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID]+" = "+emplx.getCompanyId();
                            }
                            Vector listCompany = PstCompany.list(0, 0, whereCompany, "");
                            if (listCompany != null && listCompany.size()>0){
                                for(int i=0; i<listCompany.size(); i++){
                                    Company company = (Company)listCompany.get(i);
                                    if (company.getOID() == srcCompany){
                                        %>
                                        <option selected="selected" value="<%= company.getOID() %>"><%= company.getCompany() %></option>
                                        <%
                                    } else {
                                        %>
                                        <option value="<%= company.getOID() %>"><%= company.getCompany() %></option>
                                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="caption">Satuan Kerja</div>
                <div class="divinput">
                    <select name="src_division" onChange="javascript:cmdReload()">
                        <option value="0">-select-</option>
                        <%
                            if (divisionList != null && divisionList.size()>0){
                                for(int i=0; i<divisionList.size(); i++){
                                    Division divisi = (Division)divisionList.get(i);
                                    if (divisi.getOID() == srcDivision){
                                        %>
                                        <option selected="selected" value="<%= divisi.getOID() %>"><%= divisi.getDivision() %></option>
                                        <%
                                    } else {
                                        %>
                                        <option value="<%= divisi.getOID() %>"><%= divisi.getDivision() %></option>
                                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="caption">Unit</div>
                <div class="divinput">
                    <select name="src_department" onChange="javascript:cmdReload()">
                        <option value="0">-select-</option>
                        <%
                            String whereDepart = "hr_department."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID]+ " = "+srcDivision;
                            Vector listDepart = PstDepartment.list(0, 0, whereDepart, "");
                            if (listDepart != null && listDepart.size()>0){
                                for(int i=0; i<listDepart.size(); i++){
                                    Department dept = (Department)listDepart.get(i);
                                    if (dept.getOID() == srcDepartment){
                                        %>
                                        <option selected="selected" value="<%= dept.getOID() %>"><%= dept.getDepartment() %></option>
                                        <%
                                    } else {
                                        %>
                                        <option value="<%= dept.getOID() %>"><%= dept.getDepartment() %></option>
                                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div> 
                <div class="caption">Sub Unit</div>
                <div class="divinput">
                    <select name="src_section" onChange="javascript:cmdReload()">
                        <option value="0">-select-</option>
                        <%
                            String whereSection = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+ " = "+srcDepartment;
                            Vector listSection = PstSection.list(0, 0, whereSection, "");
                            if (listSection != null && listSection.size()>0){
                                for(int i=0; i<listSection.size(); i++){
                                    Section sec = (Section)listSection.get(i);
                                    if (sec.getOID() == srcSection){
                                        %>
                                        <option selected="selected" value="<%= sec.getOID() %>"><%= sec.getSection() %></option>
                                        <%
                                    } else {
                                        %>
                                        <option value="<%= sec.getOID() %>"><%= sec.getSection() %></option>
                                        <%
                                    }
                                }
                            }
                        %>
                    </select>
                </div>
                <div class="caption">Payroll Group</div>
                <div class="divinput">
                    <select name="src_paygroup">
                        <option value="0">-select-</option>
                        <%
                            Vector listpayGroup = PstPayrollGroup.list(0, 0, "", "");
                            if (listpayGroup != null && listpayGroup.size()>0){
                                for(int i=0; i<listpayGroup.size(); i++){
                                    PayrollGroup payrollGroup = (PayrollGroup)listpayGroup.get(i);
                                    if (payrollGroup.getOID() == srcPayGroup){
                                        %>
                                        <option selected="selected" value="<%= payrollGroup.getOID() %>"><%= payrollGroup.getPayrollGroupName() %></option>
                                        <%
                                    } else {
                                        %>
                                        <option value="<%= payrollGroup.getOID() %>"><%= payrollGroup.getPayrollGroupName() %></option>
                                        <%
                                    }
                                }
                            }
                        %>
                        
                    </select>
                </div>    
                <div class="caption">Komponen</div>
                <div class="divinput">
                    <select name="src_component">
                        <option value="0">-select-</option>
                        <%
                            String whereComp = PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_TYPE] + " = 2";
                            String order = PstPayComponent.fieldNames[PstPayComponent.FLD_COMP_NAME];
                            Vector listComp = PstPayComponent.list(0, 0, whereComp, order);
                            if (listComp != null && listComp.size()>0){
                                for(int i=0; i<listComp.size(); i++){
                                    PayComponent payComp = (PayComponent)listComp.get(i);
                                    if (payComp.getOID() == srcComponent){
                                        %>
                                        <option selected="selected" value="<%= payComp.getOID() %>"><%= payComp.getCompName() %></option>
                                        <%
                                    } else {
                                        %>
                                        <option value="<%= payComp.getOID() %>"><%= payComp.getCompName() %></option>
                                        <%
                                    }
                                }
                            }
                        %>
                        
                    </select>
                </div>
                <div class="caption">Status</div>
                <div class="divinput">
                    <select name="src_status">
                        <option value="1" <%=(iCommand == Command.LIST && statusActive == 1 ? "Selected" : "")%>>Active</option>
                        <option value="0" <%=(iCommand == Command.LIST && statusActive == 0 ? "Selected" : "")%>>Non-Active</option>
                    </select>
                </div>        
                <div class="divinput"><a style="color:#FFF" href="javascript:cmdSearch()" class="btn">Search</a></div>
                <div>&nbsp;</div>
                <a style="color:#FFF;" class="btn" href="javascript:cmdAdd()">Tambah Data</a>
                <a style="color:#FFF;" class="btn" href="javascript:cmdGoToImport()">Import Data from Excel</a>
                <a style="color:#FFF;" class="btn" href="javascript:cmdDownload()">Download contoh file</a>
                <a style="color:#FFF;" class="btn" href="javascript:cmdExport()">Export to Excel</a>
                <a style="color:#FFF;" class="btn" href="javascript:cmdNonActiveAll()">Non Active All Past Date</a>
                <div>&nbsp;</div>
                <% if (iCommand == Command.ADD || iCommand == Command.EDIT){ 
                    PayConfigPotongan payConfigPotongan = new PayConfigPotongan();
                    Employee emp = new Employee();
                    PayComponent payComp = new PayComponent();
                    if (oid != 0){
                       try {
                           payConfigPotongan = PstPayConfigPotongan.fetchExc(oid);
                       } catch (Exception exc){
                           System.out.println(exc.toString());
                       }
                       if (payConfigPotongan != null){
                           try {
                               emp = PstEmployee.fetchExc(payConfigPotongan.getEmployeeId());
                           } catch (Exception exc){
                               System.out.println(exc.toString());
                           }
                           
                           try {
                               payComp = PstPayComponent.fetchExc(payConfigPotongan.getComponentId());
                           } catch (Exception exc){
                               System.out.println(exc.toString());
                           }
                           
                       }
                       
                    }
                    
                %>
                <div class="form-dialong">
                    <div style="border-bottom: 1px solid #DDD; padding-bottom: 19px; margin-bottom: 16px">
                        Form Tambah Data
                    </div>
                    <table class="tblForm">
                        <tr>
                            <td>Data Karyawan</td>
                            <td>
                                <a href="javascript:cmdBrowseEmp()" class="btn-small" style="color:#FFF">Pilih Karyawan</a>
                                <input type="hidden" id="emp_data" name="emp_id" value="<%=payConfigPotongan.getEmployeeId()%>">
                                <span id="emp-span"><%=(emp.getFullName() != null ? emp.getFullName() : "")%></span>
                            </td>
                        </tr>
                        <tr>
                            <td>Tanggal Berlaku</td>
                            <td><input type="text" name="start_date" class="mydate" value="<%=payConfigPotongan.getStartDate()%>" /></td>
                        </tr>
                        <tr>
                            <td>Tanggal Berhenti</td>
                            <td><input type="text" name="end_date" class="mydate" value="<%=payConfigPotongan.getEndDate()%>" /></td>
                        </tr>
                        <tr>
                            <td>Komponen Potongan</td>
                            <td>
                                <a href="javascript:cmdBrowseComp()" class="btn-small" style="color:#FFF">Pilih Komponen</a>
                                <input type="hidden" id="comp_data" name="comp_id" value="<%=payConfigPotongan.getComponentId()%>">
                                <span id="comp-span"><%=(payComp.getCompName() != null ? payComp.getCompName() : "")%></span>
                            </td>
                        </tr>
                        <tr>
                            <td>Angsuran Perbulan</td>
                            <td><input type="text" name="angsuran" value="<%=payConfigPotongan.getAngsuranPerbulan()%>"/></td>
                        </tr>
                        <tr>
                            <td>No. Rekening</td>
                            <td><input type="text" name="rekening" value="<%=payConfigPotongan.getNoRekening()%>" /></td>
                        </tr>
                    </table>
                    <div style="border-top: 1px solid #DDD; padding-top: 19px; margin-top: 16px">
                        <a href="javascript:cmdSave()" class="btn" style="color:#FFF">Simpan</a>
                        <a href="javascript:cmdBatal()" class="btn" style="color:#FFF">Batal</a>
                    </div>
                </div>
                <div>&nbsp;</div>
                <% } %>
                <%
                    if (iCommand == Command.CANCEL){
                        %>
                        <div class="delete_msg">
                            Apakah yakin akan menghapus data ini? <a href="javascript:cmdYa('<%= oid %>')">Ya</a> / <a href="javascript:cmdBatal()">Tidak</a>
                        </div>
                        <%
                    }
    
                    if ((iCommand == Command.LIST || iCommand == Command.EDIT || iCommand == Command.CANCEL || iCommand == Command.SAVE || iCommand == Command.CLOSE) && payConfigList.size()>0 ){
                %>
                <table class="tblStyle">
                    <tr>
                        <td class="title_tbl">No</td>
                        <td class="title_tbl"><%=(clientName.equals("BPD") ? "NRK" : "Payroll")%></td>
                        <td class="title_tbl">Nama</td>
                        <td class="title_tbl">Satuan Kerja</td>
                        <td class="title_tbl">Tanggal Berlaku</td>
                        <td class="title_tbl">Tanggal Berhenti</td>
                        <td class="title_tbl">Komponen Potongan</td>
                        <td class="title_tbl">Angsuran Perbulan</td>
                        <td class="title_tbl">No. Rekening</td>
                        <td class="title_tbl">Status</td>
                    </tr>
                    <%
                        if (payConfigList != null && payConfigList.size()>0){
                            double totalAngsuran = 0.0;
                            for(int i=0; i<payConfigList.size(); i++){
                                PayConfigPotongan payConfig = (PayConfigPotongan)payConfigList.get(i);
                                String nrk = "";
                                String nama = "";
                                String divisi = "";
                                try {
                                    Employee emp = PstEmployee.fetchExc(payConfig.getEmployeeId());
                                    nrk = emp.getEmployeeNum();
                                    nama = emp.getFullName();
                                    divisi = changeValue.getDivisionName(emp.getDivisionId());
                                } catch(Exception e){
                                    System.out.println(e.toString());
                                }
                                totalAngsuran = totalAngsuran + payConfig.getAngsuranPerbulan();
                    %>
                    <tr>
                        <td><%= (i+1) %></td>
                        <td><%= nrk %></td>
                        <td><%= nama %></td>
                        <td><%= divisi %></td>
                        <td><%= payConfig.getStartDate() %></td>
                        <td><%= payConfig.getEndDate() %></td>
                        <td><%= getComponentName(payConfig.getComponentId()) %></td>
                        <td><%= Formater.formatNumberMataUang(payConfig.getAngsuranPerbulan(), "Rp")  %></td>
                        <td><%= payConfig.getNoRekening() %></td>
                        <td>
                            <%
                                int nilai = payConfig.getValidStatus();
                                String active = "#DDD";
                                String nonActive = "";
                                if (nilai == 0){
                                    active = "#CCCCCC";
                                    nonActive = "#c90a0a";
                                } else {
                                    active = "#69c90a";
                                    nonActive = "#CCCCCC";
                                }
                            %>
                            <a style="color:<%= active %>" href="javascript:cmdActive('<%= payConfig.getOID() %>')">Active</a>
                            <a style="color:<%= nonActive %>" href="javascript:cmdNonActive('<%= payConfig.getOID() %>')">Non-Active</a>
                            <a id="edit" href="javascript:cmdEdit('<%= payConfig.getOID() %>')">Edit</a>
                            <a id="delete" href="javascript:cmdDelete('<%= payConfig.getOID() %>')">Delete</a>
                        </td>
                    </tr>
                    <%
                            }
                    %>
                    <tr>
                        <td colspan="7" rowspan="1" style="text-align: right;"><strong>Total</strong></td>
                        <td><%= Formater.formatNumberMataUang(totalAngsuran, "Rp")  %></td>
                        <td></td>
                        <td></td>
                    </tr>
                    
                    <%
                            
                        }
                    %>
                </table>
                <%
                    }
                %>
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
