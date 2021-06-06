<%@page import="com.dimata.harisma.entity.payroll.PstCurrencyType"%>
<%@page import="com.dimata.harisma.entity.payroll.CurrencyType"%>
<%@page import="com.dimata.harisma.entity.payroll.PstPayPeriod"%>
<%@page import="com.dimata.harisma.entity.payroll.PayPeriod"%>
<%@page import="com.dimata.harisma.form.payroll.FrmPayPeriod"%>
<%@page import="com.dimata.harisma.form.payroll.CtrlPayPeriod"%>
<%@ page language="java" %>
<!-- package java -->
<%@ page import = "java.util.*" %>
<!-- package dimata -->
<%@ page import = "com.dimata.util.*" %>
<!-- package qdep -->
<%@ page import = "com.dimata.gui.jsp.*" %>
<%@ page import = "com.dimata.qdep.form.*" %>
<!--package harisma -->
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ include file = "../../main/javainit.jsp" %>
<% int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_SETUP, AppObjInfo.OBJ_PAYROLL_SETUP_PERIOD);%>
<%@ include file = "../../main/checkuser.jsp" %>
<!-- JSP Block -->
<%!

public String drawList(int iCommand, Vector objectClass, FrmPayPeriod frmObject, PayPeriod objEntity, long payPeriodId,CurrencyType currencyType)
{
	
    ControlList ctrlist = new ControlList();
	ctrlist.setAreaWidth("100%");
	ctrlist.setListStyle("listgen");
	ctrlist.setTitleStyle("listgentitle");
	ctrlist.setCellStyle("listgensell");
	ctrlist.setHeaderStyle("listgentitle");
	ctrlist.addHeader("Periode","15%");
	ctrlist.addHeader("Work Days","10%");
	ctrlist.addHeader("Pay Slip Date","15%");
	ctrlist.addHeader("Start/End Period","10%");
	ctrlist.addHeader("UMR","15%");
    ctrlist.addHeader("Pay Process/Date/By ","20%");
	ctrlist.addHeader("Pay Process Close Tax/Date/By ","20%");
	ctrlist.addHeader("Tax is Paid ","5%");
	ctrlist.addHeader("First Period","5%");
	
	ctrlist.setLinkRow(0);
	ctrlist.setLinkSufix("");
	Vector lstData = ctrlist.getData();
	Vector lstLinkData = ctrlist.getLinkData();
	ctrlist.setLinkPrefix("javascript:cmdEdit('");
	ctrlist.setLinkSufix("')");
	ctrlist.reset();
	Vector rowx = new Vector();
	int index = -1;
	
	
	//untuk mengambil proses Gaji
	Vector vKeyProsesGj = new Vector();
    Vector vValProsesGj = new Vector();
    vKeyProsesGj.add(PstPayPeriod.SUDAH+"");
    vKeyProsesGj.add(PstPayPeriod.BELUM+"");
    vValProsesGj.add(PstPayPeriod.prosesGaji[PstPayPeriod.SUDAH]);
    vValProsesGj.add(PstPayPeriod.prosesGaji[PstPayPeriod.BELUM]);
	
	//untuk ambil disetor ke pajak ato tidak
	Vector vKeyPajakSetor = new Vector();
    Vector vValPajakSetor = new Vector();
    vKeyPajakSetor.add(PstPayPeriod.PAJAK_DISETOR+"");
    vValPajakSetor.add("");
	
    //untuk ambil disetor ke pajak ato tidak
    Vector vKeyFirstPeriode = new Vector();
    Vector vValFirstPeriode = new Vector();
    vKeyFirstPeriode.add(PstPayPeriod.FIRST_PERIODE_YA+"");
    vValFirstPeriode.add("");
	
    
	ControlCheckBox cb = new ControlCheckBox();
	String frmCurrency = "#,###";
                String mataUang="Rp";
                if(currencyType!=null && currencyType.getCode()!=null && currencyType.getFormatCurrency()!=null){
                    frmCurrency = currencyType.getFormatCurrency();
                    mataUang = currencyType.getCode();
                }
	//System.out.println("objectClass"+objectClass.size());
	if(objectClass!=null && objectClass.size()>0){
		for (int i = 0; i < objectClass.size(); i++) {
			PayPeriod payPeriode = (PayPeriod)objectClass.get(i);
			 if(payPeriodId == payPeriode.getOID())
				index = i;
			//FrmKpPns.userFormatStringDecimal
			rowx = new Vector();
			if((index==i) && (iCommand==Command.EDIT || iCommand==Command.ASK)){
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PERIOD] +"\" value=\""+payPeriode.getPeriod()+"\" class=\"formElemen\" size=\"20\">");
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_WORK_DAYS] +"\" value=\""+payPeriode.getWorkDays()+"\" class=\"formElemen\" size=\"20\">");
				rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_SLIP_DATE], payPeriode.getPaySlipDate(), 1,-5, "formElemen", ""));
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_START_DATE], payPeriode.getStartDate(),1,-5, "formElemen", "")+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_END_DATE], payPeriode.getEndDate(), 1,-5, "formElemen", ""));
                rowx.add(mataUang +" <input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_MIN_REG_WAGE] +"\" value=\""+FrmPayPeriod.userFormatStringDecimal(payPeriode.getMinRegWage())+"\" class=\"formElemen\" size=\"4\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROCESS], null, ""+payPeriode.getPayProsess(),vKeyProsesGj,vValProsesGj)+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_DATE] , payPeriode.getPayProcDate(), 1,-5, "formElemen", "")+"/<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_BY] +"\" value=\""+payPeriode.getPayProcBy()+"\" class=\"formElemen\" size=\"4\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROCESS_CLOSE], null, ""+payPeriode.getPayProcessClose(),vKeyProsesGj,vValProsesGj)+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_DATE_CLOSE] , payPeriode.getPayProcDateClose(), 1,-5, "formElemen", "")+"/<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_BY_CLOSE] +"\" value=\""+payPeriode.getPayProcByClose()+"\" class=\"formElemen\" size=\"4\">");
		Vector vCheck = new Vector();
                vCheck.add(payPeriode.getTaxIsPaid()+"");
                Vector vCheckFirstPeriod = new Vector();
                vCheckFirstPeriod.add(payPeriode.getFirstPeriodOfTheYear()+"");
                rowx.add(cb.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_TAX_IS_PAID],vKeyPajakSetor,vValPajakSetor,vCheck));
                rowx.add(cb.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_FIRST_PERIODE_OF_THE_YEAR],vKeyFirstPeriode,vValFirstPeriode,vCheckFirstPeriod));
			}else{
				
				Date startDate = payPeriode.getStartDate();
				Date endDate = payPeriode.getEndDate();
				Date payProcDate = payPeriode.getPayProcDate();
				Date payProcDateClose = payPeriode.getPayProcDateClose();
				
				String dateStart = startDate==null?"-":Formater.formatDate(payPeriode.getStartDate(), "dd-MM-yyyy");
				String endStart = endDate==null?"-":Formater.formatDate(payPeriode.getEndDate(), "dd-MM-yyyy");
				String procPayDate = payProcDate==null?"-":Formater.formatDate(payPeriode.getPayProcDate(), "dd-MM-yyyy");
				String procPayDateClose = payProcDateClose==null?"-":Formater.formatDate(payPeriode.getPayProcDateClose(), "dd-MM-yyyy");
				
				if(payPeriode.getTaxIsPaid()!=PstPayPeriod.PAJAK_DISETOR)
					rowx.add("<a href=\"javascript:cmdEdit('"+String.valueOf(payPeriode.getOID())+"')\">"+payPeriode.getPeriod()+"</a>");
				else
					rowx.add(payPeriode.getPeriod());
                rowx.add(String.valueOf(payPeriode.getWorkDays()));
                rowx.add(payPeriode.getPaySlipDate()==null?"":Formater.formatDate(payPeriode.getPaySlipDate(), "dd-MM-yyyy"));
                rowx.add(dateStart+"/"+endStart);
                rowx.add(mataUang +" "+String.valueOf(FrmPayPeriod.userFormatStringDecimal(payPeriode.getMinRegWage())));
                rowx.add(String.valueOf(PstPayPeriod.prosesGaji[payPeriode.getPayProsess()])+"/"+procPayDate+"/"+payPeriode.getPayProcBy());
                rowx.add(String.valueOf(PstPayPeriod.prosesGaji[payPeriode.getPayProcessClose()])+"/"+procPayDateClose+"/"+payPeriode.getPayProcByClose());
		rowx.add(String.valueOf(PstPayPeriod.pajakNames[payPeriode.getTaxIsPaid()]));
		rowx.add(String.valueOf(PstPayPeriod.firstPeriodOfTheYears[payPeriode.getFirstPeriodOfTheYear()]));
		
                }
			lstData.add(rowx);	
		}
			rowx = new Vector();
			
			if(iCommand==Command.ADD || (iCommand == Command.SAVE && frmObject.errorSize()>0)){
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PERIOD] +"\" value=\""+objEntity.getPeriod()+"\" class=\"formElemen\" size=\"20\">");
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_WORK_DAYS] +"\" value=\""+objEntity.getWorkDays()+"\" class=\"formElemen\" size=\"20\">");
				rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_SLIP_DATE] , new Date(), 1,-5, "formElemen", ""));
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_START_DATE], new Date(),1,-5, "formElemen", "")+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_END_DATE], new Date(), 1,-5, "formElemen", ""));
                rowx.add(mataUang + " <input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_MIN_REG_WAGE] +"\" value=\""+FrmPayPeriod.userFormatStringDecimal(objEntity.getMinRegWage())+"\" class=\"formElemen\" size=\"4\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROCESS], null, ""+objEntity.getPayProsess(),vKeyProsesGj,vValProsesGj)+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_DATE] , new Date(), 1,-5, "formElemen", "")+"/<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_BY] +"\" value=\""+objEntity.getPayProcBy()+"\" class=\"formElemen\" size=\"4\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROCESS_CLOSE], null, ""+objEntity.getPayProcessClose(),vKeyProsesGj,vValProsesGj)+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_DATE_CLOSE] , new Date(), 1,-5, "formElemen", "")+"/<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_BY_CLOSE] +"\" value=\""+objEntity.getPayProcByClose()+"\" class=\"formElemen\" size=\"4\">");
				Vector vCheck = new Vector();
                vCheck.add(objEntity.getTaxIsPaid()+"");
                
                Vector vCheckFirstPeriod = new Vector();
                vCheckFirstPeriod.add(objEntity.getFirstPeriodOfTheYear()+"");
                rowx.add(cb.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_TAX_IS_PAID],vKeyPajakSetor,vValPajakSetor,vCheck));
		rowx.add(cb.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_FIRST_PERIODE_OF_THE_YEAR],vKeyFirstPeriode,vValFirstPeriode,vCheckFirstPeriod));
				
			}
			lstData.add(rowx);
			
	}else{
		if(iCommand==Command.ADD){
                rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PERIOD] +"\" value=\""+objEntity.getPeriod()+"\" class=\"formElemen\" size=\"20\">");
				rowx.add("<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_WORK_DAYS] +"\" value=\""+objEntity.getWorkDays()+"\" class=\"formElemen\" size=\"20\">");
				rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_SLIP_DATE] , new Date(), 1,-5, "formElemen", ""));
                rowx.add(ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_START_DATE], new Date(),1,-5, "formElemen", "")+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_END_DATE], new Date(), 1,-5, "formElemen", ""));
                rowx.add( mataUang +" <input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_MIN_REG_WAGE] +"\" value=\""+FrmPayPeriod.userFormatStringDecimal(objEntity.getMinRegWage())+"\" class=\"formElemen\" size=\"4\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROCESS], null, ""+objEntity.getPayProsess(),vKeyProsesGj,vValProsesGj)+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_DATE] , new Date(), 1,-5, "formElemen", "")+"/<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_BY] +"\" value=\""+objEntity.getPayProcBy()+"\" class=\"formElemen\" size=\"4\">");
                rowx.add(ControlCombo.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROCESS_CLOSE], null, ""+objEntity.getPayProcessClose(),vKeyProsesGj,vValProsesGj)+"/"+ControlDate.drawDateWithStyle(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_DATE_CLOSE] , new Date(), 1,-5, "formElemen", "")+"/<input type=\"text\" name=\""+frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_PAY_PROC_BY_CLOSE] +"\" value=\""+objEntity.getPayProcByClose()+"\" class=\"formElemen\" size=\"4\">");
				Vector vCheck = new Vector();
                vCheck.add(objEntity.getTaxIsPaid()+"");
                rowx.add(cb.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_TAX_IS_PAID],vKeyPajakSetor,vValPajakSetor,vCheck));
		
                Vector vCheckFirstPeriod = new Vector();
                vCheckFirstPeriod.add(objEntity.getFirstPeriodOfTheYear()+"");
		rowx.add(cb.draw(frmObject.fieldNames[FrmPayPeriod.FRM_FIELD_FIRST_PERIODE_OF_THE_YEAR],vKeyFirstPeriode,vValFirstPeriode,vCheckFirstPeriod));
				
				
			lstData.add(rowx);
			
		}
	}
	return ctrlist.draw(index);
}

