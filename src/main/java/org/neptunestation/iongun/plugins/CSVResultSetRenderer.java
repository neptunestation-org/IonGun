package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import org.neptunestation.iongun.net.*;
import org.neptunestation.iongun.sql.*;

public class CSVResultSetRenderer extends AbstractResultSetHandler {
    @Override public boolean accepts (String mimeType) {
	return "text/csv".equalsIgnoreCase(mimeType);}

    @Override public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (String rs : properties.get(JDBCURLStreamHandlerFactory.RS))
	    for (String us : properties.get(JDBCURLStreamHandlerFactory.US)) {
		if (r==null) return;
		ResultSetMetaData m = r.getMetaData();
		for (int i=1; i<m.getColumnCount(); i++)
		    out.print(String.format("%s%s", m.getColumnName(i), us));
		out.print(String.format("%s", m.getColumnName(m.getColumnCount())));
		out.print(rs);
		while (r.next()) {
		    for (int i=1; i<m.getColumnCount(); i++)
			out.print(String.format("%s%s", r.getString(i), us));
		    out.print(String.format("%s", r.getString(m.getColumnCount())));
		    out.print(rs);}}}}
