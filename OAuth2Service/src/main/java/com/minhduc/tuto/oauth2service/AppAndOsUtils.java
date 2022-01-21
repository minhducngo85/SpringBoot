package com.minhduc.tuto.oauth2service;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public final class AppAndOsUtils {

    /**
     * Logger object
     */
    private static final Logger LOGGER = LogManager.getLogger(AppAndOsUtils.class);

    // Private constructor to prevent instantiation
    private AppAndOsUtils() {
	throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String getOsArchitecture() {
	return System.getProperty("os.arch");
    }

    public static String getOperatingSystem() {
	return System.getProperty("os.name");
    }

    public static String getOperatingSystemVersion() {
	return System.getProperty("os.version");
    }

    public static String getIP() throws UnknownHostException {
	InetAddress ip = InetAddress.getLocalHost();
	return ip.getHostAddress();
    }

    public static String getHostname() throws UnknownHostException {
	return InetAddress.getLocalHost().getHostName();
    }

    public static boolean isWindows() {
	return getOperatingSystem().toLowerCase().indexOf("win") >= 0;
    }

    public static boolean isMac() {
	return getOperatingSystem().toLowerCase().indexOf("mac") >= 0;
    }

    public static boolean isLinux() {
	return getOperatingSystem().toLowerCase().indexOf("nix") >= 0 || getOperatingSystem().toLowerCase().indexOf("nux") >= 0
	        || getOperatingSystem().toLowerCase().indexOf("aix") > 0;
    }

    public static boolean isSolaris() {
	return getOperatingSystem().toLowerCase().indexOf("sunos") >= 0;
    }

    /**
     * To read app information from pom.xml
     */
    public static String getSwVersion() {
	MavenXpp3Reader reader = new MavenXpp3Reader();
	Model model = null;
	try {
	    if (new File("pom.xml").exists()) {
		model = reader.read(new FileReader("pom.xml"));
	    } else {
		model = reader.read(new InputStreamReader(
		        AppAndOsUtils.class.getResourceAsStream("/META-INF/maven/com.minhduc.tuto.oauth2service/OAuth2Service/pom.xml")));
	    }
	} catch (IOException | XmlPullParserException e) {
	    LOGGER.warn("Unable to read SW Version from pom.xml");
	}
	if (model != null) {
	    return model.getArtifactId() + "-" + model.getVersion();
	}
	return "";
    }

    /**
     * to start a web browser
     * 
     * @param url
     */
    public static void browse(String url) {
	LOGGER.debug("Open web browser: {}", url);
	try {
	    if (Desktop.isDesktopSupported()) {
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(new URI(url));
	    } else if (AppAndOsUtils.isWindows()) {
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
	    } else {
		// Ubuntu
		Runtime runtime = Runtime.getRuntime();
		runtime.exec("xdg-open " + url);
	    }
	} catch (IOException | URISyntaxException e) {
	    LOGGER.debug(e.getMessage());
	    LOGGER.debug("Unable to start Webbroser. Please open webbroswer and visit: {}", url);
	}
    }
}
