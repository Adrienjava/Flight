package flight.application;

import java.util.InputMismatchException;
import java.util.Scanner;

public class FlightApplication {
private static IService service = new Service();

    public static void main(String[] args) {
        
        Passenger[] passenger = service.getAllPassenger(service.getPlaneSeatings());
                System.out.println("******************************************");
                System.out.println("Welcome to our flight service application.");
                System.out.println("******************************************\n");
        while (true) {
            //try catch for error handling
            try {
                
                
                System.out.print("What would you like to do?" +
                        "\n1. Add a Passenger." +
                        "\n2. Remove a Passenger." +
                        "\n3. Search for a Passenger." +
                        "\n4. Show Seat chart. " +
                        "\n5. Display all Passengers." +
                        "\n6. Close the application." +
                        "\nInput number: ");
                int selection = new Scanner(System.in).nextInt();
                if (selection == 1) {
                    service.allocateSeat();
                    passenger = service.getAllPassenger(service.getPlaneSeatings());
                }
                else if (selection == 2)
                { 
                    service.cancelSeatAllocation(); 
                }
                else if (selection == 3)
                { 
                    service.searchPassenger(); 
                }
                else if(selection == 4) 
                { 
                    System.out.println(service.displayPlaneSeats(service.getPlaneSeatings()));
                }
                else if (selection == 5)
                { 
                    service.displayAllPassenger(passenger);
                }
                else if (selection == 6) 
                { 
                    return; 
                }
            } catch(InputMismatchException imme) 
            { 
                
            }
        }
    }
    
}
