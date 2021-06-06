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
<%@ page import="com.dimata.util.blob.*" %>
<%@ page import = "com.dimata.harisma.session.employee.*" %>
<%@ include file = "../../main/javainit.jsp" %>

<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_EMPLOYEE, AppObjInfo.G2_DATABANK, AppObjInfo.OBJ_DATABANK); %>
<%@ include file = "../../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>

<!-- Jsp Block -->

<%!
	public String drawList(Vector objectClass, long docRelevantId)
	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
                ctrlist.setListStyle("tblStyle");
                ctrlist.setTitleStyle("title_tbl");
		ctrlist.setCellStyle("listgensell");
                ctrlist.setHeaderStyle("title_tbl");
                ctrlist.setCellSpacing("0");
		ctrlist.addHeader("Title","");
		ctrlist.addHeader("Description","");
		ctrlist.addHeader("Attach File","");
                ctrlist.addHeader("Send","");
		
                ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		for (int i = 0; i < objectClass.size(); i++) {
			EmpRelevantDoc empRelevantDoc = (EmpRelevantDoc)objectClass.get(i);
			 Vector rowx = new Vector();
			 if(docRelevantId == empRelevantDoc.getOID())
				 index = i;
						
			
			rowx.add(empRelevantDoc.getDocTitle());
			rowx.add(empRelevantDoc.getDocDescription());
			rowx.add("<img border=\"0\" src=\"../../images/BtnNew.jpg\" width=\"20\" height=\"20\" ><div  valign =\"top\" align=\"center\"><a style=\"text-decoration:none\" href =\"javascript:cmdAttach('"+empRelevantDoc.getOID()+"','"+empRelevantDoc.getEmployeeId()+"')\"><font color=\"#30009D\">Attach File</font></a></div>");
                        rowx.add("<a style=\"text-decoration:none\" href =\"javascript:cmdSendEmail('"+empRelevantDoc.getOID()+"','"+empRelevantDoc.getEmployeeId()+"')\"><font color=\"#30009D\">Send File</font></a>");
		
                        
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(empRelevantDoc.getOID()));
		}

		return ctrlist.draw(index);

		//return ctrlist.draw();
	}
        
        public String drawListPages(Vector objectClass, long docRelevantId) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tblStyle");
        ctrlist.setTitleStyle("title_tbl");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("title_tbl");
        ctrlist.setCellSpacing("0");
        ctrlist.addHeader("Title", "");
        ctrlist.addHeader("Description", "");
        ctrlist.addHeader("Attach File", "");
        
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdOpenPagesEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
        
        Vector rowx = new Vector();

        for (int i = 0; i < objectClass.size(); i++) {
            EmpRelvtDocPage empRelvtDocPage = (EmpRelvtDocPage) objectClass.get(i);
            rowx = new Vector();
                index = i;
                rowx.add(empRelvtDocPage.getPageTitle());
                rowx.add(empRelvtDocPage.getPageDesc());
                rowx.add(" <a href=\"javascript:cmdOpen('"+empRelvtDocPage.getFileName()+"')\">"+empRelvtDocPage.getFileName()+"</a> " );
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(empRelvtDocPage.getOID())+"','"+String.valueOf(docRelevantId));
        }

        return ctrlist.draw(index);

        //return ctrlist.draw();
    }

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidEmpRelevantDoc = FRMQueryString.requestLong(request, "relevant_doc_oid");
long oidEmpRelevantDocPages = FRMQueryString.requestLong(request, "relevant_doc_pages_oid");
long oidEmployee = FRMQueryString.requestLong(request, "employee_oid");
System.out.println("oidEmployee........."+oidEmployee);
System.out.println("iCommand............."+iCommand);
/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_EMPLOYEE_ID]+ " = "+oidEmployee;
String orderClause = PstEmpRelevantDoc.fieldNames[PstEmpRelevantDoc.FLD_DOC_TITLE];
String whereClausePages = PstEmpRelvtDocPage.fieldNames[PstEmpRelvtDocPage.FLD_DOC_RELEVANT_ID] + " = " + oidEmpRelevantDoc;
String orderClausePages = PstEmpRelvtDocPage.fieldNames[PstEmpRelvtDocPage.FLD_PAGE_TITLE];

