package com.fever.alf.challenge.provider_integration.infrastructure.web.mapper;

import com.fever.alf.challenge.provider_integration.domain.Zone;
import org.w3c.dom.Element;
import java.math.BigDecimal;

public class ZoneMapper {
    public static Zone fromXml(Element zoneElem) {
        return Zone.builder()
                .zoneId(zoneElem.getAttribute("zone_id"))
                .capacity(Integer.parseInt(zoneElem.getAttribute("capacity")))
                .price(new BigDecimal(zoneElem.getAttribute("price")))
                .name(zoneElem.getAttribute("name"))
                .numbered(Boolean.parseBoolean(zoneElem.getAttribute("numbered")))
                .build();
    }
}

