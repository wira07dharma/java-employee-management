
<%@page import="com.dimata.harisma.entity.log.PstLogSysHistory"%>
<%@page import="com.dimata.harisma.entity.log.LogSysHistory"%>
<%@page import="com.dimata.harisma.session.payroll.PayProcess"%>
<%@page import="com.dimata.harisma.session.payroll.PayProcessManager"%>
<%@ page language="java" %>
<%@ page import = "java.util.*" %>
<%@ page import = "java.text.*" %>
<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<%@ page import = "java.util.Date" %>

<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.payroll.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>
<%@ page import = "com.dimata.harisma.form.payroll.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.attendance.*" %>


<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_PROCESS, AppObjInfo.OBJ_PAYROLL_PROCESS_PROCESS);%>
<%@ include file = "../../main/checkuser.jsp" %>

<%
            CtrlPaySlipComp ctrlPaySlipComp = new CtrlPaySlipComp(request);
            int prevCommand = FRMQueryString.requestInt(request, "prev_command");
            int iCommand = FRMQueryString.requestCommand(request);
            int start = FRMQueryString.requestInt(request, "start");
            String levelCode = FRMQueryString.requestString(request, "level");            
            //levelCode = levelCode!=null && levelCode.trim().length()==0 ? "ALL" : "";
            int aksiCommand = FRMQueryString.requestInt(request, "aksiCommand");
            long periodId = FRMQueryString.requestLong(request, "periodId");
            String periodName = FRMQueryString.requestString(request, "periodName");
            long payGroupId = FRMQueryString.requestLong(request,"payGroupId");       

            long sectionId = FRMQueryString.requestLong(request,"sectionId"); 
            long payrollGroupId = FRMQueryString.requestLong(request,"payrollGroupId");                          
            long oidDivision = FRMQueryString.requestLong(request,"division");
            long oidDepartment = FRMQueryString.requestLong(request,"department");
            String departmentName=FRMQueryString.requestString(request, "departmentName");
            String empNum = FRMQueryString.requestString(request, "emp_num");

            Date rsgn_startdate = FRMQueryString.requestDateYYYYMMDD(request,"rsgn_startdate", "-");
            Date rsgn_enddate   = FRMQueryString.requestDateYYYYMMDD(request,"rsgn_enddate", "-");
            Date min_commencing = FRMQueryString.requestDateYYYYMMDD(request,"min_commencing", "-");             


%>
<%
            /*mengambil nama period saat ini
            Updated By Yunny*/
            /*Date now = new Date();
            long periodId = PstPeriod.getPeriodIdBySelectedDate(now);*/
//out.println("PeriodId:::::"+periodId);
%>


<%
            System.out.println("iCommand::::" + iCommand);
            int iErrCode = FRMMessage.ERR_NONE;
            String msgString = "";
            String msgStr = "";
            int recordToGet = 1000;
            int vectSize = 0;
            String orderClause = "";
            String whereClause = "";
            ControlLine ctrLine = new ControlLine();

// action on object agama defend on command entered
            FrmPaySlipComp frmPaySlipComp = ctrlPaySlipComp.getForm();
            PaySlipComp paySlipComp = ctrlPaySlipComp.getPaySlipComp();
            msgString = ctrlPaySlipComp.getMessage();

                    String[] levelId = null;
                    levelId = new String[PstPosition.strPositionLevelInt.length];
                    Vector levelSel= new Vector();
                    int max1 = 0;

                    for(int j = 0 ; j < PstPosition.strPositionLevelInt.length ; j++){                
                        String name = "LEVL_"+PstPosition.strPositionLevelInt[j];
                        String val = FRMQueryString.requestString(request,name);
                        levelId[j] = val;
                        if(val!=null && val.equals("1")){ 
                           levelSel.add(""+PstPosition.strPositionLevelInt[j]); 
                        }
                        max1++;
                    }                        
