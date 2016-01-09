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
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 07/10/2015.
 */
public class ListAttendanceControlAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    List<Attendance> lessons;

    public ListAttendanceControlAdapter(Activity activity, List<Attendance> lessons) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.activity = activity;
        this.lessons = lessons;
    }

    @Override
    public int getCount() {
        return lessons.size();
    }

    @Override
    public Attendance getItem(int i) {
        return lessons.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.list_attendance_item, null);
        }
        Attendance lesson = lessons.get(position);
        ((TextView) rowView.findViewById(R.id.txtLessonName)).setText(lesson.getLessonName());
        SeekBar seekBar = (SeekBar) rowView.findViewById(R.id.seekBarAttendance);
        seekBar.setProgress(lesson.getAttendance());
        seekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        return rowView;
    }
}
