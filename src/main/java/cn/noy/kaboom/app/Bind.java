package cn.noy.kaboom.app;

import cn.noy.kaboom.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bind {
    Class<? extends View> view() default View.class;
    String field() default "";
}
