<%-- 
    Document   : employee_edit_new
    Created on : May 5, 2015, 2:46:02 PM
    Author     : Dimata 007
--%>
<%@page import="com.dimata.harisma.form.masterdata.FrmDistrict"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="usu.util.StringUtil"%>
<%@page import="com.dimata.harisma.entity.recruitment.PstRecrApplication"%>
<%@page import="com.dimata.harisma.entity.recruitment.RecrApplication"%>
<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.util.lang.I_Dictionary"%>
<%@page import="com.dimata.harisma.entity.clinic.MedicalType"%>
<%@page import="com.dimata.harisma.entity.clinic.MedExpenseType"%>
<%@page import="com.dimata.harisma.entity.clinic.PstMedExpenseType"%>
<%@page import="com.dimata.harisma.entity.clinic.PstMedicalType"%>
<%@page import="com.dimata.harisma.entity.masterdata.location.PstLocation"%>
<%@page import="com.dimata.harisma.entity.attendance.I_Atendance"%>

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
<%@ page import = "com.dimata.harisma.form.locker.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.location.Location" %>
<%@ page import = "com.dimata.harisma.entity.locker.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.session.employee.SessEmployeePicture" %>
<%@page import = "com.dimata.harisma.form.masterdata.FrmKecamatan" %>
<!--%@ page import="javax.servlet.http.HttpUtils.*" %-->

<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK);%>
<% boolean privGenerate = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_GENERATE_SALARY_LEVEL));%>
<%@ include file = "../../main/checkuser.jsp" %>
<%
    /* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
%>

<%    CtrlEmployee ctrlEmployee = new CtrlEmployee(request);
    long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    int hidden_command = FRMQueryString.requestInt(request, "hidden_flag_cmd");
    
    long hidden_recr_application_id = FRMQueryString.requestLong(request, "hidden_recr_application_id");
    I_Atendance attdConfig = null;
    try {
        attdConfig = (I_Atendance) (Class.forName(PstSystemProperty.getValueByName("ATTENDANCE_CONFIG")).newInstance());
    } catch (Exception e) {
        System.out.println("Exception : " + e.getMessage());
        System.out.println("Please contact your system administration to setup system property: ATTENDANCE_CONFIG ");
    }
    //update by satrya 2013-04-09
    int schedulePerWeek = 0;
    int recordToGet = 7;
    try {
        schedulePerWeek = Integer.parseInt(PstSystemProperty.getValueByName("ATTANDACE_DEFAULT_SCHEDULE_PER_WEEK"));
        if (schedulePerWeek != 0) {
            recordToGet = 35;
        }
    } catch (Exception ex) {
        System.out.println("Execption ATTANDACE_DEFAULT_SCHEDULE_PER_WEEK: " + ex.toString());
        schedulePerWeek = 0;
    }

    int iErrCode = FRMMessage.ERR_NONE;
    String errMsg = "";
    String whereClause = "";
    String orderClause = "";
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    
    //out.println("iCommand : "+iCommand);
    ControlLine ctrLine = new ControlLine();
    //System.out.println("iCommand = "+iCommand);
    //System.out.println("prevCommand = "+prevCommand);
    
    iErrCode = ctrlEmployee.action(iCommand, oidEmployee, request, emplx.getFullName(), appUserIdSess);
    
    /**
     * Description : save custom field
     * Date : 2015-06-12
     * Author : Hendra Putu 
     */
    int commandCustom = FRMQueryString.requestInt(request, "command_custom");
    /* input0 = text field */
    String[] input0 = FRMQueryString.requestStringValues(request, "input0");
    String[] hidden0 = FRMQueryString.requestStringValues(request, "hidden0");
    String[] dataType0 = FRMQueryString.requestStringValues(request, "data_type0");
    /* input1 = textarea */
    String[] input1 = FRMQueryString.requestStringValues(request, "input1");
    String[] hidden1 = FRMQueryString.requestStringValues(request, "hidden1");
    String[] dataType1 = FRMQueryString.requestStringValues(request, "data_type1");
    /* input2 = selection */
    String[] input2 = FRMQueryString.requestStringValues(request, "input2");
    String[] hidden2 = FRMQueryString.requestStringValues(request, "hidden2");
    String[] dataType2 = FRMQueryString.requestStringValues(request, "data_type2");
    /* input3 = multiple selection */
    String[] input3 = FRMQueryString.requestStringValues(request, "input3");
    String[] hidden3 = FRMQueryString.requestStringValues(request, "hidden3");
    String[] dataType3 = FRMQueryString.requestStringValues(request, "data_type3");
    /* input4 = datepicker */
    String[] input4 = FRMQueryString.requestStringValues(request, "input4");
    String[] hidden4 = FRMQueryString.requestStringValues(request, "hidden4");
    String[] dataType4 = FRMQueryString.requestStringValues(request, "data_type4");
    /* input5 = datepicker and time */
    String[] input5 = FRMQueryString.requestStringValues(request, "input5");
    String[] hidden5 = FRMQueryString.requestStringValues(request, "hidden5");
    String[] dataType5 = FRMQueryString.requestStringValues(request, "data_type5");
    /* input6 = check box */
    String[] input6 = FRMQueryString.requestStringValues(request, "input6");
    String[] hidden6 = FRMQueryString.requestStringValues(request, "hidden6");
    String[] dataType6 = FRMQueryString.requestStringValues(request, "data_type6");
    /* input7 = radio button */
    String[] input7 = FRMQueryString.requestStringValues(request, "input7");
    String[] hidden7 = FRMQueryString.requestStringValues(request, "hidden7");
    String[] dataType7 = FRMQueryString.requestStringValues(request, "data_type7");
    
    if (commandCustom > 0){
        /* text field*/
         if (hidden0 != null && hidden0.length > 0){
            
            for(int h=0; h<hidden0.length; h++){
                EmpCustomField empCustom = new EmpCustomField();
                empCustom.setCustomFieldId(Long.valueOf(hidden0[h]));
                Date tgl = new Date();
                empCustom.setDataDate(tgl);
                empCustom.setDataNumber(0);
                empCustom.setDataText("-");
                switch(Integer.valueOf(dataType0[h])){
                    case 0: empCustom.setDataText(input0[h]); break;
                    case 1: empCustom.setDataNumber(Double.valueOf(input0[h])); break;
                    case 2: empCustom.setDataNumber(Integer.valueOf(input0[h])); break;
                    case 3: empCustom.setDataText(input0[h]); break;
                    case 4: empCustom.setDataText(input0[h]); break;
                }
                empCustom.setEmployeeId(oidEmployee);
                // insert or update
                String whereEmpCustom = "CUSTOM_FIELD_ID="+hidden0[h]+" AND EMPLOYEE_ID="+oidEmployee;
                Vector listEmpCustom = PstEmpCustomField.list(0, 0, whereEmpCustom, "");
                if (listEmpCustom != null && listEmpCustom.size()>0){
                    long empCustomID = 0;
                    for(int e=0; e<listEmpCustom.size(); e++){
                        EmpCustomField empCust = (EmpCustomField)listEmpCustom.get(e);
                        empCustomID = empCust.getOID();
                    }
                    try {
                        empCustom.setOID(empCustomID);
                        PstEmpCustomField.updateExc(empCustom);
                    } catch(Exception ex){
                        System.out.println("insert emp custom field=>"+ex.toString());
                    }
                } else {
                    try {
                        PstEmpCustomField.insertExc(empCustom);
                    } catch(Exception ex){
                        System.out.println("insert emp custom field=>"+ex.toString());
                    }
                }
            }
            
            
        }
        /* textarea */
        if (hidden1 != null && hidden1.length > 0){
            
            for(int h=0; h<hidden1.length; h++){
                EmpCustomField empCustom = new EmpCustomField();
                empCustom.setCustomFieldId(Long.valueOf(hidden1[h]));
                Date tgl = new Date();
                empCustom.setDataDate(tgl);
                empCustom.setDataNumber(0);
                empCustom.setDataText("-");
                switch(Integer.valueOf(dataType1[h])){
                    case 0: empCustom.setDataText(input1[h]); break;
                    case 1: empCustom.setDataNumber(Double.valueOf(input1[h])); break;
                    case 2: empCustom.setDataNumber(Integer.valueOf(input1[h])); break;
                    case 3: empCustom.setDataText(input1[h]); break;
                    case 4: empCustom.setDataText(input1[h]); break;
                }
                empCustom.setEmployeeId(oidEmployee);
                // insert or update
                String whereEmpCustom = "CUSTOM_FIELD_ID="+hidden1[h]+" AND EMPLOYEE_ID="+oidEmployee;
                Vector listEmpCustom = PstEmpCustomField.list(0, 0, whereEmpCustom, "");
                if (listEmpCustom != null && listEmpCustom.size()>0){
                    long empCustomID = 0;
                    for(int e=0; e<listEmpCustom.size(); e++){
                        EmpCustomField empCust = (EmpCustomField)listEmpCustom.get(e);
                        empCustomID = empCust.getOID();
                    }
                    try {
                        empCustom.setOID(empCustomID);
                        PstEmpCustomField.updateExc(empCustom);
                    } catch(Exception ex){
                        System.out.println("insert emp custom field=>"+ex.toString());
                    }
                } else {
                    try {
                        PstEmpCustomField.insertExc(empCustom);
                    } catch(Exception ex){
                        System.out.println("insert emp custom field=>"+ex.toString());
                    }
                }
            }
            
            
        }
        /* */
        if (hidden4 != null && hidden4.length > 0){
            
            for(int h=0; h<hidden4.length; h++){
                EmpCustomField empCustom = new EmpCustomField();
                empCustom.setCustomFieldId(Long.valueOf(hidden4[h]));

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd"); 
                Date tgl = dateFormat.parse(input4[h]); 
                
                empCustom.setDataDate(tgl);
                empCustom.setDataNumber(0);
                empCustom.setDataText("-");
                empCustom.setEmployeeId(oidEmployee);
                // insert or update
                String whereEmpCustom = "CUSTOM_FIELD_ID="+hidden4[h]+" AND EMPLOYEE_ID="+oidEmployee;
                Vector listEmpCustom = PstEmpCustomField.list(0, 0, whereEmpCustom, "");
                if (listEmpCustom != null && listEmpCustom.size()>0){
                    long empCustomID = 0;
                    for(int e=0; e<listEmpCustom.size(); e++){
                        EmpCustomField empCust = (EmpCustomField)listEmpCustom.get(e);
                        empCustomID = empCust.getOID();
                    }
                    try {
                        empCustom.setOID(empCustomID);
                        PstEmpCustomField.updateExc(empCustom);
                    } catch(Exception ex){
                        System.out.println("insert emp custom field=>"+ex.toString());
                    }
                } else {
                    try {
                        PstEmpCustomField.insertExc(empCustom);
                    } catch(Exception ex){
                        System.out.println("insert emp custom field=>"+ex.toString());
                    }
                }
            }
            
            
        }
        
        
    }
    
    errMsg = ctrlEmployee.getMessage();
    FrmEmployee frmEmployee = ctrlEmployee.getForm();
    //Employee employee = ctrlEmployee.getEmployee();
    //oidEmployee = employee.getOID();
    //--------------------------------------
    //sehubungan dengan picture
    //update by satrya 2013-02-12
    Employee employee = new Employee();

    try {
        if (oidEmployee != 0) {
            employee = PstEmployee.fetchExc(oidEmployee);
        }
    } catch (Exception exc) {
        employee = new Employee();
        System.out.println("Exception employee" + exc);
    }
    ///update by satrya 2013-10-21
    if (iCommand == Command.GOTO) {
        iCommand = Command.ADD;
        frmEmployee.requestEntityObject(employee);
    }
    if (iCommand == Command.ADD) {
        //employee = new Employee();
        //frmEmployee.requestEntityObject(employee);
    }

    System.out.println("Oid Employee: " + oidEmployee);

   // if (iErrCode != 0) {
    // if (iErrCode != 0 && iCommand == Command.SAVE) {
    //update by sarya 2013-08-13
    //karena waktu save empnya tidak muncul
    if (iCommand == Command.SAVE) {
        employee = ctrlEmployee.getEmployee();
    }
         //}
    //}

    //----------------------------------------
    //locker;
    FrmLocker frmLocker = ctrlEmployee.getFormLocker();
    Locker locker = ctrlEmployee.getLocker();

