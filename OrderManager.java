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
import models.Customer;
import models.Order;
import models.OrderItem;


public class OrderManager implements Management<Order> {
    private static final String FILE_PATH = "resources/orders.txt";
    private final List<Order> orders;

    public OrderManager(ProductManager pm, CustomerManager cm) {
        orders = loadOrders(pm, cm);
    }

    private static List<OrderItem> loadItems(String OID, ProductManager pm){
        List<OrderItem> items = new ArrayList<>();
        java.io.File file = new File("resources/orderitems.txt"); // định dạng OID;Item1;q1;item2;q2;item3;q3;... với item là obj product

        if(!file.exists()) return items;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line = br.readLine())!=null){
                String[] parts = line.split(";");
                if(parts[0].equals(OID)){
                    for (int i = 1; i < parts.length; i+= 2){
                        String pid = parts[i];
                        int qty = Integer.parseInt(parts[i+1]);
                        items.add(new OrderItem(pm.getProduct(pid), qty));
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return items;
    }
    private static List<Order> loadOrders(ProductManager pm, CustomerManager cm){ // định dạng file OID|CID|puschasedate
        List<Order> list = new ArrayList<>();
        java.io.File file = new File(FILE_PATH);
        if(!file.exists()) return list;
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine())!= null){
                String parts[] = line.split("\\|");
                String oid = parts[0];
                String cid = parts[1];
                LocalDate purchaseDate;
                try {
                    purchaseDate = LocalDate.parse(parts[2], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                } catch (Exception e) {
                    purchaseDate = null;
                }
                list.add(new Order(oid, loadItems(oid, pm), cm.getCustomer(cid), purchaseDate));
            }
        } catch(Exception e){
            System.out.println("Error: "+ e.getMessage());
        }
        return list;
    }

    // kiểm tra tồn tại OID
    public boolean exists(String ID) {
        for (Order u : orders) {
            if (u.getOID().equals(ID)) {
                return true;
            }
        }
        return false;
    }

    public Order getOrder(String ID){
        for (Order u : orders) {
            if (u.getOID().equals(ID))
                return u;
        }
        return null;
    }

    //lưu file cả list user
    public void saveorders() {
        try (FileWriter fw = new FileWriter(FILE_PATH, false)) {
            for (Order u : orders) {
                fw.append(u.toString()).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving orders: " + e.getMessage());
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
        for (Order elem : orders) {
            System.out.println(elem.getOID() + "," + elem.getTotal() + ","+ elem.getpurchaseDate());
        }
    }

    @Override
    public void add(Order user){
        orders.add(user);
        saveorders();
    }  

    @Override
    public void delete(String ID){
        orders.removeIf(u -> u.getOID().equals(ID));
        saveorders();
    } 

    @Override
    public void search(String ID){
        getOrder(ID);
    } 

    @Override
    public void update(Order newOrder, String ID){
        for(int i =0; i < orders.size(); i++){
            if(orders.get(i).getOID().equals(ID)){
                orders.set(i, newOrder);
            }
        }
    }

    @Override
    public List<Order> getAll(){
        return new ArrayList<>(orders);
    }

    @Override
    public String report(){
        return "Tong so don hang trong he thong: " + orders.size() + "\n";
    } 
}

