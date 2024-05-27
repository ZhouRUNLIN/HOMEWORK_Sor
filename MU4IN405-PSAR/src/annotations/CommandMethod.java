package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 运行时保留注解，以便可以通过反射读取
@Target(ElementType.METHOD) // 注解只能应用于方法
public @interface CommandMethod {
}
