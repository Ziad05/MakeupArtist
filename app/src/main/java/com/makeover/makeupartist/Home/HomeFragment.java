package com.makeover.makeupartist.Home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makeover.makeupartist.Common;
import com.makeover.makeupartist.R;
import com.makeover.makeupartist.Store_Data;
import com.makeover.makeupartist.VideoGallery.RecyclerViewOnClickListener;
import com.makeover.makeupartist.VideoGallery.YoutubePlayerActivity;
import com.makeover.makeupartist.VideoGallery.YoutubeVideoAdapter;
import com.makeover.makeupartist.VideoGallery.YoutubeVideoModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    View v;

    public HomeFragment() {}

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        final ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = generateVideoList();
        YoutubeVideoAdapter adapter = new YoutubeVideoAdapter(getContext(), youtubeVideoModelArrayList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener((RecyclerView.OnItemTouchListener) new RecyclerViewOnClickListener(getContext(), new RecyclerViewOnClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                if(!Common.isConnected(getContext())){
                    Common.Custom_Toast(getContext(),"Please! Check your internet connection");
                }
                else {
                    Intent intent = new Intent(getActivity().getBaseContext(), YoutubePlayerActivity.class);
                    intent.putExtra(Common.Video_Id_Intent_Key, youtubeVideoModelArrayList.get(position).getVideoId());
                    getActivity().startActivity(intent);
                }
            }
        }));

        return v;
    }

    private ArrayList<YoutubeVideoModel> generateVideoList() {

        ArrayList<YoutubeVideoModel> youtubeVideoModelArrayList = new ArrayList<>();

        Category_Wise_Setup(youtubeVideoModelArrayList, Store_Data.Get_eyeMakeup_Data("Id"), Store_Data.Get_eyeMakeup_Data("Title"));

        return youtubeVideoModelArrayList;
    }

    private void Category_Wise_Setup(ArrayList<YoutubeVideoModel> VideoArrayList, ArrayList<String> Id, ArrayList<String>  Title) {

        if(Id.size() == 0){
            Common.Custom_Toast(getContext(),"No Video Available!");
        }
        else {
            for (int i = 0; i < Id.size(); i++) {
                YoutubeVideoModel youtubeVideoModel = new YoutubeVideoModel();

                youtubeVideoModel.setVideoId(Id.get(i));
                youtubeVideoModel.setTitle(Title.get(i));

                VideoArrayList.add(youtubeVideoModel);
            }
        }
    }
}
