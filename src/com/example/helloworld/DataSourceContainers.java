package com.example.helloworld;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;


public class DataSourceContainers {
	private JDBCConnectionPool connectionPool = null;
	private SQLContainer kontrahContainer = null;
	private SQLContainer firmyContainer = null;
	private SQLContainer userContainer = null;

	public DataSourceContainers() {
		this.initConnectionPool();
		this.initDataBase();
		this.initContainers();
		this.fillContainers();
	}
	
	private void initConnectionPool() {
		try {
//			connectionPool  = new SimpleJDBCConnectionPool(
//					"org.firebirdsql.jdbc.FBDriver",
//					"jdbc:firebirdsql:localhost/3050:c:/TOP_BAZY/E_FIRMA.GDB", 
//					"sysdba",
//					"masterkey");
//			this.connectionPool = new SimpleJDBCConnectionPool(
//					"org.postgresql.Driver", 
//					"jdbc:postgresql://localhost:5432/vaadin",
//					"postgres", 
//					"top", 2, 5);
            connectionPool = new SimpleJDBCConnectionPool(
                    "org.hsqldb.jdbc.JDBCDriver",
                    "jdbc:hsqldb:mem:sqlcontainer", "SA", "", 2, 5);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void initDataBase() {
		try {
            Connection conn = connectionPool.reserveConnection();
            Statement statement = conn.createStatement();
            try {
                statement.executeQuery("SELECT * FROM KONTRAH");
                statement.executeQuery("SELECT * FROM FIRMY");
            } catch (SQLException e) {
                /*
                 * Failed, which means that the database is not yet initialized
                 * => Create the tables
                 */
                statement.execute("CREATE TABLE firmy ("+
                				  "ID INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0, INCREMENT BY 1) not null, "+
                				  "NAZWA VARCHAR(50) )");
                statement.execute("alter table firmy add primary key (id)");
                
                statement.execute("CREATE TABLE kontrah (" +
								  "ID INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0, INCREMENT BY 1) not null, " + 
								  "IMIE VARCHAR(50), " +
								  "NAZWISKO VARCHAR(50), " +
								  "WIEK integer, " +
								  "PESEL VARCHAR(11), " +
								  "ID_FIRMA INT )");
                statement.execute("alter table kontrah add primary key (id)");
                
                statement.execute("CREATE TABLE user ("+
      				  "ID INT GENERATED BY DEFAULT AS IDENTITY (START WITH 0, INCREMENT BY 1) not null, "+
      				  "LOGIN VARCHAR(50), " +
      				  "PASSWORD VARCHAR(50), " +
      				  "ROLE VARCHAR(50) )");
                statement.execute("alter table user add primary key (id)");
            }
            statement.close();
            conn.commit();
            connectionPool.releaseConnection(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	private void initContainers() {
		try {			
			//FirebirdGenerator generator = new FirebirdGenerator(FirebirdGenerator.Version.V2_5);
			//TableQuery tqKontrah = new TableQuery("TEST_B", connectionPool, generator);
			TableQuery tqKontrah = new TableQuery("kontrah", connectionPool);
			//tqKontrah.setVersionColumn("ID");
			kontrahContainer = new SQLContainer(tqKontrah);
			
			TableQuery tqFirmy = new TableQuery("firmy", connectionPool);
			//tqKontrah.setVersionColumn("ID");
			firmyContainer = new SQLContainer(tqFirmy);
			
			TableQuery tqUser = new TableQuery("user", connectionPool);
			userContainer = new SQLContainer(tqUser);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void fillContainers() {
		if (kontrahContainer.size() == 0 && firmyContainer.size() == 0 && userContainer.size() == 0) {
			// Fill firmaContainer
			final String firma[] = { "Firma 1", "Firma 2", "Firma 3" };
			for (int i = 0; i < firma.length; i++) {
				Object id = firmyContainer.addItem();
				firmyContainer.getContainerProperty(id, "NAZWA").setValue(firma[i]);
			}
			
			// Fill kontrahContainer
			final String imie[] = { "Lukasz", "Tomasz", "Adam" };
			final String nazwisko[] = { "Kowalski", "Nowak", "Polaczek" };
			final int wiek[] = { 25, 30, 53 }; 
			final String pesel[] = { "87022102035", "82060106012", "67022412456" };
			final int firmaId[] = { 0, 1, 2 };
			
			for (int j = 0; j < 30; j ++) {
				for (int i = 0; i < imie.length; i++) {
					Object id = kontrahContainer.addItem();
					kontrahContainer.getContainerProperty(id, "IMIE").setValue(imie[i]);
					kontrahContainer.getContainerProperty(id, "NAZWISKO").setValue(nazwisko[i]);
					kontrahContainer.getContainerProperty(id, "WIEK").setValue(wiek[i]);
					kontrahContainer.getContainerProperty(id, "PESEL").setValue(pesel[i]);
					kontrahContainer.getContainerProperty(id, "ID_FIRMA").setValue(firmaId[i]);
				}
			}
			
			// Fill userContainer
			final String login[] = { "admin", "moderator" };
			final String password[] = { "admin", "moderator" };
			final String role[] = { "admin", "moderator" };
			for (int i = 0; i < login.length; i++) {
				Object id = userContainer.addItem();
				userContainer.getContainerProperty(id, "LOGIN").setValue(login[i]);
				userContainer.getContainerProperty(id, "PASSWORD").setValue(password[i]);
				userContainer.getContainerProperty(id, "ROLE").setValue(role[i]);
			}
			
			try {
				firmyContainer.commit();
				kontrahContainer.commit();
				userContainer.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public SQLContainer getKontrahContainer() {
		return kontrahContainer;
	}
	
	public SQLContainer getFirmyContainer() {
		return firmyContainer;
	}
	
	public SQLContainer getUserContainer() {
		return userContainer;
	}
	
	public SQLContainer getFreeFormQueryContainer(String sql) {
		FreeformQuery query = new FreeformQuery(sql, connectionPool);
		SQLContainer freeFormQuerySQLContainer;
		try {
			freeFormQuerySQLContainer = new SQLContainer(query);
			return freeFormQuerySQLContainer;
		} catch (SQLException e) {
			return null;
		}
	}
	
    /**
     * Fetches a city name based on its key.
     * 
     * @param cityId
     *            Key
     * @return City name
     */
    public String getFirmaNazwa(int firmaId) {
        Object firmaItemId = this.firmyContainer.getIdByIndex(firmaId);
        String firmaString = firmyContainer.getItem(firmaItemId).getItemProperty("NAZWA").getValue().toString();
        return firmaString;
    }
}
