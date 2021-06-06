<%@ page language="java" %>
<%@ include file = "../main/javainit.jsp" %>

<!--package aiso-->
<%@ page import = "com.dimata.aiso.entity.masterdata.*" %>
<%@ page import = "com.dimata.aiso.form.masterdata.*" %>

<!--package java-->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.util.*" %>
<!--package qdep-->
<%@ page import = "com.dimata.qdep.form.*" %>
<%@ page import = "com.dimata.qdep.entity.*" %>

<!-- JSP Block -->
<%! 
/* this constant used to list text of listHeader */
public static final String textJspTitle[][] = {
	{"Nomor","Level","Status"},
	{"Number","Level","Status"}	
};

public String drawListClass(Vector objectClass, int language){
	ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("97%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	//ctrlist.setCellStyleOdd("listgensellOdd");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader(textJspTitle[language][0],"70%","0","0");
	ctrlist.addHeader(textJspTitle[language][1],"15%","0","0");
	ctrlist.addHeader(textJspTitle[language][2],"15%","0","0");

	ctrlist.setLinkRow(0);
   	ctrlist.setLinkSufix("");
	
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();						
	
   	String psn = ""; 
	int lvl = 0;
	String strLvl = "";
	String link = "";
	String closeLink ="";
	int index = -1;
	try { 								
		for (int i = 0; i < objectClass.size(); i++) 
		{
			 Perkiraan perkiraan = (Perkiraan)objectClass.get(i);
		     psn = PstPerkiraan.arrStrPostable[language][perkiraan.getPostable()];  

			 Vector rowx = new Vector();
			 lvl = perkiraan.getLevel();
			 switch(lvl){
			    case 2 : strLvl = "&nbsp;&nbsp;&nbsp;&nbsp;"; break;
			    case 3 : strLvl = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; break;
			    case 4 : strLvl = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; break;
			    case 5 : strLvl = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; break;
			    case 6 : strLvl = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"; break;
			    default : strLvl = "";
			 }
			 
			 String strAccountName = "";
			 if(language == com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN)
			 {			 
			 	strAccountName = perkiraan.getAccountNameEnglish();
			 }
			 else
			 {
			 	strAccountName = perkiraan.getNama();			 
			 }			 

			 rowx.add(strLvl+perkiraan.getNoPerkiraan()+"&nbsp;&nbsp;&nbsp;&nbsp;"+strAccountName);  
			 rowx.add(String.valueOf(lvl)); 
			 rowx.add(psn);

			 lstData.add(rowx);
		}
	} catch(Exception exc) {}
	return ctrlist.draw(); 
}
%>

<%
int iAccountGroup = FRMQueryString.requestInt(request,"accountchart_group"); 
String strHeader = FRMQueryString.requestString(request,"str_header");  
int iCommand = FRMQueryString.requestCommand(request);
String refreshMsg ="";        
if(iCommand==Command.REFRESH){
  refreshMsg=CtrlPerkiraan.refreshIdParent(iAccountGroup);
}

Vector listPerkiraan = PstPerkiraan.getAllAccount(iAccountGroup, 0);
%>
<!-- End of JSP Block -->
<html><!-- #BeginTemplate "/Templates/main-menu-left-frames.dwt" --><!-- DW6 -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>List Chart of Accounts</title>  
<!-- #EndEditable -->
<!-- #BeginEditable "headerscript" -->
<!-- #EndEditable -->
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="stylesheet" href="../style/main.css" type="text/css">
<link rel="StyleSheet" href="../dtree/dtree.css" type="text/css" />
	<script type="text/javascript" src="../dtree/dtree.js"></script>
</head> 

<body class="bodystyle" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">    
<table width="100%" border="0" cellspacing="1" cellpadding="1" height="100%">
  <tr> 
    <td width="91%" valign="top" align="left" bgcolor="#99CCCC"> 
      <table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
        <tr> 
          <td height="20" class="contenttitle" >&nbsp;<!-- #BeginEditable "contenttitle" --><!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td valign="top"><!-- #BeginEditable "content" -->


<link rel="stylesheet" href="../style/main.css" type="text/css">
  
<script language="JavaScript">
	window.focus();
</script>  
<table width="100%" border="0" cellspacing="3" cellpadding="2" height="100%">
  <tr> 
    <td width="80%" valign="top" align="center"> 
      <table width="100%" border="0" cellspacing="1" cellpadding="1">
        <tr> 
          <td>&nbsp;
               <% 
                 String refreshParent[] = {"SET ULANG INDUK PERKIRAAN","RESET PARENT ACCOUNT"};
                 if(iCommand==Command.REFRESH){ 
                  out.println(refreshParent[SESS_LANGUAGE>1 || SESS_LANGUAGE < 0? 0:SESS_LANGUAGE]);
                } 
                %>
          </td>
        </tr>
        <tr> 
          <td> 
            <div align="center"><b><font size="3"><%=strHeader%></font></b></div>
          </td>
        </tr>
        
    
        <tr> 
          <td> 
            <div align="center"><%=drawListClass(listPerkiraan,SESS_LANGUAGE)%></div>
          </td>
        </tr>
        
        <% if(iCommand==Command.REFRESH){ 
            %>
        <tr> 
          <td> 
            <div align="center"><%=refreshMsg%></div>
          </td>
        </tr>            
            <%
            } 
         %>
      </table>
    </td>
  </tr>
</table>
<!-- #EndEditable --></td>
        </tr>
        <tr> 
          <td height="100%"> 
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
            <p>&nbsp;</p>
          </td>
        </tr>
      </table>
    </td>
  </tr>
  <tr> 
    <td colspan="2" height="20" class="footer"> 
      <%@ include file = "../main/footer.jsp" %>
    </td>
  </tr>
</table>
</body>
<!-- #EndTemplate --></html>
