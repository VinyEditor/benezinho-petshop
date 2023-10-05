package br.com.fiap.petshop;

import br.com.fiap.petshop.infra.configuration.cors.CORSFilter;
import br.com.fiap.petshop.infra.configuration.jwt.JsTokenFilterNeeded;
import br.com.fiap.petshop.infra.database.EntityManagerFactoryProvider;
import br.com.fiap.petshop.infra.database.EntityManagerProvider;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.ApplicationPath;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

@ApplicationPath("/api")
public class Main {
    public static final String BASE_URI = "http://localhost/api/";

    public static final String PERSISTENCE_UNIT = "oracle-fiap";
