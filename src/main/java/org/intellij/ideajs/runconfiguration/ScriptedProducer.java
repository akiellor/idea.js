package org.intellij.ideajs.runconfiguration;

import com.google.common.base.Optional;
import org.dynjs.runtime.DynObject;

public class ScriptedProducer {
    private final JavascriptRuntime runtime;

    public ScriptedProducer(JavascriptRuntime runtime) {
        this.runtime = runtime;
    }

    public Optional<? extends Configuration> produce(Object context) {
        DynObject object = (DynObject) runtime.call("require('idea').configuration", context);
        if (object == null) { return Optional.absent(); }
        return Optional.fromNullable(new DynObjectConfiguration(object));
    }
}
