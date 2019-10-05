========================================================
// Write a message to the database
========================================================
FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference myRef = database.getReference("message");
myRef.setValue("Hello, World!");

===================================================
#Writing to Firebase
===================================================
//To read or write data from the database, you need an instance of DatabaseReference:
---------------------------------------------------------------------------------------------
private DatabaseReference mDatabase;
mDatabase = FirebaseDatabase.getInstance().getReference();

@IgnoreExtraProperties
public class User {
    public String username;
    public String email;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}

//You can add a user with setValue() as follows:
---------------------------------------------------------------------------------------------
private void writeNewUser(String userId, String name, String email) {
    User user = new User(name, email);
    mDatabase.child("users").child(userId).setValue(user);
}
mDatabase.child("users").child(userId).child("username").setValue(name);


========================================================
#READING FROM FIREBASE
========================================================


#1.                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Artist artist = postSnapshot.getValue(Artist.class);
                    //adding artist to the list 
                    artists.add(artist);
                }

From <https://www.simplifiedcoding.net/firebase-realtime-database-crud/> 



//To read data at a path and listen for changes, use the addValueEventListener()oraddListenerForSingleValueEvent() method to add //a ValueEventListener to aDatabaseReference.
---------------------------------------------------------------------------------------------
ValueEventListener postListener = new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        // Get Post object and use the values to update the UI
        Post post = dataSnapshot.getValue(Post.class);
        // ...
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        // Getting Post failed, log a message
        Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
        // ...
    }
};
mPostReference.addValueEventListener(postListener)
From <https://firebase.google.com/docs/database/android/read-and-write?authuser=1> 
---------------------------------------------------------------------------------------------
#2. 
        myRef.child("users/"+getUsernameFromEmain(user.getEmail())+"/request")
        .addListenerForSingleValueEvent(new ValueEventListener(){
        @Override
        Public void onDataChange(DataSnapshot dataSnapshot){

        	String request=(String)dataSnapshot.getValue();
        	inviteEt.setText(request);
        }

        @Override
        Public void onCancelled(DatabaseErrorerror){

        }
        });

#3.
        void IncommingRequest(){

            // Read from the database
            myRef.child("Users").child(BeforeAt(MyEmail)).child("Request")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try{
                        HashMap<String,Object> td=(HashMap<String,Object>) dataSnapshot.getValue();
                        if (td!=null){

                            String value;
                            for(String key:td.keySet()){
                                value=(String) td.get(key);
                                Log.d("User request",value);
                                etInviteEMail.setText(value);
                                ButtonColor();
                                myRef.child("Users").child(BeforeAt(MyEmail)).child("Request").setValue(uid);
                                break;
                            }
                        }

                    }catch (Exception ex){}
                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });

        }

#4.
        public void seasonalDiseaseRetrieve(){
            // Read from the database
            myRef.child("seasonal/bangaldesh/dhaka/summer/diseaseName")
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<String> diseaseList = new ArrayList<>();
                    ArrayList<String> diseaseKey = new ArrayList<>();

                    for(DataSnapshot disease: dataSnapshot.getChildren()){
                        diseaseList.add((String) disease.getValue());
                        diseaseKey.add(disease.getKey());
                    }
                    seasonalTv.setText(diseaseKey.toString()+" "+diseaseList.toString());
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value

                }
            });
        }
 


========================================================
#GETTING KEY FROM DATA
========================================================

That's because you're using a ValueEventListener. If the query matches multiple children, it returns a list of all those children. Even if there's only a single matches child, it's still a list of one. And since you're calling getKey() on that list, you get the key of the location where you ran the query.
To get the key of the matches children, loop over the children of the snapshot:
---------------------------------------------------------------------------------------------
mDatabase.child("clubs")
         .orderByChild("name")
         .equalTo("efg")
         .addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
            String clubkey = childSnapshot.getKey();


But note that if you assume that the club name is unique, you might as well store the clubs under their name and access the correct one without a query:
---------------------------------------------------------------------------------------------
mDatabase.child("clubs")
         .child("efg")
         .addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        String clubkey = dataSnapshot.getKey(); // will be efg

From <https://stackoverflow.com/questions/38232140/how-to-get-the-key-from-the-value-in-firebase/38232280#38232280> 


========================================================
#UPDATING DATA:
========================================================

You're not specifying the task id of the task that you want to update.
---------------------------------------------------------------------------------------------
    String taskId = "-K1NRz9l5PU_0CFDtgXz";
    Firebase m_objFireBaseRef = new Firebase(AppConstants.FIREBASE_URL);        
    Firebase objRef = m_objFireBaseRef.child("tasks");
    Firebase taskRef = objRef.child(taskId);
    Map<String,Object> taskMap = new HashMap<String,Object>();
    taskMap.put("Status", "COMPLETED");
    taskRef.updateChildren(taskMap);
    Alternatively, you can just call setValue() on the property you want to update
    String taskId = "-K1NRz9l5PU_0CFDtgXz";
    Firebase m_objFireBaseRef = new Firebase(AppConstants.FIREBASE_URL);        
    Firebase objRef = m_objFireBaseRef.child("tasks");
    Firebase taskRef = objRef.child(taskId);
    Firebase statusRef = taskRef.child("Status");
    statusRef.setValue("COMPLETED");
Or:
    Firebase m_objFireBaseRef = new Firebase(AppConstants.FIREBASE_URL);        
    Firebase objRef = m_objFireBaseRef.child("tasks");
    objRef.child(taskId).child("Status").setValue("COMPLETED");


Update
---------------------------------------------------------------------------------------------
mDatabase.child("users").child(Datasnapshot.getkey).child("username").setValue(name);

From <https://firebase.google.com/docs/database/android/read-and-write> 



Not sure what "I need to track the ID based on the status" means. But if you want to synchronize all tasks that are in status Pending, you'd do:
---------------------------------------------------------------------------------------------
    Firebase m_objFireBaseRef = new Firebase(AppConstants.FIREBASE_URL);        
    Firebase objRef = m_objFireBaseRef.child("tasks");
    Query pendingTasks = objRef.orderByChild("Status").equalTo("PENDING");
    pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot tasksSnapshot) {
            for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
                snapshot.getRef().child("Status").setValue("COMPLETED");
            }
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            System.out.println("The read failed: " + firebaseError.getMessage());
        }
    });

From <https://stackoverflow.com/questions/33315353/update-specific-values-using-firebase-for-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa> 


========================================================
#PREVENTING DUPLICATE DATA ENTRY:
========================================================

    mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                     //If email exists then toast shows else store the data on new key
                        if (!data.getValue(User.class).getEmail().equals(email)) {
                            mFirebaseDatabase.child(mFirebaseDatabase.push().getKey()).setValue(new User(name, email));
                        } else {
                            Toast.makeText(ChatListActivity.this, "E-mail already exists.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
    @Override
                public void onCancelled(final DatabaseError databaseError) {
                }
            });
