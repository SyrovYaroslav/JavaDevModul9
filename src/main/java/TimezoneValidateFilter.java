import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.ZoneId;

@WebFilter(value = "/*")
public class TimezoneValidateFilter extends HttpFilter{
    @Override
    public void doFilter(HttpServletRequest req,
                            HttpServletResponse resp,
                            FilterChain chain) throws IOException, ServletException {

        String timeZone = req.getParameter("timezone");
        if (timeZone != null) {
            try {
                timeZone = timeZone.replace(" ", "+");
                ZoneId.of(timeZone);
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter().write("Invalid timezone");
                resp.getWriter().close();
            }
        }
        chain.doFilter(req, resp);
    }
}
