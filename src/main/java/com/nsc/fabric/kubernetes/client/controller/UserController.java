package com.nsc.fabric.kubernetes.client.controller;

import com.nsc.fabric.kubernetes.client.crds.User;
import com.nsc.fabric.kubernetes.client.crds.UserList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.MixedOperation;
import io.fabric8.kubernetes.client.dsl.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final MixedOperation<User, UserList, Resource<User>> userClient;

    @Autowired
    public UserController(KubernetesClient kubernetesClient) {
        this.userClient = kubernetesClient.customResources(User.class, UserList.class);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return userClient.inNamespace("pratap").list().getItems();
    }

    @RequestMapping(path = "/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getByName(@PathVariable String name) throws Exception {
        Optional<User> userOptional = userClient.inNamespace("pratap").list().getItems().stream()
                .filter(user -> user.getMetadata().getName().equals(name))
                .findFirst();
        return userOptional.orElseThrow(() -> new Exception("user not found"));
    }

    /*
    {
        "metadata":{
            "name" : "postman-user"
        },
        "spec":{
            "name" : "Postman",
            "email" : "postman@gmail.com",
            "phone" : "1234567890"
        }
    }
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody User user) {
        return userClient.inNamespace("pratap").create(user);
    }

    @RequestMapping(path = "/{name}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String name) {
        userClient.inNamespace("pratap").withName(name).delete();
    }
}
