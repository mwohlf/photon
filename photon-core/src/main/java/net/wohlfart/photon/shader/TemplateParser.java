package net.wohlfart.photon.shader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TemplateParser {
	protected static final Logger LOGGER = LoggerFactory.getLogger(TemplateParser.class);

	private static final String VAR_REGEX = "\\$\\{([^}]+)\\}";
    private static final Pattern VAR_PATTERN = Pattern.compile(VAR_REGEX);

	private final String content;

	private final HashMap<String, String> keyValueMap = new HashMap<String, String>();

	public TemplateParser(String content) {
		this.content = content;
		this.keyValueMap.clear();
	}

	public void add(String key, String value) {
		keyValueMap.put(key, value); // TODO: check for duplicates
	}

	public String render() {
        final Matcher matcher = VAR_PATTERN.matcher(content);
        String result = content;
        while (matcher.find()) {
            String found = matcher.group(1);
            String newVal = keyValueMap.get(found);
            if (newVal == null) {
            	throw new IllegalArgumentException("value for key '" + found + "' is null or not found in map,"
            			+ " available keys are " + Arrays.toString(keyValueMap.keySet().toArray()));
            }
            LOGGER.debug("replacing '{}' with '{}' in shader code", found, newVal);
            result = result.replaceFirst(VAR_REGEX, newVal);
        }
        LOGGER.debug("processed shader:\n"
        		+ "----------------- ------------------\n"
        		+ " '{}'\n"
        		+ "----------------- ------------------\n", result);
		return result;
	}

}
