package flight.application;


public class Passenger implements java.io.Serializable{
    
     //attributes for the passengers
    private String name;
    public char passengerAge;
    public char classType;
    private char seatType;
    
    //constructors
    public Passenger() { }
    public Passenger(String name, char passengerAge, char classType, char seatType) {
        this.name = name;
        this.passengerAge = passengerAge;
        this.classType = Character.toUpperCase(classType);
        this.seatType = Character.toUpperCase(seatType);
    }

    //getters
    public String getName()
    { 
        return name; 
    }
    public char getPassengerAge()
    { 
        return passengerAge; 
    }
    public char getClassType() 
    { 
        return classType; 
    }
    public char getSeatType() 
    { 
        return seatType;
    }

    //setters
    
    public void setPassengerAge(char passengerAge)
    { 
        this.passengerAge = passengerAge; 
    }
    public void setClassType(char classType)
    { 
        this.classType = Character.toUpperCase(classType);
    }
    public void setSeatType(char seatType) 
    { 
        this.seatType = seatType;
    }

    //methods

    public String[] getDetails() {
        String[] details = new String[4];

        //name is retrieved
        details[0] = name;

        //here if the passenger is an adult then it will be converted to A
        //otherwise it will be converted to C for child
        if (passengerAge == 'A') 
        { 
            details[1] = "Adult";
        }
        else if (passengerAge == 'C') 
        { 
            details[1] = "Child";
        }

        //'class type' is retrieved
        if (classType == 'F')
        { 
            details[2] = "First Class"; 
        }
        else if (classType == 'B')
        { 
            details[2] = "Business Class";
        }
        else if (classType == 'E')
        { 
            details[2] = "Economy Class"; 
        }

        //'seat type' is retrieved
        if (seatType == 'W') 
        { 
            details[3] = "Window Seat"; 
        }
        else if (seatType == 'A')
        { 
            details[3] = "Aisle Seat"; 
        }
        else if (seatType == 'M') 
        { 
            details[3] = "Middle Seat";
        }

        return details;
    }
    
}
