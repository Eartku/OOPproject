package service;
import interfaces.Management;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.Customer;



public class CustomerManager implements Management<Customer>{
    private static final String FILE_PATH = "resources/customers.txt";
    private final List<Customer> customers;

    public CustomerManager() {
        customers = loadCustomers();
    }
    
    private static List<Customer> loadCustomers(){
        List<Customer> list = new ArrayList<>();
        java.io.File file = new File(FILE_PATH);
        if(!file.exists()) return list;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine())!= null){
            String parts[] = line.split("\\|");
            String CID = parts[0];
            String fullName = parts[1];
            LocalDate dob;
            try {
                dob = LocalDate.parse(parts[2], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                dob = null;
            }

            String address = parts[3];
            String email = parts[4];
            String phone = parts[5];
            String username = parts[6];
            list.add(new Customer(username, CID, fullName, dob, address, email, phone));
            }
        }
        catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return list;
    }

    public boolean exists(String ID) {
        for (Customer u : customers) {
            if (u.getCID().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    public Customer getCustomer(String ID){
        for (Customer u : customers) {
            if (u.getCID().equals(ID))
                return u;
        }
        return null;
    }

    public void saveCustomers() {
        try (FileWriter fw = new FileWriter(FILE_PATH, false)) {
            for (Customer u : customers) {
                fw.append(u.toStringProfile()).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    @Override
    public void add(Customer user){
        customers.add(user);
        saveCustomers();
    }  

    @Override
    public void delete(String ID){
        customers.removeIf(u -> u.getCID().equals(ID));
        saveCustomers();
    } 

    @Override
    public void search(String ID){
        getCustomer(ID);
    } 

    @Override
    public void update(Customer newCustomer, String ID){
        for(int i =0; i < customers.size(); i++){
            if(customers.get(i).getUsername().equals(ID)){
                customers.set(i, newCustomer);
            }
        }
    }

        @Override
    public void showList(){
        for (Customer p : customers) {
            System.out.println(p.getCID() + "-" + p.getFullname());
        }
    }

    @Override
    public List<Customer> getAll(){
        return new ArrayList<>(customers);
    }

    @Override
    public String report(){
        return "Tong so luong khach hang trong he thong: " + customers.size() + "\n";
    }
    
    public void saveAllCustomers() {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
        for (Customer c : customers) {
                String line = String.join(";",  
                    c.getCID(),
                    c.getFullname(),
                    c.getDob().toString(),
                    c.getAddress(),
                    c.getEmail(),
                    c.getPhone(),
                    c.getUsername()
                );
                bw.write(line);
                bw.newLine();
        }
    } catch (IOException e) {
        System.out.println("Loi khi luu customers: " + e.getMessage());
    }
}

}

