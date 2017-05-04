package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.plugins.*;
import org.neptunestation.iongun.util.*;

public class IonGun {
    public static void main (final String[] args) {
	try {
	    char FS = System.getenv("FS")==null ? (char)28 : (char)Integer.parseInt(System.getenv("FS"));
	    char GS = System.getenv("GS")==null ? (char)10 : (char)Integer.parseInt(System.getenv("GS"));
	    char RS = System.getenv("RS")==null ? (char)10 : (char)Integer.parseInt(System.getenv("RS"));
	    char US = System.getenv("US")==null ? (char)44 : (char)Integer.parseInt(System.getenv("US"));
	    String ACCEPT = System.getenv("ACCEPT")==null ? "text/csv" : System.getenv("ACCEPT");
	    URL.setURLStreamHandlerFactory(new JDBCURLStreamHandlerFactory());
	    boolean first = true;
	    for (String s : args) {
		URLConnection c = (new URL(s)).openConnection();
		c.setRequestProperty(JDBCURLStreamHandlerFactory.ACCEPT, ACCEPT);
		c.setRequestProperty(JDBCURLStreamHandlerFactory.FS, String.valueOf(FS));
		c.setRequestProperty(JDBCURLStreamHandlerFactory.GS, String.valueOf(GS));
		c.setRequestProperty(JDBCURLStreamHandlerFactory.RS, String.valueOf(RS));
		c.setRequestProperty(JDBCURLStreamHandlerFactory.US, String.valueOf(US));
		if (!first) System.out.print(GS);
		print(c.getInputStream(), System.out, RS);
		first = false;}}
	catch (Exception e) {throw new RuntimeException(e);}}

    public static void print (final InputStream in, final PrintStream out, final char delim) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) {out.print(l); out.print(delim);}}}
