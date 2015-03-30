package com.poolingpeople.deployer.dockerapi.boundary;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alacambra on 30.03.15.
 */
public class ContainerImage {

    Long created;
    String id;
    String parentId;
    List<RepoTag> repoTags;
    Long size;
    Long virtualSize;

    public ContainerImage() {
    }

    public ContainerImage(JsonObject image) {


        created = image.getJsonNumber("Created").longValue();
        id = image.getString("Id");
        parentId = image.getString("ParentId");
        size = image.getJsonNumber("Size").longValue();
        virtualSize = image.getJsonNumber("VirtualSize").longValue();

        repoTags = new ArrayList<>();

        JsonArray tags = image.getJsonArray("RepoTags");
        tags.stream().forEach(tag -> repoTags.add(new RepoTag(((JsonString)tag).getString())));

    }

    public class RepoTag{

        public RepoTag(String name) {
            this.name = name;
        }

        String name;
    }

    public Long getCreate() {
        return created;
    }

    public void setCreate(Long create) {
        this.created = create;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<RepoTag> getRepoTags() {
        return repoTags;
    }

    public void setRepoTags(List<RepoTag> repoTags) {
        this.repoTags = repoTags;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getVirtualSize() {
        return virtualSize;
    }

    public void setVirtualSize(Long virtualSize) {
        this.virtualSize = virtualSize;
    }
}
