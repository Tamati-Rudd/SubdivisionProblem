package subdivisionproblem;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class uses an exact dynamic programming approach to solve the subdivision problem
 * @author Tamati Rudd 18045626
 */
public class Exact {
    public Land root;
    public Land[][] optimalSubproblems;
    public ArrayList<Land> possibleSolutions;
    
    /**
     * Construct a new Exact instance
     * @param initialLand land that needs to be subdivided
     */
    public Exact(Land initialLand) {
        this.root = initialLand;
        optimalSubproblems = new Land[root.width][root.height];
        possibleSolutions = new ArrayList<>();
    }
    
    /**
     * Run the exact approach algorithm (dynamic programming)
     * - Beginning with 1x1, use bottom up dynamic programming approach to find the optimal solution for each Land subproblem
     * - Build up to the original land size (covering width then height), using existing solutions to improve efficiency
     * @return Optimal subdivision for the original land
     */
    public Land runAlgorithm() {
        for (int n = 0; n < root.height; n++) { //Until the height of the original land is reached
            for (int m = 0; m < root.width; m++) { //Until the width of the original land is reached
                if (n == 0 && m == 0) //Handle 1 x 1, which cannot be subdivided
                    optimalSubproblems[0][0] = new Land(1, 1);
                else //Compute optimal solution (subdivision) for subproblem
                    computeOptimalSolution(new Land(m+1, n+1));
            }
        }
        
        return optimalSubproblems[root.width-1][root.height-1];
    }
    
    /**
     * Compute the optimal solution (subdivision) for Land of a particular size
     * @param problem the Land size to compute for
     */
    public void computeOptimalSolution(Land problem) {
        //Account for case: do not subdivide
        possibleSolutions.add(problem.clone());
        
        //Account for possible vertical split cases
        for (int m = 0; m < problem.width-1; m++) { //Vertical splits
            //Determine size of subdivision
            Land problemClone = problem.clone();
            problemClone.subdivide(Direction.VERTICAL, m+1);
            //Get the optimal solution for each subproblem
            problemClone.leftChild = optimalSubproblems[problemClone.leftChild.width-1][problemClone.leftChild.height-1];
            problemClone.rightChild = optimalSubproblems[problemClone.rightChild.width-1][problemClone.rightChild.height-1];
            possibleSolutions.add(problemClone);
        }
        
        //Account for possible horizontal split cases
        for (int n = 0; n < problem.height-1; n++) {
            //Determine size of subdivision
            Land problemClone = problem.clone();
            problemClone.subdivide(Direction.HORIZONTAL, n+1);
            //Get the optimal solution for each subproblem
            problemClone.leftChild = optimalSubproblems[problemClone.leftChild.width-1][problemClone.leftChild.height-1];
            problemClone.rightChild = optimalSubproblems[problemClone.rightChild.width-1][problemClone.rightChild.height-1];
            possibleSolutions.add(problemClone);
        }

        //Save the optimal subproblem solution, and clear the possible solutions ArrayList for the next problem
        optimalSubproblems[problem.width-1][problem.height-1] = Collections.max(possibleSolutions);
        possibleSolutions.clear();
    }
}
