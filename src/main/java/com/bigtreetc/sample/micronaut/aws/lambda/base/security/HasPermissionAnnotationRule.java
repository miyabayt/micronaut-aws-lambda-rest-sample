package com.bigtreetc.sample.micronaut.aws.lambda.base.security;

import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.rules.SecurityRuleResult;
import io.micronaut.web.router.MethodBasedRouteMatch;
import io.micronaut.web.router.RouteMatch;
import java.util.*;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

@Singleton
@Slf4j
public class HasPermissionAnnotationRule implements SecurityRule {

  @Override
  public Publisher<SecurityRuleResult> check(
      HttpRequest<?> request, RouteMatch<?> routeMatch, Authentication authentication) {
    if (routeMatch instanceof MethodBasedRouteMatch) {
      MethodBasedRouteMatch<?, ?> methodRoute = ((MethodBasedRouteMatch) routeMatch);
      if (methodRoute.hasAnnotation(HasPermission.class)) {
        Optional<String[]> optionalValue =
            methodRoute.getValue(HasPermission.class, String[].class);
        if (optionalValue.isPresent()) {
          List<String> values = Arrays.asList(optionalValue.get());
          if (values.contains(SecurityRule.DENY_ALL)) {
            return Mono.just(SecurityRuleResult.REJECTED);
          }
          return comparePermissions(values, getPermissions(authentication));
        }
      }
    }
    return Mono.just(SecurityRuleResult.ALLOWED);
  }

  protected Publisher<SecurityRuleResult> comparePermissions(
      List<String> requiredPermissions, Collection<String> grantedPermissions) {
    if (grantedPermissions.containsAll(requiredPermissions)) {
      if (log.isDebugEnabled()) {
        log.debug(
            "The given permissions [{}] matched one or more of the required permissions [{}]. Allowing the request",
            grantedPermissions,
            requiredPermissions);
      }
      return Mono.just(SecurityRuleResult.ALLOWED);
    } else {
      if (log.isDebugEnabled()) {
        log.debug(
            "None of the given permissions [{}] matched the required permissions [{}]. Rejecting the request",
            grantedPermissions,
            requiredPermissions);
      }
      return Mono.just(SecurityRuleResult.REJECTED);
    }
  }

  @SuppressWarnings("unchecked")
  protected List<String> getPermissions(Authentication authentication) {
    List<String> permissions = new ArrayList<>();
    if (authentication != null) {
      val allowed = (List<String>) authentication.getAttributes().get("permissions");
      if (allowed != null) {
        permissions.addAll(allowed);
      }
    }
    return permissions;
  }
}
