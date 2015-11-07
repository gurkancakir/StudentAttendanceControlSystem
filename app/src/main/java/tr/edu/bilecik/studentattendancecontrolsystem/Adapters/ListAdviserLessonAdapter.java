package tr.edu.bilecik.studentattendancecontrolsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import tr.edu.bilecik.studentattendancecontrolsystem.Model.Attendance;
import tr.edu.bilecik.studentattendancecontrolsystem.Model.LessonWithCount;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 07/10/2015.
 */
public class ListAdviserLessonAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    List<LessonWithCount> lessons;

    public ListAdviserLessonAdapter(Activity activity, List<LessonWithCount> lessons)
    {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.lessons = lessons;
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public LessonWithCount getItem(int i) {
        return lessons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null){
            rowView = mInflater.inflate(R.layout.list_adviser_lesson_item, null);
        }
        LessonWithCount lesson = lessons.get(position);
        ((TextView) rowView.findViewById(R.id.txtLessonNameItem)).setText(lesson.getLessonName());
        ((TextView) rowView.findViewById(R.id.txtCountItem)).setText(""+lesson.getCount());

        return rowView;
    }
}
