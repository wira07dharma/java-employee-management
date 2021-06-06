
<%@page import="com.dimata.harisma.entity.attendance.I_Atendance"%>
<% 
/* 
 * Page Name  		:  position.jsp
 * Created on 		:  [date] [time] AM/PM 
 * 
 * @author  		: karya 
 * @version  		: 01 local
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
<%@ page import = "com.dimata.harisma.entity.search.*" %>
<%@ page import = "com.dimata.harisma.form.search.*" %>
<%@ page import = "com.dimata.harisma.entity.masterdata.*" %>
<%@ page import = "com.dimata.harisma.form.masterdata.*" %>
<%@ page import = "com.dimata.harisma.entity.admin.*" %>
<%@ include file = "../main/javainit.jsp" %>
<% int  appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_MASTERDATA, AppObjInfo.G2_MD_COMPANY, AppObjInfo.OBJ_POSITION); %>
<%@ include file = "../main/checkuser.jsp" %>
<%
/* Check privilege except VIEW, view is already checked on checkuser.jsp as basic access*/
//boolean privAdd=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_ADD));
//boolean privUpdate=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_UPDATE));
//boolean privDelete=userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_DELETE));
%>
<!-- Jsp Block -->
<%!    
    public String drawList(Vector objectClass, long positionId, I_Atendance attdConfig) {
        ControlList ctrlist = new ControlList();
        ctrlist.setAreaWidth("100%");
        ctrlist.setListStyle("tblStyle");
        ctrlist.setTitleStyle("title_tbl");
        ctrlist.setCellStyle("listgensell");
        ctrlist.setHeaderStyle("title_tbl");
            
        ctrlist.setCellSpacing("0");
        ctrlist.addHeader("Position", "");
            
        if (attdConfig != null && attdConfig.getConfigurationShowPositionCode()) {
            ctrlist.addHeader("Position Code", "");
        }
        ctrlist.addHeader("Type", "");
        ctrlist.addHeader("Show In Pay Input", "");
        ctrlist.addHeader("Job Description", "");
            
        ctrlist.setLinkRow(0);
        ctrlist.setLinkSufix("");
        Vector lstData = ctrlist.getData();
        Vector lstLinkData = ctrlist.getLinkData();
        ctrlist.setLinkPrefix("javascript:cmdEdit('");
        ctrlist.setLinkSufix("')");
        ctrlist.reset();
        int index = -1;
            
        for (int i = 0; i < objectClass.size(); i++) {
            Position position = (Position) objectClass.get(i);
            Vector rowx = new Vector();
            if (positionId == position.getOID()) {
                index = i;
            }
                
            rowx.add(position.getPosition());
            if (attdConfig != null && attdConfig.getConfigurationShowPositionCode()) {
                rowx.add(position.getKodePosition());
            }
            rowx.add("" + PstPosition.strPositionLevelNames[position.getPositionLevel()]);
            rowx.add("" + PstPosition.strShowPayInput[position.getFlagShowPayInput()]);
            rowx.add(position.getDescription());
                
            lstData.add(rowx);
            lstLinkData.add(String.valueOf(position.getOID()));
        }
        return ctrlist.draw(index);
    }

    public String getPositionName(long posId){
        String position = "";
        Position pos = new Position();
        try {
            pos = PstPosition.fetchExc(posId);
        } catch(Exception ex){
            System.out.println("getPositionName ==> "+ex.toString());
        }
        position = pos.getPosition();
        return position;
    }

    public String getTemplateName(long tempId){
        String template = "";
        StructureTemplate temp = new StructureTemplate();
        try {
            temp = PstStructureTemplate.fetchExc(tempId);
            template = temp.getTemplateName();
        } catch(Exception ex){
            System.out.println("getTemplateName =>"+ex.toString());
        }
        return template;
    }
%>
<%
int iCommand = FRMQueryString.requestCommand(request);
/* update variable by Hendra Putu | 2015-08-02 */
int customCommand = FRMQueryString.requestInt(request, "custom_command");
String searchPosition = FRMQueryString.requestString(request, "search_position");
int askCommand = FRMQueryString.requestInt(request, "ask_command");
int deleteCommand = FRMQueryString.requestInt(request, "delete_command");
long deleteCompetencyId = FRMQueryString.requestLong(request, "delete_competency_id");
long deleteTrainingId = FRMQueryString.requestLong(request, "delete_training_id");
long deleteEduId = FRMQueryString.requestLong(request, "delete_edu_id");

int kpiCommand = FRMQueryString.requestInt(request, "kpi_command");
long selectCompany = FRMQueryString.requestLong(request, "select_company");
long selectDivisi = FRMQueryString.requestLong(request, "select_divisi");
long selectDepart = FRMQueryString.requestLong(request, "select_depart");
long selectSection = FRMQueryString.requestLong(request, "select_section");
long positionMapId = FRMQueryString.requestLong(request, "position_map_id");
long topPos = FRMQueryString.requestLong(request, "top_pos");
int topType = FRMQueryString.requestInt(request, "top_type");
long downPos = FRMQueryString.requestLong(request, "down_pos");
int downType = FRMQueryString.requestInt(request, "down_type");

int start = FRMQueryString.requestInt(request, "start");
int prevCommand = FRMQueryString.requestInt(request, "prev_command");
long oidPosition = FRMQueryString.requestLong(request, "hidden_position_id");
long oidCompetency = FRMQueryString.requestLong(request, "competency_id");
long oidTraining = FRMQueryString.requestLong(request, "training_id");
long oidEducation = FRMQueryString.requestLong(request, "education_id");
long oidKPI = FRMQueryString.requestLong(request, "kpi_id");
long oidPosKPI = FRMQueryString.requestLong(request, "pos_kpi_id");

String orderBy = "";
/* update code by Hendra Putu | 2015-08-02 */
if (customCommand != 0){
    switch(customCommand){
        case 1:
            PositionCompany psc = new PositionCompany();
            psc.setCompanyId(selectCompany);
            psc.setPositionId(oidPosition);
            PstPositionCompany.insertExc(psc);
            customCommand = 0;
            break;
        case 2:
            PositionDivision div = new PositionDivision();
            div.setDivisionId(selectDivisi);
            div.setPositionId(oidPosition);
            PstPositionDivision.insertExc(div);
            customCommand = 0;
            break;
        case 3:
            PositionDepartment depart = new PositionDepartment();
            depart.setDepartmentId(selectDepart);
            depart.setPositionId(oidPosition);
            PstPositionDepartment.insertExc(depart);
            customCommand = 0;
            break;
        case 4:
            PositionSection section = new PositionSection();
            section.setSectionId(selectSection);
            section.setPositionId(oidPosition);
            PstPositionSection.insertExc(section);
            customCommand = 0;
            break;
        case 5:
            TopPosition topPosition = new TopPosition();
            topPosition.setPositionId(oidPosition);
            topPosition.setPositionToplink(topPos);
            topPosition.setTypeOfLink(topType);
            PstTopPosition.insertExc(topPosition);
            customCommand = 0;
            break;
        case 6:
            DownPosition downPosition = new DownPosition();
            downPosition.setPositionId(oidPosition);
            downPosition.setPositionDownlink(downPos);
            downPosition.setTypeOfLink(downType);
            PstDownPosition.insertExc(downPosition);
            customCommand = 0;
            break;                          
    }
}

/* Update code by Hendra Putu | 2015-09-03 */
if (deleteCommand > 0 && deleteCompetencyId > 0){
    long oidPosCompetency = PstPositionCompetencyRequired.deleteExc(deleteCompetencyId);
    deleteCommand = 0;
    deleteCompetencyId = 0;
}

if (deleteCommand > 0 && deleteTrainingId > 0){
    PstPositionTrainingRequired.deleteExc(deleteTrainingId);
    deleteCommand = 0;
    deleteTrainingId = 0;
}

if (deleteCommand > 0 && deleteEduId > 0){
    PstPositionEducationRequired.deleteExc(deleteEduId);
    deleteCommand = 0;
    deleteEduId = 0;
}


