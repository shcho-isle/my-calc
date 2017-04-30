package com.practice.web;

import com.practice.model.Expression;
import com.practice.model.Operation;
import com.practice.repository.ExpressionRepository;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.*;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class CalcPage extends WebPage {

    private static final int HISTORY_LENGTH = 10;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyy kk:mm:ss");
    private static final Duration UPDATE_INTERVAL = Duration.seconds(5);

    @SpringBean
    private ExpressionRepository repository;

    public CalcPage() {
        final Expression expression = new Expression();

        Form<?> form = new Form("form");

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMsg");
        feedbackPanel.setOutputMarkupId(true);

        final NumberTextField<BigDecimal> firstOperand = new NumberTextField<>("firstOperand", new PropertyModel<BigDecimal>(expression, "firstOperand"));
        firstOperand.setOutputMarkupId(true);
        firstOperand.setRequired(true);

        final DropDownChoice<Operation> operationChoice = new DropDownChoice<>("operation", new PropertyModel<Operation>(expression, "operation"), Arrays.asList(Operation.values()));
        operationChoice.setOutputMarkupId(true);

        final NumberTextField<BigDecimal> secondOperand = new NumberTextField<>("secondOperand", new PropertyModel<BigDecimal>(expression, "secondOperand"));
        secondOperand.setOutputMarkupId(true);
        secondOperand.setRequired(true);

        final NumberTextField<BigDecimal> result = new NumberTextField<>("result", new PropertyModel<BigDecimal>(expression, "result"));
        result.setOutputMarkupId(true);
        result.setEnabled(false);

        AjaxButton ajaxButton = new AjaxButton("submit") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);

                expression.setResult(expression.getOperation().calculate(expression.getFirstOperand(), expression.getSecondOperand()));
                expression.updateDateTime();
                repository.save(expression);

                target.add(firstOperand);
                target.add(operationChoice);
                target.add(secondOperand);
                target.add(result);
                target.addChildren(getPage(), FeedbackPanel.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target) {
                target.add(feedbackPanel);
                super.onError(target);
            }
        };

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

        add(form);
        add(historyLength);
        add(updateInterval);
        add(listContainer);

        form.add(feedbackPanel);
        form.add(firstOperand);
        form.add(operationChoice);
        form.add(secondOperand);
        form.add(result);
        form.add(ajaxButton);
    }
}
