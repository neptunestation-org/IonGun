package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class MapResultSetHandler implements ResultSetHandler {
    @Override
    public boolean accepts (String mimeType) {
	return "text/map".equalsIgnoreCase(mimeType);}

    @Override
    public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (Map<String, Util.SQLValue> p : Util.asIterable(r)) out.println(p.toString());}}



