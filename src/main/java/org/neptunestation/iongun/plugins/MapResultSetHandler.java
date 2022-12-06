package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.sql.*;
import org.neptunestation.iongun.util.*;

public class MapResultSetHandler extends AbstractResultSetHandler {
    @Override public boolean accepts (String mimeType) {
	return "text/map".equalsIgnoreCase(mimeType);}

    @Override public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (Map<String, Misc.SQLValue> p : Misc.asIterable(r)) out.println(p.toString());}}



