- spring yaml https://www.baeldung.com/spring-yaml
- https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.yaml
- inheritance https://stackoverflow.com/questions/48330310/yml-config-files-inheritance-with-spring-boot
  yaml "structures" (inline documents?): https://yaml.org/spec/1.1/current.html#id857577 (as of 2023.12.04)
  search also https://bitbucket.org/snakeyaml/snakeyaml/wiki/Documentation for `---`
- https://www.baeldung.com/spring-boot-yaml-vs-properties#3-multiple-profiles explicitly states what I couldnt find; "Also, we can have a common set of properties at the root level — in this case, the logging.file.name property will be the same in all profiles."
    - What happens if you do that *and* have multiple files?
    - Baeldung says "2.4. Profiles Across Multiple Files | As an alternative to having different profiles in the same file, we can store multiple profiles across different files. Prior to version 2.4.0, this was the only method available for properties files. We achieve this by putting the name of the profile in the file name — for example, application-dev.yml or application-dev.properties."
    - See (above) https://stackoverflow.com/questions/48330310/yml-config-files-inheritance-with-spring-boot/63137943#63137943 I guess 

- TODO https://spring.io/blog/2020/08/14/config-file-processing-in-spring-boot-2-4
- spring.config.use-legacy-processing: true TODO https://github.com/spring-projects/spring-boot/issues/24172
- https://spring.io/blog/2020/08/14/config-file-processing-in-spring-boot-2-4
- https://stackoverflow.com/questions/73062721/what-is-diff-between-spring-profiles-active-vs-spring-config-activate-on-profile
- https://docs.spring.io/spring-boot/docs/3.0.0-M4/reference/htmlsingle/#features.profiles.groups
- https://stackoverflow.com/questions/47329937/how-to-override-profile-specific-properties-with-a-different-profile
- https://stackoverflow.com/questions/47368086/spring-spring-profiles-include-overrides

ordering link dump:
```
spring profile order - Google Search
https://www.google.com/search?hl=en&q=spring%20profile%20order

Spring Profiles application properties order - Stack Overflow
https://stackoverflow.com/questions/51565409/spring-profiles-application-properties-order

java - Spring Autoconfig order/precedence on Profiles - Stack Overflow
https://stackoverflow.com/questions/48062754/spring-autoconfig-order-precedence-on-profiles

What is the order of precedence when there are multiple Spring's environment profiles as set by spring.profiles.active - Stack Overflow
https://stackoverflow.com/questions/23617831/what-is-the-order-of-precedence-when-there-are-multiple-springs-environment-pro/62222879#62222879

Precedence of Spring boot profile specific and spring.config.import properties - Stack Overflow
https://stackoverflow.com/questions/68760290/precedence-of-spring-boot-profile-specific-and-spring-config-import-properties

What is the order of precedence when there are multiple Spring's environment profiles as set by spring.profiles.active - Stack Overflow
https://stackoverflow.com/questions/23617831/what-is-the-order-of-precedence-when-there-are-multiple-springs-environment-pro

spring debug profile yaml - Google Search
https://www.google.com/search?q=spring+debug+profile+yaml&sca_esv=587597168&hl=en&ei=NHdtZZ6GA8uH9u8PgN0E&ved=0ahUKEwie8JeBmfWCAxXLg_0HHYAuAQAQ4dUDCBA&uact=5&oq=spring+debug+profile+yaml&gs_lp=Egxnd3Mtd2l6LXNlcnAiGXNwcmluZyBkZWJ1ZyBwcm9maWxlIHlhbWwyCBAhGKABGMMESLYKUNICWOkJcAF4AZABAJgBbaABnQWqAQM3LjG4AQPIAQD4AQHCAgoQABhHGNYEGLADwgIGEAAYBxgewgIGEAAYCBgewgIIEAAYCBgeGArCAggQABgIGB4YD8ICCxAAGIAEGIoFGIYDwgIKECEYoAEYwwQYCuIDBBgAIEGIBgGQBgg&sclient=gws-wiz-serp

spring profiles include precedence - Google Search
https://www.google.com/search?hl=en&q=spring%20profiles%20include%20precedence
```

