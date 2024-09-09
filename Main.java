
import java.util.Scanner;
import java.lang.String;
import java.util.regex.*;

public class Main{

    public static void main(String[] args){
      System.out.println("Digite o escalonamento:");

      Scanner scanner = new Scanner(System.in);
      
      String scheduler = scanner.nextLine();

      while(!correctTransaction(scheduler)){
        System.out.println("O escalonador contém erros, por favor, digite novamente\n");
        scheduler = scanner.nextLine();
      }
           
      scanner.close();
     }
 
    // Checa se as operações da transação estão sintáticamente corretas.
    // r4(v)r3(y)r1(y)r1(x)w2(u)r2(x)w1(y)r2(y)c1
    private static boolean correctTransaction(String scheduler){
         
      if( scheduler.isEmpty() || scheduler.contains(" ")){
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
          else{
            System.out.println("Operação Inválida.");  
            return false;
          }      
          arrayScheduler = newScheduler.toCharArray();
      }
      return true;
    }
}