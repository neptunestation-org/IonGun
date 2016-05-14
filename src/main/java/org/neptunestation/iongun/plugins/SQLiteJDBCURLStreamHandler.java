package org.neptunestation.iongun.plugins;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class SQLiteJDBCURLStreamHandler extends JDBCURLStreamHandler {
    static {SqlURLStreamHandlerFactory.registerStreamHandler(new SQLiteJDBCURLStreamHandler());}

    @Override
    public boolean acceptsProtocol (String protocol) {
	if (Arrays.asList("sqlite",
			  "sqlite2",
			  "sqlite3").contains(protocol)) return true;
	return false;}

    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    protected Connection getConnection () throws SQLException {
		return DriverManager.getConnection(String.format("jdbc:sqlite:%s", url.getPath().split("/")[1]));}};}}

