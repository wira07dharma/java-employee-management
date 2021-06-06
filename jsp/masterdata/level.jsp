<%-- 
    Document   : level.jsp
    Created on : Feb 10, 2009, 2:40:58 PM
    Author     : bayu
--%>

<%@ page language = "java" %>

<%@ page import = "java.util.*" %>

<%@ page import = "com.dimata.util.*" %>

<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>

<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.clinic.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_LEVEL); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>

<%!

	public String drawList(Vector objectClass ,  long levelId)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("tblStyle");
                ctrlist.setTitleStyle("title_tbl");
                ctrlist.setCellStyle("listgensell");
                ctrlist.setHeaderStyle("title_tbl");
                ctrlist.setCellSpacing("0");
                
		ctrlist.addHeader("Level","");
                //ctrlist.addHeader("Grade Level","15%");
		ctrlist.addHeader("Group","");
                ctrlist.addHeader("Employee Medical Level","");
                ctrlist.addHeader("Family Medical Level","");
		ctrlist.addHeader("Description","");    
                ctrlist.addHeader("Level Point");
                ctrlist.addHeader("Code");
                ctrlist.addHeader("Level Rank");
                             
		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++) {
			Level level = (Level)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(levelId == level.getOID())
				 index = i;

			rowx.add(level.getLevel());
                        
                        /*GradeLevel gradeLevel = new GradeLevel();
			try{			
				gradeLevel = PstGradeLevel.fetchExc(level.getGradeLevelId());
			}catch(Exception exc){
				gradeLevel = new GradeLevel();
			}*/
			
			//rowx.add(gradeLevel.getCodeLevel());
                        
			GroupRank groupRank = new GroupRank();
			try{			
				groupRank = PstGroupRank.fetchExc(level.getGroupRankId());
			}catch(Exception exc){
				groupRank = new GroupRank();
			}
			
			rowx.add(groupRank.getGroupName());			
                        
                        String medicLevel = "";
                        
                        try {
                            MedicalLevel mLevel = PstMedicalLevel.fetchExc(level.getEmployeeMedicalId());
                            medicLevel = mLevel.getLevelName();
                        }
                        catch(Exception e) {
                            medicLevel = "";
                        }
                        
                        rowx.add(""+medicLevel);
                        
                         try {
                            MedicalLevel mLevel = PstMedicalLevel.fetchExc(level.getFamilyMedicalId());
                            medicLevel = mLevel.getLevelName();
                        }
                        catch(Exception e) {
                            medicLevel = "";
                        }
                        
                        rowx.add("" +medicLevel);
                        rowx.add(level.getDescription());
                        rowx.add(""+level.getLevelPoint());
                        rowx.add(level.getCode());
                        rowx.add(""+level.getLevelRank());

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(level.getOID()));
		}

		return ctrlist.draw(index);
		
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request); 
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidLevel = FRMQueryString.requestLong(request, "hidden_level_id");

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstLevel.fieldNames[PstLevel.FLD_LEVEL]  + "," + PstLevel.fieldNames[PstLevel.FLD_LEVEL] + " , " + PstLevel.fieldNames[PstLevel.FLD_GROUP_RANK_ID];

CtrlLevel ctrlLevel = new CtrlLevel(request);
ControlLine ctrLine = new ControlLine();
Vector listLevel = new Vector(1,1);

/*switch statement */
iErrCode = ctrlLevel.action(iCommand , oidLevel);
/* end switch*/
FrmLevel frmLevel = ctrlLevel.getForm();

/*count list All Level*/
int vectSize = PstLevel.getCount(whereClause);

Level level = ctrlLevel.getLevel();
msgString =  ctrlLevel.getMessage();

