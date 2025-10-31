package top;

/**
 * @class TOPTWEvaluator
 * @brief Provides evaluation methods for TOPTW solutions.
 *
 * This utility class contains methods to assess the quality and objective function value of solutions
 * to the Team Orienteering Problem with Time Windows (TOPTW). It can be extended to implement various
 * evaluation strategies, such as calculating total distance, score, or feasibility of routes.
 * The class is intended to support solution analysis and optimization processes.
 */
public class TOPTWEvaluator {
    public static double NO_EVALUATED = -1.0;

    public void evaluate(TOPTWSolution solution) {
        /*CumulativeCVRP problem = solution.getProblem();
        double objectiveFunctionValue = 0.0;
        for (int i = 0; i < solution.getIndexDepot().size(); i++) {
            double cumulative = 0;
            int depot = solution.getAnIndexDepot(i);
            int actual = depot;
            actual = solution.getSuccessor(actual);
            cumulative += problem.getDistanceMatrix(0, actual);
            objectiveFunctionValue += problem.getDistanceMatrix(0, actual);
            System.out.println("Desde " + 0 + " a " + actual + " = " + cumulative);
            while (actual != depot) {
                int ant = actual;
                actual = solution.getSuccessor(actual);
                if (actual != depot) {
                    cumulative += problem.getDistanceMatrix(ant, actual);
                    objectiveFunctionValue += cumulative;
                    System.out.println("Desde " + ant + " a " + actual + " = " + cumulative);
                } else {
                    cumulative += problem.getDistanceMatrix(ant, 0);
                    objectiveFunctionValue += cumulative;
                    System.out.println("Desde " + ant + " a " + 0 + " = " + cumulative);
                }
            }
            System.out.println("");
        }
        solution.setObjectiveFunctionValue(objectiveFunctionValue);*/
    }
}
