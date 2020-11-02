/*
 * CS1011 - 051
 * Fall 2019
 * Lab 9 - Lots of Parking
 * Name: Nigel Nelson
 * Created: 11/12/2019
 */


package msoe.nelsonni.lab09;


import java.text.DecimalFormat;

/**
 * Parking lot class used to manage individual parking lots
 * @author Nigel Nelson
 */
public class ParkingLot {

    public static final double CLOSED_THRESHOLD = 80.0;
    private final String name;
    private int capacity;
    private int spotsTaken;
    private int timeClosedBegin;
    private int timeClosedEnd;
    private boolean wasClosed;
    private int minutesClosed;
    private int lastTime;

    /**
     * Creates a parking lot based on the single input of capacity
     * @param capacity Maximum number of vehicles a parking lot can hold
     */
    ParkingLot (int capacity){
        this.capacity = capacity;
        this.name = "test";
        minutesClosed = 0;
        lastTime = 0;
    }

    /**
     * Creates a parking lot based on receiving a parking lot name and capacity
     * @param name The name associated with a given parking lot
     * @param capacity Maximum number of vehicles a parking lot can hold
     */
    ParkingLot (String name, int capacity){
        this.name = name;
        this.capacity = capacity;
        minutesClosed = 0;
        lastTime = 0;

    }

    /**
     *Method that prints out the percent full a given parking lot is,
     * unless it is full, in that case is simply states the lot is full
     */
    public String toString(){
        DecimalFormat formatter = new DecimalFormat("#.#");
        String statusStatement;
        if (!isClosed()) {
            statusStatement = "Status for " + getName() + " parking lot: " + spotsTaken + " vehicles (" + formatter.format(getPercentFull()) + "%)";
        }
        else {
            statusStatement = "Status for " + getName() + " parking lot: " + spotsTaken + " vehicles (CLOSED)";
        }
        return statusStatement;
    }

    /**
     * Method that returns the total minutes that a given lot has been closed for
     * @return the minutes a given lot has been closed for
     */
    public int getMinutesClosed(){
        return minutesClosed;
    }

    /**
     * Method that returns a lot's assigned name
     * @return a lot's given name
     */
    public String getName(){
        return name;
    }

    /**
     * Method that returns the number of spots remaining for a given lot
     * @return number of spots remaining for a given lot
     */
    public int getNumberOfSpotsRemaining(){
        return capacity - spotsTaken;

    }

    /**
     * Method that returns the percent full a given lot is
     */
    public double getPercentFull(){
        double percentFull = ((double)spotsTaken / capacity) * 100.0;
        return percentFull;
    }

    /**
     * Method that discloses whether or not a lot is open or closed
     * @return whether a lot is open or closed
     */
    public boolean isClosed(){
        if (getPercentFull()>= CLOSED_THRESHOLD){
            return  true;
        }
        else{
            return false;
        }

    }

    /**
     * Method that marks the time a vehicle enters a lot,
     * adds to the number of spots taken, and checks to see if the lot is closed
     * @param timestamp indicates a vehicles entry time
     */
    public void markVehicleEntry(int timestamp){
        if (lastTime <= timestamp) {
            spotsTaken = spotsTaken + 1;
            lastTime = timestamp;
            if (isClosed() && !wasClosed) {
                timeClosedBegin = timestamp;
                wasClosed = true;

            }
        }

    }

    /**
     * Method that marks the time when a vehicle leaves the lot,
     * subtracts from the number of spots take, and checks to see
     * if the lot is now open if it was closed
     * @param timestamp time at which a vehicle exits
     */
    public void markVehicleExit(int timestamp){
        if (lastTime <= timestamp) {
            spotsTaken = spotsTaken - 1;
            lastTime = timestamp;
            if (!isClosed() && wasClosed) {
                timeClosedEnd = timestamp;
                wasClosed = false;
                minutesClosed = minutesClosed + (timeClosedEnd - timeClosedBegin);

            }
        }
    }

}
