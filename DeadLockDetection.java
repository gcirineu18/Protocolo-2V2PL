import java.util.ArrayList;

public class DeadLockDetection{
    private SysLockTable sysLockTable;
    ArrayList<String> operations;
    private WaitforGraph aGraph;
    

    public DeadLockDetection(SysLockTable sysLockTable, ArrayList<String> operations,WaitforGraph aGraph){
            this.sysLockTable = sysLockTable;
            this.aGraph = aGraph;
            this.operations = operations;         
    }

    public void deadLocked(String op) throws InterruptedException{
        int linhas = this.sysLockTable.sysLockTable.size() ;
        char[] charArray = op.toCharArray();
        
        String transactionId = "T" + charArray[1];
        String tId;
        Thread.sleep(1000);
    
        // Removendo as tuplas da operações da Transação abortada
        for(int i = 0; i < linhas ; i++){
          tId = this.sysLockTable.sysLockTable.get(i).get(0);
          
          if(tId.equals(transactionId)){      
           this.sysLockTable.sysLockTable.remove(i);
           i = 0;
           linhas--;    
          }
        }

        // Removendo as operações da Transação abortada do escalonamento de entrada
        char opNumber= Operation.getTransactionId(op);
        char operationsNumbers;
        for(int j = 0; j < operations.size(); j++){
             operationsNumbers = Operation.getTransactionId(operations.get(j));
             
             if(opNumber == operationsNumbers){              
              operations.remove(j);
              j = 0;
             }
        }          
        this.aGraph.removeEdge(Character.getNumericValue(charArray[1]));
        System.out.printf("Foi detectado um deadlock, removendo a transação mais recente %s...\n", transactionId); 
      }
}
