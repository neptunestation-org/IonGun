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
	    URL.setURLStreamHandlerFactory(new JDBCURLStreamHandlerFactory());
	    for (String s : args) {
		URLConnection c = (new URL(s)).openConnection();
		c.setRequestProperty("Accept", System.getenv("ACCEPT"));
		print(c.getInputStream(), System.out);}}
	catch (Exception e) {System.exit(1);}}

    public static void print (InputStream in, PrintStream out) throws IOException {
	print(in, out, (char)29);}

    public static void print (InputStream in, PrintStream out, char delim) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) out.println(l);
	out.print(delim);}}



