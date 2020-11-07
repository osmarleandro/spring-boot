package org.springframework.boot.actuate.management;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.WebEndpointResponse;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;

public interface IHeapDumpWebEndpoint {

	WebEndpointResponse<Resource> heapDump(Boolean live);

}