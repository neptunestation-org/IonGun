package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;

public abstract class JDBCURLConnection extends URLConnection {
    protected String accept;
    protected Properties properties = new Properties();

    public JDBCURLConnection (URL url) {
	super(url);}

    protected abstract Connection getConnection () throws SQLException;

    @Override
    public synchronized void connect () throws IOException {
	accept = getRequestProperty("Accept");
	try (Connection c = getConnection()) {connected = true;}
	catch (Exception e) {throw new RuntimeException(e);}}

    @Override
    public synchronized InputStream getInputStream () throws IOException {
	if (!connected) connect();
	PipedInputStream in = new PipedInputStream();
	PrintStream out = new PrintStream(new PipedOutputStream(in));
	new Thread(new Runnable () {
		public void run () {
		    try (Connection c = getConnection();
			 Statement s = c.createStatement();
			 ResultSet r = s.executeQuery(url.getQuery())) {
			if ("text/map".equalsIgnoreCase(accept)) {
			    for (Map<String, Util.SQLValue> p : Util.asIterable(r)) out.println(p.toString());}
			if ("text/properties".equalsIgnoreCase(accept)) {
			    for (Properties p : Util.asIterable(Util.asIterable(r))) out.println(p.toString());}
			if ("text/properties-list".equalsIgnoreCase(accept)) {
			    for (Properties p : Util.asIterable(Util.asIterable(r))) p.list(out);}
			if ("text/properties-xml".equalsIgnoreCase(accept)) {
			    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			    for (Properties p : Util.asIterable(Util.asIterable(r))) p.storeToXML(buffer, "made with ionun");
			    out.println(buffer.toString());}
			if ("text/web-xml".equalsIgnoreCase(accept)) {
			    StringWriter buffer = new StringWriter();
			    WebRowSet wrs = RowSetProvider.newFactory().createWebRowSet();
			    wrs.writeXml(r, buffer);
			    out.print(buffer.toString());}
			out.close();}
		    catch (Exception e) {throw new RuntimeException(e);}}}).start();
	return in;}

    @Override
    public synchronized Object getContent () throws IOException {
	return null;}}
