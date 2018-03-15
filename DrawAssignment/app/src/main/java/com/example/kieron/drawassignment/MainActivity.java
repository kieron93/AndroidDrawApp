package com.example.kieron.drawassignment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Random;


public class MainActivity extends AppCompatActivity
{
    private static int RESULT_LOAD_IMG = 1;
    SeekBar redBar, greenBar, blueBar, brushSize;
    MyDraw draw;
    Button save, load, clear;
    ToggleButton eraser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        draw = (MyDraw)findViewById(R.id.MyDraw);
        draw.c = new Canvas(draw.bmp);
        draw.c.drawColor(Color.WHITE);
        redBar = (SeekBar)findViewById(R.id.seekBar);
        greenBar = (SeekBar)findViewById(R.id.seekBar1);
        blueBar = (SeekBar)findViewById(R.id.seekBar2);
        brushSize = (SeekBar)findViewById(R.id.seekBar5);

        save = (Button)findViewById(R.id.button);
        clear = (Button)findViewById(R.id.button2);
        load = (Button)findViewById(R.id.button3);
        eraser = (ToggleButton)findViewById(R.id.toggleButton);

        save.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                saveToStorage(draw.bmp);
/*
                try
                {
                    dest.createNewFile();
                    FileOutputStream ostream = new FileOutputStream(dest);
                    draw.bmp.compress(Bitmap.CompressFormat.PNG, 100, ostream);
                    ostream.close();
                }
                catch (Exception e)
                {

                }*/
            }
        });

        load.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
                /*
                File src = new File(getExternalFilesDir(null), "myDrawing.png");
                try
                {
                    Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(src));
                    draw.bmp = bmp.copy(bmp.getConfig(), true);
                }
                catch (Exception e)
                {

                }
                draw.invalidate();*/
            }
        });

        eraser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                if (b) draw.eraserEnabled = true;
                else draw.eraserEnabled = false;
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                draw.clearView();
            }
        });

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser)
            {
                //Toast.makeText(MainActivity.this, "Seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
                draw.redVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser)
            {
                //Toast.makeText(MainActivity.this, "Seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
                draw.greenVal = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser)
            {
                //Toast.makeText(MainActivity.this, "Seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
                draw.blueVal = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
        brushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser)
            {
                //Toast.makeText(MainActivity.this, "Seekbar progress: " + progress, Toast.LENGTH_SHORT).show();
                draw.brushVal = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                draw.bmp = selectedImage.copy(selectedImage.getConfig(), true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        }else {
            Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void saveToStorage(Bitmap bmp)
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        File dest = new File(root + "/saved_drawing_1");
        dest.mkdirs();
        Random rand = new Random();
        int num = 999999;
        num = rand.nextInt(num);
        String fileName = "Image-" + num + ".jpg";
        File file = new File(dest, fileName);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String s, Uri uri) {
                Log.i("ExternalStorage", "Scanned " + s + ":");
                Log.i("ExternalStorage", "-> uri= " + uri);
            }
        });
    }
}
