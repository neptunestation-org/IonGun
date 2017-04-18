package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;
import org.neptunestation.iongun.util.*;

public class JDBCURLStreamHandlerFactory implements URLStreamHandlerFactory {
    List<JDBCURLStreamHandler>
	streamHandlers = new ArrayList<>();

    List<QueryHandler>
	queryHandlers = new ArrayList<>();

    public JDBCURLStreamHandlerFactory () {
	queryHandlers.add(new ShowTablesHandler());
	queryHandlers.add(new DefaultQueryHandler());
	streamHandlers.add(new DefaultURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sql".equals(protocol);}
	    });
	streamHandlers.add(new DefaultURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sqlite".equals(protocol);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    return (new URL(String.format("jdbc:sqlite:%s?%s", u.getPath(), u.getQuery()))).openConnection();}});
	streamHandlers.add(new DefaultURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sqlite2".equals(protocol);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    return (new URL(String.format("sqlite:%s",schemeSpecificPart))).openConnection();}});
	streamHandlers.add(new DefaultURLStreamHandler() {
		@Override
		public boolean accepts (String protocol) {return "sqlite3".equals(protocol);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    return (new URL(String.format("sqlite:%s",schemeSpecificPart))).openConnection();}});
	streamHandlers.add(new DefaultURLStreamHandler() {
		String subname;
		@Override
		public boolean accepts (String protocol) {return "jdbc".equals(protocol);}
		@Override
		protected void parseURL (final URL u, final String spec, final int start, final int end) {
		    int newstart = spec.indexOf(":", start);
		    subname = spec.substring(start, newstart);
		    super.parseURL(u, spec, newstart+1, end);}
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
			    try (Connection c = getConnection(u, subname)) {connected = true;}
			    catch (Exception e) {throw new RuntimeException(e);}}
			@Override
			public InputStream getInputStream () throws IOException {
			    if (!connected) connect();
			    final PipedInputStream in = new PipedInputStream();
			    final PrintStream out = new PrintStream(new PipedOutputStream(in));
			    new Thread(()->{
				    try (Connection c = getConnection(u, subname)) {
					for (QueryHandler q : queryHandlers)
					    if (q.accepts(u.getQuery())) {
						q.handle(c, u.getQuery(), ResultSetHandlerFactory.createResultSetHandler(getContentType()), out);
						break;}
					out.close();}
				    catch (Exception e) {throw new RuntimeException(e);}}).start();
			    return in;}};}});}

    public String getUrl (final URL u, final String subname) {
	return String.format("%s:%s:%s%s", "jdbc", subname,
			     u.getHost().equals("") ? "" :
			     u.getPort()<0 ? String.format("//%s", u.getHost()) :
			     String.format("//%s:%s", u.getHost(), u.getPort()),
			     u.getPath());}

    public Connection getConnection (final URL u, final String subname) throws SQLException {
	return u.getUserInfo()==null ?
	    DriverManager.getConnection(getUrl(u, subname)) :
	    DriverManager.getConnection(getUrl(u, subname), u.getUserInfo().split(":")[0], u.getUserInfo().split(":")[1]);}

    @Override
    public URLStreamHandler createURLStreamHandler (final String protocol) {
	for (JDBCURLStreamHandler sh : streamHandlers)
	    if (sh.accepts(protocol)) return sh;
	return null;}}

abstract class DefaultURLStreamHandler extends JDBCURLStreamHandler {
    String schemeSpecificPart;
    @Override
    protected void parseURL (final URL u, final String spec, final int start, final int end) {
	schemeSpecificPart = spec.substring(start);
	super.parseURL(u, spec, start, end);}
    @Override
    protected URLConnection openConnection (final URL u) throws IOException {
	return (new URL(String.format("%s",schemeSpecificPart))).openConnection();}}

class AutoCloseableArrayList<E> extends ArrayList<E> implements AutoCloseable {
    AutoCloseableArrayList (E... items) {super.addAll(Arrays.asList(items));}
    public void close () {}}

class ShowTablesHandler implements QueryHandler {
    public boolean accepts (String q) {return "show-tables".equals(q);}
    public void handle (Connection c, String q, ResultSetHandler h, PrintStream o) {
	try (ResultSet r = c.getMetaData().getTables(null, null, null, null)) {
	    if (r!=null) h.print(r, o);}
	catch (Exception e) {throw new RuntimeException(e);}}}

class DefaultQueryHandler implements QueryHandler {
    public boolean accepts (String q) {return true;}
    public void handle (Connection c, String q, ResultSetHandler h, PrintStream o) {
	try (Statement s = c.createStatement();
	     AutoCloseableArrayList<Boolean> b = new AutoCloseableArrayList(s.execute(q));
	     ResultSet r = b.get(0) ? s.getResultSet() : null) {
	    if (r!=null) h.print(r, o);}
	catch (Exception e) {throw new RuntimeException(e);}}}
