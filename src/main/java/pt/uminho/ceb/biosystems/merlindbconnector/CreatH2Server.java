package pt.uminho.ceb.biosystems.merlindbconnector;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import javax.swing.JComboBox;

import org.h2.jdbcx.JdbcConnectionPool;

import pt.uminho.sysbio.common.database.connector.datatypes.DatabaseAccess;
import pt.uminho.sysbio.common.database.connector.datatypes.Enumerators.DatabaseType;


public class CreatH2Server implements DatabaseAccess, Externalizable{
	
	private static final long serialVersionUID = 1L;
	private String database_host, database_port, database_name, database_user, database_password;
	private Connection connection;
	public JComboBox<String> checkDataServerType;
	
	public CreatH2Server() {
		super();
	}

	public CreatH2Server(String user, String password, String database_name){
		this.database_name=database_name;
		this.database_user=user;
		this.database_password=password;
	}
	
	public Connection openConnection() throws SQLException {
		JdbcConnectionPool connect = JdbcConnectionPool.create("jdbc:h2:tcp://192.168.1.142:9020//D:\\projects\\merlin\\merlin-core\\h2Database\\optest","root","password");
		this.connection= connect.getConnection();
		return this.connection;
	}
	
	
	public boolean closeConnection() {
		// TODO Auto-generated method stub
		return false;
	}

	public void closeConnection(java.sql.Connection arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public void closeStatement(Statement arg0) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	public boolean creatTable(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean delete(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String describeTable(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String describeTablePK(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean dropTable(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean existTable(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	public String fieldsTable(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getMeta(String table, pt.uminho.sysbio.common.database.connector.datatypes.Connection connection) throws SQLException {

		String[] res = null;

		try {
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet results = null;
			results = meta.getColumns(null, null, table, null) ;
			LinkedList<String> names = new LinkedList<String>();

			while (results.next()) {
				names.add(results.getString("COLUMN_NAME"));
			}

			res = new String[names.size()];
			for(int i=0;i<names.size();i++) res[i] = names.get(i);
		}

		catch (SQLException ex) {
			ex.printStackTrace();
		}

		return res;
	}

	public String get_database_host() {
		return database_name;
	}

	public String get_database_name() {
		// TODO Auto-generated method stub
		return null;
	}

	public String get_database_password() {
		// TODO Auto-generated method stub
		return null;
	}

	public String get_database_port() {		
		return database_port;
	}

	public DatabaseType get_database_type() {
		// TODO Auto-generated method stub
		return null;
	}

	public String get_database_user() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean insert(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean insertInto(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	public PreparedStatement prepareStatement(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public void readExternal(ObjectInput arg0) throws IOException, ClassNotFoundException {
		// TODO Auto-generated method stub
		
	}

	public String[][] select(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public ResultSet selectRS(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDatabase_host(String database_host) {
		this.database_name = database_host;
		
	}

	public void setDatabase_password(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setDatabase_port(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setDatabase_user(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public String[] showTables() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean update(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	public void writeExternal(ObjectOutput arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) throws SQLException {
		CreatH2Server conectar = new CreatH2Server("root","password","nome123");
		conectar.openConnection();
}

	public String get_database_path() {
		// TODO Auto-generated method stub
		return null;
	}

}


/*
public Connection openConnection() throws SQLException {
	String path = new File(FileUtils.getCurrentDirectory()).getParentFile().getParent();
	PooledConnection connect = this.dataSource.getPooledConnection();
	this.connection=JdbcConnectionPool.create("jdbc:h2:tcp://"+ AminhaProjectGUI.ipField.getText() +":"+ AminhaProjectGUI.portField.getText() +"/data/h2/test","sa", "sa");
	return this.connection;
}
JdbcConnectionPool connect = JdbcConnectionPool.create("jdbc:h2:"+path+"/h2Database/"+this.database_name+";MODE=MySQL;DATABASE_TO_UPPER=FALSE;MODE=MySQL;DATABASE_TO_UPPER=FALSE;mv_store=false;AUTO_SERVER=TRUE;AUTO_SERVER_PORT=9090",this.database_user,this.database_password);
JdbcConnectionPool connect = JdbcConnectionPool.create("jdbc:h2:"+path+"/h2Database/"+this.database_name+";MODE=MySQL;DATABASE_TO_UPPER=FALSE;MODE=MySQL;DATABASE_TO_UPPER=FALSE;",this.database_user,this.database_password);
}
*/
//JdbcConnectionPool.create

/*
public void ConnectServer() throws SQLException {	
	checkDataServerType =  AminhaProjectGUI.dataBaseTypeBox;
	if (checkDataServerType.getSelectedItem()=="H2"){
		openConnection();
	}
	if (checkDataServerType.getSelectedItem()=="MySQL"){
		
	}	
}


public void MySQLDatabaseAccess() {	
	this.dataSource = new MysqlConnectionPoolDataSource();
}
*/	

