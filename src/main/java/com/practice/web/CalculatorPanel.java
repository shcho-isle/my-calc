package com.practice.web;

import com.practice.model.Expression;
import com.practice.model.Operation;
import com.practice.repository.ExpressionRepository;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
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

        Form<Void> expressionForm = new Form<>("form");

        final FeedbackPanel feedbackPanel = new FeedbackPanel("feedbackMsg");
        feedbackPanel.setOutputMarkupId(true);

        final NumberTextField<BigDecimal> firstOperandField = new NumberTextField<>("firstOperand", new PropertyModel<BigDecimal>(expression, "firstOperand"));
        firstOperandField.setOutputMarkupId(true);
        firstOperandField.setRequired(true);

        final DropDownChoice<Operation> operationChoice = new DropDownChoice<>("operation", new PropertyModel<Operation>(expression, "operation"), Arrays.asList(Operation.values()));
        operationChoice.setOutputMarkupId(true);

        final NumberTextField<BigDecimal> secondOperandField = new NumberTextField<>("secondOperand", new PropertyModel<BigDecimal>(expression, "secondOperand"));
        secondOperandField.setOutputMarkupId(true);
        secondOperandField.setRequired(true);

        final NumberTextField<BigDecimal> resultField = new NumberTextField<>("result", new PropertyModel<BigDecimal>(expression, "result"));
        resultField.setOutputMarkupId(true);
        resultField.setEnabled(false);

        AjaxButton ajaxButton = new AjaxButton("submit") {

            private static final long serialVersionUID = 8429547600788448785L;

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                super.onSubmit(target, form);

                expression.setResult(expression.getOperation().calculate(expression.getFirstOperand(), expression.getSecondOperand()));
                expression.updateDateTime();
                repository.save(expression);

                target.add(firstOperandField);
                target.add(operationChoice);
                target.add(secondOperandField);
                target.add(resultField);
                target.addChildren(getPage(), FeedbackPanel.class);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form form) {
                target.add(feedbackPanel);
                super.onError(target, form);
            }
        };

        add(expressionForm);

        expressionForm.add(feedbackPanel);
        expressionForm.add(firstOperandField);
        expressionForm.add(operationChoice);
        expressionForm.add(secondOperandField);
        expressionForm.add(resultField);
        expressionForm.add(ajaxButton);
        expressionForm.add(new AbstractFormValidator() {
            @Override
            public FormComponent<?>[] getDependentFormComponents() {
                return new FormComponent[]{operationChoice, secondOperandField};
            }

            @Override
            public void validate(Form<?> form) {
                Operation operation = operationChoice.getConvertedInput();
                BigDecimal secondOperand = secondOperandField.getConvertedInput();
                if (operation == Operation.DIVIDE && secondOperand.compareTo(BigDecimal.ZERO) == 0) {
                    secondOperandField.error("You can not divide by zero.");
                }
            }
        });
    }
}
