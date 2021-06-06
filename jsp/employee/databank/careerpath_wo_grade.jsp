<%@page import="com.dimata.common.entity.contact.ContactList"%>
<%@page import="com.dimata.common.entity.contact.PstContactList"%>
<%@page import="com.dimata.harisma.entity.masterdata.location.Location"%>
<%@page import="com.dimata.harisma.entity.masterdata.location.PstLocation"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayGeneral"%>
<%@page import="com.dimata.harisma.entity.payroll.PayGeneral"%>
<%
            /*
             * Page Name  		:  careerpath.jsp
             * Created on 		:  [date] [time] AM/PM
             *
             * @author  		: lkarunia
             * @version  		: 01
             */

            /*******************************************************************
             * Page Description 	: [project description ... ]
             * Imput Parameters 	: [input parameter ...]
             * Output 			: [output ...]
             *******************************************************************/
%>

<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>

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
<%
    boolean isSecretaryLogin = (positionType >= PstPosition.LEVEL_SECRETARY) ? true : false;
    long hrdDepartmentOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_HRD_DEPARTMENT)));
    boolean isHRDLogin = hrdDepartmentOid == departmentOid ? true : false;
    long edpSectionOid = Long.parseLong(String.valueOf(PstSystemProperty.getPropertyLongbyName(OID_EDP_SECTION)));
    boolean isEdpLogin = edpSectionOid == sectionOfLoginUser.getOID() ? true : false;
    boolean isGeneralManager = positionType == PstPosition.LEVEL_GENERAL_MANAGER ? true : false;
%>
<!-- Jsp Block -->

