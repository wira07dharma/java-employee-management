/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.utility.machine;

import java.sql.*;
import com.dimata.harisma.entity.attendance.MachineTransaction;
import com.dimata.harisma.entity.attendance.PstMachineTransaction;
import com.dimata.harisma.entity.employee.Employee;
import java.util.Vector;

/**
 * @Note : Table CHECKINCHECKOUT di tambahakan columns : 
 *    STATUS dengan tipe : tinyint default=0
 * @author ktanjana
 */
public class Att2000Transfer implements AttTransfer_I, Runnable {

    private final static int STATUS_NEW = 0;
    private final static int STATUS_PROCES = 1;
    private boolean running = false;
    private long sleepMs = 10;
    private String message = "";
    // database ODBC
    String dsnName = "";
    String user = "";
    String password = "";
    // Data untuk database machine
    String tableName = "";   // nama table untuk database mesin
    String machineNumber = ""; // field name - nomor mesin
    String dataIDFld = "DATA_ID"; // field name - nomor mesin
    String employee_num = "BADGENUMBER"; // field di database mesin untuk payroll number
    String employeeID = "";    // field name - id karyawan di mesin data 
    //in-out bisa berbeda dengan employee ID di databank harisma                             
    String checkTime = "";    // field name - waktu attendance
    String checkType = "";    // field name - jenis IN, OUT, OUT-PERSONAL, IN-PERSONAL, OUT ON DUTY, CALLBACLsesuai 
    // tergantung dari jenis mesin yang disupport pada saat masuk harisma akan di convert 
    String verifyCode = "";  // field name - apakah sukses di verify atau tidak
    String status = "";       // field name - status yang akan di set oleh harisma setelah data diambil : 
    // dari 0=belum diambil harisma menjadi 1=sudah diambil
    private int recordSize = 0;
    private int progressSize = 0;

    //update by satrya 2013-12-18
    java.util.Date startDate=null;
    java.util.Date endDate=null;
    int statusHr=STATUS_NEW;
    int changeAutomaticManualFinish=0;
    
   
    public Att2000Transfer() {
        initClass(10, "HR-AZEC", "hairisma", "hairisma2019", "CHECKINOUT",
                "USERID", "SENSORID", "CHECKTIME", "CHECKTYPE", "VERIFYCODE", "STATUS");
    }

    public void initClass(long sleepMs, String dsnName, String user, String password,
            String tableName, String employeeID, String machineNumber, String checkTime,
            String checkType, String verifyCode, String status) {
        this.sleepMs = sleepMs;
        this.dsnName = dsnName;
        this.user = user;
        this.password = password;
        this.tableName = tableName;
        this.machineNumber = machineNumber;
        this.employeeID = employeeID;
        this.checkTime = checkTime;
        this.checkType = checkType;
        this.verifyCode = verifyCode;
        this.status = status;
        this.dataIDFld = "DATA_ID";
    }

