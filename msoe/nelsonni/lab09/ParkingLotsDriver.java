/*
 * CS1011 - 051
 * Fall 2019
 * Lab 9 - Lots of Parking
 * Name: Nigel Nelson
 * Created: 10/31/2017
 */
package msoe.nelsonni.lab09;

/**
 * Driver class used to test District and ParkingLot classes.
 *
 * @author Robert W. Hasker and CS 1011 Faculty
 * @version 20181018_7
 */
public class ParkingLotsDriver {

    /**
     * Main test method: calls other tests.
     *
     * @param args Ignored command-line arguments
     */
    public static void main(String[] args) {
        // Testing ParkingLot class
        testSmallLot();
        testOverfillingLot();
        testParkingLot();
        testComingAndGoing();
        // Testing District class
        testTinyDistrict();
        testDistrict();
        testHeavyUsage();
        testMultipleDistrictSizes();
        System.out.println("All tests finished.");
    }

    /**
     * Display message to the console in red text
     * @param message error message to be displayed
     */
    private static void displayError(String message) {
        // Inspiration for coloring from Nathan Wagenknecht
        System.out.println("\u001B[31m>>> Error: " + message + "\u001B[0m");
    }

    /**
     * Display message to the console in green text
     * @param message message to be displayed
     */
    private static void displayMessage(String message) {
        // Inspiration for coloring from Nathan Wagenknecht
        System.out.println("\u001B[32m" + message + "\u001B[0m");
    }

    /**
     * Display message to the console in green text
     * @param message message to be displayed
     */
    private static void displayResult(String message) {
        System.out.println(message);
    }

    /**
     * Test creating a parking lot with a single space.
     */
    private static void testSmallLot() {
        displayMessage("Testing Small Lot...");
        ParkingLot driveway = new ParkingLot("Blacktop", 1);
        if(!driveway.getName().equals("Blacktop")) {
            displayError("ParkingLot.getName() not working, expected 'Blacktop' not '"
                    + driveway.getName() + "'.");
        }
        if(Math.abs(ParkingLot.CLOSED_THRESHOLD-80)>0.01) {
            displayError("Incorrect threshold for closed lot.");
        }
        if(driveway.isClosed()) {
            displayError("Empty driveway is closed.");
        }
        driveway.markVehicleEntry(5);
        if(!driveway.isClosed()) {
            displayError("Driveway with something in it is not closed.");
        }
        if (!driveway.toString().equals("Status for Blacktop parking lot: 1 vehicles (CLOSED)")) {
            displayError("Incorrect status: " + driveway);
        }
        driveway.markVehicleExit(400);
        if(driveway.getNumberOfSpotsRemaining()!=1) {
            displayError("Empty driveway should have no vehicles in it.");
        }
        if(driveway.getMinutesClosed()!=395) {
            displayError("Wrong number of minutes while sleeping overnight.");
        }
        displayResult(driveway.toString());
        displayMessage("Finished Testing Small Lot\n");
    }

    /**
     * Test some boundary conditions on lot closure including what happens if a vehicle
     * enters when the lot is marked as being closed.
     */
    private static void testOverfillingLot() {
        displayMessage("Testing Overfilling a Lot...");
        ParkingLot driveway = new ParkingLot("Blacktop", 9);
        for(int i=0; i<7; ++i) {
            driveway.markVehicleEntry(2);
        }
        driveway.markVehicleEntry(3);
        if(!driveway.isClosed()) {
            displayError("Expected 9-vehicle driveway to be closed at time 3.");
        }
        driveway.markVehicleExit(12);
        if(driveway.isClosed()) {
            displayError("Expected 9-vehicle driveway to be open at time 12.");
        }
        if(driveway.getMinutesClosed()!=9) {
            displayError("9-vehicle driveway test should have 9 minutes closed but you have "
                    + driveway.getMinutesClosed() + ".");
        }

        driveway = new ParkingLot(10);
        for(int i=0; i<7; ++i) {
            driveway.markVehicleEntry(2);
        }
        driveway.markVehicleEntry(3); // 8: now closed
        if(!driveway.isClosed()) {
            displayError("Expected 10-vehicle driveway to be closed at time 3.");
        }
        driveway.markVehicleExit(12); // 7: no longer closed
        if(driveway.isClosed()) {
            displayError("Expected 10-vehicle driveway to be open at time 12.");
        }
        driveway.markVehicleEntry(13); // 8: now closed
        driveway.markVehicleExit(15); // 7: no longer closed
        if(driveway.getMinutesClosed()!=11) {
            displayError("10-vehicle driveway test should have 11 closed minutes but you have "
                    + driveway.getMinutesClosed() + ".");
        }

        ParkingLot pad = new ParkingLot("White", 9);
        for(int i=0; i<7; ++i) {
            pad.markVehicleEntry(2);
        }
        pad.markVehicleEntry(3); // 8 closed
        if(!pad.isClosed()) {
            displayError("Expected pad to be closed at time 3.");
        }
        pad.markVehicleEntry(4); // 9
        if(pad.getNumberOfSpotsRemaining()!=0) {
            displayError("Expected 0 spots remaining on pad at time 4.");
        }
        pad.markVehicleExit(11); // 8
        pad.markVehicleExit(12); // 7: no longer full
        if(pad.isClosed()) {
            displayError("Expected pad to be open at time 12.");
        }
        if(pad.getMinutesClosed()!=9) {
            displayError("Test with over-full lot should have 9 minutes closed but you have "
                    + pad.getMinutesClosed() + ".");
        }
        displayMessage("Finished Testing Overfilling a Lot\n");
    }

