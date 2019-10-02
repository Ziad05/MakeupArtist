package com.makeover.makeupartist.PhotoGallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import java.util.ArrayList;
import com.makeover.makeupartist.R;

public class ListViewAdapter extends ArrayAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> photos;

    public ListViewAdapter(Context context, ArrayList<String> photos) {
        super(context, R.layout.photo_custom_layout, photos);
        this.context = context;
        this.photos = photos;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(null == convertView){
            convertView = inflater.inflate(R.layout.photo_custom_layout, parent, false);
        }

        ImageView photoView = convertView.findViewById(R.id.photoView);

        Glide.with(context)
                .load(photos.get(position))
                .centerCrop()
                .placeholder(R.color.colorDarkGray)    /* If Images are Loaded Very Slowly beacuse of slow internet */
                .error(R.drawable.ic_no_image)          /* If given url shows any error */
                .fallback(R.drawable.ic_no_image)       /* If pass a null url */
                .transition(DrawableTransitionOptions.withCrossFade(750)) /* Switching placeholder to origianl image with animation */
                .into(photoView);

        return convertView;
    }
}
