package com.cody.backend.full.cache.batch.steps;

import static com.cody.backend.full.cache.batch.constant.constants.DISPLAY_PRODUCT_READER;
import static com.cody.backend.full.cache.batch.constant.constants.FETCH_SIZE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

import com.cody.backend.full.cache.batch.constant.Query;
import com.cody.domain.store.cache.dto.AllUser;
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
public class AllUserReader extends JdbcCursorItemReader<AllUser> {

    public AllUserReader(@Qualifier("dataSource") DataSource dataSource) {
        setName(DISPLAY_PRODUCT_READER);
        setFetchSize(FETCH_SIZE);
        setDataSource(dataSource);
        setRowMapper(new BeanPropertyRowMapper<>(AllUser.class));
        setSql(Query.getAllUserQuery());
        setRowMapper(new AllUserReaderRowMapper());
        setDriverSupportsAbsolute(true);
        setVerifyCursorPosition(false);
    }

    private static class AllUserReaderRowMapper implements RowMapper<AllUser> {
        @Override
        public AllUser mapRow(ResultSet rs, int rowNum) throws SQLException {
            return AllUser.builder()
                          .userId(rs.getLong("user_id"))
                          .userName(rs.getString("user_name"))
                          .adminId(rs.getLong("seller_id"))
                          .adminName(rs.getString("seller_name"))
                          .lastUpdatedDateTime(
                              LocalDateTime.parse(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now())))
                          .build();
        }
    }
}
