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
Retrieving Data once only... 
using: addListenerForSingleValueEvent(postListener) 
not:  addValueEventListener(postListener)
=====================================================================================================
private void prepareAttendence(final String date) {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Generating Qr code...");
        pDialog.show();

        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot != null) {
                        String id = childSnapshot.getKey();
                        Student std = childSnapshot.child(App.information).getValue(Student.class);

                        if (id != null && std != null) {
                            Attendence attendence =
                                    new Attendence(
                                            date,
                                            mParam1,
                                            id,
                                            "no",
                                            std.getName()
                                    );

                            App.classRef
                                    .child(mParam1)
                                    .child(App.registered_student)
                                    .child(id)
                                    .child(App.attendance)
                                    .child(date)
                                    .setValue(attendence)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
//                        Toast.makeText(getContext(), "Hospital added", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }
                }

                pDialog.hide();
                pDialog.cancel();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("EROOR", "loadPost:onCancelled", databaseError.toException());
                pDialog.hide();
                pDialog.cancel();
            }

        };
        App.classRef.child(mParam1)
                .child(App.registered_student)
                .addListenerForSingleValueEvent(postListener);

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

    ------------------------------------------------------
    more retrieve
    ------------------------------------------------------

    public void dataRerieveLocationById(String locationId) {

        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Retrieving...");
        pDialog.show();

        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String uid = dataSnapshot.child("uid").getValue(String.class);

                assert uid != null;
                if(uid.equals(FirebaseAuthController.getCurrentUser().getUid())){
                    Location location = dataSnapshot.child("information").getValue(Location.class);

                    assert location != null;
                    nameEt.setText(location.getLocationName());
                    costEt.setText(location.getCost());
                    locationDesEt.setText(location.getLocationDescription());
                    submittedByEt.setText(location.getSubmittedBy());
                    locationEt.setText(location.getLocation());
                }

                pDialog.hide();
                pDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("EROOR", "loadPost:onCancelled", databaseError.toException());
                pDialog.hide();
                pDialog.cancel();
            }
        };
        App.myPendingRef.child(locationId).addValueEventListener(postListener);
    }



    ------------------------------------------------------
    retrieving Complex data, child of child.........
    ------------------------------------------------------
     private void getAttandenceByDate(final String date) {


        final ProgressDialog pDialog = new ProgressDialog(getContext());
        pDialog.setMessage("Retrieving...");
        pDialog.show();

        ValueEventListener postListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String id = childSnapshot.getKey();
                    if(id != null){

                        for(DataSnapshot childOfChild : childSnapshot.child(App.attendance).getChildren()){
                            String date = childOfChild.getKey();
                            Attendence attendence = childOfChild.getValue(Attendence.class);

                            if(attendence != null){
                                Log.i("at",attendence.getDate()+"-"+date);
                                Toast.makeText(getContext(), "-"+date, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                }

                pDialog.hide();
                pDialog.cancel();

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.w("EROOR", "loadPost:onCancelled", databaseError.toException());
                pDialog.hide();
                pDialog.cancel();
            }

        };
        App.classRef.child(mParam1)
                .child(App.registered_student)
                .addValueEventListener(postListener);

    }



=====================================================================================================
Add + Edititng Data as an object
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



=====================================================================================================
Deleting Data as an object 
=====================================================================================================
App.myLocationRef.child(locationId)
            .child("images")
            .child(list.get(pos)
                    .getId()).removeValue()
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                    mySpinnerDialog.cancelSpinner();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Write failed
                    mySpinnerDialog.cancelSpinner();
                }
            });