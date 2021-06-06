/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.utility.service.leavedp;

import com.dimata.harisma.entity.attendance.AlStockManagement;
import com.dimata.harisma.entity.attendance.PstAlStockManagement;
import com.dimata.harisma.entity.employee.Employee;
import com.dimata.harisma.entity.employee.PstEmployee;
import com.dimata.harisma.entity.leave.I_Leave;
import com.dimata.harisma.entity.leave.LeaveAlClosing;
import com.dimata.harisma.entity.leave.LeaveAlClosingList;
import com.dimata.harisma.entity.leave.LeaveAlClosingNoStockList;
import com.dimata.harisma.entity.leave.LeaveLlClosingList;
import com.dimata.harisma.entity.leave.LlClosingSelected;
import com.dimata.harisma.entity.masterdata.EmpCategory;
import com.dimata.harisma.entity.masterdata.PstEmpCategory;
import com.dimata.harisma.session.leave.LeaveConfigHR;
import com.dimata.harisma.session.leave.SessLeaveClosing;
import com.dimata.system.entity.system.PstSystemProperty;
import com.dimata.util.Formater;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Gunadi
 */
public class AutomaticAlClosing implements Runnable {
    
    int i = 0;
    //update by satrya 2013-02-25
    private Date startDate=null;
     private boolean running = false;
     private long sleepMs = 86400000;
    public AutomaticAlClosing() {
    }
    
    public void run() 
    {
        try {

            this.setRunning(true);

            I_Leave leaveConfig = null;

            try {
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception e) {
                System.out.println("Exception : " + e.getMessage());
            }

            Vector vectValExtend = leaveConfig != null ? leaveConfig.getAlValExtend() : new Vector();
            Vector vectListExtend = leaveConfig != null ? leaveConfig.getAlKeyExtend() : new Vector();

            int i = 0;
            while (this.running) {
                i++;
                
                alClosing(leaveConfig);
                
                try {
                    Thread.sleep(this.getSleepMs());
                } catch (Exception exc) {
                    System.out.println(exc);
                } finally {
                }
            }
            this.running = false;


        } catch (Exception exc) {
            System.out.println(">>> Exception on AutomaticAlClosing service :((");
        }
    }
    