    /**
     * Uses other static methods to test the ParkingLot class.
     * Creates a lot with four spaces that is used for most of
     * the tests.
     */
    private static void testParkingLot() {
        displayMessage("More Complete Test of Parking Lot...");
        ParkingLot lot = new ParkingLot(4);

        if(!lot.getName().equals("test")) {
            displayError("Lot name should be 'test' not '" + lot.getName() + "'.");
        }

        testFillingLot(lot);
        testRefillingLot(lot);
        testEmptyingLot(lot);

        test0TimeEntryExit();

        testFillingTo80Percent();
        displayMessage("Finished More Complete Test of Parking Lot\n");
    }

    /**
     * Test what happens when lot fills, empties, then refills.
     */
    private static void testComingAndGoing() {
        displayMessage("Testing Coming and Going...");
        ParkingLot busy = new ParkingLot(5);
        busy.markVehicleEntry(5);
        busy.markVehicleEntry(10);
        busy.markVehicleEntry(12);
        if(busy.isClosed()) {
            displayError("Busy lot should not be closed at time 12.");
        }
        busy.markVehicleEntry(15);
        if(!busy.isClosed()) {
            displayError("Busy lot should be closed at time 15.");
        }
        if(busy.getNumberOfSpotsRemaining()!=1) {
            displayError("Busy lot should have 1 spot open at time 15.");
        }
        busy.markVehicleEntry(20);
        if(busy.getNumberOfSpotsRemaining()!=0) {
            displayError("Busy lot should have 0 spots open at time 20.");
        }
        busy.markVehicleExit(23);
        if(busy.getNumberOfSpotsRemaining()!=1) {
            displayError("Busy lot should have 1 spot open at time 23.");
        }
        busy.markVehicleExit(25);
        busy.markVehicleExit(25);
        if(busy.getMinutesClosed()!=10) {
            displayError("Busy parking lot should have been closed for 10 minutes at time 30.");
        }
        busy.markVehicleEntry(33);
        busy.markVehicleEntry(35);
        if (!busy.isClosed()) {
            System.out.println(">>> Error: Busy lot should be closed at time 35");
        }
        if(busy.getNumberOfSpotsRemaining()!=1) {
            displayError("Busy lot should have 1 spot open at time 35.");
        }
        busy.markVehicleEntry(40);
        if(busy.getNumberOfSpotsRemaining()!=0) {
            displayError("Busy lot should have 0 spots open at time 40.");
        }
        busy.markVehicleExit(45);
        if(busy.getNumberOfSpotsRemaining()!=1) {
            displayError("Busy lot should have 1 spot open at time 45.");
        }
        busy.markVehicleExit(50);
        busy.markVehicleExit(54);
        busy.markVehicleExit(60);
        busy.markVehicleExit(60);
        if(busy.getMinutesClosed()!=25) {
            displayError("Busy parking lot should have been closed for 25 minutes.");
        }
        if(busy.getNumberOfSpotsRemaining()!=5) {
            displayError("Busy lot should be empty at end.");
        }
        displayMessage("Finished Testing Coming and Going\n");
    }

