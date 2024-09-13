import java.util.ArrayList;
class Scheduler extends SysLockTable{
  
  private SysLockTable sysLockTable;
  ArrayList<String> operations;

  public Scheduler(ArrayList<String> operations){
    this.operations = operations;
    this.sysLockTable = new SysLockTable();
  }

  public String scheduleOperations(){
      int numberElements = this.operations.size();
      String operation;
      String newScheduler = "S = ";
      
      for(int i = 0; i < numberElements ; i++){
         operation = this.operations.get(i);
         newScheduler = newScheduler.concat(tryToGrantLock(operation));   
      }
      return newScheduler;
  }

  //  w2(u)ul4(x)r3(y)c1
  // Tenta conceder o bloqueio tendo em vista possíveis operações em conflito
  public String tryToGrantLock(String operation){
    char[] arrayOperation = operation.toCharArray();   
    boolean granted = false;
    char transactionNumber = arrayOperation[1];
    char objectId;
    String certifyLock;
    String writeLock;
    
    if(arrayOperation[0] =='r'){

      objectId = arrayOperation[3];
      certifyLock = String.format("cl%c(%c)",transactionNumber,objectId);    
      granted = canScheduleOperation(certifyLock);

      if(granted){
        sysLockTable.addOperationToTable(operation, 1); 
      }
      else{
        sysLockTable.addOperationToTable(operation, 2); 
      }
    }
    else if(arrayOperation[0] =='w'){

      objectId = arrayOperation[3];
      certifyLock = String.format("cl%c(%c)",transactionNumber,objectId);
      writeLock = String.format("wl%c(%c)",transactionNumber,objectId);
      granted = canScheduleOperation(writeLock, certifyLock);  

      if(granted){
        sysLockTable.addOperationToTable(operation, 1); 
      }
      else{
        sysLockTable.addOperationToTable(operation, 2); 
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
          schedule = canScheduleOperation(readLock);
          if(!schedule){      
            sysLockTable.addOperationToTable(operation, 2);
            break;
          }
        }
        if(schedule){
          sysLockTable.addOperationToTable(operation, 1);
          commitTransaction(tID);
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
  private boolean canScheduleOperation(String... operations){
    int linhas = this.sysLockTable.sysLockTable.size() ;
   
    String tId;
    String objId;
    String blockType;
    ArrayList<String> table;

    for(String operation : operations){   
     
      char[] arrayOperation = operation.toCharArray(); 
      blockType = operation.substring(0,2);
      
      tId = "T" + arrayOperation[2];
      objId = Character.toString(arrayOperation[4]);
      
      for(int i = 1; i < linhas ; i++){
        table = this.sysLockTable.sysLockTable.get(i); 

        if((table.get(0).equals(tId) && table.get(4).equals("2" ))||
        (!table.get(0).equals(tId) && table.get(1).equals(objId) && table.get(3).equals(blockType))){
            return false;
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


  private void commitTransaction(String transactionId){
 
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