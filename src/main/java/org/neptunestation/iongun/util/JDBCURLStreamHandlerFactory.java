package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;
import org.neptunestation.iongun.util.*;

public class JDBCURLStreamHandlerFactory implements URLStreamHandlerFactory {
    static class DefaultTranslator {
	public String vendor;
	public DefaultTranslator (String v) {vendor = v;}
	public String translate (URL u) {return String.format("jdbc:%s://%s%s?%s", vendor, u.getAuthority(), u.getPath(), u.getQuery());}}

    static class SQLiteTranslator extends DefaultTranslator {
	public SQLiteTranslator (String v) {super(v);}
	public String translate (URL u) {return String.format("jdbc:%s:%s?%s", vendor, u.getAuthority(), u.getQuery());}}

    static class AutoCloseableArrayList<E> extends ArrayList<E> implements AutoCloseable {
	AutoCloseableArrayList (E... items) {super.addAll(Arrays.asList(items));}
	public void close () {}}

    static List<QueryHandler> handlers = Arrays.asList(new QueryHandler() {
	    public boolean accepts (String q) {return "show-tables".equals(q);}
	    public void handle (Connection c, String q, ResultSetHandler h, PrintStream o) {
		try (ResultSet r = c.getMetaData().getTables(null, null, null, null)) {
		    if (r!=null) h.print(r, o);}
		catch (Exception e) {throw new RuntimeException(e);}}},
	new QueryHandler () {
	    public boolean accepts (String q) {return true;}
	    public void handle (Connection c, String q, ResultSetHandler h, PrintStream o) {
		try (Statement s = c.createStatement();
		     AutoCloseableArrayList<Boolean> b = new AutoCloseableArrayList(s.execute(q));
		     ResultSet r = b.get(0) ? s.getResultSet() : null) {
		    if (r!=null) h.print(r, o);}
		catch (Exception e) {throw new RuntimeException(e);}}});

    static Map<List<String>, DefaultTranslator> vendors = new HashMap<>();
    static Set<String> allVendors = new HashSet<>();

    static {
	vendors.put(Arrays.asList("sqlite", "sqlite2"), new SQLiteTranslator("sqlite"));
	vendors.put(Arrays.asList("sqlite3"), new SQLiteTranslator("sqlite"));
	vendors.put(Arrays.asList("mysql", "mysqls", "mysqlssl"), new DefaultTranslator("mysql"));
	vendors.put(Arrays.asList("oracle", "ora"), new DefaultTranslator("oracle"));
	vendors.put(Arrays.asList("postgresql", "pg", "pgsql", "postgres"), new DefaultTranslator("postgresql"));
	vendors.put(Arrays.asList("postgresqlssl", "pgs", "pgsqlssl", "postgresssl", "pgssl", "postgresqls", "pgsqls", "postgress"), new DefaultTranslator("postgresql"));
	for (List<String> v : vendors.keySet()) allVendors.addAll(v);}

    public String getUrl (URL u, String subname) {
	return String.format("%s:%s:%s%s", "jdbc", subname,
			     u.getHost().equals("") ? "" :
			     u.getPort()<0 ? String.format("//%s", u.getHost()) :
			     String.format("//%s:%s", u.getHost(), u.getPort()),
			     u.getPath());}

    public Connection getConnection (URL u, String subname) throws SQLException {
	return u.getUserInfo()==null ?
	    DriverManager.getConnection(getUrl(u, subname)) :
	    DriverManager.getConnection(getUrl(u, subname), u.getUserInfo().split(":")[0], u.getUserInfo().split(":")[1]);}

    @Override
    public URLStreamHandler createURLStreamHandler (String protocol) {
	if (allVendors.contains(protocol))
	    return new URLStreamHandler () {
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    try {
			return (new URL(String.format("sql:%s:%s%s?%s",
						      u.getProtocol(),
						      u.getAuthority()==null ? "" : String.format("//%s", u.getAuthority()),
						      u.getPath(),
						      u.getQuery()))).openConnection();}
		    catch (Exception e) {throw new IOException(e);}}};
	if ("sql".equals(protocol))
	    return new URLStreamHandler () {
		String vendor;
		protected DefaultTranslator getTranslator (String vendor) {
		    for (Map.Entry<List<String>, DefaultTranslator> e : vendors.entrySet()) if (e.getKey().contains(vendor)) return e.getValue();
		    return new DefaultTranslator(vendor);}
		@Override
		protected void parseURL (URL u, String spec, int start, int end) {
		    int colon = spec.indexOf(":", start);
		    vendor = spec.substring(start, colon);
		    String url = "";
		    start = colon;
		    super.parseURL(u, spec, start+1, end);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    try {return (new URL(getTranslator(vendor).translate(u))).openConnection();}
		    catch (Exception e) {throw new IOException(e);}}};
	if ("jdbc".equals(protocol))
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
			    try (Connection c = getConnection(u, subname)) {connected = true;}
			    catch (Exception e) {throw new RuntimeException(e);}}
			@Override
			public InputStream getInputStream () throws IOException {
			    if (!connected) connect();
			    final PipedInputStream in = new PipedInputStream();
			    final PrintStream out = new PrintStream(new PipedOutputStream(in));
			    new Thread(()->{
				    try (Connection c = getConnection(u, subname)) {
					for (QueryHandler q : handlers)
					    if (q.accepts(u.getQuery())) {
						q.handle(c, u.getQuery(), ResultSetHandlerFactory.createResultSetHandler(getContentType()), out);
						break;}
					out.close();}
				    catch (Exception e) {throw new RuntimeException(e);}}).start();
			    return in;}};}};
	return null;}}
