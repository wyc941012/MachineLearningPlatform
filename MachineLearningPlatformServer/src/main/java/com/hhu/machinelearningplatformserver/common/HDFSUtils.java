package com.hhu.machinelearningplatformserver.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HDFSUtils {

	private static FileSystem fileSystem;
	private static String HDFS_URI;
	private static final Logger logger=LoggerFactory.getLogger(HDFSUtils.class);
	
	public static void init() throws Exception {
		//读取HDFS地址
		Configuration conf=new Configuration();
		String hdfsIP=ConfigUtils.getValueByName("HDFS_IP");
		String hdfsPort=ConfigUtils.getValueByName("HDFS_PORT");
		String hdfsUser=ConfigUtils.getValueByName("HDFS_USER");
		HDFS_URI="hdfs://"+hdfsIP+":"+hdfsPort+ConfigUtils.getValueByName("HDFS_URI");
		URI uri;
		try {
			uri=new URI("hdfs://"+hdfsIP+":"+hdfsPort);
			fileSystem=FileSystem.get(uri, conf, hdfsUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	public static FileSystem getFileSystem() throws Exception {
		if(fileSystem!=null) {
			return fileSystem;
		}
		else {
			init();
			return fileSystem;
		}
	}
	
	//创建目录
	public static void createDirectory(String path) throws Exception {
		// TODO Auto-generated method stub
		String realPath=HDFS_URI+path;
		Path hdfsPath=new Path(realPath);
		try {
			if(fileSystem.exists(hdfsPath)) {
				logger.debug("目录已存在！");
			}
			else {
				fileSystem.mkdirs(hdfsPath);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		} 
	}
	
	//删除文件目录
	public static void delete(String path) throws Exception {
		String realPath=HDFS_URI+path;
		Path hdfsPath=new Path(realPath);
		try {
			if(!fileSystem.exists(hdfsPath)) {
				logger.debug("目录不存在！");
			}
			else {
				fileSystem.delete(hdfsPath,true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	//获取目录下的所有文件
	public static FileStatus[] list(String path) throws FileNotFoundException, IOException {
		String realPath=HDFS_URI+path;
		Path hdfsPath=new Path(realPath);
		FileStatus[] lists=null;
		if(!fileSystem.exists(hdfsPath)) {
			logger.info("目录不存在！");
		}
		else if(fileSystem.isFile(hdfsPath)) {
			logger.info("不是目录！");
		}
		else {
			lists=fileSystem.listStatus(hdfsPath);
		}
		return lists;
	}
	
	//上传文件
	public static void copyFileToHDFS(boolean delSrc, boolean overwrite,String srcFile,String destPath) throws IOException {
		Path srcPath=new Path(srcFile);
		Path hdfsPath=new Path(HDFS_URI+destPath);
		if(!fileSystem.exists(hdfsPath)) {
			logger.debug("目录不存在！");
			return;
		}
		fileSystem.copyFromLocalFile(delSrc, overwrite, srcPath, hdfsPath);
	}
	
	//下载文件
	public static void getFile(String srcFile, String destPath) throws IOException {
		Path srcPath=new Path(HDFS_URI+srcFile);
		Path destFile=new Path(destPath);
		if(!fileSystem.exists(srcPath)) {
			logger.debug("源文件不存在！");
			return;
		}
		fileSystem.copyToLocalFile(srcPath, destFile);
	}
	
	//判断目录或文件是否存在
	public static boolean existDir(String filePath) throws IOException {
		if(StringUtils.isEmpty(filePath)) {
			return false;
		}
		Path path=new Path(filePath);
		if(!fileSystem.exists(path)) {
			logger.debug("文件或目录不存在！");
			return false;
		}
		else {
			return true;
		}
	}
	
	//重命名
	public static void rename(String srcPath, String dstPath) throws IOException {
		srcPath=HDFS_URI+srcPath;
		dstPath=HDFS_URI+dstPath;
		Path src=new Path(srcPath);
		Path dst=new Path(dstPath);
		if(!fileSystem.exists(src)) {
			logger.debug("文件或目录不存在！");
			return;
		}
		fileSystem.rename(src, dst);
	}
	
	//获得HDFS节点信息
	public static DatanodeInfo[] getHDFSNodes() throws IOException {
		//获取所有节点
		DatanodeInfo[] dataNodeStats=new DatanodeInfo[0];
		//获取分布式文件系统
		DistributedFileSystem hdfs=(DistributedFileSystem) fileSystem;
		dataNodeStats=hdfs.getDataNodeStats();
		return dataNodeStats;
	}
	
	public static void close() throws IOException {
		fileSystem.close();
	}
	
}
