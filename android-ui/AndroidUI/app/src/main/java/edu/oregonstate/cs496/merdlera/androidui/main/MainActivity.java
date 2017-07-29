package edu.oregonstate.cs496.merdlera.androidui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.oregonstate.cs496.merdlera.androidui.R;
import edu.oregonstate.cs496.merdlera.androidui.databinding.ActivityMainBinding;
import edu.oregonstate.cs496.merdlera.androidui.main.grid.GridActivity;

public class MainActivity extends AppCompatActivity implements Contract.View {

    private Contract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.presenter = new Presenter(this);

        // Set up data binding
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setPresenter(presenter);
    }

    @Override
    public void showGrid() {
        Intent intent = new Intent(this, GridActivity.class);
        startActivity(intent);
    }

    @Override
    public void showVerticalList() {

    }

    @Override
    public void showHorizontalList() {

    }

    @Override
    public void showInfiniteList() {

    }
}
