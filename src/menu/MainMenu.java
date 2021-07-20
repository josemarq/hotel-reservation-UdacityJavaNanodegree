package menu;

import api.HotelResources;
import model.Customer;
import model.IRoom;
import model.Reservation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    static String input ="";
    static Scanner scanner = new Scanner(System.in);
    private static final String DATE_FORMAT = "MM/dd/yyyy";


    public static void showMainMenu()
    {

        System.out.print("" +
                "Hotel Reservation System Main Menu\n" +
                "------------------------------------\n" +
                "1.- \uD83D\uDD0D Find and reserve a room\n" +
                "2.- \uD83D\uDD0D See my reservations\n" +
                "3.- \uD83D\uDCC4 Create an Account\n" +
                "4.- \uD83D\uDD10 Admin Options\n" +
                "5.- Exit\n" +
                "-------------------------------------\n" +
                "Please select a option (1-5):\n");

        do {
            input = scanner.nextLine();

            if (input.length() == 1) {
                switch (input.charAt(0)) {
                    case '1':
                        findAndReserveRoom(scanner);
                        break;
                    case '2':
                        getMyReservation(scanner);
                        break;
                    case '3':
                        createAccount(scanner, false);
                        break;
                    case '4':
                        AdminMenu.showMainMenu();;
                        break;
                    case '5':
                        System.out.println("Thanks for using Hotel Reservation\nBye");
                        System.out.println("\uD83D\uDE00");
                        break;
                    default:
                        System.out.println("\u26A0\uFE0F Wrong selection, Please try again and select a option between 1-5:");
                        break;
                }
            } else {
                    System.out.println("Wrong selection, Please try again and select a option between 1-5:");
                }
        } while (input.length() >= 0);
    } // End show main menu

    //Option Menu 1
    private static void findAndReserveRoom(Scanner scanner) {
        Date checkIn = validateCheckIn(scanner);
        Date checkOut = validateCheckOut(scanner, checkIn);

        Collection<IRoom> availableRooms = HotelResources.findRooms(checkIn, checkOut);
        boolean wantsToBook = false;
        if (availableRooms.isEmpty()) {
            Date newCheckInDate = getRecommendedDate(checkIn);
            Date newCheckOutDate = getRecommendedDate(checkOut);
            availableRooms = HotelResources.findRooms(newCheckInDate, newCheckOutDate);
            if (!availableRooms.isEmpty()) {
                System.out.println("\u2139 Sorry, we don't have any available room for that dates,\nbut how we can suggest these other dates, \ncheck-in on " + newCheckInDate + " and check-out on " + newCheckOutDate);
                wantsToBook = showAvailableRoomsAndAskToBook(scanner, availableRooms);
                checkIn = newCheckInDate;
                checkOut = newCheckOutDate;
            } else {
                System.out.println("\u274C Sorry, we can't find available rooms for those dates");
                showMainMenu();
            }
        } else {
            System.out.println("Available rooms for check-in on " + checkIn + " and check-out on " + checkOut);
            wantsToBook = showAvailableRoomsAndAskToBook(scanner, availableRooms);
        }
        if (!wantsToBook) {
            return;
        }
        Customer customer = getCustomerForReservation(scanner);
        if (customer == null) {
            System.out.println("\u274C Sorry, no account exists for that email, you must registered before reserve a room.");
            showMainMenu();
            return;
        }
        IRoom room = reserveARoom(scanner, availableRooms);
        Reservation reservation = HotelResources.bookRoom(customer.getEmail(), room, checkIn, checkOut);
        if (reservation == null) {
            System.out.println("\u274C Sorry, that room is not available. The booking process is canceled.");
        } else {
            System.out.println("Thank you!\n\u2705Your reservation is registered successfully!");
            System.out.println(reservation);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            showMainMenu();
        }
    }


    //Option 2
    private static void getMyReservation(Scanner scanner) {
        System.out.println("Please enter your Email (format: name@example.com): ");
        String email = scanner.nextLine();
        Customer customer = HotelResources.getCustomer(email);
        if (customer == null) {
            System.out.println("\u274C Sorry, we can't find and account with this email address.\nRemember you need to be registered in our system for make a reservation.");
            showMainMenu();
            return;
        }
        Collection<Reservation> reservations = HotelResources.getCustomerReservations(customer.getEmail());
        if (reservations.isEmpty()) {
            System.out.println("Oh! You don't have any reservations. Go to our main menu for book a room.");
            return;
        }
        for (Reservation reservation : reservations) {
            System.out.println(reservation.toString());
        }
    }

    //Option 3
    private static String createAccount(Scanner scanner, Boolean areReserving) {
        System.out.println("Please enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.println("Ok, now please enter your last name: ");
        String lastName = scanner.nextLine();
        String email = null;
        boolean validEmail = false;
        while (!validEmail) {
            try {
                System.out.println("Great, for finish please enter your email (you@example.com): ");
                email = scanner.nextLine();
                HotelResources.createCustomer(email, firstName, lastName);
                validEmail = true;
                System.out.println("\u2705 Ok, account successfully created!\n");
                if (areReserving==false) {
                    showMainMenu();
                }
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getLocalizedMessage());
            }
        }
        return email;
    }

    private static Customer getCustomerForReservation(Scanner scanner) {
        String email = null;
        boolean hasAccount = false;
        System.out.println("Do you already have an account with us? If yes please press y/Y, any other key for no?");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y")) {
            hasAccount = true;
        }
        if (hasAccount) {
            System.out.println("Please enter your Email (you@example.com): ");
            email = scanner.nextLine();
        } else {
            email = createAccount(scanner, true);

        }
        return HotelResources.getCustomer(email);
    }

    private static Date getRecommendedDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 2);
        return c.getTime();
    }

    private static boolean showAvailableRoomsAndAskToBook(Scanner scanner, Collection<IRoom> availableRooms) {
        for (IRoom room : availableRooms) {
            System.out.println(room.toString());
        }
        System.out.println();
        System.out.println("Would you like to book a room? If yes please enter y/Y, any other key for no?");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("y") || choice.equalsIgnoreCase("yes")) {
            return true;
        }
        return false;
    }

    private static Date validateCheckIn(Scanner scanner) {
        SimpleDateFormat DateFor = new SimpleDateFormat(DATE_FORMAT);
        Date checkIn = null;
        boolean isValidCheckIn = false;
        while (!isValidCheckIn) {
            System.out.println("Enter desired Check-in date (Use format mm/dd/yyyy): ");
            String checkInInput = scanner.nextLine();
            try {
                checkIn = DateFor.parse(checkInInput);
                //Read current date
                Date today = new Date();
                if (checkIn.before(today)) {
                    System.out.println("\u274C Sorry, Check-in date cannot be in the past. Please try again:");
                } else {
                    isValidCheckIn = true;
                }
            } catch (ParseException ex) {
                System.out.println("\u274C You introduce a invalid Check-in date format, please use mm/dd/yyyy");
            }
        }
        return checkIn;
    }

    private static Date validateCheckOut(Scanner scanner, Date checkIn) {
        SimpleDateFormat DateFor = new SimpleDateFormat(DATE_FORMAT);
        Date checkOut = null;
        boolean isValidCheckOut = false;
        while (!isValidCheckOut) {
            System.out.println("Check-out date (mm/dd/yyyy): ");
            String checkoutInput = scanner.nextLine();
            try {
                checkOut = DateFor.parse(checkoutInput);
                if (checkOut.before(checkIn)) {
                    System.out.println("\u274C Sorry but Check-out date can't be before the check-in date. Please try again: ");
                } else {
                    isValidCheckOut = true;
                }
            } catch (ParseException ex) {
                System.out.println("\u274C You introduce a invalid Check-out date format, please use mm/dd/yyyy");
            }
        }
        return checkOut;
    }

    private static IRoom reserveARoom(Scanner scanner, Collection<IRoom> availableRooms) {
        IRoom room = null;
        String roomNumber;
        boolean validRoomNumber = false;
        while (!validRoomNumber) {
            System.out.println("What room would you like to reserve? Please enter the room number: ");
            roomNumber = scanner.nextLine();
            room = HotelResources.getRoom(roomNumber);
            if (room == null) {
                System.out.println("\u274C That room doesn't exists, please enter a valid room number");
            } else {
                if (!availableRooms.contains(room)) {
                    System.out.println("\u274C That room is not available, please enter a valid room number");
                } else {
                    validRoomNumber = true;
                }
            }
        }
        return room;
    }
}