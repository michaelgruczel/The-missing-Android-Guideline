package de.example.michaelgruczel.betterexample.events;


public class CheckEvent {
    private int commits;

    public CheckEvent(int commits) {
        this.commits = commits;
    }

    public int getCommits() {
        return commits;
    }
}
