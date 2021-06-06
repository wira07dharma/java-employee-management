/*
 * TransferListXLS.java
 *
 * Created on December 24, 2007, 2:27 PM
 */
package com.dimata.harisma.printout;

import com.dimata.harisma.entity.admin.AppObjInfo;
import com.dimata.harisma.report.payroll.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.*;

import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.dimata.qdep.form.*;
import com.dimata.util.*;
import com.dimata.harisma.entity.search.*;
import com.dimata.harisma.entity.employee.*;
import com.dimata.harisma.entity.leave.I_Leave;
import com.dimata.harisma.entity.masterdata.*;
import com.dimata.harisma.entity.overtime.OvertimeDetail;
import com.dimata.harisma.entity.overtime.OvertimeSummary;
import com.dimata.harisma.entity.overtime.PstOvertimeDetail;
import com.dimata.harisma.entity.payroll.*;
import com.dimata.harisma.session.admin.SessUserSession;
import com.dimata.harisma.session.employee.SessEmployee;
import com.dimata.system.entity.system.PstSystemProperty;
import java.io.PrintWriter;
import com.dimata.harisma.form.payroll.*;
import com.dimata.harisma.session.payroll.TaxCalculator;
import com.dimata.harisma.util.email;
import com.dimata.printman.DSJ_PrintObj;
import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import javax.activation.DataSource;
import javax.mail.util.ByteArrayDataSource;

/**
 *
 * @author Yunny
 */
public class PayPrintPdf extends HttpServlet {

    private static int pageLength = 32; // maximum row per slip
    private static int pageWidth = 89; // maximum lebar per slip
    //private static int pageWidth = 80; // maximum lebar per slip
    private static int leftMargin = 1; // margin di kiri
    private static int maxLeftgSiteLength = 43; // maximum lebar bagian kiri slip spt untuk company dan data karyawan
    //private static int maxLeftgSiteLength = 48; // maximum lebar bagian kiri slip spt untuk company dan data karyawan
    private static int startCompany = 1; // row mulai print company
    private static int startColHeaderValue = 1;
    private static int startRowSlipComp = 6; // start row slip component
    static int startColSlipComp = 3; // start col slip component    
    static int maxWidthSlipComp = ((getPageWidth() - startColSlipComp) * 2) / 3;
    static int startColSlipValue = startColSlipComp + maxWidthSlipComp + 1; // start col slip component
    static int maxWidthSlipValue = ((getPageWidth() - startColSlipComp)) / 3;
    public static String TOTAL_BENEFIT="TOTAL BENEFIT";
    public static String TOTAL_DEDUCTION="TOTAL DEDUCTION";
    public static String TOTAL_TAKE_HOME_PAY="TOTAL TAKE HOME PAY";
    public static Rectangle PAGE_SIZE=PageSize.A4;
    /**
     * @return the pageLength
     */
    public static int getPageLength() {
        return pageLength;
    }

    /**
     * @param aPageLength the pageLength to set
     */
    public static void setPageLength(int aPageLength) {
        pageLength = aPageLength;
    }

    /**
     * @return the pageWidth
     */
    public static int getPageWidth() {
        return pageWidth;
    }

    /**
     * @return the leftMargin
     */
    public static int getLeftMargin() {
        return leftMargin;
    }

    /**
     * @return the maxLeftgSiteLength
     */
    public static int getMaxLeftgSiteLength() {
        return maxLeftgSiteLength;
    }

    /**
     * @return the startCompany
     */
    public static int getStartCompany() {
        return startCompany;
    }

    /**
     * @return the startColHeaderValue
     */
    public static int getStartColHeaderValue() {
        return startColHeaderValue;
    }

    /**
     * @return the startRowSlipComp
     */
    public static int getStartRowSlipComp() {
        return startRowSlipComp;
    }

    /**
     * @param aPageWidth the pageWidth to set
     */
    public static void setPageWidth(int aPageWidth) {
        pageWidth = aPageWidth;
    }

    /**
     * @param aLeftMargin the leftMargin to set
     */
    public static void setLeftMargin(int aLeftMargin) {
        leftMargin = aLeftMargin;
    }

    /**
     * @param aMaxLeftgSiteLength the maxLeftgSiteLength to set
     */
    public static void setMaxLeftgSiteLength(int aMaxLeftgSiteLength) {
        maxLeftgSiteLength = aMaxLeftgSiteLength;
    }

    /**
     * @param aStartCompany the startCompany to set
     */
    public static void setStartCompany(int aStartCompany) {
        startCompany = aStartCompany;
    }

    /**
     * @param aStartColHeaderValue the startColHeaderValue to set
     */
    public static void setStartColHeaderValue(int aStartColHeaderValue) {
        startColHeaderValue = aStartColHeaderValue;
    }

    /**
     * @param aStartRowSlipComp the startRowSlipComp to set
     */
    public static void setStartRowSlipComp(int aStartRowSlipComp) {
        startRowSlipComp = aStartRowSlipComp;
    }

    /**
     * Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

    }

    /**
     * Destroys the servlet.
     */
    public void destroy() {
    }

