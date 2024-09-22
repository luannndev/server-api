package dev.luan.common.language;

import dev.luan.common.CommonAPI;
import dev.luan.common.mongo.profile.language.Language;
import dev.luan.common.mongo.profile.language.structure.LanguageHolder;
import dev.luan.common.mongo.profile.network.model.NetworkProfile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

/**
 * Implement this interface in an enum to provide your language keys in a key, default value structure.
 * This class is also used for working with the Translatable object!
 */
public interface KeyHolder {

    @NotNull String key();
    @NotNull String englishDefault(); //has to be provided in english language

    @NotNull
    default Language findLanguage(final UUID uuid) {
        final NetworkProfile globalProfile = CommonAPI.instance().networkProfileManager().findById(uuid);
        return globalProfile.getLanguage();
    }

    @NotNull
    default String translate(final UUID uuid) {
        return Translation.translateOrDefault(findLanguage(uuid), key(), englishDefault());
    }

    @NotNull
    default String translate(final LanguageHolder holder) {
        return Translation.translateOrDefault(holder.getLanguage(), key(), englishDefault());
    }

    @NotNull
    default String translate(final UUID uuid, final Function<String, String> modifier) {
        final String result = translate(uuid);
        return modifier.apply(result);
    }

    @NotNull
    default String translate(final LanguageHolder holder, final Function<String, String> modifier) {
        final String result = translate(holder);
        return modifier.apply(result);
    }

    @NotNull
    default List<String> translateList(final UUID uuid) {
        return Translation.translateListOrDefault(findLanguage(uuid), key(), englishDefault());
    }

    @NotNull
    default List<String> translateList(final LanguageHolder holder) {
        return Translation.translateListOrDefault(holder.getLanguage(), key(), englishDefault());
    }

    @NotNull
    default List<String> translateList(final UUID uuid, final Function<String, String> modifier) {
        String result = translate(uuid); //translation
        result = modifier.apply(result); //transform string
        final List<String> transformed = new ArrayList<>(); //create empty list
        Collections.addAll(transformed, result.split("\n")); //split into different lines
        return transformed;
    }

    @NotNull
    default List<String> translateList(final LanguageHolder holder, final Function<String, String> modifier) {
        String result = translate(holder); //translation
        result = modifier.apply(result); //transform string
        final List<String> transformed = new ArrayList<>(); //create empty list
        Collections.addAll(transformed, result.split("\n")); //split into different lines
        return transformed;
    }
}