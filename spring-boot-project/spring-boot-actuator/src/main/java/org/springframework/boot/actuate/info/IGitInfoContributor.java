package org.springframework.boot.actuate.info;

public interface IGitInfoContributor {

	void contribute(Info.Builder builder);

}