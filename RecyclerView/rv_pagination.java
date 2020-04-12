private int pastVisibleItems;
    boolean isLoading = false;
    boolean isRefreshing = false;

userRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                StaggeredGridLayoutManager manager =
                        (StaggeredGridLayoutManager) recyclerView.getLayoutManager();

                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int[] firstVisibleItems = manager.findFirstVisibleItemPositions(null);
                if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                    pastVisibleItems = firstVisibleItems[0];
                }

                if ((visibleItemCount + pastVisibleItems) >= totalItemCount && !isLoading) {
                    isLoading = true;
                    if (Dataholder.getInstance().hasMoreData() && !isRefreshing) {
                        Toast.makeText(MainActivity.this, "Loading data from page "+Dataholder.getInstance().getCurrentPageNumber(), Toast.LENGTH_SHORT).show();
                        getData();
                    }
                } else {
                    isLoading = false;
                }
            }
        });