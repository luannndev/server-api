package dev.luan.common.mongo.profile;

import dev.luan.common.CommonAPI;
import eu.koboo.en2do.repository.Repository;
import org.redisson.api.RMapCache;

import java.util.Set;

public abstract class ProfileManager<E, R extends Repository<E, ID>, ID> {

    protected R repository;
    protected RMapCache<ID, E> cache;

    public ProfileManager(R repository) {
        this.repository = repository;
        this.cache = CommonAPI.instance().redissonClient().getMapCache(repository.getClass().getSimpleName() + "_cache");
    }

    public abstract E onCreate(ID id);

    public abstract E onLoad(E entity);

    public abstract E onSave(E entity);

    public abstract E onUnload(E entity);

    public boolean save(ID id, E entity) {
        entity = onSave(entity);
        cache.put(id, entity);
        return repository.save(entity);
    }

    public E findById(ID id) {
        E entity = cache.get(id);
        if (entity == null) {
            entity = repository.findFirstById(id);
            if (entity != null) {
                entity = onLoad(entity);
                cache.put(id, entity);
                return entity;
            }
            entity = onCreate(id);
            cache.put(id, entity);
        }
        return entity;
    }

    public void unload(ID id) {
        E entity;
        if (cache.containsKey(id)) {
            entity = cache.get(id);
            entity = onUnload(entity);
            cache.remove(id);
            repository.save(entity);
            return;
        }
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void delete(E entity) {
        repository.delete(entity);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public boolean existsById(ID id) {
        return repository.existsById(id);
    }

    public long count() {
        return repository.countAll();
    }

    public Iterable<E> findAll() {
        return repository.findAll();
    }

    private <K, V> K getKey(Set<K> keys, V value) {
        for (K key : keys) {
            E cachedValue = cache.get(key);
            if (cachedValue != null && cachedValue.equals(value)) {
                return key;
            }
        }
        return null;
    }
}