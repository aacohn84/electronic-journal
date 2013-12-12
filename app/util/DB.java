package util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DB {
	public static void printResultSetColumnNames(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i < columnCount; i++) {
			System.out.print(metaData.getColumnName(i) + ", ");
		}
		System.out.print(metaData.getColumnName(columnCount) + "\n");
	}
}
