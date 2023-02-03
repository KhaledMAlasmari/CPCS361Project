/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author khaled
 */
public class Process {

    private String name;
    private int size;
    private String strategy;

    public Process(String name, int size, String strategy) {
        this.name = name;
        this.size = size;
        this.strategy = strategy;
    }

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public String getStrategy() {
        return strategy;
    }
}
