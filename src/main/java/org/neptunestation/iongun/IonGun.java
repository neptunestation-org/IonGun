package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.plugins.*;
import org.neptunestation.iongun.util.*;

public class IonGun {
    public static final String ACCEPT = "ACCEPT";

    public static void main (String[] args) {
	char FS = System.getenv("FS")==null ? (char)28 : (char)Integer.parseInt(System.getenv("FS"));
	char GS = System.getenv("GS")==null ? (char)29 : (char)Integer.parseInt(System.getenv("GS"));
	char RS = System.getenv("RS")==null ? (char)30 : (char)Integer.parseInt(System.getenv("RS"));
	char US = System.getenv("US")==null ? (char)31 : (char)Integer.parseInt(System.getenv("US"));
	try {
	    URL.setURLStreamHandlerFactory(new JDBCURLStreamHandlerFactory());
	    for (String s : args) {
		URLConnection c = (new URL(s)).openConnection();
		c.setRequestProperty(JDBCURLConnection.ACCEPT, System.getenv(ACCEPT));
		print(c.getInputStream(), System.out, RS);
		System.out.print(GS);}}
	catch (Exception e) {e.printStackTrace(); System.exit(1);}}

    public static void print (InputStream in, PrintStream out, char delim) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) {out.print(l); out.print(delim);}}}
