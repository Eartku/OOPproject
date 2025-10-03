package service;
import interfaces.Management;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import models.Drug;
import models.NonDrug;
import models.Product;


public class ProductManager implements Management<Product>{
    private static final String FILE_PATH = "resources/products.txt";
    private final List<Product> products;

    public ProductManager() {
        products = loadProducts();
    }

    // lấy p từ file
    private static List<Product> loadProducts(){
        List<Product> list = new ArrayList<>();
        java.io.File file = new File(FILE_PATH);
        if(!file.exists()) return list;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine())!= null){
                String parts[] = line.split("\\|");
                int type = Integer.parseInt(parts[8]);
                switch(type){
                    case 0 -> list.add(new Drug(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), parts[5], parts[6], Boolean.parseBoolean(parts[7])));
                    case 1 -> list.add(new NonDrug(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), Integer.parseInt(parts[4]), parts[5], parts[6], parts[7]));                
                }
            }
        }
        catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return list;
    }

    // kiểm tra tồn tại pname
    public boolean exists(String ID) {
        for (Product u : products) {
            if (u.getPID().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    public Product getProduct(String ID){
        for (Product u : products) {
            if (u.getPID().equals(ID))
                return u;
        }
        return null;
    }

    //lưu file cả list p
    public void saveProducts() {
        try (FileWriter fw = new FileWriter(FILE_PATH, false)) {
            for (Product u : products) {
                fw.append(u.toString()).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }

    @Override
    public void showList(){
        for (Product p : products) {
            System.out.println(p.getPID() + "-" + p.getName() + ": " + p.getPrice() +" VND");
        }
    }

    @Override
    public void add(Product p){
        products.add(p);
        saveProducts();
    }  

    @Override
    public void delete(String pname){
        products.removeIf(u -> u.getPID().equals(pname));
        saveProducts();
    } 

    @Override
    public void search(String ID){
        getProduct(ID);
    } 

    @Override
    public void update(Product newp, String pname){
        for(int i =0; i < products.size(); i++){
            if(products.get(i).getPID().equals(pname)){
                products.set(i, newp);
            }
        }
    }

    @Override
    public List<Product> getAll(){
        return new ArrayList<>(products);
    }

    public int countDrug(){
        int count = 0;
        for (Product p : products) {
            if (p instanceof Drug) {
                count++;
            }
        }
        return count;
    }

       public int countNonDrug(){
        int count = 0;
        for (Product p : products) {
            if (p instanceof NonDrug) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String report(){
        return "Tong so luong san pham trong he thong: " + products.size() + "\n"
        +  "So luong thuoc: " +  countDrug() + "\n"
        +  "So luong phi thuoc " +  countNonDrug() + "\n";
    } 
}

