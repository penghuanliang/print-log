package com.phl.print.plugin.logline;

import com.android.build.gradle.AppExtension;
import com.phl.print.plugin.logline.code.LineNumberLogFactory;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoglinePlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        AppExtension appExtension = (AppExtension) project.getProperties().get("android");

        project.getExtensions().create("printExt", PrintExt.class);

        List<String> taskNames = project.getGradle().getStartParameter().getTaskNames();

        String runTaskName = null;
        for (String taskName : taskNames) {
            if (taskName.contains("Release")) {
                return;
            }
            runTaskName = taskName;
        }

        if (null == runTaskName || runTaskName.length() <= 0) {
            return;
        }

        Pattern p = Pattern.compile("assemble[\\s\\S]*Debug$");
        Matcher matcher = p.matcher(runTaskName);
        if (matcher.find()) {
            LineNumberLogFactory.initClass(project, runTaskName);
        }


        project.afterEvaluate(partProject -> {
            PrintExt printExt = partProject.getExtensions().getByType(PrintExt.class);
            PrintExtUtil.getInstance().init(printExt);
        });


        appExtension.registerTransform(new LinelogTransform(project), Collections.EMPTY_LIST);
    }

}
