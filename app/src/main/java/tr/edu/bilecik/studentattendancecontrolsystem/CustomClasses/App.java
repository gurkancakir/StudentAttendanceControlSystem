package tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses;

/**
 * Created by gurkanmustafa on 04/10/2015.
 */
import android.app.Application;
import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Parse.initialize(this, "app-id", "client-key");
        //siteden verilen app-id ve client-key i yazdim.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "YMxrw8n1h7hDBJUnJRNwVGBbKKJqZuJ5orotKtPZ", "4aWKOke3F3zPRkKiBVcfJT4cpDHJA84PGhku3zLX");

    }
}