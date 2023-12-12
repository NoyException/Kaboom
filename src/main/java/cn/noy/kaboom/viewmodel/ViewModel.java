package cn.noy.kaboom.viewmodel;

import cn.noy.kaboom.app.Binder;

import java.beans.PropertyChangeSupport;

public abstract class ViewModel {
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    public ViewModel() {
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

//    public void onAttach(Binder.FieldBinder binder) {
//    }

    public void onAttached(){}

}
