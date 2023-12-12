package cn.noy.kaboom.model.event;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EventManager {
    private final Map<EventHandler.Priority, Multimap<Class<? extends Event>, EventHandlerWrapper<? extends Event>>> listeners;
    private int callEventDepth = 0;
    private final List<Object> toRegister = new LinkedList<>();
    private final List<Object> toUnregister = new LinkedList<>();

    public EventManager() {
        listeners = new HashMap<>();
        for (EventHandler.Priority priority : EventHandler.Priority.values()) {
            listeners.put(priority, HashMultimap.create());
        }
    }

    private void update(){
        toRegister.forEach(this::registerListenersNow);
        toRegister.clear();
        toUnregister.forEach(this::unregisterListenersNow);
        toUnregister.clear();
    }

    private void registerListenersNow(Object listener, Class<? extends Event> clazz, EventHandler.Priority priority, boolean ignoreCanceled, Consumer<? extends Event> consumer) {
        listeners.get(priority).put(clazz, new EventHandlerWrapper<>(listener, consumer, priority, ignoreCanceled));
    }

    @SuppressWarnings("unchecked")
    private void registerListenersNow(Object listener){
        for (Class<?> clazz = listener.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(EventHandler.class)) {
                    EventHandler eventHandler = method.getAnnotation(EventHandler.class);
                    if (method.getParameterCount() == 1) {
                        Class<?> parameterType = method.getParameterTypes()[0];
                        if (Event.class.isAssignableFrom(parameterType)) {
                            method.setAccessible(true);
                            registerListenersNow(listener, (Class<? extends Event>) parameterType, eventHandler.priority(), eventHandler.ignoreCanceled(), (Consumer<? extends Event>) event -> {
                                try {
                                    method.invoke(listener, event);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void unregisterListenersNow(Object listener){
        for (EventHandler.Priority priority : EventHandler.Priority.values()) {
            Multimap<Class<? extends Event>, EventHandlerWrapper<? extends Event>> multimap = listeners.get(priority);
            multimap.values().removeIf(wrapper -> wrapper.listener().equals(listener));
        }
    }

    public void registerListeners(Object listener) {
        toRegister.add(listener);
        if(callEventDepth == 0) {
            update();
        }
    }

    public void unregisterListeners(Object listener) {
        toUnregister.add(listener);
        if(callEventDepth == 0) {
            update();
        }
    }

    @SuppressWarnings("unchecked")
    <T extends Event> void callEvent(T event) {
        if(callEventDepth == 0) {
            update();
        }
        if (callEventDepth++ >= 16) {
            throw new RuntimeException("Event call depth is too high!");
        }

        Cancelable cancelable = event instanceof Cancelable ? (Cancelable) event : null;

        for (EventHandler.Priority priority : EventHandler.Priority.values()) {
            Multimap<Class<? extends Event>, EventHandlerWrapper<? extends Event>> multimap = listeners.get(priority);
            for (Class<? extends Event> clazz = event.getClass(); clazz != Event.class; clazz = (Class<? extends Event>) clazz.getSuperclass()) {
                for (EventHandlerWrapper<? extends Event> wrapper : multimap.get(clazz)) {
                    if (cancelable != null && cancelable.isCanceled() && wrapper.ignoreCanceled()) {
                        continue;
                    }
                    ((Consumer<T>) wrapper.consumer()).accept(event);
                }
            }
        }

        callEventDepth--;
        if(callEventDepth == 0) {
            update();
        }
    }

    private record EventHandlerWrapper<T extends Event>(
            Object listener,
            Consumer<T> consumer,
            EventHandler.Priority priority,
            boolean ignoreCanceled) {
    }
}
