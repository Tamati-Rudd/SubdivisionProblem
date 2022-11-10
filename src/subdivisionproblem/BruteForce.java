package subdivisionproblem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class uses the brute force approach to solve the subdivision problem
 * @author Tamati Rudd 18045626
 */
public class BruteForce {
    public Land root;
    public ArrayList<Land> possibleSubdivisions;
    
    /**
     * Construct a new Brute Force instance
     * @param initialLand land that needs to be subdivided
     */
    public BruteForce(Land initialLand) {
        this.root = initialLand;
        possibleSubdivisions = new ArrayList<>();
        possibleSubdivisions.add(root.clone()); //Case: do not subdivide
    }
    
    /**
     * Run the Brute Force algorithm
     * - Recursively compute ALL possible solutions/subdivisions
     * - Return the optimal solution (solution with the greatest value)
     */
    public Land runAlgorithm() {
        //Recursively compute ALL possible solutions
        bruteForceRecursion(root);
        //Return the solution with the greatest value
        return Collections.max(possibleSubdivisions);
    }
    
    /**
     * Recursively find ALL possible subdivisions for a Land plot
     * - Recursion is done moving down the trees of Land objects
     * - Each possible solution is saved into possibleSubdivisions
     * - Recursive calls end when 1x1 land size is reached
     * @param currentLand current land to subdivide
     */
    public void bruteForceRecursion(Land currentLand) {
        for (int m = 0; m < currentLand.width-1; m++) { //Vertical split
            currentLand.subdivide(Direction.VERTICAL, m+1);
            possibleSubdivisions.add(root.clone());
            bruteForceRecursion(currentLand.leftChild);
            bruteForceRecursion(currentLand.rightChild);
            currentLand.clear();
        }
        for (int n = 0; n < currentLand.height-1; n++) { //Horizontal split
            currentLand.subdivide(Direction.HORIZONTAL, n+1);
            possibleSubdivisions.add(root.clone());
            bruteForceRecursion(currentLand.leftChild);
            bruteForceRecursion(currentLand.rightChild);
            currentLand.clear();
        }
    }
}
