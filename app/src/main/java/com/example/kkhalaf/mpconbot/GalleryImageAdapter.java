package com.example.kkhalaf.mpconbot;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class GalleryImageAdapter extends BaseAdapter
{
    private Context mContext;
    public ArrayList<String> ImagePathes = new ArrayList<>();


    public GalleryImageAdapter(Context context, ArrayList<String> _ImagePathes)
    {
        mContext = context;
        ImagePathes = _ImagePathes;
    }

    public int getCount() {
        //return mImageIds.length;
        return ImagePathes.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup)
    {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(RetrieveImage(index));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setMaxWidth(50);
        imageView.setMaxHeight(50);
        imageView.setPadding(0,0,0,0);
        return imageView;
    }

    private Bitmap RetrieveImage(int Index)
    {
        ContextWrapper cw = new ContextWrapper(mContext);
        File MyDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        String path = MyDirectory.getAbsolutePath();
        Bitmap theImage;
        try {
            String ImageName = ImagePathes.get(Index);
            File f = new File(path, ImageName);
            theImage = BitmapFactory.decodeStream(new FileInputStream(f));
            return theImage;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }


}