package tr.edu.bilecik.studentattendancecontrolsystem.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import tr.edu.bilecik.studentattendancecontrolsystem.Adapters.ListAdviserLessonAdapter;
import tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses.MySupportFragment;
import tr.edu.bilecik.studentattendancecontrolsystem.Model.LessonWithCount;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 04/10/2015.
 */
public class MyAdviserLessonsFragment extends MySupportFragment {

    String sonuc = "";
    private ListView listViewAdviser;
    List<LessonWithCount> listLessonsWithValue = new ArrayList<>();
    private ListAdviserLessonAdapter adviserLessonAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_adviser_lessons, null);

        getActivity().setTitle(getString(R.string.title_adviser_lesson_fragment));

        listViewAdviser = (ListView) rootView.findViewById(R.id.listAdviserLessons);

        adviserLessonAdapter = new ListAdviserLessonAdapter(getActivity(), listLessonsWithValue);
        listViewAdviser.setAdapter(adviserLessonAdapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Lessons");
        query.whereEqualTo("Adviser", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                    System.out.println("bulundu : " + list.size());
                    for (ParseObject item : list) {
                        final String lessonName = item.getString("LessonName");
                        final String lessonObjectId = item.getObjectId();
                        //Ders mevcudu cekiliyor
                        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("UserLessons");
                        query1.whereEqualTo("Lessons", lessonObjectId);
                        System.out.println("Lesson : " + lessonName + " objectId : " + lessonObjectId);
                        query1.countInBackground(new CountCallback() {
                            @Override
                            public void done(int count, ParseException e) {
                                if (e == null) {
                                    listLessonsWithValue.add(new LessonWithCount(lessonName, count));
                                    adviserLessonAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                        System.out.println(item.get("LessonName"));
                    }
                    System.out.println(sonuc);
                }
            }
        });


        return rootView;
    }

}
