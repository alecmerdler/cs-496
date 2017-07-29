package edu.oregonstate.cs496.merdlera.androidui.main.infinite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import edu.oregonstate.cs496.merdlera.androidui.R;

/**
 * Created by alec on 7/29/17.
 */

public class InfiniteImageAdapter extends RecyclerView.Adapter<InfiniteImageAdapter.ViewHolder> {

    private String[] imageSources;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) view.findViewById(R.id.id_image_view);
        }
    }

    public InfiniteImageAdapter(String[] imageSources) {
        this.imageSources = imageSources;
    }

    @Override
    public InfiniteImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        // Load repeating images
        Picasso.with(viewHolder.imageView.getContext())
                .load(imageSources[position % imageSources.length])
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageSources.length;
    }
}
