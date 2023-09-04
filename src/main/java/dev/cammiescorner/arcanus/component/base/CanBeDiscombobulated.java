package dev.cammiescorner.arcanus.component.base;

import dev.onyxstudios.cca.api.v3.component.Component;

public interface CanBeDiscombobulated extends Component {

    boolean isDiscombobulated();

    int getDiscombobulatedTimer();

    void setDiscombobulatedTimer(int time);
}