%>
<%
int iCommand = FRMQueryString.requestCommand(request);
int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidPeriod = FRMQueryString.requestLong(request, "hidden_payPeriod_id");

String frmCurrency = "#,###";
String mataUang="Rp.";
        CurrencyType currencyType = new CurrencyType();
        try{
            Vector currType = PstCurrencyType.list(0, 1, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_INF_PROCESS]+"="+PstCurrencyType.YES_USED, "");
            if(currType!=null && currType.size()>0){
                currencyType =(CurrencyType)currType.get(0);
                frmCurrency = currencyType.getFormatCurrency();
                mataUang = currencyType.getCode(); 
            }
        }catch(Exception exc){
        
        }
/*variable declaration*/
int recordToGet = 10;
String msgString = "";
int iErrCode = FRMMessage.NONE;
String whereClause = "";
String orderClause = PstPayPeriod.fieldNames[PstPayPeriod.FLD_START_DATE];

CtrlPayPeriod ctrlPayPeriod = new CtrlPayPeriod(request);
ControlLine ctrLine = new ControlLine();
String strJenisBahasa = "Period";
String strCommand = "Period";
String strSave =  ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_SAVE,true);
String strDelete = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_ASK,true);
String strCancel = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_CANCEL,false);
String strConfirmDel = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_DELETE,true);
String strSaveInfo = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_SAVE_SUCCESS,false);
String strConfirmDelInfo = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_DELETE_SUCCESS,false);
String strDelQuest = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_DELETE_QUESTION,false);
String strBack = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_BACK,true);
String strAdd = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_ADD,true);
String strPrevCaption = ctrLine.getCommand(SESS_LANGUAGE,strCommand,ctrLine.CMD_PREV,false);

