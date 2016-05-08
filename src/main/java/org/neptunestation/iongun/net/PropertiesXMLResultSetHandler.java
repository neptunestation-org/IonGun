package org.neptunestation.iongun.net;

import java.io.*;
import java.sql.*;
import java.util.*;
import org.neptunestation.iongun.util.*;

public class PropertiesXMLResultSetHandler implements ResultSetHandler {
    static {ResultSetHandlerFactory.register(new PropertiesXMLResultSetHandler());}

    @Override
    public boolean accepts (String mimeType) {
	return "text/properties-xml".equalsIgnoreCase(mimeType);}

    @Override
    public void print (ResultSet r, PrintStream out) throws IOException, SQLException {
	for (Properties p : Util.asIterable(Util.asIterable(r))) p.storeToXML(out, "Made with IonGun");}}
