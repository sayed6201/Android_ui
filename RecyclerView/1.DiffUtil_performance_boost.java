
/*
===================================================================================
DiffUtil is Used to Increase the performance of RecyclerView
===================================================================================
*/


//================Data Provider Provides Data===================

public class DataProvider {

    public static List<Person> getOldPersonList(){
        List<Person> persons = new ArrayList<>();
        persons.add(new Person(1, 20, "John"));
        persons.add(new Person(2, 12, "Jack"));
        persons.add(new Person(3, 8, "Michael"));
        persons.add(new Person(4, 19, "Rafael"));
        return persons;
    }

    public static List<Person> sortByAge(List<Person> oldList){
        Collections.sort(oldList, new Comparator<Person>() {
            @Override
            public int compare(Person person, Person person2) {
                return person.age - person2.age;
            }
        });
        return oldList;
    }
}


//================DiffUtil Class===================

public class MyDiffCallback extends DiffUtil.Callback{

    List<Person> oldPersons;
    List<Person> newPersons;

    public MyDiffCallback(List<Person> newPersons, List<Person> oldPersons) {
        this.newPersons = newPersons;
        this.oldPersons = oldPersons;
    }

    @Override
    public int getOldListSize() {
        return oldPersons.size();
    }

    @Override
    public int getNewListSize() {
        return newPersons.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPersons.get(oldItemPosition).id == newPersons.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPersons.get(oldItemPosition).equals(newPersons.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}


//================Call the methodinside RecyclerView===================
public void updateList(ArrayList<Person> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallback(this.persons, newList));
        diffResult.dispatchUpdatesTo(this);
}