package org.springframework.boot.actuate.health;

import java.util.Iterator;

interface ICompositeHealthContributorReactiveAdapter {

	Iterator<NamedContributor<ReactiveHealthContributor>> iterator();

	ReactiveHealthContributor getContributor(String name);

}