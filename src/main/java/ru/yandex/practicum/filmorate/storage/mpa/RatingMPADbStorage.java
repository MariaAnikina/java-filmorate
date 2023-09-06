package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.RatingMPANotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RatingMPADbStorage implements RatingMPAStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<RatingMPA> getAll() {
        String sqlQuery = "select * from rating";
        return jdbcTemplate.query(sqlQuery, this::mapFromRow);
    }

    @Override
    public RatingMPA getRatingMPAById(int id)  {
        String sqlQuery = "select * from rating where id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapFromRow, id);
    }

    private RatingMPA mapFromRow(ResultSet rs, int rowNum) throws SQLException {
        return new RatingMPA(rs.getInt("id"), rs.getString("name"));
    }
}