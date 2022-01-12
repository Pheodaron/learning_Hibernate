# Hibernate

https://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html

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


### Транзакции
Транзакция — это совокупность операций над базой данных, которые вместе образуют логически целостную процедуру, и могут быть либо выполнены все вместе, либо не будет выполнена ни одна из них.
Транзакции являются одним из средств обеспечения согласованности базы данных. Они переводят БД из одного согласованного состояния в другое.

Транзакция может иметь два исхода: 
+ Изменение данных, произведенные в ходе ее выполнения, успешно зафиксированны в базе данных
+ транзакция отменяется, и отменяются все изменения, выполненные в ее рамках (rollback)

    Реализация транзакций в СУБД PostgreSQL основана на многоверсионной модели(Multiversion Concurensy Control). Каждый SQL-оператор видит так называемый snapshot, то есть согласованное остояние (версию) БД, которую она имела на определенный момент времени. Параллельные транзакции не наршуют согласованности данных этого snapshot'a. Когда параллельные транзакции изменяют одни и те же строки таблиц , тогда создаются отдельные версии этих строк, доступные соответсвующим транзакциям. Также MVCC никогда не блокируются операциями записи, и наоборот.

Согласно теории БД транзакции должны обладать следующими свойствами:
+ Атомарность (atomicity). Это свойство означает, что либо транзакция будет зафиксирована в базе данных полностью, т. е. будут зафиксированы результаты выполнения всех ее операций, либо не будет зафиксирована ни одна операция транзакции.
+  Согласованность (consistency). Это свойство предписывает, чтобы в результате успешного выполнения транзакции база данных была переведена из одного согласованного состояния в другое согласованное состояние.
+  Изолированность (isolation). Во время выполнения транзакции другие транзакции должны оказывать по возможности минимальное влияние на нее.
+  Долговечность (durability). После успешной фиксации транзакции пользователь должен быть уверен, что данные надежно сохранены в базе данных и впоследствии могут быть извлечены из нее, независимо от последующих возможных сбоев в работе системы.

При параллельном выполнении транзакций возможны следующие феномены:
+ Потерянное обновление (lost update). Когда разные транзакции одновременно изменяют одни и те же данные, то после фиксации изменений может оказаться, что одна транзакция перезаписала данные, обновленные и зафиксированные другой транзакцией.
+ «Грязное» чтение (dirty read). Транзакция читает данные, измененные параллельной транзакцией, которая еще не завершилась. Если эта параллельная транзакция в итоге будет отменена, тогда окажется, что первая транзакция прочитала данные, которых нет в системе.
+ Неповторяющееся чтение (non-repeatable read). При повторном чтении тех же самых данных в рамках одной транзакции оказывается, что другая транзакция успела изменить и зафиксировать эти данные. В результате тот же самый запрос выдает другой результат.
+ Фантомное чтение (phantom read). Транзакция повторно выбирает множество строк в соответствии с одним и тем же критерием. В интервале времени между выполнением этих выборок другая транзакция добавляет новые строки и успешно фиксирует изменения. В результате при выполнении повторной выборки в первой транзакции может быть получено другое множество строк.
+ Аномалия сериализации (serialization anomaly). Результат успешной фиксации группы транзакций, выполняющихся параллельно, не совпадает с результатом ни одного из возможных вариантов упорядочения этих транзакций, если бы они выполнялись последовательно.

Сериализация двух транзакций при их параллельном выполнении означает, что полученный результат будет соответствовать одному из двух возможных вариантов упорядочения транзакций при их последовательном выполнении. При этом нельзя сказать точно, какой из вариантов будет реализован.
Если СУБД не сможет гарантировать успешную сериализацию группы параллельных транзакций, тогда некоторые из них могут быть завершены с ошибкой. Эти транзакции придется выполнить повторно.
Для конкретизации степени не зависимости параллельных транзакций вводится понятие уровня изоляции транзакций. Каждый уровень характеризуется перечнем тех феноменов, которые на данном уровне не допускаются.

Всего в стандарте SQL предусмотрено четыре уровня. Каждый более высокий уровень включает в себя все возможности предыдущего.
+ Read Uncommitted. Это самый низкий уровень изоляции. Согласно стандарту SQL на этом уровне допускается чтение «грязных» (незафиксированных) данных.
+ Read Uncommitted. Это самый низкий уровень изоляции. Согласно стандарту SQL на этом уровне допускается чтение «грязных» (незафиксированных) данных. Транзакция может видеть только те незафиксированные изменения данных, которые произведены в ходе выполнения ее самой.
+ Repeatable Read. Не допускается чтение «грязных» (незафиксированных) данных и неповторяющееся чтение.
+ Serializable. Не допускается ни один из феноменов, перечисленных выше, в том числе и аномалии сериализации.

#### Transactions в Hibernate
В hibernate есть Transaction interface, который определяет единицу работы. Он представляет из себя абстракцию более высокого уровня реализации транзакций JTA, JDBC.
Transaction входит в Session и её можно получить с помощью метода beginTransaction()
Методы Transaction interface:
- void begin()
- void commit()
- void rollback()
- void setTimeout(int seconds)
- void isAlive()
- void registerSynchronization(Synchronization s)
- boolean wasCommited()
- boolean wasRolledBack()

https://docs.jboss.org/hibernate/orm/3.2/api/org/hibernate/Transaction.html
