package org.springframework.boot.actuate.scheduling;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.scheduling.ScheduledTasksEndpoint.ScheduledTasksReport;

public interface IScheduledTasksEndpoint {

	ScheduledTasksReport scheduledTasks();

}