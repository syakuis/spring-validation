package org.syaku.spring.apps.validation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.syaku.spring.apps.validation.model.Form;
import org.syaku.spring.apps.validation.support.AppValidationMessage;
import org.syaku.spring.handlers.SuccessHandler;
import org.syaku.spring.validation.*;

import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 10.
 */
@Controller
@RequestMapping("/validation")
public class FormController {
	@Autowired
	private MessageSourceAccessor messageSource;

	/**
	 * 유효성 검증을 위한 입력폼 뷰
	 * @return
	 */
	@GetMapping
	public String display() {
		return "validation/form";
	}

	/**
	 * 폼에서 전송된(form_urlencoded type) 데이터를 검증한다.
	 * @param model
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String formPost(Model model, @Validated Form form, BindingResult bindingResult) {
		model.addAttribute("form", form);

		if (bindingResult.hasErrors()) {
			model.addAttribute("errors",
					new ValidationResult(
							bindingResult,
							new AppValidationMessage(messageSource)
					).getFieldErrors());
			return "validation/form";
		}

		return "validation/done";
	}

	/**
	 * ajax 방식으로 전송된 데이터를 검증한다.
	 * {@link Form} 데이터를 검증하고 결과를 {@link BindingResult} 로 얻는 다.
	 * 얻은 결과로 직졉 처리해야 한다.
	 * @param form
	 * @param bindingResult
	 * @return
	 */
	@PostMapping
	@ResponseBody
	public SuccessHandler ajaxPost(@Validated @RequestBody Form form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
		}
		return new SuccessHandler("success");
	}

	/**
	 * ajax 방식으로 전송된 데이터를 검증한다.
	 * @param form
	 * @return
	 */
	@PutMapping
	@ResponseBody
	public SuccessHandler ajaxPut(@Validated @RequestBody Form form) {
		return new SuccessHandler("success");
	}

	@DeleteMapping
	@ResponseBody
	public SuccessHandler ajaxDelete(@Validation @RequestBody Form form, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
		}
		return new SuccessHandler("success");
	}

	/**
	 * JSR-303 표준에 의해 Collection or Array 타입은 검증에서 제외된다.
	 * 이를 처리하기 위해 {@link Validation} 선언한다. 검증 결과를 자동으로 처리된다.
	 * @param forms
	 * @return
	 */
	@PostMapping(value = "/forms")
	@ResponseBody
	public SuccessHandler ajaxPostForms(@Validation @RequestBody List<Form> forms) {
		return new SuccessHandler("success");
	}

	/**
	 * 검증 결과를 직접 처리하기 위해서는 Argument {@link BindingResult} 를 선언하면 결과를 얻을 수 있다.
	 * @param forms
	 * @param bindingResult
	 * @return
	 */
	@PutMapping(value = "/forms")
	@ResponseBody
	public SuccessHandler ajaxPutForms(@Validation @RequestBody List<Form> forms, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ValidationException(bindingResult);
		}

		return new SuccessHandler("success");
	}
}
