package main;

import editorAndSettingsMap.EditorOfMap;
import island.Island;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "edit".equalsIgnoreCase(args[0])) {
            new EditorOfMap().editor();
        } else {
            new Island().start();
        }
    }
}
