import java.util.ArrayList;
class Scheduler{
  
  
  ArrayList<String> operations;

  public Scheduler(ArrayList<String> operations){
    this.operations = operations;
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

  public boolean grantWriteLock(String wl){
    
    return true;
  }

  public boolean grantReadLock(String rl){
    return true;
  }
  public boolean grantUpdateLock(String ul){
    return true;
  }
  public boolean grantCertifyLock(String cl){
    return true;
  }

}