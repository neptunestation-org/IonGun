package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import org.neptunestation.iongun.util.*;

public class SQLHandlerFactory implements URLStreamHandlerFactory {
    private static ServiceLoader<JDBCURLStreamHandler> loader = ServiceLoader.load(JDBCURLStreamHandler.class);

    @Override
    public URLStreamHandler createURLStreamHandler (String protocol) {
	for (Iterator<JDBCURLStreamHandler> it = loader.iterator(); it.hasNext();) {
	    JDBCURLStreamHandler h = (JDBCURLStreamHandler)it.next();
	    if (h.accepts(protocol)) return h;}
	return null;}}
