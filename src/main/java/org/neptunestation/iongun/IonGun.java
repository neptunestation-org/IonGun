package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.plugins.*;
import org.neptunestation.iongun.util.*;

public class IonGun {
    static char
	FS = (char)28,
	GS = (char)29,
	RS = (char)30,
	US = (char)31;

    public static void main (String[] args) {
	String line = null;
	RS = (char)10;
	GS = (char)10;
	try {
	    URL.setURLStreamHandlerFactory(new JDBCURLStreamHandlerFactory());
	    if (args.length==0) System.exit(255);
	    ArrayList<String> commands = new ArrayList<>();
	    String[] p = args[0].split("\\?");
	    if (p.length>1) commands.add(p[1]);
	    if (commands.size()>0) for (String q : commands) {
		    if (q.trim().equals("")) break;
		    URLConnection c = (new URL(String.format("%s?%s", p[0], q))).openConnection();
		    c.setRequestProperty(JDBCURLConnection.ACCEPT, System.getenv("ACCEPT"));
		    print(c.getInputStream(), System.out, RS);
		    System.out.print(GS);
		    return;}
	    if (args.length>1) for (int i=1; i<args.length; i++) {
		    if (args[i].trim().equals("")) break;
		    URLConnection c = (new URL(String.format("%s?%s", p[0], args[i]))).openConnection();
		    c.setRequestProperty(JDBCURLConnection.ACCEPT, System.getenv("ACCEPT"));
		    print(c.getInputStream(), System.out, RS);
		    System.out.print(GS);}
	    if (args.length==1 && commands.size()==0) for (BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in)); (line=stdin.readLine())!=null;) {
		    if (line.trim().equals("")) break;
		    URLConnection c = (new URL(String.format("%s?%s", p[0], line))).openConnection();
		    c.setRequestProperty(JDBCURLConnection.ACCEPT, System.getenv("ACCEPT"));
		    print(c.getInputStream(), System.out, RS);
		    System.out.print(GS);}}
	catch (Exception e) {e.printStackTrace(); System.exit(1);}}

    public static void print (InputStream in, PrintStream out, char delim) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) {out.print(l); out.print(delim);}}}
