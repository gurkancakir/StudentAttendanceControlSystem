package tr.edu.bilecik.studentattendancecontrolsystem.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses.MySupportFragment;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 04/10/2015.
 */
public class MyLessonsFragment extends MySupportFragment {

    TextView textView;
    String sonuc = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_lessons, null);

        textView = (TextView) rootView.findViewById(R.id.textViewLessons);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLessons");
        query.whereEqualTo("Users", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                   System.out.println("bulundu : " + list.size());
                    final List<String> objectId = new ArrayList<String>();
                    for (ParseObject item : list)
                    {
                        System.out.println(item.get("Lessons"));
                        objectId.add(item.getString("Lessons"));
                    }

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Lessons");
                    query2.whereContainedIn("objectId",objectId);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            System.out.println("Sonuc : ...." + objects.size());
                            for (ParseObject po : objects)
                                sonuc += po.getString("LessonName")+"\n";
                            textView.setText(sonuc);
                        }
                    });
                } else {
                    Log.d("Lessons", "Error: " + e.getMessage());
                }
            }
        });

        return rootView;
    }
}