long oidCom = 0;
long oidTrain = 0;
long oidEdu = 0;
long oidKp = 0;
    I_Atendance attdConfig = null;
    try {
        attdConfig = (I_Atendance) (Class.forName(PstSystemProperty.getValueByName("ATTENDANCE_CONFIG")).newInstance());
    } catch (Exception e) {
        System.out.println("Exception : " + e.getMessage());
        System.out.println("Please contact your system administration to setup system property: ATTENDANCE_CONFIG ");
    }
/*variable declaration*/
int recordToGet = 30;
String msgString = "";
int iErrCode = FRMMessage.NONE;
int iErrCodePosKpi = FRMMessage.NONE;

CtrlPosition ctrlPosition = new CtrlPosition(request);
CtrlPositionKPI ctrlPositionKpi = new CtrlPositionKPI(request);
ControlLine ctrLine = new ControlLine();  
Vector listPosition = new Vector(1,1);

/*switch statement */
iErrCode = ctrlPosition.action(iCommand , oidPosition, attdConfig);
if(oidPosKPI != 0){
    iErrCodePosKpi = ctrlPositionKpi.action(6 , oidPosKPI);
}


/* end switch*/
FrmPosition frmPosition = ctrlPosition.getForm();
/*
 * Description : add competency, training & education
 * Date : 2015-02-05
 * Author : Hendra Putu
*/
if(iCommand == Command.EDIT){
    if (oidCompetency > 0){
        PositionCompetencyRequired posComReq = new PositionCompetencyRequired();
        posComReq.setCompetencyId(oidCompetency);
        posComReq.setPositionId(oidPosition);
        Vector listComLevel = new Vector();
        String whereClause = ""+PstCompetencyLevel.fieldNames[PstCompetencyLevel.FLD_COMPETENCY_ID]+"="+oidCompetency;
        listComLevel = PstCompetencyLevel.list(0, 1, whereClause, "");
        if (listComLevel != null && listComLevel.size() > 0){
            CompetencyLevel comLevel = (CompetencyLevel)listComLevel.get(0);
            posComReq.setCompetencyLevelId(comLevel.getOID());
        }
        oidCom = PstPositionCompetencyRequired.insertExc(posComReq);
    }
    oidCompetency = 0;
    if (oidTraining > 0){
        PositionTrainingRequired posTrain = new PositionTrainingRequired();
        posTrain.setTrainingId(oidTraining);
        posTrain.setPositionId(oidPosition);
        oidTrain = PstPositionTrainingRequired.insertExc(posTrain);
    }
    oidTraining = 0;
    if (oidEducation > 0){
        PositionEducationRequired posEdu = new PositionEducationRequired();
        posEdu.setEducationId(oidEducation);
        posEdu.setPositionId(oidPosition);
        oidEdu = PstPositionEducationRequired.insertExc(posEdu);
    }
    oidEducation = 0;
    if (oidKPI > 0){
        PositionKPI posKPI = new PositionKPI();
        posKPI.setKpiListId(oidKPI);
        posKPI.setPositionId(oidPosition);
        oidKp = PstPositionKPI.insertExc(posKPI);
    }
    oidKPI = 0;
}

SrcPosition srcPosition = new SrcPosition();
FrmSrcPosition frmSrcPosition  = new FrmSrcPosition(request, srcPosition);
if(iCommand==Command.FIRST || iCommand==Command.PREV || iCommand==Command.NEXT || iCommand==Command.LAST || iCommand==Command.BACK || iCommand==Command.ASK
   || iCommand==Command.EDIT || iCommand==Command.ADD || iCommand==Command.DELETE || (iCommand==Command.SAVE && frmPosition.errorSize()==0) )
{
    try
    { 
           srcPosition = (SrcPosition)session.getValue(PstPosition.SESS_HR_POSITION); 
    }
    catch(Exception e)
    { 
           srcPosition = new SrcPosition();
    }
}
else
{
    frmSrcPosition.requestEntityObject(srcPosition);
    session.putValue(PstPosition.SESS_HR_POSITION, srcPosition);	
}
/* Code is modified by Hendra Putu | 2015-08-16 */
String whereClause = "";
if (srcPosition.getRadioButton() == 1) {
    //whereClause = PstPosition.fieldNames[PstPosition.FLD_VALID_START]+" BETWEEN '"+Formater.formatDate(new Date(), "yyyy-MM-dd")+"' AND '"+Formater.formatDate(new Date(), "yyyy-MM-dd")+"'";
} else if (srcPosition.getRadioButton() == 2) {
    if (srcPosition.getStartDate().length() > 0 && srcPosition.getEndDate().length() > 0){
        whereClause = PstPosition.fieldNames[PstPosition.FLD_VALID_START]+" BETWEEN '"+srcPosition.getStartDate()+"' AND '"+srcPosition.getEndDate()+"'";
    }
}
if (srcPosition.getPosName() != null && srcPosition.getPosName().length() > 0){
   whereClause += (whereClause.length() > 0) ? " AND " : "";
   whereClause += PstPosition.fieldNames[PstPosition.FLD_POSITION] + " LIKE '%" + srcPosition.getPosName() + "%'";
}
if (srcPosition.getPosLevel() >=0 ){
    whereClause += (whereClause.length() > 0) ? " AND " : "";
    whereClause += PstPosition.fieldNames[PstPosition.FLD_POSITION_LEVEL] + " = " + srcPosition.getPosLevel();
}
if (srcPosition.getLevelRankID() > 0){
    whereClause += (whereClause.length() > 0) ? " AND " : "";
    whereClause += PstPosition.fieldNames[PstPosition.FLD_LEVEL_ID] + " = " + srcPosition.getLevelRankID();
}


String orderClause = " POSITION ";

/*count list All Position*/
int vectSize = PstPosition.getCount(whereClause);

Position position = ctrlPosition.getPosition();
msgString =  ctrlPosition.getMessage();

/*switch list Position*/
if((iCommand == Command.SAVE) && (iErrCode == FRMMessage.NONE)){
	//start = PstPosition.findLimitStart(position.getOID(),recordToGet, whereClause);
	oidPosition = position.getOID();
}

if((iCommand == Command.FIRST || iCommand == Command.PREV )||
  (iCommand == Command.NEXT || iCommand == Command.LAST)){
		start = ctrlPosition.actionList(iCommand, start, vectSize, recordToGet);
 } 
/* end switch list*/

if (customCommand == 7){
    whereClause = PstPosition.fieldNames[PstPosition.FLD_POSITION]+" LIKE '%"+searchPosition+"%'";
    orderClause = PstPosition.fieldNames[PstPosition.FLD_POSITION]+" ASC";
}
/* get record to display */
listPosition = PstPosition.list(start, recordToGet, whereClause , orderClause);

/*handle condition if size of record to display = 0 and start > 0 	after delete*/
if (listPosition.size() < 1 && start > 0)
{
    if (vectSize - recordToGet > recordToGet)
                   start = start - recordToGet;   //go to Command.PREV
    else{
            start = 0 ;
            iCommand = Command.FIRST;
            prevCommand = Command.FIRST; //go to Command.FIRST
    }
    listPosition = PstPosition.list(start,recordToGet, whereClause , orderClause);
}

%>
<html><!-- #BeginTemplate "/Templates/main.dwt" -->
<head>
<!-- #BeginEditable "doctitle" --> 
<title>HARISMA - Master Data Position</title>
<script language="JavaScript">
function cmdBackToSearch(){
    document.frmposition.command.value="<%=Command.BACK%>";
    document.frmposition.action="srcposition.jsp";
    document.frmposition.submit();
}

