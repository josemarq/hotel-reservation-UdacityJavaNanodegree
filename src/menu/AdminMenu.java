package menu;

import api.AdminResources;
import model.*;
import java.util.Collection;
import java.util.Scanner;

public class AdminMenu {
    static String input ="";
    static Scanner scanner = new Scanner(System.in);

    public static void showMainMenu()
    {
        System.out.print("" +
                "Hotel Reservation ADMIN Main Menu\n" +
                "------------------------------------\n" +
                "1.- See all customers\n" +
                "2.- See all rooms\n" +
                "3.- See all reservations\n" +
                "4.- Add a room\n" +
                "5.- Back to main menu\n" +
                "-------------------------------------\n" +
                "Please select a option (1-5):\n");

        do {
            input = scanner.nextLine();

            if (input.length() == 1) {
                switch (input.charAt(0)) {
                    case '1':
                        listCustomers();
                        break;
                    case '2':
                        listRooms();
                        break;
                    case '3':
                        listReservations();
                        break;
                    case '4':
                        addRoom(scanner);
                        break;
                    case '5':
                        MainMenu.showMainMenu();
                        break;
                    default:
                        System.out.println("\u26A0\uFE0F Wrong selection, Please try again and select a option between 1-5:");
                        break;
                }
            } else {
                System.out.println("Wrong selection, Please try again and select a option between 1-5:");
            }
        } while (input.length() >= 0);
    } // End show Admin menu

    //Option 1
    private static void listCustomers() {
        Collection<Customer> customers = AdminResources.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("Oh no!. You don't have any customer registered.\n");
            showMainMenu();
        } else {
            AdminResources.getAllCustomers().forEach(System.out::println);
        }
    }

    //Option 2
    private static void listRooms() {
        Collection<IRoom> rooms = AdminResources.getAllRooms();

        if(rooms.isEmpty()) {
            System.out.println("Hey, this is a Hotel without rooms?. Why don't add a room now, please enter 4 in Admin Menu:");
            showMainMenu();
        } else {
            AdminResources.getAllRooms().forEach(System.out::println);
        }
    }

    //Option 3
    private static void listReservations() {
        Collection<Reservation> listReservations = AdminResources.getAllReservations();
        if (listReservations.isEmpty()) {
            System.out.println("Can't find any reservation");
        } else {
            for (Reservation reservation : listReservations) {
                System.out.println(reservation.toString());
            }
        }
        System.out.println();
    }

    //Option 4
    private static void addRoom(Scanner scanner) {
        System.out.println("Enter room number:");
        final String roomNumber = scanner.nextLine();

        System.out.println("Enter price per night:");
        final double roomPrice = enterRoomPrice(scanner);

        System.out.println("Enter room type:\n1 for single bed\n2 for double bed\n");
        final RoomType roomType = enterRoomType(scanner);

        final Room room = new Room(roomNumber, roomPrice, roomType);

        AdminResources.addRoom(room);
        System.out.println("\nGreat!. The Room has been added successfully!");

        System.out.println("Would like to add another room? y/n");
        addAnotherRoom();
    }

    private static double enterRoomPrice(final Scanner scanner) {

        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException exp) {
            System.out.println("You have enter and invalid room price!\nPlease try again and enter a DOUBLE number.\nExample: 109.99 (Remember use point for separate decimals):");
            return enterRoomPrice(scanner);
        }
    }

    private static RoomType enterRoomType(final Scanner scanner) {
        try {
            return RoomType.getNumberOfBeds(Integer.parseInt(scanner.nextLine()));
        } catch (IllegalArgumentException exp) {
            System.out.println("Invalid room type!\nPlease, choose 1 for single bed or 2 for double bed:");
            return enterRoomType(scanner);
        }
    }

    private static void addAnotherRoom() {
        final Scanner scanner = new Scanner(System.in);

        try {
            String anotherRoom;

            anotherRoom = scanner.nextLine();

            while (((anotherRoom.charAt(0) != 'Y') && (anotherRoom.charAt(0) != 'y'))
                    && ((anotherRoom.charAt(0) != 'N') && (anotherRoom.charAt(0) != 'n'))
                    || anotherRoom.length() != 1) {
                System.out.println("Wrong selection. Please enter Y (Yes) or N (No)");
                anotherRoom = scanner.nextLine();
            }

            if ((anotherRoom.charAt(0) == 'Y') || (anotherRoom.charAt(0) == 'y')) {
                addRoom(scanner);
            } else if ((anotherRoom.charAt(0) == 'N') || (anotherRoom.charAt(0) == 'n'))  {
                showMainMenu();
            } else {
                addAnotherRoom();
            }
        } catch (StringIndexOutOfBoundsException ex) {
            addAnotherRoom();
        }
    }
}


