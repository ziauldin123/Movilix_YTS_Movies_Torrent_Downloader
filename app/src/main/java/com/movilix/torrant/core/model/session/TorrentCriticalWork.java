package com.movilix.torrant.core.model.session;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

class TorrentCriticalWork
{
    public enum Type
    {
        MOVING,
        SAVE_RESUME
    }

    public class State
    {
        public boolean moving;
        public boolean saveResume;
        public long changeTime;

        public State(boolean moving, boolean saveResume, long changeTime)
        {
            this.moving = moving;
            this.saveResume = saveResume;
            this.changeTime = changeTime;
        }

        public boolean isDuringChange()
        {
            return moving || saveResume;
        }
    }

    private ExecutorService exec = Executors.newFixedThreadPool(2);
    private AtomicBoolean moving = new AtomicBoolean();
    private AtomicInteger saveResume = new AtomicInteger();
    private BehaviorSubject<State> stateChangedEvent =
            BehaviorSubject.createDefault(new State(false, false, System.currentTimeMillis()));

    public boolean isMoving()
    {
        return moving.get();
    }

    public void setMoving(boolean moving)
    {
        this.moving.set(moving);
        emitChangedEvent();
    }

    public boolean isSaveResume()
    {
        return saveResume.get() > 0;
    }

    public void setSaveResume(boolean saveResume)
    {
        if (saveResume)
            this.saveResume.incrementAndGet();
        else
            this.saveResume.decrementAndGet();
        emitChangedEvent();
    }

    public Observable<State> observeStateChanging()
    {
        return stateChangedEvent;
    }

    private void emitChangedEvent()
    {
        exec.submit(() -> {
            stateChangedEvent.onNext(
                    new State(moving.get(), saveResume.get() > 0, System.currentTimeMillis()));
        });
    }
}
