package org.syaku.spring.validation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Collection or Array 타입의 객체를 검증하기 위해 사용된다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 29.
 */
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface Validation {
	Class<?>[] value() default {};
}
