====================================================================================
USE of CustomView
====================================================================================

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
 
    <com.hellohasan.androidcustomview.CustomView
        android:id="@+id/sunrise_custom_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        app:layout_constraintTop_toBottomOf="@+id/textView"
        app:setImageDrawable="@drawable/sun_rise"
        app:setTitle="Sun rise time - Dhaka, Bangladesh"
        tools:setSubTitle="5:25 AM" />
 
</androidx.constraintlayout.widget.ConstraintLayout>



====================================================================================
CustomView creation
====================================================================================

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
 
    <ImageView
        android:id="@+id/imageView"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:layout_marginStart="8dp"
        android:layout_marginTop="8dp"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        tools:ignore="ContentDescription"
        tools:src="@drawable/location" />
 
    <TextView
        android:id="@+id/titleTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="8dp"
        android:textSize="16sp"
        android:textStyle="bold"
        app:layout_constraintStart_toEndOf="@+id/imageView"
        app:layout_constraintTop_toTopOf="@+id/imageView"
        tools:text="This is title. It should be in one line" />
 
    <TextView
        android:id="@+id/subtitleTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="4dp"
        app:layout_constraintStart_toStartOf="@+id/titleTextView"
        app:layout_constraintTop_toBottomOf="@+id/titleTextView"
        tools:text="This is subtitle" />
 
    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:layout_marginTop="8dp"
        android:background="@color/color_divider"
        app:layout_constraintTop_toBottomOf="@id/imageView" />
 
</androidx.constraintlayout.widget.ConstraintLayout>



====================================================================================
add this in res/values/attrs.xml
====================================================================================

<?xml version="1.0" encoding="utf-8"?>
<resources>
 
    <declare-styleable name="CustomView">
        <attr name="setImageDrawable" format="reference" />
        <attr name="setTitle" format="string" />
        <attr name="setSubTitle" format="string" />
    </declare-styleable>
 
</resources>



====================================================================================
CustomView code
====================================================================================
class CustomView(context: Context, @Nullable attrs: AttributeSet) : ConstraintLayout(context, attrs) {
 
    private var view : View = LayoutInflater.from(context).inflate(R.layout.custom_view, this, true)
    private var imageView: ImageView
    private var titleTextView: TextView
    private var subtitleTextView: TextView
    private var imageDrawable : Drawable?
    private var title: String?
    private var subtitle: String?
 
    init {
        imageView = view.imageView as ImageView
        titleTextView = view.titleTextView as TextView
        subtitleTextView = view.subtitleTextView as TextView
 
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0)
 
        try {
            imageDrawable = typedArray.getDrawable(R.styleable.CustomView_setImageDrawable)
            title = typedArray.getString(R.styleable.CustomView_setTitle)
            subtitle = typedArray.getString(R.styleable.CustomView_setSubTitle)
 
            imageView.setImageDrawable(imageDrawable)
            titleTextView.text = title
            subtitleTextView.text = subtitle
        }
        finally {
            typedArray.recycle()
        }
 
        /** Uncomment below line if all of your attribute fields are required.
         * Throw an exception if required attributes are not set. It will caused Run Time Exception.
         * In this sample project we assume that, no attributes are mandatory
         * *
         */
        /*
        if (imageDrawable == null)
            throw RuntimeException("No image drawable provided")
        if (title == null) {
            throw RuntimeException("No title provided")
        }
        if (subtitle == null) {
            throw RuntimeException("No subtitle provided")
        }*/
    }
 
    /**
     * Below getter-setter will work, if we need to access the attributes programmatically
     */
 
    fun setImageDrawable(drawable: Drawable?) {
        imageView.setImageDrawable(drawable)
    }
    fun getImageDrawable() : Drawable? {
        return imageDrawable
    }
 
    fun setTitle(text: String?) {
        titleTextView.text = text
    }
    fun getTitle() : String? {
        return title
    }
 
    fun setSubtitle(text: String?) {
        subtitleTextView.text = text
    }
    fun getSubtitle() : String? {
        return subtitle
    }
}


====================================================================================
Application
====================================================================================

<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">
 
    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/custom_view_label"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
 
    <com.hellohasan.androidcustomview.CustomView
        android:id="@+id/sunrise_custom_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="24dp"
        app:layout_constraintTop_toBottomOf="@+id/textView"
        app:setImageDrawable="@drawable/sun_rise"
        app:setTitle="Sun rise time - Dhaka, Bangladesh"
        tools:setSubTitle="5:25 AM" />
 
    <com.hellohasan.androidcustomview.CustomView
        android:id="@+id/sunset_custom_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintTop_toBottomOf="@+id/sunrise_custom_view"
        app:setImageDrawable="@drawable/sunset"
        app:setTitle="Sunset time - Dhaka, Bangladesh"
        tools:setSubTitle="6:12 PM" />
 
</androidx.constraintlayout.widget.ConstraintLayout>




class MainActivity : AppCompatActivity() {
 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
 
        // image drawable and title are already set from xml.
        // I want to set subtitle programmatically. you can set title and image drawable from here
        sunrise_custom_view.setSubtitle("5:31 AM") // sunrise time set as subtitle
        sunset_custom_view.setSubtitle("5:01 PM") // subset time set as subtitle
    }
}