package gk646.jnet;


import gk646.jnet.userinterface.Window;
import javafx.application.Application;

public class Main {
    public static final long startUpTime = System.nanoTime();

    public static void main(String[] args) {
        Application.launch(Window.class, args);
    }
}