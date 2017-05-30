package org.syaku.spring.apps.validation.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;
import org.syaku.spring.apps.validation.model.Form;
import org.syaku.spring.apps.validation.model.Store;
import org.syaku.spring.apps.validation.support.AppValidationMessage;
import org.syaku.spring.boot.Bootstrap;
import org.syaku.spring.boot.servlet.ServletConfiguration;
import org.syaku.spring.handlers.StatusCode;
import org.syaku.spring.handlers.SuccessHandler;
import org.syaku.spring.validation.ValidationResult;
import org.syaku.spring.validation.ValidationWrapper;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 2017. 5. 30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
		classes = {
				Bootstrap.class,
				ServletConfiguration.class
		}
)
public class FormControllerTest {
	private static final Logger logger = LoggerFactory.getLogger(FormControllerTest.class);

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private Validator validator;

	@Autowired
	private MessageSourceAccessor messageSource;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.build();
	}

	@Test
	public void original_data_object() {
		Form form = new Form();
		List<Store> stores = new ArrayList<>();
		stores.add(new Store("", "good"));
		form.setStores(stores);

		DataBinder binder = new DataBinder(form);
		binder.setValidator(validator);
		binder.validate();
		BindingResult result = binder.getBindingResult();

		if (result.hasErrors()) {
			SuccessHandler successHandler = new SuccessHandler(
					messageSource.getMessage("text.error.validation"),
					true, StatusCode.FormValidation,
					new ValidationResult(
							result,
							new AppValidationMessage(messageSource)
					).getFieldErrors());

			logger.debug("{}", successHandler);
			Assert.assertTrue(successHandler.isError());
		}
	}

	@Test
	public void original_data_collection() {
		Form form = new Form();
		List<Store> stores = new ArrayList<>();
		stores.add(new Store("", "good"));
		form.setStores(stores);

		List<Form> forms = new ArrayList<>();
		forms.add(form);

		ValidationWrapper wrapper = new ValidationWrapper(forms);

		DataBinder binder = new DataBinder(wrapper);
		binder.setValidator(validator);
		binder.validate();
		BindingResult result = binder.getBindingResult();

		if (result.hasErrors()) {
			SuccessHandler successHandler = new SuccessHandler(
					messageSource.getMessage("text.error.validation"),
					true, StatusCode.FormValidation,
					new ValidationResult(
							result,
							new AppValidationMessage(messageSource)
					).getFieldErrors());

			logger.debug("{}", successHandler);
			Assert.assertTrue(successHandler.isError());
		}
	}

	@Test
	public void ajax_object() throws Exception {
		Form form = new Form();
		List<Store> stores = new ArrayList<>();
		stores.add(new Store("", "good"));
		form.setStores(stores);

		ObjectMapper mapper = new ObjectMapper();


		mockMvc.perform(post("/validation")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error", is(true)))
				.andExpect(jsonPath("$.statusCode", is(StatusCode.FormValidation.name())));

		mockMvc.perform(put("/validation")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error", is(true)))
				.andExpect(jsonPath("$.statusCode", is(StatusCode.FormValidation.name())));

		mockMvc.perform(delete("/validation")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(form))
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error", is(true)))
				.andExpect(jsonPath("$.statusCode", is(StatusCode.FormValidation.name())));
	}

	@Test
	public void ajax_collection() throws Exception {
		Form form = new Form();
		List<Store> stores = new ArrayList<>();
		stores.add(new Store("", "good"));
		form.setStores(stores);

		List<Form> forms = new ArrayList<>();
		forms.add(form);

		ObjectMapper mapper = new ObjectMapper();
		String content = mapper.writeValueAsString(forms);


		mockMvc.perform(post("/validation/forms")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error", is(true)))
				.andExpect(jsonPath("$.statusCode", is(StatusCode.FormValidation.name())));

		mockMvc.perform(put("/validation/forms")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(content)
		)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.error", is(true)))
				.andExpect(jsonPath("$.statusCode", is(StatusCode.FormValidation.name())));
	}

}
