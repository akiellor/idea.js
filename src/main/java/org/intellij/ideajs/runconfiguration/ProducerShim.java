package org.intellij.ideajs.runconfiguration;

import com.google.common.base.Optional;
import com.intellij.execution.Location;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.application.ApplicationConfiguration;
import com.intellij.execution.application.ApplicationConfigurationType;
import com.intellij.execution.junit.RuntimeConfigurationProducer;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.ClassUtil;
import org.jetbrains.annotations.Nullable;

public class ProducerShim extends RuntimeConfigurationProducer implements Cloneable {

    private static Logger log = Logger.getInstance(ProducerShim.class);

    @SuppressWarnings("unused")
    private PsiFile containingFile;

    public ProducerShim() {
        super(ApplicationConfigurationType.getInstance());
    }

    @Override
    public PsiElement getSourceElement() {
        return containingFile;
    }

    @Nullable
    @Override
    protected RunnerAndConfigurationSettings createConfigurationByElement(Location location, final ConfigurationContext context) {
        try {
            Module module = context.getModule();
            if (module == null) {
                return null;
            }

            JavascriptRuntime runtime = new JavascriptRuntime(context.getProject());
            ScriptedProducer scriptedProducer = new ScriptedProducer(runtime);

            Optional<? extends Configuration> configuration = scriptedProducer.produce(context);
            if (!configuration.isPresent()) {
                return null;
            }

            PsiClass mainClass = ClassUtil.findPsiClass(PsiManager.getInstance(context.getProject()), configuration.get().main());
            if (mainClass == null) {
                return null;
            }

            RunnerAndConfigurationSettings settings = cloneTemplateConfiguration(context.getProject(), context);
            ApplicationConfiguration intellijConfiguration = (ApplicationConfiguration) settings.getConfiguration();
            intellijConfiguration.setMainClass(mainClass);
            intellijConfiguration.setProgramParameters(configuration.get().arguments());
            intellijConfiguration.setName(configuration.get().name());
            return settings;
        } catch (Exception e) {
            log.error(e);
            return null;
        }

    }

    @Override
    public int compareTo(Object o) {
        return PREFERED;
    }

}
