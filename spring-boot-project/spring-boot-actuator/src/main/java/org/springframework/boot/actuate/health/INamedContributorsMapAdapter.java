package org.springframework.boot.actuate.health;

import java.util.Iterator;

interface INamedContributorsMapAdapter<V, C> {

	Iterator<NamedContributor<C>> iterator();

	C getContributor(String name);

}