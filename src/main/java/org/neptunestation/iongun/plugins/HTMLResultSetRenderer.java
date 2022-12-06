package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import org.neptunestation.iongun.sql.*;

public class HTMLResultSetRenderer extends AbstractResultSetHandler {
    @Override public boolean accepts (String mimeType) {
	return "text/html".equalsIgnoreCase(mimeType);}

    @Override public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	out.println("<table border=\"1\">");
	if (r==null) return;
	ResultSetMetaData m = r.getMetaData();
	out.println("<tr>");
	for (int i=1; i<=m.getColumnCount(); i++) out.println(String.format("<th>%s</th>", m.getColumnName(i)));
	out.println("</tr>");
	out.println("<tr>");
	while (r.next()) for (int i=1; i<=m.getColumnCount(); i++) out.println(String.format("<td>%s</td>", r.getString(i)));
	out.println("</tr>");
	out.println("</table>");}}
