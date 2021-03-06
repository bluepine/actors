package com.qklabs.actors;

import android.os.Handler;
import android.os.Message;

import java.util.concurrent.LinkedBlockingQueue;

class MessageQueue {
    private final LinkedBlockingQueue<Runnable> mQueue = new LinkedBlockingQueue<>();

    public void sendMessage(final Actor target, final Object message, final ActorRef sender) {
        mQueue.add(new Runnable() {
            @Override
            public void run() {
                target.onReceive(message, sender);
            }
        });
    }

    public void start(final Actor actor) {
        mQueue.add(new Runnable() {
            @Override
            public void run() {
                actor.preStart();
            }
        });
    }

    public void stop(final Actor actor) {
        mQueue.add(new Runnable() {
            @Override
            public void run() {
                actor.postStop();
            }
        });
    }

    public void processEvent() {
        Runnable task = mQueue.poll();
        if (task != null) {
            task.run();
        }
    }
}
