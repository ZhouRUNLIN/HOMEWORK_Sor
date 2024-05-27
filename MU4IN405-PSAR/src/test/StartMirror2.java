package test;

import machine.MirrorInitiator;

import java.io.IOException;

public class StartMirror2 {

    public static void main(String[] args) {

        MirrorInitiator mirrorInitiator;

        {
            try {
                mirrorInitiator = new MirrorInitiator("mirror2", 1011);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



}
