package org.neptunestation.iongun.sql;

import java.io.*;
import java.sql.*;
import java.util.*;

public interface QueryHandler {
    boolean accepts (String query);
    void setProperties (Map<String, List<String>> properties);
    void handle (Connection c, String q, ResultSetHandler h, PrintStream out);}
