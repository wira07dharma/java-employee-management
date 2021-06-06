
<% 
/* 
 * Page Name  		:  empcategory.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: karya 
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
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_CATEGORY); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//kondisi privDelete diberikan nilai false agar command Delete tidak tampak dan user tidak bisa menghapus data master
//Edited By yunny
//privDelete=false;//userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!

	public String drawList(Vector objectClass ,  long empCategoryId)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("70%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");
		ctrlist.addHeader("Category","20%");
		ctrlist.addHeader("Description","50%");
		ctrlist.addHeader("Type for Tax","50%");
                ctrlist.addHeader("Entitle Leave","50%");
                ctrlist.addHeader("Entitle Dp","50%");
                //update by satrya 2014-02-10
                ctrlist.addHeader("Entitle Insentif","50%");
                ctrlist.addHeader("Code","50%");
                ctrlist.addHeader("Category Type","50%");

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++) {
			EmpCategory empCategory = (EmpCategory)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(empCategoryId == empCategory.getOID())
				 index = i;

			rowx.add(empCategory.getEmpCategory());

			rowx.add(empCategory.getDescription());
			rowx.add(""+com.dimata.harisma.session.payroll.TaxCalculator.EMPLOYEE_TAX_TYPE[empCategory.getTypeForTax()]);
                        rowx.add(""+PstEmpCategory.fieldFlagEntitle[empCategory.getEntitleLeave()]);
                        rowx.add(""+PstEmpCategory.fieldFlagEntitle[empCategory.getEntitleDP()]);
                        //update by satrya 2014-02-10
                        rowx.add(""+PstEmpCategory.fieldFlagEntitle[empCategory.getEntitleInsentif()]); 
                        rowx.add(empCategory.getCode());
                        if(empCategory.getCategoryType() == 0) {
                            rowx.add("LOKAL");
                        } else{
                            rowx.add("ASING");
                        }
                        
                        

			lstData.add(rowx);
			lstLinkData.add(String.valueOf(empCategory.getOID()));
		}
		

		return ctrlist.draw(index);
	} 

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidEmpCategory = FRMQueryString.requestLong(request, "hidden_emp_category_id");

/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstEmpCategory.fieldNames[PstEmpCategory.FLD_EMP_CATEGORY];

CtrlEmpCategory ctrlEmpCategory = new CtrlEmpCategory(request);
ControlLine ctrLine = new ControlLine();
Vector listEmpCategory = new Vector(1,1);

/*switch statement */
iErrCode = ctrlEmpCategory.action(iCommand , oidEmpCategory);
/* end switch*/
FrmEmpCategory frmEmpCategory = ctrlEmpCategory.getForm();

/*count list All EmpCategory*/
int vectSize = PstEmpCategory.getCount(whereClause);

EmpCategory empCategory = ctrlEmpCategory.getEmpCategory();
msgString =  ctrlEmpCategory.getMessage();

