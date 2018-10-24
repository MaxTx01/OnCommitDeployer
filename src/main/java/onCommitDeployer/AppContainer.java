package onCommitDeployer;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificateException;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;

import java.io.IOException;
import java.nio.file.Paths;

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
        ContainerCreation container = docker.createContainer(ContainerConfig.builder().image(currentImageId)
                .portSpecs("8000:8000").build());
        currentContainerId = container.id();
    }
}
