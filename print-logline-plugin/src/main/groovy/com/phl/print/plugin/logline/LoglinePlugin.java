package com.phl.print.plugin.logline;

import com.android.build.gradle.AppExtension;
import com.phl.print.plugin.logline.code.LineNumberLogFactory;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;
import java.util.List;

public class LoglinePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");

        List<String> taskNames = project.getGradle().getStartParameter().getTaskNames();

        for (String taskName : taskNames) {
            if (taskName.contains("Release")) {
                return;
            }
        }

        LineNumberLogFactory.initClass(project);

        appExtension.registerTransform(new LinelogTransform(project), Collections.EMPTY_LIST);

    }

}
