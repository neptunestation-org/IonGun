package org.neptunestation.iongun.net;

import java.net.*;

/**
 * <code>JDBCURLStreamHandler</code> is a {@link URLStreamHandler} for
 * JDBC URLs.
 */
public abstract class JDBCURLStreamHandler extends URLStreamHandler {

    /**
     * <code>accepts</code> indicates whether or not this {@link
     * JDBCURLStreamHandler} can handle URLs of a particular protocol.
     *
     * @param protocol the protocol name
     *
     * @return boolean indicating acceptance or non-acceptance
     */
    public abstract boolean accepts (String protocol);}
