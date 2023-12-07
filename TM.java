
/*
 * @title HW3 - A Simple Task Tracker
 * @author Aunsh Bandivadekar | 0907
 * @version 2.0
 */


import java.util.*;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.BufferedWriter;

public class TM {
    private static TaskRepository repository = new TaskRepository();

    public static void main(String[] args){
        TM app = new TM();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            String[] commands = input.split(" ");
            String command = commands[2];

            switch (command) {
                case "start":
                    startTask(commands);
                    break;
                case "stop":
                    stopTask(commands);
                    break;
                default:
                    printHelp();
            }
        }
    }

    private static void startTask(String[] commands) {
        // Get name params
        String name = commands[3];
        String currentTask = repository.getCurrentTask();

        if(currentTask != null && !currentTask.equals(name)){
            System.out.println("Another task is running. Stop " + currentTask + " to start new task");
            return;
        }
        Task task = new Task(name);
        task.start();
        repository.setCurrentTask(name);
        repository.updateTask(task);
    }

    private static void stopTask(String[] commands){
        // Get name params
        String name = commands[3];
        String currentTask = repository.getCurrentTask();
        if(currentTask != null && !currentTask.equals(name)){
            System.out.println("Another task is running. Stop " + currentTask + " to start new task");
            return;
        }
        if(repository.getTask(name) == null) {
            System.out.println("This task does not exist. Cannot stop it.");
            return;
        }
        Task task = repository.getTask(name);
        task.stop();
        repository.updateTask(task);
        repository.makeCurrentTaskNull();
    }
    private static void printHelp() {
        System.out.println("Error - Incorrect command use");
        System.out.println("Usage: java TM start <name>");
    }
}

/*
    class Task
    @desc A class for the task
 */
class Task {

    // Fields to store task data
    private String name;
    private String description;
    private String size;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Constructor to initialize name
    public Task(String name) {
        this.name = name;
    }
    public void start() {
        this.startTime = LocalDateTime.now();
    }
    public void stop() {
        this.endTime = LocalDateTime.now();
    }
    // Getters below
    public String getName(){
        return name;
    }
    public String getDescription(){
        return description;
    }
    public String getSize(){
        return size;
    }
    public LocalDateTime getStartTime(){
        return startTime;
    }
    public LocalDateTime getEndTime(){
        return endTime;
    }
    // Setters Below
    public void setDescription(String description){
        this.description = description;
    }
    public void setSize(String size){
        this.size = size;
    }
    public void setStartTime(LocalDateTime startTime){
        this.startTime = startTime;
    }
    public void setEndTime(LocalDateTime endTime){
        this.endTime = endTime;
    }

}

class TaskRepository {

//    private Map<String, Task> tasks = new HashMap<>();
    private Map<String, Task> tasks;
    private String currentTask;

    public TaskRepository() {
        this.tasks = new HashMap<>();
        this.currentTask = null;
    }

    public void addTask(Task task) {
        tasks.put(task.getName(), task);
    }

    public Task getTask(String name) {
        return tasks.get(name);
    }

    public String getCurrentTask(){
        return currentTask;
    }

    public void makeCurrentTaskNull () {
        this.currentTask = null;
    }
    public void setCurrentTask(String name) {
        this.currentTask = name;
    }

    public void updateTask(Task task){
        Task current = tasks.get(task.getName());

        if(current != null) {
            // If task present, copy over updated fields
            current.setDescription(task.getDescription());
            current.setSize(task.getSize());
            current.setStartTime(task.getStartTime());
            current.setEndTime(task.getEndTime());

            // Save updated task object
            tasks.put(task.getName(), current);
        } else {
            // If new task, directly add
            tasks.put(task.getName(), task);
        }
        // Delete the below
        tasks.forEach((key, value) -> System.out.println(key + ":" + value));
    }
}