%>
<%
// proses calculate            
          if (iCommand == Command.START ) {
                    try{
                        PayPeriod payPeriod = PstPayPeriod.fetchExc(periodId);
                        // Period period = PstPeriod.fetchExc(periodId);
                        periodName = payPeriod.getPeriod();
                    }catch(Exception exc){
                    }
                    try{
                        Department department = PstDepartment.fetchExc(oidDepartment);
                        departmentName = department.getDepartment();
                    }catch(Exception exc){
                    }
					
					String payGroupName = "";
					try {
						PaySlipGroup payGroup = PstPaySlipGroup.fetchExc(payGroupId);
						payGroupName = payGroup.getGroupName();
					} catch (Exception exc){}
                    
                    //bersihkan query dulu
                    PstPaySlip.deletePayslipwithoutComp(periodId);
                    
                    Vector listEmpLevel = new Vector(1, 1);                    
                    listEmpLevel = PstPayEmpLevel.listEmpLevelX(levelCode, levelSel,oidDivision, oidDepartment, payrollGroupId,periodId,sectionId );
                    
					String jsonProccess = "{ \"periode\" : \""+periodName+"\", \"group\" : \"" +payGroupName
							+ "\", \"employee\" : [";
					for (int p = 0; p < listEmpLevel.size(); p++) {

						Vector temp = (Vector) listEmpLevel.get(p);
						Employee emp = (Employee) temp.get(0);
						PayEmpLevel payEmpLevel = (PayEmpLevel) temp.get(1);
						SalaryLevel salary = (SalaryLevel) temp.get(2);
						long empId = emp.getOID();
						
						if (p==0){
							jsonProccess += "{payroll : \""+emp.getEmployeeNum()+"\", name : \""+emp.getFullName()+"\"}";
						} else {
							jsonProccess += ", {payroll : \""+emp.getEmployeeNum()+"\", name : \""+emp.getFullName()+"\"}";
						}
					}
					jsonProccess += "]}";
					
					LogSysHistory logHist = new LogSysHistory();
					logHist.setLogDocumentId(0);
					logHist.setLogUserId(userSession.getAppUser().getOID());
					logHist.setLogLoginName(userSession.getAppUser().getLoginId());
					logHist.setLogDocumentNumber("-");
					logHist.setLogDocumentType("");
					logHist.setLogUserAction("Run Process");
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
					
					PayProcessManager payProcessMan =  PayProcessManager.getInstance(periodId, levelCode,oidDivision, oidDepartment, levelSel, payGroupId, payrollGroupId,sectionId, empNum);
                    PayProcess process =  payProcessMan.getProcess();
                    process.setMinCommencing(min_commencing);
                    process.setRsgnEnddate(rsgn_enddate);
                    process.setRsgnStartdate(rsgn_startdate);
                    payProcessMan.startPayrollProcess(); 
					
					
					
                } else {
                    if(iCommand == Command.STOP){                        
                      if(PayProcessManager.isRunning()){
                       PayProcessManager.stopPayrollProcess();
                      }
                   } 
               }
            

iCommand = Command.EDIT;
%>		


<!-- End of JSP Block -->
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
    <!-- #BeginEditable "doctitle" --> 
    <title>HARISMA - Run Pay Process</title>
    <!-- #EndEditable --> 
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <!-- #BeginEditable "styles" --> 
    <link rel="stylesheet" href="../../styles/main.css" type="text/css">
    <!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
    <link rel="stylesheet" href="../../styles/tab.css" type="text/css">
    <!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
    <SCRIPT language=JavaScript>
        
        function fnTrapKD(){
            if (event.keyCode == 13) {
                document.all.aSearch.focus();
                cmdSearch();
            }
        }
        
        
        function cmdStartCalculate(){
            document.frm_prepare_data.command.value="<%=Command.NONE %>";
            document.frm_prepare_data.action="pay-process.jsp";
            document.frm_prepare_data.submit();
        }

        function cmdStopCalculate(){
            document.frm_prepare_data.command.value="<%=Command.STOP %>";
            document.frm_prepare_data.action="run-pay-process.jsp";
            document.frm_prepare_data.submit();
        }

        
        function cmdBack(){
            document.frm_prepare_data.command.value="<%=Command.LIST%>";
            document.frm_prepare_data.action="pay-process.jsp";
            document.frm_prepare_data.submit();
        }
        
        
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
        
        function showObjectForMenu(){
            
        }
    </SCRIPT>
    <!-- #EndEditable --> 
