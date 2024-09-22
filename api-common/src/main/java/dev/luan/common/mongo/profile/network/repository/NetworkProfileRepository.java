package dev.luan.common.mongo.profile.network.repository;

import dev.luan.common.mongo.profile.network.model.NetworkProfile;
import eu.koboo.en2do.repository.Collection;
import eu.koboo.en2do.repository.Repository;

import java.util.List;
import java.util.UUID;

@Collection("network_profiles")
public interface NetworkProfileRepository extends Repository<NetworkProfile, UUID> {

    List<NetworkProfile> findManyByStatus(String status);

}