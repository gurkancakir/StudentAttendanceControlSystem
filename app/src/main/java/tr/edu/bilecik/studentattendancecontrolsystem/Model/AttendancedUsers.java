package tr.edu.bilecik.studentattendancecontrolsystem.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurkanmustafa on 16/10/2015.
 */
public class AttendancedUsers {
    private static AttendancedUsers ourInstance = new AttendancedUsers();

    public static AttendancedUsers getInstance() {
        return ourInstance;
    }

    public static List<String> listUserIds = new ArrayList<>();

    private AttendancedUsers() {
    }
}
