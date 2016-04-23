package org.neptunestation.iongun.util;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import javax.sql.*;

/**
 * Utility class that supports new ResultSetRenderer sub-classes
 */
public class Util {
    /**
     * Helper class that proxies a ResultSet.  A ResultSet represents
     * a collection of fields, but doesn't provide convenient ways to
     * treat it as a bona-fide Collecition or Iterable.  This class
     * helps facilitate that.
     */
    public static class SQLValue {
	int i;
	ResultSet r;
	public SQLValue (int i, ResultSet r) {
	    this.r = r;
	    this.i = i;}
	public String toString () throws IllegalStateException {
	    try {return r.getString(i);}
	    catch (Exception e) {throw new IllegalStateException(e);}}}

    /**
     * Adapt an Iterable of String-to-SQLValue Maps as an Iterable of
     * Properties.
     */
    public static Iterable<Properties> asIterable (final Iterable<Map<String, SQLValue>> it) {
	return new Iterable<Properties>() {
	    @Override public Iterator<Properties> iterator () {
		return new Iterator<Properties>() {
		    final Iterator<Map<String, SQLValue>> proxy = it.iterator();
		    @Override public boolean hasNext () {return proxy.hasNext();}
		    @Override public Properties next () {
			Properties p = new Properties();
			for (Map.Entry<String, SQLValue> e : proxy.next().entrySet()) p.setProperty(e.getKey(), e.getValue().toString());
			return p;}};}};}

    // public static Iterable<List<String>> asListIterable (final Iterable<Properties> props) throws SQLException {
    // 	return new Iterable<List<String>> {
    // 	    @Override public Iterator<List<String>> iterator () {
    // 		return new Iterator<List<String>>() {
    // 		    final Iterator<Properties> proxy = it.iterator();
    // 		    @Override public boolean hasNext () {return proxy.hasNext();}
    // 		    @Override public List<String> next () {
    // 			ArrayList<String> l = new ArrayList<>();
    // 			for (Properties p : proxy.next()) l.add(p.
			
    // 	return null;}

    /**
     * Adapt a ResultSet as an Iterable of String-to-SQLValue Maps.
     */
    public static Iterable<Map<String, SQLValue>> asIterable (final ResultSet r) throws SQLException {
	if (r instanceof RowSet) ((RowSet)r).execute();
	return
	    (Iterable<Map<String, SQLValue>>)
	    Proxy
	    .newProxyInstance(Iterable.class.getClassLoader(),
			      new Class[] {Iterable.class},
			      new InvocationHandler() {
				  @Override public Object invoke (Object proxy, Method method, Object[] args) throws Exception {
				      if (method.getDeclaringClass().equals(Iterable.class) && method.getName().equals("iterator"))
					  return new Iterator<Map<String, SQLValue>>() {
					      private ResultSetMetaData m = r.getMetaData();
					      private boolean hasNext = false;
					      private boolean didNext = false;
					      @Override public boolean hasNext () {
						  if (!didNext) {
						      try {hasNext = r.next();} catch (Exception e) {hasNext = false;}
						      didNext = true;}
						  return hasNext;}
					      @Override public Map<String, SQLValue> next () {
						  if (!hasNext()) throw new NoSuchElementException();
						  try {
						      Map<String, SQLValue> map = new HashMap<String, SQLValue>();
						      for (int i=1; i<=m.getColumnCount(); i++) map.put(m.getColumnName(i), new SQLValue(i, r));
						      didNext = false;
						      return map;}
						  catch (Exception e) {throw new IllegalStateException(e);}}
					      @Override public void remove () {throw new UnsupportedOperationException();}};
				      return method.invoke(args);}});}}