<%!    
    public String drawList(Vector objectClass, long workHistoryNowId, I_Dictionary dictionaryD) {
        int index = -1;
        String output = "";
        for (int i = 0; i < objectClass.size(); i++) {
            CareerPath careerPath = (CareerPath) objectClass.get(i);
            long oidEmpDoc = PstEmpDocListMutation.getEmpDocFinalId(careerPath.getEmployeeId(), careerPath.getWorkFrom());
        
            if (careerPath.getHistoryGroup() == PstCareerPath.RIWAYAT_JABATAN){
                output += "<tr>";
                if (workHistoryNowId == careerPath.getOID()) {
                    index = i;
                }
                String comapnyName="";
                if(careerPath.getCompanyId()!=0){
                    PayGeneral payGeneral =new PayGeneral();
                    try{
                        payGeneral =PstPayGeneral.fetchExc(careerPath.getCompanyId());
                    }catch(Exception exc){

                    }
                    comapnyName = payGeneral.getCompanyName();
                }
                output += "<td><a href=\"javascript:cmdEdit('"+careerPath.getOID()+"')\">"+comapnyName+"</a></td>";
                String divisionName="";
                if(careerPath.getDivisionId()!=0){
                    Division division =new Division();
                    try{
                        division =PstDivision.fetchExc(careerPath.getDivisionId());
                    }catch(Exception exc){

                    }
                    divisionName = division.getDivision();
                }
                if (careerPath.getDivision()!= null)
                {
                    output += "<td>"+divisionName+"</td>";
                }
                else{
                    output += "<td>"+"-"+"</td>";
                    }
                String departmentName="";
                if(careerPath.getDepartmentId()!=0){
                    Department department =new Department();
                    try{
                        department =PstDepartment.fetchExc(careerPath.getDepartmentId());
                    }catch(Exception exc){

                    }
                    departmentName = department.getDepartment();
                }

                output += "<td>"+departmentName+"</td>";
                if (careerPath.getSection().length()>0){
                    output += "<td>"+careerPath.getSection()+"</td>";
                } else {
                    output += "<td>-</td>";
                }
                
                output += "<td>"+careerPath.getPosition()+"</td>";
                int SetLocation = Integer.valueOf(PstSystemProperty.getValueByName("USE_LOCATION_SET")); 
                if (SetLocation==1) { output += "<td>"+careerPath.getLocation()+"</td>"; }
                output += "<td>"+careerPath.getLevel()+"</td>";
                output += "<td>"+careerPath.getEmpCategory()+"</td>";

                String str_dt_WorkFrom = "";
                try {
                    Date dt_WorkFrom = careerPath.getWorkFrom();
                    if (dt_WorkFrom == null) {
                        dt_WorkFrom = new Date();
                    }

                    str_dt_WorkFrom = Formater.formatDate(dt_WorkFrom, "dd MMMM yyyy");
                } catch (Exception e) {
                    str_dt_WorkFrom = "";
                }

                output += "<td>"+str_dt_WorkFrom+"</td>";

                String str_dt_WorkTo = "";
                try {
                    Date dt_WorkTo = careerPath.getWorkTo();
                    if (dt_WorkTo == null) {
                        //dt_WorkTo = new Date();
                        str_dt_WorkTo ="-";
                    } else {
                        str_dt_WorkTo = Formater.formatTimeLocale(dt_WorkTo, "dd MMMM yyyy");
                   }
                } catch (Exception e) {
                    str_dt_WorkTo = "";
                }

                output += "<td>"+str_dt_WorkTo+"</td>";
                String strGrade = "-";
                if (careerPath.getGradeLevelId() != 0){
                    try {
                        GradeLevel gLevel = PstGradeLevel.fetchExc(careerPath.getGradeLevelId());
                        strGrade = gLevel.getCodeLevel();
                    } catch(Exception e){
                        System.out.println(""+e.toString());
                    }
                } else {
                    strGrade = "-";
                }
                
                output += "<td>"+strGrade+"</td>";
                output += "<td>"+"<a href=\"javascript:cmdDetail('"+careerPath.getOID()+"')\">Detail</a></td>";
                if (oidEmpDoc !=0){
                    output += "<td>"+"<a href=\"javascript:cmdEditDetail2('"+oidEmpDoc+"')\">Detail</a></td>";
                } else {
                    output += "<td>-</td>";
                }
                
                
                output += "<td>"+"<a style=\"text-decoration:none; color:#575757;\" class=\"btn-small\" href=\"javascript:cmdMinta('"+careerPath.getOID()+"')\">&times;</a></td>";
                output += "</tr>";
            }
        }
            
        return output;

    }
    
    public String drawListGrade(Vector objectClass, long workHistoryNowId, I_Dictionary dictionaryD) {
        int index = -1;
        String output = "";
        for (int i = 0; i < objectClass.size(); i++) {
            CareerPath careerPath = (CareerPath) objectClass.get(i);
            if (careerPath.getHistoryGroup() == PstCareerPath.RIWAYAT_GRADE){
                output += "<tr>";
                if (workHistoryNowId == careerPath.getOID()) {
                    index = i;
                }
                String comapnyName="";
                if(careerPath.getCompanyId()!=0){
                    PayGeneral payGeneral =new PayGeneral();
                    try{
                        payGeneral =PstPayGeneral.fetchExc(careerPath.getCompanyId());
                    }catch(Exception exc){

                    }
                    comapnyName = payGeneral.getCompanyName();
                }
                output += "<td><a href=\"javascript:cmdEdit('"+careerPath.getOID()+"')\">"+comapnyName+"</a></td>";
                String divisionName="";
                if(careerPath.getDivisionId()!=0){
                    Division division =new Division();
                    try{
                        division =PstDivision.fetchExc(careerPath.getDivisionId());
                    }catch(Exception exc){

                    }
                    divisionName = division.getDivision();
                }
                if (careerPath.getDivision()!= null)
                {
                    output += "<td>"+divisionName+"</td>";
                }
                else{
                    output += "<td>"+"-"+"</td>";
                    }
                String departmentName="";
                if(careerPath.getDepartmentId()!=0){
                    Department department =new Department();
                    try{
                        department =PstDepartment.fetchExc(careerPath.getDepartmentId());
                    }catch(Exception exc){

                    }
                    departmentName = department.getDepartment();
                }

                output += "<td>"+departmentName+"</td>";
                if (careerPath.getSection().length()>0){
                    output += "<td>"+careerPath.getSection()+"</td>";
                } else {
                    output += "<td>-</td>";
                }
                
                output += "<td>"+careerPath.getPosition()+"</td>";
                int SetLocation = Integer.valueOf(PstSystemProperty.getValueByName("USE_LOCATION_SET")); 
                if (SetLocation==1) { output += "<td>"+careerPath.getLocation()+"</td>"; }
                output += "<td>"+careerPath.getLevel()+"</td>";
                output += "<td>"+careerPath.getEmpCategory()+"</td>";

                String str_dt_WorkFrom = "";
                try {
                    Date dt_WorkFrom = careerPath.getWorkFrom();
                    if (dt_WorkFrom == null) {
                        dt_WorkFrom = new Date();
                    }

                    str_dt_WorkFrom = Formater.formatDate(dt_WorkFrom, "dd MMMM yyyy");
                } catch (Exception e) {
                    str_dt_WorkFrom = "";
                }

                output += "<td>"+str_dt_WorkFrom+"</td>";

                String str_dt_WorkTo = "";
                try {
                    Date dt_WorkTo = careerPath.getWorkTo();
                    if (dt_WorkTo == null) {
                        //dt_WorkTo = new Date();
                        str_dt_WorkTo ="-";
                    } else {
                        str_dt_WorkTo = Formater.formatTimeLocale(dt_WorkTo, "dd MMMM yyyy");
                   }
                } catch (Exception e) {
                    str_dt_WorkTo = "";
                }

                output += "<td>"+str_dt_WorkTo+"</td>";
                String strGrade = "-";
                if (careerPath.getGradeLevelId() != 0){
                    try {
                        GradeLevel gLevel = PstGradeLevel.fetchExc(careerPath.getGradeLevelId());
                        strGrade = gLevel.getCodeLevel();
                    } catch(Exception e){
                        System.out.println(""+e.toString());
                    }
                } else {
                    strGrade = "-";
                }
                output += "<td>"+strGrade+"</td>";
                output += "<td>"+"<a href=\"javascript:cmdDetail('"+careerPath.getOID()+"')\">Detail</a></td>";
                output += "<td>-</td>";
                output += "<td>"+"<a style=\"text-decoration:none; color:#575757;\" class=\"btn-small\" href=\"javascript:cmdMinta('"+careerPath.getOID()+"')\">&times;</a></td>";
                output += "</tr>";
            }
        }
            
        return output;

    }
    
    public String drawListCareerNGrade(Vector objectClass, long workHistoryNowId, I_Dictionary dictionaryD) {
        int index = -1;
        String output = "";
        for (int i = 0; i < objectClass.size(); i++) {
            CareerPath careerPath = (CareerPath) objectClass.get(i);
            if (careerPath.getHistoryGroup() == PstCareerPath.RIWAYAT_CAREER_N_GRADE){
                output += "<tr>";
                if (workHistoryNowId == careerPath.getOID()) {
                    index = i;
                }
                String comapnyName="";
                if(careerPath.getCompanyId()!=0){
                    PayGeneral payGeneral =new PayGeneral();
                    try{
                        payGeneral =PstPayGeneral.fetchExc(careerPath.getCompanyId());
                    }catch(Exception exc){

                    }
                    comapnyName = payGeneral.getCompanyName();
                }
                output += "<td><a href=\"javascript:cmdEdit('"+careerPath.getOID()+"')\">"+comapnyName+"</a></td>";
                String divisionName="";
                if(careerPath.getDivisionId()!=0){
                    Division division =new Division();
                    try{
                        division =PstDivision.fetchExc(careerPath.getDivisionId());
                    }catch(Exception exc){

                    }
                    divisionName = division.getDivision();
                }
                if (careerPath.getDivision()!= null)
                {
                    output += "<td>"+divisionName+"</td>";
                }
                else{
                    output += "<td>"+"-"+"</td>";
                    }
                String departmentName="";
                if(careerPath.getDepartmentId()!=0){
                    Department department =new Department();
                    try{
                        department =PstDepartment.fetchExc(careerPath.getDepartmentId());
                    }catch(Exception exc){

                    }
                    departmentName = department.getDepartment();
                }

                output += "<td>"+departmentName+"</td>";
                if (careerPath.getSection().length()>0){
                    output += "<td>"+careerPath.getSection()+"</td>";
                } else {
                    output += "<td>-</td>";
                }
                
                output += "<td>"+careerPath.getPosition()+"</td>";
                int SetLocation = Integer.valueOf(PstSystemProperty.getValueByName("USE_LOCATION_SET")); 
                if (SetLocation==1) { output += "<td>"+careerPath.getLocation()+"</td>"; }
                output += "<td>"+careerPath.getLevel()+"</td>";
                output += "<td>"+careerPath.getEmpCategory()+"</td>";

                String str_dt_WorkFrom = "";
                try {
                    Date dt_WorkFrom = careerPath.getWorkFrom();
                    if (dt_WorkFrom == null) {
                        dt_WorkFrom = new Date();
                    }

                    str_dt_WorkFrom = Formater.formatDate(dt_WorkFrom, "dd MMMM yyyy");
                } catch (Exception e) {
                    str_dt_WorkFrom = "";
                }

                output += "<td>"+str_dt_WorkFrom+"</td>";

                String str_dt_WorkTo = "";
                try {
                    Date dt_WorkTo = careerPath.getWorkTo();
                    if (dt_WorkTo == null) {
                        //dt_WorkTo = new Date();
                        str_dt_WorkTo ="-";
                    } else {
                        str_dt_WorkTo = Formater.formatTimeLocale(dt_WorkTo, "dd MMMM yyyy");
                   }
                } catch (Exception e) {
                    str_dt_WorkTo = "";
                }

                output += "<td>"+str_dt_WorkTo+"</td>";
                String strGrade = "-";
                if (careerPath.getGradeLevelId() != 0){
                    try {
                        GradeLevel gLevel = PstGradeLevel.fetchExc(careerPath.getGradeLevelId());
                        strGrade = gLevel.getCodeLevel();
                    } catch(Exception e){
                        System.out.println(""+e.toString());
                    }
                } else {
                    strGrade = "-";
                }
                output += "<td>"+strGrade+"</td>";
                output += "<td>"+"<a href=\"javascript:cmdDetail('"+careerPath.getOID()+"')\">Detail</a></td>";
                output += "<td>-</td>";
                output += "<td>"+"<a style=\"text-decoration:none; color:#575757;\" class=\"btn-small\" href=\"javascript:cmdMinta('"+careerPath.getOID()+"')\">&times;</a></td>";
                output += "</tr>";
            }
        }
            
        return output;

    }
        
%>

