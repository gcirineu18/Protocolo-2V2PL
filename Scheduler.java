import java.util.ArrayList;


public class Scheduler extends SysLockTable{
  
  private SysLockTable sysLockTable;
  ArrayList<String> operations;

  public Scheduler(ArrayList<String> operations){
    this.operations = operations;
    this.sysLockTable = new SysLockTable();
  }

  public String scheduleOperations() throws InterruptedException{
      int numberElements = this.operations.size();
      String operation;
      String newScheduler = "S = ";
      
      for(int i = 0; i < numberElements ; i++){
         operation = this.operations.get(i);
         newScheduler = newScheduler.concat(tryToGrantLock(operation, false));  
         listenTableEvents(); 
         //printTable();
         
      }
      return newScheduler;
  }

  //  w2(u)ul4(x)r3(y)c1
  // Tenta conceder o bloqueio tendo em vista possíveis operações em conflito
  public String tryToGrantLock(String operation, boolean alreadyAdded) throws InterruptedException{
    char[] arrayOperation = operation.toCharArray();   
    boolean granted = false;
    char transactionNumber = arrayOperation[1];
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
        else{
          sysLockTable.changeStatusOnTable(operation, 2); 
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
          sysLockTable.changeStatusOnTable(operation, 1);; 
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
       granted = schedule;  
      }
   }
   else{
     
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
        
        if((table.get(0).equals(tId) && table.get(4).equals("2" ) && !table.get(3).equals(actualOperation))||
            (!table.get(0).equals(tId) && table.get(1).equals(objId) && 
            table.get(3).equals(blockType) && table.get(4).equals("1"))){
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
    int linhas = this.sysLockTable.sysLockTable.size() ;
    String tId;
    
    Thread.sleep(1000);

    for(int i = 0; i < linhas ; i++){
      tId = this.sysLockTable.sysLockTable.get(i).get(0);
      
      if(tId.equals(transactionId)){      
       this.sysLockTable.sysLockTable.remove(i);
       i = 0;
       linhas--;    
      }
    }
    System.out.printf("Transação %s foi commitada.\n", transactionId); 
  }

  //  w2(u)ul4(x)r3(y)c1
  private void listenTableEvents() throws InterruptedException{
    int linhas = this.sysLockTable.sysLockTable.size() ;
    String status;
    String tId;
    String objId;
    String blockType; 
    String op;
    for(int i = 0; i < linhas ; i++){
      
      status = this.sysLockTable.sysLockTable.get(i).get(4);
      blockType = this.sysLockTable.sysLockTable.get(i).get(3);
      tId = this.sysLockTable.sysLockTable.get(i).get(0);
      if(status.equals("2") && !blockType.equals("c")){
    
        tId = tId.substring(1);
        objId = this.sysLockTable.sysLockTable.get(i).get(1); 
        blockType =blockType.substring(0,1); 
        op= String.format("%s%s(%s)",blockType,tId,objId);
        
        tryToGrantLock(op, true);
        
      }
      else if(status.equals("2") && blockType.equals("c")){
        tId = tId.substring(1);
        objId = this.sysLockTable.sysLockTable.get(i).get(1); 
        blockType =blockType.substring(0); 
        op= String.format("%s%s",blockType,tId);
        tryToGrantLock(op, true); 
      }
    }
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
}