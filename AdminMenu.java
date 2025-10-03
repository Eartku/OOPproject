package view;

import interfaces.Authenticable;
import java.util.Scanner;
import models.Admin;
import service.UserManager;

public class AdminMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu(Authenticable user, UserManager um){
        MainMenu.clearScreen();
        Admin admin = (Admin) user;
        while (true) {
            System.out.println();
            System.out.println("==== MENU ADMIN ====");
            System.out.println("1. Quan ly nguoi dung");
            System.out.println("2. Quan ly san pham kha dung");
            System.out.println("3. Quan ly khach hang");
            System.out.println("4. Quan ly kho/lo hang");
            System.out.println("5. Xem thong tin Admin");
            System.out.println("0. Dang xuat");
            System.out.print("Nhap lua chon: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 0 -> {
                    System.out.println("Dang xuat thanh cong!\n");
                    return;
                }
                case 1 -> {
                    System.out.println("Quan ly nguoi dung...");
                    ManageUserMenu.showMenu(um);
                    break;
                }
                case 2 -> {
                    System.out.println("Quan ly san pham kha dung...");
                }
                case 3 -> {
                    System.out.println("Quan ly khach hang...");
                }
                case 4 -> {
                    System.out.println("Quan ly kho/ lo hang...");
                }
                case 5 -> {
                    System.out.println("Xem thong tin Admin...");
                    System.out.println(admin.toString());
                }
                default -> System.out.println("Khong hop le!");
            }
        }
    }
}
