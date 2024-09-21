
import java.util.ArrayList;
public class Operation {
    String operation;

    public Operation(String operation){
        this.operation = operation;
    }

    //  w2(u)u4(x)r3(y)c1
    public static char getTransactionId(String operation){
    char[] arrayOperation = operation.toCharArray();   
    char transactionNumber = arrayOperation[1];
    return transactionNumber;
    }

    public static String rebuildOperation(ArrayList<String> line){
        
        String tId;
        String objId;
        String blockType;
        String op = "";
        
        blockType = line.get(3);
        tId = line.get(0);
        
        if(!blockType.equals("c")){  
          tId = tId.substring(1);
          objId = line.get(1); 
          

          if(blockType.equals("cl")){
              op = String.format("w%s(%s)",tId,objId); 
              
           }
          else{
            blockType = blockType.substring(0,1);             
            op = String.format("%s%s(%s)",blockType,tId,objId);    
            }
                               
          }
    
          else if(blockType.equals("c")){      
            tId = tId.substring(1);
            objId = line.get(1); 
            blockType = blockType.substring(0); 
            op = String.format("%s%s",blockType,tId);                
          } 
          
        return op;
    }
}
