package com.nsc.fabric.kubernetes.client.crds;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.fabric8.kubernetes.api.model.KubernetesResource;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserSpec implements KubernetesResource {
    @JsonProperty("name")
    private String name;

    @JsonProperty("name")
    private String email;

    @JsonProperty("name")
    private String phone;
}
