import java.util.ArrayList;
import java.util.regex.Pattern;

public class Utils {


     // Checa se as operações da transação estão sintáticamente corretas.
    // r4(v)r3(y)r1(y)r1(x)w2(u)r2(x)w1(y)r2(y)c1
    public static boolean correctTransaction(String scheduler){     
      if(scheduler.isEmpty() || scheduler.contains(" ")){
        System.out.println("O escalonador está vazio ou contém espaços.");
        return false;
      }

      String newScheduler = scheduler.toLowerCase();  
      char[] arrayScheduler = newScheduler.toCharArray();    
      String regexScheduler;

      while(newScheduler.length() > 0){
         
        if((arrayScheduler[0] == 'w' || arrayScheduler[0] == 'r') && newScheduler.length() >= 5){ 
            
            regexScheduler = newScheduler.substring(0,5);

            if(!Pattern.matches("[rw][0-9]\\([a-z]\\)", regexScheduler)){           
                System.out.println("Operação Inválida.");  
                return false;
            }            
            newScheduler = newScheduler.substring(5);
            
          }
          else if(arrayScheduler[0] == 'c' && newScheduler.length() >= 2){
            regexScheduler = newScheduler.substring(0,2);

            if(!Pattern.matches("[c][0-9]", regexScheduler)){           
                System.out.println("Operação Inválida.");  
                return false;
            }      
            newScheduler = newScheduler.substring(2);
          }
          else if(arrayScheduler[0] == 'u' && newScheduler.length() >= 6){
            regexScheduler = newScheduler.substring(0,6);

            if(!Pattern.matches("[u][l][0-9]\\([a-z]\\)", regexScheduler)){           
                System.out.println("Operação Inválida.");  
                return false; 
          }
          newScheduler = newScheduler.substring(6);
        }
          else{
            System.out.println("Operação Inválida.");  
            return false;
          }      
          arrayScheduler = newScheduler.toCharArray();
      }
      return true;
    }

    //  Salva cada operação num array
    public static ArrayList<String> parseScheduler(String scheduler){
        
        char[] arrayCharScheduler = scheduler.toCharArray();  
        ArrayList<String> arrayListScheduler = new ArrayList<>();   

        while(scheduler.length() > 0){
         
            if((arrayCharScheduler[0] == 'w' || arrayCharScheduler[0] == 'r') ){                
                arrayListScheduler.add(scheduler.substring(0,5));                  
                scheduler = scheduler.substring(5);              
              }

              else if(arrayCharScheduler[0] == 'c'){
                arrayListScheduler.add(scheduler.substring(0,2));   
                scheduler = scheduler.substring(2);
              }   
              else{
                arrayListScheduler.add(scheduler.substring(0,6));                 
                scheduler = scheduler.substring(6);                     
              } 
              arrayCharScheduler = scheduler.toCharArray();
          }
          return arrayListScheduler;
    }
    
}
