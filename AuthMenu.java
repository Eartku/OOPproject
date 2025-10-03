package view;

import data.Data;
import interfaces.Authenticable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import models.Admin;
import models.Customer;
import models.Guest;
import service.UserManager;

public class AuthMenu {

    public static Authenticable Register(Scanner sc, UserManager um){
        MainMenu.clearScreen();
        System.out.println("______DANG KY_____");
        String username;
        while(true){
            System.out.println("Nhap Username: ");
            username = sc.nextLine();
            if(um.exists(username)){
                System.out.println("Username da ton tai!");
                System.out.println("Dang nhap? (y/n): ");
                char choice = sc.nextLine().charAt(0);
                if(choice == 'Y' || choice == 'y'){
                    return Login(sc, um);
                }
            }
            else{
                System.out.println("Username hop le!");
                break;
            }
        }
        String password;
        while(true){
            System.out.println("Nhap Password: ");
            password = sc.nextLine();
            if(password.length() < 6){
                System.out.println("Password it nhat 6 ky tu!");
            }
            else{
                System.out.println("Password hop le!");
                break;
            }
        }
        Authenticable newUser = new Guest(username, password);
        um.add(newUser);
        um.saveUsers();
        System.out.println("Dang ky thanh cong!");
        return newUser;
    }

    public static Authenticable Login(Scanner sc, UserManager um){
        MainMenu.clearScreen();
        String username, password; 
        try {
            System.out.println("______DANG NHAP_____");
            while(true){
                System.out.println("Nhap Username: ");
                username = sc.nextLine();
                if(um.exists(username)){
                    Authenticable user = um.getUser(username);
                    while (true){
                        System.out.println("Nhap mat khau: ");
                        password = sc.nextLine();
                        if(user.checkPassword(password)){
                            System.out.println("Password hop le!");
                            return user;
                        }
                        else{
                            System.out.println("Sai mat khau!");
                        }
                    }
                }
                else{
                    System.out.println("Khong tim thay username!");
                    System.out.println("Dang ky tai khoan? (y/n): ");
                    char choice = sc.nextLine().charAt(0);
                    if(choice == 'Y' || choice == 'y'){
                        Register(sc, um);
                        break;
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            System.out.println("Error login: "+ e.getMessage());
            return null;
        }
        return null;
    }

    
    public static void getMenu(Authenticable user, UserManager um) {
        if (user instanceof Admin) {
            AdminMenu.showMenu(user,um);
        } else if (user instanceof Customer) {
            CustomerMenu.showMenu(user);
        } else if (user instanceof Guest) {
            GuestMenu.showMenu(user, um);
        }
        throw new IllegalArgumentException("Unknown user type!");
    }

    public static Customer updateProfile(Guest g, Scanner sc, UserManager um){
        MainMenu.clearScreen();
        System.out.println("=== CAP NHAT THONG TIN CA NHAN ===");
        String CID = Data.generateNewID("resources/customers.txt", 'C');

        System.out.print("Ho va ten: ");
        String fullname = sc.nextLine();

        LocalDate dobDate = null;
        while (true) {
            System.out.print("Ngay sinh (dd/MM/yyyy): ");
            String dob = sc.nextLine();
            try {
                dobDate = LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                break; 
            } catch (Exception e) {
                System.out.println("Ngay sinh khong hop le! Nhap lai theo dd/MM/yyyy");
            }
        }

        System.out.print("Dia chi: ");
        String address = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("So dien thoai: ");
        String phone = sc.nextLine();

        Customer customer = new Customer(
            g.getUsername(),
            g.getPassword(),
            CID,
            fullname,
            dobDate,
            address, 
            email, 
            phone
        );
        Data.changeRole(g.getUsername(), "resources/users.txt", 1);
        um.appendCustomer("resources/customers.txt", customer);


        return customer;
    }
}
