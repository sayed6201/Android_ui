 
implementation 'com.github.clans:fab:1.6.4'
===================================================
XML code
===================================================
 <com.github.clans.fab.FloatingActionMenu
        android:id="@+id/material_design_android_floating_action_menu"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="bottom|end"
        android:layout_marginEnd="@dimen/fab_margin"
        android:visibility="visible"
        app:backgroundTint="@color/colorPrimary"
        app:elevation="6dp"
        app:pressedTranslationZ="12dp"
        android:layout_alignParentBottom="true"
        android:layout_marginLeft="11dp"
        android:layout_marginRight="11dp"
        android:layout_marginBottom="11dp"
        fab:menu_animationDelayPerItem="55"
        fab:menu_backgroundColor="@android:color/transparent"
        fab:menu_buttonSpacing="0dp"
        fab:menu_colorNormal="@color/colorPrimary"
        fab:menu_colorPressed="@color/colorPrimary"
        fab:menu_colorRipple="@color/colorPrimary"
        fab:menu_fab_label="Add post"
        fab:menu_fab_size="normal"
        fab:menu_icon="@drawable/fab_add"
        fab:menu_labels_colorNormal="#333"
        fab:menu_labels_colorPressed="#444"
        fab:menu_labels_colorRipple="#66efecec"
        fab:menu_labels_cornerRadius="3dp"
        fab:menu_labels_ellipsize="end"
        fab:menu_labels_hideAnimation="@anim/fab_slide_out_to_right"
        fab:menu_labels_margin="0dp"
        fab:menu_labels_maxLines="-1"
        fab:menu_labels_padding="8dp"
        fab:menu_labels_position="left"
        fab:menu_labels_showAnimation="@anim/fab_slide_in_from_right"
        fab:menu_labels_showShadow="true"
        fab:menu_labels_singleLine="false"
        fab:menu_labels_textColor="#000000"
        fab:menu_labels_textSize="15sp"
        fab:menu_openDirection="up"
        fab:menu_shadowColor="#66aff198"
        fab:menu_shadowRadius="4dp"
        fab:menu_shadowXOffset="1dp"
        fab:menu_shadowYOffset="4dp"
        fab:menu_showShadow="true"
        >

        <com.github.clans.fab.FloatingActionButton
            android:id="@+id/add_food"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_burger"
            fab:fab_label="Donate food"
            app:backgroundTint="@color/colorPrimary"
            fab:fab_size="mini" />

        <com.github.clans.fab.FloatingActionButton
            android:id="@+id/add_cloth"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_tshirt"
            fab:fab_label="Donate cloths"
            app:backgroundTint="@color/colorPrimary"
            fab:fab_size="mini" />

        <com.github.clans.fab.FloatingActionButton
            android:id="@+id/add_book"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/ic_book"
            fab:fab_label="Donate book"
            app:backgroundTint="@color/colorPrimary"
            fab:fab_size="mini" />
    </com.github.clans.fab.FloatingActionMenu>