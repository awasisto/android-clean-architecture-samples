package com.wasisto.githubuserfinder.util.scheduler;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

public class TestSchedulerProvider implements SchedulerProvider {

    private Scheduler testScheduler = new TestScheduler();

    @Override
    public Scheduler computation() {
        return testScheduler;
    }

    @Override
    public Scheduler io() {
        return testScheduler;
    }

    @Override
    public Scheduler ui() {
        return testScheduler;
    }
}