<%!    
    public String drawListCurrent(Employee employee,Vector objectClass, I_Dictionary dictionaryD) {
        String output = "";
        int SetLocation = -1;
        try{
           SetLocation = Integer.valueOf(PstSystemProperty.getValueByName("USE_LOCATION_SET")); 
        } catch (Exception e){
            System.out.println(e.toString());
        }

        int index = -1;
        String comapnyName="-";
        if(employee.getCompanyId()!=0){
            PayGeneral payGeneral =new PayGeneral();
            try{
                payGeneral =PstPayGeneral.fetchExc(employee.getCompanyId());
            }catch(Exception exc){

            }
            comapnyName = payGeneral.getCompanyName();
        }
        output += "<tr>";
        output += "<td>"+comapnyName+"</td>";

        String divisionName="-";
        if(employee.getDivisionId()!=0){
            Division division =new Division();
            try{
                division =PstDivision.fetchExc(employee.getDivisionId());
            }catch(Exception exc){

            }
            divisionName = division.getDivision();
        }
        output += "<td>"+divisionName+"</td>";

        String departmentName="-";
        if(employee.getDepartmentId()!=0){
            Department department =new Department();
            try{
                department =PstDepartment.fetchExc(employee.getDepartmentId());
            }catch(Exception exc){

            }
            departmentName = department.getDepartment();
        }

        output += "<td>"+departmentName+"</td>";

        String sectionName="-";
        if(employee.getSectionId()!=0){
            Section section =new Section();
            try{
                section =PstSection.fetchExc(employee.getSectionId());
            }catch(Exception exc){

            }
            sectionName = section.getSection();
        }

        output += "<td>"+sectionName+"</td>";

        String positionName="-";
        if(employee.getPositionId()!=0){
            Position position =new Position();
            try{
                position =PstPosition.fetchExc(employee.getPositionId());
            }catch(Exception exc){

            }
            positionName = position.getPosition();
        }

        output += "<td>"+positionName+"</td>";

        if (SetLocation==1) { 
            String locationName="-";
            if(employee.getLocationId()!=0){
                Location location =new Location();
                try{
                    location =PstLocation.fetchExc(employee.getLocationId());
                }catch(Exception exc){

                }
                locationName = location.getName();
            }

            output += "<td>"+locationName+"</td>";
        }

        String levelName="-";
        if(employee.getLevelId()!=0){
            Level level =new Level();
            try{
                level =PstLevel.fetchExc(employee.getLevelId());
            }catch(Exception exc){

            }
            levelName = level.getLevel();
        }

        output += "<td>"+levelName+"</td>";

        String empCatName="-";
        if(employee.getEmpCategoryId()!=0){
            EmpCategory empCategory = new EmpCategory();
            try{
                empCategory =PstEmpCategory.fetchExc(employee.getEmpCategoryId());
            }catch(Exception exc){

            }
            empCatName = empCategory.getEmpCategory();
        }

        output += "<td>"+empCatName+"</td>";
        CareerPath careerPath = new CareerPath();
        Date dateWorkFrom = new Date();
        if (objectClass.size() > 0){
                   careerPath = (CareerPath) objectClass.get(objectClass.size()-1);
                Date fromWork = careerPath.getWorkTo();
                fromWork.setDate(fromWork.getDate() + 1);
                String str_dt_WorkFrom = "";
                try {
                    Date dt_WorkFrom = fromWork;
                    if (dt_WorkFrom == null) {
                        dt_WorkFrom = new Date();
                    }

                    str_dt_WorkFrom = Formater.formatDate(dt_WorkFrom, "dd MMMM yyyy");
                    dateWorkFrom = dt_WorkFrom ;
                } catch (Exception e) {
                    str_dt_WorkFrom = "";
                }

                output += "<td>"+str_dt_WorkFrom+"</td>";

        } else {
            output += "<td>"+""+employee.getCommencingDate()+"</td>";
            dateWorkFrom = employee.getCommencingDate();
        }
        String str_dt_WorkTo = "now";
        output += "<td>"+str_dt_WorkTo+"</td>";

        String gradeLevel = "";
        try {
            GradeLevel gLevel = PstGradeLevel.fetchExc(employee.getGradeLevelId());
            gradeLevel = gLevel.getCodeLevel();
        } catch(Exception e){
            System.out.print("=>"+e.toString());
        }
        long oidEmpDoc = PstEmpDocListMutation.getEmpDocFinalId(employee.getOID(), dateWorkFrom);
        output += "<td>"+""+gradeLevel+"</td>";
        output += "<td><a href=\"javascript:cmdDetail('"+employee.getOID()+"')\">Detail</a></td>";
        output += "<td>&nbsp;</td>";
        output += "</tr>";
        return output;

    }

%>

<%
    int iCommand = FRMQueryString.requestCommand(request);
    int start = FRMQueryString.requestInt(request, "start");
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidCareerPath = FRMQueryString.requestLong(request, "career_path_oid");
    long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
    long oidHistoryComp = FRMQueryString.requestLong(request, FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_COMPANY_ID]);
    long oidHistoryDept = FRMQueryString.requestLong(request, FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DEPARTMENT_ID]);
    //update by satrya 2013-10-14
    long oidHistoryDiv = FRMQueryString.requestLong(request, FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DIVISION_ID]);
    long oidHistorySection = FRMQueryString.requestLong(request, FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_SECTION_ID]); 

//System.out.println("iCommand............."+iCommand);
/*variable declaration*/
         
    int recordToGet = 10;
    String msgString = "";
    int iErrCode = FRMMessage.NONE;
    String whereClause = PstCareerPath.fieldNames[PstCareerPath.FLD_EMPLOYEE_ID] + " = " + oidEmployee;
    String orderClause = PstCareerPath.fieldNames[PstCareerPath.FLD_WORK_FROM];

    CtrlCareerPath ctrlCareerPath = new CtrlCareerPath(request);
    ControlLine ctrLine = new ControlLine();
    Vector listCareerPath = new Vector(1, 1);
    Vector listDepartment = PstDepartment.list(0, 0, "", "DEPARTMENT");
    Vector listCompany = PstCompany.list(0, 0, "", "COMPANY");
            //Vector listSection = new Vector(1, 1);
    
    /*switch statement */
    iErrCode = ctrlCareerPath.action(iCommand, oidCareerPath, oidEmployee, request, emplx.getFullName(), appUserIdSess);
    
    if(iErrCode == ctrlCareerPath.RSLT_FRM_DATE_IN_RANGE){
        iCommand = Command.ADD;
    }
    /* end switch*/
    FrmCareerPath frmCareerPath = ctrlCareerPath.getForm();
    
    CareerPath careerPath = ctrlCareerPath.getCareerPath();
    msgString = ctrlCareerPath.getMessage();

    /*switch list CareerPath*/
    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidCareerPath == 0)) {
        start = PstCareerPath.findLimitStart(careerPath.getOID(), recordToGet, whereClause, orderClause);
    }

    /*count list All CareerPath*/
    int vectSize = PstCareerPath.getCount(whereClause);

    if ((iCommand == Command.FIRST || iCommand == Command.PREV)
            || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
        start = ctrlCareerPath.actionList(iCommand, start, vectSize, recordToGet);
    }
    /* end switch list*/

    /* get record to display */
    listCareerPath = PstCareerPath.list(start, recordToGet, whereClause, orderClause);

    /*handle condition if size of record to display = 0 and start > 0 	after delete*/
    if (listCareerPath.size() < 1 && start > 0) {
        if (vectSize - recordToGet > recordToGet) {
            start = start - recordToGet;   //go to Command.PREV
        } else {
            start = 0;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
        }
        listCareerPath = PstCareerPath.list(start, recordToGet, whereClause, orderClause);
    }

    long oidDepartment = 0;
    if (oidEmployee != 0) {
        Employee employee = new Employee();
        try {
            employee = PstEmployee.fetchExc(oidEmployee);
            oidDepartment = employee.getDepartmentId();
        } catch (Exception exc) {
            employee = new Employee();
        }
    }

    if (iCommand == Command.GOTO) {
        frmCareerPath = new FrmCareerPath(request, careerPath);
        frmCareerPath.requestEntityObject(careerPath);
    }

