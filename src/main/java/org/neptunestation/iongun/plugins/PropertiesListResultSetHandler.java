package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class PropertiesListResultSetHandler implements ResultSetHandler {
    static {ResultSetHandlerFactory.register(new PropertiesListResultSetHandler());}

    @Override
    public boolean accepts (String mimeType) {
	return "text/properties-list".equalsIgnoreCase(mimeType);}

    @Override
    public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (Properties p : Util.asIterable(Util.asIterable(r))) p.list(out);}}



