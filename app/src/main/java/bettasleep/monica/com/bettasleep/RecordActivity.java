package bettasleep.monica.com.bettasleep;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import static bettasleep.monica.com.bettasleep.R.id.fab;

public class RecordActivity extends AppCompatActivity {

    private LineChart mChart;
    FakeDataSimple fakeData = new FakeDataSimple();
    BLEDataConverter converter = new BLEDataConverter();
    int fab_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChart = new LineChart(this);
        mainLayout.addView(mChart);

        mChart.setMinimumWidth(1300);
        mChart.setMinimumHeight(1300);

        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data for the moment");

        mChart.setTouchEnabled(true);

        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        mChart.setPinchZoom(true);

        mChart.setBackgroundColor(Color.LTGRAY);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add data to line chart
        mChart.setData(data);

        // get legend object
        Legend l = mChart.getLegend();

        // customize legend
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLUE);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.BLACK);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChart.getAxisLeft();
        yl.setTextColor(Color.BLACK);
        //yl.setAxisMaxValue(120f);
        yl.setDrawGridLines(true);


        YAxis yl2 = mChart.getAxisRight();
        yl2.setEnabled(false);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.record_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Will record", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                fab_counter++;
                if (fab_counter % 2 == 1) {
                    onRecButtonClicked();
                    fab.setImageResource(R.drawable.pause_icon);
                } else {
                    fab.setImageResource(R.drawable.record_icon);
                }

                if (fab_counter % 2000 == 0) {
                    int HR = getHeartRate(mChart);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void onRecButtonClicked() {
        // simulate real time data addition
        new Thread(new Runnable() {
            @Override
            public void run() {
                // add 100 entries
                float len = fakeData.getLength();
                for (int i = 0; i < len; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry(); // notify chart about the update in addEntry method
                        }
                    });

                    // pause between datum additions
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }).start();

    }

    // add an entry to the line chart
    private void addEntry() {
        LineData data = mChart.getData();

        if (data != null) {
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);

            if (set == null) {
                // creation if null
                set = createSet();
                data.addDataSet(set);
            }

            // add a new value
            data.addXValue("");
            //float datum = fakeData.getData();

            // BRYCE - THIS IS WHERE WE START. HOW DO WE GET DATA THAT WILL GO IN byte[20] ?
            byte input_bytes[] = new byte[20];
            int j = 0;
            for (int i = 0; i < 20; i += 2) {
                input_bytes[j++] = (byte) input_bytes[i];
            }

            float output[] = converter.convert(input_bytes);

            for (int i = 0; i < 20; i++) {
                data.addEntry(new Entry((float) output[i], set.getEntryCount()), 0);
            }

            // BRYCE - THIS IS WHERE WE END

            // notify chart that the data have changed
            mChart.notifyDataSetChanged();

            // set minimum number of visible entries
            mChart.setVisibleXRangeMinimum(4);

            // but also limit number of visible entries
            mChart.setVisibleXRangeMaximum(100);

            // scroll to the last entry
            mChart.moveViewToX(data.getXValCount() - 7);
        }
    }


    // create dataset
    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "Fake ECG data");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(4f);
        set.setCircleRadius(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244, 117, 177));
        set.setDrawValues(false);
        //set.setValueTextColor(Color.BLACK);
        //set.setValueTextSize(10f);

        return set;
    }

    private int getHeartRate(LineChart chart) {
        LineData data = chart.getData();

        float points = data.getXValMaximumLength();
        float minutes = points / 2000;
        //return peakCount/minutes;
        return 1;
    }

    public void buttonSelected(View v) {
        if (v.getId() == R.id.webButton) {
            Intent intent = new Intent(this, WebActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.recButton) {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        } else if (R.id.summButton == v.getId()) {
            Intent intent = new Intent(this, SummActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.instButton) {
            Intent intent = new Intent(this, InstActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(v, "Button ID error :(", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}