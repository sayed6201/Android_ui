

public Single<List<Post>> getPosts() {

 return Single.create(new SingleOnSubscribe<List<Post>>() {
 	@Override
 	public void subscribe(@NonNull final SingleEmitter<List<Post>> e) throws Exception {

//volly code ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 		JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, ENDPOINT_GET_POSTS, null,
 			new Response.Listener<JSONArray>() {
			 	@Override
			 	public void onResponse(JSONArray response) {
			 		if (response != null) {
			 			ArrayList<Post> result = new ArrayList<>();
			 
					try {
						 for (int i = 0; i < response.length(); i++) {
						 result.add(getPost(response.getJSONObject(i)));
						 }
					 } catch (JSONException ex) {
					 	e.onError(ex);
					 }
			 
					e.onSuccess(result);
			 	}
			 }
 			},
		 new Response.ErrorListener() {
			 @Override
			 public void onErrorResponse(VolleyError error) {
			 	e.onError(error);
			 }
		 }
 	);
 
	VolleyDispatcher.getInstance().addToQueue(jsonObjectRequest);
 	}
//volly code ends here ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

 });
 }


private void downloadPosts() {
	Disposable subscription = RXPostConnector.getInstance().getPosts()
		.observeOn(AndroidSchedulers.mainThread())
		.subscribeOn(Schedulers.io())
		.subscribeWith(getObserver());
 
// add the subscription to the list to avoid a possible leak of references
	disposable.add(subscription);
}


@Override
protected void onStop() {
	super.onStop();
	if (disposable != null && !disposable.isDisposed()) {
		disposable.dispose();
	}
}