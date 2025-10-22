package com.omnet.cnt.SecurityConfig;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import oracle.jdbc.OracleConnection;

public class proxyHelper {
	

	    public static Connection getProxyConnection(DataSource ds, String appUser) throws SQLException {
	        Connection physicalConn = ds.getConnection();
	        OracleConnection oracleConn = physicalConn.unwrap(OracleConnection.class);
	        Properties proxyProps = new Properties();
	        proxyProps.put(OracleConnection.PROXY_USER_NAME, appUser);
	        oracleConn.openProxySession(OracleConnection.PROXYTYPE_USER_NAME, proxyProps);
           System.out.println("proxyhelperfile");
	        return oracleConn; 
	    }
	}


