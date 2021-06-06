
<% 
/*  
 * Page Name  		:  srcPosition.jsp
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
<!-- package wihita -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.session.attendance.*" %>
<%@ page import = "com.dimata.harisma.entity.employee.*" %>

<%@ include file = "../main/javainit.jsp" %>
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
<%@ include file = "../main/checktraining.jsp" %>

<!-- Jsp Block -->
<%
int iCommand = FRMQueryString.requestCommand(request);
String trainingName = FRMQueryString.requestString(request, "training_name");
String description = FRMQueryString.requestString(request, "description");
int orderBy = FRMQueryString.requestInt(request, "orderBy");

Vector vctDepartment = PstDepartment.list(0,0, "", PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
Vector vct = new Vector(1,1);

if(iCommand == Command.BACK) {
    vctDepartment = (Vector)session.getValue("SESS_TRAINING");
    
    Vector vctx = (Vector)session.getValue("SESS_SRC_TRAINING");
    trainingName = (String)vctx.get(0);
    description = (String)vctx.get(1);
    orderBy = Integer.parseInt((String)vctx.get(2));
}

/* added by Bayu */
/* filter department based on training priv. assigned */
if(trainType == PRIV_GENERAL) {
    vct = PstDepartment.list(0,0, "", PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
}
else {
    String filter = PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT_ID] + " = " + departmentOid;
    vct = PstDepartment.list(0,0, filter, PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]);
}
    

Vector secSelect = new Vector(1,1);

if(vct!=null && vct.size()>0){
	for(int i=0; i<vct.size(); i++){
		Department department = (Department)vct.get(i);
                                
		if(iCommand!=Command.NONE && iCommand!=Command.ADD){
			int ix = FRMQueryString.requestInt(request, "chx_"+department.getOID());
			if(ix==1){
				secSelect.add(department);
			}
		}
		else{
			secSelect.add(department);
		}
	}
}


// added by Bayu
if(iCommand == Command.BACK) {
    if(vctDepartment != null)
        for(int x=0; x<vctDepartment.size(); x++) {
            Department department = (Department)vctDepartment.get(x);
            secSelect.add(department);
        }
}

%>
<%!
	public String getSelected(Department dept, Vector secSelect){
	if(secSelect!=null && secSelect.size()>0){
		for(int i=0; i<secSelect.size(); i++){
			Department department = (Department)secSelect.get(i);
			if(department.getOID()==dept.getOID()){
				return "checked";
			}
		}
	}
	return "";
}
	

%>
<%
if(session.getValue("SESS_TRAINING")!=null){
session.removeValue("SESS_TRAINING");
}
session.putValue("SESS_TRAINING",secSelect);

