package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;

public abstract class DefaultURLStreamHandler extends JDBCURLStreamHandler {
    String schemeSpecificPart;
    @Override
    protected void parseURL (final URL u, final String spec, final int start, final int end) {
	schemeSpecificPart = spec.substring(start);
	super.parseURL(u, spec, start, end);}
    @Override
    protected URLConnection openConnection (final URL u) throws IOException {
	return (new URL(String.format("%s", schemeSpecificPart))).openConnection();}}
