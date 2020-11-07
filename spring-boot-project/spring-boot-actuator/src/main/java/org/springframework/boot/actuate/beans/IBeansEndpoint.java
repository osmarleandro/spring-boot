package org.springframework.boot.actuate.beans;

import org.springframework.boot.actuate.beans.BeansEndpoint.ApplicationBeans;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

public interface IBeansEndpoint {

	ApplicationBeans beans();

}