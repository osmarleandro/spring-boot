package org.springframework.boot.actuate.management;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.management.ThreadDumpEndpoint.ThreadDumpDescriptor;

public interface IThreadDumpEndpoint {

	ThreadDumpDescriptor threadDump();

	String textThreadDump();

}