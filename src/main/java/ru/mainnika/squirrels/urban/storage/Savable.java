package ru.mainnika.squirrels.urban.storage;

import ru.mainnika.squirrels.urban.utils.Timers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Savable {

    private static final Logger log;
    private static final ConcurrentLinkedQueue<Savable> SAVABLES;
    private static final Saver SAVER;

    static {
        log = Logger.getLogger(Savable.class.getName());
        SAVABLES = new ConcurrentLinkedQueue<>();
        SAVER = new Saver();
    }

    private final String name;

    public Savable(String name) {
        this.name = name;
        SAVABLES.add(this);
    }

    public abstract void onSave();

    private static class Saver implements Timers.Task {

        private Saver() {
            Timers.subscribe(this, 5, 5, TimeUnit.MINUTES);
        }

        @Override
        public void onTimer() {
            for (Savable savable : SAVABLES) {
                log.log(Level.INFO, "Saving {0}", savable.name);
                savable.onSave();
            }
        }
    }
}
