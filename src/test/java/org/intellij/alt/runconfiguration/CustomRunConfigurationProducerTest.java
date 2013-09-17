package org.intellij.alt.runconfiguration;

import com.intellij.openapi.project.Project;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;
import static org.intellij.alt.runconfiguration.CustomRunConfigurationProducer.JavascriptRuntime;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomRunConfigurationProducerTest {
    @Rule public FilesystemRule filesystem = new FilesystemRule(new File("."));

    @Mock Project project;

    @Test
    public void shouldCallSpecifiedFunction(){
        filesystem.file("foo.js", "exports.foo = function() { return 2; };");

        when(project.getBasePath()).thenReturn(filesystem.root());

        JavascriptRuntime runtime = new JavascriptRuntime(project);

        Object result = runtime.call("require('foo').foo");

        assertThat(result).isEqualTo(2L);
    }
}
