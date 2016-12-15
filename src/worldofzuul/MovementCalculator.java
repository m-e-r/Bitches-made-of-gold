package worldofzuul;

/**
 * A class that holds the calculations methods for moving around the world.
 */
public class MovementCalculator {

    /**
     * Used for calculating the distance between two coordinates.
     *
     * @param startX a coordinate in the first coordinate set
     * @param startY a coordinate in the first coordinate set
     * @param toX a coordinate in the last coordinate set
     * @param toY a coordinate in the last coordinate set
     * @return the distance between each coordinate set
     */
    public int calculateDistance(int startX, int startY, int toX, int toY) {
        int distance = 0;
        distance = (int) Math.sqrt(
                Math.pow(Math.abs(startX - toX), 2)
                + Math.pow(Math.abs(startY - toY), 2)
        );
        return distance;
    }

    /**
     * A method that will tell whether it is possible to reach a certain
     * coordinate from a coordinate.
     *
     * @param startX a coordinate in the first coordinate set
     * @param startY a coordinate in the first coordinate set
     * @param toX a coordinate in the last coordinate set
     * @param toY a coordinate in the last coordinate set
     * @param currentFuel the amount of fuel to calculate with
     * @return whether it is possible or not to reach the coordinate
     */
    public boolean isReachable(int startX, int startY, int toX, int toY, int currentFuel) {
        if (currentFuel >= this.calculateDistance(startX, startY, toX, toY)) {
            return true;
        } else {
            return false;
        }
    }
}
