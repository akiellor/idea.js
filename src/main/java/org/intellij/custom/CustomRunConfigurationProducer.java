package org.intellij.custom;

import com.intellij.execution.Location;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.ClassUtil;
import org.dynjs.Config;
import org.dynjs.runtime.DynJS;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.JSFunction;
import org.jetbrains.annotations.Nullable;

public class CustomRunConfigurationProducer extends RuntimeConfigurationProducer implements Cloneable {
    private PsiFile containingFile;

    public CustomRunConfigurationProducer() {
        super(ApplicationConfigurationType.getInstance());
    }

    @Override
    public PsiElement getSourceElement() {
        return containingFile;
    }

    @Nullable
    @Override
    protected RunnerAndConfigurationSettings createConfigurationByElement(Location location, final ConfigurationContext context) {
        Module module = context.getModule();
        if (module == null) { return null; }

        Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());

        Config config = new Config(this.getClass().getClassLoader());
        config.setCompileMode(Config.CompileMode.OFF);
        DynJS runtime = new DynJS(config);
        runtime.getExecutionContext().getGlobalObject().addLoadPath(context.getProject().getBasePath());

        JSFunction function = (JSFunction) runtime.evaluate("require('ide/ide').configuration");
        final DynObject object = (DynObject) runtime.getExecutionContext().call(function, (Object)null, context);
        if (object == null) { return null; }

        Configuration configuration = new DynObjectConfiguration(object);

        PsiClass mainClass = ClassUtil.findPsiClass(PsiManager.getInstance(context.getProject()), configuration.main());
        if(mainClass == null) { return null; }

        RunnerAndConfigurationSettings settings = cloneTemplateConfiguration(context.getProject(), context);
        ApplicationConfiguration intellijConfiguration = (ApplicationConfiguration) settings.getConfiguration();
        intellijConfiguration.setMainClass(mainClass);
        intellijConfiguration.setProgramParameters(configuration.arguments());
        intellijConfiguration.setName(configuration.name());
        return settings;

    }

    @Override
    public int compareTo(Object o) {
        return PREFERED;
    }
}
