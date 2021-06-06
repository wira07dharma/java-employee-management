
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

<%-- YANG INI BELUM DIEDIT --%>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_EMPLOYEE, AppObjInfo.OBJ_EMPLOYEE_RELIGION); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!

    public String drawList(int iCommand, FrmRace frmObject, Race objEntity, Vector objectClass, long raceId)
    {
            ControlList ctrlist = new ControlList(); 
            ctrlist.setAreaWidth("25%");
            ctrlist.setListStyle("listgen");
            ctrlist.setTitleStyle("listgentitle");
            ctrlist.setCellStyle("listgensell");
            ctrlist.setHeaderStyle("listgentitle");
            ctrlist.addHeader("Race","100%");

            ctrlist.setLinkRow(0);
            ctrlist.setLinkSufix("");
            Vector lstData = ctrlist.getData(); 
            Vector lstLinkData = ctrlist.getLinkData();
            Vector rowx = new Vector(1,1);
            ctrlist.reset();
            int index = -1;

            for (int i = 0; i < objectClass.size(); i++) {
                 Race race = (Race)objectClass.get(i);                 
                                  
                 rowx = new Vector();

                 // higlighting
                 if(raceId == race.getOID())
                         index = i; 

                 if(index == i && (iCommand == Command.EDIT || iCommand == Command.ASK))					
                        rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmRace.FRM_FIELD_RACE_NAME] +"\" value=\""+race.getRaceName()+"\" class=\"formElemen\" size=\"30\">");
                 else
                        rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(race.getOID())+"')\">"+race.getRaceName()+"</a>");

                 lstData.add(rowx);
            }

            rowx = new Vector();

            if(iCommand == Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize() > 0)){ 
                 rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmRace.FRM_FIELD_RACE_NAME] +"\" value=\""+objEntity.getRaceName()+"\" class=\"formElemen\" size=\"30\">");
            }

            lstData.add(rowx);

            return ctrlist.draw();
    }

%>

<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidRace = FRMQueryString.requestLong(request, "hidden_race_id");

/*variable declaration*/
String msgString = "";
int iErrCode = FRMMessage.NONE;

int recordToGet = 10;
String whereClause = "";
String orderClause = PstRace.fieldNames[PstRace.FLD_RACE_NAME]+ " ASC ";

CtrlRace ctrlRace = new CtrlRace(request);
ControlLine ctrLine = new ControlLine();
Vector listRace = new Vector(1,1);

/*switch statement */
iErrCode = ctrlRace.action(iCommand, oidRace);
/* end switch*/

msgString = ctrlRace.getMessage();
Race race = ctrlRace.getRace();
FrmRace frmRace = ctrlRace.getForm();

/*count list All Race*/
int vectSize = PstRace.getCount(whereClause);

/*switch list Race*/
if((iCommand == Command.FIRST || iCommand == Command.PREV )||
   (iCommand == Command.NEXT || iCommand == Command.LAST)){
        start = ctrlRace.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listRace = PstRace.list(start, recordToGet, whereClause , orderClause);

// debug
System.out.println("Row = " + listRace.size());
Enumeration list = listRace.elements();
while(list.hasMoreElements()) {
    System.out.println("Race value = " + ((Race)list.nextElement()).getRaceName());
}

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listRace.size() < 1 && start > 0)
{
     if (vectSize - recordToGet > recordToGet)
         start = start - recordToGet;   //go to Command.PREV
     else{
         start = 0 ;
         iCommand = Command.FIRST;
         prevCommand = Command.FIRST; //go to Command.FIRST
     }
     
     listRace = PstRace.list(start, recordToGet, whereClause , orderClause);
}
%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Master Data Race</title>
<script language="JavaScript">
<!--

