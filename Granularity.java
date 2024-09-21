import java.util.ArrayList;

public class Granularity extends SysLockTable{
    private SysLockTable sysLockTable;
    private WaitforGraph aGraph;
    ArrayList<String> operations;
    ArrayList<ArrayList<String>> BD; //onde haverá tabelas, páginas e tuplas

    public Granularity(ArrayList<String> operation){
        this.operations = operation;
        this.sysLockTable = new SysLockTable();
        this.aGraph = new WaitforGraph();
        this.BD = new ArrayList<ArrayList<String>>();
        DeadLockDetection deadLockDetection = new DeadLockDetection(this.sysLockTable, this.operations, this.aGraph);
    }

    public void schedulerGranularity(String operation, boolean alreadyAdded){
        boolean granted = false;
        char[]arrayOperation = operation.toCharArray();
        char transactionNumber = arrayOperation[1];
        char objectId;
        String certifyLock;
        String writeLock;

        if(arrayOperation[0] == 'r'){
            objectId = arrayOperation[3];
            certifyLock = String.format("cl%c(%c)",transactionNumber,objectId);    
            granted = canScheduleOperation("rl",certifyLock);
            if(!alreadyAdded){
                if(granted){
                    sysLockTable.addOperationToTable(operation, 1);
                }
                else{
                    sysLockTable.addOperationToTable(operation, 2);
                }
            }
            else{
                if(granted){
                    sysLockTable.addOperationToTable(operation, 1);
                }
            }
        }

        if(arrayOperation[0] == 'w'){
            objectId = arrayOperation[3];
            certifyLock = String.format("cl%c(%c)",transactionNumber,objectId);
            writeLock = String.format("wl%c(%c)",transactionNumber,objectId);    
            granted = canScheduleOperation("wl", writeLock, certifyLock);
            
            if(!alreadyAdded){
                if(granted){
                    sysLockTable.addOperationToTable(operation, 1);
                }
                else{
                    sysLockTable.addOperationToTable(operation, 2);
                }
            }
            else{
                if(granted){
                    sysLockTable.addOperationToTable(operation, 1);
                }
            }
        }

        // if(arrayOperation[0] == 'c'){
        //     objectId = arrayOperation[3];
        //     certifyLock = String.format("cl%c(%c)",transactionNumber,objectId);    
        //     granted = canScheduleOperation("cl",certifyLock);
        //     if(!alreadyAdded){
        //         if(granted){
        //             sysLockTable.addOperationToTable(operation, 1);
        //         }
        //         else{
        //             sysLockTable.addOperationToTable(operation, 2);
        //         }
        //     }
        //     else{
        //         if(granted){
        //             sysLockTable.addOperationToTable(operation, 1);
        //         }
        //     }
        // }
    }
}