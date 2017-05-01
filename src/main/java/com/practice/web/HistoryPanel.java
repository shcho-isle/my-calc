package com.practice.web;

import com.practice.model.Expression;
import com.practice.repository.ExpressionRepository;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistoryPanel extends Panel {

    private static final int HISTORY_LENGTH = 10;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyy kk:mm:ss");
    private static final Duration UPDATE_INTERVAL = Duration.seconds(5);

    @SpringBean
    private ExpressionRepository repository;

    public HistoryPanel(String id) {
        super(id);

        Label historyLength = new Label("historyLength", HISTORY_LENGTH);
        Label updateInterval = new Label("updateInterval", UPDATE_INTERVAL);

        IModel<List<Expression>> latestList = new LoadableDetachableModel<List<Expression>>() {
            protected List<Expression> load() {
                return repository.getLatest(HISTORY_LENGTH);
            }
        };

        ListView<Expression> listView = new ListView<Expression>("listView", latestList) {
            protected void populateItem(ListItem item) {
                Expression expr = (Expression) item.getModelObject();
                item.add(new Label("dateTime", expr.getDateTime().format(FORMATTER)));
                item.add(new Label("expression", expr.toString()));
            }
        };

        WebMarkupContainer listContainer = new WebMarkupContainer("theContainer");
        listContainer.setOutputMarkupId(true);
        listContainer.add(new AjaxSelfUpdatingTimerBehavior(UPDATE_INTERVAL));
        listContainer.add(listView);

        add(historyLength);
        add(updateInterval);
        add(listContainer);
    }
}
