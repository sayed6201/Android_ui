


/*
------------------------------------------------------
source:  https://android.jlelse.eu/parallax-scrolling-header-tabs-android-tutorial-2cc6e40aa257
populate the viewpager with fragment
Lyout design
------------------------------------------------------
*/
<?xml version="1.0" encoding="utf-8"?>
<androidx.coordinatorlayout.widget.CoordinatorLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/htab_maincontent"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">

    <com.google.android.material.appbar.AppBarLayout
        android:id="@+id/htab_appbar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="?attr/colorPrimary"
        android:fitsSystemWindows="true"
        android:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar">

        <com.google.android.material.appbar.CollapsingToolbarLayout
            android:id="@+id/htab_collapse_toolbar"
            android:layout_width="match_parent"
            android:layout_height="256dp"
            android:fitsSystemWindows="true"
            app:contentScrim="?attr/colorPrimary"
            app:layout_scrollFlags="scroll|exitUntilCollapsed|snap"
            app:titleEnabled="false">

            <ImageView
                android:id="@+id/htab_header"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:background="@drawable/gradient_login_bg"
                android:fitsSystemWindows="true"
                android:scaleType="centerCrop"
                app:layout_collapseMode="parallax"/>

            <View
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:alpha="0.3"
                android:background="@android:color/black"
                android:fitsSystemWindows="true"/>

            <androidx.appcompat.widget.Toolbar
                android:id="@+id/htab_toolbar"
                android:layout_width="match_parent"
                android:layout_height="?attr/actionBarSize"
                android:layout_gravity="top"
                android:layout_marginBottom="48dp"
                app:layout_collapseMode="pin"
                app:popupTheme="@style/ThemeOverlay.AppCompat.Light"/>

            <com.google.android.material.tabs.TabLayout
                android:id="@+id/htab_tabs"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_gravity="bottom"
                app:tabIndicatorColor="@android:color/white"
                app:tabSelectedTextColor="@android:color/white"
                app:tabTextColor="@color/white"/>

        </com.google.android.material.appbar.CollapsingToolbarLayout>

    </com.google.android.material.appbar.AppBarLayout>

    <androidx.viewpager.widget.ViewPager
        android:id="@+id/htab_viewpager"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_behavior="@string/appbar_scrolling_view_behavior"/>

</androidx.coordinatorlayout.widget.CoordinatorLayout>