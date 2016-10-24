package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;

public abstract class JDBCURLConnection extends URLConnection {
    protected String accept;
    protected ResultSetHandlerFactory factory;

    protected abstract Connection getConnection () throws SQLException;

    public static final String ACCEPT = "Accept";

    public JDBCURLConnection (URL url) {
	super(url);}

    public void setResultSetHandlerFactory (ResultSetHandlerFactory factory) {
	this.factory = factory;}

    @Override
    public synchronized void connect () throws IOException {
	accept = getRequestProperty(ACCEPT);
	try (Connection c = getConnection()) {connected = true;}
	catch (Exception e) {throw new RuntimeException(e);}}

    @Override
    public synchronized InputStream getInputStream () throws IOException {
	if (!connected) connect();
	PipedInputStream in = new PipedInputStream();
	final PrintStream out = new PrintStream(new PipedOutputStream(in));
	new Thread(new Runnable () {
		public void run () {
		    try (Connection c = getConnection();
			 Statement s = c.createStatement();
			 ResultSet r = s.executeQuery(url.getQuery())) {
			factory.createResultSetHandler(accept).print(r, out);
			out.close();}
		    catch (Exception e) {throw new RuntimeException(e);}}}).start();
	return in;}}
