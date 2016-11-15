package lablog.tables;

import java.sql.Timestamp;

// an instance of this class represents one record from history table
public class Record {
    // fields
    private int id;
    private Timestamp timestamp;
    private boolean type;

    // constructor
    public Record() { }

    // copy constructor
    public Record(Record record) {
        // set fields of new instance
        setId(record.getId());
        setTimestamp(record.getTimestamp());
        setType(record.getType());
    }

    /* generated getters and setters */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }
    /* end setters getters */
}