        /**
     * <pre>create by satrya 2013-12-18</pre>
     * Keterangan: untuk parameter search pencarian DB
     * @param startDate
     * @param endDate
     * @param statusHr 
     */
    public void searchDBMachine(java.util.Date startDate, java.util.Date endDate, int statusHr,int changeAutomaticManualFinish) {
     this.startDate=startDate;
     this.endDate=endDate;
     this.statusHr=statusHr;
     this.changeAutomaticManualFinish=changeAutomaticManualFinish;
    }
    public int initRecordToGet() {
        Connection theConn = null;
        int theSize = 0;
        try {
            // connection to an ACCESS MDB
            theConn = ODBCConnection.getConnection(dsnName, user, password);

            ResultSet rs;
            Statement stmt;
            String sql;

            sql = "select count(" + this.employeeID + ") as size from " + this.tableName + " where "
                    + this.status + "=" + STATUS_NEW + " OR " + this.status + " IS NULL";
            stmt = theConn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                theSize = rs.getInt("size");
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (theConn != null) {
                    theConn.close();
                }
            } catch (Exception e) {
            }
        }
        return theSize;
    }

    public void run() {

        this.setRunning(true);
        System.out.println("Process transfer data mesin dimulai");

        Connection getConn = null;

        try {
            getConn = ODBCConnection.getConnection(dsnName, user, password);

        } catch (Exception exc) {
            message = "Error on connection to database DSN=" + dsnName;
            return;
        }

        while (this.running) {
            String sql="";
            try {
                Thread.sleep(this.getSleepMs());
                // get records dari table mesin
                int limit = 50;
                ResultSet rs;
                Statement stmt;
                /*String sql = "SELECT " + Att2000.Fld_InOut_USERID + ","
                + Att2000.Fld_InOut_CHECKTIME + ","
                + Att2000.Fld_InOut_CHECKTYPE + ","
                + Att2000.Fld_InOut_VERIFYCODE + ","
                + Att2000.Fld_InOut_SENSORID + ","
                + Att2000.Fld_InOut_WorkCode + ","
                + Att2000.Fld_InOut_sn
                + " FROM " + Att2000.Tbl_CheckInOut + " WHERE "
                + Att2000.Fld_InOut_STATUS + " IS NULL OR " + Att2000.Fld_InOut_STATUS + " = '" + Att2000.Status_Not_Transfered + "'";
                 */

                sql = "select u." + this.employee_num + ",a." + this.checkTime + ",a." + this.checkType
                        + ",a." + this.machineNumber + ",a."+this.dataIDFld+ " from " + this.tableName + " a left join userinfo u on a." + this.employeeID
                        + "= u." + this.employeeID + " where ("
                        + this.status + "=" + STATUS_NEW + " OR " + this.status + " IS NULL) AND "+this.checkTime+" > '2019-01-01' ORDER BY " + this.checkTime;
               // System.out.println(sql);
                if (jTextArea != null) {
                    jTextArea.setText(sql);
                }
                stmt = getConn.createStatement();
                message = "Run query on table machine " + this.tableName;
                rs = stmt.executeQuery(sql);
                String dataEmployee = "";
                String dataCheckTime = "";
                String dataCheckType = "";
                String dataMachineNumber = "";
                MachineTransaction machTrans = new MachineTransaction();
                long dataID =0;
                //System.out.println(recordSize);
                int theSize = initRecordToGet();
                if (theSize > 0) {
                    recordSize = theSize;
                    progressSize = 0;
                }
                while (theSize > 0 && rs.next() && this.running) {

                    //System.out.println(recordSize);
                    Thread.sleep(this.getSleepMs());
                    dataEmployee = rs.getString(this.employee_num);
                    dataCheckTime = rs.getString(this.checkTime);
                    dataCheckType = rs.getString(this.checkType);
                    dataMachineNumber = rs.getString(this.machineNumber);
                    dataID = rs.getLong(this.dataIDFld);
                    machTrans.setCardId(dataEmployee);
                    machTrans.setDateTransaction(dataCheckTime, "yyyy-MM-dd HH:mm:ss");
                    machTrans.setMode(dataCheckType);
                    machTrans.setStation("" + dataMachineNumber);

                    //System.out.println("insert");
                    long idTrans = 0;
                    try {
                        MachineTransaction existingTrans = PstMachineTransaction.fetchBy(machTrans.getCardId(), 
                                 machTrans.getDateTransaction(), machTrans.getMode(),  machTrans.getStation());                         
                            idTrans = existingTrans.getOID();
                         if(idTrans==0){
                            idTrans = PstMachineTransaction.insertExc(machTrans);
                         }
                    } catch (Exception exc2) {
                        //System.out.println(exc2);
                    }
                    if (idTrans != 0) {
                        try {
                            //System.out.println("update");
                            updateODBC(""+dataID, dataEmployee, dataCheckTime, dsnName);
                            jProgressBar.setMinimum(0);
							jProgressBar.setMaximum(theSize);
							jProgressBar.setValue(progressSize);
							jProgressBar.setStringPainted(true);
							progressSize++;
                            Thread.sleep(this.getSleepMs());
                        } catch (Exception exc2) {
                            System.out.println("Update Failed" + exc2);
                        }
                    }


                }

                rs.close();
                stmt.close();

                // simpan ke hr_machine transaction
                // set record dari table mesin , sudah diambil dan disimpan
            } catch (Exception exc) {
                System.out.println(" RUN "+ sql +" >>> : "+exc);
            }
        }

        this.running = false;


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
     * @return the progressSize
     */
    public int getProgressSize() {
        return progressSize;
    }

    /**
     * @return the recordSize
     */
    public int getRecordSize() {
        // System.out.println("=============================+++++++++======================");
        //System.out.println(recordSize);
        return recordSize;

    }

    public boolean updateODBC(String pkData, String userid, String checktime, String db_access) {
        Connection cn = null;
        Statement stmt = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            String cs = "jdbc:odbc:" + db_access;
            cn = DriverManager.getConnection(cs);
            String qry = "UPDATE CHECKINOUT SET STATUS=" + STATUS_PROCES + " WHERE "+this.dataIDFld+"="+pkData;
                    /*" WHERE USERID=" + userid + " AND CHECKTIME= #" + checktime + "# OR "*/;
            stmt = cn.createStatement();
            stmt.executeUpdate(qry);
            return true;
        } catch (Exception e) {
            System.out.println("EXCEPTION " + e.toString());
        } finally {
            try {
                stmt.close();
                cn.close();
            } catch (Exception e) {
                System.out.println("EXCEPTION " + e.toString());
            }
        }
        return false;
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
    private javax.swing.JProgressBar jProgressBar = null;

    public void setProgressBar(javax.swing.JProgressBar jProgressBar) {
        this.jProgressBar = jProgressBar;
    }
    javax.swing.JTextArea jTextArea = null;

    public void setTextArea(javax.swing.JTextArea jTextAreaPar) {
        jTextArea = jTextAreaPar;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    public Vector<Employee> getEmployees() {
        Connection getConn = null;
        Vector<Employee>  employees = new Vector();
        try {
            getConn = ODBCConnection.getConnection(dsnName, user, password);
            int limit = 3000;
            ResultSet rs;
            Statement stmt;

            String sql;
            sql = "select userid, badgenumber, name from user_info";            
            stmt = getConn.createStatement();            
            rs = stmt.executeQuery(sql);
            long userid = 0;
            String badgenumber = "";
            String name = "";
            while (rs.next()) {
                userid = rs.getLong("userid");
                badgenumber = rs.getString("badgenumber");
                name = rs.getString("name");                
                Employee employee = new Employee();
                employee.setBarcodeNumber(badgenumber);
                employee.setOID(userid);
                employee.setFullName(name);
                employees.add(employee);
            }

            rs.close();
            stmt.close();

            // simpan ke hr_machine transaction
            // set record dari table mesin , sudah diambil dan disimpan
        } catch (Exception exc) {
            System.out.println(exc);
        }
        return employees;
    }

    public void initDBConfig(DBMachineConfig dbConfig) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
}
