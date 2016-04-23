package org.neptunestation.iongun.util;

import java.net.*;

public abstract class JDBCURLStreamHandler extends URLStreamHandler {
    public abstract boolean acceptsProtocol (String protocol);
    protected URL translate (URL url) {
	return url;}}
