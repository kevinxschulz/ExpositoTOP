package top;

/**
 * @class TOPTWRoute
 * @brief Represents a route segment in a TOPTW solution.
 *
 * This class models a route segment by storing the predecessor node, successor node, and route identifier.
 * It provides getter and setter methods for these attributes, supporting the construction and manipulation
 * of routes within Team Orienteering Problem with Time Windows (TOPTW) solutions.
 */
public class TOPTWRoute {
    int predecessor;
    int succesor;
    int id;
    
    TOPTWRoute() {
        
    }
    
    TOPTWRoute(int pre, int succ, int id) {
        this.predecessor = pre;
        this.succesor = succ;
        this.id = id;
    }
    
    public int getPredeccesor() {
        return this.predecessor;
    }
    
    public int getSuccesor() {
        return this.succesor;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setPredeccesor(int pre) {
        this.predecessor = pre;
    }
    
    public void setSuccesor(int suc) {
        this.succesor = suc;
    }
    
    public void setId(int id) {
        this.id = id;
    }
}