Vector listPayPeriod = new Vector(1,1);

/*switch statement */
iErrCode = ctrlPayPeriod.action(iCommand , oidPeriod);
/* end switch*/
FrmPayPeriod frmPayPeriod = ctrlPayPeriod.getForm();

/*count list All Period*/
int vectSize = PstPayPeriod.getCount(whereClause);

PayPeriod payPeriod = ctrlPayPeriod.getPeriod();
msgString =  ctrlPayPeriod.getMessage();

/*switch list Period*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	start = PstPayPeriod.findLimitStart(payPeriod.getOID(),recordToGet, whereClause,orderClause);
	oidPeriod = payPeriod.getOID();
}

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlPayPeriod.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

/* get record to display */
listPayPeriod = PstPayPeriod.list(start,recordToGet, whereClause , orderClause); 

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listPayPeriod.size() < 1 && start > 0)
{
	 if (vectSize - recordToGet > recordToGet)
			start = start - recordToGet;   //go to Command.PREV
	 else{
		 start = 0 ;
		 iCommand = Command.FIRST;
		 prevCommand = Command.FIRST; //go to Command.FIRST
	 }
	 listPayPeriod = PstPayPeriod.list(start,recordToGet, whereClause , orderClause);
}
%>

<!-- End of JSP Block -->
<html>
<!-- #BeginTemplate "/Templates/main.dwt" --> 
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - </title>
<!-- #EndEditable --> 
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<!-- #BeginEditable "styles" --> 
<link rel="stylesheet" href="../../styles/main.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "stylestab" --> 
<link rel="stylesheet" href="../../styles/tab.css" type="text/css">
<!-- #EndEditable --> <!-- #BeginEditable "headerscript" --> 
<script language="JavaScript">


