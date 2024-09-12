import java.util.ArrayList;
class Scheduler{
  
  private SysLockTable sysLockTable;
  ArrayList<String> operations;

  public Scheduler(ArrayList<String> operations){
    this.operations = operations;
    this.sysLockTable = new SysLockTable();
  }

  public void scheduleOperations(){
      int numberElements = this.operations.size();
      String operation;
      for(int i = 0; i < numberElements ; i++){
         operation = this.operations.get(i);
         grantLock(operation);   
      }
  }

  public String grantLock(String operation){
    char[] arrayOperation = operation.toCharArray();   
    boolean granted;
    if(arrayOperation[0] =='r'){
      granted = grantReadLock(operation);
    }
    else if(arrayOperation[0] =='w'){
      granted = grantWriteLock(operation);  
    }
    else if(arrayOperation[0] =='c'){
     granted = grantCertifyLock(operation);  
   }
   else{
     granted = grantUpdateLock(operation);
   }
    if(granted){
      return operation;
    }
    return "";
  }

  private boolean grantReadLock(String rl){
    return true;
  }
  private boolean grantWriteLock(String wl){
      return true;
  }
 
  private boolean grantUpdateLock(String ul){
    return true;
  }
  private boolean grantCertifyLock(String cl){
    return true;
  }


  // public void scheduleOperations(){
  //   int numberElements = this.operations.size();
  //   String operation;
  //   for(int i = 0; i < numberElements ; i++){
  //      operation = this.operations.get(i);
  //      //grantLock(operation);
  //      this.sysLockTable.addOperationToTable(operation, 1);
  //   }
  //   int linhas = this.sysLockTable.sysLockTable.size() ;
  //   int colunas = this.sysLockTable.sysLockTable.get(0).size();
  //   for(int i = 0; i < linhas ; i++){
  //     for(int j = 0; j < colunas; j++){
  //       System.out.printf(" |%s| ",this.sysLockTable.sysLockTable.get(i).get(j));
  //     }
  //     System.out.printf("\n");
  //   }   

}