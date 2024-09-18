
import java.util.ArrayList;

public class WaitforGraph {
    
    ArrayList<ArrayList<Integer>> arestas;
    public WaitforGraph(){
     this.arestas = new ArrayList<>();
    }
    
    

    public void addEdge(int u, int v) {
        while (this.arestas.size() <= u || this.arestas.size() <= v) {
            this.arestas.add(new ArrayList<>());
        }
        ArrayList<Integer> line = this.arestas.get(u);
        boolean alreadyAdded = false;

        for(int i= 0; i< line.size(); i++ ){
            if( line.get(i) ==  v){
                alreadyAdded = true;
            }
        }      
        if(!alreadyAdded) this.arestas.get(u).add(v);
        // printGraph();
        // System.out.printf("\n");
    }
    
    public void removeEdge(int u){
        
        this.arestas.get(u).clear();
        for(int i = 0; i<this.arestas.size(); i++){
            for(int j = 0; j<this.arestas.get(i).size(); j++){
                if(this.arestas.get(i).get(j) == u){
                    this.arestas.get(i).remove(j);
                }
            }        
        }
    }

    private boolean DFS(int v, boolean[] visited, boolean[] recStack) {
        if (recStack[v]) {
            return true;  // Ciclo detectado
        }
        if (visited[v]) {
            return false;  // Já foi visitado, sem ciclo
        }
        visited[v] = true;
        recStack[v] = true;

        for (int vizinhos : this.arestas.get(v)) {
            if (DFS(vizinhos, visited, recStack)) {
                return true;  // Se ciclo for encontrado, retorna true
            }
        }

        recStack[v] = false;
        return false;
    }

    // Função principal que faz DFS para todos os vértices e detecta ciclo
    public boolean hasCycle() {
        int numVertices = this.arestas.size();
        boolean[] visited = new boolean[numVertices];   // Para marcar vértices visitados
        boolean[] recStack = new boolean[numVertices];  // Para marcar vértices no caminho da recursão

        // Chama DFS para cada vértice
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i] && DFS(i, visited, recStack)) {
                return true;  // Se um ciclo for encontrado, retorna true
            }
        }
        return false;  // Se nenhum ciclo for encontrado
    }

    public void printGraph(){
        for (int i = 0; i < this.arestas.size(); i++) {
            System.out.print("Adjacency list of vertex " + i + ": ");
            for (int j = 0; j < this.arestas.get(i).size(); j++) {
                System.out.print(this.arestas.get(i).get(j) + " ");
            }
            System.out.printf("\n");
        }
    }
    
}
