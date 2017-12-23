package org.neptunestation.iongun.sql;

import java.util.*;
import java.util.concurrent.*;

public abstract class ResultSetHandlerFactory {
    private static ServiceLoader<ResultSetHandler> loader = ServiceLoader.load(ResultSetHandler.class);

    public static ResultSetHandler createResultSetHandler (String mimeType) {
	return createResultSetHandler(mimeType, new HashMap<String, List<String>>());}

    public static ResultSetHandler createResultSetHandler (String mimeType, Map<String, List<String>> properties) {
	for (Iterator<ResultSetHandler> it = loader.iterator(); it.hasNext();) {
	    ResultSetHandler h = it.next();
	    h.setProperties(properties);
	    if (h.accepts(mimeType)) return h;}
	throw new IllegalStateException(String.format("No ResultSetHandler instances registered for MimeType:  %s", mimeType));}}