if(iCommand==Command.LIST){
			response.sendRedirect("training.jsp?command="+Command.LIST +"&hidden_training_name="+trainingName+"&hidden_description="+description+"&hidden_sort_by="+orderBy);
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Master Data Training</title><style type="text/css">.test {display: block}</style>
<script language="JavaScript">
<!--
function cmdSearch(){ 
	document.frmsrctraining.command.value="<%=Command.LIST%>";
	document.frmsrctraining.action="srctraining.jsp";
	document.frmsrctraining.submit();
}
function cmdViewON(){
    document.getElementById("unit").style.display="table";
}
function cmdViewOFF(){
    document.getElementById("unit").style.display="none";
}
function fnTrapKD(){
   if (event.keyCode == 13) {
		document.all.aSearch.focus();
		cmdSearch();
   }
}

function setChecked(val) {
	dml=document.frmsrctraining;
	len = dml.elements.length;
	var i=0;
	for( i=0 ; i<len ; i++) {						
		dml.elements[i].checked = val;					
	}
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
</script>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../styles/tab.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
<!-- #EndEditable --> 
</head>
<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" onLoad="MM_preloadImages('<%=approot%>/images/BtnSearchOn.jpg')">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
    <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
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
                  Masterdata &gt; Training<!-- #EndEditable --> </strong></font> 
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
                                  <td valign="top"> <!-- #BeginEditable "content" --> 
                                    <form name="frmsrctraining" method="post" action="">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td valign="top" colspan="2"> 
                                            <table border="0" cellspacing="2" cellpadding="2" >
                                              <tr> 
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                              </tr>
                                              <tr> 
                                                <td>&nbsp;</td>
                                                <td> 
                                                  <div align="left">Name</div>
                                                </td>
                                                <td> 
                                                  <input type="text" name="training_name" value="<%=trainingName%>" class="elemenForm" onKeyDown="javascript:fnTrapKD()" size="40">
                                                </td>
												</tr>
												
												 <tr> 
                                                <td>&nbsp;</td>
                                                <td> 
                                                  <div align="left">Description</div>
                                                </td>
                                                <td> 
                                                  <input type="text" name="description" value="<%=description%>" class="elemenForm" onKeyDown="javascript:fnTrapKD()" size="40">
                                                </td>
						</tr>
                                                <tr>
                                                    <td>&nbsp;</td>
                                                    <td>View <%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></td>
                                                    <td>
                                                        <a href="javascript:cmdViewON()">ON</a>
                                                        <a href="javascript:cmdViewOFF()">OFF</a>
                                                    </td>
                                                </tr>
                                                <tr align="left" valign="top">
                                                    <td colspan="3">
                                                        <div id="unit" style="display: none">
                                                        <table>
                                                            <tr>
                                                                
                                                                <td>&nbsp;</td>
                                          <td height="21" nowrap align="left" valign="top"><%=dictionaryD.getWord(I_Dictionary.DEPARTMENT) %></td> 
                                          <% if(vct!= null && vct.size() == 1) {
                                                Department department = (Department)vct.get(0);
                                          %>
                                                <td>
                                                    <input type="hidden" name="chx_<%=department.getOID()%>" value="1"><%=department.getDepartment()%>
                                                </td>
                                                
                                          <% } else if(vct!=null && vct.size()>0) { %>
                                                <td>
                                                <%-- for(int i=0; i<vct.size(); i++) {
                                                        Department department = (Department)vct.get(i);
                                                        String isSelect = getSelected(department, secSelect);%>

                                                        <input type="checkbox" name="chx_<%=department.getOID()%>" value="1" <%=isSelect%>><%=department.getDepartment()%>                                               
                                                <% } --%>	
                                            <%
                                              if(vct!=null && vct.size()>0){
                                                    int itr = vct.size() / 3;

                                                    if(vct.size() % 3 > 0){
                                                        itr++;
                                                    }

                                                    int index = 0;
                                            %>
                                                    <table cellpadding="0" cellspacing="0">
                                                    
                                                    <% for(int i=0; i<itr; i++) {
														
                                                        if(i > 0){
                                                            index = (3*i);
                                                        }
                                                        %>
                                                        <tr>
                                                        <td>
                                                        <%
                                                              Department dep1 = new Department();
                                                              String isSelect = "";

                                                              if(index < vct.size()) {
                                                                    dep1 = (Department)vct.get(index);
                                                                    isSelect = getSelected(dep1, secSelect);
                                                                    %>
                                                                    <input type="checkbox" name="chx_<%=dep1.getOID()%>" value="1" <%=isSelect%>><%=dep1.getDepartment()%> 
                                                           <% } %>
                                                        </td>
                                                        <td> 
                                                            <%
                                                              Department dep2 = new Department();
                                                              index = index +  1;
                                                              
                                                              if(index < vct.size()){
                                                                    dep2 = (Department)vct.get(index);
                                                                    isSelect = getSelected(dep2, secSelect);
                                                                    %>
                                                                    <input type="checkbox" name="chx_<%=dep2.getOID()%>" value="1" <%=isSelect%>><%=dep2.getDepartment()%> 
                                                            <% } %>
                                                        </td>
                                                        <td> 
                                                            <%
                                                              Department dep3 = new Department();
                                                              index = index + 1;
                                                              
                                                              if(index < vct.size()){
                                                                    dep3 = (Department)vct.get(index);
                                                                    isSelect = getSelected(dep3, secSelect);
                                                                    %>
                                                                    <input type="checkbox" name="chx_<%=dep3.getOID()%>" value="1" <%=isSelect%>><%=dep3.getDepartment()%> 
                                                            <% }  %>
                                                        </td>
                                                    </tr>
                                                    <%}%>
                                                    </table>
                                                <%}%>
                                                </td>  
                                                <td>&nbsp;</td>
                                                <td>&nbsp;</td>
                                            <% } %>
                                                                
                                                            </tr>
                                                            
                                                            <% if(trainType == PRIV_GENERAL) { %>
                                                            <tr align="left" valign="top"> 
                                                              <td  valign="middle">&nbsp;</td>
                                                              <td  nowrap align="left">&nbsp; </td>
                                                                  <td height="8" colspan="2">
                                                                      <a href="javascript:setChecked(1)">Select All Department </a> &nbsp;&nbsp;| &nbsp;&nbsp;
                                                                      <a href="javascript:setChecked(0)">Release All</a>
                                                                  </td>				  
                                                            </tr>
                                                            <% } %>
                                                        </table>
                                                        </div>
                                                    </td>
                                                                                    
                                            </tr>
                                              	
                                              <tr> 
                                                <td>&nbsp;</td>
                                                <td>Sort By</td>
                                                 <td> <%=ControlCombo.draw("orderBy","formElemen",null, ""+orderBy, PstTraining.getOrderValue(), PstTraining.getOrderKey(), " onkeydown=\"javascript:fnTrapKD()\"") %></td>
                                              </tr>
                                              <tr> 
                                                <td>&nbsp;</td>
                                                <td> 
                                                  <div align="left"></div>
                                                </td>
                                                <td> 
                                                  <table border="0" cellpadding="0" cellspacing="0">
                                                    <tr> 
                                                      <td><a href="javascript:cmdSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image10','','<%=approot%>/images/BtnSearchOn.jpg',1)" id="aSearch"><img name="Image10" border="0" src="<%=approot%>/images/BtnSearch.jpg" width="24" height="24" alt="Search Section"></a></td>
                                                      <td class="command" nowrap><a href="javascript:cmdSearch()">Search 
                                                        Training</a> </td>
                                                    </tr>
                                                  </table>
                                                </td>
                                              </tr>
                                            </table>
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
<!-- #BeginEditable "script" --> <!-- #EndEditable --> 
<!-- #EndTemplate --></html>
