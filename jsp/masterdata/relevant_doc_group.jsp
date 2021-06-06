<%-- 
    Document   : relevant_doc_group.jsp
    Created on : Juni 3, 2014, 2:40:58 PM
    Author     : Satrya Ramayu
--%>

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
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.clinic.*" %>

<%@ include file = "../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_DEPARTMENT);%>

<% //int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_GRADE_LEVEL); %>
<%@ include file = "../main/checkuser.jsp" %>

<!-- Jsp Block -->
<%!

	public String drawList(Vector objectClass ,  long docId)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("50%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
                
		ctrlist.addHeader("Doc Group","30%");
                ctrlist.addHeader("Doc Description","70%");
		
                             
		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++) {
			EmpRelevantDocGroup empRelevantDocGroup = (EmpRelevantDocGroup)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(docId == empRelevantDocGroup.getOID())
				 index = i;
			rowx.add(empRelevantDocGroup.getDocGroup());
                        rowx.add(empRelevantDocGroup.getDocGroupDesc());
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(empRelevantDocGroup.getOID()));
		}

		return ctrlist.draw(index);
		
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidDocGroupId = FRMQueryString.requestLong(request, "hidden_doc_group_id");
String source = FRMQueryString.requestString(request, "source"); 

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstEmpRelevantDocGroup.fieldNames[PstEmpRelevantDocGroup.FLD_EMP_RELVT_DOC_GRP_ID];

CtrlEmpRelevantDocGroup ctrlEmpRelevantDocGroup = new CtrlEmpRelevantDocGroup(request);
ControlLine ctrLine = new ControlLine();
Vector listDocGroup = new Vector(1,1); 

/*switch statement */
iErrCode = ctrlEmpRelevantDocGroup.action(iCommand, oidDocGroupId);
/* end switch*/
FrmEmpRelevantDocGroup frmEmpRelevantDocGroup = ctrlEmpRelevantDocGroup.getForm();

/*count list All GradeLevel*/
int vectSize = PstEmpRelevantDocGroup.getCount(whereClause);

EmpRelevantDocGroup empRelevantDocGroup = ctrlEmpRelevantDocGroup.getEmpRelevantDocGroup();
msgString =  ctrlEmpRelevantDocGroup.getMessage();

