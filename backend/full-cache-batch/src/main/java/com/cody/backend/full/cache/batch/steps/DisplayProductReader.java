package com.cody.backend.full.cache.batch.steps;

import static com.cody.backend.full.cache.batch.constant.constants.DISPLAY_PRODUCT_READER;
import static com.cody.backend.full.cache.batch.constant.constants.FETCH_SIZE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.backend.full.cache.batch.constant.Query;
import com.cody.domain.store.cache.dto.DisplayProduct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class DisplayProductReader extends JdbcCursorItemReader<DisplayProduct> {

    public DisplayProductReader(@Qualifier("dataSource") DataSource dataSource) {
        setName(DISPLAY_PRODUCT_READER);
        setFetchSize(FETCH_SIZE);
        setDataSource(dataSource);
        setRowMapper(new BeanPropertyRowMapper<>(DisplayProduct.class));
        setSql(Query.getDisplayProductQuery());
        setRowMapper(new DisplayProductRowMapper());
        setDriverSupportsAbsolute(true);
        setVerifyCursorPosition(false);
    }

    private static class DisplayProductRowMapper implements RowMapper<DisplayProduct> {

        @Override
        public DisplayProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            return DisplayProduct.builder()
                                 .productId(rs.getLong("product_id"))
                                 .brandId(rs.getLong("brand_id"))
                                 .productPrice(rs.getLong("product_price"))
                                 .productName(rs.getString("product_name"))
                                 .brandName(rs.getString("brand_name"))
                                 .categoryId(rs.getLong("category_id"))
                                 .categoryName(rs.getString("category_name"))
                                 .lastUpdatedDateTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())))
                                 .build();
        }
    }
}
