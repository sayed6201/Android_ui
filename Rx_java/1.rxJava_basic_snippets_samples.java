====================================================================================
Example: Rxjava Snippets

blog link:
https://codingwithmitch.com/courses/rxjava-rxandroid-for-beginners/rxjava-operators-create-just-range-repeat/

Some operators:
--------------=
    # create : 
    # just : You can only pass a maximum of 10 objects to the just() operator
    # range :  you pass a min value and a max value. It will emit all the values in the range 
    # repeat : repeat must be used in conjunction with another operator.
    # timer : delaying operation
====================================================================================


//--------------------------------------------------------------------------------------
   // Example: RxJava Syntax with JAVA
//--------------------------------------------------------------------------------------


Operator: create, just, 
Obserables: Observable, Single
--------------------------------
public class MainActivity extends AppCompatActivity {

    //use composite disposible to add musltiple observer, and dispose on onDestroy()  
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    
    //Single observable of integer type
    Single<Integer> serverDownloadObservable;
    
    //Observable is used for lists/collections
    Observable<List<String>> namesFromServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CompositeDisposable compositeDisposable = new CompositeDisposable();

        //singleObserver <Integer> sample
        serverDownloadObservable = Single.create(emitter -> {
            SystemClock.sleep(1000); // simulate delay
            emitter.onSuccess(1);
        });

        serverDownloadObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("onSubscribe","called");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        Log.d("onSuccess","Your interger: "+integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onError","onError: "+e.toString());
                    }
                });

        List<String> list = new ArrayList<>();
        list.add("sayed");
        list.add("samie");

//You can only pass a maximum of 10 objects to the just() operator...
        namesFromServer = Observable.just(list);

        namesFromServer
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d("onSubscribe","called");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<String> strings) {
                        Log.d("onNext","data: "+strings);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("onError",e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("onComplete","called");
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //dispose all oberservers ...
        compositeDisposable.dispose();
    }
}


//--------------------------------------------------------------------------------------
   // create operator
            emitter.onNext(5);
            emitter.onComplete();
            can be called from create to notify the subscriber
//--------------------------------------------------------------------------------------

1. create operator :
--------------------

serverDownloadObservable = Observable.create(emitter -> {
            SystemClock.sleep(1000); // simulate delay
            emitter.onNext(5);
            emitter.onComplete();
        });

        serverDownloadObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


2. Create operator with for loop:
----------------------------------
Observable<Task> taskListObservable = Observable
    .create(new ObservableOnSubscribe<Task>() {
        @Override
        public void subscribe(ObservableEmitter<Task> emitter) throws Exception {
        
            // Inside the subscribe method iterate through the list of tasks and call onNext(task)
            for(Task task: DataSource.createTasksList()){
                if(!emitter.isDisposed()){
                    //it will pass item one by one
                    emitter.onNext(task);
                }
            }
            // Once the loop is complete, call the onComplete() method
            if(!emitter.isDisposed()){
                emitter.onComplete();
            }

        }
    })
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread());


//--------------------------------------------------------------------------------------
   // Range, repeat, timer Operator
//--------------------------------------------------------------------------------------
1. range and repeat Operator    :
----------------------------------
    Observable.range(0,3)
        .repeat(2)
        .observeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

Output:
onNext: 0
onNext: 1
onNext: 2
onNext: 0
onNext: 1
onNext: 2




2. timer, repeat
//creating timer with Rxjava
----------------------------------

Disposable disposable = Observable.timer(5000, TimeUnit.MILLISECONDS)
            .repeat() //to perform your task every 5 seconds
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("ComingHere", "Inside_Timer")
            }


//--------------------------------------------------------------------------------------
   // getting data from Api + local caching
//--------------------------------------------------------------------------------------
 
class UserRepository(val userApi: UserApi, val userDao: UserDao) {
fun getUsers(): Observable<List<User>> {        
    return Observable.concatArray(                
             getUsersFromDb(),                
             getUsersFromApi())}  
}


//proper way, considering different retrieve time, error handling
fun getUsers(errorCallback: ErrorCallback): Observable<List<User>> {        
    return Observable.concatArrayEager(                
             getUsersFromDb(),                
             getUsersFromApi()
                .materialize()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map {
                  // if the observables onError is called, invoke 
                  // callback so that presenters can handle error
                  it.error?.let {      
                      handleErrorCallback(it, errorCallback)
                  } 
                  // put item back into stream
                  it
                 }
                .filter{ !it.isOnError }
                .dematerialize<List<User>>()
                .debounce(400, MILLISECONDS)
             )}


