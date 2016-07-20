//*********************************************************************
//
//                 Copyright (c) 2004-2008 by Teradata Corporation
//                         All Rights Reserved
//
//*********************************************************************
//
//  File:       T20000JD.java
//  Header:     none
//  Purpose:    Demonstrate basic Teradata DDL.
//              The program will:
//                -  Connect as user guest/please
//                -  Drop table employee if it exists
//                -  Create table employee with the following columns:
//                       empID (INTEGER),
//                       empName (VARCHAR(30)),
//                       empDept (VARCHAR(50)),
//                       empJob (VARCHAR(300))
//                -  Create two table indexes: unique and non-unique
//                -  Disconnect.
//
//  JDBC API: java.sql.Connection, java.sql.Statement,
//            java.sql.Statement.executeUpdate
//
//  Version: Updated for Teradata V2R6
//
//  Please note that the samples assume that the user access to the
//  is set up before the examples are executed. Thus, only table/index
//  creation is demonstrated.
//*********************************************************************

import java.sql.*;

public class TeraDataTest
{
    // Name of the user able to create, drop, and manipulate tables
    public static String sUser = "guest";
    public static String sPassword = "please";

    public static void main(String args[])
    throws ClassNotFoundException
    {
        // Creation of URL to be passed to the JDBC driver
        String url = "jdbc:teradata://whomooz/TMODE=ANSI,CHARSET=UTF8";

        // Statements used in table creation
        String sDropTbl = "DROP TABLE employee";
        String sCreateTbl = "CREATE TABLE employee (empID INTEGER NOT NULL, " +
            "empName VARCHAR(30) NOT NULL, empDept VARCHAR(50) NOT NULL, " +
            "empJob VARCHAR(300), PRIMARY KEY(empID))";
        // Statements used in index creation. Both unique and non-unique indexes
        // will be created. Please note that these may not result in optimal
        // performance.
        String sCreateIdx = "CREATE INDEX (empName) ON employee";
        String sCreateIdx2 = "CREATE UNIQUE INDEX (empName, empDept) ON employee";

        try
        {
            System.out.println("\n Sample T20000JD: \n");
            System.out.println(" Looking for the Teradata JDBC driver... ");
            // Loading the Teradata JDBC driver
            Class.forName("com.teradata.jdbc.TeraDriver");
            System.out.println(" JDBC driver loaded. \n");

            // Attempting to connect to Teradata
            System.out.println(" Attempting to connect to Teradata via" +
                               " the JDBC driver...");
            // Creating a Connection object. A Connection represents a session
            // with a specific database. Within the context of a Connection,
            // SQL statements are executed and results are returned.
            // Creating a database connection with the given database URL,
            // user name, and password
            Connection con = DriverManager.getConnection(url, sUser, sPassword);
            System.out.println(" User " + sUser + " connected.");
            System.out.println(" Connection to Teradata established. \n");

            try
            {
                // Creating a statement object from an active connection.
                // A Statement object is used for executing a static SQL
                // statement and obtaining the results produced by the statement.
                // Statement.executeUpdate method is used to execute a SQL
                // INSERT, UPDATE or DELETE statement. SQL statements that
                // return nothing, such as DDL statements, can be executed
                // as well. The method will return the row count for INSERT,
                // UPDATE, or DELETE, or zero for statements that return nothing.
                Statement stmt = con.createStatement();
                System.out.println(" Statement object created. \n");

                try
                {
                    // Cleanup procedure:
                    // If the sample table already exists, drop it.
                    // Please note that the "no table present" exception
                    // will be raised if and only if no table "employee" already
                    // exists, as during the very first execution of this
                    // example. If a user wants to replicate this error multiple
                    // times, the table will have to be manually removed before
                    // running this example again in order for the error to
                    // occur.
                    try
                    {
                        System.out.println(" Dropping table if present: "
                                           + sDropTbl);
                        // Executing the drop table command
                        stmt.executeUpdate(sDropTbl);
                        System.out.println(" Table dropped.\n");
                    }
                    catch (SQLException ex)
                    {
                        // If the table did not exist, no drop is required.
                        // Ignore the raised "no table present" exception by
                        // printing out the error message and swallowing the
                        // exception. If multiple exceptions are chained
                        // together, display information on all of them.
                        // The following code will demonstrate some of the
                        // methods available for retrieving information about
                        // SQLExceptions, some inherited from the Throwable
                        // class. Please refer to JDBC API for other
                        // method descriptions.

                        while (ex != null)
                        {
                            // Display a short description of the exception.
                            // Returns information generated by the toString()
                            // method: a concatenation of three strings:
                            // - Name of the actual class of this object
                            // - ": " (a colon and a space)
                            // - Result of the getMessage() method for this
                            //   object
                            System.out.println("\n Drop table exception " +
                                               "ignored: " + ex);
                            // Display vendor-specific exception code for this
                            // SQLException object.
                            System.out.println(" Error code: "
                                               + ex.getErrorCode());
                            // Display the SQLState for this SQLException object.
                            System.out.println(" SQL State: "
                                               + ex.getSQLState());
                            // Display the detail message string, if any.
                            System.out.println(" Message: "
                                              + ex.getMessage());
                            // Display the localized description of this
                            // exception, if any. Default implementation returns
                            // the same result as getMessage().
                            System.out.println(" Localized Message: "
                                               + ex.getLocalizedMessage());

                            // Print this error and its backtrace to the
                            // standard error stream.
                            System.out.println(" Stack Trace: ");
                            ex.printStackTrace();

                            // Retrieve the exception chained to this
                            // SQLException object, if any.
                            ex = ex.getNextException();
                        }

                        System.out.println(" Table could not be dropped." +
                                           " Execution will continue...\n");
                    }

                    // Create the sample table
                    System.out.println(" Creating table: " + sCreateTbl);
                    stmt.executeUpdate(sCreateTbl);
                    System.out.println(" Sample table created. \n");
                    // Create table indexes
                    System.out.println(" Creating table indexes: "
                                       + sCreateIdx + " " + sCreateIdx2);
                    stmt.executeUpdate(sCreateIdx);
                    stmt.executeUpdate(sCreateIdx2);
                    System.out.println(" Table indexes created.");
                }
                finally
                {
                    // Close the statement
                    stmt.close();
                    System.out.println("\n Statement object closed. \n");
                }
            }
            finally
            {
                // Close the connection
                System.out.println(" Closing connection to Teradata...");
                con.close();
                System.out.println(" Connection to Teradata closed. \n");
            }

            System.out.println(" Sample T20000JD finished. \n");
        }
        catch (SQLException ex)
        {
            // A SQLException was generated.  Catch it and display
            // the error information.
            // Note that there could be multiple error objects chained
            // together.
            System.out.println();
            System.out.println("*** SQLException caught ***");

            while (ex != null)
            {
                System.out.println(" Error code: " + ex.getErrorCode());
                System.out.println(" SQL State: " + ex.getSQLState());
                System.out.println(" Message: " + ex.getMessage());
                ex.printStackTrace();
                System.out.println();
                ex = ex.getNextException();
            }

            throw new IllegalStateException ("Sample failed.") ;
        }
    } // End main
} // End class T20000JD