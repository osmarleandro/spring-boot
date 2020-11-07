package org.springframework.boot.actuate.health;

public interface ISimpleHttpCodeStatusMapper {

	int getStatusCode(Status status);

}