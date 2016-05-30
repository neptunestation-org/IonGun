package org.neptunestation.iongun.util;

import java.io.*;
import java.net.*;

public class SQLURLStreamHandlerFactory extends JDBCURLStreamHandlerFactory {
    @Override
    public URLStreamHandler createURLStreamHandler (String protocol) {
	if ("sql".equals(protocol))
	    return new URLStreamHandler () {
		String vendor;
		@Override
		protected void parseURL (URL u, String spec, int start, int end) {
		    int colon = spec.indexOf(":", start);
		    vendor = spec.substring(start, colon);
		    String url = "";
		    start = colon;
		    super.parseURL(u, spec, start+1, end);}
		@Override
		protected URLConnection openConnection (final URL u) throws IOException {
		    try {
			return (new URL("postgresql".equals(vendor) ?
					String.format("jdbc:postgresql://%s%s?%s", u.getAuthority(), u.getPath(), u.getQuery()) :
					"sqlite".equals(vendor) ?
					String.format("jdbc:sqlite:%s?%s", u.getPath(), u.getQuery()) :
					null)).openConnection();}
		    catch (Exception e) {throw new IOException(e);}}};
	return super.createURLStreamHandler(protocol);}}
