package bettasleep.monica.com.bettasleep;


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

public class RecordActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;
    private LineChart mChart;
    FakeDataSimple fakeData = new FakeDataSimple();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChart = new LineChart(this);
        mainLayout.addView(mChart);

        mChart.setMinimumWidth(1300);
        mChart.setMinimumHeight(1700);

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
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChart.getAxisLeft();
        yl.setTextColor(Color.BLACK);
        yl.setAxisMaxValue(120f);
        yl.setDrawGridLines(true);


        YAxis yl2 = mChart.getAxisRight();
        yl2.setEnabled(false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // now we're going to simulate real time data addition

        new Thread(new Runnable() {
            @Override
            public void run() {
                // add 100 entries
                float len = new FakeDataSimple().getLength();
                for (int i = 0; i < len; i++) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry(); // chart is notified of update in addEntry method
                        }
                    });

                    // pause between adds
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        // manage error ...

                    }
                }
            }
        }).start();
    }

    // we need to create a method to add an entry to the line chart

    private void addEntry() {
        LineData data = mChart.getData();

        if (data != null) {
            LineDataSet set = (LineDataSet) data.getDataSetByIndex(0);

            if (set == null) {
                // creation if null
                set = createSet();
                data.addDataSet(set);
            }

            // add a new random value
            data.addXValue("");
//            data.addEntry(new Entry((float) (Math.random() * 110) + 5f, set
//                    .getEntryCount()), 0);

            float datum = fakeData.getData();
            data.addEntry(new Entry((float) datum, set.getEntryCount()), 0);

            // notify chart data have changed
            mChart.notifyDataSetChanged();

            // limit number of visible entries
            mChart.setVisibleXRangeMinimum(4);

            // but also ACTUALLY limit number of visible entries
            mChart.setVisibleXRangeMaximum(100);

            // scroll to the last entry
            mChart.moveViewToX(data.getXValCount() - 7);
        }
    }


    // method to create set
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
}