package tr.edu.bilecik.studentattendancecontrolsystem.Fragments;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import tr.edu.bilecik.studentattendancecontrolsystem.CustomClasses.MySupportFragment;
import tr.edu.bilecik.studentattendancecontrolsystem.HomeScreen;
import tr.edu.bilecik.studentattendancecontrolsystem.R;

/**
 * Created by gurkanmustafa on 04/10/2015.
 */
public class AttendanceControlFragment extends MySupportFragment {

    BluetoothAdapter bluetoothAdapter;
    ArrayAdapter<String> detectedAdapter;
    Context context;
    ArrayList<BluetoothDevice> arrayListBluetoothDevices = null;
    ListView listViewDetected;
    private Menu optionsMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance_control, null);
        setHasOptionsMenu(true);

        listViewDetected = (ListView) rootView.findViewById(R.id.listViewDetected);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        onBluetooth();
        context = getActivity();
        detectedAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_single_choice);
        arrayListBluetoothDevices = new ArrayList<BluetoothDevice>();
        listViewDetected.setAdapter(detectedAdapter);

        arrayListBluetoothDevices.clear();
        startSearching();


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.optionsMenu = menu;
        inflater.inflate(R.menu.menu_attendance_control,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_attendance){
            try {
                attendance();
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void attendance(){

        new StartAttendanceControl().execute();
        setRefreshActionButtonState(true);//progress bar refresh iconla değişecek ve çalışacak .

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshActionButtonState(false);//3 sn sonra duracak ve refresh iconu geri gelecek. Siz bu arada başka işlemler sunucu bağlantısı vs.. yapabilirsiniz

            }
        }, 5000);

    }
    public void setRefreshActionButtonState(final boolean refreshing) {

        //bu method refresh iteme tıklandığında progress bar gözükmesi için
        if (optionsMenu != null) {
            final MenuItem refreshItem = optionsMenu
                    .findItem(R.id.action_attendance);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_refresh_progress);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    private class StartAttendanceControl extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            System.out.println("Size : " + arrayListBluetoothDevices.size() + " async");
            for (int i=0; i<arrayListBluetoothDevices.size(); i++)
            {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserDevices");
                query.whereEqualTo("DeviceNo", arrayListBluetoothDevices.get(i).getAddress().toString());
                final int finalI = i;
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        System.out.println("Object Size : " + objects.size() + " async DeviceNo : " + arrayListBluetoothDevices.get(finalI).getAddress().toString());
                        if (objects.size() > 0)
                        {
                            ParseObject attendanceStatus = new ParseObject("AttendanceStatus");
                            attendanceStatus.put("User", objects.get(0).get("UserId").toString());
                            attendanceStatus.put("Lessons", "S72WCSgzQ5");
                            attendanceStatus.put("Week", finalI);
                            attendanceStatus.saveInBackground();
                            System.out.println("Ekleniyorr");
                        }
                    }
                });
            }
            return null;
        }
    }

    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(myReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }
    private void onBluetooth() {
        if(!bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.enable();
            Log.i("Log", "Bluetooth is Enabled");
        }
    }
    private void offBluetooth() {
        if(bluetoothAdapter.isEnabled())
        {
            bluetoothAdapter.disable();
        }
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                Toast.makeText(context, "ACTION_FOUND", Toast.LENGTH_SHORT).show();

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try
                {
                    //device.getClass().getMethod("setPairingConfirmation", boolean.class).invoke(device, true);
                    //device.getClass().getMethod("cancelPairingUserInput", boolean.class).invoke(device);
                }
                catch (Exception e) {
                    Log.i("Log", "Inside the exception: ");
                    e.printStackTrace();
                }

                if(arrayListBluetoothDevices.size()<1) // this checks if the size of bluetooth device is 0,then add the
                {                                           // device to the arraylist.
                    detectedAdapter.add(device.getName()+"\n"+device.getAddress());
                    arrayListBluetoothDevices.add(device);
                    detectedAdapter.notifyDataSetChanged();
                }
                else
                {
                    boolean flag = true;    // flag to indicate that particular device is already in the arlist or not
                    for(int i = 0; i<arrayListBluetoothDevices.size();i++)
                    {
                        if(device.getAddress().equals(arrayListBluetoothDevices.get(i).getAddress()))
                        {
                            flag = false;
                        }
                    }
                    if(flag == true)
                    {
                        detectedAdapter.add(device.getName()+"\n"+device.getAddress());
                        arrayListBluetoothDevices.add(device);
                        detectedAdapter.notifyDataSetChanged();
                    }
                }
                //new StartAttendanceControl().execute();
            }
        }
    };

}
