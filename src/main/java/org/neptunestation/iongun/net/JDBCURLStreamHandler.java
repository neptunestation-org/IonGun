package org.neptunestation.iongun.net;

import java.net.*;

public abstract class JDBCURLStreamHandler extends URLStreamHandler {
    public abstract boolean accepts (String protocol);}
