package tr.edu.bilecik.studentattendancecontrolsystem.Model;

/**
 * Created by gurkanmustafa on 08/11/2015.
 */
public class LessonWithCount {

    private String lessonName;
    private int count;

    public LessonWithCount(String lessonName,int count)
    {
        this.setCount(count);
        this.setLessonName(lessonName);
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
