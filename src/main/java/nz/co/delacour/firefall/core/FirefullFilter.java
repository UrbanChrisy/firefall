package nz.co.delacour.firefall.core;

import nz.co.delacour.firefall.core.util.AbstractFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Closeable;
import java.io.IOException;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class FirefullFilter extends AbstractFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try (Closeable closeable = FirefullService.begin()) {
            chain.doFilter(request, response);
        }
    }
}
