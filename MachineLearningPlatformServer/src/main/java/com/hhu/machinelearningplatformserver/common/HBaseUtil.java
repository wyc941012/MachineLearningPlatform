package com.hhu.machinelearningplatformserver.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseUtil {

	private static HBaseUtil hBaseUtil=new HBaseUtil();
	
	private Connection connection;
	private static final String ZOOKEEPER_QUORUM="10.196.83.90,10.196.83.91,10.196.83.92";
	private static final String ZOOKEEPER_CLIENTPORT="2181";
	private static final String HBASE_ROOTDIR="hdfs://10.196.83.90:9000/hbase";
	private static final String RETRIES_NUMBER="3";
	
	public static HBaseUtil getInstance() {
		return hBaseUtil;
	}
	
	//连接HBase
	public void connection() throws IOException {
		Configuration conf=HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", ZOOKEEPER_QUORUM);
	    conf.set("hbase.zookeeper.property.clientPort", ZOOKEEPER_CLIENTPORT);//端口号
	    conf.set("hbase.rootdir", HBASE_ROOTDIR);
	    conf.set("hbase.client.retries.number", RETRIES_NUMBER);	    
	    Connection connection=ConnectionFactory.createConnection(conf);
	    this.connection=connection;
	    //this.table=(HTable) connection.getTable(TableName.valueOf(TABLE_NAME));
	}
	
	//建表
	public void createTable(String tableName, String familyName) throws IOException {
		HBaseAdmin admin=null;
		try {
			admin=(HBaseAdmin) connection.getAdmin();
			if(admin.tableExists(tableName)) {
				System.out.println("表已存在！");
				return;
			}
			HTableDescriptor descriptor=new HTableDescriptor(TableName.valueOf(tableName));
			HColumnDescriptor columnDescriptor=new HColumnDescriptor(Bytes.toBytes(familyName));
			descriptor.addFamily(columnDescriptor);
			admin.createTable(descriptor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			admin.close();
		}
	}
	
	/**
     * 插入数据
     *
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param qualifierName
     * @param value
     * @throws Exception
     */
    public void putData(String tableName, String rowKey, String familyName, String qualifierName, String value)
            throws Exception {
    	HTable table=(HTable) connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(qualifierName), Bytes.toBytes(value));
        table.put(put);
        table.close();
    }
    
    /**
     * 根据rowkey 查询
     *
     * @param tableName
     * @param rowKey
     * @return
     * @throws Exception
     */
    public Result getResult(String tableName, String rowKey) throws Exception {
    	HTable table=(HTable) connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);
        table.close();
        return result;
    }
    
    /**
     * 查询指定的某列
     *
     * @param tableName
     * @param rowKey
     * @param familyName
     * @param qualifierName
     * @return
     * @throws Exception
     */
    public String getValue(String tableName, String rowKey, String familyName, String qualifierName) throws Exception {
    	HTable table=(HTable) connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowKey));
        Result result = table.get(get);
        Cell cell = result.getColumnLatestCell(Bytes.toBytes(familyName), Bytes.toBytes(qualifierName));
        if (cell == null) {
            return null;
        }
        table.close();
        return Bytes.toString(CellUtil.cloneValue(cell));
    }
    
    //遍历表数据
    public List<Result> getAllData(String tableName) throws IOException {
    	HTable table=(HTable) connection.getTable(TableName.valueOf(tableName));
    	Scan scan=new Scan();
    	ResultScanner resultScanner=table.getScanner(scan);
    	List<Result> lists=new ArrayList<Result>();
    	for(Result result : resultScanner) {
    		lists.add(result);
    	}
    	table.close();
    	return lists;
    }
    
    /**
     * 根据Row的前缀获得value
     *
     * @param tableName
     * @param rowPrefix
     * @param familyName
     * @param qualifierName
     * @return
     * @throws Exception
     */
    public List<String> getValueByRowPrefix(String tableName, String rowPrefix, String familyName, String qualifierName) throws Exception {
    	HTable table=(HTable) connection.getTable(TableName.valueOf(tableName));
        List<String> values = new ArrayList<>();

        Scan scan = new Scan();
        scan.setFilter(new PrefixFilter(Bytes.toBytes(rowPrefix)));
        table.getScanner(scan).forEach((result) -> {
            Cell cell = result.getColumnLatestCell(Bytes.toBytes(familyName), Bytes.toBytes(qualifierName));
            if (cell != null) {
                values.add(Bytes.toString(CellUtil.cloneValue(cell)));
            }
        });
        table.close();
        return values;
    }
    
    
    /**
     * 删除指定某列
     *
     * @param tableName
     * @param rowKey
     * @param falilyName
     * @param qualifierName
     * @throws Exception
     */
    public void deleteColumn(String tableName, String rowKey, String falilyName, String qualifierName) throws Exception {
    	HTable table=(HTable) connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        delete.addColumn(Bytes.toBytes(falilyName), Bytes.toBytes(qualifierName));
        table.delete(delete);
        table.close();
    }

    /**
     * 删除指定的某个rowkey
     *
     * @param tableName
     * @param rowKey
     * @throws Exception
     */
    public void deleteColumn(String tableName, String rowKey) throws Exception {
    	HTable table=(HTable) connection.getTable(TableName.valueOf(tableName));
        Delete delete = new Delete(Bytes.toBytes(rowKey));
        table.delete(delete);
        table.close();
    }

    /**
     * 删除表
     *
     * @param tableName
     * @throws Exception
     */
    public void dropTable(String tableName) throws Exception {
    	HBaseAdmin admin=(HBaseAdmin) connection.getAdmin();
        admin.disableTable(TableName.valueOf(tableName));
        admin.deleteTable(TableName.valueOf(tableName));
        admin.close();
    }
	
	//关闭HBase连接
	public void close() throws IOException {
		//table.close();
		connection.close();
	}
}
