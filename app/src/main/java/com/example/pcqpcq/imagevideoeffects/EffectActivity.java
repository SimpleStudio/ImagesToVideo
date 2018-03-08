package com.example.pcqpcq.imagevideoeffects;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.concurrent.ExecutionException;

/**
 * Created by PrakashSaurav.
 */
public class EffectActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "EffectActivity";
    private ViewPager mViewPager;
    private Button mBtnBitRate, mBtnTime, mBtnEffect, mBtnNext, mBtnPrev;
    private TextView mTvBitRate, mTvTime, mTvEffect;
    private String[] mParcelableUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effectactivity);

//        mParcelableUris = getIntent().getStringArrayExtra("parcelableUris");
//        if (mParcelableUris == null) {
//            Toast.makeText(getApplicationContext(), "There is no path info for the photo.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        mParcelableUris = new String[]{
                "/sdcard/Download/a.jpg",
                "/sdcard/Download/b.jpg",
                "/sdcard/Download/c.jpg"
        };

        mViewPager = (ViewPager) findViewById(R.id.pager_image);
        mViewPager.setAdapter(new CustomPageAdapter());

        mBtnBitRate = (Button) findViewById(R.id.btn_bgm);
        mTvBitRate = (TextView) findViewById(R.id.tv_bitrate);

        mBtnTime = (Button) findViewById(R.id.btn_time);
        mTvTime = (TextView) findViewById(R.id.tv_time);

        mBtnEffect = (Button) findViewById(R.id.btn_effect);
        mTvEffect = (TextView) findViewById(R.id.tv_effect);

        mBtnNext = (Button) findViewById(R.id.btn_next);
        mBtnPrev = (Button) findViewById(R.id.btn_prev);

        mBtnBitRate.setOnClickListener(this);
        mBtnTime.setOnClickListener(this);
        mBtnEffect.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnPrev.setOnClickListener(this);
    }

    int timeCheck = 0;
    int effectCheck = 0;

    boolean isClicked;

    @Override
    public void onClick(View v) {
        // BGM Setting
        if (v == mBtnBitRate) {
            isClicked = !isClicked;
            int bitRate;
            if (isClicked)
                bitRate = 2000 * 1024;
            else
                bitRate = 500 * 1024;
            SlideApplication.BIT_RATE = bitRate;
            mTvBitRate.setText(String.valueOf(bitRate / 1024) + "kbps");
            Toast.makeText(getApplicationContext(), SlideApplication.BIT_RATE / 1024 + "kbps", Toast.LENGTH_SHORT).show();
        }
        // Time Setting
        else if (v == mBtnTime) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String[] items = {"2s", "3s", "5s", "10s"};

            if (SlideApplication.SLIDE_TIME == 2) timeCheck = 0;
            else if (SlideApplication.SLIDE_TIME == 3) timeCheck = 1;
            else if (SlideApplication.SLIDE_TIME == 5) timeCheck = 2;
            else if (SlideApplication.SLIDE_TIME == 10) timeCheck = 3;

            builder.setSingleChoiceItems(items, timeCheck, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    timeCheck = which;
                    Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SlideApplication.SLIDE_TIME = Integer.parseInt(items[timeCheck].split("초")[0]);
                    mTvTime.setText(Integer.toString(SlideApplication.SLIDE_TIME));
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        // Effect Setting
        else if (v == mBtnEffect) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final String[] items = {"None", "FadeIn", "Rotate", "SlideIn"};
            if (SlideApplication.SLIDE_EFFECT == SlideShow.NONE) effectCheck = 0;
            else if (SlideApplication.SLIDE_EFFECT == SlideShow.FADE_IN) effectCheck = 1;
            else if (SlideApplication.SLIDE_EFFECT == SlideShow.ROTATE) effectCheck = 2;
            else if (SlideApplication.SLIDE_EFFECT == SlideShow.SLIDE_IN) effectCheck = 3;

            builder.setSingleChoiceItems(items, effectCheck, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    effectCheck = which;
                    Toast.makeText(getApplicationContext(), items[which], Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SlideApplication.SLIDE_EFFECT = effectCheck;
                    mTvEffect.setText(items[SlideApplication.SLIDE_EFFECT]);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        // Next
        else if (v == mBtnNext) {
            new BitmapChangerTask().execute();
        }
        // Prev
        else if (v == mBtnPrev) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getStringExtra("bgm_path") != null) {
                    SlideApplication.BGM_PATH = data.getStringExtra("bgm_path");
                }
            }
        }
    }

    private class BitmapChangerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < mParcelableUris.length; i++) {
                try {
                    Bitmap bm = Glide.with(getApplicationContext()).load(mParcelableUris[i].toString())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    int width = bm.getWidth();
                    int height = bm.getHeight();

                    // Land ( 1280 x 720 )
                    if (width > height) {
                        bm = Bitmap.createScaledBitmap(bm, SlideEncoder.WIDTH, ((SlideEncoder.WIDTH * height) / width), true);
                    }
                    // Port ( 720 x 1280 )
                    else if (width < height) {
                        bm = Bitmap.createScaledBitmap(bm, ((SlideEncoder.HEIGHT * width) / height), SlideEncoder.HEIGHT, true);
                    }
                    // Square ( 800 x 800 )
                    else {
                        bm = Bitmap.createScaledBitmap(bm, SlideEncoder.WIDTH, SlideEncoder.HEIGHT, true);
                    }
                    SlideApplication.bitmapList.add(bm);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent i = new Intent(getApplicationContext(), EncodingActivity.class);
            startActivity(i);
        }
    }


    private class CustomPageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mParcelableUris.length;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext())
                    .load(mParcelableUris[position].toString())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv);
            try {
                container.addView(iv);
            } catch (IllegalStateException ise) {
                ise.printStackTrace();
            }

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
