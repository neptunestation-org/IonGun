package org.neptunestation.iongun.util;

import java.util.*;

public class AutoCloseableArrayList<E> extends ArrayList<E> implements AutoCloseable {
    private static final long serialVersionUID = 1;
    public AutoCloseableArrayList (E item) {
	super();
	super.add(item);}
    @Override
    public void close () {}}