CtrlEmpRelevantDoc ctrlEmpRelevantDoc = new CtrlEmpRelevantDoc(request);
ControlLine ctrLine = new ControlLine();
Vector listEmpRelevantDoc = new Vector(1,1);
Vector listCtrlEmpRelvtDocPage = new Vector(1, 1);
Vector listDepartment = PstDepartment.list(0, 0, "", "DEPARTMENT");
Vector listSection = new Vector(1,1);


/*switch statement */
iErrCode = ctrlEmpRelevantDoc.action(iCommand , oidEmpRelevantDoc, oidEmployee);
/* end switch*/
FrmEmpRelevantDoc frmEmpRelevantDoc = ctrlEmpRelevantDoc.getForm();

EmpRelevantDoc empRelevantDoc = ctrlEmpRelevantDoc.getEmpRelevantDoc();
msgString =  ctrlEmpRelevantDoc.getMessage();


/*switch list CareerPath*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidEmpRelevantDoc == 0))
	start = PstEmpRelevantDoc.findLimitStart(empRelevantDoc.getOID(),recordToGet, whereClause, orderClause);

/*count list All CareerPath*/
int vectSize = PstEmpRelevantDoc.getCount(whereClause);

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlEmpRelevantDoc.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listEmpRelevantDoc = PstEmpRelevantDoc.list(start,recordToGet, whereClause , orderClause);
listCtrlEmpRelvtDocPage = PstEmpRelvtDocPage.list(start, recordToGet, whereClausePages, orderClausePages);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listEmpRelevantDoc.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listEmpRelevantDoc = PstEmpRelevantDoc.list(start,recordToGet, whereClause , orderClause);
}

long oidDepartment = 0;
if(oidEmployee != 0){
	Employee employee = new Employee();
	try{
		 employee = PstEmployee.fetchExc(oidEmployee);
		 oidDepartment = employee.getDepartmentId();
	}catch(Exception exc){
		 employee = new 	Employee();
	}
}

SessEmpRelevantDoc sessEmpRelevantDoc = new SessEmpRelevantDoc();
String pictPath = "";
if(iCommand==Command.EDIT ||iCommand==Command.ASK ){
	try{
		pictPath = sessEmpRelevantDoc.fetchImageRelevantDoc(oidEmpRelevantDoc);
	}catch(Exception e){
		System.out.println("err."+e.toString());
	}
}
System.out.println("pictPath .............."+pictPath);

if(iCommand == Command.SAVE && (iErrCode != FRMMessage.NONE)){
    iCommand = Command.ADD;
}


//listSection = PstSection.list(0,500,PstSection.fieldNames[PstSection.FLD_DEPARTMENT_ID]+ " = "+oidDepartment,"SECTION");
listSection = PstSection.list(0,0,"","SECTION");

%>
<html><!-- #BeginTemplate "/Templates/maintab.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>Relevant Document </title>
<script type="text/javascript" src="../../javascripts/jquery.js"></script>
<!--script language="JavaScript"-->
<script type="text/javascript">
    
    $(document).ready(function() {
        //alert("tes");
         $("#FRM_FIELD_EMP_RELVT_DOC_GRP_ID").change(function() {
           //alert($(this).find("option:selected").text()+' clicked!');
           var terpilih = $(this).find("option:selected").text();
           $("#frm_relevant :input[name='<%=frmEmpRelevantDoc.fieldNames[FrmEmpRelevantDoc.FRM_FIELD_DOC_TITLE] %>']").val(terpilih);

       });

        });
function cmdSendEmail(relevanDocId, empId){
		  window.open("editPenerimaRelevantDoc.jsp?relevanDocId="+relevanDocId+"&empId="+empId, "Search_HOD",  "height=550,width=600, status=yes,toolbar=no,menubar=no,location=no, scrollbars=yes");
	}
    
