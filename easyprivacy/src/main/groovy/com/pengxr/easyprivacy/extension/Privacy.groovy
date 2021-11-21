package com.pengxr.easyprivacy.extension

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

/**
 * EasyPrivacy extension.
 *
 * Created by pengxr on 2021/9/10.
 */
class Privacy {

    PrivacyFeature privacyFeature

    Privacy(Project project) {
        privacyFeature = new PrivacyFeature(project)
    }

    void feature(Action<PrivacyFeature> action) {
        action.execute(privacyFeature)
    }

    void feature(Closure closure) {
        ConfigureUtil.configure(closure, privacyFeature)
    }

    static Privacy getConfig(Project project) {
        Privacy extension = project.getExtensions().findByType(Privacy.class)
        if (null == extension) {
            extension = new Privacy(project)
        }
        return extension
    }
}