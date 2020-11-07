package org.springframework.boot.actuate.info;

public interface ISimpleInfoContributor {

	void contribute(Info.Builder builder);

}