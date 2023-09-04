package dev.cammiescorner.arcanus.core.util;

public interface CanBeDiscombobulated {

    default boolean isDiscombobulated() {
        return getDiscombobulatedTimer() > 0;
    }

    int getDiscombobulatedTimer();

    void setDiscombobulatedTimer(int time);
}
