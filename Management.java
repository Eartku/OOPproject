package interfaces;

import java.util.List;

public interface Management<T> {
    //CRUD
    void add(T obj);   

    void delete(String key); 

    void search(String key); 

    void update(T obj, String key);

    void showList();

    List<T> getAll();

    String report();
}
