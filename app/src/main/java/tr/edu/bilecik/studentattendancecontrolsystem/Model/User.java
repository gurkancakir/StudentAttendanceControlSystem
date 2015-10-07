package tr.edu.bilecik.studentattendancecontrolsystem.Model;

/**
 * Created by gurkanmustafa on 07/10/2015.
 */
public class User {

    private String userName;
    private String name;
    private String surname;
    private String department;

    public User(String userName, String name, String surname, String department)
    {
        this.setUserName(userName);
        this.setName(name);
        this.setSurname(surname);
        this.setDepartment(department);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
