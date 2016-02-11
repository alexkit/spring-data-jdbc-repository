/*
 * Copyright 2012-2014 Tomasz Nurkiewicz <nurkiewicz@gmail.com>.
 * Copyright 2016 Jakub Jirutka <jakub@jirutka.cz>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nurkiewicz.jdbcrepository.mssql;

import com.nurkiewicz.jdbcrepository.JdbcRepositoryTestConfig;
import com.nurkiewicz.jdbcrepository.TableDescription;
import com.nurkiewicz.jdbcrepository.repositories.CommentWithUserRepository;
import com.nurkiewicz.jdbcrepository.sql.MssqlSqlGenerator;
import com.nurkiewicz.jdbcrepository.sql.SqlGenerator;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@EnableTransactionManagement
@Configuration
public class JdbcRepositoryTestMssqlConfig extends JdbcRepositoryTestConfig {

    public static final int MSSQL_PORT = Integer.parseInt(System.getProperty("mssql.port", "1433"));


    @Override
    public CommentWithUserRepository commentWithUserRepository() {
        return new CommentWithUserRepository(
            CommentWithUserRepository.MAPPER,
            CommentWithUserRepository.ROW_UNMAPPER,
            new CommentWithUserMssqlGenerator(),
            new TableDescription("COMMENTS", "COMMENTS c JOIN USERS u ON c.USER_NAME = u.USER_NAME", "ID")
        );
    }

    @Bean
    public SqlGenerator sqlGenerator() {
        return new MssqlSqlGenerator();
    }

    @Bean(destroyMethod = "shutdown")
    public DataSource dataSource() {
        Properties props = new Properties();
        props.setProperty("serverName", System.getProperty("mssql2012.hostname", "localhost"));
        props.setProperty("instance", System.getProperty("mssql2012.instance", "SQL2012SP1"));
        props.setProperty("databaseName", "spring_data_jdbc_repository_test");
        props.setProperty("user", System.getProperty("mssql2012.user", "sa"));
        props.setProperty("password", System.getProperty("mssql2012.password", "Password12!"));

        HikariDataSource ds = new HikariDataSource();
        ds.setDataSourceClassName("net.sourceforge.jtds.jdbcx.JtdsDataSource");
        ds.setDataSourceProperties(props);

        return ds;
    }

}
