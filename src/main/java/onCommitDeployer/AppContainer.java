package onCommitDeployer;

import com.google.common.collect.ImmutableMap;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

public class AppContainer {
    private final DockerClient docker;
    private String currentImageId;
    private String currentContainerId;

    public AppContainer() throws DockerCertificateException {
        docker = DefaultDockerClient.fromEnv().build();
    }

    public void rebuild() throws DockerException, InterruptedException, IOException {
        if(currentContainerId != null){
            docker.killContainer(currentContainerId);
            docker.removeContainer(currentContainerId);
        }

        if(currentImageId != null){
            docker.removeImage(currentImageId);
        }

        currentImageId = docker.build(Paths.get("./docker"), "app-for-deploy");
        ContainerConfig containerConfig = ContainerConfig.builder()
                .hostConfig(HostConfig.builder()
                        .portBindings(ImmutableMap.of("8000/tcp", Arrays.asList(PortBinding.of("", 8000))))
                        .build())
                .exposedPorts("8000/tcp")
                .image(currentImageId)
                .build();
        ContainerCreation container = docker.createContainer(containerConfig);
        currentContainerId = container.id();
    }
}
