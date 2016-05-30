package org.neptunestation.iongun.plugins;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class SQLiteJDBCURLStreamHandler extends JDBCURLStreamHandler {
    @Override
    public boolean accepts (String protocol) {
	return (Arrays.asList("sqlite", "sqlite2", "sqlite3").contains(protocol));}

    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    protected Connection getConnection () throws SQLException {
		return DriverManager.getConnection(String.format("jdbc:sqlite:%s", url.getPath().split("/")[1]));}};}}