//listSection = PstSection.list(0,500,PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+ " = "+oidDepartment,"SECTION");
I_Dictionary dictionaryD = userSession.getUserDictionary();
%>
<html><!-- #BeginTemplate "/Templates/maintab.dwt" -->
    <head>
        <!-- #BeginEditable "doctitle" -->
        <title>CAREER PATH</title>
        <script language="JavaScript">
            //update by satrya 2013-10-14
            function cmdUpdateDiv(){
                document.frmcareerpath.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }
            function cmdUpdateDep(){
                document.frmcareerpath.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }
            function cmdUpdatePos(){
                document.frmcareerpath.command.value="<%=String.valueOf(Command.GOTO)%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }
            function cmdAdd(){
                document.frmcareerpath.career_path_oid.value="0";
                document.frmcareerpath.command.value="<%=Command.ADD%>";
                document.frmcareerpath.prev_command.value="<%=Command.ADD%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdAsk(oidCareerPath){
                document.frmcareerpath.career_path_oid.value=oidCareerPath;
                document.frmcareerpath.command.value="<%=Command.ASK%>";
                document.frmcareerpath.prev_command.value="<%=prevCommand%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }
            
            function cmdMinta(oid){
                document.getElementById("box_delete").style.visibility="visible";
                document.getElementById("oidcareer").value=oid;
            }
            
            function cmdDetail(oid){
                window.open("../databank/careerpath_detail.jsp?oid="+oid, null, "height=550,width=500,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
            }
            
            function cmdHapus(){
                
                document.frmcareerpath.command.value="<%=Command.DELETE%>";
                document.frmcareerpath.prev_command.value="<%=prevCommand%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdConfirmDelete(oidCareerPath){
                document.frmcareerpath.career_path_oid.value=oidCareerPath;
                document.frmcareerpath.command.value="<%=Command.DELETE%>";
                document.frmcareerpath.prev_command.value="<%=prevCommand%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }
            function cmdSave(){
                document.frmcareerpath.command.value="<%=Command.SAVE%>";
                document.frmcareerpath.prev_command.value="<%=prevCommand%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdEdit(oidCareerPath){
                document.frmcareerpath.career_path_oid.value=oidCareerPath;
                document.frmcareerpath.command.value="<%=Command.EDIT%>";
                document.frmcareerpath.prev_command.value="<%=Command.EDIT%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdCancel(oidCareerPath){
                document.frmcareerpath.career_path_oid.value=oidCareerPath;
                document.frmcareerpath.command.value="<%=Command.EDIT%>";
                document.frmcareerpath.prev_command.value="<%=prevCommand%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdBack(){
                document.frmcareerpath.command.value="<%=Command.BACK%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdUpdateSection(){
                document.frmcareerpath.command.value="<%= Command.GOTO%>";
                document.frmcareerpath.prev_command.value="<%= prevCommand%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdBackEmp(empOID){
                document.frmcareerpath.employee_oid.value=empOID;
                document.frmcareerpath.command.value="<%=Command.EDIT%>";
                document.frmcareerpath.action="employee_edit.jsp";
                document.frmcareerpath.submit();
            }


            function cmdListFirst(){
                document.frmcareerpath.command.value="<%=Command.FIRST%>";
                document.frmcareerpath.prev_command.value="<%=Command.FIRST%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdListPrev(){
                document.frmcareerpath.command.value="<%=Command.PREV%>";
                document.frmcareerpath.prev_command.value="<%=Command.PREV%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdListNext(){
                document.frmcareerpath.command.value="<%=Command.NEXT%>";
                document.frmcareerpath.prev_command.value="<%=Command.NEXT%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }

            function cmdListLast(){
                document.frmcareerpath.command.value="<%=Command.LAST%>";
                document.frmcareerpath.prev_command.value="<%=Command.LAST%>";
                document.frmcareerpath.action="careerpath.jsp";
                document.frmcareerpath.submit();
            }
            function cmdEditDetail2(EmpDocument_oid){
                    window.open("<%=approot%>/masterdata/EmpDocumentDetails.jsp?EmpDocument_oid="+EmpDocument_oid);       
            }
            function fnTrapKD(){
                //alert(event.keyCode);
                switch(event.keyCode)         {
                    case <%=LIST_PREV%>:
                            cmdListPrev();
                        break        ;
                    case <%=LIST_NEXT%>:
                            cmdListNext();
                        break        ;
                    case <%=LIST_FIRST%>:
                            cmdListFirst();
                        break        ;
                    case <%=LIST_LAST%>:
                            cmdListLast();
                        break;
                    default:
                        break;
                    }
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
        
<script type="text/javascript">
    function showHint(str) {
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
            xmlhttp.open("GET", "training.jsp?q=" + str, true);
            xmlhttp.send();
        }
    }
</script>
        
<style type="text/css">
    .tblStyle {border-collapse: collapse;font-size: 11px;}
    .tblStyle td {padding: 3px 5px; border: 1px solid #CCC; }
    .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
    .title_tbl_part {
        font-weight: bold;
        background-color: #EEE; 
        color: #007fba;
    }
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
    .title_part_red {color:#ffffff; background-color: #ff9d9d; border-left: 1px solid #ff0000; padding: 9px 11px;}
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
                background-color: #00a1ec;
                border-radius: 3px;
                font-family: Arial;
                border-radius: 5px;
                color: #EEE;
                font-size: 12px;
                padding: 6px 12px 6px 12px;
                border: solid #007fba 1px;
                text-decoration: none;
            }

            .btn:hover {
                color: #FFF;
                background-color: #007fba;
                text-decoration: none;
                border: 1px solid #007fba;
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
            #box_delete {visibility: hidden;}
        </style>
<link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css">
<script src="<%=approot%>/javascripts/jquery.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>
<script>
    $(function() {
        $( "#datepicker" ).datepicker({ dateFormat: "yy-mm-dd" });
    });

   function pageLoad(){ $(".mydate").datepicker({ dateFormat: "yy-mm-dd" }); }  
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
            <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> <%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <%=dictionaryD.getWord(I_Dictionary.CAREER_PATH) %></span>
        </div>
        <% if (oidEmployee != 0) {%>
            <div class="navbar">
                <ul style="margin-left: 97px">
                    <li class=""> <a href="employee_edit.jsp?employee_oid=<%=oidEmployee%>&prev_command=<%=Command.EDIT%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PERSONAL_DATA)%></a> </li>
                    <li class=""> <a href="familymember.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.FAMILY_MEMBER) %></a> </li>
                    <li class=""> <a href="emplanguage.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.COMPETENCIES) %></a> </li>
                    <li class=""> <a href="empeducation.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EDUCATION) %></a> </li>
                    <li class=""> <a href="experience.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.EXPERIENCE) %></a></li>
                    <li class="active"><%=dictionaryD.getWord(I_Dictionary.CAREER_PATH) %></li>
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
            <form name="frmcareerpath" method ="post" action="careerpath.jsp">
                <input type="hidden" name="command" value="<%=iCommand%>">
                <input type="hidden" name="vectSize" value="<%=vectSize%>">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" id="oidcareer" name="career_path_oid" value="<%=oidCareerPath%>">
                <input type="hidden" name="department_oid" value="<%=oidDepartment%>">
                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                <div class="content-info">
                    <% 
                        Employee employee = new Employee();
                        if(oidEmployee != 0){
                            employee = new Employee();
                            try {
                                employee = PstEmployee.fetchExc(oidEmployee);
                            } catch (Exception exc) {
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
                        <tr> 
                              <td valign="top" style="padding-right: 11px;"><strong>Commencing Date</strong></td>
                              <td valign="top"><%=employee.getCommencingDate()%></td>
                        </tr>
                        <tr> 
                              <td valign="top" style="padding-right: 11px;"><strong>Birth Date</strong></td>
                              <td valign="top"><%=employee.getBirthDate()%></td>
                        </tr>
                        <% Department department = new Department();
                            try {
                                department = PstDepartment.fetchExc(employee.getDepartmentId());
                            } catch (Exception exc) {
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
                    <div id="title-large">Riwayat Jabatan</div>
                    <div id="title-small">Daftar riwayat jabatan karyawan.</div>
                </div>
                <div class="content">
                    <p style="margin-top: 2px"><button class="btn" onclick="cmdAdd()">Tambah Data</button></p>
                    <div id="box_delete">
                    <table>
                        <tr>
                            <td valign="top">
                                <div id="confirm">
                                    <strong>Are you sure to delete item ?</strong> &nbsp;
                                    <button id="btn-confirm" onclick="javascript:cmdHapus()">Yes</button>
                                    &nbsp;<button id="btn-confirm" onclick="javascript:cmdBack()">No</button>
                                </div>
                            </td>
                        </tr>
                    </table>
                    </div>
                    <table class="tblStyle">
                        <%
                            try {
                                if (listCareerPath.size() > 0) {
                        %>
                        <tr>
                            <td colspan="12" class="title_tbl_part">Daftar Riwayat Jabatan</td>
                        </tr>
                        <tr>
                            <td class="title_tbl"><%=dictionaryD.getWord(I_Dictionary.COMPANY)%></td>
                            <td class="title_tbl"><%=dictionaryD.getWord(I_Dictionary.DIVISION)%></td>
                            <td class="title_tbl"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT)%></td>
                            <td class="title_tbl"><%=dictionaryD.getWord("SECTION")%></td>
                            <td class="title_tbl"><%=dictionaryD.getWord(I_Dictionary.POSITION)%></td>
                            <%
                            int SetLocation01 = Integer.valueOf(PstSystemProperty.getValueByName("USE_LOCATION_SET")); 
                            if (SetLocation01 == 1) 
                            {       
                                %>
                                <td class="title_tbl"><%=dictionaryD.getWord("LOCATION")%></td>
                                <%
                            }
                            %>
                            <td class="title_tbl"><%=dictionaryD.getWord("LEVEL")%></td>
                            <td class="title_tbl"><%=dictionaryD.getWord("CATEGORY")%></td>
                            <td class="title_tbl">History From</td>
                            <td class="title_tbl">History To</td>
                            <td class="title_tbl">Grade</td>
                            <td class="title_tbl">View Detail</td>
                            <td class="title_tbl">View Detail Doc</td>
                            <td class="title_tbl">&nbsp;</td>
                        </tr>
                        
                        <%= drawList(listCareerPath, iCommand == Command.SAVE ? careerPath.getOID() : oidCareerPath, dictionaryD)%>
                        
                        <tr>
                            <td colspan="12" class="title_tbl_part">Daftar Riwayat Grade</td>
                        </tr>
                        <%= drawListGrade(listCareerPath, iCommand == Command.SAVE ? careerPath.getOID() : oidCareerPath, dictionaryD)%>
                        <tr>
                            <td colspan="12" class="title_tbl_part">Daftar Riwayat Jabatan dan Grade</td>
                        </tr>
                        <%= drawListCareerNGrade(listCareerPath, iCommand == Command.SAVE ? careerPath.getOID() : oidCareerPath, dictionaryD)%>
                        <%  } else {%>
                        <tr>
                            <td colspan="12">
                                No Career Path available
                            </td>
                        </tr>
                        <% }
                        } catch (Exception exc) {
                            System.out.println(""+exc.toString());
                        }%>
                        <tr>
                            <td colspan="12" class="title_tbl_part">Karir Sekarang</td>
                        </tr>
                        <%= drawListCurrent(employee, listCareerPath, dictionaryD)%>
                    </table>
                    
                   <div class="command">
                <%
                            int cmd = 0;
                            if ((iCommand == Command.FIRST || iCommand == Command.PREV)
                                    || (iCommand == Command.NEXT || iCommand == Command.LAST)) {
                                cmd = iCommand;
                            } else {
                                if (iCommand == Command.NONE || prevCommand == Command.NONE) {
                                    cmd = Command.FIRST;
                                } else {
                                    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidCareerPath == 0)) {
                                        cmd = PstCareerPath.findLimitCommand(start, recordToGet, vectSize);
                                    } else {
                                        cmd = prevCommand;
                                    }
                                }
                            }
                %>
                <% ctrLine.setLocationImg(approot + "/images");
                            ctrLine.initDefault();
                %>
                <%=ctrLine.drawImageListLimit(cmd, vectSize, start, recordToGet)%> 
                   </div>
                   
                   <%if ((iCommand == Command.ADD) || (iCommand == Command.SAVE) && (frmCareerPath.errorSize() > 0) || (iCommand == Command.EDIT) || (iCommand == Command.ASK) || (iCommand == Command.LIST) || (iCommand == Command.GOTO)) {%>
                   <table width="100%" border="0" cellspacing="2" cellpadding="2">
            <tr>
                <td colspan="2">
                    <b class="listtitle">Career Path Editor</b>
                </td>
            </tr>
            <%if(iErrCode == ctrlCareerPath.RSLT_FRM_DATE_IN_RANGE){%>
                <tr>
                    <td colspan="2">
                        <div class="title_part_red"><%=msgString%></div>
                    </td>
                </tr>
            <%}%>
            <tr>
                <td width="100%" colspan="2">
                    <table border="0" cellspacing="2" cellpadding="2" width="100%">
                        <!-- Ari_20110930
                        Menambah Company dan DIVISION{ -->
                        <tr align="left" valign="top">
                            <td valign="top" width="17%">Company </td>
                            <td width="83%">
                               <%   
                                   /*Vector company_value = new Vector(1, 1);
                                    Vector company_key = new Vector(1, 1);

                                    for (int i = 0; i < listCompany.size(); i++) {
                                        Company company = (Company) listCompany.get(i);
                                        company_value.add("" + company.getOID());
                                        company_key.add(company.getCompany());
                                    }
                                    String selComp = "" + careerPath.getCompanyId();
                                    if (careerPath.getCompanyId() == 0) {
                                        selComp = "" + oidHistoryComp;
                                    }*/
Vector company_value = new Vector(1, 1);
Vector company_key = new Vector(1, 1);
String whereCompany = "";
if (!(isHRDLogin || isEdpLogin || isGeneralManager)) {
    whereCompany = PstCompany.fieldNames[PstCompany.FLD_COMPANY_ID] + "='" + emplx.getCompanyId() + "'";
} else {
    company_value.add("0");
    company_key.add("select ...");
}
Vector listComp = PstCompany.list(0, 0, whereCompany, " COMPANY ");
for (int i = 0; i < listComp.size(); i++) {
    Company comp = (Company) listComp.get(i);
    company_key.add(comp.getCompany());
    company_value.add(String.valueOf(comp.getOID()));
}
if(oidHistoryComp==0){
    oidHistoryComp = careerPath.getCompanyId();
}
                                                                                                                                    %>
<%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_COMPANY_ID], "formElemen", null,""+(careerPath.getCompanyId()!=0?careerPath.getCompanyId():oidHistoryComp), company_value, company_key,"onChange=\"javascript:cmdUpdateDiv()\"")%>  
                                                                                                                                    <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_COMPANY_ID)%>
                                                                                                                                </td>
                                                                                                                            </tr>

                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%"><%=dictionaryD.getWord(I_Dictionary.DIVISION)%> </td>
                                                                                                                                <td width="83%">
                                                                                                                                   <%   /*Vector division_value = new Vector(1, 1);
                                                                                                                                        Vector division_key = new Vector(1, 1);
                                                                                                                                        Vector listDivision = PstDivision.list(0, 0, "", "DIVISION");
                                                                                                                                        for (int i = 0; i < listDivision.size(); i++) {
                                                                                                                                            Division division = (Division) listDivision.get(i);
                                                                                                                                            division_value.add("" + division.getOID());
                                                                                                                                            division_key.add(division.getDivision());
                                                                                                                                        }*/
Vector division_value = new Vector(1, 1);
Vector division_key = new Vector(1, 1);
if(careerPath.getDivisionId()!=0){
    oidHistoryDiv=careerPath.getDivisionId();
}
String whereDivision = "";
if (!(isHRDLogin || isEdpLogin || isGeneralManager)) {
    whereDivision = PstDivision.fieldNames[PstDivision.FLD_DIVISION_ID] + "='" + emplx.getDivisionId() + "'";
    oidHistoryDiv = emplx.getDivisionId();
} else {
    division_value.add("0");
    division_key.add("select ...");
}
if(whereDivision!=null && whereDivision.length()>0 && oidHistoryComp!=0){
   whereDivision = whereDivision + " AND "+PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+"="+oidHistoryComp;
}else if(oidHistoryComp!=0){
    whereDivision = PstDivision.fieldNames[PstDivision.FLD_COMPANY_ID]+"="+oidHistoryComp;
}
Vector listDiv = PstDivision.list(0, 0, whereDivision, " DIVISION ");
for (int i = 0; i < listDiv.size(); i++) {
    Division div = (Division) listDiv.get(i);
    division_key.add(div.getDivision());
    division_value.add(String.valueOf(div.getOID()));
}
                                                                                                                                  %>
<%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DIVISION_ID], "formElemen", null, "" +(careerPath.getDivisionId()!=0?careerPath.getDivisionId():oidHistoryDiv), division_value, division_key,"onChange=\"javascript:cmdUpdateDep()\"")%>  <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_DIVISION_ID)%></td>
                                                                                                                            </tr>

                                                                                                                           <!-- } -->
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%">Department
                                                                                                                                </td>
                                                                                                                                <td width="83%">
                                                                                                                                    <%   /*Vector department_value = new Vector(1, 1);
                                                                                                                                        Vector department_key = new Vector(1, 1);

                                                                                                                                        for (int i = 0; i < listDepartment.size(); i++) {
                                                                                                                                            Department department = (Department) listDepartment.get(i);
                                                                                                                                            department_value.add("" + department.getOID());
                                                                                                                                            department_key.add(department.getDepartment());
                                                                                                                                        }

                                                                                                                                        String selDept = "" + careerPath.getDepartmentId();
                                                                                                                                        if (careerPath.getDepartmentId() == 0) {
                                                                                                                                            selDept = "" + oidHistoryDept;
                                                                                                                                       }*/
Vector dept_value = new Vector(1, 1);
Vector dept_key = new Vector(1, 1);
if(careerPath.getDepartmentId()!=0){
    oidHistoryDept=careerPath.getDepartmentId();
}
Vector listDept = new Vector();
Position position = new Position();
if (processDependOnUserDept) {
    if (emplx.getOID() > 0) {
        if (isHRDLogin || isEdpLogin || isGeneralManager) {
            String strWhere = PstDepartment.TBL_HR_DEPARTMENT + "." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidHistoryDiv;
            dept_value.add("0");
            dept_key.add("select ...");
            listDept = PstDepartment.list(0, 0, strWhere, "DEPARTMENT");

        } else {
            position = new Position();
            try {
                position = PstPosition.fetchExc(emplx.getPositionId());
            } catch (Exception exc) {
            }

            String whereClsDep = "(((hr_department.DEPARTMENT_ID = " + departmentOid + ") "
                    + "AND hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidHistoryDiv + ") OR "
                    + "(hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_JOIN_TO_DEPARTMENT_ID] + "=" + departmentOid + "))";

            if (position.getOID() != 0 && position.getDisabedAppDivisionScope() == 0) {
                whereClsDep = " ( hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidHistoryDiv + ") ";
            }

            Vector SectionList = new Vector();
            try {
                String joinDept = PstSystemProperty.getValueByName("JOIN_DEPARMENT");
                Vector depGroup = com.dimata.util.StringParser.parseGroup(joinDept);

                String joinDeptSection = PstSystemProperty.getValueByName("JOIN_DEPARTMENT_SECTION");
                Vector depSecGroup = com.dimata.util.StringParser.parseGroup(joinDeptSection);

                int grpIdx = -1;
                int maxGrp = depGroup == null ? 0 : depGroup.size();

                int grpSecIdx = -1;
                int maxGrpSec = depSecGroup == null ? 0 : depSecGroup.size();

                int countIdx = 0;
                int MAX_LOOP = 10;
                int curr_loop = 0;

                int countIdxSec = 0;
                int MAX_LOOPSec = 10;
                int curr_loopSec = 0;

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

                Vector idxSecGroup = new Vector();

                for (int x = 0; x < maxGrpSec; x++) {

                    String[] grp = (String[]) depSecGroup.get(x);
                    for (int j = 0; j < 1; j++) {

                        String comp = grp[j];
                        if (comp.trim().compareToIgnoreCase("" + departmentOid) == 0) {
                            Counter counter = new Counter();
                            counter.setCounter(x);
                            idxSecGroup.add(counter);
                        }
                    }
                }

                for (int s = 0; s < idxSecGroup.size(); s++) {

                    Counter counter = (Counter) idxSecGroup.get(s);

                    String[] grp = (String[]) depSecGroup.get(counter.getCounter());

                    Section sec = new Section();
                    sec.setDepartmentId(Long.parseLong(grp[0]));
                    sec.setOID(Long.parseLong(grp[2]));
                    SectionList.add(sec);


                }

                // compose where clause
                if (grpIdx >= 0) {
                    String[] grp = (String[]) depGroup.get(grpIdx);
                    for (int g = 0; g < grp.length; g++) {
                        String comp = grp[g];
                        whereClsDep = whereClsDep + " OR (j.DEPARTMENT_ID = " + comp + ")";
                    }
                }
                whereClsDep = " (" + whereClsDep + ") AND hr_department." + PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidHistoryDiv;
            } catch (Exception exc) {
                System.out.println(" Parsing Join Dept" + exc);
            }

            //dept_value.add("0");
            //dept_key.add("select ...");
            listDept = PstDepartment.list(0, 0, whereClsDep, "");

            for (int idx = 0; idx < SectionList.size(); idx++) {

                Section sect = (Section) SectionList.get(idx);

                long sectionOid = 0;

                for (int z = 0; z < listDept.size(); z++) {

                    Department dep = new Department();

                    dep = (Department) listDept.get(z);

                    if (sect.getDepartmentId() == dep.getOID()) {

                        sectionOid = sect.getOID();

                    }
                }

                if (sectionOid != 0) {

                    Section lstSection = new Section();
                    Department lstDepartment = new Department();

                    try {
                        lstSection = PstSection.fetchExc(sectionOid);
                    } catch (Exception e) {
                        System.out.println("Exception " + e.toString());
                    }

                    try {
                        lstDepartment = PstDepartment.fetchExc(lstSection.getDepartmentId());
                    } catch (Exception e) {
                        System.out.println("Exception " + e.toString());
                    }

                    listDept.add(lstDepartment);

                }
            }
        }
    } else {
        dept_value.add("0");
        dept_key.add("select ...");
        listDept = PstDepartment.list(0, 0, (PstDepartment.TBL_HR_DEPARTMENT+"."+PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidHistoryDiv), "DEPARTMENT");
    }
} else {
    dept_value.add("0");
    dept_key.add("select ...");
    listDept = PstDepartment.list(0, 0, (PstDepartment.fieldNames[PstDepartment.FLD_DIVISION_ID] + "=" + oidHistoryDiv), "DEPARTMENT");
}

