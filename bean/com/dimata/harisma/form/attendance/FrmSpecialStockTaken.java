/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.form.attendance;

import com.dimata.harisma.entity.attendance.PstEmpSchedule;
import com.dimata.harisma.entity.attendance.SpecialStockTaken;
import com.dimata.harisma.entity.leave.I_Leave;
import com.dimata.harisma.entity.masterdata.Period;
import com.dimata.harisma.entity.masterdata.PstPeriod;
import com.dimata.harisma.entity.masterdata.PstScheduleCategory;
import com.dimata.harisma.entity.masterdata.PstScheduleSymbol;
import com.dimata.harisma.entity.masterdata.ScheduleSymbol;
import com.dimata.harisma.session.leave.SessLeaveApplication;
import com.dimata.qdep.form.FRMHandler;
import com.dimata.qdep.form.I_FRMInterface;
import com.dimata.qdep.form.I_FRMType;
import com.dimata.system.entity.PstSystemProperty;
import com.dimata.util.Formater;
import java.util.Date;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Dimata 007
 */
public class FrmSpecialStockTaken extends FRMHandler implements I_FRMInterface, I_FRMType {

    private SpecialStockTaken entSpecialStockTaken;
    public static final String FRM_NAME_SPECIAL_STOCK_TAKEN = "FRM_NAME_SPECIAL_STOCK_TAKEN";
    public static final int FRM_FIELD_SPECIAL_STOCK_TAKEN_ID = 0;
    public static final int FRM_FIELD_EMPLOYEE_ID = 1;
    public static final int FRM_FIELD_SCHEDULE_ID = 2;
    public static final int FRM_FIELD_SPECIAL_STOCK_ID = 3;
    public static final int FRM_FIELD_TAKEN_DATE = 4;
    public static final int FRM_FIELD_TAKEN_QTY = 5;
    public static final int FRM_FIELD_PAID_DATE = 6;
    public static final int FRM_FIELD_LEAVE_APPLICATION_ID = 7;
    public static final int FRM_FIELD_TAKEN_FINISH_DATE = 8;

    public static String[] fieldNames = {
        "FRM_FIELD_SPECIAL_STOCK_TAKEN_ID",
        "FRM_FLD_EMPLOYEE_ID",
        "FRM_FIELD_SCHEDULE_ID_STOCK",
        "FRM_FIELD_SPECIAL_STOCK_ID",
        "FRM_FIELD_SS_TAKEN_DATE",
        "FRM_FIELD_TAKEN_QTY",
        "FRM_FIELD_PAID_DATE",
        "FRM_FLD_LEAVE_APPLICATION_ID",
        "FRM_FIELD_SS_TAKEN_FINISH_DATE"
    };

    public static int[] fieldTypes = {
        TYPE_LONG,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG + ENTRY_REQUIRED,
        TYPE_LONG,
        TYPE_DATE,
        TYPE_FLOAT,
        TYPE_DATE,
        TYPE_LONG,
        TYPE_DATE
    };

    public FrmSpecialStockTaken() {
    }

    public FrmSpecialStockTaken(SpecialStockTaken entSpecialStockTaken) {
        this.entSpecialStockTaken = entSpecialStockTaken;
    }

    public FrmSpecialStockTaken(HttpServletRequest request, SpecialStockTaken entSpecialStockTaken) {
        super(new FrmSpecialStockTaken(entSpecialStockTaken), request);
        this.entSpecialStockTaken = entSpecialStockTaken;
    }

    public String getFormName() {
        return FRM_NAME_SPECIAL_STOCK_TAKEN;
    }

    public int[] getFieldTypes() {
        return fieldTypes;
    }

    public String[] getFieldNames() {
        return fieldNames;
    }

    public int getFieldSize() {
        return fieldNames.length;
    }

    public SpecialStockTaken getEntityObject() {
        return entSpecialStockTaken;
    }

