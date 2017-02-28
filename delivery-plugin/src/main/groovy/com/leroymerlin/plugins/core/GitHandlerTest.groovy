package com.leroymerlin.plugins.core

import com.leroymerlin.plugins.DeliveryPluginExtension
import com.leroymerlin.plugins.cli.Executor
import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * Created by alexandre on 06/02/2017.
 */
class GitHandlerTest extends Executor implements BaseScmAdapter {

    @Override
    void setup(Project project, DeliveryPluginExtension extension) {
        if (!"git --version".execute().text.contains('git version')) {
            throw new GradleException("Git not found, install Git before continue")
        } else {
            if (!new File('.git').exists())
                println('Init Git on the folder')

            System.setProperty('SCM_PASSWORD', 'Test')
            System.setProperty('SCM_USER', 'Test')

            String password = System.getProperty('SCM_PASSWORD')
            String user = System.getProperty('SCM_USER')
            if (password == null || user == null) {
                throw new GradleException('Please login')
            } else {
                println('Use git cache')
            }
        }
    }

    @Override
    void release() {
        println('release')
    }

    @Override
    String addAllFiles() {
        return println('addAllFiles')
    }

    @Override
    String commit(String comment) {
        return println('commmit ' + comment)
    }

    @Override
    String deleteBranch(String branchName) {
        return println('deleteBranch ' + branchName)
    }

    @Override
    String switchBranch(String branchName, boolean createIfNeeded) {
        return println('switchBranch ' + branchName + createIfNeeded)
    }

    @Override
    String tag(String annotation, String message) {
        return println('tag ' + annotation + message)
    }

    @Override
    String merge(String from) {
        return println('merge ' + from)
    }

    @Override
    String push() {
        return println('push')
    }
}