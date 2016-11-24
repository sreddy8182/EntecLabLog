package lablog.tables;

import java.sql.Timestamp;

// used to create a student from students table
public class Student {
    // fields
    private int id;
    private String strId;
    private String firstName;
    private String lastName;
    private Timestamp registered;
    private char[] subject;
    private String professor;

    // constant fields
    public static final int SIZE = 7;

    // constructor
    public Student() {
        // create subject char array
        subject = new char[SIZE];
    }

    // copy constructor
    public Student(Student student) {
        // set all fields in new instance
        setStrId(student.getStrId());
        setFirstName(student.getFirstName());
        setLastName(student.getLastName());
        setRegistered(student.getRegistered());
        setSubject(student.getSubject());
        setProfessor(student.getProfessor());
    }

    // override to string method

    /* generated getters and setters */
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getStrId() {
        return strId;
    }

    public void setStrId(String strId) {
        this.strId = strId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getRegistered() {
        return registered;
    }

    public void setRegistered(Timestamp registered) {
        this.registered = registered;
    }

    public String getSubject() {
        return new String(this.subject);
    }

    public void setSubject(String subject) {
        this.subject = subject.toCharArray();
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }
    /* end setters getters */
}
