package org.neptunestation.iongun.net;

import java.io.*;
import java.net.*;

/**
 * <code>AbstractURLStreamHandler</code> is a {@link JDBCURLStreamHandler} 
 * with some of the details filled in.
 */
public abstract class AbstractURLStreamHandler extends JDBCURLStreamHandler {
    protected String schemeSpecificPart;

    @Override
    protected void parseURL (final URL u, final String spec, final int start, final int end) {
	schemeSpecificPart = spec.substring(start);
	super.parseURL(u, spec, start, end);}

    @Override
    protected URLConnection openConnection (final URL u) throws IOException {
	return (new URL(String.format("%s", schemeSpecificPart))).openConnection();}}
