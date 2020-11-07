package org.springframework.boot.actuate.endpoint.invoke.convert;

import java.time.OffsetDateTime;

public interface IIsoOffsetDateTimeConverter {

	OffsetDateTime convert(String source);

}