package com.practice.web;

import org.apache.wicket.markup.html.WebPage;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 9143717808924950175L;

    public HomePage() {
        add(new CalculatorPanel("calcPanel"));
        add(new HistoryPanel("historyPanel"));
    }
}
