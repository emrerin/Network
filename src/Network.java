//package ad1.ss16.pa;
import java.util.*;

// -> http://beginnersbook.com/2013/12/difference-between-arraylist-and-linkedlist-in-java/
public class Network  {
    public int number;
    public ArrayList<Integer>[] nodes;

    @SuppressWarnings("unchecked")
    public Network(int n) {
        this.number = n;
        nodes = new ArrayList[n];
        for(int i = 0; i < nodes.length; i++){
            nodes[i] = new ArrayList<>();
        }
    }

    public int numberOfNodes() {
        return this.number;
    }

    public int numberOfConnections() {
        int sumConnections = 0;
        for(int i = 0; i < this.number; i++){
            sumConnections += this.nodes[i].size();
        }
        return sumConnections/2;
    }

    public void addConnection(int v, int w){
        if(! this.nodes[v].contains(w) && v != w){
            this.nodes[v].add(w);
            this.nodes[w].add(v);
        }
    }

    public void addAllConnections(int v){
        for(int i = 0; i < this.number; i++){
            this.addConnection(v, i);
        }
    }

    public void deleteConnection(int v, int w){
        this.nodes[v].remove((Integer)(w));
        this.nodes[w].remove((Integer)(v));
    }

    public void deleteAllConnections(int v){
        for(int i = 0; i < this.number; i++){
            this.deleteConnection(v, i);
        }
    }

    //Ein Knoten u ist von s genau dann erreichbar, wenn Discovered[u]=true ist.
    // liefert die Anzahl der Zusammenhangskomponenten des Graphen
    public int numberOfComponents() {
        int components = 0;
        boolean[] marked = new boolean[this.number];

        for(int i = 0; i < this.number; i++){
            if(! marked[i]){
                components++;
                depthFirstSearch(i, marked);
            }
        }
        return components;
    }

    private void depthFirstSearch(int position, boolean[] marked){
        marked[position] = true;

        for(int i = 0; i < nodes[position].size(); i++) {
            int elem = nodes[position].get(i);
            if (! marked[elem]) {
                depthFirstSearch(elem, marked);
            }
        }
    }

    public boolean hasCycle() {
        boolean visited[] = new boolean[this.number];

        for(int i = 0; i < this.number; i++){
            if(! visited[i]){
                if(isCyclicUtil(i, visited, -1)){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCyclicUtil(int v, boolean visited[], int parent){
        visited[v] = true;
        Iterator<Integer> it = nodes[v].iterator();
        while (it.hasNext()) {
            int i = it.next();
            if (! visited[i]) {
                if (isCyclicUtil(i, visited, v)){
                    return true;
                }
            }
            else if (i != parent){
                return true;
            }
        }
        return false;
    }

    public int minimalNumberOfConnections(int start, int end){
        if(start == end) { return 0; }
        Queue<Integer> q = new LinkedList<>();
        boolean[] discovered = new boolean[this.number];
        int[] dist = new int[this.number];
        q.add(start);
        dist[start] = 0;  // Array -index repräsentiert die number of Node und the value of result distance !

        while(! q.isEmpty()){
            int current = q.poll();
            Iterator<Integer> it = nodes[current].iterator();

            while (it.hasNext()){
                int elem = it.next();
                if(elem == end){
                    return dist[current] + 1;
                }
                if(! discovered[elem]){
                    discovered[elem] = true;
                    dist[elem] = dist[current] + 1;
                    q.add(elem);
                }
            }
        }
        return -1;
    }

    public List<Integer> criticalNodes() {  // hasCycle und das erste oder letzte ?!
        List<Integer> critical = new LinkedList<Integer>();
        List<Integer> neighbors;
        int firstComponents = numberOfComponents();
        int lastComponents = 0;

        if (! hasCycle()){
            for (int i = 0; i < this.number; i++) {
                if (nodes[i].size() > 1){  // bas ve son node a bakarsan size = 1'dir.
                    critical.add(i);
                }
            }
        }
        else {
            for (int i = 0; i < this.number ; i++) {
                if (nodes[i].size() > 1){
                    neighbors = new ArrayList<>(nodes[i]);
                    deleteAllConnections(i);
                    lastComponents = numberOfComponents();
                    if (lastComponents - firstComponents  >=  2){
                        critical.add(i);
                    }
                    for (int j = 0; j < neighbors.size() ; j++) {
                        addConnection(i, neighbors.get(j));
                    }
                }
            }
        }
        return critical;
    }
}



