package com.example.pcqpcq.imagevideoeffects;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.pcqpcq.imagevideoeffects.grafika.RecordFBOActivity;
import com.example.pcqpcq.imagevideoeffects.opengl.OpenGlDemoActivity;

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void onEffectActivity(View v) {
        // from here:
        // https://stackoverflow.com/questions/43534345/ffmpeg-transition-effect-between-images-of-video-slideshow
        startToActivity(EffectActivity.class);
    }

    public void onOpenGlDemoActivity(View v) {
        // from here:
        // http://bigflake.com/mediacodec/#EncodeAndMuxTest
        startToActivity(OpenGlDemoActivity.class);
    }

    public void onRecordFBOActivity(View v) {
        // from here, change output file to ExternalCacheDir:
        // https://github.com/google/grafika/blob/master/app/src/main/java/com/android/grafika/RecordFBOActivity.java
        startToActivity(RecordFBOActivity.class);
    }

    private void startToActivity(Class<?> cls) {
        Intent aa = new Intent(MainActivity.this, cls);
        startActivity(aa);
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
