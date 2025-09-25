package com.fever.alf.challenge.provider_integration.infrastructure.web.mapper;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import com.fever.alf.challenge.provider_integration.domain.Zone;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlanMapper {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ISO_DATE_TIME;

    public static Plan fromXml(Element planElem, Element basePlanElem) {
        // Map plan attributes
        String planId = planElem.getAttribute("plan_id");
        LocalDateTime startDate = LocalDateTime.parse(planElem.getAttribute("plan_start_date"), FMT);
        LocalDateTime endDate = LocalDateTime.parse(planElem.getAttribute("plan_end_date"), FMT);

        // Get common attributes from base_plan
        String sellMode = basePlanElem.getAttribute("sell_mode");
        String title = basePlanElem.getAttribute("title");

        // Parse zones using ZoneMapper for each zone element
        List<Zone> zones = new ArrayList<>();
        NodeList zoneNodes = planElem.getElementsByTagName("zone");
        for (int i = 0; i < zoneNodes.getLength(); i++) {
            Node zoneNode = zoneNodes.item(i);
            if (zoneNode.getNodeType() == Node.ELEMENT_NODE) {
                Element zoneElem = (Element) zoneNode;
                zones.add(ZoneMapper.fromXml(zoneElem));
            }
        }

        // Build and return Plan object
        return Plan.builder()
                .id(planId)
                .name(title)
                .sellMode(sellMode)
                .startDate(startDate)
                .endDate(endDate)
                .zones(zones)
                .build();
    }
}
