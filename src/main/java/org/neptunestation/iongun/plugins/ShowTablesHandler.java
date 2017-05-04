package org.neptunestation.iongun.util;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.sql.*;
import org.neptunestation.iongun.util.*;

public class ShowTablesHandler implements QueryHandler {
    public boolean accepts (String q) {return "show-tables".equals(q);}
    public void setProperties (Map<String, List<String>> properties) {}
    public void handle (Connection c, String q, ResultSetHandler h, PrintStream o) {
	try (ResultSet r = c.getMetaData().getTables(null, null, null, null)) {
	    if (r!=null) h.print(r, o);}
	catch (Exception e) {throw new RuntimeException(e);}}}
