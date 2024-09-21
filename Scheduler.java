import java.util.ArrayList;


public class Scheduler extends SysLockTable{
  
  private SysLockTable sysLockTable;
  ArrayList<String> operations;
  private WaitforGraph aGraph;
  DeadLockDetection deadLockDetection;
  ArrayList<Character> abortedT = new ArrayList<>();
  boolean deadlocked;

  public Scheduler(ArrayList<String> operations){
    this.operations = operations;
    this.sysLockTable = new SysLockTable();
    this.aGraph = new WaitforGraph();
    deadLockDetection = new DeadLockDetection(this.sysLockTable, this.operations, this.aGraph);
    this.deadlocked = false;
  }

  public String scheduleOperations() throws InterruptedException{
      int numberElements = this.operations.size();
      String operation;
      String newScheduler = "";
      int i = 0;
      
      char transactionId;
      
      while( i < numberElements){
        operation = this.operations.get(i); 

        if(!this.abortedT.isEmpty()){
          for(int j = 0; j < this.abortedT.size(); j++){
            transactionId = Operation.getTransactionId(operation);
            if(abortedT.get(j).equals(transactionId)){ 
              break;
            }
            else{
              newScheduler = newScheduler.concat(tryToGrantLock(operation, false)); 
              newScheduler = newScheduler.concat(listenTableEvents());
            }
          }
        }
        else{
          newScheduler = newScheduler.concat(tryToGrantLock(operation, false)); 
          newScheduler = newScheduler.concat(listenTableEvents());    
          }   
      //   printTable(); 
         i++;   
    }
      newScheduler = newScheduler.concat(listenTableEvents());  
      String actualScheduler = "S = " + retrieveAbortedOperations(newScheduler);      
      return actualScheduler;
  }

  //  w2(u)ul4(x)r3(y)c1
  // Tenta conceder o bloqueio tendo em vista possíveis operações em conflito
  public String tryToGrantLock(String operation, boolean alreadyAdded) throws InterruptedException{
    char[] arrayOperation = operation.toCharArray();   
    boolean granted = false;
    char transactionNumber = '\0';

    if ((arrayOperation[0] == 'w' || arrayOperation[0] == 'r') && arrayOperation[1] != 'u'){
      transactionNumber = arrayOperation[1];
    }
    else{
      transactionNumber = arrayOperation[2];
    }
    
    char objectId;
    String certifyLock;
    String writeLock;
    
    if(arrayOperation[0] =='r'){

      objectId = arrayOperation[3];
      certifyLock = String.format("cl%c(%c)",transactionNumber,objectId);    
      granted = canScheduleOperation("rl",certifyLock);
      if(!alreadyAdded){
        if(granted){
          sysLockTable.addOperationToTable(operation, 1); 
        }
        else{
          sysLockTable.addOperationToTable(operation, 2); 
        }
      }
      else{
        if(granted){
          sysLockTable.changeStatusOnTable(operation, 1);; 
        }
      }      
    }
    else if(arrayOperation[0] =='w'){

      objectId = arrayOperation[3];
      certifyLock = String.format("cl%c(%c)",transactionNumber,objectId);
      writeLock = String.format("wl%c(%c)",transactionNumber,objectId);
      granted = canScheduleOperation("wl",writeLock, certifyLock);  
 
      if(!alreadyAdded){
        if(granted){
          sysLockTable.addOperationToTable(operation, 1); 
        }
        else{
          sysLockTable.addOperationToTable(operation, 2); 
        }
      }
      else{      
        if(granted){        
          sysLockTable.changeStatusOnTable(operation, 1);          
        }
      }
    }
    else if(arrayOperation[0] =='c'){
      
      String tID = "T" + transactionNumber; 
      ArrayList<String> objects = hasWriteOperation(tID);
      String readLock;
      boolean schedule = true;

      if(objects != null){
        
        for(int i = 0; i< objects.size(); i++){
          readLock = String.format("rl%c(%s)",transactionNumber,objects.get(i));          
          schedule = canScheduleOperation("c", readLock);
          if(!schedule){            
            if(!alreadyAdded){               
              sysLockTable.addOperationToTable(operation, 2);             
              break;
            }            
          }
        }
        if(!alreadyAdded){
           if(schedule){
            sysLockTable.addOperationToTable(operation, 1);      
            commitTransaction(tID);
           }  
        }
        else{
          if(schedule){
          sysLockTable.changeStatusOnTable(operation, 1);
          commitTransaction(tID);
          }
        }       
      }
      else{   
      String lock = String.format("--%c(-)",transactionNumber);    
      granted = canScheduleOperation("c",lock);
      if(!alreadyAdded){
        if(granted){
          sysLockTable.addOperationToTable(operation, 1); 
          commitTransaction(tID);
        }
        else{
          sysLockTable.addOperationToTable(operation, 2); 
        }
      }
      else{
        if(granted){
          sysLockTable.changeStatusOnTable(operation, 1);
          commitTransaction(tID);
        }
        else{
          sysLockTable.changeStatusOnTable(operation, 2); 
        } 
      }     
      }
      granted = schedule;  
   }
   else{
     
   }
   if(!granted && this.aGraph.hasCycle()){   
    this.deadlocked = true;     
    String toBeAborted = deadLockDetection.mostRecentTransaction(aGraph.transactionsInCicle());
    this.abortedT.add(Operation.getTransactionId(toBeAborted)); 
  }   
    if(granted){
      return operation;
    }
    return "";
  }

