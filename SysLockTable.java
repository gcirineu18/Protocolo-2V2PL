public class SysLockTable {
  String[][] SysLock;

  public SysLockTable() {
  SysLock = new String[1][5];
  SysLock[0] = new String[]{"tId", "objId", "objType", "blockType", "status"};
  }
}