function cmdAdd(){
	document.frmpayPeriod.hidden_payPeriod_id.value="0";
	document.frmpayPeriod.command.value="<%=Command.ADD%>";
	document.frmpayPeriod.prev_command.value="<%=prevCommand%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
}

function cmdAsk(oidPeriod){
	document.frmpayPeriod.hidden_payPeriod_id.value=oidPeriod;
	document.frmpayPeriod.command.value="<%=Command.ASK%>";
	document.frmpayPeriod.prev_command.value="<%=prevCommand%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
}


function cmdConfirmDelete(oid){
		var x = confirm(" Are You Sure to Delete?");
		if(x){
			document.frmpayPeriod.command.value="<%=Command.DELETE%>";
			document.frmpayPeriod.action="period.jsp";
			document.frmpayPeriod.submit();
		}
}

function cmdSave(){
	document.frmpayPeriod.command.value="<%=Command.SAVE%>";
	document.frmpayPeriod.prev_command.value="<%=prevCommand%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
	}

function cmdEdit(oidPeriod){
	document.frmpayPeriod.hidden_payPeriod_id.value=oidPeriod;
	document.frmpayPeriod.command.value="<%=Command.EDIT%>";
	document.frmpayPeriod.prev_command.value="<%=prevCommand%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
	}

