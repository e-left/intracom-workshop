package com.intracomtelecom;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Query;

import java.io.IOException;

import static spark.Spark.*;
import com.intracomtelecom.BodyResponse;

public class CellLogFilter {
    private static final Logger log = LoggerFactory.getLogger(CellLogFilter.class);


    public static void main(String[] args) {
        String dbURL = "jdbc:sqlite::resource:data.sqlite";
        Sql2o sql2o = new Sql2o(dbURL, null, null);

        enableCORS("*","*","*");

        post("/logs", (req, res) -> {
            String cell = "null";
            String eNodeb = "null";

            try {
                String body = req.body();

                BodyResponse bodyResponse = new ObjectMapper().readValue(body, BodyResponse.class);

                if (bodyResponse.eNodeb != null) {
                    eNodeb = bodyResponse.eNodeb;
                }
                if (bodyResponse.cell != null) {
                    cell = bodyResponse.cell;
                }
            }catch (Exception e){

            }




            try (Connection conn = sql2o.open()) {

                String queryString = "SELECT * FROM PerformanceKPIsHourly";
                Query query = conn.createQuery(queryString);
                if(!cell.equals("null") && !eNodeb.equals("null")) {
                    queryString += " WHERE eNodeB = :eNodeb AND Cell = :cell";
                    query = conn.createQuery(queryString).addParameter("eNodeb", eNodeb).addParameter("cell", cell);
                } else
                if(!eNodeb.equals("null")) {
                    queryString += " WHERE eNodeB = :eNodeb";
                    query = conn.createQuery(queryString).addParameter("eNodeb", eNodeb);
                } else
                if(!cell.equals("null")) {
                    queryString += " WHERE Cell = :Cell";
                    query = conn.createQuery(queryString).addParameter("Cell", cell);
                }

                log.debug("Fetch all logs");
                return dataToJson(query.executeAndFetch(Log.class));
            }
        });

        get("/cells/:eNodeB", (req, res) -> {
            try (Connection conn = sql2o.open()) {
                log.debug("Fetch all cells of eNodeB " + req.params(":eNodeB"));
                return dataToJson(conn.createQuery("SELECT DISTINCT Cell FROM PerformanceKPIsHourly WHERE eNodeB = :eNodeB")
                        .addParameter("eNodeB", req.params(":eNodeB"))
                        .executeScalarList(String.class));
            }
        });

        get("/enodeb", (req, res) -> {
            try (Connection conn = sql2o.open()) {
                log.debug("Fetch eNodeB lists");
                return dataToJson(conn.createQuery("SELECT DISTINCT eNodeB FROM PerformanceKPIsHourly")
                        .executeScalarList(String.class));
            }
        });

        after((request, response) -> {
            response.type("application/json");
            response.header("Content-Encoding", "gzip");
        });
    }

    // Enables CORS on requests. This method is an initialization method and should be called once.
    private static void enableCORS(final String origin, final String methods, final String headers) {

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }

            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", origin);
            response.header("Access-Control-Request-Method", methods);
            response.header("Access-Control-Allow-Headers", headers);
            // Note: this may or may not be necessary in your particular application
            response.type("application/json");
        });
    }

    public static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(data);
        } catch (IOException e){
            log.error(e.toString());
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
}