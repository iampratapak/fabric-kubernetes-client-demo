package com.nsc.fabric.kubernetes.client.controller;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/namespace")
public class NamespaceController {

    private final KubernetesClient kubernetesClient;

    @Autowired
    public NamespaceController(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public NamespaceList getAll() {
        return kubernetesClient.namespaces().list();
    }

    @RequestMapping(path = "/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Namespace getByName(@PathVariable String name) throws Exception {
        Optional<Namespace> namespaceOptional = kubernetesClient.namespaces().list().getItems().stream()
                .filter(namespace -> name.equals(namespace.getMetadata().getName()))
                .findFirst();
        return namespaceOptional.orElseThrow(() -> new Exception("namespace not found"));
    }

    /*
    @Payload:
    {
        "metadata" : {
            "name" : "pratap-dev"
        }
    }
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Namespace create(@RequestBody Namespace namespace) {
        return kubernetesClient.namespaces().create(namespace);
    }

    @RequestMapping(path = "{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String name) {
        kubernetesClient.namespaces().withName(name).delete();
    }
}
