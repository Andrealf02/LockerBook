package com.fever.alf.challenge.provider_integration.infrastructure.providers;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.Zone;
import lombok.SneakyThrows;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class XmlPlanParser {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_DATE_TIME;

    @SneakyThrows
    public static List<Plan> parse(String xml) {
        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(xml.getBytes()));

        List<Plan> plans = new ArrayList<>();
        NodeList basePlanNodes = doc.getElementsByTagName("base_plan");

        for (int i = 0; i < basePlanNodes.getLength(); i++) {
            Element basePlan = (Element) basePlanNodes.item(i);
            // Get common attributes from base_plan
            String sellMode = basePlan.getAttribute("sell_mode");  // e.g., "online"
            String title = basePlan.getAttribute("title");

            NodeList planNodes = basePlan.getElementsByTagName("plan");

            for (int j = 0; j < planNodes.getLength(); j++) {
                Element planElem = (Element) planNodes.item(j);
                String planId = planElem.getAttribute("plan_id");
                LocalDateTime startDate = LocalDateTime.parse(planElem.getAttribute("plan_start_date"), FMT);
                LocalDateTime endDate = LocalDateTime.parse(planElem.getAttribute("plan_end_date"), FMT);

                NodeList zoneNodes = planElem.getElementsByTagName("zone");
                List<Zone> zones = new ArrayList<>();
                for (int k = 0; k < zoneNodes.getLength(); k++) {
                    Element zoneElem = (Element) zoneNodes.item(k);
                    zones.add(Zone.builder()
                            .zoneId(zoneElem.getAttribute("zone_id"))
                            .capacity(Integer.parseInt(zoneElem.getAttribute("capacity")))
                            .price(new BigDecimal(zoneElem.getAttribute("price")))
                            .name(zoneElem.getAttribute("name"))
                            .numbered(Boolean.parseBoolean(zoneElem.getAttribute("numbered")))
                            .build());
                }

                plans.add(Plan.builder()
                        .id(planId)
                        .name(title)
                        .sellMode(sellMode)
                        .startDate(startDate)
                        .endDate(endDate)
                        .zones(zones)
                        .build());
            }
        }

        return plans;
    }

}
