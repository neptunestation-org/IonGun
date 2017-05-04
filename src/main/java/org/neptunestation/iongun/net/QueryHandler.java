package org.neptunestation.iongun.util;

import java.io.*;
import java.sql.*;

public interface QueryHandler {
    boolean accepts (String query);
    void handle (Connection c, String q, ResultSetHandler h, PrintStream out);}
