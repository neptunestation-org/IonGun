package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.net.*;
import org.neptunestation.iongun.plugins.*;
import org.neptunestation.iongun.util.*;

public class IonGun {
    public static void main (String[] args) {
	URL.setURLStreamHandlerFactory(new SQLUrlStreamHandlerFactory());
	try {for (String s : args) print((new URL(s)).openStream(), System.out);}
	catch (Exception e) {System.exit(1);}}

    public static void print (InputStream in, PrintStream out) throws IOException {
	print(in, out, (char)29);}

    public static void print (InputStream in, PrintStream out, char delim) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) out.println(l);
	out.print(delim);}}



