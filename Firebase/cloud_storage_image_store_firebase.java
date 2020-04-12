

=====================================================================================================
Deletining an image firebase database
=====================================================================================================

new AlertDialog.Builder(context)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setMessage("Are you sure you want to delete?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        // get position
                                        final ProgressDialog pDialog = new ProgressDialog(context);
                                        pDialog.setMessage("Deleting...");
                                        pDialog.show();

                                        final int pos = getAdapterPosition();
                                        final String nameOfImg = list.get(pos).getImageName();
                                        // Create a reference to the file to delete
                                        StorageReference desertRef = App.storageRef.child(nameOfImg);
                                        // Delete the file
                                        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // File deleted successfully

                                                App.myPendingRef.child(locationId)
                                                        .child("images")
                                                        .child(list.get(pos)
                                                                .getId()).removeValue()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {

                                                                Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                                                pDialog.hide();
                                                                pDialog.dismiss();

                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                // Write failed
                                                                pDialog.hide();
                                                                pDialog.dismiss();
                                                            }
                                                        });

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Uh-oh, an error occurred!
                                                pDialog.hide();
                                                pDialog.dismiss();
                                                Toast.makeText(context, "something went wrong, try later", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();