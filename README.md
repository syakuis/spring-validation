# Spring Validation : 데이터 유효성 검사

spring framework, hibernate-validator

스프링 프레임워크의 유효성 검증을 보다 유용하게 기능을 확장한 튜토리얼이다.

![](http://i.imgur.com/vzIMUUZ.gif)

### 기능

인자 값이 Collection 혹은 Array 인 경우에도 검증이 가능하다.

검증 결과를 객체화하여 사용하기 편하게 반환한다.

검증 결과를 자동으로 정리하여 응답한다.

### 의존성

hibernate-validator 버전은 상관없다.

```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>4.3.2.Final</version>
</dependency>
```

실제 사용되는 핵심 소스 패키지는 `org.syaku.spring.validation.*` 이다.



### 설정

Bean Conext Configuration

```java
@Configuration
public class ValidationConfiguration {

	// @Validation 선언적인 방식을 적용한다.
	@Bean
	public ValidationAspect validationAspect() {
		return new ValidationAspect();
	}


	// 메세지 메서드를 간편하게 사용하기 위해 필요...
	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource);
	}

	/**
	 <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
	 <property name="validationMessageSource" ref="messageSource" />
	 </bean>
	 * @return
	 */
	@Bean
	public Validator validator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		// massageSource 선언하지 않으면 라이브러리에 기본 메세지를 사용한다.
		validator.setValidationMessageSource(messageSource);
		validator.afterPropertiesSet();
		return validator;
	}
}

```

Bean Application Configuration

```java
public class ServletConfiguration extends WebMvcConfigurerAdapter implements WebMvcConfigurer {

	// 유효성 검증을 설정한다.
	@Autowired
	Validator validator;

	/**
	 <mvc:annotation-driven validator="validator"/>
	 * @return
	 */
	@Override
	public Validator getValidator() {
		return validator;
	}
	
	... skip ...
	
}
```

### 사용방법

검증 오류 얻기

```java
@PostMapping
public String formPost(Model model, @Validated Form form, BindingResult bindingResult) {
	
	if (bindingResult.hasErrors()) {
		model.addAttribute("errors",
				new ValidationResult(
						bindingResult,
						new AppValidationMessage(messageSource)
				).getFieldErrors());
	}
	
	혹은
	
	if (bindingResult.hasErrors()) {
		throw new ValidationException(bindingResult);
	}
}
```

자동으로 검증 오류 처리하기

```java
@PutMapping
@ResponseBody
public SuccessHandler ajaxPut(@Validated @RequestBody Form form) {
	return new SuccessHandler("success");
}

혹은

@PutMapping
@ResponseBody
public SuccessHandler ajaxPut(@Validation @RequestBody Form form) {
	return new SuccessHandler("success");
}

```


Collection 혹은 Array 형식 검증하기

```java
@PutMapping(value = "/forms")
@ResponseBody
public SuccessHandler ajaxPutForms(@Validation @RequestBody List<Form> forms, BindingResult bindingResult) {
	if (bindingResult.hasErrors()) {
		throw new ValidationException(bindingResult);
	}

	return new SuccessHandler("success");
}
```

Collection 혹은 Array 형식 검증하고 결과 자동으로 응답하기

```java
@PostMapping(value = "/forms")
@ResponseBody
public SuccessHandler ajaxPostForms(@Validation @RequestBody List<Form> forms) {
	return new SuccessHandler("success");
}
```

### 추가로 지원하는 유효 검증 기능

@Between

값이 특정 범위 값에 포함되지는 검증한다.

@DateTime

문자열 값이 날짜형식의 값인지 검증한다.

@SingleByte

한 글자가 1바이트 인지를 검증한다. 즉 영문, 숫자 그리고 1바이트 특수문자만 입력이 가능하다.

@GeneralPattern

다양한 형식의 값을 검증한다.

```
UPPER_ALPHABET_NUMBER_UNDERSCORE,
UPPER_ALPHABET_NUMBER,
UPPER_ALPHABET,
ALPHABET_NUMBER_UNDERSCORE,
ALPHABET_NUMBER,
ALPHABET,
KOREAN_NUMBER,
KOREAN,
NUMBER,
USER_ID,
CRONTAB
```