    public void requestEntityObject(SpecialStockTaken entSpecialStockTaken) {
        try {
            this.requestParam();
            entSpecialStockTaken.setEmployeeId(getLong(FRM_FIELD_EMPLOYEE_ID));
            entSpecialStockTaken.setScheduleId(getLong(FRM_FIELD_SCHEDULE_ID));
            entSpecialStockTaken.setSpecialStockId(getLong(FRM_FIELD_SPECIAL_STOCK_ID));
            entSpecialStockTaken.setTakenDate(getDate(FRM_FIELD_TAKEN_DATE));
            entSpecialStockTaken.setTakenQty(getFloat(FRM_FIELD_TAKEN_QTY));
            entSpecialStockTaken.setPaidDate(getDate(FRM_FIELD_PAID_DATE));
            entSpecialStockTaken.setLeaveApplicationId(getLong(FRM_FIELD_LEAVE_APPLICATION_ID));
            entSpecialStockTaken.setTakenFinishDate(getDate(FRM_FIELD_TAKEN_FINISH_DATE));
			
			boolean leaveConfigCalculationCategoryOff = false;
            I_Leave leaveConfig = null;
            try {
                leaveConfigCalculationCategoryOff = Boolean.parseBoolean(PstSystemProperty.getValueByName("LEAVE_CONFIG_CALCULATE_CATEGORY_SCHEDULE_OFF"));
                leaveConfig = (I_Leave) (Class.forName(PstSystemProperty.getValueByName("LEAVE_CONFIG")).newInstance());
            } catch (Exception exc) {
                System.out.println("Exc FrmAlStockTaken" + exc);
            }
			
			if (leaveConfig != null && leaveConfig.getConfigurationLeaveApprovall() == I_Leave.LEAVE_CONFIG_REQUEST_DP_TAKEN_YES_LONG) {
                entSpecialStockTaken.setTakenFinishDate(getDate(FRM_FIELD_TAKEN_FINISH_DATE));
            } else {
                entSpecialStockTaken.setTakenFinishDate(getDate(FRM_FIELD_TAKEN_DATE));
                ///update by satrya 2012-08-07
                //dpStockTaken.setTakenFinnishDate(getDate(FRM_FIELD_TAKEN_DATE));
                // Kartika : finish date is the same  as taken date only the time will be different            
                java.util.Date finishTime = getDate(FRM_FIELD_TAKEN_FINISH_DATE);
                java.util.Date finishDate = entSpecialStockTaken.getTakenFinishDate();
                finishDate.setHours(finishTime.getHours());
                finishDate.setMinutes(finishTime.getMinutes());
                finishDate.setSeconds(finishTime.getSeconds());
                entSpecialStockTaken.setTakenFinishDate(finishDate);
            }
			
			if (entSpecialStockTaken.getTakenDate().getHours() == entSpecialStockTaken.getTakenFinishDate().getHours()
                    && entSpecialStockTaken.getTakenDate().getMinutes() == entSpecialStockTaken.getTakenFinishDate().getMinutes()) { // jika jam dan menit sama berarti cuti dalam hitungan hari kerja

                //update by satrya 2013-12-12  float dpQty = SessLeaveApplication.DATEDIFF(dpStockTaken.getTakenFinnishDate(), dpStockTaken.getTakenDate());
                float dpQty = 0;
                //jika ingin menggunakan konfigurasi, dihitung cutinya tidak dengan dayoff
                if (leaveConfigCalculationCategoryOff) {
                    int diffDay = SessLeaveApplication.DATEDIFF(entSpecialStockTaken.getTakenFinishDate(), entSpecialStockTaken.getTakenDate()) + 1;//kenapa di tambah 1 karena belum di hitung start datenya
                    //untuk mencari hari libur
                    Vector vctSchIDOff = PstScheduleSymbol.getScheduleId(PstScheduleCategory.CATEGORY_OFF);
                    int dayOffSchedule = 0;
                   
                    if (entSpecialStockTaken.getTakenDate() != null) {
                        for (int ix = 0; ix < diffDay; ix++) {
                            Date selectedDate = new Date(entSpecialStockTaken.getTakenDate().getYear(), entSpecialStockTaken.getTakenDate().getMonth(), (entSpecialStockTaken.getTakenDate().getDate() + ix));
                            Period period = PstPeriod.getPeriodBySelectedDate(selectedDate);
                            dayOffSchedule = dayOffSchedule + PstEmpSchedule.getStatusDayOff(entSpecialStockTaken.getEmployeeId(), period.getOID(), 0, vctSchIDOff, selectedDate);
                            int dayOffSchedules = PstEmpSchedule.getStatusDayOff(entSpecialStockTaken.getEmployeeId(), period.getOID(), 0, vctSchIDOff, selectedDate);
                            if (dayOffSchedules == 0) {
                                Date dtTakendate = null;
                                Date dtTakenFinishDate=null;
                                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(period.getOID(), entSpecialStockTaken.getEmployeeId(), selectedDate, 0);
                               
								if(ix==0){
									dtTakendate =(new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate(), entSpecialStockTaken.getTakenDate().getHours(), entSpecialStockTaken.getTakenDate().getMinutes(), 0));
								}else{
									dtTakendate =(new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate(), scheduleSymbol.getTimeIn().getHours(), scheduleSymbol.getTimeIn().getMinutes(), 0));
								}
								if(ix==diffDay-1){
									 dtTakenFinishDate = (new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate(), entSpecialStockTaken.getTakenFinishDate().getHours(), entSpecialStockTaken.getTakenFinishDate().getMinutes(), 0));
								}else{
									dtTakenFinishDate = (new Date(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDate(), scheduleSymbol.getTimeOut().getHours(), scheduleSymbol.getTimeOut().getMinutes(), 0));
								}
                            }
                        }
                    }
                    dpQty = diffDay - dayOffSchedule;
                } else {
                    dpQty = SessLeaveApplication.DATEDIFF(entSpecialStockTaken.getTakenFinishDate(), entSpecialStockTaken.getTakenDate());
                }
                //update by satrya 2013-02-24
                long periodId = PstPeriod.getPeriodIdBySelectedDateString(Formater.formatDate(entSpecialStockTaken.getTakenDate(), "yyyy-MM-dd"));
                ScheduleSymbol scheduleSymbol = PstEmpSchedule.getEmpScheduleDateTime(periodId, entSpecialStockTaken.getEmployeeId(), entSpecialStockTaken.getTakenDate(), 0);

