<%@ page language = "java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>

<% 
/* 
 * Page Name  		:  schedulesymbol.jsp
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

<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_SCHEDULE, AppObjInfo.OBJ_SCHEDULE_SYMBOL); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!

	public String drawList(Vector objectClass ,  long scheduleId)

	{
		ControlList ctrlist = new ControlList();
		ctrlist.setAreaWidth("100%");
		ctrlist.setListStyle("listgen");
		ctrlist.setTitleStyle("listgentitle");
		ctrlist.setCellStyle("listgensell");
		ctrlist.setHeaderStyle("listgentitle");				
		ctrlist.addHeader("Symbol","10%");
		ctrlist.addHeader("Schedule Category","20%");
		ctrlist.addHeader("Schedule Name","20%");
		ctrlist.addHeader("Time In","10%");
		ctrlist.addHeader("Time Out","10%");
		ctrlist.addHeader("Break Out","10%");
		ctrlist.addHeader("Break In","10%");
		ctrlist.addHeader("Note","30%");

		ctrlist.setLinkRow(0);
		ctrlist.setLinkSufix("");
		Vector lstData = ctrlist.getData();
		Vector lstLinkData = ctrlist.getLinkData();
		ctrlist.setLinkPrefix("javascript:cmdEdit('");
		ctrlist.setLinkSufix("')");
		ctrlist.reset();
		int index = -1;

		Vector lsLL = new Vector(1,1);
		lsLL = PstScheduleCategory.listAll();

		for (int i = 0; i < objectClass.size(); i++) 
		{
			ScheduleSymbol scheduleSymbol = (ScheduleSymbol)objectClass.get(i);
			Vector rowx = new Vector();
			if(scheduleId == scheduleSymbol.getOID())
				 index = i;
				 
			rowx.add(scheduleSymbol.getSymbol());
			
			int scheduleCat = PstScheduleCategory.CATEGORY_ALL;			
			for (int j = 0; j < lsLL.size(); j++) 
			{
				ScheduleCategory cat = (ScheduleCategory) lsLL.get(j);
				if (cat.getOID() == scheduleSymbol.getScheduleCategoryId()) 
				{
					rowx.add(cat.getCategory());
					scheduleCat = cat.getCategoryType();
				}
			}

			rowx.add(scheduleSymbol.getSchedule());

			String str_dt_TimeIn = ""; 
			try
			{
				Date dt_TimeIn = scheduleSymbol.getTimeIn();
				if(dt_TimeIn==null)
				{
					//dt_TimeIn = new Date();
                                    str_dt_TimeIn = " Not to Be Set "; 
				}else{
                                    str_dt_TimeIn = Formater.formatTimeLocale(dt_TimeIn);
                                }
			}
            catch(Exception e)
			{ 
				//str_dt_TimeIn = ""; 
                                str_dt_TimeIn = " Not to Be Set "; 
			}

			String str_dt_TimeOut = ""; 
			try
			{
				Date dt_TimeOut = scheduleSymbol.getTimeOut();
				if(dt_TimeOut==null)
				{
					//dt_TimeOut = new Date();
                                     str_dt_TimeOut = " Not to Be Set "; 
				}else{
				str_dt_TimeOut = Formater.formatTimeLocale(dt_TimeOut);
                                }
			}
			catch(Exception e)
			{ 
				str_dt_TimeOut = " Not to Be Set "; 
			}
                        
			String str_dt_BreakOut = ""; 
			try
			{
				Date dt_BreakOut = scheduleSymbol.getBreakOut();
				if(dt_BreakOut==null)
				{
					//dt_BreakOut = new Date();
                                    str_dt_BreakOut = " Not to Be Set "; 
				}else{
				str_dt_BreakOut = Formater.formatTimeLocale(dt_BreakOut);
                                }
			}
            catch(Exception e)
			{ 
				 str_dt_BreakOut = " Not to Be Set "; 
			}

			String str_dt_BreakIn = ""; 
			try
			{
				Date dt_BreakIn = scheduleSymbol.getBreakIn();
				if(dt_BreakIn==null)
				{
					//dt_BreakIn = new Date();
                                    str_dt_BreakIn =  " Not to Be Set ";
				}else{
				str_dt_BreakIn = Formater.formatTimeLocale(dt_BreakIn);
                                 }
			}
			catch(Exception e)
			{ 
				str_dt_BreakIn =  " Not to Be Set ";
			}
                       /* if(str_dt_BreakIn!=null && ( str_dt_BreakIn.equals(str_dt_BreakOut))){
                            str_dt_BreakIn ="-";
                            str_dt_BreakOut="-";
                        }*/
                        //update by satrya 2013-04-17
                        
			/*
			 * kalau timeIn dan timeOut sama, maka tidak ditampilkan
			if (str_dt_TimeIn.compareTo(str_dt_TimeOut) != 0)
			{
				rowx.add(str_dt_TimeIn);
				rowx.add(str_dt_TimeOut);
			}
			else
			{
				rowx.add("-");
				rowx.add("-");
			}
			*/

			
			// kalau timeIn dan timeOut sama maka tetap ditampilkan
			if(scheduleCat == PstScheduleCategory.CATEGORY_REGULAR
			   || scheduleCat == PstScheduleCategory.CATEGORY_SPLIT_SHIFT
			   || scheduleCat == PstScheduleCategory.CATEGORY_NIGHT_WORKER
   			   || scheduleCat == PstScheduleCategory.CATEGORY_ACCROSS_DAY
			   || scheduleCat == PstScheduleCategory.CATEGORY_EXTRA_ON_DUTY
			   || scheduleCat == PstScheduleCategory.CATEGORY_NEAR_ACCROSS_DAY			
			   || scheduleCat == PstScheduleCategory.CATEGORY_SUPPOSED_TO_BE_OFF)			
			{
                            rowx.add(str_dt_TimeIn);
                            rowx.add(str_dt_TimeOut);
                            rowx.add(str_dt_BreakOut);
                            rowx.add(str_dt_BreakIn);
			}
			else
			{
                            rowx.add("-");
                            rowx.add("-");
                            rowx.add("-");
                            rowx.add("-");
			}			
                        if(scheduleCat == PstScheduleCategory.CATEGORY_SPECIAL_LEAVE){
                            /*String strNote = " max entitle : "+scheduleSymbol.getMaxEntitle()
                                    +", periode : "+scheduleSymbol.getPeriode()
                                    +"/"+PstScheduleSymbol.fieldNamesPeriodeType[scheduleSymbol.getPeriodeType()]
                                    +" and may taken after "+scheduleSymbol.getMinService()+" month of service";
 *                          */
                            String strNote = " max entitle : "+scheduleSymbol.getMaxEntitle()
                                    +", periode : "+ (scheduleSymbol.getPeriodeType() == PstScheduleSymbol.PERIODE_TYPE_TIME_AT_ALL ? "" : "per ") 
                                    + scheduleSymbol.getPeriode() + " "
                                    + PstScheduleSymbol.fieldNamesPeriodeType[scheduleSymbol.getPeriodeType()]
                                    +", and may be taken after "+scheduleSymbol.getMinService()+" month of service";
                            
                            rowx.add(strNote);
                        }else{
                            rowx.add("-");
                        }
                                   
			lstData.add(rowx);
			lstLinkData.add(String.valueOf(scheduleSymbol.getOID()));
		}

		//return ctrlist.drawList(index);

		return ctrlist.draw(index);
	}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidCatSelect = FRMQueryString.requestLong(request, "hidden_cat_oid_select");
