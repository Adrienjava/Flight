package flight.application;

import java.io.*;
import java.util.*;

public class Service implements IService {
    //declarations
    private final static String PATH = System.getProperty("user.dir") + "\\";
    private final static String PASSENGERS_PATH = PATH + "plane_seatings.dat";
    private final static String DISPLAYER_PATH = PATH + "seatings_displayer.dat";
    private static Scanner scanner = new Scanner(System.in);
    private final char[] LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F' };


    //methods

    /** A method that will get the plane seating allocations from a file. */
    public Passenger[][] getPlaneSeatings() {
        try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(PASSENGERS_PATH))) {
            Passenger[][] allocs = (Passenger[][]) input.readObject();
            input.close();
            return allocs;
        } catch (Exception e) {
            System.out.println("Exception in file reading: " + e.getMessage() + "\n\nCreating a new file!");
            savePlaneSeats(new Passenger[12][6]);
            return getPlaneSeatings();
        }
    }

    /** A method that will save the plane seats, based on the arrangement, into a text file. */
    public void savePlaneSeats(Passenger[][] arrangement) {
        try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(PASSENGERS_PATH))) {
            output.writeObject(arrangement);
            output.close();
        } catch (IOException ioe) {
            System.out.println("IOException in file writing: " + ioe.getMessage() + "\n\nFile writing will stop.");
        }
    }

    /** A method that will get a specified plane seating from the random binary file. */
    public char getPlaneSeatingFromDisplayer(int position) {
        char ch = ' ';
        try(RandomAccessFile file = new RandomAccessFile(DISPLAYER_PATH, "rw")) {
            file.seek(position);
            byte[] bytes = new byte[1];
            file.read(bytes);
            ch = new String(bytes).charAt(0);
            file.close();
            System.out.println(ch);
            scanner.nextLine();
        } catch (IOException ioe) {
            System.out.println("IOException in random file reading: " + ioe.getMessage() + "\n\nCreating a new random file!");
        }
        return ch;
    }

    /** A method that will have the plane @seatings displayer stored in a binary file, via random access. */
    public void storePlaneSeatingsDisplayer(char type, int position) {
        try(RandomAccessFile file = new RandomAccessFile(DISPLAYER_PATH, "rw")) {
            file.seek(position);
            file.write(type);
            file.close();
        } catch (IOException ioe) {
            System.out.println("IOException in random file writing: " + ioe.getMessage() + "\n\nRandom file writing will stop.");
        }
    }

    /** A method that will display the plane @seats, based on the arrangement. */
    public String displayPlaneSeats(Passenger[][] arrangement) {
        //separator
        String displayer = "\n\n   ";
        
        for (int i = 0; i < 11; i++) {
            if (i >= 5) {
                displayer += LETTERS[i - 5];
                if (i == 7) 
                { 
                    displayer += "  ";
                }
            } displayer += " ";
        }
        displayer += "\n";

        for (int x = arrangement.length - 12; x <= 11; x++) {
            displayer += "Row " + (x + 1);
            for (int i = 0; i < 4 - String.valueOf(x + 1).length(); i++) { displayer += " "; }
            for (int y = 0; y < 6; y++) {
                if (arrangement[x][y] != null) 
                { 
                    displayer += arrangement[x][y].getPassengerAge()+ " ";
                }
                else { displayer += "* "; }
                if (y == 2) { displayer += "  "; }
            }
            displayer += "\n";
        }
        return displayer;
    }

    /** A method that will get all of the customers in the seating allocations, in order by name. */
    public Passenger[] getAllPassenger(Passenger[][] arrangement) {
        List<Passenger> passengers = new ArrayList<>();
        for (int x = 0; x < arrangement.length; x++) {
            for (int y = 0; y < 6; y++) {
                if (arrangement[x][y] != null) {
                    passengers.add(arrangement[x][y]);
                }
            }
        }
        passengers.sort(new Comparator<Passenger>() {
            @Override public int compare(Passenger c1, Passenger c2) {
                return c1.getName().compareToIgnoreCase(c2.getName());
            }
        });
        return Arrays.copyOf(passengers.toArray(), passengers.size(), Passenger[].class);
    }

    /** A method that will allocate a passenger to a random seat based on its preference. */
    public void allocateSeat() {
        try {
            System.out.print("\n--------------------------");

            //name inputter
            System.out.print("\nName: ");
            String name = scanner.nextLine();
            
            //adult or child inputter
            System.out.print("Adult or child? (A/C): ");
            char passengerAge = scanner.nextLine().charAt(0);
            if (!String.valueOf(passengerAge).matches("[aAcC]"))
            {
                throw new Exception("Invalid input detected."); 
            }

            //first, business or economy class inputter
            System.out.print("First, business or economy class? (F/B/E): ");
            char classType = scanner.nextLine().charAt(0);
            
            if (!String.valueOf(classType).matches("[fFbBeE]"))
            { 
                throw new Exception("Invalid input detected.");
            }

            //seat preference inputter
            System.out.print("Seat preference? Aisle, middle or window? (A/M/W): ");
            char seatType = scanner.nextLine().charAt(0);
            if (!String.valueOf(seatType).matches("[aAmMwW]")) 
            { 
                throw new Exception("Invalid input detected."); 
            }

            assignPassengerToSeating(new Passenger(name, passengerAge, classType, seatType));
        

        } catch (Exception e) {
            System.out.print("An exception has occurred. The allocation will now cancel. Exception: " + e.getMessage() + "\nPress ENTER to continue...");
            scanner.nextLine();
        }
    }

    private void assignPassengerToSeating(Passenger passenger) {
        Passenger[][] arrangement = getPlaneSeatings();
        int selectedRow = -1;
        int selectedColumn = -1;

        //the rows and columns for the passenger are determined
        for (int row = arrangement.length - 1; row >= 0; row--) {
            //row 1 to row 2 are first class
            //row 3 to row 6 are business class
            // row 7 to row 12 are economic class
            if ((row <= 2 && passenger.getClassType() == 'F') || (row >= 3 && row <= 6 && passenger.getClassType() == 'B') || (row >= 7 && passenger.getClassType() == 'E')) {
                selectedRow = row;
                if (passenger.getSeatType() == 'W') {
                    if (arrangement[row][0] == null) { selectedColumn = 0; }
                    else if (arrangement[row][5] == null) { selectedColumn = 5; }
                }
                else if (passenger.getSeatType() == 'M') {
                    if (arrangement[row][1] == null) { selectedColumn = 1; }
                    else if (arrangement[row][4] == null) { selectedColumn = 4; }
                }
                else if (passenger.getSeatType() == 'A') {
                    if (arrangement[row][2] == null) { selectedColumn = 2; }
                    else if (arrangement[row][3] == null) { selectedColumn = 3; }
                }
            }
        }

        //the passenger is assigned to the plane seating based on the row and column given
        try {
            if (selectedRow == -1 || selectedColumn == -1) { throw new Exception("No seats available for your preference. We apologize for that."); }
            else {
                arrangement[selectedRow][selectedColumn] = passenger;
                savePlaneSeats(arrangement);
                storePlaneSeatingsDisplayer(passenger.getPassengerAge(), selectedRow*6 + selectedColumn + 1);
                System.out.println("Passenger successfully assigned.");
            }
        } catch (Exception e) {
            System.out.println("An exception has occurred: " + e.getMessage());
        } finally {
            System.out.println("Press ENTER to continue...");
            scanner.nextLine();
        }
    }

    /** A method that will cancel a passenger's seat allocation and remove him/her from the list. */
    public void cancelSeatAllocation() {
        Passenger[][] arrangement = getPlaneSeatings();
        try {
            //a row number is selected
            System.out.print("Select row number: ");
            int selectedRow = scanner.nextInt() - 1;
            if (selectedRow > arrangement.length) { throw new Exception("Row number doesn't exist."); }

            //a column letter is selected
            System.out.print("Select column letter: ");
            //convert char to uppercase
            char selection = new Scanner(System.in).nextLine().toUpperCase().charAt(0);
            //error handling to prevent crash
            if (!String.valueOf(selection).matches("[aAbBcCdDeEfF]"))
            { 
                throw new Exception("Column letter doesn't exist.");
            }
            
            int selectedColumn = new String(LETTERS).indexOf(selection);

            if (arrangement[selectedRow][selectedColumn] != null)
            {
                arrangement[selectedRow][selectedColumn] = null;
                savePlaneSeats(arrangement);
                storePlaneSeatingsDisplayer('*', selectedRow*6 + selectedColumn + 1);
                System.out.println("Passenger successfully removed.");
            } else {
                System.out.println("No passenger exists in that seating.");
            }
        } catch (Exception e) {
            System.out.println("An exception has occurred. The cancellation will now stop. Exception: " + e.getMessage() + "\nPress ENTER to continue...");
        } finally {
            new Scanner(System.in).nextLine();
        }
    }

    /** A method that will search for a passenger by name and display its details. */
    public void searchPassenger() {
        Passenger[][] arrangement = getPlaneSeatings();
        Passenger[] passengers = getAllPassenger(arrangement);
        String[] names = new String[passengers.length];
        for (int i = 0; i < passengers.length; i++) { 
            names[i] = passengers[i].getName(); 
        }

        //a passenger by name is searched for
        System.out.print("Passenger Name: ");
        String name = scanner.nextLine();
        int pos = Arrays.binarySearch(names, name); //assessment requirement

        //details are displayed if found
        if (pos >= 0) {
            Passenger passenger = passengers[pos];
            System.out.println("--------------");
            System.out.println("Passenger found. \nName: " + passenger.getDetails()[0] + ". [" + passenger.getDetails()[1] + "]\nClass: " + passenger.getDetails()[2] + ".\nSeat: " + passenger.getDetails()[3] + ".");

            //passenger's seating arrangement is determined
            for (int row = 0; row < arrangement.length; row++) {
                for (int col = 0; col < arrangement[row].length; col++) {
                    if (arrangement[row][col] == passenger) {
                        System.out.println("Position: " + (row + 1) + "-" + LETTERS[col]);
                        System.out.println("Press Enter to continue..");
                    }
                }
            }
        } else {
            System.out.println("No passenger found. Press Enter to continue");
        }
        scanner.nextLine();
    }

    /** A method that will display all of the passengers, in order by name. */
    public void displayAllPassenger(Passenger[] passenger) {
        System.out.println("Passengers: ");
        for (int i = 0; i < passenger.length; i++) {
            System.out.println("    Name: " + passenger[i].getName() + ".");
            System.out.println("    Passenger age: " + passenger[i].getDetails()[1] + ".");
            System.out.println("    Class Type: " + passenger[i].getDetails()[2] + ".");
            System.out.println("    Seat Type: " + passenger[i].getDetails()[3] + ".");
            System.out.println("------------");
        }
        scanner.nextLine();
    }
}
