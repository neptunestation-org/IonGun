package org.neptunestation.iongun.sql;

import java.util.*;

public abstract class AbstractResultSetHandler implements ResultSetHandler {
    protected Map<String, List<String>> properties = new HashMap<String, List<String>>();
    public void setProperties (Map<String, List<String>> properties) {this.properties = properties;}}
