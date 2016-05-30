package org.neptunestation.iongun.util;

import java.util.*;
import java.util.concurrent.*;

public abstract class ResultSetHandlerFactory {
    private static ServiceLoader<ResultSetHandler> loader = ServiceLoader.load(ResultSetHandler.class);

    public static ResultSetHandler createResultSetHandler (String mimeType) {
	for (Iterator<ResultSetHandler> it = loader.iterator(); it.hasNext();) {
	    ResultSetHandler h = (ResultSetHandler)it.next();
	    if (h.accepts(mimeType)) return h;}
	for (Iterator<ResultSetHandler> it = loader.iterator(); it.hasNext();) {
	    ResultSetHandler h = (ResultSetHandler)it.next();
	    return h;}
	throw new IllegalStateException("No ResultSetHandler instances registered");}}
