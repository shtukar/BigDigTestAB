package ru.yandex.shtukarr.bigdigitalstestb;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ImageActivity extends AppCompatActivity {
    private static final String INTENT_URL = "String";
    private static final String INTENT_STATUS = "Status";
    private static final String INTENT_ID = "Id";

    private PhotoView photoView;
    private TextView textViewEmpty;
    private Bitmap imgBitmap;
    private String urls;
    private int status;
    private int id;
    private ContentProviderManager contentProviderManager;

    private final int countTime = 10000;
    private final int countInterval = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        photoView = findViewById(R.id.photo_view);
        textViewEmpty = findViewById(R.id.empty);
        urls = getIntent().getStringExtra(INTENT_URL);
        status = getIntent().getIntExtra(INTENT_STATUS, -1);
        id = getIntent().getIntExtra(INTENT_ID, -1);
        contentProviderManager = new ContentProviderManager(this);
        checkStartApp();
    }

    private void checkStartApp() {
        if (urls == null) {
            Log.d("TAG", "urls == null");
            textViewEmpty.setVisibility(View.VISIBLE);
            photoView.setVisibility(View.GONE);
            new CountDownTimer(countTime, countInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    textViewEmpty.setText(getResources().getString(R.string.empty_img) +
                            String.valueOf(millisUntilFinished / countInterval));
                }

                @Override
                public void onFinish() {
                    finish();
                }
            }.start();
        } else {
            textViewEmpty.setVisibility(View.GONE);
            photoView.setVisibility(View.VISIBLE);
            if (status == -1) {
                calledFromTest();
            } else {
                calledFromHistory();
            }
        }
    }

    private void calledFromTest() {
        if (isNetworkAvailable()) {
            loadImage();
        } else {
            Toast.makeText(getApplicationContext(), "Connection not available.", Toast.LENGTH_LONG).show();
        }
    }

    private void calledFromHistory() {
        if (status == 1) {
            if (isNetworkAvailable()) {
                loadImage();
                startDelService();
                downloadImage();
            } else {
                Toast.makeText(getApplicationContext(), "Connection not available.", Toast.LENGTH_LONG).show();
            }
        } else {
            loadImage();
        }
    }

    private void startDelService() {
        Log.d("TAG", "startDelService");
        Intent service = new Intent(getApplicationContext(), DelService.class);
        service.putExtra("ID", id);
        startService(service);
    }

    private void loadImage() {
        Picasso.get().load(urls).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("TAG", "onBitmapLoaded");
                insertOrUpdate(1);
                imgBitmap = bitmap;
                photoView.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                Log.d("TAG", "onBitmapFailed");
                insertOrUpdate(2);
                photoView.setImageDrawable(getDrawable(R.drawable.ic_error_outline_black_24dp));
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d("TAG", "onPrepareLoad");
                photoView.setImageDrawable(getDrawable(R.drawable.ic_image_black_24dp));
            }
        });
    }

    private void insertOrUpdate(int loadStatus) {
        if (status == -1)
            contentProviderManager.insertLink(urls, loadStatus, Calendar.getInstance().getTimeInMillis());
        else if (status != loadStatus)
            contentProviderManager.updateLink(id, urls, loadStatus, Calendar.getInstance().getTimeInMillis());
    }

    void downloadImage() {
        if (isStoragePermissionGranted()) {
            Log.d("TAG", "downloadImage");
            File myDir = new File(Environment.getExternalStorageDirectory(), "/BIGDIG/test/B");
            myDir.mkdirs();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp;
            File file = new File(myDir, imageFileName + ".jpg");
            try {
                FileOutputStream out = new FileOutputStream(file);
                imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
                MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{file.toString()}, null,
                        (path, uri) -> {
                            Log.d("TAG", "Scanned " + path + ":");
                            Log.d("TAG", "-> uri=" + uri);
                        });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("TAG", e.getMessage());
            }
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {
                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        } else {
            Log.v("TAG", "Permission is granted");
            return true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
