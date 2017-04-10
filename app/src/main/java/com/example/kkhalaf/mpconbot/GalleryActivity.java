package com.example.kkhalaf.mpconbot;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.lang.Object;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class GalleryActivity extends AppCompatActivity {

    ImageView selectedImage;
    Integer SelectedIndex = 0;
    ArrayList<String> ImagesNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        ImagesNames = (ArrayList<String>) getIntent().getSerializableExtra("ImagePathes");

        Gallery gallery = (Gallery) findViewById(R.id.GalleryWidget);
        selectedImage = (ImageView) findViewById(R.id.GalleryImageView);

        //gallery.setSpacing(1);
        final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this, ImagesNames);

        gallery.setAdapter(galleryImageAdapter);

        //gallery.setSpacing(1);

        selectedImage.setImageBitmap(RetrieveImage(SelectedIndex)); // set default as first image

        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                // show the selected Image
                selectedImage.setImageBitmap(RetrieveImage(position));
                SelectedIndex = position;
            }
        });
    }

    private Bitmap RetrieveImage(int Index)
    {
        ContextWrapper cw = new ContextWrapper(this);
        File MyDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String path = MyDirectory.getAbsolutePath();
        Bitmap theImage;
        try {

            if(ImagesNames.size() > 0 && Index < ImagesNames.size())
            {
                String ImageName = ImagesNames.get(Index);
                File f = new File(path, ImageName);
                theImage = BitmapFactory.decodeStream(new FileInputStream(f));
                return theImage;
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void ShareImage(View view)
    {
        Bitmap icon = RetrieveImage(SelectedIndex);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values);

        OutputStream outstream;
        try {
            outstream = getContentResolver().openOutputStream(uri);
            icon.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
            outstream.close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share Image"));
    }

    public void DeleteImage(View view)
    {
        try
        {
            // remove the file from internal storage
            ContextWrapper cw = new ContextWrapper(this);
            File MyDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);
            String path = MyDirectory.getAbsolutePath();
            File fileToBeDeleted = new File(path + "//Image" + SelectedIndex + ".jpg"); // current image
            boolean WasDeleted = fileToBeDeleted.delete();

            // Or
            //File dir = getFilesDir();
            //File file = new File(dir, ImagesNames.get(SelectedIndex));
            //boolean deleted = file.delete();

            //File fileToBeDeleted = new File(getFilesDir(), ImagesNames.get(SelectedIndex));
            //String path = fileToBeDeleted.getAbsolutePath();
            //boolean deleted = fileToBeDeleted.delete();
            //Log.d("file path to delete", path );

            // remove file name from my array
            ImagesNames.remove(SelectedIndex);
            SelectedIndex++;
            Gallery gallery = (Gallery) findViewById(R.id.GalleryWidget);
            final GalleryImageAdapter galleryImageAdapter = new GalleryImageAdapter(this, ImagesNames);
            gallery.setAdapter(galleryImageAdapter);

        } catch (Exception e)
        {
            System.err.println(e.toString());
        }
    }
}
