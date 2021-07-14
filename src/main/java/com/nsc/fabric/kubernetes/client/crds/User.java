package com.nsc.fabric.kubernetes.client.crds;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version(User.VERSION)
@Group(User.GROUP)
public class User extends CustomResource<UserSpec, Void> implements Namespaced {
    public static final String GROUP = "nsc.com";
    public static final String VERSION = "v1";
}
