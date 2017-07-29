package edu.oregonstate.cs496.merdlera.androidui.main.horizontal;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityHorizontalBinding;

/**
 * Created by alec on 7/29/17.
 */

public class HorizontalActivity extends AppCompatActivity {

    public String[] imageSources = {
            "http://envyandroid.com/content/images/2015/03/android3.png",
            "http://www.myiconfinder.com/uploads/iconsets/1c99182403db6fa330f0b15024c587a9-android.png",
            "http://www.iconsdb.com/icons/preview/orange/android-4-xxl.png",
            "https://lh3.ggpht.com/XL0CrI8skkxnboGct-duyg-bZ_MxJDTrjczyjdU8OP2PM1dmj7SP4jL1K8JQeMIB3AM=w300",
            "https://cdn.sstatic.net/Sites/android/img/apple-touch-icon@2.png?v=8f2d3ecfa663",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up data binding
        ActivityHorizontalBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_horizontal);
        binding.setView(this);
    }

    @BindingAdapter("app:imageURL")
    public static void loadGridImage(ImageView imageView, String imageURL) {
        Picasso.with(imageView.getContext()).load(imageURL).into(imageView);
    }
}
