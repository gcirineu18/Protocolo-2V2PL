
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.String;


public class Main{

    public static void main(String[] args){
      System.out.println("Digite o escalonamento:");

      Scanner scanner = new Scanner(System.in);
      
      String scheduler = scanner.nextLine();

      while(!Utils.correctTransaction(scheduler)){
        System.out.println("O escalonador contém erros, por favor, digite novamente\n");
        scheduler = scanner.nextLine();
      }          
      scanner.close();

     ArrayList<String> arrayList =  Utils.parseScheduler(scheduler);
     System.out.print(arrayList);
     }
}