/*switch list EmpCategory*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	start = PstEmpCategory.findLimitStart(empCategory.getOID(),recordToGet, whereClause, orderClause);
	oidEmpCategory = empCategory.getOID();
}

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlEmpCategory.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listEmpCategory = PstEmpCategory.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listEmpCategory.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listEmpCategory = PstEmpCategory.list(start,recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Master Data Employee Category</title>
<script language="JavaScript">


function cmdAdd(){
	document.frmempcategory.hidden_emp_category_id.value="0";
	document.frmempcategory.command.value="<%=Command.ADD%>";
	document.frmempcategory.prev_command.value="<%=prevCommand%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
}

function cmdAsk(oidEmpCategory){
	document.frmempcategory.hidden_emp_category_id.value=oidEmpCategory;
	document.frmempcategory.command.value="<%=Command.ASK%>";
	document.frmempcategory.prev_command.value="<%=prevCommand%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
}

function cmdConfirmDelete(oidEmpCategory){
	document.frmempcategory.hidden_emp_category_id.value=oidEmpCategory;
	document.frmempcategory.command.value="<%=Command.DELETE%>";
	document.frmempcategory.prev_command.value="<%=prevCommand%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
}
function cmdSave(){
	document.frmempcategory.command.value="<%=Command.SAVE%>";
	document.frmempcategory.prev_command.value="<%=prevCommand%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
	}

function cmdEdit(oidEmpCategory){
	document.frmempcategory.hidden_emp_category_id.value=oidEmpCategory;
	document.frmempcategory.command.value="<%=Command.EDIT%>";
	document.frmempcategory.prev_command.value="<%=prevCommand%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
	}

function cmdCancel(oidEmpCategory){
	document.frmempcategory.hidden_emp_category_id.value=oidEmpCategory;
	document.frmempcategory.command.value="<%=Command.EDIT%>";
	document.frmempcategory.prev_command.value="<%=prevCommand%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
}

function cmdBack(){
	document.frmempcategory.command.value="<%=Command.BACK%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
	}

function cmdListFirst(){
	document.frmempcategory.command.value="<%=Command.FIRST%>";
	document.frmempcategory.prev_command.value="<%=Command.FIRST%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
}

function cmdListPrev(){
	document.frmempcategory.command.value="<%=Command.PREV%>";
	document.frmempcategory.prev_command.value="<%=Command.PREV%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
	}

function cmdListNext(){
	document.frmempcategory.command.value="<%=Command.NEXT%>";
	document.frmempcategory.prev_command.value="<%=Command.NEXT%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
}

function cmdListLast(){
	document.frmempcategory.command.value="<%=Command.LAST%>";
	document.frmempcategory.prev_command.value="<%=Command.LAST%>";
	document.frmempcategory.action="empcategory.jsp";
	document.frmempcategory.submit();
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
			  <!-- #BeginEditable "contenttitle" --> 
                  Master Data &gt; Employee Category<!-- #EndEditable --> 
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
                                    <form name="frmempcategory" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                      <input type="hidden" name="hidden_emp_category_id" value="<%=oidEmpCategory%>">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="8"  colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                              <tr align="left" valign="top"> 
                                                <td height="14" valign="middle" colspan="3" class="listtitle">&nbsp;Employee 
                                                  Category List </td>
                                              </tr>
                                              <%
							try{
								if (listEmpCategory.size()>0){
							%>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(listEmpCategory,oidEmpCategory)%> 
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
											  <%//if((iCommand == Command.NONE || iCommand == Command.DELETE || iCommand ==Command.BACK || iCommand ==Command.SAVE)&& (frmEmpCategory.errorSize()<1)){
                                               if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmEmpCategory.errorSize()<1)){
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
                                                        New Category</a> </td>
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
                                            <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmEmpCategory.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                              <tr> 
                                                <td colspan="2" class="listtitle"><%=oidEmpCategory==0?"Add":"Edit"%> Employee 
                                                  Category</td>
                                              </tr>
                                              <tr> 
                                                <td height="100%" colspan="2" > 
                                                  <table border="0" cellspacing="2" cellpadding="2" width="50%">
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%">&nbsp;</td>
                                                      <td width="81%" class="comment">*)entry 
                                                        required </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Category</td>
                                                      <td width="81%"> 
                                                        <input type="text" name="<%=frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_EMP_CATEGORY] %>"  value="<%= empCategory.getEmpCategory() %>" class="elemenForm" size="30">
                                                        * <%=frmEmpCategory.getErrorMsg(FrmEmpCategory.FRM_FIELD_EMP_CATEGORY)%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Description</td>
                                                      <td width="81%"> 
                                                        <textarea name="<%=frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_DESCRIPTION] %>" class="elemenForm" cols="30" rows="3"><%= empCategory.getDescription() %></textarea>
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Type for Tax</td>
                                                      <td width="81%"> 
                                                        <%
                                                            Vector typeKey = new Vector(1, 1);
                                                            Vector typeValue = new Vector(1, 1);                                                            
                                                            for(int iT=0;iT < com.dimata.harisma.session.payroll.TaxCalculator.EMPLOYEE_TAX_TYPE.length;iT++ ){
                                                             typeKey.add(com.dimata.harisma.session.payroll.TaxCalculator.EMPLOYEE_TAX_TYPE[iT]);
                                                             typeValue.add(""+com.dimata.harisma.session.payroll.TaxCalculator.EMPLOYEE_TAX_TYPE_VALUE[iT]);
                                                            }
                                                                    
                                                        %>
                                                        <%=ControlCombo.draw(frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_TYPE_FOR_TAX], 
                                                        "formElemen", null, "" +empCategory.getTypeForTax(), typeValue, typeKey)%>
                                                        * <%=frmEmpCategory.getErrorMsg(FrmEmpCategory.FRM_FIELD_TYPE_FOR_TAX)%>
                                                          
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Entitle for Leave</td>
                                                      <td width="81%"> 
                                                        <%
                                                            Vector typeEntitleKey = new Vector(1, 1);
                                                            Vector typeEntitleValue = new Vector(1, 1);                                                            
                                                            typeEntitleValue.add(""+PstEmpCategory.ENTITLE_NO);
                                                            typeEntitleKey.add("NO");
                                                            typeEntitleValue.add(""+PstEmpCategory.ENTITLE_YES);
                                                            typeEntitleKey.add("YES");
                                                                    
                                                        %>
                                                        <%=ControlCombo.draw(frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_ENTITLE_FOR_LEAVE], 
                                                        "formElemen", null, "" +empCategory.getEntitleLeave(), typeEntitleValue, typeEntitleKey)%>
                                                        * <%=frmEmpCategory.getErrorMsg(FrmEmpCategory.FRM_FIELD_ENTITLE_FOR_LEAVE)%>
                                                          
                                                      </td>
                                                    </tr>
                                                    <!-- update by satrya 2014-02-10 -->
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Entitle for DP</td>
                                                      <td width="81%"> 
                                                        <%
                                                            Vector DpEntitleKey = new Vector(1, 1);
                                                            Vector DpEntitleValue = new Vector(1, 1);                                                            
                                                            DpEntitleValue.add(""+PstEmpCategory.ENTITLE_NO);
                                                            DpEntitleKey.add("NO");
                                                            DpEntitleValue.add(""+PstEmpCategory.ENTITLE_YES);
                                                            DpEntitleKey.add("YES");
                                                                    
                                                        %>
                                                        <%=ControlCombo.draw(frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_ENTITLE_DP], 
                                                        "formElemen", null, "" +empCategory.getEntitleDP(), DpEntitleValue, DpEntitleKey)%>
                                                        * <%=frmEmpCategory.getErrorMsg(FrmEmpCategory.FRM_FIELD_ENTITLE_DP)%>
                                                          
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Entitle for Insentif</td>
                                                      <td width="81%"> 
                                                        <%
                                                            Vector typeEntitleInsKey = new Vector(1, 1);
                                                            Vector typeEntitleInsValue = new Vector(1, 1);                                                            
                                                            typeEntitleInsValue.add(""+PstEmpCategory.ENTITLE_NO);
                                                            typeEntitleInsKey.add("NO");
                                                            typeEntitleInsValue.add(""+PstEmpCategory.ENTITLE_YES);
                                                            typeEntitleInsKey.add("YES");
                                                                    
                                                        %>
                                                        <%=ControlCombo.draw(frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_ENTITLE_FOR_INSENTIF], 
                                                        "formElemen", null, "" +empCategory.getEntitleInsentif(), typeEntitleInsValue, typeEntitleInsKey)%>
                                                        * <%=frmEmpCategory.getErrorMsg(FrmEmpCategory.FRM_FIELD_ENTITLE_FOR_INSENTIF)%>
                                                          
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Code</td>
                                                      <td width="81%"> 
                                                        <input type="text" name="<%=frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_CODE] %>"  value="<%= empCategory.getCode() %>" class="elemenForm" size="30">
                                                        * <%=frmEmpCategory.getErrorMsg(FrmEmpCategory.FRM_FIELD_CODE)%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="19%"> 
                                                        Category Type</td>
                                                      <td width="81%"> 
                                                        <%
                                                            Vector categoryTypeKey = new Vector(1, 1);
                                                            Vector categoryTypeValue = new Vector(1, 1);                                                            
                                                            categoryTypeValue.add(""+PstEmpCategory.CATEGORY_LOKAL);
                                                            categoryTypeKey.add("LOKAL");
                                                            categoryTypeValue.add(""+PstEmpCategory.CATEGORY_ASING);
                                                            categoryTypeKey.add("ASING");
                                                                    
                                                        %>
                                                        <%=ControlCombo.draw(frmEmpCategory.fieldNames[FrmEmpCategory.FRM_FIELD_CATEGORY_TYPE], 
                                                        "formElemen", null, "" +empCategory.getCategoryType(), categoryTypeValue, categoryTypeKey)%>
                                                        * <%=frmEmpCategory.getErrorMsg(FrmEmpCategory.FRM_FIELD_CATEGORY_TYPE)%>
                                                          
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
									String scomDel = "javascript:cmdAsk('"+oidEmpCategory+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidEmpCategory+"')";
									String scancel = "javascript:cmdEdit('"+oidEmpCategory+"')";
									ctrLine.setBackCaption("Back to List Category");
									ctrLine.setCommandStyle("buttonlink");
									ctrLine.setAddCaption("Add Category");
									ctrLine.setSaveCaption("Save Category");
									ctrLine.setDeleteCaption("Delete Category");
									ctrLine.setConfirmDelCaption("Yes Delete Category");

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
                                              <tr> 
                                                <td width="39%">&nbsp;</td>
                                                <td width="61%">&nbsp;</td>
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
