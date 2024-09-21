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

    public String mostRecentTransaction( ArrayList<String> transactionsInCycle) throws InterruptedException{
      int linhas = this.sysLockTable.sysLockTable.size();
      int count = 0;
      String abortTransaction = "";
      ArrayList<String> aux;
      int tSize = transactionsInCycle.size();
      for(int i = 0; i < linhas ; i++){
         for(int j = 0 ; j < transactionsInCycle.size(); j++){               
             aux = this.sysLockTable.sysLockTable.get(i);
             
             if(count == tSize){
               break;
             }

             if(aux.get(4).equals("1") && aux.get(0).equals(transactionsInCycle.get(j))){
                abortTransaction = transactionsInCycle.get(j);  
                count ++; 
                transactionsInCycle.remove(j);
                j = 0;
             }
            }
      } 
      deadLocked(abortTransaction);   
      return abortTransaction;
  }

    public void deadLocked( String op) throws InterruptedException{
        int linhas = this.sysLockTable.sysLockTable.size();
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
        this.aGraph.removeEdge(Character.getNumericValue(charArray[1]));
        System.out.printf("Foi detectado um deadlock, removendo a transação mais recente %s...\n", transactionId); 
      }
}
