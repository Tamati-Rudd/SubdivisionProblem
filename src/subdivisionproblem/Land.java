package subdivisionproblem;

/**
 * This class represents a single plot of land in the subdivision problem
 * A land object can have two children - subdivided land plots 
 * @author Tamati Rudd 18045626
 */
public class Land implements Comparable<Land> {
    public int width; //Width in meters (m)
    public int height; //Height in meters (m)
    
    public int cost; //Cost of any subdivisions
    
    public Land leftChild = null; //Left tree child node (left or top subdivision)
    public Land rightChild = null; //Right tree child node (right or bottom subdivision)
    
    public final int SUBDIVISION_COST = 20; //Cost to subdivide 1 meter
    /**
     * Prices depending on the size of the land (up to 6x6)
     */
    public static int[][] PRICES = {
        {20, 40, 100, 130, 150, 200},
        {40, 140, 250, 320, 400, 450},
        {100, 250, 350, 420, 450, 500},
        {130, 320, 420, 500, 600, 700},
        {150, 400, 450, 600, 700, 800},
        {200, 450, 500, 700, 800, 900}
    }; //Designed for SUBDIVISION_COST = 50
//    public static int[][] PRICES = {
//        {10, 10, 20, 70, 75, 80},
//        {10, 20, 50, 85, 95, 110},
//        {20, 50, 95, 100, 110, 150},
//        {70, 85, 100, 115, 145, 165},
//        {75, 95, 110, 145, 165, 185},
//        {80, 110, 150, 165, 185, 220}            
//    }; //Designed for SUBDIVISION_COST = 10
    
    /**
     * Construct a new land plot
     * @param width width of the land plot
     * @param height height of the land plot
     */
    public Land(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    /**
     * Perform a subdivision
     * @param direction direction of the split
     * @param split where to split
     */
    public void subdivide(Direction direction, int split) {
        if (direction.equals(Direction.HORIZONTAL)) { //Split horizontally
            leftChild = new Land(width, split); //Top split
            rightChild = new Land(width, height-split); //Bottom split
            cost = width * SUBDIVISION_COST;
        } else if (direction.equals(Direction.VERTICAL)) { //Split vertically
            leftChild = new Land(split, height); //Left split
            rightChild = new Land(width-split, height); //Right split
            cost = height * SUBDIVISION_COST;
        }
    }
      
    /**
     * Get the benefit (land price without cost) of the Land
     * @return land benefit
     */
    public int getBenefit() {
        if (hasLeftChild()) {
            return leftChild.getBenefit() + rightChild.getBenefit();
        }
        return PRICES[this.width-1][this.height-1];
    }
    
    public int getCost() {
        if (hasLeftChild()) {
            return cost + leftChild.getCost() + rightChild.getCost();
        }
        return 0;
    }
    
    /**
     * Calculate the value of the land
     * @return value = benefit - cost
     */
    public int getValue() {
        return getBenefit() - getCost();
    }
    
    /**
     * Remove all children (subdivisions) of a Land object
     */
    public void clear() {
        leftChild = null;
        rightChild = null;
        cost = 0;
    }
    
    /**
     * Test if the land is 1m x 1m, so can no longer be subdivided
     * @return whether the land is 1x2
     */
    public boolean is1x1() {
        return (width == 1 && height == 1);
    }
    
    /**
     * Test if the Land object has a left child (subdivision)
     * @return whether the Land has a left child
     */
    public boolean hasLeftChild() {
        return (leftChild != null);
    }
    
    /**
     * Test if the Land object has a right child (subdivision)
     * @return whether the Land has a right child
     */
    public boolean hasRightChild() {
        return (rightChild != null);
    }
    
    /**
     * Create a clone of a Land object (and its subdivisions)
     * @return Clone of this land object
     */
    public Land clone() {
        Land clone = new Land(width, height);
        clone.cost = cost;
        if (hasLeftChild()) {
            clone.leftChild = leftChild.clone();
        }
        if (hasRightChild()) {
            clone.rightChild = rightChild.clone();
        }
        
        return clone;
    }
    
    /**
     * Compare two Land objects by their total value
     * @param otherLand the land to compare against
     * @return which land plot has more value
     */
    @Override
    public int compareTo(Land otherLand) {
        int myValue = getValue();
        int otherValue = otherLand.getValue();
        if (myValue > otherValue) {
            return 1; //This land has more value
        } else if (myValue < otherValue) {
            return -1; //The other land has more value
        }
        return 0; //Both land plots have the same value
    }
    
    /**
     * Print a summary of the properties of the Land
     * @return Land summary
     */
    @Override
    public String toString() {        
        String summary = "Land: "+width+"m x "+height+"m with benefit $"+getBenefit()+", cost $"+cost+", value $"+getValue()+"\n";
        if (hasLeftChild())
            summary += "LEFT: "+leftChild.toString();
        if (hasRightChild())
            summary += "RIGHT: "+rightChild.toString();
        return summary;
    }
}
