
    
 Coroutines NOTE:
 ----------------
    # use suspend keyword to use a function in co-routine,
    # codes in suspend function excutes when the previous line is executedd and returned result
    
    #   Main : runs on ain thread
        IO : to access api or local DB
        Deafault: to run long running tasks 
//use these keywords to define the thread you wnat the code to be executed
        CoroutineScope(IO).launch{..}

    # you can show result in main thread using 
        withContext (Main) {
            val newText = text.text.toString() + "\n$input"
            text.text = newText
        }

   


Dependency:
-------------
 def coroutines_version = "1.2.1"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version"





====================================================================================
Example: 
    # coroutine to fake sequenceial api call

youtube link:
https://www.youtube.com/watch?v=F63mhZk-1-Y&list=PLuhrC0gjtNkXixdaBIMGy4CR6ONcxemRA&index=2&t=0s
====================================================================================
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            setNewText("Click!")
/*
    Main : runs on ain thread
    IO : to access api or local DB
    Deafault: to run long running tasks

*/
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
        
    }

    
    //this executes in main Thread
    //you can specify thread you want to execute the code in with withContext (Main)
    private suspend fun setTextOnMainThread(input: String) {
        withContext (Main) {
            val newText = text.text.toString() + "\n$input"
            text.text = newText
        }
    }

//codes in suspend function excutes when the previous line is executedd and returned result
    private suspend fun fakeApiRequest() {
        logThread("fakeApiRequest")

        val result1 = getResult1FromApi() // wait until job is done

        if ( result1.equals("Result #1")) {

            setTextOnMainThread("Got $result1")

            val result2 = getResult2FromApi() // wait until job is done

            if (result2.equals("Result #2")) {
                setTextOnMainThread("Got $result2")
            } else {
                setTextOnMainThread("Couldn't get Result #2")
            }
        } else {
            setTextOnMainThread("Couldn't get Result #1")
        }
    }


    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return "Result #2"
    }

    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

}