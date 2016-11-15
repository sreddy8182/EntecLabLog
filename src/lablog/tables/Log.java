package lablog.tables;

import java.sql.Timestamp;

// an instance of this class represents a record from loggedIn table
public class Log {
    // fields
    private int id;
    private Timestamp timeIn;

    // constructor
    public Log() { }

    // copy constructor
    public Log(Log log) {
        // set field of new instance
        setId(log.getId());
        setTimeIn(log.getTimeIn());
    }

    /* generated getters and setters */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(Timestamp timeIn) {
        this.timeIn = timeIn;
    }
    /* end setters getters */
}
