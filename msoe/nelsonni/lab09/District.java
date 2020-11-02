/*
 * CS1011 - 051
 * Fall 2019
 * Lab 9 - Lots of Parking
 * Name: Nigel Nelson
 * Created: 11/12/2019
 */
package msoe.nelsonni.lab09;

/**
 * Manages parking lots within a district.
 * @author [Nigel Nelson]
 */
public class District {
    private ParkingLot[] lots;
    public static final int MAX_LOTS = 20;
    private int numLots;
    private int districtSpotsRemaining;
    private int districtParkingClosedBeginTime;
    private int districtParkingClosedEndTime;
    private boolean wasDistrictClosed;
    private int minutesClosed;


    /**
     * Sets up a district.
     */
    public District(){
        lots = new ParkingLot[MAX_LOTS];
        wasDistrictClosed = false;
        numLots = 0;


    }

    /**
     *
     * Method that adds a lot to the lots array
     * @param name the name of the lot being added
     * @param capacity the capacity of the lot being added
     * @return
     */
    public int addLot(String name, int capacity){
        int newIndex = numLots;
        if(newIndex < MAX_LOTS) {
            lots[newIndex] = new ParkingLot(name, capacity);
            numLots++;
        }
        return newIndex<MAX_LOTS ? newIndex : -1;
    }


    /**
     * Method that returns the info for a desire lot.
     * @param index, the place of a lot in the array
     * @return
     */
    public ParkingLot getLot(int index){
        return lots[index];
    }

    /**
     * Display status information for a district, which includes all of the lots in the lots array
     * @see ParkingLot#toString() () for the format for each.
     */
    public String toString() {
        String districtStatusOpener = "District status:\n";
        String districtToString = "  " + lots[0].toString() + "\n";
        for (int i = 1; i < numLots; i++) {
            districtToString = districtToString + "  " + lots[i].toString() + "\n";
        }
        return districtStatusOpener + districtToString;
    }

    /**
     * Returns the number of remaining parking spots in the district
     * @return the number of remaining parking spots in the district
     */
    public int getNumberOfSpotsRemaining() {
        districtSpotsRemaining = 0;

        for(int i = 0; i < numLots; i++){
            districtSpotsRemaining = districtSpotsRemaining +(lots[i]).getNumberOfSpotsRemaining();
        }
        return districtSpotsRemaining;
    }

    /**
     * Returns the amount of time all the lots in the lots array have been simultaneously closed.
     * @return number of minutes all lots have been closed
     */
    public int getMinutesClosed() {

        return minutesClosed;
    }

    /**
     * Checks the status of all lots in the district and returns true if they are all closed and false otherwise.
     * @return whether all three lots in the district are all closed
     */
    public boolean isClosed() {
        boolean isOneLotNotLotClosed = false;
        for (int i = 0; i<numLots; i++){
            if(!lots[i].isClosed()){
                isOneLotNotLotClosed = true;
                break;
            }
        }

        return !isOneLotNotLotClosed;
    }

    /**
     * Record a vehicle entering a lot at a specified time.
     * <p></p>
     * This calls ParkingLot.markVehicleEntry for the lot corresponding
     * to lotNumber in the array
     * Also checks to see if the district is now closed, which would
     * begin a timer for the amount of time the district is closed.
     * @param lotNumber Number of lot
     * @param timestamp Entry time in minutes since all lots were opened.
     */
    public void markVehicleEntry(int lotNumber, int timestamp) {

        lots[lotNumber].markVehicleEntry(timestamp);
        if (isClosed() && !wasDistrictClosed){
            districtParkingClosedBeginTime = timestamp;
            wasDistrictClosed = true;
        }

    }

    /**
     * Record a vehicle exiting a lot at a specified time.
     * <p></p>
     * This calls ParkingLot.markVehicleEntry for the lot corresponding
     * to lotNumber in array.
     * Also checks to see if the district is now opened, if it is then
     * it ends the timer for the time the district is closed.
     * @param lotNumber Number of lot
     * @param timestamp Entry time in minutes since all lots were opened.
     */
    public void markVehicleExit(int lotNumber, int timestamp) {


       lots[lotNumber].markVehicleExit(timestamp);

        if (!isClosed() && wasDistrictClosed){
            districtParkingClosedEndTime = timestamp;
            wasDistrictClosed = false;
            minutesClosed = minutesClosed + ( districtParkingClosedEndTime - districtParkingClosedBeginTime );
        }

    }
}