function cmdCancel(oidPeriod){
	document.frmpayPeriod.hidden_payPeriod_id.value=oidPeriod;
	document.frmpayPeriod.command.value="<%=Command.EDIT%>";
	document.frmpayPeriod.prev_command.value="<%=prevCommand%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
}

function cmdBack(){
	document.frmpayPeriod.command.value="<%=Command.BACK%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
	}

function cmdListFirst(){
	document.frmpayPeriod.command.value="<%=Command.FIRST%>";
	document.frmpayPeriod.prev_command.value="<%=Command.FIRST%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
}

function cmdListPrev(){
	document.frmpayPeriod.command.value="<%=Command.PREV%>";
	document.frmpayPeriod.prev_command.value="<%=Command.PREV%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
	}

function cmdListNext(){
	document.frmpayPeriod.command.value="<%=Command.NEXT%>";
	document.frmpayPeriod.prev_command.value="<%=Command.NEXT%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
}

function cmdListLast(){
	document.frmpayPeriod.command.value="<%=Command.LAST%>";
	document.frmpayPeriod.prev_command.value="<%=Command.LAST%>";
	document.frmpayPeriod.action="period.jsp";
	document.frmpayPeriod.submit();
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
</script>
<!-- #EndEditable --> 
</head>
<body <%=noBack%> bgcolor="#FFFFFF" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%" bgcolor="#F9FCFF" >
     <%if(headerStyle && !verTemplate.equalsIgnoreCase("0")){%> 
           <%@include file="../../styletemplate/template_header.jsp" %>
            <%}else{%>
  <tr> 
    <td ID="TOPTITLE" background="<%=approot%>/images/HRIS_HeaderBg3.jpg" width="100%" height="54"> 
      <!-- #BeginEditable "header" --> 
      <%@ include file = "../../main/header.jsp" %>
      <!-- #EndEditable --> </td>
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
  <tr> 
    <td width="88%" valign="top" align="left"> 
      <table width="100%" border="0" cellspacing="3" cellpadding="2">
        <tr> 
          <td width="100%"> 
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td height="20"> <font color="#FF6600" face="Arial"><strong> <!-- #BeginEditable "contenttitle" -->Pay Slip 
                  Period <!-- #EndEditable --> </strong></font> </td>
              </tr>
              <tr> 
                <td> 
                  <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr> 
                      <td  style="background-color:<%=bgColorContent%>"> 
                        <table width="100%" border="0"   >
                          <tr> 
                            <td valign="top"> 
                              <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                                <tr> 
                                  <td valign="top"> <!-- #BeginEditable "content" --> 
                                     <form name="frmpayPeriod" method="post" action="">
									 <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                      <input type="hidden" name="hidden_payPeriod_id" value="<%=oidPeriod%>">
									  
                                      <table width="100%" border="0" cellspacing="1" cellpadding="1" >
                                        <tr> 
                                          <td align="center">
										  <div align="left">
										  </div>
										  </td>
                                        </tr>
										<%
										try{
										%>
										<tr> 
                                          <td align="left">
										   <%=drawList(iCommand, listPayPeriod, frmPayPeriod, payPeriod, oidPeriod,currencyType)%>  
										  </td>
                                        </tr>
										 <% 
										  }catch(Exception exc){ 
										  System.out.println("Err::::::"+exc.toString());
										  }%>
										  <tr> 
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
													  { 
														if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE) && (oidPeriod == 0))
															cmd = PstPayPeriod.findLimitCommand(start,recordToGet,vectSize);
														else
															cmd = prevCommand;
													  } 
												   } 
												%>
												<% ctrLine.setLocationImg(approot+"/images");
												ctrLine.initDefault();
												 %>
                                                  <%=ctrLine.drawImageListLimit(cmd,vectSize,start,recordToGet)%> 
                                                  </span> </td>
                                        </tr>
										<tr> 
										  <td> 
											<table>
											 <tr> 
											  <%if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmPayPeriod.errorSize()<1)){%>
											  <td colspan="2" valign="middle"> <a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add new data"></a> 
												</td>
												<td width="261" colspan="2" valign="middle"> 
                                                  <a href="javascript:cmdAdd()" class="command">Add 
                                                  New Period</a></td>
											  <%}%>
											</tr>
										  </table>
										  </td>
										</tr>
										<tr> 
										  <td height="28"> 
                                            <table>
											<tr> 
											<%
											   if((iCommand == Command.ADD || iCommand == Command.EDIT)){
											%>
											  <td width="24" valign="middle"> <a href="javascript:cmdSave()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1002','','<%=approot%>/images/BtnSave.jpg',1)"><img name="Image1002" border="0" src="<%=approot%>/images/BtnSave.jpg" width="24" height="24" alt="Save"></a>
												</td>
												<td width="100" valign="middle"> 
                                                  <a href="javascript:cmdSave()" class="command" >Save 
                                                  Period</a></td>
												  <td width="145">
												  <img name="Image261" border="0" src="<%=approot%>/images/BtnDel.jpg" width="24" height="24" alt="Delete">
										 		  <a href="javascript:cmdConfirmDelete()" class="command">Delete Period</a></td>
												<td width="24"  valign="middle"><a href="javascript:cmdBack()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image1002','','<%=approot%>/images/BtnBack.jpg',1)"><img name="Image1002" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back"></a>
												</td>
												<td width="172" colspan="2" valign="middle"><a href="javascript:cmdBack()" class="command" >Back 
                                                  to List Period</a></td>
											</tr>
											<%}%>
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
<!-- #BeginEditable "script" --> {script} 
<!-- #EndEditable --> <!-- #EndTemplate -->
</html>
