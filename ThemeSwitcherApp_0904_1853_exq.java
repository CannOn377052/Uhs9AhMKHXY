// 代码生成时间: 2025-09-04 18:53:24
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class ThemeSwitcherApp {

    // Define the themes available for the application
    private static final String[] THEMES = { "light", "dark" };

    // The path to the templates directory
    private static final String TEMPLATES_PATH = "src/main/resources/templates";

    public static void main(String[] args) {
        // Set the template engine to Velocity
        String templatesPath = System.getProperty("user.dir") + "/" + TEMPLATES_PATH;
        setTemplateEngine(new VelocityTemplateEngine(templatesPath));

        // Define the routes
        get("/", (request, response) -> new ModelAndView(null, "index.vm"), new VelocityTemplateEngine());

        post("/toggle", (request, response) -> {
            String currentTheme = request.session().attribute("theme");
            String newTheme = (currentTheme == null || currentTheme.equals("light")) ? "dark" : "light";
            request.session().attribute("theme