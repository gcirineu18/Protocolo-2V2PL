import java.util.ArrayList;
import java.util.Arrays;

public class SysLockTable {
  public ArrayList<ArrayList<String>> sysLockTable;

  protected SysLockTable() {
  this.sysLockTable = new ArrayList<>();
  this.sysLockTable.add(new ArrayList<>(Arrays.asList("tId", "objId", "objType", "blockType", "status")));  
}

// r4(v)w2(u)c1
// adicionar os bloqueios intencionais apenas se o status for 1 e se não
// houver nenhum outro intencional do mesmo tipo criado
//   protected void addOperationToTable(String operation, int status){
//     char[] arrayOperation = operation.toCharArray(); 
//     String tId;
//     String objId;

//     if(arrayOperation[0] =='r'){
//       tId = "T" + arrayOperation[1];
//       objId = Character.toString(arrayOperation[3]);
//       this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "rl", Integer.toString(status))));
//    }
//    else if(arrayOperation[0] =='w'){
//       tId = "T" + arrayOperation[1];
//       objId = Character.toString(arrayOperation[3]);
//       this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "wl", Integer.toString(status))));
//    } 
//    else if(arrayOperation[0] =='u'){
//     tId = "T" + arrayOperation[1];
//     objId = Character.toString(arrayOperation[3]);
//     this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "ul", Integer.toString(status))));
//  } 
//    // Deve checar antes se existe operação de escrita na transação para convertê-la em cl, se não, vapo
//    else if(arrayOperation[0] =='c'){
//     int linhas = this.sysLockTable.size() ;
//     ArrayList<String> lineSysLock;
//     tId = "T" + arrayOperation[1];
          
//     for(int i = 1; i < linhas ; i++){
//       lineSysLock = this.sysLockTable.get(i);            
//       if(lineSysLock.get(0).equals(tId) && status == 1 && lineSysLock.get(3).equals("wl")){             
//             lineSysLock.set(3, "cl");
//             lineSysLock.set(4, Integer.toString(status));
//       }
//       else{
//             this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, "-", "row", "c", Integer.toString(status))));
//             break;
//       } 
//     } 
//   } 
//   }

  protected void addOperationToTable(String operation, int status){
    char[] arrayOperation = operation.toCharArray(); 
    String tId = "T" + arrayOperation[1];
    String objId;
    boolean certifyLocked = false;
    String whatIntentToAdd = "";

    if(arrayOperation[0] =='r'){
      whatIntentToAdd = "irl";     
      objId = Character.toString(arrayOperation[3]);
      this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "rl", Integer.toString(status))));
   }
   else if(arrayOperation[0] =='w'){
      whatIntentToAdd = "iwl";
      
      objId = Character.toString(arrayOperation[3]);
      this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "wl", Integer.toString(status))));
   } 
   else if(arrayOperation[0] =='u'){
    whatIntentToAdd = "iul";
    
    objId = Character.toString(arrayOperation[3]);
    this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "ul", Integer.toString(status))));
 } 
   // Deve checar antes se existe operação de escrita na transação para convertê-la em cl, se não, vapo
   else if(arrayOperation[0] =='c'){
    int linhas = this.sysLockTable.size();
    ArrayList<String> lineSysLock;
       
    for(int i = 1; i < linhas ; i++){
      lineSysLock = this.sysLockTable.get(i);            
      if(lineSysLock.get(0).equals(tId) && status == 1 && lineSysLock.get(3).equals("wl")){
            whatIntentToAdd = "icl";             
            lineSysLock.set(3, "cl");
            lineSysLock.set(4, Integer.toString(status));
            certifyLocked = true;
      }
      else{
            this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, "-", "row", "c", Integer.toString(status))));
            break;
      } 
    } 
  } 
 
 // adicionar os bloqueios intencionais apenas se o status for 1 e se não
 // houver nenhum outro intencional do mesmo tipo criado
  int lines = this.sysLockTable.size();
  ArrayList<String> line;
  boolean addIntent = true;
  
  for(int j = 1; j < lines; j++){
    line = sysLockTable.get(j);
    if(arrayOperation[0] =='r'){
      if(status == 1 && line.get(3).equals("irl") && (line.get(2).equals("table") || line.get(2).equals("page"))){
         addIntent = false;
         break;
      }
    } 
    else if(arrayOperation[0] =='w'){
      if(status == 1 && line.get(3).equals("iwl") && (line.get(2).equals("table") || line.get(2).equals("page"))){
         addIntent = false;
         break;
      } 
    } 
    else if(arrayOperation[0] =='u'){
      if(status == 1 && line.get(3).equals("iul") && (line.get(2).equals("table") || line.get(2).equals("page"))){
        addIntent = false;
        break;
      }
    }    
    else if(arrayOperation[0] =='c' && certifyLocked){
       if(status == 1 && line.get(3).equals("icl") && (line.get(2).equals("table") || line.get(2).equals("page"))){
         addIntent = false;
         break;
      } 
    }
  }
  // this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, objId, "row", "ul", Integer.toString(status))));
  if(addIntent && status == 1){
    this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, "P", "page", whatIntentToAdd, Integer.toString(status)))); 
    this.sysLockTable.add(new ArrayList<>(Arrays.asList(tId, "T", "table", whatIntentToAdd, Integer.toString(status)))); 
  }
  
}
  
  // Muda o status de alguma operação após algum evento.
  // r4(v)w2(u)c1
  protected void changeStatusOnTable(String operation, int status){
    char[] arrayOperation = operation.toCharArray(); 
    String tId = "T" + arrayOperation[1];
    String blockType = Character.toString(arrayOperation[0]);
    int linhas = this.sysLockTable.size() ;
    ArrayList<String> lineSysLock;
          
    for(int i = 1; i < linhas; i++){
      
      lineSysLock = this.sysLockTable.get(i); 
       
      if(blockType.equals("c")){
        if(lineSysLock.get(0).equals(tId) && status == 1 && lineSysLock.get(3).equals("wl")){             
            lineSysLock.set(3, "cl");
            lineSysLock.set(4, Integer.toString(status));
        }
        else if(lineSysLock.get(3).equals("c") && lineSysLock.get(0).equals(tId) && lineSysLock.get(4).equals("2")){
          lineSysLock.set(4, Integer.toString(status));
        }   
      }  
      else{
        if(lineSysLock.get(0).equals(tId) && lineSysLock.get(4).equals("2") && lineSysLock.get(3).equals(String.format("%sl",blockType))){             
          lineSysLock.set(4, Integer.toString(status));
        }
      }        
    } 
  }
}
