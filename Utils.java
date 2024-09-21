import java.util.ArrayList;
import java.util.regex.Pattern;

public class Utils {


     // Checa se as operações da transação estão sintáticamente corretas.
    // r4(v)r3(y)r1(y)r1(x)w2(u)r2(x)w1(y)r2(y)c1   
    // r4(v)r3(y)r1(y)r1(x)w2(u)r2(x)w1(y)r2(y)c1w4(u)r3(x)c4w2(x)c2w3(u)w3(z)c3
    // r2(v)r1(x)w2(x)r3(v)r1(y)w3(y)r2(z)w3(z)c3c1c2
    // r1(a)r2(b)w1(a)r3(c)r4(d)r2(a)w3(b)r1(b)w4(c)r2(c)c1w2(d)w3(e)c2c3c4
    // r3(u)r2(v)w2(u)r3(w)r1(v)w3(x)r4(y)r2(w)w3(u)c2c3c4c1
    // r2(u)r3(x)r1(x)r1(p)r2(x)w3(u)w2(z)c2w1(x)c1r3(v)r3(p)c3
    public static boolean correctTransaction(String scheduler){     
      if(scheduler.isEmpty() || scheduler.contains(" ")){
        System.out.println("O escalonador está vazio ou contém espaços.");
        return false;
      }

      String newScheduler = scheduler.toLowerCase();  
      
      char[] arrayScheduler = newScheduler.toCharArray();    
      String regexScheduler;

      while(newScheduler.length() > 0){
         
        if((arrayScheduler[0] == 'w' || arrayScheduler[0] == 'r' || arrayScheduler[0] == 'u') && newScheduler.length() >= 5){ 
            
            regexScheduler = newScheduler.substring(0,5);

            if(!Pattern.matches("[rwu][0-9]\\([a-z]\\)", regexScheduler)){           
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
       scheduler = scheduler.toLowerCase();
        char[] arrayCharScheduler = scheduler.toCharArray();  
        ArrayList<String> arrayListScheduler = new ArrayList<>();   

        while(scheduler.length() > 0){
         
            if((arrayCharScheduler[0] == 'w' || arrayCharScheduler[0] == 'r' || arrayCharScheduler[0] == 'u') ){                
                arrayListScheduler.add(scheduler.substring(0,5));                  
                scheduler = scheduler.substring(5);              
              }

              else if(arrayCharScheduler[0] == 'c'){
                arrayListScheduler.add(scheduler.substring(0,2));   
                scheduler = scheduler.substring(2);
              }   
              // else{
              //   arrayListScheduler.add(scheduler.substring(0,6));                 
              //   scheduler = scheduler.substring(6);                     
              // } 
              arrayCharScheduler = scheduler.toCharArray();
          }
          System.out.println(arrayListScheduler);
          return arrayListScheduler;
    }   
}
