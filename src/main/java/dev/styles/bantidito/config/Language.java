package dev.styles.bantidito.config;

import dev.styles.bantidito.Bantidito;
import dev.styles.bantidito.utilities.file.FileConfig;

import java.util.Map;

public class Language {

    private FileConfig languageFile;

    public static String SANCTION_COMMAND_CORRECT_USAGE;

    public static String SANCTION_CUSTOMIZE_EDITING_REASON;
    public static String SANCTION_CUSTOMIZE_EDITING_DURATION;
    public static String SANCTION_CUSTOMIZE_CHANGED_REASON;
    public static String SANCTION_CUSTOMIZE_CHANGED_DURATION;
    public static String SANCTION_CUSTOMIZE_INVALID_DURATION;

    public Language(Bantidito plugin, Map<String, FileConfig> files) {
        files.put("language", new FileConfig(plugin, "language.yml"));

        initialize(files);
    }

    public void initialize(Map<String, FileConfig> files) {
        languageFile = files.get("language");

        SANCTION_COMMAND_CORRECT_USAGE = languageFile.getString("SANCTION_COMMAND.CORRECT_USAGE");

        SANCTION_CUSTOMIZE_EDITING_REASON = languageFile.getString("SANCTION_CUSTOMIZE.EDITING_REASON");
        SANCTION_CUSTOMIZE_EDITING_DURATION = languageFile.getString("SANCTION_CUSTOMIZE.EDITING_DURATION");
        SANCTION_CUSTOMIZE_CHANGED_REASON = languageFile.getString("SANCTION_CUSTOMIZE.CHANGED_REASON");
        SANCTION_CUSTOMIZE_CHANGED_DURATION = languageFile.getString("SANCTION_CUSTOMIZE.CHANGED_DURATION");
        SANCTION_CUSTOMIZE_INVALID_DURATION = languageFile.getString("SANCTION_CUSTOMIZE.INVALID_DURATION");
    }
}
