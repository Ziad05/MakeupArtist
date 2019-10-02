package com.makeover.makeupartist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.snackbar.Snackbar;
import java.io.IOException;

public class Common {
    public static DrawerLayout Root_Layout;
    private static Snackbar snackbar;
    public static String Youtube_API_Key = "AIzaSyAhweqgpiA4nEnoxgGMkwj9GbU0M5w2dX0";
    public static String Video_Category_Intent_Key = "video_category_key";
    public static String Photo_Category_Intent_Key = "photo_category_key";
    public static String Video_Id_Intent_Key = "video_id_key";
    public static String LipArt_Key = "LipArt";
    public static String EyeMakeUp_Key = "EyeMakeUp";
    public static String NailArt_Key = "NailArt";
    public static String MehediDesign_Key = "MehediDesigns";
    public static String HairStyle_Key = "HairStyle";

    /* Check Internet Connection: */
    public static boolean isConnected(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();

            if (info != null){

                for (NetworkInfo anInfo : info){

                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {

                        /* Check if network connected but no internet */
                        Runtime runtime = Runtime.getRuntime();
                        try {
                            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                            int     exitValue = ipProcess.waitFor();
                            return (exitValue == 0);
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                }
            }

        }
        return false;
    }


    /* Custom SnackBar: */
    public static void Custom_SnackBar(final Context context, final View SnackBarLayout, final String SnackBarText){

        Typeface snackBarTf = Typeface.createFromAsset(context.getAssets(), "pf_regular.ttf");

        snackbar = Snackbar.make(SnackBarLayout, SnackBarText, Snackbar.LENGTH_LONG)
                .setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                })
                .setActionTextColor(context.getResources().getColor(R.color.colorWhite));

        Snackbar.SnackbarLayout s_layout = (Snackbar.SnackbarLayout) snackbar.getView();

        TextView textView = s_layout.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(15);
        textView.setTypeface(snackBarTf);
        //textView.setPadding(0,10,0,10);
        s_layout.setBackground(context.getResources().getDrawable(R.drawable.bg_snackbar));

        snackbar.show();
    }


    /* Custom Toast: */
    public static void Custom_Toast(Context context, String message){

        Typeface toastTf = Typeface.createFromAsset(context.getAssets(), "pf_regular.ttf");

        Toast customToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        customToast.setGravity(Gravity.CENTER,0,0);
        View toastView = customToast.getView();

        TextView textView = toastView.findViewById(android.R.id.message);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        textView.setTypeface(toastTf);

        toastView.setPadding(10,50,10,50);
        toastView.setBackgroundResource(R.drawable.bg_toast);

        customToast.show();
    }
}