function cmdAdd(){
	document.frm_relevant_doc.relevant_doc_oid.value="0";
	document.frm_relevant_doc.command.value="<%=Command.ADD%>";
	document.frm_relevant_doc.prev_command.value="<%=prevCommand%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdAsk(oidEmpRelevantDoc){
	document.frm_relevant_doc.relevant_doc_oid.value=oidEmpRelevantDoc;
	document.frm_relevant_doc.command.value="<%=Command.ASK%>";
	document.frm_relevant_doc.prev_command.value="<%=prevCommand%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdConfirmDelete(oidEmpRelevantDoc){
	document.frm_relevant_doc.relevant_doc_oid.value=oidEmpRelevantDoc;
	document.frm_relevant_doc.command.value="<%=Command.DELETE%>";
	document.frm_relevant_doc.prev_command.value="<%=prevCommand%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}
function cmdSave(){
	document.frm_relevant_doc.command.value="<%=Command.SAVE%>";
	document.frm_relevant_doc.prev_command.value="<%=prevCommand%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
	}

function cmdEdit(oidEmpRelevantDoc){
	document.frm_relevant_doc.relevant_doc_oid.value=oidEmpRelevantDoc;
	document.frm_relevant_doc.command.value="<%=Command.EDIT%>";
	document.frm_relevant_doc.prev_command.value="<%=prevCommand%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
	}

function cmdCancel(oidEmpRelevantDoc){
	document.frm_relevant_doc.relevant_doc_oid.value=oidEmpRelevantDoc;
	document.frm_relevant_doc.command.value="<%=Command.EDIT%>";
	document.frm_relevant_doc.prev_command.value="<%=prevCommand%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdView(oidEmpRelevantDoc){
		window.open("popup_pict.jsp?command="+<%=Command.EDIT%>+"&emp_relevant_doc_id=" + oidEmpRelevantDoc , null, "height=700,width=820,status=yes,toolbar=no,menubar=no,location=no,scrollbars=no");

}

function cmdOpen(fileName){
		window.open("<%=approot%>/imgdoc/"+fileName , null);
		
}

function cmdBack(){
	document.frm_relevant_doc.command.value="<%=Command.BACK%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdListSection(){	
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdUpload(){
	document.frm_relevant_doc.command.value="<%=Command.SAVE%>";
	document.frm_relevant_doc.prev_command.value="<%=prevCommand%>";
	document.frm_relevant_doc.action="upload_relevant_doc.jsp";
	document.frm_relevant_doc.submit();
	}


function cmdBackEmp(empOID){
	document.frm_relevant_doc.employee_oid.value=empOID;
	document.frm_relevant_doc.command.value="<%=Command.EDIT%>";	
	document.frm_relevant_doc.action="employee_edit.jsp";
	document.frm_relevant_doc.submit();
	}


function cmdListFirst(){
	document.frm_relevant_doc.command.value="<%=Command.FIRST%>";
	document.frm_relevant_doc.prev_command.value="<%=Command.FIRST%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdListPrev(){
	document.frm_relevant_doc.command.value="<%=Command.PREV%>";
	document.frm_relevant_doc.prev_command.value="<%=Command.PREV%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
	}

function cmdListNext(){
	document.frm_relevant_doc.command.value="<%=Command.NEXT%>";
	document.frm_relevant_doc.prev_command.value="<%=Command.NEXT%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdListLast(){
	document.frm_relevant_doc.command.value="<%=Command.LAST%>";
	document.frm_relevant_doc.prev_command.value="<%=Command.LAST%>";
	document.frm_relevant_doc.action="doc_relevant.jsp";
	document.frm_relevant_doc.submit();
}

function cmdAttach(empdocId, empId){
	//document.frmleavestock.command.value="<%=Command.EDIT%>";
	//document.frm_relevant_doc.hidden_leave_stock_id.value = oid;
	//document.frmleavestock.note_type.value = type;
	//document.frmleavestock.action="leavestock_editor.jsp";
	//document.frmleavestock.submit();

	window.open("upload_pict.jsp?command="+<%=Command.EDIT%>+"&emp_relevant_doc_id=" + empdocId + "&emp_id=" + empId , null, "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");

}
function cmdOpenPages(empdocId){
    newWindow=window.open("doc_relevant_pages.jsp?command="+<%=Command.ADD%>+"&emp_relevant_doc_id=" + empdocId, null, "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
    window.focus();
}
function cmdOpenPagesEdit(empdocpagesId, empdocId){
    window.open("doc_relevant_pages.jsp?command="+<%=Command.EDIT%>+"&emp_relevant_doc_id=" + empdocId+"&relevant_doc_pages_oid=" + empdocpagesId, null, "height=400,width=600,status=yes,toolbar=no,menubar=no,location=no,scrollbars=yes");
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

function cmdBackEmployeeList() {
    document.frm_relevant_doc.action = "employee_list.jsp?command=<%=Command.FIRST%>";
    document.frm_relevant_doc.submit();
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
                background-color: #EEE;
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
                background: #ebebeb;
                border-radius: 3px;
                font-family: Arial;
                color: #7a7a7a;
                font-size: 12px;
                padding: 5px 11px 5px 11px;
                border: solid #d9d9d9 1px;
                text-decoration: none;
            }

            .btn:hover {
                color: #474747;
                background: #ddd;
                text-decoration: none;
                border: 1px solid #C5C5C5;
            }
            
            .btn-small {
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
<!-- #EndEditable -->
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
            <span id="menu_title"><%=dictionaryD.getWord(I_Dictionary.EMPLOYEE)%> <strong style="color:#333;"> / </strong> <%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></span>
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
                <li class=""> <a href="training.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.TRAINING_ON_DATABANK)%></a> </li>
                <li class=""> <a href="warning.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.WARNING) %></a> </li>
                <li class=""> <a href="reprimand.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.REPRIMAND) %></a> </li>
                <li class=""> <a href="award.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.AWARD) %></a> </li>
                <li class=""> <a href="picture.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.PICTURE) %></a> </li>
                <li class="active"> <a href="doc_relevant.jsp?employee_oid=<%=oidEmployee%>" class="tablink"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></a> </li>
            </ul>
        </div>
        <%}%>
        <div class="content-main">
            <form id="frm_relevant" name="frm_relevant_doc" method ="post"  action="">
                <input type="hidden" name="command" value="<%=iCommand%>">
                <input type="hidden" name="vectSize" value="<%=vectSize%>">
                <input type="hidden" name="start" value="<%=start%>">
                <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                <input type="hidden" name="relevant_doc_oid" value="<%=oidEmpRelevantDoc%>">
                <input type="hidden" name="relevant_doc_pages_oid" value="<%=oidEmpRelevantDocPages%>">
                <input type="hidden" name="department_oid" value="<%=oidDepartment%>">
                <input type="hidden" name="employee_oid" value="<%=oidEmployee%>">

                <input type="hidden" name="<%=FrmCareerPath.fieldNames[FrmCareerPath.FRM_FIELD_DEPARTMENT_ID]%>" value="<%=oidDepartment%>">

                <div class="content-info">
                    <% if(oidEmployee != 0){
                        Employee employee = new Employee();
                        try{
                             employee = PstEmployee.fetchExc(oidEmployee);
                        }catch(Exception exc){
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
                        <% Department department = new Department();
                            try{
                                 department = PstDepartment.fetchExc(employee.getDepartmentId());
                            }catch(Exception exc){
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
                    <div id="title-large"><%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %></div>
                    <div id="title-small">Daftar <%=dictionaryD.getWord(I_Dictionary.RELEVANT_DOCS) %> karyawan.</div>
                </div>
                <div class="content">
                    <table>
                                    <%
                                    try {
                                            if (listEmpRelevantDoc.size()>0){
                                    %>
                                    <tr align="left" valign="top"> 
                                      <td height="22" valign="middle" colspan="3" class="listtitle"> 
                                        &nbsp;Relevant Document List 
                                      </td>
                                    </tr>
                                    <tr align="left" valign="top"> 
                                      <td valign="middle" colspan="3"> 
                                        <%= drawList(listEmpRelevantDoc,iCommand == Command.SAVE?empRelevantDoc.getOID():oidEmpRelevantDoc)%> </td>
                                    </tr>
                                    <%  } else { %>
                                    <tr align="left" valign="top"> 
                                      <td height="22" valign="middle" colspan="3" class="comment"> 
                                        No Relevant Document available 
                                      </td>
                                    </tr>
                                    <% } 
                                      }catch(Exception exc){ 
                                      }%>
                                    <tr align="left" valign="top"> 
                                      <td height="8" align="left" colspan="3" class="listedittitle"> 
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
                                                        if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidEmpRelevantDoc == 0))
                                                                cmd = PstEmpRelevantDoc.findLimitCommand(start,recordToGet,vectSize);
                                                        else
                                                                cmd =prevCommand; 
                                                  }
                                           } 
                                        %>
                                        <% ctrLine.setLocationImg(approot+"/images");
                                        ctrLine.initDefault();
                                         %>
                                        <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> </span> </td>
                                    </tr>
                                    <% if(iCommand == Command.NONE || (iCommand == Command.SAVE && frmEmpRelevantDoc.errorSize()<1)|| iCommand == Command.DELETE || iCommand == Command.BACK ||
                                      iCommand == Command.FIRST || iCommand == Command.PREV || iCommand == Command.NEXT || iCommand == Command.LAST ){%>
                                    <% if(privAdd){%>
                                    <tr align="left" valign="top"> 
                                      <td> 
                                        <table cellpadding="0" cellspacing="0" border="0">
                                          <tr> 
                                            <td>&nbsp;</td>
                                          </tr>
                                          <tr> 
                                            <td width="4" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                            <td width="24" height="25"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                            <td width="6" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                            <td height="25" valign="middle" colspan="3" width="951"><a href="javascript:cmdAdd()" class="command">Add 
                                              New Relevant Document</a> 
                                            </td>
                                          </tr>
                                        </table>
                                      </td>
                                    </tr>
                                    <% } 
                                    }%>
                                  </table>
                                
                                <% if(iCommand==Command.SAVE){ %>
                                <table>
                                    <tr align="left" valign="top"> 
                                      <td> 
                                        <table cellpadding="0" cellspacing="0" border="0">
                                          <tr> 
                                            <td>&nbsp;</td>
                                          </tr>
                                          <tr> 
                                            <td width="4" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                            <td width="24" height="25"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a></td>
                                            <td width="6" height="25"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                            <td height="25" valign="middle" colspan="3" width="951"><a href="javascript:cmdAdd()" class="command">Add 
                                              New Relevant Document</a> 
                                            </td>
                                          </tr>
                                        </table>
                                      </td>
                                    </tr>
                                </table>
                                <% } %>						

                                  <%if((iCommand ==Command.ADD)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)||(iCommand==Command.LIST)){%>
                                  <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                    <tr> 
                                      <td colspan="2"><b class="listtitle">Relevant Document Editor</b></td>
                                    </tr>
                                    <tr> 
                                      <td colspan="2">*) required</td>
                                    </tr>
                                    <tr> 
                                      <td width="100%" height="216" colspan="2"> 
                                        <table width="99%" height="144" border="0" cellpadding="2" cellspacing="2">
                                        <tr align="left" valign="top"> 
                                            <td width="13%" height="32" valign="top">Group Doc
                                            </td>
                                            <td>
                                             <%
                                                Vector val_doc = new Vector(1, 1);
                                                Vector key_doc = new Vector(1, 1);
                                                Vector vdoc = PstEmpRelevantDocGroup.listAll();
                                                val_doc.add("0");
                                                key_doc.add("-- Pilih --");
                                                for (int k = 0; k < vdoc.size(); k++) {
                                                    EmpRelevantDocGroup empRelevantDocGroup = (EmpRelevantDocGroup) vdoc.get(k);
                                                    val_doc.add("" + empRelevantDocGroup.getOID());
                                                    key_doc.add("" + empRelevantDocGroup.getDocGroup());
                                                }
                                            %>
                                            <%=ControlCombo.draw("" + frmEmpRelevantDoc.fieldNames[FrmEmpRelevantDoc.FRM_FIELD_EMP_RELVT_DOC_GRP_ID], null, "" + empRelevantDoc.getEmpRelvtDocGrpId(), val_doc, key_doc, "id=\"FRM_FIELD_EMP_RELVT_DOC_GRP_ID\"", "formElemen")%>
                                            </td>
                                          </tr>
                                          <tr align="left" valign="top"> 
                                            <td width="13%" height="32" valign="top">Title 
                                            </td>
                                            <td valign="top" width="44%"><input type="hidden" name="<%=frmEmpRelevantDoc.fieldNames[FrmEmpRelevantDoc.FRM_FIELD_EMPLOYEE_ID] %>"  value="<%=""+oidEmployee %>" class="formElemen"> 
                                              <input type="text" name="<%=frmEmpRelevantDoc.fieldNames[FrmEmpRelevantDoc.FRM_FIELD_DOC_TITLE] %>"  value="<%=(empRelevantDoc.getDocTitle()!=null?empRelevantDoc.getDocTitle():"")%>" class="formElemen"> *
                                              <%= frmEmpRelevantDoc.getErrorMsg(FrmEmpRelevantDoc.FRM_FIELD_DOC_TITLE) %>
                                            </td>
                                            <td width="43%" rowspan="2">
                                                <table width="45%" height="139" border="0">
                                                    <tr>
                                                          <td height="133">

                                                          </td>
                                                    </tr>
                                                  </table>
                                            </td>
                                          </tr>
                                          <tr align="left" valign="top"> 
                                            <td width="13%" height="106" valign="top">Description 
                                            </td>
                                            <td valign="top" width="44%"><textarea name="<%=frmEmpRelevantDoc.fieldNames[FrmEmpRelevantDoc.FRM_FIELD_DOC_DESCRIPTION] %>" class="elemenForm" cols="30" rows="3"><%= empRelevantDoc.getDocDescription() %></textarea> 
                                            </td>
                                        </tr>
                                        <% if((iCommand !=Command.ADD)){%>
                                          <tr align="left" valign="top"> 
                                                <td width="13%" height="106" valign="top">Document 
                                                </td>
                                                <% if(empRelevantDoc.getFileName().length() > 0){%>
                                                <td valign="top" width="44%"><input type="hidden" name="<%=frmEmpRelevantDoc.fieldNames[FrmEmpRelevantDoc.FRM_FIELD_FILE_NAME] %>"  value="<%= empRelevantDoc.getFileName() %>" class="formElemen"> 
                                                <a href="javascript:cmdOpen('<%=empRelevantDoc.getFileName()%>')"><%=empRelevantDoc.getFileName()%></a>
                                                </td>
                                                <% }else{%>
                                                <td valign="top" width="44%"><i><font  color="#FF0000"> no relevant document found...</font></i>
                                                </td>
                                                <%
                                                }
                                                %>
                                                </tr>
                                        <%
                                        }
                                        %>
                                            <%
                                                if(oidEmpRelevantDoc != 0){

                                            %>
                                            <tr align="left" valign="top"> 
                                                <td width="13%" height="32" valign="top">
                                                 <a style="text-decoration:none" href ="javascript:cmdOpenPages('<%=empRelevantDoc.getOID()%>')"><font color="#30009D"><h3>Add doc pages</h3></font></a>
                                                </td>
                                            </tr>
                                            <tr align="left" valign="top"> 
                                                <td colspan="2" width="13%" height="32" valign="top">
                                                    <%=drawListPages(listCtrlEmpRelvtDocPage, oidEmpRelevantDoc)%>
                                                </td>
                                            </tr>
                                            <%}%>
                                        </table>
                                      </td>
                                    </tr>
                                    <tr align="left" valign="top" > 
                                      <td colspan="2" class="command"> 
                                        <%
                                                        ctrLine.setLocationImg(approot+"/images");
                                                        ctrLine.initDefault();
                                                        ctrLine.setTableWidth("80%");
                                                        String scomDel = "javascript:cmdAsk('"+oidEmpRelevantDoc+"')";
                                                        String sconDelCom = "javascript:cmdConfirmDelete('"+oidEmpRelevantDoc+"')";
                                                        String scancel = "javascript:cmdEdit('"+oidEmpRelevantDoc+"')";
                                                        ctrLine.setBackCaption("Back to List Relevant Document");
                                                        ctrLine.setCommandStyle("buttonlink");
                                                        ctrLine.setAddCaption("Add Relevant Document");
                                                        ctrLine.setSaveCaption("Save Relevant Document");
                                                        ctrLine.setDeleteCaption("Delete Relevant Document");
                                                        ctrLine.setConfirmDelCaption("Delete Relevant Document");

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
                                                        %>
                                        <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                                    </tr>

                                  </table>
                                  <%}%>
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
