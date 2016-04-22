package iongun.util;

import java.net.*;

public abstract class JDBCURLStreamHandler extends URLStreamHandler {
    protected URL translate (URL url) {
	return url;}}