</head>
<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >

     <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
  <tr> 
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
      <%@ include file = "../../main/header.jsp" %> </td>
  </tr>
  <tr> 
    <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <%@ include file = "../../main/mnmain.jsp" %> </td>
  </tr>
  <tr> 
    <td  bgcolor="#9BC1FF" height="10" valign="middle"> <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
          <td align="left"><img src="<%=approot%>/images/harismaMenuLeft1.jpg" width="8" height="8"></td>
          <td align="center" background="<%=approot%>/images/harismaMenuLine1.jpg" width="100%"><img src="<%=approot%>/images/harismaMenuLine1.jpg" width="8" height="8"></td>
          <td align="right"><img src="<%=approot%>/images/harismaMenuRight1.jpg" width="8" height="8"></td>
        </tr>
      </table></td>
  </tr>
  <%}%>
<tr> 
<td width="88%" valign="top" align="left"> 
    <table width="100%" border="0" cellspacing="3" cellpadding="2">
    <tr> 
        <td width="100%"> 
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
        <tr> 
            <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->Payroll 
            Process <!-- #EndEditable --> </strong></font> </td>
        </tr>
        <tr> 
            <td> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr> 
                <td style="background-color:<%=bgColorContent%>; "> 
                <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" >
                <tr> 
                <td valign="top"> 
                <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                <tr> 
                <td valign="top"> <!-- #BeginEditable "content" --> 
                <form name="frm_prepare_data" method="post" action="">
                    <input type="hidden" name="command" value="<%=iCommand %>">
                    <input type="hidden" name="aksiCommand" value="<%=aksiCommand%>">
                    <input type="hidden" name="start" value="<%=start%>">
                    <input type="hidden" name="periodName" value="<%=periodName%>">
                    <input type="hidden" name="periodId" value="<%=PayProcessManager.getPeriodId() %>">
                    <input type="hidden" name="department" value="<%=PayProcessManager.getDepartmentId() %>">
                    <input type="hidden" name="departmentName" value="<%=departmentName%>">
                    <input type="hidden" name="salaryLevel" value="<%=levelCode%>">
                    <input type="hidden" name="payGroupId" value="<%=payGroupId%>">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                        <tr> 
                            <td height="13" width="1%">&nbsp;</td>
                            <td height="13" width="33%" nowrap><b class="listtitle">
                                    <font size="3" color="#000000">Period &nbsp; &nbsp;<%=PayProcessManager.getPeriodName() %>
                            </font></b></td>
                            <td height="13" width="30%">&nbsp;</td>
                            <td height="13" width="28%">&nbsp;</td>
                            <td height="13" width="8%">&nbsp;</td>
                        </tr> 
                        <tr> 
                            <td height="13" width="1%">&nbsp;</td>
                            <td height="13" width="33%" nowrap><b class="listtitle"><font size="3" color="#000000">                        
                        Department : <%=PayProcessManager.getDepartmentName() %>                                                                    
                        </font></b></td>
                            <td height="13" width="30%">&nbsp;</td>
                            <td height="13" width="28%">&nbsp;</td>
                            <td height="13" width="8%">&nbsp;</td>
                        </tr> 
						<tr>
						  <td>&nbsp;</td>
						  <td height="30" width="80%"> Position : 
