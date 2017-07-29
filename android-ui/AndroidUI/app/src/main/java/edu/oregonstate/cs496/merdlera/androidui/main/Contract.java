package edu.oregonstate.cs496.merdlera.androidui.main;

/**
 * Created by alec on 7/28/17.
 */

public interface Contract {

    interface View {

        void showGrid();

        void showVerticalList();

        void showHorizontalList();

        void showInfiniteList();
    }

    interface Presenter {

        void onShowGrid();

        void onShowVerticalList();

        void onShowHorizontalList();

        void onShowInfiniteList();
    }
}
