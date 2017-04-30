package com.practice.repository;

import com.practice.model.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class ExpressionRepository {

    private static final RowMapper<Expression> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Expression.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert insertExpression;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        this.insertExpression = new SimpleJdbcInsert(dataSource)
                .withTableName("expressions")
                .usingGeneratedKeyColumns("id");
    }

    @Transactional
    public void save(Expression expression) {
        MapSqlParameterSource map = new MapSqlParameterSource()
                .addValue("date_time", Timestamp.valueOf(expression.getDateTime()))
                .addValue("first_operand", expression.getFirstOperand())
                .addValue("operation", expression.getOperation().name())
                .addValue("second_operand", expression.getSecondOperand())
                .addValue("result", expression.getResult());

        insertExpression.execute(map);
    }

    public List<Expression> getLatest(int amount) {
        return jdbcTemplate.query(
                "SELECT * FROM expressions ORDER BY date_time DESC LIMIT ?", ROW_MAPPER, amount);
    }
}