function cmdAdd(){
	document.frmRace.hidden_race_id.value="0";
	document.frmRace.command.value="<%=Command.ADD%>";
	document.frmRace.prev_command.value="<%=prevCommand%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdAsk(oidRace){
	document.frmRace.hidden_race_id.value=oidRace;
	document.frmRace.command.value="<%=Command.ASK%>";
	document.frmRace.prev_command.value="<%=prevCommand%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdConfirmDelete(oidRace){
	document.frmRace.hidden_race_id.value=oidRace;
	document.frmRace.command.value="<%=Command.DELETE%>";
	document.frmRace.prev_command.value="<%=prevCommand%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdSave(){
	document.frmRace.command.value="<%=Command.SAVE%>";
	document.frmRace.prev_command.value="<%=prevCommand%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdEdit(oidRace){
	document.frmRace.hidden_race_id.value=oidRace;
	document.frmRace.command.value="<%=Command.EDIT%>";
	document.frmRace.prev_command.value="<%=prevCommand%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdCancel(oidRace){
	document.frmRace.hidden_race_id.value=oidRace;
	document.frmRace.command.value="<%=Command.EDIT%>";
	document.frmRace.prev_command.value="<%=prevCommand%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdBack(){
	document.frmRace.command.value="<%=Command.BACK%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdListFirst(){
	document.frmRace.command.value="<%=Command.FIRST%>";
	document.frmRace.prev_command.value="<%=Command.FIRST%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdListPrev(){
	document.frmRace.command.value="<%=Command.PREV%>";
	document.frmRace.prev_command.value="<%=Command.PREV%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdListNext(){
	document.frmRace.command.value="<%=Command.NEXT%>";
	document.frmRace.prev_command.value="<%=Command.NEXT%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

function cmdListLast(){
	document.frmRace.command.value="<%=Command.LAST%>";
	document.frmRace.prev_command.value="<%=Command.LAST%>";
	document.frmRace.action="race.jsp";
	document.frmRace.submit();
}

//-------------- script form image -------------------

function cmdDelPict(oidRace){
	document.frmimage.hidden_race_id.value=oidRace;
	document.frmimage.command.value="<%=Command.POST%>";
	document.frmimage.action="race.jsp";
	document.frmimage.submit();
}

function fnTrapKD(){
	alert(event.keyCode);
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
//-->
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
                  Master Data > Race<!-- #EndEditable --> 
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
                                    <form name="frmRace" method ="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                      <input type="hidden" name="hidden_race_id" value="<%=oidRace%>">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td height="8"  colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                              <tr align="left" valign="top"> 
                                                <td height="8" valign="middle" colspan="3">&nbsp; 
                                                </td>
                                              </tr>
                                              <tr align="left" valign="top"> 
                                                <td height="14" valign="middle" colspan="3" class="listtitle">&nbsp;Race 
                                                  List </td>
                                              </tr>
                                              <%
                                                try{
                                                    // jika data kosong, tampilkan text field untuk input data
                                                    if((listRace == null || listRace.size()<1)&&(iCommand == Command.NONE))
                                                            iCommand = Command.ADD;  
                                               %>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(iCommand, frmRace, race, listRace, oidRace)%> 
                                                </td>
                                              </tr>
                                              <% } catch(Exception exc){ }%>
                                              
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
											 
                                              <%
                                              if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmRace.errorSize()<1)){
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
                                                        New Race</a></td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                                      <%}%>
                                              <%}%>
                                            </table> 
                                          </td>
                                        </tr>
                                        <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&(frmRace.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                                        <tr align="left" valign="top"> 
                                          <td height="8" valign="middle" width="17%">&nbsp;</td>
                                          <td height="8" colspan="2" width="83%">&nbsp; 
                                          </td>
                                        </tr>
                                        <tr align="left" valign="top" > 
                                          <td colspan="3" class="command"> 
                                            <%
                                                ctrLine.setLocationImg(approot+"/images");
                                                ctrLine.initDefault();
                                                ctrLine.setTableWidth("80%");
                                                String scomDel = "javascript:cmdAsk('"+oidRace+"')";
                                                String sconDelCom = "javascript:cmdConfirmDelete('"+oidRace+"')";
                                                String scancel = "javascript:cmdEdit('"+oidRace+"')";
                                                ctrLine.setBackCaption("Back to List Race");
                                                ctrLine.setCommandStyle("buttonlink");
                                                ctrLine.setAddCaption("Add Race");
                                                ctrLine.setSaveCaption("Save Race");
                                                ctrLine.setDeleteCaption("Delete Race");
                                                ctrLine.setConfirmDelCaption("Yes Delete Race");

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
                                      <%}%>
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
<!-- #EndEditable -->
<!-- #EndTemplate --></html>