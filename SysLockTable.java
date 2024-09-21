import java.util.ArrayList;
import java.util.Arrays;

public class SysLockTable {
  public ArrayList<ArrayList<String>> sysLockTable;

  protected SysLockTable() {
  this.sysLockTable = new ArrayList<>();
  this.sysLockTable.add(new ArrayList<>(Arrays.asList("tId", "objId", "objType", "blockType", "status")));  
}

// r4(v)w2(u)c1
  protected void addOperationToTable(String operation, int status){
    char[] arrayOperation = operation.toCharArray(); 
    String tId;
    String objId;

    if(arrayOperation[0] =='r' && arrayOperation[1] != 'u'){
      tId = "T" + arrayOperation[1];
      objId = Character.toString(arrayOperation[3]);
      this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "rl", Integer.toString(status))));
   }
   else if(arrayOperation[0] =='w'){
      tId = "T" + arrayOperation[1];
      objId = Character.toString(arrayOperation[3]);
      this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "wl", Integer.toString(status))));
   } 
   // Deve checar antes se existe operação de escrita na transação para convertê-la em cl, se não, vapo
   else if(arrayOperation[0] =='c'){
    int linhas = this.sysLockTable.size() ;
    ArrayList<String> aux;
    tId = "T" + arrayOperation[1];
          
    for(int i = 1; i < linhas ; i++){
      aux = this.sysLockTable.get(i);            
      if(aux.get(0).equals(tId) && status == 1 && aux.get(3).equals("wl")){             
            aux.set(3, "cl");
            aux.set(4, Integer.toString(status));
      }
      else{
            this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, "-", "row", "c", Integer.toString(status))));
            break;
      } 
    } 
  }
  else{
    tId = "T" + arrayOperation[2];
    objId = Character.toString(arrayOperation[3]);
    this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "rul", Integer.toString(status))));
  }  
  }
  
  // Muda o status de alguma operação após algum evento.
  // r4(v)w2(u)c1
  protected void changeStatusOnTable(String operation, int status){
    char[] arrayOperation = operation.toCharArray(); 
    String tId = "T" + arrayOperation[1];
    String blockType = Character.toString(arrayOperation[0]);
    int linhas = this.sysLockTable.size() ;
    ArrayList<String> aux;
          
    for(int i = 1; i < linhas; i++){
      aux = this.sysLockTable.get(i); 
      if(blockType.equals("c")){
        if(aux.get(0).equals(tId) && status == 1 && aux.get(3).equals("wl")){             
            aux.set(3, "cl");
            aux.set(4, Integer.toString(status));
        }
        else if(aux.get(3).equals("c") && aux.get(0).equals(tId) && status == 2 ){
          this.sysLockTable.remove(i);
        }   
      }  
      else{
  
        if(aux.get(0).equals(tId) && aux.get(4).equals("2") && aux.get(3).equals(String.format("%sl",blockType))){             
          aux.set(4, Integer.toString(status));
        }
      }        
    } 
  }
}
