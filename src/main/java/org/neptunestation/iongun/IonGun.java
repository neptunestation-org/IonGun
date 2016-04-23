package org.neptunestation.iongun;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import org.apache.commons.cli.*;
import org.neptunestation.iongun.net.*;
import org.neptunestation.iongun.util.*;

public class IonGun {
    public static class IonOptions extends Options {
	public IonOptions addIOptions (String opt, boolean hasArg, String description, String... longOpts) {
	    for (String s : longOpts)
		addOption(Option.builder(opt).longOpt(s).desc(description).hasArg(hasArg).build());
	    return this;}}

    public static void main (String[] args) {
	CommandLineParser parser = new DefaultParser();
	IonOptions options = new IonOptions();
	options
	    .addIOptions(null, false, "Size of database. Show the size of the database on disk. For Oracle this requires access to read the table dba_data_files - the user system has that.", "db-size dsize".split(" "))
	    .addIOptions("h", false, "Print a summary of the options to GNU sql and exit.", "help")
	    .addIOptions(null, false, "HTML output. Turn on HTML tabular output.", "html")
	    .addIOptions(null, false, "Show the list of running queries.", "show-processlist proclist listproc".split(" "))
	    .addIOptions(null, false, "Show the list of running queries.", "listproc")
	    .addIOptions(null, false, "List the databases (table spaces) in the database.", "show-databases", "showdbs", "list-databases", "listdbs")
	    .addIOptions(null, false, "List the tables in the database.", "show-databases", "show-tables", "list-tables", "table-list")
	    .addIOptions(null, false, "Remove headers and footers and print only tuples. Bug in Oracle: it still prints number of rows found.", "noheaders", "no-headers")
	    .addIOptions("p", true, "pass-through.  The string following -p will be given to the database connection program as arguments. Multiple -p's will be joined with space. Example: pass '-U' and the user name to the program:  -p \"-U scott\" can also be written -p -U -p scott.", "pass-through")
	    .addIOptions("r", false, "Try 3 times. Short version of --retries 3.")
	    .addIOptions(null, true, "Try ntimes times. If the client program returns with an error, retry the command. Default is --retries 1.", "retries")
	    .addIOptions("s", true, "Field separator. Use string as separator between columns.", "sep")
	    .addIOptions(null, false, "Do not use the first line of input (used by GNU sql itself when called with --shebang).", "skip-first-line")
	    .addIOptions(null, false, "Size of tables. Show the size of the tables in the database.", "table-size", "tablesize")
	    .addIOptions("v", false, "Print which command is sent.", "verbose")
	    .addIOptions("V", false, "Print the version GNU sql and exit.", "version")
	    .addIOptions("Y", false, "GNU sql can be called as a shebang (#!) command as the first line of a script. Like this:  #!/usr/bin/sql -Y mysql:///  SELECT * FROM foo;   For this to work --shebang or -Y must be set as the first option.", "shebang");

	HelpFormatter f = new HelpFormatter();
	f.setOptionComparator(null);
	try {
	    Class.forName("org.neptunestation.iongun.net.MySQLJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.net.OracleJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.net.PostgresJDBCURLStreamHandler");
	    Class.forName("org.neptunestation.iongun.net.SQLiteJDBCURLStreamHandler");
	    CommandLine line = parser.parse(options, args);
	    if (line.hasOption("help")) printHelp();
	    URL.setURLStreamHandlerFactory(new SqlURLStreamHandlerFactory());
	    String l;
	    for (String s : line.getArgs()) {
		BufferedReader br =
		    new BufferedReader(new InputStreamReader(("sql".equalsIgnoreCase((new URL(s)).getProtocol()) ?
							      new URL((new URL(s)).getFile()) : new URL(s))
							     .openConnection()
							     .getInputStream()));
		while ((l = br.readLine())!=null)
		    System.out.println(l);}}
	catch (ClassNotFoundException e) {throw new RuntimeException(e);}
	catch (IOException e) {throw new RuntimeException(e);}
	catch (ParseException e) {
	    System.out.println("Unexpected exception:" + e.getMessage());
	    System.exit(1);}}
    
    public static void printHelp () {
	System.out.println("Error:\n" +
			   "No DBURL given\n" +
			   "\n" +
			   "sql [-hnr] [--table-size] [--db-size] [-p pass-through] [-s string] dburl [command]\n" +
			   "\n");}}


