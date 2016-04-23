package org.neptunestation.iongun.net;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class MySQLJDBCURLStreamHandler extends JDBCURLStreamHandler {
    static {SqlURLStreamHandlerFactory.registerStreamHandler(new MySQLJDBCURLStreamHandler());}

    @Override
    public boolean acceptsProtocol (String protocol) {
	if (Arrays.asList("mysql",
			  "mysqls",
			  "mysqlssl").contains(protocol)) return true;
	return false;}

    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    @Override
	    protected Connection getConnection () throws SQLException {
		return null;}};}}
