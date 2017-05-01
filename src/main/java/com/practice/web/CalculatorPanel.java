package com.practice.web;

import com.practice.model.Expression;
import com.practice.model.Operation;
import com.practice.repository.ExpressionRepository;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.math.BigDecimal;
import java.util.Arrays;

public class CalculatorPanel extends Panel {

    @SpringBean
    private ExpressionRepository repository;

    public CalculatorPanel(String id) {
        super(id);

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

        add(form);

        form.add(feedbackPanel);
        form.add(firstOperand);
        form.add(operationChoice);
        form.add(secondOperand);
        form.add(result);
        form.add(ajaxButton);
    }
}
