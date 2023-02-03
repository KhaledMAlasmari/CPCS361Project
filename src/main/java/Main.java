
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Arrays;

public class Main {

    private static Scanner input;
    private static MainMemory memory;
    private static ArrayList<String> commandsList;
    private static boolean testMode = false;

    public static void main(String[] args) throws FileNotFoundException {
        if (testMode) {
            input = new Scanner(new File("input.txt"));
        } else {
            input = new Scanner(System.in);
        }
        commandsList = new ArrayList<>(Arrays.asList("RQ", "RL", "C", "STAT", "EXIT"));
        getMemorySize();
        listenForCommands();
    }

    private static void listenForCommands() {
        String command;
        while (true) {
            printCommands();
            command = getCommandFromUser();
            if (command.contains("rq")) {
                String[] commandParam = command.split(" ");
                Process process = new Process(commandParam[1], Integer.parseInt(commandParam[2]), commandParam[3]);
                memory.addProcess(process);
            } else if (command.contains("rl")) {
                String[] commandParam = command.split(" ");
                memory.removeProcess(commandParam[1]);
            } else if (command.equals("stat")) {
                memory.stats();
            } else if (command.equals("c")) {
                memory.compaction();
            } else {
                System.exit(0);
            }
        }
    }

    private static void getMemorySize() {
        while (true) {
            try {
                System.out.printf("Enter the memory size: \n");
                int memorySize = Integer.parseInt(input.nextLine());
                memory = new MainMemory(memorySize);
                return;
            } catch (NumberFormatException err) {
                System.out.println("Please enter a valid integer");
            }
        }
    }

    private static void printCommands() {
        System.out.println("------------------------------------------------------------------------------------------");

        System.out.println("1- RQ: Request a contiguous block of memory. (e.g P1 400 W)\n"
                + "2- RL: Release a contiguous block of memory. (RL P1)\n"
                + "3- C: Compact unused holes of memory into a single block.\n"
                + "4- STAT: Report the regions of free and allocated memory.\n"
                + "5- EXIT");

        System.out.println("------------------------------------------------------------------------------------------");

    }

    private static String getCommandFromUser() {
        String command = "";
        while (!commandsList.contains(command)) {

            System.out.printf("Enter command: \n");
            command = input.nextLine().toLowerCase();
            if (command.matches("(rl p\\d*)|(rq p\\d* \\d+ (f|b|w))|stat|exit|c")) {
                return command;
            } else {
                System.out.println("The command you enterd is invalid.");
            }
        }
        return command;
    }

    public static boolean isInteger(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
