package flight.application;


public interface IService {
    Passenger[][] getPlaneSeatings();
    void savePlaneSeats(Passenger[][] arrangement);
    String displayPlaneSeats(Passenger[][] arrangement);
    void storePlaneSeatingsDisplayer(char type, int position);
    char getPlaneSeatingFromDisplayer(int position);
    Passenger[] getAllPassenger(Passenger[][] arrangement);

    void allocateSeat();
    void cancelSeatAllocation();
    void searchPassenger();
    void displayAllPassenger(Passenger[] passenger);
}