long oidScheduleSymbol = FRMQueryString.requestLong(request, "hidden_schedule_id");

/*variable declaration*/
int recordToGet = 200;
String msgString = "";
int iErrCode = FRMMessage.NONE;

CtrlScheduleSymbol ctrlScheduleSymbol = new CtrlScheduleSymbol(request);
ControlLine ctrLine = new ControlLine();
Vector listScheduleSymbol = new Vector(1,1);

/*switch statement */
iErrCode = ctrlScheduleSymbol.action(iCommand , oidScheduleSymbol, request);
/* end switch*/
FrmScheduleSymbol frmScheduleSymbol = ctrlScheduleSymbol.getForm();


SrcScheduleSymbol srcScheduleSymbol = new SrcScheduleSymbol();
FrmSrcScheduleSymbol frmSrcScheduleSymbol  = new FrmSrcScheduleSymbol(request, srcScheduleSymbol);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==Command.BACK || iCommand==Command.ASK
   || iCommand==Command.EDIT || iCommand==Command.ADD || iCommand==Command.DELETE || (iCommand==Command.SAVE && frmScheduleSymbol.errorSize()==0) )
{
	 try
	 { 
		srcScheduleSymbol = (SrcScheduleSymbol)session.getValue(PstScheduleSymbol.SESS_HR_SCHEDULE_SYMBOL); 
	 }
	 catch(Exception e)  
	 { 
		srcScheduleSymbol = new SrcScheduleSymbol();
	 }
}
else
{
	frmSrcScheduleSymbol.requestEntityObject(srcScheduleSymbol, request);
	session.putValue(PstScheduleSymbol.SESS_HR_SCHEDULE_SYMBOL, srcScheduleSymbol);	
}


