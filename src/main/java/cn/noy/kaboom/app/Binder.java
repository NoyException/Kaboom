package cn.noy.kaboom.app;

import cn.noy.kaboom.view.View;
import cn.noy.kaboom.viewmodel.ViewModel;

public class Binder {

    public static void bind(ViewModel viewModel, View view){
        viewModel.getPropertyChangeSupport().addPropertyChangeListener(view);
        viewModel.onAttached();
        view.onAttached();
    }

}
