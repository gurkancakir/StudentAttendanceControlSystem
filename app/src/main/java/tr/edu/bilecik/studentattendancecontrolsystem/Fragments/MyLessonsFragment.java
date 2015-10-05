package tr.edu.bilecik.studentattendancecontrolsystem.Fragments;

import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses.MySupportFragment;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 04/10/2015.
 */
public class MyLessonsFragment extends MySupportFragment implements WeekView.MonthChangeListener,WeekView.EventClickListener,WeekView.EventLongPressListener {

    //TextView textView;
    String sonuc = "";
    WeekView mWeekView;
    List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_lessons, null);

        //textView = (TextView) rootView.findViewById(R.id.textViewLessons);
        // Get a reference for the week view in the layout.
        mWeekView = (WeekView) rootView.findViewById(R.id.weekView);


        // Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(this);

        // The week view has infinite scrolling horizontally. We have to provide the events of a
        // month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(this);

        // Set long press listener for events.
        mWeekView.setEventLongPressListener(this);

        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 3);
        startTime.set(Calendar.MINUTE, 0);
        Calendar endTime = (Calendar) startTime.clone();
        endTime.add(Calendar.HOUR, 1);
        WeekViewEvent event = new WeekViewEvent(1, "AA", startTime, endTime);
        event.setColor(getResources().getColor(R.color.event_color_01));
        events.add(event);




        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLessons");
        query.whereEqualTo("Users", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                    System.out.println("bulundu : " + list.size());
                    final List<String> objectId = new ArrayList<String>();
                    for (ParseObject item : list) {
                        System.out.println(item.get("Lessons"));
                        objectId.add(item.getString("Lessons"));
                    }

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Lessons");
                    query2.whereContainedIn("objectId", objectId);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            System.out.println("Sonuc : ...." + objects.size());
                            for (ParseObject po : objects) {
                                sonuc += po.getString("LessonName") + "\n";
                                Calendar startTime = Calendar.getInstance();
                                startTime.set(Calendar.HOUR_OF_DAY, 3);
                                startTime.set(Calendar.MINUTE, 0);
                                Calendar endTime = (Calendar) startTime.clone();
                                endTime.add(Calendar.HOUR, 1);
                                WeekViewEvent event = new WeekViewEvent(po.getInt("DayOfWeek"), po.getString("LessonName"), startTime, endTime);
                                event.setColor(getResources().getColor(R.color.event_color_01));
                                events.add(event);
                            }
                            //textView.setText(sonuc);
                            System.out.println(sonuc);
                        }
                    });
                } else {
                    Log.d("Lessons", "Error: " + e.getMessage());
                }
            }
        });

        setupDateTimeInterpreter(true);

        return rootView;
    }

    private void setupDateTimeInterpreter(final boolean shortDate) {
        mWeekView.setDateTimeInterpreter(new DateTimeInterpreter() {
            @Override
            public String interpretDate(Calendar date) {
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(date.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" d/M", Locale.getDefault());
                // All android api level do not have a standard way of getting the first letter of
                // the week day name. Hence we get the first char programmatically.
                // Details: http://stackoverflow.com/questions/16959502/get-one-letter-abbreviation-of-week-day-of-a-date-in-java#answer-16959657
                if (shortDate)
                    weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(date.getTime());
            }

            @Override
            public String interpretTime(int hour) {
                return hour > 11 ? (hour - 12) + " PM" : (hour == 0 ? "12 AM" : hour + " AM");
            }
        });
    }


    @Override
    public void onEventClick(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(final int newYear, final int newMonth) {
        // Populate the week view with some events.

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserLessons");
        query.whereEqualTo("Users", ParseUser.getCurrentUser().getObjectId());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(final List<ParseObject> list, ParseException e) {
                if (e == null) {
                    System.out.println("bulundu : " + list.size());
                    final List<String> objectId = new ArrayList<String>();
                    for (ParseObject item : list) {
                        System.out.println(item.get("Lessons"));
                        objectId.add(item.getString("Lessons"));
                    }

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Lessons");
                    query2.whereContainedIn("objectId", objectId);
                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            System.out.println("Sonuc : ...." + objects.size());
                            for (ParseObject po : objects) {
                                sonuc += po.getString("LessonName") + "\n";
                                Calendar startTime = Calendar.getInstance();
                                startTime.set(Calendar.HOUR_OF_DAY, 3);
                                startTime.set(Calendar.MINUTE, 0);
                                startTime.set(Calendar.MONTH, newMonth - 1);
                                startTime.set(Calendar.YEAR, newYear);
                                Calendar endTime = (Calendar) startTime.clone();
                                endTime.add(Calendar.HOUR, 1);
                                endTime.set(Calendar.MONTH, newMonth - 1);
                                WeekViewEvent event = new WeekViewEvent(po.getInt("DayOfWeek"),po.getString("LessonName"),startTime,endTime);
                                event.setColor(getResources().getColor(R.color.event_color_01));
                                events.add(event);
                            }
                            //textView.setText(sonuc);
                        }
                    });
                } else {
                    Log.d("Lessons", "Error: " + e.getMessage());
                }
            }
        });

        return events;
    }
}
