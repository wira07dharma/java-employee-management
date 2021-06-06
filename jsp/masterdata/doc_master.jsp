<%@page import="com.dimata.harisma.entity.masterdata.PstDocMasterTemplate"%>
<%@page import="com.dimata.gui.jsp.ControlCombo"%>
<%@page import="com.dimata.harisma.entity.masterdata.DocType"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstDocType"%>
<%@page import="com.dimata.harisma.entity.masterdata.PstDocMaster"%>
<%@page import="com.dimata.gui.jsp.ControlLine"%>
<%@page import="com.dimata.harisma.form.masterdata.CtrlDocMaster"%>
<%@page import="com.dimata.qdep.form.FRMMessage"%>
<%@page import="com.dimata.qdep.form.FRMQueryString"%>
<%@page import="com.dimata.util.Command"%>
<%@page import="java.util.Vector"%>
<%@page import="com.dimata.harisma.entity.masterdata.DocMaster"%>
<%@page import="com.dimata.harisma.form.masterdata.FrmDocMaster"%>
<%@page import="com.dimata.gui.jsp.ControlList"%>
<%@page import="com.dimata.harisma.entity.admin.AppObjInfo"%>
<% 
/* 
 * Page Name  		:  DocMaster.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: priska
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
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_PERFORMANCE_APPRAISAL, AppObjInfo.OBJ_GROUP_RANK); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!

	public String drawList(int iCommand,FrmDocMaster frmObject, DocMaster objEntity, Vector objectClass,  long docMasterId)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("80%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
                ctrlist.addHeader("Type Name","30%");
		ctrlist.addHeader("Title","30%");
		ctrlist.addHeader("Description","30%");
                ctrlist.addHeader("Details","50%");

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		Vector rowx = new Vector(1,1);
		ctrlist.reset();
		int index = -1;

                Vector doctype_value = new Vector(1, 1);
                Vector doctype_key = new Vector(1, 1);
                Vector listdoctype = PstDocType.list(0, 0, "", "");
                doctype_value.add(""+0);
                doctype_key.add("select");
                for (int i = 0; i < listdoctype.size(); i++) {
                    DocType docType = (DocType) listdoctype.get(i);
                    doctype_key.add(docType.getType_name());
                    doctype_value.add(String.valueOf(docType.getOID()));
                }
                
                
                
		for (int i = 0; i < objectClass.size(); i++) {
			 DocMaster docMaster = (DocMaster)objectClass.get(i);
			 rowx = new Vector();
			 if(docMasterId == docMaster.getOID())
				 index = i; 
                         //cek apakah ada doc master template
                         Vector listtemplate = PstDocMasterTemplate.list(0, 0, PstDocMasterTemplate.fieldNames[PstDocMasterTemplate.FLD_DOC_MASTER_ID] + " = " + docMaster.getOID(), "");
                
                                 
			 if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK)){				
				rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmDocMaster.FRM_FIELD_DOC_TYPE_ID], "formElemen", null, String.valueOf(docMaster.getDoc_type_id()), doctype_value, doctype_key, ""));
                                rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmDocMaster.FRM_FIELD_TITLE] +"\" value=\""+docMaster.getDoc_title()+"\" class=\"elemenForm\"> * ");
				rowx.add("<textarea name=\""+frmObject.fieldNames[FrmDocMaster.FRM_FIELD_DESCRIPTION] +"\" class=\"elemenForm\" rows=\"3\" cols=\"35\">"+docMaster.getDescription()+"</textarea>");
                                
                                if (listtemplate.size() > 0){
                                rowx.add("<a href=\"javascript:cmdExpense('"+String.valueOf(docMaster.getOID())+"')\">Expense || </a><a href=\"javascript:cmdTemplate('"+String.valueOf(docMaster.getOID())+"')\">Template || </a><a href=\"javascript:cmdFlow('"+String.valueOf(docMaster.getOID())+"')\">Flow</a> || </a><a href=\"javascript:cmdAction('"+String.valueOf(docMaster.getOID())+"')\">Action</a>");
                                } else {
                                rowx.add("<a href=\"javascript:cmdExpense('"+String.valueOf(docMaster.getOID())+"')\">Expense || </a><a href=\"javascript:cmdTemplate('"+String.valueOf(docMaster.getOID())+"')\">Template || </a><a href=\"javascript:cmdFlow('"+String.valueOf(docMaster.getOID())+"')\">Flow</a>");
                                }
                         }else{
                                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmDocMaster.FRM_FIELD_DOC_TYPE_ID], "formElemen", null, String.valueOf(docMaster.getDoc_type_id()), doctype_value, doctype_key, "disabled"));
                                rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(docMaster.getOID())+"')\">"+docMaster.getDoc_title()+"</a>");
				rowx.add(docMaster.getDescription());
                                 if (listtemplate.size() > 0){
                                rowx.add("<a href=\"javascript:cmdExpense('"+String.valueOf(docMaster.getOID())+"')\">Expense || </a><a href=\"javascript:cmdTemplate('"+String.valueOf(docMaster.getOID())+"')\">Template || </a><a href=\"javascript:cmdFlow('"+String.valueOf(docMaster.getOID())+"')\">Flow</a> || </a><a href=\"javascript:cmdAction('"+String.valueOf(docMaster.getOID())+"')\">Action</a>");
                                } else {
                                rowx.add("<a href=\"javascript:cmdExpense('"+String.valueOf(docMaster.getOID())+"')\">Expense || </a><a href=\"javascript:cmdTemplate('"+String.valueOf(docMaster.getOID())+"')\">Template || </a><a href=\"javascript:cmdFlow('"+String.valueOf(docMaster.getOID())+"')\">Flow</a>");
                                }
			} 

			lstData.add(rowx);
		}

		 rowx = new Vector();

		if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){ 
                                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmDocMaster.FRM_FIELD_DOC_TYPE_ID], "formElemen", null, String.valueOf(objEntity.getDoc_type_id()), doctype_value, doctype_key, ""));
                                rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmDocMaster.FRM_FIELD_TITLE] +"\" value=\""+objEntity.getDoc_title()+"\" class=\"elemenForm\"> * ");
                                rowx.add("<textarea name=\""+frmObject.fieldNames[FrmDocMaster.FRM_FIELD_DESCRIPTION] +"\" class=\"elemenForm\" rows=\"3\" cols=\"35\">"+objEntity.getDescription()+"</textarea>");
                                rowx.add("-");
		}

		lstData.add(rowx);

		return ctrlist.draw();
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidDocMaster = FRMQueryString.requestLong(request, "doc_master_oid");
/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = "";

CtrlDocMaster ctrlDocMaster = new CtrlDocMaster(request);
ControlLine ctrLine = new ControlLine();
Vector listDocMaster = new Vector(1,1);

iErrCode = ctrlDocMaster.action(iCommand , oidDocMaster);
/* end switch*/
FrmDocMaster frmDocMaster = ctrlDocMaster.getForm();

