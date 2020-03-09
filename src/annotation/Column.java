package annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import base.DatabaseType;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	DatabaseType type();
}
