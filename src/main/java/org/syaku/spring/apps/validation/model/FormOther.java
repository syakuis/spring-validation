package org.syaku.spring.apps.validation.model;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 30.
 */
@Data
public class FormOther {

	@NotEmpty
	private String other;
}
