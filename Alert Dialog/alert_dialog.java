====================================================================================
Confirmation Alert dialog
====================================================================================
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


====================================================================================
Custom View Alert Dialog
====================================================================================
AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mView = li.inflate(R.layout.image_zoom_layout, null);
                        PhotoView photoView = mView.findViewById(R.id.photo_view);

                        final ProgressDialog pDialog = new ProgressDialog(context);
                        pDialog.setMessage("Loading...");
                        pDialog.show();

                        Glide.with(context)
                                .load(list.get(getAdapterPosition()).getUri())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed( GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        pDialog.hide();
                                        pDialog.dismiss();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        pDialog.hide();
                                        pDialog.dismiss();
                                        return false;
                                    }
                                })
                                .into(photoView);

//                        Glide.with(context).load(list.get(getAdapterPosition()).getImgUrl()).into(photoView);
                        mBuilder.setView(mView);
                        AlertDialog mDialog = mBuilder.create();
                        mDialog.show();


<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center">

    <com.github.chrisbanes.photoview.PhotoView
        android:id="@+id/photo_view"
        android:layout_width="400dp"
        android:layout_height="500dp"
        android:src="@drawable/ic_launcher_background"
        android:scaleType="fitXY"
        />

</LinearLayout>