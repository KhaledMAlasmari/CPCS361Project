
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author khaled
 */
public class MainMemory {

    private int memorySize;
    private ArrayList<MemoryPartition> memoryPartitions;

    public MainMemory(int memorySize) {
        this.memorySize = memorySize;
        memoryPartitions = new ArrayList<>();
    }

    public void addProcess(Process process) {
        String strategy = process.getStrategy();
        if (strategy.equals("f")) {
            addProcessUsingFirstFit(process);
        } else if (strategy.equals("b")) {
            addProcessUsingBestFit(process);
        } else if (strategy.equals("w")) {
            addProcessUsinWorstFit(process);
        }
    }

    public void removeProcess(String processName) {
        int length = memoryPartitions.size();
        for (int i = 0; i < length; i++) {
            MemoryPartition partition = memoryPartitions.get(i);
            if (partition.isUsed() && processName.equalsIgnoreCase(partition.getProcessName())) {
                partition.removeProcess();
                return;
            }
        }
    }

    public void stats() {
        int numOfPartitions = memoryPartitions.size();
        for (int i = 0; i < numOfPartitions; i++) {
            MemoryPartition memP = memoryPartitions.get(i);
            if (memP.isUsed()) {
                System.out.printf("Addresses [%d:%d] Process %s - %d \n", memP.getStart(), memP.getEnd(), memP.getProcessName().toUpperCase(), memP.getProcessSize());
            } else {
                System.out.printf("Addresses [%d:%d] unused \n", memP.getStart(), memP.getEnd());

            }
        }
    }

    private void addProcessUsingFirstFitOld(Process process) {
        int numOfPartitions = memoryPartitions.size();
        if (process.getSize() > memorySize) {
            System.out.println("Process could not be added due to insufficient memory.");
            return;
        }
        // memory is empty
        if (numOfPartitions == 0 && process.getSize() <= memorySize) {
            MemoryPartition memoryPartition = new MemoryPartition(process, process.getSize(), 0, process.getSize() - 1);
            memoryPartitions.add(memoryPartition);
            return;
        }
        // add to an unused partition
        for (int i = 0; i < numOfPartitions; i++) {
            MemoryPartition memoryPartition = memoryPartitions.get(i);
            if (!memoryPartition.isUsed() && memoryPartition.hasEnoughMemory(process.getSize())) {
                addToUnusedPartition(memoryPartition, process, i + 1);
                return;
            }
        }
        // there was not any unused partitions, so we're making our own        
        // there is only one partition, create one at the end if possible
        if (numOfPartitions == 1 && memorySize - memoryPartitions.get(0).getEnd() >= process.getSize()) {
            memoryPartitions.add(new MemoryPartition(process, process.getSize(), memoryPartitions.get(0).getEnd() + 1, memoryPartitions.get(0).getEnd() + 1 + process.getSize()));
            return;
        } else {
            // there is multiple
            // try to create a new partition between existing partitions
            for (int i = 0; i < numOfPartitions - 1; i++) {
                if (memoryPartitions.get(i).getEnd() - memoryPartitions.get(i + 1).getStart() >= process.getSize()) {
                    memoryPartitions.add(new MemoryPartition(process, process.getSize(), memoryPartitions.get(i).getEnd() + 1, memoryPartitions.get(i).getEnd() + 1 + process.getSize()));
                }
            }
        }
        // add it to the end if possible
        if (memorySize - memoryPartitions.get(numOfPartitions - 1).getEnd() >= process.getSize()) {
            memoryPartitions.add(new MemoryPartition(process, process.getSize(), memoryPartitions.get(numOfPartitions - 1).getEnd() + 1, memoryPartitions.get(numOfPartitions - 1).getEnd() + 1 + process.getSize()));
            return;
        }
        System.out.println("Process could not be added due to insufficient memory.");

    }

    private void addProcessUsingFirstFit(Process process) {
        int numOfPartitions = memoryPartitions.size();
        if (process.getSize() > memorySize) {
            System.out.println("Process could not be added due to insufficient memory.");
        } else {
            // memory is empty
            if (numOfPartitions == 0 && process.getSize() <= memorySize) {
                MemoryPartition memoryPartition = new MemoryPartition(process, process.getSize(), 0, process.getSize() - 1);
                memoryPartitions.add(memoryPartition);
                MemoryPartition memoryPartition2 = new MemoryPartition(null, memorySize - process.getSize(), memoryPartition.getEnd() + 1, memorySize - 1);
                memoryPartitions.add(memoryPartition2);
                return;
            } else {
                // insert into a partition
                for (int i = 0; i < numOfPartitions; i++) {
                    MemoryPartition memoryPartition = memoryPartitions.get(i);
                    if (!memoryPartition.isUsed() && memoryPartition.hasEnoughMemory(process.getSize())) {
                        addToUnusedPartition(memoryPartition, process, i + 1);
                        return;
                    }
                }
            }
        }
        System.out.println("Process could not be added due to insufficient memory.");
    }

