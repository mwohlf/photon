package net.wohlfart.photon.shader;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TemplateParser {

	private static final String VAR_REGEX = "\\$\\{([^}]+)\\}";
    private static final Pattern VAR_PATTERN = Pattern.compile(VAR_REGEX);

	private final String content;

	private final HashMap<String, String> keyValue = new HashMap<String, String>();

	public TemplateParser(String content) {
		this.content = content;
		this.keyValue.clear();
	}

	public void add(String key, String value) {
		keyValue.put(key, value); // TODO: check for duplicates
	}

	public String render() {
        final Matcher matcher = VAR_PATTERN.matcher(content);
        String result = content;
        while (matcher.find()) {
            String found = matcher.group(1);
            String newVal = keyValue.get(found);
            result = result.replaceFirst(VAR_REGEX, newVal);
        }
		return result;
	}

}
