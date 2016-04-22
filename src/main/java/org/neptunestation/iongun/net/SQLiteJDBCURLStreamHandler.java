package iongun.net;

import iongun.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class SQLiteJDBCURLStreamHandler extends JDBCURLStreamHandler {
    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    protected Connection getConnection () throws SQLException {
		return DriverManager.getConnection(String.format("jdbc:sqlite:%s", url.getPath().split("/")[1]));}};}}

