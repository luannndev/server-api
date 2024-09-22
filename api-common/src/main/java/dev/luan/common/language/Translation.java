package dev.luan.common.language;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import dev.luan.common.CommonAPI;
import dev.luan.common.mongo.profile.language.Language;
import dev.luan.common.mongo.profile.language.model.Translatable;
import dev.luan.common.mongo.profile.language.repository.TranslationRepository;
import org.jetbrains.annotations.NotNull;

import javax.cache.integration.CacheLoaderException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public final class Translation {
    private static Translation INSTANCE;

    @NotNull
    public static Translation getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Translation();
        }
        return INSTANCE;
    }

    @NotNull
    public static Optional<Translatable> translatable(final String key) {
        try {
            return Optional.of(getInstance().cache.get(key));
        } catch (ExecutionException exception) {
            return Optional.empty();
        }
    }

    @NotNull
    public static String translate(final String language, final String key) {
        var temp = translatable(key);
        return temp.map(translatable -> translatable.getTranslation(language)).orElse("");
    }

    @NotNull
    public static String translate(final Language language, final String key) {
        return translate(language.getKey(), key);
    }

    @NotNull
    public static String translateOrDefault(final String language, final String key, final String englishDefault) {
        var temp = translatable(key); //fetch translatable
        if (temp.isEmpty()) return new Translatable(language, englishDefault).getTranslation(Language.ENGLISH); //failure
        final Translatable translatable = temp.get();

        if (!translatable.hasTranslation(Language.ENGLISH)) { //update if no default present!
            translatable.updateTranslation(Language.ENGLISH, englishDefault);
            getInstance().repository.save(translatable); //save to database!
        }

        return translatable.getTranslation(language);
    }

    @NotNull
    public static String translateOrDefault(final Language language, final String key, final String englishDefault) {
        return translateOrDefault(language.getKey(), key, englishDefault);
    }

    @NotNull
    public static List<String> translateList(final String language, final String key) {
        var temp = translatable(key);
        return temp.map(translatable -> translatable.getListTranslation(language)).orElse(Collections.emptyList());
    }

    @NotNull
    public static List<String> translateList(final Language language, final String key) {
        return translateList(language.getKey(), key);
    }

    @NotNull
    public static List<String> translateListOrDefault(final String language, final String key, final String englishDefault) {
        Optional<Translatable> temp = translatable(key); //fetch translatable
        if (temp.isEmpty()) return new Translatable(language, englishDefault).getListTranslation(Language.ENGLISH); //failure
        final Translatable translatable = temp.get();

        if (!translatable.hasTranslation(Language.ENGLISH)) { //update if no default present!
            translatable.updateTranslation(Language.ENGLISH, englishDefault);
            getInstance().repository.save(translatable); //save to database!
        }

        return translatable.getListTranslation(language);
    }

    @NotNull
    public static List<String> translateListOrDefault(final Language language, final String key, final String englishDefault) {
        return translateListOrDefault(language.getKey(), key, englishDefault);
    }

    private final LoadingCache<String, Translatable> cache;
    private final TranslationRepository repository;

    private Translation() {
        this.repository = CommonAPI.instance().translationRepository();
        this.cache = CacheBuilder.newBuilder()
                .maximumSize(500) //don't cache more than 500 keys at the same time!
                .expireAfterWrite(10, TimeUnit.MINUTES)  //expire keys after least 10 minutes!
                .build(new CacheLoader<String, Translatable>() {
                    @Override
                    public @NotNull Translatable load(@NotNull String key) throws Exception {
                        return findTranslatable(key); //init cache!
                    }
                });
    }

    @NotNull
    private Translatable findTranslatable(String key) {
        Translatable translatable = this.repository.findFirstById(key);
        if (translatable == null) {
            translatable = new Translatable(key); //create empty new one!
        }
        return translatable;
    }
}