  // Retorna falso se, dado o parâmetro, 
  // a operação não poder ser escalonada - 
  // Padrão - rl5(u) (verifica se existe um read lock que não
  // seja da transação 5 e seja sobre o obj u) 
  // Verifica também se existe uma op anterior da mesma transação aguardando
  private boolean canScheduleOperation( String actualOperation, String... operations){
    int linhas = this.sysLockTable.sysLockTable.size() ;
   
    String tId;
    String objId;
    String blockType;
    ArrayList<String> table;
    String aux =  operations[0]; 
    char[] charArray = aux.toCharArray();
    String transactionId = "T" + charArray[2] ;
    
    if(!onlyCurrentTransaction(transactionId)){
      
    for(String operation : operations){   
    
      char[] arrayOperation = operation.toCharArray(); 
      blockType = operation.substring(0,2);
      
      tId = "T" + arrayOperation[2];
      objId = Character.toString(arrayOperation[4]);
      for(int i = 1; i < linhas ; i++){
        table = this.sysLockTable.sysLockTable.get(i); 
        
        if(table.get(0).equals(tId) && table.get(4).equals("2" ) && 
        !table.get(3).equals(actualOperation)){
              return false;
        }
        else if(!table.get(0).equals(tId) && table.get(1).equals(objId) && 
        table.get(3).equals(blockType) && table.get(4).equals("1")){
          
          int transactionNumberU = Character.getNumericValue(charArray[2]);
          char[] transactionvArray = table.get(0).toCharArray();
          int transactionNumberV = Character.getNumericValue(transactionvArray[1]);
          this.aGraph.addEdge(transactionNumberU, transactionNumberV);         
          return false;
        }              
      } 
    } 
  }   
    return true;
  }

  // Checa se a transação tem operação de escrita e retorna o(s) objeto(s) da operação caso haja(m)
  private ArrayList<String> hasWriteOperation(String transactionId ){
    int linhas = this.sysLockTable.sysLockTable.size() ;
    
    ArrayList<String> table;
    ArrayList<String> objects = new ArrayList<>();   
    
    for(int i = 1; i < linhas ; i++){
      table = this.sysLockTable.sysLockTable.get(i);         
      if((table.get(0).equals(transactionId) && table.get(3).equals("wl"))){             
            objects.add(table.get(1));            
        }    
    } 
    return objects.isEmpty() ? null : objects;
  }


  private void commitTransaction(String transactionId) throws InterruptedException{
    ArrayList<String> tupla;
    int linhas = this.sysLockTable.sysLockTable.size() ;
    String tId;
    
    Thread.sleep(1000);
    String transactionOperations = "";
    //printTable();
    for(int i = 0; i < linhas ; i++){
      tId = this.sysLockTable.sysLockTable.get(i).get(0);

     
      if(tId.equals(transactionId)){  
       tupla = this.sysLockTable.sysLockTable.get(i);
       transactionOperations = transactionOperations.concat(Operation.rebuildOperation(tupla));   
       this.sysLockTable.sysLockTable.remove(i);
       i = 0;
       linhas--;    
      }
    }
    System.out.printf("Transação %s foi commitada.\n", transactionId); 
    
  }
  
   // w2(u)ul4(x)r3(y)c1
  private String listenTableEvents() throws InterruptedException{
    int linhas = this.sysLockTable.sysLockTable.size();
    
    String op;
    String scheduledOperations = "";
    String aux = "";
    int i = 1;
    String status;
    while (i<linhas) {      
      aux = ""; 
      status = this.sysLockTable.sysLockTable.get(i).get(4);
      op = Operation.rebuildOperation(this.sysLockTable.sysLockTable.get(i));
      
      if(status.equals("2")){
        aux = aux.concat(tryToGrantLock(op, true));    
        scheduledOperations = scheduledOperations.concat(aux);
      }
 
      if(!aux.isEmpty()){
        i = 1;
    } else{
      i++;
    }  
      linhas = this.sysLockTable.sysLockTable.size();
    }
    
    return scheduledOperations;
  }


  // checa se há apenas uma transação escalonada no momento
  private boolean onlyCurrentTransaction(String tId){
    
    int linhas = this.sysLockTable.sysLockTable.size() ;
    ArrayList<String> auxId;
    for(int i = 1; i < linhas ; i++){
      auxId = this.sysLockTable.sysLockTable.get(i);
      
      if(!auxId.get(0).equals(tId)) return false;        
      }   
    return true;
  }

  public void printTable(){
    int linhas = this.sysLockTable.sysLockTable.size() ;
    int colunas = this.sysLockTable.sysLockTable.get(0).size();
    for(int i = 0; i < linhas ; i++){
      for(int j = 0; j < colunas; j++){
        System.out.printf(" |%s| ",this.sysLockTable.sysLockTable.get(i).get(j));
      }
      System.out.printf("\n");
    }
  }

  public String retrieveAbortedOperations(String newScheduler){
    int totalAborts = this.abortedT.size();
    ArrayList<String> newSchedulerList = Utils.parseScheduler(newScheduler); 
    newScheduler = ""; 
    for(int i = 0; i < totalAborts; i++){
      for(int j = 0; j < newSchedulerList.size(); j++){
        if(this.abortedT.get(i).equals(Operation.getTransactionId(newSchedulerList.get(j)))){
            newSchedulerList.remove(j);
            j = 0;
        }   
      }
    }
    newScheduler = String.join("", newSchedulerList);
    return newScheduler;
  }
}