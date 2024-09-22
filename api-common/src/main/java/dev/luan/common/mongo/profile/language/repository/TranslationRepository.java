package dev.luan.common.mongo.profile.language.repository;

import dev.luan.common.mongo.profile.language.model.Translatable;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

@Collection("translations")
public interface TranslationRepository extends Repository<Translatable, String> {

}