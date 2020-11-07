package org.springframework.boot.actuate.metrics.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.micrometer.core.instrument.Tag;

public interface IDefaultWebMvcTagsProvider {

	Iterable<Tag> getTags(HttpServletRequest request, HttpServletResponse response, Object handler,
			Throwable exception);

	Iterable<Tag> getLongRequestTags(HttpServletRequest request, Object handler);

}