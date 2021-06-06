<%-- 
    Document   : training_hr
    Created on : Feb 5, 2009, 9:30:21 AM
    Author     : bayu 
--%>

<%@page import="java.text.SimpleDateFormat"%>
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
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.form.employee.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>

<%@ include file = "../../main/javainit.jsp" %>
<% 
    int appObjCodeGen = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_TRAINING, AppObjInfo.OBJ_GENERAL_TRAINING); 
    int appObjCodeDept = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_TRAINING, AppObjInfo.OBJ_DEPARTMENTAL_TRAINING); 
    int appObjCode = 0; 
    
    // check training privilege (0 = none, 1 = general, 2 = departmental)
    int trainType = checkTrainingType(appObjCodeGen, appObjCodeDept, userSession);
    
    if(trainType == PRIV_GENERAL) {    
        appObjCode = appObjCodeGen;
    }
    else if(trainType == PRIV_DEPT) {  
        appObjCode = appObjCodeDept;
    }

    boolean privView = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));
    boolean privAdd = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
    boolean privUpdate = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
    boolean privDelete = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
    boolean privPrint = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_PRINT));
%>
<%@ include file = "../../main/checktraining.jsp" %>

<%!
    public String drawList(Vector objectClass, long trainHistoryId) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("0");
        ctrlist.setListStyle("tblStyle");
        ctrlist.setTitleStyle("title_tbl");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("title_tbl");
        ctrlist.setCellSpacing("0");
        ctrlist.addHeader("Training Program", "");
        ctrlist.addHeader("Trainer", "");
        ctrlist.addHeader("Start date", "");
        ctrlist.addHeader("End date", "");
        ctrlist.addHeader("Duration", "");
        ctrlist.addHeader("Judul Pelatihan", "");
        /*
        ctrlist.addHeader("Nomor SK", "");
        ctrlist.addHeader("Tanggal SK", "");*/
        ctrlist.addHeader("&nbsp;", "");
        ctrlist.setLinkRow(0);
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
        for (int i = 0; i < objectClass.size(); i++) {
            TrainingHistory trainHistory = (TrainingHistory)objectClass.get(i);
            Vector rowx = new Vector();
            if (trainHistoryId == trainHistory.getOID()) {
                index = i;
            }
            
            String trainingName = ""+trainHistory.getTrainingProgram();
            if (trainingName.length()==0 || trainingName.equals("NULL")){
                // ambil training name//
                try {
                    Training train = PstTraining.fetchExc(trainHistory.getTrainingId());
                    trainingName = train.getName();
                } catch(Exception e){
                    System.out.println(""+e.toString());
                }
            }
            
            rowx.add(trainingName);
            rowx.add(trainHistory.getTrainer());
            rowx.add(""+trainHistory.getStartDate());
            rowx.add(""+trainHistory.getEndDate());
            rowx.add(""+trainHistory.getDuration());
            rowx.add(""+trainHistory.getRemark());
            /*
            rowx.add(trainHistory.getNomorSk());
            rowx.add(""+trainHistory.getTanggalSk());
            */
            rowx.add("<button class=\"btn-small\" onclick=\"cmdAsk('" + trainHistory.getOID() + "')\">&times;</button>");
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(trainHistory.getOID()));
        }

        return ctrlist.draw(index);
    }
