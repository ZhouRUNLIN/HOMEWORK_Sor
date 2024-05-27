package test;

import machine.MirrorInitiator;

import java.io.IOException;

public class StartMirror {

    public static void main(String[] args) {

        MirrorInitiator mirrorInitiator;

        {
            try {
                mirrorInitiator = new MirrorInitiator("mirror", 1010);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
