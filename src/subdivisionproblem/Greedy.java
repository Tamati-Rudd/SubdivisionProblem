package subdivisionproblem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class uses the greedy approach to solve the subdivision problem
 * Makes a greedy choice, then recursively finds the optimal subdivisions for that choice
 * @author Tamati Rudd 18045626
 */
public class Greedy {
    public Land root;
    public ArrayList<Land> possibleSubdivisions;
    public Land greedyChoice;
    public Land currentRoot;
    
    /**
     * Construct a new Greedy instance
     * @param initialLand land that needs to be subdivided
     */
    public Greedy(Land initialLand) {
        this.root = initialLand;
        possibleSubdivisions = new ArrayList<>();
        possibleSubdivisions.add(root.clone()); //Case: do not subdivide
    }
    
    /**
     * Run the Greedy algorithm:
     * - Perform the first level of subdivision
     * - Make the greedy choice
     * - If the greedy choice was a subdivided solution, find & combine the optimal subdivisions
     * - Return the greedy choice
     */
    public Land runAlgorithm() {
        //Perform first layer of subdivision on root (initial land)
        for (int m = 0; m < root.width-1; m++) {
            root.subdivide(Direction.VERTICAL, m+1);
            possibleSubdivisions.add(root.clone());
            root.clear();
        }
        for (int n = 0; n < root.height-1; n++) {
            root.subdivide(Direction.HORIZONTAL, n+1);
            possibleSubdivisions.add(root.clone());
            root.clear();
        }
        
        //Make the greedy choice 
        greedyChoice = Collections.max(possibleSubdivisions);              
        possibleSubdivisions.clear();

        //If the greedy choice was a subdivided solution, find & combine optimal left and right subdivisions
        //Will simply return if the greedy choice was to not subdivide
        if (greedyChoice.hasLeftChild() && greedyChoice.hasRightChild()) {
            //Find the optimal left subdivision of the greedy choice
            currentRoot = greedyChoice.leftChild.clone();
            possibleSubdivisions.add(currentRoot);
            greedyRecursion(currentRoot);
            Land optimalLeft = Collections.max(possibleSubdivisions);
            possibleSubdivisions.clear();

            //Find the optimal right subdivision of the greedy choice
            currentRoot = greedyChoice.leftChild.clone();
            possibleSubdivisions.add(currentRoot);
            greedyRecursion(currentRoot);
            Land optimalRight = Collections.max(possibleSubdivisions);
            possibleSubdivisions.clear();

            //Combine the optimal subdivisions with the greedy choice
            greedyChoice.leftChild = optimalLeft;
            greedyChoice.rightChild = optimalRight; 
        }
        
        return greedyChoice;
    }
    
    /**
     * Perform recursion on the Greedy choice to maximize greedy value
     * @param currentLand current land to subdivide
     */
    public void greedyRecursion(Land currentLand) {
        for (int m = 0; m < currentLand.width-1; m++) { //Vertical split
            currentLand.subdivide(Direction.VERTICAL, m+1);
            possibleSubdivisions.add(currentRoot.clone());
            greedyRecursion(currentLand.leftChild);
            greedyRecursion(currentLand.rightChild);
            currentLand.clear();
        }
        for (int n = 0; n < currentLand.height-1; n++) { //Horizontal split
            currentLand.subdivide(Direction.HORIZONTAL, n+1);
            possibleSubdivisions.add(currentRoot.clone());
            greedyRecursion(currentLand.leftChild);
            greedyRecursion(currentLand.rightChild);
            currentLand.clear();
        }
    }
}
