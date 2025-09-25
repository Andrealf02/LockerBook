package com.fever.alf.challenge.provider_integration.infrastructure.web.mapper;

import com.fever.alf.challenge.provider_integration.domain.Plan;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class PlanMapperTest {
    private static final String PLAN_XML = """
        <base_plan base_plan_id=\"291\" sell_mode=\"online\" title=\"Camela en concierto\">
            <plan plan_start_date=\"2021-06-30T21:00:00\" plan_end_date=\"2021-06-30T22:00:00\" plan_id=\"291\">
                <zone zone_id=\"40\" capacity=\"243\" price=\"20.00\" name=\"Platea\" numbered=\"true\" />
            </plan>
        </base_plan>
    """;

    @Test
    void fromXml_mapsAllFieldsCorrectly() throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(PLAN_XML.getBytes(StandardCharsets.UTF_8)));
        Element basePlanElem = (Element) doc.getElementsByTagName("base_plan").item(0);
        Element planElem = (Element) doc.getElementsByTagName("plan").item(0);
        Plan plan = PlanMapper.fromXml(planElem, basePlanElem);
        assertThat(plan.getId()).isEqualTo("291");
        assertThat(plan.getName()).isEqualTo("Camela en concierto");
        assertThat(plan.getSellMode()).isEqualTo("online");
        assertThat(plan.getStartDate()).isEqualTo(LocalDateTime.parse("2021-06-30T21:00:00"));
        assertThat(plan.getEndDate()).isEqualTo(LocalDateTime.parse("2021-06-30T22:00:00"));
        assertThat(plan.getZones()).hasSize(1);
        assertThat(plan.getZones().get(0).getZoneId()).isEqualTo("40");
    }

    @Test
    void fromXml_handlesMissingZoneGracefully() throws Exception {
        String xml = """
            <base_plan base_plan_id=\"292\" sell_mode=\"online\" title=\"Sin zonas\">
                <plan plan_start_date=\"2021-07-01T21:00:00\" plan_end_date=\"2021-07-01T22:00:00\" plan_id=\"292\">
                </plan>
            </base_plan>
        """;
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Element basePlanElem = (Element) doc.getElementsByTagName("base_plan").item(0);
        Element planElem = (Element) doc.getElementsByTagName("plan").item(0);
        Plan plan = PlanMapper.fromXml(planElem, basePlanElem);
        assertThat(plan.getZones()).isEmpty();
    }

    @Test
    void fromXml_throwsOnInvalidDate() throws Exception {
        String xml = """
            <base_plan base_plan_id=\"293\" sell_mode=\"online\" title=\"Fecha inválida\">
                <plan plan_start_date=\"2021-13-01T21:00:00\" plan_end_date=\"2021-07-01T22:00:00\" plan_id=\"293\">
                </plan>
            </base_plan>
        """;
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Element basePlanElem = (Element) doc.getElementsByTagName("base_plan").item(0);
        Element planElem = (Element) doc.getElementsByTagName("plan").item(0);
        assertThatThrownBy(() -> PlanMapper.fromXml(planElem, basePlanElem))
                .isInstanceOf(Exception.class);
    }
}

