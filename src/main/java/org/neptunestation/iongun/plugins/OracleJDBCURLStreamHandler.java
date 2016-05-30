package org.neptunestation.iongun.plugins;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class OracleJDBCURLStreamHandler extends JDBCURLStreamHandler {
    @Override
    public boolean accepts (String protocol) {
	return (Arrays.asList("oracle","ora").contains(protocol));}

    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    @Override
	    protected Connection getConnection () {
		return null;}};}}
    