function cmdSearchPosition(){
    document.frmposition.custom_command.value="7";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdAdd(){
    document.frmposition.hidden_position_id.value="0";
    document.frmposition.command.value="<%=Command.ADD%>";
    document.frmposition.prev_command.value="<%=prevCommand%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdJobDescDet(){
    var comm = document.frmposition.command.value;
    newWindow=window.open("jobdescdetail.jsp?comm="+comm+"&statPos="+<%=position.getPositionLevel()%>+"&hidden_position_id="+<%=oidPosition%>,"SelectEmployee", "fullscreen=yes,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
}

function cmdAddCompetency(){
    var comm = document.frmposition.command.value;
    newWindow=window.open("competency_search.jsp?comm="+comm,"SelectEmployee", "height=600,width=427,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdAddTraining(){
    var comm = document.frmposition.command.value;
    newWindow=window.open("training_search.jsp?comm="+comm,"SelectEmployee", "height=600,width=600,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdAddEducation(){
    var comm = document.frmposition.command.value;
    newWindow=window.open("education_search.jsp?comm="+comm,"SelectEmployee", "height=400,width=390,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdAddKpi(){
    var comm = document.frmposition.command.value;
    newWindow=window.open("kpi_search.jsp?comm="+comm,"SelectEmployee", "height=400,width=600,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdEditCompetency(oid){
    var comm = document.frmposition.command.value;
    newWindow=window.open("pos_competency_edit.jsp?comm="+comm+"&oid="+oid,"SelectEmployee", "height=500,width=600,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdEditTraining(oid){
    var comm = document.frmposition.command.value;
    newWindow=window.open("pos_training_edit.jsp?comm="+comm+"&oid="+oid,"SelectEmployee", "height=600,width=600,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdEditEducation(oid){
    var comm = document.frmposition.command.value;
    newWindow=window.open("pos_education_edit.jsp?comm="+comm+"&oid="+oid,"SelectEmployee", "height=400,width=590,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdEditKpi(oid){
    var comm = document.frmposition.command.value;
    newWindow=window.open("pos_kpi_edit.jsp?comm="+comm+"&oid="+oid,"SelectEmployee", "height=400,width=390,status=yes,toolbar=no,menubar=no,location=center,scrollbars=yes");
    newWindow.focus();
    //document.frm_pay_emp_level.submit();
}
function cmdRefresh(){
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}
/* Update 2015-08-01 | Putu Hendra */
function cmdAddPositionMap(oid){
    document.frmposition.hidden_position_id.value=oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}
function cmdAddPositionCompany(oidPosition){
    document.frmposition.custom_command.value="1";
    cmdAddPositionMap(oidPosition);
}
function cmdAddPositionDivisi(oidPosition){
    document.frmposition.custom_command.value="2";
    cmdAddPositionMap(oidPosition);
}
function cmdAddPositionDepart(oidPosition){
    document.frmposition.custom_command.value="3";
    cmdAddPositionMap(oidPosition);
}
function cmdAddPositionSection(oidPosition){
    document.frmposition.custom_command.value="4";
    cmdAddPositionMap(oidPosition);
}
function cmdAddTopPos(){
    document.frmposition.custom_command.value="5";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}
function cmdAddDownPos(){
    document.frmposition.custom_command.value="6";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}
function cmdDeletePosComp(oid){
    document.frmposition.custom_command.value="5";
    document.frmposition.position_map_id.value=oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdGoToForm(){
    document.frmposition.action="position_form.jsp";
    document.frmposition.submit(); 
}

function cmdAsk(oidPosition){
    document.frmposition.hidden_position_id.value=oidPosition;
    document.frmposition.command.value="<%=Command.ASK%>";
    document.frmposition.prev_command.value="<%=prevCommand%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdAskCompetency(oid){
    document.frmposition.ask_command.value="1";
    document.frmposition.delete_competency_id.value = oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdDeleteCompetency(oid){
    document.frmposition.delete_command.value="1";
    document.frmposition.ask_command.value="0";
    document.frmposition.delete_competency_id.value = oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdAskTraining(oid){
    document.frmposition.ask_command.value="2";
    document.frmposition.delete_training_id.value = oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdDeleteTraining(oid){
    document.frmposition.delete_command.value="1";
    document.frmposition.ask_command.value="0";
    document.frmposition.delete_training_id.value = oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdAskEducation(oid){
    document.frmposition.ask_command.value="3";
    document.frmposition.delete_edu_id.value = oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdDeleteEducation(oid){
    document.frmposition.delete_command.value="1";
    document.frmposition.ask_command.value="0";
    document.frmposition.delete_edu_id.value = oid;
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdBackConfirm(){
    document.frmposition.delete_command.value="0";
    document.frmposition.ask_command.value="0";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdConfirmDelete(oidPosition){
    document.frmposition.hidden_position_id.value=oidPosition;
    document.frmposition.command.value="<%=Command.DELETE%>";
    document.frmposition.prev_command.value="<%=prevCommand%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}
function cmdDeleteKpi(oidKpi){
    document.frmposition.kpi_id.value=0;
    document.frmposition.pos_kpi_id.value=oidKpi;
    document.frmposition.kpi_command.value="<%=Command.DELETE%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}
function cmdSave(){
    document.frmposition.command.value="<%=Command.SAVE%>";
    document.frmposition.prev_command.value="<%=prevCommand%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdEdit(oidPosition){
    document.frmposition.hidden_position_id.value=oidPosition;
    document.frmposition.command.value="<%=Command.EDIT%>";
    document.frmposition.prev_command.value="<%=prevCommand%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdCancel(oidPosition){
    document.frmposition.hidden_position_id.value=oidPosition;
    document.frmposition.command.value="<%=Command.EDIT%>";
    document.frmposition.prev_command.value="<%=prevCommand%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdBack(){
    document.frmposition.command.value="<%=Command.BACK%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdListFirst(){
    document.frmposition.command.value="<%=Command.FIRST%>";
    document.frmposition.prev_command.value="<%=Command.FIRST%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdListPrev(){
    document.frmposition.command.value="<%=Command.PREV%>";
    document.frmposition.prev_command.value="<%=Command.PREV%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdListNext(){
    document.frmposition.command.value="<%=Command.NEXT%>";
    document.frmposition.prev_command.value="<%=Command.NEXT%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function cmdListLast(){
    document.frmposition.command.value="<%=Command.LAST%>";
    document.frmposition.prev_command.value="<%=Command.LAST%>";
    document.frmposition.action="position.jsp";
    document.frmposition.submit();
}

function fnTrapKD(){	
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


                            
<script type="text/javascript">

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
<style type="text/css">
    #listPos {background-color: #FFF; border: 1px solid #CCC; padding: 3px 9px; cursor: pointer; margin: 1px 0px;}  
    #btn {
        padding: 3px; border: 1px solid #CCC; 
        background-color: #EEE; color: #777777; 
        font-size: 11px; cursor: pointer;
    }
    #btn:hover {border: 1px solid #999; background-color: #CCC; color: #FFF;}
    .tblStyle {border-collapse: collapse;font-size: 11px;}
    .tblStyle td {padding: 3px 5px; border: 1px solid #CCC;}
    .title_tbl {font-weight: bold;background-color: #DDD; color: #575757;}
    .title_content {
        padding: 9px 14px; 
        border-left: 1px solid #0099FF; 
        font-size: 14px; 
        background-color: #F3F3F3; 
        color:#0099FF;
        font-weight: bold;
    }
    .title_part {
        padding: 9px 14px; 
        border-left: 1px solid #0099FF; 
        font-size: 12px; 
        background-color: #F3F3F3; 
        color:#333; 
        font-weight: bold;
    }
    .part_content {
        border:1px solid #0099FF;
        border-radius: 5px;
        background-color: #F5F5F5;
    }
    .part_name {
        padding: 12px 19px; border-bottom: 1px solid #0099FF;
        border-top-left-radius: 5px;
        border-top-right-radius: 5px;
        background-color: #a9d5f2;
        color:#04619e;
        font-weight: bold;
        font-size: 12px;
    }
    #confirm {background-color: #fad9d9;border: 1px solid #da8383; color: #bf3c3c; padding: 14px 21px;border-radius: 5px;}
    #btn1 {
        background: #f27979;
        border: 1px solid #d74e4e;
        border-radius: 3px;
        font-family: Arial;
        color: #ffffff;
        font-size: 12px;
        padding: 3px 9px 3px 9px;
    }

    #btn1:hover {
        background: #d22a2a;
        border: 1px solid #c31b1b;
    }
</style>
<!-- #EndEditable -->
<link rel="stylesheet" href="<%=approot%>/javascripts/datepicker/themes/jquery.ui.all.css">
<script src="<%=approot%>/javascripts/jquery.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.core.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.widget.js"></script>
<script src="<%=approot%>/javascripts/datepicker/jquery.ui.datepicker.js"></script>
<script>
$(function() {
    $( "#datepicker1" ).datepicker({ dateFormat: "yy-mm-dd" });
    $( "#datepicker2" ).datepicker({ dateFormat: "yy-mm-dd" });
});
</script>
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
            <div class="title_content">Master Data / Position</div>
	  </td>
        </tr>
        <tr> 
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> 
                <td  style="background-color:<%=bgColorContent%>; "> 
                  <table width="100%" border="0"  >
                    <tr> 
                      <td valign="top"> 
                        <table style="border:1px solid <%=garisContent%>" width="100%" border="0" cellspacing="1" cellpadding="1" class="tablecolor">
                          <tr> 
                            <td valign="top">
		    				  <!-- #BeginEditable "content" --> 
                                    <form name="frmposition" method ="post" action="">
                                      <input type="hidden" name="custom_command" value="<%=customCommand%>">
                                      <input type="hidden" name="ask_command" value="<%=askCommand%>">
                                      <input type="hidden" name="delete_command" value="<%=deleteCommand%>">
                                      <input type="hidden" name="position_map_id" value="<%=customCommand%>">
                                      <input type="hidden" name="command" value="<%=iCommand%>">
                                      <input type="hidden" name="vectSize" value="<%=vectSize%>">
                                      <input type="hidden" name="start" value="<%=start%>">
                                      <input type="hidden" name="prev_command" value="<%=prevCommand%>">
                                      <input type="hidden" name="hidden_position_id" value="<%=oidPosition%>">
                                      <input type="hidden" name="competency_id" value="<%=oidCompetency%>" />
                                      <input type="hidden" name="delete_competency_id" value="<%=deleteCompetencyId%>" />
                                      <input type="hidden" name="delete_training_id" value="<%=deleteTrainingId%>" />
                                      <input type="hidden" name="delete_edu_id" value="<%=deleteEduId%>" />
                                      <input type="hidden" name="training_id" value="<%=oidTraining%>" />
                                      <input type="hidden" name="education_id" value="<%=oidEducation%>" />
                                      <input type="hidden" name="kpi_id" value="<%=oidKPI%>" />
                                      <input type="hidden" name="pos_kpi_id" value="<%=oidPosKPI%>" />
                                      <input type="hidden" name="kpi_command" value="<%=kpiCommand%>">
                                      
                                      <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr align="left" valign="top"> 
                                          <td colspan="3"> 
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                              <tr align="left" valign="top"> 
                                                <td height="14" valign="middle" colspan="3" class="listtitle">
                                                    <div class="title_part">
                                                        Position List&nbsp;
                                                        <input type="text" name="search_position" value="<%=searchPosition%>" placeholder="cari posisi..." size="50" />
                                                        &nbsp;<button id="btn" onclick="cmdSearchPosition()">cari</button>
                                                    </div>
                                                </td>
                                              </tr>
                                              <%
											  if (listPosition.size()>0)
											  {
											  %>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3"> 
                                                  <%= drawList(listPosition,oidPosition,attdConfig)%> 
                                                </td>
                                              </tr>
                                              <%  
											  }
											  else
											  { 
											  %>
                                              <tr align="left" valign="top"> 
                                                <td height="22" valign="middle" colspan="3" class="errfont">There 
                                                  is no position data found ...</td>
                                              </tr>											  
											  <%
											  }
											  %>
                                              <tr align="left" valign="top"> 
                                                <td height="8" align="left" colspan="3" class="command" valign="top"> 
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
											   if((iCommand != Command.ADD && iCommand != Command.ASK && iCommand != Command.EDIT)&& (frmPosition.errorSize()<1)){
											%>
                                              <tr align="left" valign="top"> 
											  	<td valign="top"> 
                                                  <table cellpadding="0" cellspacing="0" border="0" width="50%">
                                                    <tr> 
													<%if(privAdd){%>
                                                      <td width="5%"><a href="javascript:cmdAdd()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image261','','<%=approot%>/images/BtnNewOn.jpg',1)"><img name="Image261" border="0" src="<%=approot%>/images/BtnNew.jpg" width="24" height="24" alt="Add New Position"></a></td>
                                                      <td width="2%"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td width="37%" valign="middle"> 
                                                        <a href="javascript:cmdAdd()" class="command">Add 
                                                        New Position</a> </td>
														<%}%>
                                                      <td width="5%"><a href="javascript:cmdBackToSearch()" onMouseOut="MM_swapImgRestore()" onMouseOver="MM_swapImage('Image2611','','<%=approot%>/images/BtnBackOn.jpg',1)"><img name="Image2611" border="0" src="<%=approot%>/images/BtnBack.jpg" width="24" height="24" alt="Back To Search Position"></a></td>
                                                      <td width="2%"><img src="<%=approot%>/images/spacer.gif" width="1" height="1"></td>
                                                      <td width="49%" height="22" valign="middle"> 
                                                        <a href="javascript:cmdBackToSearch()" class="command">Back 
                                                        To Search Position</a> 
                                                      </td>
                                                      
                                                    </tr>
                                                  </table>
												</td>
                                              </tr>
											  <%
											  }%>
                                            </table>
                                          </td>
                                        </tr>
                                        <tr>
                                            <td colspan="3">&nbsp;</td>
                                        </tr>
                                        <tr align="left" valign="top"> 
                                          <td valign="top"> 
                                            <%if((iCommand ==Command.ADD)||(iCommand==Command.SAVE)&&( iErrCode>0 ||frmPosition.errorSize()>0)||(iCommand==Command.EDIT)||(iCommand==Command.ASK)){%>
                                            <table width="100%" border="0" cellspacing="2" cellpadding="2">
                                              <tr> 
                                                  <td colspan="2"><div class="title_part"><%=oidPosition == 0?"Add":"Edit"%> Position</div></td>
                                              </tr>
                                              <tr> 
                                                <td valign="top">
                                                  <table border="0" cellspacing="2" cellpadding="2" width="100%">
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">&nbsp;</td>
                                                      <td width="1%" class="comment">&nbsp;</td>
                                                      <td width="89%" class="comment">*)entry 
                                                        required </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Position</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                          <input type="text" size="70" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_POSITION] %>"  value="<%= position.getPosition() %>" class="elemenForm" size="30">
                                                        *<%=frmPosition.getErrorMsg(FrmPosition.FRM_FIELD_POSITION)%> </td>
                                                    </tr>
                                                    
                                                    <%if(attdConfig!=null && attdConfig.getConfigurationShowPositionCode()){%>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Position Kode</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                        <input type="text" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_POSITION_KODE] %>"  value="<%= position.getKodePosition() %>" class="elemenForm" size="30">
                                                        *<%=frmPosition.getErrorMsg(FrmPosition.FRM_FIELD_POSITION_KODE)%> </td>
                                                    </tr>
                                                    <%}else{%>
                                                        <input type="hidden" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_POSITION_KODE] %>"  value="<%= "-" %>" class="elemenForm" size="30">
                                                    <%}%>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Type</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                        <%
                                                        Vector levelKey = new Vector(1,1);
                                                        Vector levelValue = new Vector(1,1);
                                                        for(int idx=0; idx < PstPosition.strPositionLevelNames.length;idx++){																							
                                                             levelKey.add(PstPosition.strPositionLevelNames[idx]);
                                                            levelValue.add(PstPosition.strPositionLevelValue[idx]);														
                                                        }
                                                        /*
                                                        levelKey.add(PstPosition.strPositionLevelNames[PstPosition.LEVEL_GENERAL]);
                                                        levelValue.add(""+PstPosition.LEVEL_GENERAL);														

                                                        levelKey.add(PstPosition.strPositionLevelNames[PstPosition.LEVEL_SECRETARY]);
                                                        levelValue.add(""+PstPosition.LEVEL_SECRETARY);														

                                                        levelKey.add(PstPosition.strPositionLevelNames[PstPosition.LEVEL_SUPERVISOR]);
                                                        levelValue.add(""+PstPosition.LEVEL_SUPERVISOR);   														

                                                        levelKey.add(PstPosition.strPositionLevelNames[PstPosition.LEVEL_MANAGER]);
                                                        levelValue.add(""+PstPosition.LEVEL_MANAGER);														

                                                        levelKey.add(PstPosition.strPositionLevelNames[PstPosition.LEVEL_GENERAL_MANAGER]);
                                                        levelValue.add(""+PstPosition.LEVEL_GENERAL_MANAGER);														
                                                        */
                                                        out.println(ControlCombo.draw(frmPosition.fieldNames[frmPosition.FRM_FIELD_POSITION_LEVEL], "formElemen", null, ""+position.getPositionLevel(), levelValue, levelKey));
                                                    %>
                                                         &nbsp; type for payroll : <%
                                                       Vector levelPayrolKey = new Vector(1,1);
                                                        Vector levelPayrolValue = new Vector(1,1);
                                                         levelPayrolValue.add("-1");
                                                         levelPayrolKey.add(" - None - ");
                                                        for(int idx=0; idx < PstPosition.strPositionLevelNames.length;idx++){																							
                                                             levelPayrolKey.add(PstPosition.strPositionLevelNames[idx]);
                                                            levelPayrolValue.add(PstPosition.strPositionLevelValue[idx]);														
                                                        }
                                                        out.println(ControlCombo.draw(frmPosition.fieldNames[frmPosition.FRM_FIELD_POSITION_LEVEL_PAYROL], "formElemen", null, ""+position.getPositionLevelPayrol(), levelPayrolValue, levelPayrolKey));
                                                    %>
                                                     *<%=frmPosition.getErrorMsg(FrmPosition.FRM_FIELD_POSITION_LEVEL_PAYROL)%> 
                                                      </td> 
                                                      
                                                    </tr>
                                                    <!--Gede_8Maret2012{ -->
                                                    <tr align="left" valign="top">
                                                      <td valign="top" width="10%">Head Title</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%">
                                                        <%  try{
                                                            Vector headKey = new Vector(1,1);
                                                            Vector headValue = new Vector(1,1);
                                                            for(int idx=0; idx < PstPosition.strHeadTitle.length;idx++){
                                                                 headKey.add(PstPosition.strHeadTitle[idx]);
                                                                headValue.add(""+PstPosition.strHeadTitleInt[idx]);
                                                            }
                                                            out.println(ControlCombo.draw(frmPosition.fieldNames[frmPosition.FRM_FIELD_HEAD_TITLE], "formElemen", null, ""+position.getHeadTitle(), headValue, headKey));
                                                            }
                                                            catch (Exception e){
                                                            System.out.println("Error on head title : "+e.toString());
                                                            }
                                                        %>
                                                      </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="middle">Valid Status</td>
                                                        <td valign="middle">:</td>
                                                        <td valign="middle">
                                                            <select name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_VALID_STATUS]%>">
                                                                <%
                                                                if (position.getValidStatus()==PstPosition.VALID_ACTIVE){
                                                                    %>
                                                                    <option value="<%=PstPosition.VALID_ACTIVE%>" selected="selected">Active</option>
                                                                    <option value="<%=PstPosition.VALID_HISTORY%>">History</option>
                                                                    <%
                                                                } else {
                                                                    %>
                                                                    <option value="<%=PstPosition.VALID_ACTIVE%>">Active</option>
                                                                    <option value="<%=PstPosition.VALID_HISTORY%>" selected="selected">History</option>
                                                                    <%
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">Masa berlaku</td>
                                                        <td valign="top">:</td>
                                                        <td valign="top">
                                                            <input type="text" name="<%=frmPosition.fieldNames[frmPosition.FRM_FIELD_VALID_START]%>" id="datepicker1" value="<%=position.getValidStart() %>" />&nbsp;to
                                                            &nbsp;<input type="text" name="<%=frmPosition.fieldNames[frmPosition.FRM_FIELD_VALID_END]%>" id="datepicker2" value="<%=position.getValidEnd() %>" />
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="middle">Level</td>
                                                        <td valign="middle">:</td>
                                                        <td valign="middle">
                                                            <select name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_LEVEL_ID]%>">
                                                                <option value="0">-SELECT-</option>
                                                                <%
                                                                String orderLevel = PstLevel.fieldNames[PstLevel.FLD_LEVEL_RANK]+" ASC";
                                                                Vector listLevel = PstLevel.list(0, 0, "", orderLevel);
                                                                if (listLevel != null && listLevel.size()>0){
                                                                    for(int l=0; l<listLevel.size(); l++){
                                                                        Level level = (Level)listLevel.get(l);
                                                                        if (level.getOID()==position.getLevelId()){
                                                                            %>
                                                                            <option value="<%=level.getOID()%>" selected="selected"><%=level.getLevel()%></option>
                                                                            <%
                                                                        } else {
                                                                            %>
                                                                            <option value="<%=level.getOID()%>"><%=level.getLevel()%></option>
                                                                            <%
                                                                        }
                                                                        
                                                                    }
                                                                }
                                                                %>
                                                            </select>
                                                        </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Show In Pay Input</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="checkbox" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_FLAG_POSITION_SHOW_PAY_INPUT]%>" <%=(position.getFlagShowPayInput()==PstPosition.YES_SHOW_PAY_INPUT ? "checked" : "" ) %> value="1"> 
                                                      please check to show in pay input
                                                      </td>
                                                    </tr>  
                                                    <!--} -->
                                                    <tr align="left" valign="top">
                                                      <td valign="top" width="10%">Department</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%">
                                                      <input type="checkbox" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_ALL_DEPARTMENT]%>" <%=(position.getAllDepartment()==PstPosition.ALL_DEPARTMENT_TRUE ? "checked" : "" ) %> value="1" >
                                                      All Department
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Option</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="checkbox" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DISABLED_APP_UNDER_SUPERVISOR]%>" <%=(position.getDisabledAppUnderSupervisor()==PstPosition.DISABLED_APP_UNDER_SUPERVISOR_TRUE ? "checked" : "" ) %> value="1">
                                                      To DISABLE Leave Approval Employee Under Supervisor please check
                                                      </td>
                                                    </tr>   
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Option</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="checkbox" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DISABLED_APP_DEPT_SCOPE]%>" <%=(position.getDisabledAppDeptScope()==PstPosition.DISABLED_APP_DEPT_SCOPE_TRUE ? "checked" : "" ) %> value="1" >                                                        
                                                      To DISABLE Leave Approval Department Scope please check
                                                      </td>
                                                    </tr>        
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Option</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="checkbox" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DISABLED_APP_DIV_SCOPE]%>" <%=(position.getDisabedAppDivisionScope()==PstPosition.DISABLED_APP_DIV_SCOPE_TRUE ? "checked" : "" ) %> value="1" >                                                        
                                                      To DISABLE Leave Approval Division Scope please check
                                                      </td>
                                                    </tr> 
                                                    
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Time</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="text" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DEDLINE_SCH_BEFORE] %>"  value="<%= position.getDeadlineScheduleBefore() %>" class="elemenForm" size="10">
                                                      <i style="font-size: 11px">(Hour) Limit Time Update Schedule before current time ( unlimited time = 8640 )</i>
                                                      </td>
                                                    </tr>  
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Time</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="text" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DEDLINE_SCH_AFTER] %>"  value="<%= position.getDeadlineScheduleAfter() %>" class="elemenForm" size="10">                                                                                                           
                                                      <i style="font-size: 11px">(Hour) Limit Time Update Schedule after current time ( unlimited time = 8640 )</i>
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Time</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="text" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DEDLINE_SCH_LEAVE_BEFORE] %>"  value="<%= position.getDeadlineScheduleLeaveBefore() %>" class="elemenForm" size="10">                                                                                                           
                                                      <i style="font-size: 11px">(Hour) Limit Time Update Schedule Leave before current time ( unlimited time = 8640 )</i>
                                                      </td>
                                                    </tr>
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%">Time</td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                      <input type="text" name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DEDLINE_SCH_LEAVE_AFTER] %>"  value="<%= position.getDeadlineScheduleLeaveAfter() %>" class="elemenForm" size="10">                                                                                                           
                                                      <i style="font-size: 11px">(Hour) Limit Time Update Schedule Leave after current time ( unlimited time = 8640 )</i>
                                                      </td>
                                                    </tr>                     
                                                         
                                                    <tr align="left" valign="top"> 
                                                      <td valign="top" width="10%"> 
                                                        Job Description </td>
                                                      <td width="1%">:</td>
                                                      <td width="89%"> 
                                                        <textarea name="<%=frmPosition.fieldNames[FrmPosition.FRM_FIELD_DESCRIPTION] %>" class="elemenForm" cols="30" rows="3"><%= position.getDescription() %></textarea>
                                                        <br><br><button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdJobDescDet()">Job Desc Detail</button>
                                                      </td>
                                                    </tr>
                                                    <% if (iCommand == Command.EDIT){ %>
                                                    <tr>
                                                        <td valign="top">Competency</td>
                                                        <td valign="top">:</td>
                                                        <td valign="top">
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdAddCompetency()">Add Competency</button>
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdRefresh()">Refresh</button>
                                                            <%
                                                            String where = "";
                                                            where = ""+oidPosition;
                                                            Vector listPosCompetency = new Vector();
                                                            listPosCompetency = PstPositionCompetencyRequired.listInnerJoin(where);
                                                            if(listPosCompetency!= null && listPosCompetency.size() > 0){
                                                                if (askCommand == 1){ %>
                                                                <div id="confirm">
                                                                    <strong>Are you sure to delete item ?</strong> &nbsp;
                                                                    <button id="btn1" onclick="javascript:cmdDeleteCompetency('<%=deleteCompetencyId%>')">Yes</button>
                                                                    &nbsp;<button id="btn1" onclick="javascript:cmdBackConfirm()">No</button>
                                                                </div>
                                                                <% } %>
                                                                <table cellspacing="0" cellpadding="0" class="tblStyle" style="background-color: #FFF">
                                                                    <tr>
                                                                        <td class="title_tbl">Competency Name</td>
                                                                        <td class="title_tbl">Score Min</td>
                                                                        <td class="title_tbl">Score Req</td>
                                                                        <td class="title_tbl">&nbsp;</td>
                                                                    </tr>
                                                                    <%
                                                                    for(int k = 0; k < listPosCompetency.size(); k++){
                                                                        Vector vect = (Vector)listPosCompetency.get(k);
                                                                        PositionCompetencyRequired posCom = (PositionCompetencyRequired)vect.get(0);
                                                                        Competency comp = (Competency)vect.get(1); 
                                                                        %>
                                                                    <tr>
                                                                        <td><a href="javascript:cmdEditCompetency('<%=posCom.getOID()%>')"><%=comp.getCompetencyName()%></a></td>
                                                                        <td><%=posCom.getScoreReqMin()%></td>
                                                                        <td><%=posCom.getScoreReqRecommended()%></td>
                                                                        <td><button id="btn" onclick="cmdAskCompetency('<%=posCom.getOID()%>')">&times;</button></td>
                                                                    </tr>
                                                                        <%
                                                                    }
                                                                    %>
                                                                </table>
                                                                <%
                                                            }
                                                            %>
                                                        </td>
                                                    </tr>
                                                    
                                                    <tr>
                                                        <td valign="top">Training</td>
                                                        <td valign="top">:</td>
                                                        <td valign="top">
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdAddTraining()">Add Training</button>
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdRefresh()">Refresh</button>
                                                            <%
                                                            String whereTrain = "";
                                                            whereTrain = ""+oidPosition;
                                                            Vector listPosTrain = new Vector();
                                                            listPosTrain = PstPositionTrainingRequired.listInnerJoin(whereTrain);
                                                            if(listPosTrain!= null && listPosTrain.size() > 0){
                                                                if (askCommand == 2){ %>
                                                                <div id="confirm">
                                                                    <strong>Are you sure to delete item ?</strong> &nbsp;
                                                                    <button id="btn1" onclick="javascript:cmdDeleteTraining('<%=deleteTrainingId%>')">Yes</button>
                                                                    &nbsp;<button id="btn1" onclick="javascript:cmdBackConfirm()">No</button>
                                                                </div>
                                                                <% } %>
                                                                <table cellspacing="0" cellpadding="0" class="tblStyle" style="background-color: #FFF">
                                                                    <tr>
                                                                        <td class="title_tbl">Training Name</td>
                                                                        <td class="title_tbl">Score Min</td>
                                                                        <td class="title_tbl">Score Req</td>
                                                                        <td class="title_tbl">&nbsp;</td>
                                                                    </tr>
                                                                    <%
                                                                for(int k = 0; k < listPosTrain.size(); k++){
                                                                    Vector vect = (Vector)listPosTrain.get(k);
                                                                    PositionTrainingRequired posT = (PositionTrainingRequired)vect.get(0);
                                                                    Training train = (Training)vect.get(1); 
                                                                    %>
                                                                    <tr>
                                                                        <td><a href="javascript:cmdEditTraining('<%=posT.getOID()%>')"><%=train.getName()%></a></td>
                                                                        <td><%=posT.getPointMin()%></td>
                                                                        <td><%=posT.getPointRecommended()%></td>
                                                                        <td><button id="btn" onclick="cmdAskTraining('<%=posT.getOID()%>')">&times;</button></td>
                                                                    </tr>
                                                                    <%
                                                                }
                                                                %>
                                                                </table>
                                                                <%
                                                            }
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">Education</td>
                                                        <td valign="top">:</td>
                                                        <td valign="top">
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdAddEducation()">Add Education</button>
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdRefresh()">Refresh</button>
                                                            <%
                                                            String whereEdu = "";
                                                            whereEdu = ""+oidPosition;
                                                            Vector listPosEdu = new Vector();
                                                            listPosEdu = PstPositionEducationRequired.listInnerJoin(whereEdu);
                                                            if(listPosEdu!= null && listPosEdu.size() > 0){
                                                                if (askCommand == 3){ %>
                                                                <div id="confirm">
                                                                    <strong>Are you sure to delete item ?</strong> &nbsp;
                                                                    <button id="btn1" onclick="javascript:cmdDeleteEducation('<%=deleteEduId%>')">Yes</button>
                                                                    &nbsp;<button id="btn1" onclick="javascript:cmdBackConfirm()">No</button>
                                                                </div>
                                                                <% } %>
                                                                <table cellspacing="0" cellpadding="0" class="tblStyle" style="background-color: #FFF">
                                                                    <tr>
                                                                        <td class="title_tbl">Education</td>
                                                                        <td class="title_tbl">Point Min</td>
                                                                        <td class="title_tbl">Point Req</td>
                                                                        <td class="title_tbl">&nbsp;</td>
                                                                    </tr>
                                                                    <%
                                                                for(int k = 0; k < listPosEdu.size(); k++){
                                                                    Vector vect = (Vector)listPosEdu.get(k);
                                                                    PositionEducationRequired posE = (PositionEducationRequired)vect.get(0);
                                                                    Education ed = (Education)vect.get(1);
                                                                    %>
                                                                    <tr>
                                                                        <td><a href="javascript:cmdEditEducation('<%=posE.getOID()%>')"><%=ed.getEducation()%></a></td>
                                                                        <td><%=posE.getPointMin()%></td>
                                                                        <td><%=posE.getPointRecommended()%></td>
                                                                        <td><button id="btn" onclick="cmdAskEducation('<%=posE.getOID()%>')">&times;</button></td>
                                                                    </tr>
                                                                    <%
                                                                }
                                                                %>
                                                                </table>
                                                                <%
                                                            }
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td valign="top">Key Performance Indicator (KPI)</td>
                                                        <td valign="top">:</td>
                                                        <td valign="top">
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdAddKpi()">Add KPI</button>
                                                            <button id="btn" style="margin-bottom: 3px;" onclick="javascript:cmdRefresh()">Refresh</button>
                                                            <%
                                                            String whereKpi = "";
                                                            whereKpi = ""+oidPosition;
                                                            Vector listPosKpi = new Vector();
                                                            listPosKpi = PstPositionKPI.listInnerJoin(whereKpi);
                                                            if(listPosKpi!= null && listPosKpi.size() > 0){
                                                                if (askCommand == 4){ %>
                                                                <div id="confirm">
                                                                    <strong>Are you sure to delete item ?</strong> &nbsp;
                                                                    <button id="btn1" onclick="javascript:cmdDeleteEducation('<%=deleteEduId%>')">Yes</button>
                                                                    &nbsp;<button id="btn1" onclick="javascript:cmdBackConfirm()">No</button>
                                                                </div>
                                                                <% } %>
                                                                <table cellspacing="0" cellpadding="0" class="tblStyle" style="background-color: #FFF">
                                                                    <tr>
                                                                        <td class="title_tbl">KPI Name</td>
                                                                        <td class="title_tbl">Score Min</td>
                                                                        <td class="title_tbl">Score Req</td>
                                                                        <td class="title_tbl">&nbsp;</td>
                                                                    </tr>
                                                                <%
                                                                for(int k = 0; k < listPosKpi.size(); k++){
                                                                    Vector vect = (Vector)listPosKpi.get(k);
                                                                    PositionKPI posKpi = (PositionKPI)vect.get(0);
                                                                    KPI_List kpiList = (KPI_List)vect.get(1);
                                                                    
                                                                    %>
                                                                    <tr>
                                                                        <td><a href="javascript:cmdEditKpi('<%=posKpi.getOID()%>')"><%=kpiList.getKpi_title()%></a></td>
                                                                        <td><%=posKpi.getScoreMin()%></td>
                                                                        <td><%=posKpi.getScoreRecommended()%></td>
                                                                        <td><button id="btn" onclick="cmdDeleteKpi('<%=posKpi.getOID()%>')">&times;</button></td>
                                                                    </tr>
                                                                    
                                                                    <%
                                                                }
                                                                %>
                                                                </table>
                                                                <%
                                                            }
                                                            %>
                                                        </td>
                                                    </tr>
                                                    <% } %>
                                                  </table>
                                                </td>
                                                <td valign="top">
                                                    
                                                    <div class="part_content">
                                                        <div class="part_name">
                                                            Position Availability
                                                        </div>
                                                        <div style="padding: 12px 19px;">
                                                            <table>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <button id="btn" onclick="cmdAddPositionCompany('<%=oidPosition%>')" style="margin:5px 0px 2px 0px">Add Position to Company</button>
                                                                        <select name="select_company">
                                                                            <option value="0">-select-</option>
                                                                            <%
                                                                            Vector listCompany = PstCompany.list(0, 0, "", "");
                                                                            if (listCompany != null && listCompany.size()>0){
                                                                                for(int i=0; i<listCompany.size(); i++){
                                                                                    Company comp = (Company)listCompany.get(i);
                                                                                    %>
                                                                                    <option value="<%=comp.getOID()%>"><%=comp.getCompany()%></option>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>

                                                                        </select>
                                                                        <table class="tblStyle">
                                                                            <tr>
                                                                                <td class="title_tbl">No</td>
                                                                                <td class="title_tbl">Company Name</td>
                                                                                <td class="title_tbl">&nbsp;</td>
                                                                            </tr>
                                                                            <%
                                                                            String wherePosComp = "";
                                                                            
                                                                            if (oidPosition > 0){
                                                                               wherePosComp = "POSITION_ID="+oidPosition;
                                                                           
                                                                            Vector listOfPosComp = PstPositionCompany.list(0, 0, wherePosComp, "");
                                                                            if (listOfPosComp != null && listOfPosComp.size()>0){
                                                                                for(int p1=0; p1<listOfPosComp.size(); p1++){
                                                                                   try{ PositionCompany posComp = (PositionCompany)listOfPosComp.get(p1);
                                                                                    Company company = PstCompany.fetchExc(posComp.getCompanyId());
                                                                                    %>
                                                                                    <tr>
                                                                                        <td><%=p1+1%></td>
                                                                                        <td><%=company.getCompany()%></td>
                                                                                        <td><button id="btn" onclick="cmdDeletePosComp(<%=posComp.getOID()%>)">&times;</button></td>
                                                                                    </tr>
                                                                                    <%}catch(Exception exc){ System.out.println(exc);}
                                                                                }
                                                                            }
                                                                            }
                                                                            %>
                                                                            
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <button id="btn" onclick="cmdAddPositionDivisi('<%=oidPosition%>')" style="margin:5px 0px 2px 0px">Add Position to Division</button>
                                                                        <select name="select_divisi">
                                                                            <option value="0">-select-</option>
                                                                            <%
                                                                            orderBy = PstDivision.fieldNames[PstDivision.FLD_DIVISION]+" ASC";
                                                                            Vector listDiv = PstDivision.list(0, 0, "", orderBy);
                                                                            if (listDiv != null && listDiv.size() > 0){
                                                                                for(int d1=0; d1<listDiv.size(); d1++){
                                                                                    Division divisi = (Division)listDiv.get(d1);
                                                                                    %>
                                                                                    <option value="<%=divisi.getOID()%>"><%=divisi.getDivision()%></option>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>
                                                                            
                                                                        </select>
                                                                        <table class="tblStyle">
                                                                            <tr>
                                                                                <td class="title_tbl">No</td>
                                                                                <td class="title_tbl">Division Name</td>
                                                                                <td class="title_tbl">&nbsp;</td>
                                                                            </tr>
                                                                            <%
                                                                            String whereDiv = "POSITION_ID="+oidPosition;
                                                                            Vector listOfDiv = PstPositionDivision.list(0, 0, whereDiv, "");
                                                                            if (listOfDiv != null && listOfDiv.size()>0){
                                                                                for(int p2 = 0; p2<listOfDiv.size(); p2++){
                                                                                    PositionDivision posDiv = (PositionDivision)listOfDiv.get(p2);
                                                                                    String divisi = "";
                                                                                    Vector listDivi = PstDivision.list(0, 0, "DIVISION_ID="+posDiv.getDivisionId(), "");
                                                                                    if (listDivi != null && listDivi.size()>0){
                                                                                        Division div = (Division)listDivi.get(0);
                                                                                        divisi = div.getDivision();
                                                                                    }
                                                                                    %>
                                                                                    <tr>
                                                                                        <td><%=p2+1%></td>
                                                                                        <td><%=divisi%></td>
                                                                                        <td><button id="btn">&times;</button></td>
                                                                                    </tr>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <button id="btn" onclick="cmdAddPositionDepart('<%=oidPosition%>')" style="margin:5px 0px 2px 0px">Add Position to Department</button>
                                                                        <select name="select_depart">
                                                                            <option value="0">-select-</option>
                                                                            <%
                                                                            orderBy = PstDepartment.fieldNames[PstDepartment.FLD_DEPARTMENT]+" ASC";
                                                                            Vector listDepart = PstDepartment.list(0, 0, "", orderBy);
                                                                            if (listDepart != null && listDepart.size()>0){
                                                                                for(int d2=0; d2<listDepart.size(); d2++){
                                                                                    Department depart = (Department)listDepart.get(d2);
                                                                                    %>
                                                                                    <option value="<%=depart.getOID()%>"><%=depart.getDepartment()%></option>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>
                                                                        </select>
                                                                        <table class="tblStyle">
                                                                            <tr>
                                                                                <td class="title_tbl">No</td>
                                                                                <td class="title_tbl">Department Name</td>
                                                                                <td class="title_tbl">&nbsp;</td>
                                                                            </tr>
                                                                            <%
                                                                            String wherePosDepart = "POSITION_ID="+oidPosition;
                                                                            Vector listOfPosDepart = PstPositionDepartment.list(0, 0, wherePosDepart, "");
                                                                            if (listOfPosDepart != null && listOfPosDepart.size()>0){
                                                                                for(int p3=0; p3<listOfPosDepart.size(); p3++){
                                                                                    PositionDepartment posDepart = (PositionDepartment)listOfPosDepart.get(p3);
                                                                                    Department dep = PstDepartment.fetchExc(posDepart.getDepartmentId());
                                                                                    %>
                                                                                    <tr>
                                                                                        <td><%=p3+1%></td>
                                                                                        <td><%=dep.getDepartment()%></td>
                                                                                        <td><button id="btn">&times;</button></td>
                                                                                    </tr>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                                <tr>
                                                                    <td valign="top">
                                                                        <button id="btn" onclick="cmdAddPositionSection('<%=oidPosition%>')" style="margin:5px 0px 2px 0px">Add Position to Section</button>
                                                                        <select name="select_section">
                                                                            <option value="0">-select-</option>
                                                                            <%
                                                                            orderBy = PstSection.fieldNames[PstSection.FLD_SECTION]+" ASC";
                                                                            Vector listSection = PstSection.list(0, 0, "", orderBy);
                                                                            if (listSection != null && listSection.size()>0){
                                                                                for (int s=0; s<listSection.size(); s++){
                                                                                    Section section = (Section)listSection.get(s);
                                                                                    %>
                                                                                    <option value="<%=section.getOID()%>"><%=section.getSection()%></option>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>
                                                                        </select>
                                                                        <table class="tblStyle">
                                                                            <tr>
                                                                                <td class="title_tbl">No</td>
                                                                                <td class="title_tbl">Section Name</td>
                                                                                <td class="title_tbl">&nbsp;</td>
                                                                            </tr>
                                                                            <%
                                                                            String whereSec = "POSITION_ID="+oidPosition;
                                                                            Vector listOfSection = PstPositionSection.list(0, 0, whereSec, "");
                                                                            if (listOfSection != null && listOfSection.size()>0){
                                                                                for(int p3 = 0; p3<listOfSection.size(); p3++){
                                                                                    PositionSection posSection = (PositionSection)listOfSection.get(p3);
                                                                                    String section = "";
                                                                                    Vector listSec = PstSection.list(0, 0, "SECTION_ID="+posSection.getSectionId(), "");
                                                                                    if (listSec != null && listSec.size()>0){
                                                                                        Section sec = (Section)listSec.get(0);
                                                                                        section = sec.getSection();
                                                                                    }
                                                                                    %>
                                                                                    <tr>
                                                                                        <td><%=p3+1%></td>
                                                                                        <td><%=section%></td>
                                                                                        <td><button id="btn">&times;</button></td>
                                                                                    </tr>
                                                                                    <%
                                                                                }
                                                                            }
                                                                            %>
                                                                        </table>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </div>
                                                    </div>   
                                                    <div>&nbsp;</div>
                                                    <div class="part_content">
                                                        <div class="part_name">
                                                            Up Link Position
                                                        </div>
                                                        <div style="padding: 12px 19px;">
                                                            <table class="tblStyle">
                                                                <tr>
                                                                    <td class="title_tbl">No</td>
                                                                    <td class="title_tbl">Position</td>
                                                                    <td class="title_tbl">Template</td>
                                                                    <td class="title_tbl">Start Date</td>
                                                                    <td class="title_tbl">End Date</td>
                                                                    <td class="title_tbl">Type</td>
                                                                </tr>
                                                                <%
                                                                String whereUpPos = PstMappingPosition.fieldNames[PstMappingPosition.FLD_DOWN_POSITION_ID]+"="+oidPosition;
                                                                String orderUpPos = " "+PstMappingPosition.fieldNames[PstMappingPosition.FLD_TEMPLATE_ID]+" ";
                                                                Vector listUpPos = PstMappingPosition.list(0, 0, whereUpPos, orderUpPos);
                                                                int no = 0;
                                                                if (listUpPos != null&& listUpPos.size()>0){
                                                                    for (int up=0; up<listUpPos.size(); up++){
                                                                        MappingPosition upPos = (MappingPosition)listUpPos.get(up);
                                                                        no++;
                                                                        %>
                                                                <tr>
                                                                    <td><%=no%></td>
                                                                    <td><%=getPositionName(upPos.getUpPositionId())%></td>
                                                                    <td><%=getTemplateName(upPos.getTemplateId())%></td>
                                                                    <td><%=upPos.getStartDate()%></td>
                                                                    <td><%=upPos.getEndDate()%></td>
                                                                    <td><%=PstMappingPosition.typeOfLink[upPos.getTypeOfLink()]%></td>
                                                                </tr>
                                                                        <%
                                                                    }
                                                                }
                                                                %>
                                                            </table>
                                                        </div>
                                                    </div>
                                                    <div>&nbsp;</div>
                                                    <div class="part_content">
                                                        <div class="part_name">
                                                            Down Link Position
                                                        </div>
                                                        <div style="padding: 12px 19px;">
                                                            <table class="tblStyle">
                                                                <tr>
                                                                    <td class="title_tbl">No</td>
                                                                    <td class="title_tbl">Position</td>
                                                                    <td class="title_tbl">Template</td>
                                                                    <td class="title_tbl">Start Date</td>
                                                                    <td class="title_tbl">End Date</td>
                                                                    <td class="title_tbl">Type</td>
                                                                </tr>
                                                                <%
                                                                String whereDownPos = PstMappingPosition.fieldNames[PstMappingPosition.FLD_UP_POSITION_ID]+"="+oidPosition;
                                                                String orderDownPos = " "+PstMappingPosition.fieldNames[PstMappingPosition.FLD_TEMPLATE_ID]+" ";
                                                                Vector listDownPos = PstMappingPosition.list(0, 0, whereDownPos, orderDownPos);
                                                                int noDown = 0;
                                                                if (listDownPos != null&& listDownPos.size()>0){
                                                                    for (int down=0; down<listDownPos.size(); down++){
                                                                        MappingPosition downPosition = (MappingPosition)listDownPos.get(down);
                                                                        noDown++;
                                                                        %>
                                                                <tr>
                                                                    <td><%=noDown%></td>
                                                                    <td><%=getPositionName(downPosition.getDownPositionId())%></td>
                                                                    <td><%=getTemplateName(downPosition.getTemplateId())%></td>
                                                                    <td><%=downPosition.getStartDate()%></td>
                                                                    <td><%=downPosition.getEndDate()%></td>
                                                                    <td><%=PstMappingPosition.typeOfLink[downPosition.getTypeOfLink()]%></td>
                                                                </tr>
                                                                        <%
                                                                    }
                                                                }
                                                                %>
                                                            </table>
                                                        </div>
                                                    </div>
                                                </td>
                                              </tr>
                                              <tr> 
                                                <td colspan="2"> 
                                                  <%
									ctrLine.setLocationImg(approot+"/images");
									ctrLine.initDefault();
									ctrLine.setTableWidth("80%");
									String scomDel = "javascript:cmdAsk('"+oidPosition+"')";
									String sconDelCom = "javascript:cmdConfirmDelete('"+oidPosition+"')";
									String scancel = "javascript:cmdEdit('"+oidPosition+"')";
									ctrLine.setBackCaption("Back to List");
									ctrLine.setCommandStyle("buttonlink");
									ctrLine.setBackCaption("Back to List Position");
									ctrLine.setSaveCaption("Save Position");
									ctrLine.setConfirmDelCaption("Yes Delete Position");
									ctrLine.setDeleteCaption("Delete Position");

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
