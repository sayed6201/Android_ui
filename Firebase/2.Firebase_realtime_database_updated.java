=====================================================================================================
Singletone Class to create firebase Object
=====================================================================================================
public class FirebaseConnector {

    private static FirebaseDatabase database;
    private static DatabaseReference myRef;

    public static DatabaseReference getInstanceFirebaseRef(){
        if(myRef == null){
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("hospitals");
        }
        return myRef;
    }


}

=====================================================================================================
Retrieving Data as an object 
=====================================================================================================

public void dataRetrieve(){
        // Write a message to the database

        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                List<Hospitals> list = new ArrayList<>();
                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
                    String key = childSnapshot.getKey();
                    Hospitals hospital = childSnapshot.child("information").getValue(Hospitals.class);
                    hospital.setKey(key);
//                    Log.i("key",key);
//                    Toast.makeText(MainActivity.this, ""+key, Toast.LENGTH_SHORT).show();
                    list.add(hospital);
                }
                HospitalAdapter adapter = new HospitalAdapter(MainActivity.this,list);
                hospitalRecyclerview.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                hospitalRecyclerview.setHasFixedSize(true);
                hospitalRecyclerview.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("EROOR", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        FirebaseConnector.getInstanceFirebaseRef().addValueEventListener(postListener);
    }



=====================================================================================================
Edititng Data as an object
=====================================================================================================
public void saveAction(View v){
      
        Hospitals hospital =
                new Hospitals(
                    hospitalNameEt.getText().toString(),
                    hospitalDesEt.getText().toString(),
                    hospitalPhoneEt.getText().toString(),
                    hospitalLocationEt.getText().toString(),
                    hospitalLocationLinkEt.getText().toString(),
                    hospitalWebsiteEt.getText().toString()
                );
//geenerate unique key.........
        String id = myRef.push().getKey();
        myRef.child(id).child("information").setValue(hospital);
        Toast.makeText(this, "Hospital added", Toast.LENGTH_SHORT).show();
    }



=====================================================================================================
Edititng Data as an object 
=====================================================================================================
    public void editAction(View v){

        Hospitals hospitals = new Hospitals(hospitalNameEt.getText().toString().trim(),hospitalDesEt.getText().toString().trim(),hospitalPhoneEt.getText().toString().trim(),hospitalLocationEt.getText().toString().trim(),hospitalLocationLinkEt.getText().toString().trim(),hospitalWebsiteEt.getText().toString().trim());
        FirebaseConnector.getInstanceFirebaseRef().child(hospitalId).child("information").setValue(hospitals);

        Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
    }