    /**
     * Creates a new instance of TransferListXLS
     */
    public PayPrintPdf() {
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

       // System.out.println("---===| Print Payroll as PDF |===--- ");
        //response.setContentType("text/html");
        //response.setContentType("application/vnd.ms-excel");
        //response.setContentType("text/txt");//application/msword");

       // PrintWriter out = response.getWriter(); //response.getOutputStream();
                response.setContentType("application/pdf");
                //response.setContentLength(baos.size());
                ServletOutputStream out = response.getOutputStream();

        /* get data from session */
        HttpSession session = request.getSession(true);
        int appObjCode = AppObjInfo.composeObjCode(AppObjInfo.G1_PAYROLL, AppObjInfo.G2_PAYROLL_PROCESS, AppObjInfo.OBJ_PAYROLL_PROCESS_PRINT);

        boolean isLoggedIn = false; // indicate if the user is loggen in or not
        SessUserSession userSession = (SessUserSession) session.getValue(SessUserSession.HTTP_SESSION_NAME); // instant of object user session
        int  SESS_LANGUAGE =0;
        try {
            if (userSession == null) {
                userSession = new SessUserSession();
            } else {
                if (userSession.isLoggedIn() == true) {
                    isLoggedIn = true;
                }
            }
            String strLanguage = "";
            if (session.getValue("APPLICATION_LANGUAGE") != null) {
                strLanguage = String.valueOf(session.getValue("APPLICATION_LANGUAGE"));
            }

            int langDefault = com.dimata.util.lang.I_Language.LANGUAGE_DEFAULT;
            int langForeign = com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN;
            SESS_LANGUAGE = (strLanguage!=null && strLanguage.length() > 0) ? Integer.parseInt(strLanguage) : langForeign;
            
        } catch (Exception exc) {
            out.println(" >>> Exception during check login");
            out.flush();
            return;
        }

        boolean privPrint = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_PRINT));
        boolean privView = userSession.checkPrivilege(AppObjInfo.composeCode(appObjCode, AppObjInfo.COMMAND_VIEW));

        if (!privView && !privPrint) {
            out.println(" >>> You have no privileges");
            out.flush();
            return;
        }

        Color border = new Color(0x00, 0x00, 0x00);

        // setting some fonts in the color chosen by the user
        Font fontHeaderBig = new Font(Font.HELVETICA, 10, Font.BOLD, border);
        Font fontHeaderSmall = new Font(Font.HELVETICA, 6, Font.NORMAL, border);
        Font fontHeader = new Font(Font.HELVETICA, 8, Font.BOLD, border);
        Font fontContent = new Font(Font.HELVETICA, 8, Font.BOLD, border);
        Font tableContent = new Font(Font.HELVETICA, 8, Font.NORMAL, border);
        Font fontSpellCharge = new Font(Font.HELVETICA, 8, Font.BOLDITALIC, border);
        Font fontItalic = new Font(Font.HELVETICA, 8, Font.BOLDITALIC, border);
        Font fontItalicBottom = new Font(Font.HELVETICA, 8, Font.ITALIC, border);
        Font fontUnderline = new Font(Font.HELVETICA, 8, Font.UNDERLINE, border);

        Color bgColor = new Color(240, 240, 240);

        Color blackColor = new Color(0, 0, 0);

        Color putih = new Color(250, 250, 250);

        boolean toEmail = FRMQueryString.requestBoolean(request, "toEmail");
        int detailSlipYN = FRMQueryString.requestInt(request, "detail_slip");
        int iCommand = FRMQueryString.requestCommand(request);
        long periodId = FRMQueryString.requestLong(request, "periodId");
        int keyPeriod = FRMQueryString.requestInt(request, "paySlipPeriod");
        //update by satrya 2013-01-24
        long payGroupId = FRMQueryString.requestLong(request, "payGroupId");
        //String frmCurrency = "#,###";
        //update by satrya 2014-6-01
        String frmCurrency = "#,###";
        String mataUang = "Rp";
        CurrencyType currencyType = new CurrencyType();
        try {
            Vector currType = PstCurrencyType.list(0, 1, PstCurrencyType.fieldNames[PstCurrencyType.FLD_INCLUDE_INF_PROCESS] + "=" + PstCurrencyType.YES_USED, "");
            if (currType != null && currType.size() > 0) {
                currencyType = (CurrencyType) currType.get(0);
                frmCurrency = currencyType.getFormatCurrency();
                mataUang = currencyType.getCode() + ".";
            }
        } catch (Exception exc) {
        }

        boolean printZeroValue = true;
        String sprintZeroValue = PstSystemProperty.getValueByName("PAYROLL_PRINT_ZERO_VALUE");
        if (sprintZeroValue == null || sprintZeroValue.length() == 0 || sprintZeroValue.equalsIgnoreCase("YES")
                || sprintZeroValue.equalsIgnoreCase("1") || sprintZeroValue.equalsIgnoreCase("Not initialized")) {
            printZeroValue = true;
        } else {
            printZeroValue = false;
        }

        PayPeriod objPeriod = new PayPeriod();
        // Period objPeriod = new Period();
        try {
            //update by satrya 2013-02-12
            objPeriod = PstPayPeriod.fetchExc(periodId);
        } catch (Exception exc) {
        }

        CtrlPaySlipComp ctrlPaySlipComp = new CtrlPaySlipComp(request);
        int start = FRMQueryString.requestInt(request, "start");
        int inclResign = FRMQueryString.requestInt(request, "INCLUDE_RESIGN");
        boolean bIncResign = (inclResign == 1);
        long oidDivision = FRMQueryString.requestLong(request, "division");
        long oidDepartment = FRMQueryString.requestLong(request, "department");
        long oidSection = FRMQueryString.requestLong(request, "section");
        long oidPaySlipComp = FRMQueryString.requestLong(request, "section");
        String searchNrFrom = FRMQueryString.requestString(request, "searchNrFrom");
        String searchNrTo = FRMQueryString.requestString(request, "searchNrTo");
        String searchName = FRMQueryString.requestString(request, "searchName");
        int dataStatus = FRMQueryString.requestInt(request, "dataStatus");
        String codeComponenGeneral = FRMQueryString.requestString(request, "compCode");
        String compName = FRMQueryString.requestString(request, "compName");
        int aksiCommand = FRMQueryString.requestInt(request, "aksiCommand");
        long periodeId = FRMQueryString.requestLong(request, "periodId");
        int numKolom = FRMQueryString.requestInt(request, "numKolom");
        int statusSave = FRMQueryString.requestInt(request, "statusSave");
        
        int iErrCode = FRMMessage.ERR_NONE;
        String msgString = "";
        String msgStr = "";
        int recordToGet = 1000;
        int vectSize = 0;
        String orderClause = "";
        String whereClause = "";

        iErrCode = ctrlPaySlipComp.action(iCommand, oidPaySlipComp);
        FrmPaySlipComp frmPaySlipComp = ctrlPaySlipComp.getForm();
        PaySlipComp paySlipComp = ctrlPaySlipComp.getPaySlipComp();
        msgString = ctrlPaySlipComp.getMessage();

        Vector listEmpPaySlip = SessEmployee.listEmpPaySlip(oidDepartment, oidDivision, oidSection, searchNrFrom, searchNrTo, searchName, periodeId, -1, bIncResign, 0);
