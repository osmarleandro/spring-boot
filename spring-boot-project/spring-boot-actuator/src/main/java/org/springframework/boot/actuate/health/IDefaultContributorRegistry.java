package org.springframework.boot.actuate.health;

import java.util.Iterator;

interface IDefaultContributorRegistry<C> {

	void registerContributor(String name, C contributor);

	C unregisterContributor(String name);

	C getContributor(String name);

	Iterator<NamedContributor<C>> iterator();

}