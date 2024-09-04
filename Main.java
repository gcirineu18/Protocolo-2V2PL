
import java.util.Scanner;

public class Main{

    public static void main(String[] args){
      System.out.println("Digite o escalonamento:");

      Scanner scanner = new Scanner(System.in);
      
      String scheduler = scanner.next();

      while(!correctTransaction(scheduler)){
        System.out.println("O escalonamento contém erros, por favor, digite novamente\n");
        scheduler = scanner.next();
      }
           
      scanner.close();

    }

    // checks whether the operations of the transactions are syntactically correct
    private static boolean correctTransaction(String scheduler){
      
      return true;
    }
}