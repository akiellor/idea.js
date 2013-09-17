package org.intellij.alt.runconfiguration;

import com.google.common.base.Optional;
import org.dynjs.runtime.DynObject;

public class ScriptedProducer {
    private final ProducerShim.JavascriptRuntime runtime;

    public ScriptedProducer(ProducerShim.JavascriptRuntime runtime) {
        this.runtime = runtime;
    }

    public Optional<? extends Configuration> produce(Object context) {
        DynObject object = (DynObject) runtime.call("require('ide/ide').configuration", context);
        if (object == null) { return Optional.absent(); }
        return Optional.fromNullable(new DynObjectConfiguration(object));
    }
}
