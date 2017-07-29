package edu.oregonstate.cs496.merdlera.androidui.main;

/**
 * Created by alec on 7/28/17.
 */

public class Presenter implements Contract.Presenter {

    private final Contract.View view;

    public Presenter(Contract.View view) {
        this.view = view;
    }

    @Override
    public void onShowGrid() {
        view.showGrid();
    }

    @Override
    public void onShowVerticalList() {
        view.showVerticalList();
    }

    @Override
    public void onShowHorizontalList() {
        view.showHorizontalList();
    }

    @Override
    public void onShowInfiniteList() {
        view.showInfiniteList();
    }
}
