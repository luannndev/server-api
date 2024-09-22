package dev.luan.common.mongo.profile.language.structure;

import dev.luan.common.mongo.profile.language.Language;

/**
 * Classes that implement LanguageHolder have to provide a language (i.e. a custom player object or others)
 * therefore, this class may be used in all #translate or #translateList methods!
 */
public interface LanguageHolder {

    Language getLanguage();

}