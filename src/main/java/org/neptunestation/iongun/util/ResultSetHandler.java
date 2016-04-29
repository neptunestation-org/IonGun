package org.neptunestation.iongun.util;

import java.io.*;
import java.sql.*;

public interface ResultSetHandler {
    boolean accepts (String mimeType);
    void print (ResultSet r, PrintStream out) throws IOException, SQLException;}
