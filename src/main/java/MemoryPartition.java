
public class MemoryPartition {

    private Process process;
    private int size;
    private int start;
    private int end;
    private boolean isUsed;

    public MemoryPartition(Process process, int size, int start, int end) {
        this.process = process;
        this.size = size;
        this.start = start;
        this.end = end;
        this.isUsed = process != null;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public boolean hasEnoughMemory(int processSize) {
        return size >= processSize;
    }

    public void addProcess(Process process) {
       this.process = process;
       isUsed = true;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    public int getPartitionSize() {
        return size;
    }

    
    public int getProcessSize() {
        return process != null ? process.getSize() : 0;
    }

    public String getProcessName() {
        return process != null ? process.getName() : null;
    }

    public void removeProcess() {
        process = null;
        isUsed = false;
    }
}
