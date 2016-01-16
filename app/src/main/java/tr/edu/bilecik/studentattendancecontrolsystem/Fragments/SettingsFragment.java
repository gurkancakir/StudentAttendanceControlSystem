package tr.edu.bilecik.studentattendancecontrolsystem.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import tr.edu.bilecik.studentattendancecontrolsystem.Adapters.ListAdviserLessonAdapter;
import tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses.MySupportFragment;
import tr.edu.bilecik.studentattendancecontrolsystem.LoginActivity;
import tr.edu.bilecik.studentattendancecontrolsystem.Model.LessonWithCount;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 04/10/2015.
 */
public class SettingsFragment extends MySupportFragment {


    private EditText editNewPassword;
    private EditText editNewPasswordRepeat;

    private Button btnChangePassword;

    private ProgressDialog progressDialog;
    private Context context;

    private TextView txtError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, null);

        getActivity().setTitle(getString(R.string.title_adviser_lesson_fragment));

        editNewPassword = (EditText) rootView.findViewById(R.id.editNewPassword);
        editNewPasswordRepeat = (EditText) rootView.findViewById(R.id.editNewPasswordRepeat);
        btnChangePassword = (Button) rootView.findViewById(R.id.change_password_button);
        txtError = (TextView) rootView.findViewById(R.id.txtError);

        context = getActivity();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(getString(R.string.progress_change_password));
        progressDialog.setMessage(getString(R.string.progress_wait));

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                String newPassword = editNewPassword.getText().toString();
                String newPasswordRepeat = editNewPasswordRepeat.getText().toString();

                if (!newPassword.equals(newPasswordRepeat) || newPasswordRepeat.isEmpty() || newPassword.isEmpty() ) {
                    //sifreler eslesmiyor veya editler bos
                    txtError.setText("Not equal new password or please do not empty text\n");
                    progressDialog.dismiss();
                    return;
                }

                ParseUser.getCurrentUser().setPassword(editNewPassword.getText().toString()); // change password
                ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null)
                        {
                            txtError.setText("Success !!!\n");
                            ParseUser.logOut();
                            startActivity(new Intent(context,LoginActivity.class));
                        }
                        else
                        {
                            txtError.setText(e.getMessage());
                        }
                    }
                });


            }
        });


        return rootView;
    }

}
