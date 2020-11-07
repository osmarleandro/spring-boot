package org.springframework.boot.actuate.info;

public interface IBuildInfoContributor {

	void contribute(Info.Builder builder);

}