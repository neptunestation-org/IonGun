package org.neptunestation.iongun.sql;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public abstract class AbstractQueryHandler implements QueryHandler {
    public boolean accepts (String q) {return true;}
    public void setProperties (Map<String, List<String>> properties) {}
    public void handle (Connection c, String q, ResultSetHandler h, PrintStream o) {
	try (Statement s = c.createStatement();
	     AutoCloseableArrayList<Boolean> b = new AutoCloseableArrayList(s.execute(q));
	     ResultSet r = b.get(0) ? s.getResultSet() : null) {
	    if (r!=null) h.print(r, o);}
	catch (Exception e) {throw new RuntimeException(e);}}}
