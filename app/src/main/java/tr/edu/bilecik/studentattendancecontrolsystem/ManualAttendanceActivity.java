package tr.edu.bilecik.studentattendancecontrolsystem;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.dift.ui.SwipeToAction;
import tr.edu.bilecik.studentattendancecontrolsystem.Adapters.ManualListAdapter;
import tr.edu.bilecik.studentattendancecontrolsystem.Model.AttendancedUsers;
import tr.edu.bilecik.studentattendancecontrolsystem.Model.User;

public class ManualAttendanceActivity extends AppCompatActivity{

    RecyclerView recyclerView;
    SwipeToAction swipeToAction;
    ManualListAdapter manualListAdapter;

    SwipeRefreshLayout swipeRefreshLayout;

    List<User> users = new ArrayList<>();
    private String week;
    private String lesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_attendance);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        manualListAdapter = new ManualListAdapter(this.users);
        recyclerView.setAdapter(manualListAdapter);



            swipeToAction = new SwipeToAction(recyclerView, new SwipeToAction.SwipeListener() {
            @Override
            public boolean swipeLeft(final Object itemData) {
                final int pos = removeUser((User)itemData);
                displaySnackbar(((User)itemData).getName() + " removed", null,null);
                        /*"Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addUser(pos, (User)itemData);
                    }
                });*/
                return true;
            }

            @Override
            public boolean swipeRight(Object itemData) {
                User user = (User)itemData;
                addUser(user); //db ekleme
                displaySnackbar(user.getName() + getString(R.string.manuel_attendance_snacbar_aded), null, null);
                return true;
            }

            @Override
            public void onClick(Object itemData) {
                displaySnackbar(((User)itemData).getName() + getString(R.string.manuel_attendance_snacbar_removed), null, null);
            }

            @Override
            public void onLongClick(Object itemData) {
                displaySnackbar(((User)itemData).getName() + " longClick", null, null);
            }
        });

        populate();

    }

    private void populate() {
        swipeRefreshLayout.setRefreshing(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final List<String> listAttendancedUsers = AttendancedUsers.getInstance().listUserIds;
            week = extras.getString("week");
            lesson = extras.getString("lesson");
            if (lesson != null && week != null) {

                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("UserLessons");
                query1.whereEqualTo("Lessons",lesson);
                query1.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects != null)
                        {
                            for (final ParseObject obj : objects)
                            {
                                if (!listAttendancedUsers.contains(obj.getString("Users")))
                                {
                                    System.out.println(" c: " + listAttendancedUsers.size());
                                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("_User");
                                    query2.whereEqualTo("username", obj.getString("Users"));
                                    query2.getFirstInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject user, ParseException e) {
                                            if (user != null)
                                            {
                                                users.add(new User(user.getString("username"),user.getString("Name"),
                                                        user.getString("Surname"),user.getString("Department")));
                                                System.out.println("var");
                                                manualListAdapter.notifyDataSetChanged();
                                            }
                                            swipeRefreshLayout.setRefreshing(false);
                                        }
                                    });
                                }
                            }
                        }
                    }
                });
            }
        }
    }

    private boolean userListControl()
    {
        return (users.size() > 0) ? true : false;
    }
    private int removeUser(User user) {
        int pos = users.indexOf(user);
        users.remove(user);
        manualListAdapter.notifyItemRemoved(pos);


        if (!userListControl())
        {
            //kisi kalmamis anasayfaya gonder
            finish();
        }

        return pos;

    }

    //Yoklamada kisiyi var olarak ekler
    private void addUser(final User user) {
        ParseObject attendanceStatus = new ParseObject("AttendanceStatus");
        attendanceStatus.put("User", user.getUserName());
        attendanceStatus.put("Lessons", lesson);
        attendanceStatus.put("Week", Integer.parseInt(week));
        System.out.println("Ders : " + lesson + " Week : " + week + " User : " + user.getUserName());
        attendanceStatus.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    System.out.println("Manuel Eklendi");
                }else
                {
                    System.err.println(e.getStackTrace());
                }
            }
        }); //save
        removeUser(user);
    }
    private void displaySnackbar(String text, String actionName, View.OnClickListener action) {
        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                .setAction(actionName, action);

        //edit view
        View v = snack.getView();
        v.setBackgroundColor(getResources().getColor(R.color.secondary));
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);
        ((TextView) v.findViewById(android.support.design.R.id.snackbar_action)).setTextColor(Color.BLACK);

        snack.show();
    }

}
