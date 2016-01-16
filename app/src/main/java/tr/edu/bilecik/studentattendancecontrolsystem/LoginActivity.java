package tr.edu.bilecik.studentattendancecontrolsystem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses.MyActivity;


public class LoginActivity extends MyActivity {

    @Bind(R.id.sign_in_button)
    Button signInButton;

    @Bind(R.id.editStudentNo)
    EditText editStudentNo;
    private String studentNo;

    @Bind(R.id.editPassword)
    EditText editPassword;
    private String password;

    private ProgressDialog progressDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        context = this;
        final Intent intent = new Intent(context,HomeScreen.class);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            startActivity(intent);
        }

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                studentNo = editStudentNo.getText().toString();
                password = editPassword.getText().toString();

                progressDialog = new ProgressDialog(context);
                progressDialog.setTitle(getString(R.string.progress_login));
                progressDialog.setMessage(getString(R.string.progress_wait));
                progressDialog.show();

                ParseUser.logInInBackground(studentNo, password,
                        new LogInCallback() {

                            @Override
                            public void done(ParseUser user,
                                             com.parse.ParseException e) {
                                if (user != null) {
                                    progressDialog.dismiss();
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progressDialog.dismiss();
                                    String error = getString(R.string.alert_message_error);
                                    /*getString(R.string.errorLogin);
                                    if (e.getCode() == 100)//UnknownHostException yani internet baglantisi yok ise
                                        error = getString(R.string.errorLoginNetwork);
                                    else if (e.getCode() == 101) // k.adi ve Sifre hatali girilmis ise
                                        error = getString(R.string.wrong);
                                    */
                                    //android.util.Log.w("giris",""+e.getCode());
                                    Toast.makeText(getApplicationContext(),
                                            error,
                                            Toast.LENGTH_SHORT).show();
                                    e.fillInStackTrace();
                                }
                            }
                        });

            }
        });
    }

}