package com.makeover.makeupartist.PhotoGallery;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;
import com.makeover.makeupartist.Common;
import com.makeover.makeupartist.R;
import com.makeover.makeupartist.Store_Data;
import com.makeover.makeupartist.VideoGallery.YoutubePlayerActivity;
import java.util.ArrayList;
import java.util.Objects;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import static android.icu.lang.UCharacter.JoiningType.TRANSPARENT;

@SuppressLint("InflateParams")
public class PhotoFragment extends Fragment {

    View v;

    public PhotoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_photo, container, false);

        if (getArguments() != null) {

            switch (Objects.requireNonNull(getArguments().getString(Common.Photo_Category_Intent_Key))){

                case "LipArt":
                    Category_Wise_Setup(Store_Data.Get_Photos(Common.LipArt_Key), Store_Data.Get_lipsArt_Data("Id"));
                    break;

                case "EyeMakeUp":
                    Category_Wise_Setup(Store_Data.Get_Photos(Common.EyeMakeUp_Key), Store_Data.Get_eyeMakeup_Data("Id"));
                    break;

                case "NailArt":
                    Category_Wise_Setup(Store_Data.Get_Photos(Common.NailArt_Key), Store_Data.Get_nailArt_Data("Id"));
                    break;

                case "MehediDesigns":
                    Category_Wise_Setup(Store_Data.Get_Photos(Common.MehediDesign_Key), Store_Data.Get_mehendiDesign_Data("Id"));
                    break;

                case "HairStyle":
                    /*Category_Wise_Setup(Store_Data.Get_Photos(Common.HairStyle_Key), Store_Data.Get_hairStyle_Data("Id"));*/
                    break;
            }
        }

        return v;
    }

    private void Category_Wise_Setup(final ArrayList<String> photos, final ArrayList<String> video_id) {
        ListView listView = v.findViewById(R.id.listView);
        listView.setAdapter(new ListViewAdapter(getContext(), photos));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogBox(photos.get(position), video_id.get(position));
            }
        });
    }

    private  void DialogBox(final String photoUrl, final String id){

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().getDecorView().setBackgroundColor(TRANSPARENT);
        alertDialog.show();

        final AppCompatButton photo_btn = view.findViewById(R.id.view_photo);
        final AppCompatButton video_btn = view.findViewById(R.id.view_video);
        final AppCompatImageButton cancel_btn = view.findViewById(R.id.cancel);

        photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnected(getContext())) {
                    PhotoView(photoUrl);
                }
                else {
                    Common.Custom_Toast(getContext(),"Please! Check your internet connection");
                }
                alertDialog.dismiss();
            }
        });

        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Common.isConnected(getContext())) {
                    Intent intent = new Intent(getActivity().getBaseContext(), YoutubePlayerActivity.class);
                    intent.putExtra(Common.Video_Id_Intent_Key, id);
                    getActivity().startActivity(intent);
                }
                else {
                    Common.Custom_Toast(getContext(),"Please! Check your internet connection");
                }
                alertDialog.dismiss();
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Common.isConnected(getContext())) {
                    Common.Custom_Toast(getContext(),"Please! Check your internet connection");
                }
                alertDialog.dismiss();
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private  void PhotoView(final String photoUrl){

        final View view = LayoutInflater.from(getContext()).inflate(R.layout.photo_dialog_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().getDecorView().setBackgroundColor(TRANSPARENT);
        alertDialog.show();

        final ImageViewTouch photo_Holder = view.findViewById(R.id.photoView);
        final AppCompatImageButton cancel_btn = view.findViewById(R.id.cancel);

        Glide.with(getContext())
                .load(photoUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .placeholder(R.color.colorDarkGray)    /* If Images are Loaded Very Slowly beacuse of slow internet */
                .error(R.drawable.ic_no_image)          /* If given url shows any error */
                .fallback(R.drawable.ic_no_image)       /* If pass a null url */
                .transition(DrawableTransitionOptions.withCrossFade(750)) /* Switching placeholder to origianl image with animation */
                .into(photo_Holder);

        photo_Holder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(cancel_btn.getVisibility() == View.GONE){

                    cancel_btn.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cancel_btn.setVisibility(View.GONE);
                        }
                    }, 1000);
                }
                return false;
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Common.isConnected(getContext())) {
                    Common.Custom_Toast(getContext(),"Please! Check your internet connection");
                }
                alertDialog.dismiss();
            }
        });
    }
}