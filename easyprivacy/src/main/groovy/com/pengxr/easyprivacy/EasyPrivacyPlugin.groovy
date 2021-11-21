package com.pengxr.easyprivacy

import com.pengxr.easyprivacy.bit64.Bit64Feature
import com.pengxr.easyprivacy.extension.Privacy
import com.pengxr.easyprivacy.extension.PrivacyFeature
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * A Gradle plugin to help developers quickly solve privacy problems or apply market policies.
 *
 * Created by pengxr on 2021/9/10.
 */
class EasyPrivacyPlugin implements Plugin<Project> {

    public static final String PRIVACY_EXTENSION_NAME = "privacy"

    @Override
    void apply(Project project) {
        // 1. Apply plugin extensions.
        applyExtension(project)
        // 2. Apply privacy features.
        applyFeature(project)
    }

    private void applyExtension(Project project) {
        project.extensions.create(PRIVACY_EXTENSION_NAME, Privacy, project)
    }

    private void applyFeature(Project project) {
        project.afterEvaluate {
            // 2. Get extension config.
            Privacy privacy = Privacy.getConfig(project)
            PrivacyFeature privacyFeature = privacy.privacyFeature
            if (null == privacyFeature) {
                return
            }
            // 2.1 Apply 64-bit architectures
            applyBit64Feature(project)
        }
    }

    private void applyBit64Feature(Project project) {
        Bit64Feature feature = new Bit64Feature()
        feature.apply(project)
    }
}