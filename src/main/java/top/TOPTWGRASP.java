package top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * @class TOPTWGRASP
 * @brief Implements the GRASP metaheuristic for solving TOPTW instances.
 *
 * This class provides methods for constructing and improving solutions to the Team Orienteering Problem with Time Windows (TOPTW)
 * using the Greedy Randomized Adaptive Search Procedure (GRASP). It includes routines for greedy randomized construction,
 * candidate selection (including fuzzy logic and alpha-cut strategies), and solution evaluation.
 * The class supports iterative optimization, restricted candidate list management, and integration with solution and problem data.
 */
public class TOPTWGRASP {
 public static double NO_EVALUATED = -1.0;
    
    private TOPTWSolution solution;
    private int solutionTime;
    private Random random;

    public TOPTWGRASP(TOPTWSolution sol){
        this.solution = sol;
        this.solutionTime = 0;
        this.random = new Random();
    }
    
    /*procedure GRASP(Max Iterations,Seed)
        1 Read Input();
        2 for k = 1, . . . , Max Iterations do
            3 Solution ← Greedy Randomized Construction(Seed);
            4 Solution ← Local Search(Solution);
            5 Update Solution(Solution,Best Solution);
        6 end;
        7 return Best Solution;
    end GRASP*/
    
    /*procedure Greedy Randomized Construction(Seed)
        Solution ← ∅;
        Evaluate the incremental costs of the candidate elements;
        while Solution is not a complete solution do
            Build the restricted candidate list (RCL);
            Select an element s from the RCL at random;
            Solution ← Solution ∪ {s};
            Reevaluate the incremental costs;
        end;
        return Solution;
    end Greedy Randomized Construction.*/
    
    public void GRASP(int maxIterations, int maxSizeRCL) {
        double averageFitness = 0.0;
        double bestSolution = 0.0;
        for(int i = 0; i < maxIterations; i++) {
            
            this.computeGreedySolution(maxSizeRCL);
            
            // IMPRIMIR SOLUCION
            double fitness = this.solution.evaluateFitness();
            System.out.println(this.solution.getInfoSolution());
            //System.out.println("Press Any Key To Continue...");
            //new java.util.Scanner(System.in).nextLine();
            averageFitness += fitness;
            if(bestSolution < fitness) {
                bestSolution = fitness;
            }
            //double fitness = this.solution.printSolution();
                   
            /******
            * 
            * BÚSQUEDA LOCAL
            * 
            */
        }
        averageFitness = averageFitness/maxIterations;
        System.out.println(" --> MEDIA: "+averageFitness);
        System.out.println(" --> MEJOR SOLUCION: "+bestSolution);
    }
    
    public int aleatorySelectionRCL(int maxTRCL) {
       int low = 0;
       int high = maxTRCL;
       return random.nextInt(high-low) + low;
    }
    
    public int fuzzySelectionBestFDRCL(ArrayList< double[] > rcl) {
        double[] membershipFunction = new double[rcl.size()];
        double maxSc = this.getMaxScore();
        for(int j=0; j < rcl.size(); j++) {
            membershipFunction[j] = 1 - ((rcl.get(j)[4])/maxSc);
        }
        double minMemFunc = Double.MAX_VALUE;
        int posSelected = -1;
        for(int i = 0; i < rcl.size(); i++) {
            if(minMemFunc > membershipFunction[i]) {
                minMemFunc = membershipFunction[i];
                posSelected = i;
            }
        }
        return posSelected;
    }
    
    public int fuzzySelectionAlphaCutRCL(ArrayList< double[] > rcl, double alpha) {
        ArrayList< double[] > rclAlphaCut = new ArrayList< double[] >();
        ArrayList< Integer > rclPos = new ArrayList< Integer >();
        double[] membershipFunction = new double[rcl.size()];
        double maxSc = this.getMaxScore();
        for(int j=0; j < rcl.size(); j++) {
            membershipFunction[j] = 1 - ((rcl.get(j)[4])/maxSc);
            if(membershipFunction[j] <= alpha) {
                rclAlphaCut.add(rcl.get(j));
                rclPos.add(j);
            }
        }
        int posSelected = -1;
        if(rclAlphaCut.size() > 0) {
            posSelected = rclPos.get(aleatorySelectionRCL(rclAlphaCut.size()));
        } else {
            posSelected = aleatorySelectionRCL(rcl.size());
        }
        return posSelected;
    }

