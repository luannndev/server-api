package dev.luan.common.mongo.profile.language.model;

import dev.luan.common.mongo.profile.language.Language;
import eu.koboo.en2do.repository.entity.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Translatable {

    @Id
    String key;
    Map<String, String> translations;

    public Translatable(String key) {
        this.key = key;
        this.translations = new HashMap<>();
    }

    public Translatable(String key, String englishDefault) {
        this(key);
        this.translations.put("en_US", englishDefault);
    }

    @NotNull
    public List<String> getListTranslation(String language) {
        if (translations.containsKey(language)) {
            final List<String> result = new ArrayList<>();
            Collections.addAll(result, translations.get(language).split("\n"));
            return result;
        }
        return Collections.emptyList(); //ATTENTION: THIS LIST IS IMMUTABLE!
    }

    @NotNull
    public List<String> getListTranslation(Language language) {
        return getListTranslation(language.getKey());
    }

    @NotNull
    public String getTranslation(String language) {
        if (translations.containsKey(language)) {
            return translations.get(language);
        }
        return translations.get(Language.ENGLISH.getKey());
    }

    @NotNull
    public String getTranslation(Language language) {
        return getTranslation(language.getKey());
    }

    public boolean hasTranslation(String language) {
        if (translations.containsKey(language)) {
            return !translations.get(language).isEmpty();
        }
        return false;
    }

    public boolean hasTranslation(Language language) {
        return hasTranslation(language.getKey());
    }

    public void updateTranslation(String language, String update) {
        translations.put(language, update);
    }

    public void updateTranslation(Language language, String update) {
        updateTranslation(language.getKey(), update);
    }
}