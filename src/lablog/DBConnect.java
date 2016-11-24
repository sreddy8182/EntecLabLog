/* Created by: Giovani Rodriguez
   Establishes a connection and executes querys with a MySQL Database
 */
package lablog;

import lablog.tables.*;

import java.sql.*;
import java.util.ArrayList;

public class DBConnect {
    // class fields
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    // constructor
    DBConnect() {
        // attempt to access external library
        try {
            // give it the class name
            Class.forName("com.mysql.jdbc.Driver");

            // establish mysql connection
            connection = DriverManager.getConnection(Config.Url, Config.User, Config.Password);
            statement = connection.createStatement();
        } catch (Exception e) {
            // print error to console
            System.out.println("Connection Error: " + e.getMessage());
        }
    }

    // methods
    int findId(String strId) {
        // attempt to find id
        try {
            // form query
            String query = "SELECT id FROM students WHERE strId = '" + strId + "'";

            // perform query
            resultSet = statement.executeQuery(query);

            // check if record exists
            if (resultSet.next()) {
                return resultSet.getInt("id");
            } else {
                // error
                return -1;
            }
        } catch (Exception e) {
            // print error
            System.out.println("Find Id Error: " + e.getMessage());

            // return error
            return -1;
        }
    }

    private void writeHistory(String strId, boolean type) throws SQLException {
        // form query
        String query = "INSERT INTO `history` (`stuId`, `time`, `type`) VALUES ('" +
                strId + "', CURRENT_TIMESTAMP, '" + (type ? "1" : "0") + "');";

        // execute query
        statement.execute(query);
    }

    private boolean isLoggedIn(int id) {
        // get all logged in
        ArrayList<Student> log = getLoggedIn();

        // check to make sure it was successful
        if (log == null) {
            return false;
        }

        // iterate through each log
        for (int i = 0; i < log.size(); i++) {
            // check for id
            if (log.get(i).getId() == id)
                return true;
        }

        // return false
        return false;
    }

    public boolean closeConnection() {
        // attempt to close database connection
        try {
            // close
            connection.close();

            // success
            return true;
        } catch (Exception e) {
            // log
            System.out.println("Close Connection Error: " + e.getMessage());

            // return error
            return false;
        }
    }

    public int logIn(String strId) {
        // declare id
        int id = 0;

        // get id otherwise not registered
        if ((id = findId(strId)) == -1)
            return -1;

        // check if they are logged in
        if (isLoggedIn(id))
            return -2;

        // attempt to execute a query
        try {
            // write history
            writeHistory(strId, true);

            // form query
            String query = "INSERT INTO `loggedIn` (`id` ,`timeIn`) VALUES ( '" + id + "', CURRENT_TIMESTAMP);";

            // execute query
            statement.execute(query);

            // return success
            return 1;
        } catch (Exception e) {
            // print error to console
            System.out.println("Log In Error: " + e.getMessage());

            // return error
            return 0;
        }
    }

    public int logOut(String strId) {
        // declare id
        int id = 0;

        // get id otherwise not registered
        if ((id = findId(strId)) == -1)
            return -1;

        // check if they are logged in
        if (!isLoggedIn(id))
            return -2;

        // attempt to execute a query
        try {
            // write history
            writeHistory(strId, false);

            // form query
            String query = "DELETE FROM `loggedIn` WHERE `loggedIn`.`id` = " + id + ";";

            // execute query
            statement.execute(query);

            // return success
            return 1;
        } catch (Exception e) {
            // print error to console
            System.out.println("Log Out Error: " + e.getMessage());

            // return error
            return 0;
        }
    }

    public boolean registerStudent(Student student) {
        // form query
        String query = "INSERT INTO `students` (`strId`, `firstName`, `lastName`, `subject`, `professor`) VALUES ('" +
                student.getStrId() + "', '" + student.getFirstName() + "', '" + student.getLastName() +
                "', '" + student.getSubject() + "', '" + student.getProfessor() + "')";

        // attempt to execute query
        try {
            // execute query
            statement.execute(query);

            // return success
            return true;
        } catch (Exception e) {
            // log error
            System.out.println("Register Error: " + e.getMessage());

            // return error
            return false;
        }
    }

    public ArrayList<Student> getLoggedIn() {
        // create an instance of two arraylists
        ArrayList<Log> records = new ArrayList<Log>();
        ArrayList<Student> students = new ArrayList<Student>();

        // attempt to request records and student from server
        try {
            // form query
            String query = "SELECT * FROM loggedIn;";

            // execute query
            resultSet = statement.executeQuery(query);

            // add records to array lists
            while(resultSet.next()) {
                Log record = new Log();

                // set fields
                record.setId(resultSet.getInt("id"));
                record.setTimeIn(resultSet.getTimestamp("timeIn"));

                // add record to list
                records.add(record);
            }

            // check to see if there are records
            if (records.size() == 0)
                return null;

            // form query
            query = "SELECT * FROM students WHERE";

            // concatenate ids of students
            for (int i = 0, length = records.size(); i < length; i++) {
                if (i != length - 1)
                    query += " id = '" + records.get(i).getId() + "' OR";
                else
                    query += " id = '" + records.get(i).getId() + "';";
            }

            // execute query
            resultSet = statement.executeQuery(query);

            // add students to arraylist
            while(resultSet.next()) {
                Student student = new Student();

                // set fields
                student.setId(resultSet.getInt("id"));
                student.setStrId(resultSet.getString("strId"));
                student.setFirstName(resultSet.getString("firstName"));
                student.setLastName(resultSet.getString("lastName"));
                student.setRegistered(resultSet.getTimestamp("registered"));
                student.setSubject(resultSet.getString("subject"));
                student.setProfessor(resultSet.getString("professor"));

                // add record to list
                students.add(student);
            }

            // return list
            return students;
        } catch (Exception e) {
            // print error to console
            System.out.println("Get Logged In Error: " + e.getMessage());

            // return error
            return null;
        }
    }
}
