package org.neptunestation.iongun.util;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DefaultQueryHandler implements QueryHandler {
    public boolean accepts (String q) {return true;}
    public void handle (Connection c, String q, ResultSetHandler h, PrintStream o) {
	try (Statement s = c.createStatement();
	     AutoCloseableArrayList<Boolean> b = new AutoCloseableArrayList(s.execute(q));
	     ResultSet r = b.get(0) ? s.getResultSet() : null) {
	    if (r!=null) h.print(r, o);}
	catch (Exception e) {throw new RuntimeException(e);}}}
