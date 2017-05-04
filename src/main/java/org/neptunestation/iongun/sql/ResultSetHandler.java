package org.neptunestation.iongun.sql;

import java.io.*;
import java.sql.*;
import java.util.*;

public interface ResultSetHandler {
    boolean accepts (String mimeType);
    void setProperties (Map<String, List<String>> properties);
    void print (ResultSet r, PrintStream out) throws IOException, SQLException;}
