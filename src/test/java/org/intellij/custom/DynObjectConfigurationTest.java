package org.intellij.custom;

import org.dynjs.runtime.DynObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DynObjectConfigurationTest {
    @Mock DynObject object;

    @Test
    public void shouldPullStringMainFromDynObject(){
        when(object.get("main")).thenReturn("org.foo.Main");

        DynObjectConfiguration config = new DynObjectConfiguration(object);

        assertThat(config.main()).isEqualTo("org.foo.Main");
    }

    @Test
    public void shouldPullStringArgumentsFromDynObject(){
        when(object.get("arguments")).thenReturn("--foo --bar");

        DynObjectConfiguration config = new DynObjectConfiguration(object);

        assertThat(config.arguments()).isEqualTo("--foo --bar");
    }

    @Test
    public void shouldPullStringNameFromDynObject(){
        when(object.get("name")).thenReturn("foo");

        DynObjectConfiguration config = new DynObjectConfiguration(object);

        assertThat(config.name()).isEqualTo("foo");
    }
}
