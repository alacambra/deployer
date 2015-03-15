package com.poolingpeople.deployer.scenario.boundary;

import com.poolingpeople.deployer.boundary.DbSnapshotManagerFacade;
import com.poolingpeople.deployer.dockerapi.boundary.ContainerInfo;
import com.poolingpeople.deployer.dockerapi.boundary.DockerApi;

import javax.enterprise.context.RequestScoped;
import javax.faces.model.CollectionDataModel;
import javax.faces.model.DataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by alacambra on 04.03.15.
 */
@Named
@RequestScoped
public class DbSnapshotController {

    DataModel<ContainerInfo> containers;

    @Inject
    DockerApi api;

    @Inject
    DbSnapshotManagerFacade dbSnapshotManagerFacade;

    String instanceName;
    String dbSnapshotName;

    public String getDbSnapshotName() {
        return dbSnapshotName;
    }

    public DataModel<ContainerInfo> getContainers(){
        Collection<ContainerInfo> containerInfos =
                api.listContainers().stream()
                        .filter(c -> c.getNames().stream().anyMatch(n -> n.toLowerCase().contains("neo")))
                        .collect(Collectors.toList());

        containers = new CollectionDataModel<>(containerInfos);
        return containers;
    }

    public void setDbSnapshotName(String dbSnapshotName) {
        this.dbSnapshotName = dbSnapshotName;
    }

    public Collection<String> getCurrentDbInstances(){
        Collection<ContainerInfo> containersInfo = api.listContainers();
        Collection<Collection<String>> s = containersInfo.stream().map(c -> c.getNames()).collect(Collectors.toList());
        return s.stream().map(s1 -> s1.stream().collect(Collectors.joining())).collect(Collectors.toList());
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void makeSnapshot(){

        Collection<ContainerInfo> containersInfo = api.listContainers();
        ContainerInfo current = containers.getRowData();
        dbSnapshotManagerFacade.makeSnapshot(current, dbSnapshotName);

    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }
}
