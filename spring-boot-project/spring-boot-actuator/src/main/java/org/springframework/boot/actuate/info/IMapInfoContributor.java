package org.springframework.boot.actuate.info;

public interface IMapInfoContributor {

	void contribute(Info.Builder builder);

}