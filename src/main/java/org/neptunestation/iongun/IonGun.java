package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.plugins.*;
import org.neptunestation.iongun.net.*;

/**
 * <code>IonGun</code> is an application class that queries a database
 * and emits the results.
 *
 * <p>Usage:<p>
 *
 * <p><em>[options]</em> java org.neptunestation.iongun.IonGun <em>DBURL</em>...</p>
 *
 * <p>Options:</p>
 * 
 * <dl>
 * <dt><pre>FS</pre></dt><dd>file separator (default:  INFORMATION SEPARATOR FOUR)</dd>
 * <dt><pre>GS</pre></dt><dd>group separator (default:  INFORMATION SEPARATOR THREE)</dd>
 * <dt><pre>RS</pre></dt><dd>record separator (default:  INFORMATION SEPARATOR TWO)</dd>
 * <dt><pre>US</pre></dt><dd>unit separator (default:  INFORMATION SEPARATOR ONE)</dd>
 * </dl>
 *
 * @see <a  href="https://en.wikipedia.org/wiki/Delimiter#ASCII_delimited_text">this entry</a> 
 * for the meaning of these items.
 * 
 * <p>A database is addressed using a <em>DBURL</em>.  A
 * <em>DBURL</em> has the following syntax:</p>
 *
 * <p><pre>[sql:]vendor://[JDBC subprotocol]:[JDBC subname][?sqlquery]</pre></p>
 *
 * <p>@see <a href="https://www.jcp.org/aboutJava/communityprocess/mrel/jsr221/index2.html" >the JDBC spec</a>
 * for the meaning of <em>subprotocol</em> and <em>subname</em>.</p>
 *
 * <p>Multiple <em>DBURL</em> entries can be provided as
 * arguments.  The results for each are emitted to standard output.
 * Results from different <em>DBURL</em> entries are separated
 * using the <em>group separator</em>.  Records and the fields
 * within them are delimite using the <em>record
 * separator</em> and <em>unit separator</em>.  Output is
 * terminated by the <em>file separator</em>.</p>
 */
public class IonGun {
    public static void main (final String[] args) {
	try {
	    char FS = System.getenv("FS")==null ? (char)28 : (char)Integer.parseInt(System.getenv("FS"));
	    char GS = System.getenv("GS")==null ? (char)29 : (char)Integer.parseInt(System.getenv("GS"));
	    char RS = System.getenv("RS")==null ? (char)30 : (char)Integer.parseInt(System.getenv("RS"));
	    char US = System.getenv("US")==null ? (char)31 : (char)Integer.parseInt(System.getenv("US"));
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
		first = false;}
	    System.out.print(FS);}
	catch (Exception e) {throw new RuntimeException(e);}}

    public static void print (final InputStream in, final PrintStream out, final char delim) throws IOException {
	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	String l;
	while ((l = br.readLine())!=null) {out.print(l); out.print(delim);}}}
