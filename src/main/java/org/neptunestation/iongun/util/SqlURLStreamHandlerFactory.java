package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;
import org.neptunestation.iongun.util.*;

public class SqlURLStreamHandlerFactory implements URLStreamHandlerFactory {
    private static List<JDBCURLStreamHandler> handlers = new CopyOnWriteArrayList<>();

    public static void registerStreamHandler (JDBCURLStreamHandler handler) {
	handlers.add(handler);}

    @Override
    public URLStreamHandler createURLStreamHandler (String protocol) {
	for (JDBCURLStreamHandler h : handlers) if (h.acceptsProtocol(protocol)) return h;
	return null;}}
