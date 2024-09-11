import java.util.ArrayList;
import java.util.Arrays;

public class SysLockTable {
  ArrayList<ArrayList<String>> sysLockTable;
  private String operation;

  public SysLockTable() {
  this.sysLockTable = new ArrayList<>();
  this.sysLockTable.add(new ArrayList<>(Arrays.asList("tId", "objId", "objType", "blockType", "status")));  
}


  public void addOperationToTable(String operation){
    
  }


}