/*switch list GradeLevel*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	//start = PstGradeLevel.findLimitStart(gradeLevel.getOID(),recordToGet, whereClause, orderClause);
	oidDocGroupId = empRelevantDocGroup.getOID();
}

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlEmpRelevantDocGroup.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listDocGroup = PstEmpRelevantDocGroup.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listDocGroup.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listDocGroup = PstEmpRelevantDocGroup.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Master Data Doc Group</title>
<script language="JavaScript">


function cmdAdd(){
	document.frmDocGroup.hidden_doc_group_id.value="0";
	document.frmDocGroup.command.value="<%=Command.ADD%>";
	document.frmDocGroup.prev_command.value="<%=prevCommand%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
}

function cmdAsk(oidDocGroupId){
	document.frmDocGroup.hidden_doc_group_id.value=oidDocGroupId;
	document.frmDocGroup.command.value="<%=Command.ASK%>";
	document.frmDocGroup.prev_command.value="<%=prevCommand%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
}

function cmdConfirmDelete(oidDocGroupId){
	document.frmDocGroup.hidden_doc_group_id.value=oidDocGroupId;
	document.frmDocGroup.command.value="<%=Command.DELETE%>";
	document.frmDocGroup.prev_command.value="<%=prevCommand%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
}
function cmdSave(){
	document.frmDocGroup.command.value="<%=Command.SAVE%>";
	document.frmDocGroup.prev_command.value="<%=prevCommand%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
	}

function cmdEdit(oidDocGroupId){
	document.frmDocGroup.hidden_doc_group_id.value=oidDocGroupId;
	document.frmDocGroup.command.value="<%=Command.EDIT%>";
	document.frmDocGroup.prev_command.value="<%=prevCommand%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
	}

function cmdCancel(oidDocGroupId){
	document.frmDocGroup.hidden_doc_group_id.value=oidDocGroupId;
	document.frmDocGroup.command.value="<%=Command.EDIT%>";
	document.frmDocGroup.prev_command.value="<%=prevCommand%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
}

function cmdBack(){
	document.frmDocGroup.command.value="<%=Command.BACK%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
	}

function cmdListFirst(){
	document.frmDocGroup.command.value="<%=Command.FIRST%>";
	document.frmDocGroup.prev_command.value="<%=Command.FIRST%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
}

function cmdListPrev(){
	document.frmDocGroup.command.value="<%=Command.PREV%>";
	document.frmDocGroup.prev_command.value="<%=Command.PREV%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
	}

function cmdListNext(){
	document.frmDocGroup.command.value="<%=Command.NEXT%>";
	document.frmDocGroup.prev_command.value="<%=Command.NEXT%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
}

function cmdListLast(){
	document.frmDocGroup.command.value="<%=Command.LAST%>";
	document.frmDocGroup.prev_command.value="<%=Command.LAST%>";
	document.frmDocGroup.action="relevant_doc_group.jsp";
	document.frmDocGroup.submit();
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
<!-- #EndEditable -->
</head> 

<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnNewOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
    <%if(source==null || source.length()==0){%> 
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
                  Master Data &gt; Employee Doc Group<!-- #EndEditable --> 
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
                                    <form name="frmDocGroup" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                      <input type="hidden" name="hidden_doc_group_id" value="<%=oidDocGroupId%>">
                                      <input type="hidden" name="source" value="">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="8"  colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                              <tr align="left" valign="top"> 
                                                <td height="14" valign="middle" colspan="3" class="listtitle">&nbsp;Employee 
                                                  Doc Group List </td>
                                              </tr>
                                              <%
							try{
								if (listDocGroup.size()>0){
							%>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(listDocGroup,oidDocGroupId)%> 
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
											  	<%//if((iCommand == Command.NONE || iCommand == Command.DELETE || iCommand ==Command.BACK || iCommand ==Command.SAVE)&& (frmDocGroup.errorSize()<1)){
                                              if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmEmpRelevantDocGroup.errorSize()<1)){
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
                                                        New</a> </td>
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
                                            <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmEmpRelevantDocGroup.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                              <tr> 
                                                <td colspan="2" class="listtitle"><%=oidDocGroupId == 0 ?"Add":"Edit"%> Employee 
                                                  Doc Group </td>
                                              </tr>
                                              <tr> 
                                                <td height="100%"> 
                                                  <table border="0" cellspacing="2" cellpadding="2" width="50%">
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%">Doc Group
                                                      </td>
                                                      <td width="79%"> 
                                                        <input type="text" name="<%=frmEmpRelevantDocGroup.fieldNames[FrmEmpRelevantDocGroup.FRM_FIELD_DOC_GROUP] %>"  value="<%= empRelevantDocGroup.getDocGroup() %>" class="elemenForm" size="30">
                                                        * <%=frmEmpRelevantDocGroup.getErrorMsg(FrmEmpRelevantDocGroup.FRM_FIELD_DOC_GROUP)%></td>
                                                    </tr>
                                                    
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%">Doc Description
                                                      </td>
                                                      <td width="79%"> 
                                                        <input type="text" name="<%=frmEmpRelevantDocGroup.fieldNames[FrmEmpRelevantDocGroup.FRM_FIELD_DOC_GROUP_DESC] %>"  value="<%= empRelevantDocGroup.getDocGroupDesc() %>" class="elemenForm" size="30">
                                                        
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
									String scomDel = "javascript:cmdAsk('"+oidDocGroupId+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidDocGroupId+"')";
									String scancel = "javascript:cmdEdit('"+oidDocGroupId+"')";
									ctrLine.setBackCaption("Back to List Doc Group");
									ctrLine.setCommandStyle("buttonlink");
									ctrLine.setAddCaption("Add Doc Group");
									ctrLine.setSaveCaption("Save Doc Group");
									ctrLine.setDeleteCaption("Delete Doc Group");
									ctrLine.setConfirmDelCaption("Yes Delete Doc Group");

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
</body>
<!-- #BeginEditable "script" -->
<script language="JavaScript">
	//var oBody = document.body;
	//var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
</script>
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