    public void computeGreedySolution(int maxSizeRCL) {
    // inicialización
    this.solution.initSolution();
    
    ArrayList<ArrayList<Double>> departureTimesPerClient = initializeDepartureTimes();
    ArrayList<Integer> customers = initializeCustomers();
    
    ArrayList<double[]> candidates = evaluateAndSortCandidates(customers, departureTimesPerClient);
    
    boolean existCandidates = true;
    
    while(!customers.isEmpty() && existCandidates) {
        if(!candidates.isEmpty()) {
            processAvailableCandidates(maxSizeRCL, candidates, customers, departureTimesPerClient);
        } else {
            existCandidates = tryCreateNewRoute(departureTimesPerClient);
        }
        
        candidates = evaluateAndSortCandidates(customers, departureTimesPerClient);
    }
}

private ArrayList<ArrayList<Double>> initializeDepartureTimes() {
    ArrayList<ArrayList<Double>> departureTimesPerClient = new ArrayList<>();
    ArrayList<Double> init = new ArrayList<>();
    
    int size = this.solution.getProblem().getPOIs() + this.solution.getProblem().getVehicles();
    for(int z = 0; z < size; z++) {
        init.add(0.0);
    }
    
    departureTimesPerClient.add(0, init);
    return departureTimesPerClient;
}

private ArrayList<Integer> initializeCustomers() {
    ArrayList<Integer> customers = new ArrayList<>();
    for(int j = 1; j <= this.solution.getProblem().getPOIs(); j++) {
        customers.add(j);
    }
    return customers;
}

private ArrayList<double[]> evaluateAndSortCandidates(ArrayList<Integer> customers, 
                                                       ArrayList<ArrayList<Double>> departureTimesPerClient) {
    ArrayList<double[]> candidates = this.comprehensiveEvaluation(customers, departureTimesPerClient);
    
    Collections.sort(candidates, new Comparator<double[]>() {
        public int compare(double[] a, double[] b) {
            return Double.compare(a[a.length-2], b[b.length-2]);
        }
    });
    
    return candidates;
}

private void processAvailableCandidates(int maxSizeRCL, ArrayList<double[]> candidates, 
                                        ArrayList<Integer> customers, 
                                        ArrayList<ArrayList<Double>> departureTimesPerClient) {
    ArrayList<double[]> rcl = buildRestrictedCandidateList(maxSizeRCL, candidates);
    
    int posSelected = selectCandidateFromRCL(rcl);
    double[] candidateSelected = rcl.get(posSelected);
    
    removeSelectedCustomer(customers, candidateSelected);
    updateSolution(candidateSelected, departureTimesPerClient);
}

private ArrayList<double[]> buildRestrictedCandidateList(int maxSizeRCL, ArrayList<double[]> candidates) {
    ArrayList<double[]> rcl = new ArrayList<>();
    int maxTRCL = Math.min(maxSizeRCL, candidates.size());
    
    for(int j = 0; j < maxTRCL; j++) {
        rcl.add(candidates.get(j));
    }
    
    return rcl;
}

private int selectCandidateFromRCL(ArrayList<double[]> rcl) {
    int selection = 3;
    double alpha = 0.8;
    
    switch (selection) {
        case 1:
            return this.aleatorySelectionRCL(rcl.size());
        case 2:
            return this.fuzzySelectionBestFDRCL(rcl);
        case 3:
            return this.fuzzySelectionAlphaCutRCL(rcl, alpha);
        default:
            return this.aleatorySelectionRCL(rcl.size());
    }
}

private void removeSelectedCustomer(ArrayList<Integer> customers, double[] candidateSelected) {
    customers.removeIf(customer -> customer == candidateSelected[0]);
}

private boolean tryCreateNewRoute(ArrayList<ArrayList<Double>> departureTimesPerClient) {
    if(this.solution.getCreatedRoutes() < this.solution.getProblem().getVehicles()) {
        this.solution.addRoute();
        
        ArrayList<Double> initNew = new ArrayList<>();
        int size = this.solution.getProblem().getPOIs() + this.solution.getProblem().getVehicles();
        for(int z = 0; z < size; z++) {
            initNew.add(0.0);
        }
        
        departureTimesPerClient.add(initNew);
        return true;
    }
    
    return false;
}
    