    /**
     * Create district with small parking lots, fill them, then
     * ensure one is not closed.
     */
    private static void testTinyDistrict() {
        displayMessage("Testing Tiny District...");
        District ourTown = new District();
        int redLot = ourTown.addLot("Red", 1);
        int greenLot = ourTown.addLot("Green", 1);
        int blueLot = ourTown.addLot("Blue", 2);
        displayResult("Tiny " + ourTown);
        if(ourTown.getNumberOfSpotsRemaining()!=4) {
            displayError("Expected 4 spots remaining in district (not "
                    + ourTown.getNumberOfSpotsRemaining() + ").");
        }
        ourTown.markVehicleEntry(greenLot, 5);
        ourTown.markVehicleEntry(redLot, 7);
        ourTown.markVehicleEntry(blueLot, 10);
        ourTown.markVehicleEntry(blueLot, 12);
        if(!ourTown.isClosed()) {
            displayError("The tiny district should be closed at time 12.");
        }
        if(ourTown.getNumberOfSpotsRemaining()!=0) {
            displayError("Expected 0 spots remaining in district (not "
                    + ourTown.getNumberOfSpotsRemaining() + ").");
        }
        ourTown.markVehicleExit(greenLot, 15);
        if(ourTown.isClosed()) {
            displayError("Tiny district should be open at time 15.");
        }
        if(ourTown.getNumberOfSpotsRemaining()!=1) {
            displayError("Expected 1 spot remaining in district (not "
                    + ourTown.getNumberOfSpotsRemaining() + ").");
        }
        displayResult(ourTown.toString());
        ourTown.markVehicleExit(redLot, 17);
        ourTown.markVehicleEntry(greenLot, 18);
        if(ourTown.isClosed()) {
            displayError("Tiny district should be open at time 18.");
        }
        displayResult("Final Tiny " + ourTown);
        displayMessage("Lots were closed for " + ourTown.getMinutesClosed()
                + " min. in tiny district.");
        displayMessage("Finished Testing Tiny District\n");
    }

    /**
     * Test District class with three lots.
     */
    private static void testDistrict() {
        displayMessage("Finished Testing Normal District...");
        District airport = new District();
        int brownLot = airport.addLot("Brown", 10);
        int greenLot = airport.addLot("Green", 15);
        int blackLot = airport.addLot("Black", 12);
        for(int i=0; i<7; i++) {
            airport.markVehicleEntry(brownLot, i);
            airport.markVehicleEntry(greenLot, i);
            airport.markVehicleEntry(greenLot, i);
            airport.markVehicleEntry(blackLot, i);
            if(airport.isClosed()) {
                displayError("Airport closed at time 7.");
            }
        }
        if(airport.getNumberOfSpotsRemaining()!=9) {
            displayError("Expected 9 spots remaining in district (not "
                    + airport.getNumberOfSpotsRemaining() + ").");
        }
        displayMessage("Airport at time 7:");
        displayResult(airport.toString());

        airport.markVehicleEntry(brownLot, 8);
        if(airport.isClosed()) {
            displayError("Airport closed at time 8.");
        }
        displayMessage("Airport at time 8:");
        displayResult(airport.toString());

        airport.markVehicleEntry(blackLot, 9);
        airport.markVehicleEntry(blackLot, 10);
        airport.markVehicleEntry(blackLot, 10);
        if(!airport.isClosed()) {
            displayError("Airport not closed at time 10.");
        }
        displayMessage("Airport at time 10:");
        displayResult(airport.toString());
        if(airport.getNumberOfSpotsRemaining()!=5) {
            displayError("Expected 9 spots remaining in district (not "
                    + airport.getNumberOfSpotsRemaining() + ").");
        }
        displayMessage("Finished Testing Normal District\n");
    }

    /**
     * Create districts with 1, 2, and 20 lots.
     */
    private static void testMultipleDistrictSizes() {
        District city = new District();
        city.addLot("Gray", 5);
        String expectedStatus = "District status:\n" +
                "  Status for Gray parking lot: 0 vehicles (0%)\n";
        if(!city.toString().equals(expectedStatus)) {
            displayError("Unexpected status in empty city: " + city);
        }

        // test with 2 lots
        city = new District();
        city.addLot("Black", 1);
        city.addLot("White", 3);
        city.markVehicleEntry(0, 1);
        city.markVehicleEntry(1, 1);
        expectedStatus = "District status:\n" +
                "  Status for Black parking lot: 1 vehicles (CLOSED)\n" +
                "  Status for White parking lot: 1 vehicles (33.3%)\n";
        if(!city.toString().equals(expectedStatus)) {
            displayError("Unexpected status in black and white city: " + city);
        }

        // test with many lots
        final int maxLots = 20;
        city = new District();
        final int NUM_MINUTES = 1000;
        for(int i=0; i<maxLots; i++) {
            city.addLot("Color" + i, 100);
        }
        for(int minute = 0; minute < NUM_MINUTES; minute++) {
            city.markVehicleEntry(minute % maxLots, minute);
        }
        ParkingLot lot9 = city.getLot(9);
        lot9.markVehicleExit(NUM_MINUTES + 1);
        if(!lot9.toString().equals("Status for Color9 parking lot: 49 vehicles (49%)")) {
            displayError("Incorrect status for lot 9: " + lot9);
        }
        if (city.getNumberOfSpotsRemaining()!=1+NUM_MINUTES) {
            displayError("Expected " + (1+NUM_MINUTES) + " open spots but found " + city.getNumberOfSpotsRemaining());
        }
    }

