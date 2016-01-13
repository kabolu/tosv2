package edu.must.tos.util;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import javax.naming.*;
import javax.sql.DataSource;

public class TosDbConnectionListener implements ServletContextListener {
	public void contextInitialized(ServletContextEvent sce){
		try {
			Context envCtx = (Context) new InitialContext().lookup("java:comp/env");
			DataSource  ds = (DataSource) envCtx.lookup ("jdbc/tosv2");
			if (ds != null){
				sce.getServletContext().setAttribute("dbpool", ds);
       		}
		} catch (Exception e) {}
	}
	
	public void contextDestroyed(ServletContextEvent sce) {	}
}