    private void addProcessUsingBestFit(Process process) {
        int numOfPartitions = memoryPartitions.size();
        if (process.getSize() > memorySize) {
            System.out.println("Process could not be added due to insufficient memory.");
        } else {
            // memory is empty
            if (numOfPartitions == 0 && process.getSize() <= memorySize) {
                MemoryPartition memoryPartition = new MemoryPartition(process, process.getSize(), 0, process.getSize() - 1);
                memoryPartitions.add(memoryPartition);
                MemoryPartition memoryPartition2 = new MemoryPartition(null, memorySize - process.getSize(), memoryPartition.getEnd() + 1, memorySize - 1);
                memoryPartitions.add(memoryPartition2);
                return;
            } else {
                // find the smallest possible partition
                int smallestPartitionIndex = -1;
                int smallestPartitionSize = Integer.MAX_VALUE;
                for (int i = 0; i < numOfPartitions; i++) {
                    MemoryPartition memoryPartition = memoryPartitions.get(i);
                    if (!memoryPartition.isUsed() && memoryPartition.hasEnoughMemory(process.getSize())) {
                        if (memoryPartition.getPartitionSize() < smallestPartitionSize) {
                            smallestPartitionIndex = i + 1;
                            smallestPartitionSize = memoryPartition.getPartitionSize();
                        }
                    }
                }
                if (smallestPartitionIndex != -1) {
                    addToUnusedPartition(memoryPartitions.get(smallestPartitionIndex - 1), process, smallestPartitionIndex);
                    return;
                }
            }
        }
        System.out.println("Process could not be added due to insufficient memory.");
    }

    private void addProcessUsinWorstFit(Process process) {
        int numOfPartitions = memoryPartitions.size();
        if (process.getSize() > memorySize) {
            System.out.println("Process could not be added due to insufficient memory.");
        } else {
            // memory is empty
            if (numOfPartitions == 0 && process.getSize() <= memorySize) {
                MemoryPartition memoryPartition = new MemoryPartition(process, process.getSize(), 0, process.getSize() - 1);
                memoryPartitions.add(memoryPartition);
                MemoryPartition memoryPartition2 = new MemoryPartition(null, memorySize - process.getSize(), memoryPartition.getEnd() + 1, memorySize - 1);
                memoryPartitions.add(memoryPartition2);
                return;
            } else {
                // find the largest possible partition
                int largestPartitionIndex = -1;
                int largestPartitionSize = Integer.MIN_VALUE;
                for (int i = 0; i < numOfPartitions; i++) {
                    MemoryPartition memoryPartition = memoryPartitions.get(i);
                    if (!memoryPartition.isUsed() && memoryPartition.hasEnoughMemory(process.getSize())) {
                        if (memoryPartition.getPartitionSize() > largestPartitionSize) {
                            largestPartitionIndex = i + 1;
                            largestPartitionSize = memoryPartition.getPartitionSize();
                        }
                    }
                }
                if (largestPartitionIndex != -1) {
                    addToUnusedPartition(memoryPartitions.get(largestPartitionIndex - 1), process, largestPartitionIndex);
                    return;
                }
            }
        }
        System.out.println("Process could not be added due to insufficient memory.");
    }

    private void addToUnusedPartition(MemoryPartition memoryPartition, Process process, int index) {
        if (memoryPartition.getPartitionSize() > process.getSize()) {
            int partitionEnd = memoryPartition.getStart() + process.getSize();
            int newPartitonStart = partitionEnd + 1;
            int newPartitonEnd = memoryPartition.getEnd();
            memoryPartition.addProcess(process);
            memoryPartition.setEnd(partitionEnd);
            memoryPartition.setSize(memoryPartition.getEnd() - memoryPartition.getStart());
            memoryPartitions.add(index, new MemoryPartition(null, newPartitonEnd - newPartitonStart, newPartitonStart, newPartitonEnd));
        } else {
            memoryPartition.addProcess(process);
        }
    }

    public void compaction() {
        int startingIndex = -1;
        int endingIndex = -1;
        int numOfPartitions = memoryPartitions.size();
        for (int i = 0; i < numOfPartitions; i++) {
            MemoryPartition memP = memoryPartitions.get(i);
            if (memP.isUsed() && endingIndex != -1) {
                combinePartitions(startingIndex, endingIndex);
                startingIndex = -1;
                endingIndex = -1;
            } else if (!memP.isUsed() && startingIndex != -1 && endingIndex != -1) {
                endingIndex++;
            } else if (!memP.isUsed() && startingIndex == -1) {
                startingIndex = i;
                endingIndex = i;
            }
        }
        if (startingIndex != -1) {
            combinePartitions(startingIndex, endingIndex);
        }
    }

    private void combinePartitions(int startingIndex, int endingIndex) {
        int startingAddress = memoryPartitions.get(startingIndex).getStart();
        int endingAddress = memoryPartitions.get(endingIndex).getEnd();
        for (int j = startingIndex; j <= endingIndex; j++) {
            memoryPartitions.remove(startingIndex);
        }
        memoryPartitions.add(startingIndex, new MemoryPartition(null, endingAddress - startingAddress, startingAddress, endingAddress));
    }
}
