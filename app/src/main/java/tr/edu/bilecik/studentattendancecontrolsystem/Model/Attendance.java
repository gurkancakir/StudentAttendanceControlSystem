package tr.edu.bilecik.studentattendancecontrolsystem.Model;

/**
 * Created by gurkanmustafa on 07/10/2015.
 */
public class Attendance {

    private String lessonName;
    private int attendance;

    public Attendance(String lessonName, int attendance)
    {
        this.setLessonName(lessonName);
        this.setAttendance(attendance);
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }
}
