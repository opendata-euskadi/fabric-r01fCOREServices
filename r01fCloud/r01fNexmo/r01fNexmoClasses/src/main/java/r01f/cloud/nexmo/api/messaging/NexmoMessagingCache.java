package r01f.cloud.nexmo.api.messaging;

import java.util.concurrent.ExecutionException;

import javax.inject.Singleton;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheBuilderSpec;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.cloud.nexmo.model.state.NexmoMessagingState;

@Singleton
@Accessors(prefix = "_")
@Slf4j
public abstract class NexmoMessagingCache<K, S extends NexmoMessagingState> {
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter
	private final LoadingCache<K, S> _messagingCache;

/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public NexmoMessagingCache(CacheLoader<K, S> loader) {
		_messagingCache = CacheBuilder.newBuilder()
									  .build(loader);
	}
	
	public NexmoMessagingCache(CacheBuilderSpec spec, CacheLoader<K, S> loader, int maximumSize) {
		_messagingCache = CacheBuilder.from(spec)
									  .build(loader);
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	
	public S getValue(K key) throws ExecutionException {
		log.debug("Retrieving value with key {}", key);
		return _messagingCache.get(key);
	}
}
