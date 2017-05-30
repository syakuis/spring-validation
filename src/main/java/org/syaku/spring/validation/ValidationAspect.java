package org.syaku.spring.validation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Collection;
import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 16.
 */
@Aspect
public class ValidationAspect implements Ordered {
	private static final Logger logger = LoggerFactory.getLogger(ValidationAspect.class);
	private Validator validator;

	@Override
	public int getOrder() {
		return 1;
	}

	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	@Pointcut("@target(org.springframework.stereotype.Controller) || @target(org.springframework.web.bind.annotation.RestController)")
	public void pointTarget() {
		logger.debug(">< >< invoke aspectj");
	}

	@Around("pointTarget() && (execution(public * *(@org.syaku.spring.validation.Validation (*))) || execution(public * *(.., @org.syaku.spring.validation.Validation (*))) || execution(public * *(@org.syaku.spring.validation.Validation (*), ..)) || execution(public * *(.., @org.syaku.spring.validation.Validation (*), ..)))")
	public Object validation(ProceedingJoinPoint point) throws Throwable {
		logger.debug(">< >< Validation Arguments Aspect : {}", point.toShortString());
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		Annotation[][] argAnnotations = method.getParameterAnnotations();

		Object[] args = point.getArgs();
		int count = args.length;
		Object[] result = new Object[count];

		ValidationWrapper wrapper = new ValidationWrapper();
		BindingResult bindingResult = null;
		boolean isBindingResult = false;

		for (int i = 0; i < count; i++) {
			Object arg = args[i];
			Annotation annotation = getAnnotation(argAnnotations[i]);

			if (annotation != null && arg != null) {
				DataBinder binder;

				if (isType(arg)) {
					wrapper.setObject(arg);
					binder = new DataBinder(wrapper);
				} else {
					binder = new DataBinder(arg);
				}

				binder.setValidator(validator);
				binder.validate(((Validation) annotation).value());
				bindingResult = binder.getBindingResult();
			}

			if (bindingResult != null && arg instanceof BindingResult) {
				arg = bindingResult;
				isBindingResult = true;
			}

			Array.set(result, i, arg);
		}

		if (!isBindingResult && bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
		}

		return point.proceed(result);
	}

	private boolean isType(Object object) {
		Class<?> clazz = object.getClass();

		if (
				clazz.isArray() ||
				Collection.class.isAssignableFrom(clazz) ||
				Map.class.isAssignableFrom(clazz)) {
			return true;
		}

		return false;
	}

	private Annotation getAnnotation(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (Validation.class.isInstance(annotation)) {
				return annotation;
			}
		}

		return null;
	}
}
