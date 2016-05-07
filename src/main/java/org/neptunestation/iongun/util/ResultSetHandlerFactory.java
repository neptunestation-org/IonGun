package org.neptunestation.iongun.util;

import java.util.*;
import java.util.concurrent.*;

public abstract class ResultSetHandlerFactory {
    private static List<ResultSetHandler> handlers = new CopyOnWriteArrayList<>();

    public static void register (ResultSetHandler handler) {
	handlers.add(handler);}

    public ResultSetHandler createResultSetHandler (String mimeType) {
	for (ResultSetHandler h : handlers) if (h.accepts(mimeType)) return h;
	for (ResultSetHandler h : handlers) return h;
	throw new IllegalStateException("No ResultSetHandler instances registered");}}
