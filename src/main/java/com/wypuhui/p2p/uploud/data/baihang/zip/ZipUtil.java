package com.wypuhui.p2p.uploud.data.baihang.zip;

import com.wypuhui.p2p.uploud.data.baihang.response.FileUploadResponse;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * 解压缩工具类
 *
 */
public class ZipUtil {

	/**
	 * 编码格式
	 */
	public final static String encoding = "UTF-8";

	/**
	 * 压缩文件
	 * @param srcfile（待压缩文件，必须是后缀为txt的文件，不能是文件夹）
	 * @param targetFile（压缩后的文件，必须是后缀为zip的文件，不能是文件夹）
	 * @return
	 */
	 public static FileUploadResponse zipFile(File srcFile, File targetFile) {
		 FileUploadResponse response = new FileUploadResponse();
		 if(srcFile==null || targetFile==null){
			 response.setSuccess(false);
			 response.setErrorMessage("待压缩文件或压缩后的文件不能为空!");
			 return response;
		 }
		 if(!srcFile.exists() || srcFile.isDirectory()){
			 response.setSuccess(false);
			 response.setErrorMessage("待压缩文件不存在或不能是文件夹!");
			 return response;
		 }
		 if(!srcFile.getName().endsWith(".txt") || !targetFile.getName().endsWith(".zip")){
			 response.setSuccess(false);
			 response.setErrorMessage("待压缩文件后缀必须为txt，压缩后的文件后缀必须为zip!");
			 return response;
		 }
		 if(targetFile.exists()){
			 targetFile.delete();
		 }

		 Project proj = new Project();
		 FileSet fileSet = new FileSet();
		 fileSet.setProject(proj);
		 fileSet.setFile(srcFile);

		 Zip zip = new Zip();
		 zip.setProject(proj);
		 zip.setDestFile(targetFile);
		 zip.addFileset(fileSet);
		 zip.setEncoding(encoding);
		 zip.execute();
		 return response;
    }


	 /**
	 * 解压文件
	 * @param zipfile（压缩文件，必须是后缀为zip的文件，不能是文件夹）
	 * @param unzipFilePath（解压后文件的相对路径）
	 * @return
	 */
	 public static FileUploadResponse unzipFile(File zipFile,String unzipFilePath) {
		 FileUploadResponse response = new FileUploadResponse();
		 if(!zipFile.exists()){
		 	 response.setSuccess(false);
			 response.setErrorMessage("压缩文件不存在!");
			 return response;
		 }
		 if(!zipFile.getName().endsWith(".zip")){
		 	 response.setSuccess(false);
			 response.setErrorMessage("压缩文件后缀必须为zip");
			 return response;
		 }
		 Project proj = new Project();
		 Expand expand = new Expand();
		 expand.setProject(proj);
		 expand.setTaskType("unzip");
		 expand.setTaskName("unzip");
		 expand.setEncoding(encoding);

		 expand.setSrc(zipFile);
		 expand.setDest(new File(unzipFilePath));
		 expand.execute();
		 return response;

    }

	public static void toZip(File source, OutputStream out) throws IOException {
		ZipOutputStream zipOutputStream = null;
		try {
			zipOutputStream = new ZipOutputStream(out);
			byte[] bytes = new byte[4096];
			zipOutputStream.putNextEntry(new ZipEntry(source.getName()));
			int len;
			FileInputStream fileInputStream = new FileInputStream(source);
			while ((len = fileInputStream.read(bytes)) != -1) {
				zipOutputStream.write(bytes, 0, len);
			}
			fileInputStream.close();
		} catch (Exception e) {
			throw new RuntimeException("压缩文件异常", e);
		} finally {
			if (zipOutputStream != null) {
				zipOutputStream.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	// 文件字节转字符串
	public static String byteToString(File file) throws Exception {
		StringBuilder returnDatas = new StringBuilder();
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] buf = new byte[10 * 1024];
		int readLenth = 0;
		while ((readLenth = fileInputStream.read(buf)) != -1) {
			returnDatas.append(new String(buf,0,readLenth));
		}
		fileInputStream.close();
		return returnDatas.toString();
	}

}
