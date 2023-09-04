package dev.cammiescorner.arcanus.entity;

//TODO make component
@Deprecated
public interface CanBeDiscombobulated {

    default boolean isDiscombobulated() {
        return getDiscombobulatedTimer() > 0;
    }

    int getDiscombobulatedTimer();

    void setDiscombobulatedTimer(int time);
}
