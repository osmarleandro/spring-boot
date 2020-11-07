package org.springframework.boot.actuate.info;

public interface IEnvironmentInfoContributor {

	void contribute(Info.Builder builder);

}