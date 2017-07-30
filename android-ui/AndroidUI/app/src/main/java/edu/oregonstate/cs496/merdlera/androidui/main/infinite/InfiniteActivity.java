package edu.oregonstate.cs496.merdlera.androidui.main.infinite;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityInfiniteBinding;

/**
 * Created by alec on 7/29/17.
 */

public class InfiniteActivity extends AppCompatActivity {

    public String[] imageSources = {
            "http://envyandroid.com/content/images/2015/03/android3.png",
            "http://www.myiconfinder.com/uploads/iconsets/1c99182403db6fa330f0b15024c587a9-android.png",
            "http://www.iconsdb.com/icons/preview/orange/android-4-xxl.png",
            "https://lh3.ggpht.com/XL0CrI8skkxnboGct-duyg-bZ_MxJDTrjczyjdU8OP2PM1dmj7SP4jL1K8JQeMIB3AM=w300",
            "https://cdn.sstatic.net/Sites/android/img/apple-touch-icon@2.png?v=8f2d3ecfa663",
    };

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private int maxItemsPerRequest = imageSources.length;
    private String[] visibleImageSources = imageSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutManager = new LinearLayoutManager(getApplicationContext());

        // Set up data binding
        ActivityInfiniteBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_infinite);
        binding.setView(this);

        // Initialize infinite list
        recyclerView = (RecyclerView) findViewById(R.id.infinite_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new InfiniteImageAdapter(visibleImageSources));
        recyclerView.addOnScrollListener(new InfiniteScrollListener(maxItemsPerRequest, layoutManager) {
            @Override
            public void onScrolledToEnd(int firstVisibleItemPosition) {
                // Add another set of image sources to list
                visibleImageSources = new String[visibleImageSources.length + imageSources.length];
                for (int i = 0; i < visibleImageSources.length; i++) {
                    visibleImageSources[i] = imageSources[i % imageSources.length];
                }

                refreshView(recyclerView, new InfiniteImageAdapter(visibleImageSources), firstVisibleItemPosition);
            }
        });
    }
}
