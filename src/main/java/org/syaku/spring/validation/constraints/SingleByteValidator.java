package org.syaku.spring.validation.constraints;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.UnsupportedEncodingException;

/**
 * 한 글자가 1바이트 인지를 검증한다.
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 15.
 */
public class SingleByteValidator implements ConstraintValidator<SingleByte, String> {
	private static final Logger logger = LoggerFactory.getLogger(SingleByteValidator.class);

	private String charset;

	@Override
	public void initialize(SingleByte parameters) {
		this.charset = parameters.charset();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.length() == 0) {
			return true;
		}

		try {
			byte[] bytes = value.getBytes(this.charset);
			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i] < 0) {
					return false;
				}
			}
		} catch (UnsupportedEncodingException uee) {
			logger.error(uee.getMessage(), uee);
			return false;
		}

		return true;
	}
}
