/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.harisma.utility.machine;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;


/**
 *
 * @author IanRizky
 */
public class SQLServerConnection {
	public static Connection getConnection(String dsnName,String dbName, String user, String password) throws Exception {
    Driver d = (Driver)Class.forName
     ("net.sourceforge.jtds.jdbc.Driver").newInstance();
    String URL = "jdbc:jtds:sqlserver://"+dsnName+"/"+dbName;
	//System.out.println("URL Si Anying"+ URL);
	Connection c = DriverManager.getConnection(URL, user, password);
	//System.out.println("Connection Open");
    return c;
    }
}