    public void updateSolution(double[] candidateSelected, ArrayList< ArrayList< Double > > departureTimes) {
        // Inserción del cliente en la ruta  return: cliente, ruta, predecesor, coste
        this.solution.setPredecessor((int)candidateSelected[0], (int)candidateSelected[2]);
        this.solution.setSuccessor((int)candidateSelected[0], this.solution.getSuccessor((int)candidateSelected[2]));
        this.solution.setSuccessor((int)candidateSelected[2], (int)candidateSelected[0]);
        this.solution.setPredecessor(this.solution.getSuccessor((int)candidateSelected[0]), (int)candidateSelected[0]);
        
        // Actualización de las estructuras de datos y conteo a partir de la posición a insertar
        double costInsertionPre = departureTimes.get((int)candidateSelected[1]).get((int)candidateSelected[2]);
        ArrayList<Double> route = departureTimes.get((int)candidateSelected[1]);
        int pre=(int)candidateSelected[2], suc=-1;
        int depot = this.solution.getIndexRoute((int)candidateSelected[1]);
        do {
            suc = this.solution.getSuccessor(pre);
            costInsertionPre += this.solution.getDistance(pre, suc);
            
            if(costInsertionPre < this.solution.getProblem().getReadyTime(suc)) {
                costInsertionPre = this.solution.getProblem().getReadyTime(suc);
            }
            costInsertionPre += this.solution.getProblem().getServiceTime(suc);
             
            if(!this.solution.isDepot(suc))
                route.set(suc, costInsertionPre);
            pre = suc;
        } while((suc != depot));
        
        // Actualiza tiempos
        departureTimes.set((int)candidateSelected[1], route);
    }