String whereClause = "";
if(srcScheduleSymbol.getSchSymbol()!=null && srcScheduleSymbol.getSchSymbol().length()>0)
{
    try{
	whereClause = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL] + " LIKE \"%" + srcScheduleSymbol.getSchSymbol() + "%\"";
    }catch(Exception ex){
        System.out.println("WhereCaluse srcScheduleSymbol Ex " +ex.toString());
}
 }

if(srcScheduleSymbol.getSchCategory() != 0)
{
	if(whereClause!=null && whereClause.length()>0)
	{
		whereClause = whereClause + " AND " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + " = " + srcScheduleSymbol.getSchCategory();		
	}
	else
	{
		whereClause = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SCHEDULE_CATEGORY_ID] + " = " + srcScheduleSymbol.getSchCategory();			
	}
}

if(!srcScheduleSymbol.isSelectTimeIn())
{
	if(srcScheduleSymbol.getTimeInStart()!=null && srcScheduleSymbol.getTimeInEnd()!=null)
	{
		String strTimeInStart = Formater.formatDate(srcScheduleSymbol.getTimeInStart(), "HH:mm:ss");
		String strTimeInEnd = Formater.formatDate(srcScheduleSymbol.getTimeInEnd(), "HH:mm:ss");		
		if(whereClause!=null && whereClause.length()>0)
		{
			whereClause = whereClause + " AND " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN] + 
						  " BETWEEN \"" + strTimeInStart + "\" AND \"" + strTimeInEnd + "\"";		
		}
		else
		{
			whereClause = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_IN] + 
						  " BETWEEN \"" + strTimeInStart + "\" AND \"" + strTimeInEnd + "\"";		
		}	
	}	
}

if(!srcScheduleSymbol.isSelectTimeOut())
{
	if(srcScheduleSymbol.getTimeOutStart()!=null && srcScheduleSymbol.getTimeOutEnd()!=null)
	{
		String strTimeOutStart = Formater.formatDate(srcScheduleSymbol.getTimeOutStart(), "HH:mm:ss");
		String strTimeOutEnd = Formater.formatDate(srcScheduleSymbol.getTimeOutEnd(), "HH:mm:ss");		
		if(whereClause!=null && whereClause.length()>0)
		{
			whereClause = whereClause + " AND " + PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT] + 
						  " BETWEEN \"" + strTimeOutStart + "\" AND \"" + strTimeOutEnd + "\"";		
		}
		else
		{
			whereClause = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_TIME_OUT] + 
						  " BETWEEN \"" + strTimeOutStart + "\" AND \"" + strTimeOutEnd + "\"";		
		}	
	}	
}

String orderClause = PstScheduleSymbol.fieldNames[PstScheduleSymbol.FLD_SYMBOL];

/*count list All ScheduleSymbol*/
int vectSize = PstScheduleSymbol.getCount(whereClause);

ScheduleSymbol scheduleSymbol = ctrlScheduleSymbol.getScheduleSymbol();
msgString =  ctrlScheduleSymbol.getMessage();

