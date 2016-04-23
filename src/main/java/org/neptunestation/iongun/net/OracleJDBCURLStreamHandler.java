package org.neptunestation.iongun.net;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class OracleJDBCURLStreamHandler extends JDBCURLStreamHandler {
    static {SqlURLStreamHandlerFactory.registerStreamHandler(new OracleJDBCURLStreamHandler());}

    @Override
    public boolean acceptsProtocol (String protocol) {
	if (Arrays.asList("oracle",
			  "ora").contains(protocol)) return true;
	return false;}

    @Override
    protected URLConnection openConnection (URL url) throws IOException {
	return new JDBCURLConnection(url) {
	    @Override
	    protected Connection getConnection () {
		return null;}};}}
    