    /**
     * Test a number of vehicles entering and exiting three lots in a district.
     * Entry and exit is controlled by a string of digits in which 1-3
     * represents a vehicle entering lots 0-2 and 4-6 represents an exit.
     * Time passes with one minute between each entry or exit.
     * To help with debugging, 's' can be put in the string to print the
     * status at that time; add more output to the status report if you
     * find it helpful.
     */
    private static void testHeavyUsage() {
        displayMessage("Finished Testing Heavily Used District...");
        District town = new District();
        town.addLot("Pink", 25);
        town.addLot("Blue", 30);
        town.addLot("Gray", 10);

        String usage = "111243432523124321322156421123666324121345534534221" +
                // fill up lots
                "1111111122111122221111211122222233222222" +
                // a period of constant in/out traffic
                "4141414141525252525252636363636363636363636363636363636414141" +
                // a number of vehicles leave
                "55556556655454645546454446" +
                // random behavior at the end of the period
                "113232312434524621525241255146254245654241422211122661212321232";
        int min = 60;             // no activity until an hour into the day
        for(int i=0; i<usage.length(); i++) {
            char cmd = usage.charAt(i);
            if(cmd == 's') {
                displayResult("@ " + i + " " + town);
            } else if(cmd<='3') {
                town.markVehicleEntry(cmd - '1', min);
            } else {
                town.markVehicleExit(cmd - '4', min);
            }
            min++;
        }
        displayMessage("At end of day, all lots closed " + town.getMinutesClosed()
                + " min.");
        displayResult("Final " + town);
        displayMessage("Finished Testing Heavily Used District\n");
    }

    /**
     * Tests adding three vehicles to a lot with four spaces. Vehicles enter
     * at times 10, 12, and 13, the status is checked. Another vehicle
     * enters at time 20 and the status is checked again. One vehicle
     * then leaves followed by a final status check.
     *
     * @param lot ParkingLot with four spaces
     */
    private static void testFillingLot(ParkingLot lot) {
        displayMessage("Testing ParkingLot");
        lot.markVehicleEntry(10);
        lot.markVehicleEntry(12);
        lot.markVehicleEntry(13);
        displayResult(lot.toString());
        if(lot.isClosed()) {
            displayError("Lot closed with just 3 vehicles.");
        }
        if(lot.getMinutesClosed()!=0) {
            displayError("Closed minutes should be 0.");
        }
        lot.markVehicleEntry(20);
        displayResult(lot.toString());
        if(!lot.isClosed()) {
            displayError("Lot not closed with 4 vehicles.");
        }
        lot.markVehicleExit(31);
        if(lot.isClosed()) {
            displayError("Lot closed after first exit.");
        }
        if(lot.getMinutesClosed()!=11) {
            displayError("Expected 11 minutes of closed time, got "
                    + lot.getMinutesClosed() + ".");
        }
    }

    /**
     * This continues the test started in testFillingLot and assumes
     * initially there are three vehicles in the lot. One leaves and
     * two more enter. A vehicle leaves at 57 minutes, and the status
     * is rechecked. Finally, the parking lot is returned to closed at 71
     * minutes.
     *
     * @param lot ParkingLot with four spaces; must be same lot passed to testFillingLot()
     */
    private static void testRefillingLot(ParkingLot lot) {
        if(lot.getNumberOfSpotsRemaining()!=1) {
            displayError("Expected 1 spot remaining in lot.");
        }
        lot.markVehicleExit(45);
        // lot has 2 vehicles
        displayResult(lot.toString());
        lot.markVehicleEntry(50);
        lot.markVehicleEntry(52);
        lot.markVehicleExit(57);    // add 5 minutes
        // returned to 3 vehicles
        if(lot.getNumberOfSpotsRemaining()!=1) {
            displayError("Expected 1 spot remaining in lot.");
        }
        lot.markVehicleEntry(71);
        if(!lot.isClosed()) {
            displayError("Expected closed lot at 71 minutes.");
        }
    }

