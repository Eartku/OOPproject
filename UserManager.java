package service;
import interfaces.Authenticable;
import interfaces.Management;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.Admin;
import models.Customer;
import models.Guest;


public class UserManager implements Management<Authenticable>{
    private static final String FILE_PATH = "resources/users.txt";
    private final List<Authenticable> users;

    public UserManager() {
        users = loadUsers();
    }

    // lấy user từ file
    private static List<Authenticable> loadUsers(){
        List<Authenticable> list = new ArrayList<>();
        java.io.File file = new File(FILE_PATH);
        if(!file.exists()) return list;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine())!= null){
                String parts[] = line.split("\\|");
                int role = Integer.parseInt(parts[2]);
                switch(role){
                    case 0 -> list.add(new Guest(parts[0], parts[1]));
                    case 1 -> list.add(new Customer(parts[0], parts[1]));
                    case 2 -> list.add(new Admin(parts[0], parts[1]));                
                }
            }
        }
        catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return list;
    }

    // kiểm tra tồn tại Username
    public boolean exists(String username) {
        for (Authenticable u : users) {
            if (u.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public Authenticable getUser(String username){
        for (Authenticable u : users) {
            if (u.getUsername().equals(username))
                return u;
        }
        return null;
    }

    //lưu file cả list user
    public void saveUsers() {
        try (FileWriter fw = new FileWriter(FILE_PATH, false)) {
            for (Authenticable u : users) {
                fw.append(u.toString()).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }


    public void appendCustomer(String path, Customer u) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.append(u.toStringProfile()).append("\n");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void add(Authenticable user){
        users.add(user);
        saveUsers();
    }  

    @Override
    public void delete(String username){
        users.removeIf(u -> u.getUsername().equals(username));
        saveUsers();
    } 

    @Override
    public void search(String username){
        getUser(username);
    } 

    @Override
    public void update(Authenticable newUser, String username){
        for(int i =0; i < users.size(); i++){
            if(users.get(i).getUsername().equals(username)){
                users.set(i, newUser);
            }
        }
    }

    @Override
    public List<Authenticable> getAll(){
        return new ArrayList<>(users);
    }

    @Override
    public void showList(){
        for (Authenticable u : users) {
            System.out.println(u.getUsername() + "|" + u.getRole());
        }
    }

    public int countGuest(){
        int count = 0;
        for (Authenticable user : users) {
            if (user instanceof Guest) {
                count++;
            }
        }
        return count;
    }

       public int countCustomer(){
        int count = 0;
        for (Authenticable user : users) {
            if (user instanceof Customer) {
                count++;
            }
        }
        return count;
    }

       public int countAdmin(){
        int count = 0;
        for (Authenticable user : users) {
            if (user instanceof Admin) {
                count++;
            }
        }
        return count;
    }

    public void saveAllUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Authenticable user : users) {
                bw.write(user.toString()+ "\n");
            }
        } catch (IOException e) {
            System.out.println("Loi khi luu customers: " + e.getMessage());
        }
    }

    @Override
    public String report(){
        return "Tong so luong tai khoan trong he thong: " + users.size() + "\n"
        +  "So tai khoan Guest: " +  countGuest() + "\n"
        +  "So tai khoan khach hang (profile updated): " +  countCustomer() + "\n"
        +  "So tai khoan Admin: " + countAdmin();
    } 
}

