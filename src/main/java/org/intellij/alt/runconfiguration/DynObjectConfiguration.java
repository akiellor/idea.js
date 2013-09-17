package org.intellij.alt.runconfiguration;

import org.dynjs.runtime.DynObject;

public class DynObjectConfiguration implements Configuration {
    private DynObject object;

    public DynObjectConfiguration(DynObject object) {
        this.object = object;
    }

    public String main() {
        return (String) object.get("main");
    }

    public String arguments() {
        return (String) object.get("arguments");
    }

    public String name() {
        return (String) object.get("name");
    }
}
