import java.util.ArrayList;
import java.util.Arrays;

public class SysLockTable {
  public ArrayList<ArrayList<String>> sysLockTable;
  private String operation;

  public SysLockTable() {
  this.sysLockTable = new ArrayList<>();
  this.sysLockTable.add(new ArrayList<>(Arrays.asList("tId", "objId", "objType", "blockType", "status")));  
}

// r4(v)w2(u)c1
  public void addOperationToTable(String operation, int status){
    char[] arrayOperation = operation.toCharArray(); 
    String tId;
    String objId;

    if(arrayOperation[0] =='r'){
      tId = "T" + arrayOperation[1];
      objId = Character.toString(arrayOperation[3]);
      this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "t", "rl", Integer.toString(status))));
   }
   else if(arrayOperation[0] =='w'){
      tId = "T" + arrayOperation[1];
      objId = Character.toString(arrayOperation[3]);
      this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "t", "wl", Integer.toString(status))));

   } 
   // Deve checar antes se existe operação de escrita na transação para convertê-la em cl, se não, vapo
   else if(arrayOperation[0] =='c'){

    int linhas = this.sysLockTable.size() ;
    int colunas = this.sysLockTable.get(0).size();
    ArrayList<String> aux;
    tId = "T" + arrayOperation[1];
          
    for(int i = 1; i < linhas ; i++){
      aux = this.sysLockTable.get(i);
      for(int j = 0; j < colunas; j++){         
        if((aux.get(0).equals(tId) && aux.get(3).equals("wl"))){             
            aux.set(3, "cl");
            aux.set(4, Integer.toString(status));
        }
      }   
    } 
  }
  else{
    tId = "T" + arrayOperation[1];
    objId = Character.toString(arrayOperation[3]);
    this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "t", "ul", Integer.toString(status))));
  }  
  }


}
