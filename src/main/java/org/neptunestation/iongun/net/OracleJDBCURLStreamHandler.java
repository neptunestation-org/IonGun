package iongun.net;

import iongun.util.*;
import java.io.*;
import java.net.*;
import java.sql.*;

public class OracleJDBCURLStreamHandler extends JDBCURLStreamHandler {
    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    @Override
	    protected Connection getConnection () {
		return null;}};}}
    
