package com.example.EHotel;

import static io.zonky.test.db.AutoConfigureEmbeddedDatabase.DatabaseProvider.ZONKY;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;

/**
 * Base class for tests that need the real database: starts an embedded PostgreSQL server
 * (no Docker or local installation required) and loads ../schema.sql into it, so the triggers,
 * constraints and seed data are exactly the ones used by the application.
 */
@SpringBootTest
@AutoConfigureEmbeddedDatabase(provider = ZONKY)
@TestPropertySource(properties = {
        "spring.sql.init.mode=always",
        "spring.sql.init.schema-locations=file:../schema.sql",
        // schema.sql contains PL/pgSQL bodies; send it as a single script and let PostgreSQL split it
        "spring.sql.init.separator=^^^ END OF SCRIPT ^^^"
})
public abstract class PostgresIntegrationTest {
}
