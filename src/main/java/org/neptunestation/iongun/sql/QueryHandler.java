package org.neptunestation.iongun.sql;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * A <code>QueryHandler</code> processes queries.
 *
 * Using a {@link Connection} it processes a {@link String} query,
 * writing the results to a {@link PrintStream} in a format determined
 * by a {@link ResultSetHandler}.
 */
public interface QueryHandler {
    /**
     * Indicates that this <code>QueryHandler</code> can handle a
     * given dburl
     *
     * @return boolean indicating acceptance
     */
    boolean accepts (String query);

    /**
     * Provides properties that modify the behavior this
     * <code>QueryHandler</code>
     *
     * @param properties a {@link Map} of multivalued properties
     */
    void setProperties (Map<String, List<String>> properties);

    /**
     * Process a query
     *
     * @param c a {@link Connection}
     * @param q query string
     * @param h a {@link ResultSetHandler}
     * @param out a {@link PrintStream}
     */
    void handle (Connection c, String q, ResultSetHandler h, PrintStream out);}
