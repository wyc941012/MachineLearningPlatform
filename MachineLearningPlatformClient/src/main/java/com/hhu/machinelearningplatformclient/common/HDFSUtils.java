package com.hhu.machinelearningplatformclient.common;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.protocol.DatanodeInfo;

public class HDFSUtils {

	private FileSystem fileSystem;
	private String HDFS_URI;
	
	private String hdfsIP;
	private String hdfsPort;
	private String hdfsUser;
	private String hdfsUri;
	
	public String getHdfsIP() {
		return hdfsIP;
	}

	public void setHdfsIP(String hdfsIP) {
		this.hdfsIP = hdfsIP;
	}

	public String getHdfsPort() {
		return hdfsPort;
	}

	public void setHdfsPort(String hdfsPort) {
		this.hdfsPort = hdfsPort;
	}

	public String getHdfsUser() {
		return hdfsUser;
	}

	public void setHdfsUser(String hdfsUser) {
		this.hdfsUser = hdfsUser;
	}
	
	public String getHdfsUri() {
		return hdfsUri;
	}

	public void setHdfsUri(String hdfsUri) {
		this.hdfsUri = hdfsUri;
	}

	@PostConstruct
	public void init() throws Exception {
		//读取HDFS地址
		Configuration conf=new Configuration();;
		HDFS_URI="hdfs://"+hdfsIP+":"+hdfsPort+hdfsUri;
		URI uri;
		try {
			uri=new URI("hdfs://"+hdfsIP+":"+hdfsPort);
			fileSystem=FileSystem.get(uri, conf, hdfsUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw e;
		}
	}
	
	public FileSystem getFileSystem() throws Exception {
		if(fileSystem!=null) {
			return fileSystem;
		}
		else {
			init();
			return fileSystem;
		}
	}
	
	//创建目录
	public void createDirectory(String path) throws Exception {
		// TODO Auto-generated method stub
		String realPath=HDFS_URI+path;
		Path hdfsPath=new Path(realPath);
		try {
			if(fileSystem.exists(hdfsPath)) {
				System.out.println("目录已存在！");
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
	public void delete(String path) throws Exception {
		String realPath=HDFS_URI+path;
		Path hdfsPath=new Path(realPath);
		try {
			if(!fileSystem.exists(hdfsPath)) {
				System.out.println("目录不存在！");
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
	public FileStatus[] list(String path) throws FileNotFoundException, IOException {
		String realPath=HDFS_URI+path;
		Path hdfsPath=new Path(realPath);
		FileStatus[] lists=null;
		if(!fileSystem.exists(hdfsPath)) {
			System.out.println("目录不存在！");
		}
		else if(fileSystem.isFile(hdfsPath)) {
			System.out.println("不是目录！");
		}
		else {
			lists=fileSystem.listStatus(hdfsPath);
		}
		return lists;
	}
	
	//上传文件
	public void copyFileToHDFS(boolean delSrc, boolean overwrite,String srcFile,String destPath) throws IOException {
		Path srcPath=new Path(srcFile);
		Path hdfsPath=new Path(HDFS_URI+destPath);
		if(!fileSystem.exists(hdfsPath)) {
			System.out.println("目录不存在！");
			return;
		}
		fileSystem.copyFromLocalFile(delSrc, overwrite, srcPath, hdfsPath);
	}
	
	//上传文件(使用输入流的方式)
	public void uploadFileStream(boolean overwrite, InputStream inputStream, String destPath) throws IllegalArgumentException, IOException {
		FSDataOutputStream outputStream=fileSystem.create(new Path(HDFS_URI+destPath), overwrite);
		IOUtils.copy(inputStream, outputStream);
	}
	
	//下载文件
	public void getFile(String srcFile, String destPath) throws IOException {
		Path srcPath=new Path(HDFS_URI+srcFile);
		Path destFile=new Path(destPath);
		if(!fileSystem.exists(srcPath)) {
			System.out.println("源文件不存在！");
			return;
		}
		fileSystem.copyToLocalFile(srcPath, destFile);
	}
	
	//判断目录或文件是否存在
	public boolean existDir(String filePath) throws IOException {
		if(StringUtils.isEmpty(filePath)) {
			return false;
		}
		Path path=new Path(filePath);
		if(!fileSystem.exists(path)) {
			System.out.println("文件或目录不存在！");
			return false;
		}
		else {
			return true;
		}
	}
	
	//重命名
	public void rename(String srcPath, String dstPath) throws IOException {
		srcPath=HDFS_URI+srcPath;
		dstPath=HDFS_URI+dstPath;
		Path src=new Path(srcPath);
		Path dst=new Path(dstPath);
		if(!fileSystem.exists(src)) {
			System.out.println("文件或目录不存在！");
			return;
		}
		fileSystem.rename(src, dst);
	}
	
	//获得HDFS节点信息
	public DatanodeInfo[] getHDFSNodes() throws IOException {
		//获取所有节点
		DatanodeInfo[] dataNodeStats=new DatanodeInfo[0];
		//获取分布式文件系统
		DistributedFileSystem hdfs=(DistributedFileSystem) fileSystem;
		dataNodeStats=hdfs.getDataNodeStats();
		return dataNodeStats;
	}
	
	public void close() throws IOException {
		fileSystem.close();
	}
	
}