    //return: cliente, ruta, predecesor, coste tiempo, score
    public ArrayList<double[]> comprehensiveEvaluation(ArrayList<Integer> customers, ArrayList<ArrayList<Double>> departureTimes) {
    ArrayList<double[]> candidatesList = new ArrayList<>();
    
    for(int c = 0; c < customers.size(); c++) {
        int candidate = customers.get(c);
        double[] bestInsertion = findBestInsertionForCandidate(candidate, departureTimes);
        
        if(isValidCandidate(bestInsertion)) {
            candidatesList.add(bestInsertion.clone());
        }
    }
    
    return candidatesList;
}

private double[] findBestInsertionForCandidate(int candidate, ArrayList<ArrayList<Double>> departureTimes) {
    double[] bestInsertion = initializeCandidateInfo();
    
    for(int k = 0; k < this.solution.getCreatedRoutes(); k++) {
        double[] routeInsertion = evaluateCandidateInRoute(candidate, k, departureTimes);
        
        if(isValidCandidate(routeInsertion) && routeInsertion[3] < bestInsertion[3]) {
            bestInsertion = routeInsertion;
        }
    }
    
    return bestInsertion;
}

private double[] evaluateCandidateInRoute(int candidate, int routeIndex, ArrayList<ArrayList<Double>> departureTimes) {
    double[] bestInsertion = initializeCandidateInfo();
    int depot = this.solution.getIndexRoute(routeIndex);
    int pre = depot;
    
    do {
        int suc = this.solution.getSuccessor(pre);
        double[] insertionAttempt = tryInsertionBetween(candidate, pre, suc, routeIndex, depot, departureTimes);
        
        if(isValidCandidate(insertionAttempt) && insertionAttempt[3] < bestInsertion[3]) {
            bestInsertion = insertionAttempt;
        }
        
        pre = suc;
    } while(pre != depot);
    
    return bestInsertion;
}

private double[] tryInsertionBetween(int candidate, int pre, int suc, int routeIndex, int depot, 
                                      ArrayList<ArrayList<Double>> departureTimes) {
    double[] insertionInfo = initializeCandidateInfo();
    
    double timesUntilPre = departureTimes.get(routeIndex).get(pre) + this.solution.getDistance(pre, candidate);
    
    if(!isWithinDueTime(timesUntilPre, candidate)) {
        return insertionInfo;
    }
    
    double costCand = calculateArrivalCost(timesUntilPre, candidate);
    
    if(costCand > this.solution.getProblem().getMaxTimePerRoute()) {
        return insertionInfo;
    }
    
    double timesUntilSuc = costCand + this.solution.getDistance(candidate, suc);
    
    if(!isWithinDueTime(timesUntilSuc, suc)) {
        return insertionInfo;
    }
    
    double costSuc = calculateArrivalCost(timesUntilSuc, suc);
    
    if(costSuc > this.solution.getProblem().getMaxTimePerRoute()) {
        return insertionInfo;
    }
    
    if(suc != depot && !validateRemainingRoute(suc, costSuc, depot)) {
        return insertionInfo;
    }
    
    insertionInfo[0] = candidate;
    insertionInfo[1] = routeIndex;
    insertionInfo[2] = pre;
    insertionInfo[3] = costSuc;
    insertionInfo[4] = this.solution.getProblem().getScore(candidate);
    
    return insertionInfo;
}

private boolean validateRemainingRoute(int startNode, double startCost, int depot) {
    int pre = startNode;
    double currentCost = startCost;
    
    do {
        int suc = this.solution.getSuccessor(pre);
        double timesUntilNext = currentCost + this.solution.getDistance(pre, suc);
        
        if(!isWithinDueTime(timesUntilNext, suc)) {
            return false;
        }
        
        currentCost = calculateArrivalCost(timesUntilNext, suc);
        
        if(currentCost > this.solution.getProblem().getMaxTimePerRoute()) {
            return false;
        }
        
        pre = suc;
    } while(pre != depot);
    
    return true;
}

private double calculateArrivalCost(double arrivalTime, int node) {
    double cost = Math.max(arrivalTime, this.solution.getProblem().getReadyTime(node));
    return cost + this.solution.getProblem().getServiceTime(node);
}

private boolean isWithinDueTime(double arrivalTime, int node) {
    return arrivalTime < this.solution.getProblem().getDueTime(node);
}

private double[] initializeCandidateInfo() {
    double[] info = new double[5];
    info[0] = -1;  // candidate
    info[1] = -1;  // route
    info[2] = -1;  // predecessor
    info[3] = Double.MAX_VALUE;  // cost
    info[4] = -1;  // score
    return info;
}

private boolean isValidCandidate(double[] candidateInfo) {
    return candidateInfo[0] != -1 && 
           candidateInfo[1] != -1 && 
           candidateInfo[2] != -1 && 
           candidateInfo[3] != Double.MAX_VALUE && 
           candidateInfo[4] != -1;
}
    
    public TOPTWSolution getSolution() {
        return solution;
    }

    public void setSolution(TOPTWSolution solution) {
        this.solution = solution;
    }

    public int getSolutionTime() {
        return solutionTime;
    }

    public void setSolutionTime(int solutionTime) {
        this.solutionTime = solutionTime;
    }
    
    public double getMaxScore() {
        double maxSc = -1.0;
        for(int i = 0; i < this.solution.getProblem().getScore().length; i++) {
            if(this.solution.getProblem().getScore(i) > maxSc)
                maxSc = this.solution.getProblem().getScore(i);
        }
        return maxSc;
    }

}