TODO link dump after fixed:
```
java - Property 'spring.profiles.active' imported from location 'class path resource [application-dev.yml]' is invalid - Stack Overflow
https://stackoverflow.com/questions/67935961/property-spring-profiles-active-imported-from-location-class-path-resource-a

"cannot contain property" "spring.profiles.include[0]" - Google Search
https://www.google.com/search?q=%22cannot+contain+property%22+%22spring.profiles.include%5B0%5D%22&sca_esv=587583771&hl=en&ei=A21tZcrvO_7-7_UP7uOO8A4&ved=0ahUKEwiK_celj_WCAxV-_7sIHe6xA-4Q4dUDCBA&uact=5&oq=%22cannot+contain+property%22+%22spring.profiles.include%5B0%5D%22&gs_lp=Egxnd3Mtd2l6LXNlcnAiNiJjYW5ub3QgY29udGFpbiBwcm9wZXJ0eSIgInNwcmluZy5wcm9maWxlcy5pbmNsdWRlWzBdIjIEEAAYHjILEAAYgAQYigUYhgMyCxAAGIAEGIoFGIYDMgsQABiABBiKBRiGA0ipFVDjBlj2CXABeACQAQCYAVagAe8BqgEBM7gBA8gBAPgBAcICCRAAGAcYHhiwA8ICBxAAGB4YsAPCAg4QABiABBiKBRiGAxiwA8ICBhAAGAUYHuIDBBgBIEGIBgGQBgc&sclient=gws-wiz-serp

Spring "spring.profiles.include" overrides - Stack Overflow
https://stackoverflow.com/questions/47368086/spring-spring-profiles-include-overrides

spring.profiles.included not permitted in externalized non-profile-specific properties · Issue #1788 · spring-cloud/spring-cloud-config
https://github.com/spring-cloud/spring-cloud-config/issues/1788

22. Profiles
https://docs.spring.io/spring-boot/docs/1.2.0.M1/reference/html/boot-features-profiles.html

Migrating out of spring.profiles.include into spring.profiles.group · Issue #24172 · spring-projects/spring-boot
https://github.com/spring-projects/spring-boot/issues/24172

Rewriting application properties in Sprignboot 2.4 style | by Developer's Life | Medium
https://medium.com/@iamdeepinjava/rewriting-application-properties-in-sprignboot-2-4-style-a32f4604656c

jpa postgres at DuckDuckGo
https://duckduckgo.com/?t=ffab&q=jpa+postgres&ia=web

Spring Boot + Spring Data JPA + PostgreSQL example - Mkyong.com
https://mkyong.com/spring-boot/spring-boot-spring-data-jpa-postgresql/

Spring Boot, JPA/Hibernate, PostgreSQL example: CRUD - BezKoder
https://www.bezkoder.com/spring-boot-postgresql-example/

spring on-profile ignored - Google Search
https://www.google.com/search?hl=en&q=spring%20on%2Dprofile%20ignored

java - Spring activated profile is being ignored - without Spring Boot - Stack Overflow
https://stackoverflow.com/questions/51697124/spring-activated-profile-is-being-ignored-without-spring-boot

spring.profiles.include is silently ignored when used in a profile-specific document · Issue #24733 · spring-projects/spring-boot
https://github.com/spring-projects/spring-boot/issues/24733

java - Spring profile is ignored when reading properties from application.yml - Stack Overflow
https://stackoverflow.com/questions/53117490/spring-profile-is-ignored-when-reading-properties-from-application-yml

java - Setting the default active profile in Spring-boot - Stack Overflow
https://stackoverflow.com/questions/37700352/setting-the-default-active-profile-in-spring-boot

Spring Profiles | Baeldung
https://www.baeldung.com/spring-profiles

spring default active profile at DuckDuckGo
https://duckduckgo.com/?t=ffab&q=spring+default+active+profile&ia=web

What is diff between spring.profiles.active vs spring.config.activate.on-profile? - Stack Overflow
https://stackoverflow.com/questions/73062721/what-is-diff-between-spring-profiles-active-vs-spring-config-activate-on-profile

One-Stop Guide to Profiles with Spring Boot
https://reflectoring.io/spring-boot-profiles/

spring activating all properties by default at DuckDuckGo
https://duckduckgo.com/?t=ffab&q=spring+activating+all+properties+by+default&ia=web

spring ignoring "on-profile" - Google Search
https://www.google.com/search?q=spring+ignoring+%22on-profile%22&sca_esv=587583771&hl=en&ei=aG9tZdfrLbKV9u8PgrqWSA&ved=0ahUKEwjXv-DJkfWCAxWyiv0HHQKdBQkQ4dUDCBA&uact=5&oq=spring+ignoring+%22on-profile%22&gs_lp=Egxnd3Mtd2l6LXNlcnAiHHNwcmluZyBpZ25vcmluZyAib24tcHJvZmlsZSIyBRAhGKABMgUQIRigATIFECEYoAEyBRAhGKABMggQIRgWGB4YHUiqCFCtBFiPB3ABeAGQAQCYAWugAcsBqgEDMS4xuAEDyAEA-AEBwgIKEAAYRxjWBBiwA8ICCBAAGIAEGKIE4gMEGAAgQYgGAZAGCA&sclient=gws-wiz-serp

spring.config.activate.on-profile cannot be used in profile specific file · Issue #24990 · spring-projects/spring-boot
https://github.com/spring-projects/spring-boot/issues/24990

spring "the following" "profiles are active" - Google Search
https://www.google.com/search?hl=en&q=spring%20%22the%20following%22%20%22profiles%20are%20active%22

spring active profiles - Google Search
https://www.google.com/search?hl=en&q=spring%20active%20profiles

Spring Profiles in Spring Boot - Spring Cloud
https://www.springcloud.io/post/2022-09/spring-boot-profiles/#gsc.tab=0

java spring irc channel - Google Search
https://www.google.com/search?q=java+spring+irc+channel&sca_esv=587583771&hl=en&ei=snBtZZmgCNOK9u8PtqmuyAQ&ved=0ahUKEwiZwejmkvWCAxVThf0HHbaUC0kQ4dUDCBA&uact=5&oq=java+spring+irc+channel&gs_lp=Egxnd3Mtd2l6LXNlcnAiF2phdmEgc3ByaW5nIGlyYyBjaGFubmVsMgUQIRigATIFECEYoAFIww1QmgRY9gtwAXgAkAEAmAFyoAGiBaoBAzcuMbgBA8gBAPgBAcICBxAhGKABGArCAggQIRgWGB4YHeIDBBgBIEGIBgE&sclient=gws-wiz-serp

spring use-legacy-processing at DuckDuckGo
https://duckduckgo.com/?t=ffab&q=spring+use-legacy-processing&ia=web

Config file processing in Spring Boot 2.4
https://spring.io/blog/2020/08/14/config-file-processing-in-spring-boot-2-4

spring.profiles 2.3 - Google Search
https://www.google.com/search?q=spring.profiles+2.3&sca_esv=587583771&hl=en&ei=X3FtZZGCJcqO9u8PwY2vwAY&ved=0ahUKEwjRrcS5k_WCAxVKh_0HHcHGC2gQ4dUDCBA&uact=5&oq=spring.profiles+2.3&gs_lp=Egxnd3Mtd2l6LXNlcnAiE3NwcmluZy5wcm9maWxlcyAyLjMyBRAhGKABMgUQIRigATIFECEYoAEyBRAhGKABSIMJUKABWO4HcAF4AJABAJgBeKAB9QKqAQMyLjK4AQPIAQD4AQHCAggQABiABBiwA8ICCRAAGAcYHhiwA8ICCxAAGIAEGIoFGJECwgIFEAAYgATCAgQQABgewgIGEAAYFhge4gMEGAEgQYgGAZAGCg&sclient=gws-wiz-serp

What is diff between spring.profiles.active vs spring.config.activate.on-profile? - Stack Overflow
https://stackoverflow.com/questions/73062721/what-is-diff-between-spring-profiles-active-vs-spring-config-activate-on-profile

Spring Boot Reference Documentation
https://docs.spring.io/spring-boot/docs/3.0.0-M4/reference/htmlsingle/#features.profiles.groups
```