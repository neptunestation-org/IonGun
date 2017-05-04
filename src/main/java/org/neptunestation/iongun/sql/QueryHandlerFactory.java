package org.neptunestation.iongun.sql;

import java.util.*;
import java.util.concurrent.*;

public abstract class QueryHandlerFactory {
    private static ServiceLoader<QueryHandler> loader = ServiceLoader.load(QueryHandler.class);

    public static QueryHandler createQueryHandler (String mimeType) {
	return createQueryHandler(mimeType, new HashMap<String, List<String>>());}

    public static QueryHandler createQueryHandler (String mimeType, Map<String, List<String>> properties) {
	for (Iterator<QueryHandler> it = loader.iterator(); it.hasNext();) {
	    QueryHandler h = (QueryHandler)it.next();
	    h.setProperties(properties);
	    if (h.accepts(mimeType)) return h;}
	throw new IllegalStateException("No QueryHandler instances registered");}}