for (int i = 0; i < listDept.size(); i++) {
    Department dept = (Department) listDept.get(i);
    dept_key.add(dept.getDepartment());
    dept_value.add(String.valueOf(dept.getOID()));
}
                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DEPARTMENT_ID], "formElemen", null, ""+(careerPath.getDepartmentId()!=0?careerPath.getDepartmentId():oidHistoryDept), dept_value, dept_key, "onchange='javascript:cmdUpdateSection()'")%> * <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_DEPARTMENT_ID)%> </td>
                                                                                                                            </tr>
                                                                                                                            
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%"><%=dictionaryD.getWord("SECTION")%>
                                                                                                                                </td>
                                                                                                                                <td width="83%">
                                                                                                                                    <%--  Vector section_value = new Vector(1, 1);
                                                                                                                                        Vector section_key = new Vector(1, 1);

                                                                                                                                        section_value.add("0");
                                                                                                                                        section_key.add("-");

                                                                                                                                        String filter = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] + " = " + selDept;
                                                                                                                                        listSection = PstSection.list(0, 0, filter, "SECTION");

                                                                                                                                        for (int i = 0; i < listSection.size(); i++) {
                                                                                                                                            Section section = (Section) listSection.get(i);
                                                                                                                                            section_key.add(section.getSection());
                                                                                                                                            section_value.add("" + section.getOID());
                                                                                                                                        }

                                                                                                                                    --%>
                                                                                                                                    <%-- if(listSection != null && listSection.size()>0){--%>
                                                                                                                                    <%--= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_SECTION_ID], "formElemen", null, "" + careerPath.getSectionId(), section_value, section_key)--%>
                                                                                                                                    <%-- }else{%>
                                                                                                                                    <font class="comment">No
                                                                                                                                    Section available</font>
                                                                                                                                    <%}--%>
                                                                                                                                    <%--= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_SECTION_ID) --%>

                                                                                                                                    <!-- /** Ari_20110903
                                                                                                                                        /* Memperbaiki Section {-->
                                                                                                                                    <%   
                                                                                                                                    /*Vector section_value = new Vector(1, 1);
                                                                                                                                        Vector section_key = new Vector(1, 1);
                                                                                                                                        Vector listSection = PstSection.list(0, 0, "", "SECTION");
                                                                                                                                        for (int i = 0; i < listSection.size(); i++) {
                                                                                                                                            Section section = (Section) listSection.get(i);
                                                                                                                                            section_value.add("" + section.getOID());
                                                                                                                                            section_key.add(section.getSection());
                                                                                                                                        }*/
 Vector sec_value = new Vector(1, 1);