/*switch list Level*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	//start = PstLevel.findLimitStart(level.getOID(),recordToGet, whereClause, orderClause);
	oidLevel = level.getOID();
}

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlLevel.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listLevel = PstLevel.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listLevel.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listLevel = PstLevel.list(start,recordToGet, whereClause , orderClause);
}
//boolean viewGradeLevel = true;
%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN">
<html>
<head> 
 <%@ include file = "../main/konfigurasi_jquery.jsp" %>    
<script src="../javascripts/jquery.min-1.6.2.js" type="text/javascript"></script>
<script src="../javascripts/chosen.jquery.js" type="text/javascript"></script>
<title>HARISMA - Master Data Level</title>
<link rel="stylesheet" href="../stylesheets/chosen.css" >
<script language="JavaScript">
//update by satrya 2012-12-20
function cmdAddToMedicalLevel(){
    var linkPage = "<%=approot%>/clinic/medexpense/med_level.jsp?source=level_addGrade"; 
                //window.open(linkPage,"Absence Edit","height=600,width=800,status=yes,toolbar=yes,menubar=yes,location=yes");  			
                var newWin = window.open(linkPage,"medical_type","height=600,width=800,status=yes,toolbar=yes,menubar=no,resizable=yes,scrollbars=yes,location=no");  			
                newWin.focus();
}

function cmdAddToGradeLevel(){
    var linkPage = "<%=approot%>/masterdata/grade_level.jsp?source=level_addGrade"; 
                //window.open(linkPage,"Absence Edit","height=600,width=800,status=yes,toolbar=yes,menubar=yes,location=yes");  			
                var newWin = window.open(linkPage,"level_addGrade","height=600,width=800,status=yes,toolbar=yes,menubar=no,resizable=yes,scrollbars=yes,location=no");  			
                newWin.focus();
    
}

function cmdAdd(){
	document.frmlevel.hidden_level_id.value="0";
	document.frmlevel.command.value="<%=Command.ADD%>";
	document.frmlevel.prev_command.value="<%=prevCommand%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
}

function cmdAsk(oidLevel){
	document.frmlevel.hidden_level_id.value=oidLevel;
	document.frmlevel.command.value="<%=Command.ASK%>";
	document.frmlevel.prev_command.value="<%=prevCommand%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
}

function cmdConfirmDelete(oidLevel){
	document.frmlevel.hidden_level_id.value=oidLevel;
	document.frmlevel.command.value="<%=Command.DELETE%>";
	document.frmlevel.prev_command.value="<%=prevCommand%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
}
function cmdSave(){
	document.frmlevel.command.value="<%=Command.SAVE%>";
	document.frmlevel.prev_command.value="<%=prevCommand%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
	}

function cmdEdit(oidLevel){
	document.frmlevel.hidden_level_id.value=oidLevel;
	document.frmlevel.command.value="<%=Command.EDIT%>";
	document.frmlevel.prev_command.value="<%=prevCommand%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
	}

function cmdCancel(oidLevel){
	document.frmlevel.hidden_level_id.value=oidLevel;
	document.frmlevel.command.value="<%=Command.EDIT%>";
	document.frmlevel.prev_command.value="<%=prevCommand%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
}

function cmdBack(){
	document.frmlevel.command.value="<%=Command.BACK%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
	}

function cmdListFirst(){
	document.frmlevel.command.value="<%=Command.FIRST%>";
	document.frmlevel.prev_command.value="<%=Command.FIRST%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
}

function cmdListPrev(){
	document.frmlevel.command.value="<%=Command.PREV%>";
	document.frmlevel.prev_command.value="<%=Command.PREV%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
	}

function cmdListNext(){
	document.frmlevel.command.value="<%=Command.NEXT%>";
	document.frmlevel.prev_command.value="<%=Command.NEXT%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
}

function cmdListLast(){
	document.frmlevel.command.value="<%=Command.LAST%>";
	document.frmlevel.prev_command.value="<%=Command.LAST%>";
	document.frmlevel.action="level.jsp";
	document.frmlevel.submit();
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
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" --> 
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

</SCRIPT>
<style type="text/css">
    .tblStyle {border-collapse: collapse;font-size: 11px;}
    .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
    .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
</style>
<!-- #EndEditable -->
</head> 

<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
     <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../styletemplate/template_header.jsp" %>
            <%}else{%>
  <tr> 
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
      <!-- #BeginEditable "header" --> 
      <%@ include file = "../main/header.jsp" %>
      <!-- #EndEditable --> 
    </td>
  </tr> 
  <tr> 
    <td  bgcolor="#9BC1FF" height="15" ID="MAINMENU" valign="middle"> <!-- #BeginEditable "menumain" --> 
      <%@ include file = "../main/mnmain.jsp" %>
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
                  Master Data &gt; Employee Level<!-- #EndEditable --> 
            </strong></font>
	      </td>
        </tr>
        <tr> 
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td  style="background-color:<%=bgColorContent%>; "> 
                  <table width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                    <tr> 
                      <td valign="top"> 
                        <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tabbg">
                          <tr> 
                            <td valign="top">
		    				  <!-- #BeginEditable "content" --> 
                                    <form name="frmlevel" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                      <input type="hidden" name="hidden_level_id" value="<%=oidLevel%>">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="8"  colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                              <tr align="left" valign="top"> 
                                                <td height="14" valign="middle" colspan="3" class="listtitle">&nbsp;Employee 
                                                  Level List </td>
                                              </tr>
                                              <%
							try{
								if (listLevel.size()>0){
							%>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(listLevel,oidLevel)%> 
                                                </td>
                                              </tr>
                                              <%  } 
						  }catch(Exception exc){ 
						  }%>
                                              <tr align="left" valign="top"> 
                                                <td height="8" align="left" colspan="3" class="command"> 
                                                  <span class="command"> 
                                                  <% 
								   int cmd = 0;
									   if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
										(iCommand == Command.NEXT || iCommand == Command.LAST))
											cmd =iCommand; 
								   else{
									  if(iCommand == Command.NONE || prevCommand == Command.NONE)
										cmd = Command.FIRST;
									  else{
									  		if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE))
												cmd = PstDepartment.findLimitCommand(start,recordToGet,vectSize);
											else									 
									  			cmd =prevCommand;
									  }  
								   } 
							    %>
                                                  <% ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								 %>
                                                  <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> 
                                                  </span> </td>
                                              </tr>
											  	<%//if((iCommand == Command.NONE || iCommand == Command.DELETE || iCommand ==Command.BACK || iCommand ==Command.SAVE)&& (frmLevel.errorSize()<1)){
                                              if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmLevel.errorSize()<1)){
												if(privAdd){%>

                                              <tr align="left" valign="top"> 
                                                <td> 
                                                  <table cellpadding="0" cellspacing="0" border="0">
                                                    <tr> 
                                                      <td>&nbsp;</td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                                      <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td height="22" valign="middle" colspan="3" width="951"><a href="javascript:cmdAdd()" class="command">Add 
                                                        New Level</a> </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
											  <%}
											  }%>
                                            </table>
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td>&nbsp; </td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                          <td height="8" valign="middle" colspan="3"> 
                                            <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmLevel.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                              <tr> 
                                                <td colspan="2" class="listtitle"><%=oidLevel == 0 ?"Add":"Edit"%> Employee 
                                                  Level </td>
                                              </tr>
                                              <tr> 
                                                <td height="100%"> 
                                                  <table border="0" cellspacing="2" cellpadding="2" width="50%">
                                                    <tr align="left" valign="top">
                                                      <td valign="top" width="21%">Group 
                                                        rank</td>
                                                      <td width="79%"> 
                                                        <%
													  Vector rankValue = new Vector(1,1);
													  Vector rankKey = new Vector(1,1);
													  Vector listGroup = PstGroupRank.listAll();
													  for(int i=0;i<listGroup.size();i++){
													  	GroupRank groupRank = (GroupRank)listGroup.get(i);
													  	rankValue.add(""+groupRank.getOID());
														rankKey.add(""+groupRank.getGroupName());
													  }
                                                                                                           String attTag =  "data-placeholder=\"Choose a Group Rank...\" style=\"width:200px;\"";
													  %>
                                                        <%=ControlCombo.draw(frmLevel.fieldNames[FrmLevel.FRM_FIELD_GROUP_RANK_ID], "chosen-select", null,""+level.getGroupRankId(), rankKey,rankValue,null, attTag)%>
                                                         <%=frmLevel.getErrorMsg(FrmLevel.FRM_FIELD_GROUP_RANK_ID)%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%">Level 
                                                      </td>
                                                      <td width="79%"> 
                                                          <input type="text" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_LEVEL] %>"  value="<%= level.getLevel() %>" class="elemenForm" size="30" placeholder="Type name Level..">
                                                        * <%=frmLevel.getErrorMsg(FrmLevel.FRM_FIELD_LEVEL)%></td>
                                                    </tr>
                                                    <!-- update by satrya 2014-06-03 -->
                                                    <%if(false){%>
                                                    <tr align="left" valign="top">
                                                      <td valign="top" width="21%">Grade Level
                                                        </td>
                                                      <td width="79%"> 
                                                        <%
                                                              Vector gdValue = new Vector(1,1);
                                                              Vector gdKey = new Vector(1,1);
                                                              Vector listgd = PstGradeLevel.listAll();
                                                              for(int i=0;i<listgd.size();i++){
                                                                    GradeLevel gradeLevel = (GradeLevel)listgd.get(i);
                                                                    gdValue.add(""+gradeLevel.getOID());
                                                                    gdKey.add(""+gradeLevel.getCodeLevel());
                                                              }
                                                              attTag =  "data-placeholder=\"Choose a Grade Level...\" style=\"width:100px;\"";
                                                          %>
                                                         <%=ControlCombo.draw(frmLevel.fieldNames[FrmLevel.FRM_FIELD_GRADE_LEVEL_ID], "chosen-select", null,""+level.getGradeLevelId(), gdKey,gdValue,null, attTag)%>
                                                        <a href="javascript:cmdAddToGradeLevel()" class="command">Add New Grade Level</a> </td>
                                                         <%=frmLevel.getErrorMsg(FrmLevel.FRM_FIELD_EMP_MEDIC_LEVEL)%></td>
                                                    </tr>
                                                    <%}%>
                                                    <tr align="left" valign="top">
                                                      <td valign="top" width="21%">Employee Medical Level 
                                                        </td>
                                                      <td width="79%"> 
                                                        <%
                                                              Vector medicValue = new Vector(1,1);
                                                              Vector medicKey = new Vector(1,1);
                                                              Vector listMedic = PstMedicalLevel.listAll();
                                                              for(int i=0;i<listMedic.size();i++){
                                                                    MedicalLevel medic = (MedicalLevel)listMedic.get(i);
                                                                    medicValue.add(""+medic.getOID());
                                                                    medicKey.add(""+medic.getLevelName());
                                                              }	
                                                              attTag =  "data-placeholder=\"Choose a Medical Level...\" style=\"width:100px;\"";
                                                          %>
                                                          <%=ControlCombo.draw(frmLevel.fieldNames[FrmLevel.FRM_FIELD_EMP_MEDIC_LEVEL], "chosen-select", null,""+level.getEmployeeMedicalId(), medicKey,medicValue,null, attTag)%>
                                                        <a href="javascript:cmdAddToMedicalLevel()" class="command">Add New Medical Level</a> </td>
                                                         <%=frmLevel.getErrorMsg(FrmLevel.FRM_FIELD_EMP_MEDIC_LEVEL)%></td>
                                                    </tr>
                                                    <tr align="left" valign="top">
                                                      <td valign="top" width="21%">Family Member Medical Level
                                                        </td>
                                                      <td width="79%">
                                                        <%
                                                         attTag =  "data-placeholder=\"Choose a Family Member...\" style=\"width:100px;\"";
                                                        %>
                                                        <%=ControlCombo.draw(frmLevel.fieldNames[FrmLevel.FRM_FIELD_FMLY_MEDIC_LEVEL], "chosen-select", null,""+level.getFamilyMedicalId(), medicKey,medicValue,null, attTag)%>
                                                        
                                                         <%=frmLevel.getErrorMsg(FrmLevel.FRM_FIELD_FMLY_MEDIC_LEVEL)%></td>
                                                    </tr>
                                                    <!-- update field by Hendra 2015-01-08 -->
                                                    <tr align="left" valign="top">
                                                      <td valign="top" width="21%">Level Point
                                                        </td>
                                                        <td width="79%"><input type="text" placeholder="Type Number Value..." name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_LEVEL_POINT]%>" value="<%= level.getLevelPoint() %>" /></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%"> 
                                                        Description</td>
                                                      <td width="79%"> 
                                                        <textarea name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_DESCRIPTION] %>" class="elemenForm" cols="30" rows="3"><%= level.getDescription() %></textarea>
                                                      </td>
                                                    </tr>
                                                      <tr align="left" valign="top">
                                                      <td valign="top" width="21%">Code
                                                        </td>
                                                        <td width="79%"><input type="text" placeholder="Type Code Value..." name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_CODE]%>" value="<%= level.getCode() %>" /></td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="middle">Level Rank</td>
                                                        <td valign="middle">
                                                            <input type="text" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_LEVEL_RANK]%>" value="<%=level.getLevelRank()%>" />
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="middle">Entitle DP</td>
                                                        <td valign="middle">
                                                            <%
                                                                String chkDpYes = "";
                                                                String chkDpNo = "";
                                                                if (level.getEntitleDP() == 0){
                                                                   chkDpNo = " checked ";
                                                                } else {
                                                                   chkDpYes = " checked"; 
                                                                }
                                                            %>
                                                            <input type="radio" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_ENTITLE_DP]%>" value="0" id="dp_no" onClick="javascript:dpCheck();" <%=chkDpNo%> />
                                                            No
                                                            <input type="radio" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_ENTITLE_DP]%>" value="1" id="dp_yes" onClick="javascript:dpCheck();" <%=chkDpYes%> />
                                                            Yes
                                                        </td>
                                                    </tr>
                                                    <tr id="entitleDP">
                                                        <td valign="middle">DP Qty</td>
                                                        <td valign="middle">
                                                            <input type="text" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_ENTITLED_DP_QTY]%>" id="dp_qty" value="<%=level.getEntitledDPQty()%>" />
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="middle">Entitle PH</td>
                                                        <td valign="middle">
                                                            <%
                                                                String chkPhYes = "";
                                                                String chkPhNo = "";
                                                                if (level.getEntitlePH() == 0){
                                                                   chkPhNo = " checked ";
                                                                } else {
                                                                   chkPhYes = " checked"; 
                                                                }
                                                            %>
                                                            <input type="radio" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_ENTITLE_PH]%>" value="0" id="ph_no" onClick="javascript:phCheck();" <%=chkPhNo%> />
                                                            No
                                                            <input type="radio" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_ENTITLE_PH]%>" value="1" id="ph_yes" onClick="javascript:phCheck();" <%=chkPhYes%> />
                                                            Yes
                                                        </td>
                                                    </tr>
                                                    <tr id="entitlePH">
                                                        <td valign="middle">PH Qty</td>
                                                        <td valign="middle">
                                                            <input type="text" name="<%=frmLevel.fieldNames[FrmLevel.FRM_FIELD_ENTITLED_PH_QTY]%>" id="ph_qty" value="<%=level.getEntitledPHQty()%>" />
                                                        </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <tr align="left" valign="top" > 
                                                <td colspan="2" class="command"> 
                                                  <%
									ctrLine.setLocationImg(approot+"/images");
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidLevel+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidLevel+"')";
									String scancel = "javascript:cmdEdit('"+oidLevel+"')";
									ctrLine.setBackCaption("Back to List Level");
									ctrLine.setCommandStyle("buttonlink");
									ctrLine.setAddCaption("Add Level");
									ctrLine.setSaveCaption("Save Level");
									ctrLine.setDeleteCaption("Delete Level");
									ctrLine.setConfirmDelCaption("Yes Delete Level");

									if (privDelete){
										ctrLine.setConfirmDelCommand(sconDelCom);
										ctrLine.setDeleteCommand(scomDel);
										ctrLine.setEditCommand(scancel);
									}else{ 
										ctrLine.setConfirmDelCaption("");
										ctrLine.setDeleteCaption("");
										ctrLine.setEditCaption("");
									}

									if(privAdd == false  && privUpdate == false){
										ctrLine.setSaveCaption("");
									}

									if (privAdd == false){
										ctrLine.setAddCaption("");
									}
									
									if(iCommand == Command.ASK)
										ctrLine.setDeleteQuestion(msgString);
									%>
                                                  <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> 
                                                </td>
                                              </tr>
                                              <tr align="left" valign="top" > 
                                                <td colspan="3"> 
                                                  <div align="left"></div>
                                                </td>
                                              </tr>
                                            </table>
                                            <%}%>
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
                                <%@include file="../footer.jsp" %>
                            </td>
                            
            </tr>
            <%}else{%>
            <tr> 
                <td colspan="2" height="20" > <!-- #BeginEditable "footer" --> 
      <%@ include file = "../main/footer.jsp" %>
                <!-- #EndEditable --> </td>
            </tr>
            <%}%>
</table>

    <script type="text/javascript">
        var config = {
            '.chosen-select'           : {},
            '.chosen-select-deselect'  : {allow_single_deselect:true},
            '.chosen-select-no-single' : {disable_search_threshold:10},
            '.chosen-select-no-results': {no_results_text:'Oops, nothing found!'},
            '.chosen-select-width'     : {width:"95%"}
        }
        for (var selector in config) {
            $(selector).chosen(config[selector]);
        }
        
        function phCheck() {
            if (document.getElementById('ph_no').checked) {
                  document.all.entitlePH.style.display = 'none';
                  document.all.ph_qty.value='0';
            }
            else {
              document.all.entitlePH.style.display = '';
            }
        }
        
        function dpCheck() {
            if (document.getElementById('dp_no').checked) {
                  document.all.entitleDP.style.display = 'none';
                  document.all.dp_qty.value='0';
            }
            else {
              document.all.entitleDP.style.display = '';
            }
        }
          
          dpCheck();
          phCheck();
        
</script>
</body>
<!-- #BeginEditable "script" -->
<script language="JavaScript">
	//var oBody = document.body;
	//var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
</script>
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
