package org.springframework.boot.actuate.health;

import java.util.Set;

public interface ISimpleStatusAggregator {

	Status getAggregateStatus(Set<Status> statuses);

}