%>
<%
    int iCommand = FRMQueryString.requestCommand(request);
    int prevCommand = FRMQueryString.requestInt(request, "prev_command");
    long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
    long oidTrainingHistory = FRMQueryString.requestLong(request, "hidden_training_history_id");
    int start = FRMQueryString.requestInt(request, "start");
    
    // test
    int test = FRMQueryString.requestInt(request, "q");
        
    long oidDept = FRMQueryString.requestLong(request, "department_id");
    long oidTraining = FRMQueryString.requestLong(request, "training_id");
    long oidTrainingPlan = FRMQueryString.requestLong(request, "training_plan_id");
    long oidTrainingActivityActual = FRMQueryString.requestLong(request, "training_actual_id");
    long oidTrainingSchedule = FRMQueryString.requestLong(request, "training_schedule_id");
    int hour = SessTraining.getTrainingDuration(FRMQueryString.requestString(request, "training_hour"));
        
    int recordToGet = 10;
    int vectSize = 0;
    int iErrCode = FRMMessage.ERR_NONE;
    String errMsg = "";
        
    Employee employee = new Employee();
    long oidPosition = 0;
    Position position = new Position();
    long oidDepartment = 0;
    Department department = new Department();
    if (iCommand == Command.ADD) {
        try {
            session.removeValue("oidEmployee");
        } catch (Exception e) {
            System.out.println("Exc when remove from session(\"oidEmployee\") : " + e.toString());
        }
            
        try {
            session.putValue("oidEmployee", oidEmployee);
                
        } catch (Exception e) {
            System.out.println("Exc when put to session(\"oidEmployee\") : " + e.toString());
        }
    }
    try {
        employee = PstEmployee.fetchExc(oidEmployee);
        oidPosition = employee.getPositionId();
        position = PstPosition.fetchExc(oidPosition);
        oidDepartment = employee.getDepartmentId();
        department = PstDepartment.fetchExc(oidDepartment);
    } catch (Exception exc) {
        System.out.println("exc error training.jsp" + exc);
        employee = new Employee();
        position = new Position();
        department = new Department();
    }
        
        
    if (iCommand == Command.LIST) {
        Vector vctHeaderPrint = new Vector(1, 1);
        vctHeaderPrint.add(employee);
        vctHeaderPrint.add(position);
        vctHeaderPrint.add(department);
        session.putValue("HEADER_BUF", vctHeaderPrint);
    }
        
    if (iCommand == Command.LIST) {
        Vector vctHeaderPrint = new Vector(1, 1);
        vctHeaderPrint.add(employee);
        vctHeaderPrint.add(position);
        vctHeaderPrint.add(department);
        session.putValue("HEADER_BUF", vctHeaderPrint);
    }
        
        
    String whereClause = "";
    String orderClause = "";
        
        
    //------------------ end of employee process -----------------------
        
        
    CtrlTrainingHistory ctrlTrainingHistory = new CtrlTrainingHistory(request);
    ControlLine ctrLine = new ControlLine();
        
    whereClause = PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_EMPLOYEE_ID] + "=" + oidEmployee;
    orderClause = PstTrainingHistory.fieldNames[PstTrainingHistory.FLD_START_DATE];
        
    FrmTrainingHistory frmTrainingHistory = new FrmTrainingHistory();
    TrainingHistory trainingHistory = new TrainingHistory();

    iErrCode = ctrlTrainingHistory.action(iCommand, oidTrainingHistory, request, emplx.getFullName(), appUserIdSess);
    frmTrainingHistory = ctrlTrainingHistory.getForm();
    trainingHistory = ctrlTrainingHistory.getTrainingHistory();
    vectSize = PstTrainingHistory.getCount(whereClause);
    
    if(iCommand == Command.GOTO) {
        TrainingActivityActual actual = new TrainingActivityActual();          
        trainingHistory = new TrainingHistory();
        try {
            actual = PstTrainingActivityActual.fetchExc(oidTrainingActivityActual);
            trainingHistory.setStartDate(actual.getDate());
            trainingHistory.setEndDate(actual.getDate());
            trainingHistory.setStartTime(actual.getStartTime());
            trainingHistory.setEndTime(actual.getEndTime());               
            trainingHistory.setDuration(hour);
            trainingHistory.setEmployeeId(oidEmployee);
            trainingHistory.setRemark(actual.getRemark());
            trainingHistory.setTrainer(actual.getTrainner());
            trainingHistory.setTrainingActivityActualId(oidTrainingActivityActual);
            trainingHistory.setTrainingActivityPlanId(oidTrainingPlan);
            trainingHistory.setTrainingId(oidTraining);
        }
        catch(Exception e) {}         
     }
        
    if (iCommand == Command.SAVE && prevCommand == Command.ADD) {
        start = PstTrainingHistory.findLimitStart(oidTrainingHistory, recordToGet, whereClause, orderClause);
    }
        
    if ((iCommand == Command.FIRST) || (iCommand == Command.NEXT) || (iCommand == Command.PREV)
            || (iCommand == Command.LAST) || (iCommand == Command.LIST)) {
        start = ctrlTrainingHistory.actionList(iCommand, start, vectSize, recordToGet);
    }
        
    Vector vctTrHistory = PstTrainingHistory.list(start, recordToGet, whereClause, orderClause);
    /**
    * Description : berfungsi utk update data training history, dimana remark dan trainingProgram = empty
    * Update by : Hendra Putu
    * Date : 2015-11-24
    */
    Vector listTrHistory = PstTrainingHistory.list(0, 0, whereClause, orderClause);
    if (listTrHistory != null && listTrHistory.size()>0){
        for (int i = 0; i < listTrHistory.size(); i++) {
            TrainingHistory trainHistory = (TrainingHistory)listTrHistory.get(i);
            String trainingName = ""+trainHistory.getTrainingProgram();
            if (trainingName.length()==0){
                // ambil training name//
                try {
                    Training train = PstTraining.fetchExc(trainHistory.getTrainingId());
                    trainingName = train.getName();
                } catch(Exception e){
                    System.out.println(""+e.toString());
                }
            }
            if ((trainHistory.getRemark().equals(""))){
                // ambil data hr_training_activity_actual
                try {
                    TrainingActivityActual trainAct = PstTrainingActivityActual.fetchExc(trainHistory.getTrainingActivityActualId());
                    TrainingHistory trHistory = new TrainingHistory();
                    trHistory.setOID(trainHistory.getOID());
                    trHistory.setEmployeeId(trainHistory.getEmployeeId());
                    trHistory.setTrainingProgram(trainingName);
                    trHistory.setStartDate(trainHistory.getStartDate());
                    trHistory.setEndDate(trainHistory.getEndDate());
                    trHistory.setTrainer(trainHistory.getTrainer());
                    trHistory.setRemark(trainAct.getRemark());
                    trHistory.setTrainingId(trainHistory.getTrainingId());
                    trHistory.setDuration(trainHistory.getDuration());
                    trHistory.setPresence(trainHistory.getPresence());
                    trHistory.setStartTime(trainHistory.getStartTime());
                    trHistory.setEndTime(trainHistory.getEndTime());
                    trHistory.setTrainingActivityPlanId(trainHistory.getTrainingActivityPlanId());
                    trHistory.setTrainingActivityActualId(trainHistory.getTrainingActivityActualId());
                    trHistory.setPoint(trainHistory.getPoint());
                    trHistory.setNomorSk(trainHistory.getNomorSk());
                    trHistory.setTanggalSk(trainHistory.getTanggalSk());
                    trHistory.setEmpDocId(trainHistory.getEmpDocId());
                    PstTrainingHistory.updateExc(trHistory);
                } catch(Exception e){
                    System.out.println(""+e.toString());
                }
            }
        }
    }
    
    /* deskripsi test */
    String strQ = "No Data";
    if (test > 0){
        
        switch(test){
            case 1: strQ = "Satu"; break;
            case 2: strQ = "Dua"; break;
        }
        %>
        <%=strQ%>
        <%
    }
        