/*switch list ScheduleSymbol*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	//start = PstScheduleSymbol.findLimitStart(scheduleSymbol.getOID(),recordToGet, whereClause,orderClause);
	oidScheduleSymbol = scheduleSymbol.getOID();
}

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlScheduleSymbol.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listScheduleSymbol = PstScheduleSymbol.list(start,recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listScheduleSymbol.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listScheduleSymbol = PstScheduleSymbol.list(start, recordToGet, whereClause , orderClause);
}



%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Master Data Schedule Symbol</title>
<script language="JavaScript">
function cmdBackToSearch(){
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.BACK)%>";
	document.frmschedulesymbol.action="srcschedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}

function cmdAdd(){
	document.frmschedulesymbol.hidden_schedule_id.value="0";
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.ADD)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(prevCommand)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}

function cmdCheckCategory(){
    /*
    <%
    if(iCommand==Command.ADD){%>
        document.frmschedulesymbol.hidden_schedule_id.value="0";
        document.frmschedulesymbol.command.value="<%=String.valueOf(Command.ADD)%>";
    <%}else{%>
        document.frmschedulesymbol.hidden_schedule_id.value=oidScheduleSymbol;
        document.frmschedulesymbol.command.value="<%=String.valueOf(Command.EDIT)%>";
    <%}
    %>
	document.frmschedulesymbol.hidden_cat_oid_select.value=document.frmschedulesymbol.<%=FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_SCHEDULE_CATEGORY_ID]%>.value;
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(iCommand)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
         */
}

function cmdAsk(oidScheduleSymbol){
	document.frmschedulesymbol.hidden_schedule_id.value=oidScheduleSymbol;
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.ASK)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(prevCommand)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}

function cmdConfirmDelete(oidScheduleSymbol){
	document.frmschedulesymbol.hidden_schedule_id.value=oidScheduleSymbol;
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.DELETE)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(prevCommand)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}

function cmdSave(){
	if (document.all.radioTime[0].checked) {
		//document.all.tableTime.style.display = 'none';
		document.frmschedulesymbol.<%=frmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_TIME_IN] + "_hr"%>.value = "00";
		document.frmschedulesymbol.<%=frmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_TIME_IN] + "_mi"%>.value = "00";
		document.frmschedulesymbol.<%=frmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_TIME_OUT] + "_hr"%>.value = "00";
		document.frmschedulesymbol.<%=frmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_TIME_OUT] + "_mi"%>.value = "00";
	}
	else {
		//document.all.tableTime.style.display = 'block';
	}

	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.SAVE)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(prevCommand)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
	}

function cmdEdit(oidScheduleSymbol){
	document.frmschedulesymbol.hidden_schedule_id.value=oidScheduleSymbol;
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.EDIT)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(prevCommand)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
	}

function cmdCancel(oidScheduleSymbol){
	document.frmschedulesymbol.hidden_schedule_id.value=oidScheduleSymbol;
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.EDIT)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(prevCommand)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}

function cmdBack(){
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.BACK)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
	}

function cmdListFirst(){
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.FIRST)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(Command.FIRST)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}

function cmdListPrev(){
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.PREV)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(Command.PREV)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
	}

function cmdListNext(){
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.NEXT)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(Command.NEXT)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}

