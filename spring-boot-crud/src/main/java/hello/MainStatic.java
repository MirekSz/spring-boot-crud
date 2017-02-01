package hello;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

public class MainStatic {
	public static void main(String[] args) {
		CacheManager cacheManager = CacheManager.getInstance();
		CacheConfiguration cacheConfiguration = new CacheConfiguration().name("myCache").maxElementsInMemory(100)
				.timeToLiveSeconds(20);
		cacheConfiguration.setEternal(true);
		cacheConfiguration.setOverflowToDisk(false);
		cacheConfiguration.setDiskPersistent(false);
		cacheConfiguration.setStatistics(true);
		cacheConfiguration.setCopyOnRead(true);
		cacheManager.addCache(new Cache(cacheConfiguration));

		Cache cache = cacheManager.getCache("myCache");
		Element element = new Element("mirek", 12);
		cache.put(element);
		cache.get("mirek");
	}
}