%>
<!-- End of Jsp Block -->
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Training</title>
<script language="JavaScript">
    function cmdTraining(){
        window.open("../databank/training_program_browse.jsp", null, "height=550,width=700,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    }
        function cmdActual() {               
                window.open("../training/input_actual.jsp?prev=<%=prevCommand%>", null, "height=550,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");     
	}
         
	function cmdBackToEmployee(){
		document.fredit.command.value="<%=Command.BACK%>";
		document.fredit.start.value="0";
		document.fredit.action="employee_list_hr.jsp";		
		document.fredit.submit();
	}


	function cmdCancel(){
		document.fredit.command.value="<%=Command.CANCEL%>";
		document.fredit.action="training.jsp";
		document.fredit.submit();
	} 

	function cmdEdit(oid, oidPlan, oidActual){
		document.fredit.hidden_training_history_id.value=oid;
                document.fredit.training_plan_id.value=oidPlan;
                document.fredit.training_actual_id.value=oidActual;
		document.fredit.command.value="<%=Command.EDIT%>";
		document.fredit.prev_command.value="<%=Command.EDIT%>";
		document.fredit.action="training.jsp";
		document.fredit.submit(); 
	} 

	function cmdAdd(){		
		document.fredit.command.value="<%=Command.ADD%>";	
		document.fredit.action="training.jsp";
		document.fredit.submit(); 
	} 


	function cmdSave(){
		document.fredit.command.value="<%=Command.SAVE%>"; 
		document.fredit.action="training.jsp";
		document.fredit.submit();
	}

	function cmdAsk(oid){
		document.fredit.command.value="<%=Command.ASK%>"; 
                document.fredit.hidden_training_history_id.value = oid;
		document.fredit.action="training.jsp";
		document.fredit.submit();
	} 

	function cmdConfirmDelete(oid){
		document.fredit.command.value="<%=Command.DELETE%>";
                document.fredit.hidden_training_history_id.value = oid;
		document.fredit.action="training.jsp"; 
		document.fredit.submit();
	}  

	function cmdBack(){
		document.fredit.command.value="<%=Command.BACK%>"; 
		document.fredit.action="training.jsp";
		document.fredit.submit();
	}
	
	function cmdBackEmp(empOID){
                document.fredit.employee_oid.value=empOID;
                document.fredit.command.value="<%=Command.EDIT%>";	
                document.fredit.action="employee_edit.jsp";
                document.fredit.submit();
	}
	
	
	
	function cmdListFirst(){
		document.fredit.command.value="<%=Command.FIRST%>";
                document.fredit.prev_command.value="<%=Command.FIRST%>";	
		document.fredit.action="training.jsp";
		document.fredit.submit();
	}

	function cmdListPrev(){
		document.fredit.command.value="<%=Command.PREV%>";
                document.fredit.prev_command.value="<%=Command.PREV%>";
		document.fredit.action="training.jsp";
		document.fredit.submit();
	}

	function cmdListNext(){
		document.fredit.command.value="<%=Command.NEXT%>";
                document.fredit.prev_command.value="<%=Command.NEXT%>";
		document.fredit.action="training.jsp";
		document.fredit.submit();
	}

	function cmdListLast(){
		document.fredit.command.value="<%=Command.LAST%>";
                document.fredit.prev_command.value="<%=Command.LAST%>";
		document.fredit.action="training.jsp";
		document.fredit.submit();
	}
	
		
	function cmdPrint(){
		window.open("training_hist_buffer.jsp?employee_oid=<%=oidEmployee%>&approot=<%=approot%>","reportPage","height=600,width=800,status=no,toolbar=no,menubar=no,location=no"); 
	}
	
	function cmdView(trainingId){
                window.open("../training/list_training_material.jsp?hidden_training_id=" + trainingId,"trainingmaterial" , "height=600,width=800,status=no,toolbar=no,menubar=no,location=no");

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
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
<SCRIPT language=JavaScript>
    function hideObjectForEmployee(){
    	//document.fredit.EMP_DEPARTMENT.style.visibility = "hidden";
    } 
	 
    function hideObjectForLockers(){ 
    }
	
    function hideObjectForCanteen(){
    }
	
    function hideObjectForClinic(){
    }

    function hideObjectForMasterdata(){
    	//document.fredit.EMP_DEPARTMENT.style.visibility = "hidden";
    }
	
    function showObjectForMenu(){
        //document.fredit.EMP_DEPARTMENT.style.visibility = "";
    }
</SCRIPT>
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
                padding: 5px 11px 5px 11px;
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
            
        </style>
<link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css">
<script src="<%=approot%>/javascripts/jquery.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>
<script>
$(function() {
    $( "#datepicker1" ).datepicker({ dateFormat: "yy-mm-dd" });
    $( "#datepicker2" ).datepicker({ dateFormat: "yy-mm-dd" });
    $( "#datepicker3" ).datepicker({ dateFormat: "yy-mm-dd" });
});
</script>
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
            <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> Pelatihan</span>
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
                    <li class="active"> <a href="training.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.TRAINING_ON_DATABANK)%></a> </li>
                    <li class=""> <a href="warning.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.WARNING) %></a> </li>
                    <li class=""> <a href="reprimand.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.REPRIMAND) %></a> </li>
                    <li class=""> <a href="award.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.AWARD) %></a> </li>
                    <li class=""> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PICTURE) %></a> </li>
                    <li class=""> <a href="doc_relevant.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></a> </li>
                </ul>
            </div>
        <%}%>
        <div class="content-main">
            <form name="fredit" method="post" action="">
                <input type="hidden" name="command" value="<%=iCommand%>">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">
                <input type="hidden" name="hidden_training_history_id" value="<%=oidTrainingHistory%>">
                <input type="hidden" name="<%=FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_EMPLOYEE_ID]%>" value="<%=oidEmployee%>">

                <input type="hidden" name="department_id" value="<%=oidDept%>">
                <input type="hidden" name="training_id" value="<%=oidTraining%>">
                <input type="hidden" name="training_plan_id" value="<%=oidTrainingPlan%>">
                <input type="hidden" name="training_actual_id" value="<%=oidTrainingActivityActual%>">  
                <input type="hidden" name="training_schedule_id" value="<%=oidTrainingSchedule%>">
                <div class="content-info">
                    <% 
                        if(oidEmployee != 0){

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
                    <div id="title-large">Training History</div>
                    <div id="title-small">Daftar pelatihan yang pernah diikuti.</div>
                </div>
                <div class="content">
                    <p style="margin-top: 2px"><button class="btn" onclick="cmdAdd()">Tambah Data</button></p>
                    <%
                    if (iCommand == Command.ASK){
                    %>
                    <table>
                        <tr>
                            <td valign="top">
                                <div id="confirm">
                                    <strong>Are you sure to delete item ?</strong> &nbsp;
                                    <button id="btn-confirm" onclick="javascript:cmdConfirmDelete('<%=oidTrainingHistory%>')">Yes</button>
                                    &nbsp;<button id="btn-confirm" onclick="javascript:cmdBack()">No</button>
                                </div>
                            </td>
                        </tr>
                    </table>
                    <%
                    }
                    if (vctTrHistory != null && vctTrHistory.size()>0){
                        %>
                        <%= drawList(vctTrHistory, oidTrainingHistory) %>
                        <%
                    } else {
                        %>
                        <p>No record available</p>
                        <%
                    }
                    %>
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
                                    if ((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidTrainingHistory == 0)) {
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
                    <% if ((iCommand == Command.ADD || iCommand == Command.EDIT)|| iErrCode!=FRMMessage.NONE || iCommand == Command.GOTO){ %>
                    <table cellpadding="0" cellspacing="0">
                        <tr>
                            <td valign="top">
                                <div class="form-style">
                                    <div class="form-title">Form Training</div>
                                    <div class="form-content">
                                        <div id="caption">Training Program</div>
                                        <%
                                        Training trainName = new Training();
                                        String trainNameStr = "-";
                                        try {
                                            trainName = PstTraining.fetchExc(trainingHistory.getTrainingId());
                                            trainNameStr = trainName.getName();
                                        } catch(Exception e){
                                            System.out.println(""+e.toString());
                                        }
                                        
                                        %>
                                        <div>
                                            <p>
                                            <a class="btn-small" href="javascript:cmdTraining()">Browse Training Program</a>&nbsp;
                                            <a class="btn-small" href="javascript:cmdActual()()">Browse From Training Activity</a>
                                            </p>
                                        </div>
                                        <div id="div_input">
                                            <input type="text" name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_TRAINING_PROGRAM] %>" value="<%= trainNameStr %>" size="50" />
                                            <input type="hidden" name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_TRAINING_ID] %>" value="<%=trainingHistory.getTrainingId()%>" />
                                        </div>
                                        <div id="caption">Trainer</div>
                                        <div id="div_input">
                                            <input type="text" name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_TRAINER] %>" value="<%= trainingHistory.getTrainer() %>" size="50" />
                                        </div>
                                        <div id="caption">Start date - End date</div>
                                        <div id="div_input">
                                            <%
                                            /* Conversi Date to String */
                                            String DATE_FORMAT_NOW = "yyyy-MM-dd";
                                            Date dateStart = trainingHistory.getStartDate();
                                            Date dateEnd = trainingHistory.getEndDate();
                                            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                                            String stringDateStart = "";
                                            String stringDateEnd = "";
                                            
                                            try{
                                                 stringDateStart = sdf.format(dateStart);
                                                 stringDateEnd = sdf.format(dateEnd);
                                            } catch (Exception e){
                                                
                                            }
                                            %>
                                            <input type="text" id="datepicker1" name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_START_DATE] %>" value="<%= stringDateStart %>" /> - 
                                            <input type="text" id="datepicker2" name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_END_DATE] %>" value="<%= stringDateEnd %>" />
                                        </div>
                                        <div id="caption">Start time - End time</div>
                                        <div id="div_input">
                                            <%=ControlDate.drawTime(FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_START_TIME], (trainingHistory.getStartTime() == null) ? new Date() : trainingHistory.getStartTime(), "formElemen")%> To 
                                            <%=ControlDate.drawTime(FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_END_TIME], (trainingHistory.getEndTime() == null) ? new Date() : trainingHistory.getEndTime(), "formElemen")%>
                                        </div>
                                        <div id="caption">Duration</div>
                                        <div id="div_input">
                                            <input type="text" name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_DURATION] %>" value="<%= trainingHistory.getDuration() %>" />
                                        </div>
                                        <div id="caption">Judul Pelatihan</div>
                                        <div id="div_input">
                                            <textarea name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_REMARK] %>"><%=trainingHistory.getRemark() %></textarea>
                                        </div>
                                        <div id="caption">Point</div>
                                        <div id="div_input">
                                            <input type="text" name="<%= FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_POINT] %>" value="<%= trainingHistory.getPoint() %>" />
                                        </div>
                                        <!--
                                        <div id="caption">Nomor SK</div>
                                        <div id="div_input">
                                            <input type="text" name="< FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_NOMOR_SK] %>" value="< trainingHistory.getNomorSk() %>" />
                                        </div>
                                        <div id="caption">Tanggal SK</div>
                                        <div id="div_input">
                                            <input type="text" id="datepicker3" name="< FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_TANGGAL_SK] %>" value="< trainingHistory.getTanggalSk() %>" />
                                        </div>
                                        <div id="caption">Emp Doc</div>
                                        <div id="div_input">
                                            <select name="< FrmTrainingHistory.fieldNames[FrmTrainingHistory.FRM_FIELD_EMP_DOC_ID] %>">
                                                <option value="0">-SELECT-</option>
                                                <!
                                                Vector listDoc = PstEmpDoc.list(0, 0, "", "");
                                                if (listDoc != null && listDoc.size()>0){
                                                    for(int i=0; i<listDoc.size(); i++){
                                                        EmpDoc empDoc = (EmpDoc)listDoc.get(i);
                                                        if (trainingHistory.getEmpDocId()==empDoc.getOID()){
                                                            %>
                                                            <option selected="selected" value="<!=empDoc.getOID()%>"><! empDoc.getDoc_title() %></option>
                                                            <!
                                                        } else {
                                                            %>
                                                            <option value="<!=empDoc.getOID()%>"><!= empDoc.getDoc_title() %></option>
                                                            <!
                                                        }
                                                    }
                                                }
                                                %>
                                            </select>
                                        </div>
                                        -->
                                    </div>
                                    <div class="form-footer">
                                        <button class="btn" onclick="cmdSave()">Save</button>
                                        <button class="btn" onclick="cmdBack()">Close</button>
                                    </div>
                                </div>
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