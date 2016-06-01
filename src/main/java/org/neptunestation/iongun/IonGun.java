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
	    String[] pseudos = System.getenv("PSEUDOQUERIES")==null ? new String[]{} : System.getenv("PSEUDOQUERIES").split(",");
	    for (String s : args) {
		ArrayList<String> queries = new ArrayList<>();
		queries.addAll(Arrays.asList(pseudos));
		String[] p = s.split("\\?");
		if (p.length>1) queries.add(p[1]);
		for (String q : queries) {
		    URLConnection c = (new URL(String.format("%s?%s", p[0], q))).openConnection();
		    c.setRequestProperty("Accept", System.getenv("ACCEPT"));
		    print(c.getInputStream(), System.out);}}}
	catch (Exception e) {e.printStackTrace(); System.exit(1);}}

    public static void print (InputStream in, PrintStream out) throws IOException {
	print(in, out, (char)29);}

    public static void print (InputStream in, PrintStream out, char delim) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) out.println(l);
	out.print(delim);}}



