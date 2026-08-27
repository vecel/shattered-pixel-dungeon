package com.karandys.todo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Repeatable(TodoWrapper.class)
@Target({
        ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR,
        ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER
})
public @interface Todo {
    String value();
    TodoType type() default TodoType.NONE;

    enum TodoType {
        NONE, BUG
    }
}
