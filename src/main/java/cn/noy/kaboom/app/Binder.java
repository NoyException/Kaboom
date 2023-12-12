package cn.noy.kaboom.app;

import cn.noy.kaboom.view.View;
import cn.noy.kaboom.viewmodel.ViewModel;

import java.lang.reflect.Field;

public class Binder {

    public static void bind(ViewModel viewModel, View view){
//        for(Class<?> clazz = viewModel.getClass(); clazz != ViewModel.class; clazz = clazz.getSuperclass()) {
//            for (Field field : clazz.getDeclaredFields()) {
//                if(field.isAnnotationPresent(Bind.class)){
//                    try {
//                        Bind bind = field.getAnnotation(Bind.class);
//                        field.setAccessible(true);
//                        String viewFieldName = bind.field().isEmpty() ? field.getName() : bind.field();
//                        Field viewField = bind.view().getDeclaredField(viewFieldName);
//                        viewField.setAccessible(true);
//                        viewField.set(view, field.get(viewModel));
//                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//            }
//        }
//        viewModel.onAttach(new FieldBinder(view));

        viewModel.getPropertyChangeSupport().addPropertyChangeListener(view);

        viewModel.onAttached();
        view.onAttached();
    }


//    public static class FieldBinder{
//
//        private final View view;
//        public FieldBinder(View view){
//            this.view = view;
//        }
//
//        public void bind(Class<? extends View> clazz, String field, Object value){
//            try {
//                Field viewField = clazz.getField(field);
//                viewField.setAccessible(true);
//                viewField.set(view, value);
//            } catch (NoSuchFieldException | IllegalAccessException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
