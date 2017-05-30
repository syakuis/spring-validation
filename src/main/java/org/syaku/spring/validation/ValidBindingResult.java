package org.syaku.spring.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link org.springframework.validation.BindingResult} 결과에 대한 처리를 담당한다. 검증에 대한 오류가 있는 경우 {@link ValidationException} 발생한다. {@link org.springframework.web.bind.annotation.ControllerAdvice} 에서 감시하여 해당 예외에 대한 최종 처리를 담당한다.
 *
 * @see org.springframework.validation.BindingResult
 * @see ValidationException
 * @see org.springframework.web.bind.annotation.ControllerAdvice
 * @see org.syaku.spring.apps.validation.web.ExceptionController
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 16.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidBindingResult {
}
