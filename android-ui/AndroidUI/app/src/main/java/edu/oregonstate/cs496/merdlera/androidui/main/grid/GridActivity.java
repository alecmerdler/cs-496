package edu.oregonstate.cs496.merdlera.androidui.main.grid;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityGridBinding;

/**
 * Created by alec on 7/28/17.
 */

public class GridActivity extends AppCompatActivity {

    private int[] list = {1, 2, 3, 4, 5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up data binding
        ActivityGridBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_grid);
        binding.setView(this);
    }
}