                if (scheduleSymbol != null && scheduleSymbol.getTimeIn() != null && scheduleSymbol.getTimeOut() != null) {
                    Date taken = entSpecialStockTaken.getTakenDate();
                    taken.setHours(scheduleSymbol.getTimeIn().getHours());
                    taken.setMinutes(scheduleSymbol.getTimeOut().getMinutes());
                    Date finish = entSpecialStockTaken.getTakenFinishDate();
                    finish.setHours(scheduleSymbol.getTimeOut().getHours());
                    finish.setMinutes(scheduleSymbol.getTimeOut().getMinutes());

                    entSpecialStockTaken.setTakenDate(taken);
                    entSpecialStockTaken.setTakenFinishDate(finish);
                    //update by satrya 2014-04-18
                    //karena jika dia off maka tidak bisa save
                } else {
                    //jika schedulenya bernilai null maka di set defautl 8
                    Date taken = entSpecialStockTaken.getTakenDate();
                    taken.setHours(8);
                    taken.setMinutes(0);
                    taken.setSeconds(0);
                    Date finish = entSpecialStockTaken.getTakenFinishDate();
                    finish.setHours(17);
                    finish.setMinutes(0);
                    finish.setSeconds(0);
                    entSpecialStockTaken.setTakenDate(taken);
                    entSpecialStockTaken.setTakenFinishDate(finish);
                    //update by satrya 2014-04-18
                    //karena jika dia off maka tidak bisa save
                }
                //update by satrya 2013-12-12
                if (leaveConfigCalculationCategoryOff) {
                    entSpecialStockTaken.setTakenQty(dpQty);
                } else {
                    entSpecialStockTaken.setTakenQty(dpQty + 1);
                }

            }
			
        } catch (Exception e) {
            System.out.println("Error on requestEntityObject : " + e.toString());
        }
    }

}
