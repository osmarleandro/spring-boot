package org.springframework.boot.actuate.endpoint.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

public interface IEndpointMBean {

	MBeanInfo getMBeanInfo();

	Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException;

	Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException;

	void setAttribute(Attribute attribute)
			throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException;

	AttributeList getAttributes(String[] attributes);

	AttributeList setAttributes(AttributeList attributes);

}