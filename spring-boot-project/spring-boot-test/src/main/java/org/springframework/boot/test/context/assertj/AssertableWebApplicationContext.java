/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.test.context.assertj;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.function.Supplier;

import javax.servlet.Filter;

import org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * A {@link WebApplicationContext} that additionally supports AssertJ style assertions.
 * Can be used to decorate an existing servlet web application context or an application
 * context that failed to start.
 * <p>
 * See {@link ApplicationContextAssertProvider} for more details.
 *
 * @author Phillip Webb
 * @since 2.0.0
 * @see WebApplicationContextRunner
 * @see WebApplicationContext
 */
public interface AssertableWebApplicationContext
		extends ApplicationContextAssertProvider<ConfigurableWebApplicationContext>, ConfigurableWebApplicationContext {

	/**
	 * Factory method to create a new {@link AssertableWebApplicationContext} instance.
	 * @param contextSupplier a supplier that will either return a fully configured
	 * {@link ConfigurableWebApplicationContext} or throw an exception if the context
	 * fails to start.
	 * @return a {@link AssertableWebApplicationContext} instance
	 */
	static AssertableWebApplicationContext get(Supplier<? extends ConfigurableWebApplicationContext> contextSupplier) {
		return ApplicationContextAssertProvider.get(AssertableWebApplicationContext.class,
				ConfigurableWebApplicationContext.class, contextSupplier);
	}

	public default MeterRegistry getInitializedMeterRegistry(String... urls)
			throws Exception {
		assertThat(this).hasSingleBean(FilterRegistrationBean.class);
		Filter filter = getBean(FilterRegistrationBean.class).getFilter();
		assertThat(filter).isInstanceOf(WebMvcMetricsFilter.class);
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this).addFilters(filter).build();
		for (String url : urls) {
			mockMvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk());
		}
		return getBean(MeterRegistry.class);
	}

}
