import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;


@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();

        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix("C:/java/JavaDevModule9/src/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");
        Cookie[] cookies = req.getCookies();
        String timezone = req.getParameter("timezone");
        if(timezone == null) {
            if(cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if(cookie.getName().equals("timezone")) {
                        timezone = cookie.getValue();
                    }
                }
            } else {
                timezone = "UTC";
            }
        } else {
            timezone = timezone.replace(" ", "+");
        }

        String timeUTC = ZonedDateTime.now(ZoneId.of(timezone))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Context simpleContext = new Context(
                req.getLocale(),
                Map.of("timezone", timezone, "time", timeUTC)
        );

        resp.addCookie(new Cookie("timezone", timezone));

        engine.process("time", simpleContext, resp.getWriter());
        resp.getWriter().close();
    }
}
