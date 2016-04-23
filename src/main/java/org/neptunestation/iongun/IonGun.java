package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.net.*;
import org.neptunestation.iongun.util.*;

public class IonGun {
    public static void main (String[] args) {
	try {
	    Class.forName("org.neptunestation.iongun.net.MySQLJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.net.OracleJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.net.PostgresJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.net.SQLiteJDBCURLStreamHandler");
	    URL.setURLStreamHandlerFactory(new SqlURLStreamHandlerFactory());
	    String l;
	    for (String s : args) {
		URLConnection c =
		    ("sql".equalsIgnoreCase((new URL(s)).getProtocol()) ?
		     new URL((new URL(s)).getFile()) : new URL(s)).openConnection();
		// c.setRequestProperty("Accept", "text/map");
		// c.addRequestProperty("Accept", "text/properties");
		// c.addRequestProperty("Accept", "text/properties-list");
		// c.addRequestProperty("Accept", "text/properties-xml");
		c.addRequestProperty("Accept", "text/web-xml");
		// c.addRequestProperty("Accept", "text/html");
		// c.addRequestProperty("Accept", "text/csv");
		print(c.getInputStream(), System.out);}}
	catch (Exception e) {
	    e.printStackTrace(System.err);
	    System.exit(1);}}

    public static void print (InputStream in, PrintStream out) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) out.println(l);}}



