package ksmart.mybatis.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@Configuration
//@MapperScan(value = "ksmart.mybatis.mapper", sqlSessionFactoryRef = "mybatisSqlSessionFactory")
//@EnableTransactionManagement
public class MybatisConfig {
	
	@Bean(name="mybatisSqlSessionFactory")
	public SqlSessionFactory mybatisSqlSessionFactory(DataSource dataSource, ApplicationContext context) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		
		sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setMapperLocations(context.getResources("classpath:mapper/**/*.xml"));
		sqlSessionFactoryBean.setTypeAliasesPackage("ksmart.mybatis.dto");
		
		return sqlSessionFactoryBean.getObject();
	}
	
	public SqlSessionTemplate mybatisSqlSessionTemplate(@Qualifier("mybatisSqlSessionFactory") SqlSessionFactory mybatisSqlSessionFactory) {
		return new SqlSessionTemplate(mybatisSqlSessionFactory);
	}
	
	public PlatformTransactionManager transactionManager(DataSource datasource) {
		return new DataSourceTransactionManager(datasource);
	}
}
