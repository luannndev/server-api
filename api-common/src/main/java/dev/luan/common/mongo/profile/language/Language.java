package dev.luan.common.mongo.profile.language;

import dev.luan.common.mongo.profile.language.structure.LanguageHolder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum Language implements LanguageHolder {

    GERMAN("de_DE"),
    ENGLISH("en_US");

    String key;

    Language(String key) {
        this.key = key;
    }

    public static Language getLanguage(String key) {
        for (Language language : values()) {
            if (language.getKey().equals(key)) {
                return language;
            }
        }
        return null;
    }

    @Override
    public Language getLanguage() {
        return this;
    }
}