package hello;

import java.io.File;
import java.util.Map;
import java.util.stream.LongStream;

import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.Ehcache;
import org.ehcache.core.statistics.CacheStatistics;
import org.ehcache.core.statistics.TierStatistics;
import org.ehcache.impl.internal.statistics.DefaultStatisticsService;

public class EHcache {

	private static int foundPre;
	private static int foundPo;

	public static void main(String[] args) {

		DefaultStatisticsService service = new DefaultStatisticsService();
		PersistentCacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
				.with(CacheManagerBuilder.persistence("c:\\ehcache3" + File.separator + "myData"))
				.withDefaultSizeOfMaxObjectGraph(100000).using(service)
				.withCache("preConfigured",
						CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
								ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10000, EntryUnit.ENTRIES).disk(10,
										MemoryUnit.MB, true)))
				.build();
		cacheManager.init();

		Cache<Long, String> preConfigured = cacheManager.getCache("preConfigured", Long.class, String.class);

		Cache<Long, String> myCache = cacheManager.createCache("myCache",
				CacheConfigurationBuilder
						.newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder
								.newResourcePoolsBuilder().heap(100, EntryUnit.ENTRIES).disk(2, MemoryUnit.MB, true))
						.build());
		LongStream.rangeClosed(1, 10000).forEach(i -> {
			String string = myCache.get(i);
			if (string != null)
				foundPre++;
		});
		Ehcache e = (Ehcache) myCache;
		LongStream.rangeClosed(1, 10000).forEach(i -> {
			myCache.put(i, "da one!" + i);
		});
		LongStream.rangeClosed(1, 100).forEach(i -> {
			String string = myCache.get(i);
		});
		LongStream.rangeClosed(1, 100).forEach(i -> {
			String string = myCache.get(i);
		});
		LongStream.rangeClosed(1, 100).forEach(i -> {
			String string = myCache.get(i);
		});
		LongStream.rangeClosed(1, 10000).forEach(i -> {
			String string = myCache.get(i);
			if (string != null)
				foundPo++;
		});
		System.out.println("Pre " + foundPre);
		System.out.println("Po " + foundPo);
		String value = myCache.get(1L);
		System.out.println(service);
		CacheStatistics cacheStatistics = service.getCacheStatistics("myCache");
		Map<String, TierStatistics> tierStatistics = cacheStatistics.getTierStatistics();
		TierStatistics tierStatistics2 = tierStatistics.get("OnHeap");
		long mappings = tierStatistics2.getMappings();
		TierStatistics tierStatistics3 = tierStatistics.get("Disk");
		long mappings2 = tierStatistics3.getMappings();
		// cacheManager.removeCache("preConfigured");
		cacheManager.close();

	}

}
