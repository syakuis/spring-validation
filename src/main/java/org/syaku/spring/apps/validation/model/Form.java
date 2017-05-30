package org.syaku.spring.apps.validation.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.syaku.spring.validation.constraints.Between;
import org.syaku.spring.validation.constraints.DateTime;
import org.syaku.spring.validation.constraints.GeneralPattern;
import org.syaku.spring.validation.constraints.SingleByte;
import org.syaku.spring.validation.group.Edit;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 10.
 */
@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Form extends FormOther {
	@NotEmpty(groups = Edit.class)
	private String idx;

	@Length(max = 50)
	@GeneralPattern(pattern = GeneralPattern.Regex.ALPHABET)
	private String userId;

	@NotEmpty
	@SingleByte
	@Length(max = 50)
	private String password;

	@NotEmpty
	@SingleByte
	@Length(max = 50)
	private String password2;

	@NotEmpty
	@Length(max = 50)
	@GeneralPattern(pattern = GeneralPattern.Regex.KOREAN)
	private String name;

	@NotNull
	@Range(min = 1, max = 200)
	private Integer age;

	@NotEmpty
	@Between(valueSet = Between.ValueSet.YN)
	private String sex;

	@Past
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;

	@DateTime(format = "yyyyMMddHHssmm")
	private String date;

	private String phone;

	@Email
	private String email;

	@NotNull
	@Size(min = 1, max = 2)
	private String[] hobby;

	/**
	 * 검증을 위해 접근자는 public 해야한다.
	 * @return
	 */
	@AssertTrue(message = "비밀번호가 일치하지 않습니다.")
	public boolean isPassowrdCompare() {
		// 이전에 빈값을 검증하기 때문에 널 검사는 제외한다. (null pointer 예외를 피하기 위함)
		if (this.password == null || this.password2 == null) {
			return true;
		}

		return this.password.equals(this.password2);
	}

	@Valid
	@Size(min = 1, max = 2)
	List<Store> stores;
}