Vector sec_key = new Vector(1, 1);
sec_value.add("0");
sec_key.add("select ...");
//Vector listSec = PstSection.list(0, 0, "", " DEPARTMENT_ID, SECTION ");
String strWhereSec = PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID] +"="+ oidHistoryDept;
Vector listSec = PstSection.list(0, 0, strWhereSec, " SECTION ");
for (int i = 0; i < listSec.size(); i++) {
    Section sec = (Section) listSec.get(i);
    sec_key.add(sec.getSection());
    sec_value.add(String.valueOf(sec.getOID()));
}

                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_SECTION_ID], "formElemen", null, "" + (careerPath.getSectionId()!=0?careerPath.getSectionId():oidHistorySection), sec_value, sec_key)%>  <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_SECTION_ID)%>
                                                                                                                                    <!-- } -->
                                                                                                                                </td>
                                                                                                                            </tr>
                                                                                                                            <% int SetLocation = 1;
                                                                                                                                 try {
                                                                                                                                 SetLocation =Integer.valueOf(PstSystemProperty.getValueByName("USE_LOCATION_SET")); 
                                                                                                                                 } catch (Exception e){
                                                                                                                                 }
                                                                                                                                 if (SetLocation==1) {
                                                                                                                            %>
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%"><%=dictionaryD.getWord("LOCATION")%>
                                                                                                                                </td>
                                                                                                                                <td width="83%">
                                                                                                                            <%
                                                                                                                                 String CtrOrderClause = PstLocation.fieldNames[PstLocation.FLD_LOCATION_ID];
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
                                                                                                                                 %>
                                                                                                                                <%= ControlCombo.draw(FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_LOCATION_ID], "formElemen", null, "" + careerPath.getLocationId(), val_Location, key_Location)%>  <%=frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_LOCATION_ID)%>                                                                                                                           </td>
                                                                                                                                           
                                                                                                                            </tr>
                                                                                                                            <% } %>
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%"><%=dictionaryD.getWord(I_Dictionary.POSITION)%>
                                                                                                                                </td>
                                                                                                                                <td width="83%">
                                                                                                                                    <% Vector position_value = new Vector(1, 1);
                                                                                                                                        Vector position_key = new Vector(1, 1);
                                                                                                                                        Vector listPosition = PstPosition.list(0, 0, "", "POSITION");
                                                                                                                                        for (int i = 0; i < listPosition.size(); i++) {
                                                                                                                                             position = (Position) listPosition.get(i);
                                                                                                                                            position_value.add("" + position.getOID());
                                                                                                                                            position_key.add(position.getPosition());
                                                                                                                                        }
                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_POSITION_ID], "formElemen", null, "" + careerPath.getPositionId(), position_value, position_key)%> * <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_POSITION_ID)%> </td>
                                                                                                                            </tr>
                                                                                                                            <!-- Ari_20110903
                                                                                                                                Menambah Level dan Emp_Category {
                                                                                                                            -->
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%">Level</td>
                                                                                                                                <td width="83%">
                                                                                                                                    <%   Vector level_value = new Vector(1, 1);
                                                                                                                                        Vector level_key = new Vector(1, 1);
                                                                                                                                        Vector listLevel = PstLevel.list(0, 0, "", "LEVEL");
                                                                                                                                        for (int i = 0; i < listLevel.size(); i++) {
                                                                                                                                            Level level = (Level) listLevel.get(i);
                                                                                                                                            level_value.add("" + level.getOID());
                                                                                                                                            level_key.add(level.getLevel());
                                                                                                                                        }

                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_LEVEL_ID], "formElemen", null, "" + careerPath.getLevelId(), level_value, level_key)%>  <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_LEVEL_ID)%></td>
                                                                                                                            </tr>
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <%=dictionaryD.getWord("CATEGORY")%></td>
                                                                                                                                <td width="83%">
                                                                                                                                    <%   Vector empCategory_value = new Vector(1, 1);
                                                                                                                                        Vector empCategory_key = new Vector(1, 1);
                                                                                                                                        Vector listEmpCategory = PstEmpCategory.list(0, 0, "", "EMP_CATEGORY");
                                                                                                                                        for (int i = 0; i < listEmpCategory.size(); i++) {
                                                                                                                                            EmpCategory empCategory = (EmpCategory) listEmpCategory.get(i);
                                                                                                                                            empCategory_value.add("" + empCategory.getOID());
                                                                                                                                            empCategory_key.add(empCategory.getEmpCategory());
                                                                                                                                        }

                                                                                                                                    %>
                                                                                                                                    <%= ControlCombo.draw(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_EMP_CATEGORY_ID], "formElemen", null, "" + careerPath.getEmpCategoryId(), empCategory_value, empCategory_key)%>  <%= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_EMP_CATEGORY_ID)%></td>
                                                                                                                            </tr>
                                                                                                                            <!--}-->
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%">Work
                                                                                                                                    From</td>
                                                                                                                                <td width="83%"> <%=	ControlDate.drawDateWithStyle(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_WORK_FROM], careerPath.getWorkFrom() == null ? new Date() : careerPath.getWorkFrom(), 10, -55, "formElemen")%> to
                                                                                                                                    <%=	ControlDate.drawDateWithStyle(frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_WORK_TO], careerPath.getWorkTo() == null ? new Date() : careerPath.getWorkTo(), 10, -55, "formElemen")%> *
                                                                                                                                    <% String strStart = frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_WORK_FROM);
                                                                                                                                        String strEnd = frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_WORK_TO);
                                                                                                                                        if ((strStart.length() > 0) && (strEnd.length() > 0)) {
                                                                                                                                    %>
                                                                                                                                    <%= strStart%>
                                                                                                                                    <%} else {
                                                                                                                                                                                                                                                                                if ((strStart.length() > 0) || (strEnd.length() > 0)) {%>
                                                                                                                                    <%= strStart.length() > 0 ? strStart : strEnd%>
                                                                                                                                    <% }
                                                                                                                                        }%>
                                                                                                                                </td>
                                                                                                                            </tr>
                                                                                                                            <!-- Code has been off by Hendra Putu | 2015-11-25
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%">dictionaryD.getWord("SALARY")Grade</td>
                                                                                                                                <td width="83%">
                                                                                                                                    <
                                                                                                                                        NumberFormat format = NumberFormat.getInstance();
                                                                                                                                        format.setGroupingUsed(false);
                                                                                                                                    >
                                                                                                                                    <input type="text" name="<=frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_SALARY]%>" value="<= format.format(careerPath.getSalary())%>"> <= frmCareerPath.getErrorMsg(FrmCareerPath.FRM_FIELD_SALARY)%>
                                                                                                                                </td>
                                                                                                                            </tr>
                                                                                                                            <tr align="left" valign="top">
                                                                                                                                <td valign="top" width="17%"><=dictionaryD.getWord("DESCRIPTION")%></td>
                                                                                                                                <td width="83%">
                                                                                                                                    <textarea name="<=frmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DESCRIPTION]%>" class="elemenForm" cols="30" rows="3"><= careerPath.getDescription()%></textarea>
                                                                                                                                </td>
                                                                                                                            </tr>-->
                                                                                                                            <tr>
                                                                                                                            
                                                        <td valign="top">
                                                            W. A. <%=dictionaryD.getWord(I_Dictionary.PROVIDER)%>
                                                        </td>
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
                                                            <%=  ControlCombo.draw(FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_PROVIDER_ID], "formElemen", null, "" + (careerPath.getProviderID()) /* !=0?careerPath.getProviderID():employee.getProviderID()) */, provValue, provKey) %> * <%= frmCareerPath.getErrorMsg(FrmEmployee.FRM_FIELD_PROVIDER_ID) %>
                                                       </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">History Type</td>
                                                        <td valign="top">
                                                            <select name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_HISTORY_TYPE]%>">
                                                                <%
                                                                for(int t=0; t<PstCareerPath.historyType.length; t++){
                                                                    if (careerPath.getHistoryType() == t){
                                                                        %>
                                                                        <option selected="selected" value="<%=t%>"><%= PstCareerPath.historyType[t] %></option>
                                                                        <%
                                                                    } else {
                                                                        %>
                                                                        <option value="<%=t%>"><%= PstCareerPath.historyType[t] %></option>
                                                                        <%
                                                                    }
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">Nomor SK</td>
                                                        <td valign="top"><input type="text" name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_NOMOR_SK]%>" value="<%= careerPath.getNomorSk() %>" /></td>
                                                    </tr>
                                                    <tr>
                                                        <%
                                                        /* Conversi Date to String */
                                                        String DATE_FORMAT_NOW = "yyyy-MM-dd";
                                                        Date date = careerPath.getTanggalSk();
                                                        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                                                        String stringDate = sdf.format(date );
                                                        %>
                                                        <td valign="top">Tanggal SK</td>
                                                        <td valign="top"><input type="text" class="mydate" name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_TANGGAL_SK]%>" value="<%= stringDate %>" />
                                                         
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">&nbsp</td><!--Emp Doc- -->
                                                        <td valign="top"><input type="hidden" name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_EMP_DOC_ID]%>" value="123" /></td>
                                                    </tr>
                                                    
                                                    <tr>
                                                        <td valign="top">History Group</td>
                                                        <td valign="top">
                                                            <select name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_HISTORY_GROUP]%>">
                                                                <%
                                                                for(int g=0; g<PstCareerPath.historyGroup.length; g++){
                                                                    if (careerPath.getHistoryGroup() == g){
                                                                        %>
                                                                        <option selected="selected" value="<%=g%>"><%= PstCareerPath.historyGroup[g] %></option>
                                                                        <%
                                                                    } else {
                                                                        %>
                                                                        <option value="<%=g%>"><%= PstCareerPath.historyGroup[g] %></option>
                                                                        <%
                                                                    }
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>Grade</td>
                                                        <td>
                                                            <%
                                                            Vector gd_value = new Vector();
                                                            Vector gd_key = new Vector();
                                                            gd_value.add("0");
                                                            gd_key.add("SELECT");
                                                            Vector listGradeLevel = PstGradeLevel.listAll(); 
                                                            for (int i = 0; i < listGradeLevel.size(); i++) {
                                                                GradeLevel gradeLevel = (GradeLevel) listGradeLevel.get(i);
                                                                gd_key.add(gradeLevel.getCodeLevel());
                                                                gd_value.add(String.valueOf(gradeLevel.getOID()));
                                                            }
                                                            %>
                                                            <%= ControlCombo.draw(FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_GRADE_LEVEL_ID], "formElemen", null, "" + careerPath.getGradeLevelId(), gd_value, gd_key)%>
                                                        </td>
                                                    </tr>
                                                    
                                                    <tr>
                                                        <td colspan="2">
                                                            <button class="btn" onclick="cmdSave()">Save</button>&nbsp;<button class="btn" onclick="cmdBack()">Batal</button>
                                                        </td>
                                                    </tr>
                                                                                                       
                                                  </table>
                </td>
            </tr>
                   </table>
                   <% } %>
                   
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