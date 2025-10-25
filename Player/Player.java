/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* start of the file from here */

/**
 * 🎵 Player.java
 * -------------------------------------
 * This class manages playback using the MP3Player library.
 * It supports adding, removing, and reloading songs from a given path.
 * 
 * Minor improvements and documentation added for Hacktoberfest 2025.
 *
 * @author Sameer Akhtari
 */


import java.io.*;
import java.util.*;
import jaco.mp3.player.MP3Player; // Importing MP3 player library

public class Player {

    // Static variables for playback
    static String path1 = "";
    static MP3Player player = new MP3Player();

    // UI and file handling
    ListFrame lf = new ListFrame();
    File file;

    // Playlist
    private static ArrayList<String> pList = new ArrayList<>();

    /** Default constructor */
    public Player() {
        // Constructor intentionally left blank
        System.out.println("🎵 MP3 Player Initialized");
    System.out.println("👨‍💻 Hacktoberfest 2025 contribution by Misbah Qureshi");
    }

    /**
     * Retrieves MP3 files from the specified directory and adds them to playlist.
     * @param path directory path containing MP3 files
     */
    public void retrive(String path) {
        path1 = path;
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) {
            System.out.println("⚠️ No files found in: " + path);
            return;
        }

        for (File listOfFile : listOfFiles) {
            if (listOfFile.isFile()) {
                String name = listOfFile.getName();
                if (name.endsWith(".mp3")) {
                    addList(name);
                }
            }
        }

        playSet();
    }

    /**
     * Prepares the playlist and updates the UI.
     */
    public void playSet() {
        lf.clearList();

        for (int i = 0; i < pList.size(); i++) {
            player.addToPlayList(new File(path1 + "\\" + pList.get(i)));
            lf.setList(pList.get(i));
        }
    }

    /** Adds a file name to the playlist */
    public void addList(String name) {
        pList.add(name);
    }

    /** Gets a file name from the playlist by index */
    public String getList(int index) {
        return pList.get(index);
    }

    /** Removes an item from the playlist by index */
    public void removeList(int index) {
        pList.remove(index);
    }

    /**
     * Moves a song within the playlist to a new position.
     * @param index current position
     * @param number new position (1-based)
     */
    public void setAt(int index, int number) {
        String temp = getList(index);
        removeList(index);

        String temp3;
        String temp2 = getList(number - 1);
        pList.set(number - 1, temp);

        for (int i = number; i < pList.size(); i++) {
            temp3 = getList(i);
            pList.set(i, temp2);
            temp2 = temp3;
        }

        // Code updated for Hacktoberfest contribution
        reload();
    }

    /**
     * Reloads and refreshes the player.
     */
    public void reload() {
        PlayerGUI pg = new PlayerGUI();
        stop();

        if (player.isPaused()) {
            pg.pauseSet();
        }

        MP3Player tempp = new MP3Player();
        player = tempp;
        playSet();
        pg.playSet();
        player.play();
    }

    /** Deletes current track (reloads player state) */
    public void delete() {
        reload();
    }

    /** Skips to next song in playlist */
    public void next() {
        player.skipForward();
    }

    /** Skips to previous song in playlist */
    public void prev() {
        player.skipBackward();
    }

    /** Starts playing the loaded playlist */
    public void play() {
        System.out.println("▶️ Play clicked");
        player.play();
    }

    /** Sets repeat mode for the player */
    public void setRep(boolean x) {
        player.setRepeat(x);
    }

    /** Pauses current playback */
    public void pause() {
        player.pause();
    }

    /** Stops current playback */
    public void stop() {
        player.stop();
    }
}
