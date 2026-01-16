package com.yyq.service.imp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyq.entity.CarbonResult;
import com.yyq.service.CarbonAnalysisService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CarbonAnalysisServiceImpl implements CarbonAnalysisService {
    @Value("${carbon.API_KEY}")
    private String apiKey;
    @Value("${carbon.BASE_URL}")
    private String apiUrl;
    @Value("${carbon.DATA_VERSION}")
    private String dataVersion;
    @Override
    public CarbonResult calculateCarbon(Path directory) {
        try {
            double electricityConsumption = calElectricity(directory);
            double carbonEmissions = calCarbon(electricityConsumption);

            return new CarbonResult(carbonEmissions, electricityConsumption,"success","carbon","SDG13. Climate Action");
        } catch (Exception e) {
            System.out.println("fail to calculate: " + e.getMessage());
            return new CarbonResult(0, 0, "failure: " + e.getMessage(),"carbon","SDG13. Climate Action");
        }
    }

    private double calElectricity(Path directory){
        AtomicInteger totallines = new AtomicInteger(0);
        try {
            Files.walk(directory)
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach( path -> {
                        try {
                            totallines.addAndGet(Files.readAllLines(path).size());
                        } catch (IOException e) {
                            System.out.println("fail to read file" + path);
                            throw new RuntimeException(e);
                        }

                    });
        } catch (IOException e) {
            System.out.println("fail to walk directory" + e.getMessage());
            throw new RuntimeException(e);
        }
        double electricity = (totallines.get() / 100.0) * 0.1;
        return electricity;
    }

    public double calCarbon(double electricityConsumption) throws IOException {
        Map<String, Object> body = new HashMap<>();
        Map<String,Object> factor = new HashMap<>();
        Map<String,Object> params = new HashMap<>();
        params.put("energy",electricityConsumption);
        params.put("energy_unit","kWh");
        factor.put("activity_id","electricity-supply_grid-source_production_mix");
        factor.put("data_version",dataVersion);
        body.put("emission_factor",factor);
        body.put("parameters",params);
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(body);
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody requestBody = RequestBody.create(jsonString, mediaType);

        Request request = new Request.Builder()
                .url(apiUrl + "/data/v1/estimate")
                .post(requestBody)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                JsonNode root =  new ObjectMapper().readTree(response.body().string());
                double CO2KG = root.get("co2e").asDouble();
                return CO2KG * 1000;
            }
            throw new IOException("something get wrong: " + response.code());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
