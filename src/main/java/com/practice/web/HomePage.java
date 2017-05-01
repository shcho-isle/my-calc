package com.practice.web;

import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {
    public HomePage() {
        add(new CalculatorPanel("calcPanel"));
        add(new HistoryPanel("historyPanel"));
    }
}