/*count list All GroupRank*/
int vectSize = PstDocMaster.getCount(whereClause);

/*switch list GroupRank*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlDocMaster.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

DocMaster docMaster = ctrlDocMaster.getdDocMaster();
msgString =  ctrlDocMaster.getMessage();

/* get record to display */
listDocMaster = PstDocMaster.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listDocMaster.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listDocMaster = PstDocMaster.list(start,recordToGet, whereClause , orderClause);
}

            if (iCommand == Command.SAVE){
              iCommand = 0;  
            } 
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Doc Master</title>
<script language="JavaScript">


function cmdAdd(){
	document.frmDocMaster.doc_master_oid.value="0";
	document.frmDocMaster.command.value="<%=Command.ADD%>";
	document.frmDocMaster.prev_command.value="<%=prevCommand%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdAsk(oidGroupRank){
	document.frmDocMaster.doc_master_oid.value=oidGroupRank;
	document.frmDocMaster.command.value="<%=Command.ASK%>";
	document.frmDocMaster.prev_command.value="<%=prevCommand%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdFlow(DocMasterId){
	document.frmDocMaster.command.value="<%=Command.REFRESH%>";
	document.frmDocMaster.prev_command.value="<%=prevCommand%>";
	document.frmDocMaster.action="DocMasterFlow.jsp?DocMasterId="+DocMasterId;
	document.frmDocMaster.submit();
    
	//window.open("<%=approot%>/masterdata/DocMasterFlow.jsp?DocMasterId="+DocMasterId+"", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}
function cmdTemplate(DocMasterId){
	window.open("<%=approot%>/masterdata/doc_master_template.jsp?DocMasterId="+DocMasterId+"", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}
function cmdExpense(DocMasterId){
	window.open("<%=approot%>/masterdata/doc_master_expense.jsp?DocMasterId="+DocMasterId+"", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}
function cmdAction(DocMasterId){
	window.open("<%=approot%>/masterdata/doc_master_action.jsp?FRM_FIELD_DOC_MASTER_ID="+DocMasterId+"", null, "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");       
}
function cmdConfirmDelete(oidDocMaster){
	document.frmDocMaster.doc_master_oid.value=oidDocMaster;
	document.frmDocMaster.command.value="<%=Command.DELETE%>";
	document.frmDocMaster.prev_command.value="<%=prevCommand%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdSave(){
	document.frmDocMaster.command.value="<%=Command.SAVE%>";
	document.frmDocMaster.prev_command.value="<%=prevCommand%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdEdit(oidDocMaster){
	document.frmDocMaster.doc_master_oid.value=oidDocMaster;
	document.frmDocMaster.command.value="<%=Command.EDIT%>";
	document.frmDocMaster.prev_command.value="<%=prevCommand%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdCancel(oidDocMaster){
	document.frmDocMaster.doc_master_oid.value=oidDocMaster;
	document.frmDocMaster.command.value="<%=Command.EDIT%>";
	document.frmDocMaster.prev_command.value="<%=prevCommand%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdBack(){
	document.frmDocMaster.command.value="<%=Command.BACK%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdListFirst(){
	document.frmDocMaster.command.value="<%=Command.FIRST%>";
	document.frmDocMaster.prev_command.value="<%=Command.FIRST%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdListPrev(){
	document.frmDocMaster.command.value="<%=Command.PREV%>";
	document.frmDocMaster.prev_command.value="<%=Command.PREV%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdListNext(){
	document.frmDocMaster.command.value="<%=Command.NEXT%>";
	document.frmDocMaster.prev_command.value="<%=Command.NEXT%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdListLast(){
	document.frmDocMaster.command.value="<%=Command.LAST%>";
	document.frmDocMaster.prev_command.value="<%=Command.LAST%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmDocMaster.submit();
}

function cmdListCateg(oidDocMaster){
	document.frmDocMaster.doc_master_oid.value=oidDocMaster;
	document.frmDocMaster.command.value="<%=Command.LIST%>";
	document.frmDocMaster.action="doc_master.jsp";
	document.frmdoctype.submit();
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
			  <!-- #BeginEditable "contenttitle" -->Masterdata 
                  &gt; DocMaster<!-- #EndEditable --> 
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
                                    <form name="frmDocMaster" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
									  <input type="hidden" name="doc_master_oid" value="<%=oidDocMaster%>">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="8"  colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">                                              
                                              <tr align="left" valign="top"> 
                                                <td colspan="3" >
                                                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                    <tr>
                                                      <td class="listtitle" width="37%">&nbsp;</td>
                                                      <td width="63%" class="comment">&nbsp;</td>
                                                    </tr>
                                                    <tr> 
                                                      <td class="listtitle" width="37%">Doc Master List</td>
                                                      <td width="63%" class="comment">*)entry 
                                                        required </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <%
												try{
												%>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(iCommand,frmDocMaster, docMaster, listDocMaster,oidDocMaster)%></td>
                                              </tr>
                                              <% 
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
									  else 
									  	cmd =prevCommand; 
								   } 
							    %>
                                                  <% ctrLine.setLocationImg(approot+"/images");
							   	ctrLine.initDefault();
								 %>
                                                  <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> 
                                                  </span> </td>
                                              </tr>
                                              <%if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmDocMaster.errorSize()<1)){
											  	if(privAdd){%>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <table cellpadding="0" cellspacing="0" border="0">
                                                    <tr> 
                                                      <td>&nbsp;</td>
                                                    </tr>
                                                    <tr> 
                                                      <td width="4"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td width="24"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                                      <td width="6"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td height="22" valign="middle" colspan="3" width="951"><a href="javascript:cmdAdd()" class="command">Add 
                                                        New Master</a> </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <%}
											  }%>
                                            </table>
                                          </td>										  
                                        </tr>
                                        <tr align="left" valign="top"> 
                                          <td height="8" valign="middle" width="17%">&nbsp;</td>
                                          <td height="8" colspan="2" width="83%">&nbsp; 
                                          </td>
                                        </tr>
                                        <tr align="left" valign="top" > 
                                          <td colspan="3" class="command" height="26"> 
                                            <%
									ctrLine.setLocationImg(approot+"/images");
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidDocMaster+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidDocMaster+"')";
									String scancel = "javascript:cmdEdit('"+oidDocMaster+"')";
									ctrLine.setBackCaption("Back to List Doc Master");
									ctrLine.setCommandStyle("buttonlink");
									ctrLine.setAddCaption("Add Doc Master");
									ctrLine.setSaveCaption("Save Doc Master");
									ctrLine.setDeleteCaption("Delete Doc Master");
									ctrLine.setConfirmDelCaption("Yes Delete Doc Master");

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
									
									if((listDocMaster.size()<1)&&(iCommand == Command.NONE))
										 iCommand = Command.ADD;  
										 
									if(iCommand == Command.ASK)
										ctrLine.setDeleteQuestion(msgString);										 
									%>
                                            <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> 
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
</table>
</body>
<!-- #BeginEditable "script" -->
<script language="JavaScript">
	//var oBody = document.body;
	//var oSuccess = oBody.attachEvent('onkeydown',fnTrapKD);
</script>
<!-- #EndEditable -->
<!-- #EndTemplate --></html>