    /**
     * This continues the test started in testRefillingLot and assumes
     * all four spaces are filled. A vehicle leaves at 79 minutes,
     * followed by vehicles at 91, 92, and 94. The status is checked to
     * make sure no vehicles are on the lot.
     *
     * @param lot ParkingLot with four spaces; must be same lot passed to testReFillingLot()
     */
    private static void testEmptyingLot(ParkingLot lot) {
        if(lot.getNumberOfSpotsRemaining()!=0) {
            displayError("Expected 0 spots remaining in lot.");
        }
        lot.markVehicleExit(79);    // add 8 minutes
        if(lot.getMinutesClosed()!=24) {
            displayError("Expected 24 minutes of closed time, got "
                    + lot.getMinutesClosed() + ".");
        }
        if(lot.isClosed()) {
            displayError("Expected non-closed lot at 79 minutes.");
        }
        lot.markVehicleExit(91);
        lot.markVehicleExit(92);
        lot.markVehicleExit(94);
        displayResult(lot.toString());
        if(lot.getNumberOfSpotsRemaining()!=4) {
            displayError("Lot should be empty at 94 minutes.");
        }
        if(lot.getMinutesClosed()!=24) {
            displayError("Expected 24 minutes of closed time at end.");
        }
        displayResult("Final status: " + lot);
    }

    /**
     * Test entry/exit where the time is in the past, and that entering
     * and exiting within the same minute works.
     */
    private static void test0TimeEntryExit() {
        ParkingLot lot = new ParkingLot(6);

        lot.markVehicleEntry(50);
        if(lot.getNumberOfSpotsRemaining()!=5) {
            displayError("Expected 5 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot at time 50.");
        }
        // try to enter, exit in past
        lot.markVehicleEntry(49);
        if(lot.getNumberOfSpotsRemaining()!=5) {
            displayError("Expected 5 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot at time in the past.");
        }
        lot.markVehicleExit(49);
        if(lot.getNumberOfSpotsRemaining()!=5) {
            displayError("Expected 5 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot at second time in the past.");
        }
        // enter, exit all at once
        lot.markVehicleEntry(51);
        lot.markVehicleEntry(51);
        lot.markVehicleEntry(51);
        if(lot.getNumberOfSpotsRemaining()!=2) {
            displayError("Expected 2 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot at time 51.");
        }
        if(lot.isClosed()) {
            displayError("Lot should not be closed at time 51.");
        }
        lot.markVehicleEntry(51);
        if(lot.getNumberOfSpotsRemaining()!=1) {
            displayError("Expected 1 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot for second entry at time 51.");
        }
        if(!lot.isClosed()) {
            displayError("Lot should be closed for second entry at 51.");
        }
        lot.markVehicleExit(51);
        if(lot.getNumberOfSpotsRemaining()!=2) {
            displayError("Expected 2 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot after exit at time 51.");
        }
        if(lot.getMinutesClosed()!=0) {
            displayError("Expecting 0 minutes closed after exit at time 51.");
        }
    }

    /**
     * Test putting 16 vehicles into a lot with 19 spaces. This is 84%,
     * just enough to be above the threshold. For the first check,
     * 15 vehicles are added and the test confirms the lot is not closed.
     * One more is added and the fact it is flled is confirmed. Then
     * all vehicles are removed and the system checks that the lot is
     * empty and that there was just one minute where it was closed.
     */
    private static void testFillingTo80Percent() {
        ParkingLot lot = new ParkingLot(19);
        for(int i=0; i<15; i++) {
            lot.markVehicleEntry(i);
        }
        if(lot.getNumberOfSpotsRemaining()!=4) {
            displayError("Expected 4 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot.");
        }
        if(lot.getMinutesClosed()!=0) {
            displayError("Expected 0 minutes of closed time at 15.");
        }
        if(lot.isClosed()) {
            displayError("Expected non-closed lot at 15 minutes.");
        }
        lot.markVehicleEntry(16);
        if(!lot.isClosed()) {
            displayError("Expected closed lot at 16 minutes.");
        }
        lot.markVehicleExit(17);
        if(lot.isClosed()) {
            displayError("Expected non-closed lot at 17 minutes.");
        }
        for(int i=0; i<15; i++) {
            lot.markVehicleExit(i + 17);
        }
        if(lot.getNumberOfSpotsRemaining()!=19) {
            displayError("Expected 19 (not " + lot.getNumberOfSpotsRemaining()
                    + ") spots remaining in lot.");
        }
        if(lot.isClosed()) {
            displayError("Expected non-closed lot after all left.");
        }
        if(lot.getMinutesClosed()!=1) {
            displayError("Lot was closed 1 minute.");
        }
    }
}