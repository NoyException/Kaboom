package cn.noy.kaboom.model.event;

import cn.noy.kaboom.model.Game;

public abstract class Event {
    private EventState state = EventState.READY;

    public EventState getState() {
        return state;
    }

    public void call(){
        if(state == EventState.READY){
            state = EventState.CALLING;
            Game.getInstance().getEventManager().callEvent(this);
            state = EventState.CALLED;
        }
    }
}
