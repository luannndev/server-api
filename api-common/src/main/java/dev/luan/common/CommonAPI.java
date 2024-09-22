package dev.luan.common;

import dev.luan.common.configuration.exception.InvalidConfigurationException;
import dev.luan.common.configuration.impl.DatabaseConfiguration;
import dev.luan.common.mongo.profile.language.repository.TranslationRepository;
import dev.luan.common.mongo.profile.network.NetworkProfileManager;
import dev.luan.common.mongo.profile.network.repository.NetworkProfileRepository;
import eu.koboo.en2do.Credentials;
import eu.koboo.en2do.MongoManager;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.Nullable;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.nio.file.Path;
import java.util.logging.Logger;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonAPI {

    Path dataFolder;

    DatabaseConfiguration databaseConfiguration;

    MongoManager mongoManager;
    RedissonClient redissonClient;

    NetworkProfileRepository networkProfileRepository;
    NetworkProfileManager networkProfileManager;

    TranslationRepository translationRepository;

    static CommonAPI instance;

    @NonNull
    public static CommonAPI instance() {
        if (instance == null) {
            return instance = new CommonAPI();
        }
        return instance;
    }

    public CommonAPI() {
        if (instance != null) {
            return;
        }
        instance = this;
        this.dataFolder = Path.of("plugins/serverapi/common/");

        this.databaseConfiguration = new DatabaseConfiguration();
        this.databaseConfiguration.load(this.dataFolder.resolve("database.yml").toFile());
        if (this.databaseConfiguration.getMongoUri() == null || this.databaseConfiguration.getMongoDatabase() == null) {
            throw new InvalidConfigurationException("Mongo URI and/or Database is not set in the configuration file.");
        }
        if (this.mongoManager == null) {
            this.mongoManager = new MongoManager(Credentials.of(this.databaseConfiguration.getMongoUri(), this.databaseConfiguration.getMongoDatabase()));
        }
        if (this.databaseConfiguration.getRedisUri() == null) {
            throw new InvalidConfigurationException("Redis URI is not set in the configuration file.");
        }
        Config redissonConfig = new Config();
        redissonConfig.useSingleServer().setAddress(this.databaseConfiguration.getRedisUri());
        redissonConfig.useSingleServer().setSubscriptionConnectionPoolSize(1000);
        redissonConfig.useSingleServer().setConnectionPoolSize(1000);
        if (this.databaseConfiguration.getRedisPassword() != null && !this.databaseConfiguration.getRedisPassword().isEmpty() && !this.databaseConfiguration.getRedisPassword().isBlank()) {
            redissonConfig.useSingleServer().setPassword(this.databaseConfiguration.getRedisPassword());
        }
        if (this.redissonClient == null) {
            redissonConfig.setCodec(new JsonJacksonCodec());
            this.redissonClient = Redisson.create(redissonConfig);
        }

        this.networkProfileRepository = mongoManager.create(NetworkProfileRepository.class);
        this.networkProfileManager = new NetworkProfileManager();

        this.translationRepository = mongoManager.create(TranslationRepository.class);

        Logger.getAnonymousLogger().log(java.util.logging.Level.INFO, "Common API initialized");
    }

    @NonNull
    public DatabaseConfiguration databaseConfiguration() {
        if (this.databaseConfiguration == null) {
            this.databaseConfiguration = new DatabaseConfiguration();
            this.databaseConfiguration.load(this.dataFolder.resolve("database.yml").toFile());
        }
        return this.databaseConfiguration;
    }

    @Nullable
    public MongoManager mongoManager() {
        return this.mongoManager;
    }

    @Nullable
    public RedissonClient redissonClient() {
        return this.redissonClient;
    }

    @Nullable
    public NetworkProfileRepository networkProfileRepository() {
        return this.networkProfileRepository;
    }

    @Nullable
    public NetworkProfileManager networkProfileManager() {
        if (this.networkProfileManager == null) {
            this.networkProfileManager = new NetworkProfileManager();
        }
        return this.networkProfileManager;
    }

    @Nullable
    public Path dataFolder() {
        return this.dataFolder;
    }

    public void dataFolder(Path dataFolder) {
        this.dataFolder = dataFolder;
    }

    @Nullable
    public TranslationRepository translationRepository() {
        return this.translationRepository;
    }

}