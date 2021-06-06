<%-- 
    Document   : more_detail
    Created on : May 19, 2016, 4:55:23 PM
    Author     : Dimata 007
--%>

<%@page import="org.json.*"%>
<%@page import="com.dimata.qdep.db.DBHandler"%>
<%@page import="com.dimata.harisma.entity.log.ChangeValue"%>
<%@page import="com.dimata.harisma.entity.log.DictionaryField"%>
<%@page import="com.dimata.qdep.entity.I_DocStatus"%>
<%@page import="com.dimata.harisma.entity.log.PstLogSysHistory"%>
<%@page import="com.dimata.harisma.entity.log.LogSysHistory"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ include file = "../../main/javainit.jsp" %>
<style type="text/css">
    body {
        color: #575757;
        margin: 0;
        padding: 0;
        font-size: 12px;
        font-family: sans-serif;
    }
    .tblStyle {border-collapse: collapse; }
    .tblStyle td {padding: 5px 7px; border: 1px solid #CCC; font-size: 12px; }
    .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}

    .content-main {
        padding: 21px;
    }
    .btn {
        background-color: #DDD;
        border-radius: 3px;
        font-family: Arial;
        border-radius: 3px;
        color: #575757;
        font-size: 12px;
        padding: 7px 11px 7px 11px;
        text-decoration: none;
    }

    .btn:hover {
        color: #474747;
        background-color: #CCC;
        text-decoration: none;
    }
    #style_add {
        font-weight: bold;
        color: #677A1F;
        font-size: 11px;
        background-color: #E8F5BA;
        padding: 3px 5px;
        border-radius: 3px;
    }
    #style_edit {
        font-weight: bold;
        color: #257865;
        font-size: 11px;
        background-color: #BAF5E7;
        padding: 3px 5px;
        border-radius: 3px;
    }
    #style_delete {
        font-weight: bold;
        color: #8F2F3A;
        font-size: 11px;
        background-color: #F5CBD0;
        padding: 3px 5px;
        border-radius: 3px;
    }
    #style_login {
        font-weight: bold;
        color: #B83916;
        font-size: 11px;
        background-color: #FFD5C9;
        padding: 3px 5px;
        border-radius: 3px;
    }
    .header {
        position: fixed;
        width: 100%;
        padding: 21px;
        color:#FFF;
        border-top: 1px solid #CCC;
        border-bottom: 1px solid #CCC;
        background-color: #EEE;
    }
    #inp {
        color: #474747;
        border: 1px solid #CCC;
        border-radius: 3px;
        padding: 5px 7px;
    }
</style>
<script type="text/javascript">
    function cmdPerintah(){
        document.frm.perintah.value="1";
        document.frm.action="more_detail.jsp";
        document.frm.submit();
    }
    function cmdApprove(oid, act){
        document.frm.oid_log.value=oid;
        document.frm.action_param.value=act;
        document.frm.action="more_detail.jsp";
        document.frm.submit();
    }
