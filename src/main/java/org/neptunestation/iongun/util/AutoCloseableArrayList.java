package org.neptunestation.iongun.util;

import java.util.*;

public class AutoCloseableArrayList<E> extends ArrayList<E> implements AutoCloseable {
    AutoCloseableArrayList (E... items) {super.addAll(Arrays.asList(items));}
    public void close () {}}
