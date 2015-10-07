package tr.edu.bilecik.studentattendancecontrolsystem.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SeekBar;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import tr.edu.bilecik.studentattendancecontrolsystem.Adapters.ListAttendanceControlAdapter;
import tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses.MySupportFragment;
import tr.edu.bilecik.studentattendancecontrolsystem.Model.Lesson;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 04/10/2015.
 */
public class HomeFragment extends MySupportFragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView listViewAttendance;
    List<Lesson> lessonList;
    ListAttendanceControlAdapter listAttendanceControlAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home,null);
        getActivity().setTitle("Home");

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);


        listViewAttendance = (ListView) rootView.findViewById(R.id.listViewAttendance);
        lessonList = new ArrayList<>();
        listAttendanceControlAdapter = new ListAttendanceControlAdapter(getActivity(),lessonList);
        listViewAttendance.setAdapter(listAttendanceControlAdapter);

        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        attendanceControl();
                                    }
                                }
        );

        return rootView;
    }

    private void attendanceControl() {
        //Dersi veren kisiye bakildi
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Lessons");
        query.whereEqualTo("Adviser", ParseUser.getCurrentUser().getUsername().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (final ParseObject lesson : objects)
                    {
                        System.out.println("for ici");
                        //aktif kisinin derslerine bakildi.
                        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("AttendanceStatus");
                        query1.whereEqualTo("Lessons",lesson.getObjectId());
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> objects, ParseException e) {
                                lessonList.add(new Lesson(lesson.getString("LessonName"), objects.size()));
                                System.out.println("ders yoklama : "+lesson.getString("LessonName")+" "+objects.size());
                                listAttendanceControlAdapter.notifyDataSetChanged();
                            }
                        });
                    }//for end

                }
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {

    }
}
