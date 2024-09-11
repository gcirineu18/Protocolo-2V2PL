import java.util.ArrayList;
import java.util.Arrays;

public class SysLockTable {
  ArrayList<ArrayList<String>> sysLockTable ;

  public SysLockTable(String operation) {
  sysLockTable = new ArrayList<>();
  sysLockTable.add(new ArrayList<>(Arrays.asList("tId", "objId", "objType", "blockType", "status")));
  
}
}
