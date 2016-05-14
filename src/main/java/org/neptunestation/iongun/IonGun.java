package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.plugins.*;
import org.neptunestation.iongun.util.*;

public class IonGun {
    public static void main (String[] args) {
	try {
	    // Load Database Type Stream Handlers
	    Class.forName("org.neptunestation.iongun.plugins.MySQLJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.plugins.OracleJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.plugins.PostgresJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.plugins.SQLiteJDBCURLStreamHandler");
	    // Load Output Type Handlers
	    Class.forName("org.neptunestation.iongun.plugins.MapResultSetHandler");
	    Class.forName("org.neptunestation.iongun.plugins.PropertiesResultSetHandler");
	    Class.forName("org.neptunestation.iongun.plugins.PropertiesListResultSetHandler");
	    Class.forName("org.neptunestation.iongun.plugins.PropertiesXMLResultSetHandler");
	    Class.forName("org.neptunestation.iongun.plugins.WebXMLResultSetHandler");
	    Class.forName("org.neptunestation.iongun.plugins.JSONResultSetRenderer");
	    URL.setURLStreamHandlerFactory(new SqlURLStreamHandlerFactory());
	    String l;
	    for (String s : args) {
		JDBCURLConnection c =
		    (JDBCURLConnection)
		    ("sql".equalsIgnoreCase((new URL(s)).getProtocol()) ?
		     new URL((new URL(s)).getFile()) : new URL(s)).openConnection();
		c.setResultSetHandlerFactory(new ResultSetHandlerFactory(){});
		c.addRequestProperty("Accept", System.getProperty("accept"));
		print(c.getInputStream(), System.out);
		System.out.print((char)29);}}
	catch (Exception e) {
	    e.printStackTrace(System.err);
	    System.exit(1);}}

    public static void print (InputStream in, PrintStream out) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) out.println(l);}}



