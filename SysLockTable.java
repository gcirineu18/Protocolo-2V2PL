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
   else if(arrayOperation[0] =='c'){
    tId = "T" + arrayOperation[1];
    objId = "-";
    this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "t", "cl", Integer.toString(status))));

  }
  else{
     
  }  
  }


}
