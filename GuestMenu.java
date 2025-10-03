package view;

import interfaces.Authenticable;
import java.util.Scanner;
import models.Customer;
import models.Guest;
import service.UserManager;

public class GuestMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu(Authenticable user, UserManager um){
        MainMenu.clearScreen();
        Guest guest = (Guest) user;
        while (true) {
            System.out.println("==== MENU GUEST ====");
            System.out.println("1. Cap nhat thong tin ca nhan");
            System.out.println("0. Dang xuat");
            System.out.print("Nhap lua chon: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 0 -> {
                    System.out.println("Dang xuat thanh cong!\n");
                    return;
                }
                case 1 -> {
                    Customer c = AuthMenu.updateProfile(guest, sc, um);
                    AuthMenu.getMenu(user, um);
                    CustomerMenu.showMenu(c);
                    return;
                }
                default -> System.out.println("Khong hop le!");
            }
        }
    }
}
