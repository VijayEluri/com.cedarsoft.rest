package com.cedarsoft.rest.sample.rest;

import com.cedarsoft.rest.sample.User;
import com.cedarsoft.rest.sample.jaxb.UserMapping;
import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class GuiceConfig extends GuiceServletContextListener {
  @Override
  protected Injector getInjector() {
    final Map<String, String> params = new HashMap<String, String>();
    params.put( PackagesResourceConfig.PROPERTY_PACKAGES, UsersResource.class.getPackage().getName() );
    params.put( FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, "true" );

    return Guice.createInjector( Stage.DEVELOPMENT, new ExampleModule(), new ServletModule() {
      @Override
      protected void configureServlets() {
        serve( "/*" ).with( GuiceContainer.class, params );
      }
    } );
  }

  public static class ExampleModule extends AbstractModule {
    public ExampleModule() {
      System.out.println( "---------------" );
      System.out.println( "---------------" );
      System.out.println( "GuiceConfig$ExampleModule.ExampleModule" );
      System.out.println( "---------------" );
      System.out.println( "---------------" );
    }

    @Override
    protected void configure() {
      bind( UserMapping.class ).in( Singleton.class );
    }

    @Provides
    List<? extends User> provideUsers() {
      User js = new User( "info@cedarsoft.de", "Johannes Schneider" );
      User max = new User( "markus@mustermann.de", "Markus Mustermann" );
      User eva = new User( "eva@mustermann.de", "Eva Mustermann" );

      js.addFriend( max );
      js.addFriend( eva );
      eva.addFriend( max );
      max.addFriend( max );
      max.addFriend( js );

      return ImmutableList.of(
        js,
        max,
        eva
      );
    }

//    @Nullable
//    private transient WebApplication webApplicationReference;
//
//    @Provides
//    public WebApplication webApp( @NotNull GuiceContainer guiceContainer ) {
//      WebApplication copy = webApplicationReference;
//      if ( copy == null ) {
//        WebComponent component = Reflection.field( "webComponent" ).ofType( WebComponent.class ).in( guiceContainer ).get();
//        copy = Reflection.field( "application" ).ofType( WebApplication.class ).in( component ).get();
//        webApplicationReference = copy;
//      }
//      return copy;
//    }
//
//    @RequestScoped
//    @Provides
//    public HttpContext httpContext( @NotNull WebApplication webApplication ) {
//      return webApplication.getThreadLocalHttpContext();
//    }
//
//    @Provides
//    public ExceptionMapperContext exceptionMapperContext( @NotNull WebApplication webApplication ) {
//      return webApplication.getExceptionMapperContext();
//    }
//
//    @Provides
//    public FeaturesAndProperties featuresAndProperties( @NotNull WebApplication webApplication ) {
//      return webApplication.getFeaturesAndProperties();
//    }
//
//    @Provides
//    public ResourceConfig resourceConfig( @NotNull WebApplication webApplication ) {
//      return ( ResourceConfig ) webApplication.getFeaturesAndProperties();
//    }
//
//    @Provides
//    public MessageBodyWorkers messageBodyFactory( @NotNull WebApplication webApplication ) {
//      return webApplication.getMessageBodyWorkers();
//    }
//
//    @RequestScoped
//    @Provides
//    public UriInfo uriInfo( @NotNull HttpContext httpContext ) {
//      return httpContext.getUriInfo();
//    }
//
//    @RequestScoped
//    @Provides
//    public HttpRequestContext requestContext( @NotNull HttpContext httpContext ) {
//      return httpContext.getRequest();
//    }
//
//    @RequestScoped
//    @Provides
//    public HttpHeaders httpHeaders( @NotNull HttpContext httpContext ) {
//      return httpContext.getRequest();
//    }
//
//    @RequestScoped
//    @Provides
//    public Request request( @NotNull HttpContext httpContext ) {
//      return httpContext.getRequest();
//    }
//
//    @RequestScoped
//    @Provides
//    public SecurityContext securityContext( @NotNull HttpContext httpContext ) {
//      return httpContext.getRequest();
//    }
//
//    @RequestScoped
//    @Provides
//    public UriBuilder uriBuilder( @NotNull UriInfo uriInfo ) {
//      return uriInfo.getRequestUriBuilder();
//    }
  }
}