</script>
<%!
    public String parsingData(String data){
        String str = "";
        str = data.replaceAll(";", "<br>");
        return str;
    }
    
    public Vector<String> parsingDataVersi2(String data){
        Vector<String> vdata = new Vector();
        for (String retval : data.split(";")) {
            vdata.add(retval);
        }
        return vdata;
    }
    
    public String getTableDetail(String logDetail){
		String table = "";
		try {
			JSONObject jObject = new JSONObject(logDetail.trim());
			Iterator<?> keys = jObject.keys();
			
			table = "<table class=\"tblStyle\">";
			while( keys.hasNext() ) {
				table += "<tr>";
				String key = (String)keys.next();
				table += "<td class=\"title_tbl\">"+key+"</td>";
				if (jObject.get(key) instanceof JSONArray)
					{
						table += "<td><table class=\"tblStyle\">";
						// it's an array
						JSONArray urlArray = (JSONArray) jObject.get(key);
						for (int i = 0; i < 1; i++) {
							JSONObject object = urlArray.getJSONObject(i);
							Iterator<?> keysA = object.keys();
							table += "<tr>";
							table += "<td class=\"title_tbl\">no</td>";
							while( keysA.hasNext() ) {
								String keySA = (String)keysA.next();
								if ( object.get(keySA) instanceof JSONObject ) {
									table += "<td class=\"title_tbl\">"+keySA+"</td>";
								} else {
									table += "<td class=\"title_tbl\">"+keySA+"</td>";
								}
							}
							table += "</tr>";
						}
						for (int i = 0; i < urlArray.length(); i++) {
							JSONObject object = urlArray.getJSONObject(i);
							Iterator<?> keysA = object.keys();
							table += "<tr>";
							table += "<td>"+(i+1)+"</td>";
							while( keysA.hasNext() ) {
								String keySA = (String)keysA.next();
								if ( object.get(keySA) instanceof JSONObject ) {
									table += "<td>"+object.get(keySA)+"</td>";
								} else {
									table += "<td>"+object.get(keySA)+"</td>";
								}
							}
							table += "</tr>";

						}
					table += "</td></table>";
					}
				else if ( jObject.get(key) instanceof JSONObject ) {
					 table += "<td>"+jObject.get(key)+"</td>";
				} else {
					table += "<td>"+jObject.get(key)+"</td>";
				}
			}
		}catch (Exception exc){
			System.out.println(exc.toString());
		}
		
        return table;
    }
    
%>
<%
long logSysHistoryOid = FRMQueryString.requestLong(request, "oid");
long oidLog = FRMQueryString.requestLong(request, "oid_log");
int actionParam = FRMQueryString.requestInt(request, "action_param");
String approveNote = FRMQueryString.requestString(request, "approve_note");
int statusUpdate = 0;
String queryUp = "";
Date now = new Date();
if (oidLog != 0 && actionParam != 0){
    if (actionParam == 1){
        statusUpdate = PstLogSysHistory.approveLog(appUserIdSess, ""+now, approveNote, oidLog);
    }
    if (actionParam == 2){
        statusUpdate = PstLogSysHistory.notApproveLog(appUserIdSess, ""+now, approveNote, oidLog);
    }
}

LogSysHistory logSysHistory = new LogSysHistory();
if (logSysHistoryOid != 0){
    logSysHistory = PstLogSysHistory.fetchExc(logSysHistoryOid);
}
%>
<% if (logSysHistoryOid != 0){ %>
<form name="frm" action="">
    <input type="hidden" name="oid_log" value="" />
    <input type="hidden" name="action_param" value="" />

<div class="content-main">
    
    
        
        <%
            if (logSysHistory != null){
                String styleAction = "";
                if (logSysHistory.getLogUserAction().equals("ADD")){
                    styleAction = "style_add";
                }
                if (logSysHistory.getLogUserAction().equals("EDIT")){
                    styleAction = "style_edit";
                }
                if (logSysHistory.getLogUserAction().equals("DELETE")){
                    styleAction = "style_delete";
                }
                if (logSysHistory.getLogUserAction().equals("LOGIN")){
                    styleAction = "style_login";
                }
                %>
                <table style="padding: 0px; margin: 0px; font-size: 12px;">
                    <tr>
                        <td><strong>Module</strong></td>
                        <td> : <%= logSysHistory.getLogModule() %></td>
                    </tr>
                    <tr>
                        <td><strong>Action</strong></td>
                        <td> : <strong id="<%= styleAction %>"><%=logSysHistory.getLogUserAction()%></strong></td>
                    </tr>
                    <tr>
                        <td><strong>Status</strong></td>
                        <td> : <%= I_DocStatus.fieldDocumentStatus[logSysHistory.getLogStatus()] %></td>
                    </tr>
                </table>
                <div>&nbsp;</div>
                <%= getTableDetail(logSysHistory.getLogDetail()) %>
                <%
            }
        %>
    
</div>
</form>
<% } %>
<%
if (statusUpdate != 0){
    %>
    <div id="style_add">Data telah diperbarui ! Silahkan klik tombol Search untuk me-refresh hasil pencarian.</div>
    <strong><%= queryUp %></strong>
    <%
}
%>