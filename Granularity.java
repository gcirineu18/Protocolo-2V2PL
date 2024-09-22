import java.util.ArrayList;

public class Granularity extends SysLockTable{
    private SysLockTable sysLockTable;
    private WaitforGraph aGraph;
    ArrayList<String> operations;
    ArrayList<ArrayList<String>> BD; //onde haverá tabelas, páginas e tuplas

    public Granularity(SysLockTable sysLockTable,  WaitforGraph aGraph ){
       this.sysLockTable = sysLockTable;
       this.aGraph = aGraph;
    }

  // Retorna falso se, dado o parâmetro, 
  // a operação não poder ser escalonada - iwl2(x), icl3(y), iul4(w) 
  // Padrão - irl5(u) (verifica se existe um read intent lock que não
  // seja da transação 5 e seja sobre o obj u) 
  // Verifica também se existe uma op anterior da mesma transação aguardando
public boolean canScheduleOperationCheckingIntents(String actualOperation, String... operations){
    int linhas = this.sysLockTable.sysLockTable.size() ;

    String tId;
    String objId;
    String blockType;
    ArrayList<String> table;
    String aux =  operations[0]; 
    char[] charArray = aux.toCharArray();
    String transactionId = "T" + charArray[3] ;

    if(!onlyCurrentTransaction(transactionId)){

        for(String operation : operations){   
        
        char[] arrayOperation = operation.toCharArray(); 
        blockType = operation.substring(0,3);
        
        tId = "T" + arrayOperation[3];
        objId = Character.toString(arrayOperation[5]);

        for(int i = 1; i < linhas ; i++){
            table = this.sysLockTable.sysLockTable.get(i);  

            if(table.get(0).equals(tId) && table.get(4).equals("2" ) && 
            !table.get(3).equals(actualOperation)){
              return false;
            }        
        
            else if(!table.get(0).equals(tId) && table.get(1).equals(objId) && 
                       table.get(3).equals(blockType) && table.get(4).equals("1")){ 

            int transactionNumberU = Character.getNumericValue(charArray[3]);
            char[] transactionvArray = table.get(0).toCharArray();
            int transactionNumberV = Character.getNumericValue(transactionvArray[1]);
            this.aGraph.addEdge(transactionNumberU, transactionNumberV);  
            
             System.out.printf("Adicionando aresta %d e %d em granulosity\n",transactionNumberU, transactionNumberV);         
            return false;
            }              
        } 
        } 
    }   
    return true;
}   


private boolean onlyCurrentTransaction(String tId){
    
    int linhas = this.sysLockTable.sysLockTable.size() ;
    ArrayList<String> auxId;
    for(int i = 1; i < linhas ; i++){
      auxId = this.sysLockTable.sysLockTable.get(i);
      
      if(!auxId.get(0).equals(tId)) return false;        
      }   
    return true;
  }

}