    public void alClosing(I_Leave leaveConfig) {
        Date today = new Date();
        Vector resultSearch = new Vector();
        Vector result = new Vector();
        Vector resultClose = new Vector();

        if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_ANUAL || leaveConfig.getALEntitleBy() == I_Leave.AL_ENTITLE_BY_PERIOD) { /* entitle by periode */
            resultSearch = SessLeaveClosing.getEmployeeActiveALLByPeriod();
            result = SessLeaveClosing.getEmployeeNotYetHaveStockAllByPeriod();
        } else if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING) { /* entitle by commencing */
            resultSearch = SessLeaveClosing.getEmployeeActiveALL();
            result = SessLeaveClosing.getEmployeeNotYetHaveStockAll();
        } else if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING_PERIODE) { /* entitle by commencing */
            resultSearch = SessLeaveClosing.getEmployeeActiveALLByPeriod();
            result = SessLeaveClosing.getEmployeeNotYetHaveStockAllByPeriod();
        } else {/* jika tidak keduanya maka digunakan  default entitle by commencing */
            resultSearch = SessLeaveClosing.getEmployeeActiveALL();
            result = SessLeaveClosing.getEmployeeNotYetHaveStockAll();
        }

        long NotfirstAdjusment = 0;
        try {
            NotfirstAdjusment = Long.parseLong(PstSystemProperty.getValueByName("NOT_FIRST_ADJUSTMENT"));
        } catch (Exception e) {
        }

        if (resultSearch != null && resultSearch.size() > 0) {
            for (int x = 0; x < resultSearch.size(); x++) {
                LeaveAlClosingList leaveAlClosingList = (LeaveAlClosingList) resultSearch.get(x);
                if (leaveAlClosingList.getAlStockManagementId() != 0 && (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING)) {
                    Date comm = (Date) leaveAlClosingList.getCommancingDate().clone();
                    Date entitleDateX = (Date) leaveAlClosingList.getEntitledDate().clone();
                    Date newda = new Date();

                    if ((comm.getDate() != entitleDateX.getDate()) || (comm.getMonth() != entitleDateX.getMonth())) {
                        try {
                            comm.setYear(newda.getYear() - 1);
                            AlStockManagement alStockManagement2 = PstAlStockManagement.fetchExc(leaveAlClosingList.getAlStockManagementId());
                            alStockManagement2.setEntitleDate(comm);
                            long nn = PstAlStockManagement.updateExc(alStockManagement2);
                            leaveAlClosingList.setEntitledDate(comm);
                        } catch (Exception e) {
                        }
                    }
                }

                Employee employee = new Employee();
                EmpCategory empCategory = new EmpCategory();
                try {
                    employee = PstEmployee.fetchExc(leaveAlClosingList.getEmpId());
                } catch (Exception e) {
                }


                try {
                    empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
                } catch (Exception e) {
                }
                //jumlah data
                int countData = SessLeaveClosing.getCountStockManagement(leaveAlClosingList.getEmpId());

                Vector listStock = new Vector();

                listStock = SessLeaveClosing.getAlStockManagementAktif(leaveAlClosingList.getEmpId());

                AlStockManagement objAl = listStock != null && listStock.size() > 0 ? (AlStockManagement) listStock.get(0) : new AlStockManagement();

                int resultStatus = SessLeaveClosing.statusAlStockManagement(leaveAlClosingList.getEmpId(), leaveAlClosingList.getAlStockManagementId());

                String comcDate = "";

                try {
                    comcDate = Formater.formatDate(leaveAlClosingList.getCommancingDate(), "yyyy-MM-dd");
                } catch (Exception e) {
                    System.out.println("exception Parsing: " + e.toString());
                    comcDate = "";
                }

                String entDate = leaveAlClosingList.getEntitledDate() != null ? Formater.formatDate(leaveAlClosingList.getEntitledDate(), "yyyy-MM-dd") : "-";
                Date commencingDateClone = (Date) leaveAlClosingList.getCommancingDate().clone();
                Date selectedDateCom = new Date((commencingDateClone.getYear() + 1), commencingDateClone.getMonth(), (commencingDateClone.getDate()));

                float residu = leaveAlClosingList.getPrevBalance() + leaveAlClosingList.getEntitled() /* update by satrya 2013-10-28 leaveAlClosingList.getAlQty()*/ - leaveAlClosingList.getQtyUsed();//leaveAlClosingList.getEntitled() - leaveAlClosingList.getQtyUsed();
                boolean statusApplicationNotClose = false;
                statusApplicationNotClose = SessLeaveClosing.getApplicationByStockManagement(leaveAlClosingList.getEmpId(), leaveAlClosingList.getAlStockManagementId());
                Date nextEntitle = leaveAlClosingList.getEntitledDate() != null ? ((Date) leaveAlClosingList.getEntitledDate().clone()) : leaveAlClosingList.getCommancingDate();

                boolean okNextEntitle = false;
                if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_ANUAL || leaveConfig.getALEntitleBy() == I_Leave.AL_ENTITLE_BY_PERIOD) { /* entitle by periode */
                    if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_ANUAL) {
                        nextEntitle.setYear(nextEntitle.getYear() + 1);
                        nextEntitle.setMonth(0);
                        nextEntitle.setDate(1);
                        okNextEntitle = nextEntitle.before(today);
                    } else {
                        nextEntitle.setYear(nextEntitle.getYear() + 1);
                        nextEntitle.setMonth(0);
                        nextEntitle.setDate(1);
                        okNextEntitle = nextEntitle.before(today);
                    }
                } else if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING_PERIODE) { /* entitle by periode */
                    if (NotfirstAdjusment == 1) {
                        if (leaveAlClosingList.getEntitledDate().getTime() >= selectedDateCom.getTime()) {
                            nextEntitle.setYear(nextEntitle.getYear() + 1);
                            nextEntitle.setMonth(0);
                            nextEntitle.setDate(1);
                            okNextEntitle = nextEntitle.before(today);
                            //cek pertama atau tidak ?
                            boolean pertamaClosing = PstAlStockManagement.CekPertamaClosing("" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + leaveAlClosingList.getEmpId());
                            if (pertamaClosing) {
                                okNextEntitle = true;
                            }
                        } else {
                            nextEntitle.setYear(nextEntitle.getYear() + 1);
                            okNextEntitle = nextEntitle.before(today);
                        }
                    } else {
                        if (leaveAlClosingList.getEntitledDate().before(today) && leaveAlClosingList.getEntitledDate().getTime() >= selectedDateCom.getTime()) {
                            nextEntitle.setYear(nextEntitle.getYear() + 1);
                            nextEntitle.setMonth(0);
                            nextEntitle.setDate(1);
                            okNextEntitle = true;
                        } else {
                            nextEntitle.setYear(nextEntitle.getYear() + 1);
                            okNextEntitle = nextEntitle.before(today);
                        }
                    }
                } else {
                    //sementara untuk borobudur
                    Date nDate = new Date();

                    if ((commencingDateClone.getDate() != nextEntitle.getDate()) || (commencingDateClone.getMonth() != nextEntitle.getMonth())) {
                        nDate.setDate(commencingDateClone.getDate());
                        nDate.setMonth(commencingDateClone.getMonth());

                        //residu = 12;
                        resultStatus = 2;
                        nextEntitle = nDate;
                        okNextEntitle = nextEntitle.before(today);

                    } else {
                        nextEntitle.setYear(nextEntitle.getYear() + 1);
                        okNextEntitle = nextEntitle.before(today);
                    }
                    //end sementara                                                                                                          
                }
                if (statusApplicationNotClose == true || !okNextEntitle) {
                    continue;
                } else {
                    if (countData > 1) {
                        if (resultStatus == 4) {
                            continue;
                        } else if (resultStatus == 5) {
                            LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                    leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                            if (leaveAlClosing != null) {
                                resultClose.add(leaveAlClosing);
                            }
                        } else if (resultStatus == 6) {
                            LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                    leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                            if (leaveAlClosing != null) {
                                resultClose.add(leaveAlClosing);
                            }
                        } else if (resultStatus == 3) {
                            continue;
                        }
                    } else {
                        if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_ANUAL || leaveConfig.getALEntitleBy() == I_Leave.AL_ENTITLE_BY_PERIOD) { /* entitle by periode */
                            if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                        leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                if (leaveAlClosing != null) {
                                    resultClose.add(leaveAlClosing);
                                }
                            }
                        } else if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING) {
                            if (resultStatus == 1) {
                                continue;
                            } else if (resultStatus == 2) {
                                if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                    LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                            leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                    if (leaveAlClosing != null) {
                                        resultClose.add(leaveAlClosing);
                                    }
                                } else {
                                    continue;
                                }
                            } else if (resultStatus == 6) {
                                if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                    LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                            leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                    if (leaveAlClosing != null) {
                                        resultClose.add(leaveAlClosing);
                                    }
                                } else {
                                    continue;
                                }
                            } else if (resultStatus == 3) {
                                if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                    LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                            leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                    if (leaveAlClosing != null) {
                                        resultClose.add(leaveAlClosing);
                                    }
                                } else {
                                    continue;
                                }
                            }
                        } else if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING_PERIODE) {
                            if (leaveAlClosingList.getEntitledDate().getTime() >= selectedDateCom.getTime()) {
                                Calendar startDateR = Calendar.getInstance();
                                Calendar endDateR = Calendar.getInstance();
                                Date newDateS = leaveAlClosingList.getEntitledDate();
                                Date newDateE = nextEntitle;
                                startDateR.setTime(newDateS);
                                endDateR.setTime(newDateE);

                                long timeComm = commencingDateClone.getTime();
                                long timeNextEntitle = newDateE.getTime();
                                long diff = timeNextEntitle - timeComm;

                                DecimalFormat df2 = new DecimalFormat("#,###,###,##0");
                                double difInMonths = (newDateE.getTime() - newDateS.getTime()) / 2592000000d;
                                difInMonths = new Double(df2.format(difInMonths)).doubleValue();

                                //jika sudah lebih dua tahun berarti dia sudah langsung 12
                                Date nextEntitlePer = (Date) leaveAlClosingList.getCommancingDate().clone();
                                nextEntitlePer.setYear(nextEntitlePer.getYear() + 1);

                                nextEntitlePer.setYear(nextEntitlePer.getYear() + 1);
                                nextEntitlePer.setMonth(0);
                                nextEntitlePer.setDate(1);

                                if (nextEntitlePer.getTime() <= new Date().getTime()) {
                                    difInMonths = 12;
                                }
                                Date todayDate = new Date();
                                if (NotfirstAdjusment == 0) {
                                    difInMonths = (newDateE.getTime() - newDateS.getTime()) / 2592000000d;
                                    difInMonths = new Double(df2.format(difInMonths)).doubleValue();
                                } else if (NotfirstAdjusment == 1) {
                                    nextEntitlePer.setYear(nextEntitlePer.getYear() + 1);
                                    if (nextEntitlePer.getTime() >= todayDate.getTime()) {
                                        boolean pertamaClosing = PstAlStockManagement.CekPertamaClosing("" + PstAlStockManagement.fieldNames[PstAlStockManagement.FLD_EMPLOYEE_ID] + " = " + leaveAlClosingList.getEmpId());
                                        if (pertamaClosing) {
                                            difInMonths = (newDateS.getTime() - leaveAlClosingList.getCommancingDate().getTime()) / 2592000000d;
                                            difInMonths = new Double(df2.format(difInMonths)).doubleValue();
                                            residu = (int) difInMonths;
                                        } else {
                                            difInMonths = (newDateE.getTime() - newDateS.getTime()) / 2592000000d;
                                            difInMonths = new Double(df2.format(difInMonths)).doubleValue();
                                            residu = (int) difInMonths;

                                        }
                                    } else {
                                        residu = 12;
                                    }
                                }
                                if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                    LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                            leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                    if (leaveAlClosing != null) {
                                        resultClose.add(leaveAlClosing);
                                    }
                                }
                            } else {
                                Calendar startDateR = Calendar.getInstance();
                                Calendar endDateR = Calendar.getInstance();
                                Date newDateS = leaveAlClosingList.getEntitledDate();
                                Date newDateE = nextEntitle;
                                startDateR.setTime(newDateS);
                                endDateR.setTime(newDateE);

                                DecimalFormat df2 = new DecimalFormat("#,###,###,##0");
                                double difInMonths = (newDateE.getTime() - newDateS.getTime()) / 2592000000d;
                                difInMonths = new Double(df2.format(difInMonths)).doubleValue();

                                Date nextEntitlePer = (Date) leaveAlClosingList.getCommancingDate().clone();
                                nextEntitlePer.setYear(nextEntitlePer.getYear() + 1);

                                nextEntitlePer.setYear(nextEntitlePer.getYear() + 1);
                                nextEntitlePer.setMonth(0);
                                nextEntitlePer.setDate(1);

                                Date todayDate = new Date();
                                if (NotfirstAdjusment == 1) {
                                    nextEntitlePer.setYear(nextEntitlePer.getYear() + 1);
                                    if (nextEntitlePer.getTime() >= todayDate.getTime()) {
                                        difInMonths = (newDateE.getTime() - newDateS.getTime()) / 2592000000d;
                                        difInMonths = new Double(df2.format(difInMonths)).doubleValue();
                                        residu = (int) difInMonths;
                                    } else {
                                        residu = 12;
                                    }
                                }

                                if (resultStatus == 1) {
                                    continue;
                                } else if (resultStatus == 2) {
                                    if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                        LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                                leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                        if (leaveAlClosing != null) {
                                            resultClose.add(leaveAlClosing);
                                        }
                                    } else {
                                        continue;
                                    }
                                } else if (resultStatus == 3) {
                                    if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                        LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                                leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                        if (leaveAlClosing != null) {
                                            resultClose.add(leaveAlClosing);
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                            }
                        } else {
                            if (resultStatus == 1) {
                                continue;
                            } else if (resultStatus == 2) {
                                if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                    LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                            leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                    if (leaveAlClosing != null) {
                                        resultClose.add(leaveAlClosing);
                                    }
                                } else {
                                    continue;
                                }
                            } else if (resultStatus == 3) {
                                if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                                    LeaveAlClosing leaveAlClosing = closeAL(leaveAlClosingList.getAlStockManagementId(),
                                            leaveAlClosingList.getEmpId(), 0, residu, entDate, comcDate);
                                    if (leaveAlClosing != null) {
                                        resultClose.add(leaveAlClosing);
                                    }
                                } else {
                                    continue;
                                }
                            }
                        }
                    }
                }
            }
        }

        if (result != null && result.size() > 0) {
            for (int idx = 0; idx < result.size(); idx++) {
                LeaveAlClosingNoStockList leaveAlNoStockClosingList = (LeaveAlClosingNoStockList) result.get(idx);

                Employee employee = new Employee();
                EmpCategory empCategory = new EmpCategory();
                try {
                    employee = PstEmployee.fetchExc(leaveAlNoStockClosingList.getEmpId());
                } catch (Exception e) {
                }
                try {
                    empCategory = PstEmpCategory.fetchExc(employee.getEmpCategoryId());
                } catch (Exception e) {
                }

                String comcDate = "";

                try {
                    comcDate = Formater.formatDate(leaveAlNoStockClosingList.getCommancingDate(), "yyyy-MM-dd");
                } catch (Exception e) {
                    System.out.println("Exception Parsing " + e.toString());
                    comcDate = "";
                }

                String entDate = Formater.formatDate(new Date(), "yyyy-MM-dd");
                if (empCategory.getEntitleLeave() == PstEmpCategory.ENTITLE_YES) {
                    LeaveAlClosing leaveAlClosing = closeAL(0, leaveAlNoStockClosingList.getEmpId(),
                            0, 0, entDate, comcDate);
                    if (leaveAlClosing != null) {
                        resultClose.add(leaveAlClosing);
                    }
                } else {
                    continue;
                }

            }
        }

        if (resultClose.size() > 0 && resultClose != null) {
            if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_ANUAL || leaveConfig.getALEntitleBy() == I_Leave.AL_ENTITLE_BY_PERIOD) { /* menggunakan closing by period */
                SessLeaveClosing.ProcessClosingPeriod(resultClose, leaveConfig);
            } else if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING) { /* Menggunakan closing by commencing date */
                SessLeaveClosing.ProcessClosing(resultClose);
            } else if (leaveConfig.getALEntitleBy() == leaveConfig.AL_ENTITLE_BY_COMMENCING_PERIODE) { /* Menggunakan closing by commencing date */
                SessLeaveClosing.ProcessClosingPeriodAndComencing(resultClose, leaveConfig);
            } else { /* Default gunakan closing by commencing date */
                SessLeaveClosing.ProcessClosing(resultClose);
            }
        }
    }
    
    public LeaveAlClosing closeAL(long alStockId, long employeeId, int extended,
            float dataExtended, String entitleDate, String commencingDate){
        
        LeaveAlClosing leaveAlClosing = new LeaveAlClosing();
        if (employeeId != 0){
            leaveAlClosing.setStockId(alStockId);
            leaveAlClosing.setEmployeeId(employeeId);
            leaveAlClosing.setExpiredDate(extended);
            leaveAlClosing.setExtended(0);
            leaveAlClosing.setCommencingDate(commencingDate);
            leaveAlClosing.setEntitledDate(entitleDate);
            leaveAlClosing.setStatus(1);
        }
        return leaveAlClosing;
    }
    
    /**
     * @return the running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * @param running the running to set
     */
    public void setRunning(boolean running) {
        this.running = running;
    }
    
    /**
     * @return the sleepMs
     */
    public long getSleepMs() {
        return sleepMs;
    }

    /**
     * @param sleepMs the sleepMs to set
     */
    public void setSleepMs(long sleepMs) {
        this.sleepMs = sleepMs;
    }
    
}
