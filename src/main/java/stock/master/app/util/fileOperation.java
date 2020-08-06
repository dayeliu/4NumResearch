package stock.master.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class fileOperation {
	
	public static void compressFile(String sourceFile, String targetDir, String zipName) {
		
		File srcFile = new File(sourceFile);
		if (!srcFile.exists()) {
			//LogService.error("FUNC", "zipFile", 3, "File not exist. naem : " + sourceFile, ErrorCode.FILE_OPERATE_IO_ERROR);
			return;
		}
		
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		try {
			fos = new FileOutputStream(new File(targetDir, zipName));
			zos = new ZipOutputStream(fos);
			
			zos.putNextEntry(new ZipEntry(srcFile.getName()));
			addFileToZip(srcFile, zos);
			zos.closeEntry();
			
			zos.finish();
			zos.close();
			fos.flush();
		} catch (IOException e) {
			//LogService.error("FUNC", "zipFile", 3, "Exception: " + e.toString(), ErrorCode.FILE_OPERATE_IO_ERROR);
			System.out.println(e.toString());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				//LogService.error("FUNC", "zipFile", 3, "Exception: " + e.toString(), ErrorCode.FILE_OPERATE_IO_ERROR);
				System.out.println(e.toString());
			}
		}
		
		return;
	}
	
	public static void compressDirectory(String sourceDir, String targetDir, String zipName) {
		
		File srcDir = new File(sourceDir);
		
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		try {
			fos = new FileOutputStream(new File(targetDir, zipName));
			zos = new ZipOutputStream(fos);
			
			addFolderToZip(srcDir, zos, srcDir);
			
			zos.finish();
			zos.close();
			fos.flush();
		} catch (IOException e) {
			System.out.println(e.toString());
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				//LogService.error("FUNC", "zipFile", 3, "Exception: " + e.toString(), ErrorCode.FILE_OPERATE_IO_ERROR);
				System.out.println(e.toString());
			}
		}
		
		
		return ;
	}
	
	private static void addFileToZip(File srcFile, ZipOutputStream zos) {

		byte[] b = new byte[(int) srcFile.length()];

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(srcFile);
			
			int l;
			while ((l = fis.read(b)) != -1) {
				zos.write(b, 0, l);
			}
			
		} catch (IOException e) {
			//LogService.error("FUNC", "closeFile", 3, "Exception: " + e.toString(), ErrorCode.FILE_OPERATE_IO_ERROR);
			System.out.println(e.toString());
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				System.out.println(e.toString());
			}
			
			b = null;
		}
	}
	
	private static void addFolderToZip (File srcDir, ZipOutputStream zos, File rootDir) {
		
		try {
			for (File tmp : srcDir.listFiles()) {
				
				String name = getRelativePath(rootDir, tmp);
				if (tmp.isDirectory()) {

					name = name.endsWith("/") ? name : name + "/";
					zos.putNextEntry(new ZipEntry(name));
					addFolderToZip(tmp, zos, rootDir);
				} else {
					zos.putNextEntry(new ZipEntry(name));
					addFileToZip(tmp, zos);
					zos.closeEntry();
				}
			}
		} catch (IOException e) {
			System.out.println(e.toString());
		}
	}
	
	private static String getRelativePath(File srcDir, File targetFile) {
		
		final int rootLength = srcDir.getAbsolutePath().length();
		final String absFileName = targetFile.getAbsolutePath();
		final String relFileName = absFileName.substring(rootLength + 1);
	    
	    return relFileName;
	}
}
