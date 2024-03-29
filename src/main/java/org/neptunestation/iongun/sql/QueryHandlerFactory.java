package org.neptunestation.iongun.sql;

import java.util.*;

public abstract class QueryHandlerFactory {
    private static ServiceLoader<QueryHandler> loader = ServiceLoader.load(QueryHandler.class);

    public static QueryHandler createQueryHandler (String mimeType) {
	return createQueryHandler(mimeType, new HashMap<String, List<String>>());}

    public static QueryHandler createQueryHandler (String mimeType, Map<String, List<String>> properties) {
	for (Iterator<QueryHandler> it = loader.iterator(); it.hasNext();) {
	    QueryHandler h = it.next();
	    h.setProperties(properties);
	    if (h.accepts(mimeType)) return h;}
	throw new IllegalStateException("No QueryHandler instances registered");}}
