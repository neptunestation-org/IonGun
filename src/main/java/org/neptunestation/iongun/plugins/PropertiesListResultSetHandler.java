package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.sql.*;
import org.neptunestation.iongun.util.*;

public class PropertiesListResultSetHandler extends AbstractResultSetHandler {
    @Override
    public boolean accepts (String mimeType) {
	return "text/properties-list".equalsIgnoreCase(mimeType);}

    @Override
    public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (Properties p : Misc.asIterable(Misc.asIterable(r))) p.list(out);}}



