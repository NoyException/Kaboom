package cn.noy.kaboom.model.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    enum Priority{
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST
    }
    Priority priority() default Priority.NORMAL;
    boolean ignoreCanceled() default false;
}
