package edu.oregonstate.cs496.merdlera.androidui.main.locations;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityLocationsBinding;

/**
 * Created by alec on 8/3/17.
 */

public class LocationActivity extends AppCompatActivity {

    private CheckIn newCheckIn;
    private CheckInDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        newCheckIn = new CheckIn();

        dataSource = new CheckInDataSource(this);
        dataSource.open();

        // Set up data binding
        ActivityLocationsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_locations);
        binding.setNewCheckIn(newCheckIn);
    }

    public void onSubmit(View view) {
        if (newCheckIn.getComment().length() > 0) {
            dataSource.createCheckIn(newCheckIn.getComment());
        }
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
