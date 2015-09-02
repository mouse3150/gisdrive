package cn.com.esrichina.gcloud.commons;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AutoLog {
	/**
	 * @param 模块名字
	 */
	String modelName();

	/**
	 * @param 操作类型
	 */
	String operation();
}