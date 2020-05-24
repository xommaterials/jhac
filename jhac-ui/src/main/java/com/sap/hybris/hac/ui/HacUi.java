package com.sap.hybris.hac.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;

import java.time.LocalTime;

@Route
@PWA(name = "jhac UI", shortName = "jhac UI")
@Theme(value = Material.class, variant = Material.DARK)
public class HacUi extends VerticalLayout {

  public HacUi() {
    add(new Button("Click me", e -> Notification.show("Button was clicked at " + LocalTime.now())));
  }
}
