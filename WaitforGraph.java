
import java.util.ArrayList;

public class WaitforGraph {
    
    ArrayList<ArrayList<Integer>> arestas = new ArrayList<>();

    public void addEdge(ArrayList<ArrayList<Integer>> arestas,int u, int v) {
        while (arestas.size() <= u || arestas.size() <= v) {
            arestas.add(new ArrayList<>());
        }
        arestas.get(u).add(v);
    }
    
    public void removeEdge(int u){
        arestas.get(u).clear();
        for(int i = 0; i<arestas.size(); i++){
            for(int j = 0; j<arestas.get(i).size(); j++){
                if(arestas.get(i).get(j) == u){
                    arestas.get(i).remove(j);
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

        for (int vizinhos : arestas.get(v)) {
            if (DFS(vizinhos, visited, recStack)) {
                return true;  // Se ciclo for encontrado, retorna true
            }
        }

        recStack[v] = false;
        return false;
    }

    // Função principal que faz DFS para todos os vértices e detecta ciclo
    public boolean hasCycle() {
        int numVertices = arestas.size();
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
    
}
