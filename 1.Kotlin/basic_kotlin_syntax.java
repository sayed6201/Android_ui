package com.sayed.kotlintesterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var tv: TextView?=null;
    lateinit var et: EditText;
    lateinit var btn: Button;
    var num1:Double = 20.5;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv);
        et = findViewById(R.id.et);
        btn = findViewById(R.id.button);
        
//use following import to avoid findViewByID++++++++++++++++++++++++++++++++++++
import kotlinx.android.synthetic.main.activity_main.*


//        tv?.setText("sayed");
        tv?.text = "hellow";
        et.setHint("Enter Your Text Here");

        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv?.setText(s);
            }
        })

        btn.setOnClickListener {
            num1 = num1 + et.text.toString().toDouble();
            Toast.makeText(this@MainActivity,"Result: "+num1, Toast.LENGTH_SHORT).show();
        }
        
    }

    fun clicker(v: View){
        if(v.id == R.id.imageView){
            Toast.makeText(this@MainActivity," Imageview ", Toast.LENGTH_SHORT).show();
        }
    }

}



====================================================================================
OOP, Static fields,
====================================================================================

class MyClass { 

//getters
val downloadedMovieResponse : LiveData<MovieDetailData> by lazy {
        movieRepository.downloadedMovieResponse
    }

  companion object {
  val a: string;
    val info = "This is info"    
    fun getMoreInfo():String { return "This is more fun" }

    init{
        a="gfd";
    }
  } 
}
MyClass.info             // This is info
MyClass.getMoreInfo()    // This is more fun



====================================================================================
Singletones
---------------
If you need to adjust when the singleton object should be initilized, 
you can create one object for each class.
====================================================================================


---------------------------------------------------------------------------------------------------------
//singletone: 0
---------------------------------------------------------------------------------------------------------
object ServiceBuilder {
    private val client = OkHttpClient
        .Builder()
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(TmdbEndpoints::class.java)

    fun buildService(): TmdbEndpoints {
        return retrofit
    }
}

---------------------------------------------------------------------------------------------------------
//singletone: 1
---------------------------------------------------------------------------------------------------------
class UserRepository(application: Application) {

    companion object{
        var userRepository : UserRepository ?= null;
        //userData live
        var userDataLiveData : MutableLiveData<UserData>  = MutableLiveData();
        //user
        var userDataProgressbar : MutableLiveData<Boolean> =  MutableLiveData();
        //userData error
        var  userDataError : MutableLiveData<Throwable>  = MutableLiveData();
        
        
        fun getInstance( application : Application) : UserRepository{
            if (userRepository == null){
                userRepository =  UserRepository(application);
            }
            return userRepository as UserRepository;
        }
    }

    var application: Application ? = application;

    }
 val c = AnotherClass.instance
}

---------------------------------------------------------------------------------------------------------
//singletone: 2
---------------------------------------------------------------------------------------------------------
class UtilProject {
    ....
    companion object {
        val instance = UtilProject()
    }
}

class AnotherClass {
    ...
    companion object {
        val instance = AnotherClass()
        const val abc = "ABC"
    }
}

fun main(args: Array<String>) {
    val a = UtilProject.instance // UtilProject.instance will be initialized here.
    val b = AnotherClass.abc // AnotherClass.instance will be initialized here because AnotherClass's companion object is instantiated.
   


====================================================================================
constructor in kotlin
====================================================================================

---------------------------------------------------------------------------------------------------------
//primary constructors
---------------------------------------------------------------------------------------------------------

class UserRepository(application: Application) {

    companion object{
        var userRepository : UserRepository ?= null;
        //userData live
        var userDataLiveData : MutableLiveData<UserData>  = MutableLiveData();
        //user
        var userDataProgressbar : MutableLiveData<Boolean> =  MutableLiveData();
        //userData error
        var  userDataError : MutableLiveData<Throwable>  = MutableLiveData();
        
        
        fun getInstance( application : Application) : UserRepository{
            if (userRepository == null){
                userRepository =  UserRepository(application);
            }
            return userRepository as UserRepository;
        }
    }

    var application: Application ? = application;

    }

====================================================================================
open calss and inheritance 
====================================================================================
open class Animal { // Parent class
    var name: String? = null // Nullable variable
    var legs: Int = 0 // Non-nullable variable
    lateinit var map: HashMap<Integer, String> // Variable inited later in the code

    constructor(legs: Int) {
        this.legs = legs
    }
    constructor(legs: Int, name: String) {
        this.legs = legs
        this.name = name
    }
// open keyword allows the function to be overridden
    open fun speak() : String? {
        return null
    }
}

class Dog : Animal { // Child class
    constructor(legs: Int) : super(legs) {
        // Optional code block
    }
// Just a super call, without additional code block
    constructor(legs: Int, name: String) : super(legs, name)
       // Function over-ridding
        override fun speak(): String? {
            return "Bark! Bark!"
        }
}
