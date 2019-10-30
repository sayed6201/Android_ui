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