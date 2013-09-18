package org.intellij.ideajs.runconfiguration;

import com.google.common.base.Optional;
import org.dynjs.runtime.DynObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScriptedProducerTest {
    @Mock
    JavascriptRuntime runtime;
    @Mock DynObject object;
    @Mock Object context;

    @Test
    public void shouldRequireIdeConfiguration(){
        when(runtime.call("require('ide/ide').configuration", context)).thenReturn(object);

        ScriptedProducer producer = new ScriptedProducer(runtime);

        assertThat(producer.produce(context)).isEqualTo(Optional.of(new DynObjectConfiguration(object)));
    }

    @Test
    public void shouldReturnAbsentWhenConfigurationNotPresent(){
        when(runtime.call("require('ide/ide').configuration", context)).thenReturn(null);

        ScriptedProducer producer = new ScriptedProducer(runtime);

        assertThat(producer.produce(context)).isEqualTo(Optional.absent());
    }
}
