
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.String;


public class Main{

    public static void main(String[] args) throws InterruptedException{
      System.out.println("Digite o escalonamento:");

      Scanner scanner = new Scanner(System.in);
      
      String schedulerLine = scanner.nextLine();

      while(!Utils.correctTransaction(schedulerLine)){
        System.err.println("O escalonador contém erros, por favor, digite novamente\n");
        schedulerLine = scanner.nextLine();
      }          
      scanner.close();

     ArrayList<String> arrayList =  Utils.parseScheduler(schedulerLine);
     Scheduler scheduler = new Scheduler(arrayList);
     System.out.println(scheduler.scheduleOperations());
     
    // scheduler.printTable();
   
    }
     
}