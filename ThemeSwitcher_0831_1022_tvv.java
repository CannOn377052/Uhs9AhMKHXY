// 代码生成时间: 2025-08-31 10:22:09
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerTemplateEngine;

public class ThemeSwitcher {

    private String currentTheme;
    private static final String DEFAULT_THEME = "light"; // Default theme
    private static final String THEME_KEY = "theme"; // Session key for theme

    /**
     * Constructor to initialize the ThemeSwitcher with the default theme.
     */
    public ThemeSwitcher() {
        this.currentTheme = DEFAULT_THEME;
    }

    /**
     * Method to switch the theme between light and dark.
     *
     * @param session The Spark session object.
     */
    public void switchTheme(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("Session cannot be null");
        }

        if ("dark".equals(currentTheme)) {
            currentTheme = DEFAULT_THEME;
        } else {
            currentTheme = "dark";
        }
        session.attribute(THEME_KEY, currentTheme);
    }

    /**
     * Method to get the current theme.
     *
     * @return The current theme.
     */
    public String getCurrentTheme() {
        return currentTheme;
    }

    /**
     * A Spark route that handles the theme switching.
     *
     * @param qMap The query map from the HTTP request.
     * @param model The model for the view.
     * @param viewEngine The view engine for rendering the template.
     * @return A ModelAndView object representing the response.
     */
    public ModelAndView handleThemeSwitch(String qMap, Map<String, Object> model,
                                           FreeMarkerTemplateEngine viewEngine) {
        try {
            Session session = Session.current();
            if (qMap != null && qMap.contains("theme")) {
                switchTheme(session);
            }
            model.put("currentTheme", getCurrentTheme());
            return new ModelAndView(model, "themeSwitcher.ftl");
        } catch (Exception e) {
            // Handle any exceptions, e.g., log the error and return a default view
            model.put("error", "An error occurred while switching themes.");
            return new ModelAndView(model, "error.ftl");
        }
    }
}
