package group22.android.com.assign3;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class GraphActivity extends AppCompatActivity {
    GraphView myView;
    static boolean conti;
    String xlabels[] = {"100", "150", "200", "250"};
    String ylabels[] = {"550", "500", "450", "400", "350", "300", "250", "200", "150", "100"};
    float[] values = new float[521];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        try {
            values = readCsvFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        myView = new GraphView(GraphActivity.this, values, "Power vs Time", xlabels, ylabels, true);
        RelativeLayout.LayoutParams graphParam = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        myView.setLayoutParams(graphParam);
        ((RelativeLayout) findViewById(R.id.graphPlot)).addView(myView);
    }


    public float[] readCsvFile() throws IOException {
        float[] values = new float[521];
        InputStream is = getResources().openRawResource(R.raw.powerdata1);
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        int i=0;
        while ((line = reader.readLine()) != null) {
            if (line != null) {
                values[i]=Float.parseFloat(line)/1000;
                System.out.println("Converted ArrayList data: " + values[i] + "\n");
                i++;
            }
        }
        return values;
    }
}
