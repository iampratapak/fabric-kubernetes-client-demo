package com.nsc.fabric.kubernetes.client;

import com.nsc.fabric.kubernetes.client.crds.User;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.informers.ResourceEventHandler;
import io.fabric8.kubernetes.client.informers.SharedIndexInformer;
import io.fabric8.kubernetes.client.informers.SharedInformerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {

	private final KubernetesClient kubernetesClient;

	@Autowired
	public Application(KubernetesClient kubernetesClient) {
		this.kubernetesClient = kubernetesClient;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@PostConstruct
	public void eventsInAllNamespace() {
		SharedInformerFactory sharedInformerFactory = kubernetesClient.informers();
		SharedIndexInformer<User> sharedIndexInformer =
				sharedInformerFactory.sharedIndexInformerForCustomResource(User.class, 60*1000L);
		sharedIndexInformer.addEventHandler(new ResourceEventHandler<>() {
			@Override
			public void onAdd(User user) {
				System.out.println("user added " + user.getMetadata().getName());
			}

			@Override
			public void onUpdate(User oldUser, User newUser) {
				System.out.println("user updated " + oldUser.getMetadata().getName());
			}

			@Override
			public void onDelete(User user, boolean deletedFinalStateUnknown) {
				System.out.println("user deleted " + user.getMetadata().getName());
			}
		});
		sharedInformerFactory.startAllRegisteredInformers();
	}

	@PostConstruct
	public void eventsInSpecificNamespace() {
		SharedInformerFactory sharedInformerFactory = kubernetesClient.informers();
		kubernetesClient.customResources(User.class).inNamespace("pratap")
				.inform(new ResourceEventHandler<>() {
					@Override
					public void onAdd(User user) {
						System.out.println("user added " + user.getMetadata().getName());
					}

					@Override
					public void onUpdate(User oldUser, User newUser) {
						System.out.println("user updated " + oldUser.getMetadata().getName());
					}

					@Override
					public void onDelete(User user, boolean deletedFinalStateUnknown) {
						System.out.println("user deleted " + user.getMetadata().getName());
					}
				});
		sharedInformerFactory.startAllRegisteredInformers();
	}
}
