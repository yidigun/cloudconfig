package com.yidigun.utils.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class SpringUtils {

    /**
     * Dump all of environment properties to DEBUG enabled {@link Logger}.
     *
     * <pre>{@code
     * @Slf4j
     * @SpringBootApplication
     * public class SomeApplication {
     *     public static void main(String[] args) {
     *         SpringApplication.run(SomeApplication.class, args);
     *     }
     *
     *     @EventListener
     *     public void dumpEnv(ContextRefreshedEvent event) {
     *         SpringUtils.dumpEnv(event.getApplicationContext(), log);
     *     }
     * }
     * }</pre>
     * @param context Spring Application Context object.
     * @param logger Logger object. require DEBUG level enabled.
     */
    public static void dumpEnv(ApplicationContext context, Logger logger) {
        if (logger.isDebugEnabled()) {
            logger.debug(getEnvDump(context));
        }
    }

    /**
     * Create Dump report of environment properties.
     *
     * <pre>{@code
     * @Slf4j
     * @SpringBootApplication
     * public class SomeApplication {
     *     public static void main(String[] args) {
     *         SpringApplication.run(SomeApplication.class, args);
     *     }
     *
     *     @EventListener
     *     public void dumpEnv(ContextRefreshedEvent event) {
     *         if (log.isDebugEnabled())
     *             log.debug(SpringUtils.getEnvDump(event.getApplicationContext()));
     *     }
     * }
     * }</pre>
     * @param context Spring Application Context object.
     * @return Report string of all environment properties.
     */
    public static String getEnvDump(ApplicationContext context) {
        return getEnvDump(context.getEnvironment());
    }

    /**
     * Create Dump report of environment properties.
     *
     * <pre>{@code
     * @Slf4j
     * @SpringBootApplication
     * public class SomeApplication {
     *     public static void main(String[] args) {
     *         SpringApplication.run(SomeApplication.class, args);
     *     }
     *
     *     @EventListener
     *     public void dumpEnv(ContextRefreshedEvent event) {
     *         if (log.isDebugEnabled())
     *             log.debug(SpringUtils.getEnvDump(event.getApplicationContext().getEnvironment()));
     *     }
     * }
     * }</pre>
     * @param env Spring Environment object.
     * @return Report string of all environment properties.
     */
    public static String getEnvDump(Environment env) {
        StringWriter buf = new StringWriter();
        PrintWriter out = new PrintWriter(buf);

        if (env instanceof ConfigurableEnvironment) {
            ConfigurableEnvironment cenv = (ConfigurableEnvironment)env;
            out.println("*********** Environment Dump ************");

            List<String> activeProfiles = Arrays.asList(cenv.getActiveProfiles());
            out.printf("* active profiles: %s\n", activeProfiles);

            for (PropertySource<?> source: cenv.getPropertySources()) {
                if (source.containsProperty("spring.config.activate.on-profile") &&
                        !activeProfiles.contains(source.getProperty("spring.config.activate.on-profile"))) {
                    continue;
                }
                else if (!(source instanceof EnumerablePropertySource)) {
                    continue;
                }
                else {
                    out.printf("* %s (%s)\n", source.getName(), source.getClass().getSimpleName());
                }

                for (String key: ((EnumerablePropertySource<?>)source).getPropertyNames()) {
                    if (key.equals("spring.config.activate.on-profile"))
                        continue;

                    Object value = source.getProperty(key);
                    Object finalValue = null;
                    try {
                        finalValue = env.getProperty(key);
                    }
                    catch (Exception e) {
                        finalValue = e;
                    }

                    if (value == null)
                        out.printf(" - %s = null");
                    else if (value instanceof String)
                        out.printf(" - %s = \"%s\"", key, StringEscapeUtils.escapeJava(value.toString()));
                    else
                        out.printf(" - %s = %s (%s)", key, value.toString(), value.getClass().getSimpleName());

                    if (!value.equals(finalValue)) {
                        if (finalValue instanceof Exception)
                            out.printf(" = Error(%s)\n", ((Exception)finalValue).getMessage());
                        else
                            out.printf(" = \"%s\"\n", StringEscapeUtils.escapeJava(finalValue.toString()));
                    }
                    else {
                        out.println();
                    }
                }
            }

            out.println("*********** End of Environment Dump ************");
        } else {
            out.printf("Environment is not configurable: %s\n", env.getClass().getName());
        }

        return buf.toString();
    }
}
