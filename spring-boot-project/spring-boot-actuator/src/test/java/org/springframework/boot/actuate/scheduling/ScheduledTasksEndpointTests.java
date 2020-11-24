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

package org.springframework.boot.actuate.scheduling;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint.CronTaskDescription;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint.CustomTriggerTaskDescription;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint.FixedDelayTaskDescription;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint.FixedRateTaskDescription;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint.ScheduledTasksReport;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link ScheduledTasksEndpoint}.
 *
 * @author Andy Wilkinson
 */
class ScheduledTasksEndpointTests {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
			.withUserConfiguration(BaseConfiguration.class);

	@Test
	void cronScheduledMethodIsReported() {
		this.contextRunner.withUserConfiguration(CronScheduledMethod.class)
		.run((context) -> consumer.accept(context.getBean(ScheduledTasksEndpoint.class).scheduledTasks()));
	}

	@Test
	void cronTriggerIsReported() {
		this.contextRunner.withUserConfiguration(CronTriggerTask.class)
		.run((context) -> consumer.accept(context.getBean(ScheduledTasksEndpoint.class).scheduledTasks()));
	}

	@Test
	void fixedDelayScheduledMethodIsReported() {
		this.contextRunner.withUserConfiguration(FixedDelayScheduledMethod.class)
		.run((context) -> consumer.accept(context.getBean(ScheduledTasksEndpoint.class).scheduledTasks()));
	}

	@Test
	void fixedDelayTriggerIsReported() {
		this.contextRunner.withUserConfiguration(FixedDelayTriggerTask.class)
		.run((context) -> consumer.accept(context.getBean(ScheduledTasksEndpoint.class).scheduledTasks()));
	}

	@Test
	void fixedRateScheduledMethodIsReported() {
		this.contextRunner.withUserConfiguration(FixedRateScheduledMethod.class)
		.run((context) -> consumer.accept(context.getBean(ScheduledTasksEndpoint.class).scheduledTasks()));
	}

	@Test
	void fixedRateTriggerIsReported() {
		this.contextRunner.withUserConfiguration(FixedRateTriggerTask.class)
		.run((context) -> consumer.accept(context.getBean(ScheduledTasksEndpoint.class).scheduledTasks()));
	}

	@Test
	void taskWithCustomTriggerIsReported() {
		this.contextRunner.withUserConfiguration(CustomTriggerTask.class)
		.run((context) -> consumer.accept(context.getBean(ScheduledTasksEndpoint.class).scheduledTasks()));
	}

	@Configuration(proxyBeanMethods = false)
	@EnableScheduling
	static class BaseConfiguration {

		@Bean
		ScheduledTasksEndpoint endpoint(Collection<ScheduledTaskHolder> scheduledTaskHolders) {
			return new ScheduledTasksEndpoint(scheduledTaskHolders);
		}

	}

	static class FixedDelayScheduledMethod {

		@Scheduled(fixedDelay = 1, initialDelay = 2)
		void fixedDelay() {

		}

	}

	static class FixedRateScheduledMethod {

		@Scheduled(fixedRate = 3, initialDelay = 4)
		void fixedRate() {

		}

	}

	static class CronScheduledMethod {

		@Scheduled(cron = "0 0 0/3 1/1 * ?")
		void cron() {

		}

	}

	static class FixedDelayTriggerTask implements SchedulingConfigurer {

		@Override
		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			PeriodicTrigger trigger = new PeriodicTrigger(1, TimeUnit.SECONDS);
			trigger.setInitialDelay(2);
			taskRegistrar.addTriggerTask(new FixedDelayTriggerRunnable(), trigger);
		}

	}

	static class FixedRateTriggerTask implements SchedulingConfigurer {

		@Override
		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			PeriodicTrigger trigger = new PeriodicTrigger(2, TimeUnit.SECONDS);
			trigger.setInitialDelay(3);
			trigger.setFixedRate(true);
			taskRegistrar.addTriggerTask(new FixedRateTriggerRunnable(), trigger);
		}

	}

	static class CronTriggerTask implements SchedulingConfigurer {

		@Override
		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			taskRegistrar.addTriggerTask(new CronTriggerRunnable(), new CronTrigger("0 0 0/6 1/1 * ?"));
		}

	}

	static class CustomTriggerTask implements SchedulingConfigurer {

		private static final Trigger trigger = (context) -> new Date();

		@Override
		public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
			taskRegistrar.addTriggerTask(new CustomTriggerRunnable(), trigger);
		}

	}

	static class CronTriggerRunnable implements Runnable {

		@Override
		public void run() {

		}

	}

	static class FixedDelayTriggerRunnable implements Runnable {

		@Override
		public void run() {

		}

	}

	static class FixedRateTriggerRunnable implements Runnable {

		@Override
		public void run() {

		}

	}

	static class CustomTriggerRunnable implements Runnable {

		@Override
		public void run() {

		}

	}

}
