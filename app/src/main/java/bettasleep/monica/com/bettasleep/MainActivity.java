package bettasleep.monica.com.bettasleep;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import bettasleep.monica.com.bettasleep.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Select a button!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void summButtonSelected(View v) {
        if (R.id.summButton == v.getId()) {
            Snackbar.make(v, "Summary coming soon!", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    public void instButtonSelected(View v) {
        if (v.getId() == R.id.instButton) {
            Intent intent = new Intent(this, InstActivity.class);
            startActivity(intent);
        }
    }

    public void webButtonSelected(View v) {
        if (v.getId() == R.id.webButton) {
                Intent intent = new Intent(this, WebActivity.class);
                startActivity(intent);
            }
    }

    public void recButtonSelected(View v) {
        if (v.getId() == R.id.recButton) {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}