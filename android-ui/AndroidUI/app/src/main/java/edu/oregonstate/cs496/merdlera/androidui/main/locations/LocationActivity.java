package edu.oregonstate.cs496.merdlera.androidui.main.locations;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityLocationsBinding;

/**
 * Created by alec on 8/3/17.
 */

public class LocationActivity extends AppCompatActivity {

    private CheckIn newCheckIn;


    // TODO: Move database logic out of activity
    private static class CheckInEntry implements BaseColumns {
        public static final String TABLE_NAME = "check_in";
        public static final String COLUMN_NAME_MESSAGE = "message";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newCheckIn = new CheckIn();

        // Set up data binding
        ActivityLocationsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_locations);

        binding.setNewCheckIn(newCheckIn);
    }
}
