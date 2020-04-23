====================================================================================
Recyclerview onClickListner with interface
====================================================================================
class NoteHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        private TextView textViewPriority;

        public NoteHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewPriority = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    //when the interface is implemented listner will not be empty
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        //position is passed to the intrface method
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    //when this method is called listner is initialized...........
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    

MainActivity.java
-------------------------------------------------------------------------------------------------
    adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
                intent.putExtra(AddEditActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddEditActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }