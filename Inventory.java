package service;
import interfaces.Management;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import models.Batch;
import models.Customer;
import models.Product;


// Inventory hay Batch_Management
public class Inventory implements Management<Batch>{
    private static final String FILE_PATH = "resources/inventory.txt";
    private final List<Batch> inv;

    public Inventory(ProductManager pm) {
        inv = loadInventory(pm);
    }

    // lấy user từ file
    private static List<Batch> loadInventory(ProductManager pm){
        List<Batch> list = new ArrayList<>();
        java.io.File file = new File(FILE_PATH);
        if(!file.exists()) return list;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine())!= null){
                String parts[] = line.split("\\|");
                String BID = parts[0];
                Product p = pm.getProduct(parts[1]);
                int quantity = Integer.parseInt(parts[2]);
                LocalDate importDate;
                try {
                    importDate = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e) {
                    importDate = null;
                }
                list.add(new Batch(BID, p, quantity, importDate));
            }
        }
        catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return list;
    }

    // kiểm tra tồn tại Username
    public boolean exists(String ID) {
        for (Batch u : inv) {
            if (u.getBatchId().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    public Batch getUser(String username){
        for (Batch u : inv) {
            if (u.getBatchId().equals(username))
                return u;
        }
        return null;
    }

    //lưu file cả list user
    public void saveinv() {
        try (FileWriter fw = new FileWriter(FILE_PATH, false)) {
            for (Batch u : inv) {
                fw.append(u.toString()).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving inv: " + e.getMessage());
        }
    }


    public static void appendCustomer(String path, Customer u) {
        try (FileWriter fw = new FileWriter(path, true)) {
            fw.append(u.toStringProfile()).append("\n");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Override
    public void showList(){
        for(Batch b : inv){
            System.out.println(b.getBatchId() + "[" + b.getProduct().getName() + "," + b.getQuantity() + "]" + b.getImportDate());
        }
    }

    @Override
    public void add(Batch user){
        inv.add(user);
        saveinv();
    }  

    @Override
    public void delete(String username){
        inv.removeIf(u -> u.getBatchId().equals(username));
        saveinv();
    } 

    @Override
    public void search(String username){
        getUser(username);
    } 

    @Override
    public void update(Batch batch, String ID){
        for(int i = 0; i < inv.size(); i++){
            if(inv.get(i).getBatchId().equals(ID)){
                inv.set(i, batch);
            }
        }
    }

    @Override
    public List<Batch> getAll(){
        return new ArrayList<>(inv);
    }


    @Override
    public String report(){
        return "Tong so luong lo hang trong he thong: " + inv.size() + "\n";
    } 
}

