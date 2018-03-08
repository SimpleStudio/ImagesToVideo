package com.example.pcqpcq.imagevideoeffects;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;


public class EncodingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new EncodingTask().execute();
    }

    class EncodingTask extends AsyncTask<Void, String, Void> {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test_" + SlideApplication.BIT_RATE / 1024 + "_" + SlideApplication.SLIDE_EFFECT + ".mp4");
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EncodingActivity.this);
            dialog.setTitle("Generating Video..");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            long startTime = System.currentTimeMillis();
            if (f.exists()) f.delete();
            SlideEncoder slideEncoder = new SlideEncoder();

            try {
                slideEncoder.prepareEncoder(f);
                Bitmap prevBm = null;
                dialog.setMax(SlideApplication.bitmapList.size());
                for (int idx = 0; idx < SlideApplication.bitmapList.size(); idx++) {
                    publishProgress(String.valueOf(idx + 1));
                    SlideShow.init();

                    if (idx > 0) prevBm = SlideApplication.bitmapList.get(idx - 1);
                    Bitmap curBm = SlideApplication.bitmapList.get(idx);
                    for (int i = 0; i < (SlideApplication.FRAME_PER_SEC * SlideApplication.SLIDE_TIME); i++) {
                        // Drain any data from the encoder into the muxer.
                        slideEncoder.drainEncoder(false);

                        // Generate a frame and submit it.
                        slideEncoder.generateFrame(prevBm, curBm);
                    }
                }
                slideEncoder.drainEncoder(true);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                slideEncoder.releaseEncoder();
            }
            Log.e("TAG", "total time : " + (System.currentTimeMillis() - startTime));
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            dialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (dialog.isShowing()) dialog.dismiss();
            for (Bitmap bm : SlideApplication.bitmapList) bm.recycle();
            SlideApplication.bitmapList.clear();

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(f), "video/mp4");
            startActivity(i);
        }
    }
}