//        String s_employee_id = null;
//        String s_payslip_id = null;
//        String s_level_code = null;
        long oidEmployee = 0;
        String noPrint = "";
        //update by satrya 2013-01-29
        Vector sumOvertimeDailyDurIdxSalary = new Vector();
        Vector sumOvertimeDailyDurIdxDP = new Vector();
        Vector sumOvertimeDailyDurIdxTotal = new Vector();
        if (objPeriod.getStartDate() != null && objPeriod.getEndDate() != null) {
            sumOvertimeDailyDurIdxSalary = PstOvertimeDetail.listSummaryOT(oidDepartment, oidDivision, oidSection, searchNrFrom, searchNrTo, searchName, objPeriod.getStartDate(), objPeriod.getEndDate(), bIncResign, OvertimeDetail.PAID_BY_SALARY, -1);
            sumOvertimeDailyDurIdxDP = PstOvertimeDetail.listSummaryOT(oidDepartment, oidDivision, oidSection, searchNrFrom, searchNrTo, searchName, objPeriod.getStartDate(), objPeriod.getEndDate(), bIncResign, -1, OvertimeDetail.PAID_BY_DAY_OFF);
            sumOvertimeDailyDurIdxTotal = PstOvertimeDetail.listSummaryOT(oidDepartment, oidDivision, oidSection, searchNrFrom, searchNrTo, searchName, objPeriod.getStartDate(), objPeriod.getEndDate(), bIncResign, -1, -1);
        }
        Vector<DataSource> attachment = new Vector(); // by kartika 2015-09-12
        Vector<String> attachmentName = new Vector(); // by kartika 2015-09-12
        Vector<String> emailLogs = new Vector();
        emailLogs.add("Payroll send via email by Dimata Hairisma System on "+ Formater.formatDate(new Date(),"yyyy-MM-dd hh:mm:ss"));
        //update by satrya 2013-01-14
        String[] listNoPrint = new String[listEmpPaySlip.size()];
        int noInc = 0;
        if (aksiCommand == 1) {
            System.out.println("print selected");
            Vector listEmpId = new Vector();

            for (int i = 0; i < listEmpPaySlip.size(); i++) {

                try {
                    oidEmployee = FRMQueryString.requestLong(request, "print" + i + ""); // row yang dicheked
                    noPrint = FRMQueryString.requestString(request, "no_print" + i + "");
                } catch (Exception e) {
                    System.out.println("Exception" + e);
                }
                //System.out.println("nilai statusSave"+statusSave);
                //update by satrya 2013-01-14
                if (oidEmployee != 0) {
                    listEmpId.add(oidEmployee);
                    listNoPrint[noInc] = noPrint;
                    noInc++;
                }

            }
            if (listEmpId != null && listEmpId.size() > 0) {
                listEmpPaySlip = SessEmployee.listEmpPaySlipByEmployeeId(listEmpId, periodeId, -1, bIncResign);
            }
        } else {
            for (int i = 0; i < listEmpPaySlip.size(); i++) {
                listNoPrint[i] = String.valueOf(1 + i);
            }
        }

        int MODEL_PAYSLIP = PaySlip.MODEL_UP_DOWN;
        int payslipOT = 0;
        try {
            String strSlip = PstSystemProperty.getValueByName("MODEL PAYSLIP");
            if (strSlip != null) {
                MODEL_PAYSLIP = Integer.parseInt(strSlip);
            }
        } catch (Exception exc) {
        }

        String spayslipOT = null;
        try {
            spayslipOT = PstSystemProperty.getValueByName("PAYROLL_PAY_SLIP_CONFIG_OVERTIME_LOCATION");
            if (spayslipOT != null) {
                payslipOT = Integer.parseInt(spayslipOT);
            }
        } catch (Exception exc) {
            System.out.println("Exception PAYROLL_PAY_SLIP_CONFIG_OVERTIME_LOCATION not be set");
            payslipOT = 0;
        }
        long SignatureOnOff = 0;
        try {
            SignatureOnOff = Long.parseLong(PstSystemProperty.getValueByName("AKTIF_SIGNATURE"));
        } catch (Exception E) {
        }

        String codeUpahLembur = null;
        try {
            codeUpahLembur = PstSystemProperty.getValueByName("PAYROLL_PAY_SLIP_CODE_UPAH_LEMBUR");

        } catch (Exception exc) {
            System.out.println("Exception PAYROLL_PAY_SLIP_CODE_UPAH_LEMBUR not be set");

        }
        I_Leave leaveConfig = null;
        try {
            leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
        } catch (Exception e) {
            System.out.println("Exception LEAVE_CONFIG: " + e.getMessage());
        }
        //update by satrya 2013-02-21
        String codeTax_count = "";
        try {
            codeTax_count = PstSystemProperty.getValueByName("PAY_TAX_CON");
        } catch (Exception exc) {
            codeTax_count = "TAX_CON";
        }

        String pictPath = "";

        PayPeriod payPeriod = new PayPeriod();
        //Period period = new Period();
        String periodName = "";
        Date endDatePeriod = new Date();
        try {
            payPeriod = PstPayPeriod.fetchExc(periodId);
            //period = PstPeriod.fetchExc(periodId);
            periodName = payPeriod.getPeriod();
            //update by satrya 2013-02-12
            endDatePeriod = payPeriod.getEndDate();
        } catch (Exception e) {
        }
        PayGeneral payGen = new PayGeneral();
        Employee employee = new Employee();
        PaySlip paySlip = new PaySlip();
        String garis = "";
        int enter = 0;
        int incEnter = 0;
        long sectionTemp = 0;
        int increment = 1;
        PdfWriter pdfWriter = null;
        Document document = new Document(PAGE_SIZE, 30, 30, 50, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(!toEmail){
            try{
              pdfWriter= PdfWriter.getInstance(document, baos);
			  pdfWriter.setPageEvent(new MyPdfPageEventHelper());
            } catch(Exception exc){
                System.out.println(exc);
            }
        }
        for (int ip = 0; ip < listEmpPaySlip.size(); ip++) {
            Vector vlst = (Vector) listEmpPaySlip.get(ip);
            Employee objEmployee = (Employee) vlst.get(0);
            PayEmpLevel objPayEmpLevel = (PayEmpLevel) vlst.get(1);
            PaySlip objPaySlip = (PaySlip) vlst.get(2);

            long employeeId = objEmployee.getOID();
            String salaryLevel = objPayEmpLevel.getLevelCode();
            long paySlipId = objPaySlip.getOID();
            // Update 2015-01-12 by Hendra McHen
            Vector listPaySalary = PstSalaryLevelDetail.listPaySlipNew(PstSalaryLevelDetail.YES_TAKE, salaryLevel, PstPayComponent.TYPE_BENEFIT, paySlipId, keyPeriod, printZeroValue, payGroupId);
            Vector listPayDeduction = PstSalaryLevelDetail.listPaySlipNew(PstSalaryLevelDetail.YES_TAKE, salaryLevel, PstPayComponent.TYPE_DEDUCTION, paySlipId, keyPeriod, printZeroValue, payGroupId);

            // get the data for employee who have pay slip
            Vector empSlip = PstPaySlip.getEmpSlip(periodId, paySlipId);
            String employeeName = "";
            String employeeNum = "";
            int status = 0;
            int paidStatus = 0;
            String division = "";
            String department = "";
            String section = "";
            String position = "";
            double day_present = 0;
            double day_paid_lv = 0;
            double day_absent = 0;
            double day_unpaid_lv = 0;
            double day_late = 0;
            String note = "";
            //update by satrya 2013-01-20
            String bankAcc = "";
            String bankNameX = "";
            Date commDate = new Date();
            //update by satrya 2013-05-06
            double ovIdAdjustment = 0;
            String privNote = "";
            String printPaySlip = "";
            String otDuration = "";
            // double otIdx=0;
            double otPaidBySalary = 0;
            double otPaidByDP = 0;
            double otPaidByTotal = 0;
            String paidBySalary = "";
            //System.out.println("empSlip.size()"+empSlip.size());
           /* if ((empSlip != null) && empSlip.size() > 0) {
             employee = (Employee) empSlip.get(0);
             paySlip = (PaySlip) empSlip.get(1);
             }*/
            if ((empSlip != null) && empSlip.size() > 0) {
                if (sumOvertimeDailyDurIdxSalary != null && sumOvertimeDailyDurIdxSalary.size() > 0 || sumOvertimeDailyDurIdxDP != null && sumOvertimeDailyDurIdxDP.size() > 0) {
                    if (sumOvertimeDailyDurIdxSalary != null && sumOvertimeDailyDurIdxSalary.size() > 0) {
                        for (int idxSal = 0; idxSal < sumOvertimeDailyDurIdxSalary.size(); idxSal++) {
                            OvertimeSummary overtimeDetailSummary = (OvertimeSummary) sumOvertimeDailyDurIdxSalary.get(idxSal);
                            if (overtimeDetailSummary.getEmployeeId() == employeeId) {
                                otPaidBySalary = overtimeDetailSummary.getOtPaidSummarySallary();
                                paidBySalary = Formater.formatWorkDayHoursMinutes((float) overtimeDetailSummary.getOtDuration() / leaveConfig.getHourOneWorkday(), leaveConfig.getHourOneWorkday(), leaveConfig.getFormatLeave());
                                sumOvertimeDailyDurIdxSalary.remove(idxSal);
                                idxSal = idxSal - 1;
                                break;
                            }
                        }
                    }
                    if (sumOvertimeDailyDurIdxDP != null && sumOvertimeDailyDurIdxDP.size() > 0) {
                        for (int idxDP = 0; idxDP < sumOvertimeDailyDurIdxDP.size(); idxDP++) {
                            OvertimeSummary overtimeDetailSummary = (OvertimeSummary) sumOvertimeDailyDurIdxDP.get(idxDP);
                            if (overtimeDetailSummary.getEmployeeId() == employeeId) {
                                otPaidByDP = overtimeDetailSummary.getOtPaidSummaryDp();
                                sumOvertimeDailyDurIdxDP.remove(idxDP);
                                idxDP = idxDP - 1;
                                break;
                            }
                        }
                    }
                    if (sumOvertimeDailyDurIdxTotal != null && sumOvertimeDailyDurIdxTotal.size() > 0) {
                        for (int idxOtTot = 0; idxOtTot < sumOvertimeDailyDurIdxTotal.size(); idxOtTot++) {
                            OvertimeSummary overtimeDetailSummary = (OvertimeSummary) sumOvertimeDailyDurIdxTotal.get(idxOtTot);
                            if (overtimeDetailSummary.getEmployeeId() == employeeId) {
                                otPaidByTotal = overtimeDetailSummary.getOtPaidSummarySallaryAndDP();
                                otDuration = Formater.formatWorkDayHoursMinutes((float) overtimeDetailSummary.getOtDuration(), leaveConfig.getHourOneWorkday(), leaveConfig.getFormatLeave());
                                sumOvertimeDailyDurIdxTotal.remove(idxOtTot);
                                idxOtTot = idxOtTot - 1;
                                break;
                            }
                        }
                    }
                    employee = (Employee) empSlip.get(0);
                    paySlip = (PaySlip) empSlip.get(1);
                } else {
                    employee = (Employee) empSlip.get(0);
                    paySlip = (PaySlip) empSlip.get(1);
                }

            } else {
                employee = new Employee();
                paySlip = new PaySlip();
            }
            employeeName = employee.getFullName();
            employeeNum = employee.getEmployeeNum();
            status = paySlip.getStatus();
            paidStatus = paySlip.getPaidStatus();
            division = paySlip.getDivision();
            department = paySlip.getDepartment();
            section = paySlip.getSection();
            position = paySlip.getPosition();
            commDate = paySlip.getCommencDate();
            day_present = paySlip.getDayPresent();
            day_paid_lv = paySlip.getDayPaidLv();
            day_absent = paySlip.getDayAbsent();
            day_unpaid_lv = paySlip.getDayUnpaidLv();
            day_late = paySlip.getDayLate();
            if (payslipOT == 0) {
                note = paySlip.getNote();
            } else {

                note = paySlip.getNote() + "<br> Overtime Duration: " + otDuration + "<br> Overtime Idx Total: " + otPaidByTotal + " <br> Overtime Idx By Paid Salary: " + otPaidBySalary
                        + " <br> Overtime Idx By Paid Day OFF: " + otPaidByDP + " <br> Overtime Idx Adjusment: " + ovIdAdjustment + (privNote != null && privNote.length() > 0 ? " <br> Overtime Idx Adjusment: " + privNote : "");
            }
            //update by satrya 2013-01-20
            if (getMaxLeftgSiteLength() != 0) {
                for (int x = 0; x < getMaxLeftgSiteLength(); x++) {
                    garis = garis + "_";
                }
            }
            bankAcc = objPayEmpLevel.getBankAccNr();
            bankNameX = objPaySlip.getBankName();
            ovIdAdjustment = objPaySlip.getOvIdxAdj();
            privNote = objPaySlip.getPrivateNote();
			

            if (employee != null && (employee.getCompanyId() != payGen.getOID() || payGen.getOID() == 0) && employee.getCompanyId() != 0) {
                String whereGen = "" + PstPayGeneral.fieldNames[PstPayGeneral.FLD_GEN_ID] + "=" + employee.getCompanyId();
                Vector vectCity = PstPayGeneral.list(0, 0, whereGen, "");
                if (vectCity != null && vectCity.size() > 0) {
                    payGen = (PayGeneral) vectCity.get(0);
                }
            }
			
			long oidPayGenDefault = 0;
			try {
				oidPayGenDefault = Long.valueOf(PstSystemProperty.getValueByName("OID_DEFAULT_PAY_GEN"));
				payGen = PstPayGeneral.fetchExc(oidPayGenDefault);
			} catch (Exception exc){}
            printPaySlip = (payGen.getCity() != null && payGen.getCity().length() > 0 ? payGen.getCity() : "") + "," + (objPaySlip.getPaySlipDate() != null ? Formater.formatDate(objPaySlip.getPaySlipDate(), "dd/MMM/yy") : "");


            /*
             * Description : Get position and period
             * Date : 2015-02-24
             * Author : Hendra Putu
             */
            Date toDay = new Date();
            Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.DATE, 1);
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String formatted = formatDate.format(cal.getTime());

            Position pos = new Position();
			Level lvl = new Level();
            Marital mStatus = new Marital();
            Employee emp = new Employee();
            try {
                emp = PstEmployee.fetchExc(employee.getOID());
                pos = PstPosition.fetchExc(emp.getPositionId());
                mStatus = PstMarital.fetchExc(emp.getTaxMaritalId());
				lvl = PstLevel.fetchExc(emp.getLevelId());
            } catch (Exception e) {
                System.out.print(e);
            }
            if (sectionTemp == 0) {
                sectionTemp = objEmployee.getSectionId();
            } else {
                if (sectionTemp == objEmployee.getSectionId()) {
                    increment++;
                } else {
                    increment = 1;
                    sectionTemp = 0;
                }
            }

            int nilaiPresence = PstPayInput.getPayInputValue(paySlipId, "FRM.FIELD.PRESENCE.ONTIME.TIME", periodeId, employeeId);
            int nilaiWorkday = PstPayInput.getPayInputValue(paySlipId, "FRM.FIELD.EMP.WORK.DAYS", periodeId, employeeId);
            if (incEnter > 15) {
                enter = enter - 1;
                incEnter = 0;
            } else {
                enter = 4;
                incEnter++;
            }
            switch (MODEL_PAYSLIP) {

                case PaySlip.MODEL_UP_DOWN:
                    start = 0;
                    try {
                        if (toEmail) {  // if email then for each payslip is different file pdf
                            document = new Document(PAGE_SIZE, 30, 30, 50, 50);
                            //Document document = new Document(PAGE_SIZE.rotate(), 10, 10, 30, 30);
                            baos = new ByteArrayOutputStream();
							try {
								pdfWriter = PdfWriter.getInstance(document, baos);
								pdfWriter.setPageEvent(new MyPdfPageEventHelper());
							} catch (Exception exc){}
                            attachment = new Vector(); // by kartika 2015-09-12
                            attachmentName = new Vector(); // by kartika 2015-09-12
                            String user = employee.getEmployeeNum()+"."+Formater.formatDate(employee.getBirthDate(), "yyyy-MM-dd");
                            String password= employee.getEmployeeNum()+"."+Formater.formatDate(employee.getBirthDate(), "yyyy-MM-dd");
                            try{
                            pdfWriter.setEncryption( user.getBytes(), password.getBytes(), PdfWriter.AllowPrinting, false); 
                            }catch(Exception exc1){
                                System.out.println("Exception pdfWriter.setEncryption "+exc1);
                            }
                        }
                        // step2.2: creating an instance of the writer

                        if(toEmail || ip==0){
                            // step 3.1: adding some metadata to the document
                            document.addSubject("This is a payroll slip.");
                            document.addSubject("Created by Dimata Hairisma System");
                        }
                        String str = com.dimata.system.entity.PstSystemProperty.getValueByName("PRINT_HEADER");

                        //Header 
                        HeaderFooter header = new HeaderFooter(new Phrase(str, fontHeaderSmall), false);
                        header.setAlignment(Element.ALIGN_LEFT);
                        header.setBorder(Rectangle.BOTTOM);
                        header.setBorderColor(blackColor);
                        document.setHeader(header);

                        HeaderFooter footer = new HeaderFooter(new Phrase("printed : " + Formater.formatDate(new Date(), "dd/MM/yyyy"), fontHeaderSmall), false);
                        footer.setAlignment(Element.ALIGN_RIGHT);
                        footer.setBorder(Rectangle.TOP);
                        footer.setBorderColor(blackColor);
                        document.setFooter(footer);

                        //images
             /* image logo */
                        Image logo = null;

                        String strPath = com.dimata.system.entity.PstSystemProperty.getValueByName("IMGCACHE");

                        try {
                            if (true) {
                                //1.menentukan path gambar dan gambarnya
                                logo = Image.getInstance(strPath + "" + employee.getEmployeeNum() + ".JPEG");
                                logo.scalePercent(95);
                    //logo.setWidthPercentage(40);
                                //posisi atau letak gambar yang diinginkan 
                                logo.setAbsolutePosition(100, 100);
                                //  logo.setAlignment(Image.ALIGN_MIDDLE | Image.ALIGN_TOP);

                            }
                        } catch (Exception exc) {
                            System.out.println(" ERROR @ InvoicePdf - upload image : \n" + exc.toString());
                        }
                        if(toEmail || ip==0){
                            // step 3.4: opening the document
                            document.open();
                        }
                        int[] payHeaderTop = {16, 2, 30, 30, 4, 18};
                        Table payrollTable = new Table(6);
                        payrollTable.setWidth(100);
                        payrollTable.setWidths(payHeaderTop);
                        payrollTable.setBorderColor(new Color(255, 255, 255));
                        payrollTable.setBorderWidth(1);
                        payrollTable.setAlignment(1);
                        payrollTable.setCellpadding(0);
                        payrollTable.setCellspacing(1);

                        String telFax = "Telp. " + payGen.getTel() + " Fax. " + payGen.getFax();
                        //String periodStr = periodName + (detailSlipYN == 1 ? " / " + Formater.formatTimeLocale(endDatePeriod, "dd-MM-yyyy") : "");
						String periodStr = periodName +" ("+ Formater.formatTimeLocale(payPeriod.getStartDate(), "dd-MM-yyyy") + " - "+ Formater.formatTimeLocale(endDatePeriod, "dd-MM-yyyy")+")";
                        String divDepTitle = (detailSlipYN == 1 ? "Div. / Department" : "Department");
                        String divDepStr = (detailSlipYN == 1 ? (" " + division + " / " + department) : department);
                        String commDateStr = Formater.formatTimeLocale(commDate, "dd-MM-yyyy");
                        Date endDt = objPeriod.getEndDate() == null ? new Date() : objPeriod.getEndDate();
                        // end date
                        Calendar gre = Calendar.getInstance();
                        gre.setTime(endDt);
                        int day = gre.getActualMaximum(Calendar.DAY_OF_MONTH);
                        Date dtEnd = (objPeriod.getPaySlipDate() != null) ? objPeriod.getPaySlipDate() : (new Date(endDt.getYear(), endDt.getMonth(), day));
                        String privNoteStr = (privNote != null && privNote.length() > 0 && !privNote.equalsIgnoreCase("null")) ? privNote : "";

//|     1                     |2|           3                          |  4                         |5|      6           |                                     
 String[][] payDataEnglish ={
  {"No."                     ,":", ""+(ip+1)                           ,""                          ,"",""                 },                    
  {"Section"                 ,":", ""+section                          ,"Period"                    ,":",""+periodName     },  
  {"Employee No."            ,":", ""+employeeNum+ "/" + employeeName  ,"Status"                    ,":",""+TaxCalculator.STATUS_EMPLOYEE_CODE[mStatus.getMaritalStatusTax()]},
  {"Position"                ,":", ""+pos.getPosition()                ,"Grade Allowance"           ,":",""+lvl.getLevel()    }, 
  {" "                       ,"",  ""                                   ,""                          ,"",""                 }, 
  {" "                       ,"",  ""                                   ,""                          ,"",""                 }, 
  {""+payGen.getCompanyName(),"",  ""                                  ,""                          ,"",""                 }, 
  {""+payGen.getCompAddress(),"",  ""                                  ,""                          ,"",""                 },  
  {""+payGen.getCity()       ,"",  ""                                  ,""                          ,"",""                 }, 
  {""+telFax                 ,"",  ""                                  ,""                          ,"",""                 }, 
  {"Period"                  ,":", ""+periodStr                        ,""                          ,"",""                 }, 
  {"Empl. Name"              ,":", ""+employeeName                     ,""                          ,"",""                 }, 
  {"Empl. Number"            ,":", ""+employeeNum                      ,""                          ,"",""                 }, 
  {""+divDepTitle            ,":", ""+divDepStr                        ,""                          ,"",""                 }, 
  {"Comm. date"              ,":", ""+commDateStr                      ,""                          ,"",""                 }, 
  {"Bank Acc."               ,":", ""+bankAcc                          ,""                          ,"",""                 }, 
  {"Bank Name"               ,":", ""+bankNameX                        ,""                          ,"",""                 }, 
  {"Note"                    ,":" ,""+note                             ,""                          ,"",""                 }, 
  {"Priv. Note "             ,":" ,""+privNoteStr                      ,""                          ,"",""                 }, 
  {"Printed Date"            ,":", ""+formatted                        ,""                          ,"",""                 }, 
  {"Presence"                ,":", ""+nilaiPresence                    ,""                          ,"",""                 }, 
  {"WorkDays"                ,":", ""+nilaiWorkday                     ,""                          ,"",""                 }    
         
 };
 
 String[][] payDataIndonesia ={
  {"No."                     ,":", ""+(ip+1)                           ,""                          ,"",""                 },                    
  {"Seksi  "                 ,":", ""+section                          ,"Periode"                  ,":",""+periodName     },  
  {"Nomor karyawan"          ,":", ""+employeeNum+ "/" + employeeName  ,"Status"                    ,":",""+TaxCalculator.STATUS_EMPLOYEE_CODE[mStatus.getMaritalStatusTax()]},
  {"Jabatan"                ,":", ""+pos.getPosition()                ,""                          ,"",""                 }, 
  {" "                       ,"",  ""                                   ,""                          ,"",""                 }, 
  {" "                       ,"",  ""                                   ,""                          ,"",""                 }, 
  {""+payGen.getCompanyName(),"",  ""                                  ,""                          ,"",""                 }, 
  {""+payGen.getCompAddress(),"",  ""                                  ,""                          ,"",""                 },  
  {""+payGen.getCity()       ,"",  ""                                  ,""                          ,"",""                 }, 
  {""+telFax                 ,"",  ""                                  ,""                          ,"",""                 }, 
  {"Periode"                 ,":", ""+periodStr                        ,""                          ,"",""                 }, 
  {"Nama Karyawan"           ,":", ""+employeeName                     ,""                          ,"",""                 }, 
  {"Nomor Karyawan"          ,":", ""+employeeNum                      ,""                          ,"",""                 }, 
  {""+divDepTitle            ,":", ""+divDepStr                        ,""                          ,"",""                 }, 
  {"Tgl. mulai kerja"        ,":", ""+commDateStr                      ,""                          ,"",""                 }, 
  {"No. Rek. Bank"           ,":", ""+bankAcc                          ,""                          ,"",""                 }, 
  {"Nama Bank"               ,":", ""+bankNameX                        ,""                          ,"",""                 }, 
  {"Catatan"                 ,":" ,""+note                             ,""                          ,"",""                 }, 
  {"Catatan pribadi"         ,":" ,""+privNoteStr                      ,""                          ,"",""                 }, 
  {"Tgl Cetak"               ,":", ""+formatted                        ,""                          ,"",""                 } 
 };
 
 String[][] payData = SESS_LANGUAGE ==com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN ? payDataEnglish : payDataIndonesia;

                        int[][] colSpans = {
                            /* 1*/{0, 0, 4, 0, 0, 0},
                            /* 2*/ {0, 0, 0, 0, 0, 0},
                            /* 3*/ {0, 0, 0, 0, 0, 0},
                            /* 4*/ {0, 0, 0, 0, 0, 0},
                            /* 5*/ {6, 0, 0, 0, 0, 0},
                            /* 6*/ {6, 0, 0, 0, 0, 0},
                            /* 7*/ {3, 0, 0, 0, 0, 0},
                            /* 8*/ {3, 0, 0, 0, 0, 0},
                            /* 9*/ {3, 0, 0, 0, 0, 0},
                            /*10*/ {3, 0, 0, 0, 0, 0},
                            /*11*/ {0, 0, 0, 0, 0, 0},
                            /*12*/ {0, 0, 0, 0, 0, 0},
                            /*13*/ {0, 0, 0, 0, 0, 0},
                            /*14*/ {0, 0, 0, 0, 0, 0},
                            /*15*/ {0, 0, 0, 0, 0, 0},
                            /*16*/ {0, 0, 0, 0, 0, 0},
                            /*17*/ {0, 0, 0, 0, 0, 0},
                            /*18*/ {0, 0, 0, 0, 0, 0},
                            /*19*/ {3, 0, 0, 0, 0, 0},
                            /*20*/ {0, 0, 0, 0, 0, 0},
                            /*21*/ {0, 0, 0, 0, 0, 0},
                            /*22*/ {0, 0, 0, 0, 0, 0},};
                        int roxForSignature =0;
                        for (int rowIdx = 0; rowIdx < payData.length; rowIdx++) {
                            String[] rowData = payData[rowIdx];
                            for (int colIdx = 0; colIdx < rowData.length; colIdx++) {
                                String strCell = rowData[colIdx];
                                if (strCell != null && strCell.length() > 0) {
                                    Cell tCell = new Cell(new Chunk(strCell, tableContent));
                                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    tCell.setBorderColor(new Color(255, 255, 255));
                                    if (colSpans[rowIdx][colIdx] > 0) {
                                        tCell.setColspan(colSpans[rowIdx][colIdx]);
                                    }
                                    payrollTable.addCell(tCell, rowIdx, colIdx);
                                }
                            }
                            roxForSignature = rowIdx;
                        }

                        drawListBenefit(payrollTable, startRowSlipComp, startColSlipComp, listPaySalary, employeeId, periodId, paySlipId, paidBySalary, codeUpahLembur, currencyType, tableContent);
                        drawListDeduction(payrollTable, startRowSlipComp + (listPaySalary != null ? listPaySalary.size() : 0), startColSlipComp, listPayDeduction, employeeId, periodId, paySlipId, codeTax_count, currencyType, tableContent);

                        // cari total benefit
                        double totalBenefit = PstSalaryLevelDetail.getSumBenefitDoubV2(PstSalaryLevelDetail.YES_TAKE, salaryLevel, PstPayComponent.TYPE_BENEFIT, paySlipId, keyPeriod, payGroupId);
                        //System.out.println("totalBenefit..............."+totalBenefit);
                        double totalDeduction = PstSalaryLevelDetail.getSumBenefitDoubV2(PstSalaryLevelDetail.YES_TAKE, salaryLevel, PstPayComponent.TYPE_DEDUCTION, paySlipId, keyPeriod, payGroupId);

                        // kondisi ini digunakan jika ada  kasus yang nilai laporan daftar gajinya berbeda dengan nilai yang tertera di payslip
                        double totTakHomePay = totalBenefit - totalDeduction;

                        int rowTotal = 8 + getStartRowSlipComp() + (listPaySalary != null ? listPaySalary.size() : 0)
                                + (listPayDeduction != null ? listPayDeduction.size() : 0) - (codeTax_count != null && codeTax_count.length() > 0 ? 2 : 0);

                        try {
                            Cell tCell = new Cell(new Chunk(TOTAL_TAKE_HOME_PAY, tableContent));
                            tCell.setBackgroundColor(Color.LIGHT_GRAY);
                            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tCell.setBorderColor(new Color(255, 255, 255));
                            payrollTable.addCell(tCell, rowTotal, startColSlipComp);
                        } catch (Exception exc) {
                            System.out.println(exc);
                        }
                        try {
                            Cell tCell = new Cell(new Chunk(": " + mataUang, tableContent));
                            tCell.setBackgroundColor(Color.LIGHT_GRAY);
                            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tCell.setBorderColor(new Color(255, 255, 255));
                            payrollTable.addCell(tCell, rowTotal, startColSlipComp + 1);
                        } catch (Exception exc) {
                            System.out.println(exc);
                        }

                        try {
                            Cell tCell = new Cell(new Chunk(Formater.formatNumberVer1(totTakHomePay, frmCurrency), tableContent));
                            tCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            tCell.setBackgroundColor(Color.LIGHT_GRAY);
                            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            tCell.setBorderColor(new Color(255, 255, 255));
                            payrollTable.addCell(tCell, rowTotal++, startColSlipComp + 2);
                        } catch (Exception exc) {
                            System.out.println(exc);
                        }

                        if (SignatureOnOff > 0) {
                            drawListSignature(payGen, payrollTable, getStartRowSlipComp() + enter + 4 + (listPaySalary != null ? listPaySalary.size() : 0) + (listPayDeduction != null ? listPayDeduction.size() : 0), 1, 2, objEmployee, tableContent);
                        }
                        document.add(payrollTable);
                        
                        String strEmail=SESS_LANGUAGE == com.dimata.util.lang.I_Language.LANGUAGE_FOREIGN ? ( "Dear " + employee.getFullName() + ", \r\n" 
                                        +" Please find out your payslip for " + payPeriod.getPeriod() + "\r\n"
                                        +" Your password to open the payslip is : You Payroll number.year-month-date of your birtdate. As example: 0234.1990-02-18 ."
                                        +" Regards, \r\n Your Paymaster" ) :
                                       ( "Ysh. " + employee.getFullName() + ", \r\n" 
                                        +" Salam hangat dari team SDM. Silakan buka slip gaji Anda untuk " + payPeriod.getPeriod() + "\r\n"
                                        +" Kata kunci untuk membuka slip gaji Anda adalah  : nomor karyawan.tahun-bulan-tanggal dari tanggal lahir Anda. Contoh: 0234.1990-02-18 ."
                                        +" Hormat kami, \r\n Team SDM");
                        emailLogs.add(employee.getEmployeeNum() + "|" + employee.getFullName() + " | " + divDepStr);
                        
                        if (toEmail && employee != null && employee.getEmailAddress() != null && employee.getEmailAddress().length() > 5) {
                            document.close();
                            //response.setContentLength(baos.size());
                            //baos.writeTo(out);
                        
                            byte[] bytes = baos.toByteArray();
                            //construct the pdf body part
                            DataSource dataSource = new ByteArrayDataSource(bytes, "application/pdf");
                            attachment.add(dataSource); // add as attachement
                            attachmentName.add("payslip_" + payPeriod.getPeriod() + "_" + employee.getFullName() + ".pdf");
                            Vector<String> listRec = new Vector();
                            listRec.add("" + employee.getEmailAddress());
                            email.sendEmail(listRec, null, null, "Payslip " + payPeriod.getPeriod() + " of " + employee.getFullName(), strEmail, attachment, attachmentName);
                        }

                    } catch (Exception exc) {
                        System.out.println(exc);
                    }
                    break;

                default:
                    out.println("No settting for default format ");
            }
            try{
               if (toEmail==false){  
                  document.newPage(); 
               }
            }catch(Exception exc){
                throw new ServletException(exc.toString());
            }
        }
        if (toEmail) {
            try {
                document = new Document(PAGE_SIZE, 30, 30, 50, 50);
                // step 3.1: adding some metadata to the document
                document.addSubject("This is a payroll slip.");
                document.addSubject("Created by Dimata Hairisma System");

                String str = com.dimata.system.entity.PstSystemProperty.getValueByName("PRINT_HEADER");

                //Header 
                HeaderFooter header = new HeaderFooter(new Phrase(str, fontHeaderSmall), false);
                header.setAlignment(Element.ALIGN_LEFT);
                header.setBorder(Rectangle.BOTTOM);
                header.setBorderColor(blackColor);
                document.setHeader(header);

                HeaderFooter footer = new HeaderFooter(new Phrase("email : " + Formater.formatDate(new Date(), "dd/MM/yyyy"), fontHeaderSmall), false);
                footer.setAlignment(Element.ALIGN_RIGHT);
                footer.setBorder(Rectangle.TOP);
                footer.setBorderColor(blackColor);
                document.setFooter(footer);
                
                //document.newPage();
                int[] payHeaderTop = {5, 95};
                baos = new ByteArrayOutputStream();
                pdfWriter = PdfWriter.getInstance(document, baos);
				pdfWriter.setPageEvent(new MyPdfPageEventHelper());
                document.open();
                Table payrollTable = new Table(2);
                payrollTable.setWidth(100);
                payrollTable.setWidths(payHeaderTop);
                payrollTable.setBorderColor(new Color(255, 255, 255));
                payrollTable.setBorderWidth(1);
                payrollTable.setAlignment(1);
                payrollTable.setCellpadding(0);
                payrollTable.setCellspacing(1);
                
                if (emailLogs != null && emailLogs.size() > 0) {

                    for (int idx = 0; idx < emailLogs.size(); idx++) {
                        Cell tCell = new Cell(new Chunk("" + (idx==0?" ":(""+idx)), tableContent));
                        tCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                        tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tCell.setBorderColor(new Color(255, 255, 255));
                        payrollTable.addCell(tCell, idx, 0);

                        tCell = new Cell(new Chunk("" + emailLogs.get(idx)));
                        tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        tCell.setBorderColor(new Color(255, 255, 255));
                        payrollTable.addCell(tCell, idx, 1);
                    }
                }
                document.add(payrollTable);
            } catch (Exception exc) {
                System.out.println(exc);
            }

        }
         try{
             document.close();
         }catch(Exception exc){
             System.out.println(exc);
         }
         try{
         response.setContentLength(baos.size());
         baos.writeTo(out);
         out.flush();
         }catch(Exception exc){
             System.out.println(exc);
         }
    }


    public Table drawListBenefit(Table payrollTable, int startRow, int startCol, Vector objectClass, long employeeId, long periodId, long paySlipId, String paidBySalary, String codeUpahLembur, CurrencyType currencyType, Font tableContent) {
        if(objectClass==null || objectClass.size()<1){
            return null;
        }
        
        String output = "";
        String garis = "";
        int inc = 0;
        double totalBenefit = 0;
        //String frmCurrency = "#,###";
        //update by satrya 2014-06-01
        String frmCurrency = "#,###";
        String mataUang = "Rp";
        if (currencyType != null && currencyType.getCode() != null && currencyType.getFormatCurrency() != null) {
            frmCurrency = currencyType.getFormatCurrency();
            mataUang = currencyType.getCode() == null || currencyType.getCode().length() == 0 ? "Rp" : currencyType.getCode() + ".";
        }
        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            Vector vectToken = new Vector(1, 1);
            PayComponent payComponent = (PayComponent) temp.get(0);
            SalaryLevelDetail salaryLevelDetail = (SalaryLevelDetail) temp.get(1);
            PaySlipComp paySlipComp = (PaySlipComp) temp.get(2);
            // ambil value dari komponent-komponent
            if (codeUpahLembur != null && codeUpahLembur.length() > 0 && codeUpahLembur.equalsIgnoreCase(payComponent.getCompCode())) {
                //jika componennya mengandung UL 
                try {
                    Cell tCell = new Cell(new Chunk(payComponent.getCompName() + " (" + paidBySalary + ")", tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow + i, startCol);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                try {
                    Cell tCell = new Cell(new Chunk(": "+mataUang, tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow + i, startCol+1);
                } catch (Exception exc) {
                    System.out.println(exc);
                }

                try {
                    Cell tCell = new Cell(new Chunk(Formater.formatNumberVer1(paySlipComp.getCompValue(), frmCurrency), tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow + i, startCol + 2);
                } catch (Exception exc) {
                    System.out.println(exc);
                }

                if (payComponent.getUsedInForml() != 0) {
                    totalBenefit = totalBenefit + paySlipComp.getCompValue();
                }

            } else {
               try {
                    Cell tCell = new Cell(new Chunk(payComponent.getCompName(), tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow + i, startCol);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                try {
                    Cell tCell = new Cell(new Chunk(": "+mataUang, tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow +i, startCol+1);
                } catch (Exception exc) {
                    System.out.println(exc);
                }

                try {
                    Cell tCell = new Cell(new Chunk(Formater.formatNumberVer1(paySlipComp.getCompValue(), frmCurrency), tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow + i, startCol + 2);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                if (payComponent.getUsedInForml() != 0) {
                    totalBenefit = totalBenefit + paySlipComp.getCompValue();
                }
            }
            inc++;
        }


        try {
                    Cell tCell = new Cell(new Chunk(TOTAL_BENEFIT, tableContent));
                    tCell.setBackgroundColor(Color.LIGHT_GRAY);
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow + objectClass.size(), startCol);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                try {
                    Cell tCell = new Cell(new Chunk(": "+mataUang, tableContent));
                    tCell.setBackgroundColor(Color.LIGHT_GRAY);
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow +objectClass.size(), startCol+1);
                } catch (Exception exc) {
                    System.out.println(exc);
                }

                try {
                    Cell tCell = new Cell(new Chunk(Formater.formatNumberVer1(totalBenefit, frmCurrency), tableContent));
                    tCell.setBackgroundColor(Color.LIGHT_GRAY);
                    tCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, startRow + objectClass.size(), startCol + 2);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
        return payrollTable;
    }

    public String drawListDeduction(Table payrollTable, int startRow, int startCol, Vector objectClass, long employeeId, long periodId, long paySlipId, String codeTax_count, CurrencyType currencyType, Font tableContent) {
        String output = "";
        int totalBenefit = 0;
        String garis = "";
        double totalDeduction = 0;
        //String frmCurrency = "#,###";
        String frmCurrency = "#,###";
        String mataUang = "Rp";
        if (currencyType != null && currencyType.getCode() != null && currencyType.getFormatCurrency() != null) {
            frmCurrency = currencyType.getFormatCurrency();
            mataUang = currencyType.getCode() == null || currencyType.getCode().length() == 0 ? "Rp." : (currencyType.getCode() + ".");
        }
        int rows = startRow + 1;

        for (int i = 0; i < objectClass.size(); i++) {
            Vector temp = (Vector) objectClass.get(i);
            Vector vectToken = new Vector(1, 1);
            PayComponent payComponent = (PayComponent) temp.get(0);
            SalaryLevelDetail salaryLevelDetail = (SalaryLevelDetail) temp.get(1);
            PaySlipComp paySlipComp = (PaySlipComp) temp.get(2);
            // ambil value dari komponent-komponent

            if (codeTax_count != null && codeTax_count.length() > 0 && codeTax_count.equalsIgnoreCase(payComponent.getCompCode())) {
                //jika componennya mengandung pph yg di hitung konsultan maka tidak di tampilkan
                rows = rows;
            } else {
                rows = rows + 1;
                try {
                    Cell tCell = new Cell(new Chunk(payComponent.getCompName(), tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, rows, startCol);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                try {
                    Cell tCell = new Cell(new Chunk(": "+mataUang, tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, rows, startCol+1);
                } catch (Exception exc) {
                    System.out.println(exc);
                }

                try {
                    Cell tCell = new Cell(new Chunk(Formater.formatNumberVer1(paySlipComp.getCompValue(), frmCurrency), tableContent));
                    tCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    tCell.setBorderColor(new Color(255, 255, 255));
                    payrollTable.addCell(tCell, rows, startCol + 2);
                } catch (Exception exc) {
                    System.out.println(exc);
                }
                totalDeduction = totalDeduction + paySlipComp.getCompValue();
                
            }

        }
        try {
            rows = rows + 1;
            Cell tCell = new Cell(new Chunk(TOTAL_DEDUCTION, tableContent));
            tCell.setBackgroundColor(Color.LIGHT_GRAY);
            tCell.setBorderColor(new Color(255, 255, 255));
            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            payrollTable.addCell(tCell, rows, startCol);
        } catch (Exception exc) {
            System.out.println(exc);
        }
        try {
            Cell tCell = new Cell(new Chunk(": " + mataUang, tableContent));
            tCell.setBackgroundColor(Color.LIGHT_GRAY);
            tCell.setBorderColor(new Color(255, 255, 255));
            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            payrollTable.addCell(tCell, rows, startCol + 1);
        } catch (Exception exc) {
            System.out.println(exc);
        }

        try {
            Cell tCell = new Cell(new Chunk(Formater.formatNumberVer1(totalDeduction, frmCurrency), tableContent));
            tCell.setBackgroundColor(Color.LIGHT_GRAY);
            tCell.setBorderColor(new Color(255, 255, 255));
            tCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            payrollTable.addCell(tCell, rows, startCol + 2);
        } catch (Exception exc) {
            System.out.println(exc);
        }
        return output;
    }

    public String drawListSignature(PayGeneral company, Table payrollTable, int startRow, int startCol, int colSpan, Employee objEmployee, Font tableContent) {
        String output = "";

        int rows = startRow + 8;
        Date thisDate = new Date();
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        String DateNew = formatDate.format(thisDate);
        String cityAddress="";
        /*PayGeneral company = new PayGeneral();
        try {
            company = PstPayGeneral.fetchExc(objEmployee.getCompanyId());
            cityAddress= company.getCity();
        } catch (Exception e) {

        }
        Vector empWithLevel = PstEmployee.listEmployeeByLevel(objEmployee);
        Vector emplevel = (Vector) empWithLevel.get(0);

        Employee employee = new Employee();
        Level level = new Level();
        employee = (Employee) emplevel.get(0);
        level = (Level) emplevel.get(1);
        */
        String garis = "_";

        try {
            Cell tCell = new Cell(new Chunk(""+company.getCity() +", " + DateNew, tableContent));
            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tCell.setBorderColor(new Color(255, 255, 255));
            if (colSpan > 0) {
                tCell.setColspan(colSpan);
            }
            payrollTable.addCell(tCell, startRow++, startCol);

            tCell = new Cell(new Chunk(company.getCompanyName()/* +", "+level.getLevel()*/ , tableContent));
            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tCell.setBorderColor(new Color(255, 255, 255));
            if (colSpan > 0) {
                tCell.setColspan(colSpan);
            }
            payrollTable.addCell(tCell, startRow++, startCol);

            for (int spc = 0; spc < 5; spc++) {
                tCell = new Cell(new Chunk(" ", tableContent));
                tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tCell.setBorderColor(new Color(255, 255, 255));
                if (colSpan > 0) {
                    tCell.setColspan(colSpan);
                }
                payrollTable.addCell(tCell, startRow++, startCol);
            }
            tCell = new Cell(new Chunk(objEmployee.getFullName(), tableContent));
            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tCell.setBorderColor(new Color(128, 128, 128));
            tCell.setBorder(Rectangle.BOTTOM);
            if (colSpan > 0) {
                tCell.setColspan(colSpan);
            }
            payrollTable.addCell(tCell, startRow++, startCol);
          
            /*for (int i = 0; i < employee.getFullName().length(); i++) {
            garis = garis + "_";
            }   
            tCell = new Cell(new Chunk(garis, tableContent));
            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tCell.setBorderColor(new Color(255, 255, 255));
            if (colSpan > 0) {
                tCell.setColspan(colSpan);
            }
            payrollTable.addCell(tCell, startRow++, startCol);*/

            tCell = new Cell(new Chunk(objEmployee.getEmployeeNum(), tableContent));
            tCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            tCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tCell.setBorderColor(new Color(255, 255, 255));
            if (colSpan > 0) {
                tCell.setColspan(colSpan);
            }
            payrollTable.addCell(tCell, startRow++, startCol);
        } catch (Exception exc) {
            System.out.println(exc);
         }
        return output;
    }
	
	class MyPdfPageEventHelper extends PdfPageEventHelper {
 
		@Override
		public void onEndPage(PdfWriter pdfWriter, Document document) {

			   System.out.println("Creating Waterwark Image in PDF");

			   try {

					  //Use this method if you want to get image from your Local system
					  //Image waterMarkImage = Image.getInstance("E:/tiger.jpg");
					  String root = PstSystemProperty.getValueByName("HARISMA_URL");
					  String urlOfWaterMarKImage = root+"/imgcompany/wm2.jpg"   ;
						Image image = Image.getInstance(urlOfWaterMarKImage);
					  //Get waterMarkImage from some URL
					  Image waterMarkImage = Image.getInstance(new URL(urlOfWaterMarKImage));
					  waterMarkImage.setAbsolutePosition(100, 250);
					  //Get width and height of whole page
					  float pdfPageWidth = image.plainWidth();
					  float pdfPageHeight = image.plainHeight();

					  //Set waterMarkImage on whole page
					  PdfContentByte canvas = pdfWriter.getDirectContentUnder();
					  
					  canvas.addImage(waterMarkImage,
								   pdfPageWidth, 0, 0, pdfPageHeight, 100, 500);

			   }catch(Exception e){
					  System.out.println(e.toString());
			   }
		}
	}

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