//	if(((iCommand==Command.SAVE)||(iCommand==Command.DELETE))&&(frmEmployee.errorSize()<1)){
    if (iCommand == Command.DELETE) {
%>
<jsp:forward page="employee_list.jsp">
    <jsp:param name="prev_command" value="<%=prevCommand%>" />
    <jsp:param name="start" value="<%=start%>" />
    <jsp:param name="employee_oid" value="<%=employee.getOID()%>" />
</jsp:forward>
<%
    }

    //if ((iCommand == Command.SAVE) && (iErrCode == 0)) {
    //  iCommand = Command.EDIT;
    //}
    //Gede_21Nov2011
    boolean isCopy = FRMQueryString.requestBoolean(request, "hidden_copy_status");
    long gotoEmployee = FRMQueryString.requestLong(request, "hidden_goto_employee");

    long companyId = FRMQueryString.requestLong(request, FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_COMPANY_ID]);
    long divisionId = FRMQueryString.requestLong(request, FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_DIVISION_ID]);
    long departementId = FRMQueryString.requestLong(request, FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_DEPARTMENT_ID]);

    long wacompanyId = FRMQueryString.requestLong(request, FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_COMPANY_ID]);
    long wadivisionId = FRMQueryString.requestLong(request, FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_DIVISION_ID]);
    long wadepartementId = FRMQueryString.requestLong(request, FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_DEPARTMENT_ID]);

    long wasectionId = FRMQueryString.requestLong(request, FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_SECTION_ID]);

    //update by satrya 20130907
    if (iCommand == Command.GOTO) {
        employee.setCompanyId(companyId);
        employee.setDivisionId(divisionId);
        employee.setDepartmentId(departementId);
    }
    //long sectionId = FRMQueryString.requestLong(request, "hidden_companyId");
    I_Dictionary dictionaryD = userSession.getUserDictionary();
    
 
       if (hidden_recr_application_id != 0) {

                        try{
                                RecrApplication recrApplication = PstRecrApplication.fetchExc(hidden_recr_application_id);
                                employee.setFullName(recrApplication.getFullName());

                                long positionId = PstPosition.getPositionId(recrApplication.getPosition());

                                employee.setPositionId(positionId);
                                employee.setBirthDate(recrApplication.getBirthDate());
                                employee.setBirthPlace(recrApplication.getBirthPlace());
                                employee.setAddressPermanent(recrApplication.getAddress());
                                employee.setIndentCardNr(recrApplication.getIdCardNum());
                                employee.setPostalCode(recrApplication.getPostalCode());
                                employee.setAstekNum(recrApplication.getAstekNum());
                                employee.setBloodType(recrApplication.getBloodType());
                                employee.setSex(recrApplication.getSex());
                                employee.setReligionId(recrApplication.getReligionId());
                                employee.setPhone(recrApplication.getPhone());

                                employee.setMaritalId(recrApplication.getMaritalId());
                                //long oid = PstEmployee.insertExc(employee);
                        } catch (Exception e){
                        }

                } 
    
    
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <!-- #BeginTemplate "/Templates/maintab.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>HARISMA - <%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%></title>
        <script language="JavaScript">

            function cmdPrint(){
                window.open("<%=approot%>/servlet/com.dimata.harisma.report.EmployeeDetailPdf?oid=<%=oidEmployee%>");
            }

            function cmdPrintXLS(){
                window.open("<%=approot%>/servlet/com.dimata.harisma.report.EmployeeDetailXLS?oid=<%=oidEmployee%>");
            }

            function cmdGetLocker(){
                strLockerLocation = document.frm_employee.LOCKER_LOCATION.value;
                if(strLockerLocation!=0){
                    window.open("srcLocker.jsp?<%=FrmSrcLocker.fieldNames[FrmSrcLocker.FRM_FIELD_LOCATION]%>="+strLockerLocation, null, "height=500,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
                }else{
                    document.frm_employee.LOCKER_NUMBER_POS.value="";
                    document.frm_employee.<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_LOCKER_ID]%>.value="";
                }

            }
            //Gede_25Nov2011{
            function cmdSearchEmp(){
                window.open("<%=approot%>/employee/search/search.jsp?formName=frm_employee&employeeOID=<%=String.valueOf(oidEmployee)%>&empPathId=<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_HOD_EMPLOYEE_ID]%>",
                            "Search_HOD",                    "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
            }

             function editDefaultSch(){
                 window.open("<%=approot%>/employee/databank/default_schedule.jsp?employeeId=<%=employee.getOID()%>",
                            "Harisma_Edit_Default_Schedule", "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
             }


            function updateGeoAddress(){
                oidNegara    = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_COUNTRY_ID]%>.value;
                oidProvinsi  = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PROVINCE_ID]%>.value ;
                oidKabupaten = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_REGENCY_ID]%>.value ;
                oidKecamatan = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_SUBREGENCY_ID]%>.value;    
                oidDistrict = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_DISTRICT_ID]%>.value;                    
                
                window.open("<%=approot%>/employee/search/geo_address.jsp?formName=frm_employee&employee_oid=<%=String.valueOf(oidEmployee)%>&addresstype=1&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_NEGARA]%>="+oidNegara+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_PROPINSI]%>="+oidProvinsi+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KABUPATEN]%>="+oidKabupaten+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_DISTRICT_ID]%>="+oidDistrict+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KECAMATAN]%>="+oidKecamatan+"&employee=<%=(employee.getEmployeeNum() + " / " + employee.getFullName())%>",                            
                null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
            }

            function updateGeoAddressPmnt(){
                oidNegara    = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_COUNTRY_ID]%>.value;
                oidProvinsi  = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_PROVINCE_ID]%>.value ;
                oidKabupaten = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_REGENCY_ID]%>.value ;
                oidKecamatan = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_SUBREGENCY_ID]%>.value; 
                oidDistrict = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_DISTRICT_ID]%>.value;                     
                window.open("<%=approot%>/employee/search/geo_address.jsp?formName=frm_employee&employee_oid=<%=String.valueOf(oidEmployee)%>&addresstype=2&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_NEGARA]%>="+oidNegara+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_PROPINSI]%>="+oidProvinsi+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KABUPATEN]%>="+oidKabupaten+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_DISTRICT_ID]%>="+oidDistrict+"&"+
                    "<%=FrmDistrict.fieldNames[FrmDistrict.FRM_FIELD_ID_KECAMATAN]%>="+oidKecamatan+"&employee=<%=(employee.getEmployeeNum() + " / " + employee.getFullName())%>",                                                
                null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
            }

            function cmdClearSearchEmp(){
                document.frm_employee.EMP_NUMBER.value = "";
                document.frm_employee.EMP_FULLNAME.value = "";
                document.frm_employee.EMP_DEPARTMENT.value = "";
            }
            //}
            function cmdAdd(){
                document.frm_employee.command.value="<%=Command.ADD%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.employee_oid.value=0;
                document.frm_employee.submit();
            }
            function cmdCancel(){
                document.frm_employee.command.value="<%=Command.CANCEL%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }

            function cmdEdit(oid){
                document.frm_employee.command.value="<%=Command.EDIT%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }

            function cmdSave(){
                document.frm_employee.command.value="<%=Command.SAVE%>";
                document.frm_employee.action="employee_edit.jsp";
                
                document.frm_employee.submit();
            }

            function cmdSaveCustom(oid){
                document.frm_employee.command_custom.value="1";
                document.frm_employee.employee_oid.value=oid;
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }
    
            function cmdAsk(oid){
                document.frm_employee.command.value="<%=Command.ASK%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }

            function cmdConfirmDelete(oid){
                document.frm_employee.command.value="<%=Command.DELETE%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }

            function cmdBack(){
                document.frm_employee.command.value="<%=Command.FIRST%>";
                //document.frm_employee.command.value="<%=Command.LIST%>";
                document.frm_employee.action="employee_list.jsp";
                document.frm_employee.submit();
            }
            //Gede_25Nov2011{
            function cmdSearchEmp_old(){
                emp_number = document.frm_employee.EMP_NUMBER.value;
                emp_fullname = document.frm_employee.EMP_FULLNAME.value;
                emp_department = document.frm_employee.EMP_DEPARTMENT.value;
                emp_section = document.frm_employee.EMP_SECTION.value;
                emp_edit = document.frm_employee.<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_HOD_EMPLOYEE_ID]%>.value;
                window.open("empsearch.jsp?emp_number=" + emp_number + "&emp_fullname=" + emp_fullname + "&emp_department=" + emp_department + "&emp_edit=" + emp_edit + "&emp_section=" + emp_section);

            }

            function cmdClearSearchEmp_old(){
                document.frm_employee.EMP_NUMBER.value = "";
                document.frm_employee.EMP_FULLNAME.value = "";
            }

            function cmdCopyPaste(oid) {

                document.frm_employee.command.value="<%=String.valueOf(Command.SAVE)%>";
                document.frm_employee.hidden_copy_status.value = true;
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }
            
            function cmdUpdateSec(){
                document.frm_employee.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }
            
            function cmdUpdatePos(){
                document.frm_employee.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }
            
            function cmdUpdateDiv(){
                document.frm_employee.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }
             function cmdUpdateDep(){
                document.frm_employee.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frm_employee.action="employee_edit.jsp";
                document.frm_employee.submit();
            }
            
            function cmdEmpMutation(oidEmp){
                if(oidEmp==0){
                    alert("can't use Employee Mutation because you select create new employee");
                }else{
                document.frm_employee.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frm_employee.employee_oid.value="<%=oidEmployee%>";
                document.frm_employee.action="employee_mutation.jsp";
                document.frm_employee.submit();
               }
            }
            
             function cmdViewKadiv(employeeId){            
                //var linkPage = "<!--%=approot%>/employee/leave/leave_app_edit.jsp?source=presence&command=<!--%=(Command.EDIT)%-->"; 
                var linkPage = "<%=approot%>/employee/outlet/kadiv_mapping.jsp?command=<%=(Command.EDIT)%>&FRM_FIELD_EMPLOYEE_ID="+employeeId; 
                //window.open(linkPage,"Leave","height=600,width=800,status=yes,toolbar=yes,menubar=no,location=no");  			
                var newWin = window.open(linkPage,"Leave","height=700,width=990,status=yes,toolbar=yes,menubar=no,resizable=yes,scrollbars=yes,location=yes");  			
                newWin.focus();
            }
            
           function cmdGenerateSalaryLevel(){
               document.frm_employee.action="salary_level_generate.jsp";
               document.frm_employee.submit();
           }
            //}
            //-->
        </script>
        <!-- #EndEditable -->
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <!-- #BeginEditable "styles" -->
        <link rel="stylesheet" href="../../styles/main.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "stylestab" -->
        <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
        <!-- #EndEditable --> <!-- #BeginEditable "headerscript" -->
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
            var i, x, a = document.MM_sr; for (i = 0; a && i < a.length && (x = a[i]) && x.oSrc; i++) x.src = x.oSrc;
            }

            function MM_preloadImages() { //v3.0
            var d = document; if (d.images){ if (!d.MM_p) d.MM_p = new Array();
                    var i, j = d.MM_p.length, a = MM_preloadImages.arguments; for (i = 0; i < a.length; i++)
                    if (a[i].indexOf("#") != 0){ d.MM_p[j] = new Image; d.MM_p[j++].src = a[i]; }}
            }

            function MM_findObj(n, d) { //v4.0
            var p, i, x; if (!d) d = document; if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
            d = parent.frames[n.substring(p + 1)].document; n = n.substring(0, p); }
            if (!(x = d[n]) && d.all) x = d.all[n]; for (i = 0; !x && i < d.forms.length; i++) x = d.forms[i][n];
                    for (i = 0; !x && d.layers && i < d.layers.length; i++) x = MM_findObj(n, d.layers[i].document);
                    if (!x && document.getElementById) x = document.getElementById(n); return x;
            }

            function MM_swapImage() { //v3.0
            var i, j = 0, x, a = MM_swapImage.arguments; document.MM_sr = new Array; for (i = 0; i < (a.length - 2); i += 3)
                    if ((x = MM_findObj(a[i])) != null){document.MM_sr[j++] = x; if (!x.oSrc) x.oSrc = x.src; x.src = a[i + 2]; }
            }
        </SCRIPT>
        <!-- #EndEditable -->
        <style type="text/css">
            body {color:#373737; background-color: #EEE;}
            #menu_utama {padding: 9px 14px; border-left: 1px solid #0099FF; font-size: 14px; background-color: #F3F3F3;}
            #menu_title {color:#0099FF; font-size: 14px; font-weight: bold;}
            #menu_teks {color:#CCC;}
            #box_title {padding:9px; background-color: #DDD; font-weight: bold; color:#575757; margin-bottom: 7px; }
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

            .box {
                margin: 17px 7px;
                padding: 21px 23px;
                background-color: #F7F7F7;
                border: 1px solid #D5D5D5;
                border-radius: 5px;
                color:#575757;
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

        </style>
        <link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css">
        <script src="<%=approot%>/javascripts/jquery.js"></script>
        <script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
	<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>
        <script>
        function pageLoad(){ $(".mydate").datepicker({ dateFormat: "yy-mm-dd" }); } 
	</script>
    </head>
    <body onload="pageLoad()">
        <table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
            <%if (headerStyle && !verTemplate.equalsIgnoreCase("0")) {%> 
            <%@include file="../../styletemplate/template_header.jsp" %>
            <%} else {%>
            <tr>
                <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54">
                    <!-- #BeginEditable "header" -->
                    <%@ include file = "../../main/header.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td bgcolor="#9BC1FF"  ID="MAINMENU" valign="middle" height="15"> <!-- #BeginEditable "menumain" -->
                    <%@ include file = "../../main/mnmain.jsp" %>
                    <!-- #EndEditable --> </td>
            </tr>
            <tr>
                <td>
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
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
                        <% } %>
                        <tr>
                            <td valign="top" align="left">
                                           
                                <div id="menu_utama">
                                    <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> Employee Editor</span>
                                </div>
                                
                                <form name="frm_employee" method="post" action="">
                                <input type="hidden" name="command" value="">
                                <input type="hidden" name="command_custom" value="<%=commandCustom%>">
                                <input type="hidden" name="start" value="<%=start%>">

                                <!-- Gede_21Nov2011 -->
                                <input type="hidden" name="hidden_goto_employee" value="<%=String.valueOf(gotoEmployee)%>">
                                <input type="hidden" name="hidden_copy_status">
                                <input type="hidden" name="employee_oid" value="<%=employee.getOID()%>">
                                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                <input type="hidden" name="hidden_flag_cmd" value="<%=hidden_command%>">
                                <% if (oidEmployee != 0 || (employee!=null && employee.getOID()!=0) ) {
                                    //karena keika save ini tidak muncul
                                    if(oidEmployee==0 && employee!=null && employee.getOID()!=0){
                                        oidEmployee = employee.getOID();
                                    }
                                %>
                                <div class="navbar">
                                    <ul style="margin-left: 97px">
                                      <li class="active"> <%=dictionaryD.getWord(I_Dictionary.PERSONAL_DATA)%> </li>
                                      <li class=""> <a href="familymember.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER) %></a> </li>
                                      <li class=""> <a href="emplanguage.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.COMPETENCIES) %></a> </li>
                                      <li class=""> <a href="empeducation.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EDUCATION) %></a> </li>
                                      <li class=""> <a href="experience.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EXPERIENCE) %></a> </li>
                                      <li class=""> <a href="careerpath.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.CAREER_PATH) %></a> </li>
                                      <li class=""> <a href="training.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.TRAINING_ON_DATABANK)%></a></li>
                                      <li class=""> <a href="warning.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.WARNING) %></a> </li>
                                      <li class=""> <a href="reprimand.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.REPRIMAND) %></a> </li>
                                      <li class=""> <a href="award.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.AWARD) %></a> </li>
                                      <li class=""> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PICTURE) %></a> </li>
                                      <li class=""> <a href="doc_relevant.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></a> </li>
                                    </ul>
                                </div>
                                <%}%>
                                <div class="box-info">
                                    <table>
                                        <tr>
                                            <td>
                                                
                                                <%
                                                String pictPath = "";
                                                try {
                                                    SessEmployeePicture sessEmployeePicture = new SessEmployeePicture();
                                                    pictPath = sessEmployeePicture.fetchImageEmployee(employee.getOID());

                                                } catch (Exception e) {
                                                    System.out.println("err." + e.toString());
                                                }%> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink">
                                                <%
                                                     if (pictPath != null && pictPath.length() > 0) {
                                                        out.println("<img height=\"135\" id=\"photo\" title=\"Click here to upload\"  src=\"" + approot + "/" + pictPath + "\">");
                                                     } else {
                                                %>
                                                <img width="135" height="135" id="photo" src="<%=approot%>/imgcache/no-img.jpg" />
                                                <%

                                                    }
                                                %> </a>
                                                
                                                
                                            </td>
                                            <td style="padding-left: 15px;">
                                                <div id="title-info-name"><%=employee.getFullName()%> [<%=employee.getEmployeeNum()%>]</div>
                                                <div id="title-info-desc"><%=PstEmployee.getCompanyStructureName(employee.getOID())%></div>
                                            </td>
                                        </tr>
                                    </table>
                                </div>
                                
                                <table align="center">
                                    <tr>
                                        <td valign="top"> <!-- Left Side -->
                                            <div class="box">
                                                <div id="box_title">Basic Information</div>
                                                <div style="color:#0099FF;font-style: italic;">*) entry required</div>
                                                <table>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.PAYROLL)%></th>
                                                        <td valign="middle">
                                                            <%
                                                                                                                                    
                                                            //long propConfigAutoEmpNum = 1;
                                                             //   try {
                                                             //        propConfigAutoEmpNum = Long.parseLong(PstSystemProperty.getValueByName("AUTO_GENERATE_EMPLOYEE_NUMBER"));
                                                             //   } catch (Exception ex) {
                                                             //       System.out.println("Execption AUTO_GENERATE_EMPLOYEE_NUMBER " + ex);
                                                             //   }
                                                         
                                                                if(attdConfig != null && attdConfig.getConfigurasiInputEmpNum() == I_Atendance.CONFIGURATION_II_GENERATE_AUTOMATIC_EMPLOYEE_NUMBER){
                                                                 %> 
                                                                 <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_EMPLOYEE_NUM]%>" value="<%=employee.getEmployeeNum()==null || employee.getEmployeeNum().length()==0?"automatic":employee.getEmployeeNum()%>" class="formElemen" readonly="readonly">
                                                                 <%}else{%>
                                                                 <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_EMPLOYEE_NUM]%>" value="<%=employee.getEmployeeNum()%>" class="formElemen">
                                                                 <%}%>
                                                      
                                                            * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_EMPLOYEE_NUM)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.FULL_NAME)%></th>
                                                        <td valign="middle">
                                                        <input type="text" size="45" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_FULL_NAME]%>" value="<%=employee.getFullName()%>" class="formElemen">
                                                        * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_FULL_NAME)%>        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"> <%=dictionaryD.getWord(I_Dictionary.ADDRESS)%></th>
                                                        <td valign="middle">
                                                            <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDRESS]%>" size="50" value="<%=employee.getAddress()%>" class="formElemen"><br />
                                                        <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_ADDRESS)%> 
                                                        <input class="formElemen"  type="text" name="geo_address" readonly="true" value="<%=employee.getGeoAddress()%>" size="50" onClick="javascript:updateGeoAddress()" >

                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_COUNTRY_ID]%>" value="<%="" + employee.getAddrCountryId()%>" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PROVINCE_ID]%>" value="<%="" + employee.getAddrProvinceId()%>" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_REGENCY_ID]%>" value="<%="" + employee.getAddrRegencyId()%>" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_SUBREGENCY_ID]%>" value="<%="" + employee.getAddrSubRegencyId()%>" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_DISTRICT_ID]%>" value="<%="" + employee.getAddrDistrictId()%>" >
                                                                                                                                            
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.PERMANENT_ADDRESS)%></th>
                                                        <td valign="middle">
                                                        <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDRESS_PERMANENT]%>" size="50" value="<%=(employee.getAddressPermanent() != null ? employee.getAddressPermanent() : "")%>" class="formElemen"><br />
                                                        <input class="formElemen"  type="text" name="geo_address_pmnt" readonly="true" size="50" value="<%=employee.getGeoAddressPmnt()%>" onClick="javascript:updateGeoAddressPmnt()"  >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_COUNTRY_ID]%>" value="<%="" + employee.getAddrPmntCountryId()%>" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_PROVINCE_ID]%>" value="<%="" + employee.getAddrPmntProvinceId()%>" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_REGENCY_ID]%>" value="<%="" + employee.getAddrPmntRegencyId()%>" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_SUBREGENCY_ID]%>" value="<%="" + employee.getAddrPmntSubRegencyId()%>" >        
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDR_PMNT_DISTRICT_ID]%>" value="<%="" + employee.getAddrPmntDistrictId()%>" >        
                                                        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">ID Card Address</th>
                                                        <td valign="middle">
                                                        <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDRESS_ID_CARD]%>" size="50" value="<%=employee.getAddressIdCard() == null ? "" : "" + employee.getAddressIdCard()%>" class="formElemen">        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.ZIP_CODE)%></th>
                                                        <td valign="middle">
                                                        <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_POSTAL_CODE]%>" value="<%=employee.getPostalCode() == 0 ? "" : "" + employee.getPostalCode()%>" class="formElemen">        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Telephone / HP</th>
                                                        <td valign="middle">
                                                            <input type="text"  name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_PHONE]%>" value="<%=(employee.getPhone() != null ? employee.getPhone() : "")%>" class="formElemen"/>                                                                                                                                
                                                            /&nbsp;<input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_HANDPHONE]%>" value="<%=employee.getHandphone()%>" class="formElemen"/>    
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.EMERGENCY_PHONE)%> / <%=dictionaryD.getWord("PERSON_NAME")%></th>
                                                        <td valign="middle">
                                                            <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_PHONE_EMERGENCY]%>" value="<%=(employee.getPhoneEmergency() != null ? employee.getPhoneEmergency() : "")%>" class="formElemen">                                                                                                                                
                                                            /&nbsp;<input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_NAME_EMG]%>" value="<%=employee.getNameEmg()%>" class="formElemen">    
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.EMERGENCY_ADDRESS)%></th>
                                                        <td valign="middle"><input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ADDRESS_EMG]%>" value="<%=employee.getAddressEmg()%>" class="formElemen" ></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.GENDER)%></th>
                                                        <td valign="middle">
                                                        <% for (int i = 0; i < PstEmployee.sexValue.length; i++) {
                                                            String str = "";
                                                            if (employee.getSex() == PstEmployee.sexValue[i]) {
                                                                str = "checked";
                                                            }
                                                        %> <input type="radio" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_SEX]%>" value="<%="" + PstEmployee.sexValue[i]%>" <%=str%> style="border:none">
                                                        <%=PstEmployee.sexKey[i]%> <% }%>
                                                        * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_SEX)%>        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.PLACE_OF_BIRTH)%></th>
                                                        <td valign="middle">
                                                        <input type="text" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_BIRTH_PLACE]%>" value="<%=employee.getBirthPlace()%>" class="formElemen">
                                                        * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_BIRTH_PLACE)%>         
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.DATE_OF_BIRTH)%></th>
                                                        <td valign="middle">
                                                        <%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_BIRTH_DATE], employee.getBirthDate() == null ? new Date() : employee.getBirthDate(), 0, -115, "formElemen")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_BIRTH_DATE)%>        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Shio</th>
                                                        <td valign="middle">
                                                         <input type="text" disabled="disable" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_SHIO]%>" value="<%=employee.getShio()%>" class="formElemen"> *Auto        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Element</th>
                                                        <td valign="middle">
                                                        <input type="text" disabled="disable" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ELEMEN]%>" value="<%=employee.getElemen()%>" class="formElemen"> *Auto        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.RELIGION)%></th>
                                                        <td valign="middle">
                                                        <% Vector relKey = new Vector(1, 1);
                                                        Vector relValue = new Vector(1, 1);
                                                        Vector listReligion = PstReligion.listAll();
                                                        for (int i = 0; i < listReligion.size(); i++) {
                                                            Religion religion = (Religion) listReligion.get(i);
                                                            relKey.add(religion.getReligion());
                                                            relValue.add("" + religion.getOID());
                                                        }
                                                        out.println(ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_RELIGION_ID], "formElemen", null, "" + employee.getReligionId(), relValue, relKey));
                                                        %>
                                                        * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_RELIGION_ID)%>        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="top"><%=dictionaryD.getWord("MARITAL_STATUS_FOR_HR")%></th>
                                                        <td valign="middle">
                                                        <%  Vector marKey = new Vector(1, 1);
                                                            Vector marValue = new Vector(1, 1);
                                                            Vector listMarital = PstMarital.list(0, 0, "", " MARITAL_STATUS ");
                                                            marKey.add("-select-");
                                                            marValue.add("0");
                                                            for (int i = 0; i < listMarital.size(); i++) {
                                                                Marital marital = (Marital) listMarital.get(i);
                                                                marKey.add(marital.getMaritalStatus() + " - " + marital.getNumOfChildren());
                                                                marValue.add("" + marital.getOID());
                                                            }
                                                            out.println(ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_MARITAL_ID], "formElemen", null, "" + employee.getMaritalId(), marValue, marKey));
                                                        %>
                                                        * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_MARITAL_ID)%>                                                                                                                                
                                                        <br /> for Tax Report<br /> <% out.println(ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_TAX_MARITAL_ID], "formElemen", null, "" + employee.getTaxMaritalId(), marValue, marKey)); %> <br />(Set per 1 January conform to Tax Regulation )
                                                        * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_TAX_MARITAL_ID)%>        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("BLOOD_TYPE")%></th>
                                                        <td valign="middle">
                                                        <%
                                                        out.println(ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_BLOOD_TYPE], "formElemen", null, employee.getBloodType(), PstEmployee.getBlood(), PstEmployee.getBlood()));
                                                        %>        
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("RACE")%></th>
                                                        <td valign="middle">
                                                            <%
                                                            Vector race_key = new Vector();
                                                            Vector race_value = new Vector();
                                                            race_key.add("-select-");
                                                            race_value.add(""+0);
                                                            Vector listRace = PstRace.list(0, 0, "", PstRace.fieldNames[PstRace.FLD_RACE_NAME]);

                                                            for (int i = 0; i < listRace.size(); i++) {
                                                                Race race = (Race) listRace.get(i);
                                                                race_key.add(race.getRaceName());
                                                                race_value.add(String.valueOf(race.getOID()));
                                                            }

                                                            out.println(ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_RACE], "formElemen", null, "" + employee.getRace(), race_value, race_key));
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("BARCODE_NUM")%></th>
                                                        <td valign="middle">
                                                            <input  type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_BARCODE_NUMBER]%>" title=" If Employe is a Daily Worker (DL)  please replace 'DL-' with '12' ,for  example 'DL-333' become to '12333'.     If Employe's  Status  'Resigned'  please input the barcode number, barcode number is unique for example -R(BarcodeNumb/PinNumber)" value="<%=(employee.getBarcodeNumber() != null ? employee.getBarcodeNumber() : "")%>" class="formElemen">
                                                            * <%=frmEmployee.getErrorMsgModif(FrmEmployee.FRM_FIELD_BARCODE_NUMBER)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.ID_CARD_NO)%></th>
                                                        <td valign="middle">
                                                            <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_INDENT_CARD_NR]%>" value="<%=employee.getIndentCardNr()%>" class="formElemen">
                                                            <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_INDENT_CARD_NR)%> <%=dictionaryD.getWord(I_Dictionary.TYPE)%> <%
                                                            out.println(ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ID_CARD_TYPE], "formElemen", null, employee.getIdcardtype(), PstEmployee.getId_Card_Type(), PstEmployee.getId_Card_Type()));
                                                            %>
                                                        </td>
                                                    </tr>
													<tr>
                                                        <th valign="middle">No KK</th>
                                                        <td valign="middle">
                                                            <input  type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_NO_KK]%>" value="<%=(employee.getNoKK()!= null ? employee.getNoKK() : "")%>" class="formElemen">
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("VALID_TO")%></th>
                                                        <td valign="middle"><%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_INDENT_CARD_VALID_TO],
                                                                                                                                            employee.getIndentCardValidTo() == null ? new Date() : employee.getIndentCardValidTo(), 5, -25, "formElemen")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_INDENT_CARD_VALID_TO)%></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Email</th>
                                                        <td valign="middle">
                                                            <input type="text" placeholder="e.g: jhon@domain.com" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_EMAIL_ADDRESS]%>" class="formElemen" size="40" value="<%=employee.getEmailAddress()%>" />
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                        <th valign="middle">Payroll Group </th>
                                                        <td <th valign="middle">
                                                           <% //priska menambahkan 20150730
                                                                        Vector payrollGroup_value = new Vector(1, 1);
                                                                        Vector payrollGroup_key = new Vector(1, 1);
                                                                        Vector listPayrollGroup = PstPayrollGroup.list(0, 0, "", "PAYROLL_GROUP_NAME");
                                                                        payrollGroup_value.add(""+0);
                                                                        payrollGroup_key.add("select");
                                                                        for (int i = 0; i < listPayrollGroup.size(); i++) {
                                                                            PayrollGroup payrollGroup = (PayrollGroup) listPayrollGroup.get(i);
                                                                            payrollGroup_key.add(payrollGroup.getPayrollGroupName());
                                                                            payrollGroup_value.add(String.valueOf(payrollGroup.getOID()));
                                                                        }

                                                                        %>
                                                                     
                                                                         <%=ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_PAYROLL_GROUP], "formElemen", null, "" + employee.getPayrollGroup(), payrollGroup_value, payrollGroup_key)%>
                                                        </td>

                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <div id="box_title">Quotient Information</div>
                                                <table>
                                                    <tr>
                                                        <th valign="middle">IQ</th>
                                                        <td valign="top"><input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_IQ]%>" value="<%=(employee.getIq() != null ? employee.getIq() : "")%>" class="formElemen"></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">EQ</th>
                                                        <td valign="top"><input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_EQ]%>" value="<%=(employee.getEq() != null ? employee.getEq() : "")%>" class="formElemen"></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <div id="box_title">Bank Information</div>
                                                <table>
                                                    <tr>
                                                        <th valign="middle">Nama Bank</th>
                                                        <td valign="top"><input type="text" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_NAMA_BANK]%>" class="formElemen" size="40" value="<%=employee.getNamaBank()%>" /></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">No Rekening</th>
                                                        <td valign="top"><input type="text" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_NO_REKENING]%>" class="formElemen" size="40" value="<%=employee.getNoRekening()%>" /></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Nama Pemilik Rekening</th>
                                                        <td valign="top"><input type="text" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_NAMA_PEMILIK_REKENING]%>" class="formElemen" size="40" value="<%=employee.getNamaPemilikRekening()%>" /></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Cabang Bank</th>
                                                        <td valign="top"><input type="text" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_CABANG_BANK]%>" class="formElemen" size="40" value="<%=employee.getCabangBank()%>" /></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Kode Bank</th>
                                                        <td valign="top"><input type="text" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_KODE_BANK]%>" class="formElemen" size="40" value="<%=employee.getKodeBank()%>" /></td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <div id="box_title">Tax Information</div>
                                                <table>
                                                    <tr>
                                                        <th valign="middle">NPWP</th>
                                                        <td valign="top"><input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_NPWP]%>" value="<%=(employee.getNpwp() != null ? employee.getNpwp() : "")%>" class="formElemen"></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Tanggal Pajak Terdaftar</th>
                                                        <td valign="top">
                                                            <%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_TANGGAL_PAJAK_TERDAFTAR], employee.getTanggalPajakTerdaftar() == null ? employee.getTanggalPajakTerdaftar() : employee.getTanggalPajakTerdaftar(), 5, -10, "formElemen")%>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <!-- Medical Information | Update 2015-01-09 | Hendra McHen -->
                                                <div id="box_title"><%=dictionaryD.getWord("MEDICAL_INFORMATION")%></div>
                                                <table style="background-color: #eeeeee">
                                                    
                                                    <%
                                                        String whereClauseMedical = " " + PstMedExpenseType.fieldNames[PstMedExpenseType.FLD_SHOW_STATUS] + "=1 ";
                                                        Vector listMedical = PstMedExpenseType.list(0, 0, whereClauseMedical, "");
                                                        String whereClauseMType = "";
                                                        // pecah data medical info
                                                        Vector medicalData = new Vector(1, 1);
                                                        String strMedical = employee.getMedicalInfo();
                                                        if(employee.getMedicalInfo()!=null){

                                                            for (String retval : strMedical.split(",")) {
                                                                medicalData.add(retval);
                                                            }
                                                        }
                                                        for (int k = 0; k < listMedical.size(); k++) {
                                                            MedExpenseType medExTy = (MedExpenseType) listMedical.get(k);
                                                            whereClauseMType = " " + PstMedicalType.fieldNames[PstMedicalType.FLD_MED_EXPENSE_TYPE_ID] + " = " + medExTy.getOID();
                                                            Vector listMedicalType = PstMedicalType.list(0, 0, whereClauseMType, "");
                                                    %>
                                                        <tr>
                                                            <td style="padding:3px"><%=medExTy.getType()%></td>
                                                            <td style="padding:3px">
                                                                <select name="medicalinfo">
                                                                    <option value="0">-select-</option>
                                                                    <%

                                                                            for (int j = 0; j < listMedicalType.size(); j++) {
                                                                                MedicalType mty = (MedicalType) listMedicalType.get(j);
                                                                                boolean option = false;
                                                                                if(employee.getMedicalInfo()!=null){
                                                                                    for(int h=0; h<medicalData.size(); h++){
                                                                                        String strMed = (String) medicalData.get(h);
                                                                                       if (strMed.equals(String.valueOf(mty.getOID()))) {
                                                                                           option = true;
                                                                                           %>
}
                                                                                           <option value="<%=mty.getOID()%>" selected="selected"><%=mty.getTypeName()%></option>
                                                                                           <%
                                                                                       }
                                                                                    }
                                                                                }
                                                                                if(option == false){

                                                                    %>

                                                                                <option value="<%=mty.getOID()%>"><%=mty.getTypeName()%></option>
                                                                    <%
                                                                                }
                                                                            }

                                                                    %>
                                                                </select>
                                                            </td>
                                                        </tr>
                                                        <%
                                                            }
                                                        %>

                                                    </table>                                                               
                                            </div>
                                            <div class="box">
                                                <div id="box_title"><%=dictionaryD.getWord("PARENTS")%></div>
                                                <table cellpadding="1" cellspacing="1" border="0">
                                                    
                                                    <tr>

                                                        <th valign="middle">
                                                            <%=dictionaryD.getWord("FATHER")%> <%=dictionaryD.getWord(I_Dictionary.FULL_NAME)%>
                                                        </th>
                                                        <td valign="top"><input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_FATHER]%>" value="<%=employee.getFather()%>" class="formElemen"></td>
                                                    </tr>
                                                    <tr>

                                                        <th valign="middle">
                                                            <%=dictionaryD.getWord("MOTHER")%> <%=dictionaryD.getWord(I_Dictionary.FULL_NAME)%>
                                                        </th>
                                                        <td valign="top">
                                                            <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_MOTHER]%>" value="<%=employee.getMother()%>" class="formElemen">
                                                        </td>
                                                    </tr>
                                                    <tr>

                                                        <th valign="middle">
                                                            <div align="left" nowrap><%=dictionaryD.getWord("PARENTS")%> <%=dictionaryD.getWord(I_Dictionary.ADDRESS)%></div>
                                                        </th>
                                                        <td valign="top">
                                                            <input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_PARENTS_ADDRESS]%>" value="<%=employee.getParentsAddress()%>" class="formElemen" >
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <div id="box_title"><%=dictionaryD.getWord("DEFAULT_SCHEDULE")%></div>
                                                    <%
                                                    String whereClauseDS = PstDefaultSchedule.fieldNames[PstDefaultSchedule.FLD_EMPLOYEE_ID]+"="+employee.getOID();
                                                    String orderDS= PstDefaultSchedule.fieldNames[PstDefaultSchedule.FLD_DAY_INDEX] ;
                                                    Vector dftSchedules = PstDefaultSchedule.list(0, 35, whereClauseDS, orderDS);
                                                    //Vector dftSchedules = PstDefaultSchedule.list(0, 7, whereClauseDS, orderDS);
                                                   %>
                                                  <table>
                                                       <tr>
                                                           <td>&nbsp;</td>
                                                           <th>Sun</th>
                                                           <th>Mon</th>
                                                           <th>Tue</th>
                                                           <th>Wed</th>
                                                           <th>Thu</th>
                                                           <th>Fri</th>
                                                           <th>Sat</th>
                                                       </tr>            
                                                       <tr>
                                                           <%
                                                           String week="";
                                                           if(schedulePerWeek!=0){
                                                               week= " Week ";
                                                           }    
                                                           %>
                                                           <td> <%=week%> 1st </td>
                                                           <%                    
                                                             for(int idx=1;idx <= 7; idx++){
                                                               DefaultSchedule dfltSch = PstDefaultSchedule.getDefaultSchedule(idx, dftSchedules);   
                                                               %>
                                                               <td><input type="text" size="4" name="sche1_<%=idx%>" value="<%=( dfltSch.getDayIndex()==idx? PstScheduleSymbol.getScheduleSymbol(dfltSch.getSchedule1()): "-" )%>" readonly="true" /> </td>
                                                               <%
                                                               }%>
                                                       </tr>
                                                       <!-- update by satrya 2013-04-08 -->
                                                       <%if(schedulePerWeek!=0){%>
                                                       <tr>
                                                           <td> Week 2nd </td>
                                                           <%                    
                                                             for(int idx=8;idx <= 14; idx++){
                                                               DefaultSchedule dfltSch = PstDefaultSchedule.getDefaultSchedule(idx, dftSchedules);   
                                                               %>
                                                               <td><input type="text" size="4" name="sche1_<%=idx%>" value="<%=( dfltSch.getDayIndex()==idx? PstScheduleSymbol.getScheduleSymbol(dfltSch.getSchedule1()): "-" )%>" readonly="true" /> </td>
                                                               <%
                                                               }%>
                                                       </tr>
                                                       <tr>
                                                           <td> Week 3rd </td>
                                                           <%                    
                                                             for(int idx=15;idx <= 21; idx++){
                                                               DefaultSchedule dfltSch = PstDefaultSchedule.getDefaultSchedule(idx, dftSchedules);   
                                                               %>
                                                               <td><input type="text" size="4" name="sche1_<%=idx%>" value="<%=( dfltSch.getDayIndex()==idx? PstScheduleSymbol.getScheduleSymbol(dfltSch.getSchedule1()): "-" )%>" readonly="true" /> </td>
                                                               <%
                                                               }%>
                                                       </tr>
                                                       <tr>
                                                           <td> Week 4th </td>
                                                           <%                    
                                                             for(int idx=22;idx <= 28; idx++){
                                                               DefaultSchedule dfltSch = PstDefaultSchedule.getDefaultSchedule(idx, dftSchedules);   
                                                               %>
                                                               <td><input type="text" size="4" name="sche1_<%=idx%>" value="<%=( dfltSch.getDayIndex()==idx? PstScheduleSymbol.getScheduleSymbol(dfltSch.getSchedule1()): "-" )%>" readonly="true" /> </td>
                                                               <%
                                                               }%>
                                                       </tr>
                                                       <tr>
                                                           <td> Week 5th </td>
                                                           <%                    
                                                             for(int idx=29;idx <=35; idx++){
                                                               DefaultSchedule dfltSch = PstDefaultSchedule.getDefaultSchedule(idx, dftSchedules);   
                                                               %>
                                                               <td><input type="text" size="4" name="sche1_<%=idx%>" value="<%=( dfltSch.getDayIndex()==idx? PstScheduleSymbol.getScheduleSymbol(dfltSch.getSchedule1()): "-" )%>" readonly="true" /> </td>
                                                               <%
                                                               }%>
                                                       </tr>
                                                       <%}%>
                                                       <%if(schedulePerWeek!=0){%>
                                                       <tr>
                                                           <td>2nd2</td>
                                                               <%                    
                                                             for(int idx=1;idx <= 7; idx++){
                                                               DefaultSchedule dfltSch = PstDefaultSchedule.getDefaultSchedule(idx, dftSchedules);   
                                                               %>
                                                               <td><input type="text" size="4" name="sche2_<%=idx%>" value="<%=( dfltSch.getDayIndex()==idx? PstScheduleSymbol.getScheduleSymbol(dfltSch.getSchedule2()): "-" )%>" readonly="true" </td>
                                                               <%
                                                               }%>                
                                                       </tr>
                                                       <%}%>
                                                   </table>
                                                   <div style="margin: 11px 0px;">
                                                       <strong>Presence Check Parameter</strong> &nbsp;:&nbsp;
                                                       <% for (int i = 0; i < PstEmployee.presenceCheckValue.length; i++) {
                                                                String strPresenceCheck = "";
                                                                if (employee.getPresenceCheckParameter() == PstEmployee.presenceCheckValue[i]) {
                                                                    strPresenceCheck = "checked";
                                                                }
                                                        %> <input type="radio" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_PRESENCE_CHECK_PARAMETER]%>" value="<%="" + PstEmployee.presenceCheckValue[i]%>" <%=strPresenceCheck%> style="border:'none'">
                                                        <%=PstEmployee.presenceCheckKey[i]%> 
                                                        <%}%>
                                                   </div>
                                                   <a id="btn" href="javascript:editDefaultSch()">Edit Default Schedule</a>
                                            </div>
                                        </td>

                                        <td valign="top"><!-- Right Side -->
                                            <div class="box">
                                                <div id="box_title"><%=dictionaryD.getWord(I_Dictionary.COMPANY)%> <%=dictionaryD.getWord(I_Dictionary.INFORMATION_HRD)%></div>
                                                <table>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.COMPANY)%></th>
                                                        <td valign="middle">
                                                        <%
                                                        Vector comp_value = new Vector(1, 1);
                                                        Vector comp_key = new Vector(1, 1);
                                                        Vector listComp = PstCompany.list(0, 0, "", " COMPANY ");
                                                        comp_value.add(""+0);
                                                        comp_key.add("select");
                                                        for (int i = 0; i < listComp.size(); i++) {
                                                            Company div = (Company) listComp.get(i);
                                                            comp_key.add(div.getCompany());
                                                            comp_value.add(String.valueOf(div.getOID()));
                                                        }

                                                        %>
                                                        <!-- Ari_20110903
                                                            Menambah Link menuju employee_mutation.jsp { -->
                                                        <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_COMPANY_ID], "formElemen", null, "" + (employee.getCompanyId()!=0?employee.getCompanyId():companyId), comp_value, comp_key,"onChange=\"javascript:cmdUpdateDiv()\"")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_COMPANY_ID)%> <% if (employee.getResigned() != PstEmployee.YES_RESIGN) {%> <a href="javascript:cmdEmpMutation('<%=oidEmployee%>')" class="command" style="text-decoration:none">  Employee Mutation Form</a><%}%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.DIVISION)%></th>
                                                        <td valign="middle">
                                                            <%
                                                                Vector div_value = new Vector(1, 1);
                                                                Vector div_key = new Vector(1, 1);
                                                                //update by satrya 2013-09-07
                                                                   div_key.add("-select-");
                                                                    div_value.add("0");
                                                                    String strWhere = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "=" +  "" + (employee.getCompanyId()) ;//oidCompany; 
                                                                    Vector listDiv = PstDivision.list(0, 0, strWhere, " DIVISION ");
                                                                    boolean adaDiv=false;
                                                                    for (int i = 0; i < listDiv.size(); i++) {
                                                                    Division div = (Division) listDiv.get(i);
                                                                    div_key.add(div.getDivision());
                                                                    div_value.add(String.valueOf(div.getOID()));
                                                                    if(employee.getDivisionId()==div.getOID()){
                                                                    adaDiv=true;
                                                                    }
                                                                }

                                                            %>
                                                            <!-- Ari_20110903
                                                                Menambah Link menuju employee_mutation.jsp { -->
                                                            <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_DIVISION_ID], "formElemen", null, "" + (employee.getDivisionId()), div_value, div_key,"onChange=\"javascript:cmdUpdateDep()\"")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_DIVISION_ID)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT)%></th>
                                                        <td valign="middle">
                                                        <%
                                                        if(employee.getCompanyId()==0 || adaDiv==false){  
                                                         employee.setDivisionId(0); 
                                                        }
                                                        Vector dept_value = new Vector(1, 1);
                                                        Vector dept_key = new Vector(1, 1);
                                                        dept_key.add("-select-");
                                                        dept_value.add("0");
                                                        String strWhereDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + "" + employee.getDivisionId(); //oidDivision;
                                                        Vector listDept = PstDepartment.list(0, 0, strWhereDept, " DEPARTMENT ");
                                                        boolean adaDept=false;
                                                        for (int i = 0; i < listDept.size(); i++) {
                                                        Department dept = (Department) listDept.get(i);
                                                        dept_key.add(dept.getDepartment());
                                                        dept_value.add(String.valueOf(dept.getOID()));
                                                        if(employee.getDepartmentId()==dept.getOID()){
                                                                   adaDept=true;
                                                        }               

                                                        }

                                                        //update by satrya 2013-09-07
                                                        if(employee.getDivisionId()==0 || adaDept==false){ 
                                                        //departementId=0;
                                                        employee.setDepartmentId(0);
                                                        }
                                                        %> <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, "" + (employee.getDepartmentId()), dept_value, dept_key,"onChange=\"javascript:cmdUpdateSec()\"")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_DEPARTMENT_ID)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("SECTION")%></th>
                                                        <td valign="middle">
                                                        <%
                                                            Vector sec_value = new Vector(1, 1);
                                                            Vector sec_key = new Vector(1, 1);
                                                            sec_key.add("- select -");
                                                                sec_value.add("0");
                                                                String strWhereSec = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + employee.getDepartmentId();
                                                            Vector listSec = PstSection.list(0, 0, strWhereSec, "SECTION");
                                                            for (int i = 0; i < listSec.size(); i++) {
                                                                Section sec = (Section) listSec.get(i);
                                                                sec_key.add(sec.getSection());
                                                                sec_value.add(String.valueOf(sec.getOID()));
                                                            }

                                                        %> <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_SECTION_ID], "formElemen", null, "" + (employee.getSectionId()), sec_value, sec_key,"onChange=\"javascript:cmdUpdatePos()\"")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_SECTION_ID)%>  
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <%=dictionaryD.getWord("CATEGORY")%></th>
                                                        <td valign="middle">
                                                        <%
                                                            Vector ctg_value = new Vector(1, 1);
                                                            Vector ctg_key = new Vector(1, 1);
                                                            Vector listCtg = PstEmpCategory.listAll();
                                                            for (int i = 0; i < listCtg.size(); i++) {
                                                                EmpCategory ctg = (EmpCategory) listCtg.get(i);
                                                                ctg_key.add(ctg.getEmpCategory());
                                                                ctg_value.add(String.valueOf(ctg.getOID()));
                                                            }
                                                        %> <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_EMP_CATEGORY_ID], null, "" + employee.getEmpCategoryId(), ctg_value, ctg_key)%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_EMP_CATEGORY_ID)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Level</th>
                                                        <td valign="middle">
                                                        <%
                                                            Vector lvl_value = new Vector(1, 1);
                                                            Vector lvl_key = new Vector(1, 1);
                                                            Vector listlvl = PstLevel.list(0, 0, "", " LEVEL ");
                                                            for (int i = 0; i < listlvl.size(); i++) {
                                                                Level lvl = (Level) listlvl.get(i);
                                                                lvl_key.add(lvl.getLevel());
                                                                lvl_value.add(String.valueOf(lvl.getOID()));
                                                            }
                                                            Vector gd_value = new Vector();
                                                            Vector gd_key = new Vector();
                                                            Vector listGradeLevel = PstGradeLevel.listAll(); 
                                                            for (int i = 0; i < listGradeLevel.size(); i++) {
                                                                GradeLevel gradeLevel = (GradeLevel) listGradeLevel.get(i);
                                                                gd_key.add(gradeLevel.getCodeLevel());
                                                                gd_value.add(String.valueOf(gradeLevel.getOID()));
                                                            }
                                                        %> <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_LEVEL_ID], "formElemen", null, "" + employee.getLevelId(), lvl_value, lvl_key)%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_LEVEL_ID)%>
                                                        <%
                                                        int SetGrade = 1;
                                                        try{
                                                            SetGrade = Integer.valueOf(PstSystemProperty.getValueByName("USE_GRADE_SET")); 
                                                        } catch (Exception e){
                                                           System.out.printf("GRADE DAN LOCATION TIDAK DI SET?"); 
                                                        }

                                                        if (SetGrade==1) {   
                                                        %>
                                                        <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_GRADE_LEVEL_ID], "formElemen", null, "" + employee.getGradeLevelId(), gd_value, gd_key)%> 
                                                        <%}
                                                        %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.POSITION)%></th>
                                                        <td valign="middle">
                                                        <%
                                                            Vector pos_value = new Vector(1, 1);
                                                            Vector pos_key = new Vector(1, 1);
                                                            Vector listPos = PstPosition.list(0, 0, "", " POSITION ");
                                                            for (int i = 0; i < listPos.size(); i++) {
                                                                Position pos = (Position) listPos.get(i);
                                                                pos_key.add(pos.getPosition());
                                                                pos_value.add(String.valueOf(pos.getOID()));
                                                            }
                                                        %> <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_POSITION_ID], "formElemen", null, "" + employee.getPositionId(), pos_value, pos_key)%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_POSITION_ID)%>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <div id="box_title"><%=dictionaryD.getWord("WORK_ASSIGN_INFORMATION")%></div>
                                                <table>
                                                    <tr>
                                                    <th  valign="middle" nowrap>
                                                        W. A. <%=dictionaryD.getWord(I_Dictionary.COMPANY)%>
                                                    </th>
                                                        <td  valign="middle">

                                                            <%
                                                            //priska 4-11-2014
                                                            Vector wa_comp_value = new Vector(1, 1);
                                                            Vector wa_comp_key = new Vector(1, 1);
                                                            Vector listWa_Comp = PstCompany.list(0, 0, "", " COMPANY ");
                                                            wa_comp_value.add(""+0);
                                                            wa_comp_key.add("select");
                                                            for (int i = 0; i < listWa_Comp.size(); i++) {
                                                                Company wacomp = (Company) listComp.get(i);
                                                                wa_comp_key.add(wacomp.getCompany());
                                                                wa_comp_value.add(String.valueOf(wacomp.getOID()));
                                                            }
                                                            
                                                            %>
                                                            <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_COMPANY_ID], "formElemen", null, "" + (employee.getWorkassigncompanyId()!=0?employee.getWorkassigncompanyId():companyId!=0?companyId:employee.getCompanyId()), wa_comp_value, wa_comp_key,"onChange=\"javascript:cmdUpdateDiv()\"")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_WORK_ASSIGN_COMPANY_ID)%> 
                                                        </td>        
                                                    </tr>
                                                    <tr>
                                                        <th  valign="middle" nowrap>
                                                            W. A. <%=dictionaryD.getWord(I_Dictionary.DIVISION)%>
                                                        </th>
                                                       <td  valign="middle">
                                                            <%
                                                                Vector wa_div_value = new Vector(1, 1);
                                                                Vector wa_div_key = new Vector(1, 1);
                                                                //update by satrya 2013-09-07
                                                                    wa_div_key.add("-select-");
                                                                    wa_div_value.add("0");
                                                                    long xWACompany=employee.getWorkassigncompanyId()!=0?employee.getWorkassigncompanyId():companyId!=0?companyId:employee.getCompanyId();
                                                                    String strWherewadiv = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID] + "='"+xWACompany+"'" ;//oidCompany; 
                                                                    Vector listWaDiv = PstDivision.list(0, 0, strWherewadiv, " DIVISION ");
                                                                    boolean adaWaDiv=false;
                                                                    for (int i = 0; i < listWaDiv.size(); i++) {
                                                                    Division wadiv = (Division) listWaDiv.get(i);
                                                                    wa_div_key.add(wadiv.getDivision());
                                                                    wa_div_value.add(String.valueOf(wadiv.getOID()));
                                                                    if(employee.getWorkassigndivisionId()==wadiv.getOID()){
                                                                    adaWaDiv=true;
                                                                    }
                                                                }

                                                            %>
                                                            <!-- Priska menambahkan work assign 2014-11-3 -->
                                                            <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_DIVISION_ID], "formElemen", null, "" + (employee.getWorkassigndivisionId()!=0?employee.getWorkassigndivisionId():wadivisionId!=0?wadivisionId:employee.getDivisionId()), wa_div_value, wa_div_key,"onChange=\"javascript:cmdUpdateDep()\"")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_WORK_ASSIGN_DIVISION_ID)%> 
                                                       </td>
                                                                                                                                
                                                    </tr>
                                                    <tr>
                                                        <th  valign="middle">
                                                            W. A. <%=dictionaryD.getWord(I_Dictionary.DEPARTMENT)%>
                                                        </th>
                                                        <td  valign="middle">
                                                                <%
                                                                if(employee.getWorkassigncompanyId()==0 || adaWaDiv==false){  
                                                                 employee.setWorkassigndivisionId(0); 
                                                                }
                                                                Vector wa_dept_value = new Vector(1, 1);
                                                                Vector wa_dept_key = new Vector(1, 1);
                                                                wa_dept_key.add("-select-");
                                                                wa_dept_value.add("0");
                                                                long WAdivision = employee.getWorkassigndivisionId()!=0?employee.getWorkassigndivisionId():wadivisionId!=0?wadivisionId:employee.getDivisionId();
                                                                String strWherewaDept = PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + WAdivision ; //oidDivision;
                                                                Vector listwaDept = PstDepartment.list(0, 0, strWherewaDept, " DEPARTMENT ");
                                                                boolean adawaDept=false;
                                                                for (int i = 0; i < listwaDept.size(); i++) {
                                                                Department wa_dept = (Department) listwaDept.get(i);
                                                                wa_dept_key.add(wa_dept.getDepartment());
                                                                wa_dept_value.add(String.valueOf(wa_dept.getOID()));
                                                                if(employee.getWorkassigndepartmentId()==wa_dept.getOID()){
                                                                           adawaDept=true;
                                                                }

                                                                }

                                                                //update by Priska 2014-11-06
                                                                if(employee.getWorkassigndivisionId()==0 || adawaDept==false){ 
                                                                //departementId=0;
                                                                employee.setWorkassigndepartmentId(0);
                                                                }
                                                                %> <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_DEPARTMENT_ID], "formElemen", null, "" + (employee.getWorkassigndepartmentId()!=0?employee.getWorkassigndepartmentId():wadepartementId!=0?wadepartementId:employee.getDepartmentId()), wa_dept_value, wa_dept_key,"onChange=\"javascript:cmdUpdateSec()\"")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_WORK_ASSIGN_DEPARTMENT_ID)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th  valign="middle" nowrap>
                                                            W. A. <%=dictionaryD.getWord("SECTION")%>
                                                        </th>
                                                        <td  valign="middle">
                                                             <%
                                                                Vector wa_sec_value = new Vector(1, 1);
                                                                Vector wa_sec_key = new Vector(1, 1);
                                                                    wa_sec_key.add("- select -");
                                                                    wa_sec_value.add("0");

                                                                    long WAdepartment = employee.getWorkassigndepartmentId()!=0?employee.getWorkassigndepartmentId():wadepartementId!=0?wadepartementId:employee.getDepartmentId();
                                                                    String strWherewaSec = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + "=" + WAdepartment;
                                                                Vector listwaSec = PstSection.list(0, 0, strWherewaSec, "SECTION");
                                                                for (int i = 0; i < listwaSec.size(); i++) {
                                                                    Section wasec = (Section) listwaSec.get(i);
                                                                    wa_sec_key.add(wasec.getSection());
                                                                    wa_sec_value.add(String.valueOf(wasec.getOID()));
                                                                }

                                                            %> <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_SECTION_ID], "formElemen", null, "" + (employee.getWorkassignsectionId()!=0?employee.getWorkassignsectionId():employee.getSectionId()), wa_sec_value, wa_sec_key)%>  <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_WORK_ASSIGN_SECTION_ID)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th  width="58%"  valign="middle" nowrap>
                                                            W. A. <%=dictionaryD.getWord(I_Dictionary.POSITION)%>
                                                        </th>
                                                        <td  width="46%"  valign="middle">
                                                            <%
                                                                Vector wa_pos_value = new Vector(1, 1);
                                                                Vector wa_pos_key = new Vector(1, 1);
                                                                
                                                                    wa_pos_key.add("- select -");
                                                                    wa_pos_value.add("0");
                                                                Vector listwaPos = PstPosition.list(0, 0, "", " POSITION ");
                                                                for (int i = 0; i < listwaPos.size(); i++) {
                                                                    Position wapos = (Position) listwaPos.get(i);
                                                                    wa_pos_key.add(wapos.getPosition());
                                                                    wa_pos_value.add(String.valueOf(wapos.getOID()));
                                                                }

                                                            %>
                                                            <%=  ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_WORK_ASSIGN_POSITION_ID], "formElemen", null, "" + (employee.getWorkassignpositionId()!=0?employee.getWorkassignpositionId():employee.getPositionId()), pos_value, pos_key) %> * <%= frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_WORK_ASSIGN_POSITION_ID) %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th  width="58%"  valign="middle" nowrap>
                                                            W. A. <%=dictionaryD.getWord(I_Dictionary.PROVIDER)%>
                                                        </th>
                                                        <td  width="46%"  valign="middle">
                                                            <%
                                                                Vector provValue = new Vector(1, 1);
                                                                Vector provKey = new Vector(1, 1);
                                                                provKey.add("-");
                                                                    provValue.add(String.valueOf(0));
                                                                Vector listProvider = PstContactList.list(0, 0, "", ""+ PstContactList.fieldNames[PstContactList.FLD_COMP_NAME]+","+ PstContactList.fieldNames[PstContactList.FLD_PERSON_NAME]);
                                                                for (int i = 0; i < listProvider.size(); i++) {
                                                                    ContactList waContact = (ContactList) listProvider.get(i);
                                                                    provKey.add(waContact.getCompName());
                                                                    provValue.add(String.valueOf(waContact.getOID()));
                                                                }

                                                            %>
                                                            <%=  ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_PROVIDER_ID], "formElemen", null, "" + (employee.getProviderID()!=0?employee.getProviderID():employee.getProviderID()), provValue, provKey) %> * <%= frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_PROVIDER_ID) %>
                                                       </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <table>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("COMMENCING_DATE")%></th>
                                                        <td valign="middle"><%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_COMMENCING_DATE], employee.getCommencingDate() == null ? new Date() : employee.getCommencingDate(), 40, -100, "formElemen")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_COMMENCING_DATE)%></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("PROBATION_END_DATE")%></th>
                                                        <td valign="middle"><%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_PROBATION_END_DATE], employee.getProbationEndDate() == null ? null : employee.getProbationEndDate(), 10, -50, "formElemen")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_PROBATION_END_DATE)%></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("LOCKER")%></th>
                                                        <td valign="middle">
                                                        <%

                                                            Vector locLocKey = new Vector(1, 1);
                                                            Vector locLocValue = new Vector(1, 1);
                                                            Vector listLockerLocation = PstLockerLocation.list(0, 0, "", "");
                                                            locLocKey.add("select...");
                                                            locLocValue.add("0");
                                                            for (int i = 0; i < listLockerLocation.size(); i++) {
                                                                    LockerLocation lockerlocation = (LockerLocation) listLockerLocation.get(i);
                                                                    locLocKey.add(lockerlocation.getLocation());
                                                                    locLocValue.add("" + lockerlocation.getOID());
                                                            }
                                                            //out.println(ControlCombo.draw(frmLocker.fieldNames[FrmLocker.FRM_FIELD_LOCATION_ID],"formElemen",null,locker.getLocationId() != 0 ? ""+locker.getLocationId() : "",locLocValue,locLocKey));
                                                            long locationId = 0;

                                                            try {
                                                                    if (employee.getLockerId() > 0) {
                                                                            Locker lock = PstLocker.fetchExc(employee.getLockerId());
                                                                            locationId = lock.getLocationId();
                                                                    }
                                                            } catch (Exception e) {
                                                                    System.out.println("Exception EmployeeLockerId()"+e);
                                                            }

                                                            //out.println(ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_LOCKER_ID],"formElemen",null,employee.getLockerId() != 0 ? ""+locationId : "",locLocValue,locLocKey));
                                                            out.println(ControlCombo.draw("LOCKER_LOCATION", "formElemen", null, employee.getLockerId() != 0 ? "" + locationId : "", locLocValue, locLocKey, "onchange='javascript:cmdGetLocker()'"));

                                                            Locker lockerEmp = new Locker();
                                                            try {
                                                                    if(employee.getLockerId()!=0)
                                                                    lockerEmp = PstLocker.fetchExc(employee.getLockerId());
                                                            } catch (Exception ex) {
                                                                    System.out.println("Exception Loker"+ex);
                                                            }   

                                                        %>
                                                        <%--input type="text" name="<%=frmLocker.fieldNames[FrmLocker.FRM_FIELD_LOCKER_NUMBER]%>" value="<%=locker.getLockerNumber()%>" class="formElemen" size="10"--%>
                                                        <input type="text" name="LOCKER_NUMBER_POS" readonly value="<%=lockerEmp.getLockerNumber()%>" class="formElemen" >
                                                        <input type="hidden" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_LOCKER_ID]%>" value="<%="" + employee.getLockerId()%>" >
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <table>
                                                    <tr>
                                                        <th valign="middle">BPJS Ketenaga Kerjaan Number</th>
                                                        <td valign="middle"><input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ASTEK_NUM]%>" value="<%=employee.getAstekNum()%>" class="formElemen"></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">BPJS Ketenaga Kerjaan Date</th>
                                                        <td valign="middle"><%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_ASTEK_DATE], employee.getAstekDate() == null ? null : employee.getAstekDate(), 1, -46, "formElemen")%> </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">BPJS Kesehatan No.</th>
                                                        <td valign="middle"><input type="text" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_BPJS_NO]%>" value="<%=employee.getBpjs_no()!=null? employee.getBpjs_no():""%>" class="formElemen"></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">BPJS Kesehatan Date</th>
                                                        <td valign="middle"><%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_BPJS_DATE], employee.getBpjs_date(), 1, -46, "formElemen")%></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Member of BPJS Kesehatan</th>
                                                        <td valign="middle">
                                                            <% for (int i = 0; i < PstEmployee.memberOfBPJSKesehatanValue.length; i++) {
                                                                    String strMemOfBpjsKesehatan = "";
                                                                    if (employee.getMemOfBpjsKesahatan() == PstEmployee.memberOfBPJSKesehatanValue[i]) {
                                                                        strMemOfBpjsKesehatan = "checked";
                                                                    }
                                                            %> <input type="radio" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_MEMBER_OF_BPJS_KESEHATAN]%>" value="<%="" + PstEmployee.memberOfBPJSKesehatanValue[i]%>" <%=strMemOfBpjsKesehatan%> style="border:'none'">
                                                            <%=PstEmployee.memberOfBPJSKesehatanKey[i]%> <%}%> 
                                                        </td>
                                                    </tr>
                                                     <tr>
                                                        <th valign="middle">Member of BPJS Ketenagakerjaan</th>
                                                        <td valign="middle">
                                                            <% for (int i = 0; i < PstEmployee.memberOfBPJSKetenagaKerjaanValue.length; i++) {
                                                                    String strMemOfBpjsKetenagaKerjaan = "";
                                                                    if (employee.getMemOfBpjsKetenagaKerjaan() == PstEmployee.memberOfBPJSKetenagaKerjaanValue[i]) {
                                                                        strMemOfBpjsKetenagaKerjaan = "checked";
                                                                    }
                                                            %> <input type="radio" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_MEMBER_OF_BPJS_KETENAGAKERJAAN]%>" value="<%="" + PstEmployee.memberOfBPJSKetenagaKerjaanValue[i]%>" <%=strMemOfBpjsKetenagaKerjaan%> style="border:'none'">
                                                            <%=PstEmployee.memberOfBPJSKetenagaKerjaanKey[i]%> <%}%> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Status Pension Program</th>
                                                        <td valign="middle">
                                                            <% for (int i = 0; i < PstEmployee.statusPensiunProgramValue.length; i++) {
                                                                    String strStPensiun = "";
                                                                    if (employee.getStatusPensiunProgram() == PstEmployee.statusPensiunProgramValue[i]) {
                                                                            strStPensiun = "checked";
                                                                    }
                                                            %> <input type="radio" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_STATUS_PENSIUN_PROGRAM]%>" value="<%="" + PstEmployee.statusPensiunProgramValue[i]%>" <%=strStPensiun%> style="border:'none'">
                                                            <%=PstEmployee.statusPensiunProgramKey[i]%> <%}%> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Start Date Pensiun</th>
                                                        <td valign="middle"><%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_START_DATE_PENSIUN], employee.getStartDatePensiun(), +5, -10, "formElemen")%></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Dana Pendidikan</th>
                                                        <td valign="middle">
                                                            <% for (int i = 0; i < PstEmployee.statusDanaPendidikanValue.length; i++) {
                                                                    String strDanaPendidikan = "";
                                                                    if (employee.getDanaPendidikan() == PstEmployee.statusDanaPendidikanValue[i]) {
                                                                            strDanaPendidikan = "checked";
                                                                    }
                                                            %> <input type="radio" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_DANA_PENDIDIKAN]%>" value="<%="" + PstEmployee.statusDanaPendidikanValue[i]%>" <%=strDanaPendidikan%> style="border:'none'">
                                                            <%=PstEmployee.statusDanaPendidikanKey[i]%> <%}%> 

                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("RESIGNATION")%></th>
                                                        <td valign="middle">
                                                            <% for (int i = 0; i < PstEmployee.resignValue.length; i++) {
                                                                    String strRes = "";
                                                                    if (employee.getResigned() == PstEmployee.resignValue[i]) {
                                                                        strRes = "checked";
                                                                    }
                                                            %> <input type="radio" name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_RESIGNED]%>" value="<%="" + PstEmployee.resignValue[i]%>" <%=strRes%> style="border:'none'">
                                                            <%=PstEmployee.resignKey[i]%> <%}%> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord("RESIGNED_DATE")%></th>
                                                        <td valign="middle">
                                                            <%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_RESIGNED_DATE], employee.getResignedDate(), +5, -10, "formElemen")%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Resigned Reason</th>
                                                        <td valign="middle">
                                                            <% Vector resKey = new Vector(1, 1);
                                                                Vector resValue = new Vector(1, 1);
                                                                Vector listRes = PstResignedReason.list(0, 0, "", "RESIGNED_REASON");
                                                                resKey.add("select...");
                                                                resValue.add("0");
                                                                for (int i = 0; i < listRes.size(); i++) {
                                                                    ResignedReason resignedReason = (ResignedReason) listRes.get(i);
                                                                    resKey.add(resignedReason.getResignedReason());
                                                                    resValue.add("" + resignedReason.getOID());
                                                                }
                                                                out.println(ControlCombo.draw(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_RESIGNED_REASON_ID], "formElemen", null, "" + employee.getResignedReasonId(), resValue, resKey));
                                                            %> 
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Resigned Description</th>
                                                        <td valign="middle"><textarea name="<%=frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_RESIGNED_DESC]%>" class="formElemen" rows="2" cols="30"><%=employee.getResignedDesc()%></textarea></td>
                                                    </tr>
                                                    <%if(attdConfig!=null && attdConfig.getConfigurationOutletMiniMarket()==I_Atendance.CONFIGURASI_I_VIEW_SHOW_ALL_CONFIGURATION_MINIMART){%>
                                                    <tr>
                                                        <th valign="middle">Kadiv Mapping</th>
                                                        <td valign="middle"><a href="javascript:cmdViewKadiv('<%=oidEmployee%>')">Add Or View Kadiv Outlet</a></td>
                                                    </tr>
                                                    <%}%>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> PIN</th>
                                                        <td valign="middle">
                                                            <input type="password" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_EMP_PIN]%>" value="<%=(employee.getEmpPin() != null ? employee.getEmpPin() : "")%>" class="formElemen">
                                                                         * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_EMP_PIN)%>&nbsp;
                                                        </td>
                                                    </tr>
                                                    <% int SetLocation = 1;
                                                    try {
                                                    SetLocation =Integer.valueOf(PstSystemProperty.getValueByName("USE_LOCATION_SET")); 
                                                    } catch (Exception e){
                                                    }
                                                    if (SetLocation==1) {
                                                    %>
                                                    <tr>
                                                   <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.LOCATION)%></th>
                                                   <td valign="middle">      
                                                   <%
                                                    String CtrOrderClause = PstLocation.fieldNames[PstLocation.FLD_CODE];
                                                    Vector vectLocation = PstLocation.list(0,0,"",CtrOrderClause);
                                                    Vector val_Location = new Vector(1,1); //hidden values that will be deliver on request (oids) 
                                                    Vector key_Location = new Vector(1,1); //texts that displayed on combo box
                                                    val_Location.add("0");
                                                    key_Location.add("All Location");
                                                    for(int c = 0; c < vectLocation.size();c++){
                                                    Location location = (Location)vectLocation.get(c);
                                                    val_Location.add(""+location.getOID());
                                                    key_Location.add(location.getName());
                                                    }
                                                    String select_complocation = ""+employee.getLocationId(); //selected on combo box
                                                   %>
                                                   <%= ControlCombo.draw(FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_LOCATION_ID], "formElemen", null, "" + employee.getLocationId(), val_Location, key_Location)%>  <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_LOCATION_ID)%>
                                                   </td>
                                                    </tr>
                                                   <% } %>
                                                   <tr>
                                                        <th  valign="middle">
                                                        Ended Contract
                                                        </th>
                                                        <td  valign="middle">
                                                            <%=ControlDate.drawDateWithStyle(frmEmployee.fieldNames[FrmEmployee.FRM_FIELD_END_CONTRACT], employee.getEnd_contract() == null ? employee.getEnd_contract() : employee.getEnd_contract(), 0, 30, "formElemen")%> * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_END_CONTRACT)%>
                                                        </td>        
                                                   </tr>
                                                </table>
                                            </div>
                                            
                                            <div class="box">
                                                <% if(privGenerate == true){ %>
                                                <div id="box_title">Generate Salary Level</div>
                                                <table>
                                                    
                                                    <tr>
                                                        <th>Level Code</th>
                                                        <td><input type="text" name="salary_level_code" value="" placeholder="input level code" /></td>
                                                    </tr>
                                                    <tr>
                                                        <th>Level Name</th>
                                                        <td><input type="text" name="salary_level_name" value="" placeholder="input level name" /></td>
                                                    </tr>
                                                    <tr>
                                                        <th>Note</th>
                                                        <td><textarea name="salary_level_note"></textarea></td>
                                                    </tr>
                                                    <tr>
                                                        <td>&nbsp;</td>
                                                        <td><button onclick="cmdGenerateSalaryLevel()" id="btn">Generate</button></td>
                                                    </tr>
                                                </table>

                                                <% } %>
                                            </div>
                                            <div class="box">
                                                <div id="box_title">HOD</div>
                                                <table>
                                                    
                                                    <input type="hidden" name="<%=FrmEmployee.fieldNames[FrmEmployee.FRM_FIELD_HOD_EMPLOYEE_ID]%>" value="<%=employee.getHodEmployeeId()%>" class="formElemen">
                                                    <% Employee hodEmployee = new Employee();
                                                        Department hodDepartment = new Department();
                                                        Section hodSection = new Section();
                                                        if (employee != null && employee.getHodEmployeeId() != 0) {
                                                            try {
                                                                hodEmployee = PstEmployee.fetchExc(employee.getHodEmployeeId());
                                                                hodDepartment = PstDepartment.fetchExc(hodEmployee.getDepartmentId());
                                                                hodSection = PstSection.fetchExc(hodEmployee.getSectionId());
                                                            } catch (Exception exc) {
                                                                System.out.println("Exception"+exc);
                                                            }
                                                        }


                                                    %>
                                                    <tr>
                                                        <th valign="middle">Payroll</th>
                                                        <td valign="middle">
                                                            <input type="text" name="EMP_NUMBER"  value="<%=hodEmployee.getEmployeeNum()%>" class="elemenForm" size="10"  readonly>
                                                            * <%=frmEmployee.getErrorMsg(FrmEmployee.FRM_FIELD_HOD_EMPLOYEE_ID)%>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle">Name</th>
                                                        <td valign="middle"><input type="text" name="EMP_FULLNAME"  value="<%=hodEmployee.getFullName()%>" class="elemenForm"  readonly></td>
                                                    </tr>
                                                    <tr>
                                                        <th valign="middle"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></th>
                                                        <td valign="middle"><input type="text" name="EMP_DEPARTMENT"  value="<%=hodDepartment.getDepartment()%>" class="elemenForm"  readonly><input type="hidden" name="EMP_SECTION"  value="" class="elemenForm"> </td>
                                                    </tr>
                                                    <tr>
                                                        <td colspan="2" valign="middle">
                                                            <button id="btn" onclick="cmdSearchEmp()">Search Employee</button>&nbsp;<button id="btn" onclick="cmdClearSearchEmp()">Clear Search</button> 
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            <div class="box">
                                                <div id="box_title">Custom Field</div>
                                                <table>
                                                    <%
                                                    Vector listCustom = PstCustomFieldMaster.list(0, 0, "", "");
                                                    String valueInput = "";
                                                    for(int c=0; c<listCustom.size(); c++){
                                                        CustomFieldMaster custom = (CustomFieldMaster)listCustom.get(c);
                                                        String whereEmpCustom = "CUSTOM_FIELD_ID="+custom.getOID()+" AND EMPLOYEE_ID="+employee.getOID();
                                                        Vector listEmpCustom = PstEmpCustomField.list(0, 0, whereEmpCustom, "");
                                                        if (listEmpCustom != null && listEmpCustom.size()>0){
                                                            for(int e=0; e<listEmpCustom.size(); e++){
                                                                EmpCustomField empCust = (EmpCustomField)listEmpCustom.get(e);
                                                                switch(custom.getFieldType()){
                                                                    case 0: valueInput = empCust.getDataText(); break;
                                                                    case 1: valueInput = ""+empCust.getDataNumber(); break;
                                                                    case 2: valueInput = ""+empCust.getDataNumber(); break;
                                                                    case 3: valueInput = ""+empCust.getDataDate(); break;
                                                                    case 4: valueInput = ""+empCust.getDataDate(); break;
                                                                }
                                                            }
                                                        } else {
                                                            valueInput = "";
                                                        }
                                                        String input = "";
                                                        switch(custom.getInputType()){
                                                            case 0: input = "<input type=\"text\" name=\"input0\" value=\""+valueInput+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"hidden0\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type0\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                            case 1: input = "<textarea name=\"input1\"></textarea>";
                                                                    input += "<input type=\"hidden\" name=\"hidden1\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type1\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                            case 2: input = custom.getDataList();
                                                                    input += "<input type=\"hidden\" name=\"hidden2\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type2\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                            case 3: input = custom.getDataList();
                                                                    input += "<input type=\"hidden\" name=\"hidden3\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type3\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                            case 4: input = "<input type=\"text\" class=\"mydate\" name=\"input4\" value=\""+valueInput+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"hidden4\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type4\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                            case 5: input = "<input type=\"text\" class=\"mydate\" name=\"input5\" />";
                                                                    input += "<input type=\"hidden\" name=\"hidden5\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type5\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                            case 6: input = custom.getDataList();
                                                                    input += "<input type=\"hidden\" name=\"hidden6\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type6\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                            case 7: input = custom.getDataList();
                                                                    input += "<input type=\"hidden\" name=\"hidden7\" value=\""+custom.getOID()+"\" />";
                                                                    input += "<input type=\"hidden\" name=\"data_type7\" value=\""+custom.getFieldType()+"\" />";
                                                                    break;
                                                        }
                                                    
                                                    %>
                                                    <tr>
                                                        <th valign="middle"><%=custom.getFieldName()%></th>
                                                        <td valign="middle"><%=input%></td>
                                                    </tr>
                                                    
                                                    <%}%>
                                                    <tr>
                                                        <td colspan="2"><button id="btn" onclick="cmdSaveCustom('<%=employee.getOID()%>')">Save data custom</button></td>
                                                    </tr>
                                                </table>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                <% if(iErrCode!=FRMMessage.ERR_NONE  ){%>
                                                <tr>
                                                    <td>
                                                        <% 
                                                         int maxFld = FrmEmployee.fieldNames.length;
                                                         for(int idx=0;idx< maxFld;idx++)
                                                         {     
                                                            String msgX = frmEmployee.getErrorMsg(idx); 
                                                            if(msgX!=null && msgX.length()>0){
                                                               out.println( FrmEmployee.fieldNamesForUser[idx]+" = "+ frmEmployee.getErrorMsg(idx)+";");
                                                             }
                                                         }

                                                         //}

                                                        %>
                                                    </td>
                                                </tr>
                                                <%} %>
                                                <tr>
                                                    <td> <%

                                                        ctrLine.setLocationImg(approot + "/images");
                                                        ctrLine.initDefault();
                                                        ctrLine.setTableWidth("80");
                                                        String scomDel = "javascript:cmdAsk('" + oidEmployee + "')";
                                                        String sconDelCom = "javascript:cmdConfirmDelete('" + oidEmployee + "')";
                                                        String scancel = "javascript:cmdEdit('" + oidEmployee + "')";
                                                        ctrLine.setBackCaption("Back to List Employee");
                                                        ctrLine.setCommandStyle("buttonlink");
                                                        ctrLine.setDeleteCaption("Delete Employee");
                                                        ctrLine.setAddCaption("Add Employee");
                                                        ctrLine.setSaveCaption("Save Employee");

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

                                                     
                                                        
                                                        if ((iCommand == Command.EDIT || prevCommand == Command.EDIT) && (iCommand != Command.ASK )) {
                                                            iCommand = Command.EDIT;
                                                        }
                                                        %> <%= ctrLine.drawImage(iCommand, iErrCode, errMsg)%> </td>
                                                </tr>
                                                <tr>
                                                    <td> <table cellpadding="0" cellspacing="0" border="0">
                                                            <tr>
                                                                <% if (privPrint) {%>
                                                                <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                                                <td width="24"><a href="javascript:cmdPrint()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1002','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1002" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Print"></a></td>
                                                                <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                                                <td nowrap><a href="javascript:cmdPrint()" class="command" style="text-decoration:none">Print
                                                                        Employee</a></td>
                                                                <td width="8"><img src="<%=approot%>/images/spacer.gif" width="8" height="8"></td>
                                                                <td width="24"><a href="javascript:cmdPrintXLS()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1003','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image1003" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Export to Excel"></a></td>
                                                                <td width="4"><img src="<%=approot%>/images/spacer.gif" width="4" height="4"></td>
                                                                <td nowrap><a href="javascript:cmdPrintXLS()" class="command" style="text-decoration:none">Export
                                                                        to Excel</a></td>
                                                                        <%}%>
                                                            </tr>
                                                        </table>
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="2" style="padding:9px 0px">&nbsp;</td>  
                                    </tr>
                                </table>
                                </form>       
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
        <footer>
                <%@include file="../../footer.jsp" %>
        </footer>
    </body>
</html>
