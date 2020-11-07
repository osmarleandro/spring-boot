package org.springframework.boot.actuate.web.mappings.reactive;

import java.util.List;
import java.util.Set;

import org.springframework.boot.actuate.web.mappings.reactive.RequestMappingConditionsDescription.MediaTypeExpressionDescription;
import org.springframework.boot.actuate.web.mappings.reactive.RequestMappingConditionsDescription.NameValueExpressionDescription;
import org.springframework.web.bind.annotation.RequestMethod;

public interface IRequestMappingConditionsDescription {

	List<MediaTypeExpressionDescription> getConsumes();

	List<NameValueExpressionDescription> getHeaders();

	Set<RequestMethod> getMethods();

	List<NameValueExpressionDescription> getParams();

	Set<String> getPatterns();

	List<MediaTypeExpressionDescription> getProduces();

}