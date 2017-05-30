package org.syaku.spring.apps.validation.support;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.validation.FieldError;
import org.syaku.spring.validation.ValidationMessage;

/**
 * 프로젝트에 맞는 메세지를 출력하기 위해 {@link ValidationMessage} 의 구현체를 작성한다.
 *
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 16.
 */
	public class AppValidationMessage implements ValidationMessage {
	private final MessageSourceAccessor messageSourceAccessor;

	public AppValidationMessage(MessageSourceAccessor messageSourceAccessor) {
		this.messageSourceAccessor = messageSourceAccessor;
	}

	@Override
	public String getFieldName(FieldError fieldError) {
		return messageSourceAccessor.getMessage("text.field." + fieldError.getField(), fieldError.getField());
	}

	@Override
	public String getBindingFailure(FieldError fieldError) {
		return messageSourceAccessor.getMessage("text.valid.Invalid", fieldError.getDefaultMessage());
	}
}