function cmdListLast(){
	document.frmschedulesymbol.command.value="<%=String.valueOf(Command.LAST)%>";
	document.frmschedulesymbol.prev_command.value="<%=String.valueOf(Command.LAST)%>";
	document.frmschedulesymbol.action="schedulesymbol.jsp";
	document.frmschedulesymbol.submit();
}
function fnTrapKD(){
	//alert(event.keyCode);
	switch(event.keyCode) {
		case <%=String.valueOf(LIST_PREV)%>:
			cmdListPrev();
			break;
		case <%=String.valueOf(LIST_NEXT)%>:
			cmdListNext();
			break;
		case <%=String.valueOf(LIST_FIRST)%>:
			cmdListFirst();
			break;
		case <%=String.valueOf(LIST_LAST)%>:
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
      <!-- #EndEditable --> </td>
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
                <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" --> 
                  Master Data &gt; Schedule &gt; Schedule Symbol<!-- #EndEditable --> 
                  </strong></font> </td>
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
                                  <td valign="top"> <!-- #BeginEditable "content" --> 
                                    <form name="frmschedulesymbol" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=String.valueOf(iCommand)%>"> 
                                      <input type="hidden" name="vectSize" value="<%=String.valueOf(vectSize)%>">
                                      <input type="hidden" name="start" value="<%=String.valueOf(start)%>">
                                      <input type="hidden" name="prev_command" value="<%=String.valueOf(prevCommand)%>">
                                      <input type="hidden" name="hidden_schedule_id" value="<%=String.valueOf(oidScheduleSymbol)%>">
                                      <input type="hidden" name="hidden_cat_oid_select" value="">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="8"  colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                              <tr align="left" valign="top"> 
                                                <td height="14" valign="middle" colspan="3" >&nbsp;<span class="listtitle">Schedule 
                                                  Symbol List </span></td>
                                              </tr>
                                              <%
											  if (listScheduleSymbol.size()>0)
											  {
											  %>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(listScheduleSymbol,oidScheduleSymbol)%> </td>
                                              </tr>
                                              <%  
											  } 
											  %>
                                              <tr align="left" valign="top"> 
                                                <td height="8" align="left" colspan="3" class="command"> 
                                                  <span class="command"> 
                                                  <% 
                                                  int cmd = 0;
                                                  if ((iCommand == Command.FIRST || iCommand == Command.PREV )|| 
                                                                (iCommand == Command.NEXT || iCommand == Command.LAST))
                                                  { 		
                                                                        cmd =iCommand; 
                                                  }			
                                                  else
                                                  {
                                                          if(iCommand == Command.NONE || prevCommand == Command.NONE)
                                                                cmd = Command.FIRST;
                                                          else 
                                                                cmd =prevCommand; 
                                                  } 
                                                  ctrLine.setLocationImg(approot+"/images");
                                                  ctrLine.initDefault();
												 
                                                  out.println(ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet));
												  %>
												  </span> </td>
                                              </tr>
											  <%
											  if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmScheduleSymbol.errorSize()<1))
											  { 
											  %>
                                              <tr align="left" valign="top"> 
                                                <td> 
                                                  <table cellpadding="0" cellspacing="0" border="0" width="50%">
                                                    <tr> 
													<%if(privAdd){
													%>
                                                      <td width="5%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add New Symbol"></a></td>
                                                      <td width="2%"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td width="37%" valign="middle"> 
                                                        <a href="javascript:cmdAdd()" class="command">Add 
                                                        New Symbol</a> </td>
														<%}%>
                                                      <td width="5%"><a href="javascript:cmdBackToSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image2611','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image2611" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back To Search Symbol"></a></td>
                                                      <td width="2%"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td width="49%" height="22" valign="middle"> 
                                                        <a href="javascript:cmdBackToSearch()" class="command">Back 
                                                        To Search Symbol</a> </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                              <%
											  }
											  %>
                                            </table>
                                          </td>
                                        </tr>
                                        <tr> 
                                          <td>&nbsp; </td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                          <td height="8" valign="middle" colspan="3"> 
                                            <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmScheduleSymbol.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                              <tr> 
                                                <td colspan="2" class="listtitle"><%=oidScheduleSymbol==0?"Add":"Edit"%> Schedule Symbol 
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td height="100%" width="35%" colspan="2"> 
                                                  <table border="0" cellspacing="2" cellpadding="2" width="79%">
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%">&nbsp;</td>
                                                      <td width="79%" class="comment">*) 
                                                        entry required </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%"> 
                                                        Schedule Category </td>
                                                      <td width="79%"> 
                                                        <%-- <input type="text" name="<%=frmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_SCHEDULE_CATEGORY_ID] %>"  value="<%= scheduleSymbol.getScheduleCategoryId() %>" class="elemenForm"> --%>
                                                        <% 
                                                            Vector locationid_value = new Vector(1,1);
                                                            Vector locationid_key = new Vector(1,1);
                                                            String sel_locationid = "" + (scheduleSymbol.getScheduleCategoryId()); //locker.getLocationId();
                                                            
                                                            //Untuk vector category
                                                            Vector list = new Vector(1,1);
                                                            list = PstScheduleCategory.listAll();
                                                        /*    Vector vSpecialLeaveOid = new Vector(1,1);
                                                            for(int i=0;i<list.size();i++){
                                                                ScheduleCategory scheduleCategory = (ScheduleCategory) list.get(i);
                                                                if(PstScheduleCategory.CATEGORY_SPECIAL_LEAVE==scheduleCategory.getCategoryType()){
                                                                    vSpecialLeaveOid.add(String.valueOf(scheduleCategory.getOID()));
                                                                }
                                                            }
                                                          */  
                                                            for (int i = 0; i < list.size(); i++) {
                                                                    ScheduleCategory scheduleCategory = (ScheduleCategory) list.get(i);
                                                                    locationid_key.add(scheduleCategory.getCategory());
                                                                    locationid_value.add(String.valueOf(scheduleCategory.getOID()));
                                                            }
                                                        %>
                                                        
                                                        <%= ControlCombo.draw(FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_SCHEDULE_CATEGORY_ID],null, sel_locationid, locationid_value, locationid_key,"onChange=\"javascript:cmdCheckCategory()\"") %> * <%=frmScheduleSymbol.getErrorMsg(FrmScheduleSymbol.FRM_FIELD_SCHEDULE_CATEGORY_ID)%></td>
                                                        
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%"> 
                                                        Symbol</td>
                                                      <td width="79%"> 
                                                        <input type="text" maxlength="10" name="<%=frmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_SYMBOL] %>"  value="<%= scheduleSymbol.getSymbol() %>" class="elemenForm">
                                                        * <%=frmScheduleSymbol.getErrorMsg(FrmScheduleSymbol.FRM_FIELD_SYMBOL)%></td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%"> 
                                                        Schedule</td>
                                                      <td width="79%"> 
                                                        <input type="text" name="<%=frmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_SCHEDULE] %>"  value="<%= scheduleSymbol.getSchedule() %>" class="elemenForm">
                                                        * <%=frmScheduleSymbol.getErrorMsg(FrmScheduleSymbol.FRM_FIELD_SCHEDULE)%>
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="21%"> 
                                                        Time </td>
                                                      <td width="79%"> 
                                                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                          <script language="javascript">
                                                            function timeCheck() {
                                                                  if (document.all.radioTime[0].checked) {
                                                                        document.all.tableTime.style.display = 'none';
                                                                  }
                                                                  else {
                                                                    document.all.tableTime.style.display = 'block';
                                                                  }
                                                                }
                                                          </script>
                                                          <tr> 
                                                            <td width="10%"> 
                                                              <input type="radio" name="radioTime" value="radiobutton" style="border-color:'#DDDDDD'" onClick="javascript:timeCheck();">&nbsp;No
                                                            </td>
                                                            <td width="90%" align="left"> <div align="left" > &nbsp;</div>
                                                            </td>                                                            
                                                          </tr>
                                                          <tr> 
                                                            <td width="10%"> 
                                                              <input type="radio" name="radioTime" value="radiobutton" style="border-color:'#DDDDDD'" onClick="javascript:timeCheck();">&nbsp;Yes
                                                            </td>
                                                            <td width="90%" > <div align="left" > &nbsp; </div>
                                                            </td>                                                            
                                                          </tr>
                                                          <tr id="tableTime"> 
                                                            <td width="10%">&nbsp;</td>
                                                            <td width="90%"> 
                                                              <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                                                <tr> 
                                                                  <td width="19%">Time 
                                                                    In</td>
                                                                  <td width="2%"> 
                                                                    <div align="center"></div>
                                                                  </td>
                                                                  <td  nowrap> 
                                                                    <%= ControlDate.drawTimeVer2(FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_TIME_IN], scheduleSymbol.getTimeIn(), "elemenForm", 24, 1, 0) %> 
                                                                  <td width="19%" nowrap>&nbsp;&nbsp;&nbsp;Break Out 
                                                                    </td>
                                                                  <td width="2%"> 
                                                                    <div align="center"></div>
                                                                  </td>
                                                                  <td  nowrap> 
                                                                      <%
                                                                        Date BoBi = new Date();
                                                                        if(scheduleSymbol==null || scheduleSymbol.getBreakOut()==null || scheduleSymbol.getBreakIn()==null){
                                                                            BoBi.setHours(0);
                                                                            BoBi.setMinutes(0);
                                                                            BoBi.setSeconds(0);
                                                                        }
                                                                        %>
                                                                    <%= ControlDate.drawTimeVer2(FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_BREAK_OUT], scheduleSymbol!=null && scheduleSymbol.getBreakOut()!=null ? scheduleSymbol.getBreakOut(): BoBi, "elemenForm", 24, 1, 0) %> 
                                                                  </td>
                                                                  <td nowrap>To set NO BREAK, set same time for break out and break in</td>
                                                                </tr>
                                                                <tr> 
                                                                  <td width="19%" nowrap>Time 
                                                                    Out </td>
                                                                  <td width="2%"> 
                                                                    <div align="center"></div>
                                                                  </td>
                                                                  <td width="79%" nowrap> 
                                                                    <%= ControlDate.drawTimeVer2(FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_TIME_OUT], scheduleSymbol.getTimeOut(), "elemenForm", 24, 1, 0) %>
                                                                  </td>
                                                                  <td width="19%" nowrap>&nbsp;&nbsp;&nbsp;Break In</td>
                                                                  <td width="2%"> 
                                                                    <div align="center"></div>
                                                                  </td>
                                                                  <td width="79%" nowrap> 
                                                                    <%= ControlDate.drawTimeVer2(FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_BREAK_IN], scheduleSymbol!=null && scheduleSymbol.getBreakIn()!=null ? scheduleSymbol.getBreakIn():BoBi, "elemenForm", 24, 1, 0) %>
                                                                  </td>
                                                                  <td>&nbsp;</td> 
                                                                </tr>
                                                                <tr>
                                                                    <td width="19%" nowrap>Transport Allowance</td>
                                                                    <td width="17%" nowrap>
                                                                        <select name="<%=FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_TRANSPORT_ALLOWANCE] %>">
                                                                            
                                                                            <%
                                                                            int val = scheduleSymbol.getTransportAllowance();
                                                                            if (val != 0 && val > 0){
                                                                                for(int i=0;i<3;i++){
                                                                                    if (i == val){
                                                                                        %>
                                                                                        <option value="<%=i%>" selected="selected"><%=i%></option>
                                                                                        <%
                                                                                    } else {
                                                                                        %>
                                                                                        <option value="<%=i%>"><%=i%></option>
                                                                                        <%
                                                                                    }                                                                                 
                                                                                }
                                                                            } else {
                                                                                %>
                                                                                <option value="0">0</option>
                                                                                <option value="1">1</option>
                                                                                <option value="2">2</option>
                                                                                <%
                                                                            }
                                                                            %>
                                                                        </select>
                                                                    </td>
                                                                    <td width="19%" nowrap>Night Allowance</td>
                                                                    <td width="17%" nowrap>
                                                                        <select name="<%=FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_NIGHT_ALLOWANCE] %>">
                                                                            <%
                                                                            int nilai = scheduleSymbol.getNightAllowance();
                                                                            if (nilai != 0 && nilai > 0){
                                                                                for(int i=0;i<3;i++){
                                                                                    if (i == nilai){
                                                                                        %>
                                                                                        <option value="<%=i%>" selected="selected"><%=i%></option>
                                                                                        <%
                                                                                    } else {
                                                                                        %>
                                                                                        <option value="<%=i%>"><%=i%></option>
                                                                                        <%
                                                                                    }                                                                                 
                                                                                }
                                                                            } else {
                                                                                %>
                                                                                <option value="0">0</option>
                                                                                <option value="1">1</option>
                                                                                <option value="2">2</option>
                                                                                <%
                                                                            }
                                                                            %>
                                                                        </select>
                                                                    </td>
                                                                </tr>
                                                              </table>
                                                            </td>
                                                          </tr>
                                                          <script language="javascript">
                                                              var strTimeIn = "<%= scheduleSymbol.getTimeIn()%>";
                                                              var strTimeOut = "<%= scheduleSymbol.getTimeOut()%>";

                                                              if (strTimeIn == strTimeOut) {
                                                                document.all.radioTime[0].checked = true;
                                                              }
                                                              else {
                                                                document.all.radioTime[1].checked = true;
                                                              }
                                                        timeCheck();
                                                        checkPeriode();
                                                      </script>
                                                  </table>
                                                </td>
                                              </tr>
                                              <%
                                              boolean isShow = true;
                                              /*for(int i=0;i<vSpecialLeaveOid.size();i++){
                                                long oidSp = Long.parseLong(String.valueOf((String)vSpecialLeaveOid.get(i)));
                                                if(oidSp == oidCatSelect){
                                                    isShow = true;
                                                }
                                              }
                                              */
                                              
                                              if(isShow){//check show
                                              %>
                                              <tr>
                                                  <td width="21%">Work days</td>
                                                  <td width="79%"> 
                                                      <select name="<%=FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_WORK_DAYS] %>">
                                                            <%
                                                            int wd = scheduleSymbol.getWorkDays();
                                                            if (wd != 0 && wd > 0){
                                                                for(int i=1;i<=2;i++){
                                                                    if (i == wd){
                                                                        %>
                                                                        <option value="<%=i%>" selected="selected"><%=i%></option>
                                                                        <%
                                                                    } else {
                                                                        %>
                                                                        <option value="<%=i%>"><%=i%></option>
                                                                        <%
                                                                    }                                                                                 
                                                                }
                                                            } else {
                                                                %>
                                                                <option value="1">1</option>
                                                                <option value="2">2</option>
                                                                <%
                                                            }
                                                            %>
                                                          
                                                      </select>
                                                  </td>
                                              </tr>
                                              <!--for special leave-->
                                              <tr id="tableSpecialLeave">
                                                  <td width="21%">Max entitle</td>
                                                  <td width="79%"> 
                                                     <input type="text" name="<%=FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_MAX_ENTITLE] %>"  value="<%=String.valueOf(scheduleSymbol.getMaxEntitle()>0?scheduleSymbol.getMaxEntitle():0)%>" class="elemenForm">
                                                     days
                                                  </td>
                                                </tr>
                                                <tr> 
                                                  <td width="21%">Period</td>
                                                  <td width="79%">
                                                    Per &#32;
                                                    <input type="text" name="<%=FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_PERIODE] %>"  value="<%=String.valueOf(scheduleSymbol.getPeriode()>0?scheduleSymbol.getPeriode():0)%>" class="elemenForm">
                                                    &#32;
                                                    <% 
                                                        Vector periodeTypeValue = new Vector(1,1);
                                                        Vector periodeTypeKey = new Vector(1,1);
                                                        String periodeTypeSelect = String.valueOf(scheduleSymbol.getPeriodeType());
                                                        for (int i = 0; i < PstScheduleSymbol.fieldNamesPeriodeType.length; i++) {
                                                                periodeTypeKey.add(PstScheduleSymbol.fieldNamesPeriodeType[i]);
                                                                periodeTypeValue.add(String.valueOf(i));
                                                        }
                                                    %>
                                                    <%= ControlCombo.draw(FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_PERIODE_TYPE],null, periodeTypeSelect, periodeTypeValue, periodeTypeKey) %>  <%=frmScheduleSymbol.getErrorMsg(FrmScheduleSymbol.FRM_FIELD_SCHEDULE_CATEGORY_ID)%></td>
                                                  </td>
                                                </tr>
                                                <tr> 
                                                  <td width="21%">May Taken After </td>
                                                  <td width="79%"> 
                                                     <input type="text" name="<%=FrmScheduleSymbol.fieldNames[FrmScheduleSymbol.FRM_FIELD_MIN_SERVICE] %>"  value="<%=String.valueOf(scheduleSymbol.getMinService()>0?scheduleSymbol.getMinService():0)%>" class="elemenForm">
                                                     Month Of Service
                                                  </td>
                                                        
                                            </tr>
                                            <%
                                            }//end check show
                                            %>
                                              <tr align="left" valign="top" > 
                                                <td colspan="2" class="command"> 
                                                  <%
                                                ctrLine.setLocationImg(approot+"/images");
                                                ctrLine.initDefault();
                                                ctrLine.setTableWidth("80%");
                                                String scomDel = "javascript:cmdAsk('"+oidScheduleSymbol+"')";
                                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidScheduleSymbol+"')";
                                                String scancel = "javascript:cmdEdit('"+oidScheduleSymbol+"')";
                                                ctrLine.setBackCaption("Back to List Symbol");
                                                ctrLine.setCommandStyle("buttonlink");
                                                ctrLine.setAddCaption("Add Symbol");
                                                ctrLine.setSaveCaption("Save Symbol");
                                                ctrLine.setDeleteCaption("Delete Symbol");
                                                ctrLine.setConfirmDelCaption("Yes Delete Symbol");

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
                                                  <%= ctrLine.drawImage(iCommand, iErrCode, msgString)%> </td>
                                              </tr>
                                              <tr> 
                                                <td width="35%">&nbsp;</td>
                                                <td width="65%">&nbsp;</td>
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
                                    <!-- #EndEditable --> </td>
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
