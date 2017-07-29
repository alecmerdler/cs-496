package edu.oregonstate.cs496.merdlera.androidui.main;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/**
 * Created by alec on 7/29/17.
 */

public class ImageViewBindingAdapters {

    @BindingAdapter("imageURL")
    public static void loadGridImage(ImageView imageView, String imageURL) {
        Picasso.with(imageView.getContext()).load(imageURL).into(imageView);
    }
}
