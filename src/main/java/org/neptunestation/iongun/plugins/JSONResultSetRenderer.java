package org.neptunestation.iongun.plugins;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class JSONResultSetRenderer extends AbstractResultSetHandler {
    @Override
    public boolean accepts (String mimeType) {
	return "text/json".equalsIgnoreCase(mimeType);}

    @Override
    public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (String p : Util.asPropertiesIterable(Util.asIterable(Util.asIterable(r)))) out.println(p);}}
