package view;

import interfaces.Authenticable;
import java.util.Scanner;
import models.Customer;

public class CustomerMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu(Authenticable user){
        MainMenu.clearScreen();
        Customer customer = (Customer) user;
        while (true) {
                System.out.println("==== MENU CUSTOMER ====");
                System.out.println("1. Xem san pham");
                System.out.println("2. Dang ky mua san pham");
                System.out.println("3. Xem thong tin ca nhan");
                System.out.println("0. Dang xuat");
                System.out.print("Nhap lua chon: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 0 -> {
                        System.out.println("Dang xuat thanh cong!\n");
                        return;
                    }
                    case 1 -> {
                        System.out.println("Xem danh sach san pham...");
                    }
                    case 2 -> {
                        System.out.println("Dang ky mua san pham...");
                    }
                    case 3 -> {
                        System.out.println("Xem thong tin ca nhan...");
                        System.out.println(customer.toStringProfile());
                    }
                    default -> System.out.println("Khong hop le!");
                }
            }
    }
}
