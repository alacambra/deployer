package com.poolingpeople.deployer.boundary;

import com.poolingpeople.deployer.dockerapi.boundary.ContainerInfo;
import com.poolingpeople.deployer.dockerapi.boundary.DockerApi;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.CollectionDataModel;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

/**
 * Created by alacambra on 09.02.15.
 */

@Named
@RequestScoped
public class ContainerController {

    @Inject
    DockerApi api;

    DataModel<ContainerInfo> containers;

    Collection<ContainerInfo> containerInfos;

    public DataModel<ContainerInfo> getContainers(){

        if (containerInfos == null){
            containerInfos = api.listContainers();
            containers = new CollectionDataModel<>(containerInfos);
        }

        return containers;
    }

    public String destroy() {
        ContainerInfo current =  containers.getRowData();
        api.removeContainer(current.getId(), true);
        return "containers-list";
    }

    public String start() {
        System.out.println(containers.getRowIndex());
        ContainerInfo current = containers.getRowData();
        System.out.println(current.getId());
        api.startContainer(current.getId());
        return "containers-list";
    }

    public String stop() {
        ContainerInfo current = containers.getRowData();
        api.stopContainer(current.getId());
        return "containers-list";
    }
}
