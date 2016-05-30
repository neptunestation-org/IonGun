package org.neptunestation.iongun.plugins;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class MySQLJDBCURLStreamHandler extends JDBCURLStreamHandler {
    @Override
    public boolean accepts (String protocol) {
	return (Arrays.asList("mysql", "mysqls", "mysqlssl").contains(protocol));}

    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    @Override
	    protected Connection getConnection () throws SQLException {
		return null;}};}}
