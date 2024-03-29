package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import org.neptunestation.iongun.sql.*;
import org.neptunestation.iongun.util.*;

public class JSONResultSetRenderer extends AbstractResultSetHandler {
    @Override public boolean accepts (String mimeType) {
	return "text/json".equalsIgnoreCase(mimeType);}

    @Override public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (String p : Misc.asPropertiesIterable(Misc.asIterable(Misc.asIterable(r)))) out.println(p);}}
