dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
    dialect = org.hibernate.dialect.MySQL5InnoDBDialect // must be set for transactions to work!
    dbCreate = "update"
    url = "jdbc:mysql://localhost:3306/3d_viewer?autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8"
    username = "root"
    password = "igdefault"
    properties {
        maxActive = 50
        maxIdle = 25
        minIdle = 10
        initialSize = 20
        numTestsPerEvictionRun = 3
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = true
        validationQuery = "SELECT 1"
        minEvictableIdleTimeMillis = (1000 * 60 * 5)
        timeBetweenEvictionRunsMillis = (1000 * 60 * 5)
    }
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {

    development {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost:3306/3d_viewer?autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8"
        }
    }

    production {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost:3306/3d_viewer?autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8"
        }
    }

    qa {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost:3306/3d_viewer?autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8"
        }
    }

    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:mysql://localhost:3306/3d_viewer?autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8"
        }
    }
}
