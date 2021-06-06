/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.utility.service.leavedp;

import com.dimata.harisma.entity.leave.I_Leave;
import com.dimata.harisma.entity.leave.LeaveLlClosingList;
import com.dimata.harisma.entity.leave.LlClosingSelected;
import com.dimata.harisma.session.leave.LeaveConfigHR;
import com.dimata.harisma.session.leave.SessLeaveClosing;
import com.dimata.system.entity.system.PstSystemProperty;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Gunadi
 */
public class AutomaticLlClosing implements Runnable {
    
    int i = 0;
    //update by satrya 2013-02-25
    private Date startDate=null;
     private boolean running = false;
     private long sleepMs = 3600000;
     private long firstIdle = 300000;
    public AutomaticLlClosing() {
    }
    
    public void run() 
    {
        try {
            
            /* First Idle sekitar 5 menit agar AlClosing selesai terlebih dahulu */
            try {
                Thread.sleep(this.getFirstIdle());
            } catch (Exception exc) {
                System.out.println(exc);
            } 

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
                
                llClosing(leaveConfig);
                
                try {
                    Thread.sleep(this.getSleepMs());
                } catch (Exception exc) {
                    System.out.println(exc);
                } finally {
                }
            }
            this.running = false;


        } catch (Exception exc) {
            System.out.println(">>> Exception on AutomaticllClosing service :((");
        }
    }
    
    public void llClosing(I_Leave leaveConfig) {
        Vector resultLL_all = SessLeaveClosing.listLLClosingALL();
        Vector result_close = new Vector();
        
        int configuration = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_I];     // range mendapatkan LL (dalam bulan)
        int configuration_2 = leaveConfig.getIntervalLLinMonths()[LeaveConfigHR.iINTERVAL_LL_HARDROCK_II];  // range mendapatkan LL (dalam bulan)
        
        int max_size = resultLL_all.size();
        int max_iterasi = resultLL_all.size() -1 ;
        int max_count = 0;
        int i = 0 ;
        
        for (int index=0; index < resultLL_all.size(); index++){
            LeaveLlClosingList leaveLlClosingList = (LeaveLlClosingList)resultLL_all.get(index);
            float sum_exp = SessLeaveClosing.getSumExpired(leaveLlClosingList.getLlStockManagementId());
            float var_residu = leaveLlClosingList.getPrevBalance() + leaveLlClosingList.getLlQty() - leaveLlClosingList.getQtyUsed() - sum_exp;
            int statusLongLeave = SessLeaveClosing.statusDataLLStockManagement(leaveLlClosingList.getLlStockManagementId(),leaveLlClosingList.getEmpId(),leaveLlClosingList.getCommancingDate(), configuration, leaveLlClosingList.getEntitledDate(),configuration_2);
            int countData = SessLeaveClosing.sumLLStock(leaveLlClosingList.getEmpId());    //Jumlah data yang aktif
            float sum_expired = SessLeaveClosing.getSumExpired(leaveLlClosingList.getLlStockManagementId());
            if(countData == 1 && statusLongLeave == SessLeaveClosing.LL_AKTIF){
                continue;
            } else {
                max_count = max_count + 1;
                
                int diff = SessLeaveClosing.DATEDIFF(leaveLlClosingList.getExpiredDate(), leaveLlClosingList.getExpiredDate2());
                
                float extend_val = 0;
                
                if(diff == 0){
                    
                    extend_val = leaveLlClosingList.getPrevBalance()+leaveLlClosingList.getEntitled()-leaveLlClosingList.getQtyUsed();
                    
                }else if(diff > 0){
                    
                    extend_val = leaveLlClosingList.getPrevBalance()+leaveLlClosingList.getEntitled2()-leaveLlClosingList.getQtyUsed();
                    
                }else if(diff < 0){
                    
                    extend_val = leaveLlClosingList.getPrevBalance()+leaveLlClosingList.getEntitled()-leaveLlClosingList.getQtyUsed();
                    
                }
                
                long stock_id_first = 0;
                
                stock_id_first = SessLeaveClosing.getLLStock_Id_First(leaveLlClosingList.getEmpId());
                
                String comencingDate = Formater.formatDate(leaveLlClosingList.getCommancingDate(),"yyyy-MM-dd");
                String entitleDate = Formater.formatDate(leaveLlClosingList.getEntitledDate(),"yyyy-MM-dd");
                
                String exp_dt1 = "-";
                String exp_dt2 = "-";
                
                if(leaveLlClosingList.getExpiredDate() != null){
                    
                    try{
                        exp_dt1 = Formater.formatDate(leaveLlClosingList.getExpiredDate(),"yyyy-MM-dd");
                    }catch(Exception e){
                        exp_dt1 = "-";
                    }
                    
                }
                
                if(leaveLlClosingList.getExpiredDate2() != null){
                    
                    try{
                        exp_dt2 = Formater.formatDate(leaveLlClosingList.getExpiredDate2(),"yyyy-MM-dd");
                    }catch(Exception e){
                        exp_dt2 = "-";
                    }
                    
                }
                
                float residu = leaveLlClosingList.getPrevBalance() + leaveLlClosingList.getLlQty() - leaveLlClosingList.getQtyUsed() - sum_expired;
                
                boolean statusApplicationNotClose = false;
                
                statusApplicationNotClose = SessLeaveClosing.getApplicationLLNotClose(leaveLlClosingList.getLlStockManagementId());    
                if(statusApplicationNotClose == true && statusLongLeave == SessLeaveClosing.LL_CLOSE){
                    continue;
                } else {
                    if(countData > 1){
                        if(statusLongLeave == SessLeaveClosing.LL_CLOSE){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_CLOSE,0,extend_val, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF,0,extend_val, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_CLOSE_AKTIF_EXIST){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_CLOSE_AKTIF_EXIST,0,residu, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_INVALID){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_INVALID,0,0, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF_ENTITLE_2){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF_ENTITLE_2,0,0, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF_EXP_1){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF_EXP_1,0,residu, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF_EXP_2){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF_EXP_2,0,residu, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        }
                    } else {
                        if(statusLongLeave == SessLeaveClosing.LL_CLOSE){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_CLOSE,0,0, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF,0,extend_val, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_CLOSE_AKTIF_EXIST){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_CLOSE_AKTIF_EXIST,0,residu, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_INVALID){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_INVALID,0,0, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF_ENTITLE_2){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF_ENTITLE_2,0,0, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF_EXP_1){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF_EXP_1,0,residu, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        } else if(statusLongLeave == SessLeaveClosing.LL_AKTIF_EXP_2){
                            LlClosingSelected llClosingSelected = closeLL(leaveLlClosingList.getLlStockManagementId(),
                                    leaveLlClosingList.getEmpId(), leaveLlClosingList.getEntitledDate(),
                                    leaveLlClosingList.getEntitleDate2(), leaveLlClosingList.getCommancingDate(),
                                    SessLeaveClosing.LL_AKTIF_EXP_2,0,residu, leaveLlClosingList.getEntitled(),
                                    leaveLlClosingList.getEntitled2(),leaveLlClosingList.getExpiredDate(),
                                    leaveLlClosingList.getExpiredDate2(),leaveLlClosingList.getPrevBalance(),
                                    leaveLlClosingList.getQtyUsed(),leaveLlClosingList.getLlQty());
                            if (llClosingSelected != null){
                                result_close.add(llClosingSelected);
                            }
                        }
                    }
                }
            }
        }
        if(result_close != null && result_close.size() > 0){
            System.out.println(":::::::::::::::::::::::: CLOSING LL START ::::::::::::::::::::::::::::");
            SessLeaveClosing.ProcessClosingLL(result_close);
        }
    }
    
    public LlClosingSelected closeLL(long llStockId, long employeeId, Date entitleDate,
            Date entitleDate2, Date commencingDate, int statusData, int intervalDate,
            float extendValue, float entitle1, float entitle2, Date expDate1, Date expDate2,
            float prevBal, float qtyUsed, float qty){
        
        LlClosingSelected llClosingSelected = new LlClosingSelected();
        int   extended_value = 0;
        int   entitle_1 = 0;
        int   entitle_2 = 0;
        int   prev_bal = 0;
        int   qty_used = 0;
        int   i_qty = 0;
        
        try{
            i_qty = (int)qty;
        }catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
        
        try{
            extended_value = (int)extendValue;
        }catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
        
        try{
            entitle_1 = (int)entitle1;
        }catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
        
        try{
            prev_bal = (int)prevBal;
        }catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
        
        try{
            prev_bal = (int)prevBal;
        }catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
        
        try{
            qty_used = (int)qtyUsed;
        }catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
        
        if (employeeId != 0){
            llClosingSelected.setLlStockId(llStockId);
            llClosingSelected.setEmployeeId(employeeId);
            llClosingSelected.setEntitleDate(entitleDate);
            llClosingSelected.setEntitleDate2(entitleDate2);
            llClosingSelected.setCommencingDate(commencingDate);
            llClosingSelected.setStatusData(statusData);
            llClosingSelected.setIterval_date(intervalDate);
            llClosingSelected.setExtended_value(extended_value);
            llClosingSelected.setEntitle_1(entitle_1);
            llClosingSelected.setEntitle_2(entitle_2);
            llClosingSelected.setExp_date_1(expDate1);
            llClosingSelected.setExp_date_2(expDate2);
            llClosingSelected.setPrev_balance(prev_bal);
            llClosingSelected.setQty_taken(qty_used);
            llClosingSelected.setQty(i_qty);
        }
        
        return llClosingSelected;
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
    
    /**
     * @return the firstIdle
     */
    public long getFirstIdle() {
        return firstIdle;
    }

    /**
     * @param firstIdle the firstIdle to set
     */
    public void setFirstIdle(long firstIdle) {
        this.firstIdle = firstIdle;
    }
    
}