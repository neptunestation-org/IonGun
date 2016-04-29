package org.neptunestation.iongun.net;

import java.io.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;
import javax.sql.rowset.*;
import org.neptunestation.iongun.util.*;

public class WebXMLResultSetHandler implements ResultSetHandler {
    static {ResultSetHandlerFactory.register(new WebXMLResultSetHandler());}

    @Override
    public boolean accepts (String mimeType) {
	return "text/web-xml".equalsIgnoreCase(mimeType);}

    @Override
    public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	StringWriter buffer = new StringWriter();
	WebRowSet wrs = RowSetProvider.newFactory().createWebRowSet();
	wrs.writeXml(r, buffer);
	out.print(buffer.toString());}}




