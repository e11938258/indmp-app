package at.tuwien.indmp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import at.tuwien.indmp.util.Endpoints;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests(authz -> authz
                        // maDMP endpoints
                        .antMatchers(HttpMethod.PUT, Endpoints.UPDATE_MADMP, Endpoints.UPDATE_MADMP_IDENTIFIER,
                                Endpoints.DELETE_MADMP_INSTANCE)
                        .hasAuthority("SCOPE_update")
                        .antMatchers(HttpMethod.GET, Endpoints.GET_MADMP_IDENTIFIERS).hasAuthority("SCOPE_update")
                        // Services endpoints
                        .antMatchers("/system/**").hasAuthority("SCOPE_update")
                        // Other
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2.jwt());
    }
}
