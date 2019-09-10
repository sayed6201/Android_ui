

SOURCE=CODE: 
https://www.techotopia.com/index.php?title=Kotlin_-_Creating_a_Tabbed_Interface_using_the_TabLayout_Component&mobileaction=toggle_view_mobile

==========================================================================================================
MyPagerAdapter.kt
==========================================================================================================
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sayed.kotlintesterapp.fragments.FirstFragment
import com.sayed.kotlintesterapp.fragments.SecondFragment
import com.sayed.kotlintesterapp.fragments.ThirdFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                FirstFragment()
            }
            1 -> SecondFragment()
            else -> {
                return ThirdFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "First Tab"
            1 -> "Second Tab"
            else -> {
                return "Third Tab"
            }
        }
    }
}





==========================================================================================================
MainActivity.kt
==========================================================================================================
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.sayed.kotlintesterapp.ui.main.SectionsPagerAdapter
import com.sayed.kotlintesterapp.ui.main.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentAdapter = MyPagerAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter
        tabs_main.setupWithViewPager(viewpager_main);
    }
}




==========================================================================================================
FirstFragment.kt
==========================================================================================================
import android.os.Bundle
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sayed.kotlintesterapp.R

class FirstFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_first, container, false)
    }
}




==========================================================================================================
activity_main.xml
==========================================================================================================
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context=".MainActivity">


    <com.google.android.material.tabs.TabLayout
            android:id="@+id/tabs_main"
            android:layout_width="0dp"
            android:layout_height="50dp"
            app:layout_constraintTop_toTopOf="parent" app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:tabIndicatorColor="#000000"
            app:tabRippleColor="@color/colorPrimary"
    />

    <androidx.viewpager.widget.ViewPager
            android:id="@+id/viewpager_main"
            android:layout_width="405dp"
            android:layout_height="664dp"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" android:layout_marginTop="8dp"
            app:layout_constraintTop_toBottomOf="@+id/tabs_main"/>

</androidx.constraintlayout.widget.ConstraintLayout>




==========================================================================================================
fragment_first.xml
==========================================================================================================
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
             xmlns:tools="http://schemas.android.com/tools"
             android:layout_width="match_parent"
             android:layout_height="match_parent"
             tools:context="me.eijaz.tabstutorial.FirstFragment">

    <TextView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="First tab"
            android:textSize="40sp"/>

</FrameLayout>












===========================================================================================================
Viewpager with icon
===========================================================================================================
package com.sayed.kotlintesterapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.sayed.kotlintesterapp.ui.main.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureTabLayout();
    }

    private fun configureTabLayout() {

        tabs_main.addTab(tabs_main.newTab().setText("Tab 1 Item").setIcon(R.drawable.ic_mtrl_chip_close_circle))
        tabs_main.addTab(tabs_main.newTab().setText("Tab 2 Item").setIcon(R.drawable.ic_mtrl_chip_close_circle))
        tabs_main.addTab(tabs_main.newTab().setText("Tab 3 Item").setIcon(R.drawable.ic_mtrl_chip_close_circle))
        tabs_main.addTab(tabs_main.newTab().setText("Tab 4 Item").setIcon(R.drawable.ic_mtrl_chip_close_circle))

        val adapter = MyPagerAdapter(supportFragmentManager,
            tabs_main.tabCount)
        viewpager_main.adapter = adapter

        viewpager_main.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(tabs_main))
        tabs_main.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewpager_main.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }

        })
    }

}

========================================================================================================
FragmentPagerAdapter for icon
========================================================================================================
package com.sayed.kotlintesterapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sayed.kotlintesterapp.fragments.FirstFragment
import com.sayed.kotlintesterapp.fragments.SecondFragment
import com.sayed.kotlintesterapp.fragments.ThirdFragment

class MyPagerAdapter(fm: FragmentManager, private var tabCount: Int) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {

        when (position) {
            0 -> return FirstFragment()
            1 -> return SecondFragment()
            2 -> return ThirdFragment()
            3 -> return FirstFragment()
            else -> return null
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}