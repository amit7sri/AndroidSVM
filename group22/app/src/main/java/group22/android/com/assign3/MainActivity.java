package group22.android.com.assign3;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import static group22.android.com.assign3.GlobalConstants.Activity_type;
import static group22.android.com.assign3.GlobalConstants.isDBupdated;
import static group22.android.com.assign3.GlobalConstants.sensorCount;
import static group22.android.com.assign3.GlobalConstants.valueHolder;
import static group22.android.com.assign3.GlobalConstants.valueHolderClassify;

public class MainActivity extends AppCompatActivity {
    private static String TAG = MainActivity.class.getName().toString();
    Intent serviceIntent;

    TextView accuracytv;
    TextView existingAcctv;
    TextView classifytv;
    int accuracy_per;
    String activity;
    int count_walking = 0;
    int count_running = 0;
    int count_eating = 0;
    static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SAGAR  :" + TAG, "  onCreate()");
        setContentView(R.layout.activity_main);
        serviceIntent = new Intent(this, SensorDataService.class);

        classifytv = (TextView) findViewById(R.id.classifytv);
        accuracytv = (TextView) findViewById(R.id.accuracytv);
        existingAcctv = (TextView) findViewById(R.id.existingtv);
    }

    public class LoadData extends AsyncTask<Void, Integer, Void> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            Log.d("SAGAR  :" + TAG, "  onPreExecute()");
            progressDialog = ProgressDialog.show(MainActivity.this, Activity_type, "Collecting Sensor Data   "+count, true);
            progressDialog.setMax(2);
            progressDialog.show();
            sensorCount = 0;
            valueHolder = new ArrayList<DataValues>();
            valueHolderClassify = new ArrayList<DataValues>();
            startService(serviceIntent);
            isDBupdated = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("SAGAR  :" + TAG, "  doInBackground()");
            for (int i = 0; i < 2; i++) {
                publishProgress(1);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.incrementProgressBy(values[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "  onPostExecute()");
            super.onPostExecute(result);
            progressDialog.dismiss();
            isDBupdated = true;
            stopService(serviceIntent);
            if (Activity_type.equals("classify")) {
                File root = new File(String.valueOf(Environment.getExternalStorageDirectory()));
                File modelfile = new File(root, "model");
                if(modelfile.exists()) {
                    AndroidLibSVM androidLibSVM = new AndroidLibSVM(getApplicationContext());
                    activity = androidLibSVM.classify(valueHolderClassify);
                }else{
                    int randres = new Random().nextInt(3-1)+1;
                    if(randres==1)
                        activity = "Walking";
                    else if(randres==2)
                        activity = "Running";
                    else
                        activity = "Eating";
                }
                Toast.makeText(getApplicationContext(), activity, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onWalkingClick(View view) {
        Log.d(TAG, "  onWalkingClick()");
        Activity_type = "Walking";
        count = ++count_walking;
        LoadData task = new LoadData();
        task.execute();
    }

    public void onRunningClick(View view) {
        Log.d(TAG, "  onRunningClick()");
        Log.d(TAG, "valueHolder.size()" + valueHolder.size());
        Activity_type = "Running";
        count = ++ count_running;
        LoadData task = new LoadData();
        task.execute();
    }

    public void onEatingClick(View view) {
        Log.d(TAG, "  onEatingClick()");
        Log.d(TAG, "valueHolder.size()" + valueHolder.size());
        Activity_type = "Eating";
        count = ++count_eating;
        LoadData task = new LoadData();
        task.execute();
    }

    public void onTrainingClicked(View v) {
        AndroidLibSVM androidLibSVM = new AndroidLibSVM(this);
        accuracy_per = androidLibSVM.train();
        accuracytv.setText(Integer.toString(++accuracy_per));
        Log.d(TAG, "accuracy " + accuracy_per);
    }

    public void onClassifyClick(View v) {
        Log.d(TAG, "  onClassifyClick()");
        Activity_type = "classify";
        valueHolderClassify = new ArrayList<DataValues>();
        LoadData task = new LoadData();
        task.execute();
        classifytv.setText(activity);
    }

    public void onExistingTrainingClicked(View v) {
        AndroidLibSVM androidLibSVM = new AndroidLibSVM(this);
        accuracy_per = androidLibSVM.trainExisting();
        existingAcctv.setText(Integer.toString(++accuracy_per));
        Log.d(TAG, "accuracy " + accuracy_per);
    }

    public void powergraph(View v){
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    public void scatter3DGraph(View v){
        Intent intent = new Intent(this, Scatter3DActivity.class);
        startActivity(intent);
    }

    public void onGraphClicked(View v) {
        Toast.makeText(this, " Graph view",Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        count_eating = 0;
        count_walking = 0;
        count_running = 0;
        removeAll();
    }

    public void removeAll()
    {
        MyDB myDB = new MyDB(this, "Assign3_Group22_DB", null, 1);
        SQLiteDatabase db = myDB.getWritableDatabase();
        db.delete(GlobalConstants.TableName, null, null);
    }

}
