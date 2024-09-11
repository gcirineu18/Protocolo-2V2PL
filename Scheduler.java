import java.util.ArrayList;
class Scheduler{
  
  private SysLockTable sysLockTable ;
  ArrayList<String> operations;

  public Scheduler(ArrayList<String> operations){
    this.operations = operations;
    sysLockTable = new SysLockTable();
  }

  public void scheduleOperations(){
      int numberElements = this.operations.size();
      String operation;
      for(int i = 0; i < numberElements ; i++){
         operation = this.operations.get(i);
         grantLock(operation);
      }
  }

  public void grantLock(String operation){
    char[] arrayOperation = operation.toCharArray();   
    if(arrayOperation[0] =='r'){
       grantReadLock(operation);
    }
    else if(arrayOperation[0] =='w'){
       grantWriteLock(operation);  
    }
    else if(arrayOperation[0] =='c'){
      grantCertifyLock(operation);  
   }
   else{
      grantUpdateLock(operation);
   }
  }

  private boolean grantWriteLock(String wl){
      return true;
  }
  private boolean grantReadLock(String rl){
    return true;
  }
  private boolean grantUpdateLock(String ul){
    return true;
  }
  private boolean grantCertifyLock(String cl){
    return true;
  }

}