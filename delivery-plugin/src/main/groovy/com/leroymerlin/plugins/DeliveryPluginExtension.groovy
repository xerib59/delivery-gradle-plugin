package com.leroymerlin.plugins

import com.leroymerlin.plugins.core.BaseScmAdapter
import com.leroymerlin.plugins.core.GitAdapter
import com.leroymerlin.plugins.core.configurators.ProjectConfigurator
import com.leroymerlin.plugins.entities.Flow
import com.leroymerlin.plugins.entities.RegistryProperty
import com.leroymerlin.plugins.entities.SigningProperty
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class DeliveryPluginExtension {

    private ProjectConfigurator mConfigurator
    private BaseScmAdapter mScmAdapter
    NamedDomainObjectContainer<SigningProperty> signingProperties
    NamedDomainObjectContainer<RegistryProperty> dockerRegistries
    NamedDomainObjectContainer<Flow> flowsContainer
    Project project
    DeliveryPlugin plugin

    DeliveryPluginExtension(Project project, DeliveryPlugin deliveryPlugin) {
        this.project = project
        this.plugin = deliveryPlugin
        this.flowsContainer = project.container(Flow, { String name ->
            return Flow.newInstance(name, this)
        })
        this.signingProperties = project.container(SigningProperty)
        this.dockerRegistries = project.container(RegistryProperty)
    }

    def archiveRepositories = project.ext.properties.containsKey('archiveRepositories') ? project.ext.archiveRepositories : {
    }


    void dockerRegistries(@DelegatesTo(RegistryProperty) Action<? super NamedDomainObjectContainer<RegistryProperty>> action) {
        action.execute(dockerRegistries)
    }


    void setEnableReleaseGitFlow(boolean enable) {
        this.plugin.enableReleaseGitFlow(enable)
    }

    boolean autoLinkSubModules = false

    String[] linkedSubModules = []

    void signingProperties(@DelegatesTo(SigningProperty) Action<? super NamedDomainObjectContainer<SigningProperty>> action) {
        action.execute(signingProperties)
        signingProperties.each {
            SigningProperty signingProperty ->
                getConfigurator().applySigningProperty(signingProperty)
        }
    }

    def flows(@DelegatesTo(Flow) Action<? super NamedDomainObjectContainer<Flow>> action) {
        action.execute(flowsContainer)
    }

    void setConfigurator(LinkedHashMap configurator) {
        setConfigurator(configurator as ProjectConfigurator)
    }

    void setConfigurator(ProjectConfigurator configurator) {
        this.mConfigurator = configurator
        this.mConfigurator.setup(project, this)
        this.mConfigurator.applyProperties()
        this.signingProperties.each {
            SigningProperty signingProperty ->
                this.configurator.applySigningProperty(signingProperty)
        }
    }

    ProjectConfigurator getConfigurator() {
        return mConfigurator
    }

    void setScmAdapter(BaseScmAdapter scmAdapter) {
        this.mScmAdapter = scmAdapter
        this.mScmAdapter.setup(project, this)
    }

    BaseScmAdapter getScmAdapter() {
        if (mScmAdapter == null) {
            setScmAdapter(new GitAdapter())
        }
        return mScmAdapter
    }
}
