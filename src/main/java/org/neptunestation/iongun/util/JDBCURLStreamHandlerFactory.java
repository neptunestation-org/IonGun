package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;
import org.neptunestation.iongun.util.*;

public class JDBCURLStreamHandlerFactory implements URLStreamHandlerFactory {
    public String getUrl (URL u, String subname) {
	return String.format("%s:%s:%s%s", "jdbc", subname,
			     u.getHost().equals("") ? "" :
			     u.getPort()<0 ? String.format("//%s", u.getHost()) :
			     String.format("//%s:%s", u.getHost(), u.getPort()),
			     u.getPath());}
    @Override
    public URLStreamHandler createURLStreamHandler (String protocol) {
	if (Arrays.asList("sql", "jdbc").contains(protocol))
	    return new URLStreamHandler () {
		String subname;
		@Override
		protected void parseURL (URL u, String spec, int start, int end) {
		    int colon = spec.indexOf(":", start);
		    subname = spec.substring(start, colon);
		    start = colon;
		    super.parseURL(u, spec, start+1, end);}
		@Override
		protected URLConnection openConnection (final URL u) {
		    return new URLConnection (u) {
			Map<String, List<String>> properties;
			@Override
			public String getContentType () {
			    for (String s : properties.get("Accept")) return s;
			    return "text/json";}
			@Override
			public synchronized void connect () {
			    properties = getRequestProperties();
			    try (Connection c =
				 u.getUserInfo()==null ?
				 DriverManager.getConnection(getUrl(u, subname)) :
				 DriverManager.getConnection(getUrl(u, subname),
							     u.getUserInfo().split(":")[0],
							     u.getUserInfo().split(":")[1])) {
				connected = true;}
			    catch (Exception e) {throw new RuntimeException(e);}}
			@Override
			public InputStream getInputStream () throws IOException {
			    if (!connected) connect();
			    final PipedInputStream in = new PipedInputStream();
			    final PrintStream out = new PrintStream(new PipedOutputStream(in));
			    new Thread(new Runnable () {
				    public void run () {
					try (Connection c =
					     u.getUserInfo()==null ?
					     DriverManager.getConnection(getUrl(u, subname)) :
					     DriverManager.getConnection(getUrl(u, subname),
									 u.getUserInfo().split(":")[0],
									 u.getUserInfo().split(":")[1]);
					     Statement s = c.createStatement();
					     ResultSet r = s.executeQuery(url.getQuery())) {
					    ResultSetHandlerFactory.createResultSetHandler(getContentType()).print(r,out);
					    out.close();}
					catch (Exception e) {throw new RuntimeException(e);}}}).start();
			    return in;}};}};
	return null;}}
