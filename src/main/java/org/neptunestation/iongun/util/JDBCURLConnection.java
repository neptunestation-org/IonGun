package iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public abstract class JDBCURLConnection extends URLConnection {
    public JDBCURLConnection (URL url) {
	super(url);}
    @Override
    public synchronized void connect () throws IOException {
	try (Connection c = getConnection()) {connected = true;}
	catch (Exception e) {throw new RuntimeException(e);}}
    protected abstract Connection getConnection () throws SQLException;
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
			for (Map<String, Util.SQLValue> p : Util.asIterable(r))
			    out.println(p.toString());
			out.close();}
		    catch (Exception e) {throw new RuntimeException(e);}}}).start();
	return in;}
    @Override
    public synchronized Object getContent () throws IOException {
	return null;}}
