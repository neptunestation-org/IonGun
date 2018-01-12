package org.neptunestation.iongun.net;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;
import org.neptunestation.iongun.sql.*;
import org.neptunestation.iongun.util.*;

/**
 * <code>JDBCURLStreamHandlerFactory</code> is a {@link URLStreamHandlerFactory} for JDBC URLs.
 *
 */
public class JDBCURLStreamHandlerFactory implements URLStreamHandlerFactory {
    /**
     * HTTP "Accept" header name
     */
    public static final String ACCEPT = "Accept";

    /**
     * File separator
     */
    public static final String FS = "FS";

    /**
     * Group separator
     */
    public static final String GS = "GS";

    /**
     * Record separator
     */
    public static final String RS = "RS";

    /**
     * Unit separator
     */
    public static final String US = "US";

    protected List<JDBCURLStreamHandler>
	streamHandlers = new ArrayList<>();

    protected List<QueryHandler>
	queryHandlers = new ArrayList<>();

    /**
     * No-argument constructor
     */
    public JDBCURLStreamHandlerFactory () {
	queryHandlers.add(new AbstractQueryHandler(){});
	queryHandlers.add(new ShowTablesHandler());
	streamHandlers.add(new AbstractURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sql".equals(protocol);}});
	streamHandlers.add(new AbstractURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sqlite".equals(protocol);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    return (new URL(String.format("jdbc:sqlite:%s?%s", u.getPath(), u.getQuery()))).openConnection();}});
	streamHandlers.add(new AbstractURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sqlite2".equals(protocol);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    return (new URL(String.format("sqlite:%s", schemeSpecificPart))).openConnection();}});
	streamHandlers.add(new AbstractURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sqlite3".equals(protocol);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    return (new URL(String.format("sqlite:%s", schemeSpecificPart))).openConnection();}});
	streamHandlers.add(new AbstractURLStreamHandler() {
		String subname;
		@Override
		public boolean accepts (String protocol) {return "jdbc".equals(protocol);}
		@Override
		protected void parseURL (final URL u, final String spec, final int start, final int end) {
		    int delimiter = spec.indexOf(":", start);
		    subname = spec.substring(start, delimiter);
		    super.parseURL(u, spec, delimiter+1, end);}
		@Override
		protected URLConnection openConnection (final URL u) {
		    return new URLConnection (u) {
			@Override
			public String getContentType () {
			    for (String s : getRequestProperties().get(ACCEPT)) return s;
			    System.out.println("CSV!");
			    return "text/csv";}
			@Override
			public synchronized void connect () {}
			@Override
			public InputStream getInputStream () throws IOException {
			    if (!connected) connect();
			    final PipedInputStream in = new PipedInputStream();
			    final PrintStream out = new PrintStream(new PipedOutputStream(in));
			    Thread t = new Thread(()->{
				    try (Connection c = getConnection(u, subname)) {
					for (QueryHandler qh : queryHandlers)
					    if (qh.accepts(u.getQuery())) {
						qh.handle(c, u.getQuery(), ResultSetHandlerFactory.createResultSetHandler(getContentType(), getRequestProperties()), out);
						break;}
					out.close();}
				    catch (Exception e) {throw new RuntimeException(e);}});
			    t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				    public void uncaughtException (Thread th, Throwable ex) {
					ex.printStackTrace(out);
					out.close();}});
			    t.start();
			    return in;}};}});}

    protected String getUrl (final URL u, final String subname) {
	return String.format("%s:%s:%s%s", "jdbc", subname,
			     u.getHost().equals("") ? "" :
			     u.getPort()<0 ? String.format("//%s", u.getHost()) :
			     String.format("//%s:%s", u.getHost(), u.getPort()),
			     u.getPath());}

    protected Connection getConnection (final URL u, final String subname) throws SQLException {
	return u.getUserInfo()==null ?
	    DriverManager.getConnection(getUrl(u, subname)) :
	    DriverManager.getConnection(getUrl(u, subname), u.getUserInfo().split(":")[0], u.getUserInfo().split(":")[1]);}

    @Override
    public URLStreamHandler createURLStreamHandler (final String protocol) {
	for (JDBCURLStreamHandler sh : streamHandlers)
	    if (sh.accepts(protocol)) return sh;
	return null;}}
