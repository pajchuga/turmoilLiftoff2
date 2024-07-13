package com.turmoillift2.handlers;

public class MyInput {
    public static boolean[] keys;
    public static boolean[] pkeys;

    public static final int NUM_KEYS = 7;
    public static final int PAUSE_BUTTON = 0;
    public static final int PLAY_BUTTON = 1;
    public static final int UP_BUTTON = 2;
    public static final int DOWN_BUTTON = 3;
    public static final int LEFT_BUTTON = 4;
    public static final int RIGHT_BUTTON = 5;
    public static final int ATTACK_BUTTON = 6;

    static {
        keys = new boolean[NUM_KEYS];
        pkeys = new boolean[NUM_KEYS];
    }

    public static void update() {
        for (int i = 0; i < NUM_KEYS; i++) {
            pkeys[i] = keys[i];
        }
    }

    public static boolean isDown(int key) {
        return keys[key];
    }

    public static boolean isPressed(int key) {
        return keys[key] && !pkeys[key];
    }

    public static void setKey(int key, boolean set) {
        keys[key] = set;
    }

}
