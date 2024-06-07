package com.cody.backend.full.cache.batch.steps;

import static com.cody.backend.full.cache.batch.constant.constants.FETCH_SIZE;
import static com.cody.backend.full.cache.batch.constant.constants.FULL_BRAND_READER;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.backend.full.cache.batch.constant.Query;
import com.cody.domain.store.cache.dto.FullBrand;
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
public class FullBrandReader extends JdbcCursorItemReader<FullBrand> {

    public FullBrandReader(@Qualifier("dataSource") DataSource dataSource) {
        setName(FULL_BRAND_READER);
        setFetchSize(FETCH_SIZE);
        setDataSource(dataSource);
        setRowMapper(new BeanPropertyRowMapper<>(FullBrand.class));
        setSql(Query.getFullBrandQuery());
        setRowMapper(new FullBrandReaderRowMapper());
        setDriverSupportsAbsolute(true);
        setVerifyCursorPosition(false);
    }

    private static class FullBrandReaderRowMapper implements RowMapper<FullBrand> {

        @Override
        public FullBrand mapRow(ResultSet rs, int rowNum) throws SQLException {
            return FullBrand.builder()
                            .name(rs.getString("name"))
                            .id(rs.getLong("id"))
                            .lastUpdatedTime(LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())))
                            .build();
        }
    }
}
