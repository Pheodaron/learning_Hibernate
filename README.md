# Hibernate
ORM — (англ. Object-relational mapping). В двух словах ORM — это отображение объектов какого-либо объектно-ориентированного языка в структуры реляционных баз данных.

Hibernate - это инструмент ORM на основе Java, который обеспечивает структуру для сопоставления POJO с таблицами реляционной базы данных и наоборот.

JPA - это спецификация для управления реляционными данными в приложениях.

### Архитектура
    
Hibernate, как ORM-решение, эффективно «находится между» уровнем доступа к данным Java-приложения и реляционной базой данных. Приложение Java использует API-интерфейсы Hibernate для загрузки, хранения, запроса и т.д.
  + SessionFactory(org.hibernate.SessionFactory)
    
    Поточно-ориентированное (и неизменяемое) представление сопоставления модели домена приложения с базой данных. Действует как фабрика для экземпляров org.hibernate.Session . EntityManagerFactory является JPA эквивалентом SessionFactory и в основном, те две сходятся в одном SessionFactory реализации.
    
  + Session(org.hibernate.Session)
    
    Однопоточный, недолговечный объект, концептуально моделирующий «единицу работы». Hibernate является оболочкой для java.sql.Connection и действует как фабрика для экземпляров org.hibernate.Transaction.
    
  + Transaction(org.hibernate.Transaction)

    Однопоточный краткосрочный объект используется приложением для определения границ отдельных физических транзакций. Действует, как API абстракции, чтобы изолировать приложение от используемой базовой системы транзакций (JDBC или JTA).
    
    
#### Шаги для подключения hibernate (Maven)
1. Добавить зависимости (как минимум hibernate-core и JDBC драйвер базы данных) в pom.xml
2. Создать классы сущности(JPA Entity Class)
3. Создать файл конфигурации hibernate.cfg.xml
4. Создать hibernate util класс.
    
#### Создание Entity класса
Для того, чтобы пометить POJO класс Entity аннотацией он должен соответсвовать нескольким условиям:
+ иметь конструктор без параметров
+ необходимо назначить первичный ключ, т.е. пометить его аннотацией @Id
+ объявить геттеры и сеттеры
+ класс не должен быть final или иметь final методы

#### Пример hibernate.cfg.xml
```
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <!-- JDBC Database connection settings -->
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.url">jdbc:mysql://localhost:3306/hibernate_db?useSSL=false</property>
        <property name="connection.username">root</property>
        <property name="connection.password">root</property>
        <!-- JDBC connection pool settings ... using built-in test pool -->
        <property name="connection.pool_size">1</property>
        <!-- Select our SQL dialect -->
        <property name="dialect">org.hibernate.dialect.MySQL5Dialect</property>
        <!-- Echo the SQL to stdout -->
        <property name="show_sql">true</property>
        <!-- Set the current session context -->
        <property name="current_session_context_class">thread</property>
        <!-- Drop and re-create the database schema on startup -->
        <property name="hbm2ddl.auto">create-drop</property>
        <!-- dbcp connection pool configuration -->
        <property name="hibernate.dbcp.initialSize">5</property>
        <property name="hibernate.dbcp.maxTotal">20</property>
        <property name="hibernate.dbcp.maxIdle">10</property>
        <property name="hibernate.dbcp.minIdle">5</property>
        <property name="hibernate.dbcp.maxWaitMillis">-1</property>
        <mapping class="net.javaguides.hibernate.entity.Student" />
    </session-factory>
</hibernate-configuration>
```
#### Hibernate utils
Создание utils класса подразумевает три этапа:
+ Создать объект класса StandartServiceRegistry
+ Создать объект класса Metadata
+ использовать их для создания SessionFactory