<%
                Vector levels = PayProcessManager.getLevels();
                for(int iLevel = 0 ; iLevel < levels.size(); iLevel++){
                    try{                
                    String levelStr = (String) levels.get(iLevel);
                    int levelStaff = Integer.parseInt(levelStr);
                      %>    
                      <input name=<%=levelStr%> type="checkbox" checked value=1  readonly="true"> <%=PstPosition.strPositionLevelNames[levelStaff]%>  &nbsp;&nbsp;
                      <%                                               
                     }catch(Exception exc){                        
                    }
                  }
            %>						  
						  </td>
						  <td>&nbsp;</td>
						</tr>
<tr> 
                            <td height="30" width="1%">&nbsp;</td>
                            
                            <td height="30" width="80%" nowrap >Salary Level  
                                : <%= PayProcessManager.getLevelCode() %>                                
                            </td>
                            <td height="30" width="8%">&nbsp;</td>
                        </tr>						
						
                        <tr> 
                            <td>&nbsp;</td>
                            <td colspan="4"> 
                                <div align="left">
                                    <%
                                if ( PayProcessManager.isRunning()) { 
                                    %>
                                    .<br>                                    
                                    <input type="button" name="calculate" value="STOP Payroll Calculation" onClick="javascript:cmdStopCalculate('<%//=strButtonStatusTMA%>');"> Click this to stop the salary calculation 
                                </div>
                            </td>                            
                            <%
                                    } else {
                            %>
                            .<br>                            
                            <input type="button" name="calculate" value="RUN Payroll Calculation" onClick="javascript:cmdStartCalculate('<%//=strButtonStatusTMA%>');"> Click this calculate to <%//=strButtonStatusTMA%> the The Salary Component. 
                            </div>
                        </td>
                        <%
            }
                        %>
                    </tr>
                    <td class="listtitle" width="1%">&nbsp;</td>
                    <td class="listtitle" colspan="4">&nbsp;</td>
                </tr>
                <tr> 
                    <td width="1%">
                    </td>
                    <td colspan="4">&nbsp; <%=PayProcessManager.getMessage() %></td>
                </tr>
                <tr> 
                    <td width="1%">&nbsp;</td>
                    <td colspan="4">&nbsp;</td>
                </tr>
                <tr> 
                    <td width="1%">&nbsp;</td>
                    <td colspan="4">&nbsp;
                        <%=PayProcessManager.getSumMessage() %></td> 
                </tr>
                <tr> 
                    <td width="1%">&nbsp;</td>
                    <td colspan="4">&nbsp;</td>
                </tr>
                <tr> 
                    <td width="1%">&nbsp;</td>
                    <td colspan="4">&nbsp;</td>
                </tr>
            </table>
            
            
        </form>
        <!-- #EndEditable --> </td>
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
<tr>
    <%if(false){%>
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
        <%}%>
            <%if(PayProcessManager.isRunning()){%>
            <script language="JavaScript">

//enter refresh time in "minutes:seconds" Minutes should range from 0 to inifinity. Seconds should range from 0 to 59
var limit="0:08"
if (document.images){
var parselimit=limit.split(":")
parselimit=parselimit[0]*60+parselimit[1]*1
}
function beginrefresh(){
if (!document.images)
return

if (parselimit==1)
window.location = window.location.href //agar tidak memunculkan pesan confirmasi
else{
parselimit-=1
//curmin=Math.floor(parselimit/60)
//cursec=parselimit%60
//if (curmin!=0)
//curtime=curmin+" minutes and "+cursec+" seconds left until page refresh!"

setTimeout("beginrefresh()",100)
}
}

window.onload=beginrefresh
//-->
</script>
            <%}%>
</table>
</body>
<!-- #BeginEditable "script" --> 
<%if(true){%>
<script language="JavaScript">
    var oBody = document.body;
    var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
    </script>
<!-- #EndEditable --> <!-- #EndTemplate -->
<%}%>
</html>
