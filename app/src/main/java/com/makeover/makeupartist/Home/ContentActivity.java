package com.makeover.makeupartist.Home;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.makeover.makeupartist.Common;
import com.makeover.makeupartist.PhotoGallery.PhotoFragment;
import com.makeover.makeupartist.R;
import com.makeover.makeupartist.VideoGallery.VideoFragment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity {

    private CardView lipArt_Action_Panel, eyeMakeup_Action_Panel, nailArt_Action_Panel, mehediDesign_Action_Panel, hairStyle_Action_Panel;
    private AppCompatImageButton drawer_btn, refresh_btn;
    private InterstitialAd mInterstitialAd;
    private String fragment_title = "";
    private String fragment_category = "";
    private ArrayList<String> fragment_title_list = new ArrayList<>();
    private ArrayList<String> fragment_category_list = new ArrayList<>();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Id_Setup();
        ActionBar_Items();
        Default_Fragment(savedInstanceState);

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        fragment_title_list.add("Home");
        fragment_category_list.add("");


    }

    @Override protected void onStart() {
        super.onStart();
        if(!Common.isConnected(getApplicationContext())){
            Common.Custom_Toast(getApplicationContext(),"Please! Check your internet connection");
        }
    }

    private void Id_Setup() {
        Common.Root_Layout = findViewById(R.id.drawer_layout);
        drawer_btn = findViewById(R.id.drawer_btn_id);
        refresh_btn = findViewById(R.id.refresh_btn_id);
        lipArt_Action_Panel = findViewById(R.id.lipArt_action_panel);
        eyeMakeup_Action_Panel = findViewById(R.id.eyeMakeup_action_panel);
        nailArt_Action_Panel = findViewById(R.id.nailArt_action_panel);
        mehediDesign_Action_Panel = findViewById(R.id.mehediDesign_action_panel);
        hairStyle_Action_Panel = findViewById(R.id.hairStyle_action_panel);
    }

    private void ActionBar_Items() {

        AutoWritingText appName = findViewById(R.id.appName);
        appName.setCharacterDelay(125);  /*Add a character every 125ms*/
        appName.animateText(getResources().getString(R.string.app_name));
        appName.setTypeface(Typeface.createFromAsset(getAssets(), "alex_brush.ttf"));

/*
        mInterstitialAd.setAdListener(new AdListener(){
            public void onAdClosed() {
                Common.Root_Layout.openDrawer(Gravity.START);
            }

                                      });
*/
        /*findViewById(R.id.drawer_btn_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });*/


        drawer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.Root_Layout.isDrawerOpen(GravityCompat.START)) {
                    Common.Root_Layout.closeDrawer(GravityCompat.START);  /*Close Nav ActionBar_Items!*/
                }
                else {
                    if(mInterstitialAd.isLoaded() && Common.isConnected(getApplicationContext())){
                        mInterstitialAd.show();
                    }
                    Common.Root_Layout.openDrawer(GravityCompat.START);  /*Open Nav ActionBar_Items!*/
                }
            }

        });


        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment_Setup(fragment_title, fragment_category);
            }
        });
    }

    private void Default_Fragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            HomeFragment home = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, home).commit();
        }
    }

    public void Drawer_Items(View view) {
        if(view.getId() == R.id.drawer_home_btn){
            if(!Common.isConnected(getApplicationContext())) {
                Common.Custom_Toast(getApplicationContext(),"Please! Check your internet connection");
            }
            else {
                Save_Data("Home", "");
                HomeFragment home = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, home).commit();
            }
            Common.Root_Layout.closeDrawer(GravityCompat.START);  /*Close Nav ActionBar_Items!*/
        }
        else if(view.getId() == R.id.drawer_lip_art_ideas_btn){
            Show_Category_Actions(lipArt_Action_Panel, eyeMakeup_Action_Panel, nailArt_Action_Panel, mehediDesign_Action_Panel, hairStyle_Action_Panel);
        }
        else if(view.getId() == R.id.drawer_eye_makeup_btn){
            Show_Category_Actions(eyeMakeup_Action_Panel, lipArt_Action_Panel, nailArt_Action_Panel, mehediDesign_Action_Panel, hairStyle_Action_Panel);
        }
        else if(view.getId() == R.id.drawer_nail_art_designs_btn){
            Show_Category_Actions(nailArt_Action_Panel, lipArt_Action_Panel, eyeMakeup_Action_Panel, mehediDesign_Action_Panel, hairStyle_Action_Panel);
        }
        else if(view.getId() == R.id.drawer_mehedi_designs_btn){
            Show_Category_Actions(mehediDesign_Action_Panel, lipArt_Action_Panel, eyeMakeup_Action_Panel, nailArt_Action_Panel, hairStyle_Action_Panel);
        }
        else if(view.getId() == R.id.drawer_hair_style_btn){
            Show_Category_Actions(hairStyle_Action_Panel, lipArt_Action_Panel, eyeMakeup_Action_Panel, nailArt_Action_Panel, mehediDesign_Action_Panel);
        }
        else if(view.getId() == R.id.drawer_share_btn){
            Common.Root_Layout.closeDrawer(GravityCompat.START);  /*Close Nav ActionBar_Items!*/
            if(isPermissionGranted(111)){
                Share_Apk();
            }
        }
    }

    private void Show_Category_Actions(CardView action_panel_1, CardView action_panel_2, CardView action_panel_3, CardView action_panel_4, CardView action_panel_5) {

        if(action_panel_1.getVisibility() == View.GONE){
            action_panel_1.setVisibility(View.VISIBLE);

            for(CardView action_panel: new CardView[]{action_panel_2, action_panel_3, action_panel_4, action_panel_5}){
                action_panel.setVisibility(View.GONE);
            }
        }
        else {
            action_panel_1.setVisibility(View.GONE);
        }
    }

    private void Fragment_Setup(String title, String category) {
        if(!Common.isConnected(getApplicationContext())) {
            Common.Custom_Toast(getApplicationContext(),"Please! Check your internet connection");
        }
        else{
            switch (title){
                case "Home":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, new HomeFragment()).commit();
                    break;

                case "Photo":
                    switch (fragment_category){
                        case "LipArt":
                            Go_To_Photo_Gallery(Common.LipArt_Key, false);
                            break;

                        case "EyeMakeUp":
                            Go_To_Photo_Gallery(Common.EyeMakeUp_Key, false);
                            break;

                        case "NailArt":
                            Go_To_Photo_Gallery(Common.NailArt_Key, false);
                            break;

                        case "MehediDesigns":
                            Go_To_Photo_Gallery(Common.MehediDesign_Key, false);
                            break;

                        case "HairStyle":
                            /*Go_To_Photo_Gallery(Common.HairStyle_Key, false);*/
                            break;

                        default:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, new HomeFragment()).commit();
                    }
                    break;

                case "Video":
                    switch (category){
                        case "LipArt":
                            Go_To_Video_Gallery(Common.LipArt_Key, false);
                            break;

                        case "EyeMakeUp":
                            Go_To_Video_Gallery(Common.EyeMakeUp_Key, false);
                            break;

                        case "NailArt":
                            Go_To_Video_Gallery(Common.NailArt_Key, false);
                            break;

                        case "MehediDesigns":
                            Go_To_Video_Gallery(Common.MehediDesign_Key, false);
                            break;

                        case "HairStyle":
                            /*Go_To_Video_Gallery(Common.HairStyle_Key, false);*/
                            break;

                        default:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, new HomeFragment()).commit();
                    }
                    break;

                default:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, new HomeFragment()).commit();
            }
        }
    }

    public void Click_Photo(View view) {
        if(Common.isConnected(getApplicationContext())) {

            if (view.getId() == R.id.lip_art_photos_btn) {
                Go_To_Photo_Gallery(Common.LipArt_Key, true);
            }
            else if (view.getId() == R.id.eye_Makeup_photos_btn) {
                Go_To_Photo_Gallery(Common.EyeMakeUp_Key, true);
            }
            else if (view.getId() == R.id.nail_art_photos_btn) {
                Go_To_Photo_Gallery(Common.NailArt_Key, true);
            }
            else if (view.getId() == R.id.mehedi_designs_photos_btn) {
                Go_To_Photo_Gallery(Common.MehediDesign_Key, true);
            }
            else if (view.getId() == R.id.hair_style_photos_btn) {
                Common.Custom_Toast(getApplicationContext(), "Sorry! no photo available");
                /*Go_To_Photo_Gallery(Common.HairStyle_Key, true);*/
            }
        }
        else {
            Common.Custom_Toast(getApplicationContext(),"Please! Check your internet connection");
        }

        Common.Root_Layout.closeDrawer(GravityCompat.START);  /*Close Nav ActionBar_Items!*/
    }

    private void Go_To_Photo_Gallery(String category, boolean isDataSave){
        if(isDataSave){
            Save_Data("Photo", category);
        }
        PhotoFragment photo = new PhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Common.Photo_Category_Intent_Key, category);
        photo.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, photo).commit();
    }

    public void Click_Video(View view) {

        if(Common.isConnected(getApplicationContext())){

            if(view.getId() == R.id.lip_art_videos_btn){
                Go_To_Video_Gallery(Common.LipArt_Key, true);
            }
            else if(view.getId() == R.id.eye_Makeup_videos_btn){
                Go_To_Video_Gallery(Common.EyeMakeUp_Key, true);
            }
            else if(view.getId() == R.id.nail_art_videos_btn){
                Go_To_Video_Gallery(Common.NailArt_Key, true);
            }
            else if(view.getId() == R.id.mehedi_designs_videos_btn){
                Go_To_Video_Gallery(Common.MehediDesign_Key, true);
            }
            else if(view.getId() == R.id.hair_style_videos_btn){
                Common.Custom_Toast(getApplicationContext(), "Sorry! no video available");
                /*Go_To_Video_Gallery(Common.HairStyle_Key, true);*/
            }
        }
        else {
            Common.Custom_Toast(getApplicationContext(),"Please! Check your internet connection");
        }

        Common.Root_Layout.closeDrawer(GravityCompat.START);  /*Close Nav ActionBar_Items!*/
    }

    private void Go_To_Video_Gallery(String category, boolean isDrawerItemClicked){
        if(isDrawerItemClicked){
            Save_Data("Video", category);
        }
        VideoFragment video = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Common.Video_Category_Intent_Key, category);
        video.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_content_id, video).commit();
    }

    private void Save_Data(String title, String category){
        fragment_title = title;
        fragment_category = category;
        if(!(fragment_title_list.get(fragment_title_list.size()-1).equals(title) && fragment_category_list.get(fragment_category_list.size()-1).equals(category))){
            fragment_title_list.add(title);
            fragment_category_list.add(category);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {

            switch (requestCode) {
                case 111:
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {

                        /* If user rejected the permission */
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                            if (! shouldShowRequestPermissionRationale(permissions[i])) {

                                new AlertDialog.Builder(this, R.style.CustomDialogTheme)
                                        .setCancelable(true)
                                        .setTitle("Storage Permission")
                                        .setMessage("To share this app, allow "+ getResources().getString(R.string.app_name) +" access to your Storage Option. Tap Settings, Permission and turn Storage on")
                                        .setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_folder))
                                        .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getApplicationContext().getPackageName()));
                                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                getApplicationContext().startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                /*You can code here, by default cancel dialog*/
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                            else if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permissions[i])) {
                                /*If user did NOT check "never ask again*/
                            }
                        }
                    }
                    else {
                        Share_Apk();
                    }
                    break;
            }
        }
    }

    public boolean isPermissionGranted(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            }
        }
        else {
            return true;
        }
    }

    private void Share_Apk() {
        try {
            File initialApkFile = new File(getPackageManager().getApplicationInfo(getPackageName(), 0).sourceDir);

            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");

            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;

            tempFile = new File(tempFile.getPath() + "/" + getResources().getString(R.string.app_name) + ".apk");

            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }

            InputStream in = new FileInputStream(initialApkFile);
            OutputStream out = new FileOutputStream(tempFile);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("*/*");
            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(tempFile));
            startActivity(Intent.createChooser(share, "Share " + getResources().getString(R.string.app_name) + " Via"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void Share_App() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        String subject = getResources().getString(R.string.app_name);
        String body = "\nLet me recommend you this application\n\n" + "Get this app from play store  " + Uri.parse("https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());

        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(intent, "Share " + getResources().getString(R.string.app_name) + " Via"));
    }

    @Override public void onBackPressed() {

        try {
            fragment_title_list.remove(fragment_title_list.size()-1);
            fragment_category_list.remove(fragment_category_list.size()-1);

            if(fragment_title_list.size()>0 && fragment_category_list.size()>0){
                fragment_title = fragment_title_list.get(fragment_title_list.size()-1);
                fragment_category = fragment_category_list.get(fragment_category_list.size()-1);
                Fragment_Setup(fragment_title, fragment_category);
            }
            else {
                fragment_title_list.clear();
                fragment_category_list.clear();
                super.onBackPressed();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
            fragment_title_list.clear();
            fragment_category_list.clear();
            super.onBackPressed();
        }
    }
}
