package com.example.SpringJDBCTemplate.repo;

import com.example.SpringJDBCTemplate.model.Alien;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ALienRepo {

    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Alien alien){
        String sql = "insert into alien (id, name, tech) values (?, ?, ?)";
        int rows = jdbcTemplate.update(sql, alien.getId(), alien.getName(), alien.getTech());
        System.out.println(rows + "row/s affected");
    }

    public List<Alien> findAll(){
        String sql = "SELECT * FROM alien";
        RowMapper<Alien> mapper = new RowMapper<Alien>() {
            @Override
            public Alien mapRow(ResultSet rs, int rowNum) throws SQLException {
                Alien tmp = new Alien(rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3));
                return tmp;
            }
        };

        return jdbcTemplate.query(sql, mapper);
    }

}
